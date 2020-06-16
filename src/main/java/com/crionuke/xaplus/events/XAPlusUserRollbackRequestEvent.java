package com.crionuke.xaplus.events;

import com.crionuke.bolts.Event;
import com.crionuke.xaplus.XAPlusTransaction;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusUserRollbackRequestEvent extends Event<XAPlusUserRollbackRequestEvent.Handler> {

    private final XAPlusTransaction transaction;

    public XAPlusUserRollbackRequestEvent(XAPlusTransaction transaction) {
        super();
        if (transaction == null) {
            throw new NullPointerException("transaction is null");
        }
        this.transaction = transaction;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleUserRollbackRequest(this);
    }

    public XAPlusTransaction getTransaction() {
        return transaction;
    }

    public interface Handler {
        void handleUserRollbackRequest(XAPlusUserRollbackRequestEvent event) throws InterruptedException;
    }
}