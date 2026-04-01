package de.symeda.sormas.ui.samples.pathogentestlink;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.DiseaseHelper;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PosNeg;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.components.sidecomponent.SideComponentField;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;

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

		if (pathogenTest.getTestResult() == PathogenTestResultType.POSITIVE) {
			CssStyles.style(labelResult, CssStyles.LABEL_CRITICAL);
		} else {
			CssStyles.style(labelResult, CssStyles.LABEL_WARNING);
		}

		addComponentToField(labelResult);
		addILISupplementaryResults();
		addVHFSupplementaryResults();
	}

	private void addVHFSupplementaryResults() {

		if (getAssociatedEntityDisease() != Disease.UNSPECIFIED_VHF)
			return;

		Collection<PathogenTestType> selectedTests = pathogenTest.getSampleTests();
		if (selectedTests == null || selectedTests.isEmpty())
			return;

		if (selectedTests.contains(PathogenTestType.PCR)) {
			addVhfSupplementaryResult("PCR", pathogenTest.getSampleTestResultPCR(), pathogenTest.getSampleTestResultPCRDate());
		}

		if (selectedTests.contains(PathogenTestType.ANTIGEN_DETECTION)) {
			addVhfSupplementaryResult("Antigen", pathogenTest.getSampleTestResultAntigen(), pathogenTest.getSampleTestResultAntigenDate());
		}

		if (selectedTests.contains(PathogenTestType.IGM_SERUM_ANTIBODY)) {
			addVhfSupplementaryResult("IgM", pathogenTest.getSampleTestResultIGM(), pathogenTest.getSampleTestResultIGMDate());
		}

		if (selectedTests.contains(PathogenTestType.IGG_SERUM_ANTIBODY)) {
			addVhfSupplementaryResult("IgG", pathogenTest.getSampleTestResultIGG(), pathogenTest.getSampleTestResultIGGDate());
		}

		if (selectedTests.contains(PathogenTestType.IMMUNO)) {
			addVhfSupplementaryResult("Immuno", pathogenTest.getSampleTestResultImmuno(), pathogenTest.getSampleTestResultImmunoDate());
		}
	}

	private Disease getAssociatedEntityDisease() {

		if (pathogenTest.getSample() == null || pathogenTest.getSample().getUuid() == null)
			return null;

		SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(pathogenTest.getSample().getUuid());
		if (sample == null)
			return null;

		if (sample.getAssociatedCase() != null) {
			CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(sample.getAssociatedCase().getUuid());
			return caseDataDto != null ? caseDataDto.getDisease() : null;
		}

		if (sample.getAssociatedContact() != null) {
			ContactDto contactDto = FacadeProvider.getContactFacade().getContactByUuid(sample.getAssociatedContact().getUuid());
			return contactDto != null ? contactDto.getDisease() : null;
		}

		if (sample.getAssociatedEventParticipant() != null) {
			EventParticipantDto eventParticipantDto =
					FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(sample.getAssociatedEventParticipant().getUuid());

			if (eventParticipantDto != null && eventParticipantDto.getEvent() != null) {
				EventDto eventDto = FacadeProvider.getEventFacade().getEventByUuid(eventParticipantDto.getEvent().getUuid(), false);
				return eventDto != null ? eventDto.getDisease() : null;
			}
		}

		return null;
	}

	private void addVhfSupplementaryResult(String testName, PosNeg result, Date resultDate) {

		if (result == null && resultDate == null)
			return;

		HorizontalLayout resultLayout = new HorizontalLayout();
		resultLayout.setSpacing(true);
		resultLayout.setMargin(false);
		resultLayout.setWidth(100, Unit.PERCENTAGE);

		Label nameLabel = new Label(testName + ":");
		resultLayout.addComponent(nameLabel);

		if (result != null) {
			Label resultLabel = new Label(result.toString());
			if (result == PosNeg.POSITIVE) {
				CssStyles.style(resultLabel, CssStyles.LABEL_CRITICAL, CssStyles.LABEL_UPPERCASE);
			} else {
				CssStyles.style(resultLabel, CssStyles.LABEL_WARNING, CssStyles.LABEL_UPPERCASE);
			}
			resultLayout.addComponent(resultLabel);
		}

		if (resultDate != null) {
			Label dateLabel = new Label(DateFormatHelper.formatLocalDate(resultDate));
			CssStyles.style(dateLabel, CssStyles.LABEL_BOLD);
			resultLayout.addComponent(dateLabel);
		}

		addComponentToField(resultLayout);
	}

	private void addILISupplementaryResults() {

		if (pathogenTest.getTestedDisease() != Disease.NEW_INFLUENZA)
			return;

		if (pathogenTest.getSecondTestedDisease() != null
				&& pathogenTest.getTestResultForSecondDisease() != null) {

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

		if (pathogenTest.getTestResultVariant() != null) {
			Label labelVariantResult = new Label("Variant: " + DataHelper.toStringNullable(pathogenTest.getTestResultVariant()));
			CssStyles.style(labelVariantResult, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
			addComponentToField(labelVariantResult);
		}
	}
}