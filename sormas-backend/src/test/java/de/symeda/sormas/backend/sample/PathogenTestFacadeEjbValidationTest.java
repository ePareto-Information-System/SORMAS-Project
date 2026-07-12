package de.symeda.sormas.backend.sample;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.Disease;

public class PathogenTestFacadeEjbValidationTest {

	@Test
	public void shouldUseSupplementaryResultsForVhfDiseases() {
		assertTrue(PathogenTestFacadeEjb.usesVhfSpecificTestResults(Disease.UNSPECIFIED_VHF));
		Disease.AHF_DISEASES.forEach(disease -> assertTrue(PathogenTestFacadeEjb.usesVhfSpecificTestResults(disease)));
	}

	@Test
	public void shouldRequireStandardResultsForOtherDiseases() {
		assertFalse(PathogenTestFacadeEjb.usesVhfSpecificTestResults(Disease.CHOLERA));
		assertFalse(PathogenTestFacadeEjb.usesVhfSpecificTestResults(null));
	}
}
