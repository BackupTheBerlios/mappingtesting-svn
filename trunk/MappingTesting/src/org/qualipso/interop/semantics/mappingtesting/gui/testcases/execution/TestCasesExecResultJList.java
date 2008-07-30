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

import java.awt.Component;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.editing.TestCasesJList;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestCaseExecutionResult;
import org.qualipso.interop.semantics.mappingtesting.model.TestReport;

/**
 * This JList is used for representation of a list of test cases, after the test
 * cases have been executed. This list also has a separate icon for test cases,
 * which have succeeded and which have failed.
 */
public class TestCasesExecResultJList extends TestCasesJList {

    private TestReport testReport;

    /**
     * Creates a TestCasesExecResultJList, which will graphically represent
     * testCases and information, if the test case has succeeded, which is based
     * on the test report.
     * 
     * @param testCases
     * @param testReport
     */
    public TestCasesExecResultJList(Vector<TestCase> testCases, TestReport testReport) {
        super(testCases);
        this.testReport = testReport;
        ExecResultJListRenderer renderer = new ExecResultJListRenderer();
        setCellRenderer(renderer);
    }

    /*
     * This method is required by interface ListSelectionListener.
     * 
     * @see org.qualipso.interop.semantics.mappingtesting.gui.testcases.TestCasesJList#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            TestCase selectedTestCase = (TestCase) getSelectedValue();
            Controller controller = Controller.getInstance();
            controller.displayTestCaseExecutionResult(selectedTestCase);
        }
    }

    /**
     * This is a custom ListCellRenderer for TestCasesExecResultJList, which
     * shows a green or red circle next to the test case name to represent, is
     * the test case has succeeded or failed.
     * 
     */
    class ExecResultJListRenderer extends JLabel implements ListCellRenderer {

        public ExecResultJListRenderer() {
            setOpaque(true);
        }

        /*
         * This method finds the image corresponding to succeeded test cases and
         * returns the label, set up to display the text and image.
         * 
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *      java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            // Set the icon and text. If icon was null, say so.
            TestCase selectedTestCase = (TestCase) value;
            String testCaseName = selectedTestCase.getName();
            TestCaseExecutionResult tcExecResult = testReport.getTestCaseExecutionResult(testCaseName);
            boolean tcSucceeded = tcExecResult.isTestCaseSucceeded();

            ImageIcon icon = null;
            if (tcSucceeded){
                String consistencyCheckResult = tcExecResult.getResultingModelConsistencyCheckResult();
                if (consistencyCheckResult.indexOf("WARNING: There are following inconsistent classes in the model:") == -1)
                    icon = new ImageIcon(GUIConstants.GREEN_CIRCLE_ICON);
                else
                    icon = new ImageIcon(GUIConstants.YELLOW_CIRCLE_ICON);
            }                
            else
                icon = new ImageIcon(GUIConstants.RED_CIRCLE_ICON);
            if (icon != null)
                setIcon(icon);

            setText(testCaseName);
            setFont(list.getFont());

            return this;
        }
    }

}
