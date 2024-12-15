package de.symeda.sormas.app.backend.patientsymptomsprecedence;

import java.util.List;
import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.riskfactor.PatientSymptomsPrecedenceDto;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;

public class PatientSymptomsPrecedenceDtoHelper extends AdoDtoHelper<PatientSymptomsPrecedence, PatientSymptomsPrecedenceDto> {

    public PatientSymptomsPrecedenceDtoHelper() {
    }

    @Override
    protected Class<PatientSymptomsPrecedence> getAdoClass() {
        return PatientSymptomsPrecedence.class;
    }

    @Override
    protected Class<PatientSymptomsPrecedenceDto> getDtoClass() {
        return PatientSymptomsPrecedenceDto.class;
    }

    @Override
    protected Call<List<PatientSymptomsPrecedenceDto>> pullAllSince(long since,
                                                              Integer size,
                                                              String lastSynchronizedUuid) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PatientSymptomsPrecedenceDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<PatientSymptomsPrecedenceDto> patientSymptomsPrecedenceDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected void fillInnerFromDto(PatientSymptomsPrecedence target, PatientSymptomsPrecedenceDto source) {
        target.setName(source.getName());
        target.setContactAddress(source.getContactAddress());
        target.setPhone(source.getPhone());
    }

    @Override
    protected void fillInnerFromAdo(PatientSymptomsPrecedenceDto target, PatientSymptomsPrecedence source) {
        target.setName(source.getName());
        target.setContactAddress(source.getContactAddress());
        target.setPhone(source.getPhone());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
