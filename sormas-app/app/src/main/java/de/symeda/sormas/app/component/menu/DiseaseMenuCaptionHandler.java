package de.symeda.sormas.app.component.menu;

import android.content.Context;

import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.caze.CaseSection;

public class DiseaseMenuCaptionHandler {

    /**
     * Updates menu captions based on the disease
     * @param menuItems The list of menu items to modify
     * @param disease The disease to check against
     * @return Updated list of menu items with disease-specific captions
     */
    public static List<PageMenuItem> updateMenuCaptionsForDisease(List<PageMenuItem> menuItems, Disease disease, Context context) {
        if (disease == null || menuItems == null || context == null) {
            return menuItems;
        }


        // Get the index for each section we want to modify
        int personInfoIndex = CaseSection.PERSON_INFO.ordinal();
        int foodSampleIndex = CaseSection.SIXTY_DAY_FOLLOW_UP.ordinal();
        int hospitalizationIndex = CaseSection.HOSPITALIZATION.ordinal();

        // Update captions based on disease
        switch (disease) {
            case FOODBORNE_ILLNESS:
                updateMenuCaption(menuItems, personInfoIndex, R.string.caption_patient_client, context);
                updateMenuCaption(menuItems, foodSampleIndex, R.string.caption_case_food_sample_testing, context);
                updateMenuCaption(menuItems, hospitalizationIndex, R.string.caption_symptoms, context);
                break;
            case NEW_INFLUENZA:
                 updateMenuCaption(menuItems, personInfoIndex, R.string.caption_demographic_details, context);
                 break;
            case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
            case UNSPECIFIED_VHF:
                updateMenuCaption(menuItems, personInfoIndex, R.string.caption_case_person, context);
                break;
        }

        return menuItems;
    }

    /**
     * Updates a specific menu item's caption if the item exists
     * @param menuItems List of menu items
     * @param index Index of the menu item to update
     * @param newCaptionResourceId Resource ID for the new caption
     */
    private static void updateMenuCaption(List<PageMenuItem> menuItems, int index, int newCaptionResourceId, Context context) {
        if (index < menuItems.size() && menuItems.get(index) != null) {
            PageMenuItem menuItem = menuItems.get(index);
            String newCaption = context.getString(newCaptionResourceId);
            menuItem.setTitle(newCaption);
            menuItem.setDescription(newCaption);
        }
    }
}
