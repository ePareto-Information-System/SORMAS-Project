package de.symeda.sormas.app.ebsAlert.list;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.core.adapter.databinding.BindingPagedListAdapter;
import de.symeda.sormas.app.core.adapter.databinding.BindingViewHolder;
import de.symeda.sormas.app.databinding.RowEbsAlertListItemLayoutBindingImpl;

public class EbsAlertListAdapter extends BindingPagedListAdapter<EbsAlert, RowEbsAlertListItemLayoutBindingImpl> {

    public EbsAlertListAdapter() {super(R.layout.row_ebs_alert_list_item_layout);}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        super.onBindViewHolder(holder,position);

        if(getItemViewType(position) == TYPE_ITEM){
            BindingViewHolder<EbsAlert, RowEbsAlertListItemLayoutBindingImpl> pagedHolder = (BindingViewHolder) holder;
            EbsAlert item = getItem(position);

            pagedHolder.setOnListItemClickListener(this.mOnListItemClickListener);

            indicateResponseStatus(pagedHolder.binding.imgEbsStatusIcon,item);

            if (item.isModifiedOrChildModified()){
                pagedHolder.binding.imgSyncIcon.setVisibility(View.VISIBLE);
                pagedHolder.binding.imgSyncIcon.setImageResource(R.drawable.ic_sync_blue_24dp);
            }else {
                pagedHolder.binding.imgSyncIcon.setVisibility(View.GONE);
            }
        }
    }

    public void indicateResponseStatus(ImageView imgStatusPriorityIcon, EbsAlert ebsAlert) {
        Resources resources = imgStatusPriorityIcon.getContext().getResources();
        Drawable drw = (Drawable) ContextCompat.getDrawable(imgStatusPriorityIcon.getContext(), R.drawable.indicator_status_circle);
        //TODO: MAKE IT CONFORM TO RESPONSE STATUS
        if (ebsAlert.getResponseStatus() == ResponseStatus.COMPLETED) {
            drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityHigh), PorterDuff.Mode.SRC_OVER);
        } else if (ebsAlert.getResponseStatus() == ResponseStatus.ON_GOING) {
            drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityLow), PorterDuff.Mode.SRC_OVER);
        } else if (ebsAlert.getResponseStatus() == ResponseStatus.NOT_STARTED) {
            drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityNormal), PorterDuff.Mode.SRC_OVER);
        }
        imgStatusPriorityIcon.setBackground(drw);
    }
}
