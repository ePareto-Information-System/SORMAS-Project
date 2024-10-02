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

package de.symeda.sormas.app.riskAssessment.edit;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;

import java.util.List;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.EbsSection;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.edit.EbsEditFragment;
import de.symeda.sormas.app.ebs.edit.RiskAssessmentEditFragment;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;

public class RiskAssessmentNewActivity extends BaseEditActivity<RiskAssessment> {

	public static final String TAG = RiskAssessmentNewActivity.class.getSimpleName();

	private AsyncTask saveTask;

	private String riskUuid = null;

	public static void startActivity(Context fromActivity) {
		BaseEditActivity.startActivity(fromActivity, RiskAssessmentNewActivity.class, buildBundle(null));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public RiskAssesment getPageStatus() {
		return null;
	}

	@Override
	protected RiskAssessment queryRootEntity(String recordUuid) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected RiskAssessment buildRootEntity() {
		RiskAssessment risk = DatabaseHelper.getRiskAssessmentDao().build();
		risk.setEbs(EbsEditActivity.getParentEbs());
		return risk;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_risk_assessment);
		return result;
	}


	@Override
	protected void onCreateInner(Bundle savedInstanceState) {
		super.onCreateInner(savedInstanceState);
		Bundler bundler = new Bundler(savedInstanceState);
		riskUuid = bundler.getRiskUuid();
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundler bundler = new Bundler(outState);
		bundler.setRiskUuid(riskUuid);
	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, RiskAssessment activityRootData) {
		BaseEditFragment fragment = RiskAssessmentEditFragment.newInstance(activityRootData);
		fragment.setLiveValidationDisabled(true);
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_risk_assessment_new;
	}

	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final RiskAssessment riskToSave = (RiskAssessment) getActiveFragment().getPrimaryData();
		RiskAssessmentEditFragment fragment = (RiskAssessmentEditFragment) getActiveFragment();

		fragment.setLiveValidationDisabled(false);

		try {
			FragmentValidator.validate(getContext(), fragment.getContentBinding());
		} catch (ValidationException e) {
			NotificationHelper.showNotification(this, ERROR, e.getMessage());
			return;
		}
		saveDataInner(riskToSave);
	}

	private void saveDataInner(final RiskAssessment riskToSave) {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		saveTask = new SavingAsyncTask(getRootView(), riskToSave) {

			@Override
			protected void onPreExecute() {
				showPreloader();
			}

			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException {
				riskToSave.setEbs(EbsEditActivity.getParentEbs());
				DatabaseHelper.getRiskAssessmentDao().saveAndSnapshot(riskToSave);
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				hidePreloader();
				super.onPostExecute(taskResult);
				if (taskResult.getResultStatus().isSuccess()) {
					finish();
					RiskAssessmentListActivity.startActivity(getContext(), null);
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

