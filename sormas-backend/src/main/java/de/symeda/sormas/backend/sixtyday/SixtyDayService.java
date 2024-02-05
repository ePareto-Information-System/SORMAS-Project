package de.symeda.sormas.backend.sixtyday;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SixtyDayService extends BaseAdoService<SixtyDay> {

    public SixtyDayService() {
        super(SixtyDay.class);
    }

    public SixtyDay createSixtyDay() {

        SixtyDay sixtyDay = new SixtyDay();
        sixtyDay.setUuid(DataHelper.createUuid());
        return sixtyDay;
    }
}
