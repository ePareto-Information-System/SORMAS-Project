package de.symeda.sormas.backend.affectedperson;

import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.foodhistory.FoodHistory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "affectedperson")
public class AffectedPerson extends AbstractDomainObject {

    private static final long serialVersionUID = -5570515874416024602L;

    public static final String TABLE_NAME = "affectedperson";
    public static final String FOOD_HISTORY = "foodHistory";
    public static final String NAME_OF_AFFECTED_PERSON = "nameOfAffectedPerson";
    public static final String TEL_NO = "telNo";
    public static final String DATE_TIME = "dateTime";
    public static final String AGE = "age";

    private FoodHistory foodHistory;
    private String uuid;
    private String nameOfAffectedPerson;
    private String telNo;
    private Date dateTime;
    private String age;

    public AffectedPerson() {
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public FoodHistory getFoodHistory() {
        return foodHistory;
    }

    public void setFoodHistory(FoodHistory foodHistory) {
        this.foodHistory = foodHistory;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNameOfAffectedPerson() {
        return nameOfAffectedPerson;
    }

    public void setNameOfAffectedPerson(String nameOfAffectedPerson) {
        this.nameOfAffectedPerson = nameOfAffectedPerson;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
