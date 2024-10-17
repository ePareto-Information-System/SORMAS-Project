package de.symeda.sormas.app.riskAssessment.list;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.BaseListActivity;
import de.symeda.sormas.app.PagedBaseListActivity;
import de.symeda.sormas.app.PagedBaseListFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.edit.EbsEditFragment;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.riskAssessment.edit.RiskAssessmentEditActivity;
import de.symeda.sormas.app.riskAssessment.edit.RiskAssessmentNewActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Callback;

public class RiskAssessmentListActivity extends PagedBaseListActivity {

    public static RiskAssesment[] riskAssessments = new RiskAssesment[]{
        null,
            RiskAssesment.VERY_HIGH,
            RiskAssesment.HIGH,
            RiskAssesment.MEDIUM,
            RiskAssesment.LOW};
    private RiskAssessmentListViewModel model;

    public static void startActivity(Context context, RiskAssesment listFilter) {
        BaseListActivity.startActivity(context, RiskAssessmentListActivity.class, buildBundle(getStatusFilterPosition(riskAssessments, listFilter)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPreloader();
        adapter = new RiskAssessmentListAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart == 0) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewForList);
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });

        model = new ViewModelProvider(this).get(RiskAssessmentListViewModel.class);
        model.initializeViewModel(EbsEditActivity.getParentEbs().getId());
        model.getRiskAssessment().observe(this, tasks -> {
            adapter.submitList(tasks);
            hidePreloader();
        });

        setOpenPageCallback(p -> {
            showPreloader();
            model.getRiskAssessmentCriteria().setEbsId(EbsEditActivity.getParentEbs().getId());
            model.getRiskAssessmentCriteria().setRiskAssesment(riskAssessments[((PageMenuItem) p).getPosition()]);
            model.notifyCriteriaUpdated();
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        getIntent().putExtra("refreshOnResume", true);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getIntent().getBooleanExtra("refreshOnResume", false)) {
//            showPreloader();
//            model.getEbs().getValue().getDataSource().invalidate();
//        }
//    }

    @Override
    public List<PageMenuItem> getPageMenuData() {
        if (EbsEditActivity.getParentEbs().getSignalVerification().getVerified() != SignalOutcome.EVENT){
            EbsListActivity.startActivity(getContext(),null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.indicator_warning)
                    .setMessage(R.string.signal_not_event)
                    .show();
        }
        return PageMenuItem.fromEnum(riskAssessments, getContext());
    }

    @Override
    protected Callback getSynchronizeResultCallback() {
        // Reload the list after a synchronization has been done
        return () -> {
            showPreloader();
            model.getRiskAssessment().getValue().getDataSource().invalidate();
        };
    }

    @Override
    protected PagedBaseListFragment buildListFragment(PageMenuItem menuItem) {
        if (menuItem != null) {
            RiskAssesment listFilter = riskAssessments[menuItem.getPosition()];
            return RiskAssessmentListFragment.newInstance(listFilter);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getNewMenu().setTitle(R.string.action_new_risk_assessment);
        return true;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.heading_risk_assessment_list;
    }

    @Override
    public void goToNewView() {
        RiskAssessmentNewActivity.startActivity(getContext());
        finish();
    }

    @Override
    protected boolean isEntryCreateAllowed() {
        return ConfigProvider.hasUserRight(UserRight.EVENT_CREATE);
    }

    @Override
    public void addFiltersToPageMenu() {
//        View ebsListFilterView = getLayoutInflater().inflate(R.layout.filter_ebs_list_layout, null);
//        FilterEbsListLayoutBinding ebsListFilterBinding = DataBindingUtil.bind(ebsListFilterView);

//        List<Item> ebsSignalOutcomeList = DataUtils.getEnumItems(SignalOutcome.class, false);
//        ebsListFilterBinding.ebsSignalOutcome.initializeSpinner(ebsSignalOutcomeList);

//        pageMenu.addFilter(ebsListFilterView);

//        ebsListFilterBinding.ebsSignalOutcome.addValueChangedListener(e -> {
//            if (model.getEbsCriteria().getSignalOutcome() != e.getValue()) {
//                showPreloader();
//                model.getEbsCriteria().signalOutcome((SignalOutcome) e.getValue());
//                model.notifyCriteriaUpdated();
//            }
//        });
    }
}
