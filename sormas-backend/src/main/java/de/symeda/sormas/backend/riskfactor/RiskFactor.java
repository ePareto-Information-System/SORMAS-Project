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

package de.symeda.sormas.backend.riskfactor;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.NotExposedToApi;
import de.symeda.sormas.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class RiskFactor extends AbstractDomainObject {
    private static final long serialVersionUID = -8576270649634034245L;

    public static final String TABLE_NAME = "riskfactor";
    private String drinkingWaterSourceOne;
    private String drinkingWaterSourceTwo;
    private String drinkingWaterSourceThree;
    private String drinkingWaterSourceFour;
    private String nonDrinkingWaterSourceOne;
    private String nonDrinkingWaterSourceTwo;
    private String nonDrinkingWaterSourceThree;
    private String nonDrinkingWaterSourceFour;
    private String foodItemsOne;
    private String foodItemsTwo;
    private String foodItemsThree;
    private String foodItemsFour;
    private String foodItemsFive;
    private String foodItemsSix;
    private String foodItemsSeven;
    private String foodItemsEight;
    private YesNo drinkingWaterInfectedByVibrio;
    private YesNo nonDrinkingWaterInfectedByVibrio;
    private YesNo foodItemsInfectedByVibrio;
    private DrinkingWaterSource waterUsedForDrinking;
    private YesNo threeDaysPriorToDiseaseWaterSourceOne;
    private YesNo threeDaysPriorToDiseaseWaterSourceTwo;
    private YesNo threeDaysPriorToDiseaseWaterSourceThree;
    private YesNo threeDaysPriorToDiseaseWaterSourceFour;
    private YesNo threeDaysPriorToDiseaseWaterSourceFive;
    private YesNo threeDaysPriorToDiseaseFoodItemsOne;
    private YesNo threeDaysPriorToDiseaseFoodItemsTwo;
    private YesNo threeDaysPriorToDiseaseFoodItemsThree;
    private YesNo threeDaysPriorToDiseaseFoodItemsFour;
    private YesNo threeDaysPriorToDiseaseFoodItemsFive;
    private YesNo threeDaysPriorToDiseaseAttendAnyFuneral;
    private YesNo threeDaysPriorToDiseaseAttendAnySocialEvent;
    private String otherSocialEventDetails;
    private YesNo patientSpoxVaccinationScarPresent;
    private YesNo patientTravelledAnywhere3WeeksPrior;
    private String patientTravelled3WeeksIfYesIndicate;
    private YesNo patientTravelledPeriodOfIllness;
    private String patientTravelledIllnessIfYesIndicate;
    private String otherPlaces;
    private YesNo during3WeeksPatientContactWithSimilarSymptoms;
    private String during3WeeksPatientContactWithSimilarSymptomsIfYes;
    private Date dateOfContactWithIllPerson;
    private YesNo patientTouchDomesticWildAnimal;
    private String patientTouchDomesticWildAnimalIfYes;
    private CaseOutcome statusOfPatient;
    private Date dateOfDeath;
    private String placeOfDeath;
    private Date dateOfSpecimenCollection;
    private SampleMaterial typeOfSpecimenCollection;
    private String investigatorName;
    private String investigatorTitle;
    private String investigatorAddress;
    private String investigatorTel;
    private String email;
    private List<PatientSymptomsPrecedence> patientSymptomsPrecedence = new ArrayList<>();
    @NotExposedToApi
    private Date changeDateOfEmbeddedLists;


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
    @Enumerated(EnumType.STRING)
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

    public String getDuring3WeeksPatientContactWithSimilarSymptomsIfYes() {
        return during3WeeksPatientContactWithSimilarSymptomsIfYes;
    }

    public void setDuring3WeeksPatientContactWithSimilarSymptomsIfYes(String during3WeeksPatientContactWithSimilarSymptomsIfYes) {
        this.during3WeeksPatientContactWithSimilarSymptomsIfYes = during3WeeksPatientContactWithSimilarSymptomsIfYes;
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

    public CaseOutcome getStatusOfPatient() {
        return statusOfPatient;
    }

    public void setStatusOfPatient(CaseOutcome statusOfPatient) {
        this.statusOfPatient = statusOfPatient;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public Date getDateOfSpecimenCollection() {
        return dateOfSpecimenCollection;
    }

    public void setDateOfSpecimenCollection(Date dateOfSpecimenCollection) {
        this.dateOfSpecimenCollection = dateOfSpecimenCollection;
    }

    public SampleMaterial getTypeOfSpecimenCollection() {
        return typeOfSpecimenCollection;
    }

    public void setTypeOfSpecimenCollection(SampleMaterial typeOfSpecimenCollection) {
        this.typeOfSpecimenCollection = typeOfSpecimenCollection;
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public String getInvestigatorTitle() {
        return investigatorTitle;
    }

    public void setInvestigatorTitle(String investigatorTitle) {
        this.investigatorTitle = investigatorTitle;
    }

    public String getInvestigatorAddress() {
        return investigatorAddress;
    }

    public void setInvestigatorAddress(String investigatorAddress) {
        this.investigatorAddress = investigatorAddress;
    }

    public String getInvestigatorTel() {
        return investigatorTel;
    }

    public void setInvestigatorTel(String investigatorTel) {
        this.investigatorTel = investigatorTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = PatientSymptomsPrecedence.RISK_FACTOR)
    public List<PatientSymptomsPrecedence> getPatientSymptomsPrecedence() {
        return patientSymptomsPrecedence;
    }

    public void setPatientSymptomsPrecedence(List<PatientSymptomsPrecedence> patientSymptomsPrecedence) {
        this.patientSymptomsPrecedence = patientSymptomsPrecedence;
    }

    /**
     * This change date has to be set whenever exposures are modified
     */
    public Date getChangeDateOfEmbeddedLists() {
        return changeDateOfEmbeddedLists;
    }

    public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
        this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
    }
}
