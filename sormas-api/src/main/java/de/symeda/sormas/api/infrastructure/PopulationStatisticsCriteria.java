package de.symeda.sormas.api.infrastructure;

import java.util.List;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.Sex;


public interface PopulationStatisticsCriteria {
	public List<Sex> getSexes();
	
	public List<AgeGroup> getAgeGroups();
	
	public List<RegionReferenceDto> getRegions();

	public List<DistrictReferenceDto> getDistricts();
}
