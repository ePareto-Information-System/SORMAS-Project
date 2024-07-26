package de.symeda.sormas.backend.investigationnotes;

import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesFacade;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless(name = "InvestigationNotesFacade")
public class InvestigationNotesFacadeEjb implements InvestigationNotesFacade {

    @EJB
    private InvestigationNotesService service;
    @EJB
    private CaseService caseService;
    @EJB
    private RegionService regionService;
    @EJB
    private DistrictService districtService;
    @EJB
    private CommunityService communityService;
    @EJB
    private FacilityService facilityService;

    public InvestigationNotes fillOrBuildEntity(InvestigationNotesDto source, InvestigationNotes target, boolean checkChangeDate) {

        if (source == null) {
            return null;
        }
        target.setInvestigationNotesData(source.getInvestigationNotesData());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());

        target = DtoHelper.fillOrBuildEntity(source, target, InvestigationNotes::new, checkChangeDate);

        return target;
    }

    public static InvestigationNotesDto toDto(InvestigationNotes investigationNotes) {

        if (investigationNotes == null) {
            return null;
        }

        InvestigationNotesDto target = new InvestigationNotesDto();
        InvestigationNotes source = investigationNotes;

        DtoHelper.fillDto(target, source);

        target.setInvestigationNotesData(source.getInvestigationNotesData());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());

        return target;
    }

    @LocalBean
    @Stateless
    public static class InvestigationNotesFacadeEjbLocal extends InvestigationNotesFacadeEjb {

    }
}
