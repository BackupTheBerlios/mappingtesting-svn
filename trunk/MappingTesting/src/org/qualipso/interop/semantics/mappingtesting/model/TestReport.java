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

package org.qualipso.interop.semantics.mappingtesting.model;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Statement;

/**
 * This class represents a data structure for saving information about test
 * report. This class is also used for generation of the test report.
 * 
 */
public class TestReport {

//    private static final String LINE = "______________________________________________________________________________________\n";
    private static final String LINE =          "-------------------------------------------------------------------------------------------------------------------------------------------------\n";
    private static final String DOUBLE_LINE =   "=================================================================================================================================================\n";
    
    private TestProject testProject;

    private Hashtable<String, TestCaseExecutionResult> testCaseExecutionResults = new Hashtable<String, TestCaseExecutionResult>(
            5);

    private String testReportText = null;

    public TestReport(TestProject tProject) {
        this.testProject = tProject;
    }

    public void addTestCaseExecutionResult(String testCaseName, TestCaseExecutionResult tcExecResult) {
        testCaseExecutionResults.put(testCaseName, tcExecResult);
    }

    public TestCaseExecutionResult getTestCaseExecutionResult(String testCaseName) {
        return testCaseExecutionResults.get(testCaseName);
    }

    /**
     * Generates a test report text about execution of all the test cases in the
     * test project.
     * 
     * @return
     */
    private String generateTestReportText() {
        String testReportSting = "\t\t\t\t\tTEST REPORT\n"+ DOUBLE_LINE +"\n\n";
        testReportSting += "Test project name:                                  " + testProject.getProjName() + "\n";
        testReportSting += "Test project file:                                  " + testProject.getProjFileName() + "\n";
        testReportSting += "Test project absolute file path:                    "
                + testProject.getProjDirectoryAbsPath() + testProject.getProjFileName() + "\n\n";

        testReportSting += "Source ontology namespace:                          "
                + testProject.getSourceOntology().getDefaultNamespace() + "\n";
        testReportSting += "Source ontology location:                           "
                + testProject.getSourceOntology().getRepositoryString() + "\n\n";

        testReportSting += "Target ontology namespace:                          "
                + testProject.getTargetOntology().getDefaultNamespace() + "\n";
        testReportSting += "Target ontology location:                           "
                + testProject.getTargetOntology().getRepositoryString() + "\n\n";

        testReportSting += "Ontology mapping namespace:                         " + testProject.getOntologyMapping().getDefaultNamespace()
                + "\n";
        testReportSting += "Ontology mapping location:                          "
                + testProject.getOntologyMapping().getRepositoryString() + "\n\n";

        testReportSting += "Overall test case coverage:                         " + getOverallTestCaseCoverage()
                + "%\n\n";
        testReportSting += "Number of test cases:                               " + testProject.getTestCasesNumber()
                + "\n\n";

        testReportSting += "Succeeded:                                          " + getNumberOfSucceededTestCases() + " of "
                + testProject.getTestCasesNumber() + "\n";
        testReportSting += "Following test cases have succeeded:                ";
//                         "Expected output instances consistency check result: "  
        Vector<String> succeededTestCases = getSucceededTestCasesNames();
        for (Iterator iter = succeededTestCases.iterator(); iter.hasNext();) {
            String tcName = (String) iter.next();
            testReportSting += tcName + "\t";
        }
        testReportSting += " \n";

        testReportSting += "Failed:                                             " + getNumberOfFailedTestCases() + " of "
                + testProject.getTestCasesNumber() + "\n";
        testReportSting += "Following test cases have failed:                   ";
        Vector<String> failedTestCases = getFailedTestCasesNames();
        for (Iterator iter = failedTestCases.iterator(); iter.hasNext();) {
            String tcName = (String) iter.next();
            testReportSting += tcName + "\t";
        }
        testReportSting += "\n";
        testReportSting += "\n";
        testReportSting += getTestCasesExecutionReport();
        return testReportSting;
    }

    /**
     * Generates test report about execution of all the test cases.
     * 
     * @return
     */
    private String getTestCasesExecutionReport() {
        String testCasesExecutionReport = "";
        Vector<TestCase> testCases = testProject.getTestCases();
        for (Iterator iter = testCases.iterator(); iter.hasNext();) {
            testCasesExecutionReport += "\n";
            TestCase tc = (TestCase) iter.next();
            TestCaseExecutionResult tcExecResult = testCaseExecutionResults.get(tc.getName());
            testCasesExecutionReport += getTestCaseExecutionReport(tc, tcExecResult);
        }
        return testCasesExecutionReport;
    }

    /**
     * Generates a report about the execution of the test case.
     * 
     * @param tc
     * @param tcExecResult
     * @return
     */
    private String getTestCaseExecutionReport(TestCase tc, TestCaseExecutionResult tcExecResult) {
        String tcExecReport = DOUBLE_LINE + "EXECUTION RESULT OF TEST CASE:  " + tc.getName() + "\n" + DOUBLE_LINE + "\n";
        tcExecReport += "Test case name:                                     " + tc.getName() + "\n";
        tcExecReport += "Test case description:                              " + tc.getDescription() + "\n\n";
        tcExecReport += "Test case execution result:                         " + tcExecResult.getTestSucceededOrFailed()
                + "\n\n";

        tcExecReport += "Input instances:                                    " + tc.getInputOntRelativePath() + "\n";
        tcExecReport += "Input instances consistency check result:           "
                + tcExecResult.getInputConsistencyCheckResult() + "\n\n";

        tcExecReport += "Expected output instances:                          " + tc.getExpectedOutputOntRelativePath() + "\n";
        tcExecReport += "Expected output instances consistency check result: "
                + tcExecResult.getOutputConsistencyCheckResult() + "\n\n";
        tcExecReport += "Resulting instances consistency check result:       "
            + tcExecResult.getResultingModelConsistencyCheckResult() + "\n\n";

        tcExecReport += "Test case coverage:                                 " + tcExecResult.getTestCaseCoverage()+ "%\n\n";
        tcExecReport += LINE;
        tcExecReport += "Following rules have been fired:\n\n";
        for (Iterator iter = tcExecResult.getFiredRules().iterator(); iter.hasNext();) {
            MappingRule mr = (MappingRule) iter.next();
            tcExecReport += mr.getLocalName() + ":\n\n" + mr.getRuleText() + "\n\n";
        }

        tcExecReport += LINE + "\n";

        tcExecReport += "Following statements, contained in expected output instances file are not present in an ontology, containing resulting instances:\n\n";
        if (tcExecResult.getMissingStatements().size() == 0)
            tcExecReport += "all statements are present.\n";
        else {
            for (Iterator iter = tcExecResult.getMissingStatements().iterator(); iter.hasNext();) {
                Statement stmt = (Statement) iter.next();
                tcExecReport += stmt.toString() + "\n\n";
            }
        }
        tcExecReport += "\n";
        return tcExecReport;
    }

    public int getNumberOfSucceededTestCases() {
        int succeeded = 0;
        Collection<TestCaseExecutionResult> tcExecResults = testCaseExecutionResults.values();
        for (Iterator iter = tcExecResults.iterator(); iter.hasNext();) {
            TestCaseExecutionResult tcExecResult = (TestCaseExecutionResult) iter.next();
            if (tcExecResult.isTestCaseSucceeded())
                succeeded++;
        }
        return succeeded;
    }

    public int getNumberOfFailedTestCases() {
        int failed = 0;
        Collection<TestCaseExecutionResult> tcExecResults = testCaseExecutionResults.values();
        for (Iterator iter = tcExecResults.iterator(); iter.hasNext();) {
            TestCaseExecutionResult tcExecResult = (TestCaseExecutionResult) iter.next();
            if (!tcExecResult.isTestCaseSucceeded())
                failed++;
        }
        return failed;
    }

    public Vector<String> getSucceededTestCasesNames() {
        Vector<String> succeededTestCases = new Vector<String>();
        Enumeration<String> keys = testCaseExecutionResults.keys();
        while (keys.hasMoreElements()) {
            String tcName = keys.nextElement();
            TestCaseExecutionResult tcExecResult = testCaseExecutionResults.get(tcName);
            if (tcExecResult.isTestCaseSucceeded())
                succeededTestCases.add(tcName);
        }
        return succeededTestCases;
    }

    public Vector<String> getFailedTestCasesNames() {
        Vector<String> failedTestCases = new Vector<String>();
        Enumeration<String> keys = testCaseExecutionResults.keys();
        while (keys.hasMoreElements()) {
            String tcName = keys.nextElement();
            TestCaseExecutionResult tcExecResult = testCaseExecutionResults.get(tcName);
            if (!tcExecResult.isTestCaseSucceeded())
                failedTestCases.add(tcName);
        }
        return failedTestCases;
    }
    
    public int getOverallTestCaseCoverage() {
        HashSet firedRulesSet = new HashSet();
        Collection<TestCaseExecutionResult> tcExecResults = testCaseExecutionResults.values();
        for (Iterator iter = tcExecResults.iterator(); iter.hasNext();) {
            TestCaseExecutionResult tcExecResult = (TestCaseExecutionResult) iter.next();
            Vector<MappingRule> firedRules = tcExecResult.getFiredRules();
            for (Iterator iterator = firedRules.iterator(); iterator.hasNext();) {
                MappingRule mappingRule = (MappingRule) iterator.next();
                firedRulesSet.add(mappingRule);
            }
        }
        if (firedRulesSet.size() == 0) return 0;
        int overallNumberOfRules = testProject.getOntologyMapping().getMappingRules().size();
        return 100 * firedRulesSet.size() / overallNumberOfRules;
    }

    public String getTestReportText() {
        if (testReportText == null)
            testReportText = generateTestReportText();
        return testReportText;
    }
    
    public void dispose(){
        Collection<TestCaseExecutionResult> tcExecResults = testCaseExecutionResults.values();
        for (Iterator iter = tcExecResults.iterator(); iter.hasNext();) {
            TestCaseExecutionResult tcExecResult = (TestCaseExecutionResult) iter.next();
            tcExecResult.dispose();
        }
    }
}
