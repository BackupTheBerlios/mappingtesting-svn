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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import org.qualipso.interop.semantics.mappingtesting.gui.GUIConstants;
import org.qualipso.interop.semantics.mappingtesting.gui.JenaOWLModelToTreeAdapter;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * Panel that represents classes and properties of source and target ontologies.
 */
public class OntologyPanel extends JPanel implements ActionListener {
	
//	JenaOWLModel, which will be visualized
	private JenaOWLModel jenaOWLModel;
	
	private JPanel treePanel;
	

	/**
	 * A tree, representing classes and properties
	 */
	private OntologyTree propertiesTree;
	
	/**
	 * A tree, representing class hierarchy
	 */
	private OntologyTree classHierarchyTree;

	/**
	 * Create OntologyPanel for given jenaOWLModel
	 * @param jenaOWLModel
	 */
	public OntologyPanel(JenaOWLModel jenaOWLModel) {
		this.jenaOWLModel = jenaOWLModel;
		this.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());
		
		// no classes
		if (jenaOWLModel == null || 
				jenaOWLModel.getUserDefinedOWLNamedClasses().size() == 0) {
//			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel noClassesLabel = new JLabel(GUIConstants.NO_CLASSES_LABEL);
			this.add(noClassesLabel, BorderLayout.CENTER);
			return;
		}
		
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel namespacePanel = createNamespacePanel();
		topPanel.add(namespacePanel, BorderLayout.NORTH);
		JPanel radioButtonPanel = createPropertiesClassHierarchyPanel();
		topPanel.add(radioButtonPanel, BorderLayout.SOUTH);
		this.add(topPanel, BorderLayout.NORTH);		
		
		treePanel = new JPanel(new BorderLayout());
		treePanel.setBackground(GUIConstants.INNERPANEL_BACKGROUND_COLOR);
		this.add(treePanel, BorderLayout.CENTER);

		propertiesTree = new OntologyTree(jenaOWLModel,
				JenaOWLModelToTreeAdapter.CLASSES_AND_PROPERTIES_MODEL, false);
		treePanel.add(propertiesTree, BorderLayout.WEST);
	}
	
	private JPanel createNamespacePanel() {
        String baseNSText = jenaOWLModel.getNamespaceManager().getDefaultNamespace();
		JPanel nsPanel = new JPanel (new BorderLayout());
		nsPanel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
		JLabel baseNSLabel = new JLabel(GUIConstants.BASE_NAMESPACE_LABEL + baseNSText);
		nsPanel.add(baseNSLabel, BorderLayout.NORTH);
		
//		JLabel baseNSTextLabel = new JLabel(baseNSText);
//		nsPanel.add(baseNSTextLabel, BorderLayout.CENTER);
//		nsPanel.add(new JLabel(""), BorderLayout.SOUTH);
////		nsPanel.setBorder(new LineBorder(Color.BLACK));
		return nsPanel;
	}
	
	private JPanel createPropertiesClassHierarchyPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);

		JRadioButton assertedButton = new JRadioButton(GUIConstants.PROPERTIES);
		assertedButton.setActionCommand(GUIConstants.PROPERTIES);
		assertedButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
		assertedButton.addActionListener(this);
		assertedButton.setSelected(true);

		JRadioButton inferredButton = new JRadioButton(GUIConstants.CLASS_HIERARCHY);
		inferredButton.setActionCommand(GUIConstants.CLASS_HIERARCHY);
		inferredButton.setBackground(GUIConstants.PANEL_BACKGROUND_COLOR);
		inferredButton.addActionListener(this);

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(assertedButton);
		group.add(inferredButton);

		panel.add(assertedButton);
		panel.add(inferredButton);

		panel.setBorder(new LineBorder(Color.GRAY));
		return panel;
	}


	public void actionPerformed(ActionEvent e) {
		//		displays properties tree
		if (e.getActionCommand() == GUIConstants.PROPERTIES) {
			treePanel.removeAll();
			treePanel.add(propertiesTree, BorderLayout.WEST);
		}
		//		displays inferred tree
		else if (e.getActionCommand() == GUIConstants.CLASS_HIERARCHY) {
			treePanel.removeAll();

			if (classHierarchyTree == null)
				classHierarchyTree = new OntologyTree(jenaOWLModel,
						JenaOWLModelToTreeAdapter.CLASS_HIERARCHY_MODEL, true);				
				treePanel.add(classHierarchyTree, BorderLayout.WEST);
		}
		repaint();
		revalidate();
	}
}
