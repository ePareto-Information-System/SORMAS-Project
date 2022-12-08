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
package de.symeda.sormas.api.statistics.contact;

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
import de.symeda.sormas.api.contact.ContactClassification;
import de.symeda.sormas.api.contact.ContactFacade;
import de.symeda.sormas.api.contact.ContactStatus;
import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.StatisticsSubAttributeEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttribute;
import de.symeda.sormas.api.user.UserRoleFacade;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.EpiWeek;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.disease.DiseaseConfigurationFacade;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictFacade;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionFacade;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.Sex;

public class StatisticsHelper extends de.symeda.sormas.api.statistics.StatisticsHelper {

	public static final String VALUE_UNKNOWN = "VALUE_UNKNOWN";
	public static final String TOTAL = "total";
	public static final String UNKNOWN = "unknown";
	
//	private static final Map<StatisticsContactAttributeEnum, StatisticsContactAttributeEnum> attributesMap = new HashMap<StatisticsContactAttributeEnum, StatisticsContactAttributeEnum> () {{
//		put(StatisticsContactAttributeEnum.REPORT_TIME, StatisticsContactAttributeEnum.TIME);
//		put(StatisticsContactAttributeEnum.REGION_DISTRICT, StatisticsContactAttributeEnum.REGION_DISTRICT);
//		put(StatisticsContactAttributeEnum.SEX, StatisticsContactAttributeEnum.SEX);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_1_YEAR, StatisticsContactAttributeEnum.AGE_INTERVAL_1_YEAR);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_5_YEARS, StatisticsContactAttributeEnum.AGE_INTERVAL_5_YEARS);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_COARSE, StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_COARSE);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_FINE, StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_FINE);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_MEDIUM, StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_MEDIUM);
//		put(StatisticsContactAttributeEnum.AGE_INTERVAL_BASIC, StatisticsContactAttributeEnum.AGE_INTERVAL_BASIC);
//		put(StatisticsContactAttributeEnum.DISEASE, StatisticsContactAttributeEnum.DISEASE);
//		put(StatisticsContactAttributeEnum.CLASSIFICATION, null);
//		put(StatisticsContactAttributeEnum.FOLLOW_UP_STATUS, null);
//		put(StatisticsContactAttributeEnum.REPORTING_USER_ROLE, StatisticsContactAttributeEnum.USER_ROLE);
//	}};
	
//	public static StatisticsGroupingKey buildGroupingKey(Object attributeValue, StatisticsContactAttributeEnum attribute, StatisticsContactSubAttributeEnum columnSubGrouping, Function<Integer, RegionReferenceDto> regionProvider, Function<Integer, DistrictReferenceDto> districtProvider) {
//		if (isNullOrUnknown(attributeValue)) {
//			return null;
//		}
//		
//		switch (attribute) {
//			case CLASSIFICATION:
//				return ContactClassification.valueOf(attributeValue.toString());
//			case FOLLOW_UP_STATUS:
//				return FollowUpStatus.valueOf(attributeValue.toString());
//			default:
//				return null;
//				//return buildGroupingKey(attributeValue, getEnum(attribute), subAttribute, regionProvider, districtProvider);
//		}
//	}
	
	
	public static StatisticsGroupingKey buildGroupingKey(
			Object attributeValue,
			StatisticsContactAttributeEnum attribute,
			StatisticsContactSubAttributeEnum subAttribute,
			Function<Integer, RegionReferenceDto> regionProvider,
			Function<Integer, DistrictReferenceDto> districtProvider,
			Function<Integer, CommunityReferenceDto> communityProvider,
			Function<Integer, FacilityReferenceDto> healthFacilityProvider,
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
					return ContactClassification.valueOf(attributeValue.toString());
				case STATUS:
					return ContactStatus.valueOf(attributeValue.toString());
				case AGE_INTERVAL_1_YEAR:
				case AGE_INTERVAL_5_YEARS:
				case AGE_INTERVAL_CHILDREN_COARSE:
				case AGE_INTERVAL_CHILDREN_FINE:
				case AGE_INTERVAL_CHILDREN_MEDIUM:
				case AGE_INTERVAL_BASIC:
					String entryAsString = attributeValue.toString();
					if (attribute == StatisticsContactAttributeEnum.AGE_INTERVAL_5_YEARS) {
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

	





//	public static List<StatisticsGroupingKey> getTimeGroupingKeys(StatisticsContactAttributeEnum attribute, StatisticsContactSubAttributeEnum subAttribute) {
//		
//		Date oldestContactDate = null;
//		switch (attribute) {
//			case REPORT_TIME:
//				oldestContactDate = FacadeProvider.getContactFacade().getOldestContactReportDate();
//				break;
//		}
//
//		return getTimeGroupingKeys(subAttribute, oldestContactDate);
//	}	
	
	

//	@SuppressWarnings("unchecked")
//	public static List<StatisticsGroupingKey> getAttributeGroupingKeys(
//			StatisticsContactAttributeEnum attribute,
//			StatisticsContactSubAttributeEnum subAttribute) {
//		
//		if (subAttribute != null) {
//			switch (attribute) {
//				case REPORT_TIME:
//					return getTimeGroupingKeys(attribute, subAttribute);
//				default:
//					return getAttributeGroupingKeys(attribute, subAttribute);
//
//					//return getAttributeGroupingKeys(getEnum(attribute), subAttribute);
//			}
//		}
//		else {
//			switch (attribute) {
//				case CLASSIFICATION:
//					ArrayList<StatisticsGroupingKey> classificationList = new ArrayList<>();
//					for (ContactClassification classification : ContactClassification.values()) {
//						classificationList.add(classification);
//					}
//					return classificationList;
//				case FOLLOW_UP_STATUS:
//					ArrayList<StatisticsGroupingKey> statusList = new ArrayList<>();
//					for (FollowUpStatus status : FollowUpStatus.values()) {
//						statusList.add(status);
//					}
//					return statusList;
//				default:
//					//return getAttributeGroupingKeys(null, subAttribute);
//
//					return getAttributeGroupingKeys(attribute, subAttribute);
//			}
//		}
//	}

	

	
	


		public static List<StatisticsGroupingKey> getAgeIntervalGroupingKeys(StatisticsContactAttributeEnum attribute) {

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

		if (attribute != StatisticsContactAttributeEnum.AGE_INTERVAL_BASIC) {
			ageIntervalList.add(new IntegerRange(80, null));
		}
		ageIntervalList.add(new IntegerRange(null, null));
		return ageIntervalList;
	}



		public static List<StatisticsGroupingKey> getTimeGroupingKeys(StatisticsContactAttributeEnum attribute, StatisticsContactSubAttributeEnum subAttribute) {

			Date oldestContactDate = null;
			switch (attribute) {
			case ONSET_TIME:
				oldestContactDate = FacadeProvider.getContactFacade().getOldestContactCreationDate();
				break;
			case REPORT_TIME:
				oldestContactDate = FacadeProvider.getContactFacade().getOldestContactReportDate();
				break;
			case OUTCOME_TIME:
				oldestContactDate = FacadeProvider.getContactFacade().getOldestContactLastContactDate();
				break;
			default:
				return new ArrayList<>();
			}
			if(oldestContactDate!=null) {
			LocalDate earliest = oldestContactDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
			}else {
				return new ArrayList<>();

			}
		}




		@SuppressWarnings("unchecked")
		public static List<StatisticsGroupingKey> getAttributeGroupingKeys(
			StatisticsContactAttributeEnum attribute,
			StatisticsContactSubAttributeEnum subAttribute,
			DiseaseConfigurationFacade diseaseConfigurationFacade,
			ContactFacade contactFacade,
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
					return StatisticsHelper.getTimeGroupingKeys(attribute, subAttribute);
				case REGION:
					return (List<StatisticsGroupingKey>) (List<? extends StatisticsGroupingKey>) FacadeProvider.getRegionFacade()
						.getAllActiveByServerCountry();
				case DISTRICT:
					return (List<StatisticsGroupingKey>) (List<? extends StatisticsGroupingKey>) FacadeProvider.getDistrictFacade()
						.getAllActiveAsReference();
				case COMMUNITY:
				default:
					throw new IllegalArgumentException(subAttribute.toString());
				}
			} else {
				switch (attribute) {
				case SEX:
					return toGroupingKeys(Sex.values());
				case DISEASE:
					return toGroupingKeys(FacadeProvider.getDiseaseConfigurationFacade().getAllDiseases(true, true, true));
				case CLASSIFICATION:
					return toGroupingKeys(ContactClassification.values());
				case STATUS:
					return toGroupingKeys(ContactStatus.values());
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
		
		
		







		







		









	
	


	
}
