/*
 * ******************************************************************************
 * * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.symeda.sormas.api.riskfactor;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.TravelLocation;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

@DependingOnFeatureType(featureType = {
        FeatureType.CASE_SURVEILANCE,
        FeatureType.CONTACT_TRACING })
public class PatientTravelDetailsDuringDto extends PseudonymizableDto {

    public static final String I18N_PREFIX = "PatientTravelDetailsDuring";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "patienttraveldetailsduring";
    public static final String DATE_OF_TRAVEL = "dateOfTravel";
    public static final String PLACE_OF_TRAVEL = "placeOfTravel";
    private Date dateOfTravel;
    private TravelLocation placeOfTravel;
    public static PatientTravelDetailsDuringDto build() {
        PatientTravelDetailsDuringDto patientTravelDetailsDuringDto = new PatientTravelDetailsDuringDto();
        patientTravelDetailsDuringDto.setUuid(DataHelper.createUuid());
        return patientTravelDetailsDuringDto;
    }

    public static PatientTravelDetailsDuringDto build(
            String uuid,
            Date dateOfTravel,
            TravelLocation placeOfTravel) {
        PatientTravelDetailsDuringDto patientTravelDetailsDuringDto = new PatientTravelDetailsDuringDto();
        patientTravelDetailsDuringDto.setUuid(uuid);
        patientTravelDetailsDuringDto.setDateOfTravel(dateOfTravel);
        patientTravelDetailsDuringDto.setPlaceOfTravel(placeOfTravel);

        return patientTravelDetailsDuringDto;
    }

    @Override
    public String getUuid() {
        return super.getUuid();
    }
    @Override
    public void setUuid(String uuid) {
        super.setUuid(uuid);
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public TravelLocation getPlaceOfTravel() {
        return placeOfTravel;
    }

    public void setPlaceOfTravel(TravelLocation placeOfTravel) {
        this.placeOfTravel = placeOfTravel;
    }

    @Override
    public PatientTravelDetailsDuringDto clone() throws CloneNotSupportedException {
        PatientTravelDetailsDuringDto clone = (PatientTravelDetailsDuringDto) super.clone();
        return clone;
    }
}
