package org.xaplus.engine.events.user;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusTransaction;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusUserCommitRequestEvent extends Event<XAPlusUserCommitRequestEvent.Handler> {

    private final XAPlusTransaction transaction;

    public XAPlusUserCommitRequestEvent(XAPlusTransaction transaction) {
        super();
        if (transaction == null) {
            throw new NullPointerException("transaction is null");
        }
        this.transaction = transaction;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleUserCommitRequest(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "=(transaction=" + transaction + ")";
    }

    public XAPlusTransaction getTransaction() {
        return transaction;
    }

    public interface Handler {
        void handleUserCommitRequest(XAPlusUserCommitRequestEvent event) throws InterruptedException;
    }
}