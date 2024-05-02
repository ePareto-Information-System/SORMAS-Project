package de.symeda.sormas.backend.persontravelhistory;

import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PersonTravelHistoryService extends BaseAdoService<PersonTravelHistory> {
        public PersonTravelHistoryService() {
            super(PersonTravelHistory.class);
        }
}
