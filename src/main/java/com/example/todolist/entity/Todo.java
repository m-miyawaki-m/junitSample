package com.example.todolist.entity;

import java.sql.Date;

import lombok.Data;

@javax.persistence.Entity
@javax.persistence.Table(name = "todo")
@Data
public class Todo {
  @javax.persistence.Id
  @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @javax.persistence.Column(name = "id")
  private Integer id;

  @javax.persistence.Column(name = "title")
  private String title;

  @javax.persistence.Column(name = "importance")
  private Integer importance;

  @javax.persistence.Column(name = "urgency")
  private Integer urgency;

  @javax.persistence.Column(name = "deadline")
  private Date deadline;

  @javax.persistence.Column(name = "done")
  private String done;
}
