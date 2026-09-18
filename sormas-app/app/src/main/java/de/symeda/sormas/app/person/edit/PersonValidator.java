/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.person.edit;

import static android.view.View.VISIBLE;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.app.component.controls.ControlPropertyEditField;
import de.symeda.sormas.app.component.controls.ControlSpinnerField;
import de.symeda.sormas.app.component.controls.ControlTextEditField;
import de.symeda.sormas.app.databinding.FragmentPersonEditLayoutBinding;
import de.symeda.sormas.app.util.ResultCallback;

public final class PersonValidator {

	static void initializePersonValidation(final FragmentPersonEditLayoutBinding contentBinding) {
		ResultCallback<Boolean> deathDateCallback = () -> {
			Date birthDate = PersonEditFragment.calculateBirthDateValue(contentBinding);
			if (DateHelper.isDateBefore(contentBinding.personDeathDate.getValue(), birthDate)) {
				contentBinding.personDeathDate.enableErrorState(
					I18nProperties.getValidationError(
						Validations.afterDate,
						contentBinding.personDeathDate.getCaption(),
						contentBinding.personBirthdateLabel.getText()));
				return true;
			}
			if (DateHelper.isDateAfter(contentBinding.personDeathDate.getValue(), contentBinding.personBurialDate.getValue())) {
				contentBinding.personDeathDate.enableErrorState(
						I18nProperties.getValidationError(
								Validations.beforeDate,
								contentBinding.personDeathDate.getCaption(),
								contentBinding.personBurialDate.getCaption()));
				return true;
			}

			return false;
		};

		ResultCallback<Boolean> burialDateCallback = () -> {
			Date birthDate = PersonEditFragment.calculateBirthDateValue(contentBinding);
			if (DateHelper.isDateBefore(contentBinding.personBurialDate.getValue(), birthDate)) {
				contentBinding.personBurialDate.enableErrorState(
					I18nProperties.getValidationError(
						Validations.afterDate,
						contentBinding.personBurialDate.getCaption(),
						contentBinding.personBirthdateLabel.getText()));
				return true;
			}

			if (DateHelper.isDateBefore(contentBinding.personBurialDate.getValue(), contentBinding.personDeathDate.getValue())) {
				contentBinding.personBurialDate.enableErrorState(
					I18nProperties.getValidationError(
						Validations.afterDate,
						contentBinding.personBurialDate.getCaption(),
						contentBinding.personDeathDate.getCaption()));
				return true;
			}

			return false;
		};

		ResultCallback<Boolean> approximateAgeCallback = () -> {
			if (ApproximateAgeType.YEARS.equals(contentBinding.personApproximateAgeType.getValue())
				&& !StringUtils.isEmpty(contentBinding.personApproximateAge.getValue())
				&& Integer.valueOf(contentBinding.personApproximateAge.getValue()) >= 150) {
				contentBinding.personApproximateAge.enableErrorState(I18nProperties.getValidationError(Validations.softApproximateAgeTooHigh));
				return true;
			}

			return false;
		};

		initializeBirthDateValidation(contentBinding.personBirthdateYYYY, contentBinding.personBirthdateMM, contentBinding.personBirthdateDD);

		contentBinding.personDeathDate.setValidationCallback(deathDateCallback);
		contentBinding.personBurialDate.setValidationCallback(burialDateCallback);
		contentBinding.personApproximateAge.setValidationCallback(approximateAgeCallback);

		contentBinding.personDeathDate.addValueChangedListener( v -> {
			if(!burialDateCallback.call()){
				contentBinding.personBurialDate.disableErrorState();
			}
		});

		contentBinding.personBurialDate.addValueChangedListener( v -> {
			if(!deathDateCallback.call()){
				contentBinding.personDeathDate.disableErrorState();
			}
		});

		contentBinding.personBirthdateYYYY.addValueChangedListener(v -> {
			if(!deathDateCallback.call()){
				contentBinding.personDeathDate.disableErrorState();
			}
			if(!burialDateCallback.call()){
				contentBinding.personBurialDate.disableErrorState();
			}
		});
		contentBinding.personBirthdateMM.addValueChangedListener(v -> {
			if(!deathDateCallback.call()){
				contentBinding.personDeathDate.disableErrorState();
			}
			if(!burialDateCallback.call()){
				contentBinding.personBurialDate.disableErrorState();
			}
		});
		contentBinding.personBirthdateDD.addValueChangedListener(v -> {
			if(!deathDateCallback.call()){
				contentBinding.personDeathDate.disableErrorState();
			}
			if(!burialDateCallback.call()){
				contentBinding.personBurialDate.disableErrorState();
			}
		});
	}

	/**
	 * Either a complete date of birth (year, month and day) or the approximate age is mandatory (not both).
	 * Age unit becomes required once an approximate age is entered without a birth year. Hidden fields are never required.
	 */
	public static void initializeBirthDateOrApproximateAgeRequired(
		ControlSpinnerField birthdateYYYY,
		ControlSpinnerField birthdateMM,
		ControlSpinnerField birthdateDD,
		ControlTextEditField approximateAge,
		ControlSpinnerField approximateAgeType) {

		Runnable updateRequirement =
			() -> updateBirthDateOrApproximateAgeRequired(birthdateYYYY, birthdateMM, birthdateDD, approximateAge, approximateAgeType);

		birthdateYYYY.addValueChangedListener(field -> updateRequirement.run());
		birthdateMM.addValueChangedListener(field -> updateRequirement.run());
		birthdateDD.addValueChangedListener(field -> updateRequirement.run());
		approximateAge.addValueChangedListener(field -> {
			String ageValue = field.getValue() != null ? field.getValue().toString() : null;
			boolean hasBirthYear = birthdateYYYY.getValue() != null;
			if (DataHelper.isNullOrEmpty(ageValue) || hasBirthYear) {
				if (DataHelper.isNullOrEmpty(ageValue) && !hasBirthYear) {
					approximateAgeType.setRequired(false);
					approximateAgeType.setValue(null);
				}
			} else if (approximateAgeType.getValue() == null) {
				approximateAgeType.setValue(ApproximateAgeType.YEARS);
			}
			updateRequirement.run();
		});

		updateRequirement.run();
	}

	public static void updateBirthDateOrApproximateAgeRequired(
		ControlSpinnerField birthdateYYYY,
		ControlSpinnerField birthdateMM,
		ControlSpinnerField birthdateDD,
		ControlTextEditField approximateAge,
		ControlSpinnerField approximateAgeType) {

		boolean hasBirthYear = birthdateYYYY.getValue() != null;
		boolean hasBirthMonth = birthdateMM.getValue() != null;
		boolean hasBirthDay = birthdateDD.getValue() != null;
		boolean hasCompleteBirthDate = hasBirthYear && hasBirthMonth && hasBirthDay;

		String ageValue = approximateAge.getValue() != null ? approximateAge.getValue().toString() : null;
		// Age only counts as the alternative path when no birth year is set (otherwise age is derived from DOB)
		boolean hasApproximateAge = !hasBirthYear && !DataHelper.isNullOrEmpty(ageValue);

		boolean birthYearVisible = isFieldVisible(birthdateYYYY);
		boolean birthMonthVisible = isFieldVisible(birthdateMM);
		boolean birthDayVisible = isFieldVisible(birthdateDD);
		boolean ageVisible = isFieldVisible(approximateAge);
		boolean ageTypeVisible = isFieldVisible(approximateAgeType);

		boolean eitherRequired = !hasCompleteBirthDate && !hasApproximateAge;
		birthdateYYYY.setRequired(eitherRequired && birthYearVisible);
		approximateAge.setRequired(eitherRequired && ageVisible);

		boolean birthDatePartsRequired = (eitherRequired || hasBirthYear) && !hasCompleteBirthDate && birthYearVisible;
		birthdateMM.setRequired(birthDatePartsRequired && birthMonthVisible);
		birthdateDD.setRequired(birthDatePartsRequired && birthDayVisible);

		approximateAgeType.setRequired(hasApproximateAge && ageVisible && ageTypeVisible);
	}

	private static boolean isFieldVisible(ControlPropertyEditField<?> field) {
		return field != null && field.getVisibility() == VISIBLE;
	}

	public static void initializeBirthDateValidation(
		ControlSpinnerField personBirthdateYYYY,
		ControlSpinnerField personBirthdateMM,
		ControlSpinnerField personBirthdateDD) {

		ResultCallback<Boolean> birthDateCallback = () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.setLenient(false);
			if (personBirthdateYYYY.getValue() != null) {
				calendar.set(Calendar.YEAR, (Integer) personBirthdateYYYY.getValue());
			}
			if (personBirthdateMM.getValue() != null) {
				calendar.set(Calendar.MONTH, ((Integer) personBirthdateMM.getValue()) - 1);
			}
			if (personBirthdateDD.getValue() != null) {
				calendar.set(Calendar.DAY_OF_MONTH, (Integer) personBirthdateDD.getValue());
			}

			if (DateHelper.isDateAfter(calendar.getTime(), new Date())) {
				personBirthdateYYYY.enableErrorState(I18nProperties.getValidationError(Validations.birthDateInFuture));
				personBirthdateMM.enableErrorState(I18nProperties.getValidationError(Validations.birthDateInFuture));
				personBirthdateDD.enableErrorState(I18nProperties.getValidationError(Validations.birthDateInFuture));
				return true;
			}

			return false;
		};

		personBirthdateYYYY.setValidationCallback(birthDateCallback);
		personBirthdateMM.setValidationCallback(birthDateCallback);
		personBirthdateDD.setValidationCallback(birthDateCallback);
	}

}
