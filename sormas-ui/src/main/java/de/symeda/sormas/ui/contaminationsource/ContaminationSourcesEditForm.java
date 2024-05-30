package de.symeda.sormas.ui.contaminationsource;

import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.geo.GeoLatLon;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.map.LeafletMap;
import de.symeda.sormas.ui.map.LeafletMarker;
import de.symeda.sormas.ui.map.MarkerIcon;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import java.util.Collections;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class ContaminationSourcesEditForm extends AbstractEditForm<ContaminationSourceDto>  {

    public static final String CONTAMINATION_SOURCES_HEADING = "contaminationSourcesHeadingLoc";
    public static final String MAP_LOCATION = "leaftMapLoc" ;
    private GeoLatLon coordinates = null;
    private static final long serialVersionUID = 1L;
    private TextField logitudeField;
    private TextField latitudeField;

    public static String HTML_LAYOUT = divs(
            loc(CONTAMINATION_SOURCES_HEADING),
            fluidRowLocs(ContaminationSourceDto.NAME),
            fluidRowLocs(MAP_LOCATION),
            fluidRowLocs(ContaminationSourceDto.LONGITUDE, ContaminationSourceDto.LATITUDE),
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

        getContent().addComponent(createLeafletMap(), MAP_LOCATION);
    }

    private LeafletMap createLeafletMap() {

        LeafletMap map = new LeafletMap();
        map.setWidth(100, Unit.PERCENTAGE);
        map.setHeight(420, Unit.PIXELS);
        map.setZoom(12);

        coordinates = new GeoLatLon(5.6380212, -0.1462689);
        map.setCenter(coordinates);

        LeafletMarker marker = new LeafletMarker();
        marker.setLatLon(coordinates);
        marker.setIcon(MarkerIcon.CASE_UNCLASSIFIED);
        marker.setMarkerCount(1);

        map.addMarkerGroup("cases", Collections.singletonList(marker));

        map.addMapClickListener(this::mapClick);

        return map;
    }

    public void setCoordinates(GeoLatLon coordinates) {
        this.coordinates = coordinates;
    }

    private void mapClick(LeafletMap.MapClickEvent event) {
        GeoLatLon clickCoordinates = new GeoLatLon(event.getLatitude(), event.getLongitude());
        logitudeField.setValue(String.valueOf(clickCoordinates.getLon()));
        latitudeField.setValue(String.valueOf(clickCoordinates.getLat()));
        setCoordinates(clickCoordinates);
    }
}
