package de.symeda.sormas.ui.formbuilder;

import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.formbuilder.FormBuilderIndexDto;
import de.symeda.sormas.api.task.TaskDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import java.util.ArrayList;

public class FormBuilderEditForm extends AbstractEditForm<FormBuilderIndexDto> {

    private static final long serialVersionUID = 1L;
    private UserRight editOrCreateUserRight;
    private Disease disease;
    private boolean hideValidationUntilNextCommit = false;
    public FormBuilderEditForm(boolean create, boolean editedFromTaskGrid, Disease disease) {

        super(FormBuilderIndexDto.class, TaskDto.I18N_PREFIX, false, FieldVisibilityCheckers.withDisease(disease));

        this.editOrCreateUserRight = create ? UserRight.FORM_BUILDER_CREATE : UserRight.FORM_BUILDER_EDIT;
        this.disease = disease;


        setWidth(680, Sizeable.Unit.PIXELS);

        if (create) {
            hideValidationUntilNextCommit();
        }

        //addFields();
    }

    @Override
    protected String createHtmlLayout() {
        return null;
    }

    @Override
    protected void addFields() {

    }


    public void hideValidationUntilNextCommit() {

        this.hideValidationUntilNextCommit = true;

        for (Field<?> field : getFieldGroup().getFields()) {
            if (field instanceof AbstractField) {
                AbstractField<?> abstractField = (AbstractField<?>) field;
                if (!abstractField.isInvalidCommitted()) {
                    abstractField.setValidationVisible(false);
                }
            }
        }

        for (Field<?> field : getCustomFields()) {
            if (field instanceof AbstractField) {
                AbstractField<?> abstractField = (AbstractField<?>) field;
                if (!abstractField.isInvalidCommitted()) {
                    abstractField.setValidationVisible(false);
                }
            }
        }
    }
}
