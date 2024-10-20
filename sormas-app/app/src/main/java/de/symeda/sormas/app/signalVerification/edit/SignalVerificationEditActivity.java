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

package de.symeda.sormas.app.signalVerification.edit;

import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;

import java.util.List;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.EbsSection;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.edit.EbsEditFragment;
import de.symeda.sormas.app.ebs.edit.SignalVerificationEditFragment;
import de.symeda.sormas.app.ebs.edit.TriagingEditFragment;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;

public class SignalVerificationEditActivity extends BaseEditActivity<SignalVerification> {

	public static final String TAG = SignalVerificationEditActivity.class.getSimpleName();

	private AsyncTask saveTask;

	public static void startActivity(Context context, String rootUuid,EbsSection section) {
		BaseEditActivity.startActivity(context, SignalVerificationEditActivity.class, buildBundle(rootUuid,section));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public SignalOutcome getPageStatus() {
		if (getStoredRootEntity() == null){
			return null;
		}else if (getStoredRootEntity().getVerified() == null) {
			return SignalOutcome.NON_EVENT;
		}
		return getStoredRootEntity() == null ? SignalOutcome.NON_EVENT : EbsEditActivity.getParentEbs().getSignalVerification().getVerified();
	}

	@Override
	protected SignalVerification queryRootEntity(String recordUuid) {
		return DatabaseHelper.getSignalVerificationDao().queryUuid(recordUuid);
	}

	@Override
	protected SignalVerification buildRootEntity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_signal_verification);
		return result;
	}

	@Override
	public List<PageMenuItem> getPageMenuData() {
		List<PageMenuItem> menuItems = PageMenuItem.fromEnum(EbsSection.values(), getContext());
		menuItems.set(EbsSection.SIGNAL_VERIFICATION.ordinal(), null);
		menuItems.set(EbsSection.EBS_ALERT_EDIT.ordinal(), null);
		menuItems.set(EbsSection.RISK_ASSESSMENT_EDIT.ordinal(), null);
		return menuItems;

	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, SignalVerification activityRootData) {
		EbsSection section = EbsSection.SIGNAL_VERIFICATION;
		if (menuItem != null){
			section = EbsSection.fromOrdinal(menuItem.getPosition());
		}else {
			section = EbsSection.SIGNAL_VERIFICATION;
		}
		BaseEditFragment fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
		if (EbsEditActivity.getParentEbs().getTriaging().getTriagingDecision() != EbsTriagingDecision.VERIFY){
			EbsListActivity.showWarningAlert = true;
			EbsListActivity.message = R.string.triaging_decision_not_verify;
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
				fragment = SignalVerificationEditFragment.newInstance(activityRootData);
				break;
			case RISK_ASSESSMENT:
				RiskAssessmentListActivity.startActivity(getContext(), null);
				break;
			case EBS_ALERT:
				EbsAlertListActivity.startActivity(getContext(), null);
				break;
			default:
				throw new IndexOutOfBoundsException(DataHelper.toStringNullable(section));
		}
		return fragment;
	}

	@Override
	protected int getActivityTitle() {
		return R.string.heading_signal_verification_edit;
	}

	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final SignalVerification signalToSave = getStoredRootEntity();

		saveTask = new SavingAsyncTask(getRootView(), signalToSave) {

			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException, ValidationException {
				DatabaseHelper.getSignalVerificationDao().saveAndSnapshot(signalToSave);
				EbsEditActivity.getParentEbs().setSignalVerification(signalToSave);
				DatabaseHelper.getEbsDao().saveAndSnapshot(EbsEditActivity.getParentEbs());
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				super.onPostExecute(taskResult);

				if (taskResult.getResultStatus().isSuccess()) {
					finish();
					RiskAssessmentListActivity.startActivity(getContext(), null);
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
