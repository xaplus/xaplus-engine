package org.xaplus.engine.events.xaplus;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusXid;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusReportAbsenceXidFailedEvent extends Event<XAPlusReportAbsenceXidFailedEvent.Handler> {

    private final XAPlusXid xid;
    private final Exception exception;

    public XAPlusReportAbsenceXidFailedEvent(XAPlusXid xid, Exception exception) {
        super();
        if (xid == null) {
            throw new NullPointerException("xid is null");
        }
        if (exception == null) {
            throw new NullPointerException("exception is null");
        }
        this.xid = xid;
        this.exception = exception;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleReportAbsentXidFailed(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "=(xid=" + xid + ", exception=" + exception + ")";
    }

    public XAPlusXid getXid() {
        return xid;
    }

    public Exception getException() {
        return exception;
    }

    public interface Handler {
        void handleReportAbsentXidFailed(XAPlusReportAbsenceXidFailedEvent event) throws InterruptedException;
    }
}