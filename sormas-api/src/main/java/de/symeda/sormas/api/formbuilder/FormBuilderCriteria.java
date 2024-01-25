package de.symeda.sormas.api.formbuilder;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DateFilterOption;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;
import java.util.Date;

public class FormBuilderCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = -9174165215694877625L;

    public static final String FREE_TEXT = "freeText";

    private FormBuilderContext formBuilderContext;
    private String freeText;
    private UserReferenceDto assigneeUser;
    private UserReferenceDto excludeAssigneeUser;
    private CaseReferenceDto caze;
    private ContactReferenceDto contact;
    private PersonReferenceDto contactPerson;
    private EventReferenceDto event;
    private Date dueDateFrom;
    private Date dueDateTo;
    private Date startDateFrom;
    private Date startDateTo;
    private Date statusChangeDateFrom;
    private Date statusChangeDateTo;
    // Used to re-construct whether users have filtered by epi weeks or dates
    private DateFilterOption dateFilterOption = DateFilterOption.DATE;
    private EntityRelevanceStatus relevanceStatus;
    private RegionReferenceDto region;
    private DistrictReferenceDto district;
    private String assigneeUserLike;
    private String creatorUserLike;
    private boolean excludeLimitedSyncRestrictions;

    public FormBuilderCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
        this.relevanceStatus = relevanceStatus;
        return this;
    }

    @IgnoreForUrl
    public EntityRelevanceStatus getRelevanceStatus() {
        return relevanceStatus;
    }

    public FormBuilderCriteria assigneeUser(UserReferenceDto assigneeUser) {
        this.assigneeUser = assigneeUser;
        return this;
    }

    public UserReferenceDto getAssigneeUser() {
        return assigneeUser;
    }

    public FormBuilderCriteria excludeAssigneeUser(UserReferenceDto excludeAssigneeUser) {
        this.excludeAssigneeUser = excludeAssigneeUser;
        return this;
    }

    public UserReferenceDto getExcludeAssigneeUser() {
        return excludeAssigneeUser;
    }

    public FormBuilderCriteria caze(CaseReferenceDto caze) {
        this.caze = caze;
        return this;
    }

    public CaseReferenceDto getCaze() {
        return caze;
    }

    public FormBuilderCriteria contact(ContactReferenceDto contact) {
        this.contact = contact;
        return this;
    }

    public ContactReferenceDto getContact() {
        return contact;
    }

    public FormBuilderCriteria contactPerson(PersonReferenceDto contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public FormBuilderCriteria event(EventReferenceDto event) {
        this.event = event;
        return this;
    }

    public PersonReferenceDto getContactPerson() {
        return contactPerson;
    }

    public EventReferenceDto getEvent() {
        return event;
    }


    public FormBuilderCriteria dueDateBetween(Date dueDateFrom, Date dueDateTo) {
        this.dueDateFrom = dueDateFrom;
        this.dueDateTo = dueDateTo;
        return this;
    }

    public FormBuilderCriteria dueDateFrom(Date dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
        return this;
    }

    public Date getDueDateFrom() {
        return dueDateFrom;
    }

    public FormBuilderCriteria dueDateTo(Date dueDateTo) {
        this.dueDateTo = dueDateTo;
        return this;
    }

    public Date getDueDateTo() {
        return dueDateTo;
    }

    public FormBuilderCriteria startDateBetween(Date startDateFrom, Date startDateTo) {
        this.startDateFrom = startDateFrom;
        this.startDateTo = startDateTo;
        return this;
    }

    public FormBuilderCriteria startDateFrom(Date startDateFrom) {
        this.startDateFrom = startDateFrom;
        return this;
    }

    public Date getStartDateFrom() {
        return startDateFrom;
    }

    public FormBuilderCriteria startDateTo(Date startDateTo) {
        this.startDateTo = startDateTo;
        return this;
    }

    public Date getStartDateTo() {
        return startDateTo;
    }

    public FormBuilderCriteria statusChangeDateBetween(Date statusChangeDateFrom, Date statusChangeDateTo) {
        this.statusChangeDateFrom = statusChangeDateFrom;
        this.statusChangeDateTo = statusChangeDateTo;
        return this;
    }


    public FormBuilderCriteria statusChangeDateTo(Date statusChangeDateTo) {
        this.statusChangeDateTo = statusChangeDateTo;
        return this;
    }

    public Date getStatusChangeDateTo() {
        return statusChangeDateTo;
    }



    public FormBuilderContext getFormBuilderContext() {
        return formBuilderContext;
    }

    public void setFormBuilderContext(FormBuilderContext formBuilderContext) {
        this.formBuilderContext = formBuilderContext;
    }

    public FormBuilderCriteria formBuilderContext(FormBuilderContext formBuilderContext) {
        this.formBuilderContext = formBuilderContext;
        return this;
    }

    public RegionReferenceDto getRegion() {
        return region;
    }

    public void setRegion(RegionReferenceDto region) {
        this.region = region;
    }

    public FormBuilderCriteria region(RegionReferenceDto region) {
        this.region = region;
        return this;
    }

    public DistrictReferenceDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictReferenceDto district) {
        this.district = district;
    }

    public FormBuilderCriteria district(DistrictReferenceDto district) {
        this.district = district;
        return this;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public FormBuilderCriteria freeText(String freeText) {
        this.freeText = freeText;
        return this;
    }

    public String getAssigneeUserLike() {
        return assigneeUserLike;
    }

    public void setAssigneeUserLike(String assigneeUserLike) {
        this.assigneeUserLike = assigneeUserLike;
    }

    public FormBuilderCriteria assigneeUserLike(String assigneeUserLike) {
        this.assigneeUserLike = assigneeUserLike;
        return this;
    }

    public String getCreatorUserLike() {
        return creatorUserLike;
    }

    public void setCreatorUserLike(String creatorUserLike) {
        this.creatorUserLike = creatorUserLike;
    }

    public FormBuilderCriteria creatorUserLike(String creatorUserLike) {
        this.creatorUserLike = creatorUserLike;
        return this;
    }


    @IgnoreForUrl
    public boolean isExcludeLimitedSyncRestrictions() {
        return excludeLimitedSyncRestrictions;
    }

    public FormBuilderCriteria excludeLimitedSyncRestrictions(boolean excludeLimitedSyncRestrictions) {
        this.excludeLimitedSyncRestrictions = excludeLimitedSyncRestrictions;
        return this;
    }
}
