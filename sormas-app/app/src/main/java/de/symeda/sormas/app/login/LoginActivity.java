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

package de.symeda.sormas.app.login;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import java.util.Set;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRoleDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.app.BaseLocalizedActivity;
import de.symeda.sormas.app.LocaleManager;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.SormasApplication;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.Config;
import de.symeda.sormas.app.backend.config.ConfigDao;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.user.UserRole;
import de.symeda.sormas.app.component.dialog.SynchronizationDialog;
import de.symeda.sormas.app.core.NotificationContext;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.core.notification.NotificationType;
import de.symeda.sormas.app.databinding.ActivityLoginLayoutBinding;
import de.symeda.sormas.app.rest.RetroProvider;
import de.symeda.sormas.app.rest.SynchronizeDataAsync;
import de.symeda.sormas.app.settings.SettingsActivity;
import de.symeda.sormas.app.util.AppUpdateController;
import de.symeda.sormas.app.util.NavigationHelper;
import de.symeda.sormas.app.util.SoftKeyboardHelper;
import de.symeda.sormas.app.util.SormasProperties;

public class LoginActivity extends BaseLocalizedActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NotificationContext {

	private ActivityLoginLayoutBinding binding;

	private SynchronizationDialog synchronizationDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);

		if (!ConfigProvider.ensureDeviceEncryption(LoginActivity.this)) {
			return;
		}

		LoginViewModel loginViewModel = new LoginViewModel();
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login_layout);
		binding.setData(loginViewModel);

		binding.loginUsername.setLiveValidationDisabled(true);
		binding.loginPassword.setLiveValidationDisabled(true);

		boolean hasDefaultUser =
			!DataHelper.isNullOrEmpty(SormasProperties.getUserNameDefault()) && !DataHelper.isNullOrEmpty(SormasProperties.getUserPasswordDefault());
		binding.btnLoginDefaultUser.setVisibility(hasDefaultUser ? View.VISIBLE : View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		checkLoginAndDoUpdateAndInitialSync();

		if (ConfigProvider.getUser() != null) {
			binding.signInLayout.setVisibility(View.GONE);
			binding.autoLoginProgressLayout.setVisibility(View.GONE);
		} else {
			binding.signInLayout.setVisibility(View.VISIBLE);
			binding.autoLoginProgressLayout.setVisibility(View.GONE);
		}

		// Check if auto login should be triggered after restore
		if (ConfigProvider.isAutoLoginFlag()) {
			Log.d("LoginActivity", "Auto login flag detected - triggering automatic login");
			// Show progress indicator and hide sign-in form
			binding.autoLoginProgressLayout.setVisibility(View.VISIBLE);
			binding.signInLayout.setVisibility(View.GONE);
			binding.btnAutoLogin.setVisibility(View.GONE);
			// Trigger auto login automatically
			populateAutoLoginCredentials(null);
			// Clear the flag after triggering
			ConfigProvider.setAutoLoginFlag(false);
		} else {
			// Show auto login button only if auto login credentials are available
			boolean hasAutoLoginCredentials = checkAutoLoginCredentialsAvailable();
			binding.btnAutoLogin.setVisibility(hasAutoLoginCredentials ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		SoftKeyboardHelper.hideKeyboard(this, binding.loginPassword.getWindowToken());
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		checkLoginAndDoUpdateAndInitialSync();
	}

	/**
	 * Handles the result of the attempt to install a new app version.
	 * Has to be added to every activity that uses the UpdateAppDialog
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == AppUpdateController.INSTALL_RESULT) {
			switch (resultCode) {
			// Do nothing if the installation was successful
			case Activity.RESULT_OK:
			case Activity.RESULT_CANCELED:
			case Activity.RESULT_FIRST_USER:
				finishAndRemoveTask();
				break;
			// Everything else probably is an error
			default:
				AppUpdateController.getInstance().handleInstallFailure();
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (synchronizationDialog != null && synchronizationDialog.isShowing()) {
			synchronizationDialog.dismiss();
		}

		super.onDestroy();
	}

	public void showSettingsView(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void loginDefaultUser(View view) {

		binding.loginUsername.setValue(SormasProperties.getUserNameDefault());
		binding.loginPassword.setValue(SormasProperties.getUserPasswordDefault());

		login(view);
	}

	/**
	 * Called by onClick
	 */
	public void login(View view) {
		//Hide notification
		//NotificationHelper.hideNotification(binding);
		binding.loginUsername.disableErrorState();
		binding.loginPassword.disableErrorState();
		
		// Hide auto-login progress indicator when user manually logs in
		binding.autoLoginProgressLayout.setVisibility(View.GONE);

		String userName = binding.loginUsername.getValue().trim();
		String password = binding.loginPassword.getValue();

		if (userName.isEmpty()) {
			binding.loginUsername.enableErrorState(R.string.message_empty_username);
		} else if (password.isEmpty()) {
			binding.loginPassword.enableErrorState(R.string.message_empty_password);
		} else {
			ConfigProvider.setUsernameAndPassword(userName, password);

			RetroProvider.connectAsyncHandled(this, true, true, result -> {
				if (Boolean.TRUE.equals(result)) {
					RetroProvider.disconnect();
					checkLoginAndDoUpdateAndInitialSync();
				} else {
					// if we could not connect to the server, the user can't sign in - no matter the reason
					ConfigProvider.clearUserLogin();
				}
			});
		}
	}

	private void checkLoginAndDoUpdateAndInitialSync() {

		if (DataHelper.isNullOrEmpty(ConfigProvider.getServerRestUrl())) {
			NavigationHelper.goToSettings(this);
			return;
		}

		if (ConfigProvider.getPassword() == null)
			return;

		if (synchronizationDialog == null || !synchronizationDialog.isShowing()) {
			synchronizationDialog = new SynchronizationDialog(this);
			synchronizationDialog.create();
		}

		RetroProvider.connectAsyncHandled(this, true, true, result -> {
			if (Boolean.TRUE.equals(result)) {

				boolean needsSync = ConfigProvider.getUser() == null || DatabaseHelper.getCaseDao().isEmpty();

				if (needsSync) {
					SynchronizeDataAsync.call(
						SynchronizeDataAsync.SyncMode.Changes,
						getApplicationContext(),
						synchronizationDialog.getSyncCallbacks(),
						(syncFailed, syncFailedMessage) -> {

							RetroProvider.disconnect();

							if (syncFailed) {
								NotificationHelper.showNotification(LoginActivity.this, NotificationType.ERROR, syncFailedMessage);
							}

							if (synchronizationDialog != null && synchronizationDialog.isShowing()) {
								synchronizationDialog.dismiss();
								synchronizationDialog = null;
							}

							if (ConfigProvider.getUser() != null) {
								// Clear user rights cache to ensure fresh permissions are loaded after sync
								ConfigProvider.clearUserCache();
								initializeFirebase();
								if (ConfigProvider.getUser().getLanguage() != null) {
									setNewLocale(this, ConfigProvider.getUser().getLanguage());
								}
								openLandingActivity();
							} else {
								binding.autoLoginProgressLayout.setVisibility(View.GONE);
								binding.signInLayout.setVisibility(View.VISIBLE);
							}
						});
				} else {

					RetroProvider.disconnect();

					if (synchronizationDialog != null && synchronizationDialog.isShowing()) {
						synchronizationDialog.dismiss();
						synchronizationDialog = null;
					}

					// Clear user rights cache to ensure fresh permissions are loaded
					ConfigProvider.clearUserCache();
					initializeFirebase();
					if (ConfigProvider.getUser().getLanguage() != null) {
						setNewLocale(this, ConfigProvider.getUser().getLanguage());
					}
					openLandingActivity();
				}
			} else {
				if (synchronizationDialog != null && synchronizationDialog.isShowing()) {
					synchronizationDialog.dismiss();
					synchronizationDialog = null;
				}

				if (ConfigProvider.getUser() != null) {
					// Clear user rights cache to ensure fresh permissions are loaded
					ConfigProvider.clearUserCache();
					initializeFirebase();
					if (ConfigProvider.getUser().getLanguage() != null) {
						setNewLocale(this, ConfigProvider.getUser().getLanguage());
					}
					openLandingActivity();
				} else {
					binding.autoLoginProgressLayout.setVisibility(View.GONE);
					binding.signInLayout.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initializeFirebase() {
		((SormasApplication) getApplication()).getFirebaseAnalytics().setUserId(ConfigProvider.getUser().getUuid());
		FirebaseCrashlytics.getInstance().setUserId(ConfigProvider.getUser().getUuid());
	}

	private void openLandingActivity() {

		User user = ConfigProvider.getUser();

		// Set variables that were cleared in clearUserLogin() and set PIN to 1234 after successful login
//		Log.d("LoginActivity", "Setting variables after successful login");
//		ConfigProvider.setAccessGranted(true);
//		ConfigProvider.setLastNotificationDate(new java.util.Date());
//		ConfigProvider.setLastObsoleteUuidsSyncDate(new java.util.Date());
//
//		// Check if this is an auto login session and set PIN automatically
//		if (ConfigProvider.isAutoLoginFlag()) {
//			Log.d("LoginActivity", "Auto login detected - setting PIN to 1234");
//			ConfigProvider.setPin("1234");
//			ConfigProvider.setAutoLoginFlag(false); // Clear the flag
//			Log.d("LoginActivity", "PIN set to 1234 after auto login");
//		} else {
//			Log.d("LoginActivity", "Regular login - PIN not set automatically");
//		}
		
		// Show success message
//		android.widget.Toast.makeText(this, "Login successful! Variables restored and PIN set to 1234", android.widget.Toast.LENGTH_LONG).show();

		boolean caseSuveillance = !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.CASE_SURVEILANCE);
		boolean campaigns = !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.CAMPAIGNS);

		if (caseSuveillance) {
			if (ConfigProvider.hasUserRight(UserRight.CASE_VIEW)
				&& (ConfigProvider.hasUserRight(UserRight.CASE_RESPONSIBLE)
					|| user.hasJurisdictionLevel(JurisdictionLevel.HEALTH_FACILITY, JurisdictionLevel.COMMUNITY, JurisdictionLevel.POINT_OF_ENTRY))) {
				NavigationHelper.goToCases(LoginActivity.this);
			} else if (ConfigProvider.hasUserRight(UserRight.CONTACT_VIEW) && ConfigProvider.hasUserRight(UserRight.CONTACT_RESPONSIBLE)) {
				NavigationHelper.goToContacts(LoginActivity.this);
			} else if (ConfigProvider.hasUserRight(UserRight.CASE_VIEW)) {
				NavigationHelper.goToCases(LoginActivity.this);
			} else {
				NavigationHelper.goToSettings(LoginActivity.this);
			}
		} else if (campaigns && ConfigProvider.hasUserRight(UserRight.CAMPAIGN_FORM_DATA_VIEW)) {
			NavigationHelper.goToCampaigns(LoginActivity.this);
		} else {
			NavigationHelper.goToSettings(LoginActivity.this);
		}
	}

	@Override
	public View getRootView() {
		if (binding != null)
			return binding.getRoot();

		return null;
	}

	private void setNewLocale(AppCompatActivity mContext, Language language) {
		LocaleManager.setNewLocale(this, language);
		I18nProperties.setUserLanguage(ConfigProvider.getUser().getLanguage());
		Intent intent = mContext.getIntent();
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	/**
	 * Populates the login fields with auto-login credentials if they are available.
	 */
	public void populateAutoLoginCredentials(View view) {
		ConfigDao configDao = DatabaseHelper.getConfigDao();
		if (configDao == null) {
			Log.e("AutoLogin", "ConfigDao is null");
			return;
		}
		Log.d("AutoLogin", "ConfigDao obtained successfully");

		// First try to get auto-login credentials (plain text)
		Config autoLoginUsernameConfig = configDao.queryForId("autologin_username");
		Config autoLoginPasswordConfig = configDao.queryForId("autologin_password");

		String autoUsername = "";
		String autoPassword = "";
		if (autoLoginUsernameConfig != null && autoLoginPasswordConfig != null) {
			autoUsername = autoLoginUsernameConfig.getValue();
			autoPassword = autoLoginPasswordConfig.getValue();
		}

		if (autoUsername != null && autoPassword != null) {
			Log.d("LoginActivity", "Auto-login credentials found - populating fields");
			
			// Set the username and password in the binding fields
			binding.loginUsername.setValue(autoUsername);
			binding.loginPassword.setValue(autoPassword);

			// Check if user has roles before proceeding with login
			checkUserRolesAndLogin(view, autoUsername, autoPassword);
		} else {
			Log.d("LoginActivity", "No auto-login credentials found");
		}
	}

	/**
	 * Checks if auto login credentials are available in the database.
	 * 
	 * @return true if auto login credentials are available, false otherwise
	 */
	private boolean checkAutoLoginCredentialsAvailable() {
		try {
			ConfigDao configDao = DatabaseHelper.getConfigDao();
			if (configDao == null) {
				return false;
			}
			
			Config autoLoginUsernameConfig = configDao.queryForId("autologin_username");
			Config autoLoginPasswordConfig = configDao.queryForId("autologin_password");
			
			return autoLoginUsernameConfig != null && autoLoginUsernameConfig.getValue() != null && 
				   !autoLoginUsernameConfig.getValue().isEmpty() &&
				   autoLoginPasswordConfig != null && autoLoginPasswordConfig.getValue() != null &&
				   !autoLoginPasswordConfig.getValue().isEmpty();
		} catch (Exception e) {
			Log.e("LoginActivity", "Error checking auto login credentials availability", e);
			return false;
		}
	}

	/**
	 * Checks if the user has roles and requests them from backend if missing, then proceeds with login.
	 */
	private void checkUserRolesAndLogin(View view, String username, String password) {
		// First, try to get the user from local database
		User localUser = DatabaseHelper.getUserDao().getByUsername(username);
		
		if (localUser != null && localUser.getUserRoles() != null && !localUser.getUserRoles().isEmpty()) {
			// User has roles locally, proceed with login
			Log.d("LoginActivity", "User has roles locally, proceeding with login");
			login(view);
		} else {
			// User doesn't have roles locally, need to request from backend
			Log.d("LoginActivity", "User missing roles locally, requesting from backend");
			requestUserRolesFromBackend(view, username, password);
		}
	}

	/**
	 * Requests user roles from backend and then proceeds with login.
	 */
	private void requestUserRolesFromBackend(View view, String username, String password) {
		// Set credentials temporarily to make the API call
		ConfigProvider.setUsernameAndPassword(username, password);
		
		RetroProvider.connectAsyncHandled(this, true, true, result -> {
			if (Boolean.TRUE.equals(result)) {
				try {
					// Get current user to get their UUID
					User currentUser = ConfigProvider.getUser();
					if (currentUser != null) {
						// Request user roles from backend
						RetroProvider.getUserFacade().getUserRoles(currentUser.getUuid()).enqueue(new retrofit2.Callback<Set<UserRoleDto>>() {
							@Override
							public void onResponse(retrofit2.Call<Set<UserRoleDto>> call, retrofit2.Response<Set<UserRoleDto>> response) {
								if (response.isSuccessful() && response.body() != null) {
									Log.d("LoginActivity", "Successfully retrieved user roles from backend");
									// Update local user with roles
									updateLocalUserWithRoles(currentUser, response.body());
									// Proceed with login
									login(view);
								} else {
									Log.e("LoginActivity", "Failed to retrieve user roles from backend");
									// Still proceed with login even if roles request failed
									login(view);
								}
							}

							@Override
							public void onFailure(retrofit2.Call<Set<UserRoleDto>> call, Throwable t) {
								Log.e("LoginActivity", "Error requesting user roles from backend", t);
								// Still proceed with login even if roles request failed
								login(view);
							}
						});
					} else {
						Log.e("LoginActivity", "Current user is null, proceeding with login");
						login(view);
					}
				} catch (Exception e) {
					Log.e("LoginActivity", "Exception while requesting user roles", e);
					// Still proceed with login even if roles request failed
					login(view);
				} finally {
					RetroProvider.disconnect();
				}
			} else {
				Log.e("LoginActivity", "Failed to connect to backend for user roles request");
				// Clear credentials and show login form
				ConfigProvider.clearUserLogin();
				binding.autoLoginProgressLayout.setVisibility(View.GONE);
				binding.signInLayout.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * Updates the local user with roles received from backend.
	 */
	private void updateLocalUserWithRoles(User localUser, Set<UserRoleDto> backendRoles) {
		try {
			// Convert UserRoleDto to local UserRole entities
			Set<UserRole> localRoles = new java.util.HashSet<>();
			for (UserRoleDto roleDto : backendRoles) {
				// Try to find existing local role by UUID first
				UserRole localRole = DatabaseHelper.getUserRoleDao().queryUuid(roleDto.getUuid());
				
				if (localRole == null) {
					// If role doesn't exist locally, create a new one
					localRole = new UserRole();
					localRole.setUuid(roleDto.getUuid());
					localRole.setCaption(roleDto.getCaption());
					localRole.setDescription(roleDto.getDescription());
					localRole.setEnabled(roleDto.isEnabled());
					localRole.setHasOptionalHealthFacility(roleDto.getHasOptionalHealthFacility());
					localRole.setHasAssociatedDistrictUser(roleDto.getHasAssociatedDistrictUser());
					localRole.setPortHealthUser(roleDto.isPortHealthUser());
					localRole.setJurisdictionLevel(roleDto.getJurisdictionLevel());
					localRole.setLinkedDefaultUserRole(roleDto.getLinkedDefaultUserRole());
					
					// Set user rights from the role DTO
					if (roleDto.getUserRights() != null) {
						localRole.setUserRights(roleDto.getUserRights());
					}
					
					// Save the new role to local database
					DatabaseHelper.getUserRoleDao().create(localRole);
					Log.d("LoginActivity", "Created new local role: " + roleDto.getCaption());
				}
				
				localRoles.add(localRole);
			}
			
			// Update the local user with roles
			localUser.setUserRoles(localRoles);
			DatabaseHelper.getUserDao().saveAndSnapshot(localUser);
			
			Log.d("LoginActivity", "Updated local user with " + localRoles.size() + " roles");
		} catch (Exception e) {
			Log.e("LoginActivity", "Error updating local user with roles", e);
		}
	}
}
