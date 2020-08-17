package de.symeda.sormas.api.caze;

import java.io.Serializable;
import java.sql.Date;

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

	private String country;
	private long caseSn;
	private String caseId;
	private String epidNumber;
	private String disease;
	private String firstname;
	private String lastname;
	private String sex;
	private String pregnancy;
	private String trimester;

	private String postpartum;
	private long age;
	private String ageGroup;
	private String dateOfBirth;

	private Integer personBirthdateDd;
	private Integer personBirthdateMm;
	private Integer caseBirthdateYyyy;
	private Date dateOfReport;
	private String region;
	private String district;
	private String subDistrict;
	private String healthFacility;
	private String pointOfEntry;
	private String placeOfInitialDetection;
	private String caseCaseclassification;

	private String caseInvestigationstatus;
	private String caseOutcome;
	private String caseQuarantine;
	private Date caseQuarantineStart;
	private Date caseQuarantineEnd;

	private String caseClassificationSource;

	private long districtId;
	private long healthcondId;
	private String caseDiseasedetails;
	private String personApproximateagetype;
	private String userUuid;

	private String regionUuid;
	private String districtUuid;
	private String communityUuid;
	private String facilityUuid;
	private String caseHealthFacilityDetails;
	private String pointofentUuid;
	private String pointofentrydetails;

	private String hospitalizAdmittedtohealthfacility;
	private Date hospitalizAdmissiondate;
	private Date hospitalizDischargedate;
	private String hospitalizLeftagainstadvice;
	private Integer personPresentcondition;
	private Date personDeathdate;
	private Date personBurialdate;
	private String personBurialconductor;
	private String personBurialplacedescription;

	private String regionName;
	private String districtName;
	private String locationCity;
	private String locationAddress;
	private String locationPostalcode;

	private String personPhone;
	private String personPhoneowner;
	private String personEducationtype;
	private String personEducationdetails;
	private String personOccupationtype;
	private String personOccupationdetails;

	private String faclityName;
	private String faclityUuid;
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
	@ExportProperty(CaseExportDto.COUNTRY)
	@ExportGroup(ExportGroupType.ADDITIONAL)
	public String getCountry() {
		return country;
	}

	@Order(1)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseExportDto.ID)
	@ExportGroup(ExportGroupType.CORE)
	public long getCaseSn() {
		return caseSn;
	}

	@Order(2)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.UUID)
	@ExportGroup(ExportGroupType.CORE)
	public String getCaseId() {
		return caseId;
	}

	@Order(3)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.EPID_NUMBER)
	@ExportGroup(ExportGroupType.CORE)
	public String getEpidNumber() {
		return epidNumber;
	}

	@Order(4)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	@ExportProperty(CaseDataDto.DISEASE)
	public String getDisease() {
		return disease;
	}

	@Order(5)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.FIRST_NAME)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getFirstname() {
		return firstname;
	}

	@Order(7)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.LAST_NAME)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getLastname() {
		return lastname;
	}

	@Order(8)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.SEX)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getSex() {
		return sex;
	}

	@Order(9)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.PREGNANT)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPregnancy() {
		return pregnancy;
	}

	@Order(10)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.TRIMESTER)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getTrimester() {
		return trimester;
	}

	@Order(11)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.POSTPARTUM)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPostpartum() {
		return postpartum;
	}

	@Order(12)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.APPROXIMATE_AGE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public long getAge() {
		return age;
	}

	@Order(13)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseExportDto.AGE_GROUP)
//	@ExportProperty(PersonDto.APPROXIMATE_AGE_TYPE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getAgeGroup() {
		return ageGroup;
	}

	@Order(14)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.BIRTH_DATE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getDateOfBirth() {
		return dateOfBirth;
	}

//	@Order(15)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(PersonDto.BIRTH_DATE_DD)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public Integer getPersonBirthdateDd() {
		return personBirthdateDd;
	}

//	@Order(16)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(PersonDto.BIRTH_DATE_MM)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public Integer getPersonBirthdateMm() {
		return personBirthdateMm;
	}

//	@Order(17)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(PersonDto.BIRTH_DATE_YYYY)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public Integer getCaseBirthdateYyyy() {
		return caseBirthdateYyyy;
	}

	@Order(18)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.REPORT_DATE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public Date getDateOfReport() {
		return dateOfReport;
	}

	@Order(19)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.REGION)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getRegion() {
		return region;
	}

	@Order(20)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.DISTRICT)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getDistrict() {
		return district;
	}

	@Order(21)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.COMMUNITY)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getSubDistrict() {
		return subDistrict;
	}

	@Order(22)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.HEALTH_FACILITY_DETAILS)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getHealthFacility() {
		return healthFacility;
	}

	@Order(23)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.POINT_OF_ENTRY)
	@ExportGroup(ExportGroupType.CORE)
	public String getPointOfEntry() {
		return pointOfEntry;
	}

	@Order(24)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseExportDto.INITIAL_DETECTION_PLACE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPlaceOfInitialDetection() {
		return placeOfInitialDetection;
	}

	@Order(25)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.CASE_CLASSIFICATION)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseCaseclassification() {
		return caseCaseclassification;
	}

	@Order(26)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.INVESTIGATION_STATUS)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseInvestigationstatus() {
		return caseInvestigationstatus;
	}

	@Order(27)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.OUTCOME)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseOutcome() {
		return caseOutcome;
	}

	@Order(28)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.QUARANTINE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseQuarantine() {
		return caseQuarantine;
	}

	@Order(29)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.QUARANTINE_FROM)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public Date getCaseQuarantineStart() {
		return caseQuarantineStart;
	}

	@Order(30)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.QUARANTINE_TO)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public Date getCaseQuarantineEnd() {
		return caseQuarantineEnd;
	}

	@Order(31)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseExportDto.MAX_SOURCE_CASE_CLASSIFICATION)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseClassificationSource() {
		return caseClassificationSource;
	}

//	@Order(32)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(CaseDataDto.DISTRICT)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public long getDistrictId() {
		return districtId;
	}

//	@Order(33)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(CaseDataDto.HEALTH_FACILITY)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public long getHealthcondId() {
		return healthcondId;
	}

	@Order(34)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.DISEASE_DETAILS)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseDiseasedetails() {
		return caseDiseasedetails;
	}

	@Order(35)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.APPROXIMATE_AGE_REFERENCE_DATE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPersonApproximateagetype() {
		return personApproximateagetype;
	}

//	@Order(36)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty()
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getUserUuid() {
		return userUuid;
	}

//	@Order(37)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(CaseDataDto.REGION)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getRegionUuid() {
		return regionUuid;
	}

//	@Order(38)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(CaseDataDto.DISTRICT)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getDistrictUuid() {
		return districtUuid;
	}

//	@Order(39)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(CaseDataDto.COMMUNITY)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCommunityUuid() {
		return communityUuid;
	}

//	@Order(40)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(CaseDataDto)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getFacilityUuid() {
		return facilityUuid;
	}

	@Order(41)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.HEALTH_FACILITY_DETAILS)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getCaseHealthFacilityDetails() {
		return caseHealthFacilityDetails;
	}

//	@Order(42)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty()
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPointofentUuid() {
		return pointofentUuid;
	}

	@Order(43)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.POINT_OF_ENTRY_DETAILS)
	@ExportGroup(ExportGroupType.CORE)
	public String getPointofentrydetails() {
		return pointofentrydetails;
	}

	@Order(44)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY)
	@ExportGroup(ExportGroupType.HOSPITALIZATION)
	public String getHospitalizAdmittedtohealthfacility() {
		return hospitalizAdmittedtohealthfacility;
	}

	@Order(45)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(HospitalizationDto.ADMISSION_DATE)
	@ExportGroup(ExportGroupType.HOSPITALIZATION)
	public Date getHospitalizAdmissiondate() {
		return hospitalizAdmissiondate;
	}

	@Order(46)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(HospitalizationDto.DISCHARGE_DATE)
	@ExportGroup(ExportGroupType.HOSPITALIZATION)
	public Date getHospitalizDischargedate() {
		return hospitalizDischargedate;
	}

	@Order(48)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(HospitalizationDto.LEFT_AGAINST_ADVICE)
	@ExportGroup(ExportGroupType.HOSPITALIZATION)
	public String getHospitalizLeftagainstadvice() {
		return hospitalizLeftagainstadvice;
	}

	@Order(49)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.PRESENT_CONDITION)
	@ExportGroup(ExportGroupType.PERSON)
	public Integer getPersonPresentcondition() {
		return personPresentcondition;
	}

	@Order(50)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.DEATH_DATE)
	@ExportGroup(ExportGroupType.PERSON)
	public Date getPersonDeathdate() {
		return personDeathdate;
	}

	@Order(51)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.BURIAL_DATE)
	@ExportGroup(ExportGroupType.PERSON)
	public Date getPersonBurialdate() {
		return personBurialdate;
	}

	@Order(52)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.BURIAL_CONDUCTOR)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonBurialconductor() {
		return personBurialconductor;
	}

	@Order(53)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.BURIAL_PLACE_DESCRIPTION)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonBurialplacedescription() {
		return personBurialplacedescription;
	}

	@Order(54)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(LocationDto.REGION)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getRegionName() {
		return regionName;
	}

	@Order(55)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(LocationDto.DISTRICT)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getDistrictName() {
		return districtName;
	}

	@Order(56)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(LocationDto.CITY)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getLocationCity() {
		return locationCity;
	}

	@Order(57)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(LocationDto.ADDRESS)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getLocationAddress() {
		return locationAddress;
	}

	@Order(58)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(LocationDto.POSTAL_CODE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getLocationPostalcode() {
		return locationPostalcode;
	}

	@Order(59)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.PHONE)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPersonPhone() {
		return personPhone;
	}

	@Order(60)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.PHONE_OWNER)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getPersonPhoneowner() {
		return personPhoneowner;
	}

	@Order(61)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.EDUCATION_TYPE)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonEducationtype() {
		return personEducationtype;
	}

	@Order(62)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.EDUCATION_DETAILS)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonEducationdetails() {
		return personEducationdetails;
	}

	@Order(63)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.OCCUPATION_TYPE)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonOccupationtype() {
		return personOccupationtype;
	}

	@Order(64)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.OCCUPATION_DETAILS)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonOccupationdetails() {
		return personOccupationdetails;
	}

	@Order(65)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.HEALTH_FACILITY)
	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getFaclityName() {
		return faclityName;
	}

//	@Order(66)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public String getFaclityUuid() {
		return faclityUuid;
	}

	@Order(67)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.OCCUPATION_FACILITY_DETAILS)
	@ExportGroup(ExportGroupType.PERSON)
	public String getPersonOccupationfacilitydetails() {
		return personOccupationfacilitydetails;
	}

	@Order(68)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE })
	@ExportProperty(CaseExportDto.TRAVELED)
	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public String getEpidataTraveled() {
		return epidataTraveled;
	}

	@Order(69)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE })
	@ExportProperty(EpiDataDto.BURIAL_ATTENDED)
	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public String getEpidataBurialattended() {
		return epidataBurialattended;
	}

	@Order(70)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE })
	@ExportProperty(EpiDataDto.DIRECT_CONTACT_CONFIRMED_CASE)
	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public String getEpidataDirectcontactconfirmedcase() {
		return epidataDirectcontactconfirmedcase;
	}

	@Order(71)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE })
	@ExportProperty(EpiDataDto.DIRECT_CONTACT_PROBABLE_CASE)
	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public String getEpidataDirectcontactprobablecase() {
		return epidataDirectcontactprobablecase;
	}

	@Order(72)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE })
	@ExportProperty(EpiDataDto.RODENTS)
	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public String getEpidataRodents() {
		return epidataRodents;
	}

	@Order(73)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.VACCINATION)
	@ExportGroup(ExportGroupType.VACCINATION)
	public String getCaseVaccination() {
		return caseVaccination;
	}

	@Order(74)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.VACCINATION_DOSES)
	@ExportGroup(ExportGroupType.VACCINATION)
	public String getCaseVaccinationdoses() {
		return caseVaccinationdoses;
	}

	@Order(75)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.VACCINATION_DATE)
	@ExportGroup(ExportGroupType.VACCINATION)
	public Date getCaseVaccinationdate() {
		return caseVaccinationdate;
	}

	@Order(76)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.VACCINATION_INFO_SOURCE)
	@ExportGroup(ExportGroupType.VACCINATION)
	public String getCaseVaccinationinfosource() {
		return caseVaccinationinfosource;
	}

//	@Order(77)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(PersonDto.I18N_PREFIX)
//	@ExportGroup(ExportGroupType.PERSON)
	public Long getPersonId() {
		return personId;
	}

//	@Order(78)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(LocationDto.I18N_PREFIX)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public Long getLocationId() {
		return locationId;
	}

//	@Order(79)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
////	@ExportProperty(EpiDataDto.I18N_PREFIX)
//	@ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
	public Long getEpidataId() {
		return epidataId;
	}

//	@Order(80)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(SymptomDataDto.)
//	@ExportGroup(ExportGroupType.SENSITIVE)
	public Long getSymptomsId() {
		return symptomsId;
	}

//	@Order(81)
//	@ExportTarget(caseExportTypes = {
//		CaseExportType.CASE_SURVEILLANCE,
//		CaseExportType.CASE_MANAGEMENT })
//	@ExportProperty(HospitalizationDto.I18N_PREFIX)
//	@ExportGroup(ExportGroupType.HOSPITALIZATION)
	public Long getHospitalizId() {
		return hospitalizId;
	}

	@Order(82)
	@ExportProperty(SYMPTOMS)
	public String getSymptoms() {
		return symptoms;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCaseSn(long caseSn) {
		this.caseSn = caseSn;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public void setEpidNumber(String epidNumber) {
		this.epidNumber = epidNumber;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setPregnancy(String pregnancy) {
		this.pregnancy = pregnancy;
	}

	public void setTrimester(String trimester) {
		this.trimester = trimester;
	}

	public void setPostpartum(String postpartum) {
		this.postpartum = postpartum;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setPersonBirthdateDd(Integer personBirthdateDd) {
		this.personBirthdateDd = personBirthdateDd;
	}

	public void setPersonBirthdateMm(Integer personBirthdateMm) {
		this.personBirthdateMm = personBirthdateMm;
	}

	public void setCaseBirthdateYyyy(Integer caseBirthdateYyyy) {
		this.caseBirthdateYyyy = caseBirthdateYyyy;
	}

	public void setDateOfReport(Date dateOfReport) {
		this.dateOfReport = dateOfReport;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setSubDistrict(String subDistrict) {
		this.subDistrict = subDistrict;
	}

	public void setHealthFacility(String healthFacility) {
		this.healthFacility = healthFacility;
	}

	public void setPointOfEntry(String pointOfEntry) {
		this.pointOfEntry = pointOfEntry;
	}

	public void setPlaceOfInitialDetection(String placeOfInitialDetection) {
		this.placeOfInitialDetection = placeOfInitialDetection;
	}

	public void setCaseCaseclassification(String caseCaseclassification) {
		this.caseCaseclassification = caseCaseclassification;
	}

	public void setCaseInvestigationstatus(String caseInvestigationstatus) {
		this.caseInvestigationstatus = caseInvestigationstatus;
	}

	public void setCaseOutcome(String caseOutcome) {
		this.caseOutcome = caseOutcome;
	}

	public void setCaseQuarantine(String caseQuarantine) {
		this.caseQuarantine = caseQuarantine;
	}

	public void setCaseQuarantineStart(Date caseQuarantineStart) {
		this.caseQuarantineStart = caseQuarantineStart;
	}

	public void setCaseQuarantineEnd(Date caseQuarantineEnd) {
		this.caseQuarantineEnd = caseQuarantineEnd;
	}

	public void setCaseClassificationSource(String caseClassificationSource) {
		this.caseClassificationSource = caseClassificationSource;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public void setHealthcondId(long healthcondId) {
		this.healthcondId = healthcondId;
	}

	public void setCaseDiseasedetails(String caseDiseasedetails) {
		this.caseDiseasedetails = caseDiseasedetails;
	}

	public void setPersonApproximateagetype(String personApproximateagetype) {
		this.personApproximateagetype = personApproximateagetype;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public void setRegionUuid(String regionUuid) {
		this.regionUuid = regionUuid;
	}

	public void setDistrictUuid(String districtUuid) {
		this.districtUuid = districtUuid;
	}

	public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
	}

	public void setFacilityUuid(String facilityUuid) {
		this.facilityUuid = facilityUuid;
	}

	public void setCaseHealthFacilityDetails(String caseHealthFacilityDetails) {
		this.caseHealthFacilityDetails = caseHealthFacilityDetails;
	}

	public void setPointofentUuid(String pointofentUuid) {
		this.pointofentUuid = pointofentUuid;
	}

	public void setPointofentrydetails(String pointofentrydetails) {
		this.pointofentrydetails = pointofentrydetails;
	}

	public void setHospitalizAdmittedtohealthfacility(String hospitalizAdmittedtohealthfacility) {
		this.hospitalizAdmittedtohealthfacility = hospitalizAdmittedtohealthfacility;
	}

	public void setHospitalizAdmissiondate(Date hospitalizAdmissiondate) {
		this.hospitalizAdmissiondate = hospitalizAdmissiondate;
	}

	public void setHospitalizDischargedate(Date hospitalizDischargedate) {
		this.hospitalizDischargedate = hospitalizDischargedate;
	}

	public void setHospitalizLeftagainstadvice(String hospitalizLeftagainstadvice) {
		this.hospitalizLeftagainstadvice = hospitalizLeftagainstadvice;
	}

	public void setPersonPresentcondition(Integer personPresentcondition) {
		this.personPresentcondition = personPresentcondition;
	}

	public void setPersonDeathdate(Date personDeathdate) {
		this.personDeathdate = personDeathdate;
	}

	public void setPersonBurialdate(Date personBurialdate) {
		this.personBurialdate = personBurialdate;
	}

	public void setPersonBurialconductor(String personBurialconductor) {
		this.personBurialconductor = personBurialconductor;
	}

	public void setPersonBurialplacedescription(String personBurialplacedescription) {
		this.personBurialplacedescription = personBurialplacedescription;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public void setLocationPostalcode(String locationPostalcode) {
		this.locationPostalcode = locationPostalcode;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public void setPersonPhoneowner(String personPhoneowner) {
		this.personPhoneowner = personPhoneowner;
	}

	public void setPersonEducationtype(String personEducationtype) {
		this.personEducationtype = personEducationtype;
	}

	public void setPersonEducationdetails(String personEducationdetails) {
		this.personEducationdetails = personEducationdetails;
	}

	public void setPersonOccupationtype(String personOccupationtype) {
		this.personOccupationtype = personOccupationtype;
	}

	public void setPersonOccupationdetails(String personOccupationdetails) {
		this.personOccupationdetails = personOccupationdetails;
	}

	public void setFaclityName(String faclityName) {
		this.faclityName = faclityName;
	}

	public void setFaclityUuid(String faclityUuid) {
		this.faclityUuid = faclityUuid;
	}

	public void setPersonOccupationfacilitydetails(String personOccupationfacilitydetails) {
		this.personOccupationfacilitydetails = personOccupationfacilitydetails;
	}

	public void setEpidataTraveled(String epidataTraveled) {
		this.epidataTraveled = epidataTraveled;
	}

	public void setEpidataBurialattended(String epidataBurialattended) {
		this.epidataBurialattended = epidataBurialattended;
	}

	public void setEpidataDirectcontactconfirmedcase(String epidataDirectcontactconfirmedcase) {
		this.epidataDirectcontactconfirmedcase = epidataDirectcontactconfirmedcase;
	}

	public void setEpidataDirectcontactprobablecase(String epidataDirectcontactprobablecase) {
		this.epidataDirectcontactprobablecase = epidataDirectcontactprobablecase;
	}

	public void setEpidataRodents(String epidataRodents) {
		this.epidataRodents = epidataRodents;
	}

	public void setCaseVaccination(String caseVaccination) {
		this.caseVaccination = caseVaccination;
	}

	public void setCaseVaccinationdoses(String caseVaccinationdoses) {
		this.caseVaccinationdoses = caseVaccinationdoses;
	}

	public void setCaseVaccinationdate(Date caseVaccinationdate) {
		this.caseVaccinationdate = caseVaccinationdate;
	}

	public void setCaseVaccinationinfosource(String caseVaccinationinfosource) {
		this.caseVaccinationinfosource = caseVaccinationinfosource;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public void setEpidataId(Long epidataId) {
		this.epidataId = epidataId;
	}

	public void setSymptomsId(Long symptomsId) {
		this.symptomsId = symptomsId;
	}

	public void setHospitalizId(Long hospitalizId) {
		this.hospitalizId = hospitalizId;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

}
