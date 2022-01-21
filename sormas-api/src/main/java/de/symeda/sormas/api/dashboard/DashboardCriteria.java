package de.symeda.sormas.api.dashboard;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;
import de.symeda.sormas.api.utils.criteria.CriteriaWithDateType;

public class DashboardCriteria extends CriteriaWithDateType implements Serializable {

	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private Disease disease;
	private CriteriaDateType newCaseDateType;
	private Date dateFrom;
	private Date dateTo;
	private Date previousDateFrom;
	private Date previousDateTo;
	private CaseClassification caseClassification;
	private NewDateFilterType dateFilterType;

	private boolean includeNotACaseClassification;

	public DashboardCriteria(Class<? extends CriteriaDateType> dateTypeCalss) {
		super(dateTypeCalss);
	}

	public DashboardCriteria() {
		super(CriteriaDateType.class);
	}

	public NewDateFilterType getDateFilterType() {
		return dateFilterType;
	}

	public DashboardCriteria dateFilterType(NewDateFilterType dateFilterType) {
		this.dateFilterType = dateFilterType;
		return this;
	}

	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	public DashboardCriteria caseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
		return this;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public DashboardCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public DashboardCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}

	public Disease getDisease() {
		return disease;
	}

	public DashboardCriteria disease(Disease disease) {
		this.disease = disease;
		return this;
	}

	public void setNewCaseDateType(CriteriaDateType newCaseDateType) {
		this.newCaseDateType = newCaseDateType;
	}

	public CriteriaDateType getNewCaseDateType() {
		return newCaseDateType;
	}

	public DashboardCriteria newCaseDateType(CriteriaDateType newCaseDateType) {
		this.newCaseDateType = newCaseDateType;
		return this;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public DashboardCriteria dateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public DashboardCriteria dateTo(Date dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public DashboardCriteria dateBetween(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		return this;
	}

	public boolean shouldIncludeNotACaseClassification() {
		return includeNotACaseClassification;
	}

	public DashboardCriteria includeNotACaseClassification(boolean includeNotACaseClassification) {
		this.includeNotACaseClassification = includeNotACaseClassification;
		return this;
	}

	public Date getPreviousDateFrom() {
		return previousDateFrom;
	}

	public Date getPreviousDateTo() {
		return previousDateTo;
	}

	public DashboardCriteria previousDateTo(Date previousDateTo) {
		this.previousDateTo = previousDateTo;
		return this;
	}

	public DashboardCriteria previousDateFrom(Date previousDateFrom) {
		this.previousDateFrom = previousDateFrom;
		return this;
	}
}
