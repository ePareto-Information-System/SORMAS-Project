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

package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.importexport.ExportEntity;
import de.symeda.sormas.api.importexport.ExportGroup;
import de.symeda.sormas.api.importexport.ExportGroupType;
import de.symeda.sormas.api.importexport.ExportProperty;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.utils.Order;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.uuid.AbstractUuidDto;

import java.util.*;

@ExportEntity(EbsDto.class)
public class EbsExportDto extends AbstractUuidDto {

	public static final String I18N_PREFIX = "EbsExport";
	private static final String REGION = "region";
	private static final String DISTRICT = "district";
	private static final String COMMUNITY = "community";

	private String informantName;
	private String informantTel;
	private Date reportDateTime;
	private EbsSourceType sourceInformation;
	private String sourceName;
	private String sourceUrl;
	private String region;
	private String district;
	private String community;
	private Double ebsLongitude;
	private Double ebsLatitude;
	private Double ebsLatLon;
	private String descriptionOccurrence;
	private String personRegistering;
	private String personDesignation;
	private String personPhone;
	private AutomaticScanningType automaticScanningType;
	private ManualScanningType manualScanningType;
	private MediaScannningType scanningType;
	private String other;
	private PersonReporting categoryOfInformant;
	private String otherInformant;
	private YesNo supervisorReview;
	private YesNo referred;
	private YesNo specificSignal;
	private SignalCategory signalCategory;
	private YesNo healthConcern;
	private HumanCommunityCategoryDetails humanCommunityCategoryDetails;
	private HumanFaclityCategoryDetails humanFacilityCategoryDetails;
	private HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails;
	private AnimalCommunityCategoryDetails animalCommunityCategoryDetails;
	private AnimalFacilityCategoryDetails animalFacilityCategoryDetails;
	private AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails;
	private EnvironmentalCategoryDetails environmentalCategoryDetails;
	private POE poeCategoryDetails;
	private YesNo occurrencePreviously;
	private EbsTriagingDecision triagingDecision;
	private Date decisionDate;
	private String referredTo;
	private OutComeSupervisor outcomeSupervisor;
	private boolean notSignal;
	private CategoryDetailsLevel categoryDetailsLevel;
	private YesNo potentialRisk;
	private YesNo verificationSent;
	private SignalOutcome verified;
	private Date verificationCompleteDate;
	private Date dateOfOccurrence;
	private String numberOfPersonAnimal;
	private String numberOfDeath;
	private String description;
	private String whyNotVerify;
	private String numberOfPersonCases;
	private String numberOfDeathPerson;
	private YesNo morbidityMortality;
	private String morbidityMortalityComment;
	private YesNo spreadProbability;
	private String spreadProbabilityComment;
	private YesNo controlMeasures;
	private String controlMeasuresComment;
	private RiskAssesment riskAssessment;
	private Date assessmentDate;
	private String assessmentTime;
	private YesNo actionInitiated;
	private ResponseStatus responseStatus;
	private Date responseDate;
	private String detailsResponseActivities;
	private String detailsGiven;
	private YesNo alertIssued;
	private String detailsAlertUsed;
	private Date alertDate;

	public EbsExportDto(
			String uuid,
			String informantName,
			String informantTel,
			Date reportDateTime,
			EbsSourceType sourceInformation,
			String sourceName,
			String sourceUrl,
			String region,
			String district,
			String community,
			Double ebsLongitude,
			Double ebsLatitude,
			Double ebsLatLon,
			String descriptionOccurrence,
			String personRegistering,
			String personDesignation,
			String personPhone,
			AutomaticScanningType automaticScanningType,
			ManualScanningType manualScanningType,
			MediaScannningType scanningType,
			String other,
			PersonReporting categoryOfInformant,
			String otherInformant,
			YesNo supervisorReview,
			YesNo referred,
			YesNo specificSignal,
			SignalCategory signalCategory,
			YesNo healthConcern,
			HumanCommunityCategoryDetails humanCommunityCategoryDetails,
			HumanFaclityCategoryDetails humanFacilityCategoryDetails,
			HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails,
			AnimalCommunityCategoryDetails animalCommunityCategoryDetails,
			AnimalFacilityCategoryDetails animalFacilityCategoryDetails,
			AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails,
			EnvironmentalCategoryDetails environmentalCategoryDetails,
			POE poeCategoryDetails,
			YesNo occurrencePreviously,
			EbsTriagingDecision triagingDecision,
			Date decisionDate,
			String referredTo,
			OutComeSupervisor outcomeSupervisor,
			boolean notSignal,
			CategoryDetailsLevel categoryDetailsLevel,
			YesNo potentialRisk,
			YesNo verificationSent,
			SignalOutcome verified,
			Date verificationCompleteDate,
			Date dateOfOccurrence,
			String numberOfPersonAnimal,
			String numberOfDeath,
			String description,
			String whyNotVerify,
			String numberOfPersonCases,
			String numberOfDeathPerson,
			YesNo morbidityMortality,
			String morbidityMortalityComment,
			YesNo spreadProbability,
			String spreadProbabilityComment,
			YesNo controlMeasures,
			String controlMeasuresComment,
			RiskAssesment riskAssessment,
			Date assessmentDate,
			YesNo actionInitiated,
			ResponseStatus responseStatus,
			Date responseDate,
			String detailsResponseActivities,
			String detailsGiven,
			YesNo alertIssued,
			String detailsAlertUsed,
			Date alertDate
	) {
		super(uuid);
		this.informantName = informantName;
		this.informantTel = informantTel;
		this.reportDateTime = reportDateTime;
		this.sourceInformation = sourceInformation;
		this.sourceName = sourceName;
		this.sourceUrl = sourceUrl;
		this.region = region;
		this.district = district;
		this.community = community;
		this.ebsLongitude = ebsLongitude;
		this.ebsLatitude = ebsLatitude;
		this.ebsLatLon = ebsLatLon;
		this.descriptionOccurrence = descriptionOccurrence;
		this.personRegistering = personRegistering;
		this.personDesignation = personDesignation;
		this.personPhone = personPhone;
		this.automaticScanningType = automaticScanningType;
		this.manualScanningType = manualScanningType;
		this.scanningType = scanningType;
		this.other = other;
		this.categoryOfInformant = categoryOfInformant;
		this.otherInformant = otherInformant;
		this.supervisorReview = supervisorReview;
		this.referred = referred;
		this.specificSignal = specificSignal;
		this.signalCategory = signalCategory;
		this.healthConcern = healthConcern;
		this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
		this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
		this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
		this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
		this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;
		this.animalLaboratoryCategoryDetails = animalLaboratoryCategoryDetails;
		this.environmentalCategoryDetails = environmentalCategoryDetails;
		this.poeCategoryDetails = poeCategoryDetails;
		this.occurrencePreviously = occurrencePreviously;
		this.triagingDecision = triagingDecision;
		this.decisionDate = decisionDate;
		this.referredTo = referredTo;
		this.outcomeSupervisor = outcomeSupervisor;
		this.notSignal = notSignal;
		this.categoryDetailsLevel = categoryDetailsLevel;
		this.potentialRisk = potentialRisk;
		this.verificationSent = verificationSent;
		this.verified = verified;
		this.verificationCompleteDate = verificationCompleteDate;
		this.dateOfOccurrence = dateOfOccurrence;
		this.numberOfPersonAnimal = numberOfPersonAnimal;
		this.numberOfDeath = numberOfDeath;
		this.description = description;
		this.whyNotVerify = whyNotVerify;
		this.numberOfPersonCases = numberOfPersonCases;
		this.numberOfDeathPerson = numberOfDeathPerson;
		this.morbidityMortality = morbidityMortality;
		this.morbidityMortalityComment = morbidityMortalityComment;
		this.spreadProbability = spreadProbability;
		this.spreadProbabilityComment = spreadProbabilityComment;
		this.controlMeasures = controlMeasures;
		this.controlMeasuresComment = controlMeasuresComment;
		this.riskAssessment = riskAssessment;
		this.assessmentDate	= assessmentDate;
		this.actionInitiated = actionInitiated;
		this.responseStatus = responseStatus;
		this.responseDate = responseDate;
		this.detailsResponseActivities = detailsResponseActivities;
		this.detailsGiven = detailsGiven;
		this.alertIssued = alertIssued;
		this.detailsAlertUsed = detailsAlertUsed;
		this.alertDate = alertDate;
	}

	@Order(0)
	@ExportProperty(EbsDto.UUID)
	@ExportGroup(ExportGroupType.EBS)
	@Override
	public String getUuid() {
		return super.getUuid();
	}
	@Order(1)
	@ExportProperty(EbsDto.SOURCE_INFORMATION)
	@ExportGroup(ExportGroupType.EBS)
	public String getSourceName(){
		return sourceName;
	}
	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}
	@Order(2)
	@ExportProperty(EbsDto.SOURCE_URL)
	@ExportGroup(ExportGroupType.EBS)
	public String getSourceUrl(){
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl){
		this.sourceUrl = sourceUrl;
	}
	@Order(3)
	@ExportProperty(REGION)
	@ExportGroup(ExportGroupType.EBS)
	public String getRegion(){
		return region;
	}
	public void setRegion(String region){
		this.region = region;
	}
	@Order(4)
	@ExportProperty(DISTRICT)
	@ExportGroup(ExportGroupType.EBS)
	public String getDistrict(){
		return district;
	}
	public void setDistrict(String district){
		this.district = district;
	}
	@Order(5)
	@ExportProperty(COMMUNITY)
	@ExportGroup(ExportGroupType.EBS)
	public String getCommunity(){
		return community;
	}
	public void setCommunity(String community){
		this.community = community;
	}
	@Order(6)
	@ExportProperty(EbsDto.EBS_LONGITUDE)
	@ExportGroup(ExportGroupType.EBS)
	public Double getEbsLongitude(){
		return ebsLongitude;
	}
	public void setEbsLongitude(Double ebsLongitude){
		this.ebsLongitude = ebsLongitude;
	}
	@Order(7)
	@ExportProperty(EbsDto.EBS_LATITUDE)
	@ExportGroup(ExportGroupType.EBS)
	public Double getEbsLatitude(){
		return ebsLatitude;
	}
	public void setEbsLatitude(Double ebsLatitude){
		this.ebsLatitude = ebsLatitude;
	}
	@Order(8)
	@ExportProperty(EbsDto.EBS_LATLONG)
	@ExportGroup(ExportGroupType.EBS)
	public Double getEbsLatLon(){
		return ebsLatLon;
	}
	public void setEbsLatLon(Double ebsLatLon){
		this.ebsLatLon = ebsLatLon;
	}
	@Order(9)
	@ExportProperty(EbsDto.DESCRIPTION_OCCURRENCE)
	@ExportGroup(ExportGroupType.EBS)
	public String getDescriptionOccurrence(){
		return descriptionOccurrence;
	}
	public void setDescriptionOccurrence(String descriptionOccurrence){
		this.descriptionOccurrence = descriptionOccurrence;
	}
	@Order(10)
	@ExportProperty(EbsDto.PERSON_REGISTERING)
	@ExportGroup(ExportGroupType.EBS)
	public String getPersonRegistering(){
		return personRegistering;
	}
	public void setPersonRegistering(String personRegistering){
		this.personRegistering = personRegistering;
	}
	@Order(11)
	@ExportProperty(EbsDto.PERSON_DESIGNATION)
	@ExportGroup(ExportGroupType.EBS)
	public String getPersonDesignation(){
		return personDesignation;
	}
	public void setPersonDesignation(String personDesignation){
		this.personDesignation = personDesignation;
	}
	@Order(12)
	@ExportProperty(EbsDto.PERSON_PHONE)
	@ExportGroup(ExportGroupType.EBS)
	public String getPersonPhone(){
		return personPhone;
	}
	public void setPersonPhone(String personPhone){
		this.personPhone = personPhone;
	}
	@Order(13)
	@ExportProperty(EbsDto.AUTOMATIC_SCANNING_TYPE)
	@ExportGroup(ExportGroupType.EBS)
	public AutomaticScanningType getAutomaticScanningType(){
		return automaticScanningType;
	}
	public void setAutomaticScanningType(AutomaticScanningType automaticScanningType){
		this.automaticScanningType = automaticScanningType;
	}
	@Order(14)
	@ExportProperty(EbsDto.MANUAL_SCANNING_TYPE)
	@ExportGroup(ExportGroupType.EBS)
	public ManualScanningType getManualScanningType(){
		return manualScanningType;
	}
	public void setManualScanningType(ManualScanningType manualScanningType){
		this.manualScanningType = manualScanningType;
	}
	@Order(15)
	@ExportProperty(EbsDto.SCANNING_TYPE)
	@ExportGroup(ExportGroupType.EBS)
	public MediaScannningType getScanningType(){
		return scanningType;
	}
	public void setScanningType(MediaScannningType scanningType){
		this.scanningType = scanningType;
	}
	@Order(16)
	@ExportProperty(EbsDto.OTHER)
	@ExportGroup(ExportGroupType.EBS)
	public String getOther(){
		return other;
	}
	public void setOther(String other){
		this.other = other;
	}
	@Order(17)
	@ExportProperty(EbsDto.CATEGORY_OF_INFORMANT)
	@ExportGroup(ExportGroupType.EBS)
	public PersonReporting getCategoryOfInformant(){
		return categoryOfInformant;
	}
	public void setCategoryOfInformant(PersonReporting categoryOfInformant){
		this.categoryOfInformant = categoryOfInformant;
	}
	@Order(18)
	@ExportProperty(EbsDto.OTHER_INFORMANT)
	@ExportGroup(ExportGroupType.EBS)
	public String getOtherInformant(){
		return otherInformant;
	}
	public void setOtherInformant(String otherInformant){
		this.otherInformant = otherInformant;
	}
	@Order(19)
	@ExportProperty(EbsDto.INFORMANT_NAME)
	@ExportGroup(ExportGroupType.EBS)
	public String getInformantName(){
		return informantName;
	}
	public void setInformantName(String informantName){
		this.informantName = informantName;
	}
	@Order(20)
	@ExportProperty(EbsDto.INFORMANT_TEL)
	@ExportGroup(ExportGroupType.EBS)
	public String getInformantTel(){
		return informantTel;
	}
	public void setInformantTel(String informantTel){
		this.informantTel = informantTel;
	}
	@Order(21)
	@ExportProperty(EbsDto.REPORT_DATE_TIME)
	@ExportGroup(ExportGroupType.EBS)
	public Date getReportDateTime(){
		return reportDateTime;
	}
	public void setReportDateTime(Date reportDateTime){
		this.reportDateTime = reportDateTime;
	}

	@Order(23)
	@ExportProperty(EbsDto.SOURCE_INFORMATION)
	@ExportGroup(ExportGroupType.EBS)
	public EbsSourceType getSourceInformation(){
		return sourceInformation;
	}
	public void setSourceInformation(EbsSourceType sourceInformation){
		this.sourceInformation = sourceInformation;
	}

	@Order(24)
	@ExportProperty(TriagingDto.SUPERVISOR_REVIEW)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getSupervisorReview() {
		return supervisorReview;
	}
	public void setSupervisorReview(YesNo supervisorReview) {
		this.supervisorReview = supervisorReview;
	}

	@Order(25)
	@ExportProperty(TriagingDto.REFERRED)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getReferred() {
		return referred;
	}
	public void setReferred(YesNo referred) {
		this.referred = referred;
	}

	@Order(26)
	@ExportProperty(TriagingDto.SPECIFIC_SIGNAL)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getSpecificSignal() {
		return specificSignal;
	}
	public void setSpecificSignal(YesNo specificSignal) {
		this.specificSignal = specificSignal;
	}

	@Order(27)
	@ExportProperty(TriagingDto.SIGNAL_CATEGORY)
	@ExportGroup(ExportGroupType.TRIAGING)
	public SignalCategory getSignalCategory() {
		return signalCategory;
	}
	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	@Order(28)
	@ExportProperty(TriagingDto.HEALTH_CONCERN)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getHealthConcern() {
		return healthConcern;
	}
	public void setHealthConcern(YesNo healthConcern) {
		this.healthConcern = healthConcern;
	}

	@Order(29)
	@ExportProperty(TriagingDto.HUMAN_COMMUNITY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public HumanCommunityCategoryDetails getHumanCommunityCategoryDetails() {
		return humanCommunityCategoryDetails;
	}
	public void setHumanCommunityCategoryDetails(HumanCommunityCategoryDetails humanCommunityCategoryDetails) {
		this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
	}

	@Order(30)
	@ExportProperty(TriagingDto.HUMAN_FACILITY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public HumanFaclityCategoryDetails getHumanFacilityCategoryDetails() {
		return humanFacilityCategoryDetails;
	}
	public void setHumanFacilityCategoryDetails(HumanFaclityCategoryDetails humanFacilityCategoryDetails) {
		this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
	}

	@Order(31)
	@ExportProperty(TriagingDto.HUMAN_LABORATORY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public HumanLaboratoryCategoryDetails getHumanLaboratoryCategoryDetails() {
		return humanLaboratoryCategoryDetails;
	}
	public void setHumanLaboratoryCategoryDetails(HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails) {
		this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
	}

	@Order(32)
	@ExportProperty(TriagingDto.ANIMAL_COMMUNITY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public AnimalCommunityCategoryDetails getAnimalCommunityCategoryDetails() {
		return animalCommunityCategoryDetails;
	}
	public void setAnimalCommunityCategoryDetails(AnimalCommunityCategoryDetails animalCommunityCategoryDetails) {
		this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
	}

	@Order(33)
	@ExportProperty(TriagingDto.ANIMAL_FACILITY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public AnimalFacilityCategoryDetails getAnimalFacilityCategoryDetails() {
		return animalFacilityCategoryDetails;
	}
	public void setAnimalFacilityCategoryDetails(AnimalFacilityCategoryDetails animalFacilityCategoryDetails) {
		this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;
	}

	@Order(34)
	@ExportProperty(TriagingDto.ANIMAL_LABORATORY_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public AnimalLaboratoryCategoryDetails getAnimalLaboratoryCategoryDetails() {
		return animalLaboratoryCategoryDetails;
	}
	public void setAnimalLaboratoryCategoryDetails(AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails) {
		this.animalLaboratoryCategoryDetails = animalLaboratoryCategoryDetails;
	}

	@Order(35)
	@ExportProperty(TriagingDto.ENVIRONMENTAL_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public EnvironmentalCategoryDetails getEnvironmentalCategoryDetails() {
		return environmentalCategoryDetails;
	}
	public void setEnvironmentalCategoryDetails(EnvironmentalCategoryDetails environmentalCategoryDetails) {
		this.environmentalCategoryDetails = environmentalCategoryDetails;
	}

	@Order(36)
	@ExportProperty(TriagingDto.POE_CATEGORY_DETAILS)
	@ExportGroup(ExportGroupType.TRIAGING)
	public POE getPoeCategoryDetails() {
		return poeCategoryDetails;
	}
	public void setPoeCategoryDetails(POE poeCategoryDetails) {
		this.poeCategoryDetails = poeCategoryDetails;
	}

	@Order(37)
	@ExportProperty(TriagingDto.OCCURRENCE_PREVIOUSLY)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}
	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

	@Order(38)
	@ExportProperty(TriagingDto.TRIAGING_DECISION)
	@ExportGroup(ExportGroupType.TRIAGING)
	public EbsTriagingDecision getTriagingDecision() {
		return triagingDecision;
	}
	public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
		this.triagingDecision = triagingDecision;
	}

	@Order(39)
	@ExportProperty(TriagingDto.DATE_OF_DECISION)
	@ExportGroup(ExportGroupType.TRIAGING)
	public Date getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(Date decisionDate) {
		this.decisionDate = decisionDate;
	}

	@Order(40)
	@ExportProperty(TriagingDto.REFERRED_TO)
	@ExportGroup(ExportGroupType.TRIAGING)
	public String getReferredTo() {
		return referredTo;
	}
	public void setReferredTo(String referredTo) {
		this.referredTo = referredTo;
	}

	@Order(41)
	@ExportProperty(TriagingDto.OUTCOME_SUPERVISOR)
	@ExportGroup(ExportGroupType.TRIAGING)
	public OutComeSupervisor getOutcomeSupervisor() {
		return outcomeSupervisor;
	}
	public void setOutcomeSupervisor(OutComeSupervisor outcomeSupervisor) {
		this.outcomeSupervisor = outcomeSupervisor;
	}

	@Order(42)
	@ExportProperty(TriagingDto.NOT_SIGNAL)
	@ExportGroup(ExportGroupType.TRIAGING)
	public boolean isNotSignal() {
		return notSignal;
	}
	public void setNotSignal(boolean notSignal) {
		this.notSignal = notSignal;
	}

	@Order(43)
	@ExportProperty(TriagingDto.CATEGORY_DETAILS_LEVEL)
	@ExportGroup(ExportGroupType.TRIAGING)
	public CategoryDetailsLevel getCategoryDetailsLevel() {
		return categoryDetailsLevel;
	}
	public void setCategoryDetailsLevel(CategoryDetailsLevel categoryDetailsLevel) {
		this.categoryDetailsLevel = categoryDetailsLevel;
	}

	@Order(44)
	@ExportProperty(TriagingDto.POTENTIAL_RISK)
	@ExportGroup(ExportGroupType.TRIAGING)
	public YesNo getPotentialRisk() {
		return potentialRisk;
	}
	public void setPotentialRisk(YesNo potentialRisk) {
		this.potentialRisk = potentialRisk;
	}

	@Order(45)
	@ExportProperty(SignalVerificationDto.VERIFICATION_SENT)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public YesNo getVerificationSent() {
		return verificationSent;
	}
	public void setVerificationSent(YesNo verificationSent) {
		this.verificationSent = verificationSent;
	}

	@Order(46)
	@ExportProperty(SignalVerificationDto.VERIFIED)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public SignalOutcome getVerified() {
		return verified;
	}
	public void setVerified(SignalOutcome verified) {
		this.verified = verified;
	}

	@Order(47)
	@ExportProperty(SignalVerificationDto.VERIFICATION_COMPLETE_DATE)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public Date getVerificationCompleteDate() {
		return verificationCompleteDate;
	}
	public void setVerificationCompleteDate(Date verificationCompleteDate) {
		this.verificationCompleteDate = verificationCompleteDate;
	}

	@Order(48)
	@ExportProperty(SignalVerificationDto.DATE_OF_OCCURRENCE)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public Date getDateOfOccurrence() {
		return dateOfOccurrence;
	}
	public void setDateOfOccurrence(Date dateOfOccurrence) {
		this.dateOfOccurrence = dateOfOccurrence;
	}

	@Order(49)
	@ExportProperty(SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getNumberOfPersonAnimal() {
		return numberOfPersonAnimal;
	}
	public void setNumberOfPersonAnimal(String numberOfPersonAnimal) {
		this.numberOfPersonAnimal = numberOfPersonAnimal;
	}

	@Order(50)
	@ExportProperty(SignalVerificationDto.NUMBER_OF_DEATH)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getNumberOfDeath() {
		return numberOfDeath;
	}
	public void setNumberOfDeath(String numberOfDeath) {
		this.numberOfDeath = numberOfDeath;
	}

	@Order(51)
	@ExportProperty(SignalVerificationDto.DESCRIPTION)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Order(52)
	@ExportProperty(SignalVerificationDto.WHY_NOT_VERIFY)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getWhyNotVerify() {
		return whyNotVerify;
	}
	public void setWhyNotVerify(String whyNotVerify) {
		this.whyNotVerify = whyNotVerify;
	}

	@Order(53)
	@ExportProperty(SignalVerificationDto.NUMBER_OF_PERSON_CASES)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getNumberOfPersonCases() {
		return numberOfPersonCases;
	}
	public void setNumberOfPersonCases(String numberOfPersonCases) {
		this.numberOfPersonCases = numberOfPersonCases;
	}

	@Order(54)
	@ExportProperty(SignalVerificationDto.NUMBER_OF_DEATH_PERSON)
	@ExportGroup(ExportGroupType.SIGNAL_VERIFICATION)
	public String getNumberOfDeathPerson() {
		return numberOfDeathPerson;
	}
	public void setNumberOfDeathPerson(String numberOfDeathPerson) {
		this.numberOfDeathPerson = numberOfDeathPerson;
	}
	@Order(55)
	@ExportProperty(RiskAssessmentDto.MORBIDITY_MORTALITY)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public YesNo getMorbidityMortality() {
		return morbidityMortality;
	}
	public void setMorbidityMortality(YesNo morbidityMortality) {
		this.morbidityMortality = morbidityMortality;
	}

	@Order(56)
	@ExportProperty(RiskAssessmentDto.MORBIDITY_MORTALITY_COMMENT)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public String getMorbidityMortalityComment() {
		return morbidityMortalityComment;
	}
	public void setMorbidityMortalityComment(String morbidityMortalityComment) {
		this.morbidityMortalityComment = morbidityMortalityComment;
	}

	@Order(57)
	@ExportProperty(RiskAssessmentDto.SPREAD_PROBABILITY)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public YesNo getSpreadProbability() {
		return spreadProbability;
	}
	public void setSpreadProbability(YesNo spreadProbability) {
		this.spreadProbability = spreadProbability;
	}

	@Order(58)
	@ExportProperty(RiskAssessmentDto.SPREAD_PROBABILITY_COMMENT)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public String getSpreadProbabilityComment() {
		return spreadProbabilityComment;
	}
	public void setSpreadProbabilityComment(String spreadProbabilityComment) {
		this.spreadProbabilityComment = spreadProbabilityComment;
	}

	@Order(59)
	@ExportProperty(RiskAssessmentDto.CONTROL_MEASURES)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public YesNo getControlMeasures() {
		return controlMeasures;
	}
	public void setControlMeasures(YesNo controlMeasures) {
		this.controlMeasures = controlMeasures;
	}

	@Order(60)
	@ExportProperty(RiskAssessmentDto.CONTROL_MEASURES_COMMENT)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public String getControlMeasuresComment() {
		return controlMeasuresComment;
	}
	public void setControlMeasuresComment(String controlMeasuresComment) {
		this.controlMeasuresComment = controlMeasuresComment;
	}

	@Order(61)
	@ExportProperty(RiskAssessmentDto.ASSESSMENT_LEVEL)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public RiskAssesment getRiskAssessment() {
		return riskAssessment;
	}
	public void setRiskAssessment(RiskAssesment riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

	@Order(62)
	@ExportProperty(RiskAssessmentDto.ASSESSMENT_DATE)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public Date getAssessmentDate() {
		return assessmentDate;
	}
	public void setAssessmentDate(Date assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	@Order(63)
	@ExportProperty(RiskAssessmentDto.ASSESSMENT_TIME)
	@ExportGroup(ExportGroupType.RISK_ASSESSMENT)
	public String getAssessmentTime() {
		return assessmentTime;
	}
	public void setAssessmentTime(String assessmentTime) {
		this.assessmentTime = assessmentTime;
	}

	@Order(64)
	@ExportProperty(EbsAlertDto.ACTION_INITIATED)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public YesNo getActionInitiated() {
		return actionInitiated;
	}
	public void setActionInitiated(YesNo actionInitiated) {
		this.actionInitiated = actionInitiated;
	}

	@Order(65)
	@ExportProperty(EbsAlertDto.RESPONSE_STATUS)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Order(66)
	@ExportProperty(EbsAlertDto.RESPONSE_DATE)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public Date getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	@Order(67)
	@ExportProperty(EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public String getDetailsResponseActivities() {
		return detailsResponseActivities;
	}
	public void setDetailsResponseActivities(String detailsResponseActivities) {
		this.detailsResponseActivities = detailsResponseActivities;
	}

	@Order(68)
	@ExportProperty(EbsAlertDto.DETAILS_GIVEN)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public String getDetailsGiven() {
		return detailsGiven;
	}
	public void setDetailsGiven(String detailsGiven) {
		this.detailsGiven = detailsGiven;
	}

	@Order(69)
	@ExportProperty(EbsAlertDto.ALERT_ISSUED)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public YesNo getAlertIssued() {
		return alertIssued;
	}
	public void setAlertIssued(YesNo alertIssued) {
		this.alertIssued = alertIssued;
	}

	@Order(70)
	@ExportProperty(EbsAlertDto.DETAILS_ALERT_USED)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public String getDetailsAlertUsed() {
		return detailsAlertUsed;
	}
	public void setDetailsAlertUsed(String detailsAlertUsed) {
		this.detailsAlertUsed = detailsAlertUsed;
	}

	@Order(71)
	@ExportProperty(EbsAlertDto.ALERT_DATE)
	@ExportGroup(ExportGroupType.EBS_ALERT)
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
}
