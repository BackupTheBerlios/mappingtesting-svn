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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.gui.AdapterNode;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

/**
 * This class wraps classes in jenaOWLModel.
 */

public class ClassHierarchyTreeAdapterNode implements AdapterNode {

    private static final int JENA_OWL_MODEL_NODE = 0;
    private static final int CLASS_NODE = 1;
    private int type;

    /**
     * A JenaOWLModel, which should be visualised. Is not null only, when type ==
     * JENA_OWL_MODEL_NODE.
     */
    private JenaOWLModel jenaOWLModel;

    /**
     * A Vector (ordered!), containing the classes in jenaOWLModel, which have
     * no other superclasses besides owl:Thing Is not null only, when type ==
     * JENA_OWL_MODEL_NODE.
     */
    private Vector<OWLNamedClass> subClasses = null;

    /**
     * An OWLNamedClass, which should be visualised. Is not null only, when type ==
     * CLASS_NODE.
     */
    private OWLNamedClass owlNamedClass;

    /**
     * Creates an invisible root adapter node
     * 
     * @param jenaOWLModel
     */
    public ClassHierarchyTreeAdapterNode(JenaOWLModel jenaOWLModel) {
        this.jenaOWLModel = jenaOWLModel;
        this.type = JENA_OWL_MODEL_NODE;
        subClasses = new Vector<OWLNamedClass>();

        Collection cls = jenaOWLModel.getUserDefinedOWLNamedClasses();
        // copy classes from Collection to Vector, so that they become ordered
        for (Iterator iter = cls.iterator(); iter.hasNext();) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) iter.next();
            Collection namedSuperclasses = owlNamedClass.getNamedSuperclasses(false);
            if (namedSuperclasses.size() == 1
                    && namedSuperclasses.contains(jenaOWLModel.getOWLThingClass()))
                subClasses.add(owlNamedClass);
        }
        ;
    }

    /**
     * Creates an adapter node, which contains and visualises owlNamedClass
     * 
     * @param owlNamedClass
     */
    public ClassHierarchyTreeAdapterNode(OWLNamedClass owlNamedClass) {
        this.owlNamedClass = owlNamedClass;
        this.type = CLASS_NODE;
        subClasses = new Vector<OWLNamedClass>();

        Collection cls = owlNamedClass.getSubclasses(false);
        // copy subclasses from Collection to Vector, so that they become
        // ordered
        for (Iterator iter = cls.iterator(); iter.hasNext();) {
            Object c = iter.next();
            if (!(c instanceof OWLNamedClass))
                continue;
            OWLNamedClass subClass = (OWLNamedClass) c;
            subClasses.add(subClass);
        }
        ;
    }

    public String toString() {
        if (type == JENA_OWL_MODEL_NODE)
            return "owl:Thing";
        else if (type == CLASS_NODE)
            return owlNamedClass.getBrowserText();
        else
            return "???";
    }

    /**
     * @param searchIndex
     * @return
     */
    public ClassHierarchyTreeAdapterNode child(int searchIndex) {
        return new ClassHierarchyTreeAdapterNode(subClasses.get(searchIndex));
    }

    public int childCount() {
        return subClasses.size();
    }

    public boolean isLeaf() {
        if (subClasses.size() > 0)
            return false;
        return true;
    }

    /**
     * @param child
     * @return
     */
    public int indexOfChild(AdapterNode child) {
        if (!(child instanceof ClassHierarchyTreeAdapterNode))
            return -1;
        ClassHierarchyTreeAdapterNode childNode = (ClassHierarchyTreeAdapterNode) child;

        OWLNamedClass childOWLNamedClass = childNode.getOwlNamedClass();
        for (int i = 0; i < subClasses.size(); i++) {
            OWLNamedClass cl = subClasses.get(i);
            if (cl.equals(childOWLNamedClass)) {
                return i;
            }
        }
        return -1;
    }

    public OWLNamedClass getOwlNamedClass() {
        return owlNamedClass;
    }
}
