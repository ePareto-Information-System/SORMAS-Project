package de.symeda.sormas.backend.event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

import de.symeda.sormas.api.person.PersonContactDetailType;
import de.symeda.sormas.backend.common.QueryContext;
import de.symeda.sormas.backend.person.Person;

public class EventParticipantQueryContext extends QueryContext<EventParticipant, EventParticipantJoins> {

	public static final String PERSON_PHONE_SUBQUERY = "personPhoneSubquery";
	public static final String PERSON_PHONE_OWNER_SUBQUERY = "personPhoneOwnerSubquery";
	public static final String PERSON_EMAIL_SUBQUERY = "personEmailSubquery";
	public static final String PERSON_PRIMARY_OTHER_SUBQUERY = "personPrimaryOtherSubquery";
	public static final String PERSON_OTHER_CONTACT_DETAILS_SUBQUERY = "personOtherContactDetailsSubQuery";

	protected EventParticipantQueryContext(CriteriaBuilder cb, CriteriaQuery<?> query, From<?, EventParticipant> root) {
		this(cb, query, new EventParticipantJoins(root));
	}

	public EventParticipantQueryContext(CriteriaBuilder cb, CriteriaQuery<?> query, EventParticipantJoins joins) {
		super(cb, query, joins.getRoot(), joins);
	}

	@Override
	protected Expression<?> createExpression(String name) {
		final Join<EventParticipant, Person> personJoin = getJoins().getPerson();
		if (name.equals(PERSON_PHONE_SUBQUERY)) {
			return addSubqueryExpression(PERSON_PHONE_SUBQUERY, getPersonContactDetailSubquery(PersonContactDetailType.PHONE, personJoin));
		} else if (name.equals(PERSON_EMAIL_SUBQUERY)) {
			return addSubqueryExpression(PERSON_EMAIL_SUBQUERY, getPersonContactDetailSubquery(PersonContactDetailType.EMAIL, personJoin));
		} else if (name.equals(PERSON_PRIMARY_OTHER_SUBQUERY)) {
			return addSubqueryExpression(PERSON_PRIMARY_OTHER_SUBQUERY, getPersonContactDetailSubquery(PersonContactDetailType.OTHER, personJoin));
		} else if (name.equals(PERSON_PHONE_OWNER_SUBQUERY)) {
			return addSubqueryExpression(PERSON_PHONE_OWNER_SUBQUERY, phoneOwnerSubquery(personJoin));
		} else if (name.equals(PERSON_OTHER_CONTACT_DETAILS_SUBQUERY)) {
			return addSubqueryExpression(PERSON_OTHER_CONTACT_DETAILS_SUBQUERY, getPersonOtherContactDetailsSubQuery(personJoin));
		}
		throw new IllegalArgumentException("No such subquery expression defined!");
	}
}
