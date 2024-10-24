/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
*/

package de.symeda.sormas.ui.caze;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.CoreFacade;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOrigin;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.*;
import de.symeda.sormas.ui.afpimmunization.AfpImmunizationView;
import de.symeda.sormas.ui.caze.maternalhistory.MaternalHistoryView;
import de.symeda.sormas.ui.caze.porthealthinfo.PortHealthInfoView;
import de.symeda.sormas.ui.clinicalcourse.ClinicalCourseView;
import de.symeda.sormas.ui.epidata.CaseEpiDataView;
import de.symeda.sormas.ui.externalmessage.ExternalMessagesView;
import de.symeda.sormas.ui.foodhistory.FoodHistoryView;
import de.symeda.sormas.ui.hospitalization.HospitalizationView;
import de.symeda.sormas.ui.riskfactor.RiskFactorView;
import de.symeda.sormas.ui.sixtydayfollowup.SixtyDayFollowupView;
import de.symeda.sormas.ui.therapy.TherapyView;
import de.symeda.sormas.ui.utils.*;

import java.util.Arrays;


public abstract class AbstractCaseView extends AbstractEditAllowedDetailView<CaseReferenceDto> {

	public static final String VIEW_MODE_URL_PREFIX = "v";

	public static final String ROOT_VIEW_NAME = CasesView.VIEW_NAME;
	public static final String NEXT_BUTTON_LOC = "nextButtonLoc";

	private Boolean hasOutbreak;
	private boolean caseFollowupEnabled;

	private final ViewConfiguration viewConfiguration;
	private final boolean redirectSimpleModeToCaseDataView;
	private final OptionGroup viewModeToggle;
	private final Property.ValueChangeListener viewModeToggleListener;

	protected AbstractCaseView(String viewName, boolean redirectSimpleModeToCaseDataView) {
		super(viewName);
		caseFollowupEnabled = FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_FOLLOWUP);

		if (!ViewModelProviders.of(AbstractCaseView.class).has(ViewConfiguration.class)) {
			// init default view mode
			ViewConfiguration initViewConfiguration = UserProvider.getCurrent().hasUserRight(UserRight.CLINICAL_COURSE_VIEW)
				|| UserProvider.getCurrent().hasUserRight(UserRight.THERAPY_VIEW)
					? new ViewConfiguration(ViewMode.NORMAL)
					: new ViewConfiguration(ViewMode.SIMPLE);
			ViewModelProviders.of(AbstractCaseView.class).get(ViewConfiguration.class, initViewConfiguration);
		}

		this.viewConfiguration = ViewModelProviders.of(AbstractCaseView.class).get(ViewConfiguration.class);
		this.redirectSimpleModeToCaseDataView = redirectSimpleModeToCaseDataView;

		viewModeToggle = new OptionGroup();
		CssStyles.style(viewModeToggle, ValoTheme.OPTIONGROUP_HORIZONTAL, CssStyles.OPTIONGROUP_HORIZONTAL_PRIMARY, CssStyles.VSPACE_TOP_3);
		viewModeToggle.addItems((Object[]) ViewMode.values());
		viewModeToggle.setItemCaption(ViewMode.SIMPLE, I18nProperties.getEnumCaption(ViewMode.SIMPLE));
		viewModeToggle.setItemCaption(ViewMode.NORMAL, I18nProperties.getEnumCaption(ViewMode.NORMAL));
		// View mode toggle is hidden by default
		viewModeToggle.setVisible(false);
		addHeaderComponent(viewModeToggle);

		viewModeToggleListener = event -> {
			viewConfiguration.setViewMode((ViewMode) event.getProperty().getValue());
			// refresh
			ControllerProvider.getCaseController().navigateToCase(getReference().getUuid());
		};
		viewModeToggle.addValueChangeListener(viewModeToggleListener);
	}

	@Override
	protected CoreFacade getEditPermissionFacade() {
		return FacadeProvider.getCaseFacade();
	}

	@Override
	public void refreshMenu(SubMenu menu, String params) {

		if (!findReferenceByParams(params)) {
			return;
		}

		CaseDataDto caze = FacadeProvider.getCaseFacade().getCaseDataByUuid(getReference().getUuid());

		// Handle outbreaks for the disease and district of the case
		if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.OUTBREAKS)
			&& (FacadeProvider.getOutbreakFacade().hasOutbreak(caze.getResponsibleDistrict(), caze.getDisease())
				|| FacadeProvider.getOutbreakFacade().hasOutbreak(caze.getDistrict(), caze.getDisease()))
			&& caze.getDisease().usesSimpleViewForOutbreaks()) {
			hasOutbreak = true;

			//			viewConfiguration.setViewMode(ViewMode.SIMPLE);
			//			// param might change this
			//			if (passedParams.length > 1 && passedParams[1].startsWith(VIEW_MODE_URL_PREFIX + "=")) {
			//				String viewModeString = passedParams[1].substring(2);
			//				try {
			//					viewConfiguration.setViewMode(ViewMode.valueOf(viewModeString.toUpperCase()));
			//				} catch (IllegalArgumentException ex) { } // just ignore
			//			}
			//
			viewModeToggle.removeValueChangeListener(viewModeToggleListener);
			viewModeToggle.setValue(viewConfiguration.getViewMode());
			viewModeToggle.addValueChangeListener(viewModeToggleListener);
			viewModeToggle.setVisible(true);

		} else {
			hasOutbreak = false;
			viewModeToggle.setVisible(false);
		}

		menu.removeAllViews();
		menu.addView(CasesView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, Captions.caseCasesList));

		if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.EXTERNAL_MESSAGES)
			&& UserProvider.getCurrent().hasUserRight(UserRight.EXTERNAL_MESSAGE_VIEW)
			&& FacadeProvider.getExternalMessageFacade().existsExternalMessageForEntity(getReference())) {
			menu.addView(ExternalMessagesView.VIEW_NAME, I18nProperties.getCaption(Captions.externalMessagesList));
		}

		menu.addView(CaseDataView.VIEW_NAME, I18nProperties.getCaption(CaseDataDto.I18N_PREFIX), params);

		boolean showExtraMenuEntries = FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(FeatureType.OUTBREAKS)
			|| !hasOutbreak
			|| !caze.getDisease().usesSimpleViewForOutbreaks()
			|| viewConfiguration.getViewMode() != ViewMode.SIMPLE;
		if (showExtraMenuEntries) {
			Disease disease = caze.getDisease();
			if (disease != Disease.MONKEYPOX) {
				if (disease == Disease.FOODBORNE_ILLNESS) {
					menu.addView(CasePersonView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.PATIENT_CLIENT), params);
				} else if (disease == Disease.NEW_INFLUENZA) {
					menu.addView(CasePersonView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.DEMOGRAPHIC), params);
				}
				else {
					menu.addView(CasePersonView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.PERSON), params);
				}
			}
			if (caze.getDisease() == Disease.CONGENITAL_RUBELLA) {
				menu.addView(
					MaternalHistoryView.VIEW_NAME,
					I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.MATERNAL_HISTORY),
					params);
			}
			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_HOSPITALIZATION)
				&& !caze.checkIsUnreferredPortHealthCase()
				&& !UserProvider.getCurrent().isPortHealthUser()) {
				if(caze.getDisease() != Disease.FOODBORNE_ILLNESS) {
					menu.addView(HospitalizationView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.HOSPITALIZATION), params);
				}else{
					menu.addView(HospitalizationView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.ILLNESS_INFO), params);
				}
			}


			if (caze.getCaseOrigin() == CaseOrigin.POINT_OF_ENTRY
				&& ControllerProvider.getCaseController().hasPointOfEntry(caze)
				&& UserProvider.getCurrent().hasUserRight(UserRight.PORT_HEALTH_INFO_VIEW)) {
				menu.addView(
					PortHealthInfoView.VIEW_NAME,
					I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.PORT_HEALTH_INFO),
					params);
			}
			if(caze.getDisease() != Disease.FOODBORNE_ILLNESS) {
				if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_SYMPTOMS)) {
					menu.addView(CaseSymptomsView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.SYMPTOMS), params);
				}
			}


			if(caze.getDisease() != Disease.MONKEYPOX){
				if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_EPIDEMIOLOGICAL_DATA)
						&& caze.getDisease() != Disease.CONGENITAL_RUBELLA) {
						if (caze.getDisease() != Disease.NEONATAL_TETANUS) {
						menu.addView(CaseEpiDataView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.EPI_DATA), params);
						}

				}
			}

			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_SURVEILANCE)
					&& !caze.checkIsUnreferredPortHealthCase()
					&& !UserProvider.getCurrent().isPortHealthUser()) {

				if (caze.getDisease() == Disease.CHOLERA) {
					menu.addView(
							RiskFactorView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.RISK_FACTOR), params);
				}
				else if (caze.getDisease() == Disease.MONKEYPOX){
					menu.addView(
							RiskFactorView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.RISK_FACTOR_NAME), params);
				}
			}

			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_SURVEILANCE)
					&& !caze.checkIsUnreferredPortHealthCase()
					&& !UserProvider.getCurrent().isPortHealthUser()) {
				if(caze.getDisease() == Disease.FOODBORNE_ILLNESS){
					menu.addView(
							FoodHistoryView.VIEW_NAME,
							I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.FOOD_HISTORY_TAB),
							params);
				}
			}
			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_SURVEILANCE)
					&& !caze.checkIsUnreferredPortHealthCase()
					&& !UserProvider.getCurrent().isPortHealthUser()) {
				if(caze.getDisease() == Disease.AFP){
					menu.addView(
							SixtyDayFollowupView.VIEW_NAME,
							I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.SIXTY_DAY),
							params);
					menu.addView(
							AfpImmunizationView.VIEW_NAME,
							I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.AFP_IMMUNIZATION),
							params);
				}
			}
			if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_SURVEILANCE)
					&& !caze.checkIsUnreferredPortHealthCase()
					&& !UserProvider.getCurrent().isPortHealthUser()) {
				if(caze.getDisease() == Disease.FOODBORNE_ILLNESS){
					menu.addView(
							SixtyDayFollowupView.VIEW_NAME,
							I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.FOOD_SAMPLE_TESTING),
							params);
				}
			}

			if (caze.getDisease() == Disease.CORONAVIRUS) {
				if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_THERAPY)
						&& UserProvider.getCurrent().hasUserRight(UserRight.THERAPY_VIEW)
						&& !caze.checkIsUnreferredPortHealthCase()
						&& !FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(FeatureType.CLINICAL_MANAGEMENT)) {

					menu.addView(TherapyView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.THERAPY), params);
				}
			}
		}

		if (caze.getDisease() != Disease.FOODBORNE_ILLNESS && caze.getDisease() != Disease.MONKEYPOX) {
			if (caseFollowupEnabled
					&& FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_FOLLOW_UP)
					&& caze.getFollowUpStatus() != FollowUpStatus.NO_FOLLOW_UP) {
				if (Arrays.asList().contains(caze.getDisease())) {
					menu.addView(
							ClinicalCourseView.VIEW_NAME,
							I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.CLINICAL_COURSE),
							params);
				}
				menu.addView(CaseVisitsView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.VISITS), params);
			}

			if (showExtraMenuEntries) {
				if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_FOLLOW_UP)
						&& FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.VIEW_TAB_CASES_CLINICAL_COURSE)
						&& UserProvider.getCurrent().hasUserRight(UserRight.CLINICAL_COURSE_VIEW)
						&& !caze.checkIsUnreferredPortHealthCase()
						&& !FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(FeatureType.CLINICAL_MANAGEMENT)) {
            /*menu.addView(
                ClinicalCourseView.VIEW_NAME,
                I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.CLINICAL_COURSE),
                params);*/
				}
			}
			if (FacadeProvider.getDiseaseConfigurationFacade().hasFollowUp(caze.getDisease())
					&& UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_VIEW)
					&& !caze.checkIsUnreferredPortHealthCase()) {
				menu.addView(CaseContactsView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, Captions.caseContacts), params);
			}

			if (caze.getExternalData() != null && !caze.getExternalData().isEmpty()) {
				menu.addView(CaseExternalDataView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.EXTERNAL_DATA), params);
			}
		}

		if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.SAMPLES_LAB)
				&& UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_VIEW)
				&& !caze.checkIsUnreferredPortHealthCase()) {
			if (!Arrays.asList(Disease.NEONATAL_TETANUS, Disease.FOODBORNE_ILLNESS).contains(caze.getDisease())) {
				menu.addView(CaseSampleView.VIEW_NAME, I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, Captions.caseSamples), params);
			}
		}


		setMainHeaderComponent(ControllerProvider.getCaseController().getCaseViewTitleLayout(caze));

		if (caseFollowupEnabled && UserProvider.getCurrent().hasUserRight(UserRight.MANAGE_EXTERNAL_SYMPTOM_JOURNAL)) {
			PersonDto casePerson = FacadeProvider.getPersonFacade().getByUuid(caze.getPerson().getUuid());
			ExternalJournalUtil.getExternalJournalUiButton(casePerson, caze).ifPresent(getButtonsLayout()::addComponent);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

		super.enter(event);

		if (getReference() == null) {
			UI.getCurrent().getNavigator().navigateTo(getRootViewName());
		} else if (redirectSimpleModeToCaseDataView && getViewMode() == ViewMode.SIMPLE) {
			ControllerProvider.getCaseController().navigateToCase(getReference().getUuid());
		} else {
			initView(event.getParameters().trim());
		}
	}

	@Override
	protected String getRootViewName() {
		return ROOT_VIEW_NAME;
	}

	@Override
	protected CaseReferenceDto getReferenceByUuid(String uuid) {

		final CaseReferenceDto reference;
		if (FacadeProvider.getCaseFacade().exists(uuid)) {
			reference = FacadeProvider.getCaseFacade().getReferenceByUuid(uuid);
		} else {
			reference = null;
		}
		return reference;
	}

	@Override
	protected void setSubComponent(DirtyStateComponent newComponent) {

		super.setSubComponent(newComponent);

		if (getReference() != null && FacadeProvider.getCaseFacade().isDeleted(getReference().getUuid())) {
			newComponent.setEnabled(false);
		}
	}

	public CaseReferenceDto getCaseRef() {
		return (CaseReferenceDto) getReference();
	}

	public boolean isHasOutbreak() {
		return hasOutbreak;
	}

	public ViewMode getViewMode() {

		if (Boolean.FALSE.equals(hasOutbreak)) {
			return ViewMode.NORMAL;
		}

		return viewConfiguration.getViewMode();
	}

	public HorizontalLayout getNextTabButton() {
		HorizontalLayout footerLayout = new HorizontalLayout();
		footerLayout.setMargin(false);
		footerLayout.setSpacing(true);
		footerLayout.setWidth(100, Unit.PERCENTAGE);

		//create a next button
		Button caseTabNextButton = ButtonHelper.createButton(Captions.caseNextTabButton, e -> {
			SormasUI.navigateToCaseChild();
		});

		footerLayout.addComponent(caseTabNextButton);
		footerLayout.setComponentAlignment(caseTabNextButton, Alignment.MIDDLE_RIGHT);
		return footerLayout;
	}


}
