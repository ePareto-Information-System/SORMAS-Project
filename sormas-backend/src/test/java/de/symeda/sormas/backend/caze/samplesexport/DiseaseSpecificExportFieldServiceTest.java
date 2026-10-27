package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;

public class DiseaseSpecificExportFieldServiceTest {

	@Test
	public void testCsvSelectionIsActiveAndRemainingFullCatalogIsInactive() {
		RecordingService service = new RecordingService();
		service.setEntityManager(mock(EntityManager.class));

		service.seedFromCsvResources();

		List<String> fullCatalog = DiseaseSpecificCaseExportSchema.buildAllFields("en-GH")
			.stream()
			.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
			.collect(Collectors.toList());
		assertEquals(762, fullCatalog.size());
		for (Disease disease : DiseaseSpecificCaseExportSchema.getSupportedDiseases()) {
			List<DiseaseSpecificExportField> stored = service.getByDisease(disease);
			List<String> initialSelection = DiseaseSpecificCaseExportSchema.build(disease, "en-GH")
				.stream()
				.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
				.collect(Collectors.toList());

			assertEquals(fullCatalog.size(), stored.size(), "Wrong full-catalog size for " + disease);
			for (int i = 0; i < stored.size(); i++) {
				assertEquals(i, stored.get(i).getDisplayOrder(), "Non-contiguous order for " + disease);
			}
			assertEquals(
				initialSelection,
				stored.stream().filter(DiseaseSpecificExportField::isActive).map(DiseaseSpecificExportField::getPropertyPath).collect(Collectors.toList()),
				"Imported selection changed for " + disease);
			assertTrue(stored.subList(0, initialSelection.size()).stream().allMatch(DiseaseSpecificExportField::isActive));
			assertFalse(stored.subList(initialSelection.size(), stored.size()).stream().anyMatch(DiseaseSpecificExportField::isActive));
		}

		int persistedCount = service.getPersistedCount();
		service.seedFromCsvResources();
		assertEquals(persistedCount, service.getPersistedCount(), "Catalog synchronization is not idempotent");
	}

	private static final class RecordingService extends DiseaseSpecificExportFieldService {

		private final Map<Disease, List<DiseaseSpecificExportField>> fields = new EnumMap<>(Disease.class);
		private int persistedCount;

		@Override
		public List<DiseaseSpecificExportField> getByDisease(Disease disease) {
			return new ArrayList<>(fields.computeIfAbsent(disease, key -> new ArrayList<>()));
		}

		@Override
		public void persist(DiseaseSpecificExportField field) {
			fields.computeIfAbsent(field.getDisease(), key -> new ArrayList<>()).add(field);
			persistedCount++;
		}

		private void setEntityManager(EntityManager entityManager) {
			em = entityManager;
		}

		private int getPersistedCount() {
			return persistedCount;
		}
	}
}
