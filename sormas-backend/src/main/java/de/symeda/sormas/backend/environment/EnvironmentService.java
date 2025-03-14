package de.symeda.sormas.backend.environment;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.RequestContextHolder;
import de.symeda.sormas.api.environment.EnvironmentCriteria;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.AbstractCoreAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.common.DeletableAdo;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.user.User;

@Stateless
@LocalBean
public class EnvironmentService extends AbstractCoreAdoService<SignalVerification, EnvironmentJoins> {

	public EnvironmentService() {
		super(SignalVerification.class);
	}

	@Override
	protected Predicate createUserFilterInternal(CriteriaBuilder cb, CriteriaQuery cq, From<?, SignalVerification> from) {
		return createUserFilter(new EnvironmentQueryContext(cb, cq, from));
	}

	public Predicate createUserFilter(EnvironmentQueryContext queryContext) {

		User currentUser = getCurrentUser();
		if (currentUser == null) {
			return null;
		}
		final JurisdictionLevel jurisdictionLevel = currentUser.getJurisdictionLevel();
		Predicate filter = null;

		@SuppressWarnings("rawtypes")
		final CriteriaQuery cq = queryContext.getQuery();
		final CriteriaBuilder cb = queryContext.getCriteriaBuilder();
		final EnvironmentJoins environmentJoins = queryContext.getJoins();
		final From<?, SignalVerification> environmentJoin = queryContext.getRoot();

		if (jurisdictionLevel != JurisdictionLevel.NATION) {
			switch (jurisdictionLevel) {
			case REGION:
				if (currentUser.getRegion() != null) {
					filter =
						CriteriaBuilderHelper.or(cb, filter, cb.equal(environmentJoins.getLocation().get(Location.REGION), currentUser.getRegion()));
				}
				break;
			case DISTRICT:
				if (currentUser.getDistrict() != null) {
					filter = CriteriaBuilderHelper
						.or(cb, filter, cb.equal(environmentJoins.getLocation().get(Location.DISTRICT), currentUser.getDistrict()));
				}
				break;
			case COMMUNITY:
				if (currentUser.getCommunity() != null) {
					filter = CriteriaBuilderHelper
						.or(cb, filter, cb.equal(environmentJoins.getLocation().get(Location.COMMUNITY), currentUser.getCommunity()));
				}
				break;
			default:
			}

			Predicate filterResponsible = cb.equal(environmentJoins.getRoot().get(SignalVerification.REPORTING_USER), currentUser);
			filterResponsible = cb.or(filterResponsible, cb.equal(environmentJoins.getRoot().get(SignalVerification.RESPONSIBLE_USER), currentUser));

			if (filter != null) {
				filter = CriteriaBuilderHelper.or(cb, filter, filterResponsible);
			} else {
				filter = filterResponsible;
			}
		}

		if (RequestContextHolder.isMobileSync()) {
			Predicate limitedChangeDatePredicate = CriteriaBuilderHelper.and(cb, createLimitedChangeDateFilter(cb, environmentJoin));
			if (limitedChangeDatePredicate != null) {
				filter = CriteriaBuilderHelper.and(cb, filter, limitedChangeDatePredicate);
			}
		}

		return filter;
	}

	@Override
	protected EnvironmentJoins toJoins(From<?, SignalVerification> adoPath) {
		return new EnvironmentJoins(adoPath);
	}

	@Override
	public Predicate inJurisdictionOrOwned(CriteriaBuilder cb, CriteriaQuery<?> query, From<?, SignalVerification> from) {
		return cb.conjunction();
	}

	public Predicate buildCriteriaFilter(EnvironmentCriteria environmentCriteria, EnvironmentQueryContext environmentQueryContext) {

		CriteriaBuilder cb = environmentQueryContext.getCriteriaBuilder();
		From<?, SignalVerification> from = environmentQueryContext.getRoot();
		final EnvironmentJoins joins = environmentQueryContext.getJoins();

		Predicate filter = null;

		if (StringUtils.isNotEmpty(environmentCriteria.getFreeText())) {
			String[] textFilters = environmentCriteria.getFreeText().split("\\s+");
			for (String textFilter : textFilters) {
				if (DataHelper.isNullOrEmpty(textFilter)) {
					continue;
				}

				Predicate likeFilters = cb.or(
					CriteriaBuilderHelper.ilike(cb, from.get(SignalVerification.UUID), textFilter),
					CriteriaBuilderHelper.unaccentedIlike(cb, from.get(SignalVerification.EXTERNAL_ID), textFilter),
					CriteriaBuilderHelper.unaccentedIlike(cb, from.get(SignalVerification.ENVIRONMENT_NAME), textFilter),
					CriteriaBuilderHelper.unaccentedIlike(cb, from.get(SignalVerification.DESCRIPTION), textFilter));
				filter = CriteriaBuilderHelper.and(cb, filter, likeFilters);
			}
		}
		if (environmentCriteria.getRegion() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getRegion().get(Region.UUID), environmentCriteria.getRegion().getUuid()));
		}
		if (environmentCriteria.getDistrict() != null) {
			filter =
				CriteriaBuilderHelper.and(cb, filter, cb.equal(joins.getDistrict().get(District.UUID), environmentCriteria.getDistrict().getUuid()));
		}
		if (environmentCriteria.getCommunity() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.equal(joins.getCommunity().get(Community.UUID), environmentCriteria.getCommunity().getUuid()));
		}
		if (environmentCriteria.getReportDateFrom() != null && environmentCriteria.getReportDateTo() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.between(from.get(SignalVerification.REPORT_DATE), environmentCriteria.getReportDateFrom(), environmentCriteria.getReportDateTo()));
		} else if (environmentCriteria.getReportDateFrom() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.greaterThanOrEqualTo(from.get(SignalVerification.REPORT_DATE), environmentCriteria.getReportDateFrom()));
		} else if (environmentCriteria.getReportDateTo() != null) {
			filter =
				CriteriaBuilderHelper.and(cb, filter, cb.lessThanOrEqualTo(from.get(SignalVerification.REPORT_DATE), environmentCriteria.getReportDateTo()));
		}
		if (environmentCriteria.getInvestigationStatus() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.equal(from.get(SignalVerification.INVESTIGATION_STATUS), environmentCriteria.getInvestigationStatus()));
		}
		if (environmentCriteria.getEnvironmentMedia() != null) {
			filter =
				CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(SignalVerification.ENVIRONMENT_MEDIA), environmentCriteria.getEnvironmentMedia()));
		}
		if (environmentCriteria.getRelevanceStatus() != null) {
			if (environmentCriteria.getRelevanceStatus() == EntityRelevanceStatus.ACTIVE) {
				filter = CriteriaBuilderHelper
					.and(cb, filter, cb.or(cb.equal(from.get(SignalVerification.ARCHIVED), false), cb.isNull(from.get(SignalVerification.ARCHIVED))));
			} else if (environmentCriteria.getRelevanceStatus() == EntityRelevanceStatus.ARCHIVED) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(SignalVerification.ARCHIVED), true));
			} else if (environmentCriteria.getRelevanceStatus() == EntityRelevanceStatus.DELETED) {
				filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get(SignalVerification.DELETED), true));
			}
		}
		if (environmentCriteria.getRelevanceStatus() != EntityRelevanceStatus.DELETED) {
			filter = CriteriaBuilderHelper.and(cb, filter, createDefaultFilter(cb, from));
		}
		if (environmentCriteria.getResponsibleUser() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.equal(joins.getResponsibleUser().get(User.UUID), environmentCriteria.getResponsibleUser().getUuid()));
		}
		if (environmentCriteria.getGpsLatFrom() != null && environmentCriteria.getGpsLatTo() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.between(joins.getLocation().get(Location.LATITUDE), environmentCriteria.getGpsLatFrom(), environmentCriteria.getGpsLatTo()));
		} else if (environmentCriteria.getGpsLatFrom() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.greaterThanOrEqualTo(joins.getLocation().get(Location.LATITUDE), environmentCriteria.getGpsLatFrom()));
		} else if (environmentCriteria.getGpsLatTo() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.lessThanOrEqualTo(joins.getLocation().get(Location.LATITUDE), environmentCriteria.getGpsLatTo()));
		}
		if (environmentCriteria.getGpsLonFrom() != null && environmentCriteria.getGpsLonTo() != null) {
			filter = CriteriaBuilderHelper.and(
				cb,
				filter,
				cb.between(joins.getLocation().get(Location.LONGITUDE), environmentCriteria.getGpsLonFrom(), environmentCriteria.getGpsLonTo()));
		} else if (environmentCriteria.getGpsLonFrom() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.greaterThanOrEqualTo(joins.getLocation().get(Location.LONGITUDE), environmentCriteria.getGpsLonFrom()));
		} else if (environmentCriteria.getGpsLonTo() != null) {
			filter = CriteriaBuilderHelper
				.and(cb, filter, cb.lessThanOrEqualTo(joins.getLocation().get(Location.LONGITUDE), environmentCriteria.getGpsLonTo()));
		}

		return filter;
	}

	/**
	 * Creates a default filter that should be used as the basis of queries that do not use {@link EventCriteria}.
	 * This essentially removes {@link DeletableAdo#isDeleted()} events from the queries.
	 */
	public Predicate createDefaultFilter(CriteriaBuilder cb, From<?, SignalVerification> root) {
		return cb.isFalse(root.get(SignalVerification.DELETED));
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate createRelevantDataFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, SignalVerification> from) {

		Predicate filter = createDefaultFilter(cb, from);
		if (getCurrentUser() != null) {
			filter = CriteriaBuilderHelper.and(cb, filter, createUserFilterInternal(cb, cq, from));
		}

		return filter;
	}
}
