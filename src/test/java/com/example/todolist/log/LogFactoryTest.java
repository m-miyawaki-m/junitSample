package com.example.todolist.log;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;

public class LogFactoryTest {

	@Test
    public void testGetLogger() {
        Logger logger = LogFactory.getLogger(LogFactoryTest.class);
        assertNotNull("Logger should not be null", logger);
        assertTrue("Logger name should match the class name", logger.getName().contains(LogFactoryTest.class.getName()));
    }

}
