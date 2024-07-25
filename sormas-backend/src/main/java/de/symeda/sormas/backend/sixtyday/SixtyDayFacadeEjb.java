/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.sixtyday;

import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.sixtyday.SixtyDayFacade;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "SixtyDayFacade")
public class SixtyDayFacadeEjb implements SixtyDayFacade {

    @EJB
    private SixtyDayService service;
    @EJB
    private CaseService caseService;
    @EJB
    private RegionService regionService;
    @EJB
    private DistrictService districtService;
    @EJB
    private CommunityService communityService;
    @EJB
    private FacilityService facilityService;

    public SixtyDay fillOrBuildEntity(SixtyDayDto source, SixtyDay target, boolean checkChangeDate) {

        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, SixtyDay::new, checkChangeDate);

        target.setPersonExamineCase(source.getPersonExamineCase());
        target.setDateOfFollowup(source.getDateOfFollowup());
        target.setDateBirth(source.getDateBirth());
        target.setResidentialLocation(source.getResidentialLocation());
        target.setPatientFound(source.getPatientFound());
        target.setPatientFoundReason(source.getPatientFoundReason());
        target.setLocateChildAttempt(source.getLocateChildAttempt());
        target.setParalysisWeaknessPresent(source.getParalysisWeaknessPresent());
        target.setParalysisWeaknessPresentSite(source.getParalysisWeaknessPresentSite());
        target.setParalyzedPartOther(source.getParalyzedPartOther());
        target.setParalysisWeaknessFloppy(source.getParalysisWeaknessFloppy());
        target.setMuscleToneParalyzedPart(source.getMuscleToneParalyzedPart());
        target.setMuscleToneOtherPartBody(source.getMuscleToneOtherPartBody());
        target.setDeepTendon(source.getDeepTendon());
        target.setMuscleVolume(source.getMuscleVolume());
        target.setSensoryLoss(source.getSensoryLoss());
        target.setProvisionalDiagnosis(source.getProvisionalDiagnosis());
        target.setComments(source.getComments());
        target.setContactDetailsNumber(source.getContactDetailsNumber());
        target.setContactDetailsEmail(source.getContactDetailsEmail());
        target.setSignature(source.getSignature());
        target.setDateSubmissionForms(source.getDateSubmissionForms());

        target.setFoodAvailableTesting(source.getFoodAvailableTesting());
        target.setLabTestConducted(source.getLabTestConducted());
        target.setSpecifyFoodsSources(source.getSpecifyFoodsSources());
        target.setProductName(source.getProductName());
        target.setBatchNumber(source.getBatchNumber());
        target.setDateOfManufacture(source.getDateOfManufacture());
        target.setExpirationDate(source.getExpirationDate());
        target.setPackageSize(source.getPackageSize());
        target.setPackagingType(source.getPackagingType());
        target.setPackagingTypeOther(source.getPackagingTypeOther());
        target.setPlaceOfPurchase(source.getPlaceOfPurchase());
        target.setNameOfManufacturer(source.getNameOfManufacturer());
        target.setAddress(source.getAddress());
        target.setFoodTel(source.getFoodTel());
        target.setInvestigationNotes(source.getInvestigationNotes());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());
        target.setSurname(source.getSurname());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setTelNo(source.getTelNo());
        target.setDateOfCompletionOfForm(source.getDateOfCompletionOfForm());
        target.setNameOfHealthFacility(source.getNameOfHealthFacility());
        target.setSpecifySources(source.getSpecifySources());

        return target;
    }


    public static SixtyDayDto toDto(SixtyDay sixtyDay) {

        if (sixtyDay == null) {
            return null;
        }

        SixtyDayDto target = new SixtyDayDto();
        SixtyDay source = sixtyDay;

        DtoHelper.fillDto(target, source);

        target.setPersonExamineCase(source.getPersonExamineCase());
        target.setDateOfFollowup(source.getDateOfFollowup());
        target.setDateBirth(source.getDateBirth());
        target.setResidentialLocation(source.getResidentialLocation());
        target.setPatientFound(source.getPatientFound());
        target.setPatientFoundReason(source.getPatientFoundReason());
        target.setLocateChildAttempt(source.getLocateChildAttempt());
        target.setParalysisWeaknessPresent(source.getParalysisWeaknessPresent());
        target.setParalysisWeaknessPresentSite(source.getParalysisWeaknessPresentSite());
        target.setParalyzedPartOther(source.getParalyzedPartOther());
        target.setParalysisWeaknessFloppy(source.getParalysisWeaknessFloppy());
        target.setMuscleToneParalyzedPart(source.getMuscleToneParalyzedPart());
        target.setMuscleToneOtherPartBody(source.getMuscleToneOtherPartBody());
        target.setDeepTendon(source.getDeepTendon());
        target.setMuscleVolume(source.getMuscleVolume());
        target.setSensoryLoss(source.getSensoryLoss());
        target.setProvisionalDiagnosis(source.getProvisionalDiagnosis());
        target.setComments(source.getComments());
        target.setContactDetailsNumber(source.getContactDetailsNumber());
        target.setContactDetailsEmail(source.getContactDetailsEmail());
        target.setSignature(source.getSignature());
        target.setDateSubmissionForms(source.getDateSubmissionForms());

        target.setFoodAvailableTesting(source.getFoodAvailableTesting());
        target.setLabTestConducted(source.getLabTestConducted());
        target.setSpecifyFoodsSources(source.getSpecifyFoodsSources());
        target.setProductName(source.getProductName());
        target.setBatchNumber(source.getBatchNumber());
        target.setDateOfManufacture(source.getDateOfManufacture());
        target.setExpirationDate(source.getExpirationDate());
        target.setPackageSize(source.getPackageSize());
        target.setPackagingType(source.getPackagingType());
        target.setPackagingTypeOther(source.getPackagingTypeOther());
        target.setPlaceOfPurchase(source.getPlaceOfPurchase());
        target.setNameOfManufacturer(source.getNameOfManufacturer());
        target.setAddress(source.getAddress());
        target.setFoodTel(source.getFoodTel());
        target.setInvestigationNotes(source.getInvestigationNotes());
        target.setSuspectedDiagnosis(source.getSuspectedDiagnosis());
        target.setConfirmedDiagnosis(source.getConfirmedDiagnosis());
        target.setInvestigatedBy(source.getInvestigatedBy());
        target.setInvestigatorSignature(source.getInvestigatorSignature());
        target.setInvestigatorDate(source.getInvestigatorDate());
        target.setSurname(source.getSurname());
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setTelNo(source.getTelNo());
        target.setDateOfCompletionOfForm(source.getDateOfCompletionOfForm());
        target.setNameOfHealthFacility(source.getNameOfHealthFacility());
        target.setSpecifySources(source.getSpecifySources());


        return target;
    }

    @LocalBean
    @Stateless
    public static class SixtyDayFacadeEjbLocal extends SixtyDayFacadeEjb {

    }
}
