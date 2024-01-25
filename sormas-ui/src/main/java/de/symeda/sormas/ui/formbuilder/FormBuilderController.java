package de.symeda.sormas.ui.formbuilder;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.formbuilder.FormBuilderContext;
import de.symeda.sormas.api.formbuilder.FormBuilderIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.task.TaskContext;
import de.symeda.sormas.api.task.TaskDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.task.TaskEditForm;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class FormBuilderController {

    public FormBuilderController(){

    }

    public void create(FormBuilderContext formBuilderContext, ReferenceDto entityRef, Disease disease, Runnable callback) {

        FormBuilderEditForm createForm = new FormBuilderEditForm(true, false, disease);
       // createForm.setValue(createNewFormBuilder(context, entityRef));
        final CommitDiscardWrapperComponent<FormBuilderEditForm> editView = new CommitDiscardWrapperComponent<FormBuilderEditForm>(
                createForm,
                UserProvider.getCurrent().hasUserRight(UserRight.FORM_BUILDER_CREATE),
                createForm.getFieldGroup());

        editView.addCommitListener(() -> {
            if (!createForm.getFieldGroup().isModified()) {
                FormBuilderIndexDto dto = createForm.getValue();
               // FacadeProvider.getTaskFacade().saveTask(dto);
                callback.run();
            }
        });

        VaadinUiUtil.showModalPopupWindow(editView, I18nProperties.getString(Strings.headingCreateNewTask));
    }
}
