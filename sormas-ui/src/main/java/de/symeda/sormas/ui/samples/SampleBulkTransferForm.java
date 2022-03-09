package de.symeda.sormas.ui.samples;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.DateComparisonValidator;
import de.symeda.sormas.ui.utils.FieldHelper;

import java.util.Arrays;
import java.util.Date;

public class SampleBulkTransferForm extends AbstractSampleForm {

	private static final long serialVersionUID = 1L;

	private static final String HTML_LAYOUT = SAMPLE_COMMON_HTML_LAYOUT;
	
	public SampleBulkTransferForm(Disease disease) {
		super(SampleDto.class, SampleDto.I18N_PREFIX, disease, null);
	}

	@Override
	protected void addFields() {

		final ComboBox lab = addField(SampleDto.LAB, ComboBox.class);
		lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
		final TextField labDetails = addField(SampleDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		lab.addValueChangeListener(event -> updateLabDetailsVisibility(labDetails, event));
		
		addField(SampleDto.SHIPPED, CheckBox.class);
		addDateField(SampleDto.SHIPMENT_DATE, DateField.class, 7);
		addField(SampleDto.SHIPMENT_DETAILS, TextField.class);
		
		addField(SampleDto.RECEIVED, CheckBox.class);
		addField(SampleDto.RECEIVED_DATE, DateField.class);

		addField(SampleDto.SPECIMEN_CONDITION, ComboBox.class);
		addField(SampleDto.NO_TEST_POSSIBLE_REASON, TextField.class);
		
		addField(SampleDto.LAB_SAMPLE_ID, TextField.class);
		
		addField(SampleDto.COMMENT, TextArea.class).setRows(2);
		
		setVisibilities();
		
		addValueChangeListener(e -> {
			addValidators();
			defaultValueChangeListener();
		});
	}
	
	protected void addValidators() {		
		final Date sampleDate = this.getValue().getSampleDateTime();
		final DateField shipmentDate = (DateField) getField(SampleDto.SHIPMENT_DATE);
		final DateField receivedDate = (DateField) getField(SampleDto.RECEIVED_DATE);
		
		getField(SampleDto.LAB).setRequired(true);

		shipmentDate.addValidator(
			new DateComparisonValidator(
				shipmentDate,
				sampleDate,
				false,
				false,
				I18nProperties.getValidationError(
					Validations.afterDate, 
					shipmentDate.getCaption(), 
					I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.SAMPLE_DATE_TIME)
				)
			)
		);
		shipmentDate.addValidator(
			new DateComparisonValidator(
				shipmentDate,
				receivedDate,
				true,
				false,
				I18nProperties.getValidationError(Validations.beforeDate, shipmentDate.getCaption(), receivedDate.getCaption())));
		receivedDate.addValidator(
			new DateComparisonValidator(
				receivedDate,
				sampleDate,
				false,
				false,
				I18nProperties.getValidationError(
					Validations.afterDate, 
					receivedDate.getCaption(), 
					I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.SAMPLE_DATE_TIME)
				)
			)
		);
		receivedDate.addValidator(
			new DateComparisonValidator(
				receivedDate,
				shipmentDate,
				false,
				false,
				I18nProperties.getValidationError(Validations.afterDate, receivedDate.getCaption(), shipmentDate.getCaption())));
	}
	
	protected void defaultValueChangeListener() {
		
		final Field<?> receivedField = getField(SampleDto.RECEIVED);
		final Field<?> shippedField = getField(SampleDto.SHIPPED);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION),
			SampleDto.RECEIVED,
			Arrays.asList(true),
			true);
		FieldHelper.setEnabledWhen(
			getFieldGroup(),
			receivedField,
			Arrays.asList(true),
			Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION),
			true);
		FieldHelper.setRequiredWhen(
			getFieldGroup(),
			receivedField,
			Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.SPECIMEN_CONDITION),
			Arrays.asList(true));

		if (UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_EDIT_NOT_OWNED)
			|| UserProvider.getCurrent().getUuid().equals(getValue().getReportingUser().getUuid())) {
			FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS),
				SampleDto.SHIPPED,
				Arrays.asList(true),
				true);
			FieldHelper.setEnabledWhen(
				getFieldGroup(),
				shippedField,
				Arrays.asList(true),
				Arrays.asList(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS),
				true);
			FieldHelper.setRequiredWhen(getFieldGroup(), shippedField, Arrays.asList(SampleDto.SHIPMENT_DATE), Arrays.asList(true));
		} else {
			getField(SampleDto.LAB).setEnabled(false);
			shippedField.setEnabled(false);
			getField(SampleDto.SHIPMENT_DATE).setEnabled(false);
			getField(SampleDto.SHIPMENT_DETAILS).setEnabled(false);
		}
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}
}
