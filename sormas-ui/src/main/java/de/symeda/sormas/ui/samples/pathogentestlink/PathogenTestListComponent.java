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

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.ui.Notification;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.utils.components.sidecomponent.SideComponent;

@SuppressWarnings("serial")
public class PathogenTestListComponent extends SideComponent {

	public PathogenTestListComponent(
		SampleReferenceDto sampleRef, Consumer<Runnable> actionCallback, 
		Supplier<String> createOrEditAllowedCallback) {
		super(I18nProperties.getString(Strings.headingTests), actionCallback);
		setWidth(100, Unit.PERCENTAGE);

		addCreateButton(
				I18nProperties.getCaption(Captions.pathogenTestNewTest),
				() -> {
					if (createOrEditAllowedCallback.get() == null) {
						ControllerProvider.getPathogenTestController().create(sampleRef, 0, (pathogenTest, callback) -> callback.run());
					} else {
						Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Notification.Type.ERROR_MESSAGE);
					}
				},
//				() ->
//						createOrEditAllowedCallback.get() == null ?
//						ControllerProvider.getPathogenTestController().create(sampleRef, 0, (pathogenTest, callback) -> callback.run())
//					: Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Notification.Type.ERROR_MESSAGE),
				UserRight.PATHOGEN_TEST_CREATE);

		PathogenTestList pathogenTestList = new PathogenTestList(sampleRef, actionCallback, createOrEditAllowedCallback);
		addComponent(pathogenTestList);
		pathogenTestList.reload();
	}

}
