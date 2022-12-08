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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.statistics.caze;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.IntegerRange;
import de.symeda.sormas.api.Month;
import de.symeda.sormas.api.MonthOfYear;
import de.symeda.sormas.api.Quarter;
import de.symeda.sormas.api.QuarterOfYear;
import de.symeda.sormas.api.Year;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseFacade;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.disease.DiseaseConfigurationFacade;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictFacade;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionFacade;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsAttributeEnum;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.StatisticsSubAttributeEnum;
import de.symeda.sormas.api.user.UserRoleFacade;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.EpiWeek;

public class StatisticsHelper extends de.symeda.sormas.api.statistics.StatisticsHelper {

	public static final String VALUE_UNKNOWN = "VALUE_UNKNOWN";
	public static final String TOTAL = "total";
	public static final String UNKNOWN = "unknown";
	


//	public static StatisticsGroupingKey buildGroupingKey(Object attributeValue, StatisticsCaseAttributeEnum attribute, StatisticsCaseSubAttribute subAttribute, Function<Integer, RegionReferenceDto> regionProvider, Function<Integer, DistrictReferenceDto> districtProvider) {
//		if (isNullOrUnknown(attributeValue)) {
//			return null;
//		}//to be uncommented
//		
//		switch (attribute) {
//			case CLASSIFICATION:
//				return CaseClassification.valueOf(attributeValue.toString());
//			case OUTCOME:
//				return CaseOutcome.valueOf(attributeValue.toString());
//			default:
//				//return buildGroupingKey(attributeValue, getEnum(attribute), subAttribute, regionProvider, districtProvider);
//				return buildGroupingKey(attributeValue, attribute, subAttribute, regionProvider, districtProvider);
//
//		}
//	}
	
	
	


	public static boolean isNullOrUnknown(Object value) {

		if (value == null) {
			return true;
		}
		if (value instanceof IntegerRange && ((IntegerRange) value).getFrom() == null && ((IntegerRange) value).getTo() == null) {
			return true;
		}

		return isUnknown(value);
	}

//	public static List<StatisticsGroupingKey> getTimeGroupingKeys(StatisticsCaseAttributeEnum attribute, StatisticsCaseSubAttributeEnum subAttribute) {
//		
//		Date oldestCaseDate = null;
//		switch (attribute) {
//			case ONSET_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseOnsetDate();
//				break;
//			case REPORT_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseReportDate();
//				break;
//		}
//
//		return getTimeGroupingKeys(subAttribute, oldestCaseDate);
//	}
	


	



//	@SuppressWarnings("unchecked")
//	public static List<StatisticsGroupingKey> getAttributeGroupingKeys(StatisticsCaseAttributeEnum attribute, StatisticsCaseSubAttributeEnum subAttribute) {
//
//		if (subAttribute != null) {
//			switch (attribute) {
//				case REPORT_TIME:
//				case ONSET_TIME:
//					return getTimeGroupingKeys(attribute, subAttribute);
//				default:
//					return getAttributeGroupingKeys(attribute, subAttribute);
//
//					//return getAttributeGroupingKeys(getEnum(attribute), subAttribute);
//			}
//		}
//		else {
//			switch (attribute) {
//				case CLASSIFICATION: {
//					ArrayList<StatisticsGroupingKey> classificationList = new ArrayList<>();
//					for (CaseClassification classification : CaseClassification.values()) {
//						classificationList.add(classification);
//					}
//					return classificationList;
//				}
//				case OUTCOME:
//					ArrayList<StatisticsGroupingKey> outcomeList = new ArrayList<>();
//					for (CaseOutcome outcome : CaseOutcome.values()) {
//						outcomeList.add(outcome);
//					}
//					return outcomeList;
//				default:
//					//return getAttributeGroupingKeys(getEnum(attribute), subAttribute);
//					return getAttributeGroupingKeys(attribute, subAttribute);
//
//			}
//		}
//	}
	
	
	
	

	
	

//	public static StatisticsCaseSubAttribute getEnum (StatisticsCaseSubAttribute attribute) {
//		return (StatisticsCaseSubAttribute) StatisticsCaseSubAttribute.getEnum(attribute);
//	}
	
//	public static StatisticsCaseSubAttribute getEnum (StatisticsSubAttribute attribute) {
//		return (StatisticsCaseSubAttribute) StatisticsSubAttribute.getEnum(attribute);
//	}
	
	

	
	public static class getAttributeValues implements StatisticsCaseAttribute.IValuesGetter {
		
		public Collection<? extends StatisticsGroupingKey> get (StatisticsCaseAttribute attribute) {
			return getAttributeValues(attribute);
		}
	}


	
	
	
	




//	public static Collection<? extends StatisticsGroupingKey> getTimeGroupingKeys(StatisticsCaseAttributeEnum attribute,
//			StatisticsCaseAttributeEnum subAttribute) {
//		Date oldestCaseDate = null;
//		switch (attribute) {
//			case ONSET_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseOnsetDate();
//				break;
//			case REPORT_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseReportDate();
//				break;
//		}
//
//		return getTimeGroupingKeys(attribute, oldestCaseDate);
//	}
//
//
//
//
//
//
//	private static Collection<? extends StatisticsGroupingKey> getTimeGroupingKeys(
//			StatisticsCaseAttributeEnum attribute, Date oldestCaseDate) {
//		switch (attribute) {
//			case ONSET_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseOnsetDate();
//				break;
//			case REPORT_TIME:
//				oldestCaseDate = FacadeProvider.getCaseFacade().getOldestCaseReportDate();
//				break;
//		}
//
//		return getTimeGroupingKeys(attribute, oldestCaseDate);
//	}



	public static StatisticsSubAttributeEnum getEnum(StatisticsAttribute attribute) {
		return (StatisticsSubAttributeEnum) StatisticsAttribute.getEnum(attribute);
	}



	public static StatisticsSubAttributeEnum getEnum (StatisticsSubAttribute attribute) {
		return (StatisticsSubAttributeEnum) StatisticsSubAttribute.getEnum(attribute);
	}
	
//	public static StatisticsCaseAttributeEnum getEnum (StatisticsCaseAttributeEnum attribute) {
//		return attributesMap.get(attribute);
//	}
	
//	rowKey,
//	rowGrouping,
//	rowSubGrouping,
//	regionProvider,
//	districtProvider,
//	communityProvider,
//	healthFacilityProvider,
//	userRoleProvider
	
	public static StatisticsGroupingKey buildGroupingKey(
			Object attributeValue,
			StatisticsCaseAttributeEnum attribute,
			StatisticsCaseSubAttributeEnum subAttribute,
			Function<Integer, RegionReferenceDto> regionProvider,
			Function<Integer, DistrictReferenceDto> districtProvider,
			Function<Integer, CommunityReferenceDto> communityProvider,
			Function<Integer, FacilityReferenceDto> facilityProvider,
			Function<Integer, UserRoleReferenceDto> userRoleProvider) {

			if (isNullOrUnknown(attributeValue)) {
				return null;
			}

			if (subAttribute != null) {
				switch (subAttribute) {
				case YEAR:
					return new Year((int) attributeValue);
				case QUARTER:
					return new Quarter((int) attributeValue);
				case MONTH:
					return Month.values()[(int) attributeValue - 1];
				case EPI_WEEK:
					return new EpiWeek(null, (int) attributeValue);
				case QUARTER_OF_YEAR:
					String entryAsString = String.valueOf(attributeValue);
					return new QuarterOfYear(
						new Quarter(Integer.valueOf(entryAsString.substring(entryAsString.length() - 1))),
						new Year(Integer.valueOf(entryAsString.substring(0, entryAsString.length() - 1))));
				case MONTH_OF_YEAR:
					entryAsString = String.valueOf(attributeValue);
					return new MonthOfYear(
						Month.values()[Integer.valueOf(entryAsString.substring(entryAsString.length() - 2)) - 1],
						new Year(Integer.valueOf(entryAsString.substring(0, entryAsString.length() - 2))));
				case EPI_WEEK_OF_YEAR:
					entryAsString = String.valueOf(attributeValue);
					return new EpiWeek(
						Integer.valueOf(entryAsString.substring(0, entryAsString.length() - 2)),
						Integer.valueOf(entryAsString.substring(entryAsString.length() - 2)));
				case REGION:
					return regionProvider.apply(((Number) attributeValue).intValue());
				case DISTRICT:
					return districtProvider.apply(((Number) attributeValue).intValue());
				case COMMUNITY:
					return communityProvider.apply(((Number) attributeValue).intValue());
				case FACILITY:
					return facilityProvider.apply(((Number) attributeValue).intValue());
				default:
					throw new IllegalArgumentException(subAttribute.toString());
				}
			} else {
				switch (attribute) {
				case DISEASE:
					return Disease.valueOf(attributeValue.toString());
				case SEX:
					return Sex.valueOf(attributeValue.toString());
				case CLASSIFICATION:
					return CaseClassification.valueOf(attributeValue.toString());
				case OUTCOME:
					return CaseOutcome.valueOf(attributeValue.toString());
				case AGE_INTERVAL_1_YEAR:
				case AGE_INTERVAL_5_YEARS:
				case AGE_INTERVAL_CHILDREN_COARSE:
				case AGE_INTERVAL_CHILDREN_FINE:
				case AGE_INTERVAL_CHILDREN_MEDIUM:
				case AGE_INTERVAL_BASIC:
					String entryAsString = attributeValue.toString();
					if (attribute == StatisticsCaseAttributeEnum.AGE_INTERVAL_5_YEARS) {
						try {
							AgeGroup ageGroup = AgeGroup.valueOf(entryAsString);
							return ageGroup.toIntegerRange();
						} catch (IllegalArgumentException e) {
							// This is fine; continue to build the IntegerGroup based on the entry string
						}
					}

					if (entryAsString.contains("-")) {
						return new IntegerRange(
							Integer.valueOf(entryAsString.substring(0, entryAsString.indexOf("-"))),
							Integer.valueOf(entryAsString.substring(entryAsString.indexOf("-") + 1)));
					} else if (entryAsString.contains("+")) {
						return new IntegerRange(Integer.valueOf(entryAsString.substring(0, entryAsString.indexOf("+"))), null);
					} else {
						return new IntegerRange(Integer.valueOf(entryAsString), Integer.valueOf(entryAsString));
					}
				case REPORTING_USER_ROLE:
					return userRoleProvider.apply(((Number) attributeValue).intValue());
				default:
					throw new IllegalArgumentException(attribute.toString());
				}
			}
		}
	
	
	@SuppressWarnings("unchecked")
	public static List<StatisticsGroupingKey> getAttributeGroupingKeys(
		StatisticsCaseAttributeEnum attribute,
		StatisticsCaseSubAttributeEnum subAttribute,
		DiseaseConfigurationFacade diseaseConfigurationFacade,
		CaseFacade caseFacade,
		RegionFacade regionFacade,
		DistrictFacade districtFacade,
		UserRoleFacade userRoleFacade) {

		if (subAttribute != null) {
			switch (subAttribute) {
			case YEAR:
			case QUARTER:
			case MONTH:
			case EPI_WEEK:
			case QUARTER_OF_YEAR:
			case MONTH_OF_YEAR:
			case EPI_WEEK_OF_YEAR:
				return StatisticsHelper.getTimeGroupingKeys(attribute, subAttribute, caseFacade);
			case REGION:
				return (List<StatisticsGroupingKey>) (List<? extends StatisticsGroupingKey>) regionFacade.getAllActiveByServerCountry();
			case DISTRICT:
				return (List<StatisticsGroupingKey>) (List<? extends StatisticsGroupingKey>) districtFacade.getAllActiveAsReference();
			case COMMUNITY:
			case FACILITY:
				return new ArrayList<>();
			default:
				throw new IllegalArgumentException(subAttribute.toString());
			}
		} else {
			switch (attribute) {
			case SEX:
				return toGroupingKeys(Sex.values());
			case DISEASE:
				return toGroupingKeys(diseaseConfigurationFacade.getAllDiseases(true, true, true));
			case CLASSIFICATION:
				return toGroupingKeys(CaseClassification.values());
			case OUTCOME:
				return toGroupingKeys(CaseOutcome.values());
			case AGE_INTERVAL_1_YEAR:
			case AGE_INTERVAL_5_YEARS:
			case AGE_INTERVAL_CHILDREN_COARSE:
			case AGE_INTERVAL_CHILDREN_FINE:
			case AGE_INTERVAL_CHILDREN_MEDIUM:
			case AGE_INTERVAL_BASIC:
				return StatisticsHelper.getAgeIntervalGroupingKeys(attribute);
			case REPORTING_USER_ROLE:
				return (List<StatisticsGroupingKey>) (List<? extends StatisticsGroupingKey>) userRoleFacade.getAllAsReference();
			default:
				throw new IllegalArgumentException(attribute.toString());
			}
		}
	}
	
	
	
	
	
	public static List<StatisticsGroupingKey> getAgeIntervalGroupingKeys(StatisticsCaseAttributeEnum attribute) {

		List<StatisticsGroupingKey> ageIntervalList = new ArrayList<>();
		switch (attribute) {
		case AGE_INTERVAL_1_YEAR:
			for (int i = 0; i < 80; i++) {
				ageIntervalList.add(new IntegerRange(i, i));
			}
			break;
		case AGE_INTERVAL_5_YEARS:
			for (int i = 0; i < 80; i += 5) {
				ageIntervalList.add(new IntegerRange(i, i + 4));
			}
			break;
		case AGE_INTERVAL_CHILDREN_COARSE:
			ageIntervalList.add(new IntegerRange(0, 14));
			for (int i = 15; i < 30; i += 5) {
				ageIntervalList.add(new IntegerRange(i, i + 4));
			}
			for (int i = 30; i < 80; i += 10) {
				ageIntervalList.add(new IntegerRange(i, i + 9));
			}
			break;
		case AGE_INTERVAL_CHILDREN_FINE:
			for (int i = 0; i < 5; i++) {
				ageIntervalList.add(new IntegerRange(i, i));
			}
			for (int i = 5; i < 30; i += 5) {
				ageIntervalList.add(new IntegerRange(i, i + 4));
			}
			for (int i = 30; i < 80; i += 10) {
				ageIntervalList.add(new IntegerRange(i, i + 9));
			}
			break;
		case AGE_INTERVAL_CHILDREN_MEDIUM:
			for (int i = 0; i < 30; i += 5) {
				ageIntervalList.add(new IntegerRange(i, i + 4));
			}
			for (int i = 30; i < 80; i += 10) {
				ageIntervalList.add(new IntegerRange(i, i + 9));
			}
			break;
		case AGE_INTERVAL_BASIC:
			ageIntervalList.add(new IntegerRange(0, 0));
			ageIntervalList.add(new IntegerRange(1, 4));
			ageIntervalList.add(new IntegerRange(5, 14));
			ageIntervalList.add(new IntegerRange(15, null));
			break;
		default:
			throw new IllegalArgumentException(attribute.toString());
		}

		if (attribute != StatisticsCaseAttributeEnum.AGE_INTERVAL_BASIC) {
			ageIntervalList.add(new IntegerRange(80, null));
		}
		ageIntervalList.add(new IntegerRange(null, null));
		return ageIntervalList;
	}

	
	public static List<StatisticsGroupingKey> getTimeGroupingKeys(
			StatisticsCaseAttributeEnum attribute,
			StatisticsCaseSubAttributeEnum subAttribute,
			CaseFacade caseFacade) {

			Date oldestCaseDate = null;
			switch (attribute) {
			case ONSET_TIME:
				oldestCaseDate = caseFacade.getOldestCaseOnsetDate();
				break;
			case REPORT_TIME:
				oldestCaseDate = caseFacade.getOldestCaseReportDate();
				break;
			case OUTCOME_TIME:
				oldestCaseDate = caseFacade.getOldestCaseOutcomeDate();
				break;
			default:
				return new ArrayList<>();
			}

			LocalDate earliest = oldestCaseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate now = LocalDate.now();

			switch (subAttribute) {
			case YEAR:
				List<StatisticsGroupingKey> years = new ArrayList<>();
				for (int i = earliest.getYear(); i <= now.getYear(); i++) {
					years.add(new Year(i));
				}
				return years;
			case QUARTER:
				List<StatisticsGroupingKey> quarters = new ArrayList<>();
				for (int i = 1; i <= 4; i++) {
					quarters.add(new Quarter(i));
				}
				return quarters;
			case MONTH:
				return toGroupingKeys(Month.values());
			case EPI_WEEK:
				List<StatisticsGroupingKey> epiWeeks = new ArrayList<>();
				for (int i = 1; i <= DateHelper.getMaximumEpiWeekNumber(); i++) {
					epiWeeks.add(new EpiWeek(null, i));
				}
				return epiWeeks;
			case QUARTER_OF_YEAR:
				List<StatisticsGroupingKey> quarterOfYearList = new ArrayList<>();
				QuarterOfYear earliestQuarter = new QuarterOfYear(new Quarter(1), new Year(earliest.getYear()));
				QuarterOfYear latestQuarter = new QuarterOfYear(new Quarter(4), new Year(now.getYear()));
				while (earliestQuarter.getYear().getValue() <= latestQuarter.getYear().getValue()) {
					quarterOfYearList.add(new QuarterOfYear(earliestQuarter.getQuarter(), earliestQuarter.getYear()));
					earliestQuarter.increaseQuarter();
				}
				return quarterOfYearList;
			case MONTH_OF_YEAR:
				List<StatisticsGroupingKey> monthOfYearList = new ArrayList<>();
				for (int year = earliest.getYear(); year <= now.getYear(); year++) {
					for (Month month : Month.values()) {
						monthOfYearList.add(new MonthOfYear(month, year));
					}
				}
				return monthOfYearList;
			case EPI_WEEK_OF_YEAR:
				List<StatisticsGroupingKey> epiWeekOfYearList = new ArrayList<>();
				for (int year = earliest.getYear(); year <= now.getYear(); year++) {
					epiWeekOfYearList.addAll(DateHelper.createEpiWeekList(year));
				}
				return epiWeekOfYearList;
			default:
				return new ArrayList<>();
			}
		}
	
}
