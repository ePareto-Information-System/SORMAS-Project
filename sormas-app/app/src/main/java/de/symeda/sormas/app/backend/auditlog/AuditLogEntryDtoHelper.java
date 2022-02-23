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

package de.symeda.sormas.app.backend.auditlog;

import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Type;
import java.util.List;

import de.symeda.sormas.api.HasUuid;
import de.symeda.sormas.api.PushResult;
import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
//import de.symeda.sormas.api.auditlog.AuditLogEntryReferenceDto;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.auditlog.AuditLogEntry;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfoDtoHelper;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.user.UserDtoHelper;
import de.symeda.sormas.app.rest.AuditLogEntryFacadeRetro;
import de.symeda.sormas.app.rest.NoConnectionException;
import de.symeda.sormas.app.rest.RetroProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuditLogEntryDtoHelper extends AdoDtoHelper<AuditLogEntry, AuditLogEntryDto> {

	private SormasToSormasOriginInfoDtoHelper sormasToSormasOriginInfoDtoHelper = new SormasToSormasOriginInfoDtoHelper();

	@Override
	protected Class<AuditLogEntry> getAdoClass() {
		return AuditLogEntry.class;
	}

	@Override
	protected Class<AuditLogEntryDto> getDtoClass() {
		return AuditLogEntryDto.class;
	}

	@Override
	protected Call<List<AuditLogEntryDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid) throws NoConnectionException {
		return null;
	}

//	@Override
//	protected Call<List<AuditLogEntryDto>> pullAllSince(long since) throws NoConnectionException {
//		return null; //RetroProvider.getAuditLogEntryFacade().pullAllSince(since);
//	}

	@Override
	protected Call<List<AuditLogEntryDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		return null; //RetroProvider.getAuditLogEntryFacade().pullByUuids(uuids);
	}

	@Override
	protected Call<List<PushResult>> pushAll(List<AuditLogEntryDto> sampleDtos) throws NoConnectionException {
		return null; //RetroProvider.getAuditLogEntryFacade().pushAll(sampleDtos);
	}

	@Override
	public void fillInnerFromDto(AuditLogEntry target, AuditLogEntryDto source) {

//		target.setAssociatedCase(DatabaseHelper.getCaseDao().getByReferenceDto(source.getAssociatedCase()));
//
//		target.setReportingUser(DatabaseHelper.getUserDao().getByReferenceDto(source.getReportingUser()));
//		target.setReportDateTime(source.getReportDateTime());
//
//		target.setLab(DatabaseHelper.getFacilityDao().getByReferenceDto(source.getLab()));
//		target.setLabDetails(source.getLabDetails());
//
//		target.setLabSampleID(source.getLabSampleID());
//		target.setFieldSampleID(source.getFieldSampleID());
//		target.setSampleDateTime(source.getSampleDateTime());
//		target.setSampleMaterial(source.getSampleMaterial());
//		target.setSampleMaterialText(source.getSampleMaterialText());
//		target.setSamplePurpose(source.getSamplePurpose());
//		target.setShipmentDate(source.getShipmentDate());
//		target.setShipmentDetails(source.getShipmentDetails());
//		target.setReceivedDate(source.getReceivedDate());
//		target.setSpecimenCondition(source.getSpecimenCondition());
//		target.setNoTestPossibleReason(source.getNoTestPossibleReason());
//		target.setComment(source.getComment());
//		target.setSampleSource(source.getSampleSource());
//		if (source.getReferredTo() != null) {
//			target.setReferredToUuid(source.getReferredTo().getUuid());
//		} else {
//			target.setReferredToUuid(null);
//		}
//		target.setShipped(source.isShipped());
//		target.setReceived(source.isReceived());
//		target.setPathogenTestResult(source.getPathogenTestResult());
//		target.setPathogenTestingRequested(source.getPathogenTestingRequested());
//		target.setAdditionalTestingRequested(source.getAdditionalTestingRequested());
//		target.setRequestedPathogenTests(source.getRequestedPathogenTests());
//		target.setRequestedAdditionalTests(source.getRequestedAdditionalTests());
//		target.setRequestedOtherPathogenTests(source.getRequestedOtherPathogenTests());
//		target.setRequestedOtherAdditionalTests(source.getRequestedOtherAdditionalTests());
//
//		target.setReportLat(source.getReportLat());
//		target.setReportLon(source.getReportLon());
//		target.setReportLatLonAccuracy(source.getReportLatLonAccuracy());
//
//		target.setSormasToSormasOriginInfo(
//			sormasToSormasOriginInfoDtoHelper.fillOrCreateFromDto(target.getSormasToSormasOriginInfo(), source.getSormasToSormasOriginInfo()));
//		target.setOwnershipHandedOver(source.isOwnershipHandedOver());
//
//		target.setPseudonymized(source.isPseudonymized());
	}

	@Override
	public void fillInnerFromAdo(AuditLogEntryDto target, AuditLogEntry source) {
//		if (source.getAssociatedCase() != null) {
//			Case associatedCase = DatabaseHelper.getCaseDao().queryForId(source.getAssociatedCase().getId());
//			target.setAssociatedCase(CaseDtoHelper.toReferenceDto(associatedCase));
//		} else {
//			target.setAssociatedCase(null);
//		}
//
//		if (source.getReportingUser() != null) {
//			User user = DatabaseHelper.getUserDao().queryForId(source.getReportingUser().getId());
//			target.setReportingUser(UserDtoHelper.toReferenceDto(user));
//		} else {
//			target.setReportingUser(null);
//		}
//
//		if (source.getLab() != null) {
//			Facility lab = DatabaseHelper.getFacilityDao().queryForId(source.getLab().getId());
//			target.setLab(FacilityDtoHelper.toReferenceDto(lab));
//		} else {
//			target.setLab(null);
//		}
//
//		if (source.getReferredToUuid() != null) {
//			target.setReferredTo(new SampleReferenceDto(source.getReferredToUuid()));
//		} else {
//			target.setReferredTo(null);
//		}
//
//		target.setLabDetails(source.getLabDetails());
//		target.setLabSampleID(source.getLabSampleID());
//		target.setFieldSampleID(source.getFieldSampleID());
//		target.setSampleDateTime(source.getSampleDateTime());
//		target.setReportDateTime(source.getReportDateTime());
//		target.setSampleMaterial(source.getSampleMaterial());
//		target.setSampleMaterialText(source.getSampleMaterialText());
//		target.setSamplePurpose(source.getSamplePurpose());
//		target.setShipmentDate(source.getShipmentDate());
//		target.setShipmentDetails(source.getShipmentDetails());
//		target.setReceivedDate(source.getReceivedDate());
//		target.setSpecimenCondition(source.getSpecimenCondition());
//		target.setNoTestPossibleReason(source.getNoTestPossibleReason());
//		target.setComment(source.getComment());
//		target.setSampleSource(source.getSampleSource());
//		target.setShipped(source.isShipped());
//		target.setReceived(source.isReceived());
//		target.setPathogenTestResult(source.getPathogenTestResult());
//		target.setPathogenTestingRequested(source.getPathogenTestingRequested());
//		target.setAdditionalTestingRequested(source.getAdditionalTestingRequested());
//		target.setRequestedPathogenTests(source.getRequestedPathogenTests());
//		target.setRequestedAdditionalTests(source.getRequestedAdditionalTests());
//		target.setRequestedOtherPathogenTests(source.getRequestedOtherPathogenTests());
//		target.setRequestedOtherAdditionalTests(source.getRequestedOtherAdditionalTests());
//
//		target.setReportLat(source.getReportLat());
//		target.setReportLon(source.getReportLon());
//		target.setReportLatLonAccuracy(source.getReportLatLonAccuracy());
//
//		if (source.getSormasToSormasOriginInfo() != null) {
//			target.setSormasToSormasOriginInfo(sormasToSormasOriginInfoDtoHelper.adoToDto(source.getSormasToSormasOriginInfo()));
//		}
//
//		target.setPseudonymized(source.isPseudonymized());
	}

	@Override
	protected long getApproximateJsonSizeInBytes() {
		return 0;
	}

	public static void logActivity (FragmentActivity fragmentActivity, HasUuid entity) {
		logActivity(fragmentActivity, ChangeType.VIEW, entity);
	}

	public static void logActivity (FragmentActivity fragmentActivity, ChangeType activityType, HasUuid entity) {
		RetroProvider.connectAsyncHandled(fragmentActivity, true, true, result -> {
			if (Boolean.TRUE.equals(result)) {
				logActivity_send(activityType, entity);
			} else {
				logActivity_save(activityType, entity);
			}
		});
	}

	private static void logActivity_send (ChangeType activityType, HasUuid entity) {
		try {
			RetroProvider.getAuditLogEntryFacade().logActivity(
				activityType.name(),
				AuditLogEntry.getEntityClazz(entity.getClass()),
				entity.getUuid()
			).enqueue(new Callback<String>() {
				@Override
				public void onResponse(Call<String> call, Response<String> response) {
					RetroProvider.disconnect();
				}

				@Override
				public void onFailure(Call<String> call, Throwable t) {
					RetroProvider.disconnect();
					logActivity_save(activityType, entity);
				}
			});
		} catch (NoConnectionException e) {
			RetroProvider.disconnect();
			logActivity_save(activityType, entity);
		}
	}

	private static void logActivity_save (ChangeType activityType, HasUuid entity) {
		try {
			AuditLogEntryDao dao = DatabaseHelper.getAuditLogEntryDao();

			AuditLogEntry entry = dao.build();
			entry.setChangeType(activityType);
			entry.setClazz(AuditLogEntry.getEntityClazz(entity.getClass()));
			entry.setUuid(entity.getUuid());

			dao.saveAndSnapshot(entry);
		} catch (Exception e) { }
	}
}
