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
package de.symeda.sormas.backend.person;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.externaldata.HasExternalData;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.ArmedForcesRelationType;
import de.symeda.sormas.api.person.BurialConductor;
import de.symeda.sormas.api.person.CauseOfDeath;
import de.symeda.sormas.api.person.DeathPlaceType;
import de.symeda.sormas.api.person.EducationType;
import de.symeda.sormas.api.person.OccupationType;
import de.symeda.sormas.api.person.PersonContactDetailType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.person.PresentCondition;
import de.symeda.sormas.api.person.Salutation;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.person.SymptomJournalStatus;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.cadre.Cadre;
import de.symeda.sormas.api.person.*;
import de.symeda.sormas.api.utils.FieldConstraints;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.messaging.ManualMessageLog;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.immunization.entity.Immunization;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.travelentry.TravelEntry;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Person extends AbstractDomainObject implements HasExternalData {

	private static final long serialVersionUID = -1735038738114840087L;

	public static final String TABLE_NAME = "person";
	public static final String PERSON_LOCATIONS_TABLE_NAME = "person_locations";

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String OTHER_NAME = "otherName";
	public static final String SALUTATION = "salutation";
	public static final String OTHER_SALUTATION = "otherSalutation";
	public static final String NICKNAME = "nickname";
	public static final String BIRTH_NAME = "birthName";
	public static final String MOTHERS_MAIDEN_NAME = "mothersMaidenName";
	public static final String APPROXIMATE_AGE = "approximateAge";
	public static final String APPROXIMATE_AGE_TYPE = "approximateAgeType";
	public static final String APPROXIMATE_AGE_REFERENCE_DATE = "approximateAgeReferenceDate";
	public static final String BIRTHDATE_DD = "birthdateDD";
	public static final String BIRTHDATE_MM = "birthdateMM";
	public static final String BIRTHDATE_YYYY = "birthdateYYYY";
	public static final String CAUSE_OF_DEATH = "causeOfDeath";
	public static final String CAUSE_OF_DEATH_DETAILS = "causeOfDeathDetails";
	public static final String CAUSE_OF_DEATH_DISEASE = "causeOfDeathDisease";
	public static final String DEATH_PLACE_TYPE = "deathPlaceType";
	public static final String DEATH_PLACE_DESCRIPTION = "deathPlaceDescription";
	public static final String BURIAL_DATE = "burialDate";
	public static final String BURIAL_PLACE_DESCRIPTION = "burialPlaceDescription";
	public static final String BURIAL_CONDUCTOR = "burialConductor";
	public static final String ADDRESS = "address";
	public static final String SEX = "sex";
	public static final String DEATH_DATE = "deathDate";
	public static final String PRESENT_CONDITION = "presentCondition";
	public static final String EDUCATION_TYPE = "educationType";
	public static final String EDUCATION_DETAILS = "educationDetails";
	public static final String OCCUPATION_TYPE = "occupationType";
	public static final String OCCUPATION_DETAILS = "occupationDetails";
	public static final String ARMED_FORCES_RELATION_TYPE = "armedForcesRelationType";
	public static final String FATHERS_NAME = "fathersName";
	public static final String MOTHERS_NAME = "mothersName";
	public static final String NAMES_OF_GUARDIANS = "namesOfGuardians";
	public static final String PLACE_OF_BIRTH_REGION = "placeOfBirthRegion";
	public static final String PLACE_OF_BIRTH_DISTRICT = "placeOfBirthDistrict";
	public static final String PLACE_OF_BIRTH_COMMUNITY = "placeOfBirthCommunity";
	public static final String PLACE_OF_BIRTH_FACILITY = "placeOfBirthFacility";
	public static final String PLACE_OF_BIRTH_FACILITY_DETAILS = "placeOfBirthFacilityDetails";
	public static final String GESTATION_AGE_AT_BIRTH = "gestationAgeAtBirth";
	public static final String BIRTH_WEIGHT = "birthWeight";
	public static final String PASSPORT_NUMBER = "passportNumber";
	public static final String NATIONAL_HEALTH_ID = "nationalHealthId";
	public static final String GHANA_CARD = "ghanacard";
	public static final String NUMBER_OF_PEOPLE = "numberOfPeople";
	public static final String NUMBER_OF_OTHER_CONTACTS = "numberOfOtherContacts";
	public static final String PLACE_OF_BIRTH_FACILITY_TYPE = "placeOfBirthFacilityType";
	public static final String ADDRESSES = "addresses";
	public static final String PERSON_CONTACT_DETAILS = "personContactDetails";

	public static final String HAS_COVID_APP = "hasCovidApp";
	public static final String COVID_CODE_DELIVERED = "covidCodeDelivered";

	public static final String SYMPTOM_JOURNAL_STATUS = "symptomJournalStatus";
	public static final String EXTERNAL_ID = "externalId";
	public static final String EXTERNAL_TOKEN = "externalToken";
	public static final String INTERNAL_TOKEN = "internalToken";
	public static final String BIRTH_COUNTRY = "birthCountry";
	public static final String CITIZENSHIP = "citizenship";
	public static final String CASES = "cases";
	public static final String CONTACTS = "contacts";
	public static final String EVENT_PARTICIPANTS = "eventParticipants";
	public static final String IMMUNIZATIONS = "immunizations";
	public static final String ADDITIONAL_DETAILS = "additionalDetails";
	public static final String TRAVEL_ENTRIES = "travelEntries";
	public static final String CADRE = "cadre";
	public static final String OTHER_ID = "otherId";

	private String firstName;
	private String lastName;
	private String otherName;
	private Salutation salutation;
	private String otherSalutation;
	private String birthName;
	private String nickname;
	private String mothersName;
	private String mothersMaidenName;
	private String fathersName;
	private String namesOfGuardians;

	private Integer approximateAge;
	private ApproximateAgeType approximateAgeType;
	private Date approximateAgeReferenceDate;

	private CauseOfDeath causeOfDeath;
	private String causeOfDeathDetails;
	private Disease causeOfDeathDisease;
	private DeathPlaceType deathPlaceType;
	private String deathPlaceDescription;
	private Date burialDate;
	private String burialPlaceDescription;
	private BurialConductor burialConductor;

	private Location address;
	private List<ManualMessageLog> manualMessageLogs;

	private Sex sex;

	private PresentCondition presentCondition;
	private MaritalStatus marriageStatus;
	private String nationality;
	private Integer birthdateDD;
	private Integer birthdateMM;
	private Integer birthdateYYYY;
	private Region placeOfBirthRegion;
	private District placeOfBirthDistrict;
	private Community placeOfBirthCommunity;
	private Facility placeOfBirthFacility;
	private String placeOfBirthFacilityDetails;
	private Integer gestationAgeAtBirth;
	private Integer birthWeight;
	private Date deathDate;

	private EducationType educationType;
	private String educationDetails;

	private OccupationType occupationType;
	private String occupationDetails;
	private ArmedForcesRelationType armedForcesRelationType;
	private String passportNumber;
	private String nationalHealthId;
	private String ghanaCard;
	private String numberOfPeople;
	private String numberOfOtherContacts;
	private FacilityType placeOfBirthFacilityType;
	private Set<Location> addresses = new HashSet<>();
	private Set<PersonContactDetail> personContactDetails = new HashSet<>();
	private Date changeDateOfEmbeddedLists;

	private SymptomJournalStatus symptomJournalStatus;

	private boolean hasCovidApp;
	private boolean covidCodeDelivered;
	private String externalId;
	private String externalToken;
	private String internalToken;

	private Country birthCountry;
	private Country citizenship;
	private String additionalDetails;
	private String additionalPlacesStayed;
	private String homeAddressRecreational;
	private List<Case> cases = new ArrayList<>();
	private List<Contact> contacts = new ArrayList<>();
	private List<EventParticipant> eventParticipants = new ArrayList<>();
	private List<Immunization> immunizations = new ArrayList<>();
	private List<TravelEntry> travelEntries = new ArrayList<>();
	private String place;
	private String durationMonths;
	private String durationDays;
	private String place2;
	private String durationMonths2;
	private String durationDays2;
	private String place3;
	private String durationMonths3;
	private String durationDays3;
	private String place4;
	private String durationMonths4;
	private String durationDays4;
	private String investigatorName;
	private String investigatorTitle;
	private String investigatorUnit;
	private String investigatorAddress;
	private String investigatorTel;
	private YesNoUnknown receivedAntenatalCare;
	private Integer prenatalTotalVisits;
	private YesNoUnknown attendedByTrainedTBA;
	private String attendedByTrainedTBAMidwifeName;
	private AttendedBy attendedByDoctorNurse;
	private YesNoUnknown cutCordWithSterileBlade;
	private YesNoUnknown cordTreatedWithAnything;
	private String cordTreatedWithAnythingWhere;
	private LocationOfBirth locationOfBirth;
	private YesNoUnknown birthInInstitution;

	private Cadre cadre;
	private String telNumber;
	private YesNo applicable;
	private String pst14MonthsVillage;
	private String pst14MonthsZone;
	private Community pst14MonthsCommunity;
	private District pst14MonthsDistrict;
	private Region pst14MonthsRegion;
	private Country pst14MonthsCountry;
	private YesNo placeOfResidenceSameAsReportingVillage;
	private String residenceSinceWhenInMonths;
	private String ethnicity;
	private String headHouseHold;
	private Set<Profession> professionOfPatient;
	private String professionOfPatientString;
	private String professionOfPatientOther;
	private String nameHealthFacility;
	private String service;
	private String qualification;
	private String otherId;

	@Column(nullable = false, length = CHARACTER_LIMIT_DEFAULT)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(nullable = false, length = CHARACTER_LIMIT_DEFAULT)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(nullable = true, length = CHARACTER_LIMIT_DEFAULT)
	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	@Enumerated(EnumType.STRING)
	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salutation) {
		this.salutation = salutation;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getOtherSalutation() {
		return otherSalutation;
	}

	public void setOtherSalutation(String otherSalutation) {
		this.otherSalutation = otherSalutation;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getBirthName() {
		return birthName;
	}

	public void setBirthName(String birthName) {
		this.birthName = birthName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMothersMaidenName() {
		return mothersMaidenName;
	}

	public void setMothersMaidenName(String mothersMaidenName) {
		this.mothersMaidenName = mothersMaidenName;
	}

	@Column(name = "birthdate_dd")
	public Integer getBirthdateDD() {
		return birthdateDD;
	}

	public void setBirthdateDD(Integer birthdateDD) {
		this.birthdateDD = birthdateDD;
	}

	@Column(name = "birthdate_mm")
	public Integer getBirthdateMM() {
		return birthdateMM;
	}

	public void setBirthdateMM(Integer birthdateMM) {
		this.birthdateMM = birthdateMM;
	}

	@Column(name = "birthdate_yyyy")
	public Integer getBirthdateYYYY() {
		return birthdateYYYY;
	}

	public void setBirthdateYYYY(Integer birthdateYYYY) {
		this.birthdateYYYY = birthdateYYYY;
	}

	public Integer getApproximateAge() {
		return approximateAge;
	}

	public void setApproximateAge(Integer approximateAge) {
		this.approximateAge = approximateAge;
	}

	@Enumerated(EnumType.STRING)
	public ApproximateAgeType getApproximateAgeType() {
		return approximateAgeType;
	}

	public void setApproximateAgeType(ApproximateAgeType approximateAgeType) {
		this.approximateAgeType = approximateAgeType;
	}

	@Temporal(TemporalType.DATE)
	public Date getApproximateAgeReferenceDate() {
		return approximateAgeReferenceDate;
	}

	public void setApproximateAgeReferenceDate(Date approximateAgeReferenceDate) {
		this.approximateAgeReferenceDate = approximateAgeReferenceDate;
	}

	@Enumerated(EnumType.STRING)
	public DeathPlaceType getDeathPlaceType() {
		return deathPlaceType;
	}

	public void setDeathPlaceType(DeathPlaceType deathPlaceType) {
		this.deathPlaceType = deathPlaceType;
	}

	public String getDeathPlaceDescription() {
		return deathPlaceDescription;
	}

	public void setDeathPlaceDescription(String deathPlaceDescription) {
		this.deathPlaceDescription = deathPlaceDescription;
	}

	public String getBurialPlaceDescription() {
		return burialPlaceDescription;
	}

	public void setBurialPlaceDescription(String burialPlaceDescription) {
		this.burialPlaceDescription = burialPlaceDescription;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Location getAddress() {
		if (address == null) {
			address = new Location();
		}
		return address;
	}

	public void setAddress(Location address) {
		this.address = address;
	}

	@Enumerated(EnumType.STRING)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public PresentCondition getPresentCondition() {
		return presentCondition;
	}

	public void setPresentCondition(PresentCondition presentCondition) {
		this.presentCondition = presentCondition;
	}
	public MaritalStatus getMarriageStatus() {
		return marriageStatus;
	}
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setMarriageStatus(MaritalStatus marriageStatus) {
		this.marriageStatus = marriageStatus;
	}

	@Temporal(TemporalType.DATE)
	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getBurialDate() {
		return burialDate;
	}

	public void setBurialDate(Date burialDate) {
		this.burialDate = burialDate;
	}

	@Enumerated(EnumType.STRING)
	public BurialConductor getBurialConductor() {
		return burialConductor;
	}

	public void setBurialConductor(BurialConductor burialConductor) {
		this.burialConductor = burialConductor;
	}

	@Enumerated(EnumType.STRING)
	public CauseOfDeath getCauseOfDeath() {
		return causeOfDeath;
	}

	public void setCauseOfDeath(CauseOfDeath causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}

	public String getCauseOfDeathDetails() {
		return causeOfDeathDetails;
	}

	public void setCauseOfDeathDetails(String causeOfDeathDetails) {
		this.causeOfDeathDetails = causeOfDeathDetails;
	}

	@Enumerated(EnumType.STRING)
	public Disease getCauseOfDeathDisease() {
		return causeOfDeathDisease;
	}

	public void setCauseOfDeathDisease(Disease causeOfDeathDisease) {
		this.causeOfDeathDisease = causeOfDeathDisease;
	}

	@Enumerated(EnumType.STRING)
	public EducationType getEducationType() {
		return educationType;
	}

	public void setEducationType(EducationType educationType) {
		this.educationType = educationType;
	}

	public String getEducationDetails() {
		return educationDetails;
	}

	public void setEducationDetails(String educationDetails) {
		this.educationDetails = educationDetails;
	}

	@Column
	@Convert(converter = OccupationTypeConverter.class)
	public OccupationType getOccupationType() {
		return occupationType;
	}

	public void setOccupationType(OccupationType occupationType) {
		this.occupationType = occupationType;
	}

	public String getOccupationDetails() {
		return occupationDetails;
	}

	public void setOccupationDetails(String occupationDetails) {
		this.occupationDetails = occupationDetails;
	}

	@Enumerated(EnumType.STRING)
	public ArmedForcesRelationType getArmedForcesRelationType() {
		return armedForcesRelationType;
	}

	public void setArmedForcesRelationType(ArmedForcesRelationType armedForcesRelationType) {
		this.armedForcesRelationType = armedForcesRelationType;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getMothersName() {
		return mothersName;
	}

	public void setMothersName(String mothersName) {
		this.mothersName = mothersName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getFathersName() {
		return fathersName;
	}

	public void setFathersName(String fathersName) {
		this.fathersName = fathersName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getNamesOfGuardians() {
		return namesOfGuardians;
	}

	public void setNamesOfGuardians(String namesOfGuardians) {
		this.namesOfGuardians = namesOfGuardians;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Region getPlaceOfBirthRegion() {
		return placeOfBirthRegion;
	}

	public void setPlaceOfBirthRegion(Region placeOfBirthRegion) {
		this.placeOfBirthRegion = placeOfBirthRegion;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public District getPlaceOfBirthDistrict() {
		return placeOfBirthDistrict;
	}

	public void setPlaceOfBirthDistrict(District placeOfBirthDistrict) {
		this.placeOfBirthDistrict = placeOfBirthDistrict;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Community getPlaceOfBirthCommunity() {
		return placeOfBirthCommunity;
	}

	public void setPlaceOfBirthCommunity(Community placeOfBirthCommunity) {
		this.placeOfBirthCommunity = placeOfBirthCommunity;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Facility getPlaceOfBirthFacility() {
		return placeOfBirthFacility;
	}

	public void setPlaceOfBirthFacility(Facility placeOfBirthFacility) {
		this.placeOfBirthFacility = placeOfBirthFacility;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getPlaceOfBirthFacilityDetails() {
		return placeOfBirthFacilityDetails;
	}

	public void setPlaceOfBirthFacilityDetails(String placeOfBirthFacilityDetails) {
		this.placeOfBirthFacilityDetails = placeOfBirthFacilityDetails;
	}

	public Integer getGestationAgeAtBirth() {
		return gestationAgeAtBirth;
	}

	public void setGestationAgeAtBirth(Integer gestationAgeAtBirth) {
		this.gestationAgeAtBirth = gestationAgeAtBirth;
	}

	public Integer getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(Integer birthWeight) {
		this.birthWeight = birthWeight;
	}

	@Column
	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	@Column
	public String getNationalHealthId() {
		return nationalHealthId;
	}

	public void setNationalHealthId(String nationalHealthId) {
		this.nationalHealthId = nationalHealthId;
	}

	@Column
	public String getGhanaCard() {
		return ghanaCard;
	}

	public void setGhanaCard(String ghanaCard) {
		this.ghanaCard = ghanaCard;
	}
	@Column
	public String getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(String numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	@Column
	public String getNumberOfOtherContacts() {
		return numberOfOtherContacts;
	}

	public void setNumberOfOtherContacts(String numberOfOtherContacts) {
		this.numberOfOtherContacts = numberOfOtherContacts;
	}
	@Enumerated(EnumType.STRING)
	public FacilityType getPlaceOfBirthFacilityType() {
		return placeOfBirthFacilityType;
	}

	public void setPlaceOfBirthFacilityType(FacilityType placeOfBirthFacilityType) {
		this.placeOfBirthFacilityType = placeOfBirthFacilityType;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinTable(name = PERSON_LOCATIONS_TABLE_NAME,
		joinColumns = @JoinColumn(name = "person_id"),
		inverseJoinColumns = @JoinColumn(name = "location_id"))
	public Set<Location> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Location> addresses) {
		this.addresses = addresses;
	}

	@OneToMany(mappedBy = PersonContactDetail.PERSON, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<PersonContactDetail> getPersonContactDetails() {
		return personContactDetails;
	}

	public void setPersonContactDetails(Set<PersonContactDetail> personContactDetails) {
		this.personContactDetails = personContactDetails;
	}

	/**
	 * This change date has to be set whenever one of the embedded lists is modified: !oldList.equals(newList)
	 *
	 * @return
	 */
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}

	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Enumerated(EnumType.STRING)
	public SymptomJournalStatus getSymptomJournalStatus() {
		return symptomJournalStatus;
	}

	public void setSymptomJournalStatus(SymptomJournalStatus symptomJournalStatus) {
		this.symptomJournalStatus = symptomJournalStatus;
	}

	@Column
	public boolean isHasCovidApp() {
		return hasCovidApp;
	}

	public void setHasCovidApp(boolean hasCovidApp) {
		this.hasCovidApp = hasCovidApp;
	}

	@Column
	public boolean isCovidCodeDelivered() {
		return covidCodeDelivered;
	}

	public void setCovidCodeDelivered(boolean covidCodeDelivered) {
		this.covidCodeDelivered = covidCodeDelivered;
	}

	@Column
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column
	public String getExternalToken() {
		return externalToken;
	}

	public void setExternalToken(String externalToken) {
		this.externalToken = externalToken;
	}

	@Column
	public String getInternalToken() {
		return internalToken;
	}

	public void setInternalToken(String internalToken) {
		this.internalToken = internalToken;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Country getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(Country placeOfBirthCountry) {
		this.birthCountry = placeOfBirthCountry;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Country getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(Country nationality) {
		this.citizenship = nationality;
	}

	@OneToMany(mappedBy = Case.PERSON, fetch = FetchType.LAZY)
	public List<Case> getCases() {
		return cases;
	}

	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

	@OneToMany(mappedBy = EventParticipant.PERSON, fetch = FetchType.LAZY)
	public List<EventParticipant> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(List<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	@OneToMany(mappedBy = Immunization.PERSON, fetch = FetchType.LAZY)
	public List<Immunization> getImmunizations() {
		return immunizations;
	}

	public void setImmunizations(List<Immunization> immunizations) {
		this.immunizations = immunizations;
	}

	@OneToMany(mappedBy = TravelEntry.PERSON, fetch = FetchType.LAZY)
	public List<TravelEntry> getTravelEntries() {
		return travelEntries;
	}

	public void setTravelEntries(List<TravelEntry> travelEntries) {
		this.travelEntries = travelEntries;
	}

	@OneToMany(mappedBy = Contact.PERSON, fetch = FetchType.LAZY)
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@OneToMany(mappedBy = ManualMessageLog.RECIPIENT_PERSON, fetch = FetchType.LAZY)
	public List<ManualMessageLog> getManualMessageLogs() {
		return manualMessageLogs;
	}

	public void setManualMessageLogs(List<ManualMessageLog> manualMessageLogs) {
		this.manualMessageLogs = manualMessageLogs;
	}

	@Transient
	public String getPhone() {
		return getPersonContactInformation(PersonContactDetailType.PHONE);
	}

	public void setPhone(String phone) {
		setPersonContactInformation(phone, PersonContactDetailType.PHONE);
	}

	@Transient
	public String getEmailAddress() {
		return getPersonContactInformation(PersonContactDetailType.EMAIL);
	}

	public void setEmailAddress(String email) {
		setPersonContactInformation(email, PersonContactDetailType.EMAIL);
	}

	@Column(columnDefinition = "text")
	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
	@Column(columnDefinition = "text")
	public String getAdditionalPlacesStayed() {
		return additionalPlacesStayed;
	}

	public void setAdditionalPlacesStayed(String additionalPlacesStayed) {
		this.additionalPlacesStayed = additionalPlacesStayed;
	}
	@Column(columnDefinition = "text")
	public String getHomeAddressRecreational() {
		return homeAddressRecreational;
	}

	public void setHomeAddressRecreational(String homeAddressRecreational) {
		this.homeAddressRecreational = homeAddressRecreational;
	}
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDurationMonths() {
		return durationMonths;
	}

	public void setDurationMonths(String durationMonths) {
		this.durationMonths = durationMonths;
	}

	public String getDurationDays() {
		return durationDays;
	}
	public void setDurationDays(String durationDays) {
		this.durationDays = durationDays;
	}

	public String getPlace2() {
		return place2;
	}

	public void setPlace2(String place2) {
		this.place2 = place2;
	}

	public String getDurationMonths2() {
		return durationMonths2;
	}

	public void setDurationMonths2(String durationMonths2) {
		this.durationMonths2 = durationMonths2;
	}

	public String getDurationDays2() {
		return durationDays2;
	}

	public void setDurationDays2(String durationDays2) {
		this.durationDays2 = durationDays2;
	}

	public String getPlace3() {
		return place3;
	}

	public void setPlace3(String place3) {
		this.place3 = place3;
	}

	public String getDurationMonths3() {
		return durationMonths3;
	}

	public void setDurationMonths3(String durationMonths3) {
		this.durationMonths3 = durationMonths3;
	}

	public String getDurationDays3() {
		return durationDays3;
	}

	public void setDurationDays3(String durationDays3) {
		this.durationDays3 = durationDays3;
	}

	public String getPlace4() {
		return place4;
	}

	public void setPlace4(String place4) {
		this.place4 = place4;
	}

	public String getDurationMonths4() {
		return durationMonths4;
	}

	public void setDurationMonths4(String durationMonths4) {
		this.durationMonths4 = durationMonths4;
	}

	public String getDurationDays4() {
		return durationDays4;
	}

	public void setDurationDays4(String durationDays4) {
		this.durationDays4 = durationDays4;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getInvestigatorTitle() {
		return investigatorTitle;
	}

	public void setInvestigatorTitle(String investigatorTitle) {
		this.investigatorTitle = investigatorTitle;
	}

	public String getInvestigatorUnit() {
		return investigatorUnit;
	}

	public void setInvestigatorUnit(String investigatorUnit) {
		this.investigatorUnit = investigatorUnit;
	}

	public String getInvestigatorAddress() {
		return investigatorAddress;
	}

	public void setInvestigatorAddress(String investigatorAddress) {
		this.investigatorAddress = investigatorAddress;
	}

	public String getInvestigatorTel() {
		return investigatorTel;
	}

	public void setInvestigatorTel(String investigatorTel) {
		this.investigatorTel = investigatorTel;
	}


	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public YesNo getApplicable() {
		return applicable;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getHeadHouseHold() {
		return headHouseHold;
	}

	public void setHeadHouseHold(String headHouseHold) {
		this.headHouseHold = headHouseHold;
	}
	@Transient
	public Set<Profession> getProfessionOfPatient() {
		if (professionOfPatient == null){
			if(StringUtils.isEmpty(professionOfPatientString)){
				professionOfPatient = new HashSet<>();
			} else{
				professionOfPatient =
						Arrays.stream(professionOfPatientString.split(",")).map(Profession::valueOf).collect(Collectors.toSet());
			}
		}
		return professionOfPatient;
	}

	public void setProfessionOfPatient(Set<Profession> professionOfPatient) {
		this.professionOfPatient = professionOfPatient;

		if (this.professionOfPatient == null){
			return;
		}

		StringBuilder sb = new StringBuilder();
		professionOfPatient.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if(sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		professionOfPatientString = sb.toString();

	}

	public String getProfessionOfPatientString() {return professionOfPatientString;}

	public void setProfessionOfPatientString(String professionOfPatientString){
		this.professionOfPatientString = professionOfPatientString;
		professionOfPatient = null;
	}

	public String getProfessionOfPatientOther() {
		return professionOfPatientOther;
	}

	public void setProfessionOfPatientOther(String professionOfPatientOther) {
		this.professionOfPatientOther = professionOfPatientOther;
	}

	public String getNameHealthFacility() {
		return nameHealthFacility;
	}

	public void setNameHealthFacility(String nameHealthFacility) {
		this.nameHealthFacility = nameHealthFacility;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

    public void setApplicable(YesNo applicable) {
         this.applicable = applicable;
    }

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	private void setPersonContactInformation(String contactInfo, PersonContactDetailType personContactDetailType) {
		final PersonContactDetail pcd =
			new PersonContactDetail(this, true, personContactDetailType, null, null, contactInfo, null, false, null, null);
		getPersonContactDetails().add(pcd);
	}

	@Transient
	private String getPersonContactInformation(PersonContactDetailType personContactDetailType) {
		final Optional<PersonContactDetail> optionalPersonContactDetail = getPersonContactDetails().stream()
			.filter(pcd -> pcd.isPrimaryContact() && pcd.getPersonContactDetailType() == personContactDetailType)
			.findAny();
		if (optionalPersonContactDetail.isPresent()) {
			return optionalPersonContactDetail.get().getContactInformation();
		}
		return null;
	}

	public PersonReferenceDto toReference() {
		return new PersonReferenceDto(getUuid(), getFirstName(), getLastName(), getOtherName());
	}

	@Transient
	public boolean isEnrolledInExternalJournal() {
		return SymptomJournalStatus.ACCEPTED.equals(symptomJournalStatus) || SymptomJournalStatus.REGISTERED.equals(symptomJournalStatus);
	}

	@Override
	public String toString() {
		return PersonDto.buildCaption(firstName, lastName, otherName);
	}

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public Cadre getCadre() {
		return cadre;
	}

	public void setCadre(Cadre cadre) {
		this.cadre = cadre;
	}

	public YesNoUnknown getReceivedAntenatalCare() {
		return receivedAntenatalCare;
	}

	public void setReceivedAntenatalCare(YesNoUnknown receivedAntenatalCare) {
		this.receivedAntenatalCare = receivedAntenatalCare;
	}

	public Integer getPrenatalTotalVisits() {
		return prenatalTotalVisits;
	}

	public void setPrenatalTotalVisits(Integer prenatalTotalVisits) {
		this.prenatalTotalVisits = prenatalTotalVisits;
	}

	public YesNoUnknown getAttendedByTrainedTBA() {
		return attendedByTrainedTBA;
	}

	public void setAttendedByTrainedTBA(YesNoUnknown attendedByTrainedTBA) {
		this.attendedByTrainedTBA = attendedByTrainedTBA;
	}

	public String getAttendedByTrainedTBAMidwifeName() {
		return attendedByTrainedTBAMidwifeName;
	}

	public void setAttendedByTrainedTBAMidwifeName(String attendedByTrainedTBAMidwifeName) {
		this.attendedByTrainedTBAMidwifeName = attendedByTrainedTBAMidwifeName;
	}

	public AttendedBy getAttendedByDoctorNurse() {
		return attendedByDoctorNurse;
	}

	public void setAttendedByDoctorNurse(AttendedBy attendedByDoctorNurse) {
		this.attendedByDoctorNurse = attendedByDoctorNurse;
	}

	public YesNoUnknown getCutCordWithSterileBlade() {
		return cutCordWithSterileBlade;
	}

	public void setCutCordWithSterileBlade(YesNoUnknown cutCordWithSterileBlade) {
		this.cutCordWithSterileBlade = cutCordWithSterileBlade;
	}

	public YesNoUnknown getCordTreatedWithAnything() {
		return cordTreatedWithAnything;
	}

	public void setCordTreatedWithAnything(YesNoUnknown cordTreatedWithAnything) {
		this.cordTreatedWithAnything = cordTreatedWithAnything;
	}

	public String getCordTreatedWithAnythingWhere() {
		return cordTreatedWithAnythingWhere;
	}

	public void setCordTreatedWithAnythingWhere(String cordTreatedWithAnythingWhere) {
		this.cordTreatedWithAnythingWhere = cordTreatedWithAnythingWhere;
	}


	public String getPst14MonthsVillage() {
		return pst14MonthsVillage;
	}

	public void setPst14MonthsVillage(String pst14MonthsVillage) {
		this.pst14MonthsVillage = pst14MonthsVillage;
	}

	public String getPst14MonthsZone() {
		return pst14MonthsZone;
	}

	public void setPst14MonthsZone(String pst14MonthsZone) {
		this.pst14MonthsZone = pst14MonthsZone;
	}

	@OneToOne(cascade = CascadeType.ALL)

	public Community getPst14MonthsCommunity() {
		return pst14MonthsCommunity;
	}

	public void setPst14MonthsCommunity(Community pst14MonthsCommunity) {
		this.pst14MonthsCommunity = pst14MonthsCommunity;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public District getPst14MonthsDistrict() {
		return pst14MonthsDistrict;
	}


	public YesNo getPlaceOfResidenceSameAsReportingVillage() {
		return placeOfResidenceSameAsReportingVillage;
	}

	public void setPlaceOfResidenceSameAsReportingVillage(YesNo placeOfResidenceSameAsReportingVillage) {
		this.placeOfResidenceSameAsReportingVillage = placeOfResidenceSameAsReportingVillage;
	}

	public String getResidenceSinceWhenInMonths() {
		return residenceSinceWhenInMonths;
	}

	public void setResidenceSinceWhenInMonths(String residenceSinceWhenInMonths) {
		this.residenceSinceWhenInMonths = residenceSinceWhenInMonths;
	}

	public void setPst14MonthsDistrict(District pst14MonthsDistrict) {
		this.pst14MonthsDistrict = pst14MonthsDistrict;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Region getPst14MonthsRegion() {
		return pst14MonthsRegion;
	}

	public void setPst14MonthsRegion(Region pst14MonthsRegion) {
		this.pst14MonthsRegion = pst14MonthsRegion;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Country getPst14MonthsCountry() {
		return pst14MonthsCountry;
	}

	public void setPst14MonthsCountry(Country pst14MonthsCountry) {
		this.pst14MonthsCountry = pst14MonthsCountry;
	}

	public LocationOfBirth getLocationOfBirth() {
		return this.locationOfBirth;
	}

	public void setLocationOfBirth(final LocationOfBirth locationOfBirth) {
		this.locationOfBirth = locationOfBirth;
	}

	public YesNoUnknown getBirthInInstitution() {
		return this.birthInInstitution;
	}

	public void setBirthInInstitution(final YesNoUnknown birthInInstitution) {
		this.birthInInstitution = birthInInstitution;
	}
}
