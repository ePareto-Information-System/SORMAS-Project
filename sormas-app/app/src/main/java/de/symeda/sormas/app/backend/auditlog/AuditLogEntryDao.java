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

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.auditlog.AuditLogEntry;
import de.symeda.sormas.app.backend.sample.Sample;

/**
 * Created by Mate Strysewske on 06.02.2017.
 */

public class AuditLogEntryDao extends AbstractAdoDao<AuditLogEntry> {

	public AuditLogEntryDao(Dao<AuditLogEntry, Long> innerDao) throws SQLException {
		super(innerDao);
	}

	@Override
	protected Class<AuditLogEntry> getAdoClass() {
		return AuditLogEntry.class;
	}

	@Override
	public AuditLogEntry build() {
		AuditLogEntry sample = super.build();
		sample.setDetectionTimestamp(new Date());
		sample.setEditingUserId(ConfigProvider.getUser().getId());

		return sample;
	}

	@Override
	public String getTableName() {
		return AuditLogEntry.TABLE_NAME;
	}

	@Override
	public AuditLogEntry saveAndSnapshot(AuditLogEntry sample) throws DaoException {
		return super.saveAndSnapshot(sample);
	}
}
