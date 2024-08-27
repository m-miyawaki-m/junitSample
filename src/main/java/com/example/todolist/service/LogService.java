package com.example.todolist.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.example.todolist.log.LogFactory;

@Service
public class LogService {

//    private static final Logger logger = LogFactory.getLogger(LogService.class);
    private static final Logger logger = LogFactory.getLogger(LogService.class);
    
    public void doSomething() {
//    	if(this.logger.isDebugEnabled()) {
//	        logger.debug("This is an info message");
//    	}
    	
    	logger.debug("This is an info message");
    	System.out.println("log is not debug mode");
    }
}