package de.symeda.sormas.api.caze.caseimport;

import de.symeda.sormas.api.caze.CaseDataDto;

import java.io.Serializable;
import java.util.Comparator;


public class CaseDtoComparator implements Comparator<CaseDataDto>, Serializable {

    @Override
    public int compare(CaseDataDto dto1, CaseDataDto dto2) {
        if (dto1 == null && dto2 == null) {
            return 0; // Both dtos are null, consider them equal
        }
        if (dto1 == null) {
            return -1; // dto1 is null, consider dto2 greater
        }
        if (dto2 == null) {
            return 1; // dto2 is null, consider dto1 greater
        }

        String caseId1 = dto1.getUuid();
        String caseId2 = dto2.getUuid();
        int value = caseId1.compareTo(caseId2);
        // Compare the caseId values directly
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }


}
