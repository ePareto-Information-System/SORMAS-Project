package de.symeda.sormas.ui.contaminationsource;

import com.vaadin.ui.Button;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.geo.GeoLatLon;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.map.LeafletMap;
import de.symeda.sormas.ui.map.LeafletMarker;
import de.symeda.sormas.ui.map.MarkerIcon;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.util.Collections;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class ContaminationSourcesEditForm extends AbstractEditForm<ContaminationSourceDto>  {

    public static final String CONTAMINATION_SOURCES_HEADING = "contaminationSourcesHeadingLoc";
    public static final String MAP_LOCATION = "leaftMapLoc" ;
    public static final String SHOW_MAP_BUTTON = "showMapButtonLoc";
    private GeoLatLon coordinates = null;
    private static final long serialVersionUID = 1L;
    private TextField logitudeField;
    private TextField latitudeField;

//    hide and show map button
    private Button showMapButton;
    private boolean showMap = false;

    public static String HTML_LAYOUT = divs(
            loc(CONTAMINATION_SOURCES_HEADING),
            fluidRowLocs(ContaminationSourceDto.NAME),
            fluidRowLocs(5, ContaminationSourceDto.LONGITUDE, 5, ContaminationSourceDto.LATITUDE, 2, SHOW_MAP_BUTTON),
            fluidRowLocs(MAP_LOCATION),
            fluidRowLocs(ContaminationSourceDto.TYPE, ContaminationSourceDto.SOURCE),
            fluidRowLocs(ContaminationSourceDto.TREATED_WITH_ABATE, ContaminationSourceDto.ABATE_TREATMENT_DATE)
    );


    public ContaminationSourcesEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(ContaminationSourceDto.class, ContaminationSourceDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

        setWidth(960, Unit.PIXELS);
        if (create) {
            hideValidationUntilNextCommit();
        } else {
            addFields();
        }


    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {
        addFields(
                ContaminationSourceDto.NAME,
                ContaminationSourceDto.TYPE,
                ContaminationSourceDto.SOURCE,
                ContaminationSourceDto.TREATED_WITH_ABATE,
                ContaminationSourceDto.ABATE_TREATMENT_DATE);

        logitudeField = addField(ContaminationSourceDto.LONGITUDE);
        latitudeField = addField(ContaminationSourceDto.LATITUDE);

        logitudeField.addDetachListener(event -> {
            if (showMap) {
                if (logitudeField.getValue() != null && latitudeField.getValue() != null) {
                    coordinates = new GeoLatLon(Double.parseDouble(latitudeField.getValue()), Double.parseDouble(logitudeField.getValue()));
                    getContent().removeComponent(MAP_LOCATION);
                    getContent().addComponent(createLeafletMap(), MAP_LOCATION);

                }
            }
        });

        latitudeField.addDetachListener(event -> {
            if (showMap) {
                if (logitudeField.getValue() != null && latitudeField.getValue() != null) {
                    coordinates = new GeoLatLon(Double.parseDouble(latitudeField.getValue()), Double.parseDouble(logitudeField.getValue()));
                    getContent().removeComponent(MAP_LOCATION);
                    getContent().addComponent(createLeafletMap(), MAP_LOCATION);
                }
            }
        });


        showMapButton = new Button(I18nProperties.getCaption(Captions.EpiDataShowMapButton));
        showMapButton.addClickListener(event -> {
            showMap = !showMap;
            if (showMap) {
                getContent().addComponent(createLeafletMap(), MAP_LOCATION);
                showMapButton.setCaption(I18nProperties.getCaption(Captions.EpiDataHideMapButton));
            } else {
                showMapButton.setCaption(I18nProperties.getCaption(Captions.EpiDataShowMapButton));
                getContent().removeComponent(MAP_LOCATION);
            }
        });
        getContent().addComponent(showMapButton, SHOW_MAP_BUTTON);
    }

    private LeafletMap createLeafletMap() {

        LeafletMap map = new LeafletMap();
        map.setWidth(100, Unit.PERCENTAGE);
        map.setHeight(420, Unit.PIXELS);
        map.setZoom(12);

        if (logitudeField.getValue() != null && latitudeField.getValue() != null) {
            coordinates = new GeoLatLon(Double.parseDouble(latitudeField.getValue()), Double.parseDouble(logitudeField.getValue()));
        } else {
            coordinates = new GeoLatLon(5.559, -0.202);
        }
        map.setCenter(coordinates);

        LeafletMarker marker = new LeafletMarker();
        marker.setLatLon(coordinates);
        marker.setIcon(MarkerIcon.MARKER);
        marker.setMarkerCount(1);

        map.addMarkerGroupLocation("location", Collections.singletonList(marker), 40);
        map.addMarkerDragListener(this::markerDrag);

        return map;
    }

    private void markerDrag(LeafletMap.MarkerDragEvent event) {
        GeoLatLon draggedCoordinates = new GeoLatLon(event.getLatitude(), event.getLongitude());
        logitudeField.setValue(String.valueOf(draggedCoordinates.getLon()));
        latitudeField.setValue(String.valueOf(draggedCoordinates.getLat()));
    }

}
