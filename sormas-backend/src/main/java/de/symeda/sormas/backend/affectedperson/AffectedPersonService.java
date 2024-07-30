package de.symeda.sormas.backend.affectedperson;

import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class AffectedPersonService extends BaseAdoService<AffectedPerson> {

    public AffectedPersonService() {
        super(AffectedPerson.class);
    }
}
