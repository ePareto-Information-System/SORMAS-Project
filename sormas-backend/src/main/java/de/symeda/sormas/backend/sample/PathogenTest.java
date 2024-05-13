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

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;
import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.backend.common.DeletableAdo;
import de.symeda.sormas.backend.disease.DiseaseVariantConverter;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.user.User;
import org.apache.commons.lang3.StringUtils;

@Entity
public class PathogenTest extends DeletableAdo {

	private static final long serialVersionUID = 2290351143518627813L;

	public static final String TABLE_NAME = "pathogentest";

	public static final String SAMPLE = "sample";
	public static final String TESTED_DISEASE = "testedDisease";
	public static final String TESTED_DISEASE_VARIANT = "testedDiseaseVariant";
	public static final String TESTED_DISEASE_VARIANT_DETAILS = "testedDiseaseVariantDetails";
	public static final String TYPING_ID = "typingId";
	public static final String TEST_TYPE = "testType";
	public static final String PCR_TEST_SPECIFICATION = "pcrTestSpecification";
	public static final String TEST_TYPE_TEXT = "testTypeText";
	public static final String TEST_DATE_TIME = "testDateTime";
	public static final String LAB = "lab";
	public static final String LAB_DETAILS = "labDetails";
	public static final String LAB_USER = "labUser";
	public static final String TEST_RESULT = "testResult";
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
	public static final String PRESCRIBER_PHYSICIAN_CODE = "prescriberPhysicianCode";
	public static final String PRESCRIBER_FIRST_NAME = "prescriberFirstName";
	public static final String PRESCRIBER_LAST_NAME = "prescriberLastName";
	public static final String PRESCRIBER_PHONE_NUMBER = "prescriberPhoneNumber";
	public static final String PRESCRIBER_ADDRESS = "prescriberAddress";
	public static final String PRESCRIBER_POSTAL_CODE = "prescriberPostalCode";
	public static final String PRESCRIBER_CITY = "prescriberCity";
	public static final String PRESCRIBER_COUNTRY = "prescriberCountry";

	private Sample sample;
	private Disease testedDisease;
	@Convert(converter = DiseaseVariantConverter.class)
	private DiseaseVariant testedDiseaseVariant;
	private String testedDiseaseDetails;
	private String testedDiseaseVariantDetails;
	private String typingId;
	private PathogenTestType testType;
	private PCRTestSpecification pcrTestSpecification;
	private String testTypeText;
	private Date testDateTime;
	private Facility lab;
	private String labDetails;
	private User labUser;
	private PathogenTestResultType testResult;
	private String testResultText;
	private Boolean testResultVerified;
	private boolean fourFoldIncreaseAntibodyTiter;
	private String serotype;
	private Float cqValue;
	private Float ctValueE;
	private Float ctValueN;
	private Float ctValueRdrp;
	private Float ctValueS;
	private Float ctValueOrf1;
	private Float ctValueRdrpS;
	private Date reportDate;
	private boolean viaLims;
	private String externalId;
	private String externalOrderId;
	private Boolean preliminary;
	private String prescriberPhysicianCode;
	private String prescriberFirstName;
	private String prescriberLastName;
	private String prescriberPhoneNumber;
	private String prescriberAddress;
	private String prescriberPostalCode;
	private String prescriberCity;
	private Country prescriberCountry;
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
	private PathogenTestResultVariant testResultVariant;
	private String variantOtherSpecify;
	private Disease secondTestedDisease;
	private PathogenTestResultType testResultForSecondDisease;
	private Set<PathogenTestType> sampleTests;
	private PosNeg sampleTestResultPCR;
	private Date sampleTestResultPCRDate;
	private PosNeg sampleTestResultAntigen;
	private Date sampleTestResultAntigenDate;
	private PosNeg sampleTestResultIGM;
	private Date sampleTestResultIGMDate;
	private PosNeg sampleTestResultIGG;
	private Date sampleTestResultIGGDate;
	private PosNeg sampleTestResultImmuno;
	private Date sampleTestResultImmunoDate;
	private String sampleTestsString;

	private String virusDetectionGenotype;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	@Enumerated(EnumType.STRING)
	public Disease getTestedDisease() {
		return testedDisease;
	}

	public void setTestedDisease(Disease testedDisease) {
		this.testedDisease = testedDisease;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestedDiseaseDetails() {
		return testedDiseaseDetails;
	}

	public void setTestedDiseaseDetails(String testedDiseaseDetails) {
		this.testedDiseaseDetails = testedDiseaseDetails;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestedDiseaseVariantDetails() {
		return testedDiseaseVariantDetails;
	}

	public void setTestedDiseaseVariantDetails(String testedDiseaseVariantDetails) {
		this.testedDiseaseVariantDetails = testedDiseaseVariantDetails;
	}

	@Column
	@Convert(converter = DiseaseVariantConverter.class)
	public DiseaseVariant getTestedDiseaseVariant() {
		return testedDiseaseVariant;
	}

	public void setTestedDiseaseVariant(DiseaseVariant diseaseVariant) {
		this.testedDiseaseVariant = diseaseVariant;
	}

	@Column
	public String getTypingId() {
		return typingId;
	}

	public void setTypingId(String typingId) {
		this.typingId = typingId;
	}

	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
	public PathogenTestType getTestType() {
		return testType;
	}

	public void setTestType(PathogenTestType testType) {
		this.testType = testType;
	}

	@Enumerated(EnumType.STRING)
	public PCRTestSpecification getPcrTestSpecification() {
		return pcrTestSpecification;
	}

	public void setPcrTestSpecification(PCRTestSpecification pcrTestSpecification) {
		this.pcrTestSpecification = pcrTestSpecification;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTestTypeText() {
		return testTypeText;
	}

	public void setTestTypeText(String testTypeText) {
		this.testTypeText = testTypeText;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getTestDateTime() {
		return testDateTime;
	}

	public void setTestDateTime(Date testDateTime) {
		this.testDateTime = testDateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	public Facility getLab() {
		return lab;
	}

	public void setLab(Facility lab) {
		this.lab = lab;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getLabDetails() {
		return labDetails;
	}

	public void setLabDetails(String labDetails) {
		this.labDetails = labDetails;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	public User getLabUser() {
		return labUser;
	}

	public void setLabUser(User labUser) {
		this.labUser = labUser;
	}

	@Enumerated(EnumType.STRING)
//	@JoinColumn(nullable = false)
	public PathogenTestResultType getTestResult() {
		return testResult;
	}

	public void setTestResult(PathogenTestResultType testResult) {
		this.testResult = testResult;
	}

	@Column(length = CHARACTER_LIMIT_BIG)
	public String getTestResultText() {
		return testResultText;
	}

	public void setTestResultText(String testResultText) {
		this.testResultText = testResultText;
	}

	@Column(nullable = false)
	public Boolean getTestResultVerified() {
		return testResultVerified;
	}

	public void setTestResultVerified(Boolean testResultVerified) {
		this.testResultVerified = testResultVerified;
	}

	@Column
	public boolean isFourFoldIncreaseAntibodyTiter() {
		return fourFoldIncreaseAntibodyTiter;
	}

	public void setFourFoldIncreaseAntibodyTiter(boolean fourFoldIncreaseAntibodyTiter) {
		this.fourFoldIncreaseAntibodyTiter = fourFoldIncreaseAntibodyTiter;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getSerotype() {
		return serotype;
	}

	public void setSerotype(String serotype) {
		this.serotype = serotype;
	}

	@Column
	public Float getCqValue() {
		return cqValue;
	}

	public void setCqValue(Float cqValue) {
		this.cqValue = cqValue;
	}

	@Column
	public Float getCtValueE() {
		return ctValueE;
	}

	public void setCtValueE(Float ctValueE) {
		this.ctValueE = ctValueE;
	}

	@Column
	public Float getCtValueN() {
		return ctValueN;
	}

	public void setCtValueN(Float ctValueN) {
		this.ctValueN = ctValueN;
	}

	@Column
	public Float getCtValueRdrp() {
		return ctValueRdrp;
	}

	public void setCtValueRdrp(Float ctValueRdrp) {
		this.ctValueRdrp = ctValueRdrp;
	}

	@Column
	public Float getCtValueS() {
		return ctValueS;
	}

	public void setCtValueS(Float ctValueS) {
		this.ctValueS = ctValueS;
	}

	@Column
	public Float getCtValueOrf1() {
		return ctValueOrf1;
	}

	public void setCtValueOrf1(Float ctValueOrf1) {
		this.ctValueOrf1 = ctValueOrf1;
	}

	@Column
	public Float getCtValueRdrpS() {
		return ctValueRdrpS;
	}

	public void setCtValueRdrpS(Float ctValueRdrpS) {
		this.ctValueRdrpS = ctValueRdrpS;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	@Column
	public boolean isViaLims() {
		return viaLims;
	}

	public void setViaLims(boolean viaLims) {
		this.viaLims = viaLims;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getExternalOrderId() {
		return externalOrderId;
	}

	public void setExternalOrderId(String externalOrderId) {
		this.externalOrderId = externalOrderId;
	}

	@Column
	public Boolean getPreliminary() {
		return preliminary;
	}

	public void setPreliminary(Boolean preliminary) {
		this.preliminary = preliminary;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberPhysicianCode() {
		return prescriberPhysicianCode;
	}

	public void setPrescriberPhysicianCode(String prescriberPhysicianCode) {
		this.prescriberPhysicianCode = prescriberPhysicianCode;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberFirstName() {
		return prescriberFirstName;
	}

	public void setPrescriberFirstName(String prescriberFirstName) {
		this.prescriberFirstName = prescriberFirstName;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberLastName() {
		return prescriberLastName;
	}

	public void setPrescriberLastName(String prescriberLastName) {
		this.prescriberLastName = prescriberLastName;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberPhoneNumber() {
		return prescriberPhoneNumber;
	}

	public void setPrescriberPhoneNumber(String prescriberPhoneNumber) {
		this.prescriberPhoneNumber = prescriberPhoneNumber;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberAddress() {
		return prescriberAddress;
	}

	public void setPrescriberAddress(String prescriberAddress) {
		this.prescriberAddress = prescriberAddress;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberPostalCode() {
		return prescriberPostalCode;
	}

	public void setPrescriberPostalCode(String prescriberPostalCode) {
		this.prescriberPostalCode = prescriberPostalCode;
	}

	@Column(columnDefinition = "text")
	public String getPrescriberCity() {
		return prescriberCity;
	}

	public void setPrescriberCity(String prescriberCity) {
		this.prescriberCity = prescriberCity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Country getPrescriberCountry() {
		return prescriberCountry;
	}

	public void setPrescriberCountry(Country prescriberCountry) {
		this.prescriberCountry = prescriberCountry;
	}

	public String getVirusDetectionGenotype() {
		return virusDetectionGenotype;
	}

	public void setVirusDetectionGenotype(String virusDetectionGenotype) {
		this.virusDetectionGenotype = virusDetectionGenotype;
	}

	public PathogenTestReferenceDto toReference() {
		return new PathogenTestReferenceDto(getUuid());
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateLabResultsSentDistrict() {
		return dateLabResultsSentDistrict;
	}

	public void setDateLabResultsSentDistrict(Date dateLabResultsSentDistrict) {
		this.dateLabResultsSentDistrict = dateLabResultsSentDistrict;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateLabResultsSentClinician() {
		return dateLabResultsSentClinician;
	}

	public void setDateLabResultsSentClinician(Date dateLabResultsSentClinician) {
		this.dateLabResultsSentClinician = dateLabResultsSentClinician;
	}
	@Temporal(TemporalType.TIMESTAMP)
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

	@Transient
	public Set<PathogenTestType> getSampleTests() {
		if(sampleTests == null){
			if (StringUtils.isEmpty(sampleTestsString)) {
				sampleTests = new HashSet<>();
			}else{
				sampleTests = Arrays.stream(sampleTestsString.split(",")).map(PathogenTestType::valueOf).collect(Collectors.toSet());
				}
			}
		return sampleTests;
	}

	public void setSampleTests(Set<PathogenTestType> sampleTests) {
		this.sampleTests = sampleTests;

		if(this.sampleTests == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		sampleTests.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		sampleTestsString = sb.toString();

	}

	public String getSampleTestsString() {
		return sampleTestsString;
	}

	public void setSampleTestsString(String sampleTestsString) {
		this.sampleTestsString = sampleTestsString;
		sampleTests = null;
	}
	public PosNeg getSampleTestResultPCR() {
		return sampleTestResultPCR;
	}

	public void setSampleTestResultPCR(PosNeg sampleTestResultPCR) {
		this.sampleTestResultPCR = sampleTestResultPCR;
	}

	public Date getSampleTestResultPCRDate() {
		return sampleTestResultPCRDate;
	}

	public void setSampleTestResultPCRDate(Date sampleTestResultPCRDate) {
		this.sampleTestResultPCRDate = sampleTestResultPCRDate;
	}

	public PosNeg getSampleTestResultAntigen() {
		return sampleTestResultAntigen;
	}

	public void setSampleTestResultAntigen(PosNeg sampleTestResultAntigen) {
		this.sampleTestResultAntigen = sampleTestResultAntigen;
	}

	public Date getSampleTestResultAntigenDate() {
		return sampleTestResultAntigenDate;
	}

	public void setSampleTestResultAntigenDate(Date sampleTestResultAntigenDate) {
		this.sampleTestResultAntigenDate = sampleTestResultAntigenDate;
	}

	public PosNeg getSampleTestResultIGM() {
		return sampleTestResultIGM;
	}

	public void setSampleTestResultIGM(PosNeg sampleTestResultIGM) {
		this.sampleTestResultIGM = sampleTestResultIGM;
	}

	public Date getSampleTestResultIGMDate() {
		return sampleTestResultIGMDate;
	}

	public void setSampleTestResultIGMDate(Date sampleTestResultIGMDate) {
		this.sampleTestResultIGMDate = sampleTestResultIGMDate;
	}

	public PosNeg getSampleTestResultIGG() {
		return sampleTestResultIGG;
	}

	public void setSampleTestResultIGG(PosNeg sampleTestResultIGG) {
		this.sampleTestResultIGG = sampleTestResultIGG;
	}

	public Date getSampleTestResultIGGDate() {
		return sampleTestResultIGGDate;
	}

	public void setSampleTestResultIGGDate(Date sampleTestResultIGGDate) {
		this.sampleTestResultIGGDate = sampleTestResultIGGDate;
	}

	public PosNeg getSampleTestResultImmuno() {
		return sampleTestResultImmuno;
	}

	public void setSampleTestResultImmuno(PosNeg sampleTestResultImmuno) {
		this.sampleTestResultImmuno = sampleTestResultImmuno;
	}

	public Date getSampleTestResultImmunoDate() {
		return sampleTestResultImmunoDate;
	}

	public void setSampleTestResultImmunoDate(Date sampleTestResultImmunoDate) {
		this.sampleTestResultImmunoDate = sampleTestResultImmunoDate;
	}
}
