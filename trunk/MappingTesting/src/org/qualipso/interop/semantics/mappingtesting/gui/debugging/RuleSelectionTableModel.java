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

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.qualipso.interop.semantics.mappingtesting.model.MappingRule;
import org.qualipso.interop.semantics.mappingtesting.model.OntologyMapping;

/**
 * A table model, used for the table displaying rules in the
 * RuleSelectionDialog.
 * 
 */
public class RuleSelectionTableModel extends AbstractTableModel {

    private String[] columnNames = { "Debug", "Rule Name", "Expression" };
    /**
     * This vector contains information, which mapping rules have been selected.
     */
    private Vector<Boolean> selectedRulesIndices;
    private Vector<MappingRule> mappingRules;

    public RuleSelectionTableModel(OntologyMapping om, Vector<MappingRule> alreadySelectedRules) {
        this.mappingRules = om.getMappingRules();
        this.selectedRulesIndices = new Vector<Boolean>();
        for (int i = 0; i < mappingRules.size(); i++) {
            if (alreadySelectedRules == null
                    || (!alreadySelectedRules.contains(mappingRules.get(i))))
                selectedRulesIndices.add(new Boolean(false));
            else
                selectedRulesIndices.add(new Boolean(true));
        }
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return mappingRules.size();
    }

    public Object getValueAt(int row, int col) {
        MappingRule rule = mappingRules.get(row);
        if (col == 0)
            return selectedRulesIndices.get(row);
        else if (col == 1)
            return rule.getLocalName();
        else if (col == 2)
            return rule.getRuleText();
        return null;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }


    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * This method is implemented, since one column should be editable.
     */
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            selectedRulesIndices.setElementAt((Boolean) value, row);
            fireTableCellUpdated(row, col);
        }
    }

    public Vector<Boolean> getSelectedRulesIndices() {
        return selectedRulesIndices;
    }
}
