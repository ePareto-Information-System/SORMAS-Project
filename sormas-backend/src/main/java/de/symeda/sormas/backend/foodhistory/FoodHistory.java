package de.symeda.sormas.backend.foodhistory;

import de.symeda.sormas.api.utils.EventType;
import de.symeda.sormas.api.utils.FoodSource;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.affectedperson.AffectedPerson;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.NotExposedToApi;
import de.symeda.sormas.backend.containmentmeasure.ContainmentMeasure;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class FoodHistory extends AbstractDomainObject {

    private static final long serialVersionUID = -8294812479501735786L;

    public static final String TABLE_NAME = "foodhistory";

    private String nameOfAffectedPerson;
    private String nameOfAffectedPerson2;
    private String nameOfAffectedPerson3;
    private String nameOfAffectedPerson4;
    private String telNo;
    private String telNo2;
    private String telNo3;
    private String telNo4;
    private Date dateTime;
    private Date dateTime2;
    private Date dateTime3;
    private Date dateTime4;
    private String age;
    private String age2;
    private String age3;
    private String age4;
    private String suspectedFood;
    private Date dateConsumed;
    private FoodSource foodSource;
    private EventType eventType;
    private String eventOtherSpecify;
    private YesNo breakfast;
    private String totalNoPersons;
    private String foodConsumed;
    private String sourceOfFood;
    private YesNo consumedAtPlace;
    private YesNo lunch;
    private String totalNoPersonsL1;
    private String foodConsumedL1;
    private String sourceOfFoodL1;
    private YesNo consumedAtPlaceL1;
    private YesNo supper;
    private String totalNoPersonsS1;
    private String foodConsumedS1;
    private String sourceOfFoodsS1;
    private YesNo consumedAtPlaceS1;
    private YesNo breakfast2;
    private String totalNoPersons2;
    private String foodConsumed2;
    private String sourceOfFood2;
    private YesNo consumedAtPlace2;
    private YesNo lunchL2;
    private String totalNoPersonsL2;
    private String foodConsumedL2;
    private String sourceOfFoodL2;
    private YesNo consumedAtPlaceL2;
    private YesNo supperS2;
    private String totalNoPersonsS2;
    private String foodConsumedS2;
    private String sourceOfFoodS2;
    private YesNo consumedAtPlaceS2;
    private YesNo breakfast3;
    private String totalNoPersons3;
    private String foodConsumed3;
    private String sourceOfFood3;
    private YesNo consumedAtPlace3;
    private YesNo lunchL3;
    private String totalNoPersonsL3;
    private String foodConsumedL3;
    private String sourceOfFoodL3;
    private YesNo consumedAtPlaceL3;
    private YesNo supperS3;
    private String totalNoPersonsS3;
    private String foodConsumedS3;
    private String sourceOfFoodS3;
    private YesNo consumedAtPlaceS3;
    private Integer numberOfPeopleAteImplicatedFood;
    private Integer numberAffected;
    private List<AffectedPerson> affectedPersons = new ArrayList<>();
    @NotExposedToApi
    private Date changeDateOfEmbeddedLists;

    public String getNameOfAffectedPerson() {
        return nameOfAffectedPerson;
    }

    public void setNameOfAffectedPerson(String nameOfAffectedPerson) {
        this.nameOfAffectedPerson = nameOfAffectedPerson;
    }

    public String getNameOfAffectedPerson2() {
        return nameOfAffectedPerson2;
    }

    public void setNameOfAffectedPerson2(String nameOfAffectedPerson2) {
        this.nameOfAffectedPerson2 = nameOfAffectedPerson2;
    }

    public String getNameOfAffectedPerson3() {
        return nameOfAffectedPerson3;
    }

    public void setNameOfAffectedPerson3(String nameOfAffectedPerson3) {
        this.nameOfAffectedPerson3 = nameOfAffectedPerson3;
    }

    public String getNameOfAffectedPerson4() {
        return nameOfAffectedPerson4;
    }

    public void setNameOfAffectedPerson4(String nameOfAffectedPerson4) {
        this.nameOfAffectedPerson4 = nameOfAffectedPerson4;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTelNo2() {
        return telNo2;
    }

    public void setTelNo2(String telNo2) {
        this.telNo2 = telNo2;
    }

    public String getTelNo3() {
        return telNo3;
    }

    public void setTelNo3(String telNo3) {
        this.telNo3 = telNo3;
    }

    public String getTelNo4() {
        return telNo4;
    }

    public void setTelNo4(String telNo4) {
        this.telNo4 = telNo4;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateTime2() {
        return dateTime2;
    }

    public void setDateTime2(Date dateTime2) {
        this.dateTime2 = dateTime2;
    }

    public Date getDateTime3() {
        return dateTime3;
    }

    public void setDateTime3(Date dateTime3) {
        this.dateTime3 = dateTime3;
    }

    public Date getDateTime4() {
        return dateTime4;
    }

    public void setDateTime4(Date dateTime4) {
        this.dateTime4 = dateTime4;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge2() {
        return age2;
    }

    public void setAge2(String age2) {
        this.age2 = age2;
    }

    public String getAge3() {
        return age3;
    }

    public void setAge3(String age3) {
        this.age3 = age3;
    }

    public String getAge4() {
        return age4;
    }

    public void setAge4(String age4) {
        this.age4 = age4;
    }

    public String getSuspectedFood() {
        return suspectedFood;
    }

    public void setSuspectedFood(String suspectedFood) {
        this.suspectedFood = suspectedFood;
    }

    public Date getDateConsumed() {
        return dateConsumed;
    }

    public void setDateConsumed(Date dateConsumed) {
        this.dateConsumed = dateConsumed;
    }

    public FoodSource getFoodSource() {
        return foodSource;
    }

    public void setFoodSource(FoodSource foodSource) {
        this.foodSource = foodSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventOtherSpecify() {
        return eventOtherSpecify;
    }

    public void setEventOtherSpecify(String eventOtherSpecify) {
        this.eventOtherSpecify = eventOtherSpecify;
    }

    public YesNo getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(YesNo breakfast) {
        this.breakfast = breakfast;
    }

    public String getTotalNoPersons() {
        return totalNoPersons;
    }

    public void setTotalNoPersons(String totalNoPersons) {
        this.totalNoPersons = totalNoPersons;
    }

    public String getFoodConsumed() {
        return foodConsumed;
    }

    public void setFoodConsumed(String foodConsumed) {
        this.foodConsumed = foodConsumed;
    }

    public String getSourceOfFood() {
        return sourceOfFood;
    }

    public void setSourceOfFood(String sourceOfFood) {
        this.sourceOfFood = sourceOfFood;
    }

    public YesNo getConsumedAtPlace() {
        return consumedAtPlace;
    }

    public void setConsumedAtPlace(YesNo consumedAtPlace) {
        this.consumedAtPlace = consumedAtPlace;
    }

    public YesNo getLunch() {
        return lunch;
    }

    public void setLunch(YesNo lunch) {
        this.lunch = lunch;
    }

    public String getTotalNoPersonsL1() {
        return totalNoPersonsL1;
    }

    public void setTotalNoPersonsL1(String totalNoPersonsL1) {
        this.totalNoPersonsL1 = totalNoPersonsL1;
    }

    public String getFoodConsumedL1() {
        return foodConsumedL1;
    }

    public void setFoodConsumedL1(String foodConsumedL1) {
        this.foodConsumedL1 = foodConsumedL1;
    }

    public String getSourceOfFoodL1() {
        return sourceOfFoodL1;
    }

    public void setSourceOfFoodL1(String sourceOfFoodL1) {
        this.sourceOfFoodL1 = sourceOfFoodL1;
    }

    public YesNo getConsumedAtPlaceL1() {
        return consumedAtPlaceL1;
    }

    public void setConsumedAtPlaceL1(YesNo consumedAtPlaceL1) {
        this.consumedAtPlaceL1 = consumedAtPlaceL1;
    }

    public YesNo getSupper() {
        return supper;
    }

    public void setSupper(YesNo supper) {
        this.supper = supper;
    }

    public String getTotalNoPersonsS1() {
        return totalNoPersonsS1;
    }

    public void setTotalNoPersonsS1(String totalNoPersonsS1) {
        this.totalNoPersonsS1 = totalNoPersonsS1;
    }

    public String getFoodConsumedS1() {
        return foodConsumedS1;
    }

    public void setFoodConsumedS1(String foodConsumedS1) {
        this.foodConsumedS1 = foodConsumedS1;
    }

    public String getSourceOfFoodsS1() {
        return sourceOfFoodsS1;
    }

    public void setSourceOfFoodsS1(String sourceOfFoodsS1) {
        this.sourceOfFoodsS1 = sourceOfFoodsS1;
    }

    public YesNo getConsumedAtPlaceS1() {
        return consumedAtPlaceS1;
    }

    public void setConsumedAtPlaceS1(YesNo consumedAtPlaceS1) {
        this.consumedAtPlaceS1 = consumedAtPlaceS1;
    }

    public YesNo getBreakfast2() {
        return breakfast2;
    }

    public void setBreakfast2(YesNo breakfast2) {
        this.breakfast2 = breakfast2;
    }

    public String getTotalNoPersons2() {
        return totalNoPersons2;
    }

    public void setTotalNoPersons2(String totalNoPersons2) {
        this.totalNoPersons2 = totalNoPersons2;
    }

    public String getFoodConsumed2() {
        return foodConsumed2;
    }

    public void setFoodConsumed2(String foodConsumed2) {
        this.foodConsumed2 = foodConsumed2;
    }

    public String getSourceOfFood2() {
        return sourceOfFood2;
    }

    public void setSourceOfFood2(String sourceOfFood2) {
        this.sourceOfFood2 = sourceOfFood2;
    }

    public YesNo getConsumedAtPlace2() {
        return consumedAtPlace2;
    }

    public void setConsumedAtPlace2(YesNo consumedAtPlace2) {
        this.consumedAtPlace2 = consumedAtPlace2;
    }

    public YesNo getLunchL2() {
        return lunchL2;
    }

    public void setLunchL2(YesNo lunchL2) {
        this.lunchL2 = lunchL2;
    }

    public String getTotalNoPersonsL2() {
        return totalNoPersonsL2;
    }

    public void setTotalNoPersonsL2(String totalNoPersonsL2) {
        this.totalNoPersonsL2 = totalNoPersonsL2;
    }

    public String getFoodConsumedL2() {
        return foodConsumedL2;
    }

    public void setFoodConsumedL2(String foodConsumedL2) {
        this.foodConsumedL2 = foodConsumedL2;
    }

    public String getSourceOfFoodL2() {
        return sourceOfFoodL2;
    }

    public void setSourceOfFoodL2(String sourceOfFoodL2) {
        this.sourceOfFoodL2 = sourceOfFoodL2;
    }

    public YesNo getConsumedAtPlaceL2() {
        return consumedAtPlaceL2;
    }

    public void setConsumedAtPlaceL2(YesNo consumedAtPlaceL2) {
        this.consumedAtPlaceL2 = consumedAtPlaceL2;
    }

    public YesNo getSupperS2() {
        return supperS2;
    }

    public void setSupperS2(YesNo supperS2) {
        this.supperS2 = supperS2;
    }

    public String getTotalNoPersonsS2() {
        return totalNoPersonsS2;
    }

    public void setTotalNoPersonsS2(String totalNoPersonsS2) {
        this.totalNoPersonsS2 = totalNoPersonsS2;
    }

    public String getFoodConsumedS2() {
        return foodConsumedS2;
    }

    public void setFoodConsumedS2(String foodConsumedS2) {
        this.foodConsumedS2 = foodConsumedS2;
    }

    public String getSourceOfFoodS2() {
        return sourceOfFoodS2;
    }

    public void setSourceOfFoodS2(String sourceOfFoodS2) {
        this.sourceOfFoodS2 = sourceOfFoodS2;
    }

    public YesNo getConsumedAtPlaceS2() {
        return consumedAtPlaceS2;
    }

    public void setConsumedAtPlaceS2(YesNo consumedAtPlaceS2) {
        this.consumedAtPlaceS2 = consumedAtPlaceS2;
    }

    public YesNo getBreakfast3() {
        return breakfast3;
    }

    public void setBreakfast3(YesNo breakfast3) {
        this.breakfast3 = breakfast3;
    }

    public String getTotalNoPersons3() {
        return totalNoPersons3;
    }

    public void setTotalNoPersons3(String totalNoPersons3) {
        this.totalNoPersons3 = totalNoPersons3;
    }

    public String getFoodConsumed3() {
        return foodConsumed3;
    }

    public void setFoodConsumed3(String foodConsumed3) {
        this.foodConsumed3 = foodConsumed3;
    }

    public String getSourceOfFood3() {
        return sourceOfFood3;
    }

    public void setSourceOfFood3(String sourceOfFood3) {
        this.sourceOfFood3 = sourceOfFood3;
    }

    public YesNo getConsumedAtPlace3() {
        return consumedAtPlace3;
    }

    public void setConsumedAtPlace3(YesNo consumedAtPlace3) {
        this.consumedAtPlace3 = consumedAtPlace3;
    }

    public YesNo getLunchL3() {
        return lunchL3;
    }

    public void setLunchL3(YesNo lunchL3) {
        this.lunchL3 = lunchL3;
    }

    public String getTotalNoPersonsL3() {
        return totalNoPersonsL3;
    }

    public void setTotalNoPersonsL3(String totalNoPersonsL3) {
        this.totalNoPersonsL3 = totalNoPersonsL3;
    }

    public String getFoodConsumedL3() {
        return foodConsumedL3;
    }

    public void setFoodConsumedL3(String foodConsumedL3) {
        this.foodConsumedL3 = foodConsumedL3;
    }

    public String getSourceOfFoodL3() {
        return sourceOfFoodL3;
    }

    public void setSourceOfFoodL3(String sourceOfFoodL3) {
        this.sourceOfFoodL3 = sourceOfFoodL3;
    }

    public YesNo getConsumedAtPlaceL3() {
        return consumedAtPlaceL3;
    }

    public void setConsumedAtPlaceL3(YesNo consumedAtPlaceL3) {
        this.consumedAtPlaceL3 = consumedAtPlaceL3;
    }

    public YesNo getSupperS3() {
        return supperS3;
    }

    public void setSupperS3(YesNo supperS3) {
        this.supperS3 = supperS3;
    }

    public String getTotalNoPersonsS3() {
        return totalNoPersonsS3;
    }

    public void setTotalNoPersonsS3(String totalNoPersonsS3) {
        this.totalNoPersonsS3 = totalNoPersonsS3;
    }

    public String getFoodConsumedS3() {
        return foodConsumedS3;
    }

    public void setFoodConsumedS3(String foodConsumedS3) {
        this.foodConsumedS3 = foodConsumedS3;
    }

    public String getSourceOfFoodS3() {
        return sourceOfFoodS3;
    }

    public void setSourceOfFoodS3(String sourceOfFoodS3) {
        this.sourceOfFoodS3 = sourceOfFoodS3;
    }

    public YesNo getConsumedAtPlaceS3() {
        return consumedAtPlaceS3;
    }

    public void setConsumedAtPlaceS3(YesNo consumedAtPlaceS3) {
        this.consumedAtPlaceS3 = consumedAtPlaceS3;
    }

    public Integer getNumberOfPeopleAteImplicatedFood() {
        return numberOfPeopleAteImplicatedFood;
    }

    public void setNumberOfPeopleAteImplicatedFood(Integer numberOfPeopleAteImplicatedFood) {
        this.numberOfPeopleAteImplicatedFood = numberOfPeopleAteImplicatedFood;
    }

    public Integer getNumberAffected() {
        return numberAffected;
    }

    public void setNumberAffected(Integer numberAffected) {
        this.numberAffected = numberAffected;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = AffectedPerson.FOOD_HISTORY)
    public List<AffectedPerson> getAffectedPersons() {
        return affectedPersons;
    }

    public void setAffectedPersons(List<AffectedPerson> affectedPersons) {
        this.affectedPersons = affectedPersons;
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
