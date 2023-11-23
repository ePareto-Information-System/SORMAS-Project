package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.Pseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.EmptyValuePseudonymizer;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

public class CaseSampleExportDto implements Serializable {

	private static final long serialVersionUID = -3027326087594387560L;

	public static final String I18N_PREFIX = "SampleExport";

	private String uuid;
	private String labSampleID;
	private Date sampleReportDate;
	private Date sampleDateTime;
	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	@Pseudonymizer(EmptyValuePseudonymizer.class)
	private SampleExportMaterial sampleSampleExportMaterial;
	private String samplePurpose;
	private SampleSource sampleSource;
	private SamplingReason samplingReason;
	private String samplingReasonDetails;
	private String lab;
	private PathogenTestResultType pathogenTestResult;
	private Boolean pathogenTestingRequested;
	private Set<PathogenTestType> requestedPathogenTests;
	private String requestedOtherPathogenTests;
	private Boolean additionalTestingRequested;
	private Set<AdditionalTestType> requestedAdditionalTests;
	private String requestedOtherAdditionalTests;
	private boolean shipped;
	private Date shipmentDate;
	@SensitiveData
	private String shipmentDetails;
	private boolean received;
	private Date receivedDate;
	private SpecimenCondition specimenCondition;
	@SensitiveData
	private String noTestPossibleReason;
	@SensitiveData
	private String comment;
	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	private SampleExportPathogenTest pathogenTest1 = new SampleExportPathogenTest();
	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	private SampleExportPathogenTest pathogenTest2 = new SampleExportPathogenTest();
	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	private SampleExportPathogenTest pathogenTest3 = new SampleExportPathogenTest();
	private List<SampleExportPathogenTest> otherPathogenTests = new ArrayList<>();
	private AdditionalTestDto additionalTest;
	private String otherAdditionalTestsDetails = "";


	private Long caseId;

	//@formatter:off
	public CaseSampleExportDto(
							String uuid,
							String labSampleId,
							Date sampleReportDate,
						    Date sampleDateTime,
							SampleMaterial sampleMaterial,
							String sampleMaterialDetails,
							SamplePurpose samplePurpose,
							SampleSource sampleSource,
							SamplingReason samplingReason,
							String samplingReasonDetails,
							String laboratory,
							String laboratoryDetails,
							PathogenTestResultType pathogenTestResult,
							Boolean pathogenTestingRequested,
							String requestedPathogenTests,
							String requestedOtherPathogenTests,
							Boolean additionalTestingRequested,
							String requestedAdditionalTests,
							String requestedOtherAdditionalTests,
							boolean shipped,
							Date shipmentDate,
							String shipmentDetails,
							boolean received,
							Date receivedDate,
							SpecimenCondition specimenCondition,
							String noTestPossibleReason,
							String comment,
							String labUuid,
							Long caseId) {
		//@formatter:on
		this.uuid = uuid;
		this.labSampleID = labSampleId;
		this.sampleReportDate = sampleReportDate;
		this.sampleDateTime = sampleDateTime;
		this.sampleSampleExportMaterial = new SampleExportMaterial(sampleMaterial, sampleMaterialDetails);
		if (samplePurpose != null)
			this.samplePurpose = samplePurpose.toString();
		this.sampleSource = sampleSource;
		this.samplingReason = samplingReason;
		this.samplingReasonDetails = samplingReasonDetails;
		this.lab = FacilityHelper.buildFacilityString(labUuid, laboratory, laboratoryDetails);
		this.pathogenTestResult = pathogenTestResult;
		this.pathogenTestingRequested = pathogenTestingRequested;
		this.requestedPathogenTests = new HashSet<>();
		if (!StringUtils.isEmpty(requestedPathogenTests)) {
			for (String s : requestedPathogenTests.split(",")) {
				this.requestedPathogenTests.add(PathogenTestType.valueOf(s));
			}
		}
		this.requestedOtherPathogenTests = requestedOtherPathogenTests;
		this.additionalTestingRequested = additionalTestingRequested;
		this.requestedAdditionalTests = new HashSet<>();
		if (!StringUtils.isEmpty(requestedAdditionalTests)) {
			for (String s : requestedAdditionalTests.split(",")) {
				this.requestedAdditionalTests.add(AdditionalTestType.valueOf(s));
			}
		}
		this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
		this.shipped = shipped;
		this.shipmentDate = shipmentDate;
		this.shipmentDetails = shipmentDetails;
		this.received = received;
		this.receivedDate = receivedDate;
		this.specimenCondition = specimenCondition;
		this.noTestPossibleReason = noTestPossibleReason;
		this.comment = comment;
		this.caseId = caseId;
	}


	@Order(1)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Order(2)
	public String getLabSampleID() {
		return labSampleID;
	}

	public void setLabSampleID(String labSampleID) {
		this.labSampleID = labSampleID;
	}

	@Order(10)
	public Date getSampleDateTime() {
		return sampleDateTime;
	}

	public void setSampleDateTime(Date sampleDateTime) {
		this.sampleDateTime = sampleDateTime;
	}

	@Order(11)
	public String getSampleMaterialString() {
		return sampleSampleExportMaterial.formatString();
	}

	public SampleExportMaterial getSampleSampleExportMaterial() {
		return sampleSampleExportMaterial;
	}

	@Order(12)
	public String getSamplePurpose() {
		return samplePurpose;
	}

	public void setSamplePurpose(String samplePurpose) {
		this.samplePurpose = samplePurpose;
	}

	@Order(13)
	public SampleSource getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(SampleSource sampleSource) {
		this.sampleSource = sampleSource;
	}

	@Order(14)
	public SamplingReason getSamplingReason() {
		return samplingReason;
	}

	@Order(15)
	public String getSamplingReasonDetails() {
		return samplingReasonDetails;
	}

	@Order(16)
	public String getLab() {
		return lab;
	}

	public void setLab(String lab) {
		this.lab = lab;
	}

	@Order(17)
	public PathogenTestResultType getPathogenTestResult() {
		return pathogenTestResult;
	}

	public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {
		this.pathogenTestResult = pathogenTestResult;
	}

	@Order(18)
	public Boolean getPathogenTestingRequested() {
		return pathogenTestingRequested;
	}

	public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {
		this.pathogenTestingRequested = pathogenTestingRequested;
	}

	@Order(19)
	public Set<PathogenTestType> getRequestedPathogenTests() {
		return requestedPathogenTests;
	}

	public void setRequestedPathogenTests(Set<PathogenTestType> requestedPathogenTests) {
		this.requestedPathogenTests = requestedPathogenTests;
	}

	@Order(20)
	public String getRequestedOtherPathogenTests() {
		return requestedOtherPathogenTests;
	}

	public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {
		this.requestedOtherPathogenTests = requestedOtherPathogenTests;
	}

	@Order(21)
	public Boolean getAdditionalTestingRequested() {
		return additionalTestingRequested;
	}

	public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {
		this.additionalTestingRequested = additionalTestingRequested;
	}

	@Order(22)
	public Set<AdditionalTestType> getRequestedAdditionalTests() {
		return requestedAdditionalTests;
	}

	public void setRequestedAdditionalTests(Set<AdditionalTestType> requestedAdditionalTests) {
		this.requestedAdditionalTests = requestedAdditionalTests;
	}

	@Order(23)
	public String getRequestedOtherAdditionalTests() {
		return requestedOtherAdditionalTests;
	}

	public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {
		this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
	}

	@Order(24)
	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	@Order(25)
	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	@Order(26)
	public String getShipmentDetails() {
		return shipmentDetails;
	}

	public void setShipmentDetails(String shipmentDetails) {
		this.shipmentDetails = shipmentDetails;
	}

	@Order(27)
	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	@Order(28)
	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Order(31)
	public SpecimenCondition getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(SpecimenCondition specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	@Order(32)
	public String getNoTestPossibleReason() {
		return noTestPossibleReason;
	}

	public void setNoTestPossibleReason(String noTestPossibleReason) {
		this.noTestPossibleReason = noTestPossibleReason;
	}

	@Order(33)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	@Order(71)
	public String getPathogenTestType1() {
		return pathogenTest1.formatType();
	}

	@Order(72)
	public String getPathogenTestDisease1() {
		return pathogenTest1.disease;
	}

	@Order(73)
	public Date getPathogenTestDateTime1() {
		return pathogenTest1.dateTime;
	}

	@Order(74)
	public String getPathogenTestLab1() {
		return pathogenTest1.lab;
	}

	@Order(75)
	public PathogenTestResultType getPathogenTestResult1() {
		return pathogenTest1.testResult;
	}

	@Order(76)
	public Boolean getPathogenTestVerified1() {
		return pathogenTest1.verified;
	}

	@Order(81)
	public String getPathogenTestType2() {
		return pathogenTest2.formatType();
	}

	@Order(82)
	public String getPathogenTestDisease2() {
		return pathogenTest2.disease;
	}

	@Order(83)
	public Date getPathogenTestDateTime2() {
		return pathogenTest2.dateTime;
	}

	@Order(84)
	public String getPathogenTestLab2() {
		return pathogenTest2.lab;
	}

	@Order(85)
	public PathogenTestResultType getPathogenTestResult2() {
		return pathogenTest2.testResult;
	}

	@Order(86)
	public Boolean getPathogenTestVerified2() {
		return pathogenTest2.verified;
	}

	@Order(91)
	public String getPathogenTestType3() {
		return pathogenTest3.formatType();
	}

	@Order(92)
	public String getPathogenTestDisease3() {
		return pathogenTest3.disease;
	}

	@Order(93)
	public Date getPathogenTestDateTime3() {
		return pathogenTest3.dateTime;
	}

	@Order(94)
	public String getPathogenTestLab3() {
		return pathogenTest3.lab;
	}

	@Order(95)
	public PathogenTestResultType getPathogenTestResult3() {
		return pathogenTest3.testResult;
	}

	@Order(96)
	public Boolean getPathogenTestVerified3() {
		return pathogenTest3.verified;
	}

	@Order(97)
	public String getOtherPathogenTestsDetails() {
		StringBuilder sb = new StringBuilder();
		String separator = ", ";

		for (SampleExportPathogenTest otherPathogenTest : otherPathogenTests) {
			sb.append(otherPathogenTest.formatString()).append(separator);
		}

		return sb.length() > 0 ? sb.substring(0, sb.length() - separator.length()) : "";
	}

	public void addOtherPathogenTest(SampleExportPathogenTest pathogenTest) {
		otherPathogenTests.add(pathogenTest);
	}

	@Order(101)
	public AdditionalTestDto getAdditionalTest() {
		return additionalTest;
	}

	public void setAdditionalTest(AdditionalTestDto additionalTest) {
		this.additionalTest = additionalTest;
	}

	@Order(102)
	public String getOtherAdditionalTestsDetails() {
		return otherAdditionalTestsDetails;
	}

	public void setOtherAdditionalTestsDetails(String otherAdditionalTestsDetails) {
		this.otherAdditionalTestsDetails = otherAdditionalTestsDetails;
	}

	@Order(103)
	public Date getSampleReportDate() {
		return sampleReportDate;
	}

	public void setSampleReportDate(Date sampleReportDate) {
		this.sampleReportDate = sampleReportDate;
	}

	public SampleExportPathogenTest getPathogenTest1() {
		return pathogenTest1;
	}

	public void setPathogenTest1(SampleExportPathogenTest pathogenTest1) {
		this.pathogenTest1 = pathogenTest1;
	}

	public SampleExportPathogenTest getPathogenTest2() {
		return pathogenTest2;
	}

	public void setPathogenTest2(SampleExportPathogenTest pathogenTest2) {
		this.pathogenTest2 = pathogenTest2;
	}

	public SampleExportPathogenTest getPathogenTest3() {
		return pathogenTest3;
	}

	public void setPathogenTest3(SampleExportPathogenTest pathogenTest3) {
		this.pathogenTest3 = pathogenTest3;
	}

	public List<SampleExportPathogenTest> getOtherPathogenTests() {
		return otherPathogenTests;
	}

	public static class SampleExportMaterial implements Serializable {

		private SampleMaterial sampleMaterial;
		@SensitiveData
		private String sampleMaterialDetails;

		public SampleExportMaterial(SampleMaterial sampleMaterial, String sampleMaterialDetails) {
			this.sampleMaterial = sampleMaterial;
			this.sampleMaterialDetails = sampleMaterialDetails;
		}

		public String formatString() {
			return SampleMaterial.toString(sampleMaterial, sampleMaterialDetails);
		}
	}

	public static class SampleExportPathogenTest implements Serializable {

		private PathogenTestType testType;
		@SensitiveData
		private String testTypeText;
		private String disease;
		private Date dateTime;
		private String lab;
		private PathogenTestResultType testResult;
		private Boolean verified;

		public SampleExportPathogenTest() {
		}

		public SampleExportPathogenTest(
			PathogenTestType testType,
			String testTypeText,
			String disease,
			Date dateTime,
			String lab,
			PathogenTestResultType testResult,
			Boolean verified) {
			this.testType = testType;
			this.testTypeText = testTypeText;
			this.disease = disease;
			this.dateTime = dateTime;
			this.lab = lab;
			this.testResult = testResult;
			this.verified = verified;
		}

		public String formatType() {
			return PathogenTestType.toString(testType, testTypeText);
		}

		public String formatString() {
			StringBuilder sb = new StringBuilder();
			sb.append(DateHelper.formatDateForExport(dateTime)).append(" (");
			String type = formatType();
			if (type.length() > 0) {
				sb.append(type).append(", ");
			}

			sb.append(disease).append(", ").append(testResult).append(")");

			return sb.toString();
		}
	}


	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}
}
