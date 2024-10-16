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
package de.symeda.sormas.ui.samples;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.vaadin.server.Page;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.contact.ContactStatus;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestFacade;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleIndexDto;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.CommitListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class PathogenTestController {
	private Disease caseDisease;

	private final PathogenTestFacade facade = FacadeProvider.getPathogenTestFacade();

	public PathogenTestController() {
	}

	public List<PathogenTestDto> getPathogenTestsBySample(SampleReferenceDto sampleRef) {
		return facade.getAllBySample(sampleRef);
	}

	public void create(SampleReferenceDto sampleRef, int caseSampleCount) {
		SampleDto sampleDto = FacadeProvider.getSampleFacade().getSampleByUuid(sampleRef.getUuid());
		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(sampleDto.getAssociatedCase().getUuid());
		caseDisease = caseDataDto.getDisease();

		if (caseDisease == Disease.MONKEYPOX) {
			if (!Boolean.TRUE.equals(sampleDto.isReceived()) || sampleDto.getReceivedDate() == null) {
				Notification.show("Please Confirm sample is \"Received\" and indicate \"Date Sample Received at Lab\".", Notification.Type.ERROR_MESSAGE);
				return;
			}
		}

		final CommitDiscardWrapperComponent<PathogenTestForm> editView = getPathogenTestCreateComponent(sampleDto, caseSampleCount, null, false);
		VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingCreatePathogenTestResult));
	}


	public CommitDiscardWrapperComponent<PathogenTestForm> getPathogenTestCreateComponent(
			SampleDto sampleDto,
			int caseSampleCount,
			Consumer<PathogenTestDto> onSavedPathogenTest,
			boolean suppressNavigateToCase) {
		PathogenTestForm createForm = new PathogenTestForm(sampleDto, true, caseSampleCount, false, true); // Valid because jurisdiction doesn't matter for entities that are about to be created 
		createForm.setValue(PathogenTestDto.build(sampleDto, UserProvider.getCurrent().getUser()));
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<>(
				createForm,
				UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_CREATE),
				createForm.getFieldGroup());

		editView.addCommitListener(() -> {
			if (!createForm.getFieldGroup().isModified()) {
				PathogenTestDto pathogenTest = createForm.getValue();
				savePathogenTest(pathogenTest, suppressNavigateToCase);

				if (onSavedPathogenTest != null) {
					onSavedPathogenTest.accept(pathogenTest);
				}

				SormasUI.refreshView();
				// savePathogenTest(createForm.getValue(), onSavedPathogenTest, false, null);
				// callback.run();
			}
		});
		return editView;
	}

	public void savePathogenTest(PathogenTestDto dto, boolean suppressNavigateToCase) {
		savePathogenTests(Collections.singletonList(dto), dto.getSample(), suppressNavigateToCase);
	}

	public void savePathogenTests(List<PathogenTestDto> pathogenTests, SampleReferenceDto sampleRef, boolean suppressNavigateToCase) {

		final SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(sampleRef.getUuid());

		final CaseReferenceDto associatedCase = sample.getAssociatedCase();
		final ContactReferenceDto associatedContact = sample.getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = sample.getAssociatedEventParticipant();

		pathogenTests.forEach(p -> {
			if (p.getTestDateTime() == null && p.getTestedDisease() == Disease.MONKEYPOX) {
				LocalDate localDate = LocalDate.now();
				Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				p.setTestDateTime(date);
			}
			p.setSample(sampleRef);
			facade.savePathogenTest(p);
		});

		if (associatedContact != null) {
			handleAssociatedContact(pathogenTests, associatedContact);
		} else if (associatedEventParticipant != null) {
			handleAssociatedEventParticipant(pathogenTests, associatedEventParticipant);
		} else if (associatedCase != null) {
			handleAssociatedCase(pathogenTests, associatedCase, suppressNavigateToCase);
		}

		Notification.show(I18nProperties.getString(Strings.messagePathogenTestsSavedShort), TRAY_NOTIFICATION);
	}

	private void handleAssociatedCase(List<PathogenTestDto> pathogenTests, CaseReferenceDto associatedCase, boolean suppressNavigateToCase) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == case disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != case disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == case disease: Ask user whether to update the sample pathogen test result
		// a.1) Tested disease variant != case disease variant: Ask user to change the case disease variant
		// a.2) Case classification != confirmed: Ask user whether to confirm the case
		// b) Tested disease != case disease: Ask user to create a new case for the tested disease

		CaseDataDto caze = FacadeProvider.getCaseFacade().getCaseDataByUuid(associatedCase.getUuid());

		Map<Disease, List<PathogenTestDto>> testsByDisease = pathogenTests.stream().collect(Collectors.groupingBy(PathogenTestDto::getTestedDisease));
		Optional<PathogenTestDto> positiveWithSameDisease = testsByDisease.getOrDefault(caze.getDisease(), Collections.emptyList())
				.stream()
				.filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE)
				.findFirst();

		Optional<PathogenTestDto> negativeWithSameDisease = testsByDisease.getOrDefault(caze.getDisease(), Collections.emptyList())
				.stream()
				.filter(t -> t.getTestResult() == PathogenTestResultType.NEGATIVE)
				.findFirst();

		if (positiveWithSameDisease.isPresent()) {
			showChangeAssociatedSampleResultDialog(positiveWithSameDisease.get(), (accepted) -> {
				if (accepted) {
					checkForDiseaseVariantUpdate(positiveWithSameDisease.get(), caze, suppressNavigateToCase, this::showConfirmCaseDialog);
				}
			});
		} else if (negativeWithSameDisease.isPresent()) {
			showChangeAssociatedSampleResultDialog(negativeWithSameDisease.get(), null);
		}

		testsByDisease.keySet().stream().filter(disease -> disease != caze.getDisease()).forEach((disease) -> {
			List<PathogenTestDto> tests = testsByDisease.get(disease);

			Optional<PathogenTestDto> positiveWithOtherDisease =
					tests.stream().filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE).findFirst();

			if (positiveWithOtherDisease.isPresent()) {
				List<CaseDataDto> duplicatedCases =
						FacadeProvider.getCaseFacade().getDuplicatesWithPathogenTest(caze.getPerson(), positiveWithOtherDisease.get());
				if (duplicatedCases == null || duplicatedCases.size() == 0) {
					PathogenTestDto positiveTestWithOtherDisease = positiveWithOtherDisease.get();

					showCaseCloningWithNewDiseaseDialog(
							caze,
							positiveTestWithOtherDisease.getTestedDisease(),
							positiveTestWithOtherDisease.getTestedDiseaseDetails(),
							positiveTestWithOtherDisease.getTestedDiseaseVariant(),
							positiveTestWithOtherDisease.getTestedDiseaseVariantDetails());
				}
			}
		});
	}

	public CommitDiscardWrapperComponent<PathogenTestForm> getPathogenTestCreateComponentForRubella(
			SampleDto sampleDto,
			int caseSampleCount,
			Consumer<PathogenTestDto> onSavedPathogenTest,
			boolean suppressNavigateToCase) {
		PathogenTestForm createForm = new PathogenTestForm(sampleDto, true, caseSampleCount, false, true, Disease.MEASLES); // Valid because jurisdiction doesn't matter for entities that are about to be created

		createForm.setValue(PathogenTestDto.build(sampleDto, UserProvider.getCurrent().getUser()), Disease.RUBELLA);
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<>(
				createForm,
				UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_CREATE),
				createForm.getFieldGroup());


		editView.addCommitListener(() -> {
			if (!createForm.getFieldGroup().isModified()) {
				PathogenTestDto pathogenTest = createForm.getValue();
				savePathogenTest(pathogenTest, suppressNavigateToCase);

				if (onSavedPathogenTest != null) {
					onSavedPathogenTest.accept(pathogenTest);
				}

				SormasUI.refreshView();
			}
		});
		return editView;
	}


	public PathogenTestDto savePathogenTest(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			boolean suppressSampleResultUpdatePopup,
			boolean suppressNavigateToCase) {
		PathogenTestDto savedDto = facade.savePathogenTest(dto);
//		facade.savePathogenTest(dto);
		final SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(dto.getSample().getUuid());
		final CaseReferenceDto associatedCase = sample.getAssociatedCase();
		final ContactReferenceDto associatedContact = sample.getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = sample.getAssociatedEventParticipant();
		if (associatedContact != null) {
			handleAssociatedContact(dto, onSavedPathogenTest, associatedContact, suppressSampleResultUpdatePopup);
		} else if (associatedEventParticipant != null) {
			handleAssociatedEventParticipant(dto, onSavedPathogenTest, associatedEventParticipant, suppressSampleResultUpdatePopup);
		} else if (associatedCase != null) {
			handleAssociatedCase(dto, onSavedPathogenTest, associatedCase, suppressSampleResultUpdatePopup, suppressNavigateToCase);
		}
		Notification.show(I18nProperties.getString(Strings.messagePathogenTestSavedShort), TRAY_NOTIFICATION);
		return savedDto;
	}

	private void handleAssociatedCase(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			CaseReferenceDto associatedCase,
			boolean suppressSampleResultUpdatePopup,
			boolean suppressNavigateToCase) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == case disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != case disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == case disease: Ask user whether to update the sample pathogen test result
		// a.1) Tested disease variant != case disease variant: Ask user to change the case disease variant
		// a.2) Case classification != confirmed: Ask user whether to confirm the case
		// b) Tested disease != case disease: Ask user to create a new case for the tested disease

		CaseDataDto caze = FacadeProvider.getCaseFacade().getCaseDataByUuid(associatedCase.getUuid());

		final boolean equalDisease = dto.getTestedDisease() == caze.getDisease();

		Runnable callback = () -> {
			if (equalDisease
					&& PathogenTestResultType.NEGATIVE.equals(dto.getTestResult())
					
					&& !suppressSampleResultUpdatePopup) {
				showChangeAssociatedSampleResultDialog(dto, handleChanges -> {
					if (dto.getTestedDiseaseVariant() != null && !DataHelper.equal(dto.getTestedDiseaseVariant(), caze.getDiseaseVariant())) {
						showCaseUpdateWithNegativeNewDiseaseVariantDialog(
								caze,
								dto.getTestedDiseaseVariant(),
								dto.getTestedDiseaseVariantDetails(),
								() -> {
									//get case to see if there are changes
									showNoCaseDialog(FacadeProvider.getCaseFacade().getByUuid(caze.getUuid()));
								});
					} else {
						showNoCaseDialog(caze);
					}
				});
			} else if (PathogenTestResultType.POSITIVE.equals(dto.getTestResult()) ) {
				if (equalDisease && suppressSampleResultUpdatePopup) {
					checkForDiseaseVariantUpdate(dto, caze, suppressNavigateToCase, this::showConfirmCaseDialog);
				} else if (equalDisease) {
					showChangeAssociatedSampleResultDialog(dto, (accepted) -> {
						if (accepted) {
							checkForDiseaseVariantUpdate(dto, caze, suppressNavigateToCase, this::showConfirmCaseDialog);
						}
					});
				} else {

					showCaseCloningWithNewDiseaseDialog(
							caze,
							dto.getTestedDisease(),
							dto.getTestedDiseaseDetails(),
							dto.getTestedDiseaseVariant(),
							dto.getTestedDiseaseVariantDetails());
//					showCaseCloningWithNewDiseaseDialog(caze, dto.getTestedDisease()
////						dto.getTestedDiseaseDetails(),
////						dto.getTestedDiseaseVariant(),
////						dto.getTestedDiseaseVariantDetails()
//					);
					// if (onActionNeeded != null)
					// 	onActionNeeded.accept(SavePathogenTest_NeededAction.CONFIRM_CASE_CLASSIFICATION, caze);
					// else
					// 	showCaseCloningWithNewDiseaseDialog(
					// 		caze,
					// 		dto.getTestedDisease(),
					// 		dto.getTestedDiseaseDetails(),
					// 		dto.getTestedDiseaseVariant(),
					// 		dto.getTestedDiseaseVariantDetails());
				}
			}
		};

		if (onSavedPathogenTest != null) {
			onSavedPathogenTest.accept(dto, callback);
		} else {
			callback.run();
		}
	}

//		final CommitDiscardWrapperComponent<PathogenTestForm> editView =
//			getPathogenTestEditComponent(pathogenTestUuid, doneCallback, isEditAllowed, isDeleteAllowed);
//
//		Window popupWindow = VaadinUiUtil.createPopupWindow();
//
//		if (isDeleteAllowed) {
//			editView.addDeleteWithReasonOrRestoreListener((deleteDetails) -> {
//				FacadeProvider.getPathogenTestFacade().deletePathogenTest(pathogenTestUuid, deleteDetails);
//				UI.getCurrent().removeWindow(popupWindow);
//				doneCallback.run();
//			}, I18nProperties.getCaption(PathogenTestDto.I18N_PREFIX));
//		}
//		editView.addCommitListener(popupWindow::close);
//		editView.addDiscardListener(popupWindow::close);
//
//		popupWindow.setContent(editView);
//		popupWindow
//			.setCaption(I18nProperties.getString(!isEditAllowed ? Strings.headingViewPathogenTestResult : Strings.headingEditPathogenTestResult));
//		UI.getCurrent().addWindow(popupWindow);
//	}


	public void edit(String pathogenTestUuid, Runnable doneCallback, boolean isEditAllowed, boolean isDeleteAllowed) {
		final CommitDiscardWrapperComponent<PathogenTestForm> editView =
				getPathogenTestEditComponent(pathogenTestUuid, doneCallback, isEditAllowed, isDeleteAllowed);

		Window popupWindow = VaadinUiUtil.createPopupWindow();

		if (isDeleteAllowed) {
			editView.addDeleteWithReasonOrRestoreListener((deleteDetails) -> {
				FacadeProvider.getPathogenTestFacade().deletePathogenTest(pathogenTestUuid, deleteDetails);
				UI.getCurrent().removeWindow(popupWindow);
				doneCallback.run();
			}, I18nProperties.getCaption(PathogenTestDto.I18N_PREFIX));
		}
		editView.addCommitListener(popupWindow::close);
		editView.addDiscardListener(popupWindow::close);

		popupWindow.setContent(editView);
		popupWindow
				.setCaption(I18nProperties.getString(!isEditAllowed ? Strings.headingViewPathogenTestResult : Strings.headingEditPathogenTestResult));
		UI.getCurrent().addWindow(popupWindow);
	}

	public CommitDiscardWrapperComponent<PathogenTestForm> getPathogenTestEditComponent(
			String pathogenTestUuid,
			Runnable doneCallback,
			boolean isEditAllowed,
			boolean isDeleteAllowed) {

		// get fresh data
		PathogenTestDto pathogenTest = facade.getByUuid(pathogenTestUuid);
		SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(pathogenTest.getSample().getUuid());
		PathogenTestForm form = new PathogenTestForm(sample, false, 0, pathogenTest.isPseudonymized(), pathogenTest.isInJurisdiction());
		form.setValue(pathogenTest);

		boolean isEditOrDeleteAllowed = isEditAllowed || isDeleteAllowed;
		final CommitDiscardWrapperComponent<PathogenTestForm> editView =
				new CommitDiscardWrapperComponent<>(form, isEditOrDeleteAllowed, form.getFieldGroup());

		if (isEditOrDeleteAllowed) {
			editView.addCommitListener(() -> {
				if (!form.getFieldGroup().isModified()) {
					savePathogenTest(form.getValue(), false);
					doneCallback.run();
					SormasUI.refreshView();
				}
			});

			if (pathogenTest.isDeleted()) {
				editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).setVisible(true);
				if (editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).getValue() == DeletionReason.OTHER_REASON) {
					editView.getWrappedComponent().getField(PathogenTestDto.OTHER_DELETION_REASON).setVisible(true);
				}
			}
			editView.restrictEditableComponentsOnEditView(
					UserRight.SAMPLE_EDIT,
					UserRight.PATHOGEN_TEST_EDIT,
					UserRight.PATHOGEN_TEST_DELETE,
					null,
					pathogenTest.isInJurisdiction());

		}
		editView.getButtonsPanel().setVisible(isEditOrDeleteAllowed);

		return editView;
	}

	public CommitDiscardWrapperComponent<PathogenTestForm> getPathogenTestEditComponent(
			String pathogenTestUuid,
			Runnable doneCallback,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest) {

		// get fresh data
		PathogenTestDto pathogenTest = facade.getByUuid(pathogenTestUuid);
		SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(pathogenTest.getSample().getUuid());
		PathogenTestForm form = new PathogenTestForm(sample, false, 0, pathogenTest.isPseudonymized());
		form.setValue(pathogenTest);

		final CommitDiscardWrapperComponent<PathogenTestForm> editView =
				new CommitDiscardWrapperComponent<>(form, UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_EDIT), form.getFieldGroup());

		editView.addCommitListener(() -> {
			if (!form.getFieldGroup().isModified()) {
				savePathogenTest(form.getValue(), onSavedPathogenTest, false, false);
				//savePathogenTest(form.getValue(), onSavedPathogenTest, false, null);
				doneCallback.run();
				SormasUI.refreshView();
			}
		});

		if (pathogenTest.isDeleted()) {
			editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).setVisible(true);
			if (editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).getValue() == DeletionReason.OTHER_REASON) {
				editView.getWrappedComponent().getField(PathogenTestDto.OTHER_DELETION_REASON).setVisible(true);
			}
		}

		return editView;
	}

//	public CommitDiscardWrapperComponent<PathogenTestForm> getPathogenTestEditComponent(
//		String pathogenTestUuid,
//		Runnable doneCallback,
//		boolean isEditAllowed,
//		boolean isDeleteAllowed) {
//
//		// get fresh data
//		PathogenTestDto pathogenTest = facade.getByUuid(pathogenTestUuid);
//		SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(pathogenTest.getSample().getUuid());
//		PathogenTestForm form = new PathogenTestForm(sample, false, 0, pathogenTest.isPseudonymized(), pathogenTest.isInJurisdiction());
//		form.setValue(pathogenTest);
//
//		boolean isEditOrDeleteAllowed = isEditAllowed || isDeleteAllowed;
//		final CommitDiscardWrapperComponent<PathogenTestForm> editView =
//			new CommitDiscardWrapperComponent<>(form, isEditOrDeleteAllowed, form.getFieldGroup());
//
//		editView.addCommitListener(() -> {
//			if (!form.getFieldGroup().isModified()) {
//				savePathogenTest(form.getValue(), onSavedPathogenTest, false, false);
//				//savePathogenTest(form.getValue(), onSavedPathogenTest, false, null);
//				doneCallback.run();
//				SormasUI.refreshView();
//			}
//		});
//		if (isEditOrDeleteAllowed) { //1.87.0
//			editView.addCommitListener(() -> {
//				if (!form.getFieldGroup().isModified()) {
//					savePathogenTest(form.getValue(), false);
//					doneCallback.run();
//					SormasUI.refreshView();
//				}
//			}); //end 1.87.0
//
//			if (pathogenTest.isDeleted()) {
//				editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).setVisible(true);
//				if (editView.getWrappedComponent().getField(PathogenTestDto.DELETION_REASON).getValue() == DeletionReason.OTHER_REASON) {
//					editView.getWrappedComponent().getField(PathogenTestDto.OTHER_DELETION_REASON).setVisible(true);
//				}
//			}
//			editView.restrictEditableComponentsOnEditView(
//				UserRight.SAMPLE_EDIT,
//				UserRight.PATHOGEN_TEST_EDIT,
//				UserRight.PATHOGEN_TEST_DELETE,
//				null,
//				pathogenTest.isInJurisdiction());
//
//		}
//		editView.getButtonsPanel().setVisible(isEditOrDeleteAllowed);
//
//		return editView;
//	}


	private void handleAssociatedEventParticipant(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			EventParticipantReferenceDto associatedEventParticipant,
			boolean suppressSampleResultUpdatePopup) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.EVENTPARTICIPANT_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == event disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != event disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == event disease: Ask user to create a case linked to the event participant
		// a.1) If a case is created, update the sample pathogen test result
		// a.2) If no case is created (or there already is an existing case), ask user whether to update the sample pathogen test result
		// b) Tested disease != event disease: Ask user to create a case for the event participant person
		// b.1) If the event has no disease and a case is created, update the sample pathogen test result
		// b.2) If the event has no disease and no case is created, ask user whether to update the sample pathogen test result

		final EventParticipantDto eventParticipant =
				FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(associatedEventParticipant.getUuid());
		final Disease eventDisease = FacadeProvider.getEventFacade().getEventByUuid(eventParticipant.getEvent().getUuid(), false).getDisease();
		final boolean equalDisease = eventDisease != null && eventDisease.equals(dto.getTestedDisease());

		Runnable callback = () -> {
			if (equalDisease
					&& PathogenTestResultType.NEGATIVE.equals(dto.getTestResult())
					&& !suppressSampleResultUpdatePopup) {
				showChangeAssociatedSampleResultDialog(dto, null);
			} else if (PathogenTestResultType.POSITIVE.equals(dto.getTestResult())) {
				if (equalDisease) {
					if (eventParticipant.getResultingCase() == null) {
						showConvertEventParticipantToCaseDialog(eventParticipant, dto.getTestedDisease(), caseCreated -> {
							handleCaseCreationFromContactOrEventParticipant(caseCreated, dto);
						});
					} else if (!suppressSampleResultUpdatePopup) {
						showChangeAssociatedSampleResultDialog(dto, null);
					}
				} else {
					showConvertEventParticipantToCaseDialog(eventParticipant, dto.getTestedDisease(), caseCreated -> {
						if (eventDisease == null) {
							handleCaseCreationFromContactOrEventParticipant(caseCreated, dto);
						}
					});
				}
			}
		};

		if (onSavedPathogenTest != null) {
			onSavedPathogenTest.accept(dto, callback);
		} else {
			callback.run();
		}
	}

	private void handleCaseCreationFromContactOrEventParticipant(boolean caseCreated, PathogenTestDto pathogenTest) {
		if (caseCreated) {
			SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(pathogenTest.getSample().getUuid());
			if (sample.getPathogenTestResult() != pathogenTest.getTestResult()) {
				sample.setPathogenTestResult(pathogenTest.getTestResult());
				FacadeProvider.getSampleFacade().saveSample(sample);
			}
		} else {
			showChangeAssociatedSampleResultDialog(pathogenTest, null);
		}
	}


	private void showChangeAssociatedSampleResultDialog(PathogenTestDto dto, Consumer<Boolean> callback) {
		if (dto.getTestResult() != FacadeProvider.getSampleFacade().getSampleByUuid(dto.getSample().getUuid()).getPathogenTestResult()) {
			ControllerProvider.getSampleController()
					.showChangePathogenTestResultWindow(null, dto.getSample().getUuid(), dto.getTestResult(), dto, callback);
		} else if (callback != null) {
			callback.accept(true);
		}
	}



	public void showConvertContactToCaseDialog(ContactDto contact, Consumer<Boolean> callback) {
		VaadinUiUtil.showConfirmationPopup(
				I18nProperties.getCaption(Captions.convertContactToCase),
				new Label(I18nProperties.getString(Strings.messageConvertContactToCase)),
				I18nProperties.getString(Strings.yes),
				I18nProperties.getString(Strings.no),
				800,
				confirmed -> {
					if (confirmed) {
						ControllerProvider.getCaseController().createFromContact(contact);
					}
					callback.accept(confirmed);
				});
	}

	private void handleAssociatedContact(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			ContactReferenceDto associatedContact,
			boolean suppressSampleResultUpdatePopup) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == contact disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != contact disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == contact disease: Ask user to convert the contact to a case
		// a.1) If contact is converted, update the sample pathogen test result
		// a.2) If contact is not converted (or there already is a resulting case), ask user whether to update the sample pathogen test result
		// b) Tested disease != contact disease: Ask user to create a new case for the tested disease

		final ContactDto contact = FacadeProvider.getContactFacade().getByUuid(associatedContact.getUuid());
		final boolean equalDisease = dto.getTestedDisease() == contact.getDisease();

		Runnable callback = () -> {
			if (!UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_EDIT)) {
				return;
			}

			if (equalDisease
					&& PathogenTestResultType.NEGATIVE.equals(dto.getTestResult())
					
					&& !suppressSampleResultUpdatePopup) {
				showChangeAssociatedSampleResultDialog(dto, null);
			} else if (PathogenTestResultType.POSITIVE.equals(dto.getTestResult()) ) {
				if (equalDisease) {
					if (contact.getResultingCase() == null && !ContactStatus.CONVERTED.equals(contact.getContactStatus())) {
						showConvertContactToCaseDialog(contact, converted -> handleCaseCreationFromContactOrEventParticipant(converted, dto));
					} else if (!suppressSampleResultUpdatePopup) {
						showChangeAssociatedSampleResultDialog(dto, null);
					}
				} else {
					showCreateContactCaseDialog(contact, dto.getTestedDisease());
				}
			}
		};

		if (onSavedPathogenTest != null) {
			onSavedPathogenTest.accept(dto, callback);
		} else {
			callback.run();
		}
	}

	public void showCreateContactCaseDialog(ContactDto contact, Disease disease) {
		VaadinUiUtil.showConfirmationPopup(
				I18nProperties.getCaption(Captions.contactCreateContactCase),
				new Label(I18nProperties.getString(Strings.messageConvertContactToCaseDifferentDiseases)),
				I18nProperties.getString(Strings.yes),
				I18nProperties.getString(Strings.no),
				800,
				confirmed -> {
					if (confirmed) {
						ControllerProvider.getCaseController().createFromUnrelatedContact(contact, disease);
					}
				});
	}

	public void showBulkTestResultComponent(Collection<? extends SampleIndexDto> selectedSamples, Disease disease) {

		if (selectedSamples.size() == 0) {
			new Notification(
					I18nProperties.getString(Strings.headingNoSamplesSelected),
					I18nProperties.getString(Strings.messageNoSamplesSelected),
					Type.WARNING_MESSAGE,
					false).show(Page.getCurrent());
			return;
		}

		SampleIndexDto firstSelectedSample = selectedSamples.stream().findFirst().orElse(null);
		SampleDto sampleDto = FacadeProvider.getSampleFacade().getSampleByUuid(firstSelectedSample.getUuid());

		// Create a temporary pathogenTest in order to use the CommitDiscardWrapperComponent
		PathogenTestDto bulkResultData = PathogenTestDto.build(sampleDto, UserProvider.getCurrent().getUser());

		//set defaults
		bulkResultData.setTestDateTime(new java.util.Date());
		bulkResultData.setTestedDisease(disease);
		bulkResultData.setTestResultVerified(true);

		PathogenTestForm form = new PathogenTestForm(sampleDto, true, 1, false);
		form.setValue(bulkResultData);
		final CommitDiscardWrapperComponent<PathogenTestForm> editView = new CommitDiscardWrapperComponent<PathogenTestForm>(
				form,
				UserProvider.getCurrent().hasUserRight(UserRight.PATHOGEN_TEST_CREATE),
				form.getFieldGroup());

		Window popupWindow = VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingCreateBulkTests));

		editView.addCommitListener(new CommitListener() {

			@Override
			public void onCommit() {
				PathogenTestDto updatedBulkResultData = form.getValue();
				bulkCreate(selectedSamples, updatedBulkResultData);

				popupWindow.close();
				Notification.show(I18nProperties.getString(Strings.messageTestsCreated), Type.HUMANIZED_MESSAGE);
			}
		});
	}

	public void bulkCreate(
			Collection<? extends SampleIndexDto> selectedSamples,
			PathogenTestDto updatedBulkResultData) {

		Collection<CaseDataDto> casesToClassify = new ArrayList<CaseDataDto>();
		Collection<CaseDataDto> casesToClone = new ArrayList<CaseDataDto>();
		Collection<SampleIndexDto> samplesToUpdate = new ArrayList<SampleIndexDto>();

		for (SampleIndexDto sample : selectedSamples) {

			updatedBulkResultData.setUuid(DataHelper.createUuid());
			updatedBulkResultData.setSample(sample.toReference());

			savePathogenTest(updatedBulkResultData, null, false, (action, caze) -> {
				switch (action) {
					case CONFIRM_CASE_CLASSIFICATION:
						casesToClassify.add(caze);
						break;
					case CLONE_CASE_WITH_NEW_DISEASE:
						casesToClone.add(caze);
						break;
				}
			});

			if (isSampleResultDifferentFromPathogenTest(sample, updatedBulkResultData)) {
				samplesToUpdate.add(sample);
			}
		}

		//showConfirmCaseDialog(casesToClassify);
		showCaseCloningWithNewDiseaseDialog(casesToClone, updatedBulkResultData.getTestedDisease(),
				updatedBulkResultData.getTestedDiseaseDetails(), updatedBulkResultData.getTestedDiseaseVariant(),
				updatedBulkResultData.getTestedDiseaseVariantDetails());

		ControllerProvider.getSampleController().showChangePathogenTestResultWindow(
				null,
				samplesToUpdate.stream().map(sample -> sample.getUuid()).collect(Collectors.toList()),
				updatedBulkResultData.getTestResult(),
				null
		);
	}
    private enum SavePathogenTest_NeededAction {
		CONFIRM_CASE_CLASSIFICATION,
		CLONE_CASE_WITH_NEW_DISEASE,
	}

	public PathogenTestDto savePathogenTest(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			boolean suppressSampleResultUpdatePopup,
			BiConsumer<SavePathogenTest_NeededAction, CaseDataDto> onActionNeeded) {
		PathogenTestDto savedDto = facade.savePathogenTest(dto);
		final SampleDto sample = FacadeProvider.getSampleFacade().getSampleByUuid(dto.getSample().getUuid());
		final CaseReferenceDto associatedCase = sample.getAssociatedCase();
		final ContactReferenceDto associatedContact = sample.getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = sample.getAssociatedEventParticipant();
		if (associatedCase != null) {
			handleAssociatedCase(dto, onSavedPathogenTest, associatedCase, suppressSampleResultUpdatePopup, onActionNeeded);
		}
		if (associatedContact != null) {
			handleAssociatedContact(dto, onSavedPathogenTest, associatedContact, suppressSampleResultUpdatePopup);
		}
		if (associatedEventParticipant != null) {
			handleAssociatedEventParticipant(dto, onSavedPathogenTest, associatedEventParticipant, suppressSampleResultUpdatePopup);
		}
		Notification.show(I18nProperties.getString(Strings.messagePathogenTestSavedShort), TRAY_NOTIFICATION);
		return savedDto;
	}


	public static void showCaseUpdateWithNewDiseaseVariantDialog(
			CaseDataDto existingCaseDto,
			DiseaseVariant diseaseVariant,
			String diseaseVariantDetails,
			Consumer<Boolean> callback) {

		VaadinUiUtil.showConfirmationPopup(
				I18nProperties.getString(Strings.headingUpdateCaseWithNewDiseaseVariant),
				new Label(
						String.format(
								I18nProperties.getString(Strings.messageUpdateCaseWithNewDiseaseVariant),
								existingCaseDto.getDiseaseVariant() == null
										? "[" + I18nProperties.getCaption(Captions.caseNoDiseaseVariant) + "]"
										: existingCaseDto.getDiseaseVariant().toString(),
								diseaseVariant.toString())),
				I18nProperties.getString(Strings.yes),
				I18nProperties.getString(Strings.no),
				800,
				yes -> {
					if (yes) {
						CaseDataDto caseDataByUuid = FacadeProvider.getCaseFacade().getCaseDataByUuid(existingCaseDto.getUuid());
						caseDataByUuid.setDiseaseVariant(diseaseVariant);
						caseDataByUuid.setDiseaseVariantDetails(diseaseVariantDetails);
						FacadeProvider.getCaseFacade().save(caseDataByUuid);
					}
					if (callback != null) {
						callback.accept(yes);
					}
				}).bringToFront();
	}


	public static void showCaseUpdateWithNegativeNewDiseaseVariantDialog(
			CaseDataDto existingCaseDto,
			DiseaseVariant diseaseVariant,
			String diseaseVariantDetails,
			Runnable callback) {

		VaadinUiUtil.showConfirmationPopup(
				I18nProperties.getString(Strings.headingUpdateCaseWithNewDiseaseVariant),
				new Label(I18nProperties.getString(Strings.messageUpdateCaseWithNegativeSampleNewDiseaseVariant)),
				I18nProperties.getString(Strings.yes),
				I18nProperties.getString(Strings.no),
				800,
				e -> {
					if (e) {
						CaseDataDto caseDataByUuid = FacadeProvider.getCaseFacade().getCaseDataByUuid(existingCaseDto.getUuid());
						caseDataByUuid.setDiseaseVariant(diseaseVariant);
						caseDataByUuid.setDiseaseVariantDetails(diseaseVariantDetails);
						FacadeProvider.getCaseFacade().saveCase(caseDataByUuid);
						ControllerProvider.getCaseController().navigateToCase(caseDataByUuid.getUuid());
					}
					if (callback != null) {
						callback.run();
					}
				});
	}




	private void handleAssociatedCase(
			PathogenTestDto dto,
			BiConsumer<PathogenTestDto, Runnable> onSavedPathogenTest,
			CaseReferenceDto associatedCase,
			boolean suppressSampleResultUpdatePopup,
			BiConsumer<SavePathogenTest_NeededAction, CaseDataDto> onActionNeeded) {

		// Negative test result AND test result verified
		// a) Tested disease == case disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != case disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == case disease: Ask user whether to update the sample pathogen test result
		// a.1) Tested disease variant != case disease variant: Ask user to change the case disease variant
		// a.2) Case classification != confirmed: Ask user whether to confirm the case
		// b) Tested disease != case disease: Ask user to create a new case for the tested disease

		CaseDataDto caze = FacadeProvider.getCaseFacade().getCaseDataByUuid(associatedCase.getUuid());

		final boolean equalDisease = dto.getTestedDisease() == caze.getDisease();

		Runnable callback = () -> {
			if (equalDisease
					&& PathogenTestResultType.NEGATIVE.equals(dto.getTestResult())
					
					&& !suppressSampleResultUpdatePopup) {
				showChangeAssociatedSampleResultDialog(dto, null);
			} else if (PathogenTestResultType.POSITIVE.equals(dto.getTestResult()) ) {
				if (equalDisease && suppressSampleResultUpdatePopup) {
					checkForDiseaseVariantUpdate(dto, caze, this::showConfirmCaseDialog);
				} else if (equalDisease) {
					showChangeAssociatedSampleResultDialog(dto, (accepted) -> {
						if (accepted) {
							checkForDiseaseVariantUpdate(dto, caze, this::showConfirmCaseDialog);
						}
					});
				} else {
					if (onActionNeeded != null)
						onActionNeeded.accept(SavePathogenTest_NeededAction.CONFIRM_CASE_CLASSIFICATION, caze);
					else
						showCaseCloningWithNewDiseaseDialog(
								caze,
								dto.getTestedDisease(),
								dto.getTestedDiseaseDetails(),
								dto.getTestedDiseaseVariant(),
								dto.getTestedDiseaseVariantDetails());
				}
			}
		};

		if (onSavedPathogenTest != null) {
			onSavedPathogenTest.accept(dto, callback);
		} else {
			callback.run();
		}
	}
	
	


	private void handleAssociatedContact(List<PathogenTestDto> pathogenTests, ContactReferenceDto associatedContact) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == contact disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != contact disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == contact disease: Ask user to convert the contact to a case
		// a.1) If contact is converted, update the sample pathogen test result
		// a.2) If contact is not converted (or there already is a resulting case), ask user whether to update the sample pathogen test result
		// b) Tested disease != contact disease: Ask user to create a new case for the tested disease

		final ContactDto contact = FacadeProvider.getContactFacade().getByUuid(associatedContact.getUuid());

		Map<Disease, List<PathogenTestDto>> testsByDisease = pathogenTests.stream().collect(Collectors.groupingBy(PathogenTestDto::getTestedDisease));
		Optional<PathogenTestDto> positiveWithSameDisease = testsByDisease.getOrDefault(contact.getDisease(), Collections.emptyList())
			.stream()
			.filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE)
			.findFirst();

		Optional<PathogenTestDto> negativeWithSameDisease = testsByDisease.getOrDefault(contact.getDisease(), Collections.emptyList())
			.stream()
			.filter(t -> t.getTestResult() == PathogenTestResultType.NEGATIVE)
			.findFirst();

		if (positiveWithSameDisease.isPresent()) {
			if (contact.getResultingCase() == null && !ContactStatus.CONVERTED.equals(contact.getContactStatus())) {
				showConvertContactToCaseDialog(
					contact,
					converted -> handleCaseCreationFromContactOrEventParticipant(converted, positiveWithSameDisease.get()));
			} else {
				showChangeAssociatedSampleResultDialog(positiveWithSameDisease.get(), null);
			}
		} else if (negativeWithSameDisease.isPresent()) {
			showChangeAssociatedSampleResultDialog(negativeWithSameDisease.get(), null);
		}

		testsByDisease.keySet().stream().filter(disease -> disease != contact.getDisease()).forEach((disease) -> {
			List<PathogenTestDto> tests = testsByDisease.get(disease);

			Optional<PathogenTestDto> positiveWithOtherDisease =
				tests.stream().filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE).findFirst();
			if (positiveWithOtherDisease.isPresent()) {
				List<CaseDataDto> duplicatedCases =
					FacadeProvider.getCaseFacade().getDuplicatesWithPathogenTest(contact.getPerson(), positiveWithOtherDisease.get());
				if (CollectionUtils.isEmpty(duplicatedCases)) {
					showCreateContactCaseDialog(contact, positiveWithOtherDisease.get().getTestedDisease());
				}
			}
		});
	}

	private void handleAssociatedEventParticipant(List<PathogenTestDto> pathogenTests, EventParticipantReferenceDto associatedEventParticipant) {

		if (!UserProvider.getCurrent().hasUserRight(UserRight.EVENTPARTICIPANT_EDIT)) {
			return;
		}

		// Negative test result AND test result verified
		// a) Tested disease == event disease AND test result != sample pathogen test result: Ask user whether to update the sample pathogen test result
		// b) Tested disease != event disease: Do nothing

		// Positive test result AND test result verified
		// a) Tested disease == event disease: Ask user to create a case linked to the event participant
		// a.1) If a case is created, update the sample pathogen test result
		// a.2) If no case is created (or there already is an existing case), ask user whether to update the sample pathogen test result
		// b) Tested disease != event disease: Ask user to create a case for the event participant person
		// b.1) If the event has no disease and a case is created, update the sample pathogen test result
		// b.2) If the event has no disease and no case is created, ask user whether to update the sample pathogen test result

		final EventParticipantDto eventParticipant =
			FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(associatedEventParticipant.getUuid());
		final Disease eventDisease = FacadeProvider.getEventFacade().getEventByUuid(eventParticipant.getEvent().getUuid(), false).getDisease();

		Map<Disease, List<PathogenTestDto>> testsByDisease = pathogenTests.stream().collect(Collectors.groupingBy(PathogenTestDto::getTestedDisease));
		Optional<PathogenTestDto> positiveWithSameDisease = testsByDisease.getOrDefault(eventDisease, Collections.emptyList())
			.stream()
			.filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE)
			.findFirst();

		Optional<PathogenTestDto> negativeWithSameDisease = testsByDisease.getOrDefault(eventDisease, Collections.emptyList())
			.stream()
			.filter(t -> t.getTestResult() == PathogenTestResultType.NEGATIVE)
			.findFirst();

		if (positiveWithSameDisease.isPresent()) {
			if (eventParticipant.getResultingCase() == null) {
				showConvertEventParticipantToCaseDialog(eventParticipant, positiveWithSameDisease.get().getTestedDisease(), caseCreated -> {
					handleCaseCreationFromContactOrEventParticipant(caseCreated, positiveWithSameDisease.get());
				});
			} else {
				showChangeAssociatedSampleResultDialog(positiveWithSameDisease.get(), null);
			}
		} else if (negativeWithSameDisease.isPresent()) {
			showChangeAssociatedSampleResultDialog(negativeWithSameDisease.get(), null);
		}

		testsByDisease.keySet().stream().filter(disease -> disease != eventDisease).forEach((disease) -> {
			List<PathogenTestDto> tests = testsByDisease.get(disease);

			Optional<PathogenTestDto> positiveWithOtherDisease =
				tests.stream().filter(t -> t.getTestResult() == PathogenTestResultType.POSITIVE).findFirst();
			if (positiveWithOtherDisease.isPresent()) {
				List<CaseDataDto> duplicatedCases = FacadeProvider.getCaseFacade()
					.getDuplicatesWithPathogenTest(eventParticipant.getPerson().toReference(), positiveWithOtherDisease.get());
				if (CollectionUtils.isEmpty(duplicatedCases)) {
					showConvertEventParticipantToCaseDialog(eventParticipant, positiveWithOtherDisease.get().getTestedDisease(), caseCreated -> {
						if (eventDisease == null) {
							handleCaseCreationFromContactOrEventParticipant(caseCreated, positiveWithOtherDisease.get());
						}
					});
				}
			}
		});
	}

	private void checkForDiseaseVariantUpdate(
		PathogenTestDto test,
		CaseDataDto caze,
		boolean suppressNavigateToCase,
		Consumer<CaseDataDto> callback) {
		if (test.getTestedDiseaseVariant() != null
			&& !DataHelper.equal(test.getTestedDiseaseVariant(), caze.getDiseaseVariant())
			&& isNotYetRelatedDiseaseVariant(test)) {
			showCaseUpdateWithNewDiseaseVariantDialog(caze, test.getTestedDiseaseVariant(), test.getTestedDiseaseVariantDetails(), yes -> {
				if (yes && !suppressNavigateToCase) {
					ControllerProvider.getCaseController().navigateToCase(caze.getUuid());
				} else if (yes) {
					// Refresh view because it might already show the case
					SormasUI.refreshView();
				}
				// Retrieve the case again because it might have changed
				callback.accept(FacadeProvider.getCaseFacade().getByUuid(caze.getUuid()));
			});
		} else {
			callback.accept(caze);
		}
	}
	
	private void checkForDiseaseVariantUpdate(PathogenTestDto test, CaseDataDto caze, Consumer<CaseDataDto> callback) {
		if (test.getTestedDiseaseVariant() != null && !DataHelper.equal(test.getTestedDiseaseVariant(), caze.getDiseaseVariant())) {
			showCaseUpdateWithNewDiseaseVariantDialog(caze, test.getTestedDiseaseVariant(), test.getTestedDiseaseVariantDetails(), yes -> {
				if (yes) {
					ControllerProvider.getCaseController().navigateToCase(caze.getUuid());
				}
				// Retrieve the case again because it might have changed
				callback.accept(FacadeProvider.getCaseFacade().getByUuid(caze.getUuid()));
			});
		} else {
			callback.accept(caze);
		}
	}

	

	private boolean isNotYetRelatedDiseaseVariant(PathogenTestDto savedTest) {
		List<DiseaseVariant> relatedVariants = FacadeProvider.getSampleFacade().getAssociatedDiseaseVariants(savedTest.getSample().getUuid());
		AtomicInteger savedTestsWithSameVariant = new AtomicInteger();
		relatedVariants.forEach(v -> {
			if (v != null && v.equals(savedTest.getTestedDiseaseVariant())) {
				savedTestsWithSameVariant.getAndIncrement();
			}
		});
		return savedTestsWithSameVariant.get() <= 1; // one occurrence is the saved test's one
	}







	public void showConvertEventParticipantToCaseDialog(EventParticipantDto eventParticipant, Disease testedDisease, Consumer<Boolean> callback) {
		final EventDto event = FacadeProvider.getEventFacade().getEventByUuid(eventParticipant.getEvent().getUuid(), false);
		final boolean differentDiseases = testedDisease != event.getDisease();
		final boolean noEventDisease = event.getDisease() == null;
		Label dialogContent = noEventDisease
			? new Label(I18nProperties.getString(Strings.messageConvertEventParticipantToCaseNoDisease))
			: differentDiseases
				? new Label(I18nProperties.getString(Strings.messageConvertEventParticipantToCaseDifferentDiseases))
				: new Label(I18nProperties.getString(Strings.messageConvertEventParticipantToCase));
		VaadinUiUtil.showConfirmationPopup(
			I18nProperties.getCaption(Captions.convertEventParticipantToCase),
			dialogContent,
			I18nProperties.getString(Strings.yes),
			I18nProperties.getString(Strings.no),
			800,
			confirmed -> {
				if (confirmed) {
					if (differentDiseases) {
						ControllerProvider.getCaseController().createFromEventParticipantDifferentDisease(eventParticipant, testedDisease);
					} else {
						ControllerProvider.getCaseController().createFromEventParticipant(eventParticipant);
					}
				}
				callback.accept(confirmed);
			});
	}





	public void showCaseCloningWithNewDiseaseDialog(
			CaseDataDto caseDataDto, Disease disease, String diseaseDetails, DiseaseVariant diseaseVariant, String diseaseVariantDetails) {
		showCaseCloningWithNewDiseaseDialog(
//				caseDataDto,
				Arrays.asList(caseDataDto),
				disease,
				diseaseDetails,
				diseaseVariant,
				diseaseVariantDetails);

	}

	private void showCaseCloningWithNewDiseaseDialog(
			Collection<CaseDataDto> existingCasesDtos,
			Disease disease,
			String diseaseDetails,
			DiseaseVariant diseaseVariant,
			String diseaseVariantDetails) {
	VaadinUiUtil.showConfirmationPopup(
		I18nProperties.getCaption(Captions.caseCloneCaseWithNewDisease) + " " + I18nProperties.getEnumCaption(disease) + "?",
		new Label(I18nProperties.getString(Strings.messageCloneCaseWithNewDisease)),
		I18nProperties.getString(Strings.yes),
		I18nProperties.getString(Strings.no),
		800,
		confirmed -> {
			if (confirmed) {
				for(CaseDataDto existingCaseDto : existingCasesDtos) {
					CaseDataDto clonedCase = FacadeProvider.getCaseFacade().cloneCase(existingCaseDto);
					clonedCase.setCaseClassification(CaseClassification.NOT_CLASSIFIED);
					clonedCase.setClassificationUser(null);
					clonedCase.setDisease(disease);
					clonedCase.setDiseaseDetails(diseaseDetails);
					clonedCase.setDiseaseVariant(diseaseVariant);
					clonedCase.setDiseaseVariantDetails(diseaseVariantDetails);
					clonedCase.setEpidNumber(null);
					clonedCase.setReportDate(new Date());
					FacadeProvider.getCaseFacade().saveCase(clonedCase);
					ControllerProvider.getCaseController().navigateToCase(clonedCase.getUuid());
				}
			}
		});
}

	
	private void showConfirmCaseDialog(Collection<CaseDataDto> cases) {

		if (cases == null || cases.size() == 0)
			return;

		String labelText = cases.size() > 1
			? String.format(I18nProperties.getString(Strings.messageConfirmCasesAfterPathogenTest), cases.size())
			: I18nProperties.getString(Strings.messageConfirmCaseAfterPathogenTest);

		// public void showConfirmCaseDialog(CaseDataDto caze) {

		// 	if (FacadeProvider.getConfigFacade().isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY)) {
		// 		return;
		// 	}

		// 	if (caze.getCaseClassification() == CaseClassification.CONFIRMED) {
		// 		return;
		// 	}

		VaadinUiUtil.showConfirmationPopup(
			I18nProperties.getCaption(Captions.caseConfirmCase),
			new Label(labelText),
			I18nProperties.getString(Strings.yes),
			I18nProperties.getString(Strings.no),
			800,
			e -> {
				if (e.booleanValue() == true) {
					for (CaseDataDto caze : cases) {
						caze.setCaseClassification(CaseClassification.CONFIRMED);
						FacadeProvider.getCaseFacade().save(caze);
					}
					// confirmed -> {
					// 	if (confirmed) {
					// 		CaseDataDto caseDataByUuid = FacadeProvider.getCaseFacade().getCaseDataByUuid(caze.getUuid());
					// 		caseDataByUuid.setCaseClassification(CaseClassification.CONFIRMED);
					// 		FacadeProvider.getCaseFacade().saveCase(caseDataByUuid);
					// 		ControllerProvider.getCaseController().navigateToCase(caseDataByUuid.getUuid());
				}
			});
	}
	
	public void showConfirmCaseDialog(CaseDataDto caze) {
		showConfirmCaseDialog(Arrays.asList(caze));
	}

	private void showSaveNotification(CaseDataDto existingCaseDto, CaseDataDto newCaseDto) {
		if (isNewCaseClassification(existingCaseDto, newCaseDto)) {
			Notification.show(
				String.format(I18nProperties.getString(Strings.messagePathogenTestSaved), newCaseDto.getCaseClassification().toString()),
				Type.TRAY_NOTIFICATION);
		} else {
			Notification.show(I18nProperties.getString(Strings.messagePathogenTestSavedShort), Type.TRAY_NOTIFICATION);
		}
	}

	private boolean isNewCaseClassification(CaseDataDto existingCaseDto, CaseDataDto newCaseDto) {
		return existingCaseDto.getCaseClassification() != newCaseDto.getCaseClassification() && newCaseDto.getClassificationUser() == null;
	}
	public void showNoCaseDialog(CaseDataDto caze) {
		if (FacadeProvider.getConfigFacade().isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY)) {
			return;
		}

		if (caze.getCaseClassification() == CaseClassification.NO_CASE || caze.getCaseClassification() == CaseClassification.CONFIRMED) {
			return;
		}

		VaadinUiUtil.showConfirmationPopup(
			I18nProperties.getCaption(Captions.caseNoCase),
			new Label(I18nProperties.getString(Strings.messageNoCaseAfterPathogenTests)),
			I18nProperties.getString(Strings.yes),
			I18nProperties.getString(Strings.no),
			800,
			confirmed -> {
				if (confirmed) {
					CaseDataDto caseDataByUuid = FacadeProvider.getCaseFacade().getCaseDataByUuid(caze.getUuid());
					caseDataByUuid.setCaseClassification(CaseClassification.NO_CASE);
					FacadeProvider.getCaseFacade().saveCase(caseDataByUuid);
				}
			});
	}

//	private enum SavePathogenTest_NeededAction {
//		CONFIRM_CASE_CLASSIFICATION,
//		CLONE_CASE_WITH_NEW_DISEASE,
//	}



	private boolean isSampleResultDifferentFromPathogenTest (SampleIndexDto sample, PathogenTestDto test) {
		return test != null
				&& test.getTestResult() != null
				&& Boolean.TRUE.equals(test.getTestResultVerified())
				&& test.getTestedDisease() == sample.getDisease()
				&& test.getTestResult() != sample.getPathogenTestResult();
	}
}
