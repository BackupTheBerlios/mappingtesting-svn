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

package org.qualipso.interop.semantics.mappingtesting.core;

import java.io.File;

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestCaseCreatorException;
import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestProjectLoaderException;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * TestCaseCreator creates new test cases and adds them to a test project.
 */
public class TestCaseCreator {

    private static final int INPUT_INSTANCES = 0;
    private static final int EXPECTED_OUTPUT_INSTANCES = 1;

    private TestProject testProject;
    private TestProjectLoader testProjectLoader;
    
    /**
     * Creates a new TestCaseCreator for the test project tp.
     * 
     * @param tp
     * @param projLoader 
     */
    public TestCaseCreator(TestProject tp, TestProjectLoader projLoader) {
        this.testProject = tp;
        this.testProjectLoader = projLoader;
    }


    /**
     * Creates a new test case, based on test case name, description and paths
     * to the files, containing test input instances and expected test output
     * instances.
     * 
     * @param testCaseName
     * @param testCaseDescription
     * @param inputInstancesFilePath
     * @param expOutputInstancesFilePath
     * @return a created test case
     * @throws TestCaseCreatorException 
     * @throws TestProjectLoaderException 
     */
    public TestCase createTestCase(String testCaseName, String testCaseDescription,
            String inputInstancesFilePath, String expOutputInstancesFilePath) throws TestCaseCreatorException, TestProjectLoaderException {
        // tested
        if (testProject.containsTestCase(testCaseName)) {
            throw new TestCaseCreatorException("Test case " + "with name \"" + testCaseName
                    + "\" already exists.", null);
        }
        
//      check that spaces are not contained in test project name and in 3 referenced files.
        if (testCaseName.indexOf(" ") != -1){
            throw new TestCaseCreatorException("Test project \"" + testCaseName + "\" could not be created, since " +
                    "test case name cannot contain spaces.", null);
        } 
       
        JenaOWLModel inputInstancesOWLModel = testProjectLoader.loadInputInstancesJenaOWLModel(inputInstancesFilePath);
        JenaOWLModel expOutputInstancesOWLModel = testProjectLoader.loadOutputInstancesJenaOWLModel(expOutputInstancesFilePath);

        TestCase testCase = new TestCase(testCaseName, testCaseDescription, inputInstancesOWLModel,
                expOutputInstancesOWLModel);

        // Create a directory into which instance files will be copied.
        String testCaseDirString = testProject.getProjDirectoryAbsPath() + testCaseName + "/";
        File testCaseDir = new File(testCaseDirString);
        if (!testCaseDir.exists())
            testCaseDir.mkdir();

        // Determine file names of input and expected output instances files.
        String inputInstacesFileName = new File(inputInstancesFilePath).getName();
        String expOutputInstacesFileName = new File(expOutputInstancesFilePath).getName();

        // copy files, containing instances into directory testCaseDirString.
        File copyOfInputInstacesFile = new File(testCaseDirString + inputInstacesFileName);
        UtilsForProtegeOWL.saveJenaOWLModel(inputInstancesOWLModel, copyOfInputInstacesFile);
        File copyOfExpOutputInstacesFile = new File(testCaseDirString + expOutputInstacesFileName);
        UtilsForProtegeOWL
                .saveJenaOWLModel(expOutputInstancesOWLModel, copyOfExpOutputInstacesFile);

        testCase.setInputOntRelativePath(testCaseName + "/" + inputInstacesFileName);
        testCase.setExpectedOutputOntRelativePath(testCaseName + "/" + expOutputInstacesFileName);

        testProject.addTestCase(testCase);
        testCase.setTestProject(testProject);

        return testCase;
    }

}
