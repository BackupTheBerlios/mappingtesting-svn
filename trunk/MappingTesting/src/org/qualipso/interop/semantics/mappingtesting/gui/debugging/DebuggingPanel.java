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

package org.qualipso.interop.semantics.mappingtesting.gui.debugging;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.EmptyPanelWithText;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.InstancesPanel;
import org.qualipso.interop.semantics.mappingtesting.model.RulesExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

/**
 * A panel, used for debugging. This panel displays input instaces, output
 * instances and consistency check results.
 */
public class DebuggingPanel extends JPanel {

    private JSplitPane input_outputInstancesSplitPane;
    private JScrollPane inputInstancesScrollPane;
    private JScrollPane outputInstancesScrollPane;
    private JComponent consistencyPanel;

    // IF JPANEL IS USED INSTEAD OF JScrollPane, IT RESULTS IN A STRANGE LAYOUT
    // private JPanel executedTestCaseContainerPanel;

    /**
     * Creates a DebuggingPanel.
     * 
     * @param testProject
     */
    public DebuggingPanel(TestProject testProject) {
        Controller.getInstance().setDebuggingPanel(this);

        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.setLayout(new BorderLayout());

        // embedded scrollPanes
        inputInstancesScrollPane = new JScrollPane();
        inputInstancesScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.INPUT_INSTANCES_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        inputInstancesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        JPanel emptyPanel_1 = new JPanel();
        emptyPanel_1.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        inputInstancesScrollPane.getViewport().add(emptyPanel_1);

        outputInstancesScrollPane = new JScrollPane();
        outputInstancesScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.OUTPUT_INSTANCES_BORDER_TITLE,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));
        outputInstancesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JPanel emptyPanel_2 = new JPanel();
        emptyPanel_2.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        outputInstancesScrollPane.getViewport().add(emptyPanel_2);

        input_outputInstancesSplitPane = new JSplitPane();
        input_outputInstancesSplitPane.setBorder(null);
        input_outputInstancesSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        input_outputInstancesSplitPane.setDividerLocation(GUIConstants
                .DEBUGGING_PANEL_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit().getScreenSize()
                        .getWidth()));
        input_outputInstancesSplitPane.setContinuousLayout(true);
        input_outputInstancesSplitPane.setLeftComponent(inputInstancesScrollPane);
        input_outputInstancesSplitPane.setRightComponent(outputInstancesScrollPane);

        consistencyPanel = new JPanel(new BorderLayout());
        consistencyPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        this.add(consistencyPanel, BorderLayout.NORTH);
        this.add(input_outputInstancesSplitPane, BorderLayout.CENTER);
    }

    public void displayInputInstances(InstancesPanel instancesPanel) {
        inputInstancesScrollPane.getViewport().removeAll();
        inputInstancesScrollPane.getViewport().add(instancesPanel);
    }

    public void displayOutputInstances(InstancesPanel instancesPanel) {
        outputInstancesScrollPane.getViewport().removeAll();
        outputInstancesScrollPane.getViewport().add(instancesPanel);
    }

    /**
     * Cleans the panel containing results of previous debugging operations:
     * output instances panel and consistency check panels.
     */
    public void cleanDebuggingResultPanels() {
        outputInstancesScrollPane.getViewport().removeAll();
        EmptyPanelWithText emptyPanel = new EmptyPanelWithText("");
        outputInstancesScrollPane.getViewport().add(emptyPanel);
        consistencyPanel.setBorder(null);
        consistencyPanel.removeAll();
    }

    /**
     * Cleans input instances panel.
     */
    public void cleanInputInstancesPanel() {
        inputInstancesScrollPane.getViewport().removeAll();
        EmptyPanelWithText emptyPanel = new EmptyPanelWithText("");
        inputInstancesScrollPane.getViewport().add(emptyPanel);
    }

    /**
     * Displays a JPanel, on which consistency check results of input instances
     * and output instances are represented.
     * 
     * @param ruleExecutionResult
     */
    public void displayConsistencyCheckPanel(RulesExecutionResult ruleExecutionResult) {
        consistencyPanel
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.TCCOVERAGE_AND_CONSISTENCY_BORDER_TITLE,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        GUIConstants.BORDER_FONT, GUIConstants.BORDER_TITLE_COLOR));

        JLabel inputConsCheckResultLabel = new JLabel(GUIConstants.INPUT_INSTANCES_BORDER_TITLE + ": "
                + ruleExecutionResult.getInputConsistencyCheckResult());
        consistencyPanel.add(inputConsCheckResultLabel, BorderLayout.NORTH);

        JLabel outputConsCheckResultLabel = new JLabel(GUIConstants.EXP_OUTPUT_INSTANCES_BORDER_TITLE
                + ": " + ruleExecutionResult.getResultingModelConsistencyCheckResult());
        consistencyPanel.add(outputConsCheckResultLabel, BorderLayout.CENTER);
    }

}
