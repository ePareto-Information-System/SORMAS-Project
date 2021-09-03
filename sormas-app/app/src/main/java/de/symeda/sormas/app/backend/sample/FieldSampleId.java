package de.symeda.sormas.app.backend.sample;


import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.outbreak.Outbreak;

import static de.symeda.sormas.api.EntityDto.COLUMN_LENGTH_DEFAULT;

@Entity(name = FieldSampleId.TABLE_NAME)
@DatabaseTable(tableName = FieldSampleId.TABLE_NAME)
public class FieldSampleId extends AbstractDomainObject {
    public static final String TABLE_NAME = "fieldSampleId";
    public static final String I18N_PREFIX = "FieldSampleId";
    public static final String FIELD_SAMPLE_ID = "fieldSampleId";

    @Column(length = COLUMN_LENGTH_DEFAULT)
    private String fieldSampleId;

    public String getFieldSampleId() {
        return fieldSampleId;
    }

    public void setFieldSampleId(String fieldSampleId) {
        this.fieldSampleId = fieldSampleId;
    }
}
