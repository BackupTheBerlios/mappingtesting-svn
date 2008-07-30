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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.core.MemoryManager;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.gui.MessagePanel;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestCaseExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;
import org.qualipso.interop.semantics.mappingtesting.model.TestReport;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * This class executes the test cases in the test project.
 * 
 */
public class TestCaseExecutionEngine {

    private static final String SWRL_CREATED = "SWRLCreated";

    private TestProject testProject = null;
    private TestReport testReport = null;

    private RuleExecutionEngine ruleExecutionEngine;

    public TestCaseExecutionEngine(TestProject tProject) {
        testProject = tProject;
        testReport = new TestReport(testProject);
        testProject.setTestReport(testReport);
        ruleExecutionEngine = new RuleExecutionEngine(testProject);
    }

    /**
     * Executes all the testCases in the testProject.
     */
    public void executeAllTestCases() {
        Vector<TestCase> testCases = testProject.getTestCases();
        for (Iterator iter = testCases.iterator(); iter.hasNext();) {
            
            TestCase testCase = (TestCase) iter.next();
            
            MessagePanel.appendOut("\tExecuting test case ");
            MessagePanel.appendOutBlue(testCase.getName());
            MessagePanel.appendOut(" ... ");;            
            executeTestCase(testCase);
            MessagePanel.appendSuccessln("done.");
        }

    }


    /**
     * This is a private method, which is called only from this class. Executes
     * a testCase in the testingProject.
     * @param testCase
     */
    private void executeTestCase(TestCase testCase) {
        // initialize ruleExecMonitor
        RuleExecutionMonitor ruleExecMonitor = new RuleExecutionMonitor();
        ruleExecMonitor.init(testProject, testCase);

        // initialize tcExecResult
        TestCaseExecutionResult tcExecResult = new TestCaseExecutionResult(testCase.getName());
        testProject.getTestReport().addTestCaseExecutionResult(testCase.getName(), tcExecResult);
        ruleExecMonitor.setRuleExecutionResult(tcExecResult);

        // create a clone of inputOWLModel (inputOWLModelClone),
        // containing the instances of the src. ontology, its default
        // namespace is specified in registeredNamespaces.
        JenaOWLModel inputOWLModel = testCase.getInputInstancesOWLModelAsserted();
        JenaOWLModel inputOWLModelClone = null;        inputOWLModelClone = UtilsForProtegeOWL.cloneJenaOWLModel(inputOWLModel);        
        // perform consistency check of inputOWLModelClone
        ProtegeReasoner inputReasoner = UtilsForProtegeOWL
                .createProtegePelletReasoner(inputOWLModelClone);
        String inputConsistencyCheckResult = UtilsForProtegeOWL.performConsistencyCheck(
                inputReasoner, inputOWLModelClone);
        tcExecResult.setInputConsistencyCheckResult(inputConsistencyCheckResult);

        // perform consistency check of expectedOutputOWLModel
        JenaOWLModel expectedOutputOWLModel = testCase.getExpectedOutputInstancesOWLModelAsserted();
        ProtegeReasoner outputReasoner = UtilsForProtegeOWL
                .createProtegePelletReasoner(expectedOutputOWLModel);
        String outputConsistencyCheckResult = UtilsForProtegeOWL.performConsistencyCheck(
                outputReasoner, expectedOutputOWLModel);
        tcExecResult.setOutputConsistencyCheckResult(outputConsistencyCheckResult);
   
        try {
            ruleExecMonitor.importMappingModelFor1TestCaseExec(inputOWLModelClone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ruleExecutionEngine.executeRules(inputOWLModelClone);

        ruleExecMonitor.determineRuleExecInfo(inputOWLModelClone);
        JenaOWLModel resultingOWLModel = ruleExecutionEngine.createResultingOWLModel(
                inputOWLModelClone, ruleExecMonitor);

        ruleExecMonitor.deleteFileFor1TCExec();

        tcExecResult.setResultingOWLModelAsserted(resultingOWLModel);
        ProtegeReasoner resultingOWLModelReasoner = UtilsForProtegeOWL
                .createProtegePelletReasoner(resultingOWLModel);

        String resModelConsCheckResult = UtilsForProtegeOWL.performConsistencyCheck(
                resultingOWLModelReasoner, resultingOWLModel);
        tcExecResult.setResultingModelConsistencyCheckResult(resModelConsCheckResult);

        JenaOWLModel inferredResultingOWLModel = Controller.getInstance().getInferredModel(
                resultingOWLModel, tcExecResult, TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);

        Vector<Statement> missingStatements = getMissingStatements(expectedOutputOWLModel,
                inferredResultingOWLModel);

        // Is true, if expectedOutputOWLModel is a subset of
        // inferredResultingOWLModel,
        // i.e. if all the statements contained in expectedOutputOWLModel
        // are also contained in inferredResultingOWLModel.
        boolean isSubset = (missingStatements.size() == 0);
        tcExecResult.setTestCaseSucceeded(isSubset);
        tcExecResult.setMissingStatements(missingStatements);
        
        int firedRulesSize =  tcExecResult.getFiredRules().size(); 
        int overallNumberOfRules = testProject.getOntologyMapping().getMappingRules().size();
        int tcCoverage = 100 * firedRulesSize / overallNumberOfRules;
        tcExecResult.setTestCaseCoverage(tcCoverage);
        
//        inputOWLModelClone.dispose();
        MemoryManager.gc10();
    }

    /**
     * Returns a Vector of statements contained in expectedOutputOWLModel, which
     * are NOT contained in inferredResultingOWLModel.
     * 
     * @param expectedOutputOWLModel
     * @param inferredResultingOWLModel
     * @return
     */
    private Vector<Statement> getMissingStatements(JenaOWLModel expectedOutputOWLModel,
            JenaOWLModel inferredResultingOWLModel) {
        Vector<Statement> missingStatements = new Vector<Statement>();
        OntModel resultingOntModelTemp = inferredResultingOWLModel.getOntModel();
        OntModel expOutputOntModel = expectedOutputOWLModel.getOntModel();

        OntModelSpec oms = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        OntModel resultingOntModel = ModelFactory.createOntologyModel(oms, resultingOntModelTemp);

        // // This is also an alternative to get the list of statements in
        // expOutputOntModel,
        // // but this way many statements are returned, which are not made
        // about ontology
        // // instances, but about ontology definition (classes, properties,
        // etc).
        // // If it is decided later to use this code, then code before "while
        // (statements.hasNext())" can be deleted.
        // StmtIterator statements = expOutputOntModel.listStatements();

        List individuals = expOutputOntModel.listIndividuals().toList();
        for (Iterator iter = individuals.iterator(); iter.hasNext();) {
            Individual individual = (Individual) iter.next();
            StmtIterator statements = individual.listProperties();
            while (statements.hasNext()) {
                Statement statement = statements.nextStatement();

                // Most probably this code is not needed, if
                // StmtIterator statements = expOutputOntModel.listStatements();
                // is not used to determine the list of all statements, which
                // have to be checked.
                if (statement.getSubject().getURI() == null
                        || statement.getPredicate().getURI() == null
                        || (statement.getObject() instanceof Resource && ((Resource) statement
                                .getObject()).getURI() == null)) {
                    continue;
                }

                Resource s = null;
                Property p = statement.getPredicate();
                RDFNode o = null;

                // check if subject and object have been created by an SWRL rule
                // (e.g. SWRLCreated_1)
                // and if so, than replace them with a wildcard (null)
                boolean isSWRLCreatedSubject = isSWRLCreated(statement.getSubject());
                if (!isSWRLCreatedSubject)
                    s = statement.getSubject();

                if (statement.getObject() instanceof Resource) {
                    boolean isSWRLCreatedObject = isSWRLCreated((Resource) statement.getObject());
                    if (!isSWRLCreatedObject)
                        o = statement.getObject();
                } else
                    o = statement.getObject();

                boolean containsSPO = resultingOntModel.contains(s, p, o);
                if (!containsSPO)
                    missingStatements.add(statement);
            }
        }
        return missingStatements;
    }

    private boolean isSWRLCreated(Resource resource) {
        if (resource.getURI() == null || resource.getURI().indexOf(SWRL_CREATED) == -1)
            return false;
        return true;
    }

}
