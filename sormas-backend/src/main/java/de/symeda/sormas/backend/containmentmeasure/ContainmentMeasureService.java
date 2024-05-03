package de.symeda.sormas.backend.containmentmeasure;

import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.persontravelhistory.PersonTravelHistory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ContainmentMeasureService extends BaseAdoService<ContainmentMeasure> {
        public ContainmentMeasureService() {
            super(ContainmentMeasure.class);
        }
}
