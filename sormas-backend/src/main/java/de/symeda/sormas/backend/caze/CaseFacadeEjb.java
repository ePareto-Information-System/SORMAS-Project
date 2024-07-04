/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.backend.caze;

import static de.symeda.sormas.backend.common.CriteriaBuilderHelper.and;
import static de.symeda.sormas.backend.common.CriteriaBuilderHelper.or;
import static de.symeda.sormas.backend.visit.VisitLogic.getVisitResult;

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

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.caze.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import de.symeda.sormas.api.CaseMeasure;
import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.DiseaseHelper;
import de.symeda.sormas.api.EditPermissionType;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.VisitOrigin;
import de.symeda.sormas.api.caze.maternalhistory.MaternalHistoryDto;
import de.symeda.sormas.api.caze.porthealthinfo.PortHealthInfoDto;
import de.symeda.sormas.api.caze.surveillancereport.SurveillanceReportDto;
import de.symeda.sormas.api.clinicalcourse.ClinicalCourseDto;
import de.symeda.sormas.api.clinicalcourse.ClinicalCourseReferenceDto;
import de.symeda.sormas.api.clinicalcourse.ClinicalVisitCriteria;
import de.symeda.sormas.api.clinicalcourse.ClinicalVisitDto;
import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.common.CoreEntityType;
import de.symeda.sormas.api.common.DeletionDetails;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.contact.ContactCriteria;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.dashboard.DashboardCaseDto;
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
import de.symeda.sormas.api.followup.FollowUpPeriodDto;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.immunization.MeansOfImmunization;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.infrastructure.InfrastructureHelper;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.location.LocationReferenceDto;
import de.symeda.sormas.api.messaging.ManualMessageLogDto;
import de.symeda.sormas.api.messaging.MessageType;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.CauseOfDeath;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.person.PresentCondition;
import de.symeda.sormas.api.sample.AdditionalTestDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleCriteria;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sormastosormas.ShareTreeCriteria;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.symptoms.SymptomsHelper;
import de.symeda.sormas.api.task.TaskContext;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.api.task.TaskHelper;
import de.symeda.sormas.api.task.TaskPriority;
import de.symeda.sormas.api.task.TaskStatus;
import de.symeda.sormas.api.task.TaskType;
import de.symeda.sormas.api.therapy.PrescriptionCriteria;
import de.symeda.sormas.api.therapy.PrescriptionDto;
import de.symeda.sormas.api.therapy.TherapyDto;
import de.symeda.sormas.api.therapy.TherapyReferenceDto;
import de.symeda.sormas.api.therapy.TreatmentCriteria;
import de.symeda.sormas.api.therapy.TreatmentDto;
import de.symeda.sormas.api.user.NotificationType;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.AccessDeniedException;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DataHelper.Pair;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.InfoProvider;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.checkers.UserRightFieldAccessChecker;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.visit.VisitDto;
import de.symeda.sormas.api.visit.VisitResultDto;
import de.symeda.sormas.api.visit.VisitStatus;
import de.symeda.sormas.backend.caze.classification.CaseClassificationFacadeEjb.CaseClassificationFacadeEjbLocal;
import de.symeda.sormas.backend.caze.maternalhistory.MaternalHistoryFacadeEjb;
import de.symeda.sormas.backend.caze.maternalhistory.MaternalHistoryFacadeEjb.MaternalHistoryFacadeEjbLocal;
import de.symeda.sormas.backend.caze.porthealthinfo.PortHealthInfoFacadeEjb;
import de.symeda.sormas.backend.caze.porthealthinfo.PortHealthInfoFacadeEjb.PortHealthInfoFacadeEjbLocal;
import de.symeda.sormas.backend.caze.surveillancereport.SurveillanceReport;
import de.symeda.sormas.backend.caze.surveillancereport.SurveillanceReportFacadeEjb;
import de.symeda.sormas.backend.caze.surveillancereport.SurveillanceReportService;
import de.symeda.sormas.backend.clinicalcourse.ClinicalCourse;
import de.symeda.sormas.backend.clinicalcourse.ClinicalCourseFacadeEjb;
import de.symeda.sormas.backend.clinicalcourse.ClinicalCourseFacadeEjb.ClinicalCourseFacadeEjbLocal;
import de.symeda.sormas.backend.clinicalcourse.ClinicalVisit;
import de.symeda.sormas.backend.clinicalcourse.ClinicalVisitFacadeEjb;
import de.symeda.sormas.backend.clinicalcourse.ClinicalVisitFacadeEjb.ClinicalVisitFacadeEjbLocal;
import de.symeda.sormas.backend.clinicalcourse.ClinicalVisitService;
import de.symeda.sormas.backend.clinicalcourse.HealthConditions;
import de.symeda.sormas.backend.clinicalcourse.HealthConditionsMapper;
import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.common.AbstractCoreFacadeEjb;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.common.NotificationService;
import de.symeda.sormas.backend.common.messaging.ManualMessageLogService;
import de.symeda.sormas.backend.common.messaging.MessageContents;
import de.symeda.sormas.backend.common.messaging.MessageSubject;
import de.symeda.sormas.backend.common.messaging.MessagingService;
import de.symeda.sormas.backend.common.messaging.NotificationDeliveryFailedException;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactFacadeEjb.ContactFacadeEjbLocal;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.contact.VisitSummaryExportDetails;
import de.symeda.sormas.backend.disease.DiseaseConfigurationFacadeEjb.DiseaseConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.document.Document;
import de.symeda.sormas.backend.document.DocumentService;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.epidata.EpiDataFacadeEjb;
import de.symeda.sormas.backend.epidata.EpiDataFacadeEjb.EpiDataFacadeEjbLocal;
import de.symeda.sormas.backend.epidata.EpiDataService;
import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.event.EventParticipantService;
import de.symeda.sormas.backend.event.EventService;
import de.symeda.sormas.backend.event.EventSummaryDetails;
import de.symeda.sormas.backend.exposure.Exposure;
import de.symeda.sormas.backend.exposure.ExposureService;
import de.symeda.sormas.backend.externaljournal.ExternalJournalService;
import de.symeda.sormas.backend.externalsurveillancetool.ExternalSurveillanceToolGatewayFacadeEjb.ExternalSurveillanceToolGatewayFacadeEjbLocal;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal;
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
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.outbreak.Outbreak;
import de.symeda.sormas.backend.outbreak.OutbreakService;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.person.PersonFacadeEjb;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonService;
import de.symeda.sormas.backend.sample.AdditionalTest;
import de.symeda.sormas.backend.sample.AdditionalTestFacadeEjb.AdditionalTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTest;
import de.symeda.sormas.backend.sample.PathogenTestFacadeEjb.PathogenTestFacadeEjbLocal;
import de.symeda.sormas.backend.sample.PathogenTestService;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.sample.SampleFacadeEjb;
import de.symeda.sormas.backend.sample.SampleFacadeEjb.SampleFacadeEjbLocal;
import de.symeda.sormas.backend.sample.SampleService;
import de.symeda.sormas.backend.share.ExternalShareInfoCountAndLatestDate;
import de.symeda.sormas.backend.share.ExternalShareInfoService;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeEjb.SormasToSormasFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.entities.caze.SormasToSormasCaseFacadeEjb.SormasToSormasCaseFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoFacadeEjb;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoFacadeEjb.SormasToSormasOriginInfoFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.share.shareinfo.ShareInfoHelper;
import de.symeda.sormas.backend.sormastosormas.share.shareinfo.SormasToSormasShareInfo;
import de.symeda.sormas.backend.symptoms.Symptoms;
import de.symeda.sormas.backend.symptoms.SymptomsFacadeEjb;
import de.symeda.sormas.backend.symptoms.SymptomsFacadeEjb.SymptomsFacadeEjbLocal;
import de.symeda.sormas.backend.task.Task;
import de.symeda.sormas.backend.task.TaskService;
import de.symeda.sormas.backend.therapy.Prescription;
import de.symeda.sormas.backend.therapy.PrescriptionFacadeEjb;
import de.symeda.sormas.backend.therapy.PrescriptionFacadeEjb.PrescriptionFacadeEjbLocal;
import de.symeda.sormas.backend.therapy.PrescriptionService;
import de.symeda.sormas.backend.therapy.Therapy;
import de.symeda.sormas.backend.therapy.TherapyFacadeEjb;
import de.symeda.sormas.backend.therapy.TherapyFacadeEjb.TherapyFacadeEjbLocal;
import de.symeda.sormas.backend.therapy.Treatment;
import de.symeda.sormas.backend.therapy.TreatmentFacadeEjb;
import de.symeda.sormas.backend.therapy.TreatmentFacadeEjb.TreatmentFacadeEjbLocal;
import de.symeda.sormas.backend.therapy.TreatmentService;
import de.symeda.sormas.backend.travelentry.TravelEntry;
import de.symeda.sormas.backend.travelentry.services.TravelEntryService;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserFacadeEjb;
import de.symeda.sormas.backend.user.UserReference;
import de.symeda.sormas.backend.user.UserRoleFacadeEjb;
import de.symeda.sormas.backend.user.UserRoleService;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.IterableHelper;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.PatchHelper;
import de.symeda.sormas.backend.util.Pseudonymizer;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;
import de.symeda.sormas.backend.vaccination.Vaccination;
import de.symeda.sormas.backend.vaccination.VaccinationFacadeEjb;
import de.symeda.sormas.backend.vaccination.VaccinationService;
import de.symeda.sormas.backend.visit.Visit;
import de.symeda.sormas.backend.visit.VisitFacadeEjb;
import de.symeda.sormas.backend.visit.VisitFacadeEjb.VisitFacadeEjbLocal;
import de.symeda.sormas.backend.visit.VisitService;

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
    private DistrictFacadeEjbLocal districtFacade;
    @EJB
    private DistrictService districtService;
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
    private SormasToSormasOriginInfoFacadeEjbLocal originInfoFacade;
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

    @Resource
    private ManagedScheduledExecutorService executorService;

    public CaseFacadeEjb() {
    }

    @Inject
    public CaseFacadeEjb(CaseService service, UserService userService) {
        super(Case.class, CaseDataDto.class, service, userService);
    }

    @Override
    public List<CaseDataDto> getAllActiveCasesAfter(Date date) {
        return getAllActiveCasesAfter(date, false);
    }

    @Override
    public List<CaseDataDto> getAllActiveCasesAfter(Date date, boolean includeExtendedChangeDateFilters) {
        return getAllActiveCasesAfter(date, includeExtendedChangeDateFilters, null, null);
    }

    @Override
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

    @Override
    protected void selectDtoFields(CriteriaQuery<CaseDataDto> cq, Root<Case> root) {

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

        Long resultSet = em.createQuery(cq).getSingleResult();
        return resultSet;
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

    @Override
    public List<CaseIndexDto> getIndexList(CaseCriteria caseCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

        CriteriaQuery<CaseIndexDto> cq = listQueryBuilder.buildIndexCriteria(caseCriteria, sortProperties);

        List<CaseIndexDto> cases = QueryHelper.getResultList(em, cq, first, max);
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

    //@Override
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
            UserRight._CASE_EDIT})
    public CaseDataDto postUpdate(String uuid, JsonNode caseDataDtoJson) {
        CaseDataDto existingCaseDto = getCaseDataWithoutPseudonyimization(uuid);
        PatchHelper.postUpdate(caseDataDtoJson, existingCaseDto);

        return this.save(existingCaseDto);
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
        cq.multiselect(caseRoot.get(Case.ID), joins.getPerson().get(Person.ID), joins.getPersonAddress().get(Location.ID),
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
            symptomsList = em.createQuery(symptomsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
            Map<Long, Symptoms> symptoms = symptomsList.stream().collect(Collectors.toMap(Symptoms::getId, Function.identity()));

            Map<Long, Case> caseAddresses = null;
            if (ExportHelper.shouldExportFields(
                    exportConfiguration,
                    CaseDataDto.REPORT_LAT,
                    CaseExportDto.CASE_LATITUDE,
                    CaseExportDto.CASE_LONGITUDE,
                    CaseExportDto.CASE_LAT_LON_ACCURACY,
                    CaseExportDto.CASE_GPS_COORDINATES)) {
                CriteriaQuery<Case> caseAddressesCq = cb.createQuery(Case.class);
                Root<Case> caseAddressRoot = caseAddressesCq.from(Case.class);
                Expression<String> caseAddressesIdsExpr = caseAddressRoot.get(Case.ID);
                caseAddressesCq.where(caseAddressesIdsExpr.in(resultList.stream().map(CaseExportDto::getId).collect(Collectors.toList())));
                List<Case> caseAddressesList = em.createQuery(caseAddressesCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
                caseAddresses = caseAddressesList.stream().collect(Collectors.toMap(Case::getId, Function.identity()));
            }

            Map<Long, Location> personAddresses = null;
            if (ExportHelper.shouldExportFields(
                    exportConfiguration,
                    PersonDto.ADDRESS,
                    CaseExportDto.ADDRESS_GPS_COORDINATES,
                    CaseExportDto.PERSON_LATITUDE,
                    CaseExportDto.PERSON_LONGITUDE)) {
                CriteriaQuery<Location> personAddressesCq = cb.createQuery(Location.class);
                Root<Location> personAddressesRoot = personAddressesCq.from(Location.class);
                Expression<String> personAddressesIdsExpr = personAddressesRoot.get(Location.ID);
                personAddressesCq
                        .where(personAddressesIdsExpr.in(resultList.stream().map(CaseExportDto::getPersonAddressId).collect(Collectors.toList())));
                List<Location> personAddressesList =
                        em.createQuery(personAddressesCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
                personAddresses = personAddressesList.stream().collect(Collectors.toMap(Location::getId, Function.identity()));
            }

            Map<Long, Integer> prescriptionCounts = null;
            Map<Long, Integer> treatmentCounts = null;
            Map<Long, Integer> clinicalVisitCounts = null;
            Map<Long, HealthConditions> healthConditions = null;
            if (exportType == null || exportType == CaseExportType.CASE_MANAGEMENT) {
                if (ExportHelper.shouldExportFields(exportConfiguration, CaseDataDto.HEALTH_CONDITIONS)) {
                    List<HealthConditions> healthConditionsList = null;
                    CriteriaQuery<HealthConditions> healthConditionsCq = cb.createQuery(HealthConditions.class);
                    Root<HealthConditions> healthConditionsRoot = healthConditionsCq.from(HealthConditions.class);
                    Expression<String> healthConditionsIdsExpr = healthConditionsRoot.get(HealthConditions.ID);
                    healthConditionsCq.where(
                            healthConditionsIdsExpr.in(resultList.stream().map(CaseExportDto::getHealthConditionsId).collect(Collectors.toList())));
                    healthConditionsList = em.createQuery(healthConditionsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
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
                prevHospsList = em.createQuery(prevHospsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
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
                List<Exposure> exposureList = em.createQuery(exposuresCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
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
                samplesList = em.createQuery(samplesCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
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
                immunizationList = em.createQuery(immunizationsCq).setHint(ModelConstants.HINT_HIBERNATE_READ_ONLY, true).getResultList();
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
                            .ifPresent(symptom -> exportDto.setSymptoms(SymptomsFacadeEjb.toDto(symptom)));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.CASE_LATITUDE)) {
                    Optional.ofNullable(caseAddresses.get(exportDto.getId()))
                            .ifPresent(caseAddress -> exportDto.setCaseLatitude(caseAddress.buildCaseLatitudeCoordination()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.CASE_LONGITUDE)) {
                    Optional.ofNullable(caseAddresses.get(exportDto.getId()))
                            .ifPresent(caseAddress -> exportDto.setCaseLongitude(caseAddress.buildCaseLongitudeCoordination()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.CASE_LAT_LON_ACCURACY)) {
                    Optional.ofNullable(caseAddresses.get(exportDto.getId()))
                            .ifPresent(caseAddress -> exportDto.setCaseLatLonAccuracy(caseAddress.buildCaseLatLonCoordination()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.CASE_GPS_COORDINATES)) {
                    Optional.ofNullable(caseAddresses.get(exportDto.getId()))
                            .ifPresent(caseAddress -> exportDto.setCaseGpsCoordinates(caseAddress.buildCaseGpsCoordinationCaption()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.PERSON_LATITUDE)) {
                    Optional.ofNullable(personAddresses.get(exportDto.getPersonAddressId()))
                            .ifPresent(personAddress -> exportDto.setPersonLatitude(personAddress.buildLatitudeCoordination()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.PERSON_LONGITUDE)) {
                    Optional.ofNullable(personAddresses.get(exportDto.getPersonAddressId()))
                            .ifPresent(personAddress -> exportDto.setPersonLongitude(personAddress.buildLongitudeCoordination()));
                }
                if (exportConfiguration == null || exportConfiguration.getProperties().contains(CaseExportDto.PERSON_LAT_LON_ACCURACY)) {
                    Optional.ofNullable(personAddresses.get(exportDto.getPersonAddressId()))
                            .ifPresent(personAddress -> exportDto.setPersonLatLonAccuracy(personAddress.buildLatLonCoordination()));
                }
                if (personAddresses != null || exportConfiguration.getProperties().contains(CaseExportDto.ADDRESS_GPS_COORDINATES)) {
                    Optional.ofNullable(personAddresses.get(exportDto.getPersonAddressId()))
                            .ifPresent(personAddress -> exportDto.setAddressGpsCoordinates(personAddress.buildGpsCoordinatesCaption()));
                }
                if (prescriptionCounts != null) {
                    Optional.ofNullable(prescriptionCounts.get(exportDto.getId()))
                            .ifPresent(prescriptionCount -> exportDto.setNumberOfPrescriptions(prescriptionCount));
                }
                if (treatmentCounts != null) {
                    Optional.ofNullable(treatmentCounts.get(exportDto.getId()))
                            .ifPresent(treatmentCount -> exportDto.setNumberOfTreatments(treatmentCount));
                }
                if (clinicalVisitCounts != null) {
                    Optional.ofNullable(clinicalVisitCounts.get(exportDto.getId()))
                            .ifPresent(clinicalVisitCount -> exportDto.setNumberOfClinicalVisits(clinicalVisitCount));
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

        Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
        pseudonymizer.pseudonymizeDtoCollection(
                MapCaseDto.class,
                cases,
                c -> c.getInJurisdiction(),
                (c, isInJurisdiction) -> pseudonymizer.pseudonymizeDto(PersonReferenceDto.class, c.getPerson(), isInJurisdiction, null));

        return cases;
    }

    @Override
    public List<CaseDataDto> getAllCasesOfPerson(String personUuid) {

        Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");

        return service.findBy(new CaseCriteria().person(new PersonReferenceDto(personUuid)), false)
                .stream()
                .map(c -> convertToDto(c, pseudonymizer))
                .collect(Collectors.toList());
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
    public List<CaseIndexDto[]> getCasesForDuplicateMerging(CaseCriteria criteria, boolean ignoreRegion) {

        return service.getCasesForDuplicateMerging(criteria, ignoreRegion, configFacade.getNameSimilarityThreshold());
    }


    @RightsAllowed(UserRight._CASE_EDIT)
    public void updateCompleteness(String caseUuid) {
        service.updateCompleteness(caseUuid);
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
    public CaseDataDto getCaseDataByUuid(String uuid) {
        return convertToDto(service.getByUuid(uuid), getPseudonymizerForDtoWithClinician(""));
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


    private CaseDataDto getCaseDataWithoutPseudonyimization(String uuid) {
        return toDto(service.getByUuid(uuid));
    }

    @Override
    public CaseReferenceDto getReferenceByUuid(String uuid) {
        return convertToReferenceDto(service.getByUuid(uuid));
    }

    @Override
    public CaseDataDto saveCase(@Valid CaseDataDto dto) throws ValidationRuntimeException {
        return saveCase(dto, true, true);
    }

    @Override
    @RightsAllowed({
            UserRight._CASE_CREATE,
            UserRight._CASE_EDIT})
    public CaseDataDto save(@Valid @NotNull CaseDataDto dto) throws ValidationRuntimeException {
        return save(dto, true, true, true, false);
    }

    @Override
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
            UserRight._CASE_EDIT})
    public Integer saveBulkCase(
            List<String> caseUuidList,
            @Valid CaseBulkEditData updatedCaseBulkEditData,
            boolean diseaseChange,
            boolean classificationChange,
            boolean investigationStatusChange,
            boolean outcomeChange,
            boolean surveillanceOfficerChange)
            throws ValidationRuntimeException {

        int changedCases = 0;
        for (String caseUuid : caseUuidList) {
            Case caze = service.getByUuid(caseUuid);

            if (service.getEditPermissionType(caze).equals(EditPermissionType.ALLOWED)) {
                CaseDataDto existingCaseDto = toDto(caze);

                updateCaseWithBulkData(
                        updatedCaseBulkEditData,
                        caze,
                        diseaseChange,
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

    @RightsAllowed({
            UserRight._CASE_EDIT})
    public void saveBulkEditWithFacilities(
            List<String> caseUuidList,
            @Valid CaseBulkEditData updatedCaseBulkEditData,
            boolean diseaseChange,
            boolean classificationChange,
            boolean investigationStatusChange,
            boolean outcomeChange,
            boolean surveillanceOfficerChange,
            Boolean doTransfer) {

        Region newRegion = regionService.getByUuid(updatedCaseBulkEditData.getRegion().getUuid());
        District newDistrict = districtService.getByUuid(updatedCaseBulkEditData.getDistrict().getUuid());
        CommunityReferenceDto communityDto = updatedCaseBulkEditData.getCommunity();
        Community newCommunity = null;
        if (communityDto != null) {
            newCommunity = communityService.getByUuid(updatedCaseBulkEditData.getCommunity().getUuid());
        }

        for (String caseUuid : caseUuidList) {
            Case caze = service.getByUuid(caseUuid);

            if (service.getEditPermissionType(caze).equals(EditPermissionType.ALLOWED)) {
                CaseDataDto existingCaseDto = toDto(caze);

                updateCaseWithBulkData(
                        updatedCaseBulkEditData,
                        caze,
                        diseaseChange,
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
            }
        }
    }

    private void updateCaseWithBulkData(
            CaseBulkEditData updatedCaseBulkEditData,
            Case existingCase,
            boolean diseaseChange,
            boolean classificationChange,
            boolean investigationStatusChange,
            boolean outcomeChange,
            boolean surveillanceOfficerChange) {

        if (diseaseChange) {
            existingCase.setDisease(updatedCaseBulkEditData.getDisease());
            existingCase.setDiseaseVariant(updatedCaseBulkEditData.getDiseaseVariant());
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

    @RightsAllowed({
            UserRight._CASE_EDIT})
    public CaseDataDto updateFollowUpComment(@Valid @NotNull CaseDataDto dto) throws ValidationRuntimeException {
        Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");
        Case caze = service.getByUuid(dto.getUuid());
        caze.setFollowUpComment(dto.getFollowUpComment());
        service.ensurePersisted(caze);
        return convertToDto(caze, pseudonymizer);
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

        Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");

        restorePseudonymizedDto(dto, existingCaseDto, existingCaze, pseudonymizer);

        validateUserRights(dto, existingCaseDto);
        validate(dto);

        externalJournalService.handleExternalJournalPersonUpdateAsync(dto.getPerson());

        Case caze = fillOrBuildEntity(dto, existingCaze, checkChangeDate);

        // Set version number on a new case
        if (caze.getCreationDate() == null && StringUtils.isEmpty(dto.getCreationVersion())) {
            caze.setCreationVersion(InfoProvider.get().getVersion());
        }

        doSave(caze, handleChanges, existingCaseDto, syncShares);

        return convertToDto(caze, pseudonymizer);
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
            UserRight._CASE_EDIT})
    public void setSampleAssociations(ContactReferenceDto sourceContact, CaseReferenceDto cazeRef) {

        if (sourceContact != null) {
            final Contact contact = contactService.getByUuid(sourceContact.getUuid());
            final Case caze = service.getByUuid(cazeRef.getUuid());
            contact.getSamples().forEach(sample -> {
                if (!sample.isDeleted()) {
                    if (sample.getAssociatedCase() == null) {
                        sample.setAssociatedCase(caze);
                    } else if (!sample.getAssociatedCase().getUuid().equals(cazeRef.getUuid())) {
                        sampleFacade.cloneSampleForCase(sample, caze);
                    }
                }
            });

            // The samples for case are not persisted yet, so use the samples from contact since they are the same
            caze.setFollowUpUntil(service.computeFollowUpuntilDate(caze, contact.getSamples()));
        }
    }

    @Override
    @RightsAllowed({
            UserRight._CASE_CREATE,
            UserRight._CASE_EDIT})
    public void setSampleAssociations(EventParticipantReferenceDto sourceEventParticipant, CaseReferenceDto cazeRef) {

        if (sourceEventParticipant != null) {
            final EventParticipant eventParticipant = eventParticipantService.getByUuid(sourceEventParticipant.getUuid());
            final Case caze = service.getByUuid(cazeRef.getUuid());
            eventParticipant.getSamples().forEach(sample -> {
                if (!sample.isDeleted()) {
                    if (sample.getAssociatedCase() == null) {
                        sample.setAssociatedCase(caze);
                    } else if (!sample.getAssociatedCase().getUuid().equals(cazeRef.getUuid())) {
                        sampleFacade.cloneSampleForCase(sample, caze);
                    }
                }
            });

            // The samples for case are not persisted yet, so use the samples from event participant since they are the same
            caze.setFollowUpUntil(service.computeFollowUpuntilDate(caze, eventParticipant.getSamples()));
        }
    }

    @Override
    @RightsAllowed({
            UserRight._CASE_CREATE,
            UserRight._CASE_EDIT})
    public void setSampleAssociationsUnrelatedDisease(EventParticipantReferenceDto sourceEventParticipant, CaseReferenceDto cazeRef) {
        final EventParticipant eventParticipant = eventParticipantService.getByUuid(sourceEventParticipant.getUuid());
        final Case caze = service.getByUuid(cazeRef.getUuid());
        final Disease disease = caze.getDisease();
        eventParticipant.getSamples().stream().filter(sample -> sampleContainsTestForDisease(sample, disease)).forEach(sample -> {
            if (sample.getAssociatedCase() == null) {
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
    public void validate(CaseDataDto caze) throws ValidationRuntimeException {

        // Check whether any required field that does not have a not null constraint in
        // the database is empty
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
        if ((caze.getCaseOrigin() == null || caze.getCaseOrigin() == CaseOrigin.IN_COUNTRY) && caze.getHealthFacility() == null) {
            throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validFacility));
        }
        if (CaseOrigin.POINT_OF_ENTRY.equals(caze.getCaseOrigin()) && caze.getPointOfEntry() == null) {
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

            if (caze.getFacilityType() == null) {
                if (!FacilityDto.NONE_FACILITY_UUID.equals(caze.getHealthFacility().getUuid())) {
                    throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.noFacilityType));
                }
            } else if (!caze.getFacilityType().isAccommodation()) {
                throw new ValidationRuntimeException(
                        I18nProperties.getValidationError(Validations.notAccomodationFacilityType, caze.getFacilityType()));
            }

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

                if (!userService.hasRight(UserRight.CASE_TRANSFER)) {
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
            PlagueType plagueType = DiseaseHelper.getPlagueTypeForSymptoms(SymptomsFacadeEjb.toDto(newCase.getSymptoms()));
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
                && YesNoUnknown.YES != newCase.getHospitalization().getHospitalizedPreviously()) {
            newCase.getHospitalization().setHospitalizedPreviously(YesNoUnknown.YES);
        }
        if (!newCase.getEpiData().getExposures().isEmpty() && !YesNoUnknown.YES.equals(newCase.getEpiData().getExposureDetailsKnown())) {
            newCase.getEpiData().setExposureDetailsKnown(YesNoUnknown.YES);
        }

        // Update completeness value
        newCase.setCompleteness(null);

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
        // Unspecified VHF case has changed
        if (existingCase != null && existingCase.getDisease() == Disease.UNSPECIFIED_VHF && existingCase.getDisease() != newCase.getDisease()) {

            try {
                String message = String.format(
                        I18nProperties.getString(MessageContents.CONTENT_DISEASE_CHANGED),
                        DataHelper.getShortUuid(newCase.getUuid()),
                        existingCase.getDisease().toString(),
                        newCase.getDisease().toString());

                notificationService.sendNotifications(
                        NotificationType.DISEASE_CHANGED,
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
                    new Object[]{
                            caze.getDisease().getName()},
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
        if (featureConfigurationFacade.isPropertyValueTrue(FeatureType.CASE_SURVEILANCE, FeatureTypeProperty.AUTOMATIC_RESPONSIBILITY_ASSIGNMENT)) {
            District reportingUserDistrict = caze.getReportingUser().getDistrict();

            if (userRoleService.hasUserRight(caze.getReportingUser().getUserRoles(), UserRight.CASE_RESPONSIBLE)
                    && (reportingUserDistrict == null
                    || reportingUserDistrict.equals(caze.getResponsibleDistrict())
                    || reportingUserDistrict.equals(caze.getDistrict()))) {
                caze.setSurveillanceOfficer(caze.getReportingUser());
            } else {
                List<User> hospitalUsers = caze.getHealthFacility() != null && FacilityType.HOSPITAL.equals(caze.getHealthFacility().getType())
                        ? userService.getFacilityUsersOfHospital(caze.getHealthFacility())
                        : new ArrayList<>();
                Random rand = new Random();
                if (!hospitalUsers.isEmpty()) {
                    caze.setSurveillanceOfficer(hospitalUsers.get(rand.nextInt(hospitalUsers.size())).getAssociatedOfficer());
                } else {
                    User survOff = null;
                    if (caze.getResponsibleDistrict() != null) {
                        survOff = getRandomDistrictCaseResponsible(caze.getResponsibleDistrict());
                    }

                    if (survOff == null && caze.getDistrict() != null) {
                        survOff = getRandomDistrictCaseResponsible(caze.getDistrict());
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
     * @param caze              the case which related tasks are reassigned.
     * @param forceReassignment force reassignment of case tasks.
     */
    @RightsAllowed({
            UserRight._CASE_CREATE,
            UserRight._CASE_EDIT})
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
                    PersonDto existingPerson = PersonFacadeEjb.toDto(newCase.getPerson());
                    newCase.getPerson().setPresentCondition(PresentCondition.DEAD);
                    newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
                    newCase.getPerson().setCauseOfDeath(CauseOfDeath.EPIDEMIC_DISEASE);
                    newCase.getPerson().setCauseOfDeathDisease(newCase.getDisease());
                    // attention: this may lead to infinite recursion when not properly implemented
                    personFacade.onPersonChanged(existingPerson, newCase.getPerson());
                }

            } else if ((newCase.getOutcome() == CaseOutcome.NO_OUTCOME || newCase.getOutcome() == CaseOutcome.RECOVERED)
                    && existingCase.getOutcome() == CaseOutcome.DECEASED) {
                // Case was put "back alive"
                PersonDto existingPerson = PersonFacadeEjb.toDto(newCase.getPerson());
                boolean withinDateThreshold = newCase.getReportDate().before(DateHelper.addDays(existingPerson.getDeathDate(), 30))
                        && newCase.getReportDate().after(DateHelper.subtractDays(existingPerson.getDeathDate(), 30));

                if (existingPerson.getCauseOfDeath() == CauseOfDeath.EPIDEMIC_DISEASE
                        && existingPerson.getCauseOfDeathDisease() == newCase.getDisease()
                        && withinDateThreshold) {
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
            PersonDto existingPerson = PersonFacadeEjb.toDto(newCase.getPerson());
            newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
            personFacade.onPersonChanged(existingPerson, newCase.getPerson());
        } else if (existingCase == null) {
            // new Case; Still compare persons Condition and caseOutcome
            if (newCase.getOutcome() == CaseOutcome.DECEASED
                    && newCase.getPerson().getPresentCondition() != PresentCondition.BURIED
                    && newCase.getPerson().getPresentCondition() != PresentCondition.DEAD) {
                // person is alive but case has outcome deceased
                PersonDto existingPerson = PersonFacadeEjb.toDto(newCase.getPerson());
                newCase.getPerson().setDeathDate(newCase.getOutcomeDate());
                newCase.getPerson().setPresentCondition(PresentCondition.DEAD);
                newCase.getPerson().setCauseOfDeath(CauseOfDeath.EPIDEMIC_DISEASE);
                newCase.getPerson().setCauseOfDeathDisease(newCase.getDisease());
                personFacade.onPersonChanged(existingPerson, newCase.getPerson());
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

    @Override
    @RightsAllowed(UserRight._CASE_DELETE)
    public void delete(String caseUuid, DeletionDetails deletionDetails) throws ExternalSurveillanceToolRuntimeException {
        Case caze = service.getByUuid(caseUuid);
        deleteCase(caze, deletionDetails);
    }

    @Override
    @RightsAllowed(UserRight._CASE_DELETE)
    public void deleteWithContacts(String caseUuid, DeletionDetails deletionDetails) {

        Case caze = service.getByUuid(caseUuid);
        deleteCase(caze, deletionDetails);

        Optional.of(caze.getContacts()).ifPresent(cl -> cl.forEach(c -> contactService.delete(c, deletionDetails)));
    }

    private void deleteCase(Case caze, DeletionDetails deletionDetails) throws ExternalSurveillanceToolRuntimeException {

        externalJournalService.handleExternalJournalPersonUpdateAsync(caze.getPerson().toReference());
        service.delete(caze, deletionDetails);
    }

    @RightsAllowed(UserRight._CASE_DELETE)
    @Override
    public List<String> deleteCases(List<String> caseUuids, DeletionDetails deletionDetails) {

        List<String> deletedCasesUuids = new ArrayList<>();
        List<Case> casesToBeDeleted = service.getByUuids(caseUuids);
        if (casesToBeDeleted != null) {
            casesToBeDeleted.forEach(caseToBeDeleted -> {
                if (!caseToBeDeleted.isDeleted()) {
                    try {
                        deleteCase(caseToBeDeleted, deletionDetails);
                        deletedCasesUuids.add(caseToBeDeleted.getUuid());
                    } catch (ExternalSurveillanceToolRuntimeException e) {
                        logger.error("The case with uuid:" + caseToBeDeleted.getUuid() + "could not be deleted");
                    }
                }
            });
        }
        return deletedCasesUuids;
    }

    @Override
    @RightsAllowed(UserRight._CASE_MERGE)
    public void deleteCaseAsDuplicate(String caseUuid, String duplicateOfCaseUuid) {

        Case caze = service.getByUuid(caseUuid);
        Case duplicateOfCase = service.getByUuid(duplicateOfCaseUuid);
        caze.setDuplicateOf(duplicateOfCase);
        service.ensurePersisted(caze);

        delete(caseUuid, new DeletionDetails(DeletionReason.DUPLICATE_ENTRIES, null));
    }

    @RightsAllowed({
            UserRight._CASE_DELETE,
            UserRight._SYSTEM})
    public void deleteCaseInExternalSurveillanceTool(Case caze) throws ExternalSurveillanceToolException {

        if (externalSurveillanceToolGatewayFacade.isFeatureEnabled() && caze.getExternalID() != null && !caze.getExternalID().isEmpty()) {
            List<CaseDataDto> casesWithSameExternalId = getByExternalId(caze.getExternalID());
            if (casesWithSameExternalId != null && casesWithSameExternalId.size() == 1) {
                externalSurveillanceToolGatewayFacade.deleteCases(Collections.singletonList(toDto(caze)));
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
            UserRight._CASE_EDIT})
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

    @Override
    @RightsAllowed({
            UserRight._CASE_EDIT,
            UserRight._CASE_EDIT})
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
            pseudonymizer
                    .restorePseudonymizedValues(MaternalHistoryDto.class, dto.getMaternalHistory(), existingCaseDto.getMaternalHistory(), inJurisdiction);
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
            UserRight._EXTERNAL_VISITS})
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
        target.setRabiesType(source.getRabiesType());
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

        target.setSurveillanceOfficer(UserFacadeEjb.toReferenceDto(source.getSurveillanceOfficer()));
        target.setClinicianName(source.getClinicianName());
        target.setClinicianPhone(source.getClinicianPhone());
        target.setClinicianEmail(source.getClinicianEmail());
        target.setCaseOfficer(UserFacadeEjb.toReferenceDto(source.getCaseOfficer()));
        target.setSymptoms(SymptomsFacadeEjb.toDto(source.getSymptoms()));

        target.setPregnant(source.getPregnant());
        target.setVaccinationStatus(source.getVaccinationStatus());
        target.setLastDateOfVaccination(source.getLastDateOfVaccination());
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

        target.setCaseTransmissionClassification(source.getCaseTransmissionClassification());
        target.setSpecifyOtherOutcome(source.getSpecifyOtherOutcome());
        target.setDeleted(source.isDeleted());
        target.setDeletionReason(source.getDeletionReason());
        target.setOtherDeletionReason(source.getOtherDeletionReason());

        return target;
    }

    @Override
    public CaseReferenceDto toRefDto(Case aCase) {
        return convertToReferenceDto(aCase);
    }

    public Case fillOrBuildEntity(@NotNull CaseDataDto source, Case target, boolean checkChangeDate) {

        target = DtoHelper.fillOrBuildEntity(source, target, () -> {
            Case newCase = new Case();
            newCase.setSystemCaseClassification(CaseClassification.NOT_CLASSIFIED);

            return newCase;
        }, checkChangeDate);

        target.setDisease(source.getDisease());
        target.setDiseaseVariant(source.getDiseaseVariant());
        target.setDiseaseDetails(source.getDiseaseDetails());
        target.setDiseaseVariantDetails(source.getDiseaseVariantDetails());
        target.setPlagueType(source.getPlagueType());
        target.setDengueFeverType(source.getDengueFeverType());
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
        target.setPerson(personService.getByReferenceDto(source.getPerson()));
        target.setCaseClassification(source.getCaseClassification());
        target.setCaseIdentificationSource(source.getCaseIdentificationSource());
        target.setScreeningType(source.getScreeningType());
        target.setClassificationUser(userService.getByReferenceDto(source.getClassificationUser()));
        target.setClassificationDate(source.getClassificationDate());
        target.setClassificationComment(source.getClassificationComment());
        target.setClinicalConfirmation(source.getClinicalConfirmation());
        target.setEpidemiologicalConfirmation(source.getEpidemiologicalConfirmation());
        target.setLaboratoryDiagnosticConfirmation(source.getLaboratoryDiagnosticConfirmation());
        target.setInvestigationStatus(source.getInvestigationStatus());
        target.setHospitalization(hospitalizationFacade.fromDto(source.getHospitalization(), checkChangeDate));
        target.setEpiData(epiDataFacade.fromDto(source.getEpiData(), checkChangeDate));
        if (source.getTherapy() == null) {
            source.setTherapy(TherapyDto.build());
        }
        target.setTherapy(therapyFacade.fromDto(source.getTherapy(), checkChangeDate));
        if (source.getHealthConditions() == null) {
            source.setHealthConditions(HealthConditionsDto.build());
        }
        target.setHealthConditions(healthConditionsMapper.fromDto(source.getHealthConditions(), checkChangeDate));
        if (source.getClinicalCourse() == null) {
            source.setClinicalCourse(ClinicalCourseDto.build());
        }
        target.setClinicalCourse(clinicalCourseFacade.fromDto(source.getClinicalCourse(), checkChangeDate));
        if (source.getMaternalHistory() == null) {
            source.setMaternalHistory(MaternalHistoryDto.build());
        }
        target.setMaternalHistory(maternalHistoryFacade.fromDto(source.getMaternalHistory(), checkChangeDate));
        if (source.getPortHealthInfo() == null) {
            source.setPortHealthInfo(PortHealthInfoDto.build());
        }
        target.setPortHealthInfo(portHealthInfoFacade.fromDto(source.getPortHealthInfo(), checkChangeDate));

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
        target.setCaseOfficer(userService.getByReferenceDto(source.getCaseOfficer()));
        target.setSymptoms(symptomsFacade.fromDto(source.getSymptoms(), checkChangeDate));

        target.setPregnant(source.getPregnant());
        target.setVaccinationStatus(source.getVaccinationStatus());
        target.setLastDateOfVaccination(source.getLastDateOfVaccination());
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
        target.setFacilityType(source.getFacilityType());
        if (source.getSormasToSormasOriginInfo() != null) {
            target.setSormasToSormasOriginInfo(originInfoFacade.fromDto(source.getSormasToSormasOriginInfo(), checkChangeDate));
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

        target.setSpecifyOtherOutcome(source.getSpecifyOtherOutcome());
        target.setCaseTransmissionClassification(source.getCaseTransmissionClassification());

        if (source.getExternalData() != null) {
            target.setExternalData(source.getExternalData());
        }

        target.setDeleted(source.isDeleted());
        target.setDeletionReason(source.getDeletionReason());
        target.setOtherDeletionReason(source.getOtherDeletionReason());

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
            long pendingCount = existingCase != null
                    ? taskService.getCount(new TaskCriteria().taskType(TaskType.CASE_INVESTIGATION).caze(caseRef).taskStatus(TaskStatus.PENDING))
                    : 0;

            if (pendingCount == 0 && featureConfigurationFacade.isTaskGenerationFeatureEnabled(TaskType.CASE_INVESTIGATION)) {
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
            List<Pair<DistrictDto, BigDecimal>> resultList = results.stream()
                    .map(e -> new Pair<DistrictDto, BigDecimal>(districtFacade.toDto((District) e[0]), new BigDecimal((Long) e[1])))
                    .collect(Collectors.toList());
            return resultList;
        } else {
            List<Pair<DistrictDto, BigDecimal>> resultList = results.stream().map(e -> {
                District district = (District) e[0];
                Integer population = populationDataFacade.getProjectedDistrictPopulation(district.getUuid());
                Long caseCount = (Long) e[1];

                if (population == null || population <= 0) {
                    // No or negative population - these entries will be cut off in the UI
                    return new Pair<DistrictDto, BigDecimal>(districtFacade.toDto(district), new BigDecimal(0));
                } else {
                    return new Pair<DistrictDto, BigDecimal>(
                            districtFacade.toDto(district),
                            InfrastructureHelper.getCaseIncidence(caseCount.intValue(), population, InfrastructureHelper.CASE_INCIDENCE_DIVISOR));
                }
            }).sorted(new Comparator<Pair<DistrictDto, BigDecimal>>() {

                @Override
                public int compare(Pair<DistrictDto, BigDecimal> o1, Pair<DistrictDto, BigDecimal> o2) {
                    return o1.getElement1().compareTo(o2.getElement1());
                }
            }).collect(Collectors.toList());
            return resultList;
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
    public boolean isDeleted(String caseUuid) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Case> from = cq.from(Case.class);

        cq.where(cb.and(cb.isTrue(from.get(Case.DELETED)), cb.equal(from.get(AbstractDomainObject.UUID), caseUuid)));
        cq.select(cb.count(from));
        long count = em.createQuery(cq).getSingleResult();
        return count > 0;
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
        for (VisitDto otherVisit : otherCase.getVisits().stream().map(VisitFacadeEjb::toDto).collect(Collectors.toList())) {
            otherVisit.setPerson(leadCaseData.getPerson());
            otherVisit.setDisease(leadCaseData.getDisease());
            visitFacade.saveVisit(otherVisit);
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
        List<SurveillanceReport> surveillanceReports = surveillanceReportService.getByCaseUuids(Collections.singletonList(otherCase.getUuid()));
        surveillanceReports.forEach(surveillanceReport -> {
            SurveillanceReportDto surveillanceReportDto = SurveillanceReportFacadeEjb.toDto(surveillanceReport);
            surveillanceReportDto.setCaze(leadCase.toReference());
            surveillanceReportFacade.saveSurveillanceReport(surveillanceReportDto);
        });

        // 10 Activity as case
        final EpiData otherEpiData = otherCase.getEpiData();
        if (otherEpiData != null
                && YesNoUnknown.YES == otherEpiData.getActivityAsCaseDetailsKnown()
                && CollectionUtils.isNotEmpty(otherEpiData.getActivitiesAsCase())) {

            final EpiData leadEpiData = leadCase.getEpiData();
            leadEpiData.setActivityAsCaseDetailsKnown(YesNoUnknown.YES);
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
     * @param daysAfterCaseGetsArchived defines the amount of days
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RightsAllowed(UserRight._SYSTEM)
    public void archiveAllArchivableCases(int daysAfterCaseGetsArchived) {

        archiveAllArchivableCases(daysAfterCaseGetsArchived, LocalDate.now());
    }

    void archiveAllArchivableCases(int daysAfterCaseGetsArchived, LocalDate referenceDate) {

        long startTime = DateHelper.startTime();

        LocalDate notChangedSince = referenceDate.minusDays(daysAfterCaseGetsArchived);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Case> from = cq.from(Case.class);

        Timestamp notChangedTimestamp = Timestamp.valueOf(notChangedSince.atStartOfDay());
        cq.where(
                cb.equal(from.get(Case.ARCHIVED), false),
                cb.equal(from.get(Case.DELETED), false),
                cb.not(service.createChangeDateFilter(cb, from, notChangedTimestamp, true)));
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

    public boolean isCaseEditAllowed(String caseUuid) {
        Case caze = caseService.getByUuid(caseUuid);

        return caseService.isCaseEditAllowed(caze);
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
                e -> totalCount.addAndGet(countCasesWithMissingContactInfo(caseUuids, messageType)));

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
     * <p>
     * The reportDateThreshold allows to return duplicates where
     * -reportDateThreshold <= match.reportDate <= reportDateThreshold
     *
     * @param casePerson          - case and person
     * @param reportDateThreshold - the range bounds on match.reportDate
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
                    cb.equal(cb.trim(cb.lower(person.get(Person.LAST_NAME))), searchPerson.getLastName().toLowerCase().trim()));

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

            Predicate reportDatePredicate;

            if (reportDateThreshold == 0) {
                // threshold is zero: we want to get exact matches
                reportDatePredicate = cb.equal(
                        cb.function("date", Date.class, caseRoot.get(Case.REPORT_DATE)),
                        cb.function("date", Date.class, cb.literal(searchCaze.getReportDate())));
            } else {
                // threshold is nonzero: apply time range of threshold to the reportDate
                Date reportDate = casePerson.getCaze().getReportDate();
                Date dateBefore = new DateTime(reportDate).minusDays(reportDateThreshold).toDate();
                Date dateAfter = new DateTime(reportDate).plusDays(reportDateThreshold).toDate();

                reportDatePredicate = cb.between(
                        cb.function("date", Date.class, caseRoot.get(Case.REPORT_DATE)),
                        cb.function("date", Date.class, cb.literal(dateBefore)),
                        cb.function("date", Date.class, cb.literal(dateAfter)));
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
                                personFacade.getPersonByUuid((String) casePersonUuids[1])))
                .collect(Collectors.toList());
    }

    @Override
    public List<CasePersonDto> getDuplicates(@Valid CasePersonDto casePerson) {
        return getDuplicates(casePerson, 0);
    }

    @Override
    public List<CaseDataDto> getByPersonUuids(List<String> personUuids) {
        return service.getByPersonUuids(personUuids).stream().map(c -> toDto(c)).collect(Collectors.toList());
    }

    @Override
    public List<CaseDataDto> getByExternalId(String externalId) {
        Pseudonymizer pseudonymizer = getPseudonymizerForDtoWithClinician("");
        return service.getByExternalId(externalId).stream().map(c -> convertToDto(c, pseudonymizer)).collect(Collectors.toList());
    }

    @Override
    @RightsAllowed(UserRight._CASE_EDIT)
    public void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException {
        service.updateExternalData(externalData);
    }

    @RightsAllowed({
            UserRight._VISIT_CREATE,
            UserRight._VISIT_EDIT,
            UserRight._EXTERNAL_VISITS})
    public void updateSymptomsByVisit(Visit visit) {
        CaseDataDto cazeDto = CaseFacadeEjbLocal.toCaseDto(visit.getCaze());
        SymptomsDto caseSymptoms = cazeDto.getSymptoms();
        SymptomsHelper.updateSymptoms(SymptomsFacadeEjb.toDto(visit.getSymptoms()), caseSymptoms);

        save(cazeDto, true);
    }

    public Pseudonymizer getPseudonymizerForDtoWithClinician(@Nullable String pseudonymizedValue) {
        Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight, pseudonymizedValue);

        UserRightFieldAccessChecker clinicianViewRightChecker =
                new UserRightFieldAccessChecker(UserRight.CASE_CLINICIAN_VIEW, userService.hasRight(UserRight.CASE_CLINICIAN_VIEW));
        pseudonymizer.addFieldAccessChecker(clinicianViewRightChecker, clinicianViewRightChecker);

        return pseudonymizer;
    }

    @LocalBean
    @Stateless
    public static class CaseFacadeEjbLocal extends CaseFacadeEjb {

        public CaseFacadeEjbLocal() {
        }

        @Inject
        public CaseFacadeEjbLocal(CaseService service, UserService userService) {
            super(service, userService);
        }
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

    private <T extends AbstractDomainObject> Predicate createCaseCriteriaFilter(
            CaseCriteria dashboardCriteria,
            CaseQueryContext caseQueryContext) {

        final From<?, Case> from = caseQueryContext.getRoot();
        final CriteriaBuilder cb = caseQueryContext.getCriteriaBuilder();
        final CaseJoins joins = caseQueryContext.getJoins();

        Join<Case, Region> responsibleRegion = joins.getResponsibleRegion();
        Join<Case, District> responsibleDistrict = joins.getResponsibleDistrict();

        Predicate filter = null;
        if (dashboardCriteria.getDisease() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Case.DISEASE), dashboardCriteria.getDisease()));
        }
        if (dashboardCriteria.getCaseClassification() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Case.CASE_CLASSIFICATION), dashboardCriteria.getCaseClassification()));
        }
        if (dashboardCriteria.getRegion() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(responsibleRegion.get(Region.UUID), dashboardCriteria.getRegion().getUuid()));
        }
        if (dashboardCriteria.getDistrict() != null) {
            filter =
                    CriteriaBuilderHelper.and(cb, filter, cb.equal(responsibleDistrict.get(District.UUID), dashboardCriteria.getDistrict().getUuid()));
        }
        if (dashboardCriteria.getNewCaseDateFrom() != null && dashboardCriteria.getNewCaseDateTo() != null) {
            filter = CriteriaBuilderHelper.and(
                    cb,
                    filter,
                    caseService.createNewCaseFilter(
                            caseQueryContext,
                            DateHelper.getStartOfDay(dashboardCriteria.getNewCaseDateFrom()),
                            DateHelper.getEndOfDay(dashboardCriteria.getNewCaseDateTo()),
                            dashboardCriteria.getNewCaseDateType()));
        }
        if (dashboardCriteria.isIncludeNotACaseClassification() == null || dashboardCriteria.isIncludeNotACaseClassification() == false) {
            filter = CriteriaBuilderHelper
                    .and(cb, filter, cb.notEqual(caseQueryContext.getRoot().get(Case.CASE_CLASSIFICATION), CaseClassification.NO_CASE));
        }

        // Exclude deleted cases. Archived cases should stay included
        filter = CriteriaBuilderHelper.and(cb, filter, cb.isFalse(from.get(Case.DELETED)));

        return filter;
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

                for(CaseSampleExportDto embeddedDetailedSampleExportDto : caseSamples) {
                    Sample sampleFromExportDto = sampleService.getByUuid(embeddedDetailedSampleExportDto.getUuid());
                    List<PathogenTest> pathogenTests = sampleFromExportDto.getPathogenTests();

                    for (PathogenTest pathogenTest : pathogenTests) {
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

//                    if (exportDto.getId() == embeddedDetailedSampleExportDto.getCaseId()) {

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
            return newResult;
        }
        return resultList;
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

    public void mapAfpTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setAfpAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setAfpAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setAfpRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setAfpCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setAfpHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setAfpIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAfpIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAfpIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAfpIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setAfpIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setAfpIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setAfpMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setAfpNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setAfpPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setAfpGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setAfpLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setAfpCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setAfpSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setAfpDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setAfpOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAfpOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    //    Cholera
    public void mapCholeraTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setCholeraAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setCholeraAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setCholeraRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setCholeraCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setCholeraHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setCholeraIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCholeraIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCholeraIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCholeraIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setCholeraIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setCholeraIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setCholeraMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setCholeraNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setCholeraPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setCholeraGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setCholeraLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setCholeraCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setCholeraSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setCholeraDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setCholeraOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCholeraOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    //    CONGENITAL_RUBELLA
    public void mapCongenitalRubellaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setCongenitalRubellaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setCongenitalRubellaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setCongenitalRubellaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setCongenitalRubellaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setCongenitalRubellaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setCongenitalRubellaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setCongenitalRubellaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setCongenitalRubellaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setCongenitalRubellaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setCongenitalRubellaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setCongenitalRubellaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCongenitalRubellaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    //    CSM
    public void mapCsmTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setCsmAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setCsmAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setCsmRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setCsmCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setCsmHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setCsmIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCsmIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCsmIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCsmIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setCsmIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setCsmIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setCsmMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setCsmNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setCsmPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setCsmGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setCsmLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setCsmCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setCsmSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setCsmDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setCsmOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCsmOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    //    DENGUE
    public void mapDengueTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setDengueAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setDengueAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setDengueRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setDengueCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setDengueHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setDengueIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDengueIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDengueIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDengueIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setDengueIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setDengueIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setDengueMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setDengueNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setDenguePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDenguePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setDengueGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setDengueLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setDengueCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setDengueSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setDengueDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setDengueOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDengueOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    public void mapEvdTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setEvdAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setEvdAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setEvdRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setEvdCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setEvdHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setEvdIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEvdIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEvdIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEvdIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setEvdIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setEvdIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setEvdMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setEvdNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setEvdPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setEvdGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setEvdLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setEvdCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setEvdSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setEvdDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setEvdOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEvdOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    public void mapGuineaWormTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setGuineaWormAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setGuineaWormRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setGuineaWormCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setGuineaWormHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setGuineaWormIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setGuineaWormIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setGuineaWormIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setGuineaWormIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setGuineaWormIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setGuineaWormIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setGuineaWormMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setGuineaWormNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setGuineaWormPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setGuineaWormGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setGuineaWormLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setGuineaWormCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setGuineaWormSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setGuineaWormDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setGuineaWormOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setGuineaWormOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }


    public void mapLassaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setLassaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setLassaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setLassaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setLassaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setLassaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setLassaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLassaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLassaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLassaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setLassaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setLassaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setLassaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setLassaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setLassaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setLassaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setLassaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setLassaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setLassaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setLassaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setLassaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLassaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }


    public void mapMeaslesTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setMeaslesAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setMeaslesAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setMeaslesRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setMeaslesCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setMeaslesHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setMeaslesIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMeaslesIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMeaslesIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMeaslesIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setMeaslesIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setMeaslesIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setMeaslesMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setMeaslesNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setMeaslesPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setMeaslesGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setMeaslesLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setMeaslesCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setMeaslesSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setMeaslesDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setMeaslesOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMeaslesOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapMonkeyPoxTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setMonkeypoxAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setMonkeypoxRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setMonkeypoxCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setMonkeypoxHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setMonkeypoxIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMonkeypoxIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMonkeypoxIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMonkeypoxIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setMonkeypoxIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setMonkeypoxIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setMonkeypoxMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setMonkeypoxNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setMonkeypoxPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setMonkeypoxGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setMonkeypoxLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setMonkeypoxCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setMonkeypoxSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setMonkeypoxDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setMonkeypoxOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMonkeypoxOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapNewInfluenzaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setNewInfluenzaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setNewInfluenzaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setNewInfluenzaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setNewInfluenzaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setNewInfluenzaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNewInfluenzaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNewInfluenzaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNewInfluenzaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setNewInfluenzaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setNewInfluenzaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setNewInfluenzaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setNewInfluenzaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setNewInfluenzaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setNewInfluenzaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setNewInfluenzaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setNewInfluenzaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setNewInfluenzaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setNewInfluenzaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setNewInfluenzaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNewInfluenzaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPlagueTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPlagueRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPlagueCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPlagueHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPlagueIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPlagueIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPlagueMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPlaguePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlaguePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPlagueGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPlagueLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPlagueSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPlagueDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPlagueOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    public void mapPolioTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPlagueRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPlagueCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPlagueHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPlagueIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPlagueIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPlagueMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPlaguePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlaguePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPlagueGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPlagueLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPlagueCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPlagueSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPlagueDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPlagueOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPlagueOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapUnspecifiedVhfTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setUnspecifiedVhfOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUnspecifiedVhfOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapYellowFeverTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setYellowFeverAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setYellowFeverRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setYellowFeverCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setYellowFeverHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setYellowFeverIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYellowFeverIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYellowFeverIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYellowFeverIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setYellowFeverIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setYellowFeverIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setYellowFeverMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setYellowFeverNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setYellowFeverPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setYellowFeverGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setYellowFeverLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setYellowFeverCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setYellowFeverSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setYellowFeverDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setYellowFeverOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYellowFeverOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapRabiesTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setRabiesAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setRabiesAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setRabiesRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setRabiesCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setRabiesHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setRabiesIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRabiesIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRabiesIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRabiesIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setRabiesIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setRabiesIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setRabiesMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setRabiesNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setRabiesPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setRabiesGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setRabiesLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setRabiesCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setRabiesSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setRabiesDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setRabiesOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRabiesOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapAnthraxTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setAnthraxAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setAnthraxAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setAnthraxRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setAnthraxCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setAnthraxHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setAnthraxIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAnthraxIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAnthraxIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAnthraxIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setAnthraxIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setAnthraxIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setAnthraxMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setAnthraxNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setAnthraxPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setAnthraxGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setAnthraxLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setAnthraxCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setAnthraxSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setAnthraxDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setAnthraxOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAnthraxOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapCoronavirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setCoronavirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setCoronavirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setCoronavirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setCoronavirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setCoronavirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCoronavirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCoronavirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setCoronavirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setCoronavirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setCoronavirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setCoronavirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setCoronavirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setCoronavirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setCoronavirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setCoronavirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setCoronavirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setCoronavirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setCoronavirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setCoronavirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setCoronavirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPneumoniaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPneumoniaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPneumoniaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPneumoniaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPneumoniaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPneumoniaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPneumoniaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPneumoniaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPneumoniaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPneumoniaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPneumoniaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPneumoniaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPneumoniaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPneumoniaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPneumoniaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPneumoniaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPneumoniaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPneumoniaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPneumoniaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPneumoniaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPneumoniaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapMalariaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setMalariaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setMalariaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setMalariaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setMalariaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setMalariaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setMalariaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMalariaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMalariaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMalariaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setMalariaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setMalariaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setMalariaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setMalariaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setMalariaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setMalariaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setMalariaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setMalariaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setMalariaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setMalariaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setMalariaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMalariaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapTyphoidFeverTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setTyphoidFeverAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setTyphoidFeverRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setTyphoidFeverCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setTyphoidFeverHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setTyphoidFeverIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTyphoidFeverIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTyphoidFeverIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTyphoidFeverIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setTyphoidFeverIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setTyphoidFeverIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setTyphoidFeverMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setTyphoidFeverNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setTyphoidFeverPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setTyphoidFeverGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setTyphoidFeverLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setTyphoidFeverCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setTyphoidFeverSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setTyphoidFeverDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setTyphoidFeverOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTyphoidFeverOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapAcuteViralHepatitisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAcuteViralHepatitisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapNonNeonatalTetanusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNonNeonatalTetanusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapHivTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setHivAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setHivAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setHivRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setHivCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setHivHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setHivIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setHivIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setHivIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setHivIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setHivIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setHivIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setHivMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setHivNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setHivPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setHivGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setHivLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setHivCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setHivSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setHivDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setHivOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setHivOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapSchistosomiasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setSchistosomiasisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setSchistosomiasisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setSchistosomiasisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setSchistosomiasisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setSchistosomiasisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSchistosomiasisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSchistosomiasisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSchistosomiasisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setSchistosomiasisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setSchistosomiasisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setSchistosomiasisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setSchistosomiasisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setSchistosomiasisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setSchistosomiasisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setSchistosomiasisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setSchistosomiasisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setSchistosomiasisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setSchistosomiasisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setSchistosomiasisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSchistosomiasisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapSoilTransmittedHelminthsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSoilTransmittedHelminthsOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapTrypanosomiasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setTrypanosomiasisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setTrypanosomiasisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setTrypanosomiasisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setTrypanosomiasisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setTrypanosomiasisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setTrypanosomiasisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setTrypanosomiasisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setTrypanosomiasisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setTrypanosomiasisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setTrypanosomiasisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setTrypanosomiasisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrypanosomiasisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapDiarrheaDehydrationTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaDehydrationOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapDiarrheaBloodTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaBloodAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setDiarrheaBloodRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setDiarrheaBloodCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setDiarrheaBloodNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setDiarrheaBloodPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setDiarrheaBloodGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setDiarrheaBloodLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setDiarrheaBloodCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setDiarrheaBloodSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setDiarrheaBloodDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setDiarrheaBloodOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiarrheaBloodOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapSnakeBiteTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setSnakeBiteAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setSnakeBiteRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setSnakeBiteCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setSnakeBiteHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setSnakeBiteIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSnakeBiteIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSnakeBiteIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setSnakeBiteIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setSnakeBiteIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setSnakeBiteIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setSnakeBiteMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setSnakeBiteNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setSnakeBitePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBitePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setSnakeBiteGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setSnakeBiteLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setSnakeBiteCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setSnakeBiteSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setSnakeBiteDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setSnakeBiteOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setSnakeBiteOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapRubellaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setRubellaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setRubellaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setRubellaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setRubellaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setRubellaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setRubellaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRubellaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRubellaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRubellaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setRubellaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setRubellaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setRubellaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setRubellaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setRubellaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setRubellaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setRubellaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setRubellaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setRubellaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setRubellaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setRubellaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRubellaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    public void mapTuberculosisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setTuberculosisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setTuberculosisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setTuberculosisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setTuberculosisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setTuberculosisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTuberculosisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTuberculosisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTuberculosisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setTuberculosisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setTuberculosisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setTuberculosisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setTuberculosisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setTuberculosisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setTuberculosisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setTuberculosisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setTuberculosisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setTuberculosisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setTuberculosisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setTuberculosisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTuberculosisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapLeprosyTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setLeprosyAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setLeprosyAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setLeprosyRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setLeprosyCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setLeprosyHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setLeprosyIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLeprosyIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLeprosyIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLeprosyIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setLeprosyIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setLeprosyIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setLeprosyMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setLeprosyNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setLeprosyPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setLeprosyGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setLeprosyLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setLeprosyCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setLeprosySequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosySequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setLeprosyDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setLeprosyOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLeprosyOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapLymphaticFilariasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setLymphaticFilariasisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setLymphaticFilariasisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapBuruliUlcerTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setBuruliUlcerAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setBuruliUlcerRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setBuruliUlcerCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setBuruliUlcerHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setBuruliUlcerIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setBuruliUlcerIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setBuruliUlcerIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setBuruliUlcerIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setBuruliUlcerIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setBuruliUlcerIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setBuruliUlcerMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setBuruliUlcerNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setBuruliUlcerPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setBuruliUlcerGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setBuruliUlcerLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setBuruliUlcerCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setBuruliUlcerSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setBuruliUlcerDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setBuruliUlcerOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setBuruliUlcerOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPertussisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPertussisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPertussisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPertussisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPertussisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPertussisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPertussisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPertussisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPertussisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPertussisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPertussisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPertussisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPertussisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPertussisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPertussisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPertussisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPertussisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPertussisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPertussisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPertussisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPertussisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPertussisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapNeonatalTetanusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setNeonatalTetanusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setNeonatalTetanusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setNeonatalTetanusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setNeonatalTetanusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setNeonatalTetanusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setNeonatalTetanusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setNeonatalTetanusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setNeonatalTetanusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setNeonatalTetanusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setNeonatalTetanusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setNeonatalTetanusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setNeonatalTetanusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapOnchocerciasisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setOnchocerciasisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setOnchocerciasisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setOnchocerciasisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setOnchocerciasisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setOnchocerciasisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOnchocerciasisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOnchocerciasisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOnchocerciasisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setOnchocerciasisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setOnchocerciasisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setOnchocerciasisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setOnchocerciasisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setOnchocerciasisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setOnchocerciasisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setOnchocerciasisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setOnchocerciasisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setOnchocerciasisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setOnchocerciasisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setOnchocerciasisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOnchocerciasisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapDiphtheriaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setDiphteriaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setDiphteriaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setDiphteriaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setDiphteriaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setDiphteriaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiphteriaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiphteriaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiphteriaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setDiphteriaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setDiphteriaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setDiphteriaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setDiphteriaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setDiphteriaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setDiphteriaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setDiphteriaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setDiphteriaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setDiphteriaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setDiphteriaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setDiphteriaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setDiphteriaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapTrachomaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setTrachomaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setTrachomaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setTrachomaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setTrachomaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setTrachomaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setTrachomaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrachomaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrachomaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrachomaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setTrachomaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setTrachomaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setTrachomaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setTrachomaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setTrachomaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setTrachomaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setTrachomaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setTrachomaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setTrachomaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setTrachomaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setTrachomaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setTrachomaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapYawsEndemicSyphilisTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setYawsEndemicSyphilisOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapMaternalDeathsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setMaternalDeathsAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setMaternalDeathsRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setMaternalDeathsCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setMaternalDeathsHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setMaternalDeathsIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMaternalDeathsIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMaternalDeathsIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setMaternalDeathsIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setMaternalDeathsIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setMaternalDeathsIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setMaternalDeathsMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setMaternalDeathsNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setMaternalDeathsPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setMaternalDeathsGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setMaternalDeathsLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setMaternalDeathsCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setMaternalDeathsSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setMaternalDeathsDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setMaternalDeathsOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setMaternalDeathsOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPerinatalDeathsTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPerinatalDeathsAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPerinatalDeathsRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPerinatalDeathsCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPerinatalDeathsNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPerinatalDeathsPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPerinatalDeathsGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPerinatalDeathsLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPerinatalDeathsCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPerinatalDeathsSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPerinatalDeathsDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPerinatalDeathsOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPerinatalDeathsOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapInfluenzaATestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaAAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setInfluenzaARapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaARapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setInfluenzaACulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaACultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setInfluenzaAHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setInfluenzaAIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaAIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaAIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaAIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setInfluenzaAIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaAIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setInfluenzaAMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setInfluenzaANeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaANeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setInfluenzaAPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setInfluenzaAGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setInfluenzaALatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaALatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaACqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaACqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setInfluenzaASequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaASequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setInfluenzaADnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaADnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setInfluenzaAOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaAOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapInfluenzaBTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaBAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setInfluenzaBRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setInfluenzaBCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setInfluenzaBHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setInfluenzaBIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaBIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaBIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaBIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setInfluenzaBIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setInfluenzaBIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setInfluenzaBMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setInfluenzaBNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setInfluenzaBPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setInfluenzaBGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setInfluenzaBLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setInfluenzaBCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setInfluenzaBSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setInfluenzaBDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setInfluenzaBOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setInfluenzaBOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void maphMetapneumovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.sethMetapneumovirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.sethMetapneumovirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.sethMetapneumovirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.sethMetapneumovirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.sethMetapneumovirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.sethMetapneumovirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.sethMetapneumovirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.sethMetapneumovirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.sethMetapneumovirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.sethMetapneumovirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.sethMetapneumovirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.sethMetapneumovirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapRespiratorySyncytialVirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRespiratorySyncytialVirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapParainfluenza1_4TestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setParainfluenzaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setParainfluenzaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setParainfluenzaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setParainfluenzaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setParainfluenzaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setParainfluenzaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setParainfluenzaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setParainfluenzaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setParainfluenzaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setParainfluenzaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setParainfluenzaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setParainfluenzaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setParainfluenzaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setParainfluenzaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setParainfluenzaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setParainfluenzaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setParainfluenzaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setParainfluenzaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setParainfluenzaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setParainfluenzaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapAdenovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setAdenovirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setAdenovirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setAdenovirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setAdenovirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setAdenovirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAdenovirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAdenovirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAdenovirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setAdenovirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setAdenovirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setAdenovirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setAdenovirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setAdenovirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setAdenovirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setAdenovirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setAdenovirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setAdenovirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setAdenovirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setAdenovirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAdenovirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapRhinovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setRhinovirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setRhinovirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setRhinovirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setRhinovirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setRhinovirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRhinovirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRhinovirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setRhinovirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setRhinovirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setRhinovirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setRhinovirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setRhinovirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setRhinovirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setRhinovirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setRhinovirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setRhinovirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setRhinovirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setRhinovirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setRhinovirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setRhinovirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapEnterovirusTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setEnterovirusAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setEnterovirusRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setEnterovirusCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setEnterovirusHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setEnterovirusIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEnterovirusIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEnterovirusIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setEnterovirusIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setEnterovirusIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setEnterovirusIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setEnterovirusMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setEnterovirusNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setEnterovirusPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setEnterovirusGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setEnterovirusLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setEnterovirusCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setEnterovirusSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setEnterovirusDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setEnterovirusOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setEnterovirusOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapmPneumoniaeTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setmPneumoniaeAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setmPneumoniaeRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setmPneumoniaeCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setmPneumoniaeHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setmPneumoniaeIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setmPneumoniaeIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setmPneumoniaeIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setmPneumoniaeIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setmPneumoniaeIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setmPneumoniaeIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setmPneumoniaeMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setmPneumoniaeNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setmPneumoniaePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setmPneumoniaeGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setmPneumoniaeLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setmPneumoniaeCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setmPneumoniaeSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setmPneumoniaeDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setmPneumoniaeOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setmPneumoniaeOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapcPneumoniaeTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setcPneumoniaeAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setcPneumoniaeRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setcPneumoniaeCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setcPneumoniaeHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setcPneumoniaeIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setcPneumoniaeIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setcPneumoniaeIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setcPneumoniaeIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setcPneumoniaeIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setcPneumoniaeIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setcPneumoniaeMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setcPneumoniaeNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setcPneumoniaePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setcPneumoniaeGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setcPneumoniaeLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setcPneumoniaeCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setcPneumoniaeSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setcPneumoniaeDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setcPneumoniaeOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setcPneumoniaeOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapAriTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setAriAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setAriAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setAriRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setAriCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setAriHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setAriIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAriIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAriIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setAriIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setAriIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setAriIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setAriMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setAriNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setAriPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setAriGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setAriLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setAriCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setAriSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setAriDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setAriOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setAriOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapChikungunyaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setChikungunyaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setChikungunyaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setChikungunyaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setChikungunyaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setChikungunyaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setChikungunyaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setChikungunyaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setChikungunyaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setChikungunyaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setChikungunyaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setChikungunyaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setChikungunyaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setChikungunyaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setChikungunyaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setChikungunyaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setChikungunyaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setChikungunyaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setChikungunyaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setChikungunyaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setChikungunyaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPostImmunizationAdverseEventsMildTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsMildOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapPostImmunizationAdverseEventsSevereTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSeverePcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSeverePcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setPostImmunizationAdverseEventsSevereOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapFhaTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setFhaAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setFhaAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setFhaRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setFhaCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setFhaHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setFhaIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setFhaIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setFhaIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setFhaIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setFhaIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setFhaIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setFhaMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setFhaNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setFhaPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setFhaGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setFhaLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setFhaCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setFhaSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setFhaDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setFhaOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setFhaOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
    public void mapOtherTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setOtherAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setOtherAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setOtherRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setOtherCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setOtherHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setOtherIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOtherIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOtherIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setOtherIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setOtherIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setOtherIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setOtherMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setOtherNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setOtherPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setOtherGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setOtherLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setOtherCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setOtherSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setOtherDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setOtherOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setOtherOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }

    public void mapUndefinedTestsToSampleAndCase(CaseExportDetailedSampleDto embeddedDetailedSampleExportDto, PathogenTest pathogenTest) {
        switch (pathogenTest.getTestType().name()) {
            case "ANTIBODY_DETECTION":
                embeddedDetailedSampleExportDto.setUndefinedAntibodyDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "ANTIGEN_DETECTION":
                embeddedDetailedSampleExportDto.setUndefinedAntigenDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedAntibodyDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "RAPID_TEST":
                embeddedDetailedSampleExportDto.setUndefinedRapidTest(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedRapidTestDetails(pathogenTest.getTestResultText());
                break;
            case "CULTURE":
                embeddedDetailedSampleExportDto.setUndefinedCulture(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedCultureDetails(pathogenTest.getTestResultText());
                break;
            case "HISTOPATHOLOGY":
                embeddedDetailedSampleExportDto.setUndefinedHistopathology(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedHistopathologyDetails(pathogenTest.getTestResultText());
                break;
            case "ISOLATION":
                embeddedDetailedSampleExportDto.setUndefinedIsolation(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIsolationDetails(pathogenTest.getTestResultText());
                break;
            case "IGM_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUndefinedIgmSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIgmSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGG_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUndefinedIggSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIggSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "IGA_SERUM_ANTIBODY":
                embeddedDetailedSampleExportDto.setUndefinedIgaSerumAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIgaSerumAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "INCUBATION_TIME":
                embeddedDetailedSampleExportDto.setUndefinedIncubationTime(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIncubationTimeDetails(pathogenTest.getTestResultText());
                break;
            case "INDIRECT_FLUORESCENT_ANTIBODY":
                embeddedDetailedSampleExportDto.setUndefinedIndirectFluorescentAntibody(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedIndirectFluorescentAntibodyDetails(pathogenTest.getTestResultText());
                break;
            case "MICROSCOPY":
                embeddedDetailedSampleExportDto.setUndefinedMicroscopy(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedMicroscopyDetails(pathogenTest.getTestResultText());
                break;
            case "NEUTRALIZING_ANTIBODIES":
                embeddedDetailedSampleExportDto.setUndefinedNeutralizingAntibodies(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedNeutralizingAntibodiesDetails(pathogenTest.getTestResultText());
                break;
            case "PCR_RT_PCR":
                embeddedDetailedSampleExportDto.setUndefinedPcrRtPcr(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedPcrRtPcrDetails(pathogenTest.getTestResultText());
                break;
            case "GRAM_STAIN":
                embeddedDetailedSampleExportDto.setUndefinedGramStain(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedGramStainDetails(pathogenTest.getTestResultText());
                break;
            case "LATEX_AGGLUTINATION":
                embeddedDetailedSampleExportDto.setUndefinedLatexAgglutination(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedLatexAgglutinationDetails(pathogenTest.getTestResultText());
                break;
            case "CQ_VALUE_DETECTION":
                embeddedDetailedSampleExportDto.setUndefinedCqValueDetection(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedCqValueDetectionDetails(pathogenTest.getTestResultText());
                break;
            case "SEQUENCING":
                embeddedDetailedSampleExportDto.setUndefinedSequencing(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedSequencingDetails(pathogenTest.getTestResultText());
                break;
            case "DNA_MICROARRAY":
                embeddedDetailedSampleExportDto.setUndefinedDnaMicroarray(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedDnaMicroarrayDetails(pathogenTest.getTestResultText());
                break;
            case "OTHER":
                embeddedDetailedSampleExportDto.setUndefinedOther(pathogenTest.getTestResult().name());
                embeddedDetailedSampleExportDto.setUndefinedOtherDetails(pathogenTest.getTestResultText());
                break;
            default:
                break;
        }
    }
}
