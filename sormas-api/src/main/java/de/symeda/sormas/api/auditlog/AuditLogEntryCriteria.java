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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.BaseCriteria;
import de.symeda.sormas.api.utils.YesNoUnknown;

public class AuditLogEntryCriteria extends BaseCriteria implements Cloneable {

	private static final long serialVersionUID = 5114202107622217837L;

	private Type clazz;
	private String uuid;

	private boolean showViews;
	private boolean showCreates;
	private boolean showUpdates;
	private boolean showDeletes;

	private YesNoUnknown showDistinctActivitiesOnly;

	public Type getClazz() {
		return clazz;
	}

	public AuditLogEntryCriteria setClazz(Type clazz) {
		this.clazz = clazz;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public AuditLogEntryCriteria setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public YesNoUnknown showingDistinctActivitiesOnly() {
		return showDistinctActivitiesOnly;
	}

	public AuditLogEntryCriteria showDistinctActivitiesOnly(YesNoUnknown show) {
		this.showDistinctActivitiesOnly = show;
		return this;
	}

	public boolean showingViews() {
		return showViews;
	}

	public AuditLogEntryCriteria showViews(boolean showViews) {
		this.showViews = showViews;
		return this;
	}

	public boolean showingCreates() {
		return showCreates;
	}

	public AuditLogEntryCriteria showCreates(boolean showCreates) {
		this.showCreates = showCreates;
		return this;
	}

	public boolean showingUpdates() {
		return showUpdates;
	}

	public AuditLogEntryCriteria showUpdates(boolean showUpdates) {
		this.showUpdates = showUpdates;
		return this;
	}

	public boolean showingDeletes() {
		return showDeletes;
	}

	public AuditLogEntryCriteria showDeletes(boolean showDeletes) {
		this.showDeletes = showDeletes;
		return this;
	}

	public List<ChangeType> getChangeTypes() {
		List<ChangeType> changeTypes = new ArrayList<ChangeType>();

		if (showingViews()) {
			changeTypes.add(ChangeType.VIEW);
		}

		if (showingCreates()) {
			changeTypes.add(ChangeType.CREATE);
		}

		if (showingUpdates()) {
			changeTypes.add(ChangeType.UPDATE);
		}

		if (showingDeletes()) {
			changeTypes.add(ChangeType.DELETE);
		}

		return changeTypes;
	}
}
