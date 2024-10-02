package de.symeda.sormas.app.backend.ebs.riskAssessment;

import static de.symeda.sormas.api.EntityDto.NO_LAST_SYNCED_UUID;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;

import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class RiskAssessmentDtoHelper extends AdoDtoHelper<RiskAssessment, RiskAssessmentDto> {
    private LocationDtoHelper locationHelper;

    private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

    public RiskAssessmentDtoHelper() {
        locationHelper = new LocationDtoHelper();
    }

    @Override
    protected Class<RiskAssessment> getAdoClass() {
        return RiskAssessment.class;
    }

    @Override
    protected Class<RiskAssessmentDto> getDtoClass() {
        return RiskAssessmentDto.class;
    }

    @Override
    protected Call<List<RiskAssessmentDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
        return RetroProvider.getRiskAssessmentFacade().pullAllSince(since, 500, NO_LAST_SYNCED_UUID);
    }

    @Override
    protected Call<List<RiskAssessmentDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        return RetroProvider.getRiskAssessmentFacade().pullByUuids(uuids);
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<RiskAssessmentDto> riskAssessmentDtos) throws NoConnectionException {
        return RetroProvider.getRiskAssessmentFacade().pushAll(riskAssessmentDtos);
    }

    @Override
    public void fillInnerFromDto(RiskAssessment target, RiskAssessmentDto source) {
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        target.setAssessmentDate(source.getAssessmentDate());
        target.setAssessmentTime(source.getAssessmentTime());
        target.setRiskAssessment(source.getRiskAssessment());
        target.setControlMeasuresComment(source.getControlMeasuresComment());
        target.setSpreadProbabilityComment(source.getSpreadProbabilityComment());
        target.setMorbidityMortalityComment(source.getMorbidityMortalityComment());
        target.setEbs(DatabaseHelper.getEbsDao().getByReferenceDto(source.getEbs()));
    }

    @Override
    public void fillInnerFromAdo(RiskAssessmentDto target, RiskAssessment source) {
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        target.setAssessmentDate(source.getAssessmentDate());
        target.setAssessmentTime(source.getAssessmentTime());
        target.setRiskAssessment(source.getRiskAssessment());
        target.setControlMeasuresComment(source.getControlMeasuresComment());
        target.setSpreadProbabilityComment(source.getSpreadProbabilityComment());
        target.setMorbidityMortalityComment(source.getMorbidityMortalityComment());
        target.setEbs(RiskAssessmentDtoHelper.toReferenceDto(source.getEbs()));
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return RiskAssessmentDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }

    public static EbsReferenceDto toReferenceDto(Ebs ado) {
        if (ado == null) {
            return null;
        }
        EbsReferenceDto dto = new EbsReferenceDto(ado.getUuid());

        return dto;
    }
}
