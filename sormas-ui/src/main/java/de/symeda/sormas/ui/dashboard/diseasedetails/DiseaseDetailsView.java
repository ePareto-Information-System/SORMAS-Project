package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;

import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.ViewConfiguration;

@SuppressWarnings("serial")
public class DiseaseDetailsView extends AbstractView {

	private static final long serialVersionUID = -1L;
	public static final String VIEW_NAME = "disease";
	protected DiseaseDetailsViewLayout diseaseDetailsViewLayout;
	private DiseaseDetailsComponent diseaseDetailsComponent;
	private ViewConfiguration viewConfiguration;

	public DiseaseDetailsView() {
		super(VIEW_NAME);
		SormasUI.get().getNavigator().addView("0", DiseaseDetailsView.class);// o(navigationState));
		if (!ViewModelProviders.of(DiseaseDetailsView.class).has(DiseaseDetailsView.class)) {
			ViewModelProviders.of(DiseaseDetailsView.class);
			//.get(DiseaseDetailsView.class, taskCriteria);
		}
		viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);

		diseaseDetailsComponent = new DiseaseDetailsComponent(new Label(VIEW_NAME));
		addComponent(diseaseDetailsComponent);
		setExpandRatio(diseaseDetailsComponent, 1);
		System.err.println("Here we go...");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		diseaseDetailsComponent.reload(event);
	}

}
