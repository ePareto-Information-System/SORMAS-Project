/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 SORMAS Foundation gGmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.sample.PathogenTestResultType;

public class PathogenTestControllerTest {

	@Test
	public void shouldResolveIliCaseClassificationFromFinalLaboratoryResult() {
		assertEquals(
			CaseClassification.CONFIRMED,
			PathogenTestController.getIliCaseClassification(PathogenTestResultType.POSITIVE));
		assertEquals(CaseClassification.SUSPECT, PathogenTestController.getIliCaseClassification(PathogenTestResultType.NEGATIVE));
		assertEquals(CaseClassification.SUSPECT, PathogenTestController.getIliCaseClassification(PathogenTestResultType.PENDING));
		assertNull(PathogenTestController.getIliCaseClassification(PathogenTestResultType.INDETERMINATE));
		assertNull(PathogenTestController.getIliCaseClassification(PathogenTestResultType.NOT_DONE));
		assertNull(PathogenTestController.getIliCaseClassification(null));
	}
}
