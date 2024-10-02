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

import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;

import java.util.List;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.EbsSection;
import de.symeda.sormas.app.ebs.edit.EbsAlertEditFragment;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.edit.EbsEditFragment;
import de.symeda.sormas.app.ebs.edit.SignalVerificationEditFragment;
import de.symeda.sormas.app.ebs.edit.TriagingEditFragment;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;

public class EbsAlertEditActivity extends BaseEditActivity<EbsAlert> {

	public static final String TAG = EbsAlertEditActivity.class.getSimpleName();

	private AsyncTask saveTask;

	public static void startActivity(Context context, String rootUuid, EbsSection section) {
		BaseEditActivity.startActivity(context, EbsAlertEditActivity.class, buildBundle(rootUuid,section));
	}

	public static void startActivity(Context fromActivity) {
		BaseEditActivity.startActivity(fromActivity, EbsAlertEditActivity.class, buildBundle(null));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public SignalOutcome getPageStatus() {
		return getStoredRootEntity() == null ? SignalOutcome.NON_EVENT : EbsEditActivity.getParentEbs().getSignalVerification().getVerified();
	}

	@Override
	protected EbsAlert queryRootEntity(String recordUuid) {
		return DatabaseHelper.getEbsAlertDao().queryUuid(recordUuid);
	}

	@Override
	protected EbsAlert buildRootEntity() {throw new UnsupportedOperationException();}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_alert);
		return result;
	}

	@Override
	public List<PageMenuItem> getPageMenuData() {
		EbsAlert ebsAlert = getStoredRootEntity();
		List<PageMenuItem> menuItems = PageMenuItem.fromEnum(EbsSection.values(), getContext());
		if (ebsAlert != null) {
			menuItems.set(EbsSection.EBS_ALERT.ordinal(), null);
			menuItems.set(EbsSection.EBS_ALERT_EDIT.ordinal(), null);
			menuItems.set(EbsSection.RISK_ASSESSMENT_EDIT.ordinal(), null);
		}
		return menuItems;

	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, EbsAlert activityRootData) {
		EbsSection section = EbsSection.fromOrdinal(menuItem.getPosition());
		BaseEditFragment fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
		if (EbsEditActivity.getParentEbs().getSignalVerification().getVerified() != SignalOutcome.EVENT){
			EbsListActivity.startActivity(getContext(),null);
			return fragment;
		}
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
				fragment = EbsAlertEditFragment.newInstance(activityRootData);
		}
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_alert_assessment_edit;
	}


	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final EbsAlert ebsAlertToSave = getStoredRootEntity();

		saveTask = new SavingAsyncTask(getRootView(), ebsAlertToSave) {

			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException, ValidationException {
				ebsAlertToSave.setEbs(EbsEditActivity.getParentEbs());
				DatabaseHelper.getEbsAlertDao().saveAndSnapshot(ebsAlertToSave);
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				super.onPostExecute(taskResult);

				if (taskResult.getResultStatus().isSuccess()) {
					finish();
					EbsEditActivity.startActivity(getContext(), ebsAlertToSave.getUuid(), EbsSection.SIGNAL_INFORMATION);
				} else {
					onResume(); // reload data
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

