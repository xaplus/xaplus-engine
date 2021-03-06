package org.xaplus.engine;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaplus.engine.events.*;

import java.util.concurrent.TimeUnit;

public class XAPlusUserScenarioTest extends XAPlusScenarioTest {
    static private final Logger logger = LoggerFactory.getLogger(XAPlusUserScenarioTest.class);

    @Before
    public void beforeTest() {
        createComponents();
        start();
    }

    @Test
    public void testUserCommitScenario() throws InterruptedException {
        long value = initialRequest(false, false, false);
        // Check superior
        XAPlusScenarioSuperiorFinishedEvent event1 = scenarioSuperiorFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event1);
        assertEquals(value, event1.getValue());
        assertTrue(event1.getStatus());
        // Check subordinate
        XAPlusScenarioSubordinateFinishedEvent event2 = scenarioSubordinateFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event2);
        assertEquals(value, event2.getValue());
        assertTrue(event2.getStatus());
    }

    @Test
    public void testSuperiorUserRollbackBeforeRequestScenario() throws InterruptedException {
        long value = initialRequest(true, false, false);
        // Check superior
        XAPlusScenarioSuperiorFinishedEvent event1 = scenarioSuperiorFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event1);
        assertEquals(value, event1.getValue());
        assertFalse(event1.getStatus());
    }

    @Test
    public void testSuperiorUserRollbackBeforeCommitScenario() throws InterruptedException {
        long value = initialRequest(false, true, false);
        // Check superior
        XAPlusScenarioSuperiorFinishedEvent event1 = scenarioSuperiorFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event1);
        assertEquals(value, event1.getValue());
        assertFalse(event1.getStatus());
    }

    @Test
    public void testSubordinateUserRollbackBeforeCommitScenario() throws InterruptedException {
        long value = initialRequest(false, false, true);
        // Check superior
        XAPlusScenarioSuperiorFinishedEvent event1 = scenarioSuperiorFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event1);
        assertEquals(value, event1.getValue());
        assertFalse(event1.getStatus());
        // Check subordinate
        XAPlusScenarioSubordinateFinishedEvent event2 = scenarioSubordinateFinishedEvents
                .poll(POLL_TIMIOUT_MS, TimeUnit.MILLISECONDS);
        assertNotNull(event2);
        assertEquals(value, event2.getValue());
        assertFalse(event2.getStatus());
    }
}
