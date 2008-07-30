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
import java.util.Set;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.gui.AdapterNode;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLObjectProperty;

/**
 * This class wraps classes and properties in jenaOWLModel and returns the text
 * which should be displayed in the tree. It also returns children, index
 * values, and child counts. This node can be of three types: 1. A root node for
 * the whole jenaOWLModel, which is invisible 2. A node for a class in
 * jenaOWLModel 3. A node for a property in jenaOWLModel
 */
public class ClassesAndPropertiesTreeAdapterNode implements AdapterNode {

    private static final int JENA_OWL_MODEL_NODE = 0;
    private static final int CLASS_NODE = 1;
    private static final int PROPERTY_NODE = 2;
    private int type;

    /**
     * A JenaOWLModel, which should be visualised. Is not null only, when type ==
     * JENA_OWL_MODEL_NODE.
     */
    private JenaOWLModel jenaOWLModel;

    /**
     * A Vector (ordered!), containing all the classes in jenaOWLModel. Is not
     * null only, when type == JENA_OWL_MODEL_NODE.
     */
    private Vector<OWLNamedClass> classes = null;

    /**
     * An OWLNamedClass, which should be visualised. Is not null only, when type ==
     * CLASS_NODE.
     */
    private OWLNamedClass owlNamedClass;

    /**
     * A Vector (ordered!), containing all the properties of an owlNamedClass.
     * Is not null only, when type == CLASS_NODE.
     */
    private Vector<AbstractOWLProperty> properties = null;

    /**
     * An AbstractOWLProperty, which should be visualised. Is not null only,
     * when type == PROPERTY_NODE.
     */
    private AbstractOWLProperty abstractOWLProperty;

    /**
     * Creates an invisible root adapter node
     * 
     * @param jenaOWLModel
     */
    public ClassesAndPropertiesTreeAdapterNode(JenaOWLModel jenaOWLModel) {
        this.jenaOWLModel = jenaOWLModel;
        this.type = JENA_OWL_MODEL_NODE;
        classes = new Vector<OWLNamedClass>();
        Collection cls = jenaOWLModel.getUserDefinedOWLNamedClasses();
        // copy classes from Collection to Vector, so that they become ordered
        for (Iterator iter = cls.iterator(); iter.hasNext();) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) iter.next();
            classes.add(owlNamedClass);
        }
        ;
    }

    /**
     * Creates an adapter node, which contains and visualises owlNamedClass
     * 
     * @param owlNamedClass
     */
    public ClassesAndPropertiesTreeAdapterNode(OWLNamedClass owlNamedClass) {
        this.owlNamedClass = owlNamedClass;
        this.type = CLASS_NODE;
        properties = new Vector<AbstractOWLProperty>();
        Set props = owlNamedClass.getAssociatedProperties();
        for (Iterator iterator = props.iterator(); iterator.hasNext();) {
            AbstractOWLProperty prop = (AbstractOWLProperty) iterator.next();
            properties.add(prop);
        }
    }

    /**
     * Creates an adapter node, which contains and visualises
     * abstractOWLProperty
     * 
     * @param abstractOWLProperty
     */
    public ClassesAndPropertiesTreeAdapterNode(AbstractOWLProperty abstractOWLProperty) {
        this.abstractOWLProperty = abstractOWLProperty;
        this.type = PROPERTY_NODE;
    }

    public String toString() {
        if (type == JENA_OWL_MODEL_NODE)
            return "root";
        else if (type == CLASS_NODE)
            return owlNamedClass.getBrowserText();
        else if (type == PROPERTY_NODE)
            return getPropertyString();
        else
            return "???";
    }

    /**
     * Returns a string, which represents either a datatype or an object
     * property in the tree.
     * 
     * @return
     */
    private String getPropertyString() {
//        String result = abstractOWLProperty.getLocalName() + " type:";
        String result = abstractOWLProperty.getBrowserText() + "  type:";
        String propRange = "Any";
        if (abstractOWLProperty instanceof DefaultOWLDatatypeProperty) {
            if (abstractOWLProperty.getRangeDatatype() != null)
                propRange = abstractOWLProperty.getRangeDatatype().getBrowserText();
        } else if (abstractOWLProperty instanceof DefaultOWLObjectProperty) {
            DefaultOWLObjectProperty objectProp = (DefaultOWLObjectProperty) abstractOWLProperty;
            propRange = objectProp.getRange().getBrowserText();
        }
        return result + propRange;
    }

    /**
     * @param searchIndex
     * @return
     */
    public ClassesAndPropertiesTreeAdapterNode child(int searchIndex) {
        if (type == JENA_OWL_MODEL_NODE)
            return new ClassesAndPropertiesTreeAdapterNode(classes.get(searchIndex));
        else if (type == CLASS_NODE)
            return new ClassesAndPropertiesTreeAdapterNode(properties.get(searchIndex));
        else
            return null;
    }

    public int childCount() {
        if (type == JENA_OWL_MODEL_NODE)
            return classes.size();
        else if (type == CLASS_NODE)
            return properties.size();
        else
            return 0;
    }

    public boolean isLeaf() {
        if (type == JENA_OWL_MODEL_NODE && classes.size() > 0)
            return false;
        else if (type == CLASS_NODE && properties.size() > 0)
            return false;
        return true;
    }

    /**
     * 
     * @param child
     * @return
     */
    public int indexOfChild(AdapterNode child) {
        if (!(child instanceof ClassesAndPropertiesTreeAdapterNode))
            return -1;
        ClassesAndPropertiesTreeAdapterNode childNode = (ClassesAndPropertiesTreeAdapterNode) child;

        if (type == JENA_OWL_MODEL_NODE && childNode.getType() == CLASS_NODE) {
            OWLNamedClass childOWLNamedClass = childNode.getOwlNamedClass();
            for (int i = 0; i < classes.size(); i++) {
                OWLNamedClass cl = classes.get(i);
                if (cl.equals(childOWLNamedClass)) {
                    return i;
                }
            }
            return -1;
        }

        else if (type == CLASS_NODE && childNode.getType() == PROPERTY_NODE) {
            AbstractOWLProperty childOWLProperty = childNode.getAbstractOWLProperty();
            for (int i = 0; i < properties.size(); i++) {
                AbstractOWLProperty prop = properties.get(i);
                if (prop.equals(childOWLProperty)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public int getType() {
        return type;
    }

    public OWLNamedClass getOwlNamedClass() {
        return owlNamedClass;
    }

    public AbstractOWLProperty getAbstractOWLProperty() {
        return abstractOWLProperty;
    }
}
