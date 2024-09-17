package de.symeda.sormas.app.ebs.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import java.util.List;

import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.EbsCriteria;


public class EbsListViewModel extends ViewModel {
    private LiveData<PagedList<Ebs>> ebs;
    private EbsListViewModel.EbsDataFactory ebsDataFactory;

    public void initializeViewModel() {
        ebsDataFactory = new EbsListViewModel.EbsDataFactory();
        EbsCriteria ebsCriteria = new EbsCriteria();
        ebsCriteria.signalOutcome(SignalOutcome.EVENT);
        ebsDataFactory.setEbsCriteria(ebsCriteria);
        initializeList();
    }

    public EbsListViewModel() {
        ebsDataFactory = new EbsListViewModel.EbsDataFactory();
        EbsCriteria ebsCriteria = new EbsCriteria();
        ebsCriteria.signalOutcome(SignalOutcome.EVENT);
        ebsDataFactory.setEbsCriteria(ebsCriteria);

        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder eventListBuilder = new LivePagedListBuilder(ebsDataFactory, config);
        ebs = eventListBuilder.build();
    }

    public LiveData<PagedList<Ebs>> getEbs() {
        return ebs;
    }

    void notifyCriteriaUpdated() {
        if (ebs.getValue() != null) {
            ebs.getValue().getDataSource().invalidate();
            if (!ebs.getValue().isEmpty()) {
                ebs.getValue().loadAround(0);
            }
        }
    }

    public EbsCriteria getEbsCriteria(){
        return ebsDataFactory.ebsCriteria;
    }

    public static class EbsDataSource extends PositionalDataSource<Ebs> {

        private EbsCriteria ebsCriteria;

        EbsDataSource(EbsCriteria ebsCriteria) {
            this.ebsCriteria = ebsCriteria;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Ebs> callback) {
            long totalCount = DatabaseHelper.getEbsDao().countByCriteria(ebsCriteria);
            int offset = params.requestedStartPosition;
            int count = params.requestedLoadSize;
            if (offset + count > totalCount) {
                offset = (int) Math.max(0, totalCount - count);
            }
            List<Ebs> ebss = DatabaseHelper.getEbsDao().queryByCriteria(ebsCriteria, offset, count);
            callback.onResult(ebss, offset, (int) totalCount);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Ebs> callback) {
            List<Ebs> ebss = DatabaseHelper.getEbsDao().queryByCriteria(ebsCriteria, params.startPosition, params.loadSize);
            callback.onResult(ebss);
        }
    }

    public static class EbsDataFactory extends DataSource.Factory {

        private MutableLiveData<EbsDataSource> mutableDataSource;
        private EbsListViewModel.EbsDataSource ebsDataSource;
        private EbsCriteria ebsCriteria;

        EbsDataFactory() {
            this.mutableDataSource = new MutableLiveData<>();
        }

        @NonNull
        @Override
        public DataSource create() {
            ebsDataSource = new EbsListViewModel.EbsDataSource(ebsCriteria);
            mutableDataSource.postValue(ebsDataSource);
            return ebsDataSource;
        }

        void setEbsCriteria(EbsCriteria ebsCriteria) {
            this.ebsCriteria = ebsCriteria;
        }
        EbsCriteria getEbsCriteria() {
            return ebsCriteria;
        }
    }

    private void initializeList() {
        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder ebsListBuilder = new LivePagedListBuilder(ebsDataFactory, config);
        ebs = ebsListBuilder.build();
    }
}


