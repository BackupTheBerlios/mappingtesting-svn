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

package org.qualipso.interop.semantics.mappingtesting.core.ruleexecution;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import jess.Rete;

import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;
import org.qualipso.interop.semantics.mappingtesting.model.TestProject;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLRuleEngineBridgeException;
import edu.stanford.smi.protegex.owl.swrl.bridge.jess.SWRLJessBridge;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.factory.SWRLJavaFactory;

/**
 * RuleExecutionEngine executes either all the rules in the mapping or a subset
 * of rules and creates a resulting model.
 * 
 */
public class RuleExecutionEngine {

    public static final String DEBUDDED_RULES_GROUP_NAME = "DebuggedRules";
    public static final String RULE_GROUP_CLASS = "RuleGroup";

//    prefixes, which have to be filtered out from rule execution result
    public static final String SWRL_PREFIX = "swrl";
    public static final String SWRLA_PREFIX = "swrla";
    public static final String SWRLB_PREFIX = "swrlb";
    
    public static final String TEMPORAL_PREFIX = "temporal";    
    public static final String TBOX_PREFIX = "tbox";
    public static final String ABOX_PREFIX = "abox";    
    public static final String SWRLX_PREFIX = "swrlx";
    public static final String SWRLM_PREFIX = "swrlm";
    public static final String SWRLXML_PREFIX = "swrlxml";
    public static final String SQWRL_PREFIX = "sqwrl";
    

//  imports, which have to be filtered out from rule execution result
    public static final String SWRL_ONTOLOGY_URI = "http://www.w3.org/2003/11/swrl";
    public static final String SWRLA_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/3.3/swrla.owl";
    public static final String SWRLB_ONTOLOGY_URI = "http://www.w3.org/2003/11/swrlb";
    
    public static final String TEMPORAL_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl";
    public static final String TBOX_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.3/tbox.owl";
    public static final String ABOX_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.3/abox.owl";    
    public static final String SWRLX_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.3/swrlx.owl";
    public static final String SWRLM_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlm.owl";
    public static final String SWRLXML_ONTOLOGY_URI = "http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlxml.owl";
    public static final String SQWRL_ONTOLOGY_URI = "http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl";
    

    
    private TestProject testProject;

    public RuleExecutionEngine(TestProject tp) {
        testProject = tp;
    }

    /**
     * Executes all the rules, contained in inputOWLModel.
     * 
     * @param inputOWLModel
     */
    public void executeRules(JenaOWLModel inputOWLModel) {
        
        try {
            Rete rete = new Rete();
            SWRLJessBridge bridge = null;
        
            // create the bridge from SWRL to Jess-Rules
            bridge = new SWRLJessBridge(inputOWLModel, rete);

            // Execute the  bridge
            bridge.infer();

        } catch (SWRLRuleEngineBridgeException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Executes a subset of rules in inputOWLModel. The rules, which should be
     * executed, are defined in mappingRuleNames.
     * 
     * @param inputOWLModel
     * @param mappingRuleNames
     *            names of rules, which should be executed
     */
    public void executeRules(JenaOWLModel inputOWLModel, Vector<String> mappingRuleNames) {
        Rete rete = new Rete();
        SWRLJessBridge bridge = null;

        // Set prefix for swrla.owl ontology and import this ontology

         UtilsForProtegeOWL.setPrefixForNameSpace(SWRLA_PREFIX,
         SWRLA_ONTOLOGY_URI + "#", inputOWLModel);
         UtilsForProtegeOWL.importOntology(inputOWLModel, SWRLA_ONTOLOGY_URI);
        // UtilsForProtegeOWL.printRepositories(mappingModelFor1TestCaseExec);

        NamespaceManager nsm = inputOWLModel.getNamespaceManager();
        String prefix = nsm.getPrefix(SWRLA_ONTOLOGY_URI + "#");

        // Add mappingRules to a RuleGroup, which will be executed.
        OWLNamedClass ruleGroupClass = inputOWLModel.getOWLNamedClass(prefix + ":"
                + RULE_GROUP_CLASS);

        OWLIndividual ruleGroupIndividual = ruleGroupClass
                .createOWLIndividual(DEBUDDED_RULES_GROUP_NAME);

        SWRLFactory factory = new SWRLFactory(inputOWLModel);
        Collection imps = factory.getImps();
        for (Iterator iter = imps.iterator(); iter.hasNext();) {
            Object imp = iter.next();

            if (imp instanceof SWRLImp) {
                SWRLImp swrlImp = (SWRLImp) imp;
                if (mappingRuleNames.contains(swrlImp.getName())) {
                    boolean success = swrlImp.addRuleGroup(DEBUDDED_RULES_GROUP_NAME);
                    boolean isInGroup = swrlImp.isInRuleGroup(DEBUDDED_RULES_GROUP_NAME);
                }
            }
        }

        try {
            // create the bridge from SWRL to Jess-Rules
            bridge = new SWRLJessBridge(inputOWLModel, rete);

            // Execute the  bridge
            bridge.infer(DEBUDDED_RULES_GROUP_NAME);

        } catch (SWRLRuleEngineBridgeException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Creates the resultingOWLModel from inputOWLModel, filtering out the SWRL
     * rules.
     * 
     * @param inputOWLModelClone
     * @param ruleExecMonitor -
     *            if true, the resulting ontology contains the instances of the
     *            source ontology, otherwise it contains only transformation
     *            results.
     * @return
     */
    public JenaOWLModel createResultingOWLModel(JenaOWLModel inputOWLModelClone,
            RuleExecutionMonitor ruleExecMonitor) {

        // THIS CODE IS CONSISTENT WITH THE FORUM E-MAIL ANSWER
        // OF TANIA TUDORACHE WITH THE SUBJECT "Repositories and importing ontologies"

        // Create a new OWL-model to save the resulting individuals in.
        JenaOWLModel resultingOWLModel = null;
        try {
            resultingOWLModel = ProtegeOWL.createJenaOWLModel();
        } catch (OntologyLoadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // The following code was suggested at:
        // https://mailman.stanford.edu/pipermail/protege-owl/2007-December/004917.html
        // Without those lines there will be an exception.
        resultingOWLModel.setOWLJavaFactory(new SWRLJavaFactory(resultingOWLModel));
        SWRLFactory factory = new SWRLFactory(resultingOWLModel);

        // ------------- End of: the following code was suggested at:

        UtilsForProtegeOWL.copyDefaultNS(inputOWLModelClone, resultingOWLModel);

        Vector<String> excludedPrefixes = new Vector<String>();
        excludedPrefixes.add(TestProject.MAPPING_NS_PREFIX);
        excludedPrefixes.add(RuleExecutionMonitor.RULE_EXEC_MONITOR_PREFIX);
        excludedPrefixes.add(SWRL_PREFIX);
        excludedPrefixes.add(SWRLA_PREFIX);
        excludedPrefixes.add(SWRLB_PREFIX);
        
        excludedPrefixes.add(TEMPORAL_PREFIX);
        excludedPrefixes.add(TBOX_PREFIX);
        excludedPrefixes.add(ABOX_PREFIX);
        excludedPrefixes.add(SWRLX_PREFIX);
        excludedPrefixes.add(SWRLM_PREFIX);
        excludedPrefixes.add(SWRLXML_PREFIX);
        excludedPrefixes.add(SQWRL_PREFIX);

        UtilsForProtegeOWL.copyPrefixes(inputOWLModelClone, resultingOWLModel, excludedPrefixes);

        // Create a list of imports, which should not be copies,
        // the repositories for these imports will not be copied as well.
        Vector<String> excludedImports = new Vector<String>();
        String execMonitorDefaultNS = ruleExecMonitor.getExecMonitorDefaultNS();
        // remove the # character at the end
        execMonitorDefaultNS = execMonitorDefaultNS.substring(0, execMonitorDefaultNS.length() - 1);
        excludedImports.add(execMonitorDefaultNS);

        String ontMappingDefaultNS = testProject.getOntologyMapping().getDefaultNamespace();
        ontMappingDefaultNS = ontMappingDefaultNS.substring(0, ontMappingDefaultNS.length() - 1);
        excludedImports.add(ontMappingDefaultNS);

        excludedImports.add(SWRL_ONTOLOGY_URI);
        excludedImports.add(SWRLA_ONTOLOGY_URI);
        excludedImports.add(SWRLB_ONTOLOGY_URI);
        
        excludedImports.add(TEMPORAL_ONTOLOGY_URI);
        excludedImports.add(TBOX_ONTOLOGY_URI);
        excludedImports.add(ABOX_ONTOLOGY_URI);
        excludedImports.add(SWRLX_ONTOLOGY_URI);
        excludedImports.add(SWRLM_ONTOLOGY_URI);
        excludedImports.add(SWRLXML_ONTOLOGY_URI);
        excludedImports.add(SQWRL_ONTOLOGY_URI);

        UtilsForProtegeOWL.copyRepositories(inputOWLModelClone, resultingOWLModel, excludedImports);
        UtilsForProtegeOWL.copyImports(inputOWLModelClone, resultingOWLModel, excludedImports);

        // Copy the inferred individuals into the new model
        // in order to be able to save the individuals without any SWRL-rules
        // and other irrelevant facts.
        Vector registeredNamespaces = new Vector<String>();
        registeredNamespaces.add(inputOWLModelClone.getNamespaceManager().getDefaultNamespace());
        registeredNamespaces.add(testProject.getSourceOntology().getDefaultNamespace());
        registeredNamespaces.add(testProject.getTargetOntology().getDefaultNamespace());
        UtilsForProtegeOWL.transferIndividuals(inputOWLModelClone, resultingOWLModel,
                registeredNamespaces);
        
        String toOWLModelDefaultNS = resultingOWLModel.getNamespaceManager().getDefaultNamespace();
        OWLUtil.renameOntology(resultingOWLModel, resultingOWLModel.getDefaultOWLOntology(),
                toOWLModelDefaultNS.substring(0, toOWLModelDefaultNS.length()-1));

        return resultingOWLModel;
    }

}
