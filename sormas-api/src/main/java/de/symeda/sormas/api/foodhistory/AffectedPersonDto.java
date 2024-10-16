package de.symeda.sormas.api.foodhistory;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

@DependingOnFeatureType(featureType = {
        FeatureType.CASE_SURVEILANCE,
        FeatureType.CONTACT_TRACING })
public class AffectedPersonDto extends PseudonymizableDto {

    public static final String I18N_PREFIX = "ContainmentMeasure";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "affectedperson";
    public static final String NAME_OF_AFFECTED_PERSON = "nameOfAffectedPerson";
    public static final String TEL_NO = "telNo";
    public static final String DATE_TIME = "dateTime";
    public static final String AGE = "age";
//    private String uuid;
    private String nameOfAffectedPerson;
    private String telNo;
    private Date dateTime;
    private String age;

    public static AffectedPersonDto build() {
        AffectedPersonDto affectedPersonDto = new AffectedPersonDto();
        affectedPersonDto.setUuid(DataHelper.createUuid());
        return affectedPersonDto;
    }

    public static AffectedPersonDto build(
            String uuid,
            String nameOfAffectedPerson,
            String telNo,
            Date dateTime,
            String age) {
        AffectedPersonDto affectedPersonDto = new AffectedPersonDto();
        affectedPersonDto.setUuid(uuid);
        affectedPersonDto.setNameOfAffectedPerson(nameOfAffectedPerson);
        affectedPersonDto.setTelNo(telNo);
        affectedPersonDto.setDateTime(dateTime);
        affectedPersonDto.setAge(age);

        return affectedPersonDto;
    }


    @Override
    public String getUuid() {
        return super.getUuid();
    }
    @Override
    public void setUuid(String uuid) {
        super.setUuid(uuid);
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

    @Override
    public AffectedPersonDto clone() throws CloneNotSupportedException {
        AffectedPersonDto clone = (AffectedPersonDto) super.clone();
        return clone;
    }
}
