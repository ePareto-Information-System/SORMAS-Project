package de.symeda.sormas.api.caze.caseimport;

import de.symeda.sormas.api.caze.CaseMDDataDto;

import java.io.Serializable;
import java.util.Comparator;

public  class CaseMDDataDtoComparator implements Comparator<CaseMDDataDto>, Serializable {
    private static final long serialVersionUID = -3544971830146580773L;

    @Override
    public int compare(CaseMDDataDto item1, CaseMDDataDto item2) {
        if (item1 == null && item2 == null) {
            return 0; // Both items are null, consider them equal
        }
        if (item1 == null) {
            return -1; // item1 is null, consider item2 greater
        }
        if (item2 == null) {
            return 1; // item2 is null, consider item1 greater
        }

        String caseId1 = item1.getCaseId();
        String caseId2 = item2.getCaseId();

        if (caseId1 == null && caseId2 == null) {
            return 0; // Both caseIds are null, consider them equal
        }
        if (caseId1 == null) {
            return -1; // caseId1 is null, consider caseId2 greater
        }
        if (caseId2 == null) {
            return 1; // caseId2 is null, consider caseId1 greater
        }

        return caseId1.compareTo(caseId2);
    }

}