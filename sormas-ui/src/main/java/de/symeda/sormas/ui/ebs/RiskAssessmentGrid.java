package de.symeda.sormas.ui.ebs;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.ui.Grid;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsReferenceDto;
import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.List;


public class RiskAssessmentGrid extends Grid {
	public static final String INFO = "info";
	private RiskAssessmentDto riskAssessment;
	private EbsDto ebs;

	public RiskAssessmentGrid(RiskAssessmentDto riskAssessment,String ebsUuid) {
		this.riskAssessment = riskAssessment;
		setWidth(80, Unit.PERCENTAGE);
		setHeightUndefined();
		setSelectionMode(SelectionMode.NONE);

		BeanItemContainer<RiskAssessmentDto> container = new BeanItemContainer<RiskAssessmentDto>(RiskAssessmentDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);

		VaadinUiUtil.addIconColumn(generatedContainer, INFO, VaadinIcons.EYE);
		setColumns(RiskAssessmentDto.ASSESSMENT_LEVEL, RiskAssessmentDto.ASSESSMENT_DATE, RiskAssessmentDto.ASSESSMENT_TIME,INFO);
		VaadinUiUtil.setupActionColumn(getColumn(INFO));
		addItemClickListener(e->{
			if (INFO.equals(e.getPropertyId())) {
				ControllerProvider.getEbsController().showAssessmentCaseDialog((RiskAssessmentDto) e.getItemId());
			}
		});
		addItemClickListener(e->{
			if (RiskAssessmentDto.ASSESSMENT_LEVEL.equals(e.getPropertyId()) || e.isDoubleClick()) {
				ControllerProvider.getEbsController().editRiskAssessmentComponent(ebsUuid,true,(RiskAssessmentDto) e.getItemId());
			}
		});
		reload(ebsUuid);

	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<RiskAssessmentDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<RiskAssessmentDto>) container.getWrappedContainer();
	}

	public void reload(String ebsUuid) {
		getContainer().removeAllItems();
		EbsDto ebs = findEbs(ebsUuid);
		List<RiskAssessmentDto> riskAssessment = FacadeProvider.getRiskAssessmentFacade().findBy(new RiskAssessmentCriteria().Ebs(new EbsReferenceDto(ebsUuid)));
		if (riskAssessment == null || riskAssessment.isEmpty()) {
			return;
		}
		getContainer().addAll(riskAssessment);
		this.setHeightByRows(riskAssessment.size());
	}

	private EbsDto findEbs(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}
}