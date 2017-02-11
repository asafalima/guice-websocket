package com.asafalima.websocket;

import com.asafalima.websocket.di.WebsocketModule;
import com.asafalima.websocket.server.JettyServer;
import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsocketServer {

    private static Logger LOGGER = LoggerFactory.getLogger(WebsocketServer.class);

    public static void main(String[] args) throws Exception {
        Integer port = getPort();

        LOGGER.info("Starting jetty on port: {}", port);

        JettyServer server = new JettyServer(port, createInjector());
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.start();
    }

    private static Integer getPort() {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        return Integer.valueOf(port);
    }

    private static Injector createInjector() {
        return LifecycleInjector.builder()
                .withModules(new WebsocketModule())
                .build()
                .createInjector();
    }

}