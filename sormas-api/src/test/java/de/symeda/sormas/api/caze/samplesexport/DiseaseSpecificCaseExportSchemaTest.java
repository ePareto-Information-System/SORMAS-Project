package de.symeda.sormas.api.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Block;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Column;

public class DiseaseSpecificCaseExportSchemaTest {

	@Test
	public void testSupportedDiseaseSchemasAreStableAndUnique() {
		assertEquals(13, DiseaseSpecificCaseExportSchema.getSupportedDiseases().size());
		for (Disease disease : DiseaseSpecificCaseExportSchema.getSupportedDiseases()) {
			List<Column> columns = DiseaseSpecificCaseExportSchema.build(disease, "en-GH");
			assertEquals("country", columns.get(0).getPropertyPath());
			assertEquals("id", columns.get(1).getPropertyPath());
			assertEquals("uuid", columns.get(2).getPropertyPath());
			Set<String> paths = columns.stream().map(Column::getPropertyPath).collect(Collectors.toSet());
			assertEquals(columns.size(), paths.size(), "Duplicate path for " + disease);
			assertTrue(paths.contains("person.uuid"));
			assertTrue(paths.contains("person.address.uuid"));
			assertTrue(paths.contains("hospitalization.uuid"));
			for (Block block : blocks(disease)) {
				if (block != Block.CASE) {
					Column firstBlockColumn = columns.stream().filter(column -> column.getBlock() == block).findFirst().orElseThrow(AssertionError::new);
					assertEquals("uuid", firstBlockColumn.getProperty(), "Entity UUID is not first for " + disease + " / " + block);
				}
			}
		}
	}

	@Test
	public void testExactEntityBlocksForEverySupportedDisease() {
		for (Disease disease : DiseaseSpecificCaseExportSchema.getSupportedDiseases()) {
			Set<Block> expected = EnumSet.of(Block.CASE, Block.PERSON, Block.LOCATION, Block.HOSPITALIZATION);
			if (disease != Disease.FOODBORNE_ILLNESS) {
				expected.add(Block.SYMPTOMS);
			}
			if (disease != Disease.MONKEYPOX && disease != Disease.NEONATAL_TETANUS) {
				expected.add(Block.EPI_DATA);
			}
			if (disease == Disease.AFP) {
				expected.add(Block.SIXTY_DAY);
				expected.add(Block.AFP_IMMUNIZATION);
			} else if (disease == Disease.CHOLERA || disease == Disease.MONKEYPOX) {
				expected.add(Block.RISK_FACTOR);
			} else if (disease == Disease.FOODBORNE_ILLNESS) {
				expected.add(Block.FOOD_HISTORY);
				expected.add(Block.SIXTY_DAY);
				expected.add(Block.INVESTIGATION_NOTES);
			}
			if (disease != Disease.NEONATAL_TETANUS && disease != Disease.FOODBORNE_ILLNESS) {
				expected.add(Block.SAMPLE);
				expected.add(Block.PATHOGEN_TEST);
			}
			assertEquals(expected, blocks(disease), "Unexpected blocks for " + disease);
		}
	}

	@Test
	public void testDiseaseTabsAndSampleBlocks() {
		Set<Block> afpBlocks = blocks(Disease.AFP);
		assertTrue(afpBlocks.contains(Block.SIXTY_DAY));
		assertTrue(afpBlocks.contains(Block.AFP_IMMUNIZATION));
		assertTrue(afpBlocks.contains(Block.SAMPLE));

		Set<Block> choleraBlocks = blocks(Disease.CHOLERA);
		assertTrue(choleraBlocks.contains(Block.RISK_FACTOR));
		assertTrue(choleraBlocks.contains(Block.SAMPLE));

		Set<Block> foodborneBlocks = blocks(Disease.FOODBORNE_ILLNESS);
		assertTrue(foodborneBlocks.contains(Block.FOOD_HISTORY));
		assertTrue(foodborneBlocks.contains(Block.INVESTIGATION_NOTES));
		assertFalse(foodborneBlocks.contains(Block.SYMPTOMS));
		assertFalse(foodborneBlocks.contains(Block.SAMPLE));

		Set<Block> nntBlocks = blocks(Disease.NEONATAL_TETANUS);
		assertFalse(nntBlocks.contains(Block.EPI_DATA));
		assertFalse(nntBlocks.contains(Block.SAMPLE));

		Set<Block> mpoxBlocks = blocks(Disease.MONKEYPOX);
		assertTrue(mpoxBlocks.contains(Block.RISK_FACTOR));
		assertFalse(mpoxBlocks.contains(Block.EPI_DATA));
	}

	@Test
	public void testFormDisabledFieldsAreNotExported() {
		Set<String> afpPaths = paths(Disease.AFP);
		assertFalse(afpPaths.contains("person.passportNumber"));
		assertFalse(afpPaths.contains("person.address.street"));
		assertFalse(afpPaths.contains("sample.samplePurpose"));
		assertFalse(afpPaths.contains("reportingOfficerName"));
		assertFalse(afpPaths.contains("epiData.patientTravelledTwoWeeksPrior"));
		assertFalse(afpPaths.contains("healthConditions.uuid"));

		Set<String> measlesPaths = paths(Disease.MEASLES);
		assertFalse(measlesPaths.contains("healthConditions.uuid"));
		assertTrue(measlesPaths.contains("symptoms.uuid"));

		Set<String> mpoxPaths = paths(Disease.MONKEYPOX);
		assertTrue(mpoxPaths.contains("dateOfNotification"));
		assertFalse(mpoxPaths.contains("outcomeDate"));
		Set<String> guineaWormPaths = paths(Disease.GUINEA_WORM);
		assertTrue(guineaWormPaths.contains("reportingVillage"));
		assertFalse(guineaWormPaths.contains("outcomeDate"));
	}

	@Test
	public void testSampleAndPathogenTestLayoutsAreDiseaseSpecific() {
		Set<String> measlesPaths = paths(Disease.MEASLES);
		assertTrue(measlesPaths.contains("sample.shipmentDate"));
		assertTrue(measlesPaths.contains("pathogenTests.virusDetectionGenotype"));
		assertFalse(measlesPaths.contains("sample.labLocal"));
		assertFalse(measlesPaths.contains("pathogenTests.laboratoryCytology"));
		assertFalse(measlesPaths.contains("pathogenTests.sampleTests"));

		Set<String> csmPaths = paths(Disease.CSM);
		assertTrue(csmPaths.contains("pathogenTests.laboratoryCytology"));
		assertFalse(csmPaths.contains("pathogenTests.virusDetectionGenotype"));

		Set<String> ahfPaths = paths(Disease.UNSPECIFIED_VHF);
		assertTrue(ahfPaths.contains("pathogenTests.sampleTests"));
		assertTrue(ahfPaths.contains("pathogenTests.sampleTestResultPCRDate"));
		assertFalse(ahfPaths.contains("pathogenTests.testType"));
		assertFalse(ahfPaths.contains("pathogenTests.laboratoryCytology"));

		Set<String> guineaWormPaths = paths(Disease.GUINEA_WORM);
		assertTrue(guineaWormPaths.contains("sample.labLocal"));
		assertTrue(guineaWormPaths.contains("sample.confirmedAsGuineaWorm"));
		assertFalse(guineaWormPaths.contains("sample.shipmentDate"));

		Set<String> yellowFeverPaths = paths(Disease.YELLOW_FEVER);
		assertTrue(yellowFeverPaths.contains("sample.ipSampleTestResults"));
		assertFalse(yellowFeverPaths.contains("sample.dateFirstSpecimen"));
		Set<String> influenzaPaths = paths(Disease.NEW_INFLUENZA);
		assertTrue(influenzaPaths.contains("sample.otherInfluenzaVirus"));
		assertFalse(influenzaPaths.contains("sample.dateFirstSpecimen"));
		assertFalse(paths(Disease.MONKEYPOX).contains("sample.pathogenTestResult"));

		Set<String> afpDiseasePaths = paths(Disease.AFP);
		assertTrue(afpDiseasePaths.contains("sixtyDay.paralysisWeaknessPresent"));
		assertFalse(afpDiseasePaths.contains("sixtyDay.foodAvailableTesting"));
		Set<String> foodbornePaths = paths(Disease.FOODBORNE_ILLNESS);
		assertTrue(foodbornePaths.contains("sixtyDay.foodAvailableTesting"));
		assertFalse(foodbornePaths.contains("sixtyDay.paralysisWeaknessPresent"));
		assertTrue(foodbornePaths.contains("foodHistory.affectedPersons"));
		assertFalse(foodbornePaths.contains("foodHistory.nameOfAffectedPerson"));
		assertTrue(foodbornePaths.contains("investigationNotes.surname"));

		List<String> orderedMeaslesPaths = orderedPaths(Disease.MEASLES);
		assertTrue(orderedMeaslesPaths.indexOf("sample.uuid") < orderedMeaslesPaths.indexOf("sample.sampleDateTime"));
		assertTrue(orderedMeaslesPaths.indexOf("sample.sampleDateTime") < orderedMeaslesPaths.indexOf("sample.fieldSampleID"));
		assertTrue(orderedMeaslesPaths.indexOf("sample.fieldSampleID") < orderedMeaslesPaths.indexOf("sample.sampleMaterial"));
		assertTrue(orderedMeaslesPaths.indexOf("pathogenTests.testType") < orderedMeaslesPaths.indexOf("pathogenTests.testDateTime"));
	}

	@Test
	public void testLiveFormFieldsAreExportedAndOffFormFieldsAreNot() {
		Set<String> measlesPaths = paths(Disease.MEASLES);
		assertTrue(measlesPaths.contains("reportDate"));
		assertTrue(measlesPaths.contains("notifiedByList"));
		assertTrue(measlesPaths.contains("reportLat"));
		assertTrue(measlesPaths.contains("person.firstName"));
		assertTrue(measlesPaths.contains("person.marriageStatus"));
		assertTrue(measlesPaths.contains("person.address.region"));
		assertTrue(measlesPaths.contains("person.address.landMark"));
		assertTrue(measlesPaths.contains("hospitalization.selectInpatientOutpatient"));
		assertTrue(measlesPaths.contains("hospitalization.seenAtAHealthFacility"));
		assertTrue(measlesPaths.contains("symptoms.fever"));
		assertTrue(measlesPaths.contains("symptoms.generalizedRash"));
		assertTrue(measlesPaths.contains("epiData.historyOfTravelOutsideTheVillageTownDistrict"));
		assertTrue(measlesPaths.contains("notifiedOther"));
		assertTrue(measlesPaths.contains("districtLevelDate"));
		assertFalse(measlesPaths.contains("dateOfInvestigation"));
		assertFalse(measlesPaths.contains("person.salutation"));
		assertFalse(measlesPaths.contains("person.occupationType"));
		assertFalse(measlesPaths.contains("quarantine"));
		assertFalse(measlesPaths.contains("followUpStatus"));
		assertFalse(measlesPaths.contains("symptoms.diarrhea"));
		assertFalse(measlesPaths.contains("hospitalization.place"));
		assertFalse(measlesPaths.contains("epiData.exposures"));

		Set<String> choleraPaths = paths(Disease.CHOLERA);
		assertTrue(choleraPaths.contains("hospitalization.hospitalRecordNumber"));
		assertTrue(choleraPaths.contains("symptoms.diarrhea"));
		assertTrue(choleraPaths.contains("epiData.exposedToRiskFactor"));
		assertTrue(choleraPaths.contains("riskFactor.drinkingWaterSourceOne"));
		assertTrue(choleraPaths.contains("otherNotesAndObservations"));
		assertTrue(choleraPaths.contains("numberOfPeopleInSameHousehold"));
		assertFalse(choleraPaths.contains("symptoms.generalizedRash"));
		assertFalse(choleraPaths.contains("hospitalization.selectInpatientOutpatient"));

		Set<String> nntPaths = paths(Disease.NEONATAL_TETANUS);
		assertTrue(nntPaths.contains("motherVaccinatedWithTT"));
		assertTrue(nntPaths.contains("person.receivedAntenatalCare"));
		assertTrue(nntPaths.contains("symptoms.babyDied"));
		assertFalse(nntPaths.contains("vaccinationStatus"));
		assertFalse(nntPaths.contains("epiData.uuid"));
		assertFalse(nntPaths.contains("sample.uuid"));

		Set<String> guineaWormPaths = paths(Disease.GUINEA_WORM);
		assertTrue(guineaWormPaths.contains("reportingVillage"));
		assertTrue(guineaWormPaths.contains("person.placeOfResidenceSameAsReportingVillage"));
		assertTrue(guineaWormPaths.contains("person.address.village"));
		assertTrue(guineaWormPaths.contains("symptoms.emergenceOfGuineaWorm"));
		assertFalse(guineaWormPaths.contains("person.birthdateYYYY"));

		Set<String> mpoxPaths = paths(Disease.MONKEYPOX);
		assertTrue(mpoxPaths.contains("dateOfNotification"));
		assertTrue(mpoxPaths.contains("person.ethnicity"));
		assertTrue(mpoxPaths.contains("hospitalization.admittedToHealthFacilityNew"));
		assertTrue(mpoxPaths.contains("symptoms.symptomsSelected"));
		assertTrue(mpoxPaths.contains("riskFactor.patientSpoxVaccinationScarPresent"));
		assertFalse(mpoxPaths.contains("epiData.uuid"));
		assertFalse(mpoxPaths.contains("symptoms.fever"));

		Set<String> afpPaths = paths(Disease.AFP);
		assertTrue(afpPaths.contains("person.place"));
		assertTrue(afpPaths.contains("symptoms.feverOnsetParalysis"));
		assertTrue(afpPaths.contains("afpImmunization.totalNumberDoses"));
		assertFalse(afpPaths.contains("reportingOfficerName"));
		assertFalse(afpPaths.contains("symptoms.fever"));

		assertTrue(paths(Disease.YELLOW_FEVER).contains("symptoms.temperature"));
		assertTrue(paths(Disease.YELLOW_FEVER).contains("reportingOfficerTitle"));
		assertFalse(paths(Disease.YELLOW_FEVER).contains("symptoms.emergenceOfGuineaWorm"));
		assertFalse(paths(Disease.YELLOW_FEVER).contains("reportingOfficerName"));
		assertTrue(paths(Disease.CSM).contains("epiData.exposures"));
		assertFalse(paths(Disease.CSM).contains("reportLat"));
		assertFalse(paths(Disease.CSM).contains("reportingOfficerEmail"));
		assertTrue(paths(Disease.CORONAVIRUS).contains("epiData.patientTravelledTwoWeeksPrior"));
		assertTrue(paths(Disease.CORONAVIRUS).contains("caseTransmissionClassification"));
		assertTrue(paths(Disease.FOODBORNE_ILLNESS).contains("hospitalization.soughtMedicalAttention"));
		assertFalse(paths(Disease.FOODBORNE_ILLNESS).contains("caseOrigin"));
		assertTrue(paths(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS).contains("person.applicable"));
		assertFalse(paths(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS).contains("functionOfReportingOfficer"));
		assertTrue(paths(Disease.NEW_INFLUENZA).contains("epiData.previouslyVaccinatedAgainstInfluenza"));
		assertFalse(paths(Disease.NEW_INFLUENZA).contains("reportingOfficerName"));
	}

	@Test
	public void testUnsupportedDiseaseIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> DiseaseSpecificCaseExportSchema.build(Disease.RABIES, "en-GH"));
	}

	private Set<Block> blocks(Disease disease) {
		return DiseaseSpecificCaseExportSchema.build(disease, "en-GH").stream().map(Column::getBlock).collect(Collectors.toSet());
	}

	private Set<String> paths(Disease disease) {
		return orderedPaths(disease).stream().collect(Collectors.toCollection(HashSet::new));
	}

	private List<String> orderedPaths(Disease disease) {
		return DiseaseSpecificCaseExportSchema.build(disease, "en-GH").stream().map(Column::getPropertyPath).collect(Collectors.toList());
	}
}
