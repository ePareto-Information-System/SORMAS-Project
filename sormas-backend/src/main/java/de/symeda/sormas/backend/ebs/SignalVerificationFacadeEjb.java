package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.ebs.SignalVerificationFacade;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless(name = "SignalVerificationFacade")
public class SignalVerificationFacadeEjb implements SignalVerificationFacade {

    public SignalVerification fillOrBuildEntity(SignalVerificationDto source, SignalVerification target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, SignalVerification::new, checkChangeDate);
        target.setVerificationSent(source.getVerificationSent());
        target.setVerified(source.getVerified());
        target.setVerificationCompleteDate(source.getVerificationCompleteDate());
        target.setDateOfOccurrence(source.getDateOfOccurrence());
        target.setNumberOfPersonAnimal(source.getNumberOfPersonAnimal());
        target.setNumberOfDeath(source.getNumberOfDeath());
        target.setDescription(source.getDescription());
        target.setWhyNotVerify(source.getWhyNotVerify());
        target.setNumberOfDeathPerson(source.getNumberOfDeathPerson());
        target.setNumberOfPersonCases(source.getNumberOfPersonCases());
        return target;
    }

    public static SignalVerificationDto toDto(SignalVerification signalVerification) {
        if (signalVerification == null) {
            return null;
        }
        SignalVerificationDto target = new SignalVerificationDto();
        SignalVerification source = signalVerification;
        DtoHelper.fillDto(target, source);
        target.setVerificationSent(source.getVerificationSent());
        target.setVerified(source.getVerified());
        target.setVerificationCompleteDate(source.getVerificationCompleteDate());
        target.setDateOfOccurrence(source.getDateOfOccurrence());
        target.setNumberOfPersonAnimal(source.getNumberOfPersonAnimal());
        target.setNumberOfDeath(source.getNumberOfDeath());
        target.setDescription(source.getDescription());
        target.setWhyNotVerify(source.getWhyNotVerify());
        target.setNumberOfDeathPerson(source.getNumberOfDeathPerson());
        target.setNumberOfPersonCases(source.getNumberOfPersonCases());
        return target;
    }

    @LocalBean
    @Stateless
    public static class SignalVerificationFacadeEjbLocal extends SignalVerificationFacadeEjb {
    }
}
