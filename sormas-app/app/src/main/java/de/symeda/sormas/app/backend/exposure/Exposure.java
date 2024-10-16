package de.symeda.sormas.app.backend.exposure;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.contact.Contact;
import de.symeda.sormas.app.backend.epidata.EpiData;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.user.User;

@Entity(name = Exposure.TABLE_NAME)
@DatabaseTable(tableName = Exposure.TABLE_NAME)
@EmbeddedAdo(parentAccessor = Exposure.EPI_DATA)
public class Exposure extends PseudonymizableAdo {

	private static final long serialVersionUID = -5570515874416024604L;

	public static final String TABLE_NAME = "exposures";
	public static final String I18N_PREFIX = "Exposure";

	public static final String EPI_DATA = "epiData";
	public static final String LOCATION = "location";
	public static final String HANDLING_SAMPLES = "handlingSamples";
	public static final String PERCUTANEOUS = "percutaneous";
	public static final String PHYSICAL_CONTACT_WITH_BODY = "physicalContactWithBody";
	public static final String ANIMAL_CONDITION = "animalCondition";
	public static final String EATING_RAW_ANIMAL_PRODUCTS = "eatingRawAnimalProducts";
	public static final String TYPE_OF_ANIMAL = "typeOfAnimal";
	public static final String BODY_OF_WATER = "bodyOfWater";
	public static final String HABITATION_TYPE = "habitationType";
	public static final String ANIMAL_MARKET = "animalMarket";
	public static final String RISK_AREA = "riskArea";
	public static final String ANIMAL_CONTACT_TYPE = "animalContactType";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private EpiData epiData;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private User reportingUser;
	@DatabaseField
	private boolean probableInfectionEnvironment;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date startDate;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date endDate;
	@Column(columnDefinition = "text")
	private String description;
	@Enumerated(EnumType.STRING)
	private ExposureType exposureType;
	@Column(columnDefinition = "text")
	private String exposureTypeDetails;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Location location;
	@Enumerated(EnumType.STRING)
	private ExposureRole exposureRole;

	// Type of Place
	@Enumerated(EnumType.STRING)
	private TypeOfPlace typeOfPlace;
	@Column(columnDefinition = "text")
	private String typeOfPlaceDetails;
	@Enumerated(EnumType.STRING)
	private MeansOfTransport meansOfTransport;
	@Column(columnDefinition = "text")
	private String meansOfTransportDetails;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String connectionNumber;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String seatNumber;

	@Enumerated(EnumType.STRING)
	private WorkEnvironment workEnvironment;

	// Details
	@Enumerated(EnumType.STRING)
	private YesNo indoors;
	@Enumerated(EnumType.STRING)
	private YesNo outdoors;
	@Enumerated(EnumType.STRING)
	private YesNo wearingMask;
	@Enumerated(EnumType.STRING)
	private YesNo wearingPpe;
	@Enumerated(EnumType.STRING)
	private YesNo otherProtectiveMeasures;
	@Column(columnDefinition = "text")
	private String protectiveMeasuresDetails;
	@Enumerated(EnumType.STRING)
	private YesNo shortDistance;
	@Enumerated(EnumType.STRING)
	private YesNo longFaceToFaceContact;
	@Enumerated(EnumType.STRING)
	private YesNo animalMarket;
	@Enumerated(EnumType.STRING)
	private YesNo percutaneous;
	@Enumerated(EnumType.STRING)
	private YesNo contactToBodyFluids;
	@Enumerated(EnumType.STRING)
	private YesNo handlingSamples;
	@Enumerated(EnumType.STRING)
	private YesNo eatingRawAnimalProducts;
	@Enumerated(EnumType.STRING)
	private YesNo handlingAnimals;
	@Enumerated(EnumType.STRING)
	private AnimalCondition animalCondition;
	@Enumerated(EnumType.STRING)
	private YesNo animalVaccinated;
	@Enumerated(EnumType.STRING)
	private AnimalContactType animalContactType;
	@Column(columnDefinition = "text")
	private String animalContactTypeDetails;
	@Enumerated(EnumType.STRING)
	private YesNo bodyOfWater;
	@Enumerated(EnumType.STRING)
	private WaterSource waterSource;
	@Column(columnDefinition = "text")
	private String waterSourceDetails;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Contact contactToCase;
	@Enumerated(EnumType.STRING)
	private YesNo prophylaxis;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date prophylaxisDate;
	@Enumerated(EnumType.STRING)
	private YesNo riskArea;

	// Exposure sub-types
	@Enumerated(EnumType.STRING)
	private GatheringType gatheringType;
	@Column(columnDefinition = "text")
	private String gatheringDetails;
	@Enumerated(EnumType.STRING)
	private HabitationType habitationType;
	@Column(columnDefinition = "text")
	private String habitationDetails;
	@Enumerated(EnumType.STRING)
	private TypeOfAnimal typeOfAnimal;
	@Column(columnDefinition = "text")
	private String typeOfAnimalDetails;

	// Fields specific to ExposureType.BURIAL
	@Enumerated(EnumType.STRING)
	private YesNo physicalContactDuringPreparation;
	@Enumerated(EnumType.STRING)
	private YesNo physicalContactWithBody;
	@Enumerated(EnumType.STRING)
	private YesNo deceasedPersonIll;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String deceasedPersonName;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String deceasedPersonRelation;
	@Enumerated(EnumType.STRING)
	private YesNo largeAttendanceNumber;

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

	public EpiData getEpiData() {
		return epiData;
	}

	public void setEpiData(EpiData epiData) {
		this.epiData = epiData;
	}

	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	public boolean isProbableInfectionEnvironment() {
		return probableInfectionEnvironment;
	}

	public void setProbableInfectionEnvironment(boolean probableInfectionEnvironment) {
		this.probableInfectionEnvironment = probableInfectionEnvironment;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ExposureType getExposureType() {
		return exposureType;
	}

	public void setExposureType(ExposureType exposureType) {
		this.exposureType = exposureType;
	}

	public String getExposureTypeDetails() {
		return exposureTypeDetails;
	}

	public void setExposureTypeDetails(String exposureTypeDetails) {
		this.exposureTypeDetails = exposureTypeDetails;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ExposureRole getExposureRole() {
		return exposureRole;
	}

	public void setExposureRole(ExposureRole exposureRole) {
		this.exposureRole = exposureRole;
	}

	public YesNo getIndoors() {
		return indoors;
	}

	public void setIndoors(YesNo indoors) {
		this.indoors = indoors;
	}

	public YesNo getOutdoors() {
		return outdoors;
	}

	public void setOutdoors(YesNo outdoors) {
		this.outdoors = outdoors;
	}

	public YesNo getWearingMask() {
		return wearingMask;
	}

	public void setWearingMask(YesNo wearingMask) {
		this.wearingMask = wearingMask;
	}

	public YesNo getWearingPpe() {
		return wearingPpe;
	}

	public void setWearingPpe(YesNo wearingPpe) {
		this.wearingPpe = wearingPpe;
	}

	public YesNo getOtherProtectiveMeasures() {
		return otherProtectiveMeasures;
	}

	public void setOtherProtectiveMeasures(YesNo otherProtectiveMeasures) {
		this.otherProtectiveMeasures = otherProtectiveMeasures;
	}

	public String getProtectiveMeasuresDetails() {
		return protectiveMeasuresDetails;
	}

	public void setProtectiveMeasuresDetails(String protectiveMeasuresDetails) {
		this.protectiveMeasuresDetails = protectiveMeasuresDetails;
	}

	public YesNo getShortDistance() {
		return shortDistance;
	}

	public void setShortDistance(YesNo shortDistance) {
		this.shortDistance = shortDistance;
	}

	public YesNo getLongFaceToFaceContact() {
		return longFaceToFaceContact;
	}

	public void setLongFaceToFaceContact(YesNo longFaceToFaceContact) {
		this.longFaceToFaceContact = longFaceToFaceContact;
	}

	public YesNo getAnimalMarket() {
		return animalMarket;
	}

	public void setAnimalMarket(YesNo animalMarket) {
		this.animalMarket = animalMarket;
	}

	public YesNo getPercutaneous() {
		return percutaneous;
	}

	public void setPercutaneous(YesNo percutaneous) {
		this.percutaneous = percutaneous;
	}

	public YesNo getContactToBodyFluids() {
		return contactToBodyFluids;
	}

	public void setContactToBodyFluids(YesNo contactToBodyFluids) {
		this.contactToBodyFluids = contactToBodyFluids;
	}

	public YesNo getHandlingSamples() {
		return handlingSamples;
	}

	public void setHandlingSamples(YesNo handlingSamples) {
		this.handlingSamples = handlingSamples;
	}

	public YesNo getEatingRawAnimalProducts() {
		return eatingRawAnimalProducts;
	}

	public void setEatingRawAnimalProducts(YesNo eatingRawAnimalProducts) {
		this.eatingRawAnimalProducts = eatingRawAnimalProducts;
	}

	public YesNo getHandlingAnimals() {
		return handlingAnimals;
	}

	public void setHandlingAnimals(YesNo handlingAnimals) {
		this.handlingAnimals = handlingAnimals;
	}

	public AnimalCondition getAnimalCondition() {
		return animalCondition;
	}

	public void setAnimalCondition(AnimalCondition animalCondition) {
		this.animalCondition = animalCondition;
	}

	public YesNo getAnimalVaccinated() {
		return animalVaccinated;
	}

	public void setAnimalVaccinated(YesNo animalVaccinated) {
		this.animalVaccinated = animalVaccinated;
	}

	public AnimalContactType getAnimalContactType() {
		return animalContactType;
	}

	public void setAnimalContactType(AnimalContactType animalContactType) {
		this.animalContactType = animalContactType;
	}

	public String getAnimalContactTypeDetails() {
		return animalContactTypeDetails;
	}

	public void setAnimalContactTypeDetails(String animalContactTypeDetails) {
		this.animalContactTypeDetails = animalContactTypeDetails;
	}

	public YesNo getBodyOfWater() {
		return bodyOfWater;
	}

	public void setBodyOfWater(YesNo bodyOfWater) {
		this.bodyOfWater = bodyOfWater;
	}

	public WaterSource getWaterSource() {
		return waterSource;
	}

	public void setWaterSource(WaterSource waterSource) {
		this.waterSource = waterSource;
	}

	public String getWaterSourceDetails() {
		return waterSourceDetails;
	}

	public void setWaterSourceDetails(String waterSourceDetails) {
		this.waterSourceDetails = waterSourceDetails;
	}

	public Contact getContactToCase() {
		return contactToCase;
	}

	public void setContactToCase(Contact contactToCase) {
		this.contactToCase = contactToCase;
	}

	public GatheringType getGatheringType() {
		return gatheringType;
	}

	public void setGatheringType(GatheringType gatheringType) {
		this.gatheringType = gatheringType;
	}

	public String getGatheringDetails() {
		return gatheringDetails;
	}

	public void setGatheringDetails(String gatheringDetails) {
		this.gatheringDetails = gatheringDetails;
	}

	public HabitationType getHabitationType() {
		return habitationType;
	}

	public void setHabitationType(HabitationType habitationType) {
		this.habitationType = habitationType;
	}

	public String getHabitationDetails() {
		return habitationDetails;
	}

	public void setHabitationDetails(String habitationDetails) {
		this.habitationDetails = habitationDetails;
	}

	public TypeOfAnimal getTypeOfAnimal() {
		return typeOfAnimal;
	}

	public void setTypeOfAnimal(TypeOfAnimal typeOfAnimal) {
		this.typeOfAnimal = typeOfAnimal;
	}

	public String getTypeOfAnimalDetails() {
		return typeOfAnimalDetails;
	}

	public void setTypeOfAnimalDetails(String typeOfAnimalDetails) {
		this.typeOfAnimalDetails = typeOfAnimalDetails;
	}

	public YesNo getPhysicalContactDuringPreparation() {
		return physicalContactDuringPreparation;
	}

	public void setPhysicalContactDuringPreparation(YesNo physicalContactDuringPreparation) {
		this.physicalContactDuringPreparation = physicalContactDuringPreparation;
	}

	public YesNo getPhysicalContactWithBody() {
		return physicalContactWithBody;
	}

	public void setPhysicalContactWithBody(YesNo physicalContactWithBody) {
		this.physicalContactWithBody = physicalContactWithBody;
	}

	public YesNo getDeceasedPersonIll() {
		return deceasedPersonIll;
	}

	public void setDeceasedPersonIll(YesNo deceasedPersonIll) {
		this.deceasedPersonIll = deceasedPersonIll;
	}

	public String getDeceasedPersonName() {
		return deceasedPersonName;
	}

	public void setDeceasedPersonName(String deceasedPersonName) {
		this.deceasedPersonName = deceasedPersonName;
	}

	public String getDeceasedPersonRelation() {
		return deceasedPersonRelation;
	}

	public void setDeceasedPersonRelation(String deceasedPersonRelation) {
		this.deceasedPersonRelation = deceasedPersonRelation;
	}

	public TypeOfPlace getTypeOfPlace() {
		return typeOfPlace;
	}

	public void setTypeOfPlace(TypeOfPlace typeOfPlace) {
		this.typeOfPlace = typeOfPlace;
	}

	public String getTypeOfPlaceDetails() {
		return typeOfPlaceDetails;
	}

	public void setTypeOfPlaceDetails(String typeOfPlaceDetails) {
		this.typeOfPlaceDetails = typeOfPlaceDetails;
	}

	public MeansOfTransport getMeansOfTransport() {
		return meansOfTransport;
	}

	public void setMeansOfTransport(MeansOfTransport meansOfTransport) {
		this.meansOfTransport = meansOfTransport;
	}

	public String getMeansOfTransportDetails() {
		return meansOfTransportDetails;
	}

	public void setMeansOfTransportDetails(String meansOfTransportDetails) {
		this.meansOfTransportDetails = meansOfTransportDetails;
	}

	public String getConnectionNumber() {
		return connectionNumber;
	}

	public void setConnectionNumber(String connectionNumber) {
		this.connectionNumber = connectionNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public WorkEnvironment getWorkEnvironment() {
		return workEnvironment;
	}

	public void setWorkEnvironment(WorkEnvironment workEnvironment) {
		this.workEnvironment = workEnvironment;
	}

	public YesNo getProphylaxis() {
		return prophylaxis;
	}

	public void setProphylaxis(YesNo prophylaxis) {
		this.prophylaxis = prophylaxis;
	}

	public Date getProphylaxisDate() {
		return prophylaxisDate;
	}

	public void setProphylaxisDate(Date prophylaxisDate) {
		this.prophylaxisDate = prophylaxisDate;
	}

	public YesNo getRiskArea() {
		return riskArea;
	}

	public void setRiskArea(YesNo riskArea) {
		this.riskArea = riskArea;
	}

	public YesNo getLargeAttendanceNumber() {
		return largeAttendanceNumber;
	}

	public void setLargeAttendanceNumber(YesNo largeAttendanceNumber) {
		this.largeAttendanceNumber = largeAttendanceNumber;
	}
}
