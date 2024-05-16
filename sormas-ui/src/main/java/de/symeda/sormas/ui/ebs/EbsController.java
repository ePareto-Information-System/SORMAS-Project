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

import com.google.common.base.Functions;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.deletionconfiguration.DeletionInfoDto;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventIndexDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.events.*;
import de.symeda.sormas.ui.sixtydayfollowup.SixtyDayForm;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.ui.utils.components.automaticdeletion.DeletionLabel;
import de.symeda.sormas.ui.utils.components.page.title.TitleLayout;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class EbsController {

	public void registerViews(Navigator navigator) {
		navigator.addView(EBSView.VIEW_NAME, EBSView.class);
		navigator.addView(EbsDataView.VIEW_NAME, EbsDataView.class);
		navigator.addView(TriagingView.VIEW_NAME,TriagingView.class);
		navigator.addView(SignalVerificationView.VIEW_NAME,SignalVerificationView.class);
		navigator.addView(RiskAssessmentView.VIEW_NAME,RiskAssessmentView.class);
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

//	public void setUriFragmentParameter(String ebsUuid) {
//
//		String fragmentParameter;
//		if (ebsUuid == null || ebsUuid.isEmpty()) {
//			fragmentParameter = "";
//		} else {
//			fragmentParameter = ebsUuid;
//		}
//
//		Page page = SormasUI.get().getPage();
//		page.setUriFragment("!" + EBSView.VIEW_NAME + "/" + fragmentParameter, false);
//	}

	private EbsDto findEbs(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}



//	private String getDeleteConfirmationDetails(List<String> ebsUuids) {
//		boolean hasPendingRequest = FacadeProvider.getSormasToSormasEbsFacade().hasPendingRequest(ebsUuids);
//
//		return hasPendingRequest ? "<br/>" + I18nProperties.getString(Strings.messageDeleteWithPendingShareRequest) + "<br/>" : "";
//	}

	public EbsDto createNewEvent(Disease disease) {
		return EbsDto.build(FacadeProvider.getCountryFacade().getServerCountry(), UserProvider.getCurrent().getUser());
	}

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent() {
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		ebsCreateForm.setValue(createNewEvent(null));
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<EbsDataForm>(
//				ebsCreateForm,
//				UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//				ebsCreateForm.getFieldGroup());
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.WARNING_MESSAGE);
//
//					navigateToData(dto.getUuid());
//			}
//		});
//
//		return editView;
//	}

	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent() {

		EbsDataForm form = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
		form.setValue(createNewEvent(null));

		final CommitDiscardWrapperComponent<EbsDataForm> component =
				new CommitDiscardWrapperComponent<>(form, UserProvider.getCurrent().hasAllUserRights(UserRight.EVENT_CREATE), form.getFieldGroup());

		component.addCommitListener(() -> {
			if (!form.getFieldGroup().isModified()) {
				EbsDto newEvent = form.getValue();


				FacadeProvider.getEbsFacade().save(newEvent);


				EbsReferenceDto newEventRef = new EbsReferenceDto(newEvent.getUuid());
//				FacadeProvider.getEbsFacade().setRiskAssessmentAssociations(newEventRef);
				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.TRAY_NOTIFICATION);
				navigateToData(newEventRef.getUuid());
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

		editView.addCommitListener(() -> {
			ebs.setTriaging(triagingDataForm.getValue());
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
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

		editView.addCommitListener(() -> {
			ebs.setSignalVerification(signalVerificationDataForm.getValue());
			FacadeProvider.getEbsFacade().save(ebs);
			SormasUI.refreshView();
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

		editView.addCommitListener(() -> {
			ebs.setRiskAssessment(riskAssessmentDataForm.getValue());
			riskAssessmentDto.setRiskAssessment(riskAssessmentDataForm.getValue().getRiskAssessment());
			FacadeProvider.getEbsFacade().setRiskAssessmentAssociations(riskAssessmentDto.getEbs());
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

		EbsDataForm eventEditForm = new EbsDataForm(false, event.isPseudonymized(), event.isInJurisdiction());
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
				}
			}
		});

		final String uuid = event.getUuid();
		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_DELETE)) {
			editView.addDeleteWithReasonOrRestoreListener((deleteDetails) -> {
					VaadinUiUtil.showSimplePopupWindow(
							I18nProperties.getString(Strings.headingEventNotDeleted),
							I18nProperties.getString(Strings.messageEventsNotDeletedLinkedEntitiesReason));
				UI.getCurrent().getNavigator().navigateTo(EventsView.VIEW_NAME);
			},
					getDeleteConfirmationDetails(Collections.singletonList(eventUuid)), (deleteDetails) -> {
				FacadeProvider.getEbsFacade().restore(uuid);
				UI.getCurrent().getNavigator().navigateTo(EBSView.VIEW_NAME);
			},
					I18nProperties.getString(Strings.entityEvent), uuid, FacadeProvider.getEbsFacade());
		}

		// Initialize 'Archive' button
		if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_ARCHIVE)) {
			ControllerProvider.getArchiveController().addArchivingButton(event, ArchiveHandlers.forEbs(), editView, () -> {
				navigateToData(uuid);
			});
		}

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

	public RiskAssessmentDto createRiskAssessmentComponent(final String ebsUuid,
														   boolean isEditAllowed){
		CommitDiscardWrapperComponent<RiskAssessmentDataForm> createRiskAssessmentComponent = getEbsCreateRiskAssessmentComponent(ebsUuid, isEditAllowed);
		RiskAssessmentDto riskAssessmentDto = createRiskAssessmentComponent.getWrappedComponent().getValue();
		VaadinUiUtil.showModalPopupWindow(createRiskAssessmentComponent, I18nProperties.getString(Strings.headingCreateNewEvent));
		return riskAssessmentDto;
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
