package com.asafalima.websocket.di;

import com.asafalima.websocket.services.MessagingService;
import com.google.inject.AbstractModule;
import com.asafalima.websocket.endpoints.EchoEndpoint;

/**
 * @author Asaf Alima
 */
public class WebsocketModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();
        bind(MessagingService.class);
        bind(EchoEndpoint.class);
    }

}
