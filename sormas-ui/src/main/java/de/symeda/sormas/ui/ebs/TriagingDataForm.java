package de.symeda.sormas.ui.ebs;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.ui.utils.*;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.util.Arrays;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class TriagingDataForm extends AbstractEditForm<TriagingDto> {

    private static final long serialVersionUID = 1L;
    private static final String SIGNAL_INFORMATION_LOC = "signalInformationLoc";
    private static final String TRIAGING_DECISION_LOC = "triagingDecisionLoc";

    private static final String TRIAGING_ENTITY = "Triaging";

    private final EbsDto ebs;
    private final Class<? extends EntityDto> parentClass;

    private static final String HTML_LAYOUT =
            loc(SIGNAL_INFORMATION_LOC) +
                    fluidRowLocs(TriagingDto.EARLY_WARNING) +
                    fluidRowLocs(TriagingDto.SPECIFIC_SIGNAL) +
                    fluidRowLocs(TriagingDto.HEALTH_CONCERN, TriagingDto.OUTCOME_SUPERVISOR) +
                    fluidRowLocs(TriagingDto.SIGNAL_CATEGORY) +
                    fluidRowLocs(TriagingDto.CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.OCCURRENCE_PREVIOUSLY) +
            loc(TRIAGING_DECISION_LOC) +
                    fluidRowLocs(TriagingDto.TRIAGING_DECISION,TriagingDto.DATE_OF_DECISION) +
                    fluidRowLocs(TriagingDto.REFERRED_TO, "");

    TriagingDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed){
        super(
                TriagingDto.class,
                TriagingDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                createFieldAccessCheckers(isPseudonymized, inJurisdiction, true));
            this.ebs = ebsDto;
        this.parentClass = parentClass;
            addFields();
    }

    private NullableOptionGroup signalCategory;
    private OptionGroup categoryDetails;
    private OptionGroup triagingDecision;
    private DateTimeField dateOfDecision;
    private TextField referredTo;
    private Label headingTriagingDecision;
    private NullableOptionGroup previousOccurrence;
    private NullableOptionGroup healthConcern;
    private Label headingSignalInformation;

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

    @SuppressWarnings("deprecation")
    @Override
    protected void addFields() {
        if (ebs == null) {
            return;
        }
        headingSignalInformation = new Label(I18nProperties.getString(Strings.headingSignalInformation));
        headingSignalInformation.addStyleName(H3);
        getContent().addComponent(headingSignalInformation, SIGNAL_INFORMATION_LOC);

        headingTriagingDecision = new Label(I18nProperties.getString(Strings.headingTriagingDecision));
        headingTriagingDecision.addStyleName(H3);
        getContent().addComponent(headingTriagingDecision, TRIAGING_DECISION_LOC);

        NullableOptionGroup earlyWarning = addField(TriagingDto.EARLY_WARNING, NullableOptionGroup.class);
        NullableOptionGroup specificSignal = addField(TriagingDto.SPECIFIC_SIGNAL, NullableOptionGroup.class);
        healthConcern = addField(TriagingDto.HEALTH_CONCERN, NullableOptionGroup.class);
        signalCategory = addField(TriagingDto.SIGNAL_CATEGORY, NullableOptionGroup.class);
        categoryDetails = addField(TriagingDto.CATEGORY_DETAILS, OptionGroup.class);
        categoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        categoryDetails.setMultiSelect(true);
        categoryDetails.addItems(
                Arrays.stream(CategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(CategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        previousOccurrence = addField(TriagingDto.OCCURRENCE_PREVIOUSLY, NullableOptionGroup.class);
        triagingDecision = addField(TriagingDto.TRIAGING_DECISION, OptionGroup.class);
        triagingDecision.setMultiSelect(false);
        triagingDecision.addItems(
                Arrays.stream(EbsTriagingDecision.values())
                        .filter(decision -> fieldVisibilityCheckers.isVisible(EbsTriagingDecision.class, decision.name()))
                        .collect(Collectors.toList())
        );
        triagingDecision.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        dateOfDecision = addField(TriagingDto.DATE_OF_DECISION, DateTimeField.class);
        referredTo = addField(TriagingDto.REFERRED_TO, TextField.class);
        ComboBox outcomeSupervisor = addField(TriagingDto.OUTCOME_SUPERVISOR, ComboBox.class);

        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();

        previousOccurrence.addValueChangeListener(e -> {
            if (e.getProperty().getValue().toString().equals("[NO]")) {
                triagingDecision.setValue(EbsTriagingDecision.VERIFY);
            } else {
                triagingDecision.setValue(EbsTriagingDecision.DISCARD);
            }
        });


        outcomeSupervisor.addValueChangeListener(event -> {
            OutComeSupervisor supervisor = (OutComeSupervisor) event.getProperty().getValue();
            switch (supervisor) {
                case ISNOTSIGNAL:
                    showNonSignalDetails();
                    break;
                case NOTREVIEWED:
                    hideAllDetails();
                    break;
                default:
                    showSignalDetails();
                    break;
            }
        });
    }
    private void showSignalDetails() {
        signalCategory.setVisible(true);
        categoryDetails.setVisible(true);
        triagingDecision.setVisible(true);
        dateOfDecision.setVisible(true);
        referredTo.setVisible(true);
        headingTriagingDecision.setVisible(true);
        previousOccurrence.setVisible(true);
        healthConcern.setVisible(true);
    }

    private void showNonSignalDetails() {
        signalCategory.setVisible(false);
        categoryDetails.setVisible(false);
        triagingDecision.setVisible(true);
        headingSignalInformation.setVisible(true);
        headingTriagingDecision.setVisible(true);
        dateOfDecision.setVisible(true);
        previousOccurrence.setVisible(false);
        healthConcern.setVisible(false);
    }

    private void hideAllDetails() {
        signalCategory.setVisible(false);
        categoryDetails.setVisible(false);
        triagingDecision.setVisible(false);
        dateOfDecision.setVisible(false);
        referredTo.setVisible(false);
        headingTriagingDecision.setVisible(false);
        previousOccurrence.setVisible(false);
        healthConcern.setVisible(false);
    }

}


