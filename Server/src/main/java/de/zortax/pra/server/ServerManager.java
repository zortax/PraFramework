/*

    PraFramework - A simple TCP-Networking framework for Java
    Copyright (C) 2017  Zortax

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package de.zortax.pra.server;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.network.event.EventManager;
import de.zortax.pra.network.command.CommandManager;
import de.zortax.pra.server.net.PraServer;
import de.zortax.pra.server.plugin.PluginLoader;

import java.util.logging.Logger;

public class ServerManager {

    public static Logger logger;
    public static PraServer server;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static PluginLoader pluginLoader;
    public static boolean debug = false;
    public static int debugLevel = 10;
    public static boolean debugWithLogger = true;

    /**
     * Creates a new ServerManager instance
     * @param bindingIP the IP to bind on
     * @param port the port to bind on
     */
    public ServerManager(String bindingIP, int port) {
        logger = Logger.getLogger("PraServer");
        ExceptionHandler.setLogger(logger);
        commandManager = new CommandManager(logger, eventManager);
        eventManager = new EventManager();
        server = new PraServer(bindingIP, port);
    }

    /**
     * Creates a new ServerManager instance
     * @param bindingIP the IP to bind on
     * @param port the port to bind on
     * @param useCommandSystem if this server manager should use the command system
     * @param usePluginSystem if this server manager should use the plugin system
     */
    public ServerManager(String bindingIP, int port, boolean useCommandSystem, boolean usePluginSystem) {
        logger = Logger.getLogger("PraServer");
        if (useCommandSystem)
            commandManager = new CommandManager(logger, eventManager);
        eventManager = new EventManager();
        if (usePluginSystem)
            pluginLoader = new PluginLoader();
        server = new PraServer(bindingIP, port);
    }

    /**
     * Starts the server
     */
    public void startServer() {
        server.start();
    }

    /**
     * Load all plugins
     * @param pluginPath the path to the folder the plugins are in
     */
    public void loadPlugins(String pluginPath) {
        pluginLoader.loadPlugins(pluginPath);
    }

    /**
     * @return the Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @return the PraServer instance
     */
    public static PraServer getServer() {
        return server;
    }

    /**
     * @return the CommandManager instance
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * @return the EventManager instance
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * @return the PluginLoader instance
     */
    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    /**
     * En-/Disables the debug logging mode
     * @param enabled true to enable, false to disable
     */
    public static void setDebugMode(boolean enabled) {
        debug = enabled;
    }

    /**
     * Sets the debug level
     * @param level the debug level
     */
    public static void setDebugLevel(int level) {
        debugLevel = level;
    }

    /**
     * Sets if the debug mode should use the java logger
     * @param logger true for yes, false for no
     */
    public static void setDebugWithLogger(boolean logger) {
        debugWithLogger = logger;
    }

    /**
     * Sends a debug message
     * @param message the message to send
     * @param level the debug logging level
     */
    public static void debug(String message, int level) {
        if (debug) {
            if (debugLevel >= level) {
                if (debugWithLogger)
                    logger.info(message);
                else
                    System.out.println(message);
            }
        }
    }

}
