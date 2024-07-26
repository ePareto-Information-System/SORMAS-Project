package de.symeda.sormas.api.investigationnotes;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class InvestigationNotesDto extends PseudonymizableDto {

    private static final long serialVersionUID = 4846215199480684367L;

    public static final String I18N_PREFIX = "CaseInvestigationNotes";
    public static final String INVESTIGATION_NOTES = "investigationNotesData";
    public static final String SUSPECTED_DIAGNOSIS = "suspectedDiagnosis";
    public static final String CONFIRMED_DIAGNOSIS = "confirmedDiagnosis";
    public static final String INVESTIGATED_BY = "investigatedBy";
    public static final String INVESTIGATOR_SIGNATURE = "investigatorSignature";
    public static final String INVESTIGATOR_DATE = "investigatorDate";

    private String investigationNotesData;
    private String suspectedDiagnosis;
    private String confirmedDiagnosis;
    private String investigatedBy;
    private String investigatorSignature;
    private Date investigatorDate;

    public static InvestigationNotesDto build() {
        InvestigationNotesDto investigationNotesDto   = new InvestigationNotesDto();
        investigationNotesDto .setUuid(DataHelper.createUuid());
        return investigationNotesDto ;
    }

    public String getInvestigationNotesData() {
        return investigationNotesData;
    }

    public void setInvestigationNotesData(String investigationNotesData) {
        this.investigationNotesData = investigationNotesData;
    }

    public String getSuspectedDiagnosis() {
        return suspectedDiagnosis;
    }

    public void setSuspectedDiagnosis(String suspectedDiagnosis) {
        this.suspectedDiagnosis = suspectedDiagnosis;
    }

    public String getConfirmedDiagnosis() {
        return confirmedDiagnosis;
    }

    public void setConfirmedDiagnosis(String confirmedDiagnosis) {
        this.confirmedDiagnosis = confirmedDiagnosis;
    }

    public String getInvestigatedBy() {
        return investigatedBy;
    }

    public void setInvestigatedBy(String investigatedBy) {
        this.investigatedBy = investigatedBy;
    }

    public String getInvestigatorSignature() {
        return investigatorSignature;
    }

    public void setInvestigatorSignature(String investigatorSignature) {
        this.investigatorSignature = investigatorSignature;
    }

    public Date getInvestigatorDate() {
        return investigatorDate;
    }

    public void setInvestigatorDate(Date investigatorDate) {
        this.investigatorDate = investigatorDate;
    }
}
