/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.backend.immunization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import de.symeda.sormas.api.immunization.ImmunizationCriteria;
import de.symeda.sormas.api.immunization.ImmunizationIndexDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.AbstractCoreAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.region.District;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.JurisdictionHelper;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless
@LocalBean
public class ImmunizationService extends AbstractCoreAdoService<Immunization> {

	@EJB
	private UserService userService;

	public ImmunizationService() {
		super(Immunization.class);
	}

	public List<ImmunizationIndexDto> getIndexList(ImmunizationCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<ImmunizationIndexDto> cq = cb.createQuery(ImmunizationIndexDto.class);
		final Root<Immunization> immunization = cq.from(Immunization.class);

		ImmunizationQueryContext<Immunization> immunizationQueryContext = new ImmunizationQueryContext<>(cb, cq, immunization);
		ImmunizationJoins<Immunization> joins = (ImmunizationJoins<Immunization>) immunizationQueryContext.getJoins();

		final Join<Immunization, Person> person = joins.getPerson();

		final Join<Person, Location> location = person.join(Person.ADDRESS, JoinType.LEFT);
		final Join<Location, District> district = location.join(Location.DISTRICT, JoinType.LEFT);

		cq.multiselect(
			immunization.get(Immunization.UUID),
			person.get(Person.UUID),
			person.get(Person.FIRST_NAME),
			person.get(Person.LAST_NAME),
			person.get(Person.APPROXIMATE_AGE),
			person.get(Person.APPROXIMATE_AGE_TYPE),
			person.get(Person.BIRTHDATE_DD),
			person.get(Person.BIRTHDATE_MM),
			person.get(Person.BIRTHDATE_YYYY),
			person.get(Person.SEX),
			district.get(District.NAME),
			immunization.get(Immunization.MEANS_OF_IMMUNIZATION),
			immunization.get(Immunization.IMMUNIZATION_MANAGEMENT_STATUS),
			immunization.get(Immunization.IMMUNIZATION_STATUS),
			immunization.get(Immunization.START_DATE),
			immunization.get(Immunization.END_DATE),
			immunization.get(Immunization.RECOVERY_DATE),
			immunization.get(Immunization.CHANGE_DATE),
			JurisdictionHelper.booleanSelector(cb, inJurisdictionOrOwned(immunizationQueryContext)));

		Predicate filter = createUserFilter(immunizationQueryContext);
		if (criteria != null) {
			final Predicate criteriaFilter = buildCriteriaFilter(criteria, immunizationQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		if (CollectionUtils.isNotEmpty(sortProperties)) {
			List<Order> order = new ArrayList<>(sortProperties.size());
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
				case ImmunizationIndexDto.UUID:
				case ImmunizationIndexDto.MEANS_OF_IMMUNIZATION:
				case ImmunizationIndexDto.MANAGEMENT_STATUS:
				case ImmunizationIndexDto.IMMUNIZATION_STATUS:
				case ImmunizationIndexDto.START_DATE:
				case ImmunizationIndexDto.END_DATE:
				case ImmunizationIndexDto.RECOVERY_DATE:
					expression = immunization.get(sortProperty.propertyName);
					break;
				case ImmunizationIndexDto.PERSON_ID:
					expression = person.get(Person.UUID);
					break;
				case ImmunizationIndexDto.PERSON_FIRST_NAME:
					expression = person.get(Person.FIRST_NAME);
					break;
				case ImmunizationIndexDto.PERSON_LAST_NAME:
					expression = person.get(Person.LAST_NAME);
					break;
				case ImmunizationIndexDto.DISTRICT:
					expression = district.get(District.NAME);
					break;
				default:
					throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
			}
			cq.orderBy(order);
		} else {
			cq.orderBy(cb.desc(immunization.get(Immunization.CHANGE_DATE)));
		}

		cq.distinct(true);

		return QueryHelper.getResultList(em, cq, first, max);
	}

	public long count(ImmunizationCriteria criteria) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		final Root<Immunization> immunization = cq.from(Immunization.class);

		ImmunizationQueryContext<Immunization> immunizationQueryContext = new ImmunizationQueryContext<>(cb, cq, immunization);

		Predicate filter = createUserFilter(immunizationQueryContext);
		if (criteria != null) {
			final Predicate criteriaFilter = buildCriteriaFilter(criteria, immunizationQueryContext);
			filter = CriteriaBuilderHelper.and(cb, filter, criteriaFilter);
		}

		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.countDistinct(immunization));
		return em.createQuery(cq).getSingleResult();
	}

	public boolean inJurisdictionOrOwned(Immunization immunization) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
		Root<Immunization> root = cq.from(Immunization.class);
		cq.multiselect(JurisdictionHelper.booleanSelector(cb, inJurisdictionOrOwned(new ImmunizationQueryContext<>(cb, cq, root))));
		cq.where(cb.equal(root.get(Immunization.UUID), immunization.getUuid()));
		return em.createQuery(cq).getSingleResult();
	}

	public Predicate inJurisdictionOrOwned(ImmunizationQueryContext<Immunization> qc) {
		final User currentUser = userService.getCurrentUser();
		return ImmunizationJurisdictionPredicateValidator.of(qc, currentUser).inJurisdictionOrOwned();
	}

	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, Immunization> immunizationPath) {
		return inJurisdictionOrOwned(new ImmunizationQueryContext(cb, cq, immunizationPath));
	}

	public Predicate createDefaultFilter(CriteriaBuilder cb, From<?, Immunization> root) {
		return cb.isFalse(root.get(Immunization.DELETED));
	}

	public Predicate buildCriteriaFilter(ImmunizationCriteria criteria, ImmunizationQueryContext<Immunization> immunizationQueryContext) {
		final ImmunizationJoins joins = (ImmunizationJoins) immunizationQueryContext.getJoins();
		final CriteriaBuilder cb = immunizationQueryContext.getCriteriaBuilder();
		final From<?, ?> from = immunizationQueryContext.getRoot();
		Join<Immunization, Person> person = joins.getPerson();

		Predicate filter = null;

		return filter;
	}

	public List<Immunization> getAllActiveAfter(Date date) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Immunization> cq = cb.createQuery(getElementClass());
		Root<Immunization> from = cq.from(getElementClass());

		Predicate filter = createDefaultFilter(cb, from);

		if (getCurrentUser() != null) {
			Predicate userFilter = createUserFilter(cb, cq, from);
			if (userFilter != null) {
				filter = cb.and(filter, userFilter);
			}
		}

		if (date != null) {
			Predicate dateFilter = createChangeDateFilter(cb, from, DateHelper.toTimestampUpper(date));
			if (dateFilter != null) {
				filter = cb.and(filter, dateFilter);
			}
		}

		cq.where(filter);
		cq.orderBy(cb.desc(from.get(Immunization.CHANGE_DATE)));
		cq.distinct(true);

		return em.createQuery(cq).getResultList();
	}

	public List<String> getArchivedUuidsSince(Date since) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Immunization> from = cq.from(getElementClass());

		Predicate filter = createUserFilter(cb, cq, from);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(from.get(Immunization.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate archivedFilter = cb.equal(from.get(Immunization.ARCHIVED), true);
		if (filter != null) {
			filter = cb.and(filter, archivedFilter);
		} else {
			filter = archivedFilter;
		}

		cq.where(filter);
		cq.select(from.get(Immunization.UUID));

		return em.createQuery(cq).getResultList();
	}

	public List<String> getDeletedUuidsSince(Date since) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<Immunization> from = cq.from(getElementClass());

		Predicate filter = createUserFilter(cb, cq, from);
		if (since != null) {
			Predicate dateFilter = cb.greaterThanOrEqualTo(from.get(Immunization.CHANGE_DATE), since);
			if (filter != null) {
				filter = cb.and(filter, dateFilter);
			} else {
				filter = dateFilter;
			}
		}

		Predicate deletedFilter = cb.equal(from.get(Immunization.DELETED), true);
		if (filter != null) {
			filter = cb.and(filter, deletedFilter);
		} else {
			filter = deletedFilter;
		}

		cq.where(filter);
		cq.select(from.get(Immunization.UUID));

		return em.createQuery(cq).getResultList();
	}

	private Predicate createUserFilter(ImmunizationQueryContext<Immunization> immunizationQueryContext) {
		return inJurisdictionOrOwned(immunizationQueryContext);
	}
}
