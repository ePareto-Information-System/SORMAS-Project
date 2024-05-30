/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EditPermissionType;
import de.symeda.sormas.api.RequestContextHolder;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.document.DocumentRelatedEntityType;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.share.ExternalShareStatus;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.common.*;
import de.symeda.sormas.backend.document.DocumentService;
import de.symeda.sormas.backend.externalsurveillancetool.ExternalSurveillanceToolGatewayFacadeEjb;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.share.ExternalShareInfo;
import de.symeda.sormas.backend.share.ExternalShareInfoCountAndLatestDate;
import de.symeda.sormas.backend.share.ExternalShareInfoService;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfoFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfoService;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class EbsService extends AbstractCoreAdoService<Ebs, EbsJoins> {

	@EJB
	private CaseService caseService;
	@EJB
	private UserService userService;
	@EJB
	private SormasToSormasShareInfoService sormasToSormasShareInfoService;
	@EJB
	private SormasToSormasFacadeEjb.SormasToSormasFacadeEjbLocal sormasToSormasFacade;
	@EJB
	private SormasToSormasShareInfoFacadeEjb.SormasToSormasShareInfoFacadeEjbLocal sormasToSormasShareInfoFacade;
	@EJB
	private ExternalShareInfoService externalShareInfoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private EbsFacadeEjb.EbsFacadeEjbLocal ebsFacade;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjb.ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolGatewayFacade;

	public EbsService() {
		super(Ebs.class);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate createRelevantDataFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, Ebs> from) {

		Predicate filter = createActiveEbssFilter(cb, from);

		User user = getCurrentUser();
		if (user != null) {
			EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
			ebsUserFilterCriteria.forceRegionJurisdiction(true);

			EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, from);
			filter = CriteriaBuilderHelper.and(cb, filter, createUserFilter(ebsQueryContext, ebsUserFilterCriteria));
		}

		return filter;
	}

	@Override
	protected List<String> referencesToBeFetched() {
		return Arrays.asList(Ebs.EBS_LOCATION);
	}

	public List<String> getAllActiveUuids() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Ebs> from = cq.from(getElementClass());

		EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, from);

		Predicate filter = createActiveEbssFilter(cb, from);

		User user = getCurrentUser();
		if (user != null) {
			EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
			ebsUserFilterCriteria.forceRegionJurisdiction(true);

			Predicate userFilter = createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}

		if (RequestContextHolder.isMobileSync()) {
			Predicate predicate = createLimitedChangeDateFilter(cb, from);
			if (predicate != null) {
				filter = CriteriaBuilderHelper.and(cb, filter, predicate);
			}
		}

		cq.where(filter);
		cq.select(from.get(Ebs.UUID));
		cq.distinct(true);

		return em.createQuery(cq).getResultList();
	}

	public Map<Disease, Long> getEbsCountByDisease(EbsCriteria ebsCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		Predicate filter = createDefaultFilter(cb, ebs);
		filter = CriteriaBuilderHelper.and(cb, filter, buildCriteriaFilter(ebsCriteria, ebsQueryContext));
		filter = CriteriaBuilderHelper.and(cb, filter, createUserFilter(ebsQueryContext));

		if (filter != null)
			cq.where(filter);

		List<Object[]> results = em.createQuery(cq).getResultList();

		return results.stream().collect(Collectors.toMap(e -> (Disease) e[0], e -> (Long) e[1]));
	}


	@Override
	public void archive(String entityUuid, Date endOfProcessingDate) {
		super.archive(entityUuid, endOfProcessingDate);
		setArchiveInExternalSurveillanceToolForEntity(entityUuid, true);
	}

	@Override
	public void archive(List<String> entityUuids) {
		super.archive(entityUuids);
		setArchiveInExternalSurveillanceToolForEntities(entityUuids, true);
	}

	@Override
	public void dearchive(List<String> entityUuids, String dearchiveReason) {
		super.dearchive(entityUuids, dearchiveReason);
		setArchiveInExternalSurveillanceToolForEntities(entityUuids, false);
	}

	public void setArchiveInExternalSurveillanceToolForEntities(List<String> entityUuids, boolean archived) {
		if (externalSurveillanceToolGatewayFacade.isFeatureEnabled()) {
			List<String> sharedEbsUuids = getSharedEbsUuids(entityUuids);
			if (!sharedEbsUuids.isEmpty()) {
				try {
					externalSurveillanceToolGatewayFacade.sendEbsInternal(sharedEbsUuids, archived);
				} catch (ExternalSurveillanceToolException e) {
					throw new ExternalSurveillanceToolRuntimeException(e.getMessage(), e.getErrorCode());
				}
			}
		}
	}

	public List<String> getSharedEbsUuids(List<String> entityUuids) {
		List<Long> ebsIds = getEbsIds(entityUuids);
		List<String> sharedEbsUuids = new ArrayList<>();
		List<ExternalShareInfoCountAndLatestDate> ebsShareInfos =
			externalShareInfoService.getShareCountAndLatestDate(ebsIds, ExternalShareInfo.EBS);
		ebsShareInfos.forEach(shareInfo -> {
			if (shareInfo.getLatestStatus() != ExternalShareStatus.DELETED) {
				sharedEbsUuids.add(shareInfo.getAssociatedObjectUuid());
			}
		});

		return sharedEbsUuids;
	}

	public List<Long> getEbsIds(List<String> entityUuids) {
		List<Long> ebsIds = new ArrayList<>();
		entityUuids.forEach(uuid -> ebsIds.add(this.getByUuid(uuid).getId()));
		return ebsIds;
	}

	public void setArchiveInExternalSurveillanceToolForEntity(String ebsUuid, boolean archived) {
		setArchiveInExternalSurveillanceToolForEntities(Collections.singletonList(ebsUuid), archived);
	}

	public List<String> getArchivedUuidsSince(Date since) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
		ebsUserFilterCriteria.forceRegionJurisdiction(true);

		Predicate filter = createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(ebs.get(Ebs.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate archivedFilter = cb.equal(ebs.get(Ebs.ARCHIVED), true);
		if (filter != null) {
			filter = cb.and(filter, archivedFilter);
		} else {
			filter = archivedFilter;
		}

		cq.where(filter);
		cq.select(ebs.get(Ebs.UUID));

		List<String> resultList = em.createQuery(cq).getResultList();
		return resultList;
	}

	public List<String> getDeletedUuidsSince(Date since) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<String> cq = cb.createQuery(String.class);
		final Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
		ebsUserFilterCriteria.forceRegionJurisdiction(true);

		Predicate filter = createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(ebs.get(Ebs.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate deletedFilter = cb.equal(ebs.get(Ebs.DELETED), true);
		if (filter != null) {
			filter = cb.and(filter, deletedFilter);
		} else {
			filter = deletedFilter;
		}

		cq.where(filter);
		cq.select(ebs.get(Ebs.UUID));

		return em.createQuery(cq).getResultList();
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate createUserFilterInternal(CriteriaBuilder cb, CriteriaQuery cq, From<?, Ebs> from) {
		return createUserFilter(new EbsQueryContext(cb, cq, from));
	}

	@Override
	protected EbsJoins toJoins(From<?, Ebs> adoPath) {
		return new EbsJoins(adoPath);
	}

	public Predicate createUserFilter(EbsQueryContext queryContext) {
		return createUserFilter(queryContext, null);
	}

	public Predicate createUserFilter(final EbsQueryContext queryContext, final EbsUserFilterCriteria ebsUserFilterCriteria) {

		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return null;
		}
		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		Predicate filter = null;

		@SuppressWarnings("rawtypes")
		final CriteriaQuery cq = queryContext.getQuery();
		final CriteriaBuilder cb = queryContext.getCriteriaBuilder();
		final EbsJoins ebsJoins = queryContext.getJoins();
		final From<?, Ebs> ebsJoin = queryContext.getRoot();

		if (RequestContextHolder.isMobileSync()) {
			Predicate limitedChangeDatePredicate = CriteriaBuilderHelper.and(cb, createLimitedChangeDateFilter(cb, ebsJoin));
			if (limitedChangeDatePredicate != null) {
				filter = CriteriaBuilderHelper.and(cb, filter, limitedChangeDatePredicate);
			}
		}

		return filter;
	}

	@Override
	public Predicate createChangeDateFilter(CriteriaBuilder cb, From<?, Ebs> ebsPath, Timestamp date) {
		return addChangeDates(new ChangeDateFilterBuilder(cb, date), toJoins(ebsPath), false).build();
	}

	public Predicate createChangeDateFilter(CriteriaBuilder cb, EbsJoins joins, Expression<? extends Date> dateExpression) {
		return addChangeDates(new ChangeDateFilterBuilder(cb, dateExpression), joins, false).build();
	}

	@Override
	protected <T extends ChangeDateBuilder<T>> T addChangeDates(T builder, EbsJoins joins, boolean includeExtendedChangeDateFilters) {

		final From<?, Ebs> ebsFrom = joins.getRoot();
		builder = super.addChangeDates(builder, joins, includeExtendedChangeDateFilters).add(ebsFrom, Ebs.EBS_LOCATION);

		if (includeExtendedChangeDateFilters) {
			builder.add(ebsFrom, Ebs.SORMAS_TO_SORMAS_ORIGIN_INFO)
				.add(ebsFrom, Ebs.SORMAS_TO_SORMAS_SHARES);
		}

		return builder;
	}

	@Override
	public void delete(Ebs ebs, DeletionDetails deletionDetails) {
		deleteEbsInExternalSurveillanceTool(ebs);

		// Mark the ebs as deleted
		super.delete(ebs, deletionDetails);
	}

	@Override
	public void restore(Ebs ebs) {
		super.restore(ebs);
	}

	@Override
	public void deletePermanent(Ebs ebs) {

		externalShareInfoService.getShareInfoByEbs(ebs.getUuid()).forEach(e -> {
			externalShareInfoService.deletePermanent(e);
		});

		documentService.getRelatedToEntity(DocumentRelatedEntityType.EVENT, ebs.getUuid()).forEach(d -> documentService.markAsDeleted(d));

		super.deletePermanent(ebs);
	}


	private void deleteEbsInExternalSurveillanceTool(Ebs ebs) {
		try {
			ebsFacade.deleteEbsInExternalSurveillanceTool(ebs);
		} catch (ExternalSurveillanceToolException e) {
			throw new ExternalSurveillanceToolRuntimeException(e.getMessage(), e.getErrorCode());
		}
	}

	public List<Ebs> getByExternalId(String externalId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ebs> cq = cb.createQuery(Ebs.class);
		Root<Ebs> ebsRoot = cq.from(Ebs.class);

		cq.where(cb.equal(ebsRoot.get(Ebs.EXTERNAL_ID), externalId), cb.equal(ebsRoot.get(Ebs.DELETED), Boolean.FALSE));

		return em.createQuery(cq).getResultList();
	}

	public Predicate buildCriteriaFilter(EbsCriteria ebsCriteria, EbsQueryContext ebsQueryContext) {

		CriteriaBuilder cb = ebsQueryContext.getCriteriaBuilder();
		From<?, Ebs> from = ebsQueryContext.getRoot();
		final EbsJoins joins = ebsQueryContext.getJoins();

		Predicate filter = null;
		if (ebsCriteria.getRiskLevel() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.RISK_LEVEL), ebsCriteria.getRiskLevel()));
		}

		if (ebsCriteria.getReportDateTime() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.REPORT_DATE_TIME), ebsCriteria.getReportDateTime()));
		}
		if (ebsCriteria.getTriagingDecision() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.TRIAGE_DATE), ebsCriteria.getTriageDate()));
		}
		if (ebsCriteria.getTriagingDto() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.TRIAGING), ebsCriteria.getTriagingDto()));
		}
		if (ebsCriteria.getSignalVerificationDto() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.SIGNAL_VERIFICATION), ebsCriteria.getSignalVerificationDto()));
		}

		if (ebsCriteria.getSourceInformation() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.SOURCE_INFORMATION), ebsCriteria.getSourceInformation()));
		}
		if (ebsCriteria.getTriagingDecision() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Ebs.TRIAGING_DECISION), ebsCriteria.getTriagingDecision()));
		}
		if (CollectionUtils.isNotEmpty(ebsCriteria.getExcludedUuids())) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.not(from.get(AbstractDomainObject.UUID).in(ebsCriteria.getExcludedUuids())));
		}


		filter = CriteriaBuilderHelper.and(
			cb,
			filter,
			externalShareInfoService.buildShareCriteriaFilter(
				ebsCriteria,
				ebsQueryContext.getQuery(),
				cb,
				from,
				ExternalShareInfo.EBS,
				(latestShareDate) -> createChangeDateFilter(cb, joins, latestShareDate)));

		return filter;
	}

	/**
	 * Creates a filter that excludes all ebss that are either {@link Ebs#isArchived()} or {@link DeletableAdo#isDeleted()}.
	 */
	public Predicate createActiveEbssFilter(CriteriaBuilder cb, Root<Ebs> root) {
		return cb.and(cb.isFalse(root.get(Ebs.ARCHIVED)), cb.isFalse(root.get(Ebs.DELETED)));
	}

	/**
	 * Creates a filter that excludes all ebss that are either {@link Ebs#isArchived()} or {@link DeletableAdo#isDeleted()}.
	 */
	public Predicate createActiveEbssFilter(CriteriaBuilder cb, Path<Ebs> root) {
		return cb.and(cb.isFalse(root.get(Ebs.ARCHIVED)), cb.isFalse(root.get(Ebs.DELETED)));
	}

	/**
	 * Creates a default filter that should be used as the basis of queries that do not use {@link EbsCriteria}.
	 * This essentially removes {@link DeletableAdo#isDeleted()} ebss from the queries.
	 */
	public Predicate createDefaultFilter(CriteriaBuilder cb, From<?, Ebs> root) {
		return cb.isFalse(root.get(Ebs.DELETED));
	}


	@Override
	public EditPermissionType getEditPermissionType(Ebs ebs) {

		if (!inJurisdictionOrOwned(ebs)) {
			return EditPermissionType.OUTSIDE_JURISDICTION;
		}

		return super.getEditPermissionType(ebs);
	}

	public boolean inJurisdiction(Ebs ebs) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
		Root<Ebs> root = cq.from(Ebs.class);
		cq.multiselect(JurisdictionHelper.booleanSelector(cb, inJurisdiction(new EbsQueryContext(cb, cq, root), userService.getCurrentUser())));
		cq.where(cb.equal(root.get(Ebs.UUID), ebs.getUuid()));
		return em.createQuery(cq).getResultList().stream().anyMatch(aBoolean -> aBoolean);
	}

	public Predicate inJurisdiction(EbsQueryContext qc, User user) {
		return EbsJurisdictionPredicateValidator.of(qc, user).inJurisdiction();
	}

	@Override
	public Predicate inJurisdictionOrOwned(CriteriaBuilder cb, CriteriaQuery<?> cq, From<?, Ebs> from) {
		return inJurisdictionOrOwned(new EbsQueryContext(cb, cq, from));
	}

	public Predicate inJurisdictionOrOwned(EbsQueryContext qc) {
		return inJurisdictionOrOwned(qc, getCurrentUser());
	}

	public Predicate inJurisdictionOrOwned(EbsQueryContext qc, User user) {
		return EbsJurisdictionPredicateValidator.of(qc, user).inJurisdictionOrOwned();
	}

	@Transactional(rollbackOn = Exception.class)
	public void updateExternalData(List<ExternalDataDto> externalData) throws ExternalDataUpdateException {
		ExternalDataUtil.updateExternalData(externalData, this::getByUuids, this::ensurePersisted);
	}

	public List<Ebs> getAllByCase(String caseUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ebs> cq = cb.createQuery(getElementClass());
		Root<Ebs> from = cq.from(getElementClass());
		from.fetch(Ebs.EBS_LOCATION);

		EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, from);
		EbsJoins joins = ebsQueryContext.getJoins();

		Predicate filter = createActiveEbssFilter(cb, from);

		User user = getCurrentUser();
		if (user != null) {
			Predicate userFilter = createUserFilter(ebsQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}
		cq.where(filter);
		cq.distinct(true);

		return em.createQuery(cq).getResultList();
	}

	public String getUuidByCaseUuidOrPersonUuid(String searchTerm) {

		if (StringUtils.isEmpty(searchTerm)) {
			return null;
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Ebs> root = cq.from(Ebs.class);
		EbsJoins joins = new EbsJoins(root);

		cq.orderBy(cb.desc(root.get(Ebs.REPORT_DATE_TIME)));
		cq.select(root.get(Ebs.UUID));

		return QueryHelper.getFirstResult(em, cq);
	}
	public boolean hasRegionAndDistrict(String ebsUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> from = cq.from(getElementClass());
		Join<Ebs, Location> locationJoin = from.join(Ebs.EBS_LOCATION, JoinType.LEFT);

		cq.where(
			CriteriaBuilderHelper.and(
				cb,
				cb.equal(from.get(AbstractDomainObject.UUID), ebsUuid),
				cb.isNotNull(locationJoin.get(Location.REGION)),
				cb.isNotNull(locationJoin.get(Location.DISTRICT))));
		cq.select(cb.count(from));

		return em.createQuery(cq).getSingleResult() > 0;
	}


	@Override
	protected boolean hasLimitedChangeDateFilterImplementation() {
		return true;
	}

}
