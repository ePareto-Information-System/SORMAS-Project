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
package de.symeda.sormas.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.UserProvider.HasUserProvider;
import de.symeda.sormas.ui.ViewModelProviders.HasViewModelProviders;
import de.symeda.sormas.ui.utils.SormasDefaultConverterFactory;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@SuppressWarnings("serial")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("sormas")
@Widgetset("de.symeda.sormas.SormasWidgetset")
public class SormasUI extends UI implements HasUserProvider, HasViewModelProviders {

	private final UserProvider userProvider = new UserProvider();
	private final ViewModelProviders viewModelProviders = new ViewModelProviders();
	private final static  String[][] caseBasedDiseases = {
			{Disease.YELLOW_FEVER.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.AFP.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "60-Days", "AFP-Immunization", "samples"},
			{Disease.AHF.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.CSM.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "contacts", "samples"},
			{Disease.MEASLES.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.CHOLERA.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "riskFactor", "samples"},
			{Disease.CORONAVIRUS.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "therapy", "clinicalcourse", "visits", "contacts", "samples"},
			{Disease.GUINEA_WORM.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.NEW_INFLUENZA.getName(), "data", "person", "hospitalization", "symptoms", "epidata", "samples"},
			{Disease.NEONATAL_TETANUS.getName(), "data", "person", "hospitalization", "symptoms"},
			{Disease.FOODBORNE_ILLNESS.getName(), "data", "person", "hospitalization", "Food-History", "epidata", "60-Days", "investigationnotes"},
			{Disease.MONKEYPOX.getName(), "data", "hospitalization", "symptoms", "riskFactor", "samples"},
	};

	@Override
	public void init(VaadinRequest vaadinRequest) {

		setErrorHandler(SormasErrorHandler.get());
		setLocale(vaadinRequest.getLocale());

		Responsive.makeResponsive(this);

		VaadinSession.getCurrent().setConverterFactory(new SormasDefaultConverterFactory());

		getPage().setTitle(FacadeProvider.getConfigFacade().getSormasInstanceName());

		initMainScreen();
	}

	protected void initMainScreen() {
		addStyleName(ValoTheme.UI_WITH_MENU);
		setContent(new MainScreen(SormasUI.this));
		
		initUserActivityLogging();
	}

	public static SormasUI get() {
		return (SormasUI) UI.getCurrent();
	}

	@Override
	public UserProvider getUserProvider() {
		return userProvider;
	}

	@Override
	public ViewModelProviders getViewModelProviders() {
		return viewModelProviders;
	}

	@WebServlet(urlPatterns = {
		"/*" }, name = "SormasUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SormasUI.class, productionMode = false)
	@ServletSecurity(@HttpConstraint(rolesAllowed = UserRight._SORMAS_UI))
	public static class SormasUIServlet extends VaadinServlet {

	}

	public static void refreshView() {
		get().getNavigator().navigateTo(get().getNavigator().getState());
	}

	public static void navigateToCaseChild() {
		String currentState = getCurrent().getNavigator().getState();
		String caseUuid = extractCaseId(currentState);

		String nextViewName = getNextViewName(caseUuid);
		if (nextViewName.isEmpty()) {
			return;
		}
		String navigationState = "cases/" + nextViewName + "/" + caseUuid;
		get().getNavigator().navigateTo(navigationState);
	}

	public static String getNextViewName(String caseUuid) {

		String currentState = getCurrent().getNavigator().getState();
		String path = removeQueryParameters(currentState);
		String currentLocation = "";
		currentLocation = path.substring(path.indexOf("/") + 1, path.lastIndexOf("/"));
		Disease currentDisease = FacadeProvider.getCaseFacade().getByUuid(caseUuid).getDisease();
		if (currentDisease == null) {
			return "";
		}


		String[] result = getSubArrayByFirstElement(currentDisease.getName());
		if (result == null) {
			return "";
		}
		result = java.util.Arrays.stream(result).filter(e -> hasPermission(caseUuid, e)).toArray(String[]::new);

		String nextViewName = "";
		for (int i = 0; i < result.length; i++) {
			if (result[i].equals(currentLocation)) {
				if (i + 1 < result.length) {
					nextViewName = result[i + 1];
				}
				break;
			}
		}

		return nextViewName;
	}

	public static boolean hasPermission(String caseUuid, String nextViewName) {
		UserProvider userProvider = UserProvider.getCurrent();
		switch (nextViewName) {
			case "contacts":
				if (!userProvider.hasUserRight(UserRight.CONTACT_VIEW)) {
					return false;
				}
				break;
			case "samples":
				if (!FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.SAMPLES_LAB) || !userProvider.hasUserRight(UserRight.SAMPLE_VIEW) || !userProvider.hasUserRight(UserRight.SAMPLE_EDIT)) {
					return false;
				}
				break;
			case "visits":
				if (!FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.CASE_FOLLOWUP)) {
					return false;
				}
				break;
			case "therapy":
				if (!userProvider.hasUserRight(UserRight.THERAPY_VIEW)) {
					return false;
				}
				break;
			case "clinicalcourse":
				if (!userProvider.hasUserRight(UserRight.CLINICAL_COURSE_VIEW)) {
					return false;
				}
				break;
			default:
				break;
		}
		return true;
	}

	public static String[] getSubArrayByFirstElement(String firstElement) {
		for (String[] array : caseBasedDiseases) {
			if (array.length > 0 && array[0].equals(firstElement)) {
				return array;
			}
		}
		return null; // or you could throw an exception or return an empty array
	}

	public static String extractCaseId(String query) {
		query = removeQueryParameters(query);
		String caseUuid = query.substring(query.lastIndexOf("/") + 1);
		return caseUuid;
	}

	//remove query parameters from the URL
	public static String removeQueryParameters(String query) {
		int queryStart = query.indexOf("?");
		if (queryStart != -1) {
			query = query.substring(0, queryStart);
		}
		if (query.endsWith("/")) {
			query = query.substring(0, query.length() - 1);
		}
		return query;
	}
	
	private void initUserActivityLogging () {
		JavaScript.getCurrent().addFunction("de.symeda.sormas.ui.auditlog.logActivity",
		   new JavaScriptFunction() {
				@Override
				public void call(JsonArray arguments) {
					
					JsonObject params = arguments.getObject(0);
					String activityType = params.getString("activityType");
					String clazz = params.getString("clazz");
					String uuid = params.getString("uuid");
					
					ChangeType changeType = ChangeType.valueOf(activityType);
					
					FacadeProvider.getAuditLogEntryFacade().logActivity(changeType, clazz, uuid);
				}
			}
		);
	}
}
