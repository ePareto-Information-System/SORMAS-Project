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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;
import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.DeletableAdo;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.externalmessage.labmessage.SampleReport;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.sormastosormas.entities.SormasToSormasShareable;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfo;
import de.symeda.sormas.backend.user.User;
import org.bouncycastle.asn1.x509.Time;

@Entity(name = "samples")
public class Sample extends DeletableAdo implements SormasToSormasShareable {

	private static final long serialVersionUID = -7196712070188634978L;

	public static final String TABLE_NAME = "samples";

	public static final String ASSOCIATED_CASE = "associatedCase";
	public static final String ASSOCIATED_CONTACT = "associatedContact";
	public static final String ASSOCIATED_EVENT_PARTICIPANT = "associatedEventParticipant";
	public static final String LAB_SAMPLE_ID = "labSampleID";
	public static final String FIELD_SAMPLE_ID = "fieldSampleID";
	public static final String SAMPLE_DATE_TIME = "sampleDateTime";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String REPORTING_USER = "reportingUser";
	public static final String SAMPLE_MATERIAL = "sampleMaterial";
	public static final String SAMPLE_PURPOSE = "samplePurpose";
	public static final String SAMPLE_MATERIAL_TEXT = "sampleMaterialText";
	public static final String LAB = "lab";
	public static final String LAB_DETAILS = "labDetails";
	public static final String SHIPMENT_DATE = "shipmentDate";
	public static final String SHIPMENT_DETAILS = "shipmentDetails";
	public static final String RECEIVED_DATE = "receivedDate";
	public static final String NO_TEST_POSSIBLE_REASON = "noTestPossibleReason";
	public static final String COMMENT = "comment";
	public static final String SAMPLE_SOURCE = "sampleSource";
	public static final String REFERRED_TO = "referredTo";
	public static final String SHIPPED = "shipped";
	public static final String RECEIVED = "received";
	public static final String SPECIMEN_CONDITION = "specimenCondition";
	public static final String PATHOGEN_TESTING_REQUESTED = "pathogenTestingRequested";
	public static final String SAMPLE_MATERIAL_REQUESTED = "sampleMaterialTestingRequested";
	public static final String ADDITIONAL_TESTING_REQUESTED = "additionalTestingRequested";
	public static final String ADDITIONAL_TESTS = "additionalTests";
	public static final String PATHOGEN_TEST_RESULT = "pathogenTestResult";
	public static final String PATHOGEN_TEST_RESULT_CHANGE_DATE = "pathogenTestResultChangeDate";
	public static final String REQUESTED_PATHOGEN_TESTS_STRING = "requestedPathogenTestsString";
	public static final String REQUESTED_SAMPLE_MATERIALS_STRING = "requestedSampleMaterialsString";
	public static final String REQUESTED_ADDITIONAL_TESTS_STRING = "requestedAdditionalTestsString";
	public static final String REQUESTED_OTHER_PATHOGEN_TESTS = "requestedOtherPathogenTests";
	public static final String REQUESTED_OTHER_ADDITIONAL_TESTS = "requestedOtherAdditionalTests";
	public static final String PATHOGENTESTS = "pathogenTests";
	public static final String SAMPLING_REASON = "samplingReason";
	public static final String SAMPLING_REASON_DETAILS = "samplingReasonDetails";
	public static final String SORMAS_TO_SORMAS_ORIGIN_INFO = "sormasToSormasOriginInfo";
	public static final String SORMAS_TO_SORMAS_SHARES = "sormasToSormasShares";
	public static final String PATHOGEN_TEST_COUNT = "pathogenTestCount";
	public static final String SAMPLE_TESTS = "sampleTests";
	public static final String SAMPLE_DISPATCH_MODE = "sampleDispatchMode";
	public static final String SAMPLE_DISPATCH_DATE = "sampleDispatchDate";
	public static final String REQUESTED_ADDITIONAL_TESTS = "requestedAdditionalTests";
	public static final String DELETION_REASON = "deletionReason";
	public static final String OTHER_DELETION_REASON = "otherDeletionReason";
	public static final String IPSAMPLESENT = "ipSampleSent";
	public static final String IPSAMPLERESULTS = "ipSampleResults";
//	public static final String SAMPLE_DISEASE_TESTS = "sampleDiseaseTests";
	public static final String DISEASE = "disease";
	public static final String CSF_SAMPLE_COLLECTED = "csfSampleCollected";
	public static final String CSF_REASON = "csfReason";
	public static final String APPEARANCE_OF_CSF = "appearanceOfCsf";
	public static final String INOCULATION_TIME_TRANSPORT_MEDIA = "inoculationTimeTransportMedia";
	public static final String SAMPLE_SENT_TO_LAB = "sampleSentToLab";
	public static final String DATE_SAMPLE_SENT_TO_LAB = "dateSampleSentToLab";
	public static final String SAMPLE_CONTAINER_USED = "sampleContainerUsed";
	public static final String RDT_PERFORMED = "rdtPerformed";
	public static final String RDT_RESULTS = "rdtResults";
	public static final String DISTRICT_NOTIFICATION_DATE = "districtNotificationDate";
	public static final String NAME_OF_PERSON = "nameOfPerson";
	public static final String TEL_NUMBER = "telNumber";
	public static final String DATE_FORM_SENT_TO_DISTRICT = "dateFormSentToDistrict";
	public static final String DATE_FORM_RECEIVED_AT_DISTRICT = "dateFormReceivedAtDistrict";
	public static final String DATE_FORM_SENT_TO_REGION = "dateFormSentToRegion";
	public static final String DATE_FORM_RECEIVED_AT_REGION = "dateFormReceivedAtRegion";
	public static final String DATE_FORM_SENT_TO_NATIONAL = "dateFormSentToNational";
	public static final String DATE_FORM_RECEIVED_AT_NATIONAL = "dateFormReceivedAtNational";
	public static final String REASON_NOT_SENT_TO_LAB = "reasonNotSentToLab";

	public static final String LABORATORY_NAME = "laboratoryName";
	public static final String LABORATORY_SAMPLE_DATE_RECEIVED = "laboratorySampleDateReceived";
	public static final String LABORATORY_NUMBER = "laboratoryNumber";
	public static final String LABORATORY_SAMPLE_CONTAINER_RECEIVED = "laboratorySampleContainerReceived";
	public static final String LABORATORY_SAMPLE_CONTAINER_OTHER = "laboratorySampleContainerOther";
	public static final String LABORATORY_SAMPLE_CONDITION = "laboratorySampleCondition";
	public static final String LABORATORY_APPEARANCE_OF_CSF = "laboratoryAppearanceOfCSF";
	public static final String LABORATORY_TEST_PERFORMED = "laboratoryTestPerformed";
	public static final String LABORATORY_TEST_PERFORMED_OTHER = "laboratoryTestPerformedOther";
	public static final String LABORATORY_CYTOLOGY = "laboratoryCytology";
	public static final String LABORATORY_GRAM = "laboratoryGram";
	public static final String LABORATORY_GRAM_OTHER = "laboratoryGramOther";
	public static final String LABORATORY_RDT_PERFORMED = "laboratoryRdtPerformed";
	public static final String LABORATORY_RDT_RESULTS = "laboratoryRdtResults";
	public static final String LABORATORY_LATEX = "laboratoryLatex";
	public static final String LABORATORY_CULTURE = "laboratoryCulture";
	public static final String LABORATORY_CULTURE_OTHER = "laboratoryCultureOther";
	public static final String LABORATORY_OTHER_TESTS = "laboratoryOtherTests";
	public static final String LABORATORY_OTHER_TESTS_RESULTS = "laboratoryOtherTestsResults";
	public static final String LABORATORY_CEFTRIAXONE = "laboratoryCeftriaxone";
	public static final String LABORATORY_PENICILLIN_G = "laboratoryPenicillinG";
	public static final String LABORATORY_AMOXYCILLIN = "laboratoryAmoxycillin";
	public static final String LABORATORY_OXACILLIN = "laboratoryOxacillin";
	public static final String LABORATORY_ANTIBIOGRAM_OTHER = "laboratoryAntibiogramOther";
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
	public static final String LABORATORY_TYPE = "laboratoryType";

	private Case associatedCase;
	private Contact associatedContact;
	private EventParticipant associatedEventParticipant;
	private String labSampleID;
	private String fieldSampleID;
	private Date sampleDateTime;

	private Date reportDateTime;
	private User reportingUser;
	private Double reportLat;
	private Double reportLon;
	private Float reportLatLonAccuracy;

	private SampleMaterial sampleMaterial;
	private SamplePurpose samplePurpose;
	private String sampleMaterialText;
	private Facility lab;
	private String labDetails;
	private Date shipmentDate;
	private String shipmentDetails;
	private Date receivedDate;
	private SpecimenCondition specimenCondition;
	private String noTestPossibleReason;
	private String comment;
	private SampleSource sampleSource;
	private Sample referredTo;
	private boolean shipped;
/*	private boolean sampleMaterialTypeForYF;
	private boolean sampleDiseaseTests;*/
	private boolean received;
	private PathogenTestResultType pathogenTestResult;
	private Date pathogenTestResultChangeDate;

	private Boolean pathogenTestingRequested;
	private Boolean sampleMaterialTestingRequested;
	private Boolean additionalTestingRequested;
	private Set<PathogenTestType> requestedPathogenTests;
	private Set<SampleMaterial> requestedSampleMaterials;
//	private Set<PathogenTestType> sampleTests;
	private PathogenTestType sampleTests;
	private Set<AdditionalTestType> requestedAdditionalTests;
	private String requestedOtherPathogenTests;
	private String requestedOtherAdditionalTests;
	private String requestedPathogenTestsString;
	private String requestedSampleMaterialsString;
	private String sampleTestsString;
	private String requestedAdditionalTestsString;
	private SamplingReason samplingReason;
	private String samplingReasonDetails;
	private List<SampleReport> sampleReports = new ArrayList<>(0);
	private List<PathogenTest> pathogenTests;
	private List<AdditionalTest> additionalTests;

	private SormasToSormasOriginInfo sormasToSormasOriginInfo;
	private List<SormasToSormasShareInfo> sormasToSormasShares = new ArrayList<>(0);
	private YesNo ipSampleSent;
	private IpResult ipSampleResults;
	private Disease disease;
	private SampleDispatchMode sampleDispatchMode;
	private Date sampleDispatchDate;

	private YesNo csfSampleCollected;
	private YesNo rdtPerformed;
	private YesNo sampleSentToLab;

	private CsfReason csfReason;
	private CsfAppearance appearanceOfCsf;
	private SampleContainerUsed sampleContainerUsed;

	private String rdtResults;
	private String reasonNotSentToLab;
	private String nameOfPerson;
	private String telNumber;

	private Date inoculationTimeTransportMedia;
	private Date districtNotificationDate;
	private Date dateSampleSentToLab;
	private Date dateFormSentToDistrict;
	private Date dateFormReceivedAtDistrict;
	private Date dateFormSentToRegion;
	private Date dateFormReceivedAtRegion;
	private Date dateFormSentToNational;
	private Date dateFormReceivedAtNational;
	private String laboratoryName;
	private String laboratoryNumber;
	private String laboratorySerotype;
	private String laboratorySerotypeType;
	private String laboratorySerotypeResults;
	private String laboratoryFinalResults;
	private String laboratoryObservations;
	private String laboratorySampleContainerOther;
	private String laboratoryTestPerformedOther;
	private String laboratoryCytology;
	private String laboratoryGramOther;
	private String laboratoryCultureOther;
	private String laboratoryOtherTests;
	private String laboratoryOtherTestsResults;
	private String laboratoryRdtResults;
	private CaseClassification laboratoryFinalClassification;
	private String laboratoryPcrType;
	private SampleContainerUsed laboratorySampleContainerReceived;
	private SpecimenCondition laboratorySampleCondition;
	private CsfAppearance laboratoryAppearanceOfCSF;
	private LabTest laboratoryTestPerformed;
	private Gram laboratoryGram;
	private YesNo laboratoryRdtPerformed;
	private LatexCulture laboratoryLatex;
	private LatexCulture laboratoryCulture;
	private LatexCulture laboratoryPcrOptions;
	private Antibiogram laboratoryCeftriaxone;
	private Antibiogram laboratoryPenicillinG;
	private Antibiogram laboratoryAmoxycillin;
	private Antibiogram laboratoryOxacillin;
	private Antibiogram laboratoryAntibiogramOther;
	private LabType laboratoryType;
	private Date laboratoryDateResultsSentHealthFacility;
	private Date laboratoryDateResultsSentDSD;
	private Date laboratorySampleDateReceived;
	private Date laboratoryDatePcrPerformed;

	private Date dateSentToNationalRegLab;
	private Date dateDifferentiationSentToEpi;
	private Date dateDifferentiationReceivedFromEpi;
	private Date dateIsolateSentForSequencing;
	private Date dateSeqResultsSentToProgram;

	private PosNeg finalLabResults;
	private YesNoUnknown immunocompromisedStatusSuspected;
	private AFPClassification afpFinalClassification;
	private Date dateFirstSpecimen;
	private Date dateSecondSpecimen;
	private Date dateSpecimenSentNationalLevel;
	private Date dateSpecimenReceivedNationalLevel;
	private Date dateSpecimenSentInter;
	private Date dateSpecimenReceivedInter;
	private SpecimenCondition statusSpecimenReceptionAtLab;
	private Date dateCombinedCellCultureResults;
	private YesNo w1;
	private YesNo w2;
	private YesNo w3;
	private YesNoUnknown discordant;
	private YesNo sL1;
	private YesNo sL2;
	private YesNo sL3;
	private Date dateFollowUpExam;
	private InjectionSite residualAnalysis;
	private AfpResult resultExam;
	private YesNo positiveViralCulture;
	private YesNo positiveRealTime;
	private YesNo fourFoldRise;
	private InfluenzaVirus influenzaVirus;
	private String otherInfluenzaVirus;
	private String treatment;
	private String stateTreatmentAdministered;
	private Disease suspectedDisease;
	private String labLocation;
	private Date dateLabReceivedSpecimen;
	private Date dateResultsSentToClinician;
	private Date dateSpecimenSentToLab;
	private Long pathogenTestCount;
	private String containerOther;
	private YesNo hasSampleBeenCollected;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	public Case getAssociatedCase() {
		return associatedCase;
	}

	public void setAssociatedCase(Case associatedCase) {
		this.associatedCase = associatedCase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	public Contact getAssociatedContact() {
		return associatedContact;
	}

	public void setAssociatedContact(Contact associatedContact) {
		this.associatedContact = associatedContact;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	public EventParticipant getAssociatedEventParticipant() {
		return associatedEventParticipant;
	}

	public void setAssociatedEventParticipant(EventParticipant associatedEventParticipant) {
		this.associatedEventParticipant = associatedEventParticipant;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getLabSampleID() {
		return labSampleID;
	}

	public void setLabSampleID(String labSampleID) {
		this.labSampleID = labSampleID;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getFieldSampleID() {
		return fieldSampleID;
	}

	public void setFieldSampleID(String fieldSampleID) {
		this.fieldSampleID = fieldSampleID;
	}

	@Temporal(TemporalType.TIMESTAMP)
//	@Column(nullable = false)
	public Date getSampleDateTime() {
		return sampleDateTime;
	}

	public void setSampleDateTime(Date sampleDateTime) {
		this.sampleDateTime = sampleDateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	@Enumerated(EnumType.STRING)
	public SampleMaterial getSampleMaterial() {
		return sampleMaterial;
	}

	public void setSampleMaterial(SampleMaterial sampleMaterial) {
		this.sampleMaterial = sampleMaterial;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getSampleMaterialText() {
		return sampleMaterialText;
	}

	public void setSampleMaterialText(String sampleMaterialText) {
		this.sampleMaterialText = sampleMaterialText;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public SamplePurpose getSamplePurpose() {
		return samplePurpose;
	}

	public void setSamplePurpose(SamplePurpose samplePurpose) {
		this.samplePurpose = samplePurpose;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getShipmentDetails() {
		return shipmentDetails;
	}

	public void setShipmentDetails(String shipmentDetails) {
		this.shipmentDetails = shipmentDetails;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Enumerated(EnumType.STRING)
	public SpecimenCondition getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(SpecimenCondition specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getNoTestPossibleReason() {
		return noTestPossibleReason;
	}

	public void setNoTestPossibleReason(String noTestPossibleReason) {
		this.noTestPossibleReason = noTestPossibleReason;
	}

	@OneToMany(mappedBy = PathogenTest.SAMPLE, fetch = FetchType.LAZY)
	public List<PathogenTest> getPathogenTests() {
		return pathogenTests;
	}

	public void setPathogenTests(List<PathogenTest> pathogenTests) {
		this.pathogenTests = pathogenTests;
	}

	@OneToMany(mappedBy = AdditionalTest.SAMPLE, fetch = FetchType.LAZY)
	public List<AdditionalTest> getAdditionalTests() {
		return additionalTests;
	}

	public void setAdditionalTests(List<AdditionalTest> additionalTests) {
		this.additionalTests = additionalTests;
	}

	@Column(length = CHARACTER_LIMIT_BIG)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Enumerated(EnumType.STRING)
	public SampleSource getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(SampleSource sampleSource) {
		this.sampleSource = sampleSource;
	}

	@OneToOne(cascade = {})
	@JoinColumn(nullable = true)
	public Sample getReferredTo() {
		return referredTo;
	}

	public void setReferredTo(Sample referredTo) {
		this.referredTo = referredTo;
	}

	@Column
	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

/*	public boolean isYellowFeverSampleType() {
		return sampleMaterialTypeForYF;
	}

	public void setYellowFeverSampleType(boolean sampleMaterialTypeForYF) {
		this.sampleMaterialTypeForYF = sampleMaterialTypeForYF;
	}

	public boolean isDiseaseSampleTests() {
		return sampleDiseaseTests;
	}

	public void setDiseaseSampleTests(boolean sampleDiseaseTests) {
		this.sampleDiseaseTests = sampleDiseaseTests;
	}*/

	@Column
	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	@Enumerated(EnumType.STRING)
	public PathogenTestResultType getPathogenTestResult() {
		return pathogenTestResult;
	}

	public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {
		this.pathogenTestResult = pathogenTestResult;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPathogenTestResultChangeDate() {
		return pathogenTestResultChangeDate;
	}

	public void setPathogenTestResultChangeDate(Date pathogenTestResultChangeDate) {
		this.pathogenTestResultChangeDate = pathogenTestResultChangeDate;
	}

	@Column
	public Boolean getPathogenTestingRequested() {
		return pathogenTestingRequested;
	}

	public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {
		this.pathogenTestingRequested = pathogenTestingRequested;
	}

	@Column
	public Boolean getSampleMaterialTestingRequested() {
		return sampleMaterialTestingRequested;
	}

	public void setSampleMaterialTestingRequested(Boolean sampleMaterialTestingRequested) {
		this.sampleMaterialTestingRequested = sampleMaterialTestingRequested;
	}

	@Column
	public Boolean getAdditionalTestingRequested() {
		return additionalTestingRequested;
	}

	public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {
		this.additionalTestingRequested = additionalTestingRequested;
	}

	@Transient
	public Set<PathogenTestType> getRequestedPathogenTests() {
		if (requestedPathogenTests == null) {
			if (StringUtils.isEmpty(requestedPathogenTestsString)) {
				requestedPathogenTests = new HashSet<>();
			} else {
				requestedPathogenTests =
					Arrays.stream(requestedPathogenTestsString.split(",")).map(PathogenTestType::valueOf).collect(Collectors.toSet());
			}
		}
		return requestedPathogenTests;
	}

	public void setRequestedPathogenTests(Set<PathogenTestType> requestedPathogenTests) {
		this.requestedPathogenTests = requestedPathogenTests;

		if (this.requestedPathogenTests == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		requestedPathogenTests.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedPathogenTestsString = sb.toString();
	}

	@Transient
	public Set<SampleMaterial> getRequestedSampleMaterials() {
		if (requestedSampleMaterials == null) {
			if (StringUtils.isEmpty(requestedSampleMaterialsString)) {
				requestedSampleMaterials = new HashSet<>();
			} else {
				requestedSampleMaterials =
						Arrays.stream(requestedSampleMaterialsString.split(",")).map(SampleMaterial::valueOf).collect(Collectors.toSet());
			}
		}
		return requestedSampleMaterials;
	}

	public void setRequestedSampleMaterials(Set<SampleMaterial> requestedSampleMaterials) {
		this.requestedSampleMaterials = requestedSampleMaterials;

		if (this.requestedSampleMaterials == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		requestedSampleMaterials.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedSampleMaterialsString = sb.toString();
	}

	/*@Transient
	public Set<PathogenTestType> getSampleTests() {
		if (sampleTests == null) {
			if (StringUtils.isEmpty(sampleTestsString)) {
				sampleTests = new HashSet<>();
			} else {
				sampleTests =
						Arrays.stream(sampleTestsString.split(",")).map(PathogenTestType::valueOf).collect(Collectors.toSet());
			}
		}
		return sampleTests;
	}

	public void setSampleTests(Set<PathogenTestType> sampleTests) {
		this.sampleTests = sampleTests;

		if (this.sampleTests == null) {
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
	}*/

	@Transient
	public Set<AdditionalTestType> getRequestedAdditionalTests() {
		if (requestedAdditionalTests == null) {
			if (StringUtils.isEmpty(requestedAdditionalTestsString)) {
				requestedAdditionalTests = new HashSet<>();
			} else {
				requestedAdditionalTests =
					Arrays.stream(requestedAdditionalTestsString.split(",")).map(AdditionalTestType::valueOf).collect(Collectors.toSet());
			}
		}
		return requestedAdditionalTests;
	}

	public void setRequestedAdditionalTests(Set<AdditionalTestType> requestedAdditionalTests) {
		this.requestedAdditionalTests = requestedAdditionalTests;

		if (this.requestedAdditionalTests == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		requestedAdditionalTests.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedAdditionalTestsString = sb.toString();
	}

	public String getRequestedPathogenTestsString() {
		return requestedPathogenTestsString;
	}

	public void setRequestedPathogenTestsString(String requestedPathogenTestsString) {
		this.requestedPathogenTestsString = requestedPathogenTestsString;
		requestedPathogenTests = null;
	}

	public String getRequestedSampleMaterialsString() {
		return requestedSampleMaterialsString;
	}

	public void setRequestedSampleMaterialsString(String requestedSampleMaterialsString) {
		this.requestedSampleMaterialsString = requestedSampleMaterialsString;
		requestedSampleMaterials = null;
	}

	public String getRequestedAdditionalTestsString() {
		return requestedAdditionalTestsString;
	}

	public void setRequestedAdditionalTestsString(String requestedAdditionalTestsString) {
		this.requestedAdditionalTestsString = requestedAdditionalTestsString;
		requestedAdditionalTests = null;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getRequestedOtherPathogenTests() {
		return requestedOtherPathogenTests;
	}

	public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {
		this.requestedOtherPathogenTests = requestedOtherPathogenTests;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getRequestedOtherAdditionalTests() {
		return requestedOtherAdditionalTests;
	}

	public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {
		this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
	}

	@Enumerated(EnumType.STRING)
	public PathogenTestType getSampleTests() {
		return sampleTests;
	}

	public void setSampleTests(PathogenTestType sampleTests) {
		this.sampleTests = sampleTests;
	}
	@Enumerated(EnumType.STRING)
	public SamplingReason getSamplingReason() {
		return samplingReason;
	}

	public void setSamplingReason(SamplingReason samplingReason) {
		this.samplingReason = samplingReason;
	}

	@Column(columnDefinition = "text")
	public String getSamplingReasonDetails() {
		return samplingReasonDetails;
	}

	public void setSamplingReasonDetails(String samplingReasonDetails) {
		this.samplingReasonDetails = samplingReasonDetails;
	}
	public void setIpSampleSent(YesNo ipSampleSent) {
		this.ipSampleSent = ipSampleSent;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getIpSampleSent() {
		return ipSampleSent;
	}
	@Column
	public Date getSampleDispatchDate() {
		return sampleDispatchDate;
	}

	public void setSampleDispatchDate(Date sampleDispatchDate) {
		this.sampleDispatchDate = sampleDispatchDate;
	}

	public void setIpSampleResults(IpResult ipSampleResults) {
		this.ipSampleResults = ipSampleResults;
	}
	@Enumerated(EnumType.STRING)
	public IpResult getIpSampleResults(){
		return ipSampleResults;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	@Enumerated(EnumType.STRING)
	public Disease getDisease(){
		return disease;
	}

	@Enumerated(EnumType.STRING)
	public SampleDispatchMode getSampleDispatchMode() {
		return sampleDispatchMode;
	}

	public void setSampleDispatchMode(SampleDispatchMode sampleDispatchMode) {
		this.sampleDispatchMode = sampleDispatchMode;
	}

	public SampleReferenceDto toReference() {
		return new SampleReferenceDto(
			getUuid(),
			getSampleMaterial(),
			getAssociatedCase() != null ? getAssociatedCase().getUuid() : null,
			getAssociatedContact() != null ? getAssociatedContact().getUuid() : null,
			getAssociatedEventParticipant() != null ? getAssociatedEventParticipant().getUuid() : null);
	}

	public Double getReportLat() {
		return reportLat;
	}

	public void setReportLat(Double reportLat) {
		this.reportLat = reportLat;
	}

	public Double getReportLon() {
		return reportLon;
	}

	public void setReportLon(Double reportLon) {
		this.reportLon = reportLon;
	}

	public Float getReportLatLonAccuracy() {
		return reportLatLonAccuracy;
	}

	public void setReportLatLonAccuracy(Float reportLatLonAccuracy) {
		this.reportLatLonAccuracy = reportLatLonAccuracy;
	}

	@Override
	@ManyToOne(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE,
		CascadeType.DETACH,
		CascadeType.REFRESH })
	public SormasToSormasOriginInfo getSormasToSormasOriginInfo() {
		return sormasToSormasOriginInfo;
	}

	@Override
	public void setSormasToSormasOriginInfo(SormasToSormasOriginInfo sormasToSormasOriginInfo) {
		this.sormasToSormasOriginInfo = sormasToSormasOriginInfo;
	}

	@OneToMany(mappedBy = SormasToSormasShareInfo.SAMPLE, fetch = FetchType.LAZY)
	public List<SormasToSormasShareInfo> getSormasToSormasShares() {
		return sormasToSormasShares;
	}

	public void setSormasToSormasShares(List<SormasToSormasShareInfo> sormasToSormasShares) {
		this.sormasToSormasShares = sormasToSormasShares;
	}

	@OneToMany(mappedBy = SampleReport.SAMPLE, fetch = FetchType.LAZY)
	public List<SampleReport> getSampleReports() {
		return sampleReports;
	}

	public void setSampleReports(List<SampleReport> externalMessages) {
		this.sampleReports = externalMessages;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getCsfSampleCollected() {
		return csfSampleCollected;
	}
	public void setCsfSampleCollected(YesNo csfSampleCollected) {
		this.csfSampleCollected = csfSampleCollected;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getRdtPerformed() {
		return rdtPerformed;
	}
	public void setRdtPerformed(YesNo rdtPerformed) {
		this.rdtPerformed = rdtPerformed;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getSampleSentToLab() {
		return sampleSentToLab;
	}
	public void setSampleSentToLab(YesNo sampleSentToLab) {
		this.sampleSentToLab = sampleSentToLab;
	}
	@Enumerated(EnumType.STRING)
	public CsfReason getCsfReason() {
		return csfReason;
	}
	public void setCsfReason(CsfReason csfReason) {
		this.csfReason = csfReason;
	}
	@Enumerated(EnumType.STRING)
	public CsfAppearance getAppearanceOfCsf() {return appearanceOfCsf; }
	public void setAppearanceOfCsf(CsfAppearance appearanceOfCsf) {
		this.appearanceOfCsf = appearanceOfCsf;
	}
	@Enumerated(EnumType.STRING)
	public SampleContainerUsed getSampleContainerUsed() {
		return sampleContainerUsed;
	}
	public void setSampleContainerUsed(SampleContainerUsed sampleContainerUsed) {
		this.sampleContainerUsed = sampleContainerUsed;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getRdtResults() {
		return rdtResults;
	}

	public void setRdtResults(String rdtResults) {
		this.rdtResults = rdtResults;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getReasonNotSentToLab() {
		return reasonNotSentToLab;
	}

	public void setReasonNotSentToLab(String reasonNotSentToLab) {
		this.reasonNotSentToLab = reasonNotSentToLab;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getNameOfPerson() {
		return nameOfPerson;
	}

	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getInoculationTimeTransportMedia() {
		return inoculationTimeTransportMedia;
	}

	public void setInoculationTimeTransportMedia(Date inoculationTimeTransportMedia) {
		this.inoculationTimeTransportMedia = inoculationTimeTransportMedia;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDistrictNotificationDate() {
		return districtNotificationDate;
	}

	public void setDistrictNotificationDate(Date districtNotificationDate) {
		this.districtNotificationDate = districtNotificationDate;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateSampleSentToLab() {
		return dateSampleSentToLab;
	}

	public void setDateSampleSentToLab(Date dateSampleSentToLab) {
		this.dateSampleSentToLab = dateSampleSentToLab;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormSentToDistrict() {
		return dateFormSentToDistrict;
	}

	public void setDateFormSentToDistrict(Date dateFormSentToDistrict) {
		this.dateFormSentToDistrict = dateFormSentToDistrict;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormReceivedAtDistrict() {
		return dateFormReceivedAtDistrict;
	}

	public void setDateFormReceivedAtDistrict(Date dateFormReceivedAtDistrict) {
		this.dateFormReceivedAtDistrict = dateFormReceivedAtDistrict;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormSentToRegion() {
		return dateFormSentToRegion;
	}

	public void setDateFormSentToRegion(Date dateFormSentToRegion) {
		this.dateFormSentToRegion = dateFormSentToRegion;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormReceivedAtRegion() {
		return dateFormReceivedAtRegion;
	}

	public void setDateFormReceivedAtRegion(Date dateFormReceivedAtRegion) {
		this.dateFormReceivedAtRegion = dateFormReceivedAtRegion;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormSentToNational() {
		return dateFormSentToNational;
	}

	public void setDateFormSentToNational(Date dateFormSentToNational) {
		this.dateFormSentToNational = dateFormSentToNational;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public Date getDateFormReceivedAtNational() {
		return dateFormReceivedAtNational;
	}

	public void setDateFormReceivedAtNational(Date dateFormReceivedAtNational) {
		this.dateFormReceivedAtNational = dateFormReceivedAtNational;
	}
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getLaboratoryName() {
		return laboratoryName;
	}

	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public String getLaboratoryNumber() {
		return laboratoryNumber;
	}

	public void setLaboratoryNumber(String laboratoryNumber) {
		this.laboratoryNumber = laboratoryNumber;
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

	public String getLaboratorySampleContainerOther() {
		return laboratorySampleContainerOther;
	}

	public void setLaboratorySampleContainerOther(String laboratorySampleContainerOther) {
		this.laboratorySampleContainerOther = laboratorySampleContainerOther;
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

	public String getLaboratoryGramOther() {
		return laboratoryGramOther;
	}

	public void setLaboratoryGramOther(String laboratoryGramOther) {
		this.laboratoryGramOther = laboratoryGramOther;
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

	public String getLaboratoryRdtResults() {
		return laboratoryRdtResults;
	}

	public void setLaboratoryRdtResults(String laboratoryRdtResults) {
		this.laboratoryRdtResults = laboratoryRdtResults;
	}
	@Enumerated(EnumType.STRING)
	public CaseClassification getLaboratoryFinalClassification() {
		return laboratoryFinalClassification;
	}

	public void setLaboratoryFinalClassification(CaseClassification laboratoryFinalClassification) {
		this.laboratoryFinalClassification = laboratoryFinalClassification;
	}

	public String getLaboratoryPcrType() {
		return laboratoryPcrType;
	}

	public void setLaboratoryPcrType(String laboratoryPcrType) {
		this.laboratoryPcrType = laboratoryPcrType;
	}
	@Enumerated(EnumType.STRING)
	public SampleContainerUsed getLaboratorySampleContainerReceived() {
		return laboratorySampleContainerReceived;
	}

	public void setLaboratorySampleContainerReceived(SampleContainerUsed laboratorySampleContainerReceived) {
		this.laboratorySampleContainerReceived = laboratorySampleContainerReceived;
	}
	@Enumerated(EnumType.STRING)
	public SpecimenCondition getLaboratorySampleCondition() {
		return laboratorySampleCondition;
	}

	public void setLaboratorySampleCondition(SpecimenCondition laboratorySampleCondition) {
		this.laboratorySampleCondition = laboratorySampleCondition;
	}
	@Enumerated(EnumType.STRING)
	public CsfAppearance getLaboratoryAppearanceOfCSF() {
		return laboratoryAppearanceOfCSF;
	}

	public void setLaboratoryAppearanceOfCSF(CsfAppearance laboratoryAppearanceOfCSF) {
		this.laboratoryAppearanceOfCSF = laboratoryAppearanceOfCSF;
	}
	@Enumerated(EnumType.STRING)
	public LabTest getLaboratoryTestPerformed() {
		return laboratoryTestPerformed;
	}

	public void setLaboratoryTestPerformed(LabTest laboratoryTestPerformed) {
		this.laboratoryTestPerformed = laboratoryTestPerformed;
	}
	@Enumerated(EnumType.STRING)
	public Gram getLaboratoryGram() {
		return laboratoryGram;
	}

	public void setLaboratoryGram(Gram laboratoryGram) {
		this.laboratoryGram = laboratoryGram;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getLaboratoryRdtPerformed() {
		return laboratoryRdtPerformed;
	}

	public void setLaboratoryRdtPerformed(YesNo laboratoryRdtPerformed) {
		this.laboratoryRdtPerformed = laboratoryRdtPerformed;
	}
	@Enumerated(EnumType.STRING)
	public LatexCulture getLaboratoryLatex() {
		return laboratoryLatex;
	}

	public void setLaboratoryLatex(LatexCulture laboratoryLatex) {
		this.laboratoryLatex = laboratoryLatex;
	}
	@Enumerated(EnumType.STRING)
	public LatexCulture getLaboratoryCulture() {
		return laboratoryCulture;
	}

	public void setLaboratoryCulture(LatexCulture laboratoryCulture) {
		this.laboratoryCulture = laboratoryCulture;
	}
	@Enumerated(EnumType.STRING)
	public LatexCulture getLaboratoryPcrOptions() {
		return laboratoryPcrOptions;
	}

	public void setLaboratoryPcrOptions(LatexCulture laboratoryPcrOptions) {
		this.laboratoryPcrOptions = laboratoryPcrOptions;
	}
	@Enumerated(EnumType.STRING)
	public Antibiogram getLaboratoryCeftriaxone() {
		return laboratoryCeftriaxone;
	}

	public void setLaboratoryCeftriaxone(Antibiogram laboratoryCeftriaxone) {
		this.laboratoryCeftriaxone = laboratoryCeftriaxone;
	}
	@Enumerated(EnumType.STRING)
	public Antibiogram getLaboratoryPenicillinG() {
		return laboratoryPenicillinG;
	}

	public void setLaboratoryPenicillinG(Antibiogram laboratoryPenicillinG) {
		this.laboratoryPenicillinG = laboratoryPenicillinG;
	}
	@Enumerated(EnumType.STRING)
	public Antibiogram getLaboratoryAmoxycillin() {
		return laboratoryAmoxycillin;
	}

	public void setLaboratoryAmoxycillin(Antibiogram laboratoryAmoxycillin) {
		this.laboratoryAmoxycillin = laboratoryAmoxycillin;
	}
	@Enumerated(EnumType.STRING)
	public Antibiogram getLaboratoryOxacillin() {
		return laboratoryOxacillin;
	}

	public void setLaboratoryOxacillin(Antibiogram laboratoryOxacillin) {
		this.laboratoryOxacillin = laboratoryOxacillin;
	}
	@Enumerated(EnumType.STRING)
	public Antibiogram getLaboratoryAntibiogramOther() {
		return laboratoryAntibiogramOther;
	}

	public void setLaboratoryAntibiogramOther(Antibiogram laboratoryAntibiogramOther) {
		this.laboratoryAntibiogramOther = laboratoryAntibiogramOther;
	}
	@Enumerated(EnumType.STRING)
	public LabType getLaboratoryType() {
		return laboratoryType;
	}

	public void setLaboratoryType(LabType laboratoryType) {
		this.laboratoryType = laboratoryType;
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

	public Date getLaboratorySampleDateReceived() {
		return laboratorySampleDateReceived;
	}

	public void setLaboratorySampleDateReceived(Date laboratorySampleDateReceived) {
		this.laboratorySampleDateReceived = laboratorySampleDateReceived;
	}

	public Date getLaboratoryDatePcrPerformed() {
		return laboratoryDatePcrPerformed;
	}

	public void setLaboratoryDatePcrPerformed(Date laboratoryDatePcrPerformed) {
		this.laboratoryDatePcrPerformed = laboratoryDatePcrPerformed;
	}

	public Date getDateSentToNationalRegLab() {
		return dateSentToNationalRegLab;
	}

	public void setDateSentToNationalRegLab(Date dateSentToNationalRegLab) {
		this.dateSentToNationalRegLab = dateSentToNationalRegLab;
	}

	public Date getDateDifferentiationSentToEpi() {
		return dateDifferentiationSentToEpi;
	}

	public void setDateDifferentiationSentToEpi(Date dateDifferentiationSentToEpi) {
		this.dateDifferentiationSentToEpi = dateDifferentiationSentToEpi;
	}

	public Date getDateDifferentiationReceivedFromEpi() {
		return dateDifferentiationReceivedFromEpi;
	}

	public void setDateDifferentiationReceivedFromEpi(Date dateDifferentiationReceivedFromEpi) {
		this.dateDifferentiationReceivedFromEpi = dateDifferentiationReceivedFromEpi;
	}

	public Date getDateIsolateSentForSequencing() {
		return dateIsolateSentForSequencing;
	}

	public void setDateIsolateSentForSequencing(Date dateIsolateSentForSequencing) {
		this.dateIsolateSentForSequencing = dateIsolateSentForSequencing;
	}

	public Date getDateSeqResultsSentToProgram() {
		return dateSeqResultsSentToProgram;
	}

	public void setDateSeqResultsSentToProgram(Date dateSeqResultsSentToProgram) {
		this.dateSeqResultsSentToProgram = dateSeqResultsSentToProgram;
	}

	public PosNeg getFinalLabResults() {
		return finalLabResults;
	}

	public void setFinalLabResults(PosNeg finalLabResults) {
		this.finalLabResults = finalLabResults;
	}

	public YesNoUnknown getImmunocompromisedStatusSuspected() {
		return immunocompromisedStatusSuspected;
	}

	public void setImmunocompromisedStatusSuspected(YesNoUnknown immunocompromisedStatusSuspected) {
		this.immunocompromisedStatusSuspected = immunocompromisedStatusSuspected;
	}

	public AFPClassification getAfpFinalClassification() {
		return afpFinalClassification;
	}

	public void setAfpFinalClassification(AFPClassification afpFinalClassification) {
		this.afpFinalClassification = afpFinalClassification;
	}

	public Date getDateFirstSpecimen() {
		return dateFirstSpecimen;
	}

	public void setDateFirstSpecimen(Date dateFirstSpecimen) {
		this.dateFirstSpecimen = dateFirstSpecimen;
	}

	public Date getDateSecondSpecimen() {
		return dateSecondSpecimen;
	}

	public void setDateSecondSpecimen(Date dateSecondSpecimen) {
		this.dateSecondSpecimen = dateSecondSpecimen;
	}

	public Date getDateSpecimenSentNationalLevel() {
		return dateSpecimenSentNationalLevel;
	}

	public void setDateSpecimenSentNationalLevel(Date dateSpecimenSentNationalLevel) {
		this.dateSpecimenSentNationalLevel = dateSpecimenSentNationalLevel;
	}

	public Date getDateSpecimenReceivedNationalLevel() {
		return dateSpecimenReceivedNationalLevel;
	}

	public void setDateSpecimenReceivedNationalLevel(Date dateSpecimenReceivedNationalLevel) {
		this.dateSpecimenReceivedNationalLevel = dateSpecimenReceivedNationalLevel;
	}

	public Date getDateSpecimenSentInter() {
		return dateSpecimenSentInter;
	}

	public void setDateSpecimenSentInter(Date dateSpecimenSentInter) {
		this.dateSpecimenSentInter = dateSpecimenSentInter;
	}

	public Date getDateSpecimenReceivedInter() {
		return dateSpecimenReceivedInter;
	}

	public void setDateSpecimenReceivedInter(Date dateSpecimenReceivedInter) {
		this.dateSpecimenReceivedInter = dateSpecimenReceivedInter;
	}

	public SpecimenCondition getStatusSpecimenReceptionAtLab() {
		return statusSpecimenReceptionAtLab;
	}

	public void setStatusSpecimenReceptionAtLab(SpecimenCondition statusSpecimenReceptionAtLab) {
		this.statusSpecimenReceptionAtLab = statusSpecimenReceptionAtLab;
	}

	public Date getDateCombinedCellCultureResults() {
		return dateCombinedCellCultureResults;
	}

	public void setDateCombinedCellCultureResults(Date dateCombinedCellCultureResults) {
		this.dateCombinedCellCultureResults = dateCombinedCellCultureResults;
	}

	public YesNo getW1() {
		return w1;
	}

	public void setW1(YesNo w1) {
		this.w1 = w1;
	}

	public YesNo getW2() {
		return w2;
	}

	public void setW2(YesNo w2) {
		this.w2 = w2;
	}

	public YesNo getW3() {
		return w3;
	}

	public void setW3(YesNo w3) {
		this.w3 = w3;
	}

	public YesNoUnknown getDiscordant() {
		return discordant;
	}

	public void setDiscordant(YesNoUnknown discordant) {
		this.discordant = discordant;
	}

	public YesNo getsL1() {
		return sL1;
	}

	public void setsL1(YesNo sL1) {
		this.sL1 = sL1;
	}

	public YesNo getsL2() {
		return sL2;
	}

	public void setsL2(YesNo sL2) {
		this.sL2 = sL2;
	}

	public YesNo getsL3() {
		return sL3;
	}

	public void setsL3(YesNo sL3) {
		this.sL3 = sL3;
	}

	public Date getDateFollowUpExam() {
		return dateFollowUpExam;
	}

	public void setDateFollowUpExam(Date dateFollowUpExam) {
		this.dateFollowUpExam = dateFollowUpExam;
	}

	public InjectionSite getResidualAnalysis() {
		return residualAnalysis;
	}

	public void setResidualAnalysis(InjectionSite residualAnalysis) {
		this.residualAnalysis = residualAnalysis;
	}

	public AfpResult getResultExam() {
		return resultExam;
	}

	public void setResultExam(AfpResult resultExam) {
		this.resultExam = resultExam;
	}

	public YesNo getPositiveViralCulture() {
		return positiveViralCulture;
	}

	public void setPositiveViralCulture(YesNo positiveViralCulture) {
		this.positiveViralCulture = positiveViralCulture;
	}

	public YesNo getPositiveRealTime() {
		return positiveRealTime;
	}

	public void setPositiveRealTime(YesNo positiveRealTime) {
		this.positiveRealTime = positiveRealTime;
	}

	public YesNo getFourFoldRise() {
		return fourFoldRise;
	}

	public void setFourFoldRise(YesNo fourFoldRise) {
		this.fourFoldRise = fourFoldRise;
	}

	public InfluenzaVirus getInfluenzaVirus() {
		return influenzaVirus;
	}

	public void setInfluenzaVirus(InfluenzaVirus influenzaVirus) {
		this.influenzaVirus = influenzaVirus;
	}

	public String getOtherInfluenzaVirus() {
		return otherInfluenzaVirus;
	}
	public void setOtherInfluenzaVirus(String otherInfluenzaVirus) {
		this.otherInfluenzaVirus = otherInfluenzaVirus;
	}
	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getStateTreatmentAdministered() {
		return stateTreatmentAdministered;
	}

	public void setStateTreatmentAdministered(String stateTreatmentAdministered) {
		this.stateTreatmentAdministered = stateTreatmentAdministered;
	}

	public Long getPathogenTestCount() {
		return pathogenTestCount;
	}

	public void setPathogenTestCount(Long pathogenTestCount) {
		this.pathogenTestCount = pathogenTestCount;
	}

	public Disease getSuspectedDisease() {
		return suspectedDisease;
	}

	public void setSuspectedDisease(Disease suspectedDisease) {
		this.suspectedDisease = suspectedDisease;
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
	public Date getDateResultsSentToClinician() {
		return dateResultsSentToClinician;
	}

	public void setDateResultsSentToClinician(Date dateResultsSentToClinician) {
		this.dateResultsSentToClinician = dateResultsSentToClinician;
	}

	public Date getDateSpecimenSentToLab() {
		return dateSpecimenSentToLab;
	}

	public void setDateSpecimenSentToLab(Date dateSpecimenSentToLab) {
		this.dateSpecimenSentToLab = dateSpecimenSentToLab;
	}
	public String getContainerOther() {
		return containerOther;
	}

	public void setContainerOther(String containerOther) {
		this.containerOther = containerOther;
	}

	public YesNo getHasSampleBeenCollected() {
		return hasSampleBeenCollected;
	}

	public void setHasSampleBeenCollected(YesNo hasSampleBeenCollected) {
		this.hasSampleBeenCollected = hasSampleBeenCollected;
	}

}
