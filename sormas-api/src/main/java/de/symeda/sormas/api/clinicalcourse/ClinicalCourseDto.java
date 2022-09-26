package de.symeda.sormas.api.clinicalcourse;

import javax.validation.Valid;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;

@DependingOnFeatureType(featureType = FeatureType.CLINICAL_MANAGEMENT)
public class ClinicalCourseDto extends EntityDto {

	public static final String I18N_PREFIX = "ClinicalCourse";
	private static final long serialVersionUID = -2664896907352864261L;

	public static final String HEALTH_CONDITIONS = "healthConditions";

	@Valid
	private HealthConditionsDto healthConditions;

	public static ClinicalCourseDto build() {
		ClinicalCourseDto clinicalCourse = new ClinicalCourseDto();
		clinicalCourse.setUuid(DataHelper.createUuid());
		return clinicalCourse;
	}

	public static ClinicalCourseDto build(HealthConditionsDto healthConditions) {

		ClinicalCourseDto clinicalCourse = new ClinicalCourseDto();
		clinicalCourse.setUuid(DataHelper.createUuid());

		if (healthConditions == null) {
			clinicalCourse.setHealthConditions(HealthConditionsDto.build());
		} else {
			clinicalCourse.setHealthConditions(healthConditions);
		}

		return clinicalCourse;
	}

	public ClinicalCourseReferenceDto toReference() {
		return new ClinicalCourseReferenceDto(getUuid());
	}

	public HealthConditionsDto getHealthConditions() {
		return healthConditions;
	}

	public void setHealthConditions(HealthConditionsDto healthConditions) {
		this.healthConditions = healthConditions;
	}

}
