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
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.ui.utils.components.page.title.TitleLayout;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class EbsController {

	public void registerViews(Navigator navigator) {
		navigator.addView(EBSView.VIEW_NAME, EBSView.class);
		navigator.addView(EbsDataView.VIEW_NAME, EbsDataView.class);
	}








//	public void navigateToIndex() {
//		String navigationState = EBSView.VIEW_NAME;
//		SormasUI.get().getNavigator().navigateTo(navigationState);
//	}

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

//	public void navigateToParticipants(String ebsUuid) {
//		String navigationState = EbsParticipantsView.VIEW_NAME + "/" + ebsUuid;
//		SormasUI.get().getNavigator().navigateTo(navigationState);
//	}

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

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent(CaseReferenceDto caseRef) {
//
//		CaseDataDto caseDataDto = null;
//
//		if (caseRef != null) {
//			caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(caseRef.getUuid());
//		}
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		if (caseRef != null) {
//			ebsCreateForm.setValue(createNewEbs(caseDataDto.getDisease()));
//			ebsCreateForm.getField(EbsDto.DISEASE).setReadOnly(true);
//		} else {
//			ebsCreateForm.setValue(createNewEbs(null));
//		}
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<EbsDataForm>(
//			ebsCreateForm,
//			UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//			ebsCreateForm.getFieldGroup());
//
//		CaseDataDto finalCaseDataDto = caseDataDto;
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.WARNING_MESSAGE);
//
//				if (caseRef != null) {
//					EbsReferenceDto createdEbs = new EbsReferenceDto(dto.getUuid());
//
//					linkCaseToEbs(createdEbs, finalCaseDataDto, caseRef);
//					SormasUI.refreshView();
//				} else if (UserProvider.getCurrent().hasUserRight(UserRight.EVENTPARTICIPANT_VIEW)) {
//					navigateToParticipants(dto.getUuid());
//				} else {
//					navigateToData(dto.getUuid());
//				}
//			}
//		});
//
//		return editView;
//	}

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponentForCaseList(List<CaseReferenceDto> caseRefs) {
//
//		List<CaseDataDto> caseDataDtos =
//			FacadeProvider.getCaseFacade().getByUuids(caseRefs.stream().map(c -> c.getUuid()).collect(Collectors.toList()));
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		ebsCreateForm.setValue(createNewEbs(caseDataDtos.stream().findFirst().get().getDisease()));
//		ebsCreateForm.getField(EbsDto.DISEASE).setReadOnly(true);
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<>(
//			ebsCreateForm,
//			UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//			ebsCreateForm.getFieldGroup());
//
//		List<CaseDataDto> finalCaseDataDtos = caseDataDtos;
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.WARNING_MESSAGE);
//
//				linkCasesToEbs(new EbsReferenceDto(dto.getUuid()), finalCaseDataDtos);
//			}
//		});
//
//		return editView;
//	}

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponentForContactList(
//		List<ContactReferenceDto> contactRefs,
//		Consumer<List<ContactReferenceDto>> callback) {
//
//		List<ContactDto> contactDtos =
//			FacadeProvider.getContactFacade().getByUuids(contactRefs.stream().map(ReferenceDto::getUuid).collect(Collectors.toList()));
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		ebsCreateForm.setValue(createNewEbs(contactDtos.stream().findFirst().get().getDisease()));
//		ebsCreateForm.getField(EbsDto.DISEASE).setReadOnly(true);
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<EbsDataForm>(
//			ebsCreateForm,
//			UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//			ebsCreateForm.getFieldGroup());
//
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.WARNING_MESSAGE);
//
//				linkContactsToEbs(
//					new EbsReferenceDto(dto.getUuid()),
//					contactDtos,
//					remaining -> callback.accept(remaining.stream().map(ContactDto::toReference).collect(Collectors.toList())));
//			}
//		});
//
//		return editView;
//	}

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent(PersonReferenceDto personReference) {
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		ebsCreateForm.setValue(createNewEbs());
//
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<>(
//			ebsCreateForm,
//			UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//			ebsCreateForm.getFieldGroup());
//
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.TRAY_NOTIFICATION);
//
//				EbsReferenceDto createdEbs = new EbsReferenceDto(dto.getUuid());
//
//				createEbsParticipantWithPerson(createdEbs, personReference);
//				SormasUI.refreshView();
//			}
//		});
//
//		return editView;
//	}

//	public CommitDiscardWrapperComponent<EbsDataForm> getEbsCreateComponent(ContactDto contact) {
//
//		EbsDataForm ebsCreateForm = new EbsDataForm(true, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created
//		ebsCreateForm.setValue(createNewEbs(contact.getDisease()));
//		ebsCreateForm.getField(EbsDto.DISEASE).setReadOnly(true);
//
//		final CommitDiscardWrapperComponent<EbsDataForm> editView = new CommitDiscardWrapperComponent<>(
//			ebsCreateForm,
//			UserProvider.getCurrent().hasUserRight(UserRight.EVENT_CREATE),
//			ebsCreateForm.getFieldGroup());
//
//		editView.addCommitListener(() -> {
//			if (!ebsCreateForm.getFieldGroup().isModified()) {
//				EbsDto dto = ebsCreateForm.getValue();
//				FacadeProvider.getEbsFacade().save(dto);
//				Notification.show(I18nProperties.getString(Strings.messageEventCreated), Type.TRAY_NOTIFICATION);
//
//				EbsReferenceDto createdEbs = new EbsReferenceDto(dto.getUuid());
//
//				createEbsParticipantWithContact(createdEbs, contact);
//				SormasUI.refreshView();
//			}
//		});
//
//		return editView;
//	}



//	private String getDeleteConfirmationDetails(List<String> ebsUuids) {
//		boolean hasPendingRequest = FacadeProvider.getSormasToSormasEbsFacade().hasPendingRequest(ebsUuids);
//
//		return hasPendingRequest ? "<br/>" + I18nProperties.getString(Strings.messageDeleteWithPendingShareRequest) + "<br/>" : "";
//	}

	private void saveEbs(Consumer<EbsSourceType> saveCallback, EbsDto ebsDto) {
		ebsDto = FacadeProvider.getEbsFacade().save(ebsDto);
		Notification.show(I18nProperties.getString(Strings.messageEventSaved), Type.WARNING_MESSAGE);
		SormasUI.refreshView();
	}


//	public EbsDto createNewEbs() {
//		return EbsDto.build(FacadeProvider.getCountryFacade().getServerCountry(), UserProvider.getCurrent().getUser());
//	}
//
//	public EbsDto createNewEbs(Disease disease) {
//		return EbsDto.build(FacadeProvider.getCountryFacade().getServerCountry(), UserProvider.getCurrent().getUser(), disease);
//	}

	public TitleLayout getEbsViewTitleLayout(String uuid) {
		EbsDto ebs = findEbs(uuid);

		TitleLayout titleLayout = new TitleLayout();

		if (ebs.getStartDate() != null) {
			String ebsStartDateLabel = ebs.getEndDate() != null
					? DateFormatHelper.buildPeriodString(ebs.getStartDate(), ebs.getEndDate())
					: DateFormatHelper.formatDate(ebs.getStartDate());
			titleLayout.addRow(ebsStartDateLabel);
		}

		String shortUuid = DataHelper.getShortUuid(ebs.getUuid());
		String ebsTitle = ebs.getEbsTitle();
		String mainRowText = StringUtils.isNotBlank(ebsTitle) ? ebsTitle + " (" + shortUuid + ")" : shortUuid;
		titleLayout.addMainRow(mainRowText);

		return titleLayout;
	}
}
