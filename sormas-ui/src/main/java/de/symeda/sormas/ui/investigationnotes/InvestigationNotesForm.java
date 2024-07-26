package de.symeda.sormas.ui.investigationnotes;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class InvestigationNotesForm extends AbstractEditForm<InvestigationNotesDto> {
    private static final long serialVersionUID = 1L;
    private static final String OFFICIAL_HEADING_LOC = "officialHeadingLoc";

    private static final String HTML_LAYOUT = loc(OFFICIAL_HEADING_LOC) +
            fluidRowLocs(InvestigationNotesDto.INVESTIGATION_NOTES) +
            fluidRowLocs(InvestigationNotesDto.SUSPECTED_DIAGNOSIS, InvestigationNotesDto.CONFIRMED_DIAGNOSIS) +
            fluidRowLocs(InvestigationNotesDto.INVESTIGATED_BY, InvestigationNotesDto.INVESTIGATOR_SIGNATURE, InvestigationNotesDto.INVESTIGATOR_DATE);

    public InvestigationNotesForm(Disease disease,
                                  boolean isPseudonymized,
                        boolean inJurisdiction,boolean isEditAllowed) {
        super(
                InvestigationNotesDto.class,
                InvestigationNotesDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized),
                isEditAllowed);
        addFields();
    }

    @Override
    protected void addFields() {

        createLabel(I18nProperties.getString(Strings.headingofficialUse), H3, OFFICIAL_HEADING_LOC);

        addField(InvestigationNotesDto.INVESTIGATION_NOTES, TextArea.class);
        addField(InvestigationNotesDto.SUSPECTED_DIAGNOSIS, TextField.class);
        addField(InvestigationNotesDto.CONFIRMED_DIAGNOSIS, TextField.class);
        addField(InvestigationNotesDto.INVESTIGATED_BY, TextField.class);
        addField(InvestigationNotesDto.INVESTIGATOR_SIGNATURE, TextField.class);
        addField(InvestigationNotesDto.INVESTIGATOR_DATE, DateField.class);

        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();

    }

    private Label createLabel(String text, String h4, String location) {
        final Label label = new Label(text);
        label.setId(text);
        label.addStyleName(h4);
        getContent().addComponent(label, location);
        return label;
    }


    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }
}
