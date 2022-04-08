package com.example.lab2;

import com.example.lab2.model.Todo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/todosimple")
public class TodoResourceSimple {
    @GET
    @Produces({MediaType.TEXT_XML})
    public Todo getHtml() {
        Todo todo = new Todo();
        todo.setSummary("This is my first todo.");
        todo.setDescription("This is my first todo.");

        return todo;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Todo sayXMLHello() {
        Todo todo = new Todo();
        todo.setSummary("This is my first todo.");
        todo.setDescription("This is my first todo.");

        return todo;
    }
}
