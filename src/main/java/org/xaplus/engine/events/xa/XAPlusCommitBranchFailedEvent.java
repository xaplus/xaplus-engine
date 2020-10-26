package org.xaplus.engine.events.xa;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusXid;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusCommitBranchFailedEvent extends Event<XAPlusCommitBranchFailedEvent.Handler> {

    private final XAPlusXid xid;
    private final XAPlusXid branchXid;
    private final Exception exception;

    public XAPlusCommitBranchFailedEvent(XAPlusXid xid, XAPlusXid branchXid, Exception exception) {
        super();
        if (xid == null) {
            throw new NullPointerException("xid is null");
        }
        if (branchXid == null) {
            throw new NullPointerException("branchXid is null");
        }
        if (exception == null) {
            throw new NullPointerException("exception is null");
        }
        this.xid = xid;
        this.branchXid = branchXid;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "=(xid=" + xid + ", branchXid=" + branchXid +
                ", exception=" + exception + ")";
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleCommitBranchFailed(this);
    }

    public XAPlusXid getXid() {
        return xid;
    }

    public XAPlusXid getBranchXid() {
        return branchXid;
    }

    public Exception getException() {
        return exception;
    }

    public interface Handler {
        void handleCommitBranchFailed(XAPlusCommitBranchFailedEvent event) throws InterruptedException;
    }
}