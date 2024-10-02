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

package de.symeda.sormas.app.triaging.edit;

import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;

import java.util.List;

import de.symeda.sormas.api.ebs.EbsSourceType;
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
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
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
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.signalVerification.edit.SignalVerificationEditActivity;
import de.symeda.sormas.app.triaging.TriagingSection;
import de.symeda.sormas.app.util.Bundler;

public class TriagingEditActivity extends BaseEditActivity<Triaging> {

	public static final String TAG = TriagingEditActivity.class.getSimpleName();

	private AsyncTask saveTask;

	public static void startActivity(Context context, String rootUuid) {
		BaseEditActivity.startActivity(context, TriagingEditActivity.class, buildBundle(rootUuid));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	@Override
	public SignalOutcome getPageStatus() {
		if (getStoredRootEntity() == null){
			return null;
		}else if (EbsEditActivity.getParentEbs().getSignalVerification() == null) {
			return SignalOutcome.NON_EVENT;
		} else if (EbsEditActivity.getParentEbs().getSignalVerification().getVerified() == null) {
			return SignalOutcome.NON_EVENT;
		}
		return getStoredRootEntity() == null ? null : EbsEditActivity.getParentEbs().getSignalVerification().getVerified();
	}

	@Override
	protected Triaging queryRootEntity(String recordUuid) {
		return DatabaseHelper.getTriagingDao().queryUuid(recordUuid);
	}

	@Override
	protected Triaging buildRootEntity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_triaging);
		return result;
	}


	@Override
	public List<PageMenuItem> getPageMenuData() {
		List<PageMenuItem> menuItems = PageMenuItem.fromEnum(EbsSection.values(), getContext());
			menuItems.set(EbsSection.TRIAGING.ordinal(), null);
			menuItems.set(EbsSection.EBS_ALERT_EDIT.ordinal(), null);
			menuItems.set(EbsSection.RISK_ASSESSMENT_EDIT.ordinal(), null);
		return menuItems;

	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, Triaging activityRootData) {
		EbsSection section = EbsSection.fromOrdinal(menuItem.getPosition());
		BaseEditFragment fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
		switch (section) {

			case SIGNAL_INFORMATION:
				fragment = EbsEditFragment.newInstance(EbsEditActivity.getParentEbs());
				break;
			case TRIAGING:
				fragment = TriagingEditFragment.newInstance(activityRootData);
				break;
			case SIGNAL_VERIFICATION:
				fragment = SignalVerificationEditFragment.newInstance(EbsEditActivity.getParentEbs().getSignalVerification());
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
		return R.string.heading_triaging_edit;
	}

	@Override
	public void saveData() {

		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final Triaging triageToSave = getStoredRootEntity();

		saveTask = new SavingAsyncTask(getRootView(), triageToSave) {

			@Override
			public void doInBackground(TaskResultHolder resultHolder) throws DaoException, ValidationException {
				DatabaseHelper.getTriagingDao().saveAndSnapshot(triageToSave);
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				super.onPostExecute(taskResult);

				if (taskResult.getResultStatus().isSuccess()) {
					SignalVerificationEditActivity.startActivity(getContext(), EbsEditActivity.getParentEbs().getSignalVerification().getUuid());
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
