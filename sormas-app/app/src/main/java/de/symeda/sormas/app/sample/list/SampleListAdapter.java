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

package de.symeda.sormas.app.sample.list;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.sample.PathogenTest;
import de.symeda.sormas.app.backend.sample.Sample;
import de.symeda.sormas.app.core.adapter.databinding.BindingPagedListAdapter;
import de.symeda.sormas.app.core.adapter.databinding.BindingViewHolder;
import de.symeda.sormas.app.databinding.RowSampleListItemLayoutBinding;

public class SampleListAdapter extends BindingPagedListAdapter<Sample, RowSampleListItemLayoutBinding> {

	public SampleListAdapter() {
		super(R.layout.row_sample_list_item_layout);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);

		if (getItemViewType(position) == TYPE_ITEM) {
			BindingViewHolder<Sample, RowSampleListItemLayoutBinding> pagedHolder = (BindingViewHolder) holder;
			Sample item = getItem(position);

			pagedHolder.setOnListItemClickListener(this.mOnListItemClickListener);

			pagedHolder.binding.setTestResultMessage(getSampleTestResultMessage(pagedHolder.context, item));

			if (item.isModifiedOrChildModified()) {
				pagedHolder.binding.imgSyncIcon.setVisibility(View.VISIBLE);
				pagedHolder.binding.imgSyncIcon.setImageResource(R.drawable.ic_sync_blue_24dp);
			} else {
				pagedHolder.binding.imgSyncIcon.setVisibility(View.GONE);
			}
		}

		// TODO #704
//        updateUnreadIndicator(holder, record);
	}

//    public void updateUnreadIndicator(DataBoundViewHolder<RowSampleListItemLayoutBinding> holder, Sample item) {
//        backgroundRowItem = (LayerDrawable) ContextCompat.getDrawable(holder.context, R.drawable.background_list_activity_row);
//        unreadListItemIndicator = backgroundRowItem.findDrawableByLayerId(R.id.unreadListItemIndicator);
//
//        if (item != null) {
//            if (item.isUnreadOrChildUnread()) {
//                unreadListItemIndicator.setTint(holder.context.getResources().getColor(R.color.unreadIcon));
//            } else {
//                unreadListItemIndicator.setTint(holder.context.getResources().getColor(android.R.color.transparent));
//            }
//        }
//    }

	private String getSampleTestResultMessage(Context context, Sample record) {
		StringBuilder resultMessage = new StringBuilder();
		if (record.getPathogenTestResult() != null) {
			resultMessage.append(record.getPathogenTestResult());
		} else if (record.getSpecimenCondition() == SpecimenCondition.NOT_ADEQUATE) {
			resultMessage.append(context.getString(R.string.value_inadequate_specimen_condition));
		}

		PathogenTest latestTest = DatabaseHelper.getSampleTestDao().queryMostRecentBySample(record);
		if (latestTest != null && latestTest.getTestResultForSecondDisease() != null) {
			appendResult(
				resultMessage,
				context.getString(R.string.caption_sample_covid_result, latestTest.getTestResultForSecondDisease()));
		}
		if (latestTest != null && latestTest.getTestResultForThirdPathogen() != null) {
			appendResult(
				resultMessage,
				context.getString(R.string.caption_sample_hrsv_result, latestTest.getTestResultForThirdPathogen()));
		}

		return resultMessage.toString();
	}

	private void appendResult(StringBuilder resultMessage, String result) {
		if (resultMessage.length() > 0) {
			resultMessage.append('\n');
		}
		resultMessage.append(result);
	}
}
