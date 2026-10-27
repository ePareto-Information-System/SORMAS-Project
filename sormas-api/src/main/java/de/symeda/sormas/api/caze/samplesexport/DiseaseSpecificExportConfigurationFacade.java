package de.symeda.sormas.api.caze.samplesexport;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import de.symeda.sormas.api.Disease;

@Remote
public interface DiseaseSpecificExportConfigurationFacade {

	List<DiseaseSpecificExportFieldDto> getFields(Disease disease);

	void saveActiveFields(Disease disease, Set<String> activePropertyPaths);

	byte[] exportFields(Disease disease);

	void importFields(Disease disease, byte[] csvContent);
}
