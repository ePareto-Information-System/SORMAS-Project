package de.symeda.sormas.app.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.formbuilder.FormBuilder;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.component.controls.ControlPropertyField;

public class DiseaseFieldHandler {

    private Context context;

    public DiseaseFieldHandler(Context context) {
        this.context = context;
    }

    public void hideFieldsForDisease(Disease diseaseName, LinearLayout mainContent, FormType formType) {
        // Get the relevant fields for the given disease
        List<String> relevantFields = getFieldsForDisease(diseaseName, formType);

        // If no relevant fields, make all fields visible and return
        if (relevantFields.isEmpty()) {
            setAllFieldsVisibility(mainContent, View.VISIBLE);
            return;
        }

        // Loop through the children of the mainContent and set visibility based on relevantFields list
        for (int i = 0; i < mainContent.getChildCount(); i++) {
            View child = mainContent.getChildAt(i);
            handleChildView(child, relevantFields);
        }
    }

    private void setAllFieldsVisibility(ViewGroup parent, int visibility) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (isFieldView(child)) {
                child.setVisibility(visibility);
            } else if (child instanceof ViewGroup) {
                setAllFieldsVisibility((ViewGroup) child, visibility);
            }
        }
    }

    private boolean isFieldView(View view) {
        return view instanceof TextView || view instanceof ControlPropertyField;
    }

    private void handleChildView(View child, List<String> relevantFields) {


        if (isFieldView(child)) {
            if (child instanceof ControlPropertyField) {
                ControlPropertyField controlPropertyField = (ControlPropertyField) child;
                if (!controlPropertyField.hasVisibilityDependencies()) {
                    setViewVisibility(child, relevantFields);
                }
            } else {
                setViewVisibility(child, relevantFields);
            }
        } else if (child instanceof ViewGroup) {
            handleViewGroup((ViewGroup) child, relevantFields);
        }
    }

    private void handleViewGroup(ViewGroup viewGroup, List<String> relevantFields) {
        boolean groupHasVisibleField = false;

        String layoutIdName = getResourceID(viewGroup.getId());
        if (layoutIdName != null && relevantFields.contains(layoutIdName)) {
            return;
        }

        for (int j = 0; j < viewGroup.getChildCount(); j++) {
            View grandChild = viewGroup.getChildAt(j);

            if (isFieldView(grandChild)) {
                if (grandChild instanceof ControlPropertyField) {
                    ControlPropertyField controlPropertyField = (ControlPropertyField) grandChild;
                    if (!controlPropertyField.hasVisibilityDependencies()) {
                        if (setViewVisibility(grandChild, relevantFields)) {
                            groupHasVisibleField = true;
                        }
                    }
                } else {
                    if (setViewVisibility(grandChild, relevantFields)) {
                        groupHasVisibleField = true;
                    }
                }
            } else if (grandChild instanceof ViewGroup) {
                handleViewGroup((ViewGroup) grandChild, relevantFields);
                if (grandChild.getVisibility() == View.VISIBLE) {
                    groupHasVisibleField = true;
                }
            }
        }


//        viewGroup.setVisibility(groupHasVisibleField ? View.VISIBLE : View.GONE);
    }

    private boolean setViewVisibility(View view, List<String> relevantFields) {
        String viewIdName = getResourceID(view.getId());
        if (viewIdName == null) {
            return false;
        }


        boolean isVisible = relevantFields.isEmpty() || relevantFields.contains(viewIdName);

        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return isVisible;
    }

    public List<String> getFieldsForDisease(Disease diseaseName, FormType formType) {
        FormBuilder formBuilder = DatabaseHelper.getFormBuilderDao().getFormBuilder(formType, diseaseName);

        if (formBuilder != null) {
            List<FormField> formFields = DatabaseHelper.getFormBuilderDao().getFormBuilderFormFields(formBuilder);
            return formFields.stream().map(FormField::getFieldName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }


    private String getResourceID(int id) {
        try {
            return context.getResources().getResourceEntryName(id);
        } catch (Exception e) {
            return null;
        }
    }
}

