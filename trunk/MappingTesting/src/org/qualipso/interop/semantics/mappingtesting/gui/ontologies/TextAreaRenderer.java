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

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * This TableCellRenderer allows for using JTextArea to render a cell in a
 * table. This allows for multi-line cells in a table.
 * 
 * This class has been adopted from:
 * http://www.javaspecialists.co.za/archive/Issue045.html
 */
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

    private int cellWidth;

    /**
     * Creates a TextAreaRenderer.
     * 
     * @param width
     *            of the cell, in which a multi-line text is placed
     */
    public TextAreaRenderer(int width) {
        this.cellWidth = width;
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    /*
     * This method sets height of the cell, depending on columnWidth and the
     * length of the text placed in the cell. (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected,
            boolean hasFocus, int row, int column) {

        int heightWanted = 0;

        if (obj != null) {
            String text = new String((String) obj);
            int stringWidth = (int) (getFontMetrics(getFont()).stringWidth(text) * 1.2);
            int stringHeight = getFontMetrics(getFont()).getHeight();

            int numberOfRows = (stringWidth / cellWidth) + 1;
            heightWanted = numberOfRows * (stringHeight + 3);
        }

        if (heightWanted != 0 && heightWanted != table.getRowHeight(row)) {
            table.setRowHeight(row, heightWanted);
        }

        setText((String) obj);
        return this;
    }

}
