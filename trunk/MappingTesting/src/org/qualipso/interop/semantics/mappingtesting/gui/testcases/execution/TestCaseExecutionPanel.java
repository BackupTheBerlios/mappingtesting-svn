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

package org.qualipso.interop.semantics.mappingtesting.gui.testcases.execution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.InstancesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.editing.TestCasesJList;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestCaseExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;
import org.qualipso.interop.semantics.mappingtesting.model.TestReport;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * On this panel test case execution results are represented. This panel is
 * placed on the "Test case execution" Tab in TestingDebuggingTabbedPane.
 * 
 */
public class TestCaseExecutionPanel extends JPanel {

    private TestProject testProject;

    private TestReport testReport;

    private JSplitPane tcList_ExecResultsSplitPane;

    private JScrollPane testCasesListScrollPane;

    private TestCasesJList testCasesJList;

    private JPanel testRunsPanel;

    /**
     * This hashtable contains all the executedTestCasePanels, which have
     * already been created and shown as a result of selection of corresponding
     * test case in test cases list. The panels are stored in this table, so
     * that they do not have to be created again, when the same test case is
     * selected once again.
     */
    private Hashtable executedTestCasePanels;

    private JScrollPane executedTestCaseScrollPane;

    // IF JPANEL IS USED INSTEAD OF JScrollPane, IT RESULTS IN A STRANGE LAYOUT
    // private JPanel executedTestCaseContainerPanel;

    /**
     * Creates a TestCaseExecutionPanel, which initially displays an empty test
     * runs panel and an empty list of test cases.
     * 
     * @param testProject
     */
    public TestCaseExecutionPanel(TestProject testProject) {

        Controller.getInstance().setTestCaseExecutionPanel(this);

        this.testProject = testProject;
        this.testReport = testProject.getTestReport();

        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.setLayout(new BorderLayout());

        testRunsPanel = new JPanel();
        testRunsPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        testRunsPanel.setBorder(new LineBorder(Color.GRAY, 1));
        this.add(testRunsPanel, BorderLayout.NORTH);

        tcList_ExecResultsSplitPane = new JSplitPane();
        tcList_ExecResultsSplitPane.setBorder(null);
        tcList_ExecResultsSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        tcList_ExecResultsSplitPane.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        tcList_ExecResultsSplitPane.setContinuousLayout(true);

        testCasesListScrollPane = new JScrollPane();
        testCasesListScrollPane.setDoubleBuffered(true);
        testCasesListScrollPane.setAutoscrolls(true);
        testCasesListScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.TEST_CASES_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        testCasesListScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        executedTestCaseScrollPane = new JScrollPane();
        executedTestCaseScrollPane.setBorder(null);
        executedTestCaseScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        // executedTestCaseContainerPanel = new JPanel();

        tcList_ExecResultsSplitPane.setLeftComponent(testCasesListScrollPane);
        tcList_ExecResultsSplitPane.setRightComponent(executedTestCaseScrollPane);

        this.add(tcList_ExecResultsSplitPane, BorderLayout.CENTER);

        executedTestCasePanels = new Hashtable();
    }

    /**
     * Adds information about number of executed test cases and number of test
     * cases, which failed and succeeded.
     */
    public void updateTestRunsPanel() {
        String text = "Number of test cases: ";
        int testCasesNumber = testProject.getTestCasesNumber();
        int failedTCNumber = testReport.getNumberOfFailedTestCases();
        int succeededTCNumber = testReport.getNumberOfSucceededTestCases();
        text += testCasesNumber + "      ";
        text += "Failed: " + failedTCNumber + "/" + testCasesNumber + "      ";
        text += "Succeeded: " + succeededTCNumber + "/" + testCasesNumber + "      ";
//        text += "Errors: ???";
        text += "Overall test case coverage: " + testReport.getOverallTestCaseCoverage() + "%";
        testRunsPanel.add(new JLabel(text));
    }

    /**
     * Sets testCasesJList. Updates testCasesListScrollPane, which contains a
     * list of all the test cases in the project.
     * 
     * @param jList
     */
    public void setTestCasesList(TestCasesJList jList) {
        this.testCasesJList = jList;
        testCasesListScrollPane.getViewport().removeAll();
        testCasesListScrollPane.getViewport().add(jList);
    }

    public TestCasesJList getTestCasesJList() {
        return testCasesJList;
    }

    /**
     * Displays a panel, containing all the information about the execution of
     * the testCase.
     * 
     * @param testCase
     */
    public void displayExecutedTestCasePanel(TestCase testCase) {
        String testCaseName = testCase.getName();
        JPanel executedTestCasePanel = (JPanel) executedTestCasePanels.get(testCaseName);
        if (executedTestCasePanel == null) {
            executedTestCasePanel = createExecutedTestCasePanel(testCase);
            executedTestCasePanels.put(testCaseName, executedTestCasePanel);
        }
        // executedTestCaseContainerPanel.removeAll();
        // executedTestCaseContainerPanel.add(executedTestCasePanel);
        executedTestCaseScrollPane.getViewport().removeAll();
        executedTestCaseScrollPane.getViewport().add(executedTestCasePanel);

        // THERE WERE PROBLEMS, THAT THE DIVIDER LOCATION WAS MOVING STRANGELY,
        // WHEN setRightComponent(executedTestCasePanel) WAS CALLED. THAT IS WHY
        // THIS CODE HAS BEEN COMMENTED AND executedTestCasePanel IS PUT
        // INTO A executedTestCaseScrollPane.
    }

    /**
     * Creates a JPanel, on which execution results of a testCase are
     * represented.
     * 
     * @param testCase
     * @return
     */
    private JPanel createExecutedTestCasePanel(TestCase testCase) {
        JPanel executedTestCasePanel = new JPanel();
        executedTestCasePanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        executedTestCasePanel.setLayout(new BorderLayout());

        JPanel consistencyCheckResultsPanel = createConsistencyCheckResultsPanel(testCase);

        // Split panes
        JSplitPane input_OutputInstancesSplitPane = new JSplitPane();
        input_OutputInstancesSplitPane.setBorder(null);
        input_OutputInstancesSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        input_OutputInstancesSplitPane.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        input_OutputInstancesSplitPane.setContinuousLayout(true);

        JSplitPane exp_ActualOutputInstancesSplitPane = new JSplitPane();
        exp_ActualOutputInstancesSplitPane.setBorder(null);
        exp_ActualOutputInstancesSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        exp_ActualOutputInstancesSplitPane.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        exp_ActualOutputInstancesSplitPane.setContinuousLayout(true);

        // embedded scrollPanes
        JScrollPane inputInstancesScrollPane = new JScrollPane();
        inputInstancesScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.INPUT_INSTANCES_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        inputInstancesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JScrollPane expOutputInstancesScrollPane = new JScrollPane();
        expOutputInstancesScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.EXP_OUTPUT_INSTANCES_BORDER_TITLE,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));
        expOutputInstancesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JScrollPane actualOutputInstancesScrollPane = new JScrollPane();
        actualOutputInstancesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, GUIConstants.ACTUAL_OUTPUT_INSTANCES_BORDER_TITLE,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));
        actualOutputInstancesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        exp_ActualOutputInstancesSplitPane.setLeftComponent(expOutputInstancesScrollPane);
        exp_ActualOutputInstancesSplitPane.setRightComponent(actualOutputInstancesScrollPane);

        input_OutputInstancesSplitPane.setLeftComponent(inputInstancesScrollPane);
        input_OutputInstancesSplitPane.setRightComponent(exp_ActualOutputInstancesSplitPane);

        executedTestCasePanel.add(consistencyCheckResultsPanel, BorderLayout.NORTH);
        executedTestCasePanel.add(input_OutputInstancesSplitPane, BorderLayout.CENTER);

        // Create instances panels for input instances, expected and actual
        // output instances.
        InstancesPanel inputInstancesPanel = new InstancesPanel(testCase
                .getInputInstancesOWLModelAsserted(), testCase, TestProject.INSTANCES_TYPE_SOURCE_ONTOLOGY);
        inputInstancesScrollPane.getViewport().add(inputInstancesPanel);

        InstancesPanel expectedOutputInstancesPanel = new InstancesPanel(testCase
                .getExpectedOutputInstancesOWLModelAsserted(), testCase,
                TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);
        expOutputInstancesScrollPane.getViewport().add(expectedOutputInstancesPanel);

        TestCaseExecutionResult tcExecResult = testReport.getTestCaseExecutionResult(testCase
                .getName());
        JenaOWLModel actualOutputInstancesOWLModel = tcExecResult.getResultingOWLModelAsserted();
        InstancesPanel actualOutputInstancesPanel = new InstancesPanel(
                actualOutputInstancesOWLModel, tcExecResult, TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);
        actualOutputInstancesScrollPane.getViewport().add(actualOutputInstancesPanel);

//      The preferred width is set in order that tool layout looks correctly 
        int preferredHeight = executedTestCasePanel.getPreferredSize().height;
        Dimension preferredSize = new Dimension(GUIConstants
                .THREE_FOURTH_SCREEN_WIDTH((int) Toolkit.getDefaultToolkit().getScreenSize()
                        .getWidth() + 20), preferredHeight);
        executedTestCasePanel.setPreferredSize(preferredSize);
        
        return executedTestCasePanel;
    }

    /**
     * Creates a JPanel, on which consistency check results of input instances,
     * expected and actual output instances are represented
     * 
     * @param testCase
     * @return
     */
    private JPanel createConsistencyCheckResultsPanel(TestCase testCase) {
        
        TestCaseExecutionResult tcExecResult = testReport.getTestCaseExecutionResult(testCase
                .getName());
        JPanel panel = new JPanel(new BorderLayout());        
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.TCCOVERAGE_AND_CONSISTENCY_BORDER_TITLE,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));

//      Test case coverage panel and CONSISTENCY_CHECK_RESULTS label
        JPanel tcCoveragePanel = new JPanel(new BorderLayout());
        tcCoveragePanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR); 
        JLabel tcCoverageLabel = new JLabel(GUIConstants.TEST_CASE_COVERAGE + " "
                + tcExecResult.getTestCaseCoverage() + "%");
        tcCoveragePanel.add(tcCoverageLabel, BorderLayout.NORTH);
        tcCoveragePanel.add(new JLabel("_______________________"), BorderLayout.CENTER);
        tcCoveragePanel.add(new JLabel(GUIConstants.CONSISTENCY_CHECK_RESULTS), BorderLayout.SOUTH);
   
//      consistency check results panel  
        JPanel consistencyPanel = new JPanel(new BorderLayout());
        consistencyPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JLabel inputConsCheckResultLabel = new JLabel(GUIConstants.INPUT_INSTANCES_BORDER_TITLE + ": "
                + tcExecResult.getInputConsistencyCheckResult());
        consistencyPanel.add(inputConsCheckResultLabel, BorderLayout.NORTH);

        JLabel expOutputConsCheckResultLabel = new JLabel(
                GUIConstants.EXP_OUTPUT_INSTANCES_BORDER_TITLE + ": "
                        + tcExecResult.getOutputConsistencyCheckResult());
        consistencyPanel.add(expOutputConsCheckResultLabel, BorderLayout.CENTER);

        JLabel actualOutputConsCheckResultLabel = new JLabel(
                GUIConstants.ACTUAL_OUTPUT_INSTANCES_BORDER_TITLE + ": "
                        + tcExecResult.getResultingModelConsistencyCheckResult());
        consistencyPanel.add(actualOutputConsCheckResultLabel, BorderLayout.SOUTH);
        
        panel.add(tcCoveragePanel, BorderLayout.NORTH);
        panel.add(consistencyPanel, BorderLayout.SOUTH);
        return panel;
    }

}
