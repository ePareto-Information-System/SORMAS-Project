/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.ui.ebs;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.EditPermissionType;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.docgeneration.EventDocumentsComponent;
import de.symeda.sormas.ui.externalsurveillanceservice.ExternalSurveillanceServiceGateway;
import de.symeda.sormas.ui.externalsurveillanceservice.ExternalSurveillanceShareComponent;
import de.symeda.sormas.ui.sormastosormas.SormasToSormasListComponent;
import de.symeda.sormas.ui.utils.*;


public class EbsDataView extends AbstractEbsView {

	private static final long serialVersionUID = -1L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/data";

	public static final String EVENT_LOC = "signalInformation";
	public static final String TRIAGE_LOC = "triaging";
	public static final String VERIFICATION_LOC = "signalVerification";
	public static final String RISK_LOC = "riskAssessment";
	public static final String SHORTCUT_LINKS_LOC = "shortcut-links";
	public static final String DOCUMENTS_LOC = "documents";
	public static final String SORMAS_TO_SORMAS_LOC = "sormasToSormas";

	private CommitDiscardWrapperComponent<?> editComponent;
	private ExternalSurveillanceShareComponent externalSurvToolLayout;

	public EbsDataView() {
		super(VIEW_NAME);
	}

	@Override
	protected void initView(String params) {

		EbsDto ebs = FacadeProvider.getEbsFacade().getEbsByUuid(getEbsRef().getUuid(), false);

		setHeightUndefined();

		DetailSubComponentWrapper container = new DetailSubComponentWrapper(() -> editComponent);
		container.setWidth(100, Unit.PERCENTAGE);
		container.setMargin(true);
		setSubComponent(container);
		container.setEnabled(true);

		editComponent =
			ControllerProvider.getEbsController().getEbsDataEditComponent(getEbsRef().getUuid(), null);

		LayoutWithSidePanel layout = new LayoutWithSidePanel(
			editComponent,
				TRIAGE_LOC,
				VERIFICATION_LOC,
				RISK_LOC
		);

		container.addComponent(layout);

		final String uuid = ebs.getUuid();
		final EditPermissionType ebsEditAllowed = FacadeProvider.getEbsFacade().getEditPermissionType(uuid);
		boolean isEditAllowed = isEditAllowed();

		VerticalLayout shortcutLinksLayout = new VerticalLayout();
		shortcutLinksLayout.setMargin(false);
		shortcutLinksLayout.setSpacing(true);


		layout.addSidePanelComponent(shortcutLinksLayout, SHORTCUT_LINKS_LOC);

		final boolean deleted = FacadeProvider.getEventFacade().isDeleted(uuid);
		layout.disableIfNecessary(deleted, ebsEditAllowed);
		editComponent.setEnabled(isEditAllowed());
	}
}
