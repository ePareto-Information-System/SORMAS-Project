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

package de.symeda.sormas.app.backend.riskfactor;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.affectedperson.AffectedPerson;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.app.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.app.backend.exposure.Exposure;
import de.symeda.sormas.app.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;
import de.symeda.sormas.app.backend.patienttraveldetailsduring.PatientTravelDetailsDuring;
import de.symeda.sormas.app.backend.patienttraveldetailsprior.PatientTravelDetailsPrior;
import de.symeda.sormas.app.backend.persontravelhistory.PersonTravelHistory;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

@Entity(name = RiskFactor.TABLE_NAME)
@DatabaseTable(tableName = RiskFactor.TABLE_NAME)
@EmbeddedAdo
public class RiskFactor extends PseudonymizableAdo {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "riskfactor";
	public static final String I18N_PREFIX = "RiskFactor";

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String drinkingWaterSourceOne;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String drinkingWaterSourceTwo;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String drinkingWaterSourceThree;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String drinkingWaterSourceFour;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nonDrinkingWaterSourceOne;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nonDrinkingWaterSourceTwo;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nonDrinkingWaterSourceThree;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String nonDrinkingWaterSourceFour;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsOne;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsTwo;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsThree;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsFour;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsFive;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsSix;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsSeven;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String foodItemsEight;
	@Enumerated(EnumType.STRING)
	private YesNo drinkingWaterInfectedByVibrio;
	@Enumerated(EnumType.STRING)
	private YesNo nonDrinkingWaterInfectedByVibrio;
	@Enumerated(EnumType.STRING)
	private YesNo foodItemsInfectedByVibrio;
	@Enumerated(EnumType.STRING)
	private DrinkingWaterSource waterUsedForDrinking;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseWaterSourceOne;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseWaterSourceTwo;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseWaterSourceThree;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseWaterSourceFour;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseWaterSourceFive;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseFoodItemsOne;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseFoodItemsTwo;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseFoodItemsThree;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseFoodItemsFour;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseFoodItemsFive;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseAttendAnyFuneral;
	@Enumerated(EnumType.STRING)
	private YesNo threeDaysPriorToDiseaseAttendAnySocialEvent;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String otherSocialEventDetails;
	@Enumerated(EnumType.STRING)
	private YesNo patientSpoxVaccinationScarPresent;
	@Enumerated(EnumType.STRING)
	private YesNo patientTravelledAnywhere3WeeksPrior;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelled3WeeksIfYesIndicate;
	@Enumerated(EnumType.STRING)
	private YesNo patientTravelledPeriodOfIllness;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledIllnessIfYesIndicate;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String otherPlaces;
	@Enumerated(EnumType.STRING)
	private YesNo during3WeeksPatientContactWithSimilarSymptoms;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateOfContactWithIllPerson;
	@Enumerated(EnumType.STRING)
	private YesNo patientTouchDomesticWildAnimal;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTouchDomesticWildAnimalIfYes;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String during3WeeksPatientContactWithSimilarSymptomsIfYes;

	private List<PatientTravelDetailsDuring> patientTravelDetailsDurings = new ArrayList<>();
	private List<PatientTravelDetailsPrior> patientTravelDetailsPriors = new ArrayList<>();

	private List<PatientSymptomsPrecedence> patientSymptomsPrecedences = new ArrayList<>();

	public String getDrinkingWaterSourceOne() {
		return drinkingWaterSourceOne;
	}

	public void setDrinkingWaterSourceOne(String drinkingWaterSourceOne) {
		this.drinkingWaterSourceOne = drinkingWaterSourceOne;
	}

	public String getDrinkingWaterSourceTwo() {
		return drinkingWaterSourceTwo;
	}

	public void setDrinkingWaterSourceTwo(String drinkingWaterSourceTwo) {
		this.drinkingWaterSourceTwo = drinkingWaterSourceTwo;
	}

	public String getDrinkingWaterSourceThree() {
		return drinkingWaterSourceThree;
	}

	public void setDrinkingWaterSourceThree(String drinkingWaterSourceThree) {
		this.drinkingWaterSourceThree = drinkingWaterSourceThree;
	}

	public String getDrinkingWaterSourceFour() {
		return drinkingWaterSourceFour;
	}

	public void setDrinkingWaterSourceFour(String drinkingWaterSourceFour) {
		this.drinkingWaterSourceFour = drinkingWaterSourceFour;
	}

	public String getNonDrinkingWaterSourceOne() {
		return nonDrinkingWaterSourceOne;
	}

	public void setNonDrinkingWaterSourceOne(String nonDrinkingWaterSourceOne) {
		this.nonDrinkingWaterSourceOne = nonDrinkingWaterSourceOne;
	}

	public String getNonDrinkingWaterSourceTwo() {
		return nonDrinkingWaterSourceTwo;
	}

	public void setNonDrinkingWaterSourceTwo(String nonDrinkingWaterSourceTwo) {
		this.nonDrinkingWaterSourceTwo = nonDrinkingWaterSourceTwo;
	}

	public String getNonDrinkingWaterSourceThree() {
		return nonDrinkingWaterSourceThree;
	}

	public void setNonDrinkingWaterSourceThree(String nonDrinkingWaterSourceThree) {
		this.nonDrinkingWaterSourceThree = nonDrinkingWaterSourceThree;
	}

	public String getNonDrinkingWaterSourceFour() {
		return nonDrinkingWaterSourceFour;
	}

	public void setNonDrinkingWaterSourceFour(String nonDrinkingWaterSourceFour) {
		this.nonDrinkingWaterSourceFour = nonDrinkingWaterSourceFour;
	}

	public String getFoodItemsOne() {
		return foodItemsOne;
	}

	public void setFoodItemsOne(String foodItemsOne) {
		this.foodItemsOne = foodItemsOne;
	}

	public String getFoodItemsTwo() {
		return foodItemsTwo;
	}

	public void setFoodItemsTwo(String foodItemsTwo) {
		this.foodItemsTwo = foodItemsTwo;
	}

	public String getFoodItemsThree() {
		return foodItemsThree;
	}

	public void setFoodItemsThree(String foodItemsThree) {
		this.foodItemsThree = foodItemsThree;
	}

	public String getFoodItemsFour() {
		return foodItemsFour;
	}

	public void setFoodItemsFour(String foodItemsFour) {
		this.foodItemsFour = foodItemsFour;
	}

	public String getFoodItemsFive() {
		return foodItemsFive;
	}

	public void setFoodItemsFive(String foodItemsFive) {
		this.foodItemsFive = foodItemsFive;
	}

	public String getFoodItemsSix() {
		return foodItemsSix;
	}

	public void setFoodItemsSix(String foodItemsSix) {
		this.foodItemsSix = foodItemsSix;
	}

	public String getFoodItemsSeven() {
		return foodItemsSeven;
	}

	public void setFoodItemsSeven(String foodItemsSeven) {
		this.foodItemsSeven = foodItemsSeven;
	}

	public String getFoodItemsEight() {
		return foodItemsEight;
	}

	public void setFoodItemsEight(String foodItemsEight) {
		this.foodItemsEight = foodItemsEight;
	}

	public YesNo getDrinkingWaterInfectedByVibrio() {
		return drinkingWaterInfectedByVibrio;
	}

	public void setDrinkingWaterInfectedByVibrio(YesNo drinkingWaterInfectedByVibrio) {
		this.drinkingWaterInfectedByVibrio = drinkingWaterInfectedByVibrio;
	}

	public YesNo getNonDrinkingWaterInfectedByVibrio() {
		return nonDrinkingWaterInfectedByVibrio;
	}

	public void setNonDrinkingWaterInfectedByVibrio(YesNo nonDrinkingWaterInfectedByVibrio) {
		this.nonDrinkingWaterInfectedByVibrio = nonDrinkingWaterInfectedByVibrio;
	}

	public YesNo getFoodItemsInfectedByVibrio() {
		return foodItemsInfectedByVibrio;
	}

	public void setFoodItemsInfectedByVibrio(YesNo foodItemsInfectedByVibrio) {
		this.foodItemsInfectedByVibrio = foodItemsInfectedByVibrio;
	}

	public DrinkingWaterSource getWaterUsedForDrinking() {
		return waterUsedForDrinking;
	}

	public void setWaterUsedForDrinking(DrinkingWaterSource waterUsedForDrinking) {
		this.waterUsedForDrinking = waterUsedForDrinking;
	}

	public YesNo getThreeDaysPriorToDiseaseWaterSourceOne() {
		return threeDaysPriorToDiseaseWaterSourceOne;
	}

	public void setThreeDaysPriorToDiseaseWaterSourceOne(YesNo threeDaysPriorToDiseaseWaterSourceOne) {
		this.threeDaysPriorToDiseaseWaterSourceOne = threeDaysPriorToDiseaseWaterSourceOne;
	}

	public YesNo getThreeDaysPriorToDiseaseWaterSourceTwo() {
		return threeDaysPriorToDiseaseWaterSourceTwo;
	}

	public void setThreeDaysPriorToDiseaseWaterSourceTwo(YesNo threeDaysPriorToDiseaseWaterSourceTwo) {
		this.threeDaysPriorToDiseaseWaterSourceTwo = threeDaysPriorToDiseaseWaterSourceTwo;
	}

	public YesNo getThreeDaysPriorToDiseaseWaterSourceThree() {
		return threeDaysPriorToDiseaseWaterSourceThree;
	}

	public void setThreeDaysPriorToDiseaseWaterSourceThree(YesNo threeDaysPriorToDiseaseWaterSourceThree) {
		this.threeDaysPriorToDiseaseWaterSourceThree = threeDaysPriorToDiseaseWaterSourceThree;
	}

	public YesNo getThreeDaysPriorToDiseaseWaterSourceFour() {
		return threeDaysPriorToDiseaseWaterSourceFour;
	}

	public void setThreeDaysPriorToDiseaseWaterSourceFour(YesNo threeDaysPriorToDiseaseWaterSourceFour) {
		this.threeDaysPriorToDiseaseWaterSourceFour = threeDaysPriorToDiseaseWaterSourceFour;
	}

	public YesNo getThreeDaysPriorToDiseaseWaterSourceFive() {
		return threeDaysPriorToDiseaseWaterSourceFive;
	}

	public void setThreeDaysPriorToDiseaseWaterSourceFive(YesNo threeDaysPriorToDiseaseWaterSourceFive) {
		this.threeDaysPriorToDiseaseWaterSourceFive = threeDaysPriorToDiseaseWaterSourceFive;
	}

	public YesNo getThreeDaysPriorToDiseaseFoodItemsOne() {
		return threeDaysPriorToDiseaseFoodItemsOne;
	}

	public void setThreeDaysPriorToDiseaseFoodItemsOne(YesNo threeDaysPriorToDiseaseFoodItemsOne) {
		this.threeDaysPriorToDiseaseFoodItemsOne = threeDaysPriorToDiseaseFoodItemsOne;
	}

	public YesNo getThreeDaysPriorToDiseaseFoodItemsTwo() {
		return threeDaysPriorToDiseaseFoodItemsTwo;
	}

	public void setThreeDaysPriorToDiseaseFoodItemsTwo(YesNo threeDaysPriorToDiseaseFoodItemsTwo) {
		this.threeDaysPriorToDiseaseFoodItemsTwo = threeDaysPriorToDiseaseFoodItemsTwo;
	}

	public YesNo getThreeDaysPriorToDiseaseFoodItemsThree() {
		return threeDaysPriorToDiseaseFoodItemsThree;
	}

	public void setThreeDaysPriorToDiseaseFoodItemsThree(YesNo threeDaysPriorToDiseaseFoodItemsThree) {
		this.threeDaysPriorToDiseaseFoodItemsThree = threeDaysPriorToDiseaseFoodItemsThree;
	}

	public YesNo getThreeDaysPriorToDiseaseFoodItemsFour() {
		return threeDaysPriorToDiseaseFoodItemsFour;
	}

	public void setThreeDaysPriorToDiseaseFoodItemsFour(YesNo threeDaysPriorToDiseaseFoodItemsFour) {
		this.threeDaysPriorToDiseaseFoodItemsFour = threeDaysPriorToDiseaseFoodItemsFour;
	}

	public YesNo getThreeDaysPriorToDiseaseFoodItemsFive() {
		return threeDaysPriorToDiseaseFoodItemsFive;
	}

	public void setThreeDaysPriorToDiseaseFoodItemsFive(YesNo threeDaysPriorToDiseaseFoodItemsFive) {
		this.threeDaysPriorToDiseaseFoodItemsFive = threeDaysPriorToDiseaseFoodItemsFive;
	}

	public YesNo getThreeDaysPriorToDiseaseAttendAnyFuneral() {
		return threeDaysPriorToDiseaseAttendAnyFuneral;
	}

	public void setThreeDaysPriorToDiseaseAttendAnyFuneral(YesNo threeDaysPriorToDiseaseAttendAnyFuneral) {
		this.threeDaysPriorToDiseaseAttendAnyFuneral = threeDaysPriorToDiseaseAttendAnyFuneral;
	}

	public YesNo getThreeDaysPriorToDiseaseAttendAnySocialEvent() {
		return threeDaysPriorToDiseaseAttendAnySocialEvent;
	}

	public void setThreeDaysPriorToDiseaseAttendAnySocialEvent(YesNo threeDaysPriorToDiseaseAttendAnySocialEvent) {
		this.threeDaysPriorToDiseaseAttendAnySocialEvent = threeDaysPriorToDiseaseAttendAnySocialEvent;
	}

	public String getOtherSocialEventDetails() {
		return otherSocialEventDetails;
	}

	public void setOtherSocialEventDetails(String otherSocialEventDetails) {
		this.otherSocialEventDetails = otherSocialEventDetails;
	}

	public YesNo getPatientSpoxVaccinationScarPresent() {
		return patientSpoxVaccinationScarPresent;
	}

	public void setPatientSpoxVaccinationScarPresent(YesNo patientSpoxVaccinationScarPresent) {
		this.patientSpoxVaccinationScarPresent = patientSpoxVaccinationScarPresent;
	}

	public YesNo getPatientTravelledAnywhere3WeeksPrior() {
		return patientTravelledAnywhere3WeeksPrior;
	}

	public void setPatientTravelledAnywhere3WeeksPrior(YesNo patientTravelledAnywhere3WeeksPrior) {
		this.patientTravelledAnywhere3WeeksPrior = patientTravelledAnywhere3WeeksPrior;
	}

	public String getPatientTravelled3WeeksIfYesIndicate() {
		return patientTravelled3WeeksIfYesIndicate;
	}

	public void setPatientTravelled3WeeksIfYesIndicate(String patientTravelled3WeeksIfYesIndicate) {
		this.patientTravelled3WeeksIfYesIndicate = patientTravelled3WeeksIfYesIndicate;
	}

	public YesNo getPatientTravelledPeriodOfIllness() {
		return patientTravelledPeriodOfIllness;
	}

	public void setPatientTravelledPeriodOfIllness(YesNo patientTravelledPeriodOfIllness) {
		this.patientTravelledPeriodOfIllness = patientTravelledPeriodOfIllness;
	}

	public String getPatientTravelledIllnessIfYesIndicate() {
		return patientTravelledIllnessIfYesIndicate;
	}

	public void setPatientTravelledIllnessIfYesIndicate(String patientTravelledIllnessIfYesIndicate) {
		this.patientTravelledIllnessIfYesIndicate = patientTravelledIllnessIfYesIndicate;
	}

	public String getOtherPlaces() {
		return otherPlaces;
	}

	public void setOtherPlaces(String otherPlaces) {
		this.otherPlaces = otherPlaces;
	}

	public YesNo getDuring3WeeksPatientContactWithSimilarSymptoms() {
		return during3WeeksPatientContactWithSimilarSymptoms;
	}

	public void setDuring3WeeksPatientContactWithSimilarSymptoms(YesNo during3WeeksPatientContactWithSimilarSymptoms) {
		this.during3WeeksPatientContactWithSimilarSymptoms = during3WeeksPatientContactWithSimilarSymptoms;
	}

	public Date getDateOfContactWithIllPerson() {
		return dateOfContactWithIllPerson;
	}

	public void setDateOfContactWithIllPerson(Date dateOfContactWithIllPerson) {
		this.dateOfContactWithIllPerson = dateOfContactWithIllPerson;
	}

	public YesNo getPatientTouchDomesticWildAnimal() {
		return patientTouchDomesticWildAnimal;
	}

	public void setPatientTouchDomesticWildAnimal(YesNo patientTouchDomesticWildAnimal) {
		this.patientTouchDomesticWildAnimal = patientTouchDomesticWildAnimal;
	}

	public String getPatientTouchDomesticWildAnimalIfYes() {
		return patientTouchDomesticWildAnimalIfYes;
	}

	public void setPatientTouchDomesticWildAnimalIfYes(String patientTouchDomesticWildAnimalIfYes) {
		this.patientTouchDomesticWildAnimalIfYes = patientTouchDomesticWildAnimalIfYes;
	}

	public List<PatientSymptomsPrecedence> getPatientSymptomsPrecedences() {
		return patientSymptomsPrecedences;
	}
	public void setPatientSymptomsPrecedences(List<PatientSymptomsPrecedence> patientSymptomsPrecedences) {
		this.patientSymptomsPrecedences = patientSymptomsPrecedences;
	}

	public String getDuring3WeeksPatientContactWithSimilarSymptomsIfYes() {
		return during3WeeksPatientContactWithSimilarSymptomsIfYes;
	}

	public void setDuring3WeeksPatientContactWithSimilarSymptomsIfYes(String during3WeeksPatientContactWithSimilarSymptomsIfYes) {
		this.during3WeeksPatientContactWithSimilarSymptomsIfYes = during3WeeksPatientContactWithSimilarSymptomsIfYes;
	}

	public List<PatientTravelDetailsDuring> getPatientTravelDetailsDurings() {
		return patientTravelDetailsDurings;
	}

	public void setPatientTravelDetailsDurings(List<PatientTravelDetailsDuring> patientTravelDetailsDurings) {
		this.patientTravelDetailsDurings = patientTravelDetailsDurings;
	}

	public List<PatientTravelDetailsPrior> getPatientTravelDetailsPriors() {
		return patientTravelDetailsPriors;
	}

	public void setPatientTravelDetailsPriors(List<PatientTravelDetailsPrior> patientTravelDetailsPriors) {
		this.patientTravelDetailsPriors = patientTravelDetailsPriors;
	}


	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}



}
