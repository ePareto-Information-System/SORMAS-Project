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

package de.symeda.sormas.ui.affectedperson;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Window;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Table;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
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

public class AffectedPersonField extends AbstractTableField<AffectedPersonDto> {

    public static final String NAME_OF_AFFECTED_PERSON = "nameOfAffectedPerson";
    public static final String TEL_NO = "telNo";
    public static final String DATE_TIME = "dateTime";
    public static final String AGE = "age";

    private final FieldVisibilityCheckers fieldVisibilityCheckers;
    private boolean isPseudonymized;
    private boolean isEditAllowed;

    public AffectedPersonField(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, boolean isEditAllowed) {
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
                NAME_OF_AFFECTED_PERSON,
                TEL_NO,
                DATE_TIME,
                AGE
        );

        table.setCellStyleGenerator(
                FieldAccessCellStyleGenerator.withFieldAccessCheckers(AffectedPersonDto.class, UiFieldAccessCheckers.forSensitiveData(isPseudonymized)));

        for (Object columnId : table.getVisibleColumns()) {
            if (!columnId.equals(ACTION_COLUMN_ID)) {
                table.setColumnHeader(columnId, I18nProperties.getPrefixCaption(AffectedPersonDto.I18N_PREFIX, (String) columnId));
            }
        }
    }

    private void addGeneratedColumns(Table table) {

        table.addGeneratedColumn(NAME_OF_AFFECTED_PERSON, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            AffectedPersonDto affectedPerson = (AffectedPersonDto) itemId;
            return String.valueOf(affectedPerson.getAge());
        });

        table.addGeneratedColumn(TEL_NO, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            AffectedPersonDto affectedPerson = (AffectedPersonDto) itemId;
            return String.valueOf(affectedPerson.getAge());
        });

        table.addGeneratedColumn(DATE_TIME, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            AffectedPersonDto affectedPerson = (AffectedPersonDto) itemId;
            return DateFormatHelper.formatDate(affectedPerson.getDateTime());
        });

        table.addGeneratedColumn(AGE, (Table.ColumnGenerator) (source, itemId, columnId) -> {
            AffectedPersonDto affectedPerson = (AffectedPersonDto) itemId;
            return String.valueOf(affectedPerson.getAge());
        });


    }

    @Override
    protected boolean isEmpty(AffectedPersonDto entry) {
        return false;
    }

    @Override
    protected boolean isModified(AffectedPersonDto oldEntry, AffectedPersonDto newEntry) {
        return isModifiedObject(oldEntry.getNameOfAffectedPerson(), newEntry.getNameOfAffectedPerson())
                || isModifiedObject(oldEntry.getTelNo(), newEntry.getTelNo())
                || isModifiedObject(oldEntry.getAge(), newEntry.getAge());
    }

    @Override
    public Class<AffectedPersonDto> getEntryType() {
        return AffectedPersonDto.class;
    }


    @Override
    protected void editEntry(AffectedPersonDto entry, boolean create, Consumer<AffectedPersonDto> commitCallback) {
        if (create) {
            entry.setUuid(DataHelper.createUuid());
        }

        AffectedPersonEditForm affectedPersonEditForm = new AffectedPersonEditForm(create, fieldVisibilityCheckers, fieldAccessCheckers);
        affectedPersonEditForm.setValue(entry);

        final CommitDiscardWrapperComponent<AffectedPersonEditForm> component = new CommitDiscardWrapperComponent<>(
                affectedPersonEditForm,
                UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT) && isEditAllowed,
                affectedPersonEditForm.getFieldGroup());
        component.getCommitButton().setCaption(I18nProperties.getString(Strings.done));

        Window popupWindow = VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.entityAffectedPerson));
        popupWindow.setHeight(90, Sizeable.Unit.PERCENTAGE);

        if (isEditAllowed) {
            component.addCommitListener(() -> {
                if (!affectedPersonEditForm.getFieldGroup().isModified()) {
                    commitCallback.accept(affectedPersonEditForm.getValue());
                }
            });

            if (!create) {
                component.addDeleteListener(() -> {
                    popupWindow.close();
                    AffectedPersonField.this.removeEntry(entry);

                }, I18nProperties.getCaption(ExposureDto.I18N_PREFIX));
            }
        } else {
            component.getCommitButton().setVisible(false);
            component.getDiscardButton().setVisible(false);
        }
    }

    @Override
    protected AffectedPersonDto createEntry() {
        AffectedPersonDto affectedPersonDto = new AffectedPersonDto();
        return affectedPersonDto;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
    }

    public void setPseudonymized(boolean isPseudonymized) {
        this.isPseudonymized = isPseudonymized;
    }

    @Override
    public Property<Collection<AffectedPersonDto>> getPropertyDataSource() {
        getAddButton().setVisible(!isPseudonymized && isEditAllowed);
        return super.getPropertyDataSource();
    }

}
