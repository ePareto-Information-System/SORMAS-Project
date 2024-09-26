package de.symeda.sormas.api.riskfactor;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;
@DependingOnFeatureType(featureType = {
        FeatureType.CASE_SURVEILANCE,
        FeatureType.CONTACT_TRACING })
public class PatientSymptomsPrecedenceDto extends PseudonymizableDto {

    public static final String I18N_PREFIX = "PatientSymptomsPrecedence";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "patientsymptomsprecedence";
    public static final String NAME = "name";
    public static final String CONTACT_ADDRESS = "contactAddress";
    public static final String PHONE = "phone";
    private String name;
    private String contactAddress;
    private String phone;
    public static PatientSymptomsPrecedenceDto build() {
        PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = new PatientSymptomsPrecedenceDto();
        patientSymptomsPrecedenceDto.setUuid(DataHelper.createUuid());
        return patientSymptomsPrecedenceDto;
    }

    public static PatientSymptomsPrecedenceDto build(
            String uuid,
            String name,
            String contactAddress,
            String phone) {
        PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = new PatientSymptomsPrecedenceDto();
        patientSymptomsPrecedenceDto.setUuid(uuid);
        patientSymptomsPrecedenceDto.setName(name);
        patientSymptomsPrecedenceDto.setContactAddress(contactAddress);
        patientSymptomsPrecedenceDto.setPhone(phone);

        return patientSymptomsPrecedenceDto;
    }

    @Override
    public String getUuid() {
        return super.getUuid();
    }
    @Override
    public void setUuid(String uuid) {
        super.setUuid(uuid);
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

    @Override
    public PatientSymptomsPrecedenceDto clone() throws CloneNotSupportedException {
        PatientSymptomsPrecedenceDto clone = (PatientSymptomsPrecedenceDto) super.clone();
        return clone;
    }
}
