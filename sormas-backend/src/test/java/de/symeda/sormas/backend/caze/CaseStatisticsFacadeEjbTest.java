package de.symeda.sormas.backend.caze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.IntegerRange;
import de.symeda.sormas.api.Year;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.InvestigationStatus;
import de.symeda.sormas.api.infrastructure.PopulationDataDto;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttributeEnum;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCountDto;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCriteria;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttributeEnum;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.backend.AbstractBeanTest;
import de.symeda.sormas.backend.TestDataCreator.RDCF;
import de.symeda.sormas.backend.util.DateHelper8;

public class CaseStatisticsFacadeEjbTest extends AbstractBeanTest {

	@Test
	public void testQueryCaseCount() {

		RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		UserDto user = creator.createUser(
			rdcf.region.getUuid(),
			rdcf.district.getUuid(),
			rdcf.facility.getUuid(),
			"Surv",
			"Sup",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		PersonDto cazePerson = creator.createPerson("Case", "Person");
		cazePerson.setApproximateAge(30);
		cazePerson.setApproximateAgeReferenceDate(new Date());
		cazePerson.setApproximateAgeType(ApproximateAgeType.YEARS);
		cazePerson = getPersonFacade().savePerson(cazePerson);
		CaseDataDto caze = creator.createCase(
			user.toReference(),
			cazePerson.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcf);
		caze.setOutcomeDate(DateHelper.addWeeks(caze.getReportDate(), 2));
		caze = getCaseFacade().save(caze);

		StatisticsCaseCriteria criteria = new StatisticsCaseCriteria();
		int year = DateHelper8.toLocalDate(caze.getSymptoms().getOnsetDate()).getYear();
		criteria.years(Arrays.asList(new Year(year), new Year(year + 1)), StatisticsCaseAttributeEnum.ONSET_TIME);
		criteria.regions(Arrays.asList(new RegionReferenceDto(rdcf.region.getUuid(), null, null)));
		criteria.addAgeIntervals(Arrays.asList(new IntegerRange(10, 40)));
		
		//List<StatisticsCountDto> results = getCaseStatisticsFacade().queryCaseCount(criteria, null, null, null, null, false, false, null);
//		StatisticsCaseCriteria caseCriteria,
//		StatisticsCaseAttribute groupingA,
//		StatisticsCaseAttribute statisticsCaseAttribute,
//		StatisticsCaseAttribute groupingB, 
//		StatisticsCaseAttribute statisticsCaseAttribute2,
//		boolean includePopulation,
//		boolean includeZeroValues,
//		Integer populationReferenceYear
		List<StatisticsCaseCountDto> results = getCaseStatisticsFacade().queryCaseCount(criteria, null, null, null, null, false, false, null);
		// List should have one entry
		assertEquals(1, results.size());

		// try all groupings
		for (StatisticsCaseAttributeEnum groupingAttribute : StatisticsCaseAttributeEnum.values()) {
			StatisticsCaseSubAttributeEnum[] subAttributes = groupingAttribute.getSubAttributes();
			if (subAttributes.length == 0) {
				getCaseStatisticsFacade().queryCaseCount(criteria, groupingAttribute, null, null, null, false, false, null);
			} else {
				for (StatisticsCaseSubAttributeEnum subGroupingAttribute : groupingAttribute.getSubAttributes()) {
					if (subGroupingAttribute.isUsedForGrouping()) {
						getCaseStatisticsFacade().queryCaseCount(criteria, groupingAttribute, subGroupingAttribute, null, null, false, false, null);
					}
				}
			}
		}
	}

	@Test
	public void testQueryCaseCountZeroValues() {

		RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		UserDto user = creator.createUser(
			rdcf.region.getUuid(),
			rdcf.district.getUuid(),
			rdcf.facility.getUuid(),
			"Surv",
			"Sup",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		PersonDto cazePerson = creator.createPerson("Case", "Person");
		cazePerson.setApproximateAge(30);
		cazePerson.setApproximateAgeReferenceDate(new Date());
		cazePerson.setApproximateAgeType(ApproximateAgeType.YEARS);
		cazePerson = getPersonFacade().savePerson(cazePerson);
		CaseDataDto caze = creator.createCase(
			user.toReference(),
			cazePerson.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcf);
		caze = getCaseFacade().getCaseDataByUuid(caze.getUuid());

		StatisticsCaseCriteria criteria = new StatisticsCaseCriteria();
		int year = DateHelper8.toLocalDate(caze.getSymptoms().getOnsetDate()).getYear();
		criteria.years(Arrays.asList(new Year(year), new Year(year + 1)), StatisticsCaseAttributeEnum.ONSET_TIME);
		criteria.regions(Arrays.asList(new RegionReferenceDto(rdcf.region.getUuid(), null, null)));
		criteria.addAgeIntervals(Arrays.asList(new IntegerRange(10, 40)));
		
		//List<StatisticsCountDto> results = getCaseStatisticsFacade().queryCaseCount(criteria, StatisticsCaseAttribute.SEX, null, null, null, false, true, null);

		List<StatisticsCaseCountDto> results =
			getCaseStatisticsFacade().queryCaseCount(criteria, StatisticsCaseAttributeEnum.SEX, null, null, null, false, true, null);

		// List should have one entry per sex
		assertEquals(Sex.values().length, results.size());
	}

	@Test
	public void testQueryCaseCountPopulation() {

		RDCF rdcf = creator.createRDCF("Region", "District", "Community", "Facility");
		UserDto user = creator.createUser(
			rdcf.region.getUuid(),
			rdcf.district.getUuid(),
			rdcf.facility.getUuid(),
			"Surv",
			"Sup",
			creator.getUserRoleReference(DefaultUserRole.SURVEILLANCE_SUPERVISOR));
		PersonDto cazePerson = creator.createPerson("Case", "Person");
		cazePerson.setApproximateAge(30);
		cazePerson.setApproximateAgeReferenceDate(new Date());
		cazePerson.setApproximateAgeType(ApproximateAgeType.YEARS);
		cazePerson = getPersonFacade().savePerson(cazePerson);
		CaseDataDto caze = creator.createCase(
			user.toReference(),
			cazePerson.toReference(),
			Disease.EVD,
			CaseClassification.PROBABLE,
			InvestigationStatus.PENDING,
			new Date(),
			rdcf);
		caze = getCaseFacade().getCaseDataByUuid(caze.getUuid());

		StatisticsCaseCriteria criteria = new StatisticsCaseCriteria();
		criteria.regions(Arrays.asList(rdcf.region));

		//List<StatisticsCountDto> results = getCaseStatisticsFacade().queryCaseCount(criteria, StatisticsCaseAttribute.REGION_DISTRICT, StatisticsCaseSubAttribute.REGION, null, null, true, false, null);
		List<StatisticsCaseCountDto> results = getCaseStatisticsFacade()
			.queryCaseCount(criteria, StatisticsCaseAttributeEnum.JURISDICTION,
					StatisticsCaseSubAttributeEnum.REGION, 
					null,
					null,
					true,
					false,
					0);
		assertNull(results.get(0).getPopulation());

		PopulationDataDto populationData = PopulationDataDto.build(new Date());
		RegionDto region = getRegionFacade().getByUuid(rdcf.region.getUuid());
		region.setGrowthRate(10f);
		getRegionFacade().save(region);
		populationData.setRegion(rdcf.region);
		populationData.setPopulation(new Integer(10000));
		getPopulationDataFacade().savePopulationData(Arrays.asList(populationData));

		//results = getCaseStatisticsFacade().queryCaseCount(criteria, StatisticsCaseAttribute.REGION_DISTRICT, StatisticsCaseSubAttribute.REGION, null, null, true, false, LocalDate.now().getYear() + 2);
		results = getCaseStatisticsFacade().queryCaseCount(
			criteria,
			StatisticsCaseAttributeEnum.JURISDICTION,
			StatisticsCaseSubAttributeEnum.REGION,
			null,
			null,
			true,
			false,
			LocalDate.now().getYear() + 2);
		// List should have one entry
		assertEquals(Integer.valueOf(12214), results.get(0).getPopulation());
	}
}
