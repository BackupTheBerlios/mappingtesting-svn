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
package org.qualipso.interop.semantics.mappingtesting.core;

import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.qualipso.interop.semantics.mappingtesting.core.exceptions.JenaOWLModelLoadingException;
import org.qualipso.interop.semantics.mappingtesting.core.utils.MiscUtils;
import org.qualipso.interop.semantics.mappingtesting.core.utils.UtilsForProtegeOWL;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.util.FileUtils;

import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.JenaCreator;

/**
 * InferredJenaOWLModelCreator is used for creating inferred models from
 * asserted models by making asserted knowledge inferred.
 * 
 */
public class InferredOWLModelCreator {

	/**
	 * In this file the inferred model is temporary saved.
	 */
	private static final String INFERRED_MODEL_TEMP_FILE = "etc/inferredOntModel.owl";
    private static final String ASSERTED_MODEL_TEMP_FILE = "etc/tempAssertedModel.owl";
    
	
	/**
	 * Creates a InferredJenaOWLModelCreator.
	 */
	public InferredOWLModelCreator() {
	}

	/**
	 * Creates an inferred model from assertedJenaOWLModel by saving inferred
	 * knowledge as asserted in a temporary file and then reading the file.
	 * 
	 * @param assertedJenaOWLModel 
	 * @param altEntries
	 *            entries for an alternative copy of the document with the given
	 *            document URI. The key is public URI of the ontology document.
	 *            The element is a locally resolvable URL where an alternative
	 *            copy of the ontology document can be found.
	 * @return
	 */
	public JenaOWLModel createInferredModel(JenaOWLModel assertedJenaOWLModel,
			Hashtable altEntries) {
		
		OntDocumentManager docMgr = OntDocumentManager.getInstance();
		// This Vector is used, when inferred model, saved to
		// INFERRED_MODEL_TEMP_FILE will be loaded again.
		Vector<String> importedFiles = new Vector<String>();
		// Add alternative entries to the docMgr
		for (Iterator iter = altEntries.keySet().iterator(); iter.hasNext();) {
			String docURI = (String) iter.next();
			// add an importedFileURI without #.
			docMgr.addAltEntry(docURI.substring(0, docURI.length() - 1),
					"file:///" + altEntries.get(docURI));

			importedFiles.add((String) altEntries.get(docURI));
		}
		
		assertedJenaOWLModel = saveAndLoadModel(assertedJenaOWLModel, importedFiles);

		// perform reasoning
		ProtegeReasoner reasoner = UtilsForProtegeOWL
				.createProtegePelletReasoner(assertedJenaOWLModel);
		try {
			reasoner.classifyTaxonomy();
			reasoner.computeInferredHierarchy();
			reasoner.computeInferredIndividualTypes();
		} catch (ProtegeReasonerException e) {
			e.printStackTrace();
		}

		// A collection of target classes
		Collection clses = assertedJenaOWLModel.getUserDefinedOWLNamedClasses();
		clses.add(assertedJenaOWLModel.getOWLThingClass());
		JenaCreator jenaCreator = new JenaCreator(assertedJenaOWLModel, false,
				true, clses, null);
		OntModelSpec oms = new OntModelSpec(OntModelSpec.OWL_DL_MEM);

		
		oms.setDocumentManager(docMgr);
		OntModel inferredOntModel = jenaCreator.createOntModel(oms);
		File inferredModelTempFile = new File(INFERRED_MODEL_TEMP_FILE);

		// Save inferred knowledge as asserted in a INFERRED_MODEL_TEMP_FILE
		try {
			String ns = assertedJenaOWLModel.getNamespaceManager()
					.getDefaultNamespace();
			JenaOWLModel.save(inferredModelTempFile, inferredOntModel,
					FileUtils.langXMLAbbrev, ns);
		} catch (Exception ex) {
		}
		// read the model from INFERRED_MODEL_TEMP_FILE
		JenaOWLModel inferredJenaOWLModel = null;
        try {
            inferredJenaOWLModel = UtilsForProtegeOWL
            		.loadOWLModelFromURI(inferredModelTempFile.toURI().toString(),
            				importedFiles);
        } catch (JenaOWLModelLoadingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		inferredModelTempFile.delete();
		return inferredJenaOWLModel;
	}

	/**
     * Saves jenaOWLModel to a temporary file and then loads it again. 
     * This is needed, because if jenaOWLModel is a mapping result, then
     * such saving and loading again removes all the unneeded (internal) information from
     * the model, which was created as a result of rules execution
	 * @param jenaOWLModel
	 * @param importedFiles
	 * @return
	 */
	private JenaOWLModel saveAndLoadModel(JenaOWLModel jenaOWLModel, Vector<String> importedFiles) {
		File tempFile = new File(ASSERTED_MODEL_TEMP_FILE);
		UtilsForProtegeOWL.saveJenaOWLModel(jenaOWLModel, tempFile);
		
		String tempFileURI = MiscUtils.determineProgramBasePathURI() + "/" + ASSERTED_MODEL_TEMP_FILE;
		JenaOWLModel theSameModel = null;
        try {
            theSameModel = UtilsForProtegeOWL.loadOWLModelFromURI(tempFileURI, importedFiles);
        } catch (JenaOWLModelLoadingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		tempFile.delete();
		return theSameModel;
	}
}
