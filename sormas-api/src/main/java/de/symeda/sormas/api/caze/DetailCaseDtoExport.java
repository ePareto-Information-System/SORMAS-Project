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


	private Integer personBirthdateDd;
	private Integer personBirthdateMm;
	private Integer caseBirthdateYyyy;
	private String userUuid;
	private String regionUuid;
	private String districtUuid;
	private String communityUuid;
	private String facilityUuid;
	private String caseHealthFacilityDetails;
	private String pointofentUuid;

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
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.UUID)
	@ExportGroup(ExportGroupType.CORE)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.EPID_NUMBER)
	@ExportGroup(ExportGroupType.CORE)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	@ExportProperty(CaseDataDto.DISEASE)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.FIRST_NAME)
	}

	@ExportProperty(PersonDto.LAST_NAME)
	}

	@ExportProperty(PersonDto.SEX)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.PREGNANT)
	@ExportGroup(ExportGroupType.SENSITIVE)
	}

	}

	@Order(11)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	public Integer getPersonBirthdateDd() {
		return personBirthdateDd;
	}

	public Integer getPersonBirthdateMm() {
		return personBirthdateMm;
	}

	public Integer getCaseBirthdateYyyy() {
		return caseBirthdateYyyy;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	@Order(19)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@Order(21)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@Order(23)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	}

	@Order(24)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@Order(26)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	@Order(27)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	@Order(28)
	}

	@Order(29)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	@Order(30)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(CaseDataDto.QUARANTINE_TO)
	}

	@Order(31)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportProperty(PersonDto.APPROXIMATE_AGE_REFERENCE_DATE)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	@ExportGroup(ExportGroupType.CORE)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getHospitalizAdmittedtohealthfacility() {
		return hospitalizAdmittedtohealthfacility;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Date getHospitalizAdmissiondate() {
		return hospitalizAdmissiondate;
	}

	@Order(46)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Date getHospitalizDischargedate() {
		return hospitalizDischargedate;
	}

	@Order(48)
	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getHospitalizLeftagainstadvice() {
		return hospitalizLeftagainstadvice;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Integer getPersonPresentcondition() {
		return personPresentcondition;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Date getPersonDeathdate() {
		return personDeathdate;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Date getPersonBurialdate() {
		return personBurialdate;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonBurialconductor() {
		return personBurialconductor;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonBurialplacedescription() {
		return personBurialplacedescription;
	}

	@Order(54)
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getLocationAddress() {
		return locationAddress;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getLocationPostalcode() {
		return locationPostalcode;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonPhone() {
		return personPhone;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonPhoneowner() {
		return personPhoneowner;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonEducationtype() {
		return personEducationtype;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonEducationdetails() {
		return personEducationdetails;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonOccupationtype() {
		return personOccupationtype;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getPersonOccupationdetails() {
		return personOccupationdetails;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	}

	}

	@ExportTarget(caseExportTypes = {
	public String getEpidataBurialattended() {
		return epidataBurialattended;
	}

	@ExportTarget(caseExportTypes = {
	public String getEpidataDirectcontactconfirmedcase() {
		return epidataDirectcontactconfirmedcase;
	}

	@ExportTarget(caseExportTypes = {
	public String getEpidataDirectcontactprobablecase() {
		return epidataDirectcontactprobablecase;
	}

	@ExportTarget(caseExportTypes = {
	public String getEpidataRodents() {
		return epidataRodents;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getCaseVaccination() {
		return caseVaccination;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getCaseVaccinationdoses() {
		return caseVaccinationdoses;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public Date getCaseVaccinationdate() {
		return caseVaccinationdate;
	}

	@ExportTarget(caseExportTypes = {
		CaseExportType.CASE_SURVEILLANCE,
		CaseExportType.CASE_MANAGEMENT })
	public String getCaseVaccinationinfosource() {
		return caseVaccinationinfosource;
	}

	}

	}

	public void setCasePostpartum(String casePostpartum) {
		this.casePostpartum = casePostpartum;
	}

	}

	public void setCaseTrimester(String caseTrimester) {
		this.caseTrimester = caseTrimester;
	}

	}

	}

	}

	}

	}

	}

	}

	}

	}

	}

	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

}
