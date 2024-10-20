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
package de.symeda.sormas.ui.caze.importer;

import java.io.IOException;

import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.importexport.ImportFacade;
import de.symeda.sormas.api.importexport.ValueSeparator;
import de.symeda.sormas.ui.importer.AbstractImportLayout;
import de.symeda.sormas.ui.importer.ImportLayoutComponent;
import de.symeda.sormas.ui.importer.ImportReceiver;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DownloadUtil;

@SuppressWarnings("serial")
public class CaseImportLayout extends AbstractImportLayout {

	public CaseImportLayout() {

		super();

		ImportFacade importFacade = FacadeProvider.getImportFacade();

		addDownloadResourcesComponent(1, new ClassResource("/SORMAS_Import_Guide.pdf"));

		addDownloadImportTemplateComponent(2, importFacade.getCaseImportTemplateFilePath(), importFacade.getCaseImportTemplateFileName());

		addDownloadImportTemplateXlm(0, importFacade.getCaseImportTemplateXlsmFilePath(), importFacade.getCaseImportTemplateXlsmFileName());

		addImportCsvComponent(3, new ImportReceiver("_case_import_", file -> {
			resetDownloadErrorReportButton();

			try {
				CaseImporter importer = new CaseImporter(file, true, currentUser, (ValueSeparator) separator.getValue());
				//importer.startImport(this::extendDownloadErrorReportButton, currentUI, true);
				importer.startImport(resource -> extendDownloadErrorReportButton(resource), currentUI, true,true);


			} catch (IOException | CsvValidationException e) {
				new Notification(
						I18nProperties.getString(Strings.headingImportFailed),
						I18nProperties.getString(Strings.messageImportFailed),
						Type.ERROR_MESSAGE,
						false).show(Page.getCurrent());
			}
		}));

		addDownloadErrorReportComponent(4);




	}

	protected void addDownloadImportTemplateXlm(int step, String templateFilePath, String templateFileName) {
		String headline = I18nProperties.getString(Strings.headingDownloadImportTemplateXlsm);
		String infoText = I18nProperties.getString(Strings.infoDownloadImportTemplateXlsm);
		Resource buttonIcon = VaadinIcons.DOWNLOAD;
		String buttonCaption = I18nProperties.getCaption(Captions.importDownloadImportTemplateXlsm);
		ImportLayoutComponent importTemplateComponent = new ImportLayoutComponent(step, headline, infoText, buttonIcon, buttonCaption);

		try {
			Label lbl =new Label(infoText);

//			Button button = new Button(buttonCaption);
//			button.setIcon(buttonIcon);

			Button button = ButtonHelper.createIconButtonWithCaption(
					"import-step-" + step,
					buttonCaption,
					buttonIcon,
					null,
					ValoTheme.BUTTON_PRIMARY,
					CssStyles.VSPACE_TOP_3);

			DownloadUtil.attachDataCaseTemplateDownloader(button, templateFilePath, templateFileName);

			addComponent(lbl);
			//CssStyles.style(lbl, CssStyles.VSPACE_1);

			CssStyles.style(button, CssStyles.VSPACE_2);

			addComponent(button);
		} catch (Exception e) {
			new Notification(
					I18nProperties.getString(Strings.headingTemplateNotAvailable),
					I18nProperties.getString(Strings.messageTemplateNotAvailable),
					Notification.Type.ERROR_MESSAGE,
					false).show(Page.getCurrent());
		}
	}
}
