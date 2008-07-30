/*
 * IST-FP6-034763 QualiPSo: QualiPSo is a unique alliance
 * of European, Brazilian and Chinese ICT industry players,
 * SMEs, governments and academics to help industries and
 * governments fuel innovation and competitiveness with Open
 * Source software. To meet that goal, the QualiPSo consortium
 * intends to define and implement the technologies, processes
 * and policies to facilitate the development and use of Open
 * Source software components, with the same level of trust
 * traditionally offered by proprietary software. QualiPSo is
 * partially funded by the European Commission under EU’s sixth
 * framework program (FP6), as part of the Information Society
 * Technologies (IST) initiative. 
 *
 * This program has been created as part of the QualiPSo work
 * package on "Semantic Interoperability". The basic idea of this work    
 * package is to demonstrate how semantic technologies can be used to
 * cope with the diversity and heterogeneity of software and services
 * in the OSS domain.
 *
 * You can redistribute this program and/or modify it under the terms
 * of the GNU Lesser General Public License version 3 (LGPL v3.0).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * You should have received a copy of the LGPL
 * along with this program; if not, please have a look at
 * http://www.gnu.org/licenses/lgpl.html
 * to obtain the full license text.
 *
 * Author of this program: 
 * Fraunhofer Institute FOKUS, http://www.fokus.fraunhofer.de
 *
 *
 */

package org.qualipso.interop.semantics.mappingtesting.core.ruleexecution;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.JenaOWLModelLoadingException;
import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;
import org.qualipso.interop.semantics.mappingtesting.model.RulesExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

/**
 * This file is used for getting information, which rules have been fired during
 * execution of a particular test case.
 * 
 */
public class RuleExecutionMonitor {

    /**
     * Path to the temporary file, which is created for every test case
     * execution. After the test case has been executed the file will be
     * deleted.
     */
    final public static String FILE_FOR_1_TCEXEC = "etc/mappingModelFor1TestCaseExec.owl";

    /**
     * Path to the ontology file for creation of reports, which rule has been
     * fired.
     */
    final public static String RULE_EXEC_MONITOR_ONTO_FILE = "etc/rule-execution-monitor.owl";

    /**
     * A prefix for the default namespace of rule execution monitor ontology.
     */
    final public static String RULE_EXEC_MONITOR_PREFIX = "reMonitor";

    private JenaOWLModel execMonitorOWLModel = null;

    /**
     * Default namespace always contains '#' at the end.
     */
    private String execMonitorDefaultNS;
    /**
     * This model contains all the mapping rules, to which also the atoms are
     * added, needed to determine which rules were fired.
     */
    private JenaOWLModel mappingModelFor1TestCaseExec;

    private TestProject testProject = null;
    private RulesExecutionResult ruleExecutionResult = null;

    

    public void init(TestProject tProject) {
        this.testProject = tProject;
        addRuleExecResults();
    }
    
    private TestCase testCase;
    public void init(TestProject tProject, TestCase tc) {
        this.testProject = tProject;
        this.testCase = tc;
        addRuleExecResults();
    }

    /**
     * Adds an atom RuleExecResults(?ruleExecResults) to the body of all rules
     * and an atom wasFired(?ruleExecResults, "ruleName") to the head of all
     * rules.
     */
    private void addRuleExecResults() {
        try {
            loadExecMonitorOWLModel();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JenaOWLModel initialMappingOWLModel = testProject.getOntologyMapping().getJenaOWLModel();
        mappingModelFor1TestCaseExec = UtilsForProtegeOWL
                .cloneJenaOWLModelContainingRules(initialMappingOWLModel);

        // Import an ontology, which is saved in RULE_EXEC_MONITOR_ONTO_FILE
        // into mappingFor1TestCaseExec.
        String programBasePathURI = MiscUtils.determineProgramBasePathURI();
        String execMonitorFileURI = programBasePathURI + "/" + RULE_EXEC_MONITOR_ONTO_FILE;
        LocalFileRepository lfr = new LocalFileRepository(new File(RULE_EXEC_MONITOR_ONTO_FILE),
                true);
        mappingModelFor1TestCaseExec.getRepositoryManager().addProjectRepository(lfr);

        // First the prefix should be set, and then an import performed
        UtilsForProtegeOWL.setPrefixForNameSpace(RULE_EXEC_MONITOR_PREFIX, execMonitorDefaultNS,
                mappingModelFor1TestCaseExec);
        UtilsForProtegeOWL.importOntology(mappingModelFor1TestCaseExec, execMonitorFileURI);

        SWRLFactory swrlFactory = new SWRLFactory(mappingModelFor1TestCaseExec);
        SWRLVariable ruleExecutionResultsVar = swrlFactory
                .createVariable("ruleExecutionResultsVar");
        OWLNamedClass ruleExecutionResultsClass = mappingModelFor1TestCaseExec
                .getOWLNamedClass(RULE_EXEC_MONITOR_PREFIX + ":" + "RuleExecutionResults");
        SWRLClassAtom classAtom = swrlFactory.createClassAtom(ruleExecutionResultsClass,
                ruleExecutionResultsVar);

        Collection imps = swrlFactory.getImps();
        for (Iterator iter = imps.iterator(); iter.hasNext();) {
            SWRLImp imp = (SWRLImp) iter.next();
            addRuleExecResult(imp, swrlFactory, ruleExecutionResultsVar, classAtom);
        }
        
        String omDefaultNS = testProject.getOntologyMapping().getDefaultNamespace();
        OWLUtil.renameOntology(mappingModelFor1TestCaseExec, mappingModelFor1TestCaseExec.getDefaultOWLOntology(),
                omDefaultNS.substring(0, omDefaultNS.length()-1));

        UtilsForProtegeOWL.saveJenaOWLModel(mappingModelFor1TestCaseExec, new File(
                FILE_FOR_1_TCEXEC));
        
    }

    /**
     * Adds an atom RuleExecResults(?ruleExecResults) to the body of the rule
     * and an atom wasFired(?ruleExecResults, "ruleName") to the head of the
     * rule.
     * 
     * @param imp
     * @param swrlFactory
     * @param classAtom
     * @param ruleExecutionResultsVar
     * @param mappingFor1TestCaseExec
     */
    private void addRuleExecResult(SWRLImp imp, SWRLFactory swrlFactory,
            SWRLVariable ruleExecutionResultsVar, SWRLClassAtom classAtom) {
        imp.getBody().append(classAtom);

        RDFSLiteral ruleName = mappingModelFor1TestCaseExec.createRDFSLiteral(imp.getName());
        OWLDatatypeProperty ruleWasFiredProp = mappingModelFor1TestCaseExec
                .getOWLDatatypeProperty(RULE_EXEC_MONITOR_PREFIX + ":" + "ruleWasFired");
        SWRLDatavaluedPropertyAtom datavaluedPropertyAtom = swrlFactory
                .createDatavaluedPropertyAtom(ruleWasFiredProp, ruleExecutionResultsVar, ruleName);
        imp.getHead().append(datavaluedPropertyAtom);
    }

    /**
     * Loads the ontology from file RULE_EXEC_MONITOR_ONTO_FILE, which contains
     * RuleExecutionResults class and wasFired property.
     */
    private void loadExecMonitorOWLModel()
    // throws Exception
    {
        String programBasePathURI = MiscUtils.determineProgramBasePathURI();
        String execMonitorFileURI = programBasePathURI + "/" + RULE_EXEC_MONITOR_ONTO_FILE;
        try {
            execMonitorOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(execMonitorFileURI, null);
        } catch (JenaOWLModelLoadingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        execMonitorDefaultNS = execMonitorOWLModel.getNamespaceManager().getDefaultNamespace();
    }

    /**
     * Determines, which rules have been fired.
     * 
     * @param transformationResultOWLModel
     */
    public void determineRuleExecInfo(JenaOWLModel transformationResultOWLModel) {
        OWLIndividual ruleExecutionResultsIndividual = transformationResultOWLModel
                .getOWLIndividual(RULE_EXEC_MONITOR_PREFIX + ":" + "RuleExecutionResults_Instance");

        // Map<String, MappingRule> mappingRules =
        // testingProject.getOntologyMapping().getMappingRules();
        OntologyMapping om = testProject.getOntologyMapping();
        OWLProperty ruleWasFiredProperty = transformationResultOWLModel
                .getOWLProperty(RULE_EXEC_MONITOR_PREFIX + ":" + "ruleWasFired");
        Collection propValues = ruleExecutionResultsIndividual
                .getPropertyValues(ruleWasFiredProperty);
        for (Iterator iter = propValues.iterator(); iter.hasNext();) {
            Object propValue = (Object) iter.next();
            if (propValue instanceof String) {
                String supposedRuleName = (String) propValue;
                MappingRule mRule = om.getRule(supposedRuleName);
                ruleExecutionResult.addFiredRule(mRule);
                // print("mRule.getRuleText()", mRule.getRuleText());
            }
        }
    }

    /**
     * Imports a model for 1 test case execution, which is saved in a file
     * FILE_FOR_1_TCEXEC into toOWLModel. 
     * 
     * @param toOWLModel
     * @throws Exception
     */
    public void importMappingModelFor1TestCaseExec(JenaOWLModel toOWLModel) throws Exception {
        // copy all Repositories, Prefixes and Imports from
        // mappingModelFor1TestCaseExec into toOWLModel
        UtilsForProtegeOWL
                .copyRepositoriesPrefixesImports(mappingModelFor1TestCaseExec, toOWLModel);
       
        // After all its repositories etc., have been copied,
        // mappingModelFor1TestCaseExec can be imported:
        

        // add repository
//        File fileToBeImported = new File(RuleExecutionMonitor.FILE_FOR_1_TCEXEC);
//        LocalFileRepository local = new LocalFileRepository(fileToBeImported);
//        toOWLModel.getRepositoryManager().addProjectRepository(local);
        URI mappingOntNS = new URI(testProject.getOntologyMapping().getDefaultNamespace());

        // set prefix
        NamespaceManager toOWLModelNSManager = toOWLModel.getNamespaceManager();
        toOWLModelNSManager.setPrefix(mappingOntNS, testProject.MAPPING_NS_PREFIX);
        
        // import ontology
        ImportHelper toOWLModelImportHelper = new ImportHelper(toOWLModel);
        URI fileFor1RuleExecURI = URIUtilities.createURI(MiscUtils.determineProgramBasePathURI()
                + "/" + FILE_FOR_1_TCEXEC);
        
        toOWLModelImportHelper.addImport(fileFor1RuleExecURI);
        toOWLModelImportHelper.importOntologies(false);
        
        String toOWLModelDefaultNS = toOWLModel.getNamespaceManager().getDefaultNamespace();
        OWLUtil.renameOntology(toOWLModel, toOWLModel.getDefaultOWLOntology(),
                toOWLModelDefaultNS.substring(0, toOWLModelDefaultNS.length()-1));
    
    }

    public String getExecMonitorDefaultNS() {
        return execMonitorDefaultNS;
    }

    public RulesExecutionResult getRuleExecutionResult() {
        return ruleExecutionResult;
    }

    public void setRuleExecutionResult(RulesExecutionResult ruleExecResult) {
        this.ruleExecutionResult = ruleExecResult;
    }

    /**
     * Deletes a file, which have been serialized to FILE_FOR_1_TCEXEC
     */
    public void deleteFileFor1TCExec() {
        File fileFor1TCExec = new File(FILE_FOR_1_TCEXEC);
        if (fileFor1TCExec != null && fileFor1TCExec.exists())
            fileFor1TCExec.delete();
        mappingModelFor1TestCaseExec.dispose();
        execMonitorOWLModel.dispose();
    }
}
