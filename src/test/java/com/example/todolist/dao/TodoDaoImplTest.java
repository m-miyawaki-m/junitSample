package com.example.todolist.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoQuery;

public class TodoDaoImplTest {

    @Mock
    private javax.persistence.EntityManager entityManager;

    @InjectMocks
    private TodoDaoImpl todoDaoImpl;

    @Mock
    private javax.persistence.TypedQuery query;

    @Mock
    private javax.persistence.TypedQuery<Todo> typedQuery;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByJPQL() {
        TodoQuery todoQuery = new TodoQuery();
        todoQuery.setTitle("Test Title");
        todoQuery.setImportance(5);
        todoQuery.setUrgency(3);
        todoQuery.setDeadlineFrom("2024-01-01");
        todoQuery.setDeadlineTo("2024-12-31");
        todoQuery.setDone("Y");

        Todo todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Title");

        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(todo));

        List<Todo> result = todoDaoImpl.findByJPQL(todoQuery);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
    }

    @Test
    public void testFindByCriteria() {
        TodoQuery todoQuery = new TodoQuery();
        todoQuery.setTitle("Test Title");
        todoQuery.setImportance(5);
        todoQuery.setUrgency(3);
        todoQuery.setDeadlineFrom("2024-01-01");
        todoQuery.setDeadlineTo("2024-12-31");
        todoQuery.setDone("Y");

        Todo todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test Title");

        Pageable pageable = PageRequest.of(0, 10);

        when(entityManager.getCriteriaBuilder()).thenReturn(entityManager.getCriteriaBuilder());
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(todo));
        when(typedQuery.setFirstResult(any(Integer.class))).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(any(Integer.class))).thenReturn(typedQuery);

        Page<Todo> result = todoDaoImpl.findByCriteria(todoQuery, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Title", result.getContent().get(0).getTitle());
    }
}
