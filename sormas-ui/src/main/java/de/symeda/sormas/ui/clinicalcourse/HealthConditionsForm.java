package de.symeda.sormas.ui.clinicalcourse;

import static de.symeda.sormas.api.clinicalcourse.HealthConditionsDto.*;
import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.SVG_STROKE_MINOR;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidColumn;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRow;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;
import static de.symeda.sormas.ui.utils.LayoutUtil.locs;

import java.util.Arrays;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextArea;

import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;

public class HealthConditionsForm extends AbstractEditForm<HealthConditionsDto> {

	private static final long serialVersionUID = 1L;

	private static final String HEALTH_CONDITIONS_HEADINGS_LOC = "healthConditionsHeadingLoc";

	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(HEALTH_CONDITIONS_HEADINGS_LOC) +
					fluidRow(
							fluidColumn(6, 0, locs(
									TUBERCULOSIS, ASPLENIA, HEPATITIS, DIABETES, IMMUNODEFICIENCY_OTHER_THAN_HIV,
									IMMUNODEFICIENCY_INCLUDING_HIV, HIV, HIV_ART, CONGENITAL_SYPHILIS, DOWN_SYNDROME,
									CHRONIC_LIVER_DISEASE, MALIGNANCY_CHEMOTHERAPY, LUNG_DISEASE, STROKE, CANCER)),
							fluidColumn(6, 0, locs(
									CHRONIC_HEART_FAILURE, CHRONIC_PULMONARY_DISEASE, CHRONIC_KIDNEY_DISEASE,
									CHRONIC_NEUROLOGIC_CONDITION, CARDIOVASCULAR_DISEASE_INCLUDING_HYPERTENSION,
									OBESITY, CURRENT_SMOKER, FORMER_SMOKER, ASTHMA, SICKLE_CELL_DISEASE))
					) +
					loc(OTHER_CONDITIONS);
	//@formatter:on

	public HealthConditionsForm(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
		super(HealthConditionsDto.class, I18N_PREFIX, true, fieldVisibilityCheckers, fieldAccessCheckers);
	}

	@Override
	protected void addFields() {

		Label healthConditionsHeadingLabel = new Label(I18nProperties.getString(Strings.headingHealthConditions));
		healthConditionsHeadingLabel.addStyleName(H3);
		getContent().addComponent(healthConditionsHeadingLabel, HEALTH_CONDITIONS_HEADINGS_LOC);

		addFields(
			TUBERCULOSIS,
			ASPLENIA,
			HEPATITIS,
			DIABETES,
			HIV,
			HIV_ART,
			CHRONIC_LIVER_DISEASE,
			MALIGNANCY_CHEMOTHERAPY,
			CHRONIC_HEART_FAILURE,
			CHRONIC_PULMONARY_DISEASE,
			CHRONIC_KIDNEY_DISEASE,
			CHRONIC_NEUROLOGIC_CONDITION,
			DOWN_SYNDROME,
			CONGENITAL_SYPHILIS,
			IMMUNODEFICIENCY_OTHER_THAN_HIV,
			CARDIOVASCULAR_DISEASE_INCLUDING_HYPERTENSION,
			OBESITY,
			CURRENT_SMOKER,
			FORMER_SMOKER,
			ASTHMA,
			SICKLE_CELL_DISEASE,
			IMMUNODEFICIENCY_INCLUDING_HIV,
			LUNG_DISEASE,
			STROKE,
			CANCER);
		TextArea otherConditions = addField(OTHER_CONDITIONS, TextArea.class);
		otherConditions.setRows(6);
		otherConditions.setDescription(
			I18nProperties.getPrefixDescription(HealthConditionsDto.I18N_PREFIX, OTHER_CONDITIONS, "") + "\n"
				+ I18nProperties.getDescription(Descriptions.descGdpr));

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		FieldHelper.setVisibleWhen(getFieldGroup(), HIV_ART, HIV, Arrays.asList(YesNoUnknown.YES), true);
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	protected <F extends Field> F addFieldToLayout(CustomLayout layout, String propertyId, F field) {
		field.addValueChangeListener(e -> fireValueChange(false));

		return super.addFieldToLayout(layout, propertyId, field);
	}

	//hide all fields
	public void hideAllFields() {
		setVisible(false,
				TUBERCULOSIS,
				ASPLENIA,
				HEPATITIS,
				DIABETES,
				HIV,
				HIV_ART,
				CHRONIC_LIVER_DISEASE,
				MALIGNANCY_CHEMOTHERAPY,
				CHRONIC_HEART_FAILURE,
				CHRONIC_PULMONARY_DISEASE,
				CHRONIC_KIDNEY_DISEASE,
				CHRONIC_NEUROLOGIC_CONDITION,
				DOWN_SYNDROME,
				CONGENITAL_SYPHILIS,
				IMMUNODEFICIENCY_OTHER_THAN_HIV,
				CARDIOVASCULAR_DISEASE_INCLUDING_HYPERTENSION,
				OBESITY,
				CURRENT_SMOKER,
				FORMER_SMOKER,
				ASTHMA,
				SICKLE_CELL_DISEASE,
				IMMUNODEFICIENCY_INCLUDING_HIV,
				LUNG_DISEASE,
				STROKE,
				CANCER
		);
	}

	public void showForCovid19() {
		setVisible(true,
				CARDIOVASCULAR_DISEASE_INCLUDING_HYPERTENSION,
				IMMUNODEFICIENCY_INCLUDING_HIV,
				DIABETES,
				CHRONIC_KIDNEY_DISEASE,
				CHRONIC_LIVER_DISEASE,
				LUNG_DISEASE,
				STROKE,
				CANCER,
				HIV,
				OTHER_CONDITIONS
		);
	}
}
