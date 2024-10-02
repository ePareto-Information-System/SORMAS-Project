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

package de.symeda.sormas.app.ebsAlert.edit;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.edit.EbsAlertEditFragment;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;

public class EbsAlertNewActivity extends BaseEditActivity<EbsAlert> {

	public static final String TAG = EbsAlertNewActivity.class.getSimpleName();

	private AsyncTask saveTask;

	private String ebsAlertUuid = null;

	public static void startActivity(Context fromActivity) {
		BaseEditActivity.startActivity(fromActivity, EbsAlertNewActivity.class, buildBundle(null));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public ResponseStatus getPageStatus() {
		return null;
	}

	@Override
	protected EbsAlert queryRootEntity(String recordUuid) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected EbsAlert buildRootEntity() {
		EbsAlert alert = DatabaseHelper.getEbsAlertDao().build();
		alert.setEbs(EbsEditActivity.getParentEbs());
		return alert;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_alert);
		return result;
	}


	@Override
	protected void onCreateInner(Bundle savedInstanceState) {
		super.onCreateInner(savedInstanceState);
		Bundler bundler = new Bundler(savedInstanceState);
		ebsAlertUuid = bundler.getAlertUuid();
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundler bundler = new Bundler(outState);
		bundler.setAlertUuid(ebsAlertUuid);
	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, EbsAlert activityRootData) {
		BaseEditFragment fragment = EbsAlertEditFragment.newInstance(activityRootData);
		fragment.setLiveValidationDisabled(true);
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_alert_assessment_new;
	}

	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final EbsAlert ebsAlertToSave = (EbsAlert) getActiveFragment().getPrimaryData();
		EbsAlertEditFragment fragment = (EbsAlertEditFragment) getActiveFragment();

		fragment.setLiveValidationDisabled(false);

		try {
			FragmentValidator.validate(getContext(), fragment.getContentBinding());
		} catch (ValidationException e) {
			NotificationHelper.showNotification(this, ERROR, e.getMessage());
			return;
		}
		saveDataInner(ebsAlertToSave);
	}

	private void saveDataInner(final EbsAlert ebsAlertToSave) {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		saveTask = new SavingAsyncTask(getRootView(), ebsAlertToSave) {

			@Override
			protected void onPreExecute() {
				showPreloader();
			}

			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException {
				ebsAlertToSave.setEbs(EbsEditActivity.getParentEbs());
				DatabaseHelper.getEbsAlertDao().saveAndSnapshot(ebsAlertToSave);
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				hidePreloader();
				super.onPostExecute(taskResult);
				if (taskResult.getResultStatus().isSuccess()) {
					finish();
					EbsAlertListActivity.startActivity(getContext(), null);
				}
				saveTask = null;
			}
		}.executeOnThreadPool();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (saveTask != null && !saveTask.isCancelled())
			saveTask.cancel(true);
	}

}

