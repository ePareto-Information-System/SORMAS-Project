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

import android.content.res.Resources;

import java.util.List;


import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.databinding.FragmentSignalVerificationEditLayoutBinding;


public class SignalVerificationEditFragment extends BaseEditFragment<FragmentSignalVerificationEditLayoutBinding, SignalVerification, Ebs> {

	public static final String TAG = SignalVerificationEditFragment.class.getSimpleName();

	private SignalVerification record;
	// Static methods

	public static SignalVerificationEditFragment newInstance(Ebs activityRootData) {
		return newInstanceWithFieldCheckers(
			SignalVerificationEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}
	public static SignalVerificationEditFragment newInstance(SignalVerification activityRootData) {
		return newInstanceWithFieldCheckers(
			SignalVerificationEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_signal_verification);
	}

	@Override
	public SignalVerification getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		Ebs ebs = getActivityRootData();
		record = ebs.getSignalVerification();
	}

	@Override
	public void onLayoutBinding(final FragmentSignalVerificationEditLayoutBinding contentBinding) {
		contentBinding.setData(record);
		contentBinding.setSignalOutcomeClass(SignalOutcome.class);
		contentBinding.setYesNoClass(YesNo.class);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentSignalVerificationEditLayoutBinding contentBinding) {
		// Initialize ControlDateFields
		contentBinding.signalVerificationVerificationCompleteDate.initializeDateField(getFragmentManager());
		contentBinding.signalVerificationDateOfOccurrence.initializeDateField(getFragmentManager());
		setFieldVisibilitiesAndAccesses(SignalVerificationDto.class, contentBinding.mainContent);
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_signal_verification_edit_layout;
	}
}
