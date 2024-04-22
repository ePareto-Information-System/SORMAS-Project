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

    private String afpAntibodyDetection;
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
    private String afpDirectFluorescentAntibody;
    private String afpMicroscopy;
    private String afpNeutralizingAntibodies;
    private String afpPcrRtPcr;
    private String afpGramStain;
    private String afpLatexAgglutination;
    private String afpCqValueDetection;
    private String afpSequencing;
    private String afpDnaMicroarray;
    private String afpOther;
    private String afpAntibodyDetectionDetails;
    private String afpAntigenDetectionDetails;
    private String afpRapidTestDetails;
    private String afpCultureDetails;
    private String afpHistopathologyDetails;
    private String afpIsolationDetails;
    private String afpIgmSerumAntibodyDetails;
    private String afpIggSerumAntibodyDetails;
    private String afpIgaSerumAntibodyDetails;
    private String afpIncubationTimeDetails;
    private String afpIndirectFluorescentAntibodyDetails;
    private String afpDirectFluorescentAntibodyDetails;
    private String afpMicroscopyDetails;
    private String afpNeutralizingAntibodiesDetails;
    private String afpPcrRtPcrDetails;
    private String afpGramStainDetails;
    private String afpLatexAgglutinationDetails;
    private String afpCqValueDetectionDetails;
    private String afpSequencingDetails;
    private String afpDnaMicroarrayDetails;
    private String afpOtherDetails;
    private String choleraAntibodyDetection;
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
    private String choleraDirectFluorescentAntibody;
    private String choleraMicroscopy;
    private String choleraNeutralizingAntibodies;
    private String choleraPcrRtPcr;
    private String choleraGramStain;
    private String choleraLatexAgglutination;
    private String choleraCqValueDetection;
    private String choleraSequencing;
    private String choleraDnaMicroarray;
    private String choleraOther;
    private String choleraAntibodyDetectionDetails;
    private String choleraAntigenDetectionDetails;
    private String choleraRapidTestDetails;
    private String choleraCultureDetails;
    private String choleraHistopathologyDetails;
    private String choleraIsolationDetails;
    private String choleraIgmSerumAntibodyDetails;
    private String choleraIggSerumAntibodyDetails;
    private String choleraIgaSerumAntibodyDetails;
    private String choleraIncubationTimeDetails;
    private String choleraIndirectFluorescentAntibodyDetails;
    private String choleraDirectFluorescentAntibodyDetails;
    private String choleraMicroscopyDetails;
    private String choleraNeutralizingAntibodiesDetails;
    private String choleraPcrRtPcrDetails;
    private String choleraGramStainDetails;
    private String choleraLatexAgglutinationDetails;
    private String choleraCqValueDetectionDetails;
    private String choleraSequencingDetails;
    private String choleraDnaMicroarrayDetails;
    private String choleraOtherDetails;
    private String congenitalRubellaAntibodyDetection;
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
    private String congenitalRubellaDirectFluorescentAntibody;
    private String congenitalRubellaMicroscopy;
    private String congenitalRubellaNeutralizingAntibodies;
    private String congenitalRubellaPcrRtPcr;
    private String congenitalRubellaGramStain;
    private String congenitalRubellaLatexAgglutination;
    private String congenitalRubellaCqValueDetection;
    private String congenitalRubellaSequencing;
    private String congenitalRubellaDnaMicroarray;
    private String congenitalRubellaOther;
    private String congenitalRubellaAntibodyDetectionDetails;
    private String congenitalRubellaAntigenDetectionDetails;
    private String congenitalRubellaRapidTestDetails;
    private String congenitalRubellaCultureDetails;
    private String congenitalRubellaHistopathologyDetails;
    private String congenitalRubellaIsolationDetails;
    private String congenitalRubellaIgmSerumAntibodyDetails;
    private String congenitalRubellaIggSerumAntibodyDetails;
    private String congenitalRubellaIgaSerumAntibodyDetails;
    private String congenitalRubellaIncubationTimeDetails;
    private String congenitalRubellaIndirectFluorescentAntibodyDetails;
    private String congenitalRubellaDirectFluorescentAntibodyDetails;
    private String congenitalRubellaMicroscopyDetails;
    private String congenitalRubellaNeutralizingAntibodiesDetails;
    private String congenitalRubellaPcrRtPcrDetails;
    private String congenitalRubellaGramStainDetails;
    private String congenitalRubellaLatexAgglutinationDetails;
    private String congenitalRubellaCqValueDetectionDetails;
    private String congenitalRubellaSequencingDetails;
    private String congenitalRubellaDnaMicroarrayDetails;
    private String congenitalRubellaOtherDetails;
    private String csmAntibodyDetection;
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
    private String csmDirectFluorescentAntibody;
    private String csmMicroscopy;
    private String csmNeutralizingAntibodies;
    private String csmPcrRtPcr;
    private String csmGramStain;
    private String csmLatexAgglutination;
    private String csmCqValueDetection;
    private String csmSequencing;
    private String csmDnaMicroarray;
    private String csmOther;
    private String csmAntibodyDetectionDetails;
    private String csmAntigenDetectionDetails;
    private String csmRapidTestDetails;
    private String csmCultureDetails;
    private String csmHistopathologyDetails;
    private String csmIsolationDetails;
    private String csmIgmSerumAntibodyDetails;
    private String csmIggSerumAntibodyDetails;
    private String csmIgaSerumAntibodyDetails;
    private String csmIncubationTimeDetails;
    private String csmIndirectFluorescentAntibodyDetails;
    private String csmDirectFluorescentAntibodyDetails;
    private String csmMicroscopyDetails;
    private String csmNeutralizingAntibodiesDetails;
    private String csmPcrRtPcrDetails;
    private String csmGramStainDetails;
    private String csmLatexAgglutinationDetails;
    private String csmCqValueDetectionDetails;
    private String csmSequencingDetails;
    private String csmDnaMicroarrayDetails;
    private String csmOtherDetails;
    private String dengueAntibodyDetection;
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
    private String dengueDirectFluorescentAntibody;
    private String dengueMicroscopy;
    private String dengueNeutralizingAntibodies;
    private String denguePcrRtPcr;
    private String dengueGramStain;
    private String dengueLatexAgglutination;
    private String dengueCqValueDetection;
    private String dengueSequencing;
    private String dengueDnaMicroarray;
    private String dengueOther;
    private String dengueAntibodyDetectionDetails;
    private String dengueAntigenDetectionDetails;
    private String dengueRapidTestDetails;
    private String dengueCultureDetails;
    private String dengueHistopathologyDetails;
    private String dengueIsolationDetails;
    private String dengueIgmSerumAntibodyDetails;
    private String dengueIggSerumAntibodyDetails;
    private String dengueIgaSerumAntibodyDetails;
    private String dengueIncubationTimeDetails;
    private String dengueIndirectFluorescentAntibodyDetails;
    private String dengueDirectFluorescentAntibodyDetails;
    private String dengueMicroscopyDetails;
    private String dengueNeutralizingAntibodiesDetails;
    private String denguePcrRtPcrDetails;
    private String dengueGramStainDetails;
    private String dengueLatexAgglutinationDetails;
    private String dengueCqValueDetectionDetails;
    private String dengueSequencingDetails;
    private String dengueDnaMicroarrayDetails;
    private String dengueOtherDetails;
    private String evdAntibodyDetection;
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
    private String evdDirectFluorescentAntibody;
    private String evdMicroscopy;
    private String evdNeutralizingAntibodies;
    private String evdPcrRtPcr;
    private String evdGramStain;
    private String evdLatexAgglutination;
    private String evdCqValueDetection;
    private String evdSequencing;
    private String evdDnaMicroarray;
    private String evdOther;
    private String evdAntibodyDetectionDetails;
    private String evdAntigenDetectionDetails;
    private String evdRapidTestDetails;
    private String evdCultureDetails;
    private String evdHistopathologyDetails;
    private String evdIsolationDetails;
    private String evdIgmSerumAntibodyDetails;
    private String evdIggSerumAntibodyDetails;
    private String evdIgaSerumAntibodyDetails;
    private String evdIncubationTimeDetails;
    private String evdIndirectFluorescentAntibodyDetails;
    private String evdDirectFluorescentAntibodyDetails;
    private String evdMicroscopyDetails;
    private String evdNeutralizingAntibodiesDetails;
    private String evdPcrRtPcrDetails;
    private String evdGramStainDetails;
    private String evdLatexAgglutinationDetails;
    private String evdCqValueDetectionDetails;
    private String evdSequencingDetails;
    private String evdDnaMicroarrayDetails;
    private String evdOtherDetails;
    private String guineaWormAntibodyDetection;
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
    private String guineaWormDirectFluorescentAntibody;
    private String guineaWormMicroscopy;
    private String guineaWormNeutralizingAntibodies;
    private String guineaWormPcrRtPcr;
    private String guineaWormGramStain;
    private String guineaWormLatexAgglutination;
    private String guineaWormCqValueDetection;
    private String guineaWormSequencing;
    private String guineaWormDnaMicroarray;
    private String guineaWormOther;
    private String guineaWormAntibodyDetectionDetails;
    private String guineaWormAntigenDetectionDetails;
    private String guineaWormRapidTestDetails;
    private String guineaWormCultureDetails;
    private String guineaWormHistopathologyDetails;
    private String guineaWormIsolationDetails;
    private String guineaWormIgmSerumAntibodyDetails;
    private String guineaWormIggSerumAntibodyDetails;
    private String guineaWormIgaSerumAntibodyDetails;
    private String guineaWormIncubationTimeDetails;
    private String guineaWormIndirectFluorescentAntibodyDetails;
    private String guineaWormDirectFluorescentAntibodyDetails;
    private String guineaWormMicroscopyDetails;
    private String guineaWormNeutralizingAntibodiesDetails;
    private String guineaWormPcrRtPcrDetails;
    private String guineaWormGramStainDetails;
    private String guineaWormLatexAgglutinationDetails;
    private String guineaWormCqValueDetectionDetails;
    private String guineaWormSequencingDetails;
    private String guineaWormDnaMicroarrayDetails;
    private String guineaWormOtherDetails;
    private String lassaAntibodyDetection;
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
    private String lassaDirectFluorescentAntibody;
    private String lassaMicroscopy;
    private String lassaNeutralizingAntibodies;
    private String lassaPcrRtPcr;
    private String lassaGramStain;
    private String lassaLatexAgglutination;
    private String lassaCqValueDetection;
    private String lassaSequencing;
    private String lassaDnaMicroarray;
    private String lassaOther;
    private String lassaAntibodyDetectionDetails;
    private String lassaAntigenDetectionDetails;
    private String lassaRapidTestDetails;
    private String lassaCultureDetails;
    private String lassaHistopathologyDetails;
    private String lassaIsolationDetails;
    private String lassaIgmSerumAntibodyDetails;
    private String lassaIggSerumAntibodyDetails;
    private String lassaIgaSerumAntibodyDetails;
    private String lassaIncubationTimeDetails;
    private String lassaIndirectFluorescentAntibodyDetails;
    private String lassaDirectFluorescentAntibodyDetails;
    private String lassaMicroscopyDetails;
    private String lassaNeutralizingAntibodiesDetails;
    private String lassaPcrRtPcrDetails;
    private String lassaGramStainDetails;
    private String lassaLatexAgglutinationDetails;
    private String lassaCqValueDetectionDetails;
    private String lassaSequencingDetails;
    private String lassaDnaMicroarrayDetails;
    private String lassaOtherDetails;
    private String measlesAntibodyDetection;
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
    private String measlesDirectFluorescentAntibody;
    private String measlesMicroscopy;
    private String measlesNeutralizingAntibodies;
    private String measlesPcrRtPcr;
    private String measlesGramStain;
    private String measlesLatexAgglutination;
    private String measlesCqValueDetection;
    private String measlesSequencing;
    private String measlesDnaMicroarray;
    private String measlesOther;
    private String measlesAntibodyDetectionDetails;
    private String measlesAntigenDetectionDetails;
    private String measlesRapidTestDetails;
    private String measlesCultureDetails;
    private String measlesHistopathologyDetails;
    private String measlesIsolationDetails;
    private String measlesIgmSerumAntibodyDetails;
    private String measlesIggSerumAntibodyDetails;
    private String measlesIgaSerumAntibodyDetails;
    private String measlesIncubationTimeDetails;
    private String measlesIndirectFluorescentAntibodyDetails;
    private String measlesDirectFluorescentAntibodyDetails;
    private String measlesMicroscopyDetails;
    private String measlesNeutralizingAntibodiesDetails;
    private String measlesPcrRtPcrDetails;
    private String measlesGramStainDetails;
    private String measlesLatexAgglutinationDetails;
    private String measlesCqValueDetectionDetails;
    private String measlesSequencingDetails;
    private String measlesDnaMicroarrayDetails;
    private String measlesOtherDetails;
    private String monkeypoxAntibodyDetection;
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
    private String monkeypoxDirectFluorescentAntibody;
    private String monkeypoxMicroscopy;
    private String monkeypoxNeutralizingAntibodies;
    private String monkeypoxPcrRtPcr;
    private String monkeypoxGramStain;
    private String monkeypoxLatexAgglutination;
    private String monkeypoxCqValueDetection;
    private String monkeypoxSequencing;
    private String monkeypoxDnaMicroarray;
    private String monkeypoxOther;
    private String monkeypoxAntibodyDetectionDetails;
    private String monkeypoxAntigenDetectionDetails;
    private String monkeypoxRapidTestDetails;
    private String monkeypoxCultureDetails;
    private String monkeypoxHistopathologyDetails;
    private String monkeypoxIsolationDetails;
    private String monkeypoxIgmSerumAntibodyDetails;
    private String monkeypoxIggSerumAntibodyDetails;
    private String monkeypoxIgaSerumAntibodyDetails;
    private String monkeypoxIncubationTimeDetails;
    private String monkeypoxIndirectFluorescentAntibodyDetails;
    private String monkeypoxDirectFluorescentAntibodyDetails;
    private String monkeypoxMicroscopyDetails;
    private String monkeypoxNeutralizingAntibodiesDetails;
    private String monkeypoxPcrRtPcrDetails;
    private String monkeypoxGramStainDetails;
    private String monkeypoxLatexAgglutinationDetails;
    private String monkeypoxCqValueDetectionDetails;
    private String monkeypoxSequencingDetails;
    private String monkeypoxDnaMicroarrayDetails;
    private String monkeypoxOtherDetails;
    private String newInfluenzaAntibodyDetection;
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
    private String newInfluenzaDirectFluorescentAntibody;
    private String newInfluenzaMicroscopy;
    private String newInfluenzaNeutralizingAntibodies;
    private String newInfluenzaPcrRtPcr;
    private String newInfluenzaGramStain;
    private String newInfluenzaLatexAgglutination;
    private String newInfluenzaCqValueDetection;
    private String newInfluenzaSequencing;
    private String newInfluenzaDnaMicroarray;
    private String newInfluenzaOther;
    private String newInfluenzaAntibodyDetectionDetails;
    private String newInfluenzaAntigenDetectionDetails;
    private String newInfluenzaRapidTestDetails;
    private String newInfluenzaCultureDetails;
    private String newInfluenzaHistopathologyDetails;
    private String newInfluenzaIsolationDetails;
    private String newInfluenzaIgmSerumAntibodyDetails;
    private String newInfluenzaIggSerumAntibodyDetails;
    private String newInfluenzaIgaSerumAntibodyDetails;
    private String newInfluenzaIncubationTimeDetails;
    private String newInfluenzaIndirectFluorescentAntibodyDetails;
    private String newInfluenzaDirectFluorescentAntibodyDetails;
    private String newInfluenzaMicroscopyDetails;
    private String newInfluenzaNeutralizingAntibodiesDetails;
    private String newInfluenzaPcrRtPcrDetails;
    private String newInfluenzaGramStainDetails;
    private String newInfluenzaLatexAgglutinationDetails;
    private String newInfluenzaCqValueDetectionDetails;
    private String newInfluenzaSequencingDetails;
    private String newInfluenzaDnaMicroarrayDetails;
    private String newInfluenzaOtherDetails;
    private String plagueAntibodyDetection;
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
    private String plagueDirectFluorescentAntibody;
    private String plagueMicroscopy;
    private String plagueNeutralizingAntibodies;
    private String plaguePcrRtPcr;
    private String plagueGramStain;
    private String plagueLatexAgglutination;
    private String plagueCqValueDetection;
    private String plagueSequencing;
    private String plagueDnaMicroarray;
    private String plagueOther;
    private String plagueAntibodyDetectionDetails;
    private String plagueAntigenDetectionDetails;
    private String plagueRapidTestDetails;
    private String plagueCultureDetails;
    private String plagueHistopathologyDetails;
    private String plagueIsolationDetails;
    private String plagueIgmSerumAntibodyDetails;
    private String plagueIggSerumAntibodyDetails;
    private String plagueIgaSerumAntibodyDetails;
    private String plagueIncubationTimeDetails;
    private String plagueIndirectFluorescentAntibodyDetails;
    private String plagueDirectFluorescentAntibodyDetails;
    private String plagueMicroscopyDetails;
    private String plagueNeutralizingAntibodiesDetails;
    private String plaguePcrRtPcrDetails;
    private String plagueGramStainDetails;
    private String plagueLatexAgglutinationDetails;
    private String plagueCqValueDetectionDetails;
    private String plagueSequencingDetails;
    private String plagueDnaMicroarrayDetails;
    private String plagueOtherDetails;
    private String polioAntibodyDetection;
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
    private String polioDirectFluorescentAntibody;
    private String polioMicroscopy;
    private String polioNeutralizingAntibodies;
    private String polioPcrRtPcr;
    private String polioGramStain;
    private String polioLatexAgglutination;
    private String polioCqValueDetection;
    private String polioSequencing;
    private String polioDnaMicroarray;
    private String polioOther;
    private String polioAntibodyDetectionDetails;
    private String polioAntigenDetectionDetails;
    private String polioRapidTestDetails;
    private String polioCultureDetails;
    private String polioHistopathologyDetails;
    private String polioIsolationDetails;
    private String polioIgmSerumAntibodyDetails;
    private String polioIggSerumAntibodyDetails;
    private String polioIgaSerumAntibodyDetails;
    private String polioIncubationTimeDetails;
    private String polioIndirectFluorescentAntibodyDetails;
    private String polioDirectFluorescentAntibodyDetails;
    private String polioMicroscopyDetails;
    private String polioNeutralizingAntibodiesDetails;
    private String polioPcrRtPcrDetails;
    private String polioGramStainDetails;
    private String polioLatexAgglutinationDetails;
    private String polioCqValueDetectionDetails;
    private String polioSequencingDetails;
    private String polioDnaMicroarrayDetails;
    private String polioOtherDetails;
    private String unspecifiedVhfAntibodyDetection;
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
    private String unspecifiedVhfDirectFluorescentAntibody;
    private String unspecifiedVhfMicroscopy;
    private String unspecifiedVhfNeutralizingAntibodies;
    private String unspecifiedVhfPcrRtPcr;
    private String unspecifiedVhfGramStain;
    private String unspecifiedVhfLatexAgglutination;
    private String unspecifiedVhfCqValueDetection;
    private String unspecifiedVhfSequencing;
    private String unspecifiedVhfDnaMicroarray;
    private String unspecifiedVhfOther;
    private String unspecifiedVhfAntibodyDetectionDetails;
    private String unspecifiedVhfAntigenDetectionDetails;
    private String unspecifiedVhfRapidTestDetails;
    private String unspecifiedVhfCultureDetails;
    private String unspecifiedVhfHistopathologyDetails;
    private String unspecifiedVhfIsolationDetails;
    private String unspecifiedVhfIgmSerumAntibodyDetails;
    private String unspecifiedVhfIggSerumAntibodyDetails;
    private String unspecifiedVhfIgaSerumAntibodyDetails;
    private String unspecifiedVhfIncubationTimeDetails;
    private String unspecifiedVhfIndirectFluorescentAntibodyDetails;
    private String unspecifiedVhfDirectFluorescentAntibodyDetails;
    private String unspecifiedVhfMicroscopyDetails;
    private String unspecifiedVhfNeutralizingAntibodiesDetails;
    private String unspecifiedVhfPcrRtPcrDetails;
    private String unspecifiedVhfGramStainDetails;
    private String unspecifiedVhfLatexAgglutinationDetails;
    private String unspecifiedVhfCqValueDetectionDetails;
    private String unspecifiedVhfSequencingDetails;
    private String unspecifiedVhfDnaMicroarrayDetails;
    private String unspecifiedVhfOtherDetails;
    private String westNileFeverAntibodyDetection;
    private String westNileFeverAntigenDetection;
    private String westNileFeverRapidTest;
    private String westNileFeverCulture;
    private String westNileFeverHistopathology;
    private String westNileFeverIsolation;
    private String westNileFeverIgmSerumAntibody;
    private String westNileFeverIggSerumAntibody;
    private String westNileFeverIgaSerumAntibody;
    private String westNileFeverIncubationTime;
    private String westNileFeverIndirectFluorescentAntibody;
    private String westNileFeverDirectFluorescentAntibody;
    private String westNileFeverMicroscopy;
    private String westNileFeverNeutralizingAntibodies;
    private String westNileFeverPcrRtPcr;
    private String westNileFeverGramStain;
    private String westNileFeverLatexAgglutination;
    private String westNileFeverCqValueDetection;
    private String westNileFeverSequencing;
    private String westNileFeverDnaMicroarray;
    private String westNileFeverOther;
    private String westNileFeverAntibodyDetectionDetails;
    private String westNileFeverAntigenDetectionDetails;
    private String westNileFeverRapidTestDetails;
    private String westNileFeverCultureDetails;
    private String westNileFeverHistopathologyDetails;
    private String westNileFeverIsolationDetails;
    private String westNileFeverIgmSerumAntibodyDetails;
    private String westNileFeverIggSerumAntibodyDetails;
    private String westNileFeverIgaSerumAntibodyDetails;
    private String westNileFeverIncubationTimeDetails;
    private String westNileFeverIndirectFluorescentAntibodyDetails;
    private String westNileFeverDirectFluorescentAntibodyDetails;
    private String westNileFeverMicroscopyDetails;
    private String westNileFeverNeutralizingAntibodiesDetails;
    private String westNileFeverPcrRtPcrDetails;
    private String westNileFeverGramStainDetails;
    private String westNileFeverLatexAgglutinationDetails;
    private String westNileFeverCqValueDetectionDetails;
    private String westNileFeverSequencingDetails;
    private String westNileFeverDnaMicroarrayDetails;
    private String westNileFeverOtherDetails;
    private String yellowFeverAntibodyDetection;
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
    private String yellowFeverDirectFluorescentAntibody;
    private String yellowFeverMicroscopy;
    private String yellowFeverNeutralizingAntibodies;
    private String yellowFeverPcrRtPcr;
    private String yellowFeverGramStain;
    private String yellowFeverLatexAgglutination;
    private String yellowFeverCqValueDetection;
    private String yellowFeverSequencing;
    private String yellowFeverDnaMicroarray;
    private String yellowFeverOther;
    private String yellowFeverAntibodyDetectionDetails;
    private String yellowFeverAntigenDetectionDetails;
    private String yellowFeverRapidTestDetails;
    private String yellowFeverCultureDetails;
    private String yellowFeverHistopathologyDetails;
    private String yellowFeverIsolationDetails;
    private String yellowFeverIgmSerumAntibodyDetails;
    private String yellowFeverIggSerumAntibodyDetails;
    private String yellowFeverIgaSerumAntibodyDetails;
    private String yellowFeverIncubationTimeDetails;
    private String yellowFeverIndirectFluorescentAntibodyDetails;
    private String yellowFeverDirectFluorescentAntibodyDetails;
    private String yellowFeverMicroscopyDetails;
    private String yellowFeverNeutralizingAntibodiesDetails;
    private String yellowFeverPcrRtPcrDetails;
    private String yellowFeverGramStainDetails;
    private String yellowFeverLatexAgglutinationDetails;
    private String yellowFeverCqValueDetectionDetails;
    private String yellowFeverSequencingDetails;
    private String yellowFeverDnaMicroarrayDetails;
    private String yellowFeverOtherDetails;
    private String rabiesAntibodyDetection;
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
    private String rabiesDirectFluorescentAntibody;
    private String rabiesMicroscopy;
    private String rabiesNeutralizingAntibodies;
    private String rabiesPcrRtPcr;
    private String rabiesGramStain;
    private String rabiesLatexAgglutination;
    private String rabiesCqValueDetection;
    private String rabiesSequencing;
    private String rabiesDnaMicroarray;
    private String rabiesOther;
    private String rabiesAntibodyDetectionDetails;
    private String rabiesAntigenDetectionDetails;
    private String rabiesRapidTestDetails;
    private String rabiesCultureDetails;
    private String rabiesHistopathologyDetails;
    private String rabiesIsolationDetails;
    private String rabiesIgmSerumAntibodyDetails;
    private String rabiesIggSerumAntibodyDetails;
    private String rabiesIgaSerumAntibodyDetails;
    private String rabiesIncubationTimeDetails;
    private String rabiesIndirectFluorescentAntibodyDetails;
    private String rabiesDirectFluorescentAntibodyDetails;
    private String rabiesMicroscopyDetails;
    private String rabiesNeutralizingAntibodiesDetails;
    private String rabiesPcrRtPcrDetails;
    private String rabiesGramStainDetails;
    private String rabiesLatexAgglutinationDetails;
    private String rabiesCqValueDetectionDetails;
    private String rabiesSequencingDetails;
    private String rabiesDnaMicroarrayDetails;
    private String rabiesOtherDetails;
    private String anthraxAntibodyDetection;
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
    private String anthraxDirectFluorescentAntibody;
    private String anthraxMicroscopy;
    private String anthraxNeutralizingAntibodies;
    private String anthraxPcrRtPcr;
    private String anthraxGramStain;
    private String anthraxLatexAgglutination;
    private String anthraxCqValueDetection;
    private String anthraxSequencing;
    private String anthraxDnaMicroarray;
    private String anthraxOther;
    private String anthraxAntibodyDetectionDetails;
    private String anthraxAntigenDetectionDetails;
    private String anthraxRapidTestDetails;
    private String anthraxCultureDetails;
    private String anthraxHistopathologyDetails;
    private String anthraxIsolationDetails;
    private String anthraxIgmSerumAntibodyDetails;
    private String anthraxIggSerumAntibodyDetails;
    private String anthraxIgaSerumAntibodyDetails;
    private String anthraxIncubationTimeDetails;
    private String anthraxIndirectFluorescentAntibodyDetails;
    private String anthraxDirectFluorescentAntibodyDetails;
    private String anthraxMicroscopyDetails;
    private String anthraxNeutralizingAntibodiesDetails;
    private String anthraxPcrRtPcrDetails;
    private String anthraxGramStainDetails;
    private String anthraxLatexAgglutinationDetails;
    private String anthraxCqValueDetectionDetails;
    private String anthraxSequencingDetails;
    private String anthraxDnaMicroarrayDetails;
    private String anthraxOtherDetails;
    private String coronavirusAntibodyDetection;
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
    private String coronavirusDirectFluorescentAntibody;
    private String coronavirusMicroscopy;
    private String coronavirusNeutralizingAntibodies;
    private String coronavirusPcrRtPcr;
    private String coronavirusGramStain;
    private String coronavirusLatexAgglutination;
    private String coronavirusCqValueDetection;
    private String coronavirusSequencing;
    private String coronavirusDnaMicroarray;
    private String coronavirusOther;
    private String coronavirusAntibodyDetectionDetails;
    private String coronavirusAntigenDetectionDetails;
    private String coronavirusRapidTestDetails;
    private String coronavirusCultureDetails;
    private String coronavirusHistopathologyDetails;
    private String coronavirusIsolationDetails;
    private String coronavirusIgmSerumAntibodyDetails;
    private String coronavirusIggSerumAntibodyDetails;
    private String coronavirusIgaSerumAntibodyDetails;
    private String coronavirusIncubationTimeDetails;
    private String coronavirusIndirectFluorescentAntibodyDetails;
    private String coronavirusDirectFluorescentAntibodyDetails;
    private String coronavirusMicroscopyDetails;
    private String coronavirusNeutralizingAntibodiesDetails;
    private String coronavirusPcrRtPcrDetails;
    private String coronavirusGramStainDetails;
    private String coronavirusLatexAgglutinationDetails;
    private String coronavirusCqValueDetectionDetails;
    private String coronavirusSequencingDetails;
    private String coronavirusDnaMicroarrayDetails;
    private String coronavirusOtherDetails;
    private String pneumoniaAntibodyDetection;
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
    private String pneumoniaDirectFluorescentAntibody;
    private String pneumoniaMicroscopy;
    private String pneumoniaNeutralizingAntibodies;
    private String pneumoniaPcrRtPcr;
    private String pneumoniaGramStain;
    private String pneumoniaLatexAgglutination;
    private String pneumoniaCqValueDetection;
    private String pneumoniaSequencing;
    private String pneumoniaDnaMicroarray;
    private String pneumoniaOther;
    private String pneumoniaAntibodyDetectionDetails;
    private String pneumoniaAntigenDetectionDetails;
    private String pneumoniaRapidTestDetails;
    private String pneumoniaCultureDetails;
    private String pneumoniaHistopathologyDetails;
    private String pneumoniaIsolationDetails;
    private String pneumoniaIgmSerumAntibodyDetails;
    private String pneumoniaIggSerumAntibodyDetails;
    private String pneumoniaIgaSerumAntibodyDetails;
    private String pneumoniaIncubationTimeDetails;
    private String pneumoniaIndirectFluorescentAntibodyDetails;
    private String pneumoniaDirectFluorescentAntibodyDetails;
    private String pneumoniaMicroscopyDetails;
    private String pneumoniaNeutralizingAntibodiesDetails;
    private String pneumoniaPcrRtPcrDetails;
    private String pneumoniaGramStainDetails;
    private String pneumoniaLatexAgglutinationDetails;
    private String pneumoniaCqValueDetectionDetails;
    private String pneumoniaSequencingDetails;
    private String pneumoniaDnaMicroarrayDetails;
    private String pneumoniaOtherDetails;
    private String malariaAntibodyDetection;
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
    private String malariaDirectFluorescentAntibody;
    private String malariaMicroscopy;
    private String malariaNeutralizingAntibodies;
    private String malariaPcrRtPcr;
    private String malariaGramStain;
    private String malariaLatexAgglutination;
    private String malariaCqValueDetection;
    private String malariaSequencing;
    private String malariaDnaMicroarray;
    private String malariaOther;
    private String malariaAntibodyDetectionDetails;
    private String malariaAntigenDetectionDetails;
    private String malariaRapidTestDetails;
    private String malariaCultureDetails;
    private String malariaHistopathologyDetails;
    private String malariaIsolationDetails;
    private String malariaIgmSerumAntibodyDetails;
    private String malariaIggSerumAntibodyDetails;
    private String malariaIgaSerumAntibodyDetails;
    private String malariaIncubationTimeDetails;
    private String malariaIndirectFluorescentAntibodyDetails;
    private String malariaDirectFluorescentAntibodyDetails;
    private String malariaMicroscopyDetails;
    private String malariaNeutralizingAntibodiesDetails;
    private String malariaPcrRtPcrDetails;
    private String malariaGramStainDetails;
    private String malariaLatexAgglutinationDetails;
    private String malariaCqValueDetectionDetails;
    private String malariaSequencingDetails;
    private String malariaDnaMicroarrayDetails;
    private String malariaOtherDetails;
    private String typhoidFeverAntibodyDetection;
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
    private String typhoidFeverDirectFluorescentAntibody;
    private String typhoidFeverMicroscopy;
    private String typhoidFeverNeutralizingAntibodies;
    private String typhoidFeverPcrRtPcr;
    private String typhoidFeverGramStain;
    private String typhoidFeverLatexAgglutination;
    private String typhoidFeverCqValueDetection;
    private String typhoidFeverSequencing;
    private String typhoidFeverDnaMicroarray;
    private String typhoidFeverOther;
    private String typhoidFeverAntibodyDetectionDetails;
    private String typhoidFeverAntigenDetectionDetails;
    private String typhoidFeverRapidTestDetails;
    private String typhoidFeverCultureDetails;
    private String typhoidFeverHistopathologyDetails;
    private String typhoidFeverIsolationDetails;
    private String typhoidFeverIgmSerumAntibodyDetails;
    private String typhoidFeverIggSerumAntibodyDetails;
    private String typhoidFeverIgaSerumAntibodyDetails;
    private String typhoidFeverIncubationTimeDetails;
    private String typhoidFeverIndirectFluorescentAntibodyDetails;
    private String typhoidFeverDirectFluorescentAntibodyDetails;
    private String typhoidFeverMicroscopyDetails;
    private String typhoidFeverNeutralizingAntibodiesDetails;
    private String typhoidFeverPcrRtPcrDetails;
    private String typhoidFeverGramStainDetails;
    private String typhoidFeverLatexAgglutinationDetails;
    private String typhoidFeverCqValueDetectionDetails;
    private String typhoidFeverSequencingDetails;
    private String typhoidFeverDnaMicroarrayDetails;
    private String typhoidFeverOtherDetails;
    private String acuteViralHepatitisAntibodyDetection;
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
    private String acuteViralHepatitisDirectFluorescentAntibody;
    private String acuteViralHepatitisMicroscopy;
    private String acuteViralHepatitisNeutralizingAntibodies;
    private String acuteViralHepatitisPcrRtPcr;
    private String acuteViralHepatitisGramStain;
    private String acuteViralHepatitisLatexAgglutination;
    private String acuteViralHepatitisCqValueDetection;
    private String acuteViralHepatitisSequencing;
    private String acuteViralHepatitisDnaMicroarray;
    private String acuteViralHepatitisOther;
    private String acuteViralHepatitisAntibodyDetectionDetails;
    private String acuteViralHepatitisAntigenDetectionDetails;
    private String acuteViralHepatitisRapidTestDetails;
    private String acuteViralHepatitisCultureDetails;
    private String acuteViralHepatitisHistopathologyDetails;
    private String acuteViralHepatitisIsolationDetails;
    private String acuteViralHepatitisIgmSerumAntibodyDetails;
    private String acuteViralHepatitisIggSerumAntibodyDetails;
    private String acuteViralHepatitisIgaSerumAntibodyDetails;
    private String acuteViralHepatitisIncubationTimeDetails;
    private String acuteViralHepatitisIndirectFluorescentAntibodyDetails;
    private String acuteViralHepatitisDirectFluorescentAntibodyDetails;
    private String acuteViralHepatitisMicroscopyDetails;
    private String acuteViralHepatitisNeutralizingAntibodiesDetails;
    private String acuteViralHepatitisPcrRtPcrDetails;
    private String acuteViralHepatitisGramStainDetails;
    private String acuteViralHepatitisLatexAgglutinationDetails;
    private String acuteViralHepatitisCqValueDetectionDetails;
    private String acuteViralHepatitisSequencingDetails;
    private String acuteViralHepatitisDnaMicroarrayDetails;
    private String acuteViralHepatitisOtherDetails;
    private String nonNeonatalTetanusAntibodyDetection;
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
    private String nonNeonatalTetanusDirectFluorescentAntibody;
    private String nonNeonatalTetanusMicroscopy;
    private String nonNeonatalTetanusNeutralizingAntibodies;
    private String nonNeonatalTetanusPcrRtPcr;
    private String nonNeonatalTetanusGramStain;
    private String nonNeonatalTetanusLatexAgglutination;
    private String nonNeonatalTetanusCqValueDetection;
    private String nonNeonatalTetanusSequencing;
    private String nonNeonatalTetanusDnaMicroarray;
    private String nonNeonatalTetanusOther;
    private String nonNeonatalTetanusAntibodyDetectionDetails;
    private String nonNeonatalTetanusAntigenDetectionDetails;
    private String nonNeonatalTetanusRapidTestDetails;
    private String nonNeonatalTetanusCultureDetails;
    private String nonNeonatalTetanusHistopathologyDetails;
    private String nonNeonatalTetanusIsolationDetails;
    private String nonNeonatalTetanusIgmSerumAntibodyDetails;
    private String nonNeonatalTetanusIggSerumAntibodyDetails;
    private String nonNeonatalTetanusIgaSerumAntibodyDetails;
    private String nonNeonatalTetanusIncubationTimeDetails;
    private String nonNeonatalTetanusIndirectFluorescentAntibodyDetails;
    private String nonNeonatalTetanusDirectFluorescentAntibodyDetails;
    private String nonNeonatalTetanusMicroscopyDetails;
    private String nonNeonatalTetanusNeutralizingAntibodiesDetails;
    private String nonNeonatalTetanusPcrRtPcrDetails;
    private String nonNeonatalTetanusGramStainDetails;
    private String nonNeonatalTetanusLatexAgglutinationDetails;
    private String nonNeonatalTetanusCqValueDetectionDetails;
    private String nonNeonatalTetanusSequencingDetails;
    private String nonNeonatalTetanusDnaMicroarrayDetails;
    private String nonNeonatalTetanusOtherDetails;
    private String hivAntibodyDetection;
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
    private String hivDirectFluorescentAntibody;
    private String hivMicroscopy;
    private String hivNeutralizingAntibodies;
    private String hivPcrRtPcr;
    private String hivGramStain;
    private String hivLatexAgglutination;
    private String hivCqValueDetection;
    private String hivSequencing;
    private String hivDnaMicroarray;
    private String hivOther;
    private String hivAntibodyDetectionDetails;
    private String hivAntigenDetectionDetails;
    private String hivRapidTestDetails;
    private String hivCultureDetails;
    private String hivHistopathologyDetails;
    private String hivIsolationDetails;
    private String hivIgmSerumAntibodyDetails;
    private String hivIggSerumAntibodyDetails;
    private String hivIgaSerumAntibodyDetails;
    private String hivIncubationTimeDetails;
    private String hivIndirectFluorescentAntibodyDetails;
    private String hivDirectFluorescentAntibodyDetails;
    private String hivMicroscopyDetails;
    private String hivNeutralizingAntibodiesDetails;
    private String hivPcrRtPcrDetails;
    private String hivGramStainDetails;
    private String hivLatexAgglutinationDetails;
    private String hivCqValueDetectionDetails;
    private String hivSequencingDetails;
    private String hivDnaMicroarrayDetails;
    private String hivOtherDetails;
    private String schistosomiasisAntibodyDetection;
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
    private String schistosomiasisDirectFluorescentAntibody;
    private String schistosomiasisMicroscopy;
    private String schistosomiasisNeutralizingAntibodies;
    private String schistosomiasisPcrRtPcr;
    private String schistosomiasisGramStain;
    private String schistosomiasisLatexAgglutination;
    private String schistosomiasisCqValueDetection;
    private String schistosomiasisSequencing;
    private String schistosomiasisDnaMicroarray;
    private String schistosomiasisOther;
    private String schistosomiasisAntibodyDetectionDetails;
    private String schistosomiasisAntigenDetectionDetails;
    private String schistosomiasisRapidTestDetails;
    private String schistosomiasisCultureDetails;
    private String schistosomiasisHistopathologyDetails;
    private String schistosomiasisIsolationDetails;
    private String schistosomiasisIgmSerumAntibodyDetails;
    private String schistosomiasisIggSerumAntibodyDetails;
    private String schistosomiasisIgaSerumAntibodyDetails;
    private String schistosomiasisIncubationTimeDetails;
    private String schistosomiasisIndirectFluorescentAntibodyDetails;
    private String schistosomiasisDirectFluorescentAntibodyDetails;
    private String schistosomiasisMicroscopyDetails;
    private String schistosomiasisNeutralizingAntibodiesDetails;
    private String schistosomiasisPcrRtPcrDetails;
    private String schistosomiasisGramStainDetails;
    private String schistosomiasisLatexAgglutinationDetails;
    private String schistosomiasisCqValueDetectionDetails;
    private String schistosomiasisSequencingDetails;
    private String schistosomiasisDnaMicroarrayDetails;
    private String schistosomiasisOtherDetails;
    private String soilTransmittedHelminthsAntibodyDetection;
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
    private String soilTransmittedHelminthsDirectFluorescentAntibody;
    private String soilTransmittedHelminthsMicroscopy;
    private String soilTransmittedHelminthsNeutralizingAntibodies;
    private String soilTransmittedHelminthsPcrRtPcr;
    private String soilTransmittedHelminthsGramStain;
    private String soilTransmittedHelminthsLatexAgglutination;
    private String soilTransmittedHelminthsCqValueDetection;
    private String soilTransmittedHelminthsSequencing;
    private String soilTransmittedHelminthsDnaMicroarray;
    private String soilTransmittedHelminthsOther;
    private String soilTransmittedHelminthsAntibodyDetectionDetails;
    private String soilTransmittedHelminthsAntigenDetectionDetails;
    private String soilTransmittedHelminthsRapidTestDetails;
    private String soilTransmittedHelminthsCultureDetails;
    private String soilTransmittedHelminthsHistopathologyDetails;
    private String soilTransmittedHelminthsIsolationDetails;
    private String soilTransmittedHelminthsIgmSerumAntibodyDetails;
    private String soilTransmittedHelminthsIggSerumAntibodyDetails;
    private String soilTransmittedHelminthsIgaSerumAntibodyDetails;
    private String soilTransmittedHelminthsIncubationTimeDetails;
    private String soilTransmittedHelminthsIndirectFluorescentAntibodyDetails;
    private String soilTransmittedHelminthsDirectFluorescentAntibodyDetails;
    private String soilTransmittedHelminthsMicroscopyDetails;
    private String soilTransmittedHelminthsNeutralizingAntibodiesDetails;
    private String soilTransmittedHelminthsPcrRtPcrDetails;
    private String soilTransmittedHelminthsGramStainDetails;
    private String soilTransmittedHelminthsLatexAgglutinationDetails;
    private String soilTransmittedHelminthsCqValueDetectionDetails;
    private String soilTransmittedHelminthsSequencingDetails;
    private String soilTransmittedHelminthsDnaMicroarrayDetails;
    private String soilTransmittedHelminthsOtherDetails;
    private String trypanosomiasisAntibodyDetection;
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
    private String trypanosomiasisDirectFluorescentAntibody;
    private String trypanosomiasisMicroscopy;
    private String trypanosomiasisNeutralizingAntibodies;
    private String trypanosomiasisPcrRtPcr;
    private String trypanosomiasisGramStain;
    private String trypanosomiasisLatexAgglutination;
    private String trypanosomiasisCqValueDetection;
    private String trypanosomiasisSequencing;
    private String trypanosomiasisDnaMicroarray;
    private String trypanosomiasisOther;
    private String trypanosomiasisAntibodyDetectionDetails;
    private String trypanosomiasisAntigenDetectionDetails;
    private String trypanosomiasisRapidTestDetails;
    private String trypanosomiasisCultureDetails;
    private String trypanosomiasisHistopathologyDetails;
    private String trypanosomiasisIsolationDetails;
    private String trypanosomiasisIgmSerumAntibodyDetails;
    private String trypanosomiasisIggSerumAntibodyDetails;
    private String trypanosomiasisIgaSerumAntibodyDetails;
    private String trypanosomiasisIncubationTimeDetails;
    private String trypanosomiasisIndirectFluorescentAntibodyDetails;
    private String trypanosomiasisDirectFluorescentAntibodyDetails;
    private String trypanosomiasisMicroscopyDetails;
    private String trypanosomiasisNeutralizingAntibodiesDetails;
    private String trypanosomiasisPcrRtPcrDetails;
    private String trypanosomiasisGramStainDetails;
    private String trypanosomiasisLatexAgglutinationDetails;
    private String trypanosomiasisCqValueDetectionDetails;
    private String trypanosomiasisSequencingDetails;
    private String trypanosomiasisDnaMicroarrayDetails;
    private String trypanosomiasisOtherDetails;
    private String diarrheaDehydrationAntibodyDetection;
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
    private String diarrheaDehydrationDirectFluorescentAntibody;
    private String diarrheaDehydrationMicroscopy;
    private String diarrheaDehydrationNeutralizingAntibodies;
    private String diarrheaDehydrationPcrRtPcr;
    private String diarrheaDehydrationGramStain;
    private String diarrheaDehydrationLatexAgglutination;
    private String diarrheaDehydrationCqValueDetection;
    private String diarrheaDehydrationSequencing;
    private String diarrheaDehydrationDnaMicroarray;
    private String diarrheaDehydrationOther;
    private String diarrheaDehydrationAntibodyDetectionDetails;
    private String diarrheaDehydrationAntigenDetectionDetails;
    private String diarrheaDehydrationRapidTestDetails;
    private String diarrheaDehydrationCultureDetails;
    private String diarrheaDehydrationHistopathologyDetails;
    private String diarrheaDehydrationIsolationDetails;
    private String diarrheaDehydrationIgmSerumAntibodyDetails;
    private String diarrheaDehydrationIggSerumAntibodyDetails;
    private String diarrheaDehydrationIgaSerumAntibodyDetails;
    private String diarrheaDehydrationIncubationTimeDetails;
    private String diarrheaDehydrationIndirectFluorescentAntibodyDetails;
    private String diarrheaDehydrationDirectFluorescentAntibodyDetails;
    private String diarrheaDehydrationMicroscopyDetails;
    private String diarrheaDehydrationNeutralizingAntibodiesDetails;
    private String diarrheaDehydrationPcrRtPcrDetails;
    private String diarrheaDehydrationGramStainDetails;
    private String diarrheaDehydrationLatexAgglutinationDetails;
    private String diarrheaDehydrationCqValueDetectionDetails;
    private String diarrheaDehydrationSequencingDetails;
    private String diarrheaDehydrationDnaMicroarrayDetails;
    private String diarrheaDehydrationOtherDetails;
    private String diarrheaBloodAntibodyDetection;
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
    private String diarrheaBloodDirectFluorescentAntibody;
    private String diarrheaBloodMicroscopy;
    private String diarrheaBloodNeutralizingAntibodies;
    private String diarrheaBloodPcrRtPcr;
    private String diarrheaBloodGramStain;
    private String diarrheaBloodLatexAgglutination;
    private String diarrheaBloodCqValueDetection;
    private String diarrheaBloodSequencing;
    private String diarrheaBloodDnaMicroarray;
    private String diarrheaBloodOther;
    private String diarrheaBloodAntibodyDetectionDetails;
    private String diarrheaBloodAntigenDetectionDetails;
    private String diarrheaBloodRapidTestDetails;
    private String diarrheaBloodCultureDetails;
    private String diarrheaBloodHistopathologyDetails;
    private String diarrheaBloodIsolationDetails;
    private String diarrheaBloodIgmSerumAntibodyDetails;
    private String diarrheaBloodIggSerumAntibodyDetails;
    private String diarrheaBloodIgaSerumAntibodyDetails;
    private String diarrheaBloodIncubationTimeDetails;
    private String diarrheaBloodIndirectFluorescentAntibodyDetails;
    private String diarrheaBloodDirectFluorescentAntibodyDetails;
    private String diarrheaBloodMicroscopyDetails;
    private String diarrheaBloodNeutralizingAntibodiesDetails;
    private String diarrheaBloodPcrRtPcrDetails;
    private String diarrheaBloodGramStainDetails;
    private String diarrheaBloodLatexAgglutinationDetails;
    private String diarrheaBloodCqValueDetectionDetails;
    private String diarrheaBloodSequencingDetails;
    private String diarrheaBloodDnaMicroarrayDetails;
    private String diarrheaBloodOtherDetails;
    private String snakeBiteAntibodyDetection;
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
    private String snakeBiteDirectFluorescentAntibody;
    private String snakeBiteMicroscopy;
    private String snakeBiteNeutralizingAntibodies;
    private String snakeBitePcrRtPcr;
    private String snakeBiteGramStain;
    private String snakeBiteLatexAgglutination;
    private String snakeBiteCqValueDetection;
    private String snakeBiteSequencing;
    private String snakeBiteDnaMicroarray;
    private String snakeBiteOther;
    private String snakeBiteAntibodyDetectionDetails;
    private String snakeBiteAntigenDetectionDetails;
    private String snakeBiteRapidTestDetails;
    private String snakeBiteCultureDetails;
    private String snakeBiteHistopathologyDetails;
    private String snakeBiteIsolationDetails;
    private String snakeBiteIgmSerumAntibodyDetails;
    private String snakeBiteIggSerumAntibodyDetails;
    private String snakeBiteIgaSerumAntibodyDetails;
    private String snakeBiteIncubationTimeDetails;
    private String snakeBiteIndirectFluorescentAntibodyDetails;
    private String snakeBiteDirectFluorescentAntibodyDetails;
    private String snakeBiteMicroscopyDetails;
    private String snakeBiteNeutralizingAntibodiesDetails;
    private String snakeBitePcrRtPcrDetails;
    private String snakeBiteGramStainDetails;
    private String snakeBiteLatexAgglutinationDetails;
    private String snakeBiteCqValueDetectionDetails;
    private String snakeBiteSequencingDetails;
    private String snakeBiteDnaMicroarrayDetails;
    private String snakeBiteOtherDetails;
    private String rubellaAntibodyDetection;
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
    private String rubellaDirectFluorescentAntibody;
    private String rubellaMicroscopy;
    private String rubellaNeutralizingAntibodies;
    private String rubellaPcrRtPcr;
    private String rubellaGramStain;
    private String rubellaLatexAgglutination;
    private String rubellaCqValueDetection;
    private String rubellaSequencing;
    private String rubellaDnaMicroarray;
    private String rubellaOther;
    private String rubellaAntibodyDetectionDetails;
    private String rubellaAntigenDetectionDetails;
    private String rubellaRapidTestDetails;
    private String rubellaCultureDetails;
    private String rubellaHistopathologyDetails;
    private String rubellaIsolationDetails;
    private String rubellaIgmSerumAntibodyDetails;
    private String rubellaIggSerumAntibodyDetails;
    private String rubellaIgaSerumAntibodyDetails;
    private String rubellaIncubationTimeDetails;
    private String rubellaIndirectFluorescentAntibodyDetails;
    private String rubellaDirectFluorescentAntibodyDetails;
    private String rubellaMicroscopyDetails;
    private String rubellaNeutralizingAntibodiesDetails;
    private String rubellaPcrRtPcrDetails;
    private String rubellaGramStainDetails;
    private String rubellaLatexAgglutinationDetails;
    private String rubellaCqValueDetectionDetails;
    private String rubellaSequencingDetails;
    private String rubellaDnaMicroarrayDetails;
    private String rubellaOtherDetails;
    private String tuberculosisAntibodyDetection;
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
    private String tuberculosisDirectFluorescentAntibody;
    private String tuberculosisMicroscopy;
    private String tuberculosisNeutralizingAntibodies;
    private String tuberculosisPcrRtPcr;
    private String tuberculosisGramStain;
    private String tuberculosisLatexAgglutination;
    private String tuberculosisCqValueDetection;
    private String tuberculosisSequencing;
    private String tuberculosisDnaMicroarray;
    private String tuberculosisOther;
    private String tuberculosisAntibodyDetectionDetails;
    private String tuberculosisAntigenDetectionDetails;
    private String tuberculosisRapidTestDetails;
    private String tuberculosisCultureDetails;
    private String tuberculosisHistopathologyDetails;
    private String tuberculosisIsolationDetails;
    private String tuberculosisIgmSerumAntibodyDetails;
    private String tuberculosisIggSerumAntibodyDetails;
    private String tuberculosisIgaSerumAntibodyDetails;
    private String tuberculosisIncubationTimeDetails;
    private String tuberculosisIndirectFluorescentAntibodyDetails;
    private String tuberculosisDirectFluorescentAntibodyDetails;
    private String tuberculosisMicroscopyDetails;
    private String tuberculosisNeutralizingAntibodiesDetails;
    private String tuberculosisPcrRtPcrDetails;
    private String tuberculosisGramStainDetails;
    private String tuberculosisLatexAgglutinationDetails;
    private String tuberculosisCqValueDetectionDetails;
    private String tuberculosisSequencingDetails;
    private String tuberculosisDnaMicroarrayDetails;
    private String tuberculosisOtherDetails;
    private String leprosyAntibodyDetection;
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
    private String leprosyDirectFluorescentAntibody;
    private String leprosyMicroscopy;
    private String leprosyNeutralizingAntibodies;
    private String leprosyPcrRtPcr;
    private String leprosyGramStain;
    private String leprosyLatexAgglutination;
    private String leprosyCqValueDetection;
    private String leprosySequencing;
    private String leprosyDnaMicroarray;
    private String leprosyOther;
    private String leprosyAntibodyDetectionDetails;
    private String leprosyAntigenDetectionDetails;
    private String leprosyRapidTestDetails;
    private String leprosyCultureDetails;
    private String leprosyHistopathologyDetails;
    private String leprosyIsolationDetails;
    private String leprosyIgmSerumAntibodyDetails;
    private String leprosyIggSerumAntibodyDetails;
    private String leprosyIgaSerumAntibodyDetails;
    private String leprosyIncubationTimeDetails;
    private String leprosyIndirectFluorescentAntibodyDetails;
    private String leprosyDirectFluorescentAntibodyDetails;
    private String leprosyMicroscopyDetails;
    private String leprosyNeutralizingAntibodiesDetails;
    private String leprosyPcrRtPcrDetails;
    private String leprosyGramStainDetails;
    private String leprosyLatexAgglutinationDetails;
    private String leprosyCqValueDetectionDetails;
    private String leprosySequencingDetails;
    private String leprosyDnaMicroarrayDetails;
    private String leprosyOtherDetails;
    private String lymphaticFilariasisAntibodyDetection;
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
    private String lymphaticFilariasisDirectFluorescentAntibody;
    private String lymphaticFilariasisMicroscopy;
    private String lymphaticFilariasisNeutralizingAntibodies;
    private String lymphaticFilariasisPcrRtPcr;
    private String lymphaticFilariasisGramStain;
    private String lymphaticFilariasisLatexAgglutination;
    private String lymphaticFilariasisCqValueDetection;
    private String lymphaticFilariasisSequencing;
    private String lymphaticFilariasisDnaMicroarray;
    private String lymphaticFilariasisOther;
    private String lymphaticFilariasisAntibodyDetectionDetails;
    private String lymphaticFilariasisAntigenDetectionDetails;
    private String lymphaticFilariasisRapidTestDetails;
    private String lymphaticFilariasisCultureDetails;
    private String lymphaticFilariasisHistopathologyDetails;
    private String lymphaticFilariasisIsolationDetails;
    private String lymphaticFilariasisIgmSerumAntibodyDetails;
    private String lymphaticFilariasisIggSerumAntibodyDetails;
    private String lymphaticFilariasisIgaSerumAntibodyDetails;
    private String lymphaticFilariasisIncubationTimeDetails;
    private String lymphaticFilariasisIndirectFluorescentAntibodyDetails;
    private String lymphaticFilariasisDirectFluorescentAntibodyDetails;
    private String lymphaticFilariasisMicroscopyDetails;
    private String lymphaticFilariasisNeutralizingAntibodiesDetails;
    private String lymphaticFilariasisPcrRtPcrDetails;
    private String lymphaticFilariasisGramStainDetails;
    private String lymphaticFilariasisLatexAgglutinationDetails;
    private String lymphaticFilariasisCqValueDetectionDetails;
    private String lymphaticFilariasisSequencingDetails;
    private String lymphaticFilariasisDnaMicroarrayDetails;
    private String lymphaticFilariasisOtherDetails;
    private String buruliUlcerAntibodyDetection;
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
    private String buruliUlcerDirectFluorescentAntibody;
    private String buruliUlcerMicroscopy;
    private String buruliUlcerNeutralizingAntibodies;
    private String buruliUlcerPcrRtPcr;
    private String buruliUlcerGramStain;
    private String buruliUlcerLatexAgglutination;
    private String buruliUlcerCqValueDetection;
    private String buruliUlcerSequencing;
    private String buruliUlcerDnaMicroarray;
    private String buruliUlcerOther;
    private String buruliUlcerAntibodyDetectionDetails;
    private String buruliUlcerAntigenDetectionDetails;
    private String buruliUlcerRapidTestDetails;
    private String buruliUlcerCultureDetails;
    private String buruliUlcerHistopathologyDetails;
    private String buruliUlcerIsolationDetails;
    private String buruliUlcerIgmSerumAntibodyDetails;
    private String buruliUlcerIggSerumAntibodyDetails;
    private String buruliUlcerIgaSerumAntibodyDetails;
    private String buruliUlcerIncubationTimeDetails;
    private String buruliUlcerIndirectFluorescentAntibodyDetails;
    private String buruliUlcerDirectFluorescentAntibodyDetails;
    private String buruliUlcerMicroscopyDetails;
    private String buruliUlcerNeutralizingAntibodiesDetails;
    private String buruliUlcerPcrRtPcrDetails;
    private String buruliUlcerGramStainDetails;
    private String buruliUlcerLatexAgglutinationDetails;
    private String buruliUlcerCqValueDetectionDetails;
    private String buruliUlcerSequencingDetails;
    private String buruliUlcerDnaMicroarrayDetails;
    private String buruliUlcerOtherDetails;
    private String pertussisAntibodyDetection;
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
    private String pertussisDirectFluorescentAntibody;
    private String pertussisMicroscopy;
    private String pertussisNeutralizingAntibodies;
    private String pertussisPcrRtPcr;
    private String pertussisGramStain;
    private String pertussisLatexAgglutination;
    private String pertussisCqValueDetection;
    private String pertussisSequencing;
    private String pertussisDnaMicroarray;
    private String pertussisOther;
    private String pertussisAntibodyDetectionDetails;
    private String pertussisAntigenDetectionDetails;
    private String pertussisRapidTestDetails;
    private String pertussisCultureDetails;
    private String pertussisHistopathologyDetails;
    private String pertussisIsolationDetails;
    private String pertussisIgmSerumAntibodyDetails;
    private String pertussisIggSerumAntibodyDetails;
    private String pertussisIgaSerumAntibodyDetails;
    private String pertussisIncubationTimeDetails;
    private String pertussisIndirectFluorescentAntibodyDetails;
    private String pertussisDirectFluorescentAntibodyDetails;
    private String pertussisMicroscopyDetails;
    private String pertussisNeutralizingAntibodiesDetails;
    private String pertussisPcrRtPcrDetails;
    private String pertussisGramStainDetails;
    private String pertussisLatexAgglutinationDetails;
    private String pertussisCqValueDetectionDetails;
    private String pertussisSequencingDetails;
    private String pertussisDnaMicroarrayDetails;
    private String pertussisOtherDetails;
    private String neonatalTetanusAntibodyDetection;
    private String neonatalTetanusAntigenDetection;
    private String neonatalTetanusRapidTest;
    private String neonatalTetanusCulture;
    private String neonatalTetanusHistopathology;
    private String neonatalTetanusIsolation;
    private String neonatalTetanusIgmSerumAntibody;
    private String neonatalTetanusIggSerumAntibody;
    private String neonatalTetanusIgaSerumAntibody;
    private String neonatalTetanusIncubationTime;
    private String neonatalTetanusIndirectFluorescentAntibody;
    private String neonatalTetanusDirectFluorescentAntibody;
    private String neonatalTetanusMicroscopy;
    private String neonatalTetanusNeutralizingAntibodies;
    private String neonatalTetanusPcrRtPcr;
    private String neonatalTetanusGramStain;
    private String neonatalTetanusLatexAgglutination;
    private String neonatalTetanusCqValueDetection;
    private String neonatalTetanusSequencing;
    private String neonatalTetanusDnaMicroarray;
    private String neonatalTetanusOther;
    private String neonatalTetanusAntibodyDetectionDetails;
    private String neonatalTetanusAntigenDetectionDetails;
    private String neonatalTetanusRapidTestDetails;
    private String neonatalTetanusCultureDetails;
    private String neonatalTetanusHistopathologyDetails;
    private String neonatalTetanusIsolationDetails;
    private String neonatalTetanusIgmSerumAntibodyDetails;
    private String neonatalTetanusIggSerumAntibodyDetails;
    private String neonatalTetanusIgaSerumAntibodyDetails;
    private String neonatalTetanusIncubationTimeDetails;
    private String neonatalTetanusIndirectFluorescentAntibodyDetails;
    private String neonatalTetanusDirectFluorescentAntibodyDetails;
    private String neonatalTetanusMicroscopyDetails;
    private String neonatalTetanusNeutralizingAntibodiesDetails;
    private String neonatalTetanusPcrRtPcrDetails;
    private String neonatalTetanusGramStainDetails;
    private String neonatalTetanusLatexAgglutinationDetails;
    private String neonatalTetanusCqValueDetectionDetails;
    private String neonatalTetanusSequencingDetails;
    private String neonatalTetanusDnaMicroarrayDetails;
    private String neonatalTetanusOtherDetails;
    private String onchocerciasisAntibodyDetection;
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
    private String onchocerciasisDirectFluorescentAntibody;
    private String onchocerciasisMicroscopy;
    private String onchocerciasisNeutralizingAntibodies;
    private String onchocerciasisPcrRtPcr;
    private String onchocerciasisGramStain;
    private String onchocerciasisLatexAgglutination;
    private String onchocerciasisCqValueDetection;
    private String onchocerciasisSequencing;
    private String onchocerciasisDnaMicroarray;
    private String onchocerciasisOther;
    private String onchocerciasisAntibodyDetectionDetails;
    private String onchocerciasisAntigenDetectionDetails;
    private String onchocerciasisRapidTestDetails;
    private String onchocerciasisCultureDetails;
    private String onchocerciasisHistopathologyDetails;
    private String onchocerciasisIsolationDetails;
    private String onchocerciasisIgmSerumAntibodyDetails;
    private String onchocerciasisIggSerumAntibodyDetails;
    private String onchocerciasisIgaSerumAntibodyDetails;
    private String onchocerciasisIncubationTimeDetails;
    private String onchocerciasisIndirectFluorescentAntibodyDetails;
    private String onchocerciasisDirectFluorescentAntibodyDetails;
    private String onchocerciasisMicroscopyDetails;
    private String onchocerciasisNeutralizingAntibodiesDetails;
    private String onchocerciasisPcrRtPcrDetails;
    private String onchocerciasisGramStainDetails;
    private String onchocerciasisLatexAgglutinationDetails;
    private String onchocerciasisCqValueDetectionDetails;
    private String onchocerciasisSequencingDetails;
    private String onchocerciasisDnaMicroarrayDetails;
    private String onchocerciasisOtherDetails;
    private String diphteriaAntibodyDetection;
    private String diphteriaAntigenDetection;
    private String diphteriaRapidTest;
    private String diphteriaCulture;
    private String diphteriaHistopathology;
    private String diphteriaIsolation;
    private String diphteriaIgmSerumAntibody;
    private String diphteriaIggSerumAntibody;
    private String diphteriaIgaSerumAntibody;
    private String diphteriaIncubationTime;
    private String diphteriaIndirectFluorescentAntibody;
    private String diphteriaDirectFluorescentAntibody;
    private String diphteriaMicroscopy;
    private String diphteriaNeutralizingAntibodies;
    private String diphteriaPcrRtPcr;
    private String diphteriaGramStain;
    private String diphteriaLatexAgglutination;
    private String diphteriaCqValueDetection;
    private String diphteriaSequencing;
    private String diphteriaDnaMicroarray;
    private String diphteriaOther;
    private String diphteriaAntibodyDetectionDetails;
    private String diphteriaAntigenDetectionDetails;
    private String diphteriaRapidTestDetails;
    private String diphteriaCultureDetails;
    private String diphteriaHistopathologyDetails;
    private String diphteriaIsolationDetails;
    private String diphteriaIgmSerumAntibodyDetails;
    private String diphteriaIggSerumAntibodyDetails;
    private String diphteriaIgaSerumAntibodyDetails;
    private String diphteriaIncubationTimeDetails;
    private String diphteriaIndirectFluorescentAntibodyDetails;
    private String diphteriaDirectFluorescentAntibodyDetails;
    private String diphteriaMicroscopyDetails;
    private String diphteriaNeutralizingAntibodiesDetails;
    private String diphteriaPcrRtPcrDetails;
    private String diphteriaGramStainDetails;
    private String diphteriaLatexAgglutinationDetails;
    private String diphteriaCqValueDetectionDetails;
    private String diphteriaSequencingDetails;
    private String diphteriaDnaMicroarrayDetails;
    private String diphteriaOtherDetails;
    private String trachomaAntibodyDetection;
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
    private String trachomaDirectFluorescentAntibody;
    private String trachomaMicroscopy;
    private String trachomaNeutralizingAntibodies;
    private String trachomaPcrRtPcr;
    private String trachomaGramStain;
    private String trachomaLatexAgglutination;
    private String trachomaCqValueDetection;
    private String trachomaSequencing;
    private String trachomaDnaMicroarray;
    private String trachomaOther;
    private String trachomaAntibodyDetectionDetails;
    private String trachomaAntigenDetectionDetails;
    private String trachomaRapidTestDetails;
    private String trachomaCultureDetails;
    private String trachomaHistopathologyDetails;
    private String trachomaIsolationDetails;
    private String trachomaIgmSerumAntibodyDetails;
    private String trachomaIggSerumAntibodyDetails;
    private String trachomaIgaSerumAntibodyDetails;
    private String trachomaIncubationTimeDetails;
    private String trachomaIndirectFluorescentAntibodyDetails;
    private String trachomaDirectFluorescentAntibodyDetails;
    private String trachomaMicroscopyDetails;
    private String trachomaNeutralizingAntibodiesDetails;
    private String trachomaPcrRtPcrDetails;
    private String trachomaGramStainDetails;
    private String trachomaLatexAgglutinationDetails;
    private String trachomaCqValueDetectionDetails;
    private String trachomaSequencingDetails;
    private String trachomaDnaMicroarrayDetails;
    private String trachomaOtherDetails;
    private String yawsEndemicSyphilisAntibodyDetection;
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
    private String yawsEndemicSyphilisDirectFluorescentAntibody;
    private String yawsEndemicSyphilisMicroscopy;
    private String yawsEndemicSyphilisNeutralizingAntibodies;
    private String yawsEndemicSyphilisPcrRtPcr;
    private String yawsEndemicSyphilisGramStain;
    private String yawsEndemicSyphilisLatexAgglutination;
    private String yawsEndemicSyphilisCqValueDetection;
    private String yawsEndemicSyphilisSequencing;
    private String yawsEndemicSyphilisDnaMicroarray;
    private String yawsEndemicSyphilisOther;
    private String yawsEndemicSyphilisAntibodyDetectionDetails;
    private String yawsEndemicSyphilisAntigenDetectionDetails;
    private String yawsEndemicSyphilisRapidTestDetails;
    private String yawsEndemicSyphilisCultureDetails;
    private String yawsEndemicSyphilisHistopathologyDetails;
    private String yawsEndemicSyphilisIsolationDetails;
    private String yawsEndemicSyphilisIgmSerumAntibodyDetails;
    private String yawsEndemicSyphilisIggSerumAntibodyDetails;
    private String yawsEndemicSyphilisIgaSerumAntibodyDetails;
    private String yawsEndemicSyphilisIncubationTimeDetails;
    private String yawsEndemicSyphilisIndirectFluorescentAntibodyDetails;
    private String yawsEndemicSyphilisDirectFluorescentAntibodyDetails;
    private String yawsEndemicSyphilisMicroscopyDetails;
    private String yawsEndemicSyphilisNeutralizingAntibodiesDetails;
    private String yawsEndemicSyphilisPcrRtPcrDetails;
    private String yawsEndemicSyphilisGramStainDetails;
    private String yawsEndemicSyphilisLatexAgglutinationDetails;
    private String yawsEndemicSyphilisCqValueDetectionDetails;
    private String yawsEndemicSyphilisSequencingDetails;
    private String yawsEndemicSyphilisDnaMicroarrayDetails;
    private String yawsEndemicSyphilisOtherDetails;
    private String maternalDeathsAntibodyDetection;
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
    private String maternalDeathsDirectFluorescentAntibody;
    private String maternalDeathsMicroscopy;
    private String maternalDeathsNeutralizingAntibodies;
    private String maternalDeathsPcrRtPcr;
    private String maternalDeathsGramStain;
    private String maternalDeathsLatexAgglutination;
    private String maternalDeathsCqValueDetection;
    private String maternalDeathsSequencing;
    private String maternalDeathsDnaMicroarray;
    private String maternalDeathsOther;
    private String maternalDeathsAntibodyDetectionDetails;
    private String maternalDeathsAntigenDetectionDetails;
    private String maternalDeathsRapidTestDetails;
    private String maternalDeathsCultureDetails;
    private String maternalDeathsHistopathologyDetails;
    private String maternalDeathsIsolationDetails;
    private String maternalDeathsIgmSerumAntibodyDetails;
    private String maternalDeathsIggSerumAntibodyDetails;
    private String maternalDeathsIgaSerumAntibodyDetails;
    private String maternalDeathsIncubationTimeDetails;
    private String maternalDeathsIndirectFluorescentAntibodyDetails;
    private String maternalDeathsDirectFluorescentAntibodyDetails;
    private String maternalDeathsMicroscopyDetails;
    private String maternalDeathsNeutralizingAntibodiesDetails;
    private String maternalDeathsPcrRtPcrDetails;
    private String maternalDeathsGramStainDetails;
    private String maternalDeathsLatexAgglutinationDetails;
    private String maternalDeathsCqValueDetectionDetails;
    private String maternalDeathsSequencingDetails;
    private String maternalDeathsDnaMicroarrayDetails;
    private String maternalDeathsOtherDetails;
    private String perinatalDeathsAntibodyDetection;
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
    private String perinatalDeathsDirectFluorescentAntibody;
    private String perinatalDeathsMicroscopy;
    private String perinatalDeathsNeutralizingAntibodies;
    private String perinatalDeathsPcrRtPcr;
    private String perinatalDeathsGramStain;
    private String perinatalDeathsLatexAgglutination;
    private String perinatalDeathsCqValueDetection;
    private String perinatalDeathsSequencing;
    private String perinatalDeathsDnaMicroarray;
    private String perinatalDeathsOther;
    private String perinatalDeathsAntibodyDetectionDetails;
    private String perinatalDeathsAntigenDetectionDetails;
    private String perinatalDeathsRapidTestDetails;
    private String perinatalDeathsCultureDetails;
    private String perinatalDeathsHistopathologyDetails;
    private String perinatalDeathsIsolationDetails;
    private String perinatalDeathsIgmSerumAntibodyDetails;
    private String perinatalDeathsIggSerumAntibodyDetails;
    private String perinatalDeathsIgaSerumAntibodyDetails;
    private String perinatalDeathsIncubationTimeDetails;
    private String perinatalDeathsIndirectFluorescentAntibodyDetails;
    private String perinatalDeathsDirectFluorescentAntibodyDetails;
    private String perinatalDeathsMicroscopyDetails;
    private String perinatalDeathsNeutralizingAntibodiesDetails;
    private String perinatalDeathsPcrRtPcrDetails;
    private String perinatalDeathsGramStainDetails;
    private String perinatalDeathsLatexAgglutinationDetails;
    private String perinatalDeathsCqValueDetectionDetails;
    private String perinatalDeathsSequencingDetails;
    private String perinatalDeathsDnaMicroarrayDetails;
    private String perinatalDeathsOtherDetails;
    private String influenzaAAntibodyDetection;
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
    private String influenzaADirectFluorescentAntibody;
    private String influenzaAMicroscopy;
    private String influenzaANeutralizingAntibodies;
    private String influenzaAPcrRtPcr;
    private String influenzaAGramStain;
    private String influenzaALatexAgglutination;
    private String influenzaACqValueDetection;
    private String influenzaASequencing;
    private String influenzaADnaMicroarray;
    private String influenzaAOther;
    private String influenzaAAntibodyDetectionDetails;
    private String influenzaAAntigenDetectionDetails;
    private String influenzaARapidTestDetails;
    private String influenzaACultureDetails;
    private String influenzaAHistopathologyDetails;
    private String influenzaAIsolationDetails;
    private String influenzaAIgmSerumAntibodyDetails;
    private String influenzaAIggSerumAntibodyDetails;
    private String influenzaAIgaSerumAntibodyDetails;
    private String influenzaAIncubationTimeDetails;
    private String influenzaAIndirectFluorescentAntibodyDetails;
    private String influenzaADirectFluorescentAntibodyDetails;
    private String influenzaAMicroscopyDetails;
    private String influenzaANeutralizingAntibodiesDetails;
    private String influenzaAPcrRtPcrDetails;
    private String influenzaAGramStainDetails;
    private String influenzaALatexAgglutinationDetails;
    private String influenzaACqValueDetectionDetails;
    private String influenzaASequencingDetails;
    private String influenzaADnaMicroarrayDetails;
    private String influenzaAOtherDetails;
    private String influenzaBAntibodyDetection;
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
    private String influenzaBDirectFluorescentAntibody;
    private String influenzaBMicroscopy;
    private String influenzaBNeutralizingAntibodies;
    private String influenzaBPcrRtPcr;
    private String influenzaBGramStain;
    private String influenzaBLatexAgglutination;
    private String influenzaBCqValueDetection;
    private String influenzaBSequencing;
    private String influenzaBDnaMicroarray;
    private String influenzaBOther;
    private String influenzaBAntibodyDetectionDetails;
    private String influenzaBAntigenDetectionDetails;
    private String influenzaBRapidTestDetails;
    private String influenzaBCultureDetails;
    private String influenzaBHistopathologyDetails;
    private String influenzaBIsolationDetails;
    private String influenzaBIgmSerumAntibodyDetails;
    private String influenzaBIggSerumAntibodyDetails;
    private String influenzaBIgaSerumAntibodyDetails;
    private String influenzaBIncubationTimeDetails;
    private String influenzaBIndirectFluorescentAntibodyDetails;
    private String influenzaBDirectFluorescentAntibodyDetails;
    private String influenzaBMicroscopyDetails;
    private String influenzaBNeutralizingAntibodiesDetails;
    private String influenzaBPcrRtPcrDetails;
    private String influenzaBGramStainDetails;
    private String influenzaBLatexAgglutinationDetails;
    private String influenzaBCqValueDetectionDetails;
    private String influenzaBSequencingDetails;
    private String influenzaBDnaMicroarrayDetails;
    private String influenzaBOtherDetails;
    private String hMetapneumovirusAntibodyDetection;
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
    private String hMetapneumovirusDirectFluorescentAntibody;
    private String hMetapneumovirusMicroscopy;
    private String hMetapneumovirusNeutralizingAntibodies;
    private String hMetapneumovirusPcrRtPcr;
    private String hMetapneumovirusGramStain;
    private String hMetapneumovirusLatexAgglutination;
    private String hMetapneumovirusCqValueDetection;
    private String hMetapneumovirusSequencing;
    private String hMetapneumovirusDnaMicroarray;
    private String hMetapneumovirusOther;
    private String hMetapneumovirusAntibodyDetectionDetails;
    private String hMetapneumovirusAntigenDetectionDetails;
    private String hMetapneumovirusRapidTestDetails;
    private String hMetapneumovirusCultureDetails;
    private String hMetapneumovirusHistopathologyDetails;
    private String hMetapneumovirusIsolationDetails;
    private String hMetapneumovirusIgmSerumAntibodyDetails;
    private String hMetapneumovirusIggSerumAntibodyDetails;
    private String hMetapneumovirusIgaSerumAntibodyDetails;
    private String hMetapneumovirusIncubationTimeDetails;
    private String hMetapneumovirusIndirectFluorescentAntibodyDetails;
    private String hMetapneumovirusDirectFluorescentAntibodyDetails;
    private String hMetapneumovirusMicroscopyDetails;
    private String hMetapneumovirusNeutralizingAntibodiesDetails;
    private String hMetapneumovirusPcrRtPcrDetails;
    private String hMetapneumovirusGramStainDetails;
    private String hMetapneumovirusLatexAgglutinationDetails;
    private String hMetapneumovirusCqValueDetectionDetails;
    private String hMetapneumovirusSequencingDetails;
    private String hMetapneumovirusDnaMicroarrayDetails;
    private String hMetapneumovirusOtherDetails;
    private String respiratorySyncytialVirusAntibodyDetection;
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
    private String respiratorySyncytialVirusDirectFluorescentAntibody;
    private String respiratorySyncytialVirusMicroscopy;
    private String respiratorySyncytialVirusNeutralizingAntibodies;
    private String respiratorySyncytialVirusPcrRtPcr;
    private String respiratorySyncytialVirusGramStain;
    private String respiratorySyncytialVirusLatexAgglutination;
    private String respiratorySyncytialVirusCqValueDetection;
    private String respiratorySyncytialVirusSequencing;
    private String respiratorySyncytialVirusDnaMicroarray;
    private String respiratorySyncytialVirusOther;
    private String respiratorySyncytialVirusAntibodyDetectionDetails;
    private String respiratorySyncytialVirusAntigenDetectionDetails;
    private String respiratorySyncytialVirusRapidTestDetails;
    private String respiratorySyncytialVirusCultureDetails;
    private String respiratorySyncytialVirusHistopathologyDetails;
    private String respiratorySyncytialVirusIsolationDetails;
    private String respiratorySyncytialVirusIgmSerumAntibodyDetails;
    private String respiratorySyncytialVirusIggSerumAntibodyDetails;
    private String respiratorySyncytialVirusIgaSerumAntibodyDetails;
    private String respiratorySyncytialVirusIncubationTimeDetails;
    private String respiratorySyncytialVirusIndirectFluorescentAntibodyDetails;
    private String respiratorySyncytialVirusDirectFluorescentAntibodyDetails;
    private String respiratorySyncytialVirusMicroscopyDetails;
    private String respiratorySyncytialVirusNeutralizingAntibodiesDetails;
    private String respiratorySyncytialVirusPcrRtPcrDetails;
    private String respiratorySyncytialVirusGramStainDetails;
    private String respiratorySyncytialVirusLatexAgglutinationDetails;
    private String respiratorySyncytialVirusCqValueDetectionDetails;
    private String respiratorySyncytialVirusSequencingDetails;
    private String respiratorySyncytialVirusDnaMicroarrayDetails;
    private String respiratorySyncytialVirusOtherDetails;
    private String parainfluenzaAntibodyDetection;
    private String parainfluenzaAntigenDetection;
    private String parainfluenzaRapidTest;
    private String parainfluenzaCulture;
    private String parainfluenzaHistopathology;
    private String parainfluenzaIsolation;
    private String parainfluenzaIgmSerumAntibody;
    private String parainfluenzaIggSerumAntibody;
    private String parainfluenzaIgaSerumAntibody;
    private String parainfluenzaIncubationTime;
    private String parainfluenzaIndirectFluorescentAntibody;
    private String parainfluenzaDirectFluorescentAntibody;
    private String parainfluenzaMicroscopy;
    private String parainfluenzaNeutralizingAntibodies;
    private String parainfluenzaPcrRtPcr;
    private String parainfluenzaGramStain;
    private String parainfluenzaLatexAgglutination;
    private String parainfluenzaCqValueDetection;
    private String parainfluenzaSequencing;
    private String parainfluenzaDnaMicroarray;
    private String parainfluenzaOther;
    private String parainfluenzaAntibodyDetectionDetails;
    private String parainfluenzaAntigenDetectionDetails;
    private String parainfluenzaRapidTestDetails;
    private String parainfluenzaCultureDetails;
    private String parainfluenzaHistopathologyDetails;
    private String parainfluenzaIsolationDetails;
    private String parainfluenzaIgmSerumAntibodyDetails;
    private String parainfluenzaIggSerumAntibodyDetails;
    private String parainfluenzaIgaSerumAntibodyDetails;
    private String parainfluenzaIncubationTimeDetails;
    private String parainfluenzaIndirectFluorescentAntibodyDetails;
    private String parainfluenzaDirectFluorescentAntibodyDetails;
    private String parainfluenzaMicroscopyDetails;
    private String parainfluenzaNeutralizingAntibodiesDetails;
    private String parainfluenzaPcrRtPcrDetails;
    private String parainfluenzaGramStainDetails;
    private String parainfluenzaLatexAgglutinationDetails;
    private String parainfluenzaCqValueDetectionDetails;
    private String parainfluenzaSequencingDetails;
    private String parainfluenzaDnaMicroarrayDetails;
    private String parainfluenzaOtherDetails;
    private String adenovirusAntibodyDetection;
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
    private String adenovirusDirectFluorescentAntibody;
    private String adenovirusMicroscopy;
    private String adenovirusNeutralizingAntibodies;
    private String adenovirusPcrRtPcr;
    private String adenovirusGramStain;
    private String adenovirusLatexAgglutination;
    private String adenovirusCqValueDetection;
    private String adenovirusSequencing;
    private String adenovirusDnaMicroarray;
    private String adenovirusOther;
    private String adenovirusAntibodyDetectionDetails;
    private String adenovirusAntigenDetectionDetails;
    private String adenovirusRapidTestDetails;
    private String adenovirusCultureDetails;
    private String adenovirusHistopathologyDetails;
    private String adenovirusIsolationDetails;
    private String adenovirusIgmSerumAntibodyDetails;
    private String adenovirusIggSerumAntibodyDetails;
    private String adenovirusIgaSerumAntibodyDetails;
    private String adenovirusIncubationTimeDetails;
    private String adenovirusIndirectFluorescentAntibodyDetails;
    private String adenovirusDirectFluorescentAntibodyDetails;
    private String adenovirusMicroscopyDetails;
    private String adenovirusNeutralizingAntibodiesDetails;
    private String adenovirusPcrRtPcrDetails;
    private String adenovirusGramStainDetails;
    private String adenovirusLatexAgglutinationDetails;
    private String adenovirusCqValueDetectionDetails;
    private String adenovirusSequencingDetails;
    private String adenovirusDnaMicroarrayDetails;
    private String adenovirusOtherDetails;
    private String rhinovirusAntibodyDetection;
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
    private String rhinovirusDirectFluorescentAntibody;
    private String rhinovirusMicroscopy;
    private String rhinovirusNeutralizingAntibodies;
    private String rhinovirusPcrRtPcr;
    private String rhinovirusGramStain;
    private String rhinovirusLatexAgglutination;
    private String rhinovirusCqValueDetection;
    private String rhinovirusSequencing;
    private String rhinovirusDnaMicroarray;
    private String rhinovirusOther;
    private String rhinovirusAntibodyDetectionDetails;
    private String rhinovirusAntigenDetectionDetails;
    private String rhinovirusRapidTestDetails;
    private String rhinovirusCultureDetails;
    private String rhinovirusHistopathologyDetails;
    private String rhinovirusIsolationDetails;
    private String rhinovirusIgmSerumAntibodyDetails;
    private String rhinovirusIggSerumAntibodyDetails;
    private String rhinovirusIgaSerumAntibodyDetails;
    private String rhinovirusIncubationTimeDetails;
    private String rhinovirusIndirectFluorescentAntibodyDetails;
    private String rhinovirusDirectFluorescentAntibodyDetails;
    private String rhinovirusMicroscopyDetails;
    private String rhinovirusNeutralizingAntibodiesDetails;
    private String rhinovirusPcrRtPcrDetails;
    private String rhinovirusGramStainDetails;
    private String rhinovirusLatexAgglutinationDetails;
    private String rhinovirusCqValueDetectionDetails;
    private String rhinovirusSequencingDetails;
    private String rhinovirusDnaMicroarrayDetails;
    private String rhinovirusOtherDetails;
    private String enterovirusAntibodyDetection;
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
    private String enterovirusDirectFluorescentAntibody;
    private String enterovirusMicroscopy;
    private String enterovirusNeutralizingAntibodies;
    private String enterovirusPcrRtPcr;
    private String enterovirusGramStain;
    private String enterovirusLatexAgglutination;
    private String enterovirusCqValueDetection;
    private String enterovirusSequencing;
    private String enterovirusDnaMicroarray;
    private String enterovirusOther;
    private String enterovirusAntibodyDetectionDetails;
    private String enterovirusAntigenDetectionDetails;
    private String enterovirusRapidTestDetails;
    private String enterovirusCultureDetails;
    private String enterovirusHistopathologyDetails;
    private String enterovirusIsolationDetails;
    private String enterovirusIgmSerumAntibodyDetails;
    private String enterovirusIggSerumAntibodyDetails;
    private String enterovirusIgaSerumAntibodyDetails;
    private String enterovirusIncubationTimeDetails;
    private String enterovirusIndirectFluorescentAntibodyDetails;
    private String enterovirusDirectFluorescentAntibodyDetails;
    private String enterovirusMicroscopyDetails;
    private String enterovirusNeutralizingAntibodiesDetails;
    private String enterovirusPcrRtPcrDetails;
    private String enterovirusGramStainDetails;
    private String enterovirusLatexAgglutinationDetails;
    private String enterovirusCqValueDetectionDetails;
    private String enterovirusSequencingDetails;
    private String enterovirusDnaMicroarrayDetails;
    private String enterovirusOtherDetails;
    private String mPneumoniaeAntibodyDetection;
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
    private String mPneumoniaeDirectFluorescentAntibody;
    private String mPneumoniaeMicroscopy;
    private String mPneumoniaeNeutralizingAntibodies;
    private String mPneumoniaePcrRtPcr;
    private String mPneumoniaeGramStain;
    private String mPneumoniaeLatexAgglutination;
    private String mPneumoniaeCqValueDetection;
    private String mPneumoniaeSequencing;
    private String mPneumoniaeDnaMicroarray;
    private String mPneumoniaeOther;
    private String mPneumoniaeAntibodyDetectionDetails;
    private String mPneumoniaeAntigenDetectionDetails;
    private String mPneumoniaeRapidTestDetails;
    private String mPneumoniaeCultureDetails;
    private String mPneumoniaeHistopathologyDetails;
    private String mPneumoniaeIsolationDetails;
    private String mPneumoniaeIgmSerumAntibodyDetails;
    private String mPneumoniaeIggSerumAntibodyDetails;
    private String mPneumoniaeIgaSerumAntibodyDetails;
    private String mPneumoniaeIncubationTimeDetails;
    private String mPneumoniaeIndirectFluorescentAntibodyDetails;
    private String mPneumoniaeDirectFluorescentAntibodyDetails;
    private String mPneumoniaeMicroscopyDetails;
    private String mPneumoniaeNeutralizingAntibodiesDetails;
    private String mPneumoniaePcrRtPcrDetails;
    private String mPneumoniaeGramStainDetails;
    private String mPneumoniaeLatexAgglutinationDetails;
    private String mPneumoniaeCqValueDetectionDetails;
    private String mPneumoniaeSequencingDetails;
    private String mPneumoniaeDnaMicroarrayDetails;
    private String mPneumoniaeOtherDetails;
    private String cPneumoniaeAntibodyDetection;
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
    private String cPneumoniaeDirectFluorescentAntibody;
    private String cPneumoniaeMicroscopy;
    private String cPneumoniaeNeutralizingAntibodies;
    private String cPneumoniaePcrRtPcr;
    private String cPneumoniaeGramStain;
    private String cPneumoniaeLatexAgglutination;
    private String cPneumoniaeCqValueDetection;
    private String cPneumoniaeSequencing;
    private String cPneumoniaeDnaMicroarray;
    private String cPneumoniaeOther;
    private String cPneumoniaeAntibodyDetectionDetails;
    private String cPneumoniaeAntigenDetectionDetails;
    private String cPneumoniaeRapidTestDetails;
    private String cPneumoniaeCultureDetails;
    private String cPneumoniaeHistopathologyDetails;
    private String cPneumoniaeIsolationDetails;
    private String cPneumoniaeIgmSerumAntibodyDetails;
    private String cPneumoniaeIggSerumAntibodyDetails;
    private String cPneumoniaeIgaSerumAntibodyDetails;
    private String cPneumoniaeIncubationTimeDetails;
    private String cPneumoniaeIndirectFluorescentAntibodyDetails;
    private String cPneumoniaeDirectFluorescentAntibodyDetails;
    private String cPneumoniaeMicroscopyDetails;
    private String cPneumoniaeNeutralizingAntibodiesDetails;
    private String cPneumoniaePcrRtPcrDetails;
    private String cPneumoniaeGramStainDetails;
    private String cPneumoniaeLatexAgglutinationDetails;
    private String cPneumoniaeCqValueDetectionDetails;
    private String cPneumoniaeSequencingDetails;
    private String cPneumoniaeDnaMicroarrayDetails;
    private String cPneumoniaeOtherDetails;
    private String ariAntibodyDetection;
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
    private String ariDirectFluorescentAntibody;
    private String ariMicroscopy;
    private String ariNeutralizingAntibodies;
    private String ariPcrRtPcr;
    private String ariGramStain;
    private String ariLatexAgglutination;
    private String ariCqValueDetection;
    private String ariSequencing;
    private String ariDnaMicroarray;
    private String ariOther;
    private String ariAntibodyDetectionDetails;
    private String ariAntigenDetectionDetails;
    private String ariRapidTestDetails;
    private String ariCultureDetails;
    private String ariHistopathologyDetails;
    private String ariIsolationDetails;
    private String ariIgmSerumAntibodyDetails;
    private String ariIggSerumAntibodyDetails;
    private String ariIgaSerumAntibodyDetails;
    private String ariIncubationTimeDetails;
    private String ariIndirectFluorescentAntibodyDetails;
    private String ariDirectFluorescentAntibodyDetails;
    private String ariMicroscopyDetails;
    private String ariNeutralizingAntibodiesDetails;
    private String ariPcrRtPcrDetails;
    private String ariGramStainDetails;
    private String ariLatexAgglutinationDetails;
    private String ariCqValueDetectionDetails;
    private String ariSequencingDetails;
    private String ariDnaMicroarrayDetails;
    private String ariOtherDetails;
    private String chikungunyaAntibodyDetection;
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
    private String chikungunyaDirectFluorescentAntibody;
    private String chikungunyaMicroscopy;
    private String chikungunyaNeutralizingAntibodies;
    private String chikungunyaPcrRtPcr;
    private String chikungunyaGramStain;
    private String chikungunyaLatexAgglutination;
    private String chikungunyaCqValueDetection;
    private String chikungunyaSequencing;
    private String chikungunyaDnaMicroarray;
    private String chikungunyaOther;
    private String chikungunyaAntibodyDetectionDetails;
    private String chikungunyaAntigenDetectionDetails;
    private String chikungunyaRapidTestDetails;
    private String chikungunyaCultureDetails;
    private String chikungunyaHistopathologyDetails;
    private String chikungunyaIsolationDetails;
    private String chikungunyaIgmSerumAntibodyDetails;
    private String chikungunyaIggSerumAntibodyDetails;
    private String chikungunyaIgaSerumAntibodyDetails;
    private String chikungunyaIncubationTimeDetails;
    private String chikungunyaIndirectFluorescentAntibodyDetails;
    private String chikungunyaDirectFluorescentAntibodyDetails;
    private String chikungunyaMicroscopyDetails;
    private String chikungunyaNeutralizingAntibodiesDetails;
    private String chikungunyaPcrRtPcrDetails;
    private String chikungunyaGramStainDetails;
    private String chikungunyaLatexAgglutinationDetails;
    private String chikungunyaCqValueDetectionDetails;
    private String chikungunyaSequencingDetails;
    private String chikungunyaDnaMicroarrayDetails;
    private String chikungunyaOtherDetails;
    private String postImmunizationAdverseEventsMildAntibodyDetection;
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
    private String postImmunizationAdverseEventsMildDirectFluorescentAntibody;
    private String postImmunizationAdverseEventsMildMicroscopy;
    private String postImmunizationAdverseEventsMildNeutralizingAntibodies;
    private String postImmunizationAdverseEventsMildPcrRtPcr;
    private String postImmunizationAdverseEventsMildGramStain;
    private String postImmunizationAdverseEventsMildLatexAgglutination;
    private String postImmunizationAdverseEventsMildCqValueDetection;
    private String postImmunizationAdverseEventsMildSequencing;
    private String postImmunizationAdverseEventsMildDnaMicroarray;
    private String postImmunizationAdverseEventsMildOther;
    private String postImmunizationAdverseEventsMildAntibodyDetectionDetails;
    private String postImmunizationAdverseEventsMildAntigenDetectionDetails;
    private String postImmunizationAdverseEventsMildRapidTestDetails;
    private String postImmunizationAdverseEventsMildCultureDetails;
    private String postImmunizationAdverseEventsMildHistopathologyDetails;
    private String postImmunizationAdverseEventsMildIsolationDetails;
    private String postImmunizationAdverseEventsMildIgmSerumAntibodyDetails;
    private String postImmunizationAdverseEventsMildIggSerumAntibodyDetails;
    private String postImmunizationAdverseEventsMildIgaSerumAntibodyDetails;
    private String postImmunizationAdverseEventsMildIncubationTimeDetails;
    private String postImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails;
    private String postImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails;
    private String postImmunizationAdverseEventsMildMicroscopyDetails;
    private String postImmunizationAdverseEventsMildNeutralizingAntibodiesDetails;
    private String postImmunizationAdverseEventsMildPcrRtPcrDetails;
    private String postImmunizationAdverseEventsMildGramStainDetails;
    private String postImmunizationAdverseEventsMildLatexAgglutinationDetails;
    private String postImmunizationAdverseEventsMildCqValueDetectionDetails;
    private String postImmunizationAdverseEventsMildSequencingDetails;
    private String postImmunizationAdverseEventsMildDnaMicroarrayDetails;
    private String postImmunizationAdverseEventsMildOtherDetails;
    private String postImmunizationAdverseEventsSevereAntibodyDetection;
    private String postImmunizationAdverseEventsSevereAntigenDetection;
    private String postImmunizationAdverseEventsSevereRapidTest;
    private String postImmunizationAdverseEventsSevereCulture;
    private String postImmunizationAdverseEventsSevereHistopathology;
    private String postImmunizationAdverseEventsSevereIsolation;
    private String postImmunizationAdverseEventsSevereIgmSerumAntibody;
    private String postImmunizationAdverseEventsSevereIggSerumAntibody;
    private String postImmunizationAdverseEventsSevereIgaSerumAntibody;
    private String postImmunizationAdverseEventsSevereIncubationTime;
    private String postImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    private String postImmunizationAdverseEventsSevereDirectFluorescentAntibody;
    private String postImmunizationAdverseEventsSevereMicroscopy;
    private String postImmunizationAdverseEventsSevereNeutralizingAntibodies;
    private String postImmunizationAdverseEventsSeverePcrRtPcr;
    private String postImmunizationAdverseEventsSevereGramStain;
    private String postImmunizationAdverseEventsSevereLatexAgglutination;
    private String postImmunizationAdverseEventsSevereCqValueDetection;
    private String postImmunizationAdverseEventsSevereSequencing;
    private String postImmunizationAdverseEventsSevereDnaMicroarray;
    private String postImmunizationAdverseEventsSevereOther;
    private String postImmunizationAdverseEventsSevereAntibodyDetectionDetails;
    private String postImmunizationAdverseEventsSevereAntigenDetectionDetails;
    private String postImmunizationAdverseEventsSevereRapidTestDetails;
    private String postImmunizationAdverseEventsSevereCultureDetails;
    private String postImmunizationAdverseEventsSevereHistopathologyDetails;
    private String postImmunizationAdverseEventsSevereIsolationDetails;
    private String postImmunizationAdverseEventsSevereIgmSerumAntibodyDetails;
    private String postImmunizationAdverseEventsSevereIggSerumAntibodyDetails;
    private String postImmunizationAdverseEventsSevereIgaSerumAntibodyDetails;
    private String postImmunizationAdverseEventsSevereIncubationTimeDetails;
    private String postImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails;
    private String postImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails;
    private String postImmunizationAdverseEventsSevereMicroscopyDetails;
    private String postImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails;
    private String postImmunizationAdverseEventsSeverePcrRtPcrDetails;
    private String postImmunizationAdverseEventsSevereGramStainDetails;
    private String postImmunizationAdverseEventsSevereLatexAgglutinationDetails;
    private String postImmunizationAdverseEventsSevereCqValueDetectionDetails;
    private String postImmunizationAdverseEventsSevereSequencingDetails;
    private String postImmunizationAdverseEventsSevereDnaMicroarrayDetails;
    private String postImmunizationAdverseEventsSevereOtherDetails;
    private String fhaAntibodyDetection;
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
    private String fhaDirectFluorescentAntibody;
    private String fhaMicroscopy;
    private String fhaNeutralizingAntibodies;
    private String fhaPcrRtPcr;
    private String fhaGramStain;
    private String fhaLatexAgglutination;
    private String fhaCqValueDetection;
    private String fhaSequencing;
    private String fhaDnaMicroarray;
    private String fhaOther;
    private String fhaAntibodyDetectionDetails;
    private String fhaAntigenDetectionDetails;
    private String fhaRapidTestDetails;
    private String fhaCultureDetails;
    private String fhaHistopathologyDetails;
    private String fhaIsolationDetails;
    private String fhaIgmSerumAntibodyDetails;
    private String fhaIggSerumAntibodyDetails;
    private String fhaIgaSerumAntibodyDetails;
    private String fhaIncubationTimeDetails;
    private String fhaIndirectFluorescentAntibodyDetails;
    private String fhaDirectFluorescentAntibodyDetails;
    private String fhaMicroscopyDetails;
    private String fhaNeutralizingAntibodiesDetails;
    private String fhaPcrRtPcrDetails;
    private String fhaGramStainDetails;
    private String fhaLatexAgglutinationDetails;
    private String fhaCqValueDetectionDetails;
    private String fhaSequencingDetails;
    private String fhaDnaMicroarrayDetails;
    private String fhaOtherDetails;
    private String otherAntibodyDetection;
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
    private String otherDirectFluorescentAntibody;
    private String otherMicroscopy;
    private String otherNeutralizingAntibodies;
    private String otherPcrRtPcr;
    private String otherGramStain;
    private String otherLatexAgglutination;
    private String otherCqValueDetection;
    private String otherSequencing;
    private String otherDnaMicroarray;
    private String otherOther;
    private String otherAntibodyDetectionDetails;
    private String otherAntigenDetectionDetails;
    private String otherRapidTestDetails;
    private String otherCultureDetails;
    private String otherHistopathologyDetails;
    private String otherIsolationDetails;
    private String otherIgmSerumAntibodyDetails;
    private String otherIggSerumAntibodyDetails;
    private String otherIgaSerumAntibodyDetails;
    private String otherIncubationTimeDetails;
    private String otherIndirectFluorescentAntibodyDetails;
    private String otherDirectFluorescentAntibodyDetails;
    private String otherMicroscopyDetails;
    private String otherNeutralizingAntibodiesDetails;
    private String otherPcrRtPcrDetails;
    private String otherGramStainDetails;
    private String otherLatexAgglutinationDetails;
    private String otherCqValueDetectionDetails;
    private String otherSequencingDetails;
    private String otherDnaMicroarrayDetails;
    private String otherOtherDetails;
    private String undefinedAntibodyDetection;
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
    private String undefinedDirectFluorescentAntibody;
    private String undefinedMicroscopy;
    private String undefinedNeutralizingAntibodies;
    private String undefinedPcrRtPcr;
    private String undefinedGramStain;
    private String undefinedLatexAgglutination;
    private String undefinedCqValueDetection;
    private String undefinedSequencing;
    private String undefinedDnaMicroarray;
    private String undefinedOther;
    private String undefinedAntibodyDetectionDetails;
    private String undefinedAntigenDetectionDetails;
    private String undefinedRapidTestDetails;
    private String undefinedCultureDetails;
    private String undefinedHistopathologyDetails;
    private String undefinedIsolationDetails;
    private String undefinedIgmSerumAntibodyDetails;
    private String undefinedIggSerumAntibodyDetails;
    private String undefinedIgaSerumAntibodyDetails;
    private String undefinedIncubationTimeDetails;
    private String undefinedIndirectFluorescentAntibodyDetails;
    private String undefinedDirectFluorescentAntibodyDetails;
    private String undefinedMicroscopyDetails;
    private String undefinedNeutralizingAntibodiesDetails;
    private String undefinedPcrRtPcrDetails;
    private String undefinedGramStainDetails;
    private String undefinedLatexAgglutinationDetails;
    private String undefinedCqValueDetectionDetails;
    private String undefinedSequencingDetails;
    private String undefinedDnaMicroarrayDetails;
    private String undefinedOtherDetails;
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

    public void setAfpAntibodyDetection(String afpAntibodyDetection) {
        this.afpAntibodyDetection = afpAntibodyDetection;
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

    public void setAfpDirectFluorescentAntibody(String afpDirectFluorescentAntibody) {
        this.afpDirectFluorescentAntibody = afpDirectFluorescentAntibody;
    }

    public void setAfpMicroscopy(String afpMicroscopy) {
        this.afpMicroscopy = afpMicroscopy;
    }

    public void setAfpNeutralizingAntibodies(String afpNeutralizingAntibodies) {
        this.afpNeutralizingAntibodies = afpNeutralizingAntibodies;
    }

    public void setAfpPcrRtPcr(String afpPcrRtPcr) {
        this.afpPcrRtPcr = afpPcrRtPcr;
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

    public void setAfpSequencing(String afpSequencing) {
        this.afpSequencing = afpSequencing;
    }

    public void setAfpDnaMicroarray(String afpDnaMicroarray) {
        this.afpDnaMicroarray = afpDnaMicroarray;
    }

    public void setAfpOther(String afpOther) {
        this.afpOther = afpOther;
    }

    public void setAfpAntibodyDetectionDetails(String afpAntibodyDetectionDetails) {
        this.afpAntibodyDetectionDetails = afpAntibodyDetectionDetails;
    }

    public void setAfpAntigenDetectionDetails(String afpAntigenDetectionDetails) {
        this.afpAntigenDetectionDetails = afpAntigenDetectionDetails;
    }

    public void setAfpRapidTestDetails(String afpRapidTestDetails) {
        this.afpRapidTestDetails = afpRapidTestDetails;
    }

    public void setAfpCultureDetails(String afpCultureDetails) {
        this.afpCultureDetails = afpCultureDetails;
    }

    public void setAfpHistopathologyDetails(String afpHistopathologyDetails) {
        this.afpHistopathologyDetails = afpHistopathologyDetails;
    }

    public void setAfpIsolationDetails(String afpIsolationDetails) {
        this.afpIsolationDetails = afpIsolationDetails;
    }

    public void setAfpIgmSerumAntibodyDetails(String afpIgmSerumAntibodyDetails) {
        this.afpIgmSerumAntibodyDetails = afpIgmSerumAntibodyDetails;
    }

    public void setAfpIggSerumAntibodyDetails(String afpIggSerumAntibodyDetails) {
        this.afpIggSerumAntibodyDetails = afpIggSerumAntibodyDetails;
    }

    public void setAfpIgaSerumAntibodyDetails(String afpIgaSerumAntibodyDetails) {
        this.afpIgaSerumAntibodyDetails = afpIgaSerumAntibodyDetails;
    }

    public void setAfpIncubationTimeDetails(String afpIncubationTimeDetails) {
        this.afpIncubationTimeDetails = afpIncubationTimeDetails;
    }

    public void setAfpIndirectFluorescentAntibodyDetails(String afpIndirectFluorescentAntibodyDetails) {
        this.afpIndirectFluorescentAntibodyDetails = afpIndirectFluorescentAntibodyDetails;
    }

    public void setAfpDirectFluorescentAntibodyDetails(String afpDirectFluorescentAntibodyDetails) {
        this.afpDirectFluorescentAntibodyDetails = afpDirectFluorescentAntibodyDetails;
    }

    public void setAfpMicroscopyDetails(String afpMicroscopyDetails) {
        this.afpMicroscopyDetails = afpMicroscopyDetails;
    }

    public void setAfpNeutralizingAntibodiesDetails(String afpNeutralizingAntibodiesDetails) {
        this.afpNeutralizingAntibodiesDetails = afpNeutralizingAntibodiesDetails;
    }

    public void setAfpPcrRtPcrDetails(String afpPcrRtPcrDetails) {
        this.afpPcrRtPcrDetails = afpPcrRtPcrDetails;
    }

    public void setAfpGramStainDetails(String afpGramStainDetails) {
        this.afpGramStainDetails = afpGramStainDetails;
    }

    public void setAfpLatexAgglutinationDetails(String afpLatexAgglutinationDetails) {
        this.afpLatexAgglutinationDetails = afpLatexAgglutinationDetails;
    }

    public void setAfpCqValueDetectionDetails(String afpCqValueDetectionDetails) {
        this.afpCqValueDetectionDetails = afpCqValueDetectionDetails;
    }

    public void setAfpSequencingDetails(String afpSequencingDetails) {
        this.afpSequencingDetails = afpSequencingDetails;
    }

    public void setAfpDnaMicroarrayDetails(String afpDnaMicroarrayDetails) {
        this.afpDnaMicroarrayDetails = afpDnaMicroarrayDetails;
    }

    public void setAfpOtherDetails(String afpOtherDetails) {
        this.afpOtherDetails = afpOtherDetails;
    }

    public void setCholeraAntibodyDetection(String choleraAntibodyDetection) {
        this.choleraAntibodyDetection = choleraAntibodyDetection;
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

    public void setCholeraDirectFluorescentAntibody(String choleraDirectFluorescentAntibody) {
        this.choleraDirectFluorescentAntibody = choleraDirectFluorescentAntibody;
    }

    public void setCholeraMicroscopy(String choleraMicroscopy) {
        this.choleraMicroscopy = choleraMicroscopy;
    }

    public void setCholeraNeutralizingAntibodies(String choleraNeutralizingAntibodies) {
        this.choleraNeutralizingAntibodies = choleraNeutralizingAntibodies;
    }

    public void setCholeraPcrRtPcr(String choleraPcrRtPcr) {
        this.choleraPcrRtPcr = choleraPcrRtPcr;
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

    public void setCholeraSequencing(String choleraSequencing) {
        this.choleraSequencing = choleraSequencing;
    }

    public void setCholeraDnaMicroarray(String choleraDnaMicroarray) {
        this.choleraDnaMicroarray = choleraDnaMicroarray;
    }

    public void setCholeraOther(String choleraOther) {
        this.choleraOther = choleraOther;
    }

    public void setCholeraAntibodyDetectionDetails(String choleraAntibodyDetectionDetails) {
        this.choleraAntibodyDetectionDetails = choleraAntibodyDetectionDetails;
    }

    public void setCholeraAntigenDetectionDetails(String choleraAntigenDetectionDetails) {
        this.choleraAntigenDetectionDetails = choleraAntigenDetectionDetails;
    }

    public void setCholeraRapidTestDetails(String choleraRapidTestDetails) {
        this.choleraRapidTestDetails = choleraRapidTestDetails;
    }

    public void setCholeraCultureDetails(String choleraCultureDetails) {
        this.choleraCultureDetails = choleraCultureDetails;
    }

    public void setCholeraHistopathologyDetails(String choleraHistopathologyDetails) {
        this.choleraHistopathologyDetails = choleraHistopathologyDetails;
    }

    public void setCholeraIsolationDetails(String choleraIsolationDetails) {
        this.choleraIsolationDetails = choleraIsolationDetails;
    }

    public void setCholeraIgmSerumAntibodyDetails(String choleraIgmSerumAntibodyDetails) {
        this.choleraIgmSerumAntibodyDetails = choleraIgmSerumAntibodyDetails;
    }

    public void setCholeraIggSerumAntibodyDetails(String choleraIggSerumAntibodyDetails) {
        this.choleraIggSerumAntibodyDetails = choleraIggSerumAntibodyDetails;
    }

    public void setCholeraIgaSerumAntibodyDetails(String choleraIgaSerumAntibodyDetails) {
        this.choleraIgaSerumAntibodyDetails = choleraIgaSerumAntibodyDetails;
    }

    public void setCholeraIncubationTimeDetails(String choleraIncubationTimeDetails) {
        this.choleraIncubationTimeDetails = choleraIncubationTimeDetails;
    }

    public void setCholeraIndirectFluorescentAntibodyDetails(String choleraIndirectFluorescentAntibodyDetails) {
        this.choleraIndirectFluorescentAntibodyDetails = choleraIndirectFluorescentAntibodyDetails;
    }

    public void setCholeraDirectFluorescentAntibodyDetails(String choleraDirectFluorescentAntibodyDetails) {
        this.choleraDirectFluorescentAntibodyDetails = choleraDirectFluorescentAntibodyDetails;
    }

    public void setCholeraMicroscopyDetails(String choleraMicroscopyDetails) {
        this.choleraMicroscopyDetails = choleraMicroscopyDetails;
    }

    public void setCholeraNeutralizingAntibodiesDetails(String choleraNeutralizingAntibodiesDetails) {
        this.choleraNeutralizingAntibodiesDetails = choleraNeutralizingAntibodiesDetails;
    }

    public void setCholeraPcrRtPcrDetails(String choleraPcrRtPcrDetails) {
        this.choleraPcrRtPcrDetails = choleraPcrRtPcrDetails;
    }

    public void setCholeraGramStainDetails(String choleraGramStainDetails) {
        this.choleraGramStainDetails = choleraGramStainDetails;
    }

    public void setCholeraLatexAgglutinationDetails(String choleraLatexAgglutinationDetails) {
        this.choleraLatexAgglutinationDetails = choleraLatexAgglutinationDetails;
    }

    public void setCholeraCqValueDetectionDetails(String choleraCqValueDetectionDetails) {
        this.choleraCqValueDetectionDetails = choleraCqValueDetectionDetails;
    }

    public void setCholeraSequencingDetails(String choleraSequencingDetails) {
        this.choleraSequencingDetails = choleraSequencingDetails;
    }

    public void setCholeraDnaMicroarrayDetails(String choleraDnaMicroarrayDetails) {
        this.choleraDnaMicroarrayDetails = choleraDnaMicroarrayDetails;
    }

    public void setCholeraOtherDetails(String choleraOtherDetails) {
        this.choleraOtherDetails = choleraOtherDetails;
    }

    public void setCongenitalRubellaAntibodyDetection(String congenitalRubellaAntibodyDetection) {
        this.congenitalRubellaAntibodyDetection = congenitalRubellaAntibodyDetection;
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

    public void setCongenitalRubellaDirectFluorescentAntibody(String congenitalRubellaDirectFluorescentAntibody) {
        this.congenitalRubellaDirectFluorescentAntibody = congenitalRubellaDirectFluorescentAntibody;
    }

    public void setCongenitalRubellaMicroscopy(String congenitalRubellaMicroscopy) {
        this.congenitalRubellaMicroscopy = congenitalRubellaMicroscopy;
    }

    public void setCongenitalRubellaNeutralizingAntibodies(String congenitalRubellaNeutralizingAntibodies) {
        this.congenitalRubellaNeutralizingAntibodies = congenitalRubellaNeutralizingAntibodies;
    }

    public void setCongenitalRubellaPcrRtPcr(String congenitalRubellaPcrRtPcr) {
        this.congenitalRubellaPcrRtPcr = congenitalRubellaPcrRtPcr;
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

    public void setCongenitalRubellaSequencing(String congenitalRubellaSequencing) {
        this.congenitalRubellaSequencing = congenitalRubellaSequencing;
    }

    public void setCongenitalRubellaDnaMicroarray(String congenitalRubellaDnaMicroarray) {
        this.congenitalRubellaDnaMicroarray = congenitalRubellaDnaMicroarray;
    }

    public void setCongenitalRubellaOther(String congenitalRubellaOther) {
        this.congenitalRubellaOther = congenitalRubellaOther;
    }

    public void setCongenitalRubellaAntibodyDetectionDetails(String congenitalRubellaAntibodyDetectionDetails) {
        this.congenitalRubellaAntibodyDetectionDetails = congenitalRubellaAntibodyDetectionDetails;
    }

    public void setCongenitalRubellaAntigenDetectionDetails(String congenitalRubellaAntigenDetectionDetails) {
        this.congenitalRubellaAntigenDetectionDetails = congenitalRubellaAntigenDetectionDetails;
    }

    public void setCongenitalRubellaRapidTestDetails(String congenitalRubellaRapidTestDetails) {
        this.congenitalRubellaRapidTestDetails = congenitalRubellaRapidTestDetails;
    }

    public void setCongenitalRubellaCultureDetails(String congenitalRubellaCultureDetails) {
        this.congenitalRubellaCultureDetails = congenitalRubellaCultureDetails;
    }

    public void setCongenitalRubellaHistopathologyDetails(String congenitalRubellaHistopathologyDetails) {
        this.congenitalRubellaHistopathologyDetails = congenitalRubellaHistopathologyDetails;
    }

    public void setCongenitalRubellaIsolationDetails(String congenitalRubellaIsolationDetails) {
        this.congenitalRubellaIsolationDetails = congenitalRubellaIsolationDetails;
    }

    public void setCongenitalRubellaIgmSerumAntibodyDetails(String congenitalRubellaIgmSerumAntibodyDetails) {
        this.congenitalRubellaIgmSerumAntibodyDetails = congenitalRubellaIgmSerumAntibodyDetails;
    }

    public void setCongenitalRubellaIggSerumAntibodyDetails(String congenitalRubellaIggSerumAntibodyDetails) {
        this.congenitalRubellaIggSerumAntibodyDetails = congenitalRubellaIggSerumAntibodyDetails;
    }

    public void setCongenitalRubellaIgaSerumAntibodyDetails(String congenitalRubellaIgaSerumAntibodyDetails) {
        this.congenitalRubellaIgaSerumAntibodyDetails = congenitalRubellaIgaSerumAntibodyDetails;
    }

    public void setCongenitalRubellaIncubationTimeDetails(String congenitalRubellaIncubationTimeDetails) {
        this.congenitalRubellaIncubationTimeDetails = congenitalRubellaIncubationTimeDetails;
    }

    public void setCongenitalRubellaIndirectFluorescentAntibodyDetails(String congenitalRubellaIndirectFluorescentAntibodyDetails) {
        this.congenitalRubellaIndirectFluorescentAntibodyDetails = congenitalRubellaIndirectFluorescentAntibodyDetails;
    }

    public void setCongenitalRubellaDirectFluorescentAntibodyDetails(String congenitalRubellaDirectFluorescentAntibodyDetails) {
        this.congenitalRubellaDirectFluorescentAntibodyDetails = congenitalRubellaDirectFluorescentAntibodyDetails;
    }

    public void setCongenitalRubellaMicroscopyDetails(String congenitalRubellaMicroscopyDetails) {
        this.congenitalRubellaMicroscopyDetails = congenitalRubellaMicroscopyDetails;
    }

    public void setCongenitalRubellaNeutralizingAntibodiesDetails(String congenitalRubellaNeutralizingAntibodiesDetails) {
        this.congenitalRubellaNeutralizingAntibodiesDetails = congenitalRubellaNeutralizingAntibodiesDetails;
    }

    public void setCongenitalRubellaPcrRtPcrDetails(String congenitalRubellaPcrRtPcrDetails) {
        this.congenitalRubellaPcrRtPcrDetails = congenitalRubellaPcrRtPcrDetails;
    }

    public void setCongenitalRubellaGramStainDetails(String congenitalRubellaGramStainDetails) {
        this.congenitalRubellaGramStainDetails = congenitalRubellaGramStainDetails;
    }

    public void setCongenitalRubellaLatexAgglutinationDetails(String congenitalRubellaLatexAgglutinationDetails) {
        this.congenitalRubellaLatexAgglutinationDetails = congenitalRubellaLatexAgglutinationDetails;
    }

    public void setCongenitalRubellaCqValueDetectionDetails(String congenitalRubellaCqValueDetectionDetails) {
        this.congenitalRubellaCqValueDetectionDetails = congenitalRubellaCqValueDetectionDetails;
    }

    public void setCongenitalRubellaSequencingDetails(String congenitalRubellaSequencingDetails) {
        this.congenitalRubellaSequencingDetails = congenitalRubellaSequencingDetails;
    }

    public void setCongenitalRubellaDnaMicroarrayDetails(String congenitalRubellaDnaMicroarrayDetails) {
        this.congenitalRubellaDnaMicroarrayDetails = congenitalRubellaDnaMicroarrayDetails;
    }

    public void setCongenitalRubellaOtherDetails(String congenitalRubellaOtherDetails) {
        this.congenitalRubellaOtherDetails = congenitalRubellaOtherDetails;
    }

    public void setCsmAntibodyDetection(String csmAntibodyDetection) {
        this.csmAntibodyDetection = csmAntibodyDetection;
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

    public void setCsmDirectFluorescentAntibody(String csmDirectFluorescentAntibody) {
        this.csmDirectFluorescentAntibody = csmDirectFluorescentAntibody;
    }

    public void setCsmMicroscopy(String csmMicroscopy) {
        this.csmMicroscopy = csmMicroscopy;
    }

    public void setCsmNeutralizingAntibodies(String csmNeutralizingAntibodies) {
        this.csmNeutralizingAntibodies = csmNeutralizingAntibodies;
    }

    public void setCsmPcrRtPcr(String csmPcrRtPcr) {
        this.csmPcrRtPcr = csmPcrRtPcr;
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

    public void setCsmSequencing(String csmSequencing) {
        this.csmSequencing = csmSequencing;
    }

    public void setCsmDnaMicroarray(String csmDnaMicroarray) {
        this.csmDnaMicroarray = csmDnaMicroarray;
    }

    public void setCsmOther(String csmOther) {
        this.csmOther = csmOther;
    }

    public void setCsmAntibodyDetectionDetails(String csmAntibodyDetectionDetails) {
        this.csmAntibodyDetectionDetails = csmAntibodyDetectionDetails;
    }

    public void setCsmAntigenDetectionDetails(String csmAntigenDetectionDetails) {
        this.csmAntigenDetectionDetails = csmAntigenDetectionDetails;
    }

    public void setCsmRapidTestDetails(String csmRapidTestDetails) {
        this.csmRapidTestDetails = csmRapidTestDetails;
    }

    public void setCsmCultureDetails(String csmCultureDetails) {
        this.csmCultureDetails = csmCultureDetails;
    }

    public void setCsmHistopathologyDetails(String csmHistopathologyDetails) {
        this.csmHistopathologyDetails = csmHistopathologyDetails;
    }

    public void setCsmIsolationDetails(String csmIsolationDetails) {
        this.csmIsolationDetails = csmIsolationDetails;
    }

    public void setCsmIgmSerumAntibodyDetails(String csmIgmSerumAntibodyDetails) {
        this.csmIgmSerumAntibodyDetails = csmIgmSerumAntibodyDetails;
    }

    public void setCsmIggSerumAntibodyDetails(String csmIggSerumAntibodyDetails) {
        this.csmIggSerumAntibodyDetails = csmIggSerumAntibodyDetails;
    }

    public void setCsmIgaSerumAntibodyDetails(String csmIgaSerumAntibodyDetails) {
        this.csmIgaSerumAntibodyDetails = csmIgaSerumAntibodyDetails;
    }

    public void setCsmIncubationTimeDetails(String csmIncubationTimeDetails) {
        this.csmIncubationTimeDetails = csmIncubationTimeDetails;
    }

    public void setCsmIndirectFluorescentAntibodyDetails(String csmIndirectFluorescentAntibodyDetails) {
        this.csmIndirectFluorescentAntibodyDetails = csmIndirectFluorescentAntibodyDetails;
    }

    public void setCsmDirectFluorescentAntibodyDetails(String csmDirectFluorescentAntibodyDetails) {
        this.csmDirectFluorescentAntibodyDetails = csmDirectFluorescentAntibodyDetails;
    }

    public void setCsmMicroscopyDetails(String csmMicroscopyDetails) {
        this.csmMicroscopyDetails = csmMicroscopyDetails;
    }

    public void setCsmNeutralizingAntibodiesDetails(String csmNeutralizingAntibodiesDetails) {
        this.csmNeutralizingAntibodiesDetails = csmNeutralizingAntibodiesDetails;
    }

    public void setCsmPcrRtPcrDetails(String csmPcrRtPcrDetails) {
        this.csmPcrRtPcrDetails = csmPcrRtPcrDetails;
    }

    public void setCsmGramStainDetails(String csmGramStainDetails) {
        this.csmGramStainDetails = csmGramStainDetails;
    }

    public void setCsmLatexAgglutinationDetails(String csmLatexAgglutinationDetails) {
        this.csmLatexAgglutinationDetails = csmLatexAgglutinationDetails;
    }

    public void setCsmCqValueDetectionDetails(String csmCqValueDetectionDetails) {
        this.csmCqValueDetectionDetails = csmCqValueDetectionDetails;
    }

    public void setCsmSequencingDetails(String csmSequencingDetails) {
        this.csmSequencingDetails = csmSequencingDetails;
    }

    public void setCsmDnaMicroarrayDetails(String csmDnaMicroarrayDetails) {
        this.csmDnaMicroarrayDetails = csmDnaMicroarrayDetails;
    }

    public void setCsmOtherDetails(String csmOtherDetails) {
        this.csmOtherDetails = csmOtherDetails;
    }

    public void setDengueAntibodyDetection(String dengueAntibodyDetection) {
        this.dengueAntibodyDetection = dengueAntibodyDetection;
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

    public void setDengueDirectFluorescentAntibody(String dengueDirectFluorescentAntibody) {
        this.dengueDirectFluorescentAntibody = dengueDirectFluorescentAntibody;
    }

    public void setDengueMicroscopy(String dengueMicroscopy) {
        this.dengueMicroscopy = dengueMicroscopy;
    }

    public void setDengueNeutralizingAntibodies(String dengueNeutralizingAntibodies) {
        this.dengueNeutralizingAntibodies = dengueNeutralizingAntibodies;
    }

    public void setDenguePcrRtPcr(String denguePcrRtPcr) {
        this.denguePcrRtPcr = denguePcrRtPcr;
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

    public void setDengueSequencing(String dengueSequencing) {
        this.dengueSequencing = dengueSequencing;
    }

    public void setDengueDnaMicroarray(String dengueDnaMicroarray) {
        this.dengueDnaMicroarray = dengueDnaMicroarray;
    }

    public void setDengueOther(String dengueOther) {
        this.dengueOther = dengueOther;
    }

    public void setDengueAntibodyDetectionDetails(String dengueAntibodyDetectionDetails) {
        this.dengueAntibodyDetectionDetails = dengueAntibodyDetectionDetails;
    }

    public void setDengueAntigenDetectionDetails(String dengueAntigenDetectionDetails) {
        this.dengueAntigenDetectionDetails = dengueAntigenDetectionDetails;
    }

    public void setDengueRapidTestDetails(String dengueRapidTestDetails) {
        this.dengueRapidTestDetails = dengueRapidTestDetails;
    }

    public void setDengueCultureDetails(String dengueCultureDetails) {
        this.dengueCultureDetails = dengueCultureDetails;
    }

    public void setDengueHistopathologyDetails(String dengueHistopathologyDetails) {
        this.dengueHistopathologyDetails = dengueHistopathologyDetails;
    }

    public void setDengueIsolationDetails(String dengueIsolationDetails) {
        this.dengueIsolationDetails = dengueIsolationDetails;
    }

    public void setDengueIgmSerumAntibodyDetails(String dengueIgmSerumAntibodyDetails) {
        this.dengueIgmSerumAntibodyDetails = dengueIgmSerumAntibodyDetails;
    }

    public void setDengueIggSerumAntibodyDetails(String dengueIggSerumAntibodyDetails) {
        this.dengueIggSerumAntibodyDetails = dengueIggSerumAntibodyDetails;
    }

    public void setDengueIgaSerumAntibodyDetails(String dengueIgaSerumAntibodyDetails) {
        this.dengueIgaSerumAntibodyDetails = dengueIgaSerumAntibodyDetails;
    }

    public void setDengueIncubationTimeDetails(String dengueIncubationTimeDetails) {
        this.dengueIncubationTimeDetails = dengueIncubationTimeDetails;
    }

    public void setDengueIndirectFluorescentAntibodyDetails(String dengueIndirectFluorescentAntibodyDetails) {
        this.dengueIndirectFluorescentAntibodyDetails = dengueIndirectFluorescentAntibodyDetails;
    }

    public void setDengueDirectFluorescentAntibodyDetails(String dengueDirectFluorescentAntibodyDetails) {
        this.dengueDirectFluorescentAntibodyDetails = dengueDirectFluorescentAntibodyDetails;
    }

    public void setDengueMicroscopyDetails(String dengueMicroscopyDetails) {
        this.dengueMicroscopyDetails = dengueMicroscopyDetails;
    }

    public void setDengueNeutralizingAntibodiesDetails(String dengueNeutralizingAntibodiesDetails) {
        this.dengueNeutralizingAntibodiesDetails = dengueNeutralizingAntibodiesDetails;
    }

    public void setDenguePcrRtPcrDetails(String denguePcrRtPcrDetails) {
        this.denguePcrRtPcrDetails = denguePcrRtPcrDetails;
    }

    public void setDengueGramStainDetails(String dengueGramStainDetails) {
        this.dengueGramStainDetails = dengueGramStainDetails;
    }

    public void setDengueLatexAgglutinationDetails(String dengueLatexAgglutinationDetails) {
        this.dengueLatexAgglutinationDetails = dengueLatexAgglutinationDetails;
    }

    public void setDengueCqValueDetectionDetails(String dengueCqValueDetectionDetails) {
        this.dengueCqValueDetectionDetails = dengueCqValueDetectionDetails;
    }

    public void setDengueSequencingDetails(String dengueSequencingDetails) {
        this.dengueSequencingDetails = dengueSequencingDetails;
    }

    public void setDengueDnaMicroarrayDetails(String dengueDnaMicroarrayDetails) {
        this.dengueDnaMicroarrayDetails = dengueDnaMicroarrayDetails;
    }

    public void setDengueOtherDetails(String dengueOtherDetails) {
        this.dengueOtherDetails = dengueOtherDetails;
    }

    public void setEvdAntibodyDetection(String evdAntibodyDetection) {
        this.evdAntibodyDetection = evdAntibodyDetection;
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

    public void setEvdDirectFluorescentAntibody(String evdDirectFluorescentAntibody) {
        this.evdDirectFluorescentAntibody = evdDirectFluorescentAntibody;
    }

    public void setEvdMicroscopy(String evdMicroscopy) {
        this.evdMicroscopy = evdMicroscopy;
    }

    public void setEvdNeutralizingAntibodies(String evdNeutralizingAntibodies) {
        this.evdNeutralizingAntibodies = evdNeutralizingAntibodies;
    }

    public void setEvdPcrRtPcr(String evdPcrRtPcr) {
        this.evdPcrRtPcr = evdPcrRtPcr;
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

    public void setEvdSequencing(String evdSequencing) {
        this.evdSequencing = evdSequencing;
    }

    public void setEvdDnaMicroarray(String evdDnaMicroarray) {
        this.evdDnaMicroarray = evdDnaMicroarray;
    }

    public void setEvdOther(String evdOther) {
        this.evdOther = evdOther;
    }

    public void setEvdAntibodyDetectionDetails(String evdAntibodyDetectionDetails) {
        this.evdAntibodyDetectionDetails = evdAntibodyDetectionDetails;
    }

    public void setEvdAntigenDetectionDetails(String evdAntigenDetectionDetails) {
        this.evdAntigenDetectionDetails = evdAntigenDetectionDetails;
    }

    public void setEvdRapidTestDetails(String evdRapidTestDetails) {
        this.evdRapidTestDetails = evdRapidTestDetails;
    }

    public void setEvdCultureDetails(String evdCultureDetails) {
        this.evdCultureDetails = evdCultureDetails;
    }

    public void setEvdHistopathologyDetails(String evdHistopathologyDetails) {
        this.evdHistopathologyDetails = evdHistopathologyDetails;
    }

    public void setEvdIsolationDetails(String evdIsolationDetails) {
        this.evdIsolationDetails = evdIsolationDetails;
    }

    public void setEvdIgmSerumAntibodyDetails(String evdIgmSerumAntibodyDetails) {
        this.evdIgmSerumAntibodyDetails = evdIgmSerumAntibodyDetails;
    }

    public void setEvdIggSerumAntibodyDetails(String evdIggSerumAntibodyDetails) {
        this.evdIggSerumAntibodyDetails = evdIggSerumAntibodyDetails;
    }

    public void setEvdIgaSerumAntibodyDetails(String evdIgaSerumAntibodyDetails) {
        this.evdIgaSerumAntibodyDetails = evdIgaSerumAntibodyDetails;
    }

    public void setEvdIncubationTimeDetails(String evdIncubationTimeDetails) {
        this.evdIncubationTimeDetails = evdIncubationTimeDetails;
    }

    public void setEvdIndirectFluorescentAntibodyDetails(String evdIndirectFluorescentAntibodyDetails) {
        this.evdIndirectFluorescentAntibodyDetails = evdIndirectFluorescentAntibodyDetails;
    }

    public void setEvdDirectFluorescentAntibodyDetails(String evdDirectFluorescentAntibodyDetails) {
        this.evdDirectFluorescentAntibodyDetails = evdDirectFluorescentAntibodyDetails;
    }

    public void setEvdMicroscopyDetails(String evdMicroscopyDetails) {
        this.evdMicroscopyDetails = evdMicroscopyDetails;
    }

    public void setEvdNeutralizingAntibodiesDetails(String evdNeutralizingAntibodiesDetails) {
        this.evdNeutralizingAntibodiesDetails = evdNeutralizingAntibodiesDetails;
    }

    public void setEvdPcrRtPcrDetails(String evdPcrRtPcrDetails) {
        this.evdPcrRtPcrDetails = evdPcrRtPcrDetails;
    }

    public void setEvdGramStainDetails(String evdGramStainDetails) {
        this.evdGramStainDetails = evdGramStainDetails;
    }

    public void setEvdLatexAgglutinationDetails(String evdLatexAgglutinationDetails) {
        this.evdLatexAgglutinationDetails = evdLatexAgglutinationDetails;
    }

    public void setEvdCqValueDetectionDetails(String evdCqValueDetectionDetails) {
        this.evdCqValueDetectionDetails = evdCqValueDetectionDetails;
    }

    public void setEvdSequencingDetails(String evdSequencingDetails) {
        this.evdSequencingDetails = evdSequencingDetails;
    }

    public void setEvdDnaMicroarrayDetails(String evdDnaMicroarrayDetails) {
        this.evdDnaMicroarrayDetails = evdDnaMicroarrayDetails;
    }

    public void setEvdOtherDetails(String evdOtherDetails) {
        this.evdOtherDetails = evdOtherDetails;
    }

    public void setGuineaWormAntibodyDetection(String guineaWormAntibodyDetection) {
        this.guineaWormAntibodyDetection = guineaWormAntibodyDetection;
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

    public void setGuineaWormDirectFluorescentAntibody(String guineaWormDirectFluorescentAntibody) {
        this.guineaWormDirectFluorescentAntibody = guineaWormDirectFluorescentAntibody;
    }

    public void setGuineaWormMicroscopy(String guineaWormMicroscopy) {
        this.guineaWormMicroscopy = guineaWormMicroscopy;
    }

    public void setGuineaWormNeutralizingAntibodies(String guineaWormNeutralizingAntibodies) {
        this.guineaWormNeutralizingAntibodies = guineaWormNeutralizingAntibodies;
    }

    public void setGuineaWormPcrRtPcr(String guineaWormPcrRtPcr) {
        this.guineaWormPcrRtPcr = guineaWormPcrRtPcr;
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

    public void setGuineaWormSequencing(String guineaWormSequencing) {
        this.guineaWormSequencing = guineaWormSequencing;
    }

    public void setGuineaWormDnaMicroarray(String guineaWormDnaMicroarray) {
        this.guineaWormDnaMicroarray = guineaWormDnaMicroarray;
    }

    public void setGuineaWormOther(String guineaWormOther) {
        this.guineaWormOther = guineaWormOther;
    }

    public void setGuineaWormAntibodyDetectionDetails(String guineaWormAntibodyDetectionDetails) {
        this.guineaWormAntibodyDetectionDetails = guineaWormAntibodyDetectionDetails;
    }

    public void setGuineaWormAntigenDetectionDetails(String guineaWormAntigenDetectionDetails) {
        this.guineaWormAntigenDetectionDetails = guineaWormAntigenDetectionDetails;
    }

    public void setGuineaWormRapidTestDetails(String guineaWormRapidTestDetails) {
        this.guineaWormRapidTestDetails = guineaWormRapidTestDetails;
    }

    public void setGuineaWormCultureDetails(String guineaWormCultureDetails) {
        this.guineaWormCultureDetails = guineaWormCultureDetails;
    }

    public void setGuineaWormHistopathologyDetails(String guineaWormHistopathologyDetails) {
        this.guineaWormHistopathologyDetails = guineaWormHistopathologyDetails;
    }

    public void setGuineaWormIsolationDetails(String guineaWormIsolationDetails) {
        this.guineaWormIsolationDetails = guineaWormIsolationDetails;
    }

    public void setGuineaWormIgmSerumAntibodyDetails(String guineaWormIgmSerumAntibodyDetails) {
        this.guineaWormIgmSerumAntibodyDetails = guineaWormIgmSerumAntibodyDetails;
    }

    public void setGuineaWormIggSerumAntibodyDetails(String guineaWormIggSerumAntibodyDetails) {
        this.guineaWormIggSerumAntibodyDetails = guineaWormIggSerumAntibodyDetails;
    }

    public void setGuineaWormIgaSerumAntibodyDetails(String guineaWormIgaSerumAntibodyDetails) {
        this.guineaWormIgaSerumAntibodyDetails = guineaWormIgaSerumAntibodyDetails;
    }

    public void setGuineaWormIncubationTimeDetails(String guineaWormIncubationTimeDetails) {
        this.guineaWormIncubationTimeDetails = guineaWormIncubationTimeDetails;
    }

    public void setGuineaWormIndirectFluorescentAntibodyDetails(String guineaWormIndirectFluorescentAntibodyDetails) {
        this.guineaWormIndirectFluorescentAntibodyDetails = guineaWormIndirectFluorescentAntibodyDetails;
    }

    public void setGuineaWormDirectFluorescentAntibodyDetails(String guineaWormDirectFluorescentAntibodyDetails) {
        this.guineaWormDirectFluorescentAntibodyDetails = guineaWormDirectFluorescentAntibodyDetails;
    }

    public void setGuineaWormMicroscopyDetails(String guineaWormMicroscopyDetails) {
        this.guineaWormMicroscopyDetails = guineaWormMicroscopyDetails;
    }

    public void setGuineaWormNeutralizingAntibodiesDetails(String guineaWormNeutralizingAntibodiesDetails) {
        this.guineaWormNeutralizingAntibodiesDetails = guineaWormNeutralizingAntibodiesDetails;
    }

    public void setGuineaWormPcrRtPcrDetails(String guineaWormPcrRtPcrDetails) {
        this.guineaWormPcrRtPcrDetails = guineaWormPcrRtPcrDetails;
    }

    public void setGuineaWormGramStainDetails(String guineaWormGramStainDetails) {
        this.guineaWormGramStainDetails = guineaWormGramStainDetails;
    }

    public void setGuineaWormLatexAgglutinationDetails(String guineaWormLatexAgglutinationDetails) {
        this.guineaWormLatexAgglutinationDetails = guineaWormLatexAgglutinationDetails;
    }

    public void setGuineaWormCqValueDetectionDetails(String guineaWormCqValueDetectionDetails) {
        this.guineaWormCqValueDetectionDetails = guineaWormCqValueDetectionDetails;
    }

    public void setGuineaWormSequencingDetails(String guineaWormSequencingDetails) {
        this.guineaWormSequencingDetails = guineaWormSequencingDetails;
    }

    public void setGuineaWormDnaMicroarrayDetails(String guineaWormDnaMicroarrayDetails) {
        this.guineaWormDnaMicroarrayDetails = guineaWormDnaMicroarrayDetails;
    }

    public void setGuineaWormOtherDetails(String guineaWormOtherDetails) {
        this.guineaWormOtherDetails = guineaWormOtherDetails;
    }

    public void setLassaAntibodyDetection(String lassaAntibodyDetection) {
        this.lassaAntibodyDetection = lassaAntibodyDetection;
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

    public void setLassaDirectFluorescentAntibody(String lassaDirectFluorescentAntibody) {
        this.lassaDirectFluorescentAntibody = lassaDirectFluorescentAntibody;
    }

    public void setLassaMicroscopy(String lassaMicroscopy) {
        this.lassaMicroscopy = lassaMicroscopy;
    }

    public void setLassaNeutralizingAntibodies(String lassaNeutralizingAntibodies) {
        this.lassaNeutralizingAntibodies = lassaNeutralizingAntibodies;
    }

    public void setLassaPcrRtPcr(String lassaPcrRtPcr) {
        this.lassaPcrRtPcr = lassaPcrRtPcr;
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

    public void setLassaSequencing(String lassaSequencing) {
        this.lassaSequencing = lassaSequencing;
    }

    public void setLassaDnaMicroarray(String lassaDnaMicroarray) {
        this.lassaDnaMicroarray = lassaDnaMicroarray;
    }

    public void setLassaOther(String lassaOther) {
        this.lassaOther = lassaOther;
    }

    public void setLassaAntibodyDetectionDetails(String lassaAntibodyDetectionDetails) {
        this.lassaAntibodyDetectionDetails = lassaAntibodyDetectionDetails;
    }

    public void setLassaAntigenDetectionDetails(String lassaAntigenDetectionDetails) {
        this.lassaAntigenDetectionDetails = lassaAntigenDetectionDetails;
    }

    public void setLassaRapidTestDetails(String lassaRapidTestDetails) {
        this.lassaRapidTestDetails = lassaRapidTestDetails;
    }

    public void setLassaCultureDetails(String lassaCultureDetails) {
        this.lassaCultureDetails = lassaCultureDetails;
    }

    public void setLassaHistopathologyDetails(String lassaHistopathologyDetails) {
        this.lassaHistopathologyDetails = lassaHistopathologyDetails;
    }

    public void setLassaIsolationDetails(String lassaIsolationDetails) {
        this.lassaIsolationDetails = lassaIsolationDetails;
    }

    public void setLassaIgmSerumAntibodyDetails(String lassaIgmSerumAntibodyDetails) {
        this.lassaIgmSerumAntibodyDetails = lassaIgmSerumAntibodyDetails;
    }

    public void setLassaIggSerumAntibodyDetails(String lassaIggSerumAntibodyDetails) {
        this.lassaIggSerumAntibodyDetails = lassaIggSerumAntibodyDetails;
    }

    public void setLassaIgaSerumAntibodyDetails(String lassaIgaSerumAntibodyDetails) {
        this.lassaIgaSerumAntibodyDetails = lassaIgaSerumAntibodyDetails;
    }

    public void setLassaIncubationTimeDetails(String lassaIncubationTimeDetails) {
        this.lassaIncubationTimeDetails = lassaIncubationTimeDetails;
    }

    public void setLassaIndirectFluorescentAntibodyDetails(String lassaIndirectFluorescentAntibodyDetails) {
        this.lassaIndirectFluorescentAntibodyDetails = lassaIndirectFluorescentAntibodyDetails;
    }

    public void setLassaDirectFluorescentAntibodyDetails(String lassaDirectFluorescentAntibodyDetails) {
        this.lassaDirectFluorescentAntibodyDetails = lassaDirectFluorescentAntibodyDetails;
    }

    public void setLassaMicroscopyDetails(String lassaMicroscopyDetails) {
        this.lassaMicroscopyDetails = lassaMicroscopyDetails;
    }

    public void setLassaNeutralizingAntibodiesDetails(String lassaNeutralizingAntibodiesDetails) {
        this.lassaNeutralizingAntibodiesDetails = lassaNeutralizingAntibodiesDetails;
    }

    public void setLassaPcrRtPcrDetails(String lassaPcrRtPcrDetails) {
        this.lassaPcrRtPcrDetails = lassaPcrRtPcrDetails;
    }

    public void setLassaGramStainDetails(String lassaGramStainDetails) {
        this.lassaGramStainDetails = lassaGramStainDetails;
    }

    public void setLassaLatexAgglutinationDetails(String lassaLatexAgglutinationDetails) {
        this.lassaLatexAgglutinationDetails = lassaLatexAgglutinationDetails;
    }

    public void setLassaCqValueDetectionDetails(String lassaCqValueDetectionDetails) {
        this.lassaCqValueDetectionDetails = lassaCqValueDetectionDetails;
    }

    public void setLassaSequencingDetails(String lassaSequencingDetails) {
        this.lassaSequencingDetails = lassaSequencingDetails;
    }

    public void setLassaDnaMicroarrayDetails(String lassaDnaMicroarrayDetails) {
        this.lassaDnaMicroarrayDetails = lassaDnaMicroarrayDetails;
    }

    public void setLassaOtherDetails(String lassaOtherDetails) {
        this.lassaOtherDetails = lassaOtherDetails;
    }

    public void setMeaslesAntibodyDetection(String measlesAntibodyDetection) {
        this.measlesAntibodyDetection = measlesAntibodyDetection;
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

    public void setMeaslesDirectFluorescentAntibody(String measlesDirectFluorescentAntibody) {
        this.measlesDirectFluorescentAntibody = measlesDirectFluorescentAntibody;
    }

    public void setMeaslesMicroscopy(String measlesMicroscopy) {
        this.measlesMicroscopy = measlesMicroscopy;
    }

    public void setMeaslesNeutralizingAntibodies(String measlesNeutralizingAntibodies) {
        this.measlesNeutralizingAntibodies = measlesNeutralizingAntibodies;
    }

    public void setMeaslesPcrRtPcr(String measlesPcrRtPcr) {
        this.measlesPcrRtPcr = measlesPcrRtPcr;
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

    public void setMeaslesSequencing(String measlesSequencing) {
        this.measlesSequencing = measlesSequencing;
    }

    public void setMeaslesDnaMicroarray(String measlesDnaMicroarray) {
        this.measlesDnaMicroarray = measlesDnaMicroarray;
    }

    public void setMeaslesOther(String measlesOther) {
        this.measlesOther = measlesOther;
    }

    public void setMeaslesAntibodyDetectionDetails(String measlesAntibodyDetectionDetails) {
        this.measlesAntibodyDetectionDetails = measlesAntibodyDetectionDetails;
    }

    public void setMeaslesAntigenDetectionDetails(String measlesAntigenDetectionDetails) {
        this.measlesAntigenDetectionDetails = measlesAntigenDetectionDetails;
    }

    public void setMeaslesRapidTestDetails(String measlesRapidTestDetails) {
        this.measlesRapidTestDetails = measlesRapidTestDetails;
    }

    public void setMeaslesCultureDetails(String measlesCultureDetails) {
        this.measlesCultureDetails = measlesCultureDetails;
    }

    public void setMeaslesHistopathologyDetails(String measlesHistopathologyDetails) {
        this.measlesHistopathologyDetails = measlesHistopathologyDetails;
    }

    public void setMeaslesIsolationDetails(String measlesIsolationDetails) {
        this.measlesIsolationDetails = measlesIsolationDetails;
    }

    public void setMeaslesIgmSerumAntibodyDetails(String measlesIgmSerumAntibodyDetails) {
        this.measlesIgmSerumAntibodyDetails = measlesIgmSerumAntibodyDetails;
    }

    public void setMeaslesIggSerumAntibodyDetails(String measlesIggSerumAntibodyDetails) {
        this.measlesIggSerumAntibodyDetails = measlesIggSerumAntibodyDetails;
    }

    public void setMeaslesIgaSerumAntibodyDetails(String measlesIgaSerumAntibodyDetails) {
        this.measlesIgaSerumAntibodyDetails = measlesIgaSerumAntibodyDetails;
    }

    public void setMeaslesIncubationTimeDetails(String measlesIncubationTimeDetails) {
        this.measlesIncubationTimeDetails = measlesIncubationTimeDetails;
    }

    public void setMeaslesIndirectFluorescentAntibodyDetails(String measlesIndirectFluorescentAntibodyDetails) {
        this.measlesIndirectFluorescentAntibodyDetails = measlesIndirectFluorescentAntibodyDetails;
    }

    public void setMeaslesDirectFluorescentAntibodyDetails(String measlesDirectFluorescentAntibodyDetails) {
        this.measlesDirectFluorescentAntibodyDetails = measlesDirectFluorescentAntibodyDetails;
    }

    public void setMeaslesMicroscopyDetails(String measlesMicroscopyDetails) {
        this.measlesMicroscopyDetails = measlesMicroscopyDetails;
    }

    public void setMeaslesNeutralizingAntibodiesDetails(String measlesNeutralizingAntibodiesDetails) {
        this.measlesNeutralizingAntibodiesDetails = measlesNeutralizingAntibodiesDetails;
    }

    public void setMeaslesPcrRtPcrDetails(String measlesPcrRtPcrDetails) {
        this.measlesPcrRtPcrDetails = measlesPcrRtPcrDetails;
    }

    public void setMeaslesGramStainDetails(String measlesGramStainDetails) {
        this.measlesGramStainDetails = measlesGramStainDetails;
    }

    public void setMeaslesLatexAgglutinationDetails(String measlesLatexAgglutinationDetails) {
        this.measlesLatexAgglutinationDetails = measlesLatexAgglutinationDetails;
    }

    public void setMeaslesCqValueDetectionDetails(String measlesCqValueDetectionDetails) {
        this.measlesCqValueDetectionDetails = measlesCqValueDetectionDetails;
    }

    public void setMeaslesSequencingDetails(String measlesSequencingDetails) {
        this.measlesSequencingDetails = measlesSequencingDetails;
    }

    public void setMeaslesDnaMicroarrayDetails(String measlesDnaMicroarrayDetails) {
        this.measlesDnaMicroarrayDetails = measlesDnaMicroarrayDetails;
    }

    public void setMeaslesOtherDetails(String measlesOtherDetails) {
        this.measlesOtherDetails = measlesOtherDetails;
    }

    public void setMonkeypoxAntibodyDetection(String monkeypoxAntibodyDetection) {
        this.monkeypoxAntibodyDetection = monkeypoxAntibodyDetection;
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

    public void setMonkeypoxDirectFluorescentAntibody(String monkeypoxDirectFluorescentAntibody) {
        this.monkeypoxDirectFluorescentAntibody = monkeypoxDirectFluorescentAntibody;
    }

    public void setMonkeypoxMicroscopy(String monkeypoxMicroscopy) {
        this.monkeypoxMicroscopy = monkeypoxMicroscopy;
    }

    public void setMonkeypoxNeutralizingAntibodies(String monkeypoxNeutralizingAntibodies) {
        this.monkeypoxNeutralizingAntibodies = monkeypoxNeutralizingAntibodies;
    }

    public void setMonkeypoxPcrRtPcr(String monkeypoxPcrRtPcr) {
        this.monkeypoxPcrRtPcr = monkeypoxPcrRtPcr;
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

    public void setMonkeypoxSequencing(String monkeypoxSequencing) {
        this.monkeypoxSequencing = monkeypoxSequencing;
    }

    public void setMonkeypoxDnaMicroarray(String monkeypoxDnaMicroarray) {
        this.monkeypoxDnaMicroarray = monkeypoxDnaMicroarray;
    }

    public void setMonkeypoxOther(String monkeypoxOther) {
        this.monkeypoxOther = monkeypoxOther;
    }

    public void setMonkeypoxAntibodyDetectionDetails(String monkeypoxAntibodyDetectionDetails) {
        this.monkeypoxAntibodyDetectionDetails = monkeypoxAntibodyDetectionDetails;
    }

    public void setMonkeypoxAntigenDetectionDetails(String monkeypoxAntigenDetectionDetails) {
        this.monkeypoxAntigenDetectionDetails = monkeypoxAntigenDetectionDetails;
    }

    public void setMonkeypoxRapidTestDetails(String monkeypoxRapidTestDetails) {
        this.monkeypoxRapidTestDetails = monkeypoxRapidTestDetails;
    }

    public void setMonkeypoxCultureDetails(String monkeypoxCultureDetails) {
        this.monkeypoxCultureDetails = monkeypoxCultureDetails;
    }

    public void setMonkeypoxHistopathologyDetails(String monkeypoxHistopathologyDetails) {
        this.monkeypoxHistopathologyDetails = monkeypoxHistopathologyDetails;
    }

    public void setMonkeypoxIsolationDetails(String monkeypoxIsolationDetails) {
        this.monkeypoxIsolationDetails = monkeypoxIsolationDetails;
    }

    public void setMonkeypoxIgmSerumAntibodyDetails(String monkeypoxIgmSerumAntibodyDetails) {
        this.monkeypoxIgmSerumAntibodyDetails = monkeypoxIgmSerumAntibodyDetails;
    }

    public void setMonkeypoxIggSerumAntibodyDetails(String monkeypoxIggSerumAntibodyDetails) {
        this.monkeypoxIggSerumAntibodyDetails = monkeypoxIggSerumAntibodyDetails;
    }

    public void setMonkeypoxIgaSerumAntibodyDetails(String monkeypoxIgaSerumAntibodyDetails) {
        this.monkeypoxIgaSerumAntibodyDetails = monkeypoxIgaSerumAntibodyDetails;
    }

    public void setMonkeypoxIncubationTimeDetails(String monkeypoxIncubationTimeDetails) {
        this.monkeypoxIncubationTimeDetails = monkeypoxIncubationTimeDetails;
    }

    public void setMonkeypoxIndirectFluorescentAntibodyDetails(String monkeypoxIndirectFluorescentAntibodyDetails) {
        this.monkeypoxIndirectFluorescentAntibodyDetails = monkeypoxIndirectFluorescentAntibodyDetails;
    }

    public void setMonkeypoxDirectFluorescentAntibodyDetails(String monkeypoxDirectFluorescentAntibodyDetails) {
        this.monkeypoxDirectFluorescentAntibodyDetails = monkeypoxDirectFluorescentAntibodyDetails;
    }

    public void setMonkeypoxMicroscopyDetails(String monkeypoxMicroscopyDetails) {
        this.monkeypoxMicroscopyDetails = monkeypoxMicroscopyDetails;
    }

    public void setMonkeypoxNeutralizingAntibodiesDetails(String monkeypoxNeutralizingAntibodiesDetails) {
        this.monkeypoxNeutralizingAntibodiesDetails = monkeypoxNeutralizingAntibodiesDetails;
    }

    public void setMonkeypoxPcrRtPcrDetails(String monkeypoxPcrRtPcrDetails) {
        this.monkeypoxPcrRtPcrDetails = monkeypoxPcrRtPcrDetails;
    }

    public void setMonkeypoxGramStainDetails(String monkeypoxGramStainDetails) {
        this.monkeypoxGramStainDetails = monkeypoxGramStainDetails;
    }

    public void setMonkeypoxLatexAgglutinationDetails(String monkeypoxLatexAgglutinationDetails) {
        this.monkeypoxLatexAgglutinationDetails = monkeypoxLatexAgglutinationDetails;
    }

    public void setMonkeypoxCqValueDetectionDetails(String monkeypoxCqValueDetectionDetails) {
        this.monkeypoxCqValueDetectionDetails = monkeypoxCqValueDetectionDetails;
    }

    public void setMonkeypoxSequencingDetails(String monkeypoxSequencingDetails) {
        this.monkeypoxSequencingDetails = monkeypoxSequencingDetails;
    }

    public void setMonkeypoxDnaMicroarrayDetails(String monkeypoxDnaMicroarrayDetails) {
        this.monkeypoxDnaMicroarrayDetails = monkeypoxDnaMicroarrayDetails;
    }

    public void setMonkeypoxOtherDetails(String monkeypoxOtherDetails) {
        this.monkeypoxOtherDetails = monkeypoxOtherDetails;
    }

    public void setNewInfluenzaAntibodyDetection(String newInfluenzaAntibodyDetection) {
        this.newInfluenzaAntibodyDetection = newInfluenzaAntibodyDetection;
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

    public void setNewInfluenzaDirectFluorescentAntibody(String newInfluenzaDirectFluorescentAntibody) {
        this.newInfluenzaDirectFluorescentAntibody = newInfluenzaDirectFluorescentAntibody;
    }

    public void setNewInfluenzaMicroscopy(String newInfluenzaMicroscopy) {
        this.newInfluenzaMicroscopy = newInfluenzaMicroscopy;
    }

    public void setNewInfluenzaNeutralizingAntibodies(String newInfluenzaNeutralizingAntibodies) {
        this.newInfluenzaNeutralizingAntibodies = newInfluenzaNeutralizingAntibodies;
    }

    public void setNewInfluenzaPcrRtPcr(String newInfluenzaPcrRtPcr) {
        this.newInfluenzaPcrRtPcr = newInfluenzaPcrRtPcr;
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

    public void setNewInfluenzaSequencing(String newInfluenzaSequencing) {
        this.newInfluenzaSequencing = newInfluenzaSequencing;
    }

    public void setNewInfluenzaDnaMicroarray(String newInfluenzaDnaMicroarray) {
        this.newInfluenzaDnaMicroarray = newInfluenzaDnaMicroarray;
    }

    public void setNewInfluenzaOther(String newInfluenzaOther) {
        this.newInfluenzaOther = newInfluenzaOther;
    }

    public void setNewInfluenzaAntibodyDetectionDetails(String newInfluenzaAntibodyDetectionDetails) {
        this.newInfluenzaAntibodyDetectionDetails = newInfluenzaAntibodyDetectionDetails;
    }

    public void setNewInfluenzaAntigenDetectionDetails(String newInfluenzaAntigenDetectionDetails) {
        this.newInfluenzaAntigenDetectionDetails = newInfluenzaAntigenDetectionDetails;
    }

    public void setNewInfluenzaRapidTestDetails(String newInfluenzaRapidTestDetails) {
        this.newInfluenzaRapidTestDetails = newInfluenzaRapidTestDetails;
    }

    public void setNewInfluenzaCultureDetails(String newInfluenzaCultureDetails) {
        this.newInfluenzaCultureDetails = newInfluenzaCultureDetails;
    }

    public void setNewInfluenzaHistopathologyDetails(String newInfluenzaHistopathologyDetails) {
        this.newInfluenzaHistopathologyDetails = newInfluenzaHistopathologyDetails;
    }

    public void setNewInfluenzaIsolationDetails(String newInfluenzaIsolationDetails) {
        this.newInfluenzaIsolationDetails = newInfluenzaIsolationDetails;
    }

    public void setNewInfluenzaIgmSerumAntibodyDetails(String newInfluenzaIgmSerumAntibodyDetails) {
        this.newInfluenzaIgmSerumAntibodyDetails = newInfluenzaIgmSerumAntibodyDetails;
    }

    public void setNewInfluenzaIggSerumAntibodyDetails(String newInfluenzaIggSerumAntibodyDetails) {
        this.newInfluenzaIggSerumAntibodyDetails = newInfluenzaIggSerumAntibodyDetails;
    }

    public void setNewInfluenzaIgaSerumAntibodyDetails(String newInfluenzaIgaSerumAntibodyDetails) {
        this.newInfluenzaIgaSerumAntibodyDetails = newInfluenzaIgaSerumAntibodyDetails;
    }

    public void setNewInfluenzaIncubationTimeDetails(String newInfluenzaIncubationTimeDetails) {
        this.newInfluenzaIncubationTimeDetails = newInfluenzaIncubationTimeDetails;
    }

    public void setNewInfluenzaIndirectFluorescentAntibodyDetails(String newInfluenzaIndirectFluorescentAntibodyDetails) {
        this.newInfluenzaIndirectFluorescentAntibodyDetails = newInfluenzaIndirectFluorescentAntibodyDetails;
    }

    public void setNewInfluenzaDirectFluorescentAntibodyDetails(String newInfluenzaDirectFluorescentAntibodyDetails) {
        this.newInfluenzaDirectFluorescentAntibodyDetails = newInfluenzaDirectFluorescentAntibodyDetails;
    }

    public void setNewInfluenzaMicroscopyDetails(String newInfluenzaMicroscopyDetails) {
        this.newInfluenzaMicroscopyDetails = newInfluenzaMicroscopyDetails;
    }

    public void setNewInfluenzaNeutralizingAntibodiesDetails(String newInfluenzaNeutralizingAntibodiesDetails) {
        this.newInfluenzaNeutralizingAntibodiesDetails = newInfluenzaNeutralizingAntibodiesDetails;
    }

    public void setNewInfluenzaPcrRtPcrDetails(String newInfluenzaPcrRtPcrDetails) {
        this.newInfluenzaPcrRtPcrDetails = newInfluenzaPcrRtPcrDetails;
    }

    public void setNewInfluenzaGramStainDetails(String newInfluenzaGramStainDetails) {
        this.newInfluenzaGramStainDetails = newInfluenzaGramStainDetails;
    }

    public void setNewInfluenzaLatexAgglutinationDetails(String newInfluenzaLatexAgglutinationDetails) {
        this.newInfluenzaLatexAgglutinationDetails = newInfluenzaLatexAgglutinationDetails;
    }

    public void setNewInfluenzaCqValueDetectionDetails(String newInfluenzaCqValueDetectionDetails) {
        this.newInfluenzaCqValueDetectionDetails = newInfluenzaCqValueDetectionDetails;
    }

    public void setNewInfluenzaSequencingDetails(String newInfluenzaSequencingDetails) {
        this.newInfluenzaSequencingDetails = newInfluenzaSequencingDetails;
    }

    public void setNewInfluenzaDnaMicroarrayDetails(String newInfluenzaDnaMicroarrayDetails) {
        this.newInfluenzaDnaMicroarrayDetails = newInfluenzaDnaMicroarrayDetails;
    }

    public void setNewInfluenzaOtherDetails(String newInfluenzaOtherDetails) {
        this.newInfluenzaOtherDetails = newInfluenzaOtherDetails;
    }

    public void setPlagueAntibodyDetection(String plagueAntibodyDetection) {
        this.plagueAntibodyDetection = plagueAntibodyDetection;
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

    public void setPlagueDirectFluorescentAntibody(String plagueDirectFluorescentAntibody) {
        this.plagueDirectFluorescentAntibody = plagueDirectFluorescentAntibody;
    }

    public void setPlagueMicroscopy(String plagueMicroscopy) {
        this.plagueMicroscopy = plagueMicroscopy;
    }

    public void setPlagueNeutralizingAntibodies(String plagueNeutralizingAntibodies) {
        this.plagueNeutralizingAntibodies = plagueNeutralizingAntibodies;
    }

    public void setPlaguePcrRtPcr(String plaguePcrRtPcr) {
        this.plaguePcrRtPcr = plaguePcrRtPcr;
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

    public void setPlagueSequencing(String plagueSequencing) {
        this.plagueSequencing = plagueSequencing;
    }

    public void setPlagueDnaMicroarray(String plagueDnaMicroarray) {
        this.plagueDnaMicroarray = plagueDnaMicroarray;
    }

    public void setPlagueOther(String plagueOther) {
        this.plagueOther = plagueOther;
    }

    public void setPlagueAntibodyDetectionDetails(String plagueAntibodyDetectionDetails) {
        this.plagueAntibodyDetectionDetails = plagueAntibodyDetectionDetails;
    }

    public void setPlagueAntigenDetectionDetails(String plagueAntigenDetectionDetails) {
        this.plagueAntigenDetectionDetails = plagueAntigenDetectionDetails;
    }

    public void setPlagueRapidTestDetails(String plagueRapidTestDetails) {
        this.plagueRapidTestDetails = plagueRapidTestDetails;
    }

    public void setPlagueCultureDetails(String plagueCultureDetails) {
        this.plagueCultureDetails = plagueCultureDetails;
    }

    public void setPlagueHistopathologyDetails(String plagueHistopathologyDetails) {
        this.plagueHistopathologyDetails = plagueHistopathologyDetails;
    }

    public void setPlagueIsolationDetails(String plagueIsolationDetails) {
        this.plagueIsolationDetails = plagueIsolationDetails;
    }

    public void setPlagueIgmSerumAntibodyDetails(String plagueIgmSerumAntibodyDetails) {
        this.plagueIgmSerumAntibodyDetails = plagueIgmSerumAntibodyDetails;
    }

    public void setPlagueIggSerumAntibodyDetails(String plagueIggSerumAntibodyDetails) {
        this.plagueIggSerumAntibodyDetails = plagueIggSerumAntibodyDetails;
    }

    public void setPlagueIgaSerumAntibodyDetails(String plagueIgaSerumAntibodyDetails) {
        this.plagueIgaSerumAntibodyDetails = plagueIgaSerumAntibodyDetails;
    }

    public void setPlagueIncubationTimeDetails(String plagueIncubationTimeDetails) {
        this.plagueIncubationTimeDetails = plagueIncubationTimeDetails;
    }

    public void setPlagueIndirectFluorescentAntibodyDetails(String plagueIndirectFluorescentAntibodyDetails) {
        this.plagueIndirectFluorescentAntibodyDetails = plagueIndirectFluorescentAntibodyDetails;
    }

    public void setPlagueDirectFluorescentAntibodyDetails(String plagueDirectFluorescentAntibodyDetails) {
        this.plagueDirectFluorescentAntibodyDetails = plagueDirectFluorescentAntibodyDetails;
    }

    public void setPlagueMicroscopyDetails(String plagueMicroscopyDetails) {
        this.plagueMicroscopyDetails = plagueMicroscopyDetails;
    }

    public void setPlagueNeutralizingAntibodiesDetails(String plagueNeutralizingAntibodiesDetails) {
        this.plagueNeutralizingAntibodiesDetails = plagueNeutralizingAntibodiesDetails;
    }

    public void setPlaguePcrRtPcrDetails(String plaguePcrRtPcrDetails) {
        this.plaguePcrRtPcrDetails = plaguePcrRtPcrDetails;
    }

    public void setPlagueGramStainDetails(String plagueGramStainDetails) {
        this.plagueGramStainDetails = plagueGramStainDetails;
    }

    public void setPlagueLatexAgglutinationDetails(String plagueLatexAgglutinationDetails) {
        this.plagueLatexAgglutinationDetails = plagueLatexAgglutinationDetails;
    }

    public void setPlagueCqValueDetectionDetails(String plagueCqValueDetectionDetails) {
        this.plagueCqValueDetectionDetails = plagueCqValueDetectionDetails;
    }

    public void setPlagueSequencingDetails(String plagueSequencingDetails) {
        this.plagueSequencingDetails = plagueSequencingDetails;
    }

    public void setPlagueDnaMicroarrayDetails(String plagueDnaMicroarrayDetails) {
        this.plagueDnaMicroarrayDetails = plagueDnaMicroarrayDetails;
    }

    public void setPlagueOtherDetails(String plagueOtherDetails) {
        this.plagueOtherDetails = plagueOtherDetails;
    }

    public void setPolioAntibodyDetection(String polioAntibodyDetection) {
        this.polioAntibodyDetection = polioAntibodyDetection;
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

    public void setPolioDirectFluorescentAntibody(String polioDirectFluorescentAntibody) {
        this.polioDirectFluorescentAntibody = polioDirectFluorescentAntibody;
    }

    public void setPolioMicroscopy(String polioMicroscopy) {
        this.polioMicroscopy = polioMicroscopy;
    }

    public void setPolioNeutralizingAntibodies(String polioNeutralizingAntibodies) {
        this.polioNeutralizingAntibodies = polioNeutralizingAntibodies;
    }

    public void setPolioPcrRtPcr(String polioPcrRtPcr) {
        this.polioPcrRtPcr = polioPcrRtPcr;
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

    public void setPolioSequencing(String polioSequencing) {
        this.polioSequencing = polioSequencing;
    }

    public void setPolioDnaMicroarray(String polioDnaMicroarray) {
        this.polioDnaMicroarray = polioDnaMicroarray;
    }

    public void setPolioOther(String polioOther) {
        this.polioOther = polioOther;
    }

    public void setPolioAntibodyDetectionDetails(String polioAntibodyDetectionDetails) {
        this.polioAntibodyDetectionDetails = polioAntibodyDetectionDetails;
    }

    public void setPolioAntigenDetectionDetails(String polioAntigenDetectionDetails) {
        this.polioAntigenDetectionDetails = polioAntigenDetectionDetails;
    }

    public void setPolioRapidTestDetails(String polioRapidTestDetails) {
        this.polioRapidTestDetails = polioRapidTestDetails;
    }

    public void setPolioCultureDetails(String polioCultureDetails) {
        this.polioCultureDetails = polioCultureDetails;
    }

    public void setPolioHistopathologyDetails(String polioHistopathologyDetails) {
        this.polioHistopathologyDetails = polioHistopathologyDetails;
    }

    public void setPolioIsolationDetails(String polioIsolationDetails) {
        this.polioIsolationDetails = polioIsolationDetails;
    }

    public void setPolioIgmSerumAntibodyDetails(String polioIgmSerumAntibodyDetails) {
        this.polioIgmSerumAntibodyDetails = polioIgmSerumAntibodyDetails;
    }

    public void setPolioIggSerumAntibodyDetails(String polioIggSerumAntibodyDetails) {
        this.polioIggSerumAntibodyDetails = polioIggSerumAntibodyDetails;
    }

    public void setPolioIgaSerumAntibodyDetails(String polioIgaSerumAntibodyDetails) {
        this.polioIgaSerumAntibodyDetails = polioIgaSerumAntibodyDetails;
    }

    public void setPolioIncubationTimeDetails(String polioIncubationTimeDetails) {
        this.polioIncubationTimeDetails = polioIncubationTimeDetails;
    }

    public void setPolioIndirectFluorescentAntibodyDetails(String polioIndirectFluorescentAntibodyDetails) {
        this.polioIndirectFluorescentAntibodyDetails = polioIndirectFluorescentAntibodyDetails;
    }

    public void setPolioDirectFluorescentAntibodyDetails(String polioDirectFluorescentAntibodyDetails) {
        this.polioDirectFluorescentAntibodyDetails = polioDirectFluorescentAntibodyDetails;
    }

    public void setPolioMicroscopyDetails(String polioMicroscopyDetails) {
        this.polioMicroscopyDetails = polioMicroscopyDetails;
    }

    public void setPolioNeutralizingAntibodiesDetails(String polioNeutralizingAntibodiesDetails) {
        this.polioNeutralizingAntibodiesDetails = polioNeutralizingAntibodiesDetails;
    }

    public void setPolioPcrRtPcrDetails(String polioPcrRtPcrDetails) {
        this.polioPcrRtPcrDetails = polioPcrRtPcrDetails;
    }

    public void setPolioGramStainDetails(String polioGramStainDetails) {
        this.polioGramStainDetails = polioGramStainDetails;
    }

    public void setPolioLatexAgglutinationDetails(String polioLatexAgglutinationDetails) {
        this.polioLatexAgglutinationDetails = polioLatexAgglutinationDetails;
    }

    public void setPolioCqValueDetectionDetails(String polioCqValueDetectionDetails) {
        this.polioCqValueDetectionDetails = polioCqValueDetectionDetails;
    }

    public void setPolioSequencingDetails(String polioSequencingDetails) {
        this.polioSequencingDetails = polioSequencingDetails;
    }

    public void setPolioDnaMicroarrayDetails(String polioDnaMicroarrayDetails) {
        this.polioDnaMicroarrayDetails = polioDnaMicroarrayDetails;
    }

    public void setPolioOtherDetails(String polioOtherDetails) {
        this.polioOtherDetails = polioOtherDetails;
    }

    public void setUnspecifiedVhfAntibodyDetection(String unspecifiedVhfAntibodyDetection) {
        this.unspecifiedVhfAntibodyDetection = unspecifiedVhfAntibodyDetection;
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

    public void setUnspecifiedVhfDirectFluorescentAntibody(String unspecifiedVhfDirectFluorescentAntibody) {
        this.unspecifiedVhfDirectFluorescentAntibody = unspecifiedVhfDirectFluorescentAntibody;
    }

    public void setUnspecifiedVhfMicroscopy(String unspecifiedVhfMicroscopy) {
        this.unspecifiedVhfMicroscopy = unspecifiedVhfMicroscopy;
    }

    public void setUnspecifiedVhfNeutralizingAntibodies(String unspecifiedVhfNeutralizingAntibodies) {
        this.unspecifiedVhfNeutralizingAntibodies = unspecifiedVhfNeutralizingAntibodies;
    }

    public void setUnspecifiedVhfPcrRtPcr(String unspecifiedVhfPcrRtPcr) {
        this.unspecifiedVhfPcrRtPcr = unspecifiedVhfPcrRtPcr;
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

    public void setUnspecifiedVhfSequencing(String unspecifiedVhfSequencing) {
        this.unspecifiedVhfSequencing = unspecifiedVhfSequencing;
    }

    public void setUnspecifiedVhfDnaMicroarray(String unspecifiedVhfDnaMicroarray) {
        this.unspecifiedVhfDnaMicroarray = unspecifiedVhfDnaMicroarray;
    }

    public void setUnspecifiedVhfOther(String unspecifiedVhfOther) {
        this.unspecifiedVhfOther = unspecifiedVhfOther;
    }

    public void setUnspecifiedVhfAntibodyDetectionDetails(String unspecifiedVhfAntibodyDetectionDetails) {
        this.unspecifiedVhfAntibodyDetectionDetails = unspecifiedVhfAntibodyDetectionDetails;
    }

    public void setUnspecifiedVhfAntigenDetectionDetails(String unspecifiedVhfAntigenDetectionDetails) {
        this.unspecifiedVhfAntigenDetectionDetails = unspecifiedVhfAntigenDetectionDetails;
    }

    public void setUnspecifiedVhfRapidTestDetails(String unspecifiedVhfRapidTestDetails) {
        this.unspecifiedVhfRapidTestDetails = unspecifiedVhfRapidTestDetails;
    }

    public void setUnspecifiedVhfCultureDetails(String unspecifiedVhfCultureDetails) {
        this.unspecifiedVhfCultureDetails = unspecifiedVhfCultureDetails;
    }

    public void setUnspecifiedVhfHistopathologyDetails(String unspecifiedVhfHistopathologyDetails) {
        this.unspecifiedVhfHistopathologyDetails = unspecifiedVhfHistopathologyDetails;
    }

    public void setUnspecifiedVhfIsolationDetails(String unspecifiedVhfIsolationDetails) {
        this.unspecifiedVhfIsolationDetails = unspecifiedVhfIsolationDetails;
    }

    public void setUnspecifiedVhfIgmSerumAntibodyDetails(String unspecifiedVhfIgmSerumAntibodyDetails) {
        this.unspecifiedVhfIgmSerumAntibodyDetails = unspecifiedVhfIgmSerumAntibodyDetails;
    }

    public void setUnspecifiedVhfIggSerumAntibodyDetails(String unspecifiedVhfIggSerumAntibodyDetails) {
        this.unspecifiedVhfIggSerumAntibodyDetails = unspecifiedVhfIggSerumAntibodyDetails;
    }

    public void setUnspecifiedVhfIgaSerumAntibodyDetails(String unspecifiedVhfIgaSerumAntibodyDetails) {
        this.unspecifiedVhfIgaSerumAntibodyDetails = unspecifiedVhfIgaSerumAntibodyDetails;
    }

    public void setUnspecifiedVhfIncubationTimeDetails(String unspecifiedVhfIncubationTimeDetails) {
        this.unspecifiedVhfIncubationTimeDetails = unspecifiedVhfIncubationTimeDetails;
    }

    public void setUnspecifiedVhfIndirectFluorescentAntibodyDetails(String unspecifiedVhfIndirectFluorescentAntibodyDetails) {
        this.unspecifiedVhfIndirectFluorescentAntibodyDetails = unspecifiedVhfIndirectFluorescentAntibodyDetails;
    }

    public void setUnspecifiedVhfDirectFluorescentAntibodyDetails(String unspecifiedVhfDirectFluorescentAntibodyDetails) {
        this.unspecifiedVhfDirectFluorescentAntibodyDetails = unspecifiedVhfDirectFluorescentAntibodyDetails;
    }

    public void setUnspecifiedVhfMicroscopyDetails(String unspecifiedVhfMicroscopyDetails) {
        this.unspecifiedVhfMicroscopyDetails = unspecifiedVhfMicroscopyDetails;
    }

    public void setUnspecifiedVhfNeutralizingAntibodiesDetails(String unspecifiedVhfNeutralizingAntibodiesDetails) {
        this.unspecifiedVhfNeutralizingAntibodiesDetails = unspecifiedVhfNeutralizingAntibodiesDetails;
    }

    public void setUnspecifiedVhfPcrRtPcrDetails(String unspecifiedVhfPcrRtPcrDetails) {
        this.unspecifiedVhfPcrRtPcrDetails = unspecifiedVhfPcrRtPcrDetails;
    }

    public void setUnspecifiedVhfGramStainDetails(String unspecifiedVhfGramStainDetails) {
        this.unspecifiedVhfGramStainDetails = unspecifiedVhfGramStainDetails;
    }

    public void setUnspecifiedVhfLatexAgglutinationDetails(String unspecifiedVhfLatexAgglutinationDetails) {
        this.unspecifiedVhfLatexAgglutinationDetails = unspecifiedVhfLatexAgglutinationDetails;
    }

    public void setUnspecifiedVhfCqValueDetectionDetails(String unspecifiedVhfCqValueDetectionDetails) {
        this.unspecifiedVhfCqValueDetectionDetails = unspecifiedVhfCqValueDetectionDetails;
    }

    public void setUnspecifiedVhfSequencingDetails(String unspecifiedVhfSequencingDetails) {
        this.unspecifiedVhfSequencingDetails = unspecifiedVhfSequencingDetails;
    }

    public void setUnspecifiedVhfDnaMicroarrayDetails(String unspecifiedVhfDnaMicroarrayDetails) {
        this.unspecifiedVhfDnaMicroarrayDetails = unspecifiedVhfDnaMicroarrayDetails;
    }

    public void setUnspecifiedVhfOtherDetails(String unspecifiedVhfOtherDetails) {
        this.unspecifiedVhfOtherDetails = unspecifiedVhfOtherDetails;
    }

    public void setWestNileFeverAntibodyDetection(String westNileFeverAntibodyDetection) {
        this.westNileFeverAntibodyDetection = westNileFeverAntibodyDetection;
    }

    public void setWestNileFeverAntigenDetection(String westNileFeverAntigenDetection) {
        this.westNileFeverAntigenDetection = westNileFeverAntigenDetection;
    }

    public void setWestNileFeverRapidTest(String westNileFeverRapidTest) {
        this.westNileFeverRapidTest = westNileFeverRapidTest;
    }

    public void setWestNileFeverCulture(String westNileFeverCulture) {
        this.westNileFeverCulture = westNileFeverCulture;
    }

    public void setWestNileFeverHistopathology(String westNileFeverHistopathology) {
        this.westNileFeverHistopathology = westNileFeverHistopathology;
    }

    public void setWestNileFeverIsolation(String westNileFeverIsolation) {
        this.westNileFeverIsolation = westNileFeverIsolation;
    }

    public void setWestNileFeverIgmSerumAntibody(String westNileFeverIgmSerumAntibody) {
        this.westNileFeverIgmSerumAntibody = westNileFeverIgmSerumAntibody;
    }

    public void setWestNileFeverIggSerumAntibody(String westNileFeverIggSerumAntibody) {
        this.westNileFeverIggSerumAntibody = westNileFeverIggSerumAntibody;
    }

    public void setWestNileFeverIgaSerumAntibody(String westNileFeverIgaSerumAntibody) {
        this.westNileFeverIgaSerumAntibody = westNileFeverIgaSerumAntibody;
    }

    public void setWestNileFeverIncubationTime(String westNileFeverIncubationTime) {
        this.westNileFeverIncubationTime = westNileFeverIncubationTime;
    }

    public void setWestNileFeverIndirectFluorescentAntibody(String westNileFeverIndirectFluorescentAntibody) {
        this.westNileFeverIndirectFluorescentAntibody = westNileFeverIndirectFluorescentAntibody;
    }

    public void setWestNileFeverDirectFluorescentAntibody(String westNileFeverDirectFluorescentAntibody) {
        this.westNileFeverDirectFluorescentAntibody = westNileFeverDirectFluorescentAntibody;
    }

    public void setWestNileFeverMicroscopy(String westNileFeverMicroscopy) {
        this.westNileFeverMicroscopy = westNileFeverMicroscopy;
    }

    public void setWestNileFeverNeutralizingAntibodies(String westNileFeverNeutralizingAntibodies) {
        this.westNileFeverNeutralizingAntibodies = westNileFeverNeutralizingAntibodies;
    }

    public void setWestNileFeverPcrRtPcr(String westNileFeverPcrRtPcr) {
        this.westNileFeverPcrRtPcr = westNileFeverPcrRtPcr;
    }

    public void setWestNileFeverGramStain(String westNileFeverGramStain) {
        this.westNileFeverGramStain = westNileFeverGramStain;
    }

    public void setWestNileFeverLatexAgglutination(String westNileFeverLatexAgglutination) {
        this.westNileFeverLatexAgglutination = westNileFeverLatexAgglutination;
    }

    public void setWestNileFeverCqValueDetection(String westNileFeverCqValueDetection) {
        this.westNileFeverCqValueDetection = westNileFeverCqValueDetection;
    }

    public void setWestNileFeverSequencing(String westNileFeverSequencing) {
        this.westNileFeverSequencing = westNileFeverSequencing;
    }

    public void setWestNileFeverDnaMicroarray(String westNileFeverDnaMicroarray) {
        this.westNileFeverDnaMicroarray = westNileFeverDnaMicroarray;
    }

    public void setWestNileFeverOther(String westNileFeverOther) {
        this.westNileFeverOther = westNileFeverOther;
    }

    public void setWestNileFeverAntibodyDetectionDetails(String westNileFeverAntibodyDetectionDetails) {
        this.westNileFeverAntibodyDetectionDetails = westNileFeverAntibodyDetectionDetails;
    }

    public void setWestNileFeverAntigenDetectionDetails(String westNileFeverAntigenDetectionDetails) {
        this.westNileFeverAntigenDetectionDetails = westNileFeverAntigenDetectionDetails;
    }

    public void setWestNileFeverRapidTestDetails(String westNileFeverRapidTestDetails) {
        this.westNileFeverRapidTestDetails = westNileFeverRapidTestDetails;
    }

    public void setWestNileFeverCultureDetails(String westNileFeverCultureDetails) {
        this.westNileFeverCultureDetails = westNileFeverCultureDetails;
    }

    public void setWestNileFeverHistopathologyDetails(String westNileFeverHistopathologyDetails) {
        this.westNileFeverHistopathologyDetails = westNileFeverHistopathologyDetails;
    }

    public void setWestNileFeverIsolationDetails(String westNileFeverIsolationDetails) {
        this.westNileFeverIsolationDetails = westNileFeverIsolationDetails;
    }

    public void setWestNileFeverIgmSerumAntibodyDetails(String westNileFeverIgmSerumAntibodyDetails) {
        this.westNileFeverIgmSerumAntibodyDetails = westNileFeverIgmSerumAntibodyDetails;
    }

    public void setWestNileFeverIggSerumAntibodyDetails(String westNileFeverIggSerumAntibodyDetails) {
        this.westNileFeverIggSerumAntibodyDetails = westNileFeverIggSerumAntibodyDetails;
    }

    public void setWestNileFeverIgaSerumAntibodyDetails(String westNileFeverIgaSerumAntibodyDetails) {
        this.westNileFeverIgaSerumAntibodyDetails = westNileFeverIgaSerumAntibodyDetails;
    }

    public void setWestNileFeverIncubationTimeDetails(String westNileFeverIncubationTimeDetails) {
        this.westNileFeverIncubationTimeDetails = westNileFeverIncubationTimeDetails;
    }

    public void setWestNileFeverIndirectFluorescentAntibodyDetails(String westNileFeverIndirectFluorescentAntibodyDetails) {
        this.westNileFeverIndirectFluorescentAntibodyDetails = westNileFeverIndirectFluorescentAntibodyDetails;
    }

    public void setWestNileFeverDirectFluorescentAntibodyDetails(String westNileFeverDirectFluorescentAntibodyDetails) {
        this.westNileFeverDirectFluorescentAntibodyDetails = westNileFeverDirectFluorescentAntibodyDetails;
    }

    public void setWestNileFeverMicroscopyDetails(String westNileFeverMicroscopyDetails) {
        this.westNileFeverMicroscopyDetails = westNileFeverMicroscopyDetails;
    }

    public void setWestNileFeverNeutralizingAntibodiesDetails(String westNileFeverNeutralizingAntibodiesDetails) {
        this.westNileFeverNeutralizingAntibodiesDetails = westNileFeverNeutralizingAntibodiesDetails;
    }

    public void setWestNileFeverPcrRtPcrDetails(String westNileFeverPcrRtPcrDetails) {
        this.westNileFeverPcrRtPcrDetails = westNileFeverPcrRtPcrDetails;
    }

    public void setWestNileFeverGramStainDetails(String westNileFeverGramStainDetails) {
        this.westNileFeverGramStainDetails = westNileFeverGramStainDetails;
    }

    public void setWestNileFeverLatexAgglutinationDetails(String westNileFeverLatexAgglutinationDetails) {
        this.westNileFeverLatexAgglutinationDetails = westNileFeverLatexAgglutinationDetails;
    }

    public void setWestNileFeverCqValueDetectionDetails(String westNileFeverCqValueDetectionDetails) {
        this.westNileFeverCqValueDetectionDetails = westNileFeverCqValueDetectionDetails;
    }

    public void setWestNileFeverSequencingDetails(String westNileFeverSequencingDetails) {
        this.westNileFeverSequencingDetails = westNileFeverSequencingDetails;
    }

    public void setWestNileFeverDnaMicroarrayDetails(String westNileFeverDnaMicroarrayDetails) {
        this.westNileFeverDnaMicroarrayDetails = westNileFeverDnaMicroarrayDetails;
    }

    public void setWestNileFeverOtherDetails(String westNileFeverOtherDetails) {
        this.westNileFeverOtherDetails = westNileFeverOtherDetails;
    }

    public void setYellowFeverAntibodyDetection(String yellowFeverAntibodyDetection) {
        this.yellowFeverAntibodyDetection = yellowFeverAntibodyDetection;
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

    public void setYellowFeverDirectFluorescentAntibody(String yellowFeverDirectFluorescentAntibody) {
        this.yellowFeverDirectFluorescentAntibody = yellowFeverDirectFluorescentAntibody;
    }

    public void setYellowFeverMicroscopy(String yellowFeverMicroscopy) {
        this.yellowFeverMicroscopy = yellowFeverMicroscopy;
    }

    public void setYellowFeverNeutralizingAntibodies(String yellowFeverNeutralizingAntibodies) {
        this.yellowFeverNeutralizingAntibodies = yellowFeverNeutralizingAntibodies;
    }

    public void setYellowFeverPcrRtPcr(String yellowFeverPcrRtPcr) {
        this.yellowFeverPcrRtPcr = yellowFeverPcrRtPcr;
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

    public void setYellowFeverSequencing(String yellowFeverSequencing) {
        this.yellowFeverSequencing = yellowFeverSequencing;
    }

    public void setYellowFeverDnaMicroarray(String yellowFeverDnaMicroarray) {
        this.yellowFeverDnaMicroarray = yellowFeverDnaMicroarray;
    }

    public void setYellowFeverOther(String yellowFeverOther) {
        this.yellowFeverOther = yellowFeverOther;
    }

    public void setYellowFeverAntibodyDetectionDetails(String yellowFeverAntibodyDetectionDetails) {
        this.yellowFeverAntibodyDetectionDetails = yellowFeverAntibodyDetectionDetails;
    }

    public void setYellowFeverAntigenDetectionDetails(String yellowFeverAntigenDetectionDetails) {
        this.yellowFeverAntigenDetectionDetails = yellowFeverAntigenDetectionDetails;
    }

    public void setYellowFeverRapidTestDetails(String yellowFeverRapidTestDetails) {
        this.yellowFeverRapidTestDetails = yellowFeverRapidTestDetails;
    }

    public void setYellowFeverCultureDetails(String yellowFeverCultureDetails) {
        this.yellowFeverCultureDetails = yellowFeverCultureDetails;
    }

    public void setYellowFeverHistopathologyDetails(String yellowFeverHistopathologyDetails) {
        this.yellowFeverHistopathologyDetails = yellowFeverHistopathologyDetails;
    }

    public void setYellowFeverIsolationDetails(String yellowFeverIsolationDetails) {
        this.yellowFeverIsolationDetails = yellowFeverIsolationDetails;
    }

    public void setYellowFeverIgmSerumAntibodyDetails(String yellowFeverIgmSerumAntibodyDetails) {
        this.yellowFeverIgmSerumAntibodyDetails = yellowFeverIgmSerumAntibodyDetails;
    }

    public void setYellowFeverIggSerumAntibodyDetails(String yellowFeverIggSerumAntibodyDetails) {
        this.yellowFeverIggSerumAntibodyDetails = yellowFeverIggSerumAntibodyDetails;
    }

    public void setYellowFeverIgaSerumAntibodyDetails(String yellowFeverIgaSerumAntibodyDetails) {
        this.yellowFeverIgaSerumAntibodyDetails = yellowFeverIgaSerumAntibodyDetails;
    }

    public void setYellowFeverIncubationTimeDetails(String yellowFeverIncubationTimeDetails) {
        this.yellowFeverIncubationTimeDetails = yellowFeverIncubationTimeDetails;
    }

    public void setYellowFeverIndirectFluorescentAntibodyDetails(String yellowFeverIndirectFluorescentAntibodyDetails) {
        this.yellowFeverIndirectFluorescentAntibodyDetails = yellowFeverIndirectFluorescentAntibodyDetails;
    }

    public void setYellowFeverDirectFluorescentAntibodyDetails(String yellowFeverDirectFluorescentAntibodyDetails) {
        this.yellowFeverDirectFluorescentAntibodyDetails = yellowFeverDirectFluorescentAntibodyDetails;
    }

    public void setYellowFeverMicroscopyDetails(String yellowFeverMicroscopyDetails) {
        this.yellowFeverMicroscopyDetails = yellowFeverMicroscopyDetails;
    }

    public void setYellowFeverNeutralizingAntibodiesDetails(String yellowFeverNeutralizingAntibodiesDetails) {
        this.yellowFeverNeutralizingAntibodiesDetails = yellowFeverNeutralizingAntibodiesDetails;
    }

    public void setYellowFeverPcrRtPcrDetails(String yellowFeverPcrRtPcrDetails) {
        this.yellowFeverPcrRtPcrDetails = yellowFeverPcrRtPcrDetails;
    }

    public void setYellowFeverGramStainDetails(String yellowFeverGramStainDetails) {
        this.yellowFeverGramStainDetails = yellowFeverGramStainDetails;
    }

    public void setYellowFeverLatexAgglutinationDetails(String yellowFeverLatexAgglutinationDetails) {
        this.yellowFeverLatexAgglutinationDetails = yellowFeverLatexAgglutinationDetails;
    }

    public void setYellowFeverCqValueDetectionDetails(String yellowFeverCqValueDetectionDetails) {
        this.yellowFeverCqValueDetectionDetails = yellowFeverCqValueDetectionDetails;
    }

    public void setYellowFeverSequencingDetails(String yellowFeverSequencingDetails) {
        this.yellowFeverSequencingDetails = yellowFeverSequencingDetails;
    }

    public void setYellowFeverDnaMicroarrayDetails(String yellowFeverDnaMicroarrayDetails) {
        this.yellowFeverDnaMicroarrayDetails = yellowFeverDnaMicroarrayDetails;
    }

    public void setYellowFeverOtherDetails(String yellowFeverOtherDetails) {
        this.yellowFeverOtherDetails = yellowFeverOtherDetails;
    }

    public void setRabiesAntibodyDetection(String rabiesAntibodyDetection) {
        this.rabiesAntibodyDetection = rabiesAntibodyDetection;
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

    public void setRabiesDirectFluorescentAntibody(String rabiesDirectFluorescentAntibody) {
        this.rabiesDirectFluorescentAntibody = rabiesDirectFluorescentAntibody;
    }

    public void setRabiesMicroscopy(String rabiesMicroscopy) {
        this.rabiesMicroscopy = rabiesMicroscopy;
    }

    public void setRabiesNeutralizingAntibodies(String rabiesNeutralizingAntibodies) {
        this.rabiesNeutralizingAntibodies = rabiesNeutralizingAntibodies;
    }

    public void setRabiesPcrRtPcr(String rabiesPcrRtPcr) {
        this.rabiesPcrRtPcr = rabiesPcrRtPcr;
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

    public void setRabiesSequencing(String rabiesSequencing) {
        this.rabiesSequencing = rabiesSequencing;
    }

    public void setRabiesDnaMicroarray(String rabiesDnaMicroarray) {
        this.rabiesDnaMicroarray = rabiesDnaMicroarray;
    }

    public void setRabiesOther(String rabiesOther) {
        this.rabiesOther = rabiesOther;
    }

    public void setRabiesAntibodyDetectionDetails(String rabiesAntibodyDetectionDetails) {
        this.rabiesAntibodyDetectionDetails = rabiesAntibodyDetectionDetails;
    }

    public void setRabiesAntigenDetectionDetails(String rabiesAntigenDetectionDetails) {
        this.rabiesAntigenDetectionDetails = rabiesAntigenDetectionDetails;
    }

    public void setRabiesRapidTestDetails(String rabiesRapidTestDetails) {
        this.rabiesRapidTestDetails = rabiesRapidTestDetails;
    }

    public void setRabiesCultureDetails(String rabiesCultureDetails) {
        this.rabiesCultureDetails = rabiesCultureDetails;
    }

    public void setRabiesHistopathologyDetails(String rabiesHistopathologyDetails) {
        this.rabiesHistopathologyDetails = rabiesHistopathologyDetails;
    }

    public void setRabiesIsolationDetails(String rabiesIsolationDetails) {
        this.rabiesIsolationDetails = rabiesIsolationDetails;
    }

    public void setRabiesIgmSerumAntibodyDetails(String rabiesIgmSerumAntibodyDetails) {
        this.rabiesIgmSerumAntibodyDetails = rabiesIgmSerumAntibodyDetails;
    }

    public void setRabiesIggSerumAntibodyDetails(String rabiesIggSerumAntibodyDetails) {
        this.rabiesIggSerumAntibodyDetails = rabiesIggSerumAntibodyDetails;
    }

    public void setRabiesIgaSerumAntibodyDetails(String rabiesIgaSerumAntibodyDetails) {
        this.rabiesIgaSerumAntibodyDetails = rabiesIgaSerumAntibodyDetails;
    }

    public void setRabiesIncubationTimeDetails(String rabiesIncubationTimeDetails) {
        this.rabiesIncubationTimeDetails = rabiesIncubationTimeDetails;
    }

    public void setRabiesIndirectFluorescentAntibodyDetails(String rabiesIndirectFluorescentAntibodyDetails) {
        this.rabiesIndirectFluorescentAntibodyDetails = rabiesIndirectFluorescentAntibodyDetails;
    }

    public void setRabiesDirectFluorescentAntibodyDetails(String rabiesDirectFluorescentAntibodyDetails) {
        this.rabiesDirectFluorescentAntibodyDetails = rabiesDirectFluorescentAntibodyDetails;
    }

    public void setRabiesMicroscopyDetails(String rabiesMicroscopyDetails) {
        this.rabiesMicroscopyDetails = rabiesMicroscopyDetails;
    }

    public void setRabiesNeutralizingAntibodiesDetails(String rabiesNeutralizingAntibodiesDetails) {
        this.rabiesNeutralizingAntibodiesDetails = rabiesNeutralizingAntibodiesDetails;
    }

    public void setRabiesPcrRtPcrDetails(String rabiesPcrRtPcrDetails) {
        this.rabiesPcrRtPcrDetails = rabiesPcrRtPcrDetails;
    }

    public void setRabiesGramStainDetails(String rabiesGramStainDetails) {
        this.rabiesGramStainDetails = rabiesGramStainDetails;
    }

    public void setRabiesLatexAgglutinationDetails(String rabiesLatexAgglutinationDetails) {
        this.rabiesLatexAgglutinationDetails = rabiesLatexAgglutinationDetails;
    }

    public void setRabiesCqValueDetectionDetails(String rabiesCqValueDetectionDetails) {
        this.rabiesCqValueDetectionDetails = rabiesCqValueDetectionDetails;
    }

    public void setRabiesSequencingDetails(String rabiesSequencingDetails) {
        this.rabiesSequencingDetails = rabiesSequencingDetails;
    }

    public void setRabiesDnaMicroarrayDetails(String rabiesDnaMicroarrayDetails) {
        this.rabiesDnaMicroarrayDetails = rabiesDnaMicroarrayDetails;
    }

    public void setRabiesOtherDetails(String rabiesOtherDetails) {
        this.rabiesOtherDetails = rabiesOtherDetails;
    }

    public void setAnthraxAntibodyDetection(String anthraxAntibodyDetection) {
        this.anthraxAntibodyDetection = anthraxAntibodyDetection;
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

    public void setAnthraxDirectFluorescentAntibody(String anthraxDirectFluorescentAntibody) {
        this.anthraxDirectFluorescentAntibody = anthraxDirectFluorescentAntibody;
    }

    public void setAnthraxMicroscopy(String anthraxMicroscopy) {
        this.anthraxMicroscopy = anthraxMicroscopy;
    }

    public void setAnthraxNeutralizingAntibodies(String anthraxNeutralizingAntibodies) {
        this.anthraxNeutralizingAntibodies = anthraxNeutralizingAntibodies;
    }

    public void setAnthraxPcrRtPcr(String anthraxPcrRtPcr) {
        this.anthraxPcrRtPcr = anthraxPcrRtPcr;
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

    public void setAnthraxSequencing(String anthraxSequencing) {
        this.anthraxSequencing = anthraxSequencing;
    }

    public void setAnthraxDnaMicroarray(String anthraxDnaMicroarray) {
        this.anthraxDnaMicroarray = anthraxDnaMicroarray;
    }

    public void setAnthraxOther(String anthraxOther) {
        this.anthraxOther = anthraxOther;
    }

    public void setAnthraxAntibodyDetectionDetails(String anthraxAntibodyDetectionDetails) {
        this.anthraxAntibodyDetectionDetails = anthraxAntibodyDetectionDetails;
    }

    public void setAnthraxAntigenDetectionDetails(String anthraxAntigenDetectionDetails) {
        this.anthraxAntigenDetectionDetails = anthraxAntigenDetectionDetails;
    }

    public void setAnthraxRapidTestDetails(String anthraxRapidTestDetails) {
        this.anthraxRapidTestDetails = anthraxRapidTestDetails;
    }

    public void setAnthraxCultureDetails(String anthraxCultureDetails) {
        this.anthraxCultureDetails = anthraxCultureDetails;
    }

    public void setAnthraxHistopathologyDetails(String anthraxHistopathologyDetails) {
        this.anthraxHistopathologyDetails = anthraxHistopathologyDetails;
    }

    public void setAnthraxIsolationDetails(String anthraxIsolationDetails) {
        this.anthraxIsolationDetails = anthraxIsolationDetails;
    }

    public void setAnthraxIgmSerumAntibodyDetails(String anthraxIgmSerumAntibodyDetails) {
        this.anthraxIgmSerumAntibodyDetails = anthraxIgmSerumAntibodyDetails;
    }

    public void setAnthraxIggSerumAntibodyDetails(String anthraxIggSerumAntibodyDetails) {
        this.anthraxIggSerumAntibodyDetails = anthraxIggSerumAntibodyDetails;
    }

    public void setAnthraxIgaSerumAntibodyDetails(String anthraxIgaSerumAntibodyDetails) {
        this.anthraxIgaSerumAntibodyDetails = anthraxIgaSerumAntibodyDetails;
    }

    public void setAnthraxIncubationTimeDetails(String anthraxIncubationTimeDetails) {
        this.anthraxIncubationTimeDetails = anthraxIncubationTimeDetails;
    }

    public void setAnthraxIndirectFluorescentAntibodyDetails(String anthraxIndirectFluorescentAntibodyDetails) {
        this.anthraxIndirectFluorescentAntibodyDetails = anthraxIndirectFluorescentAntibodyDetails;
    }

    public void setAnthraxDirectFluorescentAntibodyDetails(String anthraxDirectFluorescentAntibodyDetails) {
        this.anthraxDirectFluorescentAntibodyDetails = anthraxDirectFluorescentAntibodyDetails;
    }

    public void setAnthraxMicroscopyDetails(String anthraxMicroscopyDetails) {
        this.anthraxMicroscopyDetails = anthraxMicroscopyDetails;
    }

    public void setAnthraxNeutralizingAntibodiesDetails(String anthraxNeutralizingAntibodiesDetails) {
        this.anthraxNeutralizingAntibodiesDetails = anthraxNeutralizingAntibodiesDetails;
    }

    public void setAnthraxPcrRtPcrDetails(String anthraxPcrRtPcrDetails) {
        this.anthraxPcrRtPcrDetails = anthraxPcrRtPcrDetails;
    }

    public void setAnthraxGramStainDetails(String anthraxGramStainDetails) {
        this.anthraxGramStainDetails = anthraxGramStainDetails;
    }

    public void setAnthraxLatexAgglutinationDetails(String anthraxLatexAgglutinationDetails) {
        this.anthraxLatexAgglutinationDetails = anthraxLatexAgglutinationDetails;
    }

    public void setAnthraxCqValueDetectionDetails(String anthraxCqValueDetectionDetails) {
        this.anthraxCqValueDetectionDetails = anthraxCqValueDetectionDetails;
    }

    public void setAnthraxSequencingDetails(String anthraxSequencingDetails) {
        this.anthraxSequencingDetails = anthraxSequencingDetails;
    }

    public void setAnthraxDnaMicroarrayDetails(String anthraxDnaMicroarrayDetails) {
        this.anthraxDnaMicroarrayDetails = anthraxDnaMicroarrayDetails;
    }

    public void setAnthraxOtherDetails(String anthraxOtherDetails) {
        this.anthraxOtherDetails = anthraxOtherDetails;
    }

    public void setCoronavirusAntibodyDetection(String coronavirusAntibodyDetection) {
        this.coronavirusAntibodyDetection = coronavirusAntibodyDetection;
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

    public void setCoronavirusDirectFluorescentAntibody(String coronavirusDirectFluorescentAntibody) {
        this.coronavirusDirectFluorescentAntibody = coronavirusDirectFluorescentAntibody;
    }

    public void setCoronavirusMicroscopy(String coronavirusMicroscopy) {
        this.coronavirusMicroscopy = coronavirusMicroscopy;
    }

    public void setCoronavirusNeutralizingAntibodies(String coronavirusNeutralizingAntibodies) {
        this.coronavirusNeutralizingAntibodies = coronavirusNeutralizingAntibodies;
    }

    public void setCoronavirusPcrRtPcr(String coronavirusPcrRtPcr) {
        this.coronavirusPcrRtPcr = coronavirusPcrRtPcr;
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

    public void setCoronavirusSequencing(String coronavirusSequencing) {
        this.coronavirusSequencing = coronavirusSequencing;
    }

    public void setCoronavirusDnaMicroarray(String coronavirusDnaMicroarray) {
        this.coronavirusDnaMicroarray = coronavirusDnaMicroarray;
    }

    public void setCoronavirusOther(String coronavirusOther) {
        this.coronavirusOther = coronavirusOther;
    }

    public void setCoronavirusAntibodyDetectionDetails(String coronavirusAntibodyDetectionDetails) {
        this.coronavirusAntibodyDetectionDetails = coronavirusAntibodyDetectionDetails;
    }

    public void setCoronavirusAntigenDetectionDetails(String coronavirusAntigenDetectionDetails) {
        this.coronavirusAntigenDetectionDetails = coronavirusAntigenDetectionDetails;
    }

    public void setCoronavirusRapidTestDetails(String coronavirusRapidTestDetails) {
        this.coronavirusRapidTestDetails = coronavirusRapidTestDetails;
    }

    public void setCoronavirusCultureDetails(String coronavirusCultureDetails) {
        this.coronavirusCultureDetails = coronavirusCultureDetails;
    }

    public void setCoronavirusHistopathologyDetails(String coronavirusHistopathologyDetails) {
        this.coronavirusHistopathologyDetails = coronavirusHistopathologyDetails;
    }

    public void setCoronavirusIsolationDetails(String coronavirusIsolationDetails) {
        this.coronavirusIsolationDetails = coronavirusIsolationDetails;
    }

    public void setCoronavirusIgmSerumAntibodyDetails(String coronavirusIgmSerumAntibodyDetails) {
        this.coronavirusIgmSerumAntibodyDetails = coronavirusIgmSerumAntibodyDetails;
    }

    public void setCoronavirusIggSerumAntibodyDetails(String coronavirusIggSerumAntibodyDetails) {
        this.coronavirusIggSerumAntibodyDetails = coronavirusIggSerumAntibodyDetails;
    }

    public void setCoronavirusIgaSerumAntibodyDetails(String coronavirusIgaSerumAntibodyDetails) {
        this.coronavirusIgaSerumAntibodyDetails = coronavirusIgaSerumAntibodyDetails;
    }

    public void setCoronavirusIncubationTimeDetails(String coronavirusIncubationTimeDetails) {
        this.coronavirusIncubationTimeDetails = coronavirusIncubationTimeDetails;
    }

    public void setCoronavirusIndirectFluorescentAntibodyDetails(String coronavirusIndirectFluorescentAntibodyDetails) {
        this.coronavirusIndirectFluorescentAntibodyDetails = coronavirusIndirectFluorescentAntibodyDetails;
    }

    public void setCoronavirusDirectFluorescentAntibodyDetails(String coronavirusDirectFluorescentAntibodyDetails) {
        this.coronavirusDirectFluorescentAntibodyDetails = coronavirusDirectFluorescentAntibodyDetails;
    }

    public void setCoronavirusMicroscopyDetails(String coronavirusMicroscopyDetails) {
        this.coronavirusMicroscopyDetails = coronavirusMicroscopyDetails;
    }

    public void setCoronavirusNeutralizingAntibodiesDetails(String coronavirusNeutralizingAntibodiesDetails) {
        this.coronavirusNeutralizingAntibodiesDetails = coronavirusNeutralizingAntibodiesDetails;
    }

    public void setCoronavirusPcrRtPcrDetails(String coronavirusPcrRtPcrDetails) {
        this.coronavirusPcrRtPcrDetails = coronavirusPcrRtPcrDetails;
    }

    public void setCoronavirusGramStainDetails(String coronavirusGramStainDetails) {
        this.coronavirusGramStainDetails = coronavirusGramStainDetails;
    }

    public void setCoronavirusLatexAgglutinationDetails(String coronavirusLatexAgglutinationDetails) {
        this.coronavirusLatexAgglutinationDetails = coronavirusLatexAgglutinationDetails;
    }

    public void setCoronavirusCqValueDetectionDetails(String coronavirusCqValueDetectionDetails) {
        this.coronavirusCqValueDetectionDetails = coronavirusCqValueDetectionDetails;
    }

    public void setCoronavirusSequencingDetails(String coronavirusSequencingDetails) {
        this.coronavirusSequencingDetails = coronavirusSequencingDetails;
    }

    public void setCoronavirusDnaMicroarrayDetails(String coronavirusDnaMicroarrayDetails) {
        this.coronavirusDnaMicroarrayDetails = coronavirusDnaMicroarrayDetails;
    }

    public void setCoronavirusOtherDetails(String coronavirusOtherDetails) {
        this.coronavirusOtherDetails = coronavirusOtherDetails;
    }

    public void setPneumoniaAntibodyDetection(String pneumoniaAntibodyDetection) {
        this.pneumoniaAntibodyDetection = pneumoniaAntibodyDetection;
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

    public void setPneumoniaDirectFluorescentAntibody(String pneumoniaDirectFluorescentAntibody) {
        this.pneumoniaDirectFluorescentAntibody = pneumoniaDirectFluorescentAntibody;
    }

    public void setPneumoniaMicroscopy(String pneumoniaMicroscopy) {
        this.pneumoniaMicroscopy = pneumoniaMicroscopy;
    }

    public void setPneumoniaNeutralizingAntibodies(String pneumoniaNeutralizingAntibodies) {
        this.pneumoniaNeutralizingAntibodies = pneumoniaNeutralizingAntibodies;
    }

    public void setPneumoniaPcrRtPcr(String pneumoniaPcrRtPcr) {
        this.pneumoniaPcrRtPcr = pneumoniaPcrRtPcr;
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

    public void setPneumoniaSequencing(String pneumoniaSequencing) {
        this.pneumoniaSequencing = pneumoniaSequencing;
    }

    public void setPneumoniaDnaMicroarray(String pneumoniaDnaMicroarray) {
        this.pneumoniaDnaMicroarray = pneumoniaDnaMicroarray;
    }

    public void setPneumoniaOther(String pneumoniaOther) {
        this.pneumoniaOther = pneumoniaOther;
    }

    public void setPneumoniaAntibodyDetectionDetails(String pneumoniaAntibodyDetectionDetails) {
        this.pneumoniaAntibodyDetectionDetails = pneumoniaAntibodyDetectionDetails;
    }

    public void setPneumoniaAntigenDetectionDetails(String pneumoniaAntigenDetectionDetails) {
        this.pneumoniaAntigenDetectionDetails = pneumoniaAntigenDetectionDetails;
    }

    public void setPneumoniaRapidTestDetails(String pneumoniaRapidTestDetails) {
        this.pneumoniaRapidTestDetails = pneumoniaRapidTestDetails;
    }

    public void setPneumoniaCultureDetails(String pneumoniaCultureDetails) {
        this.pneumoniaCultureDetails = pneumoniaCultureDetails;
    }

    public void setPneumoniaHistopathologyDetails(String pneumoniaHistopathologyDetails) {
        this.pneumoniaHistopathologyDetails = pneumoniaHistopathologyDetails;
    }

    public void setPneumoniaIsolationDetails(String pneumoniaIsolationDetails) {
        this.pneumoniaIsolationDetails = pneumoniaIsolationDetails;
    }

    public void setPneumoniaIgmSerumAntibodyDetails(String pneumoniaIgmSerumAntibodyDetails) {
        this.pneumoniaIgmSerumAntibodyDetails = pneumoniaIgmSerumAntibodyDetails;
    }

    public void setPneumoniaIggSerumAntibodyDetails(String pneumoniaIggSerumAntibodyDetails) {
        this.pneumoniaIggSerumAntibodyDetails = pneumoniaIggSerumAntibodyDetails;
    }

    public void setPneumoniaIgaSerumAntibodyDetails(String pneumoniaIgaSerumAntibodyDetails) {
        this.pneumoniaIgaSerumAntibodyDetails = pneumoniaIgaSerumAntibodyDetails;
    }

    public void setPneumoniaIncubationTimeDetails(String pneumoniaIncubationTimeDetails) {
        this.pneumoniaIncubationTimeDetails = pneumoniaIncubationTimeDetails;
    }

    public void setPneumoniaIndirectFluorescentAntibodyDetails(String pneumoniaIndirectFluorescentAntibodyDetails) {
        this.pneumoniaIndirectFluorescentAntibodyDetails = pneumoniaIndirectFluorescentAntibodyDetails;
    }

    public void setPneumoniaDirectFluorescentAntibodyDetails(String pneumoniaDirectFluorescentAntibodyDetails) {
        this.pneumoniaDirectFluorescentAntibodyDetails = pneumoniaDirectFluorescentAntibodyDetails;
    }

    public void setPneumoniaMicroscopyDetails(String pneumoniaMicroscopyDetails) {
        this.pneumoniaMicroscopyDetails = pneumoniaMicroscopyDetails;
    }

    public void setPneumoniaNeutralizingAntibodiesDetails(String pneumoniaNeutralizingAntibodiesDetails) {
        this.pneumoniaNeutralizingAntibodiesDetails = pneumoniaNeutralizingAntibodiesDetails;
    }

    public void setPneumoniaPcrRtPcrDetails(String pneumoniaPcrRtPcrDetails) {
        this.pneumoniaPcrRtPcrDetails = pneumoniaPcrRtPcrDetails;
    }

    public void setPneumoniaGramStainDetails(String pneumoniaGramStainDetails) {
        this.pneumoniaGramStainDetails = pneumoniaGramStainDetails;
    }

    public void setPneumoniaLatexAgglutinationDetails(String pneumoniaLatexAgglutinationDetails) {
        this.pneumoniaLatexAgglutinationDetails = pneumoniaLatexAgglutinationDetails;
    }

    public void setPneumoniaCqValueDetectionDetails(String pneumoniaCqValueDetectionDetails) {
        this.pneumoniaCqValueDetectionDetails = pneumoniaCqValueDetectionDetails;
    }

    public void setPneumoniaSequencingDetails(String pneumoniaSequencingDetails) {
        this.pneumoniaSequencingDetails = pneumoniaSequencingDetails;
    }

    public void setPneumoniaDnaMicroarrayDetails(String pneumoniaDnaMicroarrayDetails) {
        this.pneumoniaDnaMicroarrayDetails = pneumoniaDnaMicroarrayDetails;
    }

    public void setPneumoniaOtherDetails(String pneumoniaOtherDetails) {
        this.pneumoniaOtherDetails = pneumoniaOtherDetails;
    }

    public void setMalariaAntibodyDetection(String malariaAntibodyDetection) {
        this.malariaAntibodyDetection = malariaAntibodyDetection;
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

    public void setMalariaDirectFluorescentAntibody(String malariaDirectFluorescentAntibody) {
        this.malariaDirectFluorescentAntibody = malariaDirectFluorescentAntibody;
    }

    public void setMalariaMicroscopy(String malariaMicroscopy) {
        this.malariaMicroscopy = malariaMicroscopy;
    }

    public void setMalariaNeutralizingAntibodies(String malariaNeutralizingAntibodies) {
        this.malariaNeutralizingAntibodies = malariaNeutralizingAntibodies;
    }

    public void setMalariaPcrRtPcr(String malariaPcrRtPcr) {
        this.malariaPcrRtPcr = malariaPcrRtPcr;
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

    public void setMalariaSequencing(String malariaSequencing) {
        this.malariaSequencing = malariaSequencing;
    }

    public void setMalariaDnaMicroarray(String malariaDnaMicroarray) {
        this.malariaDnaMicroarray = malariaDnaMicroarray;
    }

    public void setMalariaOther(String malariaOther) {
        this.malariaOther = malariaOther;
    }

    public void setMalariaAntibodyDetectionDetails(String malariaAntibodyDetectionDetails) {
        this.malariaAntibodyDetectionDetails = malariaAntibodyDetectionDetails;
    }

    public void setMalariaAntigenDetectionDetails(String malariaAntigenDetectionDetails) {
        this.malariaAntigenDetectionDetails = malariaAntigenDetectionDetails;
    }

    public void setMalariaRapidTestDetails(String malariaRapidTestDetails) {
        this.malariaRapidTestDetails = malariaRapidTestDetails;
    }

    public void setMalariaCultureDetails(String malariaCultureDetails) {
        this.malariaCultureDetails = malariaCultureDetails;
    }

    public void setMalariaHistopathologyDetails(String malariaHistopathologyDetails) {
        this.malariaHistopathologyDetails = malariaHistopathologyDetails;
    }

    public void setMalariaIsolationDetails(String malariaIsolationDetails) {
        this.malariaIsolationDetails = malariaIsolationDetails;
    }

    public void setMalariaIgmSerumAntibodyDetails(String malariaIgmSerumAntibodyDetails) {
        this.malariaIgmSerumAntibodyDetails = malariaIgmSerumAntibodyDetails;
    }

    public void setMalariaIggSerumAntibodyDetails(String malariaIggSerumAntibodyDetails) {
        this.malariaIggSerumAntibodyDetails = malariaIggSerumAntibodyDetails;
    }

    public void setMalariaIgaSerumAntibodyDetails(String malariaIgaSerumAntibodyDetails) {
        this.malariaIgaSerumAntibodyDetails = malariaIgaSerumAntibodyDetails;
    }

    public void setMalariaIncubationTimeDetails(String malariaIncubationTimeDetails) {
        this.malariaIncubationTimeDetails = malariaIncubationTimeDetails;
    }

    public void setMalariaIndirectFluorescentAntibodyDetails(String malariaIndirectFluorescentAntibodyDetails) {
        this.malariaIndirectFluorescentAntibodyDetails = malariaIndirectFluorescentAntibodyDetails;
    }

    public void setMalariaDirectFluorescentAntibodyDetails(String malariaDirectFluorescentAntibodyDetails) {
        this.malariaDirectFluorescentAntibodyDetails = malariaDirectFluorescentAntibodyDetails;
    }

    public void setMalariaMicroscopyDetails(String malariaMicroscopyDetails) {
        this.malariaMicroscopyDetails = malariaMicroscopyDetails;
    }

    public void setMalariaNeutralizingAntibodiesDetails(String malariaNeutralizingAntibodiesDetails) {
        this.malariaNeutralizingAntibodiesDetails = malariaNeutralizingAntibodiesDetails;
    }

    public void setMalariaPcrRtPcrDetails(String malariaPcrRtPcrDetails) {
        this.malariaPcrRtPcrDetails = malariaPcrRtPcrDetails;
    }

    public void setMalariaGramStainDetails(String malariaGramStainDetails) {
        this.malariaGramStainDetails = malariaGramStainDetails;
    }

    public void setMalariaLatexAgglutinationDetails(String malariaLatexAgglutinationDetails) {
        this.malariaLatexAgglutinationDetails = malariaLatexAgglutinationDetails;
    }

    public void setMalariaCqValueDetectionDetails(String malariaCqValueDetectionDetails) {
        this.malariaCqValueDetectionDetails = malariaCqValueDetectionDetails;
    }

    public void setMalariaSequencingDetails(String malariaSequencingDetails) {
        this.malariaSequencingDetails = malariaSequencingDetails;
    }

    public void setMalariaDnaMicroarrayDetails(String malariaDnaMicroarrayDetails) {
        this.malariaDnaMicroarrayDetails = malariaDnaMicroarrayDetails;
    }

    public void setMalariaOtherDetails(String malariaOtherDetails) {
        this.malariaOtherDetails = malariaOtherDetails;
    }

    public void setTyphoidFeverAntibodyDetection(String typhoidFeverAntibodyDetection) {
        this.typhoidFeverAntibodyDetection = typhoidFeverAntibodyDetection;
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

    public void setTyphoidFeverDirectFluorescentAntibody(String typhoidFeverDirectFluorescentAntibody) {
        this.typhoidFeverDirectFluorescentAntibody = typhoidFeverDirectFluorescentAntibody;
    }

    public void setTyphoidFeverMicroscopy(String typhoidFeverMicroscopy) {
        this.typhoidFeverMicroscopy = typhoidFeverMicroscopy;
    }

    public void setTyphoidFeverNeutralizingAntibodies(String typhoidFeverNeutralizingAntibodies) {
        this.typhoidFeverNeutralizingAntibodies = typhoidFeverNeutralizingAntibodies;
    }

    public void setTyphoidFeverPcrRtPcr(String typhoidFeverPcrRtPcr) {
        this.typhoidFeverPcrRtPcr = typhoidFeverPcrRtPcr;
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

    public void setTyphoidFeverSequencing(String typhoidFeverSequencing) {
        this.typhoidFeverSequencing = typhoidFeverSequencing;
    }

    public void setTyphoidFeverDnaMicroarray(String typhoidFeverDnaMicroarray) {
        this.typhoidFeverDnaMicroarray = typhoidFeverDnaMicroarray;
    }

    public void setTyphoidFeverOther(String typhoidFeverOther) {
        this.typhoidFeverOther = typhoidFeverOther;
    }

    public void setTyphoidFeverAntibodyDetectionDetails(String typhoidFeverAntibodyDetectionDetails) {
        this.typhoidFeverAntibodyDetectionDetails = typhoidFeverAntibodyDetectionDetails;
    }

    public void setTyphoidFeverAntigenDetectionDetails(String typhoidFeverAntigenDetectionDetails) {
        this.typhoidFeverAntigenDetectionDetails = typhoidFeverAntigenDetectionDetails;
    }

    public void setTyphoidFeverRapidTestDetails(String typhoidFeverRapidTestDetails) {
        this.typhoidFeverRapidTestDetails = typhoidFeverRapidTestDetails;
    }

    public void setTyphoidFeverCultureDetails(String typhoidFeverCultureDetails) {
        this.typhoidFeverCultureDetails = typhoidFeverCultureDetails;
    }

    public void setTyphoidFeverHistopathologyDetails(String typhoidFeverHistopathologyDetails) {
        this.typhoidFeverHistopathologyDetails = typhoidFeverHistopathologyDetails;
    }

    public void setTyphoidFeverIsolationDetails(String typhoidFeverIsolationDetails) {
        this.typhoidFeverIsolationDetails = typhoidFeverIsolationDetails;
    }

    public void setTyphoidFeverIgmSerumAntibodyDetails(String typhoidFeverIgmSerumAntibodyDetails) {
        this.typhoidFeverIgmSerumAntibodyDetails = typhoidFeverIgmSerumAntibodyDetails;
    }

    public void setTyphoidFeverIggSerumAntibodyDetails(String typhoidFeverIggSerumAntibodyDetails) {
        this.typhoidFeverIggSerumAntibodyDetails = typhoidFeverIggSerumAntibodyDetails;
    }

    public void setTyphoidFeverIgaSerumAntibodyDetails(String typhoidFeverIgaSerumAntibodyDetails) {
        this.typhoidFeverIgaSerumAntibodyDetails = typhoidFeverIgaSerumAntibodyDetails;
    }

    public void setTyphoidFeverIncubationTimeDetails(String typhoidFeverIncubationTimeDetails) {
        this.typhoidFeverIncubationTimeDetails = typhoidFeverIncubationTimeDetails;
    }

    public void setTyphoidFeverIndirectFluorescentAntibodyDetails(String typhoidFeverIndirectFluorescentAntibodyDetails) {
        this.typhoidFeverIndirectFluorescentAntibodyDetails = typhoidFeverIndirectFluorescentAntibodyDetails;
    }

    public void setTyphoidFeverDirectFluorescentAntibodyDetails(String typhoidFeverDirectFluorescentAntibodyDetails) {
        this.typhoidFeverDirectFluorescentAntibodyDetails = typhoidFeverDirectFluorescentAntibodyDetails;
    }

    public void setTyphoidFeverMicroscopyDetails(String typhoidFeverMicroscopyDetails) {
        this.typhoidFeverMicroscopyDetails = typhoidFeverMicroscopyDetails;
    }

    public void setTyphoidFeverNeutralizingAntibodiesDetails(String typhoidFeverNeutralizingAntibodiesDetails) {
        this.typhoidFeverNeutralizingAntibodiesDetails = typhoidFeverNeutralizingAntibodiesDetails;
    }

    public void setTyphoidFeverPcrRtPcrDetails(String typhoidFeverPcrRtPcrDetails) {
        this.typhoidFeverPcrRtPcrDetails = typhoidFeverPcrRtPcrDetails;
    }

    public void setTyphoidFeverGramStainDetails(String typhoidFeverGramStainDetails) {
        this.typhoidFeverGramStainDetails = typhoidFeverGramStainDetails;
    }

    public void setTyphoidFeverLatexAgglutinationDetails(String typhoidFeverLatexAgglutinationDetails) {
        this.typhoidFeverLatexAgglutinationDetails = typhoidFeverLatexAgglutinationDetails;
    }

    public void setTyphoidFeverCqValueDetectionDetails(String typhoidFeverCqValueDetectionDetails) {
        this.typhoidFeverCqValueDetectionDetails = typhoidFeverCqValueDetectionDetails;
    }

    public void setTyphoidFeverSequencingDetails(String typhoidFeverSequencingDetails) {
        this.typhoidFeverSequencingDetails = typhoidFeverSequencingDetails;
    }

    public void setTyphoidFeverDnaMicroarrayDetails(String typhoidFeverDnaMicroarrayDetails) {
        this.typhoidFeverDnaMicroarrayDetails = typhoidFeverDnaMicroarrayDetails;
    }

    public void setTyphoidFeverOtherDetails(String typhoidFeverOtherDetails) {
        this.typhoidFeverOtherDetails = typhoidFeverOtherDetails;
    }

    public void setAcuteViralHepatitisAntibodyDetection(String acuteViralHepatitisAntibodyDetection) {
        this.acuteViralHepatitisAntibodyDetection = acuteViralHepatitisAntibodyDetection;
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

    public void setAcuteViralHepatitisDirectFluorescentAntibody(String acuteViralHepatitisDirectFluorescentAntibody) {
        this.acuteViralHepatitisDirectFluorescentAntibody = acuteViralHepatitisDirectFluorescentAntibody;
    }

    public void setAcuteViralHepatitisMicroscopy(String acuteViralHepatitisMicroscopy) {
        this.acuteViralHepatitisMicroscopy = acuteViralHepatitisMicroscopy;
    }

    public void setAcuteViralHepatitisNeutralizingAntibodies(String acuteViralHepatitisNeutralizingAntibodies) {
        this.acuteViralHepatitisNeutralizingAntibodies = acuteViralHepatitisNeutralizingAntibodies;
    }

    public void setAcuteViralHepatitisPcrRtPcr(String acuteViralHepatitisPcrRtPcr) {
        this.acuteViralHepatitisPcrRtPcr = acuteViralHepatitisPcrRtPcr;
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

    public void setAcuteViralHepatitisSequencing(String acuteViralHepatitisSequencing) {
        this.acuteViralHepatitisSequencing = acuteViralHepatitisSequencing;
    }

    public void setAcuteViralHepatitisDnaMicroarray(String acuteViralHepatitisDnaMicroarray) {
        this.acuteViralHepatitisDnaMicroarray = acuteViralHepatitisDnaMicroarray;
    }

    public void setAcuteViralHepatitisOther(String acuteViralHepatitisOther) {
        this.acuteViralHepatitisOther = acuteViralHepatitisOther;
    }

    public void setAcuteViralHepatitisAntibodyDetectionDetails(String acuteViralHepatitisAntibodyDetectionDetails) {
        this.acuteViralHepatitisAntibodyDetectionDetails = acuteViralHepatitisAntibodyDetectionDetails;
    }

    public void setAcuteViralHepatitisAntigenDetectionDetails(String acuteViralHepatitisAntigenDetectionDetails) {
        this.acuteViralHepatitisAntigenDetectionDetails = acuteViralHepatitisAntigenDetectionDetails;
    }

    public void setAcuteViralHepatitisRapidTestDetails(String acuteViralHepatitisRapidTestDetails) {
        this.acuteViralHepatitisRapidTestDetails = acuteViralHepatitisRapidTestDetails;
    }

    public void setAcuteViralHepatitisCultureDetails(String acuteViralHepatitisCultureDetails) {
        this.acuteViralHepatitisCultureDetails = acuteViralHepatitisCultureDetails;
    }

    public void setAcuteViralHepatitisHistopathologyDetails(String acuteViralHepatitisHistopathologyDetails) {
        this.acuteViralHepatitisHistopathologyDetails = acuteViralHepatitisHistopathologyDetails;
    }

    public void setAcuteViralHepatitisIsolationDetails(String acuteViralHepatitisIsolationDetails) {
        this.acuteViralHepatitisIsolationDetails = acuteViralHepatitisIsolationDetails;
    }

    public void setAcuteViralHepatitisIgmSerumAntibodyDetails(String acuteViralHepatitisIgmSerumAntibodyDetails) {
        this.acuteViralHepatitisIgmSerumAntibodyDetails = acuteViralHepatitisIgmSerumAntibodyDetails;
    }

    public void setAcuteViralHepatitisIggSerumAntibodyDetails(String acuteViralHepatitisIggSerumAntibodyDetails) {
        this.acuteViralHepatitisIggSerumAntibodyDetails = acuteViralHepatitisIggSerumAntibodyDetails;
    }

    public void setAcuteViralHepatitisIgaSerumAntibodyDetails(String acuteViralHepatitisIgaSerumAntibodyDetails) {
        this.acuteViralHepatitisIgaSerumAntibodyDetails = acuteViralHepatitisIgaSerumAntibodyDetails;
    }

    public void setAcuteViralHepatitisIncubationTimeDetails(String acuteViralHepatitisIncubationTimeDetails) {
        this.acuteViralHepatitisIncubationTimeDetails = acuteViralHepatitisIncubationTimeDetails;
    }

    public void setAcuteViralHepatitisIndirectFluorescentAntibodyDetails(String acuteViralHepatitisIndirectFluorescentAntibodyDetails) {
        this.acuteViralHepatitisIndirectFluorescentAntibodyDetails = acuteViralHepatitisIndirectFluorescentAntibodyDetails;
    }

    public void setAcuteViralHepatitisDirectFluorescentAntibodyDetails(String acuteViralHepatitisDirectFluorescentAntibodyDetails) {
        this.acuteViralHepatitisDirectFluorescentAntibodyDetails = acuteViralHepatitisDirectFluorescentAntibodyDetails;
    }

    public void setAcuteViralHepatitisMicroscopyDetails(String acuteViralHepatitisMicroscopyDetails) {
        this.acuteViralHepatitisMicroscopyDetails = acuteViralHepatitisMicroscopyDetails;
    }

    public void setAcuteViralHepatitisNeutralizingAntibodiesDetails(String acuteViralHepatitisNeutralizingAntibodiesDetails) {
        this.acuteViralHepatitisNeutralizingAntibodiesDetails = acuteViralHepatitisNeutralizingAntibodiesDetails;
    }

    public void setAcuteViralHepatitisPcrRtPcrDetails(String acuteViralHepatitisPcrRtPcrDetails) {
        this.acuteViralHepatitisPcrRtPcrDetails = acuteViralHepatitisPcrRtPcrDetails;
    }

    public void setAcuteViralHepatitisGramStainDetails(String acuteViralHepatitisGramStainDetails) {
        this.acuteViralHepatitisGramStainDetails = acuteViralHepatitisGramStainDetails;
    }

    public void setAcuteViralHepatitisLatexAgglutinationDetails(String acuteViralHepatitisLatexAgglutinationDetails) {
        this.acuteViralHepatitisLatexAgglutinationDetails = acuteViralHepatitisLatexAgglutinationDetails;
    }

    public void setAcuteViralHepatitisCqValueDetectionDetails(String acuteViralHepatitisCqValueDetectionDetails) {
        this.acuteViralHepatitisCqValueDetectionDetails = acuteViralHepatitisCqValueDetectionDetails;
    }

    public void setAcuteViralHepatitisSequencingDetails(String acuteViralHepatitisSequencingDetails) {
        this.acuteViralHepatitisSequencingDetails = acuteViralHepatitisSequencingDetails;
    }

    public void setAcuteViralHepatitisDnaMicroarrayDetails(String acuteViralHepatitisDnaMicroarrayDetails) {
        this.acuteViralHepatitisDnaMicroarrayDetails = acuteViralHepatitisDnaMicroarrayDetails;
    }

    public void setAcuteViralHepatitisOtherDetails(String acuteViralHepatitisOtherDetails) {
        this.acuteViralHepatitisOtherDetails = acuteViralHepatitisOtherDetails;
    }

    public void setNonNeonatalTetanusAntibodyDetection(String nonNeonatalTetanusAntibodyDetection) {
        this.nonNeonatalTetanusAntibodyDetection = nonNeonatalTetanusAntibodyDetection;
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

    public void setNonNeonatalTetanusDirectFluorescentAntibody(String nonNeonatalTetanusDirectFluorescentAntibody) {
        this.nonNeonatalTetanusDirectFluorescentAntibody = nonNeonatalTetanusDirectFluorescentAntibody;
    }

    public void setNonNeonatalTetanusMicroscopy(String nonNeonatalTetanusMicroscopy) {
        this.nonNeonatalTetanusMicroscopy = nonNeonatalTetanusMicroscopy;
    }

    public void setNonNeonatalTetanusNeutralizingAntibodies(String nonNeonatalTetanusNeutralizingAntibodies) {
        this.nonNeonatalTetanusNeutralizingAntibodies = nonNeonatalTetanusNeutralizingAntibodies;
    }

    public void setNonNeonatalTetanusPcrRtPcr(String nonNeonatalTetanusPcrRtPcr) {
        this.nonNeonatalTetanusPcrRtPcr = nonNeonatalTetanusPcrRtPcr;
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

    public void setNonNeonatalTetanusSequencing(String nonNeonatalTetanusSequencing) {
        this.nonNeonatalTetanusSequencing = nonNeonatalTetanusSequencing;
    }

    public void setNonNeonatalTetanusDnaMicroarray(String nonNeonatalTetanusDnaMicroarray) {
        this.nonNeonatalTetanusDnaMicroarray = nonNeonatalTetanusDnaMicroarray;
    }

    public void setNonNeonatalTetanusOther(String nonNeonatalTetanusOther) {
        this.nonNeonatalTetanusOther = nonNeonatalTetanusOther;
    }

    public void setNonNeonatalTetanusAntibodyDetectionDetails(String nonNeonatalTetanusAntibodyDetectionDetails) {
        this.nonNeonatalTetanusAntibodyDetectionDetails = nonNeonatalTetanusAntibodyDetectionDetails;
    }

    public void setNonNeonatalTetanusAntigenDetectionDetails(String nonNeonatalTetanusAntigenDetectionDetails) {
        this.nonNeonatalTetanusAntigenDetectionDetails = nonNeonatalTetanusAntigenDetectionDetails;
    }

    public void setNonNeonatalTetanusRapidTestDetails(String nonNeonatalTetanusRapidTestDetails) {
        this.nonNeonatalTetanusRapidTestDetails = nonNeonatalTetanusRapidTestDetails;
    }

    public void setNonNeonatalTetanusCultureDetails(String nonNeonatalTetanusCultureDetails) {
        this.nonNeonatalTetanusCultureDetails = nonNeonatalTetanusCultureDetails;
    }

    public void setNonNeonatalTetanusHistopathologyDetails(String nonNeonatalTetanusHistopathologyDetails) {
        this.nonNeonatalTetanusHistopathologyDetails = nonNeonatalTetanusHistopathologyDetails;
    }

    public void setNonNeonatalTetanusIsolationDetails(String nonNeonatalTetanusIsolationDetails) {
        this.nonNeonatalTetanusIsolationDetails = nonNeonatalTetanusIsolationDetails;
    }

    public void setNonNeonatalTetanusIgmSerumAntibodyDetails(String nonNeonatalTetanusIgmSerumAntibodyDetails) {
        this.nonNeonatalTetanusIgmSerumAntibodyDetails = nonNeonatalTetanusIgmSerumAntibodyDetails;
    }

    public void setNonNeonatalTetanusIggSerumAntibodyDetails(String nonNeonatalTetanusIggSerumAntibodyDetails) {
        this.nonNeonatalTetanusIggSerumAntibodyDetails = nonNeonatalTetanusIggSerumAntibodyDetails;
    }

    public void setNonNeonatalTetanusIgaSerumAntibodyDetails(String nonNeonatalTetanusIgaSerumAntibodyDetails) {
        this.nonNeonatalTetanusIgaSerumAntibodyDetails = nonNeonatalTetanusIgaSerumAntibodyDetails;
    }

    public void setNonNeonatalTetanusIncubationTimeDetails(String nonNeonatalTetanusIncubationTimeDetails) {
        this.nonNeonatalTetanusIncubationTimeDetails = nonNeonatalTetanusIncubationTimeDetails;
    }

    public void setNonNeonatalTetanusIndirectFluorescentAntibodyDetails(String nonNeonatalTetanusIndirectFluorescentAntibodyDetails) {
        this.nonNeonatalTetanusIndirectFluorescentAntibodyDetails = nonNeonatalTetanusIndirectFluorescentAntibodyDetails;
    }

    public void setNonNeonatalTetanusDirectFluorescentAntibodyDetails(String nonNeonatalTetanusDirectFluorescentAntibodyDetails) {
        this.nonNeonatalTetanusDirectFluorescentAntibodyDetails = nonNeonatalTetanusDirectFluorescentAntibodyDetails;
    }

    public void setNonNeonatalTetanusMicroscopyDetails(String nonNeonatalTetanusMicroscopyDetails) {
        this.nonNeonatalTetanusMicroscopyDetails = nonNeonatalTetanusMicroscopyDetails;
    }

    public void setNonNeonatalTetanusNeutralizingAntibodiesDetails(String nonNeonatalTetanusNeutralizingAntibodiesDetails) {
        this.nonNeonatalTetanusNeutralizingAntibodiesDetails = nonNeonatalTetanusNeutralizingAntibodiesDetails;
    }

    public void setNonNeonatalTetanusPcrRtPcrDetails(String nonNeonatalTetanusPcrRtPcrDetails) {
        this.nonNeonatalTetanusPcrRtPcrDetails = nonNeonatalTetanusPcrRtPcrDetails;
    }

    public void setNonNeonatalTetanusGramStainDetails(String nonNeonatalTetanusGramStainDetails) {
        this.nonNeonatalTetanusGramStainDetails = nonNeonatalTetanusGramStainDetails;
    }

    public void setNonNeonatalTetanusLatexAgglutinationDetails(String nonNeonatalTetanusLatexAgglutinationDetails) {
        this.nonNeonatalTetanusLatexAgglutinationDetails = nonNeonatalTetanusLatexAgglutinationDetails;
    }

    public void setNonNeonatalTetanusCqValueDetectionDetails(String nonNeonatalTetanusCqValueDetectionDetails) {
        this.nonNeonatalTetanusCqValueDetectionDetails = nonNeonatalTetanusCqValueDetectionDetails;
    }

    public void setNonNeonatalTetanusSequencingDetails(String nonNeonatalTetanusSequencingDetails) {
        this.nonNeonatalTetanusSequencingDetails = nonNeonatalTetanusSequencingDetails;
    }

    public void setNonNeonatalTetanusDnaMicroarrayDetails(String nonNeonatalTetanusDnaMicroarrayDetails) {
        this.nonNeonatalTetanusDnaMicroarrayDetails = nonNeonatalTetanusDnaMicroarrayDetails;
    }

    public void setNonNeonatalTetanusOtherDetails(String nonNeonatalTetanusOtherDetails) {
        this.nonNeonatalTetanusOtherDetails = nonNeonatalTetanusOtherDetails;
    }

    public void setHivAntibodyDetection(String hivAntibodyDetection) {
        this.hivAntibodyDetection = hivAntibodyDetection;
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

    public void setHivDirectFluorescentAntibody(String hivDirectFluorescentAntibody) {
        this.hivDirectFluorescentAntibody = hivDirectFluorescentAntibody;
    }

    public void setHivMicroscopy(String hivMicroscopy) {
        this.hivMicroscopy = hivMicroscopy;
    }

    public void setHivNeutralizingAntibodies(String hivNeutralizingAntibodies) {
        this.hivNeutralizingAntibodies = hivNeutralizingAntibodies;
    }

    public void setHivPcrRtPcr(String hivPcrRtPcr) {
        this.hivPcrRtPcr = hivPcrRtPcr;
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

    public void setHivSequencing(String hivSequencing) {
        this.hivSequencing = hivSequencing;
    }

    public void setHivDnaMicroarray(String hivDnaMicroarray) {
        this.hivDnaMicroarray = hivDnaMicroarray;
    }

    public void setHivOther(String hivOther) {
        this.hivOther = hivOther;
    }

    public void setHivAntibodyDetectionDetails(String hivAntibodyDetectionDetails) {
        this.hivAntibodyDetectionDetails = hivAntibodyDetectionDetails;
    }

    public void setHivAntigenDetectionDetails(String hivAntigenDetectionDetails) {
        this.hivAntigenDetectionDetails = hivAntigenDetectionDetails;
    }

    public void setHivRapidTestDetails(String hivRapidTestDetails) {
        this.hivRapidTestDetails = hivRapidTestDetails;
    }

    public void setHivCultureDetails(String hivCultureDetails) {
        this.hivCultureDetails = hivCultureDetails;
    }

    public void setHivHistopathologyDetails(String hivHistopathologyDetails) {
        this.hivHistopathologyDetails = hivHistopathologyDetails;
    }

    public void setHivIsolationDetails(String hivIsolationDetails) {
        this.hivIsolationDetails = hivIsolationDetails;
    }

    public void setHivIgmSerumAntibodyDetails(String hivIgmSerumAntibodyDetails) {
        this.hivIgmSerumAntibodyDetails = hivIgmSerumAntibodyDetails;
    }

    public void setHivIggSerumAntibodyDetails(String hivIggSerumAntibodyDetails) {
        this.hivIggSerumAntibodyDetails = hivIggSerumAntibodyDetails;
    }

    public void setHivIgaSerumAntibodyDetails(String hivIgaSerumAntibodyDetails) {
        this.hivIgaSerumAntibodyDetails = hivIgaSerumAntibodyDetails;
    }

    public void setHivIncubationTimeDetails(String hivIncubationTimeDetails) {
        this.hivIncubationTimeDetails = hivIncubationTimeDetails;
    }

    public void setHivIndirectFluorescentAntibodyDetails(String hivIndirectFluorescentAntibodyDetails) {
        this.hivIndirectFluorescentAntibodyDetails = hivIndirectFluorescentAntibodyDetails;
    }

    public void setHivDirectFluorescentAntibodyDetails(String hivDirectFluorescentAntibodyDetails) {
        this.hivDirectFluorescentAntibodyDetails = hivDirectFluorescentAntibodyDetails;
    }

    public void setHivMicroscopyDetails(String hivMicroscopyDetails) {
        this.hivMicroscopyDetails = hivMicroscopyDetails;
    }

    public void setHivNeutralizingAntibodiesDetails(String hivNeutralizingAntibodiesDetails) {
        this.hivNeutralizingAntibodiesDetails = hivNeutralizingAntibodiesDetails;
    }

    public void setHivPcrRtPcrDetails(String hivPcrRtPcrDetails) {
        this.hivPcrRtPcrDetails = hivPcrRtPcrDetails;
    }

    public void setHivGramStainDetails(String hivGramStainDetails) {
        this.hivGramStainDetails = hivGramStainDetails;
    }

    public void setHivLatexAgglutinationDetails(String hivLatexAgglutinationDetails) {
        this.hivLatexAgglutinationDetails = hivLatexAgglutinationDetails;
    }

    public void setHivCqValueDetectionDetails(String hivCqValueDetectionDetails) {
        this.hivCqValueDetectionDetails = hivCqValueDetectionDetails;
    }

    public void setHivSequencingDetails(String hivSequencingDetails) {
        this.hivSequencingDetails = hivSequencingDetails;
    }

    public void setHivDnaMicroarrayDetails(String hivDnaMicroarrayDetails) {
        this.hivDnaMicroarrayDetails = hivDnaMicroarrayDetails;
    }

    public void setHivOtherDetails(String hivOtherDetails) {
        this.hivOtherDetails = hivOtherDetails;
    }

    public void setSchistosomiasisAntibodyDetection(String schistosomiasisAntibodyDetection) {
        this.schistosomiasisAntibodyDetection = schistosomiasisAntibodyDetection;
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

    public void setSchistosomiasisDirectFluorescentAntibody(String schistosomiasisDirectFluorescentAntibody) {
        this.schistosomiasisDirectFluorescentAntibody = schistosomiasisDirectFluorescentAntibody;
    }

    public void setSchistosomiasisMicroscopy(String schistosomiasisMicroscopy) {
        this.schistosomiasisMicroscopy = schistosomiasisMicroscopy;
    }

    public void setSchistosomiasisNeutralizingAntibodies(String schistosomiasisNeutralizingAntibodies) {
        this.schistosomiasisNeutralizingAntibodies = schistosomiasisNeutralizingAntibodies;
    }

    public void setSchistosomiasisPcrRtPcr(String schistosomiasisPcrRtPcr) {
        this.schistosomiasisPcrRtPcr = schistosomiasisPcrRtPcr;
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

    public void setSchistosomiasisSequencing(String schistosomiasisSequencing) {
        this.schistosomiasisSequencing = schistosomiasisSequencing;
    }

    public void setSchistosomiasisDnaMicroarray(String schistosomiasisDnaMicroarray) {
        this.schistosomiasisDnaMicroarray = schistosomiasisDnaMicroarray;
    }

    public void setSchistosomiasisOther(String schistosomiasisOther) {
        this.schistosomiasisOther = schistosomiasisOther;
    }

    public void setSchistosomiasisAntibodyDetectionDetails(String schistosomiasisAntibodyDetectionDetails) {
        this.schistosomiasisAntibodyDetectionDetails = schistosomiasisAntibodyDetectionDetails;
    }

    public void setSchistosomiasisAntigenDetectionDetails(String schistosomiasisAntigenDetectionDetails) {
        this.schistosomiasisAntigenDetectionDetails = schistosomiasisAntigenDetectionDetails;
    }

    public void setSchistosomiasisRapidTestDetails(String schistosomiasisRapidTestDetails) {
        this.schistosomiasisRapidTestDetails = schistosomiasisRapidTestDetails;
    }

    public void setSchistosomiasisCultureDetails(String schistosomiasisCultureDetails) {
        this.schistosomiasisCultureDetails = schistosomiasisCultureDetails;
    }

    public void setSchistosomiasisHistopathologyDetails(String schistosomiasisHistopathologyDetails) {
        this.schistosomiasisHistopathologyDetails = schistosomiasisHistopathologyDetails;
    }

    public void setSchistosomiasisIsolationDetails(String schistosomiasisIsolationDetails) {
        this.schistosomiasisIsolationDetails = schistosomiasisIsolationDetails;
    }

    public void setSchistosomiasisIgmSerumAntibodyDetails(String schistosomiasisIgmSerumAntibodyDetails) {
        this.schistosomiasisIgmSerumAntibodyDetails = schistosomiasisIgmSerumAntibodyDetails;
    }

    public void setSchistosomiasisIggSerumAntibodyDetails(String schistosomiasisIggSerumAntibodyDetails) {
        this.schistosomiasisIggSerumAntibodyDetails = schistosomiasisIggSerumAntibodyDetails;
    }

    public void setSchistosomiasisIgaSerumAntibodyDetails(String schistosomiasisIgaSerumAntibodyDetails) {
        this.schistosomiasisIgaSerumAntibodyDetails = schistosomiasisIgaSerumAntibodyDetails;
    }

    public void setSchistosomiasisIncubationTimeDetails(String schistosomiasisIncubationTimeDetails) {
        this.schistosomiasisIncubationTimeDetails = schistosomiasisIncubationTimeDetails;
    }

    public void setSchistosomiasisIndirectFluorescentAntibodyDetails(String schistosomiasisIndirectFluorescentAntibodyDetails) {
        this.schistosomiasisIndirectFluorescentAntibodyDetails = schistosomiasisIndirectFluorescentAntibodyDetails;
    }

    public void setSchistosomiasisDirectFluorescentAntibodyDetails(String schistosomiasisDirectFluorescentAntibodyDetails) {
        this.schistosomiasisDirectFluorescentAntibodyDetails = schistosomiasisDirectFluorescentAntibodyDetails;
    }

    public void setSchistosomiasisMicroscopyDetails(String schistosomiasisMicroscopyDetails) {
        this.schistosomiasisMicroscopyDetails = schistosomiasisMicroscopyDetails;
    }

    public void setSchistosomiasisNeutralizingAntibodiesDetails(String schistosomiasisNeutralizingAntibodiesDetails) {
        this.schistosomiasisNeutralizingAntibodiesDetails = schistosomiasisNeutralizingAntibodiesDetails;
    }

    public void setSchistosomiasisPcrRtPcrDetails(String schistosomiasisPcrRtPcrDetails) {
        this.schistosomiasisPcrRtPcrDetails = schistosomiasisPcrRtPcrDetails;
    }

    public void setSchistosomiasisGramStainDetails(String schistosomiasisGramStainDetails) {
        this.schistosomiasisGramStainDetails = schistosomiasisGramStainDetails;
    }

    public void setSchistosomiasisLatexAgglutinationDetails(String schistosomiasisLatexAgglutinationDetails) {
        this.schistosomiasisLatexAgglutinationDetails = schistosomiasisLatexAgglutinationDetails;
    }

    public void setSchistosomiasisCqValueDetectionDetails(String schistosomiasisCqValueDetectionDetails) {
        this.schistosomiasisCqValueDetectionDetails = schistosomiasisCqValueDetectionDetails;
    }

    public void setSchistosomiasisSequencingDetails(String schistosomiasisSequencingDetails) {
        this.schistosomiasisSequencingDetails = schistosomiasisSequencingDetails;
    }

    public void setSchistosomiasisDnaMicroarrayDetails(String schistosomiasisDnaMicroarrayDetails) {
        this.schistosomiasisDnaMicroarrayDetails = schistosomiasisDnaMicroarrayDetails;
    }

    public void setSchistosomiasisOtherDetails(String schistosomiasisOtherDetails) {
        this.schistosomiasisOtherDetails = schistosomiasisOtherDetails;
    }

    public void setSoilTransmittedHelminthsAntibodyDetection(String soilTransmittedHelminthsAntibodyDetection) {
        this.soilTransmittedHelminthsAntibodyDetection = soilTransmittedHelminthsAntibodyDetection;
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

    public void setSoilTransmittedHelminthsDirectFluorescentAntibody(String soilTransmittedHelminthsDirectFluorescentAntibody) {
        this.soilTransmittedHelminthsDirectFluorescentAntibody = soilTransmittedHelminthsDirectFluorescentAntibody;
    }

    public void setSoilTransmittedHelminthsMicroscopy(String soilTransmittedHelminthsMicroscopy) {
        this.soilTransmittedHelminthsMicroscopy = soilTransmittedHelminthsMicroscopy;
    }

    public void setSoilTransmittedHelminthsNeutralizingAntibodies(String soilTransmittedHelminthsNeutralizingAntibodies) {
        this.soilTransmittedHelminthsNeutralizingAntibodies = soilTransmittedHelminthsNeutralizingAntibodies;
    }

    public void setSoilTransmittedHelminthsPcrRtPcr(String soilTransmittedHelminthsPcrRtPcr) {
        this.soilTransmittedHelminthsPcrRtPcr = soilTransmittedHelminthsPcrRtPcr;
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

    public void setSoilTransmittedHelminthsSequencing(String soilTransmittedHelminthsSequencing) {
        this.soilTransmittedHelminthsSequencing = soilTransmittedHelminthsSequencing;
    }

    public void setSoilTransmittedHelminthsDnaMicroarray(String soilTransmittedHelminthsDnaMicroarray) {
        this.soilTransmittedHelminthsDnaMicroarray = soilTransmittedHelminthsDnaMicroarray;
    }

    public void setSoilTransmittedHelminthsOther(String soilTransmittedHelminthsOther) {
        this.soilTransmittedHelminthsOther = soilTransmittedHelminthsOther;
    }

    public void setSoilTransmittedHelminthsAntibodyDetectionDetails(String soilTransmittedHelminthsAntibodyDetectionDetails) {
        this.soilTransmittedHelminthsAntibodyDetectionDetails = soilTransmittedHelminthsAntibodyDetectionDetails;
    }

    public void setSoilTransmittedHelminthsAntigenDetectionDetails(String soilTransmittedHelminthsAntigenDetectionDetails) {
        this.soilTransmittedHelminthsAntigenDetectionDetails = soilTransmittedHelminthsAntigenDetectionDetails;
    }

    public void setSoilTransmittedHelminthsRapidTestDetails(String soilTransmittedHelminthsRapidTestDetails) {
        this.soilTransmittedHelminthsRapidTestDetails = soilTransmittedHelminthsRapidTestDetails;
    }

    public void setSoilTransmittedHelminthsCultureDetails(String soilTransmittedHelminthsCultureDetails) {
        this.soilTransmittedHelminthsCultureDetails = soilTransmittedHelminthsCultureDetails;
    }

    public void setSoilTransmittedHelminthsHistopathologyDetails(String soilTransmittedHelminthsHistopathologyDetails) {
        this.soilTransmittedHelminthsHistopathologyDetails = soilTransmittedHelminthsHistopathologyDetails;
    }

    public void setSoilTransmittedHelminthsIsolationDetails(String soilTransmittedHelminthsIsolationDetails) {
        this.soilTransmittedHelminthsIsolationDetails = soilTransmittedHelminthsIsolationDetails;
    }

    public void setSoilTransmittedHelminthsIgmSerumAntibodyDetails(String soilTransmittedHelminthsIgmSerumAntibodyDetails) {
        this.soilTransmittedHelminthsIgmSerumAntibodyDetails = soilTransmittedHelminthsIgmSerumAntibodyDetails;
    }

    public void setSoilTransmittedHelminthsIggSerumAntibodyDetails(String soilTransmittedHelminthsIggSerumAntibodyDetails) {
        this.soilTransmittedHelminthsIggSerumAntibodyDetails = soilTransmittedHelminthsIggSerumAntibodyDetails;
    }

    public void setSoilTransmittedHelminthsIgaSerumAntibodyDetails(String soilTransmittedHelminthsIgaSerumAntibodyDetails) {
        this.soilTransmittedHelminthsIgaSerumAntibodyDetails = soilTransmittedHelminthsIgaSerumAntibodyDetails;
    }

    public void setSoilTransmittedHelminthsIncubationTimeDetails(String soilTransmittedHelminthsIncubationTimeDetails) {
        this.soilTransmittedHelminthsIncubationTimeDetails = soilTransmittedHelminthsIncubationTimeDetails;
    }

    public void setSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails(String soilTransmittedHelminthsIndirectFluorescentAntibodyDetails) {
        this.soilTransmittedHelminthsIndirectFluorescentAntibodyDetails = soilTransmittedHelminthsIndirectFluorescentAntibodyDetails;
    }

    public void setSoilTransmittedHelminthsDirectFluorescentAntibodyDetails(String soilTransmittedHelminthsDirectFluorescentAntibodyDetails) {
        this.soilTransmittedHelminthsDirectFluorescentAntibodyDetails = soilTransmittedHelminthsDirectFluorescentAntibodyDetails;
    }

    public void setSoilTransmittedHelminthsMicroscopyDetails(String soilTransmittedHelminthsMicroscopyDetails) {
        this.soilTransmittedHelminthsMicroscopyDetails = soilTransmittedHelminthsMicroscopyDetails;
    }

    public void setSoilTransmittedHelminthsNeutralizingAntibodiesDetails(String soilTransmittedHelminthsNeutralizingAntibodiesDetails) {
        this.soilTransmittedHelminthsNeutralizingAntibodiesDetails = soilTransmittedHelminthsNeutralizingAntibodiesDetails;
    }

    public void setSoilTransmittedHelminthsPcrRtPcrDetails(String soilTransmittedHelminthsPcrRtPcrDetails) {
        this.soilTransmittedHelminthsPcrRtPcrDetails = soilTransmittedHelminthsPcrRtPcrDetails;
    }

    public void setSoilTransmittedHelminthsGramStainDetails(String soilTransmittedHelminthsGramStainDetails) {
        this.soilTransmittedHelminthsGramStainDetails = soilTransmittedHelminthsGramStainDetails;
    }

    public void setSoilTransmittedHelminthsLatexAgglutinationDetails(String soilTransmittedHelminthsLatexAgglutinationDetails) {
        this.soilTransmittedHelminthsLatexAgglutinationDetails = soilTransmittedHelminthsLatexAgglutinationDetails;
    }

    public void setSoilTransmittedHelminthsCqValueDetectionDetails(String soilTransmittedHelminthsCqValueDetectionDetails) {
        this.soilTransmittedHelminthsCqValueDetectionDetails = soilTransmittedHelminthsCqValueDetectionDetails;
    }

    public void setSoilTransmittedHelminthsSequencingDetails(String soilTransmittedHelminthsSequencingDetails) {
        this.soilTransmittedHelminthsSequencingDetails = soilTransmittedHelminthsSequencingDetails;
    }

    public void setSoilTransmittedHelminthsDnaMicroarrayDetails(String soilTransmittedHelminthsDnaMicroarrayDetails) {
        this.soilTransmittedHelminthsDnaMicroarrayDetails = soilTransmittedHelminthsDnaMicroarrayDetails;
    }

    public void setSoilTransmittedHelminthsOtherDetails(String soilTransmittedHelminthsOtherDetails) {
        this.soilTransmittedHelminthsOtherDetails = soilTransmittedHelminthsOtherDetails;
    }

    public void setTrypanosomiasisAntibodyDetection(String trypanosomiasisAntibodyDetection) {
        this.trypanosomiasisAntibodyDetection = trypanosomiasisAntibodyDetection;
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

    public void setTrypanosomiasisDirectFluorescentAntibody(String trypanosomiasisDirectFluorescentAntibody) {
        this.trypanosomiasisDirectFluorescentAntibody = trypanosomiasisDirectFluorescentAntibody;
    }

    public void setTrypanosomiasisMicroscopy(String trypanosomiasisMicroscopy) {
        this.trypanosomiasisMicroscopy = trypanosomiasisMicroscopy;
    }

    public void setTrypanosomiasisNeutralizingAntibodies(String trypanosomiasisNeutralizingAntibodies) {
        this.trypanosomiasisNeutralizingAntibodies = trypanosomiasisNeutralizingAntibodies;
    }

    public void setTrypanosomiasisPcrRtPcr(String trypanosomiasisPcrRtPcr) {
        this.trypanosomiasisPcrRtPcr = trypanosomiasisPcrRtPcr;
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

    public void setTrypanosomiasisSequencing(String trypanosomiasisSequencing) {
        this.trypanosomiasisSequencing = trypanosomiasisSequencing;
    }

    public void setTrypanosomiasisDnaMicroarray(String trypanosomiasisDnaMicroarray) {
        this.trypanosomiasisDnaMicroarray = trypanosomiasisDnaMicroarray;
    }

    public void setTrypanosomiasisOther(String trypanosomiasisOther) {
        this.trypanosomiasisOther = trypanosomiasisOther;
    }

    public void setTrypanosomiasisAntibodyDetectionDetails(String trypanosomiasisAntibodyDetectionDetails) {
        this.trypanosomiasisAntibodyDetectionDetails = trypanosomiasisAntibodyDetectionDetails;
    }

    public void setTrypanosomiasisAntigenDetectionDetails(String trypanosomiasisAntigenDetectionDetails) {
        this.trypanosomiasisAntigenDetectionDetails = trypanosomiasisAntigenDetectionDetails;
    }

    public void setTrypanosomiasisRapidTestDetails(String trypanosomiasisRapidTestDetails) {
        this.trypanosomiasisRapidTestDetails = trypanosomiasisRapidTestDetails;
    }

    public void setTrypanosomiasisCultureDetails(String trypanosomiasisCultureDetails) {
        this.trypanosomiasisCultureDetails = trypanosomiasisCultureDetails;
    }

    public void setTrypanosomiasisHistopathologyDetails(String trypanosomiasisHistopathologyDetails) {
        this.trypanosomiasisHistopathologyDetails = trypanosomiasisHistopathologyDetails;
    }

    public void setTrypanosomiasisIsolationDetails(String trypanosomiasisIsolationDetails) {
        this.trypanosomiasisIsolationDetails = trypanosomiasisIsolationDetails;
    }

    public void setTrypanosomiasisIgmSerumAntibodyDetails(String trypanosomiasisIgmSerumAntibodyDetails) {
        this.trypanosomiasisIgmSerumAntibodyDetails = trypanosomiasisIgmSerumAntibodyDetails;
    }

    public void setTrypanosomiasisIggSerumAntibodyDetails(String trypanosomiasisIggSerumAntibodyDetails) {
        this.trypanosomiasisIggSerumAntibodyDetails = trypanosomiasisIggSerumAntibodyDetails;
    }

    public void setTrypanosomiasisIgaSerumAntibodyDetails(String trypanosomiasisIgaSerumAntibodyDetails) {
        this.trypanosomiasisIgaSerumAntibodyDetails = trypanosomiasisIgaSerumAntibodyDetails;
    }

    public void setTrypanosomiasisIncubationTimeDetails(String trypanosomiasisIncubationTimeDetails) {
        this.trypanosomiasisIncubationTimeDetails = trypanosomiasisIncubationTimeDetails;
    }

    public void setTrypanosomiasisIndirectFluorescentAntibodyDetails(String trypanosomiasisIndirectFluorescentAntibodyDetails) {
        this.trypanosomiasisIndirectFluorescentAntibodyDetails = trypanosomiasisIndirectFluorescentAntibodyDetails;
    }

    public void setTrypanosomiasisDirectFluorescentAntibodyDetails(String trypanosomiasisDirectFluorescentAntibodyDetails) {
        this.trypanosomiasisDirectFluorescentAntibodyDetails = trypanosomiasisDirectFluorescentAntibodyDetails;
    }

    public void setTrypanosomiasisMicroscopyDetails(String trypanosomiasisMicroscopyDetails) {
        this.trypanosomiasisMicroscopyDetails = trypanosomiasisMicroscopyDetails;
    }

    public void setTrypanosomiasisNeutralizingAntibodiesDetails(String trypanosomiasisNeutralizingAntibodiesDetails) {
        this.trypanosomiasisNeutralizingAntibodiesDetails = trypanosomiasisNeutralizingAntibodiesDetails;
    }

    public void setTrypanosomiasisPcrRtPcrDetails(String trypanosomiasisPcrRtPcrDetails) {
        this.trypanosomiasisPcrRtPcrDetails = trypanosomiasisPcrRtPcrDetails;
    }

    public void setTrypanosomiasisGramStainDetails(String trypanosomiasisGramStainDetails) {
        this.trypanosomiasisGramStainDetails = trypanosomiasisGramStainDetails;
    }

    public void setTrypanosomiasisLatexAgglutinationDetails(String trypanosomiasisLatexAgglutinationDetails) {
        this.trypanosomiasisLatexAgglutinationDetails = trypanosomiasisLatexAgglutinationDetails;
    }

    public void setTrypanosomiasisCqValueDetectionDetails(String trypanosomiasisCqValueDetectionDetails) {
        this.trypanosomiasisCqValueDetectionDetails = trypanosomiasisCqValueDetectionDetails;
    }

    public void setTrypanosomiasisSequencingDetails(String trypanosomiasisSequencingDetails) {
        this.trypanosomiasisSequencingDetails = trypanosomiasisSequencingDetails;
    }

    public void setTrypanosomiasisDnaMicroarrayDetails(String trypanosomiasisDnaMicroarrayDetails) {
        this.trypanosomiasisDnaMicroarrayDetails = trypanosomiasisDnaMicroarrayDetails;
    }

    public void setTrypanosomiasisOtherDetails(String trypanosomiasisOtherDetails) {
        this.trypanosomiasisOtherDetails = trypanosomiasisOtherDetails;
    }

    public void setDiarrheaDehydrationAntibodyDetection(String diarrheaDehydrationAntibodyDetection) {
        this.diarrheaDehydrationAntibodyDetection = diarrheaDehydrationAntibodyDetection;
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

    public void setDiarrheaDehydrationDirectFluorescentAntibody(String diarrheaDehydrationDirectFluorescentAntibody) {
        this.diarrheaDehydrationDirectFluorescentAntibody = diarrheaDehydrationDirectFluorescentAntibody;
    }

    public void setDiarrheaDehydrationMicroscopy(String diarrheaDehydrationMicroscopy) {
        this.diarrheaDehydrationMicroscopy = diarrheaDehydrationMicroscopy;
    }

    public void setDiarrheaDehydrationNeutralizingAntibodies(String diarrheaDehydrationNeutralizingAntibodies) {
        this.diarrheaDehydrationNeutralizingAntibodies = diarrheaDehydrationNeutralizingAntibodies;
    }

    public void setDiarrheaDehydrationPcrRtPcr(String diarrheaDehydrationPcrRtPcr) {
        this.diarrheaDehydrationPcrRtPcr = diarrheaDehydrationPcrRtPcr;
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

    public void setDiarrheaDehydrationSequencing(String diarrheaDehydrationSequencing) {
        this.diarrheaDehydrationSequencing = diarrheaDehydrationSequencing;
    }

    public void setDiarrheaDehydrationDnaMicroarray(String diarrheaDehydrationDnaMicroarray) {
        this.diarrheaDehydrationDnaMicroarray = diarrheaDehydrationDnaMicroarray;
    }

    public void setDiarrheaDehydrationOther(String diarrheaDehydrationOther) {
        this.diarrheaDehydrationOther = diarrheaDehydrationOther;
    }

    public void setDiarrheaDehydrationAntibodyDetectionDetails(String diarrheaDehydrationAntibodyDetectionDetails) {
        this.diarrheaDehydrationAntibodyDetectionDetails = diarrheaDehydrationAntibodyDetectionDetails;
    }

    public void setDiarrheaDehydrationAntigenDetectionDetails(String diarrheaDehydrationAntigenDetectionDetails) {
        this.diarrheaDehydrationAntigenDetectionDetails = diarrheaDehydrationAntigenDetectionDetails;
    }

    public void setDiarrheaDehydrationRapidTestDetails(String diarrheaDehydrationRapidTestDetails) {
        this.diarrheaDehydrationRapidTestDetails = diarrheaDehydrationRapidTestDetails;
    }

    public void setDiarrheaDehydrationCultureDetails(String diarrheaDehydrationCultureDetails) {
        this.diarrheaDehydrationCultureDetails = diarrheaDehydrationCultureDetails;
    }

    public void setDiarrheaDehydrationHistopathologyDetails(String diarrheaDehydrationHistopathologyDetails) {
        this.diarrheaDehydrationHistopathologyDetails = diarrheaDehydrationHistopathologyDetails;
    }

    public void setDiarrheaDehydrationIsolationDetails(String diarrheaDehydrationIsolationDetails) {
        this.diarrheaDehydrationIsolationDetails = diarrheaDehydrationIsolationDetails;
    }

    public void setDiarrheaDehydrationIgmSerumAntibodyDetails(String diarrheaDehydrationIgmSerumAntibodyDetails) {
        this.diarrheaDehydrationIgmSerumAntibodyDetails = diarrheaDehydrationIgmSerumAntibodyDetails;
    }

    public void setDiarrheaDehydrationIggSerumAntibodyDetails(String diarrheaDehydrationIggSerumAntibodyDetails) {
        this.diarrheaDehydrationIggSerumAntibodyDetails = diarrheaDehydrationIggSerumAntibodyDetails;
    }

    public void setDiarrheaDehydrationIgaSerumAntibodyDetails(String diarrheaDehydrationIgaSerumAntibodyDetails) {
        this.diarrheaDehydrationIgaSerumAntibodyDetails = diarrheaDehydrationIgaSerumAntibodyDetails;
    }

    public void setDiarrheaDehydrationIncubationTimeDetails(String diarrheaDehydrationIncubationTimeDetails) {
        this.diarrheaDehydrationIncubationTimeDetails = diarrheaDehydrationIncubationTimeDetails;
    }

    public void setDiarrheaDehydrationIndirectFluorescentAntibodyDetails(String diarrheaDehydrationIndirectFluorescentAntibodyDetails) {
        this.diarrheaDehydrationIndirectFluorescentAntibodyDetails = diarrheaDehydrationIndirectFluorescentAntibodyDetails;
    }

    public void setDiarrheaDehydrationDirectFluorescentAntibodyDetails(String diarrheaDehydrationDirectFluorescentAntibodyDetails) {
        this.diarrheaDehydrationDirectFluorescentAntibodyDetails = diarrheaDehydrationDirectFluorescentAntibodyDetails;
    }

    public void setDiarrheaDehydrationMicroscopyDetails(String diarrheaDehydrationMicroscopyDetails) {
        this.diarrheaDehydrationMicroscopyDetails = diarrheaDehydrationMicroscopyDetails;
    }

    public void setDiarrheaDehydrationNeutralizingAntibodiesDetails(String diarrheaDehydrationNeutralizingAntibodiesDetails) {
        this.diarrheaDehydrationNeutralizingAntibodiesDetails = diarrheaDehydrationNeutralizingAntibodiesDetails;
    }

    public void setDiarrheaDehydrationPcrRtPcrDetails(String diarrheaDehydrationPcrRtPcrDetails) {
        this.diarrheaDehydrationPcrRtPcrDetails = diarrheaDehydrationPcrRtPcrDetails;
    }

    public void setDiarrheaDehydrationGramStainDetails(String diarrheaDehydrationGramStainDetails) {
        this.diarrheaDehydrationGramStainDetails = diarrheaDehydrationGramStainDetails;
    }

    public void setDiarrheaDehydrationLatexAgglutinationDetails(String diarrheaDehydrationLatexAgglutinationDetails) {
        this.diarrheaDehydrationLatexAgglutinationDetails = diarrheaDehydrationLatexAgglutinationDetails;
    }

    public void setDiarrheaDehydrationCqValueDetectionDetails(String diarrheaDehydrationCqValueDetectionDetails) {
        this.diarrheaDehydrationCqValueDetectionDetails = diarrheaDehydrationCqValueDetectionDetails;
    }

    public void setDiarrheaDehydrationSequencingDetails(String diarrheaDehydrationSequencingDetails) {
        this.diarrheaDehydrationSequencingDetails = diarrheaDehydrationSequencingDetails;
    }

    public void setDiarrheaDehydrationDnaMicroarrayDetails(String diarrheaDehydrationDnaMicroarrayDetails) {
        this.diarrheaDehydrationDnaMicroarrayDetails = diarrheaDehydrationDnaMicroarrayDetails;
    }

    public void setDiarrheaDehydrationOtherDetails(String diarrheaDehydrationOtherDetails) {
        this.diarrheaDehydrationOtherDetails = diarrheaDehydrationOtherDetails;
    }

    public void setDiarrheaBloodAntibodyDetection(String diarrheaBloodAntibodyDetection) {
        this.diarrheaBloodAntibodyDetection = diarrheaBloodAntibodyDetection;
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

    public void setDiarrheaBloodDirectFluorescentAntibody(String diarrheaBloodDirectFluorescentAntibody) {
        this.diarrheaBloodDirectFluorescentAntibody = diarrheaBloodDirectFluorescentAntibody;
    }

    public void setDiarrheaBloodMicroscopy(String diarrheaBloodMicroscopy) {
        this.diarrheaBloodMicroscopy = diarrheaBloodMicroscopy;
    }

    public void setDiarrheaBloodNeutralizingAntibodies(String diarrheaBloodNeutralizingAntibodies) {
        this.diarrheaBloodNeutralizingAntibodies = diarrheaBloodNeutralizingAntibodies;
    }

    public void setDiarrheaBloodPcrRtPcr(String diarrheaBloodPcrRtPcr) {
        this.diarrheaBloodPcrRtPcr = diarrheaBloodPcrRtPcr;
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

    public void setDiarrheaBloodSequencing(String diarrheaBloodSequencing) {
        this.diarrheaBloodSequencing = diarrheaBloodSequencing;
    }

    public void setDiarrheaBloodDnaMicroarray(String diarrheaBloodDnaMicroarray) {
        this.diarrheaBloodDnaMicroarray = diarrheaBloodDnaMicroarray;
    }

    public void setDiarrheaBloodOther(String diarrheaBloodOther) {
        this.diarrheaBloodOther = diarrheaBloodOther;
    }

    public void setDiarrheaBloodAntibodyDetectionDetails(String diarrheaBloodAntibodyDetectionDetails) {
        this.diarrheaBloodAntibodyDetectionDetails = diarrheaBloodAntibodyDetectionDetails;
    }

    public void setDiarrheaBloodAntigenDetectionDetails(String diarrheaBloodAntigenDetectionDetails) {
        this.diarrheaBloodAntigenDetectionDetails = diarrheaBloodAntigenDetectionDetails;
    }

    public void setDiarrheaBloodRapidTestDetails(String diarrheaBloodRapidTestDetails) {
        this.diarrheaBloodRapidTestDetails = diarrheaBloodRapidTestDetails;
    }

    public void setDiarrheaBloodCultureDetails(String diarrheaBloodCultureDetails) {
        this.diarrheaBloodCultureDetails = diarrheaBloodCultureDetails;
    }

    public void setDiarrheaBloodHistopathologyDetails(String diarrheaBloodHistopathologyDetails) {
        this.diarrheaBloodHistopathologyDetails = diarrheaBloodHistopathologyDetails;
    }

    public void setDiarrheaBloodIsolationDetails(String diarrheaBloodIsolationDetails) {
        this.diarrheaBloodIsolationDetails = diarrheaBloodIsolationDetails;
    }

    public void setDiarrheaBloodIgmSerumAntibodyDetails(String diarrheaBloodIgmSerumAntibodyDetails) {
        this.diarrheaBloodIgmSerumAntibodyDetails = diarrheaBloodIgmSerumAntibodyDetails;
    }

    public void setDiarrheaBloodIggSerumAntibodyDetails(String diarrheaBloodIggSerumAntibodyDetails) {
        this.diarrheaBloodIggSerumAntibodyDetails = diarrheaBloodIggSerumAntibodyDetails;
    }

    public void setDiarrheaBloodIgaSerumAntibodyDetails(String diarrheaBloodIgaSerumAntibodyDetails) {
        this.diarrheaBloodIgaSerumAntibodyDetails = diarrheaBloodIgaSerumAntibodyDetails;
    }

    public void setDiarrheaBloodIncubationTimeDetails(String diarrheaBloodIncubationTimeDetails) {
        this.diarrheaBloodIncubationTimeDetails = diarrheaBloodIncubationTimeDetails;
    }

    public void setDiarrheaBloodIndirectFluorescentAntibodyDetails(String diarrheaBloodIndirectFluorescentAntibodyDetails) {
        this.diarrheaBloodIndirectFluorescentAntibodyDetails = diarrheaBloodIndirectFluorescentAntibodyDetails;
    }

    public void setDiarrheaBloodDirectFluorescentAntibodyDetails(String diarrheaBloodDirectFluorescentAntibodyDetails) {
        this.diarrheaBloodDirectFluorescentAntibodyDetails = diarrheaBloodDirectFluorescentAntibodyDetails;
    }

    public void setDiarrheaBloodMicroscopyDetails(String diarrheaBloodMicroscopyDetails) {
        this.diarrheaBloodMicroscopyDetails = diarrheaBloodMicroscopyDetails;
    }

    public void setDiarrheaBloodNeutralizingAntibodiesDetails(String diarrheaBloodNeutralizingAntibodiesDetails) {
        this.diarrheaBloodNeutralizingAntibodiesDetails = diarrheaBloodNeutralizingAntibodiesDetails;
    }

    public void setDiarrheaBloodPcrRtPcrDetails(String diarrheaBloodPcrRtPcrDetails) {
        this.diarrheaBloodPcrRtPcrDetails = diarrheaBloodPcrRtPcrDetails;
    }

    public void setDiarrheaBloodGramStainDetails(String diarrheaBloodGramStainDetails) {
        this.diarrheaBloodGramStainDetails = diarrheaBloodGramStainDetails;
    }

    public void setDiarrheaBloodLatexAgglutinationDetails(String diarrheaBloodLatexAgglutinationDetails) {
        this.diarrheaBloodLatexAgglutinationDetails = diarrheaBloodLatexAgglutinationDetails;
    }

    public void setDiarrheaBloodCqValueDetectionDetails(String diarrheaBloodCqValueDetectionDetails) {
        this.diarrheaBloodCqValueDetectionDetails = diarrheaBloodCqValueDetectionDetails;
    }

    public void setDiarrheaBloodSequencingDetails(String diarrheaBloodSequencingDetails) {
        this.diarrheaBloodSequencingDetails = diarrheaBloodSequencingDetails;
    }

    public void setDiarrheaBloodDnaMicroarrayDetails(String diarrheaBloodDnaMicroarrayDetails) {
        this.diarrheaBloodDnaMicroarrayDetails = diarrheaBloodDnaMicroarrayDetails;
    }

    public void setDiarrheaBloodOtherDetails(String diarrheaBloodOtherDetails) {
        this.diarrheaBloodOtherDetails = diarrheaBloodOtherDetails;
    }

    public void setSnakeBiteAntibodyDetection(String snakeBiteAntibodyDetection) {
        this.snakeBiteAntibodyDetection = snakeBiteAntibodyDetection;
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

    public void setSnakeBiteDirectFluorescentAntibody(String snakeBiteDirectFluorescentAntibody) {
        this.snakeBiteDirectFluorescentAntibody = snakeBiteDirectFluorescentAntibody;
    }

    public void setSnakeBiteMicroscopy(String snakeBiteMicroscopy) {
        this.snakeBiteMicroscopy = snakeBiteMicroscopy;
    }

    public void setSnakeBiteNeutralizingAntibodies(String snakeBiteNeutralizingAntibodies) {
        this.snakeBiteNeutralizingAntibodies = snakeBiteNeutralizingAntibodies;
    }

    public void setSnakeBitePcrRtPcr(String snakeBitePcrRtPcr) {
        this.snakeBitePcrRtPcr = snakeBitePcrRtPcr;
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

    public void setSnakeBiteSequencing(String snakeBiteSequencing) {
        this.snakeBiteSequencing = snakeBiteSequencing;
    }

    public void setSnakeBiteDnaMicroarray(String snakeBiteDnaMicroarray) {
        this.snakeBiteDnaMicroarray = snakeBiteDnaMicroarray;
    }

    public void setSnakeBiteOther(String snakeBiteOther) {
        this.snakeBiteOther = snakeBiteOther;
    }

    public void setSnakeBiteAntibodyDetectionDetails(String snakeBiteAntibodyDetectionDetails) {
        this.snakeBiteAntibodyDetectionDetails = snakeBiteAntibodyDetectionDetails;
    }

    public void setSnakeBiteAntigenDetectionDetails(String snakeBiteAntigenDetectionDetails) {
        this.snakeBiteAntigenDetectionDetails = snakeBiteAntigenDetectionDetails;
    }

    public void setSnakeBiteRapidTestDetails(String snakeBiteRapidTestDetails) {
        this.snakeBiteRapidTestDetails = snakeBiteRapidTestDetails;
    }

    public void setSnakeBiteCultureDetails(String snakeBiteCultureDetails) {
        this.snakeBiteCultureDetails = snakeBiteCultureDetails;
    }

    public void setSnakeBiteHistopathologyDetails(String snakeBiteHistopathologyDetails) {
        this.snakeBiteHistopathologyDetails = snakeBiteHistopathologyDetails;
    }

    public void setSnakeBiteIsolationDetails(String snakeBiteIsolationDetails) {
        this.snakeBiteIsolationDetails = snakeBiteIsolationDetails;
    }

    public void setSnakeBiteIgmSerumAntibodyDetails(String snakeBiteIgmSerumAntibodyDetails) {
        this.snakeBiteIgmSerumAntibodyDetails = snakeBiteIgmSerumAntibodyDetails;
    }

    public void setSnakeBiteIggSerumAntibodyDetails(String snakeBiteIggSerumAntibodyDetails) {
        this.snakeBiteIggSerumAntibodyDetails = snakeBiteIggSerumAntibodyDetails;
    }

    public void setSnakeBiteIgaSerumAntibodyDetails(String snakeBiteIgaSerumAntibodyDetails) {
        this.snakeBiteIgaSerumAntibodyDetails = snakeBiteIgaSerumAntibodyDetails;
    }

    public void setSnakeBiteIncubationTimeDetails(String snakeBiteIncubationTimeDetails) {
        this.snakeBiteIncubationTimeDetails = snakeBiteIncubationTimeDetails;
    }

    public void setSnakeBiteIndirectFluorescentAntibodyDetails(String snakeBiteIndirectFluorescentAntibodyDetails) {
        this.snakeBiteIndirectFluorescentAntibodyDetails = snakeBiteIndirectFluorescentAntibodyDetails;
    }

    public void setSnakeBiteDirectFluorescentAntibodyDetails(String snakeBiteDirectFluorescentAntibodyDetails) {
        this.snakeBiteDirectFluorescentAntibodyDetails = snakeBiteDirectFluorescentAntibodyDetails;
    }

    public void setSnakeBiteMicroscopyDetails(String snakeBiteMicroscopyDetails) {
        this.snakeBiteMicroscopyDetails = snakeBiteMicroscopyDetails;
    }

    public void setSnakeBiteNeutralizingAntibodiesDetails(String snakeBiteNeutralizingAntibodiesDetails) {
        this.snakeBiteNeutralizingAntibodiesDetails = snakeBiteNeutralizingAntibodiesDetails;
    }

    public void setSnakeBitePcrRtPcrDetails(String snakeBitePcrRtPcrDetails) {
        this.snakeBitePcrRtPcrDetails = snakeBitePcrRtPcrDetails;
    }

    public void setSnakeBiteGramStainDetails(String snakeBiteGramStainDetails) {
        this.snakeBiteGramStainDetails = snakeBiteGramStainDetails;
    }

    public void setSnakeBiteLatexAgglutinationDetails(String snakeBiteLatexAgglutinationDetails) {
        this.snakeBiteLatexAgglutinationDetails = snakeBiteLatexAgglutinationDetails;
    }

    public void setSnakeBiteCqValueDetectionDetails(String snakeBiteCqValueDetectionDetails) {
        this.snakeBiteCqValueDetectionDetails = snakeBiteCqValueDetectionDetails;
    }

    public void setSnakeBiteSequencingDetails(String snakeBiteSequencingDetails) {
        this.snakeBiteSequencingDetails = snakeBiteSequencingDetails;
    }

    public void setSnakeBiteDnaMicroarrayDetails(String snakeBiteDnaMicroarrayDetails) {
        this.snakeBiteDnaMicroarrayDetails = snakeBiteDnaMicroarrayDetails;
    }

    public void setSnakeBiteOtherDetails(String snakeBiteOtherDetails) {
        this.snakeBiteOtherDetails = snakeBiteOtherDetails;
    }

    public void setRubellaAntibodyDetection(String rubellaAntibodyDetection) {
        this.rubellaAntibodyDetection = rubellaAntibodyDetection;
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

    public void setRubellaDirectFluorescentAntibody(String rubellaDirectFluorescentAntibody) {
        this.rubellaDirectFluorescentAntibody = rubellaDirectFluorescentAntibody;
    }

    public void setRubellaMicroscopy(String rubellaMicroscopy) {
        this.rubellaMicroscopy = rubellaMicroscopy;
    }

    public void setRubellaNeutralizingAntibodies(String rubellaNeutralizingAntibodies) {
        this.rubellaNeutralizingAntibodies = rubellaNeutralizingAntibodies;
    }

    public void setRubellaPcrRtPcr(String rubellaPcrRtPcr) {
        this.rubellaPcrRtPcr = rubellaPcrRtPcr;
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

    public void setRubellaSequencing(String rubellaSequencing) {
        this.rubellaSequencing = rubellaSequencing;
    }

    public void setRubellaDnaMicroarray(String rubellaDnaMicroarray) {
        this.rubellaDnaMicroarray = rubellaDnaMicroarray;
    }

    public void setRubellaOther(String rubellaOther) {
        this.rubellaOther = rubellaOther;
    }

    public void setRubellaAntibodyDetectionDetails(String rubellaAntibodyDetectionDetails) {
        this.rubellaAntibodyDetectionDetails = rubellaAntibodyDetectionDetails;
    }

    public void setRubellaAntigenDetectionDetails(String rubellaAntigenDetectionDetails) {
        this.rubellaAntigenDetectionDetails = rubellaAntigenDetectionDetails;
    }

    public void setRubellaRapidTestDetails(String rubellaRapidTestDetails) {
        this.rubellaRapidTestDetails = rubellaRapidTestDetails;
    }

    public void setRubellaCultureDetails(String rubellaCultureDetails) {
        this.rubellaCultureDetails = rubellaCultureDetails;
    }

    public void setRubellaHistopathologyDetails(String rubellaHistopathologyDetails) {
        this.rubellaHistopathologyDetails = rubellaHistopathologyDetails;
    }

    public void setRubellaIsolationDetails(String rubellaIsolationDetails) {
        this.rubellaIsolationDetails = rubellaIsolationDetails;
    }

    public void setRubellaIgmSerumAntibodyDetails(String rubellaIgmSerumAntibodyDetails) {
        this.rubellaIgmSerumAntibodyDetails = rubellaIgmSerumAntibodyDetails;
    }

    public void setRubellaIggSerumAntibodyDetails(String rubellaIggSerumAntibodyDetails) {
        this.rubellaIggSerumAntibodyDetails = rubellaIggSerumAntibodyDetails;
    }

    public void setRubellaIgaSerumAntibodyDetails(String rubellaIgaSerumAntibodyDetails) {
        this.rubellaIgaSerumAntibodyDetails = rubellaIgaSerumAntibodyDetails;
    }

    public void setRubellaIncubationTimeDetails(String rubellaIncubationTimeDetails) {
        this.rubellaIncubationTimeDetails = rubellaIncubationTimeDetails;
    }

    public void setRubellaIndirectFluorescentAntibodyDetails(String rubellaIndirectFluorescentAntibodyDetails) {
        this.rubellaIndirectFluorescentAntibodyDetails = rubellaIndirectFluorescentAntibodyDetails;
    }

    public void setRubellaDirectFluorescentAntibodyDetails(String rubellaDirectFluorescentAntibodyDetails) {
        this.rubellaDirectFluorescentAntibodyDetails = rubellaDirectFluorescentAntibodyDetails;
    }

    public void setRubellaMicroscopyDetails(String rubellaMicroscopyDetails) {
        this.rubellaMicroscopyDetails = rubellaMicroscopyDetails;
    }

    public void setRubellaNeutralizingAntibodiesDetails(String rubellaNeutralizingAntibodiesDetails) {
        this.rubellaNeutralizingAntibodiesDetails = rubellaNeutralizingAntibodiesDetails;
    }

    public void setRubellaPcrRtPcrDetails(String rubellaPcrRtPcrDetails) {
        this.rubellaPcrRtPcrDetails = rubellaPcrRtPcrDetails;
    }

    public void setRubellaGramStainDetails(String rubellaGramStainDetails) {
        this.rubellaGramStainDetails = rubellaGramStainDetails;
    }

    public void setRubellaLatexAgglutinationDetails(String rubellaLatexAgglutinationDetails) {
        this.rubellaLatexAgglutinationDetails = rubellaLatexAgglutinationDetails;
    }

    public void setRubellaCqValueDetectionDetails(String rubellaCqValueDetectionDetails) {
        this.rubellaCqValueDetectionDetails = rubellaCqValueDetectionDetails;
    }

    public void setRubellaSequencingDetails(String rubellaSequencingDetails) {
        this.rubellaSequencingDetails = rubellaSequencingDetails;
    }

    public void setRubellaDnaMicroarrayDetails(String rubellaDnaMicroarrayDetails) {
        this.rubellaDnaMicroarrayDetails = rubellaDnaMicroarrayDetails;
    }

    public void setRubellaOtherDetails(String rubellaOtherDetails) {
        this.rubellaOtherDetails = rubellaOtherDetails;
    }

    public void setTuberculosisAntibodyDetection(String tuberculosisAntibodyDetection) {
        this.tuberculosisAntibodyDetection = tuberculosisAntibodyDetection;
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

    public void setTuberculosisDirectFluorescentAntibody(String tuberculosisDirectFluorescentAntibody) {
        this.tuberculosisDirectFluorescentAntibody = tuberculosisDirectFluorescentAntibody;
    }

    public void setTuberculosisMicroscopy(String tuberculosisMicroscopy) {
        this.tuberculosisMicroscopy = tuberculosisMicroscopy;
    }

    public void setTuberculosisNeutralizingAntibodies(String tuberculosisNeutralizingAntibodies) {
        this.tuberculosisNeutralizingAntibodies = tuberculosisNeutralizingAntibodies;
    }

    public void setTuberculosisPcrRtPcr(String tuberculosisPcrRtPcr) {
        this.tuberculosisPcrRtPcr = tuberculosisPcrRtPcr;
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

    public void setTuberculosisSequencing(String tuberculosisSequencing) {
        this.tuberculosisSequencing = tuberculosisSequencing;
    }

    public void setTuberculosisDnaMicroarray(String tuberculosisDnaMicroarray) {
        this.tuberculosisDnaMicroarray = tuberculosisDnaMicroarray;
    }

    public void setTuberculosisOther(String tuberculosisOther) {
        this.tuberculosisOther = tuberculosisOther;
    }

    public void setTuberculosisAntibodyDetectionDetails(String tuberculosisAntibodyDetectionDetails) {
        this.tuberculosisAntibodyDetectionDetails = tuberculosisAntibodyDetectionDetails;
    }

    public void setTuberculosisAntigenDetectionDetails(String tuberculosisAntigenDetectionDetails) {
        this.tuberculosisAntigenDetectionDetails = tuberculosisAntigenDetectionDetails;
    }

    public void setTuberculosisRapidTestDetails(String tuberculosisRapidTestDetails) {
        this.tuberculosisRapidTestDetails = tuberculosisRapidTestDetails;
    }

    public void setTuberculosisCultureDetails(String tuberculosisCultureDetails) {
        this.tuberculosisCultureDetails = tuberculosisCultureDetails;
    }

    public void setTuberculosisHistopathologyDetails(String tuberculosisHistopathologyDetails) {
        this.tuberculosisHistopathologyDetails = tuberculosisHistopathologyDetails;
    }

    public void setTuberculosisIsolationDetails(String tuberculosisIsolationDetails) {
        this.tuberculosisIsolationDetails = tuberculosisIsolationDetails;
    }

    public void setTuberculosisIgmSerumAntibodyDetails(String tuberculosisIgmSerumAntibodyDetails) {
        this.tuberculosisIgmSerumAntibodyDetails = tuberculosisIgmSerumAntibodyDetails;
    }

    public void setTuberculosisIggSerumAntibodyDetails(String tuberculosisIggSerumAntibodyDetails) {
        this.tuberculosisIggSerumAntibodyDetails = tuberculosisIggSerumAntibodyDetails;
    }

    public void setTuberculosisIgaSerumAntibodyDetails(String tuberculosisIgaSerumAntibodyDetails) {
        this.tuberculosisIgaSerumAntibodyDetails = tuberculosisIgaSerumAntibodyDetails;
    }

    public void setTuberculosisIncubationTimeDetails(String tuberculosisIncubationTimeDetails) {
        this.tuberculosisIncubationTimeDetails = tuberculosisIncubationTimeDetails;
    }

    public void setTuberculosisIndirectFluorescentAntibodyDetails(String tuberculosisIndirectFluorescentAntibodyDetails) {
        this.tuberculosisIndirectFluorescentAntibodyDetails = tuberculosisIndirectFluorescentAntibodyDetails;
    }

    public void setTuberculosisDirectFluorescentAntibodyDetails(String tuberculosisDirectFluorescentAntibodyDetails) {
        this.tuberculosisDirectFluorescentAntibodyDetails = tuberculosisDirectFluorescentAntibodyDetails;
    }

    public void setTuberculosisMicroscopyDetails(String tuberculosisMicroscopyDetails) {
        this.tuberculosisMicroscopyDetails = tuberculosisMicroscopyDetails;
    }

    public void setTuberculosisNeutralizingAntibodiesDetails(String tuberculosisNeutralizingAntibodiesDetails) {
        this.tuberculosisNeutralizingAntibodiesDetails = tuberculosisNeutralizingAntibodiesDetails;
    }

    public void setTuberculosisPcrRtPcrDetails(String tuberculosisPcrRtPcrDetails) {
        this.tuberculosisPcrRtPcrDetails = tuberculosisPcrRtPcrDetails;
    }

    public void setTuberculosisGramStainDetails(String tuberculosisGramStainDetails) {
        this.tuberculosisGramStainDetails = tuberculosisGramStainDetails;
    }

    public void setTuberculosisLatexAgglutinationDetails(String tuberculosisLatexAgglutinationDetails) {
        this.tuberculosisLatexAgglutinationDetails = tuberculosisLatexAgglutinationDetails;
    }

    public void setTuberculosisCqValueDetectionDetails(String tuberculosisCqValueDetectionDetails) {
        this.tuberculosisCqValueDetectionDetails = tuberculosisCqValueDetectionDetails;
    }

    public void setTuberculosisSequencingDetails(String tuberculosisSequencingDetails) {
        this.tuberculosisSequencingDetails = tuberculosisSequencingDetails;
    }

    public void setTuberculosisDnaMicroarrayDetails(String tuberculosisDnaMicroarrayDetails) {
        this.tuberculosisDnaMicroarrayDetails = tuberculosisDnaMicroarrayDetails;
    }

    public void setTuberculosisOtherDetails(String tuberculosisOtherDetails) {
        this.tuberculosisOtherDetails = tuberculosisOtherDetails;
    }

    public void setLeprosyAntibodyDetection(String leprosyAntibodyDetection) {
        this.leprosyAntibodyDetection = leprosyAntibodyDetection;
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

    public void setLeprosyDirectFluorescentAntibody(String leprosyDirectFluorescentAntibody) {
        this.leprosyDirectFluorescentAntibody = leprosyDirectFluorescentAntibody;
    }

    public void setLeprosyMicroscopy(String leprosyMicroscopy) {
        this.leprosyMicroscopy = leprosyMicroscopy;
    }

    public void setLeprosyNeutralizingAntibodies(String leprosyNeutralizingAntibodies) {
        this.leprosyNeutralizingAntibodies = leprosyNeutralizingAntibodies;
    }

    public void setLeprosyPcrRtPcr(String leprosyPcrRtPcr) {
        this.leprosyPcrRtPcr = leprosyPcrRtPcr;
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

    public void setLeprosySequencing(String leprosySequencing) {
        this.leprosySequencing = leprosySequencing;
    }

    public void setLeprosyDnaMicroarray(String leprosyDnaMicroarray) {
        this.leprosyDnaMicroarray = leprosyDnaMicroarray;
    }

    public void setLeprosyOther(String leprosyOther) {
        this.leprosyOther = leprosyOther;
    }

    public void setLeprosyAntibodyDetectionDetails(String leprosyAntibodyDetectionDetails) {
        this.leprosyAntibodyDetectionDetails = leprosyAntibodyDetectionDetails;
    }

    public void setLeprosyAntigenDetectionDetails(String leprosyAntigenDetectionDetails) {
        this.leprosyAntigenDetectionDetails = leprosyAntigenDetectionDetails;
    }

    public void setLeprosyRapidTestDetails(String leprosyRapidTestDetails) {
        this.leprosyRapidTestDetails = leprosyRapidTestDetails;
    }

    public void setLeprosyCultureDetails(String leprosyCultureDetails) {
        this.leprosyCultureDetails = leprosyCultureDetails;
    }

    public void setLeprosyHistopathologyDetails(String leprosyHistopathologyDetails) {
        this.leprosyHistopathologyDetails = leprosyHistopathologyDetails;
    }

    public void setLeprosyIsolationDetails(String leprosyIsolationDetails) {
        this.leprosyIsolationDetails = leprosyIsolationDetails;
    }

    public void setLeprosyIgmSerumAntibodyDetails(String leprosyIgmSerumAntibodyDetails) {
        this.leprosyIgmSerumAntibodyDetails = leprosyIgmSerumAntibodyDetails;
    }

    public void setLeprosyIggSerumAntibodyDetails(String leprosyIggSerumAntibodyDetails) {
        this.leprosyIggSerumAntibodyDetails = leprosyIggSerumAntibodyDetails;
    }

    public void setLeprosyIgaSerumAntibodyDetails(String leprosyIgaSerumAntibodyDetails) {
        this.leprosyIgaSerumAntibodyDetails = leprosyIgaSerumAntibodyDetails;
    }

    public void setLeprosyIncubationTimeDetails(String leprosyIncubationTimeDetails) {
        this.leprosyIncubationTimeDetails = leprosyIncubationTimeDetails;
    }

    public void setLeprosyIndirectFluorescentAntibodyDetails(String leprosyIndirectFluorescentAntibodyDetails) {
        this.leprosyIndirectFluorescentAntibodyDetails = leprosyIndirectFluorescentAntibodyDetails;
    }

    public void setLeprosyDirectFluorescentAntibodyDetails(String leprosyDirectFluorescentAntibodyDetails) {
        this.leprosyDirectFluorescentAntibodyDetails = leprosyDirectFluorescentAntibodyDetails;
    }

    public void setLeprosyMicroscopyDetails(String leprosyMicroscopyDetails) {
        this.leprosyMicroscopyDetails = leprosyMicroscopyDetails;
    }

    public void setLeprosyNeutralizingAntibodiesDetails(String leprosyNeutralizingAntibodiesDetails) {
        this.leprosyNeutralizingAntibodiesDetails = leprosyNeutralizingAntibodiesDetails;
    }

    public void setLeprosyPcrRtPcrDetails(String leprosyPcrRtPcrDetails) {
        this.leprosyPcrRtPcrDetails = leprosyPcrRtPcrDetails;
    }

    public void setLeprosyGramStainDetails(String leprosyGramStainDetails) {
        this.leprosyGramStainDetails = leprosyGramStainDetails;
    }

    public void setLeprosyLatexAgglutinationDetails(String leprosyLatexAgglutinationDetails) {
        this.leprosyLatexAgglutinationDetails = leprosyLatexAgglutinationDetails;
    }

    public void setLeprosyCqValueDetectionDetails(String leprosyCqValueDetectionDetails) {
        this.leprosyCqValueDetectionDetails = leprosyCqValueDetectionDetails;
    }

    public void setLeprosySequencingDetails(String leprosySequencingDetails) {
        this.leprosySequencingDetails = leprosySequencingDetails;
    }

    public void setLeprosyDnaMicroarrayDetails(String leprosyDnaMicroarrayDetails) {
        this.leprosyDnaMicroarrayDetails = leprosyDnaMicroarrayDetails;
    }

    public void setLeprosyOtherDetails(String leprosyOtherDetails) {
        this.leprosyOtherDetails = leprosyOtherDetails;
    }

    public void setLymphaticFilariasisAntibodyDetection(String lymphaticFilariasisAntibodyDetection) {
        this.lymphaticFilariasisAntibodyDetection = lymphaticFilariasisAntibodyDetection;
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

    public void setLymphaticFilariasisDirectFluorescentAntibody(String lymphaticFilariasisDirectFluorescentAntibody) {
        this.lymphaticFilariasisDirectFluorescentAntibody = lymphaticFilariasisDirectFluorescentAntibody;
    }

    public void setLymphaticFilariasisMicroscopy(String lymphaticFilariasisMicroscopy) {
        this.lymphaticFilariasisMicroscopy = lymphaticFilariasisMicroscopy;
    }

    public void setLymphaticFilariasisNeutralizingAntibodies(String lymphaticFilariasisNeutralizingAntibodies) {
        this.lymphaticFilariasisNeutralizingAntibodies = lymphaticFilariasisNeutralizingAntibodies;
    }

    public void setLymphaticFilariasisPcrRtPcr(String lymphaticFilariasisPcrRtPcr) {
        this.lymphaticFilariasisPcrRtPcr = lymphaticFilariasisPcrRtPcr;
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

    public void setLymphaticFilariasisSequencing(String lymphaticFilariasisSequencing) {
        this.lymphaticFilariasisSequencing = lymphaticFilariasisSequencing;
    }

    public void setLymphaticFilariasisDnaMicroarray(String lymphaticFilariasisDnaMicroarray) {
        this.lymphaticFilariasisDnaMicroarray = lymphaticFilariasisDnaMicroarray;
    }

    public void setLymphaticFilariasisOther(String lymphaticFilariasisOther) {
        this.lymphaticFilariasisOther = lymphaticFilariasisOther;
    }

    public void setLymphaticFilariasisAntibodyDetectionDetails(String lymphaticFilariasisAntibodyDetectionDetails) {
        this.lymphaticFilariasisAntibodyDetectionDetails = lymphaticFilariasisAntibodyDetectionDetails;
    }

    public void setLymphaticFilariasisAntigenDetectionDetails(String lymphaticFilariasisAntigenDetectionDetails) {
        this.lymphaticFilariasisAntigenDetectionDetails = lymphaticFilariasisAntigenDetectionDetails;
    }

    public void setLymphaticFilariasisRapidTestDetails(String lymphaticFilariasisRapidTestDetails) {
        this.lymphaticFilariasisRapidTestDetails = lymphaticFilariasisRapidTestDetails;
    }

    public void setLymphaticFilariasisCultureDetails(String lymphaticFilariasisCultureDetails) {
        this.lymphaticFilariasisCultureDetails = lymphaticFilariasisCultureDetails;
    }

    public void setLymphaticFilariasisHistopathologyDetails(String lymphaticFilariasisHistopathologyDetails) {
        this.lymphaticFilariasisHistopathologyDetails = lymphaticFilariasisHistopathologyDetails;
    }

    public void setLymphaticFilariasisIsolationDetails(String lymphaticFilariasisIsolationDetails) {
        this.lymphaticFilariasisIsolationDetails = lymphaticFilariasisIsolationDetails;
    }

    public void setLymphaticFilariasisIgmSerumAntibodyDetails(String lymphaticFilariasisIgmSerumAntibodyDetails) {
        this.lymphaticFilariasisIgmSerumAntibodyDetails = lymphaticFilariasisIgmSerumAntibodyDetails;
    }

    public void setLymphaticFilariasisIggSerumAntibodyDetails(String lymphaticFilariasisIggSerumAntibodyDetails) {
        this.lymphaticFilariasisIggSerumAntibodyDetails = lymphaticFilariasisIggSerumAntibodyDetails;
    }

    public void setLymphaticFilariasisIgaSerumAntibodyDetails(String lymphaticFilariasisIgaSerumAntibodyDetails) {
        this.lymphaticFilariasisIgaSerumAntibodyDetails = lymphaticFilariasisIgaSerumAntibodyDetails;
    }

    public void setLymphaticFilariasisIncubationTimeDetails(String lymphaticFilariasisIncubationTimeDetails) {
        this.lymphaticFilariasisIncubationTimeDetails = lymphaticFilariasisIncubationTimeDetails;
    }

    public void setLymphaticFilariasisIndirectFluorescentAntibodyDetails(String lymphaticFilariasisIndirectFluorescentAntibodyDetails) {
        this.lymphaticFilariasisIndirectFluorescentAntibodyDetails = lymphaticFilariasisIndirectFluorescentAntibodyDetails;
    }

    public void setLymphaticFilariasisDirectFluorescentAntibodyDetails(String lymphaticFilariasisDirectFluorescentAntibodyDetails) {
        this.lymphaticFilariasisDirectFluorescentAntibodyDetails = lymphaticFilariasisDirectFluorescentAntibodyDetails;
    }

    public void setLymphaticFilariasisMicroscopyDetails(String lymphaticFilariasisMicroscopyDetails) {
        this.lymphaticFilariasisMicroscopyDetails = lymphaticFilariasisMicroscopyDetails;
    }

    public void setLymphaticFilariasisNeutralizingAntibodiesDetails(String lymphaticFilariasisNeutralizingAntibodiesDetails) {
        this.lymphaticFilariasisNeutralizingAntibodiesDetails = lymphaticFilariasisNeutralizingAntibodiesDetails;
    }

    public void setLymphaticFilariasisPcrRtPcrDetails(String lymphaticFilariasisPcrRtPcrDetails) {
        this.lymphaticFilariasisPcrRtPcrDetails = lymphaticFilariasisPcrRtPcrDetails;
    }

    public void setLymphaticFilariasisGramStainDetails(String lymphaticFilariasisGramStainDetails) {
        this.lymphaticFilariasisGramStainDetails = lymphaticFilariasisGramStainDetails;
    }

    public void setLymphaticFilariasisLatexAgglutinationDetails(String lymphaticFilariasisLatexAgglutinationDetails) {
        this.lymphaticFilariasisLatexAgglutinationDetails = lymphaticFilariasisLatexAgglutinationDetails;
    }

    public void setLymphaticFilariasisCqValueDetectionDetails(String lymphaticFilariasisCqValueDetectionDetails) {
        this.lymphaticFilariasisCqValueDetectionDetails = lymphaticFilariasisCqValueDetectionDetails;
    }

    public void setLymphaticFilariasisSequencingDetails(String lymphaticFilariasisSequencingDetails) {
        this.lymphaticFilariasisSequencingDetails = lymphaticFilariasisSequencingDetails;
    }

    public void setLymphaticFilariasisDnaMicroarrayDetails(String lymphaticFilariasisDnaMicroarrayDetails) {
        this.lymphaticFilariasisDnaMicroarrayDetails = lymphaticFilariasisDnaMicroarrayDetails;
    }

    public void setLymphaticFilariasisOtherDetails(String lymphaticFilariasisOtherDetails) {
        this.lymphaticFilariasisOtherDetails = lymphaticFilariasisOtherDetails;
    }

    public void setBuruliUlcerAntibodyDetection(String buruliUlcerAntibodyDetection) {
        this.buruliUlcerAntibodyDetection = buruliUlcerAntibodyDetection;
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

    public void setBuruliUlcerDirectFluorescentAntibody(String buruliUlcerDirectFluorescentAntibody) {
        this.buruliUlcerDirectFluorescentAntibody = buruliUlcerDirectFluorescentAntibody;
    }

    public void setBuruliUlcerMicroscopy(String buruliUlcerMicroscopy) {
        this.buruliUlcerMicroscopy = buruliUlcerMicroscopy;
    }

    public void setBuruliUlcerNeutralizingAntibodies(String buruliUlcerNeutralizingAntibodies) {
        this.buruliUlcerNeutralizingAntibodies = buruliUlcerNeutralizingAntibodies;
    }

    public void setBuruliUlcerPcrRtPcr(String buruliUlcerPcrRtPcr) {
        this.buruliUlcerPcrRtPcr = buruliUlcerPcrRtPcr;
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

    public void setBuruliUlcerSequencing(String buruliUlcerSequencing) {
        this.buruliUlcerSequencing = buruliUlcerSequencing;
    }

    public void setBuruliUlcerDnaMicroarray(String buruliUlcerDnaMicroarray) {
        this.buruliUlcerDnaMicroarray = buruliUlcerDnaMicroarray;
    }

    public void setBuruliUlcerOther(String buruliUlcerOther) {
        this.buruliUlcerOther = buruliUlcerOther;
    }

    public void setBuruliUlcerAntibodyDetectionDetails(String buruliUlcerAntibodyDetectionDetails) {
        this.buruliUlcerAntibodyDetectionDetails = buruliUlcerAntibodyDetectionDetails;
    }

    public void setBuruliUlcerAntigenDetectionDetails(String buruliUlcerAntigenDetectionDetails) {
        this.buruliUlcerAntigenDetectionDetails = buruliUlcerAntigenDetectionDetails;
    }

    public void setBuruliUlcerRapidTestDetails(String buruliUlcerRapidTestDetails) {
        this.buruliUlcerRapidTestDetails = buruliUlcerRapidTestDetails;
    }

    public void setBuruliUlcerCultureDetails(String buruliUlcerCultureDetails) {
        this.buruliUlcerCultureDetails = buruliUlcerCultureDetails;
    }

    public void setBuruliUlcerHistopathologyDetails(String buruliUlcerHistopathologyDetails) {
        this.buruliUlcerHistopathologyDetails = buruliUlcerHistopathologyDetails;
    }

    public void setBuruliUlcerIsolationDetails(String buruliUlcerIsolationDetails) {
        this.buruliUlcerIsolationDetails = buruliUlcerIsolationDetails;
    }

    public void setBuruliUlcerIgmSerumAntibodyDetails(String buruliUlcerIgmSerumAntibodyDetails) {
        this.buruliUlcerIgmSerumAntibodyDetails = buruliUlcerIgmSerumAntibodyDetails;
    }

    public void setBuruliUlcerIggSerumAntibodyDetails(String buruliUlcerIggSerumAntibodyDetails) {
        this.buruliUlcerIggSerumAntibodyDetails = buruliUlcerIggSerumAntibodyDetails;
    }

    public void setBuruliUlcerIgaSerumAntibodyDetails(String buruliUlcerIgaSerumAntibodyDetails) {
        this.buruliUlcerIgaSerumAntibodyDetails = buruliUlcerIgaSerumAntibodyDetails;
    }

    public void setBuruliUlcerIncubationTimeDetails(String buruliUlcerIncubationTimeDetails) {
        this.buruliUlcerIncubationTimeDetails = buruliUlcerIncubationTimeDetails;
    }

    public void setBuruliUlcerIndirectFluorescentAntibodyDetails(String buruliUlcerIndirectFluorescentAntibodyDetails) {
        this.buruliUlcerIndirectFluorescentAntibodyDetails = buruliUlcerIndirectFluorescentAntibodyDetails;
    }

    public void setBuruliUlcerDirectFluorescentAntibodyDetails(String buruliUlcerDirectFluorescentAntibodyDetails) {
        this.buruliUlcerDirectFluorescentAntibodyDetails = buruliUlcerDirectFluorescentAntibodyDetails;
    }

    public void setBuruliUlcerMicroscopyDetails(String buruliUlcerMicroscopyDetails) {
        this.buruliUlcerMicroscopyDetails = buruliUlcerMicroscopyDetails;
    }

    public void setBuruliUlcerNeutralizingAntibodiesDetails(String buruliUlcerNeutralizingAntibodiesDetails) {
        this.buruliUlcerNeutralizingAntibodiesDetails = buruliUlcerNeutralizingAntibodiesDetails;
    }

    public void setBuruliUlcerPcrRtPcrDetails(String buruliUlcerPcrRtPcrDetails) {
        this.buruliUlcerPcrRtPcrDetails = buruliUlcerPcrRtPcrDetails;
    }

    public void setBuruliUlcerGramStainDetails(String buruliUlcerGramStainDetails) {
        this.buruliUlcerGramStainDetails = buruliUlcerGramStainDetails;
    }

    public void setBuruliUlcerLatexAgglutinationDetails(String buruliUlcerLatexAgglutinationDetails) {
        this.buruliUlcerLatexAgglutinationDetails = buruliUlcerLatexAgglutinationDetails;
    }

    public void setBuruliUlcerCqValueDetectionDetails(String buruliUlcerCqValueDetectionDetails) {
        this.buruliUlcerCqValueDetectionDetails = buruliUlcerCqValueDetectionDetails;
    }

    public void setBuruliUlcerSequencingDetails(String buruliUlcerSequencingDetails) {
        this.buruliUlcerSequencingDetails = buruliUlcerSequencingDetails;
    }

    public void setBuruliUlcerDnaMicroarrayDetails(String buruliUlcerDnaMicroarrayDetails) {
        this.buruliUlcerDnaMicroarrayDetails = buruliUlcerDnaMicroarrayDetails;
    }

    public void setBuruliUlcerOtherDetails(String buruliUlcerOtherDetails) {
        this.buruliUlcerOtherDetails = buruliUlcerOtherDetails;
    }

    public void setPertussisAntibodyDetection(String pertussisAntibodyDetection) {
        this.pertussisAntibodyDetection = pertussisAntibodyDetection;
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

    public void setPertussisDirectFluorescentAntibody(String pertussisDirectFluorescentAntibody) {
        this.pertussisDirectFluorescentAntibody = pertussisDirectFluorescentAntibody;
    }

    public void setPertussisMicroscopy(String pertussisMicroscopy) {
        this.pertussisMicroscopy = pertussisMicroscopy;
    }

    public void setPertussisNeutralizingAntibodies(String pertussisNeutralizingAntibodies) {
        this.pertussisNeutralizingAntibodies = pertussisNeutralizingAntibodies;
    }

    public void setPertussisPcrRtPcr(String pertussisPcrRtPcr) {
        this.pertussisPcrRtPcr = pertussisPcrRtPcr;
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

    public void setPertussisSequencing(String pertussisSequencing) {
        this.pertussisSequencing = pertussisSequencing;
    }

    public void setPertussisDnaMicroarray(String pertussisDnaMicroarray) {
        this.pertussisDnaMicroarray = pertussisDnaMicroarray;
    }

    public void setPertussisOther(String pertussisOther) {
        this.pertussisOther = pertussisOther;
    }

    public void setPertussisAntibodyDetectionDetails(String pertussisAntibodyDetectionDetails) {
        this.pertussisAntibodyDetectionDetails = pertussisAntibodyDetectionDetails;
    }

    public void setPertussisAntigenDetectionDetails(String pertussisAntigenDetectionDetails) {
        this.pertussisAntigenDetectionDetails = pertussisAntigenDetectionDetails;
    }

    public void setPertussisRapidTestDetails(String pertussisRapidTestDetails) {
        this.pertussisRapidTestDetails = pertussisRapidTestDetails;
    }

    public void setPertussisCultureDetails(String pertussisCultureDetails) {
        this.pertussisCultureDetails = pertussisCultureDetails;
    }

    public void setPertussisHistopathologyDetails(String pertussisHistopathologyDetails) {
        this.pertussisHistopathologyDetails = pertussisHistopathologyDetails;
    }

    public void setPertussisIsolationDetails(String pertussisIsolationDetails) {
        this.pertussisIsolationDetails = pertussisIsolationDetails;
    }

    public void setPertussisIgmSerumAntibodyDetails(String pertussisIgmSerumAntibodyDetails) {
        this.pertussisIgmSerumAntibodyDetails = pertussisIgmSerumAntibodyDetails;
    }

    public void setPertussisIggSerumAntibodyDetails(String pertussisIggSerumAntibodyDetails) {
        this.pertussisIggSerumAntibodyDetails = pertussisIggSerumAntibodyDetails;
    }

    public void setPertussisIgaSerumAntibodyDetails(String pertussisIgaSerumAntibodyDetails) {
        this.pertussisIgaSerumAntibodyDetails = pertussisIgaSerumAntibodyDetails;
    }

    public void setPertussisIncubationTimeDetails(String pertussisIncubationTimeDetails) {
        this.pertussisIncubationTimeDetails = pertussisIncubationTimeDetails;
    }

    public void setPertussisIndirectFluorescentAntibodyDetails(String pertussisIndirectFluorescentAntibodyDetails) {
        this.pertussisIndirectFluorescentAntibodyDetails = pertussisIndirectFluorescentAntibodyDetails;
    }

    public void setPertussisDirectFluorescentAntibodyDetails(String pertussisDirectFluorescentAntibodyDetails) {
        this.pertussisDirectFluorescentAntibodyDetails = pertussisDirectFluorescentAntibodyDetails;
    }

    public void setPertussisMicroscopyDetails(String pertussisMicroscopyDetails) {
        this.pertussisMicroscopyDetails = pertussisMicroscopyDetails;
    }

    public void setPertussisNeutralizingAntibodiesDetails(String pertussisNeutralizingAntibodiesDetails) {
        this.pertussisNeutralizingAntibodiesDetails = pertussisNeutralizingAntibodiesDetails;
    }

    public void setPertussisPcrRtPcrDetails(String pertussisPcrRtPcrDetails) {
        this.pertussisPcrRtPcrDetails = pertussisPcrRtPcrDetails;
    }

    public void setPertussisGramStainDetails(String pertussisGramStainDetails) {
        this.pertussisGramStainDetails = pertussisGramStainDetails;
    }

    public void setPertussisLatexAgglutinationDetails(String pertussisLatexAgglutinationDetails) {
        this.pertussisLatexAgglutinationDetails = pertussisLatexAgglutinationDetails;
    }

    public void setPertussisCqValueDetectionDetails(String pertussisCqValueDetectionDetails) {
        this.pertussisCqValueDetectionDetails = pertussisCqValueDetectionDetails;
    }

    public void setPertussisSequencingDetails(String pertussisSequencingDetails) {
        this.pertussisSequencingDetails = pertussisSequencingDetails;
    }

    public void setPertussisDnaMicroarrayDetails(String pertussisDnaMicroarrayDetails) {
        this.pertussisDnaMicroarrayDetails = pertussisDnaMicroarrayDetails;
    }

    public void setPertussisOtherDetails(String pertussisOtherDetails) {
        this.pertussisOtherDetails = pertussisOtherDetails;
    }

    public void setNeonatalTetanusAntibodyDetection(String neonatalTetanusAntibodyDetection) {
        this.neonatalTetanusAntibodyDetection = neonatalTetanusAntibodyDetection;
    }

    public void setNeonatalTetanusAntigenDetection(String neonatalTetanusAntigenDetection) {
        this.neonatalTetanusAntigenDetection = neonatalTetanusAntigenDetection;
    }

    public void setNeonatalTetanusRapidTest(String neonatalTetanusRapidTest) {
        this.neonatalTetanusRapidTest = neonatalTetanusRapidTest;
    }

    public void setNeonatalTetanusCulture(String neonatalTetanusCulture) {
        this.neonatalTetanusCulture = neonatalTetanusCulture;
    }

    public void setNeonatalTetanusHistopathology(String neonatalTetanusHistopathology) {
        this.neonatalTetanusHistopathology = neonatalTetanusHistopathology;
    }

    public void setNeonatalTetanusIsolation(String neonatalTetanusIsolation) {
        this.neonatalTetanusIsolation = neonatalTetanusIsolation;
    }

    public void setNeonatalTetanusIgmSerumAntibody(String neonatalTetanusIgmSerumAntibody) {
        this.neonatalTetanusIgmSerumAntibody = neonatalTetanusIgmSerumAntibody;
    }

    public void setNeonatalTetanusIggSerumAntibody(String neonatalTetanusIggSerumAntibody) {
        this.neonatalTetanusIggSerumAntibody = neonatalTetanusIggSerumAntibody;
    }

    public void setNeonatalTetanusIgaSerumAntibody(String neonatalTetanusIgaSerumAntibody) {
        this.neonatalTetanusIgaSerumAntibody = neonatalTetanusIgaSerumAntibody;
    }

    public void setNeonatalTetanusIncubationTime(String neonatalTetanusIncubationTime) {
        this.neonatalTetanusIncubationTime = neonatalTetanusIncubationTime;
    }

    public void setNeonatalTetanusIndirectFluorescentAntibody(String neonatalTetanusIndirectFluorescentAntibody) {
        this.neonatalTetanusIndirectFluorescentAntibody = neonatalTetanusIndirectFluorescentAntibody;
    }

    public void setNeonatalTetanusDirectFluorescentAntibody(String neonatalTetanusDirectFluorescentAntibody) {
        this.neonatalTetanusDirectFluorescentAntibody = neonatalTetanusDirectFluorescentAntibody;
    }

    public void setNeonatalTetanusMicroscopy(String neonatalTetanusMicroscopy) {
        this.neonatalTetanusMicroscopy = neonatalTetanusMicroscopy;
    }

    public void setNeonatalTetanusNeutralizingAntibodies(String neonatalTetanusNeutralizingAntibodies) {
        this.neonatalTetanusNeutralizingAntibodies = neonatalTetanusNeutralizingAntibodies;
    }

    public void setNeonatalTetanusPcrRtPcr(String neonatalTetanusPcrRtPcr) {
        this.neonatalTetanusPcrRtPcr = neonatalTetanusPcrRtPcr;
    }

    public void setNeonatalTetanusGramStain(String neonatalTetanusGramStain) {
        this.neonatalTetanusGramStain = neonatalTetanusGramStain;
    }

    public void setNeonatalTetanusLatexAgglutination(String neonatalTetanusLatexAgglutination) {
        this.neonatalTetanusLatexAgglutination = neonatalTetanusLatexAgglutination;
    }

    public void setNeonatalTetanusCqValueDetection(String neonatalTetanusCqValueDetection) {
        this.neonatalTetanusCqValueDetection = neonatalTetanusCqValueDetection;
    }

    public void setNeonatalTetanusSequencing(String neonatalTetanusSequencing) {
        this.neonatalTetanusSequencing = neonatalTetanusSequencing;
    }

    public void setNeonatalTetanusDnaMicroarray(String neonatalTetanusDnaMicroarray) {
        this.neonatalTetanusDnaMicroarray = neonatalTetanusDnaMicroarray;
    }

    public void setNeonatalTetanusOther(String neonatalTetanusOther) {
        this.neonatalTetanusOther = neonatalTetanusOther;
    }

    public void setNeonatalTetanusAntibodyDetectionDetails(String neonatalTetanusAntibodyDetectionDetails) {
        this.neonatalTetanusAntibodyDetectionDetails = neonatalTetanusAntibodyDetectionDetails;
    }

    public void setNeonatalTetanusAntigenDetectionDetails(String neonatalTetanusAntigenDetectionDetails) {
        this.neonatalTetanusAntigenDetectionDetails = neonatalTetanusAntigenDetectionDetails;
    }

    public void setNeonatalTetanusRapidTestDetails(String neonatalTetanusRapidTestDetails) {
        this.neonatalTetanusRapidTestDetails = neonatalTetanusRapidTestDetails;
    }

    public void setNeonatalTetanusCultureDetails(String neonatalTetanusCultureDetails) {
        this.neonatalTetanusCultureDetails = neonatalTetanusCultureDetails;
    }

    public void setNeonatalTetanusHistopathologyDetails(String neonatalTetanusHistopathologyDetails) {
        this.neonatalTetanusHistopathologyDetails = neonatalTetanusHistopathologyDetails;
    }

    public void setNeonatalTetanusIsolationDetails(String neonatalTetanusIsolationDetails) {
        this.neonatalTetanusIsolationDetails = neonatalTetanusIsolationDetails;
    }

    public void setNeonatalTetanusIgmSerumAntibodyDetails(String neonatalTetanusIgmSerumAntibodyDetails) {
        this.neonatalTetanusIgmSerumAntibodyDetails = neonatalTetanusIgmSerumAntibodyDetails;
    }

    public void setNeonatalTetanusIggSerumAntibodyDetails(String neonatalTetanusIggSerumAntibodyDetails) {
        this.neonatalTetanusIggSerumAntibodyDetails = neonatalTetanusIggSerumAntibodyDetails;
    }

    public void setNeonatalTetanusIgaSerumAntibodyDetails(String neonatalTetanusIgaSerumAntibodyDetails) {
        this.neonatalTetanusIgaSerumAntibodyDetails = neonatalTetanusIgaSerumAntibodyDetails;
    }

    public void setNeonatalTetanusIncubationTimeDetails(String neonatalTetanusIncubationTimeDetails) {
        this.neonatalTetanusIncubationTimeDetails = neonatalTetanusIncubationTimeDetails;
    }

    public void setNeonatalTetanusIndirectFluorescentAntibodyDetails(String neonatalTetanusIndirectFluorescentAntibodyDetails) {
        this.neonatalTetanusIndirectFluorescentAntibodyDetails = neonatalTetanusIndirectFluorescentAntibodyDetails;
    }

    public void setNeonatalTetanusDirectFluorescentAntibodyDetails(String neonatalTetanusDirectFluorescentAntibodyDetails) {
        this.neonatalTetanusDirectFluorescentAntibodyDetails = neonatalTetanusDirectFluorescentAntibodyDetails;
    }

    public void setNeonatalTetanusMicroscopyDetails(String neonatalTetanusMicroscopyDetails) {
        this.neonatalTetanusMicroscopyDetails = neonatalTetanusMicroscopyDetails;
    }

    public void setNeonatalTetanusNeutralizingAntibodiesDetails(String neonatalTetanusNeutralizingAntibodiesDetails) {
        this.neonatalTetanusNeutralizingAntibodiesDetails = neonatalTetanusNeutralizingAntibodiesDetails;
    }

    public void setNeonatalTetanusPcrRtPcrDetails(String neonatalTetanusPcrRtPcrDetails) {
        this.neonatalTetanusPcrRtPcrDetails = neonatalTetanusPcrRtPcrDetails;
    }

    public void setNeonatalTetanusGramStainDetails(String neonatalTetanusGramStainDetails) {
        this.neonatalTetanusGramStainDetails = neonatalTetanusGramStainDetails;
    }

    public void setNeonatalTetanusLatexAgglutinationDetails(String neonatalTetanusLatexAgglutinationDetails) {
        this.neonatalTetanusLatexAgglutinationDetails = neonatalTetanusLatexAgglutinationDetails;
    }

    public void setNeonatalTetanusCqValueDetectionDetails(String neonatalTetanusCqValueDetectionDetails) {
        this.neonatalTetanusCqValueDetectionDetails = neonatalTetanusCqValueDetectionDetails;
    }

    public void setNeonatalTetanusSequencingDetails(String neonatalTetanusSequencingDetails) {
        this.neonatalTetanusSequencingDetails = neonatalTetanusSequencingDetails;
    }

    public void setNeonatalTetanusDnaMicroarrayDetails(String neonatalTetanusDnaMicroarrayDetails) {
        this.neonatalTetanusDnaMicroarrayDetails = neonatalTetanusDnaMicroarrayDetails;
    }

    public void setNeonatalTetanusOtherDetails(String neonatalTetanusOtherDetails) {
        this.neonatalTetanusOtherDetails = neonatalTetanusOtherDetails;
    }

    public void setOnchocerciasisAntibodyDetection(String onchocerciasisAntibodyDetection) {
        this.onchocerciasisAntibodyDetection = onchocerciasisAntibodyDetection;
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

    public void setOnchocerciasisDirectFluorescentAntibody(String onchocerciasisDirectFluorescentAntibody) {
        this.onchocerciasisDirectFluorescentAntibody = onchocerciasisDirectFluorescentAntibody;
    }

    public void setOnchocerciasisMicroscopy(String onchocerciasisMicroscopy) {
        this.onchocerciasisMicroscopy = onchocerciasisMicroscopy;
    }

    public void setOnchocerciasisNeutralizingAntibodies(String onchocerciasisNeutralizingAntibodies) {
        this.onchocerciasisNeutralizingAntibodies = onchocerciasisNeutralizingAntibodies;
    }

    public void setOnchocerciasisPcrRtPcr(String onchocerciasisPcrRtPcr) {
        this.onchocerciasisPcrRtPcr = onchocerciasisPcrRtPcr;
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

    public void setOnchocerciasisSequencing(String onchocerciasisSequencing) {
        this.onchocerciasisSequencing = onchocerciasisSequencing;
    }

    public void setOnchocerciasisDnaMicroarray(String onchocerciasisDnaMicroarray) {
        this.onchocerciasisDnaMicroarray = onchocerciasisDnaMicroarray;
    }

    public void setOnchocerciasisOther(String onchocerciasisOther) {
        this.onchocerciasisOther = onchocerciasisOther;
    }

    public void setOnchocerciasisAntibodyDetectionDetails(String onchocerciasisAntibodyDetectionDetails) {
        this.onchocerciasisAntibodyDetectionDetails = onchocerciasisAntibodyDetectionDetails;
    }

    public void setOnchocerciasisAntigenDetectionDetails(String onchocerciasisAntigenDetectionDetails) {
        this.onchocerciasisAntigenDetectionDetails = onchocerciasisAntigenDetectionDetails;
    }

    public void setOnchocerciasisRapidTestDetails(String onchocerciasisRapidTestDetails) {
        this.onchocerciasisRapidTestDetails = onchocerciasisRapidTestDetails;
    }

    public void setOnchocerciasisCultureDetails(String onchocerciasisCultureDetails) {
        this.onchocerciasisCultureDetails = onchocerciasisCultureDetails;
    }

    public void setOnchocerciasisHistopathologyDetails(String onchocerciasisHistopathologyDetails) {
        this.onchocerciasisHistopathologyDetails = onchocerciasisHistopathologyDetails;
    }

    public void setOnchocerciasisIsolationDetails(String onchocerciasisIsolationDetails) {
        this.onchocerciasisIsolationDetails = onchocerciasisIsolationDetails;
    }

    public void setOnchocerciasisIgmSerumAntibodyDetails(String onchocerciasisIgmSerumAntibodyDetails) {
        this.onchocerciasisIgmSerumAntibodyDetails = onchocerciasisIgmSerumAntibodyDetails;
    }

    public void setOnchocerciasisIggSerumAntibodyDetails(String onchocerciasisIggSerumAntibodyDetails) {
        this.onchocerciasisIggSerumAntibodyDetails = onchocerciasisIggSerumAntibodyDetails;
    }

    public void setOnchocerciasisIgaSerumAntibodyDetails(String onchocerciasisIgaSerumAntibodyDetails) {
        this.onchocerciasisIgaSerumAntibodyDetails = onchocerciasisIgaSerumAntibodyDetails;
    }

    public void setOnchocerciasisIncubationTimeDetails(String onchocerciasisIncubationTimeDetails) {
        this.onchocerciasisIncubationTimeDetails = onchocerciasisIncubationTimeDetails;
    }

    public void setOnchocerciasisIndirectFluorescentAntibodyDetails(String onchocerciasisIndirectFluorescentAntibodyDetails) {
        this.onchocerciasisIndirectFluorescentAntibodyDetails = onchocerciasisIndirectFluorescentAntibodyDetails;
    }

    public void setOnchocerciasisDirectFluorescentAntibodyDetails(String onchocerciasisDirectFluorescentAntibodyDetails) {
        this.onchocerciasisDirectFluorescentAntibodyDetails = onchocerciasisDirectFluorescentAntibodyDetails;
    }

    public void setOnchocerciasisMicroscopyDetails(String onchocerciasisMicroscopyDetails) {
        this.onchocerciasisMicroscopyDetails = onchocerciasisMicroscopyDetails;
    }

    public void setOnchocerciasisNeutralizingAntibodiesDetails(String onchocerciasisNeutralizingAntibodiesDetails) {
        this.onchocerciasisNeutralizingAntibodiesDetails = onchocerciasisNeutralizingAntibodiesDetails;
    }

    public void setOnchocerciasisPcrRtPcrDetails(String onchocerciasisPcrRtPcrDetails) {
        this.onchocerciasisPcrRtPcrDetails = onchocerciasisPcrRtPcrDetails;
    }

    public void setOnchocerciasisGramStainDetails(String onchocerciasisGramStainDetails) {
        this.onchocerciasisGramStainDetails = onchocerciasisGramStainDetails;
    }

    public void setOnchocerciasisLatexAgglutinationDetails(String onchocerciasisLatexAgglutinationDetails) {
        this.onchocerciasisLatexAgglutinationDetails = onchocerciasisLatexAgglutinationDetails;
    }

    public void setOnchocerciasisCqValueDetectionDetails(String onchocerciasisCqValueDetectionDetails) {
        this.onchocerciasisCqValueDetectionDetails = onchocerciasisCqValueDetectionDetails;
    }

    public void setOnchocerciasisSequencingDetails(String onchocerciasisSequencingDetails) {
        this.onchocerciasisSequencingDetails = onchocerciasisSequencingDetails;
    }

    public void setOnchocerciasisDnaMicroarrayDetails(String onchocerciasisDnaMicroarrayDetails) {
        this.onchocerciasisDnaMicroarrayDetails = onchocerciasisDnaMicroarrayDetails;
    }

    public void setOnchocerciasisOtherDetails(String onchocerciasisOtherDetails) {
        this.onchocerciasisOtherDetails = onchocerciasisOtherDetails;
    }

    public void setDiphteriaAntibodyDetection(String diphteriaAntibodyDetection) {
        this.diphteriaAntibodyDetection = diphteriaAntibodyDetection;
    }

    public void setDiphteriaAntigenDetection(String diphteriaAntigenDetection) {
        this.diphteriaAntigenDetection = diphteriaAntigenDetection;
    }

    public void setDiphteriaRapidTest(String diphteriaRapidTest) {
        this.diphteriaRapidTest = diphteriaRapidTest;
    }

    public void setDiphteriaCulture(String diphteriaCulture) {
        this.diphteriaCulture = diphteriaCulture;
    }

    public void setDiphteriaHistopathology(String diphteriaHistopathology) {
        this.diphteriaHistopathology = diphteriaHistopathology;
    }

    public void setDiphteriaIsolation(String diphteriaIsolation) {
        this.diphteriaIsolation = diphteriaIsolation;
    }

    public void setDiphteriaIgmSerumAntibody(String diphteriaIgmSerumAntibody) {
        this.diphteriaIgmSerumAntibody = diphteriaIgmSerumAntibody;
    }

    public void setDiphteriaIggSerumAntibody(String diphteriaIggSerumAntibody) {
        this.diphteriaIggSerumAntibody = diphteriaIggSerumAntibody;
    }

    public void setDiphteriaIgaSerumAntibody(String diphteriaIgaSerumAntibody) {
        this.diphteriaIgaSerumAntibody = diphteriaIgaSerumAntibody;
    }

    public void setDiphteriaIncubationTime(String diphteriaIncubationTime) {
        this.diphteriaIncubationTime = diphteriaIncubationTime;
    }

    public void setDiphteriaIndirectFluorescentAntibody(String diphteriaIndirectFluorescentAntibody) {
        this.diphteriaIndirectFluorescentAntibody = diphteriaIndirectFluorescentAntibody;
    }

    public void setDiphteriaDirectFluorescentAntibody(String diphteriaDirectFluorescentAntibody) {
        this.diphteriaDirectFluorescentAntibody = diphteriaDirectFluorescentAntibody;
    }

    public void setDiphteriaMicroscopy(String diphteriaMicroscopy) {
        this.diphteriaMicroscopy = diphteriaMicroscopy;
    }

    public void setDiphteriaNeutralizingAntibodies(String diphteriaNeutralizingAntibodies) {
        this.diphteriaNeutralizingAntibodies = diphteriaNeutralizingAntibodies;
    }

    public void setDiphteriaPcrRtPcr(String diphteriaPcrRtPcr) {
        this.diphteriaPcrRtPcr = diphteriaPcrRtPcr;
    }

    public void setDiphteriaGramStain(String diphteriaGramStain) {
        this.diphteriaGramStain = diphteriaGramStain;
    }

    public void setDiphteriaLatexAgglutination(String diphteriaLatexAgglutination) {
        this.diphteriaLatexAgglutination = diphteriaLatexAgglutination;
    }

    public void setDiphteriaCqValueDetection(String diphteriaCqValueDetection) {
        this.diphteriaCqValueDetection = diphteriaCqValueDetection;
    }

    public void setDiphteriaSequencing(String diphteriaSequencing) {
        this.diphteriaSequencing = diphteriaSequencing;
    }

    public void setDiphteriaDnaMicroarray(String diphteriaDnaMicroarray) {
        this.diphteriaDnaMicroarray = diphteriaDnaMicroarray;
    }

    public void setDiphteriaOther(String diphteriaOther) {
        this.diphteriaOther = diphteriaOther;
    }

    public void setDiphteriaAntibodyDetectionDetails(String diphteriaAntibodyDetectionDetails) {
        this.diphteriaAntibodyDetectionDetails = diphteriaAntibodyDetectionDetails;
    }

    public void setDiphteriaAntigenDetectionDetails(String diphteriaAntigenDetectionDetails) {
        this.diphteriaAntigenDetectionDetails = diphteriaAntigenDetectionDetails;
    }

    public void setDiphteriaRapidTestDetails(String diphteriaRapidTestDetails) {
        this.diphteriaRapidTestDetails = diphteriaRapidTestDetails;
    }

    public void setDiphteriaCultureDetails(String diphteriaCultureDetails) {
        this.diphteriaCultureDetails = diphteriaCultureDetails;
    }

    public void setDiphteriaHistopathologyDetails(String diphteriaHistopathologyDetails) {
        this.diphteriaHistopathologyDetails = diphteriaHistopathologyDetails;
    }

    public void setDiphteriaIsolationDetails(String diphteriaIsolationDetails) {
        this.diphteriaIsolationDetails = diphteriaIsolationDetails;
    }

    public void setDiphteriaIgmSerumAntibodyDetails(String diphteriaIgmSerumAntibodyDetails) {
        this.diphteriaIgmSerumAntibodyDetails = diphteriaIgmSerumAntibodyDetails;
    }

    public void setDiphteriaIggSerumAntibodyDetails(String diphteriaIggSerumAntibodyDetails) {
        this.diphteriaIggSerumAntibodyDetails = diphteriaIggSerumAntibodyDetails;
    }

    public void setDiphteriaIgaSerumAntibodyDetails(String diphteriaIgaSerumAntibodyDetails) {
        this.diphteriaIgaSerumAntibodyDetails = diphteriaIgaSerumAntibodyDetails;
    }

    public void setDiphteriaIncubationTimeDetails(String diphteriaIncubationTimeDetails) {
        this.diphteriaIncubationTimeDetails = diphteriaIncubationTimeDetails;
    }

    public void setDiphteriaIndirectFluorescentAntibodyDetails(String diphteriaIndirectFluorescentAntibodyDetails) {
        this.diphteriaIndirectFluorescentAntibodyDetails = diphteriaIndirectFluorescentAntibodyDetails;
    }

    public void setDiphteriaDirectFluorescentAntibodyDetails(String diphteriaDirectFluorescentAntibodyDetails) {
        this.diphteriaDirectFluorescentAntibodyDetails = diphteriaDirectFluorescentAntibodyDetails;
    }

    public void setDiphteriaMicroscopyDetails(String diphteriaMicroscopyDetails) {
        this.diphteriaMicroscopyDetails = diphteriaMicroscopyDetails;
    }

    public void setDiphteriaNeutralizingAntibodiesDetails(String diphteriaNeutralizingAntibodiesDetails) {
        this.diphteriaNeutralizingAntibodiesDetails = diphteriaNeutralizingAntibodiesDetails;
    }

    public void setDiphteriaPcrRtPcrDetails(String diphteriaPcrRtPcrDetails) {
        this.diphteriaPcrRtPcrDetails = diphteriaPcrRtPcrDetails;
    }

    public void setDiphteriaGramStainDetails(String diphteriaGramStainDetails) {
        this.diphteriaGramStainDetails = diphteriaGramStainDetails;
    }

    public void setDiphteriaLatexAgglutinationDetails(String diphteriaLatexAgglutinationDetails) {
        this.diphteriaLatexAgglutinationDetails = diphteriaLatexAgglutinationDetails;
    }

    public void setDiphteriaCqValueDetectionDetails(String diphteriaCqValueDetectionDetails) {
        this.diphteriaCqValueDetectionDetails = diphteriaCqValueDetectionDetails;
    }

    public void setDiphteriaSequencingDetails(String diphteriaSequencingDetails) {
        this.diphteriaSequencingDetails = diphteriaSequencingDetails;
    }

    public void setDiphteriaDnaMicroarrayDetails(String diphteriaDnaMicroarrayDetails) {
        this.diphteriaDnaMicroarrayDetails = diphteriaDnaMicroarrayDetails;
    }

    public void setDiphteriaOtherDetails(String diphteriaOtherDetails) {
        this.diphteriaOtherDetails = diphteriaOtherDetails;
    }

    public void setTrachomaAntibodyDetection(String trachomaAntibodyDetection) {
        this.trachomaAntibodyDetection = trachomaAntibodyDetection;
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

    public void setTrachomaDirectFluorescentAntibody(String trachomaDirectFluorescentAntibody) {
        this.trachomaDirectFluorescentAntibody = trachomaDirectFluorescentAntibody;
    }

    public void setTrachomaMicroscopy(String trachomaMicroscopy) {
        this.trachomaMicroscopy = trachomaMicroscopy;
    }

    public void setTrachomaNeutralizingAntibodies(String trachomaNeutralizingAntibodies) {
        this.trachomaNeutralizingAntibodies = trachomaNeutralizingAntibodies;
    }

    public void setTrachomaPcrRtPcr(String trachomaPcrRtPcr) {
        this.trachomaPcrRtPcr = trachomaPcrRtPcr;
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

    public void setTrachomaSequencing(String trachomaSequencing) {
        this.trachomaSequencing = trachomaSequencing;
    }

    public void setTrachomaDnaMicroarray(String trachomaDnaMicroarray) {
        this.trachomaDnaMicroarray = trachomaDnaMicroarray;
    }

    public void setTrachomaOther(String trachomaOther) {
        this.trachomaOther = trachomaOther;
    }

    public void setTrachomaAntibodyDetectionDetails(String trachomaAntibodyDetectionDetails) {
        this.trachomaAntibodyDetectionDetails = trachomaAntibodyDetectionDetails;
    }

    public void setTrachomaAntigenDetectionDetails(String trachomaAntigenDetectionDetails) {
        this.trachomaAntigenDetectionDetails = trachomaAntigenDetectionDetails;
    }

    public void setTrachomaRapidTestDetails(String trachomaRapidTestDetails) {
        this.trachomaRapidTestDetails = trachomaRapidTestDetails;
    }

    public void setTrachomaCultureDetails(String trachomaCultureDetails) {
        this.trachomaCultureDetails = trachomaCultureDetails;
    }

    public void setTrachomaHistopathologyDetails(String trachomaHistopathologyDetails) {
        this.trachomaHistopathologyDetails = trachomaHistopathologyDetails;
    }

    public void setTrachomaIsolationDetails(String trachomaIsolationDetails) {
        this.trachomaIsolationDetails = trachomaIsolationDetails;
    }

    public void setTrachomaIgmSerumAntibodyDetails(String trachomaIgmSerumAntibodyDetails) {
        this.trachomaIgmSerumAntibodyDetails = trachomaIgmSerumAntibodyDetails;
    }

    public void setTrachomaIggSerumAntibodyDetails(String trachomaIggSerumAntibodyDetails) {
        this.trachomaIggSerumAntibodyDetails = trachomaIggSerumAntibodyDetails;
    }

    public void setTrachomaIgaSerumAntibodyDetails(String trachomaIgaSerumAntibodyDetails) {
        this.trachomaIgaSerumAntibodyDetails = trachomaIgaSerumAntibodyDetails;
    }

    public void setTrachomaIncubationTimeDetails(String trachomaIncubationTimeDetails) {
        this.trachomaIncubationTimeDetails = trachomaIncubationTimeDetails;
    }

    public void setTrachomaIndirectFluorescentAntibodyDetails(String trachomaIndirectFluorescentAntibodyDetails) {
        this.trachomaIndirectFluorescentAntibodyDetails = trachomaIndirectFluorescentAntibodyDetails;
    }

    public void setTrachomaDirectFluorescentAntibodyDetails(String trachomaDirectFluorescentAntibodyDetails) {
        this.trachomaDirectFluorescentAntibodyDetails = trachomaDirectFluorescentAntibodyDetails;
    }

    public void setTrachomaMicroscopyDetails(String trachomaMicroscopyDetails) {
        this.trachomaMicroscopyDetails = trachomaMicroscopyDetails;
    }

    public void setTrachomaNeutralizingAntibodiesDetails(String trachomaNeutralizingAntibodiesDetails) {
        this.trachomaNeutralizingAntibodiesDetails = trachomaNeutralizingAntibodiesDetails;
    }

    public void setTrachomaPcrRtPcrDetails(String trachomaPcrRtPcrDetails) {
        this.trachomaPcrRtPcrDetails = trachomaPcrRtPcrDetails;
    }

    public void setTrachomaGramStainDetails(String trachomaGramStainDetails) {
        this.trachomaGramStainDetails = trachomaGramStainDetails;
    }

    public void setTrachomaLatexAgglutinationDetails(String trachomaLatexAgglutinationDetails) {
        this.trachomaLatexAgglutinationDetails = trachomaLatexAgglutinationDetails;
    }

    public void setTrachomaCqValueDetectionDetails(String trachomaCqValueDetectionDetails) {
        this.trachomaCqValueDetectionDetails = trachomaCqValueDetectionDetails;
    }

    public void setTrachomaSequencingDetails(String trachomaSequencingDetails) {
        this.trachomaSequencingDetails = trachomaSequencingDetails;
    }

    public void setTrachomaDnaMicroarrayDetails(String trachomaDnaMicroarrayDetails) {
        this.trachomaDnaMicroarrayDetails = trachomaDnaMicroarrayDetails;
    }

    public void setTrachomaOtherDetails(String trachomaOtherDetails) {
        this.trachomaOtherDetails = trachomaOtherDetails;
    }

    public void setYawsEndemicSyphilisAntibodyDetection(String yawsEndemicSyphilisAntibodyDetection) {
        this.yawsEndemicSyphilisAntibodyDetection = yawsEndemicSyphilisAntibodyDetection;
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

    public void setYawsEndemicSyphilisDirectFluorescentAntibody(String yawsEndemicSyphilisDirectFluorescentAntibody) {
        this.yawsEndemicSyphilisDirectFluorescentAntibody = yawsEndemicSyphilisDirectFluorescentAntibody;
    }

    public void setYawsEndemicSyphilisMicroscopy(String yawsEndemicSyphilisMicroscopy) {
        this.yawsEndemicSyphilisMicroscopy = yawsEndemicSyphilisMicroscopy;
    }

    public void setYawsEndemicSyphilisNeutralizingAntibodies(String yawsEndemicSyphilisNeutralizingAntibodies) {
        this.yawsEndemicSyphilisNeutralizingAntibodies = yawsEndemicSyphilisNeutralizingAntibodies;
    }

    public void setYawsEndemicSyphilisPcrRtPcr(String yawsEndemicSyphilisPcrRtPcr) {
        this.yawsEndemicSyphilisPcrRtPcr = yawsEndemicSyphilisPcrRtPcr;
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

    public void setYawsEndemicSyphilisSequencing(String yawsEndemicSyphilisSequencing) {
        this.yawsEndemicSyphilisSequencing = yawsEndemicSyphilisSequencing;
    }

    public void setYawsEndemicSyphilisDnaMicroarray(String yawsEndemicSyphilisDnaMicroarray) {
        this.yawsEndemicSyphilisDnaMicroarray = yawsEndemicSyphilisDnaMicroarray;
    }

    public void setYawsEndemicSyphilisOther(String yawsEndemicSyphilisOther) {
        this.yawsEndemicSyphilisOther = yawsEndemicSyphilisOther;
    }

    public void setYawsEndemicSyphilisAntibodyDetectionDetails(String yawsEndemicSyphilisAntibodyDetectionDetails) {
        this.yawsEndemicSyphilisAntibodyDetectionDetails = yawsEndemicSyphilisAntibodyDetectionDetails;
    }

    public void setYawsEndemicSyphilisAntigenDetectionDetails(String yawsEndemicSyphilisAntigenDetectionDetails) {
        this.yawsEndemicSyphilisAntigenDetectionDetails = yawsEndemicSyphilisAntigenDetectionDetails;
    }

    public void setYawsEndemicSyphilisRapidTestDetails(String yawsEndemicSyphilisRapidTestDetails) {
        this.yawsEndemicSyphilisRapidTestDetails = yawsEndemicSyphilisRapidTestDetails;
    }

    public void setYawsEndemicSyphilisCultureDetails(String yawsEndemicSyphilisCultureDetails) {
        this.yawsEndemicSyphilisCultureDetails = yawsEndemicSyphilisCultureDetails;
    }

    public void setYawsEndemicSyphilisHistopathologyDetails(String yawsEndemicSyphilisHistopathologyDetails) {
        this.yawsEndemicSyphilisHistopathologyDetails = yawsEndemicSyphilisHistopathologyDetails;
    }

    public void setYawsEndemicSyphilisIsolationDetails(String yawsEndemicSyphilisIsolationDetails) {
        this.yawsEndemicSyphilisIsolationDetails = yawsEndemicSyphilisIsolationDetails;
    }

    public void setYawsEndemicSyphilisIgmSerumAntibodyDetails(String yawsEndemicSyphilisIgmSerumAntibodyDetails) {
        this.yawsEndemicSyphilisIgmSerumAntibodyDetails = yawsEndemicSyphilisIgmSerumAntibodyDetails;
    }

    public void setYawsEndemicSyphilisIggSerumAntibodyDetails(String yawsEndemicSyphilisIggSerumAntibodyDetails) {
        this.yawsEndemicSyphilisIggSerumAntibodyDetails = yawsEndemicSyphilisIggSerumAntibodyDetails;
    }

    public void setYawsEndemicSyphilisIgaSerumAntibodyDetails(String yawsEndemicSyphilisIgaSerumAntibodyDetails) {
        this.yawsEndemicSyphilisIgaSerumAntibodyDetails = yawsEndemicSyphilisIgaSerumAntibodyDetails;
    }

    public void setYawsEndemicSyphilisIncubationTimeDetails(String yawsEndemicSyphilisIncubationTimeDetails) {
        this.yawsEndemicSyphilisIncubationTimeDetails = yawsEndemicSyphilisIncubationTimeDetails;
    }

    public void setYawsEndemicSyphilisIndirectFluorescentAntibodyDetails(String yawsEndemicSyphilisIndirectFluorescentAntibodyDetails) {
        this.yawsEndemicSyphilisIndirectFluorescentAntibodyDetails = yawsEndemicSyphilisIndirectFluorescentAntibodyDetails;
    }

    public void setYawsEndemicSyphilisDirectFluorescentAntibodyDetails(String yawsEndemicSyphilisDirectFluorescentAntibodyDetails) {
        this.yawsEndemicSyphilisDirectFluorescentAntibodyDetails = yawsEndemicSyphilisDirectFluorescentAntibodyDetails;
    }

    public void setYawsEndemicSyphilisMicroscopyDetails(String yawsEndemicSyphilisMicroscopyDetails) {
        this.yawsEndemicSyphilisMicroscopyDetails = yawsEndemicSyphilisMicroscopyDetails;
    }

    public void setYawsEndemicSyphilisNeutralizingAntibodiesDetails(String yawsEndemicSyphilisNeutralizingAntibodiesDetails) {
        this.yawsEndemicSyphilisNeutralizingAntibodiesDetails = yawsEndemicSyphilisNeutralizingAntibodiesDetails;
    }

    public void setYawsEndemicSyphilisPcrRtPcrDetails(String yawsEndemicSyphilisPcrRtPcrDetails) {
        this.yawsEndemicSyphilisPcrRtPcrDetails = yawsEndemicSyphilisPcrRtPcrDetails;
    }

    public void setYawsEndemicSyphilisGramStainDetails(String yawsEndemicSyphilisGramStainDetails) {
        this.yawsEndemicSyphilisGramStainDetails = yawsEndemicSyphilisGramStainDetails;
    }

    public void setYawsEndemicSyphilisLatexAgglutinationDetails(String yawsEndemicSyphilisLatexAgglutinationDetails) {
        this.yawsEndemicSyphilisLatexAgglutinationDetails = yawsEndemicSyphilisLatexAgglutinationDetails;
    }

    public void setYawsEndemicSyphilisCqValueDetectionDetails(String yawsEndemicSyphilisCqValueDetectionDetails) {
        this.yawsEndemicSyphilisCqValueDetectionDetails = yawsEndemicSyphilisCqValueDetectionDetails;
    }

    public void setYawsEndemicSyphilisSequencingDetails(String yawsEndemicSyphilisSequencingDetails) {
        this.yawsEndemicSyphilisSequencingDetails = yawsEndemicSyphilisSequencingDetails;
    }

    public void setYawsEndemicSyphilisDnaMicroarrayDetails(String yawsEndemicSyphilisDnaMicroarrayDetails) {
        this.yawsEndemicSyphilisDnaMicroarrayDetails = yawsEndemicSyphilisDnaMicroarrayDetails;
    }

    public void setYawsEndemicSyphilisOtherDetails(String yawsEndemicSyphilisOtherDetails) {
        this.yawsEndemicSyphilisOtherDetails = yawsEndemicSyphilisOtherDetails;
    }

    public void setMaternalDeathsAntibodyDetection(String maternalDeathsAntibodyDetection) {
        this.maternalDeathsAntibodyDetection = maternalDeathsAntibodyDetection;
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

    public void setMaternalDeathsDirectFluorescentAntibody(String maternalDeathsDirectFluorescentAntibody) {
        this.maternalDeathsDirectFluorescentAntibody = maternalDeathsDirectFluorescentAntibody;
    }

    public void setMaternalDeathsMicroscopy(String maternalDeathsMicroscopy) {
        this.maternalDeathsMicroscopy = maternalDeathsMicroscopy;
    }

    public void setMaternalDeathsNeutralizingAntibodies(String maternalDeathsNeutralizingAntibodies) {
        this.maternalDeathsNeutralizingAntibodies = maternalDeathsNeutralizingAntibodies;
    }

    public void setMaternalDeathsPcrRtPcr(String maternalDeathsPcrRtPcr) {
        this.maternalDeathsPcrRtPcr = maternalDeathsPcrRtPcr;
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

    public void setMaternalDeathsSequencing(String maternalDeathsSequencing) {
        this.maternalDeathsSequencing = maternalDeathsSequencing;
    }

    public void setMaternalDeathsDnaMicroarray(String maternalDeathsDnaMicroarray) {
        this.maternalDeathsDnaMicroarray = maternalDeathsDnaMicroarray;
    }

    public void setMaternalDeathsOther(String maternalDeathsOther) {
        this.maternalDeathsOther = maternalDeathsOther;
    }

    public void setMaternalDeathsAntibodyDetectionDetails(String maternalDeathsAntibodyDetectionDetails) {
        this.maternalDeathsAntibodyDetectionDetails = maternalDeathsAntibodyDetectionDetails;
    }

    public void setMaternalDeathsAntigenDetectionDetails(String maternalDeathsAntigenDetectionDetails) {
        this.maternalDeathsAntigenDetectionDetails = maternalDeathsAntigenDetectionDetails;
    }

    public void setMaternalDeathsRapidTestDetails(String maternalDeathsRapidTestDetails) {
        this.maternalDeathsRapidTestDetails = maternalDeathsRapidTestDetails;
    }

    public void setMaternalDeathsCultureDetails(String maternalDeathsCultureDetails) {
        this.maternalDeathsCultureDetails = maternalDeathsCultureDetails;
    }

    public void setMaternalDeathsHistopathologyDetails(String maternalDeathsHistopathologyDetails) {
        this.maternalDeathsHistopathologyDetails = maternalDeathsHistopathologyDetails;
    }

    public void setMaternalDeathsIsolationDetails(String maternalDeathsIsolationDetails) {
        this.maternalDeathsIsolationDetails = maternalDeathsIsolationDetails;
    }

    public void setMaternalDeathsIgmSerumAntibodyDetails(String maternalDeathsIgmSerumAntibodyDetails) {
        this.maternalDeathsIgmSerumAntibodyDetails = maternalDeathsIgmSerumAntibodyDetails;
    }

    public void setMaternalDeathsIggSerumAntibodyDetails(String maternalDeathsIggSerumAntibodyDetails) {
        this.maternalDeathsIggSerumAntibodyDetails = maternalDeathsIggSerumAntibodyDetails;
    }

    public void setMaternalDeathsIgaSerumAntibodyDetails(String maternalDeathsIgaSerumAntibodyDetails) {
        this.maternalDeathsIgaSerumAntibodyDetails = maternalDeathsIgaSerumAntibodyDetails;
    }

    public void setMaternalDeathsIncubationTimeDetails(String maternalDeathsIncubationTimeDetails) {
        this.maternalDeathsIncubationTimeDetails = maternalDeathsIncubationTimeDetails;
    }

    public void setMaternalDeathsIndirectFluorescentAntibodyDetails(String maternalDeathsIndirectFluorescentAntibodyDetails) {
        this.maternalDeathsIndirectFluorescentAntibodyDetails = maternalDeathsIndirectFluorescentAntibodyDetails;
    }

    public void setMaternalDeathsDirectFluorescentAntibodyDetails(String maternalDeathsDirectFluorescentAntibodyDetails) {
        this.maternalDeathsDirectFluorescentAntibodyDetails = maternalDeathsDirectFluorescentAntibodyDetails;
    }

    public void setMaternalDeathsMicroscopyDetails(String maternalDeathsMicroscopyDetails) {
        this.maternalDeathsMicroscopyDetails = maternalDeathsMicroscopyDetails;
    }

    public void setMaternalDeathsNeutralizingAntibodiesDetails(String maternalDeathsNeutralizingAntibodiesDetails) {
        this.maternalDeathsNeutralizingAntibodiesDetails = maternalDeathsNeutralizingAntibodiesDetails;
    }

    public void setMaternalDeathsPcrRtPcrDetails(String maternalDeathsPcrRtPcrDetails) {
        this.maternalDeathsPcrRtPcrDetails = maternalDeathsPcrRtPcrDetails;
    }

    public void setMaternalDeathsGramStainDetails(String maternalDeathsGramStainDetails) {
        this.maternalDeathsGramStainDetails = maternalDeathsGramStainDetails;
    }

    public void setMaternalDeathsLatexAgglutinationDetails(String maternalDeathsLatexAgglutinationDetails) {
        this.maternalDeathsLatexAgglutinationDetails = maternalDeathsLatexAgglutinationDetails;
    }

    public void setMaternalDeathsCqValueDetectionDetails(String maternalDeathsCqValueDetectionDetails) {
        this.maternalDeathsCqValueDetectionDetails = maternalDeathsCqValueDetectionDetails;
    }

    public void setMaternalDeathsSequencingDetails(String maternalDeathsSequencingDetails) {
        this.maternalDeathsSequencingDetails = maternalDeathsSequencingDetails;
    }

    public void setMaternalDeathsDnaMicroarrayDetails(String maternalDeathsDnaMicroarrayDetails) {
        this.maternalDeathsDnaMicroarrayDetails = maternalDeathsDnaMicroarrayDetails;
    }

    public void setMaternalDeathsOtherDetails(String maternalDeathsOtherDetails) {
        this.maternalDeathsOtherDetails = maternalDeathsOtherDetails;
    }

    public void setPerinatalDeathsAntibodyDetection(String perinatalDeathsAntibodyDetection) {
        this.perinatalDeathsAntibodyDetection = perinatalDeathsAntibodyDetection;
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

    public void setPerinatalDeathsDirectFluorescentAntibody(String perinatalDeathsDirectFluorescentAntibody) {
        this.perinatalDeathsDirectFluorescentAntibody = perinatalDeathsDirectFluorescentAntibody;
    }

    public void setPerinatalDeathsMicroscopy(String perinatalDeathsMicroscopy) {
        this.perinatalDeathsMicroscopy = perinatalDeathsMicroscopy;
    }

    public void setPerinatalDeathsNeutralizingAntibodies(String perinatalDeathsNeutralizingAntibodies) {
        this.perinatalDeathsNeutralizingAntibodies = perinatalDeathsNeutralizingAntibodies;
    }

    public void setPerinatalDeathsPcrRtPcr(String perinatalDeathsPcrRtPcr) {
        this.perinatalDeathsPcrRtPcr = perinatalDeathsPcrRtPcr;
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

    public void setPerinatalDeathsSequencing(String perinatalDeathsSequencing) {
        this.perinatalDeathsSequencing = perinatalDeathsSequencing;
    }

    public void setPerinatalDeathsDnaMicroarray(String perinatalDeathsDnaMicroarray) {
        this.perinatalDeathsDnaMicroarray = perinatalDeathsDnaMicroarray;
    }

    public void setPerinatalDeathsOther(String perinatalDeathsOther) {
        this.perinatalDeathsOther = perinatalDeathsOther;
    }

    public void setPerinatalDeathsAntibodyDetectionDetails(String perinatalDeathsAntibodyDetectionDetails) {
        this.perinatalDeathsAntibodyDetectionDetails = perinatalDeathsAntibodyDetectionDetails;
    }

    public void setPerinatalDeathsAntigenDetectionDetails(String perinatalDeathsAntigenDetectionDetails) {
        this.perinatalDeathsAntigenDetectionDetails = perinatalDeathsAntigenDetectionDetails;
    }

    public void setPerinatalDeathsRapidTestDetails(String perinatalDeathsRapidTestDetails) {
        this.perinatalDeathsRapidTestDetails = perinatalDeathsRapidTestDetails;
    }

    public void setPerinatalDeathsCultureDetails(String perinatalDeathsCultureDetails) {
        this.perinatalDeathsCultureDetails = perinatalDeathsCultureDetails;
    }

    public void setPerinatalDeathsHistopathologyDetails(String perinatalDeathsHistopathologyDetails) {
        this.perinatalDeathsHistopathologyDetails = perinatalDeathsHistopathologyDetails;
    }

    public void setPerinatalDeathsIsolationDetails(String perinatalDeathsIsolationDetails) {
        this.perinatalDeathsIsolationDetails = perinatalDeathsIsolationDetails;
    }

    public void setPerinatalDeathsIgmSerumAntibodyDetails(String perinatalDeathsIgmSerumAntibodyDetails) {
        this.perinatalDeathsIgmSerumAntibodyDetails = perinatalDeathsIgmSerumAntibodyDetails;
    }

    public void setPerinatalDeathsIggSerumAntibodyDetails(String perinatalDeathsIggSerumAntibodyDetails) {
        this.perinatalDeathsIggSerumAntibodyDetails = perinatalDeathsIggSerumAntibodyDetails;
    }

    public void setPerinatalDeathsIgaSerumAntibodyDetails(String perinatalDeathsIgaSerumAntibodyDetails) {
        this.perinatalDeathsIgaSerumAntibodyDetails = perinatalDeathsIgaSerumAntibodyDetails;
    }

    public void setPerinatalDeathsIncubationTimeDetails(String perinatalDeathsIncubationTimeDetails) {
        this.perinatalDeathsIncubationTimeDetails = perinatalDeathsIncubationTimeDetails;
    }

    public void setPerinatalDeathsIndirectFluorescentAntibodyDetails(String perinatalDeathsIndirectFluorescentAntibodyDetails) {
        this.perinatalDeathsIndirectFluorescentAntibodyDetails = perinatalDeathsIndirectFluorescentAntibodyDetails;
    }

    public void setPerinatalDeathsDirectFluorescentAntibodyDetails(String perinatalDeathsDirectFluorescentAntibodyDetails) {
        this.perinatalDeathsDirectFluorescentAntibodyDetails = perinatalDeathsDirectFluorescentAntibodyDetails;
    }

    public void setPerinatalDeathsMicroscopyDetails(String perinatalDeathsMicroscopyDetails) {
        this.perinatalDeathsMicroscopyDetails = perinatalDeathsMicroscopyDetails;
    }

    public void setPerinatalDeathsNeutralizingAntibodiesDetails(String perinatalDeathsNeutralizingAntibodiesDetails) {
        this.perinatalDeathsNeutralizingAntibodiesDetails = perinatalDeathsNeutralizingAntibodiesDetails;
    }

    public void setPerinatalDeathsPcrRtPcrDetails(String perinatalDeathsPcrRtPcrDetails) {
        this.perinatalDeathsPcrRtPcrDetails = perinatalDeathsPcrRtPcrDetails;
    }

    public void setPerinatalDeathsGramStainDetails(String perinatalDeathsGramStainDetails) {
        this.perinatalDeathsGramStainDetails = perinatalDeathsGramStainDetails;
    }

    public void setPerinatalDeathsLatexAgglutinationDetails(String perinatalDeathsLatexAgglutinationDetails) {
        this.perinatalDeathsLatexAgglutinationDetails = perinatalDeathsLatexAgglutinationDetails;
    }

    public void setPerinatalDeathsCqValueDetectionDetails(String perinatalDeathsCqValueDetectionDetails) {
        this.perinatalDeathsCqValueDetectionDetails = perinatalDeathsCqValueDetectionDetails;
    }

    public void setPerinatalDeathsSequencingDetails(String perinatalDeathsSequencingDetails) {
        this.perinatalDeathsSequencingDetails = perinatalDeathsSequencingDetails;
    }

    public void setPerinatalDeathsDnaMicroarrayDetails(String perinatalDeathsDnaMicroarrayDetails) {
        this.perinatalDeathsDnaMicroarrayDetails = perinatalDeathsDnaMicroarrayDetails;
    }

    public void setPerinatalDeathsOtherDetails(String perinatalDeathsOtherDetails) {
        this.perinatalDeathsOtherDetails = perinatalDeathsOtherDetails;
    }

    public void setInfluenzaAAntibodyDetection(String influenzaAAntibodyDetection) {
        this.influenzaAAntibodyDetection = influenzaAAntibodyDetection;
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

    public void setInfluenzaADirectFluorescentAntibody(String influenzaADirectFluorescentAntibody) {
        this.influenzaADirectFluorescentAntibody = influenzaADirectFluorescentAntibody;
    }

    public void setInfluenzaAMicroscopy(String influenzaAMicroscopy) {
        this.influenzaAMicroscopy = influenzaAMicroscopy;
    }

    public void setInfluenzaANeutralizingAntibodies(String influenzaANeutralizingAntibodies) {
        this.influenzaANeutralizingAntibodies = influenzaANeutralizingAntibodies;
    }

    public void setInfluenzaAPcrRtPcr(String influenzaAPcrRtPcr) {
        this.influenzaAPcrRtPcr = influenzaAPcrRtPcr;
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

    public void setInfluenzaASequencing(String influenzaASequencing) {
        this.influenzaASequencing = influenzaASequencing;
    }

    public void setInfluenzaADnaMicroarray(String influenzaADnaMicroarray) {
        this.influenzaADnaMicroarray = influenzaADnaMicroarray;
    }

    public void setInfluenzaAOther(String influenzaAOther) {
        this.influenzaAOther = influenzaAOther;
    }

    public void setInfluenzaAAntibodyDetectionDetails(String influenzaAAntibodyDetectionDetails) {
        this.influenzaAAntibodyDetectionDetails = influenzaAAntibodyDetectionDetails;
    }

    public void setInfluenzaAAntigenDetectionDetails(String influenzaAAntigenDetectionDetails) {
        this.influenzaAAntigenDetectionDetails = influenzaAAntigenDetectionDetails;
    }

    public void setInfluenzaARapidTestDetails(String influenzaARapidTestDetails) {
        this.influenzaARapidTestDetails = influenzaARapidTestDetails;
    }

    public void setInfluenzaACultureDetails(String influenzaACultureDetails) {
        this.influenzaACultureDetails = influenzaACultureDetails;
    }

    public void setInfluenzaAHistopathologyDetails(String influenzaAHistopathologyDetails) {
        this.influenzaAHistopathologyDetails = influenzaAHistopathologyDetails;
    }

    public void setInfluenzaAIsolationDetails(String influenzaAIsolationDetails) {
        this.influenzaAIsolationDetails = influenzaAIsolationDetails;
    }

    public void setInfluenzaAIgmSerumAntibodyDetails(String influenzaAIgmSerumAntibodyDetails) {
        this.influenzaAIgmSerumAntibodyDetails = influenzaAIgmSerumAntibodyDetails;
    }

    public void setInfluenzaAIggSerumAntibodyDetails(String influenzaAIggSerumAntibodyDetails) {
        this.influenzaAIggSerumAntibodyDetails = influenzaAIggSerumAntibodyDetails;
    }

    public void setInfluenzaAIgaSerumAntibodyDetails(String influenzaAIgaSerumAntibodyDetails) {
        this.influenzaAIgaSerumAntibodyDetails = influenzaAIgaSerumAntibodyDetails;
    }

    public void setInfluenzaAIncubationTimeDetails(String influenzaAIncubationTimeDetails) {
        this.influenzaAIncubationTimeDetails = influenzaAIncubationTimeDetails;
    }

    public void setInfluenzaAIndirectFluorescentAntibodyDetails(String influenzaAIndirectFluorescentAntibodyDetails) {
        this.influenzaAIndirectFluorescentAntibodyDetails = influenzaAIndirectFluorescentAntibodyDetails;
    }

    public void setInfluenzaADirectFluorescentAntibodyDetails(String influenzaADirectFluorescentAntibodyDetails) {
        this.influenzaADirectFluorescentAntibodyDetails = influenzaADirectFluorescentAntibodyDetails;
    }

    public void setInfluenzaAMicroscopyDetails(String influenzaAMicroscopyDetails) {
        this.influenzaAMicroscopyDetails = influenzaAMicroscopyDetails;
    }

    public void setInfluenzaANeutralizingAntibodiesDetails(String influenzaANeutralizingAntibodiesDetails) {
        this.influenzaANeutralizingAntibodiesDetails = influenzaANeutralizingAntibodiesDetails;
    }

    public void setInfluenzaAPcrRtPcrDetails(String influenzaAPcrRtPcrDetails) {
        this.influenzaAPcrRtPcrDetails = influenzaAPcrRtPcrDetails;
    }

    public void setInfluenzaAGramStainDetails(String influenzaAGramStainDetails) {
        this.influenzaAGramStainDetails = influenzaAGramStainDetails;
    }

    public void setInfluenzaALatexAgglutinationDetails(String influenzaALatexAgglutinationDetails) {
        this.influenzaALatexAgglutinationDetails = influenzaALatexAgglutinationDetails;
    }

    public void setInfluenzaACqValueDetectionDetails(String influenzaACqValueDetectionDetails) {
        this.influenzaACqValueDetectionDetails = influenzaACqValueDetectionDetails;
    }

    public void setInfluenzaASequencingDetails(String influenzaASequencingDetails) {
        this.influenzaASequencingDetails = influenzaASequencingDetails;
    }

    public void setInfluenzaADnaMicroarrayDetails(String influenzaADnaMicroarrayDetails) {
        this.influenzaADnaMicroarrayDetails = influenzaADnaMicroarrayDetails;
    }

    public void setInfluenzaAOtherDetails(String influenzaAOtherDetails) {
        this.influenzaAOtherDetails = influenzaAOtherDetails;
    }

    public void setInfluenzaBAntibodyDetection(String influenzaBAntibodyDetection) {
        this.influenzaBAntibodyDetection = influenzaBAntibodyDetection;
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

    public void setInfluenzaBDirectFluorescentAntibody(String influenzaBDirectFluorescentAntibody) {
        this.influenzaBDirectFluorescentAntibody = influenzaBDirectFluorescentAntibody;
    }

    public void setInfluenzaBMicroscopy(String influenzaBMicroscopy) {
        this.influenzaBMicroscopy = influenzaBMicroscopy;
    }

    public void setInfluenzaBNeutralizingAntibodies(String influenzaBNeutralizingAntibodies) {
        this.influenzaBNeutralizingAntibodies = influenzaBNeutralizingAntibodies;
    }

    public void setInfluenzaBPcrRtPcr(String influenzaBPcrRtPcr) {
        this.influenzaBPcrRtPcr = influenzaBPcrRtPcr;
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

    public void setInfluenzaBSequencing(String influenzaBSequencing) {
        this.influenzaBSequencing = influenzaBSequencing;
    }

    public void setInfluenzaBDnaMicroarray(String influenzaBDnaMicroarray) {
        this.influenzaBDnaMicroarray = influenzaBDnaMicroarray;
    }

    public void setInfluenzaBOther(String influenzaBOther) {
        this.influenzaBOther = influenzaBOther;
    }

    public void setInfluenzaBAntibodyDetectionDetails(String influenzaBAntibodyDetectionDetails) {
        this.influenzaBAntibodyDetectionDetails = influenzaBAntibodyDetectionDetails;
    }

    public void setInfluenzaBAntigenDetectionDetails(String influenzaBAntigenDetectionDetails) {
        this.influenzaBAntigenDetectionDetails = influenzaBAntigenDetectionDetails;
    }

    public void setInfluenzaBRapidTestDetails(String influenzaBRapidTestDetails) {
        this.influenzaBRapidTestDetails = influenzaBRapidTestDetails;
    }

    public void setInfluenzaBCultureDetails(String influenzaBCultureDetails) {
        this.influenzaBCultureDetails = influenzaBCultureDetails;
    }

    public void setInfluenzaBHistopathologyDetails(String influenzaBHistopathologyDetails) {
        this.influenzaBHistopathologyDetails = influenzaBHistopathologyDetails;
    }

    public void setInfluenzaBIsolationDetails(String influenzaBIsolationDetails) {
        this.influenzaBIsolationDetails = influenzaBIsolationDetails;
    }

    public void setInfluenzaBIgmSerumAntibodyDetails(String influenzaBIgmSerumAntibodyDetails) {
        this.influenzaBIgmSerumAntibodyDetails = influenzaBIgmSerumAntibodyDetails;
    }

    public void setInfluenzaBIggSerumAntibodyDetails(String influenzaBIggSerumAntibodyDetails) {
        this.influenzaBIggSerumAntibodyDetails = influenzaBIggSerumAntibodyDetails;
    }

    public void setInfluenzaBIgaSerumAntibodyDetails(String influenzaBIgaSerumAntibodyDetails) {
        this.influenzaBIgaSerumAntibodyDetails = influenzaBIgaSerumAntibodyDetails;
    }

    public void setInfluenzaBIncubationTimeDetails(String influenzaBIncubationTimeDetails) {
        this.influenzaBIncubationTimeDetails = influenzaBIncubationTimeDetails;
    }

    public void setInfluenzaBIndirectFluorescentAntibodyDetails(String influenzaBIndirectFluorescentAntibodyDetails) {
        this.influenzaBIndirectFluorescentAntibodyDetails = influenzaBIndirectFluorescentAntibodyDetails;
    }

    public void setInfluenzaBDirectFluorescentAntibodyDetails(String influenzaBDirectFluorescentAntibodyDetails) {
        this.influenzaBDirectFluorescentAntibodyDetails = influenzaBDirectFluorescentAntibodyDetails;
    }

    public void setInfluenzaBMicroscopyDetails(String influenzaBMicroscopyDetails) {
        this.influenzaBMicroscopyDetails = influenzaBMicroscopyDetails;
    }

    public void setInfluenzaBNeutralizingAntibodiesDetails(String influenzaBNeutralizingAntibodiesDetails) {
        this.influenzaBNeutralizingAntibodiesDetails = influenzaBNeutralizingAntibodiesDetails;
    }

    public void setInfluenzaBPcrRtPcrDetails(String influenzaBPcrRtPcrDetails) {
        this.influenzaBPcrRtPcrDetails = influenzaBPcrRtPcrDetails;
    }

    public void setInfluenzaBGramStainDetails(String influenzaBGramStainDetails) {
        this.influenzaBGramStainDetails = influenzaBGramStainDetails;
    }

    public void setInfluenzaBLatexAgglutinationDetails(String influenzaBLatexAgglutinationDetails) {
        this.influenzaBLatexAgglutinationDetails = influenzaBLatexAgglutinationDetails;
    }

    public void setInfluenzaBCqValueDetectionDetails(String influenzaBCqValueDetectionDetails) {
        this.influenzaBCqValueDetectionDetails = influenzaBCqValueDetectionDetails;
    }

    public void setInfluenzaBSequencingDetails(String influenzaBSequencingDetails) {
        this.influenzaBSequencingDetails = influenzaBSequencingDetails;
    }

    public void setInfluenzaBDnaMicroarrayDetails(String influenzaBDnaMicroarrayDetails) {
        this.influenzaBDnaMicroarrayDetails = influenzaBDnaMicroarrayDetails;
    }

    public void setInfluenzaBOtherDetails(String influenzaBOtherDetails) {
        this.influenzaBOtherDetails = influenzaBOtherDetails;
    }

    public void sethMetapneumovirusAntibodyDetection(String hMetapneumovirusAntibodyDetection) {
        this.hMetapneumovirusAntibodyDetection = hMetapneumovirusAntibodyDetection;
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

    public void sethMetapneumovirusDirectFluorescentAntibody(String hMetapneumovirusDirectFluorescentAntibody) {
        this.hMetapneumovirusDirectFluorescentAntibody = hMetapneumovirusDirectFluorescentAntibody;
    }

    public void sethMetapneumovirusMicroscopy(String hMetapneumovirusMicroscopy) {
        this.hMetapneumovirusMicroscopy = hMetapneumovirusMicroscopy;
    }

    public void sethMetapneumovirusNeutralizingAntibodies(String hMetapneumovirusNeutralizingAntibodies) {
        this.hMetapneumovirusNeutralizingAntibodies = hMetapneumovirusNeutralizingAntibodies;
    }

    public void sethMetapneumovirusPcrRtPcr(String hMetapneumovirusPcrRtPcr) {
        this.hMetapneumovirusPcrRtPcr = hMetapneumovirusPcrRtPcr;
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

    public void sethMetapneumovirusSequencing(String hMetapneumovirusSequencing) {
        this.hMetapneumovirusSequencing = hMetapneumovirusSequencing;
    }

    public void sethMetapneumovirusDnaMicroarray(String hMetapneumovirusDnaMicroarray) {
        this.hMetapneumovirusDnaMicroarray = hMetapneumovirusDnaMicroarray;
    }

    public void sethMetapneumovirusOther(String hMetapneumovirusOther) {
        this.hMetapneumovirusOther = hMetapneumovirusOther;
    }

    public void sethMetapneumovirusAntibodyDetectionDetails(String hMetapneumovirusAntibodyDetectionDetails) {
        this.hMetapneumovirusAntibodyDetectionDetails = hMetapneumovirusAntibodyDetectionDetails;
    }

    public void sethMetapneumovirusAntigenDetectionDetails(String hMetapneumovirusAntigenDetectionDetails) {
        this.hMetapneumovirusAntigenDetectionDetails = hMetapneumovirusAntigenDetectionDetails;
    }

    public void sethMetapneumovirusRapidTestDetails(String hMetapneumovirusRapidTestDetails) {
        this.hMetapneumovirusRapidTestDetails = hMetapneumovirusRapidTestDetails;
    }

    public void sethMetapneumovirusCultureDetails(String hMetapneumovirusCultureDetails) {
        this.hMetapneumovirusCultureDetails = hMetapneumovirusCultureDetails;
    }

    public void sethMetapneumovirusHistopathologyDetails(String hMetapneumovirusHistopathologyDetails) {
        this.hMetapneumovirusHistopathologyDetails = hMetapneumovirusHistopathologyDetails;
    }

    public void sethMetapneumovirusIsolationDetails(String hMetapneumovirusIsolationDetails) {
        this.hMetapneumovirusIsolationDetails = hMetapneumovirusIsolationDetails;
    }

    public void sethMetapneumovirusIgmSerumAntibodyDetails(String hMetapneumovirusIgmSerumAntibodyDetails) {
        this.hMetapneumovirusIgmSerumAntibodyDetails = hMetapneumovirusIgmSerumAntibodyDetails;
    }

    public void sethMetapneumovirusIggSerumAntibodyDetails(String hMetapneumovirusIggSerumAntibodyDetails) {
        this.hMetapneumovirusIggSerumAntibodyDetails = hMetapneumovirusIggSerumAntibodyDetails;
    }

    public void sethMetapneumovirusIgaSerumAntibodyDetails(String hMetapneumovirusIgaSerumAntibodyDetails) {
        this.hMetapneumovirusIgaSerumAntibodyDetails = hMetapneumovirusIgaSerumAntibodyDetails;
    }

    public void sethMetapneumovirusIncubationTimeDetails(String hMetapneumovirusIncubationTimeDetails) {
        this.hMetapneumovirusIncubationTimeDetails = hMetapneumovirusIncubationTimeDetails;
    }

    public void sethMetapneumovirusIndirectFluorescentAntibodyDetails(String hMetapneumovirusIndirectFluorescentAntibodyDetails) {
        this.hMetapneumovirusIndirectFluorescentAntibodyDetails = hMetapneumovirusIndirectFluorescentAntibodyDetails;
    }

    public void sethMetapneumovirusDirectFluorescentAntibodyDetails(String hMetapneumovirusDirectFluorescentAntibodyDetails) {
        this.hMetapneumovirusDirectFluorescentAntibodyDetails = hMetapneumovirusDirectFluorescentAntibodyDetails;
    }

    public void sethMetapneumovirusMicroscopyDetails(String hMetapneumovirusMicroscopyDetails) {
        this.hMetapneumovirusMicroscopyDetails = hMetapneumovirusMicroscopyDetails;
    }

    public void sethMetapneumovirusNeutralizingAntibodiesDetails(String hMetapneumovirusNeutralizingAntibodiesDetails) {
        this.hMetapneumovirusNeutralizingAntibodiesDetails = hMetapneumovirusNeutralizingAntibodiesDetails;
    }

    public void sethMetapneumovirusPcrRtPcrDetails(String hMetapneumovirusPcrRtPcrDetails) {
        this.hMetapneumovirusPcrRtPcrDetails = hMetapneumovirusPcrRtPcrDetails;
    }

    public void sethMetapneumovirusGramStainDetails(String hMetapneumovirusGramStainDetails) {
        this.hMetapneumovirusGramStainDetails = hMetapneumovirusGramStainDetails;
    }

    public void sethMetapneumovirusLatexAgglutinationDetails(String hMetapneumovirusLatexAgglutinationDetails) {
        this.hMetapneumovirusLatexAgglutinationDetails = hMetapneumovirusLatexAgglutinationDetails;
    }

    public void sethMetapneumovirusCqValueDetectionDetails(String hMetapneumovirusCqValueDetectionDetails) {
        this.hMetapneumovirusCqValueDetectionDetails = hMetapneumovirusCqValueDetectionDetails;
    }

    public void sethMetapneumovirusSequencingDetails(String hMetapneumovirusSequencingDetails) {
        this.hMetapneumovirusSequencingDetails = hMetapneumovirusSequencingDetails;
    }

    public void sethMetapneumovirusDnaMicroarrayDetails(String hMetapneumovirusDnaMicroarrayDetails) {
        this.hMetapneumovirusDnaMicroarrayDetails = hMetapneumovirusDnaMicroarrayDetails;
    }

    public void sethMetapneumovirusOtherDetails(String hMetapneumovirusOtherDetails) {
        this.hMetapneumovirusOtherDetails = hMetapneumovirusOtherDetails;
    }

    public void setRespiratorySyncytialVirusAntibodyDetection(String respiratorySyncytialVirusAntibodyDetection) {
        this.respiratorySyncytialVirusAntibodyDetection = respiratorySyncytialVirusAntibodyDetection;
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

    public void setRespiratorySyncytialVirusDirectFluorescentAntibody(String respiratorySyncytialVirusDirectFluorescentAntibody) {
        this.respiratorySyncytialVirusDirectFluorescentAntibody = respiratorySyncytialVirusDirectFluorescentAntibody;
    }

    public void setRespiratorySyncytialVirusMicroscopy(String respiratorySyncytialVirusMicroscopy) {
        this.respiratorySyncytialVirusMicroscopy = respiratorySyncytialVirusMicroscopy;
    }

    public void setRespiratorySyncytialVirusNeutralizingAntibodies(String respiratorySyncytialVirusNeutralizingAntibodies) {
        this.respiratorySyncytialVirusNeutralizingAntibodies = respiratorySyncytialVirusNeutralizingAntibodies;
    }

    public void setRespiratorySyncytialVirusPcrRtPcr(String respiratorySyncytialVirusPcrRtPcr) {
        this.respiratorySyncytialVirusPcrRtPcr = respiratorySyncytialVirusPcrRtPcr;
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

    public void setRespiratorySyncytialVirusSequencing(String respiratorySyncytialVirusSequencing) {
        this.respiratorySyncytialVirusSequencing = respiratorySyncytialVirusSequencing;
    }

    public void setRespiratorySyncytialVirusDnaMicroarray(String respiratorySyncytialVirusDnaMicroarray) {
        this.respiratorySyncytialVirusDnaMicroarray = respiratorySyncytialVirusDnaMicroarray;
    }

    public void setRespiratorySyncytialVirusOther(String respiratorySyncytialVirusOther) {
        this.respiratorySyncytialVirusOther = respiratorySyncytialVirusOther;
    }

    public void setRespiratorySyncytialVirusAntibodyDetectionDetails(String respiratorySyncytialVirusAntibodyDetectionDetails) {
        this.respiratorySyncytialVirusAntibodyDetectionDetails = respiratorySyncytialVirusAntibodyDetectionDetails;
    }

    public void setRespiratorySyncytialVirusAntigenDetectionDetails(String respiratorySyncytialVirusAntigenDetectionDetails) {
        this.respiratorySyncytialVirusAntigenDetectionDetails = respiratorySyncytialVirusAntigenDetectionDetails;
    }

    public void setRespiratorySyncytialVirusRapidTestDetails(String respiratorySyncytialVirusRapidTestDetails) {
        this.respiratorySyncytialVirusRapidTestDetails = respiratorySyncytialVirusRapidTestDetails;
    }

    public void setRespiratorySyncytialVirusCultureDetails(String respiratorySyncytialVirusCultureDetails) {
        this.respiratorySyncytialVirusCultureDetails = respiratorySyncytialVirusCultureDetails;
    }

    public void setRespiratorySyncytialVirusHistopathologyDetails(String respiratorySyncytialVirusHistopathologyDetails) {
        this.respiratorySyncytialVirusHistopathologyDetails = respiratorySyncytialVirusHistopathologyDetails;
    }

    public void setRespiratorySyncytialVirusIsolationDetails(String respiratorySyncytialVirusIsolationDetails) {
        this.respiratorySyncytialVirusIsolationDetails = respiratorySyncytialVirusIsolationDetails;
    }

    public void setRespiratorySyncytialVirusIgmSerumAntibodyDetails(String respiratorySyncytialVirusIgmSerumAntibodyDetails) {
        this.respiratorySyncytialVirusIgmSerumAntibodyDetails = respiratorySyncytialVirusIgmSerumAntibodyDetails;
    }

    public void setRespiratorySyncytialVirusIggSerumAntibodyDetails(String respiratorySyncytialVirusIggSerumAntibodyDetails) {
        this.respiratorySyncytialVirusIggSerumAntibodyDetails = respiratorySyncytialVirusIggSerumAntibodyDetails;
    }

    public void setRespiratorySyncytialVirusIgaSerumAntibodyDetails(String respiratorySyncytialVirusIgaSerumAntibodyDetails) {
        this.respiratorySyncytialVirusIgaSerumAntibodyDetails = respiratorySyncytialVirusIgaSerumAntibodyDetails;
    }

    public void setRespiratorySyncytialVirusIncubationTimeDetails(String respiratorySyncytialVirusIncubationTimeDetails) {
        this.respiratorySyncytialVirusIncubationTimeDetails = respiratorySyncytialVirusIncubationTimeDetails;
    }

    public void setRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails(String respiratorySyncytialVirusIndirectFluorescentAntibodyDetails) {
        this.respiratorySyncytialVirusIndirectFluorescentAntibodyDetails = respiratorySyncytialVirusIndirectFluorescentAntibodyDetails;
    }

    public void setRespiratorySyncytialVirusDirectFluorescentAntibodyDetails(String respiratorySyncytialVirusDirectFluorescentAntibodyDetails) {
        this.respiratorySyncytialVirusDirectFluorescentAntibodyDetails = respiratorySyncytialVirusDirectFluorescentAntibodyDetails;
    }

    public void setRespiratorySyncytialVirusMicroscopyDetails(String respiratorySyncytialVirusMicroscopyDetails) {
        this.respiratorySyncytialVirusMicroscopyDetails = respiratorySyncytialVirusMicroscopyDetails;
    }

    public void setRespiratorySyncytialVirusNeutralizingAntibodiesDetails(String respiratorySyncytialVirusNeutralizingAntibodiesDetails) {
        this.respiratorySyncytialVirusNeutralizingAntibodiesDetails = respiratorySyncytialVirusNeutralizingAntibodiesDetails;
    }

    public void setRespiratorySyncytialVirusPcrRtPcrDetails(String respiratorySyncytialVirusPcrRtPcrDetails) {
        this.respiratorySyncytialVirusPcrRtPcrDetails = respiratorySyncytialVirusPcrRtPcrDetails;
    }

    public void setRespiratorySyncytialVirusGramStainDetails(String respiratorySyncytialVirusGramStainDetails) {
        this.respiratorySyncytialVirusGramStainDetails = respiratorySyncytialVirusGramStainDetails;
    }

    public void setRespiratorySyncytialVirusLatexAgglutinationDetails(String respiratorySyncytialVirusLatexAgglutinationDetails) {
        this.respiratorySyncytialVirusLatexAgglutinationDetails = respiratorySyncytialVirusLatexAgglutinationDetails;
    }

    public void setRespiratorySyncytialVirusCqValueDetectionDetails(String respiratorySyncytialVirusCqValueDetectionDetails) {
        this.respiratorySyncytialVirusCqValueDetectionDetails = respiratorySyncytialVirusCqValueDetectionDetails;
    }

    public void setRespiratorySyncytialVirusSequencingDetails(String respiratorySyncytialVirusSequencingDetails) {
        this.respiratorySyncytialVirusSequencingDetails = respiratorySyncytialVirusSequencingDetails;
    }

    public void setRespiratorySyncytialVirusDnaMicroarrayDetails(String respiratorySyncytialVirusDnaMicroarrayDetails) {
        this.respiratorySyncytialVirusDnaMicroarrayDetails = respiratorySyncytialVirusDnaMicroarrayDetails;
    }

    public void setRespiratorySyncytialVirusOtherDetails(String respiratorySyncytialVirusOtherDetails) {
        this.respiratorySyncytialVirusOtherDetails = respiratorySyncytialVirusOtherDetails;
    }

    public void setParainfluenzaAntibodyDetection(String parainfluenzaAntibodyDetection) {
        this.parainfluenzaAntibodyDetection = parainfluenzaAntibodyDetection;
    }

    public void setParainfluenzaAntigenDetection(String parainfluenzaAntigenDetection) {
        this.parainfluenzaAntigenDetection = parainfluenzaAntigenDetection;
    }

    public void setParainfluenzaRapidTest(String parainfluenzaRapidTest) {
        this.parainfluenzaRapidTest = parainfluenzaRapidTest;
    }

    public void setParainfluenzaCulture(String parainfluenzaCulture) {
        this.parainfluenzaCulture = parainfluenzaCulture;
    }

    public void setParainfluenzaHistopathology(String parainfluenzaHistopathology) {
        this.parainfluenzaHistopathology = parainfluenzaHistopathology;
    }

    public void setParainfluenzaIsolation(String parainfluenzaIsolation) {
        this.parainfluenzaIsolation = parainfluenzaIsolation;
    }

    public void setParainfluenzaIgmSerumAntibody(String parainfluenzaIgmSerumAntibody) {
        this.parainfluenzaIgmSerumAntibody = parainfluenzaIgmSerumAntibody;
    }

    public void setParainfluenzaIggSerumAntibody(String parainfluenzaIggSerumAntibody) {
        this.parainfluenzaIggSerumAntibody = parainfluenzaIggSerumAntibody;
    }

    public void setParainfluenzaIgaSerumAntibody(String parainfluenzaIgaSerumAntibody) {
        this.parainfluenzaIgaSerumAntibody = parainfluenzaIgaSerumAntibody;
    }

    public void setParainfluenzaIncubationTime(String parainfluenzaIncubationTime) {
        this.parainfluenzaIncubationTime = parainfluenzaIncubationTime;
    }

    public void setParainfluenzaIndirectFluorescentAntibody(String parainfluenzaIndirectFluorescentAntibody) {
        this.parainfluenzaIndirectFluorescentAntibody = parainfluenzaIndirectFluorescentAntibody;
    }

    public void setParainfluenzaDirectFluorescentAntibody(String parainfluenzaDirectFluorescentAntibody) {
        this.parainfluenzaDirectFluorescentAntibody = parainfluenzaDirectFluorescentAntibody;
    }

    public void setParainfluenzaMicroscopy(String parainfluenzaMicroscopy) {
        this.parainfluenzaMicroscopy = parainfluenzaMicroscopy;
    }

    public void setParainfluenzaNeutralizingAntibodies(String parainfluenzaNeutralizingAntibodies) {
        this.parainfluenzaNeutralizingAntibodies = parainfluenzaNeutralizingAntibodies;
    }

    public void setParainfluenzaPcrRtPcr(String parainfluenzaPcrRtPcr) {
        this.parainfluenzaPcrRtPcr = parainfluenzaPcrRtPcr;
    }

    public void setParainfluenzaGramStain(String parainfluenzaGramStain) {
        this.parainfluenzaGramStain = parainfluenzaGramStain;
    }

    public void setParainfluenzaLatexAgglutination(String parainfluenzaLatexAgglutination) {
        this.parainfluenzaLatexAgglutination = parainfluenzaLatexAgglutination;
    }

    public void setParainfluenzaCqValueDetection(String parainfluenzaCqValueDetection) {
        this.parainfluenzaCqValueDetection = parainfluenzaCqValueDetection;
    }

    public void setParainfluenzaSequencing(String parainfluenzaSequencing) {
        this.parainfluenzaSequencing = parainfluenzaSequencing;
    }

    public void setParainfluenzaDnaMicroarray(String parainfluenzaDnaMicroarray) {
        this.parainfluenzaDnaMicroarray = parainfluenzaDnaMicroarray;
    }

    public void setParainfluenzaOther(String parainfluenzaOther) {
        this.parainfluenzaOther = parainfluenzaOther;
    }

    public void setParainfluenzaAntibodyDetectionDetails(String parainfluenzaAntibodyDetectionDetails) {
        this.parainfluenzaAntibodyDetectionDetails = parainfluenzaAntibodyDetectionDetails;
    }

    public void setParainfluenzaAntigenDetectionDetails(String parainfluenzaAntigenDetectionDetails) {
        this.parainfluenzaAntigenDetectionDetails = parainfluenzaAntigenDetectionDetails;
    }

    public void setParainfluenzaRapidTestDetails(String parainfluenzaRapidTestDetails) {
        this.parainfluenzaRapidTestDetails = parainfluenzaRapidTestDetails;
    }

    public void setParainfluenzaCultureDetails(String parainfluenzaCultureDetails) {
        this.parainfluenzaCultureDetails = parainfluenzaCultureDetails;
    }

    public void setParainfluenzaHistopathologyDetails(String parainfluenzaHistopathologyDetails) {
        this.parainfluenzaHistopathologyDetails = parainfluenzaHistopathologyDetails;
    }

    public void setParainfluenzaIsolationDetails(String parainfluenzaIsolationDetails) {
        this.parainfluenzaIsolationDetails = parainfluenzaIsolationDetails;
    }

    public void setParainfluenzaIgmSerumAntibodyDetails(String parainfluenzaIgmSerumAntibodyDetails) {
        this.parainfluenzaIgmSerumAntibodyDetails = parainfluenzaIgmSerumAntibodyDetails;
    }

    public void setParainfluenzaIggSerumAntibodyDetails(String parainfluenzaIggSerumAntibodyDetails) {
        this.parainfluenzaIggSerumAntibodyDetails = parainfluenzaIggSerumAntibodyDetails;
    }

    public void setParainfluenzaIgaSerumAntibodyDetails(String parainfluenzaIgaSerumAntibodyDetails) {
        this.parainfluenzaIgaSerumAntibodyDetails = parainfluenzaIgaSerumAntibodyDetails;
    }

    public void setParainfluenzaIncubationTimeDetails(String parainfluenzaIncubationTimeDetails) {
        this.parainfluenzaIncubationTimeDetails = parainfluenzaIncubationTimeDetails;
    }

    public void setParainfluenzaIndirectFluorescentAntibodyDetails(String parainfluenzaIndirectFluorescentAntibodyDetails) {
        this.parainfluenzaIndirectFluorescentAntibodyDetails = parainfluenzaIndirectFluorescentAntibodyDetails;
    }

    public void setParainfluenzaDirectFluorescentAntibodyDetails(String parainfluenzaDirectFluorescentAntibodyDetails) {
        this.parainfluenzaDirectFluorescentAntibodyDetails = parainfluenzaDirectFluorescentAntibodyDetails;
    }

    public void setParainfluenzaMicroscopyDetails(String parainfluenzaMicroscopyDetails) {
        this.parainfluenzaMicroscopyDetails = parainfluenzaMicroscopyDetails;
    }

    public void setParainfluenzaNeutralizingAntibodiesDetails(String parainfluenzaNeutralizingAntibodiesDetails) {
        this.parainfluenzaNeutralizingAntibodiesDetails = parainfluenzaNeutralizingAntibodiesDetails;
    }

    public void setParainfluenzaPcrRtPcrDetails(String parainfluenzaPcrRtPcrDetails) {
        this.parainfluenzaPcrRtPcrDetails = parainfluenzaPcrRtPcrDetails;
    }

    public void setParainfluenzaGramStainDetails(String parainfluenzaGramStainDetails) {
        this.parainfluenzaGramStainDetails = parainfluenzaGramStainDetails;
    }

    public void setParainfluenzaLatexAgglutinationDetails(String parainfluenzaLatexAgglutinationDetails) {
        this.parainfluenzaLatexAgglutinationDetails = parainfluenzaLatexAgglutinationDetails;
    }

    public void setParainfluenzaCqValueDetectionDetails(String parainfluenzaCqValueDetectionDetails) {
        this.parainfluenzaCqValueDetectionDetails = parainfluenzaCqValueDetectionDetails;
    }

    public void setParainfluenzaSequencingDetails(String parainfluenzaSequencingDetails) {
        this.parainfluenzaSequencingDetails = parainfluenzaSequencingDetails;
    }

    public void setParainfluenzaDnaMicroarrayDetails(String parainfluenzaDnaMicroarrayDetails) {
        this.parainfluenzaDnaMicroarrayDetails = parainfluenzaDnaMicroarrayDetails;
    }

    public void setParainfluenzaOtherDetails(String parainfluenzaOtherDetails) {
        this.parainfluenzaOtherDetails = parainfluenzaOtherDetails;
    }

    public void setAdenovirusAntibodyDetection(String adenovirusAntibodyDetection) {
        this.adenovirusAntibodyDetection = adenovirusAntibodyDetection;
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

    public void setAdenovirusDirectFluorescentAntibody(String adenovirusDirectFluorescentAntibody) {
        this.adenovirusDirectFluorescentAntibody = adenovirusDirectFluorescentAntibody;
    }

    public void setAdenovirusMicroscopy(String adenovirusMicroscopy) {
        this.adenovirusMicroscopy = adenovirusMicroscopy;
    }

    public void setAdenovirusNeutralizingAntibodies(String adenovirusNeutralizingAntibodies) {
        this.adenovirusNeutralizingAntibodies = adenovirusNeutralizingAntibodies;
    }

    public void setAdenovirusPcrRtPcr(String adenovirusPcrRtPcr) {
        this.adenovirusPcrRtPcr = adenovirusPcrRtPcr;
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

    public void setAdenovirusSequencing(String adenovirusSequencing) {
        this.adenovirusSequencing = adenovirusSequencing;
    }

    public void setAdenovirusDnaMicroarray(String adenovirusDnaMicroarray) {
        this.adenovirusDnaMicroarray = adenovirusDnaMicroarray;
    }

    public void setAdenovirusOther(String adenovirusOther) {
        this.adenovirusOther = adenovirusOther;
    }

    public void setAdenovirusAntibodyDetectionDetails(String adenovirusAntibodyDetectionDetails) {
        this.adenovirusAntibodyDetectionDetails = adenovirusAntibodyDetectionDetails;
    }

    public void setAdenovirusAntigenDetectionDetails(String adenovirusAntigenDetectionDetails) {
        this.adenovirusAntigenDetectionDetails = adenovirusAntigenDetectionDetails;
    }

    public void setAdenovirusRapidTestDetails(String adenovirusRapidTestDetails) {
        this.adenovirusRapidTestDetails = adenovirusRapidTestDetails;
    }

    public void setAdenovirusCultureDetails(String adenovirusCultureDetails) {
        this.adenovirusCultureDetails = adenovirusCultureDetails;
    }

    public void setAdenovirusHistopathologyDetails(String adenovirusHistopathologyDetails) {
        this.adenovirusHistopathologyDetails = adenovirusHistopathologyDetails;
    }

    public void setAdenovirusIsolationDetails(String adenovirusIsolationDetails) {
        this.adenovirusIsolationDetails = adenovirusIsolationDetails;
    }

    public void setAdenovirusIgmSerumAntibodyDetails(String adenovirusIgmSerumAntibodyDetails) {
        this.adenovirusIgmSerumAntibodyDetails = adenovirusIgmSerumAntibodyDetails;
    }

    public void setAdenovirusIggSerumAntibodyDetails(String adenovirusIggSerumAntibodyDetails) {
        this.adenovirusIggSerumAntibodyDetails = adenovirusIggSerumAntibodyDetails;
    }

    public void setAdenovirusIgaSerumAntibodyDetails(String adenovirusIgaSerumAntibodyDetails) {
        this.adenovirusIgaSerumAntibodyDetails = adenovirusIgaSerumAntibodyDetails;
    }

    public void setAdenovirusIncubationTimeDetails(String adenovirusIncubationTimeDetails) {
        this.adenovirusIncubationTimeDetails = adenovirusIncubationTimeDetails;
    }

    public void setAdenovirusIndirectFluorescentAntibodyDetails(String adenovirusIndirectFluorescentAntibodyDetails) {
        this.adenovirusIndirectFluorescentAntibodyDetails = adenovirusIndirectFluorescentAntibodyDetails;
    }

    public void setAdenovirusDirectFluorescentAntibodyDetails(String adenovirusDirectFluorescentAntibodyDetails) {
        this.adenovirusDirectFluorescentAntibodyDetails = adenovirusDirectFluorescentAntibodyDetails;
    }

    public void setAdenovirusMicroscopyDetails(String adenovirusMicroscopyDetails) {
        this.adenovirusMicroscopyDetails = adenovirusMicroscopyDetails;
    }

    public void setAdenovirusNeutralizingAntibodiesDetails(String adenovirusNeutralizingAntibodiesDetails) {
        this.adenovirusNeutralizingAntibodiesDetails = adenovirusNeutralizingAntibodiesDetails;
    }

    public void setAdenovirusPcrRtPcrDetails(String adenovirusPcrRtPcrDetails) {
        this.adenovirusPcrRtPcrDetails = adenovirusPcrRtPcrDetails;
    }

    public void setAdenovirusGramStainDetails(String adenovirusGramStainDetails) {
        this.adenovirusGramStainDetails = adenovirusGramStainDetails;
    }

    public void setAdenovirusLatexAgglutinationDetails(String adenovirusLatexAgglutinationDetails) {
        this.adenovirusLatexAgglutinationDetails = adenovirusLatexAgglutinationDetails;
    }

    public void setAdenovirusCqValueDetectionDetails(String adenovirusCqValueDetectionDetails) {
        this.adenovirusCqValueDetectionDetails = adenovirusCqValueDetectionDetails;
    }

    public void setAdenovirusSequencingDetails(String adenovirusSequencingDetails) {
        this.adenovirusSequencingDetails = adenovirusSequencingDetails;
    }

    public void setAdenovirusDnaMicroarrayDetails(String adenovirusDnaMicroarrayDetails) {
        this.adenovirusDnaMicroarrayDetails = adenovirusDnaMicroarrayDetails;
    }

    public void setAdenovirusOtherDetails(String adenovirusOtherDetails) {
        this.adenovirusOtherDetails = adenovirusOtherDetails;
    }

    public void setRhinovirusAntibodyDetection(String rhinovirusAntibodyDetection) {
        this.rhinovirusAntibodyDetection = rhinovirusAntibodyDetection;
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

    public void setRhinovirusDirectFluorescentAntibody(String rhinovirusDirectFluorescentAntibody) {
        this.rhinovirusDirectFluorescentAntibody = rhinovirusDirectFluorescentAntibody;
    }

    public void setRhinovirusMicroscopy(String rhinovirusMicroscopy) {
        this.rhinovirusMicroscopy = rhinovirusMicroscopy;
    }

    public void setRhinovirusNeutralizingAntibodies(String rhinovirusNeutralizingAntibodies) {
        this.rhinovirusNeutralizingAntibodies = rhinovirusNeutralizingAntibodies;
    }

    public void setRhinovirusPcrRtPcr(String rhinovirusPcrRtPcr) {
        this.rhinovirusPcrRtPcr = rhinovirusPcrRtPcr;
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

    public void setRhinovirusSequencing(String rhinovirusSequencing) {
        this.rhinovirusSequencing = rhinovirusSequencing;
    }

    public void setRhinovirusDnaMicroarray(String rhinovirusDnaMicroarray) {
        this.rhinovirusDnaMicroarray = rhinovirusDnaMicroarray;
    }

    public void setRhinovirusOther(String rhinovirusOther) {
        this.rhinovirusOther = rhinovirusOther;
    }

    public void setRhinovirusAntibodyDetectionDetails(String rhinovirusAntibodyDetectionDetails) {
        this.rhinovirusAntibodyDetectionDetails = rhinovirusAntibodyDetectionDetails;
    }

    public void setRhinovirusAntigenDetectionDetails(String rhinovirusAntigenDetectionDetails) {
        this.rhinovirusAntigenDetectionDetails = rhinovirusAntigenDetectionDetails;
    }

    public void setRhinovirusRapidTestDetails(String rhinovirusRapidTestDetails) {
        this.rhinovirusRapidTestDetails = rhinovirusRapidTestDetails;
    }

    public void setRhinovirusCultureDetails(String rhinovirusCultureDetails) {
        this.rhinovirusCultureDetails = rhinovirusCultureDetails;
    }

    public void setRhinovirusHistopathologyDetails(String rhinovirusHistopathologyDetails) {
        this.rhinovirusHistopathologyDetails = rhinovirusHistopathologyDetails;
    }

    public void setRhinovirusIsolationDetails(String rhinovirusIsolationDetails) {
        this.rhinovirusIsolationDetails = rhinovirusIsolationDetails;
    }

    public void setRhinovirusIgmSerumAntibodyDetails(String rhinovirusIgmSerumAntibodyDetails) {
        this.rhinovirusIgmSerumAntibodyDetails = rhinovirusIgmSerumAntibodyDetails;
    }

    public void setRhinovirusIggSerumAntibodyDetails(String rhinovirusIggSerumAntibodyDetails) {
        this.rhinovirusIggSerumAntibodyDetails = rhinovirusIggSerumAntibodyDetails;
    }

    public void setRhinovirusIgaSerumAntibodyDetails(String rhinovirusIgaSerumAntibodyDetails) {
        this.rhinovirusIgaSerumAntibodyDetails = rhinovirusIgaSerumAntibodyDetails;
    }

    public void setRhinovirusIncubationTimeDetails(String rhinovirusIncubationTimeDetails) {
        this.rhinovirusIncubationTimeDetails = rhinovirusIncubationTimeDetails;
    }

    public void setRhinovirusIndirectFluorescentAntibodyDetails(String rhinovirusIndirectFluorescentAntibodyDetails) {
        this.rhinovirusIndirectFluorescentAntibodyDetails = rhinovirusIndirectFluorescentAntibodyDetails;
    }

    public void setRhinovirusDirectFluorescentAntibodyDetails(String rhinovirusDirectFluorescentAntibodyDetails) {
        this.rhinovirusDirectFluorescentAntibodyDetails = rhinovirusDirectFluorescentAntibodyDetails;
    }

    public void setRhinovirusMicroscopyDetails(String rhinovirusMicroscopyDetails) {
        this.rhinovirusMicroscopyDetails = rhinovirusMicroscopyDetails;
    }

    public void setRhinovirusNeutralizingAntibodiesDetails(String rhinovirusNeutralizingAntibodiesDetails) {
        this.rhinovirusNeutralizingAntibodiesDetails = rhinovirusNeutralizingAntibodiesDetails;
    }

    public void setRhinovirusPcrRtPcrDetails(String rhinovirusPcrRtPcrDetails) {
        this.rhinovirusPcrRtPcrDetails = rhinovirusPcrRtPcrDetails;
    }

    public void setRhinovirusGramStainDetails(String rhinovirusGramStainDetails) {
        this.rhinovirusGramStainDetails = rhinovirusGramStainDetails;
    }

    public void setRhinovirusLatexAgglutinationDetails(String rhinovirusLatexAgglutinationDetails) {
        this.rhinovirusLatexAgglutinationDetails = rhinovirusLatexAgglutinationDetails;
    }

    public void setRhinovirusCqValueDetectionDetails(String rhinovirusCqValueDetectionDetails) {
        this.rhinovirusCqValueDetectionDetails = rhinovirusCqValueDetectionDetails;
    }

    public void setRhinovirusSequencingDetails(String rhinovirusSequencingDetails) {
        this.rhinovirusSequencingDetails = rhinovirusSequencingDetails;
    }

    public void setRhinovirusDnaMicroarrayDetails(String rhinovirusDnaMicroarrayDetails) {
        this.rhinovirusDnaMicroarrayDetails = rhinovirusDnaMicroarrayDetails;
    }

    public void setRhinovirusOtherDetails(String rhinovirusOtherDetails) {
        this.rhinovirusOtherDetails = rhinovirusOtherDetails;
    }

    public void setEnterovirusAntibodyDetection(String enterovirusAntibodyDetection) {
        this.enterovirusAntibodyDetection = enterovirusAntibodyDetection;
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

    public void setEnterovirusDirectFluorescentAntibody(String enterovirusDirectFluorescentAntibody) {
        this.enterovirusDirectFluorescentAntibody = enterovirusDirectFluorescentAntibody;
    }

    public void setEnterovirusMicroscopy(String enterovirusMicroscopy) {
        this.enterovirusMicroscopy = enterovirusMicroscopy;
    }

    public void setEnterovirusNeutralizingAntibodies(String enterovirusNeutralizingAntibodies) {
        this.enterovirusNeutralizingAntibodies = enterovirusNeutralizingAntibodies;
    }

    public void setEnterovirusPcrRtPcr(String enterovirusPcrRtPcr) {
        this.enterovirusPcrRtPcr = enterovirusPcrRtPcr;
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

    public void setEnterovirusSequencing(String enterovirusSequencing) {
        this.enterovirusSequencing = enterovirusSequencing;
    }

    public void setEnterovirusDnaMicroarray(String enterovirusDnaMicroarray) {
        this.enterovirusDnaMicroarray = enterovirusDnaMicroarray;
    }

    public void setEnterovirusOther(String enterovirusOther) {
        this.enterovirusOther = enterovirusOther;
    }

    public void setEnterovirusAntibodyDetectionDetails(String enterovirusAntibodyDetectionDetails) {
        this.enterovirusAntibodyDetectionDetails = enterovirusAntibodyDetectionDetails;
    }

    public void setEnterovirusAntigenDetectionDetails(String enterovirusAntigenDetectionDetails) {
        this.enterovirusAntigenDetectionDetails = enterovirusAntigenDetectionDetails;
    }

    public void setEnterovirusRapidTestDetails(String enterovirusRapidTestDetails) {
        this.enterovirusRapidTestDetails = enterovirusRapidTestDetails;
    }

    public void setEnterovirusCultureDetails(String enterovirusCultureDetails) {
        this.enterovirusCultureDetails = enterovirusCultureDetails;
    }

    public void setEnterovirusHistopathologyDetails(String enterovirusHistopathologyDetails) {
        this.enterovirusHistopathologyDetails = enterovirusHistopathologyDetails;
    }

    public void setEnterovirusIsolationDetails(String enterovirusIsolationDetails) {
        this.enterovirusIsolationDetails = enterovirusIsolationDetails;
    }

    public void setEnterovirusIgmSerumAntibodyDetails(String enterovirusIgmSerumAntibodyDetails) {
        this.enterovirusIgmSerumAntibodyDetails = enterovirusIgmSerumAntibodyDetails;
    }

    public void setEnterovirusIggSerumAntibodyDetails(String enterovirusIggSerumAntibodyDetails) {
        this.enterovirusIggSerumAntibodyDetails = enterovirusIggSerumAntibodyDetails;
    }

    public void setEnterovirusIgaSerumAntibodyDetails(String enterovirusIgaSerumAntibodyDetails) {
        this.enterovirusIgaSerumAntibodyDetails = enterovirusIgaSerumAntibodyDetails;
    }

    public void setEnterovirusIncubationTimeDetails(String enterovirusIncubationTimeDetails) {
        this.enterovirusIncubationTimeDetails = enterovirusIncubationTimeDetails;
    }

    public void setEnterovirusIndirectFluorescentAntibodyDetails(String enterovirusIndirectFluorescentAntibodyDetails) {
        this.enterovirusIndirectFluorescentAntibodyDetails = enterovirusIndirectFluorescentAntibodyDetails;
    }

    public void setEnterovirusDirectFluorescentAntibodyDetails(String enterovirusDirectFluorescentAntibodyDetails) {
        this.enterovirusDirectFluorescentAntibodyDetails = enterovirusDirectFluorescentAntibodyDetails;
    }

    public void setEnterovirusMicroscopyDetails(String enterovirusMicroscopyDetails) {
        this.enterovirusMicroscopyDetails = enterovirusMicroscopyDetails;
    }

    public void setEnterovirusNeutralizingAntibodiesDetails(String enterovirusNeutralizingAntibodiesDetails) {
        this.enterovirusNeutralizingAntibodiesDetails = enterovirusNeutralizingAntibodiesDetails;
    }

    public void setEnterovirusPcrRtPcrDetails(String enterovirusPcrRtPcrDetails) {
        this.enterovirusPcrRtPcrDetails = enterovirusPcrRtPcrDetails;
    }

    public void setEnterovirusGramStainDetails(String enterovirusGramStainDetails) {
        this.enterovirusGramStainDetails = enterovirusGramStainDetails;
    }

    public void setEnterovirusLatexAgglutinationDetails(String enterovirusLatexAgglutinationDetails) {
        this.enterovirusLatexAgglutinationDetails = enterovirusLatexAgglutinationDetails;
    }

    public void setEnterovirusCqValueDetectionDetails(String enterovirusCqValueDetectionDetails) {
        this.enterovirusCqValueDetectionDetails = enterovirusCqValueDetectionDetails;
    }

    public void setEnterovirusSequencingDetails(String enterovirusSequencingDetails) {
        this.enterovirusSequencingDetails = enterovirusSequencingDetails;
    }

    public void setEnterovirusDnaMicroarrayDetails(String enterovirusDnaMicroarrayDetails) {
        this.enterovirusDnaMicroarrayDetails = enterovirusDnaMicroarrayDetails;
    }

    public void setEnterovirusOtherDetails(String enterovirusOtherDetails) {
        this.enterovirusOtherDetails = enterovirusOtherDetails;
    }

    public void setmPneumoniaeAntibodyDetection(String mPneumoniaeAntibodyDetection) {
        this.mPneumoniaeAntibodyDetection = mPneumoniaeAntibodyDetection;
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

    public void setmPneumoniaeDirectFluorescentAntibody(String mPneumoniaeDirectFluorescentAntibody) {
        this.mPneumoniaeDirectFluorescentAntibody = mPneumoniaeDirectFluorescentAntibody;
    }

    public void setmPneumoniaeMicroscopy(String mPneumoniaeMicroscopy) {
        this.mPneumoniaeMicroscopy = mPneumoniaeMicroscopy;
    }

    public void setmPneumoniaeNeutralizingAntibodies(String mPneumoniaeNeutralizingAntibodies) {
        this.mPneumoniaeNeutralizingAntibodies = mPneumoniaeNeutralizingAntibodies;
    }

    public void setmPneumoniaePcrRtPcr(String mPneumoniaePcrRtPcr) {
        this.mPneumoniaePcrRtPcr = mPneumoniaePcrRtPcr;
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

    public void setmPneumoniaeSequencing(String mPneumoniaeSequencing) {
        this.mPneumoniaeSequencing = mPneumoniaeSequencing;
    }

    public void setmPneumoniaeDnaMicroarray(String mPneumoniaeDnaMicroarray) {
        this.mPneumoniaeDnaMicroarray = mPneumoniaeDnaMicroarray;
    }

    public void setmPneumoniaeOther(String mPneumoniaeOther) {
        this.mPneumoniaeOther = mPneumoniaeOther;
    }

    public void setmPneumoniaeAntibodyDetectionDetails(String mPneumoniaeAntibodyDetectionDetails) {
        this.mPneumoniaeAntibodyDetectionDetails = mPneumoniaeAntibodyDetectionDetails;
    }

    public void setmPneumoniaeAntigenDetectionDetails(String mPneumoniaeAntigenDetectionDetails) {
        this.mPneumoniaeAntigenDetectionDetails = mPneumoniaeAntigenDetectionDetails;
    }

    public void setmPneumoniaeRapidTestDetails(String mPneumoniaeRapidTestDetails) {
        this.mPneumoniaeRapidTestDetails = mPneumoniaeRapidTestDetails;
    }

    public void setmPneumoniaeCultureDetails(String mPneumoniaeCultureDetails) {
        this.mPneumoniaeCultureDetails = mPneumoniaeCultureDetails;
    }

    public void setmPneumoniaeHistopathologyDetails(String mPneumoniaeHistopathologyDetails) {
        this.mPneumoniaeHistopathologyDetails = mPneumoniaeHistopathologyDetails;
    }

    public void setmPneumoniaeIsolationDetails(String mPneumoniaeIsolationDetails) {
        this.mPneumoniaeIsolationDetails = mPneumoniaeIsolationDetails;
    }

    public void setmPneumoniaeIgmSerumAntibodyDetails(String mPneumoniaeIgmSerumAntibodyDetails) {
        this.mPneumoniaeIgmSerumAntibodyDetails = mPneumoniaeIgmSerumAntibodyDetails;
    }

    public void setmPneumoniaeIggSerumAntibodyDetails(String mPneumoniaeIggSerumAntibodyDetails) {
        this.mPneumoniaeIggSerumAntibodyDetails = mPneumoniaeIggSerumAntibodyDetails;
    }

    public void setmPneumoniaeIgaSerumAntibodyDetails(String mPneumoniaeIgaSerumAntibodyDetails) {
        this.mPneumoniaeIgaSerumAntibodyDetails = mPneumoniaeIgaSerumAntibodyDetails;
    }

    public void setmPneumoniaeIncubationTimeDetails(String mPneumoniaeIncubationTimeDetails) {
        this.mPneumoniaeIncubationTimeDetails = mPneumoniaeIncubationTimeDetails;
    }

    public void setmPneumoniaeIndirectFluorescentAntibodyDetails(String mPneumoniaeIndirectFluorescentAntibodyDetails) {
        this.mPneumoniaeIndirectFluorescentAntibodyDetails = mPneumoniaeIndirectFluorescentAntibodyDetails;
    }

    public void setmPneumoniaeDirectFluorescentAntibodyDetails(String mPneumoniaeDirectFluorescentAntibodyDetails) {
        this.mPneumoniaeDirectFluorescentAntibodyDetails = mPneumoniaeDirectFluorescentAntibodyDetails;
    }

    public void setmPneumoniaeMicroscopyDetails(String mPneumoniaeMicroscopyDetails) {
        this.mPneumoniaeMicroscopyDetails = mPneumoniaeMicroscopyDetails;
    }

    public void setmPneumoniaeNeutralizingAntibodiesDetails(String mPneumoniaeNeutralizingAntibodiesDetails) {
        this.mPneumoniaeNeutralizingAntibodiesDetails = mPneumoniaeNeutralizingAntibodiesDetails;
    }

    public void setmPneumoniaePcrRtPcrDetails(String mPneumoniaePcrRtPcrDetails) {
        this.mPneumoniaePcrRtPcrDetails = mPneumoniaePcrRtPcrDetails;
    }

    public void setmPneumoniaeGramStainDetails(String mPneumoniaeGramStainDetails) {
        this.mPneumoniaeGramStainDetails = mPneumoniaeGramStainDetails;
    }

    public void setmPneumoniaeLatexAgglutinationDetails(String mPneumoniaeLatexAgglutinationDetails) {
        this.mPneumoniaeLatexAgglutinationDetails = mPneumoniaeLatexAgglutinationDetails;
    }

    public void setmPneumoniaeCqValueDetectionDetails(String mPneumoniaeCqValueDetectionDetails) {
        this.mPneumoniaeCqValueDetectionDetails = mPneumoniaeCqValueDetectionDetails;
    }

    public void setmPneumoniaeSequencingDetails(String mPneumoniaeSequencingDetails) {
        this.mPneumoniaeSequencingDetails = mPneumoniaeSequencingDetails;
    }

    public void setmPneumoniaeDnaMicroarrayDetails(String mPneumoniaeDnaMicroarrayDetails) {
        this.mPneumoniaeDnaMicroarrayDetails = mPneumoniaeDnaMicroarrayDetails;
    }

    public void setmPneumoniaeOtherDetails(String mPneumoniaeOtherDetails) {
        this.mPneumoniaeOtherDetails = mPneumoniaeOtherDetails;
    }

    public void setcPneumoniaeAntibodyDetection(String cPneumoniaeAntibodyDetection) {
        this.cPneumoniaeAntibodyDetection = cPneumoniaeAntibodyDetection;
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

    public void setcPneumoniaeDirectFluorescentAntibody(String cPneumoniaeDirectFluorescentAntibody) {
        this.cPneumoniaeDirectFluorescentAntibody = cPneumoniaeDirectFluorescentAntibody;
    }

    public void setcPneumoniaeMicroscopy(String cPneumoniaeMicroscopy) {
        this.cPneumoniaeMicroscopy = cPneumoniaeMicroscopy;
    }

    public void setcPneumoniaeNeutralizingAntibodies(String cPneumoniaeNeutralizingAntibodies) {
        this.cPneumoniaeNeutralizingAntibodies = cPneumoniaeNeutralizingAntibodies;
    }

    public void setcPneumoniaePcrRtPcr(String cPneumoniaePcrRtPcr) {
        this.cPneumoniaePcrRtPcr = cPneumoniaePcrRtPcr;
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

    public void setcPneumoniaeSequencing(String cPneumoniaeSequencing) {
        this.cPneumoniaeSequencing = cPneumoniaeSequencing;
    }

    public void setcPneumoniaeDnaMicroarray(String cPneumoniaeDnaMicroarray) {
        this.cPneumoniaeDnaMicroarray = cPneumoniaeDnaMicroarray;
    }

    public void setcPneumoniaeOther(String cPneumoniaeOther) {
        this.cPneumoniaeOther = cPneumoniaeOther;
    }

    public void setcPneumoniaeAntibodyDetectionDetails(String cPneumoniaeAntibodyDetectionDetails) {
        this.cPneumoniaeAntibodyDetectionDetails = cPneumoniaeAntibodyDetectionDetails;
    }

    public void setcPneumoniaeAntigenDetectionDetails(String cPneumoniaeAntigenDetectionDetails) {
        this.cPneumoniaeAntigenDetectionDetails = cPneumoniaeAntigenDetectionDetails;
    }

    public void setcPneumoniaeRapidTestDetails(String cPneumoniaeRapidTestDetails) {
        this.cPneumoniaeRapidTestDetails = cPneumoniaeRapidTestDetails;
    }

    public void setcPneumoniaeCultureDetails(String cPneumoniaeCultureDetails) {
        this.cPneumoniaeCultureDetails = cPneumoniaeCultureDetails;
    }

    public void setcPneumoniaeHistopathologyDetails(String cPneumoniaeHistopathologyDetails) {
        this.cPneumoniaeHistopathologyDetails = cPneumoniaeHistopathologyDetails;
    }

    public void setcPneumoniaeIsolationDetails(String cPneumoniaeIsolationDetails) {
        this.cPneumoniaeIsolationDetails = cPneumoniaeIsolationDetails;
    }

    public void setcPneumoniaeIgmSerumAntibodyDetails(String cPneumoniaeIgmSerumAntibodyDetails) {
        this.cPneumoniaeIgmSerumAntibodyDetails = cPneumoniaeIgmSerumAntibodyDetails;
    }

    public void setcPneumoniaeIggSerumAntibodyDetails(String cPneumoniaeIggSerumAntibodyDetails) {
        this.cPneumoniaeIggSerumAntibodyDetails = cPneumoniaeIggSerumAntibodyDetails;
    }

    public void setcPneumoniaeIgaSerumAntibodyDetails(String cPneumoniaeIgaSerumAntibodyDetails) {
        this.cPneumoniaeIgaSerumAntibodyDetails = cPneumoniaeIgaSerumAntibodyDetails;
    }

    public void setcPneumoniaeIncubationTimeDetails(String cPneumoniaeIncubationTimeDetails) {
        this.cPneumoniaeIncubationTimeDetails = cPneumoniaeIncubationTimeDetails;
    }

    public void setcPneumoniaeIndirectFluorescentAntibodyDetails(String cPneumoniaeIndirectFluorescentAntibodyDetails) {
        this.cPneumoniaeIndirectFluorescentAntibodyDetails = cPneumoniaeIndirectFluorescentAntibodyDetails;
    }

    public void setcPneumoniaeDirectFluorescentAntibodyDetails(String cPneumoniaeDirectFluorescentAntibodyDetails) {
        this.cPneumoniaeDirectFluorescentAntibodyDetails = cPneumoniaeDirectFluorescentAntibodyDetails;
    }

    public void setcPneumoniaeMicroscopyDetails(String cPneumoniaeMicroscopyDetails) {
        this.cPneumoniaeMicroscopyDetails = cPneumoniaeMicroscopyDetails;
    }

    public void setcPneumoniaeNeutralizingAntibodiesDetails(String cPneumoniaeNeutralizingAntibodiesDetails) {
        this.cPneumoniaeNeutralizingAntibodiesDetails = cPneumoniaeNeutralizingAntibodiesDetails;
    }

    public void setcPneumoniaePcrRtPcrDetails(String cPneumoniaePcrRtPcrDetails) {
        this.cPneumoniaePcrRtPcrDetails = cPneumoniaePcrRtPcrDetails;
    }

    public void setcPneumoniaeGramStainDetails(String cPneumoniaeGramStainDetails) {
        this.cPneumoniaeGramStainDetails = cPneumoniaeGramStainDetails;
    }

    public void setcPneumoniaeLatexAgglutinationDetails(String cPneumoniaeLatexAgglutinationDetails) {
        this.cPneumoniaeLatexAgglutinationDetails = cPneumoniaeLatexAgglutinationDetails;
    }

    public void setcPneumoniaeCqValueDetectionDetails(String cPneumoniaeCqValueDetectionDetails) {
        this.cPneumoniaeCqValueDetectionDetails = cPneumoniaeCqValueDetectionDetails;
    }

    public void setcPneumoniaeSequencingDetails(String cPneumoniaeSequencingDetails) {
        this.cPneumoniaeSequencingDetails = cPneumoniaeSequencingDetails;
    }

    public void setcPneumoniaeDnaMicroarrayDetails(String cPneumoniaeDnaMicroarrayDetails) {
        this.cPneumoniaeDnaMicroarrayDetails = cPneumoniaeDnaMicroarrayDetails;
    }

    public void setcPneumoniaeOtherDetails(String cPneumoniaeOtherDetails) {
        this.cPneumoniaeOtherDetails = cPneumoniaeOtherDetails;
    }

    public void setAriAntibodyDetection(String ariAntibodyDetection) {
        this.ariAntibodyDetection = ariAntibodyDetection;
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

    public void setAriDirectFluorescentAntibody(String ariDirectFluorescentAntibody) {
        this.ariDirectFluorescentAntibody = ariDirectFluorescentAntibody;
    }

    public void setAriMicroscopy(String ariMicroscopy) {
        this.ariMicroscopy = ariMicroscopy;
    }

    public void setAriNeutralizingAntibodies(String ariNeutralizingAntibodies) {
        this.ariNeutralizingAntibodies = ariNeutralizingAntibodies;
    }

    public void setAriPcrRtPcr(String ariPcrRtPcr) {
        this.ariPcrRtPcr = ariPcrRtPcr;
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

    public void setAriSequencing(String ariSequencing) {
        this.ariSequencing = ariSequencing;
    }

    public void setAriDnaMicroarray(String ariDnaMicroarray) {
        this.ariDnaMicroarray = ariDnaMicroarray;
    }

    public void setAriOther(String ariOther) {
        this.ariOther = ariOther;
    }

    public void setAriAntibodyDetectionDetails(String ariAntibodyDetectionDetails) {
        this.ariAntibodyDetectionDetails = ariAntibodyDetectionDetails;
    }

    public void setAriAntigenDetectionDetails(String ariAntigenDetectionDetails) {
        this.ariAntigenDetectionDetails = ariAntigenDetectionDetails;
    }

    public void setAriRapidTestDetails(String ariRapidTestDetails) {
        this.ariRapidTestDetails = ariRapidTestDetails;
    }

    public void setAriCultureDetails(String ariCultureDetails) {
        this.ariCultureDetails = ariCultureDetails;
    }

    public void setAriHistopathologyDetails(String ariHistopathologyDetails) {
        this.ariHistopathologyDetails = ariHistopathologyDetails;
    }

    public void setAriIsolationDetails(String ariIsolationDetails) {
        this.ariIsolationDetails = ariIsolationDetails;
    }

    public void setAriIgmSerumAntibodyDetails(String ariIgmSerumAntibodyDetails) {
        this.ariIgmSerumAntibodyDetails = ariIgmSerumAntibodyDetails;
    }

    public void setAriIggSerumAntibodyDetails(String ariIggSerumAntibodyDetails) {
        this.ariIggSerumAntibodyDetails = ariIggSerumAntibodyDetails;
    }

    public void setAriIgaSerumAntibodyDetails(String ariIgaSerumAntibodyDetails) {
        this.ariIgaSerumAntibodyDetails = ariIgaSerumAntibodyDetails;
    }

    public void setAriIncubationTimeDetails(String ariIncubationTimeDetails) {
        this.ariIncubationTimeDetails = ariIncubationTimeDetails;
    }

    public void setAriIndirectFluorescentAntibodyDetails(String ariIndirectFluorescentAntibodyDetails) {
        this.ariIndirectFluorescentAntibodyDetails = ariIndirectFluorescentAntibodyDetails;
    }

    public void setAriDirectFluorescentAntibodyDetails(String ariDirectFluorescentAntibodyDetails) {
        this.ariDirectFluorescentAntibodyDetails = ariDirectFluorescentAntibodyDetails;
    }

    public void setAriMicroscopyDetails(String ariMicroscopyDetails) {
        this.ariMicroscopyDetails = ariMicroscopyDetails;
    }

    public void setAriNeutralizingAntibodiesDetails(String ariNeutralizingAntibodiesDetails) {
        this.ariNeutralizingAntibodiesDetails = ariNeutralizingAntibodiesDetails;
    }

    public void setAriPcrRtPcrDetails(String ariPcrRtPcrDetails) {
        this.ariPcrRtPcrDetails = ariPcrRtPcrDetails;
    }

    public void setAriGramStainDetails(String ariGramStainDetails) {
        this.ariGramStainDetails = ariGramStainDetails;
    }

    public void setAriLatexAgglutinationDetails(String ariLatexAgglutinationDetails) {
        this.ariLatexAgglutinationDetails = ariLatexAgglutinationDetails;
    }

    public void setAriCqValueDetectionDetails(String ariCqValueDetectionDetails) {
        this.ariCqValueDetectionDetails = ariCqValueDetectionDetails;
    }

    public void setAriSequencingDetails(String ariSequencingDetails) {
        this.ariSequencingDetails = ariSequencingDetails;
    }

    public void setAriDnaMicroarrayDetails(String ariDnaMicroarrayDetails) {
        this.ariDnaMicroarrayDetails = ariDnaMicroarrayDetails;
    }

    public void setAriOtherDetails(String ariOtherDetails) {
        this.ariOtherDetails = ariOtherDetails;
    }

    public void setChikungunyaAntibodyDetection(String chikungunyaAntibodyDetection) {
        this.chikungunyaAntibodyDetection = chikungunyaAntibodyDetection;
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

    public void setChikungunyaDirectFluorescentAntibody(String chikungunyaDirectFluorescentAntibody) {
        this.chikungunyaDirectFluorescentAntibody = chikungunyaDirectFluorescentAntibody;
    }

    public void setChikungunyaMicroscopy(String chikungunyaMicroscopy) {
        this.chikungunyaMicroscopy = chikungunyaMicroscopy;
    }

    public void setChikungunyaNeutralizingAntibodies(String chikungunyaNeutralizingAntibodies) {
        this.chikungunyaNeutralizingAntibodies = chikungunyaNeutralizingAntibodies;
    }

    public void setChikungunyaPcrRtPcr(String chikungunyaPcrRtPcr) {
        this.chikungunyaPcrRtPcr = chikungunyaPcrRtPcr;
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

    public void setChikungunyaSequencing(String chikungunyaSequencing) {
        this.chikungunyaSequencing = chikungunyaSequencing;
    }

    public void setChikungunyaDnaMicroarray(String chikungunyaDnaMicroarray) {
        this.chikungunyaDnaMicroarray = chikungunyaDnaMicroarray;
    }

    public void setChikungunyaOther(String chikungunyaOther) {
        this.chikungunyaOther = chikungunyaOther;
    }

    public void setChikungunyaAntibodyDetectionDetails(String chikungunyaAntibodyDetectionDetails) {
        this.chikungunyaAntibodyDetectionDetails = chikungunyaAntibodyDetectionDetails;
    }

    public void setChikungunyaAntigenDetectionDetails(String chikungunyaAntigenDetectionDetails) {
        this.chikungunyaAntigenDetectionDetails = chikungunyaAntigenDetectionDetails;
    }

    public void setChikungunyaRapidTestDetails(String chikungunyaRapidTestDetails) {
        this.chikungunyaRapidTestDetails = chikungunyaRapidTestDetails;
    }

    public void setChikungunyaCultureDetails(String chikungunyaCultureDetails) {
        this.chikungunyaCultureDetails = chikungunyaCultureDetails;
    }

    public void setChikungunyaHistopathologyDetails(String chikungunyaHistopathologyDetails) {
        this.chikungunyaHistopathologyDetails = chikungunyaHistopathologyDetails;
    }

    public void setChikungunyaIsolationDetails(String chikungunyaIsolationDetails) {
        this.chikungunyaIsolationDetails = chikungunyaIsolationDetails;
    }

    public void setChikungunyaIgmSerumAntibodyDetails(String chikungunyaIgmSerumAntibodyDetails) {
        this.chikungunyaIgmSerumAntibodyDetails = chikungunyaIgmSerumAntibodyDetails;
    }

    public void setChikungunyaIggSerumAntibodyDetails(String chikungunyaIggSerumAntibodyDetails) {
        this.chikungunyaIggSerumAntibodyDetails = chikungunyaIggSerumAntibodyDetails;
    }

    public void setChikungunyaIgaSerumAntibodyDetails(String chikungunyaIgaSerumAntibodyDetails) {
        this.chikungunyaIgaSerumAntibodyDetails = chikungunyaIgaSerumAntibodyDetails;
    }

    public void setChikungunyaIncubationTimeDetails(String chikungunyaIncubationTimeDetails) {
        this.chikungunyaIncubationTimeDetails = chikungunyaIncubationTimeDetails;
    }

    public void setChikungunyaIndirectFluorescentAntibodyDetails(String chikungunyaIndirectFluorescentAntibodyDetails) {
        this.chikungunyaIndirectFluorescentAntibodyDetails = chikungunyaIndirectFluorescentAntibodyDetails;
    }

    public void setChikungunyaDirectFluorescentAntibodyDetails(String chikungunyaDirectFluorescentAntibodyDetails) {
        this.chikungunyaDirectFluorescentAntibodyDetails = chikungunyaDirectFluorescentAntibodyDetails;
    }

    public void setChikungunyaMicroscopyDetails(String chikungunyaMicroscopyDetails) {
        this.chikungunyaMicroscopyDetails = chikungunyaMicroscopyDetails;
    }

    public void setChikungunyaNeutralizingAntibodiesDetails(String chikungunyaNeutralizingAntibodiesDetails) {
        this.chikungunyaNeutralizingAntibodiesDetails = chikungunyaNeutralizingAntibodiesDetails;
    }

    public void setChikungunyaPcrRtPcrDetails(String chikungunyaPcrRtPcrDetails) {
        this.chikungunyaPcrRtPcrDetails = chikungunyaPcrRtPcrDetails;
    }

    public void setChikungunyaGramStainDetails(String chikungunyaGramStainDetails) {
        this.chikungunyaGramStainDetails = chikungunyaGramStainDetails;
    }

    public void setChikungunyaLatexAgglutinationDetails(String chikungunyaLatexAgglutinationDetails) {
        this.chikungunyaLatexAgglutinationDetails = chikungunyaLatexAgglutinationDetails;
    }

    public void setChikungunyaCqValueDetectionDetails(String chikungunyaCqValueDetectionDetails) {
        this.chikungunyaCqValueDetectionDetails = chikungunyaCqValueDetectionDetails;
    }

    public void setChikungunyaSequencingDetails(String chikungunyaSequencingDetails) {
        this.chikungunyaSequencingDetails = chikungunyaSequencingDetails;
    }

    public void setChikungunyaDnaMicroarrayDetails(String chikungunyaDnaMicroarrayDetails) {
        this.chikungunyaDnaMicroarrayDetails = chikungunyaDnaMicroarrayDetails;
    }

    public void setChikungunyaOtherDetails(String chikungunyaOtherDetails) {
        this.chikungunyaOtherDetails = chikungunyaOtherDetails;
    }

    public void setPostImmunizationAdverseEventsMildAntibodyDetection(String postImmunizationAdverseEventsMildAntibodyDetection) {
        this.postImmunizationAdverseEventsMildAntibodyDetection = postImmunizationAdverseEventsMildAntibodyDetection;
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

    public void setPostImmunizationAdverseEventsMildDirectFluorescentAntibody(String postImmunizationAdverseEventsMildDirectFluorescentAntibody) {
        this.postImmunizationAdverseEventsMildDirectFluorescentAntibody = postImmunizationAdverseEventsMildDirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsMildMicroscopy(String postImmunizationAdverseEventsMildMicroscopy) {
        this.postImmunizationAdverseEventsMildMicroscopy = postImmunizationAdverseEventsMildMicroscopy;
    }

    public void setPostImmunizationAdverseEventsMildNeutralizingAntibodies(String postImmunizationAdverseEventsMildNeutralizingAntibodies) {
        this.postImmunizationAdverseEventsMildNeutralizingAntibodies = postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsMildPcrRtPcr(String postImmunizationAdverseEventsMildPcrRtPcr) {
        this.postImmunizationAdverseEventsMildPcrRtPcr = postImmunizationAdverseEventsMildPcrRtPcr;
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

    public void setPostImmunizationAdverseEventsMildSequencing(String postImmunizationAdverseEventsMildSequencing) {
        this.postImmunizationAdverseEventsMildSequencing = postImmunizationAdverseEventsMildSequencing;
    }

    public void setPostImmunizationAdverseEventsMildDnaMicroarray(String postImmunizationAdverseEventsMildDnaMicroarray) {
        this.postImmunizationAdverseEventsMildDnaMicroarray = postImmunizationAdverseEventsMildDnaMicroarray;
    }

    public void setPostImmunizationAdverseEventsMildOther(String postImmunizationAdverseEventsMildOther) {
        this.postImmunizationAdverseEventsMildOther = postImmunizationAdverseEventsMildOther;
    }

    public void setPostImmunizationAdverseEventsMildAntibodyDetectionDetails(String postImmunizationAdverseEventsMildAntibodyDetectionDetails) {
        this.postImmunizationAdverseEventsMildAntibodyDetectionDetails = postImmunizationAdverseEventsMildAntibodyDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsMildAntigenDetectionDetails(String postImmunizationAdverseEventsMildAntigenDetectionDetails) {
        this.postImmunizationAdverseEventsMildAntigenDetectionDetails = postImmunizationAdverseEventsMildAntigenDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsMildRapidTestDetails(String postImmunizationAdverseEventsMildRapidTestDetails) {
        this.postImmunizationAdverseEventsMildRapidTestDetails = postImmunizationAdverseEventsMildRapidTestDetails;
    }

    public void setPostImmunizationAdverseEventsMildCultureDetails(String postImmunizationAdverseEventsMildCultureDetails) {
        this.postImmunizationAdverseEventsMildCultureDetails = postImmunizationAdverseEventsMildCultureDetails;
    }

    public void setPostImmunizationAdverseEventsMildHistopathologyDetails(String postImmunizationAdverseEventsMildHistopathologyDetails) {
        this.postImmunizationAdverseEventsMildHistopathologyDetails = postImmunizationAdverseEventsMildHistopathologyDetails;
    }

    public void setPostImmunizationAdverseEventsMildIsolationDetails(String postImmunizationAdverseEventsMildIsolationDetails) {
        this.postImmunizationAdverseEventsMildIsolationDetails = postImmunizationAdverseEventsMildIsolationDetails;
    }

    public void setPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails(String postImmunizationAdverseEventsMildIgmSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsMildIgmSerumAntibodyDetails = postImmunizationAdverseEventsMildIgmSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsMildIggSerumAntibodyDetails(String postImmunizationAdverseEventsMildIggSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsMildIggSerumAntibodyDetails = postImmunizationAdverseEventsMildIggSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails(String postImmunizationAdverseEventsMildIgaSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsMildIgaSerumAntibodyDetails = postImmunizationAdverseEventsMildIgaSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsMildIncubationTimeDetails(String postImmunizationAdverseEventsMildIncubationTimeDetails) {
        this.postImmunizationAdverseEventsMildIncubationTimeDetails = postImmunizationAdverseEventsMildIncubationTimeDetails;
    }

    public void setPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails(String postImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails) {
        this.postImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails = postImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails(String postImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails) {
        this.postImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails = postImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsMildMicroscopyDetails(String postImmunizationAdverseEventsMildMicroscopyDetails) {
        this.postImmunizationAdverseEventsMildMicroscopyDetails = postImmunizationAdverseEventsMildMicroscopyDetails;
    }

    public void setPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails(String postImmunizationAdverseEventsMildNeutralizingAntibodiesDetails) {
        this.postImmunizationAdverseEventsMildNeutralizingAntibodiesDetails = postImmunizationAdverseEventsMildNeutralizingAntibodiesDetails;
    }

    public void setPostImmunizationAdverseEventsMildPcrRtPcrDetails(String postImmunizationAdverseEventsMildPcrRtPcrDetails) {
        this.postImmunizationAdverseEventsMildPcrRtPcrDetails = postImmunizationAdverseEventsMildPcrRtPcrDetails;
    }

    public void setPostImmunizationAdverseEventsMildGramStainDetails(String postImmunizationAdverseEventsMildGramStainDetails) {
        this.postImmunizationAdverseEventsMildGramStainDetails = postImmunizationAdverseEventsMildGramStainDetails;
    }

    public void setPostImmunizationAdverseEventsMildLatexAgglutinationDetails(String postImmunizationAdverseEventsMildLatexAgglutinationDetails) {
        this.postImmunizationAdverseEventsMildLatexAgglutinationDetails = postImmunizationAdverseEventsMildLatexAgglutinationDetails;
    }

    public void setPostImmunizationAdverseEventsMildCqValueDetectionDetails(String postImmunizationAdverseEventsMildCqValueDetectionDetails) {
        this.postImmunizationAdverseEventsMildCqValueDetectionDetails = postImmunizationAdverseEventsMildCqValueDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsMildSequencingDetails(String postImmunizationAdverseEventsMildSequencingDetails) {
        this.postImmunizationAdverseEventsMildSequencingDetails = postImmunizationAdverseEventsMildSequencingDetails;
    }

    public void setPostImmunizationAdverseEventsMildDnaMicroarrayDetails(String postImmunizationAdverseEventsMildDnaMicroarrayDetails) {
        this.postImmunizationAdverseEventsMildDnaMicroarrayDetails = postImmunizationAdverseEventsMildDnaMicroarrayDetails;
    }

    public void setPostImmunizationAdverseEventsMildOtherDetails(String postImmunizationAdverseEventsMildOtherDetails) {
        this.postImmunizationAdverseEventsMildOtherDetails = postImmunizationAdverseEventsMildOtherDetails;
    }

    public void setPostImmunizationAdverseEventsSevereAntibodyDetection(String postImmunizationAdverseEventsSevereAntibodyDetection) {
        this.postImmunizationAdverseEventsSevereAntibodyDetection = postImmunizationAdverseEventsSevereAntibodyDetection;
    }

    public void setPostImmunizationAdverseEventsSevereAntigenDetection(String postImmunizationAdverseEventsSevereAntigenDetection) {
        this.postImmunizationAdverseEventsSevereAntigenDetection = postImmunizationAdverseEventsSevereAntigenDetection;
    }

    public void setPostImmunizationAdverseEventsSevereRapidTest(String postImmunizationAdverseEventsSevereRapidTest) {
        this.postImmunizationAdverseEventsSevereRapidTest = postImmunizationAdverseEventsSevereRapidTest;
    }

    public void setPostImmunizationAdverseEventsSevereCulture(String postImmunizationAdverseEventsSevereCulture) {
        this.postImmunizationAdverseEventsSevereCulture = postImmunizationAdverseEventsSevereCulture;
    }

    public void setPostImmunizationAdverseEventsSevereHistopathology(String postImmunizationAdverseEventsSevereHistopathology) {
        this.postImmunizationAdverseEventsSevereHistopathology = postImmunizationAdverseEventsSevereHistopathology;
    }

    public void setPostImmunizationAdverseEventsSevereIsolation(String postImmunizationAdverseEventsSevereIsolation) {
        this.postImmunizationAdverseEventsSevereIsolation = postImmunizationAdverseEventsSevereIsolation;
    }

    public void setPostImmunizationAdverseEventsSevereIgmSerumAntibody(String postImmunizationAdverseEventsSevereIgmSerumAntibody) {
        this.postImmunizationAdverseEventsSevereIgmSerumAntibody = postImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIggSerumAntibody(String postImmunizationAdverseEventsSevereIggSerumAntibody) {
        this.postImmunizationAdverseEventsSevereIggSerumAntibody = postImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIgaSerumAntibody(String postImmunizationAdverseEventsSevereIgaSerumAntibody) {
        this.postImmunizationAdverseEventsSevereIgaSerumAntibody = postImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIncubationTime(String postImmunizationAdverseEventsSevereIncubationTime) {
        this.postImmunizationAdverseEventsSevereIncubationTime = postImmunizationAdverseEventsSevereIncubationTime;
    }

    public void setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(String postImmunizationAdverseEventsSevereIndirectFluorescentAntibody) {
        this.postImmunizationAdverseEventsSevereIndirectFluorescentAntibody = postImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereDirectFluorescentAntibody(String postImmunizationAdverseEventsSevereDirectFluorescentAntibody) {
        this.postImmunizationAdverseEventsSevereDirectFluorescentAntibody = postImmunizationAdverseEventsSevereDirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereMicroscopy(String postImmunizationAdverseEventsSevereMicroscopy) {
        this.postImmunizationAdverseEventsSevereMicroscopy = postImmunizationAdverseEventsSevereMicroscopy;
    }

    public void setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(String postImmunizationAdverseEventsSevereNeutralizingAntibodies) {
        this.postImmunizationAdverseEventsSevereNeutralizingAntibodies = postImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsSeverePcrRtPcr(String postImmunizationAdverseEventsSeverePcrRtPcr) {
        this.postImmunizationAdverseEventsSeverePcrRtPcr = postImmunizationAdverseEventsSeverePcrRtPcr;
    }

    public void setPostImmunizationAdverseEventsSevereGramStain(String postImmunizationAdverseEventsSevereGramStain) {
        this.postImmunizationAdverseEventsSevereGramStain = postImmunizationAdverseEventsSevereGramStain;
    }

    public void setPostImmunizationAdverseEventsSevereLatexAgglutination(String postImmunizationAdverseEventsSevereLatexAgglutination) {
        this.postImmunizationAdverseEventsSevereLatexAgglutination = postImmunizationAdverseEventsSevereLatexAgglutination;
    }

    public void setPostImmunizationAdverseEventsSevereCqValueDetection(String postImmunizationAdverseEventsSevereCqValueDetection) {
        this.postImmunizationAdverseEventsSevereCqValueDetection = postImmunizationAdverseEventsSevereCqValueDetection;
    }

    public void setPostImmunizationAdverseEventsSevereSequencing(String postImmunizationAdverseEventsSevereSequencing) {
        this.postImmunizationAdverseEventsSevereSequencing = postImmunizationAdverseEventsSevereSequencing;
    }

    public void setPostImmunizationAdverseEventsSevereDnaMicroarray(String postImmunizationAdverseEventsSevereDnaMicroarray) {
        this.postImmunizationAdverseEventsSevereDnaMicroarray = postImmunizationAdverseEventsSevereDnaMicroarray;
    }

    public void setPostImmunizationAdverseEventsSevereOther(String postImmunizationAdverseEventsSevereOther) {
        this.postImmunizationAdverseEventsSevereOther = postImmunizationAdverseEventsSevereOther;
    }

    public void setPostImmunizationAdverseEventsSevereAntibodyDetectionDetails(String postImmunizationAdverseEventsSevereAntibodyDetectionDetails) {
        this.postImmunizationAdverseEventsSevereAntibodyDetectionDetails = postImmunizationAdverseEventsSevereAntibodyDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsSevereAntigenDetectionDetails(String postImmunizationAdverseEventsSevereAntigenDetectionDetails) {
        this.postImmunizationAdverseEventsSevereAntigenDetectionDetails = postImmunizationAdverseEventsSevereAntigenDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsSevereRapidTestDetails(String postImmunizationAdverseEventsSevereRapidTestDetails) {
        this.postImmunizationAdverseEventsSevereRapidTestDetails = postImmunizationAdverseEventsSevereRapidTestDetails;
    }

    public void setPostImmunizationAdverseEventsSevereCultureDetails(String postImmunizationAdverseEventsSevereCultureDetails) {
        this.postImmunizationAdverseEventsSevereCultureDetails = postImmunizationAdverseEventsSevereCultureDetails;
    }

    public void setPostImmunizationAdverseEventsSevereHistopathologyDetails(String postImmunizationAdverseEventsSevereHistopathologyDetails) {
        this.postImmunizationAdverseEventsSevereHistopathologyDetails = postImmunizationAdverseEventsSevereHistopathologyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIsolationDetails(String postImmunizationAdverseEventsSevereIsolationDetails) {
        this.postImmunizationAdverseEventsSevereIsolationDetails = postImmunizationAdverseEventsSevereIsolationDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails(String postImmunizationAdverseEventsSevereIgmSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsSevereIgmSerumAntibodyDetails = postImmunizationAdverseEventsSevereIgmSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails(String postImmunizationAdverseEventsSevereIggSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsSevereIggSerumAntibodyDetails = postImmunizationAdverseEventsSevereIggSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails(String postImmunizationAdverseEventsSevereIgaSerumAntibodyDetails) {
        this.postImmunizationAdverseEventsSevereIgaSerumAntibodyDetails = postImmunizationAdverseEventsSevereIgaSerumAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIncubationTimeDetails(String postImmunizationAdverseEventsSevereIncubationTimeDetails) {
        this.postImmunizationAdverseEventsSevereIncubationTimeDetails = postImmunizationAdverseEventsSevereIncubationTimeDetails;
    }

    public void setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails(String postImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails) {
        this.postImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails = postImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails(String postImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails) {
        this.postImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails = postImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereMicroscopyDetails(String postImmunizationAdverseEventsSevereMicroscopyDetails) {
        this.postImmunizationAdverseEventsSevereMicroscopyDetails = postImmunizationAdverseEventsSevereMicroscopyDetails;
    }

    public void setPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails(String postImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails) {
        this.postImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails = postImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails;
    }

    public void setPostImmunizationAdverseEventsSeverePcrRtPcrDetails(String postImmunizationAdverseEventsSeverePcrRtPcrDetails) {
        this.postImmunizationAdverseEventsSeverePcrRtPcrDetails = postImmunizationAdverseEventsSeverePcrRtPcrDetails;
    }

    public void setPostImmunizationAdverseEventsSevereGramStainDetails(String postImmunizationAdverseEventsSevereGramStainDetails) {
        this.postImmunizationAdverseEventsSevereGramStainDetails = postImmunizationAdverseEventsSevereGramStainDetails;
    }

    public void setPostImmunizationAdverseEventsSevereLatexAgglutinationDetails(String postImmunizationAdverseEventsSevereLatexAgglutinationDetails) {
        this.postImmunizationAdverseEventsSevereLatexAgglutinationDetails = postImmunizationAdverseEventsSevereLatexAgglutinationDetails;
    }

    public void setPostImmunizationAdverseEventsSevereCqValueDetectionDetails(String postImmunizationAdverseEventsSevereCqValueDetectionDetails) {
        this.postImmunizationAdverseEventsSevereCqValueDetectionDetails = postImmunizationAdverseEventsSevereCqValueDetectionDetails;
    }

    public void setPostImmunizationAdverseEventsSevereSequencingDetails(String postImmunizationAdverseEventsSevereSequencingDetails) {
        this.postImmunizationAdverseEventsSevereSequencingDetails = postImmunizationAdverseEventsSevereSequencingDetails;
    }

    public void setPostImmunizationAdverseEventsSevereDnaMicroarrayDetails(String postImmunizationAdverseEventsSevereDnaMicroarrayDetails) {
        this.postImmunizationAdverseEventsSevereDnaMicroarrayDetails = postImmunizationAdverseEventsSevereDnaMicroarrayDetails;
    }

    public void setPostImmunizationAdverseEventsSevereOtherDetails(String postImmunizationAdverseEventsSevereOtherDetails) {
        this.postImmunizationAdverseEventsSevereOtherDetails = postImmunizationAdverseEventsSevereOtherDetails;
    }

    public void setFhaAntibodyDetection(String fhaAntibodyDetection) {
        this.fhaAntibodyDetection = fhaAntibodyDetection;
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

    public void setFhaDirectFluorescentAntibody(String fhaDirectFluorescentAntibody) {
        this.fhaDirectFluorescentAntibody = fhaDirectFluorescentAntibody;
    }

    public void setFhaMicroscopy(String fhaMicroscopy) {
        this.fhaMicroscopy = fhaMicroscopy;
    }

    public void setFhaNeutralizingAntibodies(String fhaNeutralizingAntibodies) {
        this.fhaNeutralizingAntibodies = fhaNeutralizingAntibodies;
    }

    public void setFhaPcrRtPcr(String fhaPcrRtPcr) {
        this.fhaPcrRtPcr = fhaPcrRtPcr;
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

    public void setFhaSequencing(String fhaSequencing) {
        this.fhaSequencing = fhaSequencing;
    }

    public void setFhaDnaMicroarray(String fhaDnaMicroarray) {
        this.fhaDnaMicroarray = fhaDnaMicroarray;
    }

    public void setFhaOther(String fhaOther) {
        this.fhaOther = fhaOther;
    }

    public void setFhaAntibodyDetectionDetails(String fhaAntibodyDetectionDetails) {
        this.fhaAntibodyDetectionDetails = fhaAntibodyDetectionDetails;
    }

    public void setFhaAntigenDetectionDetails(String fhaAntigenDetectionDetails) {
        this.fhaAntigenDetectionDetails = fhaAntigenDetectionDetails;
    }

    public void setFhaRapidTestDetails(String fhaRapidTestDetails) {
        this.fhaRapidTestDetails = fhaRapidTestDetails;
    }

    public void setFhaCultureDetails(String fhaCultureDetails) {
        this.fhaCultureDetails = fhaCultureDetails;
    }

    public void setFhaHistopathologyDetails(String fhaHistopathologyDetails) {
        this.fhaHistopathologyDetails = fhaHistopathologyDetails;
    }

    public void setFhaIsolationDetails(String fhaIsolationDetails) {
        this.fhaIsolationDetails = fhaIsolationDetails;
    }

    public void setFhaIgmSerumAntibodyDetails(String fhaIgmSerumAntibodyDetails) {
        this.fhaIgmSerumAntibodyDetails = fhaIgmSerumAntibodyDetails;
    }

    public void setFhaIggSerumAntibodyDetails(String fhaIggSerumAntibodyDetails) {
        this.fhaIggSerumAntibodyDetails = fhaIggSerumAntibodyDetails;
    }

    public void setFhaIgaSerumAntibodyDetails(String fhaIgaSerumAntibodyDetails) {
        this.fhaIgaSerumAntibodyDetails = fhaIgaSerumAntibodyDetails;
    }

    public void setFhaIncubationTimeDetails(String fhaIncubationTimeDetails) {
        this.fhaIncubationTimeDetails = fhaIncubationTimeDetails;
    }

    public void setFhaIndirectFluorescentAntibodyDetails(String fhaIndirectFluorescentAntibodyDetails) {
        this.fhaIndirectFluorescentAntibodyDetails = fhaIndirectFluorescentAntibodyDetails;
    }

    public void setFhaDirectFluorescentAntibodyDetails(String fhaDirectFluorescentAntibodyDetails) {
        this.fhaDirectFluorescentAntibodyDetails = fhaDirectFluorescentAntibodyDetails;
    }

    public void setFhaMicroscopyDetails(String fhaMicroscopyDetails) {
        this.fhaMicroscopyDetails = fhaMicroscopyDetails;
    }

    public void setFhaNeutralizingAntibodiesDetails(String fhaNeutralizingAntibodiesDetails) {
        this.fhaNeutralizingAntibodiesDetails = fhaNeutralizingAntibodiesDetails;
    }

    public void setFhaPcrRtPcrDetails(String fhaPcrRtPcrDetails) {
        this.fhaPcrRtPcrDetails = fhaPcrRtPcrDetails;
    }

    public void setFhaGramStainDetails(String fhaGramStainDetails) {
        this.fhaGramStainDetails = fhaGramStainDetails;
    }

    public void setFhaLatexAgglutinationDetails(String fhaLatexAgglutinationDetails) {
        this.fhaLatexAgglutinationDetails = fhaLatexAgglutinationDetails;
    }

    public void setFhaCqValueDetectionDetails(String fhaCqValueDetectionDetails) {
        this.fhaCqValueDetectionDetails = fhaCqValueDetectionDetails;
    }

    public void setFhaSequencingDetails(String fhaSequencingDetails) {
        this.fhaSequencingDetails = fhaSequencingDetails;
    }

    public void setFhaDnaMicroarrayDetails(String fhaDnaMicroarrayDetails) {
        this.fhaDnaMicroarrayDetails = fhaDnaMicroarrayDetails;
    }

    public void setFhaOtherDetails(String fhaOtherDetails) {
        this.fhaOtherDetails = fhaOtherDetails;
    }

    public void setOtherAntibodyDetection(String otherAntibodyDetection) {
        this.otherAntibodyDetection = otherAntibodyDetection;
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

    public void setOtherDirectFluorescentAntibody(String otherDirectFluorescentAntibody) {
        this.otherDirectFluorescentAntibody = otherDirectFluorescentAntibody;
    }

    public void setOtherMicroscopy(String otherMicroscopy) {
        this.otherMicroscopy = otherMicroscopy;
    }

    public void setOtherNeutralizingAntibodies(String otherNeutralizingAntibodies) {
        this.otherNeutralizingAntibodies = otherNeutralizingAntibodies;
    }

    public void setOtherPcrRtPcr(String otherPcrRtPcr) {
        this.otherPcrRtPcr = otherPcrRtPcr;
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

    public void setOtherSequencing(String otherSequencing) {
        this.otherSequencing = otherSequencing;
    }

    public void setOtherDnaMicroarray(String otherDnaMicroarray) {
        this.otherDnaMicroarray = otherDnaMicroarray;
    }

    public void setOtherOther(String otherOther) {
        this.otherOther = otherOther;
    }

    public void setOtherAntibodyDetectionDetails(String otherAntibodyDetectionDetails) {
        this.otherAntibodyDetectionDetails = otherAntibodyDetectionDetails;
    }

    public void setOtherAntigenDetectionDetails(String otherAntigenDetectionDetails) {
        this.otherAntigenDetectionDetails = otherAntigenDetectionDetails;
    }

    public void setOtherRapidTestDetails(String otherRapidTestDetails) {
        this.otherRapidTestDetails = otherRapidTestDetails;
    }

    public void setOtherCultureDetails(String otherCultureDetails) {
        this.otherCultureDetails = otherCultureDetails;
    }

    public void setOtherHistopathologyDetails(String otherHistopathologyDetails) {
        this.otherHistopathologyDetails = otherHistopathologyDetails;
    }

    public void setOtherIsolationDetails(String otherIsolationDetails) {
        this.otherIsolationDetails = otherIsolationDetails;
    }

    public void setOtherIgmSerumAntibodyDetails(String otherIgmSerumAntibodyDetails) {
        this.otherIgmSerumAntibodyDetails = otherIgmSerumAntibodyDetails;
    }

    public void setOtherIggSerumAntibodyDetails(String otherIggSerumAntibodyDetails) {
        this.otherIggSerumAntibodyDetails = otherIggSerumAntibodyDetails;
    }

    public void setOtherIgaSerumAntibodyDetails(String otherIgaSerumAntibodyDetails) {
        this.otherIgaSerumAntibodyDetails = otherIgaSerumAntibodyDetails;
    }

    public void setOtherIncubationTimeDetails(String otherIncubationTimeDetails) {
        this.otherIncubationTimeDetails = otherIncubationTimeDetails;
    }

    public void setOtherIndirectFluorescentAntibodyDetails(String otherIndirectFluorescentAntibodyDetails) {
        this.otherIndirectFluorescentAntibodyDetails = otherIndirectFluorescentAntibodyDetails;
    }

    public void setOtherDirectFluorescentAntibodyDetails(String otherDirectFluorescentAntibodyDetails) {
        this.otherDirectFluorescentAntibodyDetails = otherDirectFluorescentAntibodyDetails;
    }

    public void setOtherMicroscopyDetails(String otherMicroscopyDetails) {
        this.otherMicroscopyDetails = otherMicroscopyDetails;
    }

    public void setOtherNeutralizingAntibodiesDetails(String otherNeutralizingAntibodiesDetails) {
        this.otherNeutralizingAntibodiesDetails = otherNeutralizingAntibodiesDetails;
    }

    public void setOtherPcrRtPcrDetails(String otherPcrRtPcrDetails) {
        this.otherPcrRtPcrDetails = otherPcrRtPcrDetails;
    }

    public void setOtherGramStainDetails(String otherGramStainDetails) {
        this.otherGramStainDetails = otherGramStainDetails;
    }

    public void setOtherLatexAgglutinationDetails(String otherLatexAgglutinationDetails) {
        this.otherLatexAgglutinationDetails = otherLatexAgglutinationDetails;
    }

    public void setOtherCqValueDetectionDetails(String otherCqValueDetectionDetails) {
        this.otherCqValueDetectionDetails = otherCqValueDetectionDetails;
    }

    public void setOtherSequencingDetails(String otherSequencingDetails) {
        this.otherSequencingDetails = otherSequencingDetails;
    }

    public void setOtherDnaMicroarrayDetails(String otherDnaMicroarrayDetails) {
        this.otherDnaMicroarrayDetails = otherDnaMicroarrayDetails;
    }

    public void setOtherOtherDetails(String otherOtherDetails) {
        this.otherOtherDetails = otherOtherDetails;
    }

    public void setUndefinedAntibodyDetection(String undefinedAntibodyDetection) {
        this.undefinedAntibodyDetection = undefinedAntibodyDetection;
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

    public void setUndefinedDirectFluorescentAntibody(String undefinedDirectFluorescentAntibody) {
        this.undefinedDirectFluorescentAntibody = undefinedDirectFluorescentAntibody;
    }

    public void setUndefinedMicroscopy(String undefinedMicroscopy) {
        this.undefinedMicroscopy = undefinedMicroscopy;
    }

    public void setUndefinedNeutralizingAntibodies(String undefinedNeutralizingAntibodies) {
        this.undefinedNeutralizingAntibodies = undefinedNeutralizingAntibodies;
    }

    public void setUndefinedPcrRtPcr(String undefinedPcrRtPcr) {
        this.undefinedPcrRtPcr = undefinedPcrRtPcr;
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

    public void setUndefinedSequencing(String undefinedSequencing) {
        this.undefinedSequencing = undefinedSequencing;
    }

    public void setUndefinedDnaMicroarray(String undefinedDnaMicroarray) {
        this.undefinedDnaMicroarray = undefinedDnaMicroarray;
    }

    public void setUndefinedOther(String undefinedOther) {
        this.undefinedOther = undefinedOther;
    }

    public void setUndefinedAntibodyDetectionDetails(String undefinedAntibodyDetectionDetails) {
        this.undefinedAntibodyDetectionDetails = undefinedAntibodyDetectionDetails;
    }

    public void setUndefinedAntigenDetectionDetails(String undefinedAntigenDetectionDetails) {
        this.undefinedAntigenDetectionDetails = undefinedAntigenDetectionDetails;
    }

    public void setUndefinedRapidTestDetails(String undefinedRapidTestDetails) {
        this.undefinedRapidTestDetails = undefinedRapidTestDetails;
    }

    public void setUndefinedCultureDetails(String undefinedCultureDetails) {
        this.undefinedCultureDetails = undefinedCultureDetails;
    }

    public void setUndefinedHistopathologyDetails(String undefinedHistopathologyDetails) {
        this.undefinedHistopathologyDetails = undefinedHistopathologyDetails;
    }

    public void setUndefinedIsolationDetails(String undefinedIsolationDetails) {
        this.undefinedIsolationDetails = undefinedIsolationDetails;
    }

    public void setUndefinedIgmSerumAntibodyDetails(String undefinedIgmSerumAntibodyDetails) {
        this.undefinedIgmSerumAntibodyDetails = undefinedIgmSerumAntibodyDetails;
    }

    public void setUndefinedIggSerumAntibodyDetails(String undefinedIggSerumAntibodyDetails) {
        this.undefinedIggSerumAntibodyDetails = undefinedIggSerumAntibodyDetails;
    }

    public void setUndefinedIgaSerumAntibodyDetails(String undefinedIgaSerumAntibodyDetails) {
        this.undefinedIgaSerumAntibodyDetails = undefinedIgaSerumAntibodyDetails;
    }

    public void setUndefinedIncubationTimeDetails(String undefinedIncubationTimeDetails) {
        this.undefinedIncubationTimeDetails = undefinedIncubationTimeDetails;
    }

    public void setUndefinedIndirectFluorescentAntibodyDetails(String undefinedIndirectFluorescentAntibodyDetails) {
        this.undefinedIndirectFluorescentAntibodyDetails = undefinedIndirectFluorescentAntibodyDetails;
    }

    public void setUndefinedDirectFluorescentAntibodyDetails(String undefinedDirectFluorescentAntibodyDetails) {
        this.undefinedDirectFluorescentAntibodyDetails = undefinedDirectFluorescentAntibodyDetails;
    }

    public void setUndefinedMicroscopyDetails(String undefinedMicroscopyDetails) {
        this.undefinedMicroscopyDetails = undefinedMicroscopyDetails;
    }

    public void setUndefinedNeutralizingAntibodiesDetails(String undefinedNeutralizingAntibodiesDetails) {
        this.undefinedNeutralizingAntibodiesDetails = undefinedNeutralizingAntibodiesDetails;
    }

    public void setUndefinedPcrRtPcrDetails(String undefinedPcrRtPcrDetails) {
        this.undefinedPcrRtPcrDetails = undefinedPcrRtPcrDetails;
    }

    public void setUndefinedGramStainDetails(String undefinedGramStainDetails) {
        this.undefinedGramStainDetails = undefinedGramStainDetails;
    }

    public void setUndefinedLatexAgglutinationDetails(String undefinedLatexAgglutinationDetails) {
        this.undefinedLatexAgglutinationDetails = undefinedLatexAgglutinationDetails;
    }

    public void setUndefinedCqValueDetectionDetails(String undefinedCqValueDetectionDetails) {
        this.undefinedCqValueDetectionDetails = undefinedCqValueDetectionDetails;
    }

    public void setUndefinedSequencingDetails(String undefinedSequencingDetails) {
        this.undefinedSequencingDetails = undefinedSequencingDetails;
    }

    public void setUndefinedDnaMicroarrayDetails(String undefinedDnaMicroarrayDetails) {
        this.undefinedDnaMicroarrayDetails = undefinedDnaMicroarrayDetails;
    }

    public void setUndefinedOtherDetails(String undefinedOtherDetails) {
        this.undefinedOtherDetails = undefinedOtherDetails;
    }

    public String getAfpAntibodyDetection() {
        return afpAntibodyDetection;
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

    public String getAfpDirectFluorescentAntibody() {
        return afpDirectFluorescentAntibody;
    }

    public String getAfpMicroscopy() {
        return afpMicroscopy;
    }

    public String getAfpNeutralizingAntibodies() {
        return afpNeutralizingAntibodies;
    }

    public String getAfpPcrRtPcr() {
        return afpPcrRtPcr;
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

    public String getAfpSequencing() {
        return afpSequencing;
    }

    public String getAfpDnaMicroarray() {
        return afpDnaMicroarray;
    }

    public String getAfpOther() {
        return afpOther;
    }

    public String getAfpAntibodyDetectionDetails() {
        return afpAntibodyDetectionDetails;
    }

    public String getAfpAntigenDetectionDetails() {
        return afpAntigenDetectionDetails;
    }

    public String getAfpRapidTestDetails() {
        return afpRapidTestDetails;
    }

    public String getAfpCultureDetails() {
        return afpCultureDetails;
    }

    public String getAfpHistopathologyDetails() {
        return afpHistopathologyDetails;
    }

    public String getAfpIsolationDetails() {
        return afpIsolationDetails;
    }

    public String getAfpIgmSerumAntibodyDetails() {
        return afpIgmSerumAntibodyDetails;
    }

    public String getAfpIggSerumAntibodyDetails() {
        return afpIggSerumAntibodyDetails;
    }

    public String getAfpIgaSerumAntibodyDetails() {
        return afpIgaSerumAntibodyDetails;
    }

    public String getAfpIncubationTimeDetails() {
        return afpIncubationTimeDetails;
    }

    public String getAfpIndirectFluorescentAntibodyDetails() {
        return afpIndirectFluorescentAntibodyDetails;
    }

    public String getAfpDirectFluorescentAntibodyDetails() {
        return afpDirectFluorescentAntibodyDetails;
    }

    public String getAfpMicroscopyDetails() {
        return afpMicroscopyDetails;
    }

    public String getAfpNeutralizingAntibodiesDetails() {
        return afpNeutralizingAntibodiesDetails;
    }

    public String getAfpPcrRtPcrDetails() {
        return afpPcrRtPcrDetails;
    }

    public String getAfpGramStainDetails() {
        return afpGramStainDetails;
    }

    public String getAfpLatexAgglutinationDetails() {
        return afpLatexAgglutinationDetails;
    }

    public String getAfpCqValueDetectionDetails() {
        return afpCqValueDetectionDetails;
    }

    public String getAfpSequencingDetails() {
        return afpSequencingDetails;
    }

    public String getAfpDnaMicroarrayDetails() {
        return afpDnaMicroarrayDetails;
    }

    public String getAfpOtherDetails() {
        return afpOtherDetails;
    }

    public String getCholeraAntibodyDetection() {
        return choleraAntibodyDetection;
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

    public String getCholeraDirectFluorescentAntibody() {
        return choleraDirectFluorescentAntibody;
    }

    public String getCholeraMicroscopy() {
        return choleraMicroscopy;
    }

    public String getCholeraNeutralizingAntibodies() {
        return choleraNeutralizingAntibodies;
    }

    public String getCholeraPcrRtPcr() {
        return choleraPcrRtPcr;
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

    public String getCholeraSequencing() {
        return choleraSequencing;
    }

    public String getCholeraDnaMicroarray() {
        return choleraDnaMicroarray;
    }

    public String getCholeraOther() {
        return choleraOther;
    }

    public String getCholeraAntibodyDetectionDetails() {
        return choleraAntibodyDetectionDetails;
    }

    public String getCholeraAntigenDetectionDetails() {
        return choleraAntigenDetectionDetails;
    }

    public String getCholeraRapidTestDetails() {
        return choleraRapidTestDetails;
    }

    public String getCholeraCultureDetails() {
        return choleraCultureDetails;
    }

    public String getCholeraHistopathologyDetails() {
        return choleraHistopathologyDetails;
    }

    public String getCholeraIsolationDetails() {
        return choleraIsolationDetails;
    }

    public String getCholeraIgmSerumAntibodyDetails() {
        return choleraIgmSerumAntibodyDetails;
    }

    public String getCholeraIggSerumAntibodyDetails() {
        return choleraIggSerumAntibodyDetails;
    }

    public String getCholeraIgaSerumAntibodyDetails() {
        return choleraIgaSerumAntibodyDetails;
    }

    public String getCholeraIncubationTimeDetails() {
        return choleraIncubationTimeDetails;
    }

    public String getCholeraIndirectFluorescentAntibodyDetails() {
        return choleraIndirectFluorescentAntibodyDetails;
    }

    public String getCholeraDirectFluorescentAntibodyDetails() {
        return choleraDirectFluorescentAntibodyDetails;
    }

    public String getCholeraMicroscopyDetails() {
        return choleraMicroscopyDetails;
    }

    public String getCholeraNeutralizingAntibodiesDetails() {
        return choleraNeutralizingAntibodiesDetails;
    }

    public String getCholeraPcrRtPcrDetails() {
        return choleraPcrRtPcrDetails;
    }

    public String getCholeraGramStainDetails() {
        return choleraGramStainDetails;
    }

    public String getCholeraLatexAgglutinationDetails() {
        return choleraLatexAgglutinationDetails;
    }

    public String getCholeraCqValueDetectionDetails() {
        return choleraCqValueDetectionDetails;
    }

    public String getCholeraSequencingDetails() {
        return choleraSequencingDetails;
    }

    public String getCholeraDnaMicroarrayDetails() {
        return choleraDnaMicroarrayDetails;
    }

    public String getCholeraOtherDetails() {
        return choleraOtherDetails;
    }

    public String getCongenitalRubellaAntibodyDetection() {
        return congenitalRubellaAntibodyDetection;
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

    public String getCongenitalRubellaDirectFluorescentAntibody() {
        return congenitalRubellaDirectFluorescentAntibody;
    }

    public String getCongenitalRubellaMicroscopy() {
        return congenitalRubellaMicroscopy;
    }

    public String getCongenitalRubellaNeutralizingAntibodies() {
        return congenitalRubellaNeutralizingAntibodies;
    }

    public String getCongenitalRubellaPcrRtPcr() {
        return congenitalRubellaPcrRtPcr;
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

    public String getCongenitalRubellaSequencing() {
        return congenitalRubellaSequencing;
    }

    public String getCongenitalRubellaDnaMicroarray() {
        return congenitalRubellaDnaMicroarray;
    }

    public String getCongenitalRubellaOther() {
        return congenitalRubellaOther;
    }

    public String getCongenitalRubellaAntibodyDetectionDetails() {
        return congenitalRubellaAntibodyDetectionDetails;
    }

    public String getCongenitalRubellaAntigenDetectionDetails() {
        return congenitalRubellaAntigenDetectionDetails;
    }

    public String getCongenitalRubellaRapidTestDetails() {
        return congenitalRubellaRapidTestDetails;
    }

    public String getCongenitalRubellaCultureDetails() {
        return congenitalRubellaCultureDetails;
    }

    public String getCongenitalRubellaHistopathologyDetails() {
        return congenitalRubellaHistopathologyDetails;
    }

    public String getCongenitalRubellaIsolationDetails() {
        return congenitalRubellaIsolationDetails;
    }

    public String getCongenitalRubellaIgmSerumAntibodyDetails() {
        return congenitalRubellaIgmSerumAntibodyDetails;
    }

    public String getCongenitalRubellaIggSerumAntibodyDetails() {
        return congenitalRubellaIggSerumAntibodyDetails;
    }

    public String getCongenitalRubellaIgaSerumAntibodyDetails() {
        return congenitalRubellaIgaSerumAntibodyDetails;
    }

    public String getCongenitalRubellaIncubationTimeDetails() {
        return congenitalRubellaIncubationTimeDetails;
    }

    public String getCongenitalRubellaIndirectFluorescentAntibodyDetails() {
        return congenitalRubellaIndirectFluorescentAntibodyDetails;
    }

    public String getCongenitalRubellaDirectFluorescentAntibodyDetails() {
        return congenitalRubellaDirectFluorescentAntibodyDetails;
    }

    public String getCongenitalRubellaMicroscopyDetails() {
        return congenitalRubellaMicroscopyDetails;
    }

    public String getCongenitalRubellaNeutralizingAntibodiesDetails() {
        return congenitalRubellaNeutralizingAntibodiesDetails;
    }

    public String getCongenitalRubellaPcrRtPcrDetails() {
        return congenitalRubellaPcrRtPcrDetails;
    }

    public String getCongenitalRubellaGramStainDetails() {
        return congenitalRubellaGramStainDetails;
    }

    public String getCongenitalRubellaLatexAgglutinationDetails() {
        return congenitalRubellaLatexAgglutinationDetails;
    }

    public String getCongenitalRubellaCqValueDetectionDetails() {
        return congenitalRubellaCqValueDetectionDetails;
    }

    public String getCongenitalRubellaSequencingDetails() {
        return congenitalRubellaSequencingDetails;
    }

    public String getCongenitalRubellaDnaMicroarrayDetails() {
        return congenitalRubellaDnaMicroarrayDetails;
    }

    public String getCongenitalRubellaOtherDetails() {
        return congenitalRubellaOtherDetails;
    }

    public String getCsmAntibodyDetection() {
        return csmAntibodyDetection;
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

    public String getCsmDirectFluorescentAntibody() {
        return csmDirectFluorescentAntibody;
    }

    public String getCsmMicroscopy() {
        return csmMicroscopy;
    }

    public String getCsmNeutralizingAntibodies() {
        return csmNeutralizingAntibodies;
    }

    public String getCsmPcrRtPcr() {
        return csmPcrRtPcr;
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

    public String getCsmSequencing() {
        return csmSequencing;
    }

    public String getCsmDnaMicroarray() {
        return csmDnaMicroarray;
    }

    public String getCsmOther() {
        return csmOther;
    }

    public String getCsmAntibodyDetectionDetails() {
        return csmAntibodyDetectionDetails;
    }

    public String getCsmAntigenDetectionDetails() {
        return csmAntigenDetectionDetails;
    }

    public String getCsmRapidTestDetails() {
        return csmRapidTestDetails;
    }

    public String getCsmCultureDetails() {
        return csmCultureDetails;
    }

    public String getCsmHistopathologyDetails() {
        return csmHistopathologyDetails;
    }

    public String getCsmIsolationDetails() {
        return csmIsolationDetails;
    }

    public String getCsmIgmSerumAntibodyDetails() {
        return csmIgmSerumAntibodyDetails;
    }

    public String getCsmIggSerumAntibodyDetails() {
        return csmIggSerumAntibodyDetails;
    }

    public String getCsmIgaSerumAntibodyDetails() {
        return csmIgaSerumAntibodyDetails;
    }

    public String getCsmIncubationTimeDetails() {
        return csmIncubationTimeDetails;
    }

    public String getCsmIndirectFluorescentAntibodyDetails() {
        return csmIndirectFluorescentAntibodyDetails;
    }

    public String getCsmDirectFluorescentAntibodyDetails() {
        return csmDirectFluorescentAntibodyDetails;
    }

    public String getCsmMicroscopyDetails() {
        return csmMicroscopyDetails;
    }

    public String getCsmNeutralizingAntibodiesDetails() {
        return csmNeutralizingAntibodiesDetails;
    }

    public String getCsmPcrRtPcrDetails() {
        return csmPcrRtPcrDetails;
    }

    public String getCsmGramStainDetails() {
        return csmGramStainDetails;
    }

    public String getCsmLatexAgglutinationDetails() {
        return csmLatexAgglutinationDetails;
    }

    public String getCsmCqValueDetectionDetails() {
        return csmCqValueDetectionDetails;
    }

    public String getCsmSequencingDetails() {
        return csmSequencingDetails;
    }

    public String getCsmDnaMicroarrayDetails() {
        return csmDnaMicroarrayDetails;
    }

    public String getCsmOtherDetails() {
        return csmOtherDetails;
    }

    public String getDengueAntibodyDetection() {
        return dengueAntibodyDetection;
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

    public String getDengueDirectFluorescentAntibody() {
        return dengueDirectFluorescentAntibody;
    }

    public String getDengueMicroscopy() {
        return dengueMicroscopy;
    }

    public String getDengueNeutralizingAntibodies() {
        return dengueNeutralizingAntibodies;
    }

    public String getDenguePcrRtPcr() {
        return denguePcrRtPcr;
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

    public String getDengueSequencing() {
        return dengueSequencing;
    }

    public String getDengueDnaMicroarray() {
        return dengueDnaMicroarray;
    }

    public String getDengueOther() {
        return dengueOther;
    }

    public String getDengueAntibodyDetectionDetails() {
        return dengueAntibodyDetectionDetails;
    }

    public String getDengueAntigenDetectionDetails() {
        return dengueAntigenDetectionDetails;
    }

    public String getDengueRapidTestDetails() {
        return dengueRapidTestDetails;
    }

    public String getDengueCultureDetails() {
        return dengueCultureDetails;
    }

    public String getDengueHistopathologyDetails() {
        return dengueHistopathologyDetails;
    }

    public String getDengueIsolationDetails() {
        return dengueIsolationDetails;
    }

    public String getDengueIgmSerumAntibodyDetails() {
        return dengueIgmSerumAntibodyDetails;
    }

    public String getDengueIggSerumAntibodyDetails() {
        return dengueIggSerumAntibodyDetails;
    }

    public String getDengueIgaSerumAntibodyDetails() {
        return dengueIgaSerumAntibodyDetails;
    }

    public String getDengueIncubationTimeDetails() {
        return dengueIncubationTimeDetails;
    }

    public String getDengueIndirectFluorescentAntibodyDetails() {
        return dengueIndirectFluorescentAntibodyDetails;
    }

    public String getDengueDirectFluorescentAntibodyDetails() {
        return dengueDirectFluorescentAntibodyDetails;
    }

    public String getDengueMicroscopyDetails() {
        return dengueMicroscopyDetails;
    }

    public String getDengueNeutralizingAntibodiesDetails() {
        return dengueNeutralizingAntibodiesDetails;
    }

    public String getDenguePcrRtPcrDetails() {
        return denguePcrRtPcrDetails;
    }

    public String getDengueGramStainDetails() {
        return dengueGramStainDetails;
    }

    public String getDengueLatexAgglutinationDetails() {
        return dengueLatexAgglutinationDetails;
    }

    public String getDengueCqValueDetectionDetails() {
        return dengueCqValueDetectionDetails;
    }

    public String getDengueSequencingDetails() {
        return dengueSequencingDetails;
    }

    public String getDengueDnaMicroarrayDetails() {
        return dengueDnaMicroarrayDetails;
    }

    public String getDengueOtherDetails() {
        return dengueOtherDetails;
    }

    public String getEvdAntibodyDetection() {
        return evdAntibodyDetection;
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

    public String getEvdDirectFluorescentAntibody() {
        return evdDirectFluorescentAntibody;
    }

    public String getEvdMicroscopy() {
        return evdMicroscopy;
    }

    public String getEvdNeutralizingAntibodies() {
        return evdNeutralizingAntibodies;
    }

    public String getEvdPcrRtPcr() {
        return evdPcrRtPcr;
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

    public String getEvdSequencing() {
        return evdSequencing;
    }

    public String getEvdDnaMicroarray() {
        return evdDnaMicroarray;
    }

    public String getEvdOther() {
        return evdOther;
    }

    public String getEvdAntibodyDetectionDetails() {
        return evdAntibodyDetectionDetails;
    }

    public String getEvdAntigenDetectionDetails() {
        return evdAntigenDetectionDetails;
    }

    public String getEvdRapidTestDetails() {
        return evdRapidTestDetails;
    }

    public String getEvdCultureDetails() {
        return evdCultureDetails;
    }

    public String getEvdHistopathologyDetails() {
        return evdHistopathologyDetails;
    }

    public String getEvdIsolationDetails() {
        return evdIsolationDetails;
    }

    public String getEvdIgmSerumAntibodyDetails() {
        return evdIgmSerumAntibodyDetails;
    }

    public String getEvdIggSerumAntibodyDetails() {
        return evdIggSerumAntibodyDetails;
    }

    public String getEvdIgaSerumAntibodyDetails() {
        return evdIgaSerumAntibodyDetails;
    }

    public String getEvdIncubationTimeDetails() {
        return evdIncubationTimeDetails;
    }

    public String getEvdIndirectFluorescentAntibodyDetails() {
        return evdIndirectFluorescentAntibodyDetails;
    }

    public String getEvdDirectFluorescentAntibodyDetails() {
        return evdDirectFluorescentAntibodyDetails;
    }

    public String getEvdMicroscopyDetails() {
        return evdMicroscopyDetails;
    }

    public String getEvdNeutralizingAntibodiesDetails() {
        return evdNeutralizingAntibodiesDetails;
    }

    public String getEvdPcrRtPcrDetails() {
        return evdPcrRtPcrDetails;
    }

    public String getEvdGramStainDetails() {
        return evdGramStainDetails;
    }

    public String getEvdLatexAgglutinationDetails() {
        return evdLatexAgglutinationDetails;
    }

    public String getEvdCqValueDetectionDetails() {
        return evdCqValueDetectionDetails;
    }

    public String getEvdSequencingDetails() {
        return evdSequencingDetails;
    }

    public String getEvdDnaMicroarrayDetails() {
        return evdDnaMicroarrayDetails;
    }

    public String getEvdOtherDetails() {
        return evdOtherDetails;
    }

    public String getGuineaWormAntibodyDetection() {
        return guineaWormAntibodyDetection;
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

    public String getGuineaWormDirectFluorescentAntibody() {
        return guineaWormDirectFluorescentAntibody;
    }

    public String getGuineaWormMicroscopy() {
        return guineaWormMicroscopy;
    }

    public String getGuineaWormNeutralizingAntibodies() {
        return guineaWormNeutralizingAntibodies;
    }

    public String getGuineaWormPcrRtPcr() {
        return guineaWormPcrRtPcr;
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

    public String getGuineaWormSequencing() {
        return guineaWormSequencing;
    }

    public String getGuineaWormDnaMicroarray() {
        return guineaWormDnaMicroarray;
    }

    public String getGuineaWormOther() {
        return guineaWormOther;
    }

    public String getGuineaWormAntibodyDetectionDetails() {
        return guineaWormAntibodyDetectionDetails;
    }

    public String getGuineaWormAntigenDetectionDetails() {
        return guineaWormAntigenDetectionDetails;
    }

    public String getGuineaWormRapidTestDetails() {
        return guineaWormRapidTestDetails;
    }

    public String getGuineaWormCultureDetails() {
        return guineaWormCultureDetails;
    }

    public String getGuineaWormHistopathologyDetails() {
        return guineaWormHistopathologyDetails;
    }

    public String getGuineaWormIsolationDetails() {
        return guineaWormIsolationDetails;
    }

    public String getGuineaWormIgmSerumAntibodyDetails() {
        return guineaWormIgmSerumAntibodyDetails;
    }

    public String getGuineaWormIggSerumAntibodyDetails() {
        return guineaWormIggSerumAntibodyDetails;
    }

    public String getGuineaWormIgaSerumAntibodyDetails() {
        return guineaWormIgaSerumAntibodyDetails;
    }

    public String getGuineaWormIncubationTimeDetails() {
        return guineaWormIncubationTimeDetails;
    }

    public String getGuineaWormIndirectFluorescentAntibodyDetails() {
        return guineaWormIndirectFluorescentAntibodyDetails;
    }

    public String getGuineaWormDirectFluorescentAntibodyDetails() {
        return guineaWormDirectFluorescentAntibodyDetails;
    }

    public String getGuineaWormMicroscopyDetails() {
        return guineaWormMicroscopyDetails;
    }

    public String getGuineaWormNeutralizingAntibodiesDetails() {
        return guineaWormNeutralizingAntibodiesDetails;
    }

    public String getGuineaWormPcrRtPcrDetails() {
        return guineaWormPcrRtPcrDetails;
    }

    public String getGuineaWormGramStainDetails() {
        return guineaWormGramStainDetails;
    }

    public String getGuineaWormLatexAgglutinationDetails() {
        return guineaWormLatexAgglutinationDetails;
    }

    public String getGuineaWormCqValueDetectionDetails() {
        return guineaWormCqValueDetectionDetails;
    }

    public String getGuineaWormSequencingDetails() {
        return guineaWormSequencingDetails;
    }

    public String getGuineaWormDnaMicroarrayDetails() {
        return guineaWormDnaMicroarrayDetails;
    }

    public String getGuineaWormOtherDetails() {
        return guineaWormOtherDetails;
    }

    public String getLassaAntibodyDetection() {
        return lassaAntibodyDetection;
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

    public String getLassaDirectFluorescentAntibody() {
        return lassaDirectFluorescentAntibody;
    }

    public String getLassaMicroscopy() {
        return lassaMicroscopy;
    }

    public String getLassaNeutralizingAntibodies() {
        return lassaNeutralizingAntibodies;
    }

    public String getLassaPcrRtPcr() {
        return lassaPcrRtPcr;
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

    public String getLassaSequencing() {
        return lassaSequencing;
    }

    public String getLassaDnaMicroarray() {
        return lassaDnaMicroarray;
    }

    public String getLassaOther() {
        return lassaOther;
    }

    public String getLassaAntibodyDetectionDetails() {
        return lassaAntibodyDetectionDetails;
    }

    public String getLassaAntigenDetectionDetails() {
        return lassaAntigenDetectionDetails;
    }

    public String getLassaRapidTestDetails() {
        return lassaRapidTestDetails;
    }

    public String getLassaCultureDetails() {
        return lassaCultureDetails;
    }

    public String getLassaHistopathologyDetails() {
        return lassaHistopathologyDetails;
    }

    public String getLassaIsolationDetails() {
        return lassaIsolationDetails;
    }

    public String getLassaIgmSerumAntibodyDetails() {
        return lassaIgmSerumAntibodyDetails;
    }

    public String getLassaIggSerumAntibodyDetails() {
        return lassaIggSerumAntibodyDetails;
    }

    public String getLassaIgaSerumAntibodyDetails() {
        return lassaIgaSerumAntibodyDetails;
    }

    public String getLassaIncubationTimeDetails() {
        return lassaIncubationTimeDetails;
    }

    public String getLassaIndirectFluorescentAntibodyDetails() {
        return lassaIndirectFluorescentAntibodyDetails;
    }

    public String getLassaDirectFluorescentAntibodyDetails() {
        return lassaDirectFluorescentAntibodyDetails;
    }

    public String getLassaMicroscopyDetails() {
        return lassaMicroscopyDetails;
    }

    public String getLassaNeutralizingAntibodiesDetails() {
        return lassaNeutralizingAntibodiesDetails;
    }

    public String getLassaPcrRtPcrDetails() {
        return lassaPcrRtPcrDetails;
    }

    public String getLassaGramStainDetails() {
        return lassaGramStainDetails;
    }

    public String getLassaLatexAgglutinationDetails() {
        return lassaLatexAgglutinationDetails;
    }

    public String getLassaCqValueDetectionDetails() {
        return lassaCqValueDetectionDetails;
    }

    public String getLassaSequencingDetails() {
        return lassaSequencingDetails;
    }

    public String getLassaDnaMicroarrayDetails() {
        return lassaDnaMicroarrayDetails;
    }

    public String getLassaOtherDetails() {
        return lassaOtherDetails;
    }

    public String getMeaslesAntibodyDetection() {
        return measlesAntibodyDetection;
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

    public String getMeaslesDirectFluorescentAntibody() {
        return measlesDirectFluorescentAntibody;
    }

    public String getMeaslesMicroscopy() {
        return measlesMicroscopy;
    }

    public String getMeaslesNeutralizingAntibodies() {
        return measlesNeutralizingAntibodies;
    }

    public String getMeaslesPcrRtPcr() {
        return measlesPcrRtPcr;
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

    public String getMeaslesSequencing() {
        return measlesSequencing;
    }

    public String getMeaslesDnaMicroarray() {
        return measlesDnaMicroarray;
    }

    public String getMeaslesOther() {
        return measlesOther;
    }

    public String getMeaslesAntibodyDetectionDetails() {
        return measlesAntibodyDetectionDetails;
    }

    public String getMeaslesAntigenDetectionDetails() {
        return measlesAntigenDetectionDetails;
    }

    public String getMeaslesRapidTestDetails() {
        return measlesRapidTestDetails;
    }

    public String getMeaslesCultureDetails() {
        return measlesCultureDetails;
    }

    public String getMeaslesHistopathologyDetails() {
        return measlesHistopathologyDetails;
    }

    public String getMeaslesIsolationDetails() {
        return measlesIsolationDetails;
    }

    public String getMeaslesIgmSerumAntibodyDetails() {
        return measlesIgmSerumAntibodyDetails;
    }

    public String getMeaslesIggSerumAntibodyDetails() {
        return measlesIggSerumAntibodyDetails;
    }

    public String getMeaslesIgaSerumAntibodyDetails() {
        return measlesIgaSerumAntibodyDetails;
    }

    public String getMeaslesIncubationTimeDetails() {
        return measlesIncubationTimeDetails;
    }

    public String getMeaslesIndirectFluorescentAntibodyDetails() {
        return measlesIndirectFluorescentAntibodyDetails;
    }

    public String getMeaslesDirectFluorescentAntibodyDetails() {
        return measlesDirectFluorescentAntibodyDetails;
    }

    public String getMeaslesMicroscopyDetails() {
        return measlesMicroscopyDetails;
    }

    public String getMeaslesNeutralizingAntibodiesDetails() {
        return measlesNeutralizingAntibodiesDetails;
    }

    public String getMeaslesPcrRtPcrDetails() {
        return measlesPcrRtPcrDetails;
    }

    public String getMeaslesGramStainDetails() {
        return measlesGramStainDetails;
    }

    public String getMeaslesLatexAgglutinationDetails() {
        return measlesLatexAgglutinationDetails;
    }

    public String getMeaslesCqValueDetectionDetails() {
        return measlesCqValueDetectionDetails;
    }

    public String getMeaslesSequencingDetails() {
        return measlesSequencingDetails;
    }

    public String getMeaslesDnaMicroarrayDetails() {
        return measlesDnaMicroarrayDetails;
    }

    public String getMeaslesOtherDetails() {
        return measlesOtherDetails;
    }

    public String getMonkeypoxAntibodyDetection() {
        return monkeypoxAntibodyDetection;
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

    public String getMonkeypoxDirectFluorescentAntibody() {
        return monkeypoxDirectFluorescentAntibody;
    }

    public String getMonkeypoxMicroscopy() {
        return monkeypoxMicroscopy;
    }

    public String getMonkeypoxNeutralizingAntibodies() {
        return monkeypoxNeutralizingAntibodies;
    }

    public String getMonkeypoxPcrRtPcr() {
        return monkeypoxPcrRtPcr;
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

    public String getMonkeypoxSequencing() {
        return monkeypoxSequencing;
    }

    public String getMonkeypoxDnaMicroarray() {
        return monkeypoxDnaMicroarray;
    }

    public String getMonkeypoxOther() {
        return monkeypoxOther;
    }

    public String getMonkeypoxAntibodyDetectionDetails() {
        return monkeypoxAntibodyDetectionDetails;
    }

    public String getMonkeypoxAntigenDetectionDetails() {
        return monkeypoxAntigenDetectionDetails;
    }

    public String getMonkeypoxRapidTestDetails() {
        return monkeypoxRapidTestDetails;
    }

    public String getMonkeypoxCultureDetails() {
        return monkeypoxCultureDetails;
    }

    public String getMonkeypoxHistopathologyDetails() {
        return monkeypoxHistopathologyDetails;
    }

    public String getMonkeypoxIsolationDetails() {
        return monkeypoxIsolationDetails;
    }

    public String getMonkeypoxIgmSerumAntibodyDetails() {
        return monkeypoxIgmSerumAntibodyDetails;
    }

    public String getMonkeypoxIggSerumAntibodyDetails() {
        return monkeypoxIggSerumAntibodyDetails;
    }

    public String getMonkeypoxIgaSerumAntibodyDetails() {
        return monkeypoxIgaSerumAntibodyDetails;
    }

    public String getMonkeypoxIncubationTimeDetails() {
        return monkeypoxIncubationTimeDetails;
    }

    public String getMonkeypoxIndirectFluorescentAntibodyDetails() {
        return monkeypoxIndirectFluorescentAntibodyDetails;
    }

    public String getMonkeypoxDirectFluorescentAntibodyDetails() {
        return monkeypoxDirectFluorescentAntibodyDetails;
    }

    public String getMonkeypoxMicroscopyDetails() {
        return monkeypoxMicroscopyDetails;
    }

    public String getMonkeypoxNeutralizingAntibodiesDetails() {
        return monkeypoxNeutralizingAntibodiesDetails;
    }

    public String getMonkeypoxPcrRtPcrDetails() {
        return monkeypoxPcrRtPcrDetails;
    }

    public String getMonkeypoxGramStainDetails() {
        return monkeypoxGramStainDetails;
    }

    public String getMonkeypoxLatexAgglutinationDetails() {
        return monkeypoxLatexAgglutinationDetails;
    }

    public String getMonkeypoxCqValueDetectionDetails() {
        return monkeypoxCqValueDetectionDetails;
    }

    public String getMonkeypoxSequencingDetails() {
        return monkeypoxSequencingDetails;
    }

    public String getMonkeypoxDnaMicroarrayDetails() {
        return monkeypoxDnaMicroarrayDetails;
    }

    public String getMonkeypoxOtherDetails() {
        return monkeypoxOtherDetails;
    }

    public String getNewInfluenzaAntibodyDetection() {
        return newInfluenzaAntibodyDetection;
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

    public String getNewInfluenzaDirectFluorescentAntibody() {
        return newInfluenzaDirectFluorescentAntibody;
    }

    public String getNewInfluenzaMicroscopy() {
        return newInfluenzaMicroscopy;
    }

    public String getNewInfluenzaNeutralizingAntibodies() {
        return newInfluenzaNeutralizingAntibodies;
    }

    public String getNewInfluenzaPcrRtPcr() {
        return newInfluenzaPcrRtPcr;
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

    public String getNewInfluenzaSequencing() {
        return newInfluenzaSequencing;
    }

    public String getNewInfluenzaDnaMicroarray() {
        return newInfluenzaDnaMicroarray;
    }

    public String getNewInfluenzaOther() {
        return newInfluenzaOther;
    }

    public String getNewInfluenzaAntibodyDetectionDetails() {
        return newInfluenzaAntibodyDetectionDetails;
    }

    public String getNewInfluenzaAntigenDetectionDetails() {
        return newInfluenzaAntigenDetectionDetails;
    }

    public String getNewInfluenzaRapidTestDetails() {
        return newInfluenzaRapidTestDetails;
    }

    public String getNewInfluenzaCultureDetails() {
        return newInfluenzaCultureDetails;
    }

    public String getNewInfluenzaHistopathologyDetails() {
        return newInfluenzaHistopathologyDetails;
    }

    public String getNewInfluenzaIsolationDetails() {
        return newInfluenzaIsolationDetails;
    }

    public String getNewInfluenzaIgmSerumAntibodyDetails() {
        return newInfluenzaIgmSerumAntibodyDetails;
    }

    public String getNewInfluenzaIggSerumAntibodyDetails() {
        return newInfluenzaIggSerumAntibodyDetails;
    }

    public String getNewInfluenzaIgaSerumAntibodyDetails() {
        return newInfluenzaIgaSerumAntibodyDetails;
    }

    public String getNewInfluenzaIncubationTimeDetails() {
        return newInfluenzaIncubationTimeDetails;
    }

    public String getNewInfluenzaIndirectFluorescentAntibodyDetails() {
        return newInfluenzaIndirectFluorescentAntibodyDetails;
    }

    public String getNewInfluenzaDirectFluorescentAntibodyDetails() {
        return newInfluenzaDirectFluorescentAntibodyDetails;
    }

    public String getNewInfluenzaMicroscopyDetails() {
        return newInfluenzaMicroscopyDetails;
    }

    public String getNewInfluenzaNeutralizingAntibodiesDetails() {
        return newInfluenzaNeutralizingAntibodiesDetails;
    }

    public String getNewInfluenzaPcrRtPcrDetails() {
        return newInfluenzaPcrRtPcrDetails;
    }

    public String getNewInfluenzaGramStainDetails() {
        return newInfluenzaGramStainDetails;
    }

    public String getNewInfluenzaLatexAgglutinationDetails() {
        return newInfluenzaLatexAgglutinationDetails;
    }

    public String getNewInfluenzaCqValueDetectionDetails() {
        return newInfluenzaCqValueDetectionDetails;
    }

    public String getNewInfluenzaSequencingDetails() {
        return newInfluenzaSequencingDetails;
    }

    public String getNewInfluenzaDnaMicroarrayDetails() {
        return newInfluenzaDnaMicroarrayDetails;
    }

    public String getNewInfluenzaOtherDetails() {
        return newInfluenzaOtherDetails;
    }

    public String getPlagueAntibodyDetection() {
        return plagueAntibodyDetection;
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

    public String getPlagueDirectFluorescentAntibody() {
        return plagueDirectFluorescentAntibody;
    }

    public String getPlagueMicroscopy() {
        return plagueMicroscopy;
    }

    public String getPlagueNeutralizingAntibodies() {
        return plagueNeutralizingAntibodies;
    }

    public String getPlaguePcrRtPcr() {
        return plaguePcrRtPcr;
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

    public String getPlagueSequencing() {
        return plagueSequencing;
    }

    public String getPlagueDnaMicroarray() {
        return plagueDnaMicroarray;
    }

    public String getPlagueOther() {
        return plagueOther;
    }

    public String getPlagueAntibodyDetectionDetails() {
        return plagueAntibodyDetectionDetails;
    }

    public String getPlagueAntigenDetectionDetails() {
        return plagueAntigenDetectionDetails;
    }

    public String getPlagueRapidTestDetails() {
        return plagueRapidTestDetails;
    }

    public String getPlagueCultureDetails() {
        return plagueCultureDetails;
    }

    public String getPlagueHistopathologyDetails() {
        return plagueHistopathologyDetails;
    }

    public String getPlagueIsolationDetails() {
        return plagueIsolationDetails;
    }

    public String getPlagueIgmSerumAntibodyDetails() {
        return plagueIgmSerumAntibodyDetails;
    }

    public String getPlagueIggSerumAntibodyDetails() {
        return plagueIggSerumAntibodyDetails;
    }

    public String getPlagueIgaSerumAntibodyDetails() {
        return plagueIgaSerumAntibodyDetails;
    }

    public String getPlagueIncubationTimeDetails() {
        return plagueIncubationTimeDetails;
    }

    public String getPlagueIndirectFluorescentAntibodyDetails() {
        return plagueIndirectFluorescentAntibodyDetails;
    }

    public String getPlagueDirectFluorescentAntibodyDetails() {
        return plagueDirectFluorescentAntibodyDetails;
    }

    public String getPlagueMicroscopyDetails() {
        return plagueMicroscopyDetails;
    }

    public String getPlagueNeutralizingAntibodiesDetails() {
        return plagueNeutralizingAntibodiesDetails;
    }

    public String getPlaguePcrRtPcrDetails() {
        return plaguePcrRtPcrDetails;
    }

    public String getPlagueGramStainDetails() {
        return plagueGramStainDetails;
    }

    public String getPlagueLatexAgglutinationDetails() {
        return plagueLatexAgglutinationDetails;
    }

    public String getPlagueCqValueDetectionDetails() {
        return plagueCqValueDetectionDetails;
    }

    public String getPlagueSequencingDetails() {
        return plagueSequencingDetails;
    }

    public String getPlagueDnaMicroarrayDetails() {
        return plagueDnaMicroarrayDetails;
    }

    public String getPlagueOtherDetails() {
        return plagueOtherDetails;
    }

    public String getPolioAntibodyDetection() {
        return polioAntibodyDetection;
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

    public String getPolioDirectFluorescentAntibody() {
        return polioDirectFluorescentAntibody;
    }

    public String getPolioMicroscopy() {
        return polioMicroscopy;
    }

    public String getPolioNeutralizingAntibodies() {
        return polioNeutralizingAntibodies;
    }

    public String getPolioPcrRtPcr() {
        return polioPcrRtPcr;
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

    public String getPolioSequencing() {
        return polioSequencing;
    }

    public String getPolioDnaMicroarray() {
        return polioDnaMicroarray;
    }

    public String getPolioOther() {
        return polioOther;
    }

    public String getPolioAntibodyDetectionDetails() {
        return polioAntibodyDetectionDetails;
    }

    public String getPolioAntigenDetectionDetails() {
        return polioAntigenDetectionDetails;
    }

    public String getPolioRapidTestDetails() {
        return polioRapidTestDetails;
    }

    public String getPolioCultureDetails() {
        return polioCultureDetails;
    }

    public String getPolioHistopathologyDetails() {
        return polioHistopathologyDetails;
    }

    public String getPolioIsolationDetails() {
        return polioIsolationDetails;
    }

    public String getPolioIgmSerumAntibodyDetails() {
        return polioIgmSerumAntibodyDetails;
    }

    public String getPolioIggSerumAntibodyDetails() {
        return polioIggSerumAntibodyDetails;
    }

    public String getPolioIgaSerumAntibodyDetails() {
        return polioIgaSerumAntibodyDetails;
    }

    public String getPolioIncubationTimeDetails() {
        return polioIncubationTimeDetails;
    }

    public String getPolioIndirectFluorescentAntibodyDetails() {
        return polioIndirectFluorescentAntibodyDetails;
    }

    public String getPolioDirectFluorescentAntibodyDetails() {
        return polioDirectFluorescentAntibodyDetails;
    }

    public String getPolioMicroscopyDetails() {
        return polioMicroscopyDetails;
    }

    public String getPolioNeutralizingAntibodiesDetails() {
        return polioNeutralizingAntibodiesDetails;
    }

    public String getPolioPcrRtPcrDetails() {
        return polioPcrRtPcrDetails;
    }

    public String getPolioGramStainDetails() {
        return polioGramStainDetails;
    }

    public String getPolioLatexAgglutinationDetails() {
        return polioLatexAgglutinationDetails;
    }

    public String getPolioCqValueDetectionDetails() {
        return polioCqValueDetectionDetails;
    }

    public String getPolioSequencingDetails() {
        return polioSequencingDetails;
    }

    public String getPolioDnaMicroarrayDetails() {
        return polioDnaMicroarrayDetails;
    }

    public String getPolioOtherDetails() {
        return polioOtherDetails;
    }

    public String getUnspecifiedVhfAntibodyDetection() {
        return unspecifiedVhfAntibodyDetection;
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

    public String getUnspecifiedVhfDirectFluorescentAntibody() {
        return unspecifiedVhfDirectFluorescentAntibody;
    }

    public String getUnspecifiedVhfMicroscopy() {
        return unspecifiedVhfMicroscopy;
    }

    public String getUnspecifiedVhfNeutralizingAntibodies() {
        return unspecifiedVhfNeutralizingAntibodies;
    }

    public String getUnspecifiedVhfPcrRtPcr() {
        return unspecifiedVhfPcrRtPcr;
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

    public String getUnspecifiedVhfSequencing() {
        return unspecifiedVhfSequencing;
    }

    public String getUnspecifiedVhfDnaMicroarray() {
        return unspecifiedVhfDnaMicroarray;
    }

    public String getUnspecifiedVhfOther() {
        return unspecifiedVhfOther;
    }

    public String getUnspecifiedVhfAntibodyDetectionDetails() {
        return unspecifiedVhfAntibodyDetectionDetails;
    }

    public String getUnspecifiedVhfAntigenDetectionDetails() {
        return unspecifiedVhfAntigenDetectionDetails;
    }

    public String getUnspecifiedVhfRapidTestDetails() {
        return unspecifiedVhfRapidTestDetails;
    }

    public String getUnspecifiedVhfCultureDetails() {
        return unspecifiedVhfCultureDetails;
    }

    public String getUnspecifiedVhfHistopathologyDetails() {
        return unspecifiedVhfHistopathologyDetails;
    }

    public String getUnspecifiedVhfIsolationDetails() {
        return unspecifiedVhfIsolationDetails;
    }

    public String getUnspecifiedVhfIgmSerumAntibodyDetails() {
        return unspecifiedVhfIgmSerumAntibodyDetails;
    }

    public String getUnspecifiedVhfIggSerumAntibodyDetails() {
        return unspecifiedVhfIggSerumAntibodyDetails;
    }

    public String getUnspecifiedVhfIgaSerumAntibodyDetails() {
        return unspecifiedVhfIgaSerumAntibodyDetails;
    }

    public String getUnspecifiedVhfIncubationTimeDetails() {
        return unspecifiedVhfIncubationTimeDetails;
    }

    public String getUnspecifiedVhfIndirectFluorescentAntibodyDetails() {
        return unspecifiedVhfIndirectFluorescentAntibodyDetails;
    }

    public String getUnspecifiedVhfDirectFluorescentAntibodyDetails() {
        return unspecifiedVhfDirectFluorescentAntibodyDetails;
    }

    public String getUnspecifiedVhfMicroscopyDetails() {
        return unspecifiedVhfMicroscopyDetails;
    }

    public String getUnspecifiedVhfNeutralizingAntibodiesDetails() {
        return unspecifiedVhfNeutralizingAntibodiesDetails;
    }

    public String getUnspecifiedVhfPcrRtPcrDetails() {
        return unspecifiedVhfPcrRtPcrDetails;
    }

    public String getUnspecifiedVhfGramStainDetails() {
        return unspecifiedVhfGramStainDetails;
    }

    public String getUnspecifiedVhfLatexAgglutinationDetails() {
        return unspecifiedVhfLatexAgglutinationDetails;
    }

    public String getUnspecifiedVhfCqValueDetectionDetails() {
        return unspecifiedVhfCqValueDetectionDetails;
    }

    public String getUnspecifiedVhfSequencingDetails() {
        return unspecifiedVhfSequencingDetails;
    }

    public String getUnspecifiedVhfDnaMicroarrayDetails() {
        return unspecifiedVhfDnaMicroarrayDetails;
    }

    public String getUnspecifiedVhfOtherDetails() {
        return unspecifiedVhfOtherDetails;
    }

    public String getWestNileFeverAntibodyDetection() {
        return westNileFeverAntibodyDetection;
    }

    public String getWestNileFeverAntigenDetection() {
        return westNileFeverAntigenDetection;
    }

    public String getWestNileFeverRapidTest() {
        return westNileFeverRapidTest;
    }

    public String getWestNileFeverCulture() {
        return westNileFeverCulture;
    }

    public String getWestNileFeverHistopathology() {
        return westNileFeverHistopathology;
    }

    public String getWestNileFeverIsolation() {
        return westNileFeverIsolation;
    }

    public String getWestNileFeverIgmSerumAntibody() {
        return westNileFeverIgmSerumAntibody;
    }

    public String getWestNileFeverIggSerumAntibody() {
        return westNileFeverIggSerumAntibody;
    }

    public String getWestNileFeverIgaSerumAntibody() {
        return westNileFeverIgaSerumAntibody;
    }

    public String getWestNileFeverIncubationTime() {
        return westNileFeverIncubationTime;
    }

    public String getWestNileFeverIndirectFluorescentAntibody() {
        return westNileFeverIndirectFluorescentAntibody;
    }

    public String getWestNileFeverDirectFluorescentAntibody() {
        return westNileFeverDirectFluorescentAntibody;
    }

    public String getWestNileFeverMicroscopy() {
        return westNileFeverMicroscopy;
    }

    public String getWestNileFeverNeutralizingAntibodies() {
        return westNileFeverNeutralizingAntibodies;
    }

    public String getWestNileFeverPcrRtPcr() {
        return westNileFeverPcrRtPcr;
    }

    public String getWestNileFeverGramStain() {
        return westNileFeverGramStain;
    }

    public String getWestNileFeverLatexAgglutination() {
        return westNileFeverLatexAgglutination;
    }

    public String getWestNileFeverCqValueDetection() {
        return westNileFeverCqValueDetection;
    }

    public String getWestNileFeverSequencing() {
        return westNileFeverSequencing;
    }

    public String getWestNileFeverDnaMicroarray() {
        return westNileFeverDnaMicroarray;
    }

    public String getWestNileFeverOther() {
        return westNileFeverOther;
    }

    public String getWestNileFeverAntibodyDetectionDetails() {
        return westNileFeverAntibodyDetectionDetails;
    }

    public String getWestNileFeverAntigenDetectionDetails() {
        return westNileFeverAntigenDetectionDetails;
    }

    public String getWestNileFeverRapidTestDetails() {
        return westNileFeverRapidTestDetails;
    }

    public String getWestNileFeverCultureDetails() {
        return westNileFeverCultureDetails;
    }

    public String getWestNileFeverHistopathologyDetails() {
        return westNileFeverHistopathologyDetails;
    }

    public String getWestNileFeverIsolationDetails() {
        return westNileFeverIsolationDetails;
    }

    public String getWestNileFeverIgmSerumAntibodyDetails() {
        return westNileFeverIgmSerumAntibodyDetails;
    }

    public String getWestNileFeverIggSerumAntibodyDetails() {
        return westNileFeverIggSerumAntibodyDetails;
    }

    public String getWestNileFeverIgaSerumAntibodyDetails() {
        return westNileFeverIgaSerumAntibodyDetails;
    }

    public String getWestNileFeverIncubationTimeDetails() {
        return westNileFeverIncubationTimeDetails;
    }

    public String getWestNileFeverIndirectFluorescentAntibodyDetails() {
        return westNileFeverIndirectFluorescentAntibodyDetails;
    }

    public String getWestNileFeverDirectFluorescentAntibodyDetails() {
        return westNileFeverDirectFluorescentAntibodyDetails;
    }

    public String getWestNileFeverMicroscopyDetails() {
        return westNileFeverMicroscopyDetails;
    }

    public String getWestNileFeverNeutralizingAntibodiesDetails() {
        return westNileFeverNeutralizingAntibodiesDetails;
    }

    public String getWestNileFeverPcrRtPcrDetails() {
        return westNileFeverPcrRtPcrDetails;
    }

    public String getWestNileFeverGramStainDetails() {
        return westNileFeverGramStainDetails;
    }

    public String getWestNileFeverLatexAgglutinationDetails() {
        return westNileFeverLatexAgglutinationDetails;
    }

    public String getWestNileFeverCqValueDetectionDetails() {
        return westNileFeverCqValueDetectionDetails;
    }

    public String getWestNileFeverSequencingDetails() {
        return westNileFeverSequencingDetails;
    }

    public String getWestNileFeverDnaMicroarrayDetails() {
        return westNileFeverDnaMicroarrayDetails;
    }

    public String getWestNileFeverOtherDetails() {
        return westNileFeverOtherDetails;
    }

    public String getYellowFeverAntibodyDetection() {
        return yellowFeverAntibodyDetection;
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

    public String getYellowFeverDirectFluorescentAntibody() {
        return yellowFeverDirectFluorescentAntibody;
    }

    public String getYellowFeverMicroscopy() {
        return yellowFeverMicroscopy;
    }

    public String getYellowFeverNeutralizingAntibodies() {
        return yellowFeverNeutralizingAntibodies;
    }

    public String getYellowFeverPcrRtPcr() {
        return yellowFeverPcrRtPcr;
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

    public String getYellowFeverSequencing() {
        return yellowFeverSequencing;
    }

    public String getYellowFeverDnaMicroarray() {
        return yellowFeverDnaMicroarray;
    }

    public String getYellowFeverOther() {
        return yellowFeverOther;
    }

    public String getYellowFeverAntibodyDetectionDetails() {
        return yellowFeverAntibodyDetectionDetails;
    }

    public String getYellowFeverAntigenDetectionDetails() {
        return yellowFeverAntigenDetectionDetails;
    }

    public String getYellowFeverRapidTestDetails() {
        return yellowFeverRapidTestDetails;
    }

    public String getYellowFeverCultureDetails() {
        return yellowFeverCultureDetails;
    }

    public String getYellowFeverHistopathologyDetails() {
        return yellowFeverHistopathologyDetails;
    }

    public String getYellowFeverIsolationDetails() {
        return yellowFeverIsolationDetails;
    }

    public String getYellowFeverIgmSerumAntibodyDetails() {
        return yellowFeverIgmSerumAntibodyDetails;
    }

    public String getYellowFeverIggSerumAntibodyDetails() {
        return yellowFeverIggSerumAntibodyDetails;
    }

    public String getYellowFeverIgaSerumAntibodyDetails() {
        return yellowFeverIgaSerumAntibodyDetails;
    }

    public String getYellowFeverIncubationTimeDetails() {
        return yellowFeverIncubationTimeDetails;
    }

    public String getYellowFeverIndirectFluorescentAntibodyDetails() {
        return yellowFeverIndirectFluorescentAntibodyDetails;
    }

    public String getYellowFeverDirectFluorescentAntibodyDetails() {
        return yellowFeverDirectFluorescentAntibodyDetails;
    }

    public String getYellowFeverMicroscopyDetails() {
        return yellowFeverMicroscopyDetails;
    }

    public String getYellowFeverNeutralizingAntibodiesDetails() {
        return yellowFeverNeutralizingAntibodiesDetails;
    }

    public String getYellowFeverPcrRtPcrDetails() {
        return yellowFeverPcrRtPcrDetails;
    }

    public String getYellowFeverGramStainDetails() {
        return yellowFeverGramStainDetails;
    }

    public String getYellowFeverLatexAgglutinationDetails() {
        return yellowFeverLatexAgglutinationDetails;
    }

    public String getYellowFeverCqValueDetectionDetails() {
        return yellowFeverCqValueDetectionDetails;
    }

    public String getYellowFeverSequencingDetails() {
        return yellowFeverSequencingDetails;
    }

    public String getYellowFeverDnaMicroarrayDetails() {
        return yellowFeverDnaMicroarrayDetails;
    }

    public String getYellowFeverOtherDetails() {
        return yellowFeverOtherDetails;
    }

    public String getRabiesAntibodyDetection() {
        return rabiesAntibodyDetection;
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

    public String getRabiesDirectFluorescentAntibody() {
        return rabiesDirectFluorescentAntibody;
    }

    public String getRabiesMicroscopy() {
        return rabiesMicroscopy;
    }

    public String getRabiesNeutralizingAntibodies() {
        return rabiesNeutralizingAntibodies;
    }

    public String getRabiesPcrRtPcr() {
        return rabiesPcrRtPcr;
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

    public String getRabiesSequencing() {
        return rabiesSequencing;
    }

    public String getRabiesDnaMicroarray() {
        return rabiesDnaMicroarray;
    }

    public String getRabiesOther() {
        return rabiesOther;
    }

    public String getRabiesAntibodyDetectionDetails() {
        return rabiesAntibodyDetectionDetails;
    }

    public String getRabiesAntigenDetectionDetails() {
        return rabiesAntigenDetectionDetails;
    }

    public String getRabiesRapidTestDetails() {
        return rabiesRapidTestDetails;
    }

    public String getRabiesCultureDetails() {
        return rabiesCultureDetails;
    }

    public String getRabiesHistopathologyDetails() {
        return rabiesHistopathologyDetails;
    }

    public String getRabiesIsolationDetails() {
        return rabiesIsolationDetails;
    }

    public String getRabiesIgmSerumAntibodyDetails() {
        return rabiesIgmSerumAntibodyDetails;
    }

    public String getRabiesIggSerumAntibodyDetails() {
        return rabiesIggSerumAntibodyDetails;
    }

    public String getRabiesIgaSerumAntibodyDetails() {
        return rabiesIgaSerumAntibodyDetails;
    }

    public String getRabiesIncubationTimeDetails() {
        return rabiesIncubationTimeDetails;
    }

    public String getRabiesIndirectFluorescentAntibodyDetails() {
        return rabiesIndirectFluorescentAntibodyDetails;
    }

    public String getRabiesDirectFluorescentAntibodyDetails() {
        return rabiesDirectFluorescentAntibodyDetails;
    }

    public String getRabiesMicroscopyDetails() {
        return rabiesMicroscopyDetails;
    }

    public String getRabiesNeutralizingAntibodiesDetails() {
        return rabiesNeutralizingAntibodiesDetails;
    }

    public String getRabiesPcrRtPcrDetails() {
        return rabiesPcrRtPcrDetails;
    }

    public String getRabiesGramStainDetails() {
        return rabiesGramStainDetails;
    }

    public String getRabiesLatexAgglutinationDetails() {
        return rabiesLatexAgglutinationDetails;
    }

    public String getRabiesCqValueDetectionDetails() {
        return rabiesCqValueDetectionDetails;
    }

    public String getRabiesSequencingDetails() {
        return rabiesSequencingDetails;
    }

    public String getRabiesDnaMicroarrayDetails() {
        return rabiesDnaMicroarrayDetails;
    }

    public String getRabiesOtherDetails() {
        return rabiesOtherDetails;
    }

    public String getAnthraxAntibodyDetection() {
        return anthraxAntibodyDetection;
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

    public String getAnthraxDirectFluorescentAntibody() {
        return anthraxDirectFluorescentAntibody;
    }

    public String getAnthraxMicroscopy() {
        return anthraxMicroscopy;
    }

    public String getAnthraxNeutralizingAntibodies() {
        return anthraxNeutralizingAntibodies;
    }

    public String getAnthraxPcrRtPcr() {
        return anthraxPcrRtPcr;
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

    public String getAnthraxSequencing() {
        return anthraxSequencing;
    }

    public String getAnthraxDnaMicroarray() {
        return anthraxDnaMicroarray;
    }

    public String getAnthraxOther() {
        return anthraxOther;
    }

    public String getAnthraxAntibodyDetectionDetails() {
        return anthraxAntibodyDetectionDetails;
    }

    public String getAnthraxAntigenDetectionDetails() {
        return anthraxAntigenDetectionDetails;
    }

    public String getAnthraxRapidTestDetails() {
        return anthraxRapidTestDetails;
    }

    public String getAnthraxCultureDetails() {
        return anthraxCultureDetails;
    }

    public String getAnthraxHistopathologyDetails() {
        return anthraxHistopathologyDetails;
    }

    public String getAnthraxIsolationDetails() {
        return anthraxIsolationDetails;
    }

    public String getAnthraxIgmSerumAntibodyDetails() {
        return anthraxIgmSerumAntibodyDetails;
    }

    public String getAnthraxIggSerumAntibodyDetails() {
        return anthraxIggSerumAntibodyDetails;
    }

    public String getAnthraxIgaSerumAntibodyDetails() {
        return anthraxIgaSerumAntibodyDetails;
    }

    public String getAnthraxIncubationTimeDetails() {
        return anthraxIncubationTimeDetails;
    }

    public String getAnthraxIndirectFluorescentAntibodyDetails() {
        return anthraxIndirectFluorescentAntibodyDetails;
    }

    public String getAnthraxDirectFluorescentAntibodyDetails() {
        return anthraxDirectFluorescentAntibodyDetails;
    }

    public String getAnthraxMicroscopyDetails() {
        return anthraxMicroscopyDetails;
    }

    public String getAnthraxNeutralizingAntibodiesDetails() {
        return anthraxNeutralizingAntibodiesDetails;
    }

    public String getAnthraxPcrRtPcrDetails() {
        return anthraxPcrRtPcrDetails;
    }

    public String getAnthraxGramStainDetails() {
        return anthraxGramStainDetails;
    }

    public String getAnthraxLatexAgglutinationDetails() {
        return anthraxLatexAgglutinationDetails;
    }

    public String getAnthraxCqValueDetectionDetails() {
        return anthraxCqValueDetectionDetails;
    }

    public String getAnthraxSequencingDetails() {
        return anthraxSequencingDetails;
    }

    public String getAnthraxDnaMicroarrayDetails() {
        return anthraxDnaMicroarrayDetails;
    }

    public String getAnthraxOtherDetails() {
        return anthraxOtherDetails;
    }

    public String getCoronavirusAntibodyDetection() {
        return coronavirusAntibodyDetection;
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

    public String getCoronavirusDirectFluorescentAntibody() {
        return coronavirusDirectFluorescentAntibody;
    }

    public String getCoronavirusMicroscopy() {
        return coronavirusMicroscopy;
    }

    public String getCoronavirusNeutralizingAntibodies() {
        return coronavirusNeutralizingAntibodies;
    }

    public String getCoronavirusPcrRtPcr() {
        return coronavirusPcrRtPcr;
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

    public String getCoronavirusSequencing() {
        return coronavirusSequencing;
    }

    public String getCoronavirusDnaMicroarray() {
        return coronavirusDnaMicroarray;
    }

    public String getCoronavirusOther() {
        return coronavirusOther;
    }

    public String getCoronavirusAntibodyDetectionDetails() {
        return coronavirusAntibodyDetectionDetails;
    }

    public String getCoronavirusAntigenDetectionDetails() {
        return coronavirusAntigenDetectionDetails;
    }

    public String getCoronavirusRapidTestDetails() {
        return coronavirusRapidTestDetails;
    }

    public String getCoronavirusCultureDetails() {
        return coronavirusCultureDetails;
    }

    public String getCoronavirusHistopathologyDetails() {
        return coronavirusHistopathologyDetails;
    }

    public String getCoronavirusIsolationDetails() {
        return coronavirusIsolationDetails;
    }

    public String getCoronavirusIgmSerumAntibodyDetails() {
        return coronavirusIgmSerumAntibodyDetails;
    }

    public String getCoronavirusIggSerumAntibodyDetails() {
        return coronavirusIggSerumAntibodyDetails;
    }

    public String getCoronavirusIgaSerumAntibodyDetails() {
        return coronavirusIgaSerumAntibodyDetails;
    }

    public String getCoronavirusIncubationTimeDetails() {
        return coronavirusIncubationTimeDetails;
    }

    public String getCoronavirusIndirectFluorescentAntibodyDetails() {
        return coronavirusIndirectFluorescentAntibodyDetails;
    }

    public String getCoronavirusDirectFluorescentAntibodyDetails() {
        return coronavirusDirectFluorescentAntibodyDetails;
    }

    public String getCoronavirusMicroscopyDetails() {
        return coronavirusMicroscopyDetails;
    }

    public String getCoronavirusNeutralizingAntibodiesDetails() {
        return coronavirusNeutralizingAntibodiesDetails;
    }

    public String getCoronavirusPcrRtPcrDetails() {
        return coronavirusPcrRtPcrDetails;
    }

    public String getCoronavirusGramStainDetails() {
        return coronavirusGramStainDetails;
    }

    public String getCoronavirusLatexAgglutinationDetails() {
        return coronavirusLatexAgglutinationDetails;
    }

    public String getCoronavirusCqValueDetectionDetails() {
        return coronavirusCqValueDetectionDetails;
    }

    public String getCoronavirusSequencingDetails() {
        return coronavirusSequencingDetails;
    }

    public String getCoronavirusDnaMicroarrayDetails() {
        return coronavirusDnaMicroarrayDetails;
    }

    public String getCoronavirusOtherDetails() {
        return coronavirusOtherDetails;
    }

    public String getPneumoniaAntibodyDetection() {
        return pneumoniaAntibodyDetection;
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

    public String getPneumoniaDirectFluorescentAntibody() {
        return pneumoniaDirectFluorescentAntibody;
    }

    public String getPneumoniaMicroscopy() {
        return pneumoniaMicroscopy;
    }

    public String getPneumoniaNeutralizingAntibodies() {
        return pneumoniaNeutralizingAntibodies;
    }

    public String getPneumoniaPcrRtPcr() {
        return pneumoniaPcrRtPcr;
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

    public String getPneumoniaSequencing() {
        return pneumoniaSequencing;
    }

    public String getPneumoniaDnaMicroarray() {
        return pneumoniaDnaMicroarray;
    }

    public String getPneumoniaOther() {
        return pneumoniaOther;
    }

    public String getPneumoniaAntibodyDetectionDetails() {
        return pneumoniaAntibodyDetectionDetails;
    }

    public String getPneumoniaAntigenDetectionDetails() {
        return pneumoniaAntigenDetectionDetails;
    }

    public String getPneumoniaRapidTestDetails() {
        return pneumoniaRapidTestDetails;
    }

    public String getPneumoniaCultureDetails() {
        return pneumoniaCultureDetails;
    }

    public String getPneumoniaHistopathologyDetails() {
        return pneumoniaHistopathologyDetails;
    }

    public String getPneumoniaIsolationDetails() {
        return pneumoniaIsolationDetails;
    }

    public String getPneumoniaIgmSerumAntibodyDetails() {
        return pneumoniaIgmSerumAntibodyDetails;
    }

    public String getPneumoniaIggSerumAntibodyDetails() {
        return pneumoniaIggSerumAntibodyDetails;
    }

    public String getPneumoniaIgaSerumAntibodyDetails() {
        return pneumoniaIgaSerumAntibodyDetails;
    }

    public String getPneumoniaIncubationTimeDetails() {
        return pneumoniaIncubationTimeDetails;
    }

    public String getPneumoniaIndirectFluorescentAntibodyDetails() {
        return pneumoniaIndirectFluorescentAntibodyDetails;
    }

    public String getPneumoniaDirectFluorescentAntibodyDetails() {
        return pneumoniaDirectFluorescentAntibodyDetails;
    }

    public String getPneumoniaMicroscopyDetails() {
        return pneumoniaMicroscopyDetails;
    }

    public String getPneumoniaNeutralizingAntibodiesDetails() {
        return pneumoniaNeutralizingAntibodiesDetails;
    }

    public String getPneumoniaPcrRtPcrDetails() {
        return pneumoniaPcrRtPcrDetails;
    }

    public String getPneumoniaGramStainDetails() {
        return pneumoniaGramStainDetails;
    }

    public String getPneumoniaLatexAgglutinationDetails() {
        return pneumoniaLatexAgglutinationDetails;
    }

    public String getPneumoniaCqValueDetectionDetails() {
        return pneumoniaCqValueDetectionDetails;
    }

    public String getPneumoniaSequencingDetails() {
        return pneumoniaSequencingDetails;
    }

    public String getPneumoniaDnaMicroarrayDetails() {
        return pneumoniaDnaMicroarrayDetails;
    }

    public String getPneumoniaOtherDetails() {
        return pneumoniaOtherDetails;
    }

    public String getMalariaAntibodyDetection() {
        return malariaAntibodyDetection;
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

    public String getMalariaDirectFluorescentAntibody() {
        return malariaDirectFluorescentAntibody;
    }

    public String getMalariaMicroscopy() {
        return malariaMicroscopy;
    }

    public String getMalariaNeutralizingAntibodies() {
        return malariaNeutralizingAntibodies;
    }

    public String getMalariaPcrRtPcr() {
        return malariaPcrRtPcr;
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

    public String getMalariaSequencing() {
        return malariaSequencing;
    }

    public String getMalariaDnaMicroarray() {
        return malariaDnaMicroarray;
    }

    public String getMalariaOther() {
        return malariaOther;
    }

    public String getMalariaAntibodyDetectionDetails() {
        return malariaAntibodyDetectionDetails;
    }

    public String getMalariaAntigenDetectionDetails() {
        return malariaAntigenDetectionDetails;
    }

    public String getMalariaRapidTestDetails() {
        return malariaRapidTestDetails;
    }

    public String getMalariaCultureDetails() {
        return malariaCultureDetails;
    }

    public String getMalariaHistopathologyDetails() {
        return malariaHistopathologyDetails;
    }

    public String getMalariaIsolationDetails() {
        return malariaIsolationDetails;
    }

    public String getMalariaIgmSerumAntibodyDetails() {
        return malariaIgmSerumAntibodyDetails;
    }

    public String getMalariaIggSerumAntibodyDetails() {
        return malariaIggSerumAntibodyDetails;
    }

    public String getMalariaIgaSerumAntibodyDetails() {
        return malariaIgaSerumAntibodyDetails;
    }

    public String getMalariaIncubationTimeDetails() {
        return malariaIncubationTimeDetails;
    }

    public String getMalariaIndirectFluorescentAntibodyDetails() {
        return malariaIndirectFluorescentAntibodyDetails;
    }

    public String getMalariaDirectFluorescentAntibodyDetails() {
        return malariaDirectFluorescentAntibodyDetails;
    }

    public String getMalariaMicroscopyDetails() {
        return malariaMicroscopyDetails;
    }

    public String getMalariaNeutralizingAntibodiesDetails() {
        return malariaNeutralizingAntibodiesDetails;
    }

    public String getMalariaPcrRtPcrDetails() {
        return malariaPcrRtPcrDetails;
    }

    public String getMalariaGramStainDetails() {
        return malariaGramStainDetails;
    }

    public String getMalariaLatexAgglutinationDetails() {
        return malariaLatexAgglutinationDetails;
    }

    public String getMalariaCqValueDetectionDetails() {
        return malariaCqValueDetectionDetails;
    }

    public String getMalariaSequencingDetails() {
        return malariaSequencingDetails;
    }

    public String getMalariaDnaMicroarrayDetails() {
        return malariaDnaMicroarrayDetails;
    }

    public String getMalariaOtherDetails() {
        return malariaOtherDetails;
    }

    public String getTyphoidFeverAntibodyDetection() {
        return typhoidFeverAntibodyDetection;
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

    public String getTyphoidFeverDirectFluorescentAntibody() {
        return typhoidFeverDirectFluorescentAntibody;
    }

    public String getTyphoidFeverMicroscopy() {
        return typhoidFeverMicroscopy;
    }

    public String getTyphoidFeverNeutralizingAntibodies() {
        return typhoidFeverNeutralizingAntibodies;
    }

    public String getTyphoidFeverPcrRtPcr() {
        return typhoidFeverPcrRtPcr;
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

    public String getTyphoidFeverSequencing() {
        return typhoidFeverSequencing;
    }

    public String getTyphoidFeverDnaMicroarray() {
        return typhoidFeverDnaMicroarray;
    }

    public String getTyphoidFeverOther() {
        return typhoidFeverOther;
    }

    public String getTyphoidFeverAntibodyDetectionDetails() {
        return typhoidFeverAntibodyDetectionDetails;
    }

    public String getTyphoidFeverAntigenDetectionDetails() {
        return typhoidFeverAntigenDetectionDetails;
    }

    public String getTyphoidFeverRapidTestDetails() {
        return typhoidFeverRapidTestDetails;
    }

    public String getTyphoidFeverCultureDetails() {
        return typhoidFeverCultureDetails;
    }

    public String getTyphoidFeverHistopathologyDetails() {
        return typhoidFeverHistopathologyDetails;
    }

    public String getTyphoidFeverIsolationDetails() {
        return typhoidFeverIsolationDetails;
    }

    public String getTyphoidFeverIgmSerumAntibodyDetails() {
        return typhoidFeverIgmSerumAntibodyDetails;
    }

    public String getTyphoidFeverIggSerumAntibodyDetails() {
        return typhoidFeverIggSerumAntibodyDetails;
    }

    public String getTyphoidFeverIgaSerumAntibodyDetails() {
        return typhoidFeverIgaSerumAntibodyDetails;
    }

    public String getTyphoidFeverIncubationTimeDetails() {
        return typhoidFeverIncubationTimeDetails;
    }

    public String getTyphoidFeverIndirectFluorescentAntibodyDetails() {
        return typhoidFeverIndirectFluorescentAntibodyDetails;
    }

    public String getTyphoidFeverDirectFluorescentAntibodyDetails() {
        return typhoidFeverDirectFluorescentAntibodyDetails;
    }

    public String getTyphoidFeverMicroscopyDetails() {
        return typhoidFeverMicroscopyDetails;
    }

    public String getTyphoidFeverNeutralizingAntibodiesDetails() {
        return typhoidFeverNeutralizingAntibodiesDetails;
    }

    public String getTyphoidFeverPcrRtPcrDetails() {
        return typhoidFeverPcrRtPcrDetails;
    }

    public String getTyphoidFeverGramStainDetails() {
        return typhoidFeverGramStainDetails;
    }

    public String getTyphoidFeverLatexAgglutinationDetails() {
        return typhoidFeverLatexAgglutinationDetails;
    }

    public String getTyphoidFeverCqValueDetectionDetails() {
        return typhoidFeverCqValueDetectionDetails;
    }

    public String getTyphoidFeverSequencingDetails() {
        return typhoidFeverSequencingDetails;
    }

    public String getTyphoidFeverDnaMicroarrayDetails() {
        return typhoidFeverDnaMicroarrayDetails;
    }

    public String getTyphoidFeverOtherDetails() {
        return typhoidFeverOtherDetails;
    }

    public String getAcuteViralHepatitisAntibodyDetection() {
        return acuteViralHepatitisAntibodyDetection;
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

    public String getAcuteViralHepatitisDirectFluorescentAntibody() {
        return acuteViralHepatitisDirectFluorescentAntibody;
    }

    public String getAcuteViralHepatitisMicroscopy() {
        return acuteViralHepatitisMicroscopy;
    }

    public String getAcuteViralHepatitisNeutralizingAntibodies() {
        return acuteViralHepatitisNeutralizingAntibodies;
    }

    public String getAcuteViralHepatitisPcrRtPcr() {
        return acuteViralHepatitisPcrRtPcr;
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

    public String getAcuteViralHepatitisSequencing() {
        return acuteViralHepatitisSequencing;
    }

    public String getAcuteViralHepatitisDnaMicroarray() {
        return acuteViralHepatitisDnaMicroarray;
    }

    public String getAcuteViralHepatitisOther() {
        return acuteViralHepatitisOther;
    }

    public String getAcuteViralHepatitisAntibodyDetectionDetails() {
        return acuteViralHepatitisAntibodyDetectionDetails;
    }

    public String getAcuteViralHepatitisAntigenDetectionDetails() {
        return acuteViralHepatitisAntigenDetectionDetails;
    }

    public String getAcuteViralHepatitisRapidTestDetails() {
        return acuteViralHepatitisRapidTestDetails;
    }

    public String getAcuteViralHepatitisCultureDetails() {
        return acuteViralHepatitisCultureDetails;
    }

    public String getAcuteViralHepatitisHistopathologyDetails() {
        return acuteViralHepatitisHistopathologyDetails;
    }

    public String getAcuteViralHepatitisIsolationDetails() {
        return acuteViralHepatitisIsolationDetails;
    }

    public String getAcuteViralHepatitisIgmSerumAntibodyDetails() {
        return acuteViralHepatitisIgmSerumAntibodyDetails;
    }

    public String getAcuteViralHepatitisIggSerumAntibodyDetails() {
        return acuteViralHepatitisIggSerumAntibodyDetails;
    }

    public String getAcuteViralHepatitisIgaSerumAntibodyDetails() {
        return acuteViralHepatitisIgaSerumAntibodyDetails;
    }

    public String getAcuteViralHepatitisIncubationTimeDetails() {
        return acuteViralHepatitisIncubationTimeDetails;
    }

    public String getAcuteViralHepatitisIndirectFluorescentAntibodyDetails() {
        return acuteViralHepatitisIndirectFluorescentAntibodyDetails;
    }

    public String getAcuteViralHepatitisDirectFluorescentAntibodyDetails() {
        return acuteViralHepatitisDirectFluorescentAntibodyDetails;
    }

    public String getAcuteViralHepatitisMicroscopyDetails() {
        return acuteViralHepatitisMicroscopyDetails;
    }

    public String getAcuteViralHepatitisNeutralizingAntibodiesDetails() {
        return acuteViralHepatitisNeutralizingAntibodiesDetails;
    }

    public String getAcuteViralHepatitisPcrRtPcrDetails() {
        return acuteViralHepatitisPcrRtPcrDetails;
    }

    public String getAcuteViralHepatitisGramStainDetails() {
        return acuteViralHepatitisGramStainDetails;
    }

    public String getAcuteViralHepatitisLatexAgglutinationDetails() {
        return acuteViralHepatitisLatexAgglutinationDetails;
    }

    public String getAcuteViralHepatitisCqValueDetectionDetails() {
        return acuteViralHepatitisCqValueDetectionDetails;
    }

    public String getAcuteViralHepatitisSequencingDetails() {
        return acuteViralHepatitisSequencingDetails;
    }

    public String getAcuteViralHepatitisDnaMicroarrayDetails() {
        return acuteViralHepatitisDnaMicroarrayDetails;
    }

    public String getAcuteViralHepatitisOtherDetails() {
        return acuteViralHepatitisOtherDetails;
    }

    public String getNonNeonatalTetanusAntibodyDetection() {
        return nonNeonatalTetanusAntibodyDetection;
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

    public String getNonNeonatalTetanusDirectFluorescentAntibody() {
        return nonNeonatalTetanusDirectFluorescentAntibody;
    }

    public String getNonNeonatalTetanusMicroscopy() {
        return nonNeonatalTetanusMicroscopy;
    }

    public String getNonNeonatalTetanusNeutralizingAntibodies() {
        return nonNeonatalTetanusNeutralizingAntibodies;
    }

    public String getNonNeonatalTetanusPcrRtPcr() {
        return nonNeonatalTetanusPcrRtPcr;
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

    public String getNonNeonatalTetanusSequencing() {
        return nonNeonatalTetanusSequencing;
    }

    public String getNonNeonatalTetanusDnaMicroarray() {
        return nonNeonatalTetanusDnaMicroarray;
    }

    public String getNonNeonatalTetanusOther() {
        return nonNeonatalTetanusOther;
    }

    public String getNonNeonatalTetanusAntibodyDetectionDetails() {
        return nonNeonatalTetanusAntibodyDetectionDetails;
    }

    public String getNonNeonatalTetanusAntigenDetectionDetails() {
        return nonNeonatalTetanusAntigenDetectionDetails;
    }

    public String getNonNeonatalTetanusRapidTestDetails() {
        return nonNeonatalTetanusRapidTestDetails;
    }

    public String getNonNeonatalTetanusCultureDetails() {
        return nonNeonatalTetanusCultureDetails;
    }

    public String getNonNeonatalTetanusHistopathologyDetails() {
        return nonNeonatalTetanusHistopathologyDetails;
    }

    public String getNonNeonatalTetanusIsolationDetails() {
        return nonNeonatalTetanusIsolationDetails;
    }

    public String getNonNeonatalTetanusIgmSerumAntibodyDetails() {
        return nonNeonatalTetanusIgmSerumAntibodyDetails;
    }

    public String getNonNeonatalTetanusIggSerumAntibodyDetails() {
        return nonNeonatalTetanusIggSerumAntibodyDetails;
    }

    public String getNonNeonatalTetanusIgaSerumAntibodyDetails() {
        return nonNeonatalTetanusIgaSerumAntibodyDetails;
    }

    public String getNonNeonatalTetanusIncubationTimeDetails() {
        return nonNeonatalTetanusIncubationTimeDetails;
    }

    public String getNonNeonatalTetanusIndirectFluorescentAntibodyDetails() {
        return nonNeonatalTetanusIndirectFluorescentAntibodyDetails;
    }

    public String getNonNeonatalTetanusDirectFluorescentAntibodyDetails() {
        return nonNeonatalTetanusDirectFluorescentAntibodyDetails;
    }

    public String getNonNeonatalTetanusMicroscopyDetails() {
        return nonNeonatalTetanusMicroscopyDetails;
    }

    public String getNonNeonatalTetanusNeutralizingAntibodiesDetails() {
        return nonNeonatalTetanusNeutralizingAntibodiesDetails;
    }

    public String getNonNeonatalTetanusPcrRtPcrDetails() {
        return nonNeonatalTetanusPcrRtPcrDetails;
    }

    public String getNonNeonatalTetanusGramStainDetails() {
        return nonNeonatalTetanusGramStainDetails;
    }

    public String getNonNeonatalTetanusLatexAgglutinationDetails() {
        return nonNeonatalTetanusLatexAgglutinationDetails;
    }

    public String getNonNeonatalTetanusCqValueDetectionDetails() {
        return nonNeonatalTetanusCqValueDetectionDetails;
    }

    public String getNonNeonatalTetanusSequencingDetails() {
        return nonNeonatalTetanusSequencingDetails;
    }

    public String getNonNeonatalTetanusDnaMicroarrayDetails() {
        return nonNeonatalTetanusDnaMicroarrayDetails;
    }

    public String getNonNeonatalTetanusOtherDetails() {
        return nonNeonatalTetanusOtherDetails;
    }

    public String getHivAntibodyDetection() {
        return hivAntibodyDetection;
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

    public String getHivDirectFluorescentAntibody() {
        return hivDirectFluorescentAntibody;
    }

    public String getHivMicroscopy() {
        return hivMicroscopy;
    }

    public String getHivNeutralizingAntibodies() {
        return hivNeutralizingAntibodies;
    }

    public String getHivPcrRtPcr() {
        return hivPcrRtPcr;
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

    public String getHivSequencing() {
        return hivSequencing;
    }

    public String getHivDnaMicroarray() {
        return hivDnaMicroarray;
    }

    public String getHivOther() {
        return hivOther;
    }

    public String getHivAntibodyDetectionDetails() {
        return hivAntibodyDetectionDetails;
    }

    public String getHivAntigenDetectionDetails() {
        return hivAntigenDetectionDetails;
    }

    public String getHivRapidTestDetails() {
        return hivRapidTestDetails;
    }

    public String getHivCultureDetails() {
        return hivCultureDetails;
    }

    public String getHivHistopathologyDetails() {
        return hivHistopathologyDetails;
    }

    public String getHivIsolationDetails() {
        return hivIsolationDetails;
    }

    public String getHivIgmSerumAntibodyDetails() {
        return hivIgmSerumAntibodyDetails;
    }

    public String getHivIggSerumAntibodyDetails() {
        return hivIggSerumAntibodyDetails;
    }

    public String getHivIgaSerumAntibodyDetails() {
        return hivIgaSerumAntibodyDetails;
    }

    public String getHivIncubationTimeDetails() {
        return hivIncubationTimeDetails;
    }

    public String getHivIndirectFluorescentAntibodyDetails() {
        return hivIndirectFluorescentAntibodyDetails;
    }

    public String getHivDirectFluorescentAntibodyDetails() {
        return hivDirectFluorescentAntibodyDetails;
    }

    public String getHivMicroscopyDetails() {
        return hivMicroscopyDetails;
    }

    public String getHivNeutralizingAntibodiesDetails() {
        return hivNeutralizingAntibodiesDetails;
    }

    public String getHivPcrRtPcrDetails() {
        return hivPcrRtPcrDetails;
    }

    public String getHivGramStainDetails() {
        return hivGramStainDetails;
    }

    public String getHivLatexAgglutinationDetails() {
        return hivLatexAgglutinationDetails;
    }

    public String getHivCqValueDetectionDetails() {
        return hivCqValueDetectionDetails;
    }

    public String getHivSequencingDetails() {
        return hivSequencingDetails;
    }

    public String getHivDnaMicroarrayDetails() {
        return hivDnaMicroarrayDetails;
    }

    public String getHivOtherDetails() {
        return hivOtherDetails;
    }

    public String getSchistosomiasisAntibodyDetection() {
        return schistosomiasisAntibodyDetection;
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

    public String getSchistosomiasisDirectFluorescentAntibody() {
        return schistosomiasisDirectFluorescentAntibody;
    }

    public String getSchistosomiasisMicroscopy() {
        return schistosomiasisMicroscopy;
    }

    public String getSchistosomiasisNeutralizingAntibodies() {
        return schistosomiasisNeutralizingAntibodies;
    }

    public String getSchistosomiasisPcrRtPcr() {
        return schistosomiasisPcrRtPcr;
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

    public String getSchistosomiasisSequencing() {
        return schistosomiasisSequencing;
    }

    public String getSchistosomiasisDnaMicroarray() {
        return schistosomiasisDnaMicroarray;
    }

    public String getSchistosomiasisOther() {
        return schistosomiasisOther;
    }

    public String getSchistosomiasisAntibodyDetectionDetails() {
        return schistosomiasisAntibodyDetectionDetails;
    }

    public String getSchistosomiasisAntigenDetectionDetails() {
        return schistosomiasisAntigenDetectionDetails;
    }

    public String getSchistosomiasisRapidTestDetails() {
        return schistosomiasisRapidTestDetails;
    }

    public String getSchistosomiasisCultureDetails() {
        return schistosomiasisCultureDetails;
    }

    public String getSchistosomiasisHistopathologyDetails() {
        return schistosomiasisHistopathologyDetails;
    }

    public String getSchistosomiasisIsolationDetails() {
        return schistosomiasisIsolationDetails;
    }

    public String getSchistosomiasisIgmSerumAntibodyDetails() {
        return schistosomiasisIgmSerumAntibodyDetails;
    }

    public String getSchistosomiasisIggSerumAntibodyDetails() {
        return schistosomiasisIggSerumAntibodyDetails;
    }

    public String getSchistosomiasisIgaSerumAntibodyDetails() {
        return schistosomiasisIgaSerumAntibodyDetails;
    }

    public String getSchistosomiasisIncubationTimeDetails() {
        return schistosomiasisIncubationTimeDetails;
    }

    public String getSchistosomiasisIndirectFluorescentAntibodyDetails() {
        return schistosomiasisIndirectFluorescentAntibodyDetails;
    }

    public String getSchistosomiasisDirectFluorescentAntibodyDetails() {
        return schistosomiasisDirectFluorescentAntibodyDetails;
    }

    public String getSchistosomiasisMicroscopyDetails() {
        return schistosomiasisMicroscopyDetails;
    }

    public String getSchistosomiasisNeutralizingAntibodiesDetails() {
        return schistosomiasisNeutralizingAntibodiesDetails;
    }

    public String getSchistosomiasisPcrRtPcrDetails() {
        return schistosomiasisPcrRtPcrDetails;
    }

    public String getSchistosomiasisGramStainDetails() {
        return schistosomiasisGramStainDetails;
    }

    public String getSchistosomiasisLatexAgglutinationDetails() {
        return schistosomiasisLatexAgglutinationDetails;
    }

    public String getSchistosomiasisCqValueDetectionDetails() {
        return schistosomiasisCqValueDetectionDetails;
    }

    public String getSchistosomiasisSequencingDetails() {
        return schistosomiasisSequencingDetails;
    }

    public String getSchistosomiasisDnaMicroarrayDetails() {
        return schistosomiasisDnaMicroarrayDetails;
    }

    public String getSchistosomiasisOtherDetails() {
        return schistosomiasisOtherDetails;
    }

    public String getSoilTransmittedHelminthsAntibodyDetection() {
        return soilTransmittedHelminthsAntibodyDetection;
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

    public String getSoilTransmittedHelminthsDirectFluorescentAntibody() {
        return soilTransmittedHelminthsDirectFluorescentAntibody;
    }

    public String getSoilTransmittedHelminthsMicroscopy() {
        return soilTransmittedHelminthsMicroscopy;
    }

    public String getSoilTransmittedHelminthsNeutralizingAntibodies() {
        return soilTransmittedHelminthsNeutralizingAntibodies;
    }

    public String getSoilTransmittedHelminthsPcrRtPcr() {
        return soilTransmittedHelminthsPcrRtPcr;
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

    public String getSoilTransmittedHelminthsSequencing() {
        return soilTransmittedHelminthsSequencing;
    }

    public String getSoilTransmittedHelminthsDnaMicroarray() {
        return soilTransmittedHelminthsDnaMicroarray;
    }

    public String getSoilTransmittedHelminthsOther() {
        return soilTransmittedHelminthsOther;
    }

    public String getSoilTransmittedHelminthsAntibodyDetectionDetails() {
        return soilTransmittedHelminthsAntibodyDetectionDetails;
    }

    public String getSoilTransmittedHelminthsAntigenDetectionDetails() {
        return soilTransmittedHelminthsAntigenDetectionDetails;
    }

    public String getSoilTransmittedHelminthsRapidTestDetails() {
        return soilTransmittedHelminthsRapidTestDetails;
    }

    public String getSoilTransmittedHelminthsCultureDetails() {
        return soilTransmittedHelminthsCultureDetails;
    }

    public String getSoilTransmittedHelminthsHistopathologyDetails() {
        return soilTransmittedHelminthsHistopathologyDetails;
    }

    public String getSoilTransmittedHelminthsIsolationDetails() {
        return soilTransmittedHelminthsIsolationDetails;
    }

    public String getSoilTransmittedHelminthsIgmSerumAntibodyDetails() {
        return soilTransmittedHelminthsIgmSerumAntibodyDetails;
    }

    public String getSoilTransmittedHelminthsIggSerumAntibodyDetails() {
        return soilTransmittedHelminthsIggSerumAntibodyDetails;
    }

    public String getSoilTransmittedHelminthsIgaSerumAntibodyDetails() {
        return soilTransmittedHelminthsIgaSerumAntibodyDetails;
    }

    public String getSoilTransmittedHelminthsIncubationTimeDetails() {
        return soilTransmittedHelminthsIncubationTimeDetails;
    }

    public String getSoilTransmittedHelminthsIndirectFluorescentAntibodyDetails() {
        return soilTransmittedHelminthsIndirectFluorescentAntibodyDetails;
    }

    public String getSoilTransmittedHelminthsDirectFluorescentAntibodyDetails() {
        return soilTransmittedHelminthsDirectFluorescentAntibodyDetails;
    }

    public String getSoilTransmittedHelminthsMicroscopyDetails() {
        return soilTransmittedHelminthsMicroscopyDetails;
    }

    public String getSoilTransmittedHelminthsNeutralizingAntibodiesDetails() {
        return soilTransmittedHelminthsNeutralizingAntibodiesDetails;
    }

    public String getSoilTransmittedHelminthsPcrRtPcrDetails() {
        return soilTransmittedHelminthsPcrRtPcrDetails;
    }

    public String getSoilTransmittedHelminthsGramStainDetails() {
        return soilTransmittedHelminthsGramStainDetails;
    }

    public String getSoilTransmittedHelminthsLatexAgglutinationDetails() {
        return soilTransmittedHelminthsLatexAgglutinationDetails;
    }

    public String getSoilTransmittedHelminthsCqValueDetectionDetails() {
        return soilTransmittedHelminthsCqValueDetectionDetails;
    }

    public String getSoilTransmittedHelminthsSequencingDetails() {
        return soilTransmittedHelminthsSequencingDetails;
    }

    public String getSoilTransmittedHelminthsDnaMicroarrayDetails() {
        return soilTransmittedHelminthsDnaMicroarrayDetails;
    }

    public String getSoilTransmittedHelminthsOtherDetails() {
        return soilTransmittedHelminthsOtherDetails;
    }

    public String getTrypanosomiasisAntibodyDetection() {
        return trypanosomiasisAntibodyDetection;
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

    public String getTrypanosomiasisDirectFluorescentAntibody() {
        return trypanosomiasisDirectFluorescentAntibody;
    }

    public String getTrypanosomiasisMicroscopy() {
        return trypanosomiasisMicroscopy;
    }

    public String getTrypanosomiasisNeutralizingAntibodies() {
        return trypanosomiasisNeutralizingAntibodies;
    }

    public String getTrypanosomiasisPcrRtPcr() {
        return trypanosomiasisPcrRtPcr;
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

    public String getTrypanosomiasisSequencing() {
        return trypanosomiasisSequencing;
    }

    public String getTrypanosomiasisDnaMicroarray() {
        return trypanosomiasisDnaMicroarray;
    }

    public String getTrypanosomiasisOther() {
        return trypanosomiasisOther;
    }

    public String getTrypanosomiasisAntibodyDetectionDetails() {
        return trypanosomiasisAntibodyDetectionDetails;
    }

    public String getTrypanosomiasisAntigenDetectionDetails() {
        return trypanosomiasisAntigenDetectionDetails;
    }

    public String getTrypanosomiasisRapidTestDetails() {
        return trypanosomiasisRapidTestDetails;
    }

    public String getTrypanosomiasisCultureDetails() {
        return trypanosomiasisCultureDetails;
    }

    public String getTrypanosomiasisHistopathologyDetails() {
        return trypanosomiasisHistopathologyDetails;
    }

    public String getTrypanosomiasisIsolationDetails() {
        return trypanosomiasisIsolationDetails;
    }

    public String getTrypanosomiasisIgmSerumAntibodyDetails() {
        return trypanosomiasisIgmSerumAntibodyDetails;
    }

    public String getTrypanosomiasisIggSerumAntibodyDetails() {
        return trypanosomiasisIggSerumAntibodyDetails;
    }

    public String getTrypanosomiasisIgaSerumAntibodyDetails() {
        return trypanosomiasisIgaSerumAntibodyDetails;
    }

    public String getTrypanosomiasisIncubationTimeDetails() {
        return trypanosomiasisIncubationTimeDetails;
    }

    public String getTrypanosomiasisIndirectFluorescentAntibodyDetails() {
        return trypanosomiasisIndirectFluorescentAntibodyDetails;
    }

    public String getTrypanosomiasisDirectFluorescentAntibodyDetails() {
        return trypanosomiasisDirectFluorescentAntibodyDetails;
    }

    public String getTrypanosomiasisMicroscopyDetails() {
        return trypanosomiasisMicroscopyDetails;
    }

    public String getTrypanosomiasisNeutralizingAntibodiesDetails() {
        return trypanosomiasisNeutralizingAntibodiesDetails;
    }

    public String getTrypanosomiasisPcrRtPcrDetails() {
        return trypanosomiasisPcrRtPcrDetails;
    }

    public String getTrypanosomiasisGramStainDetails() {
        return trypanosomiasisGramStainDetails;
    }

    public String getTrypanosomiasisLatexAgglutinationDetails() {
        return trypanosomiasisLatexAgglutinationDetails;
    }

    public String getTrypanosomiasisCqValueDetectionDetails() {
        return trypanosomiasisCqValueDetectionDetails;
    }

    public String getTrypanosomiasisSequencingDetails() {
        return trypanosomiasisSequencingDetails;
    }

    public String getTrypanosomiasisDnaMicroarrayDetails() {
        return trypanosomiasisDnaMicroarrayDetails;
    }

    public String getTrypanosomiasisOtherDetails() {
        return trypanosomiasisOtherDetails;
    }

    public String getDiarrheaDehydrationAntibodyDetection() {
        return diarrheaDehydrationAntibodyDetection;
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

    public String getDiarrheaDehydrationDirectFluorescentAntibody() {
        return diarrheaDehydrationDirectFluorescentAntibody;
    }

    public String getDiarrheaDehydrationMicroscopy() {
        return diarrheaDehydrationMicroscopy;
    }

    public String getDiarrheaDehydrationNeutralizingAntibodies() {
        return diarrheaDehydrationNeutralizingAntibodies;
    }

    public String getDiarrheaDehydrationPcrRtPcr() {
        return diarrheaDehydrationPcrRtPcr;
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

    public String getDiarrheaDehydrationSequencing() {
        return diarrheaDehydrationSequencing;
    }

    public String getDiarrheaDehydrationDnaMicroarray() {
        return diarrheaDehydrationDnaMicroarray;
    }

    public String getDiarrheaDehydrationOther() {
        return diarrheaDehydrationOther;
    }

    public String getDiarrheaDehydrationAntibodyDetectionDetails() {
        return diarrheaDehydrationAntibodyDetectionDetails;
    }

    public String getDiarrheaDehydrationAntigenDetectionDetails() {
        return diarrheaDehydrationAntigenDetectionDetails;
    }

    public String getDiarrheaDehydrationRapidTestDetails() {
        return diarrheaDehydrationRapidTestDetails;
    }

    public String getDiarrheaDehydrationCultureDetails() {
        return diarrheaDehydrationCultureDetails;
    }

    public String getDiarrheaDehydrationHistopathologyDetails() {
        return diarrheaDehydrationHistopathologyDetails;
    }

    public String getDiarrheaDehydrationIsolationDetails() {
        return diarrheaDehydrationIsolationDetails;
    }

    public String getDiarrheaDehydrationIgmSerumAntibodyDetails() {
        return diarrheaDehydrationIgmSerumAntibodyDetails;
    }

    public String getDiarrheaDehydrationIggSerumAntibodyDetails() {
        return diarrheaDehydrationIggSerumAntibodyDetails;
    }

    public String getDiarrheaDehydrationIgaSerumAntibodyDetails() {
        return diarrheaDehydrationIgaSerumAntibodyDetails;
    }

    public String getDiarrheaDehydrationIncubationTimeDetails() {
        return diarrheaDehydrationIncubationTimeDetails;
    }

    public String getDiarrheaDehydrationIndirectFluorescentAntibodyDetails() {
        return diarrheaDehydrationIndirectFluorescentAntibodyDetails;
    }

    public String getDiarrheaDehydrationDirectFluorescentAntibodyDetails() {
        return diarrheaDehydrationDirectFluorescentAntibodyDetails;
    }

    public String getDiarrheaDehydrationMicroscopyDetails() {
        return diarrheaDehydrationMicroscopyDetails;
    }

    public String getDiarrheaDehydrationNeutralizingAntibodiesDetails() {
        return diarrheaDehydrationNeutralizingAntibodiesDetails;
    }

    public String getDiarrheaDehydrationPcrRtPcrDetails() {
        return diarrheaDehydrationPcrRtPcrDetails;
    }

    public String getDiarrheaDehydrationGramStainDetails() {
        return diarrheaDehydrationGramStainDetails;
    }

    public String getDiarrheaDehydrationLatexAgglutinationDetails() {
        return diarrheaDehydrationLatexAgglutinationDetails;
    }

    public String getDiarrheaDehydrationCqValueDetectionDetails() {
        return diarrheaDehydrationCqValueDetectionDetails;
    }

    public String getDiarrheaDehydrationSequencingDetails() {
        return diarrheaDehydrationSequencingDetails;
    }

    public String getDiarrheaDehydrationDnaMicroarrayDetails() {
        return diarrheaDehydrationDnaMicroarrayDetails;
    }

    public String getDiarrheaDehydrationOtherDetails() {
        return diarrheaDehydrationOtherDetails;
    }

    public String getDiarrheaBloodAntibodyDetection() {
        return diarrheaBloodAntibodyDetection;
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

    public String getDiarrheaBloodDirectFluorescentAntibody() {
        return diarrheaBloodDirectFluorescentAntibody;
    }

    public String getDiarrheaBloodMicroscopy() {
        return diarrheaBloodMicroscopy;
    }

    public String getDiarrheaBloodNeutralizingAntibodies() {
        return diarrheaBloodNeutralizingAntibodies;
    }

    public String getDiarrheaBloodPcrRtPcr() {
        return diarrheaBloodPcrRtPcr;
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

    public String getDiarrheaBloodSequencing() {
        return diarrheaBloodSequencing;
    }

    public String getDiarrheaBloodDnaMicroarray() {
        return diarrheaBloodDnaMicroarray;
    }

    public String getDiarrheaBloodOther() {
        return diarrheaBloodOther;
    }

    public String getDiarrheaBloodAntibodyDetectionDetails() {
        return diarrheaBloodAntibodyDetectionDetails;
    }

    public String getDiarrheaBloodAntigenDetectionDetails() {
        return diarrheaBloodAntigenDetectionDetails;
    }

    public String getDiarrheaBloodRapidTestDetails() {
        return diarrheaBloodRapidTestDetails;
    }

    public String getDiarrheaBloodCultureDetails() {
        return diarrheaBloodCultureDetails;
    }

    public String getDiarrheaBloodHistopathologyDetails() {
        return diarrheaBloodHistopathologyDetails;
    }

    public String getDiarrheaBloodIsolationDetails() {
        return diarrheaBloodIsolationDetails;
    }

    public String getDiarrheaBloodIgmSerumAntibodyDetails() {
        return diarrheaBloodIgmSerumAntibodyDetails;
    }

    public String getDiarrheaBloodIggSerumAntibodyDetails() {
        return diarrheaBloodIggSerumAntibodyDetails;
    }

    public String getDiarrheaBloodIgaSerumAntibodyDetails() {
        return diarrheaBloodIgaSerumAntibodyDetails;
    }

    public String getDiarrheaBloodIncubationTimeDetails() {
        return diarrheaBloodIncubationTimeDetails;
    }

    public String getDiarrheaBloodIndirectFluorescentAntibodyDetails() {
        return diarrheaBloodIndirectFluorescentAntibodyDetails;
    }

    public String getDiarrheaBloodDirectFluorescentAntibodyDetails() {
        return diarrheaBloodDirectFluorescentAntibodyDetails;
    }

    public String getDiarrheaBloodMicroscopyDetails() {
        return diarrheaBloodMicroscopyDetails;
    }

    public String getDiarrheaBloodNeutralizingAntibodiesDetails() {
        return diarrheaBloodNeutralizingAntibodiesDetails;
    }

    public String getDiarrheaBloodPcrRtPcrDetails() {
        return diarrheaBloodPcrRtPcrDetails;
    }

    public String getDiarrheaBloodGramStainDetails() {
        return diarrheaBloodGramStainDetails;
    }

    public String getDiarrheaBloodLatexAgglutinationDetails() {
        return diarrheaBloodLatexAgglutinationDetails;
    }

    public String getDiarrheaBloodCqValueDetectionDetails() {
        return diarrheaBloodCqValueDetectionDetails;
    }

    public String getDiarrheaBloodSequencingDetails() {
        return diarrheaBloodSequencingDetails;
    }

    public String getDiarrheaBloodDnaMicroarrayDetails() {
        return diarrheaBloodDnaMicroarrayDetails;
    }

    public String getDiarrheaBloodOtherDetails() {
        return diarrheaBloodOtherDetails;
    }

    public String getSnakeBiteAntibodyDetection() {
        return snakeBiteAntibodyDetection;
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

    public String getSnakeBiteDirectFluorescentAntibody() {
        return snakeBiteDirectFluorescentAntibody;
    }

    public String getSnakeBiteMicroscopy() {
        return snakeBiteMicroscopy;
    }

    public String getSnakeBiteNeutralizingAntibodies() {
        return snakeBiteNeutralizingAntibodies;
    }

    public String getSnakeBitePcrRtPcr() {
        return snakeBitePcrRtPcr;
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

    public String getSnakeBiteSequencing() {
        return snakeBiteSequencing;
    }

    public String getSnakeBiteDnaMicroarray() {
        return snakeBiteDnaMicroarray;
    }

    public String getSnakeBiteOther() {
        return snakeBiteOther;
    }

    public String getSnakeBiteAntibodyDetectionDetails() {
        return snakeBiteAntibodyDetectionDetails;
    }

    public String getSnakeBiteAntigenDetectionDetails() {
        return snakeBiteAntigenDetectionDetails;
    }

    public String getSnakeBiteRapidTestDetails() {
        return snakeBiteRapidTestDetails;
    }

    public String getSnakeBiteCultureDetails() {
        return snakeBiteCultureDetails;
    }

    public String getSnakeBiteHistopathologyDetails() {
        return snakeBiteHistopathologyDetails;
    }

    public String getSnakeBiteIsolationDetails() {
        return snakeBiteIsolationDetails;
    }

    public String getSnakeBiteIgmSerumAntibodyDetails() {
        return snakeBiteIgmSerumAntibodyDetails;
    }

    public String getSnakeBiteIggSerumAntibodyDetails() {
        return snakeBiteIggSerumAntibodyDetails;
    }

    public String getSnakeBiteIgaSerumAntibodyDetails() {
        return snakeBiteIgaSerumAntibodyDetails;
    }

    public String getSnakeBiteIncubationTimeDetails() {
        return snakeBiteIncubationTimeDetails;
    }

    public String getSnakeBiteIndirectFluorescentAntibodyDetails() {
        return snakeBiteIndirectFluorescentAntibodyDetails;
    }

    public String getSnakeBiteDirectFluorescentAntibodyDetails() {
        return snakeBiteDirectFluorescentAntibodyDetails;
    }

    public String getSnakeBiteMicroscopyDetails() {
        return snakeBiteMicroscopyDetails;
    }

    public String getSnakeBiteNeutralizingAntibodiesDetails() {
        return snakeBiteNeutralizingAntibodiesDetails;
    }

    public String getSnakeBitePcrRtPcrDetails() {
        return snakeBitePcrRtPcrDetails;
    }

    public String getSnakeBiteGramStainDetails() {
        return snakeBiteGramStainDetails;
    }

    public String getSnakeBiteLatexAgglutinationDetails() {
        return snakeBiteLatexAgglutinationDetails;
    }

    public String getSnakeBiteCqValueDetectionDetails() {
        return snakeBiteCqValueDetectionDetails;
    }

    public String getSnakeBiteSequencingDetails() {
        return snakeBiteSequencingDetails;
    }

    public String getSnakeBiteDnaMicroarrayDetails() {
        return snakeBiteDnaMicroarrayDetails;
    }

    public String getSnakeBiteOtherDetails() {
        return snakeBiteOtherDetails;
    }

    public String getRubellaAntibodyDetection() {
        return rubellaAntibodyDetection;
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

    public String getRubellaDirectFluorescentAntibody() {
        return rubellaDirectFluorescentAntibody;
    }

    public String getRubellaMicroscopy() {
        return rubellaMicroscopy;
    }

    public String getRubellaNeutralizingAntibodies() {
        return rubellaNeutralizingAntibodies;
    }

    public String getRubellaPcrRtPcr() {
        return rubellaPcrRtPcr;
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

    public String getRubellaSequencing() {
        return rubellaSequencing;
    }

    public String getRubellaDnaMicroarray() {
        return rubellaDnaMicroarray;
    }

    public String getRubellaOther() {
        return rubellaOther;
    }

    public String getRubellaAntibodyDetectionDetails() {
        return rubellaAntibodyDetectionDetails;
    }

    public String getRubellaAntigenDetectionDetails() {
        return rubellaAntigenDetectionDetails;
    }

    public String getRubellaRapidTestDetails() {
        return rubellaRapidTestDetails;
    }

    public String getRubellaCultureDetails() {
        return rubellaCultureDetails;
    }

    public String getRubellaHistopathologyDetails() {
        return rubellaHistopathologyDetails;
    }

    public String getRubellaIsolationDetails() {
        return rubellaIsolationDetails;
    }

    public String getRubellaIgmSerumAntibodyDetails() {
        return rubellaIgmSerumAntibodyDetails;
    }

    public String getRubellaIggSerumAntibodyDetails() {
        return rubellaIggSerumAntibodyDetails;
    }

    public String getRubellaIgaSerumAntibodyDetails() {
        return rubellaIgaSerumAntibodyDetails;
    }

    public String getRubellaIncubationTimeDetails() {
        return rubellaIncubationTimeDetails;
    }

    public String getRubellaIndirectFluorescentAntibodyDetails() {
        return rubellaIndirectFluorescentAntibodyDetails;
    }

    public String getRubellaDirectFluorescentAntibodyDetails() {
        return rubellaDirectFluorescentAntibodyDetails;
    }

    public String getRubellaMicroscopyDetails() {
        return rubellaMicroscopyDetails;
    }

    public String getRubellaNeutralizingAntibodiesDetails() {
        return rubellaNeutralizingAntibodiesDetails;
    }

    public String getRubellaPcrRtPcrDetails() {
        return rubellaPcrRtPcrDetails;
    }

    public String getRubellaGramStainDetails() {
        return rubellaGramStainDetails;
    }

    public String getRubellaLatexAgglutinationDetails() {
        return rubellaLatexAgglutinationDetails;
    }

    public String getRubellaCqValueDetectionDetails() {
        return rubellaCqValueDetectionDetails;
    }

    public String getRubellaSequencingDetails() {
        return rubellaSequencingDetails;
    }

    public String getRubellaDnaMicroarrayDetails() {
        return rubellaDnaMicroarrayDetails;
    }

    public String getRubellaOtherDetails() {
        return rubellaOtherDetails;
    }

    public String getTuberculosisAntibodyDetection() {
        return tuberculosisAntibodyDetection;
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

    public String getTuberculosisDirectFluorescentAntibody() {
        return tuberculosisDirectFluorescentAntibody;
    }

    public String getTuberculosisMicroscopy() {
        return tuberculosisMicroscopy;
    }

    public String getTuberculosisNeutralizingAntibodies() {
        return tuberculosisNeutralizingAntibodies;
    }

    public String getTuberculosisPcrRtPcr() {
        return tuberculosisPcrRtPcr;
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

    public String getTuberculosisSequencing() {
        return tuberculosisSequencing;
    }

    public String getTuberculosisDnaMicroarray() {
        return tuberculosisDnaMicroarray;
    }

    public String getTuberculosisOther() {
        return tuberculosisOther;
    }

    public String getTuberculosisAntibodyDetectionDetails() {
        return tuberculosisAntibodyDetectionDetails;
    }

    public String getTuberculosisAntigenDetectionDetails() {
        return tuberculosisAntigenDetectionDetails;
    }

    public String getTuberculosisRapidTestDetails() {
        return tuberculosisRapidTestDetails;
    }

    public String getTuberculosisCultureDetails() {
        return tuberculosisCultureDetails;
    }

    public String getTuberculosisHistopathologyDetails() {
        return tuberculosisHistopathologyDetails;
    }

    public String getTuberculosisIsolationDetails() {
        return tuberculosisIsolationDetails;
    }

    public String getTuberculosisIgmSerumAntibodyDetails() {
        return tuberculosisIgmSerumAntibodyDetails;
    }

    public String getTuberculosisIggSerumAntibodyDetails() {
        return tuberculosisIggSerumAntibodyDetails;
    }

    public String getTuberculosisIgaSerumAntibodyDetails() {
        return tuberculosisIgaSerumAntibodyDetails;
    }

    public String getTuberculosisIncubationTimeDetails() {
        return tuberculosisIncubationTimeDetails;
    }

    public String getTuberculosisIndirectFluorescentAntibodyDetails() {
        return tuberculosisIndirectFluorescentAntibodyDetails;
    }

    public String getTuberculosisDirectFluorescentAntibodyDetails() {
        return tuberculosisDirectFluorescentAntibodyDetails;
    }

    public String getTuberculosisMicroscopyDetails() {
        return tuberculosisMicroscopyDetails;
    }

    public String getTuberculosisNeutralizingAntibodiesDetails() {
        return tuberculosisNeutralizingAntibodiesDetails;
    }

    public String getTuberculosisPcrRtPcrDetails() {
        return tuberculosisPcrRtPcrDetails;
    }

    public String getTuberculosisGramStainDetails() {
        return tuberculosisGramStainDetails;
    }

    public String getTuberculosisLatexAgglutinationDetails() {
        return tuberculosisLatexAgglutinationDetails;
    }

    public String getTuberculosisCqValueDetectionDetails() {
        return tuberculosisCqValueDetectionDetails;
    }

    public String getTuberculosisSequencingDetails() {
        return tuberculosisSequencingDetails;
    }

    public String getTuberculosisDnaMicroarrayDetails() {
        return tuberculosisDnaMicroarrayDetails;
    }

    public String getTuberculosisOtherDetails() {
        return tuberculosisOtherDetails;
    }

    public String getLeprosyAntibodyDetection() {
        return leprosyAntibodyDetection;
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

    public String getLeprosyDirectFluorescentAntibody() {
        return leprosyDirectFluorescentAntibody;
    }

    public String getLeprosyMicroscopy() {
        return leprosyMicroscopy;
    }

    public String getLeprosyNeutralizingAntibodies() {
        return leprosyNeutralizingAntibodies;
    }

    public String getLeprosyPcrRtPcr() {
        return leprosyPcrRtPcr;
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

    public String getLeprosySequencing() {
        return leprosySequencing;
    }

    public String getLeprosyDnaMicroarray() {
        return leprosyDnaMicroarray;
    }

    public String getLeprosyOther() {
        return leprosyOther;
    }

    public String getLeprosyAntibodyDetectionDetails() {
        return leprosyAntibodyDetectionDetails;
    }

    public String getLeprosyAntigenDetectionDetails() {
        return leprosyAntigenDetectionDetails;
    }

    public String getLeprosyRapidTestDetails() {
        return leprosyRapidTestDetails;
    }

    public String getLeprosyCultureDetails() {
        return leprosyCultureDetails;
    }

    public String getLeprosyHistopathologyDetails() {
        return leprosyHistopathologyDetails;
    }

    public String getLeprosyIsolationDetails() {
        return leprosyIsolationDetails;
    }

    public String getLeprosyIgmSerumAntibodyDetails() {
        return leprosyIgmSerumAntibodyDetails;
    }

    public String getLeprosyIggSerumAntibodyDetails() {
        return leprosyIggSerumAntibodyDetails;
    }

    public String getLeprosyIgaSerumAntibodyDetails() {
        return leprosyIgaSerumAntibodyDetails;
    }

    public String getLeprosyIncubationTimeDetails() {
        return leprosyIncubationTimeDetails;
    }

    public String getLeprosyIndirectFluorescentAntibodyDetails() {
        return leprosyIndirectFluorescentAntibodyDetails;
    }

    public String getLeprosyDirectFluorescentAntibodyDetails() {
        return leprosyDirectFluorescentAntibodyDetails;
    }

    public String getLeprosyMicroscopyDetails() {
        return leprosyMicroscopyDetails;
    }

    public String getLeprosyNeutralizingAntibodiesDetails() {
        return leprosyNeutralizingAntibodiesDetails;
    }

    public String getLeprosyPcrRtPcrDetails() {
        return leprosyPcrRtPcrDetails;
    }

    public String getLeprosyGramStainDetails() {
        return leprosyGramStainDetails;
    }

    public String getLeprosyLatexAgglutinationDetails() {
        return leprosyLatexAgglutinationDetails;
    }

    public String getLeprosyCqValueDetectionDetails() {
        return leprosyCqValueDetectionDetails;
    }

    public String getLeprosySequencingDetails() {
        return leprosySequencingDetails;
    }

    public String getLeprosyDnaMicroarrayDetails() {
        return leprosyDnaMicroarrayDetails;
    }

    public String getLeprosyOtherDetails() {
        return leprosyOtherDetails;
    }

    public String getLymphaticFilariasisAntibodyDetection() {
        return lymphaticFilariasisAntibodyDetection;
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

    public String getLymphaticFilariasisDirectFluorescentAntibody() {
        return lymphaticFilariasisDirectFluorescentAntibody;
    }

    public String getLymphaticFilariasisMicroscopy() {
        return lymphaticFilariasisMicroscopy;
    }

    public String getLymphaticFilariasisNeutralizingAntibodies() {
        return lymphaticFilariasisNeutralizingAntibodies;
    }

    public String getLymphaticFilariasisPcrRtPcr() {
        return lymphaticFilariasisPcrRtPcr;
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

    public String getLymphaticFilariasisSequencing() {
        return lymphaticFilariasisSequencing;
    }

    public String getLymphaticFilariasisDnaMicroarray() {
        return lymphaticFilariasisDnaMicroarray;
    }

    public String getLymphaticFilariasisOther() {
        return lymphaticFilariasisOther;
    }

    public String getLymphaticFilariasisAntibodyDetectionDetails() {
        return lymphaticFilariasisAntibodyDetectionDetails;
    }

    public String getLymphaticFilariasisAntigenDetectionDetails() {
        return lymphaticFilariasisAntigenDetectionDetails;
    }

    public String getLymphaticFilariasisRapidTestDetails() {
        return lymphaticFilariasisRapidTestDetails;
    }

    public String getLymphaticFilariasisCultureDetails() {
        return lymphaticFilariasisCultureDetails;
    }

    public String getLymphaticFilariasisHistopathologyDetails() {
        return lymphaticFilariasisHistopathologyDetails;
    }

    public String getLymphaticFilariasisIsolationDetails() {
        return lymphaticFilariasisIsolationDetails;
    }

    public String getLymphaticFilariasisIgmSerumAntibodyDetails() {
        return lymphaticFilariasisIgmSerumAntibodyDetails;
    }

    public String getLymphaticFilariasisIggSerumAntibodyDetails() {
        return lymphaticFilariasisIggSerumAntibodyDetails;
    }

    public String getLymphaticFilariasisIgaSerumAntibodyDetails() {
        return lymphaticFilariasisIgaSerumAntibodyDetails;
    }

    public String getLymphaticFilariasisIncubationTimeDetails() {
        return lymphaticFilariasisIncubationTimeDetails;
    }

    public String getLymphaticFilariasisIndirectFluorescentAntibodyDetails() {
        return lymphaticFilariasisIndirectFluorescentAntibodyDetails;
    }

    public String getLymphaticFilariasisDirectFluorescentAntibodyDetails() {
        return lymphaticFilariasisDirectFluorescentAntibodyDetails;
    }

    public String getLymphaticFilariasisMicroscopyDetails() {
        return lymphaticFilariasisMicroscopyDetails;
    }

    public String getLymphaticFilariasisNeutralizingAntibodiesDetails() {
        return lymphaticFilariasisNeutralizingAntibodiesDetails;
    }

    public String getLymphaticFilariasisPcrRtPcrDetails() {
        return lymphaticFilariasisPcrRtPcrDetails;
    }

    public String getLymphaticFilariasisGramStainDetails() {
        return lymphaticFilariasisGramStainDetails;
    }

    public String getLymphaticFilariasisLatexAgglutinationDetails() {
        return lymphaticFilariasisLatexAgglutinationDetails;
    }

    public String getLymphaticFilariasisCqValueDetectionDetails() {
        return lymphaticFilariasisCqValueDetectionDetails;
    }

    public String getLymphaticFilariasisSequencingDetails() {
        return lymphaticFilariasisSequencingDetails;
    }

    public String getLymphaticFilariasisDnaMicroarrayDetails() {
        return lymphaticFilariasisDnaMicroarrayDetails;
    }

    public String getLymphaticFilariasisOtherDetails() {
        return lymphaticFilariasisOtherDetails;
    }

    public String getBuruliUlcerAntibodyDetection() {
        return buruliUlcerAntibodyDetection;
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

    public String getBuruliUlcerDirectFluorescentAntibody() {
        return buruliUlcerDirectFluorescentAntibody;
    }

    public String getBuruliUlcerMicroscopy() {
        return buruliUlcerMicroscopy;
    }

    public String getBuruliUlcerNeutralizingAntibodies() {
        return buruliUlcerNeutralizingAntibodies;
    }

    public String getBuruliUlcerPcrRtPcr() {
        return buruliUlcerPcrRtPcr;
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

    public String getBuruliUlcerSequencing() {
        return buruliUlcerSequencing;
    }

    public String getBuruliUlcerDnaMicroarray() {
        return buruliUlcerDnaMicroarray;
    }

    public String getBuruliUlcerOther() {
        return buruliUlcerOther;
    }

    public String getBuruliUlcerAntibodyDetectionDetails() {
        return buruliUlcerAntibodyDetectionDetails;
    }

    public String getBuruliUlcerAntigenDetectionDetails() {
        return buruliUlcerAntigenDetectionDetails;
    }

    public String getBuruliUlcerRapidTestDetails() {
        return buruliUlcerRapidTestDetails;
    }

    public String getBuruliUlcerCultureDetails() {
        return buruliUlcerCultureDetails;
    }

    public String getBuruliUlcerHistopathologyDetails() {
        return buruliUlcerHistopathologyDetails;
    }

    public String getBuruliUlcerIsolationDetails() {
        return buruliUlcerIsolationDetails;
    }

    public String getBuruliUlcerIgmSerumAntibodyDetails() {
        return buruliUlcerIgmSerumAntibodyDetails;
    }

    public String getBuruliUlcerIggSerumAntibodyDetails() {
        return buruliUlcerIggSerumAntibodyDetails;
    }

    public String getBuruliUlcerIgaSerumAntibodyDetails() {
        return buruliUlcerIgaSerumAntibodyDetails;
    }

    public String getBuruliUlcerIncubationTimeDetails() {
        return buruliUlcerIncubationTimeDetails;
    }

    public String getBuruliUlcerIndirectFluorescentAntibodyDetails() {
        return buruliUlcerIndirectFluorescentAntibodyDetails;
    }

    public String getBuruliUlcerDirectFluorescentAntibodyDetails() {
        return buruliUlcerDirectFluorescentAntibodyDetails;
    }

    public String getBuruliUlcerMicroscopyDetails() {
        return buruliUlcerMicroscopyDetails;
    }

    public String getBuruliUlcerNeutralizingAntibodiesDetails() {
        return buruliUlcerNeutralizingAntibodiesDetails;
    }

    public String getBuruliUlcerPcrRtPcrDetails() {
        return buruliUlcerPcrRtPcrDetails;
    }

    public String getBuruliUlcerGramStainDetails() {
        return buruliUlcerGramStainDetails;
    }

    public String getBuruliUlcerLatexAgglutinationDetails() {
        return buruliUlcerLatexAgglutinationDetails;
    }

    public String getBuruliUlcerCqValueDetectionDetails() {
        return buruliUlcerCqValueDetectionDetails;
    }

    public String getBuruliUlcerSequencingDetails() {
        return buruliUlcerSequencingDetails;
    }

    public String getBuruliUlcerDnaMicroarrayDetails() {
        return buruliUlcerDnaMicroarrayDetails;
    }

    public String getBuruliUlcerOtherDetails() {
        return buruliUlcerOtherDetails;
    }

    public String getPertussisAntibodyDetection() {
        return pertussisAntibodyDetection;
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

    public String getPertussisDirectFluorescentAntibody() {
        return pertussisDirectFluorescentAntibody;
    }

    public String getPertussisMicroscopy() {
        return pertussisMicroscopy;
    }

    public String getPertussisNeutralizingAntibodies() {
        return pertussisNeutralizingAntibodies;
    }

    public String getPertussisPcrRtPcr() {
        return pertussisPcrRtPcr;
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

    public String getPertussisSequencing() {
        return pertussisSequencing;
    }

    public String getPertussisDnaMicroarray() {
        return pertussisDnaMicroarray;
    }

    public String getPertussisOther() {
        return pertussisOther;
    }

    public String getPertussisAntibodyDetectionDetails() {
        return pertussisAntibodyDetectionDetails;
    }

    public String getPertussisAntigenDetectionDetails() {
        return pertussisAntigenDetectionDetails;
    }

    public String getPertussisRapidTestDetails() {
        return pertussisRapidTestDetails;
    }

    public String getPertussisCultureDetails() {
        return pertussisCultureDetails;
    }

    public String getPertussisHistopathologyDetails() {
        return pertussisHistopathologyDetails;
    }

    public String getPertussisIsolationDetails() {
        return pertussisIsolationDetails;
    }

    public String getPertussisIgmSerumAntibodyDetails() {
        return pertussisIgmSerumAntibodyDetails;
    }

    public String getPertussisIggSerumAntibodyDetails() {
        return pertussisIggSerumAntibodyDetails;
    }

    public String getPertussisIgaSerumAntibodyDetails() {
        return pertussisIgaSerumAntibodyDetails;
    }

    public String getPertussisIncubationTimeDetails() {
        return pertussisIncubationTimeDetails;
    }

    public String getPertussisIndirectFluorescentAntibodyDetails() {
        return pertussisIndirectFluorescentAntibodyDetails;
    }

    public String getPertussisDirectFluorescentAntibodyDetails() {
        return pertussisDirectFluorescentAntibodyDetails;
    }

    public String getPertussisMicroscopyDetails() {
        return pertussisMicroscopyDetails;
    }

    public String getPertussisNeutralizingAntibodiesDetails() {
        return pertussisNeutralizingAntibodiesDetails;
    }

    public String getPertussisPcrRtPcrDetails() {
        return pertussisPcrRtPcrDetails;
    }

    public String getPertussisGramStainDetails() {
        return pertussisGramStainDetails;
    }

    public String getPertussisLatexAgglutinationDetails() {
        return pertussisLatexAgglutinationDetails;
    }

    public String getPertussisCqValueDetectionDetails() {
        return pertussisCqValueDetectionDetails;
    }

    public String getPertussisSequencingDetails() {
        return pertussisSequencingDetails;
    }

    public String getPertussisDnaMicroarrayDetails() {
        return pertussisDnaMicroarrayDetails;
    }

    public String getPertussisOtherDetails() {
        return pertussisOtherDetails;
    }

    public String getNeonatalTetanusAntibodyDetection() {
        return neonatalTetanusAntibodyDetection;
    }

    public String getNeonatalTetanusAntigenDetection() {
        return neonatalTetanusAntigenDetection;
    }

    public String getNeonatalTetanusRapidTest() {
        return neonatalTetanusRapidTest;
    }

    public String getNeonatalTetanusCulture() {
        return neonatalTetanusCulture;
    }

    public String getNeonatalTetanusHistopathology() {
        return neonatalTetanusHistopathology;
    }

    public String getNeonatalTetanusIsolation() {
        return neonatalTetanusIsolation;
    }

    public String getNeonatalTetanusIgmSerumAntibody() {
        return neonatalTetanusIgmSerumAntibody;
    }

    public String getNeonatalTetanusIggSerumAntibody() {
        return neonatalTetanusIggSerumAntibody;
    }

    public String getNeonatalTetanusIgaSerumAntibody() {
        return neonatalTetanusIgaSerumAntibody;
    }

    public String getNeonatalTetanusIncubationTime() {
        return neonatalTetanusIncubationTime;
    }

    public String getNeonatalTetanusIndirectFluorescentAntibody() {
        return neonatalTetanusIndirectFluorescentAntibody;
    }

    public String getNeonatalTetanusDirectFluorescentAntibody() {
        return neonatalTetanusDirectFluorescentAntibody;
    }

    public String getNeonatalTetanusMicroscopy() {
        return neonatalTetanusMicroscopy;
    }

    public String getNeonatalTetanusNeutralizingAntibodies() {
        return neonatalTetanusNeutralizingAntibodies;
    }

    public String getNeonatalTetanusPcrRtPcr() {
        return neonatalTetanusPcrRtPcr;
    }

    public String getNeonatalTetanusGramStain() {
        return neonatalTetanusGramStain;
    }

    public String getNeonatalTetanusLatexAgglutination() {
        return neonatalTetanusLatexAgglutination;
    }

    public String getNeonatalTetanusCqValueDetection() {
        return neonatalTetanusCqValueDetection;
    }

    public String getNeonatalTetanusSequencing() {
        return neonatalTetanusSequencing;
    }

    public String getNeonatalTetanusDnaMicroarray() {
        return neonatalTetanusDnaMicroarray;
    }

    public String getNeonatalTetanusOther() {
        return neonatalTetanusOther;
    }

    public String getNeonatalTetanusAntibodyDetectionDetails() {
        return neonatalTetanusAntibodyDetectionDetails;
    }

    public String getNeonatalTetanusAntigenDetectionDetails() {
        return neonatalTetanusAntigenDetectionDetails;
    }

    public String getNeonatalTetanusRapidTestDetails() {
        return neonatalTetanusRapidTestDetails;
    }

    public String getNeonatalTetanusCultureDetails() {
        return neonatalTetanusCultureDetails;
    }

    public String getNeonatalTetanusHistopathologyDetails() {
        return neonatalTetanusHistopathologyDetails;
    }

    public String getNeonatalTetanusIsolationDetails() {
        return neonatalTetanusIsolationDetails;
    }

    public String getNeonatalTetanusIgmSerumAntibodyDetails() {
        return neonatalTetanusIgmSerumAntibodyDetails;
    }

    public String getNeonatalTetanusIggSerumAntibodyDetails() {
        return neonatalTetanusIggSerumAntibodyDetails;
    }

    public String getNeonatalTetanusIgaSerumAntibodyDetails() {
        return neonatalTetanusIgaSerumAntibodyDetails;
    }

    public String getNeonatalTetanusIncubationTimeDetails() {
        return neonatalTetanusIncubationTimeDetails;
    }

    public String getNeonatalTetanusIndirectFluorescentAntibodyDetails() {
        return neonatalTetanusIndirectFluorescentAntibodyDetails;
    }

    public String getNeonatalTetanusDirectFluorescentAntibodyDetails() {
        return neonatalTetanusDirectFluorescentAntibodyDetails;
    }

    public String getNeonatalTetanusMicroscopyDetails() {
        return neonatalTetanusMicroscopyDetails;
    }

    public String getNeonatalTetanusNeutralizingAntibodiesDetails() {
        return neonatalTetanusNeutralizingAntibodiesDetails;
    }

    public String getNeonatalTetanusPcrRtPcrDetails() {
        return neonatalTetanusPcrRtPcrDetails;
    }

    public String getNeonatalTetanusGramStainDetails() {
        return neonatalTetanusGramStainDetails;
    }

    public String getNeonatalTetanusLatexAgglutinationDetails() {
        return neonatalTetanusLatexAgglutinationDetails;
    }

    public String getNeonatalTetanusCqValueDetectionDetails() {
        return neonatalTetanusCqValueDetectionDetails;
    }

    public String getNeonatalTetanusSequencingDetails() {
        return neonatalTetanusSequencingDetails;
    }

    public String getNeonatalTetanusDnaMicroarrayDetails() {
        return neonatalTetanusDnaMicroarrayDetails;
    }

    public String getNeonatalTetanusOtherDetails() {
        return neonatalTetanusOtherDetails;
    }

    public String getOnchocerciasisAntibodyDetection() {
        return onchocerciasisAntibodyDetection;
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

    public String getOnchocerciasisDirectFluorescentAntibody() {
        return onchocerciasisDirectFluorescentAntibody;
    }

    public String getOnchocerciasisMicroscopy() {
        return onchocerciasisMicroscopy;
    }

    public String getOnchocerciasisNeutralizingAntibodies() {
        return onchocerciasisNeutralizingAntibodies;
    }

    public String getOnchocerciasisPcrRtPcr() {
        return onchocerciasisPcrRtPcr;
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

    public String getOnchocerciasisSequencing() {
        return onchocerciasisSequencing;
    }

    public String getOnchocerciasisDnaMicroarray() {
        return onchocerciasisDnaMicroarray;
    }

    public String getOnchocerciasisOther() {
        return onchocerciasisOther;
    }

    public String getOnchocerciasisAntibodyDetectionDetails() {
        return onchocerciasisAntibodyDetectionDetails;
    }

    public String getOnchocerciasisAntigenDetectionDetails() {
        return onchocerciasisAntigenDetectionDetails;
    }

    public String getOnchocerciasisRapidTestDetails() {
        return onchocerciasisRapidTestDetails;
    }

    public String getOnchocerciasisCultureDetails() {
        return onchocerciasisCultureDetails;
    }

    public String getOnchocerciasisHistopathologyDetails() {
        return onchocerciasisHistopathologyDetails;
    }

    public String getOnchocerciasisIsolationDetails() {
        return onchocerciasisIsolationDetails;
    }

    public String getOnchocerciasisIgmSerumAntibodyDetails() {
        return onchocerciasisIgmSerumAntibodyDetails;
    }

    public String getOnchocerciasisIggSerumAntibodyDetails() {
        return onchocerciasisIggSerumAntibodyDetails;
    }

    public String getOnchocerciasisIgaSerumAntibodyDetails() {
        return onchocerciasisIgaSerumAntibodyDetails;
    }

    public String getOnchocerciasisIncubationTimeDetails() {
        return onchocerciasisIncubationTimeDetails;
    }

    public String getOnchocerciasisIndirectFluorescentAntibodyDetails() {
        return onchocerciasisIndirectFluorescentAntibodyDetails;
    }

    public String getOnchocerciasisDirectFluorescentAntibodyDetails() {
        return onchocerciasisDirectFluorescentAntibodyDetails;
    }

    public String getOnchocerciasisMicroscopyDetails() {
        return onchocerciasisMicroscopyDetails;
    }

    public String getOnchocerciasisNeutralizingAntibodiesDetails() {
        return onchocerciasisNeutralizingAntibodiesDetails;
    }

    public String getOnchocerciasisPcrRtPcrDetails() {
        return onchocerciasisPcrRtPcrDetails;
    }

    public String getOnchocerciasisGramStainDetails() {
        return onchocerciasisGramStainDetails;
    }

    public String getOnchocerciasisLatexAgglutinationDetails() {
        return onchocerciasisLatexAgglutinationDetails;
    }

    public String getOnchocerciasisCqValueDetectionDetails() {
        return onchocerciasisCqValueDetectionDetails;
    }

    public String getOnchocerciasisSequencingDetails() {
        return onchocerciasisSequencingDetails;
    }

    public String getOnchocerciasisDnaMicroarrayDetails() {
        return onchocerciasisDnaMicroarrayDetails;
    }

    public String getOnchocerciasisOtherDetails() {
        return onchocerciasisOtherDetails;
    }

    public String getDiphteriaAntibodyDetection() {
        return diphteriaAntibodyDetection;
    }

    public String getDiphteriaAntigenDetection() {
        return diphteriaAntigenDetection;
    }

    public String getDiphteriaRapidTest() {
        return diphteriaRapidTest;
    }

    public String getDiphteriaCulture() {
        return diphteriaCulture;
    }

    public String getDiphteriaHistopathology() {
        return diphteriaHistopathology;
    }

    public String getDiphteriaIsolation() {
        return diphteriaIsolation;
    }

    public String getDiphteriaIgmSerumAntibody() {
        return diphteriaIgmSerumAntibody;
    }

    public String getDiphteriaIggSerumAntibody() {
        return diphteriaIggSerumAntibody;
    }

    public String getDiphteriaIgaSerumAntibody() {
        return diphteriaIgaSerumAntibody;
    }

    public String getDiphteriaIncubationTime() {
        return diphteriaIncubationTime;
    }

    public String getDiphteriaIndirectFluorescentAntibody() {
        return diphteriaIndirectFluorescentAntibody;
    }

    public String getDiphteriaDirectFluorescentAntibody() {
        return diphteriaDirectFluorescentAntibody;
    }

    public String getDiphteriaMicroscopy() {
        return diphteriaMicroscopy;
    }

    public String getDiphteriaNeutralizingAntibodies() {
        return diphteriaNeutralizingAntibodies;
    }

    public String getDiphteriaPcrRtPcr() {
        return diphteriaPcrRtPcr;
    }

    public String getDiphteriaGramStain() {
        return diphteriaGramStain;
    }

    public String getDiphteriaLatexAgglutination() {
        return diphteriaLatexAgglutination;
    }

    public String getDiphteriaCqValueDetection() {
        return diphteriaCqValueDetection;
    }

    public String getDiphteriaSequencing() {
        return diphteriaSequencing;
    }

    public String getDiphteriaDnaMicroarray() {
        return diphteriaDnaMicroarray;
    }

    public String getDiphteriaOther() {
        return diphteriaOther;
    }

    public String getDiphteriaAntibodyDetectionDetails() {
        return diphteriaAntibodyDetectionDetails;
    }

    public String getDiphteriaAntigenDetectionDetails() {
        return diphteriaAntigenDetectionDetails;
    }

    public String getDiphteriaRapidTestDetails() {
        return diphteriaRapidTestDetails;
    }

    public String getDiphteriaCultureDetails() {
        return diphteriaCultureDetails;
    }

    public String getDiphteriaHistopathologyDetails() {
        return diphteriaHistopathologyDetails;
    }

    public String getDiphteriaIsolationDetails() {
        return diphteriaIsolationDetails;
    }

    public String getDiphteriaIgmSerumAntibodyDetails() {
        return diphteriaIgmSerumAntibodyDetails;
    }

    public String getDiphteriaIggSerumAntibodyDetails() {
        return diphteriaIggSerumAntibodyDetails;
    }

    public String getDiphteriaIgaSerumAntibodyDetails() {
        return diphteriaIgaSerumAntibodyDetails;
    }

    public String getDiphteriaIncubationTimeDetails() {
        return diphteriaIncubationTimeDetails;
    }

    public String getDiphteriaIndirectFluorescentAntibodyDetails() {
        return diphteriaIndirectFluorescentAntibodyDetails;
    }

    public String getDiphteriaDirectFluorescentAntibodyDetails() {
        return diphteriaDirectFluorescentAntibodyDetails;
    }

    public String getDiphteriaMicroscopyDetails() {
        return diphteriaMicroscopyDetails;
    }

    public String getDiphteriaNeutralizingAntibodiesDetails() {
        return diphteriaNeutralizingAntibodiesDetails;
    }

    public String getDiphteriaPcrRtPcrDetails() {
        return diphteriaPcrRtPcrDetails;
    }

    public String getDiphteriaGramStainDetails() {
        return diphteriaGramStainDetails;
    }

    public String getDiphteriaLatexAgglutinationDetails() {
        return diphteriaLatexAgglutinationDetails;
    }

    public String getDiphteriaCqValueDetectionDetails() {
        return diphteriaCqValueDetectionDetails;
    }

    public String getDiphteriaSequencingDetails() {
        return diphteriaSequencingDetails;
    }

    public String getDiphteriaDnaMicroarrayDetails() {
        return diphteriaDnaMicroarrayDetails;
    }

    public String getDiphteriaOtherDetails() {
        return diphteriaOtherDetails;
    }

    public String getTrachomaAntibodyDetection() {
        return trachomaAntibodyDetection;
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

    public String getTrachomaDirectFluorescentAntibody() {
        return trachomaDirectFluorescentAntibody;
    }

    public String getTrachomaMicroscopy() {
        return trachomaMicroscopy;
    }

    public String getTrachomaNeutralizingAntibodies() {
        return trachomaNeutralizingAntibodies;
    }

    public String getTrachomaPcrRtPcr() {
        return trachomaPcrRtPcr;
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

    public String getTrachomaSequencing() {
        return trachomaSequencing;
    }

    public String getTrachomaDnaMicroarray() {
        return trachomaDnaMicroarray;
    }

    public String getTrachomaOther() {
        return trachomaOther;
    }

    public String getTrachomaAntibodyDetectionDetails() {
        return trachomaAntibodyDetectionDetails;
    }

    public String getTrachomaAntigenDetectionDetails() {
        return trachomaAntigenDetectionDetails;
    }

    public String getTrachomaRapidTestDetails() {
        return trachomaRapidTestDetails;
    }

    public String getTrachomaCultureDetails() {
        return trachomaCultureDetails;
    }

    public String getTrachomaHistopathologyDetails() {
        return trachomaHistopathologyDetails;
    }

    public String getTrachomaIsolationDetails() {
        return trachomaIsolationDetails;
    }

    public String getTrachomaIgmSerumAntibodyDetails() {
        return trachomaIgmSerumAntibodyDetails;
    }

    public String getTrachomaIggSerumAntibodyDetails() {
        return trachomaIggSerumAntibodyDetails;
    }

    public String getTrachomaIgaSerumAntibodyDetails() {
        return trachomaIgaSerumAntibodyDetails;
    }

    public String getTrachomaIncubationTimeDetails() {
        return trachomaIncubationTimeDetails;
    }

    public String getTrachomaIndirectFluorescentAntibodyDetails() {
        return trachomaIndirectFluorescentAntibodyDetails;
    }

    public String getTrachomaDirectFluorescentAntibodyDetails() {
        return trachomaDirectFluorescentAntibodyDetails;
    }

    public String getTrachomaMicroscopyDetails() {
        return trachomaMicroscopyDetails;
    }

    public String getTrachomaNeutralizingAntibodiesDetails() {
        return trachomaNeutralizingAntibodiesDetails;
    }

    public String getTrachomaPcrRtPcrDetails() {
        return trachomaPcrRtPcrDetails;
    }

    public String getTrachomaGramStainDetails() {
        return trachomaGramStainDetails;
    }

    public String getTrachomaLatexAgglutinationDetails() {
        return trachomaLatexAgglutinationDetails;
    }

    public String getTrachomaCqValueDetectionDetails() {
        return trachomaCqValueDetectionDetails;
    }

    public String getTrachomaSequencingDetails() {
        return trachomaSequencingDetails;
    }

    public String getTrachomaDnaMicroarrayDetails() {
        return trachomaDnaMicroarrayDetails;
    }

    public String getTrachomaOtherDetails() {
        return trachomaOtherDetails;
    }

    public String getYawsEndemicSyphilisAntibodyDetection() {
        return yawsEndemicSyphilisAntibodyDetection;
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

    public String getYawsEndemicSyphilisDirectFluorescentAntibody() {
        return yawsEndemicSyphilisDirectFluorescentAntibody;
    }

    public String getYawsEndemicSyphilisMicroscopy() {
        return yawsEndemicSyphilisMicroscopy;
    }

    public String getYawsEndemicSyphilisNeutralizingAntibodies() {
        return yawsEndemicSyphilisNeutralizingAntibodies;
    }

    public String getYawsEndemicSyphilisPcrRtPcr() {
        return yawsEndemicSyphilisPcrRtPcr;
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

    public String getYawsEndemicSyphilisSequencing() {
        return yawsEndemicSyphilisSequencing;
    }

    public String getYawsEndemicSyphilisDnaMicroarray() {
        return yawsEndemicSyphilisDnaMicroarray;
    }

    public String getYawsEndemicSyphilisOther() {
        return yawsEndemicSyphilisOther;
    }

    public String getYawsEndemicSyphilisAntibodyDetectionDetails() {
        return yawsEndemicSyphilisAntibodyDetectionDetails;
    }

    public String getYawsEndemicSyphilisAntigenDetectionDetails() {
        return yawsEndemicSyphilisAntigenDetectionDetails;
    }

    public String getYawsEndemicSyphilisRapidTestDetails() {
        return yawsEndemicSyphilisRapidTestDetails;
    }

    public String getYawsEndemicSyphilisCultureDetails() {
        return yawsEndemicSyphilisCultureDetails;
    }

    public String getYawsEndemicSyphilisHistopathologyDetails() {
        return yawsEndemicSyphilisHistopathologyDetails;
    }

    public String getYawsEndemicSyphilisIsolationDetails() {
        return yawsEndemicSyphilisIsolationDetails;
    }

    public String getYawsEndemicSyphilisIgmSerumAntibodyDetails() {
        return yawsEndemicSyphilisIgmSerumAntibodyDetails;
    }

    public String getYawsEndemicSyphilisIggSerumAntibodyDetails() {
        return yawsEndemicSyphilisIggSerumAntibodyDetails;
    }

    public String getYawsEndemicSyphilisIgaSerumAntibodyDetails() {
        return yawsEndemicSyphilisIgaSerumAntibodyDetails;
    }

    public String getYawsEndemicSyphilisIncubationTimeDetails() {
        return yawsEndemicSyphilisIncubationTimeDetails;
    }

    public String getYawsEndemicSyphilisIndirectFluorescentAntibodyDetails() {
        return yawsEndemicSyphilisIndirectFluorescentAntibodyDetails;
    }

    public String getYawsEndemicSyphilisDirectFluorescentAntibodyDetails() {
        return yawsEndemicSyphilisDirectFluorescentAntibodyDetails;
    }

    public String getYawsEndemicSyphilisMicroscopyDetails() {
        return yawsEndemicSyphilisMicroscopyDetails;
    }

    public String getYawsEndemicSyphilisNeutralizingAntibodiesDetails() {
        return yawsEndemicSyphilisNeutralizingAntibodiesDetails;
    }

    public String getYawsEndemicSyphilisPcrRtPcrDetails() {
        return yawsEndemicSyphilisPcrRtPcrDetails;
    }

    public String getYawsEndemicSyphilisGramStainDetails() {
        return yawsEndemicSyphilisGramStainDetails;
    }

    public String getYawsEndemicSyphilisLatexAgglutinationDetails() {
        return yawsEndemicSyphilisLatexAgglutinationDetails;
    }

    public String getYawsEndemicSyphilisCqValueDetectionDetails() {
        return yawsEndemicSyphilisCqValueDetectionDetails;
    }

    public String getYawsEndemicSyphilisSequencingDetails() {
        return yawsEndemicSyphilisSequencingDetails;
    }

    public String getYawsEndemicSyphilisDnaMicroarrayDetails() {
        return yawsEndemicSyphilisDnaMicroarrayDetails;
    }

    public String getYawsEndemicSyphilisOtherDetails() {
        return yawsEndemicSyphilisOtherDetails;
    }

    public String getMaternalDeathsAntibodyDetection() {
        return maternalDeathsAntibodyDetection;
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

    public String getMaternalDeathsDirectFluorescentAntibody() {
        return maternalDeathsDirectFluorescentAntibody;
    }

    public String getMaternalDeathsMicroscopy() {
        return maternalDeathsMicroscopy;
    }

    public String getMaternalDeathsNeutralizingAntibodies() {
        return maternalDeathsNeutralizingAntibodies;
    }

    public String getMaternalDeathsPcrRtPcr() {
        return maternalDeathsPcrRtPcr;
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

    public String getMaternalDeathsSequencing() {
        return maternalDeathsSequencing;
    }

    public String getMaternalDeathsDnaMicroarray() {
        return maternalDeathsDnaMicroarray;
    }

    public String getMaternalDeathsOther() {
        return maternalDeathsOther;
    }

    public String getMaternalDeathsAntibodyDetectionDetails() {
        return maternalDeathsAntibodyDetectionDetails;
    }

    public String getMaternalDeathsAntigenDetectionDetails() {
        return maternalDeathsAntigenDetectionDetails;
    }

    public String getMaternalDeathsRapidTestDetails() {
        return maternalDeathsRapidTestDetails;
    }

    public String getMaternalDeathsCultureDetails() {
        return maternalDeathsCultureDetails;
    }

    public String getMaternalDeathsHistopathologyDetails() {
        return maternalDeathsHistopathologyDetails;
    }

    public String getMaternalDeathsIsolationDetails() {
        return maternalDeathsIsolationDetails;
    }

    public String getMaternalDeathsIgmSerumAntibodyDetails() {
        return maternalDeathsIgmSerumAntibodyDetails;
    }

    public String getMaternalDeathsIggSerumAntibodyDetails() {
        return maternalDeathsIggSerumAntibodyDetails;
    }

    public String getMaternalDeathsIgaSerumAntibodyDetails() {
        return maternalDeathsIgaSerumAntibodyDetails;
    }

    public String getMaternalDeathsIncubationTimeDetails() {
        return maternalDeathsIncubationTimeDetails;
    }

    public String getMaternalDeathsIndirectFluorescentAntibodyDetails() {
        return maternalDeathsIndirectFluorescentAntibodyDetails;
    }

    public String getMaternalDeathsDirectFluorescentAntibodyDetails() {
        return maternalDeathsDirectFluorescentAntibodyDetails;
    }

    public String getMaternalDeathsMicroscopyDetails() {
        return maternalDeathsMicroscopyDetails;
    }

    public String getMaternalDeathsNeutralizingAntibodiesDetails() {
        return maternalDeathsNeutralizingAntibodiesDetails;
    }

    public String getMaternalDeathsPcrRtPcrDetails() {
        return maternalDeathsPcrRtPcrDetails;
    }

    public String getMaternalDeathsGramStainDetails() {
        return maternalDeathsGramStainDetails;
    }

    public String getMaternalDeathsLatexAgglutinationDetails() {
        return maternalDeathsLatexAgglutinationDetails;
    }

    public String getMaternalDeathsCqValueDetectionDetails() {
        return maternalDeathsCqValueDetectionDetails;
    }

    public String getMaternalDeathsSequencingDetails() {
        return maternalDeathsSequencingDetails;
    }

    public String getMaternalDeathsDnaMicroarrayDetails() {
        return maternalDeathsDnaMicroarrayDetails;
    }

    public String getMaternalDeathsOtherDetails() {
        return maternalDeathsOtherDetails;
    }

    public String getPerinatalDeathsAntibodyDetection() {
        return perinatalDeathsAntibodyDetection;
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

    public String getPerinatalDeathsDirectFluorescentAntibody() {
        return perinatalDeathsDirectFluorescentAntibody;
    }

    public String getPerinatalDeathsMicroscopy() {
        return perinatalDeathsMicroscopy;
    }

    public String getPerinatalDeathsNeutralizingAntibodies() {
        return perinatalDeathsNeutralizingAntibodies;
    }

    public String getPerinatalDeathsPcrRtPcr() {
        return perinatalDeathsPcrRtPcr;
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

    public String getPerinatalDeathsSequencing() {
        return perinatalDeathsSequencing;
    }

    public String getPerinatalDeathsDnaMicroarray() {
        return perinatalDeathsDnaMicroarray;
    }

    public String getPerinatalDeathsOther() {
        return perinatalDeathsOther;
    }

    public String getPerinatalDeathsAntibodyDetectionDetails() {
        return perinatalDeathsAntibodyDetectionDetails;
    }

    public String getPerinatalDeathsAntigenDetectionDetails() {
        return perinatalDeathsAntigenDetectionDetails;
    }

    public String getPerinatalDeathsRapidTestDetails() {
        return perinatalDeathsRapidTestDetails;
    }

    public String getPerinatalDeathsCultureDetails() {
        return perinatalDeathsCultureDetails;
    }

    public String getPerinatalDeathsHistopathologyDetails() {
        return perinatalDeathsHistopathologyDetails;
    }

    public String getPerinatalDeathsIsolationDetails() {
        return perinatalDeathsIsolationDetails;
    }

    public String getPerinatalDeathsIgmSerumAntibodyDetails() {
        return perinatalDeathsIgmSerumAntibodyDetails;
    }

    public String getPerinatalDeathsIggSerumAntibodyDetails() {
        return perinatalDeathsIggSerumAntibodyDetails;
    }

    public String getPerinatalDeathsIgaSerumAntibodyDetails() {
        return perinatalDeathsIgaSerumAntibodyDetails;
    }

    public String getPerinatalDeathsIncubationTimeDetails() {
        return perinatalDeathsIncubationTimeDetails;
    }

    public String getPerinatalDeathsIndirectFluorescentAntibodyDetails() {
        return perinatalDeathsIndirectFluorescentAntibodyDetails;
    }

    public String getPerinatalDeathsDirectFluorescentAntibodyDetails() {
        return perinatalDeathsDirectFluorescentAntibodyDetails;
    }

    public String getPerinatalDeathsMicroscopyDetails() {
        return perinatalDeathsMicroscopyDetails;
    }

    public String getPerinatalDeathsNeutralizingAntibodiesDetails() {
        return perinatalDeathsNeutralizingAntibodiesDetails;
    }

    public String getPerinatalDeathsPcrRtPcrDetails() {
        return perinatalDeathsPcrRtPcrDetails;
    }

    public String getPerinatalDeathsGramStainDetails() {
        return perinatalDeathsGramStainDetails;
    }

    public String getPerinatalDeathsLatexAgglutinationDetails() {
        return perinatalDeathsLatexAgglutinationDetails;
    }

    public String getPerinatalDeathsCqValueDetectionDetails() {
        return perinatalDeathsCqValueDetectionDetails;
    }

    public String getPerinatalDeathsSequencingDetails() {
        return perinatalDeathsSequencingDetails;
    }

    public String getPerinatalDeathsDnaMicroarrayDetails() {
        return perinatalDeathsDnaMicroarrayDetails;
    }

    public String getPerinatalDeathsOtherDetails() {
        return perinatalDeathsOtherDetails;
    }

    public String getInfluenzaAAntibodyDetection() {
        return influenzaAAntibodyDetection;
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

    public String getInfluenzaADirectFluorescentAntibody() {
        return influenzaADirectFluorescentAntibody;
    }

    public String getInfluenzaAMicroscopy() {
        return influenzaAMicroscopy;
    }

    public String getInfluenzaANeutralizingAntibodies() {
        return influenzaANeutralizingAntibodies;
    }

    public String getInfluenzaAPcrRtPcr() {
        return influenzaAPcrRtPcr;
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

    public String getInfluenzaASequencing() {
        return influenzaASequencing;
    }

    public String getInfluenzaADnaMicroarray() {
        return influenzaADnaMicroarray;
    }

    public String getInfluenzaAOther() {
        return influenzaAOther;
    }

    public String getInfluenzaAAntibodyDetectionDetails() {
        return influenzaAAntibodyDetectionDetails;
    }

    public String getInfluenzaAAntigenDetectionDetails() {
        return influenzaAAntigenDetectionDetails;
    }

    public String getInfluenzaARapidTestDetails() {
        return influenzaARapidTestDetails;
    }

    public String getInfluenzaACultureDetails() {
        return influenzaACultureDetails;
    }

    public String getInfluenzaAHistopathologyDetails() {
        return influenzaAHistopathologyDetails;
    }

    public String getInfluenzaAIsolationDetails() {
        return influenzaAIsolationDetails;
    }

    public String getInfluenzaAIgmSerumAntibodyDetails() {
        return influenzaAIgmSerumAntibodyDetails;
    }

    public String getInfluenzaAIggSerumAntibodyDetails() {
        return influenzaAIggSerumAntibodyDetails;
    }

    public String getInfluenzaAIgaSerumAntibodyDetails() {
        return influenzaAIgaSerumAntibodyDetails;
    }

    public String getInfluenzaAIncubationTimeDetails() {
        return influenzaAIncubationTimeDetails;
    }

    public String getInfluenzaAIndirectFluorescentAntibodyDetails() {
        return influenzaAIndirectFluorescentAntibodyDetails;
    }

    public String getInfluenzaADirectFluorescentAntibodyDetails() {
        return influenzaADirectFluorescentAntibodyDetails;
    }

    public String getInfluenzaAMicroscopyDetails() {
        return influenzaAMicroscopyDetails;
    }

    public String getInfluenzaANeutralizingAntibodiesDetails() {
        return influenzaANeutralizingAntibodiesDetails;
    }

    public String getInfluenzaAPcrRtPcrDetails() {
        return influenzaAPcrRtPcrDetails;
    }

    public String getInfluenzaAGramStainDetails() {
        return influenzaAGramStainDetails;
    }

    public String getInfluenzaALatexAgglutinationDetails() {
        return influenzaALatexAgglutinationDetails;
    }

    public String getInfluenzaACqValueDetectionDetails() {
        return influenzaACqValueDetectionDetails;
    }

    public String getInfluenzaASequencingDetails() {
        return influenzaASequencingDetails;
    }

    public String getInfluenzaADnaMicroarrayDetails() {
        return influenzaADnaMicroarrayDetails;
    }

    public String getInfluenzaAOtherDetails() {
        return influenzaAOtherDetails;
    }

    public String getInfluenzaBAntibodyDetection() {
        return influenzaBAntibodyDetection;
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

    public String getInfluenzaBDirectFluorescentAntibody() {
        return influenzaBDirectFluorescentAntibody;
    }

    public String getInfluenzaBMicroscopy() {
        return influenzaBMicroscopy;
    }

    public String getInfluenzaBNeutralizingAntibodies() {
        return influenzaBNeutralizingAntibodies;
    }

    public String getInfluenzaBPcrRtPcr() {
        return influenzaBPcrRtPcr;
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

    public String getInfluenzaBSequencing() {
        return influenzaBSequencing;
    }

    public String getInfluenzaBDnaMicroarray() {
        return influenzaBDnaMicroarray;
    }

    public String getInfluenzaBOther() {
        return influenzaBOther;
    }

    public String getInfluenzaBAntibodyDetectionDetails() {
        return influenzaBAntibodyDetectionDetails;
    }

    public String getInfluenzaBAntigenDetectionDetails() {
        return influenzaBAntigenDetectionDetails;
    }

    public String getInfluenzaBRapidTestDetails() {
        return influenzaBRapidTestDetails;
    }

    public String getInfluenzaBCultureDetails() {
        return influenzaBCultureDetails;
    }

    public String getInfluenzaBHistopathologyDetails() {
        return influenzaBHistopathologyDetails;
    }

    public String getInfluenzaBIsolationDetails() {
        return influenzaBIsolationDetails;
    }

    public String getInfluenzaBIgmSerumAntibodyDetails() {
        return influenzaBIgmSerumAntibodyDetails;
    }

    public String getInfluenzaBIggSerumAntibodyDetails() {
        return influenzaBIggSerumAntibodyDetails;
    }

    public String getInfluenzaBIgaSerumAntibodyDetails() {
        return influenzaBIgaSerumAntibodyDetails;
    }

    public String getInfluenzaBIncubationTimeDetails() {
        return influenzaBIncubationTimeDetails;
    }

    public String getInfluenzaBIndirectFluorescentAntibodyDetails() {
        return influenzaBIndirectFluorescentAntibodyDetails;
    }

    public String getInfluenzaBDirectFluorescentAntibodyDetails() {
        return influenzaBDirectFluorescentAntibodyDetails;
    }

    public String getInfluenzaBMicroscopyDetails() {
        return influenzaBMicroscopyDetails;
    }

    public String getInfluenzaBNeutralizingAntibodiesDetails() {
        return influenzaBNeutralizingAntibodiesDetails;
    }

    public String getInfluenzaBPcrRtPcrDetails() {
        return influenzaBPcrRtPcrDetails;
    }

    public String getInfluenzaBGramStainDetails() {
        return influenzaBGramStainDetails;
    }

    public String getInfluenzaBLatexAgglutinationDetails() {
        return influenzaBLatexAgglutinationDetails;
    }

    public String getInfluenzaBCqValueDetectionDetails() {
        return influenzaBCqValueDetectionDetails;
    }

    public String getInfluenzaBSequencingDetails() {
        return influenzaBSequencingDetails;
    }

    public String getInfluenzaBDnaMicroarrayDetails() {
        return influenzaBDnaMicroarrayDetails;
    }

    public String getInfluenzaBOtherDetails() {
        return influenzaBOtherDetails;
    }

    public String getHMetapneumovirusAntibodyDetection() {
        return hMetapneumovirusAntibodyDetection;
    }

    public String getHMetapneumovirusAntigenDetection() {
        return hMetapneumovirusAntigenDetection;
    }

    public String getHMetapneumovirusRapidTest() {
        return hMetapneumovirusRapidTest;
    }

    public String getHMetapneumovirusCulture() {
        return hMetapneumovirusCulture;
    }

    public String getHMetapneumovirusHistopathology() {
        return hMetapneumovirusHistopathology;
    }

    public String getHMetapneumovirusIsolation() {
        return hMetapneumovirusIsolation;
    }

    public String getHMetapneumovirusIgmSerumAntibody() {
        return hMetapneumovirusIgmSerumAntibody;
    }

    public String getHMetapneumovirusIggSerumAntibody() {
        return hMetapneumovirusIggSerumAntibody;
    }

    public String getHMetapneumovirusIgaSerumAntibody() {
        return hMetapneumovirusIgaSerumAntibody;
    }

    public String getHMetapneumovirusIncubationTime() {
        return hMetapneumovirusIncubationTime;
    }

    public String getHMetapneumovirusIndirectFluorescentAntibody() {
        return hMetapneumovirusIndirectFluorescentAntibody;
    }

    public String getHMetapneumovirusDirectFluorescentAntibody() {
        return hMetapneumovirusDirectFluorescentAntibody;
    }

    public String getHMetapneumovirusMicroscopy() {
        return hMetapneumovirusMicroscopy;
    }

    public String getHMetapneumovirusNeutralizingAntibodies() {
        return hMetapneumovirusNeutralizingAntibodies;
    }

    public String getHMetapneumovirusPcrRtPcr() {
        return hMetapneumovirusPcrRtPcr;
    }

    public String getHMetapneumovirusGramStain() {
        return hMetapneumovirusGramStain;
    }

    public String getHMetapneumovirusLatexAgglutination() {
        return hMetapneumovirusLatexAgglutination;
    }

    public String getHMetapneumovirusCqValueDetection() {
        return hMetapneumovirusCqValueDetection;
    }

    public String getHMetapneumovirusSequencing() {
        return hMetapneumovirusSequencing;
    }

    public String getHMetapneumovirusDnaMicroarray() {
        return hMetapneumovirusDnaMicroarray;
    }

    public String getHMetapneumovirusOther() {
        return hMetapneumovirusOther;
    }

    public String getHMetapneumovirusAntibodyDetectionDetails() {
        return hMetapneumovirusAntibodyDetectionDetails;
    }

    public String getHMetapneumovirusAntigenDetectionDetails() {
        return hMetapneumovirusAntigenDetectionDetails;
    }

    public String getHMetapneumovirusRapidTestDetails() {
        return hMetapneumovirusRapidTestDetails;
    }

    public String getHMetapneumovirusCultureDetails() {
        return hMetapneumovirusCultureDetails;
    }

    public String getHMetapneumovirusHistopathologyDetails() {
        return hMetapneumovirusHistopathologyDetails;
    }

    public String getHMetapneumovirusIsolationDetails() {
        return hMetapneumovirusIsolationDetails;
    }

    public String getHMetapneumovirusIgmSerumAntibodyDetails() {
        return hMetapneumovirusIgmSerumAntibodyDetails;
    }

    public String getHMetapneumovirusIggSerumAntibodyDetails() {
        return hMetapneumovirusIggSerumAntibodyDetails;
    }

    public String getHMetapneumovirusIgaSerumAntibodyDetails() {
        return hMetapneumovirusIgaSerumAntibodyDetails;
    }

    public String getHMetapneumovirusIncubationTimeDetails() {
        return hMetapneumovirusIncubationTimeDetails;
    }

    public String getHMetapneumovirusIndirectFluorescentAntibodyDetails() {
        return hMetapneumovirusIndirectFluorescentAntibodyDetails;
    }

    public String getHMetapneumovirusDirectFluorescentAntibodyDetails() {
        return hMetapneumovirusDirectFluorescentAntibodyDetails;
    }

    public String getHMetapneumovirusMicroscopyDetails() {
        return hMetapneumovirusMicroscopyDetails;
    }

    public String getHMetapneumovirusNeutralizingAntibodiesDetails() {
        return hMetapneumovirusNeutralizingAntibodiesDetails;
    }

    public String getHMetapneumovirusPcrRtPcrDetails() {
        return hMetapneumovirusPcrRtPcrDetails;
    }

    public String getHMetapneumovirusGramStainDetails() {
        return hMetapneumovirusGramStainDetails;
    }

    public String getHMetapneumovirusLatexAgglutinationDetails() {
        return hMetapneumovirusLatexAgglutinationDetails;
    }

    public String getHMetapneumovirusCqValueDetectionDetails() {
        return hMetapneumovirusCqValueDetectionDetails;
    }

    public String getHMetapneumovirusSequencingDetails() {
        return hMetapneumovirusSequencingDetails;
    }

    public String getHMetapneumovirusDnaMicroarrayDetails() {
        return hMetapneumovirusDnaMicroarrayDetails;
    }

    public String getHMetapneumovirusOtherDetails() {
        return hMetapneumovirusOtherDetails;
    }

    public String getRespiratorySyncytialVirusAntibodyDetection() {
        return respiratorySyncytialVirusAntibodyDetection;
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

    public String getRespiratorySyncytialVirusDirectFluorescentAntibody() {
        return respiratorySyncytialVirusDirectFluorescentAntibody;
    }

    public String getRespiratorySyncytialVirusMicroscopy() {
        return respiratorySyncytialVirusMicroscopy;
    }

    public String getRespiratorySyncytialVirusNeutralizingAntibodies() {
        return respiratorySyncytialVirusNeutralizingAntibodies;
    }

    public String getRespiratorySyncytialVirusPcrRtPcr() {
        return respiratorySyncytialVirusPcrRtPcr;
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

    public String getRespiratorySyncytialVirusSequencing() {
        return respiratorySyncytialVirusSequencing;
    }

    public String getRespiratorySyncytialVirusDnaMicroarray() {
        return respiratorySyncytialVirusDnaMicroarray;
    }

    public String getRespiratorySyncytialVirusOther() {
        return respiratorySyncytialVirusOther;
    }

    public String getRespiratorySyncytialVirusAntibodyDetectionDetails() {
        return respiratorySyncytialVirusAntibodyDetectionDetails;
    }

    public String getRespiratorySyncytialVirusAntigenDetectionDetails() {
        return respiratorySyncytialVirusAntigenDetectionDetails;
    }

    public String getRespiratorySyncytialVirusRapidTestDetails() {
        return respiratorySyncytialVirusRapidTestDetails;
    }

    public String getRespiratorySyncytialVirusCultureDetails() {
        return respiratorySyncytialVirusCultureDetails;
    }

    public String getRespiratorySyncytialVirusHistopathologyDetails() {
        return respiratorySyncytialVirusHistopathologyDetails;
    }

    public String getRespiratorySyncytialVirusIsolationDetails() {
        return respiratorySyncytialVirusIsolationDetails;
    }

    public String getRespiratorySyncytialVirusIgmSerumAntibodyDetails() {
        return respiratorySyncytialVirusIgmSerumAntibodyDetails;
    }

    public String getRespiratorySyncytialVirusIggSerumAntibodyDetails() {
        return respiratorySyncytialVirusIggSerumAntibodyDetails;
    }

    public String getRespiratorySyncytialVirusIgaSerumAntibodyDetails() {
        return respiratorySyncytialVirusIgaSerumAntibodyDetails;
    }

    public String getRespiratorySyncytialVirusIncubationTimeDetails() {
        return respiratorySyncytialVirusIncubationTimeDetails;
    }

    public String getRespiratorySyncytialVirusIndirectFluorescentAntibodyDetails() {
        return respiratorySyncytialVirusIndirectFluorescentAntibodyDetails;
    }

    public String getRespiratorySyncytialVirusDirectFluorescentAntibodyDetails() {
        return respiratorySyncytialVirusDirectFluorescentAntibodyDetails;
    }

    public String getRespiratorySyncytialVirusMicroscopyDetails() {
        return respiratorySyncytialVirusMicroscopyDetails;
    }

    public String getRespiratorySyncytialVirusNeutralizingAntibodiesDetails() {
        return respiratorySyncytialVirusNeutralizingAntibodiesDetails;
    }

    public String getRespiratorySyncytialVirusPcrRtPcrDetails() {
        return respiratorySyncytialVirusPcrRtPcrDetails;
    }

    public String getRespiratorySyncytialVirusGramStainDetails() {
        return respiratorySyncytialVirusGramStainDetails;
    }

    public String getRespiratorySyncytialVirusLatexAgglutinationDetails() {
        return respiratorySyncytialVirusLatexAgglutinationDetails;
    }

    public String getRespiratorySyncytialVirusCqValueDetectionDetails() {
        return respiratorySyncytialVirusCqValueDetectionDetails;
    }

    public String getRespiratorySyncytialVirusSequencingDetails() {
        return respiratorySyncytialVirusSequencingDetails;
    }

    public String getRespiratorySyncytialVirusDnaMicroarrayDetails() {
        return respiratorySyncytialVirusDnaMicroarrayDetails;
    }

    public String getRespiratorySyncytialVirusOtherDetails() {
        return respiratorySyncytialVirusOtherDetails;
    }

    public String getParainfluenzaAntibodyDetection() {
        return parainfluenzaAntibodyDetection;
    }

    public String getParainfluenzaAntigenDetection() {
        return parainfluenzaAntigenDetection;
    }

    public String getParainfluenzaRapidTest() {
        return parainfluenzaRapidTest;
    }

    public String getParainfluenzaCulture() {
        return parainfluenzaCulture;
    }

    public String getParainfluenzaHistopathology() {
        return parainfluenzaHistopathology;
    }

    public String getParainfluenzaIsolation() {
        return parainfluenzaIsolation;
    }

    public String getParainfluenzaIgmSerumAntibody() {
        return parainfluenzaIgmSerumAntibody;
    }

    public String getParainfluenzaIggSerumAntibody() {
        return parainfluenzaIggSerumAntibody;
    }

    public String getParainfluenzaIgaSerumAntibody() {
        return parainfluenzaIgaSerumAntibody;
    }

    public String getParainfluenzaIncubationTime() {
        return parainfluenzaIncubationTime;
    }

    public String getParainfluenzaIndirectFluorescentAntibody() {
        return parainfluenzaIndirectFluorescentAntibody;
    }

    public String getParainfluenzaDirectFluorescentAntibody() {
        return parainfluenzaDirectFluorescentAntibody;
    }

    public String getParainfluenzaMicroscopy() {
        return parainfluenzaMicroscopy;
    }

    public String getParainfluenzaNeutralizingAntibodies() {
        return parainfluenzaNeutralizingAntibodies;
    }

    public String getParainfluenzaPcrRtPcr() {
        return parainfluenzaPcrRtPcr;
    }

    public String getParainfluenzaGramStain() {
        return parainfluenzaGramStain;
    }

    public String getParainfluenzaLatexAgglutination() {
        return parainfluenzaLatexAgglutination;
    }

    public String getParainfluenzaCqValueDetection() {
        return parainfluenzaCqValueDetection;
    }

    public String getParainfluenzaSequencing() {
        return parainfluenzaSequencing;
    }

    public String getParainfluenzaDnaMicroarray() {
        return parainfluenzaDnaMicroarray;
    }

    public String getParainfluenzaOther() {
        return parainfluenzaOther;
    }

    public String getParainfluenzaAntibodyDetectionDetails() {
        return parainfluenzaAntibodyDetectionDetails;
    }

    public String getParainfluenzaAntigenDetectionDetails() {
        return parainfluenzaAntigenDetectionDetails;
    }

    public String getParainfluenzaRapidTestDetails() {
        return parainfluenzaRapidTestDetails;
    }

    public String getParainfluenzaCultureDetails() {
        return parainfluenzaCultureDetails;
    }

    public String getParainfluenzaHistopathologyDetails() {
        return parainfluenzaHistopathologyDetails;
    }

    public String getParainfluenzaIsolationDetails() {
        return parainfluenzaIsolationDetails;
    }

    public String getParainfluenzaIgmSerumAntibodyDetails() {
        return parainfluenzaIgmSerumAntibodyDetails;
    }

    public String getParainfluenzaIggSerumAntibodyDetails() {
        return parainfluenzaIggSerumAntibodyDetails;
    }

    public String getParainfluenzaIgaSerumAntibodyDetails() {
        return parainfluenzaIgaSerumAntibodyDetails;
    }

    public String getParainfluenzaIncubationTimeDetails() {
        return parainfluenzaIncubationTimeDetails;
    }

    public String getParainfluenzaIndirectFluorescentAntibodyDetails() {
        return parainfluenzaIndirectFluorescentAntibodyDetails;
    }

    public String getParainfluenzaDirectFluorescentAntibodyDetails() {
        return parainfluenzaDirectFluorescentAntibodyDetails;
    }

    public String getParainfluenzaMicroscopyDetails() {
        return parainfluenzaMicroscopyDetails;
    }

    public String getParainfluenzaNeutralizingAntibodiesDetails() {
        return parainfluenzaNeutralizingAntibodiesDetails;
    }

    public String getParainfluenzaPcrRtPcrDetails() {
        return parainfluenzaPcrRtPcrDetails;
    }

    public String getParainfluenzaGramStainDetails() {
        return parainfluenzaGramStainDetails;
    }

    public String getParainfluenzaLatexAgglutinationDetails() {
        return parainfluenzaLatexAgglutinationDetails;
    }

    public String getParainfluenzaCqValueDetectionDetails() {
        return parainfluenzaCqValueDetectionDetails;
    }

    public String getParainfluenzaSequencingDetails() {
        return parainfluenzaSequencingDetails;
    }

    public String getParainfluenzaDnaMicroarrayDetails() {
        return parainfluenzaDnaMicroarrayDetails;
    }

    public String getParainfluenzaOtherDetails() {
        return parainfluenzaOtherDetails;
    }

    public String getAdenovirusAntibodyDetection() {
        return adenovirusAntibodyDetection;
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

    public String getAdenovirusDirectFluorescentAntibody() {
        return adenovirusDirectFluorescentAntibody;
    }

    public String getAdenovirusMicroscopy() {
        return adenovirusMicroscopy;
    }

    public String getAdenovirusNeutralizingAntibodies() {
        return adenovirusNeutralizingAntibodies;
    }

    public String getAdenovirusPcrRtPcr() {
        return adenovirusPcrRtPcr;
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

    public String getAdenovirusSequencing() {
        return adenovirusSequencing;
    }

    public String getAdenovirusDnaMicroarray() {
        return adenovirusDnaMicroarray;
    }

    public String getAdenovirusOther() {
        return adenovirusOther;
    }

    public String getAdenovirusAntibodyDetectionDetails() {
        return adenovirusAntibodyDetectionDetails;
    }

    public String getAdenovirusAntigenDetectionDetails() {
        return adenovirusAntigenDetectionDetails;
    }

    public String getAdenovirusRapidTestDetails() {
        return adenovirusRapidTestDetails;
    }

    public String getAdenovirusCultureDetails() {
        return adenovirusCultureDetails;
    }

    public String getAdenovirusHistopathologyDetails() {
        return adenovirusHistopathologyDetails;
    }

    public String getAdenovirusIsolationDetails() {
        return adenovirusIsolationDetails;
    }

    public String getAdenovirusIgmSerumAntibodyDetails() {
        return adenovirusIgmSerumAntibodyDetails;
    }

    public String getAdenovirusIggSerumAntibodyDetails() {
        return adenovirusIggSerumAntibodyDetails;
    }

    public String getAdenovirusIgaSerumAntibodyDetails() {
        return adenovirusIgaSerumAntibodyDetails;
    }

    public String getAdenovirusIncubationTimeDetails() {
        return adenovirusIncubationTimeDetails;
    }

    public String getAdenovirusIndirectFluorescentAntibodyDetails() {
        return adenovirusIndirectFluorescentAntibodyDetails;
    }

    public String getAdenovirusDirectFluorescentAntibodyDetails() {
        return adenovirusDirectFluorescentAntibodyDetails;
    }

    public String getAdenovirusMicroscopyDetails() {
        return adenovirusMicroscopyDetails;
    }

    public String getAdenovirusNeutralizingAntibodiesDetails() {
        return adenovirusNeutralizingAntibodiesDetails;
    }

    public String getAdenovirusPcrRtPcrDetails() {
        return adenovirusPcrRtPcrDetails;
    }

    public String getAdenovirusGramStainDetails() {
        return adenovirusGramStainDetails;
    }

    public String getAdenovirusLatexAgglutinationDetails() {
        return adenovirusLatexAgglutinationDetails;
    }

    public String getAdenovirusCqValueDetectionDetails() {
        return adenovirusCqValueDetectionDetails;
    }

    public String getAdenovirusSequencingDetails() {
        return adenovirusSequencingDetails;
    }

    public String getAdenovirusDnaMicroarrayDetails() {
        return adenovirusDnaMicroarrayDetails;
    }

    public String getAdenovirusOtherDetails() {
        return adenovirusOtherDetails;
    }

    public String getRhinovirusAntibodyDetection() {
        return rhinovirusAntibodyDetection;
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

    public String getRhinovirusDirectFluorescentAntibody() {
        return rhinovirusDirectFluorescentAntibody;
    }

    public String getRhinovirusMicroscopy() {
        return rhinovirusMicroscopy;
    }

    public String getRhinovirusNeutralizingAntibodies() {
        return rhinovirusNeutralizingAntibodies;
    }

    public String getRhinovirusPcrRtPcr() {
        return rhinovirusPcrRtPcr;
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

    public String getRhinovirusSequencing() {
        return rhinovirusSequencing;
    }

    public String getRhinovirusDnaMicroarray() {
        return rhinovirusDnaMicroarray;
    }

    public String getRhinovirusOther() {
        return rhinovirusOther;
    }

    public String getRhinovirusAntibodyDetectionDetails() {
        return rhinovirusAntibodyDetectionDetails;
    }

    public String getRhinovirusAntigenDetectionDetails() {
        return rhinovirusAntigenDetectionDetails;
    }

    public String getRhinovirusRapidTestDetails() {
        return rhinovirusRapidTestDetails;
    }

    public String getRhinovirusCultureDetails() {
        return rhinovirusCultureDetails;
    }

    public String getRhinovirusHistopathologyDetails() {
        return rhinovirusHistopathologyDetails;
    }

    public String getRhinovirusIsolationDetails() {
        return rhinovirusIsolationDetails;
    }

    public String getRhinovirusIgmSerumAntibodyDetails() {
        return rhinovirusIgmSerumAntibodyDetails;
    }

    public String getRhinovirusIggSerumAntibodyDetails() {
        return rhinovirusIggSerumAntibodyDetails;
    }

    public String getRhinovirusIgaSerumAntibodyDetails() {
        return rhinovirusIgaSerumAntibodyDetails;
    }

    public String getRhinovirusIncubationTimeDetails() {
        return rhinovirusIncubationTimeDetails;
    }

    public String getRhinovirusIndirectFluorescentAntibodyDetails() {
        return rhinovirusIndirectFluorescentAntibodyDetails;
    }

    public String getRhinovirusDirectFluorescentAntibodyDetails() {
        return rhinovirusDirectFluorescentAntibodyDetails;
    }

    public String getRhinovirusMicroscopyDetails() {
        return rhinovirusMicroscopyDetails;
    }

    public String getRhinovirusNeutralizingAntibodiesDetails() {
        return rhinovirusNeutralizingAntibodiesDetails;
    }

    public String getRhinovirusPcrRtPcrDetails() {
        return rhinovirusPcrRtPcrDetails;
    }

    public String getRhinovirusGramStainDetails() {
        return rhinovirusGramStainDetails;
    }

    public String getRhinovirusLatexAgglutinationDetails() {
        return rhinovirusLatexAgglutinationDetails;
    }

    public String getRhinovirusCqValueDetectionDetails() {
        return rhinovirusCqValueDetectionDetails;
    }

    public String getRhinovirusSequencingDetails() {
        return rhinovirusSequencingDetails;
    }

    public String getRhinovirusDnaMicroarrayDetails() {
        return rhinovirusDnaMicroarrayDetails;
    }

    public String getRhinovirusOtherDetails() {
        return rhinovirusOtherDetails;
    }

    public String getEnterovirusAntibodyDetection() {
        return enterovirusAntibodyDetection;
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

    public String getEnterovirusDirectFluorescentAntibody() {
        return enterovirusDirectFluorescentAntibody;
    }

    public String getEnterovirusMicroscopy() {
        return enterovirusMicroscopy;
    }

    public String getEnterovirusNeutralizingAntibodies() {
        return enterovirusNeutralizingAntibodies;
    }

    public String getEnterovirusPcrRtPcr() {
        return enterovirusPcrRtPcr;
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

    public String getEnterovirusSequencing() {
        return enterovirusSequencing;
    }

    public String getEnterovirusDnaMicroarray() {
        return enterovirusDnaMicroarray;
    }

    public String getEnterovirusOther() {
        return enterovirusOther;
    }

    public String getEnterovirusAntibodyDetectionDetails() {
        return enterovirusAntibodyDetectionDetails;
    }

    public String getEnterovirusAntigenDetectionDetails() {
        return enterovirusAntigenDetectionDetails;
    }

    public String getEnterovirusRapidTestDetails() {
        return enterovirusRapidTestDetails;
    }

    public String getEnterovirusCultureDetails() {
        return enterovirusCultureDetails;
    }

    public String getEnterovirusHistopathologyDetails() {
        return enterovirusHistopathologyDetails;
    }

    public String getEnterovirusIsolationDetails() {
        return enterovirusIsolationDetails;
    }

    public String getEnterovirusIgmSerumAntibodyDetails() {
        return enterovirusIgmSerumAntibodyDetails;
    }

    public String getEnterovirusIggSerumAntibodyDetails() {
        return enterovirusIggSerumAntibodyDetails;
    }

    public String getEnterovirusIgaSerumAntibodyDetails() {
        return enterovirusIgaSerumAntibodyDetails;
    }

    public String getEnterovirusIncubationTimeDetails() {
        return enterovirusIncubationTimeDetails;
    }

    public String getEnterovirusIndirectFluorescentAntibodyDetails() {
        return enterovirusIndirectFluorescentAntibodyDetails;
    }

    public String getEnterovirusDirectFluorescentAntibodyDetails() {
        return enterovirusDirectFluorescentAntibodyDetails;
    }

    public String getEnterovirusMicroscopyDetails() {
        return enterovirusMicroscopyDetails;
    }

    public String getEnterovirusNeutralizingAntibodiesDetails() {
        return enterovirusNeutralizingAntibodiesDetails;
    }

    public String getEnterovirusPcrRtPcrDetails() {
        return enterovirusPcrRtPcrDetails;
    }

    public String getEnterovirusGramStainDetails() {
        return enterovirusGramStainDetails;
    }

    public String getEnterovirusLatexAgglutinationDetails() {
        return enterovirusLatexAgglutinationDetails;
    }

    public String getEnterovirusCqValueDetectionDetails() {
        return enterovirusCqValueDetectionDetails;
    }

    public String getEnterovirusSequencingDetails() {
        return enterovirusSequencingDetails;
    }

    public String getEnterovirusDnaMicroarrayDetails() {
        return enterovirusDnaMicroarrayDetails;
    }

    public String getEnterovirusOtherDetails() {
        return enterovirusOtherDetails;
    }

    public String getMPneumoniaeAntibodyDetection() {
        return mPneumoniaeAntibodyDetection;
    }

    public String getMPneumoniaeAntigenDetection() {
        return mPneumoniaeAntigenDetection;
    }

    public String getMPneumoniaeRapidTest() {
        return mPneumoniaeRapidTest;
    }

    public String getMPneumoniaeCulture() {
        return mPneumoniaeCulture;
    }

    public String getMPneumoniaeHistopathology() {
        return mPneumoniaeHistopathology;
    }

    public String getMPneumoniaeIsolation() {
        return mPneumoniaeIsolation;
    }

    public String getMPneumoniaeIgmSerumAntibody() {
        return mPneumoniaeIgmSerumAntibody;
    }

    public String getMPneumoniaeIggSerumAntibody() {
        return mPneumoniaeIggSerumAntibody;
    }

    public String getMPneumoniaeIgaSerumAntibody() {
        return mPneumoniaeIgaSerumAntibody;
    }

    public String getMPneumoniaeIncubationTime() {
        return mPneumoniaeIncubationTime;
    }

    public String getMPneumoniaeIndirectFluorescentAntibody() {
        return mPneumoniaeIndirectFluorescentAntibody;
    }

    public String getMPneumoniaeDirectFluorescentAntibody() {
        return mPneumoniaeDirectFluorescentAntibody;
    }

    public String getMPneumoniaeMicroscopy() {
        return mPneumoniaeMicroscopy;
    }

    public String getMPneumoniaeNeutralizingAntibodies() {
        return mPneumoniaeNeutralizingAntibodies;
    }

    public String getMPneumoniaePcrRtPcr() {
        return mPneumoniaePcrRtPcr;
    }

    public String getMPneumoniaeGramStain() {
        return mPneumoniaeGramStain;
    }

    public String getMPneumoniaeLatexAgglutination() {
        return mPneumoniaeLatexAgglutination;
    }

    public String getMPneumoniaeCqValueDetection() {
        return mPneumoniaeCqValueDetection;
    }

    public String getMPneumoniaeSequencing() {
        return mPneumoniaeSequencing;
    }

    public String getMPneumoniaeDnaMicroarray() {
        return mPneumoniaeDnaMicroarray;
    }

    public String getMPneumoniaeOther() {
        return mPneumoniaeOther;
    }

    public String getMPneumoniaeAntibodyDetectionDetails() {
        return mPneumoniaeAntibodyDetectionDetails;
    }

    public String getMPneumoniaeAntigenDetectionDetails() {
        return mPneumoniaeAntigenDetectionDetails;
    }

    public String getMPneumoniaeRapidTestDetails() {
        return mPneumoniaeRapidTestDetails;
    }

    public String getMPneumoniaeCultureDetails() {
        return mPneumoniaeCultureDetails;
    }

    public String getMPneumoniaeHistopathologyDetails() {
        return mPneumoniaeHistopathologyDetails;
    }

    public String getMPneumoniaeIsolationDetails() {
        return mPneumoniaeIsolationDetails;
    }

    public String getMPneumoniaeIgmSerumAntibodyDetails() {
        return mPneumoniaeIgmSerumAntibodyDetails;
    }

    public String getMPneumoniaeIggSerumAntibodyDetails() {
        return mPneumoniaeIggSerumAntibodyDetails;
    }

    public String getMPneumoniaeIgaSerumAntibodyDetails() {
        return mPneumoniaeIgaSerumAntibodyDetails;
    }

    public String getMPneumoniaeIncubationTimeDetails() {
        return mPneumoniaeIncubationTimeDetails;
    }

    public String getMPneumoniaeIndirectFluorescentAntibodyDetails() {
        return mPneumoniaeIndirectFluorescentAntibodyDetails;
    }

    public String getMPneumoniaeDirectFluorescentAntibodyDetails() {
        return mPneumoniaeDirectFluorescentAntibodyDetails;
    }

    public String getMPneumoniaeMicroscopyDetails() {
        return mPneumoniaeMicroscopyDetails;
    }

    public String getMPneumoniaeNeutralizingAntibodiesDetails() {
        return mPneumoniaeNeutralizingAntibodiesDetails;
    }

    public String getMPneumoniaePcrRtPcrDetails() {
        return mPneumoniaePcrRtPcrDetails;
    }

    public String getMPneumoniaeGramStainDetails() {
        return mPneumoniaeGramStainDetails;
    }

    public String getMPneumoniaeLatexAgglutinationDetails() {
        return mPneumoniaeLatexAgglutinationDetails;
    }

    public String getMPneumoniaeCqValueDetectionDetails() {
        return mPneumoniaeCqValueDetectionDetails;
    }

    public String getMPneumoniaeSequencingDetails() {
        return mPneumoniaeSequencingDetails;
    }

    public String getMPneumoniaeDnaMicroarrayDetails() {
        return mPneumoniaeDnaMicroarrayDetails;
    }

    public String getMPneumoniaeOtherDetails() {
        return mPneumoniaeOtherDetails;
    }

    public String getCPneumoniaeAntibodyDetection() {
        return cPneumoniaeAntibodyDetection;
    }

    public String getCPneumoniaeAntigenDetection() {
        return cPneumoniaeAntigenDetection;
    }

    public String getCPneumoniaeRapidTest() {
        return cPneumoniaeRapidTest;
    }

    public String getCPneumoniaeCulture() {
        return cPneumoniaeCulture;
    }

    public String getCPneumoniaeHistopathology() {
        return cPneumoniaeHistopathology;
    }

    public String getCPneumoniaeIsolation() {
        return cPneumoniaeIsolation;
    }

    public String getCPneumoniaeIgmSerumAntibody() {
        return cPneumoniaeIgmSerumAntibody;
    }

    public String getCPneumoniaeIggSerumAntibody() {
        return cPneumoniaeIggSerumAntibody;
    }

    public String getCPneumoniaeIgaSerumAntibody() {
        return cPneumoniaeIgaSerumAntibody;
    }

    public String getCPneumoniaeIncubationTime() {
        return cPneumoniaeIncubationTime;
    }

    public String getCPneumoniaeIndirectFluorescentAntibody() {
        return cPneumoniaeIndirectFluorescentAntibody;
    }

    public String getCPneumoniaeDirectFluorescentAntibody() {
        return cPneumoniaeDirectFluorescentAntibody;
    }

    public String getCPneumoniaeMicroscopy() {
        return cPneumoniaeMicroscopy;
    }

    public String getCPneumoniaeNeutralizingAntibodies() {
        return cPneumoniaeNeutralizingAntibodies;
    }

    public String getCPneumoniaePcrRtPcr() {
        return cPneumoniaePcrRtPcr;
    }

    public String getCPneumoniaeGramStain() {
        return cPneumoniaeGramStain;
    }

    public String getCPneumoniaeLatexAgglutination() {
        return cPneumoniaeLatexAgglutination;
    }

    public String getCPneumoniaeCqValueDetection() {
        return cPneumoniaeCqValueDetection;
    }

    public String getCPneumoniaeSequencing() {
        return cPneumoniaeSequencing;
    }

    public String getCPneumoniaeDnaMicroarray() {
        return cPneumoniaeDnaMicroarray;
    }

    public String getCPneumoniaeOther() {
        return cPneumoniaeOther;
    }

    public String getCPneumoniaeAntibodyDetectionDetails() {
        return cPneumoniaeAntibodyDetectionDetails;
    }

    public String getCPneumoniaeAntigenDetectionDetails() {
        return cPneumoniaeAntigenDetectionDetails;
    }

    public String getCPneumoniaeRapidTestDetails() {
        return cPneumoniaeRapidTestDetails;
    }

    public String getCPneumoniaeCultureDetails() {
        return cPneumoniaeCultureDetails;
    }

    public String getCPneumoniaeHistopathologyDetails() {
        return cPneumoniaeHistopathologyDetails;
    }

    public String getCPneumoniaeIsolationDetails() {
        return cPneumoniaeIsolationDetails;
    }

    public String getCPneumoniaeIgmSerumAntibodyDetails() {
        return cPneumoniaeIgmSerumAntibodyDetails;
    }

    public String getCPneumoniaeIggSerumAntibodyDetails() {
        return cPneumoniaeIggSerumAntibodyDetails;
    }

    public String getCPneumoniaeIgaSerumAntibodyDetails() {
        return cPneumoniaeIgaSerumAntibodyDetails;
    }

    public String getCPneumoniaeIncubationTimeDetails() {
        return cPneumoniaeIncubationTimeDetails;
    }

    public String getCPneumoniaeIndirectFluorescentAntibodyDetails() {
        return cPneumoniaeIndirectFluorescentAntibodyDetails;
    }

    public String getCPneumoniaeDirectFluorescentAntibodyDetails() {
        return cPneumoniaeDirectFluorescentAntibodyDetails;
    }

    public String getCPneumoniaeMicroscopyDetails() {
        return cPneumoniaeMicroscopyDetails;
    }

    public String getCPneumoniaeNeutralizingAntibodiesDetails() {
        return cPneumoniaeNeutralizingAntibodiesDetails;
    }

    public String getCPneumoniaePcrRtPcrDetails() {
        return cPneumoniaePcrRtPcrDetails;
    }

    public String getCPneumoniaeGramStainDetails() {
        return cPneumoniaeGramStainDetails;
    }

    public String getCPneumoniaeLatexAgglutinationDetails() {
        return cPneumoniaeLatexAgglutinationDetails;
    }

    public String getCPneumoniaeCqValueDetectionDetails() {
        return cPneumoniaeCqValueDetectionDetails;
    }

    public String getCPneumoniaeSequencingDetails() {
        return cPneumoniaeSequencingDetails;
    }

    public String getCPneumoniaeDnaMicroarrayDetails() {
        return cPneumoniaeDnaMicroarrayDetails;
    }

    public String getCPneumoniaeOtherDetails() {
        return cPneumoniaeOtherDetails;
    }

    public String getAriAntibodyDetection() {
        return ariAntibodyDetection;
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

    public String getAriDirectFluorescentAntibody() {
        return ariDirectFluorescentAntibody;
    }

    public String getAriMicroscopy() {
        return ariMicroscopy;
    }

    public String getAriNeutralizingAntibodies() {
        return ariNeutralizingAntibodies;
    }

    public String getAriPcrRtPcr() {
        return ariPcrRtPcr;
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

    public String getAriSequencing() {
        return ariSequencing;
    }

    public String getAriDnaMicroarray() {
        return ariDnaMicroarray;
    }

    public String getAriOther() {
        return ariOther;
    }

    public String getAriAntibodyDetectionDetails() {
        return ariAntibodyDetectionDetails;
    }

    public String getAriAntigenDetectionDetails() {
        return ariAntigenDetectionDetails;
    }

    public String getAriRapidTestDetails() {
        return ariRapidTestDetails;
    }

    public String getAriCultureDetails() {
        return ariCultureDetails;
    }

    public String getAriHistopathologyDetails() {
        return ariHistopathologyDetails;
    }

    public String getAriIsolationDetails() {
        return ariIsolationDetails;
    }

    public String getAriIgmSerumAntibodyDetails() {
        return ariIgmSerumAntibodyDetails;
    }

    public String getAriIggSerumAntibodyDetails() {
        return ariIggSerumAntibodyDetails;
    }

    public String getAriIgaSerumAntibodyDetails() {
        return ariIgaSerumAntibodyDetails;
    }

    public String getAriIncubationTimeDetails() {
        return ariIncubationTimeDetails;
    }

    public String getAriIndirectFluorescentAntibodyDetails() {
        return ariIndirectFluorescentAntibodyDetails;
    }

    public String getAriDirectFluorescentAntibodyDetails() {
        return ariDirectFluorescentAntibodyDetails;
    }

    public String getAriMicroscopyDetails() {
        return ariMicroscopyDetails;
    }

    public String getAriNeutralizingAntibodiesDetails() {
        return ariNeutralizingAntibodiesDetails;
    }

    public String getAriPcrRtPcrDetails() {
        return ariPcrRtPcrDetails;
    }

    public String getAriGramStainDetails() {
        return ariGramStainDetails;
    }

    public String getAriLatexAgglutinationDetails() {
        return ariLatexAgglutinationDetails;
    }

    public String getAriCqValueDetectionDetails() {
        return ariCqValueDetectionDetails;
    }

    public String getAriSequencingDetails() {
        return ariSequencingDetails;
    }

    public String getAriDnaMicroarrayDetails() {
        return ariDnaMicroarrayDetails;
    }

    public String getAriOtherDetails() {
        return ariOtherDetails;
    }

    public String getChikungunyaAntibodyDetection() {
        return chikungunyaAntibodyDetection;
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

    public String getChikungunyaDirectFluorescentAntibody() {
        return chikungunyaDirectFluorescentAntibody;
    }

    public String getChikungunyaMicroscopy() {
        return chikungunyaMicroscopy;
    }

    public String getChikungunyaNeutralizingAntibodies() {
        return chikungunyaNeutralizingAntibodies;
    }

    public String getChikungunyaPcrRtPcr() {
        return chikungunyaPcrRtPcr;
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

    public String getChikungunyaSequencing() {
        return chikungunyaSequencing;
    }

    public String getChikungunyaDnaMicroarray() {
        return chikungunyaDnaMicroarray;
    }

    public String getChikungunyaOther() {
        return chikungunyaOther;
    }

    public String getChikungunyaAntibodyDetectionDetails() {
        return chikungunyaAntibodyDetectionDetails;
    }

    public String getChikungunyaAntigenDetectionDetails() {
        return chikungunyaAntigenDetectionDetails;
    }

    public String getChikungunyaRapidTestDetails() {
        return chikungunyaRapidTestDetails;
    }

    public String getChikungunyaCultureDetails() {
        return chikungunyaCultureDetails;
    }

    public String getChikungunyaHistopathologyDetails() {
        return chikungunyaHistopathologyDetails;
    }

    public String getChikungunyaIsolationDetails() {
        return chikungunyaIsolationDetails;
    }

    public String getChikungunyaIgmSerumAntibodyDetails() {
        return chikungunyaIgmSerumAntibodyDetails;
    }

    public String getChikungunyaIggSerumAntibodyDetails() {
        return chikungunyaIggSerumAntibodyDetails;
    }

    public String getChikungunyaIgaSerumAntibodyDetails() {
        return chikungunyaIgaSerumAntibodyDetails;
    }

    public String getChikungunyaIncubationTimeDetails() {
        return chikungunyaIncubationTimeDetails;
    }

    public String getChikungunyaIndirectFluorescentAntibodyDetails() {
        return chikungunyaIndirectFluorescentAntibodyDetails;
    }

    public String getChikungunyaDirectFluorescentAntibodyDetails() {
        return chikungunyaDirectFluorescentAntibodyDetails;
    }

    public String getChikungunyaMicroscopyDetails() {
        return chikungunyaMicroscopyDetails;
    }

    public String getChikungunyaNeutralizingAntibodiesDetails() {
        return chikungunyaNeutralizingAntibodiesDetails;
    }

    public String getChikungunyaPcrRtPcrDetails() {
        return chikungunyaPcrRtPcrDetails;
    }

    public String getChikungunyaGramStainDetails() {
        return chikungunyaGramStainDetails;
    }

    public String getChikungunyaLatexAgglutinationDetails() {
        return chikungunyaLatexAgglutinationDetails;
    }

    public String getChikungunyaCqValueDetectionDetails() {
        return chikungunyaCqValueDetectionDetails;
    }

    public String getChikungunyaSequencingDetails() {
        return chikungunyaSequencingDetails;
    }

    public String getChikungunyaDnaMicroarrayDetails() {
        return chikungunyaDnaMicroarrayDetails;
    }

    public String getChikungunyaOtherDetails() {
        return chikungunyaOtherDetails;
    }

    public String getPostImmunizationAdverseEventsMildAntibodyDetection() {
        return postImmunizationAdverseEventsMildAntibodyDetection;
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

    public String getPostImmunizationAdverseEventsMildDirectFluorescentAntibody() {
        return postImmunizationAdverseEventsMildDirectFluorescentAntibody;
    }

    public String getPostImmunizationAdverseEventsMildMicroscopy() {
        return postImmunizationAdverseEventsMildMicroscopy;
    }

    public String getPostImmunizationAdverseEventsMildNeutralizingAntibodies() {
        return postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    public String getPostImmunizationAdverseEventsMildPcrRtPcr() {
        return postImmunizationAdverseEventsMildPcrRtPcr;
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

    public String getPostImmunizationAdverseEventsMildSequencing() {
        return postImmunizationAdverseEventsMildSequencing;
    }

    public String getPostImmunizationAdverseEventsMildDnaMicroarray() {
        return postImmunizationAdverseEventsMildDnaMicroarray;
    }

    public String getPostImmunizationAdverseEventsMildOther() {
        return postImmunizationAdverseEventsMildOther;
    }

    public String getPostImmunizationAdverseEventsMildAntibodyDetectionDetails() {
        return postImmunizationAdverseEventsMildAntibodyDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsMildAntigenDetectionDetails() {
        return postImmunizationAdverseEventsMildAntigenDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsMildRapidTestDetails() {
        return postImmunizationAdverseEventsMildRapidTestDetails;
    }

    public String getPostImmunizationAdverseEventsMildCultureDetails() {
        return postImmunizationAdverseEventsMildCultureDetails;
    }

    public String getPostImmunizationAdverseEventsMildHistopathologyDetails() {
        return postImmunizationAdverseEventsMildHistopathologyDetails;
    }

    public String getPostImmunizationAdverseEventsMildIsolationDetails() {
        return postImmunizationAdverseEventsMildIsolationDetails;
    }

    public String getPostImmunizationAdverseEventsMildIgmSerumAntibodyDetails() {
        return postImmunizationAdverseEventsMildIgmSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsMildIggSerumAntibodyDetails() {
        return postImmunizationAdverseEventsMildIggSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsMildIgaSerumAntibodyDetails() {
        return postImmunizationAdverseEventsMildIgaSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsMildIncubationTimeDetails() {
        return postImmunizationAdverseEventsMildIncubationTimeDetails;
    }

    public String getPostImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails() {
        return postImmunizationAdverseEventsMildIndirectFluorescentAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails() {
        return postImmunizationAdverseEventsMildDirectFluorescentAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsMildMicroscopyDetails() {
        return postImmunizationAdverseEventsMildMicroscopyDetails;
    }

    public String getPostImmunizationAdverseEventsMildNeutralizingAntibodiesDetails() {
        return postImmunizationAdverseEventsMildNeutralizingAntibodiesDetails;
    }

    public String getPostImmunizationAdverseEventsMildPcrRtPcrDetails() {
        return postImmunizationAdverseEventsMildPcrRtPcrDetails;
    }

    public String getPostImmunizationAdverseEventsMildGramStainDetails() {
        return postImmunizationAdverseEventsMildGramStainDetails;
    }

    public String getPostImmunizationAdverseEventsMildLatexAgglutinationDetails() {
        return postImmunizationAdverseEventsMildLatexAgglutinationDetails;
    }

    public String getPostImmunizationAdverseEventsMildCqValueDetectionDetails() {
        return postImmunizationAdverseEventsMildCqValueDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsMildSequencingDetails() {
        return postImmunizationAdverseEventsMildSequencingDetails;
    }

    public String getPostImmunizationAdverseEventsMildDnaMicroarrayDetails() {
        return postImmunizationAdverseEventsMildDnaMicroarrayDetails;
    }

    public String getPostImmunizationAdverseEventsMildOtherDetails() {
        return postImmunizationAdverseEventsMildOtherDetails;
    }

    public String getPostImmunizationAdverseEventsSevereAntibodyDetection() {
        return postImmunizationAdverseEventsSevereAntibodyDetection;
    }

    public String getPostImmunizationAdverseEventsSevereAntigenDetection() {
        return postImmunizationAdverseEventsSevereAntigenDetection;
    }

    public String getPostImmunizationAdverseEventsSevereRapidTest() {
        return postImmunizationAdverseEventsSevereRapidTest;
    }

    public String getPostImmunizationAdverseEventsSevereCulture() {
        return postImmunizationAdverseEventsSevereCulture;
    }

    public String getPostImmunizationAdverseEventsSevereHistopathology() {
        return postImmunizationAdverseEventsSevereHistopathology;
    }

    public String getPostImmunizationAdverseEventsSevereIsolation() {
        return postImmunizationAdverseEventsSevereIsolation;
    }

    public String getPostImmunizationAdverseEventsSevereIgmSerumAntibody() {
        return postImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIggSerumAntibody() {
        return postImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIgaSerumAntibody() {
        return postImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereIncubationTime() {
        return postImmunizationAdverseEventsSevereIncubationTime;
    }

    public String getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody() {
        return postImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereDirectFluorescentAntibody() {
        return postImmunizationAdverseEventsSevereDirectFluorescentAntibody;
    }

    public String getPostImmunizationAdverseEventsSevereMicroscopy() {
        return postImmunizationAdverseEventsSevereMicroscopy;
    }

    public String getPostImmunizationAdverseEventsSevereNeutralizingAntibodies() {
        return postImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    public String getPostImmunizationAdverseEventsSeverePcrRtPcr() {
        return postImmunizationAdverseEventsSeverePcrRtPcr;
    }

    public String getPostImmunizationAdverseEventsSevereGramStain() {
        return postImmunizationAdverseEventsSevereGramStain;
    }

    public String getPostImmunizationAdverseEventsSevereLatexAgglutination() {
        return postImmunizationAdverseEventsSevereLatexAgglutination;
    }

    public String getPostImmunizationAdverseEventsSevereCqValueDetection() {
        return postImmunizationAdverseEventsSevereCqValueDetection;
    }

    public String getPostImmunizationAdverseEventsSevereSequencing() {
        return postImmunizationAdverseEventsSevereSequencing;
    }

    public String getPostImmunizationAdverseEventsSevereDnaMicroarray() {
        return postImmunizationAdverseEventsSevereDnaMicroarray;
    }

    public String getPostImmunizationAdverseEventsSevereOther() {
        return postImmunizationAdverseEventsSevereOther;
    }

    public String getPostImmunizationAdverseEventsSevereAntibodyDetectionDetails() {
        return postImmunizationAdverseEventsSevereAntibodyDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsSevereAntigenDetectionDetails() {
        return postImmunizationAdverseEventsSevereAntigenDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsSevereRapidTestDetails() {
        return postImmunizationAdverseEventsSevereRapidTestDetails;
    }

    public String getPostImmunizationAdverseEventsSevereCultureDetails() {
        return postImmunizationAdverseEventsSevereCultureDetails;
    }

    public String getPostImmunizationAdverseEventsSevereHistopathologyDetails() {
        return postImmunizationAdverseEventsSevereHistopathologyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIsolationDetails() {
        return postImmunizationAdverseEventsSevereIsolationDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIgmSerumAntibodyDetails() {
        return postImmunizationAdverseEventsSevereIgmSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIggSerumAntibodyDetails() {
        return postImmunizationAdverseEventsSevereIggSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIgaSerumAntibodyDetails() {
        return postImmunizationAdverseEventsSevereIgaSerumAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIncubationTimeDetails() {
        return postImmunizationAdverseEventsSevereIncubationTimeDetails;
    }

    public String getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails() {
        return postImmunizationAdverseEventsSevereIndirectFluorescentAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails() {
        return postImmunizationAdverseEventsSevereDirectFluorescentAntibodyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereMicroscopyDetails() {
        return postImmunizationAdverseEventsSevereMicroscopyDetails;
    }

    public String getPostImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails() {
        return postImmunizationAdverseEventsSevereNeutralizingAntibodiesDetails;
    }

    public String getPostImmunizationAdverseEventsSeverePcrRtPcrDetails() {
        return postImmunizationAdverseEventsSeverePcrRtPcrDetails;
    }

    public String getPostImmunizationAdverseEventsSevereGramStainDetails() {
        return postImmunizationAdverseEventsSevereGramStainDetails;
    }

    public String getPostImmunizationAdverseEventsSevereLatexAgglutinationDetails() {
        return postImmunizationAdverseEventsSevereLatexAgglutinationDetails;
    }

    public String getPostImmunizationAdverseEventsSevereCqValueDetectionDetails() {
        return postImmunizationAdverseEventsSevereCqValueDetectionDetails;
    }

    public String getPostImmunizationAdverseEventsSevereSequencingDetails() {
        return postImmunizationAdverseEventsSevereSequencingDetails;
    }

    public String getPostImmunizationAdverseEventsSevereDnaMicroarrayDetails() {
        return postImmunizationAdverseEventsSevereDnaMicroarrayDetails;
    }

    public String getPostImmunizationAdverseEventsSevereOtherDetails() {
        return postImmunizationAdverseEventsSevereOtherDetails;
    }

    public String getFhaAntibodyDetection() {
        return fhaAntibodyDetection;
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

    public String getFhaDirectFluorescentAntibody() {
        return fhaDirectFluorescentAntibody;
    }

    public String getFhaMicroscopy() {
        return fhaMicroscopy;
    }

    public String getFhaNeutralizingAntibodies() {
        return fhaNeutralizingAntibodies;
    }

    public String getFhaPcrRtPcr() {
        return fhaPcrRtPcr;
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

    public String getFhaSequencing() {
        return fhaSequencing;
    }

    public String getFhaDnaMicroarray() {
        return fhaDnaMicroarray;
    }

    public String getFhaOther() {
        return fhaOther;
    }

    public String getFhaAntibodyDetectionDetails() {
        return fhaAntibodyDetectionDetails;
    }

    public String getFhaAntigenDetectionDetails() {
        return fhaAntigenDetectionDetails;
    }

    public String getFhaRapidTestDetails() {
        return fhaRapidTestDetails;
    }

    public String getFhaCultureDetails() {
        return fhaCultureDetails;
    }

    public String getFhaHistopathologyDetails() {
        return fhaHistopathologyDetails;
    }

    public String getFhaIsolationDetails() {
        return fhaIsolationDetails;
    }

    public String getFhaIgmSerumAntibodyDetails() {
        return fhaIgmSerumAntibodyDetails;
    }

    public String getFhaIggSerumAntibodyDetails() {
        return fhaIggSerumAntibodyDetails;
    }

    public String getFhaIgaSerumAntibodyDetails() {
        return fhaIgaSerumAntibodyDetails;
    }

    public String getFhaIncubationTimeDetails() {
        return fhaIncubationTimeDetails;
    }

    public String getFhaIndirectFluorescentAntibodyDetails() {
        return fhaIndirectFluorescentAntibodyDetails;
    }

    public String getFhaDirectFluorescentAntibodyDetails() {
        return fhaDirectFluorescentAntibodyDetails;
    }

    public String getFhaMicroscopyDetails() {
        return fhaMicroscopyDetails;
    }

    public String getFhaNeutralizingAntibodiesDetails() {
        return fhaNeutralizingAntibodiesDetails;
    }

    public String getFhaPcrRtPcrDetails() {
        return fhaPcrRtPcrDetails;
    }

    public String getFhaGramStainDetails() {
        return fhaGramStainDetails;
    }

    public String getFhaLatexAgglutinationDetails() {
        return fhaLatexAgglutinationDetails;
    }

    public String getFhaCqValueDetectionDetails() {
        return fhaCqValueDetectionDetails;
    }

    public String getFhaSequencingDetails() {
        return fhaSequencingDetails;
    }

    public String getFhaDnaMicroarrayDetails() {
        return fhaDnaMicroarrayDetails;
    }

    public String getFhaOtherDetails() {
        return fhaOtherDetails;
    }

    public String getOtherAntibodyDetection() {
        return otherAntibodyDetection;
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

    public String getOtherDirectFluorescentAntibody() {
        return otherDirectFluorescentAntibody;
    }

    public String getOtherMicroscopy() {
        return otherMicroscopy;
    }

    public String getOtherNeutralizingAntibodies() {
        return otherNeutralizingAntibodies;
    }

    public String getOtherPcrRtPcr() {
        return otherPcrRtPcr;
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

    public String getOtherSequencing() {
        return otherSequencing;
    }

    public String getOtherDnaMicroarray() {
        return otherDnaMicroarray;
    }

    public String getOtherOther() {
        return otherOther;
    }

    public String getOtherAntibodyDetectionDetails() {
        return otherAntibodyDetectionDetails;
    }

    public String getOtherAntigenDetectionDetails() {
        return otherAntigenDetectionDetails;
    }

    public String getOtherRapidTestDetails() {
        return otherRapidTestDetails;
    }

    public String getOtherCultureDetails() {
        return otherCultureDetails;
    }

    public String getOtherHistopathologyDetails() {
        return otherHistopathologyDetails;
    }

    public String getOtherIsolationDetails() {
        return otherIsolationDetails;
    }

    public String getOtherIgmSerumAntibodyDetails() {
        return otherIgmSerumAntibodyDetails;
    }

    public String getOtherIggSerumAntibodyDetails() {
        return otherIggSerumAntibodyDetails;
    }

    public String getOtherIgaSerumAntibodyDetails() {
        return otherIgaSerumAntibodyDetails;
    }

    public String getOtherIncubationTimeDetails() {
        return otherIncubationTimeDetails;
    }

    public String getOtherIndirectFluorescentAntibodyDetails() {
        return otherIndirectFluorescentAntibodyDetails;
    }

    public String getOtherDirectFluorescentAntibodyDetails() {
        return otherDirectFluorescentAntibodyDetails;
    }

    public String getOtherMicroscopyDetails() {
        return otherMicroscopyDetails;
    }

    public String getOtherNeutralizingAntibodiesDetails() {
        return otherNeutralizingAntibodiesDetails;
    }

    public String getOtherPcrRtPcrDetails() {
        return otherPcrRtPcrDetails;
    }

    public String getOtherGramStainDetails() {
        return otherGramStainDetails;
    }

    public String getOtherLatexAgglutinationDetails() {
        return otherLatexAgglutinationDetails;
    }

    public String getOtherCqValueDetectionDetails() {
        return otherCqValueDetectionDetails;
    }

    public String getOtherSequencingDetails() {
        return otherSequencingDetails;
    }

    public String getOtherDnaMicroarrayDetails() {
        return otherDnaMicroarrayDetails;
    }

    public String getOtherOtherDetails() {
        return otherOtherDetails;
    }

    public String getUndefinedAntibodyDetection() {
        return undefinedAntibodyDetection;
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

    public String getUndefinedDirectFluorescentAntibody() {
        return undefinedDirectFluorescentAntibody;
    }

    public String getUndefinedMicroscopy() {
        return undefinedMicroscopy;
    }

    public String getUndefinedNeutralizingAntibodies() {
        return undefinedNeutralizingAntibodies;
    }

    public String getUndefinedPcrRtPcr() {
        return undefinedPcrRtPcr;
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

    public String getUndefinedSequencing() {
        return undefinedSequencing;
    }

    public String getUndefinedDnaMicroarray() {
        return undefinedDnaMicroarray;
    }

    public String getUndefinedOther() {
        return undefinedOther;
    }

    public String getUndefinedAntibodyDetectionDetails() {
        return undefinedAntibodyDetectionDetails;
    }

    public String getUndefinedAntigenDetectionDetails() {
        return undefinedAntigenDetectionDetails;
    }

    public String getUndefinedRapidTestDetails() {
        return undefinedRapidTestDetails;
    }

    public String getUndefinedCultureDetails() {
        return undefinedCultureDetails;
    }

    public String getUndefinedHistopathologyDetails() {
        return undefinedHistopathologyDetails;
    }

    public String getUndefinedIsolationDetails() {
        return undefinedIsolationDetails;
    }

    public String getUndefinedIgmSerumAntibodyDetails() {
        return undefinedIgmSerumAntibodyDetails;
    }

    public String getUndefinedIggSerumAntibodyDetails() {
        return undefinedIggSerumAntibodyDetails;
    }

    public String getUndefinedIgaSerumAntibodyDetails() {
        return undefinedIgaSerumAntibodyDetails;
    }

    public String getUndefinedIncubationTimeDetails() {
        return undefinedIncubationTimeDetails;
    }

    public String getUndefinedIndirectFluorescentAntibodyDetails() {
        return undefinedIndirectFluorescentAntibodyDetails;
    }

    public String getUndefinedDirectFluorescentAntibodyDetails() {
        return undefinedDirectFluorescentAntibodyDetails;
    }

    public String getUndefinedMicroscopyDetails() {
        return undefinedMicroscopyDetails;
    }

    public String getUndefinedNeutralizingAntibodiesDetails() {
        return undefinedNeutralizingAntibodiesDetails;
    }

    public String getUndefinedPcrRtPcrDetails() {
        return undefinedPcrRtPcrDetails;
    }

    public String getUndefinedGramStainDetails() {
        return undefinedGramStainDetails;
    }

    public String getUndefinedLatexAgglutinationDetails() {
        return undefinedLatexAgglutinationDetails;
    }

    public String getUndefinedCqValueDetectionDetails() {
        return undefinedCqValueDetectionDetails;
    }

    public String getUndefinedSequencingDetails() {
        return undefinedSequencingDetails;
    }

    public String getUndefinedDnaMicroarrayDetails() {
        return undefinedDnaMicroarrayDetails;
    }

    public String getUndefinedOtherDetails() {
        return undefinedOtherDetails;
    }




}
