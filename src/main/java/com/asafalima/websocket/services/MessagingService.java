package com.asafalima.websocket.services;

import javax.inject.Singleton;
import java.util.Random;

/**
 * @author Asaf Alima
 */
@Singleton
public class MessagingService {

    public String getMessage() {
        int i = new Random().nextInt(10000);
        return "Random number is: " + i;
    }

}
