package de.symeda.sormas.backend.foodhistory;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class FoodHistoryService extends BaseAdoService<FoodHistory> {
    public FoodHistoryService() {
        super(FoodHistory.class);
    }

    public FoodHistory createFoodHistory() {

        FoodHistory foodHistory = new FoodHistory();
        foodHistory.setUuid(DataHelper.createUuid());
        return foodHistory;
    }
}
