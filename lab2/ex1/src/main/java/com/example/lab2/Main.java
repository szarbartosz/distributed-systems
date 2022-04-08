package com.example.lab2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        // EX1
        // Fluent interfaces
        System.out.println(target.path("rest").path("hello").request()
                .accept(MediaType.TEXT_PLAIN).get(Response.class).toString());
        // Get plain text
        System.out.println(target.path("rest").path("hello").request()
                .accept(MediaType.TEXT_PLAIN).get(String.class));
        // Get XML
        System.out.println(target.path("rest").path("hello").request()
                .accept(MediaType.TEXT_XML).get(String.class));
        // Get HTML
        System.out.println(target.path("rest").path("hello").request()
                .accept(MediaType.TEXT_HTML).get(String.class));

        // EX2
        // Fluent interfaces
        System.out.println(target.path("rest").path("todosimple").request()
                .accept(MediaType.TEXT_XML).get(Response.class).toString());
        // Get XML
        System.out.println(target.path("rest").path("todosimple").request()
                .accept(MediaType.TEXT_XML).get(String.class));
        // Get XML for application
        System.out.println(target.path("rest").path("todosimple").request()
                .accept(MediaType.APPLICATION_XML).get(String.class));
        // Get JSON for application
        System.out.println(target.path("rest").path("todosimple").request()
                .accept(MediaType.APPLICATION_JSON).get(String.class));
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/lab2_war_exploded").build();
    }
}
