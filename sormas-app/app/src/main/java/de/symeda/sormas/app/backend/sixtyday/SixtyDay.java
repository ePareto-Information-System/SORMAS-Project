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

package de.symeda.sormas.app.backend.sixtyday;

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

import de.symeda.sormas.api.utils.NormalWasted;
import de.symeda.sormas.api.utils.PackagingType;
import de.symeda.sormas.api.utils.ParalysisSite;
import de.symeda.sormas.api.utils.SymptomLevel;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;

@Entity(name = SixtyDay.TABLE_NAME)
@DatabaseTable(tableName = SixtyDay.TABLE_NAME)
@EmbeddedAdo
public class SixtyDay extends PseudonymizableAdo {

	private static final long serialVersionUID = -8294812479501735788L;

	public static final String TABLE_NAME = "sixtyday";
	public static final String I18N_PREFIX = "SixtyDay";
//	public static final String I18N_PREFIX = "CaseSixtyDayFollowup";


	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String personExamineCase;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date dateOfFollowup;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date dateBirth;
	@Column
	private String residentialLocation;
	@Enumerated(EnumType.STRING)
	private YesNo patientFound;
	@Column
	private String patientFoundReason;
	@Column
	private String locateChildAttempt;
	@Enumerated(EnumType.STRING)
	private YesNo paralysisWeaknessPresent;
	@Transient
	private Set<ParalysisSite> paralysisWeaknessPresentSite;
	@Column
	private String paralysisWeaknessPresentSiteString;
	@Column
	private String paralyzedPartOther;
	@Enumerated(EnumType.STRING)
	private YesNo paralysisWeaknessFloppy;
	@Enumerated(EnumType.STRING)
	private SymptomLevel muscleToneParalyzedPart;
	@Enumerated(EnumType.STRING)
	private SymptomLevel muscleToneOtherPartBody;
	@Enumerated(EnumType.STRING)
	private SymptomLevel deepTendon;
	@Enumerated(EnumType.STRING)
	private NormalWasted muscleVolume;
	@Enumerated(EnumType.STRING)
	private YesNo sensoryLoss;
	@Column
	private String provisionalDiagnosis;
	@Column
	private String comments;
	@Column
	private String contactDetailsNumber;
	@Column
	private String contactDetailsEmail;
	@Column
	private String signature;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date dateSubmissionForms;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown foodAvailableTesting;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown labTestConducted;
	@Column
	private String specifyFoodsSources;
	@Column
	private String specifySources;
	@Column
	private String productName;
	@Column
	private String batchNumber;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date dateOfManufacture;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date expirationDate;
	@Column
	private String packageSize;
	@Enumerated(EnumType.STRING)
	private PackagingType packagingType;
	@Column
	private String packagingTypeOther;
	@Column
	private String placeOfPurchase;
	@Column
	private String nameOfManufacturer;
	@Column
	private String address;
	@Column
	private String foodTel;
	@Column
	private String surname;
	@Column
	private String firstName;
	@Column
	private String middleName;
	@Column
	private String telNo;
	@DatabaseField
	private Date dateOfCompletionOfForm;
	@Column
	private String nameOfHealthFacility;
	@Column
	private String barcode;

	public String getPersonExamineCase() {
		return personExamineCase;
	}

	public void setPersonExamineCase(String personExamineCase) {
		this.personExamineCase = personExamineCase;
	}

	public Date getDateOfFollowup() {
		return dateOfFollowup;
	}

	public void setDateOfFollowup(Date dateOfFollowup) {
		this.dateOfFollowup = dateOfFollowup;
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getResidentialLocation() {
		return residentialLocation;
	}

	public void setResidentialLocation(String residentialLocation) {
		this.residentialLocation = residentialLocation;
	}

	public YesNo getPatientFound() {
		return patientFound;
	}

	public void setPatientFound(YesNo patientFound) {
		this.patientFound = patientFound;
	}

	public String getPatientFoundReason() {
		return patientFoundReason;
	}

	public void setPatientFoundReason(String patientFoundReason) {
		this.patientFoundReason = patientFoundReason;
	}

	public String getLocateChildAttempt() {
		return locateChildAttempt;
	}

	public void setLocateChildAttempt(String locateChildAttempt) {
		this.locateChildAttempt = locateChildAttempt;
	}

	public YesNo getParalysisWeaknessPresent() {
		return paralysisWeaknessPresent;
	}

	public void setParalysisWeaknessPresent(YesNo paralysisWeaknessPresent) {
		this.paralysisWeaknessPresent = paralysisWeaknessPresent;
	}


	public String getParalysisWeaknessPresentSiteString() {
		return paralysisWeaknessPresentSiteString;
	}

	public void setParalysisWeaknessPresentSite(String paralysisWeaknessPresentSiteString) {
		this.paralysisWeaknessPresentSiteString = paralysisWeaknessPresentSiteString;
	}

	public String getParalyzedPartOther() {
		return paralyzedPartOther;
	}

	public void setParalyzedPartOther(String paralyzedPartOther) {
		this.paralyzedPartOther = paralyzedPartOther;
	}

	public YesNo getParalysisWeaknessFloppy() {
		return paralysisWeaknessFloppy;
	}

	public void setParalysisWeaknessFloppy(YesNo paralysisWeaknessFloppy) {
		this.paralysisWeaknessFloppy = paralysisWeaknessFloppy;
	}

	public SymptomLevel getMuscleToneParalyzedPart() {
		return muscleToneParalyzedPart;
	}

	public void setMuscleToneParalyzedPart(SymptomLevel muscleToneParalyzedPart) {
		this.muscleToneParalyzedPart = muscleToneParalyzedPart;
	}

	public SymptomLevel getMuscleToneOtherPartBody() {
		return muscleToneOtherPartBody;
	}

	public void setMuscleToneOtherPartBody(SymptomLevel muscleToneOtherPartBody) {
		this.muscleToneOtherPartBody = muscleToneOtherPartBody;
	}

	public SymptomLevel getDeepTendon() {
		return deepTendon;
	}

	public void setDeepTendon(SymptomLevel deepTendon) {
		this.deepTendon = deepTendon;
	}

	public NormalWasted getMuscleVolume() {
		return muscleVolume;
	}

	public void setMuscleVolume(NormalWasted muscleVolume) {
		this.muscleVolume = muscleVolume;
	}

	public YesNo getSensoryLoss() {
		return sensoryLoss;
	}

	public void setSensoryLoss(YesNo sensoryLoss) {
		this.sensoryLoss = sensoryLoss;
	}

	public String getProvisionalDiagnosis() {
		return provisionalDiagnosis;
	}

	public void setProvisionalDiagnosis(String provisionalDiagnosis) {
		this.provisionalDiagnosis = provisionalDiagnosis;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContactDetailsNumber() {
		return contactDetailsNumber;
	}

	public void setContactDetailsNumber(String contactDetailsNumber) {
		this.contactDetailsNumber = contactDetailsNumber;
	}

	public String getContactDetailsEmail() {
		return contactDetailsEmail;
	}

	public void setContactDetailsEmail(String contactDetailsEmail) {
		this.contactDetailsEmail = contactDetailsEmail;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getDateSubmissionForms() {
		return dateSubmissionForms;
	}

	public void setDateSubmissionForms(Date dateSubmissionForms) {
		this.dateSubmissionForms = dateSubmissionForms;
	}

	public YesNoUnknown getFoodAvailableTesting() {
		return foodAvailableTesting;
	}

	public void setFoodAvailableTesting(YesNoUnknown foodAvailableTesting) {
		this.foodAvailableTesting = foodAvailableTesting;
	}

	public YesNoUnknown getLabTestConducted() {
		return labTestConducted;
	}

	public void setLabTestConducted(YesNoUnknown labTestConducted) {
		this.labTestConducted = labTestConducted;
	}

	public String getSpecifyFoodsSources() {
		return specifyFoodsSources;
	}

	public void setSpecifyFoodsSources(String specifyFoodsSources) {
		this.specifyFoodsSources = specifyFoodsSources;
	}

	public String getSpecifySources() {
		return specifySources;
	}

	public void setSpecifySources(String specifySources) {
		this.specifySources = specifySources;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getDateOfManufacture() {
		return dateOfManufacture;
	}

	public void setDateOfManufacture(Date dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public PackagingType getPackagingType() {
		return packagingType;
	}

	public void setPackagingType(PackagingType packagingType) {
		this.packagingType = packagingType;
	}

	public String getPackagingTypeOther() {
		return packagingTypeOther;
	}

	public void setPackagingTypeOther(String packagingTypeOther) {
		this.packagingTypeOther = packagingTypeOther;
	}

	public String getPlaceOfPurchase() {
		return placeOfPurchase;
	}

	public void setPlaceOfPurchase(String placeOfPurchase) {
		this.placeOfPurchase = placeOfPurchase;
	}

	public String getNameOfManufacturer() {
		return nameOfManufacturer;
	}

	public void setNameOfManufacturer(String nameOfManufacturer) {
		this.nameOfManufacturer = nameOfManufacturer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFoodTel() {
		return foodTel;
	}

	public void setFoodTel(String foodTel) {
		this.foodTel = foodTel;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Date getDateOfCompletionOfForm() {
		return dateOfCompletionOfForm;
	}

	public void setDateOfCompletionOfForm(Date dateOfCompletionOfForm) {
		this.dateOfCompletionOfForm = dateOfCompletionOfForm;
	}

	public String getNameOfHealthFacility() {
		return nameOfHealthFacility;
	}

	public void setNameOfHealthFacility(String nameOfHealthFacility) {
		this.nameOfHealthFacility = nameOfHealthFacility;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Transient
	public Set<ParalysisSite> getParalysisWeaknessPresentSite() {
		if (paralysisWeaknessPresentSite == null) {
			if (StringUtils.isEmpty(paralysisWeaknessPresentSiteString)) {
				paralysisWeaknessPresentSite = new HashSet<>();
			} else {
				paralysisWeaknessPresentSite =
						Arrays.stream(paralysisWeaknessPresentSiteString.split(",")).map(ParalysisSite::valueOf).collect(Collectors.toSet());
			}
		}
		return paralysisWeaknessPresentSite;
	}

	public void setParalysisWeaknessPresentSite(Set<ParalysisSite> paralysisSites) {
		this.paralysisWeaknessPresentSite = paralysisSites;

		if (this.paralysisWeaknessPresentSite == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		paralysisSites.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		paralysisWeaknessPresentSiteString = sb.toString();
	}


	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}


}
