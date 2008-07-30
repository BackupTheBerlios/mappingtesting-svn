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

import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;

/**
 * This JList is used for representation of a list of test cases in the test
 * project.
 */
public class TestCasesJList extends JList implements ListSelectionListener {

    private DefaultListModel listModel;
    protected Vector<TestCase> testCases;

    /**
     * Creates a TestCasesJList, which will graphically represent testCases in a
     * test project.
     * 
     * @param testCases
     */
    public TestCasesJList(Vector<TestCase> testCases) {
        super();
        this.testCases = testCases;
        listModel = new DefaultListModel();
        for (Iterator iter = testCases.iterator(); iter.hasNext();) {
            TestCase testCase = (TestCase) iter.next();
            listModel.addElement(testCase);
        }
        setModel(listModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(this);
        if (testCases.size() > 0){
            setSelectedIndex(0);  
        }        
    }

    /*
     * This method is required by interface ListSelectionListener.
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            TestCase selectedTestCase = (TestCase) getSelectedValue();

            // this line is needed for the case, when valueChanged is called
            // from the method
            // fireSelectionValueChanged, which in turn is called by
            // listModel.removeElement(testCase)
            if (selectedTestCase == null)
                return;
            Controller controller = Controller.getInstance();
            controller.displayTestCaseInformation(selectedTestCase);
        }
    }

    /**
     * Adds new testCase to listModel and selects the new testCase in a list.
     * 
     * @param testCase
     */
    public void addTestCase(TestCase testCase) {
        int newTestCaseIndex = listModel.getSize();
        listModel.insertElementAt(testCase, newTestCaseIndex);
        // Select the new item and make it visible.
        setSelectedIndex(newTestCaseIndex);
        ensureIndexIsVisible(newTestCaseIndex);
    }

    /**
     * Deletes testCase from listModel and selects the first testCase in a list.
     * 
     * @param testCase
     */
    public void deleteTestCase(TestCase testCase) {
        if (listModel.contains(testCase)) {
            listModel.removeElement(testCase);
        }

        if (listModel.size() > 0) {
            setSelectedIndex(0);
            ensureIndexIsVisible(0);
        }
    }

}
