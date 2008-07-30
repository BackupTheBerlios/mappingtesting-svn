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

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

/**
 * This class represents a data structure, which contains information about
 * (source or target) ontologies.
 * 
 */
public class Ontology {

    private JenaOWLModel jenaOWLModel;
    private TestProject testProject;

    /**
     * This path defines the ontology in a test project file. The path can
     * either be a local file path, relative to the test project file path, or a
     * URL, under which ontology is available online.
     */
    private String pathInTestProjectFile;

    public Ontology(TestProject tp, String path) {
        this.testProject = tp;
        this.pathInTestProjectFile = path;
    }

    // public void setRelativePath(String path, boolean updateOWLModel) {
    // this.pathInTestProjectFile = path;
    // // if (updateOWLModel)
    // // srcOWLModel = ProtegeOWL.createJenaOWLModelFromURI(srcOntPath);
    // }

    /**
     * Returns a string representation of a location URL of this ontology,
     * depending on whether the ontology is available locally or online.
     * 
     * @return
     */
    public String getRepositoryString() {
        if (isLocal())
            return testProject.getProjDirectoryAbsPath() + pathInTestProjectFile;
        else
            return pathInTestProjectFile;
    }

    /**
     * Returns a string representation of a location URL of this ontology,
     * depending on whether the ontology is available locally or online. The
     * difference to getRepositoryString() is that if ontology is local, then
     * URL starts with testProject.getProjDirectoryURI().
     * 
     * @return
     */
    public String getURL() {
        if (isLocal())
            return testProject.getProjDirectoryURI() + pathInTestProjectFile;
        else
            return pathInTestProjectFile;
    }

    /**
     * Returns true, if ontology is available locally, false otherwise.
     * 
     * @return
     */
    private boolean isLocal() {
        if (pathInTestProjectFile.startsWith("http"))
            return false;
        return true;
    }

    public JenaOWLModel getJenaOWLModel() {
        return jenaOWLModel;
    }

    public void setJenaOWLModel(JenaOWLModel model) {
        this.jenaOWLModel = model;
    }

    public String getDefaultNamespace() {
        return jenaOWLModel.getNamespaceManager().getDefaultNamespace();
    }
 
    /**
     * @return path, which defines the ontology in a test project file. The path can
     * either be a local file path, relative to the test project file path, or a
     * URL, under which ontology is available online.
     */
    public String getPathInTestProjectFile() {
        return pathInTestProjectFile;
    }


    //	public void setPathInProjectFile(String path) {
    //		this.pathInTestProjectFile = path;		
    //	}

}
