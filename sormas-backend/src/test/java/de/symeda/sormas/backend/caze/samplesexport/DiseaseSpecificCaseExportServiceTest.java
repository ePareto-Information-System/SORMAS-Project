package de.symeda.sormas.backend.caze.samplesexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.opencsv.CSVReader;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTestFacadeEjb.PathogenTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.SampleFacadeEjb.SampleFacadeEjbLocal;

@ExtendWith(MockitoExtension.class)
public class DiseaseSpecificCaseExportServiceTest {

	@Mock
	private CaseFacadeEjbLocal caseFacade;
	@Mock
	private PersonFacadeEjbLocal personFacade;
	@Mock
	private SampleFacadeEjbLocal sampleFacade;
	@Mock
	private PathogenTestFacadeEjbLocal pathogenTestFacade;
	@Mock
	private ConfigFacadeEjbLocal configFacade;
	@Mock
	private CaseSamplesDetailedExportService exportService;
	@InjectMocks
	private DiseaseSpecificCaseExportService service;

	@Test
	public void testAhfUsesTheConfiguredDiseaseGroup() {
		Set<Disease> relevantDiseases = DiseaseSpecificCaseExportService.getRelevantTestDiseases(Disease.UNSPECIFIED_VHF);
		assertTrue(relevantDiseases.contains(Disease.UNSPECIFIED_VHF));
		assertTrue(relevantDiseases.containsAll(Disease.AHF_DISEASES));
	}

	@Test
	public void testNntWritesOneCaseRowWithoutLoadingSamples() throws Exception {
		CaseCriteria criteria = new CaseCriteria().disease(Disease.NEONATAL_TETANUS);
		CaseDataDto caze = buildCase("CASE-NNT", Disease.NEONATAL_TETANUS);
		configureExportPage(criteria, Collections.singletonList(new DiseaseSpecificExportCaseData(1L, caze)));
		when(caseFacade.count(criteria, false)).thenReturn(1L);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		assertEquals(1, service.writeExport("EXPORT", criteria, Collections.<String>emptyList(), Language.EN, output));
		assertEquals(4, readRowCount(output));
		verify(sampleFacade, never()).getByCaseUuids(any());
		verify(pathogenTestFacade, never()).getBySampleUuids(any());
	}

	@Test
	public void testSamplesWithoutPathogenTestsRemainRowsAndSelectedRowsAreIntersected() throws Exception {
		CaseCriteria criteria = new CaseCriteria().disease(Disease.MEASLES);
		CaseDataDto caze = buildCase("CASE-MEASLES", Disease.MEASLES);
		configureExportPage(criteria, Collections.singletonList(new DiseaseSpecificExportCaseData(2L, caze)));

		CaseReferenceDto caseReference = new CaseReferenceDto(caze.getUuid());
		SampleDto later = buildSample("SAMPLE-B", caseReference);
		SampleDto earlier = buildSample("SAMPLE-A", caseReference);
		when(sampleFacade.getByCaseUuids(Collections.singletonList(caze.getUuid()))).thenReturn(Arrays.asList(later, earlier));
		when(pathogenTestFacade.getBySampleUuids(Arrays.asList("SAMPLE-B", "SAMPLE-A"))).thenReturn(Collections.emptyList());

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Set<String> selectedRows = Collections.singleton(caze.getUuid());
		assertEquals(2, service.writeExport("EXPORT", criteria, selectedRows, Language.EN, output));
		assertEquals(5, readRowCount(output));
		verify(caseFacade).getDiseaseSpecificExportCases(criteria, selectedRows, 0, 100);
	}

	private void configureExportPage(CaseCriteria criteria, java.util.List<DiseaseSpecificExportCaseData> cases) {
		when(configFacade.getCountryLocale()).thenReturn("en-GH");
		when(configFacade.getCsvSeparator()).thenReturn(',');
		when(configFacade.getStepSizeForCsvExport()).thenReturn(100);
		when(configFacade.getEpidPrefix()).thenReturn("GH");
		when(caseFacade.getDiseaseSpecificExportCases(eq(criteria), any(), eq(0), eq(100))).thenReturn(cases);
		when(personFacade.getDiseaseSpecificExportPersons(Collections.<String>emptyList())).thenReturn(Collections.emptyList());
	}

	private CaseDataDto buildCase(String uuid, Disease disease) {
		CaseDataDto caze = new CaseDataDto();
		caze.setUuid(uuid);
		caze.setDisease(disease);
		return caze;
	}

	private SampleDto buildSample(String uuid, CaseReferenceDto caze) {
		SampleDto sample = new SampleDto();
		sample.setUuid(uuid);
		sample.setAssociatedCase(caze);
		return sample;
	}

	private int readRowCount(ByteArrayOutputStream output) throws Exception {
		try (CSVReader reader = new CSVReader(new StringReader(new String(output.toByteArray(), StandardCharsets.UTF_8)))) {
			return reader.readAll().size();
		}
	}
}
