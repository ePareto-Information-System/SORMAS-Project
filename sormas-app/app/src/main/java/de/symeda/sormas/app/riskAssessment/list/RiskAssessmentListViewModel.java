package de.symeda.sormas.app.riskAssessment.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;
import java.util.List;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessmentCriteria;


public class RiskAssessmentListViewModel extends ViewModel {
    private LiveData<PagedList<RiskAssessment>> riskAssessment;
    private RiskAssessmentListViewModel.RiskAssessmentDataFactory riskAssessmentDataFactory;

    public void initializeViewModel(Long ebsId) {
        riskAssessmentDataFactory = new RiskAssessmentListViewModel.RiskAssessmentDataFactory();
        RiskAssessmentCriteria riskAssessmentCriteria = new RiskAssessmentCriteria();
        riskAssessmentCriteria.setEbsId(ebsId);
        riskAssessmentDataFactory.setRiskAssessmentCriteria(riskAssessmentCriteria);
        initializeList();
    }

    public RiskAssessmentListViewModel() {
        riskAssessmentDataFactory = new RiskAssessmentListViewModel.RiskAssessmentDataFactory();
        RiskAssessmentCriteria riskAssessmentCriteria = new RiskAssessmentCriteria();
        riskAssessmentDataFactory.setRiskAssessmentCriteria(riskAssessmentCriteria);

        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder riskAssessmentListBuilder = new LivePagedListBuilder(riskAssessmentDataFactory, config);
        riskAssessment = riskAssessmentListBuilder.build();
    }

    public LiveData<PagedList<RiskAssessment>> getRiskAssessment() {
        return riskAssessment;
    }

    void notifyCriteriaUpdated() {
        if (riskAssessment.getValue() != null) {
            riskAssessment.getValue().getDataSource().invalidate();
            if (!riskAssessment.getValue().isEmpty()) {
                riskAssessment.getValue().loadAround(0);
            }
        }
    }

    public RiskAssessmentCriteria getRiskAssessmentCriteria(){
        return riskAssessmentDataFactory.riskAssessmentCriteria;
    }

    public static class RiskAssessmentDataSource extends PositionalDataSource<RiskAssessment> {

        private RiskAssessmentCriteria riskAssessmentCriteria;

        RiskAssessmentDataSource(RiskAssessmentCriteria riskAssessmentCriteria) {
            this.riskAssessmentCriteria = riskAssessmentCriteria;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<RiskAssessment> callback) {
            long totalCount = DatabaseHelper.getRiskAssessmentDao().countByCriteria(riskAssessmentCriteria);

            int offset = params.requestedStartPosition;
            int count = params.requestedLoadSize;

            if (offset + count > totalCount) {
                offset = (int) Math.max(0, totalCount - count);
            }

            // Modify the query to include the Ebs UUID filter
            List<RiskAssessment> risks = DatabaseHelper.getRiskAssessmentDao().queryByCriteria(
                    riskAssessmentCriteria,
                    offset,
                    count
            );

            callback.onResult(risks, offset, (int) totalCount);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<RiskAssessment> callback) {
            List<RiskAssessment> risks = DatabaseHelper.getRiskAssessmentDao().queryByCriteria(riskAssessmentCriteria, params.startPosition, params.loadSize);
            callback.onResult(risks);
        }
    }

    public static class RiskAssessmentDataFactory extends DataSource.Factory {

        private MutableLiveData<RiskAssessmentListViewModel.RiskAssessmentDataSource> mutableDataSource;
        private RiskAssessmentDataSource riskAssessmentDataSource;
        private RiskAssessmentCriteria riskAssessmentCriteria;

        RiskAssessmentDataFactory() {
            this.mutableDataSource = new MutableLiveData<>();
        }

        @NonNull
        @Override
        public DataSource create() {
            riskAssessmentDataSource = new RiskAssessmentListViewModel.RiskAssessmentDataSource(riskAssessmentCriteria);
            mutableDataSource.postValue(riskAssessmentDataSource);
            return riskAssessmentDataSource;
        }

        void setRiskAssessmentCriteria(RiskAssessmentCriteria riskAssessmentCriteria) {
            this.riskAssessmentCriteria = riskAssessmentCriteria;
        }
        RiskAssessmentCriteria getRiskAssessmentCriteria() {
            return riskAssessmentCriteria;
        }
    }

    private void initializeList() {
        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true).setInitialLoadSizeHint(16).setPageSize(8).build();

        LivePagedListBuilder riskAssessmentListBuilder = new LivePagedListBuilder(riskAssessmentDataFactory, config);
        riskAssessment = riskAssessmentListBuilder.build();
    }
}