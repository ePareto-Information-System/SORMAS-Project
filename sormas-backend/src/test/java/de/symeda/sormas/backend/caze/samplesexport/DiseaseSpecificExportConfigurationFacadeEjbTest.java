package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificExportFieldDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.backend.util.RightsAllowed;

@ExtendWith(MockitoExtension.class)
public class DiseaseSpecificExportConfigurationFacadeEjbTest {

	@Mock
	private DiseaseSpecificExportFieldService fieldService;
	@InjectMocks
	private DiseaseSpecificExportConfigurationFacadeEjb facade;

	@Test
	public void testFacadeIsProtectedByConfigurationRight() {
		RightsAllowed rights = DiseaseSpecificExportConfigurationFacadeEjb.class.getAnnotation(RightsAllowed.class);
		assertEquals(Collections.singletonList(UserRight._MANAGE_PUBLIC_EXPORT_CONFIGURATION), Arrays.asList(rights.value()));
	}

	@Test
	public void testGetFieldsPreservesDatabaseOrderAndMapsValues() {
		DiseaseSpecificExportField second = field("uuid", 1, false);
		DiseaseSpecificExportField first = field("country", 0, true);
		when(fieldService.getByDisease(Disease.AFP)).thenReturn(Arrays.asList(first, second));

		List<DiseaseSpecificExportFieldDto> result = facade.getFields(Disease.AFP);

		assertEquals(Arrays.asList("country", "uuid"), Arrays.asList(result.get(0).getPropertyPath(), result.get(1).getPropertyPath()));
		assertEquals(0, result.get(0).getDisplayOrder());
		assertTrue(result.get(0).isActive());
		assertFalse(result.get(1).isActive());
	}

	@Test
	public void testSaveActivatesSubmittedSetAndDeactivatesAllOthersWithoutChangingOrder() {
		DiseaseSpecificExportField country = field("country", 0, false);
		DiseaseSpecificExportField uuid = field("uuid", 1, true);
		when(fieldService.getByDisease(Disease.AFP)).thenReturn(Arrays.asList(country, uuid));

		facade.saveActiveFields(Disease.AFP, Collections.singleton("country"));

		assertTrue(country.isActive());
		assertFalse(uuid.isActive());
		assertEquals(0, country.getDisplayOrder());
		assertEquals(1, uuid.getDisplayOrder());
		verify(fieldService, times(2)).ensurePersisted(org.mockito.ArgumentMatchers.any(DiseaseSpecificExportField.class));
	}

	@Test
	public void testSaveRejectsEmptyUnknownAndUnsupportedSelections() {
		assertThrows(IllegalArgumentException.class, () -> facade.saveActiveFields(Disease.AFP, Collections.emptySet()));

		when(fieldService.getByDisease(Disease.AFP)).thenReturn(Collections.singletonList(field("country", 0, true)));
		assertThrows(IllegalArgumentException.class, () -> facade.saveActiveFields(Disease.AFP, Collections.singleton("unknown.path")));
		assertThrows(IllegalArgumentException.class, () -> facade.getFields(Disease.RABIES));
	}

	@Test
	public void testConfigurationCanBeExportedAndImported() {
		DiseaseSpecificExportField country = field("country", 0, true);
		DiseaseSpecificExportField uuid = field("uuid", 1, false);
		when(fieldService.getByDisease(Disease.AFP)).thenReturn(Arrays.asList(country, uuid));

		String exported = new String(facade.exportFields(Disease.AFP), StandardCharsets.UTF_8);
		assertTrue(exported.contains("disease,propertyPath,displayOrder,active"));
		assertTrue(exported.contains("AFP,country,0,true"));
		assertTrue(exported.contains("AFP,uuid,1,false"));

		String imported = "disease,propertyPath,displayOrder,active\n"
			+ "AFP,country,0,false\n"
			+ "AFP,uuid,1,true\n";
		facade.importFields(Disease.AFP, imported.getBytes(StandardCharsets.UTF_8));
		assertFalse(country.isActive());
		assertTrue(uuid.isActive());
	}

	@Test
	public void testImportAcceptsSelectedSubsetAndRejectsInvalidOrEmptySelections() {
		DiseaseSpecificExportField country = field("country", 0, true);
		DiseaseSpecificExportField uuid = field("uuid", 1, false);
		when(fieldService.getByDisease(Disease.AFP)).thenReturn(Arrays.asList(country, uuid));
		String subset = "disease,propertyPath,displayOrder,active\n"
			+ "AFP,uuid,99,true\n";
		facade.importFields(Disease.AFP, subset.getBytes(StandardCharsets.UTF_8));
		assertFalse(country.isActive());
		assertTrue(uuid.isActive());
		assertEquals(0, country.getDisplayOrder());
		assertEquals(1, uuid.getDisplayOrder());

		String empty = "disease,propertyPath,displayOrder,active\n"
			+ "AFP,country,0,false\n"
			+ "AFP,uuid,1,false\n";
		assertThrows(IllegalArgumentException.class, () -> facade.importFields(Disease.AFP, empty.getBytes(StandardCharsets.UTF_8)));

		String unknown = "disease,propertyPath,displayOrder,active\n"
			+ "AFP,unknown.path,0,true\n";
		assertThrows(IllegalArgumentException.class, () -> facade.importFields(Disease.AFP, unknown.getBytes(StandardCharsets.UTF_8)));
	}

	private DiseaseSpecificExportField field(String propertyPath, int displayOrder, boolean active) {
		DiseaseSpecificExportField field = new DiseaseSpecificExportField();
		field.setDisease(Disease.AFP);
		field.setPropertyPath(propertyPath);
		field.setDisplayOrder(displayOrder);
		field.setActive(active);
		return field;
	}
}
