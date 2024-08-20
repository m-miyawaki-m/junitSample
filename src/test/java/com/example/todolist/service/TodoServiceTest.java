package com.example.todolist.service;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;

public class TodoServiceTest {

    private TodoService todoService;
    private BindingResult bindingResult;

    @Before
    public void setUp() {
        todoService = new TodoService();
        bindingResult = new MapBindingResult(new HashMap<>(), "todoData");
    }

    // 正常系テスト: 正しい入力データの場合
    @Test
    public void testIsValidTodoData_validData() {
        TodoData todoData = new TodoData();
        todoData.setTitle("タスクのタイトル");
        todoData.setDeadline(LocalDate.now().plusDays(1).toString());

        boolean result = todoService.isValid(todoData, bindingResult, true);

        assertTrue(result);
        assertTrue(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていないことを確認
    }

    // 異常系テスト: 件名が全角スペースのみ
    @Test
    public void testIsValidTodoData_titleAllDoubleSpace() {
        TodoData todoData = new TodoData();
        todoData.setTitle("　　");
        todoData.setDeadline(LocalDate.now().plusDays(1).toString());

        boolean result = todoService.isValid(todoData, bindingResult, true);

        assertFalse(result);
        assertFalse(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていることを確認

        FieldError error = (FieldError) bindingResult.getAllErrors().get(0);
        assertEquals("件名が全角スペースです", error.getDefaultMessage());
        assertEquals("todoData", error.getObjectName());
    }

    // 異常系テスト: 過去の日付
    @Test
    public void testIsValidTodoData_pastDate() {
        TodoData todoData = new TodoData();
        todoData.setTitle("タスクのタイトル");
        todoData.setDeadline(LocalDate.now().minusDays(1).toString());

        boolean result = todoService.isValid(todoData, bindingResult, true);

        assertFalse(result);
        assertFalse(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていることを確認

        FieldError error = (FieldError) bindingResult.getAllErrors().get(0);
        assertEquals("期限を設定するときは今日以降にしてください", error.getDefaultMessage());
        assertEquals("todoData", error.getObjectName());
    }

    // 異常系テスト: 無効な日付形式
    @Test
    public void testIsValidTodoData_invalidDateFormat() {
        TodoData todoData = new TodoData();
        todoData.setTitle("タスクのタイトル");
        todoData.setDeadline("invalid-date");

        boolean result = todoService.isValid(todoData, bindingResult, true);

        assertFalse(result);
        assertFalse(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていることを確認

        FieldError error = (FieldError) bindingResult.getAllErrors().get(0);
        assertEquals("期限を設定するときはyyyy-mm-dd形式で入力してください", error.getDefaultMessage());
        assertEquals("todoData", error.getObjectName());
    }

    // 正常系テスト: 正しいクエリデータの場合
    @Test
    public void testIsValidTodoQuery_validData() {
        TodoQuery todoQuery = new TodoQuery();
        todoQuery.setDeadlineFrom(LocalDate.now().toString());
        todoQuery.setDeadlineTo(LocalDate.now().plusDays(1).toString());

        bindingResult = new MapBindingResult(new HashMap<>(), "todoQuery");

        boolean result = todoService.isValid(todoQuery, bindingResult);
        assertTrue(result);
        assertTrue(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていないことを確認
    }

    // 異常系テスト: クエリの開始日が無効な形式
    @Test
    public void testIsValidTodoQuery_invalidDateFormatFrom() {
        TodoQuery todoQuery = new TodoQuery();
        todoQuery.setDeadlineFrom("invalid-date");
        todoQuery.setDeadlineTo(LocalDate.now().plusDays(1).toString());

        bindingResult = new MapBindingResult(new HashMap<>(), "todoQuery");

        boolean result = todoService.isValid(todoQuery, bindingResult);

        assertFalse(result);
        assertFalse(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていることを確認

        FieldError error = (FieldError) bindingResult.getAllErrors().get(0);
        assertEquals("期限：開始を入力するときはyyyy-mm-dd形式で入力してください", error.getDefaultMessage());
        assertEquals("todoQuery", error.getObjectName());
    }

    // 異常系テスト: クエリの終了日が無効な形式
    @Test
    public void testIsValidTodoQuery_invalidDateFormatTo() {
        TodoQuery todoQuery = new TodoQuery();
        todoQuery.setDeadlineFrom(LocalDate.now().toString());
        todoQuery.setDeadlineTo("invalid-date");

        bindingResult = new MapBindingResult(new HashMap<>(), "todoQuery");

        boolean result = todoService.isValid(todoQuery, bindingResult);

        assertFalse(result);
        assertFalse(bindingResult.getAllErrors().isEmpty());  // エラーが追加されていることを確認

        FieldError error = (FieldError) bindingResult.getAllErrors().get(0);
        assertEquals("期限：終了を入力するときはyyyy-mm-dd形式で入力してください", error.getDefaultMessage());
        assertEquals("todoQuery", error.getObjectName());
    }
}
