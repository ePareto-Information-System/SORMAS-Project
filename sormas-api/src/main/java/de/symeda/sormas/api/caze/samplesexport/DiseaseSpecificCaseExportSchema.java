package de.symeda.sormas.api.caze.samplesexport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.Order;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

/**
 * Ordered column schema for the disease-specific case directory export.
 *
 * Fields are read in form-layout order when an explicit layout exists, and
 * otherwise in their {@link Order} or DTO declaration order. They are then
 * restricted by the same disease/country metadata used by the forms.
 * Form-specific disabled fields are supplied by
 * {@link DiseaseSpecificExportFieldConfiguration}.
 */
public final class DiseaseSpecificCaseExportSchema {

	private static final Set<Disease> SUPPORTED_DISEASES = Collections.unmodifiableSet(EnumSet.of(
		Disease.YELLOW_FEVER,
		Disease.NEONATAL_TETANUS,
		Disease.CSM,
		Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
		Disease.MEASLES,
		Disease.CORONAVIRUS,
		Disease.CHOLERA,
		Disease.AFP,
		Disease.GUINEA_WORM,
		Disease.UNSPECIFIED_VHF,
		Disease.NEW_INFLUENZA,
		Disease.MONKEYPOX,
		Disease.FOODBORNE_ILLNESS));

	private static final Set<String> CASE_CORE_PROPERTIES = new HashSet<>(Arrays.asList(
		EntityDto.UUID,
		CaseDataDto.EPID_NUMBER,
		CaseDataDto.EXTERNAL_ID,
		CaseDataDto.EXTERNAL_TOKEN,
		CaseDataDto.INTERNAL_TOKEN,
		CaseDataDto.DISEASE,
		CaseDataDto.DISEASE_DETAILS,
		CaseDataDto.DISEASE_VARIANT,
		CaseDataDto.DISEASE_VARIANT_DETAILS));

	private static final Set<String> CASE_AGGREGATES = new HashSet<>(Arrays.asList(
		CaseDataDto.PERSON,
		CaseDataDto.HOSPITALIZATION,
		CaseDataDto.SYMPTOMS,
		CaseDataDto.EPI_DATA,
		CaseDataDto.HEALTH_CONDITIONS,
		CaseDataDto.SIXTY_DAY,
		CaseDataDto.AFP_IMMUNIZATION,
		CaseDataDto.FOOD_HISTORY_TAB,
		CaseDataDto.RISK_FACTOR,
		CaseDataDto.INVESTIGATION_NOTES,
		CaseDataDto.THERAPY,
		CaseDataDto.CLINICAL_COURSE,
		CaseDataDto.MATERNAL_HISTORY,
		CaseDataDto.PORT_HEALTH_INFO));

	private static final Set<String> TECHNICAL_PROPERTIES = new HashSet<>(Arrays.asList(
		EntityDto.CREATION_DATE,
		EntityDto.CHANGE_DATE,
		"deleted",
		"sormasToSormasOriginInfo",
		"localChangeDate",
		"pseudonymized"));

	private DiseaseSpecificCaseExportSchema() {
	}

	public static boolean isSupported(Disease disease) {
		return disease != null && SUPPORTED_DISEASES.contains(disease);
	}

	public static Set<Disease> getSupportedDiseases() {
		return SUPPORTED_DISEASES;
	}

	public static boolean hasSamples(Disease disease) {
		return disease != Disease.NEONATAL_TETANUS && disease != Disease.FOODBORNE_ILLNESS;
	}

	public static boolean hasSymptoms(Disease disease) {
		return disease != Disease.FOODBORNE_ILLNESS;
	}

	public static boolean hasEpiData(Disease disease) {
		return disease != Disease.MONKEYPOX && disease != Disease.NEONATAL_TETANUS;
	}

	public static List<Column> build(Disease disease, String countryLocale) {
		if (!isSupported(disease)) {
			throw new IllegalArgumentException("Unsupported disease for disease-specific export: " + disease);
		}

		List<Column> columns = new ArrayList<>();
		columns.add(Column.synthetic(Block.CASE, "country", String.class));
		columns.add(Column.synthetic(Block.CASE, "id", Long.class));
		addProperty(columns, Block.CASE, EntityDto.UUID, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.EPID_NUMBER, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.EXTERNAL_ID, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.EXTERNAL_TOKEN, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.INTERNAL_TOKEN, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.DISEASE, Disease.class);
		addProperty(columns, Block.CASE, CaseDataDto.DISEASE_DETAILS, String.class);
		addProperty(columns, Block.CASE, CaseDataDto.DISEASE_VARIANT, Object.class);
		addProperty(columns, Block.CASE, CaseDataDto.DISEASE_VARIANT_DETAILS, String.class);

		addDtoFields(columns, Block.CASE, disease, countryLocale, union(CASE_CORE_PROPERTIES, CASE_AGGREGATES));
		addDtoFields(columns, Block.PERSON, disease, countryLocale, Collections.singleton(PersonDto.ADDRESS));
		addDtoFields(columns, Block.LOCATION, disease, countryLocale, Collections.<String>emptySet());
		addDtoFields(columns, Block.HOSPITALIZATION, disease, countryLocale, Collections.<String>emptySet());

		if (!DiseaseSpecificExportFieldConfiguration.isDisabled(CaseDataDto.class, CaseDataDto.HEALTH_CONDITIONS, disease)) {
			addDtoFields(columns, Block.HEALTH_CONDITIONS, disease, countryLocale, Collections.<String>emptySet());
		}
		if (hasSymptoms(disease)) {
			addDtoFields(columns, Block.SYMPTOMS, disease, countryLocale, Collections.<String>emptySet());
		}
		if (hasEpiData(disease)) {
			addDtoFields(columns, Block.EPI_DATA, disease, countryLocale, Collections.<String>emptySet());
		}

		if (disease == Disease.AFP) {
			addDtoFields(columns, Block.SIXTY_DAY, disease, countryLocale, Collections.<String>emptySet());
			addDtoFields(columns, Block.AFP_IMMUNIZATION, disease, countryLocale, Collections.<String>emptySet());
		} else if (disease == Disease.CHOLERA || disease == Disease.MONKEYPOX) {
			addDtoFields(columns, Block.RISK_FACTOR, disease, countryLocale, Collections.<String>emptySet());
		} else if (disease == Disease.FOODBORNE_ILLNESS) {
			addDtoFields(columns, Block.FOOD_HISTORY, disease, countryLocale, Collections.<String>emptySet());
			addDtoFields(columns, Block.SIXTY_DAY, disease, countryLocale, Collections.<String>emptySet());
			addDtoFields(columns, Block.INVESTIGATION_NOTES, disease, countryLocale, Collections.<String>emptySet());
		}

		if (hasSamples(disease)) {
			addDtoFields(columns, Block.SAMPLE, disease, countryLocale, new HashSet<>(Arrays.asList(
				"associatedCase",
				"associatedContact",
				"associatedEventParticipant")));
			addDtoFields(columns, Block.PATHOGEN_TEST, disease, countryLocale, Collections.singleton(PathogenTestDto.SAMPLE));
		}

		Set<String> paths = new HashSet<>();
		for (Column column : columns) {
			if (!paths.add(column.getPropertyPath())) {
				throw new IllegalStateException("Duplicate disease export property path: " + column.getPropertyPath());
			}
		}
		return Collections.unmodifiableList(columns);
	}

	private static Set<String> union(Set<String> first, Set<String> second) {
		Set<String> result = new HashSet<>(first);
		result.addAll(second);
		return result;
	}

	private static void addProperty(List<Column> columns, Block block, String property, Class<?> type) {
		columns.add(new Column(block, property, type, false));
	}

	private static void addDtoFields(
		List<Column> columns,
		Block block,
		Disease disease,
		String countryLocale,
		Set<String> excludedProperties) {

		Class<?> dtoClass = block.getDtoClass();
		FieldVisibilityCheckers visibility = FieldVisibilityCheckers.withDisease(disease).andWithCountry(countryLocale);

		if (EntityDto.class.isAssignableFrom(dtoClass)
			&& columns.stream().noneMatch(c -> c.getBlock() == block && EntityDto.UUID.equals(c.getProperty()))) {
			addProperty(columns, block, EntityDto.UUID, String.class);
		}

		List<OrderedField> fields = new ArrayList<>();
		Field[] declaredFields = dtoClass.getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			if (Modifier.isStatic(field.getModifiers())
				|| field.isSynthetic()
				|| excludedProperties.contains(field.getName())
				|| TECHNICAL_PROPERTIES.contains(field.getName())
				|| !isExportableType(field.getType())
				|| !visibility.isVisible(dtoClass, field)
				|| !DiseaseSpecificExportFieldConfiguration.isLayoutField(dtoClass, field.getName(), disease)
				|| DiseaseSpecificExportFieldConfiguration.isDisabled(dtoClass, field.getName(), disease)) {
				continue;
			}
			Method getter = findGetter(dtoClass, field);
			if (getter == null) {
				continue;
			}
			Order order = getter.getAnnotation(Order.class);
			Integer layoutOrder = DiseaseSpecificExportFieldConfiguration.getLayoutOrder(dtoClass, field.getName(), disease);
			fields.add(new OrderedField(field, layoutOrder != null ? layoutOrder : order != null ? order.value() : Integer.MAX_VALUE, i));
		}

		fields.sort(Comparator.comparingInt(OrderedField::getOrder).thenComparingInt(OrderedField::getDeclarationIndex));
		for (OrderedField orderedField : fields) {
			Field field = orderedField.getField();
			addProperty(columns, block, field.getName(), field.getType());
		}
	}

	private static Method findGetter(Class<?> dtoClass, Field field) {
		String suffix = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
		for (String prefix : field.getType() == boolean.class ? Arrays.asList("is", "get") : Collections.singletonList("get")) {
			try {
				return dtoClass.getMethod(prefix + suffix);
			} catch (NoSuchMethodException e) {
				// Try the next JavaBean prefix.
			}
		}
		return null;
	}

	private static boolean isExportableType(Class<?> type) {
		return type.isPrimitive()
			|| Number.class.isAssignableFrom(type)
			|| CharSequence.class.isAssignableFrom(type)
			|| Boolean.class == type
			|| Character.class == type
			|| Date.class.isAssignableFrom(type)
			|| Temporal.class.isAssignableFrom(type)
			|| Enum.class.isAssignableFrom(type)
			|| ReferenceDto.class.isAssignableFrom(type)
			|| Collection.class.isAssignableFrom(type);
	}

	public enum Block {
		CASE("", CaseDataDto.class, CaseDataDto.I18N_PREFIX),
		PERSON("person", PersonDto.class, PersonDto.I18N_PREFIX),
		LOCATION("person.address", LocationDto.class, LocationDto.I18N_PREFIX),
		HOSPITALIZATION("hospitalization", HospitalizationDto.class, HospitalizationDto.I18N_PREFIX),
		HEALTH_CONDITIONS("healthConditions", HealthConditionsDto.class, HealthConditionsDto.I18N_PREFIX),
		SYMPTOMS("symptoms", SymptomsDto.class, SymptomsDto.I18N_PREFIX),
		EPI_DATA("epiData", EpiDataDto.class, EpiDataDto.I18N_PREFIX),
		SIXTY_DAY("sixtyDay", SixtyDayDto.class, SixtyDayDto.I18N_PREFIX),
		AFP_IMMUNIZATION("afpImmunization", AfpImmunizationDto.class, AfpImmunizationDto.I18N_PREFIX),
		RISK_FACTOR("riskFactor", RiskFactorDto.class, RiskFactorDto.I18N_PREFIX),
		FOOD_HISTORY("foodHistory", FoodHistoryDto.class, FoodHistoryDto.I18N_PREFIX),
		INVESTIGATION_NOTES("investigationNotes", InvestigationNotesDto.class, InvestigationNotesDto.I18N_PREFIX),
		SAMPLE("sample", SampleDto.class, SampleDto.I18N_PREFIX),
		PATHOGEN_TEST("pathogenTests", PathogenTestDto.class, PathogenTestDto.I18N_PREFIX);

		private final String pathPrefix;
		private final Class<?> dtoClass;
		private final String i18nPrefix;

		Block(String pathPrefix, Class<?> dtoClass, String i18nPrefix) {
			this.pathPrefix = pathPrefix;
			this.dtoClass = dtoClass;
			this.i18nPrefix = i18nPrefix;
		}

		public String getPathPrefix() {
			return pathPrefix;
		}

		public Class<?> getDtoClass() {
			return dtoClass;
		}

		public String getI18nPrefix() {
			return i18nPrefix;
		}
	}

	public static final class Column {
		private final Block block;
		private final String property;
		private final Class<?> type;
		private final boolean synthetic;

		private Column(Block block, String property, Class<?> type, boolean synthetic) {
			this.block = block;
			this.property = property;
			this.type = type;
			this.synthetic = synthetic;
		}

		private static Column synthetic(Block block, String property, Class<?> type) {
			return new Column(block, property, type, true);
		}

		public Block getBlock() {
			return block;
		}

		public String getProperty() {
			return property;
		}

		public String getPropertyPath() {
			return block.getPathPrefix().isEmpty() ? property : block.getPathPrefix() + "." + property;
		}

		public Class<?> getType() {
			return type;
		}

		public boolean isSynthetic() {
			return synthetic;
		}
	}

	private static final class OrderedField {
		private final Field field;
		private final int order;
		private final int declarationIndex;

		private OrderedField(Field field, int order, int declarationIndex) {
			this.field = field;
			this.order = order;
			this.declarationIndex = declarationIndex;
		}

		private Field getField() {
			return field;
		}

		private int getOrder() {
			return order;
		}

		private int getDeclarationIndex() {
			return declarationIndex;
		}
	}
}
