package de.symeda.sormas.app.backend.patienttraveldetailsprior;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsPriorDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;


public class PatientTravelDetailsPriorDtoHelper extends AdoDtoHelper<PatientTravelDetailsPrior, PatientTravelDetailsPriorDto> {

    public PatientTravelDetailsPriorDtoHelper() {
    }

    @Override
    protected Class<PatientTravelDetailsPrior> getAdoClass() {
        return PatientTravelDetailsPrior.class;
    }

    @Override
    protected Class<PatientTravelDetailsPriorDto> getDtoClass() {
        return PatientTravelDetailsPriorDto.class;
    }

    @Override
    protected Call<List<PatientTravelDetailsPriorDto>> pullAllSince(long since,
                                                         Integer size,
                                                         String lastSynchronizedUuid) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PatientTravelDetailsPriorDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<PatientTravelDetailsPriorDto> exposureDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected void fillInnerFromDto(PatientTravelDetailsPrior target, PatientTravelDetailsPriorDto source) {
        target.setDateOfTravel(source.getDateOfTravel());
        target.setPlaceOfTravel(source.getPlaceOfTravel());
    }

    @Override
    protected void fillInnerFromAdo(PatientTravelDetailsPriorDto target, PatientTravelDetailsPrior source) {
        target.setDateOfTravel(source.getDateOfTravel());
        target.setPlaceOfTravel(source.getPlaceOfTravel());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }

}
