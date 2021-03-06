package org.xaplus.engine.exceptions;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public class XAPlusRollbackException extends Exception {

    public XAPlusRollbackException() {
        super();
    }

    public XAPlusRollbackException(String message) {
        super(message);
    }
}
