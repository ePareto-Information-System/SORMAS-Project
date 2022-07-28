/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.backend.sample;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseJoins;
import de.symeda.sormas.backend.common.QueryJoins;
import de.symeda.sormas.backend.contact.Contact;
import de.symeda.sormas.backend.contact.ContactJoins;
import de.symeda.sormas.backend.event.Event;
import de.symeda.sormas.backend.event.EventParticipant;
import de.symeda.sormas.backend.event.EventParticipantJoins;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.pointofentry.PointOfEntry;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.person.Person;
import de.symeda.sormas.backend.user.User;

public class SampleJoins extends QueryJoins<Sample> {

	private Join<Sample, User> reportingUser;
	private Join<Sample, Sample> referredSample;
	private Join<Sample, Facility> lab;
	private Join<Sample, Case> caze;
	private Join<Sample, EventParticipant> eventParticipant;
	private Join<Sample, Contact> contact;

	private CaseJoins caseJoins;
	private ContactJoins contactJoins;
	private EventParticipantJoins eventParticipantJoins;
	private Join<Case, Person> casePerson;
	private Join<Case, User> caseReportingUser;
	private Join<Case, Region> caseResponsibleRegion;
	private Join<Case, District> caseResponsibleDistrict;
	private Join<Case, Community> caseResponsibleCommunity;
	private Join<Case, Region> caseRegion;
	private Join<Case, District> caseDistrict;
	private Join<Case, Community> caseCommunity;
	private Join<Case, Facility> caseFacility;
	private Join<Case, PointOfEntry> casePointOfEntry;
	private Join<Case, User> contactCaseReportingUser;
	private Join<Case, Region> contactCaseResponsibleRegion;
	private Join<Case, District> contactCaseResponsibleDistrict;
	private Join<Case, Community> contactCaseResponsibleCommunity;
	private Join<Case, Region> contactCaseRegion;
	private Join<Case, District> contactCaseDistrict;
	private Join<Case, Community> contactCaseCommunity;
	private Join<Case, Facility> contactCaseHealthFacility;
	private Join<Case, PointOfEntry> contactCasePointOfEntry;
	private Join<Contact, Person> contactPerson;
	private Join<Contact, User> contactReportingUser;
	private Join<Contact, Region> contactRegion;
	private Join<Contact, District> contactDistrict;
	private Join<Contact, Community> contactCommunity;
	private Join<Contact, Case> contactCase;
	private Join<Person, Location> casePersonAddress;
	private Join<Person, Location> contactPersonAddress;
	private Join<Location, Region> casePersonAddressRegion;
	private Join<Location, District> casePersonAddressDistrict;
	private Join<Location, Community> casePersonAddressCommunity;
	private Join<Location, Region> contactPersonAddressRegion;
	private Join<Location, District> contactPersonAddressDistrict;
	private Join<Location, Community> contactPersonAddressCommunity;
	private Join<Location, Region> eventRegion;
	private Join<Location, District> eventDistrict;
	private Join<Location, Community> eventCommunity;
	private Join<EventParticipant, Person> eventParticipantPerson;
	private Join<EventParticipant, Event> event;
	private Join<Event, Location> eventLocation;
	private Join<Event, User> eventReportingUser;
	private Join<Event, User> eventResponsibleUser;
	private Join<Sample, PathogenTest> pathogenTests;

	public SampleJoins(From<?, Sample> root) {
		super(root);
	}

	public Join<Sample, PathogenTest> getPathogenTests() {
		return getOrCreate(pathogenTests, Sample.PATHOGENTESTS, JoinType.LEFT, this::setPathogenTests);
	}

	private void setPathogenTests(Join<Sample, PathogenTest> pathogenTests) {
		this.pathogenTests = pathogenTests;
	}

	public Join<Sample, User> getReportingUser() {
		return getOrCreate(reportingUser, Sample.REPORTING_USER, JoinType.LEFT, this::setReportingUser);
	}

	private void setReportingUser(Join<Sample, User> reportingUser) {
		this.reportingUser = reportingUser;
	}

	public Join<Sample, Sample> getReferredSample() {
		return getOrCreate(referredSample, Sample.REFERRED_TO, JoinType.LEFT, this::setReferredSample);
	}

	private void setReferredSample(Join<Sample, Sample> referredSample) {
		this.referredSample = referredSample;
	}

	public Join<Sample, Facility> getLab() {
		return getOrCreate(lab, Sample.LAB, JoinType.LEFT, this::setLab);
	}

	private void setLab(Join<Sample, Facility> lab) {
		this.lab = lab;
	}

	public Join<Sample, Case> getCaze() {
		return getOrCreate(caze, Sample.ASSOCIATED_CASE, JoinType.LEFT, this::setCaze);
	}

	private void setCaze(Join<Sample, Case> caze) {
		this.caze = caze;
	}

	public CaseJoins getCaseJoins() {
		return getOrCreate(caseJoins, () -> new CaseJoins(getCaze()), this::setCaseJoins);
	}

	private void setCaseJoins(CaseJoins caseJoins) {
		this.caseJoins = caseJoins;
	}

	public Join<Case, Person> getCasePerson() {
		return getCaseJoins().getPerson();
	}

	public Join<Case, User> getCaseReportingUser() {
		return getCaseJoins().getReportingUser();
	}

	public Join<Case, Region> getCaseResponsibleRegion() {
		return getCaseJoins().getResponsibleRegion();
	}

	public Join<Case, District> getCaseResponsibleDistrict() {
		return getCaseJoins().getResponsibleDistrict();
	}

	public Join<Case, Community> getCaseResponsibleCommunity() {
		return getCaseJoins().getResponsibleCommunity();
	}

	public Join<Case, Region> getCaseRegion() {
		return getCaseJoins().getRegion();
	}

	public Join<Case, District> getCaseDistrict() {
		return getCaseJoins().getDistrict();
	}

	public Join<Case, Community> getCaseCommunity() {
		return getCaseJoins().getCommunity();
	}

	public Join<Case, Facility> getCaseFacility() {
		return getCaseJoins().getFacility();
	}

	public Join<Case, PointOfEntry> getCasePointOfEntry() {
		return getCaseJoins().getPointOfEntry();
	}

	public Join<Sample, Contact> getContact() {
		return getOrCreate(contact, Sample.ASSOCIATED_CONTACT, JoinType.LEFT, this::setContact);
	}

	private void setContact(Join<Sample, Contact> contact) {
		this.contact = contact;
	}

	public ContactJoins getContactJoins() {
		return getOrCreate(contactJoins, () -> new ContactJoins(getContact()), this::setContactJoins);
	}

	public void setContactJoins(ContactJoins contactJoins) {
		this.contactJoins = contactJoins;
	}

	public Join<Sample, EventParticipant> getEventParticipant() {
		return getOrCreate(eventParticipant, Sample.ASSOCIATED_EVENT_PARTICIPANT, JoinType.LEFT, this::setEventParticipant);
	}

	private void setEventParticipant(Join<Sample, EventParticipant> eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

	public EventParticipantJoins getEventParticipantJoins() {
		return getOrCreate(eventParticipantJoins, () -> new EventParticipantJoins(getEventParticipant()), this::setEventParticipantJoins);
	}

	public void setEventParticipantJoins(EventParticipantJoins eventParticipantJoins) {
		this.eventParticipantJoins = eventParticipantJoins;
	}

	public Join<EventParticipant, Person> getEventParticipantPerson() {
		return getEventParticipantJoins().getPerson();
	}

	public Join<EventParticipant, Event> getEvent() {
		return getEventParticipantJoins().getEvent();
	}

	public Join<Event, Location> getEventLocation() {
		return getEventParticipantJoins().getEventJoins().getLocation();
	}

	public Join<Location, Region> getEventRegion() {
		return getEventParticipantJoins().getEventJoins().getRegion();
	}

	public Join<Location, District> getEventDistrict() {
		return getEventParticipantJoins().getEventJoins().getDistrict();
	}

	public Join<Location, Community> getEventCommunity() {
		return getEventParticipantJoins().getEventJoins().getCommunity();
	}

	public Join<Event, User> getEventReportingUser() {
		return getEventParticipantJoins().getEventJoins().getReportingUser();
	}

	public Join<Event, User> getEventResponsibleUser() {
		return getEventParticipantJoins().getEventJoins().getResponsibleUser();
	}

	public Join<Contact, Person> getContactPerson() {
		return getContactJoins().getPerson();
	}

	public Join<Contact, User> getContactReportingUser() {
		return getContactJoins().getReportingUser();
	}

	public Join<Contact, Region> getContactRegion() {
		return getContactJoins().getRegion();
	}

	public Join<Contact, District> getContactDistrict() {
		return getContactJoins().getDistrict();
	}

	public Join<Contact, Community> getContactCommunity() {
		return getContactJoins().getCommunity();
	}

	public Join<Contact, Case> getContactCase() {
		return getContactJoins().getCaze();
	}

	public Join<Case, User> getContactCaseReportingUser() {
		return getContactJoins().getCaseJoins().getReportingUser();
	}

	public Join<Case, Region> getContactCaseResponsibleRegion() {
		return getContactJoins().getCaseJoins().getResponsibleRegion();
	}

	public Join<Case, District> getContactCaseResponsibleDistrict() {
		return getContactJoins().getCaseJoins().getResponsibleDistrict();
	}

	public Join<Case, Community> getContactCaseResponsibleCommunity() {
		return getContactJoins().getCaseJoins().getResponsibleCommunity();
	}

	public Join<Case, Region> getContactCaseRegion() {
		return getContactJoins().getCaseJoins().getRegion();
	}

	public Join<Case, District> getContactCaseDistrict() {
		return getContactJoins().getCaseJoins().getDistrict();
	}

	public Join<Case, Community> getContactCaseCommunity() {
		return getContactJoins().getCaseJoins().getCommunity();
	}

	public Join<Case, Facility> getContactCaseHealthFacility() {
		return getContactJoins().getCaseJoins().getFacility();
	}

	public Join<Case, PointOfEntry> getContactCasePointOfEntry() {
		return getContactJoins().getCaseJoins().getPointOfEntry();
	}

	public Join<Person, Location> getCasePersonAddress() {
		return getCaseJoins().getPersonJoins().getAddress();
	}

	public Join<Location, Region> getCasePersonAddressRegion() {
		return getCaseJoins().getPersonJoins().getAddressJoins().getRegion();
	}

	public Join<Location, District> getCasePersonAddressDistrict() {
		return getCaseJoins().getPersonJoins().getAddressJoins().getDistrict();
	}

	public Join<Location, Community> getCasePersonAddressCommunity() {
		return getCaseJoins().getPersonJoins().getAddressJoins().getCommunity();
	}

	public Join<Person, Location> getContactPersonAddress() {
		return getContactJoins().getPersonJoins().getAddress();
	}

	public Join<Location, Region> getContactPersonAddressRegion() {
		return getContactJoins().getPersonJoins().getAddressJoins().getRegion();
	}

	public Join<Location, District> getContactPersonAddressDistrict() {
		return getContactJoins().getPersonJoins().getAddressJoins().getDistrict();
	}

	public Join<Location, Community> getContactPersonAddressCommunity() {
		return getContactJoins().getPersonJoins().getAddressJoins().getCommunity();
	}
}
