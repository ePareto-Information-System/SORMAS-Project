/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.Diseases;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public enum PathogenTestType {

	ANTIBODY_DETECTION,
	ANTIGEN_DETECTION,
	RAPID_TEST,
	CULTURE,
	HISTOPATHOLOGY,
	ISOLATION,
	IGM_SERUM_ANTIBODY,
	IGG_SERUM_ANTIBODY,
	IGA_SERUM_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	INCUBATION_TIME,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	INDIRECT_FLUORESCENT_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	DIRECT_FLUORESCENT_ANTIBODY,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	MICROSCOPY,
	NEUTRALIZING_ANTIBODIES,
	PCR_RT_PCR,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	GRAM_STAIN,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	LATEX_AGGLUTINATION,
	CQ_VALUE_DETECTION,
	SEQUENCING,
	DNA_MICROARRAY,
	RDT,
	OTHER;

	public static final List<PathogenTestType> DISEASE_TESTS = Arrays.asList(
			IGM_SERUM_ANTIBODY,
			IGG_SERUM_ANTIBODY,
			PCR_RT_PCR
	);

	public static List<PathogenTestType> getCholeraPathogenTests() {
		return  Arrays.asList(CULTURE, RDT, GRAM_STAIN, PCR_RT_PCR);
	}

	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static String toString(PathogenTestType value, String details) {
		if (value == null) {
			return "";
		}

		if (value == PathogenTestType.OTHER) {
			return DataHelper.toStringNullable(details);
		}

		return value.toString();
	}
}
