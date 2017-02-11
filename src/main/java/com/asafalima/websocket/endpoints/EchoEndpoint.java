package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/echo")
public class EchoEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoEndpoint.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private final List<Session> clientsSessions = new ArrayList<>();
    private final MessagingService messagingService;

    @Inject
    public EchoEndpoint(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(() -> clientsSessions.forEach(session -> {
            try {
                if (!session.isOpen()) {
                    clientsSessions.remove(session);
                    return;
                }

                String message = messagingService.getMessage();
                LOGGER.info("Sending message: {}, to client: {}", message, session);
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                LOGGER.error("Error while sending message to client: {}", session, e);
            }
        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduledExecutorService.shutdown();
        clientsSessions.forEach(session -> {
            try {
                if (!session.isOpen()) {
                    return;
                }

                session.close(new CloseReason(CloseCodes.SERVICE_RESTART, "Shutting down"));
            } catch (Exception e) {
                LOGGER.error("Error while closing session", e);
            }
        });
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        LOGGER.info("Socket Connected, session: {}", session);
        clientsSessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String messageToSend = message.toUpperCase();

        try {
            if (session.isOpen()) session.getBasicRemote().sendText(messageToSend);
        } catch (Exception e) {
            LOGGER.error("Failed to send message to client, message: {}, session: {}", messageToSend, session);
        }
    }

    @OnClose
    public void onWebSocketClose(Session session, CloseReason reason) {
        LOGGER.info("Socket Closed, session: {}, reason: {}", session, reason);
        clientsSessions.remove(session);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable throwable) {
        LOGGER.error("An error occurred while communicating with the client, session: {}", session, throwable);
        clientsSessions.remove(session);
    }

}
