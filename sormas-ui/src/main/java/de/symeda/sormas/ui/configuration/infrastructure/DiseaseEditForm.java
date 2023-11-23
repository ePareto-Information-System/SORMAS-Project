package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.*;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.infrastructure.components.SearchField;
import de.symeda.sormas.ui.utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

public class DiseaseEditForm extends AbstractEditForm<DiseaseConDto> {

    private static final String LEFT_FILTER_LAYOUT = "leftFilterLayout";
    private static final String RIGHT_FILTER_LAYOUT = "rightFilterLayout";
    private static final String FACILITIES_SELECTION_LOCATION = "searchFacilityLocation";

    private boolean create;
    private FacilityCriteria criteria;
    FacilityCriteria diseaseFacilitycriteria;
    private HorizontalLayout filterLayout;
    private ViewConfiguration viewConfiguration;
    private SearchField searchField;
    private ComboBox regionFilter;
    private ComboBox districtFilter;
    TwinColSelect selectDiseaseFacilities = new TwinColSelect<>();
    Boolean firstPageLoad = true;
    private int selectedFacilityCount = 0;

    private static final String HTML_LAYOUT = fluidRowLocs(LEFT_FILTER_LAYOUT, RIGHT_FILTER_LAYOUT) + fluidRowLocs(FACILITIES_SELECTION_LOCATION);

    public DiseaseEditForm(boolean create) {
        super(DiseaseConDto.class, DiseaseConDto.I18N_PREFIX, false);
        this.create = create;

        viewConfiguration = ViewModelProviders.of(DiseasesView.class).get(ViewConfiguration.class);
        criteria = ViewModelProviders.of(DiseasesView.class)
                .get(FacilityCriteria.class, new FacilityCriteria().country(FacadeProvider.getCountryFacade().getServerCountry()));
        if (criteria.getRelevanceStatus() == null) {
            criteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
        }
        criteria.nameCityLike(null);
        criteria.region(null);
        criteria.district(null);

        setWidth(900, Unit.PIXELS);

        if (create) {
            hideValidationUntilNextCommit();
        }
        addFields();
    }
    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {

        getContent().addComponent(createFilterBar(), LEFT_FILTER_LAYOUT);

        selectDiseaseFacilities.setLeftColumnCaption(I18nProperties.getCaption(Captions.facilitiesAvailable));
        selectDiseaseFacilities.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedFacilityCount + ")");
        selectDiseaseFacilities.setRows(20);
        selectDiseaseFacilities.setWidth("100%");


        //add chnage listener and put the selected data into DiseaseDto.facilities
        selectDiseaseFacilities.addValueChangeListener(event -> {
            //create a list of FacilityDto
            List<FacilityReferenceDto> selectedDtos = new ArrayList<>();
            for (FacilityIndexDto facilityIndexDto : (Set<FacilityIndexDto>) event.getValue()) {
                FacilityReferenceDto facilityDto = new FacilityReferenceDto();
                facilityDto.setUuid(facilityIndexDto.getUuid());
                facilityDto.setCaption(facilityIndexDto.getName());
                selectedDtos.add(facilityDto);
            }
            getValue().setFacilities(selectedDtos);
            selectDiseaseFacilities.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedDtos.size() + ")");
        });
        
        getContent().addComponent(selectDiseaseFacilities, FACILITIES_SELECTION_LOCATION);


    }

    private HorizontalLayout createFilterBar() {
        filterLayout = new HorizontalLayout();
        filterLayout.setMargin(false);
        filterLayout.setSpacing(true);
        filterLayout.setWidth(100, Unit.PERCENTAGE);

        searchField = new SearchField();
        searchField.addTextChangeListener(e -> {
            criteria.nameCityLike(e.getText());
            updateDataProvider();
        });
        filterLayout.addComponent(searchField);


        regionFilter = ComboBoxHelper.createComboBoxV7();
        regionFilter.setId(FacilityDto.REGION);
        regionFilter.setWidth(140, Unit.PIXELS);
        regionFilter.setCaption(I18nProperties.getPrefixCaption(FacilityDto.I18N_PREFIX, FacilityDto.REGION));
        regionFilter.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());
        regionFilter.addValueChangeListener(e -> {
            RegionReferenceDto region = (RegionReferenceDto) e.getProperty().getValue();
            criteria.region(region);
            FieldHelper
                    .updateItems(districtFilter, region != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(region.getUuid()) : null);
            updateDataProvider();
        });
        filterLayout.addComponent(regionFilter);

        districtFilter = ComboBoxHelper.createComboBoxV7();
        districtFilter.setId(FacilityDto.DISTRICT);
        districtFilter.setWidth(140, Unit.PIXELS);
        districtFilter.setCaption(I18nProperties.getPrefixCaption(FacilityDto.I18N_PREFIX, FacilityDto.DISTRICT));
        districtFilter.addValueChangeListener(e -> {
            DistrictReferenceDto district = (DistrictReferenceDto) e.getProperty().getValue();
            criteria.district(district);
            updateDataProvider();
        });
        filterLayout.addComponent(districtFilter);

        return filterLayout;
    }


    @Override
    public void setValue(DiseaseConDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);

        criteria.type(FacilityType.LABORATORY);

        List<Disease> diseases = new ArrayList<>();
        diseases.add(getValue().getDisease());
        diseaseFacilitycriteria = new FacilityCriteria();
        diseaseFacilitycriteria.type(FacilityType.LABORATORY);
        diseaseFacilitycriteria = ViewModelProviders.of(DiseasesView.class)
                .get(FacilityCriteria.class, new FacilityCriteria().country(FacadeProvider.getCountryFacade().getServerCountry()));
        if (diseaseFacilitycriteria.getRelevanceStatus() == null) {
            diseaseFacilitycriteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
        }

        CountryReferenceDto countryReferenceDto = FacadeProvider.getCountryFacade().getServerCountry();
        diseaseFacilitycriteria.country(countryReferenceDto);
        diseaseFacilitycriteria.diseases(diseases);

        diseaseFacilitycriteria.diseases(diseases);

        updateDataProvider();
        //set the item caption generator
        selectDiseaseFacilities.setItemCaptionGenerator(new ItemCaptionGenerator<FacilityIndexDto>() {
            @Override
            public String apply(FacilityIndexDto facilityIndexDto) {
                //create a string with district and community
                String district = facilityIndexDto.getDistrict() != null ? facilityIndexDto.getDistrict().getCaption() : "";
                String community = facilityIndexDto.getCommunity() != null ? facilityIndexDto.getCommunity().getCaption() : "";
                return facilityIndexDto.getName() + " (" + district + ", " + community + ")";
            }
        });
    }


    public void updateDataProvider() {

        Set<FacilityIndexDto> selectedFacilityIndexDtos;

        if (firstPageLoad) {
            //get disease from getValue.getDisease
            List<Disease> diseases = new ArrayList<>();
            diseases.add(getValue().getDisease());
            diseaseFacilitycriteria.diseases(diseases);
            selectedFacilityIndexDtos = FacadeProvider.getFacilityFacade().getIndexList(diseaseFacilitycriteria, null, null, null).stream().collect(Collectors.toSet());
            //clear diseases
            diseases.clear();
            firstPageLoad = false;
        } else {
            selectedFacilityIndexDtos = (Set<FacilityIndexDto>) selectDiseaseFacilities.getSelectedItems();
        }

        criteria.diseases(null);
        Set<FacilityIndexDto> allFacilities = FacadeProvider.getFacilityFacade().getIndexList(criteria, null, null, null).stream().collect(Collectors.toSet());
        //join the selectedFacilityIndexDtos and allFacilities
        allFacilities.addAll(selectedFacilityIndexDtos);

        //remove duplicates from allFacilities base on uuid
        allFacilities = allFacilities.stream().map(
                facilityIndexDto -> {
                    for (FacilityIndexDto facilityIndexDto1 : selectedFacilityIndexDtos) {
                        if (facilityIndexDto.getUuid().equals(facilityIndexDto1.getUuid())) {
                            return facilityIndexDto1;
                        }
                    }
                    return facilityIndexDto;
                }
        ).collect(Collectors.toSet());

        selectDiseaseFacilities.setItems(allFacilities);
        selectDiseaseFacilities.setValue(new HashSet<>(
                selectedFacilityIndexDtos
        ));
        selectDiseaseFacilities.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedFacilityIndexDtos.size() + ")");
    }
}
