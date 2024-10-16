package de.symeda.sormas.app.backend.ebs.triaging;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.ebs.TriagingDto;
import de.symeda.sormas.api.ebs.TriagingReferenceDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.event.Event;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class TriagingDtoHelper extends AdoDtoHelper<Triaging, TriagingDto> {
    private LocationDtoHelper locationHelper;

    private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

    public TriagingDtoHelper() {
        locationHelper = new LocationDtoHelper();
    }

    @Override
    protected Class<Triaging> getAdoClass() {
        return Triaging.class;
    }

    @Override
    protected Class<TriagingDto> getDtoClass() {
        return TriagingDto.class;
    }

    @Override
    protected Call<List<TriagingDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
        return RetroProvider.getTriagingFacade().pullAllSince(since, size, lastSynchronizedUuid);
    }

    @Override
    protected Call<List<TriagingDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        return RetroProvider.getTriagingFacade().pullByUuids(uuids);
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<TriagingDto> triagingDtos) throws NoConnectionException {
        return RetroProvider.getTriagingFacade().pushAll(triagingDtos);
    }

    @Override
    public void fillInnerFromDto(Triaging target, TriagingDto source) {
        target.setSupervisorReview(source.getSupervisorReview());
        target.setDecisionDate(source.getDecisionDate());
        target.setHealthConcern(source.getHealthConcern());
        target.setReferredTo(source.getReferredTo());
        target.setOccurrencePreviously(source.getOccurrencePreviously());
        target.setSpecificSignal(source.getSpecificSignal());
        target.setSignalCategory(source.getSignalCategory());
        target.setHumanCommunityCategoryDetails(source.getHumanCommunityCategoryDetails());
        target.setHumanFacilityCategoryDetails(source.getHumanFacilityCategoryDetails());
        target.setHumanLaboratoryCategoryDetails(source.getHumanLaboratoryCategoryDetails());
        target.setAnimalCommunityCategoryDetails(source.getAnimalCommunityCategoryDetails());
        target.setAnimalFacilityCategoryDetails(source.getAnimalFacilityCategoryDetails());
        target.setAnimalLaboratoryCategoryDetails(source.getAnimalLaboratoryCategoryDetails());
        target.setEnvironmentalCategoryDetails(source.getEnvironmentalCategoryDetails());
        target.setPoeCategoryDetails(source.getPoeCategoryDetails());
        target.setTriagingDecision(source.getTriagingDecision());
        target.setOutcomeSupervisor(source.getOutcomeSupervisor());
        target.setNotSignal(source.getNotSignal());
        target.setCategoryDetailsLevel(source.getCategoryDetailsLevel());
        target.setPotentialRisk(source.getPotentialRisk());
        target.setReferred(source.getReferred());
    }

    @Override
    public void fillInnerFromAdo(TriagingDto target, Triaging source) {
        target.setSupervisorReview(source.getSupervisorReview());
        target.setDecisionDate(source.getDecisionDate());
        target.setHealthConcern(source.getHealthConcern());
        target.setReferredTo(source.getReferredTo());
        target.setOccurrencePreviously(source.getOccurrencePreviously());
        target.setSpecificSignal(source.getSpecificSignal());
        target.setSignalCategory(source.getSignalCategory());
        target.setHumanCommunityCategoryDetails(source.getHumanCommunityCategoryDetails());
        target.setHumanFacilityCategoryDetails(source.getHumanFacilityCategoryDetails());
        target.setHumanLaboratoryCategoryDetails(source.getHumanLaboratoryCategoryDetails());
        target.setAnimalCommunityCategoryDetails(source.getAnimalCommunityCategoryDetails());
        target.setAnimalFacilityCategoryDetails(source.getAnimalFacilityCategoryDetails());
        target.setAnimalLaboratoryCategoryDetails(source.getAnimalLaboratoryCategoryDetails());
        target.setEnvironmentalCategoryDetails(source.getEnvironmentalCategoryDetails());
        target.setPoeCategoryDetails(source.getPoeCategoryDetails());
        target.setTriagingDecision(source.getTriagingDecision());
        target.setOutcomeSupervisor(source.getOutcomeSupervisor());
        target.setNotSignal(source.getNotSignal());
        target.setCategoryDetailsLevel(source.getCategoryDetailsLevel());
        target.setPotentialRisk(source.getPotentialRisk());
        target.setReferred(source.getReferred());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return TriagingDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }

    public static TriagingReferenceDto toReferenceDto(Ebs ado) {
        if (ado == null) {
            return null;
        }
        TriagingReferenceDto dto = new TriagingReferenceDto(ado.getUuid());

        return dto;
    }

}
