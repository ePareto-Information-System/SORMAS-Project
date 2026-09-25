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

package de.symeda.sormas.backend.caze;

import com.fasterxml.jackson.databind.JsonNode;
import de.symeda.sormas.api.*;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.caze.*;
import de.symeda.sormas.api.caze.maternalhistory.MaternalHistoryDto;
import de.symeda.sormas.api.caze.porthealthinfo.PortHealthInfoDto;
import de.symeda.sormas.api.clinicalcourse.*;
import de.symeda.sormas.api.common.CoreEntityType;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.dashboard.DashboardCaseDto;
import de.symeda.sormas.api.deletionconfiguration.DeletionReference;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.document.DocumentRelatedEntityType;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.epidata.EpiDataHelper;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.exposure.ExposureType;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolException;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.feature.FeatureTypeProperty;
import de.symeda.sormas.api.followup.FollowUpDto;
import de.symeda.sormas.api.followup.FollowUpLogic;
import de.symeda.sormas.api.followup.FollowUpPeriodDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.immunization.MeansOfImmunization;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.infrastructure.InfrastructureHelper;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.location.LocationReferenceDto;
import de.symeda.sormas.api.messaging.ManualMessageLogDto;
import de.symeda.sormas.api.messaging.MessageType;
import de.symeda.sormas.api.person.*;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.sormastosormas.ShareTreeCriteria;
import de.symeda.sormas.api.sormastosormas.SormasToSormasException;
import de.symeda.sormas.api.sormastosormas.SormasToSormasRuntimeException;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.symptoms.SymptomsHelper;
import de.symeda.sormas.api.task.*;
import de.symeda.sormas.api.therapy.*;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.user.NotificationType;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.DataHelper.Pair;
import de.symeda.sormas.api.utils.fieldaccess.checkers.UserRightFieldAccessChecker;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.vaccination.VaccinationDto;
import de.symeda.sormas.api.visit.VisitDto;
import de.symeda.sormas.api.visit.VisitResultDto;
import de.symeda.sormas.api.visit.VisitStatus;
import de.symeda.sormas.backend.ExtendedPostgreSQL94Dialect;
import de.symeda.sormas.backend.FacadeHelper;
import de.symeda.sormas.backend.afpimmunization.AfpImmunization;
import de.symeda.sormas.backend.afpimmunization.AfpImmunizationFacadeEjb;
import de.symeda.sormas.backend.afpimmunization.AfpImmunizationFacadeEjb.AfpImmunizationEjbLocal;
import de.symeda.sormas.backend.caze.classification.CaseClassificationFacadeEjb.CaseClassificationFacadeEjbLocal;
import de.symeda.sormas.backend.caze.maternalhistory.MaternalHistoryFacadeEjb;
import de.symeda.sormas.backend.caze.maternalhistory.MaternalHistoryFacadeEjb.MaternalHistoryFacadeEjbLocal;
import de.symeda.sormas.backend.caze.porthealthinfo.PortHealthInfoFacadeEjb;
import de.symeda.sormas.backend.caze.porthealthinfo.PortHealthInfoFacadeEjb.PortHealthInfoFacadeEjbLocal;
import de.symeda.sormas.backend.caze.surveillancereport.SurveillanceReportFacadeEjb;
import de.symeda.sormas.backend.caze.surveillancereport.SurveillanceReportService;
import de.symeda.sormas.backend.clinicalcourse.*;
import de.symeda.sormas.backend.clinicalcourse.ClinicalCourseFacadeEjb.ClinicalCourseFacadeEjbLocal;
import de.symeda.sormas.backend.clinicalcourse.ClinicalVisitFacadeEjb.ClinicalVisitFacadeEjbLocal;
import de.symeda.sormas.backend.common.*;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.common.messaging.*;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactFacadeEjb.ContactFacadeEjbLocal;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.contact.VisitSummaryExportDetails;
import de.symeda.sormas.backend.customizableenum.CustomizableEnumFacadeEjb.CustomizableEnumFacadeEjbLocal;
import de.symeda.sormas.backend.disease.DiseaseConfigurationFacadeEjb.DiseaseConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.document.Document;
import de.symeda.sormas.backend.document.DocumentService;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.epidata.EpiDataFacadeEjb;
import de.symeda.sormas.backend.epidata.EpiDataFacadeEjb.EpiDataFacadeEjbLocal;
import de.symeda.sormas.backend.epidata.EpiDataService;
import de.symeda.sormas.backend.event.*;
import de.symeda.sormas.backend.exposure.Exposure;
import de.symeda.sormas.backend.exposure.ExposureService;
import de.symeda.sormas.backend.externaljournal.ExternalJournalService;
import de.symeda.sormas.backend.externalsurveillancetool.ExternalSurveillanceToolGatewayFacadeEjb.ExternalSurveillanceToolGatewayFacadeEjbLocal;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.foodhistory.FoodHistory;
import de.symeda.sormas.backend.foodhistory.FoodHistoryFacadeEjb;
import de.symeda.sormas.backend.foodhistory.FoodHistoryFacadeEjb.FoodHistoryEjbLocal;
import de.symeda.sormas.backend.hospitalization.Hospitalization;
import de.symeda.sormas.backend.hospitalization.HospitalizationFacadeEjb;
import de.symeda.sormas.backend.hospitalization.HospitalizationFacadeEjb.HospitalizationFacadeEjbLocal;
import de.symeda.sormas.backend.hospitalization.PreviousHospitalization;
import de.symeda.sormas.backend.immunization.ImmunizationEntityHelper;
import de.symeda.sormas.backend.immunization.entity.Immunization;
import de.symeda.sormas.backend.importexport.ExportHelper;
import de.symeda.sormas.backend.infrastructure.PopulationDataFacadeEjb.PopulationDataFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb.CommunityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb.DistrictFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb.FacilityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntry;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntryFacadeEjb;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntryService;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.investigationnotes.InvestigationNotes;
import de.symeda.sormas.backend.investigationnotes.InvestigationNotesFacadeEjb;
import de.symeda.sormas.backend.investigationnotes.InvestigationNotesFacadeEjb.InvestigationNotesFacadeEjbLocal;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.outbreak.Outbreak;
import de.symeda.sormas.backend.outbreak.OutbreakService;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.person.PersonFacadeEjb;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonService;
import de.symeda.sormas.backend.riskfactor.RiskFactorFacadeEjb;
import de.symeda.sormas.backend.sample.*;
import de.symeda.sormas.backend.sample.AdditionalTestFacadeEjb.AdditionalTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTestFacadeEjb.PathogenTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.SampleFacadeEjb.SampleFacadeEjbLocal;
import de.symeda.sormas.backend.share.ExternalShareInfoCountAndLatestDate;
import de.symeda.sormas.backend.share.ExternalShareInfoService;
import de.symeda.sormas.backend.sixtyday.SixtyDay;
import de.symeda.sormas.backend.sixtyday.SixtyDayFacadeEjb;
import de.symeda.sormas.backend.sixtyday.SixtyDayFacadeEjb.SixtyDayFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeEjb.SormasToSormasFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.entities.caze.SormasToSormasCaseFacadeEjb.SormasToSormasCaseFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoService;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.ShareInfoHelper;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.ShareRequestInfoService;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfo;
import de.symeda.sormas.backend.symptoms.Symptoms;
import de.symeda.sormas.backend.symptoms.SymptomsFacadeEjb;
import de.symeda.sormas.backend.symptoms.SymptomsFacadeEjb.SymptomsFacadeEjbLocal;
import de.symeda.sormas.backend.task.Task;
import de.symeda.sormas.backend.task.TaskService;
import de.symeda.sormas.backend.therapy.*;
import de.symeda.sormas.backend.therapy.PrescriptionFacadeEjb.PrescriptionFacadeEjbLocal;
import de.symeda.sormas.backend.therapy.TherapyFacadeEjb.TherapyFacadeEjbLocal;
import de.symeda.sormas.backend.therapy.TreatmentFacadeEjb.TreatmentFacadeEjbLocal;
import de.symeda.sormas.backend.travelentry.TravelEntry;
import de.symeda.sormas.backend.travelentry.services.TravelEntryService;
import de.symeda.sormas.backend.user.*;
import de.symeda.sormas.backend.util.*;
import de.symeda.sormas.backend.vaccination.Vaccination;
import de.symeda.sormas.backend.vaccination.VaccinationFacadeEjb;
import de.symeda.sormas.backend.vaccination.VaccinationService;
import de.symeda.sormas.backend.visit.Visit;
import de.symeda.sormas.backend.visit.VisitFacadeEjb.VisitFacadeEjbLocal;
import de.symeda.sormas.backend.visit.VisitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.symeda.sormas.backend.common.CriteriaBuilderHelper.and;
import static de.symeda.sormas.backend.common.CriteriaBuilderHelper.or;
import static de.symeda.sormas.backend.visit.VisitLogic.getVisitResult;
import static java.util.Objects.isNull;

@Stateless(name = "CaseFacade")
@RightsAllowed(UserRight._CASE_VIEW)
public class CaseFacadeEjb extends AbstractCoreFacadeEjb<Case, CaseDataDto, CaseIndexDto, CaseReferenceDto, CaseService, CaseCriteria>
		implements CaseFacade {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private CaseClassificationFacadeEjbLocal caseClassificationFacade;
	@EJB
	private CaseListCriteriaBuilder listQueryBuilder;
	@EJB
	private PersonService personService;
	@EJB
	private FacilityService facilityService;
	@EJB
	private VisitService visitService;
	@EJB
	private VisitFacadeEjbLocal visitFacade;
	@EJB
	private SymptomsFacadeEjbLocal symptomsFacade;
	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private DistrictFacadeEjbLocal districtFacade;

	@EJB
	private CommunityFacadeEjbLocal communityFacade;
	@EJB
	private CommunityService communityService;
	@EJB
	private FacilityFacadeEjbLocal facilityFacade;
	@EJB
	private TaskService taskService;
	@EJB
	private ContactService contactService;
	@EJB
	private EventParticipantService eventParticipantService;
	@EJB
	private EventService eventService;
	@EJB
	private SampleService sampleService;
	@EJB
	private PathogenTestService pathogenTestService;
	@EJB
	private PathogenTestFacadeEjbLocal sampleTestFacade;
	@EJB
	private HospitalizationFacadeEjbLocal hospitalizationFacade;
	@EJB
	private SixtyDayFacadeEjbLocal sixtyDayFacade;
	@EJB
	private InvestigationNotesFacadeEjbLocal investigationNotesFacade;
	@EJB
	private AfpImmunizationEjbLocal afpImmunizationFacade;
	@EJB
	private FoodHistoryEjbLocal foodHistoryFacade;
	@EJB
	private RiskFactorFacadeEjb.RiskFactorFacadeEjbLocal riskFactorFacade;
	@EJB
	private EpiDataFacadeEjbLocal epiDataFacade;
	@EJB
	private ContactFacadeEjbLocal contactFacade;
	@EJB
	private SampleFacadeEjbLocal sampleFacade;
	@EJB
	private TreatmentFacadeEjbLocal treatmentFacade;
	@EJB
	private PrescriptionFacadeEjbLocal prescriptionFacade;
	@EJB
	private ClinicalVisitFacadeEjbLocal clinicalVisitFacade;
	@EJB
	private MessagingService messagingService;
	@EJB
	private NotificationService notificationService;
	@EJB
	private PersonFacadeEjbLocal personFacade;
	@EJB
	private ConfigFacadeEjbLocal configFacade;
	@EJB
	private TherapyFacadeEjbLocal therapyFacade;
	@EJB
	private ClinicalCourseFacadeEjbLocal clinicalCourseFacade;
	@EJB
	private PrescriptionService prescriptionService;
	@EJB
	private TreatmentService treatmentService;
	@EJB
	private ClinicalVisitService clinicalVisitService;
	@EJB
	private ExposureService exposureService;
	@EJB
	private OutbreakService outbreakService;
	@EJB
	private MaternalHistoryFacadeEjbLocal maternalHistoryFacade;
	@EJB
	private PointOfEntryService pointOfEntryService;
	@EJB
	private PortHealthInfoFacadeEjbLocal portHealthInfoFacade;
	@EJB
	private PopulationDataFacadeEjbLocal populationDataFacade;
	@EJB
	private FeatureConfigurationFacadeEjbLocal featureConfigurationFacade;
	@EJB
	private UserRoleFacadeEjb.UserRoleFacadeEjbLocal userRoleFacade;
	@EJB
	private SormasToSormasOriginInfoService originInfoService;
	@EJB
	private ManualMessageLogService manualMessageLogService;
	@EJB
	private AdditionalTestFacadeEjbLocal additionalTestFacade;
	@EJB
	private ExternalJournalService externalJournalService;
	@EJB
	private DiseaseConfigurationFacadeEjbLocal diseaseConfigurationFacade;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolGatewayFacade;
	@EJB
	private ExternalShareInfoService externalShareInfoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private SurveillanceReportService surveillanceReportService;
	@EJB
	private EpiDataService epiDataService;
	@EJB
	private SurveillanceReportFacadeEjb.SurveillanceReportFacadeEjbLocal surveillanceReportFacade;
	@EJB
	private SormasToSormasFacadeEjbLocal sormasToSormasFacade;
	@EJB
	private SormasToSormasCaseFacadeEjbLocal sormasToSormasCaseFacade;
	@EJB
	private ShareRequestInfoService shareRequestInfoService;
	@EJB
	private VaccinationFacadeEjb.VaccinationFacadeEjbLocal vaccinationFacade;
	@EJB
	private HealthConditionsMapper healthConditionsMapper;
	@EJB
	private TravelEntryService travelEntryService;
	@EJB
	private VaccinationService vaccinationService;
	@EJB
	private CaseService caseService;
	@EJB
	private UserRoleService userRoleService;
	@EJB
	private CustomizableEnumFacadeEjbLocal customizableEnumFacade;

	@Resource
	private ManagedScheduledExecutorService executorService;

	public CaseFacadeEjb() {
	}

	@Inject
	public CaseFacadeEjb(CaseService service) {
		super(Case.class, CaseDataDto.class, service);
	}

	@Override
	protected Pseudonymizer createPseudonymizer() {
		return getPseudonymizerForDtoWithClinician("");
	}

	private Pseudonymizer getPseudonymizerForDtoWithClinician(@Nullable String pseudonymizedValue) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, pseudonymizedValue);

		UserRightFieldAccessChecker clinicianViewRightChecker =
				new UserRightFieldAccessChecker(UserRight.CASE_CLINICIAN_VIEW, userService.hasRight(UserRight.CASE_CLINICIAN_VIEW));
		pseudonymizer.addFieldAccessChecker(clinicianViewRightChecker, clinicianViewRightChecker);

		return pseudonymizer;
	}

	public Page<CaseIndexDto> getIndexPage(CaseCriteria caseCriteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
		List<CaseIndexDto> caseIndexList = getIndexList(caseCriteria, offset, size, sortProperties);
		long totalElementCount = count(caseCriteria);
		return new Page<>(caseIndexList, offset, size, totalElementCount);
	}

	@Override
	public String getUuidByUuidEpidNumberOrExternalId(String searchTerm, CaseCriteria caseCriteria) {
		return service.getUuidByUuidEpidNumberOrExternalId(searchTerm, caseCriteria);
	}

	@Override
	public long count(CaseCriteria caseCriteria) {

		return count(caseCriteria, false);
	}

	@Override
	public long count(CaseCriteria caseCriteria, boolean ignoreUserFilter) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Case> root = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, root);

		Predicate filter = null;

		if (!ignoreUserFilter) {
			CaseUserFilterCriteria caseUserFilterCriteria = new CaseUserFilterCriteria();
			if (caseCriteria != null) {
				caseUserFilterCriteria.setIncludeCasesFromOtherJurisdictions(caseCriteria.getIncludeCasesFromOtherJurisdictions());
			}
			filter = service.createUserFilter(caseQueryContext, caseUserFilterCriteria);
		}

		if (caseCriteria != null) {
			Predicate criteriaFilter = service.createCriteriaFilter(caseCriteria, caseQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}
		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.countDistinct(root));
		return em.createQuery(cq).getSingleResult();
	}

	public Page<CaseIndexDetailedDto> getIndexDetailedPage(
			CaseCriteria caseCriteria,
			Integer offset,
			Integer size,
			List<SortProperty> sortProperties) {
		List<CaseIndexDetailedDto> caseIndexDetailedList = getIndexDetailedList(caseCriteria, offset * size, size, sortProperties);
		long totalElementCount = count(caseCriteria);
		return new Page<>(caseIndexDetailedList, offset, size, totalElementCount);
	}

	public List<CaseDataDto> getAllActiveCasesAfter(Date date) {
		return getAllActiveCasesAfter(date, false);
	}

	@Override
	public List<CaseDataDto> getAllActiveCasesAfter(Date date, boolean includeExtendedChangeDateFilters) {
		return getAllActiveCasesAfter(date, includeExtendedChangeDateFilters, null, null);
	}

	public List<CaseDataDto> getAllActiveCasesAfter(Date date, Integer batchSize, String lastSynchronizedUuid) {
		return getAllActiveCasesAfter(date, false, batchSize, lastSynchronizedUuid);
	}

	private List<CaseDataDto> getAllActiveCasesAfter(
			Date date,
			boolean includeExtendedChangeDateFilters,
			Integer batchSize,
			String lastSynchronizedUuid) {

		if (userService.getCurrentUser() == null) {
			return Collections.emptyList();
		}

		Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");
		return service.getAllActiveCasesAfter(date, includeExtendedChangeDateFilters, batchSize, lastSynchronizedUuid)
				.stream()
				.map(c -> convertToDto(c, pseudonymizer))
				.collect(Collectors.toList());
	}

	public List<MapCaseDto> getIndexListForMap(
			CaseCriteria caseCriteria,
			Integer first,
			Integer max,
			String userUuid,
			List<SortProperty> sortProperties) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MapCaseDto> cq = cb.createQuery(MapCaseDto.class);
		Root<Case> caze = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		selectMapDtoFields(cb, cq, caze, caseQueryContext);

		Predicate filter = caseService.createUserFilter(cb, cq, caze);

		if (caseCriteria != null) {
			Predicate criteriaFilter = caseService.createCriteriaFilter(caseCriteria, caseQueryContext);

			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		if (first != null && max != null) {
			TypedQuery typedQuery = em.createQuery(cq);
			String hqlQueryString=typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
			ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
			SessionImplementor hibernateSession = em.unwrap(SessionImplementor.class);
			QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("", hqlQueryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
			queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
			String sqlQueryString = queryTranslator.getSQLString();
			return em.createQuery(cq).setFirstResult(first).setMaxResults(max).getResultList();
		} else {
			TypedQuery typedQuery = em.createQuery(cq);
			String hqlQueryString=typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
			ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
			SessionImplementor hibernateSession = em.unwrap(SessionImplementor.class);
			QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("", hqlQueryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
			queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
			String sqlQueryString = queryTranslator.getSQLString();

			List<MapCaseDto> resultSet = em.createQuery(cq).getResultList();
			return resultSet;
		}
	}

	private void selectMapDtoFields(CriteriaBuilder cb, CriteriaQuery<MapCaseDto> cq, Root<Case> root, CaseQueryContext caseQueryContext) {
		Join<Case, Person> person = root.join(Case.PERSON, JoinType.LEFT);
		Join<Person, Location> personAddress = person.join(Person.ADDRESS, JoinType.LEFT);
		Join<Case, Facility> facility = root.join(Case.HEALTH_FACILITY, JoinType.LEFT);
		Join<Case, District> district = root.join(Case.RESPONSIBLE_DISTRICT, JoinType.LEFT);

//		JurisdictionHelper.booleanSelector(cb, caseService.inJurisdictionOrOwned(caseQueryContext));
		cq.multiselect(
				root.get(Case.UUID),
				root.get(Case.REPORT_DATE),
				root.get(Case.CASE_CLASSIFICATION),
				root.get(Case.DISEASE),

				person.get(Person.UUID),
				person.get(Person.FIRST_NAME),
				person.get(Person.LAST_NAME),
				person.get(Person.OTHER_NAME),

				facility.get(Facility.UUID),
				facility.get(Facility.LATITUDE),
				facility.get(Facility.LONGITUDE),


				root.get(Case.REPORT_LAT),
				root.get(Case.REPORT_LON),

				personAddress.get(Location.LATITUDE),
				personAddress.get(Location.LONGITUDE),
//			root.get(User.UUID),
//			root.get(Region.UUID),
//			root.get(District.UUID),
//			root.get(Community.UUID),
//			root.get(Region.UUID),
//			root.get(District.UUID),
//			root.get(Community.UUID),
//			root.get(PointOfEntry.UUID)
//			caseService.inJurisdictionOrOwned(caseQueryContext)
				JurisdictionHelper.booleanSelector(cb, caseService.inJurisdictionOrOwned(caseQueryContext)),
				district.get(District.UUID),
				district.get(District.DISTRICT_LATITUDE),
				district.get(District.DISTRICT_LONGITUDE)
		);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RightsAllowed(UserRight._CASE_EXPORT)
	public List<CaseIndexExportDto[]> getExportListDuplicates(
			CaseCriteria caseCriteria,
			Collection<String> selectedRows,
			CaseExportType exportType,
			int first,
			int max,
			ExportConfigurationDto exportConfiguration,
			Language userLanguage, boolean ignoreRegion) {

		return service.getCasesForDuplicateMergingExport(caseCriteria, ignoreRegion, configFacade.getNameSimilarityThreshold(), first, max);

	}

	@Override
	public LinkedHashMap<CaseDataDto, CaseDataDto> getCaseDataByParentAndChildUuid(String parentUuid, String childUuid) {
		Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");
		LinkedHashMap<CaseDataDto, CaseDataDto> resultMap = new LinkedHashMap<>();

		try {
			resultMap = service.getByParentAndChildUuid(parentUuid, childUuid)
					.entrySet()
					.stream()
					.collect(Collectors.toMap(
							entry -> convertToDto(entry.getKey(), pseudonymizer),
							entry -> convertToDto(entry.getValue(), pseudonymizer),
							(dto1, dto2) -> dto1,
							LinkedHashMap::new
					));
		} catch (Exception e) {
			// Handle the exception as needed
		}

		return resultMap;
	}


	@Override
	public List<CaseDataDto> getAllCaseDataByDisease(Disease disease) {
		return caseService.getAllByDisease(disease);
	}

	@Override
	public boolean hasPositiveLabResult(String caseUuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Disease, Long> getCaseCountByDisease(CaseCriteria caseCriteria, boolean excludeSharedCases, boolean excludeCasesFromContacts) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);

		Predicate filter = caseService.createUserFilter(
				cb,
				cq,
				caze,
				new CaseUserFilterCriteria().excludeSharedCases(excludeSharedCases).excludeCasesFromContacts(excludeCasesFromContacts));

		//final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		filter = AbstractAdoService.and(cb, filter, caseService.createCriteriaFilter(caseCriteria, cb, cq, caze));

		//filter = AbstractAdoService.and(cb, filter, createCaseCriteriaFilter(caseCriteria, caseQueryContext));
		//filter = CriteriaBuilderHelper.and(cb, filter, caseService.createCaseCriteriaFilterDiseaseDetails(caseCriteria, cq));

		if (filter != null) {
			cq.where(filter);
		}

		cq.groupBy(caze.get(Case.DISEASE));
		cq.multiselect(caze.get(Case.DISEASE), cb.count(caze));
		List<Object[]> results = em.createQuery(cq).getResultList();


		Map<Disease, Long> resultMap = results.stream().collect(Collectors.toMap(e -> (Disease) e[0], e -> (Long) e[1]));

		return resultMap;
	}

	@Override
	public List<DashboardCaseDto> getCasesForDashboard(CaseCriteria caseCriteria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DashboardCaseDto> cq = cb.createQuery(DashboardCaseDto.class);
		Root<Case> caze = cq.from(Case.class);
		Join<Case, Symptoms> symptoms = caze.join(Case.SYMPTOMS, JoinType.LEFT);
		Join<Case, Person> person = caze.join(Case.PERSON, JoinType.LEFT);

		Predicate filter =
				caseService.createUserFilter(cb, cq, caze, new CaseUserFilterCriteria().excludeSharedCases(true).excludeCasesFromContacts(true));
		Predicate criteriaFilter = caseService.createCriteriaFilter(caseCriteria, cb, cq, caze);
		filter = AbstractAdoService.and(cb, filter, criteriaFilter);

		if (filter != null) {
			cq.where(filter);
		}

		List<DashboardCaseDto> result;
		if (filter != null) {
			cq.where(filter);
			cq.multiselect(
					caze.get(Case.ID),
					caze.get(Case.UUID),
					caze.get(Case.REPORT_DATE),
					symptoms.get(Symptoms.ONSET_DATE),
					caze.get(Case.CASE_CLASSIFICATION),
					caze.get(Case.DISEASE),
					caze.get(Case.INVESTIGATION_STATUS),
					person.get(Person.PRESENT_CONDITION),
					person.get(Person.CAUSE_OF_DEATH_DISEASE));

			result = em.createQuery(cq).getResultList();
		} else {
			result = Collections.emptyList();
		}

		return result;
	}

	@Override
	public CaseDataDto saveCase(@Valid CaseDataDto dto) throws ValidationRuntimeException {
		return saveCase(dto, true, true);
	}

	public CaseDataDto saveCase(@Valid CaseDataDto dto, boolean handleChanges, boolean checkChangeDate) {
		return saveCase(dto, handleChanges, checkChangeDate, true);
	}

	public CaseDataDto saveCase(@Valid CaseDataDto dto, boolean handleChanges, boolean checkChangeDate, boolean internal)
			throws ValidationRuntimeException {

		Case existingCase = caseService.getByUuid(dto.getUuid());

		if (internal && existingCase != null && !caseService.isCaseEditAllowed(existingCase)) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.errorCaseNotEditable));
		}

		CaseDataDto existingCaseDto = handleChanges ? toDto(existingCase) : null;

		return caseSave(dto, handleChanges, existingCase, existingCaseDto, checkChangeDate, internal);
	}

	@Override
	public String getLastReportedDistrictName(CaseCriteria caseCriteria, boolean excludeSharedCases, boolean excludeCasesFromContacts) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caze = cq.from(Case.class);
		Join<Case, District> district = caze.join(Case.DISTRICT, JoinType.LEFT);

		Predicate filter = caseService.createUserFilter(
				cb,
				cq,
				caze,
				new CaseUserFilterCriteria().excludeSharedCases(excludeSharedCases).excludeCasesFromContacts(excludeCasesFromContacts));

		filter = AbstractAdoService.and(cb, filter, caseService.createCriteriaFilter(caseCriteria, cb, cq, caze));

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(district.get(District.NAME));
		cq.orderBy(cb.desc(caze.get(Case.REPORT_DATE)));

		TypedQuery<String> query = em.createQuery(cq).setMaxResults(1);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return "";
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RightsAllowed(UserRight._CASE_EXPORT)
	public List<CaseExportDetailedSampleDto> getExportListDetailed(
			CaseCriteria caseCriteria,
			Collection<String> selectedRows,
			CaseExportType exportType,
			int first,
			int max,
			ExportConfigurationDto exportConfiguration,
			Language userLanguage) {

		List<CaseSampleExportDto> allSamples = null;

		Boolean previousCaseManagementDataCriteria = caseCriteria.getMustHaveCaseManagementData();
		if (CaseExportType.CASE_MANAGEMENT == exportType) {
			caseCriteria.setMustHaveCaseManagementData(Boolean.TRUE);
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseExportDetailedSampleDto> cq = cb.createQuery(CaseExportDetailedSampleDto.class);
		Root<Case> caseRoot = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caseRoot);
		final CaseJoins joins = caseQueryContext.getJoins();

		// Events count subquery
		Subquery<Long> eventCountSq = cq.subquery(Long.class);
		Root<EventParticipant> eventCountRoot = eventCountSq.from(EventParticipant.class);
		Join<EventParticipant, Event> event = eventCountRoot.join(EventParticipant.EVENT, JoinType.INNER);
		Join<EventParticipant, Case> resultingCase = eventCountRoot.join(EventParticipant.RESULTING_CASE, JoinType.INNER);
		eventCountSq.where(
				cb.and(
						cb.equal(resultingCase.get(Case.ID), caseRoot.get(Case.ID)),
						cb.isFalse(event.get(Event.DELETED)),
						cb.isFalse(eventCountRoot.get(EventParticipant.DELETED))));
		eventCountSq.select(cb.countDistinct(event.get(Event.ID)));

		Subquery<Long> prescriptionCountSq = cq.subquery(Long.class);
		Root<Prescription> prescriptionCountRoot = prescriptionCountSq.from(Prescription.class);
		Join<Prescription, Therapy> prescriptionTherapyJoin = prescriptionCountRoot.join(Prescription.THERAPY, JoinType.LEFT);
		prescriptionCountSq.where(cb.and(cb.equal(prescriptionTherapyJoin.get(Therapy.ID), caseRoot.get(Case.THERAPY).get(Therapy.ID))));
		prescriptionCountSq.select(cb.countDistinct(prescriptionCountRoot.get(Prescription.ID)));

		Subquery<Long> treatmentCountSq = cq.subquery(Long.class);
		Root<Treatment> treatmentCountRoot = treatmentCountSq.from(Treatment.class);
		Join<Treatment, Therapy> treatmentTherapyJoin = treatmentCountRoot.join(Treatment.THERAPY, JoinType.LEFT);
		treatmentCountSq.where(cb.and(cb.equal(treatmentTherapyJoin.get(Therapy.ID), caseRoot.get(Case.THERAPY).get(Therapy.ID))));
		treatmentCountSq.select(cb.countDistinct(treatmentCountRoot.get(Treatment.ID)));

		boolean exportGpsCoordinates = ExportHelper.shouldExportFields(exportConfiguration, PersonDto.ADDRESS, CaseExportDetailedSampleDto.ADDRESS_GPS_COORDINATES);
		boolean exportPrescriptionNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.NUMBER_OF_PRESCRIPTIONS);
		boolean exportTreatmentNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.NUMBER_OF_TREATMENTS);
		boolean exportClinicalVisitNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.NUMBER_OF_CLINICAL_VISITS);
		boolean exportOutbreakInfo = ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.ASSOCIATED_WITH_OUTBREAK);

		//@formatter:off
		cq.multiselect(caseRoot.get(Case.ID), joins.getPerson().get(Person.ID),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LATITUDE) : cb.nullLiteral(Double.class),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LONGITUDE) : cb.nullLiteral(Double.class),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LATLONACCURACY) : cb.nullLiteral(Float.class),
				joins.getEpiData().get(EpiData.ID),
				joins.getRoot().get(Case.SYMPTOMS).get(Symptoms.ID),
				joins.getHospitalization().get(Hospitalization.ID),
				joins.getRoot().get(Case.HEALTH_CONDITIONS).get(HealthConditions.ID),
				caseRoot.get(Case.UUID),
				caseRoot.get(Case.EPID_NUMBER), caseRoot.get(Case.DISEASE), caseRoot.get(Case.DISEASE_VARIANT), caseRoot.get(Case.DISEASE_DETAILS),
				caseRoot.get(Case.DISEASE_VARIANT_DETAILS), joins.getPerson().get(Person.UUID), joins.getPerson().get(Person.FIRST_NAME), joins.getPerson().get(Person.LAST_NAME),
				joins.getPerson().get(Person.SALUTATION), joins.getPerson().get(Person.OTHER_SALUTATION), joins.getPerson().get(Person.SEX),
				caseRoot.get(Case.PREGNANT), joins.getPerson().get(Person.APPROXIMATE_AGE),
				joins.getPerson().get(Person.APPROXIMATE_AGE_TYPE), joins.getPerson().get(Person.BIRTHDATE_DD),
				joins.getPerson().get(Person.BIRTHDATE_MM), joins.getPerson().get(Person.BIRTHDATE_YYYY),
				caseRoot.get(Case.REPORT_DATE), joins.getRegion().get(Region.NAME),
				joins.getDistrict().get(District.NAME), joins.getCommunity().get(Community.NAME),
				caseRoot.get(Case.FACILITY_TYPE),
				joins.getFacility().get(Facility.NAME), joins.getFacility().get(Facility.UUID), caseRoot.get(Case.HEALTH_FACILITY_DETAILS),
				joins.getPointOfEntry().get(PointOfEntry.NAME), joins.getPointOfEntry().get(PointOfEntry.UUID), caseRoot.get(Case.POINT_OF_ENTRY_DETAILS),
				caseRoot.get(Case.CASE_CLASSIFICATION),
				caseRoot.get(Case.CLINICAL_CONFIRMATION), caseRoot.get(Case.EPIDEMIOLOGICAL_CONFIRMATION), caseRoot.get(Case.LABORATORY_DIAGNOSTIC_CONFIRMATION),
				caseRoot.get(Case.NOT_A_CASE_REASON_NEGATIVE_TEST),
				caseRoot.get(Case.NOT_A_CASE_REASON_PHYSICIAN_INFORMATION), caseRoot.get(Case.NOT_A_CASE_REASON_DIFFERENT_PATHOGEN),
				caseRoot.get(Case.NOT_A_CASE_REASON_OTHER), caseRoot.get(Case.NOT_A_CASE_REASON_DETAILS),
				caseRoot.get(Case.INVESTIGATION_STATUS), caseRoot.get(Case.INVESTIGATED_DATE),
				caseRoot.get(Case.OUTCOME), caseRoot.get(Case.OUTCOME_DATE),
				caseRoot.get(Case.SEQUELAE), caseRoot.get(Case.SEQUELAE_DETAILS),
				caseRoot.get(Case.BLOOD_ORGAN_OR_TISSUE_DONATED),
				caseRoot.get(Case.FOLLOW_UP_STATUS), caseRoot.get(Case.FOLLOW_UP_UNTIL),
				caseRoot.get(Case.NOSOCOMIAL_OUTBREAK), caseRoot.get(Case.INFECTION_SETTING),
				caseRoot.get(Case.PROHIBITION_TO_WORK), caseRoot.get(Case.PROHIBITION_TO_WORK_FROM), caseRoot.get(Case.PROHIBITION_TO_WORK_UNTIL),
				caseRoot.get(Case.RE_INFECTION), caseRoot.get(Case.PREVIOUS_INFECTION_DATE), caseRoot.get(Case.REINFECTION_STATUS), caseRoot.get(Case.REINFECTION_DETAILS),
				// quarantine
				caseRoot.get(Case.QUARANTINE), caseRoot.get(Case.QUARANTINE_TYPE_DETAILS), caseRoot.get(Case.QUARANTINE_FROM), caseRoot.get(Case.QUARANTINE_TO),
				caseRoot.get(Case.QUARANTINE_HELP_NEEDED),
				caseRoot.get(Case.QUARANTINE_ORDERED_VERBALLY),
				caseRoot.get(Case.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT),
				caseRoot.get(Case.QUARANTINE_ORDERED_VERBALLY_DATE),
				caseRoot.get(Case.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT_DATE),
				caseRoot.get(Case.QUARANTINE_EXTENDED),
				caseRoot.get(Case.QUARANTINE_REDUCED),
				caseRoot.get(Case.QUARANTINE_OFFICIAL_ORDER_SENT),
				caseRoot.get(Case.QUARANTINE_OFFICIAL_ORDER_SENT_DATE),

				joins.getHospitalization().get(Hospitalization.ADMITTED_TO_HEALTH_FACILITY), joins.getHospitalization().get(Hospitalization.ADMISSION_DATE),
				joins.getHospitalization().get(Hospitalization.DISCHARGE_DATE), joins.getHospitalization().get(Hospitalization.LEFT_AGAINST_ADVICE),
				joins.getPerson().get(Person.PRESENT_CONDITION), joins.getPerson().get(Person.DEATH_DATE), joins.getPerson().get(Person.BURIAL_DATE),
				joins.getPerson().get(Person.BURIAL_CONDUCTOR), joins.getPerson().get(Person.BURIAL_PLACE_DESCRIPTION),
				// address
				joins.getPersonAddressRegion().get(Region.NAME), joins.getPersonAddressDistrict().get(District.NAME), joins.getPersonAddressCommunity().get(Community.NAME),
				joins.getPersonAddress().get(Location.CITY), joins.getPersonAddress().get(Location.STREET), joins.getPersonAddress().get(Location.HOUSE_NUMBER),
				joins.getPersonAddress().get(Location.ADDITIONAL_INFORMATION), joins.getPersonAddress().get(Location.POSTAL_CODE),
				joins.getPersonAddressFacility().get(Facility.NAME), joins.getPersonAddressFacility().get(Facility.UUID), joins.getPersonAddress().get(Location.FACILITY_DETAILS),
				// phone
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_PHONE_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_PHONE_OWNER_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_EMAIL_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_OTHER_CONTACT_DETAILS_SUBQUERY),
				joins.getPerson().get(Person.EDUCATION_TYPE),
				joins.getPerson().get(Person.EDUCATION_DETAILS), joins.getPerson().get(Person.OCCUPATION_TYPE),
				joins.getPerson().get(Person.OCCUPATION_DETAILS), joins.getPerson().get(Person.ARMED_FORCES_RELATION_TYPE), joins.getEpiData().get(EpiData.CONTACT_WITH_SOURCE_CASE_KNOWN),
				caseRoot.get(Case.VACCINATION_STATUS), caseRoot.get(Case.POSTPARTUM), caseRoot.get(Case.TRIMESTER),
				eventCountSq,
				exportPrescriptionNumber ? prescriptionCountSq : cb.nullLiteral(Long.class),
				exportTreatmentNumber ? treatmentCountSq : cb.nullLiteral(Long.class),
				exportClinicalVisitNumber ? clinicalVisitSq(cb, cq, caseRoot) : cb.nullLiteral(Long.class),
				caseRoot.get(Case.EXTERNAL_ID),
				caseRoot.get(Case.EXTERNAL_TOKEN),
				caseRoot.get(Case.INTERNAL_TOKEN),
				joins.getPerson().get(Person.BIRTH_NAME),
				joins.getPersonBirthCountry().get(Country.ISO_CODE),
				joins.getPersonBirthCountry().get(Country.DEFAULT_NAME),
				joins.getPersonCitizenship().get(Country.ISO_CODE),
				joins.getPersonCitizenship().get(Country.DEFAULT_NAME),
				caseRoot.get(Case.CASE_IDENTIFICATION_SOURCE),
				caseRoot.get(Case.SCREENING_TYPE),
				// responsible jurisdiction
				joins.getResponsibleRegion().get(Region.NAME),
				joins.getResponsibleDistrict().get(District.NAME),
				joins.getResponsibleCommunity().get(Community.NAME),
				caseRoot.get(Case.CLINICIAN_NAME),
				caseRoot.get(Case.CLINICIAN_PHONE),
				caseRoot.get(Case.CLINICIAN_EMAIL),
				caseRoot.get(Case.REPORTING_USER).get(User.ID),
				caseRoot.get(Case.FOLLOW_UP_STATUS_CHANGE_USER).get(User.ID),
				caseRoot.get(Case.PREVIOUS_QUARANTINE_TO),
				caseRoot.get(Case.QUARANTINE_CHANGE_COMMENT),
				exportOutbreakInfo ? cb.selectCase().when(cb.exists(outbreakSq(caseQueryContext)), cb.literal(I18nProperties.getString(Strings.yes)))
						.otherwise(cb.literal(I18nProperties.getString(Strings.no))) : cb.nullLiteral(String.class),
				JurisdictionHelper.booleanSelector(cb, service.inJurisdictionOrOwned(caseQueryContext)));
		//@formatter:on

		cq.distinct(true);

		Predicate filter = service.createUserFilter(caseQueryContext);

		if (caseCriteria != null) {
			Predicate criteriaFilter = service.createCriteriaFilter(caseCriteria, caseQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}
		filter = CriteriaBuilderHelper.andInValues(selectedRows, filter, cb, caseRoot.get(Case.UUID));

		if (filter != null) {
			cq.where(filter);
		}

		/*
		 * Sort by report date DESC, but also by id for stable Sorting in case of equal report dates.
		 * Since this method supports paging, values might jump between pages when sorting is unstable.
		 */
		cq.orderBy(cb.desc(caseRoot.get(Case.REPORT_DATE)), cb.desc(caseRoot.get(Case.ID)));

		List<CaseExportDetailedSampleDto> resultList = QueryHelper.getResultList(em, cq, first, max);

		List<Long> resultCaseIds = resultList.stream().map(CaseExportDetailedSampleDto::getId).collect(Collectors.toList());
		if (!resultList.isEmpty()) {
			List<Symptoms> symptomsList = null;
			CriteriaQuery<Symptoms> symptomsCq = cb.createQuery(Symptoms.class);
			Root<Symptoms> symptomsRoot = symptomsCq.from(Symptoms.class);
			Expression<String> symptomsIdsExpr = symptomsRoot.get(Symptoms.ID);
			symptomsCq.where(symptomsIdsExpr.in(resultList.stream().map(CaseExportDetailedSampleDto::getSymptomsId).collect(Collectors.toList())));
			symptomsList = em.createQuery(symptomsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
			Map<Long, Symptoms> symptoms = symptomsList.stream().collect(Collectors.toMap(Symptoms::getId, Function.identity()));

			Map<Long, HealthConditions> healthConditions = null;
			if (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT) {
				if (ExportHelper.shouldExportFields(exportConfiguration, CaseDataDto.HEALTH_CONDITIONS)) {
					List<HealthConditions> healthConditionsList = null;
					CriteriaQuery<HealthConditions> healthConditionsCq = cb.createQuery(HealthConditions.class);
					Root<HealthConditions> healthConditionsRoot = healthConditionsCq.from(HealthConditions.class);
					Expression<String> healthConditionsIdsExpr = healthConditionsRoot.get(HealthConditions.ID);
					healthConditionsCq.where(
							healthConditionsIdsExpr.in(resultList.stream().map(CaseExportDetailedSampleDto::getHealthConditionsId).collect(Collectors.toList())));
					healthConditionsList = em.createQuery(healthConditionsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
					healthConditions = healthConditionsList.stream().collect(Collectors.toMap(HealthConditions::getId, Function.identity()));
				}
			}

			Map<Long, PreviousHospitalization> firstPreviousHospitalizations = null;
			if (ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.INITIAL_DETECTION_PLACE)) {
				List<PreviousHospitalization> prevHospsList = null;
				CriteriaQuery<PreviousHospitalization> prevHospsCq = cb.createQuery(PreviousHospitalization.class);
				Root<PreviousHospitalization> prevHospsRoot = prevHospsCq.from(PreviousHospitalization.class);
				Join<PreviousHospitalization, Hospitalization> prevHospsHospitalizationJoin =
						prevHospsRoot.join(PreviousHospitalization.HOSPITALIZATION, JoinType.LEFT);
				Expression<String> hospitalizationIdsExpr = prevHospsHospitalizationJoin.get(Hospitalization.ID);
				prevHospsCq
						.where(hospitalizationIdsExpr.in(resultList.stream().map(CaseExportDetailedSampleDto::getHospitalizationId).collect(Collectors.toList())));
				prevHospsCq.orderBy(cb.asc(prevHospsRoot.get(PreviousHospitalization.ADMISSION_DATE)));
				prevHospsList = em.createQuery(prevHospsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
				firstPreviousHospitalizations =
						prevHospsList.stream().collect(Collectors.toMap(p -> p.getHospitalization().getId(), Function.identity(), (id1, id2) -> id1));
			}

			Map<Long, CaseClassification> sourceCaseClassifications = null;
			if (ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.MAX_SOURCE_CASE_CLASSIFICATION)) {
				sourceCaseClassifications = contactService.getSourceCaseClassifications(resultCaseIds)
						.stream()
						.collect(
								Collectors
										.toMap(e -> (Long) e[0], e -> (CaseClassification) e[1], (c1, c2) -> c1.getSeverity() >= c2.getSeverity() ? c1 : c2));
			}

			Map<Long, List<Exposure>> exposures = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& ExportHelper
					.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.TRAVELED, CaseExportDetailedSampleDto.TRAVEL_HISTORY, CaseExportDetailedSampleDto.BURIAL_ATTENDED)) {
				CriteriaQuery<Exposure> exposuresCq = cb.createQuery(Exposure.class);
				Root<Exposure> exposuresRoot = exposuresCq.from(Exposure.class);
				Join<Exposure, EpiData> exposuresEpiDataJoin = exposuresRoot.join(Exposure.EPI_DATA, JoinType.LEFT);
				Expression<String> epiDataIdsExpr = exposuresEpiDataJoin.get(EpiData.ID);
				Predicate exposuresPredicate = cb.and(
						epiDataIdsExpr.in(resultList.stream().map(CaseExportDetailedSampleDto::getEpiDataId).collect(Collectors.toList())),
						cb.or(
								cb.equal(exposuresRoot.get(Exposure.EXPOSURE_TYPE), ExposureType.TRAVEL),
								cb.equal(exposuresRoot.get(Exposure.EXPOSURE_TYPE), ExposureType.BURIAL)));
				exposuresCq.where(exposuresPredicate);
				exposuresCq.orderBy(cb.asc(exposuresEpiDataJoin.get(EpiData.ID)));
				List<Exposure> exposureList = em.createQuery(exposuresCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
				exposures = exposureList.stream().collect(Collectors.groupingBy(e -> e.getEpiData().getId()));
			}

			Map<Long, List<CaseSampleExportDto>> samples = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDetailedSampleDto.SAMPLE_INFORMATION)) {
				List<CaseSampleExportDto> samplesList = null;
				CriteriaQuery<CaseSampleExportDto> samplesCq = cb.createQuery(CaseSampleExportDto.class);
				Root<Sample> samplesRoot = samplesCq.from(Sample.class);
				Join<Sample, Case> samplesCaseJoin = samplesRoot.join(Sample.ASSOCIATED_CASE, JoinType.LEFT);
				Expression<String> caseIdsExpr = samplesCaseJoin.get(Case.ID);
				samplesCq.multiselect(
						samplesRoot.get(Sample.UUID),
						samplesRoot.get(Sample.LAB_SAMPLE_ID),
						samplesRoot.get(Sample.SAMPLE_DATE_TIME),
						samplesRoot.get(Sample.REPORT_DATE_TIME),
						samplesRoot.get(Sample.SAMPLE_MATERIAL),
						samplesRoot.get(Sample.SAMPLE_MATERIAL_TEXT),
						samplesRoot.get(Sample.SAMPLE_PURPOSE),
						samplesRoot.get(Sample.SAMPLE_SOURCE),
						samplesRoot.get(Sample.SAMPLING_REASON),
						samplesRoot.get(Sample.SAMPLING_REASON_DETAILS),
						samplesRoot.get(Sample.LAB).get(Facility.NAME),
						samplesRoot.get(Sample.LAB_DETAILS),
						samplesRoot.get(Sample.PATHOGEN_TEST_RESULT),
						samplesRoot.get(Sample.PATHOGEN_TESTING_REQUESTED),
						samplesRoot.get(Sample.REQUESTED_PATHOGEN_TESTS_STRING),
						samplesRoot.get(Sample.REQUESTED_OTHER_PATHOGEN_TESTS),
						samplesRoot.get(Sample.ADDITIONAL_TESTING_REQUESTED),
						samplesRoot.get(Sample.REQUESTED_ADDITIONAL_TESTS_STRING),
						samplesRoot.get(Sample.REQUESTED_OTHER_ADDITIONAL_TESTS),
						samplesRoot.get(Sample.SHIPPED),
						samplesRoot.get(Sample.SHIPMENT_DATE),
						samplesRoot.get(Sample.SHIPMENT_DETAILS),
						samplesRoot.get(Sample.RECEIVED),
						samplesRoot.get(Sample.RECEIVED_DATE),
						samplesRoot.get(Sample.SPECIMEN_CONDITION),
						samplesRoot.get(Sample.NO_TEST_POSSIBLE_REASON),
						samplesRoot.get(Sample.COMMENT),
						samplesRoot.get(Sample.LAB).get(Facility.UUID),
						caseIdsExpr);

				Predicate eliminateDeletedSamplesFilter = cb.equal(samplesRoot.get(Sample.DELETED), false);
				samplesCq.where(caseIdsExpr.in(resultCaseIds), eliminateDeletedSamplesFilter);
				samplesList = em.createQuery(samplesCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
				samples = samplesList.stream().collect(Collectors.groupingBy(s -> s.getCaseId()));
				if (samplesList != null) {
					allSamples = samplesList.stream()
							.sorted(Comparator.comparing(s -> s.getCaseId()))
							.collect(Collectors.toList());
				}
			}

			List<VisitSummaryExportDetails> visitSummaries = null;
			if (featureConfigurationFacade.isFeatureEnabled(FeatureType.CASE_FOLLOWUP)
					&& ExportHelper.shouldExportFields(
					exportConfiguration,
					CaseExportDetailedSampleDto.NUMBER_OF_VISITS,
					CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_DATE,
					CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_SYMPTOMATIC,
					CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_SYMPTOMS)) {
				CriteriaQuery<VisitSummaryExportDetails> visitsCq = cb.createQuery(VisitSummaryExportDetails.class);
				Root<Case> visitsCqRoot = visitsCq.from(Case.class);
				Join<Case, Visit> visitsJoin = visitsCqRoot.join(Case.VISITS, JoinType.LEFT);
				Join<Visit, Symptoms> visitSymptomsJoin = visitsJoin.join(Visit.SYMPTOMS, JoinType.LEFT);

				visitsCq.where(
						CriteriaBuilderHelper
								.and(cb, visitsCqRoot.get(AbstractDomainObject.ID).in(resultCaseIds), cb.isNotEmpty(visitsCqRoot.get(Case.VISITS))));
				visitsCq.multiselect(
						visitsCqRoot.get(AbstractDomainObject.ID),
						visitsJoin.get(Visit.VISIT_DATE_TIME),
						visitsJoin.get(Visit.VISIT_STATUS),
						visitSymptomsJoin);

				visitSummaries = em.createQuery(visitsCq).getResultList();
			}

			Map<Long, List<Immunization>> immunizations = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& (exportConfiguration == null
					|| exportConfiguration.getProperties()
					.stream()
					.anyMatch(p -> StringUtils.equalsAny(p, ExportHelper.getVaccinationExportProperties())))) {
				List<Immunization> immunizationList;
				CriteriaQuery<Immunization> immunizationsCq = cb.createQuery(Immunization.class);
				Root<Immunization> immunizationsCqRoot = immunizationsCq.from(Immunization.class);
				Join<Immunization, Person> personJoin = immunizationsCqRoot.join(Immunization.PERSON, JoinType.LEFT);
				Expression<String> personIdsExpr = personJoin.get(Person.ID);
				immunizationsCq.where(
						CriteriaBuilderHelper.and(
								cb,
								cb.or(
										cb.equal(immunizationsCqRoot.get(Immunization.MEANS_OF_IMMUNIZATION), MeansOfImmunization.VACCINATION),
										cb.equal(immunizationsCqRoot.get(Immunization.MEANS_OF_IMMUNIZATION), MeansOfImmunization.VACCINATION_RECOVERY)),
								personIdsExpr.in(resultList.stream().map(CaseExportDetailedSampleDto::getPersonId).collect(Collectors.toList()))));
				immunizationsCq.select(immunizationsCqRoot);
				immunizationList = em.createQuery(immunizationsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
				immunizations = immunizationList.stream().collect(Collectors.groupingBy(i -> i.getPerson().getId()));
			}

			// Load latest events info
			// Adding a second query here is not perfect, but selecting the last event with a criteria query
			// doesn't seem to be possible and using a native query is not an option because of user filters
			List<EventSummaryDetails> eventSummaries = null;
			if (ExportHelper.shouldExportFields(
					exportConfiguration,
					CaseExportDetailedSampleDto.LATEST_EVENT_ID,
					CaseExportDetailedSampleDto.LATEST_EVENT_STATUS,
					CaseExportDetailedSampleDto.LATEST_EVENT_TITLE)) {

				eventSummaries = eventService.getEventSummaryDetailsByCases(resultCaseIds);
			}

			Map<Long, UserReference> caseUsers = getCaseUsersForDetailedExport(resultList, exportConfiguration);

			Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician(I18nProperties.getCaption(Captions.inaccessibleValue));

			for (CaseExportDetailedSampleDto exportDto : resultList) {
				final boolean inJurisdiction = exportDto.getInJurisdiction();

				if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDetailedSampleDto.COUNTRY)) {
					exportDto.setCountry(configFacade.getEpidPrefix());
				}
				if (ExportHelper.shouldExportFields(exportConfiguration, CaseDataDto.SYMPTOMS)) {
					Optional.ofNullable(symptoms.get(exportDto.getSymptomsId()))
							.ifPresent(symptom -> exportDto.setSymptoms(SymptomsFacadeEjb.toDto(symptom)));
				}
				if (healthConditions != null) {
					Optional.ofNullable(healthConditions.get(exportDto.getHealthConditionsId()))
							.ifPresent(healthCondition -> exportDto.setHealthConditions(HealthConditionsMapper.toDto(healthCondition)));
				}
				if (firstPreviousHospitalizations != null) {
					Optional.ofNullable(firstPreviousHospitalizations.get(exportDto.getHospitalizationId()))
							.ifPresent(firstPreviousHospitalization -> {
								if (firstPreviousHospitalization.getHealthFacility() != null) {
									exportDto.setInitialDetectionPlace(
											FacilityHelper.buildFacilityString(
													firstPreviousHospitalization.getHealthFacility().getUuid(),
													firstPreviousHospitalization.getHealthFacility().getName(),
													firstPreviousHospitalization.getHealthFacilityDetails()));
								} else {
									exportDto.setInitialDetectionPlace(I18nProperties.getCaption(Captions.unknown));
								}
							});
					if (StringUtils.isEmpty(exportDto.getInitialDetectionPlace())) {
						if (!StringUtils.isEmpty(exportDto.getHealthFacility())) {
							exportDto.setInitialDetectionPlace(exportDto.getHealthFacility());
						} else {
							exportDto.setInitialDetectionPlace(exportDto.getPointOfEntry());
						}
					}
				}
				if (sourceCaseClassifications != null) {
					Optional.ofNullable(sourceCaseClassifications.get(exportDto.getId()))
							.ifPresent(sourceCaseClassification -> exportDto.setMaxSourceCaseClassification(sourceCaseClassification));
				}
				if (exposures != null) {
					Optional.ofNullable(exposures.get(exportDto.getEpiDataId())).ifPresent(caseExposures -> {
						StringBuilder travelHistoryBuilder = new StringBuilder();
						if (caseExposures.stream().anyMatch(e -> ExposureType.BURIAL.equals(e.getExposureType()))) {
							exportDto.setBurialAttended(true);
						}
						caseExposures.stream().filter(e -> ExposureType.TRAVEL.equals(e.getExposureType())).forEach(exposure -> {
							Location location = exposure.getLocation();
							travelHistoryBuilder.append(
											EpiDataHelper.buildDetailedTravelString(
													LocationReferenceDto.buildCaption(
															location.getRegion() != null ? location.getRegion().getName() : null,
															location.getDistrict() != null ? location.getDistrict().getName() : null,
															location.getCommunity() != null ? location.getCommunity().getName() : null,
															location.getCity(),
															location.getStreet(),
															location.getHouseNumber(),
															location.getAdditionalInformation()),
													exposure.getDescription(),
													exposure.getStartDate(),
													exposure.getEndDate(),
													userLanguage))
									.append(", ");
						});
						if (travelHistoryBuilder.length() > 0) {
							exportDto.setTraveled(true);
							travelHistoryBuilder.delete(travelHistoryBuilder.lastIndexOf(", "), travelHistoryBuilder.length() - 1);
						}
						exportDto.setTravelHistory(travelHistoryBuilder.toString());
					});
				}

				if (immunizations != null) {
					Optional.ofNullable(immunizations.get(exportDto.getPersonId())).ifPresent(caseImmunizations -> {
						List<Immunization> filteredImmunizations =
								caseImmunizations.stream().filter(i -> i.getDisease() == exportDto.getDisease()).collect(Collectors.toList());
						if (!filteredImmunizations.isEmpty()) {
							filteredImmunizations.sort(Comparator.comparing(i -> ImmunizationEntityHelper.getDateForComparison(i, false)));
							Immunization mostRecentImmunization = filteredImmunizations.get(filteredImmunizations.size() - 1);
							Integer numberOfDoses = mostRecentImmunization.getNumberOfDoses();
							Date onsetDate = Optional.ofNullable(symptoms.get(exportDto.getSymptomsId())).map(Symptoms::getOnsetDate).orElse(null);

							List<Vaccination> relevantSortedVaccinations = vaccinationService.getRelevantSortedVaccinations(
									filteredImmunizations.stream().flatMap(i -> i.getVaccinations().stream()).collect(Collectors.toList()),
									onsetDate,
									exportDto.getReportDate());
							Vaccination firstVaccination = null;
							Vaccination lastVaccination = null;

							if (CollectionUtils.isNotEmpty(relevantSortedVaccinations)) {
								firstVaccination = relevantSortedVaccinations.get(0);
								lastVaccination = relevantSortedVaccinations.get(relevantSortedVaccinations.size() - 1);
								exportDto.setFirstVaccinationDate(firstVaccination.getVaccinationDate());
								exportDto.setLastVaccinationDate(lastVaccination.getVaccinationDate());
								exportDto.setVaccineName(lastVaccination.getVaccineName());
								exportDto.setOtherVaccineName(lastVaccination.getOtherVaccineName());
								exportDto.setVaccineManufacturer(lastVaccination.getVaccineManufacturer());
								exportDto.setOtherVaccineManufacturer(lastVaccination.getOtherVaccineManufacturer());
								exportDto.setVaccinationInfoSource(lastVaccination.getVaccinationInfoSource());
								exportDto.setVaccineAtcCode(lastVaccination.getVaccineAtcCode());
								exportDto.setVaccineBatchNumber(lastVaccination.getVaccineBatchNumber());
								exportDto.setVaccineUniiCode(lastVaccination.getVaccineUniiCode());
								exportDto.setVaccineInn(lastVaccination.getVaccineInn());
							}

							exportDto.setNumberOfDoses(
									numberOfDoses != null ? String.valueOf(numberOfDoses) : getNumberOfDosesFromVaccinations(lastVaccination));
						}
					});
				}
				if (visitSummaries != null) {
					List<VisitSummaryExportDetails> visits =
							visitSummaries.stream().filter(v -> v.getContactId() == exportDto.getId()).collect(Collectors.toList());

					VisitSummaryExportDetails lastCooperativeVisit = visits.stream()
							.filter(v -> v.getVisitStatus() == VisitStatus.COOPERATIVE)
							.max(Comparator.comparing(VisitSummaryExportDetails::getVisitDateTime))
							.orElse(null);

					exportDto.setNumberOfVisits(visits.size());
					if (lastCooperativeVisit != null) {
						exportDto.setLastCooperativeVisitDate(lastCooperativeVisit.getVisitDateTime());

						SymptomsDto visitSymptoms = SymptomsFacadeEjb.toDto(lastCooperativeVisit.getSymptoms());
						pseudonymizer.pseudonymizeDto(SymptomsDto.class, visitSymptoms, inJurisdiction, null);

						exportDto.setLastCooperativeVisitSymptoms(SymptomsHelper.buildSymptomsHumanString(visitSymptoms, true, userLanguage));
						exportDto.setLastCooperativeVisitSymptomatic(
								visitSymptoms.getSymptomatic() == null
										? YesNoUnknown.UNKNOWN
										: (visitSymptoms.getSymptomatic() ? YesNoUnknown.YES : YesNoUnknown.NO));
					}
				}

				if (eventSummaries != null && exportDto.getEventCount() != 0) {
					eventSummaries.stream()
							.filter(v -> v.getCaseId() == exportDto.getId())
							.max(Comparator.comparing(EventSummaryDetails::getEventDate))
							.ifPresent(eventSummary -> {
								exportDto.setLatestEventId(eventSummary.getEventUuid());
								exportDto.setLatestEventStatus(eventSummary.getEventStatus());
								exportDto.setLatestEventTitle(eventSummary.getEventTitle());
							});
				}

				if (!caseUsers.isEmpty()) {
					if (exportDto.getReportingUserId() != null) {
						UserReference user = caseUsers.get(exportDto.getReportingUserId());

						exportDto.setReportingUserName(user.getName());
						exportDto.setReportingUserRoles(
								user.getUserRoles().stream().map(userRole -> UserRoleFacadeEjb.toReferenceDto(userRole)).collect(Collectors.toSet()));
					}

					if (exportDto.getFollowUpStatusChangeUserId() != null) {
						UserReference user = caseUsers.get(exportDto.getFollowUpStatusChangeUserId());

						exportDto.setFollowUpStatusChangeUserName(user.getName());
						exportDto.setFollowUpStatusChangeUserRoles(
								user.getUserRoles().stream().map(userRole -> UserRoleFacadeEjb.toReferenceDto(userRole)).collect(Collectors.toSet()));
					}
				}
			}


		}

////        allSamples != null
//        if (allSamples != null) {
//            //get all ids of samples
//            List<Long> sampleIds = allSamples.stream().map(CaseSampleExportDto::getCaseId).collect(Collectors.toList());
//            //get all pathogen tests with the sample ids
//            List<PathogenTest> pathogenTests = pathogenTestService.getPathogenTestsBySampleIds(sampleIds);
//
//            //group pathogen tests by sample id
//            Map<Long, List<PathogenTest>> pathogenTestsBySampleId = pathogenTests.stream().collect(Collectors.groupingBy(PathogenTest::getSample));
//
//
//        }


		if (allSamples != null) {
			List<CaseExportDetailedSampleDto> newResult = new ArrayList<>();
			for (CaseExportDetailedSampleDto exportDto : resultList) {
				List<CaseSampleExportDto> caseSamples = allSamples.stream().filter(s -> s.getCaseId().equals(exportDto.getId())).collect(Collectors.toList());

				if (caseSamples.isEmpty()) {
					CaseExportDetailedSampleDto caseExportDetailedDto = new CaseExportDetailedSampleDto();
					caseExportDetailedDto.setId(exportDto.getId());
					caseExportDetailedDto.setPersonId(exportDto.getPersonId());
					caseExportDetailedDto.setAddressGpsCoordinates(exportDto.getAddressGpsCoordinates());
					caseExportDetailedDto.setEpiDataId(exportDto.getEpiDataId());
					caseExportDetailedDto.setSymptomsId(exportDto.getSymptomsId());
					caseExportDetailedDto.setHospitalizationId(exportDto.getHospitalizationId());
					caseExportDetailedDto.setHealthConditionsId(exportDto.getHealthConditionsId());
					caseExportDetailedDto.setUuid(exportDto.getUuid());
					caseExportDetailedDto.setEpidNumber(exportDto.getEpidNumber());
					caseExportDetailedDto.setArmedForcesRelationType(exportDto.getArmedForcesRelationType());
					caseExportDetailedDto.setDisease(exportDto.getDisease());
					caseExportDetailedDto.setDiseaseDetails(exportDto.getDiseaseDetails());
					caseExportDetailedDto.setDiseaseVariant(exportDto.getDiseaseVariant());
					caseExportDetailedDto.setDiseaseVariantDetails(exportDto.getDiseaseVariantDetails());
					caseExportDetailedDto.setPersonUuid(exportDto.getPersonUuid());
					caseExportDetailedDto.setFirstName(exportDto.getFirstName());
					caseExportDetailedDto.setLastName(exportDto.getLastName());
					caseExportDetailedDto.setSalutation(exportDto.getSalutation());
					caseExportDetailedDto.setOtherSalutation(exportDto.getOtherSalutation());
					caseExportDetailedDto.setSex(exportDto.getSex());
					caseExportDetailedDto.setPregnant(exportDto.getPregnant());
					caseExportDetailedDto.setApproximateAge(exportDto.getApproximateAge());
					caseExportDetailedDto.setAgeGroup(exportDto.getAgeGroup());
					caseExportDetailedDto.setBirthdate(exportDto.getBirthdate());
					caseExportDetailedDto.setReportDate(exportDto.getReportDate());
					caseExportDetailedDto.setRegion(exportDto.getRegion());
					caseExportDetailedDto.setDistrict(exportDto.getDistrict());
					caseExportDetailedDto.setCommunity(exportDto.getCommunity());
					caseExportDetailedDto.setCaseClassification(exportDto.getCaseClassification());
					caseExportDetailedDto.setClinicalConfirmation(exportDto.getClinicalConfirmation());
					caseExportDetailedDto.setEpidemiologicalConfirmation(exportDto.getEpidemiologicalConfirmation());
					caseExportDetailedDto.setLaboratoryDiagnosticConfirmation(exportDto.getLaboratoryDiagnosticConfirmation());
					caseExportDetailedDto.setNotACaseReasonNegativeTest(exportDto.getNotACaseReasonNegativeTest());
					caseExportDetailedDto.setNotACaseReasonPhysicianInformation(exportDto.getNotACaseReasonPhysicianInformation());
					caseExportDetailedDto.setNotACaseReasonDifferentPathogen(exportDto.getNotACaseReasonDifferentPathogen());
					caseExportDetailedDto.setNotACaseReasonOther(exportDto.getNotACaseReasonOther());
					caseExportDetailedDto.setNotACaseReasonDetails(exportDto.getNotACaseReasonDetails());
					caseExportDetailedDto.setInvestigationStatus(exportDto.getInvestigationStatus());
					caseExportDetailedDto.setInvestigatedDate(exportDto.getInvestigatedDate());
					caseExportDetailedDto.setOutcome(exportDto.getOutcome());
					caseExportDetailedDto.setOutcomeDate(exportDto.getOutcomeDate());
					caseExportDetailedDto.setSequelae(exportDto.getSequelae());
					caseExportDetailedDto.setSequelaeDetails(exportDto.getSequelaeDetails());
					caseExportDetailedDto.setBloodOrganOrTissueDonated(exportDto.getBloodOrganOrTissueDonated());
					caseExportDetailedDto.setNosocomialOutbreak(exportDto.getNosocomialOutbreak());
					caseExportDetailedDto.setInfectionSetting(exportDto.getInfectionSetting());
					caseExportDetailedDto.setProhibitionToWork(exportDto.getProhibitionToWork());
					caseExportDetailedDto.setProhibitionToWorkFrom(exportDto.getProhibitionToWorkFrom());
					caseExportDetailedDto.setProhibitionToWorkUntil(exportDto.getProhibitionToWorkUntil());
					caseExportDetailedDto.setReInfection(exportDto.getReInfection());
					caseExportDetailedDto.setPreviousInfectionDate(exportDto.getPreviousInfectionDate());
					caseExportDetailedDto.setReinfectionStatus(exportDto.getReinfectionStatus());
					caseExportDetailedDto.setReinfectionDetails(exportDto.getReinfectionDetails());
					caseExportDetailedDto.setQuarantine(exportDto.getQuarantine());
					caseExportDetailedDto.setQuarantineTypeDetails(exportDto.getQuarantineTypeDetails());
					caseExportDetailedDto.setQuarantineFrom(exportDto.getQuarantineFrom());
					caseExportDetailedDto.setQuarantineTo(exportDto.getQuarantineTo());
					caseExportDetailedDto.setQuarantineHelpNeeded(exportDto.getQuarantineHelpNeeded());
					caseExportDetailedDto.setQuarantineOrderedVerbally(exportDto.isQuarantineOrderedVerbally());
					caseExportDetailedDto.setQuarantineOrderedOfficialDocument(exportDto.isQuarantineOrderedOfficialDocument());
					caseExportDetailedDto.setQuarantineOrderedVerballyDate(exportDto.getQuarantineOrderedVerballyDate());
					caseExportDetailedDto.setQuarantineOrderedOfficialDocumentDate(exportDto.getQuarantineOrderedOfficialDocumentDate());
					caseExportDetailedDto.setQuarantineExtended(exportDto.isQuarantineExtended());
					caseExportDetailedDto.setQuarantineReduced(exportDto.isQuarantineReduced());
					caseExportDetailedDto.setQuarantineOfficialOrderSent(exportDto.isQuarantineOfficialOrderSent());
					caseExportDetailedDto.setQuarantineOfficialOrderSentDate(exportDto.getQuarantineOfficialOrderSentDate());
					caseExportDetailedDto.setFacilityType(exportDto.getFacilityType());
					caseExportDetailedDto.setHealthFacility(exportDto.getHealthFacility());
					caseExportDetailedDto.setHealthFacilityDetails(exportDto.getHealthFacilityDetails());
					caseExportDetailedDto.setPointOfEntry(exportDto.getPointOfEntry());
					caseExportDetailedDto.setPointOfEntryDetails(exportDto.getPointOfEntryDetails());
					caseExportDetailedDto.setAdmittedToHealthFacility(exportDto.getAdmittedToHealthFacility());
					caseExportDetailedDto.setAdmissionDate(exportDto.getAdmissionDate());
					caseExportDetailedDto.setDischargeDate(exportDto.getDischargeDate());
					caseExportDetailedDto.setLeftAgainstAdvice(exportDto.getLeftAgainstAdvice());
					caseExportDetailedDto.setPresentCondition(exportDto.getPresentCondition());
					caseExportDetailedDto.setDeathDate(exportDto.getDeathDate());
					caseExportDetailedDto.setBurialInfo(exportDto.getBurialInfo());
					caseExportDetailedDto.setAddressRegion(exportDto.getAddressRegion());
					caseExportDetailedDto.setAddressDistrict(exportDto.getAddressDistrict());
					caseExportDetailedDto.setAddressCommunity(exportDto.getAddressCommunity());
					caseExportDetailedDto.setCity(exportDto.getCity());
					caseExportDetailedDto.setStreet(exportDto.getStreet());
					caseExportDetailedDto.setHouseNumber(exportDto.getHouseNumber());
					caseExportDetailedDto.setAdditionalInformation(exportDto.getAdditionalInformation());
					caseExportDetailedDto.setPostalCode(exportDto.getPostalCode());
					caseExportDetailedDto.setFacility(exportDto.getFacility());
					caseExportDetailedDto.setFacilityDetails(exportDto.getFacilityDetails());
					caseExportDetailedDto.setPhone(exportDto.getPhone());
					caseExportDetailedDto.setPhoneOwner(exportDto.getPhoneOwner());
					caseExportDetailedDto.setEmailAddress(exportDto.getEmailAddress());
					caseExportDetailedDto.setOtherContactDetails(exportDto.getOtherContactDetails());
					caseExportDetailedDto.setEducationType(exportDto.getEducationType());
					caseExportDetailedDto.setEducationDetails(exportDto.getEducationDetails());
					caseExportDetailedDto.setOccupationType(exportDto.getOccupationType());
					caseExportDetailedDto.setOccupationDetails(exportDto.getOccupationDetails());
					caseExportDetailedDto.setContactWithSourceCaseKnown(exportDto.getContactWithSourceCaseKnown());
					caseExportDetailedDto.setVaccinationStatus(exportDto.getVaccinationStatus());
					caseExportDetailedDto.setPostpartum(exportDto.getPostpartum());
					caseExportDetailedDto.setTrimester(exportDto.getTrimester());
					caseExportDetailedDto.setFollowUpStatus(exportDto.getFollowUpStatus());
					caseExportDetailedDto.setFollowUpUntil(exportDto.getFollowUpUntil());
					caseExportDetailedDto.setEventCount(exportDto.getEventCount());
					caseExportDetailedDto.setNumberOfPrescriptions(exportDto.getNumberOfPrescriptions());
					caseExportDetailedDto.setNumberOfTreatments(exportDto.getNumberOfTreatments());
					caseExportDetailedDto.setNumberOfClinicalVisits(exportDto.getNumberOfClinicalVisits());
					caseExportDetailedDto.setExternalID(exportDto.getExternalID());
					caseExportDetailedDto.setExternalToken(exportDto.getExternalToken());
					caseExportDetailedDto.setInternalToken(exportDto.getInternalToken());
					caseExportDetailedDto.setBirthName(exportDto.getBirthName());
					caseExportDetailedDto.setBirthCountry(exportDto.getBirthCountry());
					caseExportDetailedDto.setCitizenship(exportDto.getCitizenship());
					caseExportDetailedDto.setCaseIdentificationSource(exportDto.getCaseIdentificationSource());
					caseExportDetailedDto.setScreeningType(exportDto.getScreeningType());
					caseExportDetailedDto.setResponsibleRegion(exportDto.getResponsibleRegion());
					caseExportDetailedDto.setResponsibleDistrict(exportDto.getResponsibleDistrict());
					caseExportDetailedDto.setResponsibleCommunity(exportDto.getResponsibleCommunity());
					caseExportDetailedDto.setClinicianName(exportDto.getClinicianName());
					caseExportDetailedDto.setClinicianPhone(exportDto.getClinicianPhone());
					caseExportDetailedDto.setClinicianEmail(exportDto.getClinicianEmail());
					caseExportDetailedDto.setReportingUserId(exportDto.getReportingUserId());
					caseExportDetailedDto.setFollowUpStatusChangeUserId(exportDto.getFollowUpStatusChangeUserId());
					caseExportDetailedDto.setPreviousQuarantineTo(exportDto.getPreviousQuarantineTo());
					caseExportDetailedDto.setQuarantineChangeComment(exportDto.getQuarantineChangeComment());
					caseExportDetailedDto.setAssociatedWithOutbreak(exportDto.getAssociatedWithOutbreak());
					caseExportDetailedDto.setInJurisdiction(exportDto.getInJurisdiction());
					newResult.add(caseExportDetailedDto);
				} else {
				for(CaseSampleExportDto embeddedDetailedSampleExportDto : caseSamples) {
					Sample sampleFromExportDto = sampleService.getByUuid(embeddedDetailedSampleExportDto.getUuid());
					if (sampleFromExportDto == null || sampleFromExportDto.getPathogenTests() == null) {
						continue;
					}
					List<PathogenTest> pathogenTests = sampleFromExportDto.getPathogenTests();

					for (PathogenTest pathogenTest : pathogenTests) {
						if (pathogenTest == null
							|| pathogenTest.getTestedDisease() == null
							|| pathogenTest.getTestType() == null) {
							continue;
						}
						switch (pathogenTest.getTestedDisease().getName()) {
							case "AFP":
								mapAfpTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "CHOLERA":
								mapCholeraTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "CONGENITAL_RUBELLA":
								mapCongenitalRubellaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "CSM":
								mapCsmTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "DENGUE":
								mapDengueTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "EVD":
								mapEvdTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "GUINEA_WORM":
								mapGuineaWormTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "LASSA":
								mapLassaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "MEASLES":
								mapMeaslesTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "MONKEYPOX":
								mapMonkeyPoxTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "NEW_INFLUENZA":
								mapNewInfluenzaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "POLIO":
								mapPolioTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "UNSPECIFIED_VHF":
								mapUnspecifiedVhfTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "YELLOW_FEVER":
								mapYellowFeverTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "RABIES":
								mapRabiesTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ANTHRAX":
								mapAnthraxTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "CORONAVIRUS":
								mapCoronavirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "PNEUMONIA":
								mapPneumoniaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "MALARIA":
								mapMalariaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "TYPHOID_FEVER":
								mapTyphoidFeverTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ACUTE_VIRAL_HEPATITIS":
								mapAcuteViralHepatitisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "NON_NEONATAL_TETANUS":
								mapNonNeonatalTetanusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "HIV":
								mapHivTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "SCHISTOSOMIASIS":
								mapSchistosomiasisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "SOIL_TRANSMITTED_HELMINTHS":
								mapSoilTransmittedHelminthsTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "TRYPANOSOMIASIS":
								mapTrypanosomiasisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "DIARRHEA_BLOOD":
								mapDiarrheaBloodTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "SNAKE_BITE":
								mapSnakeBiteTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "RUBELLA":
								mapRubellaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "TUBERCULOSIS":
								mapTuberculosisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "LEPROSY":
								mapLeprosyTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "LYMPHATIC_FILARIASIS":
								mapLymphaticFilariasisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "BURULI_ULCER":
								mapBuruliUlcerTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "PERTUSSIS":
								mapPertussisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "NEONATAL_TETANUS":
								mapNeonatalTetanusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ONCHOCERCIASIS":
								mapOnchocerciasisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "DIPHTERIA":
								mapDiphtheriaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "TRACHOMA":
								mapTrachomaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "YAWS_ENDEMIC_SYPHILIS":
								mapYawsEndemicSyphilisTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "MATERNAL_DEATHS":
								mapMaternalDeathsTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "PERINATAL_DEATHS":
								mapPerinatalDeathsTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "INFLUENZA_A":
								mapInfluenzaATestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "INFLUENZA_B":
								mapInfluenzaBTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "H_METAPNEUMOVIRUS":
								maphMetapneumovirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "RESPIRATORY_SYNCYTIAL_VIRUS":
								mapRespiratorySyncytialVirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "PARAINFLUENZA_1_4":
								mapParainfluenza1_4TestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ADENOVIRUS":
								mapAdenovirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "RHINOVIRUS":
								mapRhinovirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ENTEROVIRUS":
								mapEnterovirusTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "M_PNEUMONIAE":
								mapmPneumoniaeTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "C_PNEUMONIAE":
								mapcPneumoniaeTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "ARI":
								mapAriTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "CHIKUNGUNYA":
								mapChikungunyaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "POST_IMMUNIZATION_ADVERSE_EVENTS_MILD":
								mapPostImmunizationAdverseEventsMildTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "POST_IMMUNIZATION_ADVERSE_EVENTS_SEVERE":
								mapPostImmunizationAdverseEventsSevereTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "FHA":
								mapFhaTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "OTHER":
								mapOtherTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							case "UNDEFINED":
								mapUndefinedTestsToSampleAndCase(exportDto, pathogenTest);
								break;
							default:
								break;
						}
					}

//                   if (exportDto.getId() == embeddedDetailedSampleExportDto.getCaseId()) {

					CaseExportDetailedSampleDto caseExportDetailedDto = new CaseExportDetailedSampleDto();
					caseExportDetailedDto.setId(exportDto.getId());
					caseExportDetailedDto.setPersonId(exportDto.getPersonId());
					caseExportDetailedDto.setAddressGpsCoordinates(exportDto.getAddressGpsCoordinates());
					caseExportDetailedDto.setEpiDataId(exportDto.getEpiDataId());
					caseExportDetailedDto.setSymptomsId(exportDto.getSymptomsId());
					caseExportDetailedDto.setHospitalizationId(exportDto.getHospitalizationId());
					caseExportDetailedDto.setHealthConditionsId(exportDto.getHealthConditionsId());
					caseExportDetailedDto.setUuid(exportDto.getUuid());
					caseExportDetailedDto.setEpidNumber(exportDto.getEpidNumber());
					caseExportDetailedDto.setArmedForcesRelationType(exportDto.getArmedForcesRelationType());
					caseExportDetailedDto.setDisease(exportDto.getDisease());
					caseExportDetailedDto.setDiseaseDetails(exportDto.getDiseaseDetails());
					caseExportDetailedDto.setDiseaseVariant(exportDto.getDiseaseVariant());
					caseExportDetailedDto.setDiseaseVariantDetails(exportDto.getDiseaseVariantDetails());
					caseExportDetailedDto.setPersonUuid(exportDto.getPersonUuid());
					caseExportDetailedDto.setPersonUuid(exportDto.getPersonUuid());
					caseExportDetailedDto.setFirstName(exportDto.getFirstName());
					caseExportDetailedDto.setLastName(exportDto.getLastName());
					caseExportDetailedDto.setSalutation(exportDto.getSalutation());
					caseExportDetailedDto.setOtherSalutation(exportDto.getOtherSalutation());
					caseExportDetailedDto.setSex(exportDto.getSex());
					caseExportDetailedDto.setPregnant(exportDto.getPregnant());
					caseExportDetailedDto.setApproximateAge(exportDto.getApproximateAge());
					caseExportDetailedDto.setAgeGroup(exportDto.getAgeGroup());
					caseExportDetailedDto.setBirthdate(exportDto.getBirthdate());
					caseExportDetailedDto.setReportDate(exportDto.getReportDate());
					caseExportDetailedDto.setRegion(exportDto.getRegion());
					caseExportDetailedDto.setDistrict(exportDto.getDistrict());
					caseExportDetailedDto.setCommunity(exportDto.getCommunity());
					caseExportDetailedDto.setCaseClassification(exportDto.getCaseClassification());
					caseExportDetailedDto.setClinicalConfirmation(exportDto.getClinicalConfirmation());
					caseExportDetailedDto.setEpidemiologicalConfirmation(exportDto.getEpidemiologicalConfirmation());
					caseExportDetailedDto.setLaboratoryDiagnosticConfirmation(exportDto.getLaboratoryDiagnosticConfirmation());
					caseExportDetailedDto.setNotACaseReasonNegativeTest(exportDto.getNotACaseReasonNegativeTest());
					caseExportDetailedDto.setNotACaseReasonPhysicianInformation(exportDto.getNotACaseReasonPhysicianInformation());
					caseExportDetailedDto.setNotACaseReasonDifferentPathogen(exportDto.getNotACaseReasonDifferentPathogen());
					caseExportDetailedDto.setNotACaseReasonOther(exportDto.getNotACaseReasonOther());
					caseExportDetailedDto.setNotACaseReasonDetails(exportDto.getNotACaseReasonDetails());
					caseExportDetailedDto.setInvestigationStatus(exportDto.getInvestigationStatus());
					caseExportDetailedDto.setInvestigatedDate(exportDto.getInvestigatedDate());
					caseExportDetailedDto.setOutcome(exportDto.getOutcome());
					caseExportDetailedDto.setOutcomeDate(exportDto.getOutcomeDate());
					caseExportDetailedDto.setSequelae(exportDto.getSequelae());
					caseExportDetailedDto.setSequelaeDetails(exportDto.getSequelaeDetails());
					caseExportDetailedDto.setBloodOrganOrTissueDonated(exportDto.getBloodOrganOrTissueDonated());
					caseExportDetailedDto.setNosocomialOutbreak(exportDto.getNosocomialOutbreak());
					caseExportDetailedDto.setInfectionSetting(exportDto.getInfectionSetting());
					caseExportDetailedDto.setProhibitionToWork(exportDto.getProhibitionToWork());
					caseExportDetailedDto.setProhibitionToWorkFrom(exportDto.getProhibitionToWorkFrom());
					caseExportDetailedDto.setProhibitionToWorkUntil(exportDto.getProhibitionToWorkUntil());
					caseExportDetailedDto.setReInfection(exportDto.getReInfection());
					caseExportDetailedDto.setPreviousInfectionDate(exportDto.getPreviousInfectionDate());
					caseExportDetailedDto.setReinfectionStatus(exportDto.getReinfectionStatus());
					caseExportDetailedDto.setReinfectionDetails(exportDto.getReinfectionDetails());
					caseExportDetailedDto.setQuarantine(exportDto.getQuarantine());
					caseExportDetailedDto.setQuarantineTypeDetails(exportDto.getQuarantineTypeDetails());
					caseExportDetailedDto.setQuarantineFrom(exportDto.getQuarantineFrom());
					caseExportDetailedDto.setQuarantineTo(exportDto.getQuarantineTo());
					caseExportDetailedDto.setQuarantineHelpNeeded(exportDto.getQuarantineHelpNeeded());
					caseExportDetailedDto.setQuarantineOrderedVerbally(exportDto.isQuarantineOrderedVerbally());
					caseExportDetailedDto.setQuarantineOrderedOfficialDocument(exportDto.isQuarantineOrderedOfficialDocument());
					caseExportDetailedDto.setQuarantineOrderedVerballyDate(exportDto.getQuarantineOrderedVerballyDate());
					caseExportDetailedDto.setQuarantineOrderedOfficialDocumentDate(exportDto.getQuarantineOrderedOfficialDocumentDate());
					caseExportDetailedDto.setQuarantineExtended(exportDto.isQuarantineExtended());
					caseExportDetailedDto.setQuarantineReduced(exportDto.isQuarantineReduced());
					caseExportDetailedDto.setQuarantineOfficialOrderSent(exportDto.isQuarantineOfficialOrderSent());
					caseExportDetailedDto.setQuarantineOfficialOrderSentDate(exportDto.getQuarantineOfficialOrderSentDate());
					caseExportDetailedDto.setFacilityType(exportDto.getFacilityType());
					caseExportDetailedDto.setHealthFacility(exportDto.getHealthFacility());
					caseExportDetailedDto.setHealthFacilityDetails(exportDto.getHealthFacilityDetails());
					caseExportDetailedDto.setPointOfEntry(exportDto.getPointOfEntry());
					caseExportDetailedDto.setPointOfEntryDetails(exportDto.getPointOfEntryDetails());
					caseExportDetailedDto.setAdmittedToHealthFacility(exportDto.getAdmittedToHealthFacility());
					caseExportDetailedDto.setAdmissionDate(exportDto.getAdmissionDate());
					caseExportDetailedDto.setDischargeDate(exportDto.getDischargeDate());
					caseExportDetailedDto.setLeftAgainstAdvice(exportDto.getLeftAgainstAdvice());
					caseExportDetailedDto.setPresentCondition(exportDto.getPresentCondition());
					caseExportDetailedDto.setDeathDate(exportDto.getDeathDate());
					caseExportDetailedDto.setBurialInfo(exportDto.getBurialInfo());
					caseExportDetailedDto.setAddressRegion(exportDto.getAddressRegion());
					caseExportDetailedDto.setAddressDistrict(exportDto.getAddressDistrict());
					caseExportDetailedDto.setAddressCommunity(exportDto.getAddressCommunity());
					caseExportDetailedDto.setCity(exportDto.getCity());
					caseExportDetailedDto.setStreet(exportDto.getStreet());
					caseExportDetailedDto.setHouseNumber(exportDto.getHouseNumber());
					caseExportDetailedDto.setAdditionalInformation(exportDto.getAdditionalInformation());
					caseExportDetailedDto.setPostalCode(exportDto.getPostalCode());
					caseExportDetailedDto.setFacility(exportDto.getFacility());
					caseExportDetailedDto.setFacilityDetails(exportDto.getFacilityDetails());
					caseExportDetailedDto.setPhone(exportDto.getPhone());
					caseExportDetailedDto.setPhoneOwner(exportDto.getPhoneOwner());
					caseExportDetailedDto.setEmailAddress(exportDto.getEmailAddress());
					caseExportDetailedDto.setOtherContactDetails(exportDto.getOtherContactDetails());
					caseExportDetailedDto.setEducationType(exportDto.getEducationType());
					caseExportDetailedDto.setEducationDetails(exportDto.getEducationDetails());
					caseExportDetailedDto.setOccupationType(exportDto.getOccupationType());
					caseExportDetailedDto.setOccupationDetails(exportDto.getOccupationDetails());
					caseExportDetailedDto.setContactWithSourceCaseKnown(exportDto.getContactWithSourceCaseKnown());
					caseExportDetailedDto.setVaccinationStatus(exportDto.getVaccinationStatus());
					caseExportDetailedDto.setPostpartum(exportDto.getPostpartum());
					caseExportDetailedDto.setTrimester(exportDto.getTrimester());
					caseExportDetailedDto.setFollowUpStatus(exportDto.getFollowUpStatus());
					caseExportDetailedDto.setFollowUpUntil(exportDto.getFollowUpUntil());
					caseExportDetailedDto.setEventCount(exportDto.getEventCount());
					caseExportDetailedDto.setNumberOfPrescriptions(exportDto.getNumberOfPrescriptions());
					caseExportDetailedDto.setNumberOfTreatments(exportDto.getNumberOfTreatments());
					caseExportDetailedDto.setNumberOfClinicalVisits(exportDto.getNumberOfClinicalVisits());
					caseExportDetailedDto.setExternalID(exportDto.getExternalID());
					caseExportDetailedDto.setExternalToken(exportDto.getExternalToken());
					caseExportDetailedDto.setInternalToken(exportDto.getInternalToken());
					caseExportDetailedDto.setBirthName(exportDto.getBirthName());
					caseExportDetailedDto.setBirthCountry(exportDto.getBirthCountry());
					caseExportDetailedDto.setCitizenship(exportDto.getCitizenship());
					caseExportDetailedDto.setCaseIdentificationSource(exportDto.getCaseIdentificationSource());
					caseExportDetailedDto.setScreeningType(exportDto.getScreeningType());
					caseExportDetailedDto.setResponsibleRegion(exportDto.getResponsibleRegion());
					caseExportDetailedDto.setResponsibleDistrict(exportDto.getResponsibleDistrict());
					caseExportDetailedDto.setResponsibleCommunity(exportDto.getResponsibleCommunity());
					caseExportDetailedDto.setClinicianName(exportDto.getClinicianName());
					caseExportDetailedDto.setClinicianPhone(exportDto.getClinicianPhone());
					caseExportDetailedDto.setClinicianEmail(exportDto.getClinicianEmail());
					caseExportDetailedDto.setReportingUserId(exportDto.getReportingUserId());
					caseExportDetailedDto.setFollowUpStatusChangeUserId(exportDto.getFollowUpStatusChangeUserId());
					caseExportDetailedDto.setPreviousQuarantineTo(exportDto.getPreviousQuarantineTo());
					caseExportDetailedDto.setQuarantineChangeComment(exportDto.getQuarantineChangeComment());
					caseExportDetailedDto.setAssociatedWithOutbreak(exportDto.getAssociatedWithOutbreak());
					caseExportDetailedDto.setInJurisdiction(exportDto.getInJurisdiction());

					//adding sample data
					caseExportDetailedDto.setSampleUuid(embeddedDetailedSampleExportDto.getUuid());
					caseExportDetailedDto.setLabSampleId(embeddedDetailedSampleExportDto.getLabSampleID());
					caseExportDetailedDto.setSampleReportDate(embeddedDetailedSampleExportDto.getSampleReportDate());
					caseExportDetailedDto.setSampleDateTime(embeddedDetailedSampleExportDto.getSampleDateTime());
					caseExportDetailedDto.setSampleSource(embeddedDetailedSampleExportDto.getSampleSource());
					caseExportDetailedDto.setSampleMaterialString(embeddedDetailedSampleExportDto.getSampleMaterialString());
					caseExportDetailedDto.setSamplePurpose(embeddedDetailedSampleExportDto.getSamplePurpose());
					caseExportDetailedDto.setSampleSource(embeddedDetailedSampleExportDto.getSampleSource());
					caseExportDetailedDto.setSamplingReason(embeddedDetailedSampleExportDto.getSamplingReason());
					caseExportDetailedDto.setSamplingReasonDetails(embeddedDetailedSampleExportDto.getSamplingReasonDetails());
					caseExportDetailedDto.setLaboratory(embeddedDetailedSampleExportDto.getLab());
					caseExportDetailedDto.setPathogenTestResult(embeddedDetailedSampleExportDto.getPathogenTestResult());
					caseExportDetailedDto.setPathogenTestingRequested(embeddedDetailedSampleExportDto.getPathogenTestingRequested());
					caseExportDetailedDto.setRequestedPathogenTests(embeddedDetailedSampleExportDto.getRequestedPathogenTests());
					caseExportDetailedDto.setRequestedOtherPathogenTests(embeddedDetailedSampleExportDto.getRequestedOtherPathogenTests());
					caseExportDetailedDto.setRequestedOtherAdditionalTests(embeddedDetailedSampleExportDto.getRequestedOtherAdditionalTests());
					caseExportDetailedDto.setAdditionalTestingRequested(embeddedDetailedSampleExportDto.getAdditionalTestingRequested());
					caseExportDetailedDto.setRequestedAdditionalTests(embeddedDetailedSampleExportDto.getRequestedAdditionalTests());
					caseExportDetailedDto.setRequestedOtherAdditionalTests(embeddedDetailedSampleExportDto.getRequestedOtherAdditionalTests());
					caseExportDetailedDto.setShipped(embeddedDetailedSampleExportDto.isShipped());
					caseExportDetailedDto.setShipmentDate(embeddedDetailedSampleExportDto.getShipmentDate());
					caseExportDetailedDto.setShipmentDetails(embeddedDetailedSampleExportDto.getShipmentDetails());
					caseExportDetailedDto.setReceived(embeddedDetailedSampleExportDto.isReceived());
					caseExportDetailedDto.setReceivedDate(embeddedDetailedSampleExportDto.getReceivedDate());
					caseExportDetailedDto.setSpecimenCondition(embeddedDetailedSampleExportDto.getSpecimenCondition());
					caseExportDetailedDto.setNoTestPossibleReason(embeddedDetailedSampleExportDto.getNoTestPossibleReason());
					caseExportDetailedDto.setComment(embeddedDetailedSampleExportDto.getComment());

					caseExportDetailedDto.setAfpAntibodyDetection(exportDto.getAfpAntibodyDetection());
					caseExportDetailedDto.setAfpAntigenDetection(exportDto.getAfpAntigenDetection());
					caseExportDetailedDto.setAfpRapidTest(exportDto.getAfpRapidTest());
					caseExportDetailedDto.setAfpCulture(exportDto.getAfpCulture());
					caseExportDetailedDto.setAfpHistopathology(exportDto.getAfpHistopathology());
					caseExportDetailedDto.setAfpIsolation(exportDto.getAfpIsolation());
					caseExportDetailedDto.setAfpIgmSerumAntibody(exportDto.getAfpIgmSerumAntibody());
					caseExportDetailedDto.setAfpIggSerumAntibody(exportDto.getAfpIggSerumAntibody());
					caseExportDetailedDto.setAfpIgaSerumAntibody(exportDto.getAfpIgaSerumAntibody());
					caseExportDetailedDto.setAfpIncubationTime(exportDto.getAfpIncubationTime());
					caseExportDetailedDto.setAfpIndirectFluorescentAntibody(exportDto.getAfpIndirectFluorescentAntibody());
					caseExportDetailedDto.setAfpDirectFluorescentAntibody(exportDto.getAfpDirectFluorescentAntibody());
					caseExportDetailedDto.setAfpMicroscopy(exportDto.getAfpMicroscopy());
					caseExportDetailedDto.setAfpNeutralizingAntibodies(exportDto.getAfpNeutralizingAntibodies());
					caseExportDetailedDto.setAfpPcrRtPcr(exportDto.getAfpPcrRtPcr());
					caseExportDetailedDto.setAfpGramStain(exportDto.getAfpGramStain());
					caseExportDetailedDto.setAfpLatexAgglutination(exportDto.getAfpLatexAgglutination());
					caseExportDetailedDto.setAfpCqValueDetection(exportDto.getAfpCqValueDetection());
					caseExportDetailedDto.setAfpSequencing(exportDto.getAfpSequencing());
					caseExportDetailedDto.setAfpDnaMicroarray(exportDto.getAfpDnaMicroarray());
					caseExportDetailedDto.setAfpOther(exportDto.getAfpOther());
					caseExportDetailedDto.setAfpAntibodyDetectionDetails(exportDto.getAfpAntibodyDetectionDetails());
					caseExportDetailedDto.setAfpAntigenDetectionDetails(exportDto.getAfpAntigenDetectionDetails());
					caseExportDetailedDto.setAfpRapidTestDetails(exportDto.getAfpRapidTestDetails());
					caseExportDetailedDto.setAfpCultureDetails(exportDto.getAfpCultureDetails());
					caseExportDetailedDto.setAfpHistopathologyDetails(exportDto.getAfpHistopathologyDetails());
					caseExportDetailedDto.setAfpIsolationDetails(exportDto.getAfpIsolationDetails());
					caseExportDetailedDto.setAfpIgmSerumAntibodyDetails(exportDto.getAfpIgmSerumAntibodyDetails());
					caseExportDetailedDto.setAfpIggSerumAntibodyDetails(exportDto.getAfpIggSerumAntibodyDetails());
					caseExportDetailedDto.setAfpIgaSerumAntibodyDetails(exportDto.getAfpIgaSerumAntibodyDetails());
					caseExportDetailedDto.setAfpIncubationTimeDetails(exportDto.getAfpIncubationTimeDetails());
					caseExportDetailedDto.setAfpIndirectFluorescentAntibodyDetails(exportDto.getAfpIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAfpDirectFluorescentAntibodyDetails(exportDto.getAfpDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAfpMicroscopyDetails(exportDto.getAfpMicroscopyDetails());
					caseExportDetailedDto.setAfpNeutralizingAntibodiesDetails(exportDto.getAfpNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setAfpPcrRtPcrDetails(exportDto.getAfpPcrRtPcrDetails());
					caseExportDetailedDto.setAfpGramStainDetails(exportDto.getAfpGramStainDetails());
					caseExportDetailedDto.setAfpLatexAgglutinationDetails(exportDto.getAfpLatexAgglutinationDetails());
					caseExportDetailedDto.setAfpCqValueDetectionDetails(exportDto.getAfpCqValueDetectionDetails());
					caseExportDetailedDto.setAfpSequencingDetails(exportDto.getAfpSequencingDetails());
					caseExportDetailedDto.setAfpDnaMicroarrayDetails(exportDto.getAfpDnaMicroarrayDetails());
					caseExportDetailedDto.setAfpOtherDetails(exportDto.getAfpOtherDetails());
					caseExportDetailedDto.setCholeraAntibodyDetection(exportDto.getCholeraAntibodyDetection());
					caseExportDetailedDto.setCholeraAntigenDetection(exportDto.getCholeraAntigenDetection());
					caseExportDetailedDto.setCholeraRapidTest(exportDto.getCholeraRapidTest());
					caseExportDetailedDto.setCholeraCulture(exportDto.getCholeraCulture());
					caseExportDetailedDto.setCholeraHistopathology(exportDto.getCholeraHistopathology());
					caseExportDetailedDto.setCholeraIsolation(exportDto.getCholeraIsolation());
					caseExportDetailedDto.setCholeraIgmSerumAntibody(exportDto.getCholeraIgmSerumAntibody());
					caseExportDetailedDto.setCholeraIggSerumAntibody(exportDto.getCholeraIggSerumAntibody());
					caseExportDetailedDto.setCholeraIgaSerumAntibody(exportDto.getCholeraIgaSerumAntibody());
					caseExportDetailedDto.setCholeraIncubationTime(exportDto.getCholeraIncubationTime());
					caseExportDetailedDto.setCholeraIndirectFluorescentAntibody(exportDto.getCholeraIndirectFluorescentAntibody());
					caseExportDetailedDto.setCholeraDirectFluorescentAntibody(exportDto.getCholeraDirectFluorescentAntibody());
					caseExportDetailedDto.setCholeraMicroscopy(exportDto.getCholeraMicroscopy());
					caseExportDetailedDto.setCholeraNeutralizingAntibodies(exportDto.getCholeraNeutralizingAntibodies());
					caseExportDetailedDto.setCholeraPcrRtPcr(exportDto.getCholeraPcrRtPcr());
					caseExportDetailedDto.setCholeraGramStain(exportDto.getCholeraGramStain());
					caseExportDetailedDto.setCholeraLatexAgglutination(exportDto.getCholeraLatexAgglutination());
					caseExportDetailedDto.setCholeraCqValueDetection(exportDto.getCholeraCqValueDetection());
					caseExportDetailedDto.setCholeraSequencing(exportDto.getCholeraSequencing());
					caseExportDetailedDto.setCholeraDnaMicroarray(exportDto.getCholeraDnaMicroarray());
					caseExportDetailedDto.setCholeraOther(exportDto.getCholeraOther());
					caseExportDetailedDto.setCholeraAntibodyDetectionDetails(exportDto.getCholeraAntibodyDetectionDetails());
					caseExportDetailedDto.setCholeraAntigenDetectionDetails(exportDto.getCholeraAntigenDetectionDetails());
					caseExportDetailedDto.setCholeraRapidTestDetails(exportDto.getCholeraRapidTestDetails());
					caseExportDetailedDto.setCholeraCultureDetails(exportDto.getCholeraCultureDetails());
					caseExportDetailedDto.setCholeraHistopathologyDetails(exportDto.getCholeraHistopathologyDetails());
					caseExportDetailedDto.setCholeraIsolationDetails(exportDto.getCholeraIsolationDetails());
					caseExportDetailedDto.setCholeraIgmSerumAntibodyDetails(exportDto.getCholeraIgmSerumAntibodyDetails());
					caseExportDetailedDto.setCholeraIggSerumAntibodyDetails(exportDto.getCholeraIggSerumAntibodyDetails());
					caseExportDetailedDto.setCholeraIgaSerumAntibodyDetails(exportDto.getCholeraIgaSerumAntibodyDetails());
					caseExportDetailedDto.setCholeraIncubationTimeDetails(exportDto.getCholeraIncubationTimeDetails());
					caseExportDetailedDto.setCholeraIndirectFluorescentAntibodyDetails(exportDto.getCholeraIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCholeraDirectFluorescentAntibodyDetails(exportDto.getCholeraDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCholeraMicroscopyDetails(exportDto.getCholeraMicroscopyDetails());
					caseExportDetailedDto.setCholeraNeutralizingAntibodiesDetails(exportDto.getCholeraNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setCholeraPcrRtPcrDetails(exportDto.getCholeraPcrRtPcrDetails());
					caseExportDetailedDto.setCholeraGramStainDetails(exportDto.getCholeraGramStainDetails());
					caseExportDetailedDto.setCholeraLatexAgglutinationDetails(exportDto.getCholeraLatexAgglutinationDetails());
					caseExportDetailedDto.setCholeraCqValueDetectionDetails(exportDto.getCholeraCqValueDetectionDetails());
					caseExportDetailedDto.setCholeraSequencingDetails(exportDto.getCholeraSequencingDetails());
					caseExportDetailedDto.setCholeraDnaMicroarrayDetails(exportDto.getCholeraDnaMicroarrayDetails());
					caseExportDetailedDto.setCholeraOtherDetails(exportDto.getCholeraOtherDetails());
					caseExportDetailedDto.setCongenitalRubellaAntibodyDetection(exportDto.getCongenitalRubellaAntibodyDetection());
					caseExportDetailedDto.setCongenitalRubellaAntigenDetection(exportDto.getCongenitalRubellaAntigenDetection());
					caseExportDetailedDto.setCongenitalRubellaRapidTest(exportDto.getCongenitalRubellaRapidTest());
					caseExportDetailedDto.setCongenitalRubellaCulture(exportDto.getCongenitalRubellaCulture());
					caseExportDetailedDto.setCongenitalRubellaHistopathology(exportDto.getCongenitalRubellaHistopathology());
					caseExportDetailedDto.setCongenitalRubellaIsolation(exportDto.getCongenitalRubellaIsolation());
					caseExportDetailedDto.setCongenitalRubellaIgmSerumAntibody(exportDto.getCongenitalRubellaIgmSerumAntibody());
					caseExportDetailedDto.setCongenitalRubellaIggSerumAntibody(exportDto.getCongenitalRubellaIggSerumAntibody());
					caseExportDetailedDto.setCongenitalRubellaIgaSerumAntibody(exportDto.getCongenitalRubellaIgaSerumAntibody());
					caseExportDetailedDto.setCongenitalRubellaIncubationTime(exportDto.getCongenitalRubellaIncubationTime());
					caseExportDetailedDto.setCongenitalRubellaIndirectFluorescentAntibody(exportDto.getCongenitalRubellaIndirectFluorescentAntibody());
					caseExportDetailedDto.setCongenitalRubellaDirectFluorescentAntibody(exportDto.getCongenitalRubellaDirectFluorescentAntibody());
					caseExportDetailedDto.setCongenitalRubellaMicroscopy(exportDto.getCongenitalRubellaMicroscopy());
					caseExportDetailedDto.setCongenitalRubellaNeutralizingAntibodies(exportDto.getCongenitalRubellaNeutralizingAntibodies());
					caseExportDetailedDto.setCongenitalRubellaPcrRtPcr(exportDto.getCongenitalRubellaPcrRtPcr());
					caseExportDetailedDto.setCongenitalRubellaGramStain(exportDto.getCongenitalRubellaGramStain());
					caseExportDetailedDto.setCongenitalRubellaLatexAgglutination(exportDto.getCongenitalRubellaLatexAgglutination());
					caseExportDetailedDto.setCongenitalRubellaCqValueDetection(exportDto.getCongenitalRubellaCqValueDetection());
					caseExportDetailedDto.setCongenitalRubellaSequencing(exportDto.getCongenitalRubellaSequencing());
					caseExportDetailedDto.setCongenitalRubellaDnaMicroarray(exportDto.getCongenitalRubellaDnaMicroarray());
					caseExportDetailedDto.setCongenitalRubellaOther(exportDto.getCongenitalRubellaOther());
					caseExportDetailedDto.setCongenitalRubellaAntibodyDetectionDetails(exportDto.getCongenitalRubellaAntibodyDetectionDetails());
					caseExportDetailedDto.setCongenitalRubellaAntigenDetectionDetails(exportDto.getCongenitalRubellaAntigenDetectionDetails());
					caseExportDetailedDto.setCongenitalRubellaRapidTestDetails(exportDto.getCongenitalRubellaRapidTestDetails());
					caseExportDetailedDto.setCongenitalRubellaCultureDetails(exportDto.getCongenitalRubellaCultureDetails());
					caseExportDetailedDto.setCongenitalRubellaHistopathologyDetails(exportDto.getCongenitalRubellaHistopathologyDetails());
					caseExportDetailedDto.setCongenitalRubellaIsolationDetails(exportDto.getCongenitalRubellaIsolationDetails());
					caseExportDetailedDto.setCongenitalRubellaIgmSerumAntibodyDetails(exportDto.getCongenitalRubellaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setCongenitalRubellaIggSerumAntibodyDetails(exportDto.getCongenitalRubellaIggSerumAntibodyDetails());
					caseExportDetailedDto.setCongenitalRubellaIgaSerumAntibodyDetails(exportDto.getCongenitalRubellaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setCongenitalRubellaIncubationTimeDetails(exportDto.getCongenitalRubellaIncubationTimeDetails());
					caseExportDetailedDto.setCongenitalRubellaIndirectFluorescentAntibodyDetails(exportDto.getCongenitalRubellaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCongenitalRubellaDirectFluorescentAntibodyDetails(exportDto.getCongenitalRubellaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCongenitalRubellaMicroscopyDetails(exportDto.getCongenitalRubellaMicroscopyDetails());
					caseExportDetailedDto.setCongenitalRubellaNeutralizingAntibodiesDetails(exportDto.getCongenitalRubellaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setCongenitalRubellaPcrRtPcrDetails(exportDto.getCongenitalRubellaPcrRtPcrDetails());
					caseExportDetailedDto.setCongenitalRubellaGramStainDetails(exportDto.getCongenitalRubellaGramStainDetails());
					caseExportDetailedDto.setCongenitalRubellaLatexAgglutinationDetails(exportDto.getCongenitalRubellaLatexAgglutinationDetails());
					caseExportDetailedDto.setCongenitalRubellaCqValueDetectionDetails(exportDto.getCongenitalRubellaCqValueDetectionDetails());
					caseExportDetailedDto.setCongenitalRubellaSequencingDetails(exportDto.getCongenitalRubellaSequencingDetails());
					caseExportDetailedDto.setCongenitalRubellaDnaMicroarrayDetails(exportDto.getCongenitalRubellaDnaMicroarrayDetails());
					caseExportDetailedDto.setCongenitalRubellaOtherDetails(exportDto.getCongenitalRubellaOtherDetails());
					caseExportDetailedDto.setCsmAntibodyDetection(exportDto.getCsmAntibodyDetection());
					caseExportDetailedDto.setCsmAntigenDetection(exportDto.getCsmAntigenDetection());
					caseExportDetailedDto.setCsmRapidTest(exportDto.getCsmRapidTest());
					caseExportDetailedDto.setCsmCulture(exportDto.getCsmCulture());
					caseExportDetailedDto.setCsmHistopathology(exportDto.getCsmHistopathology());
					caseExportDetailedDto.setCsmIsolation(exportDto.getCsmIsolation());
					caseExportDetailedDto.setCsmIgmSerumAntibody(exportDto.getCsmIgmSerumAntibody());
					caseExportDetailedDto.setCsmIggSerumAntibody(exportDto.getCsmIggSerumAntibody());
					caseExportDetailedDto.setCsmIgaSerumAntibody(exportDto.getCsmIgaSerumAntibody());
					caseExportDetailedDto.setCsmIncubationTime(exportDto.getCsmIncubationTime());
					caseExportDetailedDto.setCsmIndirectFluorescentAntibody(exportDto.getCsmIndirectFluorescentAntibody());
					caseExportDetailedDto.setCsmDirectFluorescentAntibody(exportDto.getCsmDirectFluorescentAntibody());
					caseExportDetailedDto.setCsmMicroscopy(exportDto.getCsmMicroscopy());
					caseExportDetailedDto.setCsmNeutralizingAntibodies(exportDto.getCsmNeutralizingAntibodies());
					caseExportDetailedDto.setCsmPcrRtPcr(exportDto.getCsmPcrRtPcr());
					caseExportDetailedDto.setCsmGramStain(exportDto.getCsmGramStain());
					caseExportDetailedDto.setCsmLatexAgglutination(exportDto.getCsmLatexAgglutination());
					caseExportDetailedDto.setCsmCqValueDetection(exportDto.getCsmCqValueDetection());
					caseExportDetailedDto.setCsmSequencing(exportDto.getCsmSequencing());
					caseExportDetailedDto.setCsmDnaMicroarray(exportDto.getCsmDnaMicroarray());
					caseExportDetailedDto.setCsmOther(exportDto.getCsmOther());
					caseExportDetailedDto.setCsmAntibodyDetectionDetails(exportDto.getCsmAntibodyDetectionDetails());
					caseExportDetailedDto.setCsmAntigenDetectionDetails(exportDto.getCsmAntigenDetectionDetails());
					caseExportDetailedDto.setCsmRapidTestDetails(exportDto.getCsmRapidTestDetails());
					caseExportDetailedDto.setCsmCultureDetails(exportDto.getCsmCultureDetails());
					caseExportDetailedDto.setCsmHistopathologyDetails(exportDto.getCsmHistopathologyDetails());
					caseExportDetailedDto.setCsmIsolationDetails(exportDto.getCsmIsolationDetails());
					caseExportDetailedDto.setCsmIgmSerumAntibodyDetails(exportDto.getCsmIgmSerumAntibodyDetails());
					caseExportDetailedDto.setCsmIggSerumAntibodyDetails(exportDto.getCsmIggSerumAntibodyDetails());
					caseExportDetailedDto.setCsmIgaSerumAntibodyDetails(exportDto.getCsmIgaSerumAntibodyDetails());
					caseExportDetailedDto.setCsmIncubationTimeDetails(exportDto.getCsmIncubationTimeDetails());
					caseExportDetailedDto.setCsmIndirectFluorescentAntibodyDetails(exportDto.getCsmIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCsmDirectFluorescentAntibodyDetails(exportDto.getCsmDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCsmMicroscopyDetails(exportDto.getCsmMicroscopyDetails());
					caseExportDetailedDto.setCsmNeutralizingAntibodiesDetails(exportDto.getCsmNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setCsmPcrRtPcrDetails(exportDto.getCsmPcrRtPcrDetails());
					caseExportDetailedDto.setCsmGramStainDetails(exportDto.getCsmGramStainDetails());
					caseExportDetailedDto.setCsmLatexAgglutinationDetails(exportDto.getCsmLatexAgglutinationDetails());
					caseExportDetailedDto.setCsmCqValueDetectionDetails(exportDto.getCsmCqValueDetectionDetails());
					caseExportDetailedDto.setCsmSequencingDetails(exportDto.getCsmSequencingDetails());
					caseExportDetailedDto.setCsmDnaMicroarrayDetails(exportDto.getCsmDnaMicroarrayDetails());
					caseExportDetailedDto.setCsmOtherDetails(exportDto.getCsmOtherDetails());
					caseExportDetailedDto.setDengueAntibodyDetection(exportDto.getDengueAntibodyDetection());
					caseExportDetailedDto.setDengueAntigenDetection(exportDto.getDengueAntigenDetection());
					caseExportDetailedDto.setDengueRapidTest(exportDto.getDengueRapidTest());
					caseExportDetailedDto.setDengueCulture(exportDto.getDengueCulture());
					caseExportDetailedDto.setDengueHistopathology(exportDto.getDengueHistopathology());
					caseExportDetailedDto.setDengueIsolation(exportDto.getDengueIsolation());
					caseExportDetailedDto.setDengueIgmSerumAntibody(exportDto.getDengueIgmSerumAntibody());
					caseExportDetailedDto.setDengueIggSerumAntibody(exportDto.getDengueIggSerumAntibody());
					caseExportDetailedDto.setDengueIgaSerumAntibody(exportDto.getDengueIgaSerumAntibody());
					caseExportDetailedDto.setDengueIncubationTime(exportDto.getDengueIncubationTime());
					caseExportDetailedDto.setDengueIndirectFluorescentAntibody(exportDto.getDengueIndirectFluorescentAntibody());
					caseExportDetailedDto.setDengueDirectFluorescentAntibody(exportDto.getDengueDirectFluorescentAntibody());
					caseExportDetailedDto.setDengueMicroscopy(exportDto.getDengueMicroscopy());
					caseExportDetailedDto.setDengueNeutralizingAntibodies(exportDto.getDengueNeutralizingAntibodies());
					caseExportDetailedDto.setDenguePcrRtPcr(exportDto.getDenguePcrRtPcr());
					caseExportDetailedDto.setDengueGramStain(exportDto.getDengueGramStain());
					caseExportDetailedDto.setDengueLatexAgglutination(exportDto.getDengueLatexAgglutination());
					caseExportDetailedDto.setDengueCqValueDetection(exportDto.getDengueCqValueDetection());
					caseExportDetailedDto.setDengueSequencing(exportDto.getDengueSequencing());
					caseExportDetailedDto.setDengueDnaMicroarray(exportDto.getDengueDnaMicroarray());
					caseExportDetailedDto.setDengueOther(exportDto.getDengueOther());
					caseExportDetailedDto.setDengueAntibodyDetectionDetails(exportDto.getDengueAntibodyDetectionDetails());
					caseExportDetailedDto.setDengueAntigenDetectionDetails(exportDto.getDengueAntigenDetectionDetails());
					caseExportDetailedDto.setDengueRapidTestDetails(exportDto.getDengueRapidTestDetails());
					caseExportDetailedDto.setDengueCultureDetails(exportDto.getDengueCultureDetails());
					caseExportDetailedDto.setDengueHistopathologyDetails(exportDto.getDengueHistopathologyDetails());
					caseExportDetailedDto.setDengueIsolationDetails(exportDto.getDengueIsolationDetails());
					caseExportDetailedDto.setDengueIgmSerumAntibodyDetails(exportDto.getDengueIgmSerumAntibodyDetails());
					caseExportDetailedDto.setDengueIggSerumAntibodyDetails(exportDto.getDengueIggSerumAntibodyDetails());
					caseExportDetailedDto.setDengueIgaSerumAntibodyDetails(exportDto.getDengueIgaSerumAntibodyDetails());
					caseExportDetailedDto.setDengueIncubationTimeDetails(exportDto.getDengueIncubationTimeDetails());
					caseExportDetailedDto.setDengueIndirectFluorescentAntibodyDetails(exportDto.getDengueIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDengueDirectFluorescentAntibodyDetails(exportDto.getDengueDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDengueMicroscopyDetails(exportDto.getDengueMicroscopyDetails());
					caseExportDetailedDto.setDengueNeutralizingAntibodiesDetails(exportDto.getDengueNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setDenguePcrRtPcrDetails(exportDto.getDenguePcrRtPcrDetails());
					caseExportDetailedDto.setDengueGramStainDetails(exportDto.getDengueGramStainDetails());
					caseExportDetailedDto.setDengueLatexAgglutinationDetails(exportDto.getDengueLatexAgglutinationDetails());
					caseExportDetailedDto.setDengueCqValueDetectionDetails(exportDto.getDengueCqValueDetectionDetails());
					caseExportDetailedDto.setDengueSequencingDetails(exportDto.getDengueSequencingDetails());
					caseExportDetailedDto.setDengueDnaMicroarrayDetails(exportDto.getDengueDnaMicroarrayDetails());
					caseExportDetailedDto.setDengueOtherDetails(exportDto.getDengueOtherDetails());
					caseExportDetailedDto.setEvdAntibodyDetection(exportDto.getEvdAntibodyDetection());
					caseExportDetailedDto.setEvdAntigenDetection(exportDto.getEvdAntigenDetection());
					caseExportDetailedDto.setEvdRapidTest(exportDto.getEvdRapidTest());
					caseExportDetailedDto.setEvdCulture(exportDto.getEvdCulture());
					caseExportDetailedDto.setEvdHistopathology(exportDto.getEvdHistopathology());
					caseExportDetailedDto.setEvdIsolation(exportDto.getEvdIsolation());
					caseExportDetailedDto.setEvdIgmSerumAntibody(exportDto.getEvdIgmSerumAntibody());
					caseExportDetailedDto.setEvdIggSerumAntibody(exportDto.getEvdIggSerumAntibody());
					caseExportDetailedDto.setEvdIgaSerumAntibody(exportDto.getEvdIgaSerumAntibody());
					caseExportDetailedDto.setEvdIncubationTime(exportDto.getEvdIncubationTime());
					caseExportDetailedDto.setEvdIndirectFluorescentAntibody(exportDto.getEvdIndirectFluorescentAntibody());
					caseExportDetailedDto.setEvdDirectFluorescentAntibody(exportDto.getEvdDirectFluorescentAntibody());
					caseExportDetailedDto.setEvdMicroscopy(exportDto.getEvdMicroscopy());
					caseExportDetailedDto.setEvdNeutralizingAntibodies(exportDto.getEvdNeutralizingAntibodies());
					caseExportDetailedDto.setEvdPcrRtPcr(exportDto.getEvdPcrRtPcr());
					caseExportDetailedDto.setEvdGramStain(exportDto.getEvdGramStain());
					caseExportDetailedDto.setEvdLatexAgglutination(exportDto.getEvdLatexAgglutination());
					caseExportDetailedDto.setEvdCqValueDetection(exportDto.getEvdCqValueDetection());
					caseExportDetailedDto.setEvdSequencing(exportDto.getEvdSequencing());
					caseExportDetailedDto.setEvdDnaMicroarray(exportDto.getEvdDnaMicroarray());
					caseExportDetailedDto.setEvdOther(exportDto.getEvdOther());
					caseExportDetailedDto.setEvdAntibodyDetectionDetails(exportDto.getEvdAntibodyDetectionDetails());
					caseExportDetailedDto.setEvdAntigenDetectionDetails(exportDto.getEvdAntigenDetectionDetails());
					caseExportDetailedDto.setEvdRapidTestDetails(exportDto.getEvdRapidTestDetails());
					caseExportDetailedDto.setEvdCultureDetails(exportDto.getEvdCultureDetails());
					caseExportDetailedDto.setEvdHistopathologyDetails(exportDto.getEvdHistopathologyDetails());
					caseExportDetailedDto.setEvdIsolationDetails(exportDto.getEvdIsolationDetails());
					caseExportDetailedDto.setEvdIgmSerumAntibodyDetails(exportDto.getEvdIgmSerumAntibodyDetails());
					caseExportDetailedDto.setEvdIggSerumAntibodyDetails(exportDto.getEvdIggSerumAntibodyDetails());
					caseExportDetailedDto.setEvdIgaSerumAntibodyDetails(exportDto.getEvdIgaSerumAntibodyDetails());
					caseExportDetailedDto.setEvdIncubationTimeDetails(exportDto.getEvdIncubationTimeDetails());
					caseExportDetailedDto.setEvdIndirectFluorescentAntibodyDetails(exportDto.getEvdIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setEvdDirectFluorescentAntibodyDetails(exportDto.getEvdDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setEvdMicroscopyDetails(exportDto.getEvdMicroscopyDetails());
					caseExportDetailedDto.setEvdNeutralizingAntibodiesDetails(exportDto.getEvdNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setEvdPcrRtPcrDetails(exportDto.getEvdPcrRtPcrDetails());
					caseExportDetailedDto.setEvdGramStainDetails(exportDto.getEvdGramStainDetails());
					caseExportDetailedDto.setEvdLatexAgglutinationDetails(exportDto.getEvdLatexAgglutinationDetails());
					caseExportDetailedDto.setEvdCqValueDetectionDetails(exportDto.getEvdCqValueDetectionDetails());
					caseExportDetailedDto.setEvdSequencingDetails(exportDto.getEvdSequencingDetails());
					caseExportDetailedDto.setEvdDnaMicroarrayDetails(exportDto.getEvdDnaMicroarrayDetails());
					caseExportDetailedDto.setEvdOtherDetails(exportDto.getEvdOtherDetails());
					caseExportDetailedDto.setGuineaWormAntibodyDetection(exportDto.getGuineaWormAntibodyDetection());
					caseExportDetailedDto.setGuineaWormAntigenDetection(exportDto.getGuineaWormAntigenDetection());
					caseExportDetailedDto.setGuineaWormRapidTest(exportDto.getGuineaWormRapidTest());
					caseExportDetailedDto.setGuineaWormCulture(exportDto.getGuineaWormCulture());
					caseExportDetailedDto.setGuineaWormHistopathology(exportDto.getGuineaWormHistopathology());
					caseExportDetailedDto.setGuineaWormIsolation(exportDto.getGuineaWormIsolation());
					caseExportDetailedDto.setGuineaWormIgmSerumAntibody(exportDto.getGuineaWormIgmSerumAntibody());
					caseExportDetailedDto.setGuineaWormIggSerumAntibody(exportDto.getGuineaWormIggSerumAntibody());
					caseExportDetailedDto.setGuineaWormIgaSerumAntibody(exportDto.getGuineaWormIgaSerumAntibody());
					caseExportDetailedDto.setGuineaWormIncubationTime(exportDto.getGuineaWormIncubationTime());
					caseExportDetailedDto.setGuineaWormIndirectFluorescentAntibody(exportDto.getGuineaWormIndirectFluorescentAntibody());
					caseExportDetailedDto.setGuineaWormDirectFluorescentAntibody(exportDto.getGuineaWormDirectFluorescentAntibody());
					caseExportDetailedDto.setGuineaWormMicroscopy(exportDto.getGuineaWormMicroscopy());
					caseExportDetailedDto.setGuineaWormNeutralizingAntibodies(exportDto.getGuineaWormNeutralizingAntibodies());
					caseExportDetailedDto.setGuineaWormPcrRtPcr(exportDto.getGuineaWormPcrRtPcr());
					caseExportDetailedDto.setGuineaWormGramStain(exportDto.getGuineaWormGramStain());
					caseExportDetailedDto.setGuineaWormLatexAgglutination(exportDto.getGuineaWormLatexAgglutination());
					caseExportDetailedDto.setGuineaWormCqValueDetection(exportDto.getGuineaWormCqValueDetection());
					caseExportDetailedDto.setGuineaWormSequencing(exportDto.getGuineaWormSequencing());
					caseExportDetailedDto.setGuineaWormDnaMicroarray(exportDto.getGuineaWormDnaMicroarray());
					caseExportDetailedDto.setGuineaWormOther(exportDto.getGuineaWormOther());
					caseExportDetailedDto.setGuineaWormAntibodyDetectionDetails(exportDto.getGuineaWormAntibodyDetectionDetails());
					caseExportDetailedDto.setGuineaWormAntigenDetectionDetails(exportDto.getGuineaWormAntigenDetectionDetails());
					caseExportDetailedDto.setGuineaWormRapidTestDetails(exportDto.getGuineaWormRapidTestDetails());
					caseExportDetailedDto.setGuineaWormCultureDetails(exportDto.getGuineaWormCultureDetails());
					caseExportDetailedDto.setGuineaWormHistopathologyDetails(exportDto.getGuineaWormHistopathologyDetails());
					caseExportDetailedDto.setGuineaWormIsolationDetails(exportDto.getGuineaWormIsolationDetails());
					caseExportDetailedDto.setGuineaWormIgmSerumAntibodyDetails(exportDto.getGuineaWormIgmSerumAntibodyDetails());
					caseExportDetailedDto.setGuineaWormIggSerumAntibodyDetails(exportDto.getGuineaWormIggSerumAntibodyDetails());
					caseExportDetailedDto.setGuineaWormIgaSerumAntibodyDetails(exportDto.getGuineaWormIgaSerumAntibodyDetails());
					caseExportDetailedDto.setGuineaWormIncubationTimeDetails(exportDto.getGuineaWormIncubationTimeDetails());
					caseExportDetailedDto.setGuineaWormIndirectFluorescentAntibodyDetails(exportDto.getGuineaWormIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setGuineaWormDirectFluorescentAntibodyDetails(exportDto.getGuineaWormDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setGuineaWormMicroscopyDetails(exportDto.getGuineaWormMicroscopyDetails());
					caseExportDetailedDto.setGuineaWormNeutralizingAntibodiesDetails(exportDto.getGuineaWormNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setGuineaWormPcrRtPcrDetails(exportDto.getGuineaWormPcrRtPcrDetails());
					caseExportDetailedDto.setGuineaWormGramStainDetails(exportDto.getGuineaWormGramStainDetails());
					caseExportDetailedDto.setGuineaWormLatexAgglutinationDetails(exportDto.getGuineaWormLatexAgglutinationDetails());
					caseExportDetailedDto.setGuineaWormCqValueDetectionDetails(exportDto.getGuineaWormCqValueDetectionDetails());
					caseExportDetailedDto.setGuineaWormSequencingDetails(exportDto.getGuineaWormSequencingDetails());
					caseExportDetailedDto.setGuineaWormDnaMicroarrayDetails(exportDto.getGuineaWormDnaMicroarrayDetails());
					caseExportDetailedDto.setGuineaWormOtherDetails(exportDto.getGuineaWormOtherDetails());
					caseExportDetailedDto.setLassaAntibodyDetection(exportDto.getLassaAntibodyDetection());
					caseExportDetailedDto.setLassaAntigenDetection(exportDto.getLassaAntigenDetection());
					caseExportDetailedDto.setLassaRapidTest(exportDto.getLassaRapidTest());
					caseExportDetailedDto.setLassaCulture(exportDto.getLassaCulture());
					caseExportDetailedDto.setLassaHistopathology(exportDto.getLassaHistopathology());
					caseExportDetailedDto.setLassaIsolation(exportDto.getLassaIsolation());
					caseExportDetailedDto.setLassaIgmSerumAntibody(exportDto.getLassaIgmSerumAntibody());
					caseExportDetailedDto.setLassaIggSerumAntibody(exportDto.getLassaIggSerumAntibody());
					caseExportDetailedDto.setLassaIgaSerumAntibody(exportDto.getLassaIgaSerumAntibody());
					caseExportDetailedDto.setLassaIncubationTime(exportDto.getLassaIncubationTime());
					caseExportDetailedDto.setLassaIndirectFluorescentAntibody(exportDto.getLassaIndirectFluorescentAntibody());
					caseExportDetailedDto.setLassaDirectFluorescentAntibody(exportDto.getLassaDirectFluorescentAntibody());
					caseExportDetailedDto.setLassaMicroscopy(exportDto.getLassaMicroscopy());
					caseExportDetailedDto.setLassaNeutralizingAntibodies(exportDto.getLassaNeutralizingAntibodies());
					caseExportDetailedDto.setLassaPcrRtPcr(exportDto.getLassaPcrRtPcr());
					caseExportDetailedDto.setLassaGramStain(exportDto.getLassaGramStain());
					caseExportDetailedDto.setLassaLatexAgglutination(exportDto.getLassaLatexAgglutination());
					caseExportDetailedDto.setLassaCqValueDetection(exportDto.getLassaCqValueDetection());
					caseExportDetailedDto.setLassaSequencing(exportDto.getLassaSequencing());
					caseExportDetailedDto.setLassaDnaMicroarray(exportDto.getLassaDnaMicroarray());
					caseExportDetailedDto.setLassaOther(exportDto.getLassaOther());
					caseExportDetailedDto.setLassaAntibodyDetectionDetails(exportDto.getLassaAntibodyDetectionDetails());
					caseExportDetailedDto.setLassaAntigenDetectionDetails(exportDto.getLassaAntigenDetectionDetails());
					caseExportDetailedDto.setLassaRapidTestDetails(exportDto.getLassaRapidTestDetails());
					caseExportDetailedDto.setLassaCultureDetails(exportDto.getLassaCultureDetails());
					caseExportDetailedDto.setLassaHistopathologyDetails(exportDto.getLassaHistopathologyDetails());
					caseExportDetailedDto.setLassaIsolationDetails(exportDto.getLassaIsolationDetails());
					caseExportDetailedDto.setLassaIgmSerumAntibodyDetails(exportDto.getLassaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setLassaIggSerumAntibodyDetails(exportDto.getLassaIggSerumAntibodyDetails());
					caseExportDetailedDto.setLassaIgaSerumAntibodyDetails(exportDto.getLassaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setLassaIncubationTimeDetails(exportDto.getLassaIncubationTimeDetails());
					caseExportDetailedDto.setLassaIndirectFluorescentAntibodyDetails(exportDto.getLassaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLassaDirectFluorescentAntibodyDetails(exportDto.getLassaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLassaMicroscopyDetails(exportDto.getLassaMicroscopyDetails());
					caseExportDetailedDto.setLassaNeutralizingAntibodiesDetails(exportDto.getLassaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setLassaPcrRtPcrDetails(exportDto.getLassaPcrRtPcrDetails());
					caseExportDetailedDto.setLassaGramStainDetails(exportDto.getLassaGramStainDetails());
					caseExportDetailedDto.setLassaLatexAgglutinationDetails(exportDto.getLassaLatexAgglutinationDetails());
					caseExportDetailedDto.setLassaCqValueDetectionDetails(exportDto.getLassaCqValueDetectionDetails());
					caseExportDetailedDto.setLassaSequencingDetails(exportDto.getLassaSequencingDetails());
					caseExportDetailedDto.setLassaDnaMicroarrayDetails(exportDto.getLassaDnaMicroarrayDetails());
					caseExportDetailedDto.setLassaOtherDetails(exportDto.getLassaOtherDetails());
					caseExportDetailedDto.setMeaslesAntibodyDetection(exportDto.getMeaslesAntibodyDetection());
					caseExportDetailedDto.setMeaslesAntigenDetection(exportDto.getMeaslesAntigenDetection());
					caseExportDetailedDto.setMeaslesRapidTest(exportDto.getMeaslesRapidTest());
					caseExportDetailedDto.setMeaslesCulture(exportDto.getMeaslesCulture());
					caseExportDetailedDto.setMeaslesHistopathology(exportDto.getMeaslesHistopathology());
					caseExportDetailedDto.setMeaslesIsolation(exportDto.getMeaslesIsolation());
					caseExportDetailedDto.setMeaslesIgmSerumAntibody(exportDto.getMeaslesIgmSerumAntibody());
					caseExportDetailedDto.setMeaslesIggSerumAntibody(exportDto.getMeaslesIggSerumAntibody());
					caseExportDetailedDto.setMeaslesIgaSerumAntibody(exportDto.getMeaslesIgaSerumAntibody());
					caseExportDetailedDto.setMeaslesIncubationTime(exportDto.getMeaslesIncubationTime());
					caseExportDetailedDto.setMeaslesIndirectFluorescentAntibody(exportDto.getMeaslesIndirectFluorescentAntibody());
					caseExportDetailedDto.setMeaslesDirectFluorescentAntibody(exportDto.getMeaslesDirectFluorescentAntibody());
					caseExportDetailedDto.setMeaslesMicroscopy(exportDto.getMeaslesMicroscopy());
					caseExportDetailedDto.setMeaslesNeutralizingAntibodies(exportDto.getMeaslesNeutralizingAntibodies());
					caseExportDetailedDto.setMeaslesPcrRtPcr(exportDto.getMeaslesPcrRtPcr());
					caseExportDetailedDto.setMeaslesGramStain(exportDto.getMeaslesGramStain());
					caseExportDetailedDto.setMeaslesLatexAgglutination(exportDto.getMeaslesLatexAgglutination());
					caseExportDetailedDto.setMeaslesCqValueDetection(exportDto.getMeaslesCqValueDetection());
					caseExportDetailedDto.setMeaslesSequencing(exportDto.getMeaslesSequencing());
					caseExportDetailedDto.setMeaslesDnaMicroarray(exportDto.getMeaslesDnaMicroarray());
					caseExportDetailedDto.setMeaslesOther(exportDto.getMeaslesOther());
					caseExportDetailedDto.setMeaslesAntibodyDetectionDetails(exportDto.getMeaslesAntibodyDetectionDetails());
					caseExportDetailedDto.setMeaslesAntigenDetectionDetails(exportDto.getMeaslesAntigenDetectionDetails());
					caseExportDetailedDto.setMeaslesRapidTestDetails(exportDto.getMeaslesRapidTestDetails());
					caseExportDetailedDto.setMeaslesCultureDetails(exportDto.getMeaslesCultureDetails());
					caseExportDetailedDto.setMeaslesHistopathologyDetails(exportDto.getMeaslesHistopathologyDetails());
					caseExportDetailedDto.setMeaslesIsolationDetails(exportDto.getMeaslesIsolationDetails());
					caseExportDetailedDto.setMeaslesIgmSerumAntibodyDetails(exportDto.getMeaslesIgmSerumAntibodyDetails());
					caseExportDetailedDto.setMeaslesIggSerumAntibodyDetails(exportDto.getMeaslesIggSerumAntibodyDetails());
					caseExportDetailedDto.setMeaslesIgaSerumAntibodyDetails(exportDto.getMeaslesIgaSerumAntibodyDetails());
					caseExportDetailedDto.setMeaslesIncubationTimeDetails(exportDto.getMeaslesIncubationTimeDetails());
					caseExportDetailedDto.setMeaslesIndirectFluorescentAntibodyDetails(exportDto.getMeaslesIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMeaslesDirectFluorescentAntibodyDetails(exportDto.getMeaslesDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMeaslesMicroscopyDetails(exportDto.getMeaslesMicroscopyDetails());
					caseExportDetailedDto.setMeaslesNeutralizingAntibodiesDetails(exportDto.getMeaslesNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setMeaslesPcrRtPcrDetails(exportDto.getMeaslesPcrRtPcrDetails());
					caseExportDetailedDto.setMeaslesGramStainDetails(exportDto.getMeaslesGramStainDetails());
					caseExportDetailedDto.setMeaslesLatexAgglutinationDetails(exportDto.getMeaslesLatexAgglutinationDetails());
					caseExportDetailedDto.setMeaslesCqValueDetectionDetails(exportDto.getMeaslesCqValueDetectionDetails());
					caseExportDetailedDto.setMeaslesSequencingDetails(exportDto.getMeaslesSequencingDetails());
					caseExportDetailedDto.setMeaslesDnaMicroarrayDetails(exportDto.getMeaslesDnaMicroarrayDetails());
					caseExportDetailedDto.setMeaslesOtherDetails(exportDto.getMeaslesOtherDetails());
					caseExportDetailedDto.setMonkeypoxAntibodyDetection(exportDto.getMonkeypoxAntibodyDetection());
					caseExportDetailedDto.setMonkeypoxAntigenDetection(exportDto.getMonkeypoxAntigenDetection());
					caseExportDetailedDto.setMonkeypoxRapidTest(exportDto.getMonkeypoxRapidTest());
					caseExportDetailedDto.setMonkeypoxCulture(exportDto.getMonkeypoxCulture());
					caseExportDetailedDto.setMonkeypoxHistopathology(exportDto.getMonkeypoxHistopathology());
					caseExportDetailedDto.setMonkeypoxIsolation(exportDto.getMonkeypoxIsolation());
					caseExportDetailedDto.setMonkeypoxIgmSerumAntibody(exportDto.getMonkeypoxIgmSerumAntibody());
					caseExportDetailedDto.setMonkeypoxIggSerumAntibody(exportDto.getMonkeypoxIggSerumAntibody());
					caseExportDetailedDto.setMonkeypoxIgaSerumAntibody(exportDto.getMonkeypoxIgaSerumAntibody());
					caseExportDetailedDto.setMonkeypoxIncubationTime(exportDto.getMonkeypoxIncubationTime());
					caseExportDetailedDto.setMonkeypoxIndirectFluorescentAntibody(exportDto.getMonkeypoxIndirectFluorescentAntibody());
					caseExportDetailedDto.setMonkeypoxDirectFluorescentAntibody(exportDto.getMonkeypoxDirectFluorescentAntibody());
					caseExportDetailedDto.setMonkeypoxMicroscopy(exportDto.getMonkeypoxMicroscopy());
					caseExportDetailedDto.setMonkeypoxNeutralizingAntibodies(exportDto.getMonkeypoxNeutralizingAntibodies());
					caseExportDetailedDto.setMonkeypoxPcrRtPcr(exportDto.getMonkeypoxPcrRtPcr());
					caseExportDetailedDto.setMonkeypoxGramStain(exportDto.getMonkeypoxGramStain());
					caseExportDetailedDto.setMonkeypoxLatexAgglutination(exportDto.getMonkeypoxLatexAgglutination());
					caseExportDetailedDto.setMonkeypoxCqValueDetection(exportDto.getMonkeypoxCqValueDetection());
					caseExportDetailedDto.setMonkeypoxSequencing(exportDto.getMonkeypoxSequencing());
					caseExportDetailedDto.setMonkeypoxDnaMicroarray(exportDto.getMonkeypoxDnaMicroarray());
					caseExportDetailedDto.setMonkeypoxOther(exportDto.getMonkeypoxOther());
					caseExportDetailedDto.setMonkeypoxAntibodyDetectionDetails(exportDto.getMonkeypoxAntibodyDetectionDetails());
					caseExportDetailedDto.setMonkeypoxAntigenDetectionDetails(exportDto.getMonkeypoxAntigenDetectionDetails());
					caseExportDetailedDto.setMonkeypoxRapidTestDetails(exportDto.getMonkeypoxRapidTestDetails());
					caseExportDetailedDto.setMonkeypoxCultureDetails(exportDto.getMonkeypoxCultureDetails());
					caseExportDetailedDto.setMonkeypoxHistopathologyDetails(exportDto.getMonkeypoxHistopathologyDetails());
					caseExportDetailedDto.setMonkeypoxIsolationDetails(exportDto.getMonkeypoxIsolationDetails());
					caseExportDetailedDto.setMonkeypoxIgmSerumAntibodyDetails(exportDto.getMonkeypoxIgmSerumAntibodyDetails());
					caseExportDetailedDto.setMonkeypoxIggSerumAntibodyDetails(exportDto.getMonkeypoxIggSerumAntibodyDetails());
					caseExportDetailedDto.setMonkeypoxIgaSerumAntibodyDetails(exportDto.getMonkeypoxIgaSerumAntibodyDetails());
					caseExportDetailedDto.setMonkeypoxIncubationTimeDetails(exportDto.getMonkeypoxIncubationTimeDetails());
					caseExportDetailedDto.setMonkeypoxIndirectFluorescentAntibodyDetails(exportDto.getMonkeypoxIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMonkeypoxDirectFluorescentAntibodyDetails(exportDto.getMonkeypoxDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMonkeypoxMicroscopyDetails(exportDto.getMonkeypoxMicroscopyDetails());
					caseExportDetailedDto.setMonkeypoxNeutralizingAntibodiesDetails(exportDto.getMonkeypoxNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setMonkeypoxPcrRtPcrDetails(exportDto.getMonkeypoxPcrRtPcrDetails());
					caseExportDetailedDto.setMonkeypoxGramStainDetails(exportDto.getMonkeypoxGramStainDetails());
					caseExportDetailedDto.setMonkeypoxLatexAgglutinationDetails(exportDto.getMonkeypoxLatexAgglutinationDetails());
					caseExportDetailedDto.setMonkeypoxCqValueDetectionDetails(exportDto.getMonkeypoxCqValueDetectionDetails());
					caseExportDetailedDto.setMonkeypoxSequencingDetails(exportDto.getMonkeypoxSequencingDetails());
					caseExportDetailedDto.setMonkeypoxDnaMicroarrayDetails(exportDto.getMonkeypoxDnaMicroarrayDetails());
					caseExportDetailedDto.setMonkeypoxOtherDetails(exportDto.getMonkeypoxOtherDetails());
					caseExportDetailedDto.setNewInfluenzaAntibodyDetection(exportDto.getNewInfluenzaAntibodyDetection());
					caseExportDetailedDto.setNewInfluenzaAntigenDetection(exportDto.getNewInfluenzaAntigenDetection());
					caseExportDetailedDto.setNewInfluenzaRapidTest(exportDto.getNewInfluenzaRapidTest());
					caseExportDetailedDto.setNewInfluenzaCulture(exportDto.getNewInfluenzaCulture());
					caseExportDetailedDto.setNewInfluenzaHistopathology(exportDto.getNewInfluenzaHistopathology());
					caseExportDetailedDto.setNewInfluenzaIsolation(exportDto.getNewInfluenzaIsolation());
					caseExportDetailedDto.setNewInfluenzaIgmSerumAntibody(exportDto.getNewInfluenzaIgmSerumAntibody());
					caseExportDetailedDto.setNewInfluenzaIggSerumAntibody(exportDto.getNewInfluenzaIggSerumAntibody());
					caseExportDetailedDto.setNewInfluenzaIgaSerumAntibody(exportDto.getNewInfluenzaIgaSerumAntibody());
					caseExportDetailedDto.setNewInfluenzaIncubationTime(exportDto.getNewInfluenzaIncubationTime());
					caseExportDetailedDto.setNewInfluenzaIndirectFluorescentAntibody(exportDto.getNewInfluenzaIndirectFluorescentAntibody());
					caseExportDetailedDto.setNewInfluenzaDirectFluorescentAntibody(exportDto.getNewInfluenzaDirectFluorescentAntibody());
					caseExportDetailedDto.setNewInfluenzaMicroscopy(exportDto.getNewInfluenzaMicroscopy());
					caseExportDetailedDto.setNewInfluenzaNeutralizingAntibodies(exportDto.getNewInfluenzaNeutralizingAntibodies());
					caseExportDetailedDto.setNewInfluenzaPcrRtPcr(exportDto.getNewInfluenzaPcrRtPcr());
					caseExportDetailedDto.setNewInfluenzaGramStain(exportDto.getNewInfluenzaGramStain());
					caseExportDetailedDto.setNewInfluenzaLatexAgglutination(exportDto.getNewInfluenzaLatexAgglutination());
					caseExportDetailedDto.setNewInfluenzaCqValueDetection(exportDto.getNewInfluenzaCqValueDetection());
					caseExportDetailedDto.setNewInfluenzaSequencing(exportDto.getNewInfluenzaSequencing());
					caseExportDetailedDto.setNewInfluenzaDnaMicroarray(exportDto.getNewInfluenzaDnaMicroarray());
					caseExportDetailedDto.setNewInfluenzaOther(exportDto.getNewInfluenzaOther());
					caseExportDetailedDto.setNewInfluenzaAntibodyDetectionDetails(exportDto.getNewInfluenzaAntibodyDetectionDetails());
					caseExportDetailedDto.setNewInfluenzaAntigenDetectionDetails(exportDto.getNewInfluenzaAntigenDetectionDetails());
					caseExportDetailedDto.setNewInfluenzaRapidTestDetails(exportDto.getNewInfluenzaRapidTestDetails());
					caseExportDetailedDto.setNewInfluenzaCultureDetails(exportDto.getNewInfluenzaCultureDetails());
					caseExportDetailedDto.setNewInfluenzaHistopathologyDetails(exportDto.getNewInfluenzaHistopathologyDetails());
					caseExportDetailedDto.setNewInfluenzaIsolationDetails(exportDto.getNewInfluenzaIsolationDetails());
					caseExportDetailedDto.setNewInfluenzaIgmSerumAntibodyDetails(exportDto.getNewInfluenzaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setNewInfluenzaIggSerumAntibodyDetails(exportDto.getNewInfluenzaIggSerumAntibodyDetails());
					caseExportDetailedDto.setNewInfluenzaIgaSerumAntibodyDetails(exportDto.getNewInfluenzaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setNewInfluenzaIncubationTimeDetails(exportDto.getNewInfluenzaIncubationTimeDetails());
					caseExportDetailedDto.setNewInfluenzaIndirectFluorescentAntibodyDetails(exportDto.getNewInfluenzaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNewInfluenzaDirectFluorescentAntibodyDetails(exportDto.getNewInfluenzaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNewInfluenzaMicroscopyDetails(exportDto.getNewInfluenzaMicroscopyDetails());
					caseExportDetailedDto.setNewInfluenzaNeutralizingAntibodiesDetails(exportDto.getNewInfluenzaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setNewInfluenzaPcrRtPcrDetails(exportDto.getNewInfluenzaPcrRtPcrDetails());
					caseExportDetailedDto.setNewInfluenzaGramStainDetails(exportDto.getNewInfluenzaGramStainDetails());
					caseExportDetailedDto.setNewInfluenzaLatexAgglutinationDetails(exportDto.getNewInfluenzaLatexAgglutinationDetails());
					caseExportDetailedDto.setNewInfluenzaCqValueDetectionDetails(exportDto.getNewInfluenzaCqValueDetectionDetails());
					caseExportDetailedDto.setNewInfluenzaSequencingDetails(exportDto.getNewInfluenzaSequencingDetails());
					caseExportDetailedDto.setNewInfluenzaDnaMicroarrayDetails(exportDto.getNewInfluenzaDnaMicroarrayDetails());
					caseExportDetailedDto.setNewInfluenzaOtherDetails(exportDto.getNewInfluenzaOtherDetails());
					caseExportDetailedDto.setPlagueAntibodyDetection(exportDto.getPlagueAntibodyDetection());
					caseExportDetailedDto.setPlagueAntigenDetection(exportDto.getPlagueAntigenDetection());
					caseExportDetailedDto.setPlagueRapidTest(exportDto.getPlagueRapidTest());
					caseExportDetailedDto.setPlagueCulture(exportDto.getPlagueCulture());
					caseExportDetailedDto.setPlagueHistopathology(exportDto.getPlagueHistopathology());
					caseExportDetailedDto.setPlagueIsolation(exportDto.getPlagueIsolation());
					caseExportDetailedDto.setPlagueIgmSerumAntibody(exportDto.getPlagueIgmSerumAntibody());
					caseExportDetailedDto.setPlagueIggSerumAntibody(exportDto.getPlagueIggSerumAntibody());
					caseExportDetailedDto.setPlagueIgaSerumAntibody(exportDto.getPlagueIgaSerumAntibody());
					caseExportDetailedDto.setPlagueIncubationTime(exportDto.getPlagueIncubationTime());
					caseExportDetailedDto.setPlagueIndirectFluorescentAntibody(exportDto.getPlagueIndirectFluorescentAntibody());
					caseExportDetailedDto.setPlagueDirectFluorescentAntibody(exportDto.getPlagueDirectFluorescentAntibody());
					caseExportDetailedDto.setPlagueMicroscopy(exportDto.getPlagueMicroscopy());
					caseExportDetailedDto.setPlagueNeutralizingAntibodies(exportDto.getPlagueNeutralizingAntibodies());
					caseExportDetailedDto.setPlaguePcrRtPcr(exportDto.getPlaguePcrRtPcr());
					caseExportDetailedDto.setPlagueGramStain(exportDto.getPlagueGramStain());
					caseExportDetailedDto.setPlagueLatexAgglutination(exportDto.getPlagueLatexAgglutination());
					caseExportDetailedDto.setPlagueCqValueDetection(exportDto.getPlagueCqValueDetection());
					caseExportDetailedDto.setPlagueSequencing(exportDto.getPlagueSequencing());
					caseExportDetailedDto.setPlagueDnaMicroarray(exportDto.getPlagueDnaMicroarray());
					caseExportDetailedDto.setPlagueOther(exportDto.getPlagueOther());
					caseExportDetailedDto.setPlagueAntibodyDetectionDetails(exportDto.getPlagueAntibodyDetectionDetails());
					caseExportDetailedDto.setPlagueAntigenDetectionDetails(exportDto.getPlagueAntigenDetectionDetails());
					caseExportDetailedDto.setPlagueRapidTestDetails(exportDto.getPlagueRapidTestDetails());
					caseExportDetailedDto.setPlagueCultureDetails(exportDto.getPlagueCultureDetails());
					caseExportDetailedDto.setPlagueHistopathologyDetails(exportDto.getPlagueHistopathologyDetails());
					caseExportDetailedDto.setPlagueIsolationDetails(exportDto.getPlagueIsolationDetails());
					caseExportDetailedDto.setPlagueIgmSerumAntibodyDetails(exportDto.getPlagueIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPlagueIggSerumAntibodyDetails(exportDto.getPlagueIggSerumAntibodyDetails());
					caseExportDetailedDto.setPlagueIgaSerumAntibodyDetails(exportDto.getPlagueIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPlagueIncubationTimeDetails(exportDto.getPlagueIncubationTimeDetails());
					caseExportDetailedDto.setPlagueIndirectFluorescentAntibodyDetails(exportDto.getPlagueIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPlagueDirectFluorescentAntibodyDetails(exportDto.getPlagueDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPlagueMicroscopyDetails(exportDto.getPlagueMicroscopyDetails());
					caseExportDetailedDto.setPlagueNeutralizingAntibodiesDetails(exportDto.getPlagueNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPlaguePcrRtPcrDetails(exportDto.getPlaguePcrRtPcrDetails());
					caseExportDetailedDto.setPlagueGramStainDetails(exportDto.getPlagueGramStainDetails());
					caseExportDetailedDto.setPlagueLatexAgglutinationDetails(exportDto.getPlagueLatexAgglutinationDetails());
					caseExportDetailedDto.setPlagueCqValueDetectionDetails(exportDto.getPlagueCqValueDetectionDetails());
					caseExportDetailedDto.setPlagueSequencingDetails(exportDto.getPlagueSequencingDetails());
					caseExportDetailedDto.setPlagueDnaMicroarrayDetails(exportDto.getPlagueDnaMicroarrayDetails());
					caseExportDetailedDto.setPlagueOtherDetails(exportDto.getPlagueOtherDetails());
					caseExportDetailedDto.setPolioAntibodyDetection(exportDto.getPolioAntibodyDetection());
					caseExportDetailedDto.setPolioAntigenDetection(exportDto.getPolioAntigenDetection());
					caseExportDetailedDto.setPolioRapidTest(exportDto.getPolioRapidTest());
					caseExportDetailedDto.setPolioCulture(exportDto.getPolioCulture());
					caseExportDetailedDto.setPolioHistopathology(exportDto.getPolioHistopathology());
					caseExportDetailedDto.setPolioIsolation(exportDto.getPolioIsolation());
					caseExportDetailedDto.setPolioIgmSerumAntibody(exportDto.getPolioIgmSerumAntibody());
					caseExportDetailedDto.setPolioIggSerumAntibody(exportDto.getPolioIggSerumAntibody());
					caseExportDetailedDto.setPolioIgaSerumAntibody(exportDto.getPolioIgaSerumAntibody());
					caseExportDetailedDto.setPolioIncubationTime(exportDto.getPolioIncubationTime());
					caseExportDetailedDto.setPolioIndirectFluorescentAntibody(exportDto.getPolioIndirectFluorescentAntibody());
					caseExportDetailedDto.setPolioDirectFluorescentAntibody(exportDto.getPolioDirectFluorescentAntibody());
					caseExportDetailedDto.setPolioMicroscopy(exportDto.getPolioMicroscopy());
					caseExportDetailedDto.setPolioNeutralizingAntibodies(exportDto.getPolioNeutralizingAntibodies());
					caseExportDetailedDto.setPolioPcrRtPcr(exportDto.getPolioPcrRtPcr());
					caseExportDetailedDto.setPolioGramStain(exportDto.getPolioGramStain());
					caseExportDetailedDto.setPolioLatexAgglutination(exportDto.getPolioLatexAgglutination());
					caseExportDetailedDto.setPolioCqValueDetection(exportDto.getPolioCqValueDetection());
					caseExportDetailedDto.setPolioSequencing(exportDto.getPolioSequencing());
					caseExportDetailedDto.setPolioDnaMicroarray(exportDto.getPolioDnaMicroarray());
					caseExportDetailedDto.setPolioOther(exportDto.getPolioOther());
					caseExportDetailedDto.setPolioAntibodyDetectionDetails(exportDto.getPolioAntibodyDetectionDetails());
					caseExportDetailedDto.setPolioAntigenDetectionDetails(exportDto.getPolioAntigenDetectionDetails());
					caseExportDetailedDto.setPolioRapidTestDetails(exportDto.getPolioRapidTestDetails());
					caseExportDetailedDto.setPolioCultureDetails(exportDto.getPolioCultureDetails());
					caseExportDetailedDto.setPolioHistopathologyDetails(exportDto.getPolioHistopathologyDetails());
					caseExportDetailedDto.setPolioIsolationDetails(exportDto.getPolioIsolationDetails());
					caseExportDetailedDto.setPolioIgmSerumAntibodyDetails(exportDto.getPolioIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPolioIggSerumAntibodyDetails(exportDto.getPolioIggSerumAntibodyDetails());
					caseExportDetailedDto.setPolioIgaSerumAntibodyDetails(exportDto.getPolioIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPolioIncubationTimeDetails(exportDto.getPolioIncubationTimeDetails());
					caseExportDetailedDto.setPolioIndirectFluorescentAntibodyDetails(exportDto.getPolioIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPolioDirectFluorescentAntibodyDetails(exportDto.getPolioDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPolioMicroscopyDetails(exportDto.getPolioMicroscopyDetails());
					caseExportDetailedDto.setPolioNeutralizingAntibodiesDetails(exportDto.getPolioNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPolioPcrRtPcrDetails(exportDto.getPolioPcrRtPcrDetails());
					caseExportDetailedDto.setPolioGramStainDetails(exportDto.getPolioGramStainDetails());
					caseExportDetailedDto.setPolioLatexAgglutinationDetails(exportDto.getPolioLatexAgglutinationDetails());
					caseExportDetailedDto.setPolioCqValueDetectionDetails(exportDto.getPolioCqValueDetectionDetails());
					caseExportDetailedDto.setPolioSequencingDetails(exportDto.getPolioSequencingDetails());
					caseExportDetailedDto.setPolioDnaMicroarrayDetails(exportDto.getPolioDnaMicroarrayDetails());
					caseExportDetailedDto.setPolioOtherDetails(exportDto.getPolioOtherDetails());
					caseExportDetailedDto.setUnspecifiedVhfAntibodyDetection(exportDto.getUnspecifiedVhfAntibodyDetection());
					caseExportDetailedDto.setUnspecifiedVhfAntigenDetection(exportDto.getUnspecifiedVhfAntigenDetection());
					caseExportDetailedDto.setUnspecifiedVhfRapidTest(exportDto.getUnspecifiedVhfRapidTest());
					caseExportDetailedDto.setUnspecifiedVhfCulture(exportDto.getUnspecifiedVhfCulture());
					caseExportDetailedDto.setUnspecifiedVhfHistopathology(exportDto.getUnspecifiedVhfHistopathology());
					caseExportDetailedDto.setUnspecifiedVhfIsolation(exportDto.getUnspecifiedVhfIsolation());
					caseExportDetailedDto.setUnspecifiedVhfIgmSerumAntibody(exportDto.getUnspecifiedVhfIgmSerumAntibody());
					caseExportDetailedDto.setUnspecifiedVhfIggSerumAntibody(exportDto.getUnspecifiedVhfIggSerumAntibody());
					caseExportDetailedDto.setUnspecifiedVhfIgaSerumAntibody(exportDto.getUnspecifiedVhfIgaSerumAntibody());
					caseExportDetailedDto.setUnspecifiedVhfIncubationTime(exportDto.getUnspecifiedVhfIncubationTime());
					caseExportDetailedDto.setUnspecifiedVhfIndirectFluorescentAntibody(exportDto.getUnspecifiedVhfIndirectFluorescentAntibody());
					caseExportDetailedDto.setUnspecifiedVhfDirectFluorescentAntibody(exportDto.getUnspecifiedVhfDirectFluorescentAntibody());
					caseExportDetailedDto.setUnspecifiedVhfMicroscopy(exportDto.getUnspecifiedVhfMicroscopy());
					caseExportDetailedDto.setUnspecifiedVhfNeutralizingAntibodies(exportDto.getUnspecifiedVhfNeutralizingAntibodies());
					caseExportDetailedDto.setUnspecifiedVhfPcrRtPcr(exportDto.getUnspecifiedVhfPcrRtPcr());
					caseExportDetailedDto.setUnspecifiedVhfGramStain(exportDto.getUnspecifiedVhfGramStain());
					caseExportDetailedDto.setUnspecifiedVhfLatexAgglutination(exportDto.getUnspecifiedVhfLatexAgglutination());
					caseExportDetailedDto.setUnspecifiedVhfCqValueDetection(exportDto.getUnspecifiedVhfCqValueDetection());
					caseExportDetailedDto.setUnspecifiedVhfSequencing(exportDto.getUnspecifiedVhfSequencing());
					caseExportDetailedDto.setUnspecifiedVhfDnaMicroarray(exportDto.getUnspecifiedVhfDnaMicroarray());
					caseExportDetailedDto.setUnspecifiedVhfOther(exportDto.getUnspecifiedVhfOther());
					caseExportDetailedDto.setUnspecifiedVhfAntibodyDetectionDetails(exportDto.getUnspecifiedVhfAntibodyDetectionDetails());
					caseExportDetailedDto.setUnspecifiedVhfAntigenDetectionDetails(exportDto.getUnspecifiedVhfAntigenDetectionDetails());
					caseExportDetailedDto.setUnspecifiedVhfRapidTestDetails(exportDto.getUnspecifiedVhfRapidTestDetails());
					caseExportDetailedDto.setUnspecifiedVhfCultureDetails(exportDto.getUnspecifiedVhfCultureDetails());
					caseExportDetailedDto.setUnspecifiedVhfHistopathologyDetails(exportDto.getUnspecifiedVhfHistopathologyDetails());
					caseExportDetailedDto.setUnspecifiedVhfIsolationDetails(exportDto.getUnspecifiedVhfIsolationDetails());
					caseExportDetailedDto.setUnspecifiedVhfIgmSerumAntibodyDetails(exportDto.getUnspecifiedVhfIgmSerumAntibodyDetails());
					caseExportDetailedDto.setUnspecifiedVhfIggSerumAntibodyDetails(exportDto.getUnspecifiedVhfIggSerumAntibodyDetails());
					caseExportDetailedDto.setUnspecifiedVhfIgaSerumAntibodyDetails(exportDto.getUnspecifiedVhfIgaSerumAntibodyDetails());
					caseExportDetailedDto.setUnspecifiedVhfIncubationTimeDetails(exportDto.getUnspecifiedVhfIncubationTimeDetails());
					caseExportDetailedDto.setUnspecifiedVhfIndirectFluorescentAntibodyDetails(exportDto.getUnspecifiedVhfIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setUnspecifiedVhfDirectFluorescentAntibodyDetails(exportDto.getUnspecifiedVhfDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setUnspecifiedVhfMicroscopyDetails(exportDto.getUnspecifiedVhfMicroscopyDetails());
					caseExportDetailedDto.setUnspecifiedVhfNeutralizingAntibodiesDetails(exportDto.getUnspecifiedVhfNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setUnspecifiedVhfPcrRtPcrDetails(exportDto.getUnspecifiedVhfPcrRtPcrDetails());
					caseExportDetailedDto.setUnspecifiedVhfGramStainDetails(exportDto.getUnspecifiedVhfGramStainDetails());
					caseExportDetailedDto.setUnspecifiedVhfLatexAgglutinationDetails(exportDto.getUnspecifiedVhfLatexAgglutinationDetails());
					caseExportDetailedDto.setUnspecifiedVhfCqValueDetectionDetails(exportDto.getUnspecifiedVhfCqValueDetectionDetails());
					caseExportDetailedDto.setUnspecifiedVhfSequencingDetails(exportDto.getUnspecifiedVhfSequencingDetails());
					caseExportDetailedDto.setUnspecifiedVhfDnaMicroarrayDetails(exportDto.getUnspecifiedVhfDnaMicroarrayDetails());
					caseExportDetailedDto.setUnspecifiedVhfOtherDetails(exportDto.getUnspecifiedVhfOtherDetails());
					caseExportDetailedDto.setWestNileFeverAntibodyDetection(exportDto.getWestNileFeverAntibodyDetection());
					caseExportDetailedDto.setWestNileFeverAntigenDetection(exportDto.getWestNileFeverAntigenDetection());
					caseExportDetailedDto.setWestNileFeverRapidTest(exportDto.getWestNileFeverRapidTest());
					caseExportDetailedDto.setWestNileFeverCulture(exportDto.getWestNileFeverCulture());
					caseExportDetailedDto.setWestNileFeverHistopathology(exportDto.getWestNileFeverHistopathology());
					caseExportDetailedDto.setWestNileFeverIsolation(exportDto.getWestNileFeverIsolation());
					caseExportDetailedDto.setWestNileFeverIgmSerumAntibody(exportDto.getWestNileFeverIgmSerumAntibody());
					caseExportDetailedDto.setWestNileFeverIggSerumAntibody(exportDto.getWestNileFeverIggSerumAntibody());
					caseExportDetailedDto.setWestNileFeverIgaSerumAntibody(exportDto.getWestNileFeverIgaSerumAntibody());
					caseExportDetailedDto.setWestNileFeverIncubationTime(exportDto.getWestNileFeverIncubationTime());
					caseExportDetailedDto.setWestNileFeverIndirectFluorescentAntibody(exportDto.getWestNileFeverIndirectFluorescentAntibody());
					caseExportDetailedDto.setWestNileFeverDirectFluorescentAntibody(exportDto.getWestNileFeverDirectFluorescentAntibody());
					caseExportDetailedDto.setWestNileFeverMicroscopy(exportDto.getWestNileFeverMicroscopy());
					caseExportDetailedDto.setWestNileFeverNeutralizingAntibodies(exportDto.getWestNileFeverNeutralizingAntibodies());
					caseExportDetailedDto.setWestNileFeverPcrRtPcr(exportDto.getWestNileFeverPcrRtPcr());
					caseExportDetailedDto.setWestNileFeverGramStain(exportDto.getWestNileFeverGramStain());
					caseExportDetailedDto.setWestNileFeverLatexAgglutination(exportDto.getWestNileFeverLatexAgglutination());
					caseExportDetailedDto.setWestNileFeverCqValueDetection(exportDto.getWestNileFeverCqValueDetection());
					caseExportDetailedDto.setWestNileFeverSequencing(exportDto.getWestNileFeverSequencing());
					caseExportDetailedDto.setWestNileFeverDnaMicroarray(exportDto.getWestNileFeverDnaMicroarray());
					caseExportDetailedDto.setWestNileFeverOther(exportDto.getWestNileFeverOther());
					caseExportDetailedDto.setWestNileFeverAntibodyDetectionDetails(exportDto.getWestNileFeverAntibodyDetectionDetails());
					caseExportDetailedDto.setWestNileFeverAntigenDetectionDetails(exportDto.getWestNileFeverAntigenDetectionDetails());
					caseExportDetailedDto.setWestNileFeverRapidTestDetails(exportDto.getWestNileFeverRapidTestDetails());
					caseExportDetailedDto.setWestNileFeverCultureDetails(exportDto.getWestNileFeverCultureDetails());
					caseExportDetailedDto.setWestNileFeverHistopathologyDetails(exportDto.getWestNileFeverHistopathologyDetails());
					caseExportDetailedDto.setWestNileFeverIsolationDetails(exportDto.getWestNileFeverIsolationDetails());
					caseExportDetailedDto.setWestNileFeverIgmSerumAntibodyDetails(exportDto.getWestNileFeverIgmSerumAntibodyDetails());
					caseExportDetailedDto.setWestNileFeverIggSerumAntibodyDetails(exportDto.getWestNileFeverIggSerumAntibodyDetails());
					caseExportDetailedDto.setWestNileFeverIgaSerumAntibodyDetails(exportDto.getWestNileFeverIgaSerumAntibodyDetails());
					caseExportDetailedDto.setWestNileFeverIncubationTimeDetails(exportDto.getWestNileFeverIncubationTimeDetails());
					caseExportDetailedDto.setWestNileFeverIndirectFluorescentAntibodyDetails(exportDto.getWestNileFeverIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setWestNileFeverDirectFluorescentAntibodyDetails(exportDto.getWestNileFeverDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setWestNileFeverMicroscopyDetails(exportDto.getWestNileFeverMicroscopyDetails());
					caseExportDetailedDto.setWestNileFeverNeutralizingAntibodiesDetails(exportDto.getWestNileFeverNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setWestNileFeverPcrRtPcrDetails(exportDto.getWestNileFeverPcrRtPcrDetails());
					caseExportDetailedDto.setWestNileFeverGramStainDetails(exportDto.getWestNileFeverGramStainDetails());
					caseExportDetailedDto.setWestNileFeverLatexAgglutinationDetails(exportDto.getWestNileFeverLatexAgglutinationDetails());
					caseExportDetailedDto.setWestNileFeverCqValueDetectionDetails(exportDto.getWestNileFeverCqValueDetectionDetails());
					caseExportDetailedDto.setWestNileFeverSequencingDetails(exportDto.getWestNileFeverSequencingDetails());
					caseExportDetailedDto.setWestNileFeverDnaMicroarrayDetails(exportDto.getWestNileFeverDnaMicroarrayDetails());
					caseExportDetailedDto.setWestNileFeverOtherDetails(exportDto.getWestNileFeverOtherDetails());
					caseExportDetailedDto.setYellowFeverAntibodyDetection(exportDto.getYellowFeverAntibodyDetection());
					caseExportDetailedDto.setYellowFeverAntigenDetection(exportDto.getYellowFeverAntigenDetection());
					caseExportDetailedDto.setYellowFeverRapidTest(exportDto.getYellowFeverRapidTest());
					caseExportDetailedDto.setYellowFeverCulture(exportDto.getYellowFeverCulture());
					caseExportDetailedDto.setYellowFeverHistopathology(exportDto.getYellowFeverHistopathology());
					caseExportDetailedDto.setYellowFeverIsolation(exportDto.getYellowFeverIsolation());
					caseExportDetailedDto.setYellowFeverIgmSerumAntibody(exportDto.getYellowFeverIgmSerumAntibody());
					caseExportDetailedDto.setYellowFeverIggSerumAntibody(exportDto.getYellowFeverIggSerumAntibody());
					caseExportDetailedDto.setYellowFeverIgaSerumAntibody(exportDto.getYellowFeverIgaSerumAntibody());
					caseExportDetailedDto.setYellowFeverIncubationTime(exportDto.getYellowFeverIncubationTime());
					caseExportDetailedDto.setYellowFeverIndirectFluorescentAntibody(exportDto.getYellowFeverIndirectFluorescentAntibody());
					caseExportDetailedDto.setYellowFeverDirectFluorescentAntibody(exportDto.getYellowFeverDirectFluorescentAntibody());
					caseExportDetailedDto.setYellowFeverMicroscopy(exportDto.getYellowFeverMicroscopy());
					caseExportDetailedDto.setYellowFeverNeutralizingAntibodies(exportDto.getYellowFeverNeutralizingAntibodies());
					caseExportDetailedDto.setYellowFeverPcrRtPcr(exportDto.getYellowFeverPcrRtPcr());
					caseExportDetailedDto.setYellowFeverGramStain(exportDto.getYellowFeverGramStain());
					caseExportDetailedDto.setYellowFeverLatexAgglutination(exportDto.getYellowFeverLatexAgglutination());
					caseExportDetailedDto.setYellowFeverCqValueDetection(exportDto.getYellowFeverCqValueDetection());
					caseExportDetailedDto.setYellowFeverSequencing(exportDto.getYellowFeverSequencing());
					caseExportDetailedDto.setYellowFeverDnaMicroarray(exportDto.getYellowFeverDnaMicroarray());
					caseExportDetailedDto.setYellowFeverOther(exportDto.getYellowFeverOther());
					caseExportDetailedDto.setYellowFeverAntibodyDetectionDetails(exportDto.getYellowFeverAntibodyDetectionDetails());
					caseExportDetailedDto.setYellowFeverAntigenDetectionDetails(exportDto.getYellowFeverAntigenDetectionDetails());
					caseExportDetailedDto.setYellowFeverRapidTestDetails(exportDto.getYellowFeverRapidTestDetails());
					caseExportDetailedDto.setYellowFeverCultureDetails(exportDto.getYellowFeverCultureDetails());
					caseExportDetailedDto.setYellowFeverHistopathologyDetails(exportDto.getYellowFeverHistopathologyDetails());
					caseExportDetailedDto.setYellowFeverIsolationDetails(exportDto.getYellowFeverIsolationDetails());
					caseExportDetailedDto.setYellowFeverIgmSerumAntibodyDetails(exportDto.getYellowFeverIgmSerumAntibodyDetails());
					caseExportDetailedDto.setYellowFeverIggSerumAntibodyDetails(exportDto.getYellowFeverIggSerumAntibodyDetails());
					caseExportDetailedDto.setYellowFeverIgaSerumAntibodyDetails(exportDto.getYellowFeverIgaSerumAntibodyDetails());
					caseExportDetailedDto.setYellowFeverIncubationTimeDetails(exportDto.getYellowFeverIncubationTimeDetails());
					caseExportDetailedDto.setYellowFeverIndirectFluorescentAntibodyDetails(exportDto.getYellowFeverIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setYellowFeverDirectFluorescentAntibodyDetails(exportDto.getYellowFeverDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setYellowFeverMicroscopyDetails(exportDto.getYellowFeverMicroscopyDetails());
					caseExportDetailedDto.setYellowFeverNeutralizingAntibodiesDetails(exportDto.getYellowFeverNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setYellowFeverPcrRtPcrDetails(exportDto.getYellowFeverPcrRtPcrDetails());
					caseExportDetailedDto.setYellowFeverGramStainDetails(exportDto.getYellowFeverGramStainDetails());
					caseExportDetailedDto.setYellowFeverLatexAgglutinationDetails(exportDto.getYellowFeverLatexAgglutinationDetails());
					caseExportDetailedDto.setYellowFeverCqValueDetectionDetails(exportDto.getYellowFeverCqValueDetectionDetails());
					caseExportDetailedDto.setYellowFeverSequencingDetails(exportDto.getYellowFeverSequencingDetails());
					caseExportDetailedDto.setYellowFeverDnaMicroarrayDetails(exportDto.getYellowFeverDnaMicroarrayDetails());
					caseExportDetailedDto.setYellowFeverOtherDetails(exportDto.getYellowFeverOtherDetails());
					caseExportDetailedDto.setRabiesAntibodyDetection(exportDto.getRabiesAntibodyDetection());
					caseExportDetailedDto.setRabiesAntigenDetection(exportDto.getRabiesAntigenDetection());
					caseExportDetailedDto.setRabiesRapidTest(exportDto.getRabiesRapidTest());
					caseExportDetailedDto.setRabiesCulture(exportDto.getRabiesCulture());
					caseExportDetailedDto.setRabiesHistopathology(exportDto.getRabiesHistopathology());
					caseExportDetailedDto.setRabiesIsolation(exportDto.getRabiesIsolation());
					caseExportDetailedDto.setRabiesIgmSerumAntibody(exportDto.getRabiesIgmSerumAntibody());
					caseExportDetailedDto.setRabiesIggSerumAntibody(exportDto.getRabiesIggSerumAntibody());
					caseExportDetailedDto.setRabiesIgaSerumAntibody(exportDto.getRabiesIgaSerumAntibody());
					caseExportDetailedDto.setRabiesIncubationTime(exportDto.getRabiesIncubationTime());
					caseExportDetailedDto.setRabiesIndirectFluorescentAntibody(exportDto.getRabiesIndirectFluorescentAntibody());
					caseExportDetailedDto.setRabiesDirectFluorescentAntibody(exportDto.getRabiesDirectFluorescentAntibody());
					caseExportDetailedDto.setRabiesMicroscopy(exportDto.getRabiesMicroscopy());
					caseExportDetailedDto.setRabiesNeutralizingAntibodies(exportDto.getRabiesNeutralizingAntibodies());
					caseExportDetailedDto.setRabiesPcrRtPcr(exportDto.getRabiesPcrRtPcr());
					caseExportDetailedDto.setRabiesGramStain(exportDto.getRabiesGramStain());
					caseExportDetailedDto.setRabiesLatexAgglutination(exportDto.getRabiesLatexAgglutination());
					caseExportDetailedDto.setRabiesCqValueDetection(exportDto.getRabiesCqValueDetection());
					caseExportDetailedDto.setRabiesSequencing(exportDto.getRabiesSequencing());
					caseExportDetailedDto.setRabiesDnaMicroarray(exportDto.getRabiesDnaMicroarray());
					caseExportDetailedDto.setRabiesOther(exportDto.getRabiesOther());
					caseExportDetailedDto.setRabiesAntibodyDetectionDetails(exportDto.getRabiesAntibodyDetectionDetails());
					caseExportDetailedDto.setRabiesAntigenDetectionDetails(exportDto.getRabiesAntigenDetectionDetails());
					caseExportDetailedDto.setRabiesRapidTestDetails(exportDto.getRabiesRapidTestDetails());
					caseExportDetailedDto.setRabiesCultureDetails(exportDto.getRabiesCultureDetails());
					caseExportDetailedDto.setRabiesHistopathologyDetails(exportDto.getRabiesHistopathologyDetails());
					caseExportDetailedDto.setRabiesIsolationDetails(exportDto.getRabiesIsolationDetails());
					caseExportDetailedDto.setRabiesIgmSerumAntibodyDetails(exportDto.getRabiesIgmSerumAntibodyDetails());
					caseExportDetailedDto.setRabiesIggSerumAntibodyDetails(exportDto.getRabiesIggSerumAntibodyDetails());
					caseExportDetailedDto.setRabiesIgaSerumAntibodyDetails(exportDto.getRabiesIgaSerumAntibodyDetails());
					caseExportDetailedDto.setRabiesIncubationTimeDetails(exportDto.getRabiesIncubationTimeDetails());
					caseExportDetailedDto.setRabiesIndirectFluorescentAntibodyDetails(exportDto.getRabiesIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRabiesDirectFluorescentAntibodyDetails(exportDto.getRabiesDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRabiesMicroscopyDetails(exportDto.getRabiesMicroscopyDetails());
					caseExportDetailedDto.setRabiesNeutralizingAntibodiesDetails(exportDto.getRabiesNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setRabiesPcrRtPcrDetails(exportDto.getRabiesPcrRtPcrDetails());
					caseExportDetailedDto.setRabiesGramStainDetails(exportDto.getRabiesGramStainDetails());
					caseExportDetailedDto.setRabiesLatexAgglutinationDetails(exportDto.getRabiesLatexAgglutinationDetails());
					caseExportDetailedDto.setRabiesCqValueDetectionDetails(exportDto.getRabiesCqValueDetectionDetails());
					caseExportDetailedDto.setRabiesSequencingDetails(exportDto.getRabiesSequencingDetails());
					caseExportDetailedDto.setRabiesDnaMicroarrayDetails(exportDto.getRabiesDnaMicroarrayDetails());
					caseExportDetailedDto.setRabiesOtherDetails(exportDto.getRabiesOtherDetails());
					caseExportDetailedDto.setAnthraxAntibodyDetection(exportDto.getAnthraxAntibodyDetection());
					caseExportDetailedDto.setAnthraxAntigenDetection(exportDto.getAnthraxAntigenDetection());
					caseExportDetailedDto.setAnthraxRapidTest(exportDto.getAnthraxRapidTest());
					caseExportDetailedDto.setAnthraxCulture(exportDto.getAnthraxCulture());
					caseExportDetailedDto.setAnthraxHistopathology(exportDto.getAnthraxHistopathology());
					caseExportDetailedDto.setAnthraxIsolation(exportDto.getAnthraxIsolation());
					caseExportDetailedDto.setAnthraxIgmSerumAntibody(exportDto.getAnthraxIgmSerumAntibody());
					caseExportDetailedDto.setAnthraxIggSerumAntibody(exportDto.getAnthraxIggSerumAntibody());
					caseExportDetailedDto.setAnthraxIgaSerumAntibody(exportDto.getAnthraxIgaSerumAntibody());
					caseExportDetailedDto.setAnthraxIncubationTime(exportDto.getAnthraxIncubationTime());
					caseExportDetailedDto.setAnthraxIndirectFluorescentAntibody(exportDto.getAnthraxIndirectFluorescentAntibody());
					caseExportDetailedDto.setAnthraxDirectFluorescentAntibody(exportDto.getAnthraxDirectFluorescentAntibody());
					caseExportDetailedDto.setAnthraxMicroscopy(exportDto.getAnthraxMicroscopy());
					caseExportDetailedDto.setAnthraxNeutralizingAntibodies(exportDto.getAnthraxNeutralizingAntibodies());
					caseExportDetailedDto.setAnthraxPcrRtPcr(exportDto.getAnthraxPcrRtPcr());
					caseExportDetailedDto.setAnthraxGramStain(exportDto.getAnthraxGramStain());
					caseExportDetailedDto.setAnthraxLatexAgglutination(exportDto.getAnthraxLatexAgglutination());
					caseExportDetailedDto.setAnthraxCqValueDetection(exportDto.getAnthraxCqValueDetection());
					caseExportDetailedDto.setAnthraxSequencing(exportDto.getAnthraxSequencing());
					caseExportDetailedDto.setAnthraxDnaMicroarray(exportDto.getAnthraxDnaMicroarray());
					caseExportDetailedDto.setAnthraxOther(exportDto.getAnthraxOther());
					caseExportDetailedDto.setAnthraxAntibodyDetectionDetails(exportDto.getAnthraxAntibodyDetectionDetails());
					caseExportDetailedDto.setAnthraxAntigenDetectionDetails(exportDto.getAnthraxAntigenDetectionDetails());
					caseExportDetailedDto.setAnthraxRapidTestDetails(exportDto.getAnthraxRapidTestDetails());
					caseExportDetailedDto.setAnthraxCultureDetails(exportDto.getAnthraxCultureDetails());
					caseExportDetailedDto.setAnthraxHistopathologyDetails(exportDto.getAnthraxHistopathologyDetails());
					caseExportDetailedDto.setAnthraxIsolationDetails(exportDto.getAnthraxIsolationDetails());
					caseExportDetailedDto.setAnthraxIgmSerumAntibodyDetails(exportDto.getAnthraxIgmSerumAntibodyDetails());
					caseExportDetailedDto.setAnthraxIggSerumAntibodyDetails(exportDto.getAnthraxIggSerumAntibodyDetails());
					caseExportDetailedDto.setAnthraxIgaSerumAntibodyDetails(exportDto.getAnthraxIgaSerumAntibodyDetails());
					caseExportDetailedDto.setAnthraxIncubationTimeDetails(exportDto.getAnthraxIncubationTimeDetails());
					caseExportDetailedDto.setAnthraxIndirectFluorescentAntibodyDetails(exportDto.getAnthraxIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAnthraxDirectFluorescentAntibodyDetails(exportDto.getAnthraxDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAnthraxMicroscopyDetails(exportDto.getAnthraxMicroscopyDetails());
					caseExportDetailedDto.setAnthraxNeutralizingAntibodiesDetails(exportDto.getAnthraxNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setAnthraxPcrRtPcrDetails(exportDto.getAnthraxPcrRtPcrDetails());
					caseExportDetailedDto.setAnthraxGramStainDetails(exportDto.getAnthraxGramStainDetails());
					caseExportDetailedDto.setAnthraxLatexAgglutinationDetails(exportDto.getAnthraxLatexAgglutinationDetails());
					caseExportDetailedDto.setAnthraxCqValueDetectionDetails(exportDto.getAnthraxCqValueDetectionDetails());
					caseExportDetailedDto.setAnthraxSequencingDetails(exportDto.getAnthraxSequencingDetails());
					caseExportDetailedDto.setAnthraxDnaMicroarrayDetails(exportDto.getAnthraxDnaMicroarrayDetails());
					caseExportDetailedDto.setAnthraxOtherDetails(exportDto.getAnthraxOtherDetails());
					caseExportDetailedDto.setCoronavirusAntibodyDetection(exportDto.getCoronavirusAntibodyDetection());
					caseExportDetailedDto.setCoronavirusAntigenDetection(exportDto.getCoronavirusAntigenDetection());
					caseExportDetailedDto.setCoronavirusRapidTest(exportDto.getCoronavirusRapidTest());
					caseExportDetailedDto.setCoronavirusCulture(exportDto.getCoronavirusCulture());
					caseExportDetailedDto.setCoronavirusHistopathology(exportDto.getCoronavirusHistopathology());
					caseExportDetailedDto.setCoronavirusIsolation(exportDto.getCoronavirusIsolation());
					caseExportDetailedDto.setCoronavirusIgmSerumAntibody(exportDto.getCoronavirusIgmSerumAntibody());
					caseExportDetailedDto.setCoronavirusIggSerumAntibody(exportDto.getCoronavirusIggSerumAntibody());
					caseExportDetailedDto.setCoronavirusIgaSerumAntibody(exportDto.getCoronavirusIgaSerumAntibody());
					caseExportDetailedDto.setCoronavirusIncubationTime(exportDto.getCoronavirusIncubationTime());
					caseExportDetailedDto.setCoronavirusIndirectFluorescentAntibody(exportDto.getCoronavirusIndirectFluorescentAntibody());
					caseExportDetailedDto.setCoronavirusDirectFluorescentAntibody(exportDto.getCoronavirusDirectFluorescentAntibody());
					caseExportDetailedDto.setCoronavirusMicroscopy(exportDto.getCoronavirusMicroscopy());
					caseExportDetailedDto.setCoronavirusNeutralizingAntibodies(exportDto.getCoronavirusNeutralizingAntibodies());
					caseExportDetailedDto.setCoronavirusPcrRtPcr(exportDto.getCoronavirusPcrRtPcr());
					caseExportDetailedDto.setCoronavirusGramStain(exportDto.getCoronavirusGramStain());
					caseExportDetailedDto.setCoronavirusLatexAgglutination(exportDto.getCoronavirusLatexAgglutination());
					caseExportDetailedDto.setCoronavirusCqValueDetection(exportDto.getCoronavirusCqValueDetection());
					caseExportDetailedDto.setCoronavirusSequencing(exportDto.getCoronavirusSequencing());
					caseExportDetailedDto.setCoronavirusDnaMicroarray(exportDto.getCoronavirusDnaMicroarray());
					caseExportDetailedDto.setCoronavirusOther(exportDto.getCoronavirusOther());
					caseExportDetailedDto.setCoronavirusAntibodyDetectionDetails(exportDto.getCoronavirusAntibodyDetectionDetails());
					caseExportDetailedDto.setCoronavirusAntigenDetectionDetails(exportDto.getCoronavirusAntigenDetectionDetails());
					caseExportDetailedDto.setCoronavirusRapidTestDetails(exportDto.getCoronavirusRapidTestDetails());
					caseExportDetailedDto.setCoronavirusCultureDetails(exportDto.getCoronavirusCultureDetails());
					caseExportDetailedDto.setCoronavirusHistopathologyDetails(exportDto.getCoronavirusHistopathologyDetails());
					caseExportDetailedDto.setCoronavirusIsolationDetails(exportDto.getCoronavirusIsolationDetails());
					caseExportDetailedDto.setCoronavirusIgmSerumAntibodyDetails(exportDto.getCoronavirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setCoronavirusIggSerumAntibodyDetails(exportDto.getCoronavirusIggSerumAntibodyDetails());
					caseExportDetailedDto.setCoronavirusIgaSerumAntibodyDetails(exportDto.getCoronavirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setCoronavirusIncubationTimeDetails(exportDto.getCoronavirusIncubationTimeDetails());
					caseExportDetailedDto.setCoronavirusIndirectFluorescentAntibodyDetails(exportDto.getCoronavirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCoronavirusDirectFluorescentAntibodyDetails(exportDto.getCoronavirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setCoronavirusMicroscopyDetails(exportDto.getCoronavirusMicroscopyDetails());
					caseExportDetailedDto.setCoronavirusNeutralizingAntibodiesDetails(exportDto.getCoronavirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setCoronavirusPcrRtPcrDetails(exportDto.getCoronavirusPcrRtPcrDetails());
					caseExportDetailedDto.setCoronavirusGramStainDetails(exportDto.getCoronavirusGramStainDetails());
					caseExportDetailedDto.setCoronavirusLatexAgglutinationDetails(exportDto.getCoronavirusLatexAgglutinationDetails());
					caseExportDetailedDto.setCoronavirusCqValueDetectionDetails(exportDto.getCoronavirusCqValueDetectionDetails());
					caseExportDetailedDto.setCoronavirusSequencingDetails(exportDto.getCoronavirusSequencingDetails());
					caseExportDetailedDto.setCoronavirusDnaMicroarrayDetails(exportDto.getCoronavirusDnaMicroarrayDetails());
					caseExportDetailedDto.setCoronavirusOtherDetails(exportDto.getCoronavirusOtherDetails());
					caseExportDetailedDto.setPneumoniaAntibodyDetection(exportDto.getPneumoniaAntibodyDetection());
					caseExportDetailedDto.setPneumoniaAntigenDetection(exportDto.getPneumoniaAntigenDetection());
					caseExportDetailedDto.setPneumoniaRapidTest(exportDto.getPneumoniaRapidTest());
					caseExportDetailedDto.setPneumoniaCulture(exportDto.getPneumoniaCulture());
					caseExportDetailedDto.setPneumoniaHistopathology(exportDto.getPneumoniaHistopathology());
					caseExportDetailedDto.setPneumoniaIsolation(exportDto.getPneumoniaIsolation());
					caseExportDetailedDto.setPneumoniaIgmSerumAntibody(exportDto.getPneumoniaIgmSerumAntibody());
					caseExportDetailedDto.setPneumoniaIggSerumAntibody(exportDto.getPneumoniaIggSerumAntibody());
					caseExportDetailedDto.setPneumoniaIgaSerumAntibody(exportDto.getPneumoniaIgaSerumAntibody());
					caseExportDetailedDto.setPneumoniaIncubationTime(exportDto.getPneumoniaIncubationTime());
					caseExportDetailedDto.setPneumoniaIndirectFluorescentAntibody(exportDto.getPneumoniaIndirectFluorescentAntibody());
					caseExportDetailedDto.setPneumoniaDirectFluorescentAntibody(exportDto.getPneumoniaDirectFluorescentAntibody());
					caseExportDetailedDto.setPneumoniaMicroscopy(exportDto.getPneumoniaMicroscopy());
					caseExportDetailedDto.setPneumoniaNeutralizingAntibodies(exportDto.getPneumoniaNeutralizingAntibodies());
					caseExportDetailedDto.setPneumoniaPcrRtPcr(exportDto.getPneumoniaPcrRtPcr());
					caseExportDetailedDto.setPneumoniaGramStain(exportDto.getPneumoniaGramStain());
					caseExportDetailedDto.setPneumoniaLatexAgglutination(exportDto.getPneumoniaLatexAgglutination());
					caseExportDetailedDto.setPneumoniaCqValueDetection(exportDto.getPneumoniaCqValueDetection());
					caseExportDetailedDto.setPneumoniaSequencing(exportDto.getPneumoniaSequencing());
					caseExportDetailedDto.setPneumoniaDnaMicroarray(exportDto.getPneumoniaDnaMicroarray());
					caseExportDetailedDto.setPneumoniaOther(exportDto.getPneumoniaOther());
					caseExportDetailedDto.setPneumoniaAntibodyDetectionDetails(exportDto.getPneumoniaAntibodyDetectionDetails());
					caseExportDetailedDto.setPneumoniaAntigenDetectionDetails(exportDto.getPneumoniaAntigenDetectionDetails());
					caseExportDetailedDto.setPneumoniaRapidTestDetails(exportDto.getPneumoniaRapidTestDetails());
					caseExportDetailedDto.setPneumoniaCultureDetails(exportDto.getPneumoniaCultureDetails());
					caseExportDetailedDto.setPneumoniaHistopathologyDetails(exportDto.getPneumoniaHistopathologyDetails());
					caseExportDetailedDto.setPneumoniaIsolationDetails(exportDto.getPneumoniaIsolationDetails());
					caseExportDetailedDto.setPneumoniaIgmSerumAntibodyDetails(exportDto.getPneumoniaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPneumoniaIggSerumAntibodyDetails(exportDto.getPneumoniaIggSerumAntibodyDetails());
					caseExportDetailedDto.setPneumoniaIgaSerumAntibodyDetails(exportDto.getPneumoniaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPneumoniaIncubationTimeDetails(exportDto.getPneumoniaIncubationTimeDetails());
					caseExportDetailedDto.setPneumoniaIndirectFluorescentAntibodyDetails(exportDto.getPneumoniaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPneumoniaDirectFluorescentAntibodyDetails(exportDto.getPneumoniaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPneumoniaMicroscopyDetails(exportDto.getPneumoniaMicroscopyDetails());
					caseExportDetailedDto.setPneumoniaNeutralizingAntibodiesDetails(exportDto.getPneumoniaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPneumoniaPcrRtPcrDetails(exportDto.getPneumoniaPcrRtPcrDetails());
					caseExportDetailedDto.setPneumoniaGramStainDetails(exportDto.getPneumoniaGramStainDetails());
					caseExportDetailedDto.setPneumoniaLatexAgglutinationDetails(exportDto.getPneumoniaLatexAgglutinationDetails());
					caseExportDetailedDto.setPneumoniaCqValueDetectionDetails(exportDto.getPneumoniaCqValueDetectionDetails());
					caseExportDetailedDto.setPneumoniaSequencingDetails(exportDto.getPneumoniaSequencingDetails());
					caseExportDetailedDto.setPneumoniaDnaMicroarrayDetails(exportDto.getPneumoniaDnaMicroarrayDetails());
					caseExportDetailedDto.setPneumoniaOtherDetails(exportDto.getPneumoniaOtherDetails());
					caseExportDetailedDto.setMalariaAntibodyDetection(exportDto.getMalariaAntibodyDetection());
					caseExportDetailedDto.setMalariaAntigenDetection(exportDto.getMalariaAntigenDetection());
					caseExportDetailedDto.setMalariaRapidTest(exportDto.getMalariaRapidTest());
					caseExportDetailedDto.setMalariaCulture(exportDto.getMalariaCulture());
					caseExportDetailedDto.setMalariaHistopathology(exportDto.getMalariaHistopathology());
					caseExportDetailedDto.setMalariaIsolation(exportDto.getMalariaIsolation());
					caseExportDetailedDto.setMalariaIgmSerumAntibody(exportDto.getMalariaIgmSerumAntibody());
					caseExportDetailedDto.setMalariaIggSerumAntibody(exportDto.getMalariaIggSerumAntibody());
					caseExportDetailedDto.setMalariaIgaSerumAntibody(exportDto.getMalariaIgaSerumAntibody());
					caseExportDetailedDto.setMalariaIncubationTime(exportDto.getMalariaIncubationTime());
					caseExportDetailedDto.setMalariaIndirectFluorescentAntibody(exportDto.getMalariaIndirectFluorescentAntibody());
					caseExportDetailedDto.setMalariaDirectFluorescentAntibody(exportDto.getMalariaDirectFluorescentAntibody());
					caseExportDetailedDto.setMalariaMicroscopy(exportDto.getMalariaMicroscopy());
					caseExportDetailedDto.setMalariaNeutralizingAntibodies(exportDto.getMalariaNeutralizingAntibodies());
					caseExportDetailedDto.setMalariaPcrRtPcr(exportDto.getMalariaPcrRtPcr());
					caseExportDetailedDto.setMalariaGramStain(exportDto.getMalariaGramStain());
					caseExportDetailedDto.setMalariaLatexAgglutination(exportDto.getMalariaLatexAgglutination());
					caseExportDetailedDto.setMalariaCqValueDetection(exportDto.getMalariaCqValueDetection());
					caseExportDetailedDto.setMalariaSequencing(exportDto.getMalariaSequencing());
					caseExportDetailedDto.setMalariaDnaMicroarray(exportDto.getMalariaDnaMicroarray());
					caseExportDetailedDto.setMalariaOther(exportDto.getMalariaOther());
					caseExportDetailedDto.setMalariaAntibodyDetectionDetails(exportDto.getMalariaAntibodyDetectionDetails());
					caseExportDetailedDto.setMalariaAntigenDetectionDetails(exportDto.getMalariaAntigenDetectionDetails());
					caseExportDetailedDto.setMalariaRapidTestDetails(exportDto.getMalariaRapidTestDetails());
					caseExportDetailedDto.setMalariaCultureDetails(exportDto.getMalariaCultureDetails());
					caseExportDetailedDto.setMalariaHistopathologyDetails(exportDto.getMalariaHistopathologyDetails());
					caseExportDetailedDto.setMalariaIsolationDetails(exportDto.getMalariaIsolationDetails());
					caseExportDetailedDto.setMalariaIgmSerumAntibodyDetails(exportDto.getMalariaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setMalariaIggSerumAntibodyDetails(exportDto.getMalariaIggSerumAntibodyDetails());
					caseExportDetailedDto.setMalariaIgaSerumAntibodyDetails(exportDto.getMalariaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setMalariaIncubationTimeDetails(exportDto.getMalariaIncubationTimeDetails());
					caseExportDetailedDto.setMalariaIndirectFluorescentAntibodyDetails(exportDto.getMalariaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMalariaDirectFluorescentAntibodyDetails(exportDto.getMalariaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMalariaMicroscopyDetails(exportDto.getMalariaMicroscopyDetails());
					caseExportDetailedDto.setMalariaNeutralizingAntibodiesDetails(exportDto.getMalariaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setMalariaPcrRtPcrDetails(exportDto.getMalariaPcrRtPcrDetails());
					caseExportDetailedDto.setMalariaGramStainDetails(exportDto.getMalariaGramStainDetails());
					caseExportDetailedDto.setMalariaLatexAgglutinationDetails(exportDto.getMalariaLatexAgglutinationDetails());
					caseExportDetailedDto.setMalariaCqValueDetectionDetails(exportDto.getMalariaCqValueDetectionDetails());
					caseExportDetailedDto.setMalariaSequencingDetails(exportDto.getMalariaSequencingDetails());
					caseExportDetailedDto.setMalariaDnaMicroarrayDetails(exportDto.getMalariaDnaMicroarrayDetails());
					caseExportDetailedDto.setMalariaOtherDetails(exportDto.getMalariaOtherDetails());
					caseExportDetailedDto.setTyphoidFeverAntibodyDetection(exportDto.getTyphoidFeverAntibodyDetection());
					caseExportDetailedDto.setTyphoidFeverAntigenDetection(exportDto.getTyphoidFeverAntigenDetection());
					caseExportDetailedDto.setTyphoidFeverRapidTest(exportDto.getTyphoidFeverRapidTest());
					caseExportDetailedDto.setTyphoidFeverCulture(exportDto.getTyphoidFeverCulture());
					caseExportDetailedDto.setTyphoidFeverHistopathology(exportDto.getTyphoidFeverHistopathology());
					caseExportDetailedDto.setTyphoidFeverIsolation(exportDto.getTyphoidFeverIsolation());
					caseExportDetailedDto.setTyphoidFeverIgmSerumAntibody(exportDto.getTyphoidFeverIgmSerumAntibody());
					caseExportDetailedDto.setTyphoidFeverIggSerumAntibody(exportDto.getTyphoidFeverIggSerumAntibody());
					caseExportDetailedDto.setTyphoidFeverIgaSerumAntibody(exportDto.getTyphoidFeverIgaSerumAntibody());
					caseExportDetailedDto.setTyphoidFeverIncubationTime(exportDto.getTyphoidFeverIncubationTime());
					caseExportDetailedDto.setTyphoidFeverIndirectFluorescentAntibody(exportDto.getTyphoidFeverIndirectFluorescentAntibody());
					caseExportDetailedDto.setTyphoidFeverDirectFluorescentAntibody(exportDto.getTyphoidFeverDirectFluorescentAntibody());
					caseExportDetailedDto.setTyphoidFeverMicroscopy(exportDto.getTyphoidFeverMicroscopy());
					caseExportDetailedDto.setTyphoidFeverNeutralizingAntibodies(exportDto.getTyphoidFeverNeutralizingAntibodies());
					caseExportDetailedDto.setTyphoidFeverPcrRtPcr(exportDto.getTyphoidFeverPcrRtPcr());
					caseExportDetailedDto.setTyphoidFeverGramStain(exportDto.getTyphoidFeverGramStain());
					caseExportDetailedDto.setTyphoidFeverLatexAgglutination(exportDto.getTyphoidFeverLatexAgglutination());
					caseExportDetailedDto.setTyphoidFeverCqValueDetection(exportDto.getTyphoidFeverCqValueDetection());
					caseExportDetailedDto.setTyphoidFeverSequencing(exportDto.getTyphoidFeverSequencing());
					caseExportDetailedDto.setTyphoidFeverDnaMicroarray(exportDto.getTyphoidFeverDnaMicroarray());
					caseExportDetailedDto.setTyphoidFeverOther(exportDto.getTyphoidFeverOther());
					caseExportDetailedDto.setTyphoidFeverAntibodyDetectionDetails(exportDto.getTyphoidFeverAntibodyDetectionDetails());
					caseExportDetailedDto.setTyphoidFeverAntigenDetectionDetails(exportDto.getTyphoidFeverAntigenDetectionDetails());
					caseExportDetailedDto.setTyphoidFeverRapidTestDetails(exportDto.getTyphoidFeverRapidTestDetails());
					caseExportDetailedDto.setTyphoidFeverCultureDetails(exportDto.getTyphoidFeverCultureDetails());
					caseExportDetailedDto.setTyphoidFeverHistopathologyDetails(exportDto.getTyphoidFeverHistopathologyDetails());
					caseExportDetailedDto.setTyphoidFeverIsolationDetails(exportDto.getTyphoidFeverIsolationDetails());
					caseExportDetailedDto.setTyphoidFeverIgmSerumAntibodyDetails(exportDto.getTyphoidFeverIgmSerumAntibodyDetails());
					caseExportDetailedDto.setTyphoidFeverIggSerumAntibodyDetails(exportDto.getTyphoidFeverIggSerumAntibodyDetails());
					caseExportDetailedDto.setTyphoidFeverIgaSerumAntibodyDetails(exportDto.getTyphoidFeverIgaSerumAntibodyDetails());
					caseExportDetailedDto.setTyphoidFeverIncubationTimeDetails(exportDto.getTyphoidFeverIncubationTimeDetails());
					caseExportDetailedDto.setTyphoidFeverIndirectFluorescentAntibodyDetails(exportDto.getTyphoidFeverIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTyphoidFeverDirectFluorescentAntibodyDetails(exportDto.getTyphoidFeverDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTyphoidFeverMicroscopyDetails(exportDto.getTyphoidFeverMicroscopyDetails());
					caseExportDetailedDto.setTyphoidFeverNeutralizingAntibodiesDetails(exportDto.getTyphoidFeverNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setTyphoidFeverPcrRtPcrDetails(exportDto.getTyphoidFeverPcrRtPcrDetails());
					caseExportDetailedDto.setTyphoidFeverGramStainDetails(exportDto.getTyphoidFeverGramStainDetails());
					caseExportDetailedDto.setTyphoidFeverLatexAgglutinationDetails(exportDto.getTyphoidFeverLatexAgglutinationDetails());
					caseExportDetailedDto.setTyphoidFeverCqValueDetectionDetails(exportDto.getTyphoidFeverCqValueDetectionDetails());
					caseExportDetailedDto.setTyphoidFeverSequencingDetails(exportDto.getTyphoidFeverSequencingDetails());
					caseExportDetailedDto.setTyphoidFeverDnaMicroarrayDetails(exportDto.getTyphoidFeverDnaMicroarrayDetails());
					caseExportDetailedDto.setTyphoidFeverOtherDetails(exportDto.getTyphoidFeverOtherDetails());
					caseExportDetailedDto.setAcuteViralHepatitisAntibodyDetection(exportDto.getAcuteViralHepatitisAntibodyDetection());
					caseExportDetailedDto.setAcuteViralHepatitisAntigenDetection(exportDto.getAcuteViralHepatitisAntigenDetection());
					caseExportDetailedDto.setAcuteViralHepatitisRapidTest(exportDto.getAcuteViralHepatitisRapidTest());
					caseExportDetailedDto.setAcuteViralHepatitisCulture(exportDto.getAcuteViralHepatitisCulture());
					caseExportDetailedDto.setAcuteViralHepatitisHistopathology(exportDto.getAcuteViralHepatitisHistopathology());
					caseExportDetailedDto.setAcuteViralHepatitisIsolation(exportDto.getAcuteViralHepatitisIsolation());
					caseExportDetailedDto.setAcuteViralHepatitisIgmSerumAntibody(exportDto.getAcuteViralHepatitisIgmSerumAntibody());
					caseExportDetailedDto.setAcuteViralHepatitisIggSerumAntibody(exportDto.getAcuteViralHepatitisIggSerumAntibody());
					caseExportDetailedDto.setAcuteViralHepatitisIgaSerumAntibody(exportDto.getAcuteViralHepatitisIgaSerumAntibody());
					caseExportDetailedDto.setAcuteViralHepatitisIncubationTime(exportDto.getAcuteViralHepatitisIncubationTime());
					caseExportDetailedDto.setAcuteViralHepatitisIndirectFluorescentAntibody(exportDto.getAcuteViralHepatitisIndirectFluorescentAntibody());
					caseExportDetailedDto.setAcuteViralHepatitisDirectFluorescentAntibody(exportDto.getAcuteViralHepatitisDirectFluorescentAntibody());
					caseExportDetailedDto.setAcuteViralHepatitisMicroscopy(exportDto.getAcuteViralHepatitisMicroscopy());
					caseExportDetailedDto.setAcuteViralHepatitisNeutralizingAntibodies(exportDto.getAcuteViralHepatitisNeutralizingAntibodies());
					caseExportDetailedDto.setAcuteViralHepatitisPcrRtPcr(exportDto.getAcuteViralHepatitisPcrRtPcr());
					caseExportDetailedDto.setAcuteViralHepatitisGramStain(exportDto.getAcuteViralHepatitisGramStain());
					caseExportDetailedDto.setAcuteViralHepatitisLatexAgglutination(exportDto.getAcuteViralHepatitisLatexAgglutination());
					caseExportDetailedDto.setAcuteViralHepatitisCqValueDetection(exportDto.getAcuteViralHepatitisCqValueDetection());
					caseExportDetailedDto.setAcuteViralHepatitisSequencing(exportDto.getAcuteViralHepatitisSequencing());
					caseExportDetailedDto.setAcuteViralHepatitisDnaMicroarray(exportDto.getAcuteViralHepatitisDnaMicroarray());
					caseExportDetailedDto.setAcuteViralHepatitisOther(exportDto.getAcuteViralHepatitisOther());
					caseExportDetailedDto.setAcuteViralHepatitisAntibodyDetectionDetails(exportDto.getAcuteViralHepatitisAntibodyDetectionDetails());
					caseExportDetailedDto.setAcuteViralHepatitisAntigenDetectionDetails(exportDto.getAcuteViralHepatitisAntigenDetectionDetails());
					caseExportDetailedDto.setAcuteViralHepatitisRapidTestDetails(exportDto.getAcuteViralHepatitisRapidTestDetails());
					caseExportDetailedDto.setAcuteViralHepatitisCultureDetails(exportDto.getAcuteViralHepatitisCultureDetails());
					caseExportDetailedDto.setAcuteViralHepatitisHistopathologyDetails(exportDto.getAcuteViralHepatitisHistopathologyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIsolationDetails(exportDto.getAcuteViralHepatitisIsolationDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIgmSerumAntibodyDetails(exportDto.getAcuteViralHepatitisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIggSerumAntibodyDetails(exportDto.getAcuteViralHepatitisIggSerumAntibodyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIgaSerumAntibodyDetails(exportDto.getAcuteViralHepatitisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIncubationTimeDetails(exportDto.getAcuteViralHepatitisIncubationTimeDetails());
					caseExportDetailedDto.setAcuteViralHepatitisIndirectFluorescentAntibodyDetails(exportDto.getAcuteViralHepatitisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisDirectFluorescentAntibodyDetails(exportDto.getAcuteViralHepatitisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisMicroscopyDetails(exportDto.getAcuteViralHepatitisMicroscopyDetails());
					caseExportDetailedDto.setAcuteViralHepatitisNeutralizingAntibodiesDetails(exportDto.getAcuteViralHepatitisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setAcuteViralHepatitisPcrRtPcrDetails(exportDto.getAcuteViralHepatitisPcrRtPcrDetails());
					caseExportDetailedDto.setAcuteViralHepatitisGramStainDetails(exportDto.getAcuteViralHepatitisGramStainDetails());
					caseExportDetailedDto.setAcuteViralHepatitisLatexAgglutinationDetails(exportDto.getAcuteViralHepatitisLatexAgglutinationDetails());
					caseExportDetailedDto.setAcuteViralHepatitisCqValueDetectionDetails(exportDto.getAcuteViralHepatitisCqValueDetectionDetails());
					caseExportDetailedDto.setAcuteViralHepatitisSequencingDetails(exportDto.getAcuteViralHepatitisSequencingDetails());
					caseExportDetailedDto.setAcuteViralHepatitisDnaMicroarrayDetails(exportDto.getAcuteViralHepatitisDnaMicroarrayDetails());
					caseExportDetailedDto.setAcuteViralHepatitisOtherDetails(exportDto.getAcuteViralHepatitisOtherDetails());
					caseExportDetailedDto.setNonNeonatalTetanusAntibodyDetection(exportDto.getNonNeonatalTetanusAntibodyDetection());
					caseExportDetailedDto.setNonNeonatalTetanusAntigenDetection(exportDto.getNonNeonatalTetanusAntigenDetection());
					caseExportDetailedDto.setNonNeonatalTetanusRapidTest(exportDto.getNonNeonatalTetanusRapidTest());
					caseExportDetailedDto.setNonNeonatalTetanusCulture(exportDto.getNonNeonatalTetanusCulture());
					caseExportDetailedDto.setNonNeonatalTetanusHistopathology(exportDto.getNonNeonatalTetanusHistopathology());
					caseExportDetailedDto.setNonNeonatalTetanusIsolation(exportDto.getNonNeonatalTetanusIsolation());
					caseExportDetailedDto.setNonNeonatalTetanusIgmSerumAntibody(exportDto.getNonNeonatalTetanusIgmSerumAntibody());
					caseExportDetailedDto.setNonNeonatalTetanusIggSerumAntibody(exportDto.getNonNeonatalTetanusIggSerumAntibody());
					caseExportDetailedDto.setNonNeonatalTetanusIgaSerumAntibody(exportDto.getNonNeonatalTetanusIgaSerumAntibody());
					caseExportDetailedDto.setNonNeonatalTetanusIncubationTime(exportDto.getNonNeonatalTetanusIncubationTime());
					caseExportDetailedDto.setNonNeonatalTetanusIndirectFluorescentAntibody(exportDto.getNonNeonatalTetanusIndirectFluorescentAntibody());
					caseExportDetailedDto.setNonNeonatalTetanusDirectFluorescentAntibody(exportDto.getNonNeonatalTetanusDirectFluorescentAntibody());
					caseExportDetailedDto.setNonNeonatalTetanusMicroscopy(exportDto.getNonNeonatalTetanusMicroscopy());
					caseExportDetailedDto.setNonNeonatalTetanusNeutralizingAntibodies(exportDto.getNonNeonatalTetanusNeutralizingAntibodies());
					caseExportDetailedDto.setNonNeonatalTetanusPcrRtPcr(exportDto.getNonNeonatalTetanusPcrRtPcr());
					caseExportDetailedDto.setNonNeonatalTetanusGramStain(exportDto.getNonNeonatalTetanusGramStain());
					caseExportDetailedDto.setNonNeonatalTetanusLatexAgglutination(exportDto.getNonNeonatalTetanusLatexAgglutination());
					caseExportDetailedDto.setNonNeonatalTetanusCqValueDetection(exportDto.getNonNeonatalTetanusCqValueDetection());
					caseExportDetailedDto.setNonNeonatalTetanusSequencing(exportDto.getNonNeonatalTetanusSequencing());
					caseExportDetailedDto.setNonNeonatalTetanusDnaMicroarray(exportDto.getNonNeonatalTetanusDnaMicroarray());
					caseExportDetailedDto.setNonNeonatalTetanusOther(exportDto.getNonNeonatalTetanusOther());
					caseExportDetailedDto.setNonNeonatalTetanusAntibodyDetectionDetails(exportDto.getNonNeonatalTetanusAntibodyDetectionDetails());
					caseExportDetailedDto.setNonNeonatalTetanusAntigenDetectionDetails(exportDto.getNonNeonatalTetanusAntigenDetectionDetails());
					caseExportDetailedDto.setNonNeonatalTetanusRapidTestDetails(exportDto.getNonNeonatalTetanusRapidTestDetails());
					caseExportDetailedDto.setNonNeonatalTetanusCultureDetails(exportDto.getNonNeonatalTetanusCultureDetails());
					caseExportDetailedDto.setNonNeonatalTetanusHistopathologyDetails(exportDto.getNonNeonatalTetanusHistopathologyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIsolationDetails(exportDto.getNonNeonatalTetanusIsolationDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIgmSerumAntibodyDetails(exportDto.getNonNeonatalTetanusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIggSerumAntibodyDetails(exportDto.getNonNeonatalTetanusIggSerumAntibodyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIgaSerumAntibodyDetails(exportDto.getNonNeonatalTetanusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIncubationTimeDetails(exportDto.getNonNeonatalTetanusIncubationTimeDetails());
					caseExportDetailedDto.setNonNeonatalTetanusIndirectFluorescentAntibodyDetails(exportDto.getNonNeonatalTetanusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusDirectFluorescentAntibodyDetails(exportDto.getNonNeonatalTetanusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusMicroscopyDetails(exportDto.getNonNeonatalTetanusMicroscopyDetails());
					caseExportDetailedDto.setNonNeonatalTetanusNeutralizingAntibodiesDetails(exportDto.getNonNeonatalTetanusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setNonNeonatalTetanusPcrRtPcrDetails(exportDto.getNonNeonatalTetanusPcrRtPcrDetails());
					caseExportDetailedDto.setNonNeonatalTetanusGramStainDetails(exportDto.getNonNeonatalTetanusGramStainDetails());
					caseExportDetailedDto.setNonNeonatalTetanusLatexAgglutinationDetails(exportDto.getNonNeonatalTetanusLatexAgglutinationDetails());
					caseExportDetailedDto.setNonNeonatalTetanusCqValueDetectionDetails(exportDto.getNonNeonatalTetanusCqValueDetectionDetails());
					caseExportDetailedDto.setNonNeonatalTetanusSequencingDetails(exportDto.getNonNeonatalTetanusSequencingDetails());
					caseExportDetailedDto.setNonNeonatalTetanusDnaMicroarrayDetails(exportDto.getNonNeonatalTetanusDnaMicroarrayDetails());
					caseExportDetailedDto.setNonNeonatalTetanusOtherDetails(exportDto.getNonNeonatalTetanusOtherDetails());
					caseExportDetailedDto.setHivAntibodyDetection(exportDto.getHivAntibodyDetection());
					caseExportDetailedDto.setHivAntigenDetection(exportDto.getHivAntigenDetection());
					caseExportDetailedDto.setHivRapidTest(exportDto.getHivRapidTest());
					caseExportDetailedDto.setHivCulture(exportDto.getHivCulture());
					caseExportDetailedDto.setHivHistopathology(exportDto.getHivHistopathology());
					caseExportDetailedDto.setHivIsolation(exportDto.getHivIsolation());
					caseExportDetailedDto.setHivIgmSerumAntibody(exportDto.getHivIgmSerumAntibody());
					caseExportDetailedDto.setHivIggSerumAntibody(exportDto.getHivIggSerumAntibody());
					caseExportDetailedDto.setHivIgaSerumAntibody(exportDto.getHivIgaSerumAntibody());
					caseExportDetailedDto.setHivIncubationTime(exportDto.getHivIncubationTime());
					caseExportDetailedDto.setHivIndirectFluorescentAntibody(exportDto.getHivIndirectFluorescentAntibody());
					caseExportDetailedDto.setHivDirectFluorescentAntibody(exportDto.getHivDirectFluorescentAntibody());
					caseExportDetailedDto.setHivMicroscopy(exportDto.getHivMicroscopy());
					caseExportDetailedDto.setHivNeutralizingAntibodies(exportDto.getHivNeutralizingAntibodies());
					caseExportDetailedDto.setHivPcrRtPcr(exportDto.getHivPcrRtPcr());
					caseExportDetailedDto.setHivGramStain(exportDto.getHivGramStain());
					caseExportDetailedDto.setHivLatexAgglutination(exportDto.getHivLatexAgglutination());
					caseExportDetailedDto.setHivCqValueDetection(exportDto.getHivCqValueDetection());
					caseExportDetailedDto.setHivSequencing(exportDto.getHivSequencing());
					caseExportDetailedDto.setHivDnaMicroarray(exportDto.getHivDnaMicroarray());
					caseExportDetailedDto.setHivOther(exportDto.getHivOther());
					caseExportDetailedDto.setHivAntibodyDetectionDetails(exportDto.getHivAntibodyDetectionDetails());
					caseExportDetailedDto.setHivAntigenDetectionDetails(exportDto.getHivAntigenDetectionDetails());
					caseExportDetailedDto.setHivRapidTestDetails(exportDto.getHivRapidTestDetails());
					caseExportDetailedDto.setHivCultureDetails(exportDto.getHivCultureDetails());
					caseExportDetailedDto.setHivHistopathologyDetails(exportDto.getHivHistopathologyDetails());
					caseExportDetailedDto.setHivIsolationDetails(exportDto.getHivIsolationDetails());
					caseExportDetailedDto.setHivIgmSerumAntibodyDetails(exportDto.getHivIgmSerumAntibodyDetails());
					caseExportDetailedDto.setHivIggSerumAntibodyDetails(exportDto.getHivIggSerumAntibodyDetails());
					caseExportDetailedDto.setHivIgaSerumAntibodyDetails(exportDto.getHivIgaSerumAntibodyDetails());
					caseExportDetailedDto.setHivIncubationTimeDetails(exportDto.getHivIncubationTimeDetails());
					caseExportDetailedDto.setHivIndirectFluorescentAntibodyDetails(exportDto.getHivIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setHivDirectFluorescentAntibodyDetails(exportDto.getHivDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setHivMicroscopyDetails(exportDto.getHivMicroscopyDetails());
					caseExportDetailedDto.setHivNeutralizingAntibodiesDetails(exportDto.getHivNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setHivPcrRtPcrDetails(exportDto.getHivPcrRtPcrDetails());
					caseExportDetailedDto.setHivGramStainDetails(exportDto.getHivGramStainDetails());
					caseExportDetailedDto.setHivLatexAgglutinationDetails(exportDto.getHivLatexAgglutinationDetails());
					caseExportDetailedDto.setHivCqValueDetectionDetails(exportDto.getHivCqValueDetectionDetails());
					caseExportDetailedDto.setHivSequencingDetails(exportDto.getHivSequencingDetails());
					caseExportDetailedDto.setHivDnaMicroarrayDetails(exportDto.getHivDnaMicroarrayDetails());
					caseExportDetailedDto.setHivOtherDetails(exportDto.getHivOtherDetails());
					caseExportDetailedDto.setSchistosomiasisAntibodyDetection(exportDto.getSchistosomiasisAntibodyDetection());
					caseExportDetailedDto.setSchistosomiasisAntigenDetection(exportDto.getSchistosomiasisAntigenDetection());
					caseExportDetailedDto.setSchistosomiasisRapidTest(exportDto.getSchistosomiasisRapidTest());
					caseExportDetailedDto.setSchistosomiasisCulture(exportDto.getSchistosomiasisCulture());
					caseExportDetailedDto.setSchistosomiasisHistopathology(exportDto.getSchistosomiasisHistopathology());
					caseExportDetailedDto.setSchistosomiasisIsolation(exportDto.getSchistosomiasisIsolation());
					caseExportDetailedDto.setSchistosomiasisIgmSerumAntibody(exportDto.getSchistosomiasisIgmSerumAntibody());
					caseExportDetailedDto.setSchistosomiasisIggSerumAntibody(exportDto.getSchistosomiasisIggSerumAntibody());
					caseExportDetailedDto.setSchistosomiasisIgaSerumAntibody(exportDto.getSchistosomiasisIgaSerumAntibody());
					caseExportDetailedDto.setSchistosomiasisIncubationTime(exportDto.getSchistosomiasisIncubationTime());
					caseExportDetailedDto.setSchistosomiasisIndirectFluorescentAntibody(exportDto.getSchistosomiasisIndirectFluorescentAntibody());
					caseExportDetailedDto.setSchistosomiasisDirectFluorescentAntibody(exportDto.getSchistosomiasisDirectFluorescentAntibody());
					caseExportDetailedDto.setSchistosomiasisMicroscopy(exportDto.getSchistosomiasisMicroscopy());
					caseExportDetailedDto.setSchistosomiasisNeutralizingAntibodies(exportDto.getSchistosomiasisNeutralizingAntibodies());
					caseExportDetailedDto.setSchistosomiasisPcrRtPcr(exportDto.getSchistosomiasisPcrRtPcr());
					caseExportDetailedDto.setSchistosomiasisGramStain(exportDto.getSchistosomiasisGramStain());
					caseExportDetailedDto.setSchistosomiasisLatexAgglutination(exportDto.getSchistosomiasisLatexAgglutination());
					caseExportDetailedDto.setSchistosomiasisCqValueDetection(exportDto.getSchistosomiasisCqValueDetection());
					caseExportDetailedDto.setSchistosomiasisSequencing(exportDto.getSchistosomiasisSequencing());
					caseExportDetailedDto.setSchistosomiasisDnaMicroarray(exportDto.getSchistosomiasisDnaMicroarray());
					caseExportDetailedDto.setSchistosomiasisOther(exportDto.getSchistosomiasisOther());
					caseExportDetailedDto.setSchistosomiasisAntibodyDetectionDetails(exportDto.getSchistosomiasisAntibodyDetectionDetails());
					caseExportDetailedDto.setSchistosomiasisAntigenDetectionDetails(exportDto.getSchistosomiasisAntigenDetectionDetails());
					caseExportDetailedDto.setSchistosomiasisRapidTestDetails(exportDto.getSchistosomiasisRapidTestDetails());
					caseExportDetailedDto.setSchistosomiasisCultureDetails(exportDto.getSchistosomiasisCultureDetails());
					caseExportDetailedDto.setSchistosomiasisHistopathologyDetails(exportDto.getSchistosomiasisHistopathologyDetails());
					caseExportDetailedDto.setSchistosomiasisIsolationDetails(exportDto.getSchistosomiasisIsolationDetails());
					caseExportDetailedDto.setSchistosomiasisIgmSerumAntibodyDetails(exportDto.getSchistosomiasisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setSchistosomiasisIggSerumAntibodyDetails(exportDto.getSchistosomiasisIggSerumAntibodyDetails());
					caseExportDetailedDto.setSchistosomiasisIgaSerumAntibodyDetails(exportDto.getSchistosomiasisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setSchistosomiasisIncubationTimeDetails(exportDto.getSchistosomiasisIncubationTimeDetails());
					caseExportDetailedDto.setSchistosomiasisIndirectFluorescentAntibodyDetails(exportDto.getSchistosomiasisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSchistosomiasisDirectFluorescentAntibodyDetails(exportDto.getSchistosomiasisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSchistosomiasisMicroscopyDetails(exportDto.getSchistosomiasisMicroscopyDetails());
					caseExportDetailedDto.setSchistosomiasisNeutralizingAntibodiesDetails(exportDto.getSchistosomiasisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setSchistosomiasisPcrRtPcrDetails(exportDto.getSchistosomiasisPcrRtPcrDetails());
					caseExportDetailedDto.setSchistosomiasisGramStainDetails(exportDto.getSchistosomiasisGramStainDetails());
					caseExportDetailedDto.setSchistosomiasisLatexAgglutinationDetails(exportDto.getSchistosomiasisLatexAgglutinationDetails());
					caseExportDetailedDto.setSchistosomiasisCqValueDetectionDetails(exportDto.getSchistosomiasisCqValueDetectionDetails());
					caseExportDetailedDto.setSchistosomiasisSequencingDetails(exportDto.getSchistosomiasisSequencingDetails());
					caseExportDetailedDto.setSchistosomiasisDnaMicroarrayDetails(exportDto.getSchistosomiasisDnaMicroarrayDetails());
					caseExportDetailedDto.setSchistosomiasisOtherDetails(exportDto.getSchistosomiasisOtherDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsAntibodyDetection(exportDto.getSoilTransmittedHelminthsAntibodyDetection());
					caseExportDetailedDto.setSoilTransmittedHelminthsAntigenDetection(exportDto.getSoilTransmittedHelminthsAntigenDetection());
					caseExportDetailedDto.setSoilTransmittedHelminthsRapidTest(exportDto.getSoilTransmittedHelminthsRapidTest());
					caseExportDetailedDto.setSoilTransmittedHelminthsCulture(exportDto.getSoilTransmittedHelminthsCulture());
					caseExportDetailedDto.setSoilTransmittedHelminthsHistopathology(exportDto.getSoilTransmittedHelminthsHistopathology());
					caseExportDetailedDto.setSoilTransmittedHelminthsIsolation(exportDto.getSoilTransmittedHelminthsIsolation());
					caseExportDetailedDto.setSoilTransmittedHelminthsIgmSerumAntibody(exportDto.getSoilTransmittedHelminthsIgmSerumAntibody());
					caseExportDetailedDto.setSoilTransmittedHelminthsIggSerumAntibody(exportDto.getSoilTransmittedHelminthsIggSerumAntibody());
					caseExportDetailedDto.setSoilTransmittedHelminthsIgaSerumAntibody(exportDto.getSoilTransmittedHelminthsIgaSerumAntibody());
					caseExportDetailedDto.setSoilTransmittedHelminthsIncubationTime(exportDto.getSoilTransmittedHelminthsIncubationTime());
					caseExportDetailedDto.setSoilTransmittedHelminthsIndirectFluorescentAntibody(exportDto.getSoilTransmittedHelminthsIndirectFluorescentAntibody());
					caseExportDetailedDto.setSoilTransmittedHelminthsDirectFluorescentAntibody(exportDto.getSoilTransmittedHelminthsDirectFluorescentAntibody());
					caseExportDetailedDto.setSoilTransmittedHelminthsMicroscopy(exportDto.getSoilTransmittedHelminthsMicroscopy());
					caseExportDetailedDto.setSoilTransmittedHelminthsNeutralizingAntibodies(exportDto.getSoilTransmittedHelminthsNeutralizingAntibodies());
					caseExportDetailedDto.setSoilTransmittedHelminthsPcrRtPcr(exportDto.getSoilTransmittedHelminthsPcrRtPcr());
					caseExportDetailedDto.setSoilTransmittedHelminthsGramStain(exportDto.getSoilTransmittedHelminthsGramStain());
					caseExportDetailedDto.setSoilTransmittedHelminthsLatexAgglutination(exportDto.getSoilTransmittedHelminthsLatexAgglutination());
					caseExportDetailedDto.setSoilTransmittedHelminthsCqValueDetection(exportDto.getSoilTransmittedHelminthsCqValueDetection());
					caseExportDetailedDto.setSoilTransmittedHelminthsSequencing(exportDto.getSoilTransmittedHelminthsSequencing());
					caseExportDetailedDto.setSoilTransmittedHelminthsDnaMicroarray(exportDto.getSoilTransmittedHelminthsDnaMicroarray());
					caseExportDetailedDto.setSoilTransmittedHelminthsOther(exportDto.getSoilTransmittedHelminthsOther());
					caseExportDetailedDto.setSoilTransmittedHelminthsAntibodyDetectionDetails(exportDto.getSoilTransmittedHelminthsAntibodyDetectionDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsAntigenDetectionDetails(exportDto.getSoilTransmittedHelminthsAntigenDetectionDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsRapidTestDetails(exportDto.getSoilTransmittedHelminthsRapidTestDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsCultureDetails(exportDto.getSoilTransmittedHelminthsCultureDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsHistopathologyDetails(exportDto.getSoilTransmittedHelminthsHistopathologyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIsolationDetails(exportDto.getSoilTransmittedHelminthsIsolationDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIgmSerumAntibodyDetails(exportDto.getSoilTransmittedHelminthsIgmSerumAntibodyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIggSerumAntibodyDetails(exportDto.getSoilTransmittedHelminthsIggSerumAntibodyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIgaSerumAntibodyDetails(exportDto.getSoilTransmittedHelminthsIgaSerumAntibodyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIncubationTimeDetails(exportDto.getSoilTransmittedHelminthsIncubationTimeDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails(exportDto.getSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsDirectFluorescentAntibodyDetails(exportDto.getSoilTransmittedHelminthsDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsMicroscopyDetails(exportDto.getSoilTransmittedHelminthsMicroscopyDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsNeutralizingAntibodiesDetails(exportDto.getSoilTransmittedHelminthsNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsPcrRtPcrDetails(exportDto.getSoilTransmittedHelminthsPcrRtPcrDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsGramStainDetails(exportDto.getSoilTransmittedHelminthsGramStainDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsLatexAgglutinationDetails(exportDto.getSoilTransmittedHelminthsLatexAgglutinationDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsCqValueDetectionDetails(exportDto.getSoilTransmittedHelminthsCqValueDetectionDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsSequencingDetails(exportDto.getSoilTransmittedHelminthsSequencingDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsDnaMicroarrayDetails(exportDto.getSoilTransmittedHelminthsDnaMicroarrayDetails());
					caseExportDetailedDto.setSoilTransmittedHelminthsOtherDetails(exportDto.getSoilTransmittedHelminthsOtherDetails());
					caseExportDetailedDto.setTrypanosomiasisAntibodyDetection(exportDto.getTrypanosomiasisAntibodyDetection());
					caseExportDetailedDto.setTrypanosomiasisAntigenDetection(exportDto.getTrypanosomiasisAntigenDetection());
					caseExportDetailedDto.setTrypanosomiasisRapidTest(exportDto.getTrypanosomiasisRapidTest());
					caseExportDetailedDto.setTrypanosomiasisCulture(exportDto.getTrypanosomiasisCulture());
					caseExportDetailedDto.setTrypanosomiasisHistopathology(exportDto.getTrypanosomiasisHistopathology());
					caseExportDetailedDto.setTrypanosomiasisIsolation(exportDto.getTrypanosomiasisIsolation());
					caseExportDetailedDto.setTrypanosomiasisIgmSerumAntibody(exportDto.getTrypanosomiasisIgmSerumAntibody());
					caseExportDetailedDto.setTrypanosomiasisIggSerumAntibody(exportDto.getTrypanosomiasisIggSerumAntibody());
					caseExportDetailedDto.setTrypanosomiasisIgaSerumAntibody(exportDto.getTrypanosomiasisIgaSerumAntibody());
					caseExportDetailedDto.setTrypanosomiasisIncubationTime(exportDto.getTrypanosomiasisIncubationTime());
					caseExportDetailedDto.setTrypanosomiasisIndirectFluorescentAntibody(exportDto.getTrypanosomiasisIndirectFluorescentAntibody());
					caseExportDetailedDto.setTrypanosomiasisDirectFluorescentAntibody(exportDto.getTrypanosomiasisDirectFluorescentAntibody());
					caseExportDetailedDto.setTrypanosomiasisMicroscopy(exportDto.getTrypanosomiasisMicroscopy());
					caseExportDetailedDto.setTrypanosomiasisNeutralizingAntibodies(exportDto.getTrypanosomiasisNeutralizingAntibodies());
					caseExportDetailedDto.setTrypanosomiasisPcrRtPcr(exportDto.getTrypanosomiasisPcrRtPcr());
					caseExportDetailedDto.setTrypanosomiasisGramStain(exportDto.getTrypanosomiasisGramStain());
					caseExportDetailedDto.setTrypanosomiasisLatexAgglutination(exportDto.getTrypanosomiasisLatexAgglutination());
					caseExportDetailedDto.setTrypanosomiasisCqValueDetection(exportDto.getTrypanosomiasisCqValueDetection());
					caseExportDetailedDto.setTrypanosomiasisSequencing(exportDto.getTrypanosomiasisSequencing());
					caseExportDetailedDto.setTrypanosomiasisDnaMicroarray(exportDto.getTrypanosomiasisDnaMicroarray());
					caseExportDetailedDto.setTrypanosomiasisOther(exportDto.getTrypanosomiasisOther());
					caseExportDetailedDto.setTrypanosomiasisAntibodyDetectionDetails(exportDto.getTrypanosomiasisAntibodyDetectionDetails());
					caseExportDetailedDto.setTrypanosomiasisAntigenDetectionDetails(exportDto.getTrypanosomiasisAntigenDetectionDetails());
					caseExportDetailedDto.setTrypanosomiasisRapidTestDetails(exportDto.getTrypanosomiasisRapidTestDetails());
					caseExportDetailedDto.setTrypanosomiasisCultureDetails(exportDto.getTrypanosomiasisCultureDetails());
					caseExportDetailedDto.setTrypanosomiasisHistopathologyDetails(exportDto.getTrypanosomiasisHistopathologyDetails());
					caseExportDetailedDto.setTrypanosomiasisIsolationDetails(exportDto.getTrypanosomiasisIsolationDetails());
					caseExportDetailedDto.setTrypanosomiasisIgmSerumAntibodyDetails(exportDto.getTrypanosomiasisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setTrypanosomiasisIggSerumAntibodyDetails(exportDto.getTrypanosomiasisIggSerumAntibodyDetails());
					caseExportDetailedDto.setTrypanosomiasisIgaSerumAntibodyDetails(exportDto.getTrypanosomiasisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setTrypanosomiasisIncubationTimeDetails(exportDto.getTrypanosomiasisIncubationTimeDetails());
					caseExportDetailedDto.setTrypanosomiasisIndirectFluorescentAntibodyDetails(exportDto.getTrypanosomiasisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTrypanosomiasisDirectFluorescentAntibodyDetails(exportDto.getTrypanosomiasisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTrypanosomiasisMicroscopyDetails(exportDto.getTrypanosomiasisMicroscopyDetails());
					caseExportDetailedDto.setTrypanosomiasisNeutralizingAntibodiesDetails(exportDto.getTrypanosomiasisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setTrypanosomiasisPcrRtPcrDetails(exportDto.getTrypanosomiasisPcrRtPcrDetails());
					caseExportDetailedDto.setTrypanosomiasisGramStainDetails(exportDto.getTrypanosomiasisGramStainDetails());
					caseExportDetailedDto.setTrypanosomiasisLatexAgglutinationDetails(exportDto.getTrypanosomiasisLatexAgglutinationDetails());
					caseExportDetailedDto.setTrypanosomiasisCqValueDetectionDetails(exportDto.getTrypanosomiasisCqValueDetectionDetails());
					caseExportDetailedDto.setTrypanosomiasisSequencingDetails(exportDto.getTrypanosomiasisSequencingDetails());
					caseExportDetailedDto.setTrypanosomiasisDnaMicroarrayDetails(exportDto.getTrypanosomiasisDnaMicroarrayDetails());
					caseExportDetailedDto.setTrypanosomiasisOtherDetails(exportDto.getTrypanosomiasisOtherDetails());
					caseExportDetailedDto.setDiarrheaDehydrationAntibodyDetection(exportDto.getDiarrheaDehydrationAntibodyDetection());
					caseExportDetailedDto.setDiarrheaDehydrationAntigenDetection(exportDto.getDiarrheaDehydrationAntigenDetection());
					caseExportDetailedDto.setDiarrheaDehydrationRapidTest(exportDto.getDiarrheaDehydrationRapidTest());
					caseExportDetailedDto.setDiarrheaDehydrationCulture(exportDto.getDiarrheaDehydrationCulture());
					caseExportDetailedDto.setDiarrheaDehydrationHistopathology(exportDto.getDiarrheaDehydrationHistopathology());
					caseExportDetailedDto.setDiarrheaDehydrationIsolation(exportDto.getDiarrheaDehydrationIsolation());
					caseExportDetailedDto.setDiarrheaDehydrationIgmSerumAntibody(exportDto.getDiarrheaDehydrationIgmSerumAntibody());
					caseExportDetailedDto.setDiarrheaDehydrationIggSerumAntibody(exportDto.getDiarrheaDehydrationIggSerumAntibody());
					caseExportDetailedDto.setDiarrheaDehydrationIgaSerumAntibody(exportDto.getDiarrheaDehydrationIgaSerumAntibody());
					caseExportDetailedDto.setDiarrheaDehydrationIncubationTime(exportDto.getDiarrheaDehydrationIncubationTime());
					caseExportDetailedDto.setDiarrheaDehydrationIndirectFluorescentAntibody(exportDto.getDiarrheaDehydrationIndirectFluorescentAntibody());
					caseExportDetailedDto.setDiarrheaDehydrationDirectFluorescentAntibody(exportDto.getDiarrheaDehydrationDirectFluorescentAntibody());
					caseExportDetailedDto.setDiarrheaDehydrationMicroscopy(exportDto.getDiarrheaDehydrationMicroscopy());
					caseExportDetailedDto.setDiarrheaDehydrationNeutralizingAntibodies(exportDto.getDiarrheaDehydrationNeutralizingAntibodies());
					caseExportDetailedDto.setDiarrheaDehydrationPcrRtPcr(exportDto.getDiarrheaDehydrationPcrRtPcr());
					caseExportDetailedDto.setDiarrheaDehydrationGramStain(exportDto.getDiarrheaDehydrationGramStain());
					caseExportDetailedDto.setDiarrheaDehydrationLatexAgglutination(exportDto.getDiarrheaDehydrationLatexAgglutination());
					caseExportDetailedDto.setDiarrheaDehydrationCqValueDetection(exportDto.getDiarrheaDehydrationCqValueDetection());
					caseExportDetailedDto.setDiarrheaDehydrationSequencing(exportDto.getDiarrheaDehydrationSequencing());
					caseExportDetailedDto.setDiarrheaDehydrationDnaMicroarray(exportDto.getDiarrheaDehydrationDnaMicroarray());
					caseExportDetailedDto.setDiarrheaDehydrationOther(exportDto.getDiarrheaDehydrationOther());
					caseExportDetailedDto.setDiarrheaDehydrationAntibodyDetectionDetails(exportDto.getDiarrheaDehydrationAntibodyDetectionDetails());
					caseExportDetailedDto.setDiarrheaDehydrationAntigenDetectionDetails(exportDto.getDiarrheaDehydrationAntigenDetectionDetails());
					caseExportDetailedDto.setDiarrheaDehydrationRapidTestDetails(exportDto.getDiarrheaDehydrationRapidTestDetails());
					caseExportDetailedDto.setDiarrheaDehydrationCultureDetails(exportDto.getDiarrheaDehydrationCultureDetails());
					caseExportDetailedDto.setDiarrheaDehydrationHistopathologyDetails(exportDto.getDiarrheaDehydrationHistopathologyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIsolationDetails(exportDto.getDiarrheaDehydrationIsolationDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIgmSerumAntibodyDetails(exportDto.getDiarrheaDehydrationIgmSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIggSerumAntibodyDetails(exportDto.getDiarrheaDehydrationIggSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIgaSerumAntibodyDetails(exportDto.getDiarrheaDehydrationIgaSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIncubationTimeDetails(exportDto.getDiarrheaDehydrationIncubationTimeDetails());
					caseExportDetailedDto.setDiarrheaDehydrationIndirectFluorescentAntibodyDetails(exportDto.getDiarrheaDehydrationIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationDirectFluorescentAntibodyDetails(exportDto.getDiarrheaDehydrationDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationMicroscopyDetails(exportDto.getDiarrheaDehydrationMicroscopyDetails());
					caseExportDetailedDto.setDiarrheaDehydrationNeutralizingAntibodiesDetails(exportDto.getDiarrheaDehydrationNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setDiarrheaDehydrationPcrRtPcrDetails(exportDto.getDiarrheaDehydrationPcrRtPcrDetails());
					caseExportDetailedDto.setDiarrheaDehydrationGramStainDetails(exportDto.getDiarrheaDehydrationGramStainDetails());
					caseExportDetailedDto.setDiarrheaDehydrationLatexAgglutinationDetails(exportDto.getDiarrheaDehydrationLatexAgglutinationDetails());
					caseExportDetailedDto.setDiarrheaDehydrationCqValueDetectionDetails(exportDto.getDiarrheaDehydrationCqValueDetectionDetails());
					caseExportDetailedDto.setDiarrheaDehydrationSequencingDetails(exportDto.getDiarrheaDehydrationSequencingDetails());
					caseExportDetailedDto.setDiarrheaDehydrationDnaMicroarrayDetails(exportDto.getDiarrheaDehydrationDnaMicroarrayDetails());
					caseExportDetailedDto.setDiarrheaDehydrationOtherDetails(exportDto.getDiarrheaDehydrationOtherDetails());
					caseExportDetailedDto.setDiarrheaBloodAntibodyDetection(exportDto.getDiarrheaBloodAntibodyDetection());
					caseExportDetailedDto.setDiarrheaBloodAntigenDetection(exportDto.getDiarrheaBloodAntigenDetection());
					caseExportDetailedDto.setDiarrheaBloodRapidTest(exportDto.getDiarrheaBloodRapidTest());
					caseExportDetailedDto.setDiarrheaBloodCulture(exportDto.getDiarrheaBloodCulture());
					caseExportDetailedDto.setDiarrheaBloodHistopathology(exportDto.getDiarrheaBloodHistopathology());
					caseExportDetailedDto.setDiarrheaBloodIsolation(exportDto.getDiarrheaBloodIsolation());
					caseExportDetailedDto.setDiarrheaBloodIgmSerumAntibody(exportDto.getDiarrheaBloodIgmSerumAntibody());
					caseExportDetailedDto.setDiarrheaBloodIggSerumAntibody(exportDto.getDiarrheaBloodIggSerumAntibody());
					caseExportDetailedDto.setDiarrheaBloodIgaSerumAntibody(exportDto.getDiarrheaBloodIgaSerumAntibody());
					caseExportDetailedDto.setDiarrheaBloodIncubationTime(exportDto.getDiarrheaBloodIncubationTime());
					caseExportDetailedDto.setDiarrheaBloodIndirectFluorescentAntibody(exportDto.getDiarrheaBloodIndirectFluorescentAntibody());
					caseExportDetailedDto.setDiarrheaBloodDirectFluorescentAntibody(exportDto.getDiarrheaBloodDirectFluorescentAntibody());
					caseExportDetailedDto.setDiarrheaBloodMicroscopy(exportDto.getDiarrheaBloodMicroscopy());
					caseExportDetailedDto.setDiarrheaBloodNeutralizingAntibodies(exportDto.getDiarrheaBloodNeutralizingAntibodies());
					caseExportDetailedDto.setDiarrheaBloodPcrRtPcr(exportDto.getDiarrheaBloodPcrRtPcr());
					caseExportDetailedDto.setDiarrheaBloodGramStain(exportDto.getDiarrheaBloodGramStain());
					caseExportDetailedDto.setDiarrheaBloodLatexAgglutination(exportDto.getDiarrheaBloodLatexAgglutination());
					caseExportDetailedDto.setDiarrheaBloodCqValueDetection(exportDto.getDiarrheaBloodCqValueDetection());
					caseExportDetailedDto.setDiarrheaBloodSequencing(exportDto.getDiarrheaBloodSequencing());
					caseExportDetailedDto.setDiarrheaBloodDnaMicroarray(exportDto.getDiarrheaBloodDnaMicroarray());
					caseExportDetailedDto.setDiarrheaBloodOther(exportDto.getDiarrheaBloodOther());
					caseExportDetailedDto.setDiarrheaBloodAntibodyDetectionDetails(exportDto.getDiarrheaBloodAntibodyDetectionDetails());
					caseExportDetailedDto.setDiarrheaBloodAntigenDetectionDetails(exportDto.getDiarrheaBloodAntigenDetectionDetails());
					caseExportDetailedDto.setDiarrheaBloodRapidTestDetails(exportDto.getDiarrheaBloodRapidTestDetails());
					caseExportDetailedDto.setDiarrheaBloodCultureDetails(exportDto.getDiarrheaBloodCultureDetails());
					caseExportDetailedDto.setDiarrheaBloodHistopathologyDetails(exportDto.getDiarrheaBloodHistopathologyDetails());
					caseExportDetailedDto.setDiarrheaBloodIsolationDetails(exportDto.getDiarrheaBloodIsolationDetails());
					caseExportDetailedDto.setDiarrheaBloodIgmSerumAntibodyDetails(exportDto.getDiarrheaBloodIgmSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaBloodIggSerumAntibodyDetails(exportDto.getDiarrheaBloodIggSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaBloodIgaSerumAntibodyDetails(exportDto.getDiarrheaBloodIgaSerumAntibodyDetails());
					caseExportDetailedDto.setDiarrheaBloodIncubationTimeDetails(exportDto.getDiarrheaBloodIncubationTimeDetails());
					caseExportDetailedDto.setDiarrheaBloodIndirectFluorescentAntibodyDetails(exportDto.getDiarrheaBloodIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiarrheaBloodDirectFluorescentAntibodyDetails(exportDto.getDiarrheaBloodDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiarrheaBloodMicroscopyDetails(exportDto.getDiarrheaBloodMicroscopyDetails());
					caseExportDetailedDto.setDiarrheaBloodNeutralizingAntibodiesDetails(exportDto.getDiarrheaBloodNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setDiarrheaBloodPcrRtPcrDetails(exportDto.getDiarrheaBloodPcrRtPcrDetails());
					caseExportDetailedDto.setDiarrheaBloodGramStainDetails(exportDto.getDiarrheaBloodGramStainDetails());
					caseExportDetailedDto.setDiarrheaBloodLatexAgglutinationDetails(exportDto.getDiarrheaBloodLatexAgglutinationDetails());
					caseExportDetailedDto.setDiarrheaBloodCqValueDetectionDetails(exportDto.getDiarrheaBloodCqValueDetectionDetails());
					caseExportDetailedDto.setDiarrheaBloodSequencingDetails(exportDto.getDiarrheaBloodSequencingDetails());
					caseExportDetailedDto.setDiarrheaBloodDnaMicroarrayDetails(exportDto.getDiarrheaBloodDnaMicroarrayDetails());
					caseExportDetailedDto.setDiarrheaBloodOtherDetails(exportDto.getDiarrheaBloodOtherDetails());
					caseExportDetailedDto.setSnakeBiteAntibodyDetection(exportDto.getSnakeBiteAntibodyDetection());
					caseExportDetailedDto.setSnakeBiteAntigenDetection(exportDto.getSnakeBiteAntigenDetection());
					caseExportDetailedDto.setSnakeBiteRapidTest(exportDto.getSnakeBiteRapidTest());
					caseExportDetailedDto.setSnakeBiteCulture(exportDto.getSnakeBiteCulture());
					caseExportDetailedDto.setSnakeBiteHistopathology(exportDto.getSnakeBiteHistopathology());
					caseExportDetailedDto.setSnakeBiteIsolation(exportDto.getSnakeBiteIsolation());
					caseExportDetailedDto.setSnakeBiteIgmSerumAntibody(exportDto.getSnakeBiteIgmSerumAntibody());
					caseExportDetailedDto.setSnakeBiteIggSerumAntibody(exportDto.getSnakeBiteIggSerumAntibody());
					caseExportDetailedDto.setSnakeBiteIgaSerumAntibody(exportDto.getSnakeBiteIgaSerumAntibody());
					caseExportDetailedDto.setSnakeBiteIncubationTime(exportDto.getSnakeBiteIncubationTime());
					caseExportDetailedDto.setSnakeBiteIndirectFluorescentAntibody(exportDto.getSnakeBiteIndirectFluorescentAntibody());
					caseExportDetailedDto.setSnakeBiteDirectFluorescentAntibody(exportDto.getSnakeBiteDirectFluorescentAntibody());
					caseExportDetailedDto.setSnakeBiteMicroscopy(exportDto.getSnakeBiteMicroscopy());
					caseExportDetailedDto.setSnakeBiteNeutralizingAntibodies(exportDto.getSnakeBiteNeutralizingAntibodies());
					caseExportDetailedDto.setSnakeBitePcrRtPcr(exportDto.getSnakeBitePcrRtPcr());
					caseExportDetailedDto.setSnakeBiteGramStain(exportDto.getSnakeBiteGramStain());
					caseExportDetailedDto.setSnakeBiteLatexAgglutination(exportDto.getSnakeBiteLatexAgglutination());
					caseExportDetailedDto.setSnakeBiteCqValueDetection(exportDto.getSnakeBiteCqValueDetection());
					caseExportDetailedDto.setSnakeBiteSequencing(exportDto.getSnakeBiteSequencing());
					caseExportDetailedDto.setSnakeBiteDnaMicroarray(exportDto.getSnakeBiteDnaMicroarray());
					caseExportDetailedDto.setSnakeBiteOther(exportDto.getSnakeBiteOther());
					caseExportDetailedDto.setSnakeBiteAntibodyDetectionDetails(exportDto.getSnakeBiteAntibodyDetectionDetails());
					caseExportDetailedDto.setSnakeBiteAntigenDetectionDetails(exportDto.getSnakeBiteAntigenDetectionDetails());
					caseExportDetailedDto.setSnakeBiteRapidTestDetails(exportDto.getSnakeBiteRapidTestDetails());
					caseExportDetailedDto.setSnakeBiteCultureDetails(exportDto.getSnakeBiteCultureDetails());
					caseExportDetailedDto.setSnakeBiteHistopathologyDetails(exportDto.getSnakeBiteHistopathologyDetails());
					caseExportDetailedDto.setSnakeBiteIsolationDetails(exportDto.getSnakeBiteIsolationDetails());
					caseExportDetailedDto.setSnakeBiteIgmSerumAntibodyDetails(exportDto.getSnakeBiteIgmSerumAntibodyDetails());
					caseExportDetailedDto.setSnakeBiteIggSerumAntibodyDetails(exportDto.getSnakeBiteIggSerumAntibodyDetails());
					caseExportDetailedDto.setSnakeBiteIgaSerumAntibodyDetails(exportDto.getSnakeBiteIgaSerumAntibodyDetails());
					caseExportDetailedDto.setSnakeBiteIncubationTimeDetails(exportDto.getSnakeBiteIncubationTimeDetails());
					caseExportDetailedDto.setSnakeBiteIndirectFluorescentAntibodyDetails(exportDto.getSnakeBiteIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSnakeBiteDirectFluorescentAntibodyDetails(exportDto.getSnakeBiteDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setSnakeBiteMicroscopyDetails(exportDto.getSnakeBiteMicroscopyDetails());
					caseExportDetailedDto.setSnakeBiteNeutralizingAntibodiesDetails(exportDto.getSnakeBiteNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setSnakeBitePcrRtPcrDetails(exportDto.getSnakeBitePcrRtPcrDetails());
					caseExportDetailedDto.setSnakeBiteGramStainDetails(exportDto.getSnakeBiteGramStainDetails());
					caseExportDetailedDto.setSnakeBiteLatexAgglutinationDetails(exportDto.getSnakeBiteLatexAgglutinationDetails());
					caseExportDetailedDto.setSnakeBiteCqValueDetectionDetails(exportDto.getSnakeBiteCqValueDetectionDetails());
					caseExportDetailedDto.setSnakeBiteSequencingDetails(exportDto.getSnakeBiteSequencingDetails());
					caseExportDetailedDto.setSnakeBiteDnaMicroarrayDetails(exportDto.getSnakeBiteDnaMicroarrayDetails());
					caseExportDetailedDto.setSnakeBiteOtherDetails(exportDto.getSnakeBiteOtherDetails());
					caseExportDetailedDto.setRubellaAntibodyDetection(exportDto.getRubellaAntibodyDetection());
					caseExportDetailedDto.setRubellaAntigenDetection(exportDto.getRubellaAntigenDetection());
					caseExportDetailedDto.setRubellaRapidTest(exportDto.getRubellaRapidTest());
					caseExportDetailedDto.setRubellaCulture(exportDto.getRubellaCulture());
					caseExportDetailedDto.setRubellaHistopathology(exportDto.getRubellaHistopathology());
					caseExportDetailedDto.setRubellaIsolation(exportDto.getRubellaIsolation());
					caseExportDetailedDto.setRubellaIgmSerumAntibody(exportDto.getRubellaIgmSerumAntibody());
					caseExportDetailedDto.setRubellaIggSerumAntibody(exportDto.getRubellaIggSerumAntibody());
					caseExportDetailedDto.setRubellaIgaSerumAntibody(exportDto.getRubellaIgaSerumAntibody());
					caseExportDetailedDto.setRubellaIncubationTime(exportDto.getRubellaIncubationTime());
					caseExportDetailedDto.setRubellaIndirectFluorescentAntibody(exportDto.getRubellaIndirectFluorescentAntibody());
					caseExportDetailedDto.setRubellaDirectFluorescentAntibody(exportDto.getRubellaDirectFluorescentAntibody());
					caseExportDetailedDto.setRubellaMicroscopy(exportDto.getRubellaMicroscopy());
					caseExportDetailedDto.setRubellaNeutralizingAntibodies(exportDto.getRubellaNeutralizingAntibodies());
					caseExportDetailedDto.setRubellaPcrRtPcr(exportDto.getRubellaPcrRtPcr());
					caseExportDetailedDto.setRubellaGramStain(exportDto.getRubellaGramStain());
					caseExportDetailedDto.setRubellaLatexAgglutination(exportDto.getRubellaLatexAgglutination());
					caseExportDetailedDto.setRubellaCqValueDetection(exportDto.getRubellaCqValueDetection());
					caseExportDetailedDto.setRubellaSequencing(exportDto.getRubellaSequencing());
					caseExportDetailedDto.setRubellaDnaMicroarray(exportDto.getRubellaDnaMicroarray());
					caseExportDetailedDto.setRubellaOther(exportDto.getRubellaOther());
					caseExportDetailedDto.setRubellaAntibodyDetectionDetails(exportDto.getRubellaAntibodyDetectionDetails());
					caseExportDetailedDto.setRubellaAntigenDetectionDetails(exportDto.getRubellaAntigenDetectionDetails());
					caseExportDetailedDto.setRubellaRapidTestDetails(exportDto.getRubellaRapidTestDetails());
					caseExportDetailedDto.setRubellaCultureDetails(exportDto.getRubellaCultureDetails());
					caseExportDetailedDto.setRubellaHistopathologyDetails(exportDto.getRubellaHistopathologyDetails());
					caseExportDetailedDto.setRubellaIsolationDetails(exportDto.getRubellaIsolationDetails());
					caseExportDetailedDto.setRubellaIgmSerumAntibodyDetails(exportDto.getRubellaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setRubellaIggSerumAntibodyDetails(exportDto.getRubellaIggSerumAntibodyDetails());
					caseExportDetailedDto.setRubellaIgaSerumAntibodyDetails(exportDto.getRubellaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setRubellaIncubationTimeDetails(exportDto.getRubellaIncubationTimeDetails());
					caseExportDetailedDto.setRubellaIndirectFluorescentAntibodyDetails(exportDto.getRubellaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRubellaDirectFluorescentAntibodyDetails(exportDto.getRubellaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRubellaMicroscopyDetails(exportDto.getRubellaMicroscopyDetails());
					caseExportDetailedDto.setRubellaNeutralizingAntibodiesDetails(exportDto.getRubellaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setRubellaPcrRtPcrDetails(exportDto.getRubellaPcrRtPcrDetails());
					caseExportDetailedDto.setRubellaGramStainDetails(exportDto.getRubellaGramStainDetails());
					caseExportDetailedDto.setRubellaLatexAgglutinationDetails(exportDto.getRubellaLatexAgglutinationDetails());
					caseExportDetailedDto.setRubellaCqValueDetectionDetails(exportDto.getRubellaCqValueDetectionDetails());
					caseExportDetailedDto.setRubellaSequencingDetails(exportDto.getRubellaSequencingDetails());
					caseExportDetailedDto.setRubellaDnaMicroarrayDetails(exportDto.getRubellaDnaMicroarrayDetails());
					caseExportDetailedDto.setRubellaOtherDetails(exportDto.getRubellaOtherDetails());
					caseExportDetailedDto.setTuberculosisAntibodyDetection(exportDto.getTuberculosisAntibodyDetection());
					caseExportDetailedDto.setTuberculosisAntigenDetection(exportDto.getTuberculosisAntigenDetection());
					caseExportDetailedDto.setTuberculosisRapidTest(exportDto.getTuberculosisRapidTest());
					caseExportDetailedDto.setTuberculosisCulture(exportDto.getTuberculosisCulture());
					caseExportDetailedDto.setTuberculosisHistopathology(exportDto.getTuberculosisHistopathology());
					caseExportDetailedDto.setTuberculosisIsolation(exportDto.getTuberculosisIsolation());
					caseExportDetailedDto.setTuberculosisIgmSerumAntibody(exportDto.getTuberculosisIgmSerumAntibody());
					caseExportDetailedDto.setTuberculosisIggSerumAntibody(exportDto.getTuberculosisIggSerumAntibody());
					caseExportDetailedDto.setTuberculosisIgaSerumAntibody(exportDto.getTuberculosisIgaSerumAntibody());
					caseExportDetailedDto.setTuberculosisIncubationTime(exportDto.getTuberculosisIncubationTime());
					caseExportDetailedDto.setTuberculosisIndirectFluorescentAntibody(exportDto.getTuberculosisIndirectFluorescentAntibody());
					caseExportDetailedDto.setTuberculosisDirectFluorescentAntibody(exportDto.getTuberculosisDirectFluorescentAntibody());
					caseExportDetailedDto.setTuberculosisMicroscopy(exportDto.getTuberculosisMicroscopy());
					caseExportDetailedDto.setTuberculosisNeutralizingAntibodies(exportDto.getTuberculosisNeutralizingAntibodies());
					caseExportDetailedDto.setTuberculosisPcrRtPcr(exportDto.getTuberculosisPcrRtPcr());
					caseExportDetailedDto.setTuberculosisGramStain(exportDto.getTuberculosisGramStain());
					caseExportDetailedDto.setTuberculosisLatexAgglutination(exportDto.getTuberculosisLatexAgglutination());
					caseExportDetailedDto.setTuberculosisCqValueDetection(exportDto.getTuberculosisCqValueDetection());
					caseExportDetailedDto.setTuberculosisSequencing(exportDto.getTuberculosisSequencing());
					caseExportDetailedDto.setTuberculosisDnaMicroarray(exportDto.getTuberculosisDnaMicroarray());
					caseExportDetailedDto.setTuberculosisOther(exportDto.getTuberculosisOther());
					caseExportDetailedDto.setTuberculosisAntibodyDetectionDetails(exportDto.getTuberculosisAntibodyDetectionDetails());
					caseExportDetailedDto.setTuberculosisAntigenDetectionDetails(exportDto.getTuberculosisAntigenDetectionDetails());
					caseExportDetailedDto.setTuberculosisRapidTestDetails(exportDto.getTuberculosisRapidTestDetails());
					caseExportDetailedDto.setTuberculosisCultureDetails(exportDto.getTuberculosisCultureDetails());
					caseExportDetailedDto.setTuberculosisHistopathologyDetails(exportDto.getTuberculosisHistopathologyDetails());
					caseExportDetailedDto.setTuberculosisIsolationDetails(exportDto.getTuberculosisIsolationDetails());
					caseExportDetailedDto.setTuberculosisIgmSerumAntibodyDetails(exportDto.getTuberculosisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setTuberculosisIggSerumAntibodyDetails(exportDto.getTuberculosisIggSerumAntibodyDetails());
					caseExportDetailedDto.setTuberculosisIgaSerumAntibodyDetails(exportDto.getTuberculosisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setTuberculosisIncubationTimeDetails(exportDto.getTuberculosisIncubationTimeDetails());
					caseExportDetailedDto.setTuberculosisIndirectFluorescentAntibodyDetails(exportDto.getTuberculosisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTuberculosisDirectFluorescentAntibodyDetails(exportDto.getTuberculosisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTuberculosisMicroscopyDetails(exportDto.getTuberculosisMicroscopyDetails());
					caseExportDetailedDto.setTuberculosisNeutralizingAntibodiesDetails(exportDto.getTuberculosisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setTuberculosisPcrRtPcrDetails(exportDto.getTuberculosisPcrRtPcrDetails());
					caseExportDetailedDto.setTuberculosisGramStainDetails(exportDto.getTuberculosisGramStainDetails());
					caseExportDetailedDto.setTuberculosisLatexAgglutinationDetails(exportDto.getTuberculosisLatexAgglutinationDetails());
					caseExportDetailedDto.setTuberculosisCqValueDetectionDetails(exportDto.getTuberculosisCqValueDetectionDetails());
					caseExportDetailedDto.setTuberculosisSequencingDetails(exportDto.getTuberculosisSequencingDetails());
					caseExportDetailedDto.setTuberculosisDnaMicroarrayDetails(exportDto.getTuberculosisDnaMicroarrayDetails());
					caseExportDetailedDto.setTuberculosisOtherDetails(exportDto.getTuberculosisOtherDetails());
					caseExportDetailedDto.setLeprosyAntibodyDetection(exportDto.getLeprosyAntibodyDetection());
					caseExportDetailedDto.setLeprosyAntigenDetection(exportDto.getLeprosyAntigenDetection());
					caseExportDetailedDto.setLeprosyRapidTest(exportDto.getLeprosyRapidTest());
					caseExportDetailedDto.setLeprosyCulture(exportDto.getLeprosyCulture());
					caseExportDetailedDto.setLeprosyHistopathology(exportDto.getLeprosyHistopathology());
					caseExportDetailedDto.setLeprosyIsolation(exportDto.getLeprosyIsolation());
					caseExportDetailedDto.setLeprosyIgmSerumAntibody(exportDto.getLeprosyIgmSerumAntibody());
					caseExportDetailedDto.setLeprosyIggSerumAntibody(exportDto.getLeprosyIggSerumAntibody());
					caseExportDetailedDto.setLeprosyIgaSerumAntibody(exportDto.getLeprosyIgaSerumAntibody());
					caseExportDetailedDto.setLeprosyIncubationTime(exportDto.getLeprosyIncubationTime());
					caseExportDetailedDto.setLeprosyIndirectFluorescentAntibody(exportDto.getLeprosyIndirectFluorescentAntibody());
					caseExportDetailedDto.setLeprosyDirectFluorescentAntibody(exportDto.getLeprosyDirectFluorescentAntibody());
					caseExportDetailedDto.setLeprosyMicroscopy(exportDto.getLeprosyMicroscopy());
					caseExportDetailedDto.setLeprosyNeutralizingAntibodies(exportDto.getLeprosyNeutralizingAntibodies());
					caseExportDetailedDto.setLeprosyPcrRtPcr(exportDto.getLeprosyPcrRtPcr());
					caseExportDetailedDto.setLeprosyGramStain(exportDto.getLeprosyGramStain());
					caseExportDetailedDto.setLeprosyLatexAgglutination(exportDto.getLeprosyLatexAgglutination());
					caseExportDetailedDto.setLeprosyCqValueDetection(exportDto.getLeprosyCqValueDetection());
					caseExportDetailedDto.setLeprosySequencing(exportDto.getLeprosySequencing());
					caseExportDetailedDto.setLeprosyDnaMicroarray(exportDto.getLeprosyDnaMicroarray());
					caseExportDetailedDto.setLeprosyOther(exportDto.getLeprosyOther());
					caseExportDetailedDto.setLeprosyAntibodyDetectionDetails(exportDto.getLeprosyAntibodyDetectionDetails());
					caseExportDetailedDto.setLeprosyAntigenDetectionDetails(exportDto.getLeprosyAntigenDetectionDetails());
					caseExportDetailedDto.setLeprosyRapidTestDetails(exportDto.getLeprosyRapidTestDetails());
					caseExportDetailedDto.setLeprosyCultureDetails(exportDto.getLeprosyCultureDetails());
					caseExportDetailedDto.setLeprosyHistopathologyDetails(exportDto.getLeprosyHistopathologyDetails());
					caseExportDetailedDto.setLeprosyIsolationDetails(exportDto.getLeprosyIsolationDetails());
					caseExportDetailedDto.setLeprosyIgmSerumAntibodyDetails(exportDto.getLeprosyIgmSerumAntibodyDetails());
					caseExportDetailedDto.setLeprosyIggSerumAntibodyDetails(exportDto.getLeprosyIggSerumAntibodyDetails());
					caseExportDetailedDto.setLeprosyIgaSerumAntibodyDetails(exportDto.getLeprosyIgaSerumAntibodyDetails());
					caseExportDetailedDto.setLeprosyIncubationTimeDetails(exportDto.getLeprosyIncubationTimeDetails());
					caseExportDetailedDto.setLeprosyIndirectFluorescentAntibodyDetails(exportDto.getLeprosyIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLeprosyDirectFluorescentAntibodyDetails(exportDto.getLeprosyDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLeprosyMicroscopyDetails(exportDto.getLeprosyMicroscopyDetails());
					caseExportDetailedDto.setLeprosyNeutralizingAntibodiesDetails(exportDto.getLeprosyNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setLeprosyPcrRtPcrDetails(exportDto.getLeprosyPcrRtPcrDetails());
					caseExportDetailedDto.setLeprosyGramStainDetails(exportDto.getLeprosyGramStainDetails());
					caseExportDetailedDto.setLeprosyLatexAgglutinationDetails(exportDto.getLeprosyLatexAgglutinationDetails());
					caseExportDetailedDto.setLeprosyCqValueDetectionDetails(exportDto.getLeprosyCqValueDetectionDetails());
					caseExportDetailedDto.setLeprosySequencingDetails(exportDto.getLeprosySequencingDetails());
					caseExportDetailedDto.setLeprosyDnaMicroarrayDetails(exportDto.getLeprosyDnaMicroarrayDetails());
					caseExportDetailedDto.setLeprosyOtherDetails(exportDto.getLeprosyOtherDetails());
					caseExportDetailedDto.setLymphaticFilariasisAntibodyDetection(exportDto.getLymphaticFilariasisAntibodyDetection());
					caseExportDetailedDto.setLymphaticFilariasisAntigenDetection(exportDto.getLymphaticFilariasisAntigenDetection());
					caseExportDetailedDto.setLymphaticFilariasisRapidTest(exportDto.getLymphaticFilariasisRapidTest());
					caseExportDetailedDto.setLymphaticFilariasisCulture(exportDto.getLymphaticFilariasisCulture());
					caseExportDetailedDto.setLymphaticFilariasisHistopathology(exportDto.getLymphaticFilariasisHistopathology());
					caseExportDetailedDto.setLymphaticFilariasisIsolation(exportDto.getLymphaticFilariasisIsolation());
					caseExportDetailedDto.setLymphaticFilariasisIgmSerumAntibody(exportDto.getLymphaticFilariasisIgmSerumAntibody());
					caseExportDetailedDto.setLymphaticFilariasisIggSerumAntibody(exportDto.getLymphaticFilariasisIggSerumAntibody());
					caseExportDetailedDto.setLymphaticFilariasisIgaSerumAntibody(exportDto.getLymphaticFilariasisIgaSerumAntibody());
					caseExportDetailedDto.setLymphaticFilariasisIncubationTime(exportDto.getLymphaticFilariasisIncubationTime());
					caseExportDetailedDto.setLymphaticFilariasisIndirectFluorescentAntibody(exportDto.getLymphaticFilariasisIndirectFluorescentAntibody());
					caseExportDetailedDto.setLymphaticFilariasisDirectFluorescentAntibody(exportDto.getLymphaticFilariasisDirectFluorescentAntibody());
					caseExportDetailedDto.setLymphaticFilariasisMicroscopy(exportDto.getLymphaticFilariasisMicroscopy());
					caseExportDetailedDto.setLymphaticFilariasisNeutralizingAntibodies(exportDto.getLymphaticFilariasisNeutralizingAntibodies());
					caseExportDetailedDto.setLymphaticFilariasisPcrRtPcr(exportDto.getLymphaticFilariasisPcrRtPcr());
					caseExportDetailedDto.setLymphaticFilariasisGramStain(exportDto.getLymphaticFilariasisGramStain());
					caseExportDetailedDto.setLymphaticFilariasisLatexAgglutination(exportDto.getLymphaticFilariasisLatexAgglutination());
					caseExportDetailedDto.setLymphaticFilariasisCqValueDetection(exportDto.getLymphaticFilariasisCqValueDetection());
					caseExportDetailedDto.setLymphaticFilariasisSequencing(exportDto.getLymphaticFilariasisSequencing());
					caseExportDetailedDto.setLymphaticFilariasisDnaMicroarray(exportDto.getLymphaticFilariasisDnaMicroarray());
					caseExportDetailedDto.setLymphaticFilariasisOther(exportDto.getLymphaticFilariasisOther());
					caseExportDetailedDto.setLymphaticFilariasisAntibodyDetectionDetails(exportDto.getLymphaticFilariasisAntibodyDetectionDetails());
					caseExportDetailedDto.setLymphaticFilariasisAntigenDetectionDetails(exportDto.getLymphaticFilariasisAntigenDetectionDetails());
					caseExportDetailedDto.setLymphaticFilariasisRapidTestDetails(exportDto.getLymphaticFilariasisRapidTestDetails());
					caseExportDetailedDto.setLymphaticFilariasisCultureDetails(exportDto.getLymphaticFilariasisCultureDetails());
					caseExportDetailedDto.setLymphaticFilariasisHistopathologyDetails(exportDto.getLymphaticFilariasisHistopathologyDetails());
					caseExportDetailedDto.setLymphaticFilariasisIsolationDetails(exportDto.getLymphaticFilariasisIsolationDetails());
					caseExportDetailedDto.setLymphaticFilariasisIgmSerumAntibodyDetails(exportDto.getLymphaticFilariasisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setLymphaticFilariasisIggSerumAntibodyDetails(exportDto.getLymphaticFilariasisIggSerumAntibodyDetails());
					caseExportDetailedDto.setLymphaticFilariasisIgaSerumAntibodyDetails(exportDto.getLymphaticFilariasisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setLymphaticFilariasisIncubationTimeDetails(exportDto.getLymphaticFilariasisIncubationTimeDetails());
					caseExportDetailedDto.setLymphaticFilariasisIndirectFluorescentAntibodyDetails(exportDto.getLymphaticFilariasisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLymphaticFilariasisDirectFluorescentAntibodyDetails(exportDto.getLymphaticFilariasisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setLymphaticFilariasisMicroscopyDetails(exportDto.getLymphaticFilariasisMicroscopyDetails());
					caseExportDetailedDto.setLymphaticFilariasisNeutralizingAntibodiesDetails(exportDto.getLymphaticFilariasisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setLymphaticFilariasisPcrRtPcrDetails(exportDto.getLymphaticFilariasisPcrRtPcrDetails());
					caseExportDetailedDto.setLymphaticFilariasisGramStainDetails(exportDto.getLymphaticFilariasisGramStainDetails());
					caseExportDetailedDto.setLymphaticFilariasisLatexAgglutinationDetails(exportDto.getLymphaticFilariasisLatexAgglutinationDetails());
					caseExportDetailedDto.setLymphaticFilariasisCqValueDetectionDetails(exportDto.getLymphaticFilariasisCqValueDetectionDetails());
					caseExportDetailedDto.setLymphaticFilariasisSequencingDetails(exportDto.getLymphaticFilariasisSequencingDetails());
					caseExportDetailedDto.setLymphaticFilariasisDnaMicroarrayDetails(exportDto.getLymphaticFilariasisDnaMicroarrayDetails());
					caseExportDetailedDto.setLymphaticFilariasisOtherDetails(exportDto.getLymphaticFilariasisOtherDetails());
					caseExportDetailedDto.setBuruliUlcerAntibodyDetection(exportDto.getBuruliUlcerAntibodyDetection());
					caseExportDetailedDto.setBuruliUlcerAntigenDetection(exportDto.getBuruliUlcerAntigenDetection());
					caseExportDetailedDto.setBuruliUlcerRapidTest(exportDto.getBuruliUlcerRapidTest());
					caseExportDetailedDto.setBuruliUlcerCulture(exportDto.getBuruliUlcerCulture());
					caseExportDetailedDto.setBuruliUlcerHistopathology(exportDto.getBuruliUlcerHistopathology());
					caseExportDetailedDto.setBuruliUlcerIsolation(exportDto.getBuruliUlcerIsolation());
					caseExportDetailedDto.setBuruliUlcerIgmSerumAntibody(exportDto.getBuruliUlcerIgmSerumAntibody());
					caseExportDetailedDto.setBuruliUlcerIggSerumAntibody(exportDto.getBuruliUlcerIggSerumAntibody());
					caseExportDetailedDto.setBuruliUlcerIgaSerumAntibody(exportDto.getBuruliUlcerIgaSerumAntibody());
					caseExportDetailedDto.setBuruliUlcerIncubationTime(exportDto.getBuruliUlcerIncubationTime());
					caseExportDetailedDto.setBuruliUlcerIndirectFluorescentAntibody(exportDto.getBuruliUlcerIndirectFluorescentAntibody());
					caseExportDetailedDto.setBuruliUlcerDirectFluorescentAntibody(exportDto.getBuruliUlcerDirectFluorescentAntibody());
					caseExportDetailedDto.setBuruliUlcerMicroscopy(exportDto.getBuruliUlcerMicroscopy());
					caseExportDetailedDto.setBuruliUlcerNeutralizingAntibodies(exportDto.getBuruliUlcerNeutralizingAntibodies());
					caseExportDetailedDto.setBuruliUlcerPcrRtPcr(exportDto.getBuruliUlcerPcrRtPcr());
					caseExportDetailedDto.setBuruliUlcerGramStain(exportDto.getBuruliUlcerGramStain());
					caseExportDetailedDto.setBuruliUlcerLatexAgglutination(exportDto.getBuruliUlcerLatexAgglutination());
					caseExportDetailedDto.setBuruliUlcerCqValueDetection(exportDto.getBuruliUlcerCqValueDetection());
					caseExportDetailedDto.setBuruliUlcerSequencing(exportDto.getBuruliUlcerSequencing());
					caseExportDetailedDto.setBuruliUlcerDnaMicroarray(exportDto.getBuruliUlcerDnaMicroarray());
					caseExportDetailedDto.setBuruliUlcerOther(exportDto.getBuruliUlcerOther());
					caseExportDetailedDto.setBuruliUlcerAntibodyDetectionDetails(exportDto.getBuruliUlcerAntibodyDetectionDetails());
					caseExportDetailedDto.setBuruliUlcerAntigenDetectionDetails(exportDto.getBuruliUlcerAntigenDetectionDetails());
					caseExportDetailedDto.setBuruliUlcerRapidTestDetails(exportDto.getBuruliUlcerRapidTestDetails());
					caseExportDetailedDto.setBuruliUlcerCultureDetails(exportDto.getBuruliUlcerCultureDetails());
					caseExportDetailedDto.setBuruliUlcerHistopathologyDetails(exportDto.getBuruliUlcerHistopathologyDetails());
					caseExportDetailedDto.setBuruliUlcerIsolationDetails(exportDto.getBuruliUlcerIsolationDetails());
					caseExportDetailedDto.setBuruliUlcerIgmSerumAntibodyDetails(exportDto.getBuruliUlcerIgmSerumAntibodyDetails());
					caseExportDetailedDto.setBuruliUlcerIggSerumAntibodyDetails(exportDto.getBuruliUlcerIggSerumAntibodyDetails());
					caseExportDetailedDto.setBuruliUlcerIgaSerumAntibodyDetails(exportDto.getBuruliUlcerIgaSerumAntibodyDetails());
					caseExportDetailedDto.setBuruliUlcerIncubationTimeDetails(exportDto.getBuruliUlcerIncubationTimeDetails());
					caseExportDetailedDto.setBuruliUlcerIndirectFluorescentAntibodyDetails(exportDto.getBuruliUlcerIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setBuruliUlcerDirectFluorescentAntibodyDetails(exportDto.getBuruliUlcerDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setBuruliUlcerMicroscopyDetails(exportDto.getBuruliUlcerMicroscopyDetails());
					caseExportDetailedDto.setBuruliUlcerNeutralizingAntibodiesDetails(exportDto.getBuruliUlcerNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setBuruliUlcerPcrRtPcrDetails(exportDto.getBuruliUlcerPcrRtPcrDetails());
					caseExportDetailedDto.setBuruliUlcerGramStainDetails(exportDto.getBuruliUlcerGramStainDetails());
					caseExportDetailedDto.setBuruliUlcerLatexAgglutinationDetails(exportDto.getBuruliUlcerLatexAgglutinationDetails());
					caseExportDetailedDto.setBuruliUlcerCqValueDetectionDetails(exportDto.getBuruliUlcerCqValueDetectionDetails());
					caseExportDetailedDto.setBuruliUlcerSequencingDetails(exportDto.getBuruliUlcerSequencingDetails());
					caseExportDetailedDto.setBuruliUlcerDnaMicroarrayDetails(exportDto.getBuruliUlcerDnaMicroarrayDetails());
					caseExportDetailedDto.setBuruliUlcerOtherDetails(exportDto.getBuruliUlcerOtherDetails());
					caseExportDetailedDto.setPertussisAntibodyDetection(exportDto.getPertussisAntibodyDetection());
					caseExportDetailedDto.setPertussisAntigenDetection(exportDto.getPertussisAntigenDetection());
					caseExportDetailedDto.setPertussisRapidTest(exportDto.getPertussisRapidTest());
					caseExportDetailedDto.setPertussisCulture(exportDto.getPertussisCulture());
					caseExportDetailedDto.setPertussisHistopathology(exportDto.getPertussisHistopathology());
					caseExportDetailedDto.setPertussisIsolation(exportDto.getPertussisIsolation());
					caseExportDetailedDto.setPertussisIgmSerumAntibody(exportDto.getPertussisIgmSerumAntibody());
					caseExportDetailedDto.setPertussisIggSerumAntibody(exportDto.getPertussisIggSerumAntibody());
					caseExportDetailedDto.setPertussisIgaSerumAntibody(exportDto.getPertussisIgaSerumAntibody());
					caseExportDetailedDto.setPertussisIncubationTime(exportDto.getPertussisIncubationTime());
					caseExportDetailedDto.setPertussisIndirectFluorescentAntibody(exportDto.getPertussisIndirectFluorescentAntibody());
					caseExportDetailedDto.setPertussisDirectFluorescentAntibody(exportDto.getPertussisDirectFluorescentAntibody());
					caseExportDetailedDto.setPertussisMicroscopy(exportDto.getPertussisMicroscopy());
					caseExportDetailedDto.setPertussisNeutralizingAntibodies(exportDto.getPertussisNeutralizingAntibodies());
					caseExportDetailedDto.setPertussisPcrRtPcr(exportDto.getPertussisPcrRtPcr());
					caseExportDetailedDto.setPertussisGramStain(exportDto.getPertussisGramStain());
					caseExportDetailedDto.setPertussisLatexAgglutination(exportDto.getPertussisLatexAgglutination());
					caseExportDetailedDto.setPertussisCqValueDetection(exportDto.getPertussisCqValueDetection());
					caseExportDetailedDto.setPertussisSequencing(exportDto.getPertussisSequencing());
					caseExportDetailedDto.setPertussisDnaMicroarray(exportDto.getPertussisDnaMicroarray());
					caseExportDetailedDto.setPertussisOther(exportDto.getPertussisOther());
					caseExportDetailedDto.setPertussisAntibodyDetectionDetails(exportDto.getPertussisAntibodyDetectionDetails());
					caseExportDetailedDto.setPertussisAntigenDetectionDetails(exportDto.getPertussisAntigenDetectionDetails());
					caseExportDetailedDto.setPertussisRapidTestDetails(exportDto.getPertussisRapidTestDetails());
					caseExportDetailedDto.setPertussisCultureDetails(exportDto.getPertussisCultureDetails());
					caseExportDetailedDto.setPertussisHistopathologyDetails(exportDto.getPertussisHistopathologyDetails());
					caseExportDetailedDto.setPertussisIsolationDetails(exportDto.getPertussisIsolationDetails());
					caseExportDetailedDto.setPertussisIgmSerumAntibodyDetails(exportDto.getPertussisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPertussisIggSerumAntibodyDetails(exportDto.getPertussisIggSerumAntibodyDetails());
					caseExportDetailedDto.setPertussisIgaSerumAntibodyDetails(exportDto.getPertussisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPertussisIncubationTimeDetails(exportDto.getPertussisIncubationTimeDetails());
					caseExportDetailedDto.setPertussisIndirectFluorescentAntibodyDetails(exportDto.getPertussisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPertussisDirectFluorescentAntibodyDetails(exportDto.getPertussisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPertussisMicroscopyDetails(exportDto.getPertussisMicroscopyDetails());
					caseExportDetailedDto.setPertussisNeutralizingAntibodiesDetails(exportDto.getPertussisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPertussisPcrRtPcrDetails(exportDto.getPertussisPcrRtPcrDetails());
					caseExportDetailedDto.setPertussisGramStainDetails(exportDto.getPertussisGramStainDetails());
					caseExportDetailedDto.setPertussisLatexAgglutinationDetails(exportDto.getPertussisLatexAgglutinationDetails());
					caseExportDetailedDto.setPertussisCqValueDetectionDetails(exportDto.getPertussisCqValueDetectionDetails());
					caseExportDetailedDto.setPertussisSequencingDetails(exportDto.getPertussisSequencingDetails());
					caseExportDetailedDto.setPertussisDnaMicroarrayDetails(exportDto.getPertussisDnaMicroarrayDetails());
					caseExportDetailedDto.setPertussisOtherDetails(exportDto.getPertussisOtherDetails());
					caseExportDetailedDto.setNeonatalTetanusAntibodyDetection(exportDto.getNeonatalTetanusAntibodyDetection());
					caseExportDetailedDto.setNeonatalTetanusAntigenDetection(exportDto.getNeonatalTetanusAntigenDetection());
					caseExportDetailedDto.setNeonatalTetanusRapidTest(exportDto.getNeonatalTetanusRapidTest());
					caseExportDetailedDto.setNeonatalTetanusCulture(exportDto.getNeonatalTetanusCulture());
					caseExportDetailedDto.setNeonatalTetanusHistopathology(exportDto.getNeonatalTetanusHistopathology());
					caseExportDetailedDto.setNeonatalTetanusIsolation(exportDto.getNeonatalTetanusIsolation());
					caseExportDetailedDto.setNeonatalTetanusIgmSerumAntibody(exportDto.getNeonatalTetanusIgmSerumAntibody());
					caseExportDetailedDto.setNeonatalTetanusIggSerumAntibody(exportDto.getNeonatalTetanusIggSerumAntibody());
					caseExportDetailedDto.setNeonatalTetanusIgaSerumAntibody(exportDto.getNeonatalTetanusIgaSerumAntibody());
					caseExportDetailedDto.setNeonatalTetanusIncubationTime(exportDto.getNeonatalTetanusIncubationTime());
					caseExportDetailedDto.setNeonatalTetanusIndirectFluorescentAntibody(exportDto.getNeonatalTetanusIndirectFluorescentAntibody());
					caseExportDetailedDto.setNeonatalTetanusDirectFluorescentAntibody(exportDto.getNeonatalTetanusDirectFluorescentAntibody());
					caseExportDetailedDto.setNeonatalTetanusMicroscopy(exportDto.getNeonatalTetanusMicroscopy());
					caseExportDetailedDto.setNeonatalTetanusNeutralizingAntibodies(exportDto.getNeonatalTetanusNeutralizingAntibodies());
					caseExportDetailedDto.setNeonatalTetanusPcrRtPcr(exportDto.getNeonatalTetanusPcrRtPcr());
					caseExportDetailedDto.setNeonatalTetanusGramStain(exportDto.getNeonatalTetanusGramStain());
					caseExportDetailedDto.setNeonatalTetanusLatexAgglutination(exportDto.getNeonatalTetanusLatexAgglutination());
					caseExportDetailedDto.setNeonatalTetanusCqValueDetection(exportDto.getNeonatalTetanusCqValueDetection());
					caseExportDetailedDto.setNeonatalTetanusSequencing(exportDto.getNeonatalTetanusSequencing());
					caseExportDetailedDto.setNeonatalTetanusDnaMicroarray(exportDto.getNeonatalTetanusDnaMicroarray());
					caseExportDetailedDto.setNeonatalTetanusOther(exportDto.getNeonatalTetanusOther());
					caseExportDetailedDto.setNeonatalTetanusAntibodyDetectionDetails(exportDto.getNeonatalTetanusAntibodyDetectionDetails());
					caseExportDetailedDto.setNeonatalTetanusAntigenDetectionDetails(exportDto.getNeonatalTetanusAntigenDetectionDetails());
					caseExportDetailedDto.setNeonatalTetanusRapidTestDetails(exportDto.getNeonatalTetanusRapidTestDetails());
					caseExportDetailedDto.setNeonatalTetanusCultureDetails(exportDto.getNeonatalTetanusCultureDetails());
					caseExportDetailedDto.setNeonatalTetanusHistopathologyDetails(exportDto.getNeonatalTetanusHistopathologyDetails());
					caseExportDetailedDto.setNeonatalTetanusIsolationDetails(exportDto.getNeonatalTetanusIsolationDetails());
					caseExportDetailedDto.setNeonatalTetanusIgmSerumAntibodyDetails(exportDto.getNeonatalTetanusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setNeonatalTetanusIggSerumAntibodyDetails(exportDto.getNeonatalTetanusIggSerumAntibodyDetails());
					caseExportDetailedDto.setNeonatalTetanusIgaSerumAntibodyDetails(exportDto.getNeonatalTetanusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setNeonatalTetanusIncubationTimeDetails(exportDto.getNeonatalTetanusIncubationTimeDetails());
					caseExportDetailedDto.setNeonatalTetanusIndirectFluorescentAntibodyDetails(exportDto.getNeonatalTetanusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNeonatalTetanusDirectFluorescentAntibodyDetails(exportDto.getNeonatalTetanusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setNeonatalTetanusMicroscopyDetails(exportDto.getNeonatalTetanusMicroscopyDetails());
					caseExportDetailedDto.setNeonatalTetanusNeutralizingAntibodiesDetails(exportDto.getNeonatalTetanusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setNeonatalTetanusPcrRtPcrDetails(exportDto.getNeonatalTetanusPcrRtPcrDetails());
					caseExportDetailedDto.setNeonatalTetanusGramStainDetails(exportDto.getNeonatalTetanusGramStainDetails());
					caseExportDetailedDto.setNeonatalTetanusLatexAgglutinationDetails(exportDto.getNeonatalTetanusLatexAgglutinationDetails());
					caseExportDetailedDto.setNeonatalTetanusCqValueDetectionDetails(exportDto.getNeonatalTetanusCqValueDetectionDetails());
					caseExportDetailedDto.setNeonatalTetanusSequencingDetails(exportDto.getNeonatalTetanusSequencingDetails());
					caseExportDetailedDto.setNeonatalTetanusDnaMicroarrayDetails(exportDto.getNeonatalTetanusDnaMicroarrayDetails());
					caseExportDetailedDto.setNeonatalTetanusOtherDetails(exportDto.getNeonatalTetanusOtherDetails());
					caseExportDetailedDto.setOnchocerciasisAntibodyDetection(exportDto.getOnchocerciasisAntibodyDetection());
					caseExportDetailedDto.setOnchocerciasisAntigenDetection(exportDto.getOnchocerciasisAntigenDetection());
					caseExportDetailedDto.setOnchocerciasisRapidTest(exportDto.getOnchocerciasisRapidTest());
					caseExportDetailedDto.setOnchocerciasisCulture(exportDto.getOnchocerciasisCulture());
					caseExportDetailedDto.setOnchocerciasisHistopathology(exportDto.getOnchocerciasisHistopathology());
					caseExportDetailedDto.setOnchocerciasisIsolation(exportDto.getOnchocerciasisIsolation());
					caseExportDetailedDto.setOnchocerciasisIgmSerumAntibody(exportDto.getOnchocerciasisIgmSerumAntibody());
					caseExportDetailedDto.setOnchocerciasisIggSerumAntibody(exportDto.getOnchocerciasisIggSerumAntibody());
					caseExportDetailedDto.setOnchocerciasisIgaSerumAntibody(exportDto.getOnchocerciasisIgaSerumAntibody());
					caseExportDetailedDto.setOnchocerciasisIncubationTime(exportDto.getOnchocerciasisIncubationTime());
					caseExportDetailedDto.setOnchocerciasisIndirectFluorescentAntibody(exportDto.getOnchocerciasisIndirectFluorescentAntibody());
					caseExportDetailedDto.setOnchocerciasisDirectFluorescentAntibody(exportDto.getOnchocerciasisDirectFluorescentAntibody());
					caseExportDetailedDto.setOnchocerciasisMicroscopy(exportDto.getOnchocerciasisMicroscopy());
					caseExportDetailedDto.setOnchocerciasisNeutralizingAntibodies(exportDto.getOnchocerciasisNeutralizingAntibodies());
					caseExportDetailedDto.setOnchocerciasisPcrRtPcr(exportDto.getOnchocerciasisPcrRtPcr());
					caseExportDetailedDto.setOnchocerciasisGramStain(exportDto.getOnchocerciasisGramStain());
					caseExportDetailedDto.setOnchocerciasisLatexAgglutination(exportDto.getOnchocerciasisLatexAgglutination());
					caseExportDetailedDto.setOnchocerciasisCqValueDetection(exportDto.getOnchocerciasisCqValueDetection());
					caseExportDetailedDto.setOnchocerciasisSequencing(exportDto.getOnchocerciasisSequencing());
					caseExportDetailedDto.setOnchocerciasisDnaMicroarray(exportDto.getOnchocerciasisDnaMicroarray());
					caseExportDetailedDto.setOnchocerciasisOther(exportDto.getOnchocerciasisOther());
					caseExportDetailedDto.setOnchocerciasisAntibodyDetectionDetails(exportDto.getOnchocerciasisAntibodyDetectionDetails());
					caseExportDetailedDto.setOnchocerciasisAntigenDetectionDetails(exportDto.getOnchocerciasisAntigenDetectionDetails());
					caseExportDetailedDto.setOnchocerciasisRapidTestDetails(exportDto.getOnchocerciasisRapidTestDetails());
					caseExportDetailedDto.setOnchocerciasisCultureDetails(exportDto.getOnchocerciasisCultureDetails());
					caseExportDetailedDto.setOnchocerciasisHistopathologyDetails(exportDto.getOnchocerciasisHistopathologyDetails());
					caseExportDetailedDto.setOnchocerciasisIsolationDetails(exportDto.getOnchocerciasisIsolationDetails());
					caseExportDetailedDto.setOnchocerciasisIgmSerumAntibodyDetails(exportDto.getOnchocerciasisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setOnchocerciasisIggSerumAntibodyDetails(exportDto.getOnchocerciasisIggSerumAntibodyDetails());
					caseExportDetailedDto.setOnchocerciasisIgaSerumAntibodyDetails(exportDto.getOnchocerciasisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setOnchocerciasisIncubationTimeDetails(exportDto.getOnchocerciasisIncubationTimeDetails());
					caseExportDetailedDto.setOnchocerciasisIndirectFluorescentAntibodyDetails(exportDto.getOnchocerciasisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setOnchocerciasisDirectFluorescentAntibodyDetails(exportDto.getOnchocerciasisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setOnchocerciasisMicroscopyDetails(exportDto.getOnchocerciasisMicroscopyDetails());
					caseExportDetailedDto.setOnchocerciasisNeutralizingAntibodiesDetails(exportDto.getOnchocerciasisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setOnchocerciasisPcrRtPcrDetails(exportDto.getOnchocerciasisPcrRtPcrDetails());
					caseExportDetailedDto.setOnchocerciasisGramStainDetails(exportDto.getOnchocerciasisGramStainDetails());
					caseExportDetailedDto.setOnchocerciasisLatexAgglutinationDetails(exportDto.getOnchocerciasisLatexAgglutinationDetails());
					caseExportDetailedDto.setOnchocerciasisCqValueDetectionDetails(exportDto.getOnchocerciasisCqValueDetectionDetails());
					caseExportDetailedDto.setOnchocerciasisSequencingDetails(exportDto.getOnchocerciasisSequencingDetails());
					caseExportDetailedDto.setOnchocerciasisDnaMicroarrayDetails(exportDto.getOnchocerciasisDnaMicroarrayDetails());
					caseExportDetailedDto.setOnchocerciasisOtherDetails(exportDto.getOnchocerciasisOtherDetails());
					caseExportDetailedDto.setDiphteriaAntibodyDetection(exportDto.getDiphteriaAntibodyDetection());
					caseExportDetailedDto.setDiphteriaAntigenDetection(exportDto.getDiphteriaAntigenDetection());
					caseExportDetailedDto.setDiphteriaRapidTest(exportDto.getDiphteriaRapidTest());
					caseExportDetailedDto.setDiphteriaCulture(exportDto.getDiphteriaCulture());
					caseExportDetailedDto.setDiphteriaHistopathology(exportDto.getDiphteriaHistopathology());
					caseExportDetailedDto.setDiphteriaIsolation(exportDto.getDiphteriaIsolation());
					caseExportDetailedDto.setDiphteriaIgmSerumAntibody(exportDto.getDiphteriaIgmSerumAntibody());
					caseExportDetailedDto.setDiphteriaIggSerumAntibody(exportDto.getDiphteriaIggSerumAntibody());
					caseExportDetailedDto.setDiphteriaIgaSerumAntibody(exportDto.getDiphteriaIgaSerumAntibody());
					caseExportDetailedDto.setDiphteriaIncubationTime(exportDto.getDiphteriaIncubationTime());
					caseExportDetailedDto.setDiphteriaIndirectFluorescentAntibody(exportDto.getDiphteriaIndirectFluorescentAntibody());
					caseExportDetailedDto.setDiphteriaDirectFluorescentAntibody(exportDto.getDiphteriaDirectFluorescentAntibody());
					caseExportDetailedDto.setDiphteriaMicroscopy(exportDto.getDiphteriaMicroscopy());
					caseExportDetailedDto.setDiphteriaNeutralizingAntibodies(exportDto.getDiphteriaNeutralizingAntibodies());
					caseExportDetailedDto.setDiphteriaPcrRtPcr(exportDto.getDiphteriaPcrRtPcr());
					caseExportDetailedDto.setDiphteriaGramStain(exportDto.getDiphteriaGramStain());
					caseExportDetailedDto.setDiphteriaLatexAgglutination(exportDto.getDiphteriaLatexAgglutination());
					caseExportDetailedDto.setDiphteriaCqValueDetection(exportDto.getDiphteriaCqValueDetection());
					caseExportDetailedDto.setDiphteriaSequencing(exportDto.getDiphteriaSequencing());
					caseExportDetailedDto.setDiphteriaDnaMicroarray(exportDto.getDiphteriaDnaMicroarray());
					caseExportDetailedDto.setDiphteriaOther(exportDto.getDiphteriaOther());
					caseExportDetailedDto.setDiphteriaAntibodyDetectionDetails(exportDto.getDiphteriaAntibodyDetectionDetails());
					caseExportDetailedDto.setDiphteriaAntigenDetectionDetails(exportDto.getDiphteriaAntigenDetectionDetails());
					caseExportDetailedDto.setDiphteriaRapidTestDetails(exportDto.getDiphteriaRapidTestDetails());
					caseExportDetailedDto.setDiphteriaCultureDetails(exportDto.getDiphteriaCultureDetails());
					caseExportDetailedDto.setDiphteriaHistopathologyDetails(exportDto.getDiphteriaHistopathologyDetails());
					caseExportDetailedDto.setDiphteriaIsolationDetails(exportDto.getDiphteriaIsolationDetails());
					caseExportDetailedDto.setDiphteriaIgmSerumAntibodyDetails(exportDto.getDiphteriaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setDiphteriaIggSerumAntibodyDetails(exportDto.getDiphteriaIggSerumAntibodyDetails());
					caseExportDetailedDto.setDiphteriaIgaSerumAntibodyDetails(exportDto.getDiphteriaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setDiphteriaIncubationTimeDetails(exportDto.getDiphteriaIncubationTimeDetails());
					caseExportDetailedDto.setDiphteriaIndirectFluorescentAntibodyDetails(exportDto.getDiphteriaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiphteriaDirectFluorescentAntibodyDetails(exportDto.getDiphteriaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setDiphteriaMicroscopyDetails(exportDto.getDiphteriaMicroscopyDetails());
					caseExportDetailedDto.setDiphteriaNeutralizingAntibodiesDetails(exportDto.getDiphteriaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setDiphteriaPcrRtPcrDetails(exportDto.getDiphteriaPcrRtPcrDetails());
					caseExportDetailedDto.setDiphteriaGramStainDetails(exportDto.getDiphteriaGramStainDetails());
					caseExportDetailedDto.setDiphteriaLatexAgglutinationDetails(exportDto.getDiphteriaLatexAgglutinationDetails());
					caseExportDetailedDto.setDiphteriaCqValueDetectionDetails(exportDto.getDiphteriaCqValueDetectionDetails());
					caseExportDetailedDto.setDiphteriaSequencingDetails(exportDto.getDiphteriaSequencingDetails());
					caseExportDetailedDto.setDiphteriaDnaMicroarrayDetails(exportDto.getDiphteriaDnaMicroarrayDetails());
					caseExportDetailedDto.setDiphteriaOtherDetails(exportDto.getDiphteriaOtherDetails());
					caseExportDetailedDto.setTrachomaAntibodyDetection(exportDto.getTrachomaAntibodyDetection());
					caseExportDetailedDto.setTrachomaAntigenDetection(exportDto.getTrachomaAntigenDetection());
					caseExportDetailedDto.setTrachomaRapidTest(exportDto.getTrachomaRapidTest());
					caseExportDetailedDto.setTrachomaCulture(exportDto.getTrachomaCulture());
					caseExportDetailedDto.setTrachomaHistopathology(exportDto.getTrachomaHistopathology());
					caseExportDetailedDto.setTrachomaIsolation(exportDto.getTrachomaIsolation());
					caseExportDetailedDto.setTrachomaIgmSerumAntibody(exportDto.getTrachomaIgmSerumAntibody());
					caseExportDetailedDto.setTrachomaIggSerumAntibody(exportDto.getTrachomaIggSerumAntibody());
					caseExportDetailedDto.setTrachomaIgaSerumAntibody(exportDto.getTrachomaIgaSerumAntibody());
					caseExportDetailedDto.setTrachomaIncubationTime(exportDto.getTrachomaIncubationTime());
					caseExportDetailedDto.setTrachomaIndirectFluorescentAntibody(exportDto.getTrachomaIndirectFluorescentAntibody());
					caseExportDetailedDto.setTrachomaDirectFluorescentAntibody(exportDto.getTrachomaDirectFluorescentAntibody());
					caseExportDetailedDto.setTrachomaMicroscopy(exportDto.getTrachomaMicroscopy());
					caseExportDetailedDto.setTrachomaNeutralizingAntibodies(exportDto.getTrachomaNeutralizingAntibodies());
					caseExportDetailedDto.setTrachomaPcrRtPcr(exportDto.getTrachomaPcrRtPcr());
					caseExportDetailedDto.setTrachomaGramStain(exportDto.getTrachomaGramStain());
					caseExportDetailedDto.setTrachomaLatexAgglutination(exportDto.getTrachomaLatexAgglutination());
					caseExportDetailedDto.setTrachomaCqValueDetection(exportDto.getTrachomaCqValueDetection());
					caseExportDetailedDto.setTrachomaSequencing(exportDto.getTrachomaSequencing());
					caseExportDetailedDto.setTrachomaDnaMicroarray(exportDto.getTrachomaDnaMicroarray());
					caseExportDetailedDto.setTrachomaOther(exportDto.getTrachomaOther());
					caseExportDetailedDto.setTrachomaAntibodyDetectionDetails(exportDto.getTrachomaAntibodyDetectionDetails());
					caseExportDetailedDto.setTrachomaAntigenDetectionDetails(exportDto.getTrachomaAntigenDetectionDetails());
					caseExportDetailedDto.setTrachomaRapidTestDetails(exportDto.getTrachomaRapidTestDetails());
					caseExportDetailedDto.setTrachomaCultureDetails(exportDto.getTrachomaCultureDetails());
					caseExportDetailedDto.setTrachomaHistopathologyDetails(exportDto.getTrachomaHistopathologyDetails());
					caseExportDetailedDto.setTrachomaIsolationDetails(exportDto.getTrachomaIsolationDetails());
					caseExportDetailedDto.setTrachomaIgmSerumAntibodyDetails(exportDto.getTrachomaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setTrachomaIggSerumAntibodyDetails(exportDto.getTrachomaIggSerumAntibodyDetails());
					caseExportDetailedDto.setTrachomaIgaSerumAntibodyDetails(exportDto.getTrachomaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setTrachomaIncubationTimeDetails(exportDto.getTrachomaIncubationTimeDetails());
					caseExportDetailedDto.setTrachomaIndirectFluorescentAntibodyDetails(exportDto.getTrachomaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTrachomaDirectFluorescentAntibodyDetails(exportDto.getTrachomaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setTrachomaMicroscopyDetails(exportDto.getTrachomaMicroscopyDetails());
					caseExportDetailedDto.setTrachomaNeutralizingAntibodiesDetails(exportDto.getTrachomaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setTrachomaPcrRtPcrDetails(exportDto.getTrachomaPcrRtPcrDetails());
					caseExportDetailedDto.setTrachomaGramStainDetails(exportDto.getTrachomaGramStainDetails());
					caseExportDetailedDto.setTrachomaLatexAgglutinationDetails(exportDto.getTrachomaLatexAgglutinationDetails());
					caseExportDetailedDto.setTrachomaCqValueDetectionDetails(exportDto.getTrachomaCqValueDetectionDetails());
					caseExportDetailedDto.setTrachomaSequencingDetails(exportDto.getTrachomaSequencingDetails());
					caseExportDetailedDto.setTrachomaDnaMicroarrayDetails(exportDto.getTrachomaDnaMicroarrayDetails());
					caseExportDetailedDto.setTrachomaOtherDetails(exportDto.getTrachomaOtherDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisAntibodyDetection(exportDto.getYawsEndemicSyphilisAntibodyDetection());
					caseExportDetailedDto.setYawsEndemicSyphilisAntigenDetection(exportDto.getYawsEndemicSyphilisAntigenDetection());
					caseExportDetailedDto.setYawsEndemicSyphilisRapidTest(exportDto.getYawsEndemicSyphilisRapidTest());
					caseExportDetailedDto.setYawsEndemicSyphilisCulture(exportDto.getYawsEndemicSyphilisCulture());
					caseExportDetailedDto.setYawsEndemicSyphilisHistopathology(exportDto.getYawsEndemicSyphilisHistopathology());
					caseExportDetailedDto.setYawsEndemicSyphilisIsolation(exportDto.getYawsEndemicSyphilisIsolation());
					caseExportDetailedDto.setYawsEndemicSyphilisIgmSerumAntibody(exportDto.getYawsEndemicSyphilisIgmSerumAntibody());
					caseExportDetailedDto.setYawsEndemicSyphilisIggSerumAntibody(exportDto.getYawsEndemicSyphilisIggSerumAntibody());
					caseExportDetailedDto.setYawsEndemicSyphilisIgaSerumAntibody(exportDto.getYawsEndemicSyphilisIgaSerumAntibody());
					caseExportDetailedDto.setYawsEndemicSyphilisIncubationTime(exportDto.getYawsEndemicSyphilisIncubationTime());
					caseExportDetailedDto.setYawsEndemicSyphilisIndirectFluorescentAntibody(exportDto.getYawsEndemicSyphilisIndirectFluorescentAntibody());
					caseExportDetailedDto.setYawsEndemicSyphilisDirectFluorescentAntibody(exportDto.getYawsEndemicSyphilisDirectFluorescentAntibody());
					caseExportDetailedDto.setYawsEndemicSyphilisMicroscopy(exportDto.getYawsEndemicSyphilisMicroscopy());
					caseExportDetailedDto.setYawsEndemicSyphilisNeutralizingAntibodies(exportDto.getYawsEndemicSyphilisNeutralizingAntibodies());
					caseExportDetailedDto.setYawsEndemicSyphilisPcrRtPcr(exportDto.getYawsEndemicSyphilisPcrRtPcr());
					caseExportDetailedDto.setYawsEndemicSyphilisGramStain(exportDto.getYawsEndemicSyphilisGramStain());
					caseExportDetailedDto.setYawsEndemicSyphilisLatexAgglutination(exportDto.getYawsEndemicSyphilisLatexAgglutination());
					caseExportDetailedDto.setYawsEndemicSyphilisCqValueDetection(exportDto.getYawsEndemicSyphilisCqValueDetection());
					caseExportDetailedDto.setYawsEndemicSyphilisSequencing(exportDto.getYawsEndemicSyphilisSequencing());
					caseExportDetailedDto.setYawsEndemicSyphilisDnaMicroarray(exportDto.getYawsEndemicSyphilisDnaMicroarray());
					caseExportDetailedDto.setYawsEndemicSyphilisOther(exportDto.getYawsEndemicSyphilisOther());
					caseExportDetailedDto.setYawsEndemicSyphilisAntibodyDetectionDetails(exportDto.getYawsEndemicSyphilisAntibodyDetectionDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisAntigenDetectionDetails(exportDto.getYawsEndemicSyphilisAntigenDetectionDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisRapidTestDetails(exportDto.getYawsEndemicSyphilisRapidTestDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisCultureDetails(exportDto.getYawsEndemicSyphilisCultureDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisHistopathologyDetails(exportDto.getYawsEndemicSyphilisHistopathologyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIsolationDetails(exportDto.getYawsEndemicSyphilisIsolationDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIgmSerumAntibodyDetails(exportDto.getYawsEndemicSyphilisIgmSerumAntibodyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIggSerumAntibodyDetails(exportDto.getYawsEndemicSyphilisIggSerumAntibodyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIgaSerumAntibodyDetails(exportDto.getYawsEndemicSyphilisIgaSerumAntibodyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIncubationTimeDetails(exportDto.getYawsEndemicSyphilisIncubationTimeDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisIndirectFluorescentAntibodyDetails(exportDto.getYawsEndemicSyphilisIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisDirectFluorescentAntibodyDetails(exportDto.getYawsEndemicSyphilisDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisMicroscopyDetails(exportDto.getYawsEndemicSyphilisMicroscopyDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisNeutralizingAntibodiesDetails(exportDto.getYawsEndemicSyphilisNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisPcrRtPcrDetails(exportDto.getYawsEndemicSyphilisPcrRtPcrDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisGramStainDetails(exportDto.getYawsEndemicSyphilisGramStainDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisLatexAgglutinationDetails(exportDto.getYawsEndemicSyphilisLatexAgglutinationDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisCqValueDetectionDetails(exportDto.getYawsEndemicSyphilisCqValueDetectionDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisSequencingDetails(exportDto.getYawsEndemicSyphilisSequencingDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisDnaMicroarrayDetails(exportDto.getYawsEndemicSyphilisDnaMicroarrayDetails());
					caseExportDetailedDto.setYawsEndemicSyphilisOtherDetails(exportDto.getYawsEndemicSyphilisOtherDetails());
					caseExportDetailedDto.setMaternalDeathsAntibodyDetection(exportDto.getMaternalDeathsAntibodyDetection());
					caseExportDetailedDto.setMaternalDeathsAntigenDetection(exportDto.getMaternalDeathsAntigenDetection());
					caseExportDetailedDto.setMaternalDeathsRapidTest(exportDto.getMaternalDeathsRapidTest());
					caseExportDetailedDto.setMaternalDeathsCulture(exportDto.getMaternalDeathsCulture());
					caseExportDetailedDto.setMaternalDeathsHistopathology(exportDto.getMaternalDeathsHistopathology());
					caseExportDetailedDto.setMaternalDeathsIsolation(exportDto.getMaternalDeathsIsolation());
					caseExportDetailedDto.setMaternalDeathsIgmSerumAntibody(exportDto.getMaternalDeathsIgmSerumAntibody());
					caseExportDetailedDto.setMaternalDeathsIggSerumAntibody(exportDto.getMaternalDeathsIggSerumAntibody());
					caseExportDetailedDto.setMaternalDeathsIgaSerumAntibody(exportDto.getMaternalDeathsIgaSerumAntibody());
					caseExportDetailedDto.setMaternalDeathsIncubationTime(exportDto.getMaternalDeathsIncubationTime());
					caseExportDetailedDto.setMaternalDeathsIndirectFluorescentAntibody(exportDto.getMaternalDeathsIndirectFluorescentAntibody());
					caseExportDetailedDto.setMaternalDeathsDirectFluorescentAntibody(exportDto.getMaternalDeathsDirectFluorescentAntibody());
					caseExportDetailedDto.setMaternalDeathsMicroscopy(exportDto.getMaternalDeathsMicroscopy());
					caseExportDetailedDto.setMaternalDeathsNeutralizingAntibodies(exportDto.getMaternalDeathsNeutralizingAntibodies());
					caseExportDetailedDto.setMaternalDeathsPcrRtPcr(exportDto.getMaternalDeathsPcrRtPcr());
					caseExportDetailedDto.setMaternalDeathsGramStain(exportDto.getMaternalDeathsGramStain());
					caseExportDetailedDto.setMaternalDeathsLatexAgglutination(exportDto.getMaternalDeathsLatexAgglutination());
					caseExportDetailedDto.setMaternalDeathsCqValueDetection(exportDto.getMaternalDeathsCqValueDetection());
					caseExportDetailedDto.setMaternalDeathsSequencing(exportDto.getMaternalDeathsSequencing());
					caseExportDetailedDto.setMaternalDeathsDnaMicroarray(exportDto.getMaternalDeathsDnaMicroarray());
					caseExportDetailedDto.setMaternalDeathsOther(exportDto.getMaternalDeathsOther());
					caseExportDetailedDto.setMaternalDeathsAntibodyDetectionDetails(exportDto.getMaternalDeathsAntibodyDetectionDetails());
					caseExportDetailedDto.setMaternalDeathsAntigenDetectionDetails(exportDto.getMaternalDeathsAntigenDetectionDetails());
					caseExportDetailedDto.setMaternalDeathsRapidTestDetails(exportDto.getMaternalDeathsRapidTestDetails());
					caseExportDetailedDto.setMaternalDeathsCultureDetails(exportDto.getMaternalDeathsCultureDetails());
					caseExportDetailedDto.setMaternalDeathsHistopathologyDetails(exportDto.getMaternalDeathsHistopathologyDetails());
					caseExportDetailedDto.setMaternalDeathsIsolationDetails(exportDto.getMaternalDeathsIsolationDetails());
					caseExportDetailedDto.setMaternalDeathsIgmSerumAntibodyDetails(exportDto.getMaternalDeathsIgmSerumAntibodyDetails());
					caseExportDetailedDto.setMaternalDeathsIggSerumAntibodyDetails(exportDto.getMaternalDeathsIggSerumAntibodyDetails());
					caseExportDetailedDto.setMaternalDeathsIgaSerumAntibodyDetails(exportDto.getMaternalDeathsIgaSerumAntibodyDetails());
					caseExportDetailedDto.setMaternalDeathsIncubationTimeDetails(exportDto.getMaternalDeathsIncubationTimeDetails());
					caseExportDetailedDto.setMaternalDeathsIndirectFluorescentAntibodyDetails(exportDto.getMaternalDeathsIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMaternalDeathsDirectFluorescentAntibodyDetails(exportDto.getMaternalDeathsDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setMaternalDeathsMicroscopyDetails(exportDto.getMaternalDeathsMicroscopyDetails());
					caseExportDetailedDto.setMaternalDeathsNeutralizingAntibodiesDetails(exportDto.getMaternalDeathsNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setMaternalDeathsPcrRtPcrDetails(exportDto.getMaternalDeathsPcrRtPcrDetails());
					caseExportDetailedDto.setMaternalDeathsGramStainDetails(exportDto.getMaternalDeathsGramStainDetails());
					caseExportDetailedDto.setMaternalDeathsLatexAgglutinationDetails(exportDto.getMaternalDeathsLatexAgglutinationDetails());
					caseExportDetailedDto.setMaternalDeathsCqValueDetectionDetails(exportDto.getMaternalDeathsCqValueDetectionDetails());
					caseExportDetailedDto.setMaternalDeathsSequencingDetails(exportDto.getMaternalDeathsSequencingDetails());
					caseExportDetailedDto.setMaternalDeathsDnaMicroarrayDetails(exportDto.getMaternalDeathsDnaMicroarrayDetails());
					caseExportDetailedDto.setMaternalDeathsOtherDetails(exportDto.getMaternalDeathsOtherDetails());
					caseExportDetailedDto.setPerinatalDeathsAntibodyDetection(exportDto.getPerinatalDeathsAntibodyDetection());
					caseExportDetailedDto.setPerinatalDeathsAntigenDetection(exportDto.getPerinatalDeathsAntigenDetection());
					caseExportDetailedDto.setPerinatalDeathsRapidTest(exportDto.getPerinatalDeathsRapidTest());
					caseExportDetailedDto.setPerinatalDeathsCulture(exportDto.getPerinatalDeathsCulture());
					caseExportDetailedDto.setPerinatalDeathsHistopathology(exportDto.getPerinatalDeathsHistopathology());
					caseExportDetailedDto.setPerinatalDeathsIsolation(exportDto.getPerinatalDeathsIsolation());
					caseExportDetailedDto.setPerinatalDeathsIgmSerumAntibody(exportDto.getPerinatalDeathsIgmSerumAntibody());
					caseExportDetailedDto.setPerinatalDeathsIggSerumAntibody(exportDto.getPerinatalDeathsIggSerumAntibody());
					caseExportDetailedDto.setPerinatalDeathsIgaSerumAntibody(exportDto.getPerinatalDeathsIgaSerumAntibody());
					caseExportDetailedDto.setPerinatalDeathsIncubationTime(exportDto.getPerinatalDeathsIncubationTime());
					caseExportDetailedDto.setPerinatalDeathsIndirectFluorescentAntibody(exportDto.getPerinatalDeathsIndirectFluorescentAntibody());
					caseExportDetailedDto.setPerinatalDeathsDirectFluorescentAntibody(exportDto.getPerinatalDeathsDirectFluorescentAntibody());
					caseExportDetailedDto.setPerinatalDeathsMicroscopy(exportDto.getPerinatalDeathsMicroscopy());
					caseExportDetailedDto.setPerinatalDeathsNeutralizingAntibodies(exportDto.getPerinatalDeathsNeutralizingAntibodies());
					caseExportDetailedDto.setPerinatalDeathsPcrRtPcr(exportDto.getPerinatalDeathsPcrRtPcr());
					caseExportDetailedDto.setPerinatalDeathsGramStain(exportDto.getPerinatalDeathsGramStain());
					caseExportDetailedDto.setPerinatalDeathsLatexAgglutination(exportDto.getPerinatalDeathsLatexAgglutination());
					caseExportDetailedDto.setPerinatalDeathsCqValueDetection(exportDto.getPerinatalDeathsCqValueDetection());
					caseExportDetailedDto.setPerinatalDeathsSequencing(exportDto.getPerinatalDeathsSequencing());
					caseExportDetailedDto.setPerinatalDeathsDnaMicroarray(exportDto.getPerinatalDeathsDnaMicroarray());
					caseExportDetailedDto.setPerinatalDeathsOther(exportDto.getPerinatalDeathsOther());
					caseExportDetailedDto.setPerinatalDeathsAntibodyDetectionDetails(exportDto.getPerinatalDeathsAntibodyDetectionDetails());
					caseExportDetailedDto.setPerinatalDeathsAntigenDetectionDetails(exportDto.getPerinatalDeathsAntigenDetectionDetails());
					caseExportDetailedDto.setPerinatalDeathsRapidTestDetails(exportDto.getPerinatalDeathsRapidTestDetails());
					caseExportDetailedDto.setPerinatalDeathsCultureDetails(exportDto.getPerinatalDeathsCultureDetails());
					caseExportDetailedDto.setPerinatalDeathsHistopathologyDetails(exportDto.getPerinatalDeathsHistopathologyDetails());
					caseExportDetailedDto.setPerinatalDeathsIsolationDetails(exportDto.getPerinatalDeathsIsolationDetails());
					caseExportDetailedDto.setPerinatalDeathsIgmSerumAntibodyDetails(exportDto.getPerinatalDeathsIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPerinatalDeathsIggSerumAntibodyDetails(exportDto.getPerinatalDeathsIggSerumAntibodyDetails());
					caseExportDetailedDto.setPerinatalDeathsIgaSerumAntibodyDetails(exportDto.getPerinatalDeathsIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPerinatalDeathsIncubationTimeDetails(exportDto.getPerinatalDeathsIncubationTimeDetails());
					caseExportDetailedDto.setPerinatalDeathsIndirectFluorescentAntibodyDetails(exportDto.getPerinatalDeathsIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPerinatalDeathsDirectFluorescentAntibodyDetails(exportDto.getPerinatalDeathsDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPerinatalDeathsMicroscopyDetails(exportDto.getPerinatalDeathsMicroscopyDetails());
					caseExportDetailedDto.setPerinatalDeathsNeutralizingAntibodiesDetails(exportDto.getPerinatalDeathsNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPerinatalDeathsPcrRtPcrDetails(exportDto.getPerinatalDeathsPcrRtPcrDetails());
					caseExportDetailedDto.setPerinatalDeathsGramStainDetails(exportDto.getPerinatalDeathsGramStainDetails());
					caseExportDetailedDto.setPerinatalDeathsLatexAgglutinationDetails(exportDto.getPerinatalDeathsLatexAgglutinationDetails());
					caseExportDetailedDto.setPerinatalDeathsCqValueDetectionDetails(exportDto.getPerinatalDeathsCqValueDetectionDetails());
					caseExportDetailedDto.setPerinatalDeathsSequencingDetails(exportDto.getPerinatalDeathsSequencingDetails());
					caseExportDetailedDto.setPerinatalDeathsDnaMicroarrayDetails(exportDto.getPerinatalDeathsDnaMicroarrayDetails());
					caseExportDetailedDto.setPerinatalDeathsOtherDetails(exportDto.getPerinatalDeathsOtherDetails());
					caseExportDetailedDto.setInfluenzaAAntibodyDetection(exportDto.getInfluenzaAAntibodyDetection());
					caseExportDetailedDto.setInfluenzaAAntigenDetection(exportDto.getInfluenzaAAntigenDetection());
					caseExportDetailedDto.setInfluenzaARapidTest(exportDto.getInfluenzaARapidTest());
					caseExportDetailedDto.setInfluenzaACulture(exportDto.getInfluenzaACulture());
					caseExportDetailedDto.setInfluenzaAHistopathology(exportDto.getInfluenzaAHistopathology());
					caseExportDetailedDto.setInfluenzaAIsolation(exportDto.getInfluenzaAIsolation());
					caseExportDetailedDto.setInfluenzaAIgmSerumAntibody(exportDto.getInfluenzaAIgmSerumAntibody());
					caseExportDetailedDto.setInfluenzaAIggSerumAntibody(exportDto.getInfluenzaAIggSerumAntibody());
					caseExportDetailedDto.setInfluenzaAIgaSerumAntibody(exportDto.getInfluenzaAIgaSerumAntibody());
					caseExportDetailedDto.setInfluenzaAIncubationTime(exportDto.getInfluenzaAIncubationTime());
					caseExportDetailedDto.setInfluenzaAIndirectFluorescentAntibody(exportDto.getInfluenzaAIndirectFluorescentAntibody());
					caseExportDetailedDto.setInfluenzaADirectFluorescentAntibody(exportDto.getInfluenzaADirectFluorescentAntibody());
					caseExportDetailedDto.setInfluenzaAMicroscopy(exportDto.getInfluenzaAMicroscopy());
					caseExportDetailedDto.setInfluenzaANeutralizingAntibodies(exportDto.getInfluenzaANeutralizingAntibodies());
					caseExportDetailedDto.setInfluenzaAPcrRtPcr(exportDto.getInfluenzaAPcrRtPcr());
					caseExportDetailedDto.setInfluenzaAGramStain(exportDto.getInfluenzaAGramStain());
					caseExportDetailedDto.setInfluenzaALatexAgglutination(exportDto.getInfluenzaALatexAgglutination());
					caseExportDetailedDto.setInfluenzaACqValueDetection(exportDto.getInfluenzaACqValueDetection());
					caseExportDetailedDto.setInfluenzaASequencing(exportDto.getInfluenzaASequencing());
					caseExportDetailedDto.setInfluenzaADnaMicroarray(exportDto.getInfluenzaADnaMicroarray());
					caseExportDetailedDto.setInfluenzaAOther(exportDto.getInfluenzaAOther());
					caseExportDetailedDto.setInfluenzaAAntibodyDetectionDetails(exportDto.getInfluenzaAAntibodyDetectionDetails());
					caseExportDetailedDto.setInfluenzaAAntigenDetectionDetails(exportDto.getInfluenzaAAntigenDetectionDetails());
					caseExportDetailedDto.setInfluenzaARapidTestDetails(exportDto.getInfluenzaARapidTestDetails());
					caseExportDetailedDto.setInfluenzaACultureDetails(exportDto.getInfluenzaACultureDetails());
					caseExportDetailedDto.setInfluenzaAHistopathologyDetails(exportDto.getInfluenzaAHistopathologyDetails());
					caseExportDetailedDto.setInfluenzaAIsolationDetails(exportDto.getInfluenzaAIsolationDetails());
					caseExportDetailedDto.setInfluenzaAIgmSerumAntibodyDetails(exportDto.getInfluenzaAIgmSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaAIggSerumAntibodyDetails(exportDto.getInfluenzaAIggSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaAIgaSerumAntibodyDetails(exportDto.getInfluenzaAIgaSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaAIncubationTimeDetails(exportDto.getInfluenzaAIncubationTimeDetails());
					caseExportDetailedDto.setInfluenzaAIndirectFluorescentAntibodyDetails(exportDto.getInfluenzaAIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setInfluenzaADirectFluorescentAntibodyDetails(exportDto.getInfluenzaADirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setInfluenzaAMicroscopyDetails(exportDto.getInfluenzaAMicroscopyDetails());
					caseExportDetailedDto.setInfluenzaANeutralizingAntibodiesDetails(exportDto.getInfluenzaANeutralizingAntibodiesDetails());
					caseExportDetailedDto.setInfluenzaAPcrRtPcrDetails(exportDto.getInfluenzaAPcrRtPcrDetails());
					caseExportDetailedDto.setInfluenzaAGramStainDetails(exportDto.getInfluenzaAGramStainDetails());
					caseExportDetailedDto.setInfluenzaALatexAgglutinationDetails(exportDto.getInfluenzaALatexAgglutinationDetails());
					caseExportDetailedDto.setInfluenzaACqValueDetectionDetails(exportDto.getInfluenzaACqValueDetectionDetails());
					caseExportDetailedDto.setInfluenzaASequencingDetails(exportDto.getInfluenzaASequencingDetails());
					caseExportDetailedDto.setInfluenzaADnaMicroarrayDetails(exportDto.getInfluenzaADnaMicroarrayDetails());
					caseExportDetailedDto.setInfluenzaAOtherDetails(exportDto.getInfluenzaAOtherDetails());
					caseExportDetailedDto.setInfluenzaBAntibodyDetection(exportDto.getInfluenzaBAntibodyDetection());
					caseExportDetailedDto.setInfluenzaBAntigenDetection(exportDto.getInfluenzaBAntigenDetection());
					caseExportDetailedDto.setInfluenzaBRapidTest(exportDto.getInfluenzaBRapidTest());
					caseExportDetailedDto.setInfluenzaBCulture(exportDto.getInfluenzaBCulture());
					caseExportDetailedDto.setInfluenzaBHistopathology(exportDto.getInfluenzaBHistopathology());
					caseExportDetailedDto.setInfluenzaBIsolation(exportDto.getInfluenzaBIsolation());
					caseExportDetailedDto.setInfluenzaBIgmSerumAntibody(exportDto.getInfluenzaBIgmSerumAntibody());
					caseExportDetailedDto.setInfluenzaBIggSerumAntibody(exportDto.getInfluenzaBIggSerumAntibody());
					caseExportDetailedDto.setInfluenzaBIgaSerumAntibody(exportDto.getInfluenzaBIgaSerumAntibody());
					caseExportDetailedDto.setInfluenzaBIncubationTime(exportDto.getInfluenzaBIncubationTime());
					caseExportDetailedDto.setInfluenzaBIndirectFluorescentAntibody(exportDto.getInfluenzaBIndirectFluorescentAntibody());
					caseExportDetailedDto.setInfluenzaBDirectFluorescentAntibody(exportDto.getInfluenzaBDirectFluorescentAntibody());
					caseExportDetailedDto.setInfluenzaBMicroscopy(exportDto.getInfluenzaBMicroscopy());
					caseExportDetailedDto.setInfluenzaBNeutralizingAntibodies(exportDto.getInfluenzaBNeutralizingAntibodies());
					caseExportDetailedDto.setInfluenzaBPcrRtPcr(exportDto.getInfluenzaBPcrRtPcr());
					caseExportDetailedDto.setInfluenzaBGramStain(exportDto.getInfluenzaBGramStain());
					caseExportDetailedDto.setInfluenzaBLatexAgglutination(exportDto.getInfluenzaBLatexAgglutination());
					caseExportDetailedDto.setInfluenzaBCqValueDetection(exportDto.getInfluenzaBCqValueDetection());
					caseExportDetailedDto.setInfluenzaBSequencing(exportDto.getInfluenzaBSequencing());
					caseExportDetailedDto.setInfluenzaBDnaMicroarray(exportDto.getInfluenzaBDnaMicroarray());
					caseExportDetailedDto.setInfluenzaBOther(exportDto.getInfluenzaBOther());
					caseExportDetailedDto.setInfluenzaBAntibodyDetectionDetails(exportDto.getInfluenzaBAntibodyDetectionDetails());
					caseExportDetailedDto.setInfluenzaBAntigenDetectionDetails(exportDto.getInfluenzaBAntigenDetectionDetails());
					caseExportDetailedDto.setInfluenzaBRapidTestDetails(exportDto.getInfluenzaBRapidTestDetails());
					caseExportDetailedDto.setInfluenzaBCultureDetails(exportDto.getInfluenzaBCultureDetails());
					caseExportDetailedDto.setInfluenzaBHistopathologyDetails(exportDto.getInfluenzaBHistopathologyDetails());
					caseExportDetailedDto.setInfluenzaBIsolationDetails(exportDto.getInfluenzaBIsolationDetails());
					caseExportDetailedDto.setInfluenzaBIgmSerumAntibodyDetails(exportDto.getInfluenzaBIgmSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaBIggSerumAntibodyDetails(exportDto.getInfluenzaBIggSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaBIgaSerumAntibodyDetails(exportDto.getInfluenzaBIgaSerumAntibodyDetails());
					caseExportDetailedDto.setInfluenzaBIncubationTimeDetails(exportDto.getInfluenzaBIncubationTimeDetails());
					caseExportDetailedDto.setInfluenzaBIndirectFluorescentAntibodyDetails(exportDto.getInfluenzaBIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setInfluenzaBDirectFluorescentAntibodyDetails(exportDto.getInfluenzaBDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setInfluenzaBMicroscopyDetails(exportDto.getInfluenzaBMicroscopyDetails());
					caseExportDetailedDto.setInfluenzaBNeutralizingAntibodiesDetails(exportDto.getInfluenzaBNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setInfluenzaBPcrRtPcrDetails(exportDto.getInfluenzaBPcrRtPcrDetails());
					caseExportDetailedDto.setInfluenzaBGramStainDetails(exportDto.getInfluenzaBGramStainDetails());
					caseExportDetailedDto.setInfluenzaBLatexAgglutinationDetails(exportDto.getInfluenzaBLatexAgglutinationDetails());
					caseExportDetailedDto.setInfluenzaBCqValueDetectionDetails(exportDto.getInfluenzaBCqValueDetectionDetails());
					caseExportDetailedDto.setInfluenzaBSequencingDetails(exportDto.getInfluenzaBSequencingDetails());
					caseExportDetailedDto.setInfluenzaBDnaMicroarrayDetails(exportDto.getInfluenzaBDnaMicroarrayDetails());
					caseExportDetailedDto.setInfluenzaBOtherDetails(exportDto.getInfluenzaBOtherDetails());
					caseExportDetailedDto.sethMetapneumovirusAntibodyDetection(exportDto.getHMetapneumovirusAntibodyDetection());
					caseExportDetailedDto.sethMetapneumovirusAntigenDetection(exportDto.getHMetapneumovirusAntigenDetection());
					caseExportDetailedDto.sethMetapneumovirusRapidTest(exportDto.getHMetapneumovirusRapidTest());
					caseExportDetailedDto.sethMetapneumovirusCulture(exportDto.getHMetapneumovirusCulture());
					caseExportDetailedDto.sethMetapneumovirusHistopathology(exportDto.getHMetapneumovirusHistopathology());
					caseExportDetailedDto.sethMetapneumovirusIsolation(exportDto.getHMetapneumovirusIsolation());
					caseExportDetailedDto.sethMetapneumovirusIgmSerumAntibody(exportDto.getHMetapneumovirusIgmSerumAntibody());
					caseExportDetailedDto.sethMetapneumovirusIggSerumAntibody(exportDto.getHMetapneumovirusIggSerumAntibody());
					caseExportDetailedDto.sethMetapneumovirusIgaSerumAntibody(exportDto.getHMetapneumovirusIgaSerumAntibody());
					caseExportDetailedDto.sethMetapneumovirusIncubationTime(exportDto.getHMetapneumovirusIncubationTime());
					caseExportDetailedDto.sethMetapneumovirusIndirectFluorescentAntibody(exportDto.getHMetapneumovirusIndirectFluorescentAntibody());
					caseExportDetailedDto.sethMetapneumovirusDirectFluorescentAntibody(exportDto.getHMetapneumovirusDirectFluorescentAntibody());
					caseExportDetailedDto.sethMetapneumovirusMicroscopy(exportDto.getHMetapneumovirusMicroscopy());
					caseExportDetailedDto.sethMetapneumovirusNeutralizingAntibodies(exportDto.getHMetapneumovirusNeutralizingAntibodies());
					caseExportDetailedDto.sethMetapneumovirusPcrRtPcr(exportDto.getHMetapneumovirusPcrRtPcr());
					caseExportDetailedDto.sethMetapneumovirusGramStain(exportDto.getHMetapneumovirusGramStain());
					caseExportDetailedDto.sethMetapneumovirusLatexAgglutination(exportDto.getHMetapneumovirusLatexAgglutination());
					caseExportDetailedDto.sethMetapneumovirusCqValueDetection(exportDto.getHMetapneumovirusCqValueDetection());
					caseExportDetailedDto.sethMetapneumovirusSequencing(exportDto.getHMetapneumovirusSequencing());
					caseExportDetailedDto.sethMetapneumovirusDnaMicroarray(exportDto.getHMetapneumovirusDnaMicroarray());
					caseExportDetailedDto.sethMetapneumovirusOther(exportDto.getHMetapneumovirusOther());
					caseExportDetailedDto.sethMetapneumovirusAntibodyDetectionDetails(exportDto.getHMetapneumovirusAntibodyDetectionDetails());
					caseExportDetailedDto.sethMetapneumovirusAntigenDetectionDetails(exportDto.getHMetapneumovirusAntigenDetectionDetails());
					caseExportDetailedDto.sethMetapneumovirusRapidTestDetails(exportDto.getHMetapneumovirusRapidTestDetails());
					caseExportDetailedDto.sethMetapneumovirusCultureDetails(exportDto.getHMetapneumovirusCultureDetails());
					caseExportDetailedDto.sethMetapneumovirusHistopathologyDetails(exportDto.getHMetapneumovirusHistopathologyDetails());
					caseExportDetailedDto.sethMetapneumovirusIsolationDetails(exportDto.getHMetapneumovirusIsolationDetails());
					caseExportDetailedDto.sethMetapneumovirusIgmSerumAntibodyDetails(exportDto.getHMetapneumovirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.sethMetapneumovirusIggSerumAntibodyDetails(exportDto.getHMetapneumovirusIggSerumAntibodyDetails());
					caseExportDetailedDto.sethMetapneumovirusIgaSerumAntibodyDetails(exportDto.getHMetapneumovirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.sethMetapneumovirusIncubationTimeDetails(exportDto.getHMetapneumovirusIncubationTimeDetails());
					caseExportDetailedDto.sethMetapneumovirusIndirectFluorescentAntibodyDetails(exportDto.getHMetapneumovirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.sethMetapneumovirusDirectFluorescentAntibodyDetails(exportDto.getHMetapneumovirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.sethMetapneumovirusMicroscopyDetails(exportDto.getHMetapneumovirusMicroscopyDetails());
					caseExportDetailedDto.sethMetapneumovirusNeutralizingAntibodiesDetails(exportDto.getHMetapneumovirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.sethMetapneumovirusPcrRtPcrDetails(exportDto.getHMetapneumovirusPcrRtPcrDetails());
					caseExportDetailedDto.sethMetapneumovirusGramStainDetails(exportDto.getHMetapneumovirusGramStainDetails());
					caseExportDetailedDto.sethMetapneumovirusLatexAgglutinationDetails(exportDto.getHMetapneumovirusLatexAgglutinationDetails());
					caseExportDetailedDto.sethMetapneumovirusCqValueDetectionDetails(exportDto.getHMetapneumovirusCqValueDetectionDetails());
					caseExportDetailedDto.sethMetapneumovirusSequencingDetails(exportDto.getHMetapneumovirusSequencingDetails());
					caseExportDetailedDto.sethMetapneumovirusDnaMicroarrayDetails(exportDto.getHMetapneumovirusDnaMicroarrayDetails());
					caseExportDetailedDto.sethMetapneumovirusOtherDetails(exportDto.getHMetapneumovirusOtherDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusAntibodyDetection(exportDto.getRespiratorySyncytialVirusAntibodyDetection());
					caseExportDetailedDto.setRespiratorySyncytialVirusAntigenDetection(exportDto.getRespiratorySyncytialVirusAntigenDetection());
					caseExportDetailedDto.setRespiratorySyncytialVirusRapidTest(exportDto.getRespiratorySyncytialVirusRapidTest());
					caseExportDetailedDto.setRespiratorySyncytialVirusCulture(exportDto.getRespiratorySyncytialVirusCulture());
					caseExportDetailedDto.setRespiratorySyncytialVirusHistopathology(exportDto.getRespiratorySyncytialVirusHistopathology());
					caseExportDetailedDto.setRespiratorySyncytialVirusIsolation(exportDto.getRespiratorySyncytialVirusIsolation());
					caseExportDetailedDto.setRespiratorySyncytialVirusIgmSerumAntibody(exportDto.getRespiratorySyncytialVirusIgmSerumAntibody());
					caseExportDetailedDto.setRespiratorySyncytialVirusIggSerumAntibody(exportDto.getRespiratorySyncytialVirusIggSerumAntibody());
					caseExportDetailedDto.setRespiratorySyncytialVirusIgaSerumAntibody(exportDto.getRespiratorySyncytialVirusIgaSerumAntibody());
					caseExportDetailedDto.setRespiratorySyncytialVirusIncubationTime(exportDto.getRespiratorySyncytialVirusIncubationTime());
					caseExportDetailedDto.setRespiratorySyncytialVirusIndirectFluorescentAntibody(exportDto.getRespiratorySyncytialVirusIndirectFluorescentAntibody());
					caseExportDetailedDto.setRespiratorySyncytialVirusDirectFluorescentAntibody(exportDto.getRespiratorySyncytialVirusDirectFluorescentAntibody());
					caseExportDetailedDto.setRespiratorySyncytialVirusMicroscopy(exportDto.getRespiratorySyncytialVirusMicroscopy());
					caseExportDetailedDto.setRespiratorySyncytialVirusNeutralizingAntibodies(exportDto.getRespiratorySyncytialVirusNeutralizingAntibodies());
					caseExportDetailedDto.setRespiratorySyncytialVirusPcrRtPcr(exportDto.getRespiratorySyncytialVirusPcrRtPcr());
					caseExportDetailedDto.setRespiratorySyncytialVirusGramStain(exportDto.getRespiratorySyncytialVirusGramStain());
					caseExportDetailedDto.setRespiratorySyncytialVirusLatexAgglutination(exportDto.getRespiratorySyncytialVirusLatexAgglutination());
					caseExportDetailedDto.setRespiratorySyncytialVirusCqValueDetection(exportDto.getRespiratorySyncytialVirusCqValueDetection());
					caseExportDetailedDto.setRespiratorySyncytialVirusSequencing(exportDto.getRespiratorySyncytialVirusSequencing());
					caseExportDetailedDto.setRespiratorySyncytialVirusDnaMicroarray(exportDto.getRespiratorySyncytialVirusDnaMicroarray());
					caseExportDetailedDto.setRespiratorySyncytialVirusOther(exportDto.getRespiratorySyncytialVirusOther());
					caseExportDetailedDto.setRespiratorySyncytialVirusAntibodyDetectionDetails(exportDto.getRespiratorySyncytialVirusAntibodyDetectionDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusAntigenDetectionDetails(exportDto.getRespiratorySyncytialVirusAntigenDetectionDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusRapidTestDetails(exportDto.getRespiratorySyncytialVirusRapidTestDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusCultureDetails(exportDto.getRespiratorySyncytialVirusCultureDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusHistopathologyDetails(exportDto.getRespiratorySyncytialVirusHistopathologyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIsolationDetails(exportDto.getRespiratorySyncytialVirusIsolationDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIgmSerumAntibodyDetails(exportDto.getRespiratorySyncytialVirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIggSerumAntibodyDetails(exportDto.getRespiratorySyncytialVirusIggSerumAntibodyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIgaSerumAntibodyDetails(exportDto.getRespiratorySyncytialVirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIncubationTimeDetails(exportDto.getRespiratorySyncytialVirusIncubationTimeDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails(exportDto.getRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusDirectFluorescentAntibodyDetails(exportDto.getRespiratorySyncytialVirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusMicroscopyDetails(exportDto.getRespiratorySyncytialVirusMicroscopyDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusNeutralizingAntibodiesDetails(exportDto.getRespiratorySyncytialVirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusPcrRtPcrDetails(exportDto.getRespiratorySyncytialVirusPcrRtPcrDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusGramStainDetails(exportDto.getRespiratorySyncytialVirusGramStainDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusLatexAgglutinationDetails(exportDto.getRespiratorySyncytialVirusLatexAgglutinationDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusCqValueDetectionDetails(exportDto.getRespiratorySyncytialVirusCqValueDetectionDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusSequencingDetails(exportDto.getRespiratorySyncytialVirusSequencingDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusDnaMicroarrayDetails(exportDto.getRespiratorySyncytialVirusDnaMicroarrayDetails());
					caseExportDetailedDto.setRespiratorySyncytialVirusOtherDetails(exportDto.getRespiratorySyncytialVirusOtherDetails());
					caseExportDetailedDto.setParainfluenzaAntibodyDetection(exportDto.getParainfluenzaAntibodyDetection());
					caseExportDetailedDto.setParainfluenzaAntigenDetection(exportDto.getParainfluenzaAntigenDetection());
					caseExportDetailedDto.setParainfluenzaRapidTest(exportDto.getParainfluenzaRapidTest());
					caseExportDetailedDto.setParainfluenzaCulture(exportDto.getParainfluenzaCulture());
					caseExportDetailedDto.setParainfluenzaHistopathology(exportDto.getParainfluenzaHistopathology());
					caseExportDetailedDto.setParainfluenzaIsolation(exportDto.getParainfluenzaIsolation());
					caseExportDetailedDto.setParainfluenzaIgmSerumAntibody(exportDto.getParainfluenzaIgmSerumAntibody());
					caseExportDetailedDto.setParainfluenzaIggSerumAntibody(exportDto.getParainfluenzaIggSerumAntibody());
					caseExportDetailedDto.setParainfluenzaIgaSerumAntibody(exportDto.getParainfluenzaIgaSerumAntibody());
					caseExportDetailedDto.setParainfluenzaIncubationTime(exportDto.getParainfluenzaIncubationTime());
					caseExportDetailedDto.setParainfluenzaIndirectFluorescentAntibody(exportDto.getParainfluenzaIndirectFluorescentAntibody());
					caseExportDetailedDto.setParainfluenzaDirectFluorescentAntibody(exportDto.getParainfluenzaDirectFluorescentAntibody());
					caseExportDetailedDto.setParainfluenzaMicroscopy(exportDto.getParainfluenzaMicroscopy());
					caseExportDetailedDto.setParainfluenzaNeutralizingAntibodies(exportDto.getParainfluenzaNeutralizingAntibodies());
					caseExportDetailedDto.setParainfluenzaPcrRtPcr(exportDto.getParainfluenzaPcrRtPcr());
					caseExportDetailedDto.setParainfluenzaGramStain(exportDto.getParainfluenzaGramStain());
					caseExportDetailedDto.setParainfluenzaLatexAgglutination(exportDto.getParainfluenzaLatexAgglutination());
					caseExportDetailedDto.setParainfluenzaCqValueDetection(exportDto.getParainfluenzaCqValueDetection());
					caseExportDetailedDto.setParainfluenzaSequencing(exportDto.getParainfluenzaSequencing());
					caseExportDetailedDto.setParainfluenzaDnaMicroarray(exportDto.getParainfluenzaDnaMicroarray());
					caseExportDetailedDto.setParainfluenzaOther(exportDto.getParainfluenzaOther());
					caseExportDetailedDto.setParainfluenzaAntibodyDetectionDetails(exportDto.getParainfluenzaAntibodyDetectionDetails());
					caseExportDetailedDto.setParainfluenzaAntigenDetectionDetails(exportDto.getParainfluenzaAntigenDetectionDetails());
					caseExportDetailedDto.setParainfluenzaRapidTestDetails(exportDto.getParainfluenzaRapidTestDetails());
					caseExportDetailedDto.setParainfluenzaCultureDetails(exportDto.getParainfluenzaCultureDetails());
					caseExportDetailedDto.setParainfluenzaHistopathologyDetails(exportDto.getParainfluenzaHistopathologyDetails());
					caseExportDetailedDto.setParainfluenzaIsolationDetails(exportDto.getParainfluenzaIsolationDetails());
					caseExportDetailedDto.setParainfluenzaIgmSerumAntibodyDetails(exportDto.getParainfluenzaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setParainfluenzaIggSerumAntibodyDetails(exportDto.getParainfluenzaIggSerumAntibodyDetails());
					caseExportDetailedDto.setParainfluenzaIgaSerumAntibodyDetails(exportDto.getParainfluenzaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setParainfluenzaIncubationTimeDetails(exportDto.getParainfluenzaIncubationTimeDetails());
					caseExportDetailedDto.setParainfluenzaIndirectFluorescentAntibodyDetails(exportDto.getParainfluenzaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setParainfluenzaDirectFluorescentAntibodyDetails(exportDto.getParainfluenzaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setParainfluenzaMicroscopyDetails(exportDto.getParainfluenzaMicroscopyDetails());
					caseExportDetailedDto.setParainfluenzaNeutralizingAntibodiesDetails(exportDto.getParainfluenzaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setParainfluenzaPcrRtPcrDetails(exportDto.getParainfluenzaPcrRtPcrDetails());
					caseExportDetailedDto.setParainfluenzaGramStainDetails(exportDto.getParainfluenzaGramStainDetails());
					caseExportDetailedDto.setParainfluenzaLatexAgglutinationDetails(exportDto.getParainfluenzaLatexAgglutinationDetails());
					caseExportDetailedDto.setParainfluenzaCqValueDetectionDetails(exportDto.getParainfluenzaCqValueDetectionDetails());
					caseExportDetailedDto.setParainfluenzaSequencingDetails(exportDto.getParainfluenzaSequencingDetails());
					caseExportDetailedDto.setParainfluenzaDnaMicroarrayDetails(exportDto.getParainfluenzaDnaMicroarrayDetails());
					caseExportDetailedDto.setParainfluenzaOtherDetails(exportDto.getParainfluenzaOtherDetails());
					caseExportDetailedDto.setAdenovirusAntibodyDetection(exportDto.getAdenovirusAntibodyDetection());
					caseExportDetailedDto.setAdenovirusAntigenDetection(exportDto.getAdenovirusAntigenDetection());
					caseExportDetailedDto.setAdenovirusRapidTest(exportDto.getAdenovirusRapidTest());
					caseExportDetailedDto.setAdenovirusCulture(exportDto.getAdenovirusCulture());
					caseExportDetailedDto.setAdenovirusHistopathology(exportDto.getAdenovirusHistopathology());
					caseExportDetailedDto.setAdenovirusIsolation(exportDto.getAdenovirusIsolation());
					caseExportDetailedDto.setAdenovirusIgmSerumAntibody(exportDto.getAdenovirusIgmSerumAntibody());
					caseExportDetailedDto.setAdenovirusIggSerumAntibody(exportDto.getAdenovirusIggSerumAntibody());
					caseExportDetailedDto.setAdenovirusIgaSerumAntibody(exportDto.getAdenovirusIgaSerumAntibody());
					caseExportDetailedDto.setAdenovirusIncubationTime(exportDto.getAdenovirusIncubationTime());
					caseExportDetailedDto.setAdenovirusIndirectFluorescentAntibody(exportDto.getAdenovirusIndirectFluorescentAntibody());
					caseExportDetailedDto.setAdenovirusDirectFluorescentAntibody(exportDto.getAdenovirusDirectFluorescentAntibody());
					caseExportDetailedDto.setAdenovirusMicroscopy(exportDto.getAdenovirusMicroscopy());
					caseExportDetailedDto.setAdenovirusNeutralizingAntibodies(exportDto.getAdenovirusNeutralizingAntibodies());
					caseExportDetailedDto.setAdenovirusPcrRtPcr(exportDto.getAdenovirusPcrRtPcr());
					caseExportDetailedDto.setAdenovirusGramStain(exportDto.getAdenovirusGramStain());
					caseExportDetailedDto.setAdenovirusLatexAgglutination(exportDto.getAdenovirusLatexAgglutination());
					caseExportDetailedDto.setAdenovirusCqValueDetection(exportDto.getAdenovirusCqValueDetection());
					caseExportDetailedDto.setAdenovirusSequencing(exportDto.getAdenovirusSequencing());
					caseExportDetailedDto.setAdenovirusDnaMicroarray(exportDto.getAdenovirusDnaMicroarray());
					caseExportDetailedDto.setAdenovirusOther(exportDto.getAdenovirusOther());
					caseExportDetailedDto.setAdenovirusAntibodyDetectionDetails(exportDto.getAdenovirusAntibodyDetectionDetails());
					caseExportDetailedDto.setAdenovirusAntigenDetectionDetails(exportDto.getAdenovirusAntigenDetectionDetails());
					caseExportDetailedDto.setAdenovirusRapidTestDetails(exportDto.getAdenovirusRapidTestDetails());
					caseExportDetailedDto.setAdenovirusCultureDetails(exportDto.getAdenovirusCultureDetails());
					caseExportDetailedDto.setAdenovirusHistopathologyDetails(exportDto.getAdenovirusHistopathologyDetails());
					caseExportDetailedDto.setAdenovirusIsolationDetails(exportDto.getAdenovirusIsolationDetails());
					caseExportDetailedDto.setAdenovirusIgmSerumAntibodyDetails(exportDto.getAdenovirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setAdenovirusIggSerumAntibodyDetails(exportDto.getAdenovirusIggSerumAntibodyDetails());
					caseExportDetailedDto.setAdenovirusIgaSerumAntibodyDetails(exportDto.getAdenovirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setAdenovirusIncubationTimeDetails(exportDto.getAdenovirusIncubationTimeDetails());
					caseExportDetailedDto.setAdenovirusIndirectFluorescentAntibodyDetails(exportDto.getAdenovirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAdenovirusDirectFluorescentAntibodyDetails(exportDto.getAdenovirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAdenovirusMicroscopyDetails(exportDto.getAdenovirusMicroscopyDetails());
					caseExportDetailedDto.setAdenovirusNeutralizingAntibodiesDetails(exportDto.getAdenovirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setAdenovirusPcrRtPcrDetails(exportDto.getAdenovirusPcrRtPcrDetails());
					caseExportDetailedDto.setAdenovirusGramStainDetails(exportDto.getAdenovirusGramStainDetails());
					caseExportDetailedDto.setAdenovirusLatexAgglutinationDetails(exportDto.getAdenovirusLatexAgglutinationDetails());
					caseExportDetailedDto.setAdenovirusCqValueDetectionDetails(exportDto.getAdenovirusCqValueDetectionDetails());
					caseExportDetailedDto.setAdenovirusSequencingDetails(exportDto.getAdenovirusSequencingDetails());
					caseExportDetailedDto.setAdenovirusDnaMicroarrayDetails(exportDto.getAdenovirusDnaMicroarrayDetails());
					caseExportDetailedDto.setAdenovirusOtherDetails(exportDto.getAdenovirusOtherDetails());
					caseExportDetailedDto.setRhinovirusAntibodyDetection(exportDto.getRhinovirusAntibodyDetection());
					caseExportDetailedDto.setRhinovirusAntigenDetection(exportDto.getRhinovirusAntigenDetection());
					caseExportDetailedDto.setRhinovirusRapidTest(exportDto.getRhinovirusRapidTest());
					caseExportDetailedDto.setRhinovirusCulture(exportDto.getRhinovirusCulture());
					caseExportDetailedDto.setRhinovirusHistopathology(exportDto.getRhinovirusHistopathology());
					caseExportDetailedDto.setRhinovirusIsolation(exportDto.getRhinovirusIsolation());
					caseExportDetailedDto.setRhinovirusIgmSerumAntibody(exportDto.getRhinovirusIgmSerumAntibody());
					caseExportDetailedDto.setRhinovirusIggSerumAntibody(exportDto.getRhinovirusIggSerumAntibody());
					caseExportDetailedDto.setRhinovirusIgaSerumAntibody(exportDto.getRhinovirusIgaSerumAntibody());
					caseExportDetailedDto.setRhinovirusIncubationTime(exportDto.getRhinovirusIncubationTime());
					caseExportDetailedDto.setRhinovirusIndirectFluorescentAntibody(exportDto.getRhinovirusIndirectFluorescentAntibody());
					caseExportDetailedDto.setRhinovirusDirectFluorescentAntibody(exportDto.getRhinovirusDirectFluorescentAntibody());
					caseExportDetailedDto.setRhinovirusMicroscopy(exportDto.getRhinovirusMicroscopy());
					caseExportDetailedDto.setRhinovirusNeutralizingAntibodies(exportDto.getRhinovirusNeutralizingAntibodies());
					caseExportDetailedDto.setRhinovirusPcrRtPcr(exportDto.getRhinovirusPcrRtPcr());
					caseExportDetailedDto.setRhinovirusGramStain(exportDto.getRhinovirusGramStain());
					caseExportDetailedDto.setRhinovirusLatexAgglutination(exportDto.getRhinovirusLatexAgglutination());
					caseExportDetailedDto.setRhinovirusCqValueDetection(exportDto.getRhinovirusCqValueDetection());
					caseExportDetailedDto.setRhinovirusSequencing(exportDto.getRhinovirusSequencing());
					caseExportDetailedDto.setRhinovirusDnaMicroarray(exportDto.getRhinovirusDnaMicroarray());
					caseExportDetailedDto.setRhinovirusOther(exportDto.getRhinovirusOther());
					caseExportDetailedDto.setRhinovirusAntibodyDetectionDetails(exportDto.getRhinovirusAntibodyDetectionDetails());
					caseExportDetailedDto.setRhinovirusAntigenDetectionDetails(exportDto.getRhinovirusAntigenDetectionDetails());
					caseExportDetailedDto.setRhinovirusRapidTestDetails(exportDto.getRhinovirusRapidTestDetails());
					caseExportDetailedDto.setRhinovirusCultureDetails(exportDto.getRhinovirusCultureDetails());
					caseExportDetailedDto.setRhinovirusHistopathologyDetails(exportDto.getRhinovirusHistopathologyDetails());
					caseExportDetailedDto.setRhinovirusIsolationDetails(exportDto.getRhinovirusIsolationDetails());
					caseExportDetailedDto.setRhinovirusIgmSerumAntibodyDetails(exportDto.getRhinovirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setRhinovirusIggSerumAntibodyDetails(exportDto.getRhinovirusIggSerumAntibodyDetails());
					caseExportDetailedDto.setRhinovirusIgaSerumAntibodyDetails(exportDto.getRhinovirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setRhinovirusIncubationTimeDetails(exportDto.getRhinovirusIncubationTimeDetails());
					caseExportDetailedDto.setRhinovirusIndirectFluorescentAntibodyDetails(exportDto.getRhinovirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRhinovirusDirectFluorescentAntibodyDetails(exportDto.getRhinovirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setRhinovirusMicroscopyDetails(exportDto.getRhinovirusMicroscopyDetails());
					caseExportDetailedDto.setRhinovirusNeutralizingAntibodiesDetails(exportDto.getRhinovirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setRhinovirusPcrRtPcrDetails(exportDto.getRhinovirusPcrRtPcrDetails());
					caseExportDetailedDto.setRhinovirusGramStainDetails(exportDto.getRhinovirusGramStainDetails());
					caseExportDetailedDto.setRhinovirusLatexAgglutinationDetails(exportDto.getRhinovirusLatexAgglutinationDetails());
					caseExportDetailedDto.setRhinovirusCqValueDetectionDetails(exportDto.getRhinovirusCqValueDetectionDetails());
					caseExportDetailedDto.setRhinovirusSequencingDetails(exportDto.getRhinovirusSequencingDetails());
					caseExportDetailedDto.setRhinovirusDnaMicroarrayDetails(exportDto.getRhinovirusDnaMicroarrayDetails());
					caseExportDetailedDto.setRhinovirusOtherDetails(exportDto.getRhinovirusOtherDetails());
					caseExportDetailedDto.setEnterovirusAntibodyDetection(exportDto.getEnterovirusAntibodyDetection());
					caseExportDetailedDto.setEnterovirusAntigenDetection(exportDto.getEnterovirusAntigenDetection());
					caseExportDetailedDto.setEnterovirusRapidTest(exportDto.getEnterovirusRapidTest());
					caseExportDetailedDto.setEnterovirusCulture(exportDto.getEnterovirusCulture());
					caseExportDetailedDto.setEnterovirusHistopathology(exportDto.getEnterovirusHistopathology());
					caseExportDetailedDto.setEnterovirusIsolation(exportDto.getEnterovirusIsolation());
					caseExportDetailedDto.setEnterovirusIgmSerumAntibody(exportDto.getEnterovirusIgmSerumAntibody());
					caseExportDetailedDto.setEnterovirusIggSerumAntibody(exportDto.getEnterovirusIggSerumAntibody());
					caseExportDetailedDto.setEnterovirusIgaSerumAntibody(exportDto.getEnterovirusIgaSerumAntibody());
					caseExportDetailedDto.setEnterovirusIncubationTime(exportDto.getEnterovirusIncubationTime());
					caseExportDetailedDto.setEnterovirusIndirectFluorescentAntibody(exportDto.getEnterovirusIndirectFluorescentAntibody());
					caseExportDetailedDto.setEnterovirusDirectFluorescentAntibody(exportDto.getEnterovirusDirectFluorescentAntibody());
					caseExportDetailedDto.setEnterovirusMicroscopy(exportDto.getEnterovirusMicroscopy());
					caseExportDetailedDto.setEnterovirusNeutralizingAntibodies(exportDto.getEnterovirusNeutralizingAntibodies());
					caseExportDetailedDto.setEnterovirusPcrRtPcr(exportDto.getEnterovirusPcrRtPcr());
					caseExportDetailedDto.setEnterovirusGramStain(exportDto.getEnterovirusGramStain());
					caseExportDetailedDto.setEnterovirusLatexAgglutination(exportDto.getEnterovirusLatexAgglutination());
					caseExportDetailedDto.setEnterovirusCqValueDetection(exportDto.getEnterovirusCqValueDetection());
					caseExportDetailedDto.setEnterovirusSequencing(exportDto.getEnterovirusSequencing());
					caseExportDetailedDto.setEnterovirusDnaMicroarray(exportDto.getEnterovirusDnaMicroarray());
					caseExportDetailedDto.setEnterovirusOther(exportDto.getEnterovirusOther());
					caseExportDetailedDto.setEnterovirusAntibodyDetectionDetails(exportDto.getEnterovirusAntibodyDetectionDetails());
					caseExportDetailedDto.setEnterovirusAntigenDetectionDetails(exportDto.getEnterovirusAntigenDetectionDetails());
					caseExportDetailedDto.setEnterovirusRapidTestDetails(exportDto.getEnterovirusRapidTestDetails());
					caseExportDetailedDto.setEnterovirusCultureDetails(exportDto.getEnterovirusCultureDetails());
					caseExportDetailedDto.setEnterovirusHistopathologyDetails(exportDto.getEnterovirusHistopathologyDetails());
					caseExportDetailedDto.setEnterovirusIsolationDetails(exportDto.getEnterovirusIsolationDetails());
					caseExportDetailedDto.setEnterovirusIgmSerumAntibodyDetails(exportDto.getEnterovirusIgmSerumAntibodyDetails());
					caseExportDetailedDto.setEnterovirusIggSerumAntibodyDetails(exportDto.getEnterovirusIggSerumAntibodyDetails());
					caseExportDetailedDto.setEnterovirusIgaSerumAntibodyDetails(exportDto.getEnterovirusIgaSerumAntibodyDetails());
					caseExportDetailedDto.setEnterovirusIncubationTimeDetails(exportDto.getEnterovirusIncubationTimeDetails());
					caseExportDetailedDto.setEnterovirusIndirectFluorescentAntibodyDetails(exportDto.getEnterovirusIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setEnterovirusDirectFluorescentAntibodyDetails(exportDto.getEnterovirusDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setEnterovirusMicroscopyDetails(exportDto.getEnterovirusMicroscopyDetails());
					caseExportDetailedDto.setEnterovirusNeutralizingAntibodiesDetails(exportDto.getEnterovirusNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setEnterovirusPcrRtPcrDetails(exportDto.getEnterovirusPcrRtPcrDetails());
					caseExportDetailedDto.setEnterovirusGramStainDetails(exportDto.getEnterovirusGramStainDetails());
					caseExportDetailedDto.setEnterovirusLatexAgglutinationDetails(exportDto.getEnterovirusLatexAgglutinationDetails());
					caseExportDetailedDto.setEnterovirusCqValueDetectionDetails(exportDto.getEnterovirusCqValueDetectionDetails());
					caseExportDetailedDto.setEnterovirusSequencingDetails(exportDto.getEnterovirusSequencingDetails());
					caseExportDetailedDto.setEnterovirusDnaMicroarrayDetails(exportDto.getEnterovirusDnaMicroarrayDetails());
					caseExportDetailedDto.setEnterovirusOtherDetails(exportDto.getEnterovirusOtherDetails());
					caseExportDetailedDto.setmPneumoniaeAntibodyDetection(exportDto.getMPneumoniaeAntibodyDetection());
					caseExportDetailedDto.setmPneumoniaeAntigenDetection(exportDto.getMPneumoniaeAntigenDetection());
					caseExportDetailedDto.setmPneumoniaeRapidTest(exportDto.getMPneumoniaeRapidTest());
					caseExportDetailedDto.setmPneumoniaeCulture(exportDto.getMPneumoniaeCulture());
					caseExportDetailedDto.setmPneumoniaeHistopathology(exportDto.getMPneumoniaeHistopathology());
					caseExportDetailedDto.setmPneumoniaeIsolation(exportDto.getMPneumoniaeIsolation());
					caseExportDetailedDto.setmPneumoniaeIgmSerumAntibody(exportDto.getMPneumoniaeIgmSerumAntibody());
					caseExportDetailedDto.setmPneumoniaeIggSerumAntibody(exportDto.getMPneumoniaeIggSerumAntibody());
					caseExportDetailedDto.setmPneumoniaeIgaSerumAntibody(exportDto.getMPneumoniaeIgaSerumAntibody());
					caseExportDetailedDto.setmPneumoniaeIncubationTime(exportDto.getMPneumoniaeIncubationTime());
					caseExportDetailedDto.setmPneumoniaeIndirectFluorescentAntibody(exportDto.getMPneumoniaeIndirectFluorescentAntibody());
					caseExportDetailedDto.setmPneumoniaeDirectFluorescentAntibody(exportDto.getMPneumoniaeDirectFluorescentAntibody());
					caseExportDetailedDto.setmPneumoniaeMicroscopy(exportDto.getMPneumoniaeMicroscopy());
					caseExportDetailedDto.setmPneumoniaeNeutralizingAntibodies(exportDto.getMPneumoniaeNeutralizingAntibodies());
					caseExportDetailedDto.setmPneumoniaePcrRtPcr(exportDto.getMPneumoniaePcrRtPcr());
					caseExportDetailedDto.setmPneumoniaeGramStain(exportDto.getMPneumoniaeGramStain());
					caseExportDetailedDto.setmPneumoniaeLatexAgglutination(exportDto.getMPneumoniaeLatexAgglutination());
					caseExportDetailedDto.setmPneumoniaeCqValueDetection(exportDto.getMPneumoniaeCqValueDetection());
					caseExportDetailedDto.setmPneumoniaeSequencing(exportDto.getMPneumoniaeSequencing());
					caseExportDetailedDto.setmPneumoniaeDnaMicroarray(exportDto.getMPneumoniaeDnaMicroarray());
					caseExportDetailedDto.setmPneumoniaeOther(exportDto.getMPneumoniaeOther());
					caseExportDetailedDto.setmPneumoniaeAntibodyDetectionDetails(exportDto.getMPneumoniaeAntibodyDetectionDetails());
					caseExportDetailedDto.setmPneumoniaeAntigenDetectionDetails(exportDto.getMPneumoniaeAntigenDetectionDetails());
					caseExportDetailedDto.setmPneumoniaeRapidTestDetails(exportDto.getMPneumoniaeRapidTestDetails());
					caseExportDetailedDto.setmPneumoniaeCultureDetails(exportDto.getMPneumoniaeCultureDetails());
					caseExportDetailedDto.setmPneumoniaeHistopathologyDetails(exportDto.getMPneumoniaeHistopathologyDetails());
					caseExportDetailedDto.setmPneumoniaeIsolationDetails(exportDto.getMPneumoniaeIsolationDetails());
					caseExportDetailedDto.setmPneumoniaeIgmSerumAntibodyDetails(exportDto.getMPneumoniaeIgmSerumAntibodyDetails());
					caseExportDetailedDto.setmPneumoniaeIggSerumAntibodyDetails(exportDto.getMPneumoniaeIggSerumAntibodyDetails());
					caseExportDetailedDto.setmPneumoniaeIgaSerumAntibodyDetails(exportDto.getMPneumoniaeIgaSerumAntibodyDetails());
					caseExportDetailedDto.setmPneumoniaeIncubationTimeDetails(exportDto.getMPneumoniaeIncubationTimeDetails());
					caseExportDetailedDto.setmPneumoniaeIndirectFluorescentAntibodyDetails(exportDto.getMPneumoniaeIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setmPneumoniaeDirectFluorescentAntibodyDetails(exportDto.getMPneumoniaeDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setmPneumoniaeMicroscopyDetails(exportDto.getMPneumoniaeMicroscopyDetails());
					caseExportDetailedDto.setmPneumoniaeNeutralizingAntibodiesDetails(exportDto.getMPneumoniaeNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setmPneumoniaePcrRtPcrDetails(exportDto.getMPneumoniaePcrRtPcrDetails());
					caseExportDetailedDto.setmPneumoniaeGramStainDetails(exportDto.getMPneumoniaeGramStainDetails());
					caseExportDetailedDto.setmPneumoniaeLatexAgglutinationDetails(exportDto.getMPneumoniaeLatexAgglutinationDetails());
					caseExportDetailedDto.setmPneumoniaeCqValueDetectionDetails(exportDto.getMPneumoniaeCqValueDetectionDetails());
					caseExportDetailedDto.setmPneumoniaeSequencingDetails(exportDto.getMPneumoniaeSequencingDetails());
					caseExportDetailedDto.setmPneumoniaeDnaMicroarrayDetails(exportDto.getMPneumoniaeDnaMicroarrayDetails());
					caseExportDetailedDto.setmPneumoniaeOtherDetails(exportDto.getMPneumoniaeOtherDetails());
					caseExportDetailedDto.setcPneumoniaeAntibodyDetection(exportDto.getCPneumoniaeAntibodyDetection());
					caseExportDetailedDto.setcPneumoniaeAntigenDetection(exportDto.getCPneumoniaeAntigenDetection());
					caseExportDetailedDto.setcPneumoniaeRapidTest(exportDto.getCPneumoniaeRapidTest());
					caseExportDetailedDto.setcPneumoniaeCulture(exportDto.getCPneumoniaeCulture());
					caseExportDetailedDto.setcPneumoniaeHistopathology(exportDto.getCPneumoniaeHistopathology());
					caseExportDetailedDto.setcPneumoniaeIsolation(exportDto.getCPneumoniaeIsolation());
					caseExportDetailedDto.setcPneumoniaeIgmSerumAntibody(exportDto.getCPneumoniaeIgmSerumAntibody());
					caseExportDetailedDto.setcPneumoniaeIggSerumAntibody(exportDto.getCPneumoniaeIggSerumAntibody());
					caseExportDetailedDto.setcPneumoniaeIgaSerumAntibody(exportDto.getCPneumoniaeIgaSerumAntibody());
					caseExportDetailedDto.setcPneumoniaeIncubationTime(exportDto.getCPneumoniaeIncubationTime());
					caseExportDetailedDto.setcPneumoniaeIndirectFluorescentAntibody(exportDto.getCPneumoniaeIndirectFluorescentAntibody());
					caseExportDetailedDto.setcPneumoniaeDirectFluorescentAntibody(exportDto.getCPneumoniaeDirectFluorescentAntibody());
					caseExportDetailedDto.setcPneumoniaeMicroscopy(exportDto.getCPneumoniaeMicroscopy());
					caseExportDetailedDto.setcPneumoniaeNeutralizingAntibodies(exportDto.getCPneumoniaeNeutralizingAntibodies());
					caseExportDetailedDto.setcPneumoniaePcrRtPcr(exportDto.getCPneumoniaePcrRtPcr());
					caseExportDetailedDto.setcPneumoniaeGramStain(exportDto.getCPneumoniaeGramStain());
					caseExportDetailedDto.setcPneumoniaeLatexAgglutination(exportDto.getCPneumoniaeLatexAgglutination());
					caseExportDetailedDto.setcPneumoniaeCqValueDetection(exportDto.getCPneumoniaeCqValueDetection());
					caseExportDetailedDto.setcPneumoniaeSequencing(exportDto.getCPneumoniaeSequencing());
					caseExportDetailedDto.setcPneumoniaeDnaMicroarray(exportDto.getCPneumoniaeDnaMicroarray());
					caseExportDetailedDto.setcPneumoniaeOther(exportDto.getCPneumoniaeOther());
					caseExportDetailedDto.setcPneumoniaeAntibodyDetectionDetails(exportDto.getCPneumoniaeAntibodyDetectionDetails());
					caseExportDetailedDto.setcPneumoniaeAntigenDetectionDetails(exportDto.getCPneumoniaeAntigenDetectionDetails());
					caseExportDetailedDto.setcPneumoniaeRapidTestDetails(exportDto.getCPneumoniaeRapidTestDetails());
					caseExportDetailedDto.setcPneumoniaeCultureDetails(exportDto.getCPneumoniaeCultureDetails());
					caseExportDetailedDto.setcPneumoniaeHistopathologyDetails(exportDto.getCPneumoniaeHistopathologyDetails());
					caseExportDetailedDto.setcPneumoniaeIsolationDetails(exportDto.getCPneumoniaeIsolationDetails());
					caseExportDetailedDto.setcPneumoniaeIgmSerumAntibodyDetails(exportDto.getCPneumoniaeIgmSerumAntibodyDetails());
					caseExportDetailedDto.setcPneumoniaeIggSerumAntibodyDetails(exportDto.getCPneumoniaeIggSerumAntibodyDetails());
					caseExportDetailedDto.setcPneumoniaeIgaSerumAntibodyDetails(exportDto.getCPneumoniaeIgaSerumAntibodyDetails());
					caseExportDetailedDto.setcPneumoniaeIncubationTimeDetails(exportDto.getCPneumoniaeIncubationTimeDetails());
					caseExportDetailedDto.setcPneumoniaeIndirectFluorescentAntibodyDetails(exportDto.getCPneumoniaeIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setcPneumoniaeDirectFluorescentAntibodyDetails(exportDto.getCPneumoniaeDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setcPneumoniaeMicroscopyDetails(exportDto.getCPneumoniaeMicroscopyDetails());
					caseExportDetailedDto.setcPneumoniaeNeutralizingAntibodiesDetails(exportDto.getCPneumoniaeNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setcPneumoniaePcrRtPcrDetails(exportDto.getCPneumoniaePcrRtPcrDetails());
					caseExportDetailedDto.setcPneumoniaeGramStainDetails(exportDto.getCPneumoniaeGramStainDetails());
					caseExportDetailedDto.setcPneumoniaeLatexAgglutinationDetails(exportDto.getCPneumoniaeLatexAgglutinationDetails());
					caseExportDetailedDto.setcPneumoniaeCqValueDetectionDetails(exportDto.getCPneumoniaeCqValueDetectionDetails());
					caseExportDetailedDto.setcPneumoniaeSequencingDetails(exportDto.getCPneumoniaeSequencingDetails());
					caseExportDetailedDto.setcPneumoniaeDnaMicroarrayDetails(exportDto.getCPneumoniaeDnaMicroarrayDetails());
					caseExportDetailedDto.setcPneumoniaeOtherDetails(exportDto.getCPneumoniaeOtherDetails());
					caseExportDetailedDto.setAriAntibodyDetection(exportDto.getAriAntibodyDetection());
					caseExportDetailedDto.setAriAntigenDetection(exportDto.getAriAntigenDetection());
					caseExportDetailedDto.setAriRapidTest(exportDto.getAriRapidTest());
					caseExportDetailedDto.setAriCulture(exportDto.getAriCulture());
					caseExportDetailedDto.setAriHistopathology(exportDto.getAriHistopathology());
					caseExportDetailedDto.setAriIsolation(exportDto.getAriIsolation());
					caseExportDetailedDto.setAriIgmSerumAntibody(exportDto.getAriIgmSerumAntibody());
					caseExportDetailedDto.setAriIggSerumAntibody(exportDto.getAriIggSerumAntibody());
					caseExportDetailedDto.setAriIgaSerumAntibody(exportDto.getAriIgaSerumAntibody());
					caseExportDetailedDto.setAriIncubationTime(exportDto.getAriIncubationTime());
					caseExportDetailedDto.setAriIndirectFluorescentAntibody(exportDto.getAriIndirectFluorescentAntibody());
					caseExportDetailedDto.setAriDirectFluorescentAntibody(exportDto.getAriDirectFluorescentAntibody());
					caseExportDetailedDto.setAriMicroscopy(exportDto.getAriMicroscopy());
					caseExportDetailedDto.setAriNeutralizingAntibodies(exportDto.getAriNeutralizingAntibodies());
					caseExportDetailedDto.setAriPcrRtPcr(exportDto.getAriPcrRtPcr());
					caseExportDetailedDto.setAriGramStain(exportDto.getAriGramStain());
					caseExportDetailedDto.setAriLatexAgglutination(exportDto.getAriLatexAgglutination());
					caseExportDetailedDto.setAriCqValueDetection(exportDto.getAriCqValueDetection());
					caseExportDetailedDto.setAriSequencing(exportDto.getAriSequencing());
					caseExportDetailedDto.setAriDnaMicroarray(exportDto.getAriDnaMicroarray());
					caseExportDetailedDto.setAriOther(exportDto.getAriOther());
					caseExportDetailedDto.setAriAntibodyDetectionDetails(exportDto.getAriAntibodyDetectionDetails());
					caseExportDetailedDto.setAriAntigenDetectionDetails(exportDto.getAriAntigenDetectionDetails());
					caseExportDetailedDto.setAriRapidTestDetails(exportDto.getAriRapidTestDetails());
					caseExportDetailedDto.setAriCultureDetails(exportDto.getAriCultureDetails());
					caseExportDetailedDto.setAriHistopathologyDetails(exportDto.getAriHistopathologyDetails());
					caseExportDetailedDto.setAriIsolationDetails(exportDto.getAriIsolationDetails());
					caseExportDetailedDto.setAriIgmSerumAntibodyDetails(exportDto.getAriIgmSerumAntibodyDetails());
					caseExportDetailedDto.setAriIggSerumAntibodyDetails(exportDto.getAriIggSerumAntibodyDetails());
					caseExportDetailedDto.setAriIgaSerumAntibodyDetails(exportDto.getAriIgaSerumAntibodyDetails());
					caseExportDetailedDto.setAriIncubationTimeDetails(exportDto.getAriIncubationTimeDetails());
					caseExportDetailedDto.setAriIndirectFluorescentAntibodyDetails(exportDto.getAriIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAriDirectFluorescentAntibodyDetails(exportDto.getAriDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setAriMicroscopyDetails(exportDto.getAriMicroscopyDetails());
					caseExportDetailedDto.setAriNeutralizingAntibodiesDetails(exportDto.getAriNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setAriPcrRtPcrDetails(exportDto.getAriPcrRtPcrDetails());
					caseExportDetailedDto.setAriGramStainDetails(exportDto.getAriGramStainDetails());
					caseExportDetailedDto.setAriLatexAgglutinationDetails(exportDto.getAriLatexAgglutinationDetails());
					caseExportDetailedDto.setAriCqValueDetectionDetails(exportDto.getAriCqValueDetectionDetails());
					caseExportDetailedDto.setAriSequencingDetails(exportDto.getAriSequencingDetails());
					caseExportDetailedDto.setAriDnaMicroarrayDetails(exportDto.getAriDnaMicroarrayDetails());
					caseExportDetailedDto.setAriOtherDetails(exportDto.getAriOtherDetails());
					caseExportDetailedDto.setChikungunyaAntibodyDetection(exportDto.getChikungunyaAntibodyDetection());
					caseExportDetailedDto.setChikungunyaAntigenDetection(exportDto.getChikungunyaAntigenDetection());
					caseExportDetailedDto.setChikungunyaRapidTest(exportDto.getChikungunyaRapidTest());
					caseExportDetailedDto.setChikungunyaCulture(exportDto.getChikungunyaCulture());
					caseExportDetailedDto.setChikungunyaHistopathology(exportDto.getChikungunyaHistopathology());
					caseExportDetailedDto.setChikungunyaIsolation(exportDto.getChikungunyaIsolation());
					caseExportDetailedDto.setChikungunyaIgmSerumAntibody(exportDto.getChikungunyaIgmSerumAntibody());
					caseExportDetailedDto.setChikungunyaIggSerumAntibody(exportDto.getChikungunyaIggSerumAntibody());
					caseExportDetailedDto.setChikungunyaIgaSerumAntibody(exportDto.getChikungunyaIgaSerumAntibody());
					caseExportDetailedDto.setChikungunyaIncubationTime(exportDto.getChikungunyaIncubationTime());
					caseExportDetailedDto.setChikungunyaIndirectFluorescentAntibody(exportDto.getChikungunyaIndirectFluorescentAntibody());
					caseExportDetailedDto.setChikungunyaDirectFluorescentAntibody(exportDto.getChikungunyaDirectFluorescentAntibody());
					caseExportDetailedDto.setChikungunyaMicroscopy(exportDto.getChikungunyaMicroscopy());
					caseExportDetailedDto.setChikungunyaNeutralizingAntibodies(exportDto.getChikungunyaNeutralizingAntibodies());
					caseExportDetailedDto.setChikungunyaPcrRtPcr(exportDto.getChikungunyaPcrRtPcr());
					caseExportDetailedDto.setChikungunyaGramStain(exportDto.getChikungunyaGramStain());
					caseExportDetailedDto.setChikungunyaLatexAgglutination(exportDto.getChikungunyaLatexAgglutination());
					caseExportDetailedDto.setChikungunyaCqValueDetection(exportDto.getChikungunyaCqValueDetection());
					caseExportDetailedDto.setChikungunyaSequencing(exportDto.getChikungunyaSequencing());
					caseExportDetailedDto.setChikungunyaDnaMicroarray(exportDto.getChikungunyaDnaMicroarray());
					caseExportDetailedDto.setChikungunyaOther(exportDto.getChikungunyaOther());
					caseExportDetailedDto.setChikungunyaAntibodyDetectionDetails(exportDto.getChikungunyaAntibodyDetectionDetails());
					caseExportDetailedDto.setChikungunyaAntigenDetectionDetails(exportDto.getChikungunyaAntigenDetectionDetails());
					caseExportDetailedDto.setChikungunyaRapidTestDetails(exportDto.getChikungunyaRapidTestDetails());
					caseExportDetailedDto.setChikungunyaCultureDetails(exportDto.getChikungunyaCultureDetails());
					caseExportDetailedDto.setChikungunyaHistopathologyDetails(exportDto.getChikungunyaHistopathologyDetails());
					caseExportDetailedDto.setChikungunyaIsolationDetails(exportDto.getChikungunyaIsolationDetails());
					caseExportDetailedDto.setChikungunyaIgmSerumAntibodyDetails(exportDto.getChikungunyaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setChikungunyaIggSerumAntibodyDetails(exportDto.getChikungunyaIggSerumAntibodyDetails());
					caseExportDetailedDto.setChikungunyaIgaSerumAntibodyDetails(exportDto.getChikungunyaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setChikungunyaIncubationTimeDetails(exportDto.getChikungunyaIncubationTimeDetails());
					caseExportDetailedDto.setChikungunyaIndirectFluorescentAntibodyDetails(exportDto.getChikungunyaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setChikungunyaDirectFluorescentAntibodyDetails(exportDto.getChikungunyaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setChikungunyaMicroscopyDetails(exportDto.getChikungunyaMicroscopyDetails());
					caseExportDetailedDto.setChikungunyaNeutralizingAntibodiesDetails(exportDto.getChikungunyaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setChikungunyaPcrRtPcrDetails(exportDto.getChikungunyaPcrRtPcrDetails());
					caseExportDetailedDto.setChikungunyaGramStainDetails(exportDto.getChikungunyaGramStainDetails());
					caseExportDetailedDto.setChikungunyaLatexAgglutinationDetails(exportDto.getChikungunyaLatexAgglutinationDetails());
					caseExportDetailedDto.setChikungunyaCqValueDetectionDetails(exportDto.getChikungunyaCqValueDetectionDetails());
					caseExportDetailedDto.setChikungunyaSequencingDetails(exportDto.getChikungunyaSequencingDetails());
					caseExportDetailedDto.setChikungunyaDnaMicroarrayDetails(exportDto.getChikungunyaDnaMicroarrayDetails());
					caseExportDetailedDto.setChikungunyaOtherDetails(exportDto.getChikungunyaOtherDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildAntibodyDetection(exportDto.getPostImmunizationAdverseEventsMildAntibodyDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildAntigenDetection(exportDto.getPostImmunizationAdverseEventsMildAntigenDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildRapidTest(exportDto.getPostImmunizationAdverseEventsMildRapidTest());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildCulture(exportDto.getPostImmunizationAdverseEventsMildCulture());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildHistopathology(exportDto.getPostImmunizationAdverseEventsMildHistopathology());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIsolation(exportDto.getPostImmunizationAdverseEventsMildIsolation());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIgmSerumAntibody(exportDto.getPostImmunizationAdverseEventsMildIgmSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIggSerumAntibody(exportDto.getPostImmunizationAdverseEventsMildIggSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIgaSerumAntibody(exportDto.getPostImmunizationAdverseEventsMildIgaSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIncubationTime(exportDto.getPostImmunizationAdverseEventsMildIncubationTime());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibody(exportDto.getPostImmunizationAdverseEventsMildIndirectFluorescentAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildDirectFluorescentAntibody(exportDto.getPostImmunizationAdverseEventsMildDirectFluorescentAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildMicroscopy(exportDto.getPostImmunizationAdverseEventsMildMicroscopy());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodies(exportDto.getPostImmunizationAdverseEventsMildNeutralizingAntibodies());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildPcrRtPcr(exportDto.getPostImmunizationAdverseEventsMildPcrRtPcr());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildGramStain(exportDto.getPostImmunizationAdverseEventsMildGramStain());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildLatexAgglutination(exportDto.getPostImmunizationAdverseEventsMildLatexAgglutination());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildCqValueDetection(exportDto.getPostImmunizationAdverseEventsMildCqValueDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildSequencing(exportDto.getPostImmunizationAdverseEventsMildSequencing());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildDnaMicroarray(exportDto.getPostImmunizationAdverseEventsMildDnaMicroarray());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildOther(exportDto.getPostImmunizationAdverseEventsMildOther());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(exportDto.getPostImmunizationAdverseEventsMildAntibodyDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildAntigenDetectionDetails(exportDto.getPostImmunizationAdverseEventsMildAntigenDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildRapidTestDetails(exportDto.getPostImmunizationAdverseEventsMildRapidTestDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildCultureDetails(exportDto.getPostImmunizationAdverseEventsMildCultureDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildHistopathologyDetails(exportDto.getPostImmunizationAdverseEventsMildHistopathologyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIsolationDetails(exportDto.getPostImmunizationAdverseEventsMildIsolationDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIggSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsMildIggSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIncubationTimeDetails(exportDto.getPostImmunizationAdverseEventsMildIncubationTimeDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails(exportDto.getPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails(exportDto.getPostImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildMicroscopyDetails(exportDto.getPostImmunizationAdverseEventsMildMicroscopyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails(exportDto.getPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildPcrRtPcrDetails(exportDto.getPostImmunizationAdverseEventsMildPcrRtPcrDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildGramStainDetails(exportDto.getPostImmunizationAdverseEventsMildGramStainDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildLatexAgglutinationDetails(exportDto.getPostImmunizationAdverseEventsMildLatexAgglutinationDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildCqValueDetectionDetails(exportDto.getPostImmunizationAdverseEventsMildCqValueDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildSequencingDetails(exportDto.getPostImmunizationAdverseEventsMildSequencingDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildDnaMicroarrayDetails(exportDto.getPostImmunizationAdverseEventsMildDnaMicroarrayDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsMildOtherDetails(exportDto.getPostImmunizationAdverseEventsMildOtherDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereAntibodyDetection(exportDto.getPostImmunizationAdverseEventsSevereAntibodyDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereAntigenDetection(exportDto.getPostImmunizationAdverseEventsSevereAntigenDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereRapidTest(exportDto.getPostImmunizationAdverseEventsSevereRapidTest());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereCulture(exportDto.getPostImmunizationAdverseEventsSevereCulture());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereHistopathology(exportDto.getPostImmunizationAdverseEventsSevereHistopathology());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIsolation(exportDto.getPostImmunizationAdverseEventsSevereIsolation());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibody(exportDto.getPostImmunizationAdverseEventsSevereIgmSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIggSerumAntibody(exportDto.getPostImmunizationAdverseEventsSevereIggSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibody(exportDto.getPostImmunizationAdverseEventsSevereIgaSerumAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIncubationTime(exportDto.getPostImmunizationAdverseEventsSevereIncubationTime());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(exportDto.getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereDirectFluorescentAntibody(exportDto.getPostImmunizationAdverseEventsSevereDirectFluorescentAntibody());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereMicroscopy(exportDto.getPostImmunizationAdverseEventsSevereMicroscopy());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(exportDto.getPostImmunizationAdverseEventsSevereNeutralizingAntibodies());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSeverePcrRtPcr(exportDto.getPostImmunizationAdverseEventsSeverePcrRtPcr());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereGramStain(exportDto.getPostImmunizationAdverseEventsSevereGramStain());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereLatexAgglutination(exportDto.getPostImmunizationAdverseEventsSevereLatexAgglutination());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereCqValueDetection(exportDto.getPostImmunizationAdverseEventsSevereCqValueDetection());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereSequencing(exportDto.getPostImmunizationAdverseEventsSevereSequencing());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereDnaMicroarray(exportDto.getPostImmunizationAdverseEventsSevereDnaMicroarray());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereOther(exportDto.getPostImmunizationAdverseEventsSevereOther());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(exportDto.getPostImmunizationAdverseEventsSevereAntibodyDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereAntigenDetectionDetails(exportDto.getPostImmunizationAdverseEventsSevereAntigenDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereRapidTestDetails(exportDto.getPostImmunizationAdverseEventsSevereRapidTestDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereCultureDetails(exportDto.getPostImmunizationAdverseEventsSevereCultureDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereHistopathologyDetails(exportDto.getPostImmunizationAdverseEventsSevereHistopathologyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIsolationDetails(exportDto.getPostImmunizationAdverseEventsSevereIsolationDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails(exportDto.getPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIncubationTimeDetails(exportDto.getPostImmunizationAdverseEventsSevereIncubationTimeDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails(exportDto.getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails(exportDto.getPostImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereMicroscopyDetails(exportDto.getPostImmunizationAdverseEventsSevereMicroscopyDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails(exportDto.getPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSeverePcrRtPcrDetails(exportDto.getPostImmunizationAdverseEventsSeverePcrRtPcrDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereGramStainDetails(exportDto.getPostImmunizationAdverseEventsSevereGramStainDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereLatexAgglutinationDetails(exportDto.getPostImmunizationAdverseEventsSevereLatexAgglutinationDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereCqValueDetectionDetails(exportDto.getPostImmunizationAdverseEventsSevereCqValueDetectionDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereSequencingDetails(exportDto.getPostImmunizationAdverseEventsSevereSequencingDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereDnaMicroarrayDetails(exportDto.getPostImmunizationAdverseEventsSevereDnaMicroarrayDetails());
					caseExportDetailedDto.setPostImmunizationAdverseEventsSevereOtherDetails(exportDto.getPostImmunizationAdverseEventsSevereOtherDetails());
					caseExportDetailedDto.setFhaAntibodyDetection(exportDto.getFhaAntibodyDetection());
					caseExportDetailedDto.setFhaAntigenDetection(exportDto.getFhaAntigenDetection());
					caseExportDetailedDto.setFhaRapidTest(exportDto.getFhaRapidTest());
					caseExportDetailedDto.setFhaCulture(exportDto.getFhaCulture());
					caseExportDetailedDto.setFhaHistopathology(exportDto.getFhaHistopathology());
					caseExportDetailedDto.setFhaIsolation(exportDto.getFhaIsolation());
					caseExportDetailedDto.setFhaIgmSerumAntibody(exportDto.getFhaIgmSerumAntibody());
					caseExportDetailedDto.setFhaIggSerumAntibody(exportDto.getFhaIggSerumAntibody());
					caseExportDetailedDto.setFhaIgaSerumAntibody(exportDto.getFhaIgaSerumAntibody());
					caseExportDetailedDto.setFhaIncubationTime(exportDto.getFhaIncubationTime());
					caseExportDetailedDto.setFhaIndirectFluorescentAntibody(exportDto.getFhaIndirectFluorescentAntibody());
					caseExportDetailedDto.setFhaDirectFluorescentAntibody(exportDto.getFhaDirectFluorescentAntibody());
					caseExportDetailedDto.setFhaMicroscopy(exportDto.getFhaMicroscopy());
					caseExportDetailedDto.setFhaNeutralizingAntibodies(exportDto.getFhaNeutralizingAntibodies());
					caseExportDetailedDto.setFhaPcrRtPcr(exportDto.getFhaPcrRtPcr());
					caseExportDetailedDto.setFhaGramStain(exportDto.getFhaGramStain());
					caseExportDetailedDto.setFhaLatexAgglutination(exportDto.getFhaLatexAgglutination());
					caseExportDetailedDto.setFhaCqValueDetection(exportDto.getFhaCqValueDetection());
					caseExportDetailedDto.setFhaSequencing(exportDto.getFhaSequencing());
					caseExportDetailedDto.setFhaDnaMicroarray(exportDto.getFhaDnaMicroarray());
					caseExportDetailedDto.setFhaOther(exportDto.getFhaOther());
					caseExportDetailedDto.setFhaAntibodyDetectionDetails(exportDto.getFhaAntibodyDetectionDetails());
					caseExportDetailedDto.setFhaAntigenDetectionDetails(exportDto.getFhaAntigenDetectionDetails());
					caseExportDetailedDto.setFhaRapidTestDetails(exportDto.getFhaRapidTestDetails());
					caseExportDetailedDto.setFhaCultureDetails(exportDto.getFhaCultureDetails());
					caseExportDetailedDto.setFhaHistopathologyDetails(exportDto.getFhaHistopathologyDetails());
					caseExportDetailedDto.setFhaIsolationDetails(exportDto.getFhaIsolationDetails());
					caseExportDetailedDto.setFhaIgmSerumAntibodyDetails(exportDto.getFhaIgmSerumAntibodyDetails());
					caseExportDetailedDto.setFhaIggSerumAntibodyDetails(exportDto.getFhaIggSerumAntibodyDetails());
					caseExportDetailedDto.setFhaIgaSerumAntibodyDetails(exportDto.getFhaIgaSerumAntibodyDetails());
					caseExportDetailedDto.setFhaIncubationTimeDetails(exportDto.getFhaIncubationTimeDetails());
					caseExportDetailedDto.setFhaIndirectFluorescentAntibodyDetails(exportDto.getFhaIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setFhaDirectFluorescentAntibodyDetails(exportDto.getFhaDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setFhaMicroscopyDetails(exportDto.getFhaMicroscopyDetails());
					caseExportDetailedDto.setFhaNeutralizingAntibodiesDetails(exportDto.getFhaNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setFhaPcrRtPcrDetails(exportDto.getFhaPcrRtPcrDetails());
					caseExportDetailedDto.setFhaGramStainDetails(exportDto.getFhaGramStainDetails());
					caseExportDetailedDto.setFhaLatexAgglutinationDetails(exportDto.getFhaLatexAgglutinationDetails());
					caseExportDetailedDto.setFhaCqValueDetectionDetails(exportDto.getFhaCqValueDetectionDetails());
					caseExportDetailedDto.setFhaSequencingDetails(exportDto.getFhaSequencingDetails());
					caseExportDetailedDto.setFhaDnaMicroarrayDetails(exportDto.getFhaDnaMicroarrayDetails());
					caseExportDetailedDto.setFhaOtherDetails(exportDto.getFhaOtherDetails());
					caseExportDetailedDto.setOtherAntibodyDetection(exportDto.getOtherAntibodyDetection());
					caseExportDetailedDto.setOtherAntigenDetection(exportDto.getOtherAntigenDetection());
					caseExportDetailedDto.setOtherRapidTest(exportDto.getOtherRapidTest());
					caseExportDetailedDto.setOtherCulture(exportDto.getOtherCulture());
					caseExportDetailedDto.setOtherHistopathology(exportDto.getOtherHistopathology());
					caseExportDetailedDto.setOtherIsolation(exportDto.getOtherIsolation());
					caseExportDetailedDto.setOtherIgmSerumAntibody(exportDto.getOtherIgmSerumAntibody());
					caseExportDetailedDto.setOtherIggSerumAntibody(exportDto.getOtherIggSerumAntibody());
					caseExportDetailedDto.setOtherIgaSerumAntibody(exportDto.getOtherIgaSerumAntibody());
					caseExportDetailedDto.setOtherIncubationTime(exportDto.getOtherIncubationTime());
					caseExportDetailedDto.setOtherIndirectFluorescentAntibody(exportDto.getOtherIndirectFluorescentAntibody());
					caseExportDetailedDto.setOtherDirectFluorescentAntibody(exportDto.getOtherDirectFluorescentAntibody());
					caseExportDetailedDto.setOtherMicroscopy(exportDto.getOtherMicroscopy());
					caseExportDetailedDto.setOtherNeutralizingAntibodies(exportDto.getOtherNeutralizingAntibodies());
					caseExportDetailedDto.setOtherPcrRtPcr(exportDto.getOtherPcrRtPcr());
					caseExportDetailedDto.setOtherGramStain(exportDto.getOtherGramStain());
					caseExportDetailedDto.setOtherLatexAgglutination(exportDto.getOtherLatexAgglutination());
					caseExportDetailedDto.setOtherCqValueDetection(exportDto.getOtherCqValueDetection());
					caseExportDetailedDto.setOtherSequencing(exportDto.getOtherSequencing());
					caseExportDetailedDto.setOtherDnaMicroarray(exportDto.getOtherDnaMicroarray());
					caseExportDetailedDto.setOtherOther(exportDto.getOtherOther());
					caseExportDetailedDto.setOtherAntibodyDetectionDetails(exportDto.getOtherAntibodyDetectionDetails());
					caseExportDetailedDto.setOtherAntigenDetectionDetails(exportDto.getOtherAntigenDetectionDetails());
					caseExportDetailedDto.setOtherRapidTestDetails(exportDto.getOtherRapidTestDetails());
					caseExportDetailedDto.setOtherCultureDetails(exportDto.getOtherCultureDetails());
					caseExportDetailedDto.setOtherHistopathologyDetails(exportDto.getOtherHistopathologyDetails());
					caseExportDetailedDto.setOtherIsolationDetails(exportDto.getOtherIsolationDetails());
					caseExportDetailedDto.setOtherIgmSerumAntibodyDetails(exportDto.getOtherIgmSerumAntibodyDetails());
					caseExportDetailedDto.setOtherIggSerumAntibodyDetails(exportDto.getOtherIggSerumAntibodyDetails());
					caseExportDetailedDto.setOtherIgaSerumAntibodyDetails(exportDto.getOtherIgaSerumAntibodyDetails());
					caseExportDetailedDto.setOtherIncubationTimeDetails(exportDto.getOtherIncubationTimeDetails());
					caseExportDetailedDto.setOtherIndirectFluorescentAntibodyDetails(exportDto.getOtherIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setOtherDirectFluorescentAntibodyDetails(exportDto.getOtherDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setOtherMicroscopyDetails(exportDto.getOtherMicroscopyDetails());
					caseExportDetailedDto.setOtherNeutralizingAntibodiesDetails(exportDto.getOtherNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setOtherPcrRtPcrDetails(exportDto.getOtherPcrRtPcrDetails());
					caseExportDetailedDto.setOtherGramStainDetails(exportDto.getOtherGramStainDetails());
					caseExportDetailedDto.setOtherLatexAgglutinationDetails(exportDto.getOtherLatexAgglutinationDetails());
					caseExportDetailedDto.setOtherCqValueDetectionDetails(exportDto.getOtherCqValueDetectionDetails());
					caseExportDetailedDto.setOtherSequencingDetails(exportDto.getOtherSequencingDetails());
					caseExportDetailedDto.setOtherDnaMicroarrayDetails(exportDto.getOtherDnaMicroarrayDetails());
					caseExportDetailedDto.setOtherOtherDetails(exportDto.getOtherOtherDetails());
					caseExportDetailedDto.setUndefinedAntibodyDetection(exportDto.getUndefinedAntibodyDetection());
					caseExportDetailedDto.setUndefinedAntigenDetection(exportDto.getUndefinedAntigenDetection());
					caseExportDetailedDto.setUndefinedRapidTest(exportDto.getUndefinedRapidTest());
					caseExportDetailedDto.setUndefinedCulture(exportDto.getUndefinedCulture());
					caseExportDetailedDto.setUndefinedHistopathology(exportDto.getUndefinedHistopathology());
					caseExportDetailedDto.setUndefinedIsolation(exportDto.getUndefinedIsolation());
					caseExportDetailedDto.setUndefinedIgmSerumAntibody(exportDto.getUndefinedIgmSerumAntibody());
					caseExportDetailedDto.setUndefinedIggSerumAntibody(exportDto.getUndefinedIggSerumAntibody());
					caseExportDetailedDto.setUndefinedIgaSerumAntibody(exportDto.getUndefinedIgaSerumAntibody());
					caseExportDetailedDto.setUndefinedIncubationTime(exportDto.getUndefinedIncubationTime());
					caseExportDetailedDto.setUndefinedIndirectFluorescentAntibody(exportDto.getUndefinedIndirectFluorescentAntibody());
					caseExportDetailedDto.setUndefinedDirectFluorescentAntibody(exportDto.getUndefinedDirectFluorescentAntibody());
					caseExportDetailedDto.setUndefinedMicroscopy(exportDto.getUndefinedMicroscopy());
					caseExportDetailedDto.setUndefinedNeutralizingAntibodies(exportDto.getUndefinedNeutralizingAntibodies());
					caseExportDetailedDto.setUndefinedPcrRtPcr(exportDto.getUndefinedPcrRtPcr());
					caseExportDetailedDto.setUndefinedGramStain(exportDto.getUndefinedGramStain());
					caseExportDetailedDto.setUndefinedLatexAgglutination(exportDto.getUndefinedLatexAgglutination());
					caseExportDetailedDto.setUndefinedCqValueDetection(exportDto.getUndefinedCqValueDetection());
					caseExportDetailedDto.setUndefinedSequencing(exportDto.getUndefinedSequencing());
					caseExportDetailedDto.setUndefinedDnaMicroarray(exportDto.getUndefinedDnaMicroarray());
					caseExportDetailedDto.setUndefinedOther(exportDto.getUndefinedOther());
					caseExportDetailedDto.setUndefinedAntibodyDetectionDetails(exportDto.getUndefinedAntibodyDetectionDetails());
					caseExportDetailedDto.setUndefinedAntigenDetectionDetails(exportDto.getUndefinedAntigenDetectionDetails());
					caseExportDetailedDto.setUndefinedRapidTestDetails(exportDto.getUndefinedRapidTestDetails());
					caseExportDetailedDto.setUndefinedCultureDetails(exportDto.getUndefinedCultureDetails());
					caseExportDetailedDto.setUndefinedHistopathologyDetails(exportDto.getUndefinedHistopathologyDetails());
					caseExportDetailedDto.setUndefinedIsolationDetails(exportDto.getUndefinedIsolationDetails());
					caseExportDetailedDto.setUndefinedIgmSerumAntibodyDetails(exportDto.getUndefinedIgmSerumAntibodyDetails());
					caseExportDetailedDto.setUndefinedIggSerumAntibodyDetails(exportDto.getUndefinedIggSerumAntibodyDetails());
					caseExportDetailedDto.setUndefinedIgaSerumAntibodyDetails(exportDto.getUndefinedIgaSerumAntibodyDetails());
					caseExportDetailedDto.setUndefinedIncubationTimeDetails(exportDto.getUndefinedIncubationTimeDetails());
					caseExportDetailedDto.setUndefinedIndirectFluorescentAntibodyDetails(exportDto.getUndefinedIndirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setUndefinedDirectFluorescentAntibodyDetails(exportDto.getUndefinedDirectFluorescentAntibodyDetails());
					caseExportDetailedDto.setUndefinedMicroscopyDetails(exportDto.getUndefinedMicroscopyDetails());
					caseExportDetailedDto.setUndefinedNeutralizingAntibodiesDetails(exportDto.getUndefinedNeutralizingAntibodiesDetails());
					caseExportDetailedDto.setUndefinedPcrRtPcrDetails(exportDto.getUndefinedPcrRtPcrDetails());
					caseExportDetailedDto.setUndefinedGramStainDetails(exportDto.getUndefinedGramStainDetails());
					caseExportDetailedDto.setUndefinedLatexAgglutinationDetails(exportDto.getUndefinedLatexAgglutinationDetails());
					caseExportDetailedDto.setUndefinedCqValueDetectionDetails(exportDto.getUndefinedCqValueDetectionDetails());
					caseExportDetailedDto.setUndefinedSequencingDetails(exportDto.getUndefinedSequencingDetails());
					caseExportDetailedDto.setUndefinedDnaMicroarrayDetails(exportDto.getUndefinedDnaMicroarrayDetails());
					caseExportDetailedDto.setUndefinedOtherDetails(exportDto.getUndefinedOtherDetails());

					caseExportDetailedDto.setOtherPathogenTests(embeddedDetailedSampleExportDto.getOtherPathogenTestsDetails());

					newResult.add(caseExportDetailedDto);
//                    }
				}
				}
			}
			return newResult;
		}
		return resultList;
	}


	@RightsAllowed(UserRight._CASE_MERGE)
	public void deleteCaseAsDuplicate(String caseUuid, String duplicateOfCaseUuid) {

		Case caze = service.getByUuid(caseUuid);
		Case duplicateOfCase = service.getByUuid(duplicateOfCaseUuid);
		caze.setDuplicateOf(duplicateOfCase);
		service.ensurePersisted(caze);

		delete(caseUuid, new DeletionDetails(DeletionReason.DUPLICATE_ENTRIES, null));
	}



	@Override
	public List<CaseIndexDto[]> getCasesForDuplicateMerging(CaseCriteria criteria, boolean ignoreRegion) {
		return null;
	}
	@Override
	public List<CaseIndexDto> getIndexList(CaseCriteria caseCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

		CriteriaQuery<Tuple> cqIds = listQueryBuilder.buildIndexCriteriaPrefetchIds(caseCriteria, sortProperties);
		List<Long> indexListIds =
				QueryHelper.getResultList(em, cqIds, first, max).stream().map(t -> t.get(0, Long.class)).collect(Collectors.toList());

		List<CaseIndexDto> cases = new ArrayList<>();
		IterableHelper.executeBatched(indexListIds, ModelConstants.PARAMETER_LIMIT, batchedIds -> {
			CriteriaQuery<CaseIndexDto> cq = listQueryBuilder.buildIndexCriteria(caseCriteria, sortProperties, batchedIds);
			cases.addAll(QueryHelper.getResultList(em, cq, null, null));
		});

		List<Long> caseIds = cases.stream().map(CaseIndexDto::getId).collect(Collectors.toList());

		Map<String, ExternalShareInfoCountAndLatestDate> survToolShareCountAndDates = null;
		if (externalSurveillanceToolGatewayFacade.isFeatureEnabled()) {
			survToolShareCountAndDates = externalShareInfoService.getCaseShareCountAndLatestDate(caseIds)
					.stream()
					.collect(Collectors.toMap(ExternalShareInfoCountAndLatestDate::getAssociatedObjectUuid, Function.identity()));
		}

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		for (CaseIndexDto caze : cases) {
			if (survToolShareCountAndDates != null) {
				ExternalShareInfoCountAndLatestDate survToolShareCountAndDate = survToolShareCountAndDates.get(caze.getUuid());

				if (survToolShareCountAndDate != null) {
					caze.setSurveillanceToolShareCount(survToolShareCountAndDate.getCount());
					caze.setSurveillanceToolLastShareDate(survToolShareCountAndDate.getLatestDate());
					caze.setSurveillanceToolStatus(survToolShareCountAndDate.getLatestStatus());
				}
			}

			Boolean isInJurisdiction = caze.getInJurisdiction();
			pseudonymizer.pseudonymizeDto(
					CaseIndexDto.class,
					caze,
					isInJurisdiction,
					c -> pseudonymizer.pseudonymizeDto(AgeAndBirthDateDto.class, caze.getAgeAndBirthDate(), isInJurisdiction, null));

			if (diseaseConfigurationFacade.hasFollowUp(caze.getDisease())) {
				int numberOfMissedVisits =
						FollowUpLogic.getNumberOfRequiredVisitsSoFar(caze.getReportDate(), caze.getFollowUpUntil()) - caze.getVisitCount();
				if (numberOfMissedVisits < 0) {
					numberOfMissedVisits = 0;
				}
				caze.setMissedVisitsCount(numberOfMissedVisits);
			}
		}

		return cases;
	}



	@Override
	public List<CaseIndexDetailedDto> getIndexDetailedList(CaseCriteria caseCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

		CriteriaQuery<CaseIndexDetailedDto> cq = listQueryBuilder.buildIndexDetailedCriteria(caseCriteria, sortProperties);

		List<CaseIndexDetailedDto> cases = QueryHelper.getResultList(em, cq, first, max);

		// Load latest events info
		// Adding a second query here is not perfect, but selecting the last event with a criteria query
		// doesn't seem to be possible and using a native query is not an option because of user filters
		List<EventSummaryDetails> eventSummaries =
				eventService.getEventSummaryDetailsByCases(cases.stream().map(CaseIndexDetailedDto::getId).collect(Collectors.toList()));

		Map<String, ExternalShareInfoCountAndLatestDate> survToolShareCountAndDates = null;
		if (externalSurveillanceToolGatewayFacade.isFeatureEnabled()) {
			survToolShareCountAndDates =
					externalShareInfoService.getCaseShareCountAndLatestDate(cases.stream().map(CaseIndexDto::getId).collect(Collectors.toList()))
							.stream()
							.collect(Collectors.toMap(ExternalShareInfoCountAndLatestDate::getAssociatedObjectUuid, Function.identity()));
		}

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		for (CaseIndexDetailedDto caze : cases) {
			if (survToolShareCountAndDates != null) {
				ExternalShareInfoCountAndLatestDate survToolShareCountAndDate = survToolShareCountAndDates.get(caze.getUuid());

				if (survToolShareCountAndDate != null) {
					caze.setSurveillanceToolShareCount(survToolShareCountAndDate.getCount());
					caze.setSurveillanceToolLastShareDate(survToolShareCountAndDate.getLatestDate());
					caze.setSurveillanceToolStatus(survToolShareCountAndDate.getLatestStatus());
				}
			}

			if (caze.getEventCount() > 0) {
				eventSummaries.stream()
						.filter(v -> v.getCaseId() == caze.getId())
						.max(Comparator.comparing(EventSummaryDetails::getEventDate))
						.ifPresent(eventSummary -> {
							caze.setLatestEventId(eventSummary.getEventUuid());
							caze.setLatestEventStatus(eventSummary.getEventStatus());
							caze.setLatestEventTitle(eventSummary.getEventTitle());
						});
			}

			Boolean isInJurisdiction = caze.getInJurisdiction();
			pseudonymizer.pseudonymizeDto(CaseIndexDetailedDto.class, caze, isInJurisdiction, c -> {
				pseudonymizer.pseudonymizeDto(AgeAndBirthDateDto.class, caze.getAgeAndBirthDate(), isInJurisdiction, null);
				pseudonymizer
						.pseudonymizeUser(userService.getByUuid(caze.getReportingUser().getUuid()), userService.getCurrentUser(), caze::setReportingUser);
			});
		}

		return cases;
	}

	@Override
	public List<CaseSelectionDto> getCaseSelectionList(CaseCriteria caseCriteria) {
		List<CaseSelectionDto> entries = service.getCaseSelectionList(caseCriteria);

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		pseudonymizer.pseudonymizeDtoCollection(CaseSelectionDto.class, entries, CaseSelectionDto::isInJurisdiction, null);

		return entries;
	}

	@Override
	public List<CaseListEntryDto> getEntriesList(String personUuid, Integer first, Integer max) {

		Long personId = personFacade.getPersonIdByUuid(personUuid);
		List<CaseListEntryDto> entries = service.getEntriesList(personId, first, max);

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		pseudonymizer.pseudonymizeDtoCollection(CaseListEntryDto.class, entries, CaseListEntryDto::isInJurisdiction, null);

		return entries;
	}

	@RightsAllowed({
			UserRight._CASE_EDIT })
	public CaseDataDto postUpdate(String uuid, JsonNode caseDataDtoJson) {
		CaseDataDto existingCaseDto = getCaseDataWithoutPseudonyimization(uuid);
		PatchHelper.postUpdate(caseDataDtoJson, existingCaseDto);

		return this.save(existingCaseDto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RightsAllowed(UserRight._CASE_EXPORT)
	public List<CaseExportDto> getExportList(
			CaseCriteria caseCriteria,
			Collection<String> selectedRows,
			CaseExportType exportType,
			int first,
			int max,
			ExportConfigurationDto exportConfiguration,
			Language userLanguage) {

		Boolean previousCaseManagementDataCriteria = caseCriteria.getMustHaveCaseManagementData();
		if (CaseExportType.CASE_MANAGEMENT == exportType) {
			caseCriteria.setMustHaveCaseManagementData(Boolean.TRUE);
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseExportDto> cq = cb.createQuery(CaseExportDto.class);
		Root<Case> caseRoot = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caseRoot);
		final CaseJoins joins = caseQueryContext.getJoins();

		// Events count subquery
		Subquery<Long> eventCountSq = cq.subquery(Long.class);
		Root<EventParticipant> eventCountRoot = eventCountSq.from(EventParticipant.class);
		Join<EventParticipant, Event> event = eventCountRoot.join(EventParticipant.EVENT, JoinType.INNER);
		Join<EventParticipant, Case> resultingCase = eventCountRoot.join(EventParticipant.RESULTING_CASE, JoinType.INNER);
		eventCountSq.where(
				cb.and(
						cb.equal(resultingCase.get(Case.ID), caseRoot.get(Case.ID)),
						cb.isFalse(event.get(Event.DELETED)),
						cb.isFalse(eventCountRoot.get(EventParticipant.DELETED))));
		eventCountSq.select(cb.countDistinct(event.get(Event.ID)));

		Subquery<Long> prescriptionCountSq = cq.subquery(Long.class);
		Root<Prescription> prescriptionCountRoot = prescriptionCountSq.from(Prescription.class);
		Join<Prescription, Therapy> prescriptionTherapyJoin = prescriptionCountRoot.join(Prescription.THERAPY, JoinType.LEFT);
		prescriptionCountSq.where(cb.and(cb.equal(prescriptionTherapyJoin.get(Therapy.ID), caseRoot.get(Case.THERAPY).get(Therapy.ID))));
		prescriptionCountSq.select(cb.countDistinct(prescriptionCountRoot.get(Prescription.ID)));

		Subquery<Long> treatmentCountSq = cq.subquery(Long.class);
		Root<Treatment> treatmentCountRoot = treatmentCountSq.from(Treatment.class);
		Join<Treatment, Therapy> treatmentTherapyJoin = treatmentCountRoot.join(Treatment.THERAPY, JoinType.LEFT);
		treatmentCountSq.where(cb.and(cb.equal(treatmentTherapyJoin.get(Therapy.ID), caseRoot.get(Case.THERAPY).get(Therapy.ID))));
		treatmentCountSq.select(cb.countDistinct(treatmentCountRoot.get(Treatment.ID)));

		boolean exportGpsCoordinates = ExportHelper.shouldExportFields(exportConfiguration, PersonDto.ADDRESS, CaseExportDto.ADDRESS_GPS_COORDINATES);
		boolean exportPrescriptionNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.NUMBER_OF_PRESCRIPTIONS);
		boolean exportTreatmentNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.NUMBER_OF_TREATMENTS);
		boolean exportClinicalVisitNumber = (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT)
				&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.NUMBER_OF_CLINICAL_VISITS);
		boolean exportOutbreakInfo = ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.ASSOCIATED_WITH_OUTBREAK);

		//@formatter:off
		cq.multiselect(caseRoot.get(Case.ID), joins.getPerson().get(Person.ID),
				joins.getPersonAddress().get(Location.ID),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LATITUDE) : cb.nullLiteral(Double.class),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LONGITUDE) : cb.nullLiteral(Double.class),
				exportGpsCoordinates ? joins.getPersonAddress().get(Location.LATLONACCURACY) : cb.nullLiteral(Float.class),
				joins.getEpiData().get(EpiData.ID),
				joins.getRoot().get(Case.SYMPTOMS).get(Symptoms.ID),
				joins.getHospitalization().get(Hospitalization.ID),
				joins.getRoot().get(Case.HEALTH_CONDITIONS).get(HealthConditions.ID),
				caseRoot.get(Case.UUID),
				caseRoot.get(Case.EPID_NUMBER), caseRoot.get(Case.DISEASE), caseRoot.get(Case.DISEASE_VARIANT), caseRoot.get(Case.DISEASE_DETAILS),
				caseRoot.get(Case.DISEASE_VARIANT_DETAILS), joins.getPerson().get(Person.UUID), joins.getPerson().get(Person.FIRST_NAME), joins.getPerson().get(Person.LAST_NAME),
				joins.getPerson().get(Person.SALUTATION), joins.getPerson().get(Person.OTHER_SALUTATION), joins.getPerson().get(Person.SEX),
				caseRoot.get(Case.PREGNANT), joins.getPerson().get(Person.APPROXIMATE_AGE),
				joins.getPerson().get(Person.APPROXIMATE_AGE_TYPE), joins.getPerson().get(Person.BIRTHDATE_DD),
				joins.getPerson().get(Person.BIRTHDATE_MM), joins.getPerson().get(Person.BIRTHDATE_YYYY),
				caseRoot.get(Case.REPORT_DATE), joins.getRegion().get(Region.NAME),
				joins.getDistrict().get(District.NAME), joins.getCommunity().get(Community.NAME),
				caseRoot.get(Case.FACILITY_TYPE),
				joins.getFacility().get(Facility.NAME), joins.getFacility().get(Facility.UUID), caseRoot.get(Case.HEALTH_FACILITY_DETAILS),
				joins.getPointOfEntry().get(PointOfEntry.NAME), joins.getPointOfEntry().get(PointOfEntry.UUID), caseRoot.get(Case.POINT_OF_ENTRY_DETAILS),
				caseRoot.get(Case.CASE_CLASSIFICATION),
				caseRoot.get(Case.CLINICAL_CONFIRMATION), caseRoot.get(Case.EPIDEMIOLOGICAL_CONFIRMATION), caseRoot.get(Case.LABORATORY_DIAGNOSTIC_CONFIRMATION),
				caseRoot.get(Case.NOT_A_CASE_REASON_NEGATIVE_TEST),
				caseRoot.get(Case.NOT_A_CASE_REASON_PHYSICIAN_INFORMATION), caseRoot.get(Case.NOT_A_CASE_REASON_DIFFERENT_PATHOGEN),
				caseRoot.get(Case.NOT_A_CASE_REASON_OTHER), caseRoot.get(Case.NOT_A_CASE_REASON_DETAILS),
				caseRoot.get(Case.INVESTIGATION_STATUS), caseRoot.get(Case.INVESTIGATED_DATE),
				caseRoot.get(Case.OUTCOME), caseRoot.get(Case.OUTCOME_DATE),
				caseRoot.get(Case.SEQUELAE), caseRoot.get(Case.SEQUELAE_DETAILS),
				caseRoot.get(Case.BLOOD_ORGAN_OR_TISSUE_DONATED),
				caseRoot.get(Case.FOLLOW_UP_STATUS), caseRoot.get(Case.FOLLOW_UP_UNTIL),
				caseRoot.get(Case.NOSOCOMIAL_OUTBREAK), caseRoot.get(Case.INFECTION_SETTING),
				caseRoot.get(Case.PROHIBITION_TO_WORK), caseRoot.get(Case.PROHIBITION_TO_WORK_FROM), caseRoot.get(Case.PROHIBITION_TO_WORK_UNTIL),
				caseRoot.get(Case.RE_INFECTION), caseRoot.get(Case.PREVIOUS_INFECTION_DATE), caseRoot.get(Case.REINFECTION_STATUS), caseRoot.get(Case.REINFECTION_DETAILS),
				// quarantine
				caseRoot.get(Case.QUARANTINE), caseRoot.get(Case.QUARANTINE_TYPE_DETAILS), caseRoot.get(Case.QUARANTINE_FROM), caseRoot.get(Case.QUARANTINE_TO),
				caseRoot.get(Case.QUARANTINE_HELP_NEEDED),
				caseRoot.get(Case.QUARANTINE_ORDERED_VERBALLY),
				caseRoot.get(Case.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT),
				caseRoot.get(Case.QUARANTINE_ORDERED_VERBALLY_DATE),
				caseRoot.get(Case.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT_DATE),
				caseRoot.get(Case.QUARANTINE_EXTENDED),
				caseRoot.get(Case.QUARANTINE_REDUCED),
				caseRoot.get(Case.QUARANTINE_OFFICIAL_ORDER_SENT),
				caseRoot.get(Case.QUARANTINE_OFFICIAL_ORDER_SENT_DATE),

				joins.getHospitalization().get(Hospitalization.ADMITTED_TO_HEALTH_FACILITY), joins.getHospitalization().get(Hospitalization.ADMISSION_DATE),
				joins.getHospitalization().get(Hospitalization.DISCHARGE_DATE), joins.getHospitalization().get(Hospitalization.LEFT_AGAINST_ADVICE),
				joins.getPerson().get(Person.PRESENT_CONDITION), joins.getPerson().get(Person.DEATH_DATE), joins.getPerson().get(Person.BURIAL_DATE),
				joins.getPerson().get(Person.BURIAL_CONDUCTOR), joins.getPerson().get(Person.BURIAL_PLACE_DESCRIPTION),
				// address
				joins.getPersonAddressRegion().get(Region.NAME), joins.getPersonAddressDistrict().get(District.NAME), joins.getPersonAddressCommunity().get(Community.NAME),
				joins.getPersonAddress().get(Location.CITY), joins.getPersonAddress().get(Location.STREET), joins.getPersonAddress().get(Location.HOUSE_NUMBER),
				joins.getPersonAddress().get(Location.ADDITIONAL_INFORMATION), joins.getPersonAddress().get(Location.POSTAL_CODE),
				joins.getPersonAddressFacility().get(Facility.NAME), joins.getPersonAddressFacility().get(Facility.UUID), joins.getPersonAddress().get(Location.FACILITY_DETAILS),
				// phone
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_PHONE_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_PHONE_OWNER_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_EMAIL_SUBQUERY),
				caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_OTHER_CONTACT_DETAILS_SUBQUERY),
				joins.getPerson().get(Person.EDUCATION_TYPE),
				joins.getPerson().get(Person.EDUCATION_DETAILS), joins.getPerson().get(Person.OCCUPATION_TYPE),
				joins.getPerson().get(Person.OCCUPATION_DETAILS), joins.getPerson().get(Person.ARMED_FORCES_RELATION_TYPE), joins.getEpiData().get(EpiData.CONTACT_WITH_SOURCE_CASE_KNOWN),
				caseRoot.get(Case.VACCINATION_STATUS), caseRoot.get(Case.POSTPARTUM), caseRoot.get(Case.TRIMESTER),
				eventCountSq,
				exportPrescriptionNumber ? prescriptionCountSq : cb.nullLiteral(Long.class),
				exportTreatmentNumber ? treatmentCountSq : cb.nullLiteral(Long.class),
				exportClinicalVisitNumber ? clinicalVisitSq(cb, cq, caseRoot) : cb.nullLiteral(Long.class),
				caseRoot.get(Case.EXTERNAL_ID),
				caseRoot.get(Case.EXTERNAL_TOKEN),
				caseRoot.get(Case.INTERNAL_TOKEN),
				joins.getPerson().get(Person.BIRTH_NAME),
				joins.getPersonBirthCountry().get(Country.ISO_CODE),
				joins.getPersonBirthCountry().get(Country.DEFAULT_NAME),
				joins.getPersonCitizenship().get(Country.ISO_CODE),
				joins.getPersonCitizenship().get(Country.DEFAULT_NAME),
				caseRoot.get(Case.CASE_IDENTIFICATION_SOURCE),
				caseRoot.get(Case.SCREENING_TYPE),
				// responsible jurisdiction
				joins.getResponsibleRegion().get(Region.NAME),
				joins.getResponsibleDistrict().get(District.NAME),
				joins.getResponsibleCommunity().get(Community.NAME),
				caseRoot.get(Case.CLINICIAN_NAME),
				caseRoot.get(Case.CLINICIAN_PHONE),
				caseRoot.get(Case.CLINICIAN_EMAIL),
				caseRoot.get(Case.REPORTING_USER).get(User.ID),
				caseRoot.get(Case.FOLLOW_UP_STATUS_CHANGE_USER).get(User.ID),
				caseRoot.get(Case.PREVIOUS_QUARANTINE_TO),
				caseRoot.get(Case.QUARANTINE_CHANGE_COMMENT),
				exportOutbreakInfo ? cb.selectCase().when(cb.exists(outbreakSq(caseQueryContext)), cb.literal(I18nProperties.getString(Strings.yes)))
						.otherwise(cb.literal(I18nProperties.getString(Strings.no))) : cb.nullLiteral(String.class),
				JurisdictionHelper.booleanSelector(cb, service.inJurisdictionOrOwned(caseQueryContext)));
		//@formatter:on

		cq.distinct(true);

		Predicate filter = service.createUserFilter(caseQueryContext);

		if (caseCriteria != null) {
			Predicate criteriaFilter = service.createCriteriaFilter(caseCriteria, caseQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}
		filter = CriteriaBuilderHelper.andInValues(selectedRows, filter, cb, caseRoot.get(Case.UUID));

		if (filter != null) {
			cq.where(filter);
		}

		/*
		 * Sort by report date DESC, but also by id for stable Sorting in case of equal report dates.
		 * Since this method supports paging, values might jump between pages when sorting is unstable.
		 */
		cq.orderBy(cb.desc(caseRoot.get(Case.REPORT_DATE)), cb.desc(caseRoot.get(Case.ID)));

		List<CaseExportDto> resultList = QueryHelper.getResultList(em, cq, first, max);

		List<Long> resultCaseIds = resultList.stream().map(CaseExportDto::getId).collect(Collectors.toList());
		if (!resultList.isEmpty()) {
			List<Symptoms> symptomsList = null;
			CriteriaQuery<Symptoms> symptomsCq = cb.createQuery(Symptoms.class);
			Root<Symptoms> symptomsRoot = symptomsCq.from(Symptoms.class);
			Expression<String> symptomsIdsExpr = symptomsRoot.get(Symptoms.ID);
			symptomsCq.where(symptomsIdsExpr.in(resultList.stream().map(CaseExportDto::getSymptomsId).collect(Collectors.toList())));
			symptomsList = em.createQuery(symptomsCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
			Map<Long, Symptoms> symptoms = symptomsList.stream().collect(Collectors.toMap(Symptoms::getId, Function.identity()));

			Map<Long, HealthConditions> healthConditions = null;
			if (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT) {
				if (ExportHelper.shouldExportFields(exportConfiguration, CaseDataDto.HEALTH_CONDITIONS)) {
					List<HealthConditions> healthConditionsList = null;
					CriteriaQuery<HealthConditions> healthConditionsCq = cb.createQuery(HealthConditions.class);
					Root<HealthConditions> healthConditionsRoot = healthConditionsCq.from(HealthConditions.class);
					Expression<String> healthConditionsIdsExpr = healthConditionsRoot.get(HealthConditions.ID);
					healthConditionsCq.where(
							healthConditionsIdsExpr.in(resultList.stream().map(CaseExportDto::getHealthConditionsId).collect(Collectors.toList())));
					healthConditionsList = em.createQuery(healthConditionsCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
					healthConditions = healthConditionsList.stream().collect(Collectors.toMap(HealthConditions::getId, Function.identity()));
				}
			}

			Map<Long, PreviousHospitalization> firstPreviousHospitalizations = null;
			if (ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.INITIAL_DETECTION_PLACE)) {
				List<PreviousHospitalization> prevHospsList = null;
				CriteriaQuery<PreviousHospitalization> prevHospsCq = cb.createQuery(PreviousHospitalization.class);
				Root<PreviousHospitalization> prevHospsRoot = prevHospsCq.from(PreviousHospitalization.class);
				Join<PreviousHospitalization, Hospitalization> prevHospsHospitalizationJoin =
						prevHospsRoot.join(PreviousHospitalization.HOSPITALIZATION, JoinType.LEFT);
				Expression<String> hospitalizationIdsExpr = prevHospsHospitalizationJoin.get(Hospitalization.ID);
				prevHospsCq
						.where(hospitalizationIdsExpr.in(resultList.stream().map(CaseExportDto::getHospitalizationId).collect(Collectors.toList())));
				prevHospsCq.orderBy(cb.asc(prevHospsRoot.get(PreviousHospitalization.ADMISSION_DATE)));
				prevHospsList = em.createQuery(prevHospsCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
				firstPreviousHospitalizations =
						prevHospsList.stream().collect(Collectors.toMap(p -> p.getHospitalization().getId(), Function.identity(), (id1, id2) -> id1));
			}

			Map<Long, CaseClassification> sourceCaseClassifications = null;
			if (ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.MAX_SOURCE_CASE_CLASSIFICATION)) {
				sourceCaseClassifications = contactService.getSourceCaseClassifications(resultCaseIds)
						.stream()
						.collect(
								Collectors
										.toMap(e -> (Long) e[0], e -> (CaseClassification) e[1], (c1, c2) -> c1.getSeverity() >= c2.getSeverity() ? c1 : c2));
			}

			Map<Long, List<Exposure>> exposures = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& ExportHelper
					.shouldExportFields(exportConfiguration, CaseExportDto.TRAVELED, CaseExportDto.TRAVEL_HISTORY, CaseExportDto.BURIAL_ATTENDED)) {
				CriteriaQuery<Exposure> exposuresCq = cb.createQuery(Exposure.class);
				Root<Exposure> exposuresRoot = exposuresCq.from(Exposure.class);
				Join<Exposure, EpiData> exposuresEpiDataJoin = exposuresRoot.join(Exposure.EPI_DATA, JoinType.LEFT);
				Expression<String> epiDataIdsExpr = exposuresEpiDataJoin.get(EpiData.ID);
				Predicate exposuresPredicate = cb.and(
						epiDataIdsExpr.in(resultList.stream().map(CaseExportDto::getEpiDataId).collect(Collectors.toList())),
						cb.or(
								cb.equal(exposuresRoot.get(Exposure.EXPOSURE_TYPE), ExposureType.TRAVEL),
								cb.equal(exposuresRoot.get(Exposure.EXPOSURE_TYPE), ExposureType.BURIAL)));
				exposuresCq.where(exposuresPredicate);
				exposuresCq.orderBy(cb.asc(exposuresEpiDataJoin.get(EpiData.ID)));
				List<Exposure> exposureList = em.createQuery(exposuresCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
				exposures = exposureList.stream().collect(Collectors.groupingBy(e -> e.getEpiData().getId()));
			}

			Map<Long, List<EmbeddedSampleExportDto>> samples = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& ExportHelper.shouldExportFields(exportConfiguration, CaseExportDto.SAMPLE_INFORMATION)) {
				List<EmbeddedSampleExportDto> samplesList = null;
				CriteriaQuery<EmbeddedSampleExportDto> samplesCq = cb.createQuery(EmbeddedSampleExportDto.class);
				Root<Sample> samplesRoot = samplesCq.from(Sample.class);
				Join<Sample, Case> samplesCaseJoin = samplesRoot.join(Sample.ASSOCIATED_CASE, JoinType.LEFT);
				Expression<String> caseIdsExpr = samplesCaseJoin.get(Case.ID);
				samplesCq.multiselect(
						samplesRoot.get(Sample.UUID),
						samplesRoot.get(Sample.SAMPLE_DATE_TIME),
						samplesRoot.get(Sample.LAB).get(Facility.UUID),
						samplesRoot.get(Sample.LAB).get(Facility.NAME),
						samplesRoot.get(Sample.LAB_DETAILS),
						samplesRoot.get(Sample.PATHOGEN_TEST_RESULT),
						caseIdsExpr);

				Predicate eliminateDeletedSamplesFilter = cb.equal(samplesRoot.get(Sample.DELETED), false);
				samplesCq.where(caseIdsExpr.in(resultCaseIds), eliminateDeletedSamplesFilter);
				samplesList = em.createQuery(samplesCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
				samples = samplesList.stream().collect(Collectors.groupingBy(s -> s.getCaseId()));
			}

			List<VisitSummaryExportDetails> visitSummaries = null;
			if (featureConfigurationFacade.isFeatureEnabled(FeatureType.CASE_FOLLOWUP)
					&& ExportHelper.shouldExportFields(
					exportConfiguration,
					CaseExportDto.NUMBER_OF_VISITS,
					CaseExportDto.LAST_COOPERATIVE_VISIT_DATE,
					CaseExportDto.LAST_COOPERATIVE_VISIT_SYMPTOMATIC,
					CaseExportDto.LAST_COOPERATIVE_VISIT_SYMPTOMS)) {
				CriteriaQuery<VisitSummaryExportDetails> visitsCq = cb.createQuery(VisitSummaryExportDetails.class);
				Root<Case> visitsCqRoot = visitsCq.from(Case.class);
				Join<Case, Visit> visitsJoin = visitsCqRoot.join(Case.VISITS, JoinType.LEFT);
				Join<Visit, Symptoms> visitSymptomsJoin = visitsJoin.join(Visit.SYMPTOMS, JoinType.LEFT);

				visitsCq.where(
						CriteriaBuilderHelper
								.and(cb, visitsCqRoot.get(AbstractDomainObject.ID).in(resultCaseIds), cb.isNotEmpty(visitsCqRoot.get(Case.VISITS))));
				visitsCq.multiselect(
						visitsCqRoot.get(AbstractDomainObject.ID),
						visitsJoin.get(Visit.VISIT_DATE_TIME),
						visitsJoin.get(Visit.VISIT_STATUS),
						visitSymptomsJoin);

				visitSummaries = em.createQuery(visitsCq).getResultList();
			}

			Map<Long, List<Immunization>> immunizations = null;
			if ((exportType == null || exportType == CaseExportType.CASE_SURVEILLANCE)
					&& (exportConfiguration == null
					|| exportConfiguration.getProperties()
					.stream()
					.anyMatch(p -> StringUtils.equalsAny(p, ExportHelper.getVaccinationExportProperties())))) {
				List<Immunization> immunizationList;
				CriteriaQuery<Immunization> immunizationsCq = cb.createQuery(Immunization.class);
				Root<Immunization> immunizationsCqRoot = immunizationsCq.from(Immunization.class);
				Join<Immunization, Person> personJoin = immunizationsCqRoot.join(Immunization.PERSON, JoinType.LEFT);
				Expression<String> personIdsExpr = personJoin.get(Person.ID);
				immunizationsCq.where(
						CriteriaBuilderHelper.and(
								cb,
								cb.or(
										cb.equal(immunizationsCqRoot.get(Immunization.MEANS_OF_IMMUNIZATION), MeansOfImmunization.VACCINATION),
										cb.equal(immunizationsCqRoot.get(Immunization.MEANS_OF_IMMUNIZATION), MeansOfImmunization.VACCINATION_RECOVERY)),
								personIdsExpr.in(resultList.stream().map(CaseExportDto::getPersonId).collect(Collectors.toList()))));
				immunizationsCq.select(immunizationsCqRoot);
				immunizationList = em.createQuery(immunizationsCq).setHint(ModelConstants.READ_ONLY, true).getResultList();
				immunizations = immunizationList.stream().collect(Collectors.groupingBy(i -> i.getPerson().getId()));
			}

			// Load latest events info
			// Adding a second query here is not perfect, but selecting the last event with a criteria query
			// doesn't seem to be possible and using a native query is not an option because of user filters
			List<EventSummaryDetails> eventSummaries = null;
			if (ExportHelper.shouldExportFields(
					exportConfiguration,
					CaseExportDto.LATEST_EVENT_ID,
					CaseExportDto.LATEST_EVENT_STATUS,
					CaseExportDto.LATEST_EVENT_TITLE)) {

				eventSummaries = eventService.getEventSummaryDetailsByCases(resultCaseIds);
			}

			Map<Long, UserReference> caseUsers = getCaseUsersForExport(resultList, exportConfiguration);

			Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician(I18nProperties.getCaption(Captions.inaccessibleValue));

			for (CaseExportDto exportDto : resultList) {
				final boolean inJurisdiction = exportDto.getInJurisdiction();

				if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.COUNTRY)) {
					exportDto.setCountry(configFacade.getEpidPrefix());
				}
				if (ExportHelper.shouldExportFields(exportConfiguration, CaseDataDto.SYMPTOMS)) {
					Optional.ofNullable(symptoms.get(exportDto.getSymptomsId()))
							.ifPresent(symptom -> exportDto.setSymptoms(SymptomsFacadeEjb.toSymptomsDto(symptom)));
				}
				if (healthConditions != null) {
					Optional.ofNullable(healthConditions.get(exportDto.getHealthConditionsId()))
							.ifPresent(healthCondition -> exportDto.setHealthConditions(HealthConditionsMapper.toDto(healthCondition)));
				}
				if (firstPreviousHospitalizations != null) {
					Optional.ofNullable(firstPreviousHospitalizations.get(exportDto.getHospitalizationId()))
							.ifPresent(firstPreviousHospitalization -> {
								if (firstPreviousHospitalization.getHealthFacility() != null) {
									exportDto.setInitialDetectionPlace(
											FacilityHelper.buildFacilityString(
													firstPreviousHospitalization.getHealthFacility().getUuid(),
													firstPreviousHospitalization.getHealthFacility().getName(),
													firstPreviousHospitalization.getHealthFacilityDetails()));
								} else {
									exportDto.setInitialDetectionPlace(I18nProperties.getCaption(Captions.unknown));
								}
							});
					if (StringUtils.isEmpty(exportDto.getInitialDetectionPlace())) {
						if (!StringUtils.isEmpty(exportDto.getHealthFacility())) {
							exportDto.setInitialDetectionPlace(exportDto.getHealthFacility());
						} else {
							exportDto.setInitialDetectionPlace(exportDto.getPointOfEntry());
						}
					}
				}
				if (sourceCaseClassifications != null) {
					Optional.ofNullable(sourceCaseClassifications.get(exportDto.getId()))
							.ifPresent(sourceCaseClassification -> exportDto.setMaxSourceCaseClassification(sourceCaseClassification));
				}
				if (exposures != null) {
					Optional.ofNullable(exposures.get(exportDto.getEpiDataId())).ifPresent(caseExposures -> {
						StringBuilder travelHistoryBuilder = new StringBuilder();
						if (caseExposures.stream().anyMatch(e -> ExposureType.BURIAL.equals(e.getExposureType()))) {
							exportDto.setBurialAttended(true);
						}
						caseExposures.stream().filter(e -> ExposureType.TRAVEL.equals(e.getExposureType())).forEach(exposure -> {
							Location location = exposure.getLocation();
							travelHistoryBuilder.append(
											EpiDataHelper.buildDetailedTravelString(
													LocationReferenceDto.buildCaption(
															location.getRegion() != null ? location.getRegion().getName() : null,
															location.getDistrict() != null ? location.getDistrict().getName() : null,
															location.getCommunity() != null ? location.getCommunity().getName() : null,
															location.getCity(),
															location.getStreet(),
															location.getHouseNumber(),
															location.getAdditionalInformation()),
													exposure.getDescription(),
													exposure.getStartDate(),
													exposure.getEndDate(),
													userLanguage))
									.append(", ");
						});
						if (travelHistoryBuilder.length() > 0) {
							exportDto.setTraveled(true);
							travelHistoryBuilder.delete(travelHistoryBuilder.lastIndexOf(", "), travelHistoryBuilder.length() - 1);
						}
						exportDto.setTravelHistory(travelHistoryBuilder.toString());
					});
				}
				if (samples != null) {
					Optional.ofNullable(samples.get(exportDto.getId())).ifPresent(caseSamples -> {
						int count = 0;
						caseSamples.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
						for (EmbeddedSampleExportDto sampleDto : caseSamples) {

							switch (++count) {
								case 1:
									exportDto.setSample1(sampleDto);
									break;
								case 2:
									exportDto.setSample2(sampleDto);
									break;
								case 3:
									exportDto.setSample3(sampleDto);
									break;
								default:
									exportDto.addOtherSample(sampleDto);
							}
						}
					});
				}
				if (immunizations != null) {
					Optional.ofNullable(immunizations.get(exportDto.getPersonId())).ifPresent(caseImmunizations -> {
						List<Immunization> filteredImmunizations =
								caseImmunizations.stream().filter(i -> i.getDisease() == exportDto.getDisease()).collect(Collectors.toList());
						if (!filteredImmunizations.isEmpty()) {
							filteredImmunizations.sort(Comparator.comparing(i -> ImmunizationEntityHelper.getDateForComparison(i, false)));
							Immunization mostRecentImmunization = filteredImmunizations.get(filteredImmunizations.size() - 1);
							Integer numberOfDoses = mostRecentImmunization.getNumberOfDoses();
							Date onsetDate = Optional.ofNullable(symptoms.get(exportDto.getSymptomsId())).map(Symptoms::getOnsetDate).orElse(null);

							List<Vaccination> relevantSortedVaccinations = vaccinationService.getRelevantSortedVaccinations(
									filteredImmunizations.stream().flatMap(i -> i.getVaccinations().stream()).collect(Collectors.toList()),
									onsetDate,
									exportDto.getReportDate());
							Vaccination firstVaccination = null;
							Vaccination lastVaccination = null;

							if (CollectionUtils.isNotEmpty(relevantSortedVaccinations)) {
								firstVaccination = relevantSortedVaccinations.get(0);
								lastVaccination = relevantSortedVaccinations.get(relevantSortedVaccinations.size() - 1);
								exportDto.setFirstVaccinationDate(firstVaccination.getVaccinationDate());
								exportDto.setLastVaccinationDate(lastVaccination.getVaccinationDate());
								exportDto.setVaccineName(lastVaccination.getVaccineName());
								exportDto.setOtherVaccineName(lastVaccination.getOtherVaccineName());
								exportDto.setVaccineManufacturer(lastVaccination.getVaccineManufacturer());
								exportDto.setOtherVaccineManufacturer(lastVaccination.getOtherVaccineManufacturer());
								exportDto.setVaccinationInfoSource(lastVaccination.getVaccinationInfoSource());
								exportDto.setVaccineAtcCode(lastVaccination.getVaccineAtcCode());
								exportDto.setVaccineBatchNumber(lastVaccination.getVaccineBatchNumber());
								exportDto.setVaccineUniiCode(lastVaccination.getVaccineUniiCode());
								exportDto.setVaccineInn(lastVaccination.getVaccineInn());
							}

							exportDto.setNumberOfDoses(
									numberOfDoses != null ? String.valueOf(numberOfDoses) : getNumberOfDosesFromVaccinations(lastVaccination));
						}
					});
				}
				if (visitSummaries != null) {
					List<VisitSummaryExportDetails> visits =
							visitSummaries.stream().filter(v -> v.getContactId() == exportDto.getId()).collect(Collectors.toList());

					VisitSummaryExportDetails lastCooperativeVisit = visits.stream()
							.filter(v -> v.getVisitStatus() == VisitStatus.COOPERATIVE)
							.max(Comparator.comparing(VisitSummaryExportDetails::getVisitDateTime))
							.orElse(null);

					exportDto.setNumberOfVisits(visits.size());
					if (lastCooperativeVisit != null) {
						exportDto.setLastCooperativeVisitDate(lastCooperativeVisit.getVisitDateTime());

						SymptomsDto visitSymptoms = SymptomsFacadeEjb.toSymptomsDto(lastCooperativeVisit.getSymptoms());
						pseudonymizer.pseudonymizeDto(SymptomsDto.class, visitSymptoms, inJurisdiction, null);

						exportDto.setLastCooperativeVisitSymptoms(SymptomsHelper.buildSymptomsHumanString(visitSymptoms, true, userLanguage));
						exportDto.setLastCooperativeVisitSymptomatic(
								visitSymptoms.getSymptomatic() == null
										? YesNoUnknown.UNKNOWN
										: (visitSymptoms.getSymptomatic() ? YesNoUnknown.YES : YesNoUnknown.NO));
					}
				}

				if (eventSummaries != null && exportDto.getEventCount() != 0) {
					eventSummaries.stream()
							.filter(v -> v.getCaseId() == exportDto.getId())
							.max(Comparator.comparing(EventSummaryDetails::getEventDate))
							.ifPresent(eventSummary -> {
								exportDto.setLatestEventId(eventSummary.getEventUuid());
								exportDto.setLatestEventStatus(eventSummary.getEventStatus());
								exportDto.setLatestEventTitle(eventSummary.getEventTitle());
							});
				}

				if (!caseUsers.isEmpty()) {
					if (exportDto.getReportingUserId() != null) {
						UserReference user = caseUsers.get(exportDto.getReportingUserId());

						exportDto.setReportingUserName(user.getName());
						exportDto.setReportingUserRoles(
								user.getUserRoles().stream().map(userRole -> UserRoleFacadeEjb.toReferenceDto(userRole)).collect(Collectors.toSet()));
					}

					if (exportDto.getFollowUpStatusChangeUserId() != null) {
						UserReference user = caseUsers.get(exportDto.getFollowUpStatusChangeUserId());

						exportDto.setFollowUpStatusChangeUserName(user.getName());
						exportDto.setFollowUpStatusChangeUserRoles(
								user.getUserRoles().stream().map(userRole -> UserRoleFacadeEjb.toReferenceDto(userRole)).collect(Collectors.toSet()));
					}
				}

				pseudonymizer.pseudonymizeDto(CaseExportDto.class, exportDto, inJurisdiction, c -> {
					pseudonymizer.pseudonymizeDto(BirthDateDto.class, c.getBirthdate(), inJurisdiction, null);
					pseudonymizer.pseudonymizeDto(EmbeddedSampleExportDto.class, c.getSample1(), inJurisdiction, null);
					pseudonymizer.pseudonymizeDto(EmbeddedSampleExportDto.class, c.getSample2(), inJurisdiction, null);
					pseudonymizer.pseudonymizeDto(EmbeddedSampleExportDto.class, c.getSample3(), inJurisdiction, null);
					pseudonymizer.pseudonymizeDtoCollection(EmbeddedSampleExportDto.class, c.getOtherSamples(), s -> inJurisdiction, null);
					pseudonymizer.pseudonymizeDto(BurialInfoDto.class, c.getBurialInfo(), inJurisdiction, null);
					pseudonymizer.pseudonymizeDto(SymptomsDto.class, c.getSymptoms(), inJurisdiction, null);
				});
			}
		}

		caseCriteria.setMustHaveCaseManagementData(previousCaseManagementDataCriteria);

		return resultList;
	}

	private Subquery<Boolean> outbreakSq(CaseQueryContext caseQueryContext) {

		final CriteriaBuilder cb = caseQueryContext.getCriteriaBuilder();
		final CaseJoins joins = caseQueryContext.getJoins();
		final CriteriaQuery<?> cq = caseQueryContext.getQuery();
		final From<?, Case> caseRoot = caseQueryContext.getRoot();

		final Subquery<Boolean> outbreakSubquery = cq.subquery(Boolean.class);
		final Root<Outbreak> outbreakRoot = outbreakSubquery.from(Outbreak.class);
		final Join<Outbreak, District> districtJoin = outbreakRoot.join(Outbreak.DISTRICT, JoinType.LEFT);
		outbreakSubquery.select(outbreakRoot.get(Outbreak.ID));
		outbreakSubquery.where(cb.and(cb.equal(districtJoin.get(District.ID), joins.getDistrict().

						get(District.ID)),
				cb.equal(outbreakRoot.get(Outbreak.DISEASE), caseRoot.get(Case.DISEASE)),
				cb.lessThanOrEqualTo(outbreakRoot.get(Outbreak.START_DATE), caseRoot.get(Case.REPORT_DATE)),
				cb.or(
						cb.isNull(outbreakRoot.get(Outbreak.END_DATE)),
						cb.greaterThanOrEqualTo(outbreakRoot.get(Outbreak.END_DATE), caseRoot.get(Case.REPORT_DATE)))));
		return outbreakSubquery;
	}

	private <T> Subquery<Long> clinicalVisitSq(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<Case> caseRoot) {
		Subquery<Long> clinicalVisitCountSq = cq.subquery(Long.class);
		Root<ClinicalVisit> clinicalVisitRoot = clinicalVisitCountSq.from(ClinicalVisit.class);
		Join<ClinicalVisit, ClinicalCourse> clinicalVisitClinicalCourseJoin = clinicalVisitRoot.join(ClinicalVisit.CLINICAL_COURSE, JoinType.LEFT);
		clinicalVisitCountSq.where(
				cb.and(cb.equal(clinicalVisitClinicalCourseJoin.get(ClinicalCourse.ID), caseRoot.get(Case.CLINICAL_COURSE).get(ClinicalCourse.ID))));
		clinicalVisitCountSq.select(cb.countDistinct(clinicalVisitRoot.get(ClinicalVisit.ID)));
		return clinicalVisitCountSq;
	}

	private Map<Long, UserReference> getCaseUsersForExport(List<CaseExportDto> resultList, ExportConfigurationDto exportConfiguration) {
		Map<Long, UserReference> caseUsers = Collections.emptyMap();
		if (exportConfiguration == null
				|| exportConfiguration.getProperties().contains(CaseDataDto.REPORTING_USER)
				|| exportConfiguration.getProperties().contains(CaseDataDto.FOLLOW_UP_STATUS_CHANGE_USER)) {
			Set<Long> userIds = resultList.stream()
					.map((c -> Arrays.asList(c.getReportingUserId(), c.getFollowUpStatusChangeUserId())))
					.flatMap(Collection::stream)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			caseUsers = userService.getUserReferencesByIds(userIds).stream().collect(Collectors.toMap(UserReference::getId, Function.identity()));
		}

		return caseUsers;
	}

	private String getNumberOfDosesFromVaccinations(Vaccination vaccination) {
		return vaccination != null ? vaccination.getVaccineDose() : "";
	}

	@Override
	public List<String> getAllActiveUuids() {

		if (userService.getCurrentUser() == null) {
			return Collections.emptyList();
		}

		return service.getAllActiveUuids();
	}

	@RightsAllowed({
			UserRight._DASHBOARD_SURVEILLANCE_VIEW, })
	public Long countCasesForMap(
			RegionReferenceDto regionRef,
			DistrictReferenceDto districtRef,
			Disease disease,
			Date from,
			Date to,
			NewCaseDateType dateType) {
		Region region = regionService.getByReferenceDto(regionRef);
		District district = districtService.getByReferenceDto(districtRef);

		return service.countCasesForMap(region, district, disease, from, to, dateType);
	}

	@Override
	@RightsAllowed({
			UserRight._DASHBOARD_SURVEILLANCE_VIEW, })
	public List<MapCaseDto> getCasesForMap(
			RegionReferenceDto regionRef,
			DistrictReferenceDto districtRef,
			Disease disease,
			Date from,
			Date to,
			NewCaseDateType dateType) {

		Region region = regionService.getByReferenceDto(regionRef);
		District district = districtService.getByReferenceDto(districtRef);

		List<MapCaseDto> cases = service.getCasesForMap(region, district, disease, from, to, dateType);
		// todo shouldn't this also use the overridden createPseudonymizer method?
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		pseudonymizer.pseudonymizeDtoCollection(
				MapCaseDto.class,
				cases,
				MapCaseDto::getInJurisdiction,
				(c, isInJurisdiction) -> pseudonymizer.pseudonymizeDto(PersonReferenceDto.class, c.getPerson(), isInJurisdiction, null));

		return cases;
	}

	@Override
	public List<CaseDataDto> getAllCasesOfPerson(String personUuid) {

		List<Case> entities = service.findBy(new CaseCriteria().person(new PersonReferenceDto(personUuid)), false);
		return toPseudonymizedDtos(entities);
	}

	@Override
	public List<CaseReferenceDto> getRandomCaseReferences(CaseCriteria criteria, int count, Random randomGenerator) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caze = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);

		Predicate filter = service.createUserFilter(caseQueryContext, new CaseUserFilterCriteria().excludeCasesFromContacts(true));
		filter = CriteriaBuilderHelper.and(cb, filter, service.createCriteriaFilter(criteria, caseQueryContext));
		if (filter != null) {
			cq.where(filter);
		}

		cq.orderBy(cb.desc(caze.get(Case.UUID)));
		cq.select(caze.get(Case.UUID));

		List<String> uuids = em.createQuery(cq).getResultList();
		if (uuids.isEmpty()) {
			return null;
		}

		return randomGenerator.ints(count, 0, uuids.size()).mapToObj(i -> new CaseReferenceDto(uuids.get(i))).collect(Collectors.toList());
	}

	@Override
	public List<CaseSelectionDto> getSimilarCases(CaseSimilarityCriteria criteria) {

		List<CaseSelectionDto> entries = service.getSimilarCases(criteria);

		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));
		pseudonymizer.pseudonymizeDtoCollection(CaseSelectionDto.class, entries, CaseSelectionDto::isInJurisdiction, null);

		return entries;
	}

	@Override
	public List<CaseDataDto> getRelevantCasesForVaccination(VaccinationDto vaccinationDto) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Case> cq = cb.createQuery(Case.class);
		final Root<Case> caze = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();

		Vaccination vaccination = vaccinationService.getByUuid(vaccinationDto.getUuid());
		Join<Case, Person> person = joins.getPerson();
		Join<Person, Immunization> immunizationJoin = person.join(Person.IMMUNIZATIONS, JoinType.LEFT);
		Join<Immunization, Vaccination> vaccinationsJoin = immunizationJoin.join(Immunization.VACCINATIONS, JoinType.LEFT);

		Predicate predicate = cb.in(vaccinationsJoin).value(vaccination);
		cq.where(predicate);
		cq.select(caze);

		List<Case> cases = em.createQuery(cq).getResultList();
		return toDtos(cases.stream().filter(c -> vaccinationService.isVaccinationRelevant(c, vaccination)));
	}

	@Override
	public boolean hasOtherValidVaccination(CaseDataDto caze, String vaccinationUuid) {
		List<VaccinationDto> relevantVaccinationsForCase = vaccinationFacade.getRelevantVaccinationsForCase(caze);
		//checking if the vaccination selected for delete is in the relevant vaccinations of the case
		return relevantVaccinationsForCase.stream().anyMatch(v -> !v.getUuid().equals(vaccinationUuid));
	}

	@Override
	public Pair<RegionReferenceDto, DistrictReferenceDto> getRegionAndDistrictRefsOf(CaseReferenceDto caze) {
		return caseService.getRegionAndDistrictRefsOf(caze);
	}

	@Override
	public List<CaseMergeIndexDto[]> getCasesForDuplicateMerging(
			CaseCriteria criteria,
			@Min(1) Integer limit,
			boolean showDuplicatesWithDifferentRegion) {
		List<CaseMergeIndexDto[]> cases =
				service.getCasesForDuplicateMerging(criteria, limit, showDuplicatesWithDifferentRegion, configFacade.getNameSimilarityThreshold());

		for (CaseMergeIndexDto[] caze : cases) {
			pseudonymizeCasePairs(caze);
		}

		return cases;
	}

	public void pseudonymizeCasePairs(CaseMergeIndexDto[] cazePair) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));

		Arrays.stream(cazePair).forEach(caze -> {
			Boolean isInJurisdiction = caze.getInJurisdiction();
			pseudonymizer.pseudonymizeDto(
					CaseMergeIndexDto.class,
					caze,
					isInJurisdiction,
					c -> pseudonymizer.pseudonymizeDto(AgeAndBirthDateDto.class, caze.getAgeAndBirthDate(), isInJurisdiction, null));
		});
	}

	@RightsAllowed(UserRight._CASE_EDIT)
	public void updateCompleteness(String caseUuid) {
		service.updateCompleteness(caseUuid);
	}

	@Override
	public CaseDataDto getCaseDataByUuid(String uuid) {
		// todo this plainly duplicates getByUuid from AbstractCoreFacade
		return toPseudonymizedDto(service.getByUuid(uuid, true));
	}

	private CaseDataDto getCaseDataWithoutPseudonyimization(String uuid) {
		return toDto(service.getByUuid(uuid, true));
	}

	@Override
	public CaseReferenceDto getReferenceByUuid(String uuid) {
		return convertToReferenceDto(service.getByUuid(uuid));
	}


	@Override
	@RightsAllowed({
			UserRight._CASE_EDIT })
	public Integer saveBulkCase(
			List<String> caseUuidList,
			@Valid CaseBulkEditData updatedCaseBulkEditData,
			boolean diseaseChange,
			boolean diseaseVariantChange,
			boolean classificationChange,
			boolean investigationStatusChange,
			boolean outcomeChange,
			boolean surveillanceOfficerChange)
			throws ValidationRuntimeException {

		int changedCases = 0;
		for (String caseUuid : caseUuidList) {
			Case caze = service.getByUuid(caseUuid);

			if (service.isEditAllowed(caze)) {
				CaseDataDto existingCaseDto = toDto(caze);
				updateCaseWithBulkData(
						updatedCaseBulkEditData,
						caze,
						diseaseChange,
						diseaseVariantChange,
						classificationChange,
						investigationStatusChange,
						outcomeChange,
						surveillanceOfficerChange);
				doSave(caze, true, existingCaseDto, true);
				changedCases++;
			}
		}
		return changedCases;
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_EDIT })
	public Integer saveBulkEditWithFacilities(
			List<String> caseUuidList,
			@Valid CaseBulkEditData updatedCaseBulkEditData,
			boolean diseaseChange,
			boolean diseaseVariantChange,
			boolean classificationChange,
			boolean investigationStatusChange,
			boolean outcomeChange,
			boolean surveillanceOfficerChange,
			Boolean doTransfer) {

		Region newRegion = regionService.getByUuid(updatedCaseBulkEditData.getRegion().getUuid());
		District newDistrict = districtService.getByUuid(updatedCaseBulkEditData.getDistrict().getUuid());
		Community newCommunity =
				updatedCaseBulkEditData.getCommunity() != null ? communityService.getByUuid(updatedCaseBulkEditData.getCommunity().getUuid()) : null;
		Facility newFacility = facilityService.getByUuid(updatedCaseBulkEditData.getHealthFacility().getUuid());

		int changedCases = 0;
		for (String caseUuid : caseUuidList) {
			Case caze = service.getByUuid(caseUuid);

			if (service.isEditAllowed(caze)) {
				CaseDataDto existingCaseDto = toDto(caze);
				updateCaseWithBulkData(
						updatedCaseBulkEditData,
						caze,
						diseaseChange,
						diseaseVariantChange,
						classificationChange,
						investigationStatusChange,
						outcomeChange,
						surveillanceOfficerChange);

				caze.setRegion(newRegion);
				caze.setDistrict(newDistrict);
				caze.setCommunity(newCommunity);
				caze.setFacilityType(updatedCaseBulkEditData.getFacilityType());
				caze.setHealthFacility(facilityService.getByUuid(updatedCaseBulkEditData.getHealthFacility().getUuid()));
				caze.setHealthFacilityDetails(updatedCaseBulkEditData.getHealthFacilityDetails());
				CaseLogic.handleHospitalization(toDto(caze), existingCaseDto, doTransfer);
				doSave(caze, true, existingCaseDto, true);
				changedCases++;
			}
		}

		return changedCases;
	}

	private void updateCaseWithBulkData(
			CaseBulkEditData updatedCaseBulkEditData,
			Case existingCase,
			boolean diseaseChange,
			boolean diseaseVariantChange,
			boolean classificationChange,
			boolean investigationStatusChange,
			boolean outcomeChange,
			boolean surveillanceOfficerChange) {

		if (diseaseChange) {
			Disease newDisease = updatedCaseBulkEditData.getDisease();
			existingCase.setDisease(newDisease);

			if (!diseaseVariantChange
					&& existingCase.getDiseaseVariant() != null
					&& !customizableEnumFacade
					.existsEnumValue(CustomizableEnumType.DISEASE_VARIANT, existingCase.getDiseaseVariant().getValue(), newDisease)) {
				existingCase.setDiseaseVariant(null);
				existingCase.setDiseaseVariantDetails(null);
			} else if (diseaseVariantChange) {
				DiseaseVariant diseaseVariant = updatedCaseBulkEditData.getDiseaseVariant();
				existingCase.setDiseaseVariant(diseaseVariant);

				existingCase.setDiseaseVariantDetails(diseaseVariant == null ? null : updatedCaseBulkEditData.getDiseaseVariantDetails());
			}

			existingCase.setDiseaseDetails(updatedCaseBulkEditData.getDiseaseDetails());
			existingCase.setPlagueType(updatedCaseBulkEditData.getPlagueType());
			existingCase.setDengueFeverType(updatedCaseBulkEditData.getDengueFeverType());
			existingCase.setRabiesType(updatedCaseBulkEditData.getRabiesType());
		}
		if (classificationChange) {
			existingCase.setCaseClassification(updatedCaseBulkEditData.getCaseClassification());
		}
		if (investigationStatusChange) {
			existingCase.setInvestigationStatus(updatedCaseBulkEditData.getInvestigationStatus());
		}
		if (outcomeChange) {
			existingCase.setOutcome(updatedCaseBulkEditData.getOutcome());
		}
		// Setting the surveillance officer is only allowed if all selected cases are in
		// the same district
		if (surveillanceOfficerChange) {
			UserReferenceDto surveillanceOfficer = updatedCaseBulkEditData.getSurveillanceOfficer();
			existingCase.setSurveillanceOfficer(surveillanceOfficer != null ? userService.getByUuid(surveillanceOfficer.getUuid()) : null);
		}

		if (Objects.nonNull(updatedCaseBulkEditData.getHealthFacilityDetails())) {
			existingCase.setHealthFacilityDetails(updatedCaseBulkEditData.getHealthFacilityDetails());
		}

		if (updatedCaseBulkEditData.getDontShareWithReportingTool() != null) {
			existingCase.setDontShareWithReportingTool(updatedCaseBulkEditData.getDontShareWithReportingTool());
		}
	}



	@RightsAllowed({
			UserRight._CASE_EDIT })
	public CaseDataDto updateFollowUpComment(@Valid @NotNull CaseDataDto dto) throws ValidationRuntimeException {

		Case caze = service.getByUuid(dto.getUuid());
		caze.setFollowUpComment(dto.getFollowUpComment());
		service.ensurePersisted(caze);
		return toPseudonymizedDto(caze);
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_EDIT,
			UserRight._IMMUNIZATION_CREATE,
			UserRight._IMMUNIZATION_EDIT,
			UserRight._IMMUNIZATION_DELETE })
	public void updateVaccinationStatus(CaseReferenceDto caseRef, VaccinationStatus status) {
		Case caze = service.getByReferenceDto(caseRef);
		caze.setVaccinationStatus(status);

		service.ensurePersisted(caze);
	}

	private CaseDataDto caseSave(
			@Valid CaseDataDto dto,
			boolean handleChanges,
			Case existingCaze,
			CaseDataDto existingCaseDto,
			boolean checkChangeDate,
			boolean syncShares)
			throws ValidationRuntimeException {
		SymptomsHelper.updateIsSymptomatic(dto.getSymptoms());

		Pseudonymizer pseudonymizer = createPseudonymizer();

		restorePseudonymizedDto(dto, existingCaseDto, existingCaze, pseudonymizer);

		validateUserRights(dto, existingCaseDto);
		validate(dto);

		externalJournalService.handleExternalJournalPersonUpdateAsync(dto.getPerson());

		// Adjust the checkChangeDate flag based on disease logic
		checkChangeDate = shouldCheckChangeDate(dto, checkChangeDate);

		Case caze = fillOrBuildEntity(dto, existingCaze, checkChangeDate);

		// Set version number on a new case
		if (caze.getCreationDate() == null && StringUtils.isEmpty(dto.getCreationVersion())) {
			caze.setCreationVersion(InfoProvider.get().getVersion());
		}

		doSave(caze, handleChanges, existingCaseDto, syncShares);

		return toPseudonymizedDto(caze, pseudonymizer);
	}

	/**
	 * Determines whether to check the change date based on the disease type.
	 *
	 * @param dto              The CaseDataDto containing the disease information.
	 * @param checkChangeDate  The current value of the checkChangeDate flag.
	 * @return Updated value of the checkChangeDate flag.
	 */
	private boolean shouldCheckChangeDate(CaseDataDto dto, boolean checkChangeDate) {
		if (dto.getDisease() == Disease.MONKEYPOX) {
			return false;
		}
		return checkChangeDate;
	}


	@RightsAllowed(UserRight._CASE_EDIT)
	public void syncSharesAsync(ShareTreeCriteria criteria) {
		executorService.schedule(() -> sormasToSormasCaseFacade.syncShares(criteria), 5, TimeUnit.SECONDS);
	}

	private void doSave(Case caze, boolean handleChanges, CaseDataDto existingCaseDto, boolean syncShares) {
		service.ensurePersisted(caze);
		if (handleChanges) {
			updateCaseVisitAssociations(existingCaseDto, caze);
			onCaseChanged(existingCaseDto, caze, syncShares);
		}
	}

	private void updateCaseVisitAssociations(CaseDataDto existingCase, Case caze) {

		if (existingCase != null
				&& Objects.equals(existingCase.getReportDate(), caze.getReportDate())
				&& Objects.equals(existingCase.getFollowUpUntil(), caze.getFollowUpUntil())
				&& existingCase.getDisease() == caze.getDisease()) {
			return;
		}

		if (existingCase != null) {
			for (Visit visit : caze.getVisits()) {
				visit.setCaze(null);
			}
		}

		Set<Visit> allRelevantVisits = visitService.getAllRelevantVisits(
				caze.getPerson(),
				caze.getDisease(),
				CaseLogic.getStartDate(caze.getSymptoms().getOnsetDate(), caze.getSymptoms().getOnsetDate()),
				CaseLogic.getEndDate(caze.getSymptoms().getOnsetDate(), caze.getReportDate(), caze.getFollowUpUntil()));

		for (Visit visit : allRelevantVisits) {
			caze.getVisits().add(visit); // Necessary for further logic during the case save process
			visit.setCaze(caze);
		}
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public void setSampleAssociations(ContactReferenceDto sourceContact, CaseReferenceDto cazeRef) {

		if (sourceContact != null) {
			final Contact contact = contactService.getByUuid(sourceContact.getUuid());
			final Case caze = service.getByUuid(cazeRef.getUuid());
			List<Sample> samples = contact.getSamples().stream().filter(sample -> !sample.isDeleted()).collect(Collectors.toList());

			if (samples.size() > 0) {
				samples.forEach(sample -> {
					if (contact.getDisease() == caze.getDisease() && sample.getAssociatedCase() == null) {
						sample.setAssociatedCase(caze);
						sampleService.ensurePersisted(sample);
					} else if (!DataHelper.isSame(sample.getAssociatedCase(), cazeRef)) {
						sampleFacade.cloneSampleForCase(sample, caze);
					}
				});

				onCaseChanged(toDto(caze), caze);
			}

			// The samples for case are not persisted yet, so use the samples from contact since they are the same
			caze.setFollowUpUntil(service.computeFollowUpuntilDate(caze, contact.getSamples()));
		}
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public void setSampleAssociations(EventParticipantReferenceDto sourceEventParticipant, CaseReferenceDto cazeRef) {

		if (sourceEventParticipant != null) {
			final EventParticipant eventParticipant = eventParticipantService.getByUuid(sourceEventParticipant.getUuid());
			final Case caze = service.getByUuid(cazeRef.getUuid());
			List<Sample> samples = eventParticipant.getSamples().stream().filter(sample -> !sample.isDeleted()).collect(Collectors.toList());

			if (samples.size() > 0) {
				samples.forEach(sample -> {
					if (eventParticipant.getEvent().getDisease() == caze.getDisease() && sample.getAssociatedCase() == null) {
						sample.setAssociatedCase(caze);
						sampleService.ensurePersisted(sample);
					} else if (!sample.getAssociatedCase().getUuid().equals(cazeRef.getUuid())) {
						sampleFacade.cloneSampleForCase(sample, caze);
					}
				});

				onCaseChanged(toDto(caze), caze);
			}

			// The samples for case are not persisted yet, so use the samples from event participant since they are the same
			caze.setFollowUpUntil(service.computeFollowUpuntilDate(caze, eventParticipant.getSamples()));
		}
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public void setSampleAssociationsUnrelatedDisease(EventParticipantReferenceDto sourceEventParticipant, CaseReferenceDto cazeRef) {
		final EventParticipant eventParticipant = eventParticipantService.getByUuid(sourceEventParticipant.getUuid());
		final Case caze = service.getByUuid(cazeRef.getUuid());
		final Disease disease = caze.getDisease();
		eventParticipant.getSamples().stream().filter(sample -> sampleContainsTestForDisease(sample, disease)).forEach(sample -> {
			if (eventParticipant.getEvent().getDisease() == disease && sample.getAssociatedCase() == null) {
				sample.setAssociatedCase(caze);
			} else {
				sampleFacade.cloneSampleForCase(sample, caze);
			}

			// The samples for case are not persisted yet, so use the samples from event participant since they are the same
			caze.setFollowUpUntil(service.computeFollowUpuntilDate(caze, eventParticipant.getSamples()));
		});

	}

	private boolean sampleContainsTestForDisease(Sample sample, Disease disease) {
		return sample.getPathogenTests().stream().anyMatch(test -> test.getTestedDisease().equals(disease));
	}

	@Override
	public void validate(@Valid CaseDataDto caze) throws ValidationRuntimeException {

		// Check whether any required field that does not have a not null constraint in
		// the database is empty
		if (caze.getReportingUser() == null && !caze.isPseudonymized()) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validReportingUser));
		}

		if (caze.getResponsibleRegion() == null) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validResponsibleRegion));
		}
		if (caze.getResponsibleDistrict() == null) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validResponsibleDistrict));
		}
		if (caze.getResponsibleCommunity() != null
				&& !communityFacade.getByUuid(caze.getResponsibleCommunity().getUuid()).getDistrict().equals(caze.getResponsibleDistrict())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noResponsibleCommunityInResponsibleDistrict));
		}
		if ((caze.getCaseOrigin() == null || caze.getCaseOrigin() == CaseOrigin.IN_COUNTRY) && (caze.getHealthFacility() == null && caze.getFacilityType() == null)  && caze.getDisease() != Disease.FOODBORNE_ILLNESS && caze.getDisease() != Disease.AFP) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validFacility));
		}

		if(caze.getDisease() == Disease.MONKEYPOX && caze.getHealthFacility() == null){
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validFacility));
		}
		if (CaseOrigin.POINT_OF_ENTRY.equals(caze.getCaseOrigin()) && caze.getPointOfEntry() == null && caze.getDisease() == Disease.CORONAVIRUS) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validPointOfEntry));
		}
		if (caze.getDisease() == null) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validDisease));
		}
		// Check whether there are any infrastructure errors
		if (!districtFacade.getByUuid(caze.getResponsibleDistrict().getUuid()).getRegion().equals(caze.getResponsibleRegion())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noResponsibleDistrictInResponsibleRegion));
		}
		if (caze.getResponsibleCommunity() != null
				&& !communityFacade.getByUuid(caze.getResponsibleCommunity().getUuid()).getDistrict().equals(caze.getResponsibleDistrict())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noResponsibleCommunityInResponsibleDistrict));
		}
		if (caze.getRegion() != null
				&& caze.getDistrict() != null
				&& !districtFacade.getByUuid(caze.getDistrict().getUuid()).getRegion().equals(caze.getRegion())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noDistrictInRegion));
		}
		if (caze.getDistrict() != null
				&& caze.getCommunity() != null
				&& !communityFacade.getByUuid(caze.getCommunity().getUuid()).getDistrict().equals(caze.getDistrict())) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noCommunityInDistrict));
		}
		if (caze.getHealthFacility() != null) {
			FacilityDto healthFacility = facilityFacade.getByUuid(caze.getHealthFacility().getUuid());

			/*if (caze.getFacilityType() == null) {
				if (!FacilityDto.NONE_FACILITY_UUID.equals(caze.getHealthFacility().getUuid())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityType));
				}
			}*/
			/*else if (!caze.getFacilityType().isAccommodation()) {
				throw new ValidationRuntimeException(
						I18nProperties.getValidationError(Validations.notAccomodationFacilityType, caze.getFacilityType()));
			}*/

			if (caze.getRegion() == null) {
				if (caze.getResponsibleCommunity() == null
						&& healthFacility.getDistrict() != null
						&& !healthFacility.getDistrict().equals(caze.getResponsibleDistrict())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInResponsibleDistrict));
				}
				if (caze.getResponsibleCommunity() != null
						&& healthFacility.getCommunity() != null
						&& !caze.getResponsibleCommunity().equals(healthFacility.getCommunity())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInResponsibleCommunity));
				}
				if (healthFacility.getRegion() != null && !caze.getResponsibleRegion().equals(healthFacility.getRegion())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInResponsibleRegion));
				}
			} else {
				if (caze.getCommunity() == null && healthFacility.getDistrict() != null && !healthFacility.getDistrict().equals(caze.getDistrict())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInDistrict));
				}
				if (caze.getCommunity() != null
						&& healthFacility.getCommunity() != null
						&& !caze.getCommunity().equals(healthFacility.getCommunity())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInCommunity));
				}
				if (healthFacility.getRegion() != null && !caze.getRegion().equals(healthFacility.getRegion())) {
					throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityInRegion));
				}
			}
		}
	}

	public void validateUserRights(CaseDataDto caze, CaseDataDto existingCaze) {
		if (existingCaze != null) {
			if (!DataHelper.isSame(caze.getHealthFacility(), existingCaze.getHealthFacility())) {

				if (existingCaze.getPointOfEntry() != null
						&& caze.getHealthFacility() != null
						&& !userService.hasRight(UserRight.CASE_REFER_FROM_POE)) {
					throw new AccessDeniedException(
							String.format(
									I18nProperties.getString(Strings.errorNoRightsForChangingField),
									I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.HEALTH_FACILITY)));
				}

				if (existingCaze.getHealthFacility() != null && !userService.hasRight(UserRight.CASE_TRANSFER)) {
					throw new AccessDeniedException(
							String.format(
									I18nProperties.getString(Strings.errorNoRightsForChangingField),
									I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.HEALTH_FACILITY)));
				}
			}

			if (!userService.hasRight(UserRight.CASE_INVESTIGATE)
					&& (!DataHelper.equal(caze.getInvestigationStatus(), existingCaze.getInvestigationStatus())
					|| !DataHelper.equal(caze.getInvestigatedDate(), existingCaze.getInvestigatedDate()))) {
				throw new AccessDeniedException(
						String.format(
								I18nProperties.getString(Strings.errorNoRightsForChangingMultipleFields),
								I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.INVESTIGATION_STATUS)));
			}

			if (!userService.hasRight(UserRight.CASE_CLASSIFY)
					&& (!DataHelper.equal(caze.getCaseClassification(), existingCaze.getCaseClassification())
					|| !DataHelper.equal(caze.getClassificationComment(), existingCaze.getClassificationComment())
					|| !DataHelper.equal(caze.getClassificationDate(), existingCaze.getClassificationDate())
					|| !DataHelper.equal(caze.getClassificationUser(), existingCaze.getClassificationUser())
					|| !DataHelper.equal(caze.getOutcome(), existingCaze.getOutcome())
					|| !DataHelper.equal(caze.getOutcomeDate(), existingCaze.getOutcomeDate()))) {
				throw new AccessDeniedException(
						String.format(
								I18nProperties.getString(Strings.errorNoRightsForChangingMultipleFields),
								I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.CASE_CLASSIFICATION)));
			}

			if (!userService.hasRight(UserRight.CASE_CHANGE_DISEASE)
					&& (!DataHelper.equal(caze.getDisease(), existingCaze.getDisease())
					|| !DataHelper.equal(caze.getDiseaseDetails(), existingCaze.getDiseaseDetails()))) {
				throw new AccessDeniedException(
						String.format(
								I18nProperties.getString(Strings.errorNoRightsForChangingField),
								I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.DISEASE)));
			}

			if (!userService.hasRight(UserRight.CASE_CHANGE_EPID_NUMBER) && (!DataHelper.equal(caze.getEpidNumber(), existingCaze.getEpidNumber()))) {
				throw new AccessDeniedException(
						String.format(
								I18nProperties.getString(Strings.errorNoRightsForChangingField),
								I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.EPID_NUMBER)));
			}

			if (!userService.hasRight(UserRight.CASE_CLINICIAN_VIEW)
					&& (!DataHelper.equal(caze.getClinicianName(), existingCaze.getClinicianName())
					|| !DataHelper.equal(caze.getClinicianEmail(), existingCaze.getClinicianEmail())
					|| !DataHelper.equal(caze.getClinicianPhone(), existingCaze.getClinicianPhone()))) {
				throw new AccessDeniedException(
						String.format(
								I18nProperties.getString(Strings.errorNoRightsForChangingMultipleFields),
								I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.CLINICIAN_NAME)));
			}
		}
	}

	/**
	 * Handles potential changes of related tasks that needs to be done after
	 * a case has been created/saved
	 */
	private void updateTasksOnCaseChanged(Case newCase, CaseDataDto existingCase) {
		// In case that *any* jurisdiction of the case has been changed, we need to see if we need to reassign related tasks.
		// Tasks can be assigned to various user roles and users, therefore it is crucial to make sure
		// that no tasks related to cases are assigned to officers lacking jurisdiction on the case.

		if (existingCase != null) {
			boolean responsibleRegionChanged = !DataHelper.isSame(existingCase.getResponsibleRegion(), newCase.getResponsibleRegion());
			boolean regionChanged = !DataHelper.isSame(existingCase.getRegion(), newCase.getRegion());

			boolean responsibleDistrictChanged = !DataHelper.isSame(existingCase.getResponsibleDistrict(), newCase.getResponsibleDistrict());
			boolean districtChanged = !DataHelper.isSame(existingCase.getDistrict(), newCase.getDistrict());

			// check if infrastructure was changed, added, or removed from the case

			boolean responsibleCommunityChanged = !DataHelper.isSame(existingCase.getResponsibleCommunity(), newCase.getResponsibleCommunity());
			boolean communityChanged = !DataHelper.isSame(existingCase.getCommunity(), newCase.getCommunity());

			boolean facilityChanged = !DataHelper.isSame(existingCase.getHealthFacility(), newCase.getHealthFacility());

			if (responsibleRegionChanged
					|| responsibleDistrictChanged
					|| responsibleCommunityChanged
					|| regionChanged
					|| districtChanged
					|| communityChanged
					|| facilityChanged) {
				reassignTasksOfCase(newCase, false);
			}

		}

		// Create a task to search for other cases for new Plague cases
		if (existingCase == null
				&& newCase.getDisease() == Disease.PLAGUE
				&& featureConfigurationFacade.isTaskGenerationFeatureEnabled(TaskType.ACTIVE_SEARCH_FOR_OTHER_CASES)) {
			createActiveSearchForOtherCasesTask(newCase);
		}
	}

	@PermitAll
	public void onCaseSampleChanged(Case associatedCase) {
		// Update case classification if the feature is enabled
		if (configFacade.isFeatureAutomaticCaseClassification()) {
			if (associatedCase.getCaseClassification() != CaseClassification.NO_CASE) {
				Long pathogenTestsCount = pathogenTestService.countByCase(associatedCase);
				if (pathogenTestsCount == 0) {
					return;
				}
				// calculate classification
				CaseDataDto newCaseDto = toDto(associatedCase);

				CaseClassification classification = caseClassificationFacade.getClassification(newCaseDto);

				// only update when classification by system changes - user may overwrite this
				if (classification != associatedCase.getSystemCaseClassification()) {
					associatedCase.setSystemCaseClassification(classification);

					// really a change? (user may have already set it)
					if (classification != associatedCase.getCaseClassification()) {
						associatedCase.setCaseClassification(classification);
						associatedCase.setClassificationUser(null);
						associatedCase.setClassificationDate(new Date());
					}
				}
			}
		}
	}

	/**
	 * Handles potential changes, processes and backend logic that needs to be done
	 * after a case has been created/saved
	 */
	@PermitAll
	public void onCaseChanged(CaseDataDto existingCase, Case newCase) {
		onCaseChanged(existingCase, newCase, true);
	}

	@PermitAll
	public void onCaseChanged(CaseDataDto existingCase, Case newCase, boolean syncShares) {

		// If its a new case and the case is new and the geo coordinates of the case's
		// health facility are null, set its coordinates to the case's report
		// coordinates, if available. Else if case report coordinates are null set them
		// to the facility's coordinates
		Facility facility = newCase.getHealthFacility();
		if (existingCase == null && facility != null && !FacilityHelper.isOtherOrNoneHealthFacility(facility.getUuid())) {
			if ((facility.getLatitude() == null || facility.getLongitude() == null)
					&& newCase.getReportLat() != null
					&& newCase.getReportLon() != null) {
				facility.setLatitude(newCase.getReportLat());
				facility.setLongitude(newCase.getReportLon());
				facilityService.ensurePersisted(facility);
			} else if (newCase.getReportLat() == null && newCase.getReportLon() == null && newCase.getReportLatLonAccuracy() == null) {
				newCase.setReportLat(facility.getLatitude());
				newCase.setReportLon(facility.getLongitude());
			}
		}

		// Clear facility type if no facility or home was selected
		if (newCase.getHealthFacility() == null || FacilityDto.NONE_FACILITY_UUID.equals(newCase.getHealthFacility().getUuid())) {
			newCase.setFacilityType(null);
		}

		// Generate epid number if missing or incomplete
		FieldVisibilityCheckers fieldVisibilityCheckers = FieldVisibilityCheckers.withCountry(configFacade.getCountryLocale());
		if (fieldVisibilityCheckers.isVisible(CaseDataDto.class, CaseDataDto.EPID_NUMBER)
				&& !CaseLogic.isCompleteEpidNumber(newCase.getEpidNumber())) {
			newCase.setEpidNumber(
					generateEpidNumber(
							newCase.getEpidNumber(),
							newCase.getUuid(),
							newCase.getDisease(),
							newCase.getReportDate(),
							newCase.getResponsibleDistrict().getUuid()));
		}

		// update the plague type based on symptoms
		if (newCase.getDisease() == Disease.PLAGUE) {
			PlagueType plagueType = DiseaseHelper.getPlagueTypeForSymptoms(SymptomsFacadeEjb.toSymptomsDto(newCase.getSymptoms()));
			if (plagueType != newCase.getPlagueType() && plagueType != null) {
				newCase.setPlagueType(plagueType);
			}
		}

		District survOffDistrict = newCase.getSurveillanceOfficer() != null ? newCase.getSurveillanceOfficer().getDistrict() : null;
		Region survOffRegion = newCase.getSurveillanceOfficer() != null ? newCase.getSurveillanceOfficer().getRegion() : null;

		boolean missingSurvOffDistrict =
				survOffDistrict == null || (!survOffDistrict.equals(newCase.getResponsibleDistrict()) && !survOffDistrict.equals(newCase.getDistrict()));
		boolean missingSurvOffRegion =
				survOffRegion == null || (!survOffRegion.equals(newCase.getResponsibleRegion()) && !survOffRegion.equals(newCase.getRegion()));

		if (missingSurvOffDistrict && missingSurvOffRegion) {
			setCaseResponsible(newCase);
		}

		updateInvestigationByStatus(existingCase, newCase);

		updatePersonAndCaseByOutcome(existingCase, newCase);

		updateCaseAge(existingCase, newCase);

		// Change the disease of all contacts if the case disease or disease details have changed
		if (existingCase != null
				&& (newCase.getDisease() != existingCase.getDisease()
				|| !StringUtils.equals(newCase.getDiseaseDetails(), existingCase.getDiseaseDetails()))) {
			for (Contact contact : contactService.findBy(new ContactCriteria().caze(newCase.toReference()), null)) {
				if (contact.getDisease() != newCase.getDisease() || !StringUtils.equals(contact.getDiseaseDetails(), newCase.getDiseaseDetails())) {
					// Only do the change if it hasn't been done in the mobile app before
					contact.setDisease(newCase.getDisease());
					contact.setDiseaseDetails(newCase.getDiseaseDetails());
					contactService.ensurePersisted(contact);
				}
			}
		}

		if (existingCase != null
				&& (newCase.getDisease() != existingCase.getDisease()
				|| !Objects.equals(newCase.getReportDate(), existingCase.getReportDate())
				|| !Objects.equals(newCase.getSymptoms().getOnsetDate(), existingCase.getSymptoms().getOnsetDate()))) {

			// Update follow-up until and status of all contacts
			for (Contact contact : contactService.findBy(new ContactCriteria().caze(newCase.toReference()), null)) {
				contactService.updateFollowUpDetails(contact, false);
				contactService.udpateContactStatus(contact);
			}
			for (Contact contact : contactService.getAllByResultingCase(newCase)) {
				contactService.updateFollowUpDetails(contact, false);
				contactService.udpateContactStatus(contact);
			}
		}

		// Update follow-up
		service.updateFollowUpDetails(newCase, existingCase != null && newCase.getFollowUpStatus() != existingCase.getFollowUpStatus());

		updateTasksOnCaseChanged(newCase, existingCase);

		// Update case classification if the feature is enabled
		CaseClassification classification = null;
		boolean setClassificationInfo = true;
		if (configFacade.isFeatureAutomaticCaseClassification()) {
			if (newCase.getCaseClassification() != CaseClassification.NO_CASE) {
				// calculate classification
				CaseDataDto newCaseDto = toDto(newCase);

				classification = caseClassificationFacade.getClassification(newCaseDto);

				// only update when classification by system changes - user may overwrite this
				if (classification != newCase.getSystemCaseClassification()) {
					newCase.setSystemCaseClassification(classification);

					// really a change? (user may have already set it)
					if (classification != newCase.getCaseClassification()) {
						newCase.setCaseClassification(classification);
						newCase.setClassificationUser(null);
						newCase.setClassificationDate(new Date());
						setClassificationInfo = false;
					}
				}
			}
		}

		if (setClassificationInfo
				&& ((existingCase == null && newCase.getCaseClassification() != CaseClassification.NOT_CLASSIFIED)
				|| (existingCase != null && newCase.getCaseClassification() != existingCase.getCaseClassification()))) {
			newCase.setClassificationUser(userService.getCurrentUser());
			newCase.setClassificationDate(new Date());
		}

		// calculate reference definition for cases
		if (configFacade.isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY)) {
			boolean fulfilled = evaluateFulfilledCondition(toDto(newCase), classification);
			newCase.setCaseReferenceDefinition(fulfilled ? CaseReferenceDefinition.FULFILLED : CaseReferenceDefinition.NOT_FULFILLED);
		}

		// Set Yes/No/Unknown fields associated with embedded lists to Yes if the lists
		// are not empty
		if (!newCase.getHospitalization().getPreviousHospitalizations().isEmpty()
				&& YesNo.YES != newCase.getHospitalization().getHospitalizedPreviously()) {
			newCase.getHospitalization().setHospitalizedPreviously(YesNo.YES);
		}
		if (!newCase.getEpiData().getExposures().isEmpty() && !YesNo.YES.equals(newCase.getEpiData().getExposureDetailsKnown())) {
			newCase.getEpiData().setExposureDetailsKnown(YesNo.YES);
		}

		// Update completeness value
		service.clearCompleteness(newCase);

		// Send an email to all responsible supervisors when the case classification has
		// changed
		if (existingCase != null && existingCase.getCaseClassification() != newCase.getCaseClassification()) {

			try {
				String message = String.format(
						I18nProperties.getString(MessageContents.CONTENT_CASE_CLASSIFICATION_CHANGED),
						DataHelper.getShortUuid(newCase.getUuid()),
						newCase.getCaseClassification().toString());
				notificationService.sendNotifications(
						NotificationType.CASE_CLASSIFICATION_CHANGED,
						JurisdictionHelper.getCaseRegions(newCase),
						null,
						MessageSubject.CASE_CLASSIFICATION_CHANGED,
						message);
			} catch (NotificationDeliveryFailedException e) {
				logger.error("NotificationDeliveryFailedException when trying to notify supervisors about the change of a case classification. ");
			}
		}

		// Send an email to all responsible supervisors when the disease of an
		// AHF case has changed
		if (existingCase != null && existingCase.getDisease() == Disease.UNSPECIFIED_VHF && existingCase.getDisease() != newCase.getDisease()) {

			try {
				String message = String.format(
						I18nProperties.getString(MessageContents.CONTENT_DISEASE_CHANGED),
						DataHelper.getShortUuid(newCase.getUuid()),
						existingCase.getDisease().toString(),
						newCase.getDisease().toString());

				notificationService.sendNotifications(
						NotificationType.CASE_DISEASE_CHANGED,
						JurisdictionHelper.getCaseRegions(newCase),
						null,
						MessageSubject.DISEASE_CHANGED,
						message);
			} catch (NotificationDeliveryFailedException e) {
				logger.error("NotificationDeliveryFailedException when trying to notify supervisors about the change of a case disease.");
			}
		}

		// If the case is a newly created case or if it was not in a CONFIRMED status
		// and now the case is in a CONFIRMED status, notify related surveillance officers
		Set<CaseClassification> confirmedClassifications = CaseClassification.getConfirmedClassifications();
		if ((existingCase == null || !confirmedClassifications.contains(existingCase.getCaseClassification()))
				&& confirmedClassifications.contains(newCase.getCaseClassification())) {
			sendConfirmedCaseNotificationsForEvents(newCase);
		}

		if (existingCase != null && syncShares && sormasToSormasFacade.isFeatureConfigured()) {
			syncSharesAsync(new ShareTreeCriteria(existingCase.getUuid()));
		}

		// This logic should be consistent with CaseDataForm.onQuarantineEndChange
		if (existingCase != null && existingCase.getQuarantineTo() != null && !existingCase.getQuarantineTo().equals(newCase.getQuarantineTo())) {
			newCase.setPreviousQuarantineTo(existingCase.getQuarantineTo());
		}

		if (existingCase == null) {
			vaccinationFacade.updateVaccinationStatuses(newCase);
		}

		// On German systems, correct and clean up reinfection data
		if (configFacade.isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY)) {
			newCase.setReinfectionDetails(cleanupReinfectionDetails(newCase.getReinfectionDetails()));
			newCase.setReinfectionStatus(CaseLogic.calculateReinfectionStatus(newCase.getReinfectionDetails()));
		}
	}

	private boolean evaluateFulfilledCondition(CaseDataDto newCase, CaseClassification caseClassification) {

		if (newCase.getCaseClassification() != CaseClassification.NO_CASE) {
			List<CaseClassification> fulfilledCaseClassificationOptions =
					Arrays.asList(CaseClassification.CONFIRMED, CaseClassification.CONFIRMED_NO_SYMPTOMS, CaseClassification.CONFIRMED_UNKNOWN_SYMPTOMS);

			if (caseClassification == null) {
				caseClassification = caseClassificationFacade.getClassification(newCase);
			}

			List<PathogenTest> casePathogenTests = null;
			if (fulfilledCaseClassificationOptions.contains(caseClassification)) {
				casePathogenTests = pathogenTestService.getAllByCase(newCase.getUuid());
				casePathogenTests = casePathogenTests.stream()
						.filter(
								pathogenTest -> (Arrays.asList(PathogenTestType.PCR_RT_PCR, PathogenTestType.ISOLATION, PathogenTestType.SEQUENCING)
										.contains(pathogenTest.getTestType())
										&& PathogenTestResultType.POSITIVE.equals(pathogenTest.getTestResult())))
						.collect(Collectors.toList());
			}
			return casePathogenTests != null && !casePathogenTests.isEmpty();
		} else {
			return false;
		}
	}

	private void sendConfirmedCaseNotificationsForEvents(Case caze) {

		try {
			notificationService.sendNotifications(
					NotificationType.EVENT_PARTICIPANT_CASE_CLASSIFICATION_CONFIRMED,
					MessageSubject.EVENT_PARTICIPANT_CASE_CLASSIFICATION_CONFIRMED,
					new Object[] {
							caze.getDisease().getName() },
					() -> {
						final Date fromDate = Date.from(Instant.now().minus(Duration.ofDays(30)));
						Map<String, User> eventResponsibleUsers =
								eventService.getAllEventUuidWithResponsibleUserByCaseAfterDateForNotification(caze, fromDate);

						return eventResponsibleUsers.keySet()
								.stream()
								.collect(
										Collectors.toMap(
												eventResponsibleUsers::get,
												eventUuid -> String.format(
														I18nProperties.getString(MessageContents.CONTENT_EVENT_PARTICIPANT_CASE_CLASSIFICATION_CONFIRMED),
														DataHelper.getShortUuid(eventUuid),
														caze.getDisease().getName(),
														DataHelper.getShortUuid(caze.getUuid()))));
					});
		} catch (NotificationDeliveryFailedException e) {
			logger.error("NotificationDeliveryFailedException when trying to notify event responsible user about a newly confirmed case.");
		}
	}

	@RightsAllowed(UserRight._CASE_EDIT)
	public void setCaseResponsible(Case caze) {
		setCaseResponsible(caze, false, null, null, null);
	}

	@RightsAllowed(UserRight._CASE_EDIT)
	public void setCaseResponsible(
			Case caze,
			boolean neededFeatureAlreadyChecked,
			List<User> possibleUsersForReplacementSurvOfficerBasedOnResponsibleDistrict,
			List<User> possibleUsersForReplacementSurvOfficerBasedOnDistrict,
			Set<User> possibleUsersForReplacementFacilityUsers) {
		if (neededFeatureAlreadyChecked
				|| featureConfigurationFacade
				.isPropertyValueTrue(FeatureType.CASE_SURVEILANCE, FeatureTypeProperty.AUTOMATIC_RESPONSIBILITY_ASSIGNMENT)) {
			District reportingUserDistrict = caze.getReportingUser().getDistrict();

			if (userRoleService.hasUserRight(caze.getReportingUser().getUserRoles(), UserRight.CASE_RESPONSIBLE)
					&& (reportingUserDistrict == null
					|| reportingUserDistrict.equals(caze.getResponsibleDistrict())
					|| reportingUserDistrict.equals(caze.getDistrict()))) {
				caze.setSurveillanceOfficer(caze.getReportingUser());
			} else {
				List<User> hospitalUsers;
				if (possibleUsersForReplacementFacilityUsers == null) {
					hospitalUsers = caze.getHealthFacility() != null && FacilityType.HOSPITAL.equals(caze.getHealthFacility().getType())
							? userService.getFacilityUsersOfHospital(caze.getHealthFacility())
							: new ArrayList<>();
				} else {
					hospitalUsers = possibleUsersForReplacementFacilityUsers.stream()
							.filter(
									user -> user.getHealthFacility().equals(caze.getHealthFacility())
											&& user.getJurisdictionLevel().equals(JurisdictionLevel.HEALTH_FACILITY))
							.collect(Collectors.toList());
				}
				Random rand = new Random();

				if (!hospitalUsers.isEmpty()) {
					caze.setSurveillanceOfficer(hospitalUsers.get(rand.nextInt(hospitalUsers.size())).getAssociatedOfficer());
				}

				else {
					User survOff = null;
					if (caze.getResponsibleDistrict() != null) {
						if (possibleUsersForReplacementSurvOfficerBasedOnResponsibleDistrict == null) {
							survOff = getRandomDistrictCaseResponsible(caze.getResponsibleDistrict());
						} else if (!possibleUsersForReplacementSurvOfficerBasedOnResponsibleDistrict.isEmpty()) {
							List<User> collect = possibleUsersForReplacementSurvOfficerBasedOnResponsibleDistrict.stream()
									.filter(user -> caze.getResponsibleDistrict().equals(user.getDistrict()))
									.collect(Collectors.toList());
							survOff = collect.size() > 0 ? collect.get(new Random().nextInt(collect.size())) : null;
						}
					}

					if (survOff == null && caze.getDistrict() != null) {
						if (possibleUsersForReplacementSurvOfficerBasedOnDistrict == null) {
							survOff = getRandomDistrictCaseResponsible(caze.getDistrict());
						} else if (!possibleUsersForReplacementSurvOfficerBasedOnDistrict.isEmpty()) {
							List<User> collect = possibleUsersForReplacementSurvOfficerBasedOnDistrict.stream()
									.filter(user -> caze.getDistrict().equals(user.getDistrict()))
									.collect(Collectors.toList());
							survOff = collect.size() > 0 ? collect.get(new Random().nextInt(collect.size())) : null;
						}
					}

					caze.setSurveillanceOfficer(survOff);
				}
			}
		}
	}

	/**
	 * Reassigns tasks related to `caze`. With `forceReassignment` beeing false, the function will only reassign
	 * the tasks if the assignees lack jurisdiction on the case. When forced, all tasks will be reassigned.
	 *
	 * @param caze
	 *            the case which related tasks are reassigned.
	 * @param forceReassignment
	 *            force reassignment of case tasks.
	 */
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public void reassignTasksOfCase(Case caze, boolean forceReassignment) {
		// for each task that is related to the case, the task assignee must match the jurisdiction of the case
		// otherwise we will reassign the task
		for (Task task : caze.getTasks()) {
			if (task.getTaskStatus() != TaskStatus.PENDING) {
				continue;
			}

			User taskAssignee = task.getAssigneeUser();

			if (forceReassignment || taskAssignee == null || !service.inJurisdiction(caze, taskAssignee)) {
				// if there is any mismatch between the jurisdiction of the case and the assigned user,
				// we need to reassign the tasks
				assignOfficerOrSupervisorToTask(caze, task);
				taskService.ensurePersisted(task);
			}

		}

	}

	@Override
	@RightsAllowed(UserRight._SYSTEM)
	public int updateCompleteness() {
		List<String> getCompletenessCheckCaseList = getCompletenessCheckNeededCaseList();

		IterableHelper.executeBatched(getCompletenessCheckCaseList, 10, caseCompletionBatch -> service.updateCompleteness(caseCompletionBatch));

		return getCompletenessCheckCaseList.size();
	}

	@Override
	public PreviousCaseDto getMostRecentPreviousCase(PersonReferenceDto person, Disease disease, Date startDate) {

		return service.getMostRecentPreviousCase(person.getUuid(), disease, startDate);
	}

	private List<String> getCompletenessCheckNeededCaseList() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caze = cq.from(Case.class);

		cq.where(cb.isNull(caze.get(Case.COMPLETENESS)));

		cq.orderBy(cb.desc(caze.get(Case.CHANGE_DATE)));
		cq.select(caze.get(Case.UUID));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getGenerateEpidNumber(CaseDataDto caze) {
		return generateEpidNumber(
				caze.getEpidNumber(),
				caze.getUuid(),
				caze.getDisease(),
				caze.getReportDate(),
				caze.getResponsibleDistrict().getUuid());
	}

	private String generateEpidNumber(String newEpidNumber, String caseUuid, Disease disease, Date reportDate, String districtUuid) {

		if (!CaseLogic.isEpidNumberPrefix(newEpidNumber)) {
			// Generate a completely new epid number if the prefix is not complete or doesn't match the pattern
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(reportDate);
			String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
			newEpidNumber = districtFacade.getFullEpidCodeForDistrict(districtUuid) + "-" + year + "-";
		}

		// Generate a suffix number
		String highestEpidNumber = service.getHighestEpidNumber(newEpidNumber, caseUuid, disease);
		if (highestEpidNumber == null || highestEpidNumber.endsWith("-")) {
			// If there is not yet a case with a suffix for this epid number in the database, use 001
			newEpidNumber = newEpidNumber + "001";
		} else {
			// Otherwise, extract the suffix from the highest existing epid number and increase it by 1
			String suffixString = highestEpidNumber.substring(highestEpidNumber.lastIndexOf('-'));
			// Remove all non-digits from the suffix to ignore earlier input errors
			suffixString = suffixString.replaceAll("[^\\d]", "");
			if (suffixString.isEmpty()) {
				// If the suffix is empty now, that means there is not yet an epid number with a
				// suffix containing numbers
				newEpidNumber = newEpidNumber + "001";
			} else {
				int suffix = Integer.parseInt(suffixString) + 1;
				newEpidNumber += String.format("%03d", suffix);
			}
		}

		return newEpidNumber;
	}

	private void updatePersonAndCaseByOutcome(CaseDataDto existingCase, Case newCase) {

		if (existingCase != null && newCase.getOutcome() != existingCase.getOutcome()) {

			if (newCase.getOutcome() == null || newCase.getOutcome() == CaseOutcome.NO_OUTCOME) {
				newCase.setOutcomeDate(null);
			}

			if (newCase.getOutcome() == CaseOutcome.DECEASED) {
				if (newCase.getPerson().getPresentCondition() != PresentCondition.DEAD
						&& newCase.getPerson().getPresentCondition() != PresentCondition.BURIED) {
					PersonDto existingPerson = PersonFacadeEjb.toPersonDto(newCase.getPerson());
					newCase.getPerson().setPresentCondition(PresentCondition.DEAD);
					newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
					newCase.getPerson().setCauseOfDeath(CauseOfDeath.EPIDEMIC_DISEASE);
					newCase.getPerson().setCauseOfDeathDisease(newCase.getDisease());
					// attention: this may lead to infinite recursion when not properly implemented
					personFacade.onPersonChanged(existingPerson, newCase.getPerson());
				}
			} else if (newCase.getOutcome() == CaseOutcome.UNKNOWN || newCase.getOutcome() == CaseOutcome.RECOVERED) {

				PersonDto existingPerson = PersonFacadeEjb.toPersonDto(newCase.getPerson());

				if (existingPerson.getCauseOfDeath() == CauseOfDeath.EPIDEMIC_DISEASE
						&& existingPerson.getCauseOfDeathDisease() == newCase.getDisease()) {
					// Make sure no other case associated with the person has Outcome=DECEASED
					CaseCriteria caseCriteria = new CaseCriteria();
					caseCriteria.setPerson(existingPerson.toReference());
					caseCriteria.setOutcome(CaseOutcome.DECEASED);
					if (count(caseCriteria, true) == 0) {
						newCase.getPerson().setPresentCondition(PresentCondition.ALIVE);
						newCase.getPerson().setBurialDate(null);
						newCase.getPerson().setDeathDate(null);
						newCase.getPerson().setDeathPlaceDescription(null);
						newCase.getPerson().setDeathPlaceType(null);
						newCase.getPerson().setCauseOfDeath(null);
						newCase.getPerson().setCauseOfDeathDetails(null);
						newCase.getPerson().setCauseOfDeathDisease(null);
						personFacade.onPersonChanged(existingPerson, newCase.getPerson());
					}
				}
			}
		} else if (existingCase != null
				&& newCase.getOutcome() == CaseOutcome.DECEASED
				&& (newCase.getPerson().getPresentCondition() == PresentCondition.DEAD
				|| newCase.getPerson().getPresentCondition() == PresentCondition.BURIED)
				&& !Objects.equals(existingCase.getOutcomeDate(), newCase.getOutcomeDate())
				&& newCase.getOutcomeDate() != null
				&& newCase.getPerson().getCauseOfDeath() == CauseOfDeath.EPIDEMIC_DISEASE
				&& newCase.getPerson().getCauseOfDeathDisease() == existingCase.getDisease()) {
			// outcomeDate of a deceased case was changed, but person is already considered dead
			// update the deathdate of the person
			PersonDto existingPerson = PersonFacadeEjb.toPersonDto(newCase.getPerson());
			newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
			personFacade.onPersonChanged(existingPerson, newCase.getPerson());
		} else if (existingCase == null) {
			// new Case; Still compare persons Condition and caseOutcome
			if (newCase.getOutcome() == CaseOutcome.DECEASED
					&& newCase.getPerson().getPresentCondition() != PresentCondition.BURIED
					&& newCase.getPerson().getPresentCondition() != PresentCondition.DEAD) {
				// person is alive but case has outcome deceased
				PersonDto existingPerson = PersonFacadeEjb.toPersonDto(newCase.getPerson());
				newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
				newCase.getPerson().setPresentCondition(PresentCondition.DEAD);
				newCase.getPerson().setCauseOfDeath(CauseOfDeath.EPIDEMIC_DISEASE);
				newCase.getPerson().setCauseOfDeathDisease(newCase.getDisease());
				personFacade.onPersonChanged(existingPerson, newCase.getPerson());
			} else if (newCase.getOutcome() == CaseOutcome.UNKNOWN || newCase.getOutcome() == CaseOutcome.RECOVERED) {

				PersonDto existingPerson = PersonFacadeEjb.toPersonDto(newCase.getPerson());

				if (existingPerson.getCauseOfDeath() == CauseOfDeath.EPIDEMIC_DISEASE
						&& existingPerson.getCauseOfDeathDisease() == newCase.getDisease()) {
					// Make sure no other case associated with the person has Outcome=DECEASED
					CaseCriteria caseCriteria = new CaseCriteria();
					caseCriteria.setPerson(existingPerson.toReference());
					caseCriteria.setOutcome(CaseOutcome.DECEASED);
					if (count(caseCriteria, true) == 0) {
						newCase.getPerson()
								.setPresentCondition(newCase.getOutcome() == CaseOutcome.UNKNOWN ? PresentCondition.UNKNOWN : PresentCondition.ALIVE);
						newCase.getPerson().setBurialDate(null);
						newCase.getPerson().setDeathDate(null);
						newCase.getPerson().setDeathPlaceDescription(null);
						newCase.getPerson().setDeathPlaceType(null);
						newCase.getPerson().setCauseOfDeath(null);
						newCase.getPerson().setCauseOfDeathDetails(null);
						newCase.getPerson().setCauseOfDeathDisease(null);
						personFacade.onPersonChanged(existingPerson, newCase.getPerson());
					}
				}
			}
		}
	}

	private void updateCaseAge(CaseDataDto existingCase, Case newCase) {

		if (newCase.getPerson().getApproximateAge() != null) {
			Date newCaseStartDate = CaseLogic.getStartDate(newCase.getSymptoms().getOnsetDate(), newCase.getReportDate());
			if (existingCase == null || !CaseLogic.getStartDate(existingCase).equals(newCaseStartDate)) {
				if (newCase.getPerson().getApproximateAgeType() == ApproximateAgeType.MONTHS) {
					newCase.setCaseAge(0);
				} else {
					Date personChangeDate = newCase.getPerson().getChangeDate();
					Date referenceDate = newCaseStartDate;
					newCase.setCaseAge(newCase.getPerson().getApproximateAge() - DateHelper.getYearsBetween(referenceDate, personChangeDate));
					if (newCase.getCaseAge() < 0) {
						newCase.setCaseAge(0);
					}
				}

			}
		}
	}

	public void pseudonymizeDto(Case source, CaseDataDto dto, Pseudonymizer pseudonymizer) {
		if (dto != null) {
			boolean inJurisdiction = service.inJurisdictionOrOwned(source);

			pseudonymizer.pseudonymizeDto(CaseDataDto.class, dto, inJurisdiction, c -> {
				User currentUser = userService.getCurrentUser();
				pseudonymizer.pseudonymizeUser(source.getReportingUser(), currentUser, dto::setReportingUser);
				pseudonymizer.pseudonymizeUser(source.getClassificationUser(), currentUser, dto::setClassificationUser);

				pseudonymizer.pseudonymizeDto(
						EpiDataDto.class,
						dto.getEpiData(),
						inJurisdiction,
						e -> pseudonymizer.pseudonymizeDtoCollection(
								ExposureDto.class,
								e.getExposures(),
								exp -> inJurisdiction,
								(exp, expInJurisdiction) -> pseudonymizer.pseudonymizeDto(LocationDto.class, exp.getLocation(), expInJurisdiction, null)));

				pseudonymizer.pseudonymizeDto(HealthConditionsDto.class, c.getHealthConditions(), inJurisdiction, null);

				pseudonymizer.pseudonymizeDtoCollection(
						PreviousHospitalizationDto.class,
						c.getHospitalization().getPreviousHospitalizations(),
						h -> inJurisdiction,
						null);

				pseudonymizer.pseudonymizeDto(SymptomsDto.class, dto.getSymptoms(), inJurisdiction, null);
				pseudonymizer.pseudonymizeDto(MaternalHistoryDto.class, dto.getMaternalHistory(), inJurisdiction, null);
			});
		}
	}

	public boolean isEditAllowed(String uuid) {
		return service.isEditAllowed(service.getByUuid(uuid));
	}

	@Override
	@RightsAllowed(UserRight._CASE_DELETE)
	public void delete(String caseUuid, DeletionDetails deletionDetails)
			throws ExternalSurveillanceToolRuntimeException, SormasToSormasRuntimeException {
		Case caze = service.getByUuid(caseUuid);
		deleteCase(caze, deletionDetails);
	}

	@Override
	@RightsAllowed(UserRight._CASE_DELETE)
	public List<String> delete(List<String> uuids, DeletionDetails deletionDetails) {
		List<String> deletedCaseUuids = new ArrayList<>();
		List<Case> casesToBeDeleted = service.getByUuids(uuids);
		if (casesToBeDeleted != null) {
			casesToBeDeleted.forEach(caseToBeDeleted -> {
				if (!caseToBeDeleted.isDeleted()) {
					try {
						deleteCase(caseToBeDeleted, deletionDetails);
						deletedCaseUuids.add(caseToBeDeleted.getUuid());
					} catch (ExternalSurveillanceToolRuntimeException | SormasToSormasRuntimeException | AccessDeniedException e) {
						logger.error("The case with uuid {} could not be deleted", caseToBeDeleted.getUuid(), e);
					}
				}
			});
		}
		return deletedCaseUuids;
	}

	@Override
	@RightsAllowed(UserRight._CASE_DELETE)
	public void restore(String uuid) {
		super.restore(uuid);
	}

	@Override
	@RightsAllowed(UserRight._CASE_DELETE)
	public List<String> restore(List<String> uuids) {
		List<String> restoredCaseUuids = new ArrayList<>();
		List<Case> casesToBeRestored = caseService.getByUuids(uuids);

		if (casesToBeRestored != null) {
			casesToBeRestored.forEach(caseToBeRestored -> {
				try {
					restore(caseToBeRestored.getUuid());
					restoredCaseUuids.add(caseToBeRestored.getUuid());
				} catch (Exception e) {
					logger.error("The case with uuid {} could not be restored", caseToBeRestored.getUuid(), e);
				}
			});
		}
		return restoredCaseUuids;
	}

	@Override
	@RightsAllowed(UserRight._CASE_DELETE)
	public void deleteWithContacts(String caseUuid, DeletionDetails deletionDetails) {

		Case caze = service.getByUuid(caseUuid);
		deleteCase(caze, deletionDetails);

		Optional.of(caze.getContacts()).ifPresent(cl -> cl.forEach(c -> contactService.delete(c, deletionDetails)));
	}

	private void deleteCase(Case caze, DeletionDetails deletionDetails)
			throws ExternalSurveillanceToolRuntimeException, SormasToSormasRuntimeException, AccessDeniedException {

		if (!caseService.inJurisdictionOrOwned(caze)) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.messageCaseOutsideJurisdictionDeletionDenied));
		}
		externalJournalService.handleExternalJournalPersonUpdateAsync(caze.getPerson().toReference());

		try {
			sormasToSormasFacade.revokePendingShareRequests(caze.getSormasToSormasShares(), true);
		} catch (SormasToSormasException e) {
			throw new SormasToSormasRuntimeException(e);
		}

		service.delete(caze, deletionDetails);
	}

	@Override
	@RightsAllowed(UserRight._CASE_MERGE)
	public void deleteAsDuplicate(String caseUuid, String duplicateOfCaseUuid) {

		Case caze = service.getByUuid(caseUuid);
		Case duplicateOfCase = service.getByUuid(duplicateOfCaseUuid);
		caze.setDuplicateOf(duplicateOfCase);
		service.ensurePersisted(caze);

		delete(caseUuid, new DeletionDetails(DeletionReason.DUPLICATE_ENTRIES, null));
	}

	@RightsAllowed({
			UserRight._CASE_DELETE,
			UserRight._CASE_MERGE,
			UserRight._SYSTEM })
	public void deleteCaseInExternalSurveillanceTool(Case caze) throws ExternalSurveillanceToolException {

		if (externalSurveillanceToolGatewayFacade.isFeatureEnabled() && caze.getExternalID() != null && !caze.getExternalID().isEmpty()) {
			// getByExternalId(caze) throws NPE (see #10820) so we use the service directly.
			// Can potentially be changed back once 10844 is done.
			List<Case> casesWithSameExternalId = service.getByExternalId(caze.getExternalID());
			if (casesWithSameExternalId != null && casesWithSameExternalId.size() == 1 && externalShareInfoService.isCaseShared(caze.getId())) {
				externalSurveillanceToolGatewayFacade.deleteCasesInternal(Collections.singletonList(toDto(caze)));
			}
		}
	}

	@Override
	@RightsAllowed(UserRight._CASE_ARCHIVE)
	public void archive(String entityUuid, Date endOfProcessingDate, boolean includeContacts) {
		super.archive(entityUuid, endOfProcessingDate);
		if (includeContacts) {
			List<String> caseContacts = contactService.getAllUuidsByCaseUuids(Collections.singletonList(entityUuid));
			contactService.archive(caseContacts);
		}
	}

	@Override
	@RightsAllowed(UserRight._CASE_ARCHIVE)
	public void archive(List<String> entityUuids, boolean includeContacts) {
		super.archive(entityUuids);
		if (includeContacts) {
			List<String> caseContacts = contactService.getAllUuidsByCaseUuids(entityUuids);
			contactService.archive(caseContacts);
		}
	}

	@Override
	@RightsAllowed(UserRight._CASE_ARCHIVE)
	public void dearchive(List<String> entityUuids, String dearchiveReason, boolean includeContacts) {
		super.dearchive(entityUuids, dearchiveReason);
		if (includeContacts) {
			List<String> caseContacts = contactService.getAllUuidsByCaseUuids(entityUuids);
			contactService.dearchive(caseContacts, dearchiveReason);
		}
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public void setResultingCase(EventParticipantReferenceDto eventParticipantReferenceDto, CaseReferenceDto caseReferenceDto) {
		final EventParticipant eventParticipant = eventParticipantService.getByUuid(eventParticipantReferenceDto.getUuid());
		if (eventParticipant != null) {
			eventParticipant.setResultingCase(caseService.getByUuid(caseReferenceDto.getUuid()));
			eventParticipantService.ensurePersisted(eventParticipant);
		}
	}

	@Override
	public EditPermissionType isEditContactAllowed(String uuid) {
		Case ado = service.getByUuid(uuid);
		return service.isAddContactAllowed(ado);
	}

	@Override
	public List<String> getArchivedUuidsSince(Date since) {

		if (userService.getCurrentUser() == null) {
			return Collections.emptyList();
		}

		return service.getArchivedUuidsSince(since);
	}

	@Override
	public List<String> getDeletedUuidsSince(Date since) {

		if (userService.getCurrentUser() == null) {
			return Collections.emptyList();
		}
		return service.getDeletedUuidsSince(since);
	}

	public static CaseReferenceDto toReferenceDto(Case entity) {

		if (entity == null) {
			return null;
		}

		return entity.toReference();
	}

	@Override
	public void pseudonymizeDto(Case source, CaseDataDto dto, Pseudonymizer pseudonymizer, boolean inJurisdiction) {

		if (dto != null) {
			pseudonymizer.pseudonymizeDto(CaseDataDto.class, dto, inJurisdiction, c -> {
				User currentUser = userService.getCurrentUser();
				pseudonymizer.pseudonymizeUser(source.getReportingUser(), currentUser, dto::setReportingUser);
				pseudonymizer.pseudonymizeUser(source.getClassificationUser(), currentUser, dto::setClassificationUser);

				pseudonymizer.pseudonymizeDto(
						EpiDataDto.class,
						dto.getEpiData(),
						inJurisdiction,
						e -> pseudonymizer.pseudonymizeDtoCollection(
								ExposureDto.class,
								e.getExposures(),
								exp -> inJurisdiction,
								(exp, expInJurisdiction) -> pseudonymizer.pseudonymizeDto(LocationDto.class, exp.getLocation(), expInJurisdiction, null)));

				pseudonymizer.pseudonymizeDto(HealthConditionsDto.class, c.getHealthConditions(), inJurisdiction, null);

				pseudonymizer.pseudonymizeDtoCollection(
						PreviousHospitalizationDto.class,
						c.getHospitalization().getPreviousHospitalizations(),
						h -> inJurisdiction,
						null);

				pseudonymizer.pseudonymizeDto(SymptomsDto.class, dto.getSymptoms(), inJurisdiction, null);
				pseudonymizer.pseudonymizeDto(MaternalHistoryDto.class, dto.getMaternalHistory(), inJurisdiction, null);
			});
		}
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_EDIT,
			UserRight._CASE_EDIT })
	public void restorePseudonymizedDto(CaseDataDto dto, CaseDataDto existingCaseDto, Case caze, Pseudonymizer pseudonymizer) {
		if (existingCaseDto != null) {
			boolean inJurisdiction = service.inJurisdictionOrOwned(caze);

			User currentUser = userService.getCurrentUser();

			pseudonymizer.restoreUser(caze.getReportingUser(), currentUser, dto, dto::setReportingUser);
			pseudonymizer.restoreUser(caze.getClassificationUser(), currentUser, dto, dto::setClassificationUser);

			pseudonymizer.restorePseudonymizedValues(CaseDataDto.class, dto, existingCaseDto, inJurisdiction);

			EpiDataDto epiData = dto.getEpiData();
			EpiDataDto existingEpiData = existingCaseDto.getEpiData();

			pseudonymizer.restorePseudonymizedValues(EpiDataDto.class, epiData, existingEpiData, inJurisdiction);

			epiData.getExposures().forEach(exposure -> {
				ExposureDto existingExposure =
						existingEpiData.getExposures().stream().filter(exp -> DataHelper.isSame(exposure, exp)).findFirst().orElse(null);

				if (existingExposure != null) {
					pseudonymizer.restorePseudonymizedValues(ExposureDto.class, exposure, existingExposure, inJurisdiction);
					pseudonymizer
							.restorePseudonymizedValues(LocationDto.class, exposure.getLocation(), existingExposure.getLocation(), inJurisdiction);
				}
			});

			pseudonymizer.restorePseudonymizedValues(
					HealthConditionsDto.class,
					dto.getHealthConditions(),
					existingCaseDto.getHealthConditions(),
					inJurisdiction);

			dto.getHospitalization()
					.getPreviousHospitalizations()
					.forEach(
							previousHospitalization -> existingCaseDto.getHospitalization()
									.getPreviousHospitalizations()
									.stream()
									.filter(eh -> DataHelper.isSame(previousHospitalization, eh))
									.findFirst()
									.ifPresent(
											existingPreviousHospitalization -> pseudonymizer.restorePseudonymizedValues(
													PreviousHospitalizationDto.class,
													previousHospitalization,
													existingPreviousHospitalization,
													inJurisdiction)));

			pseudonymizer.restorePseudonymizedValues(SymptomsDto.class, dto.getSymptoms(), existingCaseDto.getSymptoms(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(MaternalHistoryDto.class, dto.getMaternalHistory(), existingCaseDto.getMaternalHistory(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(SixtyDayDto.class, dto.getSixtyDay(), existingCaseDto.getSixtyDay(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(RiskFactorDto.class, dto.getRiskFactor(), existingCaseDto.getRiskFactor(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(AfpImmunizationDto.class, dto.getAfpImmunization(), existingCaseDto.getAfpImmunization(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(FoodHistoryDto.class, dto.getFoodHistory(), existingCaseDto.getFoodHistory(), inJurisdiction);
			pseudonymizer.restorePseudonymizedValues(InvestigationNotesDto.class, dto.getInvestigationNotes(), existingCaseDto.getInvestigationNotes(), inJurisdiction);
		}
	}

	public CaseReferenceDto convertToReferenceDto(Case source) {

		CaseReferenceDto dto = toReferenceDto(source);

		if (dto != null) {
			boolean inJurisdiction = service.inJurisdictionOrOwned(source);
			Pseudonymizer.getDefault(userService::hasRight).pseudonymizeDto(CaseReferenceDto.class, dto, inJurisdiction, null);
		}

		return dto;
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_VIEW,
			UserRight._EXTERNAL_VISITS })
	public CaseDataDto toDto(Case source) {
		return toCaseDto(source);
	}

	public static CaseDataDto toCaseDto(Case source) {

		if (source == null) {
			return null;
		}

		CaseDataDto target = new CaseDataDto();
		DtoHelper.fillDto(target, source);

		target.setDisease(source.getDisease());
		target.setDiseaseVariant(source.getDiseaseVariant());
		target.setDiseaseDetails(source.getDiseaseDetails());
		target.setDiseaseVariantDetails(source.getDiseaseVariantDetails());
		target.setPlagueType(source.getPlagueType());
		target.setDengueFeverType(source.getDengueFeverType());
		target.setIdsrDiagnosis(source.getIdsrDiagnosis());
		target.setRabiesType(source.getRabiesType());
		target.setCaseClassification(source.getCaseClassification() == CaseClassification.NOT_CLASSIFIED ? CaseClassification.SUSPECT : source.getCaseClassification());
		target.setCaseClassification(source.getCaseClassification());
		target.setCaseIdentificationSource(source.getCaseIdentificationSource());
		target.setScreeningType(source.getScreeningType());
		target.setClassificationUser(UserFacadeEjb.toReferenceDto(source.getClassificationUser()));
		target.setClassificationDate(source.getClassificationDate());
		target.setClassificationComment(source.getClassificationComment());
		target.setClinicalConfirmation(source.getClinicalConfirmation());
		target.setEpidemiologicalConfirmation(source.getEpidemiologicalConfirmation());
		target.setLaboratoryDiagnosticConfirmation(source.getLaboratoryDiagnosticConfirmation());
		target.setInvestigationStatus(source.getInvestigationStatus());
		target.setPerson(PersonFacadeEjb.toReferenceDto(source.getPerson()));
		target.setHospitalization(HospitalizationFacadeEjb.toDto(source.getHospitalization()));
		target.setSixtyDay(SixtyDayFacadeEjb.toDto(source.getSixtyDay()));
		target.setInvestigationNotes(InvestigationNotesFacadeEjb.toDto(source.getInvestigationNotes()));
		target.setAfpImmunization(AfpImmunizationFacadeEjb.toDto(source.getAfpImmunization()));
		target.setFoodHistory(FoodHistoryFacadeEjb.toDto(source.getFoodHistory()));
		target.setRiskFactor(RiskFactorFacadeEjb.toDto(source.getRiskFactor()));
		target.setEpiData(EpiDataFacadeEjb.toDto(source.getEpiData()));
		if (source.getTherapy() != null) {
			target.setTherapy(TherapyFacadeEjb.toDto(source.getTherapy()));
		}
		if (source.getClinicalCourse() != null) {
			target.setClinicalCourse(ClinicalCourseFacadeEjb.toDto(source.getClinicalCourse()));
		}
		target.setHealthConditions(HealthConditionsMapper.toDto(source.getHealthConditions()));
		if (source.getMaternalHistory() != null) {
			target.setMaternalHistory(MaternalHistoryFacadeEjb.toDto(source.getMaternalHistory()));
		}
		if (source.getPortHealthInfo() != null) {
			target.setPortHealthInfo(PortHealthInfoFacadeEjb.toDto(source.getPortHealthInfo()));
		}

		target.setResponsibleRegion(RegionFacadeEjb.toReferenceDto(source.getResponsibleRegion()));
		target.setResponsibleDistrict(DistrictFacadeEjb.toReferenceDto(source.getResponsibleDistrict()));
		target.setResponsibleCommunity(CommunityFacadeEjb.toReferenceDto(source.getResponsibleCommunity()));

		target.setRegion(RegionFacadeEjb.toReferenceDto(source.getRegion()));
		target.setDistrict(DistrictFacadeEjb.toReferenceDto(source.getDistrict()));
		target.setCommunity(CommunityFacadeEjb.toReferenceDto(source.getCommunity()));
		target.setHealthFacility(FacilityFacadeEjb.toReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());

		target.setReportingUser(UserFacadeEjb.toReferenceDto(source.getReportingUser()));
		target.setReportDate(source.getReportDate());
		target.setInvestigatedDate(source.getInvestigatedDate());
		target.setRegionLevelDate(source.getRegionLevelDate());
		target.setNationalLevelDate(source.getNationalLevelDate());
		target.setDistrictLevelDate(source.getDistrictLevelDate());
		target.setDateFormSentToDistrict(source.getDateFormSentToDistrict());

		target.setSurveillanceOfficer(UserFacadeEjb.toReferenceDto(source.getSurveillanceOfficer()));
		target.setClinicianName(source.getClinicianName());
		target.setClinicianPhone(source.getClinicianPhone());
		target.setClinicianEmail(source.getClinicianEmail());
		target.setReportingOfficerTitle(source.getReportingOfficerTitle());
		target.setReportingOfficerName(source.getReportingOfficerName());
		target.setFunctionOfReportingOfficer(source.getFunctionOfReportingOfficer());
		target.setReportingOfficerContactPhone(source.getReportingOfficerContactPhone());
		target.setReportingOfficerEmail(source.getReportingOfficerEmail());
		target.setCaseOfficer(UserFacadeEjb.toReferenceDto(source.getCaseOfficer()));
		target.setSymptoms(SymptomsFacadeEjb.toSymptomsDto(source.getSymptoms()));
		target.setHomeAddressRecreational(source.getHomeAddressRecreational());
		target.setHospitalName(source.getHospitalName());
		target.setNotifiedBy(source.getNotifiedBy());
		target.setDateOfNotification(source.getDateOfNotification());
		target.setDateOfInvestigation(source.getDateOfInvestigation());

		target.setPregnant(source.getPregnant());
		target.setVaccinationStatus(source.getVaccinationStatus());
		target.setVaccinationType(source.getVaccinationType());
		target.setVaccinationDate(source.getVaccinationDate());
		target.setSmallpoxVaccinationScar(source.getSmallpoxVaccinationScar());
		target.setSmallpoxVaccinationReceived(source.getSmallpoxVaccinationReceived());
		target.setSmallpoxLastVaccinationDate(source.getSmallpoxLastVaccinationDate());

		target.setEpidNumber(source.getEpidNumber());

		target.setReportLat(source.getReportLat());
		target.setReportLon(source.getReportLon());
		target.setReportLatLonAccuracy(source.getReportLatLonAccuracy());

		target.setOutcome(source.getOutcome());
		target.setOutcomeDate(source.getOutcomeDate());
		target.setSequelae(source.getSequelae());
		target.setSequelaeDetails(source.getSequelaeDetails());
		target.setNotifyingClinic(source.getNotifyingClinic());
		target.setNotifyingClinicDetails(source.getNotifyingClinicDetails());

		target.setCreationVersion(source.getCreationVersion());
		target.setCaseOrigin(source.getCaseOrigin());
		target.setPointOfEntry(PointOfEntryFacadeEjb.toReferenceDto(source.getPointOfEntry()));
		target.setPointOfEntryDetails(source.getPointOfEntryDetails());
		target.setAdditionalDetails(source.getAdditionalDetails());
		target.setExternalID(source.getExternalID());
		target.setExternalToken(source.getExternalToken());
		target.setInternalToken(source.getInternalToken());
		target.setSharedToCountry(source.isSharedToCountry());
		target.setQuarantine(source.getQuarantine());
		target.setQuarantineTypeDetails(source.getQuarantineTypeDetails());
		target.setQuarantineTo(source.getQuarantineTo());
		target.setQuarantineFrom(source.getQuarantineFrom());
		target.setQuarantineHelpNeeded(source.getQuarantineHelpNeeded());
		target.setQuarantineOrderedVerbally(source.isQuarantineOrderedVerbally());
		target.setQuarantineOrderedOfficialDocument(source.isQuarantineOrderedOfficialDocument());
		target.setQuarantineOrderedVerballyDate(source.getQuarantineOrderedVerballyDate());
		target.setQuarantineOrderedOfficialDocumentDate(source.getQuarantineOrderedOfficialDocumentDate());
		target.setQuarantineHomePossible(source.getQuarantineHomePossible());
		target.setQuarantineHomePossibleComment(source.getQuarantineHomePossibleComment());
		target.setQuarantineHomeSupplyEnsured(source.getQuarantineHomeSupplyEnsured());
		target.setQuarantineHomeSupplyEnsuredComment(source.getQuarantineHomeSupplyEnsuredComment());
		target.setQuarantineExtended(source.isQuarantineExtended());
		target.setQuarantineReduced(source.isQuarantineReduced());
		target.setQuarantineOfficialOrderSent(source.isQuarantineOfficialOrderSent());
		target.setQuarantineOfficialOrderSentDate(source.getQuarantineOfficialOrderSentDate());
		target.setPostpartum(source.getPostpartum());
		target.setTrimester(source.getTrimester());
		target.setVaccineType(source.getVaccineType());
		target.setNumberOfDoses(source.getNumberOfDoses());
		target.setFollowUpComment(source.getFollowUpComment());
		target.setFollowUpStatus(source.getFollowUpStatus());
		target.setFollowUpUntil(source.getFollowUpUntil());
		target.setOverwriteFollowUpUntil(source.isOverwriteFollowUpUntil());
		target.setFacilityType(source.getFacilityType());

		target.setCaseIdIsm(source.getCaseIdIsm());
		target.setContactTracingFirstContactType(source.getContactTracingFirstContactType());
		target.setContactTracingFirstContactDate(source.getContactTracingFirstContactDate());
		target.setWasInQuarantineBeforeIsolation(source.getWasInQuarantineBeforeIsolation());
		target.setQuarantineReasonBeforeIsolation(source.getQuarantineReasonBeforeIsolation());
		target.setQuarantineReasonBeforeIsolationDetails(source.getQuarantineReasonBeforeIsolationDetails());
		target.setEndOfIsolationReason(source.getEndOfIsolationReason());
		target.setEndOfIsolationReasonDetails(source.getEndOfIsolationReasonDetails());

		target.setNosocomialOutbreak(source.isNosocomialOutbreak());
		target.setInfectionSetting(source.getInfectionSetting());

		target.setProhibitionToWork(source.getProhibitionToWork());
		target.setProhibitionToWorkFrom(source.getProhibitionToWorkFrom());
		target.setProhibitionToWorkUntil(source.getProhibitionToWorkUntil());

		target.setReInfection(source.getReInfection());
		target.setPreviousInfectionDate(source.getPreviousInfectionDate());
		target.setReinfectionStatus(source.getReinfectionStatus());
		if (source.getReinfectionDetails() != null) {
			target.setReinfectionDetails(new HashMap<>(source.getReinfectionDetails()));
		}

		target.setBloodOrganOrTissueDonated(source.getBloodOrganOrTissueDonated());

		target.setNotACaseReasonNegativeTest(source.isNotACaseReasonNegativeTest());
		target.setNotACaseReasonPhysicianInformation(source.isNotACaseReasonPhysicianInformation());
		target.setNotACaseReasonDifferentPathogen(source.isNotACaseReasonDifferentPathogen());
		target.setNotACaseReasonOther(source.isNotACaseReasonOther());
		target.setNotACaseReasonDetails(source.getNotACaseReasonDetails());
		target.setSormasToSormasOriginInfo(SormasToSormasOriginInfoFacadeEjb.toDto(source.getSormasToSormasOriginInfo()));
		target.setOwnershipHandedOver(source.getSormasToSormasShares().stream().anyMatch(ShareInfoHelper::isOwnerShipHandedOver));
		target.setFollowUpStatusChangeDate(source.getFollowUpStatusChangeDate());
		if (source.getFollowUpStatusChangeUser() != null) {
			target.setFollowUpStatusChangeUser(source.getFollowUpStatusChangeUser().toReference());
		}
		target.setDontShareWithReportingTool(source.isDontShareWithReportingTool());
		target.setCaseReferenceDefinition(source.getCaseReferenceDefinition());
		target.setPreviousQuarantineTo(source.getPreviousQuarantineTo());
		target.setQuarantineChangeComment(source.getQuarantineChangeComment());

		if (source.getExternalData() != null) {
			target.setExternalData(new HashMap<>(source.getExternalData()));
		}

		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());
		target.setCaseTransmissionClassification(source.getCaseTransmissionClassification());
		target.setVaccinationRoutine(source.getVaccinationRoutine());
		target.setVaccinationRoutineDate(source.getVaccinationRoutineDate());

		target.setLastVaccinationDate(source.getLastVaccinationDate());
		target.setAddressMpox(source.getAddressMpox());
		target.setVillage(source.getVillage());
		target.setCity(source.getCity());
		target.setNationality(source.getNationality());
		target.setEthnicity(source.getEthnicity());
		target.setOccupation(source.getOccupation());
		target.setSpecifyEventDiagnosis(source.getSpecifyEventDiagnosis());
		target.setNotifiedByList(source.getNotifiedByList());
		target.setNotifiedOther(source.getNotifiedOther());
		target.setMobileTeamNo(source.getMobileTeamNo());
		target.setInformationGivenBy(source.getInformationGivenBy());
		target.setFamilyLinkWithPatient(source.getFamilyLinkWithPatient());
		target.setNameOfVillagePersonGotIll(source.getNameOfVillagePersonGotIll());

		target.setMotherVaccinatedWithTT(source.getMotherVaccinatedWithTT());
		target.setMotherHaveCard(source.getMotherHaveCard());
		target.setMotherNumberOfDoses(source.getMotherNumberOfDoses());
		target.setMotherVaccinationStatus(source.getMotherVaccinationStatus());
		target.setMotherTTDateOne(source.getMotherTTDateOne());
		target.setMotherTTDateTwo(source.getMotherTTDateTwo());
		target.setMotherTTDateThree(source.getMotherTTDateThree());
		target.setMotherTTDateFour(source.getMotherTTDateFour());
		target.setMotherTTDateFive(source.getMotherTTDateFive());
		target.setMotherLastDoseDate(source.getMotherLastDoseDate());
		target.setSeenInOPD(source.getSeenInOPD());
		target.setAdmittedInOPD(source.getAdmittedInOPD());
		target.setMotherGivenProtectiveDoseTT(source.getMotherGivenProtectiveDoseTT());
		target.setMotherGivenProtectiveDoseTTDate(source.getMotherGivenProtectiveDoseTTDate());
		target.setSupplementalImmunization(source.getSupplementalImmunization());
		target.setSupplementalImmunizationDetails(source.getSupplementalImmunizationDetails());
		target.setSecondVaccinationDate(source.getSecondVaccinationDate());
		target.setReportingVillage(source.getReportingVillage());
		target.setReportingZone(source.getReportingZone());
		target.setInvestigationOfficerName(source.getInvestigationOfficerName());
		target.setInvestigationOfficerPosition(source.getInvestigationOfficerPosition());
		target.setFormCompletedByName(source.getFormCompletedByName());
		target.setFormCompletedByPosition(source.getFormCompletedByPosition());
		target.setFormCompletedByCellPhoneNo(source.getFormCompletedByCellPhoneNo());
		target.setOtherNotesAndObservations(source.getOtherNotesAndObservations());
		target.setDateLatestUpdateRecord(source.getDateLatestUpdateRecord());
		target.setNumberOfPeopleInSameHousehold(source.getNumberOfPeopleInSameHousehold());
		target.setRegionOfResidence(RegionFacadeEjb.toReferenceDto(source.getRegionOfResidence()));
		target.setDistrictOfResidence(DistrictFacadeEjb.toReferenceDto(source.getDistrictOfResidence()));
		target.setInvestigationOfficerAddress(source.getInvestigationOfficerAddress());

		return target;
	}

	@Override
	protected CaseReferenceDto toRefDto(Case aCase) {
		return convertToReferenceDto(aCase);
	}

	public Case fillOrBuildEntity(@NotNull CaseDataDto source, Case target, boolean checkChangeDate) {
		boolean targetWasNull = isNull(target);

		target = DtoHelper.fillOrBuildEntity(source, target, Case::build, checkChangeDate);

		if (targetWasNull) {
			FacadeHelper.setUuidIfDtoExists(target.getHospitalization(), source.getHospitalization());
			FacadeHelper.setUuidIfDtoExists(target.getEpiData(), source.getEpiData());
			FacadeHelper.setUuidIfDtoExists(target.getSymptoms(), source.getSymptoms());
			FacadeHelper.setUuidIfDtoExists(target.getSixtyDay(), source.getSixtyDay());
			FacadeHelper.setUuidIfDtoExists(target.getRiskFactor(), source.getRiskFactor());
			FacadeHelper.setUuidIfDtoExists(target.getAfpImmunization(), source.getAfpImmunization());
			FacadeHelper.setUuidIfDtoExists(target.getFoodHistory(), source.getFoodHistory());
			FacadeHelper.setUuidIfDtoExists(target.getInvestigationNotes(), source.getInvestigationNotes());
		}

		target.setDisease(source.getDisease());
		target.setDiseaseVariant(source.getDiseaseVariant());
		target.setDiseaseDetails(source.getDiseaseDetails());
		target.setDiseaseVariantDetails(source.getDiseaseVariantDetails());
		target.setPlagueType(source.getPlagueType());
		target.setDengueFeverType(source.getDengueFeverType());
		target.setIdsrDiagnosis(source.getIdsrDiagnosis());
		target.setRabiesType(source.getRabiesType());
		if (source.getReportDate() != null) {
			target.setReportDate(source.getReportDate());
		} else {
			// make sure we do have a report date
			target.setReportDate(new Date());
		}
		target.setReportingUser(userService.getByReferenceDto(source.getReportingUser()));
		target.setInvestigatedDate(source.getInvestigatedDate());
		target.setRegionLevelDate(source.getRegionLevelDate());
		target.setNationalLevelDate(source.getNationalLevelDate());
		target.setDistrictLevelDate(source.getDistrictLevelDate());
		target.setDateFormSentToDistrict(source.getDateFormSentToDistrict());
		target.setPerson(personService.getByReferenceDto(source.getPerson()));
		target.setCaseClassification(source.getCaseClassification() == CaseClassification.NOT_CLASSIFIED ? CaseClassification.SUSPECT : source.getCaseClassification());
		target.setCaseIdentificationSource(source.getCaseIdentificationSource());
		target.setScreeningType(source.getScreeningType());
		target.setClassificationUser(userService.getByReferenceDto(source.getClassificationUser()));
		target.setClassificationDate(source.getClassificationDate());
		target.setClassificationComment(source.getClassificationComment());
		target.setClinicalConfirmation(source.getClinicalConfirmation());
		target.setEpidemiologicalConfirmation(source.getEpidemiologicalConfirmation());
		target.setLaboratoryDiagnosticConfirmation(source.getLaboratoryDiagnosticConfirmation());
		target.setInvestigationStatus(source.getInvestigationStatus());
		target.setSixtyDay(sixtyDayFacade.fillOrBuildEntity(source.getSixtyDay(), target.getSixtyDay(),checkChangeDate));
		target.setInvestigationNotes(investigationNotesFacade.fillOrBuildEntity(source.getInvestigationNotes(), target.getInvestigationNotes(),checkChangeDate));
		target.setAfpImmunization(afpImmunizationFacade.fillOrBuildEntity(source.getAfpImmunization(), target.getAfpImmunization(),checkChangeDate));
		target.setFoodHistory(foodHistoryFacade.fillOrBuildEntity(source.getFoodHistory(), target.getFoodHistory(),checkChangeDate));

		target.setRiskFactor(riskFactorFacade.fillOrBuildEntity(source.getRiskFactor(), target.getRiskFactor(), checkChangeDate));
		target.setHospitalization(hospitalizationFacade.fillOrBuildEntity(source.getHospitalization(), target.getHospitalization(), checkChangeDate));
		target.setEpiData(epiDataFacade.fillOrBuildEntity(source.getEpiData(), target.getEpiData(), checkChangeDate));
		if (source.getTherapy() == null) {
			source.setTherapy(TherapyDto.build());
		}
		target.setTherapy(therapyFacade.fillOrBuildEntity(source.getTherapy(), target.getTherapy(), checkChangeDate));
		if (source.getHealthConditions() == null) {
			source.setHealthConditions(HealthConditionsDto.build());
		}
		target.setHealthConditions(
				healthConditionsMapper.fillOrBuildEntity(source.getHealthConditions(), target.getHealthConditions(), checkChangeDate));
		if (source.getClinicalCourse() == null) {
			source.setClinicalCourse(ClinicalCourseDto.build());
		}
		target.setClinicalCourse(clinicalCourseFacade.fillOrBuildEntity(source.getClinicalCourse(), target.getClinicalCourse(), checkChangeDate));
		if (source.getMaternalHistory() == null) {
			source.setMaternalHistory(MaternalHistoryDto.build());
		}
		target.setMaternalHistory(maternalHistoryFacade.fillOrBuildEntity(source.getMaternalHistory(), target.getMaternalHistory(), checkChangeDate));
		if (source.getPortHealthInfo() == null) {
			source.setPortHealthInfo(PortHealthInfoDto.build());
		}
		target.setPortHealthInfo(portHealthInfoFacade.fillOrBuildEntity(source.getPortHealthInfo(), target.getPortHealthInfo(), checkChangeDate));

		target.setResponsibleRegion(regionService.getByReferenceDto(source.getResponsibleRegion()));
		target.setResponsibleDistrict(districtService.getByReferenceDto(source.getResponsibleDistrict()));
		target.setResponsibleCommunity(communityService.getByReferenceDto(source.getResponsibleCommunity()));

		target.setRegion(regionService.getByReferenceDto(source.getRegion()));
		target.setDistrict(districtService.getByReferenceDto(source.getDistrict()));
		target.setCommunity(communityService.getByReferenceDto(source.getCommunity()));
		target.setHealthFacility(facilityService.getByReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());

		target.setSurveillanceOfficer(userService.getByReferenceDto(source.getSurveillanceOfficer()));
		target.setClinicianName(source.getClinicianName());
		target.setClinicianPhone(source.getClinicianPhone());
		target.setClinicianEmail(source.getClinicianEmail());
		target.setReportingOfficerName(source.getReportingOfficerName());
		target.setReportingOfficerTitle(source.getReportingOfficerTitle());
		target.setFunctionOfReportingOfficer(source.getFunctionOfReportingOfficer());
		target.setReportingOfficerContactPhone(source.getReportingOfficerContactPhone());
		target.setReportingOfficerEmail(source.getReportingOfficerEmail());
		target.setCaseOfficer(userService.getByReferenceDto(source.getCaseOfficer()));
		target.setSymptoms(symptomsFacade.fromDto(source.getSymptoms(), checkChangeDate));
		target.setHomeAddressRecreational(source.getHomeAddressRecreational());
		target.setHospitalName(source.getHospitalName());
		target.setNotifiedBy(source.getNotifiedBy());
		target.setDateOfNotification(source.getDateOfNotification());
		target.setDateOfInvestigation(source.getDateOfInvestigation());
		//target.setSymptoms(symptomsFacade.fillOrBuildEntity(source.getSymptoms(), target.getSymptoms(), checkChangeDate));

		target.setPregnant(source.getPregnant());
		target.setVaccinationType(source.getVaccinationType());
		target.setVaccinationDate(source.getVaccinationDate());
		target.setVaccinationStatus(source.getVaccinationStatus());
		target.setSmallpoxVaccinationScar(source.getSmallpoxVaccinationScar());
		target.setSmallpoxVaccinationReceived(source.getSmallpoxVaccinationReceived());
		target.setSmallpoxLastVaccinationDate(source.getSmallpoxLastVaccinationDate());

		target.setEpidNumber(source.getEpidNumber());

		target.setReportLat(source.getReportLat());
		target.setReportLon(source.getReportLon());
		target.setReportLatLonAccuracy(source.getReportLatLonAccuracy());

		if (source.getOutcome() != null) {
			target.setOutcome(source.getOutcome());
		}
		target.setOutcomeDate(source.getOutcomeDate());
		target.setSequelae(source.getSequelae());
		target.setSequelaeDetails(source.getSequelaeDetails());
		target.setNotifyingClinic(source.getNotifyingClinic());
		target.setNotifyingClinicDetails(source.getNotifyingClinicDetails());

		target.setCreationVersion(source.getCreationVersion());
		if (source.getCaseOrigin() != null) {
			target.setCaseOrigin(source.getCaseOrigin());
		}
		target.setPointOfEntry(pointOfEntryService.getByReferenceDto(source.getPointOfEntry()));
		target.setPointOfEntryDetails(source.getPointOfEntryDetails());
		target.setAdditionalDetails(source.getAdditionalDetails());
		target.setExternalID(source.getExternalID());
		target.setExternalToken(source.getExternalToken());
		target.setInternalToken(source.getInternalToken());
		target.setSharedToCountry(source.isSharedToCountry());
		target.setQuarantine(source.getQuarantine());
		target.setQuarantineTypeDetails(source.getQuarantineTypeDetails());
		target.setQuarantineTo(source.getQuarantineTo());
		target.setQuarantineFrom(source.getQuarantineFrom());
		target.setQuarantineHelpNeeded(source.getQuarantineHelpNeeded());
		target.setQuarantineOrderedVerbally(source.isQuarantineOrderedVerbally());
		target.setQuarantineOrderedOfficialDocument(source.isQuarantineOrderedOfficialDocument());
		target.setQuarantineOrderedVerballyDate(source.getQuarantineOrderedVerballyDate());
		target.setQuarantineOrderedOfficialDocumentDate(source.getQuarantineOrderedOfficialDocumentDate());
		target.setQuarantineHomePossible(source.getQuarantineHomePossible());
		target.setQuarantineHomePossibleComment(source.getQuarantineHomePossibleComment());
		target.setQuarantineHomeSupplyEnsured(source.getQuarantineHomeSupplyEnsured());
		target.setQuarantineHomeSupplyEnsuredComment(source.getQuarantineHomeSupplyEnsuredComment());
		target.setQuarantineExtended(source.isQuarantineExtended());
		target.setQuarantineReduced(source.isQuarantineReduced());
		target.setQuarantineOfficialOrderSent(source.isQuarantineOfficialOrderSent());
		target.setQuarantineOfficialOrderSentDate(source.getQuarantineOfficialOrderSentDate());
		target.setPostpartum(source.getPostpartum());
		target.setTrimester(source.getTrimester());
		target.setVaccineType(source.getVaccineType());
		target.setNumberOfDoses(source.getNumberOfDoses());
		target.setFacilityType(source.getFacilityType());
		if (source.getSormasToSormasOriginInfo() != null) {
			target.setSormasToSormasOriginInfo(originInfoService.getByUuid(source.getSormasToSormasOriginInfo().getUuid()));
		}

		// TODO this makes sure follow-up is not overriden from the mobile app side. remove once that is implemented
		if (source.getFollowUpStatus() != null) {
			target.setFollowUpComment(source.getFollowUpComment());
			target.setFollowUpStatus(source.getFollowUpStatus());
			target.setFollowUpUntil(source.getFollowUpUntil());
			target.setOverwriteFollowUpUntil(source.isOverwriteFollowUpUntil());
			target.setFollowUpStatusChangeDate(source.getFollowUpStatusChangeDate());
			target.setFollowUpStatusChangeUser(userService.getByReferenceDto(source.getFollowUpStatusChangeUser()));
		}

		target.setCaseIdIsm(source.getCaseIdIsm());
		target.setContactTracingFirstContactType(source.getContactTracingFirstContactType());
		target.setContactTracingFirstContactDate(source.getContactTracingFirstContactDate());
		target.setQuarantineReasonBeforeIsolation(source.getQuarantineReasonBeforeIsolation());
		target.setWasInQuarantineBeforeIsolation(source.getWasInQuarantineBeforeIsolation());
		target.setQuarantineReasonBeforeIsolationDetails(source.getQuarantineReasonBeforeIsolationDetails());
		target.setEndOfIsolationReason(source.getEndOfIsolationReason());
		target.setEndOfIsolationReasonDetails(source.getEndOfIsolationReasonDetails());

		target.setNosocomialOutbreak(source.isNosocomialOutbreak());
		target.setInfectionSetting(source.getInfectionSetting());

		target.setProhibitionToWork(source.getProhibitionToWork());
		target.setProhibitionToWorkFrom(source.getProhibitionToWorkFrom());
		target.setProhibitionToWorkUntil(source.getProhibitionToWorkUntil());

		target.setReInfection(source.getReInfection());
		target.setPreviousInfectionDate(source.getPreviousInfectionDate());
		target.setReinfectionStatus(source.getReinfectionStatus());
		target.setReinfectionDetails(source.getReinfectionDetails());

		target.setBloodOrganOrTissueDonated(source.getBloodOrganOrTissueDonated());

		target.setNotACaseReasonNegativeTest(source.isNotACaseReasonNegativeTest());
		target.setNotACaseReasonPhysicianInformation(source.isNotACaseReasonPhysicianInformation());
		target.setNotACaseReasonDifferentPathogen(source.isNotACaseReasonDifferentPathogen());
		target.setNotACaseReasonOther(source.isNotACaseReasonOther());
		target.setNotACaseReasonDetails(source.getNotACaseReasonDetails());
		target.setDontShareWithReportingTool(source.isDontShareWithReportingTool());
		target.setCaseReferenceDefinition(source.getCaseReferenceDefinition());
		target.setPreviousQuarantineTo(source.getPreviousQuarantineTo());
		target.setQuarantineChangeComment(source.getQuarantineChangeComment());

		if (source.getExternalData() != null) {
			target.setExternalData(source.getExternalData());
		}

		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());
		target.setCaseTransmissionClassification(source.getCaseTransmissionClassification());
		target.setSecondVaccinationDate(source.getSecondVaccinationDate());
		target.setLastVaccinationDate(source.getLastVaccinationDate());

		target.setVaccinationRoutine(source.getVaccinationRoutine());
		target.setVaccinationRoutineDate(source.getVaccinationRoutineDate());

		target.setMotherVaccinatedWithTT(source.getMotherVaccinatedWithTT());
		target.setMotherHaveCard(source.getMotherHaveCard());
		target.setMotherNumberOfDoses(source.getMotherNumberOfDoses());
		target.setMotherVaccinationStatus(source.getMotherVaccinationStatus());
		target.setMotherTTDateOne(source.getMotherTTDateOne());
		target.setMotherTTDateTwo(source.getMotherTTDateTwo());
		target.setMotherTTDateThree(source.getMotherTTDateThree());
		target.setMotherTTDateFour(source.getMotherTTDateFour());
		target.setMotherTTDateFive(source.getMotherTTDateFive());
		target.setMotherLastDoseDate(source.getMotherLastDoseDate());

		target.setSeenInOPD(source.getSeenInOPD());
		target.setAdmittedInOPD(source.getAdmittedInOPD());
		target.setMotherGivenProtectiveDoseTT(source.getMotherGivenProtectiveDoseTT());
		target.setMotherGivenProtectiveDoseTTDate(source.getMotherGivenProtectiveDoseTTDate());
		target.setSupplementalImmunization(source.getSupplementalImmunization());
		target.setSupplementalImmunizationDetails(source.getSupplementalImmunizationDetails());
		target.setAddressMpox(source.getAddressMpox());
		target.setVillage(source.getVillage());
		target.setCity(source.getCity());
		target.setNationality(source.getNationality());
		target.setEthnicity(source.getEthnicity());
		target.setOccupation(source.getOccupation());
		target.setRegionOfResidence(regionService.getByReferenceDto(source.getRegionOfResidence()));
		target.setDistrictOfResidence(districtService.getByReferenceDto(source.getDistrictOfResidence()));
		target.setReportingZone(source.getReportingZone());
		target.setReportingVillage(source.getReportingVillage());
		target.setSpecifyEventDiagnosis(source.getSpecifyEventDiagnosis());
		target.setInvestigationOfficerName(source.getInvestigationOfficerName());
		target.setInvestigationOfficerPosition(source.getInvestigationOfficerPosition());
		target.setOtherNotesAndObservations(source.getOtherNotesAndObservations());
		target.setDateLatestUpdateRecord(source.getDateLatestUpdateRecord());
		target.setNumberOfPeopleInSameHousehold(source.getNumberOfPeopleInSameHousehold());
		target.setFormCompletedByName(source.getFormCompletedByName());
		target.setFormCompletedByPosition(source.getFormCompletedByPosition());
		target.setFormCompletedByCellPhoneNo(source.getFormCompletedByCellPhoneNo());
		target.setNotifiedByList(source.getNotifiedByList());
		target.setNotifiedOther(source.getNotifiedOther());
		target.setMobileTeamNo(source.getMobileTeamNo());
		target.setInformationGivenBy(source.getInformationGivenBy());
		target.setFamilyLinkWithPatient(source.getFamilyLinkWithPatient());
		target.setNameOfVillagePersonGotIll(source.getNameOfVillagePersonGotIll());
		target.setInvestigationOfficerAddress(source.getInvestigationOfficerAddress());

		return target;
	}

	private Map<ReinfectionDetail, Boolean> cleanupReinfectionDetails(Map<ReinfectionDetail, Boolean> reinfectionDetails) {
		if (reinfectionDetails != null && reinfectionDetails.containsValue(Boolean.FALSE)) {
			Map<ReinfectionDetail, Boolean> onlyTrueReinfectionDetails = new HashMap<>();
			onlyTrueReinfectionDetails =
					reinfectionDetails.entrySet().stream().filter(Map.Entry::getValue).collect(Collectors.toMap(Map.Entry::getKey, entry -> true));

			return onlyTrueReinfectionDetails;
		} else {
			return reinfectionDetails;
		}
	}

	@Override
	protected CoreEntityType getCoreEntityType() {
		return CoreEntityType.CASE;
	}

	private void updateInvestigationByStatus(CaseDataDto existingCase, Case caze) {

		CaseReferenceDto caseRef = caze.toReference();
		InvestigationStatus investigationStatus = caze.getInvestigationStatus();

		if (investigationStatus != InvestigationStatus.PENDING) {
			// Set the investigation date
			if (caze.getInvestigatedDate() == null) {
				caze.setInvestigatedDate(new Date());
			}

			// Set the task status of all investigation tasks to "Removed" because
			// the case status has been updated manually
			if (existingCase != null) {
				List<Task> pendingTasks =
						taskService.findBy(new TaskCriteria().taskType(TaskType.CASE_INVESTIGATION).caze(caseRef).taskStatus(TaskStatus.PENDING), true);
				final boolean caseStatusSetToDone =
						caze.getInvestigationStatus() == InvestigationStatus.DONE && existingCase.getInvestigationStatus() != InvestigationStatus.DONE;
				for (Task task : pendingTasks) {
					task.setTaskStatus(caseStatusSetToDone ? TaskStatus.DONE : TaskStatus.REMOVED);
					task.setStatusChangeDate(new Date());
				}

				if (caseStatusSetToDone) {
					sendInvestigationDoneNotifications(caze);
				}
			}

		} else {
			// Remove the investigation date
			caze.setInvestigatedDate(null);

			// Create a new investigation task if none is present
			long investigationTaskCount =
					existingCase != null ? taskService.getCount(new TaskCriteria().taskType(TaskType.CASE_INVESTIGATION).caze(caseRef)) : 0;

			if (investigationTaskCount == 0 && featureConfigurationFacade.isTaskGenerationFeatureEnabled(TaskType.CASE_INVESTIGATION)) {
				createInvestigationTask(caze);
			}
		}
	}

	@RightsAllowed(UserRight._CASE_EDIT)
	public void updateInvestigationByTask(Case caze) {

		CaseReferenceDto caseRef = caze.toReference();

		// any pending case investigation task?
		long pendingCount =
				taskService.getCount(new TaskCriteria().taskType(TaskType.CASE_INVESTIGATION).caze(caseRef).taskStatus(TaskStatus.PENDING));

		if (pendingCount > 0) {
			// set status to investigation pending
			caze.setInvestigationStatus(InvestigationStatus.PENDING);
			// .. and clear date
			caze.setInvestigatedDate(null);
		} else {
			// get "case investigation" task created last
			List<Task> cazeTasks = taskService.findBy(new TaskCriteria().taskType(TaskType.CASE_INVESTIGATION).caze(caseRef), true);

			if (!cazeTasks.isEmpty()) {
				Task youngestTask = cazeTasks.stream().max(new Comparator<Task>() {

					@Override
					public int compare(Task o1, Task o2) {
						return o1.getCreationDate().compareTo(o2.getCreationDate());
					}
				}).get();

				switch (youngestTask.getTaskStatus()) {
					case PENDING:
						throw new UnsupportedOperationException("there should not be any pending tasks");
					case DONE:
						caze.setInvestigationStatus(InvestigationStatus.DONE);
						caze.setInvestigatedDate(youngestTask.getStatusChangeDate());
						sendInvestigationDoneNotifications(caze);
						break;
					case REMOVED:
						caze.setInvestigationStatus(InvestigationStatus.DISCARDED);
						caze.setInvestigatedDate(youngestTask.getStatusChangeDate());
						break;
					case NOT_EXECUTABLE:
						caze.setInvestigationStatus(InvestigationStatus.PENDING);
						caze.setInvestigatedDate(null);
						break;
					default:
						break;
				}
			}
		}
	}

	private void createInvestigationTask(Case caze) {

		Task task = new Task();
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskContext(TaskContext.CASE);
		task.setCaze(caze);
		task.setTaskType(TaskType.CASE_INVESTIGATION);
		task.setSuggestedStart(TaskHelper.getDefaultSuggestedStart());
		task.setDueDate(TaskHelper.getDefaultDueDate());
		task.setPriority(TaskPriority.NORMAL);

		assignOfficerOrSupervisorToTask(caze, task);

		taskService.ensurePersisted(task);
	}

	private void createActiveSearchForOtherCasesTask(Case caze) {

		Task task = new Task();
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskContext(TaskContext.CASE);
		task.setCaze(caze);
		task.setTaskType(TaskType.ACTIVE_SEARCH_FOR_OTHER_CASES);
		task.setSuggestedStart(TaskHelper.getDefaultSuggestedStart());
		task.setDueDate(TaskHelper.getDefaultDueDate());
		task.setPriority(TaskPriority.NORMAL);

		assignOfficerOrSupervisorToTask(caze, task);

		taskService.ensurePersisted(task);
	}

	private void assignOfficerOrSupervisorToTask(Case caze, Task task) {

		User assignee = null;

		if (caze.getSurveillanceOfficer() != null) {
			// 1) The surveillance officer that is responsible for the case
			assignee = caze.getSurveillanceOfficer();
		} else {
			// 2) A random user with UserRight.CASE_RESPONSIBLE from the case responsible district
			assignee = getRandomDistrictCaseResponsible(caze.getResponsibleDistrict());
		}

		if (assignee == null && caze.getDistrict() != null) {
			// 3) A random surveillance officer from the case district
			assignee = getRandomDistrictCaseResponsible(caze.getDistrict());
		}

		if (assignee == null) {
			if (caze.getReportingUser() != null && (userRoleService.hasUserRight(caze.getReportingUser().getUserRoles(), UserRight.TASK_ASSIGN))) {
				// 4) If the case was created by a surveillance supervisor, assign them
				assignee = caze.getReportingUser();
			} else {
				// 5) Assign a random surveillance supervisor from the case responsible region
				assignee = getRandomRegionCaseResponsible(caze.getResponsibleRegion());
			}
			if (assignee == null && caze.getRegion() != null) {
				// 6) Assign a random surveillance supervisor from the case region
				assignee = getRandomRegionCaseResponsible(caze.getRegion());
			}
		}

		task.setAssigneeUser(assignee);
		if (assignee == null) {
			logger.warn("No valid assignee user found for task " + task.getUuid());
		}
	}

	private User getRandomDistrictCaseResponsible(District district) {

		return userService.getRandomDistrictUser(district, UserRight.CASE_RESPONSIBLE);
	}

	private User getRandomRegionCaseResponsible(Region region) {

		return userService.getRandomRegionUser(region, UserRight.CASE_RESPONSIBLE);
	}

	@Override
	public boolean doesEpidNumberExist(String epidNumber, String caseUuid, Disease caseDisease) {
		if (epidNumber == null) {
			return false;
		}

		int suffixSeperatorIndex = epidNumber.lastIndexOf('-');
		if (suffixSeperatorIndex == -1) {
			// no suffix - use the whole string as prefix
			suffixSeperatorIndex = epidNumber.length() - 1;
		}
		String prefixString = epidNumber.substring(0, suffixSeperatorIndex + 1);
		String suffixString = epidNumber.substring(suffixSeperatorIndex + 1);
		suffixString = suffixString.replaceAll("[^\\d]", "");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caze = cq.from(Case.class);

		Predicate filter = cb.and(cb.equal(caze.get(Case.DELETED), false), cb.equal(caze.get(Case.DISEASE), caseDisease));
		if (!DataHelper.isNullOrEmpty(caseUuid)) {
			filter = cb.and(filter, cb.notEqual(caze.get(Case.UUID), caseUuid));
		}

		ParameterExpression<String> regexPattern = null, regexReplacement = null, regexFlags = null;
		if (suffixString.length() > 0) {
			// has to start with prefix
			filter = cb.and(filter, cb.like(caze.get(Case.EPID_NUMBER), prefixString + "%"));

			// for the suffix only consider the actual number. Any other characters and leading zeros are ignored
			int suffixNumber;
			try {
				suffixNumber = Integer.parseInt(suffixString);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						String.format("Invalid suffix for epid number. epidNumber: '%s', suffixString: '%s'", epidNumber, suffixString),
						e);
			}
			regexPattern = cb.parameter(String.class);
			regexReplacement = cb.parameter(String.class);
			regexFlags = cb.parameter(String.class);
			Expression<String> epidNumberSuffixClean = cb.function(
					"regexp_replace",
					String.class,
					cb.substring(caze.get(Case.EPID_NUMBER), suffixSeperatorIndex + 2),
					regexPattern,
					regexReplacement,
					regexFlags);
			filter = cb.and(filter, cb.equal(cb.concat("0", epidNumberSuffixClean).as(Integer.class), suffixNumber));
		} else {
			filter = cb.and(filter, cb.equal(caze.get(Case.EPID_NUMBER), prefixString));
		}
		cq.where(filter);

		cq.select(caze.get(Case.EPID_NUMBER));
		TypedQuery<String> query = em.createQuery(cq);
		if (regexPattern != null) {
			query.setParameter(regexPattern, "\\D"); // Non-digits
			query.setParameter(regexReplacement, ""); // Replace all non-digits with empty string
			query.setParameter(regexFlags, "g"); // Global search
		}
		return QueryHelper.getFirstResult(query) != null;
	}

	@Override
	public boolean doesExternalTokenExist(String externalToken, String caseUuid) {
		return service.exists(
				(cb, caseRoot, cq) -> CriteriaBuilderHelper.and(
						cb,
						cb.equal(caseRoot.get(Case.EXTERNAL_TOKEN), externalToken),
						cb.notEqual(caseRoot.get(Case.UUID), caseUuid),
						cb.notEqual(caseRoot.get(Case.DELETED), Boolean.TRUE)));
	}

	@Override
	public List<Pair<DistrictDto, BigDecimal>> getCaseMeasurePerDistrict(Date fromDate, Date toDate, Disease disease, CaseMeasure caseMeasure) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		final Root<Case> caseRoot = cq.from(Case.class);
		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caseRoot);

		Root<District> districtRoot = cq.from(District.class);

		Predicate filter = service.createDefaultFilter(cb, caseRoot);
		if (fromDate != null || toDate != null) {
			filter = service.createCaseRelevanceFilter(caseQueryContext, fromDate, toDate);
		}

		if (disease != null) {
			Predicate diseaseFilter = cb.equal(caseRoot.get(Case.DISEASE), disease);
			filter = filter != null ? cb.and(filter, diseaseFilter) : diseaseFilter;
		}

		Predicate districtFilter = cb.or(
				cb.equal(caseRoot.get(Case.DISTRICT), districtRoot),
				cb.and(cb.isNull(caseRoot.get(Case.DISTRICT)), cb.equal(caseRoot.get(Case.RESPONSIBLE_DISTRICT), districtRoot)));
		filter = filter != null ? cb.and(filter, districtFilter) : districtFilter;

		cq.where(filter);

		cq.groupBy(districtRoot);
		cq.multiselect(districtRoot, cb.count(caseRoot));
		if (caseMeasure == CaseMeasure.CASE_COUNT) {
			cq.orderBy(cb.asc(cb.count(caseRoot)));
		}
		List<Object[]> results = em.createQuery(cq).getResultList();

		if (caseMeasure == CaseMeasure.CASE_COUNT) {
			return results.stream()
					.map(e -> new Pair<>(districtFacade.toDto((District) e[0]), new BigDecimal((Long) e[1])))
					.collect(Collectors.toList());
		} else {
			return results.stream().map(e -> {
				District district = (District) e[0];
				Integer population = populationDataFacade.getProjectedDistrictPopulation(district.getUuid());
				Long caseCount = (Long) e[1];

				if (population == null || population <= 0) {
					// No, or negative population - these entries will be cut off in the UI
					return new Pair<>(districtFacade.toDto(district), new BigDecimal(0));
				} else {
					return new Pair<>(
							districtFacade.toDto(district),
							InfrastructureHelper.getCaseIncidence(caseCount.intValue(), population, InfrastructureHelper.CASE_INCIDENCE_DIVISOR));
				}
			}).sorted(Comparator.comparing(Pair::getElement1)).collect(Collectors.toList());
		}
	}

	private void sendInvestigationDoneNotifications(Case caze) {

		try {
			String message =
					String.format(I18nProperties.getString(MessageContents.CONTENT_CASE_INVESTIGATION_DONE), DataHelper.getShortUuid(caze.getUuid()));
			notificationService.sendNotifications(
					NotificationType.CASE_INVESTIGATION_DONE,
					JurisdictionHelper.getCaseRegions(caze),
					null,
					MessageSubject.CASE_INVESTIGATION_DONE,
					message);
		} catch (NotificationDeliveryFailedException e) {
			logger.error("NotificationDeliveryFailedException when trying to notify supervisors about the completion of a case investigation.");
		}
	}

	@Override
	public Date getOldestCaseOnsetDate() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Timestamp> cq = cb.createQuery(Timestamp.class);
		Root<Case> from = cq.from(Case.class);
		Join<Case, Symptoms> symptoms = from.join(Case.SYMPTOMS, JoinType.LEFT);

		Path<Timestamp> expression = symptoms.get(Symptoms.ONSET_DATE);
		cq.select(cb.least(expression));
		cq.where(cb.greaterThan(symptoms.get(Symptoms.ONSET_DATE), DateHelper.getDateZero(2000, 1, 1)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Date getOldestCaseReportDate() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Timestamp> cq = cb.createQuery(Timestamp.class);
		Root<Case> from = cq.from(Case.class);

		final Path<Timestamp> reportDate = from.get(Case.REPORT_DATE);
		cq.select(cb.least(reportDate));
		cq.where(cb.greaterThan(from.get(Case.REPORT_DATE), DateHelper.getDateZero(2000, 1, 1)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Date getOldestCaseOutcomeDate() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Timestamp> cq = cb.createQuery(Timestamp.class);
		Root<Case> from = cq.from(Case.class);

		final Path<Timestamp> reportDate = from.get(Case.OUTCOME_DATE);
		cq.select(cb.least(reportDate));
		cq.where(cb.greaterThan(from.get(Case.OUTCOME_DATE), DateHelper.getDateZero(2000, 1, 1)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	@RightsAllowed(UserRight._CASE_MERGE)
	public void mergeCase(String leadUuid, String otherUuid) {

		mergeCase(getCaseDataWithoutPseudonyimization(leadUuid), getCaseDataWithoutPseudonyimization(otherUuid), false);
	}

	private void mergeCase(CaseDataDto leadCaseData, CaseDataDto otherCaseData, boolean cloning) {

		// 1 Merge Dtos
		// 1.1 Case

		copyDtoValues(leadCaseData, otherCaseData, cloning);
		save(leadCaseData, !cloning, true, true, false);

		// 1.2 Person - Only merge when the persons have different UUIDs
		if (!cloning && !DataHelper.equal(leadCaseData.getPerson().getUuid(), otherCaseData.getPerson().getUuid())) {
			PersonDto leadPerson = personFacade.getPersonByUuid(leadCaseData.getPerson().getUuid());
			PersonDto otherPerson = personFacade.getPersonByUuid(otherCaseData.getPerson().getUuid());
			personFacade.mergePerson(leadPerson, otherPerson);
		} else {
			assert (DataHelper.equal(leadCaseData.getPerson().getUuid(), otherCaseData.getPerson().getUuid()));
		}

		// 2 Change CaseReference
		Case leadCase = service.getByUuid(leadCaseData.getUuid());
		Case otherCase = service.getByUuid(otherCaseData.getUuid());

		// 2.1 Contacts
		List<Contact> contacts = contactService.findBy(new ContactCriteria().caze(otherCase.toReference()), null);
		for (Contact contact : contacts) {
			if (cloning) {
				ContactDto newContact =
						ContactDto.build(leadCase.toReference(), leadCase.getDisease(), leadCase.getDiseaseDetails(), leadCase.getDiseaseVariant());
				newContact.setPerson(new PersonReferenceDto(contact.getPerson().getUuid()));
				DtoHelper.copyDtoValues(newContact, contactFacade.toDto(contact), cloning);
				contactFacade.save(newContact, false, false);
			} else {
				// simply move existing entities to the merge target
				contact.setCaze(leadCase);
				contactService.ensurePersisted(contact);
			}
		}

		// 2.2 Samples
		List<Sample> samples = sampleService.findBy(new SampleCriteria().caze(otherCase.toReference()), null);
		for (Sample sample : samples) {
			if (cloning) {
				SampleDto newSample = SampleDto.build(sample.getReportingUser().toReference(), leadCase.toReference());
				DtoHelper.copyDtoValues(newSample, SampleFacadeEjb.toDto(sample), cloning);
				sampleFacade.saveSample(newSample, false, true, true);

				// 2.2.1 Pathogen Tests
				for (PathogenTest pathogenTest : sample.getPathogenTests()) {
					PathogenTestDto newPathogenTest = PathogenTestDto.build(newSample.toReference(), pathogenTest.getLabUser().toReference());
					DtoHelper.copyDtoValues(newPathogenTest, PathogenTestFacadeEjbLocal.toDto(pathogenTest), cloning);
					sampleTestFacade.savePathogenTest(newPathogenTest);
				}

				for (AdditionalTest additionalTest : sample.getAdditionalTests()) {
					AdditionalTestDto newAdditionalTest = AdditionalTestDto.build(newSample.toReference());
					DtoHelper.copyDtoValues(newAdditionalTest, AdditionalTestFacadeEjbLocal.toDto(additionalTest), cloning);
					additionalTestFacade.saveAdditionalTest(newAdditionalTest);
				}
			} else {
				// simply move existing entities to the merge target
				sample.setAssociatedCase(leadCase);
				sampleService.ensurePersisted(sample);
			}
		}

		// 2.3 Tasks
		if (!cloning) {
			// simply move existing entities to the merge target

			List<Task> tasks = taskService.findBy(new TaskCriteria().caze(new CaseReferenceDto(otherCase.getUuid())), true);
			for (Task task : tasks) {
				task.setCaze(leadCase);
				taskService.ensurePersisted(task);
			}
		}

		// 3 Change Therapy Reference
		// 3.1 Treatments
		List<Treatment> treatments =
				treatmentService.findBy(new TreatmentCriteria().therapy(new TherapyReferenceDto(otherCase.getTherapy().getUuid())));
		TherapyReferenceDto leadCaseTherapyReference = new TherapyReferenceDto(leadCase.getTherapy().getUuid());
		for (Treatment treatment : treatments) {
			if (cloning) {
				TreatmentDto newTreatment = TreatmentDto.build(leadCaseTherapyReference);
				DtoHelper.copyDtoValues(newTreatment, TreatmentFacadeEjb.toDto(treatment), cloning);
				treatmentFacade.saveTreatment(newTreatment);
			} else {
				// simply move existing entities to the merge target
				treatment.setTherapy(leadCase.getTherapy());
				treatmentService.ensurePersisted(treatment);
			}
		}

		// 3.2 Prescriptions
		List<Prescription> prescriptions =
				prescriptionService.findBy(new PrescriptionCriteria().therapy(new TherapyReferenceDto(otherCase.getTherapy().getUuid())));
		for (Prescription prescription : prescriptions) {
			if (cloning) {
				PrescriptionDto newPrescription = PrescriptionDto.buildPrescription(leadCaseTherapyReference);
				DtoHelper.copyDtoValues(newPrescription, PrescriptionFacadeEjb.toDto(prescription), cloning);
				prescriptionFacade.savePrescription(newPrescription);
			} else {
				// simply move existing entities to the merge target
				prescription.setTherapy(leadCase.getTherapy());
				prescriptionService.ensurePersisted(prescription);
			}
		}

		// 4 Change Clinical Course Reference
		// 4.1 Clinical Visits
		List<ClinicalVisit> clinicalVisits = clinicalVisitService
				.findBy(new ClinicalVisitCriteria().clinicalCourse(new ClinicalCourseReferenceDto(otherCase.getClinicalCourse().getUuid())));
		for (ClinicalVisit clinicalVisit : clinicalVisits) {
			if (cloning) {
				ClinicalVisitDto newClinicalVisit = ClinicalVisitDto.build(leadCaseData.getClinicalCourse().toReference(), leadCase.getDisease());
				DtoHelper.copyDtoValues(newClinicalVisit, ClinicalVisitFacadeEjb.toDto(clinicalVisit), cloning);
				clinicalVisitFacade.saveClinicalVisit(newClinicalVisit, leadCase.getUuid(), false);
			} else {
				// simply move existing entities to the merge target
				clinicalVisit.setClinicalCourse(leadCase.getClinicalCourse());
				clinicalVisitService.ensurePersisted(clinicalVisit);
			}
		}

		// 5 Attach otherCase visits to leadCase
		// (set the person and the disease of the visit, saveVisit does the rest)
		for (VisitDto otherVisit : otherCase.getVisits().stream().map(v->visitFacade.toDto(v)).collect(Collectors.toList())) {
			otherVisit.setPerson(leadCaseData.getPerson());
			otherVisit.setDisease(leadCaseData.getDisease());
			visitFacade.save(otherVisit);
		}

		// 6 Documents
		List<Document> documents = documentService.getRelatedToEntity(DocumentRelatedEntityType.CASE, otherCase.getUuid());
		for (Document document : documents) {
			document.setRelatedEntityUuid(leadCaseData.getUuid());

			documentService.ensurePersisted(document);
		}

		// 7 Persist Event links through eventparticipants
		Set<EventParticipant> eventParticipants = otherCase.getEventParticipants();
		for (EventParticipant eventParticipant : eventParticipants) {
			eventParticipant.setResultingCase(leadCase);
			eventParticipantService.ensurePersisted(eventParticipant);
		}
		otherCase.getEventParticipants().clear();

		// 8 Exposures - Make sure there are no two probable infection environments
		// if there are more than 2 exposures marked as probable infection environment, find the one that originates from the otherCase and set it to false
		// the one originating from the otherCase should always be found at the higher index
		List<Exposure> probableExposuresList =
				leadCase.getEpiData().getExposures().stream().filter(Exposure::isProbableInfectionEnvironment).collect(Collectors.toList());
		while (probableExposuresList.size() >= 2) {
			// should never be > 2, but still make sure to set all but one exposures to false
			probableExposuresList.get(probableExposuresList.size() - 1).setProbableInfectionEnvironment(false);
			exposureService.ensurePersisted(probableExposuresList.get(probableExposuresList.size() - 1));
			probableExposuresList.remove(probableExposuresList.size() - 1);
		}

		// 9 Reports
		/*List<SurveillanceReport> surveillanceReports = surveillanceReportService.getByCaseUuids(Collections.singletonList(otherCase.getUuid()));
		surveillanceReports.forEach(surveillanceReport -> {
			SurveillanceReportDto surveillanceReportDto = this.toDto(surveillanceReport);
			surveillanceReportDto.setCaze(leadCase.toReference());
			surveillanceReportFacade.saveSurveillanceReport(surveillanceReportDto);
		});*/

		// 10 Activity as case
		final EpiData otherEpiData = otherCase.getEpiData();
		if (otherEpiData != null
				&& YesNo.YES == otherEpiData.getActivityAsCaseDetailsKnown()
				&& CollectionUtils.isNotEmpty(otherEpiData.getActivitiesAsCase())) {

			final EpiData leadEpiData = leadCase.getEpiData();
			leadEpiData.setActivityAsCaseDetailsKnown(YesNo.YES);
			epiDataService.ensurePersisted(leadEpiData);
		}

		// Travel entries reference
		List<TravelEntry> travelEntries = travelEntryService.getAllByResultingCase(otherCase);
		travelEntries.forEach(t -> {
			t.setResultingCase(leadCase);
			t.setPerson(leadCase.getPerson());
			travelEntryService.ensurePersisted(t);
		});
	}

	private void copyDtoValues(CaseDataDto leadCaseData, CaseDataDto otherCaseData, boolean cloning) {
		String leadAdditionalDetails = leadCaseData.getAdditionalDetails();
		String leadFollowUpComment = leadCaseData.getFollowUpComment();

		DtoHelper.copyDtoValues(leadCaseData, otherCaseData, cloning);

		if (!cloning) {
			leadCaseData.setAdditionalDetails(DataHelper.joinStrings(" ", leadAdditionalDetails, otherCaseData.getAdditionalDetails()));
			leadCaseData.setFollowUpComment(DataHelper.joinStrings(" ", leadFollowUpComment, otherCaseData.getFollowUpComment()));
		}
	}

	@Override
	@RightsAllowed(UserRight._CASE_CREATE)
	public CaseDataDto cloneCase(CaseDataDto existingCaseDto) {

		CaseDataDto newCase = CaseDataDto.build(existingCaseDto.getPerson(), existingCaseDto.getDisease());
		mergeCase(newCase, existingCaseDto, true);
		return getCaseDataByUuid(newCase.getUuid());
	}

	/**
	 * Archives all cases that have not been changed for a defined amount of days
	 *
	 * @param daysAfterCaseGetsArchived
	 *            defines the amount of days
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@RightsAllowed(UserRight._SYSTEM)
	public void archiveAllArchivableCases(int daysAfterCaseGetsArchived) {

		archiveAllArchivableCases(daysAfterCaseGetsArchived, LocalDate.now());
	}

	@RightsAllowed(UserRight._SYSTEM)
	public void archiveAllArchivableCases(int daysAfterCaseGetsArchived, LocalDate referenceDate) {

		long startTime = DateHelper.startTime();

		LocalDate notChangedSince = referenceDate.minusDays(daysAfterCaseGetsArchived);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> from = cq.from(Case.class);

		CaseQueryContext qc = new CaseQueryContext(cb, cq, from);

		Timestamp notChangedTimestamp = Timestamp.valueOf(notChangedSince.atStartOfDay());
		cq.where(
				cb.equal(from.get(Case.ARCHIVED), false),
				cb.equal(from.get(Case.DELETED), false),
				cb.not(service.createChangeDateFilter(cb, qc.getJoins(), notChangedTimestamp, true)));
		cq.select(from.get(Case.UUID)).distinct(true);
		List<String> caseUuids = em.createQuery(cq).getResultList();

		if (!caseUuids.isEmpty()) {
			archive(caseUuids, true);
		}

		logger.debug(
				"archiveAllArchivableCases() finished. caseCount = {}, daysAfterCaseGetsArchived = {}, {}ms",
				caseUuids.size(),
				daysAfterCaseGetsArchived,
				DateHelper.durationMillies(startTime));
	}

	public Page<CaseFollowUpDto> getCaseFollowUpIndexPage(
			CaseFollowUpCriteria criteria,
			Integer offset,
			Integer size,
			List<SortProperty> sortProperties) {
		List<CaseFollowUpDto> caseFollowUpIndexList =
				getCaseFollowUpList(criteria, criteria.getReferenceDate(), criteria.getInterval(), offset, size, sortProperties);
		long totalElementCount = count(criteria);
		return new Page<>(caseFollowUpIndexList, offset, size, totalElementCount);

	}

	@Override
	public List<CaseFollowUpDto> getCaseFollowUpList(
			CaseCriteria caseCriteria,
			Date referenceDate,
			int interval,
			Integer first,
			Integer max,
			List<SortProperty> sortProperties) {

		Date end = DateHelper.getEndOfDay(referenceDate);
		Date start = DateHelper.getStartOfDay(DateHelper.subtractDays(end, interval));

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseFollowUpDto> cq = cb.createQuery(CaseFollowUpDto.class);
		Root<Case> caze = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, caze);
		final CaseJoins joins = caseQueryContext.getJoins();

		cq.multiselect(
				caze.get(Case.UUID),
				caze.get(Case.CHANGE_DATE),
				joins.getPerson().get(Person.FIRST_NAME),
				joins.getPerson().get(Person.LAST_NAME),
				joins.getPerson().get(Person.OTHER_NAME),
				caze.get(Case.REPORT_DATE),
				joins.getSymptoms().get(Symptoms.ONSET_DATE),
				caze.get(Case.FOLLOW_UP_UNTIL),
				joins.getPerson().get(Person.SYMPTOM_JOURNAL_STATUS),
				caze.get(Case.DISEASE),
				JurisdictionHelper.booleanSelector(cb, service.inJurisdictionOrOwned(caseQueryContext)));

		Predicate filter =
				CriteriaBuilderHelper.and(cb, service.createUserFilter(caseQueryContext), service.createCriteriaFilter(caseCriteria, caseQueryContext));

		if (filter != null) {
			cq.where(filter);
		}

		cq.distinct(true);

		if (sortProperties != null && !sortProperties.isEmpty()) {
			List<Order> order = new ArrayList<>(sortProperties.size());
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
					case FollowUpDto.UUID:
					case FollowUpDto.REPORT_DATE:
					case FollowUpDto.FOLLOW_UP_UNTIL:
						expression = caze.get(sortProperty.propertyName);
						break;
					case FollowUpDto.FIRST_NAME:
						expression = joins.getPerson().get(Person.FIRST_NAME);
						order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						break;
					case FollowUpDto.SYMPTOM_JOURNAL_STATUS:
						expression = joins.getPerson().get(Person.SYMPTOM_JOURNAL_STATUS);
						break;
					case FollowUpDto.LAST_NAME:
						expression = joins.getPerson().get(Person.LAST_NAME);
						order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
						break;
					default:
						throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
			}
			cq.orderBy(order);
		} else {
			cq.orderBy(cb.desc(caze.get(Case.CHANGE_DATE)));
		}

		List<CaseFollowUpDto> resultList = QueryHelper.getResultList(em, cq, first, max);
		if (!resultList.isEmpty()) {

			List<String> caseUuids = resultList.stream().map(FollowUpDto::getUuid).collect(Collectors.toList());

			CriteriaQuery<Object[]> visitsCq = cb.createQuery(Object[].class);
			Root<Case> visitsCqRoot = visitsCq.from(Case.class);
			Join<Case, Visit> visitsJoin = visitsCqRoot.join(Case.VISITS, JoinType.LEFT);
			Join<Visit, Symptoms> visitSymptomsJoin = visitsJoin.join(Visit.SYMPTOMS, JoinType.LEFT);

			visitsCq.where(
					CriteriaBuilderHelper.and(
							cb,
							caze.get(AbstractDomainObject.UUID).in(caseUuids),
							cb.isNotEmpty(visitsCqRoot.get(Case.VISITS)),
							cb.between(visitsJoin.get(Visit.VISIT_DATE_TIME), start, end)));
			visitsCq.multiselect(
					visitsCqRoot.get(Case.UUID),
					visitsJoin.get(Visit.VISIT_DATE_TIME),
					visitsJoin.get(Visit.VISIT_STATUS),
					visitsJoin.get(Visit.ORIGIN),
					visitSymptomsJoin.get(Symptoms.SYMPTOMATIC));
			// Sort by visit date so that we'll have the latest visit of each day
			visitsCq.orderBy(cb.asc(visitsJoin.get(Visit.VISIT_DATE_TIME)));

			visitsCq.orderBy(cb.asc(visitsJoin.get(Visit.VISIT_DATE_TIME)), cb.asc(visitsJoin.get(Visit.CREATION_DATE)));

			List<Object[]> visits = em.createQuery(visitsCq).getResultList();
			Map<String, CaseFollowUpDto> resultMap = resultList.stream().collect(Collectors.toMap(CaseFollowUpDto::getUuid, Function.identity()));

			Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, I18nProperties.getCaption(Captions.inaccessibleValue));

			for (CaseFollowUpDto caseFollowUpDto : resultMap.values()) {
				caseFollowUpDto.initVisitSize(interval + 1);
				pseudonymizer.pseudonymizeDto(CaseFollowUpDto.class, caseFollowUpDto, caseFollowUpDto.getInJurisdiction(), null);
			}

			for (Object[] v : visits) {
				int day = DateHelper.getDaysBetween(start, (Date) v[1]);
				VisitResultDto result = getVisitResult((VisitStatus) v[2], (VisitOrigin) v[3], (Boolean) v[4]);
				resultMap.get(v[0]).getVisitResults()[day - 1] = result;
			}
		}

		return resultList;
	}

	@Override
	@RightsAllowed(UserRight._CASE_EDIT)
	public FollowUpPeriodDto calculateFollowUpUntilDate(CaseDataDto caseDto, boolean ignoreOverwrite) {
		List<SampleDto> samples = Collections.emptyList();
		if (userService.hasRight(UserRight.SAMPLE_VIEW)) {
			samples = sampleFacade.getByCaseUuids(Collections.singletonList(caseDto.getUuid()));
		}
		return CaseLogic.calculateFollowUpUntilDate(
				caseDto,
				CaseLogic.getFollowUpStartDate(caseDto, samples),
				visitFacade.getVisitsByCase(caseDto.toReference()),
				diseaseConfigurationFacade.getCaseFollowUpDuration(caseDto.getDisease()),
				ignoreOverwrite,
				featureConfigurationFacade.isPropertyValueTrue(FeatureType.CASE_FOLLOWUP, FeatureTypeProperty.ALLOW_FREE_FOLLOW_UP_OVERWRITE));
	}

	@Override
	@RightsAllowed(UserRight._CASE_EDIT)
	public void sendMessage(List<String> caseUuids, String subject, String messageContent, MessageType... messageTypes) {
		caseUuids.forEach(uuid -> {
			final Case aCase = service.getByUuid(uuid);
			final Person person = aCase.getPerson();

			try {
				messagingService.sendManualMessage(person, subject, messageContent, messageTypes);
			} catch (NotificationDeliveryFailedException e) {
				logger.error(
						String.format(
								"NotificationDeliveryFailedException when trying to notify person about: %s" + "Failed to send " + e.getMessageType()
										+ " to person with UUID %s.",
								subject,
								person.getUuid()));
			}
		});
	}

	@Override
	public long countCasesWithMissingContactInformation(List<String> caseUuids, MessageType messageType) {

		final AtomicLong totalCount = new AtomicLong();

		IterableHelper.executeBatched(
				caseUuids,
				ModelConstants.PARAMETER_LIMIT,
				batchedUuids -> totalCount.addAndGet(countCasesWithMissingContactInfo(batchedUuids, messageType)));

		return totalCount.get();
	}

	private Long countCasesWithMissingContactInfo(List<String> caseUuids, MessageType messageType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Case> root = cq.from(Case.class);

		final CaseQueryContext caseQueryContext = new CaseQueryContext(cb, cq, root);

		cq.select(cb.count(root));
		Expression<?> contactInformation = messageType == MessageType.EMAIL
				? caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_EMAIL_SUBQUERY)
				: caseQueryContext.getSubqueryExpression(CaseQueryContext.PERSON_PHONE_SUBQUERY);
		cq.where(cb.and(root.get(Case.UUID).in(caseUuids), cb.isNull(contactInformation)));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<ManualMessageLogDto> getMessageLog(String personUuid, MessageType messageType) {
		return manualMessageLogService.getByPersonUuid(personUuid, messageType)
				.stream()
				.map(
						mml -> new ManualMessageLogDto(
								mml.getMessageType(),
								mml.getSentDate(),
								mml.getSendingUser().toReference(),
								mml.getRecipientPerson().toReference()))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getUuidsNotShareableWithExternalReportingTools(List<String> caseUuids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Case> caseRoot = cq.from(Case.class);
		Join<Case, SormasToSormasOriginInfo> originInfoJoin = caseRoot.join(Case.SORMAS_TO_SORMAS_ORIGIN_INFO, JoinType.LEFT);
		Join<Case, SormasToSormasShareInfo> shareInfoJoin = caseRoot.join(Case.SORMAS_TO_SORMAS_SHARES, JoinType.LEFT);

		cq.select(caseRoot.get(Case.UUID));
		cq.where(
				cb.and(
						cb.or(
								cb.isFalse(originInfoJoin.get(SormasToSormasOriginInfo.OWNERSHIP_HANDED_OVER)),
								cb.isTrue(shareInfoJoin.get(SormasToSormasShareInfo.OWNERSHIP_HANDED_OVER)),
								cb.isTrue(caseRoot.get(Case.DONT_SHARE_WITH_REPORTING_TOOL))),
						caseRoot.get(Case.UUID).in(caseUuids)));
		cq.orderBy(cb.asc(caseRoot.get(AbstractDomainObject.CREATION_DATE)));

		return QueryHelper.getResultList(em, cq, null, null);
	}

	/**
	 * Find duplicates based on case and person dto
	 * Conditions:
	 * * same externalId
	 * * same externalToken
	 * * same first name, last name, date of birth, sex (null is considered equal to any sex), disease, reportDate (ignore time), district
	 *
	 * The reportDateThreshold allows to return duplicates where
	 * -reportDateThreshold <= match.reportDate <= reportDateThreshold
	 *
	 * @param casePerson
	 *            - case and person
	 * @param reportDateThreshold
	 *            - the range bounds on match.reportDate
	 * @return list of duplicate cases
	 */
	@Override
	public List<CasePersonDto> getDuplicates(@Valid CasePersonDto casePerson, int reportDateThreshold) {

		CaseDataDto searchCaze = casePerson.getCaze();
		PersonDto searchPerson = casePerson.getPerson();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caseRoot = cq.from(Case.class);
		CaseJoins caseCaseJoins = new CaseJoins(caseRoot);
		Join<Case, Person> person = caseCaseJoins.getPerson();

		cq.multiselect(caseRoot.get(Case.UUID), person.get(Person.UUID));

		Path<String> externalId = caseRoot.get(Case.EXTERNAL_ID);
		Path<String> externalToken = caseRoot.get(Case.EXTERNAL_TOKEN);

		Predicate externalIdPredicate = null;
		if (searchCaze.getExternalID() != null) {
			externalIdPredicate = and(cb, cb.isNotNull(externalId), cb.equal(cb.lower(externalId), searchCaze.getExternalID().toLowerCase().trim()));
		}

		Predicate externalTokenPredicate = null;
		if (searchCaze.getExternalToken() != null) {
			externalTokenPredicate =
					and(cb, cb.isNotNull(externalToken), cb.equal(cb.trim(cb.lower(externalToken)), searchCaze.getExternalToken().toLowerCase().trim()));
		}
		// todo this should use PersonService.buildSimilarityCriteriaFilter
		Predicate combinedPredicate = null;
		if (searchPerson.getFirstName() != null
				&& searchPerson.getLastName() != null
				&& searchCaze.getReportDate() != null
				&& searchCaze.getResponsibleDistrict() != null) {
			Predicate personPredicate = and(
					cb,
					cb.equal(cb.trim(cb.lower(person.get(Person.FIRST_NAME))), searchPerson.getFirstName().toLowerCase().trim()),
					cb.equal(cb.trim(cb.lower(person.get(Person.LAST_NAME))), searchPerson.getLastName().toLowerCase().trim()),
					cb.equal(cb.trim(cb.lower(person.get(Person.OTHER_NAME))), searchPerson.getLastName().toLowerCase().trim()));

			if (searchPerson.getBirthdateDD() != null) {
				personPredicate = and(
						cb,
						personPredicate,
						or(cb, cb.isNull(person.get(Person.BIRTHDATE_DD)), cb.equal(person.get(Person.BIRTHDATE_DD), searchPerson.getBirthdateDD())));
			}

			if (searchPerson.getBirthdateMM() != null) {
				personPredicate = and(
						cb,
						personPredicate,
						or(cb, cb.isNull(person.get(Person.BIRTHDATE_DD)), cb.equal(person.get(Person.BIRTHDATE_MM), searchPerson.getBirthdateMM())));
			}

			if (searchPerson.getBirthdateYYYY() != null) {
				personPredicate = and(
						cb,
						personPredicate,
						or(
								cb,
								cb.isNull(person.get(Person.BIRTHDATE_YYYY)),
								cb.equal(person.get(Person.BIRTHDATE_YYYY), searchPerson.getBirthdateYYYY())));
			}

			if (searchPerson.getSex() != null) {
				personPredicate =
						and(cb, personPredicate, or(cb, cb.isNull(person.get(Person.SEX)), cb.equal(person.get(Person.SEX), searchPerson.getSex())));
			}

			final Predicate reportDatePredicate;
			if (reportDateThreshold == 0) {
				// threshold is zero: we want to get exact matches
				reportDatePredicate = cb.equal(
						cb.function(ExtendedPostgreSQL94Dialect.DATE, Date.class, caseRoot.get(Case.REPORT_DATE)),
						cb.function(ExtendedPostgreSQL94Dialect.DATE, Date.class, cb.literal(searchCaze.getReportDate())));
			} else {
				// threshold is nonzero: apply time range of threshold to the reportDate
				Date reportDate = casePerson.getCaze().getReportDate();
				Date dateBefore = DateHelper.subtractDays(reportDate, reportDateThreshold);
				Date dateAfter = DateHelper.addDays(reportDate, reportDateThreshold);
				reportDatePredicate = cb.between(
						cb.function(ExtendedPostgreSQL94Dialect.DATE, Date.class, caseRoot.get(Case.REPORT_DATE)),
						cb.function(ExtendedPostgreSQL94Dialect.DATE, Date.class, cb.literal(dateBefore)),
						cb.function(ExtendedPostgreSQL94Dialect.DATE, Date.class, cb.literal(dateAfter)));
			}

			Predicate districtPredicate = CriteriaBuilderHelper.or(
					cb,
					cb.equal(caseCaseJoins.getResponsibleDistrict().get(District.UUID), searchCaze.getResponsibleDistrict().getUuid()),
					cb.equal(caseCaseJoins.getDistrict().get(District.UUID), searchCaze.getResponsibleDistrict().getUuid()));
			if (searchCaze.getDistrict() != null) {
				districtPredicate = CriteriaBuilderHelper.or(
						cb,
						districtPredicate,
						cb.equal(caseCaseJoins.getResponsibleDistrict().get(District.UUID), searchCaze.getDistrict().getUuid()),
						cb.equal(caseCaseJoins.getDistrict().get(District.UUID), searchCaze.getDistrict().getUuid()));
			}

			combinedPredicate =
					and(cb, personPredicate, cb.equal(caseRoot.get(Case.DISEASE), searchCaze.getDisease()), reportDatePredicate, districtPredicate);
		}

		Predicate filters = or(cb, externalIdPredicate, externalTokenPredicate, combinedPredicate);
		if (filters == null) {
			return Collections.emptyList();
		}

		cq.where(filters);

		List<Object[]> duplicateUuids = em.createQuery(cq).getResultList();

		return duplicateUuids.stream()
				.map(
						(casePersonUuids) -> new CasePersonDto(
								getCaseDataByUuid((String) casePersonUuids[0]),
								personFacade.getByUuid((String) casePersonUuids[1])))
				.collect(Collectors.toList());
	}

	@Override
	public List<CaseDataDto> getDuplicatesWithPathogenTest(PersonReferenceDto personReferenceDto, PathogenTestDto pathogenTestDto) {
		return null;
	}

	@Override
	public List<CasePersonDto> getDuplicates(@Valid CasePersonDto casePerson) {
		return getDuplicates(casePerson, 0);
	}

	@Override
	public List<CaseDataDto> getByPersonUuids(List<String> personUuids) {
		return toDtos(service.getByPersonUuids(personUuids).stream());
	}

	@Override
	public List<CaseDataDto> getByExternalId(String externalId) {
		return toPseudonymizedDtos(service.getByExternalId(externalId));
	}

	@Override
	@RightsAllowed(UserRight._CASE_EDIT)
	public void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException {
		service.updateExternalData(externalData);
	}

	@RightsAllowed({
			UserRight._VISIT_CREATE,
			UserRight._VISIT_EDIT,
			UserRight._EXTERNAL_VISITS })
	public void updateSymptomsByVisit(Visit visit) {
		CaseDataDto cazeDto = toDto(visit.getCaze());
		SymptomsDto caseSymptoms = cazeDto.getSymptoms();
		SymptomsHelper.updateSymptoms(SymptomsFacadeEjb.toSymptomsDto(visit.getSymptoms()), caseSymptoms);

		caseSave(cazeDto, true, visit.getCaze(), cazeDto, true, true);
	}

	@Override
	protected String getDeleteReferenceField(DeletionReference deletionReference) {
		if (deletionReference == DeletionReference.REPORT) {
			return Case.REPORT_DATE;
		}

		return super.getDeleteReferenceField(deletionReference);
	}
//save methods
//@Override
@RightsAllowed({
		UserRight._CASE_CREATE,
		UserRight._CASE_EDIT,
		UserRight._EXTERNAL_VISITS})
public CaseDataDto save(@Valid @NotNull CaseDataDto dto, boolean systemSave) throws ValidationRuntimeException {
	return save(dto, true, true, true, systemSave);
}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT})
	public CoreAndPersonDto<CaseDataDto> save(@Valid @NotNull CoreAndPersonDto<CaseDataDto> coreAndPersonDto) throws ValidationRuntimeException {
		CaseDataDto caseDto = coreAndPersonDto.getCoreData();
		CoreAndPersonDto savedCoreAndPersonDto = new CoreAndPersonDto();
		if (coreAndPersonDto.getPerson() != null) {
			PersonDto newlyCreatedPersonDto = personFacade.savePerson(coreAndPersonDto.getPerson());
			caseDto.setPerson(newlyCreatedPersonDto.toReference());
			savedCoreAndPersonDto.setPerson(newlyCreatedPersonDto);
		}
		CaseDataDto savedCaseData = save(caseDto, true, true, true, false);
		savedCoreAndPersonDto.setCoreData(savedCaseData);
		return savedCoreAndPersonDto;
	}

	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT})
	public CaseDataDto save(@Valid CaseDataDto dto, boolean handleChanges, boolean checkChangeDate, boolean internal, boolean systemSave)
			throws ValidationRuntimeException {

		Case existingCase = service.getByUuid(dto.getUuid());

		if (!systemSave && internal && existingCase != null && !service.getEditPermissionType(existingCase).equals(EditPermissionType.ALLOWED)) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.errorCaseNotEditable));
		}
		CaseDataDto existingCaseDto = handleChanges ? toDto(existingCase) : null;
		return caseSave(dto, handleChanges, existingCase, existingCaseDto, checkChangeDate, internal);
	}

	@Override
	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT})
	public CaseDataDto save(@Valid @NotNull CaseDataDto dto) throws ValidationRuntimeException {
		return save(dto, true, true, true, false);
	}


	@RightsAllowed({
			UserRight._CASE_CREATE,
			UserRight._CASE_EDIT })
	public CaseDataDto save(@Valid CaseDataDto dto, boolean handleChanges, boolean checkChangeDate, boolean internal)
			throws ValidationRuntimeException {

		Case existingCase = service.getByUuid(dto.getUuid(), true);
		FacadeHelper.checkCreateAndEditRights(existingCase, userService, UserRight.CASE_CREATE, UserRight.CASE_EDIT);

		if (existingCase != null && internal) {
			EditPermissionType editPermission = service.getEditPermissionType(existingCase);
			if (editPermission == EditPermissionType.OUTSIDE_JURISDICTION) {
				throw new AccessDeniedException(I18nProperties.getString(Strings.errorCaseNotEditableOutsideJurisdiction));
			} else if (editPermission != EditPermissionType.ALLOWED) {
				throw new AccessDeniedException(I18nProperties.getString(Strings.errorCaseNotEditable));
			}
		}

		return caseSave(dto, handleChanges, existingCase, toDto(existingCase), checkChangeDate, internal);
	}


	public Map<Disease, District> getLastReportedDistrictByDisease(
			CaseCriteria caseCriteria,
			boolean excludeSharedCases,
			boolean excludeCasesFromContacts) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<Case> caze = cq.from(Case.class);
		Join<Case, District> districtJoin = caze.join(Case.DISTRICT, JoinType.LEFT);

		Predicate filter = caseService.createUserFilter(
				cb,
				cq,
				caze,
				new CaseUserFilterCriteria().excludeSharedCases(excludeSharedCases).excludeCasesFromContacts(excludeCasesFromContacts));

		filter = AbstractAdoService.and(cb, filter, caseService.createCriteriaFilter(caseCriteria, cb, cq, caze));

		if (filter != null) {
			cq.where(filter);
		}

		Expression<Number> maxReportDate = cb.max(caze.get(Case.REPORT_DATE));
		cq.multiselect(caze.get(Case.DISEASE), districtJoin, maxReportDate);
		cq.groupBy(caze.get(Case.DISEASE), districtJoin);
		cq.orderBy(cb.desc(maxReportDate));

		List<Object[]> results = em.createQuery(cq).getResultList();

		Map<Disease, District> resultMap = new HashMap<>();
		for (Object[] e : results) {
			Disease disease = (Disease) e[0];
			if (!resultMap.containsKey(disease)) {
				District district = (District) e[1];
				resultMap.put(disease, district);
			}
		}

		return resultMap;
	}

	@Override
	@RightsAllowed(UserRight._CASE_MERGE)
	public void merge(String leadUuid, String otherUuid) {
		mergeCase(getCaseDataWithoutPseudonyimization(leadUuid), getCaseDataWithoutPseudonyimization(otherUuid), false);
	}
	@LocalBean
	@Stateless
	public static class CaseFacadeEjbLocal extends CaseFacadeEjb {
	public CaseFacadeEjbLocal() {}

		@Inject
		public CaseFacadeEjbLocal(CaseService service) {
			super(service);
		}
	}

	private Map<Long, UserReference> getCaseUsersForDetailedExport(List<CaseExportDetailedSampleDto> resultList, ExportConfigurationDto exportConfiguration) {
		Map<Long, UserReference> caseUsers = Collections.emptyMap();
		if (exportConfiguration == null
				|| exportConfiguration.getProperties().contains(CaseDataDto.REPORTING_USER)
				|| exportConfiguration.getProperties().contains(CaseDataDto.FOLLOW_UP_STATUS_CHANGE_USER)) {
			Set<Long> userIds = resultList.stream()
					.map((c -> Arrays.asList(c.getReportingUserId(), c.getFollowUpStatusChangeUserId())))
					.flatMap(Collection::stream)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			caseUsers = userService.getUserReferencesByIds(userIds).stream().collect(Collectors.toMap(UserReference::getId, Function.identity()));
		}

		return caseUsers;
	}


	private static String safeTestResultName(PathogenTest pathogenTest) {
		return pathogenTest.getTestResult() != null ? pathogenTest.getTestResult().name() : null;
	}

	public void mapAfpTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setAfpAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setAfpAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setAfpRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setAfpCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setAfpHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setAfpIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAfpIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAfpIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAfpIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setAfpIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setAfpIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setAfpMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setAfpNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setAfpPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setAfpGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setAfpLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setAfpCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setAfpSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setAfpDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setAfpOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAfpOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	//    Cholera
	public void mapCholeraTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setCholeraAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setCholeraAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setCholeraRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setCholeraCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setCholeraHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setCholeraIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCholeraIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCholeraIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCholeraIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setCholeraIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setCholeraIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setCholeraMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setCholeraNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setCholeraPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setCholeraGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setCholeraLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setCholeraCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setCholeraSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setCholeraDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setCholeraOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCholeraOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	//    CONGENITAL_RUBELLA
	public void mapCongenitalRubellaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setCongenitalRubellaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setCongenitalRubellaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setCongenitalRubellaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setCongenitalRubellaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setCongenitalRubellaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setCongenitalRubellaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setCongenitalRubellaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setCongenitalRubellaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setCongenitalRubellaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setCongenitalRubellaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setCongenitalRubellaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCongenitalRubellaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	//    CSM
	public void mapCsmTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setCsmAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setCsmAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setCsmRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setCsmCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setCsmHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setCsmIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCsmIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCsmIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCsmIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setCsmIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setCsmIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setCsmMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setCsmNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setCsmPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setCsmGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setCsmLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setCsmCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setCsmSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setCsmDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setCsmOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCsmOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	//    DENGUE
	public void mapDengueTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setDengueAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setDengueAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setDengueRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setDengueCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setDengueHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setDengueIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDengueIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDengueIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDengueIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setDengueIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setDengueIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setDengueMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setDengueNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setDenguePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDenguePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setDengueGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setDengueLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setDengueCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setDengueSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setDengueDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setDengueOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDengueOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	public void mapEvdTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setEvdAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setEvdAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setEvdRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setEvdCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setEvdHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setEvdIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEvdIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEvdIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEvdIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setEvdIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setEvdIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setEvdMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setEvdNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setEvdPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setEvdGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setEvdLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setEvdCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setEvdSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setEvdDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setEvdOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEvdOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	public void mapGuineaWormTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setGuineaWormAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setGuineaWormRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setGuineaWormCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setGuineaWormHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setGuineaWormIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setGuineaWormIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setGuineaWormIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setGuineaWormIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setGuineaWormIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setGuineaWormIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setGuineaWormMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setGuineaWormNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setGuineaWormPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setGuineaWormGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setGuineaWormLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setGuineaWormCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setGuineaWormSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setGuineaWormDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setGuineaWormOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setGuineaWormOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}


	public void mapLassaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setLassaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setLassaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setLassaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setLassaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setLassaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setLassaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLassaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLassaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLassaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setLassaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setLassaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setLassaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setLassaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setLassaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setLassaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setLassaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setLassaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setLassaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setLassaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setLassaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLassaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}


	public void mapMeaslesTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setMeaslesAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setMeaslesAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setMeaslesRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setMeaslesCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setMeaslesHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setMeaslesIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMeaslesIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMeaslesIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMeaslesIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setMeaslesIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setMeaslesIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setMeaslesMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setMeaslesNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setMeaslesPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setMeaslesGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setMeaslesLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setMeaslesCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setMeaslesSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setMeaslesDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setMeaslesOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMeaslesOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapMonkeyPoxTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setMonkeypoxAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setMonkeypoxRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setMonkeypoxCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setMonkeypoxHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setMonkeypoxIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMonkeypoxIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMonkeypoxIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMonkeypoxIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setMonkeypoxIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setMonkeypoxIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setMonkeypoxMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setMonkeypoxNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setMonkeypoxPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setMonkeypoxGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setMonkeypoxLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setMonkeypoxCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setMonkeypoxSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setMonkeypoxDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setMonkeypoxOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMonkeypoxOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapNewInfluenzaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setNewInfluenzaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setNewInfluenzaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setNewInfluenzaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setNewInfluenzaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setNewInfluenzaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNewInfluenzaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNewInfluenzaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNewInfluenzaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setNewInfluenzaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setNewInfluenzaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setNewInfluenzaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setNewInfluenzaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setNewInfluenzaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setNewInfluenzaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setNewInfluenzaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setNewInfluenzaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setNewInfluenzaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setNewInfluenzaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setNewInfluenzaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNewInfluenzaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPlagueTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPlagueRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPlagueCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPlagueHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPlagueIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPlagueIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPlagueMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPlaguePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlaguePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPlagueGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPlagueLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPlagueSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPlagueDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPlagueOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	public void mapPolioTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPlagueRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPlagueCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPlagueHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPlagueIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPlagueIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPlagueMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPlaguePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlaguePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPlagueGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPlagueLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPlagueCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPlagueSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPlagueDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPlagueOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPlagueOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapUnspecifiedVhfTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setUnspecifiedVhfOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUnspecifiedVhfOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapYellowFeverTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setYellowFeverAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setYellowFeverRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setYellowFeverCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setYellowFeverHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setYellowFeverIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYellowFeverIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYellowFeverIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYellowFeverIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setYellowFeverIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setYellowFeverIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setYellowFeverMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setYellowFeverNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setYellowFeverPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setYellowFeverGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setYellowFeverLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setYellowFeverCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setYellowFeverSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setYellowFeverDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setYellowFeverOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYellowFeverOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapRabiesTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setRabiesAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setRabiesAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setRabiesRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setRabiesCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setRabiesHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setRabiesIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRabiesIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRabiesIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRabiesIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setRabiesIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setRabiesIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setRabiesMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setRabiesNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setRabiesPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setRabiesGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setRabiesLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setRabiesCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setRabiesSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setRabiesDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setRabiesOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRabiesOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapAnthraxTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setAnthraxAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setAnthraxAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setAnthraxRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setAnthraxCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setAnthraxHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setAnthraxIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAnthraxIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAnthraxIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAnthraxIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setAnthraxIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setAnthraxIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setAnthraxMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setAnthraxNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setAnthraxPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setAnthraxGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setAnthraxLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setAnthraxCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setAnthraxSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setAnthraxDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setAnthraxOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAnthraxOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapCoronavirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setCoronavirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setCoronavirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setCoronavirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setCoronavirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setCoronavirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCoronavirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCoronavirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setCoronavirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setCoronavirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setCoronavirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setCoronavirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setCoronavirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setCoronavirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setCoronavirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setCoronavirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setCoronavirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setCoronavirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setCoronavirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setCoronavirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setCoronavirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPneumoniaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPneumoniaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPneumoniaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPneumoniaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPneumoniaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPneumoniaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPneumoniaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPneumoniaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPneumoniaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPneumoniaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPneumoniaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPneumoniaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPneumoniaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPneumoniaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPneumoniaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPneumoniaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPneumoniaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPneumoniaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPneumoniaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPneumoniaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPneumoniaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapMalariaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setMalariaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setMalariaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setMalariaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setMalariaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setMalariaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setMalariaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMalariaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMalariaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMalariaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setMalariaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setMalariaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setMalariaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setMalariaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setMalariaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setMalariaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setMalariaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setMalariaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setMalariaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setMalariaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setMalariaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMalariaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapTyphoidFeverTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setTyphoidFeverAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setTyphoidFeverRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setTyphoidFeverCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setTyphoidFeverHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setTyphoidFeverIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTyphoidFeverIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTyphoidFeverIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTyphoidFeverIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setTyphoidFeverIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setTyphoidFeverIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setTyphoidFeverMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setTyphoidFeverNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setTyphoidFeverPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setTyphoidFeverGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setTyphoidFeverLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setTyphoidFeverCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setTyphoidFeverSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setTyphoidFeverDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setTyphoidFeverOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTyphoidFeverOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapAcuteViralHepatitisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAcuteViralHepatitisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapNonNeonatalTetanusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNonNeonatalTetanusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapHivTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setHivAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setHivAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setHivRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setHivCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setHivHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setHivIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setHivIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setHivIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setHivIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setHivIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setHivIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setHivMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setHivNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setHivPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setHivGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setHivLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setHivCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setHivSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setHivDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setHivOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setHivOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapSchistosomiasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setSchistosomiasisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setSchistosomiasisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setSchistosomiasisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setSchistosomiasisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setSchistosomiasisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSchistosomiasisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSchistosomiasisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSchistosomiasisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setSchistosomiasisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setSchistosomiasisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setSchistosomiasisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setSchistosomiasisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setSchistosomiasisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setSchistosomiasisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setSchistosomiasisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setSchistosomiasisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setSchistosomiasisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setSchistosomiasisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setSchistosomiasisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSchistosomiasisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapSoilTransmittedHelminthsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapTrypanosomiasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setTrypanosomiasisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setTrypanosomiasisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setTrypanosomiasisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setTrypanosomiasisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setTrypanosomiasisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setTrypanosomiasisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setTrypanosomiasisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setTrypanosomiasisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setTrypanosomiasisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setTrypanosomiasisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setTrypanosomiasisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrypanosomiasisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapDiarrheaDehydrationTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaDehydrationOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapDiarrheaBloodTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaBloodAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setDiarrheaBloodRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setDiarrheaBloodCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setDiarrheaBloodNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setDiarrheaBloodPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setDiarrheaBloodGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setDiarrheaBloodLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setDiarrheaBloodCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setDiarrheaBloodSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setDiarrheaBloodDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setDiarrheaBloodOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiarrheaBloodOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapSnakeBiteTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setSnakeBiteAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setSnakeBiteRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setSnakeBiteCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setSnakeBiteHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setSnakeBiteIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSnakeBiteIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSnakeBiteIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setSnakeBiteIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setSnakeBiteIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setSnakeBiteIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setSnakeBiteMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setSnakeBiteNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setSnakeBitePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBitePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setSnakeBiteGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setSnakeBiteLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setSnakeBiteCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setSnakeBiteSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setSnakeBiteDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setSnakeBiteOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setSnakeBiteOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapRubellaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setRubellaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setRubellaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setRubellaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setRubellaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setRubellaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setRubellaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRubellaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRubellaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRubellaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setRubellaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setRubellaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setRubellaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setRubellaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setRubellaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setRubellaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setRubellaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setRubellaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setRubellaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setRubellaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setRubellaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRubellaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	public void mapTuberculosisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setTuberculosisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setTuberculosisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setTuberculosisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setTuberculosisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setTuberculosisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTuberculosisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTuberculosisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTuberculosisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setTuberculosisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setTuberculosisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setTuberculosisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setTuberculosisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setTuberculosisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setTuberculosisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setTuberculosisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setTuberculosisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setTuberculosisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setTuberculosisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setTuberculosisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTuberculosisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapLeprosyTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setLeprosyAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setLeprosyAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setLeprosyRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setLeprosyCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setLeprosyHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setLeprosyIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLeprosyIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLeprosyIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLeprosyIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setLeprosyIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setLeprosyIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setLeprosyMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setLeprosyNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setLeprosyPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setLeprosyGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setLeprosyLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setLeprosyCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setLeprosySequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosySequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setLeprosyDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setLeprosyOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLeprosyOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapLymphaticFilariasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setLymphaticFilariasisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setLymphaticFilariasisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapBuruliUlcerTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setBuruliUlcerAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setBuruliUlcerRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setBuruliUlcerCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setBuruliUlcerHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setBuruliUlcerIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setBuruliUlcerIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setBuruliUlcerIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setBuruliUlcerIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setBuruliUlcerIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setBuruliUlcerIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setBuruliUlcerMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setBuruliUlcerNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setBuruliUlcerPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setBuruliUlcerGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setBuruliUlcerLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setBuruliUlcerCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setBuruliUlcerSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setBuruliUlcerDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setBuruliUlcerOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setBuruliUlcerOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPertussisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPertussisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPertussisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPertussisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPertussisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPertussisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPertussisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPertussisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPertussisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPertussisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPertussisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPertussisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPertussisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPertussisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPertussisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPertussisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPertussisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPertussisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPertussisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPertussisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPertussisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPertussisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapNeonatalTetanusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setNeonatalTetanusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setNeonatalTetanusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setNeonatalTetanusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setNeonatalTetanusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setNeonatalTetanusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setNeonatalTetanusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setNeonatalTetanusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setNeonatalTetanusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setNeonatalTetanusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setNeonatalTetanusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setNeonatalTetanusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setNeonatalTetanusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapOnchocerciasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setOnchocerciasisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setOnchocerciasisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setOnchocerciasisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setOnchocerciasisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setOnchocerciasisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOnchocerciasisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOnchocerciasisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOnchocerciasisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setOnchocerciasisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setOnchocerciasisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setOnchocerciasisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setOnchocerciasisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setOnchocerciasisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setOnchocerciasisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setOnchocerciasisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setOnchocerciasisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setOnchocerciasisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setOnchocerciasisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setOnchocerciasisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOnchocerciasisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapDiphtheriaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setDiphteriaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setDiphteriaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setDiphteriaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setDiphteriaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setDiphteriaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiphteriaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiphteriaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiphteriaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setDiphteriaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setDiphteriaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setDiphteriaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setDiphteriaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setDiphteriaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setDiphteriaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setDiphteriaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setDiphteriaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setDiphteriaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setDiphteriaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setDiphteriaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setDiphteriaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapTrachomaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setTrachomaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setTrachomaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setTrachomaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setTrachomaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setTrachomaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setTrachomaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrachomaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrachomaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrachomaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setTrachomaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setTrachomaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setTrachomaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setTrachomaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setTrachomaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setTrachomaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setTrachomaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setTrachomaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setTrachomaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setTrachomaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setTrachomaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setTrachomaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapYawsEndemicSyphilisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setYawsEndemicSyphilisOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapMaternalDeathsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setMaternalDeathsAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setMaternalDeathsRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setMaternalDeathsCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setMaternalDeathsHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setMaternalDeathsIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMaternalDeathsIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMaternalDeathsIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setMaternalDeathsIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setMaternalDeathsIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setMaternalDeathsIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setMaternalDeathsMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setMaternalDeathsNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setMaternalDeathsPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setMaternalDeathsGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setMaternalDeathsLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setMaternalDeathsCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setMaternalDeathsSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setMaternalDeathsDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setMaternalDeathsOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setMaternalDeathsOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPerinatalDeathsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPerinatalDeathsAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPerinatalDeathsRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPerinatalDeathsCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPerinatalDeathsNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPerinatalDeathsPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPerinatalDeathsGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPerinatalDeathsLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPerinatalDeathsCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPerinatalDeathsSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPerinatalDeathsDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPerinatalDeathsOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPerinatalDeathsOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapInfluenzaATestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaAAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setInfluenzaARapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaARapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setInfluenzaACulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaACultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setInfluenzaAHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setInfluenzaAIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaAIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaAIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaAIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setInfluenzaAIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaAIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setInfluenzaAMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setInfluenzaANeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaANeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setInfluenzaAPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setInfluenzaAGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setInfluenzaALatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaALatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaACqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaACqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setInfluenzaASequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaASequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setInfluenzaADnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaADnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setInfluenzaAOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaAOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapInfluenzaBTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaBAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setInfluenzaBRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setInfluenzaBCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setInfluenzaBHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setInfluenzaBIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaBIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaBIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaBIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setInfluenzaBIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setInfluenzaBIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setInfluenzaBMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setInfluenzaBNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setInfluenzaBPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setInfluenzaBGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setInfluenzaBLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setInfluenzaBCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setInfluenzaBSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setInfluenzaBDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setInfluenzaBOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setInfluenzaBOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void maphMetapneumovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.sethMetapneumovirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.sethMetapneumovirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.sethMetapneumovirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.sethMetapneumovirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.sethMetapneumovirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.sethMetapneumovirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.sethMetapneumovirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.sethMetapneumovirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.sethMetapneumovirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.sethMetapneumovirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.sethMetapneumovirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.sethMetapneumovirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapRespiratorySyncytialVirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapParainfluenza1_4TestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setParainfluenzaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setParainfluenzaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setParainfluenzaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setParainfluenzaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setParainfluenzaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setParainfluenzaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setParainfluenzaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setParainfluenzaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setParainfluenzaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setParainfluenzaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setParainfluenzaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setParainfluenzaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setParainfluenzaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setParainfluenzaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setParainfluenzaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setParainfluenzaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setParainfluenzaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setParainfluenzaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setParainfluenzaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setParainfluenzaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapAdenovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setAdenovirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setAdenovirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setAdenovirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setAdenovirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setAdenovirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAdenovirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAdenovirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAdenovirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setAdenovirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setAdenovirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setAdenovirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setAdenovirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setAdenovirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setAdenovirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setAdenovirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setAdenovirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setAdenovirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setAdenovirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setAdenovirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAdenovirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapRhinovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setRhinovirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setRhinovirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setRhinovirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setRhinovirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setRhinovirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRhinovirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRhinovirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setRhinovirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setRhinovirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setRhinovirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setRhinovirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setRhinovirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setRhinovirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setRhinovirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setRhinovirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setRhinovirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setRhinovirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setRhinovirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setRhinovirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setRhinovirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapEnterovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setEnterovirusAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setEnterovirusRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setEnterovirusCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setEnterovirusHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setEnterovirusIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEnterovirusIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEnterovirusIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setEnterovirusIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setEnterovirusIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setEnterovirusIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setEnterovirusMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setEnterovirusNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setEnterovirusPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setEnterovirusGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setEnterovirusLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setEnterovirusCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setEnterovirusSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setEnterovirusDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setEnterovirusOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setEnterovirusOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapmPneumoniaeTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setmPneumoniaeAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setmPneumoniaeRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setmPneumoniaeCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setmPneumoniaeHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setmPneumoniaeIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setmPneumoniaeIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setmPneumoniaeIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setmPneumoniaeIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setmPneumoniaeIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setmPneumoniaeIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setmPneumoniaeMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setmPneumoniaeNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setmPneumoniaePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setmPneumoniaeGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setmPneumoniaeLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setmPneumoniaeCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setmPneumoniaeSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setmPneumoniaeDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setmPneumoniaeOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setmPneumoniaeOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapcPneumoniaeTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setcPneumoniaeAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setcPneumoniaeRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setcPneumoniaeCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setcPneumoniaeHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setcPneumoniaeIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setcPneumoniaeIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setcPneumoniaeIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setcPneumoniaeIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setcPneumoniaeIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setcPneumoniaeIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setcPneumoniaeMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setcPneumoniaeNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setcPneumoniaePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setcPneumoniaeGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setcPneumoniaeLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setcPneumoniaeCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setcPneumoniaeSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setcPneumoniaeDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setcPneumoniaeOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setcPneumoniaeOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapAriTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setAriAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setAriAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setAriRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setAriCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setAriHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setAriIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAriIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAriIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setAriIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setAriIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setAriIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setAriMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setAriNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setAriPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setAriGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setAriLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setAriCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setAriSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setAriDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setAriOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setAriOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapChikungunyaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setChikungunyaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setChikungunyaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setChikungunyaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setChikungunyaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setChikungunyaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setChikungunyaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setChikungunyaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setChikungunyaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setChikungunyaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setChikungunyaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setChikungunyaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setChikungunyaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setChikungunyaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setChikungunyaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setChikungunyaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setChikungunyaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setChikungunyaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setChikungunyaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setChikungunyaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setChikungunyaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPostImmunizationAdverseEventsMildTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapPostImmunizationAdverseEventsSevereTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSeverePcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSeverePcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapFhaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setFhaAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setFhaAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setFhaRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setFhaCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setFhaHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setFhaIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setFhaIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setFhaIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setFhaIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setFhaIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setFhaIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setFhaMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setFhaNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setFhaPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setFhaGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setFhaLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setFhaCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setFhaSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setFhaDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setFhaOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setFhaOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
	public void mapOtherTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setOtherAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setOtherAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setOtherRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setOtherCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setOtherHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setOtherIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOtherIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOtherIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setOtherIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setOtherIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setOtherIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setOtherMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setOtherNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setOtherPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setOtherGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setOtherLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setOtherCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setOtherSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setOtherDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setOtherOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setOtherOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}

	public void mapUndefinedTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
		if (pathogenTest.getTestType() == null) {
			return;
		}
		switch (pathogenTest.getTestType().name()) {
			case "ANTIBODY_DETECTION":
				embeddedDetailedSampleExportDto.setUndefinedAntibodyDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "ANTIGEN_DETECTION":
				embeddedDetailedSampleExportDto.setUndefinedAntigenDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedAntibodyDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "RAPID_TEST":
				embeddedDetailedSampleExportDto.setUndefinedRapidTest(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedRapidTestDetails(pathogenTest.getTestResultText());
				break;
			case "CULTURE":
				embeddedDetailedSampleExportDto.setUndefinedCulture(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedCultureDetails(pathogenTest.getTestResultText());
				break;
			case "HISTOPATHOLOGY":
				embeddedDetailedSampleExportDto.setUndefinedHistopathology(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedHistopathologyDetails(pathogenTest.getTestResultText());
				break;
			case "ISOLATION":
				embeddedDetailedSampleExportDto.setUndefinedIsolation(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIsolationDetails(pathogenTest.getTestResultText());
				break;
			case "IGM_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUndefinedIgmSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGG_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUndefinedIggSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIggSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "IGA_SERUM_ANTIBODY":
				embeddedDetailedSampleExportDto.setUndefinedIgaSerumAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "INCUBATION_TIME":
				embeddedDetailedSampleExportDto.setUndefinedIncubationTime(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIncubationTimeDetails(pathogenTest.getTestResultText());
				break;
			case "INDIRECT_FLUORESCENT_ANTIBODY":
				embeddedDetailedSampleExportDto.setUndefinedIndirectFluorescentAntibody(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
				break;
			case "MICROSCOPY":
				embeddedDetailedSampleExportDto.setUndefinedMicroscopy(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedMicroscopyDetails(pathogenTest.getTestResultText());
				break;
			case "NEUTRALIZING_ANTIBODIES":
				embeddedDetailedSampleExportDto.setUndefinedNeutralizingAntibodies(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
				break;
			case "PCR_RT_PCR":
				embeddedDetailedSampleExportDto.setUndefinedPcrRtPcr(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedPcrRtPcrDetails(pathogenTest.getTestResultText());
				break;
			case "GRAM_STAIN":
				embeddedDetailedSampleExportDto.setUndefinedGramStain(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedGramStainDetails(pathogenTest.getTestResultText());
				break;
			case "LATEX_AGGLUTINATION":
				embeddedDetailedSampleExportDto.setUndefinedLatexAgglutination(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedLatexAgglutinationDetails(pathogenTest.getTestResultText());
				break;
			case "CQ_VALUE_DETECTION":
				embeddedDetailedSampleExportDto.setUndefinedCqValueDetection(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedCqValueDetectionDetails(pathogenTest.getTestResultText());
				break;
			case "SEQUENCING":
				embeddedDetailedSampleExportDto.setUndefinedSequencing(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedSequencingDetails(pathogenTest.getTestResultText());
				break;
			case "DNA_MICROARRAY":
				embeddedDetailedSampleExportDto.setUndefinedDnaMicroarray(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedDnaMicroarrayDetails(pathogenTest.getTestResultText());
				break;
			case "OTHER":
				embeddedDetailedSampleExportDto.setUndefinedOther(safeTestResultName(pathogenTest));
				embeddedDetailedSampleExportDto.setUndefinedOtherDetails(pathogenTest.getTestResultText());
				break;
			default:
				break;
		}
	}
}
