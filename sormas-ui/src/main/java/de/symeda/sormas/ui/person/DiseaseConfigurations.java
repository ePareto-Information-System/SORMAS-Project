package de.symeda.sormas.ui.person;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Profession;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.FieldHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.CssStyles.H3;

public abstract class DiseaseConfigurations extends AbstractEditForm<PersonDto> {

    private static final Map<Disease, DiseaseConfiguration> CONFIGURATIONS = new HashMap<>();

    static {
        CONFIGURATIONS.put(Disease.NEW_INFLUENZA, form -> {
            form.setFieldsVisible(true,
                    form.investigatorName, form.investigatorTitle, form.investigatorUnit, form.investigatorAddress, form.investigatorTel);
        });

        CONFIGURATIONS.put(Disease.AHF, form -> {
            Label healthStaffDetailsLabel = new Label(I18nProperties.getString(Strings.healthStaffDetailsLabel));
            healthStaffDetailsLabel.addStyleName(H3);
            form.getContentPublic().addComponent(healthStaffDetailsLabel, PersonEditForm.HEALTH_STAFF_DETAILS_LOC);

            form.addField(PersonDto.HEAD_HOUSEHOLD, TextField.class);
            form.ethnicityField.setVisible(true);
            form.nationality.setVisible(true);
            form.ethnicityField.setCaption("Ethnic group");

            form.tickProfession = form.addField(PersonDto.PROFESSION_OF_PATIENT, OptionGroup.class);
            CssStyles.style(form.tickProfession, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
            form.tickProfession.setMultiSelect(true);

            form.tickProfession.addItems(
                    Arrays.stream(Profession.values())
                            .filter(c -> form.fieldVisibilityCheckers.isVisible(Profession.class, c.name()))
                            .collect(Collectors.toList()));

            form.professionOther = form.addField(PersonDto.PROFESSION_OF_PATIENT_OTHER, TextField.class);
            form.professionOther.setVisible(false);

            form.tickProfession.addValueChangeListener(event -> {
                Set<Profession> selectedProfessions = (Set<Profession>) event.getProperty().getValue();
                form.professionOther.setVisible(selectedProfessions != null && selectedProfessions.contains(Profession.OTHERS));
            });

            form.addField(PersonDto.NAME_HEALTH_FACILITY, TextField.class);
            form.addField(PersonDto.SERVICE, TextField.class);
            form.addField(PersonDto.QUALIFICATION, TextField.class);

            form.occupationHeader.setVisible(false);
            form.personContactDetailsField.setVisible(false);
        });

        CONFIGURATIONS.put(Disease.AFP, form -> {
            Label childSeekhelpHeadingLabel = new Label(I18nProperties.getString(Strings.headingChildSeek));
            childSeekhelpHeadingLabel.addStyleName(H3);
            childSeekhelpHeadingLabel.addStyleName("afp-childseek-label");
            form.getContentPublic().addComponent(childSeekhelpHeadingLabel, PersonEditForm.SEEK_HELP_HEADING_LOC);

            form.addField(PersonDto.PLACE, TextField.class);
            form.addField(PersonDto.DURATION_MONTHS, TextField.class);
            form.addField(PersonDto.DURATION_DAYS, TextField.class);
            form.addField(PersonDto.PLACE2, TextField.class);
            form.addField(PersonDto.DURATION_MONTHS2, TextField.class);
            form.addField(PersonDto.DURATION_DAYS2, TextField.class);
            form.addField(PersonDto.PLACE3, TextField.class);
            form.addField(PersonDto.DURATION_MONTHS3, TextField.class);
            form.addField(PersonDto.DURATION_DAYS3, TextField.class);
            form.addField(PersonDto.PLACE4, TextField.class);
            form.addField(PersonDto.DURATION_MONTHS4, TextField.class);
            form.addField(PersonDto.DURATION_DAYS4, TextField.class);

            form.setFieldsVisible(true,
                    form.investigatorName, form.investigatorTitle, form.investigatorUnit, form.investigatorAddress, form.investigatorTel, form.homeaddrecreational);

            form.generalCommentLabel.setVisible(false);
            form.occupationHeader.setVisible(false);
            form.addressesHeader.setVisible(false);
            form.contactInformationHeader.setVisible(false);
        });

        CONFIGURATIONS.put(Disease.FOODBORNE_ILLNESS, form -> {
            form.addressHeader.setVisible(false);
            form.personContactDetailsField.setVisible(false);
            form.addField(PersonDto.TEL_NUMBER, TextField.class);
        });

        CONFIGURATIONS.put(Disease.MONKEYPOX, form -> {
            form.personContactDetailsField.setVisible(false);
        });

        CONFIGURATIONS.put(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, form -> {
            form.occupationHeader.setVisible(false);
            form.personContactDetailsField.setVisible(false);
            form.setFieldsVisible(true, form.passport);

            form.addField(PersonDto.TEL_NUMBER, TextField.class);
            ComboBox applicable = form.addField(PersonDto.APPLICABLE, ComboBox.class);
            FieldHelper.setVisibleWhen(applicable, Arrays.asList(form.mothername, form.fathername), Arrays.asList(YesNo.YES), true);
        });

        CONFIGURATIONS.put(Disease.YELLOW_FEVER, form -> {
            form.setVisible(true, PersonDto.GHANA_CARD, PersonDto.NATIONAL_HEALTH_ID);
        });
    }

    protected DiseaseConfigurations(Class<PersonDto> type, String propertyI18nPrefix) {
        super(type, propertyI18nPrefix);
    }

    public static void applyConfiguration(Disease disease, PersonEditForm form) {
        DiseaseConfiguration configuration = CONFIGURATIONS.get(disease);
        if (configuration != null) {
            configuration.applyConfiguration(form);
        }
    }

    @FunctionalInterface
    public interface DiseaseConfiguration {
        void applyConfiguration(PersonEditForm form);
    }
}
