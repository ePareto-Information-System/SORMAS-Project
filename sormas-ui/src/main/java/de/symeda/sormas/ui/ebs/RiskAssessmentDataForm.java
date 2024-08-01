package de.symeda.sormas.ui.ebs;


import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.text.SimpleDateFormat;
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
     fluidRowLocs(RiskAssessmentDto.MORBIDITY_MORTALITY_COMMENT) +
        fluidRowLocs(RiskAssessmentDto.SPREAD_PROBABILITY) +
        fluidRowLocs(RiskAssessmentDto.SPREAD_PROBABILITY_COMMENT) +
        fluidRowLocs(RiskAssessmentDto.CONTROL_MEASURES) +
        fluidRowLocs(RiskAssessmentDto.CONTROL_MEASURES_COMMENT) +
            fluidRowLocs(RiskAssessmentDto.ASSESSMENT_LEVEL, RiskAssessmentDto.ASSESSMENT_DATE, RiskAssessmentDto.ASSESSMENT_TIME);

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
        Label riskAssessment = new Label(I18nProperties.getString(Strings.headingRiskAssessment));
        riskAssessment.addStyleName(H3);
        getContent().addComponent(riskAssessment, RISK_ASSESSMENT_LOC);

        Label riskClassification = new Label(I18nProperties.getString(Strings.headingRiskDetails));
        riskClassification.addStyleName(H5);
        getContent().addComponent(riskClassification, RISK_CLASSIFICATION_LOC);
        NullableOptionGroup morbidityMortality = addField(RiskAssessmentDto.MORBIDITY_MORTALITY,NullableOptionGroup.class);
        TextArea morbidityMortalityComment = addField(RiskAssessmentDto.MORBIDITY_MORTALITY_COMMENT, TextArea.class);
        NullableOptionGroup spreadProbability = addField(RiskAssessmentDto.SPREAD_PROBABILITY,NullableOptionGroup.class);
        TextArea spreadProbabilityComment = addField(RiskAssessmentDto.SPREAD_PROBABILITY_COMMENT,TextArea.class);
        NullableOptionGroup controlMeasures = addField(RiskAssessmentDto.CONTROL_MEASURES,NullableOptionGroup.class);
        TextArea controlMeasuresComment = addField(RiskAssessmentDto.CONTROL_MEASURES_COMMENT,TextArea.class);
        ComboBox riskAssesment =  addField(RiskAssessmentDto.ASSESSMENT_LEVEL, ComboBox.class);
        DateField responseDate = addField(RiskAssessmentDto.ASSESSMENT_DATE, DateField.class);
        TextField responseTime = addField(RiskAssessmentDto.ASSESSMENT_TIME, TextField.class);

        responseTime.setVisible(false);

        ValueChangeListener commonListener = event -> {

            // Check if all three fields have YesNo.YES
            if (morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.NO) {
                riskAssesment.setValue(RiskAssesment.VERY_HIGH);
                riskAssesment.setStyleName("very-high-risk-assessment");
            }
            else if(morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.YES || morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.NO || morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.NO) {
                riskAssesment.setValue(RiskAssesment.HIGH);
                riskAssesment.setStyleName("high-risk-assessment");
            }
            else if(morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.NO || morbidityMortality.getNullableValue() == YesNo.YES && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.YES || morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.YES && controlMeasures.getNullableValue() == YesNo.YES) {
                riskAssesment.setValue(RiskAssesment.MEDIUM);
                riskAssesment.setStyleName("moderate-risk-assessment");
            }
            else if(morbidityMortality.getNullableValue() == YesNo.NO && spreadProbability.getNullableValue() == YesNo.NO && controlMeasures.getNullableValue() == YesNo.YES) {
                riskAssesment.setValue(RiskAssesment.LOW);
                riskAssesment.setStyleName("low-risk-assessment");
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
        controlMeasuresComment.setRequired(true);
        morbidityMortalityComment.setRequired(true);
        spreadProbabilityComment.setRequired(true);
    }
}
