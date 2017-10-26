package de.zortax.pra.network.command;//  Created by Leo on 28.04.2017.

import java.util.logging.Level;

/**
 * Interface for command sender
 */
public interface CommandSender {

    /**
     * Should check if this command sender is allowed to execute a command
     * @param permission the permission that should be checked
     * @return true, if the sender has the given permission, false if not
     */
    boolean hasPermission(String permission);

    /**
     * @return the name of this specific command sender
     */
    String getName();

    /**
     * Should implement code to send this command sender a message
     * @param message the message to send
     */
    void sendMessage(String message);

    /**
     * Should implement code to send this command sender a message with a specifig log level.
     * Calls the sendMessage method if not implemented.
     * @param level the log level
     * @param message the message to send
     */
    default void sendMessage(Level level, String message) {
        sendMessage(message);
    }

    /**
     * Should return the language code for the current command sender
     * @return language code
     */
    default String getLanguage() {
        return "en";
    }

}
