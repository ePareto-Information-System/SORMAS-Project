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

import java.lang.reflect.Type;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.auditlog.AuditLogEntryCriteria;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class EntityAuditLogComponent extends VerticalLayout {

	private EntityAuditLogList list;
	private AuditLogEntryCriteria criteria;

	public EntityAuditLogComponent(Type clazz, String uuid) {
		setWidth(100, Unit.PERCENTAGE);

		HorizontalLayout componentHeader = new HorizontalLayout();
		componentHeader.setWidth(100, Unit.PERCENTAGE);
		addComponent(componentHeader);
		
		criteria = new AuditLogEntryCriteria()
			.setClazz(clazz)
			.setUuid(uuid)
			.showViews(true)
			.showDistinctActivitiesOnly(YesNoUnknown.YES);

		list = new EntityAuditLogList(criteria);
		addComponent(list);
		list.reload();

		Label activitiesHeader = new Label(I18nProperties.getCaption(Captions.EntityAuditLogComponent_heading));
		activitiesHeader.addStyleName(CssStyles.H3);
		componentHeader.addComponent(activitiesHeader);
		
		PopupButton filterButton = CreateFilterButton();
		componentHeader.addComponent(filterButton);
		componentHeader.setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
	}
	
	private PopupButton CreateFilterButton () {
		VerticalLayout layersLayout = new VerticalLayout();
		layersLayout.setMargin(true);
		layersLayout.setSpacing(false);
		layersLayout.setSizeUndefined();
		
		{
			Label infoLabel = new Label(I18nProperties.getCaption(Captions.EntityAuditLogComponent_showUniqueActivities));
			layersLayout.addComponent(infoLabel);
			
			OptionGroup caseClassificationOptions = new OptionGroup();
			caseClassificationOptions.addItems(new YesNoUnknown[] { YesNoUnknown.YES, YesNoUnknown.NO });
			caseClassificationOptions.setValue(criteria.showingDistinctActivitiesOnly());
			caseClassificationOptions.addValueChangeListener(event -> {
				criteria.showDistinctActivitiesOnly((YesNoUnknown) event.getProperty().getValue());
				reload();
			});
			layersLayout.addComponent(caseClassificationOptions);
		}
		
		{
			Label infoLabel = new Label(I18nProperties.getCaption(Captions.EntityAuditLogComponent_selectActivities));
			CssStyles.style(infoLabel, CssStyles.VSPACE_4);
			layersLayout.addComponent(infoLabel);
			
			CheckBox showViews = new CheckBox();
			showViews.setId(ChangeType.VIEW.toString());
			showViews.setCaption(ChangeType.VIEW.toString());
			showViews.setValue(criteria.showingViews());
			showViews.addValueChangeListener(e -> {
				criteria.showViews((boolean) e.getProperty().getValue());
				reload();
			});
			layersLayout.addComponent(showViews);

			CheckBox showCreates = new CheckBox();
			showCreates.setId(ChangeType.CREATE.toString());
			showCreates.setCaption(ChangeType.CREATE.toString());
			showCreates.setValue(criteria.showingCreates());
			showCreates.addValueChangeListener(e -> {
				criteria.showCreates((boolean) e.getProperty().getValue());
				reload();
			});
			layersLayout.addComponent(showCreates);
			
			CheckBox showUpdates = new CheckBox();
			showUpdates.setId(ChangeType.UPDATE.toString());
			showUpdates.setCaption(ChangeType.UPDATE.toString());
			showUpdates.setValue(criteria.showingUpdates());
			showUpdates.addValueChangeListener(e -> {
				criteria.showUpdates((boolean) e.getProperty().getValue());
				reload();
			});
			layersLayout.addComponent(showUpdates);
			
			CheckBox showDeletes = new CheckBox();
			showDeletes.setId(ChangeType.DELETE.toString());
			showDeletes.setCaption(ChangeType.DELETE.toString());
			showDeletes.setValue(criteria.showingDeletes());
			showDeletes.addValueChangeListener(e -> {
				criteria.showDeletes((boolean) e.getProperty().getValue());
				reload();
			});
			layersLayout.addComponent(showDeletes);
		}
			
		PopupButton layersDropdown = ButtonHelper.createPopupButton(Captions.EntityAuditLogComponent_filters, layersLayout, CssStyles.BUTTON_SUBTLE);
		
		return layersDropdown;
	}

	public void reload() {
		list.reload();
	}
}
