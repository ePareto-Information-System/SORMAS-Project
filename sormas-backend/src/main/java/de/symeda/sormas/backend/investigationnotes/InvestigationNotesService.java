package de.symeda.sormas.backend.investigationnotes;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class InvestigationNotesService extends BaseAdoService<InvestigationNotes> {

    public InvestigationNotesService() {
        super(InvestigationNotes.class);
    }

    public InvestigationNotes createInvestigationNotes() {

        InvestigationNotes investigationNotes = new InvestigationNotes();
        investigationNotes.setUuid(DataHelper.createUuid());
        return investigationNotes;
    }
}
