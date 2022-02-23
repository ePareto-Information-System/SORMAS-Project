package de.symeda.sormas.ui.entitymap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.CaseMeasure;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseIndexDto;
import de.symeda.sormas.api.caze.MapCaseDto;
import de.symeda.sormas.api.contact.ContactClassification;
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.MapContactDto;
import de.symeda.sormas.api.dashboard.DashboardEventDto;
import de.symeda.sormas.api.geo.GeoLatLon;
import de.symeda.sormas.api.geo.GeoShapeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.InfrastructureHelper;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DataHelper.Pair;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.map.LeafletMap;
import de.symeda.sormas.ui.map.LeafletMap.MarkerClickEvent;
import de.symeda.sormas.ui.map.LeafletMap.MarkerClickListener;
import de.symeda.sormas.ui.map.LeafletMapUtil;
import de.symeda.sormas.ui.map.LeafletMarker;
import de.symeda.sormas.ui.map.LeafletPolygon;
import de.symeda.sormas.ui.map.MarkerIcon;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class ContactMapComponent extends VerticalLayout {

	final static Logger logger = LoggerFactory.getLogger(ContactMapComponent.class);

	private static final String CONTACTS_GROUP_ID = "contacts";
	private static final String CASES_GROUP_ID = "cases";
	private static final String EVENTS_GROUP_ID = "events";
	private static final String REGIONS_GROUP_ID = "regions";
	private static final String DISTRICTS_GROUP_ID = "districts";

	// Layouts and components
	private final DashboardDataProvider dashboardDataProvider;
	private final ContactCriteria contactCriteria;
	private ContactDto contactDto;
	private final LeafletMap map;
	private PopupButton legendDropdown;

	// Layers
	private boolean showContacts;
	private boolean showConfirmedContacts;
	private boolean showUnconfirmedContacts;
	private boolean showEvents;
	private boolean showRegions;
	private boolean hideOtherCountries;

	// Entities
	private List<CaseIndexDto> cases;
	private final HashMap<FacilityReferenceDto, MapContactDto> contactsByFacility = new HashMap<>();
	private List<MapCaseDto> mapCaseDtos = new ArrayList<>();
	private List<MapContactDto> mapAndFacilityCases = new ArrayList<>();
	private List<MapContactDto> mapContactDtos = new ArrayList<>();

	// Markers
	private final List<FacilityReferenceDto> markerCaseFacilities = new ArrayList<FacilityReferenceDto>();
	private final List<MapContactDto> markerContacts = new ArrayList<MapContactDto>();
	private final List<DashboardEventDto> markerEvents = new ArrayList<DashboardEventDto>();
	private final List<RegionReferenceDto> polygonRegions = new ArrayList<RegionReferenceDto>();
	private final List<DistrictReferenceDto> polygonDistricts = new ArrayList<DistrictReferenceDto>();

	// Others
	private CaseMeasure caseMeasure = CaseMeasure.CASE_COUNT;
	private MapCaseDisplayMode mapCaseDisplayMode = MapCaseDisplayMode.HEALTH_FACILITY_OR_CASE_ADDRESS;
	private BigDecimal districtValuesLowerQuartile;
	private BigDecimal districtValuesMedian;
	private BigDecimal districtValuesUpperQuartile;
	private Consumer<Boolean> externalExpandListener;
	private boolean emptyPopulationDistrictPresent;

	public ContactMapComponent(ContactCriteria contactCriteria) {
		this.contactCriteria = contactCriteria;
		this.dashboardDataProvider = new DashboardDataProvider();
		this.map = new LeafletMap();

		setMargin(false);
		setSpacing(false);
		setSizeFull();

		map.setWidth(100, Unit.PERCENTAGE);
		map.setHeight(580, Unit.PIXELS);
		map.addMarkerClickListener(new MarkerClickListener() {

			@Override
			public void markerClick(MarkerClickEvent event) {
				onMarkerClicked(event.getGroupId(), event.getMarkerIndex());
			}
		});
		{

			GeoShapeProvider geoShapeProvider = FacadeProvider.getGeoShapeProvider();

			final GeoLatLon mapCenter;
			if (UserProvider.getCurrent().hasAnyUserRole(UserRole.NATIONAL_USER, UserRole.NATIONAL_CLINICIAN, UserRole.NATIONAL_OBSERVER)) {
				mapCenter = geoShapeProvider.getCenterOfAllRegions();

			} else {
				UserDto user = UserProvider.getCurrent().getUser();
				if (user.getRegion() != null) {
					mapCenter = geoShapeProvider.getCenterOfRegion(user.getRegion());
				} else {
					mapCenter = geoShapeProvider.getCenterOfAllRegions();
				}
			}

			GeoLatLon center = Optional.ofNullable(mapCenter).orElseGet(FacadeProvider.getConfigFacade()::getCountryCenter);

			map.setCenter(center);
		}
		map.setZoom(FacadeProvider.getConfigFacade().getMapZoom());

		showContacts = true;
		showEvents = false;
		showConfirmedContacts = true;
		showUnconfirmedContacts = true;
		hideOtherCountries = false;

		this.setMargin(true);

		// Add components
		addComponent(map);
//		addComponent(createFooter());
		setExpandRatio(map, 1);

		refreshMap();
	}

	public ContactMapComponent(ContactDto contact, CaseDataDto caseDataDto) {
		this.contactCriteria = new ContactCriteria();
		contactDto = contact;
		this.dashboardDataProvider = new DashboardDataProvider();
		this.map = new LeafletMap();

		setMargin(false);
		setSpacing(false);
		setSizeFull();

		map.setWidth(100, Unit.PERCENTAGE);
		map.setHeight(350, Unit.PIXELS);
//		map.setZoom(18);
		map.addMarkerClickListener(new MarkerClickListener() {

			@Override
			public void markerClick(MarkerClickEvent event) {
				onMarkerClicked(event.getGroupId(), event.getMarkerIndex());
			}
		});
		{

			GeoShapeProvider geoShapeProvider = FacadeProvider.getGeoShapeProvider();

			final GeoLatLon mapCenter;
			if (UserProvider.getCurrent().hasAnyUserRole(UserRole.NATIONAL_USER, UserRole.NATIONAL_CLINICIAN, UserRole.NATIONAL_OBSERVER)) {
				mapCenter = geoShapeProvider.getCenterOfAllRegions();

			} else {
				UserDto user = UserProvider.getCurrent().getUser();
				if (user.getRegion() != null) {
					mapCenter = geoShapeProvider.getCenterOfRegion(user.getRegion());
				} else {
					mapCenter = geoShapeProvider.getCenterOfAllRegions();
				}
			}

			GeoLatLon center = Optional.ofNullable(mapCenter).orElseGet(FacadeProvider.getConfigFacade()::getCountryCenter);

			map.setCenter(center);
		}
		map.setZoom(FacadeProvider.getConfigFacade().getMapZoom());

		showContacts = true;
		showEvents = false;
		showConfirmedContacts = true;
		showUnconfirmedContacts = true;
		hideOtherCountries = false;

		this.setMargin(true);

		// Add components
		addComponent(map);
		addComponent(createFooter());
		setExpandRatio(map, 1);

		loadMap();
	}

	public void loadMap() {
		LeafletMapUtil.clearOtherCountriesOverlay(map);

		if (hideOtherCountries) {
			LeafletMapUtil.addOtherCountriesOverlay(map);
		}

		if (showContacts) {
			MapContactDto mapContact =
				new MapContactDto(contactDto.getUuid(), contactDto.getContactClassification(), contactDto.getReportLat(), contactDto.getReportLon());
			showContactMarker(mapContact);
		}

		// Re-create the map key layout to only show the keys for the selected layers
		legendDropdown.setContent(createLegend());
	}

	public void refreshMap() {
		clearRegionShapes();
		clearCaseMarkers();
		LeafletMapUtil.clearOtherCountriesOverlay(map);

		if (hideOtherCountries) {
			LeafletMapUtil.addOtherCountriesOverlay(map);
		}

		if (showRegions) {
//			showRegionsShapes(caseMeasure, fromDate, toDate, dashboardDataProvider.getDisease());
		}
		if (showContacts) {
			MapContactDto mapContact =
				new MapContactDto(contactDto.getUuid(), contactDto.getContactClassification(), contactDto.getReportLat(), contactDto.getReportLon());
			showContactMarker(mapContact);
		}

		// Re-create the map key layout to only show the keys for the selected layers
		legendDropdown.setContent(createLegend());
	}

	public void setExpandListener(Consumer<Boolean> listener) {
		externalExpandListener = listener;
	}

	private HorizontalLayout createFooter() {
		HorizontalLayout mapFooterLayout = new HorizontalLayout();
		mapFooterLayout.setWidth(100, Unit.PERCENTAGE);
		mapFooterLayout.setSpacing(true);
		CssStyles.style(mapFooterLayout, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_3);

		// Map key dropdown button
		legendDropdown = new PopupButton(I18nProperties.getCaption(Captions.dashboardMapKey));
		CssStyles.style(legendDropdown, CssStyles.BUTTON_SUBTLE);
		legendDropdown.setContent(createLegend());
		mapFooterLayout.addComponent(legendDropdown);
		mapFooterLayout.setComponentAlignment(legendDropdown, Alignment.MIDDLE_RIGHT);
		mapFooterLayout.setExpandRatio(legendDropdown, 1);

		// Layers dropdown button
		PopupButton layersDropdown = new PopupButton(I18nProperties.getCaption(Captions.dashboardMapLayers));
		{
			CssStyles.style(layersDropdown, CssStyles.BUTTON_SUBTLE);

			VerticalLayout layersLayout = new VerticalLayout();
			layersLayout.setMargin(true);
			layersLayout.setSpacing(false);
			layersLayout.setSizeUndefined();
			layersDropdown.setContent(layersLayout);

			// Add check boxes and apply button
			{
				OptionGroup mapContactDisplayModeSelect = new OptionGroup();
				mapContactDisplayModeSelect.setWidth(100, Unit.PERCENTAGE);
				mapContactDisplayModeSelect.addItems((Object[]) MapCaseDisplayMode.values());
				mapContactDisplayModeSelect.setValue(mapCaseDisplayMode);
				mapContactDisplayModeSelect.addValueChangeListener(event -> {
					mapCaseDisplayMode = (MapCaseDisplayMode) event.getProperty().getValue();
					refreshMap();
				});

				HorizontalLayout showContactsLayout = new HorizontalLayout();
				{
					showContactsLayout.setMargin(false);
					showContactsLayout.setSpacing(false);
					CheckBox showContactsCheckBox = new CheckBox();
					showContactsCheckBox.setCaption(I18nProperties.getCaption(Captions.dashboardShowUnconfirmedContacts));
					showContactsCheckBox.setValue(showContacts);
					showContactsCheckBox.addValueChangeListener(e -> {
						showContacts = (boolean) e.getProperty().getValue();
						mapContactDisplayModeSelect.setEnabled(showContacts);
						mapContactDisplayModeSelect.setValue(mapCaseDisplayMode);
						refreshMap();
					});
					showContactsLayout.addComponent(showContactsCheckBox);

					Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
					infoLabel.setDescription(I18nProperties.getString(Strings.infoCaseMap));
					CssStyles.style(infoLabel, CssStyles.LABEL_MEDIUM, CssStyles.LABEL_SECONDARY, CssStyles.HSPACE_LEFT_3);
					infoLabel.setHeightUndefined();
					showContactsLayout.addComponent(infoLabel);
					showContactsLayout.setComponentAlignment(infoLabel, Alignment.TOP_CENTER);
				}
				layersLayout.addComponent(showContactsLayout);

				layersLayout.addComponent(mapContactDisplayModeSelect);
				mapContactDisplayModeSelect.setEnabled(showContacts);

				if (UserProvider.getCurrent().hasUserRole(UserRole.NATIONAL_USER)
					|| UserProvider.getCurrent().hasUserRole(UserRole.NATIONAL_CLINICIAN)
					|| UserProvider.getCurrent().hasUserRole(UserRole.NATIONAL_OBSERVER)) {
					OptionGroup regionMapVisualizationSelect = new OptionGroup();
					regionMapVisualizationSelect.setWidth(100, Unit.PERCENTAGE);
					regionMapVisualizationSelect.addItems((Object[]) CaseMeasure.values());
					regionMapVisualizationSelect.setValue(caseMeasure);
					regionMapVisualizationSelect.addValueChangeListener(event -> {
						caseMeasure = (CaseMeasure) event.getProperty().getValue();
						refreshMap();
					});

					HorizontalLayout showRegionsLayout = new HorizontalLayout();
					{
						showRegionsLayout.setMargin(false);
						showRegionsLayout.setSpacing(false);
						CheckBox showRegionsCheckBox = new CheckBox();
						showRegionsCheckBox.setCaption(I18nProperties.getCaption(Captions.dashboardShowRegions));
						showRegionsCheckBox.setValue(showRegions);
						showRegionsCheckBox.addValueChangeListener(e -> {
							showRegions = (boolean) e.getProperty().getValue();
							regionMapVisualizationSelect.setEnabled(showRegions);
							regionMapVisualizationSelect.setValue(caseMeasure);
							refreshMap();
						});
						showRegionsLayout.addComponent(showRegionsCheckBox);

						Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
						infoLabel.setDescription(I18nProperties.getString(Strings.infoCaseIncidence));
						CssStyles.style(infoLabel, CssStyles.LABEL_MEDIUM, CssStyles.LABEL_SECONDARY, CssStyles.HSPACE_LEFT_3);
						infoLabel.setHeightUndefined();
						showRegionsLayout.addComponent(infoLabel);
						showRegionsLayout.setComponentAlignment(infoLabel, Alignment.TOP_CENTER);
					}
					layersLayout.addComponent(showRegionsLayout);
					layersLayout.addComponent(regionMapVisualizationSelect);
					regionMapVisualizationSelect.setEnabled(showRegions);
				}

				CheckBox hideOtherCountriesCheckBox = new CheckBox();
				hideOtherCountriesCheckBox.setCaption(I18nProperties.getCaption(Captions.dashboardHideOtherCountries));
				hideOtherCountriesCheckBox.setValue(hideOtherCountries);
				hideOtherCountriesCheckBox.addValueChangeListener(e -> {
					hideOtherCountries = (boolean) e.getProperty().getValue();
					refreshMap();
				});
				layersLayout.addComponent(hideOtherCountriesCheckBox);
			}
		}
		mapFooterLayout.addComponent(layersDropdown);
		mapFooterLayout.setComponentAlignment(layersDropdown, Alignment.MIDDLE_RIGHT);

		return mapFooterLayout;
	}

	private VerticalLayout createLegend() {
		VerticalLayout legendLayout = new VerticalLayout();
		legendLayout.setSpacing(false);
		legendLayout.setMargin(true);
		legendLayout.setSizeUndefined();

		// Disable map key dropdown if no layers have been selected
		if (showContacts || showContacts || showEvents || showRegions) {
			legendDropdown.setEnabled(true);
		} else {
			legendDropdown.setEnabled(false);
			return legendLayout;
		}

		// Health facilities

		// Cases
		if (showContacts) {
			if (mapCaseDisplayMode == MapCaseDisplayMode.HEALTH_FACILITY
				|| mapCaseDisplayMode == MapCaseDisplayMode.HEALTH_FACILITY_OR_CASE_ADDRESS) {
				Label facilitiesKeyLabel = new Label(I18nProperties.getCaption(Captions.dashboardHealthFacilities));
				CssStyles.style(facilitiesKeyLabel, CssStyles.H4, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);
				legendLayout.addComponent(facilitiesKeyLabel);

				HorizontalLayout facilitiesKeyLayout = new HorizontalLayout();
				{
					facilitiesKeyLayout.setSpacing(false);
					facilitiesKeyLayout.setMargin(false);
					HorizontalLayout legendEntry =
						buildMarkerLegendEntry(MarkerIcon.FACILITY_UNCLASSIFIED, I18nProperties.getCaption(Captions.dashboardNotYetClassifiedOnly));
					CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
					facilitiesKeyLayout.addComponent(legendEntry);
					legendEntry = buildMarkerLegendEntry(MarkerIcon.FACILITY_SUSPECT, I18nProperties.getCaption(Captions.dashboardGt1SuspectCases));
					CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
					facilitiesKeyLayout.addComponent(legendEntry);
					legendEntry = buildMarkerLegendEntry(MarkerIcon.FACILITY_PROBABLE, I18nProperties.getCaption(Captions.dashboardGt1ProbableCases));
					CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
					facilitiesKeyLayout.addComponent(legendEntry);
					legendEntry =
						buildMarkerLegendEntry(MarkerIcon.FACILITY_CONFIRMED, I18nProperties.getCaption(Captions.dashboardGt1ConfirmedCases));
					facilitiesKeyLayout.addComponent(legendEntry);
				}
				legendLayout.addComponent(facilitiesKeyLayout);
			}

			Label casesKeyLabel = new Label(I18nProperties.getString(Strings.entityCases));
			if (mapCaseDisplayMode == MapCaseDisplayMode.HEALTH_FACILITY
				|| mapCaseDisplayMode == MapCaseDisplayMode.HEALTH_FACILITY_OR_CASE_ADDRESS) {
				CssStyles.style(casesKeyLabel, CssStyles.H4, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_3);
			} else {
				CssStyles.style(casesKeyLabel, CssStyles.H4, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);
			}
			legendLayout.addComponent(casesKeyLabel);

			HorizontalLayout casesKeyLayout = new HorizontalLayout();
			{
				casesKeyLayout.setSpacing(false);
				casesKeyLayout.setMargin(false);
				HorizontalLayout legendEntry =
					buildMarkerLegendEntry(MarkerIcon.CASE_UNCLASSIFIED, I18nProperties.getCaption(Captions.dashboardNotYetClassified));
				CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
				casesKeyLayout.addComponent(legendEntry);
				legendEntry = buildMarkerLegendEntry(MarkerIcon.CASE_SUSPECT, I18nProperties.getCaption(Captions.dashboardSuspect));
				CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
				casesKeyLayout.addComponent(legendEntry);
				legendEntry = buildMarkerLegendEntry(MarkerIcon.CASE_PROBABLE, I18nProperties.getCaption(Captions.dashboardProbable));
				CssStyles.style(legendEntry, CssStyles.HSPACE_RIGHT_3);
				casesKeyLayout.addComponent(legendEntry);
				legendEntry = buildMarkerLegendEntry(MarkerIcon.CASE_CONFIRMED, I18nProperties.getCaption(Captions.dashboardConfirmed));
				casesKeyLayout.addComponent(legendEntry);
			}
			legendLayout.addComponent(casesKeyLayout);
		}

		// Districts
		if (showRegions && districtValuesLowerQuartile != null && districtValuesMedian != null && districtValuesUpperQuartile != null) {
			Label districtsKeyLabel = new Label(I18nProperties.getString(Strings.entityDistricts));
			if (showContacts || showContacts || showEvents) {
				CssStyles.style(districtsKeyLabel, CssStyles.H4, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_3);
			} else {
				CssStyles.style(districtsKeyLabel, CssStyles.H4, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);
			}
			legendLayout.addComponent(districtsKeyLabel);
			legendLayout.addComponent(
				buildRegionLegend(
					false,
					caseMeasure,
					emptyPopulationDistrictPresent,
					districtValuesLowerQuartile,
					districtValuesMedian,
					districtValuesUpperQuartile,
					InfrastructureHelper.CASE_INCIDENCE_DIVISOR));

			Label descLabel = new Label(I18nProperties.getString(Strings.infoDashboardIncidence));
			CssStyles.style(descLabel, CssStyles.LABEL_SMALL);
			legendLayout.addComponent(descLabel);
		}

		return legendLayout;
	}

	public static HorizontalLayout buildMarkerLegendEntry(MarkerIcon icon, String labelCaption) {
		return buildLegendEntry(new Label(icon.getHtmlElement("16px"), ContentMode.HTML), labelCaption);
	}

	public static HorizontalLayout buildMapIconLegendEntry(String iconName, String labelCaption) {
		Image icon = new Image(null, new ExternalResource("VAADIN/map/marker/" + iconName + ".png"));
		icon.setWidth(12.375f, Unit.PIXELS);
		icon.setHeight(16.875f, Unit.PIXELS);
		return buildLegendEntry(icon, labelCaption);
	}

	private static HorizontalLayout buildLegendEntry(AbstractComponent icon, String labelCaption) {
		HorizontalLayout entry = new HorizontalLayout();
		entry.setSpacing(false);
		entry.setSizeUndefined();
		CssStyles.style(icon, CssStyles.HSPACE_RIGHT_4);
		entry.addComponent(icon);
		Label label = new Label(labelCaption);
		label.setSizeUndefined();
		label.addStyleName(ValoTheme.LABEL_SMALL);
		entry.addComponent(label);
		return entry;
	}

	public static AbstractOrderedLayout buildRegionLegend(
		boolean vertical,
		CaseMeasure caseMeasure,
		boolean emptyPopulationDistrictPresent,
		BigDecimal districtShapesLowerQuartile,
		BigDecimal districtShapesMedian,
		BigDecimal districtShapesUpperQuartile,
		int caseIncidenceDivisor) {
		AbstractOrderedLayout regionLegendLayout = vertical ? new VerticalLayout() : new HorizontalLayout();
		regionLegendLayout.setSpacing(true);
		CssStyles.style(regionLegendLayout, CssStyles.LAYOUT_MINIMAL);
		regionLegendLayout.setSizeUndefined();

		HorizontalLayout legendEntry;
		switch (caseMeasure) {
		case CASE_COUNT:
			legendEntry = buildMapIconLegendEntry(
				"lowest-region-small",
				districtShapesLowerQuartile.compareTo(BigDecimal.ONE) > 0
					? "1 - " + districtShapesLowerQuartile + " " + I18nProperties.getString(Strings.entityCases)
					: "1 " + I18nProperties.getString(Strings.entityCase));
			break;
		case CASE_INCIDENCE:
			legendEntry = buildMapIconLegendEntry(
				"lowest-region-small",
				"<= " + DataHelper.getTruncatedBigDecimal(districtShapesLowerQuartile) + " " + I18nProperties.getString(Strings.entityCases) + " / "
					+ caseIncidenceDivisor);
			break;
		default:
			throw new IllegalArgumentException(caseMeasure.toString());
		}
		regionLegendLayout.addComponent(legendEntry);

		if (districtShapesLowerQuartile.compareTo(districtShapesMedian) < 0) {
			switch (caseMeasure) {
			case CASE_COUNT:
				legendEntry = buildMapIconLegendEntry(
					"low-region-small",
					districtShapesMedian.compareTo(districtShapesLowerQuartile.add(BigDecimal.ONE)) > 0
						? districtShapesLowerQuartile.add(BigDecimal.ONE) + " - " + districtShapesMedian + " "
							+ I18nProperties.getString(Strings.entityCases)
						: districtShapesMedian + " " + I18nProperties.getString(Strings.entityCases));
				break;
			case CASE_INCIDENCE:
				legendEntry = buildMapIconLegendEntry(
					"low-region-small",
					DataHelper.getTruncatedBigDecimal(districtShapesLowerQuartile.add(new BigDecimal(0.1)).setScale(1, RoundingMode.HALF_UP)) + " - "
						+ DataHelper.getTruncatedBigDecimal(districtShapesMedian) + " " + I18nProperties.getString(Strings.entityCases) + " / "
						+ caseIncidenceDivisor);
				break;
			default:
				throw new IllegalArgumentException(caseMeasure.toString());
			}

			regionLegendLayout.addComponent(legendEntry);
		}

		if (districtShapesMedian.compareTo(districtShapesUpperQuartile) < 0) {
			switch (caseMeasure) {
			case CASE_COUNT:
				legendEntry = buildMapIconLegendEntry(
					"high-region-small",
					districtShapesUpperQuartile.compareTo(districtShapesMedian.add(BigDecimal.ONE)) > 0
						? districtShapesMedian.add(BigDecimal.ONE) + " - " + districtShapesUpperQuartile + " "
							+ I18nProperties.getString(Strings.entityCases)
						: districtShapesUpperQuartile + " " + I18nProperties.getString(Strings.entityCases));
				break;
			case CASE_INCIDENCE:
				legendEntry = buildMapIconLegendEntry(
					"high-region-small",
					DataHelper.getTruncatedBigDecimal(districtShapesMedian.add(new BigDecimal(0.1)).setScale(1, RoundingMode.HALF_UP)) + " - "
						+ DataHelper.getTruncatedBigDecimal(districtShapesUpperQuartile) + " " + I18nProperties.getString(Strings.entityCases) + " / "
						+ caseIncidenceDivisor);
				break;
			default:
				throw new IllegalArgumentException(caseMeasure.toString());
			}

			regionLegendLayout.addComponent(legendEntry);
		}

		switch (caseMeasure) {
		case CASE_COUNT:
			legendEntry = buildMapIconLegendEntry(
				"highest-region-small",
				"> " + districtShapesUpperQuartile + " " + I18nProperties.getString(Strings.entityCases));
			break;
		case CASE_INCIDENCE:
			legendEntry = buildMapIconLegendEntry(
				"highest-region-small",
				"> " + DataHelper.getTruncatedBigDecimal(districtShapesUpperQuartile) + " " + I18nProperties.getString(Strings.entityCases) + " / "
					+ caseIncidenceDivisor);
			break;
		default:
			throw new IllegalArgumentException(caseMeasure.toString());
		}
		regionLegendLayout.addComponent(legendEntry);

		if (caseMeasure == CaseMeasure.CASE_INCIDENCE && emptyPopulationDistrictPresent) {
			legendEntry = buildMapIconLegendEntry("no-population-region-small", I18nProperties.getCaption(Captions.dashboardNoPopulationData));
			regionLegendLayout.addComponent(legendEntry);
		}

		return regionLegendLayout;
	}

	private void clearRegionShapes() {
		map.removeGroup(REGIONS_GROUP_ID);
		map.removeGroup(DISTRICTS_GROUP_ID);
		polygonRegions.clear();
		polygonDistricts.clear();

		emptyPopulationDistrictPresent = false;
		map.setTileLayerOpacity(1);
	}

	private void showRegionsShapes(CaseMeasure caseMeasure, Date fromDate, Date toDate, Disease disease) {
		clearRegionShapes();
		map.setTileLayerOpacity(0.5f);

		List<RegionReferenceDto> regions = FacadeProvider.getRegionFacade().getAllActiveAsReference();
		List<LeafletPolygon> regionPolygons = new ArrayList<LeafletPolygon>();

		// draw outlines of all regions
		for (RegionReferenceDto region : regions) {

			GeoLatLon[][] regionShape = FacadeProvider.getGeoShapeProvider().getRegionShape(region);
			if (regionShape == null) {
				continue;
			}

			for (GeoLatLon[] regionShapePart : regionShape) {
				LeafletPolygon polygon = new LeafletPolygon();
				polygon.setCaption(region.getCaption());
				// fillOpacity is used, so we can still hover the region
				polygon.setOptions("{\"weight\": 1, \"color\": '#444', \"fillOpacity\": 0.02}");
				polygon.setLatLons(regionShapePart);
				regionPolygons.add(polygon);
				polygonRegions.add(region);
			}
		}

		map.addPolygonGroup(REGIONS_GROUP_ID, regionPolygons);

		List<Pair<DistrictDto, BigDecimal>> measurePerDistrict =
			FacadeProvider.getCaseFacade().getCaseMeasurePerDistrict(fromDate, toDate, disease, caseMeasure);
		if (caseMeasure == CaseMeasure.CASE_COUNT) {
			districtValuesLowerQuartile =
				measurePerDistrict.size() > 0 ? measurePerDistrict.get((int) (measurePerDistrict.size() * 0.25)).getElement1() : null;
			districtValuesMedian =
				measurePerDistrict.size() > 0 ? measurePerDistrict.get((int) (measurePerDistrict.size() * 0.5)).getElement1() : null;
			districtValuesUpperQuartile =
				measurePerDistrict.size() > 0 ? measurePerDistrict.get((int) (measurePerDistrict.size() * 0.75)).getElement1() : null;
		} else {
			// For case incidence, districts without or with a population <= 0 should not be
			// used for the calculation of the quartiles because they will falsify the
			// result
			List<Pair<DistrictDto, BigDecimal>> measurePerDistrictWithoutMissingPopulations = new ArrayList<>();
			measurePerDistrictWithoutMissingPopulations.addAll(measurePerDistrict);
			measurePerDistrictWithoutMissingPopulations.removeIf(d -> d.getElement1() == null || d.getElement1().intValue() <= 0);
			districtValuesLowerQuartile = measurePerDistrictWithoutMissingPopulations.size() > 0
				? measurePerDistrictWithoutMissingPopulations.get((int) (measurePerDistrictWithoutMissingPopulations.size() * 0.25)).getElement1()
				: null;
			districtValuesMedian = measurePerDistrictWithoutMissingPopulations.size() > 0
				? measurePerDistrictWithoutMissingPopulations.get((int) (measurePerDistrictWithoutMissingPopulations.size() * 0.5)).getElement1()
				: null;
			districtValuesUpperQuartile = measurePerDistrictWithoutMissingPopulations.size() > 0
				? measurePerDistrictWithoutMissingPopulations.get((int) (measurePerDistrictWithoutMissingPopulations.size() * 0.75)).getElement1()
				: null;
		}

		List<LeafletPolygon> districtPolygons = new ArrayList<LeafletPolygon>();

		// Draw relevant district fills
		for (Pair<DistrictDto, BigDecimal> districtMeasure : measurePerDistrict) {

			DistrictDto district = districtMeasure.getElement0();
			DistrictReferenceDto districtRef = district.toReference();
			BigDecimal districtValue = districtMeasure.getElement1();
			GeoLatLon[][] districtShape = FacadeProvider.getGeoShapeProvider().getDistrictShape(districtRef);
			if (districtShape == null) {
				continue;
			}

			String fillColor;
			if (districtValue.compareTo(BigDecimal.ZERO) == 0) {
				fillColor = "#000";
			} else if (districtValue.compareTo(districtValuesLowerQuartile) < 0) {
				fillColor = "#FEDD6C";
			} else if (districtValue.compareTo(districtValuesMedian) < 0) {
				fillColor = "#FDBF44";
			} else if (districtValue.compareTo(districtValuesUpperQuartile) < 0) {
				fillColor = "#F47B20";
			} else {
				fillColor = "#ED1B24";
			}

			if (caseMeasure == CaseMeasure.CASE_INCIDENCE) {
				if (districtValue == null || districtValue.intValue() <= 0) {
					// grey when region has no population data
					emptyPopulationDistrictPresent = true;
					fillColor = "#999";
				}
			}

			for (GeoLatLon[] districtShapePart : districtShape) {
				LeafletPolygon polygon = new LeafletPolygon();
				polygon.setCaption(district.getName() + "<br>" + districtValue);
				polygon.setOptions("{\"stroke\": false, \"color\": '" + fillColor + "', \"fillOpacity\": 0.8}");
				polygon.setLatLons(districtShapePart);
				districtPolygons.add(polygon);
				polygonDistricts.add(districtRef);
			}
		}

		map.addPolygonGroup(DISTRICTS_GROUP_ID, districtPolygons);
	}

	private void clearCaseMarkers() {

		map.removeGroup(CONTACTS_GROUP_ID);
		markerCaseFacilities.clear();
		contactsByFacility.clear();
		mapCaseDtos.clear();
		mapAndFacilityCases.clear();
	}

	private void showContactMarker(MapContactDto contacts) {

		clearCaseMarkers();

		fillContactList(contacts);

		List<LeafletMarker> contactMarkers = new ArrayList<LeafletMarker>();

//		for (FacilityReferenceDto facilityReference : contactsByFacility.keySet()) {

//			MapContactDto contactMapDto = contactsByFacility.get(facilityReference);
		// colorize the icon by the "strongest" classification type (order as in enum)
		// and set its size depending
		// on the number of cases
		Set<ContactClassification> classificationSet = new HashSet<>();
		classificationSet.add(contacts.getContactClassification());

		MarkerIcon icon;
		if (classificationSet.contains(CaseClassification.CONFIRMED)) {
			icon = MarkerIcon.FACILITY_CONFIRMED;
		} else if (classificationSet.contains(CaseClassification.PROBABLE)) {
			icon = MarkerIcon.FACILITY_PROBABLE;
		} else if (classificationSet.contains(CaseClassification.SUSPECT)) {
			icon = MarkerIcon.FACILITY_SUSPECT;
		} else {
			icon = MarkerIcon.FACILITY_UNCLASSIFIED;
		}

		// create and place the marker
//			markerCaseFacilities.add(facilityReference);

		LeafletMarker leafletMarker = new LeafletMarker();
		leafletMarker.setLatLon(contacts.getReportLat(), contacts.getReportLon());
		leafletMarker.setIcon(icon);
		leafletMarker.setMarkerCount(1);
//		}

		for (MapContactDto contact : mapContactDtos) {
			LeafletMarker marker = new LeafletMarker();
			if (contact.getContactClassification() == ContactClassification.CONFIRMED) {
				marker.setIcon(MarkerIcon.CASE_CONFIRMED);
			} else if (contact.getContactClassification() == ContactClassification.NO_CONTACT) {
				marker.setIcon(MarkerIcon.CONTACT_OK);
			} else {
				marker.setIcon(MarkerIcon.CASE_UNCLASSIFIED);
			}

			if (contact.getAddressLat() != null && contact.getAddressLon() != null) {
				marker.setLatLon(contact.getAddressLat(), contact.getAddressLon());
			} else {
				marker.setLatLon(contact.getReportLat(), contact.getReportLon());
			}

			contactMarkers.add(marker);
		}

		map.addMarkerGroup("contacts", contactMarkers);
	}

	private void fillContactList(MapContactDto contactsMap) {
		ContactClassification classification = contactsMap.getContactClassification();
		if (classification == null || classification == ContactClassification.NO_CONTACT)
			return;
		boolean hasContactGps = (contactsMap.getAddressLat() != null && contactsMap.getAddressLon() != null)
			|| (contactsMap.getReportLat() != null || contactsMap.getReportLon() != null);
		boolean hasFacilityGps = contactsMap.getReportLat() != null && contactsMap.getReportLon() != null;
		if (!hasContactGps && !hasFacilityGps) {
			return; // no gps at all
		}

		if (mapCaseDisplayMode == MapCaseDisplayMode.CASE_ADDRESS) {
			if (!hasContactGps) {
				return;
			}
			mapContactDtos.add(contactsMap);
		} else {
			if (FacilityDto.NONE_FACILITY_UUID.equals(contactsMap.getUuid())
				|| FacilityDto.OTHER_FACILITY_UUID.equals(contactsMap.getUuid())
				|| !hasFacilityGps) {
				if (mapCaseDisplayMode == MapCaseDisplayMode.HEALTH_FACILITY_OR_CASE_ADDRESS) {
					if (!hasContactGps) {
						return;
					}
					mapContactDtos.add(contactsMap);
				} else {
					return;
				}
			} else {
				if (!hasFacilityGps) {
					return;
				}
				FacilityReferenceDto facility = new FacilityReferenceDto();
				facility.setUuid(contactsMap.getUuid());
				if (contactsByFacility.get(facility) == null) {
					contactsByFacility.put(facility, contactsMap);
				}
			}
		}

		mapAndFacilityCases.add(contactsMap);
	}

	private void onMarkerClicked(String groupId, int markerIndex) {

		switch (groupId) {
//		case CASES_GROUP_ID:
		case CONTACTS_GROUP_ID:

			if (markerIndex < markerCaseFacilities.size()) {
//				FacilityReferenceDto facility = markerCaseFacilities.get(markerIndex);
//				VerticalLayout layout = new VerticalLayout();
//				Window window = VaadinUiUtil.showPopupWindow(layout);
//				ContactPopupGridOnMap caseGrid = new ContactPopupGridOnMap(window, facility, ContactMapComponent.this);
//				caseGrid.setHeightMode(HeightMode.ROW);
//				layout.addComponent(caseGrid);
//				layout.setMargin(true);
//				FacilityDto facilityDto = FacadeProvider.getFacilityFacade().getByUuid(facility.getUuid());
//				window.setCaption(I18nProperties.getCaption(Captions.dashboardCasesIn) + " " + facilityDto.toString());
			} else {
//				markerIndex -= markerCaseFacilities.size();
//				MapContactDto contact = markerContacts.get(markerIndex);
//				ControllerProvider.getContactController().navigateToData(contact.getUuid(), false);

				markerIndex -= markerCaseFacilities.size();
				MapCaseDto caze = mapCaseDtos.get(markerIndex);
				ControllerProvider.getCaseController().navigateToCase(caze.getUuid(), true);
			}
			break;
//		case CONTACTS_GROUP_ID: {
		case CASES_GROUP_ID: {
//			MapContactDto contact = markerContacts.get(markerIndex);
//			ControllerProvider.getContactController().navigateToData(contact.getUuid(), false);
			MapCaseDto caze = mapCaseDtos.get(markerIndex);
			ControllerProvider.getCaseController().navigateToCase(caze.getUuid(), true);
		}
			break;
		case EVENTS_GROUP_ID: {
			DashboardEventDto event = markerEvents.get(markerIndex);
			ControllerProvider.getEventController().navigateToData(event.getUuid(), true);
		}
			break;
		}
	}

}
