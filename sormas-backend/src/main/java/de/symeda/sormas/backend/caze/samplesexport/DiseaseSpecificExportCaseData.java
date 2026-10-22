package de.symeda.sormas.backend.caze.samplesexport;

import de.symeda.sormas.api.caze.CaseDataDto;

/** Internal, already access-filtered case page entry used by the export job. */
public class DiseaseSpecificExportCaseData {

	private final Long id;
	private final CaseDataDto caze;

	public DiseaseSpecificExportCaseData(Long id, CaseDataDto caze) {
		this.id = id;
		this.caze = caze;
	}

	public Long getId() {
		return id;
	}

	public CaseDataDto getCaze() {
		return caze;
	}
}
