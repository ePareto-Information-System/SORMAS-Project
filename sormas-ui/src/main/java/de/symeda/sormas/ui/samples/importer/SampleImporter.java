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
package de.symeda.sormas.ui.samples.importer;

import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import de.symeda.sormas.api.FacadeProvider;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.importexport.ImportLineResultDto;
import de.symeda.sormas.api.importexport.InvalidColumnException;
import de.symeda.sormas.api.importexport.ValueSeparator;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonFacade;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleFacade;
import de.symeda.sormas.api.sample.SampleSelectionDto;
import de.symeda.sormas.api.sample.SampleSimilarityCriteria;
import de.symeda.sormas.api.sample.sampleimport.SampleImportEntities;
import de.symeda.sormas.api.sample.sampleimport.SampleImportFacade;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.importer.*;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.DiscardListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Data importer that is used to import Samples and associated samples.
 * This importer adds the following logic:
 *
 * - Check the database for similar Samples and, if at least one is found, execute the
 * similarityCallback received by the calling class.
 * - The import will wait for the similarityCallback to be resolved before it is continued
 * - Based on the results of the similarityCallback, an existing Sample might be overridden by
 * the data in the CSV file
 * - Save the person and Sample to the database (unless the Sample was skipped or the import
 * was canceled)
 */
public class SampleImporter extends DataImporter {

	private UI currentUI;
	private final SampleImportFacade sampleImportFacade;

	private final PersonFacade personFacade;
	private final SampleFacade sampleFacade;

	public SampleImporter(File inputFile, boolean hasEntityClassRow, UserDto currentUser, ValueSeparator csvSeparator) throws IOException {
		super(inputFile, hasEntityClassRow, currentUser, csvSeparator);

		sampleImportFacade = FacadeProvider.getSampleImportFacade();

		personFacade = FacadeProvider.getPersonFacade();
		sampleFacade = FacadeProvider.getSampleFacade();
	}

	@Override
	public void startImport(Consumer<StreamResource> addErrorReportToLayoutCallback, UI currentUI, boolean duplicatesPossible)
		throws IOException, CsvValidationException {

		this.currentUI = currentUI;
		super.startImport(addErrorReportToLayoutCallback, currentUI, duplicatesPossible);
	}

	@Override
	protected ImportLineResult importDataFromCsvLine(
		String[] values,
		String[] entityClasses,
		String[] entityProperties,
		String[][] entityPropertyPaths,
		boolean firstLine)
		throws IOException, InvalidColumnException, InterruptedException {

		// regenerate the UUID to prevent overwrite in Sample of export and import of the same entities
		setValueUuid(values, entityProperties, DataHelper.createUuid());

		ImportLineResultDto<SampleImportEntities> importResult =
			sampleImportFacade.importSampleData(values, entityClasses, entityProperties, entityPropertyPaths, !firstLine);

		if (importResult.isError()) {
			writeImportError(values, importResult.getMessage());
			return ImportLineResult.ERROR;
		} else if (importResult.isDuplicate()) {
			SampleImportEntities entities = importResult.getImportEntities();
			SampleDto importSample = entities.getSample();
			PersonDto importPerson = entities.getPerson();

			String selectedPersonUuid = null;
			String selectedSampleUuid = null;

			SampleImportConsumer consumer = new SampleImportConsumer();
			ImportSimilarityResultOption resultOption = null;

			SampleImportLock personSelectLock = new SampleImportLock();
			// We need to pause the current thread to prevent the import from continuing until the user has acted
			synchronized (personSelectLock) {
				// Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
				// to allow the importer to resume
				handlePersonSimilarity(
					importPerson,
					result -> consumer.onImportResult(result, personSelectLock),
					(person, similarityResultOption) -> new SampleImportSimilarityResult(person, null, similarityResultOption),
					Strings.infoSelectOrCreatePersonForImport,
					currentUI);

				try {
					if (!personSelectLock.wasNotified) {
						personSelectLock.wait();
					}
				} catch (InterruptedException e) {
					logger.error("InterruptedException when trying to perform LOCK.wait() in Sample import: " + e.getMessage());
					throw e;
				}

				if (consumer.result != null) {
					resultOption = consumer.result.getResultOption();
				}

				// If the user picked an existing person, override the Sample person with it
				if (ImportSimilarityResultOption.PICK.equals(resultOption)) {
					selectedPersonUuid = consumer.result.getMatchingPerson().getUuid();
					// Reset the importResult option for Sample selection
					resultOption = null;
				}
			}

			if (ImportSimilarityResultOption.SKIP.equals(resultOption)) {
				return ImportLineResult.SKIPPED;
			} else {
				final SampleImportLock SampleSelectLock = new SampleImportLock();
				synchronized (SampleSelectLock) {
					// Retrieve all similar Samples from the database
					SampleSimilarityCriteria criteria =
						SampleSimilarityCriteria.forSample(importSample, selectedPersonUuid != null ? selectedPersonUuid : importPerson.getUuid());

					List<SampleDto> similarSamples = sampleFacade.getSimilarSamplesSelection(criteria);

					if (similarSamples.size() > 0) {
						// Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
						// to allow the importer to resume
						if (selectedPersonUuid != null) {
							importPerson = personFacade.getPersonByUuid(selectedPersonUuid);
						}

						handleSampleSimilarity(
							new SampleImportSimilarityInput(importSample, importPerson, similarSamples),
							result -> consumer.onImportResult(result, SampleSelectLock));

						try {
							if (!SampleSelectLock.wasNotified) {
								SampleSelectLock.wait();
							}
						} catch (InterruptedException e) {
							logger.error("InterruptedException when trying to perform LOCK.wait() in Sample import: " + e.getMessage());
							throw e;
						}

						if (consumer.result != null) {
							resultOption = consumer.result.getResultOption();
						}

						// If the user chose to override an existing Sample with the imported Sample, insert the new data into the existing Sample and associate the imported samples with it
						if (resultOption == ImportSimilarityResultOption.OVERRIDE
							&& consumer.result != null
							&& consumer.result.getMatchingSample() != null) {
							selectedSampleUuid = consumer.result.getMatchingSample().getUuid();
							setValueUuid(values, entityProperties, selectedSampleUuid);
						}
					}
				}
			}

			if (resultOption == ImportSimilarityResultOption.SKIP) {
				consumer.result = null;
				return ImportLineResult.SKIPPED;
			} else if (resultOption == ImportSimilarityResultOption.PICK) {
				consumer.result = null;
				return ImportLineResult.DUPLICATE;
			} else if (resultOption == ImportSimilarityResultOption.CANCEL) {
				cancelImport();
				return ImportLineResult.SKIPPED;
			} else {
				ImportLineResultDto<SampleImportEntities> saveResult;
				boolean skipPersonValidation = selectedPersonUuid != null;
				if (selectedPersonUuid != null || selectedSampleUuid != null) {
					saveResult = sampleImportFacade.updateSampleWithImportData(
						selectedPersonUuid,
						selectedSampleUuid,
						values,
						entityClasses,
						entityPropertyPaths,
						skipPersonValidation);
				} else {
					saveResult = sampleImportFacade.saveImportedEntities(entities, skipPersonValidation);
				}

				if (saveResult.isError()) {
					writeImportError(values, importResult.getMessage());
					return ImportLineResult.ERROR;
				}
			}
		}

		return ImportLineResult.SUCCESS;
	}

	private void setValueUuid(String[] values, String[] entityProperties, String uuid) {
		int uuidIndex = ArrayUtils.indexOf(entityProperties, SampleDto.UUID);
		if (uuidIndex >= 0) {
			values[uuidIndex] = uuid;
		}
	}

	/**
	 * Presents a popup window to the user that allows them to deal with detected potentially duplicate Samples.
	 * By passing the desired result to the resultConsumer, the importer decided how to proceed with the import process.
	 */
	protected void handleSampleSimilarity(SampleImportSimilarityInput input, Consumer<SampleImportSimilarityResult> resultConsumer) {
		currentUI.accessSynchronously(() -> {
			SamplePickOrImportField pickOrImportField = new SamplePickOrImportField(input.getSample(), input.getPerson(), input.getSimilarSamples());
			pickOrImportField.setWidth(1024, Unit.PIXELS);

			final CommitDiscardWrapperComponent<SamplePickOrImportField> component = new CommitDiscardWrapperComponent<>(pickOrImportField);

			component.addCommitListener(() -> {
				SampleSelectionDto pickedSample = pickOrImportField.getValue();
				if (pickedSample != null) {
					if (pickOrImportField.isOverrideSample()) {
						resultConsumer.accept(new SampleImportSimilarityResult(null, pickedSample, ImportSimilarityResultOption.OVERRIDE));
					} else {
						resultConsumer.accept(new SampleImportSimilarityResult(null, pickedSample, ImportSimilarityResultOption.PICK));
					}
				} else {
					resultConsumer.accept(new SampleImportSimilarityResult(null, null, ImportSimilarityResultOption.CREATE));
				}
			});

			DiscardListener discardListener =
				() -> resultConsumer.accept(new SampleImportSimilarityResult(null, null, ImportSimilarityResultOption.CANCEL));
			component.addDiscardListener(discardListener);
			component.getDiscardButton().setCaption(I18nProperties.getCaption(Captions.actionCancel));
			component.getCommitButton().setCaption(I18nProperties.getCaption(Captions.actionConfirm));
			component.getCommitButton().setEnabled(false);

			Button skipButton = ButtonHelper.createButton(Captions.actionSkip, e -> {
				component.removeDiscardListener(discardListener);
				component.discard();
				resultConsumer.accept(new SampleImportSimilarityResult(null, null, ImportSimilarityResultOption.SKIP));
			});
			component.getButtonsPanel().addComponentAsFirst(skipButton);

			pickOrImportField.setSelectionChangeCallback((commitAllowed) -> component.getCommitButton().setEnabled(commitAllowed));

			VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.headingPickOrCreateSample));
		});
	}

	private class SampleImportConsumer {

		protected SampleImportSimilarityResult result;

		private void onImportResult(SampleImportSimilarityResult result, SampleImportLock LOCK) {
			this.result = result;
			synchronized (LOCK) {
				LOCK.notify();
				LOCK.wasNotified = true;
			}
		}
	}

	private class SampleImportLock {

		protected boolean wasNotified = false;
	}
}
