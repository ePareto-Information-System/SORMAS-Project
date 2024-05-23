package de.symeda.sormas.ui.afpimmunization;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class AfpImmunizationForm extends AbstractEditForm<AfpImmunizationDto> {
    private static final long serialVersionUID = 1L;
    private static final String IMMUNIZATION_HEADING_LOC = "immunizationHeadingLoc";

    private static final String HTML_LAYOUT =
            loc(IMMUNIZATION_HEADING_LOC)
                    + fluidRowLocs(6, AfpImmunizationDto.TOTAL_NUMBER_DOSES)
                    + fluidRowLocs(AfpImmunizationDto.OPV_DOSE_AT_BIRTH, AfpImmunizationDto.SECOND, AfpImmunizationDto.FOURTH)
                    + fluidRowLocs(AfpImmunizationDto.FIRST, AfpImmunizationDto.THIRD, AfpImmunizationDto.LAST_DOSE)
                    + fluidRowLocs(AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_SIA, AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_RI)
                    + fluidRowLocs(6,AfpImmunizationDto.DATE_LAST_OPV_DOSES_RECEIVED_THROUGH_SIA)
                    + fluidRowLocs(AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_SIA, AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_RI)
                    + fluidRowLocs(AfpImmunizationDto.DATE_LAST_IPV_DOSES_RECEIVED_THROUGH_SIA, AfpImmunizationDto.SOURCE_RI_VACCINATION_INFORMATION);

    private Field totalNumberDoses;
    private TextField opvDoseAtBirth;
    private TextField secondDose;
    private TextField fourthDose;
    private TextField firstDose;
    private TextField thirdDose;
    private TextField lastDose;
    private TextField totalOpvDosesReceivedThroughSia;
    private TextField totalOpvDosesReceivedThroughRi;
    private DateField dateLastOpvDosesReceivedThroughSia;
    private DateField dateLastIpvDosesReceivedThroughSia;
    private TextField totalIpvDosesReceivedThroughSia;
    private TextField totalIpvDosesReceivedThroughRi;
    private ComboBox sourceRiVaccinationInformation;

    private final Disease disease;
    private final Class<? extends EntityDto> parentClass;
    private final boolean isPseudonymized;

    public AfpImmunizationForm(Disease disease, Class<? extends EntityDto> parentClass,
                        boolean isPseudonymized,
                        boolean inJurisdiction,boolean isEditAllowed) {
        super(
                AfpImmunizationDto.class,
                AfpImmunizationDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized),
                isEditAllowed);
        this.disease = disease;
        this.parentClass = parentClass;
        this.isPseudonymized = isPseudonymized;
        addFields();
    }

    @Override
    protected void addFields() {
        Label afpImmunizationHeadingLabel = new Label(I18nProperties.getString(Strings.headingAfpImmunization));
        afpImmunizationHeadingLabel.addStyleName(H3);
        getContent().addComponent(afpImmunizationHeadingLabel, IMMUNIZATION_HEADING_LOC);

        totalNumberDoses = addField(AfpImmunizationDto.TOTAL_NUMBER_DOSES, Field.class);
        opvDoseAtBirth = addField(AfpImmunizationDto.OPV_DOSE_AT_BIRTH, TextField.class);
        secondDose = addField(AfpImmunizationDto.SECOND, TextField.class);
        fourthDose = addField(AfpImmunizationDto.FOURTH, TextField.class);

        firstDose = addField(AfpImmunizationDto.FIRST, TextField.class);
        thirdDose = addField(AfpImmunizationDto.THIRD, TextField.class);
        lastDose = addField(AfpImmunizationDto.LAST_DOSE, TextField.class);

        totalOpvDosesReceivedThroughSia = addField(AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_SIA, TextField.class);
        totalOpvDosesReceivedThroughRi = addField(AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_RI, TextField.class);

        dateLastOpvDosesReceivedThroughSia = addField(AfpImmunizationDto.DATE_LAST_OPV_DOSES_RECEIVED_THROUGH_SIA, DateField.class);

        totalIpvDosesReceivedThroughSia = addField(AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_SIA, TextField.class);
        totalIpvDosesReceivedThroughRi = addField(AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_RI, TextField.class);

        dateLastIpvDosesReceivedThroughSia = addField(AfpImmunizationDto.DATE_LAST_IPV_DOSES_RECEIVED_THROUGH_SIA, DateField.class);
        sourceRiVaccinationInformation = addField(AfpImmunizationDto.SOURCE_RI_VACCINATION_INFORMATION, ComboBox.class);

        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();
    }
    private Label createLabel(String text, String h4, String location) {
        final Label label = new Label(text);
        label.setId(text);
        label.addStyleName(h4);
        getContent().addComponent(label, location);
        return label;
    }


    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

}
