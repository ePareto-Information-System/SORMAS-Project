package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;

@ExtendWith(MockitoExtension.class)
public class CaseSamplesDetailedExportFacadeEjbTest {

	@Mock
	private CaseSamplesDetailedExportService exportService;
	@Mock
	private CaseSamplesDetailedExportAsyncService asyncService;
	@Mock
	private DiseaseSpecificExportFieldService fieldService;
	@Mock
	private UserService userService;
	@InjectMocks
	private CaseSamplesDetailedExportFacadeEjb facade;

	@Test
	public void testDiseaseSpecificExportCapturesImmutableFieldSnapshot() {
		User user = new User();
		when(userService.getCurrentUser()).thenReturn(user);
		List<String> configuredPaths = new ArrayList<>(Arrays.asList("country", "uuid"));
		when(fieldService.getActivePropertyPaths(Disease.AFP)).thenReturn(configuredPaths);
		CaseCriteria criteria = new CaseCriteria().disease(Disease.AFP);

		facade.startDiseaseSpecificDetailedExport(criteria, Collections.emptyList(), Language.EN);
		configuredPaths.clear();

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<String>> snapshot = ArgumentCaptor.forClass(List.class);
		verify(asyncService).generateDiseaseSpecificAndNotify(
			any(String.class),
			eq(criteria),
			eq(Collections.emptyList()),
			eq(Language.EN),
			any(String.class),
			snapshot.capture());
		assertEquals(Arrays.asList("country", "uuid"), snapshot.getValue());
		assertThrows(UnsupportedOperationException.class, () -> snapshot.getValue().add("id"));
	}

	@Test
	public void testMissingConfigurationIsRejectedBeforeAuditRowCreation() {
		when(userService.getCurrentUser()).thenReturn(new User());
		when(fieldService.getActivePropertyPaths(Disease.AFP)).thenReturn(Collections.emptyList());

		assertThrows(
			IllegalStateException.class,
			() -> facade.startDiseaseSpecificDetailedExport(new CaseCriteria().disease(Disease.AFP), Collections.emptyList(), Language.EN));
		verify(exportService, never()).persistNewExport(any(CaseSamplesDetailedExport.class));
		verify(asyncService, never()).generateDiseaseSpecificAndNotify(any(), any(), any(), any(), any(), any());
	}
}
