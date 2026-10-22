package de.symeda.sormas.backend.caze.samplesexport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTestFacadeEjb.PathogenTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.SampleFacadeEjb.SampleFacadeEjbLocal;

@Stateless
@LocalBean
public class DiseaseSpecificCaseExportService {

	@EJB
	private CaseFacadeEjbLocal caseFacade;
	@EJB
	private PersonFacadeEjbLocal personFacade;
	@EJB
	private SampleFacadeEjbLocal sampleFacade;
	@EJB
	private PathogenTestFacadeEjbLocal pathogenTestFacade;
	@EJB
	private ConfigFacadeEjbLocal configFacade;
	@EJB
	private CaseSamplesDetailedExportService exportService;

	public int writeExport(
		String exportUuid,
		CaseCriteria criteria,
		Collection<String> selectedRows,
		Language language,
		OutputStream outputStream) throws Exception {

		Disease disease = criteria != null ? criteria.getDisease() : null;
		if (!DiseaseSpecificCaseExportSchema.isSupported(disease)) {
			throw new IllegalArgumentException("Select one supported disease before starting a disease-specific export");
		}

		String countryLocale = configFacade.getCountryLocale();
		List<de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Column> columns =
			DiseaseSpecificCaseExportSchema.build(disease, countryLocale);
		Collection<String> selected = selectedRows != null ? selectedRows : Collections.<String>emptyList();
		long totalCases = selected.isEmpty() ? caseFacade.count(criteria, false) : selected.size();
		int rowCount = 0;
		int start = 0;
		int stepSize = configFacade.getStepSizeForCsvExport();

		try (DiseaseSpecificCaseExportWriter writer = new DiseaseSpecificCaseExportWriter(
			outputStream,
			configFacade.getCsvSeparator(),
			columns,
			language,
			countryLocale)) {

			List<DiseaseSpecificExportCaseData> cases;
			do {
				cases = caseFacade.getDiseaseSpecificExportCases(criteria, selected, start, stepSize);
				if (cases.isEmpty()) {
					break;
				}

				List<String> personUuids = cases.stream()
					.map(DiseaseSpecificExportCaseData::getCaze)
					.map(CaseDataDto::getPerson)
					.filter(person -> person != null)
					.map(person -> person.getUuid())
					.distinct()
					.collect(Collectors.toList());
				Map<String, PersonDto> peopleByUuid = new HashMap<>();
				for (PersonDto person : personFacade.getDiseaseSpecificExportPersons(personUuids)) {
					peopleByUuid.put(person.getUuid(), person);
				}

				Map<String, List<SampleDto>> samplesByCase = new HashMap<>();
				Map<String, List<PathogenTestDto>> testsBySample = new HashMap<>();
				if (DiseaseSpecificCaseExportSchema.hasSamples(disease)) {
					List<String> caseUuids = cases.stream().map(entry -> entry.getCaze().getUuid()).collect(Collectors.toList());
					List<SampleDto> samples = sampleFacade.getByCaseUuids(caseUuids);
					for (SampleDto sample : samples) {
						if (sample.getAssociatedCase() != null) {
							samplesByCase.computeIfAbsent(sample.getAssociatedCase().getUuid(), key -> new ArrayList<SampleDto>()).add(sample);
						}
					}
					for (List<SampleDto> caseSamples : samplesByCase.values()) {
						caseSamples.sort(Comparator.comparing(SampleDto::getSampleDateTime, Comparator.nullsLast(Date::compareTo)).thenComparing(SampleDto::getUuid));
					}

					List<String> sampleUuids = samples.stream().map(SampleDto::getUuid).collect(Collectors.toList());
					if (!sampleUuids.isEmpty()) {
						Set<Disease> relevantTestDiseases = getRelevantTestDiseases(disease);
						List<PathogenTestDto> tests = pathogenTestFacade.getBySampleUuids(sampleUuids);
						for (PathogenTestDto test : tests) {
							if (test.getSample() != null && relevantTestDiseases.contains(test.getTestedDisease())) {
								testsBySample.computeIfAbsent(test.getSample().getUuid(), key -> new ArrayList<PathogenTestDto>()).add(test);
							}
						}
						for (List<PathogenTestDto> sampleTests : testsBySample.values()) {
							sampleTests.sort(
								Comparator.comparing(PathogenTestDto::getTestDateTime, Comparator.nullsLast(Date::compareTo))
									.thenComparing(PathogenTestDto::getUuid));
						}
					}
				}

				for (DiseaseSpecificExportCaseData entry : cases) {
					CaseDataDto caze = entry.getCaze();
					PersonDto person = caze.getPerson() != null ? peopleByUuid.get(caze.getPerson().getUuid()) : null;
					List<SampleDto> samples = samplesByCase.get(caze.getUuid());
					if (samples == null || samples.isEmpty()) {
						writer.writeRow(new DiseaseSpecificCaseExportRow(
							configFacade.getEpidPrefix(), entry.getId(), caze, person, null, Collections.<PathogenTestDto>emptyList()));
						rowCount++;
					} else {
						for (SampleDto sample : samples) {
							writer.writeRow(new DiseaseSpecificCaseExportRow(
								configFacade.getEpidPrefix(),
								entry.getId(),
								caze,
								person,
								sample,
								testsBySample.get(sample.getUuid())));
							rowCount++;
						}
					}
				}
				writer.flush();
				start += cases.size();
				exportService.updateProgress(exportUuid, rowCount, totalCases);
			} while (cases.size() == stepSize);
		}
		return rowCount;
	}

	static Set<Disease> getRelevantTestDiseases(Disease disease) {
		Set<Disease> diseases = new HashSet<>();
		diseases.add(disease);
		List<Disease> configuredDiseases = Disease.diseaseMap.get(disease);
		if (configuredDiseases != null) {
			diseases.addAll(configuredDiseases);
		}
		if (disease == Disease.UNSPECIFIED_VHF) {
			diseases.addAll(Disease.AHF_DISEASES);
		}
		return diseases;
	}
}
