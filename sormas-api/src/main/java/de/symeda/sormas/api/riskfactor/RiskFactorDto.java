package de.symeda.sormas.api.riskfactor;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class RiskFactorDto extends PseudonymizableDto {
    private static final long serialVersionUID = 4846215199480684370L;

    public static final String I18N_PREFIX = "RiskFactor";

    public static final String DRINKING_WATER_SOURCE_ONE = "drinkingWaterSourceOne";
    public static final String DRINKING_WATER_SOURCE_TWO = "drinkingWaterSourceTwo";
    public static final String DRINKING_WATER_SOURCE_THREE = "drinkingWaterSourceThree";
    public static final String DRINKING_WATER_SOURCE_FOUR = "drinkingWaterSourceFour";
    public static final String NON_DRINKING_WATER_SOURCE_ONE = "nonDrinkingWaterSourceOne";
    public static final String NON_DRINKING_WATER_SOURCE_TWO = "nonDrinkingWaterSourceTwo";
    public static final String NON_DRINKING_WATER_SOURCE_THREE = "nonDrinkingWaterSourceThree";
    public static final String NON_DRINKING_WATER_SOURCE_FOUR = "nonDrinkingWaterSourceFour";
    public static final String FOOD_ITEMS_ONE = "foodItemsOne";
    public static final String FOOD_ITEMS_TWO = "foodItemsTwo";
    public static final String FOOD_ITEMS_THREE = "foodItemsThree";
    public static final String FOOD_ITEMS_FOUR = "foodItemsFour";
    public static final String FOOD_ITEMS_FIVE = "foodItemsFive";
    public static final String FOOD_ITEMS_SIX = "foodItemsSix";
    public static final String FOOD_ITEMS_SEVEN = "foodItemsSeven";
    public static final String FOOD_ITEMS_EIGHT = "foodItemsEight";
    public static final String DRINKING_WATER_INFECTED_BY_VIBRIO = "drinkingWaterInfectedByVibrio";
    public static final String NON_DRINKING_WATER_INFECTED_BY_VIBRIO = "nonDrinkingWaterInfectedByVibrio";
    public static final String FOOD_ITEMS_INFECTED_BY_VIBRIO = "foodItemsInfectedByVibrio";
    public static final String WATER_USED_FOR_DRINKING = "waterUsedForDrinking";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE = "threeDaysPriorToDiseaseWaterSourceOne";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO = "threeDaysPriorToDiseaseWaterSourceTwo";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE = "threeDaysPriorToDiseaseWaterSourceThree";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR = "threeDaysPriorToDiseaseWaterSourceFour";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE = "threeDaysPriorToDiseaseWaterSourceFive";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE = "threeDaysPriorToDiseaseFoodItemsOne";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO = "threeDaysPriorToDiseaseFoodItemsTwo";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE = "threeDaysPriorToDiseaseFoodItemsThree";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR = "threeDaysPriorToDiseaseFoodItemsFour";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE = "threeDaysPriorToDiseaseFoodItemsFive";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL = "threeDaysPriorToDiseaseAttendAnyFuneral";
    public static final String THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT = "threeDaysPriorToDiseaseAttendAnySocialEvent";
    public static final String OTHER_SOCIAL_EVENT_DETAILS = "otherSocialEventDetails";


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
    private YesNo vibrioCholeraeIdentifiedInStools;
    private YesNo drugsSensitiveToVibrioStrain;
    private YesNo drugsResistantToVibrioStrain;

    public static RiskFactorDto build() {
        RiskFactorDto riskFactorDto  = new RiskFactorDto();
        riskFactorDto.setUuid(DataHelper.createUuid());
        return riskFactorDto;
    }

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

    public YesNo getVibrioCholeraeIdentifiedInStools() {
        return vibrioCholeraeIdentifiedInStools;
    }

    public void setVibrioCholeraeIdentifiedInStools(YesNo vibrioCholeraeIdentifiedInStools) {
        this.vibrioCholeraeIdentifiedInStools = vibrioCholeraeIdentifiedInStools;
    }

    public YesNo getDrugsSensitiveToVibrioStrain() {
        return drugsSensitiveToVibrioStrain;
    }

    public void setDrugsSensitiveToVibrioStrain(YesNo drugsSensitiveToVibrioStrain) {
        this.drugsSensitiveToVibrioStrain = drugsSensitiveToVibrioStrain;
    }

    public YesNo getDrugsResistantToVibrioStrain() {
        return drugsResistantToVibrioStrain;
    }

    public void setDrugsResistantToVibrioStrain(YesNo drugsResistantToVibrioStrain) {
        this.drugsResistantToVibrioStrain = drugsResistantToVibrioStrain;
    }
}
