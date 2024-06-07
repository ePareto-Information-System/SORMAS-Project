package de.symeda.sormas.ui.ebs;


import com.vaadin.ui.Label;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.NullableOptionGroup;
;
import java.util.Arrays;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class SignalVerificationDataForm extends AbstractEditForm<SignalVerificationDto> {

    private static final long serialVersionUID = 1L;
    private static final String EVENT_DETAILS_LOC = "eventDetailsLoc";
    private static final String SIGNAL_VERIFICATION_LOC = "signalVerificationLoc";

    private static final String SIGNAL_VERIFICATION_ENTITY = "SignalVerification";

    private final EbsDto ebs;
    private final Class<? extends EntityDto> parentClass;

    private static final String HTML_LAYOUT =
    loc(SIGNAL_VERIFICATION_LOC) +
     fluidRowLocs(SignalVerificationDto.VERIFICATION_SENT, SignalVerificationDto.VERIFICATION_SENT_DATE) +
    fluidRowLocs(SignalVerificationDto.VERIFIED) +
    fluidRowLocs(SignalVerificationDto.VERIFICATION_COMPLETE_DATE) +
            loc(EVENT_DETAILS_LOC) +
    fluidRowLocs(SignalVerificationDto.DATE_OF_OCCURRENCE, SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL, SignalVerificationDto.NUMBER_OF_DEATH) +
    fluidRowLocs(SignalVerificationDto.DESCRIPTION) +
    fluidRowLocs(SignalVerificationDto.WHY_NOT_VERIFY);

    SignalVerificationDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed){
        super(
            SignalVerificationDto.class,
            SignalVerificationDto.I18N_PREFIX,
            false,
            FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
            createFieldAccessCheckers(isPseudonymized, inJurisdiction, true),ebsDto);
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
        Label signalVerification = new Label(I18nProperties.getString(Strings.headingSignalVerification));
        signalVerification.addStyleName(H3);
        getContent().addComponent(signalVerification, SIGNAL_VERIFICATION_LOC);

        Label headingEventDetails = new Label(I18nProperties.getString(Strings.headingEventDetails));
        headingEventDetails.addStyleName(H3);
        getContent().addComponent(headingEventDetails, EVENT_DETAILS_LOC);
        NullableOptionGroup sentVerification = addField(SignalVerificationDto.VERIFICATION_SENT, NullableOptionGroup.class);
        NullableOptionGroup verified = addField(SignalVerificationDto.VERIFIED, NullableOptionGroup.class);
        addField(SignalVerificationDto.VERIFICATION_SENT_DATE, DateField.class);
        addField(SignalVerificationDto.VERIFICATION_COMPLETE_DATE, DateField.class);
        addField(SignalVerificationDto.DATE_OF_OCCURRENCE, DateField.class);
        addField(SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL, TextField.class);
        addField(SignalVerificationDto.NUMBER_OF_DEATH, TextField.class);
        addField(SignalVerificationDto.DESCRIPTION, TextArea.class);
        addField(SignalVerificationDto.WHY_NOT_VERIFY, TextArea.class);


        EbsDto selectedEbs = getEbsDto();

        if (selectedEbs.getSignalVerification().getVerificationSent() == YesNo.YES){
            sentVerification.setReadOnly(true);
        }

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(SignalVerificationDto.DESCRIPTION,SignalVerificationDto.VERIFICATION_COMPLETE_DATE,SignalVerificationDto.DATE_OF_OCCURRENCE,SignalVerificationDto.NUMBER_OF_PERSON_ANIMAL,SignalVerificationDto.NUMBER_OF_DEATH),
                SignalVerificationDto.VERIFIED,
                Arrays.asList(YesNo.YES),
                true);
        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(SignalVerificationDto.WHY_NOT_VERIFY),
                SignalVerificationDto.VERIFIED,
                Arrays.asList(YesNo.NO),
                true);

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(SignalVerificationDto.VERIFICATION_SENT_DATE,SignalVerificationDto.VERIFIED),
                SignalVerificationDto.VERIFICATION_SENT,
                Arrays.asList(YesNo.YES),
                true);
        verified.addValueChangeListener(event -> {
            if (event.getProperty().getValue().toString().equals("[YES]")){
                getContent().getComponent(EVENT_DETAILS_LOC).setVisible(true);
            }else{
                getContent().getComponent(EVENT_DETAILS_LOC).setVisible(false);

            }
        });


        setRequired(true,SignalVerificationDto.VERIFICATION_SENT);
    }


}
