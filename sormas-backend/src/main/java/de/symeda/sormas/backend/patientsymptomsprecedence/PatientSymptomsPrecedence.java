package de.symeda.sormas.backend.patientsymptomsprecedence;


import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.riskfactor.RiskFactor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name = "patientsymptomsprecedence")
public class PatientSymptomsPrecedence extends AbstractDomainObject {

    public static final String I18N_PREFIX = "PatientSymptomsPrecedence";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "patientsymptomsprecedence";
    public static final String RISK_FACTOR = "riskFactor";
    public static final String NAME = "name";
    public static final String CONTACT_ADDRESS = "contactAddress";
    public static final String PHONE = "phone";
    private String uuid;
    private String name;
    private String contactAddress;
    private String phone;
    private RiskFactor riskFactor;

    @ManyToOne
    @JoinColumn(nullable = false)
    public RiskFactor getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(RiskFactor riskFactor) {
        this.riskFactor = riskFactor;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
