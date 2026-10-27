package de.symeda.sormas.backend.caze.samplesexport;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificExportConfigurationFacade;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificExportFieldDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.RightsAllowed;

@Stateless(name = "DiseaseSpecificExportConfigurationFacade")
@RightsAllowed(UserRight._MANAGE_PUBLIC_EXPORT_CONFIGURATION)
public class DiseaseSpecificExportConfigurationFacadeEjb implements DiseaseSpecificExportConfigurationFacade {

	@EJB
	private DiseaseSpecificExportFieldService fieldService;

	@Override
	public List<DiseaseSpecificExportFieldDto> getFields(Disease disease) {
		validateDisease(disease);
		List<DiseaseSpecificExportField> fields = fieldService.getByDisease(disease);
		if (fields.isEmpty()) {
			throw new IllegalStateException(I18nProperties.getString(Strings.errorDiseaseSpecificExportConfigurationMissing));
		}
		return fields.stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public void saveActiveFields(Disease disease, Set<String> activePropertyPaths) {
		validateDisease(disease);
		if (activePropertyPaths == null || activePropertyPaths.isEmpty()) {
			throw new IllegalArgumentException(I18nProperties.getString(Strings.validationDiseaseSpecificExportAtLeastOneField));
		}

		List<DiseaseSpecificExportField> fields = fieldService.getByDisease(disease);
		applyActiveFields(disease, activePropertyPaths, fields);
	}

	@Override
	public byte[] exportFields(Disease disease) {
		validateDisease(disease);
		List<DiseaseSpecificExportField> fields = fieldService.getByDisease(disease);
		if (fields.isEmpty()) {
			throw new IllegalStateException(I18nProperties.getString(Strings.errorDiseaseSpecificExportConfigurationMissing));
		}
		return DiseaseSpecificExportFieldCsv.write(fields);
	}

	@Override
	public void importFields(Disease disease, byte[] csvContent) {
		validateDisease(disease);
		List<DiseaseSpecificExportField> fields = fieldService.getByDisease(disease);
		if (fields.isEmpty()) {
			throw new IllegalStateException(I18nProperties.getString(Strings.errorDiseaseSpecificExportConfigurationMissing));
		}

		List<DiseaseSpecificExportFieldCsv.Row> rows = DiseaseSpecificExportFieldCsv.read(csvContent);
		Set<String> configuredPaths = fields.stream()
			.map(DiseaseSpecificExportField::getPropertyPath)
			.collect(Collectors.toCollection(HashSet::new));
		Set<String> importedPaths = new HashSet<>();
		Set<String> activePaths = new LinkedHashSet<>();
		for (DiseaseSpecificExportFieldCsv.Row row : rows) {
			if (row.getDisease() != disease
				|| !configuredPaths.contains(row.getPropertyPath())
				|| !importedPaths.add(row.getPropertyPath())) {
				throw new IllegalArgumentException("Invalid field in configuration CSV: " + row.getPropertyPath());
			}
			if (row.isActive()) {
				activePaths.add(row.getPropertyPath());
			}
		}
		if (activePaths.isEmpty()) {
			throw new IllegalArgumentException(I18nProperties.getString(Strings.validationDiseaseSpecificExportAtLeastOneField));
		}
		applyActiveFields(disease, activePaths, fields);
	}

	private void applyActiveFields(
		Disease disease,
		Set<String> activePropertyPaths,
		List<DiseaseSpecificExportField> fields) {
		if (fields.isEmpty()) {
			throw new IllegalStateException(I18nProperties.getString(Strings.errorDiseaseSpecificExportConfigurationMissing));
		}
		Set<String> configuredPaths = fields.stream()
			.map(DiseaseSpecificExportField::getPropertyPath)
			.collect(Collectors.toCollection(HashSet::new));
		Set<String> unknownPaths = new HashSet<>(activePropertyPaths);
		unknownPaths.removeAll(configuredPaths);
		if (!unknownPaths.isEmpty()) {
			throw new IllegalArgumentException("Unknown disease-specific export fields for " + disease + ": " + unknownPaths);
		}

		for (DiseaseSpecificExportField field : fields) {
			field.setActive(activePropertyPaths.contains(field.getPropertyPath()));
			fieldService.ensurePersisted(field);
		}
	}

	private void validateDisease(Disease disease) {
		if (!DiseaseSpecificCaseExportSchema.isSupported(disease)) {
			throw new IllegalArgumentException("Unsupported disease for disease-specific export: " + disease);
		}
	}

	private DiseaseSpecificExportFieldDto toDto(DiseaseSpecificExportField source) {
		DiseaseSpecificExportFieldDto target = new DiseaseSpecificExportFieldDto();
		DtoHelper.fillDto(target, source);
		target.setDisease(source.getDisease());
		target.setPropertyPath(source.getPropertyPath());
		target.setDisplayOrder(source.getDisplayOrder());
		target.setActive(source.isActive());
		return target;
	}
}
