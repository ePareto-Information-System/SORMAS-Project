package de.symeda.sormas.backend.caze.samplesexport;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVWriter;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseExportDetailedSampleDto;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Block;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Column;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificExportFieldConfiguration;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.CSVCommentLineValidator;
import de.symeda.sormas.api.utils.CSVUtils;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.Order;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;

class DiseaseSpecificCaseExportWriter implements Closeable {

	private static final String ITEM_SEPARATOR = " | ";
	private static final String ATTRIBUTE_SEPARATOR = "; ";

	private final CSVWriter writer;
	private final List<Column> columns;
	private final Language language;
	private final String countryLocale;

	DiseaseSpecificCaseExportWriter(
		OutputStream outputStream,
		char separator,
		List<Column> columns,
		Language language,
		String countryLocale) throws IOException {

		this.writer = CSVUtils.createCSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8.name()), separator);
		this.columns = columns;
		this.language = language != null ? language : Language.EN;
		this.countryLocale = countryLocale;
		I18nProperties.setUserLanguage(this.language);
		writeHeaders();
	}

	private void writeHeaders() {
		String[] entityNames = new String[columns.size()];
		String[] propertyPaths = new String[columns.size()];
		String[] captions = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);
			entityNames[i] = DataHelper.getHumanClassName(column.getBlock().getDtoClass());
			propertyPaths[i] = column.getPropertyPath();
			String caption = I18nProperties.findPrefixCaption(
				column.getProperty(),
				CaseExportDetailedSampleDto.I18N_PREFIX,
				column.getBlock().getI18nPrefix(),
				CaseDataDto.I18N_PREFIX);
			captions[i] = caption != null ? caption : column.getProperty();
			if (Date.class.isAssignableFrom(column.getType())) {
				captions[i] += " (" + DateHelper.getLocalDateFormat(language).toPattern() + ")";
			}
		}
		captions[0] = CSVCommentLineValidator.DEFAULT_COMMENT_LINE_PREFIX + captions[0];
		writer.writeNext(entityNames);
		writer.writeNext(propertyPaths);
		writer.writeNext(captions, false);
	}

	void writeRow(DiseaseSpecificCaseExportRow row) {
		String[] values = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			values[i] = readValue(row, columns.get(i));
		}
		writer.writeNext(values);
	}

	void flush() throws IOException {
		writer.flush();
	}

	private String readValue(DiseaseSpecificCaseExportRow row, Column column) {
		try {
			Disease disease = row.getCaze().getDisease();
			if (column.isSynthetic()) {
				if ("country".equals(column.getProperty())) {
					return row.getCountry();
				}
				if ("id".equals(column.getProperty())) {
					return DataHelper.valueToString(row.getCaseId());
				}
			}

			if (column.getBlock() == Block.PATHOGEN_TEST) {
				List<String> testValues = new ArrayList<>();
				for (Object test : row.getPathogenTests()) {
					String value = formatValue(invokeGetter(test, column.getProperty()), disease);
					if (!value.isEmpty()) {
						testValues.add(value);
					}
				}
				return String.join(ITEM_SEPARATOR, testValues);
			}

			Object source = getBlockSource(row, column.getBlock());
			return source == null ? "" : formatValue(invokeGetter(source, column.getProperty()), disease);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Could not read export property " + column.getPropertyPath(), e);
		}
	}

	private Object getBlockSource(DiseaseSpecificCaseExportRow row, Block block) {
		switch (block) {
		case CASE:
			return row.getCaze();
		case PERSON:
			return row.getPerson();
		case LOCATION:
			return row.getPerson() != null ? row.getPerson().getAddress() : null;
		case HOSPITALIZATION:
			return row.getCaze().getHospitalization();
		case HEALTH_CONDITIONS:
			return row.getCaze().getHealthConditions();
		case SYMPTOMS:
			return row.getCaze().getSymptoms();
		case EPI_DATA:
			return row.getCaze().getEpiData();
		case SIXTY_DAY:
			return row.getCaze().getSixtyDay();
		case AFP_IMMUNIZATION:
			return row.getCaze().getAfpImmunization();
		case RISK_FACTOR:
			return row.getCaze().getRiskFactor();
		case FOOD_HISTORY:
			return row.getCaze().getFoodHistory();
		case INVESTIGATION_NOTES:
			return row.getCaze().getInvestigationNotes();
		case SAMPLE:
			return row.getSample();
		default:
			return null;
		}
	}

	private Object invokeGetter(Object target, String property) throws ReflectiveOperationException {
		if (EntityDto.UUID.equals(property) && target instanceof EntityDto) {
			return ((EntityDto) target).getUuid();
		}
		String suffix = Character.toUpperCase(property.charAt(0)) + property.substring(1);
		try {
			return target.getClass().getMethod("get" + suffix).invoke(target);
		} catch (NoSuchMethodException e) {
			return target.getClass().getMethod("is" + suffix).invoke(target);
		}
	}

	private String formatValue(Object value, Disease disease) {
		if (!(value instanceof Collection)) {
			return DataHelper.valueToString(value);
		}
		List<Object> items = new ArrayList<>((Collection<?>) value);
		items.sort(Comparator.comparing(this::dateSortKey).thenComparing(this::uuidSortKey).thenComparing(DataHelper::valueToString));
		List<String> serialized = new ArrayList<>();
		for (Object item : items) {
			serialized.add(serializeItem(item, disease));
		}
		return String.join(ITEM_SEPARATOR, serialized);
	}

	private String serializeItem(Object item, Disease disease) {
		if (item == null) {
			return "";
		}
		if (item instanceof CharSequence || item instanceof Number || item instanceof Boolean || item instanceof Date || item instanceof Enum
			|| item instanceof ReferenceDto) {
			return DataHelper.valueToString(item);
		}

		List<String> attributes = new ArrayList<>();
		if (item instanceof EntityDto) {
			attributes.add(EntityDto.UUID + "=" + ((EntityDto) item).getUuid());
		}
		List<Field> fields = new ArrayList<>();
		for (Field field : item.getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic() || Collection.class.isAssignableFrom(field.getType())) {
				continue;
			}
			if (!FieldVisibilityCheckers.withDisease(disease).andWithCountry(countryLocale).isVisible(item.getClass(), field)
				|| DiseaseSpecificExportFieldConfiguration.isDisabled(item.getClass(), field.getName(), disease)) {
				continue;
			}
			fields.add(field);
		}
		fields.sort(Comparator.comparingInt(field -> fieldOrder(item.getClass(), field)));
		for (Field field : fields) {
			try {
				Object value = invokeGetter(item, field.getName());
				String formatted = DataHelper.valueToString(value);
				if (!formatted.isEmpty()) {
					attributes.add(field.getName() + "=" + formatted);
				}
			} catch (ReflectiveOperationException e) {
				// Non-bean and back-reference fields are not part of the compact representation.
			}
		}
		return String.join(ATTRIBUTE_SEPARATOR, attributes);
	}

	private int fieldOrder(Class<?> type, Field field) {
		try {
			Method getter = type.getMethod("get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1));
			Order order = getter.getAnnotation(Order.class);
			return order != null ? order.value() : Integer.MAX_VALUE;
		} catch (NoSuchMethodException e) {
			return Integer.MAX_VALUE;
		}
	}

	private Date dateSortKey(Object value) {
		if (value == null) {
			return new Date(Long.MAX_VALUE);
		}
		String dateProperty = relevantDateProperty(value.getClass().getSimpleName());
		if (dateProperty != null) {
			try {
				Object date = invokeGetter(value, dateProperty);
				if (date instanceof Date) {
					return (Date) date;
				}
			} catch (ReflectiveOperationException e) {
				// Fall through to UUID ordering when a child record has no readable date.
			}
		}
		return new Date(Long.MAX_VALUE);
	}

	private String relevantDateProperty(String simpleClassName) {
		switch (simpleClassName) {
		case "PreviousHospitalizationDto":
			return "admissionDate";
		case "ExposureDto":
		case "ActivityAsCaseDto":
			return "startDate";
		case "PersonTravelHistoryDto":
			return "dateFrom";
		case "ContaminationSourceDto":
			return "abateTreatmentDate";
		case "ContainmentMeasureDto":
			return "dateWormDetectedEmergence";
		case "AffectedPersonDto":
			return "dateTime";
		case "PatientTravelDetailsPriorDto":
		case "PatientTravelDetailsDuringDto":
			return "dateOfTravel";
		default:
			return null;
		}
	}

	private String uuidSortKey(Object value) {
		return value instanceof EntityDto ? String.valueOf(((EntityDto) value).getUuid()) : "";
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}
