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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;

@SuppressWarnings("serial")
public class EntityAuditLogEntry extends HorizontalLayout {

	private final AuditLogEntryDto entry;

	public EntityAuditLogEntry(AuditLogEntryDto entry) {

		setSpacing(true);
		setMargin(false);
		setWidth(100, Unit.PERCENTAGE);
		addStyleName(CssStyles.SORMAS_LIST_ENTRY);
		this.entry = entry;

		VerticalLayout labelLayout = new VerticalLayout();
		labelLayout.setSpacing(false);
		labelLayout.setMargin(false);
		labelLayout.setWidth(100, Unit.PERCENTAGE);
		addComponent(labelLayout);
		setExpandRatio(labelLayout, 1);

		HorizontalLayout topLabelLayout = new HorizontalLayout();
		topLabelLayout.setSpacing(false);
		topLabelLayout.setMargin(false);
		topLabelLayout.setWidth(100, Unit.PERCENTAGE);
		labelLayout.addComponent(topLabelLayout);
		Label labelTopLeft = new Label(ChangeType.toPastTense(entry.getChangeType()));
		CssStyles.style(labelTopLeft, CssStyles.LABEL_BOLD, CssStyles.LABEL_UPPERCASE);
		topLabelLayout.addComponent(labelTopLeft);
		
		Label labelTopRight = new Label(DateFormatHelper.formatLocalDateTime(entry.getDetectionTimestamp()));
		labelTopRight.setSizeUndefined();
		topLabelLayout.addComponent(labelTopRight);
		topLabelLayout.setComponentAlignment(labelTopRight, Alignment.TOP_RIGHT);

		HorizontalLayout middleLabelLayout = new HorizontalLayout();
		middleLabelLayout.setSpacing(false);
		middleLabelLayout.setMargin(false);
		middleLabelLayout.setWidth(100, Unit.PERCENTAGE);
		labelLayout.addComponent(middleLabelLayout);
		Label labelLeft = new Label(entry.getEditingUser().getName());
		middleLabelLayout.addComponent(labelLeft);
	}

	public AuditLogEntryDto getPathogenTest() {
		return entry;
	}
}
