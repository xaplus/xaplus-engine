package org.xaplus.engine.events.recovery;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusXid;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusRecoveredXidRolledBackEvent extends Event<XAPlusRecoveredXidRolledBackEvent.Handler> {

    private final XAPlusXid xid;
    private final String uniqueName;

    public XAPlusRecoveredXidRolledBackEvent(XAPlusXid xid, String uniqueName) {
        super();
        if (xid == null) {
            throw new NullPointerException("xid is null");
        }
        if (xid == null) {
            throw new NullPointerException("uniqueName is null");
        }
        this.xid = xid;
        this.uniqueName = uniqueName;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleRecoveredXidRolledBack(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "=(xid=" + xid +
                ", uniqueName=" + uniqueName + ")";
    }

    public XAPlusXid getXid() {
        return xid;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public interface Handler {
        void handleRecoveredXidRolledBack(XAPlusRecoveredXidRolledBackEvent event) throws InterruptedException;
    }
}