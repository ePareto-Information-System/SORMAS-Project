/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.event.EventGroupReferenceDto;
import de.symeda.sormas.api.uuid.AbstractUuidDto;

import java.util.Date;

public class EbsGroupIndexDto extends AbstractUuidDto {

	private static final long serialVersionUID = 8322646504033924938L;

	public static final String I18N_PREFIX = "EbsGroup";

	public static final String UUID = "uuid";
	public static final String NAME = "name";
	public static final String EBS_COUNT = "ebsCount";
	public static final String CHANGED_DATE = "changeDate";

	private String name;
	private Long ebsCount;
	private Date changeDate;

	public EbsGroupIndexDto(String uuid, String name, Date changeDate, Long eventCount) {
		super(uuid);
		this.name = name;
		this.changeDate = changeDate;
		this.ebsCount = eventCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Long getEbsCount() {
		return ebsCount;
	}

	public void setEbsCount(Long ebsCount) {
		this.ebsCount = ebsCount;
	}

	public EbsGroupReferenceDto toReference() {
		return new EbsGroupReferenceDto(getUuid(), getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EbsGroupIndexDto that = (EbsGroupIndexDto) o;

		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final String uuid = getUuid();
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
}
