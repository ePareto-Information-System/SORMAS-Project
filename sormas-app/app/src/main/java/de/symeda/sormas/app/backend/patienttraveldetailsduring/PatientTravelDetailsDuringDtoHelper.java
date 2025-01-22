package de.symeda.sormas.app.backend.patienttraveldetailsduring;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsDuringDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;


public class PatientTravelDetailsDuringDtoHelper extends AdoDtoHelper<PatientTravelDetailsDuring, PatientTravelDetailsDuringDto> {

    public PatientTravelDetailsDuringDtoHelper() {
    }

    @Override
    protected Class<PatientTravelDetailsDuring> getAdoClass() {
        return PatientTravelDetailsDuring.class;
    }

    @Override
    protected Class<PatientTravelDetailsDuringDto> getDtoClass() {
        return PatientTravelDetailsDuringDto.class;
    }

    @Override
    protected Call<List<PatientTravelDetailsDuringDto>> pullAllSince(long since,
                                                         Integer size,
                                                         String lastSynchronizedUuid) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PatientTravelDetailsDuringDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<PatientTravelDetailsDuringDto> exposureDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected void fillInnerFromDto(PatientTravelDetailsDuring target, PatientTravelDetailsDuringDto source) {
        target.setDateOfTravel(source.getDateOfTravel());
        target.setPlaceOfTravel(source.getPlaceOfTravel());
    }

    @Override
    protected void fillInnerFromAdo(PatientTravelDetailsDuringDto target, PatientTravelDetailsDuring source) {
        target.setDateOfTravel(source.getDateOfTravel());
        target.setPlaceOfTravel(source.getPlaceOfTravel());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }

}
