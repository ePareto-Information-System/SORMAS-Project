package de.symeda.sormas.backend.disease;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_TEXT;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.audit.AuditIgnore;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.InfrastructureAdo;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.user.UserRole;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = DiseaseConfiguration.TABLE_NAME)
@AuditIgnore(retainWrites = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiseaseConfiguration extends InfrastructureAdo {

	private static final long serialVersionUID = -7653585175036656526L;

	public static final String TABLE_NAME = "diseaseconfiguration";

	//public static final String DISEASE = "disease";

	public static final String UUID = "uuid";
	public static final String DISEASE = "disease";
	public static final String PRIMARY_DISEASE = "primaryDisease";
	public static final String CASE_SURVEILLANCE_ENABLED = "caseSurveillanceEnabled";
	public static final String ACTIVE = "active";
	public static final String CASE_BASED = "caseBased";
	public static final String FOLLOW_UP_ENABLED = "followUpEnabled";
	public static final String FOLLOW_UP_DURATION = "followUpDuration";
	public static final String CASE_FOLLOW_UP_DURATION = "caseFollowUpDuration";
	public static final String EVENT_PARTICIPANT_FOLLOW_UP_DURATION = "eventParticipantFollowUpDuration";
	public static final String EXTENDED_CLASSIFICATION = "extendedClassification";
	public static final String EXTENDED_CLASSIFICATION_MULTI = "extendedClassificationMulti";
	public static final String AGE_GROUPS = "ageGroups";
	public static final String DISEASE_NAME = "diseaseName";

	private Disease disease;
	private Boolean active;
	private Boolean primaryDisease;
	private Boolean caseSurveillanceEnabled;
	private Boolean aggregateReportingEnabled;
	private Boolean followUpEnabled;
	private Integer followUpDuration;
	private Integer caseFollowUpDuration;
	private Integer eventParticipantFollowUpDuration;
	private Boolean extendedClassification;
	private Boolean extendedClassificationMulti;
	private List<String> ageGroups;

	public static DiseaseConfiguration build(Disease disease) {
		DiseaseConfiguration configuration = new DiseaseConfiguration();
		configuration.setDisease(disease);
		return configuration;
	}

	@Override
	public String getUuid() {
		return super.getUuid();
	}

	@Override
	public void setUuid(String uuid) {
		super.setUuid(uuid);
	}

	@Enumerated(EnumType.STRING)
	@Column(unique = true)
	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	@Column
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column
	public Boolean getPrimaryDisease() {
		return primaryDisease;
	}

	public void setPrimaryDisease(Boolean primaryDisease) {
		this.primaryDisease = primaryDisease;
	}

	@Column
	public Boolean getCaseSurveillanceEnabled() {
		return caseSurveillanceEnabled;
	}

	public void setCaseSurveillanceEnabled(Boolean caseBased) {
		this.caseSurveillanceEnabled = caseBased;
	}

	@Column
	public Boolean getAggregateReportingEnabled() {
		return aggregateReportingEnabled;
	}

	public void setAggregateReportingEnabled(Boolean aggregateReportingEnabled) {
		this.aggregateReportingEnabled = aggregateReportingEnabled;
	}

	@Column
	public Boolean getFollowUpEnabled() {
		return followUpEnabled;
	}

	public void setFollowUpEnabled(Boolean followUpEnabled) {
		this.followUpEnabled = followUpEnabled;
	}

	@Column
	public Integer getFollowUpDuration() {
		return followUpDuration;
	}

	public void setFollowUpDuration(Integer followUpDuration) {
		this.followUpDuration = followUpDuration;
	}

	@Column
	public Integer getCaseFollowUpDuration() {
		return caseFollowUpDuration;
	}

	public void setCaseFollowUpDuration(Integer caseFollowUpDuration) {
		this.caseFollowUpDuration = caseFollowUpDuration;
	}

	@Column
	public Integer getEventParticipantFollowUpDuration() {
		return eventParticipantFollowUpDuration;
	}

	public void setEventParticipantFollowUpDuration(Integer eventParticipantFollowUpDuration) {
		this.eventParticipantFollowUpDuration = eventParticipantFollowUpDuration;
	}

	@Column
	public Boolean getExtendedClassification() {
		return extendedClassification;
	}

	public void setExtendedClassification(Boolean extendedClassification) {
		this.extendedClassification = extendedClassification;
	}

	@Column
	public Boolean getExtendedClassificationMulti() {
		return extendedClassificationMulti;
	}

	public void setExtendedClassificationMulti(Boolean extendedClassificationMulti) {
		this.extendedClassificationMulti = extendedClassificationMulti;
	}

	@Column(length = CHARACTER_LIMIT_TEXT)
	@Convert(converter = AgeGroupsConverter.class)
	public List<String> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<String> ageGroups) {
		this.ageGroups = ageGroups;
	}

	private Set<Facility> facilities = new HashSet<>();
	public static final String TABLE_NAME_FACILITY_DISEASE = "facility_diseaseconfiguration";

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_NAME_FACILITY_DISEASE,
			joinColumns = @JoinColumn(name = "diseaseconfiguration_id"),
			inverseJoinColumns = @JoinColumn(name = "facility_id"))
	public Set<Facility> getFacilities() {
		return facilities;
	}
	public void setFacilities(Set<Facility> facilities) {
		this.facilities = facilities;
	}
}