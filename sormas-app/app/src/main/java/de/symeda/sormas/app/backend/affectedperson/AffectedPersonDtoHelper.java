package de.symeda.sormas.app.backend.affectedperson;

import java.util.List;
import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;
import de.symeda.sormas.api.PostResponse;


public class AffectedPersonDtoHelper extends AdoDtoHelper<AffectedPerson, AffectedPersonDto> {

    public AffectedPersonDtoHelper() {
    }

    @Override
    protected Class<AffectedPerson> getAdoClass() {
        return AffectedPerson.class;
    }

    @Override
    protected Class<AffectedPersonDto> getDtoClass() {
        return AffectedPersonDto.class;
    }

    @Override
    protected Call<List<AffectedPersonDto>> pullAllSince(long since,
                                                         Integer size,
                                                         String lastSynchronizedUuid) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<AffectedPersonDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<AffectedPersonDto> exposureDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected void fillInnerFromDto(AffectedPerson target, AffectedPersonDto source) {
        target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
        target.setTelNo(source.getTelNo());
        target.setDateTime(source.getDateTime());
        target.setAge(source.getAge());
    }

    @Override
    protected void fillInnerFromAdo(AffectedPersonDto target, AffectedPerson source) {
        target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
        target.setTelNo(source.getTelNo());
        target.setDateTime(source.getDateTime());
        target.setAge(source.getAge());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }

}
