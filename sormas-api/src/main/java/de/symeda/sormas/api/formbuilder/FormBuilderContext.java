package de.symeda.sormas.api.formbuilder;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DependingOnUserRight;

public enum FormBuilderContext {

    //remove this later
    @DependingOnUserRight(UserRight.CASE_VIEW)
    CASE(FeatureType.TASK_GENERATION_CASE_SURVEILLANCE, "cases", Strings.notificationTaskAssociatedCaseLink),
    @DependingOnUserRight(UserRight.CONTACT_VIEW)
    CONTACT(FeatureType.TASK_GENERATION_CONTACT_TRACING, "contacts", Strings.notificationTaskAssociatedContactLink),
    @DependingOnUserRight(UserRight.EVENT_VIEW)
    EVENT(FeatureType.TASK_GENERATION_EVENT_SURVEILLANCE, "events", Strings.notificationTaskAssociatedEventLink),
    GENERAL(FeatureType.TASK_GENERATION_GENERAL, null, null),
    @DependingOnUserRight(UserRight.TRAVEL_ENTRY_VIEW)
    TRAVEL_ENTRY(FeatureType.TRAVEL_ENTRIES, "travelEntries", Strings.notificationTaskAssociatedTravelEntryLink);

    private final FeatureType featureType;
    private final String urlPattern;
    private final String associatedEntityLinkMessage;

    FormBuilderContext(FeatureType featureType, String urlPattern, String associatedEntityLinkMessage) {
        this.featureType = featureType;
        this.urlPattern = urlPattern;
        this.associatedEntityLinkMessage = associatedEntityLinkMessage;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getAssociatedEntityLinkMessage() {
        return associatedEntityLinkMessage;
    }

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
