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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.sample.FinalClassification;
import de.symeda.sormas.api.sample.PCRTestSpecification;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.util.DateFormatHelper;

@Entity(name = PathogenTest.TABLE_NAME)
@DatabaseTable(tableName = PathogenTest.TABLE_NAME)
public class PathogenTest extends PseudonymizableAdo {

	private static final long serialVersionUID = 2290351143518627813L;

	public static final String TABLE_NAME = "pathogenTest";
	public static final String I18N_PREFIX = "PathogenTest";

	public static final String TEST_DATE_TIME = "testDateTime";
	public static final String SAMPLE = "sample";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Sample sample;

	@Enumerated(EnumType.STRING)
	private PathogenTestType testType;

	@Enumerated(EnumType.STRING)
	private PCRTestSpecification pcrTestSpecification;

	@Column
	private String testTypeText;

	@Enumerated(EnumType.STRING)
	private Disease testedDisease;

	@Column(name = "testedDiseaseVariant")
	private String testedDiseaseVariantString;
	private DiseaseVariant testedDiseaseVariant;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String testedDiseaseDetails;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String testedDiseaseVariantDetails;

	@Column
	private String typingId;
	@Column
	private String virusDetectionGenotype;

	@Enumerated(EnumType.STRING)
	@Column
	private PathogenTestResultType testResult;

	@Column
	private Boolean testResultVerified;

	@Column(length = CHARACTER_LIMIT_BIG)
	private String testResultText;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date testDateTime;

	@Column
	private boolean fourFoldIncreaseAntibodyTiter;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String serotype;

	@DatabaseField
	private Float cqValue;

	@DatabaseField(dataType = DataType.DATE_LONG, canBeNull = true)
	private Date reportDate;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private Facility lab;

	@Column
	private String labDetails;
	@Column
	private String labLocation;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private User labUser;

	@Column
	private boolean viaLims;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateLabResultsSentDistrict;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateLabResultsSentClinician;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateSurveillanceSentResultsToDistrict;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateDistrictReceivedLabResults;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date laboratoryDateResultsSentDSD;

	@Enumerated
	private FinalClassification finalClassification;

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
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

	public Disease getTestedDisease() {
		return testedDisease;
	}

	public void setTestedDisease(Disease testedDisease) {
		this.testedDisease = testedDisease;
	}

	public String getTestedDiseaseVariantString() {
		return testedDiseaseVariantString;
	}

	public void setTestedDiseaseVariantString(String testedDiseaseVariantString) {
		this.testedDiseaseVariantString = testedDiseaseVariantString;
	}

	@Transient
	public DiseaseVariant getTestedDiseaseVariant() {
		if (StringUtils.isBlank(testedDiseaseVariantString)) {
			return null;
		} else {
			return DatabaseHelper.getCustomizableEnumValueDao().getEnumValue(CustomizableEnumType.DISEASE_VARIANT, testedDiseaseVariantString);
		}
	}

	public void setTestedDiseaseVariant(DiseaseVariant testedDiseaseVariant) {
		this.testedDiseaseVariant = testedDiseaseVariant;
		if (testedDiseaseVariant == null) {
			testedDiseaseVariantString = null;
		} else {
			testedDiseaseVariantString = testedDiseaseVariant.getValue();
		}
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

	public String getVirusDetectionGenotype() {
		return virusDetectionGenotype;
	}

	public void setVirusDetectionGenotype(String virusDetectionGenotype) {
		this.virusDetectionGenotype = virusDetectionGenotype;
	}

	public void setTypingId(String typingId) {
		this.typingId = typingId;
	}

	public PathogenTestResultType getTestResult() {
		return testResult;
	}

	public Boolean getTestResultVerified() {
		return testResultVerified;
	}

	public void setTestResultVerified(Boolean testResultVerified) {
		this.testResultVerified = testResultVerified;
	}

	public String getTestResultText() {
		return testResultText;
	}

	public void setTestResultText(String testResultText) {
		this.testResultText = testResultText;
	}

	public Facility getLab() {
		return lab;
	}

	public void setLab(Facility lab) {
		this.lab = lab;
	}

	public void setTestResult(PathogenTestResultType testResult) {
		this.testResult = testResult;
	}

	public User getLabUser() {
		return labUser;
	}

	public void setLabUser(User labUser) {
		this.labUser = labUser;
	}

	public Date getTestDateTime() {
		return testDateTime;
	}

	public void setTestDateTime(Date testDateTime) {
		this.testDateTime = testDateTime;
	}

	public String getLabDetails() {
		return labDetails;
	}

	public void setLabDetails(String labDetails) {
		this.labDetails = labDetails;
	}

	public String getLabLocation() {
		return labLocation;
	}

	public void setLabLocation(String labLocation) {
		this.labLocation = labLocation;
	}

	public boolean isFourFoldIncreaseAntibodyTiter() {
		return fourFoldIncreaseAntibodyTiter;
	}

	public void setFourFoldIncreaseAntibodyTiter(boolean fourFoldIncreaseAntibodyTiter) {
		this.fourFoldIncreaseAntibodyTiter = fourFoldIncreaseAntibodyTiter;
	}

	public String getTestTypeText() {
		return testTypeText;
	}

	public void setTestTypeText(String testTypeText) {
		this.testTypeText = testTypeText;
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
	

	public Date getDateSurveillanceSentResultsToDistrict() {
		return dateSurveillanceSentResultsToDistrict;
	}

	public void setDateSurveillanceSentResultsToDistrict(Date dateSurveillanceSentResultsToDistrict) {
		this.dateSurveillanceSentResultsToDistrict = dateSurveillanceSentResultsToDistrict;
	}

	public Date getDateDistrictReceivedLabResults() {
		return dateDistrictReceivedLabResults;
	}

	public void setDateDistrictReceivedLabResults(Date dateDistrictReceivedLabResults) {
		this.dateDistrictReceivedLabResults = dateDistrictReceivedLabResults;
	}

	public Date getLaboratoryDateResultsSentDSD() {
		return laboratoryDateResultsSentDSD;
	}

	public void setLaboratoryDateResultsSentDSD(Date laboratoryDateResultsSentDSD) {
		this.laboratoryDateResultsSentDSD = laboratoryDateResultsSentDSD;
	}

	public FinalClassification getFinalClassification() {
		return finalClassification;
	}

	public void setFinalClassification(FinalClassification finalClassification) {
		this.finalClassification = finalClassification;
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

	@Override
	public String buildCaption() {
		return super.buildCaption() + DateFormatHelper.formatLocalDate(getTestDateTime());
	}


}
