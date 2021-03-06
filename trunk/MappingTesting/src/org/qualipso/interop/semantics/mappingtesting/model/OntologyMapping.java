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
 * partially funded by the European Commission under EU�s sixth
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

package org.qualipso.interop.semantics.mappingtesting.model;

import java.util.Iterator;
import java.util.Vector;

/**
 * This class represents a data structure for saving information about ontology
 * mapping, contained in the test project.
 * 
 */
public class OntologyMapping extends Ontology {

    private Vector<MappingRule> mappingRules;

    public OntologyMapping(TestProject tp, String path) {
        super(tp, path);
        mappingRules = new Vector<MappingRule>();
    }

    public void addMappingRule(MappingRule mappingRule) {
         mappingRules.add(mappingRule);
    }

    public Vector<MappingRule> getMappingRules() {
        return mappingRules;
    }

    public MappingRule getRule(String ruleName) {
        for (Iterator iter = mappingRules.iterator(); iter.hasNext();) {
            MappingRule mappingRule = (MappingRule) iter.next();
            if (mappingRule.getName().equals(ruleName))
                return mappingRule;
        }
        return null;
    }

    public void setMappingRules(Vector<MappingRule> mappingRules) {
        this.mappingRules = mappingRules;
    }

}
