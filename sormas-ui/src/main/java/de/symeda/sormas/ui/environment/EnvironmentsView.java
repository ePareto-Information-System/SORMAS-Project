/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.ui.environment;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.environment.EnvironmentCriteria;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.ViewConfiguration;

public class EnvironmentsView extends AbstractView {

	public static final String VIEW_NAME = "environments";
	private static final long serialVersionUID = 5429099846079698053L;
	private final ViewConfiguration viewConfiguration;
	private final EnvironmentGridComponent gridComponent;

	private final EnvironmentCriteria gridCriteria;

	public EnvironmentsView() {
		super(VIEW_NAME);

		setSizeFull();

		viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
		gridCriteria = ViewModelProviders.of(getClass())
			.getOrDefault(EnvironmentCriteria.class, () -> new EnvironmentCriteria().relevanceStatus(EntityRelevanceStatus.ACTIVE));

		if (UserProvider.getCurrent().hasUserRight(UserRight.ENVIRONMENT_CREATE)) {
			final Button btnNewContact = ButtonHelper.createIconButton(
				Captions.environmentNewEnvironment,
				VaadinIcons.PLUS_CIRCLE,
				e -> ControllerProvider.getEnvironmentController().create(),
				ValoTheme.BUTTON_PRIMARY);
			addHeaderComponent(btnNewContact);

		}

		gridComponent = new EnvironmentGridComponent(gridCriteria, viewConfiguration, () -> navigateTo(gridCriteria, true), () -> {
			ViewModelProviders.of(getClass()).remove(EnvironmentCriteria.class);
			navigateTo(null, true);
		});
		addComponent(gridComponent);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		String params = event.getParameters().trim();
		if (params.startsWith("?")) {
			params = params.substring(1);
			gridCriteria.fromUrlParams(params);
		}

		setApplyingCriteria(true);
		gridComponent.updateFilterComponents((gridCriteria));
		setApplyingCriteria(false);

		gridComponent.reload();
	}
}
