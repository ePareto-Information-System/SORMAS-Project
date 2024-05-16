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

package de.symeda.sormas.ui.containmentmeasure;

import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;
import de.symeda.sormas.api.epidata.ContainmentMeasureDto;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.api.epidata.TravelPeriodType;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.caze.AbstractTableField;
import de.symeda.sormas.ui.contaminationsource.ContaminationSourcesEditForm;
import de.symeda.sormas.ui.epidata.PersonTravelHistoryEditForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.FieldAccessCellStyleGenerator;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.Collection;
import java.util.function.Consumer;

public class ContainmentMeasureField extends AbstractTableField<ContainmentMeasureDto> {

//    COLUMN_DATE
    private static final String COLUMN_DATE = Captions.date;
    private static final String LOCATION_OF_WORM = ContainmentMeasureDto.LOCATION_OF_WORM;
    private static final String DATE_WORM_DETECTED_EMERGENCE = ContainmentMeasureDto.DATE_WORM_DETECTED_EMERGENCE;
    private static final String DATE_WORM_DETECT_BY_SUPERVISOR = ContainmentMeasureDto.DATE_WORM_DETECT_BY_SUPERVISOR;
    private static final String DATE_CONFIRMED = ContainmentMeasureDto.DATE_CONFIRMED;
    private static final String DATE_OF_GUINEA_WORM_EXPULLED = ContainmentMeasureDto.DATE_OF_GUINEA_WORM_EXPULLED;
    private static final String REGULAR_BANDAGING = ContainmentMeasureDto.REGULAR_BANDAGING;
    private static final String COMPLETELY_EXTRACTED = ContainmentMeasureDto.COMPLETELY_EXTRACTED;


    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;


    public ContainmentMeasureField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
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
                LOCATION_OF_WORM,
                DATE_WORM_DETECTED_EMERGENCE,
                DATE_WORM_DETECT_BY_SUPERVISOR,
                DATE_CONFIRMED,
                DATE_OF_GUINEA_WORM_EXPULLED,
                REGULAR_BANDAGING,
                COMPLETELY_EXTRACTED
        );


        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(ContainmentMeasureDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(ContainmentMeasureDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {

        table.addGeneratedColumn(DATE_WORM_DETECTED_EMERGENCE, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            ContainmentMeasureDto containmentMeasure = (ContainmentMeasureDto) itemId;
            return DateFormatHelper.formatDate(containmentMeasure.getDateWormDetectedEmergence());
        });

        table.addGeneratedColumn(DATE_WORM_DETECT_BY_SUPERVISOR, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            ContainmentMeasureDto containmentMeasure = (ContainmentMeasureDto) itemId;
            return DateFormatHelper.formatDate(containmentMeasure.getDateWormDetectBySupervisor());
        });

        table.addGeneratedColumn(DATE_CONFIRMED, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            ContainmentMeasureDto containmentMeasure = (ContainmentMeasureDto) itemId;
            return DateFormatHelper.formatDate(containmentMeasure.getDateConfirmed());
        });

        table.addGeneratedColumn(DATE_OF_GUINEA_WORM_EXPULLED, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            ContainmentMeasureDto containmentMeasure = (ContainmentMeasureDto) itemId;
            return DateFormatHelper.formatDate(containmentMeasure.getDateOfGuineaWormExpelled());
        });



    }

    @Override
    protected boolean isEmpty(ContainmentMeasureDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(ContainmentMeasureDto oldEntry, ContainmentMeasureDto newEntry) {
        return isModifiedObject(oldEntry.getLocationOfWorm(), newEntry.getLocationOfWorm())
                || isModifiedObject(oldEntry.getDateWormDetectedEmergence(), newEntry.getDateWormDetectedEmergence())
                || isModifiedObject(oldEntry.getDateWormDetectBySupervisor(), newEntry.getDateWormDetectBySupervisor());
    }

    @Override
    public Class<ContainmentMeasureDto> getEntryType() {
        return ContainmentMeasureDto.class;
    }

    @Override
    protected void editEntry(ContainmentMeasureDto entry, boolean create, Consumer<ContainmentMeasureDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        ContainmentMeasureEditForm containmentMeasureEditForm = new ContainmentMeasureEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        containmentMeasureEditForm.setValue(entry);

        final CommitDiscardWrapperComponent<ContainmentMeasureEditForm> component = new CommitDiscardWrapperComponent<>(
                containmentMeasureEditForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                containmentMeasureEditForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityContainmentMeasure));
        popupWindow.setHeight(90, Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!containmentMeasureEditForm.getFieldGroup().isModified()) {
                    commitCallback.accept(containmentMeasureEditForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    ContainmentMeasureField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected ContainmentMeasureDto createEntry() {
        UserDto user = UserProvider.getCurrent().getUser();
        ContainmentMeasureDto containmentMeasureDto = new ContainmentMeasureDto();
        return containmentMeasureDto;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<ContainmentMeasureDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }
}
