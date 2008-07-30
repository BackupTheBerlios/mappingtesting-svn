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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.ClassHierarchyTreeAdapterNode;
import org.qualipso.interop.semantics.mappingtesting.gui.ontologies.ClassesAndPropertiesTreeAdapterNode;
import org.qualipso.interop.semantics.mappingtesting.gui.testcases.InstancesTreeAdapterNode;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * A tree model, which is used in trees, displaying ontologies and ontology
 * instances. Adapts different entities in jenaOWLModel to tree nodes. This tree
 * model can be of three types: CLASSES_AND_PROPERTIES_MODEL,
 * CLASS_HIERARCHY_MODEL or INSTANCES_MODEL.
 */
public class JenaOWLModelToTreeAdapter implements TreeModel {

    public static final int CLASSES_AND_PROPERTIES_MODEL = 0;
    public static final int CLASS_HIERARCHY_MODEL = 1;
    public static final int INSTANCES_MODEL = 2;

    private JenaOWLModel jenaOWLModel;
    private int modelType;

    /**
     * Creates a JenaOWLModelToTreeAdapter, having defined modelType, for
     * jenaOWLModel.
     * 
     * @param jenaOWLModel
     * @param modelType
     */
    public JenaOWLModelToTreeAdapter(JenaOWLModel jenaOWLModel, int modelType) {
        this.jenaOWLModel = jenaOWLModel;
        this.modelType = modelType;
    }

    public void addTreeModelListener(TreeModelListener l) {
        // TODO Auto-generated method stub
    }

    public Object getChild(Object parent, int index) {
        AdapterNode node = (AdapterNode) parent;
        return node.child(index);
    }

    public int getChildCount(Object parent) {
        AdapterNode node = (AdapterNode) parent;
        return node.childCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        AdapterNode parentNode = (AdapterNode) parent;
        AdapterNode childNode = (AdapterNode) child;
        return parentNode.indexOfChild(childNode);
    }

    public Object getRoot() {
        if (modelType == CLASSES_AND_PROPERTIES_MODEL)
            return new ClassesAndPropertiesTreeAdapterNode(jenaOWLModel);
        else if (modelType == CLASS_HIERARCHY_MODEL)
            return new ClassHierarchyTreeAdapterNode(jenaOWLModel);
        else if (modelType == INSTANCES_MODEL)
            return new InstancesTreeAdapterNode(jenaOWLModel);
        return null;
    }

    public boolean isLeaf(Object node) {
        AdapterNode adapterNode = (AdapterNode) node;
        return adapterNode.isLeaf();
    }

    public void removeTreeModelListener(TreeModelListener l) {
        // TODO Auto-generated method stub

    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        // TODO Auto-generated method stub

    }

}
