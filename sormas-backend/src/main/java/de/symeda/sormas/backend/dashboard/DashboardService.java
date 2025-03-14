package de.symeda.sormas.backend.dashboard;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.backend.ebs.Ebs;
import de.symeda.sormas.backend.ebs.EbsAlert;
import de.symeda.sormas.backend.ebs.EbsAlertService;
import de.symeda.sormas.backend.ebs.EbsJoins;
import de.symeda.sormas.backend.ebs.EbsQueryContext;
import de.symeda.sormas.backend.ebs.EbsService;
import de.symeda.sormas.backend.ebs.RiskAssessment;
import de.symeda.sormas.backend.ebs.RiskAssessmentService;
import de.symeda.sormas.backend.ebs.SignalVerification;
import de.symeda.sormas.backend.ebs.SignalVerificationService;
import de.symeda.sormas.backend.ebs.Triaging;
import de.symeda.sormas.backend.ebs.TriagingService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.dashboard.DashboardCaseDto;
import de.symeda.sormas.api.dashboard.DashboardCriteria;
import de.symeda.sormas.api.dashboard.DashboardEventDto;
import de.symeda.sormas.api.dashboard.PathogenTestResultDto;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.person.PresentCondition;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseJoins;
import de.symeda.sormas.backend.caze.CaseQueryContext;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.caze.CaseUserFilterCriteria;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.ConfigFacadeEjb;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.event.EventJoins;
import de.symeda.sormas.backend.event.EventQueryContext;
import de.symeda.sormas.backend.event.EventService;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.sample.SampleService;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.QueryHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;

@Stateless
@LocalBean
public class DashboardService {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private CaseService caseService;

	@EJB
	private SignalVerificationService signalVerificationService;

	@EJB
	private TriagingService triagingService;

	@EJB
	private RiskAssessmentService riskAssessmentService;

	@EJB
	private EbsService ebsService;

	@EJB
	private EbsAlertService ebsAlertService;
	@EJB
	private EventService eventService;

	@EJB
	private SampleService sampleService;
	@EJB
	private ConfigFacadeEjb.ConfigFacadeEjbLocal configFacade;

	public List<DashboardCaseDto> getCases(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DashboardCaseDto> cq = cb.createQuery(DashboardCaseDto.class);
		Root<Case> caze = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();
		Join<Case, Person> person = joins.getPerson();

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		Predicate criteriaFilter = createCaseCriteriaFilter(dashboardCriteria, caseQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);

		List<DashboardCaseDto> result;
		if (filter != null) {
			cq.where(filter);
			cq.multiselect(
				caze.get(Case.ID),
				caze.get(Case.UUID),
				caze.get(Case.REPORT_DATE),
				caze.get(Case.CASE_CLASSIFICATION),
				caze.get(Case.DISEASE),
				person.get(Person.PRESENT_CONDITION),
				person.get(Person.CAUSE_OF_DEATH_DISEASE),
				caze.get(Case.QUARANTINE_FROM),
				caze.get(Case.QUARANTINE_TO),
				caze.get(Case.CASE_REFERENCE_DEFINITION));

			result = em.createQuery(cq).getResultList();
		} else {
			result = Collections.emptyList();
		}

		return result;
	}

	public Map<PathogenTestResultType, Long> getNewTestResultCountByResultType(DashboardCriteria dashboardCriteria) {

		// 1. Get all pathogen test results for relevant cases
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<PathogenTestResultDto> cq = cb.createQuery(PathogenTestResultDto.class);
		final Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();
		final Join<Case, Sample> sample = joins.getSamples();

		cq.multiselect(caze.get(Case.ID), sample.get(Sample.PATHOGEN_TEST_RESULT), sample.get(Sample.SAMPLE_DATE_TIME));
		cq.distinct(true);

		final Predicate userFilter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		final Predicate criteriaFilter = createCaseCriteriaFilter(dashboardCriteria, caseQueryContext);
		final Predicate sampleFilter = cb.and(
			cb.isFalse(sample.get(Sample.DELETED)),
			cb.or(cb.isNull(sample.get(Sample.SPECIMEN_CONDITION)), cb.equal(sample.get(Sample.SPECIMEN_CONDITION), SpecimenCondition.ADEQUATE)));
		cq.where(CriteriaBuilderHelper.and(cb, userFilter, criteriaFilter, sampleFilter));

		final List<PathogenTestResultDto> queryResult = QueryHelper.getResultList(em, cq, null, null);

		// 2. Get most recent pathogen test result for each case
		final Map<Long, PathogenTestResultDto> caseTestResults = new HashMap<>();
		queryResult.forEach(caseTestsDto -> {
			final Long caseId = caseTestsDto.getCaseId();
			PathogenTestResultDto existingResult = caseTestResults.get(caseId);

			if (existingResult == null ||
					(existingResult.getSampleDateTime() != null &&
							caseTestsDto.getSampleDateTime() != null &&
							existingResult.getSampleDateTime().before(caseTestsDto.getSampleDateTime()))) {
				caseTestResults.put(caseId, caseTestsDto);
			}
		});


		// 3. Count test results by PathogenTestResultType
		final Map<PathogenTestResultType, Long> result = new HashMap<>();
		for (PathogenTestResultType pathogenTestResultType : PathogenTestResultType.values()) {
			long count =
				caseTestResults.values().stream().filter(caseTestsDto -> caseTestsDto.getPathogenTestResultType() == pathogenTestResultType).count();
			if (count > 0) {
				result.put(pathogenTestResultType, count);
			}
		}

		return result;
	}

	public Map<CaseClassification, Integer> getCasesCountByClassification(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		Predicate criteriaFilter = createCaseCriteriaFilter(dashboardCriteria, caseQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);

		Map<CaseClassification, Integer> result;
		if (filter != null) {
			cq.multiselect(caze.get(Case.CASE_CLASSIFICATION), cb.count(caze.get(Case.CASE_CLASSIFICATION)));
			cq.where(filter);
			cq.groupBy(caze.get(Case.CASE_CLASSIFICATION));

			List<Object[]> resultList = em.createQuery(cq).getResultList();
			boolean aggregateConfirmed = !configFacade.isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY);
			result = getCasesCountByClassification(resultList, aggregateConfirmed);
		} else {
			result = Collections.emptyMap();
		}

		return result;
	}

	static Map<CaseClassification, Integer> getCasesCountByClassification(List<Object[]> classificationCountList, boolean aggregateConfirmed) {

		Map<CaseClassification, Integer> result = classificationCountList.stream()
			.collect(Collectors.toMap(tuple -> (CaseClassification) tuple[0], tuple -> ((Number) tuple[1]).intValue()));

		if (aggregateConfirmed) {
			CaseClassification confirmedKey = CaseClassification.CONFIRMED;
			int confirmedSum = result.entrySet()
				.stream()
				.filter(e -> CaseClassification.getConfirmedClassifications().contains(e.getKey()))
				.mapToInt(e -> e.getValue())
				.sum();
			for (CaseClassification caseClassification : CaseClassification.getConfirmedClassifications()) {
				if (caseClassification == confirmedKey && confirmedSum > 0) {
					result.put(confirmedKey, confirmedSum);
				} else {
					result.remove(caseClassification);
				}
			}
		}

		return result;
	}

	public Map<Disease, Long> getCaseCountByDisease(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));

		filter = CriteriaBuilderHelper.and(cb, filter, createCaseCriteriaFilter(dashboardCriteria, caseQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		cq.groupBy(caze.get(Case.DISEASE));
		cq.multiselect(caze.get(Case.DISEASE), cb.count(caze));
		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<Disease, Long> resultMap = results.stream().collect(Collectors.toMap(e -> (Disease) e[0], e -> (Long) e[1]));

		return resultMap;
	}

	public String getLastReportedDistrictName(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();
		Join<Case, District> district = joins.getResponsibleDistrict();

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));

		filter = CriteriaBuilderHelper.and(cb, filter, createCaseCriteriaFilter(dashboardCriteria, caseQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(district.get(District.NAME));
		List<Order> order = new ArrayList<>();
		order.add(cb.desc(caze.get(Case.REPORT_DATE)));
		order.add(cb.desc(caze.get(Case.CREATION_DATE)));
		cq.orderBy(order);

		return QueryHelper.getFirstResult(em, cq, t -> t == null ? StringUtils.EMPTY : t);
	}

	public Map<Disease, District> getLastReportedDistrictByDisease(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();
		Join<Case, District> districtJoin = joins.getResponsibleDistrict();

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));

		filter = CriteriaBuilderHelper.and(cb, filter, createCaseCriteriaFilter(dashboardCriteria, caseQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		Expression<Number> maxReportDate = cb.max(caze.get(Case.REPORT_DATE));
		Expression<Number> maxCreationDate = cb.max(caze.get(Case.CREATION_DATE));
		cq.multiselect(caze.get(Case.DISEASE), districtJoin);
		cq.groupBy(caze.get(Case.DISEASE), districtJoin);

		List<Order> order = new ArrayList<>();
		order.add(cb.desc(maxReportDate));
		order.add(cb.desc(maxCreationDate));
		cq.orderBy(order);

		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<Disease, District> resultMap = new HashMap<>();
		for (Object[] e : results) {
			Disease disease = (Disease) e[0];
			if (!resultMap.containsKey(disease)) {
				District district = (District) e[1];
				resultMap.put(disease, district);
			}
		}

		return resultMap;
	}

	public Map<Disease, Long> getDeathCountByDisease(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> root = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, root);
		CaseJoins joins = caseQueryContext.getJoins();
		Join<Case, Person> person = joins.getPerson();

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		filter = CriteriaBuilderHelper.and(cb, filter, createCaseCriteriaFilter(dashboardCriteria, caseQueryContext));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(person.get(Person.CAUSE_OF_DEATH_DISEASE), root.get(Case.DISEASE)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.multiselect(person.get(Person.CAUSE_OF_DEATH_DISEASE), cb.count(person));
		cq.groupBy(person.get(Person.CAUSE_OF_DEATH_DISEASE));

		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<Disease, Long> outbreaks = results.stream().collect(Collectors.toMap(e -> (Disease) e[0], e -> (Long) e[1]));

		return outbreaks;
	}

	public long countCasesConvertedFromContacts(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		Predicate criteriaFilter = createCaseCriteriaFilter(dashboardCriteria, caseQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);

		caze.join(Case.CONVERTED_FROM_CONTACT, JoinType.INNER);

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.countDistinct(caze));
		return em.createQuery(cq).getSingleResult();
	}

	public Map<PresentCondition, Integer> getCasesCountPerPersonCondition(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();

		Join<Case, Person> person = joins.getPerson();

		Predicate filter = caseService.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		Predicate criteriaFilter = createCaseCriteriaFilter(dashboardCriteria, caseQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);

		if (filter != null) {
			cq.where(filter);
		}

		cq.groupBy(person.get(Person.PRESENT_CONDITION));
		cq.multiselect(person.get(Person.PRESENT_CONDITION), cb.count(caze));
		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<PresentCondition, Integer> resultMap = results.stream()
			.collect(
				Collectors.toMap(
					e -> e[0] != null ? (PresentCondition) e[0] : PresentCondition.UNKNOWN,
					e -> ((Number) e[1]).intValue(),
					(v1, v2) -> v1 + v2));
		return resultMap;
	}

	public List<DashboardEventDto> getNewEvents(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DashboardEventDto> cq = cb.createQuery(DashboardEventDto.class);
		Root<Event> event = cq.from(Event.class);
		EventQueryContext eventQueryContext = new EventQueryContext(cb, cq, event);
		EventJoins eventJoins = eventQueryContext.getJoins();
		Join<Event, Location> eventLocation = eventJoins.getLocation();
		Join<Location, District> eventDistrict = eventJoins.getDistrict();

		Predicate filter = eventService.createDefaultFilter(cb, event);
		filter = CriteriaBuilderHelper.and(cb, filter, buildEventCriteriaFilter(dashboardCriteria, eventQueryContext));
		filter = CriteriaBuilderHelper.and(cb, filter, eventService.createUserFilter(eventQueryContext));

		List<DashboardEventDto> result;

		if (filter != null) {
			cq.where(filter);
			cq.multiselect(
				event.get(Event.UUID),
				event.get(Event.EVENT_STATUS),
				event.get(Event.EVENT_INVESTIGATION_STATUS),
				event.get(Event.DISEASE),
				event.get(Event.DISEASE_DETAILS),
				event.get(Event.START_DATE),
				event.get(Event.REPORT_LAT),
				event.get(Event.REPORT_LON),
				eventLocation.get(Location.LATITUDE),
				eventLocation.get(Location.LONGITUDE),
				eventJoins.getReportingUser().get(User.UUID),
				eventJoins.getResponsibleUser().get(User.UUID),
				eventJoins.getRegion().get(Region.UUID),
				eventDistrict.get(District.NAME),
				eventDistrict.get(District.UUID),
				eventJoins.getCommunity().get(Community.UUID),
				JurisdictionHelper.booleanSelector(cb, eventService.inJurisdictionOrOwned(eventQueryContext)));

			result = em.createQuery(cq).getResultList();

		} else {
			result = Collections.emptyList();
		}

		return result;
	}

	public Map<EventStatus, Long> getEventCountByStatus(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Event> event = cq.from(Event.class);
		EventQueryContext eventQueryContext = new EventQueryContext(cb, cq, event);

		cq.multiselect(event.get(Event.EVENT_STATUS), cb.count(event));
		cq.groupBy(event.get(Event.EVENT_STATUS));

		Predicate filter = eventService.createDefaultFilter(cb, event);
		filter = CriteriaBuilderHelper.and(cb, filter, buildEventCriteriaFilter(dashboardCriteria, eventQueryContext));
		filter = CriteriaBuilderHelper.and(cb, filter, eventService.createUserFilter(eventQueryContext));

		if (filter != null)
			cq.where(filter);

		List<Object[]> results = em.createQuery(cq).getResultList();

		return results.stream().collect(Collectors.toMap(e -> (EventStatus) e[0], e -> (Long) e[1]));
	}

	private <T extends AbstractDomainObject> Predicate createCaseCriteriaFilter(
		DashboardCriteria dashboardCriteria,
		CaseQueryContext caseQueryContext) {

		final From<?, Case> from = caseQueryContext.getRoot();
		final CriteriaBuilder cb = caseQueryContext.getCriteriaBuilder();
		final CaseJoins joins = caseQueryContext.getJoins();

		Join<Case, Region> responsibleRegion = joins.getResponsibleRegion();
		Join<Case, District> responsibleDistrict = joins.getResponsibleDistrict();

		Predicate filter = null;
		if (dashboardCriteria.getDisease() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Case.DISEASE), dashboardCriteria.getDisease()));
		}
		if (dashboardCriteria.getCaseClassification() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Case.CASE_CLASSIFICATION), dashboardCriteria.getCaseClassification()));
		}
		if (dashboardCriteria.getRegion() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(responsibleRegion.get(Region.UUID), dashboardCriteria.getRegion().getUuid()));
		}
		if (dashboardCriteria.getDistrict() != null) {
			filter =
				CriteriaBuilderHelper.and(cb, filter, cb.equal(responsibleDistrict.get(District.UUID), dashboardCriteria.getDistrict().getUuid()));
		}
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				caseService.createNewCaseFilter(
					caseQueryContext,
					DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
					DateHelper.getEndOfDay(dashboardCriteria.getDateTo()),
					dashboardCriteria.getNewCaseDateType()));
		}
		//if (!dashboardCriteria.shouldIncludeNotACaseClassification()) {

			if (dashboardCriteria.isIncludeNotACaseClassification()==null||dashboardCriteria.isIncludeNotACaseClassification()==false) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.notEqual(caseQueryContext.getRoot().get(Case.CASE_CLASSIFICATION), CaseClassification.NO_CASE));
		}

		// Exclude deleted cases. Archived cases should stay included
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Case.DELETED)));

		return filter;
	}

	private Predicate buildEventCriteriaFilter(DashboardCriteria dashboardCriteria, EventQueryContext eventQueryContext) {

		CriteriaBuilder cb = eventQueryContext.getCriteriaBuilder();
		From<?, Event> from = eventQueryContext.getRoot();

		Predicate filter = null;
		if (dashboardCriteria.getDisease() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Event.DISEASE), dashboardCriteria.getDisease()));
		}
		if (dashboardCriteria.getRegion() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.equal(
					from.join(Event.EVENT_LOCATION, JoinType.LEFT).join(Location.REGION, JoinType.LEFT).get(Region.UUID),
					dashboardCriteria.getRegion().getUuid()));
		}
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.equal(
					from.join(Event.EVENT_LOCATION, JoinType.LEFT).join(Location.DISTRICT, JoinType.LEFT).get(District.UUID),
					dashboardCriteria.getDistrict().getUuid()));
		}

		filter = CriteriaBuilderHelper.and(cb, filter, createEventDateFilter(eventQueryContext.getQuery(), cb, from, dashboardCriteria));

		// Exclude deleted events. Archived events should stay included
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Event.DELETED)));

		return filter;
	}

	private Predicate createEventDateFilter(CriteriaQuery<?> cq, CriteriaBuilder cb, From<?, Event> from, DashboardCriteria dashboardCriteria) {
		Predicate filter = null;

		Date eventDateFrom = dashboardCriteria.getDateFrom();
		Date eventDateTo = dashboardCriteria.getDateTo();

		Predicate eventDateFilter = null;

		if (eventDateFrom != null && eventDateTo != null) {
			eventDateFilter = cb.or(
				cb.and(cb.isNull(from.get(Event.END_DATE)), cb.between(from.get(Event.START_DATE), eventDateFrom, eventDateTo)),
				cb.and(cb.isNull(from.get(Event.START_DATE)), cb.between(from.get(Event.END_DATE), eventDateFrom, eventDateTo)),
				cb.and(
					cb.greaterThanOrEqualTo(from.get(Event.END_DATE), eventDateFrom),
					cb.lessThanOrEqualTo(from.get(Event.START_DATE), eventDateTo)));
		} else if (eventDateFrom != null) {
			eventDateFilter = cb.or(
				cb.and(cb.isNull(from.get(Event.END_DATE)), cb.greaterThanOrEqualTo(from.get(Event.START_DATE), eventDateFrom)),
				cb.and(cb.isNull(from.get(Event.START_DATE)), cb.greaterThanOrEqualTo(from.get(Event.END_DATE), eventDateFrom)));
		} else if (eventDateTo != null) {
			eventDateFilter = cb.or(
				cb.and(cb.isNull(from.get(Event.START_DATE)), cb.lessThanOrEqualTo(from.get(Event.END_DATE), eventDateTo)),
				cb.and(cb.isNull(from.get(Event.END_DATE)), cb.lessThanOrEqualTo(from.get(Event.START_DATE), eventDateTo)));
		}

		if (eventDateFrom != null || eventDateTo != null) {
			Predicate reportFilter = cb.and(
				cb.isNull(from.get(Event.START_DATE)),
				cb.isNull(from.get(Event.END_DATE)),
				cb.between(from.get(Event.REPORT_DATE_TIME), eventDateFrom, eventDateTo));
			filter = CriteriaBuilderHelper.and(cb, filter, cb.or(eventDateFilter, reportFilter));
		}

		return filter;
	}



	public Predicate createCriteriaFilter(
			DashboardCriteria criteria,
			CriteriaBuilder cb,
			CriteriaQuery<?> cq,
			From<Ebs, Ebs> from) {

		Predicate filter = null;

		// Join the SignalVerification table
		Join<Ebs, SignalVerification> signalVerification = from.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT); // Assuming Ebs.SIGNAL_VERIFICATION is the relationship name

		// Join the Location table for region and district filtering
		Join<Ebs, Location> location = from.join(Ebs.EBS_LOCATION, JoinType.LEFT); // Assuming Ebs.EBS_LOCATION is the relationship name
		Join<Location, Region> region = location.join(Location.REGION, JoinType.LEFT);
		Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);

		// Add filters

		// Region filter
		if (criteria.getRegion() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), criteria.getRegion().getUuid())
			);
		}

		// District filter
		if (criteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), criteria.getDistrict().getUuid())
			);
		}

		// Date filter
		if (criteria.getDateFrom() != null && criteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							from.get(Ebs.CREATION_DATE), // Assuming Ebs.REPORT_DATE_TIME is the field for date filtering
							DateHelper.getStartOfDay(criteria.getDateFrom()),
							DateHelper.getEndOfDay(criteria.getDateTo())
					)
			);
		}

		// Signal verification filter
		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(signalVerification.get(SignalVerification.VERIFIED), SignalOutcome.EVENT));

		// Use the deleted field from the Ebs table
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Ebs.DELETED)));

		// Use the archived field from the Ebs table
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Ebs.ARCHIVED)));

		return filter;
	}


	private <T extends AbstractDomainObject> Predicate createEbsCriteriaFilter(
			DashboardCriteria dashboardCriteria,
			EbsQueryContext ebsQueryContext) {

		final From<?, Ebs> from = ebsQueryContext.getRoot();
		final CriteriaBuilder cb = ebsQueryContext.getCriteriaBuilder();
		final EbsJoins joins = ebsQueryContext.getJoins();

		Join<Location, Region> responsibleRegion = joins.getRegion();
		Join<Location, District> responsibleDistrict = joins.getDistrict();


		Predicate filter = null;

		if (dashboardCriteria.getRegion() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(responsibleRegion.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(responsibleDistrict.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}
//
//		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
//			filter = CriteriaBuilderHelper.and(
//					cb,
//					filter,
//					cb.between(
//							from.get(Ebs.REPORT_DATE_TIME),
//							dashboardCriteria.getDateFrom(),
//							dashboardCriteria.getDateTo()
//					)
//			);
//		}

		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					ebsService.createNewEbsFilter(
							ebsQueryContext,
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo()),
							dashboardCriteria.getNewCaseDateType()));
		}
		// Exclude deleted cases. Archived cases should stay included
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Case.DELETED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Case.ARCHIVED)));


		return filter;
	}

	public Long getSignalInformationCount(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);

		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(ebs));

		return em.createQuery(cq).getSingleResult();

	}

	public Long getTriagingCount(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> ebs = cq.from(Ebs.class); // Start from Ebs table

		// Create join to Triaging table
		Join<Ebs, Triaging> triaging = ebs.join(Ebs.TRIAGING, JoinType.LEFT); // Assuming Ebs.TRIAGING is the relationship name

		// Create joins for filtering
		Join<Ebs, Location> location;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT); // Assuming Ebs.EBS_LOCATION is the relationship name
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter - using decisiondate as the primary date field for triaging
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							triaging.get(Triaging.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted cases. Archived cases should stay included
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(triaging.get(Triaging.TRIAGING_DECISION)));

		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(ebs));

		return em.createQuery(cq).getSingleResult();
	}

	public Long getEbsEventCount(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> from = cq.from(Ebs.class);

		Predicate filter = CriteriaBuilderHelper.and(cb, createCriteriaFilter(dashboardCriteria,cb,cq,from));
		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(from));

		return em.createQuery(cq).getSingleResult();

	}

	public Long getSignalVerificationCount(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Ebs> ebs = cq.from(Ebs.class); // Start from Ebs table

		// Create join to SignalVerification table
		Join<Ebs, SignalVerification> signalVerification = ebs.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT); // Assuming Ebs.SIGNAL_VERIFICATION is the relationship name

		// Create joins for filtering
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT); // Assuming Ebs.EBS_LOCATION is the relationship name
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter - using verificationsentdate from the schema
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							signalVerification.get(SignalVerification.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));

		// Exclude archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(signalVerification.get(SignalVerification.VERIFIED),SignalOutcome.EVENT));


		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(ebs));

		return em.createQuery(cq).getSingleResult();
	}

	public Long getRiskAssessmentCount(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<RiskAssessment> riskAssessment = cq.from(RiskAssessment.class);

		// Create joins for filtering
		Join<RiskAssessment, Ebs> ebs = riskAssessment.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, SignalVerification> ebsSignalVerificationJoin = ebs.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter - using assessmentdate from the schema
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							riskAssessment.get(RiskAssessment.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.isNotNull(ebsSignalVerificationJoin.get(SignalVerification.VERIFIED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(ebsSignalVerificationJoin.get(SignalVerification.VERIFIED), SignalOutcome.EVENT));

		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(riskAssessment));

		return em.createQuery(cq).getSingleResult();
	}

	public Long getAlertCount(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EbsAlert> ebsAlert = cq.from(EbsAlert.class);

		// Create joins for filtering
		Join<EbsAlert, Ebs> ebs = ebsAlert.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter - using alertdate from the schema
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							ebsAlert.get(EbsAlert.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));

		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		// Select count
		cq.select(cb.count(ebsAlert));

		return em.createQuery(cq).getSingleResult();
	}

	public Date getLatestSignalInformationCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<Ebs> root = cq.from(Ebs.class);

		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, root);

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);

		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(root.get(SignalVerification.CREATION_DATE));

		cq.orderBy(cb.desc(root.get(SignalVerification.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Date getLatestEbsEventCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<Ebs> root = cq.from(Ebs.class);

		Predicate filter = CriteriaBuilderHelper.and(cb, createCriteriaFilter(dashboardCriteria,cb,cq,root));
		if (filter != null) {
			cq.where(filter);
		}

		cq.select(root.get(SignalVerification.CREATION_DATE));

		cq.orderBy(cb.desc(root.get(SignalVerification.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Date getLatestTriagingCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<Ebs> ebs = cq.from(Ebs.class); // Start from Ebs table

		// Create join to Triaging table
		Join<Ebs, Triaging> triaging = ebs.join(Ebs.TRIAGING, JoinType.LEFT); // Assuming Ebs.TRIAGING is the relationship name

		// Create joins for filtering
		Join<Ebs, Location> location;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT); // Assuming Ebs.EBS_LOCATION is the relationship name
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							triaging.get(Triaging.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		// Select the creation date from Ebs
		cq.select(ebs.get(Ebs.CREATION_DATE));
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Date getLatestSignalVerificationCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<Ebs> ebs = cq.from(Ebs.class); // Start from Ebs table

		// Create join to SignalVerification table
		Join<Ebs, SignalVerification> signalVerification = ebs.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT); // Assuming Ebs.SIGNAL_VERIFICATION is the relationship name

		// Create joins for filtering
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT); // Assuming Ebs.EBS_LOCATION is the relationship name
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							signalVerification.get(SignalVerification.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		// Select the creation date from Ebs
		cq.select(ebs.get(Ebs.CREATION_DATE));
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Date getLatestRiskAssessmentCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<RiskAssessment> root = cq.from(RiskAssessment.class);

		// Create joins for filtering
		Join<RiskAssessment, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(RiskAssessment.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(root.get(RiskAssessment.CREATION_DATE));
		cq.orderBy(cb.desc(root.get(RiskAssessment.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Date getLatestAlertCreationDate(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<EbsAlert> root = cq.from(EbsAlert.class);

		// Create joins for filtering
		Join<EbsAlert, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(Ebs.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(root.get(Ebs.CREATION_DATE));
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<Date> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public EbsSourceType getSignalInformationSource(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		cq.select(ebs.get(Ebs.SOURCE_INFORMATION));

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);

		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (filter != null) {
			cq.where(filter);
		}
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EbsSourceType getLatestEbsEventSource(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<Ebs> root = cq.from(Ebs.class);

		Join<Ebs, SignalVerification> ebsJoin = root.join(Ebs.SIGNAL_VERIFICATION);

		Predicate filter = CriteriaBuilderHelper.and(cb, createCriteriaFilter(dashboardCriteria,cb,cq,root));
		if (filter != null) {
			cq.where(filter);
		}

		cq.select(root.get(Ebs.SOURCE_INFORMATION));

		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EbsSourceType getLatestTriagingSource(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<Ebs> root = cq.from(Ebs.class);

		// Create joins for filtering
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = root.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = cb.isNotNull(root.get(Ebs.TRIAGING_ID));

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			Join<Ebs, Triaging> triaging = root.join(Ebs.TRIAGING, JoinType.LEFT);
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							triaging.get(Triaging.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.ARCHIVED)));

		cq.where(filter);
		cq.select(root.get(Ebs.SOURCE_INFORMATION));
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EbsSourceType getLatestSignalVerificationSource(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<Ebs> root = cq.from(Ebs.class);

		// Create joins for filtering
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = root.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = cb.isNotNull(root.get(Ebs.SIGNAL_VERIFICATION_ID));

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			Join<Ebs, SignalVerification> signalVerification = root.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT);
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							signalVerification.get(SignalVerification.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.ARCHIVED)));

		cq.where(filter);
		cq.select(root.get(Ebs.SOURCE_INFORMATION));
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EbsSourceType getLatestAlertSource(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<EbsAlert> root = cq.from(EbsAlert.class);

		// Create joins for filtering
		Join<EbsAlert, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(Ebs.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(ebs.get(Ebs.SOURCE_INFORMATION));
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public EbsSourceType getLatestRiskAssessmentSource(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EbsSourceType> cq = cb.createQuery(EbsSourceType.class);
		Root<RiskAssessment> root = cq.from(RiskAssessment.class);

		// Create joins for filtering
		Join<RiskAssessment, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = null;
		Join<Location, Region> region = null;
		Join<Location, District> district = null;

		// Only create location joins if needed for region/district filtering
		if (dashboardCriteria.getRegion() != null || dashboardCriteria.getDistrict() != null) {
			location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
			region = location.join(Location.REGION, JoinType.LEFT);
			district = location.join(Location.DISTRICT, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null && district != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(RiskAssessment.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(ebs.get(Ebs.SOURCE_INFORMATION));
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<EbsSourceType> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Map<EbsEvent, Long> getEbsEventCountByEbsEvent(DashboardCriteria dashboardCriteria) {
		Map<EbsEvent, Long> resultMap = new HashMap<>();

		try {

			Long ebsCount = getSignalInformationCount(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_INFORMATION, ebsCount);
			Long triagingCount = getTriagingCount(dashboardCriteria);
			resultMap.put(EbsEvent.TRIAGING,triagingCount);
			Long signalVerificationCount = getSignalVerificationCount(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_VERIFICATION,signalVerificationCount);
			Long eventCount = getEbsEventCount(dashboardCriteria);
			resultMap.put(EbsEvent.EVENT,eventCount);
			Long riskAssessmentCount = getRiskAssessmentCount(dashboardCriteria);
			resultMap.put(EbsEvent.RISK_ASSESSMENT,riskAssessmentCount);
			Long alertCount = getAlertCount(dashboardCriteria);
			resultMap.put(EbsEvent.ALERT,alertCount);

		}catch (Exception e){
			e.printStackTrace();
		}

		return resultMap;
	}

	public Map<EbsEvent, Date> getLatestEbsEventsDate(DashboardCriteria dashboardCriteria) {
		Map<EbsEvent, Date> resultMap = new HashMap<>();

		try {

			Date ebsDate = getLatestSignalInformationCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_INFORMATION, ebsDate);
			Date triagingDate = getLatestTriagingCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.TRIAGING,triagingDate);
			Date signalVerificationDate = getLatestSignalVerificationCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_VERIFICATION,signalVerificationDate);
			Date eventDate = getLatestEbsEventCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.EVENT,eventDate);
			Date riskAssessmentDate = getLatestRiskAssessmentCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.RISK_ASSESSMENT,riskAssessmentDate);
			Date alertDate = getLatestAlertCreationDate(dashboardCriteria);
			resultMap.put(EbsEvent.ALERT,alertDate);

		}catch (Exception e){
			e.printStackTrace();
		}

		return resultMap;
	}

	public Map<EbsEvent, EbsSourceType> getLatestEbsEventsSource(DashboardCriteria dashboardCriteria) {

		Map<EbsEvent, EbsSourceType> resultMap = new HashMap<>();

		try {

			EbsSourceType ebsEbsSourceType = getSignalInformationSource(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_INFORMATION, ebsEbsSourceType);
			EbsSourceType triagingEbsSourceType = getLatestTriagingSource(dashboardCriteria);
			resultMap.put(EbsEvent.TRIAGING,triagingEbsSourceType);
			EbsSourceType signalVerificationEbsSourceType = getLatestSignalVerificationSource(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_VERIFICATION,signalVerificationEbsSourceType);
			EbsSourceType eventEbsSourceType = getLatestEbsEventSource(dashboardCriteria);
			resultMap.put(EbsEvent.EVENT,eventEbsSourceType);
			EbsSourceType riskAssessmentEbsSourceType = getLatestRiskAssessmentSource(dashboardCriteria);
			resultMap.put(EbsEvent.RISK_ASSESSMENT,riskAssessmentEbsSourceType);
			EbsSourceType alertEbsSourceType = getLatestAlertSource(dashboardCriteria);
			resultMap.put(EbsEvent.ALERT,alertEbsSourceType);

		}catch (Exception e){
			e.printStackTrace();
		}

		return resultMap;
	}

	public Map<EbsEvent, District> getLastReportedDistrictByEbsEvent(DashboardCriteria dashboardCriteria) {

		Map<EbsEvent, District> resultMap = new HashMap<>();

		try {

			District ebsEbsDistrict = getLatestSignalInformationDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_INFORMATION, ebsEbsDistrict);
			District triagingEbsDistrict = getLatestTriagingDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.TRIAGING,triagingEbsDistrict);
			District signalVerificationEbsDistrict = getLatestSignalVerificationDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.SIGNAL_VERIFICATION,signalVerificationEbsDistrict);
			District eventEbsDistrict = getLatestEbsEventDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.EVENT,eventEbsDistrict);
			District riskAssessmentEbsDistrict = getLatestRiskAssessmentDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.RISK_ASSESSMENT,riskAssessmentEbsDistrict);
			District alertEbsDistrict = getLatestAlertDistrict(dashboardCriteria);
			resultMap.put(EbsEvent.ALERT,alertEbsDistrict);

		}catch (Exception e){
			e.printStackTrace();
		}

		return resultMap;
	}

	private District getLatestSignalInformationDistrict(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<Ebs> root = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, root);
		Join<Ebs,Location> locationJoin= root.join(Ebs.EBS_LOCATION);
		Join<Location,District> districtJoin= locationJoin.join(Location.DISTRICT);

		cq.select(districtJoin);

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);

		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (filter != null) {
			cq.where(filter);
		}
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private District getLatestEbsEventDistrict(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<Ebs> root = cq.from(Ebs.class);

		Join<Ebs, SignalVerification> ebsJoin = root.join(Ebs.SIGNAL_VERIFICATION);
		Join<Ebs, Location> ebsLocationJoin = root.join(Ebs.EBS_LOCATION);
		Join<Location, District> locationDistrictJoin = ebsLocationJoin.join(Location.DISTRICT);

		Predicate filter = CriteriaBuilderHelper.and(cb, createCriteriaFilter(dashboardCriteria,cb,cq,root));
		if (filter != null) {
			cq.where(filter);
		}

		cq.select(locationDistrictJoin);

		cq.orderBy(cb.desc(ebsJoin.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public District getLatestSignalVerificationDistrict(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<Ebs> root = cq.from(Ebs.class);

		// Create joins for filtering
		Join<Ebs, Location> location = root.join(Ebs.EBS_LOCATION, JoinType.LEFT);
		Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);
		Join<Location, Region> region = null;

		// Only create region join if needed for region filtering
		if (dashboardCriteria.getRegion() != null) {
			region = location.join(Location.REGION, JoinType.LEFT);
		}

		Predicate filter = cb.isNotNull(root.get(Ebs.SIGNAL_VERIFICATION_ID));

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter (pre-filtering when district is specified)
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			Join<Ebs, SignalVerification> signalVerification = root.join(Ebs.SIGNAL_VERIFICATION, JoinType.LEFT);
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							signalVerification.get(SignalVerification.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.ARCHIVED)));

		cq.where(filter);
		cq.select(district);
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public District getLatestTriagingDistrict(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<Ebs> root = cq.from(Ebs.class);

		// Create joins for filtering
		Join<Ebs, Location> location = root.join(Ebs.EBS_LOCATION, JoinType.LEFT);
		Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);
		Join<Location, Region> region = null;

		// Only create region join if needed for region filtering
		if (dashboardCriteria.getRegion() != null) {
			region = location.join(Location.REGION, JoinType.LEFT);
		}

		Predicate filter = cb.isNotNull(root.get(Ebs.TRIAGING_ID));

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter (pre-filtering when district is specified)
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			Join<Ebs, Triaging> triaging = root.join(Ebs.TRIAGING, JoinType.LEFT);
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							triaging.get(Triaging.DATE_OF_DECISION),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(root.get(Ebs.ARCHIVED)));

		cq.where(filter);
		cq.select(district);
		cq.orderBy(cb.desc(root.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public District getLatestAlertDistrict(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<EbsAlert> root = cq.from(EbsAlert.class);

		// Create joins for filtering
		Join<EbsAlert, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
		Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);
		Join<Location, Region> region = null;

		// Only create region join if needed for region filtering
		if (dashboardCriteria.getRegion() != null) {
			region = location.join(Location.REGION, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(EbsAlert.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(district);
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public District getLatestRiskAssessmentDistrict(DashboardCriteria dashboardCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<District> cq = cb.createQuery(District.class);
		Root<RiskAssessment> root = cq.from(RiskAssessment.class);

		// Create joins for filtering
		Join<RiskAssessment, Ebs> ebs = root.join(Ebs.TABLE_NAME, JoinType.LEFT);
		Join<Ebs, Location> location = ebs.join(Ebs.EBS_LOCATION, JoinType.LEFT);
		Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);
		Join<Location, Region> region = null;

		// Only create region join if needed for region filtering
		if (dashboardCriteria.getRegion() != null) {
			region = location.join(Location.REGION, JoinType.LEFT);
		}

		Predicate filter = null;

		// Region filter
		if (dashboardCriteria.getRegion() != null && region != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(region.get(Region.UUID), dashboardCriteria.getRegion().getUuid())
			);
		}

		// District filter
		if (dashboardCriteria.getDistrict() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.equal(district.get(District.UUID), dashboardCriteria.getDistrict().getUuid())
			);
		}

		// Date filter if applicable
		if (dashboardCriteria.getDateFrom() != null && dashboardCriteria.getDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
					cb,
					filter,
					cb.between(
							root.get(RiskAssessment.CREATION_DATE),
							DateHelper.getStartOfDay(dashboardCriteria.getDateFrom()),
							DateHelper.getEndOfDay(dashboardCriteria.getDateTo())
					)
			);
		}

		// Exclude deleted and archived cases
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.DELETED)));
		filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(ebs.get(Ebs.ARCHIVED)));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(district);
		cq.orderBy(cb.desc(ebs.get(Ebs.CREATION_DATE)));

		TypedQuery<District> query = em.createQuery(cq);
		query.setMaxResults(1);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public Map<EbsSourceType, Integer> getSourceTypeCount(DashboardCriteria dashboardCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		cq.multiselect(
				ebs.get(Ebs.SOURCE_INFORMATION),
				cb.count(ebs)
		).groupBy(ebs.get(Ebs.SOURCE_INFORMATION));

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (filter != null) {
			cq.where(filter); // Add the filter to the query
		}

		TypedQuery<Tuple> query = em.createQuery(cq);
		List<Tuple> results = query.getResultList();

		Map<EbsSourceType, Integer> sourceTypeCountMap = new HashMap<>();
		for (Tuple tuple : results) {
			EbsSourceType sourceType = tuple.get(0, EbsSourceType.class); // Get source type
			Long count = tuple.get(1, Long.class); // Get count (as Long)
			sourceTypeCountMap.put(sourceType, count.intValue()); // Convert to Integer
		}

		return sourceTypeCountMap;
	}

	public List<EbsCategoryOfInformantDto> getEbsCategoryOfInformantDtoBySourceInformation(
			DashboardCriteria dashboardCriteria,
			EbsSourceType sourceType, EbsEvent ebsEvent) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Ebs> ebs = cq.from(Ebs.class);
		final EbsQueryContext ebsQueryContext = new EbsQueryContext(cb, cq, ebs);

		Predicate filter = ebsService.createUserFilter(ebsQueryContext);
		filter = CriteriaBuilderHelper.and(cb, filter, createEbsCriteriaFilter(dashboardCriteria, ebsQueryContext));

		if (sourceType != null && !sourceType.toString().isEmpty()) {
			Predicate sourceTypeFilter = cb.equal(ebs.get(Ebs.SOURCE_INFORMATION), sourceType);
			filter = CriteriaBuilderHelper.and(cb, filter, sourceTypeFilter);
		}

		if (ebsEvent != null) {
			switch (ebsEvent) {
				case TRIAGING:
					Predicate triagingFilter = cb.isNotNull(ebs.get(Ebs.TRIAGING_ID));
					filter = CriteriaBuilderHelper.and(cb, filter, triagingFilter);
					break;
				case SIGNAL_VERIFICATION:
					Predicate signalVerificationFilter = cb.isNotNull(ebs.get(Ebs.SIGNAL_VERIFICATION_ID));
					filter = CriteriaBuilderHelper.and(cb, filter, signalVerificationFilter);
					break;
				case RISK_ASSESSMENT:
					Join<Ebs, RiskAssessment> riskAssessmentJoin = ebs.join(Ebs.RISK_ASSESSMENT, JoinType.LEFT);
					Predicate riskAssessmentFilter = cb.isNotNull(riskAssessmentJoin.get(RiskAssessment.ID));
					filter = CriteriaBuilderHelper.and(cb, filter, riskAssessmentFilter);
					break;
				case ALERT:
					Join<Ebs, EbsAlert> ebsAlertJoin = ebs.join(Ebs.EBS_ALERT, JoinType.LEFT);
					Predicate ebsAlertFilter = cb.isNotNull(ebsAlertJoin.get(EbsAlert.ID));
					filter = CriteriaBuilderHelper.and(cb, filter, ebsAlertFilter);
					break;
				default:
					break;
			}
		}

		Predicate notNullCondition = cb.and(
				cb.isNotNull(ebs.get(Ebs.SOURCE_INFORMATION)),
				cb.isNotNull(ebs.get(Ebs.CATEGORY_OF_INFORMANT))
		);

		cq.where(CriteriaBuilderHelper.and(cb, filter, notNullCondition));

		cq.multiselect(
				ebs.get(Ebs.SOURCE_INFORMATION),
				ebs.get(Ebs.CATEGORY_OF_INFORMANT),
				cb.count(ebs)
    );

		cq.groupBy(
				ebs.get(Ebs.SOURCE_INFORMATION),
				ebs.get(Ebs.CATEGORY_OF_INFORMANT)
    );

		cq.orderBy(
				cb.asc(ebs.get(Ebs.SOURCE_INFORMATION)),
				cb.desc(cb.count(ebs))
		);

		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<EbsSourceType, Long> sourceTotals = new HashMap<>();

		for (Object[] row : results) {
			EbsSourceType source = (EbsSourceType) row[0];
			Long count = (Long) row[2];
			sourceTotals.merge(source, count, Long::sum);
		}

		List<EbsCategoryOfInformantDto> dtoList = new ArrayList<>();
		for (Object[] row : results) {
			EbsSourceType source = (EbsSourceType) row[0];
			PersonReporting category = (PersonReporting) row[1];
			Long count = (Long) row[2];

			double percentage = 0;
			if (sourceTotals.containsKey(source) && sourceTotals.get(source) > 0) {
				percentage = (count * 100.0) / sourceTotals.get(source);
				percentage = Math.ceil(percentage * 100) / 100;
			}

			dtoList.add(new EbsCategoryOfInformantDto(source, category, count, percentage));
		}

		return dtoList;
	}
}
