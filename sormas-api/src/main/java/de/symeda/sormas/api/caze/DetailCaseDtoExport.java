package de.symeda.sormas.api.caze;

import java.io.Serializable;
import java.sql.Date;
// @NamedStoredProcedureQuery(name="demo_fn", procedureName="demo_fn")

import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.importexport.ExportGroup;
import de.symeda.sormas.api.importexport.ExportGroupType;
import de.symeda.sormas.api.importexport.ExportProperty;
import de.symeda.sormas.api.importexport.ExportTarget;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.Order;

public class DetailCaseDtoExport implements Serializable {

	private static final long serialVersionUID = 8581579464816945555L;

	public static final String I18N_PREFIX = "DetailCaseExportTwo";

	public static final String SYMPTOMS = "symptoms";

	private long caseId;
	private long districtId;
	private long healthcondId;
	private String caseUuid;
	private String caseEpidnumber;
	private String caseDisease;
	private String caseDiseasedetails;
	private String personFirstname;
	private String personLastname;
	private String personSex;
	private String casePregnant;

	private Integer personApproximateage;
	private String personApproximateagetype;
	private Integer personBirthdateDd;
	private Integer personBirthdateMm;
	private Integer caseBirthdateYyyy;
	private Date caseReportdate;
	private String userUuid;
	private String regionUuid;
	private String regionName;
	private String districtUuid;
	private String districtName;
	private String communityUuid;
	private String communityName;
	private String facilityName;
	private String facilityUuid;
	private String caseHealthFacilityDetails;
	private String pointofentName;
	private String pointofentUuid;
	private String casePointofentrydetails;
	private String caseCaseclassification;
	private String caseInvestigationstatus;
	private String caseOutcome;
	private String caseQuarantine;

	private Date caseQuarantinefrom;
	private Date caseQuarantineto;
	private String hospitalizAdmittedtohealthfacility;
	private Date hospitalizAdmissiondate;
	private Date hospitalizDischargedate;
	private String hospitalizLeftagainstadvice;
	private Integer personPresentcondition;
	private Date personDeathdate;
	private Date personBurialdate;
	private String personBurialconductor;
	private String personBurialplacedescription;
	private String locationCity;
	private String locationAddress;
	private String locationPostalcode;
	private String personPhone;
	private String personPhoneowner;
	private String personEducationtype;
	private String personEducationdetails;
	private String personOccupationtype;
	private String personOccupationdetails;
	private String personOccupationfacilitydetails;
	private String epidataTraveled;
	private String epidataBurialattended;
	private String epidataDirectcontactconfirmedcase;
	private String epidataDirectcontactprobablecase;
	private String epidataRodents;
	private String caseVaccination;
	private String caseVaccinationdoses;
	private Date caseVaccinationdate;
	private String caseVaccinationinfosource;
	private String casePostpartum;
	private String caseTrimester;
	private Long personId;
	private Long locationId;
	private Long epidataId;
	private Long symptomsId;
	private Long hospitalizId;
	private String symptoms;

	public DetailCaseDtoExport() {
		// TODO Auto-generated constructor stub
	}

	@Order(0)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseExportDto.ID)
	@ExportGroup(ExportGroupType.CORE)
	public long getCaseId() {
		return caseId;
	}

	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}

//	@Order(1)
//	@ExportProperty(DISTRICT_ID)
	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

//	@Order(2)
//	@ExportProperty(HEALTH_COND_ID)
	public long getHealthcondId() {
		return healthcondId;
	}

	public void setHealthcondId(long healthcondId) {
		this.healthcondId = healthcondId;
	}

	@Order(3)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.UUID)
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseUuid() {
		return caseUuid;
	}

	public void setCaseUuid(String caseUuid) {
		this.caseUuid = caseUuid;
	}

	@Order(4)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.EPID_NUMBER)
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseEpidnumber() {
		return caseEpidnumber;
	}

	public void setCaseEpidnumber(String caseEpidnumber) {
		this.caseEpidnumber = caseEpidnumber;
	}

	@Order(5)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	@ExportProperty(CaseDataDto.DISEASE)
	public String getCaseDisease() {
		return caseDisease;
	}

	public void setCaseDisease(String caseDisease) {
		this.caseDisease = caseDisease;
	}

	@Order(6)
	@ExportProperty(CaseDataDto.DISEASE_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseDiseasedetails() {
		return caseDiseasedetails;
	}

	public void setCaseDiseasedetails(String caseDiseasedetails) {
		this.caseDiseasedetails = caseDiseasedetails;
	}

	@Order(7)
	@ExportProperty(PersonDto.FIRST_NAME)
	public String getPersonFirstname() {
		return personFirstname;
	}

	public void setPersonFirstname(String personFirstname) {
		this.personFirstname = personFirstname;
	}

	@Order(8)
	@ExportProperty(PersonDto.LAST_NAME)
	public String getPersonLastname() {
		return personLastname;
	}

	public void setPersonLastname(String personLastname) {
		this.personLastname = personLastname;
	}

	@Order(9)
	@ExportProperty(PersonDto.SEX)
	public String getPersonSex() {
		return personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	@Order(10)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.PREGNANT)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCasePregnant() {
		return casePregnant;
	}

	public void setCasePregnant(String casePregnant) {
		this.casePregnant = casePregnant;
	}

	@Order(11)
	@ExportProperty(PersonDto.APPROXIMATE_AGE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Integer getPersonApproximateage() {
		return personApproximateage;
	}

	public void setPersonApproximateage(Integer personApproximateage) {
		this.personApproximateage = personApproximateage;
	}

	@Order(12)
	@ExportProperty(CaseExportDto.AGE_GROUP)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonApproximateagetype() {
		return personApproximateagetype;
	}

	public void setPersonApproximateagetype(String personApproximateagetype) {
		this.personApproximateagetype = personApproximateagetype;
	}

//	@Order(13)
//	@ExportProperty(BIRTH_DATE_DD)
	public Integer getPersonBirthdateDd() {
		return personBirthdateDd;
	}

	public void setPersonBirthdateDd(Integer personBirthdateDd) {
		this.personBirthdateDd = personBirthdateDd;
	}

//	@Order(14)
//	@ExportProperty(BIRTH_DATE_MM)
	public Integer getPersonBirthdateMm() {
		return personBirthdateMm;
	}

	public void setPersonBirthdateMm(Integer personBirthdateMm) {
		this.personBirthdateMm = personBirthdateMm;
	}

//	@Order(15)
//	@ExportProperty(BIRTH_DATE_YYYY)
	public Integer getCaseBirthdateYyyy() {
		return caseBirthdateYyyy;
	}

	public void setCaseBirthdateYyyy(Integer caseBirthdateYyyy) {
		this.caseBirthdateYyyy = caseBirthdateYyyy;
	}

	@Order(16)
	@ExportProperty(CaseDataDto.REPORT_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getCaseReportdate() {
		return caseReportdate;
	}

	public void setCaseReportdate(Date caseReportdate) {
		this.caseReportdate = caseReportdate;
	}

//	@Order(17)
//	@ExportProperty(USER_UUID)
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

//	@Order(18)
//	@ExportProperty(REGION_UUID)
	public String getRegionUuid() {
		return regionUuid;
	}

	public void setRegionUuid(String regionUuid) {
		this.regionUuid = regionUuid;
	}

	@Order(19)
	@ExportProperty(CaseDataDto.REGION)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

//	@Order(20)
//	@ExportProperty(DISTRICT_UUID)
	public String getDistrictUuid() {
		return districtUuid;
	}

	public void setDistrictUuid(String districtUuid) {
		this.districtUuid = districtUuid;
	}

	@Order(21)
	@ExportProperty(CaseDataDto.DISTRICT)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

//	@Order(22)
//	@ExportProperty(COMMUNITY_UUID)
	public String getCommunityUuid() {
		return communityUuid;
	}

	public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
	}

	@Order(23)
	@ExportProperty(CaseDataDto.COMMUNITY)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	@Order(24)
//	@ExportProperty(FACILITY_NAME)
	@ExportProperty(CaseDataDto.HEALTH_FACILITY)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

//	@Order(25)
//	@ExportProperty(FACILITY_UUID)
	public String getFacilityUuid() {
		return facilityUuid;
	}

	public void setFacilityUuid(String facilityUuid) {
		this.facilityUuid = facilityUuid;
	}

	@Order(26)
	@ExportProperty(CaseDataDto.HEALTH_FACILITY_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseHealthFacilityDetails() {
		return caseHealthFacilityDetails;
	}

	public void setCaseHealthFacilityDetails(String caseHealthFacilityDetails) {
		this.caseHealthFacilityDetails = caseHealthFacilityDetails;
	}

	@Order(27)
	@ExportProperty(CaseDataDto.POINT_OF_ENTRY)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPointofentName() {
		return pointofentName;
	}

	public void setPointofentName(String pointofentName) {
		this.pointofentName = pointofentName;
	}

//	@Order(28)
//	@ExportProperty(POE_UUID)
	public String getPointofentUuid() {
		return pointofentUuid;
	}

	public void setPointofentUuid(String pointofentUuid) {
		this.pointofentUuid = pointofentUuid;
	}

	@Order(29)
	@ExportProperty(CaseDataDto.POINT_OF_ENTRY_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCasePointofentrydetails() {
		return casePointofentrydetails;
	}

	public void setCasePointofentrydetails(String casePointofentrydetails) {
		this.casePointofentrydetails = casePointofentrydetails;
	}

	@Order(30)
	@ExportProperty(CaseDataDto.CASE_CLASSIFICATION)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseCaseclassification() {
		return caseCaseclassification;
	}

	public void setCaseCaseclassification(String caseCaseclassification) {
		this.caseCaseclassification = caseCaseclassification;
	}

	@Order(31)
	@ExportProperty(CaseDataDto.INVESTIGATION_STATUS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseInvestigationstatus() {
		return caseInvestigationstatus;
	}

	public void setCaseInvestigationstatus(String caseInvestigationstatus) {
		this.caseInvestigationstatus = caseInvestigationstatus;
	}

	@Order(32)
	@ExportProperty(CaseDataDto.OUTCOME)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseOutcome() {
		return caseOutcome;
	}

	public void setCaseOutcome(String caseOutcome) {
		this.caseOutcome = caseOutcome;
	}

	@Order(33)
	@ExportProperty(CaseDataDto.QUARANTINE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseQuarantine() {
		return caseQuarantine;
	}

	public void setCaseQuarantine(String caseQuarantine) {
		this.caseQuarantine = caseQuarantine;
	}

	@Order(34)
	@ExportProperty(CaseDataDto.QUARANTINE_FROM)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getCaseQuarantinefrom() {
		return caseQuarantinefrom;
	}

	public void setCaseQuarantinefrom(Date caseQuarantinefrom) {
		this.caseQuarantinefrom = caseQuarantinefrom;
	}

	@Order(35)
	@ExportProperty(CaseDataDto.QUARANTINE_TO)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getCaseQuarantineto() {
		return caseQuarantineto;
	}

	public void setCaseQuarantineto(Date caseQuarantineto) {
		this.caseQuarantineto = caseQuarantineto;
	}

	@Order(36)
	@ExportProperty(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getHospitalizAdmittedtohealthfacility() {
		return hospitalizAdmittedtohealthfacility;
	}

	public void setHospitalizAdmittedtohealthfacility(String hospitalizAdmittedtohealthfacility) {
		this.hospitalizAdmittedtohealthfacility = hospitalizAdmittedtohealthfacility;
	}

	@Order(37)
	@ExportProperty(HospitalizationDto.ADMISSION_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getHospitalizAdmissiondate() {
		return hospitalizAdmissiondate;
	}

	public void setHospitalizAdmissiondate(Date hospitalizAdmissiondate) {
		this.hospitalizAdmissiondate = hospitalizAdmissiondate;
	}

	@Order(38)
	@ExportProperty(HospitalizationDto.DISCHARGE_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getHospitalizDischargedate() {
		return hospitalizDischargedate;
	}

	public void setHospitalizDischargedate(Date hospitalizDischargedate) {
		this.hospitalizDischargedate = hospitalizDischargedate;
	}

	@Order(39)
	@ExportProperty(HospitalizationDto.LEFT_AGAINST_ADVICE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getHospitalizLeftagainstadvice() {
		return hospitalizLeftagainstadvice;
	}

	public void setHospitalizLeftagainstadvice(String hospitalizLeftagainstadvice) {
		this.hospitalizLeftagainstadvice = hospitalizLeftagainstadvice;
	}

	@Order(40)
	@ExportProperty(PersonDto.PRESENT_CONDITION)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Integer getPersonPresentcondition() {
		return personPresentcondition;
	}

	public void setPersonPresentcondition(Integer personPresentcondition) {
		this.personPresentcondition = personPresentcondition;
	}

	@Order(41)
	@ExportProperty(PersonDto.DEATH_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getPersonDeathdate() {
		return personDeathdate;
	}

	public void setPersonDeathdate(Date personDeathdate) {
		this.personDeathdate = personDeathdate;
	}

	@Order(42)
	@ExportProperty(PersonDto.DEATH_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getPersonBurialdate() {
		return personBurialdate;
	}

	public void setPersonBurialdate(Date personBurialdate) {
		this.personBurialdate = personBurialdate;
	}

	@Order(43)
	@ExportProperty(PersonDto.BURIAL_CONDUCTOR)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonBurialconductor() {
		return personBurialconductor;
	}

	public void setPersonBurialconductor(String personBurialconductor) {
		this.personBurialconductor = personBurialconductor;
	}

	@Order(44)
	@ExportProperty(PersonDto.BURIAL_PLACE_DESCRIPTION)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonBurialplacedescription() {
		return personBurialplacedescription;
	}

	public void setPersonBurialplacedescription(String personBurialplacedescription) {
		this.personBurialplacedescription = personBurialplacedescription;
	}

	@Order(45)
	@ExportProperty(LocationDto.CITY)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	@Order(46)
	@ExportProperty(LocationDto.ADDRESS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	@Order(47)
	@ExportProperty(LocationDto.POSTAL_CODE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getLocationPostalcode() {
		return locationPostalcode;
	}

	public void setLocationPostalcode(String locationPostalcode) {
		this.locationPostalcode = locationPostalcode;
	}

	@Order(48)
	@ExportProperty(PersonDto.PHONE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	@Order(49)
	@ExportProperty(PersonDto.PHONE_OWNER)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonPhoneowner() {
		return personPhoneowner;
	}

	public void setPersonPhoneowner(String personPhoneowner) {
		this.personPhoneowner = personPhoneowner;
	}

	@Order(50)
	@ExportProperty(PersonDto.EDUCATION_TYPE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonEducationtype() {
		return personEducationtype;
	}

	public void setPersonEducationtype(String personEducationtype) {
		this.personEducationtype = personEducationtype;
	}

	@Order(51)
	@ExportProperty(PersonDto.EDUCATION_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonEducationdetails() {
		return personEducationdetails;
	}

	public void setPersonEducationdetails(String personEducationdetails) {
		this.personEducationdetails = personEducationdetails;
	}

	@Order(52)
	@ExportProperty(PersonDto.OCCUPATION_TYPE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonOccupationtype() {
		return personOccupationtype;
	}

	public void setPersonOccupationtype(String personOccupationtype) {
		this.personOccupationtype = personOccupationtype;
	}

	@Order(53)
	@ExportProperty(PersonDto.OCCUPATION_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonOccupationdetails() {
		return personOccupationdetails;
	}

	public void setPersonOccupationdetails(String personOccupationdetails) {
		this.personOccupationdetails = personOccupationdetails;
	}

	@Order(54)
	@ExportProperty(PersonDto.OCCUPATION_FACILITY_DETAILS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getPersonOccupationfacilitydetails() {
		return personOccupationfacilitydetails;
	}

	public void setPersonOccupationfacilitydetails(String personOccupationfacilitydetails) {
		this.personOccupationfacilitydetails = personOccupationfacilitydetails;
	}

	@Order(55)
	@ExportProperty(EpiDataDto.TRAVELED)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidataTraveled() {
		return epidataTraveled;
	}

	public void setEpidataTraveled(String epidataTraveled) {
		this.epidataTraveled = epidataTraveled;
	}

	@Order(56)
	@ExportProperty(EpiDataDto.BURIAL_ATTENDED)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidataBurialattended() {
		return epidataBurialattended;
	}

	public void setEpidataBurialattended(String epidataBurialattended) {
		this.epidataBurialattended = epidataBurialattended;
	}

	@Order(57)
	@ExportProperty(EpiDataDto.DIRECT_CONTACT_CONFIRMED_CASE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidataDirectcontactconfirmedcase() {
		return epidataDirectcontactconfirmedcase;
	}

	public void setEpidataDirectcontactconfirmedcase(String epidataDirectcontactconfirmedcase) {
		this.epidataDirectcontactconfirmedcase = epidataDirectcontactconfirmedcase;
	}

	@Order(58)
	@ExportProperty(EpiDataDto.DIRECT_CONTACT_PROBABLE_CASE)
//	@ExportProperty(value = EPI_DATA_DIRECT_CONTACT_PROBABLE_CASE, combined = true)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidataDirectcontactprobablecase() {
		return epidataDirectcontactprobablecase;
	}

	public void setEpidataDirectcontactprobablecase(String epidataDirectcontactprobablecase) {
		this.epidataDirectcontactprobablecase = epidataDirectcontactprobablecase;
	}

	@Order(59)
	@ExportProperty(EpiDataDto.RODENTS)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidataRodents() {
		return epidataRodents;
	}

	public void setEpidataRodents(String epidataRodents) {
		this.epidataRodents = epidataRodents;
	}

	@Order(60)
	@ExportProperty(CaseDataDto.VACCINATION)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseVaccination() {
		return caseVaccination;
	}

	public void setCaseVaccination(String caseVaccination) {
		this.caseVaccination = caseVaccination;
	}

	@Order(61)
	@ExportProperty(CaseDataDto.VACCINATION_DOSES)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseVaccinationdoses() {
		return caseVaccinationdoses;
	}

	public void setCaseVaccinationdoses(String caseVaccinationdoses) {
		this.caseVaccinationdoses = caseVaccinationdoses;
	}

	@Order(62)
	@ExportProperty(CaseDataDto.VACCINATION_DATE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public Date getCaseVaccinationdate() {
		return caseVaccinationdate;
	}

	public void setCaseVaccinationdate(Date caseVaccinationdate) {
		this.caseVaccinationdate = caseVaccinationdate;
	}

	@Order(63)
	@ExportProperty(CaseDataDto.VACCINATION_INFO_SOURCE)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseVaccinationinfosource() {
		return caseVaccinationinfosource;
	}

	public void setCaseVaccinationinfosource(String caseVaccinationinfosource) {
		this.caseVaccinationinfosource = caseVaccinationinfosource;
	}

	@Order(64)
	@ExportProperty(CaseDataDto.POSTPARTUM)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCasePostpartum() {
		return casePostpartum;
	}

	public void setCasePostpartum(String casePostpartum) {
		this.casePostpartum = casePostpartum;
	}

	@Order(65)
	@ExportProperty(CaseDataDto.TRIMESTER)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseTrimester() {
		return caseTrimester;
	}

	public void setCaseTrimester(String caseTrimester) {
		this.caseTrimester = caseTrimester;
	}

//	@Order(66)
//	@ExportProperty(PERSON_ID)
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

//	@Order(67)
//	@ExportProperty(LOCATION_ID)
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

//	@Order(68)
//	@ExportProperty(EPI_DATA_ID)
	public Long getEpidataId() {
		return epidataId;
	}

	public void setEpidataId(Long epidataId) {
		this.epidataId = epidataId;
	}

//	@Order(69)
//	@ExportProperty(SYMPTOMS_ID)
	public Long getSymptomsId() {
		return symptomsId;
	}

	public void setSymptomsId(Long symptomsId) {
		this.symptomsId = symptomsId;
	}

//	@Order(70)
//	@ExportProperty(HOSPITALIZED_ID)
	public Long getHospitalizId() {
		return hospitalizId;
	}

	public void setHospitalizId(Long hospitalizId) {
		this.hospitalizId = hospitalizId;
	}

	@Order(71)
	@ExportProperty(SYMPTOMS)
	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

}
