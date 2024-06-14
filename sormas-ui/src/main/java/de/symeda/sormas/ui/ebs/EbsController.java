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

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.deletionconfiguration.DeletionInfoDto;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.ui.utils.components.automaticdeletion.DeletionLabel;
import de.symeda.sormas.ui.utils.components.page.title.TitleLayout;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

public class EbsController {

	public void registerViews(Navigator navigator) {
		navigator.addView(EBSView.VIEW_NAME, EBSView.class);
		navigator.addView(EbsDataView.VIEW_NAME, EbsDataView.class);
		navigator.addView(TriagingView.VIEW_NAME,TriagingView.class);
		navigator.addView(SignalVerificationView.VIEW_NAME,SignalVerificationView.class);
		navigator.addView(RiskAssessmentView.VIEW_NAME,RiskAssessmentView.class);
		navigator.addView(EbsAlertView.VIEW_NAME,EbsAlertView.class);
	}

	public void navigateToIndex() {
		String navigationState = EBSView.VIEW_NAME;
		SormasUI.get().getNavigator().navigateTo(navigationState);
	}

	public void navigateToData(String ebsUuid) {
		navigateToData(ebsUuid, false);
	}

	public void navigateToData(String ebsUuid, boolean openTab) {

		String navigationState = EbsDataView.VIEW_NAME + "/" + ebsUuid;
		if (openTab) {
			SormasUI.get().getPage().open(SormasUI.get().getPage().getLocation().getRawPath() + "#!" + navigationState, "_blank", false);
		} else {
			SormasUI.get().getNavigator().navigateTo(navigationState);
		}
	}

	public void navigateToTriaging(String ebsUuid, boolean openTab) {

		String navigationState = TriagingView.VIEW_NAME + "/" + ebsUuid;
		if (openTab) {
			SormasUI.get().getPage().open(SormasUI.get().getPage().getLocation().getRawPath() + "#!" + navigationState, "_blank", false);
		} else {
			SormasUI.get().getNavigator().navigateTo(navigationState);
		}
	}
	public void navigateToSignalVerification(String ebsUuid, boolean openTab) {

		String navigationState = SignalVerificationView.VIEW_NAME + "/" + ebsUuid;
		if (openTab) {
			SormasUI.get().getPage().open(SormasUI.get().getPage().getLocation().getRawPath() + "#!" + navigationState, "_blank", false);
		} else {
			SormasUI.get().getNavigator().navigateTo(navigationState);
		}
	}
	public void navigateToRiskAssessment(String ebsUuid, boolean openTab) {

		String navigationState = RiskAssessmentView.VIEW_NAME + "/" + ebsUuid;
		if (openTab) {
			SormasUI.get().getPage().open(SormasUI.get().getPage().getLocation().getRawPath() + "#!" + navigationState, "_blank", false);
		} else {
			SormasUI.get().getNavigator().navigateTo(navigationState);
		}
	}
	public void navigateToAlert(String ebsUuid, boolean openTab) {

		String navigationState = EbsAlertView.VIEW_NAME + "/" + ebsUuid;
		if (openTab) {
			SormasUI.get().getPage().open(SormasUI.get().getPage().getLocation().getRawPath() + "#!" + navigationState, "_blank", false);
		} else {
			SormasUI.get().getNavigator().navigateTo(navigationState);
		}
	}

	public void navigateTo(EbsCriteria ebsCriteria) {
		navigateTo(ebsCriteria, false);
	}

	public void navigateTo(EbsCriteria ebsCriteria, boolean changeToDefaultViewType) {
		if (changeToDefaultViewType) {
			ViewModelProviders.of(EBSView.class).remove(EbsViewConfiguration.class);
		}
		ViewModelProviders.of(EBSView.class).remove(EbsCriteria.class);
		String navigationState = AbstractView.buildNavigationState(EBSView.VIEW_NAME, ebsCriteria);
		SormasUI.get().getNavigator().navigateTo(navigationState);
	}

	public EbsDto findEbs(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}


	public EbsDto createNewEvent(Disease disease) {
		return EbsDto.build(FacadeProvider.getCountryFacade().getServerCountry(), UserProvider.getCurrent().getUser());
	}
	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent() {

		EbsDataForm form = new EbsDataForm(null,true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
		form.setValue(createNewEvent(null));

		final CommitDiscardWrapperComponent<EbsDataForm> component =
				new CommitDiscardWrapperComponent<>(form, UserProvider.getCurrent().hasAllUserRights(UserRight.EVENT_CREATE), form.getFieldGroup());
		component.getDiscardButton().setCaption("Cancel");
		component.addCommitListener(() -> {
			if (!form.getFieldGroup().isModified()) {
				EbsDto newEvent = form.getValue();


				FacadeProvider.getEbsFacade().save(newEvent);


				EbsReferenceDto newEventRef = new EbsReferenceDto(newEvent.getUuid());
				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.TRAY_NOTIFICATION);
				navigateToTriaging(newEventRef.getUuid(),false);
			}
		});

		return component;
	}

	public CommitDiscardWrapperComponent<TriagingDataForm> getEbsCreateTriagingComponent(final String ebsUuid,
																						 boolean isEditAllowed) {
		EbsDto ebs = findEbs(ebsUuid);
		TriagingDataForm triagingDataForm = new TriagingDataForm(
				ebs,
				EbsDto.class,
				ebs.isPseudonymized(),
				ebs.isInJurisdiction(),
				isEditAllowed);
		triagingDataForm.setValue(ebs.getTriaging());

		final CommitDiscardWrapperComponent<TriagingDataForm> editView = new CommitDiscardWrapperComponent<TriagingDataForm>(
				triagingDataForm,
				triagingDataForm.getFieldGroup());
		editView.getDiscardButton().setCaption("Cancel");
		TriagingDto newEvent = triagingDataForm.getValue();
		editView.addCommitListener(() -> {
			ebs.setTriaging(triagingDataForm.getValue());
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
			navigateToSignalVerification(ebs.getUuid(),false);
		});

		return editView;
	}
	public CommitDiscardWrapperComponent<SignalVerificationDataForm> getEbsCreateSignalVerficationComponent(final String ebsUuid,
																						 boolean isEditAllowed) {
		EbsDto ebs = findEbs(ebsUuid);
		SignalVerificationDataForm signalVerificationDataForm = new SignalVerificationDataForm(
				ebs,
				EbsDto.class,
				ebs.isPseudonymized(),
				ebs.isInJurisdiction(),
				isEditAllowed);
		signalVerificationDataForm.setValue(ebs.getSignalVerification());
		final CommitDiscardWrapperComponent<SignalVerificationDataForm> editView = new CommitDiscardWrapperComponent<SignalVerificationDataForm>(
				signalVerificationDataForm,
				signalVerificationDataForm.getFieldGroup());
		editView.getDiscardButton().setCaption("Cancel");
		editView.addCommitListener(() -> {
			ebs.setSignalVerification(signalVerificationDataForm.getValue());
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
			Notification.show(I18nProperties.getString(Strings.messageSignalVerificationSavedShort), TRAY_NOTIFICATION);
			navigateToRiskAssessment(ebs.getUuid(),false);
		});

		return editView;
	}

	public CommitDiscardWrapperComponent<RiskAssessmentDataForm> getEbsCreateRiskAssessmentComponent(final String ebsUuid,
																						 boolean isEditAllowed) {
		EbsDto ebs = findEbs(ebsUuid);
		RiskAssessmentDto riskAssessmentDto = RiskAssessmentDto.build(ebs.toReference());
		RiskAssessmentDataForm riskAssessmentDataForm = new RiskAssessmentDataForm(
				ebs,
				EbsDto.class,
				ebs.isPseudonymized(),
				ebs.isInJurisdiction(),
				isEditAllowed);

		riskAssessmentDataForm.setValue(riskAssessmentDto);
		final CommitDiscardWrapperComponent<RiskAssessmentDataForm> editView = new CommitDiscardWrapperComponent<RiskAssessmentDataForm>(
				riskAssessmentDataForm,
				riskAssessmentDataForm.getFieldGroup());
		editView.getDiscardButton().setCaption("Cancel");
		editView.addCommitListener(() -> {
			ebs.setRiskAssessment(riskAssessmentDataForm.getValue());
			riskAssessmentDto.setRiskAssessment(riskAssessmentDataForm.getValue().getRiskAssessment());
			FacadeProvider.getRiskAssessmentFacade().saveRisk(riskAssessmentDto);
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
			Notification notification =  Notification.show(I18nProperties.getString(Strings.messageRiskAssessmentSavedShort), TRAY_NOTIFICATION);
			showAssessmentCaseDialog(riskAssessmentDto);
			notification.addCloseListener(e -> {
				navigateToAlert(ebs.getUuid(),false);
			});
		});


		return editView;
	}

	public void showAssessmentCaseDialog(RiskAssessmentDto riskAssessmentDto) {
		Label riskCaption = new Label();
		riskCaption.addStyleName(CssStyles.H1);
		Label recommendedLabel = new Label(
				String.format(
						I18nProperties.getString(Strings.RecommendedActions),
						7, 7),
				ContentMode.HTML);
		recommendedLabel.addStyleName(CssStyles.H1);
		Label recommended = null;
		Label notificationTypeLabel = new Label(
				String.format(
						I18nProperties.getString(Strings.NotificationType),
						7, 7),
				ContentMode.HTML);
		notificationTypeLabel.addStyleName(CssStyles.H1);
		Label notificationType = null;
		Label notificationActionLabel = new Label(String.format(	I18nProperties.getString(Strings.NotificationAction), 7, 7), ContentMode.HTML);
		notificationActionLabel.addStyleName(CssStyles.H1);
				Label notificaticationAction = null;
		if (riskAssessmentDto.getRiskAssessment() == RiskAssesment.VERY_HIGH){
			riskCaption =  new Label( String.format(I18nProperties.getString(Strings.VeryHighRisk),50,50), ContentMode.HTML);
			recommended =  new Label( String.format(I18nProperties.getString(Strings.VeryHighRiskRecommendation),50,50), ContentMode.HTML);
			recommended.setStyleName("risk-label");
			recommended.removeStyleName("v-label-undef-w");
			notificationType =  new Label( String.format(I18nProperties.getString(Strings.VeryHighRiskNotificationType),50,50), ContentMode.HTML);
			notificationType.setStyleName("risk-label");
			notificationType.removeStyleName("v-label-undef-w");
			notificaticationAction =  new Label( String.format(I18nProperties.getString(Strings.VeryHighRiskNotificationAction),50,50), ContentMode.HTML);
			notificaticationAction.setStyleName("risk-label");
			notificaticationAction.removeStyleName("v-label-undef-w");
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setWidth(80, Sizeable.Unit.PERCENTAGE);
			verticalLayout.addComponents(recommendedLabel, recommended, notificationTypeLabel, notificationType, notificationActionLabel, notificaticationAction);
			Window window =  VaadinUiUtil.showPopupWindow(
					verticalLayout,
					I18nProperties.getCaption(riskCaption.getValue()));
			window.setWidth(50, Sizeable.Unit.PERCENTAGE);
						window.setStyleName("very-high-risk-assessment");
		}else if (riskAssessmentDto.getRiskAssessment() == RiskAssesment.HIGH){
			riskCaption =  new Label( String.format(I18nProperties.getString(Strings.HighRisk),50,50), ContentMode.HTML);
			recommended =  new Label( String.format(I18nProperties.getString(Strings.HighRiskRecommendation),50,50), ContentMode.HTML);
			recommended.setStyleName("risk-label");
			recommended.removeStyleName("v-label-undef-w");
			notificationType =  new Label( String.format(I18nProperties.getString(Strings.HighRiskNotificationType),50,50), ContentMode.HTML);
			notificationType.setStyleName("risk-label");
			notificationType.removeStyleName("v-label-undef-w");
			notificaticationAction =  new Label( String.format(I18nProperties.getString(Strings.HighRiskNotificationAction),50,50), ContentMode.HTML);
			notificaticationAction.setStyleName("risk-label");
			notificaticationAction.removeStyleName("v-label-undef-w");
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setWidth(80, Sizeable.Unit.PERCENTAGE);
			verticalLayout.addComponents(recommendedLabel, recommended, notificationTypeLabel, notificationType, notificationActionLabel, notificaticationAction);
			Window window =  VaadinUiUtil.showPopupWindow(
					verticalLayout,
					I18nProperties.getCaption(riskCaption.getValue()));
			window.setWidth(50, Sizeable.Unit.PERCENTAGE);
			window.setStyleName("high-risk-assessment");
		} else if (riskAssessmentDto.getRiskAssessment() == RiskAssesment.MEDIUM){
			riskCaption =  new Label( String.format(I18nProperties.getString(Strings.ModerateRisk),50,50), ContentMode.HTML);
			recommended =  new Label( String.format(I18nProperties.getString(Strings.ModerateRiskRecommendation),50,50), ContentMode.HTML);
			recommended.setStyleName("risk-label");
			recommended.removeStyleName("v-label-undef-w");
			notificationType =  new Label( String.format(I18nProperties.getString(Strings.ModerateRiskNotifcationType),50,50), ContentMode.HTML);
			notificationType.setStyleName("risk-label");
			notificationType.removeStyleName("v-label-undef-w");
			notificaticationAction =  new Label( String.format(I18nProperties.getString(Strings.ModerateRiskNotificationAction),50,50), ContentMode.HTML);
			notificaticationAction.setStyleName("risk-label");
			notificaticationAction.removeStyleName("v-label-undef-w");
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setWidth(80, Sizeable.Unit.PERCENTAGE);
			verticalLayout.addComponents(recommendedLabel, recommended, notificationTypeLabel, notificationType, notificationActionLabel, notificaticationAction);
			Window window =  VaadinUiUtil.showPopupWindow(
					verticalLayout,
					I18nProperties.getCaption(riskCaption.getValue()));
			window.setWidth(50, Sizeable.Unit.PERCENTAGE);
			window.setStyleName("moderate-risk-assessment");
		} else if (riskAssessmentDto.getRiskAssessment() == RiskAssesment.LOW){
			riskCaption =  new Label( String.format(I18nProperties.getString(Strings.LowRisk),50,50), ContentMode.HTML);
			recommended =  new Label( String.format(I18nProperties.getString(Strings.LowRiskReccomendation),50,50), ContentMode.HTML);
			recommended.setStyleName("risk-label");
			recommended.removeStyleName("v-label-undef-w");
			notificationType =  new Label( String.format(I18nProperties.getString(Strings.LowRiskNotificationTYpe),50,50), ContentMode.HTML);
			notificationType.setStyleName("risk-label");
			notificationType.removeStyleName("v-label-undef-w");
			notificaticationAction =  new Label( String.format(I18nProperties.getString(Strings.LowRiskNotificationAction),50,50), ContentMode.HTML);
			notificaticationAction.setStyleName("risk-label");
			notificaticationAction.removeStyleName("v-label-undef-w");
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setWidth(80, Sizeable.Unit.PERCENTAGE);
			verticalLayout.addComponents(recommendedLabel, recommended, notificationTypeLabel, notificationType, notificationActionLabel, notificaticationAction);
			Window window =  VaadinUiUtil.showPopupWindow(
					verticalLayout,
					I18nProperties.getCaption(riskCaption.getValue()));
			window.setWidth(50, Sizeable.Unit.PERCENTAGE);
			window.setStyleName("low-risk-assessment");
		}
	}


	public CommitDiscardWrapperComponent<EbsAlertDataForm> getEbsCreateAlertComponent(final String ebsUuid,
																						 boolean isEditAllowed) {
		EbsDto ebs = findEbs(ebsUuid);
		EbsAlertDto ebsAlertDto = EbsAlertDto.build(ebs.toReference());
		EbsAlertDataForm alertDataForm = new EbsAlertDataForm(
				ebs,
				EbsDto.class,
				ebs.isPseudonymized(),
				ebs.isInJurisdiction(),
				isEditAllowed);
		alertDataForm.setValue(ebsAlertDto);
		final CommitDiscardWrapperComponent<EbsAlertDataForm> editView = new CommitDiscardWrapperComponent<EbsAlertDataForm>(
				alertDataForm,
				alertDataForm.getFieldGroup());
		editView.getDiscardButton().setCaption("Cancel");
		editView.addCommitListener(() -> {
			ebs.setAlert(alertDataForm.getValue());
			ebsAlertDto.setAlertUsed(alertDataForm.getValue().getAlertUsed());
			FacadeProvider.getAlertFacade().saveAlert(ebsAlertDto);
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
		});

		return editView;
	}

	private EbsDto findEvent(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}

	private String getDeleteConfirmationDetails(List<String> eventUuids) {
//		boolean hasPendingRequest = FacadeProvider.getSormasToSormasEbsFacade().hasPendingRequest(eventUuids);
//
//		return hasPendingRequest ? "<br/>" + I18nProperties.getString(Strings.messageDeleteWithPendingShareRequest) + "<br/>" : "";

		return "";
	}

	private Consumer<List<EbsIndexDto>> bulkOperationCallback(EbsGrid eventGrid, Window popupWindow) {
		return remainingEvents -> {
			if (popupWindow != null) {
				popupWindow.close();
			}

			eventGrid.reload();
			if (CollectionUtils.isNotEmpty(remainingEvents)) {
				eventGrid.asMultiSelect().selectItems(remainingEvents.toArray(new EbsIndexDto[0]));
			} else {
				navigateToIndex();
			}
		};
	}

	public void archiveAllSelectedItems(Collection<EbsIndexDto> selectedRows, EbsGrid eventGrid) {
		ControllerProvider.getArchiveController()
				.archiveSelectedItems(selectedRows, ArchiveHandlers.forEvent(), bulkOperationCallback(eventGrid, null));
	}


	public CommitDiscardWrapperComponent<EbsDataForm> getEbsDataEditComponent(final String eventUuid, Consumer<EbsSourceType> saveCallback) {

		EbsDto event = findEvent(eventUuid);
		DeletionInfoDto automaticDeletionInfoDto = FacadeProvider.getEbsFacade().getAutomaticDeletionInfo(eventUuid);
		DeletionInfoDto manuallyDeletionInfoDto = FacadeProvider.getEbsFacade().getManuallyDeletionInfo(eventUuid);

		EbsDataForm eventEditForm = new EbsDataForm(event,false, event.isPseudonymized(), event.isInJurisdiction());
		eventEditForm.setValue(event);
		final CommitDiscardWrapperComponent<EbsDataForm> editView =
				new CommitDiscardWrapperComponent<EbsDataForm>(eventEditForm, true, eventEditForm.getFieldGroup());

		editView.getButtonsPanel()
				.addComponentAsFirst(new DeletionLabel(automaticDeletionInfoDto, manuallyDeletionInfoDto, event.isDeleted(), EbsDto.I18N_PREFIX));

		if (event.isDeleted()) {
			editView.getWrappedComponent().getField(EbsDto.DELETION_REASON).setVisible(true);
			if (editView.getWrappedComponent().getField(EbsDto.DELETION_REASON).getValue() == DeletionReason.OTHER_REASON) {
				editView.getWrappedComponent().getField(EbsDto.OTHER_DELETION_REASON).setVisible(true);
			}
		}

		editView.addCommitListener(() -> {
			if (!eventEditForm.getFieldGroup().isModified()) {
				EbsDto eventDto = eventEditForm.getValue();

				final UserDto user = UserProvider.getCurrent().getUser();
				final RegionReferenceDto userRegion = user.getRegion();
				final DistrictReferenceDto userDistrict = user.getDistrict();
				final RegionReferenceDto epEventRegion = eventDto.getEbsLocation().getRegion();
				final DistrictReferenceDto epEventDistrict = eventDto.getEbsLocation().getDistrict();
				final Boolean eventOutsideJurisdiction =
						(userRegion != null && !userRegion.equals(epEventRegion) || userDistrict != null && !userDistrict.equals(epEventDistrict));

				if (eventOutsideJurisdiction) {
					VaadinUiUtil.showConfirmationPopup(
							I18nProperties.getString(Strings.headingEventJurisdictionUpdated),
							new Label(I18nProperties.getString(Strings.messageEventJurisdictionUpdated)),
							I18nProperties.getString(Strings.yes),
							I18nProperties.getString(Strings.no),
							500,
							confirmed -> {
								if (confirmed) {
									saveEbs(saveCallback, eventDto);
								}
							});
				} else {
					saveEbs(saveCallback, eventDto);
					navigateToTriaging(eventUuid,false);
				}
			}
		});

		final String uuid = event.getUuid();
//		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_DELETE)) {
//			editView.addDeleteWithReasonOrRestoreListener((deleteDetails) -> {
//					VaadinUiUtil.showSimplePopupWindow(
//							I18nProperties.getString(Strings.headingEventNotDeleted),
//							I18nProperties.getString(Strings.messageEventsNotDeletedLinkedEntitiesReason));
//				UI.getCurrent().getNavigator().navigateTo(EventsView.VIEW_NAME);
//			},
//					getDeleteConfirmationDetails(Collections.singletonList(eventUuid)), (deleteDetails) -> {
//				FacadeProvider.getEbsFacade().restore(uuid);
//				UI.getCurrent().getNavigator().navigateTo(EBSView.VIEW_NAME);
//			},
//					I18nProperties.getString(Strings.entityEvent), uuid, FacadeProvider.getEbsFacade());
//		}

		// Initialize 'Archive' button
//		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_ARCHIVE)) {
//			ControllerProvider.getArchiveController().addArchivingButton(event, ArchiveHandlers.forEbs(), editView, () -> {
//				navigateToData(uuid);
//			});
//		}
		editView.getDiscardButton().setCaption("Cancel");
		editView.restrictEditableComponentsOnEditView(
				UserRight.EVENT_EDIT,
				null,
				UserRight.EVENT_DELETE,
				UserRight.EVENT_ARCHIVE,
				FacadeProvider.getEbsFacade().getEditPermissionType(eventUuid),
				event.isInJurisdiction());

		return editView;
	}

	private void saveEbs(Consumer<EbsSourceType> saveCallback, EbsDto ebsDto) {
		ebsDto = FacadeProvider.getEbsFacade().save(ebsDto);
		Notification.show(I18nProperties.getString(Strings.messageEventSaved), Type.WARNING_MESSAGE);
		SormasUI.refreshView();
	}


	public EbsDto create() {
		CommitDiscardWrapperComponent<EbsDataForm> ebsCreateComponent = getEbsCreateComponent();
		EbsDto ebsDto = ebsCreateComponent.getWrappedComponent().getValue();
		VaadinUiUtil.showModalPopupWindow(ebsCreateComponent, I18nProperties.getString(Strings.headingCreateNewEvent));
		return ebsDto;
	}

	public boolean isSignalVerified(String ebsUuid) {
		EbsDto ebs = findEbs(ebsUuid);
        return ebs.getSignalVerification().getVerified() == YesNo.NO || ebs.getSignalVerification().getVerified() == null ;
    }
	public boolean isTriagingDiscard(String ebsUuid) {
		EbsDto ebs = findEbs(ebsUuid);
        return ebs.getTriaging().getTriagingDecision() == EbsTriagingDecision.DISCARD;
    }

	public RiskAssessmentDto createRiskAssessmentComponent(final String ebsUuid,
														   boolean isEditAllowed){
		CommitDiscardWrapperComponent<RiskAssessmentDataForm> createRiskAssessmentComponent = getEbsCreateRiskAssessmentComponent(ebsUuid, isEditAllowed);
		RiskAssessmentDto riskAssessmentDto = createRiskAssessmentComponent.getWrappedComponent().getValue();
		if (isSignalVerified(ebsUuid)){
			return null;
		}
		VaadinUiUtil.showModalPopupWindow(createRiskAssessmentComponent, I18nProperties.getString(Strings.headingCreateNewAssessment));
		return riskAssessmentDto;
	}
	public EbsAlertDto createAlertComponent(final String ebsUuid,
														   boolean isEditAllowed){
		CommitDiscardWrapperComponent<EbsAlertDataForm> createAlertComponent = getEbsCreateAlertComponent(ebsUuid, isEditAllowed);
		EbsAlertDto ebsAlertDto = createAlertComponent.getWrappedComponent().getValue();
		if (isSignalVerified(ebsUuid)){
			return null;
		}
		VaadinUiUtil.showModalPopupWindow(createAlertComponent, I18nProperties.getString(Strings.headingCreateNewEvent));
		return ebsAlertDto;
	}

	public TitleLayout getEbsViewTitleLayout(String uuid) {
		EbsDto ebs = findEbs(uuid);

		TitleLayout titleLayout = new TitleLayout();

		String shortUuid = DataHelper.getShortUuid(ebs.getUuid());
		String ebsTitle = ebs.getSourceName();
		String mainRowText = StringUtils.isNotBlank(ebsTitle) ? ebsTitle + " (" + shortUuid + ")" : shortUuid;
		titleLayout.addMainRow(mainRowText);

		return titleLayout;
	}
}
