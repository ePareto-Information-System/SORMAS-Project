/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.sample;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.person.PersonNameDto;
import de.symeda.sormas.backend.sormastosormas.share.SormasToSormasShareInfo;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfoFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfoService;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.location.Location;
import org.apache.commons.collections.CollectionUtils;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.RequestContextHolder;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleAssociationType;
import de.symeda.sormas.api.sample.SampleCriteria;
import de.symeda.sormas.api.sample.SampleIndexDto;
import de.symeda.sormas.api.sample.SampleJurisdictionFlagsDto;
import de.symeda.sormas.api.sample.SampleListEntryDto;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseFacadeEjb;
import de.symeda.sormas.backend.caze.CaseQueryContext;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.common.AbstractDeletableAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.common.DeletableAdo;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactQueryContext;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.event.EventParticipantQueryContext;
import de.symeda.sormas.backend.event.EventParticipantService;
import de.symeda.sormas.backend.externalmessage.ExternalMessageService;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.sample.transformers.SampleListEntryDtoResultTransformer;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.IterableHelper;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.Pseudonymizer;
import de.symeda.sormas.backend.util.QueryHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;

@Stateless
@LocalBean
public class SampleService extends AbstractDeletableAdoService<Sample> {

	@EJB
	private UserService userService;
	@EJB
	private CaseService caseService;
	@EJB
	private CaseFacadeEjb.CaseFacadeEjbLocal caseFacade;
	@EJB
	private ContactService contactService;
	@EJB
	private EventParticipantService eventParticipantService;
	@EJB
	private PathogenTestService pathogenTestService;
	@EJB
	private AdditionalTestService additionalTestService;
	@EJB
	private SormasToSormasShareInfoFacadeEjb.SormasToSormasShareInfoFacadeEjbLocal sormasToSormasShareInfoFacade;
	@EJB
	private SormasToSormasShareInfoService sormasToSormasShareInfoService;
	@EJB
	private ExternalMessageService externalMessageService;

	public SampleService() {
		super(Sample.class);
	}

	public List<Sample> findBy(SampleCriteria criteria, User user, String sortProperty, boolean ascending) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(getElementClass());
		Root<Sample> from = cq.from(getElementClass());

		SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, from);

		Predicate filter = buildCriteriaFilter(criteria, sampleQueryContext);

		if (user != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, createUserFilter(sampleQueryContext, null));
		}
		if (filter != null) {
			cq.where(filter);
		}

		if (ascending) {
			cq.orderBy(cb.asc(from.get(sortProperty)));
		} else {
			cq.orderBy(cb.desc(from.get(sortProperty)));
		}

		return em.createQuery(cq).getResultList();
	}

	public List<Sample> findBy(SampleCriteria criteria, User user) {
		return findBy(criteria, user, Sample.CREATION_DATE, true);
	}

	public List<SampleIndexDto> getIndexList(SampleCriteria sampleCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		List<Long> indexListIds = getIndexListIds(sampleCriteria, first, max, sortProperties);

		List<SampleIndexDto> samples = new ArrayList<>();
		IterableHelper.executeBatched(indexListIds, ModelConstants.PARAMETER_LIMIT, batchedIds -> {
			final CriteriaBuilder cb = em.getCriteriaBuilder();
			final CriteriaQuery<SampleIndexDto> cq = cb.createQuery(SampleIndexDto.class);
			final Root<Sample> sample = cq.from(Sample.class);

			SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, sample);
			SampleJoins joins = sampleQueryContext.getJoins();

			final Join<Sample, Case> caze = joins.getCaze();
			final Join<Case, District> caseDistrict = joins.getCaseResponsibleDistrict();
			final Join<Case, Community> caseCommunity = joins.getCaseResponsibleCommunity();


			final Join<Sample, Contact> contact = joins.getContact();
			final Join<Contact, District> contactDistrict = joins.getContactDistrict();
			final Join<Case, District> contactCaseDistrict = joins.getContactCaseDistrict();

			final Join<Contact, Community> contactCommunity = joins.getContactCommunity();
			final Join<Case, Community> contactCaseCommunity = joins.getContactCaseCommunity();


			final Join<EventParticipant, Event> event = joins.getEvent();
			final Join<Location, District> eventDistrict = joins.getEventDistrict();
			final Join<Location, Community> eventCommunity = joins.getEventCommunity();

			Expression<Object> diseaseSelect = cb.selectCase()
					.when(cb.isNotNull(caze), caze.get(Case.DISEASE))
					.otherwise(cb.selectCase().when(cb.isNotNull(contact), contact.get(Contact.DISEASE)).otherwise(event.get(Event.DISEASE)));

			Expression<Object> diseaseDetailsSelect = cb.selectCase()
				.when(cb.isNotNull(caze), caze.get(Case.DISEASE_DETAILS))
				.otherwise(
					cb.selectCase().when(cb.isNotNull(contact), contact.get(Contact.DISEASE_DETAILS)).otherwise(event.get(Event.DISEASE_DETAILS)));

			Expression<Object> districtSelect = cb.selectCase()
					.when(cb.isNotNull(caseDistrict), caseDistrict.get(District.NAME))
					.otherwise(
							cb.selectCase()
									.when(cb.isNotNull(contactDistrict), contactDistrict.get(District.NAME))
									.otherwise(
											cb.selectCase()
													.when(cb.isNotNull(contactCaseDistrict), contactCaseDistrict.get(District.NAME))
													.otherwise(eventDistrict.get(District.NAME))));

			Expression<Object> communitySelect = cb.selectCase()
					.when(cb.isNotNull(caseCommunity), caseCommunity.get(Community.NAME))
					.otherwise(
							cb.selectCase()
									.when(cb.isNotNull(contactCommunity), contactCommunity.get(Community.NAME))
									.otherwise(
											cb.selectCase()
													.when(cb.isNotNull(contactCaseCommunity), contactCaseCommunity.get(Community.NAME))
													.otherwise(eventCommunity.get(Community.NAME))));

			List<Selection<?>> selections = new ArrayList<>(
				Arrays.asList(
						sample.get(Sample.UUID),
						caze.get(Case.EPID_NUMBER),
						sample.get(Sample.LAB_SAMPLE_ID),
						sample.get(Sample.FIELD_SAMPLE_ID),
						sample.get(Sample.SAMPLE_DATE_TIME),
						sample.get(Sample.SHIPPED),
						sample.get(Sample.SHIPMENT_DATE),
						sample.get(Sample.RECEIVED),
						sample.get(Sample.RECEIVED_DATE),
						sample.get(Sample.SAMPLE_MATERIAL),
						sample.get(Sample.REQUESTED_SAMPLE_MATERIALS_STRING),
						sample.get(Sample.SAMPLE_PURPOSE),
						sample.get(Sample.SPECIMEN_CONDITION),
						joins.getLab().get(Facility.NAME),
						joins.getReferredSample().get(Sample.UUID),
						sample.get(Sample.SAMPLING_REASON),
						sample.get(Sample.SAMPLING_REASON_DETAILS),
						caze.get(Case.UUID),
						joins.getCasePerson().get(Person.FIRST_NAME),
						joins.getCasePerson().get(Person.LAST_NAME),
						joins.getCasePerson().get(Person.OTHER_NAME),
						joins.getContact().get(Contact.UUID),
						joins.getContactPerson().get(Person.FIRST_NAME),
						joins.getContactPerson().get(Person.LAST_NAME),
						joins.getEventParticipant().get(EventParticipant.UUID),
						joins.getEventParticipantPerson().get(Person.FIRST_NAME),
						joins.getEventParticipantPerson().get(Person.LAST_NAME),
						diseaseSelect,
						diseaseDetailsSelect,
						sample.get(Sample.PATHOGEN_TEST_RESULT),
						sample.get(Sample.ADDITIONAL_TESTING_REQUESTED),
						cb.isNotEmpty(sample.get(Sample.ADDITIONAL_TESTS)),
						districtSelect,
						communitySelect,
						joins.getLab().get(Facility.UUID),
						sample.get(Sample.SAMPLE_DISPATCH_MODE)));

			// Tests count subquery
			Subquery<Long> testCountSq = cq.subquery(Long.class);
			Root<PathogenTest> testCountRoot = testCountSq.from(PathogenTest.class);
			testCountSq.where(cb.equal(testCountRoot.get(PathogenTest.SAMPLE), sample), cb.isFalse(testCountRoot.get(PathogenTest.DELETED)));
			testCountSq.select(cb.countDistinct(testCountRoot.get(PathogenTest.ID)));
			selections.add(testCountSq.getSelection());

			selections.addAll(getJurisdictionSelections(sampleQueryContext));
			cq.multiselect(selections);

			cq.where(sample.get(Sample.ID).in(batchedIds));
			cq.orderBy(getOrderList(sortProperties, sampleQueryContext));
			cq.distinct(true);

			samples.addAll(QueryHelper.getResultList(em, cq, null, null));
		});

		if (!samples.isEmpty()) {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> testCq = cb.createQuery(Object[].class);
			Root<PathogenTest> testRoot = testCq.from(PathogenTest.class);
			Expression<String> sampleIdExpr = testRoot.get(PathogenTest.SAMPLE).get(Sample.UUID);

			Path<Long> testType = testRoot.get(PathogenTest.TEST_TYPE);
			Path<Date> cqValue = testRoot.get(PathogenTest.CQ_VALUE);
			testCq.select(cb.array(testType, cqValue, sampleIdExpr));

			testCq.where(
				cb.isFalse(testRoot.get(PathogenTest.DELETED)),
				sampleIdExpr.in(samples.stream().map(SampleIndexDto::getUuid).collect(Collectors.toList())));
			testCq.orderBy(cb.desc(testRoot.get(PathogenTest.CHANGE_DATE)));

			List<Object[]> testList = em.createQuery(testCq).getResultList();

			Map<String, Object[]> tests = testList.stream()
				.filter(distinctByKey(pathogenTest -> pathogenTest[2]))
				.collect(Collectors.toMap(pathogenTest -> pathogenTest[2].toString(), Function.identity()));

			for (SampleIndexDto indexDto : samples) {
				Optional.ofNullable(tests.get(indexDto.getUuid())).ifPresent(test -> {
					indexDto.setTypeOfLastTest((PathogenTestType) test[0]);
					indexDto.setLastTestCqValue((Float) test[1]);
				});
			}
		}

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		Pseudonymizer emptyValuePseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		pseudonymizer
			.pseudonymizeDtoCollection(SampleIndexDto.class, samples, s -> s.getSampleJurisdictionFlagsDto().getInJurisdiction(), (s, ignored) -> {
				final SampleJurisdictionFlagsDto sampleJurisdictionFlagsDto = s.getSampleJurisdictionFlagsDto();
				if (s.getAssociatedCase() != null) {
					emptyValuePseudonymizer
						.pseudonymizeDto(CaseReferenceDto.class, s.getAssociatedCase(), sampleJurisdictionFlagsDto.getCaseInJurisdiction(), null);
				}

				ContactReferenceDto associatedContact = s.getAssociatedContact();
				if (associatedContact != null) {
					emptyValuePseudonymizer.pseudonymizeDto(
						ContactReferenceDto.PersonName.class,
						associatedContact.getContactName(),
						sampleJurisdictionFlagsDto.getContactInJurisdiction(),
						null);

					if (associatedContact.getCaseName() != null) {
						pseudonymizer.pseudonymizeDto(
							ContactReferenceDto.PersonName.class,
							associatedContact.getCaseName(),
							sampleJurisdictionFlagsDto.getContactCaseInJurisdiction(),
							null);
					}
				}

				if (s.getAssociatedEventParticipant() != null) {
					emptyValuePseudonymizer.pseudonymizeDto(
						EventParticipantReferenceDto.class,
						s.getAssociatedEventParticipant(),
						sampleJurisdictionFlagsDto.getEvenParticipantInJurisdiction(),
						null);
				}
			}, true);

		return samples;
	}

	public List<Long> getIndexListIds(SampleCriteria sampleCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<Sample> sample = cq.from(Sample.class);

		SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, sample);

		List<Selection<?>> selections = new ArrayList<>();
		selections.add(sample.get(Sample.ID));

		List<Order> orderList = getOrderList(sortProperties, sampleQueryContext);
		List<Expression<?>> sortColumns = orderList.stream().map(Order::getExpression).collect(Collectors.toList());
		selections.addAll(sortColumns);

		cq.multiselect(selections);

		Predicate filter = createUserFilter(sampleQueryContext, sampleCriteria);

		if (sampleCriteria != null) {
			Predicate criteriaFilter = buildCriteriaFilter(sampleCriteria, sampleQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		cq.distinct(true);
		cq.orderBy(orderList);

		return QueryHelper.getResultList(em, cq, first, max).stream().map(t -> t.get(0, Long.class)).collect(Collectors.toList());
	}

	private List<Order> getOrderList(List<SortProperty> sortProperties, SampleQueryContext sampleQueryContext) {
		From<?, Sample> sample = sampleQueryContext.getRoot();
		CriteriaBuilder cb = sampleQueryContext.getCriteriaBuilder();
		SampleJoins joins = sampleQueryContext.getJoins();

		List<Order> orderList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(sortProperties)) {
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
					case SampleIndexDto.UUID:
					case SampleIndexDto.LAB_SAMPLE_ID:
					case SampleIndexDto.SHIPPED:
					case SampleIndexDto.RECEIVED:
					case SampleIndexDto.REFERRED:
					case SampleIndexDto.SAMPLE_DATE_TIME:
					case SampleIndexDto.SHIPMENT_DATE:
					case SampleIndexDto.RECEIVED_DATE:
					case SampleIndexDto.SAMPLE_MATERIAL:
					case SampleIndexDto.SAMPLE_PURPOSE:
					case SampleIndexDto.PATHOGEN_TEST_RESULT:
					case SampleIndexDto.ADDITIONAL_TESTING_STATUS:
						expression = sample.get(sortProperty.propertyName);
						break;
					case SampleIndexDto.DISEASE:
						expression = sampleQueryContext.getDiseaseExpression();
						break;
					case SampleIndexDto.EPID_NUMBER:
						expression = joins.getCaze().get(Case.EPID_NUMBER);
						break;
					case SampleIndexDto.ASSOCIATED_CASE:
						expression = joins.getCasePerson().get(Person.LAST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getCasePerson().get(Person.FIRST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getCasePerson().get(Person.OTHER_NAME);
						break;
					case SampleIndexDto.ASSOCIATED_CONTACT:
						expression = joins.getContactPerson().get(Person.LAST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getContactPerson().get(Person.FIRST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getContactPerson().get(Person.OTHER_NAME);
						break;
					case SampleIndexDto.ASSOCIATED_EVENT_PARTICIPANT:
						expression = joins.getEventParticipantPerson().get(Person.LAST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getEventParticipantPerson().get(Person.FIRST_NAME);
						orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						expression = joins.getEventParticipantPerson().get(Person.OTHER_NAME);
						break;
					case SampleIndexDto.DISTRICT:
						expression = sampleQueryContext.getDistrictNameExpression();
						break;
					case SampleIndexDto.LAB:
						expression = joins.getLab().get(Facility.NAME);
						break;
					default:
						throw new IllegalArgumentException(sortProperty.propertyName);
				}

				orderList.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
			}
		} else {
			orderList.add(cb.desc(sample.get(Sample.SAMPLE_DATE_TIME)));
		}

		return orderList;
	}

	public List<SampleListEntryDto> getEntriesList(SampleCriteria sampleCriteria, Integer first, Integer max) {
		if (sampleCriteria == null) {
			return Collections.emptyList();
		}

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		final Root<Sample> sample = cq.from(Sample.class);

		SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, sample);
		SampleJoins joins = sampleQueryContext.getJoins();

		cq.distinct(true);

		List<Selection<?>> selections = new ArrayList<>(
				Arrays.asList(
						sample.get(Sample.UUID),
						sample.get(Sample.SAMPLE_MATERIAL),
						sample.get(Sample.PATHOGEN_TEST_RESULT),
						sample.get(Sample.SPECIMEN_CONDITION),
						sample.get(Sample.SAMPLE_PURPOSE),
						joins.getReferredSample().get(Sample.UUID),
						sample.get(Sample.RECEIVED),
						sample.get(Sample.RECEIVED_DATE),
						sample.get(Sample.SHIPPED),
						sample.get(Sample.SHIPMENT_DATE),
						sample.get(Sample.SAMPLE_DATE_TIME),
						joins.getLab().get(Facility.NAME),
						joins.getLab().get(Facility.UUID),
						sample.get(Sample.SAMPLING_REASON),
						sample.get(Sample.SAMPLING_REASON_DETAILS),
						sample.get(Sample.ADDITIONAL_TESTING_REQUESTED),
						cb.isNotEmpty(sample.get(Sample.ADDITIONAL_TESTS))));

		// Tests count subquery
		Subquery<Long> testCountSq = cq.subquery(Long.class);
		Root<PathogenTest> testCountRoot = testCountSq.from(PathogenTest.class);
		testCountSq.where(cb.equal(testCountRoot.get(PathogenTest.SAMPLE), sample), cb.isFalse(testCountRoot.get(PathogenTest.DELETED)));
		testCountSq.select(cb.countDistinct(testCountRoot.get(PathogenTest.ID)));
		selections.add(testCountSq.getSelection());

		selections.addAll(getJurisdictionSelections(sampleQueryContext));
		cq.multiselect(selections);

		Predicate filter = CriteriaBuilderHelper.and(cb, createDefaultFilter(cb, sample), createUserFilter(sampleQueryContext, sampleCriteria));
		Predicate criteriaFilter = buildSampleListCriteriaFilter(sampleCriteria, cb, joins);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);

		if (filter != null) {
			cq.where(filter);
		}

		cq.orderBy(cb.desc(sample.get(Sample.SAMPLE_DATE_TIME)));

		return createQuery(cq, first, max).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new SampleListEntryDtoResultTransformer())
				.getResultList();
	}

	public List<Sample> getAllActiveSamplesAfter(Date date, User user, Integer batchSize, String lastSynchronizedUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(getElementClass());
		Root<Sample> from = cq.from(getElementClass());
		SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, from);

		Predicate filter = createActiveSamplesFilter(sampleQueryContext);

		if (user != null) {
			Predicate userFilter = createUserFilter(sampleQueryContext, null);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}

		if (date != null) {
			Predicate dateFilter = createChangeDateFilter(cb, from, date, lastSynchronizedUuid);
			filter = CriteriaBuilderHelper.and(cb, filter, dateFilter);
		}

		cq.where(filter);
		cq.distinct(true);

		return getBatchedQueryResults(cb, cq, from, batchSize);
	}



	public List<String> getAllActiveUuids(User user) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<String> cq = cb.createQuery(String.class);
		final Root<Sample> from = cq.from(getElementClass());
		final SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, from);

		Predicate filter = createActiveSamplesFilter(sampleQueryContext);

		if (user != null) {
			Predicate userFilter = createUserFilter(sampleQueryContext, null);
			filter = CriteriaBuilderHelper.and(cb, filter, userFilter);
		}

		cq.where(filter);
		cq.select(from.get(Sample.UUID));

		return em.createQuery(cq).getResultList();
	}

	/**
	 * Returns the sample that refers to the sample identified by the sampleUuid.
	 *
	 * @param sampleUuid
	 *            The UUID of the sample to get the referral for.
	 * @return The sample that refers to this sample, or null if none is found.
	 */
	public Sample getReferredFrom(String sampleUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(getElementClass());
		Root<Sample> from = cq.from(getElementClass());
		Join<Sample, Sample> referredJoin = from.join(Sample.REFERRED_TO, JoinType.LEFT);

		cq.where(cb.equal(referredJoin.get(AbstractDomainObject.UUID), sampleUuid));
		return QueryHelper.getSingleResult(em, cq);
	}

	public boolean isFieldSampleIdUnique(String uuid, String fieldSampleId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		ParameterExpression<String> fieldSampleIdParams = cb.parameter(String.class, Sample.FIELD_SAMPLE_ID);
		CriteriaQuery<Sample> cq = cb.createQuery(getElementClass());
		Root<Sample> from = cq.from(getElementClass());
		cq.where(cb.equal(from.get(Sample.FIELD_SAMPLE_ID), fieldSampleIdParams));

		TypedQuery<Sample> q = em.createQuery(cq).setParameter(fieldSampleIdParams, fieldSampleId);

		List<Sample> samples = q.getResultList();
		Sample sameSample = samples.stream().filter(p -> p.getUuid().equals(uuid)).findFirst().orElse(null);

		return samples.size() == 0 || sameSample != null;
	}

	public List<String> getDeletedUuidsSince(User user, Date since) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<String> cq = cb.createQuery(String.class);
		final Root<Sample> sample = cq.from(Sample.class);
		final SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, sample);

		Predicate filter = createUserFilter(sampleQueryContext, null);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(sample.get(Sample.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate deletedFilter = cb.equal(sample.get(Sample.DELETED), true);
		if (filter != null) {
			filter = cb.and(filter, deletedFilter);
		} else {
			filter = deletedFilter;
		}

		cq.where(filter);
		cq.select(sample.get(Sample.UUID));

		return em.createQuery(cq).getResultList();
	}

	public List<Sample> getByCaseUuids(List<String> caseUuids) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
		Root<Sample> sampleRoot = cq.from(Sample.class);
		Join<Sample, Case> caseJoin = sampleRoot.join(Sample.ASSOCIATED_CASE, JoinType.LEFT);

		Predicate filter = cb.and(createDefaultFilter(cb, sampleRoot), caseJoin.get(AbstractDomainObject.UUID).in(caseUuids));

		cq.where(filter);
		return em.createQuery(cq).getResultList();
	}

	public List<Sample> getByContactUuids(List<String> contactUuids) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
		Root<Sample> sampleRoot = cq.from(Sample.class);
		Join<Sample, Contact> contactJoin = sampleRoot.join(Sample.ASSOCIATED_CONTACT, JoinType.LEFT);

		Predicate filter = cb.and(createDefaultFilter(cb, sampleRoot), contactJoin.get(AbstractDomainObject.UUID).in(contactUuids));

		cq.where(filter);
		return em.createQuery(cq).getResultList();
	}

	public List<Sample> getByPersonNames(List<PersonNameDto> personNames) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
		Root<Sample> sampleRoot = cq.from(Sample.class);
		Join<Sample, Case> caseJoin = sampleRoot.join(Sample.ASSOCIATED_CASE);
		Join<Case, Person> personJoin = caseJoin.join(Case.PERSON);

		Predicate[] predicates = new Predicate[personNames.size()];

		for (int i = 0; i < personNames.size(); i++) {
			PersonNameDto personName = personNames.get(i);
			Predicate firstNamePredicate = cb.equal(personJoin.get(Person.FIRST_NAME), personName.getFirstName());
			Predicate lastNamePredicate = cb.equal(personJoin.get(Person.LAST_NAME), personName.getLastName());
			Predicate uuidPredicate = cb.equal(personJoin.get(Person.UUID), personName.getUuid());

			predicates[i] = cb.and(firstNamePredicate, lastNamePredicate, uuidPredicate);
		}

		Predicate filter = cb.and(createDefaultFilter(cb, sampleRoot), cb.or(predicates));

		cq.where(filter);
		return em.createQuery(cq).getResultList();
	}


	public List<Sample> getByEventParticipantUuids(List<String> eventParticipantUuids) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
		Root<Sample> sampleRoot = cq.from(Sample.class);
		Join<Sample, EventParticipant> eventParticipantJoin = sampleRoot.join(Sample.ASSOCIATED_EVENT_PARTICIPANT, JoinType.LEFT);

		Predicate filter = cb.and(createDefaultFilter(cb, sampleRoot), eventParticipantJoin.get(AbstractDomainObject.UUID).in(eventParticipantUuids));

		cq.where(filter);
		return em.createQuery(cq).getResultList();
	}

	public Map<PathogenTestResultType, Long> getNewTestResultCountByResultType(List<Long> caseIds) {

		if (CollectionUtils.isEmpty(caseIds)) {
			// Avoid empty IN clause
			return new HashMap<>();
		}

		// Avoid parameter limit by joining caseIds to a String instead of n parameters
		StringBuilder queryBuilder = new StringBuilder();
		//@formatter:off
		queryBuilder.append("WITH sortedsamples AS (SELECT DISTINCT ON (").append(Sample.ASSOCIATED_CASE).append("_id) ")
				.append(Sample.ASSOCIATED_CASE).append("_id, ").append(Sample.PATHOGEN_TEST_RESULT).append(", ").append(Sample.SAMPLE_DATE_TIME)
				.append(" FROM ").append(Sample.TABLE_NAME).append(" WHERE (").append(Sample.SPECIMEN_CONDITION).append(" IS NULL OR ")
				.append(Sample.SPECIMEN_CONDITION).append(" = '").append(SpecimenCondition.ADEQUATE.name()).append("') AND ").append(Sample.TABLE_NAME)
				.append(".").append(Sample.DELETED).append(" = false ORDER BY ").append(Sample.ASSOCIATED_CASE).append("_id, ")
				.append(Sample.SAMPLE_DATE_TIME).append(" desc) SELECT sortedsamples.").append(Sample.PATHOGEN_TEST_RESULT).append(", COUNT(")
				.append(Sample.ASSOCIATED_CASE).append("_id) FROM sortedsamples JOIN ").append(Case.TABLE_NAME).append(" ON sortedsamples.")
				.append(Sample.ASSOCIATED_CASE).append("_id = ").append(Case.TABLE_NAME).append(".id ")
				.append(" WHERE sortedsamples.").append(Sample.ASSOCIATED_CASE).append("_id IN (:caseIds)")
				.append(" GROUP BY sortedsamples." + Sample.PATHOGEN_TEST_RESULT);
		//@formatter:on

		if (caseIds.size() < ModelConstants.PARAMETER_LIMIT) {
			List<Object[]> results;
			Query query = em.createNativeQuery(queryBuilder.toString());
			query.setParameter("caseIds", caseIds);
			results = query.getResultList();

			return results.stream()
					.filter(e -> e[0] != null)
					.collect(Collectors.toMap(e -> PathogenTestResultType.valueOf((String) e[0]), e -> ((BigInteger) e[1]).longValue()));
		} else {
			List<Object[]> results = new LinkedList<>();
			IterableHelper.executeBatched(caseIds, ModelConstants.PARAMETER_LIMIT, batchedCaseIds -> {
				Query query = em.createNativeQuery(queryBuilder.toString());
				query.setParameter("caseIds", batchedCaseIds);
				results.addAll(query.getResultList());
			});
			return results.stream()
					.filter(e -> e[0] != null)
					.collect(Collectors.toMap(e -> PathogenTestResultType.valueOf((String) e[0]), e -> ((BigInteger) e[1]).longValue()));
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	@Deprecated
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, Sample> samplePath) {
		return createUserFilter(new SampleQueryContext(cb, cq, samplePath), null);
	}

	@SuppressWarnings("rawtypes")
	public Predicate createUserFilter(SampleQueryContext sampleQueryContext, SampleCriteria criteria) {

		final CriteriaQuery cq = sampleQueryContext.getQuery();
		final CriteriaBuilder cb = sampleQueryContext.getCriteriaBuilder();
		final SampleJoins joins = sampleQueryContext.getJoins();

		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return null;
		}

		Predicate filter = createUserFilterWithoutAssociations(cb, joins);

		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		if (jurisdictionLevel == JurisdictionLevel.LABORATORY || jurisdictionLevel == JurisdictionLevel.EXTERNAL_LABORATORY) {
			return filter;
		}

		if (criteria != null && criteria.getSampleAssociationType() != null && criteria.getSampleAssociationType() != SampleAssociationType.ALL) {
			final SampleAssociationType sampleAssociationType = criteria.getSampleAssociationType();
			if (sampleAssociationType == SampleAssociationType.CASE) {
				filter = CriteriaBuilderHelper.or(cb, filter, caseService.createUserFilter(new CaseQueryContext(cb, cq, joins.getCaseJoins()), null));
			} else if (sampleAssociationType == SampleAssociationType.CONTACT && !RequestContextHolder.isMobileSync()) {
				filter = CriteriaBuilderHelper
						.or(cb, filter, contactService.createUserFilter(new ContactQueryContext(cb, cq, joins.getContactJoins()), null));
			} else if (sampleAssociationType == SampleAssociationType.EVENT_PARTICIPANT && !RequestContextHolder.isMobileSync()) {
				filter = CriteriaBuilderHelper.or(
						cb,
						filter,
						eventParticipantService.createUserFilter(new EventParticipantQueryContext(cb, cq, joins.getEventParticipantJoins())));
			}
		} else if (currentUser.getLimitedDisease() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					CriteriaBuilderHelper.or(
							cb,
							caseService.createUserFilter(new CaseQueryContext(cb, cq, joins.getCaseJoins()), null),
							RequestContextHolder.isMobileSync()
									? null
									: contactService.createUserFilter(new ContactQueryContext(cb, cq, joins.getContactJoins()), null),
							RequestContextHolder.isMobileSync()
									? null
									: eventParticipantService.createUserFilter(new EventParticipantQueryContext(cb, cq, joins.getEventParticipantJoins()))));
		} else {
			filter = CriteriaBuilderHelper.or(
					cb,
					filter,
					caseService.createUserFilter(new CaseQueryContext(cb, cq, joins.getCaseJoins()), null),
					RequestContextHolder.isMobileSync()
							? null
							: contactService.createUserFilter(new ContactQueryContext(cb, cq, joins.getContactJoins()), null),
					RequestContextHolder.isMobileSync()
							? null
							: eventParticipantService.createUserFilter(new EventParticipantQueryContext(cb, cq, joins.getEventParticipantJoins())));
		}

		return filter;
	}

	/**
	 * Creates a user filter that does not take sample associations into account, i.e. their associated cases, contacts, and event
	 * participants. Instead, it filters for samples of the user's laboratory (if present) and removes samples with diseases
	 * that the user can't access if they have a limited disease set. SHOULD GENERALLY NOT BE USED WITHOUT A PROPER USER FILTER!
	 */
	public Predicate createUserFilterWithoutAssociations(CriteriaBuilder cb, SampleJoins joins) {
		Predicate filter = null;

		User currentUser = getCurrentUser();
		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		// Lab users can see samples assigned to their laboratory
		if (jurisdictionLevel == JurisdictionLevel.LABORATORY || jurisdictionLevel == JurisdictionLevel.EXTERNAL_LABORATORY) {
			if (currentUser.getLaboratory() != null) {
				filter = CriteriaBuilderHelper.or(cb, filter, cb.equal(joins.getLab(), currentUser.getLaboratory()));
			}
		}

		// Only show samples of a specific disease if a limited disease is set
		if (currentUser.getLimitedDisease() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.or(
							cb.and(cb.isNotNull(joins.getEvent()), cb.isNull(joins.getEvent().get(Event.DISEASE))),
							cb.equal(
									cb.selectCase()
											.when(cb.isNotNull(joins.getCaze()), joins.getCaze().get(Case.DISEASE))
											.when(cb.isNotNull(joins.getContact()), joins.getContact().get(Contact.DISEASE))
											.otherwise(joins.getEvent().get(Event.DISEASE)),
									currentUser.getLimitedDisease())));
		}

		return filter;
	}

//	public SampleJurisdictionFlagsDto inJurisdictionOrOwned(Sample sample) {
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<SampleJurisdictionFlagsDto> cq = cb.createQuery(SampleJurisdictionFlagsDto.class);
//		Root<Sample> root = cq.from(Sample.class);
//		cq.multiselect(getJurisdictionSelections(new SampleQueryContext(cb, cq, root)));
//		cq.where(cb.equal(root.get(Sample.UUID), sample.getUuid()));
//		return em.createQuery(cq).getSingleResult();
//	}

	public List<Selection<?>> getJurisdictionSelections(SampleQueryContext qc) {

		final CriteriaBuilder cb = qc.getCriteriaBuilder();
		final SampleJoins joins = qc.getJoins();
		final CriteriaQuery cq = qc.getQuery();
		return Arrays.asList(
				JurisdictionHelper.booleanSelector(cb, inJurisdictionOrOwned(qc)),
				JurisdictionHelper.booleanSelector(
						cb,
						cb.and(cb.isNotNull(joins.getCaze()), caseService.inJurisdictionOrOwned(new CaseQueryContext(cb, cq, joins.getCaseJoins())))),
				JurisdictionHelper.booleanSelector(
						cb,
						cb.and(
								cb.isNotNull(joins.getContact()),
								contactService.inJurisdictionOrOwned(new ContactQueryContext(cb, cq, joins.getContactJoins())))),
				JurisdictionHelper.booleanSelector(
						cb,
						cb.and(
								cb.isNotNull(joins.getContact()),
								cb.isNotNull(joins.getContactJoins().getCaze()),
								caseService.inJurisdictionOrOwned(new CaseQueryContext(cb, cq, joins.getContactJoins().getCaseJoins())))),
				JurisdictionHelper.booleanSelector(
						cb,
						cb.and(
								cb.isNotNull(joins.getEventParticipant()),
								eventParticipantService.inJurisdictionOrOwned(new EventParticipantQueryContext(cb, cq, joins.getEventParticipantJoins())))));
	}

	public Predicate inJurisdictionOrOwned(SampleQueryContext qc) {
		final User currentUser = userService.getCurrentUser();
		return SampleJurisdictionPredicateValidator.of(qc, currentUser).inJurisdictionOrOwned();
	}

	public Predicate buildCriteriaFilter(SampleCriteria criteria, SampleQueryContext sampleQueryContext) {
		SampleJoins joins = sampleQueryContext.getJoins();
		CriteriaBuilder cb = sampleQueryContext.getCriteriaBuilder();
		final From<?, ?> sample = joins.getRoot();

		Predicate filter = null;
		final SampleAssociationType sampleAssociationType = criteria.getSampleAssociationType();
		if (sampleAssociationType == SampleAssociationType.CASE) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getCaze()));
		} else if (sampleAssociationType == SampleAssociationType.CONTACT) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getContact()));
		} else if (sampleAssociationType == SampleAssociationType.EVENT_PARTICIPANT) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getEventParticipant()));
		}

		if (criteria.getPathogenTestType() != null) {
			filter =
					CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getPathogenTests().get(PathogenTest.TEST_TYPE), criteria.getPathogenTestType()));
		}

		if (criteria.getRegion() != null) {
			final String regionUuid = criteria.getRegion().getUuid();
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					CriteriaBuilderHelper.or(
							cb,
							cb.equal(joins.getCaseRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getCaseResponsibleRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getContactRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getContactCaseRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getContactCaseResponsibleRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getEventRegion().get(Region.UUID), regionUuid),
							cb.equal(joins.getEventParticipantJoins().getEventParticipantResponsibleRegion().get(Region.UUID), regionUuid)));
		}
		if (criteria.getDistrict() != null) {
			final String districtUuid = criteria.getDistrict().getUuid();
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					CriteriaBuilderHelper.or(
							cb,
							cb.equal(joins.getCaseDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getCaseResponsibleDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getContactDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getContactCaseDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getContactCaseResponsibleDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getEventDistrict().get(District.UUID), districtUuid),
							cb.equal(joins.getEventParticipantJoins().getEventParticipantResponsibleDistrict().get(District.UUID), districtUuid)));
		}
		if (criteria.getCommunity() != null) {
//			Expression<Object> communityExpression = cb.selectCase()
//				.when(cb.isNotNull(joins.getCaseCommunity()), joins.getCaseCommunity().get(Community.UUID))
//				.otherwise(
//					cb.selectCase()
//						.when(cb.isNotNull(joins.getContactCommunity()), joins.getContactCommunity().get(Community.UUID))
//						.otherwise(joins.getContactCaseCommunity().get(Community.UUID)));
//			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(communityExpression, criteria.getCommunity().getUuid()));
			final String communityUuid = criteria.getCommunity().getUuid();

			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					CriteriaBuilderHelper.or(
							cb,
							cb.equal(joins.getCaseCommunity().get(Community.UUID), communityUuid),
							cb.equal(joins.getCaseResponsibleCommunity().get(Community.UUID), communityUuid),
							cb.equal(joins.getContactCommunity().get(Community.UUID), communityUuid),
							cb.equal(joins.getContactCaseCommunity().get(Community.UUID), communityUuid),
							cb.equal(joins.getContactCaseResponsibleCommunity().get(Community.UUID), communityUuid),
							cb.equal(joins.getEventCommunity().get(Community.UUID), communityUuid)
					));

		}
		if (criteria.getLaboratory() != null) {
			filter =
					CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getLab().get(AbstractDomainObject.UUID), criteria.getLaboratory().getUuid()));
		}
		if (criteria.getSamplePurpose() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.SAMPLE_PURPOSE), criteria.getSamplePurpose()));
		}
		if (criteria.getShipped() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.SHIPPED), criteria.getShipped()));
		}
		if (criteria.getReceived() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.RECEIVED), criteria.getReceived()));
		}
		if (criteria.getSamplePurpose() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.SAMPLE_PURPOSE), criteria.getSamplePurpose()));
		}

		if (criteria.getReferred() != null) {
			if (criteria.getReferred().equals(Boolean.TRUE)) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(sample.get(Sample.REFERRED_TO)));
			} else {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.isNull(sample.get(Sample.REFERRED_TO)));
			}
		}
		if (criteria.getPathogenTestResult() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.PATHOGEN_TEST_RESULT), criteria.getPathogenTestResult()));
		}
		if (criteria.getCaseClassification() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getCaze().get(Case.CASE_CLASSIFICATION), criteria.getCaseClassification()));
		}
		if (criteria.getDisease() != null) {
			Expression<Object> diseaseExpression = cb.selectCase()
					.when(cb.isNotNull(joins.getCaze()), joins.getCaze().get(Case.DISEASE))
					.otherwise(
							cb.selectCase()
									.when(cb.isNotNull(joins.getContact()), joins.getContact().get(Contact.DISEASE))
									.otherwise(joins.getEvent().get(Event.DISEASE)));
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(diseaseExpression, criteria.getDisease()));
		}
		if (criteria.getCaze() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getCaze().get(Case.UUID), criteria.getCaze().getUuid()));
		}
		if (criteria.getContact() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getContact().get(Contact.UUID), criteria.getContact().getUuid()));
		}
		if (criteria.getEventParticipant() != null) {
			filter = CriteriaBuilderHelper
					.and(cb, filter, cb.equal(joins.getEventParticipant().get(EventParticipant.UUID), criteria.getEventParticipant().getUuid()));
		}

		filter = createSampleDateFilter(cb, filter, sample, criteria);

		if (criteria.getSpecimenCondition() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.SPECIMEN_CONDITION), criteria.getSpecimenCondition()));
		}
		if (criteria.getRelevanceStatus() != null) {
			if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ACTIVE) {
				filter = CriteriaBuilderHelper
						.and(cb, filter, cb.or(cb.equal(joins.getCaze().get(Case.ARCHIVED), false), cb.isNull(joins.getCaze().get(Case.ARCHIVED))));
			} else if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ARCHIVED) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getCaze().get(Case.ARCHIVED), true));
			}
		}
		if (criteria.getDeleted() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(sample.get(Sample.DELETED), criteria.getDeleted()));
		}

		if (criteria.getCaseCodeIdLike() != null) {
			String[] textFilters = criteria.getCaseCodeIdLike().split("\\s+");
			for (String textFilter : textFilters) {
				if (DataHelper.isNullOrEmpty(textFilter)) {
					continue;
				}

				Predicate likeFilters = cb.or(
						//case
						CriteriaBuilderHelper.ilike(cb, joins.getCaze().get(Case.UUID), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getCasePerson().get(Person.FIRST_NAME), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getCasePerson().get(Person.LAST_NAME), textFilter),
						CriteriaBuilderHelper.ilike(cb, joins.getCaze().get(Case.EPID_NUMBER), textFilter),
						//contact
						CriteriaBuilderHelper.ilike(cb, joins.getContact().get(Contact.UUID), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getContactJoins().getPerson().get(Person.FIRST_NAME), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getContactJoins().getPerson().get(Person.LAST_NAME), textFilter),
						//EventParticipant
						CriteriaBuilderHelper.ilike(cb, joins.getEventParticipant().get(EventParticipant.UUID), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getEventParticipantJoins().getPerson().get(Person.FIRST_NAME), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getEventParticipantJoins().getPerson().get(Person.LAST_NAME), textFilter),

						CriteriaBuilderHelper.ilike(cb, sample.get(Sample.UUID), textFilter),
						CriteriaBuilderHelper.ilike(cb, sample.get(Sample.LAB_SAMPLE_ID), textFilter),
						CriteriaBuilderHelper.ilike(cb, sample.get(Sample.FIELD_SAMPLE_ID), textFilter),
						CriteriaBuilderHelper.unaccentedIlike(cb, joins.getLab().get(Facility.NAME), textFilter));
				filter = CriteriaBuilderHelper.and(cb, filter, likeFilters);
			}
		}

		if (criteria.getCaseUuids() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, sample.get(Sample.ASSOCIATED_CASE).get(Case.UUID).in(criteria.getCaseUuids()));
		}

		if (criteria.getContactUuids() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, sample.get(Sample.ASSOCIATED_CONTACT).get(Contact.UUID).in(criteria.getContactUuids()));
		}

		if (criteria.getEventParticipantUuids() != null) {
			filter = CriteriaBuilderHelper
					.and(cb, filter, sample.get(Sample.ASSOCIATED_EVENT_PARTICIPANT).get(EventParticipant.UUID).in(criteria.getEventParticipantUuids()));
		}

		return filter;
	}

	private Predicate createSampleDateFilter(CriteriaBuilder cb, Predicate filter, From<?, ?> sample, SampleCriteria criteria) {

		String dateProperty = Sample.SAMPLE_DATE_TIME;

		if (criteria.getSampleDateType() != null) {
			switch (criteria.getSampleDateType()) {
				case REPORT:
					dateProperty = Sample.REPORT_DATE_TIME;
					break;
				case SHIPMENT:
					dateProperty = Sample.SHIPMENT_DATE;
					break;
				case RECIPIENCE:
					dateProperty = Sample.RECEIVED_DATE;
					break;
				case RESULT:
					dateProperty = Sample.PATHOGEN_TEST_RESULT_CHANGE_DATE;
					break;
				case COLLECTION:
				default:
					dateProperty = Sample.SAMPLE_DATE_TIME;
					break;
			}
		}

		if (criteria.getSampleDateFrom() != null && criteria.getSampleDateTo() != null) {
			filter =
					CriteriaBuilderHelper.and(cb, filter, cb.between(sample.get(dateProperty), criteria.getSampleDateFrom(), criteria.getSampleDateTo()));
		} else if (criteria.getSampleDateFrom() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.greaterThanOrEqualTo(sample.get(dateProperty), criteria.getSampleDateFrom()));
		} else if (criteria.getSampleDateTo() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.lessThanOrEqualTo(sample.get(dateProperty), criteria.getSampleDateTo()));
		}
		return filter;
	}

	private Predicate buildSampleListCriteriaFilter(SampleCriteria criteria, CriteriaBuilder cb, SampleJoins joins) {
		Predicate filter = null;
		final SampleAssociationType sampleAssociationType = criteria.getSampleAssociationType();
		if (sampleAssociationType == SampleAssociationType.CASE) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getCaze()));
		} else if (sampleAssociationType == SampleAssociationType.CONTACT) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getContact()));
		} else if (sampleAssociationType == SampleAssociationType.EVENT_PARTICIPANT) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(joins.getEventParticipant()));
		}

		if (criteria.getCaze() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getCaze().get(Case.UUID), criteria.getCaze().getUuid()));
		}
		if (criteria.getContact() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getContact().get(Contact.UUID), criteria.getContact().getUuid()));
		}
		if (criteria.getEventParticipant() != null) {
			filter = CriteriaBuilderHelper
					.and(cb, filter, cb.equal(joins.getEventParticipant().get(EventParticipant.UUID), criteria.getEventParticipant().getUuid()));
		}

		return filter;
	}

	@Override
	public void deletePermanent(Sample sample) {

		// Delete all pathogen tests of this sample
		for (PathogenTest pathogenTest : sample.getPathogenTests()) {
			pathogenTestService.deletePermanent(pathogenTest);
		}

		// Delete all additional tests of this sample
		for (AdditionalTest additionalTest : sample.getAdditionalTests()) {
			additionalTestService.deletePermanent(additionalTest);
		}

		// Remove the case from any S2S share info referencing it
		sormasToSormasShareInfoService.getByAssociatedEntity(SormasToSormasShareInfo.SAMPLE, sample.getUuid()).forEach(s -> {
			s.setSample(null);
			if (sormasToSormasShareInfoFacade.hasAnyEntityReference(s)) {
				sormasToSormasShareInfoService.ensurePersisted(s);
			} else {
				sormasToSormasShareInfoService.deletePermanent(s);
			}
		});

		deleteSampleLinks(sample);

		super.deletePermanent(sample);
	}

	@Override
	public void delete(Sample sample, DeletionDetails deletionDetails) {

		// Mark all pathogen tests of this sample as deleted
		for (PathogenTest pathogenTest : sample.getPathogenTests()) {
			pathogenTestService.delete(pathogenTest, deletionDetails);
		}

		deleteSampleLinks(sample);

		super.delete(sample, deletionDetails);
	}

	private void deleteSampleLinks(Sample sample) {

		// Remove the reference from another sample to this sample if existing
		Sample referralSample = getReferredFrom(sample.getUuid());
		if (referralSample != null) {
			referralSample.setReferredTo(null);
			ensurePersisted(referralSample);
		}

		// Remove the reference from all lab messages
		externalMessageService.getForSample(new SampleReferenceDto(sample.getUuid())).forEach(labMessage -> {
			labMessage.setSample(null);
			externalMessageService.ensurePersisted(labMessage);
		});
	}

	/**
	 * @param sampleUuids
	 *            {@link Sample}s identified by {@code List<String> sampleUuids} to be deleted.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteAll(List<String> sampleUuids, DeletionDetails deletionDetails) {

		List<Sample> samplesList = getByUuids(sampleUuids);
		List<String> pathogenTestUUIDsList = new ArrayList<>();
		List<String> additionalTestUUIDsList = new ArrayList<>();
		for (Sample sample : samplesList) {
			// Mark all pathogen tests of this sample as deleted
			if (!sample.getPathogenTests().isEmpty()) {
				for (PathogenTest pathogenTest : sample.getPathogenTests()) {
					pathogenTestUUIDsList.add(pathogenTest.getUuid());
				}
			}

			// Delete all additional tests of this sample
			if (!sample.getPathogenTests().isEmpty()) {
				for (AdditionalTest additionalTest : sample.getAdditionalTests()) {
					additionalTestUUIDsList.add(additionalTest.getUuid());
				}
			}
		}

		long startTime;
		if (pathogenTestUUIDsList.size() > 0) {
			startTime = DateHelper.startTime();
			IterableHelper.executeBatched(
					pathogenTestUUIDsList,
					pathogenTestUUIDsList.size(),
					batchedSampleUuids -> pathogenTestService.delete(pathogenTestUUIDsList));
			logger.debug(
					"pathogenTestService.delete(pathogenTestUUIDsList) = {}, {}ms",
					pathogenTestUUIDsList.size(),
					DateHelper.durationMillies(startTime));
		}

		if (additionalTestUUIDsList.size() > 0) {
			startTime = DateHelper.startTime();
			IterableHelper.executeBatched(
					additionalTestUUIDsList,
					additionalTestUUIDsList.size(),
					batchedSampleUuids -> additionalTestService.delete(additionalTestUUIDsList));
			logger.debug(
					"additionalTestService.delete(additionalTestUUIDsList) = {}, {}ms",
					additionalTestUUIDsList.size(),
					DateHelper.durationMillies(startTime));
		}

		for (Sample sample : samplesList) {
			// Remove the reference from another sample to this sample if existing
			Sample referralSample = getReferredFrom(sample.getUuid());
			if (referralSample != null) {
				referralSample.setReferredTo(null);
				ensurePersisted(referralSample);
			}
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<Sample> cu = cb.createCriteriaUpdate(Sample.class);
		Root<Sample> root = cu.from(Sample.class);

		cu.set(Sample.CHANGE_DATE, Timestamp.from(Instant.now()));
		cu.set(root.get(Sample.DELETED), true);
		cu.set(root.get(Sample.DELETION_REASON), deletionDetails.getDeletionReason());
		cu.set(root.get(Sample.OTHER_DELETION_REASON), deletionDetails.getOtherDeletionReason());

		cu.where(root.get(Sample.UUID).in(sampleUuids));

		em.createQuery(cu).executeUpdate();

		Map<String, Case> stringCaseMap = new HashMap<>();
		for (Sample sample : samplesList) {
			final Case associatedCase = sample.getAssociatedCase();
			if (associatedCase != null) {
				stringCaseMap.put(associatedCase.getUuid(), associatedCase);
			}
		}

		for (Map.Entry<String, Case> entry : stringCaseMap.entrySet()) {
			Case associatedCase = entry.getValue();
			caseFacade.onCaseSampleChanged(associatedCase);
		}
	}

	/**
	 * Creates a filter that excludes all samples that are deleted or associated with archived or deleted entities
	 */
	public Predicate createActiveSamplesFilter(SampleQueryContext sampleQueryContext) {
		final From<?, Sample> root = sampleQueryContext.getRoot();
		final CriteriaBuilder cb = sampleQueryContext.getCriteriaBuilder();
		final SampleJoins joins = sampleQueryContext.getJoins();

		final Join<Sample, Case> caze = joins.getCaze();
		final Join<Sample, Contact> contact = joins.getContact();
		final Join<Contact, Case> contactCase = joins.getContactCase();
		final Join<Sample, EventParticipant> eventParticipant = joins.getEventParticipant();
		final Join<EventParticipant, Event> event = joins.getEvent();

		Predicate pred = cb.or(
				cb.and(cb.isFalse(caze.get(Case.ARCHIVED)), cb.isFalse(caze.get(Case.DELETED))),
				cb.and(
						cb.or(
								cb.isNull(contact.get(Contact.CAZE)),
								cb.and(cb.isFalse(contactCase.get(Case.ARCHIVED)), cb.isFalse(contactCase.get(Case.DELETED)))),
						cb.isFalse(contact.get(Contact.DELETED))),
				cb.and(
						cb.isFalse(event.get(Event.ARCHIVED)),
						cb.isFalse(event.get(Event.DELETED)),
						cb.isFalse(eventParticipant.get(EventParticipant.DELETED))));
		return cb.and(pred, cb.isFalse(root.get(Sample.DELETED)));
	}

	/**
	 * Creates a default filter that should be used as the basis of queries that do not use {@link SampleCriteria}.
	 * This essentially removes {@link DeletableAdo#isDeleted()} samples from the queries.
	 */
	public Predicate createDefaultFilter(CriteriaBuilder cb, Root<Sample> root) {
		return cb.isFalse(root.get(Sample.DELETED));
	}

	public Boolean isSampleEditAllowed(Sample sample) {
		if (sample.getSormasToSormasOriginInfo() != null && !sample.getSormasToSormasOriginInfo().isOwnershipHandedOver()) {
			return false;
		}

		return inJurisdictionOrOwned(sample) && !sormasToSormasShareInfoService.isSamlpeOwnershipHandedOver(sample);
	}

	public Date getEarliestSampleDate(Collection<Sample> samples) {
		Date earliestSampleDate = null;
		for (Sample sample : samples) {
			if (!sample.isDeleted()
					&& sample.getPathogenTestResult() == PathogenTestResultType.POSITIVE
					&& (earliestSampleDate == null || sample.getSampleDateTime().before(earliestSampleDate))) {
				earliestSampleDate = sample.getSampleDateTime();
			}
		}
		return earliestSampleDate;
	}

	private <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public Predicate createUserFilterWithoutCase(CriteriaBuilder cb, SampleJoins joins) {
		Predicate filter = null;
		// user that reported it is not able to access it. Otherwise they would also need to access the case
		//filter = cb.equal(samplePath.get(Sample.REPORTING_USER), user);

		User currentUser = getCurrentUser();
		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		// lab users can see samples assigned to their laboratory
		if (jurisdictionLevel == JurisdictionLevel.LABORATORY || jurisdictionLevel == JurisdictionLevel.EXTERNAL_LABORATORY) {
			if (currentUser.getLaboratory() != null) {
				filter = AbstractAdoService.or(cb, filter, cb.equal(joins.getRoot().get(Sample.LAB), currentUser.getLaboratory()));
			}
		}

		// only show samples of a specific disease if a limited disease is set
		if (filter != null && currentUser.getLimitedDisease() != null) {
			filter = AbstractAdoService.and(
					cb,
					filter,
					cb.equal(
							cb.selectCase()
									.when(cb.isNotNull(joins.getCaze()), joins.getCaze().get(Case.DISEASE))
									.otherwise(joins.getContact().get(Contact.DISEASE)),
							currentUser.getLimitedDisease()));
		}

		return filter;
	}

	public void unlinkFromEventParticipant(Sample sample) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<Sample> cu = cb.createCriteriaUpdate(Sample.class);
		Root<Sample> root = cu.from(Sample.class);

		cu.set(Sample.ASSOCIATED_EVENT_PARTICIPANT, null);

		cu.where(cb.equal(root.get(Sample.UUID), sample.getUuid()));

		em.createQuery(cu).executeUpdate();

	}

	public List<DiseaseVariant> getAssociatedDiseaseVariants(String sampleUuid) {
		if (DataHelper.isNullOrEmpty(sampleUuid)) {
			return Collections.emptyList();
		}

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<DiseaseVariant> cq = cb.createQuery(DiseaseVariant.class);
		final Root<Sample> from = cq.from(getElementClass());
		final Join<Sample, PathogenTest> pathogenTestJoin = from.join(Sample.PATHOGENTESTS, JoinType.LEFT);

		Predicate filter = createDefaultFilter(cb, from);

		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(AbstractDomainObject.UUID), sampleUuid));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(pathogenTestJoin.get(DeletableAdo.DELETED), false));
		cq.where(filter);
		cq.select(pathogenTestJoin.get(PathogenTest.TESTED_DISEASE_VARIANT));
		return em.createQuery(cq).getResultList();
	}

	public boolean isEditAllowed(Sample sample) {
		if (sample.getSormasToSormasOriginInfo() != null && !sample.getSormasToSormasOriginInfo().isOwnershipHandedOver()) {
			return false;
		}

		if (featureConfigurationFacade.isFeatureDisabled(FeatureType.EDIT_ARCHIVED_ENTITIES) && !sampleAssignedToActiveEntity(sample.getUuid())) {
			return false;
		}

		return getJurisdictionFlags(sample).getInJurisdiction() && !sormasToSormasShareInfoService.isSamlpeOwnershipHandedOver(sample);
	}

	private boolean sampleAssignedToActiveEntity(String sampleUuid) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
		Root<Sample> from = cq.from(getElementClass());
		SampleQueryContext sampleQueryContext = new SampleQueryContext(cb, cq, from);
		SampleJoins joins = sampleQueryContext.getJoins();

		cq.select(cb.literal(true));

		Predicate predicate = cb.and(cb.equal(from.get(Sample.UUID), sampleUuid), assignedToActiveEntity(cb, joins));

		cq.where(predicate);

		Boolean exist = QueryHelper.getSingleResult(em, cq);

		return Boolean.TRUE.equals(exist);
	}


	private Predicate assignedToActiveEntity(CriteriaBuilder cb, SampleJoins joins) {

		return cb.or(
				cb.isFalse(joins.getCaze().get(Case.ARCHIVED)),
				cb.isFalse(joins.getContact().get(Contact.ARCHIVED)),
				cb.isFalse(joins.getEventParticipant().get(EventParticipant.ARCHIVED)));
	}


	public SampleJurisdictionFlagsDto getJurisdictionFlags(Sample entity) {

		return getJurisdictionsFlags(Collections.singletonList(entity)).get(entity.getId());
	}


	public Map<Long, SampleJurisdictionFlagsDto> getJurisdictionsFlags(List<Sample> entities) {

		return getSelectionAttributes(
				entities,
				(cb, cq, from) -> getJurisdictionSelections(new SampleQueryContext(cb, cq, from)),
				e -> new SampleJurisdictionFlagsDto(e));
	}




}