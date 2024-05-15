package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SignalVerificationService extends BaseAdoService<SignalVerification> {

        public SignalVerificationService() {super(SignalVerification.class);}

        public SignalVerification createSignalVerification() {

            SignalVerification signalVerification = new SignalVerification();
            signalVerification.setUuid(DataHelper.createUuid());
            return signalVerification;
        }
}
