package de.symeda.sormas.api.caze.samplesexport;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;

public class DiseaseSpecificExportFieldDto extends EntityDto {

	private static final long serialVersionUID = 1L;

	public static final String I18N_PREFIX = "DiseaseSpecificExportField";
	public static final String DISEASE = "disease";
	public static final String PROPERTY_PATH = "propertyPath";
	public static final String DISPLAY_ORDER = "displayOrder";
	public static final String ACTIVE = "active";

	private Disease disease;
	private String propertyPath;
	private int displayOrder;
	private boolean active;

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
