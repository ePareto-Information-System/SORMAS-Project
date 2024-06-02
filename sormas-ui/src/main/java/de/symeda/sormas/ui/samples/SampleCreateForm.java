package de.symeda.sormas.ui.samples;

import com.vaadin.v7.ui.CheckBox;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SamplePurpose;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

public class SampleCreateForm extends AbstractSampleForm {

	private static final long serialVersionUID = 1L;
	private Disease disease;

	public SampleCreateForm(Disease disease) {
		super(SampleDto.class, SampleDto.I18N_PREFIX, disease, null);
		this.disease = disease;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addFields() {
		addCommonFields();
		initializeRequestedTestFields();
		addValidators();
		setVisibilities();

		addValueChangeListener(e -> {
			defaultValueChangeListener();
			final NullableOptionGroup samplePurposeField = getField(SampleDto.SAMPLE_PURPOSE);
			samplePurposeField.setValue(SamplePurpose.EXTERNAL);

		});
	}


	@Override
	protected String createHtmlLayout() {
		disease = getCaseDisease();

		String SELECTED_LAYOUT = "";
			switch (disease) {
				case GUINEA_WORM:
					SELECTED_LAYOUT = GUINEA_WORM_LAYOUT;
					break;
				case MEASLES:
					SELECTED_LAYOUT = MEASLES_LAYOUT;
					break;
				case CHOLERA:
					SELECTED_LAYOUT = CHOLERA_HTML_LAYOUT;
					break;
				default:
					SELECTED_LAYOUT = SAMPLE_COMMON_HTML_LAYOUT;
					break;
			}

		return SELECTED_LAYOUT;
	}

}
