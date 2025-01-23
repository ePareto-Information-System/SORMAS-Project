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

import static de.symeda.sormas.api.Disease.*;
import static de.symeda.sormas.api.Disease.OTHER;

public enum SampleMaterial {
	@Diseases({
	NEW_INFLUENZA,
	 })
	NASOPHARYNGEAL_SWAB,
	@Diseases({
			NEW_INFLUENZA,
	})
	OROPHARYNGEAL_SWAB,
	@Diseases({
			NEW_INFLUENZA,
	})
	ORO_NASO,
	@Diseases(value = {
	}, hide = true)
	BLOOD,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	SERA,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	STOOL,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	NASAL_SWAB,
	@Diseases(value = {
	}, hide = true)
	THROAT_SWAB,
//	NP_SWAB,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	RECTAL_SWAB,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	CEREBROSPINAL_FLUID,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	CRUST,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	TISSUE,
	@Diseases(value = {
		Disease.CORONAVIRUS }, hide = true)
	URINE,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	CORNEA_PM,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	SALIVA,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	URINE_PM,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	NUCHAL_SKIN_BIOPSY,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	SPUTUM,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	ENDOTRACHEAL_ASPIRATE,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	BRONCHOALVEOLAR_LAVAGE,
	@Diseases(value = {
		Disease.CORONAVIRUS, MEASLES }, hide = true)
	BRAIN_TISSUE,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	ANTERIOR_NARES_SWAB,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	OP_ASPIRATE,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	NP_ASPIRATE,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	PLEURAL_FLUID,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	PLASMA,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	SERUM,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	POST_MORTEM_LIVER_SPECIMEN,
	@Diseases(value = {
	}, hide = true)
	GINGIVAL_FLUID,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	PLASMA_SERUM,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	ASPIRATE,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	PUS,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	BIOPSY,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	URETHRAL,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	FOOD_WATER,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	BLOOD_ANTI_COAGULANT,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	CARDIAC,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	SWAB,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	VOMITUS,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	WATER,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	FOOD_ITEM,
	@Diseases(value = {
			MEASLES,
	}, hide = true)
	BLOOD_SERUM,
	@Diseases(value = {
	}, hide = true)
	OTHER;

	//getYellowFeverMateriealTypes
	public static SampleMaterial[] getYellowFeverMateriealTypes() {
		return new SampleMaterial[] { SampleMaterial.BLOOD, SampleMaterial.SERUM, SampleMaterial.POST_MORTEM_LIVER_SPECIMEN };
	}

	//MEASELS Sample Types
	public static SampleMaterial[] getMeaselsMateriealTypes() {
		return new SampleMaterial[] { GINGIVAL_FLUID, THROAT_SWAB, URINE, NASAL_SWAB, BLOOD_SERUM, OTHER };
	}
	//IDSR
	public static SampleMaterial[] getMPoxTypes() {
		return new SampleMaterial[] { BLOOD, PLASMA, SERUM, ASPIRATE, CEREBROSPINAL_FLUID, PUS, SALIVA, BIOPSY, STOOL, URETHRAL, URINE, SPUTUM, FOOD_WATER };
	}

	public static SampleMaterial[] getNewInfluenzaType() {
		return new SampleMaterial[] { CRUST, SWAB, BLOOD };
	}


	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public static SampleMaterial[] getCholeraMateriealTypes() {
		return new SampleMaterial[] { STOOL, VOMITUS, WATER, FOOD_ITEM, OTHER  };
	}


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
