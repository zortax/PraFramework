package de.zortax.pra.server.command;//  Created by Leo on 28.04.2017.

import java.util.logging.Level;

public interface CommandSender {

    boolean hasPermission(String permission);

    String getName();

    void sendMessage(String message);

    default void sendMessage(Level level, String message) {
        sendMessage(message);
    }

}
