package com.asafalima.websocket.di;

import com.google.inject.Injector;

import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @author Asaf Alima
 */
public class GuiceConfigurator extends Configurator {

    private final Injector injector;

    public GuiceConfigurator(Injector injector) {
        this.injector = injector;
    }

    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance(endpointClass);
    }

}
