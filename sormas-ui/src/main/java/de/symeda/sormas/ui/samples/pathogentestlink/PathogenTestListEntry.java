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
package de.symeda.sormas.ui.samples.pathogentestlink;

import de.symeda.sormas.api.Disease;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import de.symeda.sormas.api.DiseaseHelper;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.components.sidecomponent.SideComponentField;

@SuppressWarnings("serial")
public class PathogenTestListEntry extends SideComponentField {

	private final PathogenTestDto pathogenTest;

	public PathogenTestListEntry(PathogenTestDto pathogenTest, boolean showTestResultText) {

		this.pathogenTest = pathogenTest;

		HorizontalLayout topLabelLayout = new HorizontalLayout();
		topLabelLayout.setSpacing(false);
		topLabelLayout.setMargin(false);
		topLabelLayout.setWidth(100, Unit.PERCENTAGE);
		addComponentToField(topLabelLayout);
		Label labelTopLeft = new Label(PathogenTestType.toString(pathogenTest.getTestType(), pathogenTest.getTestTypeText()));
		CssStyles.style(labelTopLeft, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
		topLabelLayout.addComponent(labelTopLeft);

		if (Boolean.TRUE.equals(pathogenTest.getTestResultVerified())) {
			Label labelTopRight = new Label(VaadinIcons.CHECK_CIRCLE.getHtml(), ContentMode.HTML);
			labelTopRight.setSizeUndefined();
			labelTopRight.addStyleName(CssStyles.LABEL_LARGE);
			labelTopRight.setDescription(I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.TEST_RESULT_VERIFIED));
			topLabelLayout.addComponent(labelTopRight);
			topLabelLayout.setComponentAlignment(labelTopRight, Alignment.TOP_RIGHT);
		}

		if (showTestResultText && !DataHelper.isNullOrEmpty(pathogenTest.getTestResultText())) {
			Label resultTextLabel = new Label(StringUtils.abbreviate(pathogenTest.getTestResultText(), 125));
			resultTextLabel.setDescription(pathogenTest.getTestResultText());
			resultTextLabel.setWidthFull();
			addComponentToField(resultTextLabel);
		}

		HorizontalLayout middleLabelLayout = new HorizontalLayout();
		middleLabelLayout.setSpacing(false);
		middleLabelLayout.setMargin(false);
		middleLabelLayout.setWidth(100, Unit.PERCENTAGE);
		addComponentToField(middleLabelLayout);

		Label labelMiddleLeft =
			new Label(DataHelper.toStringNullable(DiseaseHelper.toString(pathogenTest.getTestedDisease(), pathogenTest.getTestedDiseaseDetails())));
		middleLabelLayout.addComponent(labelMiddleLeft);

		Label labelMiddleRight = new Label(DateFormatHelper.formatLocalDateTime(pathogenTest.getTestDateTime()));
		labelMiddleRight.addStyleName(CssStyles.ALIGN_RIGHT);
		middleLabelLayout.addComponent(labelMiddleRight);
		middleLabelLayout.setComponentAlignment(labelMiddleRight, Alignment.TOP_RIGHT);

		if (pathogenTest.getTestedDiseaseVariant() != null || pathogenTest.getCqValue() != null) {
			HorizontalLayout bottomLabelLayout = new HorizontalLayout();
			bottomLabelLayout.setSpacing(false);
			bottomLabelLayout.setMargin(false);
			bottomLabelLayout.setWidth(100, Unit.PERCENTAGE);
			addComponentToField(bottomLabelLayout);

			if (pathogenTest.getTestedDiseaseVariant() != null) {
				Label labelBottomLeft = new Label(pathogenTest.getTestedDiseaseVariant().toString());
				bottomLabelLayout.addComponent(labelBottomLeft);
			}

			if (pathogenTest.getCqValue() != null) {
				Label labelBottomRight = new Label(
					I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.CQ_VALUE) + ": " + pathogenTest.getCqValue());
				labelBottomRight.addStyleName(CssStyles.ALIGN_RIGHT);
				bottomLabelLayout.addComponent(labelBottomRight);
				bottomLabelLayout.setComponentAlignment(labelBottomRight, Alignment.TOP_RIGHT);
			}
		}

		Label labelResult = new Label(DataHelper.toStringNullable(pathogenTest.getTestResult()));
		CssStyles.style(labelResult, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
		Label labelVariantResult = new Label("Variant: " + DataHelper.toStringNullable(pathogenTest.getTestResultVariant()));

		if (pathogenTest.getTestResult() == PathogenTestResultType.POSITIVE) {
			CssStyles.style(labelResult, CssStyles.LABEL_CRITICAL);
			if (pathogenTest.getTestResultVariant() != null){
				CssStyles.style(labelVariantResult, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
			}
		} else {
			CssStyles.style(labelResult, CssStyles.LABEL_WARNING);
		}
		addComponentToField(labelResult);
		addComponentToField(labelVariantResult);
		addILISupplementaryResults();

	}

	public PathogenTestDto getPathogenTest() {
		return pathogenTest;
	}

	private void addILISupplementaryResults() {

		if (pathogenTest.getTestedDisease() != Disease.NEW_INFLUENZA)
			return;

		if (pathogenTest.getSecondTestedDisease() != null
				&& pathogenTest.getTestResultForSecondDisease() != null) {

			// Disease name (normal)
			Label secondName = new Label(
					DiseaseHelper.toString(pathogenTest.getSecondTestedDisease(), null) + ": "
			);
			addComponentToField(secondName);

			Label secondResult = new Label(
					pathogenTest.getTestResultForSecondDisease().toString()
			);
			if (pathogenTest.getTestResultForSecondDisease() == PathogenTestResultType.POSITIVE) {
				CssStyles.style(secondResult, CssStyles.LABEL_CRITICAL, CssStyles.LABEL_UPPERCASE);
			} else {
				CssStyles.style(secondResult, CssStyles.LABEL_WARNING, CssStyles.LABEL_UPPERCASE);
			}
			addComponentToField(secondResult);
		}

		if (pathogenTest.getThirdPathogenTested() != null
				&& pathogenTest.getTestResultForThirdPathogen() != null) {

			Label thirdName = new Label(
					pathogenTest.getThirdPathogenTested() + ": "
			);
			addComponentToField(thirdName);

			Label thirdResult = new Label(
					pathogenTest.getTestResultForThirdPathogen().toString()
			);
			if (pathogenTest.getTestResultForThirdPathogen() == PathogenTestResultType.POSITIVE) {
				CssStyles.style(thirdResult, CssStyles.LABEL_CRITICAL, CssStyles.LABEL_UPPERCASE);
			} else {
				CssStyles.style(thirdResult, CssStyles.LABEL_WARNING, CssStyles.LABEL_UPPERCASE);
			}
			addComponentToField(thirdResult);

			if (pathogenTest.getPositiveSubtypes() != null) {
				Label subType = new Label(
						"Subtype: " + pathogenTest.getPositiveSubtypes().toString()
				);
				addComponentToField(subType);
				CssStyles.style(subType, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
			}
		}
	}

}
