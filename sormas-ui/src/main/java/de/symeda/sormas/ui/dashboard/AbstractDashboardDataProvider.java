/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.ui.dashboard;

import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.dashboard.DashboardCriteria;
import de.symeda.sormas.api.dashboard.NewDateFilterType;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.event.RiskLevel;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.ui.dashboard.components.DashboardFilterLayout;
import java.util.Date;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.dashboard.BaseDashboardCriteria;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;

public abstract class AbstractDashboardDataProvider<C extends BaseDashboardCriteria<C>> {

	protected Date fromDate;
	protected Date toDate;
	protected Date previousFromDate;
	protected Date previousToDate;
	protected RegionReferenceDto region;
	protected DistrictReferenceDto district;
	protected Disease disease;
	private DashboardType dashboardType;
	private CommunityReferenceDto community;

	private NewDateFilterType dateFilterType;
	private EbsSourceType eventSourceTypeForFilter;
	private SignalCategory signalCategory;
	private RiskLevel riskLevel;
	private EbsSourceType eventSourceType;
	private CaseClassification caseClassification;
	private NewCaseDateType newCaseDateType ;

	public abstract void refreshData();

	public C buildDashboardCriteriaWithDates() {
		return buildDashboardCriteria().dateBetween(fromDate, toDate);
	}

	protected C buildDashboardCriteria() {
		return newCriteria().region(region).district(district).disease(disease);
	}

	protected abstract C newCriteria();

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getPreviousFromDate() {
		return previousFromDate;
	}

	public void setPreviousFromDate(Date previousFromDate) {
		this.previousFromDate = previousFromDate;
	}

	public Date getPreviousToDate() {
		return previousToDate;
	}

	public void setPreviousToDate(Date previousToDate) {
		this.previousToDate = previousToDate;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}
	public DashboardType getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(DashboardType dashboardType) {
		this.dashboardType = dashboardType;
	}

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public void setCommunity(CommunityReferenceDto community) {
		this.community = community;
	}

	public NewDateFilterType getDateFilterType() {
		return dateFilterType;
	}

	public void setDateFilterType(NewDateFilterType dateFilterType) {
		this.dateFilterType = dateFilterType;
	}

	public EbsSourceType getEventSourceTypeForFilter() {
		return eventSourceTypeForFilter;
	}

	public void setEventSourceTypeForFilter(EbsSourceType eventSourceTypeForFilter) {
		this.eventSourceTypeForFilter = eventSourceTypeForFilter;
	}

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	public void setEventSourceType(EbsSourceType eventSourceType) {
		this.eventSourceType = eventSourceType;
	}

	public EbsSourceType getEventSourceType() {
		return eventSourceType;
	}


	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public NewCaseDateType getNewCaseDateType() {
		if (newCaseDateType == null) {
			return NewCaseDateType.MOST_RELEVANT;
		}
		return newCaseDateType;
	}

	public void setNewCaseDateType(NewCaseDateType newCaseDateType) {
		this.newCaseDateType = newCaseDateType;
	}
	public DashboardCriteria getCriteria() {
		return new DashboardCriteria().region(region)
				.district(district)
				.community(community)
				.sourceInformation(eventSourceType)
				.signalCategory(signalCategory)
				.riskLevel(riskLevel)
				.disease(disease)
				.dateBetween(fromDate, toDate)
				.caseClassification(caseClassification)
				.newCaseDateType(newCaseDateType)
				.dateFilterType(dateFilterType);
	}
}
