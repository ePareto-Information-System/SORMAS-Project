/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.backend.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.person.PersonNameDto;
import de.symeda.sormas.api.sample.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.user.NotificationType;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;
import de.symeda.sormas.backend.FacadeHelper;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.common.CoreAdo;
import de.symeda.sormas.backend.common.NotificationService;
import de.symeda.sormas.backend.common.messaging.MessageContents;
import de.symeda.sormas.backend.common.messaging.MessageSubject;
import de.symeda.sormas.backend.common.messaging.NotificationDeliveryFailedException;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactFacadeEjb.ContactFacadeEjbLocal;
import de.symeda.sormas.backend.event.EventFacadeEjb.EventFacadeEjbLocal;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.event.EventParticipantFacadeEjb.EventParticipantFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.country.CountryFacadeEjb;
import de.symeda.sormas.backend.infrastructure.country.CountryService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserFacadeEjb;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.Pseudonymizer;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;

@Stateless(name = "PathogenTestFacade")
public class PathogenTestFacadeEjb implements PathogenTestFacade {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private CaseFacadeEjbLocal caseFacade;
	@EJB
	private ContactFacadeEjbLocal contactFacade;
	@EJB
	private EventParticipantFacadeEjbLocal eventParticipantFacade;
	@EJB
	private EventFacadeEjbLocal eventFacade;
	@EJB
	private PathogenTestService pathogenTestService;
	@EJB
	private SampleService sampleService;
	@EJB
	private FacilityService facilityService;
	@EJB
	private UserService userService;
	@EJB
	private NotificationService notificationService;
	@EJB
	private CountryService countryService;

	@Override
	public List<String> getAllActiveUuids() {
		User user = userService.getCurrentUser();

		if (user == null) {
			return Collections.emptyList();
		}

		return pathogenTestService.getAllActiveUuids(user);
	}

	@Override
	public List<PathogenTestDto> getAllActivePathogenTestsAfter(Date date) {
		return getAllActivePathogenTestsAfter(date, null, null);
	}

	@Override
	public List<PathogenTestDto> getAllActivePathogenTestsAfter(Date date, Integer batchSize, String lastSynchronizedUuid) {

		if (userService.getCurrentUser() == null) {
			return Collections.emptyList();
		}

		return toPseudonymizedDtos(pathogenTestService.getAllAfter(date, batchSize, lastSynchronizedUuid));
	}

	private List<PathogenTestDto> toPseudonymizedDtos(List<PathogenTest> entities) {

		List<Long> inJurisdictionIds = pathogenTestService.getInJurisdictionIds(entities);
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		List<PathogenTestDto> dtos =
			entities.stream().map(p -> convertToDto(p, pseudonymizer, inJurisdictionIds.contains(p.getId()))).collect(Collectors.toList());
		return dtos;
	}

	public List<PathogenTestDto> getIndexList(
		PathogenTestCriteria pathogenTestCriteria,
		Integer first,
		Integer max,
		List<SortProperty> sortProperties) {

		return toPseudonymizedDtos(pathogenTestService.getIndexList(pathogenTestCriteria, first, max, sortProperties));
	}

	public Page<PathogenTestDto> getIndexPage(
		PathogenTestCriteria pathogenTestCriteria,
		Integer offset,
		Integer size,
		List<SortProperty> sortProperties) {

		List<PathogenTestDto> pathogenTestList = getIndexList(pathogenTestCriteria, offset, size, sortProperties);
		long totalElementCount = pathogenTestService.count(pathogenTestCriteria);
		return new Page<>(pathogenTestList, offset, size, totalElementCount);

	}

	@Override
	public List<PathogenTestDto> getByUuids(List<String> uuids) {

		return toPseudonymizedDtos(pathogenTestService.getByUuids(uuids));
	}

	@Override
	public List<PathogenTestDto> getBySampleUuids(List<String> sampleUuids) {

		return toPseudonymizedDtos(pathogenTestService.getBySampleUuids(sampleUuids, false));
	}

	@Override
	public PathogenTestDto getLatestPathogenTest(String sampleUuid) {
		if (sampleUuid == null) {
			return null;
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PathogenTest> cq = cb.createQuery(PathogenTest.class);
		Root<PathogenTest> pathogenTestRoot = cq.from(PathogenTest.class);
		Join<PathogenTest, Sample> sampleJoin = pathogenTestRoot.join(PathogenTest.SAMPLE);

		Predicate filter = cb.and(cb.equal(sampleJoin.get(Sample.UUID), sampleUuid), cb.isFalse(pathogenTestRoot.get(CoreAdo.DELETED)));
		cq.where(filter);
		cq.orderBy(cb.desc(pathogenTestRoot.get(PathogenTest.CREATION_DATE)));

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return QueryHelper.getFirstResult(em, cq, t -> convertToDto(t, pseudonymizer));
	}

	@Override
	public List<PathogenTestDto> getAllBySample(SampleReferenceDto sampleRef) {

		if (sampleRef == null) {
			return Collections.emptyList();
		}

		List<PathogenTest> entities =
			sampleService.getByUuid(sampleRef.getUuid()).getPathogenTests().stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
		return toPseudonymizedDtos(entities);
	}

	@Override
	public List<PathogenTestDto> getByPersonNames(List<PersonNameDto> personNameDtos) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return pathogenTestService.getByPersonNames(personNameDtos).stream().map(c -> convertToDto(c, pseudonymizer)).collect(Collectors.toList());
	}

	@Override
	public List<String> getDeletedUuidsSince(Date since) {
		User user = userService.getCurrentUser();

		if (user == null) {
			return Collections.emptyList();
		}

		return pathogenTestService.getDeletedUuidsSince(since);
	}

	@Override
	public PathogenTestDto getByUuid(String uuid) {
		return convertToDto(pathogenTestService.getByUuid(uuid), Pseudonymizer.getDefault(userService::hasRight));
	}

	@Override
	public PathogenTestDto savePathogenTest(@Valid PathogenTestDto dto) {
		return savePathogenTest(dto, true, true);
	}

	public PathogenTestDto savePathogenTest(@Valid PathogenTestDto dto, boolean checkChangeDate, boolean syncShares) {
		PathogenTest existingSampleTest = pathogenTestService.getByUuid(dto.getUuid());
		FacadeHelper.checkCreateAndEditRights(existingSampleTest, userService, UserRight.PATHOGEN_TEST_CREATE, UserRight.PATHOGEN_TEST_EDIT);

		PathogenTestDto existingSampleTestDto = toDto(existingSampleTest);

		restorePseudonymizedDto(dto, existingSampleTest, existingSampleTestDto);

		PathogenTest pathogenTest = fillOrBuildEntity(dto, existingSampleTest, checkChangeDate);
		pathogenTestService.ensurePersisted(pathogenTest);

		onPathogenTestChanged(existingSampleTestDto, pathogenTest);

		handleAssociatedEntityChanges(pathogenTest, syncShares);

		return convertToDto(pathogenTest, Pseudonymizer.getDefault(userService::hasRight));
	}

	private void handleAssociatedEntityChanges(PathogenTest pathogenTest, boolean syncShares) {
		// Update case classification if necessary
		final Case associatedCase = pathogenTest.getSample().getAssociatedCase();
		if (associatedCase != null && userService.hasRight(UserRight.CASE_EDIT)) {
			caseFacade.onCaseChanged(caseFacade.toDto(associatedCase), associatedCase, syncShares);
		}

		// update contact if necessary
		Contact associatedContact = pathogenTest.getSample().getAssociatedContact();
		if (associatedContact != null && userService.hasRight(UserRight.CONTACT_EDIT)) {
			contactFacade.onContactChanged(contactFacade.toDto(associatedContact), syncShares);
		}

		// update event participant if necessary
		EventParticipant associatedEventParticipant = pathogenTest.getSample().getAssociatedEventParticipant();
		if (associatedEventParticipant != null && userService.hasRight(UserRight.EVENTPARTICIPANT_EDIT)) {
			eventParticipantFacade.onEventParticipantChanged(
				eventFacade.toDto(associatedEventParticipant.getEvent()),
				eventParticipantFacade.toDto(associatedEventParticipant),
				associatedEventParticipant,
				syncShares);
		}
	}

	@Override
	@RightsAllowed(UserRight._PATHOGEN_TEST_DELETE)
	public void deletePathogenTest(String pathogenTestUuid, DeletionDetails deletionDetails) {

		PathogenTest pathogenTest = pathogenTestService.getByUuid(pathogenTestUuid);
		pathogenTestService.delete(pathogenTest, deletionDetails);

		handleAssociatedEntityChanges(pathogenTest, true);
	}

	@Override
	public boolean hasPathogenTest(SampleReferenceDto sample) {
		Sample sampleEntity = sampleService.getByReferenceDto(sample);
		return pathogenTestService.hasPathogenTest(sampleEntity);
	}

	@Override
	public void validate(PathogenTestDto pathogenTest) throws ValidationRuntimeException {
		if (pathogenTest.getSample() == null) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validSample));
		}
		if (pathogenTest.getTestType() == null && (pathogenTest.getTestedDisease() != Disease.AHF || pathogenTest.getTestedDisease() != Disease.EVD || pathogenTest.getTestedDisease() != Disease.MARBURG || pathogenTest.getTestedDisease() != Disease.LASSA || pathogenTest.getTestedDisease() != Disease.DENGUE || pathogenTest.getTestedDisease() != Disease.CHIKUNGUNYA || pathogenTest.getTestedDisease() != Disease.ZIKA || pathogenTest.getTestedDisease() != Disease.YELLOW_FEVER)) {
			throw new ValidationRuntimeException(
				I18nProperties.getValidationError(
					Validations.required,
					I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.TEST_TYPE)));
		}
		if (pathogenTest.getTestedDisease() == null) {
			throw new ValidationRuntimeException(
				I18nProperties.getValidationError(
					Validations.required,
					I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.TESTED_DISEASE)));
		}
		if (pathogenTest.getLab() == null) {
			throw new ValidationRuntimeException(
				I18nProperties
					.getValidationError(Validations.required, I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.LAB)));
		}
		if ((pathogenTest.getTestResult() == null) && ((pathogenTest.getTestedDisease() != Disease.AHF) || (pathogenTest.getTestedDisease() != Disease.EVD) || (pathogenTest.getTestedDisease() != Disease.MARBURG) || (pathogenTest.getTestedDisease() != Disease.LASSA) || (pathogenTest.getTestedDisease() != Disease.DENGUE) || (pathogenTest.getTestedDisease() != Disease.CHIKUNGUNYA) || (pathogenTest.getTestedDisease() != Disease.ZIKA) || (pathogenTest.getTestedDisease() != Disease.YELLOW_FEVER))) {
			throw new ValidationRuntimeException(
				I18nProperties.getValidationError(
					Validations.required,
					I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.TEST_RESULT)));
		}
		if (pathogenTest.getTestResultVerified() == null) {
			throw new ValidationRuntimeException(
				I18nProperties.getValidationError(
					Validations.required,
					I18nProperties.getPrefixCaption(PathogenTestDto.I18N_PREFIX, PathogenTestDto.TEST_RESULT_VERIFIED)));
		}
	}

	@Override
	public Date getLatestPathogenTestDate(String sampleUuid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Date> cq = cb.createQuery(Date.class);
		Root<PathogenTest> pathogenTestRoot = cq.from(PathogenTest.class);
		Join<PathogenTest, Sample> sampleJoin = pathogenTestRoot.join(PathogenTest.SAMPLE);

		Predicate filter = cb.equal(sampleJoin.get(Sample.UUID), sampleUuid);
		cq.where(filter);
		cq.orderBy(cb.desc(pathogenTestRoot.get(PathogenTest.TEST_DATE_TIME)));
		cq.select(pathogenTestRoot.get(PathogenTest.TEST_DATE_TIME));

		return QueryHelper.getFirstResult(em, cq);
	}

	public List<PathogenTestDto> getPositiveOrLatest(List<String> sampleUuids) {

		Collection<PathogenTestDto> dtos = toPseudonymizedDtos(pathogenTestService.getBySampleUuids(sampleUuids, true)).stream()
			.collect(Collectors.toMap(s -> s.getSample().getUuid(), s -> s, (s1, s2) -> {

				// keep the positive one
				if (s1.getTestResult() == PathogenTestResultType.POSITIVE) {
					return s1;
				} else if (s2.getTestResult() == PathogenTestResultType.POSITIVE) {
					return s2;
				}

				// ordered by creation date by default, so always keep the first one
				return s1;
			}))
			.values();
		return new ArrayList<>(dtos);
	}

	public static PathogenTestDto toDto(PathogenTest source) {
		if (source == null) {
			return null;
		}

		PathogenTestDto target = new PathogenTestDto();
		DtoHelper.fillDto(target, source);

		target.setSample(SampleFacadeEjb.toReferenceDto(source.getSample()));
		target.setTestedDisease(source.getTestedDisease());
		target.setTestedDiseaseVariant(source.getTestedDiseaseVariant());
		target.setTestedDiseaseDetails(source.getTestedDiseaseDetails());
		target.setTestedDiseaseVariantDetails(source.getTestedDiseaseVariantDetails());
		target.setTypingId(source.getTypingId());
		target.setTestType(source.getTestType());
		target.setPcrTestSpecification(source.getPcrTestSpecification());
		target.setTestTypeText(source.getTestTypeText());
		target.setTestDateTime(source.getTestDateTime());
		target.setLab(FacilityFacadeEjb.toReferenceDto(source.getLab()));
		target.setLabDetails(source.getLabDetails());
		target.setLabUser(UserFacadeEjb.toReferenceDto(source.getLabUser()));
		target.setTestResult(source.getTestResult());
		target.setTestResultText(source.getTestResultText());
		target.setTestResultVerified(source.getTestResultVerified());
		target.setFourFoldIncreaseAntibodyTiter(source.isFourFoldIncreaseAntibodyTiter());
		target.setSerotype(source.getSerotype());
		target.setCqValue(source.getCqValue());
		target.setCtValueE(source.getCtValueE());
		target.setCtValueN(source.getCtValueN());
		target.setCtValueRdrp(source.getCtValueRdrp());
		target.setCtValueS(source.getCtValueS());
		target.setCtValueOrf1(source.getCtValueOrf1());
		target.setCtValueRdrpS(source.getCtValueRdrpS());
		target.setReportDate(source.getReportDate());
		target.setViaLims(source.isViaLims());
		target.setExternalId(source.getExternalId());
		target.setExternalOrderId(source.getExternalOrderId());
		target.setPreliminary(source.getPreliminary());

		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());

		target.setPrescriberPhysicianCode(source.getPrescriberPhysicianCode());
		target.setPrescriberFirstName(source.getPrescriberFirstName());
		target.setPrescriberLastName(source.getPrescriberLastName());
		target.setPrescriberPhoneNumber(source.getPrescriberPhoneNumber());
		target.setPrescriberAddress(source.getPrescriberAddress());
		target.setPrescriberPostalCode(source.getPrescriberPostalCode());
		target.setPrescriberCity(source.getPrescriberCity());
		target.setPrescriberCountry(CountryFacadeEjb.toReferenceDto(source.getPrescriberCountry()));
		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setFinalClassification(source.getFinalClassification());

		target.setLaboratoryType(source.getLaboratoryType());
		target.setLaboratoryName(source.getLaboratoryName());
		target.setLaboratoryTestPerformed(source.getLaboratoryTestPerformed());
		target.setLaboratoryTestPerformedOther(source.getLaboratoryTestPerformedOther());
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
		target.setLaboratoryOtherTests(source.getLaboratoryOtherTests());
		target.setLaboratoryOtherTestsResults(source.getLaboratoryOtherTestsResults());
		target.setLaboratoryCeftriaxone(source.getLaboratoryCeftriaxone());
		target.setLaboratoryPenicillinG(source.getLaboratoryPenicillinG());
		target.setLaboratoryAmoxycillin(source.getLaboratoryAmoxycillin());
		target.setLaboratoryOxacillin(source.getLaboratoryOxacillin());
		target.setLaboratoryAntibiogramOther(source.getLaboratoryAntibiogramOther());
		target.setDateSampleSentRegLab(source.getDateSampleSentRegLab());
		target.setLaboratoryDatePcrPerformed(source.getLaboratoryDatePcrPerformed());
		target.setLaboratoryPcrType(source.getLaboratoryPcrType());
		target.setLaboratoryPcrOptions(source.getLaboratoryPcrOptions());
		target.setLaboratorySerotype(source.getLaboratorySerotype());
		target.setLaboratorySerotypeType(source.getLaboratorySerotypeType());
		target.setLaboratorySerotypeResults(source.getLaboratorySerotypeResults());
		target.setLaboratoryFinalResults(source.getLaboratoryFinalResults());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratoryDateResultsSentHealthFacility(source.getLaboratoryDateResultsSentHealthFacility());
		target.setLaboratoryDateResultsSentDSD(source.getLaboratoryDateResultsSentDSD());
		target.setLaboratoryFinalClassification(source.getLaboratoryFinalClassification());
		target.setLabLocation(source.getLabLocation());
		target.setDateLabReceivedSpecimen(source.getDateLabReceivedSpecimen());
		target.setSpecimenCondition(source.getSpecimenCondition());
		target.setDateLabResultsSentDistrict(source.getDateLabResultsSentDistrict());
		target.setDateLabResultsSentClinician(source.getDateLabResultsSentClinician());
		target.setDateDistrictReceivedLabResults(source.getDateDistrictReceivedLabResults());
		target.setTestResultVariant(source.getTestResultVariant());
		target.setVariantOtherSpecify(source.getVariantOtherSpecify());
		target.setSecondTestedDisease(source.getSecondTestedDisease());
		target.setTestResultForSecondDisease(source.getTestResultForSecondDisease());
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


		target.setVibrioCholeraeIdentifiedInStools(source.getVibrioCholeraeIdentifiedInStools());
		target.setDrugsSensitiveToVibrioStrain(source.getDrugsSensitiveToVibrioStrain());
		target.setDrugsResistantToVibrioStrain(source.getDrugsResistantToVibrioStrain());

		target.setOtherNotesAndObservations(source.getOtherNotesAndObservations());
		target.setDateSurveillanceSentResultsToDistrict(source.getDateSurveillanceSentResultsToDistrict());
		return target;
	}

	public PathogenTestDto convertToDto(PathogenTest source, Pseudonymizer pseudonymizer) {

		if (source == null) {
			return null;
		}

		boolean inJurisdiction = pathogenTestService.inJurisdictionOrOwned(source);
		return convertToDto(source, pseudonymizer, inJurisdiction);
	}

	private PathogenTestDto convertToDto(PathogenTest source, Pseudonymizer pseudonymizer, boolean inJurisdiction) {

		PathogenTestDto target = toDto(source);
		pseudonymizeDto(source, target, pseudonymizer, inJurisdiction);
		return target;
	}

	private void pseudonymizeDto(PathogenTest source, PathogenTestDto target, Pseudonymizer pseudonymizer, boolean inJurisdiction) {

		if (source != null && target != null) {
			pseudonymizer.pseudonymizeDto(PathogenTestDto.class, target, inJurisdiction, null);
		}
	}

	private void restorePseudonymizedDto(PathogenTestDto dto, PathogenTest existingSampleTest, PathogenTestDto existingSampleTestDto) {

		if (existingSampleTestDto != null) {
			boolean isInJurisdiction = pathogenTestService.inJurisdictionOrOwned(existingSampleTest);
			Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);

			pseudonymizer.restorePseudonymizedValues(PathogenTestDto.class, dto, existingSampleTestDto, isInJurisdiction);
		}
	}

	public PathogenTest fillOrBuildEntity(@NotNull PathogenTestDto source, PathogenTest target, boolean checkChangeDate) {
		target = DtoHelper.fillOrBuildEntity(source, target, PathogenTest::new, checkChangeDate);

		target.setSample(sampleService.getByReferenceDto(source.getSample()));
		target.setTestedDisease(source.getTestedDisease());
		target.setTestedDiseaseVariant(source.getTestedDiseaseVariant());
		target.setTestedDiseaseDetails(source.getTestedDiseaseDetails());
		target.setTestedDiseaseVariantDetails(source.getTestedDiseaseVariantDetails());
		target.setTypingId(source.getTypingId());
		target.setTestType(source.getTestType());
		target.setPcrTestSpecification(source.getPcrTestSpecification());
		target.setTestTypeText(source.getTestTypeText());
		target.setTestDateTime(source.getTestDateTime());
		target.setLab(facilityService.getByReferenceDto(source.getLab()));
		target.setLabDetails(source.getLabDetails());
		target.setLabUser(userService.getByReferenceDto(source.getLabUser()));
		target.setTestResult(source.getTestResult());
		target.setTestResultText(source.getTestResultText());
		target.setTestResultVerified(source.getTestResultVerified());
		target.setFourFoldIncreaseAntibodyTiter(source.isFourFoldIncreaseAntibodyTiter());
		target.setSerotype(source.getSerotype());
		target.setCqValue(source.getCqValue());
		target.setCtValueE(source.getCtValueE());
		target.setCtValueN(source.getCtValueN());
		target.setCtValueRdrp(source.getCtValueRdrp());
		target.setCtValueS(source.getCtValueS());
		target.setCtValueOrf1(source.getCtValueOrf1());
		target.setCtValueRdrpS(source.getCtValueRdrpS());
		target.setReportDate(source.getReportDate());
		target.setViaLims(source.isViaLims());
		target.setExternalId(source.getExternalId());
		target.setExternalOrderId(source.getExternalOrderId());
		target.setPreliminary(source.getPreliminary());

		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());

		target.setPrescriberPhysicianCode(source.getPrescriberPhysicianCode());
		target.setPrescriberFirstName(source.getPrescriberFirstName());
		target.setPrescriberLastName(source.getPrescriberLastName());
		target.setPrescriberPhoneNumber(source.getPrescriberPhoneNumber());
		target.setPrescriberAddress(source.getPrescriberAddress());
		target.setPrescriberPostalCode(source.getPrescriberPostalCode());
		target.setPrescriberCity(source.getPrescriberCity());
		target.setPrescriberCountry(countryService.getByReferenceDto(source.getPrescriberCountry()));
		target.setVirusDetectionGenotype(source.getVirusDetectionGenotype());
		target.setFinalClassification(source.getFinalClassification());

		target.setLaboratoryType(source.getLaboratoryType());
		target.setLaboratoryName(source.getLaboratoryName());
		target.setLaboratoryTestPerformed(source.getLaboratoryTestPerformed());
		target.setLaboratoryTestPerformedOther(source.getLaboratoryTestPerformedOther());
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
		target.setLaboratoryOtherTests(source.getLaboratoryOtherTests());
		target.setLaboratoryOtherTestsResults(source.getLaboratoryOtherTestsResults());
		target.setLaboratoryCeftriaxone(source.getLaboratoryCeftriaxone());
		target.setLaboratoryPenicillinG(source.getLaboratoryPenicillinG());
		target.setLaboratoryAmoxycillin(source.getLaboratoryAmoxycillin());
		target.setLaboratoryOxacillin(source.getLaboratoryOxacillin());
		target.setLaboratoryAntibiogramOther(source.getLaboratoryAntibiogramOther());
		target.setDateSampleSentRegLab(source.getDateSampleSentRegLab());
		target.setLaboratoryDatePcrPerformed(source.getLaboratoryDatePcrPerformed());
		target.setLaboratoryPcrType(source.getLaboratoryPcrType());
		target.setLaboratoryPcrOptions(source.getLaboratoryPcrOptions());
		target.setLaboratorySerotype(source.getLaboratorySerotype());
		target.setLaboratorySerotypeType(source.getLaboratorySerotypeType());
		target.setLaboratorySerotypeResults(source.getLaboratorySerotypeResults());
		target.setLaboratoryFinalResults(source.getLaboratoryFinalResults());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratoryDateResultsSentHealthFacility(source.getLaboratoryDateResultsSentHealthFacility());
		target.setLaboratoryDateResultsSentDSD(source.getLaboratoryDateResultsSentDSD());
		target.setLaboratoryFinalClassification(source.getLaboratoryFinalClassification());
		target.setLabLocation(source.getLabLocation());
		target.setDateLabReceivedSpecimen(source.getDateLabReceivedSpecimen());
		target.setSpecimenCondition(source.getSpecimenCondition());
		target.setDateLabResultsSentDistrict(source.getDateLabResultsSentDistrict());
		target.setDateLabResultsSentClinician(source.getDateLabResultsSentClinician());
		target.setDateDistrictReceivedLabResults(source.getDateDistrictReceivedLabResults());
		target.setTestResultVariant(source.getTestResultVariant());
		target.setVariantOtherSpecify(source.getVariantOtherSpecify());
		target.setSecondTestedDisease(source.getSecondTestedDisease());
		target.setTestResultForSecondDisease(source.getTestResultForSecondDisease());
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

		target.setVibrioCholeraeIdentifiedInStools(source.getVibrioCholeraeIdentifiedInStools());
		target.setDrugsSensitiveToVibrioStrain(source.getDrugsSensitiveToVibrioStrain());
		target.setDrugsResistantToVibrioStrain(source.getDrugsResistantToVibrioStrain());

		target.setOtherNotesAndObservations(source.getOtherNotesAndObservations());
		target.setDateSurveillanceSentResultsToDistrict(source.getDateSurveillanceSentResultsToDistrict());
		return target;
	}

	private void onPathogenTestChanged(PathogenTestDto existingPathogenTest, PathogenTest newPathogenTest) {
		// Send an email to all responsible supervisors when a new non-pending sample test is created or the status of
		// a formerly pending test result has changed
		final String sampleUuid = newPathogenTest.getSample().getUuid();
		final Sample sample = sampleService.getByUuid(sampleUuid);
		final Case caze = sample.getAssociatedCase();
		final Contact contact = sample.getAssociatedContact();
		final EventParticipant eventParticipant = sample.getAssociatedEventParticipant();

		Disease disease = null;
		Set<NotificationType> notificationTypes = new HashSet<>();
		List<Region> regions = new ArrayList<>();

		if (caze != null) {
			disease = caze.getDisease();
			notificationTypes.add(NotificationType.CASE_LAB_RESULT_ARRIVED);
			regions.addAll(JurisdictionHelper.getCaseRegions(caze));
		}

		if (contact != null) {
			disease = contact.getDisease() != null ? contact.getDisease() : contact.getCaze().getDisease();
			notificationTypes.add(NotificationType.CONTACT_LAB_RESULT_ARRIVED);
			regions.addAll(JurisdictionHelper.getContactRegions(contact));
		}

		if (eventParticipant != null) {
			disease = eventParticipant.getEvent().getDisease();
			notificationTypes.add(NotificationType.EVENT_PARTICIPANT_LAB_RESULT_ARRIVED);
			regions.add(eventParticipant.getRegion());

			if (disease == null) {
				sendMessageOnPathogenTestChanged(
					existingPathogenTest,
					newPathogenTest,
					null,
					notificationTypes,
					regions,
					MessageContents.CONTENT_LAB_RESULT_ARRIVED_EVENT_PARTICIPANT_NO_DISEASE,
					DataHelper.getShortUuid(eventParticipant.getUuid()));
			}
		}

		if (disease != null) {
			final String contentLabResultArrived = caze != null
				? MessageContents.CONTENT_LAB_RESULT_ARRIVED
				: contact != null ? MessageContents.CONTENT_LAB_RESULT_ARRIVED_CONTACT : MessageContents.CONTENT_LAB_RESULT_ARRIVED_EVENT_PARTICIPANT;

			final String shortUuid =
				DataHelper.getShortUuid(caze != null ? caze.getUuid() : contact != null ? contact.getUuid() : eventParticipant.getUuid());
			sendMessageOnPathogenTestChanged(
				existingPathogenTest,
				newPathogenTest,
				disease,
				notificationTypes,
				regions,
				contentLabResultArrived,
				shortUuid);
		}
	}

	private void sendMessageOnPathogenTestChanged(
		PathogenTestDto existingPathogenTest,
		PathogenTest newPathogenTest,
		Disease disease,
		Set<NotificationType> notificationTypes,
		List<Region> regions,
		String contentLabResultArrived,
		String shortUuid) {
		boolean isNewTestWithResult = existingPathogenTest == null && newPathogenTest.getTestResult() != PathogenTestResultType.PENDING;
		boolean testResultChanged = existingPathogenTest != null
			&& existingPathogenTest.getTestResult() == PathogenTestResultType.PENDING
			&& newPathogenTest.getTestResult() != PathogenTestResultType.PENDING;
		if (newPathogenTest.getTestResult() != null && isNewTestWithResult || testResultChanged) {
			try {
				String message = String.format(
					I18nProperties.getString(contentLabResultArrived),
					newPathogenTest.getTestResult().toString(),
					disease,
					shortUuid,
					newPathogenTest.getTestType(),
					newPathogenTest.getTestedDisease());

				notificationService.sendNotifications(notificationTypes, regions, null, MessageSubject.LAB_RESULT_ARRIVED, message);
			} catch (NotificationDeliveryFailedException e) {
				logger.error("EmailDeliveryFailedException when trying to notify supervisors " + "about the arrival of a lab result.");
			}
		}
	}

	@LocalBean
	@Stateless
	public static class PathogenTestFacadeEjbLocal extends PathogenTestFacadeEjb {
	}

	@Override
	public long count(SampleCriteria sampleCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		final Root<PathogenTest> pathogenRoot = cq.from(PathogenTest.class);

		Predicate filter = null;

		if (sampleCriteria != null) {
			filter = pathogenTestService.buildPathogenCriteriaFilter(sampleCriteria, cb, pathogenRoot);
			cq.where(filter);
		}
		cq.select(cb.count(pathogenRoot));

		Long count = em.createQuery(cq).getSingleResult();
		return count;
	}

	@Override
	public Boolean checkIfPathogenTestIsTheFirst(String sampleUuid, String pathogenTestUuid) {
		//return false if sampleUuid or pathogenTestUuid is null
		if (sampleUuid == null || pathogenTestUuid == null) {
			return false;
		}

		//get sample and its first pathogen test
		Sample sample = sampleService.getByUuid(sampleUuid);
		//get associated pathogen tests and Order by creation date and select first
		PathogenTest firstPathogenTest = sample.getPathogenTests().stream().sorted((p1, p2) -> p1.getCreationDate().compareTo(p2.getCreationDate())).findFirst().orElse(null);

		//check if the first pathogen test is the same as the pathogen test
		if (firstPathogenTest != null && firstPathogenTest.getUuid().equals(pathogenTestUuid)) {
			return true;
		}

		return false;
	}
}
