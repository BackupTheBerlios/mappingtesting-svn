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

package org.qualipso.interop.semantics.mappingtesting.gui.testcases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.border.LineBorder;

import org.qualipso.interop.semantics.mappingtesting.core.Controller;
import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.JenaOWLModelToTreeAdapter;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.OntologyTree;
import org.qualipso.interop.semantics.mappingtesting.model.TestCase;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * This panel presents instances either of source ontology or polymorp instances
 * of source and target ontology (it is assumed, that expected test output can
 * also contain polymorph instances and not only instances of target ontology).
 * 
 */
public class InstancesPanel extends JPanel implements ActionListener {

    // tree representing ontology instances and its asserted properties
    private OntologyTree assertedInstancesTree;

    // tree representing ontology instances and its asserted and inferred
    // properties
    private OntologyTree inferredInstancesTree = null;

    // Asserted JenaOWLModel, which will be visualized
    private JenaOWLModel assertedJenaOWLModel;

    private JPanel treePanel;

    private int instancesType;
    private Object jenaOWLModelSource;

    /**
     * Create InstancesPanel for given jenaOWLModel
     * 
     * @param jenaOWLModel
     * @param modelSource
     *            defines which source the instances in jenaOWLModel come from.
     *            Can either be of type TestCase, RuleExecutionResult or null
     *            (is debugging instances are shown).
     * @param instancesType
     *            instancesType defines, whether the panel contains instances of
     *            source ontology or instances of both source and target
     *            ontologies. Can have values: TestProject.SOURCE_ONTOLOGY or
     *            TestProject.SOURCE_AND_TARGET_ONTOLOGY.
     */
    public InstancesPanel(JenaOWLModel jenaOWLModel, Object modelSource, int instancesType) {
        this.assertedJenaOWLModel = jenaOWLModel;
        this.instancesType = instancesType;
        this.jenaOWLModelSource = modelSource;
        this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);

        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel namespacePanel = this.createNamespacePanel();
        topPanel.add(namespacePanel, BorderLayout.NORTH);
        JPanel assertedInferredPanel = this.createAssertedInferredPanel();
        topPanel.add(assertedInferredPanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);

        treePanel = new JPanel(new BorderLayout());
        treePanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
        this.add(treePanel, BorderLayout.CENTER);

        assertedInstancesTree = new OntologyTree(jenaOWLModel,
                JenaOWLModelToTreeAdapter.INSTANCES_MODEL, false);

        treePanel.add(assertedInstancesTree, BorderLayout.WEST);
    }

    private JPanel createNamespacePanel() {
        String baseNSText = assertedJenaOWLModel.getNamespaceManager().getDefaultNamespace();
        
        JPanel nsPanel = new JPanel(new BorderLayout());
        nsPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        JLabel baseNSLabel = new JLabel(GUIConstants.BASE_NAMESPACE_LABEL + baseNSText);
        nsPanel.add(baseNSLabel, BorderLayout.NORTH);
        
//        JLabel baseNSTextLabel = new JLabel(baseNSText + baseNSText);
//        nsPanel.add(baseNSTextLabel, BorderLayout.CENTER);
//        nsPanel.add(new JLabel(""), BorderLayout.SOUTH);
//        // nsPanel.setBorder(new LineBorder(Color.GRAY));
        return nsPanel;
    }

    private JPanel createAssertedInferredPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

        // Group the radio buttons.
        ButtonGroup group = new ButtonGroup();

        JRadioButton assertedButton = new JRadioButton(GUIConstants.ASSERTED);
        assertedButton.setActionCommand(GUIConstants.ASSERTED);
        assertedButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
        assertedButton.addActionListener(this);
        assertedButton.setSelected(true);
        panel.add(assertedButton);
        group.add(assertedButton);

        boolean isExpOutputInstancesPanel = (jenaOWLModelSource instanceof TestCase)
                && (instancesType == TestProject.INSTANCES_TYPE_SOURCE_AND_TARGET_ONTOLOGY);

        // if instancesPanel represents expected output instances in a test
        // case,
        // then inferred model should not be shown. The tool checks only,
        // if asserted statements from expected output are also present in the
        // actual output model.
        if (!isExpOutputInstancesPanel) {
            JRadioButton inferredButton = new JRadioButton(GUIConstants.INFERRED);
            inferredButton.setActionCommand(GUIConstants.INFERRED);
            inferredButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
            inferredButton.addActionListener(this);
            group.add(inferredButton);
            panel.add(inferredButton);
        }

        panel.setBorder(new LineBorder(Color.GRAY));
        return panel;
    }

    public JTree getAssertedInstancesTree() {
        return assertedInstancesTree;
    }

    public void actionPerformed(ActionEvent e) {
        // displays asserted tree
        if (e.getActionCommand() == GUIConstants.ASSERTED) {
            treePanel.removeAll();
            treePanel.add(assertedInstancesTree, BorderLayout.WEST);
        }
        // displays inferred tree
        else if (e.getActionCommand() == GUIConstants.INFERRED) {
            treePanel.removeAll();

            if (inferredInstancesTree != null) {
                treePanel.add(inferredInstancesTree, BorderLayout.WEST);
            } else {

                JenaOWLModel inferredModel = Controller.getInstance().getInferredModel(
                        assertedJenaOWLModel, jenaOWLModelSource, instancesType);
                inferredInstancesTree = new OntologyTree(inferredModel,
                        JenaOWLModelToTreeAdapter.INSTANCES_MODEL, false);
                treePanel.add(inferredInstancesTree, BorderLayout.WEST);
            }
        }
        repaint();
        revalidate();
    }
}
