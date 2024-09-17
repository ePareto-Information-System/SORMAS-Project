package de.symeda.sormas.app.backend.ebs;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.event.Event;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class EbsDtoHelper extends AdoDtoHelper<Ebs, EbsDto> {
    private LocationDtoHelper locationHelper;

    private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

    public EbsDtoHelper() {
        locationHelper = new LocationDtoHelper();
    }

    @Override
    protected Class<Ebs> getAdoClass() {
        return Ebs.class;
    }

    @Override
    protected Class<EbsDto> getDtoClass() {
        return EbsDto.class;
    }

    @Override
    protected Call<List<EbsDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
        return RetroProvider.getEbsFacade().pullAllSince(since, size, lastSynchronizedUuid);
    }

    @Override
    protected Call<List<EbsDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        return RetroProvider.getEbsFacade().pullByUuids(uuids);
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<EbsDto> ebsDtos) throws NoConnectionException {
        return RetroProvider.getEbsFacade().pushAll(ebsDtos);
    }

    @Override
    public void fillInnerFromDto(Ebs target, EbsDto source) {
        target.setInformantName(source.getInformantName());
        target.setInformantTel(source.getInformantTel());
        target.setEndDate(source.getEndDate());
        target.setReportDateTime(source.getReportDateTime());
        target.setCategoryOfInformant(source.getCategoryOfInformant());
        target.setEbsLatLon(source.getEbsLatLon());
        target.setAutomaticScanningType(source.getAutomaticScanningType());
        target.setManualScanningType(source.getManualScanningType());
        target.setScanningType(source.getScanningType());
        target.setDescriptionOccurrence(source.getDescriptionOccurrence());
        target.setOther(source.getOther());
        target.setPersonDesignation(source.getPersonDesignation());
        target.setPersonPhone(source.getPersonPhone());
        target.setPersonRegistering(source.getPersonRegistering());
        target.setSourceName(source.getSourceName());
        target.setSourceInformation(source.getSourceInformation());
        target.setDateOnset(source.getDateOnset());
        target.setEbsLongitude(source.getEbsLongitude());
        target.setEbsLatitude(source.getEbsLongitude());
        target.setOtherInformant(source.getOtherInformant());
    }

    @Override
    public void fillInnerFromAdo(EbsDto target, Ebs source) {
        target.setInformantName(source.getInformantName());
        target.setInformantTel(source.getInformantTel());
        target.setEndDate(source.getEndDate());
        target.setReportDateTime(source.getReportDateTime());
        target.setCategoryOfInformant(source.getCategoryOfInformant());
        target.setEbsLatLon(source.getEbsLatLon());
        target.setAutomaticScanningType(source.getAutomaticScanningType());
        target.setManualScanningType(source.getManualScanningType());
        target.setScanningType(source.getScanningType());
        target.setDescriptionOccurrence(source.getDescriptionOccurrence());
        target.setOther(source.getOther());
        target.setPersonDesignation(source.getPersonDesignation());
        target.setPersonPhone(source.getPersonPhone());
        target.setPersonRegistering(source.getPersonRegistering());
        target.setSourceName(source.getSourceName());
        target.setSourceInformation(source.getSourceInformation());
        target.setDateOnset(source.getDateOnset());
        target.setEbsLongitude(source.getEbsLongitude());
        target.setEbsLatitude(source.getEbsLongitude());
        target.setOtherInformant(source.getOtherInformant());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return EbsDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }

    public static EbsReferenceDto toReferenceDto(Event ado) {
        if (ado == null) {
            return null;
        }
        EbsReferenceDto dto = new EbsReferenceDto(ado.getUuid());

        return dto;
    }

}
