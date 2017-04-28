package de.zortax.pra.server.command;//  Created by Leo on 28.04.2017.

import de.zortax.pra.server.ServerManager;

import java.util.logging.Level;

public class ConsoleSender implements CommandSender {

    public static ConsoleSender instance = null;

    static {
        instance = new ConsoleSender();
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        if (ServerManager.getLogger() != null)
            ServerManager.getLogger().info(message);
    }

    @Override
    public void sendMessage(Level level, String message) {
        if (ServerManager.getLogger() != null)
            ServerManager.getLogger().log(level, message);
    }
}
