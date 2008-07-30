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
import java.util.Iterator;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.core.MemoryManager;
import org.qualipso.interop.semantics.mappingtesting.core.TestProjectLoader;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.RulesExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * RuleDebugger performs debugging of selected mapping rules by applying them to
 * the instances of the source ontology, defined in instancesOWLModel.
 * 
 */
public class RuleDebugger {

    private TestProject testProject;
    /**
     * Rules to be debugged.
     */
    private Vector<MappingRule> rulesToDebug;
    /**
     * A JenaOWLModel, which is used as input for debugging
     */
    private JenaOWLModel instancesOWLModel;
    private RuleExecutionEngine ruleExecutionEngine;
    private int fileNumber = 0;
    private TestProjectLoader testProjectLoader;

    /**
     * Creates a RuleDebugger for a specific testProject.
     * 
     * @param testProject
     * @param testProjectLoader 
     */
    public RuleDebugger(TestProject testProject, TestProjectLoader testProjectLoader) {
        this.testProject = testProject;
        Controller.getInstance().setRuleDebugger(this);
        this.ruleExecutionEngine = new RuleExecutionEngine(this.testProject);
        this.testProjectLoader = testProjectLoader;
    }


    public RulesExecutionResult debug() throws Exception {

        // Set prefix for swrla.owl ontology and import this ontology

        // String SWRLA_PREFIX = "swrla";
        // String SWRLA_ONTOLOGY_URI =
        // "http://swrl.stanford.edu/ontologies/3.3/swrla.owl";
        // UtilsForProtegeOWL.setPrefixForNameSpace(SWRLA_PREFIX,
        // SWRLA_ONTOLOGY_URI + "#", testProject.getMappingOWLModel());
        // UtilsForProtegeOWL.importOntology(testProject.getMappingOWLModel(),
        // SWRLA_ONTOLOGY_URI);

        // initialize ruleExecMonitor
        RuleExecutionMonitor ruleExecMonitor = new RuleExecutionMonitor();
        ruleExecMonitor.init(testProject);

        RulesExecutionResult ruleExecutionResult = new RulesExecutionResult();
        ruleExecMonitor.setRuleExecutionResult(ruleExecutionResult);

        // create a clone of instancesOWLModel (instancesOWLModelClone),
        // containing the instances of the src. ontology, its default
        // namespace is specified in registeredNamespaces.
        JenaOWLModel instancesOWLModelClone = UtilsForProtegeOWL
                .cloneJenaOWLModel(instancesOWLModel);

        // perform consistency check of inputOWLModelClone
        ProtegeReasoner inputReasoner = UtilsForProtegeOWL
                .createProtegePelletReasoner(instancesOWLModelClone);
        String inputConsistencyCheckResult = UtilsForProtegeOWL.performConsistencyCheck(
                inputReasoner, instancesOWLModelClone);
        ruleExecutionResult.setInputConsistencyCheckResult(inputConsistencyCheckResult);

        try {
            ruleExecMonitor.importMappingModelFor1TestCaseExec(instancesOWLModelClone);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ruleExecutionEngine.executeRules(instancesOWLModelClone, getMappingRuleNames());
        ruleExecMonitor.determineRuleExecInfo(instancesOWLModelClone);

        JenaOWLModel resultingOWLModel = ruleExecutionEngine.createResultingOWLModel(
                instancesOWLModelClone, ruleExecMonitor);

        ruleExecMonitor.deleteFileFor1TCExec();
        ruleExecutionResult.setResultingOWLModelAsserted(resultingOWLModel);
        ProtegeReasoner resultingOWLModelReasoner = UtilsForProtegeOWL
                .createProtegePelletReasoner(resultingOWLModel);

        String resModelConsCheckResult = UtilsForProtegeOWL.performConsistencyCheck(
                resultingOWLModelReasoner, resultingOWLModel);
        ruleExecutionResult.setResultingModelConsistencyCheckResult(resModelConsCheckResult);
        // saveResultingOntology(resultingOWLModel);
        
//        instancesOWLModelClone.dispose();
        MemoryManager.gc10();
        return ruleExecutionResult;
    }

    public void setRulesToDebug(Vector<MappingRule> selectedRules) {
        this.rulesToDebug = selectedRules;
    }


//    /**
//     * Returns a Vector, containing names of the rules, which should be
//     * debugged. To each rule name a prefix is added, which is used, when a
//     * mapping ontology is imported into other files.
//     * 
//     * @return
//     */
//    private Vector<String> getMappingRuleNamesWithPrefix() {
//        Vector<String> ruleNames = new Vector<String>();
//        for (Iterator iter = rulesToDebug.iterator(); iter.hasNext();) {
//            MappingRule mr = (MappingRule) iter.next();
//            ruleNames.add(TestProject.MAPPING_NS_PREFIX + ":" + mr.getName());
//        }
//        return ruleNames;
//    }
    
    /**
     * Returns a Vector, containing names of the rules, which should be
     * debugged. 
     * @return
     */
    private Vector<String> getMappingRuleNames() {
        Vector<String> ruleNames = new Vector<String>();
        for (Iterator iter = rulesToDebug.iterator(); iter.hasNext();) {
            MappingRule mr = (MappingRule) iter.next();
            ruleNames.add(mr.getName());
        }
        return ruleNames;
    }

    public JenaOWLModel getInstancesOWLModel() {
        return instancesOWLModel;
    }

    public void setInstancesOWLModel(JenaOWLModel instancesOWLModel) {
        this.instancesOWLModel = instancesOWLModel;
    }

}
