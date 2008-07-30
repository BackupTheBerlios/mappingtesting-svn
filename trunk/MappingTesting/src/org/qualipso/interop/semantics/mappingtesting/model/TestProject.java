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

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class represents a data structure for saving information about test
 * project.
 * 
 */
public class TestProject {
    
    public static final int INSTANCES_TYPE_SOURCE_ONTOLOGY = 0;
    public static final int INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY = 1;
    
    /**
     * A file, where a test project ontology is saved. THE FILE CANNOT BE:
     * "/etc/testproject.owl" (with slash)
     */
    final public static String TEST_PROJECT_ONT_FILE = "etc/testproject.owl";
    
    /**
     * A prefix for the default namespace of the test project ontology.
     */
    final public static String TEST_PROJECT_ONTOLOGY_PREFIX = "test";    
    final public static String TEST_PROJECT_ONTOLOGY_DEFAULT_NS = "http://www.fokus.fraunhofer.de/testproject.owl";  
    
    /**
     * A namespace prefix, which is used, when a mapping ontology is imported
     * into other files.
     */
    public static final String MAPPING_NS_PREFIX = "mapping";
    
    public static final String TEST_PROJECT_CLASS_NAME = "TestProject";
    public static final String SOURCE_ONTOLOGY_PROPERTY_NAME = "sourceOntology";
    public static final String TARGET_ONTOLOGY_PROPERTY_NAME = "targetOntology";
    public static final String ONTOLOGY_MAPPING_PROPERTY_NAME = "ontologyMapping";
    public static final String HAS_TEST_CASE_PROPERTY_NAME = "hasTestCase";
    
    public static final String TEST_CASE_CLASS_NAME = "TestCase";
    public static final String NAME_PROPERTY_NAME = "name";
    public static final String DESCRIPTION_PROPERTY_NAME = "description";
    public static final String TEST_INPUT_INSTANCES_PROPERTY_NAME = "testInputInstances";
    public static final String EXPECTED_TEST_OUTPUT_INSTANCES_PROPERTY_NAME = "expectedTestOutputInstances";
         
    private String projName;

    /**
     * The URI of the directory, in which the project file, the source and
     * target ontology files are saved.
     */
    private String projDirectoryURI;

    /**
     * The name of the test project file, e.g. testproject1.owl
     */
    private String projFileName;
    
    /**
     * Default namespace of test project.
     */
    private String projDefaultNS;

    private Ontology sourceOntology;
    private Ontology targetOntology;
    private OntologyMapping ontologyMapping;

    private TestReport testReport;

    private Vector<TestCase> testCases = new Vector<TestCase>();

    private RulesExecutionResult lastDebuggingResult;
    private Vector<MappingRule> rulesToDebug = new Vector<MappingRule>();
    private String projDirectoryAbsPath;


    public void addTestCase(TestCase testCase) {
        testCases.add(testCase);
    }

    public void removeTestCase(TestCase testCase) {
        testCases.remove(testCase);
    }

    public Vector<TestCase> getTestCases() {
        return testCases;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String name) {
        this.projName = name;
    }

    /**
     * Returns a URI of project directory path,e.g. file:/D:/dir/projectDir
     * 
     * @return
     */
    public String getProjDirectoryURI() {
        return projDirectoryURI;
    }

    public void setProjDirectoryURI(String projDirectoryURI) {
        this.projDirectoryURI = projDirectoryURI;
    }

    /**
     * Returns a project directory absolute path,e.g. D:/dir/projectDir/
     * @return
     */
    public String getProjDirectoryAbsPath() {
        return projDirectoryAbsPath;
    }
//    Was commented because of " " in abs path and "%20" in URI
//    public String getProjDirectoryAbsPath() {
//        return projDirectoryURI.substring(6);
//    }

    public String getProjFileAbsPath() {
        return getProjDirectoryAbsPath() + getProjFileName();
    }

    public OntologyMapping getOntologyMapping() {
        return ontologyMapping;
    }

    public void setOntologyMapping(OntologyMapping ontologyMapping) {
        this.ontologyMapping = ontologyMapping;
    }

    public TestReport getTestReport() {
        return testReport;
    }

    public void setTestReport(TestReport testReport) {
        this.testReport = testReport;
    }

    public int getTestCasesNumber() {
        return testCases.size();
    }

    public String getProjFileName() {
        return projFileName;
    }

    public void setProjFileName(String projFileName) {
        this.projFileName = projFileName;
    }

    /**
     * Returs a vector, containing only test case names.
     * 
     * @return
     */
    private Vector<String> getTestCasesNames() {
        Vector<String> testCasesNames = new Vector<String>();
        for (Iterator iter = testCases.iterator(); iter.hasNext();) {
            TestCase testCase = (TestCase) iter.next();
            testCasesNames.add(testCase.getName());
        }
        return testCasesNames;
    }

    /**
     * Returns true, if a test project already contains a test case, identified
     * by the testCaseName.
     * 
     * @param testCaseName
     * @return
     */
    public boolean containsTestCase(String testCaseName) {
        return getTestCasesNames().contains(testCaseName);
    }

    /**
     * Returns entries for an alternative copy of the source and target
     * ontologies with the given ontology URI.
     * 
     * @return
     */
    public Hashtable getSrcAndTargetOntAltEntries() {
        Hashtable ht = new Hashtable();
        ht.put(sourceOntology.getDefaultNamespace(), sourceOntology.getRepositoryString());
        ht.put(targetOntology.getDefaultNamespace(), targetOntology.getRepositoryString());
        return ht;
    }

    /**
     * Returns entries for an alternative copy of the source ontology with the
     * given ontology URI.
     * 
     * @return
     */
    public Hashtable getSrcOntAltEntries() {
        Hashtable ht = new Hashtable();
        ht.put(sourceOntology.getDefaultNamespace(), sourceOntology.getRepositoryString());
        return ht;
    }

    public Ontology getSourceOntology() {
        return sourceOntology;
    }

    public void setSourceOntology(Ontology sourceOntology) {
        this.sourceOntology = sourceOntology;
    }

    public Ontology getTargetOntology() {
        return targetOntology;
    }

    public void setTargetOntology(Ontology targetOntology) {
        this.targetOntology = targetOntology;
    }

    public void setLastDebuggingResult(RulesExecutionResult ruleExecutionResult) {
        this.lastDebuggingResult = ruleExecutionResult;
    }

    public RulesExecutionResult getLastDebuggingResult() {
        return lastDebuggingResult;
    }

    public Vector<MappingRule> getRulesToDebug() {
        return rulesToDebug;
    }

    public void setRulesToDebug(Vector<MappingRule> rulesToDebug) {
        this.rulesToDebug = rulesToDebug;
    }
    
    /**
     * Returns the URI of the directory, in which the project file
     * and the source and targe ontology files are saved.
     * @param projFileURI
     * @return
     */
    public static String getProjDirectoryURI(String projFileURI) {
        File projFile = new File(projFileURI);
        int projNameLength = projFile.getName().length();
        String projDirectoryURI = projFileURI.substring(0, projFileURI.length() - projNameLength);
        return projDirectoryURI;
    }
    
    /**
     * Returns absolute path of the directory, in which the project file
     * and the source and targe ontology files are saved.
     * @param projFileAbsPath
     * @return
     */
    public static String getProjDirectoryAbsPath(String projFileAbsPath) {
        File projFile = new File(projFileAbsPath);
        int projNameLength = projFile.getName().length();
        String projDirAbsPath = projFileAbsPath.substring(0, projFileAbsPath.length() - projNameLength);
        return projDirAbsPath;
    }

    /**
     * @param projectFilePath
     * @return a name of a file, defined by projectFileURI
     */
    public static String getProjFileName(String projectFilePath) {
        File projFile = new File(projectFilePath);
        return projFile.getName();
    }

    public String getProjDefaultNS() {
        return projDefaultNS;
    }

    public void setProjDefaultNS(String projDefaultNS) {
        this.projDefaultNS = projDefaultNS;
    }

    public void setProjDirectoryAbsPath(String projDirectoryAbsPath) {
        this.projDirectoryAbsPath = projDirectoryAbsPath;
    }
    
    public void dispose(){     

        if (ontologyMapping != null) ontologyMapping.setMappingRules(null);       
        
        if (testCases != null) {
            for (Iterator iter = testCases.iterator(); iter.hasNext();) {
                TestCase testCase = (TestCase) iter.next();
                testCase.dispose();
            }
        }
        
        if (testReport != null) testReport.dispose();
        if (lastDebuggingResult != null) lastDebuggingResult.dispose();
        
        if (sourceOntology != null) sourceOntology.getJenaOWLModel().dispose();
        if (targetOntology != null) targetOntology.getJenaOWLModel().dispose();
        if (ontologyMapping != null) ontologyMapping.getJenaOWLModel().dispose();
        
        rulesToDebug = null;
        testCases = null; 
        testReport = null;
        lastDebuggingResult = null;
        sourceOntology  = null;
        targetOntology  = null;
        ontologyMapping = null;        
    }
}
