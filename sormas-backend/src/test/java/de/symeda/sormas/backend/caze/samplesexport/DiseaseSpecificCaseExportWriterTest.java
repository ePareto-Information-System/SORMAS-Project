package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.opencsv.CSVReader;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;
import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.utils.CSVCommentLineValidator;

public class DiseaseSpecificCaseExportWriterTest {

	@Test
	public void testThreeHeadersAndJoinedPathogenTests() throws Exception {
		CaseDataDto caze = new CaseDataDto();
		caze.setUuid("CASE-UUID");
		caze.setDisease(Disease.MEASLES);
		caze.setEpidNumber("EPID-1");
		caze.setPerson(new PersonReferenceDto("PERSON-UUID"));

		PersonDto person = PersonDto.build();
		person.setUuid("PERSON-UUID");
		person.setFirstName("Juma");
		person.getAddress().setUuid("LOCATION-UUID");
		person.getAddress().setCity("Accra");

		SampleDto sample = new SampleDto();
		sample.setUuid("SAMPLE-UUID");

		PathogenTestDto first = PathogenTestDto.build(new SampleReferenceDto("SAMPLE-UUID", "Sample"), null);
		first.setUuid("TEST-1");
		first.setTestedDisease(Disease.MEASLES);
		first.setTestResult(PathogenTestResultType.POSITIVE);
		first.setOtherNotesAndObservations("first, \"quoted\"\nline");
		PathogenTestDto second = PathogenTestDto.build(new SampleReferenceDto("SAMPLE-UUID", "Sample"), null);
		second.setUuid("TEST-2");
		second.setTestedDisease(Disease.MEASLES);
		second.setTestResult(PathogenTestResultType.NEGATIVE);
		second.setOtherNotesAndObservations("second");

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (DiseaseSpecificCaseExportWriter writer = new DiseaseSpecificCaseExportWriter(
			output,
			',',
			DiseaseSpecificCaseExportSchema.build(Disease.MEASLES, "en-GH"),
			Language.EN,
			"en-GH")) {
			writer.writeRow(new DiseaseSpecificCaseExportRow("GH", 42L, caze, person, sample, Arrays.asList(first, second)));
		}

		List<String[]> rows;
		try (CSVReader reader = new CSVReader(new StringReader(new String(output.toByteArray(), StandardCharsets.UTF_8)))) {
			rows = reader.readAll();
		}
		assertEquals(4, rows.size());
		assertEquals(rows.get(0).length, rows.get(1).length);
		assertEquals(rows.get(1).length, rows.get(2).length);
		assertEquals(rows.get(2).length, rows.get(3).length);
		assertTrue(rows.get(2)[0].startsWith(CSVCommentLineValidator.DEFAULT_COMMENT_LINE_PREFIX));

		assertEquals("42", value(rows, "id"));
		assertEquals("CASE-UUID", value(rows, "uuid"));
		assertEquals("PERSON-UUID", value(rows, "person.uuid"));
		assertEquals("LOCATION-UUID", value(rows, "person.address.uuid"));
		assertEquals("SAMPLE-UUID", value(rows, "sample.uuid"));
		assertEquals("Juma", value(rows, "person.firstName"));
		assertEquals("Accra", value(rows, "person.address.city"));
		assertTrue(value(rows, "pathogenTests.testResult").contains(" | "));
		assertEquals("first, \"quoted\"\nline | second", value(rows, "pathogenTests.otherNotesAndObservations"));
		assertTrue(caption(rows, "sample.shipmentDate").contains("("));
	}

	@Test
	public void testConfiguredFieldsAreTheOnlyColumnsInEveryRow() throws Exception {
		CaseDataDto caze = new CaseDataDto();
		caze.setUuid("CASE-UUID");
		caze.setDisease(Disease.MEASLES);
		PersonDto person = PersonDto.build();
		person.setFirstName("Juma");
		PathogenTestDto test = PathogenTestDto.build(new SampleReferenceDto("SAMPLE", "Sample"), null);
		test.setTestResult(PathogenTestResultType.POSITIVE);

		List<String> configuredPaths = Arrays.asList("country", "person.firstName", "pathogenTests.testResult");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (DiseaseSpecificCaseExportWriter writer = new DiseaseSpecificCaseExportWriter(
			output,
			',',
			DiseaseSpecificCaseExportSchema.build(Disease.MEASLES, "en-GH", configuredPaths),
			Language.EN,
			"en-GH")) {
			writer.writeRow(new DiseaseSpecificCaseExportRow("GH", 42L, caze, person, null, Arrays.asList(test)));
		}

		List<String[]> rows;
		try (CSVReader reader = new CSVReader(new StringReader(new String(output.toByteArray(), StandardCharsets.UTF_8)))) {
			rows = reader.readAll();
		}
		assertEquals(4, rows.size());
		for (String[] row : rows) {
			assertEquals(configuredPaths.size(), row.length);
		}
		assertEquals(configuredPaths, Arrays.asList(rows.get(1)));
		assertEquals("GH", value(rows, "country"));
		assertEquals("Juma", value(rows, "person.firstName"));
		assertEquals("Positive", value(rows, "pathogenTests.testResult"));
		assertFalse(Arrays.asList(rows.get(1)).contains("uuid"));
	}

	@Test
	public void testCollectionValuedColumnCanBeSelectedAsOneField() throws Exception {
		CaseDataDto caze = new CaseDataDto();
		caze.setDisease(Disease.FOODBORNE_ILLNESS);
		FoodHistoryDto foodHistory = FoodHistoryDto.build();
		AffectedPersonDto affectedPerson = AffectedPersonDto.build();
		affectedPerson.setNameOfAffectedPerson("Akosua");
		foodHistory.setAffectedPersons(Arrays.asList(affectedPerson));
		caze.setFoodHistory(foodHistory);

		List<String> configuredPaths = Arrays.asList("country", "foodHistory.affectedPersons");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (DiseaseSpecificCaseExportWriter writer = new DiseaseSpecificCaseExportWriter(
			output,
			',',
			DiseaseSpecificCaseExportSchema.build(Disease.FOODBORNE_ILLNESS, "en-GH", configuredPaths),
			Language.EN,
			"en-GH")) {
			writer.writeRow(new DiseaseSpecificCaseExportRow("GH", 42L, caze, null, null, Arrays.asList()));
		}

		List<String[]> rows;
		try (CSVReader reader = new CSVReader(new StringReader(new String(output.toByteArray(), StandardCharsets.UTF_8)))) {
			rows = reader.readAll();
		}
		assertEquals(configuredPaths, Arrays.asList(rows.get(1)));
		assertTrue(value(rows, "foodHistory.affectedPersons").contains("Akosua"));
	}

	private String value(List<String[]> rows, String propertyPath) {
		for (int i = 0; i < rows.get(1).length; i++) {
			if (propertyPath.equals(rows.get(1)[i])) {
				return rows.get(3)[i];
			}
		}
		throw new AssertionError("Missing property path " + propertyPath);
	}

	private String caption(List<String[]> rows, String propertyPath) {
		for (int i = 0; i < rows.get(1).length; i++) {
			if (propertyPath.equals(rows.get(1)[i])) {
				return rows.get(2)[i];
			}
		}
		throw new AssertionError("Missing property path " + propertyPath);
	}
}
