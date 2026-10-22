package de.symeda.sormas.backend.caze.samplesexport;

import java.util.Collections;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.SampleDto;

class DiseaseSpecificCaseExportRow {

	private final String country;
	private final Long caseId;
	private final CaseDataDto caze;
	private final PersonDto person;
	private final SampleDto sample;
	private final List<PathogenTestDto> pathogenTests;

	DiseaseSpecificCaseExportRow(
		String country,
		Long caseId,
		CaseDataDto caze,
		PersonDto person,
		SampleDto sample,
		List<PathogenTestDto> pathogenTests) {
		this.country = country;
		this.caseId = caseId;
		this.caze = caze;
		this.person = person;
		this.sample = sample;
		this.pathogenTests = pathogenTests != null ? pathogenTests : Collections.<PathogenTestDto>emptyList();
	}

	String getCountry() {
		return country;
	}

	Long getCaseId() {
		return caseId;
	}

	CaseDataDto getCaze() {
		return caze;
	}

	PersonDto getPerson() {
		return person;
	}

	SampleDto getSample() {
		return sample;
	}

	List<PathogenTestDto> getPathogenTests() {
		return pathogenTests;
	}
}
