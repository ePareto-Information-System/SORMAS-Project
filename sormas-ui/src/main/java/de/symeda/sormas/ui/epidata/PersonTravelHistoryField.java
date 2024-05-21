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

package de.symeda.sormas.ui.epidata;

import java.util.Collection;
import java.util.function.Consumer;

import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.api.epidata.TravelPeriodType;

import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;

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
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.FieldAccessCellStyleGenerator;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class PersonTravelHistoryField extends AbstractTableField<PersonTravelHistoryDto> {

//    COLUMN_DATE
    private static final String COLUMN_DATE = Captions.date;
    private static final String COLUMN_TRAVEL_PERIOD_TYPE = PersonTravelHistoryDto.TRAVEL_PERIOD_TYPE;
    private static final String DATE_FROM = PersonTravelHistoryDto.DATE_FROM;
    private static final String DATE_TO = PersonTravelHistoryDto.DATE_TO;
    private static final String VILLAGE = PersonTravelHistoryDto.VILLAGE;
    private static final String SUB_DISTRICT = PersonTravelHistoryDto.SUB_DISTRICT;
    private static final String DISTRICT = PersonTravelHistoryDto.DISTRICT;
    private static final String REGION = PersonTravelHistoryDto.REGION;

    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;


    public PersonTravelHistoryField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
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
                DATE_FROM,
                DATE_TO,
                VILLAGE,
                SUB_DISTRICT,
                DISTRICT,
                REGION);


        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(PersonTravelHistoryDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(PersonTravelHistoryDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {

        //format date
        table.addGeneratedColumn(DATE_FROM, (source, itemId, columnId) -> {
            PersonTravelHistoryDto personTravelHistoryDto = (PersonTravelHistoryDto) itemId;
            return DateFormatHelper.formatDate(personTravelHistoryDto.getDateFrom());
        });

        table.addGeneratedColumn(DATE_TO, (source, itemId, columnId) -> {
            PersonTravelHistoryDto personTravelHistoryDto = (PersonTravelHistoryDto) itemId;
            return DateFormatHelper.formatDate(personTravelHistoryDto.getDateFrom());
        });
    }

    @Override
    protected boolean isEmpty(PersonTravelHistoryDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(PersonTravelHistoryDto oldEntry, PersonTravelHistoryDto newEntry) {
        return isModifiedObject(oldEntry.getTravelPeriodType(), newEntry.getTravelPeriodType())
                || isModifiedObject(oldEntry.getDateFrom(), newEntry.getDateFrom())
                || isModifiedObject(oldEntry.getDateTo(), newEntry.getDateTo());
    }

    @Override
    public Class<PersonTravelHistoryDto> getEntryType() {
        return PersonTravelHistoryDto.class;
    }

    @Override
    protected void editEntry(PersonTravelHistoryDto entry, boolean create, Consumer<PersonTravelHistoryDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        PersonTravelHistoryEditForm personTravelHistoryForm = new PersonTravelHistoryEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        personTravelHistoryForm.setValue(entry);

        final CommitDiscardWrapperComponent<PersonTravelHistoryEditForm> component = new CommitDiscardWrapperComponent<>(
                personTravelHistoryForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                personTravelHistoryForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityPersonTravelHistory));
        popupWindow.setHeight(90, Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!personTravelHistoryForm.getFieldGroup().isModified()) {
                    commitCallback.accept(personTravelHistoryForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    PersonTravelHistoryField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected PersonTravelHistoryDto createEntry() {
        UserDto user = UserProvider.getCurrent().getUser();
        PersonTravelHistoryDto personTravelHistory = PersonTravelHistoryDto.build(TravelPeriodType.TEN_TO_FOURTEEN_MONTHS);
        return personTravelHistory;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<PersonTravelHistoryDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }
}
