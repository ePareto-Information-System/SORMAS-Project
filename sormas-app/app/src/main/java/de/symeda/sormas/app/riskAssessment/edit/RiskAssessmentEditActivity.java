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

import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Menu;

import androidx.core.app.ActivityCompat;

import java.util.List;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.component.dialog.InfoDialog;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.EbsSection;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.edit.EbsEditFragment;
import de.symeda.sormas.app.ebs.edit.RiskAssessmentEditFragment;
import de.symeda.sormas.app.ebs.edit.SignalVerificationEditFragment;
import de.symeda.sormas.app.ebs.edit.TriagingEditFragment;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;

public class RiskAssessmentEditActivity extends BaseEditActivity<RiskAssessment> {

	public static final String TAG = RiskAssessmentEditActivity.class.getSimpleName();

	private AsyncTask saveTask;

	public static void startActivity(Context context, String rootUuid, EbsSection section) {
		BaseEditActivity.startActivity(context, RiskAssessmentEditActivity.class, buildBundle(rootUuid,section));
	}

	public static void startActivity(Context fromActivity) {
		BaseEditActivity.startActivity(fromActivity, RiskAssessmentEditActivity.class, buildBundle(null));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public SignalOutcome getPageStatus() {
		return getStoredRootEntity() == null ? SignalOutcome.NON_EVENT : EbsEditActivity.getParentEbs().getSignalVerification().getVerified();
	}


	@Override
	protected RiskAssessment queryRootEntity(String recordUuid) {
		return DatabaseHelper.getRiskAssessmentDao().queryUuid(recordUuid);
	}

	@Override
	protected RiskAssessment buildRootEntity() {throw new UnsupportedOperationException();}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_risk_assessment);
		return result;
	}

	@Override
	public List<PageMenuItem> getPageMenuData() {
		RiskAssessment riskAssessment = getStoredRootEntity();
		List<PageMenuItem> menuItems = PageMenuItem.fromEnum(EbsSection.values(), getContext());
		if (riskAssessment != null) {
			showRiskDialog(riskAssessment.getRiskAssessment().toString()).show();
			menuItems.set(EbsSection.RISK_ASSESSMENT_EDIT.ordinal(), null);
			menuItems.set(EbsSection.RISK_ASSESSMENT.ordinal(), null);
			menuItems.set(EbsSection.EBS_ALERT_EDIT.ordinal(), null);
		}
		return menuItems;

	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, RiskAssessment activityRootData) {
		BaseEditFragment fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
		if (EbsEditActivity.getParentEbs().getSignalVerification().getVerified() != SignalOutcome.EVENT){
			EbsListActivity.startActivity(getContext(),null);
			return fragment;
		}
		EbsSection section = EbsSection.fromOrdinal(menuItem.getPosition());

		switch (section) {
			case SIGNAL_INFORMATION:
				fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
				break;
			case TRIAGING:
				fragment = TriagingEditFragment.newInstance(EbsEditActivity.getParentEbs().getTriaging());
				break;
			case SIGNAL_VERIFICATION:
				fragment = SignalVerificationEditFragment.newInstance(EbsEditActivity.getParentEbs().getSignalVerification());
			case RISK_ASSESSMENT:
				RiskAssessmentListActivity.startActivity(getContext(), null);
				break;
			case EBS_ALERT:
				EbsAlertListActivity.startActivity(getContext(), null);
				break;
			default:
				fragment = RiskAssessmentEditFragment.newInstance(activityRootData);
		}
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_risk_assessment_edit;
	}


	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final RiskAssessment riskToSave = getStoredRootEntity();

		saveTask = new SavingAsyncTask(getRootView(), riskToSave) {
			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException, ValidationException {
				riskToSave.setEbs(EbsEditActivity.getParentEbs());
				DatabaseHelper.getRiskAssessmentDao().saveAndSnapshot(riskToSave);
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				super.onPostExecute(taskResult);

				if (taskResult.getResultStatus().isSuccess()) {
					InfoDialog riskInfo =  showRiskDialog(riskToSave.getRiskAssessment().toString());
					riskInfo.setPositiveButton(R.string.action_ok, listener);
					riskInfo.show();
				} else {
					onResume(); // reload data
				}
				saveTask = null;
			}
		}.executeOnThreadPool();
	}

	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int id) {
			finish();
			EbsAlertListActivity.startActivity(getContext(), null);
		}
	};
	private InfoDialog showRiskDialog(String riskassessment){
		InfoDialog riskAssessmentInfoDialog = null;
		switch (riskassessment){
			case "Very High":
				riskAssessmentInfoDialog =
						new InfoDialog(getContext(), R.layout.dialog_risk_very_high_assessment_layout, null);
				break;
			case "High":
				riskAssessmentInfoDialog =
						new InfoDialog(getContext(), R.layout.dialog_risk_high_assessment_layout, null);
				break;
			case "Moderate":
				riskAssessmentInfoDialog =
						new InfoDialog(getContext(), R.layout.dialog_risk_moderate_assessment_layout, null);
				break;
			case "Low":
				riskAssessmentInfoDialog =
						new InfoDialog(getContext(), R.layout.dialog_risk_low_assessment_layout, null);
				break;
			default:
				riskAssessmentInfoDialog =
						new InfoDialog(getContext(), R.layout.dialog_risk_low_assessment_layout, null);
		}
		return riskAssessmentInfoDialog;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (saveTask != null && !saveTask.isCancelled())
			saveTask.cancel(true);
	}

}

