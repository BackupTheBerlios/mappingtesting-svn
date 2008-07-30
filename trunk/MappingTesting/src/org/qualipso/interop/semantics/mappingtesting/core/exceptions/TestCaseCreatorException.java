package org.qualipso.interop.semantics.mappingtesting.core.exceptions;

/**
 *  This exception is thrown, when a test case cannot be created. 
 */
public class TestCaseCreatorException extends Exception {
    
    /**
     * This exception is thrown, when a test case cannot be created. 
     * @param message 
     * @param cause
     */
    public TestCaseCreatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
