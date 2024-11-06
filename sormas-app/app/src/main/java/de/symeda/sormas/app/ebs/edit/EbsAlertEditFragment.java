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


import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentEbsAlertEditLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;


public class EbsAlertEditFragment extends BaseEditFragment<FragmentEbsAlertEditLayoutBinding, EbsAlert, EbsAlert> {

	public static final String TAG = EbsAlertEditFragment.class.getSimpleName();

	private EbsAlert record;
	private List<Item> statuses;
	// Static methods

	public static EbsAlertEditFragment newInstance(Ebs activityRootData) {
		return newInstanceWithFieldCheckers(
			EbsAlertEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}
	public static EbsAlertEditFragment newInstance(EbsAlert activityRootData) {
		return newInstanceWithFieldCheckers(
			EbsAlertEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_ebs_alert);
	}

	@Override
	public EbsAlert getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		record = getActivityRootData();
		statuses = DataUtils.getEnumItems(ResponseStatus.class,true);
	}

	@Override
	public void onLayoutBinding(final FragmentEbsAlertEditLayoutBinding contentBinding) {
		contentBinding.setData(record);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.setResponseStatusClass(ResponseStatus.class);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentEbsAlertEditLayoutBinding contentBinding) {
		// Initialize ControlDateFields
		contentBinding.ebsAlertAlertDate.initializeDateField(getFragmentManager());
		contentBinding.ebsAlertResponseDate.initializeDateField(getFragmentManager());
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_ebs_alert_edit_layout;
	}
}
