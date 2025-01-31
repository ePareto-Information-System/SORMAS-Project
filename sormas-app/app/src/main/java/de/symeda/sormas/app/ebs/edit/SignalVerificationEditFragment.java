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

package de.symeda.sormas.app.ebs.edit;

import static de.symeda.sormas.api.utils.DataHelper.isNullOrEmpty;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.widget.Toast;

import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.component.controls.ControlDateField;
import de.symeda.sormas.app.component.controls.ControlTextEditField;
import de.symeda.sormas.app.databinding.FragmentSignalVerificationEditLayoutBinding;

public class SignalVerificationEditFragment
	extends BaseEditFragment<FragmentSignalVerificationEditLayoutBinding, SignalVerification, SignalVerification> {

	public static final String PERSON = "Person";
	public static final String ANIMAL = "Animal";
	public static final String NUMBER_OF_DEATH_MORE_CASES = "The number of death cannot be more than cases";
	public static final String TAG = SignalVerificationEditFragment.class.getSimpleName();
	public final String THE_DATE_OF_DECISION_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE =
		"The Verification Completed cannot be earlier than the Date of Report or Date of Occurrence or Decision Date.";
	public final String THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE =
		"The Date of Occurrence cannot be older than the Date of Report or Date of Verification Completed Date.";
	private SignalVerification record;

	public static SignalVerificationEditFragment newInstance(Ebs activityRootData) {
		return newInstanceWithFieldCheckers(
			SignalVerificationEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	public static SignalVerificationEditFragment newInstance(SignalVerification activityRootData) {
		return newInstanceWithFieldCheckers(
			SignalVerificationEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_signal_verification);
	}

	@Override
	public SignalVerification getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		SignalVerification signalVerification = getActivityRootData();
		record = signalVerification;

	}

	@Override
	public void onLayoutBinding(final FragmentSignalVerificationEditLayoutBinding contentBinding) {
		contentBinding.setData(record);
		contentBinding.setSignalOutcomeClass(SignalOutcome.class);
		contentBinding.setYesNoClass(YesNo.class);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentSignalVerificationEditLayoutBinding contentBinding) {
		// Initialize ControlDateFields
		super.onAfterLayoutBinding(contentBinding);
		setFieldVisibilitiesAndAccesses(SignalVerificationDto.class, contentBinding.mainContent);
		contentBinding.signalVerificationVerificationCompleteDate.initializeDateField(getFragmentManager());
		contentBinding.signalVerificationDateOfOccurrence.initializeDateField(getFragmentManager());
		contentBinding.signalVerificationVerificationCompleteDate.addValueChangedListener(e -> {
			validateSignalVerificationVerificationCompleteDate(contentBinding);
		});
		contentBinding.signalVerificationDateOfOccurrence.addValueChangedListener(e -> {
			signalVerificationDateOfOccurrence(contentBinding);
			checkDeathCount(contentBinding, PERSON, true);
			checkDeathCount(contentBinding, ANIMAL, true);
		});

		validateSignalVerificationVerificationCompleteDate(contentBinding);
		signalVerificationDateOfOccurrence(contentBinding);
		contentBinding.signalVerificationVerificationSent.addValueChangedListener(e -> {
			if (e.getValue() == YesNo.NO) {
				showAlert(R.string.review_verification_24hrs);
			} else {
				checkDeathCount(contentBinding, PERSON, true);
				checkDeathCount(contentBinding, ANIMAL, true);
			}
		});
		contentBinding.signalVerificationNumberOfDeathPerson.addValueChangedListener(e -> {
			checkDeathCount(contentBinding, PERSON, false);
		});
		contentBinding.signalVerificationNumberOfPersonCases.addValueChangedListener(e -> {
			checkDeathCount(contentBinding, PERSON, false);
		});
		contentBinding.signalVerificationNumberOfPersonAnimal.addValueChangedListener(e -> {
			checkDeathCount(contentBinding, ANIMAL, false);
		});
		contentBinding.signalVerificationNumberOfDeath.addValueChangedListener(e -> {
			checkDeathCount(contentBinding, ANIMAL, false);
		});
		contentBinding.signalVerificationDescription.addValueChangedListener(e -> {
			checkDeathCount(contentBinding, PERSON, true);
			checkDeathCount(contentBinding, ANIMAL, true);
		});
	}

	private void checkDeathCount(FragmentSignalVerificationEditLayoutBinding contentBinding, String entity, boolean anotherField) {
		String numberOfCases = "";
		String value = "";
		ControlTextEditField fieldToValidate = null;

		switch (entity) {
		case PERSON:
			numberOfCases = contentBinding.signalVerificationNumberOfPersonCases.getValue();
			value = contentBinding.signalVerificationNumberOfDeathPerson.getValue();
			fieldToValidate = contentBinding.signalVerificationNumberOfDeathPerson;
			break;
		case ANIMAL:
			numberOfCases = contentBinding.signalVerificationNumberOfPersonAnimal.getValue();
			value = contentBinding.signalVerificationNumberOfDeath.getValue();
			fieldToValidate = contentBinding.signalVerificationNumberOfDeath;
			break;
		default:
			return;
		}
		if (anotherField) {
			CheckCaseNull(contentBinding, entity, value, numberOfCases);
		}

		if (!isNullOrEmpty(value) && !isNullOrEmpty(numberOfCases)) {
			try {
				int deathCount = Integer.parseInt(value);
				int caseCount = Integer.parseInt(numberOfCases);

				if (deathCount > caseCount) {
					ControlTextEditField finalFieldToValidate = fieldToValidate;
					fieldToValidate.setValidationCallback(() -> {
						finalFieldToValidate
							.enableErrorState(I18nProperties.getValidationError(NUMBER_OF_DEATH_MORE_CASES, NUMBER_OF_DEATH_MORE_CASES));
						return true;
					});
				} else {
					fieldToValidate.setValidationCallback(() -> false);
				}
			} catch (NumberFormatException ex) {
				ControlTextEditField finalTempFieldToValidate = fieldToValidate;
				fieldToValidate.setValidationCallback(() -> {
					finalTempFieldToValidate.enableErrorState("Invalid input. Please enter a valid number.");
					return true;
				});
			}
		} else {
			fieldToValidate.setValidationCallback(() -> false);
		}
	}

	private void CheckCaseNull(FragmentSignalVerificationEditLayoutBinding contentBinding, String entity, String value, String numberOfCases) {
		if (!isNullOrEmpty(value) && isNullOrEmpty(numberOfCases) && entity.equals(PERSON)) {
			int valueToInt = Integer.parseInt(value);
			contentBinding.signalVerificationNumberOfPersonCases.setValue(String.valueOf(valueToInt));
		}
		if (!isNullOrEmpty(value) && isNullOrEmpty(numberOfCases) && entity.equals(ANIMAL)) {
			int valueToInt = Integer.parseInt(value);
			contentBinding.signalVerificationNumberOfPersonAnimal.setValue(String.valueOf(valueToInt));
		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_signal_verification_edit_layout;
	}

	public void validateSignalVerificationVerificationCompleteDate(FragmentSignalVerificationEditLayoutBinding contentBinding) {
		Date dateOfReport = EbsEditActivity.getParentEbs().getReportDateTime();
		Date dateOfDecision = EbsEditActivity.getParentEbs().getTriaging().getDecisionDate();
		ControlDateField verificationCompleteDate = contentBinding.signalVerificationVerificationCompleteDate;
		Date DateverificationCompleteDate = verificationCompleteDate.getValue();
		if (dateOfReport == null) {
			dateOfReport = new Date(0);
		}

		Date dateOfReportDate = clearTime(dateOfReport);
		Date dateOfDecisionDate = clearTime(dateOfDecision);
		if (DateverificationCompleteDate != null) {
			DateverificationCompleteDate = clearTime(DateverificationCompleteDate);
			if (DateverificationCompleteDate.before(dateOfReportDate) || DateverificationCompleteDate.before(dateOfDecisionDate)) {
				if (!dateOfReportDate.toString().equals(DateverificationCompleteDate.toString())
					|| !dateOfDecision.toString().equals(DateverificationCompleteDate.toString())) {
					showError(THE_DATE_OF_DECISION_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE);
					verificationCompleteDate.setValidationCallback(() -> {
						verificationCompleteDate.enableErrorState(
							I18nProperties.getValidationError(
								THE_DATE_OF_DECISION_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE,
								THE_DATE_OF_DECISION_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE));
						return true;
					});
				} else {
					verificationCompleteDate.setValidationCallback(() -> false);
				}
			} else {
				verificationCompleteDate.setValidationCallback(() -> false);
			}
		}
	}

	public void signalVerificationDateOfOccurrence(FragmentSignalVerificationEditLayoutBinding contentBinding) {
		Date dateOfReport = EbsEditActivity.getParentEbs().getReportDateTime();
		ControlDateField verificationCompleteDate = contentBinding.signalVerificationVerificationCompleteDate;
		ControlDateField signalVerificationDateOfOccurrence = contentBinding.signalVerificationDateOfOccurrence;
		Date DatesignalVerificationDateOfOccurrenceDate = signalVerificationDateOfOccurrence.getValue();
		if (dateOfReport == null) {
			dateOfReport = new Date(0);
		}
		if (verificationCompleteDate.getValue() == null) {
			verificationCompleteDate.setValue(dateOfReport);
		}

		Date dateOfReportDate = clearTime(dateOfReport);
		if (DatesignalVerificationDateOfOccurrenceDate != null) {
			DatesignalVerificationDateOfOccurrenceDate = clearTime(DatesignalVerificationDateOfOccurrenceDate);
			if (DatesignalVerificationDateOfOccurrenceDate.after(dateOfReportDate)) {
				if (!dateOfReportDate.toString().equals(DatesignalVerificationDateOfOccurrenceDate.toString())) {
					showError(THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE);
					signalVerificationDateOfOccurrence.setValidationCallback(() -> {
						signalVerificationDateOfOccurrence.enableErrorState(
							I18nProperties.getValidationError(
								THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE,
								THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE));
						return true;
					});
				} else {
					signalVerificationDateOfOccurrence.setValidationCallback(() -> false);
				}
			} else {
				signalVerificationDateOfOccurrence.setValidationCallback(() -> false);
			}
		}
	}

	private Date clearTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private void showError(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
	}

	public void showAlert(int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.indicator_warning).setMessage(message).show();
	}
}
