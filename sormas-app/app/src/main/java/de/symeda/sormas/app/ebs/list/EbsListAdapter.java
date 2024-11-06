package de.symeda.sormas.app.ebs.list;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.core.adapter.databinding.BindingPagedListAdapter;
import de.symeda.sormas.app.core.adapter.databinding.BindingViewHolder;
import de.symeda.sormas.app.databinding.RowEbsListItemLayoutBinding;

public class EbsListAdapter extends BindingPagedListAdapter<Ebs, RowEbsListItemLayoutBinding> {

    public EbsListAdapter() {super(R.layout.row_ebs_list_item_layout);}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        super.onBindViewHolder(holder,position);

        if(getItemViewType(position) == TYPE_ITEM){
            BindingViewHolder<Ebs, RowEbsListItemLayoutBinding> pagedHolder = (BindingViewHolder) holder;
            Ebs item = getItem(position);

            pagedHolder.setOnListItemClickListener(this.mOnListItemClickListener);

            indicateEbsOutcome(pagedHolder.binding.imgEbsStatusIcon,item);

            if (item.isModifiedOrChildModified()){
                pagedHolder.binding.imgSyncIcon.setVisibility(View.VISIBLE);
                pagedHolder.binding.imgSyncIcon.setImageResource(R.drawable.ic_sync_blue_24dp);
            }else {
                pagedHolder.binding.imgSyncIcon.setVisibility(View.GONE);
            }
        }
    }

    public void indicateEbsOutcome(ImageView imgEbsPriorityIcon, Ebs ebs) {
        Resources resources = imgEbsPriorityIcon.getContext().getResources();
        Drawable drw = (Drawable) ContextCompat.getDrawable(imgEbsPriorityIcon.getContext(), R.drawable.indicator_status_circle);
        //TODO: MAKE IT CONFORM TO SIGNAL OUTCOME
        SignalVerification signalVerification = ebs.getSignalVerification();
        if (signalVerification == null) {
            drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityNormal), PorterDuff.Mode.SRC_OVER);
        } else {
            SignalOutcome verifiedStatus = signalVerification.getVerified();
            if (verifiedStatus == null) {
                drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityHigh), PorterDuff.Mode.SRC_OVER);
            } else if (verifiedStatus == SignalOutcome.EVENT) {
                drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityLow), PorterDuff.Mode.SRC_OVER);
            } else if (verifiedStatus == SignalOutcome.NON_EVENT) {
                drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityNormal), PorterDuff.Mode.SRC_OVER);
            } else {
                drw.setColorFilter(resources.getColor(R.color.indicatorTaskPriorityLow), PorterDuff.Mode.SRC_OVER);
            }
        }

        imgEbsPriorityIcon.setBackground(drw);
    }
}
