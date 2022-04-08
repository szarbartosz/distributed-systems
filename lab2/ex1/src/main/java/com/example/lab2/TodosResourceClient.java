package com.example.lab2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TodosResourceClient {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        System.out.println(service.path("rest").path("todos"))
                .request(MediaType.TEXT_XML).get(String.class);
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/lab2_war_exploded").build();
    }
}
