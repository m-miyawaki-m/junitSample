package com.example.todolist.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LogServiceTest {

    @Mock
    private Logger mockLogger;

    @InjectMocks
    private LogService logService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // リフレクションを使って、static finalフィールドを書き換え
        Field loggerField = LogService.class.getDeclaredField("logger");
        loggerField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(loggerField, loggerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // loggerフィールドを書き換える
        loggerField.set(null, mockLogger);
    }

    @Test
    public void testDoSomething_debugEnabled() {
        // isDebugEnabled() が true を返すようにモック
        when(mockLogger.isDebugEnabled()).thenReturn(true);

        logService.doSomething();

        verify(mockLogger).debug("This is an info message");
        verify(mockLogger, never()).debug("This is not info message");
    }

    @Test
    public void testDoSomething_debugDisabled() {
        // isDebugEnabled() が false を返すようにモック
        when(mockLogger.isDebugEnabled()).thenReturn(false);

        logService.doSomething();

        // "This is an info message" が呼び出されていないことを確認
        verify(mockLogger, never()).debug("This is an info message");
        verify(mockLogger, never()).debug("This is not info message");
    }
}

