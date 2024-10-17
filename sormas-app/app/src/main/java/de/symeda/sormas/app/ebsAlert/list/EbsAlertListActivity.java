package de.symeda.sormas.app.ebsAlert.list;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.app.BaseListActivity;
import de.symeda.sormas.app.PagedBaseListActivity;
import de.symeda.sormas.app.PagedBaseListFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.ebs.edit.EbsEditActivity;
import de.symeda.sormas.app.ebs.list.EbsListActivity;
import de.symeda.sormas.app.ebsAlert.edit.EbsAlertNewActivity;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Callback;

public class EbsAlertListActivity extends PagedBaseListActivity {

    public static ResponseStatus[] responseStatuses = new ResponseStatus[]{
        null,
            ResponseStatus.NOT_STARTED,
            ResponseStatus.ON_GOING,
            ResponseStatus.COMPLETED};
    private EbsAlertListViewModel model;

    public static void startActivity(Context context, ResponseStatus listFilter) {
        BaseListActivity.startActivity(context, EbsAlertListActivity.class, buildBundle(getStatusFilterPosition(responseStatuses, listFilter)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPreloader();
        adapter = new EbsAlertListAdapter();
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

        model = new ViewModelProvider(this).get(EbsAlertListViewModel.class);
        model.initializeViewModel(EbsEditActivity.getParentEbs().getId());
        model.getEbsAlert().observe(this, tasks -> {
            adapter.submitList(tasks);
            hidePreloader();
        });

        setOpenPageCallback(p -> {
            showPreloader();
            model.getEbsAlertCriteria().setEbsId(EbsEditActivity.getParentEbs().getId());
            model.getEbsAlertCriteria().setResponseStatus(responseStatuses[((PageMenuItem) p).getPosition()]);
            model.notifyCriteriaUpdated();
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        getIntent().putExtra("refreshOnResume", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("refreshOnResume", false)) {
            showPreloader();
            model.getEbsAlert().getValue().getDataSource().invalidate();
        }
    }

    @Override
    public List<PageMenuItem> getPageMenuData() {
        if (EbsEditActivity.getParentEbs().getSignalVerification().getVerified() != SignalOutcome.EVENT){
            EbsListActivity.startActivity(getContext(),null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.indicator_warning)
                    .setMessage(R.string.signal_not_event)
                    .show();
        }
        return PageMenuItem.fromEnum(responseStatuses, getContext());
    }

    @Override
    protected Callback getSynchronizeResultCallback() {
        // Reload the list after a synchronization has been done
        return () -> {
            showPreloader();
            model.getEbsAlert().getValue().getDataSource().invalidate();
        };
    }

    @Override
    protected PagedBaseListFragment buildListFragment(PageMenuItem menuItem) {
        if (menuItem != null) {
            ResponseStatus listFilter = responseStatuses[menuItem.getPosition()];
            return EbsAlertListFragment.newInstance(listFilter);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getNewMenu().setTitle(R.string.action_new_alert);
        return true;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.heading_alert_list;
    }

    @Override
    public void goToNewView() {
        EbsAlertNewActivity.startActivity(getContext());
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
