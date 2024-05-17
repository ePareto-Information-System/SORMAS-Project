/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.api.sample;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.sormastosormas.S2SIgnoreProperty;
import de.symeda.sormas.api.sormastosormas.SormasToSormasConfig;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

@DependingOnFeatureType(featureType = FeatureType.SAMPLES_LAB)
public class PathogenTestDto extends PseudonymizableDto {

	private static final long serialVersionUID = -5213210080802372054L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 3391;

	public static final String I18N_PREFIX = "PathogenTest";

	public static final String SAMPLE = "sample";
	public static final String TESTED_DISEASE = "testedDisease";
	public static final String SECOND_TESTED_DISEASE = "secondTestedDisease";
	public static final String TESTED_DISEASE_VARIANT = "testedDiseaseVariant";
	public static final String TESTED_DISEASE_VARIANT_DETAILS = "testedDiseaseVariantDetails";
	public static final String TYPING_ID = "typingId";
	public static final String TEST_TYPE = "testType";
	public static final String PCR_TEST_SPECIFICATION = "pcrTestSpecification";
	public static final String TESTED_DISEASE_DETAILS = "testedDiseaseDetails";
	public static final String TEST_TYPE_TEXT = "testTypeText";
	public static final String TEST_DATE_TIME = "testDateTime";
	public static final String LAB = "lab";
	public static final String LAB_DETAILS = "labDetails";
	public static final String LAB_USER = "labUser";
	public static final String TEST_RESULT = "testResult";
	public static final String TEST_RESULT_FOR_SECOND_DISEASE = "testResultForSecondDisease";
	public static final String TEST_RESULT_VARIANT = "testResultVariant";
	public static final String VARIANT_OTHER_SPECIFY = "variantOtherSpecify";
	public static final String TEST_RESULT_TEXT = "testResultText";
	public static final String TEST_RESULT_VERIFIED = "testResultVerified";
	public static final String FOUR_FOLD_INCREASE_ANTIBODY_TITER = "fourFoldIncreaseAntibodyTiter";
	public static final String SEROTYPE = "serotype";
	public static final String CQ_VALUE = "cqValue";
	public static final String CT_VALUE_E = "ctValueE";
	public static final String CT_VALUE_N = "ctValueN";
	public static final String CT_VALUE_RDRP = "ctValueRdrp";
	public static final String CT_VALUE_S = "ctValueS";
	public static final String CT_VALUE_ORF_1 = "ctValueOrf1";
	public static final String CT_VALUE_RDRP_S = "ctValueRdrpS";
	public static final String REPORT_DATE = "reportDate";
	public static final String VIA_LIMS = "viaLims";
	public static final String EXTERNAL_ID = "externalId";
	public static final String EXTERNAL_ORDER_ID = "externalOrderId";
	public static final String PRELIMINARY = "preliminary";
	public static final String DELETION_REASON = "deletionReason";
	public static final String OTHER_DELETION_REASON = "otherDeletionReason";
	public static final String PRESCRIBER_PHYSICIAN_CODE = "prescriberPhysicianCode";
	public static final String PRESCRIBER_FIRST_NAME = "prescriberFirstName";
	public static final String PRESCRIBER_LAST_NAME = "prescriberLastName";
	public static final String PRESCRIBER_PHONE_NUMBER = "prescriberPhoneNumber";
	public static final String PRESCRIBER_ADDRESS = "prescriberAddress";
	public static final String PRESCRIBER_POSTAL_CODE = "prescriberPostalCode";
	public static final String PRESCRIBER_CITY = "prescriberCity";
	public static final String PRESCRIBER_COUNTRY = "prescriberCountry";
	public static final String LABORATORY_TYPE = "laboratoryType";
	public static final String LABORATORY_NAME = "laboratoryName";
	public static final String LABORATORY_TEST_PERFORMED = "laboratoryTestPerformed";
	public static final String LABORATORY_TEST_PERFORMED_OTHER = "laboratoryTestPerformedOther";
	public static final String LABORATORY_CYTOLOGY_LEUCOCYTES = "laboratoryCytology";
	public static final String LABORATORY_CYTOLOGY_PMN = "laboratoryCytologyPmn";
	public static final String LABORATORY_CYTOLOGY_LYMPH = "laboratoryCytologyLymph";
	public static final String LABORATORY_GRAM = "laboratoryGram";
	public static final String LABORATORY_GRAM_OTHER = "laboratoryGramOther";
	public static final String LABORATORY_RDT_PERFORMED = "laboratoryRdtPerformed";
	public static final String LABORATORY_RDT_RESULTS = "laboratoryRdtResults";
	public static final String LABORATORY_LATEX = "laboratoryLatex";
	public static final String OTHER_TEST = "laboratoryLatexOtherResults";
	public static final String DATE_SENT_REPORTING_HEALTH_FACILITY = "dateSentReportingHealthFac";
	public static final String DATE_SAMPLE_SENT_REGREF_LAB = "dateSampleSentRegRefLab";
	public static final String LABORATORY_CULTURE = "laboratoryCulture";
	public static final String LABORATORY_CULTURE_OTHER = "laboratoryCultureOther";
	public static final String LABORATORY_OTHER_TESTS = "laboratoryOtherTests";
	public static final String LABORATORY_OTHER_TESTS_RESULTS = "laboratoryOtherTestsResults";
	public static final String LABORATORY_CEFTRIAXONE = "laboratoryCeftriaxone";
	public static final String LABORATORY_PENICILLIN_G = "laboratoryPenicillinG";
	public static final String LABORATORY_AMOXYCILLIN = "laboratoryAmoxycillin";
	public static final String LABORATORY_OXACILLIN = "laboratoryOxacillin";
	public static final String LABORATORY_ANTIBIOGRAM_OTHER = "laboratoryAntibiogramOther";
	public static final String DATE_SAMPLE_SENT_REF_LAB = "dateSampleSentRegLab";
	public static final String LABORATORY_DATE_PCR_PERFORMED = "laboratoryDatePcrPerformed";
	public static final String LABORATORY_PCR_TYPE = "laboratoryPcrType";
	public static final String LABORATORY_PCR_OPTIONS = "laboratoryPcrOptions";
	public static final String LABORATORY_SEROTYPE = "laboratorySerotype";
	public static final String LABORATORY_SEROTYPE_TYPE = "laboratorySerotypeType";
	public static final String LABORATORY_SEROTYPE_RESULTS = "laboratorySerotypeResults";
	public static final String LABORATORY_FINAL_RESULTS = "laboratoryFinalResults";
	public static final String LABORATORY_OBSERVATIONS = "laboratoryObservations";
	public static final String LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY = "laboratoryDateResultsSentHealthFacility";
	public static final String LABORATORY_DATE_RESULTS_SENT_DSD = "laboratoryDateResultsSentDSD";
	public static final String LABORATORY_FINAL_CLASSIFICATION = "laboratoryFinalClassification";
	public static final String LAB_LOCATION = "labLocation";
	public static final String DATE_LAB_RECEIVED_SPECIMEN = "dateLabReceivedSpecimen";
	public static final String SPECIMEN_CONDITION = "specimenCondition";
	public static final String DATE_LAB_RESULTS_SENT_DISTRICT = "dateLabResultsSentDistrict";
	public static final String DATE_LAB_RESULTS_SENT_CLINICIAN = "dateLabResultsSentClinician";
	public static final String DATE_DISTRICT_RECEIVED_LAB_RESULTS = "dateDistrictReceivedLabResults";



	@NotNull(message = Validations.validSample)
	private SampleReferenceDto sample;
//	@NotNull(message = Validations.validDisease)
	private Disease testedDisease;
	private Disease secondTestedDisease;
	private DiseaseVariant testedDiseaseVariant;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String testedDiseaseDetails;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String testedDiseaseVariantDetails;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String typingId;
	@NotNull(message = Validations.requiredField)
	private PathogenTestType testType;
	private PCRTestSpecification pcrTestSpecification;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String testTypeText;
	private Date testDateTime;
	@NotNull(message = Validations.requiredField)
	private FacilityReferenceDto lab;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String labDetails;
	@SensitiveData
	private UserReferenceDto labUser;
	@NotNull(message = Validations.requiredField)
	private PathogenTestResultType testResult;
	private PathogenTestResultType testResultForSecondDisease;
	private PathogenTestResultVariant testResultVariant;
	private String variantOtherSpecify;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String testResultText;
	@NotNull(message = Validations.requiredField)
	private Boolean testResultVerified;
	private boolean fourFoldIncreaseAntibodyTiter;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String serotype;
	private Float cqValue;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueE;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueN;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueRdrp;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueS;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueOrf1;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private Float ctValueRdrpS;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_GERMANY)
	private Date reportDate;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_GERMANY)
	private boolean viaLims;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_GERMANY)
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_EXTERNAL_ID)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String externalId;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_GERMANY)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String externalOrderId;
	private Boolean preliminary;
	private boolean deleted;
	private DeletionReason deletionReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherDeletionReason;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberPhysicianCode;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberFirstName;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberLastName;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberPhoneNumber;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberAddress;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberPostalCode;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String prescriberCity;
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_LUXEMBOURG)
	private CountryReferenceDto prescriberCountry;

	private LabType laboratoryType;
	private String laboratoryName;
	private LabTest laboratoryTestPerformed;
	private String laboratoryTestPerformedOther;
	private String laboratoryCytology;
	private String laboratoryCytologyPmn;
	private String laboratoryCytologyLymph;
	private Gram laboratoryGram;
	private String laboratoryGramOther;
	private YesNo laboratoryRdtPerformed;
	private String laboratoryRdtResults;
	private LatexCulture laboratoryLatex;
	private String laboratoryLatexOtherResults;
	private Date dateSentReportingHealthFac;
	private Date dateSampleSentRegRefLab;
	private LatexCulture laboratoryCulture;
	private String laboratoryCultureOther;
	private String laboratoryOtherTests;
	private String laboratoryOtherTestsResults;
	private Antibiogram laboratoryCeftriaxone;
	private Antibiogram laboratoryPenicillinG;
	private Antibiogram laboratoryAmoxycillin;
	private Antibiogram laboratoryOxacillin;
	private Antibiogram laboratoryAntibiogramOther;
	private Date dateSampleSentRegLab;
	private Date laboratoryDatePcrPerformed;
	private String laboratoryPcrType;
	private LatexCulture laboratoryPcrOptions;
	private String laboratorySerotype;
	private String laboratorySerotypeType;
	private String laboratorySerotypeResults;
	private String laboratoryFinalResults;
	private String laboratoryObservations;
	private Date laboratoryDateResultsSentHealthFacility;
	private Date laboratoryDateResultsSentDSD;
	private CaseClassification laboratoryFinalClassification;
	private String labLocation;
	private Date dateLabReceivedSpecimen;
	private SpecimenCondition specimenCondition;
	private Date dateLabResultsSentDistrict;
	private Date dateLabResultsSentClinician;
	private Date dateDistrictReceivedLabResults;

	public static PathogenTestDto build(SampleDto sample, UserDto currentUser) {

		PathogenTestDto pathogenTest = new PathogenTestDto();
		pathogenTest.setUuid(DataHelper.createUuid());
		pathogenTest.setSample(sample.toReference());
		if (sample.getSamplePurpose() == SamplePurpose.INTERNAL) {
			pathogenTest.setTestResultVerified(true);
		}
		pathogenTest.setLab(currentUser.getLaboratory());
		if (pathogenTest.getLab() == null) {
			pathogenTest.setLab(sample.getLab());
			pathogenTest.setLabDetails(sample.getLabDetails());
		}
		pathogenTest.setLabUser(currentUser.toReference());
		return pathogenTest;
	}

	public static PathogenTestDto build(SampleReferenceDto sample, UserReferenceDto currentUser) {

		PathogenTestDto pathogenTest = new PathogenTestDto();
		pathogenTest.setUuid(DataHelper.createUuid());
		pathogenTest.setSample(sample);
		pathogenTest.setLabUser(currentUser);
		return pathogenTest;
	}

	@ImportIgnore
	public SampleReferenceDto getSample() {
		return sample;
	}

	public void setSample(SampleReferenceDto sample) {
		this.sample = sample;
	}

	public Disease getTestedDisease() {
		return testedDisease;
	}

	public void setTestedDisease(Disease testedDisease) {
		this.testedDisease = testedDisease;
	}

	public DiseaseVariant getTestedDiseaseVariant() {
		return testedDiseaseVariant;
	}

	public void setTestedDiseaseVariant(DiseaseVariant testedDiseaseVariant) {
		this.testedDiseaseVariant = testedDiseaseVariant;
	}

	public String getTestedDiseaseDetails() {
		return testedDiseaseDetails;
	}

	public void setTestedDiseaseDetails(String testedDiseaseDetails) {
		this.testedDiseaseDetails = testedDiseaseDetails;
	}

	public String getTestedDiseaseVariantDetails() {
		return testedDiseaseVariantDetails;
	}

	public void setTestedDiseaseVariantDetails(String testedDiseaseVariantDetails) {
		this.testedDiseaseVariantDetails = testedDiseaseVariantDetails;
	}

	public String getTypingId() {
		return typingId;
	}

	public void setTypingId(String typingId) {
		this.typingId = typingId;
	}

	public PathogenTestType getTestType() {
		return testType;
	}

	public void setTestType(PathogenTestType testType) {
		this.testType = testType;
	}

	public PCRTestSpecification getPcrTestSpecification() {
		return pcrTestSpecification;
	}

	public void setPcrTestSpecification(PCRTestSpecification pcrTestSpecification) {
		this.pcrTestSpecification = pcrTestSpecification;
	}

	public String getTestTypeText() {
		return testTypeText;
	}

	public void setTestTypeText(String testTypeText) {
		this.testTypeText = testTypeText;
	}

	public Date getTestDateTime() {
		return testDateTime;
	}

	public void setTestDateTime(Date testDateTime) {
		this.testDateTime = testDateTime;
	}

	public FacilityReferenceDto getLab() {
		return lab;
	}

	public void setLab(FacilityReferenceDto lab) {
		this.lab = lab;
	}

	public String getLabDetails() {
		return labDetails;
	}

	public void setLabDetails(String labDetails) {
		this.labDetails = labDetails;
	}

	public UserReferenceDto getLabUser() {
		return labUser;
	}

	public void setLabUser(UserReferenceDto labUser) {
		this.labUser = labUser;
	}

	public PathogenTestResultType getTestResult() {
		return testResult;
	}

	public void setTestResult(PathogenTestResultType testResult) {
		this.testResult = testResult;
	}

	public String getTestResultText() {
		return testResultText;
	}

	public void setTestResultText(String testResultText) {
		this.testResultText = testResultText;
	}

	public Boolean getTestResultVerified() {
		return testResultVerified;
	}

	public void setTestResultVerified(Boolean testResultVerified) {
		this.testResultVerified = testResultVerified;
	}

	public boolean isFourFoldIncreaseAntibodyTiter() {
		return fourFoldIncreaseAntibodyTiter;
	}

	public void setFourFoldIncreaseAntibodyTiter(boolean fourFoldIncreaseAntibodyTiter) {
		this.fourFoldIncreaseAntibodyTiter = fourFoldIncreaseAntibodyTiter;
	}

	public PathogenTestReferenceDto toReference() {
		return new PathogenTestReferenceDto(getUuid());
	}

	public String getSerotype() {
		return serotype;
	}

	public void setSerotype(String serotype) {
		this.serotype = serotype;
	}

	public Float getCqValue() {
		return cqValue;
	}

	public void setCqValue(Float cqValue) {
		this.cqValue = cqValue;
	}

	public Float getCtValueE() {
		return ctValueE;
	}

	public void setCtValueE(Float ctValueE) {
		this.ctValueE = ctValueE;
	}

	public Float getCtValueN() {
		return ctValueN;
	}

	public void setCtValueN(Float ctValueN) {
		this.ctValueN = ctValueN;
	}

	public Float getCtValueRdrp() {
		return ctValueRdrp;
	}

	public void setCtValueRdrp(Float ctValueRdrp) {
		this.ctValueRdrp = ctValueRdrp;
	}

	public Float getCtValueS() {
		return ctValueS;
	}

	public void setCtValueS(Float ctValueS) {
		this.ctValueS = ctValueS;
	}

	public Float getCtValueOrf1() {
		return ctValueOrf1;
	}

	public void setCtValueOrf1(Float ctValueOrf1) {
		this.ctValueOrf1 = ctValueOrf1;
	}

	public Float getCtValueRdrpS() {
		return ctValueRdrpS;
	}

	public void setCtValueRdrpS(Float ctValueRdrpS) {
		this.ctValueRdrpS = ctValueRdrpS;
	}

	@Override
	public String buildCaption() {
		return DateFormatHelper.formatLocalDateTime(testDateTime) + " - " + testType + " (" + testedDisease + "): " + testResult;
	}

	@JsonIgnore
	public String i18nPrefix() {
		return I18N_PREFIX;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public boolean isViaLims() {
		return viaLims;
	}

	public void setViaLims(boolean viaLims) {
		this.viaLims = viaLims;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalOrderId() {
		return externalOrderId;
	}

	public void setExternalOrderId(String externalOrderId) {
		this.externalOrderId = externalOrderId;
	}

	public Boolean getPreliminary() {
		return preliminary;
	}

	public void setPreliminary(Boolean preliminary) {
		this.preliminary = preliminary;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeletionReason getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(DeletionReason deletionReason) {
		this.deletionReason = deletionReason;
	}

	public String getOtherDeletionReason() {
		return otherDeletionReason;
	}

	public void setOtherDeletionReason(String otherDeletionReason) {
		this.otherDeletionReason = otherDeletionReason;
	}

	public String getPrescriberPhysicianCode() {
		return prescriberPhysicianCode;
	}

	public void setPrescriberPhysicianCode(String prescriberPhysicianCode) {
		this.prescriberPhysicianCode = prescriberPhysicianCode;
	}

	public String getPrescriberFirstName() {
		return prescriberFirstName;
	}

	public void setPrescriberFirstName(String prescriberFirstName) {
		this.prescriberFirstName = prescriberFirstName;
	}

	public String getPrescriberLastName() {
		return prescriberLastName;
	}

	public void setPrescriberLastName(String prescriberLastName) {
		this.prescriberLastName = prescriberLastName;
	}

	public String getPrescriberPhoneNumber() {
		return prescriberPhoneNumber;
	}

	public void setPrescriberPhoneNumber(String prescriberPhoneNumber) {
		this.prescriberPhoneNumber = prescriberPhoneNumber;
	}

	public String getPrescriberAddress() {
		return prescriberAddress;
	}

	public void setPrescriberAddress(String prescriberAddress) {
		this.prescriberAddress = prescriberAddress;
	}

	public String getPrescriberPostalCode() {
		return prescriberPostalCode;
	}

	public void setPrescriberPostalCode(String prescriberPostalCode) {
		this.prescriberPostalCode = prescriberPostalCode;
	}

	public String getPrescriberCity() {
		return prescriberCity;
	}

	public void setPrescriberCity(String prescriberCity) {
		this.prescriberCity = prescriberCity;
	}

	public CountryReferenceDto getPrescriberCountry() {
		return prescriberCountry;
	}

	public void setPrescriberCountry(CountryReferenceDto prescriberCountry) {
		this.prescriberCountry = prescriberCountry;
	}

	@Override
	public PathogenTestDto clone() throws CloneNotSupportedException {
		return (PathogenTestDto) super.clone();
	}

	public LabType getLaboratoryType() {
		return laboratoryType;
	}

	public void setLaboratoryType(LabType laboratoryType) {
		this.laboratoryType = laboratoryType;
	}
	public String getLaboratoryName() {
		return laboratoryName;
	}

	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}
	public LabTest getLaboratoryTestPerformed() {
		return laboratoryTestPerformed;
	}

	public void setLaboratoryTestPerformed(LabTest laboratoryTestPerformed) {
		this.laboratoryTestPerformed = laboratoryTestPerformed;
	}

	public String getLaboratoryTestPerformedOther() {
		return laboratoryTestPerformedOther;
	}

	public void setLaboratoryTestPerformedOther(String laboratoryTestPerformedOther) {
		this.laboratoryTestPerformedOther = laboratoryTestPerformedOther;
	}

	public String getLaboratoryCytology() {
		return laboratoryCytology;
	}

	public void setLaboratoryCytology(String laboratoryCytology) {
		this.laboratoryCytology = laboratoryCytology;
	}

	public String getLaboratoryCytologyPmn() {
		return laboratoryCytologyPmn;
	}

	public void setLaboratoryCytologyPmn(String laboratoryCytologyPmn) {
		this.laboratoryCytologyPmn = laboratoryCytologyPmn;
	}

	public String getLaboratoryCytologyLymph() {
		return laboratoryCytologyLymph;
	}

	public void setLaboratoryCytologyLymph(String laboratoryCytologyLymph) {
		this.laboratoryCytologyLymph = laboratoryCytologyLymph;
	}

	public Gram getLaboratoryGram() {
		return laboratoryGram;
	}

	public void setLaboratoryGram(Gram laboratoryGram) {
		this.laboratoryGram = laboratoryGram;
	}

	public String getLaboratoryGramOther() {
		return laboratoryGramOther;
	}

	public void setLaboratoryGramOther(String laboratoryGramOther) {
		this.laboratoryGramOther = laboratoryGramOther;
	}

	public YesNo getLaboratoryRdtPerformed() {
		return laboratoryRdtPerformed;
	}

	public void setLaboratoryRdtPerformed(YesNo laboratoryRdtPerformed) {
		this.laboratoryRdtPerformed = laboratoryRdtPerformed;
	}

	public String getLaboratoryRdtResults() {
		return laboratoryRdtResults;
	}

	public void setLaboratoryRdtResults(String laboratoryRdtResults) {
		this.laboratoryRdtResults = laboratoryRdtResults;
	}

	public LatexCulture getLaboratoryLatex() {
		return laboratoryLatex;
	}

	public void setLaboratoryLatex(LatexCulture laboratoryLatex) {
		this.laboratoryLatex = laboratoryLatex;
	}

	public String getLaboratoryLatexOtherResults() {
		return laboratoryLatexOtherResults;
	}

	public void setLaboratoryLatexOtherResults(String laboratoryLatexOtherResults) {
		this.laboratoryLatexOtherResults = laboratoryLatexOtherResults;
	}

	public Date getDateSentReportingHealthFac() {
		return dateSentReportingHealthFac;
	}

	public void setDateSentReportingHealthFac(Date dateSentReportingHealthFac) {
		this.dateSentReportingHealthFac = dateSentReportingHealthFac;
	}

	public Date getDateSampleSentRegRefLab() {
		return dateSampleSentRegRefLab;
	}

	public void setDateSampleSentRegRefLab(Date dateSampleSentRegRefLab) {
		this.dateSampleSentRegRefLab = dateSampleSentRegRefLab;
	}

	public LatexCulture getLaboratoryCulture() {
		return laboratoryCulture;
	}

	public void setLaboratoryCulture(LatexCulture laboratoryCulture) {
		this.laboratoryCulture = laboratoryCulture;
	}

	public String getLaboratoryCultureOther() {
		return laboratoryCultureOther;
	}

	public void setLaboratoryCultureOther(String laboratoryCultureOther) {
		this.laboratoryCultureOther = laboratoryCultureOther;
	}

	public String getLaboratoryOtherTests() {
		return laboratoryOtherTests;
	}

	public void setLaboratoryOtherTests(String laboratoryOtherTests) {
		this.laboratoryOtherTests = laboratoryOtherTests;
	}

	public String getLaboratoryOtherTestsResults() {
		return laboratoryOtherTestsResults;
	}

	public void setLaboratoryOtherTestsResults(String laboratoryOtherTestsResults) {
		this.laboratoryOtherTestsResults = laboratoryOtherTestsResults;
	}

	public Antibiogram getLaboratoryCeftriaxone() {
		return laboratoryCeftriaxone;
	}

	public void setLaboratoryCeftriaxone(Antibiogram laboratoryCeftriaxone) {
		this.laboratoryCeftriaxone = laboratoryCeftriaxone;
	}

	public Antibiogram getLaboratoryPenicillinG() {
		return laboratoryPenicillinG;
	}

	public void setLaboratoryPenicillinG(Antibiogram laboratoryPenicillinG) {
		this.laboratoryPenicillinG = laboratoryPenicillinG;
	}

	public Antibiogram getLaboratoryAmoxycillin() {
		return laboratoryAmoxycillin;
	}

	public void setLaboratoryAmoxycillin(Antibiogram laboratoryAmoxycillin) {
		this.laboratoryAmoxycillin = laboratoryAmoxycillin;
	}

	public Antibiogram getLaboratoryOxacillin() {
		return laboratoryOxacillin;
	}

	public void setLaboratoryOxacillin(Antibiogram laboratoryOxacillin) {
		this.laboratoryOxacillin = laboratoryOxacillin;
	}

	public Antibiogram getLaboratoryAntibiogramOther() {
		return laboratoryAntibiogramOther;
	}

	public void setLaboratoryAntibiogramOther(Antibiogram laboratoryAntibiogramOther) {
		this.laboratoryAntibiogramOther = laboratoryAntibiogramOther;
	}

	public Date getDateSampleSentRegLab() {
		return dateSampleSentRegLab;
	}

	public void setDateSampleSentRegLab(Date dateSampleSentRegLab) {
		this.dateSampleSentRegLab = dateSampleSentRegLab;
	}
	public Date getLaboratoryDatePcrPerformed() {
		return laboratoryDatePcrPerformed;
	}

	public void setLaboratoryDatePcrPerformed(Date laboratoryDatePcrPerformed) {
		this.laboratoryDatePcrPerformed = laboratoryDatePcrPerformed;
	}

	public String getLaboratoryPcrType() {
		return laboratoryPcrType;
	}

	public void setLaboratoryPcrType(String laboratoryPcrType) {
		this.laboratoryPcrType = laboratoryPcrType;
	}

	public LatexCulture getLaboratoryPcrOptions() {
		return laboratoryPcrOptions;
	}

	public void setLaboratoryPcrOptions(LatexCulture laboratoryPcrOptions) {
		this.laboratoryPcrOptions = laboratoryPcrOptions;
	}

	public String getLaboratorySerotype() {
		return laboratorySerotype;
	}

	public void setLaboratorySerotype(String laboratorySerotype) {
		this.laboratorySerotype = laboratorySerotype;
	}

	public String getLaboratorySerotypeType() {
		return laboratorySerotypeType;
	}

	public void setLaboratorySerotypeType(String laboratorySerotypeType) {
		this.laboratorySerotypeType = laboratorySerotypeType;
	}

	public String getLaboratorySerotypeResults() {
		return laboratorySerotypeResults;
	}

	public void setLaboratorySerotypeResults(String laboratorySerotypeResults) {
		this.laboratorySerotypeResults = laboratorySerotypeResults;
	}

	public String getLaboratoryFinalResults() {
		return laboratoryFinalResults;
	}

	public void setLaboratoryFinalResults(String laboratoryFinalResults) {
		this.laboratoryFinalResults = laboratoryFinalResults;
	}

	public String getLaboratoryObservations() {
		return laboratoryObservations;
	}

	public void setLaboratoryObservations(String laboratoryObservations) {
		this.laboratoryObservations = laboratoryObservations;
	}

	public Date getLaboratoryDateResultsSentHealthFacility() {
		return laboratoryDateResultsSentHealthFacility;
	}

	public void setLaboratoryDateResultsSentHealthFacility(Date laboratoryDateResultsSentHealthFacility) {
		this.laboratoryDateResultsSentHealthFacility = laboratoryDateResultsSentHealthFacility;
	}

	public Date getLaboratoryDateResultsSentDSD() {
		return laboratoryDateResultsSentDSD;
	}

	public void setLaboratoryDateResultsSentDSD(Date laboratoryDateResultsSentDSD) {
		this.laboratoryDateResultsSentDSD = laboratoryDateResultsSentDSD;
	}

	public CaseClassification getLaboratoryFinalClassification() {
		return laboratoryFinalClassification;
	}

	public void setLaboratoryFinalClassification(CaseClassification laboratoryFinalClassification) {
		this.laboratoryFinalClassification = laboratoryFinalClassification;
	}

	public String getLabLocation() {
		return labLocation;
	}

	public void setLabLocation(String labLocation) {
		this.labLocation = labLocation;
	}

	public Date getDateLabReceivedSpecimen() {
		return dateLabReceivedSpecimen;
	}

	public void setDateLabReceivedSpecimen(Date dateLabReceivedSpecimen) {
		this.dateLabReceivedSpecimen = dateLabReceivedSpecimen;
	}

	public SpecimenCondition getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(SpecimenCondition specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	public Date getDateLabResultsSentDistrict() {
		return dateLabResultsSentDistrict;
	}

	public void setDateLabResultsSentDistrict(Date dateLabResultsSentDistrict) {
		this.dateLabResultsSentDistrict = dateLabResultsSentDistrict;
	}

	public Date getDateLabResultsSentClinician() {
		return dateLabResultsSentClinician;
	}

	public void setDateLabResultsSentClinician(Date dateLabResultsSentClinician) {
		this.dateLabResultsSentClinician = dateLabResultsSentClinician;
	}

	public Date getDateDistrictReceivedLabResults() {
		return dateDistrictReceivedLabResults;
	}

	public void setDateDistrictReceivedLabResults(Date dateDistrictReceivedLabResults) {
		this.dateDistrictReceivedLabResults = dateDistrictReceivedLabResults;
	}

	public PathogenTestResultVariant getTestResultVariant() {
		return testResultVariant;
	}

	public void setTestResultVariant(PathogenTestResultVariant testResultVariant) {
		this.testResultVariant = testResultVariant;
	}

	public String getVariantOtherSpecify() {
		return variantOtherSpecify;
	}

	public void setVariantOtherSpecify(String variantOtherSpecify) {
		this.variantOtherSpecify = variantOtherSpecify;
	}

	public Disease getSecondTestedDisease() {
		return secondTestedDisease;
	}

	public void setSecondTestedDisease(Disease secondTestedDisease) {
		this.secondTestedDisease = secondTestedDisease;
	}

	public PathogenTestResultType getTestResultForSecondDisease() {
		return testResultForSecondDisease;
	}

	public void setTestResultForSecondDisease(PathogenTestResultType testResultForSecondDisease) {
		this.testResultForSecondDisease = testResultForSecondDisease;
	}
}
