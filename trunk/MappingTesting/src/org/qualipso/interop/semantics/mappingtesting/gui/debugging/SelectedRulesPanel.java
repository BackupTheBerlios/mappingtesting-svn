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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.RulesPanel;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.RulesTableModel;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.TextAreaRenderer;
import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;

/**
 * SelectedRulesPanel, which represents selected mapping rules, which should be
 * debugged, in a human-readable syntax.
 */
public class SelectedRulesPanel extends RulesPanel {

    /**
     * Creates a SelectedRulesPanel, which represents selected mapping rules,
     * which should be debugged
     * 
     * @param mappingRules
     */
    public SelectedRulesPanel(Vector<MappingRule> mappingRules) {
        this.mappingRules = mappingRules;
        this.setLayout(new BorderLayout());

        // no rules
        if (mappingRules == null || mappingRules.size() == 0) {
            createEmptyPanelWithText(GUIConstants.NO_RULES_SELECTED);
            return;
        }

        // rules > 0 -> display rules table

        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);

        JPanel titlePanel = createTitlePanel(GUIConstants.SELECTED_RULES_PANEL_TITLE);
        this.add(titlePanel, BorderLayout.NORTH);

        // create rules table
        RulesTableModel tableModel = new RulesTableModel(mappingRules);
        this.table = new JTable(tableModel);
        int tableWidth = GUIConstants.RULES_TABLE_WIDTH((int) Toolkit.getDefaultToolkit()
                .getScreenSize().getWidth());
        int column0Width = setColumnWidth(0);
        int column1Width = tableWidth - column0Width;
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer(column1Width));

        table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), table
                        .getHeight()));
        
//      Java_1.6
        table.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(table);
        this.add(tableScrollPane, BorderLayout.CENTER);
    }
}
