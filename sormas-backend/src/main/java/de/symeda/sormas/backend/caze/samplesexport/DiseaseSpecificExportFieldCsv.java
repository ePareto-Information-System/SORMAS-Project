package de.symeda.sormas.backend.caze.samplesexport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.utils.CSVUtils;

final class DiseaseSpecificExportFieldCsv {

	private static final String[] HEADER = {
		"disease",
		"propertyPath",
		"displayOrder",
		"active" };

	private DiseaseSpecificExportFieldCsv() {
	}

	static List<Row> read(byte[] content) {
		if (content == null || content.length == 0) {
			throw new IllegalArgumentException("The disease-specific export configuration CSV is empty");
		}
		return read(new ByteArrayInputStream(content));
	}

	static List<Row> read(InputStream input) {
		try (CSVReader reader = CSVUtils.createBomCsvReader(input)) {
			String[] header = reader.readNext();
			if (!Arrays.equals(HEADER, header)) {
				throw new IllegalArgumentException("Invalid disease-specific export configuration CSV header");
			}

			List<Row> rows = new ArrayList<>();
			String[] values;
			while ((values = reader.readNext()) != null) {
				if (values.length != HEADER.length) {
					throw new IllegalArgumentException("Invalid disease-specific export configuration CSV row");
				}
				boolean active;
				if ("true".equalsIgnoreCase(values[3])) {
					active = true;
				} else if ("false".equalsIgnoreCase(values[3])) {
					active = false;
				} else {
					throw new IllegalArgumentException("Invalid active value in disease-specific export configuration CSV: " + values[3]);
				}
				try {
					rows.add(new Row(Disease.valueOf(values[0]), values[1], Integer.parseInt(values[2]), active));
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("Invalid disease-specific export configuration CSV row: " + Arrays.toString(values), e);
				}
			}
			return rows;
		} catch (IOException | CsvValidationException e) {
			throw new IllegalArgumentException("Could not read disease-specific export configuration CSV", e);
		}
	}

	static byte[] write(List<DiseaseSpecificExportField> fields) {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream();
			CSVWriter writer = CSVUtils.createCSVWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), ',')) {
			writer.writeNext(HEADER, false);
			for (DiseaseSpecificExportField field : fields) {
				writer.writeNext(
					new String[] {
						field.getDisease().name(),
						field.getPropertyPath(),
						String.valueOf(field.getDisplayOrder()),
						String.valueOf(field.isActive()) },
					false);
			}
			writer.flush();
			return output.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException("Could not create disease-specific export configuration CSV", e);
		}
	}

	static final class Row {
		private final Disease disease;
		private final String propertyPath;
		private final int displayOrder;
		private final boolean active;

		private Row(Disease disease, String propertyPath, int displayOrder, boolean active) {
			this.disease = disease;
			this.propertyPath = propertyPath;
			this.displayOrder = displayOrder;
			this.active = active;
		}

		Disease getDisease() {
			return disease;
		}

		String getPropertyPath() {
			return propertyPath;
		}

		int getDisplayOrder() {
			return displayOrder;
		}

		boolean isActive() {
			return active;
		}
	}
}
