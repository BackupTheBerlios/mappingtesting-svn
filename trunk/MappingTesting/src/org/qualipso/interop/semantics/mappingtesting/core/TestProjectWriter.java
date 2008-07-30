package org.qualipso.interop.semantics.mappingtesting.core;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.gui.MessagePanel;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;

/**
 * This class represents a writer for test projet files. It is used
 * to serialize all the information about test project into OWL format.
 */
public class TestProjectWriter {

    
    /**
     * Saves all the information about testProject into an .owl file,
     * and replaces the current test project file. 
     * @param testProject
     */
    public static void saveTestProject(TestProject testProject) {
        MessagePanel.appendOut("Saving test project in file ");
        MessagePanel.appendOutBlue(testProject.getProjFileAbsPath());
        MessagePanel.appendOutln(" ... ");
        
        JenaOWLModel testProjJenaOWLModel = null;
        try {
            testProjJenaOWLModel = ProtegeOWL.createJenaOWLModel();
        } catch (OntologyLoadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        importTestProjectOntology(testProjJenaOWLModel);
        testProjJenaOWLModel.getNamespaceManager().setDefaultNamespace(testProject.getProjDefaultNS());
        
        OWLNamedClass testProjectClass = testProjJenaOWLModel.getOWLNamedClass(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.TEST_PROJECT_CLASS_NAME); 
        OWLDatatypeProperty sourceOntologyProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.SOURCE_ONTOLOGY_PROPERTY_NAME);
        OWLDatatypeProperty targetOntologyProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.TARGET_ONTOLOGY_PROPERTY_NAME);
        OWLDatatypeProperty ontologyMappingProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.ONTOLOGY_MAPPING_PROPERTY_NAME);
        
        OWLIndividual testProjectIndividual = testProjectClass.createOWLIndividual(testProject.getProjName());
        testProjectIndividual.setPropertyValue(sourceOntologyProperty, testProject.getSourceOntology().getPathInTestProjectFile());
        testProjectIndividual.setPropertyValue(targetOntologyProperty, testProject.getTargetOntology().getPathInTestProjectFile());
        testProjectIndividual.setPropertyValue(ontologyMappingProperty, testProject.getOntologyMapping().getPathInTestProjectFile());
        
        Vector<TestCase> testCases = testProject.getTestCases();
        Vector<OWLIndividual> testCaseIndividuals = new Vector<OWLIndividual>();
        for (Iterator iter = testCases.iterator(); iter.hasNext();) {
            TestCase testCase = (TestCase) iter.next();
            OWLIndividual testCaseIndividual = getTestCase(testProjectIndividual, testCase, testProjJenaOWLModel);
            testCaseIndividuals.add(testCaseIndividual);
        }        
        
        OWLObjectProperty  hasTestCaseProperty = testProjJenaOWLModel.getOWLObjectProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.HAS_TEST_CASE_PROPERTY_NAME);
        testProjectIndividual.setPropertyValues(hasTestCaseProperty, testCaseIndividuals);

        String testProjDefaultNS = testProject.getProjDefaultNS();        
        OWLUtil.renameOntology(testProjJenaOWLModel, testProjJenaOWLModel.getDefaultOWLOntology(),
                testProjDefaultNS.substring(0, testProjDefaultNS.length()-1)); 
        
        UtilsForProtegeOWL.saveJenaOWLModel(testProjJenaOWLModel, new File(testProject.getProjFileAbsPath()));  
        MessagePanel.appendSuccessln("done.");
    }

    /**
     * Adds an instance of class "TestCase", containing all the information about testCase,
     * to testProjectIndividual. 
     * @param testProjectIndividual
     * @param testCase
     * @param testProjJenaOWLModel
     */
    private static OWLIndividual getTestCase(OWLIndividual testProjectIndividual, TestCase testCase, JenaOWLModel testProjJenaOWLModel) {
        OWLNamedClass testCaseClass = testProjJenaOWLModel.getOWLNamedClass(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.TEST_CASE_CLASS_NAME); 
        OWLDatatypeProperty nameProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.NAME_PROPERTY_NAME);
        OWLDatatypeProperty descriptionProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.DESCRIPTION_PROPERTY_NAME);
        OWLDatatypeProperty testInputInstancesProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.TEST_INPUT_INSTANCES_PROPERTY_NAME);
        OWLDatatypeProperty expectedTestOutputInstancesProperty = testProjJenaOWLModel.getOWLDatatypeProperty(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX + ":"
                + TestProject.EXPECTED_TEST_OUTPUT_INSTANCES_PROPERTY_NAME);
               
        OWLIndividual testCaseIndividual = testCaseClass.createOWLIndividual(testCase.getName());
        testCaseIndividual.setPropertyValue(nameProperty, testCase.getName());
        testCaseIndividual.setPropertyValue(descriptionProperty, testCase.getDescription());
        testCaseIndividual.setPropertyValue(testInputInstancesProperty, testCase.getInputOntRelativePath());
        testCaseIndividual.setPropertyValue(expectedTestOutputInstancesProperty, testCase.getExpectedOutputOntRelativePath());
        
        return testCaseIndividual;
    }

    /**
     * Import an ontology from TestProject.TEST_PROJECT_ONT_FILE into testProjJenaOWLModel
     * @param testProjJenaOWLModel
     */
    private static void importTestProjectOntology(JenaOWLModel testProjJenaOWLModel) {
        String programBasePathURI = MiscUtils.determineProgramBasePathURI();
        String execMonitorFileURI = programBasePathURI + "/" + TestProject.TEST_PROJECT_ONT_FILE;
        LocalFileRepository lfr = new LocalFileRepository(new File(TestProject.TEST_PROJECT_ONT_FILE),
                true);
        testProjJenaOWLModel.getRepositoryManager().addProjectRepository(lfr);

        // First the prefix should be set, and then an import performed
        UtilsForProtegeOWL.setPrefixForNameSpace(TestProject.TEST_PROJECT_ONTOLOGY_PREFIX, 
                TestProject.TEST_PROJECT_ONTOLOGY_DEFAULT_NS + "#", testProjJenaOWLModel);
        UtilsForProtegeOWL.importOntology(testProjJenaOWLModel, TestProject.TEST_PROJECT_ONTOLOGY_DEFAULT_NS);
    }
 
}
