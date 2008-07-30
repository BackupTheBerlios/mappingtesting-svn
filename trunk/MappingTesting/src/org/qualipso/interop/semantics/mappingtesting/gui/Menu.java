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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;

/**
 * Menu of the Mapping Testing Tool.
 * 
 */
public class Menu extends JMenuBar {

    private JMenu testProjectMenu;
    private JMenu editTestCasesMenu;
    private JMenu runTestCasesMenu;
    private JMenu debuggingMenu;
    
    private JMenuItem saveTestProjectMenuItem;
    private JMenuItem closeTestProjectMenuItem;

    private JMenuItem showTestReportMenuItem;
    private JMenuItem saveTestReportMenuItem;

    private JMenuItem cleanPrevDebuggingOutputMenuItem;
    private JMenuItem selectRulesToBeDebuggedMenuItem;
    private JMenuItem loadSourceOntologyInstancesMenuItem;
    private JMenuItem startDebuggingMenuItem;
    private JMenuItem deleteTestCaseMenuItem;

    public Menu() {

        this.setBorder(null);
        this.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        MenuListener menuListener = new MenuListener();

        // "Test Project" Menu
        testProjectMenu = new JMenu(GUIConstants.TEST_PROJECT_MENU);

        JMenuItem loadTestProjectMenuItem = new JMenuItem(GUIConstants.OPEN_TEST_PROJECT);
        loadTestProjectMenuItem.addActionListener(menuListener);
        testProjectMenu.add(loadTestProjectMenuItem);
        
        JMenuItem newTestProjectMenuItem = new JMenuItem(GUIConstants.NEW_TEST_PROJECT);
        newTestProjectMenuItem.addActionListener(menuListener);
        testProjectMenu.add(newTestProjectMenuItem);
        
        saveTestProjectMenuItem = new JMenuItem(GUIConstants.SAVE_TEST_PROJECT);
        saveTestProjectMenuItem.addActionListener(menuListener);
        testProjectMenu.add(saveTestProjectMenuItem);
        saveTestProjectMenuItem.setEnabled(false);
        
        closeTestProjectMenuItem = new JMenuItem(GUIConstants.CLOSE_TEST_PROJECT);
        closeTestProjectMenuItem.addActionListener(menuListener);
        testProjectMenu.add(closeTestProjectMenuItem);
        closeTestProjectMenuItem.setEnabled(false);
        

        // "Edit Test Cases" Menu
        editTestCasesMenu = new JMenu(GUIConstants.EDIT_TEST_CASES_MENU);
        editTestCasesMenu.setEnabled(false);

        JMenuItem newTestCaseMenuItem = new JMenuItem(GUIConstants.NEW_TEST_CASE);
        newTestCaseMenuItem.addActionListener(menuListener);
        editTestCasesMenu.add(newTestCaseMenuItem);

        deleteTestCaseMenuItem = new JMenuItem(GUIConstants.DELETE_TEST_CASE);
        deleteTestCaseMenuItem.setEnabled(false);
        deleteTestCaseMenuItem.addActionListener(menuListener);
        editTestCasesMenu.add(deleteTestCaseMenuItem);

        // "Run Test Cases" Menu
        runTestCasesMenu = new JMenu(GUIConstants.RUN_TEST_CASES_MENU);
        runTestCasesMenu.setEnabled(false);

        JMenuItem runAllTestCasesMenuItem = new JMenuItem(GUIConstants.RUN_ALL_TEST_CASES);
        runAllTestCasesMenuItem.addActionListener(menuListener);
        runTestCasesMenu.add(runAllTestCasesMenuItem);

        showTestReportMenuItem = new JMenuItem(GUIConstants.SHOW_TEST_REPORT);
        showTestReportMenuItem.setEnabled(false);
        showTestReportMenuItem.addActionListener(menuListener);
        runTestCasesMenu.add(showTestReportMenuItem);

        saveTestReportMenuItem = new JMenuItem(GUIConstants.SAVE_TEST_REPORT);
        saveTestReportMenuItem.setEnabled(false);
        saveTestReportMenuItem.addActionListener(menuListener);
        runTestCasesMenu.add(saveTestReportMenuItem);

        // "Debugging" Menu
        debuggingMenu = new JMenu(GUIConstants.DEBUGGING_MENU);
        debuggingMenu.setEnabled(false);

        cleanPrevDebuggingOutputMenuItem = new JMenuItem(GUIConstants.CLEAN_PREVIOUS_DEBUGGING_OUTPUT);
        cleanPrevDebuggingOutputMenuItem.setEnabled(false);
        cleanPrevDebuggingOutputMenuItem.addActionListener(menuListener);
        debuggingMenu.add(cleanPrevDebuggingOutputMenuItem);

        selectRulesToBeDebuggedMenuItem = new JMenuItem(GUIConstants.SELECT_RULES_TO_BE_DEBUGGED);
        selectRulesToBeDebuggedMenuItem.addActionListener(menuListener);
        debuggingMenu.add(selectRulesToBeDebuggedMenuItem);

        loadSourceOntologyInstancesMenuItem = new JMenuItem(
                GUIConstants.LOAD_SOURCE_ONTOLOGY_INSTANCES);
        loadSourceOntologyInstancesMenuItem.addActionListener(menuListener);
        debuggingMenu.add(loadSourceOntologyInstancesMenuItem);

        startDebuggingMenuItem = new JMenuItem(GUIConstants.START_DEBUGGING);
        startDebuggingMenuItem.setEnabled(false);
        startDebuggingMenuItem.addActionListener(menuListener);
        debuggingMenu.add(startDebuggingMenuItem);

        this.add(testProjectMenu);
        this.add(editTestCasesMenu);
        this.add(runTestCasesMenu);
        this.add(debuggingMenu);
    }

    public JMenu getRunTestCasesMenu() {
        return runTestCasesMenu;
    }

    public JMenuItem getSaveTestReportMenuItem() {
        return saveTestReportMenuItem;
    }

    public JMenuItem getShowTestReportMenuItem() {
        return showTestReportMenuItem;
    }

    public JMenu getEditTestCasesMenu() {
        return editTestCasesMenu;
    }

    public JMenuItem getCleanPrevDebuggingOutputMenuItem() {
        return cleanPrevDebuggingOutputMenuItem;
    }

    public JMenu getDebuggingMenu() {
        return debuggingMenu;
    }

    public JMenuItem getLoadSourceOntologyInstancesMenuItem() {
        return loadSourceOntologyInstancesMenuItem;
    }

    public JMenuItem getSelectRulesToBeDebuggedMenuItem() {
        return selectRulesToBeDebuggedMenuItem;
    }

    public JMenuItem getStartDebuggingMenuItem() {
        return startDebuggingMenuItem;
    }

    public JMenuItem getCloseTestProjectMenuItem() {
        return closeTestProjectMenuItem;
    }

    public JMenuItem getSaveTestProjectMenuItem() {
        return saveTestProjectMenuItem;
    }

    public JMenuItem getDeleteTestCaseMenuItem() {
        return deleteTestCaseMenuItem;
    }

}

class MenuListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        Controller controller = Controller.getInstance();

        if (e.getActionCommand().equals(GUIConstants.OPEN_TEST_PROJECT)) {
            controller.openTestProject();
        }
        else if (e.getActionCommand().equals(GUIConstants.NEW_TEST_PROJECT)) {
            controller.createNewTestProject();
        }
        else if (e.getActionCommand().equals(GUIConstants.SAVE_TEST_PROJECT)) {
            controller.saveTestProject();
        }
        else if (e.getActionCommand().equals(GUIConstants.CLOSE_TEST_PROJECT)) {
            controller.closeTestProject();
        }

        else if (e.getActionCommand().equals(GUIConstants.NEW_TEST_CASE)) {
            controller.createNewTestCase();
        }
        else if (e.getActionCommand().equals(GUIConstants.DELETE_TEST_CASE)) {
            controller.deleteTestCase();
        }

        else if (e.getActionCommand().equals(GUIConstants.RUN_ALL_TEST_CASES)) {
            controller.executeAllTestCases();
        } else if (e.getActionCommand().equals(GUIConstants.SHOW_TEST_REPORT)) {
            controller.showTestReport();
        } else if (e.getActionCommand().equals(GUIConstants.SAVE_TEST_REPORT)) {
            controller.saveTestReport();
        }

        else if (e.getActionCommand().equals(GUIConstants.CLEAN_PREVIOUS_DEBUGGING_OUTPUT)) {
            controller.cleanDebuggingOutput();
        } else if (e.getActionCommand().equals(GUIConstants.SELECT_RULES_TO_BE_DEBUGGED)) {
            controller.selectRulesToBeDebugged();
        } else if (e.getActionCommand().equals(GUIConstants.LOAD_SOURCE_ONTOLOGY_INSTANCES)) {
            controller.loadInstancesForDebugging();
        } else if (e.getActionCommand().equals(GUIConstants.START_DEBUGGING)) {
            controller.debugSelectedRules();
        }
    }
}
