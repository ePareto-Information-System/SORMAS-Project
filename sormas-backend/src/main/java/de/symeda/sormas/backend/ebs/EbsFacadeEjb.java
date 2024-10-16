/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.common.CoreEntityType;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.deletionconfiguration.DeletionReference;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.sormastosormas.ShareTreeCriteria;
import de.symeda.sormas.api.sormastosormas.SormasToSormasException;
import de.symeda.sormas.api.sormastosormas.SormasToSormasRuntimeException;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.AccessDeniedException;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.FacadeHelper;
import de.symeda.sormas.backend.common.AbstractCoreFacadeEjb;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.externalsurveillancetool.ExternalSurveillanceToolGatewayFacadeEjb.ExternalSurveillanceToolGatewayFacadeEjbLocal;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb.CommunityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.country.CountryFacadeEjb.CountryFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb.DistrictFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.location.LocationFacadeEjb;
import de.symeda.sormas.backend.location.LocationFacadeEjb.LocationFacadeEjbLocal;
import de.symeda.sormas.backend.share.ExternalShareInfoService;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeEjb.SormasToSormasFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoService;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;

@Stateless(name = "EbsFacade")
public class EbsFacadeEjb extends AbstractCoreFacadeEjb<Ebs, EbsDto, EbsIndexDto, EbsReferenceDto, EbsService, EbsCriteria>
		implements EbsFacade {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private UserService userService;
	@EJB
	private LocationFacadeEjbLocal locationFacade;
	@EJB
	private SormasToSormasOriginInfoService originInfoService;
	@EJB
	private CountryFacadeEjbLocal countryFacade;
	@EJB
	private DistrictFacadeEjbLocal districtFacade;
	@EJB
	private CommunityFacadeEjbLocal communityFacade;
	@EJB
	private FeatureConfigurationFacadeEjbLocal featureConfigurationFacade;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolFacade;
	@EJB
	private ExternalShareInfoService externalShareInfoService;
	@EJB
	private SormasToSormasFacadeEjbLocal sormasToSormasFacade;
	@EJB
	private EbsService ebsService;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolGatewayFacade;
	@EJB
	private TriagingFacadeEjb.TriagingFacadeEjbLocal triagingFacade;
	@EJB
	private SignalVerificationFacadeEjb.SignalVerificationFacadeEjbLocal signalVerificationFacade;
	@EJB
	private RiskAssessmentFacadeEjb.RiskAssessmentFacadeEjbLocal riskAssessmentFacade;
	@EJB
	private RiskAssessmentService riskAssessmentService;
	@EJB
	private EbsAlertService ebsAlertService;

	public EbsFacadeEjb() {
	}

	@Inject
	public EbsFacadeEjb(EbsService service) {
		super(Ebs.class, EbsDto.class, service);
	}

	public static EbsReferenceDto toReferenceDto(Ebs entity) {

		if (entity == null) {
			return null;
		}

		return new EbsReferenceDto(entity.getUuid(), getCaption(entity));
	}

	private static String getCaption(Ebs entity) {
		return EbsReferenceDto.buildCaption(
				entity.getTriageDate());
	}

	public static EbsReferenceDto toDetailedReferenceDto(Ebs entity) {

		if (entity == null) {
			return null;
		}

		return new EbsDetailedReferenceDto(
				entity.getUuid(),
				getCaption(entity),
				entity.getReportDateTime());
	}
	@Override
	public EbsDto toDto(Ebs source) {
		return toEbsDto(source);
	}


	public static EbsDto toEbsDto(Ebs source) {

		if (source == null) {
			return null;
		}
		EbsDto target = new EbsDto();
		DtoHelper.fillDto(target, source);

		target.setInformantName(source.getInformantName());
		target.setInformantTel(source.getInformantTel());
		target.setReportDateTime(source.getReportDateTime());
		target.setCategoryOfInformant((source.getCategoryOfInformant()));
		target.setEbsLocation(LocationFacadeEjb.toDto(source.getEbsLocation()));
		target.setAutomaticScanningType(source.getAutomaticScanningType());
		target.setManualScanningType(source.getManualScanningType());
		target.setScanningType(source.getScanningType());
		target.setDescriptionOccurrence(source.getDescriptionOccurrence());
		target.setOther(source.getOther());
		target.setPersonDesignation(source.getPersonDesignation());
		target.setPersonPhone(source.getPersonPhone());
		target.setPersonRegistering(source.getPersonRegistering());
		target.setSourceName(source.getSourceName());
		target.setSourceInformation(source.getSourceInformation());

		target.setDateOnset(source.getDateOnset());
		target.setEbsLongitude(source.getEbsLongitude());
		target.setEbsLatitude(source.getEbsLongitude());
		target.setEbsLatLon(source.getEbsLatLon());
		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setTriaging(TriagingFacadeEjb.toDto(source.getTriaging()));
		target.setSignalVerification(SignalVerificationFacadeEjb.toDto(source.getSignalVerification()));
		target.setOtherInformant(source.getOtherInformant());
		return target;
	}

	@Override
	public List<String> getAllActiveUuids() {

		User user = userService.getCurrentUser();
		if (user == null) {
			return Collections.emptyList();
		}

		return service.getAllActiveUuids();
	}

	@Override
	public List<EbsDto> getAllByCase(CaseDataDto caseDataDto) {
		return toDtos(service.getAllByCase(caseDataDto.getUuid()).stream());
	}

	@Override
	public List<String> getDeletedUuidsSince(Date since) {

		User user = userService.getCurrentUser();
		if (user == null) {
			return Collections.emptyList();
		}

		return service.getDeletedUuidsSince(since);
	}

	@PermitAll
	public Map<Disease, Long> getEbsCountByDisease(EbsCriteria ebsCriteria) {

		return service.getEbsCountByDisease(ebsCriteria);
	}

	@Override
	public EbsDto getEbsByUuid(String uuid, boolean detailedReferences) {
		return (detailedReferences)
				? convertToDetailedReferenceDto(service.getByUuid(uuid), createPseudonymizer())
				: toPseudonymizedDto(service.getByUuid(uuid));
	}

	@Override
	public EbsReferenceDto getReferenceByUuid(String uuid) {
		return toReferenceDto(service.getByUuid(uuid));
	}

	@Override
	public EbsDto save(@Valid @NotNull EbsDto dto) {
		return save(dto, true, true);
	}


	public EbsDto save(@NotNull EbsDto dto, boolean checkChangeDate, boolean internal) {

		Ebs existingEbs = dto.getUuid() != null ? service.getByUuid(dto.getUuid()) : null;
		FacadeHelper.checkCreateAndEditRights(existingEbs, userService, UserRight.EVENT_CREATE, UserRight.EVENT_EDIT);

		if (internal && existingEbs != null && !service.isEditAllowed(existingEbs)) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.errorEventNotEditable));
		}

		EbsDto existingDto = toDto(existingEbs);

		Pseudonymizer pseudonymizer = createPseudonymizer();
		restorePseudonymizedDto(dto, existingDto, existingEbs, pseudonymizer);

		validate(dto);
		Ebs ebs = fillOrBuildEntity(dto, existingEbs, checkChangeDate);
		service.ensurePersisted(ebs);

		onEventChange(toDto(ebs), internal);

		return toPseudonymizedDto(ebs, pseudonymizer);
	}

	@PermitAll
	public void onEventChange(EbsDto ebs, boolean syncShares) {
		if (syncShares && sormasToSormasFacade.isFeatureConfigured()) {
			syncSharesAsync(new ShareTreeCriteria(ebs.getUuid()));
		}
	}
	
	public void syncSharesAsync(ShareTreeCriteria criteria) {
//		executorService.schedule(() -> {
//			sormasToSormasEventFacade.syncShares(criteria);
//		}, 5, TimeUnit.SECONDS);
	}

	@Override
	public void delete(String ebsUuid, DeletionDetails deletionDetails)
			throws ExternalSurveillanceToolRuntimeException, SormasToSormasRuntimeException {
		Ebs ebs = service.getByUuid(ebsUuid);
		deleteEbs(ebs, deletionDetails);
	}

	private void deleteEbs(Ebs ebs, DeletionDetails deletionDetails)
			throws ExternalSurveillanceToolRuntimeException, SormasToSormasRuntimeException, AccessDeniedException {

		if (!ebsService.inJurisdictionOrOwned(ebs)) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.messageEventOutsideJurisdictionDeletionDenied));
		}

		try {
			sormasToSormasFacade.revokePendingShareRequests(ebs.getSormasToSormasShares(), true);
		} catch (SormasToSormasException e) {
			throw new SormasToSormasRuntimeException(e);
		}

		service.delete(ebs, deletionDetails);
	}

	@Override
	@RightsAllowed(UserRight._EVENT_DELETE)
	public List<String> delete(List<String> uuids, DeletionDetails deletionDetails) {
		List<String> deletedEbsUuids = new ArrayList<>();
		List<Ebs> ebsToBeDeleted = service.getByUuids(uuids);
		if (ebsToBeDeleted != null) {
			ebsToBeDeleted.forEach(ebstoBeDeleted -> {
				if (!ebstoBeDeleted.isDeleted()) {
					try {
						deleteEbs(ebstoBeDeleted, deletionDetails);
						deletedEbsUuids.add(ebstoBeDeleted.getUuid());
					} catch (ExternalSurveillanceToolRuntimeException | SormasToSormasRuntimeException | AccessDeniedException e) {
						logger.error("The event with uuid:" + ebstoBeDeleted.getUuid() + "could not be deleted");
					}
				}
			});
		}
		return deletedEbsUuids;
	}

	@Override
	@RightsAllowed(UserRight._EVENT_DELETE)
	public void restore(String uuid) {
		super.restore(uuid);
	}

	@Override
	protected void pseudonymizeDto(Ebs source, EbsDto dto, Pseudonymizer pseudonymizer) {

	}

	@Override
	@RightsAllowed(UserRight._EVENT_DELETE)
	public List<String> restore(List<String> uuids) {
		List<String> restoredEbssUuids = new ArrayList<>();
		List<Ebs> ebsToBeRestored = ebsService.getByUuids(uuids);

		if (ebsToBeRestored != null) {
			ebsToBeRestored.forEach(ebstoBeDeleted -> {
				try {
					restore(ebstoBeDeleted.getUuid());
					restoredEbssUuids.add(ebstoBeDeleted.getUuid());
				} catch (Exception e) {
					logger.error("The event with uuid: " + ebstoBeDeleted.getUuid() + " could not be restored");
				}
			});
		}
		return restoredEbssUuids;
	}

	@RightsAllowed({
			UserRight._EVENT_DELETE,
			UserRight._SYSTEM })
	public void deleteEbsInExternalSurveillanceTool(Ebs ebs) throws ExternalSurveillanceToolException {

		if (externalSurveillanceToolGatewayFacade.isFeatureEnabled() && StringUtils.isNotBlank(ebs.getExternalId())) {
			List<Ebs> ebsWithSameExternalId = service.getByExternalId(ebs.getExternalId());
			if (ebsWithSameExternalId != null
					&& ebsWithSameExternalId.size() == 1
					&& externalSurveillanceToolFacade.isFeatureEnabled()
					&& externalShareInfoService.isEbsShared(ebs.getId())) {
				externalSurveillanceToolGatewayFacade.deleteEbsInternal(Collections.singletonList(toDto(ebs)));
			}
		}
	}

	@Override
	public long count(EbsCriteria ebsCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		EbsQueryContext queryContext = new EbsQueryContext(cb, cq, ebs);

		Predicate filter = null;

		if (ebsCriteria != null) {
			if (ebsCriteria.getUserFilterIncluded()) {
				EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
				filter = service.createUserFilter(queryContext, ebsUserFilterCriteria);
			}

			Predicate criteriaFilter = service.buildCriteriaFilter(ebsCriteria, queryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.countDistinct(ebs));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<EbsIndexDto> getIndexList(EbsCriteria ebsCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		List<Long> indexListIds = getIndexListIds(ebsCriteria, first, max, sortProperties);
		List<EbsIndexDto> indexList = new ArrayList<>();
		Set<Long> addedIds = new HashSet<>();
		CriteriaBuilder cb = em.getCriteriaBuilder();

		IterableHelper.executeBatched(indexListIds, ModelConstants.PARAMETER_LIMIT, batchedIds -> {
			CriteriaQuery<EbsIndexDto> cq = cb.createQuery(EbsIndexDto.class);
			Root<Ebs> ebs = cq.from(Ebs.class);
			EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);
			EbsJoins ebsJoins = ebsQueryContext.getJoins();

			Join<Ebs, Triaging> triaging = ebs.join("triaging", JoinType.LEFT);
			Join<Ebs, SignalVerification> signalVerification = ebs.join("signalVerification", JoinType.LEFT);
			Join<Ebs, Location> location = ebsJoins.getLocation();
			Join<Location, Region> region = ebsJoins.getRegion();
			Join<Location, District> district = ebsJoins.getDistrict();
			Join<Location, Community> community = ebsJoins.getCommunity();

			// Subquery for latest ebsAlert
			Subquery<Long> subqueryAlert = cq.subquery(Long.class);
			Root<EbsAlert> subRootAlert = subqueryAlert.from(EbsAlert.class);
			subqueryAlert.select(cb.max(subRootAlert.get(EbsAlert.ID)))
					.where(cb.equal(subRootAlert.get(EbsAlert.EBS), ebs));

			Join<Ebs, EbsAlert> ebsAlert = ebs.join("ebsAlert", JoinType.LEFT);
			Predicate alertPredicate = cb.or(cb.isNull(ebsAlert.get(EbsAlert.ID)), cb.equal(ebsAlert.get(EbsAlert.ID), subqueryAlert));

			// Subquery for latest riskAssessment
			Subquery<Long> subqueryRisk = cq.subquery(Long.class);
			Root<RiskAssessment> subRootRisk = subqueryRisk.from(RiskAssessment.class);
			subqueryRisk.select(cb.max(subRootRisk.get(RiskAssessment.ID)))
					.where(cb.equal(subRootRisk.get(RiskAssessment.EBS), ebs));

			Join<Ebs, RiskAssessment> riskAssessment = ebs.join("riskAssessment", JoinType.LEFT);
			Predicate riskPredicate = cb.or(cb.isNull(riskAssessment.get(RiskAssessment.ID)), cb.equal(riskAssessment.get(RiskAssessment.ID), subqueryRisk));

			// Create the selection
			cq.multiselect(
					ebs.get(Ebs.ID),
					ebs.get(Ebs.UUID),
					ebs.get(Ebs.TRIAGE_DATE),
					ebs.get(Ebs.SOURCE_INFORMATION),
					triaging.get(Triaging.TRIAGING_DECISION),
					ebs.get(Ebs.REPORT_DATE_TIME),
					ebs.get(Ebs.CATEGORY_OF_INFORMANT),
					ebs.get(Ebs.INFORMANT_NAME),
					ebs.get(Ebs.INFORMANT_TEL),
					triaging.get(Triaging.SIGNAL_CATEGORY),
					signalVerification.get(SignalVerification.VERIFIED),
					signalVerification.get(SignalVerification.NUMBER_OF_DEATH),
					triaging.get(Triaging.DATE_OF_DECISION),
					ebs.get(Ebs.PERSON_REGISTERING),
					ebs.get(Ebs.PERSON_DESIGNATION),
					signalVerification.get(SignalVerification.VERIFICATION_SENT),
					signalVerification.get(SignalVerification.VERIFICATION_SENT_DATE),
					signalVerification.get(SignalVerification.VERIFICATION_COMPLETE_DATE),
					riskAssessment.get(RiskAssessment.RISK_ASSESSMENT),
					ebsAlert.get(EbsAlert.ACTION_INITIATED),
					ebsAlert.get(EbsAlert.RESPONSE_STATUS),
					region.get(Region.UUID),
					region.get(Region.NAME),
					community.get(Community.UUID),
					community.get(Community.NAME),
					location.get(Location.CITY),
					district.get(District.UUID),
					district.get(District.NAME)
			);

			Predicate filter = ebs.get(Ebs.ID).in(batchedIds);

			if (ebsCriteria != null) {
				if (ebsCriteria.getUserFilterIncluded()) {
					EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
					filter = service.createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
				}

				Predicate criteriaFilter = service.buildCriteriaFilter(ebsCriteria, ebsQueryContext);
				filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
			}

			if (filter != null) {
				cq.where(cb.and(filter, alertPredicate, riskPredicate));
			} else {
				cq.where(cb.and(alertPredicate, riskPredicate));
			}

			sortBy(sortProperties, ebsQueryContext);
			cq.distinct(true);

			List<EbsIndexDto> results = QueryHelper.getResultList(em, cq, null, null);

			for (EbsIndexDto dto : results) {
				if (addedIds.add(dto.getId())) {
					indexList.add(dto);
				}
			}
		});
		return indexList;
	}

	@Override
	public List<EbsIndexDto> getEventIndexList(EbsCriteria ebsCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		List<Long> indexListIds = getIndexListIds(ebsCriteria, first, max, sortProperties);
		List<EbsIndexDto> indexList = new ArrayList<>();
		Set<Long> addedIds = new HashSet<>();
		CriteriaBuilder cb = em.getCriteriaBuilder();

		IterableHelper.executeBatched(indexListIds, ModelConstants.PARAMETER_LIMIT, batchedIds -> {
			CriteriaQuery<EbsIndexDto> cq = cb.createQuery(EbsIndexDto.class);
			Root<Ebs> ebs = cq.from(Ebs.class);
			EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);
			EbsJoins ebsJoins = ebsQueryContext.getJoins();

			Join<Ebs, Triaging> triaging = ebs.join("triaging", JoinType.LEFT);
			Join<Ebs, SignalVerification> signalVerification = ebs.join("signalVerification", JoinType.LEFT);
			Join<Ebs, Location> location = ebsJoins.getLocation();
			Join<Location, Region> region = ebsJoins.getRegion();
			Join<Location, District> district = ebsJoins.getDistrict();
			Join<Location, Community> community = ebsJoins.getCommunity();

			// Subquery for latest ebsAlert
			Subquery<Long> subqueryAlert = cq.subquery(Long.class);
			Root<EbsAlert> subRootAlert = subqueryAlert.from(EbsAlert.class);
			subqueryAlert.select(cb.max(subRootAlert.get(EbsAlert.ID)))
					.where(cb.equal(subRootAlert.get(EbsAlert.EBS), ebs));

			Join<Ebs, EbsAlert> ebsAlert = ebs.join("ebsAlert", JoinType.LEFT);
			Predicate alertPredicate = cb.or(cb.isNull(ebsAlert.get(EbsAlert.ID)), cb.equal(ebsAlert.get(EbsAlert.ID), subqueryAlert));

			// Subquery for latest riskAssessment
			Subquery<Long> subqueryRisk = cq.subquery(Long.class);
			Root<RiskAssessment> subRootRisk = subqueryRisk.from(RiskAssessment.class);
			subqueryRisk.select(cb.max(subRootRisk.get(RiskAssessment.ID)))
					.where(cb.equal(subRootRisk.get(RiskAssessment.EBS), ebs));

			Join<Ebs, RiskAssessment> riskAssessment = ebs.join("riskAssessment", JoinType.LEFT);
			Predicate riskPredicate = cb.or(cb.isNull(riskAssessment.get(RiskAssessment.ID)), cb.equal(riskAssessment.get(RiskAssessment.ID), subqueryRisk));

			// Predicate for verified status
			Predicate verifiedPredicate = cb.equal(signalVerification.get(SignalVerification.VERIFIED), YesNo.YES);

			// Create the selection
			cq.multiselect(
					ebs.get(Ebs.ID),
					ebs.get(Ebs.UUID),
					ebs.get(Ebs.TRIAGE_DATE),
					ebs.get(Ebs.SOURCE_INFORMATION),
					triaging.get(Triaging.TRIAGING_DECISION),
					ebs.get(Ebs.REPORT_DATE_TIME),
					ebs.get(Ebs.CATEGORY_OF_INFORMANT),
					ebs.get(Ebs.INFORMANT_NAME),
					ebs.get(Ebs.INFORMANT_TEL),
					triaging.get(Triaging.SIGNAL_CATEGORY),
					signalVerification.get(SignalVerification.VERIFIED),
					signalVerification.get(SignalVerification.NUMBER_OF_DEATH),
					triaging.get(Triaging.DATE_OF_DECISION),
					ebs.get(Ebs.PERSON_REGISTERING),
					ebs.get(Ebs.PERSON_DESIGNATION),
					signalVerification.get(SignalVerification.VERIFICATION_SENT),
					signalVerification.get(SignalVerification.VERIFICATION_SENT_DATE),
					signalVerification.get(SignalVerification.VERIFICATION_COMPLETE_DATE),
					riskAssessment.get(RiskAssessment.RISK_ASSESSMENT),
					ebsAlert.get(EbsAlert.ACTION_INITIATED),
					ebsAlert.get(EbsAlert.RESPONSE_STATUS),
					region.get(Region.UUID),
					region.get(Region.NAME),
					community.get(Community.UUID),
					community.get(Community.NAME),
					location.get(Location.CITY),
					district.get(District.UUID),
					district.get(District.NAME)
			);

			Predicate filter = ebs.get(Ebs.ID).in(batchedIds);

			if (ebsCriteria != null) {
				if (ebsCriteria.getUserFilterIncluded()) {
					EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
					filter = service.createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
				}

				Predicate criteriaFilter = service.buildCriteriaFilter(ebsCriteria, ebsQueryContext);
				filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
			}

			if (filter != null) {
				cq.where(cb.and(filter, alertPredicate, riskPredicate, verifiedPredicate));
			} else {
				cq.where(cb.and(alertPredicate, riskPredicate, verifiedPredicate));
			}

			sortBy(sortProperties, ebsQueryContext);
			cq.distinct(true);

			List<EbsIndexDto> results = QueryHelper.getResultList(em, cq, null, null);

			for (EbsIndexDto dto : results) {
				if (addedIds.add(dto.getId())) {
					indexList.add(dto);
				}
			}
		});
		return indexList;
	}

	private List<Long> getIndexListIds(EbsCriteria ebsCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Ebs> ebs = cq.from(Ebs.class);

		EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		// Ensure at least one selection
		List<Selection<?>> selections = new ArrayList<>();
		selections.add(ebs.get(Ebs.ID)); // Ensure there's always at least one selection
		selections.addAll(sortBy(sortProperties, ebsQueryContext));

		cq.multiselect(selections.toArray(new Selection[0])); // Convert to array

		Predicate filter = null;

		if (ebsCriteria != null) {
			if (ebsCriteria.getUserFilterIncluded()) {
				EbsUserFilterCriteria ebsUserFilterCriteria = new EbsUserFilterCriteria();
				filter = service.createUserFilter(ebsQueryContext, ebsUserFilterCriteria);
			}

			Predicate criteriaFilter = service.buildCriteriaFilter(ebsCriteria, ebsQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		List<Tuple> ebss = QueryHelper.getResultList(em, cq, first, max);
		return ebss.stream().map(t -> t.get(0, Long.class)).collect(Collectors.toList());
	}

	private List<Selection<?>> sortBy(List<SortProperty> sortProperties, EbsQueryContext ebsQueryContext) {

		List<Selection<?>> selections = new ArrayList<>();
		CriteriaBuilder cb = ebsQueryContext.getCriteriaBuilder();
		CriteriaQuery<?> cq = ebsQueryContext.getQuery();

		if (sortProperties != null && !sortProperties.isEmpty()) {
			EbsJoins ebsJoins = ebsQueryContext.getJoins();
			Join<Ebs, User> reportingUser = ebsJoins.getReportingUser();
			Join<Ebs, Triaging> decisionDate = ebsJoins.getTriaging();
			Join<Ebs, SignalVerification> signalVerification = ebsJoins.getSignalVerification();
			Join<Ebs, RiskAssessment> riskAssessment = ebsJoins.getRiskAssessment();
			Join<Ebs, EbsAlert> ebsAlert = ebsJoins.getEbsAlert();
			Join<Ebs, Location> location = ebsJoins.getLocation();
			Join<Location, Region> region = ebsJoins.getRegion();
			Join<Location, District> district = ebsJoins.getDistrict();
			Join<Location, Community> community = ebsJoins.getCommunity();

			List<Order> order = new ArrayList<>(sortProperties.size());
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
					case EbsIndexDto.UUID:
					case EbsIndexDto.TRIAGE_DATE:
					case EbsIndexDto.INFORMANT_NAME:
					case EbsIndexDto.INFORMANT_TEL:
					case EbsIndexDto.SOURCE_INFORMATION:
					case EbsIndexDto.REPORT_DATE_TIME:
					case EbsIndexDto.CATEGORY_OF_INFORMANT:
					case EbsIndexDto.PERSON_REGISTERING:
					case EbsIndexDto.PERSON_DESIGNATION:
						expression = ebsQueryContext.getRoot().get(sortProperty.propertyName);
						break;
					case EbsIndexDto.TRIAGING_DECISION_DATE:
						expression = decisionDate.get(Triaging.DATE_OF_DECISION);
						break;
					case EbsIndexDto.VERIFICATION_SENT:
						expression = signalVerification.get(SignalVerification.VERIFICATION_SENT);
						break;
					case EbsIndexDto.VERIFICATION_SENT_DATE:
						expression = signalVerification.get(SignalVerification.VERIFICATION_SENT_DATE);
						break;
					case EbsIndexDto.VERIFIED_DATE:
						expression = signalVerification.get(SignalVerification.VERIFICATION_COMPLETE_DATE);
						break;
					case EbsIndexDto.RISK_STATUS:
						expression = riskAssessment.get(RiskAssessment.RISK_ASSESSMENT);
						break;
					case EbsIndexDto.ACTION_INITIATED:
						expression = ebsAlert.get(EbsAlert.ACTION_INITIATED);
						break;
					case EbsIndexDto.RESPONSE_STATUS:
						expression = ebsAlert.get(EbsAlert.RESPONSE_STATUS);
						break;
					case EbsIndexDto.VERIFIED:
						expression = signalVerification.get(SignalVerification.VERIFIED);
						break;
					case EbsIndexDto.SIGNAL_CATEGORY:
						expression = decisionDate.get(Triaging.SIGNAL_CATEGORY);
						break;
					case EbsIndexDto.TRIAGING_DECISION:
						expression = decisionDate.get(Triaging.TRIAGING_DECISION);
						break;
					case EbsIndexDto.DEATH:
						expression = signalVerification.get(SignalVerification.NUMBER_OF_DEATH);
						break;
					case EbsIndexDto.EBS_LOCATION:
						expression = region.get(Region.NAME);
						order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = community.get(Community.NAME);
						break;
					case EbsIndexDto.REGION:
						expression = region.get(Region.NAME);
						break;
					case EbsIndexDto.DISTRICT:
						expression = district.get(District.NAME);
						break;
					case EbsIndexDto.COMMUNITY:
						expression = community.get(Community.NAME);
						break;
					case EbsIndexDto.TOWN:
						expression = location.get(Location.CITY);
						order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						break;
					default:
						throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
				selections.add(expression);
			}
			cq.orderBy(order);
		}

		return selections;
	}

	@Override
	public Page<EbsIndexDto> getIndexPage(EbsCriteria ebsCriteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
		List<EbsIndexDto> ebsIndexList = getIndexList(ebsCriteria, offset, size, sortProperties);
		long totalElementCount = count(ebsCriteria);
		return new Page<>(ebsIndexList, offset, size, totalElementCount);
	}

	@Override
	@RightsAllowed(UserRight._EVENT_EXPORT)
	public List<EbsExportDto> getExportList(EbsCriteria ebsCriteria, Collection<String> selectedRows, Integer first, Integer max) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsExportDto> cq = cb.createQuery(EbsExportDto.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);
		EbsJoins ebsJoins = ebsQueryContext.getJoins();
		Join<Ebs, Location> location = ebsJoins.getLocation();
		Join<Location, Region> region = ebsJoins.getRegion();
		Join<Location, District> district = ebsJoins.getDistrict();
		Join<Location, Community> community = ebsJoins.getCommunity();
		Join<Ebs, User> reportingUser = ebsJoins.getReportingUser();
		Join<Ebs, Triaging> decisionDate = ebsJoins.getTriaging();

		cq.multiselect(
				ebs.get(Ebs.UUID),
				ebs.get(Ebs.EXTERNAL_ID),
				ebs.get(Ebs.INFORMANT_NAME),
				ebs.get(Ebs.INFORMANT_TEL),
				ebs.get(Ebs.RISK_LEVEL),
				ebs.get(Ebs.SPECIFIC_RISK),
				ebs.get(Ebs.TRIAGE_DATE),
				ebs.get(Ebs.SIGNAL_CATEGORY),
				ebs.get(Ebs.VERIFIED),
				ebs.get(Ebs.CASES),
				ebs.get(Ebs.DEATH),
				ebs.get(Ebs.CATEGORY_OF_INFORMANT),
				region.get(Region.UUID),
				region.get(Region.NAME),
				district.get(District.UUID),
				district.get(District.NAME),
				community.get(Community.UUID),
				community.get(Community.NAME),
				location.get(Location.ADDITIONAL_INFORMATION),
				ebs.get(Ebs.SOURCE_INFORMATION),
				ebs.get(Ebs.REPORT_DATE_TIME),
				reportingUser.get(User.UUID),
				reportingUser.get(User.FIRST_NAME),
				reportingUser.get(User.LAST_NAME));
		decisionDate.get(Triaging.DATE_OF_DECISION);

		cq.distinct(true);

		Predicate filter = service.createUserFilter(ebsQueryContext);

		if (ebsCriteria != null) {
			Predicate criteriaFilter = service.buildCriteriaFilter(ebsCriteria, ebsQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (CollectionUtils.isNotEmpty(selectedRows)) {
			filter = CriteriaBuilderHelper.andInValues(selectedRows, filter, cb, ebs.get(Ebs.UUID));
		}

		cq.where(filter);
		cq.orderBy(cb.desc(ebs.get(Ebs.REPORT_DATE_TIME)));

		List<EbsExportDto> exportList = QueryHelper.getResultList(em, cq, first, max);

		Map<String, Long> caseCounts = new HashMap<>();
		Map<String, Long> deathCounts = new HashMap<>();
		Map<String, Long> contactCounts = new HashMap<>();
		Map<String, Long> contactCountsSourceInEbs = new HashMap<>();
		if (exportList != null && !exportList.isEmpty()) {
			List<Object[]> objectQueryList = null;
			List<String> ebsUuids = exportList.stream().map(EbsExportDto::getUuid).collect(Collectors.toList());


		}

		if (exportList != null) {
			for (EbsExportDto exportDto : exportList) {
				Optional.ofNullable(caseCounts.get(exportDto.getUuid())).ifPresent(exportDto::setCaseCount);
				Optional.ofNullable(deathCounts.get(exportDto.getUuid())).ifPresent(exportDto::setDeathCount);
				Optional.ofNullable(contactCounts.get(exportDto.getUuid())).ifPresent(exportDto::setContactCount);
			}
		}

		return exportList;
	}

	@Override
	public List<String> getArchivedUuidsSince(Date since) {

		User user = userService.getCurrentUser();

		if (user == null) {
			return Collections.emptyList();
		}

		return service.getArchivedUuidsSince(since);
	}

	@Override
	public void validate(@Valid EbsDto ebs) throws ValidationRuntimeException {

		LocationDto location = ebs.getEbsLocation();
		CountryReferenceDto locationCountry = location.getCountry();
		CountryReferenceDto serverCountry = countryFacade.getServerCountry();
		boolean regionAndDistrictRequired = serverCountry == null
				? locationCountry == null
				: locationCountry == null || serverCountry.getIsoCode().equalsIgnoreCase(locationCountry.getIsoCode());

		if (location.getRegion() == null && regionAndDistrictRequired) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validRegion));
		}
		if (location.getDistrict() == null && regionAndDistrictRequired) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validDistrict));
		}

		// Check whether there are any infrastructure errors
		if (location.getDistrict() != null && !districtFacade.getByUuid(location.getDistrict().getUuid()).getRegion().equals(location.getRegion())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noDistrictInRegion));
		}
		if (location.getCommunity() != null
				&& !communityFacade.getByUuid(location.getCommunity().getUuid()).getDistrict().equals(location.getDistrict())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noCommunityInDistrict));
		}
	}


	@Override
	protected EbsReferenceDto toRefDto(Ebs ebs) {
		return toReferenceDto(ebs);
	}

	public EbsDto convertToDetailedReferenceDto(Ebs source, Pseudonymizer pseudonymizer) {

		EbsDto ebsDto = toDto(source);
		pseudonymizeDto(source, ebsDto, pseudonymizer, service.inJurisdictionOrOwned(source));

		return ebsDto;
	}

	@Override
	protected void pseudonymizeDto(Ebs ebs, EbsDto dto, Pseudonymizer pseudonymizer, boolean inJurisdiction) {

		if (dto != null) {
			pseudonymizer.pseudonymizeDto(EbsDto.class, dto, inJurisdiction, e -> {
				pseudonymizer.pseudonymizeUser(ebs.getReportingUser(), userService.getCurrentUser(), dto::setReportingUser);
			});
		}
	}

	protected void restorePseudonymizedDto(EbsDto dto, EbsDto existingDto, Ebs ebs, Pseudonymizer pseudonymizer) {
		if (existingDto != null) {
			boolean inJurisdiction = service.inJurisdictionOrOwned(ebs);
			pseudonymizer.restorePseudonymizedValues(EbsDto.class, dto, existingDto, inJurisdiction);
			pseudonymizer.restoreUser(ebs.getReportingUser(), userService.getCurrentUser(), dto, dto::setReportingUser);
		}
	}

	public Ebs fillOrBuildEntity(@NotNull EbsDto source, Ebs target, boolean checkChangeDate) {
		boolean targetWasNull = isNull(target);
		target = DtoHelper.fillOrBuildEntity(source, target, Ebs::new, checkChangeDate);

		target.setInformantName(source.getInformantName());
		target.setInformantTel(source.getInformantTel());
		target.setEndDate(source.getEndDate());
		target.setReportDateTime(source.getReportDateTime());
		target.setCategoryOfInformant(source.getCategoryOfInformant());
		target.setEbsLocation(locationFacade.fillOrBuildEntity(source.getEbsLocation(), target.getEbsLocation(), checkChangeDate));
		target.setResponsibleUser(userService.getByReferenceDto(source.getResponsibleUser()));
		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setEbsLatLon(source.getEbsLatLon());
		target.setAutomaticScanningType(source.getAutomaticScanningType());
		target.setManualScanningType(source.getManualScanningType());
		target.setScanningType(source.getScanningType());
		target.setDescriptionOccurrence(source.getDescriptionOccurrence());
		target.setOther(source.getOther());
		target.setPersonDesignation(source.getPersonDesignation());
		target.setPersonPhone(source.getPersonPhone());
		target.setPersonRegistering(source.getPersonRegistering());
		target.setSourceName(source.getSourceName());
		target.setSourceInformation(source.getSourceInformation());
		target.setDateOnset(source.getDateOnset());
		target.setEbsLongitude(source.getEbsLongitude());
		target.setEbsLatitude(source.getEbsLongitude());
		target.setTriaging(triagingFacade.fillOrBuildEntity(source.getTriaging(), target.getTriaging(), checkChangeDate));
		target.setSignalVerification(signalVerificationFacade.fillOrBuildEntity(source.getSignalVerification(), target.getSignalVerification(), checkChangeDate));
		target.setOtherInformant(source.getOtherInformant());
		return target;
	}

	/**
	 * Archives all ebss that have not been changed for a defined amount of days
	 *
	 * @param daysAfterEbsGetsArchived
	 *            defines the amount of days
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RightsAllowed(UserRight._SYSTEM)
	public void archiveAllArchivableEbss(int daysAfterEbsGetsArchived) {

		archiveAllArchivableEbss(daysAfterEbsGetsArchived, LocalDate.now());
	}

	@Override
	@RightsAllowed(UserRight._SYSTEM)
	public void archiveAllArchivableEbss(int daysAfterEbsGetsArchived, @NotNull LocalDate referenceDate) {

		LocalDate notChangedSince = referenceDate.minusDays(daysAfterEbsGetsArchived);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Ebs> from = cq.from(Ebs.class);

		Timestamp notChangedTimestamp = Timestamp.valueOf(notChangedSince.atStartOfDay());
		cq.where(
				cb.equal(from.get(Ebs.ARCHIVED), false),
				cb.equal(from.get(Ebs.DELETED), false),
				cb.not(service.createChangeDateFilter(cb, from, notChangedTimestamp)));
		cq.select(from.get(Ebs.UUID)).distinct(true);
		List<String> ebsUuids = em.createQuery(cq).getResultList();

		if (!ebsUuids.isEmpty()) {
			archive(ebsUuids);
		}
	}

	@Override
	public boolean doesExternalTokenExist(String externalToken, String ebsUuid) {
		return service.exists(
				(cb, ebsRoot, cq) -> CriteriaBuilderHelper.and(
						cb,
						cb.notEqual(ebsRoot.get(Ebs.UUID), ebsUuid),
						cb.notEqual(ebsRoot.get(Ebs.DELETED), Boolean.TRUE)));
	}

	@Override
	public Set<RegionReferenceDto> getAllRegionsRelatedToEbsUuids(List<String> uuids) {
		Set<RegionReferenceDto> regionReferenceDtos = new HashSet<>();
		IterableHelper.executeBatched(uuids, ModelConstants.PARAMETER_LIMIT, batchedUuids -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = cb.createQuery(String.class);
			Root<Ebs> ebsRoot = cq.from(Ebs.class);
			Join<Ebs, Location> locationJoin = ebsRoot.join(Ebs.EBS_LOCATION, JoinType.INNER);
			Join<Location, Region> regionJoin = locationJoin.join(Location.REGION, JoinType.INNER);

			cq.select(regionJoin.get(Region.UUID)).distinct(true);
			cq.where(ebsRoot.get(Ebs.UUID).in(batchedUuids));

			em.createQuery(cq).getResultList().stream().map(RegionReferenceDto::new).forEach(regionReferenceDtos::add);
		});
		return regionReferenceDtos;
	}
	@Override
	public String getUuidByCaseUuidOrPersonUuid(String searchTerm) {
		return service.getUuidByCaseUuidOrPersonUuid(searchTerm);
	}
	@Override
	public void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException {
		service.updateExternalData(externalData);
	}

	@Override
	public boolean hasRegionAndDistrict(String ebsUuid) {
		return service.hasRegionAndDistrict(ebsUuid);
	}

	@Override
	public Integer saveBulkEbs(
			List<String> ebsUuidList,
			EbsDto updatedTempEbs) {

		int changedEbs = 0;
		for (String ebsUuid : ebsUuidList) {
			Ebs ebs = service.getByUuid(ebsUuid);

			if (service.isEditAllowed(ebs)) {
				EbsDto ebsDto = toDto(ebs);

				save(ebsDto);
				changedEbs++;
			}
		}
		return changedEbs;

	}

	@Override
	public void setRiskAssessmentAssociations(EbsReferenceDto ebsRef) {

		final Ebs ebs = ebsService.getByUuid(ebsRef.getUuid());
		List<RiskAssessment> riskAssessments = ebs.getRiskAssessment().stream().filter(risks -> risks.getEbs() == null).collect(Collectors.toList());

		if (riskAssessments.size() > 0) {
			riskAssessments.forEach(risk -> {
				risk.setEbs(ebs);
				riskAssessmentService.ensurePersisted(risk);
			});
		}
	}

	@Override
	public void setEbsAlertAssociations(EbsReferenceDto ebsRef) {

		final Ebs ebs = ebsService.getByUuid(ebsRef.getUuid());
		List<EbsAlert> ebsAlerts = ebs.getEbsAlert().stream().filter(alerts -> alerts.getEbs() == null).collect(Collectors.toList());

		if (ebsAlerts.size() > 0) {
			ebsAlerts.forEach(alert -> {
				alert.setEbs(ebs);
				ebsAlertService.ensurePersisted(alert);
			});
		}
	}


	@Override
	protected String getDeleteReferenceField(DeletionReference deletionReference) {
		if (deletionReference == DeletionReference.REPORT) {
			return Ebs.REPORT_DATE_TIME;
		}

		return super.getDeleteReferenceField(deletionReference);
	}

	@Override
	protected CoreEntityType getCoreEntityType() {
		return CoreEntityType.EBS;
	}

	@Override
	public boolean isInJurisdictionOrOwned(String uuid) {
		return service.inJurisdictionOrOwned(service.getByUuid(uuid));
	}

	@LocalBean
	@Stateless
	public static class EbsFacadeEjbLocal extends EbsFacadeEjb {

		public EbsFacadeEjbLocal() {
		}

		@Inject
		public EbsFacadeEjbLocal(EbsService service) {
			super(service);
		}
	}


}