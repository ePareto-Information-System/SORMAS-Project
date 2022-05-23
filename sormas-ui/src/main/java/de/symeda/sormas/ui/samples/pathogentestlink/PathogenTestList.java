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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import com.vaadin.ui.Notification;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.PaginationList;

@SuppressWarnings("serial")
public class PathogenTestList extends PaginationList<PathogenTestDto> {

	private int caseSampleCount;
	private Supplier<String> createOrEditAllowedCallback;
	private static final int MAX_DISPLAYED_ENTRIES = 5;

	private final SampleReferenceDto sampleRef;

	public PathogenTestList(SampleReferenceDto sampleRef, Consumer<Runnable> actionCallback,
	Supplier<String> createOrEditAllowedCallback) {
		super(MAX_DISPLAYED_ENTRIES);
		this.sampleRef = sampleRef;
		this.createOrEditAllowedCallback = createOrEditAllowedCallback;
	}

	@Override
	public void reload() {
		List<PathogenTestDto> pathogenTests = ControllerProvider.getPathogenTestController().getPathogenTestsBySample(sampleRef);

		setEntries(pathogenTests);
		if (!pathogenTests.isEmpty()) {
			showPage(1);
		} else {
			listLayout.removeAllComponents();
			updatePaginationLayout();
			Label noPathogenTestsLabel = new Label(I18nProperties.getString(Strings.infoNoPathogenTests));
			listLayout.addComponent(noPathogenTestsLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {
		List<PathogenTestDto> displayedEntries = getDisplayedEntries();
		for (PathogenTestDto pathogenTest : displayedEntries) {
			PathogenTestListEntry listEntry = new PathogenTestListEntry(pathogenTest);
			if (UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_EDIT)) {
				String pathogenTestUuid = pathogenTest.getUuid();
				listEntry.addEditButton("edit-test-" + pathogenTestUuid, (Button.ClickListener) event -> {
					if (createOrEditAllowedCallback.get() == null) {
						ControllerProvider.getPathogenTestController()
						.edit(pathogenTestUuid, SormasUI::refreshView, (pathogenTestDto, callback) -> callback.run());
					} else {
						Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Notification.Type.ERROR_MESSAGE);
					}
					return;
				});
			}
			listLayout.addComponent(listEntry);
		}
	}
}
