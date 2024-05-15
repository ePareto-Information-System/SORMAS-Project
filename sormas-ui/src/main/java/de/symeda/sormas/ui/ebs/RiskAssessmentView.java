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

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;


public class RiskAssessmentView extends AbstractEbsView {
	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/riskAssessment";
	public RiskAssessmentGrid grid;
	private Button addButton;
	private VerticalLayout gridLayout;

	public RiskAssessmentView() {
		super(VIEW_NAME);
	}


	@Override
	protected void initView(String params) {

		addButton = ButtonHelper.createIconButton(
				Captions.eventNewEvent,
				VaadinIcons.PLUS_CIRCLE,
				e -> ControllerProvider.getEbsController().createRiskAssessmentComponent(getEbsRef().getUuid(),
						isEditAllowed() && UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EDIT)),
				ValoTheme.BUTTON_PRIMARY);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		buttonLayout.setSizeUndefined();

		buttonLayout.addComponent(addButton);

		grid = new RiskAssessmentGrid(new RiskAssessmentDto(), getEbsRef().getUuid());

		gridLayout = new VerticalLayout();
		gridLayout.addComponent(buttonLayout);
		gridLayout.addComponent(grid);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(false);
		gridLayout.setSizeFull();
		gridLayout.setExpandRatio(grid, 1);
		addComponent(gridLayout);

	}
}