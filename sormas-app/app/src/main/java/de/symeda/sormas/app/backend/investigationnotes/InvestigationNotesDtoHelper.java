package de.symeda.sormas.app.backend.investigationnotes;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class InvestigationNotesDtoHelper extends AdoDtoHelper<InvestigationNotes, InvestigationNotesDto> {

    public InvestigationNotesDtoHelper(){
    }

    @Override
    protected Class<InvestigationNotes> getAdoClass() {
        return InvestigationNotes.class;
    }

    @Override
    protected Class<InvestigationNotesDto> getDtoClass() {
        return InvestigationNotesDto.class;
    }

    @Override
    protected Call<List<InvestigationNotesDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<InvestigationNotesDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    protected Call<List<PostResponse>> pushAll(List<InvestigationNotesDto> investigationNotesDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    public void fillInnerFromDto(InvestigationNotes target, InvestigationNotesDto source) {
        target.setInvestigationNotesData(source.getInvestigationNotesData());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());
        target.setSurname(source.getSurname());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setTelNo(source.getTelNo());
        target.setDateOfCompletionOfForm(source.getDateOfCompletionOfForm());
        target.setNameOfHealthFacility(source.getNameOfHealthFacility());
    }

    @Override
    public void fillInnerFromAdo(InvestigationNotesDto target, InvestigationNotes source) {
        target.setInvestigationNotesData(source.getInvestigationNotesData());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());
        target.setSurname(source.getSurname());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setTelNo(source.getTelNo());
        target.setDateOfCompletionOfForm(source.getDateOfCompletionOfForm());
        target.setNameOfHealthFacility(source.getNameOfHealthFacility());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
