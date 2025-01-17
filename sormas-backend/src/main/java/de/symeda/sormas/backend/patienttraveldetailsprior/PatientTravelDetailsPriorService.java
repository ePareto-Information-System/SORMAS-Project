package de.symeda.sormas.backend.patienttraveldetailsprior;

import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PatientTravelDetailsPriorService extends BaseAdoService<PatientTravelDetailsPrior> {
    public PatientTravelDetailsPriorService() {
        super(PatientTravelDetailsPrior.class);
    }
}
