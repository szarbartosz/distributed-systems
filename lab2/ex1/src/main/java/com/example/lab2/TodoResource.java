package com.example.lab2;

import com.example.lab2.model.Todo;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBElement;

public class TodoResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String id;

    public TodoResource(UriInfo uriInfo, Request request, String id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Todo getTodo() {
        Todo todo = TodoDao.getModel().get(id);
        return todo;
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public Todo getTodoHTML() {
        Todo todo = TodoDao.getModel().get(id);
        return todo;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response putTodo(JAXBElement<Todo> todo) {
        Todo c = todo.getValue();
        return putAndGetResponse(c);
    }

    @DELETE
    public void deleteTodo() {
        TodoDao.getModel().remove(id);
    }

    private Response putAndGetResponse(Todo todo) {
        Response response;
        if (TodoDao.getModel().containsKey(todo.getId())) {
            response = Response.noContent().build();
        } else {
            response = Response.created(uriInfo.getAbsolutePath()).build();
        }
        TodoDao.getModel().put(todo.getId(), todo);
        return response;
    }
}
