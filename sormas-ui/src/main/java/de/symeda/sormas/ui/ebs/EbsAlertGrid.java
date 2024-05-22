package de.symeda.sormas.ui.ebs;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.ui.Grid;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import java.util.List;


public class EbsAlertGrid extends Grid {
	public static final String INFO = "info";
	private EbsAlertDto ebsAlertDto;
	private EbsDto ebs;

	public EbsAlertGrid(EbsAlertDto ebsAlertDto, String ebsUuid) {
		this.ebsAlertDto = ebsAlertDto;
		setWidth(80, Unit.PERCENTAGE);
		setHeightUndefined();
		setSelectionMode(SelectionMode.NONE);

		BeanItemContainer<EbsAlertDto> container = new BeanItemContainer<EbsAlertDto>(EbsAlertDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);

		VaadinUiUtil.addIconColumn(generatedContainer, INFO, VaadinIcons.EYE);
		setColumns(EbsAlertDto.ACTION_INITIATED, EbsAlertDto.RESPONSE_STATUS, EbsAlertDto.ALERT_USED,INFO);
		VaadinUiUtil.setupActionColumn(getColumn(INFO));
//		addItemClickListener(new ShowDetailsListener<>(INFO, e -> ControllerProvider.getEbsController().navigateToData(e.getUuid())));

		reload(ebsUuid);
	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<EbsAlertDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<EbsAlertDto>) container.getWrappedContainer();
	}

	public void reload(String ebsUuid) {
		getContainer().removeAllItems();
		EbsDto ebs = findEbs(ebsUuid);
		List<EbsAlertDto> ebsAlertDto = FacadeProvider.getAlertFacade().findBy(new RiskAssessmentCriteria().Ebs(new EbsReferenceDto(ebsUuid)));
		if (ebsAlertDto == null || ebsAlertDto.isEmpty()) {
			return;
		}
		getContainer().addAll(ebsAlertDto);
		this.setHeightByRows(ebsAlertDto.size());
	}

	private EbsDto findEbs(String uuid) {
		return FacadeProvider.getEbsFacade().getEbsByUuid(uuid, false);
	}
}