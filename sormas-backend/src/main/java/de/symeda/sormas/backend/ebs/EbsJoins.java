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

package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.backend.common.QueryJoins;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.location.LocationJoins;
import de.symeda.sormas.backend.user.User;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class EbsJoins extends QueryJoins<Ebs> {

	private Join<Ebs, User> reportingUser;
	private Join<Ebs, User> responsibleUser;
	private Join<Ebs, Location> location;

	private LocationJoins locationJoins;

	private Join<Ebs, Triaging> triaging;
	private Join<Ebs, SignalVerification> signalVerification;
	private Join<Ebs, RiskAssessment> riskAssessment;
	private Join<Ebs, EbsAlert> ebsAlert;

	public EbsJoins(From<?, Ebs> event) {
		super(event);
	}

	public Join<Ebs, User> getReportingUser() {
		return getOrCreate(reportingUser, Ebs.REPORTING_USER, JoinType.LEFT, this::setReportingUser);
	}

	private void setReportingUser(Join<Ebs, User> reportingUser) {
		this.reportingUser = reportingUser;
	}

	public Join<Ebs, User> getResponsibleUser() {
		return getOrCreate(responsibleUser, Ebs.RESPONSIBLE_USER, JoinType.LEFT, this::setResponsibleUser);
	}

	private void setResponsibleUser(Join<Ebs, User> responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public Join<Ebs, Location> getLocation() {
		return getOrCreate(location, Ebs.EBS_LOCATION, JoinType.LEFT, this::setLocation);
	}

	private void setLocation(Join<Ebs, Location> location) {
		this.location = location;
	}

	public Join<Location, Region> getRegion() {
		return getLocationJoins().getRegion();
	}

	public Join<Location, District> getDistrict() {
		return getLocationJoins().getDistrict();
	}

	public Join<Location, Community> getCommunity() {
		return getLocationJoins().getCommunity();
	}

	public Join<Location, Facility> getFacility() {
		return getLocationJoins().getFacility();
	}

	public LocationJoins getLocationJoins() {
		return getOrCreate(locationJoins, () -> new LocationJoins(getLocation()), this::setLocationJoins);
	}

	private void setLocationJoins(LocationJoins locationJoins) {
		this.locationJoins = locationJoins;
	}

	public Join<Ebs, Triaging> getTriaging() {
		return getOrCreate(triaging, Ebs.TRIAGING, JoinType.LEFT, this::setTriaging);
	}

	public void setTriaging(Join<Ebs, Triaging> triaging) {
		this.triaging = triaging;
	}

	public Join<Ebs, SignalVerification> getSignalVerification() {
		return getOrCreate(signalVerification, Ebs.SIGNAL_VERIFICATION, JoinType.LEFT, this::setSignalVerification);
	}

	public void setSignalVerification(Join<Ebs, SignalVerification> signalVerification) {
		this.signalVerification = signalVerification;
	}

	public Join<Ebs, RiskAssessment> getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(Join<Ebs, RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

	public Join<Ebs, EbsAlert> getEbsAlert() {
		return ebsAlert;
	}

	public void setEbsAlert(Join<Ebs, EbsAlert> ebsAlert) {
		this.ebsAlert = ebsAlert;
	}
}
