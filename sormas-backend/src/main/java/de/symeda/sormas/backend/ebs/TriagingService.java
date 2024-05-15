package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TriagingService extends BaseAdoService<Triaging> {

        public TriagingService() {super(Triaging.class);}

        public Triaging createTriaging() {

            Triaging triaging = new Triaging();
            triaging.setUuid(DataHelper.createUuid());
            return triaging;
        }
}
