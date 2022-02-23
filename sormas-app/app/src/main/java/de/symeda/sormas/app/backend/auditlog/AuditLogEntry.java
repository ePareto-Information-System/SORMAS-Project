/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.app.backend.auditlog;

//import static de.symeda.sormas.api.EntityDto.COLUMN_LENGTH_BIG;
//import static de.symeda.sormas.api.EntityDto.COLUMN_LENGTH_DEFAULT;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;

@Entity(name = AuditLogEntry.TABLE_NAME)
@DatabaseTable(tableName = AuditLogEntry.TABLE_NAME)
public class AuditLogEntry extends AbstractDomainObject {

	private static final long serialVersionUID = -7196712070188634978L;

	public static final String TABLE_NAME = "auditlogentry";
	public static final String I18N_PREFIX = "AuditLogEntry";

//	public static final String SAMPLE_DATE_TIME = "sampleDateTime";

//	@DatabaseField
//	private Long id;

//	@Column(length = COLUMN_LENGTH_DEFAULT)
	private String clazz;

//	@Column(length = COLUMN_LENGTH_DEFAULT)
//	private String uuid;

//	@Column(length = COLUMN_LENGTH_DEFAULT)
	private String editingUser;

	@DatabaseField
	private Long editingUserId;

//	@Column(length = COLUMN_LENGTH_DEFAULT)
	private String transactionId;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date detectionTimestamp;

	@Enumerated(EnumType.STRING)
	private ChangeType changeType;

//	private Map<String, String> attributes;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

//	public String getUuid() {
//		return uuid;
//	}

//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}

	public Long getEditingUserId() {
		return editingUserId;
	}

	public void setEditingUserId(Long editingUserId) {
		this.editingUserId = editingUserId;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public Date getDetectionTimestamp() {
		return detectionTimestamp;
	}

	public void setDetectionTimestamp(Date detectionTimestamp) {
		this.detectionTimestamp = detectionTimestamp;
	}

	public static String getEntityClazz (java.lang.reflect.Type type) {
		return type.toString()
				.replace("class ", "")
				.replaceAll(".sormas.api.", ".sormas.backend.")
				.replaceAll("ReferenceDto$", "")
				.replaceAll("Dto$", "");
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}
}
