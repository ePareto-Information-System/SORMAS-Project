package de.symeda.sormas.app.backend.contaminationsource;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.region.CommunityDtoHelper;
import de.symeda.sormas.app.backend.region.DistrictDtoHelper;
import de.symeda.sormas.app.backend.region.RegionDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class ContaminationSourceDtoHelper extends AdoDtoHelper<ContaminationSource, ContaminationSourceDto> {


	public ContaminationSourceDtoHelper() {
	}

	@Override
	protected Class<ContaminationSource> getAdoClass() {
		return ContaminationSource.class;
	}

	@Override
	protected Class<ContaminationSourceDto> getDtoClass() {
		return ContaminationSourceDto.class;
	}

	@Override
	protected Call<List<ContaminationSourceDto>> pullAllSince(long since,
														 Integer size,
														 String lastSynchronizedUuid) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<ContaminationSourceDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<ContaminationSourceDto> exposureDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected void fillInnerFromDto(ContaminationSource target, ContaminationSourceDto source) {
		target.setContaminationType(source.getContaminationType());
		target.setLongitude(source.getLongitude());
		target.setLatitude(source.getLatitude());
		target.setType(source.getType());
		target.setSource(source.getSource());
		target.setTreatedWithAbate(source.getTreatedWithAbate());
		target.setAbateTreatmentDate(source.getAbateTreatmentDate());

	}

	@Override
	protected void fillInnerFromAdo(ContaminationSourceDto target, ContaminationSource source) {
		target.setContaminationType(source.getContaminationType());
		target.setLongitude(source.getLongitude());
		target.setLatitude(source.getLatitude());
		target.setType(source.getType());
		target.setSource(source.getSource());
		target.setTreatedWithAbate(source.getTreatedWithAbate());
		target.setAbateTreatmentDate(source.getAbateTreatmentDate());
	}

	@Override
	protected long getApproximateJsonSizeInBytes() {
		return 0;
	}
}
