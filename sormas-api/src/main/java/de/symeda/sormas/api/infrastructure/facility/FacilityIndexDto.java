/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.api.infrastructure.facility;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import de.symeda.sormas.api.Disease;
//import de.symeda.sormas.api.HasUuid;
import de.symeda.sormas.api.disease.DiseaseConfigurationDto;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.Diseases;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.uuid.AbstractUuidDto;
import de.symeda.sormas.api.utils.AFPFacilityOptions;
import org.apache.commons.lang3.StringUtils;

public class FacilityIndexDto extends AbstractUuidDto {

	public static final String I18N_PREFIX = "Facility";

	public static final String NAME = "name";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String POSTAL_CODE = "postalCode";
	public static final String CITY = "city";
	public static final String STREET = "street";
	public static final String HOUSE_NUMBER = "houseNumber";
	public static final String ADDITIONAL_INFORMATION = "additionalInformation";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String TYPE = "type";
	public static final String EXTERNAL_ID = "externalID";
	public static final String DISEASES = "diseases";

	private String name;
	private FacilityType type;
	private DhimsFacility dhimsFacilityType;
	private AFPFacilityOptions afpFacilityOptions;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private CommunityReferenceDto community;
	private String postalCode;
	private String city;
	private String street;
	private String houseNumber;
	private String additionalInformation;
	private Double latitude;
	private Double longitude;
	private String externalID;
	private String diseases;
	private String uuid;

	public FacilityIndexDto(
			String uuid,
			String name,
			FacilityType type,
			String regionUuid,
			String regionName,
			String districtUuid,
			String districtName,
			String communityUuid,
			String communityName,
			String postalCode,
			String city,
			String street,
			String houseNumber,
			String additionalInformation,
			Double latitude,
			Double longitude,
			String externalID) {

		super(uuid);
		this.uuid =	uuid;
		this.name = name;
		this.type = type;
		if (regionUuid != null) {
			this.region = new RegionReferenceDto(regionUuid, regionName, null);
		}
		if (districtUuid != null) {
			this.district = new DistrictReferenceDto(districtUuid, districtName, null);
		}
		if (communityUuid != null) {
			this.community = new CommunityReferenceDto(communityUuid, communityName, null);
		}
		this.postalCode = postalCode;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
		this.additionalInformation = additionalInformation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.externalID = externalID;
	}

	public FacilityIndexDto(
			String uuid,
			String name,
			FacilityType type,
			String regionUuid,
			String regionName,
			String districtUuid,
			String districtName,
			String communityUuid,
			String communityName,
			String city,
			Double latitude,
			Double longitude,
			String externalID) {
		super(uuid);

//		this.uuid = uuid;
		this.name = name;
		this.type = type;
		this.dhimsFacilityType = dhimsFacilityType;
		this.afpFacilityOptions = afpFacilityOptions;
		if (regionUuid != null) {
			this.region = new RegionReferenceDto(regionUuid, regionName, null);
		}
		if (districtUuid != null) {
			this.district = new DistrictReferenceDto(districtUuid, districtName, null);
		}
		if (communityUuid != null) {
			this.community = new CommunityReferenceDto(communityUuid, communityName, null);
		}
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.externalID = externalID;

	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FacilityType getType() {
		return type;
	}

	public void setType(FacilityType type) {
		this.type = type;
	}
	public DhimsFacility getDhimsFacilityType() {
		return dhimsFacilityType;
	}

	public void setDhimsFacilityType(DhimsFacility dhimsFacilityType) {
		this.dhimsFacilityType = dhimsFacilityType;
	}
	public AFPFacilityOptions getAfpFacilityOptions() {
		return afpFacilityOptions;
	}

	public void setAfpFacilityOptions(AFPFacilityOptions afpFacilityOptions) {
		this.afpFacilityOptions = afpFacilityOptions;
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

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public void setCommunity(CommunityReferenceDto community) {
		this.community = community;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	public String getCaption() {
		return FacilityHelper.buildFacilityString(getUuid(), name);
	}

	@Override
	public String toString() {
		return I18N_PREFIX + StringUtils.SPACE + getUuid();
	}

	public FacilityReferenceDto toReference() {
		return new FacilityReferenceDto(getUuid(), getCaption(), getExternalID());
	}
	public String getDiseases() {
		return diseases;
	}

	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}



}