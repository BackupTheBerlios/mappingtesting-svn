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

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestProjectLoaderException;
import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.model.Ontology;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

/**
 * TestProjectCreator is used for creating new test project.
 */
public class TestProjectCreator {
    
    private static final String TEST_PROJECT_DEFAULT_NS = "http://www.fokus.fraunhofer.de/testprojects/";

    /**
     * Creates a new test project, based on testProjName, testProjDirectory and paths
     * to source and target ontologies and ontology mapping.
     * 
     * @param testProjName
     * @param testProjDirectory
     * @param sourceOntologyPath
     * @param targetOntologyPath
     * @param ontologyMappingPath 
     * @return a created test project
     * @throws TestProjectLoaderException 
     */
    public TestProject createTestProject(String testProjName, String testProjDirectory,
            String sourceOntologyPath, String targetOntologyPath, String ontologyMappingPath) 
    throws TestProjectLoaderException
    {
        
        checkTestProjDirExists(testProjDirectory);
        
        TestProject testProject = new TestProject();
        String projDefaultNS = TEST_PROJECT_DEFAULT_NS + testProjName + ".owl#";
        testProject.setProjDefaultNS(projDefaultNS);
        
        String projectFilePath = testProjDirectory + "/" + testProjName + ".owl";
        String projectFileURI = MiscUtils.determineFilePathURI(projectFilePath);
        
        testProject.setProjDirectoryURI(TestProject.getProjDirectoryURI(projectFileURI));
        testProject.setProjFileName(TestProject.getProjFileName(projectFileURI));
        testProject.setProjName(testProjName);
        testProject.setProjDirectoryAbsPath(TestProject.getProjDirectoryAbsPath(projectFilePath));
        
//      copy source and target ontologies and an ontology mapping. Check whether the 3 ontologies can be loaded.
        if (! sourceOntologyPath.startsWith("http")){
            MiscUtils.copyFile(sourceOntologyPath, testProjDirectory);;
            sourceOntologyPath = (new File(sourceOntologyPath)).getName();
        }   
        Ontology sourceOntology = TestProjectLoader.loadSourceOntology(sourceOntologyPath, testProject);

        
        if (! targetOntologyPath.startsWith("http")){
            MiscUtils.copyFile(targetOntologyPath, testProjDirectory);;
            targetOntologyPath = (new File(targetOntologyPath)).getName();
        } 
        Ontology targetOntology = TestProjectLoader.loadTargetOntology(targetOntologyPath, testProject);

        
        if (! ontologyMappingPath.startsWith("http")){
            MiscUtils.copyFile(ontologyMappingPath, testProjDirectory);;
            ontologyMappingPath = (new File(ontologyMappingPath)).getName();
        } 
        Ontology ontologyMapping = TestProjectLoader.loadOntologyMapping(ontologyMappingPath, testProject);
        
        if (sourceOntology != null && 
            targetOntology != null && 
            ontologyMapping != null){
            
            return testProject;
        }        
        return null;
    }
    

    private void checkTestProjDirExists(String testProjDirectory) throws TestProjectLoaderException {
        File projDirectoryFile = new File(testProjDirectory);
        if (!projDirectoryFile.exists()){
            throw new TestProjectLoaderException ("Test project directory \"" +
                    testProjDirectory + "\" does not exist.", null);
        }
    }


    /**
     * Checks that all the parameters are not null and not an empty String.
     * @param testProjectName
     * @param testProjectDirectory
     * @param sourceOntologyPath
     * @param targetOntologyPath
     * @param ontologyMappingPath
     * @return "OK", if all the parameters are not empty; error message otherwise.
     */
    public String checkParametersNotEmpty(String testProjectName, String testProjectDirectory, 
            String sourceOntologyPath, String targetOntologyPath, String ontologyMappingPath) {
        String cannotBeEmpty = " cannot be empty.";
        if (testProjectName == null || testProjectName.equals(""))
            return "Test project name" + cannotBeEmpty;
        if (testProjectDirectory == null || testProjectDirectory.equals(""))
            return "Test project directory" + cannotBeEmpty;
        if (sourceOntologyPath == null || sourceOntologyPath.equals(""))
            return "Source ontology path" + cannotBeEmpty;
        if (targetOntologyPath == null || targetOntologyPath.equals(""))
            return "Target ontology path" + cannotBeEmpty;
        if (ontologyMappingPath == null || ontologyMappingPath.equals(""))
            return "Ontology mapping path" + cannotBeEmpty;

        return "OK";
    }

}
