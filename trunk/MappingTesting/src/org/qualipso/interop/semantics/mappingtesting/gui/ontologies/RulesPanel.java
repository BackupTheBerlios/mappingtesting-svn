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

package org.qualipso.interop.semantics.mappingtesting.gui.ontologies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.EmptyPanelWithText;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;

import edu.stanford.smi.protegex.owl.model.NamespaceManager;

/**
 * RulesPanel represents mapping rules between source and target ontologies in a
 * human-readable syntax.
 */
public class RulesPanel extends JPanel {

    // table representing rules
    protected JTable table;
    protected OntologyMapping ontologyMapping;
    protected Vector mappingRules = null;
    protected String srcOntNS;
    protected String targetOntNS;

    /**
     * This empty constructor will be invoked from the subclass.
     */
    public RulesPanel() {
    }

    /**
     * Creates a RulesPanel, which represents rules in the ontology mapping om.
     * The panel also shows namespaces of source and target ontologies: srcOntNS
     * and targetOntNS.
     * 
     * @param om
     * @param srcOntNS
     * @param targetOntNS
     */
    public RulesPanel(OntologyMapping om, String srcOntNS, String targetOntNS) {
        this.ontologyMapping = om;
        this.mappingRules = om.getMappingRules();
        this.srcOntNS = srcOntNS;
        this.targetOntNS = targetOntNS;
        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);

        // no rules
        if (mappingRules == null || mappingRules.size() == 0) {
            createEmptyPanelWithText(GUIConstants.NO_RULES_CONTAINED_IN_MAPPING);
            return;
        }

        // rules > 0 -> display rules table
        this.setLayout(new BorderLayout());

        JPanel namespacePanel = this.createNamespacePanel();
        this.add(namespacePanel, BorderLayout.NORTH);

        // create rules table
        RulesTableModel tableModel = new RulesTableModel(mappingRules);
        this.table = new JTable(tableModel);

        // is calculated as the width of RulesPanel = screenWidth/2
        int tableWidth = GUIConstants.RULES_TABLE_WIDTH((int) Toolkit.getDefaultToolkit()
                .getScreenSize().getWidth());
        int column0Width = setColumnWidth(0);
        int column1Width = tableWidth - column0Width;
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer(column1Width));

        // Java_1.6
//        table.setFillsViewportHeight(true);
        // JScrollPane tableScrollPane = new JScrollPane(table);
        // tableScrollPane.setBackground(Color.WHITE);
        // this.setBackground(Color.WHITE);
        // this.add(table, BorderLayout.CENTER);
        // table.setPreferredScrollableViewportSize(new
        // Dimension(table.getWidth(), table.getHeight()));
        // table.repaint();
        // table.revalidate();

        table.getTableHeader().setBorder(new LineBorder(Color.GRAY));        
        JPanel tablePanel = new JPanel(new BorderLayout()); 
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        this.add(tablePanel, BorderLayout.CENTER);

        // initColumnSizes();
        // table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    protected void createEmptyPanelWithText(String text) {
        setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        EmptyPanelWithText emptyPanel = new EmptyPanelWithText(text);
        emptyPanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        add(emptyPanel, BorderLayout.NORTH);
    }

    /**
     * Sets the optimal width of the column, identified by the columnIndex,
     * dependent on the width of the column header and length of text in a column.
     * 
     * @param columnIndex
     * @return optimal width of the column
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
            String rowValue = (String) table.getModel().getValueAt(i, columnIndex);
            if (rowValue.length() > maxColumnElementLength){
                maxColumnElementLength = rowValue.length();
                longestRowValueIndex = i;
            }
        }
        
        comp = table.getDefaultRenderer(table.getModel().getColumnClass(columnIndex)).
        getTableCellRendererComponent(
            table, table.getModel().getValueAt(longestRowValueIndex, columnIndex),
            false, false, 0, columnIndex);
        int cellWidth = comp.getPreferredSize().width + 10;
        
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


    /**
     * Creates a panel, which represents namespaces and namespaces prefixes of
     * source and target ontologies in the following way, e.g.:
     * xmlns:srcOnt="http://www.fokus.fraunhofer.de/test/sourceOntology.owl#"
     * 
     * @return
     */
    private JPanel createNamespacePanel() {
        JPanel nsPanel = new JPanel(new BorderLayout());
        nsPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        // nsPanel.setBorder(new LineBorder(Color.BLACK));
        NamespaceManager nsManager = ontologyMapping.getJenaOWLModel().getNamespaceManager();
        JLabel baseNSLabel = new JLabel(GUIConstants.NAMESPACE_PREFIXES);
        nsPanel.add(baseNSLabel, BorderLayout.NORTH);

        String srcOntPrefix = nsManager.getPrefix(srcOntNS);
        String srcOntPrefixAndNS = "xmlns:" + srcOntPrefix + "=\"" + srcOntNS + "\"";
        nsPanel.add(new JLabel(srcOntPrefixAndNS), BorderLayout.CENTER);

        String targetOntPrefix = nsManager.getPrefix(targetOntNS);
        String targetOntPrefixAndNS = "xmlns:" + targetOntPrefix + "=\"" + targetOntNS + "\"";
        nsPanel.add(new JLabel(targetOntPrefixAndNS), BorderLayout.SOUTH);

        return nsPanel;
    }

    /**
     * Creates a title panel, which shows titleText. This method is called in
     * subclasses of RulesPanel.
     * 
     * @return
     */
    protected JPanel createTitlePanel(String titleText) {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        // titlePanel.setBorder(new LineBorder(Color.BLACK));
        JLabel titleLabel = new JLabel(titleText);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }
}
