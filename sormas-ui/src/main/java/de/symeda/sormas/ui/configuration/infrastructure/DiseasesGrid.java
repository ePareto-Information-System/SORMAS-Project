package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConCriteria;
import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConIndexDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.stream.Collectors;

public class DiseasesGrid extends FilteredGrid<DiseaseConIndexDto, DiseaseConCriteria> {
    private static final long serialVersionUID = 4488941182432777837L;


    public DiseasesGrid(DiseaseConCriteria criteria) {
        super(DiseaseConIndexDto.class);
        setSizeFull();

        ViewConfiguration viewConfiguration = ViewModelProviders.of(DiseasesView.class).get(ViewConfiguration.class);
        setInEagerMode(viewConfiguration.isInEagerMode());

        if (isInEagerMode() && UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS)) {
            setCriteria(criteria);
            setEagerDataProvider();
        } else {
            setLazyDataProvider();
            setCriteria(criteria);
        }


        setColumns(
                DiseaseConIndexDto.UUID,
                DiseaseConIndexDto.DISEASE,
                DiseaseConIndexDto.ACTIVE,
                DiseaseConIndexDto.PRIMARY_DISEASE,
                DiseaseConIndexDto.FOLLOW_UP_ENABLED,
                DiseaseConIndexDto.FOLLOW_UP_DURATION,
                DiseaseConIndexDto.EXTENDED_CLASSIFICATION,
                DiseaseConIndexDto.FACILITIES

        );

        if (UserProvider.getCurrent().hasUserRight(UserRight.INFRASTRUCTURE_EDIT)) {

            addEditColumn(e -> {
                ControllerProvider.getInfrastructureController().editDisease(e.getUuid());
            });
		}

		for (Column<?, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(DiseaseConIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
    }

    public void setLazyDataProvider() {
        DataProvider<DiseaseConIndexDto, DiseaseConCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> FacadeProvider.getDiseaseFacade()
                        .getIndexList(
                                query.getFilter().orElse(null),
                                query.getOffset(),
                                query.getLimit(),
                                query.getSortOrders()
                                        .stream()
                                        .map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
                                        .collect(Collectors.toList()))
                        .stream(),
                query -> (int) FacadeProvider.getDiseaseFacade().count(query.getFilter().orElse(null)));
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.NONE);
    }

    public void setEagerDataProvider() {

        ListDataProvider<DiseaseConIndexDto> dataProvider =
                DataProvider.fromStream(FacadeProvider.getDiseaseFacade().getIndexList(getCriteria(), null, null, null).stream());
        setDataProvider(dataProvider);
    }

    public void reload() {
        getDataProvider().refreshAll();
    }
}
