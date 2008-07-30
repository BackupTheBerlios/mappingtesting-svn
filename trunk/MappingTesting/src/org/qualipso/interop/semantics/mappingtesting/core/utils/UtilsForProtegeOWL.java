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

package org.qualipso.interop.semantics.mappingtesting.core.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.JenaOWLModelLoadingException;
import org.qualipso.interop.semantics.mappingtesting.core.ruleexecution.RuleExecutionEngine;

import com.hp.hpl.jena.util.FileUtils;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.inference.pellet.ProtegePelletOWLAPIReasoner;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.RepositoryManager;
import edu.stanford.smi.protegex.owl.repository.impl.HTTPRepository;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.factory.SWRLJavaFactory;

public class UtilsForProtegeOWL {

    /**
     * Loads a JenaOWLModel from URI, defined by filePathURI. Before loading
     * creates a LocalFileRepository or a HTTP from the files, defined in
     * repositoryStrings.
     * 
     * It is assumed here, that the .repository files are not contained in the
     * test project.
     * 
     * @param filePathURI
     * @param repositoryStrings
     * @return
     * @throws JenaOWLModelLoadingException 
     */
    public static JenaOWLModel loadOWLModelFromURI(String filePathURI,
            Vector<String> repositoryStrings) throws JenaOWLModelLoadingException {

        if (repositoryStrings == null || repositoryStrings.size() == 0)
            try {
                return ProtegeOWL.createJenaOWLModelFromURI(filePathURI);
            } catch (Exception e1) {
                throw new JenaOWLModelLoadingException(e1.getMessage(), e1);
            }

        // This code is taken from:
        // https://mailman.stanford.edu/pipermail/protege-owl/2006-October/000364.html

        JenaOWLModel jenaOWLModel = null;
        try {
            jenaOWLModel = ProtegeOWL.createJenaOWLModel();
        } catch (OntologyLoadException e2) {
            throw new JenaOWLModelLoadingException(e2.getMessage(), e2);
        }
        URI projFilePathURI = URIUtilities.createURI(filePathURI);
        RepositoryManager repositoryManager = jenaOWLModel.getRepositoryManager();
        for (String repositoryString : repositoryStrings) {
            if (repositoryString.startsWith("http")) {
                URL repositoryURL = null;
                try {
                    repositoryURL = new URL(repositoryString);
                } catch (MalformedURLException e1) {
                    throw new JenaOWLModelLoadingException(e1.getMessage(), e1);
                }            

                HTTPRepository httpRepository = new HTTPRepository(repositoryURL);
                repositoryManager.addProjectRepository(httpRepository);
            } else {
                File importedFile = new File(repositoryString);
                LocalFileRepository localRepository = new LocalFileRepository(importedFile);
                localRepository.setForceReadOnly(true);
                repositoryManager.addProjectRepository(localRepository);
            }
        }

        try {
            jenaOWLModel.load(projFilePathURI, FileUtils.langXMLAbbrev);
        }catch (Exception e) {
            throw new JenaOWLModelLoadingException(e.getMessage(), e);
        }

        return jenaOWLModel;
    }

    /**
     * Saves jenaOWLModel in a file.
     * 
     * @param jenaOWLModel
     * @param file
     */
    public static void saveJenaOWLModel(JenaOWLModel jenaOWLModel, File file) {
        String modelDefaultNS = jenaOWLModel.getNamespaceManager().getDefaultNamespace();
        try {
            JenaOWLModel.save(file, jenaOWLModel.getOntModel(), FileUtils.langXMLAbbrev,
                    modelDefaultNS);
        } catch (Exception ex) {
            System.out.println("File NOT saved!");
        }
    }

    /**
     * Returns a dataproperty value of a property, defined by the propertyName,
     * of the owlIndividual in the owlModel.
     * 
     * @param owlModel
     * @param owlIndividual
     * @param propertyName
     * @return
     */
    public static Object getDataPropertyValue(JenaOWLModel owlModel, OWLIndividual owlIndividual,
            String propertyName) {
        RDFProperty property = owlModel.getRDFProperty(propertyName);
        return owlIndividual.getPropertyValue(property);
    }

    /**
     * This method replaces in the ruleText AND_CHAR and IMP_CHAR defined in
     * Protege SWRLParser with "/\" and "-->"
     * 
     * @param ruleText
     * @return modified ruleText
     */
    public static String modifyBrowserText(String ruleText) {
        // these chars have to be replaced by: /\ and -->
        char AND_CHAR = '\u2227'; // ^
        char IMP_CHAR = '\u2192'; // >
        Vector conjunctionIndexes = new Vector();
        Vector implicationIndexes = new Vector();

        char[] charArray = ruleText.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == AND_CHAR)
                conjunctionIndexes.add(i);
        }
//        ruleText = MiscUtils.replace(ruleText, conjunctionIndexes, "/\\");
        ruleText = MiscUtils.replace(ruleText, conjunctionIndexes, "^");
        charArray = ruleText.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == IMP_CHAR)
                implicationIndexes.add(i);
        }
        ruleText = MiscUtils.replace(ruleText, implicationIndexes, "-->");
        return ruleText;
    }

    /**
     * Copies all repositories, prefixes (and corresponding namespaces) and
     * necessary imports from a source owl-model to a destination owl-model in
     * order to be able to copy individuals afterwards.
     * 
     * @param fromOwlModel
     *            the source OWL model
     * @param toOwlModel
     *            the destination OWL model
     */
    public static void copyRepositoriesPrefixesImports(JenaOWLModel fromOwlModel,
            JenaOWLModel toOwlModel) {
        copyAllRepositories(fromOwlModel, toOwlModel);       
        copyAllPrefixes(fromOwlModel, toOwlModel);        
        copyAllImports(fromOwlModel, toOwlModel);
    }

    /**
     * Copies all the repositories in the fromOwlModel into toOwlModel.
     * 
     * @param fromOwlModel
     * @param toOwlModel
     */
    public static void copyAllRepositories(JenaOWLModel fromOwlModel, JenaOWLModel toOwlModel) {
        // make the mappings of owl-URIs to owl-Files available in the
        // destination owl-model
        // in order to be able to copy the imports
        List<Repository> fromOwlModelProjRepositories = fromOwlModel.getRepositoryManager()
                .getProjectRepositories();

        for (Iterator<Repository> repIt = fromOwlModelProjRepositories.iterator(); repIt.hasNext();) {
            Repository repository = repIt.next();
            toOwlModel.getRepositoryManager().addProjectRepository(repository);
        }
    }

    /**
     * Copies the repositories in the fromOwlModel into toOwlModel, excluding
     * the repositories in Vector excludedRepositories.
     * 
     * @param fromOWLModel
     * @param toOWLModel
     * @param excludedRepositories
     */
    public static void copyRepositories(JenaOWLModel fromOWLModel, JenaOWLModel toOWLModel,
            Vector<String> excludedRepositories) {

        // MAY BE TODO: It looks like this method doesn't filter out repositories
        // of type ForcedURLRetrievalRepository --> not sure if it is true for build 504

        if (excludedRepositories == null) {
            copyAllRepositories(fromOWLModel, toOWLModel);
            return;
        }

        List<Repository> fromOWLModelProjRepositories = fromOWLModel.getRepositoryManager()
                .getProjectRepositories();

        for (Iterator<Repository> repIt = fromOWLModelProjRepositories.iterator(); repIt.hasNext();) {
            Repository repository = repIt.next();
            boolean isExcludedRepository = false;
            for (Iterator iter = excludedRepositories.iterator(); iter.hasNext();) {
                String excludedNS = (String) iter.next();
                URI excludedNSURI = URIUtilities.createURI(excludedNS);
                if (repository.contains(excludedNSURI)) {
                    isExcludedRepository = true;
                    break;
                }
            }
            if (isExcludedRepository)
                continue;
            toOWLModel.getRepositoryManager().addProjectRepository(repository);
        }
    }

    /**
     * Copies all the prefixes and corresponding namespaces in the fromOWLModel
     * into the toOWLModel.
     * 
     * @param fromOWLModel
     * @param toOWLModel
     */
    public static void copyAllPrefixes(JenaOWLModel fromOWLModel, JenaOWLModel toOWLModel) {
        NamespaceManager nsmFrom = fromOWLModel.getNamespaceManager();
        NamespaceManager nsmTo = toOWLModel.getNamespaceManager();

        Collection<String> prefixes = nsmFrom.getPrefixes();
        // copy namespaces
        for (Iterator prefixIt = prefixes.iterator(); prefixIt.hasNext();) {
            String prefix = (String) prefixIt.next();
//            do not copy default namespace
            if(prefix.equals("")) continue;
            String namespaceForPrefix = nsmFrom.getNamespaceForPrefix(prefix);
            nsmTo.setPrefix(namespaceForPrefix, prefix);
        }
    }

    /**
     * Copies all the prefixes and corresponding namespaces in the fromOWLModel
     * into the toOWLModel, excluding all the prefixes, specified in
     * excludedPrefixes.
     * 
     * @param fromOWLModel
     * @param toOWLModel
     * @param excludedPrefixes
     */
    public static void copyPrefixes(JenaOWLModel fromOWLModel, JenaOWLModel toOWLModel,
            Vector<String> excludedPrefixes) {

        if (excludedPrefixes == null) {
            copyAllPrefixes(fromOWLModel, toOWLModel);
            return;
        }

        NamespaceManager nsmFrom = fromOWLModel.getNamespaceManager();
        NamespaceManager nsmTo = toOWLModel.getNamespaceManager();

        Collection<String> prefixes = nsmFrom.getPrefixes();
        // copy namespaces
        for (Iterator prefixIt = prefixes.iterator(); prefixIt.hasNext();) {
            String prefix = (String) prefixIt.next();
            if (excludedPrefixes.contains(prefix))
                continue;
            String namespaceForPrefix = nsmFrom.getNamespaceForPrefix(prefix);
            nsmTo.setPrefix(namespaceForPrefix, prefix);
        }
    }

    /**
     * Copies namespace and the prefix, which corresponds to this namespace in
     * the fromOWLModel into the toOWLModel. THIS METHOD IS NOT CALLED ANYWHERE,
     * NOT SURE IF IT IS NEEDED.
     * 
     * @param fromOWLModel
     * @param namespace
     * @param toOWLModel
     */
    public static void copyPrefix(JenaOWLModel fromOWLModel, String namespace,
            JenaOWLModel toOWLModel) {
        NamespaceManager nsmFrom = fromOWLModel.getNamespaceManager();
        NamespaceManager nsmTo = toOWLModel.getNamespaceManager();

        String prefix = nsmFrom.getPrefix(namespace);
        String namespaceForPrefix = nsmFrom.getNamespaceForPrefix(prefix);
        nsmTo.setPrefix(namespaceForPrefix, prefix);
    }

    /**
     * Sets the default namespace of the fromOwlModel as a default namespace in
     * the toOwlModel.
     * 
     * @param fromOwlModel
     * @param toOwlModel
     */
    public static void copyDefaultNS(JenaOWLModel fromOwlModel, JenaOWLModel toOwlModel) {
        String defaultNSInit = fromOwlModel.getNamespaceManager().getDefaultNamespace();
        toOwlModel.getNamespaceManager().setDefaultNamespace(defaultNSInit);
    }

    /**
     * Copies all the imports in the fromOwlModel into the toOwlModel.
     * 
     * @param fromOwlModel
     * @param toOwlModel
     */
    public static void copyAllImports(JenaOWLModel fromOwlModel, JenaOWLModel toOwlModel) {
        ImportHelper toOWLModelImportHelper = new ImportHelper(toOwlModel);
        Set allImports = fromOwlModel.getAllImports();
        for (Iterator iter = allImports.iterator(); iter.hasNext();) {
            String ontImport = (String) iter.next();
            URI ontImportUri = URIUtilities.createURI(ontImport);
            toOWLModelImportHelper.addImport(ontImportUri);
        }

        try {
            toOWLModelImportHelper.importOntologies(false);
        } catch (Exception e) {
             System.out.println("IMPORT FAILED!!!!!!!!!!!");
            e.printStackTrace();
        }
    }

    /**
     * Copies the imports in the fromOwlModel into the toOwlModel, excluding the
     * imports contained in the excludedImports Vector.
     * 
     * @param fromOwlModel
     * @param toOwlModel
     * @param excludedImports
     */
    public static void copyImports(JenaOWLModel fromOwlModel, JenaOWLModel toOwlModel,
            Vector<String> excludedImports) {

        ImportHelper toOWLModelImportHelper = new ImportHelper(toOwlModel);
        Set allImports = fromOwlModel.getAllImports();
        for (Iterator iter = allImports.iterator(); iter.hasNext();) {
            String ontImport = (String) iter.next();
            if (excludedImports.contains(ontImport))
                continue;
            URI ontImportUri = URIUtilities.createURI(ontImport);
            toOWLModelImportHelper.addImport(ontImportUri);
        }

        try {
            toOWLModelImportHelper.importOntologies(false);
        } catch (Exception e) {
             System.out.println("IMPORT FAILED!!!!!!!!!!!");
            e.printStackTrace();
        }
    }

    /**
     * Sets a prefix for the namespace in the jenaOWLModel.
     * 
     * @param prefix
     * @param namespace
     * @param jenaOWLModel
     */
    public static void setPrefixForNameSpace(String prefix, String namespace,
            JenaOWLModel jenaOWLModel) {
        NamespaceManager nsm = jenaOWLModel.getNamespaceManager();
        nsm.setPrefix(namespace, prefix);
    }

    /**
     * Clones all the rules in initialJenaOWLModel and adds them to a clone.
     * Repositories, prefixes, imports and default namespace are also copied.
     * 
     * @param initialJenaOWLModel
     * @return a clone of initialJenaOWLModel
     */
    public static JenaOWLModel cloneJenaOWLModelContainingRules(JenaOWLModel initialJenaOWLModel) {
        JenaOWLModel clone = null;
        try {
            clone = ProtegeOWL.createJenaOWLModel();
        } catch (OntologyLoadException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // was suggested at:
        // https://mailman.stanford.edu/pipermail/protege-owl/2007-December/004917.html
        clone.setOWLJavaFactory(new SWRLJavaFactory(clone));

        copyDefaultNS(initialJenaOWLModel, clone);
        copyRepositoriesPrefixesImports(initialJenaOWLModel, clone);
        SWRLFactory swrlFactoryForClone = new SWRLFactory(clone);
        try {
            swrlFactoryForClone.copyImps(initialJenaOWLModel);
        } catch (SWRLFactoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return clone;
    }

    /**
     * Creates a clone of jenaOWLModel, by copying all the user defined
     * individuals. Repositories, prefixes, imports and default namespace are
     * also copied.
     * 
     * @param jenaOWLModel
     * @return
     * @throws Exception 
     */
    public static JenaOWLModel cloneJenaOWLModel(JenaOWLModel jenaOWLModel){
        JenaOWLModel clone = null;
        try {
            clone = ProtegeOWL.createJenaOWLModel();
        } catch (OntologyLoadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // clone.setOWLJavaFactory(new SWRLJavaFactory(clone));
        copyDefaultNS(jenaOWLModel, clone);
        copyRepositoriesPrefixesImports(jenaOWLModel, clone);
        transferUserDefinedIndividuals(jenaOWLModel, clone);
        return clone;
    }

    /**
     * Shallow-copy all typed individuals (types must be of registered
     * namespaces) from one OWL-model to another.
     * 
     * @param fromOwlModel
     * @param toOwlModel
     * @param registeredNamespaces -
     *            only individuals from a namespace (or of types) defined in
     *            registeredNamespaces will be copied to the clone. It is
     *            important to define the registered namespaces, otherwise many
     *            individuals of type rdfs? or xsd? are copied and then there
     *            are problems with copy.setName(individual.getName()).
     */
    public static void transferIndividuals(JenaOWLModel fromOwlModel, JenaOWLModel toOwlModel,
            Collection registeredNamespaces) {

        // int i = 1;

        Collection<RDFIndividual> individuals = fromOwlModel.getRDFIndividuals(true);

        boolean isRegisteredNamespace;

        for (Iterator<RDFIndividual> it = individuals.iterator(); it.hasNext();) {
            isRegisteredNamespace = false;

            RDFIndividual individual = it.next();

            if (registeredNamespaces.contains(individual.getNamespace())
                    && (!individual.getName().equals(RuleExecutionEngine.DEBUDDED_RULES_GROUP_NAME)))
                isRegisteredNamespace = true;
            else {
                Collection individualTypes = individual.getRDFTypes();
                for (Iterator types = individualTypes.iterator(); types.hasNext();) {
                    RDFResource type = (RDFResource) types.next();
                    String namespace = type.getNamespace();
                    if (registeredNamespaces.contains(namespace))
                        isRegisteredNamespace = true;
                    break;
                }
            }

            // individualTypes = individual.getInferredTypes();
            // for (Iterator types = individualTypes.iterator();
            // types.hasNext();) {
            // RDFSClass type = (RDFSClass) types.next();
            // String namespace = type.getNamespace();
            // if (registeredNamespaces.contains(namespace))
            // isRegisteredNamespace = true;
            // }
            
            if (individual.getLocalName().equals("DebuggedRules")){
                continue;
            }

            if (isRegisteredNamespace) {
                RDFIndividual copy = (RDFIndividual) individual.shallowCopy(toOwlModel, null);
                copy = (RDFIndividual) copy.rename(individual.getName());
            }
        }
    }

    /**
     * Shallow-copy all user-defined individuals from one OWL-model to another.
     * No filtering of rules, etc. is done here; for that
     * transferIndividuals(...) should be used. Since no filtering is done, this
     * method is supposed to function faster, as transferIndividuals(...).
     * 
     * @param fromOwlModel
     * @param toOwlModel
     */
    public static void transferUserDefinedIndividuals(JenaOWLModel fromOwlModel,
            JenaOWLModel toOwlModel) {

        Collection<RDFIndividual> individuals = fromOwlModel.getUserDefinedRDFIndividuals(false);

        for (Iterator<RDFIndividual> it = individuals.iterator(); it.hasNext();) {

            RDFIndividual individual = it.next();
            RDFIndividual copy = (RDFIndividual) individual.shallowCopy(toOwlModel, null);
            copy = (RDFIndividual) copy.rename(individual.getName());
        }
    }

    /**
     * Imports an ontology, defined by its file URI (uriString) into
     * jenaOWLModel. The file, defined by the uriString is read and its base
     * namespace is added to the imports.
     * 
     * @param jenaOWLModel
     * @param uriString
     */
    public static void importOntology(JenaOWLModel jenaOWLModel, String uriString) {
        ImportHelper importHelper = new ImportHelper(jenaOWLModel);
        // this is the URI from where your ontology is created
        URI importUri = URIUtilities.createURI(uriString);
        importHelper.addImport(importUri);

        try {
            // do the actual import
            importHelper.importOntologies(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Imports all the ontologies, defined by their file URI, which are saved in
     * uriStrings, into jenaOWLModel. The files, defined by the uriStrings are
     * read and their base namespace is added to the imports.
     * 
     * @param jenaOWLModel
     * @param uriStrings
     */
    public static void importOntologies(JenaOWLModel jenaOWLModel, Vector<String> uriStrings) {
        ImportHelper importHelper = new ImportHelper(jenaOWLModel);

        for (Iterator iter = uriStrings.iterator(); iter.hasNext();) {
            String uriString = (String) iter.next();
            URI importUri = URIUtilities.createURI(uriString);
            importHelper.addImport(importUri);
        }

        try {
            importHelper.importOntologies(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProtegeReasoner createProtegePelletReasoner(JenaOWLModel owlModel) {

        // Get the reasoner manager and obtain a reasoner for the OWL model.
        ReasonerManager reasonerManager = ReasonerManager.getInstance();

        // Get an instance of the Protege Pellet reasoner
        ProtegeReasoner reasoner = reasonerManager.createProtegeReasoner(owlModel,
                ProtegePelletOWLAPIReasoner.class);
        return reasoner;
    }

    /**
     * Peforms consistency check of a JenaOWLModel owlModel.
     * 
     * @param reasoner
     * @param owlModel
     * @return
     */
    public static String performConsistencyCheck(ProtegeReasoner reasoner, JenaOWLModel owlModel) {
        try {
            reasoner.computeInconsistentConcepts();
        } catch (ProtegeReasonerException e) {
            e.printStackTrace();
        }
        Collection inconsistentClasses = owlModel.getInconsistentClasses();
        if (inconsistentClasses.size() == 0) {
            return "There are no inconsistent classes.";
        }

        String result = "";
        result += "WARNING: There are following inconsistent classes in the model: ";
        for (Iterator iter = inconsistentClasses.iterator(); iter.hasNext();) {
            OWLNamedClass inconsistentClass = (OWLNamedClass) iter.next();
            result += inconsistentClass.getLocalName() + ", ";
        }
        return result.substring(0, result.length()-2) + ".";
    }

}
