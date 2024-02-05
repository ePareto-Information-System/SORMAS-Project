/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.backend.exposure;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.epidata.AnimalCondition;
import de.symeda.sormas.api.epidata.WaterSource;
import de.symeda.sormas.api.event.MeansOfTransport;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.exposure.AnimalContactType;
import de.symeda.sormas.api.exposure.ExposureRole;
import de.symeda.sormas.api.exposure.ExposureType;
import de.symeda.sormas.api.exposure.GatheringType;
import de.symeda.sormas.api.exposure.HabitationType;
import de.symeda.sormas.api.exposure.TypeOfAnimal;
import de.symeda.sormas.api.exposure.WorkEnvironment;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.user.User;

@Entity(name = "exposures")
@Audited
public class Exposure extends AbstractDomainObject {

	private static final long serialVersionUID = -5570515874416024604L;

	public static final String TABLE_NAME = "exposures";

	public static final String EPI_DATA = "epiData";
	public static final String LOCATION = "location";
	public static final String EXPOSURE_TYPE = "exposureType";
	public static final String CONTACT_TO_CASE = "contactToCase";

	private EpiData epiData;
	private User reportingUser;
	private boolean probableInfectionEnvironment;
	private Date startDate;
	private Date endDate;
	private String description;
	private ExposureType exposureType;
	private String exposureTypeDetails;
	private Location location;
	private ExposureRole exposureRole;

	// Type of Place
	private TypeOfPlace typeOfPlace;
	private String typeOfPlaceDetails;
	private MeansOfTransport meansOfTransport;
	private String meansOfTransportDetails;
	private String connectionNumber;
	private String seatNumber;

	private WorkEnvironment workEnvironment;

	// Details
	private YesNo indoors;
	private YesNo outdoors;
	private YesNo wearingMask;
	private YesNo wearingPpe;
	private YesNo otherProtectiveMeasures;
	private String protectiveMeasuresDetails;
	private YesNo shortDistance;
	private YesNo longFaceToFaceContact;
	private YesNo animalMarket;
	private YesNo percutaneous;
	private YesNo contactToBodyFluids;
	private YesNo handlingSamples;
	private YesNo eatingRawAnimalProducts;
	private YesNo handlingAnimals;
	private AnimalCondition animalCondition;
	private YesNo animalVaccinated;
	private AnimalContactType animalContactType;
	private String animalContactTypeDetails;
	private YesNo bodyOfWater;
	private WaterSource waterSource;
	private String waterSourceDetails;
	private Contact contactToCase;
	private YesNo prophylaxis;
	private Date prophylaxisDate;
	private YesNo riskArea;

	// Exposure sub-types
	private GatheringType gatheringType;
	private String gatheringDetails;
	private HabitationType habitationType;
	private String habitationDetails;
	private TypeOfAnimal typeOfAnimal;
	private String typeOfAnimalDetails;

	// Fields specific to ExposureType.BURIAL
	private YesNo physicalContactDuringPreparation;
	private YesNo physicalContactWithBody;
	private YesNo deceasedPersonIll;
	private String deceasedPersonName;
	private String deceasedPersonRelation;

	// Fields specific to ExposureType.GATHERING
	private YesNo largeAttendanceNumber;

	@ManyToOne
	@JoinColumn(nullable = false)
	public EpiData getEpiData() {
		return epiData;
	}

	public void setEpiData(EpiData epiData) {
		this.epiData = epiData;
	}

	@ManyToOne
	@JoinColumn
	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	@Column
	public boolean isProbableInfectionEnvironment() {
		return probableInfectionEnvironment;
	}

	public void setProbableInfectionEnvironment(boolean probableInfectionEnvironment) {
		this.probableInfectionEnvironment = probableInfectionEnvironment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(columnDefinition = "text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public ExposureType getExposureType() {
		return exposureType;
	}

	public void setExposureType(ExposureType exposureType) {
		this.exposureType = exposureType;
	}

	@Column(columnDefinition = "text")
	public String getExposureTypeDetails() {
		return exposureTypeDetails;
	}

	public void setExposureTypeDetails(String exposureTypeDetails) {
		this.exposureTypeDetails = exposureTypeDetails;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	public Location getLocation() {
		if (location == null) {
			location = new Location();
		}
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Enumerated(EnumType.STRING)
	public ExposureRole getExposureRole() {
		return exposureRole;
	}

	public void setExposureRole(ExposureRole exposureRole) {
		this.exposureRole = exposureRole;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getIndoors() {
		return indoors;
	}

	public void setIndoors(YesNo indoors) {
		this.indoors = indoors;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getOutdoors() {
		return outdoors;
	}

	public void setOutdoors(YesNo outdoors) {
		this.outdoors = outdoors;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getWearingMask() {
		return wearingMask;
	}

	public void setWearingMask(YesNo wearingMask) {
		this.wearingMask = wearingMask;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getWearingPpe() {
		return wearingPpe;
	}

	public void setWearingPpe(YesNo wearingPpe) {
		this.wearingPpe = wearingPpe;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getOtherProtectiveMeasures() {
		return otherProtectiveMeasures;
	}

	public void setOtherProtectiveMeasures(YesNo otherProtectiveMeasures) {
		this.otherProtectiveMeasures = otherProtectiveMeasures;
	}

	@Column(columnDefinition = "text")
	public String getProtectiveMeasuresDetails() {
		return protectiveMeasuresDetails;
	}

	public void setProtectiveMeasuresDetails(String protectiveMeasuresDetails) {
		this.protectiveMeasuresDetails = protectiveMeasuresDetails;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getShortDistance() {
		return shortDistance;
	}

	public void setShortDistance(YesNo shortDistance) {
		this.shortDistance = shortDistance;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getLongFaceToFaceContact() {
		return longFaceToFaceContact;
	}

	public void setLongFaceToFaceContact(YesNo longFaceToFaceContact) {
		this.longFaceToFaceContact = longFaceToFaceContact;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getAnimalMarket() {
		return animalMarket;
	}

	public void setAnimalMarket(YesNo animalMarket) {
		this.animalMarket = animalMarket;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getPercutaneous() {
		return percutaneous;
	}

	public void setPercutaneous(YesNo percutaneous) {
		this.percutaneous = percutaneous;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getContactToBodyFluids() {
		return contactToBodyFluids;
	}

	public void setContactToBodyFluids(YesNo contactToBodyFluids) {
		this.contactToBodyFluids = contactToBodyFluids;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getHandlingSamples() {
		return handlingSamples;
	}

	public void setHandlingSamples(YesNo handlingSamples) {
		this.handlingSamples = handlingSamples;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getEatingRawAnimalProducts() {
		return eatingRawAnimalProducts;
	}

	public void setEatingRawAnimalProducts(YesNo eatingRawAnimalProducts) {
		this.eatingRawAnimalProducts = eatingRawAnimalProducts;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getHandlingAnimals() {
		return handlingAnimals;
	}

	public void setHandlingAnimals(YesNo handlingAnimals) {
		this.handlingAnimals = handlingAnimals;
	}

	@Enumerated(EnumType.STRING)
	public AnimalCondition getAnimalCondition() {
		return animalCondition;
	}

	public void setAnimalCondition(AnimalCondition animalCondition) {
		this.animalCondition = animalCondition;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getAnimalVaccinated() {
		return animalVaccinated;
	}

	public void setAnimalVaccinated(YesNo animalVaccinated) {
		this.animalVaccinated = animalVaccinated;
	}

	@Enumerated(EnumType.STRING)
	public AnimalContactType getAnimalContactType() {
		return animalContactType;
	}

	public void setAnimalContactType(AnimalContactType animalContactType) {
		this.animalContactType = animalContactType;
	}

	@Column(columnDefinition = "text")
	public String getAnimalContactTypeDetails() {
		return animalContactTypeDetails;
	}

	public void setAnimalContactTypeDetails(String animalContactTypeDetails) {
		this.animalContactTypeDetails = animalContactTypeDetails;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getBodyOfWater() {
		return bodyOfWater;
	}

	public void setBodyOfWater(YesNo bodyOfWater) {
		this.bodyOfWater = bodyOfWater;
	}

	@Enumerated(EnumType.STRING)
	public WaterSource getWaterSource() {
		return waterSource;
	}

	public void setWaterSource(WaterSource waterSource) {
		this.waterSource = waterSource;
	}

	@Column(columnDefinition = "text")
	public String getWaterSourceDetails() {
		return waterSourceDetails;
	}

	public void setWaterSourceDetails(String waterSourceDetails) {
		this.waterSourceDetails = waterSourceDetails;
	}

	@ManyToOne
	@JoinColumn
	public Contact getContactToCase() {
		return contactToCase;
	}

	public void setContactToCase(Contact contactToCase) {
		this.contactToCase = contactToCase;
	}

	@Enumerated(EnumType.STRING)
	public GatheringType getGatheringType() {
		return gatheringType;
	}

	public void setGatheringType(GatheringType gatheringType) {
		this.gatheringType = gatheringType;
	}

	@Column(columnDefinition = "text")
	public String getGatheringDetails() {
		return gatheringDetails;
	}

	public void setGatheringDetails(String gatheringDetails) {
		this.gatheringDetails = gatheringDetails;
	}

	@Enumerated(EnumType.STRING)
	public HabitationType getHabitationType() {
		return habitationType;
	}

	public void setHabitationType(HabitationType habitationType) {
		this.habitationType = habitationType;
	}

	@Column(columnDefinition = "text")
	public String getHabitationDetails() {
		return habitationDetails;
	}

	public void setHabitationDetails(String habitationDetails) {
		this.habitationDetails = habitationDetails;
	}

	@Enumerated(EnumType.STRING)
	public TypeOfAnimal getTypeOfAnimal() {
		return typeOfAnimal;
	}

	public void setTypeOfAnimal(TypeOfAnimal typeOfAnimal) {
		this.typeOfAnimal = typeOfAnimal;
	}

	@Column(columnDefinition = "text")
	public String getTypeOfAnimalDetails() {
		return typeOfAnimalDetails;
	}

	public void setTypeOfAnimalDetails(String typeOfAnimalDetails) {
		this.typeOfAnimalDetails = typeOfAnimalDetails;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getPhysicalContactDuringPreparation() {
		return physicalContactDuringPreparation;
	}

	public void setPhysicalContactDuringPreparation(YesNo physicalContactDuringPreparation) {
		this.physicalContactDuringPreparation = physicalContactDuringPreparation;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getPhysicalContactWithBody() {
		return physicalContactWithBody;
	}

	public void setPhysicalContactWithBody(YesNo physicalContactWithBody) {
		this.physicalContactWithBody = physicalContactWithBody;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getDeceasedPersonIll() {
		return deceasedPersonIll;
	}

	public void setDeceasedPersonIll(YesNo deceasedPersonIll) {
		this.deceasedPersonIll = deceasedPersonIll;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getDeceasedPersonName() {
		return deceasedPersonName;
	}

	public void setDeceasedPersonName(String deceasedPersonName) {
		this.deceasedPersonName = deceasedPersonName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getDeceasedPersonRelation() {
		return deceasedPersonRelation;
	}

	public void setDeceasedPersonRelation(String deceasedPersonRelation) {
		this.deceasedPersonRelation = deceasedPersonRelation;
	}

	@Enumerated(EnumType.STRING)
	public TypeOfPlace getTypeOfPlace() {
		return typeOfPlace;
	}

	public void setTypeOfPlace(TypeOfPlace typeOfPlace) {
		this.typeOfPlace = typeOfPlace;
	}

	@Column(columnDefinition = "text")
	public String getTypeOfPlaceDetails() {
		return typeOfPlaceDetails;
	}

	public void setTypeOfPlaceDetails(String typeOfPlaceDetails) {
		this.typeOfPlaceDetails = typeOfPlaceDetails;
	}

	@Enumerated(EnumType.STRING)
	public MeansOfTransport getMeansOfTransport() {
		return meansOfTransport;
	}

	public void setMeansOfTransport(MeansOfTransport meansOfTransport) {
		this.meansOfTransport = meansOfTransport;
	}

	@Column(columnDefinition = "text")
	public String getMeansOfTransportDetails() {
		return meansOfTransportDetails;
	}

	public void setMeansOfTransportDetails(String meansOfTransportDetails) {
		this.meansOfTransportDetails = meansOfTransportDetails;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getConnectionNumber() {
		return connectionNumber;
	}

	public void setConnectionNumber(String connectionNumber) {
		this.connectionNumber = connectionNumber;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getSeatNumber() {
		return seatNumber;
	}

	@Enumerated(EnumType.STRING)
	public WorkEnvironment getWorkEnvironment() {
		return workEnvironment;
	}

	public void setWorkEnvironment(WorkEnvironment workEnvironment) {
		this.workEnvironment = workEnvironment;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getProphylaxis() {
		return prophylaxis;
	}

	public void setProphylaxis(YesNo prophylaxis) {
		this.prophylaxis = prophylaxis;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getProphylaxisDate() {
		return prophylaxisDate;
	}

	public void setProphylaxisDate(Date prophylaxisDate) {
		this.prophylaxisDate = prophylaxisDate;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getRiskArea() {
		return riskArea;
	}

	public void setRiskArea(YesNo riskArea) {
		this.riskArea = riskArea;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getLargeAttendanceNumber() {
		return largeAttendanceNumber;
	}

	public void setLargeAttendanceNumber(YesNo largeAttendanceNumber) {
		this.largeAttendanceNumber = largeAttendanceNumber;
	}
}
