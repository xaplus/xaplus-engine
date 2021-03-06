package org.xaplus.engine;

import com.crionuke.bolts.Bolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaplus.engine.events.rollback.XAPlusRollbackDoneEvent;
import org.xaplus.engine.events.rollback.XAPlusRollbackFailedEvent;
import org.xaplus.engine.events.timer.XAPlusTransactionTimedOutEvent;
import org.xaplus.engine.events.tm.XAPlusTransactionClosedEvent;
import org.xaplus.engine.events.twopc.XAPlus2pcDoneEvent;
import org.xaplus.engine.events.twopc.XAPlus2pcFailedEvent;
import org.xaplus.engine.events.user.XAPlusUserCommitRequestEvent;
import org.xaplus.engine.events.user.XAPlusUserRollbackRequestEvent;
import org.xaplus.engine.exceptions.XAPlusCommitException;
import org.xaplus.engine.exceptions.XAPlusRollbackException;
import org.xaplus.engine.exceptions.XAPlusTimeoutException;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
class XAPlusManagerService extends Bolt implements
        XAPlusUserCommitRequestEvent.Handler,
        XAPlusUserRollbackRequestEvent.Handler,
        XAPlus2pcDoneEvent.Handler,
        XAPlus2pcFailedEvent.Handler,
        XAPlusRollbackDoneEvent.Handler,
        XAPlusRollbackFailedEvent.Handler,
        XAPlusTransactionTimedOutEvent.Handler {
    static private final Logger logger = LoggerFactory.getLogger(XAPlusManagerService.class);

    private final XAPlusThreadPool threadPool;
    private final XAPlusDispatcher dispatcher;
    private final XAPlusResources resources;
    private final XAPlusTracker tracker;

    XAPlusManagerService(XAPlusProperties properties, XAPlusThreadPool threadPool, XAPlusDispatcher dispatcher,
                         XAPlusResources resources) {
        super(properties.getServerId() + "-manager", properties.getQueueSize());
        this.threadPool = threadPool;
        this.dispatcher = dispatcher;
        this.resources = resources;
        tracker = new XAPlusTracker();
    }

    @Override
    public void handleUserCommitRequest(XAPlusUserCommitRequestEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusTransaction transaction = event.getTransaction();
        if (tracker.track(transaction)) {
            if (logger.isInfoEnabled()) {
                logger.info("User start 2pc protocol, {}", transaction);
            }
        }
    }

    @Override
    public void handleUserRollbackRequest(XAPlusUserRollbackRequestEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusTransaction transaction = event.getTransaction();
        if (tracker.track(transaction)) {
            if (logger.isInfoEnabled()) {
                logger.info("User start rollback protocol, {}", transaction);
            }
        }
    }

    @Override
    public void handle2pcDone(XAPlus2pcDoneEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusXid xid = event.getTransaction().getXid();
        if (tracker.contains(xid)) {
            XAPlusTransaction transaction = tracker.remove(xid);
            if (logger.isInfoEnabled()) {
                logger.info("Transaction done, {}", transaction);
            }
            transaction.close();
            dispatcher.dispatch(new XAPlusTransactionClosedEvent(transaction));
            transaction.getFuture().put(new XAPlusResult(true));
        }
    }

    @Override
    public void handle2pcFailed(XAPlus2pcFailedEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusXid xid = event.getTransaction().getXid();
        if (tracker.contains(xid)) {
            XAPlusTransaction transaction = tracker.remove(xid);
            if (logger.isInfoEnabled()) {
                logger.info("Transaction 2pc failed, {}", transaction);
            }
            transaction.close();
            dispatcher.dispatch(new XAPlusTransactionClosedEvent(transaction));
            transaction.getFuture().put(new XAPlusResult(new XAPlusCommitException("2pc commit exception")));
        }
    }

    @Override
    public void handleRollbackDone(XAPlusRollbackDoneEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusXid xid = event.getTransaction().getXid();
        if (tracker.contains(xid)) {
            XAPlusTransaction transaction = tracker.remove(xid);
            if (logger.isInfoEnabled()) {
                logger.info("Transaction rolled back, {}", transaction);
            }
            transaction.close();
            dispatcher.dispatch(new XAPlusTransactionClosedEvent(transaction));
            transaction.getFuture().put(new XAPlusResult(false));
        }
    }

    @Override
    public void handleRollbackFailed(XAPlusRollbackFailedEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusXid xid = event.getTransaction().getXid();
        if (tracker.contains(xid)) {
            XAPlusTransaction transaction = tracker.remove(xid);
            if (logger.isInfoEnabled()) {
                logger.info("Transaction rollback failed, {}", transaction);
            }
            transaction.close();
            dispatcher.dispatch(new XAPlusTransactionClosedEvent(transaction));
            transaction.getFuture().put(new XAPlusResult(new XAPlusRollbackException("rollback exception")));
        }
    }

    @Override
    public void handleTransactionTimedOut(XAPlusTransactionTimedOutEvent event) throws InterruptedException {
        if (logger.isTraceEnabled()) {
            logger.trace("Handle {}", event);
        }
        XAPlusXid xid = event.getTransaction().getXid();
        if (tracker.contains(xid)) {
            XAPlusTransaction transaction = tracker.remove(xid);
            if (logger.isInfoEnabled()) {
                logger.info("Transaction timed out, {}", transaction);
            }
            transaction.close();
            dispatcher.dispatch(new XAPlusTransactionClosedEvent(transaction));
            transaction.getFuture().put(new XAPlusResult(new XAPlusTimeoutException("timeout exception")));
        }
    }

    void postConstruct() {
        threadPool.execute(this);
        dispatcher.subscribe(this, XAPlusUserCommitRequestEvent.class);
        dispatcher.subscribe(this, XAPlusUserRollbackRequestEvent.class);
        dispatcher.subscribe(this, XAPlus2pcDoneEvent.class);
        dispatcher.subscribe(this, XAPlus2pcFailedEvent.class);
        dispatcher.subscribe(this, XAPlusRollbackDoneEvent.class);
        dispatcher.subscribe(this, XAPlusRollbackFailedEvent.class);
        dispatcher.subscribe(this, XAPlusTransactionTimedOutEvent.class);
    }
}

