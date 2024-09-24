package de.symeda.sormas.app.backend.persontravelhistory;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.region.CommunityDtoHelper;
import de.symeda.sormas.app.backend.region.DistrictDtoHelper;
import de.symeda.sormas.app.backend.region.RegionDtoHelper;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.user.UserDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class PersonTravelHistoryDtoHelper extends AdoDtoHelper<PersonTravelHistory, PersonTravelHistoryDto> {


	public PersonTravelHistoryDtoHelper() {
	}

	@Override
	protected Class<PersonTravelHistory> getAdoClass() {
		return PersonTravelHistory.class;
	}

	@Override
	protected Class<PersonTravelHistoryDto> getDtoClass() {
		return PersonTravelHistoryDto.class;
	}

	@Override
	protected Call<List<PersonTravelHistoryDto>> pullAllSince(long since,
														 Integer size,
														 String lastSynchronizedUuid) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PersonTravelHistoryDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<PersonTravelHistoryDto> exposureDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected void fillInnerFromDto(PersonTravelHistory target, PersonTravelHistoryDto source) {

		target.setTravelPeriodType(source.getTravelPeriodType());
		target.setDateFrom(source.getDateFrom());
		target.setDateTo(source.getDateTo());
		target.setVillage(source.getVillage());
		if (source.getSubDistrict() != null) {
			target.setSubDistrict(DatabaseHelper.getCommunityDao().getByReferenceDto(source.getSubDistrict()));
		} else {
			target.setSubDistrict(null);
		}
		if (source.getDistrict() != null) {
			target.setDistrict(DatabaseHelper.getDistrictDao().getByReferenceDto(source.getDistrict()));
		} else {
			target.setDistrict(null);
		}
		if (source.getRegion() != null) {
			target.setRegion(DatabaseHelper.getRegionDao().getByReferenceDto(source.getRegion()));
		} else {
			target.setRegion(null);
		}

	}

	@Override
	protected void fillInnerFromAdo(PersonTravelHistoryDto target, PersonTravelHistory source) {

		target.setTravelPeriodType(source.getTravelPeriodType());
		target.setDateFrom(source.getDateFrom());
		target.setDateTo(source.getDateTo());
		target.setVillage(source.getVillage());
		if (source.getSubDistrict() != null) {
			target.setSubDistrict(CommunityDtoHelper.toReferenceDto(DatabaseHelper.getCommunityDao().queryForId(source.getSubDistrict().getId())));
		} else {
			target.setSubDistrict(null);
		}
		if (source.getDistrict() != null) {
			target.setDistrict(DistrictDtoHelper.toReferenceDto(DatabaseHelper.getDistrictDao().queryForId(source.getDistrict().getId())));
		} else {
			target.setDistrict(null);
		}
		if (source.getRegion() != null) {
			target.setRegion(RegionDtoHelper.toReferenceDto(DatabaseHelper.getRegionDao().queryForId(source.getRegion().getId())));
		} else {
			target.setRegion(null);
		}

	}

	@Override
	protected long getApproximateJsonSizeInBytes() {
		return 0;
	}
}
