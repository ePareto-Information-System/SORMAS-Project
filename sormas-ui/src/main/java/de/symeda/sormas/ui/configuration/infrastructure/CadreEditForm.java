package de.symeda.sormas.ui.configuration.infrastructure;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.infrastructure.area.AreaDto;
import de.symeda.sormas.api.infrastructure.cadre.CadreDto;
import de.symeda.sormas.ui.utils.AbstractEditForm;

@SuppressWarnings("serial")
public class CadreEditForm extends AbstractEditForm<CadreDto> {

	private static final String HTML_LAYOUT = fluidRowLocs(CadreDto.POSITION, CadreDto.EXTERNAL_ID);

	public CadreEditForm(boolean create) {
		super(CadreDto.class, CadreDto.I18N_PREFIX);

		setWidth(840, Unit.PIXELS);

		if (create) {
			hideValidationUntilNextCommit();
		}
	}

	@Override
	protected void addFields() {
		addField(CadreDto.POSITION, TextField.class);
		addField(CadreDto.EXTERNAL_ID, TextField.class);

		setRequired(true, CadreDto.POSITION);
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

}
