/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.formbuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;

public class FormBuilderDtoHelper extends AdoDtoHelper<FormBuilder, FormBuilderDto> {

	@Override
	protected Class<FormBuilder> getAdoClass() {
		return FormBuilder.class;
	}

	@Override
	protected Class<FormBuilderDto> getDtoClass() {
		return FormBuilderDto.class;
	}

	@Override
	protected Call<List<FormBuilderDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
		return RetroProvider.getFormBuilderFacade().pullAllSince(since);
	}

	@Override
	protected Call<List<FormBuilderDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		return RetroProvider.getFormBuilderFacade().pullByUuids(uuids);
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<FormBuilderDto> dtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is read-only");
	}

	@Override
	public void fillInnerFromDto(FormBuilder target, FormBuilderDto source) {
		target.setFormType(source.getFormType());
		target.setDisease(source.getDisease());
		target.setActive(source.getActive());
		if (source.getFormFields().size() > 0) {
			List<FormField> formFields = Optional.of(target).map(FormBuilder::getFormFields).orElseGet(LinkedList::new);
			target.setFormFields(formFields);
			formFields.clear();
			source.getFormFields()
					.stream()
					.map(formFieldReferenceDto -> DatabaseHelper.getFormFieldDao().getByReferenceDto(formFieldReferenceDto))
					.forEach(formFields::add);
			target.setFormFields(formFields);
		}
	}

	@Override
	public void fillInnerFromAdo(FormBuilderDto target, FormBuilder source) {
		target.setFormType(source.getFormType());
		target.setDisease(source.getDisease());
		target.setActive(source.getActive());
	}

	@Override
	protected long getApproximateJsonSizeInBytes() {
		return 0;
	}
}
