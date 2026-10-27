package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;

public class DiseaseSpecificExportSeedIntegrityTest {

	@Test
	public void testMigrationAndGeneratedSeedMatchTheSchemaCatalog() throws Exception {
		String schema = readSchema();
		assertTrue(schema.contains("CREATE TABLE diseasespecificexportfield ("));
		assertTrue(schema.contains("CREATE TABLE diseasespecificexportfield_history (LIKE diseasespecificexportfield)"));
		assertTrue(schema.contains("uq_diseasespecificexportfield_disease_path UNIQUE (disease, propertypath)"));
		assertTrue(schema.contains("uq_diseasespecificexportfield_disease_order UNIQUE (disease, displayorder)"));
		assertTrue(schema.contains("ON diseasespecificexportfield (disease, active, displayorder)"));
		assertTrue(schema.contains("delete_history_trigger('diseasespecificexportfield_history', 'id')"));
		assertTrue(schema.contains("VALUES (675, 'Add disease-specific export field configuration')"));
		assertFalse(schema.contains("INSERT INTO diseasespecificexportfield ("));

		Map<Disease, Integer> expectedCounts = expectedCounts();
		Set<String> fullCatalogPaths = DiseaseSpecificCaseExportSchema.buildAllFields("en-GH")
			.stream()
			.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
			.collect(Collectors.toCollection(HashSet::new));
		assertEquals(DiseaseSpecificCaseExportSchema.buildAllFields("en-GH").size(), fullCatalogPaths.size());
		int total = 0;
		for (Map.Entry<Disease, Integer> expected : expectedCounts.entrySet()) {
			String resourceName = "disease-specific-export-fields/" + expected.getKey().name() + ".csv";
			InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName);
			assertNotNull(input, "Missing seed CSV for " + expected.getKey());
			List<DiseaseSpecificExportFieldCsv.Row> rows = DiseaseSpecificExportFieldCsv.read(input);
			total += rows.size();
			assertEquals(expected.getValue().intValue(), rows.size(), "Wrong seed count for " + expected.getKey());
			for (int i = 0; i < rows.size(); i++) {
				assertEquals(expected.getKey(), rows.get(i).getDisease());
				assertEquals(i, rows.get(i).getDisplayOrder(), "Non-contiguous display order for " + expected.getKey());
				assertTrue(rows.get(i).isActive(), "Inactive initial field for " + expected.getKey());
			}
			assertEquals(
				rows.size(),
				rows.stream().map(DiseaseSpecificExportFieldCsv.Row::getPropertyPath).distinct().count(),
				"Duplicate property path for " + expected.getKey());

			List<String> catalogPaths = DiseaseSpecificCaseExportSchema.build(expected.getKey(), "en-GH")
				.stream()
				.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
				.collect(Collectors.toList());
			assertEquals(catalogPaths, rows.stream().map(DiseaseSpecificExportFieldCsv.Row::getPropertyPath).collect(Collectors.toList()));
			assertTrue(fullCatalogPaths.containsAll(catalogPaths), "Full field catalog is incomplete for " + expected.getKey());
		}
		assertEquals(13, expectedCounts.size());
		assertEquals(1782, total);
	}

	private String readSchema() throws Exception {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("sql/sormas_schema.sql")) {
			assertNotNull(input);
			return new String(input.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	private Map<Disease, Integer> expectedCounts() {
		Map<Disease, Integer> counts = new EnumMap<>(Disease.class);
		counts.put(Disease.AFP, 174);
		counts.put(Disease.CHOLERA, 146);
		counts.put(Disease.CSM, 160);
		counts.put(Disease.GUINEA_WORM, 113);
		counts.put(Disease.MEASLES, 125);
		counts.put(Disease.MONKEYPOX, 112);
		counts.put(Disease.NEW_INFLUENZA, 125);
		counts.put(Disease.YELLOW_FEVER, 133);
		counts.put(Disease.CORONAVIRUS, 156);
		counts.put(Disease.NEONATAL_TETANUS, 96);
		counts.put(Disease.UNSPECIFIED_VHF, 185);
		counts.put(Disease.FOODBORNE_ILLNESS, 159);
		counts.put(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, 98);
		return counts;
	}

}
