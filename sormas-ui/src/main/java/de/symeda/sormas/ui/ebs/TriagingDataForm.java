package de.symeda.sormas.ui.ebs;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.*;
import com.vaadin.ui.Label;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.NullableOptionGroup;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class TriagingDataForm extends AbstractEditForm<TriagingDto> {

    private static final long serialVersionUID = 1L;
    private static final String SIGNAL_INFORMATION_LOC = "signalInformationLoc";
    private static final String TRIAGING_DECISION_LOC = "triagingDecisionLoc";

    private final EbsDto ebs;
    private final Class<? extends EntityDto> parentClass;

    private static final String HTML_LAYOUT =
            loc(SIGNAL_INFORMATION_LOC) +
                    fluidRowLocs(TriagingDto.SPECIFIC_SIGNAL) +
                    fluidRowLocs(TriagingDto.SUPERVISOR_REVIEW) +
                    fluidRowLocs(TriagingDto.OUTCOME_SUPERVISOR,"") +
                    fluidRowLocs(TriagingDto.POTENTIAL_RISK) +
                    fluidRowLocs(TriagingDto.HEALTH_CONCERN) +
                    fluidRowLocs(TriagingDto.SIGNAL_CATEGORY) +
                    fluidRowLocs(TriagingDto.CATEGORY_DETAILS_LEVEL) +
                    fluidRowLocs(TriagingDto.HUMAN_COMMUNITY_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.HUMAN_LABORATORY_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.HUMAN_FACILITY_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.ANIMAL_COMMUNITY_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.ANIMAL_FACILITY_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.ENVIRONMENTAL_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.POE_CATEGORY_DETAILS,"") +
                    fluidRowLocs(TriagingDto.OCCURRENCE_PREVIOUSLY) +
                    fluidRowLocs(TriagingDto.REFERRED) +
                    fluidRowLocs(TriagingDto.REFERRED_TO, "") +
                    loc(TRIAGING_DECISION_LOC) +
                    fluidRowLocs(TriagingDto.TRIAGING_DECISION,TriagingDto.DATE_OF_DECISION);


    TriagingDataForm(EbsDto ebsDto, Class<? extends EntityDto> parentClass, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed){
        super(
                TriagingDto.class,
                TriagingDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                createFieldAccessCheckers(isPseudonymized, inJurisdiction, true),
                ebsDto);
        this.ebs = ebsDto;
        this.parentClass = parentClass;
        addFields();
    }

    private NullableOptionGroup signalCategory;
    private OptionGroup humanCommCategoryDetails;
    private OptionGroup triagingDecision;
    private DateField dateOfDecision;
    private TextField referredTo;
    private NullableOptionGroup previousOccurrence;
    private NullableOptionGroup healthConcern;
    private NullableOptionGroup potentialRisk;
    private NullableOptionGroup supervisorReview;
    private NullableOptionGroup referred;
    private Label headingSignalInformation;
    private OptionGroup humanFacCategoryDetails;
    private OptionGroup humanLabCategoryDetails;
    private OptionGroup animalCommCategoryDetails;
    private OptionGroup animalFacCategoryDetails;
    private OptionGroup environmentalCategoryDetails;
    private OptionGroup poeCategoryDetails;
    private NullableOptionGroup categoryLevel;

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

        Label headingTriagingDecision = new Label(I18nProperties.getString(Strings.headingTriagingDecision));
        headingTriagingDecision.addStyleName(H3);
        getContent().addComponent(headingTriagingDecision, TRIAGING_DECISION_LOC);

        NullableOptionGroup specificSignal = addField(TriagingDto.SPECIFIC_SIGNAL, NullableOptionGroup.class);
        healthConcern = addField(TriagingDto.HEALTH_CONCERN, NullableOptionGroup.class);
        potentialRisk = addField(TriagingDto.POTENTIAL_RISK, NullableOptionGroup.class);
        supervisorReview = addField(TriagingDto.SUPERVISOR_REVIEW, NullableOptionGroup.class);
        referred = addField(TriagingDto.REFERRED, NullableOptionGroup.class);
        signalCategory = addField(TriagingDto.SIGNAL_CATEGORY, NullableOptionGroup.class);
        categoryLevel = addField(TriagingDto.CATEGORY_DETAILS_LEVEL, NullableOptionGroup.class);
        humanCommCategoryDetails = addField(TriagingDto.HUMAN_COMMUNITY_CATEGORY_DETAILS, OptionGroup.class);
        humanCommCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        humanCommCategoryDetails.setMultiSelect(true);
        humanCommCategoryDetails.addItems(
                Arrays.stream(HumanCommunityCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(HumanCommunityCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        humanCommCategoryDetails.setVisible(false);
        humanFacCategoryDetails = addField(TriagingDto.HUMAN_FACILITY_CATEGORY_DETAILS, OptionGroup.class);
        humanFacCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        humanFacCategoryDetails.setMultiSelect(true);
        humanFacCategoryDetails.addItems(
                Arrays.stream(HumanFaclityCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(HumanFaclityCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        humanFacCategoryDetails.setVisible(false);
        humanLabCategoryDetails = addField(TriagingDto.HUMAN_LABORATORY_CATEGORY_DETAILS, OptionGroup.class);
        humanLabCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        humanLabCategoryDetails.setMultiSelect(true);
        humanLabCategoryDetails.addItems(
                Arrays.stream(HumanLaboratoryCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(HumanLaboratoryCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        humanLabCategoryDetails.setVisible(false);
        animalCommCategoryDetails = addField(TriagingDto.ANIMAL_COMMUNITY_CATEGORY_DETAILS, OptionGroup.class);
        animalCommCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        animalCommCategoryDetails.setMultiSelect(true);
        animalCommCategoryDetails.addItems(
                Arrays.stream(AnimalCommunityCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(AnimalCommunityCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        animalCommCategoryDetails.setVisible(false);
        animalFacCategoryDetails = addField(TriagingDto.ANIMAL_FACILITY_CATEGORY_DETAILS, OptionGroup.class);
        animalFacCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        animalFacCategoryDetails.setMultiSelect(true);
        animalFacCategoryDetails.addItems(
                Arrays.stream(AnimalFacilityCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(AnimalFacilityCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        animalFacCategoryDetails.setVisible(false);
        environmentalCategoryDetails = addField(TriagingDto.ENVIRONMENTAL_CATEGORY_DETAILS, OptionGroup.class);
        environmentalCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        environmentalCategoryDetails.setMultiSelect(true);
        environmentalCategoryDetails.addItems(
                Arrays.stream(EnvironmentalCategoryDetails.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(EnvironmentalCategoryDetails.class, category.name()))
                        .collect(Collectors.toList())
        );
        environmentalCategoryDetails.setVisible(false);
        poeCategoryDetails = addField(TriagingDto.POE_CATEGORY_DETAILS, OptionGroup.class);
        poeCategoryDetails.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        poeCategoryDetails.setMultiSelect(true);
        poeCategoryDetails.addItems(
                Arrays.stream(POE.values())
                        .filter(category -> fieldVisibilityCheckers.isVisible(POE.class, category.name()))
                        .collect(Collectors.toList())
        );
        poeCategoryDetails.setVisible(false);
        previousOccurrence = addField(TriagingDto.OCCURRENCE_PREVIOUSLY, NullableOptionGroup.class);
        triagingDecision = addField(TriagingDto.TRIAGING_DECISION, OptionGroup.class);
        triagingDecision.setMultiSelect(false);
        triagingDecision.addItems(
                Arrays.stream(EbsTriagingDecision.values())
                        .filter(decision -> fieldVisibilityCheckers.isVisible(EbsTriagingDecision.class, decision.name()))
                        .collect(Collectors.toList())
        );
        triagingDecision.addStyleName(CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        dateOfDecision = addField(TriagingDto.DATE_OF_DECISION, DateField.class);
        referredTo = addField(TriagingDto.REFERRED_TO, TextField.class);
        ComboBox outcomeSupervisor = addField(TriagingDto.OUTCOME_SUPERVISOR, ComboBox.class);
        referredTo.setVisible(false);
        supervisorReview.setVisible(false);
        potentialRisk.setVisible(false);
        healthConcern.setVisible(false);
        referred.setVisible(false);
        triagingDecision.setVisible(false);
        dateOfDecision.setVisible(false);
        EbsDto selectedEbs = getEbsDto();

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(TriagingDto.OCCURRENCE_PREVIOUSLY),
                TriagingDto.SPECIFIC_SIGNAL,
                Arrays.asList(YesNo.YES),
                true);

        specificSignal.addValueChangeListener(e->{
            if(Objects.equals(e.getProperty().getValue().toString(), "[YES]")){
                signalCategory.setVisible(true);
                previousOccurrence.setVisible(true);
//                previousOccurrence.setRequired(true);
                triagingDecision.setVisible(true);
                triagingDecision.setRequired(true);
                dateOfDecision.setVisible(true);
                dateOfDecision.setRequired(true);
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                potentialRisk.setVisible(false);
                supervisorReview.setVisible(false);
                potentialRisk.setValue(null);
                supervisorReview.setValue(null);
                referred.setVisible(false);
                referred.setValue(null);
                outcomeSupervisor.setVisible(false);
            }else if ((Objects.equals(e.getProperty().getValue().toString(), "[NO]"))){
                previousOccurrence.setValue(null);
                previousOccurrence.setVisible(false);
                previousOccurrence.setRequired(false);
                supervisorReview.setVisible(true);
                signalCategory.setVisible(false);
                signalCategory.setValue(null);
                categoryLevel.setVisible(false);
                categoryLevel.setValue(null);
                selectedEbs.getTriaging().setCategoryDetailsLevel(null);
                selectedEbs.getTriaging().setSignalCategory(null);
                selectedEbs.getTriaging().setHumanCommunityCategoryDetails(null);
                selectedEbs.getTriaging().setHumanFacilityCategoryDetails(null);
                selectedEbs.getTriaging().setHumanLaboratoryCategoryDetails(null);
                humanCommCategoryDetails.setVisible(false);
                humanCommCategoryDetails.setValue(null);
                humanFacCategoryDetails.setVisible(false);
                humanFacCategoryDetails.setValue(null);
                humanLabCategoryDetails.setVisible(false);
                humanLabCategoryDetails.setValue(null);
                if (ebs.getTriaging().getSupervisorReview() == null) {
                    reviewSignal(Strings.seniorOfficials,selectedEbs);
                }
                selectedEbs.getTriaging().setAnimalCommunityCategoryDetails(null);
                selectedEbs.getTriaging().setAnimalFacilityCategoryDetails(null);

                animalCommCategoryDetails.setVisible(false);
                animalFacCategoryDetails.setVisible(false);  // Repeated for lab

                selectedEbs.getTriaging().setEnvironmentalCategoryDetails(null);
                selectedEbs.getTriaging().setPoeCategoryDetails(null);
                environmentalCategoryDetails.setVisible(false);  // Shown for all levels

                poeCategoryDetails.setVisible(false);
            }
        });
        signalCategory.addValueChangeListener(e->{
            final Set<String> validCategories = new HashSet<>(Set.of("[Human]", "[Environment]", "[Animal]", "[POE]"));
            String propertyValue = e.getProperty().getValue().toString();
            if (validCategories.contains(propertyValue)) {
                categoryLevel.setVisible(true);
            } else {
                categoryLevel.setVisible(false);
            }
            if (categoryLevel.getValue() != null) {
                try {
                    categoryLevel.setValue(CategoryDetailsLevel.COMMUNITY);
                    SignalCategory category = getSignalCategory(propertyValue);
                    setVisibility(categoryLevel.getNullableValue().toString(), category);
                }catch (Exception exception){
                    System.out.println(exception.getMessage());
                }

            }
            try {
                displayCategories(e.getProperty().getValue().toString());
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }

        });

        categoryLevel.addValueChangeListener(valueChangeEvent -> {
            var level = valueChangeEvent.getProperty().getValue().toString();
            var category = signalCategory.getNullableValue();
            setVisibility(level, (SignalCategory) category);
        });

        previousOccurrence.addValueChangeListener(e -> {
            if (e.getProperty().getValue().toString().equals("[NO]")) {
                triagingDecision.setValue(EbsTriagingDecision.VERIFY);
                selectedEbs.getSignalVerification().setVerificationSent(YesNo.YES);
                selectedEbs.getSignalVerification().setDateOfOccurrence(new Date());
            } else {
                triagingDecision.setValue(EbsTriagingDecision.DISCARD);
                selectedEbs.getSignalVerification().setVerificationSent(YesNo.NO);
            }
        });

        supervisorReview.addValueChangeListener(e->{
            if (e.getProperty().getValue().toString().equals("[YES]")) {
                outcomeSupervisor.setVisible(true);
                triagingDecision.setVisible(true);
                dateOfDecision.setVisible(true);
                triagingDecision.setRequired(true);
                dateOfDecision.setRequired(true);
            }
            else if (e.getProperty().getValue().toString().equals("[NO]")) {
                if (ebs.getTriaging().getSupervisorReview() != YesNo.NO) {
                    reviewSignal(Strings.seniorOfficials,selectedEbs);
                }
                potentialRisk.setVisible(false);
                potentialRisk.setValue(null);
                dateOfDecision.setRequired(false);
                dateOfDecision.setValue(null);
                dateOfDecision.setVisible(false);
                triagingDecision.setVisible(false);
                triagingDecision.setRequired(false);
                triagingDecision.setValue(null);
                outcomeSupervisor.setVisible(false);
                outcomeSupervisor.setValue(OutComeSupervisor.ISNOTSIGNAL);
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                referred.setVisible(false);
                referred.setValue(null);
                referredTo.setVisible(false);
                referredTo.setValue(null);
            }
        });

        outcomeSupervisor.addValueChangeListener(e->{
            if (e.getProperty().getValue().equals(OutComeSupervisor.ISSIGNAL)) {
                healthConcern.setVisible(true);
                previousOccurrence.setVisible(true);
            }
            else if (e.getProperty().getValue().equals(OutComeSupervisor.ISNOTSIGNAL)) {
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                referredTo.setVisible(false);
                referred.setVisible(false);
            }
        });
        healthConcern.addValueChangeListener(e->{
            if (e.getProperty().getValue().toString().equals("[NO]")) {
                triagingDecision.setValue(EbsTriagingDecision.DISCARD);
                referredTo.setVisible(false);
                referred.setVisible(false);
                referredTo.setValue(null);
                referred.setValue(null);
            }
            else if (e.getProperty().getValue().toString().equals("[YES]")) {
                if (ebs.getTriaging().getHealthConcern() != YesNo.YES) {
                    reviewSignal(Strings.referredNotifs,selectedEbs);
                }
                referred.setVisible(true);
            }
        });
        referred.addValueChangeListener(e->{
            if (e.getProperty().getValue().toString().equals("[YES]")) {
                referredTo.setVisible(true);
                triagingDecision.setValue(EbsTriagingDecision.MORE_INFORMATION);
            }
            else if (e.getProperty().getValue().toString().equals("[NO]")) {
                referredTo.setVisible(false);
                referredTo.setValue(null);
            }
        });


        if (selectedEbs != null) {
            TriagingDto selectedTriaging = selectedEbs.getTriaging();
            if(selectedTriaging.getCategoryDetailsLevel() != null  ) {
                setVisibility(selectedTriaging.getCategoryDetailsLevel().toString(), selectedTriaging.getSignalCategory());
            }
        }

        if (selectedEbs.getTriaging().getSpecificSignal() != null) {
            if (Objects.equals(selectedEbs.getTriaging().getSpecificSignal().toString(), "YES")) {
                healthConcern.setVisible(true);
                signalCategory.setVisible(true);
            }else if (Objects.equals(selectedEbs.getTriaging().getSpecificSignal().toString(), "NO")) {
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                potentialRisk.setVisible(false);
                potentialRisk.setValue(null);
                signalCategory.setVisible(false);
                signalCategory.setValue(null);
                categoryLevel.setVisible(false);
                categoryLevel.setValue(null);
                outcomeSupervisor.setVisible(true);
                humanCommCategoryDetails.setVisible(false);
                humanCommCategoryDetails.setValue(null);
                humanFacCategoryDetails.setVisible(false);
                humanFacCategoryDetails.setValue(null);
                humanLabCategoryDetails.setValue(null);

                animalCommCategoryDetails.setVisible(false);
                animalCommCategoryDetails.setValue(null);
                animalFacCategoryDetails.setVisible(false);  // Repeated for lab
                animalFacCategoryDetails.setValue(null);  // Repeated for lab

                environmentalCategoryDetails.setVisible(false);  // Shown for all levels
                environmentalCategoryDetails.setValue(null);  // Shown for all levels

                poeCategoryDetails.setVisible(false);  // Shown for all levels
                poeCategoryDetails.setValue(null);  // Shown for all levels
            }
        }else {
            outcomeSupervisor.setVisible(false);
            signalCategory.setVisible(false);
            signalCategory.setValue(null);
            categoryLevel.setVisible(false);
            categoryLevel.setValue(null);
        }
        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();
    }

    private static @Nullable SignalCategory getSignalCategory(String propertyValue) {
        SignalCategory category = null;
        switch (propertyValue) {
            case "[Human]":
                category = SignalCategory.HUMAN;
                break;
            case "[Environment]":
                category = SignalCategory.ENVIRONMENT;
                break;
            case "[Animal]":
                category = SignalCategory.ANIMAL;
                break;
            case "[POE]":
                category = SignalCategory.POE;
                break;
        }
        return category;
    }


    private void setVisibility(String level, SignalCategory category) {
        // Hide all category details by default
        humanCommCategoryDetails.setVisible(false);
        humanFacCategoryDetails.setVisible(false);
        humanLabCategoryDetails.setVisible(false);
        animalCommCategoryDetails.setVisible(false);
        animalFacCategoryDetails.setVisible(false);
        environmentalCategoryDetails.setVisible(false);
        poeCategoryDetails.setVisible(false);

        if (level == null || category == null) {
            return;
        }

        boolean isCommunityLevel = "[Community]".equals(level);
        boolean isFacilityLevel = "[Facility]".equals(level);
        boolean isLaboratoryLevel = "[Laboratory]".equals(level);

        switch (category) {
            case HUMAN:
                humanCommCategoryDetails.setVisible(isCommunityLevel);
                humanFacCategoryDetails.setVisible(isFacilityLevel);
                humanLabCategoryDetails.setVisible(isLaboratoryLevel);
                break;
            case ANIMAL:
                animalCommCategoryDetails.setVisible(isCommunityLevel);
                animalFacCategoryDetails.setVisible(isFacilityLevel || isLaboratoryLevel);
                break;
            case ENVIRONMENT:
                environmentalCategoryDetails.setVisible(true);
                break;
            case POE:
                poeCategoryDetails.setVisible(true);
                break;
        }
    }
    public void reviewSignal(String captionsText,EbsDto ebs){
        Label notificationType =  new Label( String.format(I18nProperties.getString(captionsText),50,50), ContentMode.HTML);
        VerticalLayout verticalLayout = new VerticalLayout();
        notificationType.setStyleName("window-text");
        verticalLayout.addComponents(notificationType);
        verticalLayout.setWidth(100, Unit.PERCENTAGE);
        Window window =  VaadinUiUtil.showPopupWindow(
                verticalLayout,
                I18nProperties.getCaption(Captions.signalReview));
        window.setWidth(50, Unit.PERCENTAGE);
        window.setStyleName("low-risk-assessment");
    }

    public void displayCategories(String property){
        List<CategoryDetailsLevel> categories;
        switch (property) {
            case "[Environment]":
            case "[POE]":
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY);
                break;
            case "[Animal]":
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY, CategoryDetailsLevel.FACILITY);
                break;
            case "[Human]":
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY, CategoryDetailsLevel.FACILITY, CategoryDetailsLevel.LABORATORY);
                break;
            default:
                categories = Collections.emptyList();
                break;
        }
        try {
            FieldHelper.updateEnumData(categoryLevel, categories);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

}


