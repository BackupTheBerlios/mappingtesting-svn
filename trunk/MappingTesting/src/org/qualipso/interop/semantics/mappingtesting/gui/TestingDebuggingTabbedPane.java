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

package org.qualipso.interop.semantics.mappingtesting.gui;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;

/**
 * A tabbed pane, containing "Test case editor", "Test case execution" and
 * "Debugging" panels
 * 
 */
public class TestingDebuggingTabbedPane extends JTabbedPane implements ChangeListener {

    public TestingDebuggingTabbedPane() {
        Controller.getInstance().setTestingDebuggingTabbedPane(this);

        JPanel emptyTestCaseEditorPanel = new JPanel();
        emptyTestCaseEditorPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JPanel emptyTestCaseExecutionPanel = new JPanel();
        emptyTestCaseExecutionPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        JPanel emptyDebuggingPanel = new JPanel();
        emptyDebuggingPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        addTab("Test case editor", emptyTestCaseEditorPanel);
        addTab("Test case execution", emptyTestCaseExecutionPanel);
        addTab("Debugging", emptyDebuggingPanel);

        setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        setBorder(new LineBorder(Color.GRAY));
        addChangeListener(this);

        // The following line enables to use scrolling tabs.
    }

    public void stateChanged(ChangeEvent e) {
        Controller controller = Controller.getInstance();
        controller.updateGUIAfterNewTabSelection(getSelectedIndex());
    }
    
    /**
     * Sets empty panel on all the three tabs.
     */
    public void clearAll(){
        JPanel emptyTestCaseEditorPanel = new JPanel();
        emptyTestCaseEditorPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        setComponentAt(0, emptyTestCaseEditorPanel);
        
        JPanel emptyTestCaseExecutionPanel = new JPanel();
        emptyTestCaseExecutionPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        setComponentAt(1, emptyTestCaseExecutionPanel);
        
        JPanel emptyDebuggingPanel = new JPanel();
        emptyDebuggingPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        setComponentAt(2, emptyDebuggingPanel);
        
        setSelectedIndex(0);        
    }
    
    /**
     * Sets empty panel on the "Test case execution" tab.
     */
    public void clearTestCaseExecutionPanel(){        
        JPanel emptyTestCaseExecutionPanel = new JPanel();
        emptyTestCaseExecutionPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        setComponentAt(1, emptyTestCaseExecutionPanel);   
    }
}
