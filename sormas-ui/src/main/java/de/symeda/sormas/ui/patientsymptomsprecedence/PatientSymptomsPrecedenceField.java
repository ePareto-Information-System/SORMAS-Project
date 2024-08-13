/*
 * ******************************************************************************
 * * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.symeda.sormas.ui.patientsymptomsprecedence;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.riskfactor.PatientSymptomsPrecedenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.caze.AbstractTableField;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.FieldAccessCellStyleGenerator;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.Collection;
import java.util.function.Consumer;

public class PatientSymptomsPrecedenceField extends AbstractTableField<PatientSymptomsPrecedenceDto>{

    public static final String NAME = "name";
    public static final String CONTACT_ADDRESS = "contactAddress";
    public static final String PHONE = "Phone";

    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;

    public PatientSymptomsPrecedenceField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
        super(fieldAccessCheckers, isEditAllowed);
        this.fieldVisibilityCheckers = fieldVisibilityCheckers;
        this.isEditAllowed = isEditAllowed;
    }

    @Override
    protected void updateColumns() {
        Table table = getTable();

        addGeneratedColumns(table);

        table.setVisibleColumns(
                ACTION_COLUMN_ID,
                NAME,
                CONTACT_ADDRESS,
                PHONE
        );

        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(PatientSymptomsPrecedenceDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(PatientSymptomsPrecedenceDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {

        table.addGeneratedColumn(NAME, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = (PatientSymptomsPrecedenceDto) itemId;
            return String.valueOf(patientSymptomsPrecedenceDto.getName());
        });

        table.addGeneratedColumn(CONTACT_ADDRESS, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = (PatientSymptomsPrecedenceDto) itemId;
            return String.valueOf(patientSymptomsPrecedenceDto.getContactAddress());
        });

        table.addGeneratedColumn(PHONE, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = (PatientSymptomsPrecedenceDto) itemId;
            return String.valueOf(patientSymptomsPrecedenceDto.getPhone());
        });


    }

    @Override
    protected boolean isEmpty(PatientSymptomsPrecedenceDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(PatientSymptomsPrecedenceDto oldEntry, PatientSymptomsPrecedenceDto newEntry) {
        return isModifiedObject(oldEntry.getName(), newEntry.getName())
                || isModifiedObject(oldEntry.getContactAddress(), newEntry.getContactAddress())
                || isModifiedObject(oldEntry.getPhone(), newEntry.getPhone());
    }

    @Override
    public Class<PatientSymptomsPrecedenceDto> getEntryType() {
        return PatientSymptomsPrecedenceDto.class;
    }


    @Override
    protected void editEntry(PatientSymptomsPrecedenceDto entry, boolean create, Consumer<PatientSymptomsPrecedenceDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        PatientSymptomsPrecedenceEditForm patientSymptomsPrecedenceEditForm = new PatientSymptomsPrecedenceEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        patientSymptomsPrecedenceEditForm.setValue(entry);

        final CommitDiscardWrapperComponent<PatientSymptomsPrecedenceEditForm> component = new CommitDiscardWrapperComponent<>(
                patientSymptomsPrecedenceEditForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                patientSymptomsPrecedenceEditForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityPatientSymptomsPrecedence));
        popupWindow.setHeight(90, Sizeable.Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!patientSymptomsPrecedenceEditForm.getFieldGroup().isModified()) {
                    commitCallback.accept(patientSymptomsPrecedenceEditForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    PatientSymptomsPrecedenceField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected PatientSymptomsPrecedenceDto createEntry() {
        PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = new PatientSymptomsPrecedenceDto();
        return patientSymptomsPrecedenceDto;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<PatientSymptomsPrecedenceDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }
}
