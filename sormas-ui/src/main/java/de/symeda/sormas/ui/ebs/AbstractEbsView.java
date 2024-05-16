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
package de.symeda.sormas.ui.ebs;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import de.symeda.sormas.api.CoreFacade;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.ebs.TriagingDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SubMenu;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.events.EventActionsView;
import de.symeda.sormas.ui.events.EventDataView;
import de.symeda.sormas.ui.events.EventParticipantsView;
import de.symeda.sormas.ui.events.EventsView;
import de.symeda.sormas.ui.utils.AbstractEditAllowedDetailView;
import de.symeda.sormas.ui.utils.DirtyStateComponent;

@SuppressWarnings("serial")
public abstract class AbstractEbsView extends AbstractEditAllowedDetailView<EbsReferenceDto> {

	public static final String ROOT_VIEW_NAME = EBSView.VIEW_NAME;

	protected AbstractEbsView(String viewName) {
		super(viewName);
	}

	@Override
	protected CoreFacade getEditPermissionFacade() {
		return FacadeProvider.getEbsFacade();
	}

	@Override
	public void enter(ViewChangeEvent event) {

		super.enter(event);
		initOrRedirect(event);
	}

	@Override
	public void refreshMenu(SubMenu menu, String params) {

		if (!findReferenceByParams(params)) {
			return;
		}

		menu.removeAllViews();
		menu.addView(EBSView.VIEW_NAME, I18nProperties.getCaption(Captions.eventEventsList));
		menu.addView(EbsDataView.VIEW_NAME, I18nProperties.getCaption(Captions.SignalInformation), params);
		menu.addView(TriagingView.VIEW_NAME, I18nProperties.getCaption(Captions.Triaging), params);
		menu.addView(SignalVerificationView.VIEW_NAME, I18nProperties.getCaption(Captions.SignalVerification), params);
		menu.addView(RiskAssessmentView.VIEW_NAME, I18nProperties.getCaption(Captions.RiskAssessMent), params);

		setMainHeaderComponent(ControllerProvider.getEbsController().getEbsViewTitleLayout(getReference().getUuid()));
	}

	@Override
	protected EbsReferenceDto getReferenceByUuid(String uuid) {

		final EbsReferenceDto reference;
		if (FacadeProvider.getEbsFacade().exists(uuid)) {
			reference = FacadeProvider.getEbsFacade().getReferenceByUuid(uuid);
		} else {
			reference = null;
		}
		return reference;
	}

	@Override
	protected String getRootViewName() {
		return ROOT_VIEW_NAME;
	}

	@Override
	protected void setSubComponent(DirtyStateComponent newComponent) {
		super.setSubComponent(newComponent);

		if (getReference() != null && isEbsDeleted()) {
			newComponent.setEnabled(false);
		}
	}

	protected boolean isEbsDeleted() {
		return FacadeProvider.getEbsFacade().isDeleted(getEbsRef().getUuid());
	}

	public EbsReferenceDto getEbsRef() {
		return getReference();
	}
}
