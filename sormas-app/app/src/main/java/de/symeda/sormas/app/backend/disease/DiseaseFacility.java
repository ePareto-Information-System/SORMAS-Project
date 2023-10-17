package de.symeda.sormas.app.backend.disease;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Entity;

import de.symeda.sormas.app.backend.facility.Facility;

@Entity(name = DiseaseFacility.TABLE_NAME)
@DatabaseTable(tableName = DiseaseFacility.TABLE_NAME)
public class DiseaseFacility {

    public static final String TABLE_NAME = "facility_diseaseConfiguration";
    public static final String FACILITY = "facility";
    public static final String DISEASE_CONFIGURATION = "diseaseConfiguration";

    @DatabaseField(foreign = true, columnName = "facility_id")
    private Facility facility;

    @DatabaseField(foreign = true, columnName = "diseaseConfiguration_id")
    private DiseaseConfiguration diseaseConfiguration;

    //Needed for dto serialization
    public DiseaseFacility() {
    }

    public DiseaseFacility(DiseaseConfiguration diseaseConfiguration, Facility facility) {
        this.diseaseConfiguration = diseaseConfiguration;
        this.facility = facility;
    }

    public DiseaseConfiguration getDiseaseConfiguration() {
        return diseaseConfiguration;
    }

    public void setDiseaseConfiguration(DiseaseConfiguration diseaseConfiguration) {
        this.diseaseConfiguration = diseaseConfiguration;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}