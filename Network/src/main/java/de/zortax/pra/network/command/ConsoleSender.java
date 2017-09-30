package de.zortax.pra.network.command;//  Created by Leo on 28.04.2017.

import java.util.logging.Level;

/**
 * Default implementation for the console command sender
 */
public class ConsoleSender implements CommandSender {

    /**
     * The console command sender instance
     */
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
        if (CommandManager.getLogger() != null)
            CommandManager.getLogger().info(message);
    }

    @Override
    public void sendMessage(Level level, String message) {
        if (CommandManager.getLogger() != null)
            CommandManager.getLogger().log(level, message);
    }
}
