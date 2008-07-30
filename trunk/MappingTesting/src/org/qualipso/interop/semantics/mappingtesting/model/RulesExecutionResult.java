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

package org.qualipso.interop.semantics.mappingtesting.model;

import java.util.Vector;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * In this entity class all information about execution of mapping rules is
 * stored. Mapping rules could be executed in a test case (subclass
 * TestCaseExecutionResult) or during debugging.
 */
public class RulesExecutionResult {

    protected String inputConsistencyCheckResult;
    protected String resultingModelConsistencyCheckResult;
    protected JenaOWLModel resultingOWLModelAsserted;
    protected JenaOWLModel resultingOWLModelInferred;
    protected Vector<MappingRule> firedRules;

    public RulesExecutionResult() {
        firedRules = new Vector<MappingRule>();
    }

    /**
     * Adds a mapping rule to the set of fired rules.
     * 
     * @param mr
     */
    public void addFiredRule(MappingRule mr) {
        firedRules.add(mr);
    }

    public Vector<MappingRule> getFiredRules() {
        return firedRules;
    }

    /**
     * Returns true, if during test case execution mr was fired, else otherwise.
     * 
     * @param mr
     * @return
     */
    public boolean isRuleFired(MappingRule mr) {
        return firedRules.contains(mr);
    }

    public String getInputConsistencyCheckResult() {
        return inputConsistencyCheckResult;
    }

    public void setInputConsistencyCheckResult(String inputConsistencyCheckResult) {
        this.inputConsistencyCheckResult = inputConsistencyCheckResult;
    }

    public String getResultingModelConsistencyCheckResult() {
        return resultingModelConsistencyCheckResult;
    }

    public void setResultingModelConsistencyCheckResult(String resultingModelConsistencyCheckResult) {
        this.resultingModelConsistencyCheckResult = resultingModelConsistencyCheckResult;
    }

    public JenaOWLModel getResultingOWLModelAsserted() {
        return resultingOWLModelAsserted;
    }

    public void setResultingOWLModelAsserted(JenaOWLModel resultingOWLModel) {
        this.resultingOWLModelAsserted = resultingOWLModel;
    }

    public JenaOWLModel getResultingOWLModelInferred() {
        return resultingOWLModelInferred;
    }

    public void setResultingOWLModelInferred(JenaOWLModel resultingOWLModelInferred) {
        this.resultingOWLModelInferred = resultingOWLModelInferred;
    }
    
    public void dispose(){
        if (resultingOWLModelAsserted != null)
            resultingOWLModelAsserted.dispose();
        
        if (resultingOWLModelInferred != null)
            resultingOWLModelInferred.dispose();
        firedRules = null;        
    }

}
