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

package org.qualipso.interop.semantics.mappingtesting.gui.ontologies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.RulesExecutionResult;

/**
 * ExecutedRulesPanel represents mapping rules between source and target
 * ontologies in a human-readable syntax. The panel also shows information, if
 * the rule was fired during execution of a particular test case.
 */
public class ExecutedRulesPanel extends RulesPanel {

    private RulesExecutionResult ruleExecutionResult;

    // Will be not null if this panel represents test case execution result,
    // and will be null, when this panel represents debugging result.
    private String testCaseName = null;

    /**
     * Creates an ExecutedRulesPanel, which represents rules and ruleExecResult.
     * If tcName != null, the panel also shows the name of the test case.
     * 
     * @param rules
     * @param ruleExecResult
     * @param tcName
     */
    public ExecutedRulesPanel(Vector<MappingRule> rules, RulesExecutionResult ruleExecResult,
            String tcName) {
        this.mappingRules = rules;
        this.ruleExecutionResult = ruleExecResult;

        // this panel represents test case execution result, and not debugging
        // result
        if (tcName != null)
            this.testCaseName = tcName;

        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);

        // no rules
        if (mappingRules == null || mappingRules.size() == 0) {
            createEmptyPanelWithText("");
            return;
        }

        // rules > 0 -> display rules table
        this.setLayout(new BorderLayout());

        JPanel titlePanel = null;
        if (this.testCaseName != null)
            titlePanel = createTitlePanel(GUIConstants.EXECUTED_RULES_PANEL_TITLE + this.testCaseName);
        else
            titlePanel = createTitlePanel(GUIConstants.DEBUGGED_RULES_PANEL_TITLE);
        this.add(titlePanel, BorderLayout.NORTH);

        // create rules table
        ExecutedRulesTableModel tableModel = new ExecutedRulesTableModel(mappingRules,
                ruleExecResult);
        this.table = new JTable(tableModel);

        int tableWidth = GUIConstants.RULES_TABLE_WIDTH((int) Toolkit.getDefaultToolkit()
                .getScreenSize().getWidth());
        int column0Width = setColumnWidth(0);
        int column1Width = setColumnWidth(1);
        int column2Width = tableWidth - column0Width - column1Width;
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer(column2Width));

        // table.setPreferredScrollableViewportSize(new
        // Dimension(table.getWidth(), table.getHeight()));
//      Java_1.6
        table.setFillsViewportHeight(true);

//        JScrollPane tableScrollPane = new JScrollPane(table);
        
        table.getTableHeader().setBorder(new LineBorder(Color.GRAY));        
        JPanel tablePanel = new JPanel(new BorderLayout()); 
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        this.add(tablePanel, BorderLayout.CENTER);
    }
}
