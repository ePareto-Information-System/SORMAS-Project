package de.symeda.sormas.app.backend.ebs.triaging;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.ebs.AnimalCommunityCategoryDetails;
import de.symeda.sormas.api.ebs.AnimalFacilityCategoryDetails;
import de.symeda.sormas.api.ebs.AnimalLaboratoryCategoryDetails;
import de.symeda.sormas.api.ebs.CategoryDetailsLevel;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.EnvironmentalCategoryDetails;
import de.symeda.sormas.api.ebs.HumanCommunityCategoryDetails;
import de.symeda.sormas.api.ebs.HumanFaclityCategoryDetails;
import de.symeda.sormas.api.ebs.HumanLaboratoryCategoryDetails;
import de.symeda.sormas.api.ebs.OutComeSupervisor;
import de.symeda.sormas.api.ebs.POE;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.ebs.Ebs;

@Entity(name = Triaging.TABLE_NAME)
@EmbeddedAdo
@DatabaseTable(tableName = Triaging.TABLE_NAME)
public class Triaging extends PseudonymizableAdo {

    private static final long serialVersionUID = 2430932452606853497L;

    public static final String TABLE_NAME = "triaging";
    public static final String I18N_PREFIX = "triaging";

    public static final String SUPERVISOR_REVIEW = "supervisorReview";
    public static final String SPECIFIC_SIGNAL = "specificSignal";
    public static final String HEALTH_CONCERN = "healthConcern";
    public static final String SIGNAL_CATEGORY = "signalCategory";
    public static final String HUMAN_COMMUNITY_CATEGORY_DETAILS = "humanCommunityCategoryDetails";
    public static final String HUMAN_FACILITY_CATEGORY_DETAILS = "humanFacilityCategoryDetails";
    public static final String HUMAN_LABORATORY_CATEGORY_DETAILS = "humanLaboratoryCategoryDetails";
    public static final String ANIMAL_COMMUNITY_CATEGORY_DETAILS = "animalCommunityCategoryDetails";
    public static final String ANIMAL_FACILITY_CATEGORY_DETAILS = "animalFacilityCategoryDetails";
    public static final String ANIMAL_LABORATORY_CATEGORY_DETAILS = "animalLaboratoryCategoryDetails";
    public static final String ENVIRONMENTAL_CATEGORY_DETAILS = "environmentalCategoryDetails";
    public static final String POE_CATEGORY_DETAILS = "poeCategoryDetails";
    public static final String OCCURRENCE_PREVIOUSLY = "occurrencePreviously";
    public static final String TRIAGING_DECISION = "triagingDecision";
    public static final String DATE_OF_DECISION = "decisionDate";
    public static final String REFERRED_TO = "referredTo";
    public static final String RESPONSIBLE_USER = "responsibleUser";
    public static final String OUTCOME_SUPERVISOR = "outcomeSupervisor";
    public static final String NOT_SIGNAL = "notSignal";
    public static final String CATEGORY_DETAILS_LEVEL = "categoryDetailsLevel";
    public static final String POTENTIAL_RISK = "potentialRisk";
    public static final String REFERRED = "referred";


    @Enumerated(EnumType.STRING)
    private YesNo supervisorReview;
    @Enumerated(EnumType.STRING)
    private YesNo referred;
    @Enumerated(EnumType.STRING)
    private YesNo specificSignal;
    @Enumerated(EnumType.STRING)
    private SignalCategory signalCategory;
    @Enumerated(EnumType.STRING)
    private YesNo healthConcern;
    @Enumerated(EnumType.STRING)
    private HumanCommunityCategoryDetails humanCommunityCategoryDetails;
    @Enumerated(EnumType.STRING)
    private HumanFaclityCategoryDetails humanFacilityCategoryDetails;
    @Enumerated(EnumType.STRING)
    private HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails;
    @Enumerated(EnumType.STRING)
    private AnimalCommunityCategoryDetails animalCommunityCategoryDetails;
    @Enumerated(EnumType.STRING)
    private AnimalFacilityCategoryDetails animalFacilityCategoryDetails;
    @Enumerated(EnumType.STRING)
    private AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails;
    @Enumerated(EnumType.STRING)
    private EnvironmentalCategoryDetails environmentalCategoryDetails;
    @Enumerated(EnumType.STRING)
    private POE poeCategoryDetails;
    @Enumerated(EnumType.STRING)
    private CategoryDetailsLevel categoryDetailsLevel;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Ebs ebs;

    @Enumerated(EnumType.STRING)
    private YesNo occurrencePreviously;
    @Enumerated(EnumType.STRING)
    private EbsTriagingDecision triagingDecision;

    @DatabaseField
    private String triagingDecisionString;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date decisionDate;
    @DatabaseField
    private String referredTo;

    @Enumerated(EnumType.STRING)
    private OutComeSupervisor outcomeSupervisor;
    @DatabaseField
    private boolean notSignal;
    @Enumerated(EnumType.STRING)
    private YesNo potentialRisk;

    public EbsReferenceDto toReference() {
        return new EbsReferenceDto(getUuid());
    }


    public YesNo getSupervisorReview() {
        return supervisorReview;
    }

    public void setSupervisorReview(YesNo earlyWarning) {
        this.supervisorReview = earlyWarning;
    }

    public YesNo getSpecificSignal() {
        return specificSignal;
    }

    public void setSpecificSignal(YesNo specificSignal) {
        this.specificSignal = specificSignal;
    }

    @Enumerated(EnumType.STRING)
    public SignalCategory getSignalCategory() {
        return signalCategory;
    }

    public void setSignalCategory(SignalCategory signalCategory) {
        this.signalCategory = signalCategory;
    }

    public YesNo getHealthConcern() {
        return healthConcern;
    }

    public void setHealthConcern(YesNo healthConcern) {
        this.healthConcern = healthConcern;
    }


    public HumanCommunityCategoryDetails getHumanCommunityCategoryDetails() {
        return humanCommunityCategoryDetails;
    }

    public void setHumanCommunityCategoryDetails(HumanCommunityCategoryDetails humanCommunityCategoryDetails) {
        this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
    }

    public HumanFaclityCategoryDetails getHumanFacilityCategoryDetails() {
        return humanFacilityCategoryDetails;
    }

    public void setHumanFacilityCategoryDetails(HumanFaclityCategoryDetails humanFacilityCategoryDetails) {
        this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
    }

    public HumanLaboratoryCategoryDetails getHumanLaboratoryCategoryDetails() {
        return humanLaboratoryCategoryDetails;
    }

    public void setHumanLaboratoryCategoryDetails(HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails) {
        this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
    }

    public AnimalCommunityCategoryDetails getAnimalCommunityCategoryDetails() {
        return animalCommunityCategoryDetails;
    }

    public void setAnimalCommunityCategoryDetails(AnimalCommunityCategoryDetails animalCommunityCategoryDetails) {
        this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
    }

    public AnimalFacilityCategoryDetails getAnimalFacilityCategoryDetails() {
        return animalFacilityCategoryDetails;
    }

    public void setAnimalFacilityCategoryDetails(AnimalFacilityCategoryDetails animalFacilityCategoryDetails) {
        this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;

    }

    public AnimalLaboratoryCategoryDetails getAnimalLaboratoryCategoryDetails() {
        return animalLaboratoryCategoryDetails;
    }

    public void setAnimalLaboratoryCategoryDetails(AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails) {
        this.animalLaboratoryCategoryDetails = animalLaboratoryCategoryDetails;
    }

    public EnvironmentalCategoryDetails getEnvironmentalCategoryDetails() {
        return environmentalCategoryDetails;
    }

    public void setEnvironmentalCategoryDetails(EnvironmentalCategoryDetails environmentalCategoryDetails) {
        this.environmentalCategoryDetails = environmentalCategoryDetails;
    }

    public POE getPoeCategoryDetails() {
        return poeCategoryDetails;
    }

    public void setPoeCategoryDetails(POE poeCategoryDetails) {
        this.poeCategoryDetails = poeCategoryDetails;

    }

    public YesNo getOccurrencePreviously() {
        return occurrencePreviously;
    }

    public void setOccurrencePreviously(YesNo occurrencePreviously) {
        this.occurrencePreviously = occurrencePreviously;
    }

    public EbsTriagingDecision getTriagingDecision() {
        return triagingDecision;
    }

    public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
        this.triagingDecision = triagingDecision;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(String referredTo) {
        this.referredTo = referredTo;
    }


    public String getTriagingDecisionString() {
        return triagingDecisionString;
    }

    public void setTriagingDecisionString(String triagingDecisionString) {
        this.triagingDecisionString = triagingDecisionString;
    }
    @Enumerated(EnumType.STRING)
    public OutComeSupervisor getOutcomeSupervisor() {
        return outcomeSupervisor;
    }

    public void setOutcomeSupervisor(OutComeSupervisor outcomeSupervisor) {
        this.outcomeSupervisor = outcomeSupervisor;
    }


    public boolean getNotSignal() {
        return notSignal;
    }

    public void setNotSignal(boolean notSignal) {
        this.notSignal = notSignal;
    }
    @Enumerated(EnumType.STRING)
    public CategoryDetailsLevel getCategoryDetailsLevel() {
        return categoryDetailsLevel;
    }

    public void setCategoryDetailsLevel(CategoryDetailsLevel categoryDetailsLevel) {
        this.categoryDetailsLevel = categoryDetailsLevel;
    }

    public YesNo getPotentialRisk() {
        return potentialRisk;
    }

    public void setPotentialRisk(YesNo potentialRisk) {
        this.potentialRisk = potentialRisk;
    }

    public YesNo getReferred() {
        return referred;
    }

    public void setReferred(YesNo referred) {
        this.referred = referred;
    }

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    public Ebs getEbs() {
        return ebs;
    }

    public void setEbs(Ebs ebs) {
        this.ebs = ebs;
    }
}
