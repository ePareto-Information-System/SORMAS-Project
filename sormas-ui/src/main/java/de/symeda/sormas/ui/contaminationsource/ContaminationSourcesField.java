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

package de.symeda.sormas.ui.contaminationsource;

import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
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
import de.symeda.sormas.ui.epidata.PersonTravelHistoryEditForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.FieldAccessCellStyleGenerator;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.Collection;
import java.util.function.Consumer;

public class ContaminationSourcesField extends AbstractTableField<ContaminationSourceDto> {

//    COLUMN_DATE
    private static final String COLUMN_DATE = Captions.date;
    private static final String NAME = "name";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String TYPE = "type";
    private static final String SOURCE = "source";
    private static final String TREATED_WITH_ABATE = "treatedWithAbate";
    private static final String ABATE_TREATMENT_DATE = "abateTreatmentDate";

    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;


    public ContaminationSourcesField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
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
                LONGITUDE,
                LATITUDE,
                TYPE,
                SOURCE,
                TREATED_WITH_ABATE,
                ABATE_TREATMENT_DATE);


        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(ContaminationSourceDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(ContaminationSourceDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {
//        table.addGeneratedColumn(COLUMN_TRAVEL_PERIOD_TYPE, (Table.ColumnGenerator) (source, itemId, columnId) -> {
//            ContaminationSourceDto personTravelHistoryDto = (ContaminationSourceDto) itemId;
//            String personTravelHistoryString = TravelPeriodType.OTHER != personTravelHistoryDto.getTravelPeriodType()
//                    ? personTravelHistoryDto.getTravelPeriodType().toString()
//                    : TravelPeriodType.OTHER.toString();
//
//            return new Label(personTravelHistoryString, ContentMode.HTML);
//        });

        table.addGeneratedColumn(ABATE_TREATMENT_DATE, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            ContaminationSourceDto contaminationSourceDto = (ContaminationSourceDto) itemId;
            return DateFormatHelper.formatDate(contaminationSourceDto.getAbateTreatmentDate());
        });


       
    }

    @Override
    protected boolean isEmpty(ContaminationSourceDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(ContaminationSourceDto oldEntry, ContaminationSourceDto newEntry) {
        return isModifiedObject(oldEntry.getName(), newEntry.getName())
                || isModifiedObject(oldEntry.getLongitude(), newEntry.getLongitude())
                || isModifiedObject(oldEntry.getLatitude(), newEntry.getLatitude());
    }

    @Override
    public Class<ContaminationSourceDto> getEntryType() {
        return ContaminationSourceDto.class;
    }

    @Override
    protected void editEntry(ContaminationSourceDto entry, boolean create, Consumer<ContaminationSourceDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        ContaminationSourcesEditForm contaminationSourcesEditForm = new ContaminationSourcesEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        contaminationSourcesEditForm.setValue(entry);

        final CommitDiscardWrapperComponent<ContaminationSourcesEditForm> component = new CommitDiscardWrapperComponent<>(
                contaminationSourcesEditForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                contaminationSourcesEditForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityContaminationSource));
        popupWindow.setHeight(90, Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!contaminationSourcesEditForm.getFieldGroup().isModified()) {
                    commitCallback.accept(contaminationSourcesEditForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    ContaminationSourcesField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected ContaminationSourceDto createEntry() {
        UserDto user = UserProvider.getCurrent().getUser();
        ContaminationSourceDto contaminationSourceDto = ContaminationSourceDto.build("WATER");
        return contaminationSourceDto;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<ContaminationSourceDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }
}
