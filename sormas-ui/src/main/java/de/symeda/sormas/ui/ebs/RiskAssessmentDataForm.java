package de.symeda.sormas.ui.ebs;


import com.vaadin.ui.Label;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.H5;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

;

public class RiskAssessmentDataForm extends AbstractEditForm<RiskAssessmentDto> {

    private static final long serialVersionUID = 1L;
    private static final String RISK_ASSESSMENT_LOC = "riskAssessmentLoc";
    private static final String RISK_CLASSIFICATION_LOC = "riskClassificationLoc";

    private final EbsDto ebs;
    private final Class<? extends EntityDto> parentClass;

    private static final String HTML_LAYOUT =
    loc(RISK_ASSESSMENT_LOC) +
    loc(RISK_CLASSIFICATION_LOC) +
     fluidRowLocs(RiskAssessmentDto.MORBIDITY_MORTALITY) +
        fluidRowLocs(RiskAssessmentDto.SPREAD_PROBABILITY) +
        fluidRowLocs(RiskAssessmentDto.CONTROL_MEASURES) +
            fluidRowLocs(RiskAssessmentDto.RISK_ASSESSMENT, RiskAssessmentDto.RESPONSE_DATE, RiskAssessmentDto.RESPONSE_TIME);

    RiskAssessmentDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed){
        super(
                RiskAssessmentDto.class,
                RiskAssessmentDto.I18N_PREFIX,
            false,
            FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
            createFieldAccessCheckers(isPseudonymized, inJurisdiction, true));
        this.ebs = ebsDto;
        this.parentClass = parentClass;
        addFields();
    }

    private static UiFieldAccessCheckers createFieldAccessCheckers(
            boolean isPseudonymized,
            boolean inJurisdiction,
            boolean withPersonalAndSensitive) {

        if (withPersonalAndSensitive) {
            return UiFieldAccessCheckers
                    .forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized);
        }

        return UiFieldAccessCheckers.getNoop();
    }


    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {
        if (ebs == null){
            return;
        }
        Label riskAssessment = new Label(I18nProperties.getString(Strings.headingSignalVerification));
        riskAssessment.addStyleName(H3);
        getContent().addComponent(riskAssessment, RISK_ASSESSMENT_LOC);

        Label riskClassification = new Label(I18nProperties.getString(Strings.headingEventDetails));
        riskClassification.addStyleName(H5);
        getContent().addComponent(riskClassification, RISK_CLASSIFICATION_LOC);
        NullableOptionGroup morbidityMortality = addField(RiskAssessmentDto.MORBIDITY_MORTALITY,NullableOptionGroup.class);
        NullableOptionGroup spreadProbability = addField(RiskAssessmentDto.SPREAD_PROBABILITY,NullableOptionGroup.class);
        NullableOptionGroup controlMeasures = addField(RiskAssessmentDto.CONTROL_MEASURES,NullableOptionGroup.class);
        ComboBox riskAssesment =  addField(RiskAssessmentDto.RISK_ASSESSMENT, ComboBox.class);
        DateField responseDate = addField(RiskAssessmentDto.RESPONSE_DATE, DateField.class);
        TextField responseTime = addField(RiskAssessmentDto.RESPONSE_TIME, TextField.class);

//        riskAssesment.setVisible(false);
//        responseDate.setVisible(false);
//        responseDate.setVisible(false);

        ValueChangeListener commonListener = event -> {

            // Check if all three fields have YesNo.YES
            if (morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.NO) {
                riskAssesment.setValue(RiskAssesment.VERY_HIGH);
            }
            else if(morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.YES || morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.NO) {
                riskAssesment.setValue(RiskAssesment.HIGH);
            }
            else if(morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.NO || morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.YES || morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.YES) {
                riskAssesment.setValue(RiskAssesment.MEDIUM);
            }
            else if(morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.YES) {
                riskAssesment.setValue(RiskAssesment.LOW);
            }

            // Set responseDate to the current date
            responseDate.setValue(new Date());

            // Set responseTime to the current time
            Date now = new Date();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mma");
            String formattedTime = timeFormatter.format(now).toLowerCase();
            responseTime.setValue(formattedTime);
        };
        morbidityMortality.addValueChangeListener(commonListener);
        spreadProbability.addValueChangeListener(commonListener);
        controlMeasures.addValueChangeListener(commonListener);
    }
}
