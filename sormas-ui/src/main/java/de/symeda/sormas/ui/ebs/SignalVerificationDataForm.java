package de.symeda.sormas.ui.ebs;

import static de.symeda.sormas.api.utils.DataHelper.isNullOrEmpty;
import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.EbsAnimalDeathValidator;
import de.symeda.sormas.ui.utils.EbsDateValidator;
import de.symeda.sormas.ui.utils.EbsPersonDeathValidator;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.FutureDateValidator;
import de.symeda.sormas.ui.utils.NullableOptionGroup;
import de.symeda.sormas.ui.utils.NumberNumericValueValidator;

;

public class SignalVerificationDataForm extends AbstractEditForm<SignalVerificationDto> {

	private static final long serialVersionUID = 1L;
	private static final String EVENT_DETAILS_LOC = "eventDetailsLoc";
	private static final String SIGNAL_VERIFICATION_LOC = "signalVerificationLoc";

	private static final String SIGNAL_VERIFICATION_ENTITY = "SignalVerification";
	public static final String PERSON = "Person";
	public static final String ANIMAL = "Animal";
	public static final String NUMBER_OF_DEATH_MORE_CASES = "The number of death cannot be more than cases";
	public final String THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE =
		"The Date cannot be older than the Date of Report";
	public final String THE_DATE_OF_OCCURRENCE_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE =
		"The Date cannot be earlier than the Date of Report or Triage Decision Date";

	private final EbsDto ebs;
	private final Class<? extends EntityDto> parentClass;
	TextField numberOfPersonAnimal;
	TextField numberOfDeath;
	TextField numberOfPersonCases;
	TextField numberOfDeathPerson;
	TextArea description;
	DateField verificationCompleteDate;
	DateTimeField DateOfOccurrence;
	EbsAnimalDeathValidator animalDeathValidator = new EbsAnimalDeathValidator(NUMBER_OF_DEATH_MORE_CASES, false);
	EbsPersonDeathValidator personDeathValidator = new EbsPersonDeathValidator(NUMBER_OF_DEATH_MORE_CASES, false);
	EbsDateValidator dateValidator = new EbsDateValidator(THE_DATE_OF_OCCURRENCE_CANNOT_BE_OLDER_THAN_THE_DATE_OF_OCCURRENCE, false);
	EbsDateValidator completeDateValidator = new EbsDateValidator(THE_DATE_OF_OCCURRENCE_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE, false);

	private static final String HTML_LAYOUT = loc(SIGNAL_VERIFICATION_LOC)
		+ fluidRowLocs(SignalVerificationDto.VERIFICATION_SENT)
		+ fluidRowLocs(SignalVerificationDto.VERIFIED)
		+ fluidRowLocs(SignalVerificationDto.VERIFICATION_COMPLETE_DATE, "")
		+ loc(EVENT_DETAILS_LOC)
		+ fluidRowLocs(SignalVerificationDto.DATE_OF_OCCURRENCE, SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL, SignalVerificationDto.NUMBER_OF_DEATH)
		+ fluidRowLocs(SignalVerificationDto.NUMBER_OF_PERSON_CASES, SignalVerificationDto.NUMBER_OF_DEATH_PERSON, "")
		+ fluidRowLocs(SignalVerificationDto.DESCRIPTION)
		+ fluidRowLocs(SignalVerificationDto.WHY_NOT_VERIFY);

	SignalVerificationDataForm(
		EbsDto ebsDto,
		Class<? extends EntityDto> parentClass,
		boolean isPseudonymized,
		boolean inJurisdiction,
		boolean isEditAllowed) {
		super(
			SignalVerificationDto.class,
			SignalVerificationDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
			createFieldAccessCheckers(isPseudonymized, true),
			ebsDto);
		this.ebs = ebsDto;
		this.parentClass = parentClass;
		addFields();
	}

	private static UiFieldAccessCheckers createFieldAccessCheckers(boolean isPseudonymized, boolean withPersonalAndSensitive) {

		if (withPersonalAndSensitive) {
			return UiFieldAccessCheckers.getDefault(isPseudonymized);
		}

		return UiFieldAccessCheckers.getNoop();
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	protected void addFields() {
		if (ebs == null) {
			return;
		}
		Label signalVerification = new Label(I18nProperties.getString(Strings.headingSignalVerification));
		signalVerification.addStyleName(H3);
		getContent().addComponent(signalVerification, SIGNAL_VERIFICATION_LOC);

		Label headingEventDetails = new Label(I18nProperties.getString(Strings.headingEventDetails));
		headingEventDetails.addStyleName(H3);
		getContent().addComponent(headingEventDetails, EVENT_DETAILS_LOC);
		NullableOptionGroup sentVerification = addField(SignalVerificationDto.VERIFICATION_SENT, NullableOptionGroup.class);
		NullableOptionGroup verified = addField(SignalVerificationDto.VERIFIED, NullableOptionGroup.class);
		verificationCompleteDate = addField(SignalVerificationDto.VERIFICATION_COMPLETE_DATE, DateField.class);
		DateOfOccurrence = addField(SignalVerificationDto.DATE_OF_OCCURRENCE, DateTimeField.class);
		numberOfPersonAnimal = addField(SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL, TextField.class);
		numberOfDeath = addField(SignalVerificationDto.NUMBER_OF_DEATH, TextField.class);
		description = addField(SignalVerificationDto.DESCRIPTION, TextArea.class);
		addField(SignalVerificationDto.WHY_NOT_VERIFY, TextArea.class);
		numberOfPersonCases = addField(SignalVerificationDto.NUMBER_OF_PERSON_CASES, TextField.class);
		numberOfDeathPerson = addField(SignalVerificationDto.NUMBER_OF_DEATH_PERSON, TextField.class);

		numberOfPersonCases.addValidator(
			new NumberNumericValueValidator(I18nProperties.getValidationError(Validations.onlyNumbersAllowed, numberOfPersonCases.getCaption())));
		numberOfDeathPerson.addValidator(
			new NumberNumericValueValidator(I18nProperties.getValidationError(Validations.onlyNumbersAllowed, numberOfDeathPerson.getCaption())));
		numberOfPersonAnimal.addValidator(
			new NumberNumericValueValidator(I18nProperties.getValidationError(Validations.onlyNumbersAllowed, numberOfPersonAnimal.getCaption())));
		numberOfDeath.addValidator(
			new NumberNumericValueValidator(I18nProperties.getValidationError(Validations.onlyNumbersAllowed, numberOfDeath.getCaption())));

		EbsDto selectedEbs = getEbsDto();

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(
				SignalVerificationDto.DESCRIPTION,
				SignalVerificationDto.DATE_OF_OCCURRENCE,
				SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL,
				SignalVerificationDto.NUMBER_OF_DEATH,
				SignalVerificationDto.NUMBER_OF_DEATH_PERSON,
				SignalVerificationDto.NUMBER_OF_PERSON_CASES),
			SignalVerificationDto.VERIFIED,
			Arrays.asList(SignalOutcome.EVENT),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(SignalVerificationDto.WHY_NOT_VERIFY),
			SignalVerificationDto.VERIFIED,
			Arrays.asList(SignalOutcome.NON_EVENT),
			true);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(SignalVerificationDto.VERIFIED, SignalVerificationDto.VERIFICATION_COMPLETE_DATE),
			SignalVerificationDto.VERIFICATION_SENT,
			Arrays.asList(YesNo.YES),
			true);
		verified.addValueChangeListener(event -> {
			if (event.getProperty().getValue().toString().equals("[Event]")) {
				getContent().getComponent(EVENT_DETAILS_LOC).setVisible(true);
				setRequired(
					true,
					SignalVerificationDto.VERIFICATION_COMPLETE_DATE,
					SignalVerificationDto.DATE_OF_OCCURRENCE,
					SignalVerificationDto.DESCRIPTION);
				setRequired(false, SignalVerificationDto.WHY_NOT_VERIFY);
			} else {
				setRequired(
					false,
					SignalVerificationDto.VERIFICATION_COMPLETE_DATE,
					SignalVerificationDto.DATE_OF_OCCURRENCE,
					SignalVerificationDto.DESCRIPTION);
				if (sentVerification.getValue() != null && sentVerification.getValue().toString().equals("NO")) {
					setRequired(false, SignalVerificationDto.WHY_NOT_VERIFY);
				} else {
					setRequired(true, SignalVerificationDto.WHY_NOT_VERIFY);
				}
				getContent().getComponent(EVENT_DETAILS_LOC).setVisible(false);

			}
		});
		setRequired(true, SignalVerificationDto.VERIFICATION_SENT);

		sentVerification.addValueChangeListener(e -> {
			if (e.getProperty().getValue().toString().equals("NO")) {
				setRequired(false, SignalVerificationDto.WHY_NOT_VERIFY);
			}
		});
		sentVerification.addValueChangeListener(event -> {
			if (event.getProperty().getValue().equals(YesNo.NO) && selectedEbs.getTriaging().getTriagingDecision() == EbsTriagingDecision.VERIFY) {
				TriagingDataForm.reviewSignal(Strings.verifyNotifs);
			}
		});

		if (selectedEbs.getSignalVerification().getVerificationSent() != null
			&& selectedEbs.getSignalVerification().getVerificationSent() == YesNo.NO) {
			setRequired(false, SignalVerificationDto.WHY_NOT_VERIFY);
		}
		numberOfDeathPerson.addValueChangeListener(event -> {
			checkDeathCount(PERSON, false);
		});
		numberOfPersonCases.addValueChangeListener(event -> {
			checkDeathCount(PERSON, false);
		});
		numberOfPersonAnimal.addValueChangeListener(event -> {
			checkDeathCount(ANIMAL, false);
		});
		numberOfDeath.addValueChangeListener(event -> {
			checkDeathCount(ANIMAL, false);
		});
		description.addFocusListener(event -> {
			checkDeathCount(ANIMAL, true);
			checkDeathCount(PERSON, true);
		});
		DateOfOccurrence.addValueChangeListener(event -> {
			validateSignalVerificationDateOfOccurrence(selectedEbs);
		});
		verificationCompleteDate.addValueChangeListener(event -> {
			validateSignalVerificationVerificationCompleteDate(selectedEbs);
		});
		validateSignalVerificationDateOfOccurrence(selectedEbs);
		validateSignalVerificationVerificationCompleteDate(selectedEbs);
	}

	private void checkDeathCount(String entity, boolean anotherField) {
		String numberOfCases = "";
		String value = "";

		switch (entity) {
		case PERSON:
			numberOfCases = numberOfPersonCases.getValue();
			value = numberOfDeathPerson.getValue();
			break;
		case ANIMAL:
			numberOfCases = numberOfPersonAnimal.getValue();
			value = numberOfDeath.getValue();
			break;
		default:
			return;
		}

		if (anotherField) {
			CheckCaseNull(entity, value, numberOfCases);
		}

		if (!isNullOrEmpty(value) && !isNullOrEmpty(numberOfCases)) {
			try {
				int deathCount = Integer.parseInt(value);
				int caseCount = Integer.parseInt(numberOfCases);

				if (deathCount > caseCount) {
					addDeathCountExceedsCaseCountValidator(entity);
				} else {
					addNumberFormatValidator(entity);
					removeDeathCountExceedsCaseCountValidator(entity);
				}
			} catch (NumberFormatException ex) {
				addNumberFormatValidator(entity);
			}
		}
	}

	private void addNumberFormatValidator(String entity) {
		NumberNumericValueValidator validator = new NumberNumericValueValidator(
			I18nProperties.getValidationError(
				Validations.numberOfDeathMoreThanCases,
				entity.equals(PERSON) ? numberOfDeathPerson.getCaption() : numberOfDeath.getCaption()));

		if (entity.equals(PERSON)) {
			numberOfDeathPerson.addValidator(validator);
		} else {
			numberOfDeath.addValidator(validator);
		}
	}

	private void addDeathCountExceedsCaseCountValidator(String entity) {
		if (entity.equals(PERSON)) {
			personDeathValidator.setValidDeath(false);
			numberOfDeathPerson.removeAllValidators();
			numberOfDeathPerson.addValidator(personDeathValidator);
			addNumberFormatValidator(entity);
		} else {
			animalDeathValidator.setValidDeath(false);
			numberOfDeath.removeAllValidators();
			numberOfDeath.addValidator(animalDeathValidator);
			addNumberFormatValidator(entity);
		}

	}

	private void removeDeathCountExceedsCaseCountValidator(String entity) {
		if (entity.equals(PERSON)) {
			personDeathValidator.setValidDeath(true);
			numberOfDeathPerson.removeValidator(personDeathValidator);
		} else {
			animalDeathValidator.setValidDeath(true);
			numberOfDeath.removeValidator(animalDeathValidator);
		}
	}

	private void CheckCaseNull(String entity, String value, String numberOfCases) {
		if (!isNullOrEmpty(value) && isNullOrEmpty(numberOfCases) && entity.equals(PERSON)) {
			int valueToInt = Integer.parseInt(value);
			numberOfPersonCases.setValue(String.valueOf(valueToInt));
		}
		if (!isNullOrEmpty(value) && isNullOrEmpty(numberOfCases) && entity.equals(ANIMAL)) {
			int valueToInt = Integer.parseInt(value);
			numberOfPersonAnimal.setValue(String.valueOf(valueToInt));
		}
	}

	public void validateSignalVerificationVerificationCompleteDate(EbsDto selectedEbs) {
		Date dateOfReport = selectedEbs.getReportDateTime();
		Date dateOfDecision = selectedEbs.getTriaging().getDecisionDate();
		DateField verificationCompleteDateField = verificationCompleteDate;
		Date DateverificationCompleteDate = verificationCompleteDateField.getValue();
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
					verificationCompleteDateField.addValidator(completeDateValidator);
					addVerificationDateValidator();
				} else {
					verificationCompleteDate.removeAllValidators();
				}
			} else {
				verificationCompleteDate.removeAllValidators();
			}
		}
	}

	public void validateSignalVerificationDateOfOccurrence(EbsDto selectedEbs) {
		Date dateOfReport = selectedEbs.getReportDateTime();
		DateTimeField DateOfOccurrenceField = DateOfOccurrence;
		if (dateOfReport == null) {
			dateOfReport = new Date(0);
		}

		Date dateOfReportDate = clearTime(dateOfReport);
		if (DateOfOccurrenceField.getValue() != null) {
			if (DateOfOccurrenceField.getValue().after(dateOfReportDate)) {
				DateOfOccurrenceField.addValidator(dateValidator);
				addOccurrenceDateValidator();
			} else {
				DateOfOccurrence.removeAllValidators();
			}
		}
        DateOfOccurrence.addValidator(new FutureDateValidator(DateOfOccurrence, 0, I18nProperties.getCaption(Captions.SignalVerification_dateOfOccurrence)));
    }

	private Date clearTime(Date date) {
		assert date != null;
		Calendar calendar = Calendar.getInstance();
		if (date == null) {
			date = new Date();
		}
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private void addVerificationDateValidator() {
		dateValidator.setValidDate(false);
		verificationCompleteDate.removeValidator(completeDateValidator);
		verificationCompleteDate.addValidator(completeDateValidator);
	}

	private void addOccurrenceDateValidator() {
		dateValidator.setValidDate(false);
	}

}
