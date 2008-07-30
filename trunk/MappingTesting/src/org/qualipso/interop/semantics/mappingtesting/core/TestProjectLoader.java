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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.qualipso.interop.semantics.mappingtesting.core.exceptions.JenaOWLModelLoadingException;
import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestProjectLoaderException;
import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.gui.MessagePanel;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.Ontology;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;

/**
 * This class loads a test project from an .owl file and creates all the Java
 * data stuctures, which will contain the test project.
 */
public class TestProjectLoader {

    // TODO: delete this variable, it should not be in this class.
    final public static String SWRLA_ONTO_FILE = "etc/swrla.owl";

    private JenaOWLModel projOWLModel;
    private TestProject testProject;
    private ProtegeReasoner projOWLModelReasoner;

    private OWLIndividual testProjectIndividual;

    /**
     * Loads a project, identified by the projectFileURI
     * 
     * @param projectFileAbsPath
     * @return
     * @throws TestProjectLoaderException
     */
    public TestProject loadProjectFile(String projectFileAbsPath) throws TestProjectLoaderException {
        MessagePanel.appendOut("\tLoading project file ");
        MessagePanel.appendOutBlue(projectFileAbsPath);
        MessagePanel.appendOut(" ... ");
        checkForSpacesInFileName(projectFileAbsPath);
        String projectFileURI = MiscUtils.determineFilePathURI(projectFileAbsPath);
        Vector<String> importedFiles = new Vector<String>();
        importedFiles.add(TestProject.TEST_PROJECT_ONT_FILE);

        try {
            projOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(projectFileURI, importedFiles);
        } catch (JenaOWLModelLoadingException e) {
            throw new TestProjectLoaderException("Project file could not be loaded.", null);
        }

        testProject = new TestProject();
        String projDefaultNS = projOWLModel.getNamespaceManager().getDefaultNamespace();
        testProject.setProjDefaultNS(projDefaultNS);
        testProject.setProjDirectoryURI(TestProject.getProjDirectoryURI(projectFileURI));
        testProject.setProjFileName(TestProject.getProjFileName(projectFileAbsPath));
        testProject.setProjDirectoryAbsPath(TestProject.getProjDirectoryAbsPath(projectFileAbsPath));
        
        projOWLModelReasoner = UtilsForProtegeOWL.createProtegePelletReasoner(projOWLModel);

        MessagePanel.appendSuccessln("done.");
        return testProject;
    }

    public static void checkForSpacesInFileName(String projectFileAbsPath) throws TestProjectLoaderException {
        String fileName = TestProject.getProjFileName(projectFileAbsPath);
        if (fileName.indexOf(" ") != -1){
            throw new TestProjectLoaderException("File " + projectFileAbsPath + " cannot be loaded, " +
                    "since spaces are not allowed in names of files, used by the tool." , null);
        }        
    }

    /**
     * Loads source and target ontologies, and a mapping file and creates
     * srcOWLModel, targetOWLModel and mappingOWLModel.
     * 
     * @throws TestProjectLoaderException
     */
    public void load3OWLModels() throws TestProjectLoaderException {
        OWLNamedClass testProjectClass = null;
        try{
            testProjectClass = projOWLModel
            .getOWLNamedClass(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                    + TestProject.TEST_PROJECT_CLASS_NAME);
        } catch (ClassCastException e) {
            throw new TestProjectLoaderException("Project file does not import test project ontology "+
                    TestProject.TEST_PROJECT_ONTOLOGY_DEFAULT_NS, e);
        } 

        Collection<OWLIndividual> tProjIndividuals = null;
        try {
            tProjIndividuals = projOWLModelReasoner
                    .getIndividualsBelongingToClass(testProjectClass);
            // This way both ProtegeReasonerException and NullPointerExceptions
            // are caught
        } catch (InconsistentOntologyException e) {
            throw new TestProjectLoaderException("Project file is inconsistent.", e);
        } catch (Exception e) {
            throw new TestProjectLoaderException("Project file does not contain individuals of type "
                    + TestProject.TEST_PROJECT_CLASS_NAME + ".", e);
        }

        if (tProjIndividuals == null || tProjIndividuals.size() == 0) {
            throw new TestProjectLoaderException("Project file does not contain individuals of type "
                    + TestProject.TEST_PROJECT_CLASS_NAME + ".", null);
        }

        Iterator iter = tProjIndividuals.iterator();
        testProjectIndividual = (OWLIndividual) iter.next();
        // TODO: Name should be a property
        testProject.setProjName(testProjectIndividual.getName());

        // Load source ontology
        // sourceOntologyProperty should not be null, if the programm gets here,
        // since whether testproject.owl ontology is imported is checked a the
        // beginning of this method.
        String srcOntPath = getPathFromProperty(testProjectIndividual,
                TestProject.SOURCE_ONTOLOGY_PROPERTY_NAME);
        Ontology sourceOntology = loadSourceOntology(srcOntPath, testProject);

        // Load target ontology
        String targetOntPath = getPathFromProperty(testProjectIndividual,
                TestProject.TARGET_ONTOLOGY_PROPERTY_NAME);
        Ontology targetOntology = loadTargetOntology(targetOntPath, testProject);

        // Load ontology mapping
        String ontologyMappingPath = getPathFromProperty(testProjectIndividual,
                TestProject.ONTOLOGY_MAPPING_PROPERTY_NAME);
        OntologyMapping om = loadOntologyMapping(ontologyMappingPath, testProject);

        parseMappingOWLModel(testProject.getOntologyMapping().getJenaOWLModel());
    }

    /**
     * Loads source ontology from a file, defined by srcOntPath.
     * 
     * @param srcOntPath
     * @return
     * @throws TestProjectLoaderException
     * @throws TestProjectLoaderException
     */
    public static Ontology loadSourceOntology(String srcOntPath, TestProject tProj)
            throws TestProjectLoaderException {
        MessagePanel.appendOut("\tLoading source ontology ");
        MessagePanel.appendOutBlue(srcOntPath);
        MessagePanel.appendOut(" ... ");

        Ontology sourceOntology = new Ontology(tProj, srcOntPath);
        try {
            checkForSpacesInFileName(sourceOntology.getURL());
        } catch (TestProjectLoaderException e) {
            MessagePanel.appendErrorln("\t" + e.getMessage());
            throw new TestProjectLoaderException(null, e);
        }
        
        JenaOWLModel srcOWLModel = null;
        try {
            srcOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(sourceOntology.getURL(), null);
        } catch (JenaOWLModelLoadingException e) {
            MessagePanel.appendErrorln("\tSource ontology could not be loaded.");
            throw new TestProjectLoaderException(null, e);
        }
        sourceOntology.setJenaOWLModel(srcOWLModel);
        tProj.setSourceOntology(sourceOntology);
        MessagePanel.appendSuccessln("done.");
        return sourceOntology;
    }

    /**
     * Loads target ontology from a file, defined by targetOntPath.
     * 
     * @param targetOntPath
     * @return
     * @throws TestProjectLoaderException
     */
    public static Ontology loadTargetOntology(String targetOntPath, TestProject tProj)
            throws TestProjectLoaderException {
        MessagePanel.appendOut("\tLoading target ontology ");
        MessagePanel.appendOutBlue(targetOntPath);
        MessagePanel.appendOut(" ... ");

        Ontology targetOntology = new Ontology(tProj, targetOntPath);
        try{
            checkForSpacesInFileName(targetOntology.getURL());
        } catch (TestProjectLoaderException e) {
            MessagePanel.appendErrorln("\t" + e.getMessage());
            throw new TestProjectLoaderException(null, e);
        }
        JenaOWLModel targetOWLModel = null;
        try {
            targetOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(targetOntology.getURL(), null);
        } catch (JenaOWLModelLoadingException e) {
            MessagePanel.appendErrorln("\tTarget ontology could not be loaded.");
            throw new TestProjectLoaderException(null, e);
        }
        targetOntology.setJenaOWLModel(targetOWLModel);
        tProj.setTargetOntology(targetOntology);

        MessagePanel.appendSuccessln("done.");
        return targetOntology;
    }

    /**
     * Loads ontology mapping from a file, defined by ontologyMappingPath.
     * 
     * @param ontologyMappingPath
     * @return
     * @throws TestProjectLoaderException
     */
    public static OntologyMapping loadOntologyMapping(String ontologyMappingPath, TestProject tProj)
            throws TestProjectLoaderException {

        MessagePanel.appendOut("\tLoading ontology mapping ");
        MessagePanel.appendOutBlue(ontologyMappingPath);
        MessagePanel.appendOut(" ... ");

        Vector<String> repositories = new Vector<String>();
        String sourceOntologyRepositoryString = tProj.getSourceOntology().getRepositoryString();
        String targetOntologyRepositoryString = tProj.getTargetOntology().getRepositoryString();
        sourceOntologyRepositoryString = sourceOntologyRepositoryString.replaceAll("%20", " ");
        targetOntologyRepositoryString = targetOntologyRepositoryString.replaceAll("%20", " ");

        repositories.add(sourceOntologyRepositoryString);
        repositories.add(targetOntologyRepositoryString);
        repositories.add(SWRLA_ONTO_FILE);

        OntologyMapping om = new OntologyMapping(tProj, ontologyMappingPath);
        
        try{
            checkForSpacesInFileName(om.getURL());
        } catch (TestProjectLoaderException e) {
            MessagePanel.appendErrorln("\t" + e.getMessage());
            throw new TestProjectLoaderException(null, e);
        }
        JenaOWLModel mappingOWLModel = null;
        try {
            mappingOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(om.getURL(), repositories);
        } catch (JenaOWLModelLoadingException e) {
            MessagePanel.appendErrorln("\tOntology mapping could not be loaded.");
            throw new TestProjectLoaderException(null, e);
        }
        om.setJenaOWLModel(mappingOWLModel);
        tProj.setOntologyMapping(om);

        MessagePanel.appendSuccessln("done.");
        return om;
    }

    /**
     * Parses mappingOWLModel, created from an ontology mapping file.
     * mappingOWLModel contains mapping rules, which are parsed and saved in an
     * MappingRule class.
     * 
     * @param mappingOWLModel
     */
    private void parseMappingOWLModel(JenaOWLModel mappingOWLModel) {
        // SWRLParser parser = new SWRLParser (mappingOWLModel);
        // parser.parse("");
        OntologyMapping om = testProject.getOntologyMapping();
        SWRLFactory factory = new SWRLFactory(mappingOWLModel);
        Collection imps = factory.getImps();
        for (Iterator iter = imps.iterator(); iter.hasNext();) {
            SWRLImp swrlImp = (SWRLImp) iter.next();
            MappingRule mRule = new MappingRule(swrlImp);
            om.addMappingRule(mRule);
        }
    }

    /**
     * Checks if ontologyMapping contains at least one rule.
     * 
     * @param ontologyMapping
     * @throws TestProjectLoaderException
     *             is thrown when ontologyMapping does not contain any mapping
     *             rules.
     */
    public void checkAtLeastOneRuleIsContained(OntologyMapping ontologyMapping)
            throws TestProjectLoaderException {
        Vector<MappingRule> mappingRules = ontologyMapping.getMappingRules();
        if (mappingRules.size() == 0) {
            String message = "Ontology mapping "
                    + ontologyMapping.getPathInTestProjectFile()
                    + " does not contain any mapping rules, that is why the test project is invalid.";
            throw new TestProjectLoaderException(message, null);
        }
    }

    /**
     * Checks that ontology mapping file contains all the imporst, necessary to
     * display correctly and execute the mapping rules.
     * 
     * @param ontologyMapping
     * @throws TestProjectLoaderException
     */
    public void checkForMissingImports(OntologyMapping ontologyMapping)
            throws TestProjectLoaderException {
        Vector<MappingRule> mappingRules = ontologyMapping.getMappingRules();
        for (Iterator iter = mappingRules.iterator(); iter.hasNext();) {
            MappingRule mappingRule = (MappingRule) iter.next();
            if (mappingRule.getRuleText().indexOf("<INVALID_") != -1) {
                String message = "Ontology mapping "
                        + ontologyMapping.getPathInTestProjectFile()
                        + " does not contain all the imports, necessary to display correctly and execute the mapping rules.";
                throw new TestProjectLoaderException(message, null);
            }
        }
    }

    /**
     * Loads all the test cases, contained in the test project.
     * 
     * @throws TestProjectLoaderException
     * 
     */
    public void loadTestCases() throws TestProjectLoaderException {

        // should not be null, since it has already been checked, that
        // testproject.owl is imported.
        OWLNamedClass testCaseClass = projOWLModel
                .getOWLNamedClass(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                        + TestProject.TEST_CASE_CLASS_NAME);
        RDFProperty hasTestCaseProperty = projOWLModel
                .getRDFProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                        + TestProject.HAS_TEST_CASE_PROPERTY_NAME);

        Collection testCasePropertyValues = testProjectIndividual
                .getPropertyValues(hasTestCaseProperty);
        if (testCasePropertyValues == null || testCasePropertyValues.size() == 0) {
            return;
        }
        for (Iterator iter = testCasePropertyValues.iterator(); iter.hasNext();) {
            Object testCasePropertyValue = (Object) iter.next();
            if (!(testCasePropertyValue instanceof OWLIndividual)) {
                String message = "\tTest case \"" + testCasePropertyValue + "\" ignored."
                        + " Test case should be of type \"test:TestCase\".";
                MessagePanel.appendErrorln(message);
                continue;
            }

            OWLIndividual testCaseIndividual = (OWLIndividual) testCasePropertyValue;
            if (!testCaseIndividual.getRDFType().equals(testCaseClass)) {
                String message = "\tTest case \"" + testCaseIndividual.getLocalName()
                        + "\" ignored." + " Test case should be of type \"test:TestCase\".";
                MessagePanel.appendErrorln(message);
                continue;
            }

            loadTestCase(testCaseIndividual);
        }
    }

    /**
     * Load a test case, described by the testCaseIndividual in the testing
     * project ontology.
     * 
     * @param testCaseIndividual
     * @throws TestProjectLoaderException
     * @throws JenaOWLModelLoadingException
     * @throws Exception
     */
    private void loadTestCase(OWLIndividual testCaseIndividual) 
//    throws ProjectLoaderException 
    {
        String testCaseName = (String) UtilsForProtegeOWL.getDataPropertyValue(projOWLModel,
                testCaseIndividual, TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                        + TestProject.NAME_PROPERTY_NAME);
        if (testCaseName == null) {
            String message = "\tTest case \"" + testCaseIndividual.getLocalName()
                    + "\" ignored, because " + "test case individual \""
                    + testProjectIndividual.getLocalName() + "\" does not have \""
                    + TestProject.NAME_PROPERTY_NAME + "\" property.";
            MessagePanel.appendErrorln(message);
            return;
        }

        MessagePanel.appendOut("\tLoading test case ");
        MessagePanel.appendOutBlue(testCaseName);
        MessagePanel.appendOut(" ... ");

        String testCaseDescription = (String) UtilsForProtegeOWL.getDataPropertyValue(projOWLModel,
                testCaseIndividual, TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                        + TestProject.DESCRIPTION_PROPERTY_NAME);
        String inputInstancesPath = null;
        String outputInstancesPath = null;        
        try {
            inputInstancesPath = getPathFromProperty(testCaseIndividual,
                    TestProject.TEST_INPUT_INSTANCES_PROPERTY_NAME);
            outputInstancesPath = getPathFromProperty(testCaseIndividual,
                    TestProject.EXPECTED_TEST_OUTPUT_INSTANCES_PROPERTY_NAME);
        } catch (TestProjectLoaderException e) {
            String message = "\tTest case \"" + testCaseName
            + "\" ignored. ";
            MessagePanel.appendErrorln(message + e.getMessage());
            return;
        }
        
        JenaOWLModel inputInstancesOWLModel;
        JenaOWLModel outputInstancesOWLModel;
        try {
            inputInstancesOWLModel = loadInputInstancesJenaOWLModel(testProject.getProjDirectoryURI() + inputInstancesPath);
            outputInstancesOWLModel = loadOutputInstancesJenaOWLModel(testProject.getProjDirectoryURI() + outputInstancesPath);
        } catch (TestProjectLoaderException e) {
            e.printStackTrace();
            String message = "\tTest case \"" + testCaseName + "\" ignored. ";
            MessagePanel.appendErrorln(message + e.getMessage());
            return;
        }

        TestCase testCase = new TestCase(testCaseName, testCaseDescription,
                inputInstancesOWLModel, outputInstancesOWLModel);
        testCase.setInputOntRelativePath(inputInstancesPath);
        testCase.setExpectedOutputOntRelativePath(outputInstancesPath);

        testProject.addTestCase(testCase);
        testCase.setTestProject(testProject);
        MessagePanel.appendSuccessln("done.");
    }
    

    public JenaOWLModel loadInputInstancesJenaOWLModel(String inputInstFileAbsPath) throws TestProjectLoaderException {
        if (inputInstFileAbsPath.startsWith("http") || inputInstFileAbsPath.startsWith("ftp") ){
            String message = "File, containing test input instances, " +
                    "cannot be online file.";
            throw new TestProjectLoaderException(message, null);
        }        
            
        JenaOWLModel inputInstancesOWLModel = null;
        
//        Load test input instances
        Vector<String> repositoryStrings = new Vector<String>();
        repositoryStrings.add(testProject.getSourceOntology().getRepositoryString());
        
        checkForSpacesInFileName(inputInstFileAbsPath);

        try {            
            inputInstancesOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(inputInstFileAbsPath, repositoryStrings);
        } catch (JenaOWLModelLoadingException e) {
            e.getCause().printStackTrace();
            String message = "Test input instances file \"" + inputInstFileAbsPath + 
                    "\" could not be loaded.";
            throw new TestProjectLoaderException(message, null);
        }        
        boolean onlySrcOntInstancesAreIncluded = checkOnlySrcOntInstancesAreIncluded(inputInstancesOWLModel);
        if (!onlySrcOntInstancesAreIncluded){
            String message = "All individuals in file \"" + inputInstFileAbsPath + 
                    "\" should be either instances of source ontology or owl:Thing.";
            throw new TestProjectLoaderException(message, null);
        }  
        return inputInstancesOWLModel;
    }
    
    public JenaOWLModel loadOutputInstancesJenaOWLModel(String expOutputInstFileAbsPath) throws TestProjectLoaderException {
        if (expOutputInstFileAbsPath.startsWith("http") || expOutputInstFileAbsPath.startsWith("ftp") ){
            String message = "File, containing expected test output instances, " +
                    "cannot be online file.";
            throw new TestProjectLoaderException(message, null);
        }     
        JenaOWLModel outputInstancesOWLModel = null;
        
        Vector<String> repositoryStrings = new Vector<String>();
        repositoryStrings.add(testProject.getSourceOntology().getRepositoryString());
        repositoryStrings.add(testProject.getTargetOntology().getRepositoryString());

        checkForSpacesInFileName(expOutputInstFileAbsPath);

        try {
            outputInstancesOWLModel = UtilsForProtegeOWL.loadOWLModelFromURI(expOutputInstFileAbsPath, repositoryStrings);
        } catch (JenaOWLModelLoadingException e) {
            e.getCause().printStackTrace();
            String message = "Expected test output instances file \"" + expOutputInstFileAbsPath + 
                    "\" could not be loaded.";
            throw new TestProjectLoaderException(message, null);
        }
        return outputInstancesOWLModel;
    }


    /**
     * Checks, that all user defined individuals in inputInstancesOWLModel are
     * actually instances of source ontology.
     * @param inputInstancesOWLModel
     * @return true, if all user defined individuals in inputInstancesOWLModel are
     * actually instances of source ontology or instances of owl:Thing
     */
    private boolean checkOnlySrcOntInstancesAreIncluded(JenaOWLModel inputInstancesOWLModel) {
        String srcOntDefaultNS = testProject.getSourceOntology().getDefaultNamespace();

        Collection<RDFIndividual> individuals = inputInstancesOWLModel.getUserDefinedRDFIndividuals(false);

        for (Iterator<RDFIndividual> it = individuals.iterator(); it.hasNext();) {

            RDFIndividual individual = it.next();
            Collection individualTypes = individual.getRDFTypes();
            for (Iterator types = individualTypes.iterator(); types.hasNext();) {
                RDFResource type = (RDFResource) types.next();
                String namespace = type.getNamespace();
                if (namespace.equals(srcOntDefaultNS)){
                    continue;
                } 
                else if (type.equals(inputInstancesOWLModel.getOWLThingClass())){
                    continue;
                }
                else{
                    return false;
                }
            }
        }
        return true;        
    }

    private String getPathFromProperty(OWLIndividual individual, String propertyName)
            throws TestProjectLoaderException {
        Object pathObject = UtilsForProtegeOWL.getDataPropertyValue(projOWLModel, individual,
                TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":" + propertyName);
        if (pathObject == null) {
            throw new TestProjectLoaderException("Individual \"" + individual.getLocalName()
                    + "\" does not have \"" + propertyName + "\" property.", null);
        }
        if (!(pathObject instanceof String)) {
            throw new TestProjectLoaderException("Individual \"" + individual.getLocalName()
                    + "\" has \"" + propertyName
                    + "\" property, but property value is not of type String.", null);
        }
        String path = (String) pathObject;
        return path;
    }
}
