package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.Pseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.EmptyValuePseudonymizer;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.*;

public class CaseSampleExportDto implements Serializable {

    private static final long serialVersionUID = -3027326087594387560L;

    public static final String I18N_PREFIX = "SampleExport";

    private String uuid;
    private String labSampleID;
    private Date sampleReportDate;
    private Date sampleDateTime;
    @EmbeddedPersonalData
    @EmbeddedSensitiveData
    @Pseudonymizer(EmptyValuePseudonymizer.class)
    private SampleExportMaterial sampleSampleExportMaterial;
    private String samplePurpose;
    private SampleSource sampleSource;
    private SamplingReason samplingReason;
    private String samplingReasonDetails;
    private String lab;
    private PathogenTestResultType pathogenTestResult;
    private Boolean pathogenTestingRequested;
    private Set<PathogenTestType> requestedPathogenTests;
    private String requestedOtherPathogenTests;
    private Boolean additionalTestingRequested;
    private Set<AdditionalTestType> requestedAdditionalTests;
    private String requestedOtherAdditionalTests;
    private boolean shipped;
    private Date shipmentDate;
    @SensitiveData
    private String shipmentDetails;
    private boolean received;
    private Date receivedDate;
    private SpecimenCondition specimenCondition;
    @SensitiveData
    private String noTestPossibleReason;
    @SensitiveData
    private String comment;
    @EmbeddedPersonalData
    @EmbeddedSensitiveData
    private SampleExportPathogenTest pathogenTest1 = new SampleExportPathogenTest();
    @EmbeddedPersonalData
    @EmbeddedSensitiveData
    private SampleExportPathogenTest pathogenTest2 = new SampleExportPathogenTest();
    @EmbeddedPersonalData
    @EmbeddedSensitiveData
    private SampleExportPathogenTest pathogenTest3 = new SampleExportPathogenTest();
    private List<SampleExportPathogenTest> otherPathogenTests = new ArrayList<>();
    private AdditionalTestDto additionalTest;
    private String otherAdditionalTestsDetails = "";
    private Long caseId;
    private String afpAntiBodyDetection;
    private String afpAntigenDetection;
    private String afpRapidTest;
    private String afpCulture;
    private String afpHistopathology;
    private String afpIsolation;
    private String afpIgmSerumAntibody;
    private String afpIggSerumAntibody;
    private String afpIgaSerumAntibody;
    private String afpIncubationTime;
    private String afpIndirectFluorescentAntibody;
    private String afpMicroscopy;
    private String afpNeutralizingAntibodies;
    private String afpPcr;
    private String afpGramStain;
    private String afpLatexAgglutination;
    private String afpCqValueDetection;
    private String afpSeQuencing;
    private String afpDnaMicroArray;
    private String afpOther;

    private String choleraAntiBodyDetection;
    private String choleraAntigenDetection;
    private String choleraRapidTest;
    private String choleraCulture;
    private String choleraHistopathology;
    private String choleraIsolation;
    private String choleraIgmSerumAntibody;
    private String choleraIggSerumAntibody;
    private String choleraIgaSerumAntibody;
    private String choleraIncubationTime;
    private String choleraIndirectFluorescentAntibody;
    private String choleraMicroscopy;
    private String choleraNeutralizingAntibodies;
    private String choleraPcr;
    private String choleraGramStain;
    private String choleraLatexAgglutination;
    private String choleraCqValueDetection;
    private String choleraSeQuencing;
    private String choleraDnaMicroArray;
    private String choleraOther;

    private String congenitalRubellaAntiBodyDetection;
    private String congenitalRubellaAntigenDetection;
    private String congenitalRubellaRapidTest;
    private String congenitalRubellaCulture;
    private String congenitalRubellaHistopathology;
    private String congenitalRubellaIsolation;
    private String congenitalRubellaIgmSerumAntibody;
    private String congenitalRubellaIggSerumAntibody;
    private String congenitalRubellaIgaSerumAntibody;
    private String congenitalRubellaIncubationTime;
    private String congenitalRubellaIndirectFluorescentAntibody;
    private String congenitalRubellaMicroscopy;
    private String congenitalRubellaNeutralizingAntibodies;
    private String congenitalRubellaPcr;
    private String congenitalRubellaGramStain;
    private String congenitalRubellaLatexAgglutination;
    private String congenitalRubellaCqValueDetection;
    private String congenitalRubellaSeQuencing;
    private String congenitalRubellaDnaMicroArray;
    private String congenitalRubellaOther;

    private String csmAntiBodyDetection;
    private String csmAntigenDetection;
    private String csmRapidTest;
    private String csmCulture;
    private String csmHistopathology;
    private String csmIsolation;
    private String csmIgmSerumAntibody;
    private String csmIggSerumAntibody;
    private String csmIgaSerumAntibody;
    private String csmIncubationTime;
    private String csmIndirectFluorescentAntibody;
    private String csmMicroscopy;
    private String csmNeutralizingAntibodies;
    private String csmPcr;
    private String csmGramStain;
    private String csmLatexAgglutination;
    private String csmCqValueDetection;
    private String csmSeQuencing;
    private String csmDnaMicroArray;
    private String csmOther;

    private String dengueAntiBodyDetection;
    private String dengueAntigenDetection;
    private String dengueRapidTest;
    private String dengueCulture;
    private String dengueHistopathology;
    private String dengueIsolation;
    private String dengueIgmSerumAntibody;
    private String dengueIggSerumAntibody;
    private String dengueIgaSerumAntibody;
    private String dengueIncubationTime;
    private String dengueIndirectFluorescentAntibody;
    private String dengueMicroscopy;
    private String dengueNeutralizingAntibodies;
    private String denguePcr;
    private String dengueGramStain;
    private String dengueLatexAgglutination;
    private String dengueCqValueDetection;
    private String dengueSeQuencing;
    private String dengueDnaMicroArray;
    private String dengueOther;

    private String evdAntiBodyDetection;
    private String evdAntigenDetection;
    private String evdRapidTest;
    private String evdCulture;
    private String evdHistopathology;
    private String evdIsolation;
    private String evdIgmSerumAntibody;
    private String evdIggSerumAntibody;
    private String evdIgaSerumAntibody;
    private String evdIncubationTime;
    private String evdIndirectFluorescentAntibody;
    private String evdMicroscopy;
    private String evdNeutralizingAntibodies;
    private String evdPcr;
    private String evdGramStain;
    private String evdLatexAgglutination;
    private String evdCqValueDetection;
    private String evdSeQuencing;
    private String evdDnaMicroArray;
    private String evdOther;
    private String guineaWormAntiBodyDetection;
    private String guineaWormAntigenDetection;
    private String guineaWormRapidTest;
    private String guineaWormCulture;
    private String guineaWormHistopathology;
    private String guineaWormIsolation;
    private String guineaWormIgmSerumAntibody;
    private String guineaWormIggSerumAntibody;
    private String guineaWormIgaSerumAntibody;
    private String guineaWormIncubationTime;
    private String guineaWormIndirectFluorescentAntibody;
    private String guineaWormMicroscopy;
    private String guineaWormNeutralizingAntibodies;
    private String guineaWormPcr;
    private String guineaWormGramStain;
    private String guineaWormLatexAgglutination;
    private String guineaWormCqValueDetection;
    private String guineaWormSeQuencing;
    private String guineaWormDnaMicroArray;
    private String guineaWormOther;
    private String lassaAntiBodyDetection;
    private String lassaAntigenDetection;
    private String lassaRapidTest;
    private String lassaCulture;
    private String lassaHistopathology;
    private String lassaIsolation;
    private String lassaIgmSerumAntibody;
    private String lassaIggSerumAntibody;
    private String lassaIgaSerumAntibody;
    private String lassaIncubationTime;
    private String lassaIndirectFluorescentAntibody;
    private String lassaMicroscopy;
    private String lassaNeutralizingAntibodies;
    private String lassaPcr;
    private String lassaGramStain;
    private String lassaLatexAgglutination;
    private String lassaCqValueDetection;
    private String lassaSeQuencing;
    private String lassaDnaMicroArray;
    private String lassaOther;
    private String measlesAntiBodyDetection;
    private String measlesAntigenDetection;
    private String measlesRapidTest;
    private String measlesCulture;
    private String measlesHistopathology;
    private String measlesIsolation;
    private String measlesIgmSerumAntibody;
    private String measlesIggSerumAntibody;
    private String measlesIgaSerumAntibody;
    private String measlesIncubationTime;
    private String measlesIndirectFluorescentAntibody;
    private String measlesMicroscopy;
    private String measlesNeutralizingAntibodies;
    private String measlesPcr;
    private String measlesGramStain;
    private String measlesLatexAgglutination;
    private String measlesCqValueDetection;
    private String measlesSeQuencing;
    private String measlesDnaMicroArray;
    private String measlesOther;
    private String monkeyPoxAntiBodyDetection;
    private String monkeyPoxAntigenDetection;
    private String monkeyPoxRapidTest;
    private String monkeyPoxCulture;
    private String monkeyPoxHistopathology;
    private String monkeyPoxIsolation;
    private String monkeyPoxIgmSerumAntibody;
    private String monkeyPoxIggSerumAntibody;
    private String monkeyPoxIgaSerumAntibody;
    private String monkeyPoxIncubationTime;
    private String monkeyPoxIndirectFluorescentAntibody;
    private String monkeyPoxMicroscopy;
    private String monkeyPoxNeutralizingAntibodies;
    private String monkeyPoxPcr;
    private String monkeyPoxGramStain;
    private String monkeyPoxLatexAgglutination;
    private String monkeyPoxCqValueDetection;
    private String monkeyPoxSeQuencing;
    private String monkeyPoxDnaMicroArray;
    private String monkeyPoxOther;
    private String monkeypoxAntiBodyDetection;
    private String monkeypoxAntigenDetection;
    private String monkeypoxRapidTest;
    private String monkeypoxCulture;
    private String monkeypoxHistopathology;
    private String monkeypoxIsolation;
    private String monkeypoxIgmSerumAntibody;
    private String monkeypoxIggSerumAntibody;
    private String monkeypoxIgaSerumAntibody;
    private String monkeypoxIncubationTime;
    private String monkeypoxIndirectFluorescentAntibody;
    private String monkeypoxMicroscopy;
    private String monkeypoxNeutralizingAntibodies;
    private String monkeypoxPcr;
    private String monkeypoxGramStain;
    private String monkeypoxLatexAgglutination;
    private String monkeypoxCqValueDetection;
    private String monkeypoxSeQuencing;
    private String monkeypoxDnaMicroArray;
    private String monkeypoxOther;
    private String newInfluenzaAntiBodyDetection;
    private String newInfluenzaAntigenDetection;
    private String newInfluenzaRapidTest;
    private String newInfluenzaCulture;
    private String newInfluenzaHistopathology;
    private String newInfluenzaIsolation;
    private String newInfluenzaIgmSerumAntibody;
    private String newInfluenzaIggSerumAntibody;
    private String newInfluenzaIgaSerumAntibody;
    private String newInfluenzaIncubationTime;
    private String newInfluenzaIndirectFluorescentAntibody;
    private String newInfluenzaMicroscopy;
    private String newInfluenzaNeutralizingAntibodies;
    private String newInfluenzaPcr;
    private String newInfluenzaGramStain;
    private String newInfluenzaLatexAgglutination;
    private String newInfluenzaCqValueDetection;
    private String newInfluenzaSeQuencing;
    private String newInfluenzaDnaMicroArray;
    private String newInfluenzaOther;
    private String plagueAntiBodyDetection;
    private String plagueAntigenDetection;
    private String plagueRapidTest;
    private String plagueCulture;
    private String plagueHistopathology;
    private String plagueIsolation;
    private String plagueIgmSerumAntibody;
    private String plagueIggSerumAntibody;
    private String plagueIgaSerumAntibody;
    private String plagueIncubationTime;
    private String plagueIndirectFluorescentAntibody;
    private String plagueMicroscopy;
    private String plagueNeutralizingAntibodies;
    private String plaguePcr;
    private String plagueGramStain;
    private String plagueLatexAgglutination;
    private String plagueCqValueDetection;
    private String plagueSeQuencing;
    private String plagueDnaMicroArray;
    private String plagueOther;
    private String polioAntiBodyDetection;
    private String polioAntigenDetection;
    private String polioRapidTest;
    private String polioCulture;
    private String polioHistopathology;
    private String polioIsolation;
    private String polioIgmSerumAntibody;
    private String polioIggSerumAntibody;
    private String polioIgaSerumAntibody;
    private String polioIncubationTime;
    private String polioIndirectFluorescentAntibody;
    private String polioMicroscopy;
    private String polioNeutralizingAntibodies;
    private String polioPcr;
    private String polioGramStain;
    private String polioLatexAgglutination;
    private String polioCqValueDetection;
    private String polioSeQuencing;
    private String polioDnaMicroArray;
    private String polioOther;
    private String unspecifiedVhfAntiBodyDetection;
    private String unspecifiedVhfAntigenDetection;
    private String unspecifiedVhfRapidTest;
    private String unspecifiedVhfCulture;
    private String unspecifiedVhfHistopathology;
    private String unspecifiedVhfIsolation;
    private String unspecifiedVhfIgmSerumAntibody;
    private String unspecifiedVhfIggSerumAntibody;
    private String unspecifiedVhfIgaSerumAntibody;
    private String unspecifiedVhfIncubationTime;
    private String unspecifiedVhfIndirectFluorescentAntibody;
    private String unspecifiedVhfMicroscopy;
    private String unspecifiedVhfNeutralizingAntibodies;
    private String unspecifiedVhfPcr;
    private String unspecifiedVhfGramStain;
    private String unspecifiedVhfLatexAgglutination;
    private String unspecifiedVhfCqValueDetection;
    private String unspecifiedVhfSeQuencing;
    private String unspecifiedVhfDnaMicroArray;
    private String unspecifiedVhfOther;
    private String yellowFeverAntiBodyDetection;
    private String yellowFeverAntigenDetection;
    private String yellowFeverRapidTest;
    private String yellowFeverCulture;
    private String yellowFeverHistopathology;
    private String yellowFeverIsolation;
    private String yellowFeverIgmSerumAntibody;
    private String yellowFeverIggSerumAntibody;
    private String yellowFeverIgaSerumAntibody;
    private String yellowFeverIncubationTime;
    private String yellowFeverIndirectFluorescentAntibody;
    private String yellowFeverMicroscopy;
    private String yellowFeverNeutralizingAntibodies;
    private String yellowFeverPcr;
    private String yellowFeverGramStain;
    private String yellowFeverLatexAgglutination;
    private String yellowFeverCqValueDetection;
    private String yellowFeverSeQuencing;
    private String yellowFeverDnaMicroArray;
    private String yellowFeverOther;
    private String rabiesAntiBodyDetection;
    private String rabiesAntigenDetection;
    private String rabiesRapidTest;
    private String rabiesCulture;
    private String rabiesHistopathology;
    private String rabiesIsolation;
    private String rabiesIgmSerumAntibody;
    private String rabiesIggSerumAntibody;
    private String rabiesIgaSerumAntibody;
    private String rabiesIncubationTime;
    private String rabiesIndirectFluorescentAntibody;
    private String rabiesMicroscopy;
    private String rabiesNeutralizingAntibodies;
    private String rabiesPcr;
    private String rabiesGramStain;
    private String rabiesLatexAgglutination;
    private String rabiesCqValueDetection;
    private String rabiesSeQuencing;
    private String rabiesDnaMicroArray;
    private String rabiesOther;
    private String anthraxAntiBodyDetection;
    private String anthraxAntigenDetection;
    private String anthraxRapidTest;
    private String anthraxCulture;
    private String anthraxHistopathology;
    private String anthraxIsolation;
    private String anthraxIgmSerumAntibody;
    private String anthraxIggSerumAntibody;
    private String anthraxIgaSerumAntibody;
    private String anthraxIncubationTime;
    private String anthraxIndirectFluorescentAntibody;
    private String anthraxMicroscopy;
    private String anthraxNeutralizingAntibodies;
    private String anthraxPcr;
    private String anthraxGramStain;
    private String anthraxLatexAgglutination;
    private String anthraxCqValueDetection;
    private String anthraxSeQuencing;
    private String anthraxDnaMicroArray;
    private String anthraxOther;
    private String coronavirusAntiBodyDetection;
    private String coronavirusAntigenDetection;
    private String coronavirusRapidTest;
    private String coronavirusCulture;
    private String coronavirusHistopathology;
    private String coronavirusIsolation;
    private String coronavirusIgmSerumAntibody;
    private String coronavirusIggSerumAntibody;
    private String coronavirusIgaSerumAntibody;
    private String coronavirusIncubationTime;
    private String coronavirusIndirectFluorescentAntibody;
    private String coronavirusMicroscopy;
    private String coronavirusNeutralizingAntibodies;
    private String coronavirusPcr;
    private String coronavirusGramStain;
    private String coronavirusLatexAgglutination;
    private String coronavirusCqValueDetection;
    private String coronavirusSeQuencing;
    private String coronavirusDnaMicroArray;
    private String coronavirusOther;
    private String pneumoniaAntiBodyDetection;
    private String pneumoniaAntigenDetection;
    private String pneumoniaRapidTest;
    private String pneumoniaCulture;
    private String pneumoniaHistopathology;
    private String pneumoniaIsolation;
    private String pneumoniaIgmSerumAntibody;
    private String pneumoniaIggSerumAntibody;
    private String pneumoniaIgaSerumAntibody;
    private String pneumoniaIncubationTime;
    private String pneumoniaIndirectFluorescentAntibody;
    private String pneumoniaMicroscopy;
    private String pneumoniaNeutralizingAntibodies;
    private String pneumoniaPcr;
    private String pneumoniaGramStain;
    private String pneumoniaLatexAgglutination;
    private String pneumoniaCqValueDetection;
    private String pneumoniaSeQuencing;
    private String pneumoniaDnaMicroArray;
    private String pneumoniaOther;
    private String malariaAntiBodyDetection;
    private String malariaAntigenDetection;
    private String malariaRapidTest;
    private String malariaCulture;
    private String malariaHistopathology;
    private String malariaIsolation;
    private String malariaIgmSerumAntibody;
    private String malariaIggSerumAntibody;
    private String malariaIgaSerumAntibody;
    private String malariaIncubationTime;
    private String malariaIndirectFluorescentAntibody;
    private String malariaMicroscopy;
    private String malariaNeutralizingAntibodies;
    private String malariaPcr;
    private String malariaGramStain;
    private String malariaLatexAgglutination;
    private String malariaCqValueDetection;
    private String malariaSeQuencing;
    private String malariaDnaMicroArray;
    private String malariaOther;
    private String typhoidFeverAntiBodyDetection;
    private String typhoidFeverAntigenDetection;
    private String typhoidFeverRapidTest;
    private String typhoidFeverCulture;
    private String typhoidFeverHistopathology;
    private String typhoidFeverIsolation;
    private String typhoidFeverIgmSerumAntibody;
    private String typhoidFeverIggSerumAntibody;
    private String typhoidFeverIgaSerumAntibody;
    private String typhoidFeverIncubationTime;
    private String typhoidFeverIndirectFluorescentAntibody;
    private String typhoidFeverMicroscopy;
    private String typhoidFeverNeutralizingAntibodies;
    private String typhoidFeverPcr;
    private String typhoidFeverGramStain;
    private String typhoidFeverLatexAgglutination;
    private String typhoidFeverCqValueDetection;
    private String typhoidFeverSeQuencing;
    private String typhoidFeverDnaMicroArray;
    private String typhoidFeverOther;
    private String acuteViralHepatitisAntiBodyDetection;
    private String acuteViralHepatitisAntigenDetection;
    private String acuteViralHepatitisRapidTest;
    private String acuteViralHepatitisCulture;
    private String acuteViralHepatitisHistopathology;
    private String acuteViralHepatitisIsolation;
    private String acuteViralHepatitisIgmSerumAntibody;
    private String acuteViralHepatitisIggSerumAntibody;
    private String acuteViralHepatitisIgaSerumAntibody;
    private String acuteViralHepatitisIncubationTime;
    private String acuteViralHepatitisIndirectFluorescentAntibody;
    private String acuteViralHepatitisMicroscopy;
    private String acuteViralHepatitisNeutralizingAntibodies;
    private String acuteViralHepatitisPcr;
    private String acuteViralHepatitisGramStain;
    private String acuteViralHepatitisLatexAgglutination;
    private String acuteViralHepatitisCqValueDetection;
    private String acuteViralHepatitisSeQuencing;
    private String acuteViralHepatitisDnaMicroArray;
    private String acuteViralHepatitisOther;
//    NON_NEONATAL_TETANUS
    private String nonNeonatalTetanusAntiBodyDetection;
    private String nonNeonatalTetanusAntigenDetection;
    private String nonNeonatalTetanusRapidTest;
    private String nonNeonatalTetanusCulture;
    private String nonNeonatalTetanusHistopathology;
    private String nonNeonatalTetanusIsolation;
    private String nonNeonatalTetanusIgmSerumAntibody;
    private String nonNeonatalTetanusIggSerumAntibody;
    private String nonNeonatalTetanusIgaSerumAntibody;
    private String nonNeonatalTetanusIncubationTime;
    private String nonNeonatalTetanusIndirectFluorescentAntibody;
    private String nonNeonatalTetanusMicroscopy;
    private String nonNeonatalTetanusNeutralizingAntibodies;
    private String nonNeonatalTetanusPcr;
    private String nonNeonatalTetanusGramStain;
    private String nonNeonatalTetanusLatexAgglutination;
    private String nonNeonatalTetanusCqValueDetection;
    private String nonNeonatalTetanusSeQuencing;
    private String nonNeonatalTetanusDnaMicroArray;
    private String nonNeonatalTetanusOther;
    private String hivAntiBodyDetection;
    private String hivAntigenDetection;
    private String hivRapidTest;
    private String hivCulture;
    private String hivHistopathology;
    private String hivIsolation;
    private String hivIgmSerumAntibody;
    private String hivIggSerumAntibody;
    private String hivIgaSerumAntibody;
    private String hivIncubationTime;
    private String hivIndirectFluorescentAntibody;
    private String hivMicroscopy;
    private String hivNeutralizingAntibodies;
    private String hivPcr;
    private String hivGramStain;
    private String hivLatexAgglutination;
    private String hivCqValueDetection;
    private String hivSeQuencing;
    private String hivDnaMicroArray;
    private String hivOther;
    private String schistosomiasisAntiBodyDetection;
    private String schistosomiasisAntigenDetection;
    private String schistosomiasisRapidTest;
    private String schistosomiasisCulture;
    private String schistosomiasisHistopathology;
    private String schistosomiasisIsolation;
    private String schistosomiasisIgmSerumAntibody;
    private String schistosomiasisIggSerumAntibody;
    private String schistosomiasisIgaSerumAntibody;
    private String schistosomiasisIncubationTime;
    private String schistosomiasisIndirectFluorescentAntibody;
    private String schistosomiasisMicroscopy;
    private String schistosomiasisNeutralizingAntibodies;
    private String schistosomiasisPcr;
    private String schistosomiasisGramStain;
    private String schistosomiasisLatexAgglutination;
    private String schistosomiasisCqValueDetection;
    private String schistosomiasisSeQuencing;
    private String schistosomiasisDnaMicroArray;
    private String schistosomiasisOther;
    private String soilTransmittedHelminthsAntiBodyDetection;
    private String soilTransmittedHelminthsAntigenDetection;
    private String soilTransmittedHelminthsRapidTest;
    private String soilTransmittedHelminthsCulture;
    private String soilTransmittedHelminthsHistopathology;
    private String soilTransmittedHelminthsIsolation;
    private String soilTransmittedHelminthsIgmSerumAntibody;
    private String soilTransmittedHelminthsIggSerumAntibody;
    private String soilTransmittedHelminthsIgaSerumAntibody;
    private String soilTransmittedHelminthsIncubationTime;
    private String soilTransmittedHelminthsIndirectFluorescentAntibody;
    private String soilTransmittedHelminthsMicroscopy;
    private String soilTransmittedHelminthsNeutralizingAntibodies;
    private String soilTransmittedHelminthsPcr;
    private String soilTransmittedHelminthsGramStain;
    private String soilTransmittedHelminthsLatexAgglutination;
    private String soilTransmittedHelminthsCqValueDetection;
    private String soilTransmittedHelminthsSeQuencing;
    private String soilTransmittedHelminthsDnaMicroArray;
    private String soilTransmittedHelminthsOther;
    private String trypanosomiasisAntiBodyDetection;
    private String trypanosomiasisAntigenDetection;
    private String trypanosomiasisRapidTest;
    private String trypanosomiasisCulture;
    private String trypanosomiasisHistopathology;
    private String trypanosomiasisIsolation;
    private String trypanosomiasisIgmSerumAntibody;
    private String trypanosomiasisIggSerumAntibody;
    private String trypanosomiasisIgaSerumAntibody;
    private String trypanosomiasisIncubationTime;
    private String trypanosomiasisIndirectFluorescentAntibody;
    private String trypanosomiasisMicroscopy;
    private String trypanosomiasisNeutralizingAntibodies;
    private String trypanosomiasisPcr;
    private String trypanosomiasisGramStain;
    private String trypanosomiasisLatexAgglutination;
    private String trypanosomiasisCqValueDetection;
    private String trypanosomiasisSeQuencing;
    private String trypanosomiasisDnaMicroArray;
    private String trypanosomiasisOther;
    private String diarrheaDehydrationAntiBodyDetection;
    private String diarrheaDehydrationAntigenDetection;
    private String diarrheaDehydrationRapidTest;
    private String diarrheaDehydrationCulture;
    private String diarrheaDehydrationHistopathology;
    private String diarrheaDehydrationIsolation;
    private String diarrheaDehydrationIgmSerumAntibody;
    private String diarrheaDehydrationIggSerumAntibody;
    private String diarrheaDehydrationIgaSerumAntibody;
    private String diarrheaDehydrationIncubationTime;
    private String diarrheaDehydrationIndirectFluorescentAntibody;
    private String diarrheaDehydrationMicroscopy;
    private String diarrheaDehydrationNeutralizingAntibodies;
    private String diarrheaDehydrationPcr;
    private String diarrheaDehydrationGramStain;
    private String diarrheaDehydrationLatexAgglutination;
    private String diarrheaDehydrationCqValueDetection;
    private String diarrheaDehydrationSeQuencing;
    private String diarrheaDehydrationDnaMicroArray;
    private String diarrheaDehydrationOther;
    private String diarrheaBloodAntiBodyDetection;
    private String diarrheaBloodAntigenDetection;
    private String diarrheaBloodRapidTest;
    private String diarrheaBloodCulture;
    private String diarrheaBloodHistopathology;
    private String diarrheaBloodIsolation;
    private String diarrheaBloodIgmSerumAntibody;
    private String diarrheaBloodIggSerumAntibody;
    private String diarrheaBloodIgaSerumAntibody;
    private String diarrheaBloodIncubationTime;
    private String diarrheaBloodIndirectFluorescentAntibody;
    private String diarrheaBloodMicroscopy;
    private String diarrheaBloodNeutralizingAntibodies;
    private String diarrheaBloodPcr;
    private String diarrheaBloodGramStain;
    private String diarrheaBloodLatexAgglutination;
    private String diarrheaBloodCqValueDetection;
    private String diarrheaBloodSeQuencing;
    private String diarrheaBloodDnaMicroArray;
    private String diarrheaBloodOther;
    private String snakeBiteAntiBodyDetection;
    private String snakeBiteAntigenDetection;
    private String snakeBiteRapidTest;
    private String snakeBiteCulture;
    private String snakeBiteHistopathology;
    private String snakeBiteIsolation;
    private String snakeBiteIgmSerumAntibody;
    private String snakeBiteIggSerumAntibody;
    private String snakeBiteIgaSerumAntibody;
    private String snakeBiteIncubationTime;
    private String snakeBiteIndirectFluorescentAntibody;
    private String snakeBiteMicroscopy;
    private String snakeBiteNeutralizingAntibodies;
    private String snakeBitePcr;
    private String snakeBiteGramStain;
    private String snakeBiteLatexAgglutination;
    private String snakeBiteCqValueDetection;
    private String snakeBiteSeQuencing;
    private String snakeBiteDnaMicroArray;
    private String snakeBiteOther;
    private String rubellaAntiBodyDetection;
    private String rubellaAntigenDetection;
    private String rubellaRapidTest;
    private String rubellaCulture;
    private String rubellaHistopathology;
    private String rubellaIsolation;
    private String rubellaIgmSerumAntibody;
    private String rubellaIggSerumAntibody;
    private String rubellaIgaSerumAntibody;
    private String rubellaIncubationTime;
    private String rubellaIndirectFluorescentAntibody;
    private String rubellaMicroscopy;
    private String rubellaNeutralizingAntibodies;
    private String rubellaPcr;
    private String rubellaGramStain;
    private String rubellaLatexAgglutination;
    private String rubellaCqValueDetection;
    private String rubellaSeQuencing;
    private String rubellaDnaMicroArray;
    private String rubellaOther;
    private String tuberculosisAntiBodyDetection;
    private String tuberculosisAntigenDetection;
    private String tuberculosisRapidTest;
    private String tuberculosisCulture;
    private String tuberculosisHistopathology;
    private String tuberculosisIsolation;
    private String tuberculosisIgmSerumAntibody;
    private String tuberculosisIggSerumAntibody;
    private String tuberculosisIgaSerumAntibody;
    private String tuberculosisIncubationTime;
    private String tuberculosisIndirectFluorescentAntibody;
    private String tuberculosisMicroscopy;
    private String tuberculosisNeutralizingAntibodies;
    private String tuberculosisPcr;
    private String tuberculosisGramStain;
    private String tuberculosisLatexAgglutination;
    private String tuberculosisCqValueDetection;
    private String tuberculosisSeQuencing;
    private String tuberculosisDnaMicroArray;
    private String tuberculosisOther;
    private String leprosyAntiBodyDetection;
    private String leprosyAntigenDetection;
    private String leprosyRapidTest;
    private String leprosyCulture;
    private String leprosyHistopathology;
    private String leprosyIsolation;
    private String leprosyIgmSerumAntibody;
    private String leprosyIggSerumAntibody;
    private String leprosyIgaSerumAntibody;
    private String leprosyIncubationTime;
    private String leprosyIndirectFluorescentAntibody;
    private String leprosyMicroscopy;
    private String leprosyNeutralizingAntibodies;
    private String leprosyPcr;
    private String leprosyGramStain;
    private String leprosyLatexAgglutination;
    private String leprosyCqValueDetection;
    private String leprosySeQuencing;
    private String leprosyDnaMicroArray;
    private String leprosyOther;
    private String lymphaticFilariasisAntiBodyDetection;
    private String lymphaticFilariasisAntigenDetection;
    private String lymphaticFilariasisRapidTest;
    private String lymphaticFilariasisCulture;
    private String lymphaticFilariasisHistopathology;
    private String lymphaticFilariasisIsolation;
    private String lymphaticFilariasisIgmSerumAntibody;
    private String lymphaticFilariasisIggSerumAntibody;
    private String lymphaticFilariasisIgaSerumAntibody;
    private String lymphaticFilariasisIncubationTime;
    private String lymphaticFilariasisIndirectFluorescentAntibody;
    private String lymphaticFilariasisMicroscopy;
    private String lymphaticFilariasisNeutralizingAntibodies;
    private String lymphaticFilariasisPcr;
    private String lymphaticFilariasisGramStain;
    private String lymphaticFilariasisLatexAgglutination;
    private String lymphaticFilariasisCqValueDetection;
    private String lymphaticFilariasisSeQuencing;
    private String lymphaticFilariasisDnaMicroArray;
    private String lymphaticFilariasisOther;
    private String buruliUlcerAntiBodyDetection;
    private String buruliUlcerAntigenDetection;
    private String buruliUlcerRapidTest;
    private String buruliUlcerCulture;
    private String buruliUlcerHistopathology;
    private String buruliUlcerIsolation;
    private String buruliUlcerIgmSerumAntibody;
    private String buruliUlcerIggSerumAntibody;
    private String buruliUlcerIgaSerumAntibody;
    private String buruliUlcerIncubationTime;
    private String buruliUlcerIndirectFluorescentAntibody;
    private String buruliUlcerMicroscopy;
    private String buruliUlcerNeutralizingAntibodies;
    private String buruliUlcerPcr;
    private String buruliUlcerGramStain;
    private String buruliUlcerLatexAgglutination;
    private String buruliUlcerCqValueDetection;
    private String buruliUlcerSeQuencing;
    private String buruliUlcerDnaMicroArray;
    private String buruliUlcerOther;
    private String pertussisAntiBodyDetection;
    private String pertussisAntigenDetection;
    private String pertussisRapidTest;
    private String pertussisCulture;
    private String pertussisHistopathology;
    private String pertussisIsolation;
    private String pertussisIgmSerumAntibody;
    private String pertussisIggSerumAntibody;
    private String pertussisIgaSerumAntibody;
    private String pertussisIncubationTime;
    private String pertussisIndirectFluorescentAntibody;
    private String pertussisMicroscopy;
    private String pertussisNeutralizingAntibodies;
    private String pertussisPcr;
    private String pertussisGramStain;
    private String pertussisLatexAgglutination;
    private String pertussisCqValueDetection;
    private String pertussisSeQuencing;
    private String pertussisDnaMicroArray;
    private String pertussisOther;
    private String neonatalAntiBodyDetection;
    private String neonatalAntigenDetection;
    private String neonatalRapidTest;
    private String neonatalCulture;
    private String neonatalHistopathology;
    private String neonatalIsolation;
    private String neonatalIgmSerumAntibody;
    private String neonatalIggSerumAntibody;
    private String neonatalIgaSerumAntibody;
    private String neonatalIncubationTime;
    private String neonatalIndirectFluorescentAntibody;
    private String neonatalMicroscopy;
    private String neonatalNeutralizingAntibodies;
    private String neonatalPcr;
    private String neonatalGramStain;
    private String neonatalLatexAgglutination;
    private String neonatalCqValueDetection;
    private String neonatalSeQuencing;
    private String neonatalDnaMicroArray;
    private String neonatalOther;
    private String onchocerciasisAntiBodyDetection;
    private String onchocerciasisAntigenDetection;
    private String onchocerciasisRapidTest;
    private String onchocerciasisCulture;
    private String onchocerciasisHistopathology;
    private String onchocerciasisIsolation;
    private String onchocerciasisIgmSerumAntibody;
    private String onchocerciasisIggSerumAntibody;
    private String onchocerciasisIgaSerumAntibody;
    private String onchocerciasisIncubationTime;
    private String onchocerciasisIndirectFluorescentAntibody;
    private String onchocerciasisMicroscopy;
    private String onchocerciasisNeutralizingAntibodies;
    private String onchocerciasisPcr;
    private String onchocerciasisGramStain;
    private String onchocerciasisLatexAgglutination;
    private String onchocerciasisCqValueDetection;
    private String onchocerciasisSeQuencing;
    private String onchocerciasisDnaMicroArray;
    private String onchocerciasisOther;
    private String diphtheriaAntiBodyDetection;
    private String diphtheriaAntigenDetection;
    private String diphtheriaRapidTest;
    private String diphtheriaCulture;
    private String diphtheriaHistopathology;
    private String diphtheriaIsolation;
    private String diphtheriaIgmSerumAntibody;
    private String diphtheriaIggSerumAntibody;
    private String diphtheriaIgaSerumAntibody;
    private String diphtheriaIncubationTime;
    private String diphtheriaIndirectFluorescentAntibody;
    private String diphtheriaMicroscopy;
    private String diphtheriaNeutralizingAntibodies;
    private String diphtheriaPcr;
    private String diphtheriaGramStain;
    private String diphtheriaLatexAgglutination;
    private String diphtheriaCqValueDetection;
    private String diphtheriaSeQuencing;
    private String diphtheriaDnaMicroArray;
    private String diphtheriaOther;
    private String trachomaAntiBodyDetection;
    private String trachomaAntigenDetection;
    private String trachomaRapidTest;
    private String trachomaCulture;
    private String trachomaHistopathology;
    private String trachomaIsolation;
    private String trachomaIgmSerumAntibody;
    private String trachomaIggSerumAntibody;
    private String trachomaIgaSerumAntibody;
    private String trachomaIncubationTime;
    private String trachomaIndirectFluorescentAntibody;
    private String trachomaMicroscopy;
    private String trachomaNeutralizingAntibodies;
    private String trachomaPcr;
    private String trachomaGramStain;
    private String trachomaLatexAgglutination;
    private String trachomaCqValueDetection;
    private String trachomaSeQuencing;
    private String trachomaDnaMicroArray;
    private String trachomaOther;
    private String yawsEndemicSyphilisAntiBodyDetection;
    private String yawsEndemicSyphilisAntigenDetection;
    private String yawsEndemicSyphilisRapidTest;
    private String yawsEndemicSyphilisCulture;
    private String yawsEndemicSyphilisHistopathology;
    private String yawsEndemicSyphilisIsolation;
    private String yawsEndemicSyphilisIgmSerumAntibody;
    private String yawsEndemicSyphilisIggSerumAntibody;
    private String yawsEndemicSyphilisIgaSerumAntibody;
    private String yawsEndemicSyphilisIncubationTime;
    private String yawsEndemicSyphilisIndirectFluorescentAntibody;
    private String yawsEndemicSyphilisMicroscopy;
    private String yawsEndemicSyphilisNeutralizingAntibodies;
    private String yawsEndemicSyphilisPcr;
    private String yawsEndemicSyphilisGramStain;
    private String yawsEndemicSyphilisLatexAgglutination;
    private String yawsEndemicSyphilisCqValueDetection;
    private String yawsEndemicSyphilisSeQuencing;
    private String yawsEndemicSyphilisDnaMicroArray;
    private String yawsEndemicSyphilisOther;
    private String maternalDeathsAntiBodyDetection;
    private String maternalDeathsAntigenDetection;
    private String maternalDeathsRapidTest;
    private String maternalDeathsCulture;
    private String maternalDeathsHistopathology;
    private String maternalDeathsIsolation;
    private String maternalDeathsIgmSerumAntibody;
    private String maternalDeathsIggSerumAntibody;
    private String maternalDeathsIgaSerumAntibody;
    private String maternalDeathsIncubationTime;
    private String maternalDeathsIndirectFluorescentAntibody;
    private String maternalDeathsMicroscopy;
    private String maternalDeathsNeutralizingAntibodies;
    private String maternalDeathsPcr;
    private String maternalDeathsGramStain;
    private String maternalDeathsLatexAgglutination;
    private String maternalDeathsCqValueDetection;
    private String maternalDeathsSeQuencing;
    private String maternalDeathsDnaMicroArray;
    private String maternalDeathsOther;
    private String perinatalDeathsAntiBodyDetection;
    private String perinatalDeathsAntigenDetection;
    private String perinatalDeathsRapidTest;
    private String perinatalDeathsCulture;
    private String perinatalDeathsHistopathology;
    private String perinatalDeathsIsolation;
    private String perinatalDeathsIgmSerumAntibody;
    private String perinatalDeathsIggSerumAntibody;
    private String perinatalDeathsIgaSerumAntibody;
    private String perinatalDeathsIncubationTime;
    private String perinatalDeathsIndirectFluorescentAntibody;
    private String perinatalDeathsMicroscopy;
    private String perinatalDeathsNeutralizingAntibodies;
    private String perinatalDeathsPcr;
    private String perinatalDeathsGramStain;
    private String perinatalDeathsLatexAgglutination;
    private String perinatalDeathsCqValueDetection;
    private String perinatalDeathsSeQuencing;
    private String perinatalDeathsDnaMicroArray;
    private String perinatalDeathsOther;
    private String influenzaAAntiBodyDetection;
    private String influenzaAAntigenDetection;
    private String influenzaARapidTest;
    private String influenzaACulture;
    private String influenzaAHistopathology;
    private String influenzaAIsolation;
    private String influenzaAIgmSerumAntibody;
    private String influenzaAIggSerumAntibody;
    private String influenzaAIgaSerumAntibody;
    private String influenzaAIncubationTime;
    private String influenzaAIndirectFluorescentAntibody;
    private String influenzaAMicroscopy;
    private String influenzaANeutralizingAntibodies;
    private String influenzaAPcr;
    private String influenzaAGramStain;
    private String influenzaALatexAgglutination;
    private String influenzaACqValueDetection;
    private String influenzaASeQuencing;
    private String influenzaADnaMicroArray;
    private String influenzaAOther;
    private String influenzaBAntiBodyDetection;
    private String influenzaBAntigenDetection;
    private String influenzaBRapidTest;
    private String influenzaBCulture;
    private String influenzaBHistopathology;
    private String influenzaBIsolation;
    private String influenzaBIgmSerumAntibody;
    private String influenzaBIggSerumAntibody;
    private String influenzaBIgaSerumAntibody;
    private String influenzaBIncubationTime;
    private String influenzaBIndirectFluorescentAntibody;
    private String influenzaBMicroscopy;
    private String influenzaBNeutralizingAntibodies;
    private String influenzaBPcr;
    private String influenzaBGramStain;
    private String influenzaBLatexAgglutination;
    private String influenzaBCqValueDetection;
    private String influenzaBSeQuencing;
    private String influenzaBDnaMicroArray;
    private String influenzaBOther;
    private String hMetapneumovirusAntiBodyDetection;
    private String hMetapneumovirusAntigenDetection;
    private String hMetapneumovirusRapidTest;
    private String hMetapneumovirusCulture;
    private String hMetapneumovirusHistopathology;
    private String hMetapneumovirusIsolation;
    private String hMetapneumovirusIgmSerumAntibody;
    private String hMetapneumovirusIggSerumAntibody;
    private String hMetapneumovirusIgaSerumAntibody;
    private String hMetapneumovirusIncubationTime;
    private String hMetapneumovirusIndirectFluorescentAntibody;
    private String hMetapneumovirusMicroscopy;
    private String hMetapneumovirusNeutralizingAntibodies;
    private String hMetapneumovirusPcr;
    private String hMetapneumovirusGramStain;
    private String hMetapneumovirusLatexAgglutination;
    private String hMetapneumovirusCqValueDetection;
    private String hMetapneumovirusSeQuencing;
    private String hMetapneumovirusDnaMicroArray;
    private String hMetapneumovirusOther;
    private String respiratorySyncytialVirusAntiBodyDetection;
    private String respiratorySyncytialVirusAntigenDetection;
    private String respiratorySyncytialVirusRapidTest;
    private String respiratorySyncytialVirusCulture;
    private String respiratorySyncytialVirusHistopathology;
    private String respiratorySyncytialVirusIsolation;
    private String respiratorySyncytialVirusIgmSerumAntibody;
    private String respiratorySyncytialVirusIggSerumAntibody;
    private String respiratorySyncytialVirusIgaSerumAntibody;
    private String respiratorySyncytialVirusIncubationTime;
    private String respiratorySyncytialVirusIndirectFluorescentAntibody;
    private String respiratorySyncytialVirusMicroscopy;
    private String respiratorySyncytialVirusNeutralizingAntibodies;
    private String respiratorySyncytialVirusPcr;
    private String respiratorySyncytialVirusGramStain;
    private String respiratorySyncytialVirusLatexAgglutination;
    private String respiratorySyncytialVirusCqValueDetection;
    private String respiratorySyncytialVirusSeQuencing;
    private String respiratorySyncytialVirusDnaMicroArray;
    private String respiratorySyncytialVirusOther;
    private String parainfluenza1_4AntiBodyDetection;
    private String parainfluenza1_4AntigenDetection;
    private String parainfluenza1_4RapidTest;
    private String parainfluenza1_4Culture;
    private String parainfluenza1_4Histopathology;
    private String parainfluenza1_4Isolation;
    private String parainfluenza1_4IgmSerumAntibody;
    private String parainfluenza1_4IggSerumAntibody;
    private String parainfluenza1_4IgaSerumAntibody;
    private String parainfluenza1_4IncubationTime;
    private String parainfluenza1_4IndirectFluorescentAntibody;
    private String parainfluenza1_4Microscopy;
    private String parainfluenza1_4NeutralizingAntibodies;
    private String parainfluenza1_4Pcr;
    private String parainfluenza1_4GramStain;
    private String parainfluenza1_4LatexAgglutination;
    private String parainfluenza1_4CqValueDetection;
    private String parainfluenza1_4SeQuencing;
    private String parainfluenza1_4DnaMicroArray;
    private String parainfluenza1_4Other;
    private String adenovirusAntiBodyDetection;
    private String adenovirusAntigenDetection;
    private String adenovirusRapidTest;
    private String adenovirusCulture;
    private String adenovirusHistopathology;
    private String adenovirusIsolation;
    private String adenovirusIgmSerumAntibody;
    private String adenovirusIggSerumAntibody;
    private String adenovirusIgaSerumAntibody;
    private String adenovirusIncubationTime;
    private String adenovirusIndirectFluorescentAntibody;
    private String adenovirusMicroscopy;
    private String adenovirusNeutralizingAntibodies;
    private String adenovirusPcr;
    private String adenovirusGramStain;
    private String adenovirusLatexAgglutination;
    private String adenovirusCqValueDetection;
    private String adenovirusSeQuencing;
    private String adenovirusDnaMicroArray;
    private String adenovirusOther;
    private String rhinovirusAntiBodyDetection;
    private String rhinovirusAntigenDetection;
    private String rhinovirusRapidTest;
    private String rhinovirusCulture;
    private String rhinovirusHistopathology;
    private String rhinovirusIsolation;
    private String rhinovirusIgmSerumAntibody;
    private String rhinovirusIggSerumAntibody;
    private String rhinovirusIgaSerumAntibody;
    private String rhinovirusIncubationTime;
    private String rhinovirusIndirectFluorescentAntibody;
    private String rhinovirusMicroscopy;
    private String rhinovirusNeutralizingAntibodies;
    private String rhinovirusPcr;
    private String rhinovirusGramStain;
    private String rhinovirusLatexAgglutination;
    private String rhinovirusCqValueDetection;
    private String rhinovirusSeQuencing;
    private String rhinovirusDnaMicroArray;
    private String rhinovirusOther;
    private String enterovirusAntiBodyDetection;
    private String enterovirusAntigenDetection;
    private String enterovirusRapidTest;
    private String enterovirusCulture;
    private String enterovirusHistopathology;
    private String enterovirusIsolation;
    private String enterovirusIgmSerumAntibody;
    private String enterovirusIggSerumAntibody;
    private String enterovirusIgaSerumAntibody;
    private String enterovirusIncubationTime;
    private String enterovirusIndirectFluorescentAntibody;
    private String enterovirusMicroscopy;
    private String enterovirusNeutralizingAntibodies;
    private String enterovirusPcr;
    private String enterovirusGramStain;
    private String enterovirusLatexAgglutination;
    private String enterovirusCqValueDetection;
    private String enterovirusSeQuencing;
    private String enterovirusDnaMicroArray;
    private String enterovirusOther;
    private String mPneumoniaeAntiBodyDetection;
    private String mPneumoniaeAntigenDetection;
    private String mPneumoniaeRapidTest;
    private String mPneumoniaeCulture;
    private String mPneumoniaeHistopathology;
    private String mPneumoniaeIsolation;
    private String mPneumoniaeIgmSerumAntibody;
    private String mPneumoniaeIggSerumAntibody;
    private String mPneumoniaeIgaSerumAntibody;
    private String mPneumoniaeIncubationTime;
    private String mPneumoniaeIndirectFluorescentAntibody;
    private String mPneumoniaeMicroscopy;
    private String mPneumoniaeNeutralizingAntibodies;
    private String mPneumoniaePcr;
    private String mPneumoniaeGramStain;
    private String mPneumoniaeLatexAgglutination;
    private String mPneumoniaeCqValueDetection;
    private String mPneumoniaeSeQuencing;
    private String mPneumoniaeDnaMicroArray;
    private String mPneumoniaeOther;
    private String cPneumoniaeAntiBodyDetection;
    private String cPneumoniaeAntigenDetection;
    private String cPneumoniaeRapidTest;
    private String cPneumoniaeCulture;
    private String cPneumoniaeHistopathology;
    private String cPneumoniaeIsolation;
    private String cPneumoniaeIgmSerumAntibody;
    private String cPneumoniaeIggSerumAntibody;
    private String cPneumoniaeIgaSerumAntibody;
    private String cPneumoniaeIncubationTime;
    private String cPneumoniaeIndirectFluorescentAntibody;
    private String cPneumoniaeMicroscopy;
    private String cPneumoniaeNeutralizingAntibodies;
    private String cPneumoniaePcr;
    private String cPneumoniaeGramStain;
    private String cPneumoniaeLatexAgglutination;
    private String cPneumoniaeCqValueDetection;
    private String cPneumoniaeSeQuencing;
    private String cPneumoniaeDnaMicroArray;
    private String cPneumoniaeOther;
    private String ariAntiBodyDetection;
    private String ariAntigenDetection;
    private String ariRapidTest;
    private String ariCulture;
    private String ariHistopathology;
    private String ariIsolation;
    private String ariIgmSerumAntibody;
    private String ariIggSerumAntibody;
    private String ariIgaSerumAntibody;
    private String ariIncubationTime;
    private String ariIndirectFluorescentAntibody;
    private String ariMicroscopy;
    private String ariNeutralizingAntibodies;
    private String ariPcr;
    private String ariGramStain;
    private String ariLatexAgglutination;
    private String ariCqValueDetection;
    private String ariSeQuencing;
    private String ariDnaMicroArray;
    private String ariOther;
    private String chikungunyaAntiBodyDetection;
    private String chikungunyaAntigenDetection;
    private String chikungunyaRapidTest;
    private String chikungunyaCulture;
    private String chikungunyaHistopathology;
    private String chikungunyaIsolation;
    private String chikungunyaIgmSerumAntibody;
    private String chikungunyaIggSerumAntibody;
    private String chikungunyaIgaSerumAntibody;
    private String chikungunyaIncubationTime;
    private String chikungunyaIndirectFluorescentAntibody;
    private String chikungunyaMicroscopy;
    private String chikungunyaNeutralizingAntibodies;
    private String chikungunyaPcr;
    private String chikungunyaGramStain;
    private String chikungunyaLatexAgglutination;
    private String chikungunyaCqValueDetection;
    private String chikungunyaSeQuencing;
    private String chikungunyaDnaMicroArray;
    private String chikungunyaOther;
    private String postImmunizationAdverseEventsMildAntiBodyDetection;
    private String postImmunizationAdverseEventsMildAntigenDetection;
    private String postImmunizationAdverseEventsMildRapidTest;
    private String postImmunizationAdverseEventsMildCulture;
    private String postImmunizationAdverseEventsMildHistopathology;
    private String postImmunizationAdverseEventsMildIsolation;
    private String postImmunizationAdverseEventsMildIgmSerumAntibody;
    private String postImmunizationAdverseEventsMildIggSerumAntibody;
    private String postImmunizationAdverseEventsMildIgaSerumAntibody;
    private String postImmunizationAdverseEventsMildIncubationTime;
    private String postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    private String postImmunizationAdverseEventsMildMicroscopy;
    private String postImmunizationAdverseEventsMildNeutralizingAntibodies;
    private String postImmunizationAdverseEventsMildPcr;
    private String postImmunizationAdverseEventsMildGramStain;
    private String postImmunizationAdverseEventsMildLatexAgglutination;
    private String postImmunizationAdverseEventsMildCqValueDetection;
    private String postImmunizationAdverseEventsMildSeQuencing;
    private String postImmunizationAdverseEventsMildDnaMicroArray;
    private String postImmunizationAdverseEventsMildOther;
    private String PostImmunizationAdverseEventsSevereAntiBodyDetection;
    private String PostImmunizationAdverseEventsSevereAntigenDetection;
    private String PostImmunizationAdverseEventsSevereRapidTest;
    private String PostImmunizationAdverseEventsSevereCulture;
    private String PostImmunizationAdverseEventsSevereHistopathology;
    private String PostImmunizationAdverseEventsSevereIsolation;
    private String PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIggSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIncubationTime;
    private String PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    private String PostImmunizationAdverseEventsSevereMicroscopy;
    private String PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    private String PostImmunizationAdverseEventsSeverePcr;
    private String PostImmunizationAdverseEventsSevereGramStain;
    private String PostImmunizationAdverseEventsSevereLatexAgglutination;
    private String PostImmunizationAdverseEventsSevereCqValueDetection;
    private String PostImmunizationAdverseEventsSevereSeQuencing;
    private String PostImmunizationAdverseEventsSevereDnaMicroArray;
    private String PostImmunizationAdverseEventsSevereOther;
    private String fhaAntiBodyDetection;
    private String fhaAntigenDetection;
    private String fhaRapidTest;
    private String fhaCulture;
    private String fhaHistopathology;
    private String fhaIsolation;
    private String fhaIgmSerumAntibody;
    private String fhaIggSerumAntibody;
    private String fhaIgaSerumAntibody;
    private String fhaIncubationTime;
    private String fhaIndirectFluorescentAntibody;
    private String fhaMicroscopy;
    private String fhaNeutralizingAntibodies;
    private String fhaPcr;
    private String fhaGramStain;
    private String fhaLatexAgglutination;
    private String fhaCqValueDetection;
    private String fhaSeQuencing;
    private String fhaDnaMicroArray;
    private String fhaOther;
    private String otherAntiBodyDetection;
    private String otherAntigenDetection;
    private String otherRapidTest;
    private String otherCulture;
    private String otherHistopathology;
    private String otherIsolation;
    private String otherIgmSerumAntibody;
    private String otherIggSerumAntibody;
    private String otherIgaSerumAntibody;
    private String otherIncubationTime;
    private String otherIndirectFluorescentAntibody;
    private String otherMicroscopy;
    private String otherNeutralizingAntibodies;
    private String otherPcr;
    private String otherGramStain;
    private String otherLatexAgglutination;
    private String otherCqValueDetection;
    private String otherSeQuencing;
    private String otherDnaMicroArray;
    private String otherOther;
    private String undefinedAntiBodyDetection;
    private String undefinedAntigenDetection;
    private String undefinedRapidTest;
    private String undefinedCulture;
    private String undefinedHistopathology;
    private String undefinedIsolation;
    private String undefinedIgmSerumAntibody;
    private String undefinedIggSerumAntibody;
    private String undefinedIgaSerumAntibody;
    private String undefinedIncubationTime;
    private String undefinedIndirectFluorescentAntibody;
    private String undefinedMicroscopy;
    private String undefinedNeutralizingAntibodies;
    private String undefinedPcr;
    private String undefinedGramStain;
    private String undefinedLatexAgglutination;
    private String undefinedCqValueDetection;
    private String undefinedSeQuencing;
    private String undefinedDnaMicroArray;
    private String undefinedOther;
    //@formatter:off
    public CaseSampleExportDto(
            String uuid,
            String labSampleId,
            Date sampleReportDate,
            Date sampleDateTime,
            SampleMaterial sampleMaterial,
            String sampleMaterialDetails,
            SamplePurpose samplePurpose,
            SampleSource sampleSource,
            SamplingReason samplingReason,
            String samplingReasonDetails,
            String laboratory,
            String laboratoryDetails,
            PathogenTestResultType pathogenTestResult,
            Boolean pathogenTestingRequested,
            String requestedPathogenTests,
            String requestedOtherPathogenTests,
            Boolean additionalTestingRequested,
            String requestedAdditionalTests,
            String requestedOtherAdditionalTests,
            boolean shipped,
            Date shipmentDate,
            String shipmentDetails,
            boolean received,
            Date receivedDate,
            SpecimenCondition specimenCondition,
            String noTestPossibleReason,
            String comment,
            String labUuid,
            Long caseId) {
        //@formatter:on
        this.uuid = uuid;
        this.labSampleID = labSampleId;
        this.sampleReportDate = sampleReportDate;
        this.sampleDateTime = sampleDateTime;
        this.sampleSampleExportMaterial = new SampleExportMaterial(sampleMaterial, sampleMaterialDetails);
        if (samplePurpose != null)
            this.samplePurpose = samplePurpose.toString();
        this.sampleSource = sampleSource;
        this.samplingReason = samplingReason;
        this.samplingReasonDetails = samplingReasonDetails;
        this.lab = FacilityHelper.buildFacilityString(labUuid, laboratory, laboratoryDetails);
        this.pathogenTestResult = pathogenTestResult;
        this.pathogenTestingRequested = pathogenTestingRequested;
        this.requestedPathogenTests = new HashSet<>();
        if (!StringUtils.isEmpty(requestedPathogenTests)) {
            for (String s : requestedPathogenTests.split(",")) {
                this.requestedPathogenTests.add(PathogenTestType.valueOf(s));
            }
        }
        this.requestedOtherPathogenTests = requestedOtherPathogenTests;
        this.additionalTestingRequested = additionalTestingRequested;
        this.requestedAdditionalTests = new HashSet<>();
        if (!StringUtils.isEmpty(requestedAdditionalTests)) {
            for (String s : requestedAdditionalTests.split(",")) {
                this.requestedAdditionalTests.add(AdditionalTestType.valueOf(s));
            }
        }
        this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
        this.shipped = shipped;
        this.shipmentDate = shipmentDate;
        this.shipmentDetails = shipmentDetails;
        this.received = received;
        this.receivedDate = receivedDate;
        this.specimenCondition = specimenCondition;
        this.noTestPossibleReason = noTestPossibleReason;
        this.comment = comment;
        this.caseId = caseId;
    }


    @Order(1)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Order(2)
    public String getLabSampleID() {
        return labSampleID;
    }

    public void setLabSampleID(String labSampleID) {
        this.labSampleID = labSampleID;
    }

    @Order(10)
    public Date getSampleDateTime() {
        return sampleDateTime;
    }

    public void setSampleDateTime(Date sampleDateTime) {
        this.sampleDateTime = sampleDateTime;
    }

    @Order(11)
    public String getSampleMaterialString() {
        return sampleSampleExportMaterial.formatString();
    }

    public SampleExportMaterial getSampleSampleExportMaterial() {
        return sampleSampleExportMaterial;
    }

    @Order(12)
    public String getSamplePurpose() {
        return samplePurpose;
    }

    public void setSamplePurpose(String samplePurpose) {
        this.samplePurpose = samplePurpose;
    }

    @Order(13)
    public SampleSource getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(SampleSource sampleSource) {
        this.sampleSource = sampleSource;
    }

    @Order(14)
    public SamplingReason getSamplingReason() {
        return samplingReason;
    }

    @Order(15)
    public String getSamplingReasonDetails() {
        return samplingReasonDetails;
    }

    @Order(16)
    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    @Order(17)
    public PathogenTestResultType getPathogenTestResult() {
        return pathogenTestResult;
    }

    public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {
        this.pathogenTestResult = pathogenTestResult;
    }

    @Order(18)
    public Boolean getPathogenTestingRequested() {
        return pathogenTestingRequested;
    }

    public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {
        this.pathogenTestingRequested = pathogenTestingRequested;
    }

    @Order(19)
    public Set<PathogenTestType> getRequestedPathogenTests() {
        return requestedPathogenTests;
    }

    public void setRequestedPathogenTests(Set<PathogenTestType> requestedPathogenTests) {
        this.requestedPathogenTests = requestedPathogenTests;
    }

    @Order(20)
    public String getRequestedOtherPathogenTests() {
        return requestedOtherPathogenTests;
    }

    public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {
        this.requestedOtherPathogenTests = requestedOtherPathogenTests;
    }

    @Order(21)
    public Boolean getAdditionalTestingRequested() {
        return additionalTestingRequested;
    }

    public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {
        this.additionalTestingRequested = additionalTestingRequested;
    }

    @Order(22)
    public Set<AdditionalTestType> getRequestedAdditionalTests() {
        return requestedAdditionalTests;
    }

    public void setRequestedAdditionalTests(Set<AdditionalTestType> requestedAdditionalTests) {
        this.requestedAdditionalTests = requestedAdditionalTests;
    }

    @Order(23)
    public String getRequestedOtherAdditionalTests() {
        return requestedOtherAdditionalTests;
    }

    public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {
        this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
    }

    @Order(24)
    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    @Order(25)
    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    @Order(26)
    public String getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(String shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

    @Order(27)
    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    @Order(28)
    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Order(31)
    public SpecimenCondition getSpecimenCondition() {
        return specimenCondition;
    }

    public void setSpecimenCondition(SpecimenCondition specimenCondition) {
        this.specimenCondition = specimenCondition;
    }

    @Order(32)
    public String getNoTestPossibleReason() {
        return noTestPossibleReason;
    }

    public void setNoTestPossibleReason(String noTestPossibleReason) {
        this.noTestPossibleReason = noTestPossibleReason;
    }

    @Order(33)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Order(71)
    public String getPathogenTestType1() {
        return pathogenTest1.formatType();
    }

    @Order(72)
    public String getPathogenTestDisease1() {
        return pathogenTest1.disease;
    }

    @Order(73)
    public Date getPathogenTestDateTime1() {
        return pathogenTest1.dateTime;
    }

    @Order(74)
    public String getPathogenTestLab1() {
        return pathogenTest1.lab;
    }

    @Order(75)
    public PathogenTestResultType getPathogenTestResult1() {
        return pathogenTest1.testResult;
    }

    @Order(76)
    public Boolean getPathogenTestVerified1() {
        return pathogenTest1.verified;
    }

    @Order(81)
    public String getPathogenTestType2() {
        return pathogenTest2.formatType();
    }

    @Order(82)
    public String getPathogenTestDisease2() {
        return pathogenTest2.disease;
    }

    @Order(83)
    public Date getPathogenTestDateTime2() {
        return pathogenTest2.dateTime;
    }

    @Order(84)
    public String getPathogenTestLab2() {
        return pathogenTest2.lab;
    }

    @Order(85)
    public PathogenTestResultType getPathogenTestResult2() {
        return pathogenTest2.testResult;
    }

    @Order(86)
    public Boolean getPathogenTestVerified2() {
        return pathogenTest2.verified;
    }

    @Order(91)
    public String getPathogenTestType3() {
        return pathogenTest3.formatType();
    }

    @Order(92)
    public String getPathogenTestDisease3() {
        return pathogenTest3.disease;
    }

    @Order(93)
    public Date getPathogenTestDateTime3() {
        return pathogenTest3.dateTime;
    }

    @Order(94)
    public String getPathogenTestLab3() {
        return pathogenTest3.lab;
    }

    @Order(95)
    public PathogenTestResultType getPathogenTestResult3() {
        return pathogenTest3.testResult;
    }

    @Order(96)
    public Boolean getPathogenTestVerified3() {
        return pathogenTest3.verified;
    }

    @Order(97)
    public String getOtherPathogenTestsDetails() {
        StringBuilder sb = new StringBuilder();
        String separator = ", ";

        for (SampleExportPathogenTest otherPathogenTest : otherPathogenTests) {
            sb.append(otherPathogenTest.formatString()).append(separator);
        }

        return sb.length() > 0 ? sb.substring(0, sb.length() - separator.length()) : "";
    }

    public void addOtherPathogenTest(SampleExportPathogenTest pathogenTest) {
        otherPathogenTests.add(pathogenTest);
    }

    @Order(101)
    public AdditionalTestDto getAdditionalTest() {
        return additionalTest;
    }

    public void setAdditionalTest(AdditionalTestDto additionalTest) {
        this.additionalTest = additionalTest;
    }

    @Order(102)
    public String getOtherAdditionalTestsDetails() {
        return otherAdditionalTestsDetails;
    }

    public void setOtherAdditionalTestsDetails(String otherAdditionalTestsDetails) {
        this.otherAdditionalTestsDetails = otherAdditionalTestsDetails;
    }

    @Order(103)
    public Date getSampleReportDate() {
        return sampleReportDate;
    }



    public void setSampleReportDate(Date sampleReportDate) {
        this.sampleReportDate = sampleReportDate;
    }

    public SampleExportPathogenTest getPathogenTest1() {
        return pathogenTest1;
    }

    //    public void setPathogenTest1(SampleExportPathogenTest pathogenTest1) {
//        this.pathogenTest1 = pathogenTest1;
//    }
//
//    public SampleExportPathogenTest getPathogenTest2() {
//        return pathogenTest2;
//    }
//
//    public void setPathogenTest2(SampleExportPathogenTest pathogenTest2) {
//        this.pathogenTest2 = pathogenTest2;
//    }
//
//    public SampleExportPathogenTest getPathogenTest3() {
//        return pathogenTest3;
//    }
//
//    public void setPathogenTest3(SampleExportPathogenTest pathogenTest3) {
//        this.pathogenTest3 = pathogenTest3;
//    }

    public List<SampleExportPathogenTest> getOtherPathogenTests() {
        return otherPathogenTests;
    }

    public static class SampleExportMaterial implements Serializable {

        private SampleMaterial sampleMaterial;
        @SensitiveData
        private String sampleMaterialDetails;

        public SampleExportMaterial(SampleMaterial sampleMaterial, String sampleMaterialDetails) {
            this.sampleMaterial = sampleMaterial;
            this.sampleMaterialDetails = sampleMaterialDetails;
        }

        public String formatString() {
            return SampleMaterial.toString(sampleMaterial, sampleMaterialDetails);
        }
    }

    public static class SampleExportPathogenTest implements Serializable {

        private PathogenTestType testType;
        @SensitiveData
        private String testTypeText;
        private String disease;
        private Date dateTime;
        private String lab;
        private PathogenTestResultType testResult;
        private Boolean verified;

        public SampleExportPathogenTest() {
        }

        public SampleExportPathogenTest(
                PathogenTestType testType,
                String testTypeText,
                String disease,
                Date dateTime,
                String lab,
                PathogenTestResultType testResult,
                Boolean verified) {
            this.testType = testType;
            this.testTypeText = testTypeText;
            this.disease = disease;
            this.dateTime = dateTime;
            this.lab = lab;
            this.testResult = testResult;
            this.verified = verified;
        }

        public String formatType() {
            return PathogenTestType.toString(testType, testTypeText);
        }

        public String formatString() {
            StringBuilder sb = new StringBuilder();
            sb.append(DateHelper.formatDateForExport(dateTime)).append(" (");
            String type = formatType();
            if (type.length() > 0) {
                sb.append(type).append(", ");
            }

            sb.append(disease).append(", ").append(testResult).append(")");

            return sb.toString();
        }
    }


    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public void setSampleSampleExportMaterial(SampleExportMaterial sampleSampleExportMaterial) {
        this.sampleSampleExportMaterial = sampleSampleExportMaterial;
    }

    public void setSamplingReason(SamplingReason samplingReason) {
        this.samplingReason = samplingReason;
    }

    public void setSamplingReasonDetails(String samplingReasonDetails) {
        this.samplingReasonDetails = samplingReasonDetails;
    }

    public void setPathogenTest1(SampleExportPathogenTest pathogenTest1) {
        this.pathogenTest1 = pathogenTest1;
    }

    public void setPathogenTest2(SampleExportPathogenTest pathogenTest2) {
        this.pathogenTest2 = pathogenTest2;
    }

    public void setPathogenTest3(SampleExportPathogenTest pathogenTest3) {
        this.pathogenTest3 = pathogenTest3;
    }

    public void setOtherPathogenTests(List<SampleExportPathogenTest> otherPathogenTests) {
        this.otherPathogenTests = otherPathogenTests;
    }

    public void setAfpAntiBodyDetection(String afpAntiBodyDetection) {
        this.afpAntiBodyDetection = afpAntiBodyDetection;
    }

    public void setAfpAntigenDetection(String afpAntigenDetection) {
        this.afpAntigenDetection = afpAntigenDetection;
    }

    public void setAfpRapidTest(String afpRapidTest) {
        this.afpRapidTest = afpRapidTest;
    }

    public void setAfpCulture(String afpCulture) {
        this.afpCulture = afpCulture;
    }

    public void setAfpHistopathology(String afpHistopathology) {
        this.afpHistopathology = afpHistopathology;
    }

    public void setAfpIsolation(String afpIsolation) {
        this.afpIsolation = afpIsolation;
    }

    public void setAfpIgmSerumAntibody(String afpIgmSerumAntibody) {
        this.afpIgmSerumAntibody = afpIgmSerumAntibody;
    }

    public void setAfpIggSerumAntibody(String afpIggSerumAntibody) {
        this.afpIggSerumAntibody = afpIggSerumAntibody;
    }

    public void setAfpIgaSerumAntibody(String afpIgaSerumAntibody) {
        this.afpIgaSerumAntibody = afpIgaSerumAntibody;
    }

    public void setAfpIncubationTime(String afpIncubationTime) {
        this.afpIncubationTime = afpIncubationTime;
    }

    public void setAfpIndirectFluorescentAntibody(String afpIndirectFluorescentAntibody) {
        this.afpIndirectFluorescentAntibody = afpIndirectFluorescentAntibody;
    }

    public void setAfpMicroscopy(String afpMicroscopy) {
        this.afpMicroscopy = afpMicroscopy;
    }

    public void setAfpNeutralizingAntibodies(String afpNeutralizingAntibodies) {
        this.afpNeutralizingAntibodies = afpNeutralizingAntibodies;
    }

    public void setAfpPcr(String afpPcr) {
        this.afpPcr = afpPcr;
    }

    public void setAfpGramStain(String afpGramStain) {
        this.afpGramStain = afpGramStain;
    }

    public void setAfpLatexAgglutination(String afpLatexAgglutination) {
        this.afpLatexAgglutination = afpLatexAgglutination;
    }

    public void setAfpCqValueDetection(String afpCqValueDetection) {
        this.afpCqValueDetection = afpCqValueDetection;
    }

    public void setAfpSeQuencing(String afpSeQuencing) {
        this.afpSeQuencing = afpSeQuencing;
    }

    public void setAfpDnaMicroArray(String afpDnaMicroArray) {
        this.afpDnaMicroArray = afpDnaMicroArray;
    }

    public void setAfpOther(String afpOther) {
        this.afpOther = afpOther;
    }

    public void setCholeraAntiBodyDetection(String choleraAntiBodyDetection) {
        this.choleraAntiBodyDetection = choleraAntiBodyDetection;
    }

    public void setCholeraAntigenDetection(String choleraAntigenDetection) {
        this.choleraAntigenDetection = choleraAntigenDetection;
    }

    public void setCholeraRapidTest(String choleraRapidTest) {
        this.choleraRapidTest = choleraRapidTest;
    }

    public void setCholeraCulture(String choleraCulture) {
        this.choleraCulture = choleraCulture;
    }

    public void setCholeraHistopathology(String choleraHistopathology) {
        this.choleraHistopathology = choleraHistopathology;
    }

    public void setCholeraIsolation(String choleraIsolation) {
        this.choleraIsolation = choleraIsolation;
    }

    public void setCholeraIgmSerumAntibody(String choleraIgmSerumAntibody) {
        this.choleraIgmSerumAntibody = choleraIgmSerumAntibody;
    }

    public void setCholeraIggSerumAntibody(String choleraIggSerumAntibody) {
        this.choleraIggSerumAntibody = choleraIggSerumAntibody;
    }

    public void setCholeraIgaSerumAntibody(String choleraIgaSerumAntibody) {
        this.choleraIgaSerumAntibody = choleraIgaSerumAntibody;
    }

    public void setCholeraIncubationTime(String choleraIncubationTime) {
        this.choleraIncubationTime = choleraIncubationTime;
    }

    public void setCholeraIndirectFluorescentAntibody(String choleraIndirectFluorescentAntibody) {
        this.choleraIndirectFluorescentAntibody = choleraIndirectFluorescentAntibody;
    }

    public void setCholeraMicroscopy(String choleraMicroscopy) {
        this.choleraMicroscopy = choleraMicroscopy;
    }

    public void setCholeraNeutralizingAntibodies(String choleraNeutralizingAntibodies) {
        this.choleraNeutralizingAntibodies = choleraNeutralizingAntibodies;
    }

    public void setCholeraPcr(String choleraPcr) {
        this.choleraPcr = choleraPcr;
    }

    public void setCholeraGramStain(String choleraGramStain) {
        this.choleraGramStain = choleraGramStain;
    }

    public void setCholeraLatexAgglutination(String choleraLatexAgglutination) {
        this.choleraLatexAgglutination = choleraLatexAgglutination;
    }

    public void setCholeraCqValueDetection(String choleraCqValueDetection) {
        this.choleraCqValueDetection = choleraCqValueDetection;
    }

    public void setCholeraSeQuencing(String choleraSeQuencing) {
        this.choleraSeQuencing = choleraSeQuencing;
    }

    public void setCholeraDnaMicroArray(String choleraDnaMicroArray) {
        this.choleraDnaMicroArray = choleraDnaMicroArray;
    }

    public void setCholeraOther(String choleraOther) {
        this.choleraOther = choleraOther;
    }

    public void setCongenitalRubellaAntiBodyDetection(String congenitalRubellaAntiBodyDetection) {
        this.congenitalRubellaAntiBodyDetection = congenitalRubellaAntiBodyDetection;
    }

    public void setCongenitalRubellaAntigenDetection(String congenitalRubellaAntigenDetection) {
        this.congenitalRubellaAntigenDetection = congenitalRubellaAntigenDetection;
    }

    public void setCongenitalRubellaRapidTest(String congenitalRubellaRapidTest) {
        this.congenitalRubellaRapidTest = congenitalRubellaRapidTest;
    }

    public void setCongenitalRubellaCulture(String congenitalRubellaCulture) {
        this.congenitalRubellaCulture = congenitalRubellaCulture;
    }

    public void setCongenitalRubellaHistopathology(String congenitalRubellaHistopathology) {
        this.congenitalRubellaHistopathology = congenitalRubellaHistopathology;
    }

    public void setCongenitalRubellaIsolation(String congenitalRubellaIsolation) {
        this.congenitalRubellaIsolation = congenitalRubellaIsolation;
    }

    public void setCongenitalRubellaIgmSerumAntibody(String congenitalRubellaIgmSerumAntibody) {
        this.congenitalRubellaIgmSerumAntibody = congenitalRubellaIgmSerumAntibody;
    }

    public void setCongenitalRubellaIggSerumAntibody(String congenitalRubellaIggSerumAntibody) {
        this.congenitalRubellaIggSerumAntibody = congenitalRubellaIggSerumAntibody;
    }

    public void setCongenitalRubellaIgaSerumAntibody(String congenitalRubellaIgaSerumAntibody) {
        this.congenitalRubellaIgaSerumAntibody = congenitalRubellaIgaSerumAntibody;
    }

    public void setCongenitalRubellaIncubationTime(String congenitalRubellaIncubationTime) {
        this.congenitalRubellaIncubationTime = congenitalRubellaIncubationTime;
    }

    public void setCongenitalRubellaIndirectFluorescentAntibody(String congenitalRubellaIndirectFluorescentAntibody) {
        this.congenitalRubellaIndirectFluorescentAntibody = congenitalRubellaIndirectFluorescentAntibody;
    }

    public void setCongenitalRubellaMicroscopy(String congenitalRubellaMicroscopy) {
        this.congenitalRubellaMicroscopy = congenitalRubellaMicroscopy;
    }

    public void setCongenitalRubellaNeutralizingAntibodies(String congenitalRubellaNeutralizingAntibodies) {
        this.congenitalRubellaNeutralizingAntibodies = congenitalRubellaNeutralizingAntibodies;
    }

    public void setCongenitalRubellaPcr(String congenitalRubellaPcr) {
        this.congenitalRubellaPcr = congenitalRubellaPcr;
    }

    public void setCongenitalRubellaGramStain(String congenitalRubellaGramStain) {
        this.congenitalRubellaGramStain = congenitalRubellaGramStain;
    }

    public void setCongenitalRubellaLatexAgglutination(String congenitalRubellaLatexAgglutination) {
        this.congenitalRubellaLatexAgglutination = congenitalRubellaLatexAgglutination;
    }

    public void setCongenitalRubellaCqValueDetection(String congenitalRubellaCqValueDetection) {
        this.congenitalRubellaCqValueDetection = congenitalRubellaCqValueDetection;
    }

    public void setCongenitalRubellaSeQuencing(String congenitalRubellaSeQuencing) {
        this.congenitalRubellaSeQuencing = congenitalRubellaSeQuencing;
    }

    public void setCongenitalRubellaDnaMicroArray(String congenitalRubellaDnaMicroArray) {
        this.congenitalRubellaDnaMicroArray = congenitalRubellaDnaMicroArray;
    }

    public void setCongenitalRubellaOther(String congenitalRubellaOther) {
        this.congenitalRubellaOther = congenitalRubellaOther;
    }

    public void setCsmAntiBodyDetection(String csmAntiBodyDetection) {
        this.csmAntiBodyDetection = csmAntiBodyDetection;
    }

    public void setCsmAntigenDetection(String csmAntigenDetection) {
        this.csmAntigenDetection = csmAntigenDetection;
    }

    public void setCsmRapidTest(String csmRapidTest) {
        this.csmRapidTest = csmRapidTest;
    }

    public void setCsmCulture(String csmCulture) {
        this.csmCulture = csmCulture;
    }

    public void setCsmHistopathology(String csmHistopathology) {
        this.csmHistopathology = csmHistopathology;
    }

    public void setCsmIsolation(String csmIsolation) {
        this.csmIsolation = csmIsolation;
    }

    public void setCsmIgmSerumAntibody(String csmIgmSerumAntibody) {
        this.csmIgmSerumAntibody = csmIgmSerumAntibody;
    }

    public void setCsmIggSerumAntibody(String csmIggSerumAntibody) {
        this.csmIggSerumAntibody = csmIggSerumAntibody;
    }

    public void setCsmIgaSerumAntibody(String csmIgaSerumAntibody) {
        this.csmIgaSerumAntibody = csmIgaSerumAntibody;
    }

    public void setCsmIncubationTime(String csmIncubationTime) {
        this.csmIncubationTime = csmIncubationTime;
    }

    public void setCsmIndirectFluorescentAntibody(String csmIndirectFluorescentAntibody) {
        this.csmIndirectFluorescentAntibody = csmIndirectFluorescentAntibody;
    }

    public void setCsmMicroscopy(String csmMicroscopy) {
        this.csmMicroscopy = csmMicroscopy;
    }

    public void setCsmNeutralizingAntibodies(String csmNeutralizingAntibodies) {
        this.csmNeutralizingAntibodies = csmNeutralizingAntibodies;
    }

    public void setCsmPcr(String csmPcr) {
        this.csmPcr = csmPcr;
    }

    public void setCsmGramStain(String csmGramStain) {
        this.csmGramStain = csmGramStain;
    }

    public void setCsmLatexAgglutination(String csmLatexAgglutination) {
        this.csmLatexAgglutination = csmLatexAgglutination;
    }

    public void setCsmCqValueDetection(String csmCqValueDetection) {
        this.csmCqValueDetection = csmCqValueDetection;
    }

    public void setCsmSeQuencing(String csmSeQuencing) {
        this.csmSeQuencing = csmSeQuencing;
    }

    public void setCsmDnaMicroArray(String csmDnaMicroArray) {
        this.csmDnaMicroArray = csmDnaMicroArray;
    }

    public void setCsmOther(String csmOther) {
        this.csmOther = csmOther;
    }

    public void setDengueAntiBodyDetection(String dengueAntiBodyDetection) {
        this.dengueAntiBodyDetection = dengueAntiBodyDetection;
    }

    public void setDengueAntigenDetection(String dengueAntigenDetection) {
        this.dengueAntigenDetection = dengueAntigenDetection;
    }

    public void setDengueRapidTest(String dengueRapidTest) {
        this.dengueRapidTest = dengueRapidTest;
    }

    public void setDengueCulture(String dengueCulture) {
        this.dengueCulture = dengueCulture;
    }

    public void setDengueHistopathology(String dengueHistopathology) {
        this.dengueHistopathology = dengueHistopathology;
    }

    public void setDengueIsolation(String dengueIsolation) {
        this.dengueIsolation = dengueIsolation;
    }

    public void setDengueIgmSerumAntibody(String dengueIgmSerumAntibody) {
        this.dengueIgmSerumAntibody = dengueIgmSerumAntibody;
    }

    public void setDengueIggSerumAntibody(String dengueIggSerumAntibody) {
        this.dengueIggSerumAntibody = dengueIggSerumAntibody;
    }

    public void setDengueIgaSerumAntibody(String dengueIgaSerumAntibody) {
        this.dengueIgaSerumAntibody = dengueIgaSerumAntibody;
    }

    public void setDengueIncubationTime(String dengueIncubationTime) {
        this.dengueIncubationTime = dengueIncubationTime;
    }

    public void setDengueIndirectFluorescentAntibody(String dengueIndirectFluorescentAntibody) {
        this.dengueIndirectFluorescentAntibody = dengueIndirectFluorescentAntibody;
    }

    public void setDengueMicroscopy(String dengueMicroscopy) {
        this.dengueMicroscopy = dengueMicroscopy;
    }

    public void setDengueNeutralizingAntibodies(String dengueNeutralizingAntibodies) {
        this.dengueNeutralizingAntibodies = dengueNeutralizingAntibodies;
    }

    public void setDenguePcr(String denguePcr) {
        this.denguePcr = denguePcr;
    }

    public void setDengueGramStain(String dengueGramStain) {
        this.dengueGramStain = dengueGramStain;
    }

    public void setDengueLatexAgglutination(String dengueLatexAgglutination) {
        this.dengueLatexAgglutination = dengueLatexAgglutination;
    }

    public void setDengueCqValueDetection(String dengueCqValueDetection) {
        this.dengueCqValueDetection = dengueCqValueDetection;
    }

    public void setDengueSeQuencing(String dengueSeQuencing) {
        this.dengueSeQuencing = dengueSeQuencing;
    }

    public void setDengueDnaMicroArray(String dengueDnaMicroArray) {
        this.dengueDnaMicroArray = dengueDnaMicroArray;
    }

    public void setDengueOther(String dengueOther) {
        this.dengueOther = dengueOther;
    }

    public void setEvdAntiBodyDetection(String evdAntiBodyDetection) {
        this.evdAntiBodyDetection = evdAntiBodyDetection;
    }

    public void setEvdAntigenDetection(String evdAntigenDetection) {
        this.evdAntigenDetection = evdAntigenDetection;
    }

    public void setEvdRapidTest(String evdRapidTest) {
        this.evdRapidTest = evdRapidTest;
    }

    public void setEvdCulture(String evdCulture) {
        this.evdCulture = evdCulture;
    }

    public void setEvdHistopathology(String evdHistopathology) {
        this.evdHistopathology = evdHistopathology;
    }

    public void setEvdIsolation(String evdIsolation) {
        this.evdIsolation = evdIsolation;
    }

    public void setEvdIgmSerumAntibody(String evdIgmSerumAntibody) {
        this.evdIgmSerumAntibody = evdIgmSerumAntibody;
    }

    public void setEvdIggSerumAntibody(String evdIggSerumAntibody) {
        this.evdIggSerumAntibody = evdIggSerumAntibody;
    }

    public void setEvdIgaSerumAntibody(String evdIgaSerumAntibody) {
        this.evdIgaSerumAntibody = evdIgaSerumAntibody;
    }

    public void setEvdIncubationTime(String evdIncubationTime) {
        this.evdIncubationTime = evdIncubationTime;
    }

    public void setEvdIndirectFluorescentAntibody(String evdIndirectFluorescentAntibody) {
        this.evdIndirectFluorescentAntibody = evdIndirectFluorescentAntibody;
    }

    public void setEvdMicroscopy(String evdMicroscopy) {
        this.evdMicroscopy = evdMicroscopy;
    }

    public void setEvdNeutralizingAntibodies(String evdNeutralizingAntibodies) {
        this.evdNeutralizingAntibodies = evdNeutralizingAntibodies;
    }

    public void setEvdPcr(String evdPcr) {
        this.evdPcr = evdPcr;
    }

    public void setEvdGramStain(String evdGramStain) {
        this.evdGramStain = evdGramStain;
    }

    public void setEvdLatexAgglutination(String evdLatexAgglutination) {
        this.evdLatexAgglutination = evdLatexAgglutination;
    }

    public void setEvdCqValueDetection(String evdCqValueDetection) {
        this.evdCqValueDetection = evdCqValueDetection;
    }

    public void setEvdSeQuencing(String evdSeQuencing) {
        this.evdSeQuencing = evdSeQuencing;
    }

    public void setEvdDnaMicroArray(String evdDnaMicroArray) {
        this.evdDnaMicroArray = evdDnaMicroArray;
    }

    public void setEvdOther(String evdOther) {
        this.evdOther = evdOther;
    }

    public void setGuineaWormAntiBodyDetection(String guineaWormAntiBodyDetection) {
        this.guineaWormAntiBodyDetection = guineaWormAntiBodyDetection;
    }

    public void setGuineaWormAntigenDetection(String guineaWormAntigenDetection) {
        this.guineaWormAntigenDetection = guineaWormAntigenDetection;
    }

    public void setGuineaWormRapidTest(String guineaWormRapidTest) {
        this.guineaWormRapidTest = guineaWormRapidTest;
    }

    public void setGuineaWormCulture(String guineaWormCulture) {
        this.guineaWormCulture = guineaWormCulture;
    }

    public void setGuineaWormHistopathology(String guineaWormHistopathology) {
        this.guineaWormHistopathology = guineaWormHistopathology;
    }

    public void setGuineaWormIsolation(String guineaWormIsolation) {
        this.guineaWormIsolation = guineaWormIsolation;
    }

    public void setGuineaWormIgmSerumAntibody(String guineaWormIgmSerumAntibody) {
        this.guineaWormIgmSerumAntibody = guineaWormIgmSerumAntibody;
    }

    public void setGuineaWormIggSerumAntibody(String guineaWormIggSerumAntibody) {
        this.guineaWormIggSerumAntibody = guineaWormIggSerumAntibody;
    }

    public void setGuineaWormIgaSerumAntibody(String guineaWormIgaSerumAntibody) {
        this.guineaWormIgaSerumAntibody = guineaWormIgaSerumAntibody;
    }

    public void setGuineaWormIncubationTime(String guineaWormIncubationTime) {
        this.guineaWormIncubationTime = guineaWormIncubationTime;
    }

    public void setGuineaWormIndirectFluorescentAntibody(String guineaWormIndirectFluorescentAntibody) {
        this.guineaWormIndirectFluorescentAntibody = guineaWormIndirectFluorescentAntibody;
    }

    public void setGuineaWormMicroscopy(String guineaWormMicroscopy) {
        this.guineaWormMicroscopy = guineaWormMicroscopy;
    }

    public void setGuineaWormNeutralizingAntibodies(String guineaWormNeutralizingAntibodies) {
        this.guineaWormNeutralizingAntibodies = guineaWormNeutralizingAntibodies;
    }

    public void setGuineaWormPcr(String guineaWormPcr) {
        this.guineaWormPcr = guineaWormPcr;
    }

    public void setGuineaWormGramStain(String guineaWormGramStain) {
        this.guineaWormGramStain = guineaWormGramStain;
    }

    public void setGuineaWormLatexAgglutination(String guineaWormLatexAgglutination) {
        this.guineaWormLatexAgglutination = guineaWormLatexAgglutination;
    }

    public void setGuineaWormCqValueDetection(String guineaWormCqValueDetection) {
        this.guineaWormCqValueDetection = guineaWormCqValueDetection;
    }

    public void setGuineaWormSeQuencing(String guineaWormSeQuencing) {
        this.guineaWormSeQuencing = guineaWormSeQuencing;
    }

    public void setGuineaWormDnaMicroArray(String guineaWormDnaMicroArray) {
        this.guineaWormDnaMicroArray = guineaWormDnaMicroArray;
    }

    public void setGuineaWormOther(String guineaWormOther) {
        this.guineaWormOther = guineaWormOther;
    }

    public void setLassaAntiBodyDetection(String lassaAntiBodyDetection) {
        this.lassaAntiBodyDetection = lassaAntiBodyDetection;
    }

    public void setLassaAntigenDetection(String lassaAntigenDetection) {
        this.lassaAntigenDetection = lassaAntigenDetection;
    }

    public void setLassaRapidTest(String lassaRapidTest) {
        this.lassaRapidTest = lassaRapidTest;
    }

    public void setLassaCulture(String lassaCulture) {
        this.lassaCulture = lassaCulture;
    }

    public void setLassaHistopathology(String lassaHistopathology) {
        this.lassaHistopathology = lassaHistopathology;
    }

    public void setLassaIsolation(String lassaIsolation) {
        this.lassaIsolation = lassaIsolation;
    }

    public void setLassaIgmSerumAntibody(String lassaIgmSerumAntibody) {
        this.lassaIgmSerumAntibody = lassaIgmSerumAntibody;
    }

    public void setLassaIggSerumAntibody(String lassaIggSerumAntibody) {
        this.lassaIggSerumAntibody = lassaIggSerumAntibody;
    }

    public void setLassaIgaSerumAntibody(String lassaIgaSerumAntibody) {
        this.lassaIgaSerumAntibody = lassaIgaSerumAntibody;
    }

    public void setLassaIncubationTime(String lassaIncubationTime) {
        this.lassaIncubationTime = lassaIncubationTime;
    }

    public void setLassaIndirectFluorescentAntibody(String lassaIndirectFluorescentAntibody) {
        this.lassaIndirectFluorescentAntibody = lassaIndirectFluorescentAntibody;
    }

    public void setLassaMicroscopy(String lassaMicroscopy) {
        this.lassaMicroscopy = lassaMicroscopy;
    }

    public void setLassaNeutralizingAntibodies(String lassaNeutralizingAntibodies) {
        this.lassaNeutralizingAntibodies = lassaNeutralizingAntibodies;
    }

    public void setLassaPcr(String lassaPcr) {
        this.lassaPcr = lassaPcr;
    }

    public void setLassaGramStain(String lassaGramStain) {
        this.lassaGramStain = lassaGramStain;
    }

    public void setLassaLatexAgglutination(String lassaLatexAgglutination) {
        this.lassaLatexAgglutination = lassaLatexAgglutination;
    }

    public void setLassaCqValueDetection(String lassaCqValueDetection) {
        this.lassaCqValueDetection = lassaCqValueDetection;
    }

    public void setLassaSeQuencing(String lassaSeQuencing) {
        this.lassaSeQuencing = lassaSeQuencing;
    }

    public void setLassaDnaMicroArray(String lassaDnaMicroArray) {
        this.lassaDnaMicroArray = lassaDnaMicroArray;
    }

    public void setLassaOther(String lassaOther) {
        this.lassaOther = lassaOther;
    }

    public void setMeaslesAntiBodyDetection(String measlesAntiBodyDetection) {
        this.measlesAntiBodyDetection = measlesAntiBodyDetection;
    }

    public void setMeaslesAntigenDetection(String measlesAntigenDetection) {
        this.measlesAntigenDetection = measlesAntigenDetection;
    }

    public void setMeaslesRapidTest(String measlesRapidTest) {
        this.measlesRapidTest = measlesRapidTest;
    }

    public void setMeaslesCulture(String measlesCulture) {
        this.measlesCulture = measlesCulture;
    }

    public void setMeaslesHistopathology(String measlesHistopathology) {
        this.measlesHistopathology = measlesHistopathology;
    }

    public void setMeaslesIsolation(String measlesIsolation) {
        this.measlesIsolation = measlesIsolation;
    }

    public void setMeaslesIgmSerumAntibody(String measlesIgmSerumAntibody) {
        this.measlesIgmSerumAntibody = measlesIgmSerumAntibody;
    }

    public void setMeaslesIggSerumAntibody(String measlesIggSerumAntibody) {
        this.measlesIggSerumAntibody = measlesIggSerumAntibody;
    }

    public void setMeaslesIgaSerumAntibody(String measlesIgaSerumAntibody) {
        this.measlesIgaSerumAntibody = measlesIgaSerumAntibody;
    }

    public void setMeaslesIncubationTime(String measlesIncubationTime) {
        this.measlesIncubationTime = measlesIncubationTime;
    }

    public void setMeaslesIndirectFluorescentAntibody(String measlesIndirectFluorescentAntibody) {
        this.measlesIndirectFluorescentAntibody = measlesIndirectFluorescentAntibody;
    }

    public void setMeaslesMicroscopy(String measlesMicroscopy) {
        this.measlesMicroscopy = measlesMicroscopy;
    }

    public void setMeaslesNeutralizingAntibodies(String measlesNeutralizingAntibodies) {
        this.measlesNeutralizingAntibodies = measlesNeutralizingAntibodies;
    }

    public void setMeaslesPcr(String measlesPcr) {
        this.measlesPcr = measlesPcr;
    }

    public void setMeaslesGramStain(String measlesGramStain) {
        this.measlesGramStain = measlesGramStain;
    }

    public void setMeaslesLatexAgglutination(String measlesLatexAgglutination) {
        this.measlesLatexAgglutination = measlesLatexAgglutination;
    }

    public void setMeaslesCqValueDetection(String measlesCqValueDetection) {
        this.measlesCqValueDetection = measlesCqValueDetection;
    }

    public void setMeaslesSeQuencing(String measlesSeQuencing) {
        this.measlesSeQuencing = measlesSeQuencing;
    }

    public void setMeaslesDnaMicroArray(String measlesDnaMicroArray) {
        this.measlesDnaMicroArray = measlesDnaMicroArray;
    }

    public void setMeaslesOther(String measlesOther) {
        this.measlesOther = measlesOther;
    }

    public void setMonkeyPoxAntiBodyDetection(String monkeyPoxAntiBodyDetection) {
        this.monkeyPoxAntiBodyDetection = monkeyPoxAntiBodyDetection;
    }

    public void setMonkeyPoxAntigenDetection(String monkeyPoxAntigenDetection) {
        this.monkeyPoxAntigenDetection = monkeyPoxAntigenDetection;
    }

    public void setMonkeyPoxRapidTest(String monkeyPoxRapidTest) {
        this.monkeyPoxRapidTest = monkeyPoxRapidTest;
    }

    public void setMonkeyPoxCulture(String monkeyPoxCulture) {
        this.monkeyPoxCulture = monkeyPoxCulture;
    }

    public void setMonkeyPoxHistopathology(String monkeyPoxHistopathology) {
        this.monkeyPoxHistopathology = monkeyPoxHistopathology;
    }

    public void setMonkeyPoxIsolation(String monkeyPoxIsolation) {
        this.monkeyPoxIsolation = monkeyPoxIsolation;
    }

    public void setMonkeyPoxIgmSerumAntibody(String monkeyPoxIgmSerumAntibody) {
        this.monkeyPoxIgmSerumAntibody = monkeyPoxIgmSerumAntibody;
    }

    public void setMonkeyPoxIggSerumAntibody(String monkeyPoxIggSerumAntibody) {
        this.monkeyPoxIggSerumAntibody = monkeyPoxIggSerumAntibody;
    }

    public void setMonkeyPoxIgaSerumAntibody(String monkeyPoxIgaSerumAntibody) {
        this.monkeyPoxIgaSerumAntibody = monkeyPoxIgaSerumAntibody;
    }

    public void setMonkeyPoxIncubationTime(String monkeyPoxIncubationTime) {
        this.monkeyPoxIncubationTime = monkeyPoxIncubationTime;
    }

    public void setMonkeyPoxIndirectFluorescentAntibody(String monkeyPoxIndirectFluorescentAntibody) {
        this.monkeyPoxIndirectFluorescentAntibody = monkeyPoxIndirectFluorescentAntibody;
    }

    public void setMonkeyPoxMicroscopy(String monkeyPoxMicroscopy) {
        this.monkeyPoxMicroscopy = monkeyPoxMicroscopy;
    }

    public void setMonkeyPoxNeutralizingAntibodies(String monkeyPoxNeutralizingAntibodies) {
        this.monkeyPoxNeutralizingAntibodies = monkeyPoxNeutralizingAntibodies;
    }

    public void setMonkeyPoxPcr(String monkeyPoxPcr) {
        this.monkeyPoxPcr = monkeyPoxPcr;
    }

    public void setMonkeyPoxGramStain(String monkeyPoxGramStain) {
        this.monkeyPoxGramStain = monkeyPoxGramStain;
    }

    public void setMonkeyPoxLatexAgglutination(String monkeyPoxLatexAgglutination) {
        this.monkeyPoxLatexAgglutination = monkeyPoxLatexAgglutination;
    }

    public void setMonkeyPoxCqValueDetection(String monkeyPoxCqValueDetection) {
        this.monkeyPoxCqValueDetection = monkeyPoxCqValueDetection;
    }

    public void setMonkeyPoxSeQuencing(String monkeyPoxSeQuencing) {
        this.monkeyPoxSeQuencing = monkeyPoxSeQuencing;
    }

    public void setMonkeyPoxDnaMicroArray(String monkeyPoxDnaMicroArray) {
        this.monkeyPoxDnaMicroArray = monkeyPoxDnaMicroArray;
    }

    public void setMonkeyPoxOther(String monkeyPoxOther) {
        this.monkeyPoxOther = monkeyPoxOther;
    }

    public void setMonkeypoxAntiBodyDetection(String monkeypoxAntiBodyDetection) {
        this.monkeypoxAntiBodyDetection = monkeypoxAntiBodyDetection;
    }

    public void setMonkeypoxAntigenDetection(String monkeypoxAntigenDetection) {
        this.monkeypoxAntigenDetection = monkeypoxAntigenDetection;
    }

    public void setMonkeypoxRapidTest(String monkeypoxRapidTest) {
        this.monkeypoxRapidTest = monkeypoxRapidTest;
    }

    public void setMonkeypoxCulture(String monkeypoxCulture) {
        this.monkeypoxCulture = monkeypoxCulture;
    }

    public void setMonkeypoxHistopathology(String monkeypoxHistopathology) {
        this.monkeypoxHistopathology = monkeypoxHistopathology;
    }

    public void setMonkeypoxIsolation(String monkeypoxIsolation) {
        this.monkeypoxIsolation = monkeypoxIsolation;
    }

    public void setMonkeypoxIgmSerumAntibody(String monkeypoxIgmSerumAntibody) {
        this.monkeypoxIgmSerumAntibody = monkeypoxIgmSerumAntibody;
    }

    public void setMonkeypoxIggSerumAntibody(String monkeypoxIggSerumAntibody) {
        this.monkeypoxIggSerumAntibody = monkeypoxIggSerumAntibody;
    }

    public void setMonkeypoxIgaSerumAntibody(String monkeypoxIgaSerumAntibody) {
        this.monkeypoxIgaSerumAntibody = monkeypoxIgaSerumAntibody;
    }

    public void setMonkeypoxIncubationTime(String monkeypoxIncubationTime) {
        this.monkeypoxIncubationTime = monkeypoxIncubationTime;
    }

    public void setMonkeypoxIndirectFluorescentAntibody(String monkeypoxIndirectFluorescentAntibody) {
        this.monkeypoxIndirectFluorescentAntibody = monkeypoxIndirectFluorescentAntibody;
    }

    public void setMonkeypoxMicroscopy(String monkeypoxMicroscopy) {
        this.monkeypoxMicroscopy = monkeypoxMicroscopy;
    }

    public void setMonkeypoxNeutralizingAntibodies(String monkeypoxNeutralizingAntibodies) {
        this.monkeypoxNeutralizingAntibodies = monkeypoxNeutralizingAntibodies;
    }

    public void setMonkeypoxPcr(String monkeypoxPcr) {
        this.monkeypoxPcr = monkeypoxPcr;
    }

    public void setMonkeypoxGramStain(String monkeypoxGramStain) {
        this.monkeypoxGramStain = monkeypoxGramStain;
    }

    public void setMonkeypoxLatexAgglutination(String monkeypoxLatexAgglutination) {
        this.monkeypoxLatexAgglutination = monkeypoxLatexAgglutination;
    }

    public void setMonkeypoxCqValueDetection(String monkeypoxCqValueDetection) {
        this.monkeypoxCqValueDetection = monkeypoxCqValueDetection;
    }

    public void setMonkeypoxSeQuencing(String monkeypoxSeQuencing) {
        this.monkeypoxSeQuencing = monkeypoxSeQuencing;
    }

    public void setMonkeypoxDnaMicroArray(String monkeypoxDnaMicroArray) {
        this.monkeypoxDnaMicroArray = monkeypoxDnaMicroArray;
    }

    public void setMonkeypoxOther(String monkeypoxOther) {
        this.monkeypoxOther = monkeypoxOther;
    }

    public void setNewInfluenzaAntiBodyDetection(String newInfluenzaAntiBodyDetection) {
        this.newInfluenzaAntiBodyDetection = newInfluenzaAntiBodyDetection;
    }

    public void setNewInfluenzaAntigenDetection(String newInfluenzaAntigenDetection) {
        this.newInfluenzaAntigenDetection = newInfluenzaAntigenDetection;
    }

    public void setNewInfluenzaRapidTest(String newInfluenzaRapidTest) {
        this.newInfluenzaRapidTest = newInfluenzaRapidTest;
    }

    public void setNewInfluenzaCulture(String newInfluenzaCulture) {
        this.newInfluenzaCulture = newInfluenzaCulture;
    }

    public void setNewInfluenzaHistopathology(String newInfluenzaHistopathology) {
        this.newInfluenzaHistopathology = newInfluenzaHistopathology;
    }

    public void setNewInfluenzaIsolation(String newInfluenzaIsolation) {
        this.newInfluenzaIsolation = newInfluenzaIsolation;
    }

    public void setNewInfluenzaIgmSerumAntibody(String newInfluenzaIgmSerumAntibody) {
        this.newInfluenzaIgmSerumAntibody = newInfluenzaIgmSerumAntibody;
    }

    public void setNewInfluenzaIggSerumAntibody(String newInfluenzaIggSerumAntibody) {
        this.newInfluenzaIggSerumAntibody = newInfluenzaIggSerumAntibody;
    }

    public void setNewInfluenzaIgaSerumAntibody(String newInfluenzaIgaSerumAntibody) {
        this.newInfluenzaIgaSerumAntibody = newInfluenzaIgaSerumAntibody;
    }

    public void setNewInfluenzaIncubationTime(String newInfluenzaIncubationTime) {
        this.newInfluenzaIncubationTime = newInfluenzaIncubationTime;
    }

    public void setNewInfluenzaIndirectFluorescentAntibody(String newInfluenzaIndirectFluorescentAntibody) {
        this.newInfluenzaIndirectFluorescentAntibody = newInfluenzaIndirectFluorescentAntibody;
    }

    public void setNewInfluenzaMicroscopy(String newInfluenzaMicroscopy) {
        this.newInfluenzaMicroscopy = newInfluenzaMicroscopy;
    }

    public void setNewInfluenzaNeutralizingAntibodies(String newInfluenzaNeutralizingAntibodies) {
        this.newInfluenzaNeutralizingAntibodies = newInfluenzaNeutralizingAntibodies;
    }

    public void setNewInfluenzaPcr(String newInfluenzaPcr) {
        this.newInfluenzaPcr = newInfluenzaPcr;
    }

    public void setNewInfluenzaGramStain(String newInfluenzaGramStain) {
        this.newInfluenzaGramStain = newInfluenzaGramStain;
    }

    public void setNewInfluenzaLatexAgglutination(String newInfluenzaLatexAgglutination) {
        this.newInfluenzaLatexAgglutination = newInfluenzaLatexAgglutination;
    }

    public void setNewInfluenzaCqValueDetection(String newInfluenzaCqValueDetection) {
        this.newInfluenzaCqValueDetection = newInfluenzaCqValueDetection;
    }

    public void setNewInfluenzaSeQuencing(String newInfluenzaSeQuencing) {
        this.newInfluenzaSeQuencing = newInfluenzaSeQuencing;
    }

    public void setNewInfluenzaDnaMicroArray(String newInfluenzaDnaMicroArray) {
        this.newInfluenzaDnaMicroArray = newInfluenzaDnaMicroArray;
    }

    public void setNewInfluenzaOther(String newInfluenzaOther) {
        this.newInfluenzaOther = newInfluenzaOther;
    }

    public void setPlagueAntiBodyDetection(String plagueAntiBodyDetection) {
        this.plagueAntiBodyDetection = plagueAntiBodyDetection;
    }

    public void setPlagueAntigenDetection(String plagueAntigenDetection) {
        this.plagueAntigenDetection = plagueAntigenDetection;
    }

    public void setPlagueRapidTest(String plagueRapidTest) {
        this.plagueRapidTest = plagueRapidTest;
    }

    public void setPlagueCulture(String plagueCulture) {
        this.plagueCulture = plagueCulture;
    }

    public void setPlagueHistopathology(String plagueHistopathology) {
        this.plagueHistopathology = plagueHistopathology;
    }

    public void setPlagueIsolation(String plagueIsolation) {
        this.plagueIsolation = plagueIsolation;
    }

    public void setPlagueIgmSerumAntibody(String plagueIgmSerumAntibody) {
        this.plagueIgmSerumAntibody = plagueIgmSerumAntibody;
    }

    public void setPlagueIggSerumAntibody(String plagueIggSerumAntibody) {
        this.plagueIggSerumAntibody = plagueIggSerumAntibody;
    }

    public void setPlagueIgaSerumAntibody(String plagueIgaSerumAntibody) {
        this.plagueIgaSerumAntibody = plagueIgaSerumAntibody;
    }

    public void setPlagueIncubationTime(String plagueIncubationTime) {
        this.plagueIncubationTime = plagueIncubationTime;
    }

    public void setPlagueIndirectFluorescentAntibody(String plagueIndirectFluorescentAntibody) {
        this.plagueIndirectFluorescentAntibody = plagueIndirectFluorescentAntibody;
    }

    public void setPlagueMicroscopy(String plagueMicroscopy) {
        this.plagueMicroscopy = plagueMicroscopy;
    }

    public void setPlagueNeutralizingAntibodies(String plagueNeutralizingAntibodies) {
        this.plagueNeutralizingAntibodies = plagueNeutralizingAntibodies;
    }

    public void setPlaguePcr(String plaguePcr) {
        this.plaguePcr = plaguePcr;
    }

    public void setPlagueGramStain(String plagueGramStain) {
        this.plagueGramStain = plagueGramStain;
    }

    public void setPlagueLatexAgglutination(String plagueLatexAgglutination) {
        this.plagueLatexAgglutination = plagueLatexAgglutination;
    }

    public void setPlagueCqValueDetection(String plagueCqValueDetection) {
        this.plagueCqValueDetection = plagueCqValueDetection;
    }

    public void setPlagueSeQuencing(String plagueSeQuencing) {
        this.plagueSeQuencing = plagueSeQuencing;
    }

    public void setPlagueDnaMicroArray(String plagueDnaMicroArray) {
        this.plagueDnaMicroArray = plagueDnaMicroArray;
    }

    public void setPlagueOther(String plagueOther) {
        this.plagueOther = plagueOther;
    }

    public void setPolioAntiBodyDetection(String polioAntiBodyDetection) {
        this.polioAntiBodyDetection = polioAntiBodyDetection;
    }

    public void setPolioAntigenDetection(String polioAntigenDetection) {
        this.polioAntigenDetection = polioAntigenDetection;
    }

    public void setPolioRapidTest(String polioRapidTest) {
        this.polioRapidTest = polioRapidTest;
    }

    public void setPolioCulture(String polioCulture) {
        this.polioCulture = polioCulture;
    }

    public void setPolioHistopathology(String polioHistopathology) {
        this.polioHistopathology = polioHistopathology;
    }

    public void setPolioIsolation(String polioIsolation) {
        this.polioIsolation = polioIsolation;
    }

    public void setPolioIgmSerumAntibody(String polioIgmSerumAntibody) {
        this.polioIgmSerumAntibody = polioIgmSerumAntibody;
    }

    public void setPolioIggSerumAntibody(String polioIggSerumAntibody) {
        this.polioIggSerumAntibody = polioIggSerumAntibody;
    }

    public void setPolioIgaSerumAntibody(String polioIgaSerumAntibody) {
        this.polioIgaSerumAntibody = polioIgaSerumAntibody;
    }

    public void setPolioIncubationTime(String polioIncubationTime) {
        this.polioIncubationTime = polioIncubationTime;
    }

    public void setPolioIndirectFluorescentAntibody(String polioIndirectFluorescentAntibody) {
        this.polioIndirectFluorescentAntibody = polioIndirectFluorescentAntibody;
    }

    public void setPolioMicroscopy(String polioMicroscopy) {
        this.polioMicroscopy = polioMicroscopy;
    }

    public void setPolioNeutralizingAntibodies(String polioNeutralizingAntibodies) {
        this.polioNeutralizingAntibodies = polioNeutralizingAntibodies;
    }

    public void setPolioPcr(String polioPcr) {
        this.polioPcr = polioPcr;
    }

    public void setPolioGramStain(String polioGramStain) {
        this.polioGramStain = polioGramStain;
    }

    public void setPolioLatexAgglutination(String polioLatexAgglutination) {
        this.polioLatexAgglutination = polioLatexAgglutination;
    }

    public void setPolioCqValueDetection(String polioCqValueDetection) {
        this.polioCqValueDetection = polioCqValueDetection;
    }

    public void setPolioSeQuencing(String polioSeQuencing) {
        this.polioSeQuencing = polioSeQuencing;
    }

    public void setPolioDnaMicroArray(String polioDnaMicroArray) {
        this.polioDnaMicroArray = polioDnaMicroArray;
    }

    public void setPolioOther(String polioOther) {
        this.polioOther = polioOther;
    }

    public void setUnspecifiedVhfAntiBodyDetection(String unspecifiedVhfAntiBodyDetection) {
        this.unspecifiedVhfAntiBodyDetection = unspecifiedVhfAntiBodyDetection;
    }

    public void setUnspecifiedVhfAntigenDetection(String unspecifiedVhfAntigenDetection) {
        this.unspecifiedVhfAntigenDetection = unspecifiedVhfAntigenDetection;
    }

    public void setUnspecifiedVhfRapidTest(String unspecifiedVhfRapidTest) {
        this.unspecifiedVhfRapidTest = unspecifiedVhfRapidTest;
    }

    public void setUnspecifiedVhfCulture(String unspecifiedVhfCulture) {
        this.unspecifiedVhfCulture = unspecifiedVhfCulture;
    }

    public void setUnspecifiedVhfHistopathology(String unspecifiedVhfHistopathology) {
        this.unspecifiedVhfHistopathology = unspecifiedVhfHistopathology;
    }

    public void setUnspecifiedVhfIsolation(String unspecifiedVhfIsolation) {
        this.unspecifiedVhfIsolation = unspecifiedVhfIsolation;
    }

    public void setUnspecifiedVhfIgmSerumAntibody(String unspecifiedVhfIgmSerumAntibody) {
        this.unspecifiedVhfIgmSerumAntibody = unspecifiedVhfIgmSerumAntibody;
    }

    public void setUnspecifiedVhfIggSerumAntibody(String unspecifiedVhfIggSerumAntibody) {
        this.unspecifiedVhfIggSerumAntibody = unspecifiedVhfIggSerumAntibody;
    }

    public void setUnspecifiedVhfIgaSerumAntibody(String unspecifiedVhfIgaSerumAntibody) {
        this.unspecifiedVhfIgaSerumAntibody = unspecifiedVhfIgaSerumAntibody;
    }

    public void setUnspecifiedVhfIncubationTime(String unspecifiedVhfIncubationTime) {
        this.unspecifiedVhfIncubationTime = unspecifiedVhfIncubationTime;
    }

    public void setUnspecifiedVhfIndirectFluorescentAntibody(String unspecifiedVhfIndirectFluorescentAntibody) {
        this.unspecifiedVhfIndirectFluorescentAntibody = unspecifiedVhfIndirectFluorescentAntibody;
    }

    public void setUnspecifiedVhfMicroscopy(String unspecifiedVhfMicroscopy) {
        this.unspecifiedVhfMicroscopy = unspecifiedVhfMicroscopy;
    }

    public void setUnspecifiedVhfNeutralizingAntibodies(String unspecifiedVhfNeutralizingAntibodies) {
        this.unspecifiedVhfNeutralizingAntibodies = unspecifiedVhfNeutralizingAntibodies;
    }

    public void setUnspecifiedVhfPcr(String unspecifiedVhfPcr) {
        this.unspecifiedVhfPcr = unspecifiedVhfPcr;
    }

    public void setUnspecifiedVhfGramStain(String unspecifiedVhfGramStain) {
        this.unspecifiedVhfGramStain = unspecifiedVhfGramStain;
    }

    public void setUnspecifiedVhfLatexAgglutination(String unspecifiedVhfLatexAgglutination) {
        this.unspecifiedVhfLatexAgglutination = unspecifiedVhfLatexAgglutination;
    }

    public void setUnspecifiedVhfCqValueDetection(String unspecifiedVhfCqValueDetection) {
        this.unspecifiedVhfCqValueDetection = unspecifiedVhfCqValueDetection;
    }

    public void setUnspecifiedVhfSeQuencing(String unspecifiedVhfSeQuencing) {
        this.unspecifiedVhfSeQuencing = unspecifiedVhfSeQuencing;
    }

    public void setUnspecifiedVhfDnaMicroArray(String unspecifiedVhfDnaMicroArray) {
        this.unspecifiedVhfDnaMicroArray = unspecifiedVhfDnaMicroArray;
    }

    public void setUnspecifiedVhfOther(String unspecifiedVhfOther) {
        this.unspecifiedVhfOther = unspecifiedVhfOther;
    }

    public void setYellowFeverAntiBodyDetection(String yellowFeverAntiBodyDetection) {
        this.yellowFeverAntiBodyDetection = yellowFeverAntiBodyDetection;
    }

    public void setYellowFeverAntigenDetection(String yellowFeverAntigenDetection) {
        this.yellowFeverAntigenDetection = yellowFeverAntigenDetection;
    }

    public void setYellowFeverRapidTest(String yellowFeverRapidTest) {
        this.yellowFeverRapidTest = yellowFeverRapidTest;
    }

    public void setYellowFeverCulture(String yellowFeverCulture) {
        this.yellowFeverCulture = yellowFeverCulture;
    }

    public void setYellowFeverHistopathology(String yellowFeverHistopathology) {
        this.yellowFeverHistopathology = yellowFeverHistopathology;
    }

    public void setYellowFeverIsolation(String yellowFeverIsolation) {
        this.yellowFeverIsolation = yellowFeverIsolation;
    }

    public void setYellowFeverIgmSerumAntibody(String yellowFeverIgmSerumAntibody) {
        this.yellowFeverIgmSerumAntibody = yellowFeverIgmSerumAntibody;
    }

    public void setYellowFeverIggSerumAntibody(String yellowFeverIggSerumAntibody) {
        this.yellowFeverIggSerumAntibody = yellowFeverIggSerumAntibody;
    }

    public void setYellowFeverIgaSerumAntibody(String yellowFeverIgaSerumAntibody) {
        this.yellowFeverIgaSerumAntibody = yellowFeverIgaSerumAntibody;
    }

    public void setYellowFeverIncubationTime(String yellowFeverIncubationTime) {
        this.yellowFeverIncubationTime = yellowFeverIncubationTime;
    }

    public void setYellowFeverIndirectFluorescentAntibody(String yellowFeverIndirectFluorescentAntibody) {
        this.yellowFeverIndirectFluorescentAntibody = yellowFeverIndirectFluorescentAntibody;
    }

    public void setYellowFeverMicroscopy(String yellowFeverMicroscopy) {
        this.yellowFeverMicroscopy = yellowFeverMicroscopy;
    }

    public void setYellowFeverNeutralizingAntibodies(String yellowFeverNeutralizingAntibodies) {
        this.yellowFeverNeutralizingAntibodies = yellowFeverNeutralizingAntibodies;
    }

    public void setYellowFeverPcr(String yellowFeverPcr) {
        this.yellowFeverPcr = yellowFeverPcr;
    }

    public void setYellowFeverGramStain(String yellowFeverGramStain) {
        this.yellowFeverGramStain = yellowFeverGramStain;
    }

    public void setYellowFeverLatexAgglutination(String yellowFeverLatexAgglutination) {
        this.yellowFeverLatexAgglutination = yellowFeverLatexAgglutination;
    }

    public void setYellowFeverCqValueDetection(String yellowFeverCqValueDetection) {
        this.yellowFeverCqValueDetection = yellowFeverCqValueDetection;
    }

    public void setYellowFeverSeQuencing(String yellowFeverSeQuencing) {
        this.yellowFeverSeQuencing = yellowFeverSeQuencing;
    }

    public void setYellowFeverDnaMicroArray(String yellowFeverDnaMicroArray) {
        this.yellowFeverDnaMicroArray = yellowFeverDnaMicroArray;
    }

    public void setYellowFeverOther(String yellowFeverOther) {
        this.yellowFeverOther = yellowFeverOther;
    }

    public void setRabiesAntiBodyDetection(String rabiesAntiBodyDetection) {
        this.rabiesAntiBodyDetection = rabiesAntiBodyDetection;
    }

    public void setRabiesAntigenDetection(String rabiesAntigenDetection) {
        this.rabiesAntigenDetection = rabiesAntigenDetection;
    }

    public void setRabiesRapidTest(String rabiesRapidTest) {
        this.rabiesRapidTest = rabiesRapidTest;
    }

    public void setRabiesCulture(String rabiesCulture) {
        this.rabiesCulture = rabiesCulture;
    }

    public void setRabiesHistopathology(String rabiesHistopathology) {
        this.rabiesHistopathology = rabiesHistopathology;
    }

    public void setRabiesIsolation(String rabiesIsolation) {
        this.rabiesIsolation = rabiesIsolation;
    }

    public void setRabiesIgmSerumAntibody(String rabiesIgmSerumAntibody) {
        this.rabiesIgmSerumAntibody = rabiesIgmSerumAntibody;
    }

    public void setRabiesIggSerumAntibody(String rabiesIggSerumAntibody) {
        this.rabiesIggSerumAntibody = rabiesIggSerumAntibody;
    }

    public void setRabiesIgaSerumAntibody(String rabiesIgaSerumAntibody) {
        this.rabiesIgaSerumAntibody = rabiesIgaSerumAntibody;
    }

    public void setRabiesIncubationTime(String rabiesIncubationTime) {
        this.rabiesIncubationTime = rabiesIncubationTime;
    }

    public void setRabiesIndirectFluorescentAntibody(String rabiesIndirectFluorescentAntibody) {
        this.rabiesIndirectFluorescentAntibody = rabiesIndirectFluorescentAntibody;
    }

    public void setRabiesMicroscopy(String rabiesMicroscopy) {
        this.rabiesMicroscopy = rabiesMicroscopy;
    }

    public void setRabiesNeutralizingAntibodies(String rabiesNeutralizingAntibodies) {
        this.rabiesNeutralizingAntibodies = rabiesNeutralizingAntibodies;
    }

    public void setRabiesPcr(String rabiesPcr) {
        this.rabiesPcr = rabiesPcr;
    }

    public void setRabiesGramStain(String rabiesGramStain) {
        this.rabiesGramStain = rabiesGramStain;
    }

    public void setRabiesLatexAgglutination(String rabiesLatexAgglutination) {
        this.rabiesLatexAgglutination = rabiesLatexAgglutination;
    }

    public void setRabiesCqValueDetection(String rabiesCqValueDetection) {
        this.rabiesCqValueDetection = rabiesCqValueDetection;
    }

    public void setRabiesSeQuencing(String rabiesSeQuencing) {
        this.rabiesSeQuencing = rabiesSeQuencing;
    }

    public void setRabiesDnaMicroArray(String rabiesDnaMicroArray) {
        this.rabiesDnaMicroArray = rabiesDnaMicroArray;
    }

    public void setRabiesOther(String rabiesOther) {
        this.rabiesOther = rabiesOther;
    }

    public void setAnthraxAntiBodyDetection(String anthraxAntiBodyDetection) {
        this.anthraxAntiBodyDetection = anthraxAntiBodyDetection;
    }

    public void setAnthraxAntigenDetection(String anthraxAntigenDetection) {
        this.anthraxAntigenDetection = anthraxAntigenDetection;
    }

    public void setAnthraxRapidTest(String anthraxRapidTest) {
        this.anthraxRapidTest = anthraxRapidTest;
    }

    public void setAnthraxCulture(String anthraxCulture) {
        this.anthraxCulture = anthraxCulture;
    }

    public void setAnthraxHistopathology(String anthraxHistopathology) {
        this.anthraxHistopathology = anthraxHistopathology;
    }

    public void setAnthraxIsolation(String anthraxIsolation) {
        this.anthraxIsolation = anthraxIsolation;
    }

    public void setAnthraxIgmSerumAntibody(String anthraxIgmSerumAntibody) {
        this.anthraxIgmSerumAntibody = anthraxIgmSerumAntibody;
    }

    public void setAnthraxIggSerumAntibody(String anthraxIggSerumAntibody) {
        this.anthraxIggSerumAntibody = anthraxIggSerumAntibody;
    }

    public void setAnthraxIgaSerumAntibody(String anthraxIgaSerumAntibody) {
        this.anthraxIgaSerumAntibody = anthraxIgaSerumAntibody;
    }

    public void setAnthraxIncubationTime(String anthraxIncubationTime) {
        this.anthraxIncubationTime = anthraxIncubationTime;
    }

    public void setAnthraxIndirectFluorescentAntibody(String anthraxIndirectFluorescentAntibody) {
        this.anthraxIndirectFluorescentAntibody = anthraxIndirectFluorescentAntibody;
    }

    public void setAnthraxMicroscopy(String anthraxMicroscopy) {
        this.anthraxMicroscopy = anthraxMicroscopy;
    }

    public void setAnthraxNeutralizingAntibodies(String anthraxNeutralizingAntibodies) {
        this.anthraxNeutralizingAntibodies = anthraxNeutralizingAntibodies;
    }

    public void setAnthraxPcr(String anthraxPcr) {
        this.anthraxPcr = anthraxPcr;
    }

    public void setAnthraxGramStain(String anthraxGramStain) {
        this.anthraxGramStain = anthraxGramStain;
    }

    public void setAnthraxLatexAgglutination(String anthraxLatexAgglutination) {
        this.anthraxLatexAgglutination = anthraxLatexAgglutination;
    }

    public void setAnthraxCqValueDetection(String anthraxCqValueDetection) {
        this.anthraxCqValueDetection = anthraxCqValueDetection;
    }

    public void setAnthraxSeQuencing(String anthraxSeQuencing) {
        this.anthraxSeQuencing = anthraxSeQuencing;
    }

    public void setAnthraxDnaMicroArray(String anthraxDnaMicroArray) {
        this.anthraxDnaMicroArray = anthraxDnaMicroArray;
    }

    public void setAnthraxOther(String anthraxOther) {
        this.anthraxOther = anthraxOther;
    }

    public void setCoronavirusAntiBodyDetection(String coronavirusAntiBodyDetection) {
        this.coronavirusAntiBodyDetection = coronavirusAntiBodyDetection;
    }

    public void setCoronavirusAntigenDetection(String coronavirusAntigenDetection) {
        this.coronavirusAntigenDetection = coronavirusAntigenDetection;
    }

    public void setCoronavirusRapidTest(String coronavirusRapidTest) {
        this.coronavirusRapidTest = coronavirusRapidTest;
    }

    public void setCoronavirusCulture(String coronavirusCulture) {
        this.coronavirusCulture = coronavirusCulture;
    }

    public void setCoronavirusHistopathology(String coronavirusHistopathology) {
        this.coronavirusHistopathology = coronavirusHistopathology;
    }

    public void setCoronavirusIsolation(String coronavirusIsolation) {
        this.coronavirusIsolation = coronavirusIsolation;
    }

    public void setCoronavirusIgmSerumAntibody(String coronavirusIgmSerumAntibody) {
        this.coronavirusIgmSerumAntibody = coronavirusIgmSerumAntibody;
    }

    public void setCoronavirusIggSerumAntibody(String coronavirusIggSerumAntibody) {
        this.coronavirusIggSerumAntibody = coronavirusIggSerumAntibody;
    }

    public void setCoronavirusIgaSerumAntibody(String coronavirusIgaSerumAntibody) {
        this.coronavirusIgaSerumAntibody = coronavirusIgaSerumAntibody;
    }

    public void setCoronavirusIncubationTime(String coronavirusIncubationTime) {
        this.coronavirusIncubationTime = coronavirusIncubationTime;
    }

    public void setCoronavirusIndirectFluorescentAntibody(String coronavirusIndirectFluorescentAntibody) {
        this.coronavirusIndirectFluorescentAntibody = coronavirusIndirectFluorescentAntibody;
    }

    public void setCoronavirusMicroscopy(String coronavirusMicroscopy) {
        this.coronavirusMicroscopy = coronavirusMicroscopy;
    }

    public void setCoronavirusNeutralizingAntibodies(String coronavirusNeutralizingAntibodies) {
        this.coronavirusNeutralizingAntibodies = coronavirusNeutralizingAntibodies;
    }

    public void setCoronavirusPcr(String coronavirusPcr) {
        this.coronavirusPcr = coronavirusPcr;
    }

    public void setCoronavirusGramStain(String coronavirusGramStain) {
        this.coronavirusGramStain = coronavirusGramStain;
    }

    public void setCoronavirusLatexAgglutination(String coronavirusLatexAgglutination) {
        this.coronavirusLatexAgglutination = coronavirusLatexAgglutination;
    }

    public void setCoronavirusCqValueDetection(String coronavirusCqValueDetection) {
        this.coronavirusCqValueDetection = coronavirusCqValueDetection;
    }

    public void setCoronavirusSeQuencing(String coronavirusSeQuencing) {
        this.coronavirusSeQuencing = coronavirusSeQuencing;
    }

    public void setCoronavirusDnaMicroArray(String coronavirusDnaMicroArray) {
        this.coronavirusDnaMicroArray = coronavirusDnaMicroArray;
    }

    public void setCoronavirusOther(String coronavirusOther) {
        this.coronavirusOther = coronavirusOther;
    }

    public void setPneumoniaAntiBodyDetection(String pneumoniaAntiBodyDetection) {
        this.pneumoniaAntiBodyDetection = pneumoniaAntiBodyDetection;
    }

    public void setPneumoniaAntigenDetection(String pneumoniaAntigenDetection) {
        this.pneumoniaAntigenDetection = pneumoniaAntigenDetection;
    }

    public void setPneumoniaRapidTest(String pneumoniaRapidTest) {
        this.pneumoniaRapidTest = pneumoniaRapidTest;
    }

    public void setPneumoniaCulture(String pneumoniaCulture) {
        this.pneumoniaCulture = pneumoniaCulture;
    }

    public void setPneumoniaHistopathology(String pneumoniaHistopathology) {
        this.pneumoniaHistopathology = pneumoniaHistopathology;
    }

    public void setPneumoniaIsolation(String pneumoniaIsolation) {
        this.pneumoniaIsolation = pneumoniaIsolation;
    }

    public void setPneumoniaIgmSerumAntibody(String pneumoniaIgmSerumAntibody) {
        this.pneumoniaIgmSerumAntibody = pneumoniaIgmSerumAntibody;
    }

    public void setPneumoniaIggSerumAntibody(String pneumoniaIggSerumAntibody) {
        this.pneumoniaIggSerumAntibody = pneumoniaIggSerumAntibody;
    }

    public void setPneumoniaIgaSerumAntibody(String pneumoniaIgaSerumAntibody) {
        this.pneumoniaIgaSerumAntibody = pneumoniaIgaSerumAntibody;
    }

    public void setPneumoniaIncubationTime(String pneumoniaIncubationTime) {
        this.pneumoniaIncubationTime = pneumoniaIncubationTime;
    }

    public void setPneumoniaIndirectFluorescentAntibody(String pneumoniaIndirectFluorescentAntibody) {
        this.pneumoniaIndirectFluorescentAntibody = pneumoniaIndirectFluorescentAntibody;
    }

    public void setPneumoniaMicroscopy(String pneumoniaMicroscopy) {
        this.pneumoniaMicroscopy = pneumoniaMicroscopy;
    }

    public void setPneumoniaNeutralizingAntibodies(String pneumoniaNeutralizingAntibodies) {
        this.pneumoniaNeutralizingAntibodies = pneumoniaNeutralizingAntibodies;
    }

    public void setPneumoniaPcr(String pneumoniaPcr) {
        this.pneumoniaPcr = pneumoniaPcr;
    }

    public void setPneumoniaGramStain(String pneumoniaGramStain) {
        this.pneumoniaGramStain = pneumoniaGramStain;
    }

    public void setPneumoniaLatexAgglutination(String pneumoniaLatexAgglutination) {
        this.pneumoniaLatexAgglutination = pneumoniaLatexAgglutination;
    }

    public void setPneumoniaCqValueDetection(String pneumoniaCqValueDetection) {
        this.pneumoniaCqValueDetection = pneumoniaCqValueDetection;
    }

    public void setPneumoniaSeQuencing(String pneumoniaSeQuencing) {
        this.pneumoniaSeQuencing = pneumoniaSeQuencing;
    }

    public void setPneumoniaDnaMicroArray(String pneumoniaDnaMicroArray) {
        this.pneumoniaDnaMicroArray = pneumoniaDnaMicroArray;
    }

    public void setPneumoniaOther(String pneumoniaOther) {
        this.pneumoniaOther = pneumoniaOther;
    }

    public void setMalariaAntiBodyDetection(String malariaAntiBodyDetection) {
        this.malariaAntiBodyDetection = malariaAntiBodyDetection;
    }

    public void setMalariaAntigenDetection(String malariaAntigenDetection) {
        this.malariaAntigenDetection = malariaAntigenDetection;
    }

    public void setMalariaRapidTest(String malariaRapidTest) {
        this.malariaRapidTest = malariaRapidTest;
    }

    public void setMalariaCulture(String malariaCulture) {
        this.malariaCulture = malariaCulture;
    }

    public void setMalariaHistopathology(String malariaHistopathology) {
        this.malariaHistopathology = malariaHistopathology;
    }

    public void setMalariaIsolation(String malariaIsolation) {
        this.malariaIsolation = malariaIsolation;
    }

    public void setMalariaIgmSerumAntibody(String malariaIgmSerumAntibody) {
        this.malariaIgmSerumAntibody = malariaIgmSerumAntibody;
    }

    public void setMalariaIggSerumAntibody(String malariaIggSerumAntibody) {
        this.malariaIggSerumAntibody = malariaIggSerumAntibody;
    }

    public void setMalariaIgaSerumAntibody(String malariaIgaSerumAntibody) {
        this.malariaIgaSerumAntibody = malariaIgaSerumAntibody;
    }

    public void setMalariaIncubationTime(String malariaIncubationTime) {
        this.malariaIncubationTime = malariaIncubationTime;
    }

    public void setMalariaIndirectFluorescentAntibody(String malariaIndirectFluorescentAntibody) {
        this.malariaIndirectFluorescentAntibody = malariaIndirectFluorescentAntibody;
    }

    public void setMalariaMicroscopy(String malariaMicroscopy) {
        this.malariaMicroscopy = malariaMicroscopy;
    }

    public void setMalariaNeutralizingAntibodies(String malariaNeutralizingAntibodies) {
        this.malariaNeutralizingAntibodies = malariaNeutralizingAntibodies;
    }

    public void setMalariaPcr(String malariaPcr) {
        this.malariaPcr = malariaPcr;
    }

    public void setMalariaGramStain(String malariaGramStain) {
        this.malariaGramStain = malariaGramStain;
    }

    public void setMalariaLatexAgglutination(String malariaLatexAgglutination) {
        this.malariaLatexAgglutination = malariaLatexAgglutination;
    }

    public void setMalariaCqValueDetection(String malariaCqValueDetection) {
        this.malariaCqValueDetection = malariaCqValueDetection;
    }

    public void setMalariaSeQuencing(String malariaSeQuencing) {
        this.malariaSeQuencing = malariaSeQuencing;
    }

    public void setMalariaDnaMicroArray(String malariaDnaMicroArray) {
        this.malariaDnaMicroArray = malariaDnaMicroArray;
    }

    public void setMalariaOther(String malariaOther) {
        this.malariaOther = malariaOther;
    }

    public void setTyphoidFeverAntiBodyDetection(String typhoidFeverAntiBodyDetection) {
        this.typhoidFeverAntiBodyDetection = typhoidFeverAntiBodyDetection;
    }

    public void setTyphoidFeverAntigenDetection(String typhoidFeverAntigenDetection) {
        this.typhoidFeverAntigenDetection = typhoidFeverAntigenDetection;
    }

    public void setTyphoidFeverRapidTest(String typhoidFeverRapidTest) {
        this.typhoidFeverRapidTest = typhoidFeverRapidTest;
    }

    public void setTyphoidFeverCulture(String typhoidFeverCulture) {
        this.typhoidFeverCulture = typhoidFeverCulture;
    }

    public void setTyphoidFeverHistopathology(String typhoidFeverHistopathology) {
        this.typhoidFeverHistopathology = typhoidFeverHistopathology;
    }

    public void setTyphoidFeverIsolation(String typhoidFeverIsolation) {
        this.typhoidFeverIsolation = typhoidFeverIsolation;
    }

    public void setTyphoidFeverIgmSerumAntibody(String typhoidFeverIgmSerumAntibody) {
        this.typhoidFeverIgmSerumAntibody = typhoidFeverIgmSerumAntibody;
    }

    public void setTyphoidFeverIggSerumAntibody(String typhoidFeverIggSerumAntibody) {
        this.typhoidFeverIggSerumAntibody = typhoidFeverIggSerumAntibody;
    }

    public void setTyphoidFeverIgaSerumAntibody(String typhoidFeverIgaSerumAntibody) {
        this.typhoidFeverIgaSerumAntibody = typhoidFeverIgaSerumAntibody;
    }

    public void setTyphoidFeverIncubationTime(String typhoidFeverIncubationTime) {
        this.typhoidFeverIncubationTime = typhoidFeverIncubationTime;
    }

    public void setTyphoidFeverIndirectFluorescentAntibody(String typhoidFeverIndirectFluorescentAntibody) {
        this.typhoidFeverIndirectFluorescentAntibody = typhoidFeverIndirectFluorescentAntibody;
    }

    public void setTyphoidFeverMicroscopy(String typhoidFeverMicroscopy) {
        this.typhoidFeverMicroscopy = typhoidFeverMicroscopy;
    }

    public void setTyphoidFeverNeutralizingAntibodies(String typhoidFeverNeutralizingAntibodies) {
        this.typhoidFeverNeutralizingAntibodies = typhoidFeverNeutralizingAntibodies;
    }

    public void setTyphoidFeverPcr(String typhoidFeverPcr) {
        this.typhoidFeverPcr = typhoidFeverPcr;
    }

    public void setTyphoidFeverGramStain(String typhoidFeverGramStain) {
        this.typhoidFeverGramStain = typhoidFeverGramStain;
    }

    public void setTyphoidFeverLatexAgglutination(String typhoidFeverLatexAgglutination) {
        this.typhoidFeverLatexAgglutination = typhoidFeverLatexAgglutination;
    }

    public void setTyphoidFeverCqValueDetection(String typhoidFeverCqValueDetection) {
        this.typhoidFeverCqValueDetection = typhoidFeverCqValueDetection;
    }

    public void setTyphoidFeverSeQuencing(String typhoidFeverSeQuencing) {
        this.typhoidFeverSeQuencing = typhoidFeverSeQuencing;
    }

    public void setTyphoidFeverDnaMicroArray(String typhoidFeverDnaMicroArray) {
        this.typhoidFeverDnaMicroArray = typhoidFeverDnaMicroArray;
    }

    public void setTyphoidFeverOther(String typhoidFeverOther) {
        this.typhoidFeverOther = typhoidFeverOther;
    }

    public void setAcuteViralHepatitisAntiBodyDetection(String acuteViralHepatitisAntiBodyDetection) {
        this.acuteViralHepatitisAntiBodyDetection = acuteViralHepatitisAntiBodyDetection;
    }

    public void setAcuteViralHepatitisAntigenDetection(String acuteViralHepatitisAntigenDetection) {
        this.acuteViralHepatitisAntigenDetection = acuteViralHepatitisAntigenDetection;
    }

    public void setAcuteViralHepatitisRapidTest(String acuteViralHepatitisRapidTest) {
        this.acuteViralHepatitisRapidTest = acuteViralHepatitisRapidTest;
    }

    public void setAcuteViralHepatitisCulture(String acuteViralHepatitisCulture) {
        this.acuteViralHepatitisCulture = acuteViralHepatitisCulture;
    }

    public void setAcuteViralHepatitisHistopathology(String acuteViralHepatitisHistopathology) {
        this.acuteViralHepatitisHistopathology = acuteViralHepatitisHistopathology;
    }

    public void setAcuteViralHepatitisIsolation(String acuteViralHepatitisIsolation) {
        this.acuteViralHepatitisIsolation = acuteViralHepatitisIsolation;
    }

    public void setAcuteViralHepatitisIgmSerumAntibody(String acuteViralHepatitisIgmSerumAntibody) {
        this.acuteViralHepatitisIgmSerumAntibody = acuteViralHepatitisIgmSerumAntibody;
    }

    public void setAcuteViralHepatitisIggSerumAntibody(String acuteViralHepatitisIggSerumAntibody) {
        this.acuteViralHepatitisIggSerumAntibody = acuteViralHepatitisIggSerumAntibody;
    }

    public void setAcuteViralHepatitisIgaSerumAntibody(String acuteViralHepatitisIgaSerumAntibody) {
        this.acuteViralHepatitisIgaSerumAntibody = acuteViralHepatitisIgaSerumAntibody;
    }

    public void setAcuteViralHepatitisIncubationTime(String acuteViralHepatitisIncubationTime) {
        this.acuteViralHepatitisIncubationTime = acuteViralHepatitisIncubationTime;
    }

    public void setAcuteViralHepatitisIndirectFluorescentAntibody(String acuteViralHepatitisIndirectFluorescentAntibody) {
        this.acuteViralHepatitisIndirectFluorescentAntibody = acuteViralHepatitisIndirectFluorescentAntibody;
    }

    public void setAcuteViralHepatitisMicroscopy(String acuteViralHepatitisMicroscopy) {
        this.acuteViralHepatitisMicroscopy = acuteViralHepatitisMicroscopy;
    }

    public void setAcuteViralHepatitisNeutralizingAntibodies(String acuteViralHepatitisNeutralizingAntibodies) {
        this.acuteViralHepatitisNeutralizingAntibodies = acuteViralHepatitisNeutralizingAntibodies;
    }

    public void setAcuteViralHepatitisPcr(String acuteViralHepatitisPcr) {
        this.acuteViralHepatitisPcr = acuteViralHepatitisPcr;
    }

    public void setAcuteViralHepatitisGramStain(String acuteViralHepatitisGramStain) {
        this.acuteViralHepatitisGramStain = acuteViralHepatitisGramStain;
    }

    public void setAcuteViralHepatitisLatexAgglutination(String acuteViralHepatitisLatexAgglutination) {
        this.acuteViralHepatitisLatexAgglutination = acuteViralHepatitisLatexAgglutination;
    }

    public void setAcuteViralHepatitisCqValueDetection(String acuteViralHepatitisCqValueDetection) {
        this.acuteViralHepatitisCqValueDetection = acuteViralHepatitisCqValueDetection;
    }

    public void setAcuteViralHepatitisSeQuencing(String acuteViralHepatitisSeQuencing) {
        this.acuteViralHepatitisSeQuencing = acuteViralHepatitisSeQuencing;
    }

    public void setAcuteViralHepatitisDnaMicroArray(String acuteViralHepatitisDnaMicroArray) {
        this.acuteViralHepatitisDnaMicroArray = acuteViralHepatitisDnaMicroArray;
    }

    public void setAcuteViralHepatitisOther(String acuteViralHepatitisOther) {
        this.acuteViralHepatitisOther = acuteViralHepatitisOther;
    }

    public void setNonNeonatalTetanusAntiBodyDetection(String nonNeonatalTetanusAntiBodyDetection) {
        this.nonNeonatalTetanusAntiBodyDetection = nonNeonatalTetanusAntiBodyDetection;
    }

    public void setNonNeonatalTetanusAntigenDetection(String nonNeonatalTetanusAntigenDetection) {
        this.nonNeonatalTetanusAntigenDetection = nonNeonatalTetanusAntigenDetection;
    }

    public void setNonNeonatalTetanusRapidTest(String nonNeonatalTetanusRapidTest) {
        this.nonNeonatalTetanusRapidTest = nonNeonatalTetanusRapidTest;
    }

    public void setNonNeonatalTetanusCulture(String nonNeonatalTetanusCulture) {
        this.nonNeonatalTetanusCulture = nonNeonatalTetanusCulture;
    }

    public void setNonNeonatalTetanusHistopathology(String nonNeonatalTetanusHistopathology) {
        this.nonNeonatalTetanusHistopathology = nonNeonatalTetanusHistopathology;
    }

    public void setNonNeonatalTetanusIsolation(String nonNeonatalTetanusIsolation) {
        this.nonNeonatalTetanusIsolation = nonNeonatalTetanusIsolation;
    }

    public void setNonNeonatalTetanusIgmSerumAntibody(String nonNeonatalTetanusIgmSerumAntibody) {
        this.nonNeonatalTetanusIgmSerumAntibody = nonNeonatalTetanusIgmSerumAntibody;
    }

    public void setNonNeonatalTetanusIggSerumAntibody(String nonNeonatalTetanusIggSerumAntibody) {
        this.nonNeonatalTetanusIggSerumAntibody = nonNeonatalTetanusIggSerumAntibody;
    }

    public void setNonNeonatalTetanusIgaSerumAntibody(String nonNeonatalTetanusIgaSerumAntibody) {
        this.nonNeonatalTetanusIgaSerumAntibody = nonNeonatalTetanusIgaSerumAntibody;
    }

    public void setNonNeonatalTetanusIncubationTime(String nonNeonatalTetanusIncubationTime) {
        this.nonNeonatalTetanusIncubationTime = nonNeonatalTetanusIncubationTime;
    }

    public void setNonNeonatalTetanusIndirectFluorescentAntibody(String nonNeonatalTetanusIndirectFluorescentAntibody) {
        this.nonNeonatalTetanusIndirectFluorescentAntibody = nonNeonatalTetanusIndirectFluorescentAntibody;
    }

    public void setNonNeonatalTetanusMicroscopy(String nonNeonatalTetanusMicroscopy) {
        this.nonNeonatalTetanusMicroscopy = nonNeonatalTetanusMicroscopy;
    }

    public void setNonNeonatalTetanusNeutralizingAntibodies(String nonNeonatalTetanusNeutralizingAntibodies) {
        this.nonNeonatalTetanusNeutralizingAntibodies = nonNeonatalTetanusNeutralizingAntibodies;
    }

    public void setNonNeonatalTetanusPcr(String nonNeonatalTetanusPcr) {
        this.nonNeonatalTetanusPcr = nonNeonatalTetanusPcr;
    }

    public void setNonNeonatalTetanusGramStain(String nonNeonatalTetanusGramStain) {
        this.nonNeonatalTetanusGramStain = nonNeonatalTetanusGramStain;
    }

    public void setNonNeonatalTetanusLatexAgglutination(String nonNeonatalTetanusLatexAgglutination) {
        this.nonNeonatalTetanusLatexAgglutination = nonNeonatalTetanusLatexAgglutination;
    }

    public void setNonNeonatalTetanusCqValueDetection(String nonNeonatalTetanusCqValueDetection) {
        this.nonNeonatalTetanusCqValueDetection = nonNeonatalTetanusCqValueDetection;
    }

    public void setNonNeonatalTetanusSeQuencing(String nonNeonatalTetanusSeQuencing) {
        this.nonNeonatalTetanusSeQuencing = nonNeonatalTetanusSeQuencing;
    }

    public void setNonNeonatalTetanusDnaMicroArray(String nonNeonatalTetanusDnaMicroArray) {
        this.nonNeonatalTetanusDnaMicroArray = nonNeonatalTetanusDnaMicroArray;
    }

    public void setNonNeonatalTetanusOther(String nonNeonatalTetanusOther) {
        this.nonNeonatalTetanusOther = nonNeonatalTetanusOther;
    }

    public void setHivAntiBodyDetection(String hivAntiBodyDetection) {
        this.hivAntiBodyDetection = hivAntiBodyDetection;
    }

    public void setHivAntigenDetection(String hivAntigenDetection) {
        this.hivAntigenDetection = hivAntigenDetection;
    }

    public void setHivRapidTest(String hivRapidTest) {
        this.hivRapidTest = hivRapidTest;
    }

    public void setHivCulture(String hivCulture) {
        this.hivCulture = hivCulture;
    }

    public void setHivHistopathology(String hivHistopathology) {
        this.hivHistopathology = hivHistopathology;
    }

    public void setHivIsolation(String hivIsolation) {
        this.hivIsolation = hivIsolation;
    }

    public void setHivIgmSerumAntibody(String hivIgmSerumAntibody) {
        this.hivIgmSerumAntibody = hivIgmSerumAntibody;
    }

    public void setHivIggSerumAntibody(String hivIggSerumAntibody) {
        this.hivIggSerumAntibody = hivIggSerumAntibody;
    }

    public void setHivIgaSerumAntibody(String hivIgaSerumAntibody) {
        this.hivIgaSerumAntibody = hivIgaSerumAntibody;
    }

    public void setHivIncubationTime(String hivIncubationTime) {
        this.hivIncubationTime = hivIncubationTime;
    }

    public void setHivIndirectFluorescentAntibody(String hivIndirectFluorescentAntibody) {
        this.hivIndirectFluorescentAntibody = hivIndirectFluorescentAntibody;
    }

    public void setHivMicroscopy(String hivMicroscopy) {
        this.hivMicroscopy = hivMicroscopy;
    }

    public void setHivNeutralizingAntibodies(String hivNeutralizingAntibodies) {
        this.hivNeutralizingAntibodies = hivNeutralizingAntibodies;
    }

    public void setHivPcr(String hivPcr) {
        this.hivPcr = hivPcr;
    }

    public void setHivGramStain(String hivGramStain) {
        this.hivGramStain = hivGramStain;
    }

    public void setHivLatexAgglutination(String hivLatexAgglutination) {
        this.hivLatexAgglutination = hivLatexAgglutination;
    }

    public void setHivCqValueDetection(String hivCqValueDetection) {
        this.hivCqValueDetection = hivCqValueDetection;
    }

    public void setHivSeQuencing(String hivSeQuencing) {
        this.hivSeQuencing = hivSeQuencing;
    }

    public void setHivDnaMicroArray(String hivDnaMicroArray) {
        this.hivDnaMicroArray = hivDnaMicroArray;
    }

    public void setHivOther(String hivOther) {
        this.hivOther = hivOther;
    }

    public void setSchistosomiasisAntiBodyDetection(String schistosomiasisAntiBodyDetection) {
        this.schistosomiasisAntiBodyDetection = schistosomiasisAntiBodyDetection;
    }

    public void setSchistosomiasisAntigenDetection(String schistosomiasisAntigenDetection) {
        this.schistosomiasisAntigenDetection = schistosomiasisAntigenDetection;
    }

    public void setSchistosomiasisRapidTest(String schistosomiasisRapidTest) {
        this.schistosomiasisRapidTest = schistosomiasisRapidTest;
    }

    public void setSchistosomiasisCulture(String schistosomiasisCulture) {
        this.schistosomiasisCulture = schistosomiasisCulture;
    }

    public void setSchistosomiasisHistopathology(String schistosomiasisHistopathology) {
        this.schistosomiasisHistopathology = schistosomiasisHistopathology;
    }

    public void setSchistosomiasisIsolation(String schistosomiasisIsolation) {
        this.schistosomiasisIsolation = schistosomiasisIsolation;
    }

    public void setSchistosomiasisIgmSerumAntibody(String schistosomiasisIgmSerumAntibody) {
        this.schistosomiasisIgmSerumAntibody = schistosomiasisIgmSerumAntibody;
    }

    public void setSchistosomiasisIggSerumAntibody(String schistosomiasisIggSerumAntibody) {
        this.schistosomiasisIggSerumAntibody = schistosomiasisIggSerumAntibody;
    }

    public void setSchistosomiasisIgaSerumAntibody(String schistosomiasisIgaSerumAntibody) {
        this.schistosomiasisIgaSerumAntibody = schistosomiasisIgaSerumAntibody;
    }

    public void setSchistosomiasisIncubationTime(String schistosomiasisIncubationTime) {
        this.schistosomiasisIncubationTime = schistosomiasisIncubationTime;
    }

    public void setSchistosomiasisIndirectFluorescentAntibody(String schistosomiasisIndirectFluorescentAntibody) {
        this.schistosomiasisIndirectFluorescentAntibody = schistosomiasisIndirectFluorescentAntibody;
    }

    public void setSchistosomiasisMicroscopy(String schistosomiasisMicroscopy) {
        this.schistosomiasisMicroscopy = schistosomiasisMicroscopy;
    }

    public void setSchistosomiasisNeutralizingAntibodies(String schistosomiasisNeutralizingAntibodies) {
        this.schistosomiasisNeutralizingAntibodies = schistosomiasisNeutralizingAntibodies;
    }

    public void setSchistosomiasisPcr(String schistosomiasisPcr) {
        this.schistosomiasisPcr = schistosomiasisPcr;
    }

    public void setSchistosomiasisGramStain(String schistosomiasisGramStain) {
        this.schistosomiasisGramStain = schistosomiasisGramStain;
    }

    public void setSchistosomiasisLatexAgglutination(String schistosomiasisLatexAgglutination) {
        this.schistosomiasisLatexAgglutination = schistosomiasisLatexAgglutination;
    }

    public void setSchistosomiasisCqValueDetection(String schistosomiasisCqValueDetection) {
        this.schistosomiasisCqValueDetection = schistosomiasisCqValueDetection;
    }

    public void setSchistosomiasisSeQuencing(String schistosomiasisSeQuencing) {
        this.schistosomiasisSeQuencing = schistosomiasisSeQuencing;
    }

    public void setSchistosomiasisDnaMicroArray(String schistosomiasisDnaMicroArray) {
        this.schistosomiasisDnaMicroArray = schistosomiasisDnaMicroArray;
    }

    public void setSchistosomiasisOther(String schistosomiasisOther) {
        this.schistosomiasisOther = schistosomiasisOther;
    }

    public void setSoilTransmittedHelminthsAntiBodyDetection(String soilTransmittedHelminthsAntiBodyDetection) {
        this.soilTransmittedHelminthsAntiBodyDetection = soilTransmittedHelminthsAntiBodyDetection;
    }

    public void setSoilTransmittedHelminthsAntigenDetection(String soilTransmittedHelminthsAntigenDetection) {
        this.soilTransmittedHelminthsAntigenDetection = soilTransmittedHelminthsAntigenDetection;
    }

    public void setSoilTransmittedHelminthsRapidTest(String soilTransmittedHelminthsRapidTest) {
        this.soilTransmittedHelminthsRapidTest = soilTransmittedHelminthsRapidTest;
    }

    public void setSoilTransmittedHelminthsCulture(String soilTransmittedHelminthsCulture) {
        this.soilTransmittedHelminthsCulture = soilTransmittedHelminthsCulture;
    }

    public void setSoilTransmittedHelminthsHistopathology(String soilTransmittedHelminthsHistopathology) {
        this.soilTransmittedHelminthsHistopathology = soilTransmittedHelminthsHistopathology;
    }

    public void setSoilTransmittedHelminthsIsolation(String soilTransmittedHelminthsIsolation) {
        this.soilTransmittedHelminthsIsolation = soilTransmittedHelminthsIsolation;
    }

    public void setSoilTransmittedHelminthsIgmSerumAntibody(String soilTransmittedHelminthsIgmSerumAntibody) {
        this.soilTransmittedHelminthsIgmSerumAntibody = soilTransmittedHelminthsIgmSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIggSerumAntibody(String soilTransmittedHelminthsIggSerumAntibody) {
        this.soilTransmittedHelminthsIggSerumAntibody = soilTransmittedHelminthsIggSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIgaSerumAntibody(String soilTransmittedHelminthsIgaSerumAntibody) {
        this.soilTransmittedHelminthsIgaSerumAntibody = soilTransmittedHelminthsIgaSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIncubationTime(String soilTransmittedHelminthsIncubationTime) {
        this.soilTransmittedHelminthsIncubationTime = soilTransmittedHelminthsIncubationTime;
    }

    public void setSoilTransmittedHelminthsIndirectFluorescentAntibody(String soilTransmittedHelminthsIndirectFluorescentAntibody) {
        this.soilTransmittedHelminthsIndirectFluorescentAntibody = soilTransmittedHelminthsIndirectFluorescentAntibody;
    }

    public void setSoilTransmittedHelminthsMicroscopy(String soilTransmittedHelminthsMicroscopy) {
        this.soilTransmittedHelminthsMicroscopy = soilTransmittedHelminthsMicroscopy;
    }

    public void setSoilTransmittedHelminthsNeutralizingAntibodies(String soilTransmittedHelminthsNeutralizingAntibodies) {
        this.soilTransmittedHelminthsNeutralizingAntibodies = soilTransmittedHelminthsNeutralizingAntibodies;
    }

    public void setSoilTransmittedHelminthsPcr(String soilTransmittedHelminthsPcr) {
        this.soilTransmittedHelminthsPcr = soilTransmittedHelminthsPcr;
    }

    public void setSoilTransmittedHelminthsGramStain(String soilTransmittedHelminthsGramStain) {
        this.soilTransmittedHelminthsGramStain = soilTransmittedHelminthsGramStain;
    }

    public void setSoilTransmittedHelminthsLatexAgglutination(String soilTransmittedHelminthsLatexAgglutination) {
        this.soilTransmittedHelminthsLatexAgglutination = soilTransmittedHelminthsLatexAgglutination;
    }

    public void setSoilTransmittedHelminthsCqValueDetection(String soilTransmittedHelminthsCqValueDetection) {
        this.soilTransmittedHelminthsCqValueDetection = soilTransmittedHelminthsCqValueDetection;
    }

    public void setSoilTransmittedHelminthsSeQuencing(String soilTransmittedHelminthsSeQuencing) {
        this.soilTransmittedHelminthsSeQuencing = soilTransmittedHelminthsSeQuencing;
    }

    public void setSoilTransmittedHelminthsDnaMicroArray(String soilTransmittedHelminthsDnaMicroArray) {
        this.soilTransmittedHelminthsDnaMicroArray = soilTransmittedHelminthsDnaMicroArray;
    }

    public void setSoilTransmittedHelminthsOther(String soilTransmittedHelminthsOther) {
        this.soilTransmittedHelminthsOther = soilTransmittedHelminthsOther;
    }

    public void setTrypanosomiasisAntiBodyDetection(String trypanosomiasisAntiBodyDetection) {
        this.trypanosomiasisAntiBodyDetection = trypanosomiasisAntiBodyDetection;
    }

    public void setTrypanosomiasisAntigenDetection(String trypanosomiasisAntigenDetection) {
        this.trypanosomiasisAntigenDetection = trypanosomiasisAntigenDetection;
    }

    public void setTrypanosomiasisRapidTest(String trypanosomiasisRapidTest) {
        this.trypanosomiasisRapidTest = trypanosomiasisRapidTest;
    }

    public void setTrypanosomiasisCulture(String trypanosomiasisCulture) {
        this.trypanosomiasisCulture = trypanosomiasisCulture;
    }

    public void setTrypanosomiasisHistopathology(String trypanosomiasisHistopathology) {
        this.trypanosomiasisHistopathology = trypanosomiasisHistopathology;
    }

    public void setTrypanosomiasisIsolation(String trypanosomiasisIsolation) {
        this.trypanosomiasisIsolation = trypanosomiasisIsolation;
    }

    public void setTrypanosomiasisIgmSerumAntibody(String trypanosomiasisIgmSerumAntibody) {
        this.trypanosomiasisIgmSerumAntibody = trypanosomiasisIgmSerumAntibody;
    }

    public void setTrypanosomiasisIggSerumAntibody(String trypanosomiasisIggSerumAntibody) {
        this.trypanosomiasisIggSerumAntibody = trypanosomiasisIggSerumAntibody;
    }

    public void setTrypanosomiasisIgaSerumAntibody(String trypanosomiasisIgaSerumAntibody) {
        this.trypanosomiasisIgaSerumAntibody = trypanosomiasisIgaSerumAntibody;
    }

    public void setTrypanosomiasisIncubationTime(String trypanosomiasisIncubationTime) {
        this.trypanosomiasisIncubationTime = trypanosomiasisIncubationTime;
    }

    public void setTrypanosomiasisIndirectFluorescentAntibody(String trypanosomiasisIndirectFluorescentAntibody) {
        this.trypanosomiasisIndirectFluorescentAntibody = trypanosomiasisIndirectFluorescentAntibody;
    }

    public void setTrypanosomiasisMicroscopy(String trypanosomiasisMicroscopy) {
        this.trypanosomiasisMicroscopy = trypanosomiasisMicroscopy;
    }

    public void setTrypanosomiasisNeutralizingAntibodies(String trypanosomiasisNeutralizingAntibodies) {
        this.trypanosomiasisNeutralizingAntibodies = trypanosomiasisNeutralizingAntibodies;
    }

    public void setTrypanosomiasisPcr(String trypanosomiasisPcr) {
        this.trypanosomiasisPcr = trypanosomiasisPcr;
    }

    public void setTrypanosomiasisGramStain(String trypanosomiasisGramStain) {
        this.trypanosomiasisGramStain = trypanosomiasisGramStain;
    }

    public void setTrypanosomiasisLatexAgglutination(String trypanosomiasisLatexAgglutination) {
        this.trypanosomiasisLatexAgglutination = trypanosomiasisLatexAgglutination;
    }

    public void setTrypanosomiasisCqValueDetection(String trypanosomiasisCqValueDetection) {
        this.trypanosomiasisCqValueDetection = trypanosomiasisCqValueDetection;
    }

    public void setTrypanosomiasisSeQuencing(String trypanosomiasisSeQuencing) {
        this.trypanosomiasisSeQuencing = trypanosomiasisSeQuencing;
    }

    public void setTrypanosomiasisDnaMicroArray(String trypanosomiasisDnaMicroArray) {
        this.trypanosomiasisDnaMicroArray = trypanosomiasisDnaMicroArray;
    }

    public void setTrypanosomiasisOther(String trypanosomiasisOther) {
        this.trypanosomiasisOther = trypanosomiasisOther;
    }

    public void setDiarrheaDehydrationAntiBodyDetection(String diarrheaDehydrationAntiBodyDetection) {
        this.diarrheaDehydrationAntiBodyDetection = diarrheaDehydrationAntiBodyDetection;
    }

    public void setDiarrheaDehydrationAntigenDetection(String diarrheaDehydrationAntigenDetection) {
        this.diarrheaDehydrationAntigenDetection = diarrheaDehydrationAntigenDetection;
    }

    public void setDiarrheaDehydrationRapidTest(String diarrheaDehydrationRapidTest) {
        this.diarrheaDehydrationRapidTest = diarrheaDehydrationRapidTest;
    }

    public void setDiarrheaDehydrationCulture(String diarrheaDehydrationCulture) {
        this.diarrheaDehydrationCulture = diarrheaDehydrationCulture;
    }

    public void setDiarrheaDehydrationHistopathology(String diarrheaDehydrationHistopathology) {
        this.diarrheaDehydrationHistopathology = diarrheaDehydrationHistopathology;
    }

    public void setDiarrheaDehydrationIsolation(String diarrheaDehydrationIsolation) {
        this.diarrheaDehydrationIsolation = diarrheaDehydrationIsolation;
    }

    public void setDiarrheaDehydrationIgmSerumAntibody(String diarrheaDehydrationIgmSerumAntibody) {
        this.diarrheaDehydrationIgmSerumAntibody = diarrheaDehydrationIgmSerumAntibody;
    }

    public void setDiarrheaDehydrationIggSerumAntibody(String diarrheaDehydrationIggSerumAntibody) {
        this.diarrheaDehydrationIggSerumAntibody = diarrheaDehydrationIggSerumAntibody;
    }

    public void setDiarrheaDehydrationIgaSerumAntibody(String diarrheaDehydrationIgaSerumAntibody) {
        this.diarrheaDehydrationIgaSerumAntibody = diarrheaDehydrationIgaSerumAntibody;
    }

    public void setDiarrheaDehydrationIncubationTime(String diarrheaDehydrationIncubationTime) {
        this.diarrheaDehydrationIncubationTime = diarrheaDehydrationIncubationTime;
    }

    public void setDiarrheaDehydrationIndirectFluorescentAntibody(String diarrheaDehydrationIndirectFluorescentAntibody) {
        this.diarrheaDehydrationIndirectFluorescentAntibody = diarrheaDehydrationIndirectFluorescentAntibody;
    }

    public void setDiarrheaDehydrationMicroscopy(String diarrheaDehydrationMicroscopy) {
        this.diarrheaDehydrationMicroscopy = diarrheaDehydrationMicroscopy;
    }

    public void setDiarrheaDehydrationNeutralizingAntibodies(String diarrheaDehydrationNeutralizingAntibodies) {
        this.diarrheaDehydrationNeutralizingAntibodies = diarrheaDehydrationNeutralizingAntibodies;
    }

    public void setDiarrheaDehydrationPcr(String diarrheaDehydrationPcr) {
        this.diarrheaDehydrationPcr = diarrheaDehydrationPcr;
    }

    public void setDiarrheaDehydrationGramStain(String diarrheaDehydrationGramStain) {
        this.diarrheaDehydrationGramStain = diarrheaDehydrationGramStain;
    }

    public void setDiarrheaDehydrationLatexAgglutination(String diarrheaDehydrationLatexAgglutination) {
        this.diarrheaDehydrationLatexAgglutination = diarrheaDehydrationLatexAgglutination;
    }

    public void setDiarrheaDehydrationCqValueDetection(String diarrheaDehydrationCqValueDetection) {
        this.diarrheaDehydrationCqValueDetection = diarrheaDehydrationCqValueDetection;
    }

    public void setDiarrheaDehydrationSeQuencing(String diarrheaDehydrationSeQuencing) {
        this.diarrheaDehydrationSeQuencing = diarrheaDehydrationSeQuencing;
    }

    public void setDiarrheaDehydrationDnaMicroArray(String diarrheaDehydrationDnaMicroArray) {
        this.diarrheaDehydrationDnaMicroArray = diarrheaDehydrationDnaMicroArray;
    }

    public void setDiarrheaDehydrationOther(String diarrheaDehydrationOther) {
        this.diarrheaDehydrationOther = diarrheaDehydrationOther;
    }

    public void setDiarrheaBloodAntiBodyDetection(String diarrheaBloodAntiBodyDetection) {
        this.diarrheaBloodAntiBodyDetection = diarrheaBloodAntiBodyDetection;
    }

    public void setDiarrheaBloodAntigenDetection(String diarrheaBloodAntigenDetection) {
        this.diarrheaBloodAntigenDetection = diarrheaBloodAntigenDetection;
    }

    public void setDiarrheaBloodRapidTest(String diarrheaBloodRapidTest) {
        this.diarrheaBloodRapidTest = diarrheaBloodRapidTest;
    }

    public void setDiarrheaBloodCulture(String diarrheaBloodCulture) {
        this.diarrheaBloodCulture = diarrheaBloodCulture;
    }

    public void setDiarrheaBloodHistopathology(String diarrheaBloodHistopathology) {
        this.diarrheaBloodHistopathology = diarrheaBloodHistopathology;
    }

    public void setDiarrheaBloodIsolation(String diarrheaBloodIsolation) {
        this.diarrheaBloodIsolation = diarrheaBloodIsolation;
    }

    public void setDiarrheaBloodIgmSerumAntibody(String diarrheaBloodIgmSerumAntibody) {
        this.diarrheaBloodIgmSerumAntibody = diarrheaBloodIgmSerumAntibody;
    }

    public void setDiarrheaBloodIggSerumAntibody(String diarrheaBloodIggSerumAntibody) {
        this.diarrheaBloodIggSerumAntibody = diarrheaBloodIggSerumAntibody;
    }

    public void setDiarrheaBloodIgaSerumAntibody(String diarrheaBloodIgaSerumAntibody) {
        this.diarrheaBloodIgaSerumAntibody = diarrheaBloodIgaSerumAntibody;
    }

    public void setDiarrheaBloodIncubationTime(String diarrheaBloodIncubationTime) {
        this.diarrheaBloodIncubationTime = diarrheaBloodIncubationTime;
    }

    public void setDiarrheaBloodIndirectFluorescentAntibody(String diarrheaBloodIndirectFluorescentAntibody) {
        this.diarrheaBloodIndirectFluorescentAntibody = diarrheaBloodIndirectFluorescentAntibody;
    }

    public void setDiarrheaBloodMicroscopy(String diarrheaBloodMicroscopy) {
        this.diarrheaBloodMicroscopy = diarrheaBloodMicroscopy;
    }

    public void setDiarrheaBloodNeutralizingAntibodies(String diarrheaBloodNeutralizingAntibodies) {
        this.diarrheaBloodNeutralizingAntibodies = diarrheaBloodNeutralizingAntibodies;
    }

    public void setDiarrheaBloodPcr(String diarrheaBloodPcr) {
        this.diarrheaBloodPcr = diarrheaBloodPcr;
    }

    public void setDiarrheaBloodGramStain(String diarrheaBloodGramStain) {
        this.diarrheaBloodGramStain = diarrheaBloodGramStain;
    }

    public void setDiarrheaBloodLatexAgglutination(String diarrheaBloodLatexAgglutination) {
        this.diarrheaBloodLatexAgglutination = diarrheaBloodLatexAgglutination;
    }

    public void setDiarrheaBloodCqValueDetection(String diarrheaBloodCqValueDetection) {
        this.diarrheaBloodCqValueDetection = diarrheaBloodCqValueDetection;
    }

    public void setDiarrheaBloodSeQuencing(String diarrheaBloodSeQuencing) {
        this.diarrheaBloodSeQuencing = diarrheaBloodSeQuencing;
    }

    public void setDiarrheaBloodDnaMicroArray(String diarrheaBloodDnaMicroArray) {
        this.diarrheaBloodDnaMicroArray = diarrheaBloodDnaMicroArray;
    }

    public void setDiarrheaBloodOther(String diarrheaBloodOther) {
        this.diarrheaBloodOther = diarrheaBloodOther;
    }

    public void setSnakeBiteAntiBodyDetection(String snakeBiteAntiBodyDetection) {
        this.snakeBiteAntiBodyDetection = snakeBiteAntiBodyDetection;
    }

    public void setSnakeBiteAntigenDetection(String snakeBiteAntigenDetection) {
        this.snakeBiteAntigenDetection = snakeBiteAntigenDetection;
    }

    public void setSnakeBiteRapidTest(String snakeBiteRapidTest) {
        this.snakeBiteRapidTest = snakeBiteRapidTest;
    }

    public void setSnakeBiteCulture(String snakeBiteCulture) {
        this.snakeBiteCulture = snakeBiteCulture;
    }

    public void setSnakeBiteHistopathology(String snakeBiteHistopathology) {
        this.snakeBiteHistopathology = snakeBiteHistopathology;
    }

    public void setSnakeBiteIsolation(String snakeBiteIsolation) {
        this.snakeBiteIsolation = snakeBiteIsolation;
    }

    public void setSnakeBiteIgmSerumAntibody(String snakeBiteIgmSerumAntibody) {
        this.snakeBiteIgmSerumAntibody = snakeBiteIgmSerumAntibody;
    }

    public void setSnakeBiteIggSerumAntibody(String snakeBiteIggSerumAntibody) {
        this.snakeBiteIggSerumAntibody = snakeBiteIggSerumAntibody;
    }

    public void setSnakeBiteIgaSerumAntibody(String snakeBiteIgaSerumAntibody) {
        this.snakeBiteIgaSerumAntibody = snakeBiteIgaSerumAntibody;
    }

    public void setSnakeBiteIncubationTime(String snakeBiteIncubationTime) {
        this.snakeBiteIncubationTime = snakeBiteIncubationTime;
    }

    public void setSnakeBiteIndirectFluorescentAntibody(String snakeBiteIndirectFluorescentAntibody) {
        this.snakeBiteIndirectFluorescentAntibody = snakeBiteIndirectFluorescentAntibody;
    }

    public void setSnakeBiteMicroscopy(String snakeBiteMicroscopy) {
        this.snakeBiteMicroscopy = snakeBiteMicroscopy;
    }

    public void setSnakeBiteNeutralizingAntibodies(String snakeBiteNeutralizingAntibodies) {
        this.snakeBiteNeutralizingAntibodies = snakeBiteNeutralizingAntibodies;
    }

    public void setSnakeBitePcr(String snakeBitePcr) {
        this.snakeBitePcr = snakeBitePcr;
    }

    public void setSnakeBiteGramStain(String snakeBiteGramStain) {
        this.snakeBiteGramStain = snakeBiteGramStain;
    }

    public void setSnakeBiteLatexAgglutination(String snakeBiteLatexAgglutination) {
        this.snakeBiteLatexAgglutination = snakeBiteLatexAgglutination;
    }

    public void setSnakeBiteCqValueDetection(String snakeBiteCqValueDetection) {
        this.snakeBiteCqValueDetection = snakeBiteCqValueDetection;
    }

    public void setSnakeBiteSeQuencing(String snakeBiteSeQuencing) {
        this.snakeBiteSeQuencing = snakeBiteSeQuencing;
    }

    public void setSnakeBiteDnaMicroArray(String snakeBiteDnaMicroArray) {
        this.snakeBiteDnaMicroArray = snakeBiteDnaMicroArray;
    }

    public void setSnakeBiteOther(String snakeBiteOther) {
        this.snakeBiteOther = snakeBiteOther;
    }

    public void setRubellaAntiBodyDetection(String rubellaAntiBodyDetection) {
        this.rubellaAntiBodyDetection = rubellaAntiBodyDetection;
    }

    public void setRubellaAntigenDetection(String rubellaAntigenDetection) {
        this.rubellaAntigenDetection = rubellaAntigenDetection;
    }

    public void setRubellaRapidTest(String rubellaRapidTest) {
        this.rubellaRapidTest = rubellaRapidTest;
    }

    public void setRubellaCulture(String rubellaCulture) {
        this.rubellaCulture = rubellaCulture;
    }

    public void setRubellaHistopathology(String rubellaHistopathology) {
        this.rubellaHistopathology = rubellaHistopathology;
    }

    public void setRubellaIsolation(String rubellaIsolation) {
        this.rubellaIsolation = rubellaIsolation;
    }

    public void setRubellaIgmSerumAntibody(String rubellaIgmSerumAntibody) {
        this.rubellaIgmSerumAntibody = rubellaIgmSerumAntibody;
    }

    public void setRubellaIggSerumAntibody(String rubellaIggSerumAntibody) {
        this.rubellaIggSerumAntibody = rubellaIggSerumAntibody;
    }

    public void setRubellaIgaSerumAntibody(String rubellaIgaSerumAntibody) {
        this.rubellaIgaSerumAntibody = rubellaIgaSerumAntibody;
    }

    public void setRubellaIncubationTime(String rubellaIncubationTime) {
        this.rubellaIncubationTime = rubellaIncubationTime;
    }

    public void setRubellaIndirectFluorescentAntibody(String rubellaIndirectFluorescentAntibody) {
        this.rubellaIndirectFluorescentAntibody = rubellaIndirectFluorescentAntibody;
    }

    public void setRubellaMicroscopy(String rubellaMicroscopy) {
        this.rubellaMicroscopy = rubellaMicroscopy;
    }

    public void setRubellaNeutralizingAntibodies(String rubellaNeutralizingAntibodies) {
        this.rubellaNeutralizingAntibodies = rubellaNeutralizingAntibodies;
    }

    public void setRubellaPcr(String rubellaPcr) {
        this.rubellaPcr = rubellaPcr;
    }

    public void setRubellaGramStain(String rubellaGramStain) {
        this.rubellaGramStain = rubellaGramStain;
    }

    public void setRubellaLatexAgglutination(String rubellaLatexAgglutination) {
        this.rubellaLatexAgglutination = rubellaLatexAgglutination;
    }

    public void setRubellaCqValueDetection(String rubellaCqValueDetection) {
        this.rubellaCqValueDetection = rubellaCqValueDetection;
    }

    public void setRubellaSeQuencing(String rubellaSeQuencing) {
        this.rubellaSeQuencing = rubellaSeQuencing;
    }

    public void setRubellaDnaMicroArray(String rubellaDnaMicroArray) {
        this.rubellaDnaMicroArray = rubellaDnaMicroArray;
    }

    public void setRubellaOther(String rubellaOther) {
        this.rubellaOther = rubellaOther;
    }

    public void setTuberculosisAntiBodyDetection(String tuberculosisAntiBodyDetection) {
        this.tuberculosisAntiBodyDetection = tuberculosisAntiBodyDetection;
    }

    public void setTuberculosisAntigenDetection(String tuberculosisAntigenDetection) {
        this.tuberculosisAntigenDetection = tuberculosisAntigenDetection;
    }

    public void setTuberculosisRapidTest(String tuberculosisRapidTest) {
        this.tuberculosisRapidTest = tuberculosisRapidTest;
    }

    public void setTuberculosisCulture(String tuberculosisCulture) {
        this.tuberculosisCulture = tuberculosisCulture;
    }

    public void setTuberculosisHistopathology(String tuberculosisHistopathology) {
        this.tuberculosisHistopathology = tuberculosisHistopathology;
    }

    public void setTuberculosisIsolation(String tuberculosisIsolation) {
        this.tuberculosisIsolation = tuberculosisIsolation;
    }

    public void setTuberculosisIgmSerumAntibody(String tuberculosisIgmSerumAntibody) {
        this.tuberculosisIgmSerumAntibody = tuberculosisIgmSerumAntibody;
    }

    public void setTuberculosisIggSerumAntibody(String tuberculosisIggSerumAntibody) {
        this.tuberculosisIggSerumAntibody = tuberculosisIggSerumAntibody;
    }

    public void setTuberculosisIgaSerumAntibody(String tuberculosisIgaSerumAntibody) {
        this.tuberculosisIgaSerumAntibody = tuberculosisIgaSerumAntibody;
    }

    public void setTuberculosisIncubationTime(String tuberculosisIncubationTime) {
        this.tuberculosisIncubationTime = tuberculosisIncubationTime;
    }

    public void setTuberculosisIndirectFluorescentAntibody(String tuberculosisIndirectFluorescentAntibody) {
        this.tuberculosisIndirectFluorescentAntibody = tuberculosisIndirectFluorescentAntibody;
    }

    public void setTuberculosisMicroscopy(String tuberculosisMicroscopy) {
        this.tuberculosisMicroscopy = tuberculosisMicroscopy;
    }

    public void setTuberculosisNeutralizingAntibodies(String tuberculosisNeutralizingAntibodies) {
        this.tuberculosisNeutralizingAntibodies = tuberculosisNeutralizingAntibodies;
    }

    public void setTuberculosisPcr(String tuberculosisPcr) {
        this.tuberculosisPcr = tuberculosisPcr;
    }

    public void setTuberculosisGramStain(String tuberculosisGramStain) {
        this.tuberculosisGramStain = tuberculosisGramStain;
    }

    public void setTuberculosisLatexAgglutination(String tuberculosisLatexAgglutination) {
        this.tuberculosisLatexAgglutination = tuberculosisLatexAgglutination;
    }

    public void setTuberculosisCqValueDetection(String tuberculosisCqValueDetection) {
        this.tuberculosisCqValueDetection = tuberculosisCqValueDetection;
    }

    public void setTuberculosisSeQuencing(String tuberculosisSeQuencing) {
        this.tuberculosisSeQuencing = tuberculosisSeQuencing;
    }

    public void setTuberculosisDnaMicroArray(String tuberculosisDnaMicroArray) {
        this.tuberculosisDnaMicroArray = tuberculosisDnaMicroArray;
    }

    public void setTuberculosisOther(String tuberculosisOther) {
        this.tuberculosisOther = tuberculosisOther;
    }

    public void setLeprosyAntiBodyDetection(String leprosyAntiBodyDetection) {
        this.leprosyAntiBodyDetection = leprosyAntiBodyDetection;
    }

    public void setLeprosyAntigenDetection(String leprosyAntigenDetection) {
        this.leprosyAntigenDetection = leprosyAntigenDetection;
    }

    public void setLeprosyRapidTest(String leprosyRapidTest) {
        this.leprosyRapidTest = leprosyRapidTest;
    }

    public void setLeprosyCulture(String leprosyCulture) {
        this.leprosyCulture = leprosyCulture;
    }

    public void setLeprosyHistopathology(String leprosyHistopathology) {
        this.leprosyHistopathology = leprosyHistopathology;
    }

    public void setLeprosyIsolation(String leprosyIsolation) {
        this.leprosyIsolation = leprosyIsolation;
    }

    public void setLeprosyIgmSerumAntibody(String leprosyIgmSerumAntibody) {
        this.leprosyIgmSerumAntibody = leprosyIgmSerumAntibody;
    }

    public void setLeprosyIggSerumAntibody(String leprosyIggSerumAntibody) {
        this.leprosyIggSerumAntibody = leprosyIggSerumAntibody;
    }

    public void setLeprosyIgaSerumAntibody(String leprosyIgaSerumAntibody) {
        this.leprosyIgaSerumAntibody = leprosyIgaSerumAntibody;
    }

    public void setLeprosyIncubationTime(String leprosyIncubationTime) {
        this.leprosyIncubationTime = leprosyIncubationTime;
    }

    public void setLeprosyIndirectFluorescentAntibody(String leprosyIndirectFluorescentAntibody) {
        this.leprosyIndirectFluorescentAntibody = leprosyIndirectFluorescentAntibody;
    }

    public void setLeprosyMicroscopy(String leprosyMicroscopy) {
        this.leprosyMicroscopy = leprosyMicroscopy;
    }

    public void setLeprosyNeutralizingAntibodies(String leprosyNeutralizingAntibodies) {
        this.leprosyNeutralizingAntibodies = leprosyNeutralizingAntibodies;
    }

    public void setLeprosyPcr(String leprosyPcr) {
        this.leprosyPcr = leprosyPcr;
    }

    public void setLeprosyGramStain(String leprosyGramStain) {
        this.leprosyGramStain = leprosyGramStain;
    }

    public void setLeprosyLatexAgglutination(String leprosyLatexAgglutination) {
        this.leprosyLatexAgglutination = leprosyLatexAgglutination;
    }

    public void setLeprosyCqValueDetection(String leprosyCqValueDetection) {
        this.leprosyCqValueDetection = leprosyCqValueDetection;
    }

    public void setLeprosySeQuencing(String leprosySeQuencing) {
        this.leprosySeQuencing = leprosySeQuencing;
    }

    public void setLeprosyDnaMicroArray(String leprosyDnaMicroArray) {
        this.leprosyDnaMicroArray = leprosyDnaMicroArray;
    }

    public void setLeprosyOther(String leprosyOther) {
        this.leprosyOther = leprosyOther;
    }

    public void setLymphaticFilariasisAntiBodyDetection(String lymphaticFilariasisAntiBodyDetection) {
        this.lymphaticFilariasisAntiBodyDetection = lymphaticFilariasisAntiBodyDetection;
    }

    public void setLymphaticFilariasisAntigenDetection(String lymphaticFilariasisAntigenDetection) {
        this.lymphaticFilariasisAntigenDetection = lymphaticFilariasisAntigenDetection;
    }

    public void setLymphaticFilariasisRapidTest(String lymphaticFilariasisRapidTest) {
        this.lymphaticFilariasisRapidTest = lymphaticFilariasisRapidTest;
    }

    public void setLymphaticFilariasisCulture(String lymphaticFilariasisCulture) {
        this.lymphaticFilariasisCulture = lymphaticFilariasisCulture;
    }

    public void setLymphaticFilariasisHistopathology(String lymphaticFilariasisHistopathology) {
        this.lymphaticFilariasisHistopathology = lymphaticFilariasisHistopathology;
    }

    public void setLymphaticFilariasisIsolation(String lymphaticFilariasisIsolation) {
        this.lymphaticFilariasisIsolation = lymphaticFilariasisIsolation;
    }

    public void setLymphaticFilariasisIgmSerumAntibody(String lymphaticFilariasisIgmSerumAntibody) {
        this.lymphaticFilariasisIgmSerumAntibody = lymphaticFilariasisIgmSerumAntibody;
    }

    public void setLymphaticFilariasisIggSerumAntibody(String lymphaticFilariasisIggSerumAntibody) {
        this.lymphaticFilariasisIggSerumAntibody = lymphaticFilariasisIggSerumAntibody;
    }

    public void setLymphaticFilariasisIgaSerumAntibody(String lymphaticFilariasisIgaSerumAntibody) {
        this.lymphaticFilariasisIgaSerumAntibody = lymphaticFilariasisIgaSerumAntibody;
    }

    public void setLymphaticFilariasisIncubationTime(String lymphaticFilariasisIncubationTime) {
        this.lymphaticFilariasisIncubationTime = lymphaticFilariasisIncubationTime;
    }

    public void setLymphaticFilariasisIndirectFluorescentAntibody(String lymphaticFilariasisIndirectFluorescentAntibody) {
        this.lymphaticFilariasisIndirectFluorescentAntibody = lymphaticFilariasisIndirectFluorescentAntibody;
    }

    public void setLymphaticFilariasisMicroscopy(String lymphaticFilariasisMicroscopy) {
        this.lymphaticFilariasisMicroscopy = lymphaticFilariasisMicroscopy;
    }

    public void setLymphaticFilariasisNeutralizingAntibodies(String lymphaticFilariasisNeutralizingAntibodies) {
        this.lymphaticFilariasisNeutralizingAntibodies = lymphaticFilariasisNeutralizingAntibodies;
    }

    public void setLymphaticFilariasisPcr(String lymphaticFilariasisPcr) {
        this.lymphaticFilariasisPcr = lymphaticFilariasisPcr;
    }

    public void setLymphaticFilariasisGramStain(String lymphaticFilariasisGramStain) {
        this.lymphaticFilariasisGramStain = lymphaticFilariasisGramStain;
    }

    public void setLymphaticFilariasisLatexAgglutination(String lymphaticFilariasisLatexAgglutination) {
        this.lymphaticFilariasisLatexAgglutination = lymphaticFilariasisLatexAgglutination;
    }

    public void setLymphaticFilariasisCqValueDetection(String lymphaticFilariasisCqValueDetection) {
        this.lymphaticFilariasisCqValueDetection = lymphaticFilariasisCqValueDetection;
    }

    public void setLymphaticFilariasisSeQuencing(String lymphaticFilariasisSeQuencing) {
        this.lymphaticFilariasisSeQuencing = lymphaticFilariasisSeQuencing;
    }

    public void setLymphaticFilariasisDnaMicroArray(String lymphaticFilariasisDnaMicroArray) {
        this.lymphaticFilariasisDnaMicroArray = lymphaticFilariasisDnaMicroArray;
    }

    public void setLymphaticFilariasisOther(String lymphaticFilariasisOther) {
        this.lymphaticFilariasisOther = lymphaticFilariasisOther;
    }

    public void setBuruliUlcerAntiBodyDetection(String buruliUlcerAntiBodyDetection) {
        this.buruliUlcerAntiBodyDetection = buruliUlcerAntiBodyDetection;
    }

    public void setBuruliUlcerAntigenDetection(String buruliUlcerAntigenDetection) {
        this.buruliUlcerAntigenDetection = buruliUlcerAntigenDetection;
    }

    public void setBuruliUlcerRapidTest(String buruliUlcerRapidTest) {
        this.buruliUlcerRapidTest = buruliUlcerRapidTest;
    }

    public void setBuruliUlcerCulture(String buruliUlcerCulture) {
        this.buruliUlcerCulture = buruliUlcerCulture;
    }

    public void setBuruliUlcerHistopathology(String buruliUlcerHistopathology) {
        this.buruliUlcerHistopathology = buruliUlcerHistopathology;
    }

    public void setBuruliUlcerIsolation(String buruliUlcerIsolation) {
        this.buruliUlcerIsolation = buruliUlcerIsolation;
    }

    public void setBuruliUlcerIgmSerumAntibody(String buruliUlcerIgmSerumAntibody) {
        this.buruliUlcerIgmSerumAntibody = buruliUlcerIgmSerumAntibody;
    }

    public void setBuruliUlcerIggSerumAntibody(String buruliUlcerIggSerumAntibody) {
        this.buruliUlcerIggSerumAntibody = buruliUlcerIggSerumAntibody;
    }

    public void setBuruliUlcerIgaSerumAntibody(String buruliUlcerIgaSerumAntibody) {
        this.buruliUlcerIgaSerumAntibody = buruliUlcerIgaSerumAntibody;
    }

    public void setBuruliUlcerIncubationTime(String buruliUlcerIncubationTime) {
        this.buruliUlcerIncubationTime = buruliUlcerIncubationTime;
    }

    public void setBuruliUlcerIndirectFluorescentAntibody(String buruliUlcerIndirectFluorescentAntibody) {
        this.buruliUlcerIndirectFluorescentAntibody = buruliUlcerIndirectFluorescentAntibody;
    }

    public void setBuruliUlcerMicroscopy(String buruliUlcerMicroscopy) {
        this.buruliUlcerMicroscopy = buruliUlcerMicroscopy;
    }

    public void setBuruliUlcerNeutralizingAntibodies(String buruliUlcerNeutralizingAntibodies) {
        this.buruliUlcerNeutralizingAntibodies = buruliUlcerNeutralizingAntibodies;
    }

    public void setBuruliUlcerPcr(String buruliUlcerPcr) {
        this.buruliUlcerPcr = buruliUlcerPcr;
    }

    public void setBuruliUlcerGramStain(String buruliUlcerGramStain) {
        this.buruliUlcerGramStain = buruliUlcerGramStain;
    }

    public void setBuruliUlcerLatexAgglutination(String buruliUlcerLatexAgglutination) {
        this.buruliUlcerLatexAgglutination = buruliUlcerLatexAgglutination;
    }

    public void setBuruliUlcerCqValueDetection(String buruliUlcerCqValueDetection) {
        this.buruliUlcerCqValueDetection = buruliUlcerCqValueDetection;
    }

    public void setBuruliUlcerSeQuencing(String buruliUlcerSeQuencing) {
        this.buruliUlcerSeQuencing = buruliUlcerSeQuencing;
    }

    public void setBuruliUlcerDnaMicroArray(String buruliUlcerDnaMicroArray) {
        this.buruliUlcerDnaMicroArray = buruliUlcerDnaMicroArray;
    }

    public void setBuruliUlcerOther(String buruliUlcerOther) {
        this.buruliUlcerOther = buruliUlcerOther;
    }

    public void setPertussisAntiBodyDetection(String pertussisAntiBodyDetection) {
        this.pertussisAntiBodyDetection = pertussisAntiBodyDetection;
    }

    public void setPertussisAntigenDetection(String pertussisAntigenDetection) {
        this.pertussisAntigenDetection = pertussisAntigenDetection;
    }

    public void setPertussisRapidTest(String pertussisRapidTest) {
        this.pertussisRapidTest = pertussisRapidTest;
    }

    public void setPertussisCulture(String pertussisCulture) {
        this.pertussisCulture = pertussisCulture;
    }

    public void setPertussisHistopathology(String pertussisHistopathology) {
        this.pertussisHistopathology = pertussisHistopathology;
    }

    public void setPertussisIsolation(String pertussisIsolation) {
        this.pertussisIsolation = pertussisIsolation;
    }

    public void setPertussisIgmSerumAntibody(String pertussisIgmSerumAntibody) {
        this.pertussisIgmSerumAntibody = pertussisIgmSerumAntibody;
    }

    public void setPertussisIggSerumAntibody(String pertussisIggSerumAntibody) {
        this.pertussisIggSerumAntibody = pertussisIggSerumAntibody;
    }

    public void setPertussisIgaSerumAntibody(String pertussisIgaSerumAntibody) {
        this.pertussisIgaSerumAntibody = pertussisIgaSerumAntibody;
    }

    public void setPertussisIncubationTime(String pertussisIncubationTime) {
        this.pertussisIncubationTime = pertussisIncubationTime;
    }

    public void setPertussisIndirectFluorescentAntibody(String pertussisIndirectFluorescentAntibody) {
        this.pertussisIndirectFluorescentAntibody = pertussisIndirectFluorescentAntibody;
    }

    public void setPertussisMicroscopy(String pertussisMicroscopy) {
        this.pertussisMicroscopy = pertussisMicroscopy;
    }

    public void setPertussisNeutralizingAntibodies(String pertussisNeutralizingAntibodies) {
        this.pertussisNeutralizingAntibodies = pertussisNeutralizingAntibodies;
    }

    public void setPertussisPcr(String pertussisPcr) {
        this.pertussisPcr = pertussisPcr;
    }

    public void setPertussisGramStain(String pertussisGramStain) {
        this.pertussisGramStain = pertussisGramStain;
    }

    public void setPertussisLatexAgglutination(String pertussisLatexAgglutination) {
        this.pertussisLatexAgglutination = pertussisLatexAgglutination;
    }

    public void setPertussisCqValueDetection(String pertussisCqValueDetection) {
        this.pertussisCqValueDetection = pertussisCqValueDetection;
    }

    public void setPertussisSeQuencing(String pertussisSeQuencing) {
        this.pertussisSeQuencing = pertussisSeQuencing;
    }

    public void setPertussisDnaMicroArray(String pertussisDnaMicroArray) {
        this.pertussisDnaMicroArray = pertussisDnaMicroArray;
    }

    public void setPertussisOther(String pertussisOther) {
        this.pertussisOther = pertussisOther;
    }

    public void setNeonatalAntiBodyDetection(String neonatalAntiBodyDetection) {
        this.neonatalAntiBodyDetection = neonatalAntiBodyDetection;
    }

    public void setNeonatalAntigenDetection(String neonatalAntigenDetection) {
        this.neonatalAntigenDetection = neonatalAntigenDetection;
    }

    public void setNeonatalRapidTest(String neonatalRapidTest) {
        this.neonatalRapidTest = neonatalRapidTest;
    }

    public void setNeonatalCulture(String neonatalCulture) {
        this.neonatalCulture = neonatalCulture;
    }

    public void setNeonatalHistopathology(String neonatalHistopathology) {
        this.neonatalHistopathology = neonatalHistopathology;
    }

    public void setNeonatalIsolation(String neonatalIsolation) {
        this.neonatalIsolation = neonatalIsolation;
    }

    public void setNeonatalIgmSerumAntibody(String neonatalIgmSerumAntibody) {
        this.neonatalIgmSerumAntibody = neonatalIgmSerumAntibody;
    }

    public void setNeonatalIggSerumAntibody(String neonatalIggSerumAntibody) {
        this.neonatalIggSerumAntibody = neonatalIggSerumAntibody;
    }

    public void setNeonatalIgaSerumAntibody(String neonatalIgaSerumAntibody) {
        this.neonatalIgaSerumAntibody = neonatalIgaSerumAntibody;
    }

    public void setNeonatalIncubationTime(String neonatalIncubationTime) {
        this.neonatalIncubationTime = neonatalIncubationTime;
    }

    public void setNeonatalIndirectFluorescentAntibody(String neonatalIndirectFluorescentAntibody) {
        this.neonatalIndirectFluorescentAntibody = neonatalIndirectFluorescentAntibody;
    }

    public void setNeonatalMicroscopy(String neonatalMicroscopy) {
        this.neonatalMicroscopy = neonatalMicroscopy;
    }

    public void setNeonatalNeutralizingAntibodies(String neonatalNeutralizingAntibodies) {
        this.neonatalNeutralizingAntibodies = neonatalNeutralizingAntibodies;
    }

    public void setNeonatalPcr(String neonatalPcr) {
        this.neonatalPcr = neonatalPcr;
    }

    public void setNeonatalGramStain(String neonatalGramStain) {
        this.neonatalGramStain = neonatalGramStain;
    }

    public void setNeonatalLatexAgglutination(String neonatalLatexAgglutination) {
        this.neonatalLatexAgglutination = neonatalLatexAgglutination;
    }

    public void setNeonatalCqValueDetection(String neonatalCqValueDetection) {
        this.neonatalCqValueDetection = neonatalCqValueDetection;
    }

    public void setNeonatalSeQuencing(String neonatalSeQuencing) {
        this.neonatalSeQuencing = neonatalSeQuencing;
    }

    public void setNeonatalDnaMicroArray(String neonatalDnaMicroArray) {
        this.neonatalDnaMicroArray = neonatalDnaMicroArray;
    }

    public void setNeonatalOther(String neonatalOther) {
        this.neonatalOther = neonatalOther;
    }

    public void setOnchocerciasisAntiBodyDetection(String onchocerciasisAntiBodyDetection) {
        this.onchocerciasisAntiBodyDetection = onchocerciasisAntiBodyDetection;
    }

    public void setOnchocerciasisAntigenDetection(String onchocerciasisAntigenDetection) {
        this.onchocerciasisAntigenDetection = onchocerciasisAntigenDetection;
    }

    public void setOnchocerciasisRapidTest(String onchocerciasisRapidTest) {
        this.onchocerciasisRapidTest = onchocerciasisRapidTest;
    }

    public void setOnchocerciasisCulture(String onchocerciasisCulture) {
        this.onchocerciasisCulture = onchocerciasisCulture;
    }

    public void setOnchocerciasisHistopathology(String onchocerciasisHistopathology) {
        this.onchocerciasisHistopathology = onchocerciasisHistopathology;
    }

    public void setOnchocerciasisIsolation(String onchocerciasisIsolation) {
        this.onchocerciasisIsolation = onchocerciasisIsolation;
    }

    public void setOnchocerciasisIgmSerumAntibody(String onchocerciasisIgmSerumAntibody) {
        this.onchocerciasisIgmSerumAntibody = onchocerciasisIgmSerumAntibody;
    }

    public void setOnchocerciasisIggSerumAntibody(String onchocerciasisIggSerumAntibody) {
        this.onchocerciasisIggSerumAntibody = onchocerciasisIggSerumAntibody;
    }

    public void setOnchocerciasisIgaSerumAntibody(String onchocerciasisIgaSerumAntibody) {
        this.onchocerciasisIgaSerumAntibody = onchocerciasisIgaSerumAntibody;
    }

    public void setOnchocerciasisIncubationTime(String onchocerciasisIncubationTime) {
        this.onchocerciasisIncubationTime = onchocerciasisIncubationTime;
    }

    public void setOnchocerciasisIndirectFluorescentAntibody(String onchocerciasisIndirectFluorescentAntibody) {
        this.onchocerciasisIndirectFluorescentAntibody = onchocerciasisIndirectFluorescentAntibody;
    }

    public void setOnchocerciasisMicroscopy(String onchocerciasisMicroscopy) {
        this.onchocerciasisMicroscopy = onchocerciasisMicroscopy;
    }

    public void setOnchocerciasisNeutralizingAntibodies(String onchocerciasisNeutralizingAntibodies) {
        this.onchocerciasisNeutralizingAntibodies = onchocerciasisNeutralizingAntibodies;
    }

    public void setOnchocerciasisPcr(String onchocerciasisPcr) {
        this.onchocerciasisPcr = onchocerciasisPcr;
    }

    public void setOnchocerciasisGramStain(String onchocerciasisGramStain) {
        this.onchocerciasisGramStain = onchocerciasisGramStain;
    }

    public void setOnchocerciasisLatexAgglutination(String onchocerciasisLatexAgglutination) {
        this.onchocerciasisLatexAgglutination = onchocerciasisLatexAgglutination;
    }

    public void setOnchocerciasisCqValueDetection(String onchocerciasisCqValueDetection) {
        this.onchocerciasisCqValueDetection = onchocerciasisCqValueDetection;
    }

    public void setOnchocerciasisSeQuencing(String onchocerciasisSeQuencing) {
        this.onchocerciasisSeQuencing = onchocerciasisSeQuencing;
    }

    public void setOnchocerciasisDnaMicroArray(String onchocerciasisDnaMicroArray) {
        this.onchocerciasisDnaMicroArray = onchocerciasisDnaMicroArray;
    }

    public void setOnchocerciasisOther(String onchocerciasisOther) {
        this.onchocerciasisOther = onchocerciasisOther;
    }

    public void setDiphtheriaAntiBodyDetection(String diphtheriaAntiBodyDetection) {
        this.diphtheriaAntiBodyDetection = diphtheriaAntiBodyDetection;
    }

    public void setDiphtheriaAntigenDetection(String diphtheriaAntigenDetection) {
        this.diphtheriaAntigenDetection = diphtheriaAntigenDetection;
    }

    public void setDiphtheriaRapidTest(String diphtheriaRapidTest) {
        this.diphtheriaRapidTest = diphtheriaRapidTest;
    }

    public void setDiphtheriaCulture(String diphtheriaCulture) {
        this.diphtheriaCulture = diphtheriaCulture;
    }

    public void setDiphtheriaHistopathology(String diphtheriaHistopathology) {
        this.diphtheriaHistopathology = diphtheriaHistopathology;
    }

    public void setDiphtheriaIsolation(String diphtheriaIsolation) {
        this.diphtheriaIsolation = diphtheriaIsolation;
    }

    public void setDiphtheriaIgmSerumAntibody(String diphtheriaIgmSerumAntibody) {
        this.diphtheriaIgmSerumAntibody = diphtheriaIgmSerumAntibody;
    }

    public void setDiphtheriaIggSerumAntibody(String diphtheriaIggSerumAntibody) {
        this.diphtheriaIggSerumAntibody = diphtheriaIggSerumAntibody;
    }

    public void setDiphtheriaIgaSerumAntibody(String diphtheriaIgaSerumAntibody) {
        this.diphtheriaIgaSerumAntibody = diphtheriaIgaSerumAntibody;
    }

    public void setDiphtheriaIncubationTime(String diphtheriaIncubationTime) {
        this.diphtheriaIncubationTime = diphtheriaIncubationTime;
    }

    public void setDiphtheriaIndirectFluorescentAntibody(String diphtheriaIndirectFluorescentAntibody) {
        this.diphtheriaIndirectFluorescentAntibody = diphtheriaIndirectFluorescentAntibody;
    }

    public void setDiphtheriaMicroscopy(String diphtheriaMicroscopy) {
        this.diphtheriaMicroscopy = diphtheriaMicroscopy;
    }

    public void setDiphtheriaNeutralizingAntibodies(String diphtheriaNeutralizingAntibodies) {
        this.diphtheriaNeutralizingAntibodies = diphtheriaNeutralizingAntibodies;
    }

    public void setDiphtheriaPcr(String diphtheriaPcr) {
        this.diphtheriaPcr = diphtheriaPcr;
    }

    public void setDiphtheriaGramStain(String diphtheriaGramStain) {
        this.diphtheriaGramStain = diphtheriaGramStain;
    }

    public void setDiphtheriaLatexAgglutination(String diphtheriaLatexAgglutination) {
        this.diphtheriaLatexAgglutination = diphtheriaLatexAgglutination;
    }

    public void setDiphtheriaCqValueDetection(String diphtheriaCqValueDetection) {
        this.diphtheriaCqValueDetection = diphtheriaCqValueDetection;
    }

    public void setDiphtheriaSeQuencing(String diphtheriaSeQuencing) {
        this.diphtheriaSeQuencing = diphtheriaSeQuencing;
    }

    public void setDiphtheriaDnaMicroArray(String diphtheriaDnaMicroArray) {
        this.diphtheriaDnaMicroArray = diphtheriaDnaMicroArray;
    }

    public void setDiphtheriaOther(String diphtheriaOther) {
        this.diphtheriaOther = diphtheriaOther;
    }

    public void setTrachomaAntiBodyDetection(String trachomaAntiBodyDetection) {
        this.trachomaAntiBodyDetection = trachomaAntiBodyDetection;
    }

    public void setTrachomaAntigenDetection(String trachomaAntigenDetection) {
        this.trachomaAntigenDetection = trachomaAntigenDetection;
    }

    public void setTrachomaRapidTest(String trachomaRapidTest) {
        this.trachomaRapidTest = trachomaRapidTest;
    }

    public void setTrachomaCulture(String trachomaCulture) {
        this.trachomaCulture = trachomaCulture;
    }

    public void setTrachomaHistopathology(String trachomaHistopathology) {
        this.trachomaHistopathology = trachomaHistopathology;
    }

    public void setTrachomaIsolation(String trachomaIsolation) {
        this.trachomaIsolation = trachomaIsolation;
    }

    public void setTrachomaIgmSerumAntibody(String trachomaIgmSerumAntibody) {
        this.trachomaIgmSerumAntibody = trachomaIgmSerumAntibody;
    }

    public void setTrachomaIggSerumAntibody(String trachomaIggSerumAntibody) {
        this.trachomaIggSerumAntibody = trachomaIggSerumAntibody;
    }

    public void setTrachomaIgaSerumAntibody(String trachomaIgaSerumAntibody) {
        this.trachomaIgaSerumAntibody = trachomaIgaSerumAntibody;
    }

    public void setTrachomaIncubationTime(String trachomaIncubationTime) {
        this.trachomaIncubationTime = trachomaIncubationTime;
    }

    public void setTrachomaIndirectFluorescentAntibody(String trachomaIndirectFluorescentAntibody) {
        this.trachomaIndirectFluorescentAntibody = trachomaIndirectFluorescentAntibody;
    }

    public void setTrachomaMicroscopy(String trachomaMicroscopy) {
        this.trachomaMicroscopy = trachomaMicroscopy;
    }

    public void setTrachomaNeutralizingAntibodies(String trachomaNeutralizingAntibodies) {
        this.trachomaNeutralizingAntibodies = trachomaNeutralizingAntibodies;
    }

    public void setTrachomaPcr(String trachomaPcr) {
        this.trachomaPcr = trachomaPcr;
    }

    public void setTrachomaGramStain(String trachomaGramStain) {
        this.trachomaGramStain = trachomaGramStain;
    }

    public void setTrachomaLatexAgglutination(String trachomaLatexAgglutination) {
        this.trachomaLatexAgglutination = trachomaLatexAgglutination;
    }

    public void setTrachomaCqValueDetection(String trachomaCqValueDetection) {
        this.trachomaCqValueDetection = trachomaCqValueDetection;
    }

    public void setTrachomaSeQuencing(String trachomaSeQuencing) {
        this.trachomaSeQuencing = trachomaSeQuencing;
    }

    public void setTrachomaDnaMicroArray(String trachomaDnaMicroArray) {
        this.trachomaDnaMicroArray = trachomaDnaMicroArray;
    }

    public void setTrachomaOther(String trachomaOther) {
        this.trachomaOther = trachomaOther;
    }

    public void setYawsEndemicSyphilisAntiBodyDetection(String yawsEndemicSyphilisAntiBodyDetection) {
        this.yawsEndemicSyphilisAntiBodyDetection = yawsEndemicSyphilisAntiBodyDetection;
    }

    public void setYawsEndemicSyphilisAntigenDetection(String yawsEndemicSyphilisAntigenDetection) {
        this.yawsEndemicSyphilisAntigenDetection = yawsEndemicSyphilisAntigenDetection;
    }

    public void setYawsEndemicSyphilisRapidTest(String yawsEndemicSyphilisRapidTest) {
        this.yawsEndemicSyphilisRapidTest = yawsEndemicSyphilisRapidTest;
    }

    public void setYawsEndemicSyphilisCulture(String yawsEndemicSyphilisCulture) {
        this.yawsEndemicSyphilisCulture = yawsEndemicSyphilisCulture;
    }

    public void setYawsEndemicSyphilisHistopathology(String yawsEndemicSyphilisHistopathology) {
        this.yawsEndemicSyphilisHistopathology = yawsEndemicSyphilisHistopathology;
    }

    public void setYawsEndemicSyphilisIsolation(String yawsEndemicSyphilisIsolation) {
        this.yawsEndemicSyphilisIsolation = yawsEndemicSyphilisIsolation;
    }

    public void setYawsEndemicSyphilisIgmSerumAntibody(String yawsEndemicSyphilisIgmSerumAntibody) {
        this.yawsEndemicSyphilisIgmSerumAntibody = yawsEndemicSyphilisIgmSerumAntibody;
    }

    public void setYawsEndemicSyphilisIggSerumAntibody(String yawsEndemicSyphilisIggSerumAntibody) {
        this.yawsEndemicSyphilisIggSerumAntibody = yawsEndemicSyphilisIggSerumAntibody;
    }

    public void setYawsEndemicSyphilisIgaSerumAntibody(String yawsEndemicSyphilisIgaSerumAntibody) {
        this.yawsEndemicSyphilisIgaSerumAntibody = yawsEndemicSyphilisIgaSerumAntibody;
    }

    public void setYawsEndemicSyphilisIncubationTime(String yawsEndemicSyphilisIncubationTime) {
        this.yawsEndemicSyphilisIncubationTime = yawsEndemicSyphilisIncubationTime;
    }

    public void setYawsEndemicSyphilisIndirectFluorescentAntibody(String yawsEndemicSyphilisIndirectFluorescentAntibody) {
        this.yawsEndemicSyphilisIndirectFluorescentAntibody = yawsEndemicSyphilisIndirectFluorescentAntibody;
    }

    public void setYawsEndemicSyphilisMicroscopy(String yawsEndemicSyphilisMicroscopy) {
        this.yawsEndemicSyphilisMicroscopy = yawsEndemicSyphilisMicroscopy;
    }

    public void setYawsEndemicSyphilisNeutralizingAntibodies(String yawsEndemicSyphilisNeutralizingAntibodies) {
        this.yawsEndemicSyphilisNeutralizingAntibodies = yawsEndemicSyphilisNeutralizingAntibodies;
    }

    public void setYawsEndemicSyphilisPcr(String yawsEndemicSyphilisPcr) {
        this.yawsEndemicSyphilisPcr = yawsEndemicSyphilisPcr;
    }

    public void setYawsEndemicSyphilisGramStain(String yawsEndemicSyphilisGramStain) {
        this.yawsEndemicSyphilisGramStain = yawsEndemicSyphilisGramStain;
    }

    public void setYawsEndemicSyphilisLatexAgglutination(String yawsEndemicSyphilisLatexAgglutination) {
        this.yawsEndemicSyphilisLatexAgglutination = yawsEndemicSyphilisLatexAgglutination;
    }

    public void setYawsEndemicSyphilisCqValueDetection(String yawsEndemicSyphilisCqValueDetection) {
        this.yawsEndemicSyphilisCqValueDetection = yawsEndemicSyphilisCqValueDetection;
    }

    public void setYawsEndemicSyphilisSeQuencing(String yawsEndemicSyphilisSeQuencing) {
        this.yawsEndemicSyphilisSeQuencing = yawsEndemicSyphilisSeQuencing;
    }

    public void setYawsEndemicSyphilisDnaMicroArray(String yawsEndemicSyphilisDnaMicroArray) {
        this.yawsEndemicSyphilisDnaMicroArray = yawsEndemicSyphilisDnaMicroArray;
    }

    public void setYawsEndemicSyphilisOther(String yawsEndemicSyphilisOther) {
        this.yawsEndemicSyphilisOther = yawsEndemicSyphilisOther;
    }

    public void setMaternalDeathsAntiBodyDetection(String maternalDeathsAntiBodyDetection) {
        this.maternalDeathsAntiBodyDetection = maternalDeathsAntiBodyDetection;
    }

    public void setMaternalDeathsAntigenDetection(String maternalDeathsAntigenDetection) {
        this.maternalDeathsAntigenDetection = maternalDeathsAntigenDetection;
    }

    public void setMaternalDeathsRapidTest(String maternalDeathsRapidTest) {
        this.maternalDeathsRapidTest = maternalDeathsRapidTest;
    }

    public void setMaternalDeathsCulture(String maternalDeathsCulture) {
        this.maternalDeathsCulture = maternalDeathsCulture;
    }

    public void setMaternalDeathsHistopathology(String maternalDeathsHistopathology) {
        this.maternalDeathsHistopathology = maternalDeathsHistopathology;
    }

    public void setMaternalDeathsIsolation(String maternalDeathsIsolation) {
        this.maternalDeathsIsolation = maternalDeathsIsolation;
    }

    public void setMaternalDeathsIgmSerumAntibody(String maternalDeathsIgmSerumAntibody) {
        this.maternalDeathsIgmSerumAntibody = maternalDeathsIgmSerumAntibody;
    }

    public void setMaternalDeathsIggSerumAntibody(String maternalDeathsIggSerumAntibody) {
        this.maternalDeathsIggSerumAntibody = maternalDeathsIggSerumAntibody;
    }

    public void setMaternalDeathsIgaSerumAntibody(String maternalDeathsIgaSerumAntibody) {
        this.maternalDeathsIgaSerumAntibody = maternalDeathsIgaSerumAntibody;
    }

    public void setMaternalDeathsIncubationTime(String maternalDeathsIncubationTime) {
        this.maternalDeathsIncubationTime = maternalDeathsIncubationTime;
    }

    public void setMaternalDeathsIndirectFluorescentAntibody(String maternalDeathsIndirectFluorescentAntibody) {
        this.maternalDeathsIndirectFluorescentAntibody = maternalDeathsIndirectFluorescentAntibody;
    }

    public void setMaternalDeathsMicroscopy(String maternalDeathsMicroscopy) {
        this.maternalDeathsMicroscopy = maternalDeathsMicroscopy;
    }

    public void setMaternalDeathsNeutralizingAntibodies(String maternalDeathsNeutralizingAntibodies) {
        this.maternalDeathsNeutralizingAntibodies = maternalDeathsNeutralizingAntibodies;
    }

    public void setMaternalDeathsPcr(String maternalDeathsPcr) {
        this.maternalDeathsPcr = maternalDeathsPcr;
    }

    public void setMaternalDeathsGramStain(String maternalDeathsGramStain) {
        this.maternalDeathsGramStain = maternalDeathsGramStain;
    }

    public void setMaternalDeathsLatexAgglutination(String maternalDeathsLatexAgglutination) {
        this.maternalDeathsLatexAgglutination = maternalDeathsLatexAgglutination;
    }

    public void setMaternalDeathsCqValueDetection(String maternalDeathsCqValueDetection) {
        this.maternalDeathsCqValueDetection = maternalDeathsCqValueDetection;
    }

    public void setMaternalDeathsSeQuencing(String maternalDeathsSeQuencing) {
        this.maternalDeathsSeQuencing = maternalDeathsSeQuencing;
    }

    public void setMaternalDeathsDnaMicroArray(String maternalDeathsDnaMicroArray) {
        this.maternalDeathsDnaMicroArray = maternalDeathsDnaMicroArray;
    }

    public void setMaternalDeathsOther(String maternalDeathsOther) {
        this.maternalDeathsOther = maternalDeathsOther;
    }

    public void setPerinatalDeathsAntiBodyDetection(String perinatalDeathsAntiBodyDetection) {
        this.perinatalDeathsAntiBodyDetection = perinatalDeathsAntiBodyDetection;
    }

    public void setPerinatalDeathsAntigenDetection(String perinatalDeathsAntigenDetection) {
        this.perinatalDeathsAntigenDetection = perinatalDeathsAntigenDetection;
    }

    public void setPerinatalDeathsRapidTest(String perinatalDeathsRapidTest) {
        this.perinatalDeathsRapidTest = perinatalDeathsRapidTest;
    }

    public void setPerinatalDeathsCulture(String perinatalDeathsCulture) {
        this.perinatalDeathsCulture = perinatalDeathsCulture;
    }

    public void setPerinatalDeathsHistopathology(String perinatalDeathsHistopathology) {
        this.perinatalDeathsHistopathology = perinatalDeathsHistopathology;
    }

    public void setPerinatalDeathsIsolation(String perinatalDeathsIsolation) {
        this.perinatalDeathsIsolation = perinatalDeathsIsolation;
    }

    public void setPerinatalDeathsIgmSerumAntibody(String perinatalDeathsIgmSerumAntibody) {
        this.perinatalDeathsIgmSerumAntibody = perinatalDeathsIgmSerumAntibody;
    }

    public void setPerinatalDeathsIggSerumAntibody(String perinatalDeathsIggSerumAntibody) {
        this.perinatalDeathsIggSerumAntibody = perinatalDeathsIggSerumAntibody;
    }

    public void setPerinatalDeathsIgaSerumAntibody(String perinatalDeathsIgaSerumAntibody) {
        this.perinatalDeathsIgaSerumAntibody = perinatalDeathsIgaSerumAntibody;
    }

    public void setPerinatalDeathsIncubationTime(String perinatalDeathsIncubationTime) {
        this.perinatalDeathsIncubationTime = perinatalDeathsIncubationTime;
    }

    public void setPerinatalDeathsIndirectFluorescentAntibody(String perinatalDeathsIndirectFluorescentAntibody) {
        this.perinatalDeathsIndirectFluorescentAntibody = perinatalDeathsIndirectFluorescentAntibody;
    }

    public void setPerinatalDeathsMicroscopy(String perinatalDeathsMicroscopy) {
        this.perinatalDeathsMicroscopy = perinatalDeathsMicroscopy;
    }

    public void setPerinatalDeathsNeutralizingAntibodies(String perinatalDeathsNeutralizingAntibodies) {
        this.perinatalDeathsNeutralizingAntibodies = perinatalDeathsNeutralizingAntibodies;
    }

    public void setPerinatalDeathsPcr(String perinatalDeathsPcr) {
        this.perinatalDeathsPcr = perinatalDeathsPcr;
    }

    public void setPerinatalDeathsGramStain(String perinatalDeathsGramStain) {
        this.perinatalDeathsGramStain = perinatalDeathsGramStain;
    }

    public void setPerinatalDeathsLatexAgglutination(String perinatalDeathsLatexAgglutination) {
        this.perinatalDeathsLatexAgglutination = perinatalDeathsLatexAgglutination;
    }

    public void setPerinatalDeathsCqValueDetection(String perinatalDeathsCqValueDetection) {
        this.perinatalDeathsCqValueDetection = perinatalDeathsCqValueDetection;
    }

    public void setPerinatalDeathsSeQuencing(String perinatalDeathsSeQuencing) {
        this.perinatalDeathsSeQuencing = perinatalDeathsSeQuencing;
    }

    public void setPerinatalDeathsDnaMicroArray(String perinatalDeathsDnaMicroArray) {
        this.perinatalDeathsDnaMicroArray = perinatalDeathsDnaMicroArray;
    }

    public void setPerinatalDeathsOther(String perinatalDeathsOther) {
        this.perinatalDeathsOther = perinatalDeathsOther;
    }

    public void setInfluenzaAAntiBodyDetection(String influenzaAAntiBodyDetection) {
        this.influenzaAAntiBodyDetection = influenzaAAntiBodyDetection;
    }

    public void setInfluenzaAAntigenDetection(String influenzaAAntigenDetection) {
        this.influenzaAAntigenDetection = influenzaAAntigenDetection;
    }

    public void setInfluenzaARapidTest(String influenzaARapidTest) {
        this.influenzaARapidTest = influenzaARapidTest;
    }

    public void setInfluenzaACulture(String influenzaACulture) {
        this.influenzaACulture = influenzaACulture;
    }

    public void setInfluenzaAHistopathology(String influenzaAHistopathology) {
        this.influenzaAHistopathology = influenzaAHistopathology;
    }

    public void setInfluenzaAIsolation(String influenzaAIsolation) {
        this.influenzaAIsolation = influenzaAIsolation;
    }

    public void setInfluenzaAIgmSerumAntibody(String influenzaAIgmSerumAntibody) {
        this.influenzaAIgmSerumAntibody = influenzaAIgmSerumAntibody;
    }

    public void setInfluenzaAIggSerumAntibody(String influenzaAIggSerumAntibody) {
        this.influenzaAIggSerumAntibody = influenzaAIggSerumAntibody;
    }

    public void setInfluenzaAIgaSerumAntibody(String influenzaAIgaSerumAntibody) {
        this.influenzaAIgaSerumAntibody = influenzaAIgaSerumAntibody;
    }

    public void setInfluenzaAIncubationTime(String influenzaAIncubationTime) {
        this.influenzaAIncubationTime = influenzaAIncubationTime;
    }

    public void setInfluenzaAIndirectFluorescentAntibody(String influenzaAIndirectFluorescentAntibody) {
        this.influenzaAIndirectFluorescentAntibody = influenzaAIndirectFluorescentAntibody;
    }

    public void setInfluenzaAMicroscopy(String influenzaAMicroscopy) {
        this.influenzaAMicroscopy = influenzaAMicroscopy;
    }

    public void setInfluenzaANeutralizingAntibodies(String influenzaANeutralizingAntibodies) {
        this.influenzaANeutralizingAntibodies = influenzaANeutralizingAntibodies;
    }

    public void setInfluenzaAPcr(String influenzaAPcr) {
        this.influenzaAPcr = influenzaAPcr;
    }

    public void setInfluenzaAGramStain(String influenzaAGramStain) {
        this.influenzaAGramStain = influenzaAGramStain;
    }

    public void setInfluenzaALatexAgglutination(String influenzaALatexAgglutination) {
        this.influenzaALatexAgglutination = influenzaALatexAgglutination;
    }

    public void setInfluenzaACqValueDetection(String influenzaACqValueDetection) {
        this.influenzaACqValueDetection = influenzaACqValueDetection;
    }

    public void setInfluenzaASeQuencing(String influenzaASeQuencing) {
        this.influenzaASeQuencing = influenzaASeQuencing;
    }

    public void setInfluenzaADnaMicroArray(String influenzaADnaMicroArray) {
        this.influenzaADnaMicroArray = influenzaADnaMicroArray;
    }

    public void setInfluenzaAOther(String influenzaAOther) {
        this.influenzaAOther = influenzaAOther;
    }

    public void setInfluenzaBAntiBodyDetection(String influenzaBAntiBodyDetection) {
        this.influenzaBAntiBodyDetection = influenzaBAntiBodyDetection;
    }

    public void setInfluenzaBAntigenDetection(String influenzaBAntigenDetection) {
        this.influenzaBAntigenDetection = influenzaBAntigenDetection;
    }

    public void setInfluenzaBRapidTest(String influenzaBRapidTest) {
        this.influenzaBRapidTest = influenzaBRapidTest;
    }

    public void setInfluenzaBCulture(String influenzaBCulture) {
        this.influenzaBCulture = influenzaBCulture;
    }

    public void setInfluenzaBHistopathology(String influenzaBHistopathology) {
        this.influenzaBHistopathology = influenzaBHistopathology;
    }

    public void setInfluenzaBIsolation(String influenzaBIsolation) {
        this.influenzaBIsolation = influenzaBIsolation;
    }

    public void setInfluenzaBIgmSerumAntibody(String influenzaBIgmSerumAntibody) {
        this.influenzaBIgmSerumAntibody = influenzaBIgmSerumAntibody;
    }

    public void setInfluenzaBIggSerumAntibody(String influenzaBIggSerumAntibody) {
        this.influenzaBIggSerumAntibody = influenzaBIggSerumAntibody;
    }

    public void setInfluenzaBIgaSerumAntibody(String influenzaBIgaSerumAntibody) {
        this.influenzaBIgaSerumAntibody = influenzaBIgaSerumAntibody;
    }

    public void setInfluenzaBIncubationTime(String influenzaBIncubationTime) {
        this.influenzaBIncubationTime = influenzaBIncubationTime;
    }

    public void setInfluenzaBIndirectFluorescentAntibody(String influenzaBIndirectFluorescentAntibody) {
        this.influenzaBIndirectFluorescentAntibody = influenzaBIndirectFluorescentAntibody;
    }

    public void setInfluenzaBMicroscopy(String influenzaBMicroscopy) {
        this.influenzaBMicroscopy = influenzaBMicroscopy;
    }

    public void setInfluenzaBNeutralizingAntibodies(String influenzaBNeutralizingAntibodies) {
        this.influenzaBNeutralizingAntibodies = influenzaBNeutralizingAntibodies;
    }

    public void setInfluenzaBPcr(String influenzaBPcr) {
        this.influenzaBPcr = influenzaBPcr;
    }

    public void setInfluenzaBGramStain(String influenzaBGramStain) {
        this.influenzaBGramStain = influenzaBGramStain;
    }

    public void setInfluenzaBLatexAgglutination(String influenzaBLatexAgglutination) {
        this.influenzaBLatexAgglutination = influenzaBLatexAgglutination;
    }

    public void setInfluenzaBCqValueDetection(String influenzaBCqValueDetection) {
        this.influenzaBCqValueDetection = influenzaBCqValueDetection;
    }

    public void setInfluenzaBSeQuencing(String influenzaBSeQuencing) {
        this.influenzaBSeQuencing = influenzaBSeQuencing;
    }

    public void setInfluenzaBDnaMicroArray(String influenzaBDnaMicroArray) {
        this.influenzaBDnaMicroArray = influenzaBDnaMicroArray;
    }

    public void setInfluenzaBOther(String influenzaBOther) {
        this.influenzaBOther = influenzaBOther;
    }

    public void sethMetapneumovirusAntiBodyDetection(String hMetapneumovirusAntiBodyDetection) {
        this.hMetapneumovirusAntiBodyDetection = hMetapneumovirusAntiBodyDetection;
    }

    public void sethMetapneumovirusAntigenDetection(String hMetapneumovirusAntigenDetection) {
        this.hMetapneumovirusAntigenDetection = hMetapneumovirusAntigenDetection;
    }

    public void sethMetapneumovirusRapidTest(String hMetapneumovirusRapidTest) {
        this.hMetapneumovirusRapidTest = hMetapneumovirusRapidTest;
    }

    public void sethMetapneumovirusCulture(String hMetapneumovirusCulture) {
        this.hMetapneumovirusCulture = hMetapneumovirusCulture;
    }

    public void sethMetapneumovirusHistopathology(String hMetapneumovirusHistopathology) {
        this.hMetapneumovirusHistopathology = hMetapneumovirusHistopathology;
    }

    public void sethMetapneumovirusIsolation(String hMetapneumovirusIsolation) {
        this.hMetapneumovirusIsolation = hMetapneumovirusIsolation;
    }

    public void sethMetapneumovirusIgmSerumAntibody(String hMetapneumovirusIgmSerumAntibody) {
        this.hMetapneumovirusIgmSerumAntibody = hMetapneumovirusIgmSerumAntibody;
    }

    public void sethMetapneumovirusIggSerumAntibody(String hMetapneumovirusIggSerumAntibody) {
        this.hMetapneumovirusIggSerumAntibody = hMetapneumovirusIggSerumAntibody;
    }

    public void sethMetapneumovirusIgaSerumAntibody(String hMetapneumovirusIgaSerumAntibody) {
        this.hMetapneumovirusIgaSerumAntibody = hMetapneumovirusIgaSerumAntibody;
    }

    public void sethMetapneumovirusIncubationTime(String hMetapneumovirusIncubationTime) {
        this.hMetapneumovirusIncubationTime = hMetapneumovirusIncubationTime;
    }

    public void sethMetapneumovirusIndirectFluorescentAntibody(String hMetapneumovirusIndirectFluorescentAntibody) {
        this.hMetapneumovirusIndirectFluorescentAntibody = hMetapneumovirusIndirectFluorescentAntibody;
    }

    public void sethMetapneumovirusMicroscopy(String hMetapneumovirusMicroscopy) {
        this.hMetapneumovirusMicroscopy = hMetapneumovirusMicroscopy;
    }

    public void sethMetapneumovirusNeutralizingAntibodies(String hMetapneumovirusNeutralizingAntibodies) {
        this.hMetapneumovirusNeutralizingAntibodies = hMetapneumovirusNeutralizingAntibodies;
    }

    public void sethMetapneumovirusPcr(String hMetapneumovirusPcr) {
        this.hMetapneumovirusPcr = hMetapneumovirusPcr;
    }

    public void sethMetapneumovirusGramStain(String hMetapneumovirusGramStain) {
        this.hMetapneumovirusGramStain = hMetapneumovirusGramStain;
    }

    public void sethMetapneumovirusLatexAgglutination(String hMetapneumovirusLatexAgglutination) {
        this.hMetapneumovirusLatexAgglutination = hMetapneumovirusLatexAgglutination;
    }

    public void sethMetapneumovirusCqValueDetection(String hMetapneumovirusCqValueDetection) {
        this.hMetapneumovirusCqValueDetection = hMetapneumovirusCqValueDetection;
    }

    public void sethMetapneumovirusSeQuencing(String hMetapneumovirusSeQuencing) {
        this.hMetapneumovirusSeQuencing = hMetapneumovirusSeQuencing;
    }

    public void sethMetapneumovirusDnaMicroArray(String hMetapneumovirusDnaMicroArray) {
        this.hMetapneumovirusDnaMicroArray = hMetapneumovirusDnaMicroArray;
    }

    public void sethMetapneumovirusOther(String hMetapneumovirusOther) {
        this.hMetapneumovirusOther = hMetapneumovirusOther;
    }

    public void setRespiratorySyncytialVirusAntiBodyDetection(String respiratorySyncytialVirusAntiBodyDetection) {
        this.respiratorySyncytialVirusAntiBodyDetection = respiratorySyncytialVirusAntiBodyDetection;
    }

    public void setRespiratorySyncytialVirusAntigenDetection(String respiratorySyncytialVirusAntigenDetection) {
        this.respiratorySyncytialVirusAntigenDetection = respiratorySyncytialVirusAntigenDetection;
    }

    public void setRespiratorySyncytialVirusRapidTest(String respiratorySyncytialVirusRapidTest) {
        this.respiratorySyncytialVirusRapidTest = respiratorySyncytialVirusRapidTest;
    }

    public void setRespiratorySyncytialVirusCulture(String respiratorySyncytialVirusCulture) {
        this.respiratorySyncytialVirusCulture = respiratorySyncytialVirusCulture;
    }

    public void setRespiratorySyncytialVirusHistopathology(String respiratorySyncytialVirusHistopathology) {
        this.respiratorySyncytialVirusHistopathology = respiratorySyncytialVirusHistopathology;
    }

    public void setRespiratorySyncytialVirusIsolation(String respiratorySyncytialVirusIsolation) {
        this.respiratorySyncytialVirusIsolation = respiratorySyncytialVirusIsolation;
    }

    public void setRespiratorySyncytialVirusIgmSerumAntibody(String respiratorySyncytialVirusIgmSerumAntibody) {
        this.respiratorySyncytialVirusIgmSerumAntibody = respiratorySyncytialVirusIgmSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIggSerumAntibody(String respiratorySyncytialVirusIggSerumAntibody) {
        this.respiratorySyncytialVirusIggSerumAntibody = respiratorySyncytialVirusIggSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIgaSerumAntibody(String respiratorySyncytialVirusIgaSerumAntibody) {
        this.respiratorySyncytialVirusIgaSerumAntibody = respiratorySyncytialVirusIgaSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIncubationTime(String respiratorySyncytialVirusIncubationTime) {
        this.respiratorySyncytialVirusIncubationTime = respiratorySyncytialVirusIncubationTime;
    }

    public void setRespiratorySyncytialVirusIndirectFluorescentAntibody(String respiratorySyncytialVirusIndirectFluorescentAntibody) {
        this.respiratorySyncytialVirusIndirectFluorescentAntibody = respiratorySyncytialVirusIndirectFluorescentAntibody;
    }

    public void setRespiratorySyncytialVirusMicroscopy(String respiratorySyncytialVirusMicroscopy) {
        this.respiratorySyncytialVirusMicroscopy = respiratorySyncytialVirusMicroscopy;
    }

    public void setRespiratorySyncytialVirusNeutralizingAntibodies(String respiratorySyncytialVirusNeutralizingAntibodies) {
        this.respiratorySyncytialVirusNeutralizingAntibodies = respiratorySyncytialVirusNeutralizingAntibodies;
    }

    public void setRespiratorySyncytialVirusPcr(String respiratorySyncytialVirusPcr) {
        this.respiratorySyncytialVirusPcr = respiratorySyncytialVirusPcr;
    }

    public void setRespiratorySyncytialVirusGramStain(String respiratorySyncytialVirusGramStain) {
        this.respiratorySyncytialVirusGramStain = respiratorySyncytialVirusGramStain;
    }

    public void setRespiratorySyncytialVirusLatexAgglutination(String respiratorySyncytialVirusLatexAgglutination) {
        this.respiratorySyncytialVirusLatexAgglutination = respiratorySyncytialVirusLatexAgglutination;
    }

    public void setRespiratorySyncytialVirusCqValueDetection(String respiratorySyncytialVirusCqValueDetection) {
        this.respiratorySyncytialVirusCqValueDetection = respiratorySyncytialVirusCqValueDetection;
    }

    public void setRespiratorySyncytialVirusSeQuencing(String respiratorySyncytialVirusSeQuencing) {
        this.respiratorySyncytialVirusSeQuencing = respiratorySyncytialVirusSeQuencing;
    }

    public void setRespiratorySyncytialVirusDnaMicroArray(String respiratorySyncytialVirusDnaMicroArray) {
        this.respiratorySyncytialVirusDnaMicroArray = respiratorySyncytialVirusDnaMicroArray;
    }

    public void setRespiratorySyncytialVirusOther(String respiratorySyncytialVirusOther) {
        this.respiratorySyncytialVirusOther = respiratorySyncytialVirusOther;
    }

    public void setParainfluenza1_4AntiBodyDetection(String parainfluenza1_4AntiBodyDetection) {
        this.parainfluenza1_4AntiBodyDetection = parainfluenza1_4AntiBodyDetection;
    }

    public void setParainfluenza1_4AntigenDetection(String parainfluenza1_4AntigenDetection) {
        this.parainfluenza1_4AntigenDetection = parainfluenza1_4AntigenDetection;
    }

    public void setParainfluenza1_4RapidTest(String parainfluenza1_4RapidTest) {
        this.parainfluenza1_4RapidTest = parainfluenza1_4RapidTest;
    }

    public void setParainfluenza1_4Culture(String parainfluenza1_4Culture) {
        this.parainfluenza1_4Culture = parainfluenza1_4Culture;
    }

    public void setParainfluenza1_4Histopathology(String parainfluenza1_4Histopathology) {
        this.parainfluenza1_4Histopathology = parainfluenza1_4Histopathology;
    }

    public void setParainfluenza1_4Isolation(String parainfluenza1_4Isolation) {
        this.parainfluenza1_4Isolation = parainfluenza1_4Isolation;
    }

    public void setParainfluenza1_4IgmSerumAntibody(String parainfluenza1_4IgmSerumAntibody) {
        this.parainfluenza1_4IgmSerumAntibody = parainfluenza1_4IgmSerumAntibody;
    }

    public void setParainfluenza1_4IggSerumAntibody(String parainfluenza1_4IggSerumAntibody) {
        this.parainfluenza1_4IggSerumAntibody = parainfluenza1_4IggSerumAntibody;
    }

    public void setParainfluenza1_4IgaSerumAntibody(String parainfluenza1_4IgaSerumAntibody) {
        this.parainfluenza1_4IgaSerumAntibody = parainfluenza1_4IgaSerumAntibody;
    }

    public void setParainfluenza1_4IncubationTime(String parainfluenza1_4IncubationTime) {
        this.parainfluenza1_4IncubationTime = parainfluenza1_4IncubationTime;
    }

    public void setParainfluenza1_4IndirectFluorescentAntibody(String parainfluenza1_4IndirectFluorescentAntibody) {
        this.parainfluenza1_4IndirectFluorescentAntibody = parainfluenza1_4IndirectFluorescentAntibody;
    }

    public void setParainfluenza1_4Microscopy(String parainfluenza1_4Microscopy) {
        this.parainfluenza1_4Microscopy = parainfluenza1_4Microscopy;
    }

    public void setParainfluenza1_4NeutralizingAntibodies(String parainfluenza1_4NeutralizingAntibodies) {
        this.parainfluenza1_4NeutralizingAntibodies = parainfluenza1_4NeutralizingAntibodies;
    }

    public void setParainfluenza1_4Pcr(String parainfluenza1_4Pcr) {
        this.parainfluenza1_4Pcr = parainfluenza1_4Pcr;
    }

    public void setParainfluenza1_4GramStain(String parainfluenza1_4GramStain) {
        this.parainfluenza1_4GramStain = parainfluenza1_4GramStain;
    }

    public void setParainfluenza1_4LatexAgglutination(String parainfluenza1_4LatexAgglutination) {
        this.parainfluenza1_4LatexAgglutination = parainfluenza1_4LatexAgglutination;
    }

    public void setParainfluenza1_4CqValueDetection(String parainfluenza1_4CqValueDetection) {
        this.parainfluenza1_4CqValueDetection = parainfluenza1_4CqValueDetection;
    }

    public void setParainfluenza1_4SeQuencing(String parainfluenza1_4SeQuencing) {
        this.parainfluenza1_4SeQuencing = parainfluenza1_4SeQuencing;
    }

    public void setParainfluenza1_4DnaMicroArray(String parainfluenza1_4DnaMicroArray) {
        this.parainfluenza1_4DnaMicroArray = parainfluenza1_4DnaMicroArray;
    }

    public void setParainfluenza1_4Other(String parainfluenza1_4Other) {
        this.parainfluenza1_4Other = parainfluenza1_4Other;
    }

    public void setAdenovirusAntiBodyDetection(String adenovirusAntiBodyDetection) {
        this.adenovirusAntiBodyDetection = adenovirusAntiBodyDetection;
    }

    public void setAdenovirusAntigenDetection(String adenovirusAntigenDetection) {
        this.adenovirusAntigenDetection = adenovirusAntigenDetection;
    }

    public void setAdenovirusRapidTest(String adenovirusRapidTest) {
        this.adenovirusRapidTest = adenovirusRapidTest;
    }

    public void setAdenovirusCulture(String adenovirusCulture) {
        this.adenovirusCulture = adenovirusCulture;
    }

    public void setAdenovirusHistopathology(String adenovirusHistopathology) {
        this.adenovirusHistopathology = adenovirusHistopathology;
    }

    public void setAdenovirusIsolation(String adenovirusIsolation) {
        this.adenovirusIsolation = adenovirusIsolation;
    }

    public void setAdenovirusIgmSerumAntibody(String adenovirusIgmSerumAntibody) {
        this.adenovirusIgmSerumAntibody = adenovirusIgmSerumAntibody;
    }

    public void setAdenovirusIggSerumAntibody(String adenovirusIggSerumAntibody) {
        this.adenovirusIggSerumAntibody = adenovirusIggSerumAntibody;
    }

    public void setAdenovirusIgaSerumAntibody(String adenovirusIgaSerumAntibody) {
        this.adenovirusIgaSerumAntibody = adenovirusIgaSerumAntibody;
    }

    public void setAdenovirusIncubationTime(String adenovirusIncubationTime) {
        this.adenovirusIncubationTime = adenovirusIncubationTime;
    }

    public void setAdenovirusIndirectFluorescentAntibody(String adenovirusIndirectFluorescentAntibody) {
        this.adenovirusIndirectFluorescentAntibody = adenovirusIndirectFluorescentAntibody;
    }

    public void setAdenovirusMicroscopy(String adenovirusMicroscopy) {
        this.adenovirusMicroscopy = adenovirusMicroscopy;
    }

    public void setAdenovirusNeutralizingAntibodies(String adenovirusNeutralizingAntibodies) {
        this.adenovirusNeutralizingAntibodies = adenovirusNeutralizingAntibodies;
    }

    public void setAdenovirusPcr(String adenovirusPcr) {
        this.adenovirusPcr = adenovirusPcr;
    }

    public void setAdenovirusGramStain(String adenovirusGramStain) {
        this.adenovirusGramStain = adenovirusGramStain;
    }

    public void setAdenovirusLatexAgglutination(String adenovirusLatexAgglutination) {
        this.adenovirusLatexAgglutination = adenovirusLatexAgglutination;
    }

    public void setAdenovirusCqValueDetection(String adenovirusCqValueDetection) {
        this.adenovirusCqValueDetection = adenovirusCqValueDetection;
    }

    public void setAdenovirusSeQuencing(String adenovirusSeQuencing) {
        this.adenovirusSeQuencing = adenovirusSeQuencing;
    }

    public void setAdenovirusDnaMicroArray(String adenovirusDnaMicroArray) {
        this.adenovirusDnaMicroArray = adenovirusDnaMicroArray;
    }

    public void setAdenovirusOther(String adenovirusOther) {
        this.adenovirusOther = adenovirusOther;
    }

    public void setRhinovirusAntiBodyDetection(String rhinovirusAntiBodyDetection) {
        this.rhinovirusAntiBodyDetection = rhinovirusAntiBodyDetection;
    }

    public void setRhinovirusAntigenDetection(String rhinovirusAntigenDetection) {
        this.rhinovirusAntigenDetection = rhinovirusAntigenDetection;
    }

    public void setRhinovirusRapidTest(String rhinovirusRapidTest) {
        this.rhinovirusRapidTest = rhinovirusRapidTest;
    }

    public void setRhinovirusCulture(String rhinovirusCulture) {
        this.rhinovirusCulture = rhinovirusCulture;
    }

    public void setRhinovirusHistopathology(String rhinovirusHistopathology) {
        this.rhinovirusHistopathology = rhinovirusHistopathology;
    }

    public void setRhinovirusIsolation(String rhinovirusIsolation) {
        this.rhinovirusIsolation = rhinovirusIsolation;
    }

    public void setRhinovirusIgmSerumAntibody(String rhinovirusIgmSerumAntibody) {
        this.rhinovirusIgmSerumAntibody = rhinovirusIgmSerumAntibody;
    }

    public void setRhinovirusIggSerumAntibody(String rhinovirusIggSerumAntibody) {
        this.rhinovirusIggSerumAntibody = rhinovirusIggSerumAntibody;
    }

    public void setRhinovirusIgaSerumAntibody(String rhinovirusIgaSerumAntibody) {
        this.rhinovirusIgaSerumAntibody = rhinovirusIgaSerumAntibody;
    }

    public void setRhinovirusIncubationTime(String rhinovirusIncubationTime) {
        this.rhinovirusIncubationTime = rhinovirusIncubationTime;
    }

    public void setRhinovirusIndirectFluorescentAntibody(String rhinovirusIndirectFluorescentAntibody) {
        this.rhinovirusIndirectFluorescentAntibody = rhinovirusIndirectFluorescentAntibody;
    }

    public void setRhinovirusMicroscopy(String rhinovirusMicroscopy) {
        this.rhinovirusMicroscopy = rhinovirusMicroscopy;
    }

    public void setRhinovirusNeutralizingAntibodies(String rhinovirusNeutralizingAntibodies) {
        this.rhinovirusNeutralizingAntibodies = rhinovirusNeutralizingAntibodies;
    }

    public void setRhinovirusPcr(String rhinovirusPcr) {
        this.rhinovirusPcr = rhinovirusPcr;
    }

    public void setRhinovirusGramStain(String rhinovirusGramStain) {
        this.rhinovirusGramStain = rhinovirusGramStain;
    }

    public void setRhinovirusLatexAgglutination(String rhinovirusLatexAgglutination) {
        this.rhinovirusLatexAgglutination = rhinovirusLatexAgglutination;
    }

    public void setRhinovirusCqValueDetection(String rhinovirusCqValueDetection) {
        this.rhinovirusCqValueDetection = rhinovirusCqValueDetection;
    }

    public void setRhinovirusSeQuencing(String rhinovirusSeQuencing) {
        this.rhinovirusSeQuencing = rhinovirusSeQuencing;
    }

    public void setRhinovirusDnaMicroArray(String rhinovirusDnaMicroArray) {
        this.rhinovirusDnaMicroArray = rhinovirusDnaMicroArray;
    }

    public void setRhinovirusOther(String rhinovirusOther) {
        this.rhinovirusOther = rhinovirusOther;
    }

    public void setEnterovirusAntiBodyDetection(String enterovirusAntiBodyDetection) {
        this.enterovirusAntiBodyDetection = enterovirusAntiBodyDetection;
    }

    public void setEnterovirusAntigenDetection(String enterovirusAntigenDetection) {
        this.enterovirusAntigenDetection = enterovirusAntigenDetection;
    }

    public void setEnterovirusRapidTest(String enterovirusRapidTest) {
        this.enterovirusRapidTest = enterovirusRapidTest;
    }

    public void setEnterovirusCulture(String enterovirusCulture) {
        this.enterovirusCulture = enterovirusCulture;
    }

    public void setEnterovirusHistopathology(String enterovirusHistopathology) {
        this.enterovirusHistopathology = enterovirusHistopathology;
    }

    public void setEnterovirusIsolation(String enterovirusIsolation) {
        this.enterovirusIsolation = enterovirusIsolation;
    }

    public void setEnterovirusIgmSerumAntibody(String enterovirusIgmSerumAntibody) {
        this.enterovirusIgmSerumAntibody = enterovirusIgmSerumAntibody;
    }

    public void setEnterovirusIggSerumAntibody(String enterovirusIggSerumAntibody) {
        this.enterovirusIggSerumAntibody = enterovirusIggSerumAntibody;
    }

    public void setEnterovirusIgaSerumAntibody(String enterovirusIgaSerumAntibody) {
        this.enterovirusIgaSerumAntibody = enterovirusIgaSerumAntibody;
    }

    public void setEnterovirusIncubationTime(String enterovirusIncubationTime) {
        this.enterovirusIncubationTime = enterovirusIncubationTime;
    }

    public void setEnterovirusIndirectFluorescentAntibody(String enterovirusIndirectFluorescentAntibody) {
        this.enterovirusIndirectFluorescentAntibody = enterovirusIndirectFluorescentAntibody;
    }

    public void setEnterovirusMicroscopy(String enterovirusMicroscopy) {
        this.enterovirusMicroscopy = enterovirusMicroscopy;
    }

    public void setEnterovirusNeutralizingAntibodies(String enterovirusNeutralizingAntibodies) {
        this.enterovirusNeutralizingAntibodies = enterovirusNeutralizingAntibodies;
    }

    public void setEnterovirusPcr(String enterovirusPcr) {
        this.enterovirusPcr = enterovirusPcr;
    }

    public void setEnterovirusGramStain(String enterovirusGramStain) {
        this.enterovirusGramStain = enterovirusGramStain;
    }

    public void setEnterovirusLatexAgglutination(String enterovirusLatexAgglutination) {
        this.enterovirusLatexAgglutination = enterovirusLatexAgglutination;
    }

    public void setEnterovirusCqValueDetection(String enterovirusCqValueDetection) {
        this.enterovirusCqValueDetection = enterovirusCqValueDetection;
    }

    public void setEnterovirusSeQuencing(String enterovirusSeQuencing) {
        this.enterovirusSeQuencing = enterovirusSeQuencing;
    }

    public void setEnterovirusDnaMicroArray(String enterovirusDnaMicroArray) {
        this.enterovirusDnaMicroArray = enterovirusDnaMicroArray;
    }

    public void setEnterovirusOther(String enterovirusOther) {
        this.enterovirusOther = enterovirusOther;
    }

    public void setmPneumoniaeAntiBodyDetection(String mPneumoniaeAntiBodyDetection) {
        this.mPneumoniaeAntiBodyDetection = mPneumoniaeAntiBodyDetection;
    }

    public void setmPneumoniaeAntigenDetection(String mPneumoniaeAntigenDetection) {
        this.mPneumoniaeAntigenDetection = mPneumoniaeAntigenDetection;
    }

    public void setmPneumoniaeRapidTest(String mPneumoniaeRapidTest) {
        this.mPneumoniaeRapidTest = mPneumoniaeRapidTest;
    }

    public void setmPneumoniaeCulture(String mPneumoniaeCulture) {
        this.mPneumoniaeCulture = mPneumoniaeCulture;
    }

    public void setmPneumoniaeHistopathology(String mPneumoniaeHistopathology) {
        this.mPneumoniaeHistopathology = mPneumoniaeHistopathology;
    }

    public void setmPneumoniaeIsolation(String mPneumoniaeIsolation) {
        this.mPneumoniaeIsolation = mPneumoniaeIsolation;
    }

    public void setmPneumoniaeIgmSerumAntibody(String mPneumoniaeIgmSerumAntibody) {
        this.mPneumoniaeIgmSerumAntibody = mPneumoniaeIgmSerumAntibody;
    }

    public void setmPneumoniaeIggSerumAntibody(String mPneumoniaeIggSerumAntibody) {
        this.mPneumoniaeIggSerumAntibody = mPneumoniaeIggSerumAntibody;
    }

    public void setmPneumoniaeIgaSerumAntibody(String mPneumoniaeIgaSerumAntibody) {
        this.mPneumoniaeIgaSerumAntibody = mPneumoniaeIgaSerumAntibody;
    }

    public void setmPneumoniaeIncubationTime(String mPneumoniaeIncubationTime) {
        this.mPneumoniaeIncubationTime = mPneumoniaeIncubationTime;
    }

    public void setmPneumoniaeIndirectFluorescentAntibody(String mPneumoniaeIndirectFluorescentAntibody) {
        this.mPneumoniaeIndirectFluorescentAntibody = mPneumoniaeIndirectFluorescentAntibody;
    }

    public void setmPneumoniaeMicroscopy(String mPneumoniaeMicroscopy) {
        this.mPneumoniaeMicroscopy = mPneumoniaeMicroscopy;
    }

    public void setmPneumoniaeNeutralizingAntibodies(String mPneumoniaeNeutralizingAntibodies) {
        this.mPneumoniaeNeutralizingAntibodies = mPneumoniaeNeutralizingAntibodies;
    }

    public void setmPneumoniaePcr(String mPneumoniaePcr) {
        this.mPneumoniaePcr = mPneumoniaePcr;
    }

    public void setmPneumoniaeGramStain(String mPneumoniaeGramStain) {
        this.mPneumoniaeGramStain = mPneumoniaeGramStain;
    }

    public void setmPneumoniaeLatexAgglutination(String mPneumoniaeLatexAgglutination) {
        this.mPneumoniaeLatexAgglutination = mPneumoniaeLatexAgglutination;
    }

    public void setmPneumoniaeCqValueDetection(String mPneumoniaeCqValueDetection) {
        this.mPneumoniaeCqValueDetection = mPneumoniaeCqValueDetection;
    }

    public void setmPneumoniaeSeQuencing(String mPneumoniaeSeQuencing) {
        this.mPneumoniaeSeQuencing = mPneumoniaeSeQuencing;
    }

    public void setmPneumoniaeDnaMicroArray(String mPneumoniaeDnaMicroArray) {
        this.mPneumoniaeDnaMicroArray = mPneumoniaeDnaMicroArray;
    }

    public void setmPneumoniaeOther(String mPneumoniaeOther) {
        this.mPneumoniaeOther = mPneumoniaeOther;
    }

    public void setcPneumoniaeAntiBodyDetection(String cPneumoniaeAntiBodyDetection) {
        this.cPneumoniaeAntiBodyDetection = cPneumoniaeAntiBodyDetection;
    }

    public void setcPneumoniaeAntigenDetection(String cPneumoniaeAntigenDetection) {
        this.cPneumoniaeAntigenDetection = cPneumoniaeAntigenDetection;
    }

    public void setcPneumoniaeRapidTest(String cPneumoniaeRapidTest) {
        this.cPneumoniaeRapidTest = cPneumoniaeRapidTest;
    }

    public void setcPneumoniaeCulture(String cPneumoniaeCulture) {
        this.cPneumoniaeCulture = cPneumoniaeCulture;
    }

    public void setcPneumoniaeHistopathology(String cPneumoniaeHistopathology) {
        this.cPneumoniaeHistopathology = cPneumoniaeHistopathology;
    }

    public void setcPneumoniaeIsolation(String cPneumoniaeIsolation) {
        this.cPneumoniaeIsolation = cPneumoniaeIsolation;
    }

    public void setcPneumoniaeIgmSerumAntibody(String cPneumoniaeIgmSerumAntibody) {
        this.cPneumoniaeIgmSerumAntibody = cPneumoniaeIgmSerumAntibody;
    }

    public void setcPneumoniaeIggSerumAntibody(String cPneumoniaeIggSerumAntibody) {
        this.cPneumoniaeIggSerumAntibody = cPneumoniaeIggSerumAntibody;
    }

    public void setcPneumoniaeIgaSerumAntibody(String cPneumoniaeIgaSerumAntibody) {
        this.cPneumoniaeIgaSerumAntibody = cPneumoniaeIgaSerumAntibody;
    }

    public void setcPneumoniaeIncubationTime(String cPneumoniaeIncubationTime) {
        this.cPneumoniaeIncubationTime = cPneumoniaeIncubationTime;
    }

    public void setcPneumoniaeIndirectFluorescentAntibody(String cPneumoniaeIndirectFluorescentAntibody) {
        this.cPneumoniaeIndirectFluorescentAntibody = cPneumoniaeIndirectFluorescentAntibody;
    }

    public void setcPneumoniaeMicroscopy(String cPneumoniaeMicroscopy) {
        this.cPneumoniaeMicroscopy = cPneumoniaeMicroscopy;
    }

    public void setcPneumoniaeNeutralizingAntibodies(String cPneumoniaeNeutralizingAntibodies) {
        this.cPneumoniaeNeutralizingAntibodies = cPneumoniaeNeutralizingAntibodies;
    }

    public void setcPneumoniaePcr(String cPneumoniaePcr) {
        this.cPneumoniaePcr = cPneumoniaePcr;
    }

    public void setcPneumoniaeGramStain(String cPneumoniaeGramStain) {
        this.cPneumoniaeGramStain = cPneumoniaeGramStain;
    }

    public void setcPneumoniaeLatexAgglutination(String cPneumoniaeLatexAgglutination) {
        this.cPneumoniaeLatexAgglutination = cPneumoniaeLatexAgglutination;
    }

    public void setcPneumoniaeCqValueDetection(String cPneumoniaeCqValueDetection) {
        this.cPneumoniaeCqValueDetection = cPneumoniaeCqValueDetection;
    }

    public void setcPneumoniaeSeQuencing(String cPneumoniaeSeQuencing) {
        this.cPneumoniaeSeQuencing = cPneumoniaeSeQuencing;
    }

    public void setcPneumoniaeDnaMicroArray(String cPneumoniaeDnaMicroArray) {
        this.cPneumoniaeDnaMicroArray = cPneumoniaeDnaMicroArray;
    }

    public void setcPneumoniaeOther(String cPneumoniaeOther) {
        this.cPneumoniaeOther = cPneumoniaeOther;
    }

    public void setAriAntiBodyDetection(String ariAntiBodyDetection) {
        this.ariAntiBodyDetection = ariAntiBodyDetection;
    }

    public void setAriAntigenDetection(String ariAntigenDetection) {
        this.ariAntigenDetection = ariAntigenDetection;
    }

    public void setAriRapidTest(String ariRapidTest) {
        this.ariRapidTest = ariRapidTest;
    }

    public void setAriCulture(String ariCulture) {
        this.ariCulture = ariCulture;
    }

    public void setAriHistopathology(String ariHistopathology) {
        this.ariHistopathology = ariHistopathology;
    }

    public void setAriIsolation(String ariIsolation) {
        this.ariIsolation = ariIsolation;
    }

    public void setAriIgmSerumAntibody(String ariIgmSerumAntibody) {
        this.ariIgmSerumAntibody = ariIgmSerumAntibody;
    }

    public void setAriIggSerumAntibody(String ariIggSerumAntibody) {
        this.ariIggSerumAntibody = ariIggSerumAntibody;
    }

    public void setAriIgaSerumAntibody(String ariIgaSerumAntibody) {
        this.ariIgaSerumAntibody = ariIgaSerumAntibody;
    }

    public void setAriIncubationTime(String ariIncubationTime) {
        this.ariIncubationTime = ariIncubationTime;
    }

    public void setAriIndirectFluorescentAntibody(String ariIndirectFluorescentAntibody) {
        this.ariIndirectFluorescentAntibody = ariIndirectFluorescentAntibody;
    }

    public void setAriMicroscopy(String ariMicroscopy) {
        this.ariMicroscopy = ariMicroscopy;
    }

    public void setAriNeutralizingAntibodies(String ariNeutralizingAntibodies) {
        this.ariNeutralizingAntibodies = ariNeutralizingAntibodies;
    }

    public void setAriPcr(String ariPcr) {
        this.ariPcr = ariPcr;
    }

    public void setAriGramStain(String ariGramStain) {
        this.ariGramStain = ariGramStain;
    }

    public void setAriLatexAgglutination(String ariLatexAgglutination) {
        this.ariLatexAgglutination = ariLatexAgglutination;
    }

    public void setAriCqValueDetection(String ariCqValueDetection) {
        this.ariCqValueDetection = ariCqValueDetection;
    }

    public void setAriSeQuencing(String ariSeQuencing) {
        this.ariSeQuencing = ariSeQuencing;
    }

    public void setAriDnaMicroArray(String ariDnaMicroArray) {
        this.ariDnaMicroArray = ariDnaMicroArray;
    }

    public void setAriOther(String ariOther) {
        this.ariOther = ariOther;
    }

    public void setChikungunyaAntiBodyDetection(String chikungunyaAntiBodyDetection) {
        this.chikungunyaAntiBodyDetection = chikungunyaAntiBodyDetection;
    }

    public void setChikungunyaAntigenDetection(String chikungunyaAntigenDetection) {
        this.chikungunyaAntigenDetection = chikungunyaAntigenDetection;
    }

    public void setChikungunyaRapidTest(String chikungunyaRapidTest) {
        this.chikungunyaRapidTest = chikungunyaRapidTest;
    }

    public void setChikungunyaCulture(String chikungunyaCulture) {
        this.chikungunyaCulture = chikungunyaCulture;
    }

    public void setChikungunyaHistopathology(String chikungunyaHistopathology) {
        this.chikungunyaHistopathology = chikungunyaHistopathology;
    }

    public void setChikungunyaIsolation(String chikungunyaIsolation) {
        this.chikungunyaIsolation = chikungunyaIsolation;
    }

    public void setChikungunyaIgmSerumAntibody(String chikungunyaIgmSerumAntibody) {
        this.chikungunyaIgmSerumAntibody = chikungunyaIgmSerumAntibody;
    }

    public void setChikungunyaIggSerumAntibody(String chikungunyaIggSerumAntibody) {
        this.chikungunyaIggSerumAntibody = chikungunyaIggSerumAntibody;
    }

    public void setChikungunyaIgaSerumAntibody(String chikungunyaIgaSerumAntibody) {
        this.chikungunyaIgaSerumAntibody = chikungunyaIgaSerumAntibody;
    }

    public void setChikungunyaIncubationTime(String chikungunyaIncubationTime) {
        this.chikungunyaIncubationTime = chikungunyaIncubationTime;
    }

    public void setChikungunyaIndirectFluorescentAntibody(String chikungunyaIndirectFluorescentAntibody) {
        this.chikungunyaIndirectFluorescentAntibody = chikungunyaIndirectFluorescentAntibody;
    }

    public void setChikungunyaMicroscopy(String chikungunyaMicroscopy) {
        this.chikungunyaMicroscopy = chikungunyaMicroscopy;
    }

    public void setChikungunyaNeutralizingAntibodies(String chikungunyaNeutralizingAntibodies) {
        this.chikungunyaNeutralizingAntibodies = chikungunyaNeutralizingAntibodies;
    }

    public void setChikungunyaPcr(String chikungunyaPcr) {
        this.chikungunyaPcr = chikungunyaPcr;
    }

    public void setChikungunyaGramStain(String chikungunyaGramStain) {
        this.chikungunyaGramStain = chikungunyaGramStain;
    }

    public void setChikungunyaLatexAgglutination(String chikungunyaLatexAgglutination) {
        this.chikungunyaLatexAgglutination = chikungunyaLatexAgglutination;
    }

    public void setChikungunyaCqValueDetection(String chikungunyaCqValueDetection) {
        this.chikungunyaCqValueDetection = chikungunyaCqValueDetection;
    }

    public void setChikungunyaSeQuencing(String chikungunyaSeQuencing) {
        this.chikungunyaSeQuencing = chikungunyaSeQuencing;
    }

    public void setChikungunyaDnaMicroArray(String chikungunyaDnaMicroArray) {
        this.chikungunyaDnaMicroArray = chikungunyaDnaMicroArray;
    }

    public void setChikungunyaOther(String chikungunyaOther) {
        this.chikungunyaOther = chikungunyaOther;
    }

    public void setPostImmunizationAdverseEventsMildAntiBodyDetection(String postImmunizationAdverseEventsMildAntiBodyDetection) {
        this.postImmunizationAdverseEventsMildAntiBodyDetection = postImmunizationAdverseEventsMildAntiBodyDetection;
    }

    public void setPostImmunizationAdverseEventsMildAntigenDetection(String postImmunizationAdverseEventsMildAntigenDetection) {
        this.postImmunizationAdverseEventsMildAntigenDetection = postImmunizationAdverseEventsMildAntigenDetection;
    }

    public void setPostImmunizationAdverseEventsMildRapidTest(String postImmunizationAdverseEventsMildRapidTest) {
        this.postImmunizationAdverseEventsMildRapidTest = postImmunizationAdverseEventsMildRapidTest;
    }

    public void setPostImmunizationAdverseEventsMildCulture(String postImmunizationAdverseEventsMildCulture) {
        this.postImmunizationAdverseEventsMildCulture = postImmunizationAdverseEventsMildCulture;
    }

    public void setPostImmunizationAdverseEventsMildHistopathology(String postImmunizationAdverseEventsMildHistopathology) {
        this.postImmunizationAdverseEventsMildHistopathology = postImmunizationAdverseEventsMildHistopathology;
    }

    public void setPostImmunizationAdverseEventsMildIsolation(String postImmunizationAdverseEventsMildIsolation) {
        this.postImmunizationAdverseEventsMildIsolation = postImmunizationAdverseEventsMildIsolation;
    }

    public void setPostImmunizationAdverseEventsMildIgmSerumAntibody(String postImmunizationAdverseEventsMildIgmSerumAntibody) {
        this.postImmunizationAdverseEventsMildIgmSerumAntibody = postImmunizationAdverseEventsMildIgmSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIggSerumAntibody(String postImmunizationAdverseEventsMildIggSerumAntibody) {
        this.postImmunizationAdverseEventsMildIggSerumAntibody = postImmunizationAdverseEventsMildIggSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIgaSerumAntibody(String postImmunizationAdverseEventsMildIgaSerumAntibody) {
        this.postImmunizationAdverseEventsMildIgaSerumAntibody = postImmunizationAdverseEventsMildIgaSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIncubationTime(String postImmunizationAdverseEventsMildIncubationTime) {
        this.postImmunizationAdverseEventsMildIncubationTime = postImmunizationAdverseEventsMildIncubationTime;
    }

    public void setPostImmunizationAdverseEventsMildIndirectFluorescentAntibody(String postImmunizationAdverseEventsMildIndirectFluorescentAntibody) {
        this.postImmunizationAdverseEventsMildIndirectFluorescentAntibody = postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsMildMicroscopy(String postImmunizationAdverseEventsMildMicroscopy) {
        this.postImmunizationAdverseEventsMildMicroscopy = postImmunizationAdverseEventsMildMicroscopy;
    }

    public void setPostImmunizationAdverseEventsMildNeutralizingAntibodies(String postImmunizationAdverseEventsMildNeutralizingAntibodies) {
        this.postImmunizationAdverseEventsMildNeutralizingAntibodies = postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsMildPcr(String postImmunizationAdverseEventsMildPcr) {
        this.postImmunizationAdverseEventsMildPcr = postImmunizationAdverseEventsMildPcr;
    }

    public void setPostImmunizationAdverseEventsMildGramStain(String postImmunizationAdverseEventsMildGramStain) {
        this.postImmunizationAdverseEventsMildGramStain = postImmunizationAdverseEventsMildGramStain;
    }

    public void setPostImmunizationAdverseEventsMildLatexAgglutination(String postImmunizationAdverseEventsMildLatexAgglutination) {
        this.postImmunizationAdverseEventsMildLatexAgglutination = postImmunizationAdverseEventsMildLatexAgglutination;
    }

    public void setPostImmunizationAdverseEventsMildCqValueDetection(String postImmunizationAdverseEventsMildCqValueDetection) {
        this.postImmunizationAdverseEventsMildCqValueDetection = postImmunizationAdverseEventsMildCqValueDetection;
    }

    public void setPostImmunizationAdverseEventsMildSeQuencing(String postImmunizationAdverseEventsMildSeQuencing) {
        this.postImmunizationAdverseEventsMildSeQuencing = postImmunizationAdverseEventsMildSeQuencing;
    }

    public void setPostImmunizationAdverseEventsMildDnaMicroArray(String postImmunizationAdverseEventsMildDnaMicroArray) {
        this.postImmunizationAdverseEventsMildDnaMicroArray = postImmunizationAdverseEventsMildDnaMicroArray;
    }

    public void setPostImmunizationAdverseEventsMildOther(String postImmunizationAdverseEventsMildOther) {
        this.postImmunizationAdverseEventsMildOther = postImmunizationAdverseEventsMildOther;
    }

    public void setPostImmunizationAdverseEventsSevereAntiBodyDetection(String PostImmunizationAdverseEventsSevereAntiBodyDetection) {
        this.PostImmunizationAdverseEventsSevereAntiBodyDetection = PostImmunizationAdverseEventsSevereAntiBodyDetection;
    }

    public void setPostImmunizationAdverseEventsSevereAntigenDetection(String PostImmunizationAdverseEventsSevereAntigenDetection) {
        this.PostImmunizationAdverseEventsSevereAntigenDetection = PostImmunizationAdverseEventsSevereAntigenDetection;
    }

    public void setPostImmunizationAdverseEventsSevereRapidTest(String PostImmunizationAdverseEventsSevereRapidTest) {
        this.PostImmunizationAdverseEventsSevereRapidTest = PostImmunizationAdverseEventsSevereRapidTest;
    }

    public void setPostImmunizationAdverseEventsSevereCulture(String PostImmunizationAdverseEventsSevereCulture) {
        this.PostImmunizationAdverseEventsSevereCulture = PostImmunizationAdverseEventsSevereCulture;
    }

    public void setPostImmunizationAdverseEventsSevereHistopathology(String PostImmunizationAdverseEventsSevereHistopathology) {
        this.PostImmunizationAdverseEventsSevereHistopathology = PostImmunizationAdverseEventsSevereHistopathology;
    }

    public void setPostImmunizationAdverseEventsSevereIsolation(String PostImmunizationAdverseEventsSevereIsolation) {
        this.PostImmunizationAdverseEventsSevereIsolation = PostImmunizationAdverseEventsSevereIsolation;
    }

    public void setPostImmunizationAdverseEventsSevereIgmSerumAntibody(String PostImmunizationAdverseEventsSevereIgmSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIgmSerumAntibody = PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIggSerumAntibody(String PostImmunizationAdverseEventsSevereIggSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIggSerumAntibody = PostImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIgaSerumAntibody(String PostImmunizationAdverseEventsSevereIgaSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIgaSerumAntibody = PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIncubationTime(String PostImmunizationAdverseEventsSevereIncubationTime) {
        this.PostImmunizationAdverseEventsSevereIncubationTime = PostImmunizationAdverseEventsSevereIncubationTime;
    }

    public void setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(String PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody) {
        this.PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody = PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereMicroscopy(String PostImmunizationAdverseEventsSevereMicroscopy) {
        this.PostImmunizationAdverseEventsSevereMicroscopy = PostImmunizationAdverseEventsSevereMicroscopy;
    }

    public void setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(String PostImmunizationAdverseEventsSevereNeutralizingAntibodies) {
        this.PostImmunizationAdverseEventsSevereNeutralizingAntibodies = PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsSeverePcr(String PostImmunizationAdverseEventsSeverePcr) {
        this.PostImmunizationAdverseEventsSeverePcr = PostImmunizationAdverseEventsSeverePcr;
    }

    public void setPostImmunizationAdverseEventsSevereGramStain(String PostImmunizationAdverseEventsSevereGramStain) {
        this.PostImmunizationAdverseEventsSevereGramStain = PostImmunizationAdverseEventsSevereGramStain;
    }

    public void setPostImmunizationAdverseEventsSevereLatexAgglutination(String PostImmunizationAdverseEventsSevereLatexAgglutination) {
        this.PostImmunizationAdverseEventsSevereLatexAgglutination = PostImmunizationAdverseEventsSevereLatexAgglutination;
    }

    public void setPostImmunizationAdverseEventsSevereCqValueDetection(String PostImmunizationAdverseEventsSevereCqValueDetection) {
        this.PostImmunizationAdverseEventsSevereCqValueDetection = PostImmunizationAdverseEventsSevereCqValueDetection;
    }

    public void setPostImmunizationAdverseEventsSevereSeQuencing(String PostImmunizationAdverseEventsSevereSeQuencing) {
        this.PostImmunizationAdverseEventsSevereSeQuencing = PostImmunizationAdverseEventsSevereSeQuencing;
    }

    public void setPostImmunizationAdverseEventsSevereDnaMicroArray(String PostImmunizationAdverseEventsSevereDnaMicroArray) {
        this.PostImmunizationAdverseEventsSevereDnaMicroArray = PostImmunizationAdverseEventsSevereDnaMicroArray;
    }

    public void setPostImmunizationAdverseEventsSevereOther(String PostImmunizationAdverseEventsSevereOther) {
        this.PostImmunizationAdverseEventsSevereOther = PostImmunizationAdverseEventsSevereOther;
    }

    public void setFhaAntiBodyDetection(String fhaAntiBodyDetection) {
        this.fhaAntiBodyDetection = fhaAntiBodyDetection;
    }

    public void setFhaAntigenDetection(String fhaAntigenDetection) {
        this.fhaAntigenDetection = fhaAntigenDetection;
    }

    public void setFhaRapidTest(String fhaRapidTest) {
        this.fhaRapidTest = fhaRapidTest;
    }

    public void setFhaCulture(String fhaCulture) {
        this.fhaCulture = fhaCulture;
    }

    public void setFhaHistopathology(String fhaHistopathology) {
        this.fhaHistopathology = fhaHistopathology;
    }

    public void setFhaIsolation(String fhaIsolation) {
        this.fhaIsolation = fhaIsolation;
    }

    public void setFhaIgmSerumAntibody(String fhaIgmSerumAntibody) {
        this.fhaIgmSerumAntibody = fhaIgmSerumAntibody;
    }

    public void setFhaIggSerumAntibody(String fhaIggSerumAntibody) {
        this.fhaIggSerumAntibody = fhaIggSerumAntibody;
    }

    public void setFhaIgaSerumAntibody(String fhaIgaSerumAntibody) {
        this.fhaIgaSerumAntibody = fhaIgaSerumAntibody;
    }

    public void setFhaIncubationTime(String fhaIncubationTime) {
        this.fhaIncubationTime = fhaIncubationTime;
    }

    public void setFhaIndirectFluorescentAntibody(String fhaIndirectFluorescentAntibody) {
        this.fhaIndirectFluorescentAntibody = fhaIndirectFluorescentAntibody;
    }

    public void setFhaMicroscopy(String fhaMicroscopy) {
        this.fhaMicroscopy = fhaMicroscopy;
    }

    public void setFhaNeutralizingAntibodies(String fhaNeutralizingAntibodies) {
        this.fhaNeutralizingAntibodies = fhaNeutralizingAntibodies;
    }

    public void setFhaPcr(String fhaPcr) {
        this.fhaPcr = fhaPcr;
    }

    public void setFhaGramStain(String fhaGramStain) {
        this.fhaGramStain = fhaGramStain;
    }

    public void setFhaLatexAgglutination(String fhaLatexAgglutination) {
        this.fhaLatexAgglutination = fhaLatexAgglutination;
    }

    public void setFhaCqValueDetection(String fhaCqValueDetection) {
        this.fhaCqValueDetection = fhaCqValueDetection;
    }

    public void setFhaSeQuencing(String fhaSeQuencing) {
        this.fhaSeQuencing = fhaSeQuencing;
    }

    public void setFhaDnaMicroArray(String fhaDnaMicroArray) {
        this.fhaDnaMicroArray = fhaDnaMicroArray;
    }

    public void setFhaOther(String fhaOther) {
        this.fhaOther = fhaOther;
    }

    public void setOtherAntiBodyDetection(String otherAntiBodyDetection) {
        this.otherAntiBodyDetection = otherAntiBodyDetection;
    }

    public void setOtherAntigenDetection(String otherAntigenDetection) {
        this.otherAntigenDetection = otherAntigenDetection;
    }

    public void setOtherRapidTest(String otherRapidTest) {
        this.otherRapidTest = otherRapidTest;
    }

    public void setOtherCulture(String otherCulture) {
        this.otherCulture = otherCulture;
    }

    public void setOtherHistopathology(String otherHistopathology) {
        this.otherHistopathology = otherHistopathology;
    }

    public void setOtherIsolation(String otherIsolation) {
        this.otherIsolation = otherIsolation;
    }

    public void setOtherIgmSerumAntibody(String otherIgmSerumAntibody) {
        this.otherIgmSerumAntibody = otherIgmSerumAntibody;
    }

    public void setOtherIggSerumAntibody(String otherIggSerumAntibody) {
        this.otherIggSerumAntibody = otherIggSerumAntibody;
    }

    public void setOtherIgaSerumAntibody(String otherIgaSerumAntibody) {
        this.otherIgaSerumAntibody = otherIgaSerumAntibody;
    }

    public void setOtherIncubationTime(String otherIncubationTime) {
        this.otherIncubationTime = otherIncubationTime;
    }

    public void setOtherIndirectFluorescentAntibody(String otherIndirectFluorescentAntibody) {
        this.otherIndirectFluorescentAntibody = otherIndirectFluorescentAntibody;
    }

    public void setOtherMicroscopy(String otherMicroscopy) {
        this.otherMicroscopy = otherMicroscopy;
    }

    public void setOtherNeutralizingAntibodies(String otherNeutralizingAntibodies) {
        this.otherNeutralizingAntibodies = otherNeutralizingAntibodies;
    }

    public void setOtherPcr(String otherPcr) {
        this.otherPcr = otherPcr;
    }

    public void setOtherGramStain(String otherGramStain) {
        this.otherGramStain = otherGramStain;
    }

    public void setOtherLatexAgglutination(String otherLatexAgglutination) {
        this.otherLatexAgglutination = otherLatexAgglutination;
    }

    public void setOtherCqValueDetection(String otherCqValueDetection) {
        this.otherCqValueDetection = otherCqValueDetection;
    }

    public void setOtherSeQuencing(String otherSeQuencing) {
        this.otherSeQuencing = otherSeQuencing;
    }

    public void setOtherDnaMicroArray(String otherDnaMicroArray) {
        this.otherDnaMicroArray = otherDnaMicroArray;
    }

    public void setOtherOther(String otherOther) {
        this.otherOther = otherOther;
    }

    public void setUndefinedAntiBodyDetection(String undefinedAntiBodyDetection) {
        this.undefinedAntiBodyDetection = undefinedAntiBodyDetection;
    }

    public void setUndefinedAntigenDetection(String undefinedAntigenDetection) {
        this.undefinedAntigenDetection = undefinedAntigenDetection;
    }

    public void setUndefinedRapidTest(String undefinedRapidTest) {
        this.undefinedRapidTest = undefinedRapidTest;
    }

    public void setUndefinedCulture(String undefinedCulture) {
        this.undefinedCulture = undefinedCulture;
    }

    public void setUndefinedHistopathology(String undefinedHistopathology) {
        this.undefinedHistopathology = undefinedHistopathology;
    }

    public void setUndefinedIsolation(String undefinedIsolation) {
        this.undefinedIsolation = undefinedIsolation;
    }

    public void setUndefinedIgmSerumAntibody(String undefinedIgmSerumAntibody) {
        this.undefinedIgmSerumAntibody = undefinedIgmSerumAntibody;
    }

    public void setUndefinedIggSerumAntibody(String undefinedIggSerumAntibody) {
        this.undefinedIggSerumAntibody = undefinedIggSerumAntibody;
    }

    public void setUndefinedIgaSerumAntibody(String undefinedIgaSerumAntibody) {
        this.undefinedIgaSerumAntibody = undefinedIgaSerumAntibody;
    }

    public void setUndefinedIncubationTime(String undefinedIncubationTime) {
        this.undefinedIncubationTime = undefinedIncubationTime;
    }

    public void setUndefinedIndirectFluorescentAntibody(String undefinedIndirectFluorescentAntibody) {
        this.undefinedIndirectFluorescentAntibody = undefinedIndirectFluorescentAntibody;
    }

    public void setUndefinedMicroscopy(String undefinedMicroscopy) {
        this.undefinedMicroscopy = undefinedMicroscopy;
    }

    public void setUndefinedNeutralizingAntibodies(String undefinedNeutralizingAntibodies) {
        this.undefinedNeutralizingAntibodies = undefinedNeutralizingAntibodies;
    }

    public void setUndefinedPcr(String undefinedPcr) {
        this.undefinedPcr = undefinedPcr;
    }

    public void setUndefinedGramStain(String undefinedGramStain) {
        this.undefinedGramStain = undefinedGramStain;
    }

    public void setUndefinedLatexAgglutination(String undefinedLatexAgglutination) {
        this.undefinedLatexAgglutination = undefinedLatexAgglutination;
    }

    public void setUndefinedCqValueDetection(String undefinedCqValueDetection) {
        this.undefinedCqValueDetection = undefinedCqValueDetection;
    }

    public void setUndefinedSeQuencing(String undefinedSeQuencing) {
        this.undefinedSeQuencing = undefinedSeQuencing;
    }

    public void setUndefinedDnaMicroArray(String undefinedDnaMicroArray) {
        this.undefinedDnaMicroArray = undefinedDnaMicroArray;
    }

    public void setUndefinedOther(String undefinedOther) {
        this.undefinedOther = undefinedOther;
    }


    public String getAfpAntiBodyDetection() {
        return afpAntiBodyDetection;
    }

    public String getAfpAntigenDetection() {
        return afpAntigenDetection;
    }

    public String getAfpRapidTest() {
        return afpRapidTest;
    }

    public String getAfpCulture() {
        return afpCulture;
    }

    public String getAfpHistopathology() {
        return afpHistopathology;
    }

    public String getAfpIsolation() {
        return afpIsolation;
    }

    public String getAfpIgmSerumAntibody() {
        return afpIgmSerumAntibody;
    }

    public String getAfpIggSerumAntibody() {
        return afpIggSerumAntibody;
    }

    public String getAfpIgaSerumAntibody() {
        return afpIgaSerumAntibody;
    }

    public String getAfpIncubationTime() {
        return afpIncubationTime;
    }

    public String getAfpIndirectFluorescentAntibody() {
        return afpIndirectFluorescentAntibody;
    }

    public String getAfpMicroscopy() {
        return afpMicroscopy;
    }

    public String getAfpNeutralizingAntibodies() {
        return afpNeutralizingAntibodies;
    }

    public String getAfpPcr() {
        return afpPcr;
    }

    public String getAfpGramStain() {
        return afpGramStain;
    }

    public String getAfpLatexAgglutination() {
        return afpLatexAgglutination;
    }

    public String getAfpCqValueDetection() {
        return afpCqValueDetection;
    }

    public String getAfpSeQuencing() {
        return afpSeQuencing;
    }

    public String getAfpDnaMicroArray() {
        return afpDnaMicroArray;
    }

    public String getAfpOther() {
        return afpOther;
    }

    public String getCholeraAntiBodyDetection() {
        return choleraAntiBodyDetection;
    }

    public String getCholeraAntigenDetection() {
        return choleraAntigenDetection;
    }

    public String getCholeraRapidTest() {
        return choleraRapidTest;
    }

    public String getCholeraCulture() {
        return choleraCulture;
    }

    public String getCholeraHistopathology() {
        return choleraHistopathology;
    }

    public String getCholeraIsolation() {
        return choleraIsolation;
    }

    public String getCholeraIgmSerumAntibody() {
        return choleraIgmSerumAntibody;
    }

    public String getCholeraIggSerumAntibody() {
        return choleraIggSerumAntibody;
    }

    public String getCholeraIgaSerumAntibody() {
        return choleraIgaSerumAntibody;
    }

    public String getCholeraIncubationTime() {
        return choleraIncubationTime;
    }

    public String getCholeraIndirectFluorescentAntibody() {
        return choleraIndirectFluorescentAntibody;
    }

    public String getCholeraMicroscopy() {
        return choleraMicroscopy;
    }

    public String getCholeraNeutralizingAntibodies() {
        return choleraNeutralizingAntibodies;
    }

    public String getCholeraPcr() {
        return choleraPcr;
    }

    public String getCholeraGramStain() {
        return choleraGramStain;
    }

    public String getCholeraLatexAgglutination() {
        return choleraLatexAgglutination;
    }

    public String getCholeraCqValueDetection() {
        return choleraCqValueDetection;
    }

    public String getCholeraSeQuencing() {
        return choleraSeQuencing;
    }

    public String getCholeraDnaMicroArray() {
        return choleraDnaMicroArray;
    }

    public String getCholeraOther() {
        return choleraOther;
    }

    public String getCongenitalRubellaAntiBodyDetection() {
        return congenitalRubellaAntiBodyDetection;
    }

    public String getCongenitalRubellaAntigenDetection() {
        return congenitalRubellaAntigenDetection;
    }

    public String getCongenitalRubellaRapidTest() {
        return congenitalRubellaRapidTest;
    }

    public String getCongenitalRubellaCulture() {
        return congenitalRubellaCulture;
    }

    public String getCongenitalRubellaHistopathology() {
        return congenitalRubellaHistopathology;
    }

    public String getCongenitalRubellaIsolation() {
        return congenitalRubellaIsolation;
    }

    public String getCongenitalRubellaIgmSerumAntibody() {
        return congenitalRubellaIgmSerumAntibody;
    }

    public String getCongenitalRubellaIggSerumAntibody() {
        return congenitalRubellaIggSerumAntibody;
    }

    public String getCongenitalRubellaIgaSerumAntibody() {
        return congenitalRubellaIgaSerumAntibody;
    }

    public String getCongenitalRubellaIncubationTime() {
        return congenitalRubellaIncubationTime;
    }

    public String getCongenitalRubellaIndirectFluorescentAntibody() {
        return congenitalRubellaIndirectFluorescentAntibody;
    }

    public String getCongenitalRubellaMicroscopy() {
        return congenitalRubellaMicroscopy;
    }

    public String getCongenitalRubellaNeutralizingAntibodies() {
        return congenitalRubellaNeutralizingAntibodies;
    }

    public String getCongenitalRubellaPcr() {
        return congenitalRubellaPcr;
    }

    public String getCongenitalRubellaGramStain() {
        return congenitalRubellaGramStain;
    }

    public String getCongenitalRubellaLatexAgglutination() {
        return congenitalRubellaLatexAgglutination;
    }

    public String getCongenitalRubellaCqValueDetection() {
        return congenitalRubellaCqValueDetection;
    }

    public String getCongenitalRubellaSeQuencing() {
        return congenitalRubellaSeQuencing;
    }

    public String getCongenitalRubellaDnaMicroArray() {
        return congenitalRubellaDnaMicroArray;
    }

    public String getCongenitalRubellaOther() {
        return congenitalRubellaOther;
    }

    public String getCsmAntiBodyDetection() {
        return csmAntiBodyDetection;
    }

    public String getCsmAntigenDetection() {
        return csmAntigenDetection;
    }

    public String getCsmRapidTest() {
        return csmRapidTest;
    }

    public String getCsmCulture() {
        return csmCulture;
    }

    public String getCsmHistopathology() {
        return csmHistopathology;
    }

    public String getCsmIsolation() {
        return csmIsolation;
    }

    public String getCsmIgmSerumAntibody() {
        return csmIgmSerumAntibody;
    }

    public String getCsmIggSerumAntibody() {
        return csmIggSerumAntibody;
    }

    public String getCsmIgaSerumAntibody() {
        return csmIgaSerumAntibody;
    }

    public String getCsmIncubationTime() {
        return csmIncubationTime;
    }

    public String getCsmIndirectFluorescentAntibody() {
        return csmIndirectFluorescentAntibody;
    }

    public String getCsmMicroscopy() {
        return csmMicroscopy;
    }

    public String getCsmNeutralizingAntibodies() {
        return csmNeutralizingAntibodies;
    }

    public String getCsmPcr() {
        return csmPcr;
    }

    public String getCsmGramStain() {
        return csmGramStain;
    }

    public String getCsmLatexAgglutination() {
        return csmLatexAgglutination;
    }

    public String getCsmCqValueDetection() {
        return csmCqValueDetection;
    }

    public String getCsmSeQuencing() {
        return csmSeQuencing;
    }

    public String getCsmDnaMicroArray() {
        return csmDnaMicroArray;
    }

    public String getCsmOther() {
        return csmOther;
    }

    public String getDengueAntiBodyDetection() {
        return dengueAntiBodyDetection;
    }

    public String getDengueAntigenDetection() {
        return dengueAntigenDetection;
    }

    public String getDengueRapidTest() {
        return dengueRapidTest;
    }

    public String getDengueCulture() {
        return dengueCulture;
    }

    public String getDengueHistopathology() {
        return dengueHistopathology;
    }

    public String getDengueIsolation() {
        return dengueIsolation;
    }

    public String getDengueIgmSerumAntibody() {
        return dengueIgmSerumAntibody;
    }

    public String getDengueIggSerumAntibody() {
        return dengueIggSerumAntibody;
    }

    public String getDengueIgaSerumAntibody() {
        return dengueIgaSerumAntibody;
    }

    public String getDengueIncubationTime() {
        return dengueIncubationTime;
    }

    public String getDengueIndirectFluorescentAntibody() {
        return dengueIndirectFluorescentAntibody;
    }

    public String getDengueMicroscopy() {
        return dengueMicroscopy;
    }

    public String getDengueNeutralizingAntibodies() {
        return dengueNeutralizingAntibodies;
    }

    public String getDenguePcr() {
        return denguePcr;
    }

    public String getDengueGramStain() {
        return dengueGramStain;
    }

    public String getDengueLatexAgglutination() {
        return dengueLatexAgglutination;
    }

    public String getDengueCqValueDetection() {
        return dengueCqValueDetection;
    }

    public String getDengueSeQuencing() {
        return dengueSeQuencing;
    }

    public String getDengueDnaMicroArray() {
        return dengueDnaMicroArray;
    }

    public String getDengueOther() {
        return dengueOther;
    }

    public String getEvdAntiBodyDetection() {
        return evdAntiBodyDetection;
    }

    public String getEvdAntigenDetection() {
        return evdAntigenDetection;
    }

    public String getEvdRapidTest() {
        return evdRapidTest;
    }

    public String getEvdCulture() {
        return evdCulture;
    }

    public String getEvdHistopathology() {
        return evdHistopathology;
    }

    public String getEvdIsolation() {
        return evdIsolation;
    }

    public String getEvdIgmSerumAntibody() {
        return evdIgmSerumAntibody;
    }

    public String getEvdIggSerumAntibody() {
        return evdIggSerumAntibody;
    }

    public String getEvdIgaSerumAntibody() {
        return evdIgaSerumAntibody;
    }

    public String getEvdIncubationTime() {
        return evdIncubationTime;
    }

    public String getEvdIndirectFluorescentAntibody() {
        return evdIndirectFluorescentAntibody;
    }

    public String getEvdMicroscopy() {
        return evdMicroscopy;
    }

    public String getEvdNeutralizingAntibodies() {
        return evdNeutralizingAntibodies;
    }

    public String getEvdPcr() {
        return evdPcr;
    }

    public String getEvdGramStain() {
        return evdGramStain;
    }

    public String getEvdLatexAgglutination() {
        return evdLatexAgglutination;
    }

    public String getEvdCqValueDetection() {
        return evdCqValueDetection;
    }

    public String getEvdSeQuencing() {
        return evdSeQuencing;
    }

    public String getEvdDnaMicroArray() {
        return evdDnaMicroArray;
    }

    public String getEvdOther() {
        return evdOther;
    }

    public String getGuineaWormAntiBodyDetection() {
        return guineaWormAntiBodyDetection;
    }

    public String getGuineaWormAntigenDetection() {
        return guineaWormAntigenDetection;
    }

    public String getGuineaWormRapidTest() {
        return guineaWormRapidTest;
    }

    public String getGuineaWormCulture() {
        return guineaWormCulture;
    }

    public String getGuineaWormHistopathology() {
        return guineaWormHistopathology;
    }

    public String getGuineaWormIsolation() {
        return guineaWormIsolation;
    }

    public String getGuineaWormIgmSerumAntibody() {
        return guineaWormIgmSerumAntibody;
    }

    public String getGuineaWormIggSerumAntibody() {
        return guineaWormIggSerumAntibody;
    }

    public String getGuineaWormIgaSerumAntibody() {
        return guineaWormIgaSerumAntibody;
    }

    public String getGuineaWormIncubationTime() {
        return guineaWormIncubationTime;
    }

    public String getGuineaWormIndirectFluorescentAntibody() {
        return guineaWormIndirectFluorescentAntibody;
    }

    public String getGuineaWormMicroscopy() {
        return guineaWormMicroscopy;
    }

    public String getGuineaWormNeutralizingAntibodies() {
        return guineaWormNeutralizingAntibodies;
    }

    public String getGuineaWormPcr() {
        return guineaWormPcr;
    }

    public String getGuineaWormGramStain() {
        return guineaWormGramStain;
    }

    public String getGuineaWormLatexAgglutination() {
        return guineaWormLatexAgglutination;
    }

    public String getGuineaWormCqValueDetection() {
        return guineaWormCqValueDetection;
    }

    public String getGuineaWormSeQuencing() {
        return guineaWormSeQuencing;
    }

    public String getGuineaWormDnaMicroArray() {
        return guineaWormDnaMicroArray;
    }

    public String getGuineaWormOther() {
        return guineaWormOther;
    }

    public String getLassaAntiBodyDetection() {
        return lassaAntiBodyDetection;
    }

    public String getLassaAntigenDetection() {
        return lassaAntigenDetection;
    }

    public String getLassaRapidTest() {
        return lassaRapidTest;
    }

    public String getLassaCulture() {
        return lassaCulture;
    }

    public String getLassaHistopathology() {
        return lassaHistopathology;
    }

    public String getLassaIsolation() {
        return lassaIsolation;
    }

    public String getLassaIgmSerumAntibody() {
        return lassaIgmSerumAntibody;
    }

    public String getLassaIggSerumAntibody() {
        return lassaIggSerumAntibody;
    }

    public String getLassaIgaSerumAntibody() {
        return lassaIgaSerumAntibody;
    }

    public String getLassaIncubationTime() {
        return lassaIncubationTime;
    }

    public String getLassaIndirectFluorescentAntibody() {
        return lassaIndirectFluorescentAntibody;
    }

    public String getLassaMicroscopy() {
        return lassaMicroscopy;
    }

    public String getLassaNeutralizingAntibodies() {
        return lassaNeutralizingAntibodies;
    }

    public String getLassaPcr() {
        return lassaPcr;
    }

    public String getLassaGramStain() {
        return lassaGramStain;
    }

    public String getLassaLatexAgglutination() {
        return lassaLatexAgglutination;
    }

    public String getLassaCqValueDetection() {
        return lassaCqValueDetection;
    }

    public String getLassaSeQuencing() {
        return lassaSeQuencing;
    }

    public String getLassaDnaMicroArray() {
        return lassaDnaMicroArray;
    }

    public String getLassaOther() {
        return lassaOther;
    }

    public String getMeaslesAntiBodyDetection() {
        return measlesAntiBodyDetection;
    }

    public String getMeaslesAntigenDetection() {
        return measlesAntigenDetection;
    }

    public String getMeaslesRapidTest() {
        return measlesRapidTest;
    }

    public String getMeaslesCulture() {
        return measlesCulture;
    }

    public String getMeaslesHistopathology() {
        return measlesHistopathology;
    }

    public String getMeaslesIsolation() {
        return measlesIsolation;
    }

    public String getMeaslesIgmSerumAntibody() {
        return measlesIgmSerumAntibody;
    }

    public String getMeaslesIggSerumAntibody() {
        return measlesIggSerumAntibody;
    }

    public String getMeaslesIgaSerumAntibody() {
        return measlesIgaSerumAntibody;
    }

    public String getMeaslesIncubationTime() {
        return measlesIncubationTime;
    }

    public String getMeaslesIndirectFluorescentAntibody() {
        return measlesIndirectFluorescentAntibody;
    }

    public String getMeaslesMicroscopy() {
        return measlesMicroscopy;
    }

    public String getMeaslesNeutralizingAntibodies() {
        return measlesNeutralizingAntibodies;
    }

    public String getMeaslesPcr() {
        return measlesPcr;
    }

    public String getMeaslesGramStain() {
        return measlesGramStain;
    }

    public String getMeaslesLatexAgglutination() {
        return measlesLatexAgglutination;
    }

    public String getMeaslesCqValueDetection() {
        return measlesCqValueDetection;
    }

    public String getMeaslesSeQuencing() {
        return measlesSeQuencing;
    }

    public String getMeaslesDnaMicroArray() {
        return measlesDnaMicroArray;
    }

    public String getMeaslesOther() {
        return measlesOther;
    }

    public String getMonkeyPoxAntiBodyDetection() {
        return monkeyPoxAntiBodyDetection;
    }

    public String getMonkeyPoxAntigenDetection() {
        return monkeyPoxAntigenDetection;
    }

    public String getMonkeyPoxRapidTest() {
        return monkeyPoxRapidTest;
    }

    public String getMonkeyPoxCulture() {
        return monkeyPoxCulture;
    }

    public String getMonkeyPoxHistopathology() {
        return monkeyPoxHistopathology;
    }

    public String getMonkeyPoxIsolation() {
        return monkeyPoxIsolation;
    }

    public String getMonkeyPoxIgmSerumAntibody() {
        return monkeyPoxIgmSerumAntibody;
    }

    public String getMonkeyPoxIggSerumAntibody() {
        return monkeyPoxIggSerumAntibody;
    }

    public String getMonkeyPoxIgaSerumAntibody() {
        return monkeyPoxIgaSerumAntibody;
    }

    public String getMonkeyPoxIncubationTime() {
        return monkeyPoxIncubationTime;
    }

    public String getMonkeyPoxIndirectFluorescentAntibody() {
        return monkeyPoxIndirectFluorescentAntibody;
    }

    public String getMonkeyPoxMicroscopy() {
        return monkeyPoxMicroscopy;
    }

    public String getMonkeyPoxNeutralizingAntibodies() {
        return monkeyPoxNeutralizingAntibodies;
    }

    public String getMonkeyPoxPcr() {
        return monkeyPoxPcr;
    }

    public String getMonkeyPoxGramStain() {
        return monkeyPoxGramStain;
    }

    public String getMonkeyPoxLatexAgglutination() {
        return monkeyPoxLatexAgglutination;
    }

    public String getMonkeyPoxCqValueDetection() {
        return monkeyPoxCqValueDetection;
    }

    public String getMonkeyPoxSeQuencing() {
        return monkeyPoxSeQuencing;
    }

    public String getMonkeyPoxDnaMicroArray() {
        return monkeyPoxDnaMicroArray;
    }

    public String getMonkeyPoxOther() {
        return monkeyPoxOther;
    }

    public String getMonkeypoxAntiBodyDetection() {
        return monkeypoxAntiBodyDetection;
    }

    public String getMonkeypoxAntigenDetection() {
        return monkeypoxAntigenDetection;
    }

    public String getMonkeypoxRapidTest() {
        return monkeypoxRapidTest;
    }

    public String getMonkeypoxCulture() {
        return monkeypoxCulture;
    }

    public String getMonkeypoxHistopathology() {
        return monkeypoxHistopathology;
    }

    public String getMonkeypoxIsolation() {
        return monkeypoxIsolation;
    }

    public String getMonkeypoxIgmSerumAntibody() {
        return monkeypoxIgmSerumAntibody;
    }

    public String getMonkeypoxIggSerumAntibody() {
        return monkeypoxIggSerumAntibody;
    }

    public String getMonkeypoxIgaSerumAntibody() {
        return monkeypoxIgaSerumAntibody;
    }

    public String getMonkeypoxIncubationTime() {
        return monkeypoxIncubationTime;
    }

    public String getMonkeypoxIndirectFluorescentAntibody() {
        return monkeypoxIndirectFluorescentAntibody;
    }

    public String getMonkeypoxMicroscopy() {
        return monkeypoxMicroscopy;
    }

    public String getMonkeypoxNeutralizingAntibodies() {
        return monkeypoxNeutralizingAntibodies;
    }

    public String getMonkeypoxPcr() {
        return monkeypoxPcr;
    }

    public String getMonkeypoxGramStain() {
        return monkeypoxGramStain;
    }

    public String getMonkeypoxLatexAgglutination() {
        return monkeypoxLatexAgglutination;
    }

    public String getMonkeypoxCqValueDetection() {
        return monkeypoxCqValueDetection;
    }

    public String getMonkeypoxSeQuencing() {
        return monkeypoxSeQuencing;
    }

    public String getMonkeypoxDnaMicroArray() {
        return monkeypoxDnaMicroArray;
    }

    public String getMonkeypoxOther() {
        return monkeypoxOther;
    }

    public String getNewInfluenzaAntiBodyDetection() {
        return newInfluenzaAntiBodyDetection;
    }

    public String getNewInfluenzaAntigenDetection() {
        return newInfluenzaAntigenDetection;
    }

    public String getNewInfluenzaRapidTest() {
        return newInfluenzaRapidTest;
    }

    public String getNewInfluenzaCulture() {
        return newInfluenzaCulture;
    }

    public String getNewInfluenzaHistopathology() {
        return newInfluenzaHistopathology;
    }

    public String getNewInfluenzaIsolation() {
        return newInfluenzaIsolation;
    }

    public String getNewInfluenzaIgmSerumAntibody() {
        return newInfluenzaIgmSerumAntibody;
    }

    public String getNewInfluenzaIggSerumAntibody() {
        return newInfluenzaIggSerumAntibody;
    }

    public String getNewInfluenzaIgaSerumAntibody() {
        return newInfluenzaIgaSerumAntibody;
    }

    public String getNewInfluenzaIncubationTime() {
        return newInfluenzaIncubationTime;
    }

    public String getNewInfluenzaIndirectFluorescentAntibody() {
        return newInfluenzaIndirectFluorescentAntibody;
    }

    public String getNewInfluenzaMicroscopy() {
        return newInfluenzaMicroscopy;
    }

    public String getNewInfluenzaNeutralizingAntibodies() {
        return newInfluenzaNeutralizingAntibodies;
    }

    public String getNewInfluenzaPcr() {
        return newInfluenzaPcr;
    }

    public String getNewInfluenzaGramStain() {
        return newInfluenzaGramStain;
    }

    public String getNewInfluenzaLatexAgglutination() {
        return newInfluenzaLatexAgglutination;
    }

    public String getNewInfluenzaCqValueDetection() {
        return newInfluenzaCqValueDetection;
    }

    public String getNewInfluenzaSeQuencing() {
        return newInfluenzaSeQuencing;
    }

    public String getNewInfluenzaDnaMicroArray() {
        return newInfluenzaDnaMicroArray;
    }

    public String getNewInfluenzaOther() {
        return newInfluenzaOther;
    }

    public String getPlagueAntiBodyDetection() {
        return plagueAntiBodyDetection;
    }

    public String getPlagueAntigenDetection() {
        return plagueAntigenDetection;
    }

    public String getPlagueRapidTest() {
        return plagueRapidTest;
    }

    public String getPlagueCulture() {
        return plagueCulture;
    }

    public String getPlagueHistopathology() {
        return plagueHistopathology;
    }

    public String getPlagueIsolation() {
        return plagueIsolation;
    }

    public String getPlagueIgmSerumAntibody() {
        return plagueIgmSerumAntibody;
    }

    public String getPlagueIggSerumAntibody() {
        return plagueIggSerumAntibody;
    }

    public String getPlagueIgaSerumAntibody() {
        return plagueIgaSerumAntibody;
    }

    public String getPlagueIncubationTime() {
        return plagueIncubationTime;
    }

    public String getPlagueIndirectFluorescentAntibody() {
        return plagueIndirectFluorescentAntibody;
    }

    public String getPlagueMicroscopy() {
        return plagueMicroscopy;
    }

    public String getPlagueNeutralizingAntibodies() {
        return plagueNeutralizingAntibodies;
    }

    public String getPlaguePcr() {
        return plaguePcr;
    }

    public String getPlagueGramStain() {
        return plagueGramStain;
    }

    public String getPlagueLatexAgglutination() {
        return plagueLatexAgglutination;
    }

    public String getPlagueCqValueDetection() {
        return plagueCqValueDetection;
    }

    public String getPlagueSeQuencing() {
        return plagueSeQuencing;
    }

    public String getPlagueDnaMicroArray() {
        return plagueDnaMicroArray;
    }

    public String getPlagueOther() {
        return plagueOther;
    }

    public String getPolioAntiBodyDetection() {
        return polioAntiBodyDetection;
    }

    public String getPolioAntigenDetection() {
        return polioAntigenDetection;
    }

    public String getPolioRapidTest() {
        return polioRapidTest;
    }

    public String getPolioCulture() {
        return polioCulture;
    }

    public String getPolioHistopathology() {
        return polioHistopathology;
    }

    public String getPolioIsolation() {
        return polioIsolation;
    }

    public String getPolioIgmSerumAntibody() {
        return polioIgmSerumAntibody;
    }

    public String getPolioIggSerumAntibody() {
        return polioIggSerumAntibody;
    }

    public String getPolioIgaSerumAntibody() {
        return polioIgaSerumAntibody;
    }

    public String getPolioIncubationTime() {
        return polioIncubationTime;
    }

    public String getPolioIndirectFluorescentAntibody() {
        return polioIndirectFluorescentAntibody;
    }

    public String getPolioMicroscopy() {
        return polioMicroscopy;
    }

    public String getPolioNeutralizingAntibodies() {
        return polioNeutralizingAntibodies;
    }

    public String getPolioPcr() {
        return polioPcr;
    }

    public String getPolioGramStain() {
        return polioGramStain;
    }

    public String getPolioLatexAgglutination() {
        return polioLatexAgglutination;
    }

    public String getPolioCqValueDetection() {
        return polioCqValueDetection;
    }

    public String getPolioSeQuencing() {
        return polioSeQuencing;
    }

    public String getPolioDnaMicroArray() {
        return polioDnaMicroArray;
    }

    public String getPolioOther() {
        return polioOther;
    }

    public String getUnspecifiedVhfAntiBodyDetection() {
        return unspecifiedVhfAntiBodyDetection;
    }

    public String getUnspecifiedVhfAntigenDetection() {
        return unspecifiedVhfAntigenDetection;
    }

    public String getUnspecifiedVhfRapidTest() {
        return unspecifiedVhfRapidTest;
    }

    public String getUnspecifiedVhfCulture() {
        return unspecifiedVhfCulture;
    }

    public String getUnspecifiedVhfHistopathology() {
        return unspecifiedVhfHistopathology;
    }

    public String getUnspecifiedVhfIsolation() {
        return unspecifiedVhfIsolation;
    }

    public String getUnspecifiedVhfIgmSerumAntibody() {
        return unspecifiedVhfIgmSerumAntibody;
    }

    public String getUnspecifiedVhfIggSerumAntibody() {
        return unspecifiedVhfIggSerumAntibody;
    }

    public String getUnspecifiedVhfIgaSerumAntibody() {
        return unspecifiedVhfIgaSerumAntibody;
    }

    public String getUnspecifiedVhfIncubationTime() {
        return unspecifiedVhfIncubationTime;
    }

    public String getUnspecifiedVhfIndirectFluorescentAntibody() {
        return unspecifiedVhfIndirectFluorescentAntibody;
    }

    public String getUnspecifiedVhfMicroscopy() {
        return unspecifiedVhfMicroscopy;
    }

    public String getUnspecifiedVhfNeutralizingAntibodies() {
        return unspecifiedVhfNeutralizingAntibodies;
    }

    public String getUnspecifiedVhfPcr() {
        return unspecifiedVhfPcr;
    }

    public String getUnspecifiedVhfGramStain() {
        return unspecifiedVhfGramStain;
    }

    public String getUnspecifiedVhfLatexAgglutination() {
        return unspecifiedVhfLatexAgglutination;
    }

    public String getUnspecifiedVhfCqValueDetection() {
        return unspecifiedVhfCqValueDetection;
    }

    public String getUnspecifiedVhfSeQuencing() {
        return unspecifiedVhfSeQuencing;
    }

    public String getUnspecifiedVhfDnaMicroArray() {
        return unspecifiedVhfDnaMicroArray;
    }

    public String getUnspecifiedVhfOther() {
        return unspecifiedVhfOther;
    }

    public String getYellowFeverAntiBodyDetection() {
        return yellowFeverAntiBodyDetection;
    }

    public String getYellowFeverAntigenDetection() {
        return yellowFeverAntigenDetection;
    }

    public String getYellowFeverRapidTest() {
        return yellowFeverRapidTest;
    }

    public String getYellowFeverCulture() {
        return yellowFeverCulture;
    }

    public String getYellowFeverHistopathology() {
        return yellowFeverHistopathology;
    }

    public String getYellowFeverIsolation() {
        return yellowFeverIsolation;
    }

    public String getYellowFeverIgmSerumAntibody() {
        return yellowFeverIgmSerumAntibody;
    }

    public String getYellowFeverIggSerumAntibody() {
        return yellowFeverIggSerumAntibody;
    }

    public String getYellowFeverIgaSerumAntibody() {
        return yellowFeverIgaSerumAntibody;
    }

    public String getYellowFeverIncubationTime() {
        return yellowFeverIncubationTime;
    }

    public String getYellowFeverIndirectFluorescentAntibody() {
        return yellowFeverIndirectFluorescentAntibody;
    }

    public String getYellowFeverMicroscopy() {
        return yellowFeverMicroscopy;
    }

    public String getYellowFeverNeutralizingAntibodies() {
        return yellowFeverNeutralizingAntibodies;
    }

    public String getYellowFeverPcr() {
        return yellowFeverPcr;
    }

    public String getYellowFeverGramStain() {
        return yellowFeverGramStain;
    }

    public String getYellowFeverLatexAgglutination() {
        return yellowFeverLatexAgglutination;
    }

    public String getYellowFeverCqValueDetection() {
        return yellowFeverCqValueDetection;
    }

    public String getYellowFeverSeQuencing() {
        return yellowFeverSeQuencing;
    }

    public String getYellowFeverDnaMicroArray() {
        return yellowFeverDnaMicroArray;
    }

    public String getYellowFeverOther() {
        return yellowFeverOther;
    }

    public String getRabiesAntiBodyDetection() {
        return rabiesAntiBodyDetection;
    }

    public String getRabiesAntigenDetection() {
        return rabiesAntigenDetection;
    }

    public String getRabiesRapidTest() {
        return rabiesRapidTest;
    }

    public String getRabiesCulture() {
        return rabiesCulture;
    }

    public String getRabiesHistopathology() {
        return rabiesHistopathology;
    }

    public String getRabiesIsolation() {
        return rabiesIsolation;
    }

    public String getRabiesIgmSerumAntibody() {
        return rabiesIgmSerumAntibody;
    }

    public String getRabiesIggSerumAntibody() {
        return rabiesIggSerumAntibody;
    }

    public String getRabiesIgaSerumAntibody() {
        return rabiesIgaSerumAntibody;
    }

    public String getRabiesIncubationTime() {
        return rabiesIncubationTime;
    }

    public String getRabiesIndirectFluorescentAntibody() {
        return rabiesIndirectFluorescentAntibody;
    }

    public String getRabiesMicroscopy() {
        return rabiesMicroscopy;
    }

    public String getRabiesNeutralizingAntibodies() {
        return rabiesNeutralizingAntibodies;
    }

    public String getRabiesPcr() {
        return rabiesPcr;
    }

    public String getRabiesGramStain() {
        return rabiesGramStain;
    }

    public String getRabiesLatexAgglutination() {
        return rabiesLatexAgglutination;
    }

    public String getRabiesCqValueDetection() {
        return rabiesCqValueDetection;
    }

    public String getRabiesSeQuencing() {
        return rabiesSeQuencing;
    }

    public String getRabiesDnaMicroArray() {
        return rabiesDnaMicroArray;
    }

    public String getRabiesOther() {
        return rabiesOther;
    }

    public String getAnthraxAntiBodyDetection() {
        return anthraxAntiBodyDetection;
    }

    public String getAnthraxAntigenDetection() {
        return anthraxAntigenDetection;
    }

    public String getAnthraxRapidTest() {
        return anthraxRapidTest;
    }

    public String getAnthraxCulture() {
        return anthraxCulture;
    }

    public String getAnthraxHistopathology() {
        return anthraxHistopathology;
    }

    public String getAnthraxIsolation() {
        return anthraxIsolation;
    }

    public String getAnthraxIgmSerumAntibody() {
        return anthraxIgmSerumAntibody;
    }

    public String getAnthraxIggSerumAntibody() {
        return anthraxIggSerumAntibody;
    }

    public String getAnthraxIgaSerumAntibody() {
        return anthraxIgaSerumAntibody;
    }

    public String getAnthraxIncubationTime() {
        return anthraxIncubationTime;
    }

    public String getAnthraxIndirectFluorescentAntibody() {
        return anthraxIndirectFluorescentAntibody;
    }

    public String getAnthraxMicroscopy() {
        return anthraxMicroscopy;
    }

    public String getAnthraxNeutralizingAntibodies() {
        return anthraxNeutralizingAntibodies;
    }

    public String getAnthraxPcr() {
        return anthraxPcr;
    }

    public String getAnthraxGramStain() {
        return anthraxGramStain;
    }

    public String getAnthraxLatexAgglutination() {
        return anthraxLatexAgglutination;
    }

    public String getAnthraxCqValueDetection() {
        return anthraxCqValueDetection;
    }

    public String getAnthraxSeQuencing() {
        return anthraxSeQuencing;
    }

    public String getAnthraxDnaMicroArray() {
        return anthraxDnaMicroArray;
    }

    public String getAnthraxOther() {
        return anthraxOther;
    }

    public String getCoronavirusAntiBodyDetection() {
        return coronavirusAntiBodyDetection;
    }

    public String getCoronavirusAntigenDetection() {
        return coronavirusAntigenDetection;
    }

    public String getCoronavirusRapidTest() {
        return coronavirusRapidTest;
    }

    public String getCoronavirusCulture() {
        return coronavirusCulture;
    }

    public String getCoronavirusHistopathology() {
        return coronavirusHistopathology;
    }

    public String getCoronavirusIsolation() {
        return coronavirusIsolation;
    }

    public String getCoronavirusIgmSerumAntibody() {
        return coronavirusIgmSerumAntibody;
    }

    public String getCoronavirusIggSerumAntibody() {
        return coronavirusIggSerumAntibody;
    }

    public String getCoronavirusIgaSerumAntibody() {
        return coronavirusIgaSerumAntibody;
    }

    public String getCoronavirusIncubationTime() {
        return coronavirusIncubationTime;
    }

    public String getCoronavirusIndirectFluorescentAntibody() {
        return coronavirusIndirectFluorescentAntibody;
    }

    public String getCoronavirusMicroscopy() {
        return coronavirusMicroscopy;
    }

    public String getCoronavirusNeutralizingAntibodies() {
        return coronavirusNeutralizingAntibodies;
    }

    public String getCoronavirusPcr() {
        return coronavirusPcr;
    }

    public String getCoronavirusGramStain() {
        return coronavirusGramStain;
    }

    public String getCoronavirusLatexAgglutination() {
        return coronavirusLatexAgglutination;
    }

    public String getCoronavirusCqValueDetection() {
        return coronavirusCqValueDetection;
    }

    public String getCoronavirusSeQuencing() {
        return coronavirusSeQuencing;
    }

    public String getCoronavirusDnaMicroArray() {
        return coronavirusDnaMicroArray;
    }

    public String getCoronavirusOther() {
        return coronavirusOther;
    }

    public String getPneumoniaAntiBodyDetection() {
        return pneumoniaAntiBodyDetection;
    }

    public String getPneumoniaAntigenDetection() {
        return pneumoniaAntigenDetection;
    }

    public String getPneumoniaRapidTest() {
        return pneumoniaRapidTest;
    }

    public String getPneumoniaCulture() {
        return pneumoniaCulture;
    }

    public String getPneumoniaHistopathology() {
        return pneumoniaHistopathology;
    }

    public String getPneumoniaIsolation() {
        return pneumoniaIsolation;
    }

    public String getPneumoniaIgmSerumAntibody() {
        return pneumoniaIgmSerumAntibody;
    }

    public String getPneumoniaIggSerumAntibody() {
        return pneumoniaIggSerumAntibody;
    }

    public String getPneumoniaIgaSerumAntibody() {
        return pneumoniaIgaSerumAntibody;
    }

    public String getPneumoniaIncubationTime() {
        return pneumoniaIncubationTime;
    }

    public String getPneumoniaIndirectFluorescentAntibody() {
        return pneumoniaIndirectFluorescentAntibody;
    }

    public String getPneumoniaMicroscopy() {
        return pneumoniaMicroscopy;
    }

    public String getPneumoniaNeutralizingAntibodies() {
        return pneumoniaNeutralizingAntibodies;
    }

    public String getPneumoniaPcr() {
        return pneumoniaPcr;
    }

    public String getPneumoniaGramStain() {
        return pneumoniaGramStain;
    }

    public String getPneumoniaLatexAgglutination() {
        return pneumoniaLatexAgglutination;
    }

    public String getPneumoniaCqValueDetection() {
        return pneumoniaCqValueDetection;
    }

    public String getPneumoniaSeQuencing() {
        return pneumoniaSeQuencing;
    }

    public String getPneumoniaDnaMicroArray() {
        return pneumoniaDnaMicroArray;
    }

    public String getPneumoniaOther() {
        return pneumoniaOther;
    }

    public String getMalariaAntiBodyDetection() {
        return malariaAntiBodyDetection;
    }

    public String getMalariaAntigenDetection() {
        return malariaAntigenDetection;
    }

    public String getMalariaRapidTest() {
        return malariaRapidTest;
    }

    public String getMalariaCulture() {
        return malariaCulture;
    }

    public String getMalariaHistopathology() {
        return malariaHistopathology;
    }

    public String getMalariaIsolation() {
        return malariaIsolation;
    }

    public String getMalariaIgmSerumAntibody() {
        return malariaIgmSerumAntibody;
    }

    public String getMalariaIggSerumAntibody() {
        return malariaIggSerumAntibody;
    }

    public String getMalariaIgaSerumAntibody() {
        return malariaIgaSerumAntibody;
    }

    public String getMalariaIncubationTime() {
        return malariaIncubationTime;
    }

    public String getMalariaIndirectFluorescentAntibody() {
        return malariaIndirectFluorescentAntibody;
    }

    public String getMalariaMicroscopy() {
        return malariaMicroscopy;
    }

    public String getMalariaNeutralizingAntibodies() {
        return malariaNeutralizingAntibodies;
    }

    public String getMalariaPcr() {
        return malariaPcr;
    }

    public String getMalariaGramStain() {
        return malariaGramStain;
    }

    public String getMalariaLatexAgglutination() {
        return malariaLatexAgglutination;
    }

    public String getMalariaCqValueDetection() {
        return malariaCqValueDetection;
    }

    public String getMalariaSeQuencing() {
        return malariaSeQuencing;
    }

    public String getMalariaDnaMicroArray() {
        return malariaDnaMicroArray;
    }

    public String getMalariaOther() {
        return malariaOther;
    }

    public String getTyphoidFeverAntiBodyDetection() {
        return typhoidFeverAntiBodyDetection;
    }

    public String getTyphoidFeverAntigenDetection() {
        return typhoidFeverAntigenDetection;
    }

    public String getTyphoidFeverRapidTest() {
        return typhoidFeverRapidTest;
    }

    public String getTyphoidFeverCulture() {
        return typhoidFeverCulture;
    }

    public String getTyphoidFeverHistopathology() {
        return typhoidFeverHistopathology;
    }

    public String getTyphoidFeverIsolation() {
        return typhoidFeverIsolation;
    }

    public String getTyphoidFeverIgmSerumAntibody() {
        return typhoidFeverIgmSerumAntibody;
    }

    public String getTyphoidFeverIggSerumAntibody() {
        return typhoidFeverIggSerumAntibody;
    }

    public String getTyphoidFeverIgaSerumAntibody() {
        return typhoidFeverIgaSerumAntibody;
    }

    public String getTyphoidFeverIncubationTime() {
        return typhoidFeverIncubationTime;
    }

    public String getTyphoidFeverIndirectFluorescentAntibody() {
        return typhoidFeverIndirectFluorescentAntibody;
    }

    public String getTyphoidFeverMicroscopy() {
        return typhoidFeverMicroscopy;
    }

    public String getTyphoidFeverNeutralizingAntibodies() {
        return typhoidFeverNeutralizingAntibodies;
    }

    public String getTyphoidFeverPcr() {
        return typhoidFeverPcr;
    }

    public String getTyphoidFeverGramStain() {
        return typhoidFeverGramStain;
    }

    public String getTyphoidFeverLatexAgglutination() {
        return typhoidFeverLatexAgglutination;
    }

    public String getTyphoidFeverCqValueDetection() {
        return typhoidFeverCqValueDetection;
    }

    public String getTyphoidFeverSeQuencing() {
        return typhoidFeverSeQuencing;
    }

    public String getTyphoidFeverDnaMicroArray() {
        return typhoidFeverDnaMicroArray;
    }

    public String getTyphoidFeverOther() {
        return typhoidFeverOther;
    }

    public String getAcuteViralHepatitisAntiBodyDetection() {
        return acuteViralHepatitisAntiBodyDetection;
    }

    public String getAcuteViralHepatitisAntigenDetection() {
        return acuteViralHepatitisAntigenDetection;
    }

    public String getAcuteViralHepatitisRapidTest() {
        return acuteViralHepatitisRapidTest;
    }

    public String getAcuteViralHepatitisCulture() {
        return acuteViralHepatitisCulture;
    }

    public String getAcuteViralHepatitisHistopathology() {
        return acuteViralHepatitisHistopathology;
    }

    public String getAcuteViralHepatitisIsolation() {
        return acuteViralHepatitisIsolation;
    }

    public String getAcuteViralHepatitisIgmSerumAntibody() {
        return acuteViralHepatitisIgmSerumAntibody;
    }

    public String getAcuteViralHepatitisIggSerumAntibody() {
        return acuteViralHepatitisIggSerumAntibody;
    }

    public String getAcuteViralHepatitisIgaSerumAntibody() {
        return acuteViralHepatitisIgaSerumAntibody;
    }

    public String getAcuteViralHepatitisIncubationTime() {
        return acuteViralHepatitisIncubationTime;
    }

    public String getAcuteViralHepatitisIndirectFluorescentAntibody() {
        return acuteViralHepatitisIndirectFluorescentAntibody;
    }

    public String getAcuteViralHepatitisMicroscopy() {
        return acuteViralHepatitisMicroscopy;
    }

    public String getAcuteViralHepatitisNeutralizingAntibodies() {
        return acuteViralHepatitisNeutralizingAntibodies;
    }

    public String getAcuteViralHepatitisPcr() {
        return acuteViralHepatitisPcr;
    }

    public String getAcuteViralHepatitisGramStain() {
        return acuteViralHepatitisGramStain;
    }

    public String getAcuteViralHepatitisLatexAgglutination() {
        return acuteViralHepatitisLatexAgglutination;
    }

    public String getAcuteViralHepatitisCqValueDetection() {
        return acuteViralHepatitisCqValueDetection;
    }

    public String getAcuteViralHepatitisSeQuencing() {
        return acuteViralHepatitisSeQuencing;
    }

    public String getAcuteViralHepatitisDnaMicroArray() {
        return acuteViralHepatitisDnaMicroArray;
    }

    public String getAcuteViralHepatitisOther() {
        return acuteViralHepatitisOther;
    }

    public String getNonNeonatalTetanusAntiBodyDetection() {
        return nonNeonatalTetanusAntiBodyDetection;
    }

    public String getNonNeonatalTetanusAntigenDetection() {
        return nonNeonatalTetanusAntigenDetection;
    }

    public String getNonNeonatalTetanusRapidTest() {
        return nonNeonatalTetanusRapidTest;
    }

    public String getNonNeonatalTetanusCulture() {
        return nonNeonatalTetanusCulture;
    }

    public String getNonNeonatalTetanusHistopathology() {
        return nonNeonatalTetanusHistopathology;
    }

    public String getNonNeonatalTetanusIsolation() {
        return nonNeonatalTetanusIsolation;
    }

    public String getNonNeonatalTetanusIgmSerumAntibody() {
        return nonNeonatalTetanusIgmSerumAntibody;
    }

    public String getNonNeonatalTetanusIggSerumAntibody() {
        return nonNeonatalTetanusIggSerumAntibody;
    }

    public String getNonNeonatalTetanusIgaSerumAntibody() {
        return nonNeonatalTetanusIgaSerumAntibody;
    }

    public String getNonNeonatalTetanusIncubationTime() {
        return nonNeonatalTetanusIncubationTime;
    }

    public String getNonNeonatalTetanusIndirectFluorescentAntibody() {
        return nonNeonatalTetanusIndirectFluorescentAntibody;
    }

    public String getNonNeonatalTetanusMicroscopy() {
        return nonNeonatalTetanusMicroscopy;
    }

    public String getNonNeonatalTetanusNeutralizingAntibodies() {
        return nonNeonatalTetanusNeutralizingAntibodies;
    }

    public String getNonNeonatalTetanusPcr() {
        return nonNeonatalTetanusPcr;
    }

    public String getNonNeonatalTetanusGramStain() {
        return nonNeonatalTetanusGramStain;
    }

    public String getNonNeonatalTetanusLatexAgglutination() {
        return nonNeonatalTetanusLatexAgglutination;
    }

    public String getNonNeonatalTetanusCqValueDetection() {
        return nonNeonatalTetanusCqValueDetection;
    }

    public String getNonNeonatalTetanusSeQuencing() {
        return nonNeonatalTetanusSeQuencing;
    }

    public String getNonNeonatalTetanusDnaMicroArray() {
        return nonNeonatalTetanusDnaMicroArray;
    }

    public String getNonNeonatalTetanusOther() {
        return nonNeonatalTetanusOther;
    }

    public String getHivAntiBodyDetection() {
        return hivAntiBodyDetection;
    }

    public String getHivAntigenDetection() {
        return hivAntigenDetection;
    }

    public String getHivRapidTest() {
        return hivRapidTest;
    }

    public String getHivCulture() {
        return hivCulture;
    }

    public String getHivHistopathology() {
        return hivHistopathology;
    }

    public String getHivIsolation() {
        return hivIsolation;
    }

    public String getHivIgmSerumAntibody() {
        return hivIgmSerumAntibody;
    }

    public String getHivIggSerumAntibody() {
        return hivIggSerumAntibody;
    }

    public String getHivIgaSerumAntibody() {
        return hivIgaSerumAntibody;
    }

    public String getHivIncubationTime() {
        return hivIncubationTime;
    }

    public String getHivIndirectFluorescentAntibody() {
        return hivIndirectFluorescentAntibody;
    }

    public String getHivMicroscopy() {
        return hivMicroscopy;
    }

    public String getHivNeutralizingAntibodies() {
        return hivNeutralizingAntibodies;
    }

    public String getHivPcr() {
        return hivPcr;
    }

    public String getHivGramStain() {
        return hivGramStain;
    }

    public String getHivLatexAgglutination() {
        return hivLatexAgglutination;
    }

    public String getHivCqValueDetection() {
        return hivCqValueDetection;
    }

    public String getHivSeQuencing() {
        return hivSeQuencing;
    }

    public String getHivDnaMicroArray() {
        return hivDnaMicroArray;
    }

    public String getHivOther() {
        return hivOther;
    }

    public String getSchistosomiasisAntiBodyDetection() {
        return schistosomiasisAntiBodyDetection;
    }

    public String getSchistosomiasisAntigenDetection() {
        return schistosomiasisAntigenDetection;
    }

    public String getSchistosomiasisRapidTest() {
        return schistosomiasisRapidTest;
    }

    public String getSchistosomiasisCulture() {
        return schistosomiasisCulture;
    }

    public String getSchistosomiasisHistopathology() {
        return schistosomiasisHistopathology;
    }

    public String getSchistosomiasisIsolation() {
        return schistosomiasisIsolation;
    }

    public String getSchistosomiasisIgmSerumAntibody() {
        return schistosomiasisIgmSerumAntibody;
    }

    public String getSchistosomiasisIggSerumAntibody() {
        return schistosomiasisIggSerumAntibody;
    }

    public String getSchistosomiasisIgaSerumAntibody() {
        return schistosomiasisIgaSerumAntibody;
    }

    public String getSchistosomiasisIncubationTime() {
        return schistosomiasisIncubationTime;
    }

    public String getSchistosomiasisIndirectFluorescentAntibody() {
        return schistosomiasisIndirectFluorescentAntibody;
    }

    public String getSchistosomiasisMicroscopy() {
        return schistosomiasisMicroscopy;
    }

    public String getSchistosomiasisNeutralizingAntibodies() {
        return schistosomiasisNeutralizingAntibodies;
    }

    public String getSchistosomiasisPcr() {
        return schistosomiasisPcr;
    }

    public String getSchistosomiasisGramStain() {
        return schistosomiasisGramStain;
    }

    public String getSchistosomiasisLatexAgglutination() {
        return schistosomiasisLatexAgglutination;
    }

    public String getSchistosomiasisCqValueDetection() {
        return schistosomiasisCqValueDetection;
    }

    public String getSchistosomiasisSeQuencing() {
        return schistosomiasisSeQuencing;
    }

    public String getSchistosomiasisDnaMicroArray() {
        return schistosomiasisDnaMicroArray;
    }

    public String getSchistosomiasisOther() {
        return schistosomiasisOther;
    }

    public String getSoilTransmittedHelminthsAntiBodyDetection() {
        return soilTransmittedHelminthsAntiBodyDetection;
    }

    public String getSoilTransmittedHelminthsAntigenDetection() {
        return soilTransmittedHelminthsAntigenDetection;
    }

    public String getSoilTransmittedHelminthsRapidTest() {
        return soilTransmittedHelminthsRapidTest;
    }

    public String getSoilTransmittedHelminthsCulture() {
        return soilTransmittedHelminthsCulture;
    }

    public String getSoilTransmittedHelminthsHistopathology() {
        return soilTransmittedHelminthsHistopathology;
    }

    public String getSoilTransmittedHelminthsIsolation() {
        return soilTransmittedHelminthsIsolation;
    }

    public String getSoilTransmittedHelminthsIgmSerumAntibody() {
        return soilTransmittedHelminthsIgmSerumAntibody;
    }

    public String getSoilTransmittedHelminthsIggSerumAntibody() {
        return soilTransmittedHelminthsIggSerumAntibody;
    }

    public String getSoilTransmittedHelminthsIgaSerumAntibody() {
        return soilTransmittedHelminthsIgaSerumAntibody;
    }

    public String getSoilTransmittedHelminthsIncubationTime() {
        return soilTransmittedHelminthsIncubationTime;
    }

    public String getSoilTransmittedHelminthsIndirectFluorescentAntibody() {
        return soilTransmittedHelminthsIndirectFluorescentAntibody;
    }

    public String getSoilTransmittedHelminthsMicroscopy() {
        return soilTransmittedHelminthsMicroscopy;
    }

    public String getSoilTransmittedHelminthsNeutralizingAntibodies() {
        return soilTransmittedHelminthsNeutralizingAntibodies;
    }

    public String getSoilTransmittedHelminthsPcr() {
        return soilTransmittedHelminthsPcr;
    }

    public String getSoilTransmittedHelminthsGramStain() {
        return soilTransmittedHelminthsGramStain;
    }

    public String getSoilTransmittedHelminthsLatexAgglutination() {
        return soilTransmittedHelminthsLatexAgglutination;
    }

    public String getSoilTransmittedHelminthsCqValueDetection() {
        return soilTransmittedHelminthsCqValueDetection;
    }

    public String getSoilTransmittedHelminthsSeQuencing() {
        return soilTransmittedHelminthsSeQuencing;
    }

    public String getSoilTransmittedHelminthsDnaMicroArray() {
        return soilTransmittedHelminthsDnaMicroArray;
    }

    public String getSoilTransmittedHelminthsOther() {
        return soilTransmittedHelminthsOther;
    }

    public String getTrypanosomiasisAntiBodyDetection() {
        return trypanosomiasisAntiBodyDetection;
    }

    public String getTrypanosomiasisAntigenDetection() {
        return trypanosomiasisAntigenDetection;
    }

    public String getTrypanosomiasisRapidTest() {
        return trypanosomiasisRapidTest;
    }

    public String getTrypanosomiasisCulture() {
        return trypanosomiasisCulture;
    }

    public String getTrypanosomiasisHistopathology() {
        return trypanosomiasisHistopathology;
    }

    public String getTrypanosomiasisIsolation() {
        return trypanosomiasisIsolation;
    }

    public String getTrypanosomiasisIgmSerumAntibody() {
        return trypanosomiasisIgmSerumAntibody;
    }

    public String getTrypanosomiasisIggSerumAntibody() {
        return trypanosomiasisIggSerumAntibody;
    }

    public String getTrypanosomiasisIgaSerumAntibody() {
        return trypanosomiasisIgaSerumAntibody;
    }

    public String getTrypanosomiasisIncubationTime() {
        return trypanosomiasisIncubationTime;
    }

    public String getTrypanosomiasisIndirectFluorescentAntibody() {
        return trypanosomiasisIndirectFluorescentAntibody;
    }

    public String getTrypanosomiasisMicroscopy() {
        return trypanosomiasisMicroscopy;
    }

    public String getTrypanosomiasisNeutralizingAntibodies() {
        return trypanosomiasisNeutralizingAntibodies;
    }

    public String getTrypanosomiasisPcr() {
        return trypanosomiasisPcr;
    }

    public String getTrypanosomiasisGramStain() {
        return trypanosomiasisGramStain;
    }

    public String getTrypanosomiasisLatexAgglutination() {
        return trypanosomiasisLatexAgglutination;
    }

    public String getTrypanosomiasisCqValueDetection() {
        return trypanosomiasisCqValueDetection;
    }

    public String getTrypanosomiasisSeQuencing() {
        return trypanosomiasisSeQuencing;
    }

    public String getTrypanosomiasisDnaMicroArray() {
        return trypanosomiasisDnaMicroArray;
    }

    public String getTrypanosomiasisOther() {
        return trypanosomiasisOther;
    }

    public String getDiarrheaDehydrationAntiBodyDetection() {
        return diarrheaDehydrationAntiBodyDetection;
    }

    public String getDiarrheaDehydrationAntigenDetection() {
        return diarrheaDehydrationAntigenDetection;
    }

    public String getDiarrheaDehydrationRapidTest() {
        return diarrheaDehydrationRapidTest;
    }

    public String getDiarrheaDehydrationCulture() {
        return diarrheaDehydrationCulture;
    }

    public String getDiarrheaDehydrationHistopathology() {
        return diarrheaDehydrationHistopathology;
    }

    public String getDiarrheaDehydrationIsolation() {
        return diarrheaDehydrationIsolation;
    }

    public String getDiarrheaDehydrationIgmSerumAntibody() {
        return diarrheaDehydrationIgmSerumAntibody;
    }

    public String getDiarrheaDehydrationIggSerumAntibody() {
        return diarrheaDehydrationIggSerumAntibody;
    }

    public String getDiarrheaDehydrationIgaSerumAntibody() {
        return diarrheaDehydrationIgaSerumAntibody;
    }

    public String getDiarrheaDehydrationIncubationTime() {
        return diarrheaDehydrationIncubationTime;
    }

    public String getDiarrheaDehydrationIndirectFluorescentAntibody() {
        return diarrheaDehydrationIndirectFluorescentAntibody;
    }

    public String getDiarrheaDehydrationMicroscopy() {
        return diarrheaDehydrationMicroscopy;
    }

    public String getDiarrheaDehydrationNeutralizingAntibodies() {
        return diarrheaDehydrationNeutralizingAntibodies;
    }

    public String getDiarrheaDehydrationPcr() {
        return diarrheaDehydrationPcr;
    }

    public String getDiarrheaDehydrationGramStain() {
        return diarrheaDehydrationGramStain;
    }

    public String getDiarrheaDehydrationLatexAgglutination() {
        return diarrheaDehydrationLatexAgglutination;
    }

    public String getDiarrheaDehydrationCqValueDetection() {
        return diarrheaDehydrationCqValueDetection;
    }

    public String getDiarrheaDehydrationSeQuencing() {
        return diarrheaDehydrationSeQuencing;
    }

    public String getDiarrheaDehydrationDnaMicroArray() {
        return diarrheaDehydrationDnaMicroArray;
    }

    public String getDiarrheaDehydrationOther() {
        return diarrheaDehydrationOther;
    }

    public String getDiarrheaBloodAntiBodyDetection() {
        return diarrheaBloodAntiBodyDetection;
    }

    public String getDiarrheaBloodAntigenDetection() {
        return diarrheaBloodAntigenDetection;
    }

    public String getDiarrheaBloodRapidTest() {
        return diarrheaBloodRapidTest;
    }

    public String getDiarrheaBloodCulture() {
        return diarrheaBloodCulture;
    }

    public String getDiarrheaBloodHistopathology() {
        return diarrheaBloodHistopathology;
    }

    public String getDiarrheaBloodIsolation() {
        return diarrheaBloodIsolation;
    }

    public String getDiarrheaBloodIgmSerumAntibody() {
        return diarrheaBloodIgmSerumAntibody;
    }

    public String getDiarrheaBloodIggSerumAntibody() {
        return diarrheaBloodIggSerumAntibody;
    }

    public String getDiarrheaBloodIgaSerumAntibody() {
        return diarrheaBloodIgaSerumAntibody;
    }

    public String getDiarrheaBloodIncubationTime() {
        return diarrheaBloodIncubationTime;
    }

    public String getDiarrheaBloodIndirectFluorescentAntibody() {
        return diarrheaBloodIndirectFluorescentAntibody;
    }

    public String getDiarrheaBloodMicroscopy() {
        return diarrheaBloodMicroscopy;
    }

    public String getDiarrheaBloodNeutralizingAntibodies() {
        return diarrheaBloodNeutralizingAntibodies;
    }

    public String getDiarrheaBloodPcr() {
        return diarrheaBloodPcr;
    }

    public String getDiarrheaBloodGramStain() {
        return diarrheaBloodGramStain;
    }

    public String getDiarrheaBloodLatexAgglutination() {
        return diarrheaBloodLatexAgglutination;
    }

    public String getDiarrheaBloodCqValueDetection() {
        return diarrheaBloodCqValueDetection;
    }

    public String getDiarrheaBloodSeQuencing() {
        return diarrheaBloodSeQuencing;
    }

    public String getDiarrheaBloodDnaMicroArray() {
        return diarrheaBloodDnaMicroArray;
    }

    public String getDiarrheaBloodOther() {
        return diarrheaBloodOther;
    }

    public String getSnakeBiteAntiBodyDetection() {
        return snakeBiteAntiBodyDetection;
    }

    public String getSnakeBiteAntigenDetection() {
        return snakeBiteAntigenDetection;
    }

    public String getSnakeBiteRapidTest() {
        return snakeBiteRapidTest;
    }

    public String getSnakeBiteCulture() {
        return snakeBiteCulture;
    }

    public String getSnakeBiteHistopathology() {
        return snakeBiteHistopathology;
    }

    public String getSnakeBiteIsolation() {
        return snakeBiteIsolation;
    }

    public String getSnakeBiteIgmSerumAntibody() {
        return snakeBiteIgmSerumAntibody;
    }

    public String getSnakeBiteIggSerumAntibody() {
        return snakeBiteIggSerumAntibody;
    }

    public String getSnakeBiteIgaSerumAntibody() {
        return snakeBiteIgaSerumAntibody;
    }

    public String getSnakeBiteIncubationTime() {
        return snakeBiteIncubationTime;
    }

    public String getSnakeBiteIndirectFluorescentAntibody() {
        return snakeBiteIndirectFluorescentAntibody;
    }

    public String getSnakeBiteMicroscopy() {
        return snakeBiteMicroscopy;
    }

    public String getSnakeBiteNeutralizingAntibodies() {
        return snakeBiteNeutralizingAntibodies;
    }

    public String getSnakeBitePcr() {
        return snakeBitePcr;
    }

    public String getSnakeBiteGramStain() {
        return snakeBiteGramStain;
    }

    public String getSnakeBiteLatexAgglutination() {
        return snakeBiteLatexAgglutination;
    }

    public String getSnakeBiteCqValueDetection() {
        return snakeBiteCqValueDetection;
    }

    public String getSnakeBiteSeQuencing() {
        return snakeBiteSeQuencing;
    }

    public String getSnakeBiteDnaMicroArray() {
        return snakeBiteDnaMicroArray;
    }

    public String getSnakeBiteOther() {
        return snakeBiteOther;
    }

    public String getRubellaAntiBodyDetection() {
        return rubellaAntiBodyDetection;
    }

    public String getRubellaAntigenDetection() {
        return rubellaAntigenDetection;
    }

    public String getRubellaRapidTest() {
        return rubellaRapidTest;
    }

    public String getRubellaCulture() {
        return rubellaCulture;
    }

    public String getRubellaHistopathology() {
        return rubellaHistopathology;
    }

    public String getRubellaIsolation() {
        return rubellaIsolation;
    }

    public String getRubellaIgmSerumAntibody() {
        return rubellaIgmSerumAntibody;
    }

    public String getRubellaIggSerumAntibody() {
        return rubellaIggSerumAntibody;
    }

    public String getRubellaIgaSerumAntibody() {
        return rubellaIgaSerumAntibody;
    }

    public String getRubellaIncubationTime() {
        return rubellaIncubationTime;
    }

    public String getRubellaIndirectFluorescentAntibody() {
        return rubellaIndirectFluorescentAntibody;
    }

    public String getRubellaMicroscopy() {
        return rubellaMicroscopy;
    }

    public String getRubellaNeutralizingAntibodies() {
        return rubellaNeutralizingAntibodies;
    }

    public String getRubellaPcr() {
        return rubellaPcr;
    }

    public String getRubellaGramStain() {
        return rubellaGramStain;
    }

    public String getRubellaLatexAgglutination() {
        return rubellaLatexAgglutination;
    }

    public String getRubellaCqValueDetection() {
        return rubellaCqValueDetection;
    }

    public String getRubellaSeQuencing() {
        return rubellaSeQuencing;
    }

    public String getRubellaDnaMicroArray() {
        return rubellaDnaMicroArray;
    }

    public String getRubellaOther() {
        return rubellaOther;
    }

    public String getTuberculosisAntiBodyDetection() {
        return tuberculosisAntiBodyDetection;
    }

    public String getTuberculosisAntigenDetection() {
        return tuberculosisAntigenDetection;
    }

    public String getTuberculosisRapidTest() {
        return tuberculosisRapidTest;
    }

    public String getTuberculosisCulture() {
        return tuberculosisCulture;
    }

    public String getTuberculosisHistopathology() {
        return tuberculosisHistopathology;
    }

    public String getTuberculosisIsolation() {
        return tuberculosisIsolation;
    }

    public String getTuberculosisIgmSerumAntibody() {
        return tuberculosisIgmSerumAntibody;
    }

    public String getTuberculosisIggSerumAntibody() {
        return tuberculosisIggSerumAntibody;
    }

    public String getTuberculosisIgaSerumAntibody() {
        return tuberculosisIgaSerumAntibody;
    }

    public String getTuberculosisIncubationTime() {
        return tuberculosisIncubationTime;
    }

    public String getTuberculosisIndirectFluorescentAntibody() {
        return tuberculosisIndirectFluorescentAntibody;
    }

    public String getTuberculosisMicroscopy() {
        return tuberculosisMicroscopy;
    }

    public String getTuberculosisNeutralizingAntibodies() {
        return tuberculosisNeutralizingAntibodies;
    }

    public String getTuberculosisPcr() {
        return tuberculosisPcr;
    }

    public String getTuberculosisGramStain() {
        return tuberculosisGramStain;
    }

    public String getTuberculosisLatexAgglutination() {
        return tuberculosisLatexAgglutination;
    }

    public String getTuberculosisCqValueDetection() {
        return tuberculosisCqValueDetection;
    }

    public String getTuberculosisSeQuencing() {
        return tuberculosisSeQuencing;
    }

    public String getTuberculosisDnaMicroArray() {
        return tuberculosisDnaMicroArray;
    }

    public String getTuberculosisOther() {
        return tuberculosisOther;
    }

    public String getLeprosyAntiBodyDetection() {
        return leprosyAntiBodyDetection;
    }

    public String getLeprosyAntigenDetection() {
        return leprosyAntigenDetection;
    }

    public String getLeprosyRapidTest() {
        return leprosyRapidTest;
    }

    public String getLeprosyCulture() {
        return leprosyCulture;
    }

    public String getLeprosyHistopathology() {
        return leprosyHistopathology;
    }

    public String getLeprosyIsolation() {
        return leprosyIsolation;
    }

    public String getLeprosyIgmSerumAntibody() {
        return leprosyIgmSerumAntibody;
    }

    public String getLeprosyIggSerumAntibody() {
        return leprosyIggSerumAntibody;
    }

    public String getLeprosyIgaSerumAntibody() {
        return leprosyIgaSerumAntibody;
    }

    public String getLeprosyIncubationTime() {
        return leprosyIncubationTime;
    }

    public String getLeprosyIndirectFluorescentAntibody() {
        return leprosyIndirectFluorescentAntibody;
    }

    public String getLeprosyMicroscopy() {
        return leprosyMicroscopy;
    }

    public String getLeprosyNeutralizingAntibodies() {
        return leprosyNeutralizingAntibodies;
    }

    public String getLeprosyPcr() {
        return leprosyPcr;
    }

    public String getLeprosyGramStain() {
        return leprosyGramStain;
    }

    public String getLeprosyLatexAgglutination() {
        return leprosyLatexAgglutination;
    }

    public String getLeprosyCqValueDetection() {
        return leprosyCqValueDetection;
    }

    public String getLeprosySeQuencing() {
        return leprosySeQuencing;
    }

    public String getLeprosyDnaMicroArray() {
        return leprosyDnaMicroArray;
    }

    public String getLeprosyOther() {
        return leprosyOther;
    }

    public String getLymphaticFilariasisAntiBodyDetection() {
        return lymphaticFilariasisAntiBodyDetection;
    }

    public String getLymphaticFilariasisAntigenDetection() {
        return lymphaticFilariasisAntigenDetection;
    }

    public String getLymphaticFilariasisRapidTest() {
        return lymphaticFilariasisRapidTest;
    }

    public String getLymphaticFilariasisCulture() {
        return lymphaticFilariasisCulture;
    }

    public String getLymphaticFilariasisHistopathology() {
        return lymphaticFilariasisHistopathology;
    }

    public String getLymphaticFilariasisIsolation() {
        return lymphaticFilariasisIsolation;
    }

    public String getLymphaticFilariasisIgmSerumAntibody() {
        return lymphaticFilariasisIgmSerumAntibody;
    }

    public String getLymphaticFilariasisIggSerumAntibody() {
        return lymphaticFilariasisIggSerumAntibody;
    }

    public String getLymphaticFilariasisIgaSerumAntibody() {
        return lymphaticFilariasisIgaSerumAntibody;
    }

    public String getLymphaticFilariasisIncubationTime() {
        return lymphaticFilariasisIncubationTime;
    }

    public String getLymphaticFilariasisIndirectFluorescentAntibody() {
        return lymphaticFilariasisIndirectFluorescentAntibody;
    }

    public String getLymphaticFilariasisMicroscopy() {
        return lymphaticFilariasisMicroscopy;
    }

    public String getLymphaticFilariasisNeutralizingAntibodies() {
        return lymphaticFilariasisNeutralizingAntibodies;
    }

    public String getLymphaticFilariasisPcr() {
        return lymphaticFilariasisPcr;
    }

    public String getLymphaticFilariasisGramStain() {
        return lymphaticFilariasisGramStain;
    }

    public String getLymphaticFilariasisLatexAgglutination() {
        return lymphaticFilariasisLatexAgglutination;
    }

    public String getLymphaticFilariasisCqValueDetection() {
        return lymphaticFilariasisCqValueDetection;
    }

    public String getLymphaticFilariasisSeQuencing() {
        return lymphaticFilariasisSeQuencing;
    }

    public String getLymphaticFilariasisDnaMicroArray() {
        return lymphaticFilariasisDnaMicroArray;
    }

    public String getLymphaticFilariasisOther() {
        return lymphaticFilariasisOther;
    }

    public String getBuruliUlcerAntiBodyDetection() {
        return buruliUlcerAntiBodyDetection;
    }

    public String getBuruliUlcerAntigenDetection() {
        return buruliUlcerAntigenDetection;
    }

    public String getBuruliUlcerRapidTest() {
        return buruliUlcerRapidTest;
    }

    public String getBuruliUlcerCulture() {
        return buruliUlcerCulture;
    }

    public String getBuruliUlcerHistopathology() {
        return buruliUlcerHistopathology;
    }

    public String getBuruliUlcerIsolation() {
        return buruliUlcerIsolation;
    }

    public String getBuruliUlcerIgmSerumAntibody() {
        return buruliUlcerIgmSerumAntibody;
    }

    public String getBuruliUlcerIggSerumAntibody() {
        return buruliUlcerIggSerumAntibody;
    }

    public String getBuruliUlcerIgaSerumAntibody() {
        return buruliUlcerIgaSerumAntibody;
    }

    public String getBuruliUlcerIncubationTime() {
        return buruliUlcerIncubationTime;
    }

    public String getBuruliUlcerIndirectFluorescentAntibody() {
        return buruliUlcerIndirectFluorescentAntibody;
    }

    public String getBuruliUlcerMicroscopy() {
        return buruliUlcerMicroscopy;
    }

    public String getBuruliUlcerNeutralizingAntibodies() {
        return buruliUlcerNeutralizingAntibodies;
    }

    public String getBuruliUlcerPcr() {
        return buruliUlcerPcr;
    }

    public String getBuruliUlcerGramStain() {
        return buruliUlcerGramStain;
    }

    public String getBuruliUlcerLatexAgglutination() {
        return buruliUlcerLatexAgglutination;
    }

    public String getBuruliUlcerCqValueDetection() {
        return buruliUlcerCqValueDetection;
    }

    public String getBuruliUlcerSeQuencing() {
        return buruliUlcerSeQuencing;
    }

    public String getBuruliUlcerDnaMicroArray() {
        return buruliUlcerDnaMicroArray;
    }

    public String getBuruliUlcerOther() {
        return buruliUlcerOther;
    }

    public String getPertussisAntiBodyDetection() {
        return pertussisAntiBodyDetection;
    }

    public String getPertussisAntigenDetection() {
        return pertussisAntigenDetection;
    }

    public String getPertussisRapidTest() {
        return pertussisRapidTest;
    }

    public String getPertussisCulture() {
        return pertussisCulture;
    }

    public String getPertussisHistopathology() {
        return pertussisHistopathology;
    }

    public String getPertussisIsolation() {
        return pertussisIsolation;
    }

    public String getPertussisIgmSerumAntibody() {
        return pertussisIgmSerumAntibody;
    }

    public String getPertussisIggSerumAntibody() {
        return pertussisIggSerumAntibody;
    }

    public String getPertussisIgaSerumAntibody() {
        return pertussisIgaSerumAntibody;
    }

    public String getPertussisIncubationTime() {
        return pertussisIncubationTime;
    }

    public String getPertussisIndirectFluorescentAntibody() {
        return pertussisIndirectFluorescentAntibody;
    }

    public String getPertussisMicroscopy() {
        return pertussisMicroscopy;
    }

    public String getPertussisNeutralizingAntibodies() {
        return pertussisNeutralizingAntibodies;
    }

    public String getPertussisPcr() {
        return pertussisPcr;
    }

    public String getPertussisGramStain() {
        return pertussisGramStain;
    }

    public String getPertussisLatexAgglutination() {
        return pertussisLatexAgglutination;
    }

    public String getPertussisCqValueDetection() {
        return pertussisCqValueDetection;
    }

    public String getPertussisSeQuencing() {
        return pertussisSeQuencing;
    }

    public String getPertussisDnaMicroArray() {
        return pertussisDnaMicroArray;
    }

    public String getPertussisOther() {
        return pertussisOther;
    }

    public String getNeonatalAntiBodyDetection() {
        return neonatalAntiBodyDetection;
    }

    public String getNeonatalAntigenDetection() {
        return neonatalAntigenDetection;
    }

    public String getNeonatalRapidTest() {
        return neonatalRapidTest;
    }

    public String getNeonatalCulture() {
        return neonatalCulture;
    }

    public String getNeonatalHistopathology() {
        return neonatalHistopathology;
    }

    public String getNeonatalIsolation() {
        return neonatalIsolation;
    }

    public String getNeonatalIgmSerumAntibody() {
        return neonatalIgmSerumAntibody;
    }

    public String getNeonatalIggSerumAntibody() {
        return neonatalIggSerumAntibody;
    }

    public String getNeonatalIgaSerumAntibody() {
        return neonatalIgaSerumAntibody;
    }

    public String getNeonatalIncubationTime() {
        return neonatalIncubationTime;
    }

    public String getNeonatalIndirectFluorescentAntibody() {
        return neonatalIndirectFluorescentAntibody;
    }

    public String getNeonatalMicroscopy() {
        return neonatalMicroscopy;
    }

    public String getNeonatalNeutralizingAntibodies() {
        return neonatalNeutralizingAntibodies;
    }

    public String getNeonatalPcr() {
        return neonatalPcr;
    }

    public String getNeonatalGramStain() {
        return neonatalGramStain;
    }

    public String getNeonatalLatexAgglutination() {
        return neonatalLatexAgglutination;
    }

    public String getNeonatalCqValueDetection() {
        return neonatalCqValueDetection;
    }

    public String getNeonatalSeQuencing() {
        return neonatalSeQuencing;
    }

    public String getNeonatalDnaMicroArray() {
        return neonatalDnaMicroArray;
    }

    public String getNeonatalOther() {
        return neonatalOther;
    }

    public String getOnchocerciasisAntiBodyDetection() {
        return onchocerciasisAntiBodyDetection;
    }

    public String getOnchocerciasisAntigenDetection() {
        return onchocerciasisAntigenDetection;
    }

    public String getOnchocerciasisRapidTest() {
        return onchocerciasisRapidTest;
    }

    public String getOnchocerciasisCulture() {
        return onchocerciasisCulture;
    }

    public String getOnchocerciasisHistopathology() {
        return onchocerciasisHistopathology;
    }

    public String getOnchocerciasisIsolation() {
        return onchocerciasisIsolation;
    }

    public String getOnchocerciasisIgmSerumAntibody() {
        return onchocerciasisIgmSerumAntibody;
    }

    public String getOnchocerciasisIggSerumAntibody() {
        return onchocerciasisIggSerumAntibody;
    }

    public String getOnchocerciasisIgaSerumAntibody() {
        return onchocerciasisIgaSerumAntibody;
    }

    public String getOnchocerciasisIncubationTime() {
        return onchocerciasisIncubationTime;
    }

    public String getOnchocerciasisIndirectFluorescentAntibody() {
        return onchocerciasisIndirectFluorescentAntibody;
    }

    public String getOnchocerciasisMicroscopy() {
        return onchocerciasisMicroscopy;
    }

    public String getOnchocerciasisNeutralizingAntibodies() {
        return onchocerciasisNeutralizingAntibodies;
    }

    public String getOnchocerciasisPcr() {
        return onchocerciasisPcr;
    }

    public String getOnchocerciasisGramStain() {
        return onchocerciasisGramStain;
    }

    public String getOnchocerciasisLatexAgglutination() {
        return onchocerciasisLatexAgglutination;
    }

    public String getOnchocerciasisCqValueDetection() {
        return onchocerciasisCqValueDetection;
    }

    public String getOnchocerciasisSeQuencing() {
        return onchocerciasisSeQuencing;
    }

    public String getOnchocerciasisDnaMicroArray() {
        return onchocerciasisDnaMicroArray;
    }

    public String getOnchocerciasisOther() {
        return onchocerciasisOther;
    }

    public String getDiphtheriaAntiBodyDetection() {
        return diphtheriaAntiBodyDetection;
    }

    public String getDiphtheriaAntigenDetection() {
        return diphtheriaAntigenDetection;
    }

    public String getDiphtheriaRapidTest() {
        return diphtheriaRapidTest;
    }

    public String getDiphtheriaCulture() {
        return diphtheriaCulture;
    }

    public String getDiphtheriaHistopathology() {
        return diphtheriaHistopathology;
    }

    public String getDiphtheriaIsolation() {
        return diphtheriaIsolation;
    }

    public String getDiphtheriaIgmSerumAntibody() {
        return diphtheriaIgmSerumAntibody;
    }

    public String getDiphtheriaIggSerumAntibody() {
        return diphtheriaIggSerumAntibody;
    }

    public String getDiphtheriaIgaSerumAntibody() {
        return diphtheriaIgaSerumAntibody;
    }

    public String getDiphtheriaIncubationTime() {
        return diphtheriaIncubationTime;
    }

    public String getDiphtheriaIndirectFluorescentAntibody() {
        return diphtheriaIndirectFluorescentAntibody;
    }

    public String getDiphtheriaMicroscopy() {
        return diphtheriaMicroscopy;
    }

    public String getDiphtheriaNeutralizingAntibodies() {
        return diphtheriaNeutralizingAntibodies;
    }

    public String getDiphtheriaPcr() {
        return diphtheriaPcr;
    }

    public String getDiphtheriaGramStain() {
        return diphtheriaGramStain;
    }

    public String getDiphtheriaLatexAgglutination() {
        return diphtheriaLatexAgglutination;
    }

    public String getDiphtheriaCqValueDetection() {
        return diphtheriaCqValueDetection;
    }

    public String getDiphtheriaSeQuencing() {
        return diphtheriaSeQuencing;
    }

    public String getDiphtheriaDnaMicroArray() {
        return diphtheriaDnaMicroArray;
    }

    public String getDiphtheriaOther() {
        return diphtheriaOther;
    }

    public String getTrachomaAntiBodyDetection() {
        return trachomaAntiBodyDetection;
    }

    public String getTrachomaAntigenDetection() {
        return trachomaAntigenDetection;
    }

    public String getTrachomaRapidTest() {
        return trachomaRapidTest;
    }

    public String getTrachomaCulture() {
        return trachomaCulture;
    }

    public String getTrachomaHistopathology() {
        return trachomaHistopathology;
    }

    public String getTrachomaIsolation() {
        return trachomaIsolation;
    }

    public String getTrachomaIgmSerumAntibody() {
        return trachomaIgmSerumAntibody;
    }

    public String getTrachomaIggSerumAntibody() {
        return trachomaIggSerumAntibody;
    }

    public String getTrachomaIgaSerumAntibody() {
        return trachomaIgaSerumAntibody;
    }

    public String getTrachomaIncubationTime() {
        return trachomaIncubationTime;
    }

    public String getTrachomaIndirectFluorescentAntibody() {
        return trachomaIndirectFluorescentAntibody;
    }

    public String getTrachomaMicroscopy() {
        return trachomaMicroscopy;
    }

    public String getTrachomaNeutralizingAntibodies() {
        return trachomaNeutralizingAntibodies;
    }

    public String getTrachomaPcr() {
        return trachomaPcr;
    }

    public String getTrachomaGramStain() {
        return trachomaGramStain;
    }

    public String getTrachomaLatexAgglutination() {
        return trachomaLatexAgglutination;
    }

    public String getTrachomaCqValueDetection() {
        return trachomaCqValueDetection;
    }

    public String getTrachomaSeQuencing() {
        return trachomaSeQuencing;
    }

    public String getTrachomaDnaMicroArray() {
        return trachomaDnaMicroArray;
    }

    public String getTrachomaOther() {
        return trachomaOther;
    }

    public String getYawsEndemicSyphilisAntiBodyDetection() {
        return yawsEndemicSyphilisAntiBodyDetection;
    }

    public String getYawsEndemicSyphilisAntigenDetection() {
        return yawsEndemicSyphilisAntigenDetection;
    }

    public String getYawsEndemicSyphilisRapidTest() {
        return yawsEndemicSyphilisRapidTest;
    }

    public String getYawsEndemicSyphilisCulture() {
        return yawsEndemicSyphilisCulture;
    }

    public String getYawsEndemicSyphilisHistopathology() {
        return yawsEndemicSyphilisHistopathology;
    }

    public String getYawsEndemicSyphilisIsolation() {
        return yawsEndemicSyphilisIsolation;
    }

    public String getYawsEndemicSyphilisIgmSerumAntibody() {
        return yawsEndemicSyphilisIgmSerumAntibody;
    }

    public String getYawsEndemicSyphilisIggSerumAntibody() {
        return yawsEndemicSyphilisIggSerumAntibody;
    }

    public String getYawsEndemicSyphilisIgaSerumAntibody() {
        return yawsEndemicSyphilisIgaSerumAntibody;
    }

    public String getYawsEndemicSyphilisIncubationTime() {
        return yawsEndemicSyphilisIncubationTime;
    }

    public String getYawsEndemicSyphilisIndirectFluorescentAntibody() {
        return yawsEndemicSyphilisIndirectFluorescentAntibody;
    }

    public String getYawsEndemicSyphilisMicroscopy() {
        return yawsEndemicSyphilisMicroscopy;
    }

    public String getYawsEndemicSyphilisNeutralizingAntibodies() {
        return yawsEndemicSyphilisNeutralizingAntibodies;
    }

    public String getYawsEndemicSyphilisPcr() {
        return yawsEndemicSyphilisPcr;
    }

    public String getYawsEndemicSyphilisGramStain() {
        return yawsEndemicSyphilisGramStain;
    }

    public String getYawsEndemicSyphilisLatexAgglutination() {
        return yawsEndemicSyphilisLatexAgglutination;
    }

    public String getYawsEndemicSyphilisCqValueDetection() {
        return yawsEndemicSyphilisCqValueDetection;
    }

    public String getYawsEndemicSyphilisSeQuencing() {
        return yawsEndemicSyphilisSeQuencing;
    }

    public String getYawsEndemicSyphilisDnaMicroArray() {
        return yawsEndemicSyphilisDnaMicroArray;
    }

    public String getYawsEndemicSyphilisOther() {
        return yawsEndemicSyphilisOther;
    }

    public String getMaternalDeathsAntiBodyDetection() {
        return maternalDeathsAntiBodyDetection;
    }

    public String getMaternalDeathsAntigenDetection() {
        return maternalDeathsAntigenDetection;
    }

    public String getMaternalDeathsRapidTest() {
        return maternalDeathsRapidTest;
    }

    public String getMaternalDeathsCulture() {
        return maternalDeathsCulture;
    }

    public String getMaternalDeathsHistopathology() {
        return maternalDeathsHistopathology;
    }

    public String getMaternalDeathsIsolation() {
        return maternalDeathsIsolation;
    }

    public String getMaternalDeathsIgmSerumAntibody() {
        return maternalDeathsIgmSerumAntibody;
    }

    public String getMaternalDeathsIggSerumAntibody() {
        return maternalDeathsIggSerumAntibody;
    }

    public String getMaternalDeathsIgaSerumAntibody() {
        return maternalDeathsIgaSerumAntibody;
    }

    public String getMaternalDeathsIncubationTime() {
        return maternalDeathsIncubationTime;
    }

    public String getMaternalDeathsIndirectFluorescentAntibody() {
        return maternalDeathsIndirectFluorescentAntibody;
    }

    public String getMaternalDeathsMicroscopy() {
        return maternalDeathsMicroscopy;
    }

    public String getMaternalDeathsNeutralizingAntibodies() {
        return maternalDeathsNeutralizingAntibodies;
    }

    public String getMaternalDeathsPcr() {
        return maternalDeathsPcr;
    }

    public String getMaternalDeathsGramStain() {
        return maternalDeathsGramStain;
    }

    public String getMaternalDeathsLatexAgglutination() {
        return maternalDeathsLatexAgglutination;
    }

    public String getMaternalDeathsCqValueDetection() {
        return maternalDeathsCqValueDetection;
    }

    public String getMaternalDeathsSeQuencing() {
        return maternalDeathsSeQuencing;
    }

    public String getMaternalDeathsDnaMicroArray() {
        return maternalDeathsDnaMicroArray;
    }

    public String getMaternalDeathsOther() {
        return maternalDeathsOther;
    }

    public String getPerinatalDeathsAntiBodyDetection() {
        return perinatalDeathsAntiBodyDetection;
    }

    public String getPerinatalDeathsAntigenDetection() {
        return perinatalDeathsAntigenDetection;
    }

    public String getPerinatalDeathsRapidTest() {
        return perinatalDeathsRapidTest;
    }

    public String getPerinatalDeathsCulture() {
        return perinatalDeathsCulture;
    }

    public String getPerinatalDeathsHistopathology() {
        return perinatalDeathsHistopathology;
    }

    public String getPerinatalDeathsIsolation() {
        return perinatalDeathsIsolation;
    }

    public String getPerinatalDeathsIgmSerumAntibody() {
        return perinatalDeathsIgmSerumAntibody;
    }

    public String getPerinatalDeathsIggSerumAntibody() {
        return perinatalDeathsIggSerumAntibody;
    }

    public String getPerinatalDeathsIgaSerumAntibody() {
        return perinatalDeathsIgaSerumAntibody;
    }

    public String getPerinatalDeathsIncubationTime() {
        return perinatalDeathsIncubationTime;
    }

    public String getPerinatalDeathsIndirectFluorescentAntibody() {
        return perinatalDeathsIndirectFluorescentAntibody;
    }

    public String getPerinatalDeathsMicroscopy() {
        return perinatalDeathsMicroscopy;
    }

    public String getPerinatalDeathsNeutralizingAntibodies() {
        return perinatalDeathsNeutralizingAntibodies;
    }

    public String getPerinatalDeathsPcr() {
        return perinatalDeathsPcr;
    }

    public String getPerinatalDeathsGramStain() {
        return perinatalDeathsGramStain;
    }

    public String getPerinatalDeathsLatexAgglutination() {
        return perinatalDeathsLatexAgglutination;
    }

    public String getPerinatalDeathsCqValueDetection() {
        return perinatalDeathsCqValueDetection;
    }

    public String getPerinatalDeathsSeQuencing() {
        return perinatalDeathsSeQuencing;
    }

    public String getPerinatalDeathsDnaMicroArray() {
        return perinatalDeathsDnaMicroArray;
    }

    public String getPerinatalDeathsOther() {
        return perinatalDeathsOther;
    }

    public String getInfluenzaAAntiBodyDetection() {
        return influenzaAAntiBodyDetection;
    }

    public String getInfluenzaAAntigenDetection() {
        return influenzaAAntigenDetection;
    }

    public String getInfluenzaARapidTest() {
        return influenzaARapidTest;
    }

    public String getInfluenzaACulture() {
        return influenzaACulture;
    }

    public String getInfluenzaAHistopathology() {
        return influenzaAHistopathology;
    }

    public String getInfluenzaAIsolation() {
        return influenzaAIsolation;
    }

    public String getInfluenzaAIgmSerumAntibody() {
        return influenzaAIgmSerumAntibody;
    }

    public String getInfluenzaAIggSerumAntibody() {
        return influenzaAIggSerumAntibody;
    }

    public String getInfluenzaAIgaSerumAntibody() {
        return influenzaAIgaSerumAntibody;
    }

    public String getInfluenzaAIncubationTime() {
        return influenzaAIncubationTime;
    }

    public String getInfluenzaAIndirectFluorescentAntibody() {
        return influenzaAIndirectFluorescentAntibody;
    }

    public String getInfluenzaAMicroscopy() {
        return influenzaAMicroscopy;
    }

    public String getInfluenzaANeutralizingAntibodies() {
        return influenzaANeutralizingAntibodies;
    }

    public String getInfluenzaAPcr() {
        return influenzaAPcr;
    }

    public String getInfluenzaAGramStain() {
        return influenzaAGramStain;
    }

    public String getInfluenzaALatexAgglutination() {
        return influenzaALatexAgglutination;
    }

    public String getInfluenzaACqValueDetection() {
        return influenzaACqValueDetection;
    }

    public String getInfluenzaASeQuencing() {
        return influenzaASeQuencing;
    }

    public String getInfluenzaADnaMicroArray() {
        return influenzaADnaMicroArray;
    }

    public String getInfluenzaAOther() {
        return influenzaAOther;
    }

    public String getInfluenzaBAntiBodyDetection() {
        return influenzaBAntiBodyDetection;
    }

    public String getInfluenzaBAntigenDetection() {
        return influenzaBAntigenDetection;
    }

    public String getInfluenzaBRapidTest() {
        return influenzaBRapidTest;
    }

    public String getInfluenzaBCulture() {
        return influenzaBCulture;
    }

    public String getInfluenzaBHistopathology() {
        return influenzaBHistopathology;
    }

    public String getInfluenzaBIsolation() {
        return influenzaBIsolation;
    }

    public String getInfluenzaBIgmSerumAntibody() {
        return influenzaBIgmSerumAntibody;
    }

    public String getInfluenzaBIggSerumAntibody() {
        return influenzaBIggSerumAntibody;
    }

    public String getInfluenzaBIgaSerumAntibody() {
        return influenzaBIgaSerumAntibody;
    }

    public String getInfluenzaBIncubationTime() {
        return influenzaBIncubationTime;
    }

    public String getInfluenzaBIndirectFluorescentAntibody() {
        return influenzaBIndirectFluorescentAntibody;
    }

    public String getInfluenzaBMicroscopy() {
        return influenzaBMicroscopy;
    }

    public String getInfluenzaBNeutralizingAntibodies() {
        return influenzaBNeutralizingAntibodies;
    }

    public String getInfluenzaBPcr() {
        return influenzaBPcr;
    }

    public String getInfluenzaBGramStain() {
        return influenzaBGramStain;
    }

    public String getInfluenzaBLatexAgglutination() {
        return influenzaBLatexAgglutination;
    }

    public String getInfluenzaBCqValueDetection() {
        return influenzaBCqValueDetection;
    }

    public String getInfluenzaBSeQuencing() {
        return influenzaBSeQuencing;
    }

    public String getInfluenzaBDnaMicroArray() {
        return influenzaBDnaMicroArray;
    }

    public String getInfluenzaBOther() {
        return influenzaBOther;
    }

    public String gethMetapneumovirusAntiBodyDetection() {
        return hMetapneumovirusAntiBodyDetection;
    }

    public String gethMetapneumovirusAntigenDetection() {
        return hMetapneumovirusAntigenDetection;
    }

    public String gethMetapneumovirusRapidTest() {
        return hMetapneumovirusRapidTest;
    }

    public String gethMetapneumovirusCulture() {
        return hMetapneumovirusCulture;
    }

    public String gethMetapneumovirusHistopathology() {
        return hMetapneumovirusHistopathology;
    }

    public String gethMetapneumovirusIsolation() {
        return hMetapneumovirusIsolation;
    }

    public String gethMetapneumovirusIgmSerumAntibody() {
        return hMetapneumovirusIgmSerumAntibody;
    }

    public String gethMetapneumovirusIggSerumAntibody() {
        return hMetapneumovirusIggSerumAntibody;
    }

    public String gethMetapneumovirusIgaSerumAntibody() {
        return hMetapneumovirusIgaSerumAntibody;
    }

    public String gethMetapneumovirusIncubationTime() {
        return hMetapneumovirusIncubationTime;
    }

    public String gethMetapneumovirusIndirectFluorescentAntibody() {
        return hMetapneumovirusIndirectFluorescentAntibody;
    }

    public String gethMetapneumovirusMicroscopy() {
        return hMetapneumovirusMicroscopy;
    }

    public String gethMetapneumovirusNeutralizingAntibodies() {
        return hMetapneumovirusNeutralizingAntibodies;
    }

    public String gethMetapneumovirusPcr() {
        return hMetapneumovirusPcr;
    }

    public String gethMetapneumovirusGramStain() {
        return hMetapneumovirusGramStain;
    }

    public String gethMetapneumovirusLatexAgglutination() {
        return hMetapneumovirusLatexAgglutination;
    }

    public String gethMetapneumovirusCqValueDetection() {
        return hMetapneumovirusCqValueDetection;
    }

    public String gethMetapneumovirusSeQuencing() {
        return hMetapneumovirusSeQuencing;
    }

    public String gethMetapneumovirusDnaMicroArray() {
        return hMetapneumovirusDnaMicroArray;
    }

    public String gethMetapneumovirusOther() {
        return hMetapneumovirusOther;
    }

    public String getRespiratorySyncytialVirusAntiBodyDetection() {
        return respiratorySyncytialVirusAntiBodyDetection;
    }

    public String getRespiratorySyncytialVirusAntigenDetection() {
        return respiratorySyncytialVirusAntigenDetection;
    }

    public String getRespiratorySyncytialVirusRapidTest() {
        return respiratorySyncytialVirusRapidTest;
    }

    public String getRespiratorySyncytialVirusCulture() {
        return respiratorySyncytialVirusCulture;
    }

    public String getRespiratorySyncytialVirusHistopathology() {
        return respiratorySyncytialVirusHistopathology;
    }

    public String getRespiratorySyncytialVirusIsolation() {
        return respiratorySyncytialVirusIsolation;
    }

    public String getRespiratorySyncytialVirusIgmSerumAntibody() {
        return respiratorySyncytialVirusIgmSerumAntibody;
    }

    public String getRespiratorySyncytialVirusIggSerumAntibody() {
        return respiratorySyncytialVirusIggSerumAntibody;
    }

    public String getRespiratorySyncytialVirusIgaSerumAntibody() {
        return respiratorySyncytialVirusIgaSerumAntibody;
    }

    public String getRespiratorySyncytialVirusIncubationTime() {
        return respiratorySyncytialVirusIncubationTime;
    }

    public String getRespiratorySyncytialVirusIndirectFluorescentAntibody() {
        return respiratorySyncytialVirusIndirectFluorescentAntibody;
    }

    public String getRespiratorySyncytialVirusMicroscopy() {
        return respiratorySyncytialVirusMicroscopy;
    }

    public String getRespiratorySyncytialVirusNeutralizingAntibodies() {
        return respiratorySyncytialVirusNeutralizingAntibodies;
    }

    public String getRespiratorySyncytialVirusPcr() {
        return respiratorySyncytialVirusPcr;
    }

    public String getRespiratorySyncytialVirusGramStain() {
        return respiratorySyncytialVirusGramStain;
    }

    public String getRespiratorySyncytialVirusLatexAgglutination() {
        return respiratorySyncytialVirusLatexAgglutination;
    }

    public String getRespiratorySyncytialVirusCqValueDetection() {
        return respiratorySyncytialVirusCqValueDetection;
    }

    public String getRespiratorySyncytialVirusSeQuencing() {
        return respiratorySyncytialVirusSeQuencing;
    }

    public String getRespiratorySyncytialVirusDnaMicroArray() {
        return respiratorySyncytialVirusDnaMicroArray;
    }

    public String getRespiratorySyncytialVirusOther() {
        return respiratorySyncytialVirusOther;
    }

    public String getParainfluenza1_4AntiBodyDetection() {
        return parainfluenza1_4AntiBodyDetection;
    }

    public String getParainfluenza1_4AntigenDetection() {
        return parainfluenza1_4AntigenDetection;
    }

    public String getParainfluenza1_4RapidTest() {
        return parainfluenza1_4RapidTest;
    }

    public String getParainfluenza1_4Culture() {
        return parainfluenza1_4Culture;
    }

    public String getParainfluenza1_4Histopathology() {
        return parainfluenza1_4Histopathology;
    }

    public String getParainfluenza1_4Isolation() {
        return parainfluenza1_4Isolation;
    }

    public String getParainfluenza1_4IgmSerumAntibody() {
        return parainfluenza1_4IgmSerumAntibody;
    }

    public String getParainfluenza1_4IggSerumAntibody() {
        return parainfluenza1_4IggSerumAntibody;
    }

    public String getParainfluenza1_4IgaSerumAntibody() {
        return parainfluenza1_4IgaSerumAntibody;
    }

    public String getParainfluenza1_4IncubationTime() {
        return parainfluenza1_4IncubationTime;
    }

    public String getParainfluenza1_4IndirectFluorescentAntibody() {
        return parainfluenza1_4IndirectFluorescentAntibody;
    }

    public String getParainfluenza1_4Microscopy() {
        return parainfluenza1_4Microscopy;
    }

    public String getParainfluenza1_4NeutralizingAntibodies() {
        return parainfluenza1_4NeutralizingAntibodies;
    }

    public String getParainfluenza1_4Pcr() {
        return parainfluenza1_4Pcr;
    }

    public String getParainfluenza1_4GramStain() {
        return parainfluenza1_4GramStain;
    }

    public String getParainfluenza1_4LatexAgglutination() {
        return parainfluenza1_4LatexAgglutination;
    }

    public String getParainfluenza1_4CqValueDetection() {
        return parainfluenza1_4CqValueDetection;
    }

    public String getParainfluenza1_4SeQuencing() {
        return parainfluenza1_4SeQuencing;
    }

    public String getParainfluenza1_4DnaMicroArray() {
        return parainfluenza1_4DnaMicroArray;
    }

    public String getParainfluenza1_4Other() {
        return parainfluenza1_4Other;
    }

    public String getAdenovirusAntiBodyDetection() {
        return adenovirusAntiBodyDetection;
    }

    public String getAdenovirusAntigenDetection() {
        return adenovirusAntigenDetection;
    }

    public String getAdenovirusRapidTest() {
        return adenovirusRapidTest;
    }

    public String getAdenovirusCulture() {
        return adenovirusCulture;
    }

    public String getAdenovirusHistopathology() {
        return adenovirusHistopathology;
    }

    public String getAdenovirusIsolation() {
        return adenovirusIsolation;
    }

    public String getAdenovirusIgmSerumAntibody() {
        return adenovirusIgmSerumAntibody;
    }

    public String getAdenovirusIggSerumAntibody() {
        return adenovirusIggSerumAntibody;
    }

    public String getAdenovirusIgaSerumAntibody() {
        return adenovirusIgaSerumAntibody;
    }

    public String getAdenovirusIncubationTime() {
        return adenovirusIncubationTime;
    }

    public String getAdenovirusIndirectFluorescentAntibody() {
        return adenovirusIndirectFluorescentAntibody;
    }

    public String getAdenovirusMicroscopy() {
        return adenovirusMicroscopy;
    }

    public String getAdenovirusNeutralizingAntibodies() {
        return adenovirusNeutralizingAntibodies;
    }

    public String getAdenovirusPcr() {
        return adenovirusPcr;
    }

    public String getAdenovirusGramStain() {
        return adenovirusGramStain;
    }

    public String getAdenovirusLatexAgglutination() {
        return adenovirusLatexAgglutination;
    }

    public String getAdenovirusCqValueDetection() {
        return adenovirusCqValueDetection;
    }

    public String getAdenovirusSeQuencing() {
        return adenovirusSeQuencing;
    }

    public String getAdenovirusDnaMicroArray() {
        return adenovirusDnaMicroArray;
    }

    public String getAdenovirusOther() {
        return adenovirusOther;
    }

    public String getRhinovirusAntiBodyDetection() {
        return rhinovirusAntiBodyDetection;
    }

    public String getRhinovirusAntigenDetection() {
        return rhinovirusAntigenDetection;
    }

    public String getRhinovirusRapidTest() {
        return rhinovirusRapidTest;
    }

    public String getRhinovirusCulture() {
        return rhinovirusCulture;
    }

    public String getRhinovirusHistopathology() {
        return rhinovirusHistopathology;
    }

    public String getRhinovirusIsolation() {
        return rhinovirusIsolation;
    }

    public String getRhinovirusIgmSerumAntibody() {
        return rhinovirusIgmSerumAntibody;
    }

    public String getRhinovirusIggSerumAntibody() {
        return rhinovirusIggSerumAntibody;
    }

    public String getRhinovirusIgaSerumAntibody() {
        return rhinovirusIgaSerumAntibody;
    }

    public String getRhinovirusIncubationTime() {
        return rhinovirusIncubationTime;
    }

    public String getRhinovirusIndirectFluorescentAntibody() {
        return rhinovirusIndirectFluorescentAntibody;
    }

    public String getRhinovirusMicroscopy() {
        return rhinovirusMicroscopy;
    }

    public String getRhinovirusNeutralizingAntibodies() {
        return rhinovirusNeutralizingAntibodies;
    }

    public String getRhinovirusPcr() {
        return rhinovirusPcr;
    }

    public String getRhinovirusGramStain() {
        return rhinovirusGramStain;
    }

    public String getRhinovirusLatexAgglutination() {
        return rhinovirusLatexAgglutination;
    }

    public String getRhinovirusCqValueDetection() {
        return rhinovirusCqValueDetection;
    }

    public String getRhinovirusSeQuencing() {
        return rhinovirusSeQuencing;
    }

    public String getRhinovirusDnaMicroArray() {
        return rhinovirusDnaMicroArray;
    }

    public String getRhinovirusOther() {
        return rhinovirusOther;
    }

    public String getEnterovirusAntiBodyDetection() {
        return enterovirusAntiBodyDetection;
    }

    public String getEnterovirusAntigenDetection() {
        return enterovirusAntigenDetection;
    }

    public String getEnterovirusRapidTest() {
        return enterovirusRapidTest;
    }

    public String getEnterovirusCulture() {
        return enterovirusCulture;
    }

    public String getEnterovirusHistopathology() {
        return enterovirusHistopathology;
    }

    public String getEnterovirusIsolation() {
        return enterovirusIsolation;
    }

    public String getEnterovirusIgmSerumAntibody() {
        return enterovirusIgmSerumAntibody;
    }

    public String getEnterovirusIggSerumAntibody() {
        return enterovirusIggSerumAntibody;
    }

    public String getEnterovirusIgaSerumAntibody() {
        return enterovirusIgaSerumAntibody;
    }

    public String getEnterovirusIncubationTime() {
        return enterovirusIncubationTime;
    }

    public String getEnterovirusIndirectFluorescentAntibody() {
        return enterovirusIndirectFluorescentAntibody;
    }

    public String getEnterovirusMicroscopy() {
        return enterovirusMicroscopy;
    }

    public String getEnterovirusNeutralizingAntibodies() {
        return enterovirusNeutralizingAntibodies;
    }

    public String getEnterovirusPcr() {
        return enterovirusPcr;
    }

    public String getEnterovirusGramStain() {
        return enterovirusGramStain;
    }

    public String getEnterovirusLatexAgglutination() {
        return enterovirusLatexAgglutination;
    }

    public String getEnterovirusCqValueDetection() {
        return enterovirusCqValueDetection;
    }

    public String getEnterovirusSeQuencing() {
        return enterovirusSeQuencing;
    }

    public String getEnterovirusDnaMicroArray() {
        return enterovirusDnaMicroArray;
    }

    public String getEnterovirusOther() {
        return enterovirusOther;
    }

    public String getmPneumoniaeAntiBodyDetection() {
        return mPneumoniaeAntiBodyDetection;
    }

    public String getmPneumoniaeAntigenDetection() {
        return mPneumoniaeAntigenDetection;
    }

    public String getmPneumoniaeRapidTest() {
        return mPneumoniaeRapidTest;
    }

    public String getmPneumoniaeCulture() {
        return mPneumoniaeCulture;
    }

    public String getmPneumoniaeHistopathology() {
        return mPneumoniaeHistopathology;
    }

    public String getmPneumoniaeIsolation() {
        return mPneumoniaeIsolation;
    }

    public String getmPneumoniaeIgmSerumAntibody() {
        return mPneumoniaeIgmSerumAntibody;
    }

    public String getmPneumoniaeIggSerumAntibody() {
        return mPneumoniaeIggSerumAntibody;
    }

    public String getmPneumoniaeIgaSerumAntibody() {
        return mPneumoniaeIgaSerumAntibody;
    }

    public String getmPneumoniaeIncubationTime() {
        return mPneumoniaeIncubationTime;
    }

    public String getmPneumoniaeIndirectFluorescentAntibody() {
        return mPneumoniaeIndirectFluorescentAntibody;
    }

    public String getmPneumoniaeMicroscopy() {
        return mPneumoniaeMicroscopy;
    }

    public String getmPneumoniaeNeutralizingAntibodies() {
        return mPneumoniaeNeutralizingAntibodies;
    }

    public String getmPneumoniaePcr() {
        return mPneumoniaePcr;
    }

    public String getmPneumoniaeGramStain() {
        return mPneumoniaeGramStain;
    }

    public String getmPneumoniaeLatexAgglutination() {
        return mPneumoniaeLatexAgglutination;
    }

    public String getmPneumoniaeCqValueDetection() {
        return mPneumoniaeCqValueDetection;
    }

    public String getmPneumoniaeSeQuencing() {
        return mPneumoniaeSeQuencing;
    }

    public String getmPneumoniaeDnaMicroArray() {
        return mPneumoniaeDnaMicroArray;
    }

    public String getmPneumoniaeOther() {
        return mPneumoniaeOther;
    }

    public String getcPneumoniaeAntiBodyDetection() {
        return cPneumoniaeAntiBodyDetection;
    }

    public String getcPneumoniaeAntigenDetection() {
        return cPneumoniaeAntigenDetection;
    }

    public String getcPneumoniaeRapidTest() {
        return cPneumoniaeRapidTest;
    }

    public String getcPneumoniaeCulture() {
        return cPneumoniaeCulture;
    }

    public String getcPneumoniaeHistopathology() {
        return cPneumoniaeHistopathology;
    }

    public String getcPneumoniaeIsolation() {
        return cPneumoniaeIsolation;
    }

    public String getcPneumoniaeIgmSerumAntibody() {
        return cPneumoniaeIgmSerumAntibody;
    }

    public String getcPneumoniaeIggSerumAntibody() {
        return cPneumoniaeIggSerumAntibody;
    }

    public String getcPneumoniaeIgaSerumAntibody() {
        return cPneumoniaeIgaSerumAntibody;
    }

    public String getcPneumoniaeIncubationTime() {
        return cPneumoniaeIncubationTime;
    }

    public String getcPneumoniaeIndirectFluorescentAntibody() {
        return cPneumoniaeIndirectFluorescentAntibody;
    }

    public String getcPneumoniaeMicroscopy() {
        return cPneumoniaeMicroscopy;
    }

    public String getcPneumoniaeNeutralizingAntibodies() {
        return cPneumoniaeNeutralizingAntibodies;
    }

    public String getcPneumoniaePcr() {
        return cPneumoniaePcr;
    }

    public String getcPneumoniaeGramStain() {
        return cPneumoniaeGramStain;
    }

    public String getcPneumoniaeLatexAgglutination() {
        return cPneumoniaeLatexAgglutination;
    }

    public String getcPneumoniaeCqValueDetection() {
        return cPneumoniaeCqValueDetection;
    }

    public String getcPneumoniaeSeQuencing() {
        return cPneumoniaeSeQuencing;
    }

    public String getcPneumoniaeDnaMicroArray() {
        return cPneumoniaeDnaMicroArray;
    }

    public String getcPneumoniaeOther() {
        return cPneumoniaeOther;
    }

    public String getAriAntiBodyDetection() {
        return ariAntiBodyDetection;
    }

    public String getAriAntigenDetection() {
        return ariAntigenDetection;
    }

    public String getAriRapidTest() {
        return ariRapidTest;
    }

    public String getAriCulture() {
        return ariCulture;
    }

    public String getAriHistopathology() {
        return ariHistopathology;
    }

    public String getAriIsolation() {
        return ariIsolation;
    }

    public String getAriIgmSerumAntibody() {
        return ariIgmSerumAntibody;
    }

    public String getAriIggSerumAntibody() {
        return ariIggSerumAntibody;
    }

    public String getAriIgaSerumAntibody() {
        return ariIgaSerumAntibody;
    }

    public String getAriIncubationTime() {
        return ariIncubationTime;
    }

    public String getAriIndirectFluorescentAntibody() {
        return ariIndirectFluorescentAntibody;
    }

    public String getAriMicroscopy() {
        return ariMicroscopy;
    }

    public String getAriNeutralizingAntibodies() {
        return ariNeutralizingAntibodies;
    }

    public String getAriPcr() {
        return ariPcr;
    }

    public String getAriGramStain() {
        return ariGramStain;
    }

    public String getAriLatexAgglutination() {
        return ariLatexAgglutination;
    }

    public String getAriCqValueDetection() {
        return ariCqValueDetection;
    }

    public String getAriSeQuencing() {
        return ariSeQuencing;
    }

    public String getAriDnaMicroArray() {
        return ariDnaMicroArray;
    }

    public String getAriOther() {
        return ariOther;
    }

    public String getChikungunyaAntiBodyDetection() {
        return chikungunyaAntiBodyDetection;
    }

    public String getChikungunyaAntigenDetection() {
        return chikungunyaAntigenDetection;
    }

    public String getChikungunyaRapidTest() {
        return chikungunyaRapidTest;
    }

    public String getChikungunyaCulture() {
        return chikungunyaCulture;
    }

    public String getChikungunyaHistopathology() {
        return chikungunyaHistopathology;
    }

    public String getChikungunyaIsolation() {
        return chikungunyaIsolation;
    }

    public String getChikungunyaIgmSerumAntibody() {
        return chikungunyaIgmSerumAntibody;
    }

    public String getChikungunyaIggSerumAntibody() {
        return chikungunyaIggSerumAntibody;
    }

    public String getChikungunyaIgaSerumAntibody() {
        return chikungunyaIgaSerumAntibody;
    }

    public String getChikungunyaIncubationTime() {
        return chikungunyaIncubationTime;
    }

    public String getChikungunyaIndirectFluorescentAntibody() {
        return chikungunyaIndirectFluorescentAntibody;
    }

    public String getChikungunyaMicroscopy() {
        return chikungunyaMicroscopy;
    }

    public String getChikungunyaNeutralizingAntibodies() {
        return chikungunyaNeutralizingAntibodies;
    }

    public String getChikungunyaPcr() {
        return chikungunyaPcr;
    }

    public String getChikungunyaGramStain() {
        return chikungunyaGramStain;
    }

    public String getChikungunyaLatexAgglutination() {
        return chikungunyaLatexAgglutination;
    }

    public String getChikungunyaCqValueDetection() {
        return chikungunyaCqValueDetection;
    }

    public String getChikungunyaSeQuencing() {
        return chikungunyaSeQuencing;
    }

    public String getChikungunyaDnaMicroArray() {
        return chikungunyaDnaMicroArray;
    }

    public String getChikungunyaOther() {
        return chikungunyaOther;
    }

    public String getPostImmunizationAdverseEventsMildAntiBodyDetection() {
        return postImmunizationAdverseEventsMildAntiBodyDetection;
    }

    public String getPostImmunizationAdverseEventsMildAntigenDetection() {
        return postImmunizationAdverseEventsMildAntigenDetection;
    }

    public String getPostImmunizationAdverseEventsMildRapidTest() {
        return postImmunizationAdverseEventsMildRapidTest;
    }

    public String getPostImmunizationAdverseEventsMildCulture() {
        return postImmunizationAdverseEventsMildCulture;
    }

    public String getPostImmunizationAdverseEventsMildHistopathology() {
        return postImmunizationAdverseEventsMildHistopathology;
    }

    public String getPostImmunizationAdverseEventsMildIsolation() {
        return postImmunizationAdverseEventsMildIsolation;
    }

    public String getPostImmunizationAdverseEventsMildIgmSerumAntibody() {
        return postImmunizationAdverseEventsMildIgmSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsMildIggSerumAntibody() {
        return postImmunizationAdverseEventsMildIggSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsMildIgaSerumAntibody() {
        return postImmunizationAdverseEventsMildIgaSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsMildIncubationTime() {
        return postImmunizationAdverseEventsMildIncubationTime;
    }

    public String getPostImmunizationAdverseEventsMildIndirectFluorescentAntibody() {
        return postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    }

    public String getPostImmunizationAdverseEventsMildMicroscopy() {
        return postImmunizationAdverseEventsMildMicroscopy;
    }

    public String getPostImmunizationAdverseEventsMildNeutralizingAntibodies() {
        return postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    public String getPostImmunizationAdverseEventsMildPcr() {
        return postImmunizationAdverseEventsMildPcr;
    }

    public String getPostImmunizationAdverseEventsMildGramStain() {
        return postImmunizationAdverseEventsMildGramStain;
    }

    public String getPostImmunizationAdverseEventsMildLatexAgglutination() {
        return postImmunizationAdverseEventsMildLatexAgglutination;
    }

    public String getPostImmunizationAdverseEventsMildCqValueDetection() {
        return postImmunizationAdverseEventsMildCqValueDetection;
    }

    public String getPostImmunizationAdverseEventsMildSeQuencing() {
        return postImmunizationAdverseEventsMildSeQuencing;
    }

    public String getPostImmunizationAdverseEventsMildDnaMicroArray() {
        return postImmunizationAdverseEventsMildDnaMicroArray;
    }

    public String getPostImmunizationAdverseEventsMildOther() {
        return postImmunizationAdverseEventsMildOther;
    }

    public String getPostImmunizationAdverseEventsSevereAntiBodyDetection() {
        return PostImmunizationAdverseEventsSevereAntiBodyDetection;
    }

    public String getPostImmunizationAdverseEventsSevereAntigenDetection() {
        return PostImmunizationAdverseEventsSevereAntigenDetection;
    }

    public String getPostImmunizationAdverseEventsSevereRapidTest() {
        return PostImmunizationAdverseEventsSevereRapidTest;
    }

    public String getPostImmunizationAdverseEventsSevereCulture() {
        return PostImmunizationAdverseEventsSevereCulture;
    }

    public String getPostImmunizationAdverseEventsSevereHistopathology() {
        return PostImmunizationAdverseEventsSevereHistopathology;
    }

    public String getPostImmunizationAdverseEventsSevereIsolation() {
        return PostImmunizationAdverseEventsSevereIsolation;
    }

    public String getPostImmunizationAdverseEventsSevereIgmSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIggSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIgaSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIncubationTime() {
        return PostImmunizationAdverseEventsSevereIncubationTime;
    }

    public String getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody() {
        return PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereMicroscopy() {
        return PostImmunizationAdverseEventsSevereMicroscopy;
    }

    public String getPostImmunizationAdverseEventsSevereNeutralizingAntibodies() {
        return PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    public String getPostImmunizationAdverseEventsSeverePcr() {
        return PostImmunizationAdverseEventsSeverePcr;
    }

    public String getPostImmunizationAdverseEventsSevereGramStain() {
        return PostImmunizationAdverseEventsSevereGramStain;
    }

    public String getPostImmunizationAdverseEventsSevereLatexAgglutination() {
        return PostImmunizationAdverseEventsSevereLatexAgglutination;
    }

    public String getPostImmunizationAdverseEventsSevereCqValueDetection() {
        return PostImmunizationAdverseEventsSevereCqValueDetection;
    }

    public String getPostImmunizationAdverseEventsSevereSeQuencing() {
        return PostImmunizationAdverseEventsSevereSeQuencing;
    }

    public String getPostImmunizationAdverseEventsSevereDnaMicroArray() {
        return PostImmunizationAdverseEventsSevereDnaMicroArray;
    }

    public String getPostImmunizationAdverseEventsSevereOther() {
        return PostImmunizationAdverseEventsSevereOther;
    }

    public String getFhaAntiBodyDetection() {
        return fhaAntiBodyDetection;
    }

    public String getFhaAntigenDetection() {
        return fhaAntigenDetection;
    }

    public String getFhaRapidTest() {
        return fhaRapidTest;
    }

    public String getFhaCulture() {
        return fhaCulture;
    }

    public String getFhaHistopathology() {
        return fhaHistopathology;
    }

    public String getFhaIsolation() {
        return fhaIsolation;
    }

    public String getFhaIgmSerumAntibody() {
        return fhaIgmSerumAntibody;
    }

    public String getFhaIggSerumAntibody() {
        return fhaIggSerumAntibody;
    }

    public String getFhaIgaSerumAntibody() {
        return fhaIgaSerumAntibody;
    }

    public String getFhaIncubationTime() {
        return fhaIncubationTime;
    }

    public String getFhaIndirectFluorescentAntibody() {
        return fhaIndirectFluorescentAntibody;
    }

    public String getFhaMicroscopy() {
        return fhaMicroscopy;
    }

    public String getFhaNeutralizingAntibodies() {
        return fhaNeutralizingAntibodies;
    }

    public String getFhaPcr() {
        return fhaPcr;
    }

    public String getFhaGramStain() {
        return fhaGramStain;
    }

    public String getFhaLatexAgglutination() {
        return fhaLatexAgglutination;
    }

    public String getFhaCqValueDetection() {
        return fhaCqValueDetection;
    }

    public String getFhaSeQuencing() {
        return fhaSeQuencing;
    }

    public String getFhaDnaMicroArray() {
        return fhaDnaMicroArray;
    }

    public String getFhaOther() {
        return fhaOther;
    }

    public String getOtherAntiBodyDetection() {
        return otherAntiBodyDetection;
    }

    public String getOtherAntigenDetection() {
        return otherAntigenDetection;
    }

    public String getOtherRapidTest() {
        return otherRapidTest;
    }

    public String getOtherCulture() {
        return otherCulture;
    }

    public String getOtherHistopathology() {
        return otherHistopathology;
    }

    public String getOtherIsolation() {
        return otherIsolation;
    }

    public String getOtherIgmSerumAntibody() {
        return otherIgmSerumAntibody;
    }

    public String getOtherIggSerumAntibody() {
        return otherIggSerumAntibody;
    }

    public String getOtherIgaSerumAntibody() {
        return otherIgaSerumAntibody;
    }

    public String getOtherIncubationTime() {
        return otherIncubationTime;
    }

    public String getOtherIndirectFluorescentAntibody() {
        return otherIndirectFluorescentAntibody;
    }

    public String getOtherMicroscopy() {
        return otherMicroscopy;
    }

    public String getOtherNeutralizingAntibodies() {
        return otherNeutralizingAntibodies;
    }

    public String getOtherPcr() {
        return otherPcr;
    }

    public String getOtherGramStain() {
        return otherGramStain;
    }

    public String getOtherLatexAgglutination() {
        return otherLatexAgglutination;
    }

    public String getOtherCqValueDetection() {
        return otherCqValueDetection;
    }

    public String getOtherSeQuencing() {
        return otherSeQuencing;
    }

    public String getOtherDnaMicroArray() {
        return otherDnaMicroArray;
    }

    public String getOtherOther() {
        return otherOther;
    }

    public String getUndefinedAntiBodyDetection() {
        return undefinedAntiBodyDetection;
    }

    public String getUndefinedAntigenDetection() {
        return undefinedAntigenDetection;
    }

    public String getUndefinedRapidTest() {
        return undefinedRapidTest;
    }

    public String getUndefinedCulture() {
        return undefinedCulture;
    }

    public String getUndefinedHistopathology() {
        return undefinedHistopathology;
    }

    public String getUndefinedIsolation() {
        return undefinedIsolation;
    }

    public String getUndefinedIgmSerumAntibody() {
        return undefinedIgmSerumAntibody;
    }

    public String getUndefinedIggSerumAntibody() {
        return undefinedIggSerumAntibody;
    }

    public String getUndefinedIgaSerumAntibody() {
        return undefinedIgaSerumAntibody;
    }

    public String getUndefinedIncubationTime() {
        return undefinedIncubationTime;
    }

    public String getUndefinedIndirectFluorescentAntibody() {
        return undefinedIndirectFluorescentAntibody;
    }

    public String getUndefinedMicroscopy() {
        return undefinedMicroscopy;
    }

    public String getUndefinedNeutralizingAntibodies() {
        return undefinedNeutralizingAntibodies;
    }

    public String getUndefinedPcr() {
        return undefinedPcr;
    }

    public String getUndefinedGramStain() {
        return undefinedGramStain;
    }

    public String getUndefinedLatexAgglutination() {
        return undefinedLatexAgglutination;
    }

    public String getUndefinedCqValueDetection() {
        return undefinedCqValueDetection;
    }

    public String getUndefinedSeQuencing() {
        return undefinedSeQuencing;
    }

    public String getUndefinedDnaMicroArray() {
        return undefinedDnaMicroArray;
    }

    public String getUndefinedOther() {
        return undefinedOther;
    }
}
