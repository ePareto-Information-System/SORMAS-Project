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
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.Diseases;

import java.util.Arrays;
import java.util.List;

public enum SampleMaterial {

	WHOLE_BLOOD,
	SERA,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	STOOL,
	NASAL_SWAB,
	THROAT_SWAB,
	NP_SWAB,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	RECTAL_SWAB,
	CEREBROSPINAL_FLUID,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	CRUST,
	TISSUE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	URINE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	CORNEA_PM,
	SALIVA,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	URINE_PM,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	NUCHAL_SKIN_BIOPSY,
	SPUTUM,
	ENDOTRACHEAL_ASPIRATE,
	BRONCHOALVEOLAR_LAVAGE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	BRAIN_TISSUE,
	ANTERIOR_NARES_SWAB,
	OP_ASPIRATE,
	NP_ASPIRATE,
	PLEURAL_FLUID,
	PLASMA,
	SERUM,
	POST_MORTEM_LIVER_SPECIMEN,
	PLASMA_SERUM,
	OTHER, OROPHARYNGEAL_SWAB;

	//getYellowFeverMateriealTypes
	public static SampleMaterial[] getYellowFeverMateriealTypes() {
		return new SampleMaterial[] { SampleMaterial.WHOLE_BLOOD, SampleMaterial.SERUM, SampleMaterial.POST_MORTEM_LIVER_SPECIMEN };
	}

	//MEASELS Sample Types
	public static SampleMaterial[] getMeaselsMateriealTypes() {
		return new SampleMaterial[] { SampleMaterial.NASAL_SWAB, SampleMaterial.THROAT_SWAB, SampleMaterial.WHOLE_BLOOD, SampleMaterial.URINE };
	}


	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
	public static final List<SampleMaterial> YF_TYPES = Arrays.asList(
			WHOLE_BLOOD, SERUM, POST_MORTEM_LIVER_SPECIMEN);


	public static String toString(SampleMaterial value, String details) {

		if (value == null) {
			return "";
		}

		if (value == SampleMaterial.OTHER) {
			return DataHelper.toStringNullable(details);
		}

		return value.toString();
	}
}
