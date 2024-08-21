package com.example.todolist.entity;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

public class TodoTest {

    private Todo todo;

    @Before
    public void setUp() {
        todo = new Todo();
    }

    @Test
    public void testIdGetterSetter() {
        Integer id = 1;
        todo.setId(id);
        assertEquals(id, todo.getId());
    }

    @Test
    public void testTitleGetterSetter() {
        String title = "Test Title";
        todo.setTitle(title);
        assertEquals(title, todo.getTitle());
    }

    @Test
    public void testImportanceGetterSetter() {
        Integer importance = 5;
        todo.setImportance(importance);
        assertEquals(importance, todo.getImportance());
    }

    @Test
    public void testUrgencyGetterSetter() {
        Integer urgency = 3;
        todo.setUrgency(urgency);
        assertEquals(urgency, todo.getUrgency());
    }

    @Test
    public void testDeadlineGetterSetter() {
        Date deadline = Date.valueOf("2024-12-31");
        todo.setDeadline(deadline);
        assertEquals(deadline, todo.getDeadline());
    }

    @Test
    public void testDoneGetterSetter() {
        String done = "yes";
        todo.setDone(done);
        assertEquals(done, todo.getDone());
    }
}
