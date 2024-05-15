package de.symeda.sormas.ui.ebs;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.ui.Grid;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.utils.ShowDetailsListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.List;


public class RiskAssessmentGrid extends Grid {
	public static final String INFO = "info";
	private RiskAssessmentDto riskAssessment;
	private EbsDto ebs;

	public RiskAssessmentGrid(RiskAssessmentDto riskAssessment,String ebsUuid) {
		this.riskAssessment = riskAssessment;
		setWidth(960, Unit.PIXELS);
		setHeightUndefined();
		setSelectionMode(SelectionMode.NONE);

		BeanItemContainer<RiskAssessmentDto> container = new BeanItemContainer<RiskAssessmentDto>(RiskAssessmentDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);
		VaadinUiUtil.addIconColumn(generatedContainer, INFO, VaadinIcons.EYE);
		setColumns(RiskAssessmentDto.RISK_ASSESSMENT, RiskAssessmentDto.RESPONSE_DATE, RiskAssessmentDto.RESPONSE_TIME,INFO);
//		addItemClickListener(new ShowDetailsListener<>(INFO, e -> ControllerProvider.getEbsController().navigateToData(e.getUuid())));

//		reload(ebsUuid);
	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<RiskAssessmentDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<RiskAssessmentDto>) container.getWrappedContainer();
	}

	public void reload(String ebsUuid) {
		getContainer().removeAllItems();
		EbsDto ebs = findEbs(ebsUuid);
		List<RiskAssessmentDto> riskAssessment = (List<RiskAssessmentDto>) ebs.getRiskAssessment();
		getContainer().addAll(riskAssessment);
		this.setHeightByRows(riskAssessment.size());
	}

	private EbsDto findEbs(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}
}