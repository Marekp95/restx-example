package com.github.marekp95.rest.example.api;

import picocli.CommandLine;
import restx.server.WebServer;
import restx.server.simple.simple.SimpleWebServer;

import java.util.concurrent.Callable;

public class Server implements Callable<Void> {

    @CommandLine.Option(names = {"-p", "--port"}, description = "Port", defaultValue = "8080")
    private int port;
    @CommandLine.Option(names = {"--prod", "--production"}, description = "Production mode")
    private boolean productionMode;

    private Server() {
    }

    public static void main(String[] args) {
        new CommandLine(new Server()).execute(args);
    }

    @Override
    public Void call() throws Exception {
        final WebServer server = SimpleWebServer.builder()
                .setRouterPath("/api")
                .setPort(port)
                .build();

        System.setProperty("restx.mode", productionMode ? "prod" : "dev");

        server.startAndAwait();
        return null;
    }
}
