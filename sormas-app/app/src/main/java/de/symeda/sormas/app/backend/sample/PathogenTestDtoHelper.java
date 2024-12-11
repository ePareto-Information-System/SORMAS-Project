/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.sample;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.facility.FacilityDtoHelper;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.user.UserDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class PathogenTestDtoHelper extends AdoDtoHelper<PathogenTest, PathogenTestDto> {

	@Override
	protected Class<PathogenTest> getAdoClass() {
		return PathogenTest.class;
	}

	@Override
	protected Class<PathogenTestDto> getDtoClass() {
		return PathogenTestDto.class;
	}

	@Override
	protected Call<List<PathogenTestDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
		return RetroProvider.getSampleTestFacade().pullAllSince(since, size, lastSynchronizedUuid);
	}

	@Override
	protected Call<List<PathogenTestDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		return RetroProvider.getSampleTestFacade().pullByUuids(uuids);
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<PathogenTestDto> pathogenTestDtos) throws NoConnectionException {
		return RetroProvider.getSampleTestFacade().pushAll(pathogenTestDtos);
	}

	@Override
	protected void fillInnerFromDto(PathogenTest target, PathogenTestDto source) {

		target.setSample(DatabaseHelper.getSampleDao().getByReferenceDto(source.getSample()));
		target.setTestDateTime(source.getTestDateTime());
		target.setTestResult(source.getTestResult());
		target.setTestType(source.getTestType());
		target.setPcrTestSpecification(source.getPcrTestSpecification());
		target.setTestTypeText(source.getTestTypeText());
		target.setTestedDisease(source.getTestedDisease());
		target.setTestedDiseaseVariant(source.getTestedDiseaseVariant());
		target.setTestedDiseaseDetails(source.getTestedDiseaseDetails());
		target.setTestedDiseaseVariantDetails(source.getTestedDiseaseVariantDetails());
		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setTypingId(source.getTypingId());
		target.setTestResultVerified(source.getTestResultVerified());
		target.setTestResultText(source.getTestResultText());
		target.setFourFoldIncreaseAntibodyTiter(source.isFourFoldIncreaseAntibodyTiter());
		target.setSerotype(source.getSerotype());
		target.setCqValue(source.getCqValue());
		target.setReportDate(source.getReportDate());
		target.setDateLabResultsSentDistrict(source.getDateLabResultsSentDistrict());
		target.setDateLabResultsSentClinician(source.getDateLabResultsSentClinician());
		target.setFinalClassification(source.getFinalClassification());
		target.setViaLims(source.isViaLims());
		target.setLab(DatabaseHelper.getFacilityDao().getByReferenceDto(source.getLab()));
		target.setLabDetails(source.getLabDetails());
		target.setLabLocation(source.getLabLocation());
		target.setLabUser(DatabaseHelper.getUserDao().getByReferenceDto(source.getLabUser()));
		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setDateSurveillanceSentResultsToDistrict(source.getDateSurveillanceSentResultsToDistrict());
		target.setTestResultText(source.getTestResultText());
		target.setDateDistrictReceivedLabResults(source.getDateDistrictReceivedLabResults());
		target.setLaboratoryDateResultsSentDSD(source.getLaboratoryDateResultsSentDSD());
		target.setFinalClassification(source.getFinalClassification());
		target.setSampleTests(source.getSampleTests());
		target.setSampleTestResultPCR(source.getSampleTestResultPCR());
		target.setSampleTestResultPCRDate(source.getSampleTestResultPCRDate());
		target.setSampleTestResultAntigen(source.getSampleTestResultAntigen());
		target.setSampleTestResultAntigenDate(source.getSampleTestResultAntigenDate());
		target.setSampleTestResultIGM(source.getSampleTestResultIGM());
		target.setSampleTestResultIGMDate(source.getSampleTestResultIGMDate());
		target.setSampleTestResultIGG(source.getSampleTestResultIGG());
		target.setSampleTestResultIGGDate(source.getSampleTestResultIGGDate());
		target.setSampleTestResultImmuno(source.getSampleTestResultImmuno());
		target.setSampleTestResultImmunoDate(source.getSampleTestResultImmunoDate());
		target.setSecondTestedDisease(source.getSecondTestedDisease());
		target.setTestResultForSecondDisease(source.getTestResultForSecondDisease());
		target.setLaboratoryType(source.getLaboratoryType());
		target.setLaboratoryCytology(source.getLaboratoryCytology());
		target.setLaboratoryCytologyPmn(source.getLaboratoryCytologyPmn());
		target.setLaboratoryCytologyLymph(source.getLaboratoryCytologyLymph());
		target.setLaboratoryGram(source.getLaboratoryGram());
		target.setLaboratoryGramOther(source.getLaboratoryGramOther());
		target.setLaboratoryRdtPerformed(source.getLaboratoryRdtPerformed());
		target.setLaboratoryRdtResults(source.getLaboratoryRdtResults());
		target.setLaboratoryLatex(source.getLaboratoryLatex());
		target.setLaboratoryLatexOtherResults(source.getLaboratoryLatexOtherResults());
		target.setDateSentReportingHealthFac(source.getDateSentReportingHealthFac());
		target.setDateSampleSentRegRefLab(source.getDateSampleSentRegRefLab());
		target.setLaboratoryCulture(source.getLaboratoryCulture());
		target.setLaboratoryCultureOther(source.getLaboratoryCultureOther());
		target.setLaboratoryCeftriaxone(source.getLaboratoryCeftriaxone());
		target.setLaboratoryPenicillinG(source.getLaboratoryPenicillinG());
		target.setLaboratoryAmoxycillin(source.getLaboratoryAmoxycillin());
		target.setLaboratoryOxacillin(source.getLaboratoryOxacillin());
		target.setLaboratoryAntibiogramOther(source.getLaboratoryAntibiogramOther());
		target.setLaboratoryOtherTests(source.getLaboratoryOtherTests());
		target.setLaboratoryPcrOptions(source.getLaboratoryPcrOptions());
		target.setLaboratorySerotype(source.getLaboratorySerotype());
		target.setLaboratoryDatePcrPerformed(source.getLaboratoryDatePcrPerformed());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratoryFinalResults(source.getLaboratoryFinalResults());
		target.setLaboratoryFinalClassification(source.getLaboratoryFinalClassification());
		target.setDateSampleSentRegLab(source.getDateSampleSentRegLab());
		target.setTestResultVariant(source.getTestResultVariant());
		target.setVariantOtherSpecify(source.getVariantOtherSpecify());
		target.setVibrioCholeraeIdentifiedInStools(source.getVibrioCholeraeIdentifiedInStools());
		target.setDrugsSensitiveToVibrioStrain(source.getDrugsSensitiveToVibrioStrain());
		target.setDrugsResistantToVibrioStrain(source.getDrugsResistantToVibrioStrain());

		target.setPseudonymized(source.isPseudonymized());
	}

	@Override
	protected void fillInnerFromAdo(PathogenTestDto target, PathogenTest source) {
		if (source.getSample() != null) {
			Sample sample = DatabaseHelper.getSampleDao().queryForId(source.getSample().getId());
			target.setSample(SampleDtoHelper.toReferenceDto(sample));
		} else {
			target.setSample(null);
		}
		target.setTestDateTime(source.getTestDateTime());
		target.setTestResult(source.getTestResult());
		target.setTestType(source.getTestType());
		target.setPcrTestSpecification(source.getPcrTestSpecification());
		target.setTestTypeText(source.getTestTypeText());
		target.setTestedDisease(source.getTestedDisease());
		target.setTestedDiseaseVariant(source.getTestedDiseaseVariant());
		target.setTestedDiseaseDetails(source.getTestedDiseaseDetails());
		target.setTestedDiseaseVariantDetails(source.getTestedDiseaseVariantDetails());
		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setTypingId(source.getTypingId());
		target.setSecondTestedDisease(source.getSecondTestedDisease());
		target.setTestResultForSecondDisease(source.getTestResultForSecondDisease());

		if (source.getLab() != null) {
			Facility lab = DatabaseHelper.getFacilityDao().queryForId(source.getLab().getId());
			target.setLab(FacilityDtoHelper.toReferenceDto(lab));
		} else {
			target.setLab(null);
		}
		target.setLabDetails(source.getLabDetails());
		target.setLabLocation(source.getLabLocation());

		target.setTestResultVerified(source.getTestResultVerified());
		target.setTestResultText(source.getTestResultText());
		target.setFourFoldIncreaseAntibodyTiter(source.isFourFoldIncreaseAntibodyTiter());
		target.setSerotype(source.getSerotype());
		target.setCqValue(source.getCqValue());
		target.setReportDate(source.getReportDate());
		target.setDateLabResultsSentDistrict(source.getDateLabResultsSentDistrict());
		target.setDateLabResultsSentClinician(source.getDateLabResultsSentClinician());
		target.setFinalClassification(source.getFinalClassification());
		target.setViaLims(source.isViaLims());
		target.setSampleTests(source.getSampleTests());
		target.setSampleTestResultPCR(source.getSampleTestResultPCR());
		target.setSampleTestResultPCRDate(source.getSampleTestResultPCRDate());
		target.setSampleTestResultAntigen(source.getSampleTestResultAntigen());
		target.setSampleTestResultAntigenDate(source.getSampleTestResultAntigenDate());
		target.setSampleTestResultIGM(source.getSampleTestResultIGM());
		target.setSampleTestResultIGMDate(source.getSampleTestResultIGMDate());
		target.setSampleTestResultIGG(source.getSampleTestResultIGG());
		target.setSampleTestResultIGGDate(source.getSampleTestResultIGGDate());
		target.setSampleTestResultImmuno(source.getSampleTestResultImmuno());
		target.setSampleTestResultImmunoDate(source.getSampleTestResultImmunoDate());
		target.setLaboratoryType(source.getLaboratoryType());
		target.setLaboratoryCytology(source.getLaboratoryCytology());
		target.setLaboratoryCytologyPmn(source.getLaboratoryCytologyPmn());
		target.setLaboratoryCytologyLymph(source.getLaboratoryCytologyLymph());
		target.setLaboratoryGram(source.getLaboratoryGram());
		target.setLaboratoryGramOther(source.getLaboratoryGramOther());
		target.setLaboratoryRdtPerformed(source.getLaboratoryRdtPerformed());
		target.setLaboratoryRdtResults(source.getLaboratoryRdtResults());
		target.setLaboratoryLatex(source.getLaboratoryLatex());
		target.setLaboratoryLatexOtherResults(source.getLaboratoryLatexOtherResults());
		target.setDateSentReportingHealthFac(source.getDateSentReportingHealthFac());
		target.setDateSampleSentRegRefLab(source.getDateSampleSentRegRefLab());
		target.setLaboratoryCulture(source.getLaboratoryCulture());
		target.setLaboratoryCultureOther(source.getLaboratoryCultureOther());
		target.setLaboratoryCeftriaxone(source.getLaboratoryCeftriaxone());
		target.setLaboratoryPenicillinG(source.getLaboratoryPenicillinG());
		target.setLaboratoryAmoxycillin(source.getLaboratoryAmoxycillin());
		target.setLaboratoryOxacillin(source.getLaboratoryOxacillin());
		target.setLaboratoryAntibiogramOther(source.getLaboratoryAntibiogramOther());
		target.setLaboratoryOtherTests(source.getLaboratoryOtherTests());
		target.setLaboratoryPcrOptions(source.getLaboratoryPcrOptions());
		target.setLaboratorySerotype(source.getLaboratorySerotype());
		target.setLaboratoryDatePcrPerformed(source.getLaboratoryDatePcrPerformed());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratoryFinalResults(source.getLaboratoryFinalResults());
		target.setLaboratoryFinalClassification(source.getLaboratoryFinalClassification());
		target.setDateSampleSentRegLab(source.getDateSampleSentRegLab());
		target.setTestResultVariant(source.getTestResultVariant());
		target.setVariantOtherSpecify(source.getVariantOtherSpecify());

		if (source.getLabUser() != null) {
			User user = DatabaseHelper.getUserDao().queryForId(source.getLabUser().getId());
			target.setLabUser(UserDtoHelper.toReferenceDto(user));
		} else {
			target.setLabUser(null);
		}

		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setDateSurveillanceSentResultsToDistrict(source.getDateSurveillanceSentResultsToDistrict());
		target.setTestResultText(source.getTestResultText());
		target.setDateDistrictReceivedLabResults(source.getDateDistrictReceivedLabResults());
		target.setLaboratoryDateResultsSentDSD(source.getLaboratoryDateResultsSentDSD());
		target.setFinalClassification(source.getFinalClassification());
		target.setVibrioCholeraeIdentifiedInStools(source.getVibrioCholeraeIdentifiedInStools());
		target.setDrugsSensitiveToVibrioStrain(source.getDrugsSensitiveToVibrioStrain());
		target.setDrugsResistantToVibrioStrain(source.getDrugsResistantToVibrioStrain());

		target.setPseudonymized(source.isPseudonymized());
	}

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return PathogenTestDto.APPROXIMATE_JSON_SIZE_IN_BYTES;
    }
}
