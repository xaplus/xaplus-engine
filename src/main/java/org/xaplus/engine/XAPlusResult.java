package org.xaplus.engine;

import org.xaplus.engine.exceptions.XAPlusCommitException;
import org.xaplus.engine.exceptions.XAPlusRollbackException;
import org.xaplus.engine.exceptions.XAPlusTimeoutException;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusResult {

    private final boolean status;
    private final XAPlusCommitException commitException;
    private final XAPlusRollbackException rollbackException;
    private final XAPlusTimeoutException timeoutException;

    XAPlusResult(boolean status) {
        this.status = status;
        commitException = null;
        rollbackException = null;
        timeoutException = null;
    }

    XAPlusResult(XAPlusCommitException commitException) {
        this.status = false;
        this.commitException = commitException;
        rollbackException = null;
        timeoutException = null;
    }

    XAPlusResult(XAPlusRollbackException rollbackException) {
        this.status = false;
        commitException = null;
        this.rollbackException = rollbackException;
        timeoutException = null;
    }

    XAPlusResult(XAPlusTimeoutException timeoutException) {
        this.status = false;
        commitException = null;
        rollbackException = null;
        this.timeoutException = timeoutException;
    }

    boolean get() throws XAPlusCommitException, XAPlusRollbackException, XAPlusTimeoutException {
        if (commitException != null) {
            throw commitException;
        } else if (rollbackException != null) {
            throw rollbackException;
        } else if (timeoutException != null) {
            throw timeoutException;
        } else {
            return status;
        }
    }
}
