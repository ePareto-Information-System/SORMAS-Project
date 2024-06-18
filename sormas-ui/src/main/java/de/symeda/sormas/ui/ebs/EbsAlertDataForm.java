package de.symeda.sormas.ui.ebs;

import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsAlertDto;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.util.Arrays;
import java.util.Collections;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

public class EbsAlertDataForm extends AbstractEditForm<EbsAlertDto> {

    private static final long serialVersionUID = 1L;

    private final EbsDto ebs;
    private final Class<? extends EntityDto> parentClass;

    private static final String HTML_LAYOUT =
            fluidRowLocs(EbsAlertDto.ALERT_ISSUED,EbsAlertDto.ALERT_DATE) +
            fluidRowLocs(EbsAlertDto.DETAILS_ALERT_USED) +
            fluidRowLocs(EbsAlertDto.ACTION_INITIATED) +
            fluidRowLocs(EbsAlertDto.RESPONSE_STATUS,EbsAlertDto.RESPONSE_DATE) +
            fluidRowLocs(EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES);


    EbsAlertDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed){
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

        addField(EbsAlertDto.ACTION_INITIATED, NullableOptionGroup.class);
        addField(EbsAlertDto.RESPONSE_STATUS, OptionGroup.class);
        addField(EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES, TextArea.class);
        addField(EbsAlertDto.ALERT_ISSUED, NullableOptionGroup.class);
        addField(EbsAlertDto.ALERT_DATE, DateField.class);
        addField(EbsAlertDto.RESPONSE_DATE, DateField.class);
        addField(EbsAlertDto.DETAILS_ALERT_USED, TextArea.class);

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(EbsAlertDto.DETAILS_ALERT_USED,EbsAlertDto.ALERT_DATE),
                EbsAlertDto.ALERT_ISSUED,
                Arrays.asList(YesNo.YES),
                true);

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(EbsAlertDto.RESPONSE_DATE),
                EbsAlertDto.RESPONSE_STATUS,
                Arrays.asList(ResponseStatus.COMPLETED),
                true);

        FieldHelper.setRequiredWhen(
                getFieldGroup(),
                Arrays.asList(EbsAlertDto.DETAILS_ALERT_USED,EbsAlertDto.ALERT_DATE),
                Collections.singletonList(EbsAlertDto.ALERT_ISSUED),
                Arrays.asList(YesNo.YES));

        setRequired(true, EbsAlertDto.ACTION_INITIATED, EbsAlertDto.RESPONSE_STATUS, EbsAlertDto.ALERT_ISSUED,EbsAlertDto.DETAILS_RESPONSE_ACTIVITIES);
    }
}
