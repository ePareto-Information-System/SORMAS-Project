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
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.formbuilder.FormBuilder;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.component.controls.ControlButton;
import de.symeda.sormas.app.component.controls.ControlCheckBoxField;
import de.symeda.sormas.app.component.controls.ControlDateField;
import de.symeda.sormas.app.component.controls.ControlPropertyEditField;
import de.symeda.sormas.app.component.controls.ControlPropertyField;
import de.symeda.sormas.app.component.controls.ControlSwitchField;
import de.symeda.sormas.app.component.controls.ControlTextReadField;

public class DiseaseFieldHandler {
    private static String TAG = DiseaseFieldHandler.class.getSimpleName();

    private Context context;

    public DiseaseFieldHandler(Context context) {
        this.context = context;
    }

    public void hideFieldsForDisease(Disease diseaseName, LinearLayout mainContent, FormType formType) {
        // Get the relevant fields for the given disease
        List<FormField> relevantFields = getFieldsForDisease(diseaseName, formType);
        Log.d(TAG, "Relevant fields retrieved: " + relevantFields);

        if (relevantFields.isEmpty()) {
            Log.d(TAG, "No relevant fields found, making all fields visible.");
            setAllFieldsVisibility(mainContent, View.VISIBLE);
            return;
        }

        // Get field names for visibility checking
        List<String> fieldNames = relevantFields.stream()
                .map(FormField::getFieldName)
                .collect(Collectors.toList());
        Log.d(TAG, "Field names for visibility check: " + fieldNames);
        // Set visibility based on relevant fields
        for (int i = 0; i < mainContent.getChildCount(); i++) {
            View child = mainContent.getChildAt(i);
            handleChildView(child, fieldNames);
        }

        Log.d(TAG, "Starting hideFieldsForDisease with disease: " + diseaseName + " and formType: " + formType);

        reorderFieldsForDisease(relevantFields, mainContent);
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
        return view instanceof TextView || view instanceof ControlPropertyField || view instanceof ControlCheckBoxField || view instanceof ControlDateField || view instanceof ControlTextReadField
                || view instanceof ControlSwitchField || view instanceof ControlButton;
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
        // Check for a valid ID before retrieving the resource name
        if (view.getId() == View.NO_ID || view.getId() == 0) {
            Log.d(TAG, "Skipping view with no valid ID.");
            return false;
        }

        String viewIdName;
        try {
            viewIdName = context.getResources().getResourceEntryName(view.getId());
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Resource ID not found for view ID: " + view.getId(), e);
            return false;
        }

        boolean isVisible = relevantFields.isEmpty() || relevantFields.contains(viewIdName);
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return isVisible;
    }


    private void reorderFieldsForDisease(List<FormField> orderedFields, ViewGroup parent) {
        // Map to store views and their container hierarchies
        Map<Integer, ViewInfo> viewInfoMap = new HashMap<>();
        gatherChildViewsWithContainers(parent, viewInfoMap, null);

        // Create a map of resource names to IDs for easier lookup
        Map<String, Integer> resourceNameToId = new HashMap<>();
        for (int id : viewInfoMap.keySet()) {
            try {
                String resourceName = context.getResources().getResourceEntryName(id);
                resourceNameToId.put(resourceName, id);
                Log.d(TAG, "Mapped resource: " + resourceName + " to ID: " + id);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Could not find resource name for ID: " + id);
            }
        }

        // Create a list to hold the reordered views
        List<View> reorderedViews = new ArrayList<>();
        Map<ViewGroup, List<View>> containerChildren = new HashMap<>();

        for (Map.Entry<Integer, ViewInfo> entry : viewInfoMap.entrySet()) {
            String resourceName = "";
            try {
                resourceName = context.getResources().getResourceEntryName(entry.getKey());
                Log.d(TAG, "View ID: " + resourceName);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Could not find resource name for ID: " + entry.getKey());
            }
        }

        for (FormField field : orderedFields) {
            int viewId = context.getResources().getIdentifier(field.getFieldName(), "id", context.getPackageName());
            ViewInfo viewInfo = viewInfoMap.get(viewId);

            if (viewInfo != null) {
                if (viewInfo.container != null) {
                    // Handle views that are part of a container
                    containerChildren
                            .computeIfAbsent(viewInfo.container, k -> new ArrayList<>())
                            .add(viewInfo.view);

                    boolean isLastInContainer = true;
                    for (FormField remainingField : orderedFields.subList(orderedFields.indexOf(field) + 1, orderedFields.size())) {
                        int remainingId = context.getResources().getIdentifier(remainingField.getFieldName(), "id", context.getPackageName());
                        ViewInfo remainingInfo = viewInfoMap.get(remainingId);
                        if (remainingInfo != null && remainingInfo.container == viewInfo.container) {
                            isLastInContainer = false;
                            break;
                        }
                    }

                    if (isLastInContainer && !reorderedViews.contains(viewInfo.container)) {
                        reorderedViews.add(viewInfo.container);
                    }
                } else {
                    // Handle views that are not in containers
                    reorderedViews.add(viewInfo.view);
                }
            } else {
                Log.d(TAG, "No matching View found for FormField with name: " + field.getFieldName());
            }
        }

        parent.removeAllViews();

        // Add the reordered views back
        for (View view : reorderedViews) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            // Ensure proper layout params
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
            }

            // If it's a container, ensure its children are in the correct order
            if (view instanceof ViewGroup && containerChildren.containsKey(view)) {
                ViewGroup container = (ViewGroup) view;
                container.removeAllViews();
                for (View child : containerChildren.get(container)) {
                    if (child.getParent() != null) {
                        ((ViewGroup) child.getParent()).removeView(child);
                    }
                    container.addView(child);
                }
            }

            parent.addView(view);
        }
    }


    // Helper class to store view information
    private static class ViewInfo {
        View view;
        ViewGroup container;

        ViewInfo(View view, ViewGroup container) {
            this.view = view;
            this.container = container;
        }
    }

    private void gatherChildViewsWithContainers(ViewGroup parent, Map<Integer, ViewInfo> viewInfoMap, ViewGroup container) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            String resourceName = "";
            try {
                if (child.getId() != View.NO_ID) {
                    resourceName = context.getResources().getResourceEntryName(child.getId());
                    Log.d(TAG, "Processing view: " + resourceName);
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Resource not found for view ID: " + child.getId());
            }

            if (child.getId() != View.NO_ID) {
                // Add the view itself if it has an ID
                viewInfoMap.put(child.getId(), new ViewInfo(child, container));
            }

            if (child instanceof ViewGroup) {
                // Always recurse into ViewGroups, whether they're containers or not
                gatherChildViewsWithContainers((ViewGroup) child, viewInfoMap,
                        (isContainer(child) ? (ViewGroup)child : container));
            }
        }
    }

    private boolean isContainer(View view) {
        if (!(view instanceof ViewGroup)) return false;
        try {
            String resourceName = context.getResources().getResourceEntryName(view.getId());
            return resourceName != null && (resourceName.contains("_layout") || resourceName.contains("_container") || view.getId() == R.id.btns);
        } catch (Resources.NotFoundException e) {
            return false;
        }
    }


    public List<FormField> getFieldsForDisease(Disease diseaseName, FormType formType) {
        FormBuilder formBuilder = DatabaseHelper.getFormBuilderDao().getFormBuilder(formType, diseaseName);

        if (formBuilder != null) {
            List<FormField> orderedFields = DatabaseHelper.getFormBuilderDao().getOrderedFormBuilderFormFields(formBuilder);
            Log.d(TAG, "Ordered fields retrieved from database: " + orderedFields);
            return orderedFields;
        }
        Log.d(TAG, "No FormBuilder found for Disease=" + diseaseName + ", FormType=" + formType);
        return new ArrayList<>();
    }


}

