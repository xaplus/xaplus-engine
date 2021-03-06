package org.xaplus.engine.events;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusXid;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusScenarioSubordinateRequestEvent extends Event<XAPlusScenarioSubordinateRequestEvent.Handler> {

    private final XAPlusXid xid;
    private final long value;
    private final boolean beforeCommitException;

    public XAPlusScenarioSubordinateRequestEvent(XAPlusXid xid, long value, boolean beforeCommitException) {
        super();
        if (xid == null) {
            throw new NullPointerException("xid is null");
        }
        this.xid = xid;
        this.value = value;
        this.beforeCommitException = beforeCommitException;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleScenarioSubordinateRequest(this);
    }

    public XAPlusXid getXid() {
        return xid;
    }

    public long getValue() {
        return value;
    }

    public boolean isBeforeCommitException() {
        return beforeCommitException;
    }

    public interface Handler {
        void handleScenarioSubordinateRequest(XAPlusScenarioSubordinateRequestEvent event) throws InterruptedException;
    }
}