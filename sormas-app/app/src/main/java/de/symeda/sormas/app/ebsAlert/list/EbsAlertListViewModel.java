package de.symeda.sormas.app.ebsAlert.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import java.util.List;

import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlert;
import de.symeda.sormas.app.backend.ebs.ebsAlert.EbsAlertCriteria;


public class EbsAlertListViewModel extends ViewModel {
    private LiveData<PagedList<EbsAlert>> ebsAlert;
    private EbsAlertListViewModel.EbsAlertDataFactory ebsAlertDataFactory;

    public void initializeViewModel(Long ebsId) {
        ebsAlertDataFactory = new EbsAlertListViewModel.EbsAlertDataFactory();
        EbsAlertCriteria ebsAlertCriteria = new EbsAlertCriteria();
        ebsAlertCriteria.setEbsId(ebsId);
        ebsAlertDataFactory.setEbsAlertCriteria(ebsAlertCriteria);
        initializeList();
    }

    public EbsAlertListViewModel() {
        ebsAlertDataFactory = new EbsAlertListViewModel.EbsAlertDataFactory();
        EbsAlertCriteria ebsAlertCriteria = new EbsAlertCriteria();
        ebsAlertDataFactory.setEbsAlertCriteria(ebsAlertCriteria);

        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder ebsAlertListBuilder = new LivePagedListBuilder(ebsAlertDataFactory, config);
        ebsAlert = ebsAlertListBuilder.build();
    }

    public LiveData<PagedList<EbsAlert>> getEbsAlert() {
        return ebsAlert;
    }

    void notifyCriteriaUpdated() {
        if (ebsAlert.getValue() != null) {
            ebsAlert.getValue().getDataSource().invalidate();
            if (!ebsAlert.getValue().isEmpty()) {
                ebsAlert.getValue().loadAround(0);
            }
        }
    }

    public EbsAlertCriteria getEbsAlertCriteria(){
        return ebsAlertDataFactory.ebsAlertCriteria;
    }

    public static class EbsAlertDataSource extends PositionalDataSource<EbsAlert> {

        private EbsAlertCriteria ebsAlertCriteria;

        EbsAlertDataSource(EbsAlertCriteria ebsAlertCriteria) {
            this.ebsAlertCriteria = ebsAlertCriteria;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<EbsAlert> callback) {
            long totalCount = DatabaseHelper.getEbsAlertDao().countByCriteria(ebsAlertCriteria);

            int offset = params.requestedStartPosition;
            int count = params.requestedLoadSize;

            if (offset + count > totalCount) {
                offset = (int) Math.max(0, totalCount - count);
            }

            // Modify the query to include the Ebs UUID filter
            List<EbsAlert> alerts = DatabaseHelper.getEbsAlertDao().queryByCriteria(
                    ebsAlertCriteria,
                    offset,
                    count
            );

            callback.onResult(alerts, offset, (int) totalCount);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<EbsAlert> callback) {
            List<EbsAlert> alerts = DatabaseHelper.getEbsAlertDao().queryByCriteria(ebsAlertCriteria, params.startPosition, params.loadSize);
            callback.onResult(alerts);
        }
    }

    public static class EbsAlertDataFactory extends DataSource.Factory {

        private MutableLiveData<EbsAlertListViewModel.EbsAlertDataSource> mutableDataSource;
        private EbsAlertDataSource ebsAlertDataSource;
        private EbsAlertCriteria ebsAlertCriteria;

        EbsAlertDataFactory() {
            this.mutableDataSource = new MutableLiveData<>();
        }

        @NonNull
        @Override
        public DataSource create() {
            ebsAlertDataSource = new EbsAlertListViewModel.EbsAlertDataSource(ebsAlertCriteria);
            mutableDataSource.postValue(ebsAlertDataSource);
            return ebsAlertDataSource;
        }

        void setEbsAlertCriteria(EbsAlertCriteria ebsAlertCriteria) {
            this.ebsAlertCriteria = ebsAlertCriteria;
        }
        EbsAlertCriteria getEbsAlertCriteria() {
            return ebsAlertCriteria;
        }
    }

    private void initializeList() {
        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder ebsAlertListBuilder = new LivePagedListBuilder(ebsAlertDataFactory, config);
        ebsAlert = ebsAlertListBuilder.build();
    }
}