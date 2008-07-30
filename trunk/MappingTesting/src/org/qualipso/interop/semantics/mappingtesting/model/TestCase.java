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

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * This class represents a data structure for saving information
 * about test cases in the test project. 
 *
 */
public class TestCase {

	private String name;
	private String description;
	private JenaOWLModel inputInstancesOWLModelAsserted;
	private JenaOWLModel inputInstancesOWLModelInferred = null;
	private JenaOWLModel expectedOutputInstancesOWLModelAsserted;
	private JenaOWLModel expectedOutputInstancesOWLModelInferred = null;
	private TestProject testProject;
	
	/** 	
	 * The paths of input and expected output ontologies will be
	 * saved in the test case in the test project file.
	 */
	private String inputOntRelativePath;
	private String expectedOutputOntRelativePath;
		

	/**
     * Creates a test case having a specified name, testCaseDescription,
     * inputInstancesOWLModel and outputInstancesOWLModel.
	 * @param name
	 * @param testCaseDescription
	 * @param inputInstancesOWLModel
	 * @param outputInstancesOWLModel
	 */
	public TestCase(String name, String testCaseDescription, JenaOWLModel inputInstancesOWLModel, JenaOWLModel outputInstancesOWLModel) {
		this.name = name;
		this.description = testCaseDescription;
		this.inputInstancesOWLModelAsserted = inputInstancesOWLModel;
		this.expectedOutputInstancesOWLModelAsserted = outputInstancesOWLModel;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInputOntRelativePath() {
		return inputOntRelativePath;
	}
	
	public void setInputOntRelativePath(String path) {
		this.inputOntRelativePath = path;
	}

	public String getExpectedOutputOntRelativePath() {
		return expectedOutputOntRelativePath;
	}
	
	public void setExpectedOutputOntRelativePath(String path) {
		this.expectedOutputOntRelativePath = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JenaOWLModel getInputInstancesOWLModelAsserted() {
		return inputInstancesOWLModelAsserted;
	}

	public JenaOWLModel getExpectedOutputInstancesOWLModelAsserted() {
		return expectedOutputInstancesOWLModelAsserted;
	}

	public TestProject getTestProject() {
		return testProject;
	}

	public void setTestProject(TestProject testingProject) {
		this.testProject = testingProject;
	}
	
	public String getTestCaseRelativePath(){
		int slashLastIndex = inputOntRelativePath.lastIndexOf("/");
		return inputOntRelativePath.substring(0, slashLastIndex+1);
	}
	
	public String getTestCaseAbsPath(){		
		return testProject.getProjDirectoryAbsPath() + getTestCaseRelativePath();
	}
	
	public String toString(){
		return getName();
	}

	public JenaOWLModel getExpectedOutputInstancesOWLModelInferred() {
		return expectedOutputInstancesOWLModelInferred;
	}

	public void setExpectedOutputInstancesOWLModelInferred(
			JenaOWLModel expectedOutputInstancesOWLModelInferred) {
		this.expectedOutputInstancesOWLModelInferred = expectedOutputInstancesOWLModelInferred;
	}

	public JenaOWLModel getInputInstancesOWLModelInferred() {
		return inputInstancesOWLModelInferred;
	}

	public void setInputInstancesOWLModelInferred(
			JenaOWLModel inputInstancesOWLModelInferred) {
		this.inputInstancesOWLModelInferred = inputInstancesOWLModelInferred;
	}

    public void dispose() {
        if (inputInstancesOWLModelAsserted != null)
            inputInstancesOWLModelAsserted.dispose();
        if (inputInstancesOWLModelInferred != null)
            inputInstancesOWLModelInferred.dispose();
        if (expectedOutputInstancesOWLModelAsserted != null)
            expectedOutputInstancesOWLModelAsserted.dispose();
        if (expectedOutputInstancesOWLModelInferred != null)
            expectedOutputInstancesOWLModelInferred.dispose();       
    }
}
