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

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestCaseCreatorException;
import org.qualipso.interop.semantics.mappingtesting.core.exceptions.TestProjectLoaderException;
import org.qualipso.interop.semantics.mappingtesting.core.ruleexecution.RuleDebugger;
import org.qualipso.interop.semantics.mappingtesting.core.ruleexecution.TestCaseExecutionEngine;
import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.gui.EmptyPanelWithText;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.Menu;
import org.qualipso.interop.semantics.mappingtesting.gui.MessagePanel;
import org.qualipso.interop.semantics.mappingtesting.gui.NewTestProjectDialog;
import org.qualipso.interop.semantics.mappingtesting.gui.TestingDebuggingTabbedPane;
import org.qualipso.interop.semantics.mappingtesting.gui.debugging.DebuggingPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.debugging.RuleSelectionDialog;
import org.qualipso.interop.semantics.mappingtesting.gui.debugging.SelectedRulesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.ExecutedRulesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.OntologiesAndRulesSplitPane;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.OntologyPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.RulesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.InstancesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.editing.NewTestCaseDialog;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.editing.TestCaseEditorPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.execution.TestCaseExecutionPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.execution.TestCasesExecResultJList;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.execution.TestReportTextPreview;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;
import org.qualipso.interop.semantics.mappingtesting.model.RulesExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestCaseExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;
import org.qualipso.interop.semantics.mappingtesting.model.TestReport;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * A singleton controller, which it is accessible from all the classes of the program. 
 * It coordinates communication between GUI components and other control classes or data model objects. 
 */
public class Controller {

    // singleton object
    private static Controller controller;

    // references to GUI components
    private JFrame frame;

    private Menu menu;

    private OntologiesAndRulesSplitPane ontologiesAndRulesPane;

    private TestingDebuggingTabbedPane testingDebuggingTabbedPane;

    private TestCaseEditorPanel testCaseEditorPanel;

    private TestCaseExecutionPanel testCaseExecutionPanel;

    private DebuggingPanel debuggingPanel;

    private TestCaseCreator testCaseCreator = null;
    private TestProjectCreator testProjectCreator = null;

    private RuleDebugger ruleDebugger = null;

    private TestProject testProject;

    /**
     * This variable shows, whether, when the Debugging tab was selected, the
     * corresponding RulePanel was showing selected rules to be debugged
     * (SelectedRulesPanel) or already debugged rules (ExecutedRulesPanel).
     */
    private boolean alreadyDebuggedRulesLastShown = true;

    private TestProjectLoader testProjectLoader;

    /**
     * singleton creator
     * 
     * @return Controller
     */
    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    private Controller() {
    }

    /**
     * Shows a file chooser for selection of the test project, and then opens
     * the selected test project.
     */
    public void openTestProject() {
        // close the current test project
        if (controller.getTestProject() != null) {
            boolean testProjClosed = controller.closeTestProject();
            // user has cancelled the operation
            if (!testProjClosed) {
                return;
            }
        }

        File currentDir = new File(".");
        String defaultPath = currentDir.getAbsolutePath();
        String projPath = choosePath(GUIConstants.OPEN_TEST_PROJECT, defaultPath,
                JFileChooser.OPEN_DIALOG, controller.getFrame(), false);

        if (projPath == null)
            return;

        openTestProject(projPath);
    }

    /**
     * Loads a test project, which is identified by the projPath.
     * 
     * @param path
     */
    public synchronized void openTestProject(String path) {
        final String projPath = path;
        new Thread() {

            public void run() {

                MessagePanel.appendOut("Opening test project ");
                MessagePanel.appendOutBlue(projPath);
                MessagePanel.appendOutln(" ... ");

                // load a test project, defined in PROJECT_FILE.
                testProjectLoader = new TestProjectLoader();

                try {
                    testProject = testProjectLoader.loadProjectFile(projPath);
                    testProjectLoader.load3OWLModels();
                    controller.displayOntologiesAndRules();
                    testProjectLoader.checkAtLeastOneRuleIsContained(testProject
                            .getOntologyMapping());
                    testProjectLoader.checkForMissingImports(testProject.getOntologyMapping());
                    testProjectLoader.loadTestCases();
                    controller.displayTestCaseEditorPanel();
                    controller.setEnabledMenusForOpenTestProj(true);
                } catch (TestProjectLoaderException e1) {
                    if (e1.getMessage() != null) {
                        MessagePanel.appendErrorln("\t" + e1.getMessage());
                    }
                    MessagePanel.appendErrorln("Test project could not be opened.");
                    if (e1.getCause() != null) {
                        e1.getCause().printStackTrace();
                    } else {
                        e1.printStackTrace();
                    }
                    testProject = null;
                    return;
                }
                MessagePanel.appendSuccessln("done.");
                controller.setEnabledMenusForOpenTestProj(true);
            }
        }.start();
    }

    /**
     * Enables or disables menus needed, when a test project is open.
     * 
     * @param b
     *            true to enable the menus, false to disable
     */
    protected void setEnabledMenusForOpenTestProj(boolean b) {
        // Test Project Menu
        // New Test Project and Open Test Project always enabled
        controller.getMenu().getSaveTestProjectMenuItem().setEnabled(b);
        controller.getMenu().getCloseTestProjectMenuItem().setEnabled(b);

        // Edit and Run Test Cases Menus
        // "New Test Case" always enabled, when EditTestCasesMenu enabled.
        // "Run All Test Cases" always enabled, when getRunTestCasesMenu
        // enabled.
        controller.getMenu().getEditTestCasesMenu().setEnabled(b);

        if (b == false) {
            controller.getMenu().getDeleteTestCaseMenuItem().setEnabled(b);

            controller.getMenu().getRunTestCasesMenu().setEnabled(b);
            controller.getMenu().getShowTestReportMenuItem().setEnabled(b);
            controller.getMenu().getSaveTestReportMenuItem().setEnabled(b);
        }
        // RunTestCasesMenu is enabled only when number of test cases > 0
        else if (b == true && testProject.getTestCases().size() > 0) {
            controller.getMenu().getDeleteTestCaseMenuItem().setEnabled(b);
            controller.getMenu().getRunTestCasesMenu().setEnabled(b);
        }

        // Debugging Menu
        // "Select Rules to be Debugged" and "Load source Ontology Instances"
        // are always enabled, when DebuggingMenu enabled.
        controller.getMenu().getDebuggingMenu().setEnabled(b);

        if (b == false) {
            controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(b);
            controller.getMenu().getStartDebuggingMenuItem().setEnabled(b);
        }
    }

    /**
     * Shows NewTestProjectDialog and creates a new test project, based on the
     * information, provided by a user in NewTestProjectDialog.
     */
    public void createNewTestProject() {
        // close the current test project
        if (testProject != null) {
            boolean testProjClosed = controller.closeTestProject();
            // user has cancelled the operation
            if (!testProjClosed) {
                return;
            }
        }

        NewTestProjectDialog newTestProjectDialog = new NewTestProjectDialog(null);
        newTestProjectDialog.setVisible(true);
        if (newTestProjectDialog.getReturnStatus() == NewTestCaseDialog.RET_CANCEL)
            return;
        String testProjectName = newTestProjectDialog.getTestProjectName();
        String testProjectDirectory = newTestProjectDialog.getTestProjectDirectory();
        String sourceOntologyPath = newTestProjectDialog.getSourceOntologyPath();
        String targetOntologyPath = newTestProjectDialog.getTargetOntologyPath();
        String ontologyMappingPath = newTestProjectDialog.getOntologyMappingPath();

        MessagePanel.appendOut("Creating new test project ");
        MessagePanel.appendOutBlue(testProjectName);
        MessagePanel.appendOutln(" ... ");

        if (testProjectCreator == null)
            testProjectCreator = new TestProjectCreator();

        String parsNotEmptyCheckResult = testProjectCreator.checkParametersNotEmpty(
                testProjectName, testProjectDirectory, sourceOntologyPath, targetOntologyPath,
                ontologyMappingPath);
        if (!parsNotEmptyCheckResult.equals("OK")) {
            MessagePanel.appendErrorln("\t" + parsNotEmptyCheckResult);
            MessagePanel.appendErrorln("Test project could not be created.");
            return;
        }

        // check that spaces are not contained in test project name and in 3
        // referenced files.
        if (testProjectName.indexOf(" ") != -1) {
            MessagePanel.appendErrorln("Test project \"" + testProjectName
                    + "\" could not be created, since "
                    + "test project name cannot contain spaces.");
            return;
        }

        TestProject newTestProject = null;
        try {
            newTestProject = testProjectCreator.createTestProject(testProjectName,
                    testProjectDirectory, sourceOntologyPath, targetOntologyPath,
                    ontologyMappingPath);
        } catch (TestProjectLoaderException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                MessagePanel.appendErrorln("\t" + e.getMessage());
            }
            MessagePanel.appendErrorln("Test project could not be created.");
            return;
        }
        if (newTestProject == null) {
            MessagePanel.appendErrorln("Test project could not be created.");
            return;
        }

        MessagePanel.appendSuccessln("done.");

        controller.setTestProject(newTestProject);
        controller.saveTestProject();

        TestProject currentTestProject = controller.getTestProject();
        controller.openTestProject(currentTestProject.getProjFileAbsPath());
    }

    /**
     * Saves the current test project in the test project directory.
     */
    public void saveTestProject() {
        TestProjectWriter.saveTestProject(controller.getTestProject());
    }

    /**
     * Closes the current test project.
     * 
     * @return true, if the test project was closed; false if closing of the
     *         current test project has been cancelled.
     */
    public boolean closeTestProject() {
        // check if the current project should be saved
        if (controller.getTestProject() != null) {
            int saveCurrentProject = showSaveCurrentProjectDialog();
            if (saveCurrentProject == JOptionPane.YES_OPTION) {
                saveTestProject();
            } else if (saveCurrentProject == JOptionPane.NO_OPTION) {
            } else if (saveCurrentProject == JOptionPane.CANCEL_OPTION) {
                return false;
                // the user has closed dialog window
            } else {
                return false;
            }
        }
        
        MessagePanel.appendOut("Closing test project  ");
        MessagePanel.appendOutBlue(testProject.getProjName());
        MessagePanel.appendOutln(" ... ");
        
//      these 2 statements should be in this order
        controller.getTestingDebuggingTabbedPane().clearAll();  
        controller.getOntologiesAndRulesPane().clearAll();
        
        controller.getTestProject().dispose();
        controller.setTestProject(null); 
        
        controller.setTestCaseEditorPanel(null);
        controller.setTestCaseExecutionPanel(null);
        controller.setDebuggingPanel(null);
        controller.setTestCaseCreator(null);
        controller.setTestProjectCreator(null);
        controller.setRuleDebugger(null);
        controller.setTestProjectLoader(null);

        alreadyDebuggedRulesLastShown = true;

        controller.setEnabledMenusForOpenTestProj(false);
        MemoryManager.gc10(); 

        MessagePanel.appendSuccessln("done.");

        return true;       

    }

    /**
     * Shows a dialog, asking whether the current project should be saved.
     * 
     * @return an integer indicating the option chosen by the user, or
     *         CLOSED_OPTION if the user closed the dialog
     */
    private int showSaveCurrentProjectDialog() {
        Object[] options = { "Yes", "No", "Cancel" };
        int n = JOptionPane.showOptionDialog(frame,
                "Do you want to save changes to the current project?", "Save?",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options[2]);

        return n;
    }

    /**
     * Creates an inferred model from assertedJenaOWLModel.
     * 
     * @param assertedJenaOWLModel
     * @param jenaOWLModelSource
     *            defines which source instances in jenaOWLModel come from. Can
     *            either be of type TestCase or of type RuleExecutionResult.
     * @param instancesType
     *            defines, whether assertedJenaOWLModel contains instances of
     *            source ontology or instances of both source and target
     *            ontologies. Can have values: TestProject.SOURCE_ONTOLOGY or
     *            TestProject.SOURCE_AND_TARGET_ONTOLOGY.
     * @return
     */
    public JenaOWLModel getInferredModel(JenaOWLModel assertedJenaOWLModel,
            Object jenaOWLModelSource, int instancesType) {

        Hashtable altEntries = null;
        if (instancesType == TestProject.INSTANCES_TYPE_SOURCE_ONTOLOGY)
            altEntries = testProject.getSrcOntAltEntries();
        else if (instancesType == TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY)
            altEntries = testProject.getSrcAndTargetOntAltEntries();

        // assertedJenaOWLModel is used for debugging
        if (jenaOWLModelSource == null) {
            InferredOWLModelCreator modelCreator = new InferredOWLModelCreator();
            return modelCreator.createInferredModel(assertedJenaOWLModel, altEntries);
        }

        // assertedJenaOWLModel comes from a test case
        if (jenaOWLModelSource instanceof TestCase) {
            TestCase tc = (TestCase) jenaOWLModelSource;

            // assertedJenaOWLModel contains input instances
            if (instancesType == TestProject.INSTANCES_TYPE_SOURCE_ONTOLOGY) {
                if (tc.getInputInstancesOWLModelInferred() != null)
                    return tc.getInputInstancesOWLModelInferred();
                else {
                    MessagePanel.appendOut("Creating inferred model for  ");
                    MessagePanel.appendOutBlue(assertedJenaOWLModel.getNamespaceManager()
                            .getDefaultNamespace());
                    MessagePanel.appendOut(" ... ");

                    InferredOWLModelCreator modelCreator = new InferredOWLModelCreator();
                    JenaOWLModel inferredJenaOWLModel = modelCreator.createInferredModel(
                            assertedJenaOWLModel, altEntries);
                    tc.setInputInstancesOWLModelInferred(inferredJenaOWLModel);
  
                    MessagePanel.appendSuccessln("done");
                    return inferredJenaOWLModel;
                }
            }
            // assertedJenaOWLModel contains expected output instances
            else if (instancesType == TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY) {
                if (tc.getExpectedOutputInstancesOWLModelInferred() != null)
                    return tc.getExpectedOutputInstancesOWLModelInferred();
                else {
                    MessagePanel.appendOut("Creating inferred model for  ");
                    MessagePanel.appendOutBlue(assertedJenaOWLModel.getNamespaceManager()
                            .getDefaultNamespace());
                    MessagePanel.appendOut(" ... ");

                    InferredOWLModelCreator modelCreator = new InferredOWLModelCreator();
                    JenaOWLModel inferredJenaOWLModel = modelCreator.createInferredModel(
                            assertedJenaOWLModel, altEntries);
                    tc.setExpectedOutputInstancesOWLModelInferred(inferredJenaOWLModel);

                    MessagePanel.appendSuccessln("done");
                    return inferredJenaOWLModel;
                }
            }
        }

        // assertedJenaOWLModel comes from a RuleExecutionResult
        if (jenaOWLModelSource instanceof RulesExecutionResult) {

            RulesExecutionResult rer = (RulesExecutionResult) jenaOWLModelSource;
            if (rer.getResultingOWLModelInferred() != null) {
                return rer.getResultingOWLModelInferred();
            } else {
                InferredOWLModelCreator modelCreator = new InferredOWLModelCreator();
                JenaOWLModel inferredJenaOWLModel = modelCreator.createInferredModel(
                        assertedJenaOWLModel, altEntries);
                rer.setResultingOWLModelInferred(inferredJenaOWLModel);     
                return inferredJenaOWLModel;
            }
        }
        // should not get here
        return null;
    }

    /**
     * Graphically represents source and target ontologies in the testProject,
     * and the rules, which constitute the mapping between the ontologies.
     */
    private void displayOntologiesAndRules() {
        // new Thread() {
        // public void run() {
        // Display updated source ontology tree
        JenaOWLModel srcOWLModel = testProject.getSourceOntology().getJenaOWLModel();
        controller.getOntologiesAndRulesPane().setOntologyPanel(new OntologyPanel(srcOWLModel),
                OntologiesAndRulesSplitPane.SRC_PANEL);

        // Display updated target ontology tree
        JenaOWLModel targetOWLModel = testProject.getTargetOntology().getJenaOWLModel();
        controller.getOntologiesAndRulesPane().setOntologyPanel(new OntologyPanel(targetOWLModel),
                OntologiesAndRulesSplitPane.TARGET_PANEL);

        // Display updated rules table
        OntologyMapping ontologyMapping = testProject.getOntologyMapping();
        controller.getOntologiesAndRulesPane().setRulesPanel(
                new RulesPanel(ontologyMapping, testProject.getSourceOntology()
                        .getDefaultNamespace(), testProject.getTargetOntology()
                        .getDefaultNamespace()));
        // }
        // }.start();
    }

    /**
     * Presents all test cases in testProject in TestCaseEditorPanel.
     */
    private void displayTestCaseEditorPanel() {
        TestCaseEditorPanel tcEditorPanel = new TestCaseEditorPanel(testProject);
        Vector<TestCase> testCases = testProject.getTestCases();
        if (testProject.getTestCases().size() > 0) {
            controller.getTestCaseEditorPanel().displayTestCasesList();
        } else {
            controller.getTestCaseEditorPanel().displayNoTestCasesMessage();
        }

        controller.getTestingDebuggingTabbedPane().setComponentAt(0, tcEditorPanel);
        controller.getTestingDebuggingTabbedPane().setSelectedIndex(0);
    }

    /**
     * Display information about the selectedTestCase
     * 
     * @param selectedTestCase
     */
    public void displayTestCaseInformation(TestCase selectedTestCase) {
        // final TestCase testCase = selectedTestCase;
        // new Thread() {
        // public void run() {
        controller.getTestCaseEditorPanel().displayTestCaseInfoPanel(selectedTestCase);
        // }
        // }.start();
    }

    /**
     * Shows a dialog, where information about the new test case can be entered.
     * Creates a new test case and adds it to testProject. Updates GUI.
     */
    public void createNewTestCase() {
        int initialNumberOfTestCases = testProject.getTestCasesNumber();
        NewTestCaseDialog newTCDialog = new NewTestCaseDialog(null, testProject
                .getProjDirectoryAbsPath());
        newTCDialog.setVisible(true);
        if (newTCDialog.getReturnStatus() == NewTestCaseDialog.RET_CANCEL)
            return;
        String testCaseName = newTCDialog.getTestCaseName();
        String testCaseDescription = newTCDialog.getTestCaseDescription();
        String inputInstacesFilePath = newTCDialog.getInputInstFilePath();
        String expOutputInstacesFilePath = newTCDialog.getExpOutputInstFilePath();

        MessagePanel.appendOut("Creating new test case ");
        MessagePanel.appendOutBlue(testCaseName);
        MessagePanel.appendOutln(" ... ");

        if (testCaseCreator == null)
            testCaseCreator = new TestCaseCreator(testProject, testProjectLoader);
        TestCase newTestCase;
        try {
            newTestCase = testCaseCreator.createTestCase(testCaseName, testCaseDescription,
                    inputInstacesFilePath, expOutputInstacesFilePath);
        } catch (TestCaseCreatorException e) {
            e.printStackTrace();
            MessagePanel.appendErrorln("\t" + e.getMessage());
            MessagePanel.appendErrorln("Test case could not be created.");
            return;
        } catch (TestProjectLoaderException e) {
            e.printStackTrace();
            MessagePanel.appendErrorln("\t" + e.getMessage());
            MessagePanel.appendErrorln("Test case could not be created.");
            return;
        }

        controller.getTestCaseEditorPanel().addTestCaseToJList(newTestCase);
        if (initialNumberOfTestCases == 0) {
            controller.getTestCaseEditorPanel().displayTestCasesList();
            controller.getMenu().getDeleteTestCaseMenuItem().setEnabled(true);
            controller.getMenu().getRunTestCasesMenu().setEnabled(true);
        }
        MessagePanel.appendSuccessln("done.");
    }

    /**
     * Deletes a test case, which has been selected in TestCasesJList in
     * TestCaseEditorPanel from the testProject. Deletes directory, containing
     * input and expected output instances in the test case from the file
     * system. Updates GUI.
     */
    public void deleteTestCase() {
        TestCase testCaseToDelete = controller.getTestCaseEditorPanel().getSelectedTestCase();
        int n = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delele test case \""
                + testCaseToDelete.getName() + "\"?\n" + "This will also delete directory "
                + testCaseToDelete.getName() + " from\n" + testProject.getProjDirectoryAbsPath(),
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {

            MessagePanel.appendOut("Deleting test case ");
            MessagePanel.appendOutBlue(testCaseToDelete.getName());
            MessagePanel.appendOut(" ... ");

            String testCaseDirString = testProject.getProjDirectoryAbsPath()
                    + testCaseToDelete.getName();
            boolean testCaseDeleted = MiscUtils.deleteNonEmptyDirectory(testCaseDirString);
            if (testCaseDeleted) {                
                testProject.getTestCases().remove(testCaseToDelete);
                controller.getTestCaseEditorPanel().deleteTestCaseFromJList(testCaseToDelete);
                
                if (testProject.getTestCases().size() < 1) {
                    controller.getTestCaseEditorPanel().displayNoTestCasesMessage();
                    controller.getTestCaseEditorPanel().displayEmptyTestCaseInfoPanel();
                    controller.getMenu().getDeleteTestCaseMenuItem().setEnabled(false);
                    controller.getMenu().getRunTestCasesMenu().setEnabled(false);
                }
                MessagePanel.appendSuccessln("done.");
            }
            // if (testProject.getTestCases().size() > 0){
            // controller.getMenu().getRunTestCasesMenu().setEnabled(true);
            // }
            else {
                MessagePanel.appendErrorln("Test case could not be deleted.");
            }
        }
    }

    // ------------------------------------------------------------------
    /**
     * Executes all test cases in the testProject and presents test case
     * execution results.
     */
    public synchronized void executeAllTestCases() {
        new Thread() {
            public void run() {       

                MessagePanel.appendOut("Executing all test cases in ");
                MessagePanel.appendOutBlue(testProject.getProjName());
                MessagePanel.appendOutln(" ... ");
                
                controller.updateRulesPanel(1);
                controller.getTestingDebuggingTabbedPane().clearTestCaseExecutionPanel();
                controller.setTestCaseExecutionPanel(null);
                
                if (testProject != null) {
                    if (testProject.getTestReport() != null){
                        testProject.getTestReport().dispose();
                        testProject.setTestReport(null);
                    }

                    MemoryManager.gc10();
                         
                    TestCaseExecutionEngine testCaseExecEngine = new TestCaseExecutionEngine(
                            testProject);
                    testCaseExecEngine.executeAllTestCases();
                    displayTestCaseExecutionPanel();
                    enableTestReportMenus();
                    MessagePanel.appendSuccessln("done.");
                } else
                    MessagePanel
                            .appendErrorln("Test cases could not be executed, since test project is empty.");
          
            }

        }.start();
    }

    protected void enableTestReportMenus() {
        controller.getMenu().getShowTestReportMenuItem().setEnabled(true);
        controller.getMenu().getSaveTestReportMenuItem().setEnabled(true);
    }

    /**
     * Presents results of execution of all test cases in
     * TestCaseExecutionPanel.
     */
    private void displayTestCaseExecutionPanel() {
        TestCaseExecutionPanel tcExecPanel = new TestCaseExecutionPanel(testProject);
        controller.getTestCaseExecutionPanel().updateTestRunsPanel();
        Vector<TestCase> testCases = testProject.getTestCases();
        controller.getTestCaseExecutionPanel().setTestCasesList(
                new TestCasesExecResultJList(testCases, testProject.getTestReport()));

        controller.getTestingDebuggingTabbedPane().setComponentAt(1, tcExecPanel);
        controller.getTestingDebuggingTabbedPane().setSelectedIndex(1);
    }

    /**
     * Displays an execution result of a selectedTestCase.
     * 
     * @param selectedTestCase
     */
    public void displayTestCaseExecutionResult(TestCase selectedTestCase) {
        final TestCase testCase = selectedTestCase;
        controller.getTestCaseExecutionPanel().displayExecutedTestCasePanel(testCase);
        OntologyMapping ontologyMapping = testProject.getOntologyMapping();

        TestCaseExecutionResult tcExecResult = testProject.getTestReport()
                .getTestCaseExecutionResult(selectedTestCase.getName());
        ExecutedRulesPanel execRulesPanel = new ExecutedRulesPanel(ontologyMapping
                .getMappingRules(), tcExecResult, selectedTestCase.getName());
        controller.getOntologiesAndRulesPane().setRulesPanel(execRulesPanel);
    }

    /**
     * Updates GUI after the new Tab in TestingDebuggingTabbedPane has been
     * selected.
     * 
     * @param tabbedPaneSelectedIndex
     */
    public void updateGUIAfterNewTabSelection(int tabbedPaneSelectedIndex) {

        if (testProject == null)
            return;

        // "Test case editor" Tab is selected
        if (tabbedPaneSelectedIndex == 0) {
            controller.getMenu().getEditTestCasesMenu().setEnabled(true);

        }

        // "Test case execution" Tab is selected
        else if (tabbedPaneSelectedIndex == 1) {
            controller.getMenu().getEditTestCasesMenu().setEnabled(false);
        }

        // "Debugging" Tab is selected
        else if (tabbedPaneSelectedIndex == 2) {
            controller.getMenu().getEditTestCasesMenu().setEnabled(false);
        }

        updateRulesPanel(tabbedPaneSelectedIndex);
        JPanel rulesPanel = controller.getOntologiesAndRulesPane().getRulesPanel();
    }

    /**
     * Updates the rules panel in OntologiesAndRulesPane, after the new Tab has
     * been selected in TestingDebuggingTabbedPane. When "Test case editor" Tab
     * is selected the RulesPanel is shown. When "Test case execution" Tab is
     * selected the ExecutedRulesPanel is shown. When "Debugging" Tab is
     * selected the SelectedRulesPanel or ExecutedRulesPanel is shown.
     * 
     * @param tabbedPaneSelectedIndex
     */
    private void updateRulesPanel(int tabbedPaneSelectedIndex) {
        OntologyMapping ontologyMapping = testProject.getOntologyMapping();

        // "Test case editor" Tab is selected
        if (tabbedPaneSelectedIndex == 0) {
            controller.getOntologiesAndRulesPane().setRulesPanel(
                    new RulesPanel(ontologyMapping, testProject.getSourceOntology()
                            .getDefaultNamespace(), testProject.getTargetOntology()
                            .getDefaultNamespace()));
        }

        // "Test case execution" Tab is selected
        else if (tabbedPaneSelectedIndex == 1) {
            // test cases have not been executed yet
            if (controller.getTestCaseExecutionPanel() == null) {
                controller.getOntologiesAndRulesPane().setRulesPanel(
                        new RulesPanel(ontologyMapping, testProject.getSourceOntology()
                                .getDefaultNamespace(), testProject.getTargetOntology()
                                .getDefaultNamespace()));
                return;
            }

            TestCase selectedTestCase = (TestCase) controller.getTestCaseExecutionPanel()
                    .getTestCasesJList().getSelectedValue();

            TestCaseExecutionResult tcExecResult = testProject.getTestReport()
                    .getTestCaseExecutionResult(selectedTestCase.getName());
            ExecutedRulesPanel execRulesPanel = new ExecutedRulesPanel(ontologyMapping
                    .getMappingRules(), tcExecResult, selectedTestCase.getName());
            controller.getOntologiesAndRulesPane().setRulesPanel(execRulesPanel);
        }

        // "Debugging" Tab is selected
        else if (tabbedPaneSelectedIndex == 2) {
            // No rules for debugging have been selected yet
            if (controller.getRuleDebugger() == null) {
                EmptyPanelWithText emptyPanel = new EmptyPanelWithText(GUIConstants.NO_RULES_SELECTED);
                controller.getOntologiesAndRulesPane().setRulesPanel(emptyPanel);
            } else {
                if (isAlreadyDebuggedRulesLastShown())
                    displayDebuggedRules();
                else
                    displaySelectedRules();
            }
        }
    }

    /**
     * Shows test report in a separate frame.
     */
    public void showTestReport() {
        TestReport testReport = testProject.getTestReport();
        if (testReport != null)
            new TestReportTextPreview("Test report", testReport.getTestReportText());
    }

    /**
     * Shows JFileChooser and saves test report in a file, determined by a user.
     */
    public void saveTestReport() {
        String defaultPath = testProject.getProjDirectoryAbsPath();
        String testReportPath = choosePath(GUIConstants.SAVE_TEST_REPORT, defaultPath,
                JFileChooser.SAVE_DIALOG, controller.getFrame(), false);

        if (testReportPath == null)
            return;

        MessagePanel.appendOut("Saving test report in ");
        MessagePanel.appendOutBlue(testReportPath);
        MessagePanel.appendOutln(" ... ");
        String testReportText = testProject.getTestReport().getTestReportText();
        saveFile(testReportPath, testReportText);
        MessagePanel.appendSuccessln("done.");
    }

    // -----------------------------------------------------------------------------

    /**
     * Shows a dialog, in which a user can select the rules to be debugged.
     * Shows selected rules in the OntologiesAndRulesPane.
     */
    public void selectRulesToBeDebugged() {
        Vector<MappingRule> alreadySelectedRules = null;
        if (controller.getRuleDebugger() != null)
            alreadySelectedRules = controller.getTestProject().getRulesToDebug();

        RuleSelectionDialog dialog = new RuleSelectionDialog(null,
                testProject.getOntologyMapping(), alreadySelectedRules);
        dialog.setVisible(true);

        if (dialog.getReturnStatus() == NewTestCaseDialog.RET_CANCEL)
            return;

        controller.getTestingDebuggingTabbedPane().setSelectedIndex(2);
        Vector<MappingRule> selectedRules = dialog.getSelectedRules();

        if (controller.getRuleDebugger() == null)
            new RuleDebugger(testProject, testProjectLoader);

        if (controller.getDebuggingPanel() == null) {
            new DebuggingPanel(testProject);
            controller.getTestingDebuggingTabbedPane().setComponentAt(2,
                    controller.getDebuggingPanel());
        }

        controller.getTestProject().setRulesToDebug(selectedRules);
        displaySelectedRules();

        controller.getDebuggingPanel().cleanDebuggingResultPanels();
        controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(false);

        controller.getTestingDebuggingTabbedPane().setSelectedIndex(2);
        checkIfDebuggingCanBeStarted();
    }

    /**
     * Displays an ExecutedRulesPanel, containing the rules, which have been
     * debugged and information, which rules have been fired.
     */
    private void displayDebuggedRules() {
        Vector<MappingRule> debuggedRules = controller.getTestProject().getRulesToDebug();
        RulesExecutionResult rulesDebuggingResult = controller.getTestProject()
                .getLastDebuggingResult();
        ExecutedRulesPanel execRulesPanel = new ExecutedRulesPanel(debuggedRules,
                rulesDebuggingResult, null);
        controller.getOntologiesAndRulesPane().setRulesPanel(execRulesPanel);
        controller.setAlreadyDebuggedRulesLastShown(true);
    }

    /**
     * Displays a SelectedRulesPanel, containing the rules, which have been
     * selected the last time RuleSelectionDialog has been shown.
     */
    public void displaySelectedRules() {
        Vector<MappingRule> mRules = controller.getTestProject().getRulesToDebug();
        SelectedRulesPanel selRulesPanel = new SelectedRulesPanel(mRules);
        controller.getOntologiesAndRulesPane().setRulesPanel(selRulesPanel);
        controller.setAlreadyDebuggedRulesLastShown(false);
    }

    /**
     * Shows a file chooser, with which a user can select a file, containing
     * instances of source ontology, which will be used for debugging. Shows
     * selected instances in DebuggingPanel.
     */
    public void loadInstancesForDebugging() {

        String instPath = controller.choosePath(GUIConstants.SELECT_INPUT_INSTANCES_FILE, testProject
                .getProjDirectoryAbsPath(), JFileChooser.OPEN_DIALOG, controller.getFrame(), false);

        // A user has pressed Cancel
        if (instPath == null)
            return;

        if (controller.getRuleDebugger() == null)
            new RuleDebugger(testProject, testProjectLoader);

        if (controller.getDebuggingPanel() == null) {
            new DebuggingPanel(testProject);
            controller.getTestingDebuggingTabbedPane().setComponentAt(2,
                    controller.getDebuggingPanel());
        }
        // The output instances panel will be cleaned before model loading is
        // performed
        controller.getDebuggingPanel().cleanDebuggingResultPanels();
        displaySelectedRules();
        controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(false);

        MessagePanel.appendOut("Loading input instances file ");
        MessagePanel.appendOutBlue(instPath);
        MessagePanel.appendOut(" ... ");

        JenaOWLModel inputInstancesOWLModel = null;
        try {
            if (controller.getRuleDebugger().getInstancesOWLModel() != null)
                controller.getRuleDebugger().getInstancesOWLModel().dispose();
            
            inputInstancesOWLModel = testProjectLoader.loadInputInstancesJenaOWLModel(instPath);
            controller.getRuleDebugger().setInstancesOWLModel(inputInstancesOWLModel);
            System.gc();
        } catch (TestProjectLoaderException e) {            
            e.printStackTrace();
            controller.getRuleDebugger().setInstancesOWLModel(null);
            controller.getDebuggingPanel().cleanInputInstancesPanel();
            controller.getMenu().getStartDebuggingMenuItem().setEnabled(false);
            MessagePanel.appendErrorln("\t" + e.getMessage());
            MessagePanel.appendErrorln("Input instances file could not be loaded.");
            return;
        }

        // not sure if it can be null
        if (inputInstancesOWLModel == null) {
            controller.getRuleDebugger().setInstancesOWLModel(null);
            controller.getDebuggingPanel().cleanInputInstancesPanel();
            controller.getMenu().getStartDebuggingMenuItem().setEnabled(false);
            MessagePanel.appendErrorln("Input instances file could not be loaded.");
            return;
        }

        InstancesPanel inputInstancesPanel = new InstancesPanel(inputInstancesOWLModel, null,
                TestProject.INSTANCES_TYPE_SOURCE_ONTOLOGY);
        controller.getDebuggingPanel().displayInputInstances(inputInstancesPanel);

        controller.getTestingDebuggingTabbedPane().setSelectedIndex(2);
        checkIfDebuggingCanBeStarted();

        MessagePanel.appendSuccessln("done.");
    }

    /**
     * Checks if debugging can be started and activates corresponding menu item.
     */
    private void checkIfDebuggingCanBeStarted() {
        if (controller.getTestProject().getRulesToDebug() != null
                && controller.getTestProject().getRulesToDebug().size() > 0
                && controller.getRuleDebugger().getInstancesOWLModel() != null)

            controller.getMenu().getStartDebuggingMenuItem().setEnabled(true);
        else
            controller.getMenu().getStartDebuggingMenuItem().setEnabled(false);
    }

    /**
     * Starts debugging, using selected rules and input ontology instances.
     */
    public synchronized void debugSelectedRules() {
        new Thread() {
            public void run() {                
                MessagePanel.appendOut("Debugging selected rules in ");
                MessagePanel.appendOutBlue(testProject.getProjName());
                MessagePanel.appendOutln(" ... ");

                controller.getDebuggingPanel().cleanDebuggingResultPanels();
                controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(false);
                controller.displaySelectedRules();
                
                RuleDebugger debugger = controller.getRuleDebugger();
                // actually this should not happen
                if (debugger == null) {
                    MessagePanel.appendErrorln("Rules cannot be debugged.");
                    return;
                }
                debugger.setRulesToDebug(controller.getTestProject().getRulesToDebug());
                RulesExecutionResult ruleExecutionResult = null;
                try {
                    ruleExecutionResult = debugger.debug();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                controller.getTestProject().setLastDebuggingResult(ruleExecutionResult);
                if (ruleExecutionResult == null) {
                    MessagePanel.appendErrorln("Rules cannot be debugged.");
                    return;
                }

                displayDebuggingResult(ruleExecutionResult);
                controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(true);

                MessagePanel.appendSuccessln("done.");
            }
        }.start();
    }

    /**
     * Displays result of performing debugging operation: instances of output
     * ontology, consistency check results and information, which rules have
     * been fired.
     * 
     * @param ruleExecutionResult
     */
    protected void displayDebuggingResult(RulesExecutionResult ruleExecutionResult) {
        // display consistency check results
        controller.getDebuggingPanel().displayConsistencyCheckPanel(ruleExecutionResult);
        controller.getFrame().repaint();

        // display output instances
        InstancesPanel outputInstancesPanel = new InstancesPanel(ruleExecutionResult
                .getResultingOWLModelAsserted(), ruleExecutionResult,
                TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);
        controller.getDebuggingPanel().displayOutputInstances(outputInstancesPanel);

        displayDebuggedRules();
    }

    /**
     * Cleans the panels, containing instances of output ontology and
     * consistency check results.
     */
    public void cleanDebuggingOutput() {
        controller.getDebuggingPanel().cleanDebuggingResultPanels();
        displaySelectedRules();
        controller.getMenu().getCleanPrevDebuggingOutputMenuItem().setEnabled(false);
    }

    /**
     * Opens file chooser dialog, having the specified title or default path.
     * The dialog can either be of type JFileChooser.OPEN_DIALOG or
     * JFileChooser.SAVE_DIALOG.
     * 
     * @param dialogTitle
     * @param defaultPath
     * @param dialogType
     * @return
     */
    public String choosePath(String dialogTitle, String defaultPath, int dialogType,
            Component parent, boolean directoriesOnly) {

        // select target directory
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);

        // show default target dir
        if (defaultPath != null) {
            File defaultDir = new File(defaultPath);
            if (defaultDir.exists() && defaultDir.isDirectory()) {
                fileChooser.setSelectedFile(defaultDir);
            }
        }

        if (directoriesOnly) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }

        int returnValue = -125;
        if (dialogType == JFileChooser.OPEN_DIALOG)
            returnValue = fileChooser.showOpenDialog(parent);
        else if (dialogType == JFileChooser.SAVE_DIALOG)
            returnValue = fileChooser.showSaveDialog(parent);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsoluteFile().getAbsolutePath();

        } else {
            return null;
        }
    }

    /**
     * Saves textString in a file, identified by filePath.
     * 
     * @param filePath
     * @param textString
     */
    public void saveFile(String filePath, String textString) {
        File reportFile = new File(filePath);
        FileWriter fw;
        try {
            fw = new FileWriter(reportFile);
            fw.write(textString);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOntologiesAndRulesPane(OntologiesAndRulesSplitPane parameterPane) {
        this.ontologiesAndRulesPane = parameterPane;
    }

    public OntologiesAndRulesSplitPane getOntologiesAndRulesPane() {
        return ontologiesAndRulesPane;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public JFrame getFrame() {
        return frame;
    }

    public TestingDebuggingTabbedPane getTestingDebuggingTabbedPane() {
        return testingDebuggingTabbedPane;
    }

    public void setTestingDebuggingTabbedPane(TestingDebuggingTabbedPane testingDebuggingTabbedPane) {
        this.testingDebuggingTabbedPane = testingDebuggingTabbedPane;
    }

    public TestCaseEditorPanel getTestCaseEditorPanel() {
        return testCaseEditorPanel;
    }

    public void setTestCaseEditorPanel(TestCaseEditorPanel testCaseEditorPanel) {
        this.testCaseEditorPanel = testCaseEditorPanel;
    }

    public TestCaseExecutionPanel getTestCaseExecutionPanel() {
        return testCaseExecutionPanel;
    }

    public void setTestCaseExecutionPanel(TestCaseExecutionPanel testCaseExecutionPanel) {
        this.testCaseExecutionPanel = testCaseExecutionPanel;
    }

    public DebuggingPanel getDebuggingPanel() {
        return debuggingPanel;
    }

    public void setDebuggingPanel(DebuggingPanel debuggingPanel) {
        this.debuggingPanel = debuggingPanel;
    }

    public RuleDebugger getRuleDebugger() {
        return ruleDebugger;
    }

    public void setRuleDebugger(RuleDebugger ruleDebugger) {
        this.ruleDebugger = ruleDebugger;
    }

    public boolean isAlreadyDebuggedRulesLastShown() {
        return alreadyDebuggedRulesLastShown;
    }

    public void setAlreadyDebuggedRulesLastShown(boolean debuggedRulesLastShown) {
        this.alreadyDebuggedRulesLastShown = debuggedRulesLastShown;
    }

    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
    }

    public void setTestCaseCreator(TestCaseCreator testCaseCreator) {
        this.testCaseCreator = testCaseCreator;
    }

    public void setTestProjectCreator(TestProjectCreator testProjectCreator) {
        this.testProjectCreator = testProjectCreator;
    }

    public void setTestProjectLoader(TestProjectLoader testProjectLoader) {
        this.testProjectLoader = testProjectLoader;
    }

    public TestProjectLoader getTestProjectLoader() {
        return testProjectLoader;
    }

}
