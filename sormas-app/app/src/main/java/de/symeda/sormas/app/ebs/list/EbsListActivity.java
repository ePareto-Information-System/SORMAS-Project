package de.symeda.sormas.app.ebs.list;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.app.BaseListActivity;
import de.symeda.sormas.app.PagedBaseListActivity;
import de.symeda.sormas.app.PagedBaseListFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.menu.PageMenuItem;
import de.symeda.sormas.app.ebs.edit.EbsNewActivity;
import de.symeda.sormas.app.util.Callback;
import de.symeda.sormas.app.util.DataUtils;

public class EbsListActivity extends PagedBaseListActivity {

    public static EbsSourceType[] sourceTypes = new EbsSourceType[]{
        null,
            EbsSourceType.CEBS,
            EbsSourceType.HEBS};
    private EbsListViewModel model;

    public static void startActivity(Context context, SignalOutcome listFilter) {
        BaseListActivity.startActivity(context, EbsListActivity.class, buildBundle(getStatusFilterPosition(sourceTypes, listFilter)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showPreloader();
        adapter = new EbsListAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                // Scroll to the topmost position after cases have been inserted
                if (positionStart == 0) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewForList);
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });
        model = new ViewModelProvider(this).get(EbsListViewModel.class);
        model.initializeViewModel();
        model.getEbs().observe(this, tasks -> {
            adapter.submitList(tasks);
            hidePreloader();
        });
        setOpenPageCallback(p -> {
            showPreloader();
            model.getEbsCriteria().setSourceType(sourceTypes[((PageMenuItem) p).getPosition()]);
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
        return PageMenuItem.fromEnum(sourceTypes, getContext());
    }

    @Override
    protected Callback getSynchronizeResultCallback() {
        // Reload the list after a synchronization has been done
        return () -> {
            showPreloader();
            model.getEbs().getValue().getDataSource().invalidate();
        };
    }

    @Override
    protected PagedBaseListFragment buildListFragment(PageMenuItem menuItem) {
        if (menuItem != null) {
            EbsSourceType listFilter = sourceTypes[menuItem.getPosition()];
            return EbsListFragment.newInstance(listFilter);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getNewMenu().setTitle(R.string.action_new_ebs);
        return true;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.heading_ebs_list;
    }

    @Override
    public void goToNewView() {
        EbsNewActivity.startActivity(this);
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