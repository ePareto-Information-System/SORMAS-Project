package de.symeda.sormas.api.infrastructure.cadre;

import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.PersonalData;

public class CadreReferenceDto extends ReferenceDto {

	@PersonalData
	private String position;
	@PersonalData
	private String externalId;

	public CadreReferenceDto() {
	}

	public CadreReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public CadreReferenceDto(String uuid, String position, String externalId) {
		setUuid(uuid);

		this.externalId = externalId;
		this.position = position;
	}

	@Override
	public String getCaption() {
		return buildCaption(getUuid(), position);
	}

	private String buildCaption(String uuid, String position) {
		String positionName = CadreDto.buildCaption(position);
		String shortUuid = DataHelper.getShortUuid(uuid);

		if (positionName.trim().length() > 0) {
			return positionName + " (" + shortUuid + ")";
		}

		return shortUuid;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
