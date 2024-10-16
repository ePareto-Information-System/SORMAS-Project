package de.symeda.sormas.app.backend.ebs.ebsAlert;

import static de.symeda.sormas.api.EntityDto.NO_LAST_SYNCED_UUID;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.EbsAlertDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class EbsAlertDtoHelper extends AdoDtoHelper<EbsAlert, EbsAlertDto> {
    private LocationDtoHelper locationHelper;

    private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

    public EbsAlertDtoHelper() {
        locationHelper = new LocationDtoHelper();
    }

    @Override
    protected Class<EbsAlert> getAdoClass() {
        return EbsAlert.class;
    }

    @Override
    protected Class<EbsAlertDto> getDtoClass() {
        return EbsAlertDto.class;
    }

    @Override
    protected Call<List<EbsAlertDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
        return RetroProvider.getEbsAlertFacade().pullAllSince(since, 500, NO_LAST_SYNCED_UUID);
    }

    @Override
    protected Call<List<EbsAlertDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        return RetroProvider.getEbsAlertFacade().pullByUuids(uuids);
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<EbsAlertDto> ebsAlertDtos) throws NoConnectionException {
        return RetroProvider.getEbsAlertFacade().pushAll(ebsAlertDtos);
    }

    @Override
    public void fillInnerFromDto(EbsAlert target, EbsAlertDto source) {
        target.setResponseDate(source.getResponseDate());
        target.setResponseStatus(source.getResponseStatus());
        target.setAlertIssued(source.getAlertIssued());
        target.setDetailsAlertUsed(source.getDetailsAlertUsed());
        target.setDetailsResponseActivities(source.getDetailsResponseActivities());
        target.setDetailsGiven(source.getDetailsGiven());
        target.setActionInitiated(source.getActionInitiated());
        target.setAlertDate(source.getAlertDate());
        target.setEbs(DatabaseHelper.getEbsDao().getByReferenceDto(source.getEbs()));
    }

    @Override
    public void fillInnerFromAdo(EbsAlertDto target, EbsAlert source) {
        target.setActionInitiated(source.getActionInitiated());
        target.setResponseDate(source.getResponseDate());
        target.setResponseStatus(source.getResponseStatus());
        target.setAlertIssued(source.getAlertIssued());
        target.setDetailsAlertUsed(source.getDetailsAlertUsed());
        target.setDetailsResponseActivities(source.getDetailsResponseActivities());
        target.setDetailsGiven(source.getDetailsGiven());
        target.setAlertDate(source.getAlertDate());
        target.setEbs(EbsAlertDtoHelper.toReferenceDto(source.getEbs()));
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return EbsAlertDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }

    public static EbsReferenceDto toReferenceDto(Ebs ado) {
        if (ado == null) {
            return null;
        }
        EbsReferenceDto dto = new EbsReferenceDto(ado.getUuid());

        return dto;
    }
}
