package de.symeda.sormas.api.caze.caseimport;

import de.symeda.sormas.api.importexport.ImportLineResult;
import de.symeda.sormas.api.importexport.ImportLineResultDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityImportResultDto implements Serializable {
    private static final long serialVersionUID = -3544971830146580773L;

    final private  ImportLineResultDto<CaseDMImportEntities> importEntitiesImportLineResultDto;

    final private  ImportLineResult importEntitiesImportLine;


    final private  CaseDMImportEntities caseDMImportEntities;



    public ImportLineResultDto<CaseDMImportEntities> getImportEntitiesImportLineResultDto() {
        return importEntitiesImportLineResultDto;
    }

    public CaseDMImportEntities getCaseDMImportEntities() {
        return caseDMImportEntities;
    }


    public EntityImportResultDto(ImportLineResultDto<CaseDMImportEntities> importEntitiesImportLineResultDto, ImportLineResult importEntitiesImportLine, CaseDMImportEntities caseDMImportEntities) {
        this.importEntitiesImportLineResultDto = importEntitiesImportLineResultDto;
        this.importEntitiesImportLine = importEntitiesImportLine;
        this.caseDMImportEntities = caseDMImportEntities;
    }

    public ImportLineResult getImportEntitiesImportLine() {
        return importEntitiesImportLine;
    }
}
