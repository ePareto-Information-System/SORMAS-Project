package de.symeda.sormas.ui.riskfactor;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.OutbreakFieldVisibilityChecker;
import de.symeda.sormas.ui.utils.ViewMode;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class RiskFactorForm extends AbstractEditForm<RiskFactorDto> {

    private static final long serialVersionUID = 1L;
    private static final String RISK_FACTOR_HEADING_LOC = "riskFactorHeadingLoc";
    private static final String HTML_LAYOUT =
            loc(RISK_FACTOR_HEADING_LOC) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_ONE) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_THREE) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_FOUR) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_ONE) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_TWO) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_THREE) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_FOUR) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_FIVE) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_SIX) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_SEVEN) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_EIGHT) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO) +
                    fluidRowLocs(RiskFactorDto.WATER_USED_FOR_DRINKING) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT) +
                    fluidRowLocs(RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS);

    private final CaseDataDto caze;
    private final ViewMode viewMode;

    public RiskFactorForm(CaseDataDto caze, ViewMode viewMode, boolean isPseudonymized, boolean isEditAllowed) {

        super(
                RiskFactorDto.class,
                RiskFactorDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale())
                        .add(new OutbreakFieldVisibilityChecker(viewMode)),
                UiFieldAccessCheckers.forSensitiveData(isPseudonymized));
        this.caze = caze;
        this.viewMode = viewMode;
        addFields();

    }


    @Override
    protected String createHtmlLayout() {
        return null;
    }

    @Override
    protected void addFields() {
        if (caze == null || viewMode == null) {
            return;
        }
    }

}
