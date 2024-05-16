package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.backend.common.QueryContext;
import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.event.EventJoins;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public class EbsQueryContext extends QueryContext<Ebs, EbsJoins> {

	public EbsQueryContext(CriteriaBuilder cb, CriteriaQuery<?> query, From<?, Ebs> root) {
		this(cb, query, new EbsJoins(root));
	}

	public EbsQueryContext(CriteriaBuilder cb, CriteriaQuery<?> query, EbsJoins eventJoins) {
		super(cb, query, eventJoins.getRoot(), eventJoins);
	}

	@Override
	protected Expression<?> createExpression(String name) {
		return null;
	}
}
