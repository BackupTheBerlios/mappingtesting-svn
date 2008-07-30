package org.qualipso.interop.semantics.mappingtesting.core.exceptions;

/**
 * This exception is thrown, when JenaOWLModel cannot be loaded. 
 */
public class JenaOWLModelLoadingException extends Exception  {

    /**
     * This exception is thrown, when JenaOWLModel cannot be loaded. 
     * @param message 
     * @param cause
     */
    public JenaOWLModelLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
