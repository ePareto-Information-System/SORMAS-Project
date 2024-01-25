package de.symeda.sormas.ui.formbuilder;

import de.symeda.sormas.api.formbuilder.FormBuilderCriteria;
import de.symeda.sormas.api.formbuilder.FormBuilderIndexDto;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.api.task.TaskIndexDto;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.checkers.UserRightFieldVisibilityChecker;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractFilterForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.filterLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class FormBuilderGridFilterForm extends AbstractFilterForm<FormBuilderCriteria> {

    private static final long serialVersionUID = -8661345403078183133L;

    private static final String WEEK_AND_DATE_FILTER = "weekAndDateFilter";

    private static final String MORE_FILTERS_HTML =
            filterLocs(TaskCriteria.ASSIGNEE_USER_LIKE, TaskCriteria.CREATOR_USER_LIKE) + loc(WEEK_AND_DATE_FILTER);

    protected FormBuilderGridFilterForm() {
        super(
                FormBuilderCriteria.class,
                TaskIndexDto.I18N_PREFIX,
                FieldVisibilityCheckers.withCheckers(new UserRightFieldVisibilityChecker(UserProvider.getCurrent()::hasUserRight)));
    }

    @Override
    protected String[] getMainFilterLocators() {
        return new String[] {
                FormBuilderIndexDto.REGION,
                FormBuilderIndexDto.DISTRICT};
    }
    @Override
    protected String createMoreFiltersHtmlLayout() {
        return MORE_FILTERS_HTML;
    }

    @Override
    protected void addFields() {

    }
}
