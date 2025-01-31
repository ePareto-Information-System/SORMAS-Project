package de.symeda.sormas.ui.ebs;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

import java.util.Arrays;
import java.util.Collections;

import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsAlertDto;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

public class EbsAlertDataForm extends AbstractEditForm<EbsAlertDto> {

	private static final long serialVersionUID = 1L;

	private final EbsDto ebs;
	private final Class<? extends EntityDto> parentClass;
	NullableOptionGroup actionInitiated;
	OptionGroup responseStatus;
	TextArea detailsResponse;
	NullableOptionGroup alertIssued;
	DateField alertDate;
	DateField responseDate;
	TextArea details_alert_issued;
	private static final String HTML_LAYOUT = fluidRowLocs(EbsAlertDto.ALERT_ISSUED, EbsAlertDto.ALERT_DATE)
		+ fluidRowLocs(EbsAlertDto.DETAILS_ALERT_USED)
		+ fluidRowLocs(EbsAlertDto.ACTION_INITIATED)
		+ fluidRowLocs(EbsAlertDto.RESPONSE_STATUS, EbsAlertDto.RESPONSE_DATE)
		+ fluidRowLocs(EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES);

	EbsAlertDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed) {
		super(
			EbsAlertDto.class,
			EbsAlertDto.I18N_PREFIX,
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

		actionInitiated = addField(EbsAlertDto.ACTION_INITIATED, NullableOptionGroup.class);
		responseStatus = addField(EbsAlertDto.RESPONSE_STATUS, OptionGroup.class);
		detailsResponse = addField(EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES, TextArea.class);
		alertIssued = addField(EbsAlertDto.ALERT_ISSUED, NullableOptionGroup.class);
		alertDate =  addField(EbsAlertDto.ALERT_DATE, DateField.class);
		responseDate =  addField(EbsAlertDto.RESPONSE_DATE, DateField.class);
		details_alert_issued =  addField(EbsAlertDto.DETAILS_ALERT_USED, TextArea.class);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsAlertDto.ALERT_DATE,EbsAlertDto.ACTION_INITIATED),
			EbsAlertDto.ALERT_ISSUED,
			Arrays.asList(YesNo.YES),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsAlertDto.DETAILS_ALERT_USED),
			EbsAlertDto.ALERT_ISSUED,
			Arrays.asList(YesNo.NO,YesNo.YES),
			true);
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(EbsAlertDto.RESPONSE_STATUS,EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES),
				EbsAlertDto.ACTION_INITIATED,
				Arrays.asList(YesNo.YES),
				true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsAlertDto.RESPONSE_DATE),
			EbsAlertDto.RESPONSE_STATUS,
			Arrays.asList(ResponseStatus.COMPLETED),
			true);

		alertIssued.addValueChangeListener(event -> {
			if (alertIssued.getValue() != null && alertIssued.getValue().equals(YesNo.YES)) {
				details_alert_issued.setRequired(true);
				alertDate.setRequired(true);
				actionInitiated.setRequired(true);
			}else {
				details_alert_issued.setRequired(false);
				alertDate.setRequired(false);
				actionInitiated.setRequired(false);
			}
		});
		alertIssued.addValueChangeListener(event -> {
			if (alertIssued.getValue() != null && alertIssued.getValue().equals(YesNo.NO)) {
				details_alert_issued.setRequired(true);
				details_alert_issued.setCaption(I18nProperties.getCaption(Captions.EbsAlert_reasonNoAlert));
			}else {
				details_alert_issued.setCaption(I18nProperties.getCaption(Captions.EbsAlert_detailsAlertUsed));
				details_alert_issued.setValue("");
			}
		});

		actionInitiated.addValueChangeListener(event -> {
			if (actionInitiated.getValue() != null && actionInitiated.getValue().equals(YesNo.YES)) {
				detailsResponse.setRequired(true);
				responseStatus.setRequired(true);
			}else{
				detailsResponse.setRequired(false);
				responseStatus.setRequired(false);
			}
		});
		responseStatus.addValueChangeListener(event -> {
			if (responseStatus.getValue() != null && responseStatus.getValue().equals(ResponseStatus.COMPLETED)) {
				responseDate.setRequired(true);
			}else {
				responseDate.setRequired(false);
			}
		});

		setRequired(
				true,
				EbsAlertDto.ALERT_ISSUED);
	}
}
