package de.symeda.sormas.backend.afpimmunization;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class AfpImmunizationService extends BaseAdoService<AfpImmunization> {
    public AfpImmunizationService() {
        super(AfpImmunization.class);
    }

    public AfpImmunization createAfpImmunization() {

        AfpImmunization afpImmunization = new AfpImmunization();
        afpImmunization.setUuid(DataHelper.createUuid());
        return afpImmunization;
    }
}
