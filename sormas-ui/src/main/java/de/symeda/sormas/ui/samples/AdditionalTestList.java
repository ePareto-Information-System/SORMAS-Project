package de.symeda.sormas.ui.samples;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.Consumer;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.AdditionalTestDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.PaginationList;

@SuppressWarnings("serial")
public class AdditionalTestList extends PaginationList<AdditionalTestDto> {

	private String sampleUuid;
	private Supplier<String> createOrEditAllowedCallback;

	public AdditionalTestList(String sampleUuid, Supplier<String> createOrEditAllowedCallback) {

		super(3);
		this.sampleUuid = sampleUuid;
		this.createOrEditAllowedCallback = createOrEditAllowedCallback;
	}
	private Consumer<Runnable> actionCallback;
	private boolean isEditable;

	public AdditionalTestList(String sampleUuid, Consumer<Runnable> actionCallback, boolean isEditable) {
		super(3);
		this.sampleUuid = sampleUuid;
		this.isEditable = isEditable;
		this.actionCallback = actionCallback;
	}

	@Override
	public void reload() {

		List<AdditionalTestDto> additionalTests = ControllerProvider.getAdditionalTestController().getAdditionalTestsBySample(sampleUuid);

		setEntries(additionalTests);
		if (!additionalTests.isEmpty()) {
			showPage(1);
		} else {
			listLayout.removeAllComponents();
			updatePaginationLayout();
			Label noAdditionalTestsLabel = new Label(I18nProperties.getString(Strings.infoNoAdditionalTests));
			listLayout.addComponent(noAdditionalTestsLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {

		List<AdditionalTestDto> displayedEntries = getDisplayedEntries();
		for (int i = 0, displayedEntriesSize = displayedEntries.size(); i < displayedEntriesSize; i++) {
			AdditionalTestDto additionalTest = displayedEntries.get(i);
			AdditionalTestListEntry listEntry = new AdditionalTestListEntry(additionalTest);
			if (UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_EDIT)) {
				listEntry.addEditListener(i, e -> {
					if (createOrEditAllowedCallback.get() == null) {
						ControllerProvider.getAdditionalTestController().openEditComponent(additionalTest, AdditionalTestList.this::reload);
					} else {
						Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Type.ERROR_MESSAGE);
					}
				});
			}
//start 1.87.0
			boolean isEditableAndHasEditRight =
				UserProvider.getCurrent().hasAllUserRightsWithEditAllowedFlag(isEditable, UserRight.ADDITIONAL_TEST_EDIT);
			boolean isEditableAndHasDeleteRight =
				UserProvider.getCurrent().hasAllUserRightsWithEditAllowedFlag(isEditable, UserRight.ADDITIONAL_TEST_DELETE);

			listEntry.addActionButton(
				additionalTest.getUuid(),
				e -> actionCallback.accept(
					() -> ControllerProvider.getAdditionalTestController()
						.openEditComponent(additionalTest, AdditionalTestList.this::reload, isEditableAndHasEditRight, isEditableAndHasDeleteRight)),
				isEditableAndHasEditRight);
			listEntry.setEnabled(isEditable); //end 1.87.0
			listLayout.addComponent(listEntry);
		}
	}
}
