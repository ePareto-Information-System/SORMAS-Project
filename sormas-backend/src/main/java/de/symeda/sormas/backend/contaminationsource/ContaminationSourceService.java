package de.symeda.sormas.backend.contaminationsource;

import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ContaminationSourceService extends BaseAdoService<ContaminationSource> {
        public ContaminationSourceService() {
            super(ContaminationSource.class);
        }
}
