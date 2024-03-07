package de.symeda.sormas.ui.caze;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;

public class CaseDataFormConfiguration {
        private final Disease disease;
        private final CaseDataForm caseDataForm;

    public CaseDataFormConfiguration(Disease disease, CaseDataForm caseDataForm) {
        this.disease = disease;
        this.caseDataForm = caseDataForm;
    }

    public void handleVisibility() {
            switch (disease) {
                case AHF:
                    handleAHF();
                    break;
                case YELLOW_FEVER:
                    handleYellowFever();
                    break;
                case CSM:
                    handleCSM();
                    break;
                case AFP:
                    handleAFP();
                    break;
                case NEW_INFLUENZA:
                    handleInfluenza();
                    break;
                default:
            }
        }

        private void handleAHF() {
        }

        private void handleYellowFever() {
        }

        private void handleCSM() {
        }

        private void handleAFP() {
        }

        private void handleInfluenza() {
        }

}
