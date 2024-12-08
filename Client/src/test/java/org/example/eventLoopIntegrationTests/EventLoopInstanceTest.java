package org.example.eventLoopIntegrationTests;

import org.junit.jupiter.api.Test;
import presentation.EventLoop;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventLoopInstanceTest {

    @Test
    public void verifyCreationOfNonNullInstanceOfEventLoopTest() {
        EventLoop eventLoop = new EventLoop();
        assertNotNull(eventLoop);
    }
}
