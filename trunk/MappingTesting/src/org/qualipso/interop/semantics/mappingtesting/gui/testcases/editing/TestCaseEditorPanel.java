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

package org.qualipso.interop.semantics.mappingtesting.gui.testcases.editing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.EmptyPanelWithText;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.InstancesPanel;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

/**
 * On this panel test cases in a test project and information about test cases
 * are represented. This panel is placed on the "Test case editor" Tab in
 * TestingDebuggingTabbedPane.
 */
public class TestCaseEditorPanel extends JPanel {

    private TestProject testProject;

    private JSplitPane tcList_tcInfoSplitPane;

    private JScrollPane testCasesListScrollPane;
    private TestCasesJList testCasesJList;

    /**
     * This hashtable contains all the testCaseInfoPanels, which have already
     * been created and shown as a result of selection of corresponding test
     * case in test cases list. The panels are stored in this table, so that
     * they do not have to be created again, when the same test case is selected
     * once again.
     */
    private Hashtable testCaseInfoPanels;

    private JScrollPane testCaseInfoScrollPane;
//    private JPanel testCaseInfoScrollPanel_Test;


    // IF JPANEL IS USED INSTEAD OF JScrollPane, IT RESULTS IN A STRANGE LAYOUT
    // private JPanel executedTestCaseContainerPanel;

    /**
     * Creates a TestCaseEditorPanel, which initially displays an empty list of
     * test cases.
     * 
     * @param testProject
     */
    public TestCaseEditorPanel(TestProject testProject) {

        Controller.getInstance().setTestCaseEditorPanel(this);

        this.testProject = testProject;

        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.setLayout(new BorderLayout());

        tcList_tcInfoSplitPane = new JSplitPane();
        tcList_tcInfoSplitPane.setBorder(null);
        tcList_tcInfoSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        tcList_tcInfoSplitPane.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        tcList_tcInfoSplitPane.setContinuousLayout(true);

        testCasesListScrollPane = new JScrollPane();
        testCasesListScrollPane.setDoubleBuffered(true);
        testCasesListScrollPane.setAutoscrolls(true);
        testCasesListScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.TEST_CASES_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        testCasesListScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        testCaseInfoScrollPane = new JScrollPane();
        testCaseInfoScrollPane.setBorder(null);
        testCaseInfoScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        Dimension minimumSize = new Dimension(100, 100);
        testCaseInfoScrollPane.setMinimumSize(minimumSize);
        
        //        testCaseInfoScrollPanel_Test = new JPanel();
        
        tcList_tcInfoSplitPane.setLeftComponent(testCasesListScrollPane);        
        tcList_tcInfoSplitPane.setRightComponent(testCaseInfoScrollPane);
//        tcList_tcInfoSplitPane.setRightComponent(testCaseInfoScrollPanel_Test);

        this.add(tcList_tcInfoSplitPane, BorderLayout.CENTER);

        testCaseInfoPanels = new Hashtable();
        
        testCasesJList = new TestCasesJList(testProject.getTestCases());
    }

    /**
     * Displays testCasesJList. Updates testCasesListScrollPane, which contains a
     * list of all the test cases in the project.
     */
    public void displayTestCasesList() {        
        testCasesListScrollPane.getViewport().removeAll();
        testCasesListScrollPane.getViewport().add(testCasesJList);
    }
    
    public void displayNoTestCasesMessage() {
        String text = "Test project does not contain any test cases.";
        EmptyPanelWithText panel = new EmptyPanelWithText(text);
        testCasesListScrollPane.getViewport().removeAll();
        testCasesListScrollPane.getViewport().add(panel);
    }
    
    public void displayEmptyTestCaseInfoPanel() {
        EmptyPanelWithText panel = new EmptyPanelWithText("");
        testCaseInfoScrollPane.getViewport().removeAll();
        testCaseInfoScrollPane.getViewport().add(panel);
    }

    public TestCasesJList getTestCasesJList() {
        return testCasesJList;
    }

    /**
     * Displays a panel, containing information about the testCase.
     * 
     * @param testCase
     */
    public void displayTestCaseInfoPanel(TestCase testCase) {
        String testCaseName = testCase.getName();
        JPanel testCaseInfoPanel = (JPanel) testCaseInfoPanels.get(testCaseName);
        if (testCaseInfoPanel == null) {
            testCaseInfoPanel = createTestCaseInfoPanel(testCase);
            testCaseInfoPanels.put(testCaseName, testCaseInfoPanel);
        }

        testCaseInfoScrollPane.getViewport().removeAll();
        testCaseInfoScrollPane.getViewport().add(testCaseInfoPanel);
    }

    /**
     * Creates a JPanel, on which information about a testCase (description,
     * test input and expected test output instances) is represented.
     * 
     * @param testCase
     * @return
     */
    private JPanel createTestCaseInfoPanel(TestCase testCase) {
        JPanel testCaseInfoPanel = new JPanel();
        testCaseInfoPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        testCaseInfoPanel.setLayout(new BorderLayout());

        JPanel descriptionPanel = createDescriptionPanel(testCase);

        // Split panes
        JSplitPane input_OutputInstancesSplitPane = new JSplitPane();
        input_OutputInstancesSplitPane.setBorder(null);
        input_OutputInstancesSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        input_OutputInstancesSplitPane.setDividerLocation(GUIConstants
                .INSTANCES_SPLITPANE_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        input_OutputInstancesSplitPane.setContinuousLayout(true);

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

        input_OutputInstancesSplitPane.setLeftComponent(inputInstancesScrollPane);
        input_OutputInstancesSplitPane.setRightComponent(expOutputInstancesScrollPane);

        testCaseInfoPanel.add(descriptionPanel, BorderLayout.NORTH);
        testCaseInfoPanel.add(input_OutputInstancesSplitPane, BorderLayout.CENTER);

        // Create instances panels for input instances, expected and actual
        // output instances.
        Hashtable altEntriesForInputInstances = testProject.getSrcOntAltEntries();
        InstancesPanel inputInstancesPanel = new InstancesPanel(testCase
                .getInputInstancesOWLModelAsserted(), testCase, TestProject.INSTANCES_TYPE_SOURCE_ONTOLOGY);
        inputInstancesScrollPane.getViewport().add(inputInstancesPanel);

        Hashtable altEntriesForExpOutputInstances = testProject.getSrcAndTargetOntAltEntries();
        InstancesPanel expectedOutputInstancesPanel = new InstancesPanel(testCase
                .getExpectedOutputInstancesOWLModelAsserted(), testCase,
                TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);
        expOutputInstancesScrollPane.getViewport().add(expectedOutputInstancesPanel);
        
//      The preferred width is set in order that tool layout looks correctly  
        int preferredHeight = testCaseInfoPanel.getPreferredSize().height;
        Dimension preferredSize = new Dimension(GUIConstants
                .THREE_FOURTH_SCREEN_WIDTH((int) Toolkit.getDefaultToolkit().getScreenSize()
                        .getWidth()), preferredHeight);
        testCaseInfoPanel.setPreferredSize(preferredSize);
        return testCaseInfoPanel;
    }

    /**
     * Creates a JPanel, on which description of a test case is represented.
     * 
     * @param testCase
     * @return
     */
    private JPanel createDescriptionPanel(TestCase testCase) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.DESCRIPTION_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        panel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JTextArea testCaseDescriptionTextArea = new JTextArea(1, 40);
        testCaseDescriptionTextArea.setText(testCase.getDescription());
        testCaseDescriptionTextArea.setEditable(false);
        testCaseDescriptionTextArea.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        JScrollPane scrollPane = new JScrollPane(testCaseDescriptionTextArea);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Adds a new testCase to a current TestCasesJList. 
     * @param testCase
     */
    public void addTestCaseToJList(TestCase testCase) {
        testCasesJList.addTestCase(testCase);
    }

    /**
     * Deletes testCase from a current TestCasesJList. 
     * @param testCase
     */
    public void deleteTestCaseFromJList(TestCase testCase) {
        testCasesJList.deleteTestCase(testCase);
    }

    public TestCase getSelectedTestCase() {
        Object o = testCasesJList.getSelectedValue();
        if (!(o instanceof TestCase))
            return null;
        return (TestCase) o;
    }


}
