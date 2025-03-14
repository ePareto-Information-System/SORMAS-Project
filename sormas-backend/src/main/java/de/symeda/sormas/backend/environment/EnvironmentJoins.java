package de.symeda.sormas.backend.environment;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import de.symeda.sormas.backend.common.QueryJoins;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.location.LocationJoins;
import de.symeda.sormas.backend.user.User;

public class EnvironmentJoins extends QueryJoins<SignalVerification> {

	private Join<SignalVerification, Location> location;
	private Join<SignalVerification, User> responsibleUser;
	private LocationJoins locationJoins;

	public EnvironmentJoins(From<?, SignalVerification> root) {
		super(root);
	}

	public Join<SignalVerification, Location> getLocation() {
		return getOrCreate(location, SignalVerification.LOCATION, JoinType.LEFT, this::setLocation);
	}

	public void setLocation(Join<SignalVerification, Location> location) {
		this.location = location;
	}

	public LocationJoins getLocationJoins() {
		return getOrCreate(locationJoins, () -> new LocationJoins(getLocation()), this::setLocationJoins);
	}

	public void setLocationJoins(LocationJoins locationJoins) {
		this.locationJoins = locationJoins;
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

	public Join<SignalVerification, User> getResponsibleUser() {
		return getOrCreate(responsibleUser, SignalVerification.RESPONSIBLE_USER, JoinType.LEFT, this::setResponsibleUser);
	}

	private void setResponsibleUser(Join<SignalVerification, User> responsibleUser) {
		this.responsibleUser = responsibleUser;
	}
}
