package de.symeda.sormas.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;

public class FieldOrderConfigurations {

    private static void arrangeYellowFeverCaseCreate(FormType formType){

    }

    public static List<Integer> getConfigurationForDisease(Disease disease, FormType formType) {
        if (disease == Disease.YELLOW_FEVER && formType == FormType.CASE_CREATE) {
            return Arrays.asList(
                    R.id.caseData_disease,
                    R.id.caseData_caseOrigin,
                    R.id.caseData_epidNumber,
                    R.id.date_fields_layout,
                    R.id.caseData_headingCaseResponsibleJurisidction,
                    R.id.region_district_fields_layout,
                    R.id.caseData_responsibleCommunity,
                    R.id.caseData_healthFacility,
                    R.id.caseData_healthFacilityDetails,
                    R.id.name_fields_layout,
                    R.id.caseData_otherName,
                    R.id.person_birthdateLabel,
                    R.id.birthday_fields_layout,
                    R.id.person_sex,
                    R.id.person_ghanaCard,
                    R.id.person_nationalHealthId,
                    R.id.person_phone
            );
        }
        return new ArrayList<>();
    }

}

