package de.symeda.sormas.app.backend.patientsymptomsprecedence;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.persistence.Column;
import javax.persistence.Entity;

import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;

@Entity(name = PatientSymptomsPrecedence.TABLE_NAME)
@DatabaseTable(tableName = PatientSymptomsPrecedence.TABLE_NAME)
@EmbeddedAdo(parentAccessor = PatientSymptomsPrecedence.RISK_FACTOR)

public class PatientSymptomsPrecedence extends PseudonymizableAdo{

    private static final long serialVersionUID = -6551672739041643945L;
    public static final String RISK_FACTOR = "riskFactor";
    public static final String TABLE_NAME = "patientsymptomsprecedence";
    public static final String I18N_PREFIX = "PatientSymptomsPrecedence";

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private RiskFactor riskFactor;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String name;
    @Column
    private String contactAddress;
    @Column
    private String phone;

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    public RiskFactor getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(RiskFactor riskFactor) {
        this.riskFactor = riskFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




}
