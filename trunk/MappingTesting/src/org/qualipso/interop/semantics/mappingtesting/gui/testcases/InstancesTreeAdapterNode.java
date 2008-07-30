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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.gui.AdapterNode;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

/**
 * This class wraps individuals and properties in jenaOWLModel and returns the
 * text which should be displayed in the tree. It also returns children, index
 * values, and child counts. This node can be of three types: 1. A root node for
 * the whole jenaOWLModel, which is invisible 2. A node for a instance in
 * jenaOWLModel 3. A node for a property and its value in jenaOWLModel
 * 
 */
public class InstancesTreeAdapterNode implements AdapterNode {

    private static final int JENA_OWL_MODEL_NODE = 0;
    private static final int INDIVIDUAL_NODE = 1;
    private static final int PROPERTY_AND_VALUE_NODE = 2;
    private int type;

    /**
     * A JenaOWLModel, which should be visualised. Is not null only, when type ==
     * JENA_OWL_MODEL_NODE.
     */
    private JenaOWLModel jenaOWLModel = null;

    /**
     * A Vector (ordered!), containing all the individuals in jenaOWLModel. Is
     * not null only, when type == JENA_OWL_MODEL_NODE.
     */
    private Vector<RDFIndividual> individuals = null;

    /**
     * An RDFIndividual, which should be visualised. Is not null only, when type ==
     * INDIVIDUAL_NODE.
     */
    private RDFIndividual rdfIndividual = null;
    /**
     * A Vector (ordered!), containing all the properties of an rdfIndividual.
     * Is not null only, when type == INDIVIDUAL_NODE.
     */
    private Vector<PropertyAndValueNode> propertiesAndValues = null;

    /**
     * A node, which wraps a property of an rdfIndividual and property value.
     * which should be visualised. Is not null only, when type ==
     * PROPERTY_AND_VALUE_NODE.
     */
    private PropertyAndValueNode propAndValueNode = null;

    /**
     * Creates an invisible root adapter node
     * 
     * @param jenaOWLModel
     */
    public InstancesTreeAdapterNode(JenaOWLModel jenaOWLModel) {
        this.jenaOWLModel = jenaOWLModel;
        this.type = JENA_OWL_MODEL_NODE;

        // Parameters:
        // onlyVisibleClasses - true to only return instances of visible classes
        Collection individualsCollection = jenaOWLModel.getUserDefinedRDFIndividuals(false);
        // copy individuals from Collection to Vector, so that they become
        // ordered
        individuals = new Vector<RDFIndividual>();
        for (Iterator iter = individualsCollection.iterator(); iter.hasNext();) {
            // casting is correct: RDFIndividual is superinterface of
            // OWLIndividual
            RDFIndividual ind = (RDFIndividual) iter.next();
            individuals.add(ind);
        }
        ;
    }

    /**
     * Creates an adapter node, which contains and visualises rdfIndividual
     * 
     * @param rdfIndividual
     */
    public InstancesTreeAdapterNode(RDFIndividual rdfIndividual) {
        this.rdfIndividual = rdfIndividual;
        this.type = INDIVIDUAL_NODE;
        propertiesAndValues = new Vector<PropertyAndValueNode>();
        Collection rdfProperties = rdfIndividual.getRDFProperties();
        for (Iterator iter = rdfProperties.iterator(); iter.hasNext();) {
            RDFProperty property = (RDFProperty) iter.next();
            // filter out properties added by protege
            if (property.getName().startsWith("protege:"))
                continue;

            Collection propValues = rdfIndividual.getPropertyValues(property, true);
            for (Iterator iterator = propValues.iterator(); iterator.hasNext();) {
                Object propValue = (Object) iterator.next();
                propertiesAndValues.add(new PropertyAndValueNode(property, propValue));
            }
        }
    }

    /**
     * Creates an adapter node, which contains and visualises a property and
     * property value
     * 
     * @param node
     */
    public InstancesTreeAdapterNode(PropertyAndValueNode node) {
        this.propAndValueNode = node;
        this.type = PROPERTY_AND_VALUE_NODE;
    }

    public String toString() {
        if (type == JENA_OWL_MODEL_NODE)
            return "root";
        else if (type == INDIVIDUAL_NODE)
            return rdfIndividual.getBrowserText();
        else if (type == PROPERTY_AND_VALUE_NODE)
            return getPropertyString();
        else
            return "???";
    }

    /**
     * Returns a string, which will represent either a datatype or an object
     * property in the tree.
     * 
     * @return
     */
    private String getPropertyString() {
//        String result = propAndValueNode.getProperty().getLocalName() + "   ";
        String result = propAndValueNode.getProperty().getBrowserText() + "   ";
        Object propValue = propAndValueNode.getPropValue();
        if (propValue instanceof OWLNamedClass) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) propValue;
            return result + owlNamedClass.getBrowserText();
        } else if (propValue instanceof RDFIndividual) {
            RDFIndividual rdfIndividual = (RDFIndividual) propValue;
            return result + rdfIndividual.getBrowserText();
        }
        return result + propValue;
    }

    /**
     * @param searchIndex
     * @return
     */
    public InstancesTreeAdapterNode child(int searchIndex) {
        if (type == JENA_OWL_MODEL_NODE)
            return new InstancesTreeAdapterNode(individuals.get(searchIndex));
        else if (type == INDIVIDUAL_NODE)
            return new InstancesTreeAdapterNode(propertiesAndValues.get(searchIndex));
        return null;
    }

    public int childCount() {
        if (type == JENA_OWL_MODEL_NODE)
            return individuals.size();
        else if (type == INDIVIDUAL_NODE)
            return propertiesAndValues.size();
        else
            return 0;
    }

    public boolean isLeaf() {
        if (type == JENA_OWL_MODEL_NODE && individuals.size() > 0)
            return false;
        else if (type == INDIVIDUAL_NODE && propertiesAndValues.size() > 0)
            return false;
        return true;
    }

    /**
     * @param child
     * @return
     */
    public int indexOfChild(AdapterNode child) {
        if (!(child instanceof InstancesTreeAdapterNode))
            return -1;
        InstancesTreeAdapterNode childNode = (InstancesTreeAdapterNode) child;

        if (type == JENA_OWL_MODEL_NODE && childNode.getType() == INDIVIDUAL_NODE) {
            RDFIndividual childRDFIndividual = childNode.getRdfIndividual();
            for (int i = 0; i < individuals.size(); i++) {
                RDFIndividual ind = individuals.get(i);
                if (ind.equals(childRDFIndividual)) {
                     return i;
                }
            }
            return -1;
        }

        else if (type == INDIVIDUAL_NODE && childNode.getType() == PROPERTY_AND_VALUE_NODE) {
            PropertyAndValueNode childPropAndValueNode = childNode.getPropAndValueNode();
            for (int i = 0; i < propertiesAndValues.size(); i++) {
                PropertyAndValueNode propAndValueNode = propertiesAndValues.get(i);
                if (propAndValueNode.equals(childPropAndValueNode)) {
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

    /**
     * This class wraps an RDFProperty and its value.
     */
    class PropertyAndValueNode {

        private RDFProperty property;
        private Object propValue;

        public PropertyAndValueNode(RDFProperty property, Object propValue) {
            this.property = property;
            this.propValue = propValue;
        }

        public RDFProperty getProperty() {
            return property;
        }

        public Object getPropValue() {
            return propValue;
        }

        public boolean equals(PropertyAndValueNode node) {
            if (!(property.equals(node.getProperty())))
                return false;
            if (!(propValue.equals(node.getPropValue())))
                return false;
            return true;
        }
    }

    public RDFIndividual getRdfIndividual() {
        return rdfIndividual;
    }

    public PropertyAndValueNode getPropAndValueNode() {
        return propAndValueNode;
    }
}
