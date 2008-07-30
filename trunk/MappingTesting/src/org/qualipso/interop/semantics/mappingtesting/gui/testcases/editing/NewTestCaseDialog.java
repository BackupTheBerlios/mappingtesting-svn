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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;

/**
 * This dialog is used for creation of new test cases. In NewTestCaseDialog test
 * case name, description and paths to the files, containing test input
 * instances and expected test output instances can be defined.
 */
public class NewTestCaseDialog extends javax.swing.JDialog implements ActionListener {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private int returnStatus = RET_CANCEL;

    private String testCaseName = null;
    private String testCaseDescription = null;
    private String inputInstFilePath = null;
    private String expOutputInstFilePath = null;
    private String testProjAbsPath;

    private javax.swing.JPanel dialogPanel;
    private javax.swing.JButton browseInputInstFileButton;
    private javax.swing.JButton browseOutputInstFileButton;
    private javax.swing.JTextField inputInstancesFileJTextField;
    private javax.swing.JTextField outputInstancesFileJTextField;
    private javax.swing.JTextField testCaseNameJTextField;
    private JTextArea testCaseDescriptionTextArea;

    /**
     * Creates new NewTestCaseDialog.
     * 
     * @param parent
     * @param testProjPath
     *            is used as default path by the file chooser, which is used for
     *            selection of files, containing test input instances and
     *            expected test output instances.
     */
    public NewTestCaseDialog(java.awt.Frame parent, String testProjPath) {
        super(parent, true);
        this.testProjAbsPath = testProjPath;
        initComponents();
        setTitle("New Test Case");
        setResizable(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        dialogPanel = new JPanel(new BorderLayout());
        getContentPane().add(dialogPanel);
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel nameAndDescriptionPanel = new javax.swing.JPanel(new BorderLayout());
        JPanel testCaseInstancesPanel = new javax.swing.JPanel(new BorderLayout());
        JPanel okCancelJPanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // init testCaseNamePanel
        JPanel testCaseNamePanel = new javax.swing.JPanel();
        JLabel testCaseNameJLabel = new javax.swing.JLabel();
        testCaseNameJTextField = new javax.swing.JTextField(40);
        testCaseNameJLabel.setText("      Test Case Name:");
        testCaseNamePanel.add(testCaseNameJLabel);
        testCaseNamePanel.add(testCaseNameJTextField);
        nameAndDescriptionPanel.add(testCaseNamePanel, java.awt.BorderLayout.NORTH);

        JPanel testCaseDescriptionPanel = new javax.swing.JPanel();
        JLabel testCaseDescriptionJLabel = new javax.swing.JLabel("               Description:");
        testCaseDescriptionTextArea = new JTextArea(3, 40);
        JScrollPane scrollPane = new JScrollPane(testCaseDescriptionTextArea);
        testCaseDescriptionPanel.add(testCaseDescriptionJLabel);
        testCaseDescriptionPanel.add(scrollPane);
        nameAndDescriptionPanel.add(testCaseDescriptionPanel, java.awt.BorderLayout.CENTER);

        // init testCaseInstancesPanel

        // init inputInstancesFilePanel
        JPanel inputInstancesFilePanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel inputInstancesFileJLabel = new javax.swing.JLabel();
        inputInstancesFileJTextField = new javax.swing.JTextField(40);
        browseInputInstFileButton = new javax.swing.JButton();
        inputInstancesFileJLabel.setText("        Test Input Instances File:");
        inputInstancesFilePanel.add(inputInstancesFileJLabel);
        inputInstancesFilePanel.add(inputInstancesFileJTextField);

        browseInputInstFileButton.setText(GUIConstants.BROWSE);
        browseInputInstFileButton.addActionListener(this);
        inputInstancesFilePanel.add(browseInputInstFileButton);

        testCaseInstancesPanel.add(inputInstancesFilePanel, java.awt.BorderLayout.NORTH);

        // init outputInstancesFilePanel
        JPanel outputInstancesFilePanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel outputInstancesFileJLabel = new javax.swing.JLabel();
        outputInstancesFileJTextField = new javax.swing.JTextField(40);
        browseOutputInstFileButton = new javax.swing.JButton();
        outputInstancesFileJLabel.setText("Expected Test Output Instances File:");
        outputInstancesFilePanel.add(outputInstancesFileJLabel);
        outputInstancesFilePanel.add(outputInstancesFileJTextField);

        browseOutputInstFileButton.setText(GUIConstants.BROWSE);
        browseOutputInstFileButton.addActionListener(this);
        outputInstancesFilePanel.add(browseOutputInstFileButton);

        testCaseInstancesPanel.add(outputInstancesFilePanel, java.awt.BorderLayout.CENTER);

        // init notePanel
        JPanel notePanel = new javax.swing.JPanel(new BorderLayout());
        JLabel noteJLabel = new javax.swing.JLabel();
        noteJLabel.setText("NOTE: The files, containing input and expected output instances"
                + " will be copied to the following directory:");
        notePanel.add(noteJLabel, BorderLayout.NORTH);

        JLabel pathJLabel = new javax.swing.JLabel();
        pathJLabel.setText(testProjAbsPath + "[testCaseName]");
        notePanel.add(pathJLabel, BorderLayout.CENTER);

        testCaseInstancesPanel.add(notePanel, java.awt.BorderLayout.SOUTH);

        // init okCancelJPanel
        JButton okJButton = new javax.swing.JButton();
        okJButton.setText(GUIConstants.OK);
        okJButton.addActionListener(this);
        okCancelJPanel.add(okJButton);

        JButton cancelJButton = new javax.swing.JButton();
        cancelJButton.setText(GUIConstants.CANCEL);
        cancelJButton.addActionListener(this);
        okCancelJPanel.add(cancelJButton);

        okCancelJPanel.setMinimumSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setMaximumSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setPreferredSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setSize(new java.awt.Dimension(800, 60));

        dialogPanel.add(nameAndDescriptionPanel, java.awt.BorderLayout.NORTH);
        dialogPanel.add(testCaseInstancesPanel, java.awt.BorderLayout.CENTER);
        dialogPanel.add(okCancelJPanel, java.awt.BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /** @return the return status of this dialog: either RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public void actionPerformed(ActionEvent e) {
        Controller controller = Controller.getInstance();

        // Test input instances can be selected with the help of file chooser
        if (e.getSource() == browseInputInstFileButton) {
            String inputInstPath = controller.choosePath(GUIConstants.SELECT_INPUT_INSTANCES_FILE,
                    testProjAbsPath, JFileChooser.OPEN_DIALOG, this, false);
            if (inputInstPath != null)
                inputInstancesFileJTextField.setText(inputInstPath);
        }
        // Expected test output instances can be selected with the help of file
        // chooser
        else if (e.getSource() == browseOutputInstFileButton) {
            String outputInstPath = controller.choosePath(
                    GUIConstants.SELECT_EXPECTED_OUTPUT_INSTANCES_FILE, testProjAbsPath,
                    JFileChooser.OPEN_DIALOG, this, false);
            if (outputInstPath != null)
                outputInstancesFileJTextField.setText(outputInstPath);
        }
        // a user has pressed OK
        else if (e.getActionCommand().equals(GUIConstants.OK)) {
            testCaseName = testCaseNameJTextField.getText();
            testCaseDescription = testCaseDescriptionTextArea.getText();
            inputInstFilePath = inputInstancesFileJTextField.getText();
            expOutputInstFilePath = outputInstancesFileJTextField.getText();
            doClose(RET_OK);
        }
        // a user has pressed CANCEL
        else if (e.getActionCommand().equals(GUIConstants.CANCEL)) {
            doClose(RET_CANCEL);
        }
    }

    public String getExpOutputInstFilePath() {
        return expOutputInstFilePath;
    }

    public String getInputInstFilePath() {
        return inputInstFilePath;
    }

    public String getTestCaseDescription() {
        return testCaseDescription;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

}
