package com.github.marekp95.rest.example.api;

import restx.server.WebServer;
import restx.server.simple.simple.SimpleWebServer;

import java.util.Optional;

public class Server {

    public static void main(String[] args) throws Exception {
        final int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
        final WebServer server = SimpleWebServer.builder()
                .setRouterPath("/api")
                .setPort(port)
                .build();

        System.setProperty("restx.mode", System.getProperty("restx.mode", "dev"));

        server.startAndAwait();
    }
}
