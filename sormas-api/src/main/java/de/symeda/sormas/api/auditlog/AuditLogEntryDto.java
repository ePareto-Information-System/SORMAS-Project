/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.auditlog;

import java.util.Date;
import java.util.Map;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.user.UserDto;

/**
 * For saving changes on entities.
 * 
 * @author Oliver Milke
 */
public class AuditLogEntryDto extends EntityDto {

	private static final long serialVersionUID = 2439546041916003652L;

	public static final String ID = "id";
	public static final String CLASS = "clazz";
	public static final String UUID = "uuid";
	public static final String EDITING_USER_ID = "editingUserId";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String DETECTION_TIMESTAMP = "detectionTimestamp";
	public static final String CHANGE_TYPE = "changeType";

	private Long id;

	private String clazz;
	private String uuid;
	private Long editingUserId;
	private UserDto editingUser;
	private String transactionId;
	private Date detectionTimestamp;
	private ChangeType changeType;

	private Map<String, String> attributes;

	public AuditLogEntryDto(Long id, String clazz, String uuid, Long editingUserId, String transactionId,
			Date detectionTimestamp, ChangeType changeType) {

		this.id = id;
		this.clazz = clazz;
		this.uuid = uuid;
		this.editingUserId = editingUserId;
		this.transactionId = transactionId;
		this.detectionTimestamp = detectionTimestamp;
		this.changeType = changeType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return Uuid of the audited entity.
	 */
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public UserDto getEditingUser() {
		return editingUser;
	}

	public void setEditingUser(UserDto editingUser) {
		this.editingUser = editingUser;
	}

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

	/**
	 * @return The timestamp when the change has been detected/logged.
	 */
	public Date getDetectionTimestamp() {
		return detectionTimestamp;
	}

	public void setDetectionTimestamp(Date detectionTimestamp) {
		this.detectionTimestamp = detectionTimestamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public static String getEntityClazz(java.lang.reflect.Type type) {
		return type.toString().replaceAll(".sormas.api.", ".sormas.backend.").replaceAll("ReferenceDto$", "")
				.replaceAll("Dto$", "");
	}
}
