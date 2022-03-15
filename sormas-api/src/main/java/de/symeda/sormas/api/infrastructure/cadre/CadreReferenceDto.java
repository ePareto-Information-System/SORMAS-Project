package de.symeda.sormas.api.infrastructure.cadre;

import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

public class CadreReferenceDto extends InfrastructureDataReferenceDto implements StatisticsGroupingKey {

	public CadreReferenceDto() {
	}

	public CadreReferenceDto(String uuid) {
		super(uuid);
	}

	public CadreReferenceDto(String uuid, String caption, String externalId) {
		super(uuid, caption, externalId);
	}

	@Override
	public String getCaption() {
		return I18nProperties.getContinentName(super.getCaption());
	}

	@Override
	public int keyCompareTo(StatisticsGroupingKey o) {
		if (o == null) {
			throw new NullPointerException("Can't compare to null.");
		}

		if (this.equals(o)) {
			return 0;
		}
		int captionComparison = this.getCaption().compareTo(((CadreReferenceDto) o).getCaption());
		if (captionComparison != 0) {
			return captionComparison;
		} else {
			return this.getUuid().compareTo(((CadreReferenceDto) o).getUuid());
		}
	}
}
