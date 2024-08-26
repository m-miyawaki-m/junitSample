package com.example.todolist.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFactory {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}