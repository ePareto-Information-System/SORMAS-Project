package de.symeda.sormas.backend.contact;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.IntegerRange;
import de.symeda.sormas.api.contact.ContactStatisticsFacade;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.statistics.StatisticsContactAttribute;
import de.symeda.sormas.api.statistics.StatisticsContactCountDto;
import de.symeda.sormas.api.statistics.StatisticsContactCriteria;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.statistics.StatisticsHelper;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.backend.disease.DiseaseConfigurationFacadeEjb;
import de.symeda.sormas.backend.infrastructure.PopulationData;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.symptoms.Symptoms;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserRoleFacadeEjb;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless(name = "ContactStatisticsFacade")
public class ContactStatisticsFacadeEjb implements ContactStatisticsFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private CommunityService communityService;
	@EJB
	private DiseaseConfigurationFacadeEjb.DiseaseConfigurationFacadeEjbLocal diseaseConfigurationFacade;
	@EJB
	private ContactFacadeEjb.ContactFacadeEjbLocal contactFacade;
	@EJB
	private RegionFacadeEjb.RegionFacadeEjbLocal regionFacade;
	@EJB
	private DistrictFacadeEjb.DistrictFacadeEjbLocal districtFacade;
	@EJB
	private CommunityFacadeEjb.CommunityFacadeEjbLocal communityFacade;
	@EJB
	private FacilityFacadeEjb.FacilityFacadeEjbLocal facilityFacade;
	@EJB
	private UserRoleFacadeEjb.UserRoleFacadeEjbLocal userRoleFacade;

	@SuppressWarnings("unchecked")
	@Override
	public List<StatisticsContactCountDto> queryContactCount(
		StatisticsContactCriteria contactCriteria,
		StatisticsContactAttribute rowGrouping,
		StatisticsSubAttribute rowSubGrouping,
		StatisticsContactAttribute columnGrouping,
		StatisticsSubAttribute columnSubGrouping,
		boolean includePopulation,
		boolean includeZeroValues,
		Integer populationReferenceYear) {

		// contact counts
		Pair<String, List<Object>> contactCountQueryAndParams =
			buildContactCountQuery(contactCriteria, rowGrouping, rowSubGrouping, columnGrouping, columnSubGrouping);

		Query contactCountQuery = em.createNativeQuery(contactCountQueryAndParams.getKey().toString());
		for (int i = 0; i < contactCountQueryAndParams.getValue().size(); i++) {
			contactCountQuery.setParameter(i + 1, contactCountQueryAndParams.getValue().get(i));
		}

		Function<Integer, RegionReferenceDto> regionProvider = id -> regionFacade.getRegionReferenceById(id);
		Function<Integer, DistrictReferenceDto> districtProvider = id -> districtFacade.getDistrictReferenceById(id);
		Function<Integer, CommunityReferenceDto> communityProvider = id -> communityFacade.getCommunityReferenceById(id);
		Function<Integer, FacilityReferenceDto> healthFacilityProvider = id -> facilityFacade.getFacilityReferenceById(id);
		Function<Integer, UserRoleReferenceDto> userRoleProvider = id -> userRoleFacade.getUserRoleReferenceById(id);

		List<StatisticsContactCountDto> contactCountResults = ((Stream<Object[]>) contactCountQuery.getResultStream()).map(result -> {
			Object rowKey = "".equals(result[1]) ? null : result[1];
			Object columnKey = "".equals(result[2]) ? null : result[2];
			return new StatisticsContactCountDto(
				result[0] != null ? ((Number) result[0]).intValue() : null,
				null,
				StatisticsHelper.buildContactGroupingKey(
					rowKey,
					rowGrouping,
					rowSubGrouping,
					regionProvider,
					districtProvider,
					communityProvider,
					healthFacilityProvider,
					userRoleProvider),
				StatisticsHelper.buildContactGroupingKey(
					columnKey,
					columnGrouping,
					columnSubGrouping,
					regionProvider,
					districtProvider,
					communityProvider,
					healthFacilityProvider,
					userRoleProvider));
		}).collect(Collectors.toList());

		if (includeZeroValues) {
			List<StatisticsGroupingKey> allRowKeys;
			if (rowGrouping != null) {
				allRowKeys = (List<StatisticsGroupingKey>) contactCriteria.getFilterValuesForGrouping(rowGrouping, rowSubGrouping);
				if (allRowKeys == null) {
					allRowKeys = StatisticsHelper.getContactAttributeGroupingKeys(
						rowGrouping,
						rowSubGrouping,
						diseaseConfigurationFacade,
						contactFacade,
						regionFacade,
						districtFacade,
						userRoleFacade);
				}
			} else {
				allRowKeys = Arrays.asList((StatisticsGroupingKey) null);
			}
			List<StatisticsGroupingKey> allColumnKeys;
			if (columnGrouping != null) {
				allColumnKeys = (List<StatisticsGroupingKey>) contactCriteria.getFilterValuesForGrouping(columnGrouping, columnSubGrouping);
				if (allColumnKeys == null) {
					allColumnKeys = StatisticsHelper.getContactAttributeGroupingKeys(
						columnGrouping,
						columnSubGrouping,
						diseaseConfigurationFacade,
						contactFacade,
						regionFacade,
						districtFacade,
						userRoleFacade);
				}
			} else {
				allColumnKeys = Arrays.asList((StatisticsGroupingKey) null);
			}

			for (StatisticsGroupingKey rowKey : allRowKeys) {
				for (StatisticsGroupingKey columnKey : allColumnKeys) {
					StatisticsContactCountDto zeroDto = new StatisticsContactCountDto(0, null, rowKey, columnKey);
					if (!contactCountResults.contains(zeroDto)) {
						contactCountResults.add(zeroDto);
					}
				}
			}
		}

		// population
		if (includePopulation) {
			Pair<String, List<Object>> populationQueryAndParams =
				buildPopulationQuery(contactCriteria, rowGrouping, rowSubGrouping, columnGrouping, columnSubGrouping, populationReferenceYear);

			Query populationQuery = em.createNativeQuery(populationQueryAndParams.getKey().toString());
			for (int i = 0; i < populationQueryAndParams.getValue().size(); i++) {
				populationQuery.setParameter(i + 1, populationQueryAndParams.getValue().get(i));
			}

			List<StatisticsContactCountDto> populationResults = ((Stream<Object[]>) populationQuery.getResultStream()).map(result -> {
				Object rowKey = "".equals(result[1]) ? null : result[1];
				Object columnKey = "".equals(result[2]) ? null : result[2];
				return new StatisticsContactCountDto(
					null,
					result[0] != null ? ((Number) result[0]).intValue() : null,
					StatisticsHelper.buildContactGroupingKey(
						rowKey,
						rowGrouping,
						rowSubGrouping,
						regionProvider,
						districtProvider,
						communityProvider,
						healthFacilityProvider,
						userRoleProvider),
					StatisticsHelper.buildContactGroupingKey(
						columnKey,
						columnGrouping,
						columnSubGrouping,
						regionProvider,
						districtProvider,
						communityProvider,
						healthFacilityProvider,
						userRoleProvider));
			}).collect(Collectors.toList());

			boolean rowIsPopulation = rowGrouping != null && rowGrouping.isPopulationData();
			boolean columnIsPopulation = columnGrouping != null && columnGrouping.isPopulationData();
			if (!populationResults.isEmpty()) {
				assert ((populationResults.get(0).getRowKey() != null) == rowIsPopulation);
				assert ((populationResults.get(0).getColumnKey() != null) == columnIsPopulation);
			}

			// add the population data to the case counts
			// when a key is not a population data key, we use null instead
			StatisticsContactCountDto searchDto = new StatisticsContactCountDto(null, null, null, null);
			for (StatisticsContactCountDto contactCountResult : contactCountResults) {

				if (rowIsPopulation) {
					searchDto.setRowKey(contactCountResult.getRowKey());
				}
				if (columnIsPopulation) {
					searchDto.setColumnKey(contactCountResult.getColumnKey());
				}

				int index = populationResults.indexOf(searchDto);
				if (index >= 0) {
					contactCountResult.setPopulation(populationResults.get(index).getPopulation());
				}
			}
		}

		return contactCountResults;
	}

	/**
	 * private void replaceIdsWithGroupingKeys(List<StatisticsContactCountDto> results, StatisticsContactAttribute groupingA,
	 * for (StatisticsContactCountDto result : results) {
	 * Builds SQL query string and list of parameters (for filters)
	 */
	public Pair<String, List<Object>> buildContactCountQuery(
		StatisticsContactCriteria contactCriteria,
		StatisticsContactAttribute groupingA,
		StatisticsSubAttribute subGroupingA,
		StatisticsContactAttribute groupingB,
		StatisticsSubAttribute subGroupingB) {

		// Steps to build the query:
		// 1. Join the required tables
		// 2. Build the filter query
		// 3. Add selected groupings
		// 4. Retrieve and prepare the results

		/////////////
		// 1. Join tables that contact are grouped by or that are used in the contactCriteria
		/////////////

		StringBuilder contactJoinBuilder = new StringBuilder();
		if (subGroupingA == StatisticsSubAttribute.COMMUNITY || subGroupingB == StatisticsSubAttribute.COMMUNITY) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(Community.TABLE_NAME)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.COMMUNITY)
				.append("_id = ")
				.append(Community.TABLE_NAME)
				.append(".")
				.append(Community.ID);
		}
		if (subGroupingA == StatisticsSubAttribute.DISTRICT || subGroupingB == StatisticsSubAttribute.DISTRICT) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(District.TABLE_NAME)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.DISTRICT)
				.append("_id = ")
				.append(District.TABLE_NAME)
				.append(".")
				.append(District.ID);
		}
		if (subGroupingA == StatisticsSubAttribute.REGION || subGroupingB == StatisticsSubAttribute.REGION) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(Region.TABLE_NAME)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.REGION)
				.append("_id = ")
				.append(Region.TABLE_NAME)
				.append(".")
				.append(Region.ID);
		}

		if (groupingA == StatisticsContactAttribute.ONSET_TIME
			|| groupingB == StatisticsContactAttribute.ONSET_TIME
			|| contactCriteria.hasOnsetDate()) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(Symptoms.TABLE_NAME)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.PERSON)
				.append("_id")
				.append(" = ")
				.append(Symptoms.TABLE_NAME)
				.append(".")
				.append(Symptoms.ID);
		}

		if (groupingA == StatisticsContactAttribute.SEX
			|| groupingB == StatisticsContactAttribute.SEX
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_1_YEAR
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_1_YEAR
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_5_YEARS
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_5_YEARS
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_COARSE
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_COARSE
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_FINE
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_FINE
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_MEDIUM
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_CHILDREN_MEDIUM
			|| groupingA == StatisticsContactAttribute.AGE_INTERVAL_BASIC
			|| groupingB == StatisticsContactAttribute.AGE_INTERVAL_BASIC
			|| contactCriteria.getSexes() != null
			|| contactCriteria.getAgeIntervals() != null
			|| contactCriteria.getPersonRegions() != null
			|| contactCriteria.getPersonDistricts() != null
			|| contactCriteria.getPersonCommunities() != null
			|| contactCriteria.getPersonCity() != null
			|| contactCriteria.getPersonPostcode() != null) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(Person.TABLE_NAME)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.PERSON)
				.append("_id")
				.append(" = ")
				.append(Person.TABLE_NAME)
				.append(".")
				.append(Person.ID);
		}

		if (contactCriteria.getPersonRegions() != null
			|| contactCriteria.getPersonDistricts() != null
			|| contactCriteria.getPersonCommunities() != null
			|| contactCriteria.getPersonCity() != null
			|| contactCriteria.getPersonPostcode() != null) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(Location.TABLE_NAME)
				.append(" ON ")
				.append(Person.TABLE_NAME)
				.append(".")
				.append(Person.ADDRESS)
				.append("_id")
				.append(" = ")
				.append(Location.TABLE_NAME)
				.append(".")
				.append(Location.ID);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportingUserRoles())
			|| groupingA == StatisticsContactAttribute.REPORTING_USER_ROLE
			|| groupingB == StatisticsContactAttribute.REPORTING_USER_ROLE) {
			contactJoinBuilder.append(" LEFT JOIN ")
				.append(User.TABLE_NAME_USERROLES)
				.append(" ON ")
				.append(Contact.TABLE_NAME)
				.append(".")
				.append(Contact.REPORTING_USER)
				.append("_id")
				.append(" = ")
				.append(User.TABLE_NAME_USERROLES)
				.append(".")
				.append(UserDto.COLUMN_NAME_USER_ID);
		}

		/////////////
		// 2. Build filter based on contactCriteria
		/////////////

		StringBuilder contactFilterBuilder = new StringBuilder(" WHERE ");

		contactFilterBuilder.append("(").append(Contact.TABLE_NAME).append(".").append(Contact.DELETED).append(" = false");
		// needed for the full join on population
		contactFilterBuilder.append(" OR ").append(Contact.TABLE_NAME).append(".").append(Contact.DELETED).append(" IS NULL ");
		contactFilterBuilder.append(")");
		List<Object> filterBuilderParameters = new ArrayList<Object>();

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetYears())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"YEAR",
				Person.TABLE_NAME,
				Person.CREATION_DATE,
				contactCriteria.getOnsetYears(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetQuarters())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"QUARTER",
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetQuarters(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetMonths())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"MONTH",
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetMonths(),
				dateValue -> (dateValue.ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetEpiWeeks())) {
			extendFilterBuilderWithEpiWeek(
				contactFilterBuilder,
				filterBuilderParameters,
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetEpiWeeks(),
				value -> value.getWeek());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetQuartersOfYear())) {
			extendFilterBuilderWithQuarterOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetQuartersOfYear(),
				value -> value.getYear().getValue() * 10 + value.getQuarter().getValue());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetMonthsOfYear())) {
			extendFilterBuilderWithMonthOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetMonthsOfYear(),
				value -> value.getYear().getValue() * 100 + (value.getMonth().ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOnsetEpiWeeksOfYear())) {
			extendFilterBuilderWithEpiWeekOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE,
				contactCriteria.getOnsetEpiWeeksOfYear(),
				value -> value.getYear() * 100 + value.getWeek());
		}

		if (contactCriteria.getOnsetDateFrom() != null || contactCriteria.getOnsetDateTo() != null) {
			extendFilterBuilderWithDate(
				contactFilterBuilder,
				filterBuilderParameters,
				contactCriteria.getOnsetDateFrom(),
				contactCriteria.getOnsetDateTo(),
				Symptoms.TABLE_NAME,
				Symptoms.ONSET_DATE);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportYears())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"YEAR",
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportYears(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportQuarters())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"QUARTER",
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportQuarters(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportMonths())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"MONTH",
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportMonths(),
				dateValue -> (dateValue.ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportEpiWeeks())) {
			extendFilterBuilderWithEpiWeek(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportEpiWeeks(),
				value -> value.getWeek());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportQuartersOfYear())) {
			extendFilterBuilderWithQuarterOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportQuartersOfYear(),
				value -> value.getYear().getValue() * 10 + value.getQuarter().getValue());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportMonthsOfYear())) {
			extendFilterBuilderWithMonthOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportMonthsOfYear(),
				value -> value.getYear().getValue() * 100 + (value.getMonth().ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportEpiWeeksOfYear())) {
			extendFilterBuilderWithEpiWeekOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME,
				contactCriteria.getReportEpiWeeksOfYear(),
				value -> value.getYear() * 100 + value.getWeek());
		}

		if (contactCriteria.getReportDateFrom() != null || contactCriteria.getReportDateTo() != null) {
			extendFilterBuilderWithDate(
				contactFilterBuilder,
				filterBuilderParameters,
				contactCriteria.getReportDateFrom(),
				contactCriteria.getReportDateTo(),
				Contact.TABLE_NAME,
				Contact.REPORT_DATE_TIME);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeYears())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"YEAR",
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeYears(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeQuarters())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"QUARTER",
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeQuarters(),
				dateValue -> (dateValue.getValue()));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeMonths())) {
			extendFilterBuilderWithDateElement(
				contactFilterBuilder,
				filterBuilderParameters,
				"MONTH",
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeMonths(),
				dateValue -> (dateValue.ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeEpiWeeks())) {
			extendFilterBuilderWithEpiWeek(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeEpiWeeks(),
				value -> value.getWeek());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeQuartersOfYear())) {
			extendFilterBuilderWithQuarterOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeQuartersOfYear(),
				value -> value.getYear().getValue() * 10 + value.getQuarter().getValue());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeMonthsOfYear())) {
			extendFilterBuilderWithMonthOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeMonthsOfYear(),
				value -> value.getYear().getValue() * 100 + (value.getMonth().ordinal() + 1));
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getOutcomeEpiWeeksOfYear())) {
			extendFilterBuilderWithEpiWeekOfYear(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE,
				contactCriteria.getOutcomeEpiWeeksOfYear(),
				value -> value.getYear() * 100 + value.getWeek());
		}

		if (contactCriteria.getOutcomeDateFrom() != null || contactCriteria.getOutcomeDateTo() != null) {
			extendFilterBuilderWithDate(
				contactFilterBuilder,
				filterBuilderParameters,
				contactCriteria.getOutcomeDateFrom(),
				contactCriteria.getOutcomeDateTo(),
				Contact.TABLE_NAME,
				Contact.LAST_CONTACT_DATE);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getPersonRegions())) {
			List<Long> regionIds = regionService.getIdsByReferenceDtos(contactCriteria.getPersonRegions());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Location.TABLE_NAME,
				Location.REGION + "_id",
				regionIds,
				entry -> entry);
		}
		if (CollectionUtils.isNotEmpty(contactCriteria.getPersonDistricts())) {
			List<Long> districtIds = districtService.getIdsByReferenceDtos(contactCriteria.getPersonDistricts());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Location.TABLE_NAME,
				Location.DISTRICT + "_id",
				districtIds,
				entry -> entry);
		}
		if (CollectionUtils.isNotEmpty(contactCriteria.getPersonCommunities())) {
			List<Long> communityIds = communityService.getIdsByReferenceDtos(contactCriteria.getPersonCommunities());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Location.TABLE_NAME,
				Location.COMMUNITY + "_id",
				communityIds,
				entry -> entry);
		}
		if (StringUtils.isNotEmpty(contactCriteria.getPersonCity())) {
			extendFilterBuilderWithLike(contactFilterBuilder, Location.TABLE_NAME, Location.CITY, contactCriteria.getPersonCity());
		}
		if (StringUtils.isNotEmpty(contactCriteria.getPersonPostcode())) {
			extendFilterBuilderWithLike(contactFilterBuilder, Location.TABLE_NAME, Location.POSTAL_CODE, contactCriteria.getPersonPostcode());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getSexes()) || contactCriteria.isSexUnknown() != null) {
			if (contactFilterBuilder.length() > 0) {
				contactFilterBuilder.append(" AND ");
			}

			contactFilterBuilder.append("(");
			StringBuilder subFilterBuilder = new StringBuilder();

			if (CollectionUtils.isNotEmpty(contactCriteria.getSexes())) {
				extendFilterBuilderWithSimpleValue(
					subFilterBuilder,
					filterBuilderParameters,
					Person.TABLE_NAME,
					Person.SEX,
					contactCriteria.getSexes(),
					entry -> entry.name());
			}

			if (contactCriteria.isSexUnknown() != null) {
				if (subFilterBuilder.length() > 0) {
					subFilterBuilder.append(" OR ");
				}
				subFilterBuilder.append(Person.TABLE_NAME)
					.append(".")
					.append(Person.SEX)
					.append(" IS ")
					.append(contactCriteria.isSexUnknown() == true ? "NULL" : "NOT NULL");
			}

			contactFilterBuilder.append(subFilterBuilder);
			contactFilterBuilder.append(")");
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getAgeIntervals())) {
			if (contactFilterBuilder.length() > 0) {
				contactFilterBuilder.append(" AND ");
			}

			contactFilterBuilder.append("(");
			StringBuilder subFilterBuilder = new StringBuilder();

			Integer upperRangeBoundary = null;
			boolean appendUnknown = false;
			List<Integer> agesList = new ArrayList<Integer>();
			for (IntegerRange range : contactCriteria.getAgeIntervals()) {
				if (range.getTo() == null) {
					if (range.getFrom() == null) {
						appendUnknown = true;
					} else {
						upperRangeBoundary = range.getFrom();
					}
				} else {
					agesList.addAll(IntStream.rangeClosed(range.getFrom(), range.getTo()).boxed().collect(Collectors.toList()));
				}
			}

			if (agesList.size() > 0) {
				extendFilterBuilderWithSimpleValue(
					subFilterBuilder,
					filterBuilderParameters,
					Person.TABLE_NAME,
					Person.APPROXIMATE_AGE,
					agesList,
					value -> value);
			}

			if (upperRangeBoundary != null) {
				if (subFilterBuilder.length() > 0) {
					subFilterBuilder.append(" OR ");
				}
				subFilterBuilder.append(Person.TABLE_NAME)
					.append(".")
					.append(Person.APPROXIMATE_AGE)
					.append(" >= ?")
					.append(filterBuilderParameters.size() + 1);
				filterBuilderParameters.add(upperRangeBoundary);
			}

			if (appendUnknown) {
				if (subFilterBuilder.length() > 0) {
					subFilterBuilder.append(" OR ");
				}
				subFilterBuilder.append(Person.TABLE_NAME).append(".").append(Person.APPROXIMATE_AGE).append(" IS NULL");
			}

			contactFilterBuilder.append(subFilterBuilder);
			contactFilterBuilder.append(")");
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getDiseases())) {
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.DISEASE,
				contactCriteria.getDiseases(),
				entry -> entry.name());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getClassifications())) {
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.CONTACT_CLASSIFICATION,
				contactCriteria.getClassifications(),
				entry -> entry.name());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getStatus())) {
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.CONTACT_STATUS,
				contactCriteria.getStatus(),
				entry -> entry.name());
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getRegions())) {
			List<Long> regionIds = regionService.getIdsByReferenceDtos(contactCriteria.getRegions());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.REGION + "_id",
				regionIds,
				entry -> entry);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getDistricts())) {
			List<Long> districtIds = districtService.getIdsByReferenceDtos(contactCriteria.getDistricts());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.DISTRICT + "_id",
				districtIds,
				entry -> entry);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getCommunities())) {
			List<Long> communityIds = communityService.getIdsByReferenceDtos(contactCriteria.getCommunities());
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				Contact.TABLE_NAME,
				Contact.COMMUNITY + "_id",
				communityIds,
				entry -> entry);
		}

		if (CollectionUtils.isNotEmpty(contactCriteria.getReportingUserRoles())) {
			extendFilterBuilderWithSimpleValue(
				contactFilterBuilder,
				filterBuilderParameters,
				User.TABLE_NAME_USERROLES,
				UserDto.COLUMN_NAME_USERROLE,
				contactCriteria.getReportingUserRoles(),
				entry -> entry);
		}

		//////////////
		// 3. Add selected groupings
		/////////////

		String groupingSelectQueryA = null, groupingSelectQueryB = null;
		StringBuilder contactGroupByBuilder = new StringBuilder();
		StringBuilder orderByBuilder = new StringBuilder();
		String groupAAlias = "groupA";
		String groupBAlias = "groupB";

		if (groupingA != null || groupingB != null) {
			contactGroupByBuilder.append(" GROUP BY ");

			if (groupingA != null) {
				groupingSelectQueryA = buildContactGroupingSelectQuery(groupingA, subGroupingA, groupAAlias);
				contactGroupByBuilder.append(groupAAlias);
			}
			if (groupingB != null) {
				groupingSelectQueryB = buildContactGroupingSelectQuery(groupingB, subGroupingB, groupBAlias);
				if (groupingA != null) {
					contactGroupByBuilder.append(",");
				}
				contactGroupByBuilder.append(groupBAlias);
			}
		}

		//////////////
		// 4. Order results
		/////////////

		orderByBuilder.append(" ORDER BY ");
		if (groupingA != null) {
			orderByBuilder.append(groupAAlias).append(" NULLS LAST");
		}
		if (groupingB != null) {
			if (groupingA != null) {
				orderByBuilder.append(",");
			}
			orderByBuilder.append(groupBAlias).append(" NULLS LAST");
		}

		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("SELECT COUNT(*) AS contactcount ");

		if (groupingSelectQueryA != null) {
			queryBuilder.append(", ").append(groupingSelectQueryA);
		} else {
			queryBuilder.append(", null\\:\\:text AS ").append(groupAAlias);
		}
		if (groupingSelectQueryB != null) {
			queryBuilder.append(", ").append(groupingSelectQueryB);
		} else {
			queryBuilder.append(", null\\:\\:text AS ").append(groupBAlias);
		}

		queryBuilder.append(" FROM ")
			.append(Contact.TABLE_NAME)
			.append(contactJoinBuilder)
			.append(contactFilterBuilder)
			.append(contactGroupByBuilder);

		if (groupingA != null || groupingB != null) {
			queryBuilder.append(orderByBuilder);
		}

		return new ImmutablePair<String, List<Object>>(queryBuilder.toString(), filterBuilderParameters);
	}

	/**
	 * Builds SQL query string and list of parameters (for filters)
	 */
	public Pair<String, List<Object>> buildPopulationQuery(
		StatisticsContactCriteria contactCriteria,
		StatisticsContactAttribute groupingA,
		StatisticsSubAttribute subGroupingA,
		StatisticsContactAttribute groupingB,
		StatisticsSubAttribute subGroupingB,
		Integer populationReferenceYear) {

		////////
		// GROUP BY
		///////

		String groupAAlias = "groupA";
		String groupBAlias = "groupB";
		String groupASelect = buildPopulationGroupingSelect(groupingA, subGroupingA);
		String groupBSelect = buildPopulationGroupingSelect(groupingB, subGroupingB);

		StringBuilder groupByBuilder = new StringBuilder();
		if (groupASelect != null || groupBSelect != null) {
			groupByBuilder.append(" GROUP BY ");
			if (groupASelect != null) {
				groupByBuilder.append(groupAAlias);
			}
			if (groupBSelect != null) {
				if (groupASelect != null) {
					groupByBuilder.append(", ");
				}
				groupByBuilder.append(groupBAlias);
			}
		}

		if (groupASelect == null) {
			groupASelect = "null\\:\\:text";
		}
		groupASelect += " AS " + groupAAlias;

		if (groupBSelect == null) {
			groupBSelect = "null\\:\\:text";
		}
		groupBSelect += " AS " + groupBAlias;

		////////
		// WHERE
		///////

		StringBuilder whereBuilder = new StringBuilder();
		List<Object> filterBuilderParameters = new ArrayList<Object>();

		if (CollectionUtils.isNotEmpty(contactCriteria.getRegions())) {
			// limit to specific regions

			List<Long> regionIds = regionService.getIdsByReferenceDtos(contactCriteria.getRegions());
			extendFilterBuilderWithSimpleValue(
				whereBuilder,
				filterBuilderParameters,
				PopulationData.TABLE_NAME,
				PopulationData.REGION + "_id",
				regionIds,
				entry -> entry);
		}

		boolean usesCommunitys;
		List<Long> communityIds;
		if (CollectionUtils.isNotEmpty(contactCriteria.getCommunities())) {
			// limit to specific communitys

			communityIds = communityService.getIdsByReferenceDtos(contactCriteria.getCommunities());
			extendFilterBuilderWithSimpleValue(
				whereBuilder,
				filterBuilderParameters,
				PopulationData.TABLE_NAME,
				PopulationData.COMMUNITY + "_id",
				communityIds,
				entry -> entry);
			usesCommunitys = true;
		} else {
			// limit either to entries with community or to entries without community

			communityIds = null;
			usesCommunitys = subGroupingA == StatisticsSubAttribute.COMMUNITY || subGroupingB == StatisticsSubAttribute.COMMUNITY;

			if (whereBuilder.length() > 0) {
				whereBuilder.append(" AND ");
			}
			whereBuilder.append("(").append(PopulationData.TABLE_NAME).append(".").append(PopulationData.COMMUNITY).append("_id");
			if (usesCommunitys) {
				whereBuilder.append(" IS NOT NULL)");
			} else {
				// use entry with sum for all community
				whereBuilder.append(" IS NULL)");
			}
		}

		boolean usesDistricts;
		List<Long> districtIds;
		if (CollectionUtils.isNotEmpty(contactCriteria.getDistricts())) {
			// limit to specific districts

			districtIds = districtService.getIdsByReferenceDtos(contactCriteria.getDistricts());
			extendFilterBuilderWithSimpleValue(
				whereBuilder,
				filterBuilderParameters,
				PopulationData.TABLE_NAME,
				PopulationData.DISTRICT + "_id",
				districtIds,
				entry -> entry);
			usesDistricts = true;
		} else {
			// limit either to entries with district or to entries without district

			districtIds = null;
			usesDistricts = subGroupingA == StatisticsSubAttribute.DISTRICT || subGroupingB == StatisticsSubAttribute.DISTRICT;

			if (whereBuilder.length() > 0) {
				whereBuilder.append(" AND ");
			}
			whereBuilder.append("(").append(PopulationData.TABLE_NAME).append(".").append(PopulationData.DISTRICT).append("_id");
			if (usesDistricts || usesCommunitys) {
				whereBuilder.append(" IS NOT NULL)");
			} else {
				// use entry with sum for all districts
				whereBuilder.append(" IS NULL)");
			}
		}

		// sex
		whereBuilder.append(" AND (");
		if (CollectionUtils.isNotEmpty(contactCriteria.getSexes())) {

			StringBuilder subFilterBuilder = new StringBuilder();
			extendFilterBuilderWithSimpleValue(
				subFilterBuilder,
				filterBuilderParameters,
				PopulationData.TABLE_NAME,
				PopulationData.SEX,
				contactCriteria.getSexes(),
				entry -> entry.name());
			whereBuilder.append(subFilterBuilder);

		} else {

			boolean usesSex = groupingA == StatisticsContactAttribute.SEX || groupingB == StatisticsContactAttribute.SEX;
			whereBuilder.append(PopulationData.TABLE_NAME).append(".").append(PopulationData.SEX);
			if (usesSex) {
				whereBuilder.append(" IS NOT NULL");
			} else {
				// use entry with sum for all sexes
				whereBuilder.append(" IS NULL");
			}
		}
		whereBuilder.append(")");

		// age group
		whereBuilder.append(" AND (");
		if (CollectionUtils.isNotEmpty(contactCriteria.getAgeIntervals())) {

			List<AgeGroup> ageGroups = contactCriteria.getAgeIntervals().stream().map(ageInterval -> {
				AgeGroup ageGroup = AgeGroup.getAgeGroupFromIntegerRange(ageInterval);
				if (ageGroup == null) {
					throw new IllegalArgumentException("Could not map integer range to age group: " + ageInterval.toString());
				}
				return ageGroup;
			}).collect(Collectors.toList());

			StringBuilder subFilterBuilder = new StringBuilder();
			extendFilterBuilderWithSimpleValue(
				subFilterBuilder,
				filterBuilderParameters,
				PopulationData.TABLE_NAME,
				PopulationData.AGE_GROUP,
				ageGroups,
				entry -> entry.name());
			whereBuilder.append(subFilterBuilder);

		} else {

			boolean usesAgeGroup =
				groupingA == StatisticsContactAttribute.AGE_INTERVAL_5_YEARS || groupingB == StatisticsContactAttribute.AGE_INTERVAL_5_YEARS;
			whereBuilder.append(PopulationData.TABLE_NAME).append(".").append(PopulationData.AGE_GROUP);
			if (usesAgeGroup) {
				whereBuilder.append(" IS NOT NULL");
			} else {
				// use entry with sum for all sexes
				whereBuilder.append(" IS NULL");
			}
		}
		whereBuilder.append(")");

		whereBuilder.insert(0, " WHERE ");

		////////
		// SELECT
		///////

		StringBuilder selectBuilder = new StringBuilder(" SELECT SUM(").append(PopulationData.POPULATION)
			.append("*EXP(growthsource.growthrate*0.01*")
			.append("(?")
			.append(filterBuilderParameters.size() + 1)
			.append(" - date_part('year', " + PopulationData.COLLECTION_DATE)
			.append("\\:\\:timestamp)")
			.append("))) AS population, ");

		if (populationReferenceYear == null) {
			filterBuilderParameters.add(LocalDate.now().getYear());
		} else {
			filterBuilderParameters.add(populationReferenceYear);
		}
		selectBuilder.append(groupASelect).append(", ").append(groupBSelect);
		selectBuilder.append(" FROM ").append(PopulationData.TABLE_NAME);

		// growth rates to calculate the population
		selectBuilder.append(" LEFT JOIN ");
		if (communityIds != null || subGroupingA == StatisticsSubAttribute.COMMUNITY || subGroupingB == StatisticsSubAttribute.COMMUNITY) {
			selectBuilder.append(Community.TABLE_NAME)
				.append(" AS growthsource ON growthsource.")
				.append(Community.ID)
				.append(" = ")
				.append(PopulationData.COMMUNITY)
				.append("_id");
		} else if (districtIds != null || subGroupingA == StatisticsSubAttribute.DISTRICT || subGroupingB == StatisticsSubAttribute.DISTRICT) {
			selectBuilder.append(District.TABLE_NAME)
				.append(" AS growthsource ON growthsource.")
				.append(District.ID)
				.append(" = ")
				.append(PopulationData.DISTRICT)
				.append("_id");
		} else {
			selectBuilder.append(Region.TABLE_NAME)
				.append(" AS growthsource ON growthsource.")
				.append(Region.ID)
				.append(" = ")
				.append(PopulationData.REGION)
				.append("_id");
		}

		////////
		// ORDER BY
		///////

		StringBuilder orderByBuilder = new StringBuilder();
		orderByBuilder.append(" ORDER BY ").append(groupAAlias).append(" NULLS LAST, ").append(groupBAlias).append(" NULLS LAST");

		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append(selectBuilder);
		queryBuilder.append(whereBuilder);
		queryBuilder.append(groupByBuilder);
		queryBuilder.append(orderByBuilder);

		return new ImmutablePair<String, List<Object>>(queryBuilder.toString(), filterBuilderParameters);
	}

	private String buildPopulationGroupingSelect(StatisticsContactAttribute grouping, StatisticsSubAttribute subGrouping) {

		if (grouping != null) {
			switch (grouping) {
			case JURISDICTION: {
				switch (subGrouping) {
				case REGION:
					return PopulationData.TABLE_NAME + "." + PopulationData.REGION + "_id";
				case DISTRICT:
					return PopulationData.TABLE_NAME + "." + PopulationData.DISTRICT + "_id";
				case COMMUNITY:
					return PopulationData.TABLE_NAME + "." + PopulationData.COMMUNITY + "_id";
				default:
					return null;
				}
			}
			case SEX:
				return PopulationData.TABLE_NAME + "." + PopulationData.SEX;
			case AGE_INTERVAL_5_YEARS:
				return PopulationData.TABLE_NAME + "." + PopulationData.AGE_GROUP;
			default:
				return null;
			}
		}
		return null;
	}

	private void extendFilterBuilderWithLike(StringBuilder filterBuilder, String tableName, String fieldName, String filterValue) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append(tableName).append(".").append(fieldName).append(" LIKE ").append("'%").append(filterValue).append("%'");
	}

	private <T> StringBuilder extendFilterBuilderWithSimpleValue(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, ?> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append(tableName).append(".").append(fieldName).append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private StringBuilder extendFilterBuilderWithDate(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		Date from,
		Date to,
		String tableName,
		String fieldName) {

		if (from != null || to != null) {
			if (filterBuilder.length() > 0) {
				filterBuilder.append(" AND ");
			}

			if (from != null && to != null) {
				filterBuilder.append(tableName).append(".").append(fieldName).append(" BETWEEN ?").append(filterBuilderParameters.size() + 1);
				filterBuilderParameters.add(from);
				filterBuilder.append(" AND ?").append(filterBuilderParameters.size() + 1).append("");
				filterBuilderParameters.add(to);
			} else if (from != null) {
				filterBuilder.append(tableName).append(".").append(fieldName).append(" >= ?").append(filterBuilderParameters.size() + 1);
				filterBuilderParameters.add(from);
			} else {
				filterBuilder.append(tableName).append(".").append(fieldName).append(" <= ?").append(filterBuilderParameters.size() + 1);
				filterBuilderParameters.add(to);
			}
		}

		return filterBuilder;
	}

	private <T> StringBuilder extendFilterBuilderWithDateElement(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String dateElementToExtract,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, Integer> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append("(CAST(EXTRACT(" + dateElementToExtract + " FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(")  AS integer))")
			.append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private <T> StringBuilder extendFilterBuilderWithEpiWeek(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, Integer> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append("epi_week(").append(tableName).append(".").append(fieldName).append(")").append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private <T> StringBuilder extendFilterBuilderWithEpiWeekOfYear(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, Integer> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append("(epi_year(")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(")")
			.append(" * 100")
			.append(" + epi_week(")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append("))")
			.append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private <T> StringBuilder extendFilterBuilderWithQuarterOfYear(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, Integer> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append("(CAST(EXTRACT(YEAR FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(")")
			.append(" * 10 AS integer)) + (CAST(EXTRACT(QUARTER FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") AS integer))")
			.append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private <T> StringBuilder extendFilterBuilderWithMonthOfYear(
		StringBuilder filterBuilder,
		List<Object> filterBuilderParameters,
		String tableName,
		String fieldName,
		List<T> values,
		Function<T, Integer> valueMapper) {

		if (filterBuilder.length() > 0) {
			filterBuilder.append(" AND ");
		}

		filterBuilder.append("(CAST(EXTRACT(YEAR FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(")")
			.append(" * 100 AS integer)) + (CAST(EXTRACT(MONTH FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") AS integer))")
			.append(" IN ");
		return QueryHelper.appendInFilterValues(filterBuilder, filterBuilderParameters, values, valueMapper);
	}

	private String buildContactGroupingSelectQuery(StatisticsContactAttribute grouping, StatisticsSubAttribute subGrouping, String groupAlias) {

		StringBuilder groupingSelectPartBuilder = new StringBuilder();
		switch (grouping) {
		case SEX:
			groupingSelectPartBuilder.append(Person.TABLE_NAME).append(".").append(Person.SEX).append(" AS ").append(groupAlias);
			break;
		case DISEASE:
			groupingSelectPartBuilder.append(Contact.TABLE_NAME).append(".").append(Contact.DISEASE).append(" AS ").append(groupAlias);
			break;
		case CLASSIFICATION:
			groupingSelectPartBuilder.append(Contact.TABLE_NAME).append(".").append(Contact.CONTACT_CLASSIFICATION).append(" AS ").append(groupAlias);
			break;
		case STATUS:
			groupingSelectPartBuilder.append(Contact.TABLE_NAME).append(".").append(Contact.CONTACT_STATUS).append(" AS ").append(groupAlias);
			break;
		case JURISDICTION: {
			switch (subGrouping) {
			case REGION:
				groupingSelectPartBuilder.append(Region.TABLE_NAME).append(".").append(Region.ID).append(" AS ").append(groupAlias);
				break;
			case DISTRICT:
				groupingSelectPartBuilder.append(District.TABLE_NAME).append(".").append(District.ID).append(" AS ").append(groupAlias);
				break;
			case COMMUNITY:
				groupingSelectPartBuilder.append(Community.TABLE_NAME).append(".").append(Community.ID).append(" AS ").append(groupAlias);
				break;
			default:
				throw new IllegalArgumentException(subGrouping.toString());
			}
			break;
		}
		case AGE_INTERVAL_1_YEAR:
		case AGE_INTERVAL_5_YEARS:
		case AGE_INTERVAL_CHILDREN_COARSE:
		case AGE_INTERVAL_CHILDREN_FINE:
		case AGE_INTERVAL_CHILDREN_MEDIUM:
		case AGE_INTERVAL_BASIC:
			extendGroupingBuilderWithAgeInterval(groupingSelectPartBuilder, grouping, groupAlias);
			break;
		case ONSET_TIME:
			switch (subGrouping) {
			case YEAR:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "YEAR", Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case QUARTER:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "QUARTER", Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case MONTH:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "MONTH", Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case EPI_WEEK:
				extendGroupingBuilderWithEpiWeek(groupingSelectPartBuilder, Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case QUARTER_OF_YEAR:
				extendGroupingBuilderWithQuarterOfYear(groupingSelectPartBuilder, Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case MONTH_OF_YEAR:
				extendGroupingBuilderWithMonthOfYear(groupingSelectPartBuilder, Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			case EPI_WEEK_OF_YEAR:
				extendGroupingBuilderWithEpiWeekOfYear(groupingSelectPartBuilder, Symptoms.TABLE_NAME, Symptoms.ONSET_DATE, groupAlias);
				break;
			default:
				throw new IllegalArgumentException(subGrouping.toString());
			}
			break;
		case REPORT_TIME:
			switch (subGrouping) {
			case YEAR:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "YEAR", Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case QUARTER:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "QUARTER", Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case MONTH:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "MONTH", Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case EPI_WEEK:
				extendGroupingBuilderWithEpiWeek(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case QUARTER_OF_YEAR:
				extendGroupingBuilderWithQuarterOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case MONTH_OF_YEAR:
				extendGroupingBuilderWithMonthOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			case EPI_WEEK_OF_YEAR:
				extendGroupingBuilderWithEpiWeekOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.REPORT_DATE_TIME, groupAlias);
				break;
			default:
				throw new IllegalArgumentException(subGrouping.toString());
			}
			break;
		case OUTCOME_TIME:
			switch (subGrouping) {
			case YEAR:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "YEAR", Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case QUARTER:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "QUARTER", Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case MONTH:
				extendGroupingBuilderWithDate(groupingSelectPartBuilder, "MONTH", Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case EPI_WEEK:
				extendGroupingBuilderWithEpiWeek(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case QUARTER_OF_YEAR:
				extendGroupingBuilderWithQuarterOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case MONTH_OF_YEAR:
				extendGroupingBuilderWithMonthOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			case EPI_WEEK_OF_YEAR:
				extendGroupingBuilderWithEpiWeekOfYear(groupingSelectPartBuilder, Contact.TABLE_NAME, Contact.LAST_CONTACT_DATE, groupAlias);
				break;
			default:
				throw new IllegalArgumentException(subGrouping.toString());
			}
			break;
		case REPORTING_USER_ROLE:
			groupingSelectPartBuilder.append(User.TABLE_NAME_USERROLES)
				.append(".")
				.append(UserDto.COLUMN_NAME_USERROLE)
				.append(" AS ")
				.append(groupAlias);
			break;
		default:
			throw new IllegalArgumentException(grouping.toString());
		}
		return groupingSelectPartBuilder.toString();
	}

	private void extendGroupingBuilderWithDate(
		StringBuilder groupingBuilder,
		String dateToExtract,
		String tableName,
		String fieldName,
		String groupAlias) {

		groupingBuilder.append("(CAST(EXTRACT(" + dateToExtract + " FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") AS integer)) AS ")
			.append(groupAlias);
	}

	private void extendGroupingBuilderWithEpiWeek(StringBuilder groupingBuilder, String tableName, String fieldName, String groupAlias) {
		groupingBuilder.append("epi_week(").append(tableName).append(".").append(fieldName).append(") AS ").append(groupAlias);
	}

	private void extendGroupingBuilderWithEpiWeekOfYear(StringBuilder groupingBuilder, String tableName, String fieldName, String groupAlias) {

		groupingBuilder.append("(epi_year(")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") * 100")
			.append(" + epi_week(")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(")) AS ")
			.append(groupAlias);
	}

	private void extendGroupingBuilderWithQuarterOfYear(StringBuilder groupingBuilder, String tableName, String fieldName, String groupAlias) {

		groupingBuilder.append("((CAST(EXTRACT(YEAR FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") * 10 AS integer)))")
			.append(" + (CAST(EXTRACT(QUARTER FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") AS integer)) AS ")
			.append(groupAlias);
	}

	private void extendGroupingBuilderWithMonthOfYear(StringBuilder groupingBuilder, String tableName, String fieldName, String groupAlias) {

		groupingBuilder.append("((CAST(EXTRACT(YEAR FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") * 100 AS integer)))")
			.append(" + (CAST(EXTRACT(MONTH FROM ")
			.append(tableName)
			.append(".")
			.append(fieldName)
			.append(") AS integer)) AS ")
			.append(groupAlias);
	}

	private void extendGroupingBuilderWithAgeInterval(StringBuilder groupingBuilder, StatisticsContactAttribute grouping, String groupAlias) {

		groupingBuilder.append("CASE ");
		switch (grouping) {
		case AGE_INTERVAL_1_YEAR:
			for (int i = 0; i < 80; i++) {
				groupingBuilder.append("WHEN ")
					.append(Person.TABLE_NAME)
					.append(".")
					.append(Person.APPROXIMATE_AGE)
					.append(" = ")
					.append(i < 10 ? "0" + i : i)
					.append(" THEN ")
					.append("'")
					.append(i < 10 ? "0" + i : i)
					.append("' ");
			}
			break;
		case AGE_INTERVAL_5_YEARS:
			for (AgeGroup ageGroup : AgeGroup.values()) {
				addAgeGroupToStringBuilder(groupingBuilder, ageGroup);
			}
			break;
		case AGE_INTERVAL_CHILDREN_COARSE:
			addAgeIntervalToStringBuilder(groupingBuilder, 0, 14);
			for (int i = 15; i < 30; i += 5) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 4);
			}
			for (int i = 30; i < 80; i += 10) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 9);
			}
			break;
		case AGE_INTERVAL_CHILDREN_FINE:
			for (int i = 0; i < 5; i++) {
				groupingBuilder.append("WHEN ")
					.append(Person.TABLE_NAME)
					.append(".")
					.append(Person.APPROXIMATE_AGE)
					.append(" = ")
					.append(i)
					.append(" THEN ")
					.append("'")
					.append("0" + i)
					.append("-")
					.append("0" + i)
					.append("' ");
			}
			for (int i = 5; i < 30; i += 5) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 4);
			}
			for (int i = 30; i < 80; i += 10) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 9);
			}
			break;
		case AGE_INTERVAL_CHILDREN_MEDIUM:
			for (int i = 0; i < 30; i += 5) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 4);
			}
			for (int i = 30; i < 80; i += 10) {
				addAgeIntervalToStringBuilder(groupingBuilder, i, 9);
			}
			break;
		case AGE_INTERVAL_BASIC:
			addAgeIntervalToStringBuilder(groupingBuilder, 0, 0);
			addAgeIntervalToStringBuilder(groupingBuilder, 1, 3);
			addAgeIntervalToStringBuilder(groupingBuilder, 5, 9);
			groupingBuilder.append("WHEN ").append(Person.TABLE_NAME).append(".").append(Person.APPROXIMATE_AGE).append(" >= 15 THEN '15+' ");
			break;
		default:
			throw new IllegalArgumentException(grouping.toString());
		}

		if (grouping != StatisticsContactAttribute.AGE_INTERVAL_BASIC && grouping != StatisticsContactAttribute.AGE_INTERVAL_5_YEARS) {
			groupingBuilder.append("WHEN ").append(Person.TABLE_NAME).append(".").append(Person.APPROXIMATE_AGE).append(" >= 80 THEN '80+' ");
		}
		groupingBuilder.append("ELSE NULL END AS " + groupAlias);
	}

	private void addAgeIntervalToStringBuilder(StringBuilder groupingBuilder, int number, int increase) {

		String lowerNumberString = number < 10 ? "0" + number : String.valueOf(number);
		String higherNumberString = number + increase < 10 ? "0" + (number + increase) : String.valueOf(number + increase);
		groupingBuilder.append("WHEN ")
			.append(Person.TABLE_NAME)
			.append(".")
			.append(Person.APPROXIMATE_AGE)
			.append(" BETWEEN ")
			.append(number)
			.append(" AND ")
			.append(number + increase)
			.append(" THEN '")
			.append(lowerNumberString)
			.append("-")
			.append(higherNumberString)
			.append("' ");
	}

	private void addAgeGroupToStringBuilder(StringBuilder groupingBuilder, AgeGroup ageGroup) {

		IntegerRange ageRange = ageGroup.toIntegerRange();
		groupingBuilder.append("WHEN ").append(Person.TABLE_NAME).append(".").append(Person.APPROXIMATE_AGE);
		if (ageRange.getTo() == null) {
			groupingBuilder.append(" >= ").append(ageRange.getFrom());
		} else {
			groupingBuilder.append(" BETWEEN ").append(ageRange.getFrom()).append(" AND ").append(ageRange.getTo());
		}
		groupingBuilder.append(" THEN '").append(ageGroup.name()).append("' ");
	}

	@LocalBean
	@Stateless
	public static class ContactStatisticsFacadeEjbLocal extends ContactStatisticsFacadeEjb {

	}
}
