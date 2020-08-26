package de.symeda.sormas.backend.util;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.utils.DateHelper;

public class CriteriaToJsonObject {

	public CriteriaToJsonObject() {

	}

	public CriteriaToJsonObject(CaseCriteria caseCriteria, int first, int max) {
		hashMapToJson(caseCriteria, first, max);
	}

	public String hashMapToJson(CaseCriteria caseCriteria, int first, int max) {
		Map<String, Object> mapObjectsParams = new HashMap<String, Object>();
		mapObjectsParams.put("maxRows", max);
		mapObjectsParams.put("firstRow", first);
		mapObjectsParams.put("caseDelete", caseCriteria.getDeleted());
		mapObjectsParams.put("caseArchive", caseCriteria.getRelevanceStatus().getValue(caseCriteria.getRelevanceStatus()));
		mapObjectsParams.put("reportingUser", caseCriteria.getSurveillanceOfficer() != null ? caseCriteria.getReportingUserLike() : "NULL");

		mapObjectsParams.put("caseDisease", caseCriteria.getDisease() != null ? caseCriteria.getDisease().getName() : "NULL");
		mapObjectsParams.put("caseOutcome", caseCriteria.getOutcome() != null ? caseCriteria.getOutcome().getName() : "NULL");
		mapObjectsParams.put("excludeSharedCases", caseCriteria.getExcludeSharedCases() != null ? caseCriteria.getExcludeSharedCases() : "NULL");

		mapObjectsParams.put("caseOrigin", caseCriteria.getCaseOrigin() != null ? caseCriteria.getCaseOrigin() : "NULL");
		mapObjectsParams.put("healthFacility", caseCriteria.getHealthFacility() != null ? caseCriteria.getHealthFacility().getUuid() : "NULL");

		mapObjectsParams
			.put("classification", caseCriteria.getCaseClassification() != null ? caseCriteria.getCaseClassification().getName() : "NULL");
		mapObjectsParams.put("investigationStatus", caseCriteria.getInvestigationStatus() != null ? caseCriteria.getInvestigationStatus() : "NULL");

		mapObjectsParams.put("presentCondition", caseCriteria.getPresentCondition() != null ? caseCriteria.getPresentCondition() : null); //Small null because of postgres 
		mapObjectsParams.put("region", caseCriteria.getRegion() != null ? caseCriteria.getRegion().getUuid() : "NULL");
		mapObjectsParams.put("district", caseCriteria.getDistrict() != null ? caseCriteria.getDistrict().getUuid() : "NULL");
		mapObjectsParams.put("subDistrict", caseCriteria.getCommunity() != null ? caseCriteria.getCommunity().getUuid() : "NULL");

		mapObjectsParams.put("pointofEntry", caseCriteria.getPointOfEntry() != null ? caseCriteria.getPointOfEntry().getUuid() : "NULL");

		mapObjectsParams
			.put("surveillanceOfficer", caseCriteria.getSurveillanceOfficer() != null ? caseCriteria.getSurveillanceOfficer().getUuid() : null);

		mapObjectsParams.put("reportedBy", caseCriteria.getReportingUserRole() != null ? caseCriteria.getReportingUserRole() : null);
		mapObjectsParams.put("quarantineEnd", caseCriteria.getQuarantineTo() != null ? caseCriteria.getQuarantineTo() : null);

		mapObjectsParams.put(
			"onlyCaseWithoutGeo",
			caseCriteria.getMustHaveNoGeoCoordinates() != null && caseCriteria.getMustHaveNoGeoCoordinates() == true
				? caseCriteria.getMustHaveNoGeoCoordinates()
				: null);

		mapObjectsParams.put(
			"onlyPortHealthCaseWithouthealthfacility",
			caseCriteria.getMustBePortHealthCaseWithoutFacility() != null ? caseCriteria.getMustBePortHealthCaseWithoutFacility() : null);
		mapObjectsParams
			.put("onlyCaseWithCaseManagementData", caseCriteria.getMustHaveCaseManagementData() != null ? caseCriteria.getDistrict() : "NULL");
		mapObjectsParams.put(
			"onlyCaseWithoutResponsibleOfficer",
			caseCriteria.getWithoutResponsibleOfficer() != null ? caseCriteria.getWithoutResponsibleOfficer() : "NULL");

		mapObjectsParams.put("filterOption", caseCriteria.getDateFilterOption() != null ? caseCriteria.getDateFilterOption().name() : "NULL");
		mapObjectsParams.put("refDateType", caseCriteria.getNewCaseDateType() != null ? caseCriteria.getNewCaseDateType() : "NULL");
		mapObjectsParams
			.put("newCaseFrom", caseCriteria.getNewCaseDateFrom() != null ? DateHelper.getStartOfDay(caseCriteria.getNewCaseDateFrom()) : "NULL");
		mapObjectsParams
			.put("newCaseTo", caseCriteria.getNewCaseDateTo() != null ? DateHelper.getStartOfDay(caseCriteria.getNewCaseDateTo()) : "NULL");

		mapObjectsParams.put(
			"creationDateFrom",
			caseCriteria.getCreationDateFrom() != null ? DateHelper.getStartOfDay(caseCriteria.getCreationDateFrom()) : "NULL");
		mapObjectsParams
			.put("creationDateTo", caseCriteria.getCreationDateTo() != null ? DateHelper.getStartOfDay(caseCriteria.getCreationDateTo()) : "NULL");
		mapObjectsParams.put("personUuid", caseCriteria.getPerson() != null ? caseCriteria.getPerson().getUuid() : "NULL");

		JsonObject payload = Json.createObjectBuilder(mapObjectsParams).build();
		String object = payload.asJsonObject().toString();
		return object;
	}

}
