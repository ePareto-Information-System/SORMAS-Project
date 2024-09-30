package de.symeda.sormas.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.app.FieldOrderConfigurations;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.formbuilder.FormBuilder;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.component.controls.ControlCheckBoxField;
import de.symeda.sormas.app.component.controls.ControlDateField;
import de.symeda.sormas.app.component.controls.ControlPropertyEditField;
import de.symeda.sormas.app.component.controls.ControlPropertyField;
import de.symeda.sormas.app.component.controls.ControlSpinnerField;

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
        List<Integer> fieldOrder = getFieldOrderForDisease(diseaseName, formType);
        if (fieldOrder.isEmpty()) {
            Log.d("HideFields", "No field order defined for disease: " + diseaseName);
            return;
        }

        reorderFieldsForDisease(fieldOrder, mainContent);
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
        return view instanceof ControlPropertyEditField || view instanceof TextView ||
                view instanceof ControlPropertyField || view instanceof ControlCheckBoxField || view instanceof ControlDateField;
    }

    private void handleChildView(View child, List<String> relevantFields) {
        if (isFieldView(child)) {
            setViewVisibility(child, relevantFields);
        } else if (child instanceof ViewGroup) {
            handleViewGroup((ViewGroup) child, relevantFields);
        }
    }

    private void handleViewGroup(ViewGroup viewGroup, List<String> relevantFields) {
        boolean groupHasVisibleField = false;

        for (int j = 0; j < viewGroup.getChildCount(); j++) {
            View grandChild = viewGroup.getChildAt(j);

            if (isFieldView(grandChild)) {
                if (setViewVisibility(grandChild, relevantFields)) {
                    groupHasVisibleField = true;
                }
            } else if (grandChild instanceof ViewGroup) {
                handleViewGroup((ViewGroup) grandChild, relevantFields);
                if (grandChild.getVisibility() == View.VISIBLE) {
                    groupHasVisibleField = true;
                }
            }
        }

        viewGroup.setVisibility(groupHasVisibleField ? View.VISIBLE : View.GONE);
    }

    private boolean setViewVisibility(View view, List<String> relevantFields) {
        String viewIdName = context.getResources().getResourceEntryName(view.getId());
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

    public List<Integer> getFieldOrderForDisease(Disease diseaseName, FormType formType) {
        return FieldOrderConfigurations.getConfigurationForDisease(diseaseName, formType);

    }

    public void reorderFieldsForDisease(List<Integer> fieldOrder, ViewGroup parent) {
        Map<Integer, View> viewMap = new HashMap<>();
        gatherChildViews(parent, viewMap);

        reorderViewsInPlace(parent, fieldOrder, viewMap);
    }


    // Recursively gather child views, including those inside nested ViewGroups
    private void gatherChildViews(ViewGroup parent, Map<Integer, View> viewMap) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() != View.NO_ID) {
                viewMap.put(child.getId(), child);
            } else {
            }
            if (child instanceof ViewGroup) {
                gatherChildViews((ViewGroup) child, viewMap);
            }
        }
    }

    // Reorder the views within their respective parents
    private void reorderViewsInPlace(ViewGroup parent, List<Integer> fieldOrder, Map<Integer, View> viewMap) {
        List<View> reorderedViews = new ArrayList<>();

        // Loop through fieldOrder to get the views in the correct order
        for (int fieldId : fieldOrder) {
            View view = viewMap.get(fieldId);

            if (view != null && view.getParent() == parent) {
                reorderedViews.add(view);
            } else {
            }
        }

        parent.removeAllViews();

        // Add views back to parent in the new order
        for (View view : reorderedViews) {
            if (view.getParent() == null) {
                parent.addView(view);
            } else {
            }
        }
    }

}

