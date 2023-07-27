package de.symeda.sormas.api.caze.caseimport;

import de.symeda.sormas.api.caze.CaseMDDataDto;

import java.io.Serializable;
import java.util.*;

public class CaseDMImportEntities implements Serializable {

    private static final long serialVersionUID = -3544971830146580773L;

    private  final CaseMDDataDto caseMDDataDto;
    private final List<CaseMDDataDto> caseMDDataDtoList ;

    private final LinkedHashMap<CaseMDDataDto, CaseMDDataDto> parentChildMap;
    private boolean headerRecordProcessed;
    private CaseMDDataDto lastProcessedItem;

    public CaseDMImportEntities(){
        caseMDDataDto = new CaseMDDataDto() ;
        caseMDDataDtoList= new ArrayList<>();
        parentChildMap =new LinkedHashMap<>();

    }

    public CaseMDDataDto getCaseMDDataDto() {
        return caseMDDataDto;
    }

    public List<CaseMDDataDto> getCaseMDDataDtoList() {
        return caseMDDataDtoList;
    }

    public LinkedHashMap<CaseMDDataDto, CaseMDDataDto> getParentChildMap() {
        return parentChildMap;
    }


    public boolean isHeaderRecordProcessed() {
        return headerRecordProcessed;
    }

    public void setHeaderRecordProcessed(boolean headerRecordProcessed) {
        this.headerRecordProcessed = headerRecordProcessed;
    }

    public CaseMDDataDto getLastProcessedItem() {
        return lastProcessedItem;
    }

    public void setLastProcessedItem(CaseMDDataDto lastProcessedItem) {
        this.lastProcessedItem = lastProcessedItem;
    }
}
