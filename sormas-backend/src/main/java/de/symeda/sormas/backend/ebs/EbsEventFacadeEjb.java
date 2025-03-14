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
package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.caze.MapCaseDto;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.ebs.EbsDetailedReferenceDto;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsEventDto;
import de.symeda.sormas.api.ebs.EbsEventFacade;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.ebs.TriagingDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.externalsurveillancetool.ExternalSurveillanceToolGatewayFacadeEjb.ExternalSurveillanceToolGatewayFacadeEjbLocal;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb.CommunityFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.country.CountryFacadeEjb.CountryFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb.DistrictFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.location.LocationFacadeEjb;
import de.symeda.sormas.backend.location.LocationFacadeEjb.LocationFacadeEjbLocal;
import de.symeda.sormas.backend.share.ExternalShareInfoService;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeEjb.SormasToSormasFacadeEjbLocal;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfoService;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.Pseudonymizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static java.util.Objects.isNull;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(name = "EbsEventFacade")
public class EbsEventFacadeEjb implements EbsEventFacade {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private UserService userService;
	@EJB
	private LocationFacadeEjbLocal locationFacade;
	@EJB
	private SormasToSormasOriginInfoService originInfoService;
	@EJB
	private CountryFacadeEjbLocal countryFacade;
	@EJB
	private DistrictFacadeEjbLocal districtFacade;
	@EJB
	private CommunityFacadeEjbLocal communityFacade;
	@EJB
	private FeatureConfigurationFacadeEjbLocal featureConfigurationFacade;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolFacade;
	@EJB
	private ExternalShareInfoService externalShareInfoService;
	@EJB
	private SormasToSormasFacadeEjbLocal sormasToSormasFacade;

	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private EbsService ebsService;
	@EJB
	private ExternalSurveillanceToolGatewayFacadeEjbLocal externalSurveillanceToolGatewayFacade;
	@EJB
	private TriagingFacadeEjb.TriagingFacadeEjbLocal triagingFacade;
	@EJB
	private SignalVerificationFacadeEjb.SignalVerificationFacadeEjbLocal signalVerificationFacade;


	public EbsEventFacadeEjb() {
	}

	@Inject
	public EbsEventFacadeEjb(EbsService service, UserService userService) {
		super();
	}

	public static EbsReferenceDto toReferenceDto(Ebs entity) {

		if (entity == null) {
			return null;
		}

		return new EbsReferenceDto(entity.getUuid(), getCaption(entity));
	}

	private static String getCaption(Ebs entity) {
		return EbsReferenceDto.buildCaption(entity.getTriageDate());
	}

	public static EbsReferenceDto toDetailedReferenceDto(Ebs entity) {

		if (entity == null) {
			return null;
		}

		return new EbsDetailedReferenceDto(entity.getUuid(), getCaption(entity), entity.getReportDateTime());
	}

	public static EbsDto toEbsDto(Ebs source) {

		if (source == null) {
			return null;
		}
		EbsDto target = new EbsDto();
		DtoHelper.fillDto(target, source);

		target.setInformantName(source.getInformantName());
		target.setInformantTel(source.getInformantTel());
		target.setReportDateTime(source.getReportDateTime());
		target.setCategoryOfInformant((source.getCategoryOfInformant()));
		target.setEbsLocation(LocationFacadeEjb.toDto(source.getEbsLocation()));
		target.setAutomaticScanningType(source.getAutomaticScanningType());
		target.setManualScanningType(source.getManualScanningType());
		target.setScanningType(source.getScanningType());
		target.setDescriptionOccurrence(source.getDescriptionOccurrence());
		target.setOther(source.getOther());
		target.setPersonDesignation(source.getPersonDesignation());
		target.setPersonPhone(source.getPersonPhone());
		target.setPersonRegistering(source.getPersonRegistering());
		target.setSourceName(source.getSourceName());
		target.setSourceInformation(source.getSourceInformation());

		target.setDateOnset(source.getDateOnset());
		target.setEbsLongitude(source.getEbsLongitude());
		target.setEbsLatitude(source.getEbsLongitude());
		target.setEbsLatLon(source.getEbsLatLon());
		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());
		if (source.getTriaging() != null) {
			target.setTriaging(TriagingFacadeEjb.toDto(source.getTriaging()));
		}
		if (source.getSignalVerification() != null) {
			target.setSignalVerification(SignalVerificationFacadeEjb.toDto(source.getSignalVerification()));
		}
		target.setOtherInformant(source.getOtherInformant());
		return target;
	}






	private List<Selection<?>> sortBy(List<SortProperty> sortProperties, EbsQueryContext ebsQueryContext) {
		List<Selection<?>> selections = new ArrayList<>();
		CriteriaBuilder cb = ebsQueryContext.getCriteriaBuilder();
		CriteriaQuery<?> cq = ebsQueryContext.getQuery();
		Root<Ebs> ebs = cq.from(Ebs.class);

		if (sortProperties != null && !sortProperties.isEmpty()) {
			EbsJoins ebsJoins = ebsQueryContext.getJoins();
			Join<Ebs, Triaging> triaging = ebsJoins.getTriaging();
			Join<Ebs, SignalVerification> signalVerification = ebsJoins.getSignalVerification();
			Join<Ebs, RiskAssessment> riskAssessment = ebs.join("riskAssessment", JoinType.LEFT);
			Join<Ebs, EbsAlert> ebsAlert = ebs.join("ebsAlert", JoinType.LEFT);
			Join<Ebs, Location> location = ebsJoins.getLocation();
			Join<Location, Region> region = ebsJoins.getRegion();
			Join<Location, District> district = ebsJoins.getDistrict();
			Join<Location, Community> community = ebsJoins.getCommunity();

			List<Order> order = new ArrayList<>(sortProperties.size());

			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
				case EbsIndexDto.UUID:
				case EbsIndexDto.TRIAGE_DATE:
				case EbsIndexDto.INFORMANT_NAME:
				case EbsIndexDto.INFORMANT_TEL:
				case EbsIndexDto.SOURCE_INFORMATION:
				case EbsIndexDto.REPORT_DATE_TIME:
				case EbsIndexDto.CATEGORY_OF_INFORMANT:
				case EbsIndexDto.PERSON_REGISTERING:
				case EbsIndexDto.PERSON_DESIGNATION:
					expression = ebsQueryContext.getRoot().get(sortProperty.propertyName);
					break;
				case EbsIndexDto.TRIAGING_DECISION_DATE:
					expression = triaging.get(Triaging.DATE_OF_DECISION);
					break;
				case EbsIndexDto.VERIFICATION_SENT:
					expression = signalVerification.get(SignalVerification.VERIFICATION_SENT);
					break;
				case EbsIndexDto.VERIFIED_DATE:
					expression = signalVerification.get(SignalVerification.VERIFICATION_COMPLETE_DATE);
					break;
				case EbsIndexDto.RISK_STATUS:
					expression = riskAssessment.get(RiskAssessment.RISK_ASSESSMENT);
					break;
				case EbsIndexDto.ACTION_INITIATED:
					expression = ebsAlert.get(EbsAlert.ACTION_INITIATED);
					break;
				case EbsIndexDto.RESPONSE_STATUS:
					expression = ebsAlert.get(EbsAlert.RESPONSE_STATUS);
					break;
				case EbsIndexDto.VERIFIED:
					expression = signalVerification.get(SignalVerification.VERIFIED);
					break;
				case EbsIndexDto.SIGNAL_CATEGORY:
					expression = triaging.get(Triaging.SIGNAL_CATEGORY);
					break;
				case EbsIndexDto.TRIAGING_DECISION:
					expression = triaging.get(Triaging.TRIAGING_DECISION);
					break;
				case EbsIndexDto.DEATH:
					expression = signalVerification.get(SignalVerification.NUMBER_OF_DEATH);
					break;
				case EbsIndexDto.EBS_LOCATION:
					expression = region.get(Region.NAME);
					selections.add(expression);
					order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
					expression = community.get(Community.NAME);
					break;
				case EbsIndexDto.REGION:
					expression = region.get(Region.NAME);
					break;
				case EbsIndexDto.DISTRICT:
					expression = district.get(District.NAME);
					break;
				case EbsIndexDto.COMMUNITY:
					expression = community.get(Community.NAME);
					break;
				case EbsIndexDto.TOWN:
					expression = location.get(Location.CITY);
					selections.add(expression);
					order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
					break;
				default:
					throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
				selections.add(expression);
			}
			cq.orderBy(order);
		} else {
			Path<Object> changeDate = ebsQueryContext.getRoot().get(Ebs.CHANGE_DATE);
			cq.orderBy(cb.desc(changeDate));
			selections.add(changeDate);
		}

		return selections;
	}




	public Ebs fillOrBuildEntity(@NotNull EbsDto source, Ebs target, boolean checkChangeDate) {
		boolean targetWasNull = isNull(target);
		target = DtoHelper.fillOrBuildEntity(source, target, Ebs::new, checkChangeDate);

		target.setInformantName(source.getInformantName());
		target.setInformantTel(source.getInformantTel());
		target.setEndDate(source.getEndDate());
		target.setReportDateTime(source.getReportDateTime());
		target.setCategoryOfInformant(source.getCategoryOfInformant());
		target.setEbsLocation(locationFacade.fromDto(source.getEbsLocation(), checkChangeDate));
		target.setResponsibleUser(userService.getByReferenceDto(source.getResponsibleUser()));
		target.setDeleted(source.isDeleted());
		target.setDeletionReason(source.getDeletionReason());
		target.setOtherDeletionReason(source.getOtherDeletionReason());
		target.setEbsLatLon(source.getEbsLatLon());
		target.setAutomaticScanningType(source.getAutomaticScanningType());
		target.setManualScanningType(source.getManualScanningType());
		target.setScanningType(source.getScanningType());
		target.setDescriptionOccurrence(source.getDescriptionOccurrence());
		target.setOther(source.getOther());
		target.setPersonDesignation(source.getPersonDesignation());
		target.setPersonPhone(source.getPersonPhone());
		target.setPersonRegistering(source.getPersonRegistering());
		target.setSourceName(source.getSourceName());
		target.setSourceInformation(source.getSourceInformation());
		target.setDateOnset(source.getDateOnset());
		target.setEbsLongitude(source.getEbsLongitude());
		target.setEbsLatitude(source.getEbsLongitude());
		if (source.getTriaging() == null) {
			source.setTriaging(TriagingDto.build());
		}
		target.setTriaging(triagingFacade.fillOrBuildEntity(source.getTriaging(), target.getTriaging(), false));
		if (source.getSignalVerification() == null) {
			source.setSignalVerification(SignalVerificationDto.build());
		}
		target
			.setSignalVerification(signalVerificationFacade.fillOrBuildEntity(source.getSignalVerification(), target.getSignalVerification(), false));
		target.setOtherInformant(source.getOtherInformant());
		return target;
	}

	@Override
	public List<EbsEvent> getAllEbsEvents() {
		return Arrays.asList(EbsEvent.values());
	}

	@Override
	public List<PersonReporting> getPersonReportingByEventSourceType(EbsSourceType srcType) {
		return ebsService.getPersonReportingByEventSourceType(srcType);
	}

	@Override
	public List<EbsEventDto> getEbsEventForMap(RegionReferenceDto regionRef, DistrictReferenceDto districtRef, EbsEvent ebsEvent, Date from, Date to, NewCaseDateType dateType) {

		Region region = regionService.getByReferenceDto(regionRef);
		District district = districtService.getByReferenceDto(districtRef);

        return ebsService.getEbsEventForMap(region, district, ebsEvent, from, to, dateType);
	}

	@LocalBean
	@Stateless
	public static class EbsEventFacadeEjbLocal extends EbsEventFacadeEjb {

		public EbsEventFacadeEjbLocal() {
		}

		@Inject
		public EbsEventFacadeEjbLocal(EbsService service, UserService userService) {
			super(service, userService);
		}
	}

}
