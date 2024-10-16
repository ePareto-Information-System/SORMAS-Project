package de.symeda.sormas.app.riskAssessment.list;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.core.adapter.databinding.BindingPagedListAdapter;
import de.symeda.sormas.app.core.adapter.databinding.BindingViewHolder;
import de.symeda.sormas.app.databinding.RowEbsListItemLayoutBinding;
import de.symeda.sormas.app.databinding.RowEbsRiskAssessmentListItemLayoutBinding;

public class RiskAssessmentListAdapter extends BindingPagedListAdapter<RiskAssessment, RowEbsRiskAssessmentListItemLayoutBinding> {

    public RiskAssessmentListAdapter() {super(R.layout.row_ebs_risk_assessment_list_item_layout);}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        super.onBindViewHolder(holder,position);

        if(getItemViewType(position) == TYPE_ITEM){
            BindingViewHolder<RiskAssessment, RowEbsRiskAssessmentListItemLayoutBinding> pagedHolder = (BindingViewHolder) holder;
            RiskAssessment item = getItem(position);

            pagedHolder.setOnListItemClickListener(this.mOnListItemClickListener);

            indicateRiskAssessment(pagedHolder.binding.imgRiskStatusIcon,item);

            if (item.isModifiedOrChildModified()){
                pagedHolder.binding.imgSyncIcon.setVisibility(View.VISIBLE);
                pagedHolder.binding.imgSyncIcon.setImageResource(R.drawable.ic_sync_blue_24dp);
            }else {
                pagedHolder.binding.imgSyncIcon.setVisibility(View.GONE);
            }
        }
    }

    public void indicateRiskAssessment(ImageView imgRiskPriorityIcon, RiskAssessment riskAssessment) {
        Resources resources = imgRiskPriorityIcon.getContext().getResources();
        Drawable drw = (Drawable) ContextCompat.getDrawable(imgRiskPriorityIcon.getContext(), R.drawable.indicator_status_circle);
        //TODO: MAKE IT CONFORM TO RISKASSESSMENT
        if (riskAssessment.getRiskAssessment() == RiskAssesment.VERY_HIGH) {
            drw.setColorFilter(resources.getColor(R.color.veryHighRisk), PorterDuff.Mode.SRC_OVER);
        } else if (riskAssessment.getRiskAssessment() == RiskAssesment.HIGH) {
            drw.setColorFilter(resources.getColor(R.color.highRisk), PorterDuff.Mode.SRC_OVER);
        } else if (riskAssessment.getRiskAssessment() == RiskAssesment.MEDIUM) {
            drw.setColorFilter(resources.getColor(R.color.moderateRisk), PorterDuff.Mode.SRC_OVER);
        } else if (riskAssessment.getRiskAssessment() == RiskAssesment.LOW) {
            drw.setColorFilter(resources.getColor(R.color.lowRisk), PorterDuff.Mode.SRC_OVER);
        }

        imgRiskPriorityIcon.setBackground(drw);
    }
}
