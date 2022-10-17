package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.*;

public class AdditionalView extends AbstractConfigurationView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/listOfValue";

    private HorizontalLayout layout;
    protected Button createButton;
    protected Button exportButton;
    private CadreView cadreView;

    public AdditionalView() {
        super(VIEW_NAME);
        cadreView = new CadreView();
        addComponent(layout());
    }

    public HorizontalLayout layout(){
        layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(createActionBtn());

        return layout;
    }

    public Button createActionBtn(){
        Button btnAction = ButtonHelper.createIconButton(Captions.cadre, VaadinIcons.USERS, e -> {
               CommitDiscardWrapperComponent<CadreView> cadreCreateComponent = new CommitDiscardWrapperComponent<>(cadreView);
               cadreCreateComponent.setWidth(1200, Unit.PIXELS);
               cadreCreateComponent.getButtonsPanel().removeComponent(cadreCreateComponent.getCommitButton());
               cadreCreateComponent.getDiscardButton().setCaption(I18nProperties.getCaption(Captions.actionClose));

            VaadinUiUtil.showModalPopupWindow(cadreCreateComponent, I18nProperties.getString(Strings.headingCreateNewCadre));
        }, ValoTheme.BUTTON_PRIMARY);
        return btnAction;
    }
}
