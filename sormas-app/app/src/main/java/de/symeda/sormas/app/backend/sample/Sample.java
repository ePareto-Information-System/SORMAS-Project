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

package de.symeda.sormas.app.backend.sample;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;
import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.sample.AdditionalTestType;
import de.symeda.sormas.api.sample.FinalClassification;
import de.symeda.sormas.api.sample.IpSampleTestType;
import de.symeda.sormas.api.sample.FilterChangingFrequency;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.PosNegEq;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.sample.SamplePurpose;
import de.symeda.sormas.api.sample.SampleSource;
import de.symeda.sormas.api.sample.SamplingReason;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.utils.CsfAppearance;
import de.symeda.sormas.api.utils.CsfReason;
import de.symeda.sormas.api.utils.ExamResult;
import de.symeda.sormas.api.utils.InjectionSite;
import de.symeda.sormas.api.utils.PosNeg;
import de.symeda.sormas.api.utils.SampleContainerUsed;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.contact.Contact;
import de.symeda.sormas.app.backend.event.EventParticipant;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfo;
import de.symeda.sormas.app.backend.user.User;
//import de.symeda.sormas.app.core.YesNo;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.util.DateFormatHelper;

@Entity(name = Sample.TABLE_NAME)
@DatabaseTable(tableName = Sample.TABLE_NAME)
public class Sample extends PseudonymizableAdo {

	private static final long serialVersionUID = -7196712070188634978L;

	public static final String TABLE_NAME = "samples";
	public static final String I18N_PREFIX = "Sample";

	public static final String SAMPLE_DATE_TIME = "sampleDateTime";
	public static final String ASSOCIATED_CASE = "associatedCase";
	public static final String REFERRED_TO_UUID = "referredToUuid";
	public static final String SHIPPED = "shipped";
	public static final String RECEIVED = "received";
	public static final String PATHOGEN_TEST_RESULT = "pathogenTestResult";
	public static final String LAB_SAMPLE_ID = "labSampleID";
	public static final String FIELD_SAMPLE_ID = "fieldSampleID";
	public static final String FOR_RETEST = "forRetest";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Case associatedCase;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Contact associatedContact;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private EventParticipant associatedEventParticipant;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String labSampleID;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String fieldSampleID;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown forRetest;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date sampleDateTime;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date reportDateTime;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private User reportingUser;

	@DatabaseField
	private Double reportLat;
	@DatabaseField
	private Double reportLon;
	@DatabaseField
	private Float reportLatLonAccuracy;

	@Enumerated(EnumType.STRING)
	private SampleMaterial sampleMaterial;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String sampleMaterialText;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private Facility lab;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String labDetails;

	@Enumerated(EnumType.STRING)
	private SamplePurpose samplePurpose;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date shipmentDate;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String shipmentDetails;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date receivedDate;

	@Enumerated(EnumType.STRING)
	private SpecimenCondition specimenCondition;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String noTestPossibleReason;

	@Column(length = CHARACTER_LIMIT_BIG)
	private String comment;

	@Enumerated(EnumType.STRING)
	private SampleSource sampleSource;

	@DatabaseField
	private String referredToUuid;

	@DatabaseField
	private boolean shipped;

	@DatabaseField
	private boolean received;

	@Enumerated(EnumType.STRING)
	private PathogenTestResultType pathogenTestResult;
	@Enumerated(EnumType.STRING)
	private Disease suspectedDisease;
	@DatabaseField
	private Boolean pathogenTestingRequested;

	@DatabaseField
	private Boolean additionalTestingRequested;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedPathogenTestsString;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedAdditionalTestsString;

	@Transient
	private Set<PathogenTestType> requestedPathogenTests;
	@Transient
	private Set<IpSampleTestType> ipSampleTestResults;
	@Transient
	private Set<SampleMaterial> requestedSampleMaterials;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedSampleMaterialsString;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ipSampleTestResultsString;
	@Enumerated(EnumType.STRING)
	private PosNegEq selectedResultIGM;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date selectedResultIGMDate;
	@Enumerated(EnumType.STRING)
	private PosNegEq selectedResultPcr;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date selectedResultPcrDate;
	@Enumerated(EnumType.STRING)
	private PosNegEq selectedResultPrnt;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date selectedResultPrntDate;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String inputValuePrnt;
	@Transient
	private Set<AdditionalTestType> requestedAdditionalTests;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedOtherPathogenTests;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedOtherAdditionalTests;

	@Enumerated(EnumType.STRING)
	private SamplingReason samplingReason;
	@Column(columnDefinition = "text")
	private String samplingReasonDetails;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private SormasToSormasOriginInfo sormasToSormasOriginInfo;
	@DatabaseField
	private boolean ownershipHandedOver;
	@Enumerated(EnumType.STRING)
	private SampleDispatchMode sampleDispatchMode;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date sampleDispatchDate;
	@Enumerated(EnumType.STRING)
	private YesNo ipSampleSent;
	@Enumerated(EnumType.STRING)
	private YesNo hasSampleBeenCollected;

	@Enumerated(EnumType.STRING)
	private YesNo specimenSavedAndPreservedInAlcohol;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String specimenSavedAndPreservedInAlcoholWhy;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenSentToRegion;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenReceivedAtRegion;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nameOfPersonWhoReceivedSpecimenAtRegion;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenSentToNational;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenReceivedAtNational;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nameOfPersonWhoReceivedSpecimenAtNational;

	@Enumerated(EnumType.STRING)
	private YesNo sentForConfirmationNational;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date sentForConfirmationNationalDate;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String sentForConfirmationTo;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateResultReceivedNational;

	@Enumerated(EnumType.STRING)
	private YesNo useOfClothFilter;

	@Enumerated(EnumType.STRING)
	private FilterChangingFrequency frequencyOfChangingFilters;

	@Column(columnDefinition = "text")
	private String remarks;

	@Enumerated(EnumType.STRING)
	private YesNo confirmedAsGuineaWorm;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String labLocation;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToDistrict;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormReceivedAtDistrict;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToHigherLevel;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String personCompletingForm;
	@Enumerated(EnumType.STRING)
	private CsfAppearance appearanceOfCsf;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date inoculationTimeTransportMedia;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSampleSentToLab;
	@Enumerated(EnumType.STRING)
	private YesNo sampleSentToLab;
	@Enumerated(EnumType.STRING)
	private SampleContainerUsed sampleContainerUsed;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String containerOther;
	@Enumerated(EnumType.STRING)
	private YesNo rdtPerformed;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String rdtResults;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date districtNotificationDate;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nameOfPerson;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String telNumber;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToRegion;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormReceivedAtRegion;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToNational;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormReceivedAtNational;
	@DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateFirstSpecimen;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSecondSpecimen;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenSentNationalLevel;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenReceivedNationalLevel;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenSentInter;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSpecimenReceivedInter;
	@Enumerated(EnumType.STRING)
	private SpecimenCondition statusSpecimenReceptionAtLab;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateCombinedCellCultureResults;
	@Enumerated(EnumType.STRING)
	private YesNo w1;
	@Enumerated(EnumType.STRING)
	private YesNo w2;
	@Enumerated(EnumType.STRING)
	private YesNo w3;
	@Enumerated(EnumType.STRING)
	private YesNo sL1;
	@Enumerated(EnumType.STRING)
	private YesNo sL2;
	@Enumerated(EnumType.STRING)
	private YesNo sL3;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSentToNationalRegLab;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateDifferentiationSentToEpi;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateDifferentiationReceivedFromEpi;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateIsolateSentForSequencing;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSeqResultsSentToProgram;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFollowUpExam;
	@Enumerated(EnumType.STRING)
	private InjectionSite residualAnalysis;
	@Enumerated(EnumType.STRING)
	private ExamResult resultExam;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown immunocompromisedStatusSuspected;
	@Enumerated(EnumType.STRING)
	private FinalClassification afpFinalClassification;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date laboratorySampleDateReceived;
	@Enumerated(EnumType.STRING)
	private CsfReason csfReason;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown discordant;
	@Enumerated(EnumType.STRING)
	private PosNeg finalLabResults;


	public Case getAssociatedCase() {
		return associatedCase;
	}

	public void setAssociatedCase(Case associatedCase) {
		this.associatedCase = associatedCase;
	}

	public Contact getAssociatedContact() {
		return associatedContact;
	}

	public void setAssociatedContact(Contact associatedContact) {
		this.associatedContact = associatedContact;
	}

	public EventParticipant getAssociatedEventParticipant() {
		return associatedEventParticipant;
	}

	public void setAssociatedEventParticipant(EventParticipant associatedEventParticipant) {
		this.associatedEventParticipant = associatedEventParticipant;
	}

	public String getLabSampleID() {
		return labSampleID;
	}

	public void setLabSampleID(String labSampleID) {
		this.labSampleID = labSampleID;
	}

	public Date getSampleDateTime() {
		return sampleDateTime;
	}

	public String getFieldSampleID() {
		return fieldSampleID;
	}

	public void setFieldSampleID(String fieldSampleID) {
		this.fieldSampleID = fieldSampleID;
	}

	public YesNoUnknown getForRetest() {
		return forRetest;
	}

	public void setForRetest(YesNoUnknown forRetest) {
		this.forRetest = forRetest;
	}

	public void setSampleDateTime(Date sampleDateTime) {
		this.sampleDateTime = sampleDateTime;
	}

	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	public SampleMaterial getSampleMaterial() {
		return sampleMaterial;
	}

	public void setSampleMaterial(SampleMaterial sampleMaterial) {
		this.sampleMaterial = sampleMaterial;
	}

	public String getSampleMaterialText() {
		return sampleMaterialText;
	}

	public void setSampleMaterialText(String sampleMaterialText) {
		this.sampleMaterialText = sampleMaterialText;
	}

	public Facility getLab() {
		return lab;
	}

	public void setLab(Facility lab) {
		this.lab = lab;
	}

	public String getLabDetails() {
		return labDetails;
	}

	public void setLabDetails(String labDetails) {
		this.labDetails = labDetails;
	}

	public SamplePurpose getSamplePurpose() {
		return samplePurpose;
	}

	public void setSamplePurpose(SamplePurpose samplePurpose) {
		this.samplePurpose = samplePurpose;
	}

	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public String getShipmentDetails() {
		return shipmentDetails;
	}

	public void setShipmentDetails(String shipmentDetails) {
		this.shipmentDetails = shipmentDetails;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getNoTestPossibleReason() {
		return noTestPossibleReason;
	}

	public void setNoTestPossibleReason(String noTestPossibleReason) {
		this.noTestPossibleReason = noTestPossibleReason;
	}

	public SpecimenCondition getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(SpecimenCondition specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public SampleSource getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(SampleSource sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getReferredToUuid() {
		return referredToUuid;
	}

	public void setReferredToUuid(String referredToUuid) {
		this.referredToUuid = referredToUuid;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public PathogenTestResultType getPathogenTestResult() {
		return pathogenTestResult;
	}

	public Disease getSuspectedDisease() {
		return suspectedDisease;
	}

	public void setSuspectedDisease(Disease suspectedDisease) {
		this.suspectedDisease = suspectedDisease;
	}

	public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {
		this.pathogenTestResult = pathogenTestResult;
	}

	public Boolean getPathogenTestingRequested() {
		return pathogenTestingRequested;
	}

	public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {
		this.pathogenTestingRequested = pathogenTestingRequested;
	}

	public Boolean getAdditionalTestingRequested() {
		return additionalTestingRequested;
	}

	public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {
		this.additionalTestingRequested = additionalTestingRequested;
	}

	public String getRequestedPathogenTestsString() {
		return requestedPathogenTestsString;
	}

	public void setRequestedPathogenTestsString(String requestedPathogenTestsString) {
		this.requestedPathogenTestsString = requestedPathogenTestsString;
		requestedPathogenTests = null;
	}

	public String getIpSampleTestResultsString() {
		return ipSampleTestResultsString;
	}

	public void setIpSampleTestResultsString(String ipSampleTestResultsString) {
		this.ipSampleTestResultsString = ipSampleTestResultsString;
		ipSampleTestResults = null;
	}

	public String getRequestedAdditionalTestsString() {
		return requestedAdditionalTestsString;
	}

	public void setRequestedAdditionalTestsString(String requestedAdditionalTestsString) {
		this.requestedAdditionalTestsString = requestedAdditionalTestsString;
		requestedAdditionalTests = null;
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

	public String getRequestedSampleMaterialsString() {
		return requestedSampleMaterialsString;
	}

	public void setRequestedSampleMaterialsString(String requestedSampleMaterialsString) {
		this.requestedSampleMaterialsString = requestedSampleMaterialsString;
		requestedSampleMaterials = null;
	}

	@Transient
	public Set<PathogenTestType> getRequestedPathogenTests() {
		if (requestedPathogenTests == null) {
			requestedPathogenTests = new HashSet<>();
			if (!StringUtils.isEmpty(requestedPathogenTestsString)) {
				String[] testTypes = requestedPathogenTestsString.split(",");
				for (String testType : testTypes) {
					requestedPathogenTests.add(PathogenTestType.valueOf(testType));
				}
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
		for (PathogenTestType test : requestedPathogenTests) {
			sb.append(test.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedPathogenTestsString = sb.toString();
	}

	@Transient

	public Set<IpSampleTestType> getIpSampleTestResults() {
		if (ipSampleTestResults == null) {
			ipSampleTestResults = new HashSet<>();
			if (!StringUtils.isEmpty(ipSampleTestResultsString)) {
				String[] testTypes = ipSampleTestResultsString.split(",");
				for (String testType : testTypes) {
					ipSampleTestResults.add(IpSampleTestType.valueOf(testType));
				}
			}
		}
		return ipSampleTestResults;
	}

	public void setIpSampleTestResults(Set<IpSampleTestType> ipSampleTestResults) {
		this.ipSampleTestResults = ipSampleTestResults;

		if (this.ipSampleTestResults == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		ipSampleTestResults.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		ipSampleTestResultsString = sb.toString();
	}

	public PosNegEq getSelectedResultIGM() {
		return selectedResultIGM;
	}

	public void setSelectedResultIGM(PosNegEq selectedResultIGM) {
		this.selectedResultIGM = selectedResultIGM;
	}

	public Date getSelectedResultIGMDate() {
		return selectedResultIGMDate;
	}

	public void setSelectedResultIGMDate(Date selectedResultIGMDate) {
		this.selectedResultIGMDate = selectedResultIGMDate;
	}

	public PosNegEq getSelectedResultPcr() {
		return selectedResultPcr;
	}

	public void setSelectedResultPcr(PosNegEq selectedResultPcr) {
		this.selectedResultPcr = selectedResultPcr;
	}

	public Date getSelectedResultPcrDate() {
		return selectedResultPcrDate;
	}

	public void setSelectedResultPcrDate(Date selectedResultPcrDate) {
		this.selectedResultPcrDate = selectedResultPcrDate;
	}

	public PosNegEq getSelectedResultPrnt() {
		return selectedResultPrnt;
	}

	public void setSelectedResultPrnt(PosNegEq selectedResultPrnt) {
		this.selectedResultPrnt = selectedResultPrnt;
	}

	public Date getSelectedResultPrntDate() {
		return selectedResultPrntDate;
	}

	public void setSelectedResultPrntDate(Date selectedResultPrntDate) {
		this.selectedResultPrntDate = selectedResultPrntDate;
	}

	public String getInputValuePrnt() {
		return inputValuePrnt;
	}

	public void setInputValuePrnt(String inputValuePrnt) {
		this.inputValuePrnt = inputValuePrnt;
	}

	@Transient
	public Set<AdditionalTestType> getRequestedAdditionalTests() {
		if (requestedAdditionalTests == null) {
			requestedAdditionalTests = new HashSet<>();
			if (!StringUtils.isEmpty(requestedAdditionalTestsString)) {
				String[] testTypes = requestedAdditionalTestsString.split(",");
				for (String testType : testTypes) {
					requestedAdditionalTests.add(AdditionalTestType.valueOf(testType));
				}
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
		for (AdditionalTestType test : requestedAdditionalTests) {
			sb.append(test.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedAdditionalTestsString = sb.toString();
	}

	public String getRequestedOtherPathogenTests() {
		return requestedOtherPathogenTests;
	}

	public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {
		this.requestedOtherPathogenTests = requestedOtherPathogenTests;
	}

	public String getRequestedOtherAdditionalTests() {
		return requestedOtherAdditionalTests;
	}

	public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {
		this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

	@Override
	public String buildCaption() {
		return super.buildCaption() + " " + DateFormatHelper.formatLocalDate(getSampleDateTime());
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

	public SamplingReason getSamplingReason() {
		return samplingReason;
	}

	public void setSamplingReason(SamplingReason samplingReason) {
		this.samplingReason = samplingReason;
	}

	public String getSamplingReasonDetails() {
		return samplingReasonDetails;
	}

	public void setSamplingReasonDetails(String samplingReasonDetails) {
		this.samplingReasonDetails = samplingReasonDetails;
	}

	public SormasToSormasOriginInfo getSormasToSormasOriginInfo() {
		return sormasToSormasOriginInfo;
	}

	public void setSormasToSormasOriginInfo(SormasToSormasOriginInfo sormasToSormasOriginInfo) {
		this.sormasToSormasOriginInfo = sormasToSormasOriginInfo;
	}

	public boolean isOwnershipHandedOver() {
		return ownershipHandedOver;
	}

	public void setOwnershipHandedOver(boolean ownershipHandedOver) {
		this.ownershipHandedOver = ownershipHandedOver;
	}

	public SampleDispatchMode getSampleDispatchMode() {
		return sampleDispatchMode;
	}
	public void setSampleDispatchMode(SampleDispatchMode sampleDispatchMode) {
		this.sampleDispatchMode = sampleDispatchMode;
	}

	public Date getSampleDispatchDate() {
		return sampleDispatchDate;
	}

	public void setSampleDispatchDate(Date sampleDispatchDate) {
		this.sampleDispatchDate = sampleDispatchDate;
	}

	public de.symeda.sormas.api.utils.YesNo getIpSampleSent() {
		return ipSampleSent;
	}

	public void setIpSampleSent(YesNo ipSampleSent) {
		this.ipSampleSent = ipSampleSent;
	}
	public YesNo getSpecimenSavedAndPreservedInAlcohol() {
		return specimenSavedAndPreservedInAlcohol;
	}

	public void setSpecimenSavedAndPreservedInAlcohol(YesNo specimenSavedAndPreservedInAlcohol) {
		this.specimenSavedAndPreservedInAlcohol = specimenSavedAndPreservedInAlcohol;
	}

	public String getSpecimenSavedAndPreservedInAlcoholWhy() {
		return specimenSavedAndPreservedInAlcoholWhy;
	}

	public void setSpecimenSavedAndPreservedInAlcoholWhy(String specimenSavedAndPreservedInAlcoholWhy) {
		this.specimenSavedAndPreservedInAlcoholWhy = specimenSavedAndPreservedInAlcoholWhy;
	}

	public Date getDateSpecimenSentToRegion() {
		return dateSpecimenSentToRegion;
	}

	public void setDateSpecimenSentToRegion(Date dateSpecimenSentToRegion) {
		this.dateSpecimenSentToRegion = dateSpecimenSentToRegion;
	}

	public Date getDateSpecimenReceivedAtRegion() {
		return dateSpecimenReceivedAtRegion;
	}

	public void setDateSpecimenReceivedAtRegion(Date dateSpecimenReceivedAtRegion) {
		this.dateSpecimenReceivedAtRegion = dateSpecimenReceivedAtRegion;
	}

	public String getNameOfPersonWhoReceivedSpecimenAtRegion() {
		return nameOfPersonWhoReceivedSpecimenAtRegion;
	}

	public void setNameOfPersonWhoReceivedSpecimenAtRegion(String nameOfPersonWhoReceivedSpecimenAtRegion) {
		this.nameOfPersonWhoReceivedSpecimenAtRegion = nameOfPersonWhoReceivedSpecimenAtRegion;
	}

	public Date getDateSpecimenSentToNational() {
		return dateSpecimenSentToNational;
	}

	public void setDateSpecimenSentToNational(Date dateSpecimenSentToNational) {
		this.dateSpecimenSentToNational = dateSpecimenSentToNational;
	}

	public Date getDateSpecimenReceivedAtNational() {
		return dateSpecimenReceivedAtNational;
	}

	public void setDateSpecimenReceivedAtNational(Date dateSpecimenReceivedAtNational) {
		this.dateSpecimenReceivedAtNational = dateSpecimenReceivedAtNational;
	}

	public String getNameOfPersonWhoReceivedSpecimenAtNational() {
		return nameOfPersonWhoReceivedSpecimenAtNational;
	}

	public void setNameOfPersonWhoReceivedSpecimenAtNational(String nameOfPersonWhoReceivedSpecimenAtNational) {
		this.nameOfPersonWhoReceivedSpecimenAtNational = nameOfPersonWhoReceivedSpecimenAtNational;
	}

	public YesNo getSentForConfirmationNational() {
		return sentForConfirmationNational;
	}

	public void setSentForConfirmationNational(YesNo sentForConfirmationNational) {
		this.sentForConfirmationNational = sentForConfirmationNational;
	}

	public Date getSentForConfirmationNationalDate() {
		return sentForConfirmationNationalDate;
	}

	public void setSentForConfirmationNationalDate(Date sentForConfirmationNationalDate) {
		this.sentForConfirmationNationalDate = sentForConfirmationNationalDate;
	}

	public String getSentForConfirmationTo() {
		return sentForConfirmationTo;
	}

	public void setSentForConfirmationTo(String sentForConfirmationTo) {
		this.sentForConfirmationTo = sentForConfirmationTo;
	}

	public Date getDateResultReceivedNational() {
		return dateResultReceivedNational;
	}

	public void setDateResultReceivedNational(Date dateResultReceivedNational) {
		this.dateResultReceivedNational = dateResultReceivedNational;
	}

	public YesNo getUseOfClothFilter() {
		return useOfClothFilter;
	}

	public void setUseOfClothFilter(YesNo useOfClothFilter) {
		this.useOfClothFilter = useOfClothFilter;
	}

	public FilterChangingFrequency getFrequencyOfChangingFilters() {
		return frequencyOfChangingFilters;
	}

	public void setFrequencyOfChangingFilters(FilterChangingFrequency frequencyOfChangingFilters) {
		this.frequencyOfChangingFilters = frequencyOfChangingFilters;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public YesNo getConfirmedAsGuineaWorm() {
		return confirmedAsGuineaWorm;
	}

	public void setConfirmedAsGuineaWorm(YesNo confirmedAsGuineaWorm) {
		this.confirmedAsGuineaWorm = confirmedAsGuineaWorm;
	}

	public String getLabLocation() {
		return labLocation;
	}

	public void setLabLocation(String labLocation) {
		this.labLocation = labLocation;
	}

	public Date getDateFormSentToDistrict() {
		return dateFormSentToDistrict;
	}

	public void setDateFormSentToDistrict(Date dateFormSentToDistrict) {
		this.dateFormSentToDistrict = dateFormSentToDistrict;
	}

	public Date getDateFormReceivedAtDistrict() {
		return dateFormReceivedAtDistrict;
	}

	public void setDateFormReceivedAtDistrict(Date dateFormReceivedAtDistrict) {
		this.dateFormReceivedAtDistrict = dateFormReceivedAtDistrict;
	}

	public Date getDateFormSentToHigherLevel() {
		return dateFormSentToHigherLevel;
	}

	public void setDateFormSentToHigherLevel(Date dateFormSentToHigherLevel) {
		this.dateFormSentToHigherLevel = dateFormSentToHigherLevel;
	}

	public String getPersonCompletingForm() {
		return personCompletingForm;
	}

	public void setPersonCompletingForm(String personCompletingForm) {
		this.personCompletingForm = personCompletingForm;
	}

	public YesNo getHasSampleBeenCollected() {
		return hasSampleBeenCollected;
	}

	public void setHasSampleBeenCollected(YesNo hasSampleBeenCollected) {
		this.hasSampleBeenCollected = hasSampleBeenCollected;
	}

	public CsfAppearance getAppearanceOfCsf() {return appearanceOfCsf; }
	public void setAppearanceOfCsf(CsfAppearance appearanceOfCsf) {
		this.appearanceOfCsf = appearanceOfCsf;
	}

	public Date getInoculationTimeTransportMedia() {
		return inoculationTimeTransportMedia;
	}

	public void setInoculationTimeTransportMedia(Date inoculationTimeTransportMedia) {
		this.inoculationTimeTransportMedia = inoculationTimeTransportMedia;
	}
	public YesNo getSampleSentToLab() {
		return sampleSentToLab;
	}
	public void setSampleSentToLab(YesNo sampleSentToLab) {
		this.sampleSentToLab = sampleSentToLab;
	}

	public Date getDateSampleSentToLab() {
		return dateSampleSentToLab;
	}

	public void setDateSampleSentToLab(Date dateSampleSentToLab) {
		this.dateSampleSentToLab = dateSampleSentToLab;
	}

	public SampleContainerUsed getSampleContainerUsed() {
		return sampleContainerUsed;
	}
	public void setSampleContainerUsed(SampleContainerUsed sampleContainerUsed) {
		this.sampleContainerUsed = sampleContainerUsed;
	}

	public String getContainerOther() {
		return containerOther;
	}

	public void setContainerOther(String containerOther) {
		this.containerOther = containerOther;
	}

	public YesNo getRdtPerformed() {
		return rdtPerformed;
	}
	public void setRdtPerformed(YesNo rdtPerformed) {
		this.rdtPerformed = rdtPerformed;
	}

	public String getRdtResults() {
		return rdtResults;
	}

	public void setRdtResults(String rdtResults) {
		this.rdtResults = rdtResults;
	}

	public Date getDistrictNotificationDate() {
		return districtNotificationDate;
	}

	public void setDistrictNotificationDate(Date districtNotificationDate) {
		this.districtNotificationDate = districtNotificationDate;
	}
	public String getNameOfPerson() {
		return nameOfPerson;
	}

	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}
	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public Date getDateFormSentToRegion() {
		return dateFormSentToRegion;
	}

	public void setDateFormSentToRegion(Date dateFormSentToRegion) {
		this.dateFormSentToRegion = dateFormSentToRegion;
	}

	public Date getDateFormReceivedAtRegion() {
		return dateFormReceivedAtRegion;
	}

	public void setDateFormReceivedAtRegion(Date dateFormReceivedAtRegion) {
		this.dateFormReceivedAtRegion = dateFormReceivedAtRegion;
	}

	public Date getDateFormSentToNational() {
		return dateFormSentToNational;
	}

	public void setDateFormSentToNational(Date dateFormSentToNational) {
		this.dateFormSentToNational = dateFormSentToNational;
	}

	public Date getDateFormReceivedAtNational() {
		return dateFormReceivedAtNational;
	}

	public void setDateFormReceivedAtNational(Date dateFormReceivedAtNational) {
		this.dateFormReceivedAtNational = dateFormReceivedAtNational;
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

	public YesNo getSL1() {
		return sL1;
	}

	public void setSL1(YesNo sL1) {
		this.sL1 = sL1;
	}

	public YesNo getSL2() {
		return sL2;
	}

	public void setSL2(YesNo sL2) {
		this.sL2 = sL2;
	}

	public YesNo getSL3() {
		return sL3;
	}

	public void setSL3(YesNo sL3) {
		this.sL3 = sL3;
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

	public ExamResult getResultExam() {
		return resultExam;
	}

	public void setResultExam(ExamResult resultExam) {
		this.resultExam = resultExam;
	}

	public YesNoUnknown getImmunocompromisedStatusSuspected() {
		return immunocompromisedStatusSuspected;
	}

	public void setImmunocompromisedStatusSuspected(YesNoUnknown immunocompromisedStatusSuspected) {
		this.immunocompromisedStatusSuspected = immunocompromisedStatusSuspected;
	}

	public FinalClassification getAfpFinalClassification() {
		return afpFinalClassification;
	}

	public void setAfpFinalClassification(FinalClassification afpFinalClassification) {
		this.afpFinalClassification = afpFinalClassification;
	}

	public Date getLaboratorySampleDateReceived() {
		return laboratorySampleDateReceived;
	}

	public void setLaboratorySampleDateReceived(Date laboratorySampleDateReceived) {
		this.laboratorySampleDateReceived = laboratorySampleDateReceived;
	}

	public CsfReason getCsfReason() {
		return csfReason;
	}
	public void setCsfReason(CsfReason csfReason) {
		this.csfReason = csfReason;
	}

	public YesNoUnknown getDiscordant(){
		return discordant;
	}
	public void setDiscordant(YesNoUnknown discordant){
		this.discordant = discordant;
	}

	public PosNeg getFinalLabResults() {
		return finalLabResults;
	}

	public void setFinalLabResults(PosNeg finalLabResults) {
		this.finalLabResults = finalLabResults;
	}
}
