package org.xaplus.engine;

import com.crionuke.bolts.Bolt;
import com.crionuke.bolts.Dispatcher;
import com.crionuke.bolts.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kirill Byvshev (k@byv.sh)
 * @since 1.0.0
 */
class XAPlusDispatcher {
    static private final Logger logger = LoggerFactory.getLogger(XAPlusDispatcher.class);

    private final Dispatcher dispatcher;

    XAPlusDispatcher() {
        dispatcher = new Dispatcher();
    }

    void subscribe(Bolt bolt, Object topic) {
        dispatcher.subscribe(bolt, topic);
    }

    boolean dispatch(Event event) throws InterruptedException {
        return dispatcher.dispatch(event);
    }
}
