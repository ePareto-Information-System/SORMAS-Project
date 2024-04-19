package de.symeda.sormas.api.dashboard;

import de.symeda.sormas.api.CaseMeasure;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;
import de.symeda.sormas.api.utils.criteria.CriteriaWithDateType;
import java.util.Date;

//public class DashboardCriteria extends CriteriaWithDateType implements Serializable {
public class DashboardCriteria extends BaseDashboardCriteria<DashboardCriteria> {

	private CriteriaDateType newCaseDateType;
	private EpiCurveGrouping epiCurveGrouping;
	private Boolean showMinimumEntries;
	private CaseMeasure caseMeasure;
	private CaseClassification caseClassification;
	private NewDateFilterType dateFilterType;

	private Boolean includeNotACaseClassification;

	public DashboardCriteria(Class<DashboardCriteria> dashboardCriteriaClass) {
		super(dashboardCriteriaClass);
	}

//	public DashboardCriteria(Class<? extends CriteriaDateType> dateTypeCalss) {
//		super(dateTypeCalss);
//	}

	public DashboardCriteria() {
		super(DashboardCriteria.class);
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
		//return self;
	}

	public boolean shouldIncludeNotACaseClassification() {
		return includeNotACaseClassification;
	}

	public DashboardCriteria includeNotACaseClassification(boolean includeNotACaseClassification) {
		this.includeNotACaseClassification = includeNotACaseClassification;
		return self;
	}

	public EpiCurveGrouping getEpiCurveGrouping() {
		return epiCurveGrouping;
	}

	public void setEpiCurveGrouping(EpiCurveGrouping epiCurveGrouping) {
		this.epiCurveGrouping = epiCurveGrouping;
	}

	public Boolean isIncludeNotACaseClassification() {
		return includeNotACaseClassification;
	}

	public Boolean isShowMinimumEntries() {
		return showMinimumEntries;
	}

	public void setShowMinimumEntries(Boolean showMinimumEntries) {
		this.showMinimumEntries = showMinimumEntries;
	}

	public CaseMeasure getCaseMeasure() {
		return caseMeasure;
	}
	public DashboardCriteria previousDateTo(Date previousDateTo) {
		this.previousDateTo = previousDateTo;
		return this;
	}

	public DashboardCriteria previousDateFrom(Date previousDateFrom) {
		this.previousDateFrom = previousDateFrom;
		return this;
	}//..




	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public void setPreviousDateFrom(Date previousDateFrom) {
		this.previousDateFrom = previousDateFrom;
	}

	public void setPreviousDateTo(Date previousDateTo) {
		this.previousDateTo = previousDateTo;
	}

	public void setCaseMeasure(CaseMeasure caseMeasure) {
		this.caseMeasure = caseMeasure;
	}

	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public void setIncludeNotACaseClassification(Boolean includeNotACaseClassification) {
		this.includeNotACaseClassification = includeNotACaseClassification;
	}

	public void setDateFilterType(NewDateFilterType dateFilterType) {
		this.dateFilterType = dateFilterType;
	}

	@Override
	public String toString() {
		return "DashboardCriteria [region=" + region + ", district=" + district + ", disease=" + disease
				+ ", newCaseDateType=" + newCaseDateType + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo
				+ ", previousDateFrom=" + previousDateFrom + ", previousDateTo=" + previousDateTo
				+ ", epiCurveGrouping=" + epiCurveGrouping + ", showMinimumEntries=" + showMinimumEntries
				+ ", caseMeasure=" + caseMeasure + ", caseClassification=" + caseClassification + ", dateFilterType="
				+ dateFilterType + ", includeNotACaseClassification=" + includeNotACaseClassification + "]";
	}






}
