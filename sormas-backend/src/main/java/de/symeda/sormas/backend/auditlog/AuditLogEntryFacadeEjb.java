/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.auditlog;

import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.symeda.sormas.api.auditlog.AuditLogEntryFacade;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.ModelConstants;

@Stateless(name = "AuditLogEntryFacade")
public class AuditLogEntryFacadeEjb implements AuditLogEntryFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME_AUDITLOG)
	private EntityManager em;
	
	@EJB
	private UserService userService;
	
	@Override
	public void logActivity(ChangeType changeType, String clazz, String uuid) {
		User user = userService.getCurrentUser();
		
		Date changeDate = AuditLogDateHelper.from(java.time.LocalDateTime.now());

		AuditLogEntry log = new AuditLogEntry();
		log.setAttributes(new HashMap<String, String>(0));
		log.setDetectionTimestamp(changeDate);
		log.setChangeType(changeType);
		log.setEditingUser(user.getUserName());
		log.setEditingUserId(user.getId());
		log.setTransactionId(null);
		log.setUuid(uuid);
		log.setClazz(clazz);

		this.em.persist(log);
	}

	
	@LocalBean
	@Stateless
	public static class AuditLogEntryFacadeEjbLocal extends AuditLogEntryFacadeEjb {

	}
}
