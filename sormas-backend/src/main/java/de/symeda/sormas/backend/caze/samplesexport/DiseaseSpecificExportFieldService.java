package de.symeda.sormas.backend.caze.samplesexport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;
import de.symeda.sormas.backend.common.BaseAdoService;

@Stateless
@LocalBean
public class DiseaseSpecificExportFieldService extends BaseAdoService<DiseaseSpecificExportField> {

	public DiseaseSpecificExportFieldService() {
		super(DiseaseSpecificExportField.class);
	}

	public List<DiseaseSpecificExportField> getByDisease(Disease disease) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DiseaseSpecificExportField> cq = cb.createQuery(getElementClass());
		Root<DiseaseSpecificExportField> root = cq.from(getElementClass());
		cq.where(cb.equal(root.get(DiseaseSpecificExportField.DISEASE), disease));
		cq.orderBy(cb.asc(root.get(DiseaseSpecificExportField.DISPLAY_ORDER)));
		return em.createQuery(cq).getResultList();
	}

	public List<String> getActivePropertyPaths(Disease disease) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<DiseaseSpecificExportField> root = cq.from(getElementClass());
		cq.select(root.get(DiseaseSpecificExportField.PROPERTY_PATH));
		cq.where(
			cb.equal(root.get(DiseaseSpecificExportField.DISEASE), disease),
			cb.isTrue(root.get(DiseaseSpecificExportField.ACTIVE)));
		cq.orderBy(cb.asc(root.get(DiseaseSpecificExportField.DISPLAY_ORDER)));
		return em.createQuery(cq).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void seedFromCsvResources() {
		List<String> allCatalogPaths = DiseaseSpecificCaseExportSchema.buildAllFields("en-GH")
			.stream()
			.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
			.collect(Collectors.toList());

		for (Disease disease : DiseaseSpecificCaseExportSchema.getSupportedDiseases()) {
			List<DiseaseSpecificExportField> existingFields = getByDisease(disease);
			if (existingFields.isEmpty()) {
				List<DiseaseSpecificExportFieldCsv.Row> rows = readSeedRows(disease);
				validateSeedRows(disease, rows);
				for (DiseaseSpecificExportFieldCsv.Row row : rows) {
					DiseaseSpecificExportField field = createField(
						disease,
						row.getPropertyPath(),
						row.getDisplayOrder(),
						row.isActive());
					persist(field);
					existingFields.add(field);
				}
			}

			Set<String> existingPaths = existingFields.stream()
				.map(DiseaseSpecificExportField::getPropertyPath)
				.collect(Collectors.toCollection(HashSet::new));
			int nextDisplayOrder = existingFields.stream()
				.mapToInt(DiseaseSpecificExportField::getDisplayOrder)
				.max()
				.orElse(-1) + 1;
			for (String propertyPath : allCatalogPaths) {
				if (existingPaths.add(propertyPath)) {
					persist(createField(disease, propertyPath, nextDisplayOrder++, false));
				}
			}
		}
		em.flush();
	}

	private List<DiseaseSpecificExportFieldCsv.Row> readSeedRows(Disease disease) {
		String resourceName = "disease-specific-export-fields/" + disease.name() + ".csv";
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		if (input == null) {
			throw new IllegalStateException("Missing disease-specific export seed file: " + resourceName);
		}
		return DiseaseSpecificExportFieldCsv.read(input);
	}

	private DiseaseSpecificExportField createField(Disease disease, String propertyPath, int displayOrder, boolean active) {
		DiseaseSpecificExportField field = new DiseaseSpecificExportField();
		field.setDisease(disease);
		field.setPropertyPath(propertyPath);
		field.setDisplayOrder(displayOrder);
		field.setActive(active);
		return field;
	}

	private void validateSeedRows(Disease disease, List<DiseaseSpecificExportFieldCsv.Row> rows) {
		List<String> catalogPaths = DiseaseSpecificCaseExportSchema.build(disease, "en-GH")
			.stream()
			.map(DiseaseSpecificCaseExportSchema.Column::getPropertyPath)
			.collect(Collectors.toCollection(ArrayList::new));
		if (rows.size() != catalogPaths.size()) {
			throw new IllegalStateException("Wrong number of fields in disease-specific export seed file for " + disease);
		}
		for (int i = 0; i < rows.size(); i++) {
			DiseaseSpecificExportFieldCsv.Row row = rows.get(i);
			if (row.getDisease() != disease
				|| row.getDisplayOrder() != i
				|| !catalogPaths.get(i).equals(row.getPropertyPath())
				|| !row.isActive()) {
				throw new IllegalStateException("Invalid disease-specific export seed row " + i + " for " + disease);
			}
		}
	}
}
