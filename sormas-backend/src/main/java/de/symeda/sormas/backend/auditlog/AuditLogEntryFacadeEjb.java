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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.auditlog.AuditLogEntryCriteria;
import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
import de.symeda.sormas.api.auditlog.AuditLogEntryFacade;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserFacadeEjb.UserFacadeEjbLocal;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.ModelConstants;

@Stateless(name = "AuditLogEntryFacade")
public class AuditLogEntryFacadeEjb implements AuditLogEntryFacade {

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME_AUDITLOG)
	private EntityManager em;
	
	@EJB
	private UserFacadeEjbLocal userFacade;
	
	@EJB
	private UserService userService;

	@Override
	public List<AuditLogEntryDto> getList(AuditLogEntryCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties) {

		if (sortProperties == null) {
			sortProperties = new ArrayList<SortProperty>();
			sortProperties.add(new SortProperty(AuditLogEntry.DETECTION_TIMESTAMP, false));
		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AuditLogEntryDto> cq = cb.createQuery(AuditLogEntryDto.class);
		Root<AuditLogEntry> entry = cq.from(AuditLogEntry.class);
		
		Predicate filter = cb.and();
		
		if (criteria.getClazz() != null) {
			filter = cb.and(filter, cb.equal(entry.get(AuditLogEntry.CLASS), AuditLogEntryDto.getEntityClazz(criteria.getClazz())));
		}
		
		if (criteria.getUuid() != null) {
			filter = cb.and(filter, cb.equal(entry.get(AuditLogEntry.UUID), criteria.getUuid()));
		}
		
		filter = cb.and(filter, entry.get(AuditLogEntry.CHANGE_TYPE).in(criteria.getChangeTypes()));
		
		cq.where(filter);
		
		List<Order> ordering = sortProperties
								.stream()
								.map((n) -> n.ascending ? cb.asc(entry.get(n.propertyName)) : cb.desc(entry.get(n.propertyName)))
								.collect(Collectors.toList());
		cq.orderBy(ordering);
		
		cq.multiselect(
			entry.get(AuditLogEntryDto.ID),
			entry.get(AuditLogEntryDto.CLASS),
			entry.get(AuditLogEntryDto.UUID),
			entry.get(AuditLogEntryDto.EDITING_USER_ID),
			entry.get(AuditLogEntryDto.TRANSACTION_ID),
			entry.get(AuditLogEntryDto.DETECTION_TIMESTAMP),
			entry.get(AuditLogEntryDto.CHANGE_TYPE)
		);
		
		TypedQuery<AuditLogEntryDto> query = em.createQuery(cq);
		
		if (first != null && max != null)
			query = query.setFirstResult(first).setMaxResults(max);
				
		List<AuditLogEntryDto> entries = query.getResultList();
		
		// todo: it is better to move this into the query
		if (criteria.showingDistinctActivitiesOnly().equals(YesNoUnknown.YES)) {
			entries = entries
					 .stream()
					 .filter(distinctByKeys(AuditLogEntryDto::getChangeType, AuditLogEntryDto::getEditingUserId))
					 .collect(Collectors.toList());		
		}
		
		// get users for entries
		// it's a workaround for postgres' error: cross-database references are not implemented: "sormas_db.public.users"
		{
			List<User> users = userFacade.getByIds(entries.stream().map(n -> n.getEditingUserId()).collect(Collectors.toList()));
			
			for (AuditLogEntryDto dto : entries) {
				User user = users.stream().filter(n -> n.getId().equals(dto.getEditingUserId())).findFirst().orElse(userFacade.getSystemUser());
				dto.setEditingUser(userFacade.toDto(user));
			}
		}
		
		return entries;
	}
	
	private static <T>  java.util.function.Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) 
	{
	    final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
	    
	    return t -> {
	      final List<?> keys = Arrays.stream(keyExtractors)
	                  .map(ke -> ke.apply(t))
	                  .collect(Collectors.toList());
	      
	      return seen.putIfAbsent(keys, Boolean.TRUE) == null;
	    };
	}
	
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
