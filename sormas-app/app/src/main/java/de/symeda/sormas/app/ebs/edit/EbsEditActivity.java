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

package de.symeda.sormas.app.ebs.edit;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.core.notification.NotificationType.WARNING;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;


import java.util.List;

import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.app.BaseEditActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.async.AsyncTaskResult;
import de.symeda.sormas.app.core.async.SavingAsyncTask;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.ebs.EbsSection;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.ebsAlert.list.EbsAlertListActivity;
import de.symeda.sormas.app.riskAssessment.list.RiskAssessmentListActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Bundler;
import de.symeda.sormas.app.util.Consumer;

public class EbsEditActivity extends BaseEditActivity<Ebs> {

	public static final String TAG = EbsEditActivity.class.getSimpleName();

	private AsyncTask saveTask;

	private static Ebs parent;

	public static void startActivity(Context context, String rootUuid, EbsSection section) {
		BaseEditActivity.startActivity(context, EbsEditActivity.class, buildBundle(rootUuid,section));
	}

	public static Bundler buildBundle(String rootUuid) {
		return BaseEditActivity.buildBundle(rootUuid);
	}

	public static Ebs getParentEbs(){
		return parent;
	}

	public static void setParent(Ebs parent) {
		EbsEditActivity.parent = parent;
	}

	@Override
	public SignalOutcome getPageStatus() {
		if (getStoredRootEntity() == null){
			return null;
		}else if (getStoredRootEntity().getSignalVerification() == null) {
			return SignalOutcome.NON_EVENT;
		} else if (getStoredRootEntity().getSignalVerification().getVerified() == null) {
			return SignalOutcome.NON_EVENT;
		}
		return getStoredRootEntity() == null ? null : getStoredRootEntity().getSignalVerification().getVerified();
	}

	@Override
	protected Ebs queryRootEntity(String recordUuid) {
		return DatabaseHelper.getEbsDao().queryUuid(recordUuid);
	}

	@Override
	protected Ebs buildRootEntity() {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getSaveMenu().setTitle(R.string.action_save_ebs);
		return result;
	}

	@Override
	public List<PageMenuItem> getPageMenuData() {
		List<PageMenuItem> menuItems = PageMenuItem.fromEnum(EbsSection.values(), getContext());
		menuItems.set(EbsSection.EBS_ALERT_EDIT.ordinal(), null);
		menuItems.set(EbsSection.RISK_ASSESSMENT_EDIT.ordinal(), null);
		return menuItems;

	}

	@Override
	protected BaseEditFragment buildEditFragment(PageMenuItem menuItem, Ebs activityRootData) {
		EbsSection section = EbsSection.fromOrdinal(menuItem.getPosition());
		final Ebs changedCase = getStoredRootEntity();
		setParent(changedCase);
		BaseEditFragment fragment = EbsEditFragment.newInstance(activityRootData);
		switch (section) {

			case SIGNAL_INFORMATION:
				fragment = EbsEditFragment.newInstance(activityRootData);
				break;
			case TRIAGING:
				fragment = TriagingEditFragment.newInstance(activityRootData.getTriaging());
				break;
			case SIGNAL_VERIFICATION:
				if (changedCase.getTriaging().getTriagingDecision() != EbsTriagingDecision.VERIFY){
					EbsListActivity.startActivity(getContext(),null);
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(R.string.indicator_warning)
							.setMessage(R.string.triaging_decision_not_verify)
							.show();
					return fragment;
				}
				fragment = SignalVerificationEditFragment.newInstance(activityRootData.getSignalVerification());
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
		return R.string.heading_event_edit;
	}

	@Override
	public void saveData() {
		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		saveData(parameter->goToNextPage());
	}
	public void saveData(final Consumer<Ebs> successCallback) {
		if (saveTask != null) {
			NotificationHelper.showNotification(this, WARNING, getString(R.string.message_already_saving));
			return; // don't save multiple times
		}

		final Ebs changedCase = getStoredRootEntity();

		try {
			FragmentValidator.validate(getContext(), getActiveFragment().getContentBinding());
		} catch (ValidationException e) {
			NotificationHelper.showNotification(this, ERROR, e.getMessage());
			return;
		}

		saveTask = new SavingAsyncTask(getRootView(), changedCase) {

			@Override
			protected void onPreExecute() {
				showPreloader();
			}

			@Override
			protected void doInBackground(TaskResultHolder resultHolder) throws DaoException {
				synchronized (EbsEditActivity.this) {
					DatabaseHelper.getTriagingDao().saveAndSnapshot(changedCase.getTriaging());
					DatabaseHelper.getSignalVerificationDao().saveAndSnapshot(changedCase.getSignalVerification());
					DatabaseHelper.getEbsDao().saveAndSnapshot(changedCase);
				}
			}

			@Override
			protected void onPostExecute(AsyncTaskResult<TaskResultHolder> taskResult) {
				hidePreloader();
				super.onPostExecute(taskResult);
				if (taskResult.getResultStatus().isSuccess()) {
					setParent(changedCase);
					TriagingEditActivity.startActivity(getContext(),changedCase.getTriaging().getUuid(),EbsSection.TRIAGING);
				} else {
					onResume();
					// reload data
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

