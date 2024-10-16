package de.symeda.sormas.backend.patientsymptomsprecedence;

import de.symeda.sormas.backend.common.BaseAdoService;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PatientSymptomsPrecedenceService extends BaseAdoService<PatientSymptomsPrecedence> {
    public PatientSymptomsPrecedenceService() {
        super(PatientSymptomsPrecedence.class);
    }
}
