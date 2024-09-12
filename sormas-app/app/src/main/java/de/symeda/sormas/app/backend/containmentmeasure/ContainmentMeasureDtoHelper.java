package de.symeda.sormas.app.backend.containmentmeasure;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.epidata.ContainmentMeasureDto;
import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class ContainmentMeasureDtoHelper extends AdoDtoHelper<ContainmentMeasure, ContainmentMeasureDto> {


	public ContainmentMeasureDtoHelper() {
	}

	@Override
	protected Class<ContainmentMeasure> getAdoClass() {
		return ContainmentMeasure.class;
	}

	@Override
	protected Class<ContainmentMeasureDto> getDtoClass() {
		return ContainmentMeasureDto.class;
	}

	@Override
	protected Call<List<ContainmentMeasureDto>> pullAllSince(long since,
														 Integer size,
														 String lastSynchronizedUuid) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<ContainmentMeasureDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<ContainmentMeasureDto> exposureDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected void fillInnerFromDto(ContainmentMeasure target, ContainmentMeasureDto source) {
		target.setLocationOfWorm(source.getLocationOfWorm());
		target.setDateWormDetectedEmergence(source.getDateWormDetectedEmergence());
		target.setDateWormDetectBySupervisor(source.getDateWormDetectBySupervisor());
		target.setDateConfirmed(source.getDateConfirmed());
		target.setDateOfGuineaWormExpelled(source.getDateOfGuineaWormExpelled());
		target.setRegularBandaging(source.getRegularBandaging());
		target.setCompletelyExtracted(source.getCompletelyExtracted());
	}

	@Override
	protected void fillInnerFromAdo(ContainmentMeasureDto target, ContainmentMeasure source) {
		target.setLocationOfWorm(source.getLocationOfWorm());
		target.setDateWormDetectedEmergence(source.getDateWormDetectedEmergence());
		target.setDateWormDetectBySupervisor(source.getDateWormDetectBySupervisor());
		target.setDateConfirmed(source.getDateConfirmed());
		target.setDateOfGuineaWormExpelled(source.getDateOfGuineaWormExpelled());
		target.setRegularBandaging(source.getRegularBandaging());
		target.setCompletelyExtracted(source.getCompletelyExtracted());
	}

	@Override
	protected long getApproximateJsonSizeInBytes() {
		return 0;
	}
}
