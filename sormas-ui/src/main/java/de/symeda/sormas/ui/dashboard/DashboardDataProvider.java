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
package de.symeda.sormas.ui.dashboard;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
import de.symeda.sormas.api.dashboard.EbsEventOutcomeDto;
import de.symeda.sormas.api.ebs.EbsEventBurdenDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.EbsTimeMetric;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.event.RiskLevel;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.symeda.sormas.api.Disease;
import org.apache.commons.lang3.time.DateUtils;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseReferenceDefinition;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.dashboard.DashboardCaseDto;
import de.symeda.sormas.api.dashboard.DashboardContactDto;
import de.symeda.sormas.api.dashboard.DashboardCriteria;
import de.symeda.sormas.api.dashboard.DashboardEventDto;
import de.symeda.sormas.api.dashboard.DashboardQuarantineDataDto;
import de.symeda.sormas.api.dashboard.NewDateFilterType;
import de.symeda.sormas.api.disease.DiseaseBurdenDto;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.outbreak.OutbreakCriteria;

import de.symeda.sormas.api.sample.DashboardTestResultDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleCountType;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;

// FIXME: 06/08/2020 this should be refactored into two specific data providers for case and contact dashboards
public class DashboardDataProvider extends AbstractDashboardDataProvider<DashboardCriteria> {

	private final Class<? extends CriteriaDateType> dateTypeClass;
	private DashboardType dashboardType;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private Disease disease;

	private EbsEvent ebsEvent;
	private Date fromDate;
	private Date toDate;
	private Date previousFromDate;
	private Date previousToDate;
	private CaseClassification caseClassification;
	private NewDateFilterType dateFilterType;

	private NewCaseDateType newCaseDateType ;
	//= NewCaseDateType.MOST_RELEVANT;

	// overall
	private List<DiseaseBurdenDto> diseasesBurden = new ArrayList<>();
	private DiseaseBurdenDto diseaseBurdenDetail;

	// TODO make disease specific when contact dashboard is updated
	private List<DashboardContactDto> contacts = new ArrayList<>();
	private List<DashboardContactDto> previousContacts = new ArrayList<>();

	// disease specific
	private List<DashboardCaseDto> cases = new ArrayList<>();
	private List<DashboardCaseDto> previousCases = new ArrayList<>();
	private Map<CaseClassification, Integer> casesCountByClassification = new HashMap<>();
	//List<PersonReporting> personReportingByEventSourceType;
	private Long outbreakDistrictCount = 0L;
	private String lastReportedDistrict = "";
	private List<DashboardEventDto> events = new ArrayList<>();
	private Map<PathogenTestResultType, Long> newCasesFinalLabResultCountByResultType;
	private Map<EventStatus, Long> eventCountByStatus;
	private List<DashboardTestResultDto> testResults = new ArrayList<>();
	private List<DashboardTestResultDto> previousTestResults = new ArrayList<>();
	private Map<SampleCountType, Long> sampleCount = new HashMap<SampleCountType, Long>();

	private Long contactsInQuarantineCount = 0L;
	private Long contactsPlacedInQuarantineCount = 0L;
	private Long casesInQuarantineCount = 0L;
	private Long casesPlacedInQuarantineCount = 0L;
	private Long contactsConvertedToCaseCount = 0L;
	private Map<SampleCountType, Long> previousSampleCount = new HashMap<SampleCountType, Long>();
	private Long caseWithReferenceDefinitionFulfilledCount = 0L;
	private Map<SampleCountType, Long> sampleCounts = new HashMap<SampleCountType, Long>();
	private Map<SampleCountType, Long> previousSampleCounts = new HashMap<SampleCountType, Long>();
	private List<EbsEventBurdenDto> ebsEventsBurden = new ArrayList<>();

	private Map<EbsSourceType, Integer> sourceTypeCount = new HashMap<>();
	private EbsSourceType eventSourceType;

	private EbsSourceType eventSourceTypeForFilter;

	private List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoBySourceType;
	private List<EbsCategoryOfInformantDto>  ebsCategoryOfInformantDtoBySourceTypeForHeb;
	private List<EbsCategoryOfInformantDto>  ebsCategoryOfInformantDtoBySourceTypeForMediaScan;
	private List<EbsCategoryOfInformantDto>  ebsCategoryOfInformantDtoBySourceTypeForHotline;

	private CommunityReferenceDto community;
	private Map<EbsSourceType, Map<SignalCategory, Integer>> signalCategoryBySourceType;
	private Map<EbsSourceType, Map<EbsTimeMetric, Integer>> timeframeCounts;

	private Map<EbsTimeMetric, Map<RiskAssesment, Integer>> riskAssessmentTimeMetric;

	private Map<EbsSourceType, Map<RiskAssesment, Integer>>riskAssessmentSourceType;

	private Map<EbsSourceType, Map<EbsTimeMetric, Integer>> timeMetricBySourceType;

	public DashboardDataProvider(Class<? extends CriteriaDateType> dateTypeClass) {
		this.dateTypeClass = dateTypeClass;
	}

	public DashboardDataProvider() {
		this.dateTypeClass = CriteriaDateType.class;
	}
	private List<RegionDto> regionDtoList;

	private Map<PathogenTestResultType, Long> testResultCountByResultType;
	private SignalCategory signalCategory;
	private RiskLevel riskLevel;

	private Map<EbsTimeMetric,Integer> ebsPendingTimeMetricByEbsEvent;

	private Map<EbsTimeMetric,Integer> ebsCompletedTimeMetricByEbsEvent;

	private List<EbsEventOutcomeDto>ebsEventOutcome;

	public void refreshData() {
		//this.getCriteria();
		// Update the entities lists according to the filters
		// Disease burden
		setDiseasesBurden(
			FacadeProvider.getDashboardFacade()
				.getDiseaseBurden(region, district, fromDate, toDate, previousFromDate, previousToDate, newCaseDateType, caseClassification));

		this.refreshDataForSelectedDisease();
	}




	@Override
	protected DashboardCriteria newCriteria() {
		return new DashboardCriteria();
	}

	private void refreshDataForQuarantinedContacts() {

		List<DashboardQuarantineDataDto> contactsInQuarantineDtos = getContacts().stream()
			.map(DashboardContactDto::getDashboardQuarantineDataDto)
			.filter(quarantineData(fromDate, toDate))
			.collect(Collectors.toList());

		setContactsInQuarantineCount((long) contactsInQuarantineDtos.size());

		Long dashboardContactsPlacedInQuarantineCount = getPlacedInQuarantine(contactsInQuarantineDtos);

		setContactsPlacedInQuarantineCount(dashboardContactsPlacedInQuarantineCount);
	}

	private void refreshDataForQuarantinedCases() {

		List<DashboardQuarantineDataDto> casesInQuarantineDtos = getCases().stream()
			.map(DashboardCaseDto::getDashboardQuarantineDataDto)
			.filter(quarantineData(fromDate, toDate))
			.collect(Collectors.toList());

		setCasesInQuarantineCount((long) casesInQuarantineDtos.size());

		Long dashboardCasesPlacedInQuarantineCount = getPlacedInQuarantine(casesInQuarantineDtos);

		setCasesPlacedInQuarantineCount(dashboardCasesPlacedInQuarantineCount);
	}

	private Predicate<DashboardQuarantineDataDto> quarantineData(Date fromDate, Date toDate) {

		return p -> {
			Date quarantineFrom = p.getQuarantineFrom();
			Date quarantineTo = p.getQuarantineTo();

			if (fromDate != null && toDate != null) {
				if (quarantineFrom != null && quarantineTo != null) {
					return quarantineTo.after(fromDate) && quarantineFrom.before(toDate);
				} else if (quarantineFrom != null) {
					return quarantineFrom.after(fromDate) && quarantineFrom.before(toDate);
				} else if (quarantineTo != null) {
					return quarantineTo.after(fromDate) && quarantineTo.before(toDate);
				}
			} else if (fromDate != null) {
				if (quarantineFrom != null) {
					return quarantineFrom.after(fromDate);
				} else if (quarantineTo != null) {
					return quarantineTo.after(fromDate);
				}
			} else if (toDate != null) {
				if (quarantineFrom != null) {
					return quarantineFrom.before(toDate);
				} else if (quarantineTo != null) {
					return quarantineTo.before(toDate);
				}
			}

			return false;
		};
	}

	private Long getPlacedInQuarantine(List<DashboardQuarantineDataDto> contactsInQuarantineDtos) {
		return contactsInQuarantineDtos.stream()
			.filter(
				dashboardQuarantineDataDto -> (dashboardQuarantineDataDto.getQuarantineFrom() != null
					&& fromDate.before(DateUtils.addDays(dashboardQuarantineDataDto.getQuarantineFrom(), 1))
					&& dashboardQuarantineDataDto.getQuarantineFrom().before(toDate)))
			.count();
	}

	private void refreshDataForConvertedContactsToCase() {

		DashboardCriteria dashboardCriteria = buildDashboardCriteriaWithDates();
		setContactsConvertedToCaseCount(FacadeProvider.getDashboardFacade().countCasesConvertedFromContacts(dashboardCriteria));
	}

	private void refreshDataForCasesWithReferenceDefinitionFulfilled() {
		List<DashboardCaseDto> casesWithReferenceDefinitionFulfilled =
			getCases().stream().filter(cases -> cases.getCaseReferenceDefinition() == CaseReferenceDefinition.FULFILLED).collect(Collectors.toList());
		setCaseWithReferenceDefinitionFulfilledCount(Long.valueOf(casesWithReferenceDefinitionFulfilled.size()));
	}
		
	public void refreshDiseaseData() {
		DiseaseBurdenDto dbd= FacadeProvider.getDiseaseFacade().getDiseaseForDashboard(region, district, disease, fromDate, toDate, previousFromDate, previousToDate
				,newCaseDateType
				,caseClassification);

		setDiseaseBurdenDetail(dbd);

		setOutbreakDistrictCount(
			FacadeProvider.getOutbreakFacade()
				.getOutbreakDistrictCount(
					new OutbreakCriteria().region(region).district(district).disease(disease).reportedBetween(fromDate, toDate).caseClassification(caseClassification)));

		this.refreshDataForSelectedDisease();
	}


	public void refreshDataForSelectedDisease() {

		// Update the entities lists according to the filters

		if (getDashboardType() == DashboardType.CONTACTS) {
			// Contacts
			setContacts(FacadeProvider.getContactFacade().getContactsForDashboard(region, district, disease, fromDate, toDate));
			setPreviousContacts(
					FacadeProvider.getContactFacade().getContactsForDashboard(region, district, disease, previousFromDate, previousToDate));

			this.refreshDataForQuarantinedContacts();
		}
		
		DashboardType dashboardType = getDashboardType();
		if (dashboardType == DashboardType.CONTACTS || this.disease != null) {
			DashboardCriteria caseDashboardCriteria = buildDashboardCriteria(fromDate, toDate);
			if (this.disease != null) {
				caseDashboardCriteria.setDisease(disease);
			}

			// Cases
			setCases(FacadeProvider.getDashboardFacade().getCases(caseDashboardCriteria));
			setLastReportedDistrict(FacadeProvider.getDashboardFacade().getLastReportedDistrictName(caseDashboardCriteria));
			setCasesCountByClassification(
					FacadeProvider.getDashboardFacade()
							.getCasesCountByClassification(caseDashboardCriteria.includeNotACaseClassification(true)));

			setPreviousCases(FacadeProvider.getDashboardFacade().getCases(buildDashboardCriteria(previousFromDate, previousToDate)));

			// test results
			if (getDashboardType() != DashboardType.CONTACTS) {
				setNewCasesFinalLabResultCountByResultType(
						FacadeProvider.getDashboardFacade().getNewCasesFinalLabResultCountByResultType(caseDashboardCriteria));
			}
		}

		if (this.disease == null || getDashboardType() == DashboardType.CONTACTS) {
			return;
		}

		// Events
		DashboardCriteria eventDashboardCriteria = buildDashboardCriteriaWithDates();
		setEvents(FacadeProvider.getDashboardFacade().getNewEvents(eventDashboardCriteria));
		setEventCountByStatus(FacadeProvider.getDashboardFacade().getEventCountByStatus(eventDashboardCriteria));

		setOutbreakDistrictCount(
				FacadeProvider.getOutbreakFacade()
						.getOutbreakDistrictCount(
								new OutbreakCriteria().region(region).district(district).disease(disease).reportedBetween(fromDate, toDate)));

		refreshDataForQuarantinedCases();
		refreshDataForConvertedContactsToCase();
		refreshDataForCasesWithReferenceDefinitionFulfilled();
	}

	public Map<PathogenTestResultType, Long> getTestResultCountByResultType() {
		return testResultCountByResultType;
	}

	public void setTestResultCountByResultType(Map<PathogenTestResultType, Long> testResults) {
		this.testResultCountByResultType = testResults;
	}

	private DashboardCriteria buildDashboardCriteria(Date fromDate, Date toDate) {
		return buildDashboardCriteria().newCaseDateType(newCaseDateType).dateBetween(fromDate, toDate);
	}

	public List<DashboardCaseDto> getCases() {
		return cases;
	}

	public void setCases(List<DashboardCaseDto> cases) {
		this.cases = cases;
	}

	public List<DashboardCaseDto> getPreviousCases() {
		return previousCases;
	}

	public void setPreviousCases(List<DashboardCaseDto> previousCases) {
		this.previousCases = previousCases;
	}

	public Map<CaseClassification, Integer> getCasesCountByClassification() {
		return casesCountByClassification;
	}

	public void setCasesCountByClassification(Map<CaseClassification, Integer> casesCountByClassification) {
		this.casesCountByClassification = casesCountByClassification;
	}

	public List<DashboardEventDto> getEvents() {
		return events;
	}

	public void setEvents(List<DashboardEventDto> events) {
		this.events = events;
	}

	public Map<EventStatus, Long> getEventCountByStatus() {
		return eventCountByStatus;
	}

	public void setEventCountByStatus(Map<EventStatus, Long> events) {
		this.eventCountByStatus = events;
	}

	public Map<PathogenTestResultType, Long> getNewCasesFinalLabResultCountByResultType() {
		return newCasesFinalLabResultCountByResultType;
	}

	public void setNewCasesFinalLabResultCountByResultType(Map<PathogenTestResultType, Long> labResults) {
		this.newCasesFinalLabResultCountByResultType = labResults;
	}

	public List<DashboardContactDto> getContacts() {
		return contacts;
	}

	public void setContacts(List<DashboardContactDto> contacts) {
		this.contacts = contacts;
	}

	public List<DashboardContactDto> getPreviousContacts() {
		return previousContacts;
	}

	public void setPreviousContacts(List<DashboardContactDto> previousContacts) {
		this.previousContacts = previousContacts;
	}

	public List<DiseaseBurdenDto> getDiseasesBurden() {
		return diseasesBurden;
	}

	public void setDiseasesBurden(List<DiseaseBurdenDto> diseasesBurden) {
		this.diseasesBurden = diseasesBurden;
	}

	public DiseaseBurdenDto getDiseaseBurdenDetail() {
		return diseaseBurdenDetail;
	}

	public void setDiseaseBurdenDetail(DiseaseBurdenDto diseaseBurdenDetail) {
		this.diseaseBurdenDetail = diseaseBurdenDetail;
	}

	public Long getOutbreakDistrictCount() {
		return outbreakDistrictCount;
	}

	public void setOutbreakDistrictCount(Long districtCount) {
		this.outbreakDistrictCount = districtCount;
	}

	public String getLastReportedDistrict() {
		return this.lastReportedDistrict;
	}

	public void setLastReportedDistrict(String district) {
		this.lastReportedDistrict = district;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;

		this.refreshDataForSelectedDisease();
	}

	public EbsEvent getEbsEvent() {
		return ebsEvent;
	}

	public void setEbsEvent(EbsEvent ebsEvent) {
		this.ebsEvent = ebsEvent;

		this.refreshDataForSelectedEbsEvent();
	}

	public void refreshDataForSelectedEbsEvent() {
		DashboardCriteria ebsDashboardCriteria = buildDashboardCriteria(fromDate, toDate);

		if ( this.ebsEvent != null) {
			if(this.ebsEvent==EbsEvent.SIGNAL_INFORMATION) {
				setLastReportedDistrict(FacadeProvider.getDashboardFacade().getLastReportedDistrictName(ebsDashboardCriteria));

				setSourceTypeCount(
						FacadeProvider.getDashboardFacade()
								.getSourceTypeCount(buildDashboardCriteria(fromDate, toDate)));


				setEbsCategoryOfInformantDtoBySourceType(
						FacadeProvider.getDashboardFacade()
								.getEbsCategoryOfInformantDtoBySourceInformation(EbsSourceType.CEBS, ebsEvent));

				setEbsCategoryOfInformantDtoBySourceTypeForHeb(
						FacadeProvider.getDashboardFacade()
								.getEbsCategoryOfInformantDtoBySourceInformation(EbsSourceType.HEBS, ebsEvent));

				setEbsCategoryOfInformantDtoBySourceTypeForMediaScan(
						FacadeProvider.getDashboardFacade()
								.getEbsCategoryOfInformantDtoBySourceInformation(EbsSourceType.MEDIA_NEWS, ebsEvent));

				setEbsCategoryOfInformantDtoBySourceTypeForHotline(
						FacadeProvider.getDashboardFacade()
								.getEbsCategoryOfInformantDtoBySourceInformation(EbsSourceType.HOTLINE_PERSON, ebsEvent));
			}

			if(this.ebsEvent.equals(EbsEvent.TRIAGING)){
				setEbsPendingTimeMetricByEbsEvent(
						FacadeProvider.getDashboardFacade()
									.getPendingCountByTimeMetric(ebsDashboardCriteria,this.ebsEvent));

				setEbsCompletedTimeMetricByEbsEvent(
						FacadeProvider.getDashboardFacade()
								.getCompletedCountByEbsTimeMetric(ebsDashboardCriteria,this.ebsEvent));

				setEbsEventOutcome(
						FacadeProvider.getDashboardFacade().getEbsEventOutcome(this.ebsEvent)
				);

				setSignalCategoryBySourceType(
						FacadeProvider.getDashboardFacade().getSignalCategoryBySourceType(ebsDashboardCriteria,this.ebsEvent)
				);

			}

			if(this.ebsEvent.equals(EbsEvent.SIGNAL_VERIFICATION)){
				setEbsPendingTimeMetricByEbsEvent(
						FacadeProvider.getDashboardFacade()
								.getPendingCountByTimeMetric(ebsDashboardCriteria,this.ebsEvent));

				setEbsEventOutcome(
						FacadeProvider.getDashboardFacade().getEbsEventOutcome(this.ebsEvent)
				);

				setTimeMetricBySourceType(
						FacadeProvider.getDashboardFacade().getTimeMetricBySourceType(ebsDashboardCriteria,this.ebsEvent)
				);
			}

			if(this.ebsEvent.equals(EbsEvent.RISK_ASSESSMENT)||this.ebsEvent.equals(EbsEvent.ALERT)){
				setRiskAssessmentSourceType(FacadeProvider.getDashboardFacade().getRiskAssessmentSourceType(ebsDashboardCriteria,this.ebsEvent));
				setRiskAssessmentTimeMetric(FacadeProvider.getDashboardFacade().getRiskAssessmentTimeMetric(ebsDashboardCriteria,this.ebsEvent));
			}
		}
	}

	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public NewDateFilterType getDateFilterType() {
		if (dateFilterType == NewDateFilterType.TODAY) {
			setFromDate(DateHelper.getStartOfDay(new Date()));
			setToDate(new Date());
		}
		if (dateFilterType == NewDateFilterType.YESTERDAY) {
			setFromDate(DateHelper.getStartOfDay(DateHelper.subtractDays(new Date(), 1)));
			setToDate(DateHelper.getEndOfDay(DateHelper.subtractDays(new Date(), 1)));
		}
		if (dateFilterType == NewDateFilterType.THIS_WEEK) {
			setFromDate(DateHelper.getStartOfWeek(new Date()));
			setToDate(new Date());
		}
		if (dateFilterType == NewDateFilterType.LAST_WEEK) {
			setFromDate(DateHelper.getStartOfWeek(DateHelper.subtractWeeks(new Date(), 1)));
			setToDate(DateHelper.getEndOfWeek(DateHelper.subtractWeeks(new Date(), 1)));
		}
		if (dateFilterType == NewDateFilterType.THIS_YEAR) {
			setFromDate(DateHelper.getStartOfWeek(DateHelper.getStartOfYear(new Date())));
			setToDate(new Date());
		}
		return dateFilterType;
	}

	public void setDateFilterType(NewDateFilterType dateFilterType) {
		this.dateFilterType = dateFilterType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getPreviousFromDate() {
		return previousFromDate;
	}

	public void setPreviousFromDate(Date previousFromDate) {
		this.previousFromDate = previousFromDate;
	}

	public Date getPreviousToDate() {
		return previousToDate;
	}

	public void setPreviousToDate(Date previousToDate) {
		this.previousToDate = previousToDate;
	}

	public NewCaseDateType getNewCaseDateType() {
		if (newCaseDateType == null) {
			return NewCaseDateType.MOST_RELEVANT;
		}
		return newCaseDateType;
	}

	public void setNewCaseDateType(NewCaseDateType newCaseDateType) {
		this.newCaseDateType = newCaseDateType;
	}

	public DashboardType getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(DashboardType dashboardType) {
		this.dashboardType = dashboardType;
	}

	public Long getContactsInQuarantineCount() {
		return contactsInQuarantineCount;
	}

	public void setContactsInQuarantineCount(Long contactsInQuarantineCount) {
		this.contactsInQuarantineCount = contactsInQuarantineCount;
	}

	public Long getContactsPlacedInQuarantineCount() {
		return contactsPlacedInQuarantineCount;
	}

	public void setContactsPlacedInQuarantineCount(Long contactsPlacedInQuarantineCount) {
		this.contactsPlacedInQuarantineCount = contactsPlacedInQuarantineCount;
	}

	public Long getCasesInQuarantineCount() {
		return casesInQuarantineCount;
	}

	public void setCasesInQuarantineCount(Long casesInQuarantineCount) {
		this.casesInQuarantineCount = casesInQuarantineCount;
	}

	public Long getCasesPlacedInQuarantineCount() {
		return casesPlacedInQuarantineCount;
	}

	public void setCasesPlacedInQuarantineCount(Long casesPlacedInQuarantineCount) {
		this.casesPlacedInQuarantineCount = casesPlacedInQuarantineCount;
	}

	public Long getContactsConvertedToCaseCount() {
		return contactsConvertedToCaseCount;
	}

	public void setContactsConvertedToCaseCount(Long contactsConvertedToCaseCount) {
		this.contactsConvertedToCaseCount = contactsConvertedToCaseCount;
	}

	public Map<SampleCountType, Long> getPreviousSampleCount() {
		return previousSampleCount;
	}

	public void setPreviousSampleCount(Map<SampleCountType, Long> previousSampleCount) {
		this.previousSampleCount = previousSampleCount;
	}

	public Map<SampleCountType, Long> getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(Map<SampleCountType, Long> sampleCount) {
		this.sampleCount = sampleCount;
	}
	
	public Long getCaseWithReferenceDefinitionFulfilledCount() {
		return caseWithReferenceDefinitionFulfilledCount;
	}

	public void setCaseWithReferenceDefinitionFulfilledCount(Long caseWithReferenceDefinitionFulfilledCount) {
		this.caseWithReferenceDefinitionFulfilledCount = caseWithReferenceDefinitionFulfilledCount;
	} 

	public Map<SampleCountType, Long> getSampleCounts() {
		return sampleCounts;
	}

	public void setSampleCounts(Map<SampleCountType, Long> sampleCounts) {
		this.sampleCounts = sampleCounts;
	}

	public Map<SampleCountType, Long> getPreviousSampleCounts() {
		return previousSampleCounts;
	}

	public void setPreviousSampleCounts(Map<SampleCountType, Long> previousSampleCounts) {
		this.previousSampleCounts = previousSampleCounts;
	}
	public List<RegionDto> getRegionDtoList() {
		return regionDtoList;
	}

	public void setRegionDtoList(List<RegionDto> regionDtoList) {
		this.regionDtoList = regionDtoList;
	}

	public Map<EbsSourceType, Integer> getSourceTypeCount() {
		return sourceTypeCount;
	}

	public void setSourceTypeCount(Map<EbsSourceType, Integer> sourceTypeCount) {
		this.sourceTypeCount = sourceTypeCount;
	}

	public DashboardCriteria getCriteria() {
		return new DashboardCriteria().region(region)
				.district(district)
				.community(community)
				.sourceInformation(eventSourceType)
				.signalCategory(signalCategory)
				.riskLevel(riskLevel)
				.disease(disease)
				.dateBetween(fromDate, toDate)
				.caseClassification(caseClassification)
				.newCaseDateType(newCaseDateType)
				.dateFilterType(dateFilterType);
	}

	public List<EbsEventBurdenDto> getEbsEventsBurden() {

		return ebsEventsBurden;
	}

//	public List<PersonReporting> getPersonReportingByEventSourceType() {
//		return personReportingByEventSourceType;
//	}
//
//	public void setPersonReportingByEventSourceType(List<PersonReporting> personReportingByEventSourceType) {
//		this.personReportingByEventSourceType = personReportingByEventSourceType;
//	}



	public void setEbsEventBurden(List<EbsEventBurdenDto> ebsEventsBurden) {
		this.ebsEventsBurden = ebsEventsBurden;
	}

	public void setEventSourceType(EbsSourceType eventSourceType) {
		this.eventSourceType = eventSourceType;
	}

	public EbsSourceType getEventSourceType() {
		return eventSourceType;
	}

	public EbsSourceType getEventSourceTypeForFilter() {
		return eventSourceTypeForFilter;
	}

	public void setEventSourceTypeForFilter(EbsSourceType eventSourceTypeForFilter) {
		this.eventSourceTypeForFilter = eventSourceTypeForFilter;
	}

	public List<EbsCategoryOfInformantDto> getEbsCategoryOfInformantDtoBySourceType() {
		return ebsCategoryOfInformantDtoBySourceType;
	}

	public void setEbsCategoryOfInformantDtoBySourceType(List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoBySourceType) {
		this.ebsCategoryOfInformantDtoBySourceType = ebsCategoryOfInformantDtoBySourceType;
	}

	public void refreshEbsData() {
		//this.getCriteria();
		// Update the entities lists according to the filters
		// EbsEvent burden
		setEbsEventBurden(
				FacadeProvider.getDashboardFacade()
						.getEbsEventBurden(region, district,community, fromDate, toDate, previousFromDate, previousToDate, newCaseDateType, signalCategory,eventSourceTypeForFilter,riskLevel));


		this.refreshDataForSelectedEbsEvent();
	}


	public List<EbsCategoryOfInformantDto> getEbsCategoryOfInformantDtoBySourceTypeForHeb() {
		return ebsCategoryOfInformantDtoBySourceTypeForHeb;
	}

	public void setEbsCategoryOfInformantDtoBySourceTypeForHeb(List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoBySourceTypeForHeb) {
		this.ebsCategoryOfInformantDtoBySourceTypeForHeb = ebsCategoryOfInformantDtoBySourceTypeForHeb;
	}

	public List<EbsCategoryOfInformantDto> getEbsCategoryOfInformantDtoBySourceTypeForMediaScan() {
		return ebsCategoryOfInformantDtoBySourceTypeForMediaScan;
	}

	public void setEbsCategoryOfInformantDtoBySourceTypeForMediaScan(List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoBySourceTypeForMediaScan) {
		this.ebsCategoryOfInformantDtoBySourceTypeForMediaScan = ebsCategoryOfInformantDtoBySourceTypeForMediaScan;
	}

	public List<EbsCategoryOfInformantDto> getEbsCategoryOfInformantDtoBySourceTypeForHotline() {
		return ebsCategoryOfInformantDtoBySourceTypeForHotline;
	}

	public void setEbsCategoryOfInformantDtoBySourceTypeForHotline(List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoBySourceTypeForHotline) {
		this.ebsCategoryOfInformantDtoBySourceTypeForHotline = ebsCategoryOfInformantDtoBySourceTypeForHotline;
	}

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public void setCommunity(CommunityReferenceDto community) {
		this.community = community;
	}

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public Map<EbsTimeMetric, Integer> getEbsPendingTimeMetricByEbsEvent() {
		return ebsPendingTimeMetricByEbsEvent;
	}

	public void setEbsPendingTimeMetricByEbsEvent(Map<EbsTimeMetric, Integer> ebsPendingTimeMetricByEbsEvent) {
		this.ebsPendingTimeMetricByEbsEvent = ebsPendingTimeMetricByEbsEvent;
	}

	public Map<EbsTimeMetric, Integer> getEbsCompletedTimeMetricByEbsEvent() {
		return ebsCompletedTimeMetricByEbsEvent;
	}

	public void setEbsCompletedTimeMetricByEbsEvent(Map<EbsTimeMetric, Integer> ebsCompletedTimeMetricByEbsEvent) {
		this.ebsCompletedTimeMetricByEbsEvent = ebsCompletedTimeMetricByEbsEvent;
	}


	public List<EbsEventOutcomeDto> getEbsEventOutcome() {
		return ebsEventOutcome;
	}

	public void setEbsEventOutcome(List<EbsEventOutcomeDto> ebsEventOutcome) {
		this.ebsEventOutcome = ebsEventOutcome;
	}

	public Map<EbsSourceType, Map<SignalCategory, Integer>> getSignalCategoryBySourceType() {
		return signalCategoryBySourceType;
	}

	public void setSignalCategoryBySourceType(Map<EbsSourceType, Map<SignalCategory, Integer>> signalCategoryBySourceType) {
		this.signalCategoryBySourceType = signalCategoryBySourceType;
	}

	public Map<EbsSourceType, Map<EbsTimeMetric, Integer>> getTimeframeCounts() {
		return timeframeCounts;
	}

	public void setTimeframeCounts(Map<EbsSourceType, Map<EbsTimeMetric, Integer>> timeframeCounts) {
		this.timeframeCounts = timeframeCounts;
	}

	public Map<EbsTimeMetric, Map<RiskAssesment, Integer>> getRiskAssessmentTimeMetric() {
		return riskAssessmentTimeMetric;
	}

	public void setRiskAssessmentTimeMetric(Map<EbsTimeMetric, Map<RiskAssesment, Integer>> riskAssessmentTimeMetric) {
		this.riskAssessmentTimeMetric = riskAssessmentTimeMetric;
	}

	public Map<EbsSourceType, Map<RiskAssesment, Integer>> getRiskAssessmentSourceType() {
		return riskAssessmentSourceType;
	}

	public void setRiskAssessmentSourceType(Map<EbsSourceType, Map<RiskAssesment, Integer>> riskAssessmentSourceType) {
		this.riskAssessmentSourceType = riskAssessmentSourceType;
	}

	public Map<EbsSourceType, Map<EbsTimeMetric, Integer>> getTimeMetricBySourceType() {
		return timeMetricBySourceType;
	}

	public void setTimeMetricBySourceType(Map<EbsSourceType, Map<EbsTimeMetric, Integer>> timeMetricBySourceType) {
		this.timeMetricBySourceType = timeMetricBySourceType;
	}


}
