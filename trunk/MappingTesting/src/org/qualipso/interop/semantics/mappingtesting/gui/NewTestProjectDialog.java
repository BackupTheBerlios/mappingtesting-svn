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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;

/**
 * This dialog is used for creation of new test projects. In
 * NewTestProjectDialog test project name and paths to the files, source and
 * target ontologies and ontology mapping should be defined.
 */
public class NewTestProjectDialog extends javax.swing.JDialog implements ActionListener {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private int returnStatus = RET_CANCEL;

    private String testProjectName = null;
    private String testProjectDirectory = null;
    private String sourceOntologyPath = null;
    private String targetOntologyPath = null;
    private String ontologyMappingPath = null;
    // private String testProjAbsPath;

    private javax.swing.JPanel dialogPanel;
    
    private javax.swing.JTextField testProjNameJTextField;
    private javax.swing.JTextField testProjDirectoryJTextField;
    private javax.swing.JButton browseProjDirectoryButton;
    
    private javax.swing.JTextField sourceOntologyFileJTextField;
    private javax.swing.JTextField targetOntologyFileJTextField;
    private javax.swing.JTextField ontologyMappingFileJTextField;
    
    private javax.swing.JButton browseSourceOntologyFileButton;
    private javax.swing.JButton browseTargetOntologyFileButton;
    private javax.swing.JButton browseOntologyMappingFileButton;
    

    /**
     * Creates new NewTestProjectDialog.
     * 
     * @param parent
     */
    public NewTestProjectDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setTitle("New Test Project");
        setResizable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        dialogPanel = new JPanel(new BorderLayout());
        getContentPane().add(dialogPanel);
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel nameAndDirectoryPanel = new javax.swing.JPanel(new BorderLayout());
        JPanel ontologiesAndRulesPanel = new javax.swing.JPanel(new BorderLayout());
        JPanel okCancelJPanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // init testProjectNamePanel
        JPanel testProjectNamePanel = new javax.swing.JPanel();
        JLabel testProjectNameJLabel = new javax.swing.JLabel();
        testProjNameJTextField = new javax.swing.JTextField(40);
        testProjectNameJLabel.setText("      Test Project Name:");
        testProjectNamePanel.add(testProjectNameJLabel);
        testProjectNamePanel.add(testProjNameJTextField);
        nameAndDirectoryPanel.add(testProjectNamePanel, java.awt.BorderLayout.NORTH);

//      init testProjDirectoryPanel
        JPanel testProjDirectoryPanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel testProjDirectoryJLabel = new javax.swing.JLabel("Test Project Directory:");
        testProjDirectoryPanel.add(testProjDirectoryJLabel);
        
        testProjDirectoryJTextField = new javax.swing.JTextField(40);
        testProjDirectoryPanel.add(testProjDirectoryJTextField);

        browseProjDirectoryButton = new javax.swing.JButton();
        browseProjDirectoryButton.setText(GUIConstants.BROWSE);
        browseProjDirectoryButton.addActionListener(this);
        testProjDirectoryPanel.add(browseProjDirectoryButton);
        nameAndDirectoryPanel.add(testProjDirectoryPanel, java.awt.BorderLayout.CENTER);
        
        // init ontologiesAndRulesPanel

        // init sourceOntologyFilePanel
        JPanel sourceOntologyFilePanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel sourceOntologyFileJLabel = new javax.swing.JLabel();
        sourceOntologyFileJTextField = new javax.swing.JTextField(40);
        browseSourceOntologyFileButton = new javax.swing.JButton();
        sourceOntologyFileJLabel.setText("Source Ontology Path:");
        sourceOntologyFilePanel.add(sourceOntologyFileJLabel);
        sourceOntologyFilePanel.add(sourceOntologyFileJTextField);

        browseSourceOntologyFileButton.setText(GUIConstants.BROWSE);
        browseSourceOntologyFileButton.addActionListener(this);
        sourceOntologyFilePanel.add(browseSourceOntologyFileButton);

        ontologiesAndRulesPanel.add(sourceOntologyFilePanel, java.awt.BorderLayout.NORTH);

        // init outputInstancesFilePanel
        JPanel targetOntologyFilePanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel targetOntologyFileJLabel = new javax.swing.JLabel();
        targetOntologyFileJTextField = new javax.swing.JTextField(40);
        browseTargetOntologyFileButton = new javax.swing.JButton();
        targetOntologyFileJLabel.setText("Target Ontology Path:");
        targetOntologyFilePanel.add(targetOntologyFileJLabel);
        targetOntologyFilePanel.add(targetOntologyFileJTextField);

        browseTargetOntologyFileButton.setText(GUIConstants.BROWSE);
        browseTargetOntologyFileButton.addActionListener(this);
        targetOntologyFilePanel.add(browseTargetOntologyFileButton);

        ontologiesAndRulesPanel.add(targetOntologyFilePanel, java.awt.BorderLayout.CENTER);
        
        // init ontologyMappingFilePanel
        JPanel ontologyMappingFilePanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel ontologyMappingFileJLabel = new javax.swing.JLabel();
        ontologyMappingFileJTextField = new javax.swing.JTextField(40);        
        ontologyMappingFileJLabel.setText("Ontology Mapping Path:");
        ontologyMappingFilePanel.add(ontologyMappingFileJLabel);
        ontologyMappingFilePanel.add(ontologyMappingFileJTextField);

        browseOntologyMappingFileButton = new javax.swing.JButton();
        browseOntologyMappingFileButton.setText(GUIConstants.BROWSE);
        browseOntologyMappingFileButton.addActionListener(this);
        ontologyMappingFilePanel.add(browseOntologyMappingFileButton);
    

        // init notePanel
        JPanel notePanel = new javax.swing.JPanel(new BorderLayout());
        JLabel noteJLabel1 = new javax.swing.JLabel();
        noteJLabel1.setText("NOTE: If the files, containing source and target ontologies or " +
                "ontology mapping, are local," );
        notePanel.add(noteJLabel1, BorderLayout.NORTH);
        
        JLabel noteJLabel2 = new javax.swing.JLabel();
        noteJLabel2.setText("they will be copied to the test project directory.");
        notePanel.add(noteJLabel2, BorderLayout.CENTER);     
        
        JPanel mappingAndNotePanel = new JPanel(new BorderLayout());
        mappingAndNotePanel.add(ontologyMappingFilePanel, java.awt.BorderLayout.NORTH);
        mappingAndNotePanel.add(notePanel, java.awt.BorderLayout.CENTER);
        
        ontologiesAndRulesPanel.add(mappingAndNotePanel, java.awt.BorderLayout.SOUTH);

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

        dialogPanel.add(nameAndDirectoryPanel, java.awt.BorderLayout.NORTH);
        dialogPanel.add(ontologiesAndRulesPanel, java.awt.BorderLayout.CENTER);
        dialogPanel.add(okCancelJPanel, java.awt.BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Closes the dialog */
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
        
        // Test project directory can be selected with the help of a file chooser
        if (e.getSource() == browseProjDirectoryButton) {
            String path = controller.choosePath(GUIConstants.SELECT_TEST_PROJECT_DIRECTORY,
                    null, JFileChooser.OPEN_DIALOG, this, true);
            if (path != null)
                testProjDirectoryJTextField.setText(path);
        }

        // Source ontology can be selected with the help of a file chooser
        else if (e.getSource() == browseSourceOntologyFileButton) {
            String path = controller.choosePath(GUIConstants.SELECT_SOURCE_ONTOLOGY_FILE,
                    null, JFileChooser.OPEN_DIALOG, this, false);
            if (path != null)
                sourceOntologyFileJTextField.setText(path);
        }
        
        // Target ontology can be selected with the help of file chooser
        else if (e.getSource() == browseTargetOntologyFileButton) {
            String path = controller.choosePath(
                    GUIConstants.SELECT_TARGET_ONTOLOGY_FILE, 
                    null, JFileChooser.OPEN_DIALOG, this, false);
            if (path != null)
                targetOntologyFileJTextField.setText(path);
        }
        
        // Ontology mapping can be selected with the help of file chooser
        else if (e.getSource() == browseOntologyMappingFileButton) {
            String path = controller.choosePath(
                    GUIConstants.SELECT_ONTOLOGY_MAPPING_FILE, 
                    null, JFileChooser.OPEN_DIALOG, this, false);
            if (path != null)
                ontologyMappingFileJTextField.setText(path);
        }
        
        // a user has pressed OK
        else if (e.getActionCommand().equals(GUIConstants.OK)) {
            testProjectName = testProjNameJTextField.getText();
            testProjectDirectory = testProjDirectoryJTextField.getText();
            sourceOntologyPath = sourceOntologyFileJTextField.getText();
            targetOntologyPath = targetOntologyFileJTextField.getText();
            ontologyMappingPath = ontologyMappingFileJTextField.getText();
            doClose(RET_OK);
        }
        // a user has pressed CANCEL
        else if (e.getActionCommand().equals(GUIConstants.CANCEL)) {
            doClose(RET_CANCEL);
        }
    }


    public String getOntologyMappingPath() {
        return ontologyMappingPath;
    }

    public String getSourceOntologyPath() {
        return sourceOntologyPath;
    }

    public String getTargetOntologyPath() {
        return targetOntologyPath;
    }

    public String getTestProjectDirectory() {
        return testProjectDirectory;
    }

    public String getTestProjectName() {
        return testProjectName;
    }
}
