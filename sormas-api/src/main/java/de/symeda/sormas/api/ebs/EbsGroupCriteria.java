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
package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.DateFilterOption;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class EbsGroupCriteria extends BaseCriteria implements Serializable {

	private static final long serialVersionUID = 2194071029732246594L;

	public static final String EBS = "ebs";

	public static final String FREE_TEXT = "freeText";
	public static final String FREE_TEXT_EBS = "freeTextEbs";
	public static final String DISTRICT = "district";
	public static final String REGION = "region";
	public static final String COMMUNITY = "community";

	private EbsReferenceDto ebs;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private CommunityReferenceDto community;
	private Date ebsDateFrom;
	private Date ebsDateTo;
	private DateFilterOption dateFilterOption = DateFilterOption.DATE;
	private String freeText;
	private String freeTextEbs;
	private Boolean userFilterIncluded = true;
	private Set<String> excludedUuids;
	private EntityRelevanceStatus relevanceStatus;

	public EbsReferenceDto getEbs() {
		return ebs;
	}

	public void setEbs(EbsReferenceDto ebs) {
		this.ebs = ebs;
	}

	public EbsGroupCriteria ebs(EbsReferenceDto ebs) {
		this.ebs = ebs;
		return this;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public EbsGroupCriteria region(RegionReferenceDto region) {
		this.region = region;
		return this;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public EbsGroupCriteria district(DistrictReferenceDto district) {
		this.district = district;
		return this;
	}

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public void setCommunity(CommunityReferenceDto community) {
		this.community = community;
	}

	public EbsGroupCriteria community(CommunityReferenceDto community) {
		this.community = community;
		return this;
	}

	public Date getEbsDateFrom() {
		return ebsDateFrom;
	}

	public void setEbsDateFrom(Date ebsDateFrom) {
		this.ebsDateFrom = ebsDateFrom;
	}

	public EbsGroupCriteria ebsDateFrom(Date ebsDateFrom) {
		this.ebsDateFrom = ebsDateFrom;
		return this;
	}

	public Date getEbsDateTo() {
		return ebsDateTo;
	}

	public void setEbsDateTo(Date ebsDateTo) {
		this.ebsDateTo = ebsDateTo;
	}

	public EbsGroupCriteria ebsDateTo(Date ebsDateTo) {
		this.ebsDateTo = ebsDateTo;
		return this;
	}

	public DateFilterOption getDateFilterOption() {
		return dateFilterOption;
	}

	public void setDateFilterOption(DateFilterOption dateFilterOption) {
		this.dateFilterOption = dateFilterOption;
	}

	public EbsGroupCriteria dateFilterOption(DateFilterOption dateFilterOption) {
		this.dateFilterOption = dateFilterOption;
		return this;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public EbsGroupCriteria freeText(String freeText) {
		this.freeText = freeText;
		return this;
	}

	public String getFreeTextEbs() {
		return freeTextEbs;
	}

	public void setFreeTextEbs(String freeTextEbs) {
		this.freeTextEbs = freeTextEbs;
	}

	public EbsGroupCriteria freeTextEbs(String freeTextEbs) {
		this.freeTextEbs = freeTextEbs;
		return this;
	}

	public Set<String> getExcludedUuids() {
		return excludedUuids;
	}

	public void setExcludedUuids(Set<String> excludedUuids) {
		this.excludedUuids = excludedUuids;
	}

	public EbsGroupCriteria excludedUuids(Set<String> excludedUuids) {
		this.excludedUuids = excludedUuids;
		return this;
	}

	public Boolean getUserFilterIncluded() {
		return userFilterIncluded;
	}

	public void setUserFilterIncluded(Boolean userFilterIncluded) {
		this.userFilterIncluded = userFilterIncluded;
	}

	public EbsGroupCriteria userFilterIncluded(Boolean userFilterIncluded) {
		this.userFilterIncluded = userFilterIncluded;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}

	public void setRelevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
	}

	public EbsGroupCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}
}
