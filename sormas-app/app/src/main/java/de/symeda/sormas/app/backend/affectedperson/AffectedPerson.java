package de.symeda.sormas.app.backend.affectedperson;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.foodhistory.FoodHistory;

@Entity(name = AffectedPerson.TABLE_NAME)
@DatabaseTable(tableName = AffectedPerson.TABLE_NAME)
@EmbeddedAdo(parentAccessor = AffectedPerson.FOOD_HISTORY)
public class AffectedPerson extends PseudonymizableAdo {

    private static final long serialVersionUID = -5570515874416024602L;

    public static final String TABLE_NAME = "affectedperson";
    public static final String I18N_PREFIX = "AffectedPerson";
    public static final String FOOD_HISTORY = "foodHistory";

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private FoodHistory foodHistory;
    private String uuid;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String nameOfAffectedPerson;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String telNo;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateTime;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String age;

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public FoodHistory getFoodHistory() {
        return foodHistory;
    }

    public void setFoodHistory(FoodHistory foodHistory) {
        this.foodHistory = foodHistory;
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
