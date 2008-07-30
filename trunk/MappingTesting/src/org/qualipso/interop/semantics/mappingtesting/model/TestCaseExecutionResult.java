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

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Statement;

/**
 * This class represents a data structure for saving information about execution
 * result of a particular test case.
 */
public class TestCaseExecutionResult extends RulesExecutionResult {

    private String testCaseName;
    private boolean testCaseSucceeded;
    private String outputConsistencyCheckResult;

    private Vector<Statement> missingStatements;
    int testCaseCoverage;

    public TestCaseExecutionResult(String testCaseName) {
        super();
        this.testCaseName = testCaseName;
        this.missingStatements = new Vector<Statement>();
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public boolean isTestCaseSucceeded() {
        return testCaseSucceeded;
    }

    public void setTestCaseSucceeded(boolean testCaseSucceeded) {
        this.testCaseSucceeded = testCaseSucceeded;
    }

    public String getOutputConsistencyCheckResult() {
        return outputConsistencyCheckResult;
    }

    public void setOutputConsistencyCheckResult(String outputConsistencyCheckResult) {
        this.outputConsistencyCheckResult = outputConsistencyCheckResult;
    }

    /**
     * Returns "succeeded", if the test case has succeeded and "failed", if the
     * test case has failed.
     * 
     * @return
     */
    public String getTestSucceededOrFailed() {
        if (testCaseSucceeded){
            if (resultingModelConsistencyCheckResult.indexOf("WARNING: There are following inconsistent classes in the model:") == -1)
                return "succeeded";
            else 
                return "succeeded, but resulting instanceas are not consistent with ontology definitions";
        }            
        else if (!testCaseSucceeded)
            return "failed";
        else
            return null;
    }

    /**
     * @param missingStatements
     *            a Vector of statements contained in expectedOutputOWLModel,
     *            which are NOT contained in inferredResultingOWLModel.
     */
    public void setMissingStatements(Vector<Statement> missingStatements) {
        this.missingStatements = missingStatements;
    }

    /**
     * @return a Vector of statements contained in expectedOutputOWLModel, which
     *         are NOT contained in inferredResultingOWLModel.
     */
    public Vector<Statement> getMissingStatements() {
        return missingStatements;
    }

    public int getTestCaseCoverage() {
        return testCaseCoverage;
    }

    public void setTestCaseCoverage(int testCaseCoverage) {
        this.testCaseCoverage = testCaseCoverage;
    }
}
