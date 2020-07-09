package de.symeda.sormas.ui.utils;

import com.vaadin.v7.data.Validator;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.ui.ControllerProvider;

public class FieldSampleIdValidatorUtil implements Validator {

	private SampleDto dto;

	public FieldSampleIdValidatorUtil(SampleDto sampleDto) {
		getValue(sampleDto);
	}

	void getValue(SampleDto sampleDto) {
		dto = sampleDto;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (value == null || value.equals(""))
			return;

		else if (!(value instanceof String && value != null && ControllerProvider.getSampleController().isFieldSampleIdExist((String) value)))
			throw new InvalidValueException(I18nProperties.getString(Strings.messageFieldSampleIdExist));
	}

}
