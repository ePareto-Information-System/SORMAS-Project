package de.symeda.sormas.ui.ebs;

import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ebs.*;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
                    fluidRowLocs(TriagingDto.SPECIFIC_SIGNAL) +
                    fluidRowLocs(TriagingDto.POTENTIAL_RISK) +
                    fluidRowLocs(TriagingDto.HEALTH_CONCERN) +
                    fluidRowLocs(TriagingDto.REFERRED_TO, "") +
                    fluidRowLocs(TriagingDto.OUTCOME_SUPERVISOR,"") +
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
    private Label headingSignalInformation;
    private OptionGroup humanFacCategoryDetails;
    private OptionGroup humanLabCategoryDetails;
    private OptionGroup animalCommCategoryDetails;
    private OptionGroup animalFacCategoryDetails;
    private OptionGroup environmentalCategoryDetails;
    private OptionGroup poeCategoryDetails;
    private OptionGroup categoryLevel;

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
        signalCategory = addField(TriagingDto.SIGNAL_CATEGORY, NullableOptionGroup.class);
        categoryLevel = addField(TriagingDto.CATEGORY_DETAILS_LEVEL, OptionGroup.class);
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
        potentialRisk.setVisible(false);
        healthConcern.setVisible(false);
        EbsDto selectedEbs = getEbsDto();

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(TriagingDto.OCCURRENCE_PREVIOUSLY),
                TriagingDto.POTENTIAL_RISK,
                Arrays.asList(YesNo.YES),
                true);
        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                Arrays.asList(TriagingDto.OCCURRENCE_PREVIOUSLY),
                TriagingDto.SPECIFIC_SIGNAL,
                Arrays.asList(YesNo.YES),
                true);

        specificSignal.addValueChangeListener(e->{
            if(Objects.equals(e.getProperty().getValue().toString(), "[YES]")){
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                potentialRisk.setVisible(false);
                potentialRisk.setValue(null);
                signalCategory.setVisible(true);
                outcomeSupervisor.setVisible(false);
            }else if ((Objects.equals(e.getProperty().getValue().toString(), "[NO]"))){
                potentialRisk.setVisible(true);
                signalCategory.setVisible(false);
                signalCategory.setValue(null);
                outcomeSupervisor.setVisible(true);
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

                Notification.show(I18nProperties.getString(Strings.seniorOfficials), Notification.Type.WARNING_MESSAGE);

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
            List<CategoryDetailsLevel> categories = null;
            var category = signalCategory.getNullableValue();
            if (Objects.equals(e.getProperty().getValue().toString(), "[Human]") || Objects.equals(e.getProperty().getValue().toString(), "[Environment]") || Objects.equals(e.getProperty().getValue().toString(), "[Animal]") || Objects.equals(e.getProperty().getValue().toString(), "[POE]")){
                categoryLevel.setVisible(true);
            }else {
                categoryLevel.setVisible(false);
            }
            if (Objects.equals(e.getProperty().getValue().toString(), "[Environment]") ||Objects.equals(e.getProperty().getValue().toString(), "[POE]") ){
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY);
                setVisibility("Community Level", (SignalCategory) category);
            } else if (Objects.equals(e.getProperty().getValue().toString(), "[Animal]")) {
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY,CategoryDetailsLevel.FACILITY);
                setVisibility("Community Level", (SignalCategory) category);
            }else if (Objects.equals(e.getProperty().getValue().toString(), "[Human]")) {
                categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY,CategoryDetailsLevel.FACILITY,CategoryDetailsLevel.LABORATORY);
                setVisibility("Community Level", (SignalCategory) category);
            }
            try {
                FieldHelper.updateEnumData(categoryLevel,categories);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
            categoryLevel.setVisible(true);

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

        potentialRisk.addValueChangeListener(e->{
            if (e.getProperty().getValue().toString().equals("[NO]")) {
                healthConcern.setVisible(true);
            }
            else if (e.getProperty().getValue().toString().equals("[YES]")) {
                healthConcern.setVisible(false);
                healthConcern.setValue(null);
                referredTo.setVisible(false);
            }
        });
        healthConcern.addValueChangeListener(e->{
            if (e.getProperty().getValue().toString().equals("[NO]")) {
                triagingDecision.setValue(EbsTriagingDecision.DISCARD);
                referredTo.setVisible(false);
                referredTo.setValue(null);
            }
            else if (e.getProperty().getValue().toString().equals("[YES]")) {
                referredTo.setVisible(true);
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
        setRequired(true,TriagingDto.DATE_OF_DECISION, TriagingDto.TRIAGING_DECISION);
        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();
    }


    private void setVisibility(String level, SignalCategory category) {
        if (level == null || category == null){
            return;
        }
        boolean isCommunityLevel = "Community Level".equals(level);
        boolean isFacilityLevel = "Facility Level".equals(level);
        boolean isLaboratoryLevel = "Laboratory Level".equals(level);

        humanCommCategoryDetails.setVisible(isCommunityLevel && category == SignalCategory.HUMAN);
        humanFacCategoryDetails.setVisible(isFacilityLevel && category == SignalCategory.HUMAN);
        humanLabCategoryDetails.setVisible(isLaboratoryLevel && category == SignalCategory.HUMAN);

        animalCommCategoryDetails.setVisible(isCommunityLevel && category == SignalCategory.ANIMAL);
        animalFacCategoryDetails.setVisible((isFacilityLevel || isLaboratoryLevel) && category == SignalCategory.ANIMAL);  // Repeated for lab

        environmentalCategoryDetails.setVisible(category == SignalCategory.ENVIRONMENT);  // Shown for all levels

        poeCategoryDetails.setVisible(category == SignalCategory.POE);  // Shown for all levels

        // Hide all other details that are not set to visible above
        if (!humanCommCategoryDetails.isVisible()) humanCommCategoryDetails.setVisible(false);
        if (!humanFacCategoryDetails.isVisible()) humanFacCategoryDetails.setVisible(false);
        if (!humanLabCategoryDetails.isVisible()) humanLabCategoryDetails.setVisible(false);
        if (!animalCommCategoryDetails.isVisible()) animalCommCategoryDetails.setVisible(false);
        if (!animalFacCategoryDetails.isVisible()) animalFacCategoryDetails.setVisible(false);
        if (!environmentalCategoryDetails.isVisible()) environmentalCategoryDetails.setVisible(false);
        if (!poeCategoryDetails.isVisible()) poeCategoryDetails.setVisible(false);
        categoryLevel.setValue(CategoryDetailsLevel.COMMUNITY);
    }

    private void showSignalDetails() {
//        signalCategory.setVisible(true);
//        categoryDetails.setVisible(true);
//        triagingDecision.setVisible(true);
//        dateOfDecision.setVisible(true);
//        referredTo.setVisible(true);
//        headingSignalInformation.setVisible(true);
////        headingTriagingDecision.setVisible(true);
//        previousOccurrence.setVisible(true);
////        healthConcern.setVisible(true);
    }

    private void showNonSignalDetails() {
//        signalCategory.setVisible(false);
//        categoryDetails.setVisible(false);
//        triagingDecision.setVisible(true);
//        headingSignalInformation.setVisible(true);
////        headingTriagingDecision.setVisible(true);
//        dateOfDecision.setVisible(true);
//        previousOccurrence.setVisible(false);
////        healthConcern.setVisible(false);
    }

    private void hideAllDetails() {
//        signalCategory.setVisible(false);
//        categoryDetails.setVisible(false);
//        triagingDecision.setVisible(false);
//        dateOfDecision.setVisible(false);
//        referredTo.setVisible(false);
////        headingTriagingDecision.setVisible(false);
//        previousOccurrence.setVisible(false);
////        healthConcern.setVisible(false);
    }


}


