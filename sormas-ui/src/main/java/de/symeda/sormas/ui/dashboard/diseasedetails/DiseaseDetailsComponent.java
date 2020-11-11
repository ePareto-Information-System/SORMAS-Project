package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;

public class DiseaseDetailsComponent extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label viewTitleLabel;
	private VerticalLayout verticalLayout;

	public DiseaseDetailsComponent(Label viewTitleLabel) {
		setSizeFull();
		setMargin(false);

		this.viewTitleLabel = viewTitleLabel;
//		this.tasksView = tasksView;
//		originalViewTitle = viewTitleLabel.getValue();

//		criteria = ViewModelProviders.of(TasksView.class).get(TaskCriteria.class);
//		if (criteria.getRelevanceStatus() == null) {
//			criteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
//		}

//		grid = new TaskGrid(criteria);
		verticalLayout = new VerticalLayout();
		verticalLayout.setCaption("Welcome to the disease dashboard.....");
//		verticalLayout.addComponent(createFilterBar());
//		verticalLayout.addComponent(createAssigneeFilterBar());
//		verticalLayout.addComponent(grid);
//		grid.getDataProvider().addDataProviderListener(e -> updateAssigneeFilterButtons());

		verticalLayout.setMargin(true);
//		styleGridLayout(verticalLayout);

		addComponent(verticalLayout);
	}

	private void addTopLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setHeight(80, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);
//		CssStyles.style(layout, CssStyles.getSampleCountColor(sampleCountType));
	}

	private VerticalLayout addButtomLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setHeight(80, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);
//		CssStyles.style(layout, CssStyles.getSampleCountColor(sampleCountType));
		return layout;

	}

	private void addDiseaseCardLayout(
		Disease disease,
		boolean outbreak,
		Long countDisease,
		Long deaths,
		double percentage,
		String reportedDistrict,
		String outBreakDistrict) {

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setHeight(80, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);

//		layout.addComponent(addTopLayout());
//		layout.setExpandRatio(addTopLayout(), 1);

		addComponent(layout);
	}

	private void addDiseaseTableLayout(CaseDataDto caseDataDto) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setHeight(80, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);

		addComponent(layout);
	}

	private void addDiseaseMapLayout(CaseDataDto caseDataDto) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.setHeight(80, Unit.PIXELS);
		layout.setWidth(100, Unit.PERCENTAGE);

		addComponent(layout);
	}

	public void reload(ViewChangeEvent event) {
		String params = event.getParameters().trim();
		if (params.startsWith("?")) {
			params = params.substring(1);
//			criteria.fromUrlParams(params);
		}
//		updateFilterComponents();
//		grid.reload();
	}

}
