package de.symeda.sormas.app.backend.ebs.signalVerification;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.ebs.SignalVerificationReferenceDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class SignalVerificationDtoHelper extends AdoDtoHelper<SignalVerification, SignalVerificationDto> {
    private LocationDtoHelper locationHelper;

    private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

    public SignalVerificationDtoHelper() {
        locationHelper = new LocationDtoHelper();
    }

    @Override
    protected Class<SignalVerification> getAdoClass() {
        return SignalVerification.class;
    }

    @Override
    protected Class<SignalVerificationDto> getDtoClass() {
        return SignalVerificationDto.class;
    }

    @Override
    protected Call<List<SignalVerificationDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
        return RetroProvider.getSignalVerificationFacade().pullAllSince(since, size, lastSynchronizedUuid);
    }

    @Override
    protected Call<List<SignalVerificationDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        return RetroProvider.getSignalVerificationFacade().pullByUuids(uuids);
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<SignalVerificationDto> signalVerificationDtos) throws NoConnectionException {
        return RetroProvider.getSignalVerificationFacade().pushAll(signalVerificationDtos);
    }

    @Override
    public void fillInnerFromDto(SignalVerification target, SignalVerificationDto source) {
        target.setVerificationSent(source.getVerificationSent());
        target.setVerified(source.getVerified());
        target.setVerificationCompleteDate(source.getVerificationCompleteDate());
        target.setDateOfOccurrence(source.getDateOfOccurrence());
        target.setNumberOfPersonAnimal(source.getNumberOfPersonAnimal());
        target.setNumberOfDeath(source.getNumberOfDeath());
        target.setDescription(source.getDescription());
        target.setWhyNotVerify(source.getWhyNotVerify());
        target.setNumberOfDeathPerson(source.getNumberOfDeathPerson());
        target.setNumberOfPersonCases(source.getNumberOfPersonCases());
    }

    @Override
    public void fillInnerFromAdo(SignalVerificationDto target, SignalVerification source) {
        target.setVerificationSent(source.getVerificationSent());
        target.setVerified(source.getVerified());
        target.setVerificationCompleteDate(source.getVerificationCompleteDate());
        target.setDateOfOccurrence(source.getDateOfOccurrence());
        target.setNumberOfPersonAnimal(source.getNumberOfPersonAnimal());
        target.setNumberOfDeath(source.getNumberOfDeath());
        target.setDescription(source.getDescription());
        target.setWhyNotVerify(source.getWhyNotVerify());
        target.setNumberOfDeathPerson(source.getNumberOfDeathPerson());
        target.setNumberOfPersonCases(source.getNumberOfPersonCases());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return SignalVerificationDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }

}
