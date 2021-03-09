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
package de.symeda.sormas.ui.auditlog;

import java.util.List;

import com.vaadin.ui.Label;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.auditlog.AuditLogEntryCriteria;
import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.utils.PaginationList;

@SuppressWarnings("serial")
public class EntityAuditLogList extends PaginationList<AuditLogEntryDto> {

	private AuditLogEntryCriteria criteria;

	public EntityAuditLogList(AuditLogEntryCriteria criteria) {
		super(5);

		this.criteria = criteria;
	}

	@Override
	public void reload() {	
		List<AuditLogEntryDto> activities = FacadeProvider.getAuditLogEntryFacade().getList(criteria, null, null, null);

		setEntries(activities);
		if (!activities.isEmpty()) {
			showPage(1);
		} else {
			updatePaginationLayout();
			Label noPathogenTestsLabel = new Label(I18nProperties.getCaption(Captions.EntityAuditLogComponent_noActivities));
			listLayout.addComponent(noPathogenTestsLabel);
		}
	}

	@Override
	protected void drawDisplayedEntries() {
		List<AuditLogEntryDto> displayedEntries = getDisplayedEntries();
		for (int i = 0, displayedEntriesSize = displayedEntries.size(); i < displayedEntriesSize; i++) {
			AuditLogEntryDto pathogenTest = displayedEntries.get(i);
			EntityAuditLogEntry listEntry = new EntityAuditLogEntry(pathogenTest);
			listLayout.addComponent(listEntry);
		}
	}
}
