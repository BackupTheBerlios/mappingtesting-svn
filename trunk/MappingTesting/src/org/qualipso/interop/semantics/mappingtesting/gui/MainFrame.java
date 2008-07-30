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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.OntologiesAndRulesSplitPane;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBlue;

/**
 * The main frame of Mapping Testing Tool, which contains all other GUI elements.
 * 
 * The class has been adapted from:
 * Nils Barnickel: Interoperability in eGovernment through Cross-Ontology Semantic Web Service Composition.
 * Diploma thesis, FU Berlin, August 2006.
 * (http://sourceforge.net/projects/swscomposer.)
 */
public class MainFrame extends JFrame implements WindowListener {

    // displays ontologies and rules
    private OntologiesAndRulesSplitPane ontsAndRulesSplitPane;

    // displays tabbed pane
    private TestingDebuggingTabbedPane tabbedPane;

    // displays messages
    private MessagePanel messagePanel;

    // structure for panels
    private JSplitPane bottom_RestSplitPane;
    private JSplitPane ontsAndRules_tabbedPaneSplitPane;

    /**
     * Creates initial GUI components and configures layout.
     * 
     */
    public MainFrame() {

        // init
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener( this );

//        // set look and feel
        try {
            Plastic3DLookAndFeel.setCurrentTheme(new SkyBlue());
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (Exception e) {
        }
        this.getContentPane().setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.height -= 35;
        this.setSize(dim);

        // title
        this.setTitle("Mapping Testing Tool");

        // menu
        Menu menu = new Menu();
        Controller.getInstance().setMenu(menu);
        this.setJMenuBar(menu);

        // ontologies and rules pane
        this.ontsAndRulesSplitPane = new OntologiesAndRulesSplitPane();

        // tabbed pane for testing and debugging
        this.tabbedPane = new TestingDebuggingTabbedPane();

        // message panel
        this.messagePanel = MessagePanel.getInstance();

        // structure in split panes
        ontsAndRules_tabbedPaneSplitPane = new JSplitPane();
        ontsAndRules_tabbedPaneSplitPane.setBorder(null);
        ontsAndRules_tabbedPaneSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        ontsAndRules_tabbedPaneSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        ontsAndRules_tabbedPaneSplitPane.setLeftComponent(tabbedPane);
        ontsAndRules_tabbedPaneSplitPane.setRightComponent(ontsAndRulesSplitPane);
        ontsAndRules_tabbedPaneSplitPane.setDividerLocation(GUIConstants
                .ONTSANDRULES_TABBEDPANEL_DIVIDER_LOCATION((int) dim.getHeight()));

        bottom_RestSplitPane = new JSplitPane();
        bottom_RestSplitPane.setBorder(null);
        bottom_RestSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        bottom_RestSplitPane.setDividerSize(GUIConstants.DIVIDERSIZE);
        bottom_RestSplitPane.setLeftComponent(ontsAndRules_tabbedPaneSplitPane);
        bottom_RestSplitPane.setRightComponent(messagePanel);
        bottom_RestSplitPane.setDividerLocation(GUIConstants
                .REST_MESSAGEPANEL_DIVIDER_LOCATION((int) dim.getHeight()));

        this.getContentPane().add(bottom_RestSplitPane);
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void windowClosing(WindowEvent e) {
        if (Controller.getInstance().getTestProject() == null){
            System.exit(0);
        }   
        else{
            boolean projectClosed = Controller.getInstance().closeTestProject(); 
            if (projectClosed) {
                System.exit(0);
            }
        }        
    }

    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

}
