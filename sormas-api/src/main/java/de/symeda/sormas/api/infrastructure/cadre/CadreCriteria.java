package de.symeda.sormas.api.infrastructure.cadre;

import java.io.Serializable;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

public class CadreCriteria extends BaseCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = -3172115054516586926L;

	private EntityRelevanceStatus relevanceStatus;
	private String nameLike;

	public CadreCriteria relevanceStatus(EntityRelevanceStatus relevanceStatus) {
		this.relevanceStatus = relevanceStatus;
		return this;
	}

	@IgnoreForUrl
	public EntityRelevanceStatus getRelevanceStatus() {
		return relevanceStatus;
	}

	@IgnoreForUrl
	public String getNameLike() {
		return nameLike;
	}

	public CadreCriteria nameLike(String nameLike) {
		this.nameLike = nameLike;
		return this;
	}
}
