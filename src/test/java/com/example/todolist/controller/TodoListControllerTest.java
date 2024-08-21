package com.example.todolist.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;

public class TodoListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoService todoService;
    
    @Mock
    private HttpSession session;

    @InjectMocks
    private TodoListController todoListController;

    private Page<Todo> todoPage;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this); // モックの初期化
        mockMvc = MockMvcBuilders.standaloneSetup(todoListController).build();

        Todo todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Title");
        todo.setImportance(5);
        todo.setUrgency(3);
        todo.setDeadline(java.sql.Date.valueOf("2024-12-31"));
        todo.setDone("yes");

        todoPage = new PageImpl<>(Collections.singletonList(todo));
    }

    @Test
    public void testShowTodoList() throws Exception {
        when(todoService.isValid(any(), any())).thenReturn(true);
        when(todoRepository.findAll(any(Pageable.class))).thenReturn(todoPage);

        mockMvc.perform(get("/todo")
                .param("page", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(view().name("todoList"))
            .andExpect(model().attributeExists("todoList"))
            .andExpect(model().attributeExists("todoPage"));
    }

    @Test
    public void testTodoById() throws Exception {
        Todo todo = new Todo();
        todo.setId(1);
        when(todoRepository.findById(anyInt())).thenReturn(java.util.Optional.of(todo));

        mockMvc.perform(get("/todo/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("todoForm"))
            .andExpect(model().attributeExists("todoData"))
            .andExpect(model().attribute("todoData", todo));
    }

    @Test
    public void testCreateTodo() throws Exception {
        mockMvc.perform(post("/todo/create/form"))
            .andExpect(status().isOk())
            .andExpect(view().name("todoForm"))
            .andExpect(model().attributeExists("todoData"));
    }

    @Test
    public void testCreateTodoDo() throws Exception {
        when(todoService.isValid(any(), any(), anyBoolean())).thenReturn(true);
        when(todoRepository.saveAndFlush(any(Todo.class))).thenReturn(new Todo());

        mockMvc.perform(post("/todo/create/do")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", "Test Title")
            .param("importance", "5")
            .param("urgency", "3")
            .param("deadline", "2024-12-31")
            .param("done", "yes"))
            .andExpect(redirectedUrl("/todo"));
    }

    @Test
    public void testUpdateTodo() throws Exception {
        when(todoService.isValid(any(), any(), anyBoolean())).thenReturn(true);

        mockMvc.perform(post("/todo/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("id", "1")
            .param("title", "Test Title")
            .param("importance", "5")
            .param("urgency", "3")
            .param("deadline", "2024-12-31")
            .param("done", "yes"))
            .andExpect(redirectedUrl("/todo"));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(post("/todo/delete")
            .param("id", "1"))
            .andExpect(redirectedUrl("/todo"));
    }
}
