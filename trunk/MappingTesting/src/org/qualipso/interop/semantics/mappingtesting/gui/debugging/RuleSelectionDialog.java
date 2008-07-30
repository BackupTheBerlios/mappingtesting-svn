/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package org.qualipso.interop.semantics.mappingtesting.gui.debugging;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.TextAreaRenderer;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;

/**
 * A dialog, in which a user can select mapping rules, to be debugged.
 * 
 */
public class RuleSelectionDialog extends JDialog implements ActionListener {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private int returnStatus = RET_CANCEL;

    private OntologyMapping ontologyMapping;
    private Vector<MappingRule> mappingRules;
    private JPanel dialogPanel;
    private JTable table;
    private Vector<MappingRule> selectedRules = null;
    private RuleSelectionTableModel tableModel;

    /**
     * Creates a RuleSelectionDialog, which displayes a list of all the rules,
     * contained in ontology mapping om. Mapping rules, which are contained in
     * Vector alreadySelectedRules, will be initially marked as selected.
     * 
     * @param parent
     * @param om
     * @param alreadySelectedRules
     */
    public RuleSelectionDialog(Frame parent, OntologyMapping om,
            Vector<MappingRule> alreadySelectedRules) {
        super(parent, true);
        setTitle("Select Rules to be Debugged");
        this.ontologyMapping = om;
        this.mappingRules = om.getMappingRules();
        this.dialogPanel = new JPanel();
        this.selectedRules = alreadySelectedRules;
        dialogPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(dialogPanel);

        // create rules table
        tableModel = new RuleSelectionTableModel(ontologyMapping, selectedRules);
        this.table = new JTable(tableModel);

        // is calculated as the width of RulesPanel = screenWidth/2
        int tableWidth = GUIConstants.ONTS_AND_RULES_SPLITPANE_RIGHT_DIVIDER_LOCATION((int) Toolkit
                .getDefaultToolkit().getScreenSize().getWidth());
        int column0Width = setColumnWidth(0);
        int column1Width = setColumnWidth(1);
        int column2Width = tableWidth - column0Width - column1Width;
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer(column2Width));

        // table.setPreferredScrollableViewportSize(new
        // Dimension(table.getWidth(), table.getHeight()));
        
//        Java_1.6
        table.setFillsViewportHeight(true);
        
        JScrollPane tableScrollPane = new JScrollPane(table);

        // init okCancelJPanel
        JPanel okCancelJPanel = new javax.swing.JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        okCancelJPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        JButton okJButton = new javax.swing.JButton();
        okJButton.setText(GUIConstants.OK);
        okJButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        okJButton.addActionListener(this);
        okCancelJPanel.add(okJButton);

        JButton cancelJButton = new javax.swing.JButton();
        cancelJButton.setText(GUIConstants.CANCEL);
        cancelJButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        cancelJButton.addActionListener(this);
        okCancelJPanel.add(cancelJButton);

        okCancelJPanel.setMinimumSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setMaximumSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setPreferredSize(new java.awt.Dimension(800, 60));
        okCancelJPanel.setSize(new java.awt.Dimension(800, 60));

        dialogPanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);
        dialogPanel.add(okCancelJPanel, java.awt.BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pack();
    }

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

    /**
     * Sets the optimal width of the column, identified by the columnIndex,
     * dependent on the width of the column headerand length of text in a column.
     * This method also regards, that cell value can be not of type String (e.g. boolean)
     * 
     * @param columnIndex
     * @return
     */
    protected int setColumnWidth(int columnIndex) {
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        TableColumn column = table.getColumnModel().getColumn(columnIndex);

        Component comp = headerRenderer.getTableCellRendererComponent(null,
                column.getHeaderValue(), false, false, 0, columnIndex);
        int headerWidth = comp.getPreferredSize().width + 10;
        
        int maxColumnElementLength = 0;
        int longestRowValueIndex = -1;
        for (int i = 0; i<table.getModel().getRowCount(); i++){
            Object rowValue = table.getModel().getValueAt(i, columnIndex);
            if (!(rowValue instanceof String)){
                break;
            }
            String rowValueString = (String) rowValue;
            if (rowValueString.length() > maxColumnElementLength){
                maxColumnElementLength = rowValueString.length();
                longestRowValueIndex = i;
            }
        }
        
        int cellWidth = 0;
        if (longestRowValueIndex != -1){
            comp = table.getDefaultRenderer(table.getModel().getColumnClass(columnIndex)).
            getTableCellRendererComponent(
                table, table.getModel().getValueAt(longestRowValueIndex, columnIndex),
                false, false, 0, columnIndex);
            cellWidth = comp.getPreferredSize().width + 10;
        }
        
        int maxWidth = Math.max(headerWidth, cellWidth);

        table.getColumnModel().getColumn(columnIndex).setWidth(maxWidth);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(maxWidth);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(maxWidth);

//        Doesn't help
//      table.getColumnModel().getColumn(columnIndex).setMinWidth(headerWidth);
//        table.getColumnModel().getColumn(columnIndex).sizeWidthToFit();        
//        table.getColumnModel().getColumn(columnIndex).setResizable(true);
        return maxWidth;
    }

    public void actionPerformed(ActionEvent e) {
        Controller controller = Controller.getInstance();

        // a user has pressed OK
        if (e.getActionCommand().equals(GUIConstants.OK)) {
            Vector<Boolean> selectedRulesIndices = tableModel.getSelectedRulesIndices();
            selectedRules = new Vector<MappingRule>();
            for (int i = 0; i < selectedRulesIndices.size(); i++) {
                Boolean ruleSelected = selectedRulesIndices.get(i);
                if (ruleSelected)
                    selectedRules.add(mappingRules.get(i));
            }
            doClose(RET_OK);
        }

        //		a user has pressed CANCEL
        else if (e.getActionCommand().equals(GUIConstants.CANCEL)) {
            doClose(RET_CANCEL);
        }
    }

    public Vector<MappingRule> getSelectedRules() {
        return selectedRules;
    }

}
