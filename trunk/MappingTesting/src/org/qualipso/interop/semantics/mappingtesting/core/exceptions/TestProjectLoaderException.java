package org.qualipso.interop.semantics.mappingtesting.core.exceptions;

/**
 * This exception is thrown, when either test project file or
 * source and target ontologies, or ontology mapping, or test cases
 * could not be loaded 
 */
public class TestProjectLoaderException extends Exception  {

    /**
     * This exception is thrown, when either test project file or
     * source and target ontologies, or ontology mapping, or test cases
     * could not be loaded 
     * @param message 
     * @param cause
     */
    public TestProjectLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
