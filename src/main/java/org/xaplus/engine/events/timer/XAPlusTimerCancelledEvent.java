package org.xaplus.engine.events.timer;

import com.crionuke.bolts.Event;
import org.xaplus.engine.XAPlusTransaction;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
public final class XAPlusTimerCancelledEvent extends Event<XAPlusTimerCancelledEvent.Handler> {

    private final XAPlusTransaction transaction;

    public XAPlusTimerCancelledEvent(XAPlusTransaction transaction) {
        super();
        if (transaction == null) {
            throw new NullPointerException("transaction is null");
        }
        this.transaction = transaction;
    }

    public XAPlusTransaction getTransaction() {
        return transaction;
    }

    @Override
    public void handle(Handler handler) throws InterruptedException {
        handler.handleTimerCancelled(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "=(transaction=" + transaction + ")";
    }

    public interface Handler {
        void handleTimerCancelled(XAPlusTimerCancelledEvent event) throws InterruptedException;
    }
}