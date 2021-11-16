package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.disease.DiseaseBurdenDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.LayoutUtil;

import java.util.List;

//public class DiseaseDetailsComponent extends VerticalLayout {
public class DiseaseDetailsComponent extends CssLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String viewTitleLabel;
	private VerticalLayout verticalLayout;

	public static final String CARD_COUNT = "count";
	public static final String DISEASE_GRID = "grid";
	public static final String DISEASE_MAP = "diseasemap";
	private DashboardDataProvider dashboardDataProvider;
	private List<DiseaseBurdenDto> diseaseBurden;

//	public DiseaseDetailsComponent(DiseaseBurdenDto diseaseBurden) {
	public DiseaseDetailsComponent(DashboardDataProvider dashboardDataProvider) {
		setSizeFull();
//		setMargin(false);
		this.dashboardDataProvider = dashboardDataProvider;
	}

	public void refresh(){
		String htmlLayout = LayoutUtil.fluidRow(
			LayoutUtil.fluidColumnLoc(8, 0, 12, 0, CARD_COUNT),
			LayoutUtil.fluidColumnLoc(4, 0, 6, 0, DISEASE_GRID),
			LayoutUtil.fluidColumnLoc(4, 0, 6, 0, DISEASE_MAP));

		addTopLayout(
				dashboardDataProvider.getDiseaseBurdenDetail().getDisease(),
				dashboardDataProvider.getDiseaseBurdenDetail().getCaseCount(),
				dashboardDataProvider.getDiseaseBurdenDetail().getPreviousCaseCount(),
				dashboardDataProvider.getDiseaseBurdenDetail().getOutbreakDistrictCount() > 0);

		addStatsLayout(
				dashboardDataProvider.getDiseaseBurdenDetail().getCaseDeathCount(),
				dashboardDataProvider.getDiseaseBurdenDetail().getEventCount(),
				dashboardDataProvider.getDiseaseBurdenDetail().getLastReportedDistrictName(),
				dashboardDataProvider.getDiseaseBurdenDetail().getDisease());
	}

	private void addTopLayout(Disease disease, Long casesCount, Long previousCasesCount, boolean isOutbreak) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		CssStyles.style(layout, CssStyles.getDiseaseColor(disease));
		layout.setHeight(75, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);

		VerticalLayout nameAndOutbreakLayout = new VerticalLayout();
		nameAndOutbreakLayout.setMargin(false);
		nameAndOutbreakLayout.setSpacing(false);
		nameAndOutbreakLayout.setHeight(100, Unit.PERCENTAGE);
		nameAndOutbreakLayout.setWidth(100, Unit.PERCENTAGE);

		HorizontalLayout nameLayout = new HorizontalLayout();
		nameLayout.setMargin(false);
		nameLayout.setSpacing(false);
		nameLayout.setWidth(100, Unit.PERCENTAGE);
		nameLayout.setHeight(100, Unit.PERCENTAGE);
		Label nameLabel = new Label(disease.toShortString());
		CssStyles.style(
			nameLabel,
			CssStyles.LABEL_WHITE,
			nameLabel.getValue().length() > 12 ? CssStyles.LABEL_LARGE : CssStyles.LABEL_XLARGE,
			CssStyles.LABEL_BOLD,
			CssStyles.ALIGN_CENTER,
			CssStyles.LABEL_UPPERCASE);
		nameLayout.addComponent(nameLabel);
		nameLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_CENTER);
		nameAndOutbreakLayout.addComponent(nameLayout);
		nameAndOutbreakLayout.setExpandRatio(nameLayout, 1);

		if (isOutbreak) {
			HorizontalLayout outbreakLayout = new HorizontalLayout();
			outbreakLayout.setMargin(false);
			outbreakLayout.setSpacing(false);
			CssStyles.style(outbreakLayout, CssStyles.BACKGROUND_CRITICAL);
			outbreakLayout.setWidth(100, Unit.PERCENTAGE);
			outbreakLayout.setHeight(30, Unit.PIXELS);
			Label outbreakLabel = new Label(I18nProperties.getCaption(Captions.dashboardOutbreak));
			CssStyles.style(outbreakLabel, CssStyles.LABEL_WHITE, CssStyles.ALIGN_CENTER, CssStyles.LABEL_UPPERCASE);
			outbreakLayout.addComponent(outbreakLabel);
			outbreakLayout.setComponentAlignment(outbreakLabel, Alignment.MIDDLE_CENTER);
			nameAndOutbreakLayout.addComponent(outbreakLayout);
		}

		layout.addComponent(nameAndOutbreakLayout);
		layout.setExpandRatio(nameAndOutbreakLayout, 1);

		VerticalLayout countLayout = new VerticalLayout();
		countLayout.setMargin(false);
		countLayout.setSpacing(false);
		CssStyles.style(countLayout, CssStyles.getDiseaseColor(disease), CssStyles.BACKGROUND_DARKER);
		countLayout.setHeight(100, Unit.PERCENTAGE);
		countLayout.setWidth(100, Unit.PERCENTAGE);

		Label countLabel = new Label(casesCount.toString());
		CssStyles
			.style(countLabel, CssStyles.LABEL_WHITE, CssStyles.LABEL_BOLD, CssStyles.LABEL_XXXLARGE, CssStyles.ALIGN_CENTER, CssStyles.VSPACE_TOP_4);
		countLayout.addComponent(countLabel);
		countLayout.setComponentAlignment(countLabel, Alignment.BOTTOM_CENTER);

		HorizontalLayout comparisonLayout = new HorizontalLayout();
		{
			comparisonLayout.setMargin(false);
			comparisonLayout.setSpacing(false);

			Label growthLabel = new Label("", ContentMode.HTML);
			String chevronType = "";
			if (previousCasesCount < casesCount) {
				chevronType = VaadinIcons.CHEVRON_UP.getHtml();
			} else if (previousCasesCount > casesCount) {
				chevronType = VaadinIcons.CHEVRON_DOWN.getHtml();
			} else {
				chevronType = VaadinIcons.CHEVRON_RIGHT.getHtml();
			}
			growthLabel.setValue(
				"<div class=\"v-label v-widget " + CssStyles.LABEL_WHITE + " v-label-" + CssStyles.LABEL_WHITE
					+ " align-center v-label-align-center bold v-label-bold v-has-width\" " + "	  style=\"margin-top: 3px;\">"
					+ "		<span class=\"v-icon\" style=\"font-family: VaadinIcons;\">" + chevronType + "		</span>" + "</div>");

			comparisonLayout.addComponent(growthLabel);

			Label previousCountLabel = new Label(previousCasesCount.toString());
			CssStyles.style(previousCountLabel, CssStyles.LABEL_WHITE, CssStyles.LABEL_BOLD, CssStyles.LABEL_XLARGE, CssStyles.HSPACE_LEFT_4);
			comparisonLayout.addComponent(previousCountLabel);
			comparisonLayout.setComponentAlignment(growthLabel, Alignment.MIDDLE_CENTER);
			comparisonLayout.setComponentAlignment(previousCountLabel, Alignment.MIDDLE_CENTER);
		}
		countLayout.addComponent(comparisonLayout);
		countLayout.setComponentAlignment(comparisonLayout, Alignment.MIDDLE_CENTER);

		countLayout.setExpandRatio(countLabel, 0.4f);
		countLayout.setExpandRatio(comparisonLayout, 0.6f);

		layout.addComponent(countLayout);
		layout.setExpandRatio(countLayout, 0.65f);

		addComponent(layout);
	}

	private void addStatsLayout(Long fatalities, Long events, String district, Disease disease) {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setMargin(false);
		layout.setSpacing(false);
		CssStyles.style(layout, CssStyles.BACKGROUND_HIGHLIGHT);

		HorizontalLayout statsItem = createStatsItem(
			I18nProperties.getCaption(Captions.dashboardLastReport) + ": ",
			district.length() == 0 ? I18nProperties.getString(Strings.none) : district,
			false,
			true);
		CssStyles.style(statsItem, CssStyles.VSPACE_TOP_4);
		layout.addComponent(statsItem);
		layout.addComponent(createStatsItem(I18nProperties.getCaption(Captions.dashboardFatalities), fatalities.toString(), fatalities > 0, false));
		statsItem = createStatsItem(I18nProperties.getCaption(Captions.DiseaseBurden_eventCount), events.toString(), false, false);
		CssStyles.style(statsItem, CssStyles.VSPACE_4);
		layout.addComponent(statsItem);

//		layout.addComponent(addDiseaseButton(disease.getName()));
		Button diseaseDetailButton = ButtonHelper
			.createIconButton(null, VaadinIcons.ELLIPSIS_DOTS_H, null, ValoTheme.BUTTON_BORDERLESS, CssStyles.VSPACE_TOP_NONE, CssStyles.VSPACE_4);
		diseaseDetailButton.setVisible(true);
		layout.addComponent(diseaseDetailButton);
		diseaseDetailButton.addClickListener(click -> ControllerProvider.getDashboardController().navigateToDisease(disease));

		addComponent(layout);
	}

	private HorizontalLayout createStatsItem(String label, String value, boolean isCritical, boolean singleColumn) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setMargin(false);
		layout.setSpacing(false);

		Label nameLabel = new Label(label);
		CssStyles.style(nameLabel, CssStyles.LABEL_PRIMARY, isCritical ? CssStyles.LABEL_CRITICAL : "", CssStyles.HSPACE_LEFT_3);
		layout.addComponent(nameLabel);
		if (!singleColumn) {
			layout.setExpandRatio(nameLabel, 1);
		}

		Label valueLabel = new Label(value);
		CssStyles.style(
			valueLabel,
			CssStyles.LABEL_PRIMARY,
			isCritical ? CssStyles.LABEL_CRITICAL : "",
			singleColumn ? CssStyles.HSPACE_LEFT_5 : CssStyles.ALIGN_CENTER);
		layout.addComponent(valueLabel);
		layout.setExpandRatio(valueLabel, singleColumn ? 1f : 0.65f);

		return layout;
	}
}
