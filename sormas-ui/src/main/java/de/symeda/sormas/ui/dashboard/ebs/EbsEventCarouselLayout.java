/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.dashboard.ebs;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.CheckBox;
import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.SubMenu;
import de.symeda.sormas.ui.dashboard.DashboardCssStyles;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.map.DashboardMapComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.epicurve.EbsEpiCurveComponent;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class EbsEventCarouselLayout extends VerticalLayout {

	private DashboardDataProvider dashboardDataProvider;

	private EbsEventStatisticsComponent statisticsComponent;
	//private EbsEpiCurveComponent epiCurveComponent;
	private DashboardMapComponent mapComponent;
	private Consumer<Boolean> externalExpandListener;
	private SubMenu carouselMenu;
	private List<EbsEvent> ebsEvents;
	private Registration pollRegistration;
	

	public EbsEventCarouselLayout(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;

		statisticsComponent = new EbsEventStatisticsComponent(dashboardDataProvider);

		//epiCurveComponent = new EbsEpiCurveComponent(dashboardDataProvider);
		mapComponent = new DashboardMapComponent(dashboardDataProvider);

		Set<EbsEvent> excludedEvents = EnumSet.of(EbsEvent.EVENT);

		ebsEvents = FacadeProvider.getEbsEventFacade().getAllEbsEvents()
				.stream()
				.filter(event -> !excludedEvents.contains(event))
				.collect(Collectors.toList());

		this.initLayout();
	}

	public void setExpandListener(Consumer<Boolean> listener) {
		externalExpandListener = listener;
	}

	private void initLayout() {
		addStyleName(DashboardCssStyles.CURVE_AND_MAP_LAYOUT);
		setWidth(100, Unit.PERCENTAGE);
		setHeightUndefined();
		setMargin(false);
		setSpacing(false);

		HorizontalLayout carouselMenuLayout = createCarouselMenuLayout();
		addComponent(carouselMenuLayout);
		setExpandRatio(carouselMenuLayout, 0);

		addComponent(statisticsComponent);
		statisticsComponent.addStyleName(DashboardCssStyles.HIGHLIGHTED_STATISTICS_COMPONENT);
		setExpandRatio(statisticsComponent, 0);

		HorizontalLayout epiCurveAndMapLayout = createEpiCurveAndMapLayout();
		addComponent(epiCurveAndMapLayout);
		setExpandRatio(epiCurveAndMapLayout, 1);
	}

	private HorizontalLayout createCarouselMenuLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		CssStyles.style(layout, CssStyles.HSPACE_LEFT_2);

		CheckBox autoSlide = this.setupSlideShow();
		layout.addComponent(autoSlide);
		layout.setComponentAlignment(autoSlide, Alignment.MIDDLE_LEFT);

		carouselMenu = new SubMenu();

		for (EbsEvent ebsEvent : ebsEvents) {
			carouselMenu.addView(I18nProperties.getEnumCaption(ebsEvent), I18nProperties.getEnumCaption(ebsEvent), () -> {
				this.changeSelectedEbsEvent(ebsEvent, true);

				return true;
			});
		}

		if (ebsEvents.size() > 0) {
			this.setActiveEbsEvent(ebsEvents.get(0), false);
		}

		layout.addComponent(carouselMenu);

		return layout;
	}

	private HorizontalLayout createEpiCurveAndMapLayout() {
		HorizontalLayout epiCurveAndMapLayout = new HorizontalLayout();
		epiCurveAndMapLayout.setWidth(100, Unit.PERCENTAGE);
		final int BASE_HEIGHT = 480;
		epiCurveAndMapLayout.setHeight(BASE_HEIGHT, Unit.PIXELS);

		//epiCurveAndMapLayout.addComponent(epiCurveComponent);
		epiCurveAndMapLayout.addComponent(mapComponent);

//		epiCurveComponent.setExpandListener(expanded -> {
//			if (expanded) {
//				removeComponent(statisticsComponent);
//				epiCurveAndMapLayout.removeComponent(mapComponent);
//				epiCurveAndMapLayout.setHeight(100, Unit.PERCENTAGE);
//				setHeight(100, Unit.PERCENTAGE);
//			} else {
//				addComponent(statisticsComponent, 1);
//				epiCurveAndMapLayout.addComponent(mapComponent, 1);
//				mapComponent.refreshMap();
//				epiCurveAndMapLayout.setHeight(BASE_HEIGHT, Unit.PIXELS);
//				setHeightUndefined();
//			}
//			if (externalExpandListener != null) {
//				externalExpandListener.accept(expanded);
//			}
//		});

		mapComponent.setExpandListener(expanded -> {
			if (expanded) {
				removeComponent(statisticsComponent);
				//epiCurveAndMapLayout.removeComponent(epiCurveComponent);
				epiCurveAndMapLayout.setHeight(100, Unit.PERCENTAGE);
				setHeight(100, Unit.PERCENTAGE);
			} else {
				addComponent(statisticsComponent, 1);
				//epiCurveAndMapLayout.addComponent(epiCurveComponent, 0);
				epiCurveAndMapLayout.setHeight(BASE_HEIGHT, Unit.PIXELS);
				setHeightUndefined();
			}
			if (externalExpandListener != null) {
				externalExpandListener.accept(expanded);
			}
		});

		return epiCurveAndMapLayout;
	}

	private CheckBox setupSlideShow() {
		// slideshow option
		CheckBox autoSlide = new CheckBox(I18nProperties.getCaption(Captions.dashboardEbsEventCarouselSlideShow));
		autoSlide.addValueChangeListener(e -> {
			this.changeAutoSlideOption(autoSlide.getValue());
		});

		// enabled by default
		autoSlide.setValue(false);

		return autoSlide;
	}

	private void setActiveEbsEvent(EbsEvent selectedEbsEvent, boolean doRefresh) {
		carouselMenu.setActiveView(I18nProperties.getEnumCaption(selectedEbsEvent));
		this.changeSelectedEbsEvent(selectedEbsEvent, doRefresh);
	}

	private void changeSelectedEbsEvent(EbsEvent ebsEvent, boolean doRefresh) {
		this.dashboardDataProvider.setEbsEvent(ebsEvent);
		if (doRefresh) {
			this.dashboardDataProvider.refreshDataForSelectedEbsEvent();
			refresh();
		}
	}

	@Override
	public void detach() {
		super.detach();

		// deactivate polling
		changeAutoSlideOption(false);
	}

	private void changeAutoSlideOption(boolean isActivated) {

		if (isActivated) {
			SormasUI.getCurrent().setPollInterval(1000 * 90);

			// set timer for slideshow
			if (pollRegistration == null) {
				pollRegistration = SormasUI.getCurrent().addPollListener(e -> {
					EbsEvent selectedEbsEvent = dashboardDataProvider.getEbsEvent();
					int nextEbsEventIndex = 0;

					if (selectedEbsEvent != null) {
						nextEbsEventIndex = ebsEvents.indexOf(selectedEbsEvent) + 1;

						if (nextEbsEventIndex >= ebsEvents.size()) {
							nextEbsEventIndex = 0;
						}
					}

					this.setActiveEbsEvent(ebsEvents.get(nextEbsEventIndex), true);
				});
			}
		} else {
			SormasUI.getCurrent().setPollInterval(-1);

			if (pollRegistration != null) {
				pollRegistration.remove();
				pollRegistration = null;
			}
		}
	}

	public void refresh() {
		this.statisticsComponent.refresh();
		//this.epiCurveComponent.clearAndFillEpiCurveChart();
		this.mapComponent.refreshMap();
	}
}
