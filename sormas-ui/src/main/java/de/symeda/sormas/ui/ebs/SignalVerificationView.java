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

import com.vaadin.ui.Notification;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;


public class SignalVerificationView extends AbstractEbsView {
	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/signalVerification";

	public SignalVerificationView() {
		super(VIEW_NAME,EBSView.currentview);
	}

	@Override
	protected void initView(String params) {
		if (ControllerProvider.getEbsController().isTriagingDiscard(getEbsRef().getUuid())) {
			Notification.show(I18nProperties.getCaption(Captions.ebsSignalVerificationDisabled), WARNING_MESSAGE);
			return;
		}
		CommitDiscardWrapperComponent<SignalVerificationDataForm> signalVerficationForm = ControllerProvider.getEbsController()
				.getEbsCreateSignalVerficationComponent(getEbsRef().getUuid(),
						isEditAllowed() && UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EDIT));
		setSubComponent(signalVerficationForm);
		setEditPermission(
				signalVerficationForm,
				UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EDIT));

	}
}
