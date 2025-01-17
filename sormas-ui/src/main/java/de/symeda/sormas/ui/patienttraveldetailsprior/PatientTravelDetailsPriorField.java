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

package de.symeda.sormas.ui.patienttraveldetailsprior;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsPriorDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.caze.AbstractTableField;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.FieldAccessCellStyleGenerator;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.Collection;
import java.util.function.Consumer;

public class PatientTravelDetailsPriorField extends AbstractTableField<PatientTravelDetailsPriorDto> {

    public static final String DATE_OF_TRAVEL = "dateOfTravel";
    public static final String PLACE_OF_TRAVEL = "placeOfTravel";
    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;

    public PatientTravelDetailsPriorField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
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
                DATE_OF_TRAVEL,
                PLACE_OF_TRAVEL
        );

        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(PatientTravelDetailsPriorDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(PatientTravelDetailsPriorDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {

        table.addGeneratedColumn(DATE_OF_TRAVEL, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            PatientTravelDetailsPriorDto patientTravelDetailsPriorDto = (PatientTravelDetailsPriorDto) itemId;
            return DateFormatHelper.formatDate(patientTravelDetailsPriorDto.getDateOfTravel());
        });

        table.addGeneratedColumn(PLACE_OF_TRAVEL, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            PatientTravelDetailsPriorDto patientTravelDetailsPriorDto = (PatientTravelDetailsPriorDto) itemId;
            return String.valueOf(patientTravelDetailsPriorDto.getPlaceOfTravel());
        });

    }

    @Override
    protected boolean isEmpty(PatientTravelDetailsPriorDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(PatientTravelDetailsPriorDto oldEntry, PatientTravelDetailsPriorDto newEntry) {
        return isModifiedObject(oldEntry.getDateOfTravel(), newEntry.getDateOfTravel())
                || isModifiedObject(oldEntry.getPlaceOfTravel(), newEntry.getPlaceOfTravel());
    }

    @Override
    public Class<PatientTravelDetailsPriorDto> getEntryType() {
        return PatientTravelDetailsPriorDto.class;
    }

    @Override
    protected void editEntry(PatientTravelDetailsPriorDto entry, boolean create, Consumer<PatientTravelDetailsPriorDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        PatientTravelDetailsPriorEditForm patientTravelDetailsPriorEditForm = new PatientTravelDetailsPriorEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        patientTravelDetailsPriorEditForm.setValue(entry);

        final CommitDiscardWrapperComponent<PatientTravelDetailsPriorEditForm> component = new CommitDiscardWrapperComponent<>(
                patientTravelDetailsPriorEditForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                patientTravelDetailsPriorEditForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityPatientTravelDetailsPrior));
        popupWindow.setHeight(90, Sizeable.Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!patientTravelDetailsPriorEditForm.getFieldGroup().isModified()) {
                    commitCallback.accept(patientTravelDetailsPriorEditForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    PatientTravelDetailsPriorField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected PatientTravelDetailsPriorDto createEntry() {
        PatientTravelDetailsPriorDto patientTravelDetailsPriorDto = new PatientTravelDetailsPriorDto();
        return patientTravelDetailsPriorDto;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<PatientTravelDetailsPriorDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }
}
