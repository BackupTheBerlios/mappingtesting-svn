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

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import org.qualipso.interop.semantics.mappingtesting.gui.JenaOWLModelToTreeAdapter;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * A tree, which visualizes ontology. It has different forms: 1. A tree,
 * representing classes and their associated properties 2. A tree, representing
 * ontology instances with properties and property values.
 * 
 */
public class OntologyTree extends JTree {

    /**
     * Creates a tree, which visualizes ontology.
     * 
     * @param jenaOWLModel
     * @param treeModelType
     *            defines, which form a tree should have.
     */
    public OntologyTree(JenaOWLModel jenaOWLModel, int treeModelType, boolean visibleRoot) {
        super();
        JenaOWLModelToTreeAdapter treeModel = new JenaOWLModelToTreeAdapter(jenaOWLModel,
                treeModelType);
        setModel(treeModel);
        setRootVisible(visibleRoot);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        // this.tree.setCellRenderer(new OntologyTreeRenderer());
    }

}
