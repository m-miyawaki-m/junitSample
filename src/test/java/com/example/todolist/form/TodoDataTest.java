package com.example.todolist.form;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import com.example.todolist.entity.Todo;

public class TodoDataTest {

    private TodoData todoData;

    @Before
    public void setUp() {
        todoData = new TodoData();
    }

    @Test
    public void testToEntityWithValidData() {
        // Setterを使用してデータを設定
        todoData.setId(1);
        todoData.setTitle("Test Title");
        todoData.setImportance(5);
        todoData.setUrgency(3);
        todoData.setDeadline("2024-12-31");
        todoData.setDone("yes");

        // toEntityメソッドをテスト
        Todo todo = todoData.toEntity();

        // TodoDataのフィールドが正しくTodoエンティティにコピーされているか確認
        assertEquals(todoData.getId(), todo.getId());
        assertEquals(todoData.getTitle(), todo.getTitle());
        assertEquals(todoData.getImportance(), todo.getImportance());
        assertEquals(todoData.getUrgency(), todo.getUrgency());
        assertEquals(Date.valueOf("2024-12-31"), todo.getDeadline());
        assertEquals(todoData.getDone(), todo.getDone());
    }

    @Test
    public void testToEntityWithInvalidDate() {
        // 無効な日付を設定
        todoData.setDeadline("invalid-date");

        // toEntityメソッドをテスト
        Todo todo = todoData.toEntity();

        // 無効な日付の場合、期限がnullに設定されることを確認
        assertNull(todo.getDeadline());
    }
}
