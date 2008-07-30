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

import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;

/**
 * A split pane, which displays source and target ontologies, and the mapping
 * rules between them.
 * 
 */
public class OntologiesAndRulesSplitPane extends JSplitPane {

    public static final int SRC_PANEL = 0;
    public static final int TARGET_PANEL = 1;

    JScrollPane srcOntScrollPane;
    JScrollPane targetOntScrollPane;
    JScrollPane rulesScrollPane;

    JPanel srcOntPanel;
    JPanel targetOntPanel;
    JPanel rulesPanel;

    JSplitPane rulesAndTargetOnt_SplitPane;

    public OntologiesAndRulesSplitPane() {

        Controller.getInstance().setOntologiesAndRulesPane(this);

        this.setBorder(null);
        this.setDividerSize(GUIConstants.DIVIDERSIZE);
        this.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_LEFT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));
        this.setContinuousLayout(true);

        rulesAndTargetOnt_SplitPane = new JSplitPane();
        rulesAndTargetOnt_SplitPane.setBorder(null);
        rulesAndTargetOnt_SplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        rulesAndTargetOnt_SplitPane.setDividerLocation(GUIConstants
                .ONTS_AND_RULES_SPLITPANE_RIGHT_DIVIDER_LOCATION((int) Toolkit.getDefaultToolkit()
                        .getScreenSize().getWidth()));

        rulesAndTargetOnt_SplitPane.setContinuousLayout(true);


        this.srcOntScrollPane = new JScrollPane();
        this.srcOntScrollPane.setDoubleBuffered(true);
        this.srcOntScrollPane.setAutoscrolls(true);
        this.srcOntScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.SOURCE_ONT_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        srcOntScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        this.targetOntScrollPane = new JScrollPane();
        this.targetOntScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.TARGET_ONT_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        this.targetOntScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        this.rulesScrollPane = new JScrollPane();
        this.rulesScrollPane
                .setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                        GUIConstants.MAPPING_RULES_BORDER_TITLE, TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, GUIConstants.BORDER_FONT,
                        GUIConstants.BORDER_TITLE_COLOR));
        this.rulesScrollPane.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        this.srcOntPanel = new JPanel();
        this.srcOntPanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.srcOntScrollPane.getViewport().add(this.srcOntPanel);

        this.targetOntPanel = new JPanel();
        this.targetOntPanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.targetOntScrollPane.getViewport().add(this.targetOntPanel);

        this.rulesPanel = new JPanel();
        this.rulesPanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.rulesScrollPane.getViewport().add(this.rulesPanel);

        rulesAndTargetOnt_SplitPane.setLeftComponent(this.rulesScrollPane);
        rulesAndTargetOnt_SplitPane.setRightComponent(this.targetOntScrollPane);

        this.setLeftComponent(this.srcOntScrollPane);
        this.setRightComponent(this.rulesAndTargetOnt_SplitPane);
    }

    /**
     * Sets a new ontology panel, depending on the panel type: SRC_PANEL or
     * TARGET_PANEL.
     * 
     * @param panel
     * @param panelType
     */
    public void setOntologyPanel(OntologyPanel panel, int panelType) {
        JScrollPane paneToChange = null;
        if (panelType == SRC_PANEL)
            paneToChange = srcOntScrollPane;
        else if (panelType == TARGET_PANEL)
            paneToChange = targetOntScrollPane;
        else
            return;
        paneToChange.getViewport().removeAll();
        paneToChange.getViewport().add(panel);
    }

    public void setRulesPanel(JPanel panel) {
        rulesScrollPane.getViewport().removeAll();
        rulesScrollPane.getViewport().add(panel);
    }

    public JPanel getRulesPanel() {
        return (JPanel) rulesScrollPane.getViewport().getComponent(0);
    }
   
    
    /**
     * Sets empty panel on all three scroll panes.
     */
    public void clearAll(){
        srcOntScrollPane.getViewport().removeAll();
        srcOntScrollPane.getViewport().add(srcOntPanel);
        
        targetOntScrollPane.getViewport().removeAll();
        targetOntScrollPane.getViewport().add(targetOntPanel);
        
        rulesScrollPane.getViewport().removeAll();        
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        rulesScrollPane.getViewport().add(emptyPanel);
    }

}
