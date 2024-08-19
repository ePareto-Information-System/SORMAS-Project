package de.symeda.sormas.app.backend.formbuilder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Entity;

import de.symeda.sormas.app.backend.formfield.FormField;

@Entity(name = FormBuilderFormField.TABLE_NAME)
@DatabaseTable(tableName = FormBuilderFormField.TABLE_NAME)
public class FormBuilderFormField {

    public static final String TABLE_NAME = "forms_form_fields";
    public static final String FORM_BUILDER = FormBuilder.TABLE_NAME;
    public static final String FORM_FIELD = FormField.TABLE_NAME;

    @DatabaseField(foreign = true, columnName = "form_id")
    private FormBuilder formBuilder;

    @DatabaseField(foreign = true, columnName = "formField_id")
    private FormField formField;

    //Needed for dto serialization
    public FormBuilderFormField() {
    }

    public FormBuilderFormField(FormBuilder formBuilder, FormField formField) {
        this.formBuilder = formBuilder;
        this.formField = formField;
    }

    public FormField getFormField() {
        return formField;
    }

    public void setFormField(FormField formField) {
        this.formField = formField;
    }

    public FormBuilder getFormBuilder() {
        return formBuilder;
    }

    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }
}