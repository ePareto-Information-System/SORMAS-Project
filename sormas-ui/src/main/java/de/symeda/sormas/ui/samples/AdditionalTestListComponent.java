package de.symeda.sormas.ui.samples;


import java.util.function.Supplier;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.function.Consumer;

import com.vaadin.ui.Button;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.components.sidecomponent.SideComponent;

@SuppressWarnings("serial")
public class AdditionalTestListComponent extends SideComponent {

	private AdditionalTestList list;
	private Button createButton;
	public AdditionalTestListComponent(String sampleUuid, Supplier<String> createOrEditAllowedCallback) {
		super(sampleUuid);

		setWidth(100, Unit.PERCENTAGE);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		list = new AdditionalTestList(sampleUuid, createOrEditAllowedCallback);
		addComponent(list);
		list.reload();

		Label testsHeader = new Label(I18nProperties.getString(Strings.headingAdditionalTests));
		testsHeader.addStyleName(CssStyles.H3);
		componentHeader.addComponent(testsHeader);

		if (UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_CREATE)) {
			createButton = ButtonHelper.createIconButton(Captions.additionalTestNewTest, VaadinIcons.PLUS_CIRCLE, e -> {
				if (createOrEditAllowedCallback.get() == null) {
					ControllerProvider.getAdditionalTestController().openCreateComponent(sampleUuid, list::reload);
				} else {
					Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Type.ERROR_MESSAGE);
				}
			}, ValoTheme.BUTTON_PRIMARY);

			componentHeader.addComponent(createButton);
			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);



	// public AdditionalTestListComponent(String sampleUuid, Consumer<Runnable> actionCallback, boolean isEditAllowed) {
	// 	super(I18nProperties.getString(Strings.headingAdditionalTests), actionCallback);

	// 	list = new AdditionalTestList(sampleUuid, actionCallback, isEditAllowed);
	// 	addComponent(list);
	// 	list.reload();

	// 	if (isEditAllowed && UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_CREATE)) {
	// 		addCreateButton(
	// 			I18nProperties.getCaption(Captions.additionalTestNewTest),
	// 			() -> ControllerProvider.getAdditionalTestController().openCreateComponent(sampleUuid, list::reload),
	// 			UserRight.ADDITIONAL_TEST_CREATE);
		}
		//if(UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_EDIT_PATHOGEN_TEST_REFRERRED_TO) ) {
			createButton.setEnabled(true);
			list.setEnabled(true);
		//}

	}

	public AdditionalTestListComponent(String sampleUuid, Consumer<Runnable> actionCallback, boolean isEditAllowed) {
		super(I18nProperties.getString(Strings.headingAdditionalTests), actionCallback);

		list = new AdditionalTestList(sampleUuid, actionCallback, isEditAllowed);
		addComponent(list);
		list.reload();

		//if (isEditAllowed && UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_CREATE)) {
			addCreateButton(
					I18nProperties.getCaption(Captions.additionalTestNewTest),
					() -> ControllerProvider.getAdditionalTestController().openCreateComponent(sampleUuid, list::reload),
					UserRight.ADDITIONAL_TEST_CREATE);
		//}


		setWidth(100, Unit.PERCENTAGE);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);

		//list = new AdditionalTestList(sampleUuid, createOrEditAllowedCallback);
		addComponent(list);
		list.reload();

//		Label testsHeader = new Label(I18nProperties.getString(Strings.headingAdditionalTests));
//		testsHeader.addStyleName(CssStyles.H3);
//		componentHeader.addComponent(testsHeader);

		//if (UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_CREATE)) {
//			createButton = ButtonHelper.createIconButton(Captions.additionalTestNewTest, VaadinIcons.PLUS_CIRCLE, e -> {
//				//if (createOrEditAllowedCallback.get() == null) {
//					ControllerProvider.getAdditionalTestController().openCreateComponent(sampleUuid, list::reload);
////				} else {
////					Notification.show(null, I18nProperties.getString(createOrEditAllowedCallback.get()), Type.ERROR_MESSAGE);
////				}
//			}, ValoTheme.BUTTON_PRIMARY);

//			componentHeader.addComponent(createButton);
//			componentHeader.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);



			// public AdditionalTestListComponent(String sampleUuid, Consumer<Runnable> actionCallback, boolean isEditAllowed) {
			// 	super(I18nProperties.getString(Strings.headingAdditionalTests), actionCallback);

			// 	list = new AdditionalTestList(sampleUuid, actionCallback, isEditAllowed);
			// 	addComponent(list);
			// 	list.reload();

			// 	if (isEditAllowed && UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_CREATE)) {
			// 		addCreateButton(
			// 			I18nProperties.getCaption(Captions.additionalTestNewTest),
			// 			() -> ControllerProvider.getAdditionalTestController().openCreateComponent(sampleUuid, list::reload),
			// 			UserRight.ADDITIONAL_TEST_CREATE);
		//}
		//if(UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_EDIT_PATHOGEN_TEST_REFRERRED_TO) ) {
//			createButton.setEnabled(false);
			list.setEnabled(false);
		//}
	}
	public AdditionalTestListComponent(String heading) {
		super(heading);
	}

	public AdditionalTestListComponent(String heading, Consumer<Runnable> actionCallback) {
		super(heading, actionCallback);
	}


	public void reload() {
		list.reload();
	}
}
