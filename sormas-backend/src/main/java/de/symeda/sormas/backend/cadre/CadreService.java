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
package de.symeda.sormas.backend.cadre;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.infrastructure.cadre.CadreCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractCoreAdoService;
import de.symeda.sormas.backend.common.AbstractInfrastructureAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;

@Stateless
@LocalBean
public class CadreService extends AbstractCoreAdoService<Cadre> {

	public CadreService() {
		super(Cadre.class);
	}

	public List<Cadre> getByPosition(String name, boolean includeArchivedEntities) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cadre> cq = cb.createQuery(getElementClass());
		Root<Cadre> from = cq.from(getElementClass());

		Predicate filter = CriteriaBuilderHelper.unaccentedIlikePrecise(cb, from.get(Cadre.POSITION), name.trim());
//		if (!includeArchivedEntities) {
//			filter = cb.and(filter, createBasicFilter(cb, from));
//		}

		cq.where(filter);

		return em.createQuery(cq).getResultList();
	}

	public List<Cadre> getByExternalId(String externalId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cadre> cq = cb.createQuery(getElementClass());
		Root<Cadre> rootFrom = cq.from(getElementClass());


//		cq.where(cb.equal(rootFrom.get(Cadre.EXTERNAL_ID), externalId), cb.equal(rootFrom.get(Cadre.DELETED), Boolean.FALSE));
		cq.where(cb.equal(rootFrom.get(Cadre.EXTERNAL_ID), externalId));

		return em.createQuery(cq).getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, Cadre> from) {
		// no filter by user needed
		return null;
	}

//	@Override
	public Predicate buildCriteriaFilter(CadreCriteria criteria, CriteriaBuilder cb, Root<Cadre> from) {

		Predicate filter = null;
		if (criteria.getNameLike() != null) {
			String[] textFilters = criteria.getNameLike().split("\\s+");
			for (String textFilter : textFilters) {
				if (DataHelper.isNullOrEmpty(textFilter)) {
					continue;
				}

				Predicate likeFilters = cb.or(
					CriteriaBuilderHelper.unaccentedIlike(cb, from.get(Cadre.POSITION), textFilter));
				filter = CriteriaBuilderHelper.and(cb, filter, likeFilters);
			}
		}
		if (criteria.getRelevanceStatus() != null) {
			if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ACTIVE) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.or(cb.equal(from.get(Cadre.ARCHIVED), false), cb.isNull(from.get(Cadre.ARCHIVED))));
			} else if (criteria.getRelevanceStatus() == EntityRelevanceStatus.ARCHIVED) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(Cadre.ARCHIVED), true));
			}
		}
		return filter;
	}

	public void archiveDearchive(Cadre dearchive) {
		ensurePersisted(dearchive);
	}
}
