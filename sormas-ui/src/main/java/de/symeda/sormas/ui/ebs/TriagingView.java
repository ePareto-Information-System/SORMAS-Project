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

import com.vaadin.ui.VerticalLayout;
import de.symeda.sormas.api.EditPermissionType;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.externalsurveillanceservice.ExternalSurveillanceShareComponent;
import de.symeda.sormas.ui.hospitalization.HospitalizationForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.DetailSubComponentWrapper;
import de.symeda.sormas.ui.utils.LayoutWithSidePanel;


public class TriagingView extends AbstractEbsView {
	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/triaging";

	public TriagingView() {
		super(VIEW_NAME);
	}

	@Override
	protected void initView(String params) {

		CommitDiscardWrapperComponent<TriagingDataForm> triagingForm = ControllerProvider.getEbsController()
				.getEbsCreateTriagingComponent(getEbsRef().getUuid(),
						isEditAllowed() && UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EDIT));
		setSubComponent(triagingForm);
		setEditPermission(
				triagingForm,
				UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EDIT));
	}
}
