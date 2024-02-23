package de.symeda.sormas.api.infrastructure.diseasecon;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.infrastructure.InfrastructureDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;

import java.util.List;

@DependingOnFeatureType(featureType = {
		FeatureType.CASE_SURVEILANCE,
		FeatureType.EVENT_SURVEILLANCE,
		FeatureType.AGGREGATE_REPORTING })
public class DiseaseConDto extends InfrastructureDto {

	private static final long serialVersionUID = -7653585175036656526L;

	public static final String I18N_PREFIX = "DiseaseConfiguration";

	private Disease disease;
	private Boolean active;
	private Boolean primaryDisease;
	private Boolean caseBased;
	private Boolean followUpEnabled;
	private Integer followUpDuration;
	private Integer caseFollowUpDuration;
	private Integer eventParticipantFollowUpDuration;
	private Boolean extendedClassification;
	private Boolean extendedClassificationMulti;
	private List<String> ageGroups;

	public DiseaseConDto(){}

	public DiseaseConDto(Disease disease,
						 Boolean active,
						 Boolean caseBased,
						 Boolean followUpEnabled,
						 Integer followUpDuration,
						 Integer eventParticipantFollowUpDuration,
						 Boolean extendedClassification,
						 Boolean extendedClassificationMulti,
						 List<String> ageGroups) {
		this.disease = disease;
		this.active = active;
		this.caseBased = caseBased;
		this.followUpEnabled = followUpEnabled;
		this.followUpDuration = followUpDuration;
		this.eventParticipantFollowUpDuration = eventParticipantFollowUpDuration;
		this.extendedClassification = extendedClassification;
		this.extendedClassificationMulti = extendedClassificationMulti;
		this.ageGroups = ageGroups;
	}

	private List<FacilityReferenceDto> facilities;

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getPrimaryDisease() {
		return primaryDisease;
	}

	public void setPrimaryDisease(Boolean primaryDisease) {
		this.primaryDisease = primaryDisease;
	}

	public Boolean getCaseBased() {
		return caseBased;
	}

	public void setCaseBased(Boolean caseBased) {
		this.caseBased = caseBased;
	}

	public Boolean getFollowUpEnabled() {
		return followUpEnabled;
	}

	public void setFollowUpEnabled(Boolean followUpEnabled) {
		this.followUpEnabled = followUpEnabled;
	}

	public Integer getFollowUpDuration() {
		return followUpDuration;
	}

	public void setFollowUpDuration(Integer followUpDuration) {
		this.followUpDuration = followUpDuration;
	}

	public Integer getCaseFollowUpDuration() {
		return caseFollowUpDuration;
	}

	public void setCaseFollowUpDuration(Integer caseFollowUpDuration) {
		this.caseFollowUpDuration = caseFollowUpDuration;
	}

	public Integer getEventParticipantFollowUpDuration() {
		return eventParticipantFollowUpDuration;
	}

	public void setEventParticipantFollowUpDuration(Integer eventParticipantFollowUpDuration) {
		this.eventParticipantFollowUpDuration = eventParticipantFollowUpDuration;
	}

	public Boolean getExtendedClassification() {
		return extendedClassification;
	}

	public void setExtendedClassification(Boolean extendedClassification) {
		this.extendedClassification = extendedClassification;
	}

	public Boolean getExtendedClassificationMulti() {
		return extendedClassificationMulti;
	}

	public void setExtendedClassificationMulti(Boolean extendedClassificationMulti) {
		this.extendedClassificationMulti = extendedClassificationMulti;
	}

	public List<String> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<String> ageGroups) {
		this.ageGroups = ageGroups;
	}

	public List<FacilityReferenceDto> getFacilities() {
		return facilities;
	}
	public void setFacilities(List<FacilityReferenceDto> diseaseFacilities) {
		this.facilities = diseaseFacilities;
	}

	public static DiseaseConDto build() {
		DiseaseConDto dto = new DiseaseConDto();
		dto.setUuid(DataHelper.createUuid());
		return dto;
	}
}
