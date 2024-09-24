package com.example.todolist.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.example.todolist.service.LogService;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LogServiceTest2 {

    @Mock
    private Logger mockLogger;

    @InjectMocks
    private LogService logService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Unsafeクラスを使って、static finalフィールドを書き換え
        Field loggerField = LogService.class.getDeclaredField("logger");
        loggerField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(loggerField, loggerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // Unsafeを使って、loggerフィールドを書き換える
        Unsafe unsafe = getUnsafeInstance();
        unsafe.putObject(unsafe.staticFieldBase(loggerField), unsafe.staticFieldOffset(loggerField), mockLogger);
    }

    private Unsafe getUnsafeInstance() throws Exception {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        return (Unsafe) theUnsafeField.get(null);
    }

    @Test
    public void testDoSomething_debugEnabled() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // isDebugEnabled() が true を返すようにモック
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        // リフレクションでloggerフィールドを取得し、モックが適用されているか確認
        Field loggerField = LogService.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        Logger actualLogger = (Logger) loggerField.get(logService);
        System.out.println("Logger instance in LogService: " + actualLogger);
        System.out.println("Is mockLogger: " + (actualLogger == mockLogger));
        System.out.println(actualLogger.isDebugEnabled());
        
        logService.doSomething();

        verify(mockLogger).debug("This is an info message");
        verify(mockLogger, never()).debug("This is not info message");
    }

    @Test
    public void testDoSomething_debugDisabled() {
        // isDebugEnabled() が false を返すようにモック
        when(mockLogger.isDebugEnabled()).thenReturn(false);
        System.out.println("ケース０２：" + mockLogger.isDebugEnabled());

        
        logService.doSomething();

        // "This is not info message" が呼び出されていることを確認
        verify(mockLogger).debug("This is not info message");
        // "This is an info message" が呼び出されていないことを確認
        verify(mockLogger, never()).debug("This is an info message");
    }
}
