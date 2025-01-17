package de.symeda.sormas.backend.patienttraveldetailsduring;

import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PatientTravelDetailsDuringService extends BaseAdoService<PatientTravelDetailsDuring> {
    public PatientTravelDetailsDuringService() {
        super(PatientTravelDetailsDuring.class);
    }
}
