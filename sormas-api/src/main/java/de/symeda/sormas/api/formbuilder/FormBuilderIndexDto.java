package de.symeda.sormas.api.formbuilder;

import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableIndexDto;

import java.io.Serializable;

public class FormBuilderIndexDto extends PseudonymizableIndexDto implements Serializable {

    private static final long serialVersionUID = 2439546041916003653L;

    public static final String I18N_PREFIX = "FormBuilder";

    public static final String REGION = "region";
    public static final String DISTRICT = "district";
    public static final String COMMUNITY = "community";
    public static final String FACILITY = "facility";


    private RegionReferenceDto region;
    private DistrictReferenceDto district;
    private CommunityReferenceDto community;

    public FormBuilderIndexDto() {

    }


    public RegionReferenceDto getRegion() {
        return region;
    }

    public void setRegion(RegionReferenceDto region) {
        this.region = region;
    }

    public DistrictReferenceDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictReferenceDto district) {
        this.district = district;
    }

    public CommunityReferenceDto getCommunity() {
        return community;
    }

    public void setCommunity(CommunityReferenceDto community) {
        this.community = community;
    }

}
