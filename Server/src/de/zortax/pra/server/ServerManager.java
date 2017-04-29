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

import de.zortax.pra.network.event.EventManager;
import de.zortax.pra.server.command.CommandManager;
import de.zortax.pra.server.net.PraServer;
import de.zortax.pra.server.plugin.PluginLoader;

import java.util.logging.Logger;

public class ServerManager {

    public static Logger logger;
    public static PraServer server;
    public static CommandManager commandManager;
    public static EventManager eventManager;
    public static PluginLoader pluginLoader;
    public static boolean debug = true;
    public static int debugLevel = 10;
    public static boolean debugWithLogger = false;

    public ServerManager(String bindingIP, int port) {
        logger = Logger.getLogger("PraServer");
        commandManager = new CommandManager();
        eventManager = new EventManager();
        server = new PraServer(bindingIP, port);
    }

    public ServerManager(String bindingIP, int port, boolean useCommandSystem, boolean usePluginSystem) {
        logger = Logger.getLogger("PraServer");
        if (useCommandSystem)
            commandManager = new CommandManager();
        eventManager = new EventManager();
        if (usePluginSystem)
            pluginLoader = new PluginLoader();
        server = new PraServer(bindingIP, port);
    }

    public void startServer() {
        server.start();
    }

    public void loadPlugins(String pluginPath) {
        pluginLoader.loadPlugins(pluginPath);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PraServer getServer() {
        return server;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    public static void setDebugMode(boolean enabled) {
        debug = enabled;
    }

    public static void setDebugLevel(int level) {
        debugLevel = level;
    }

    public static void setDebugWithLogger(boolean logger) {
        debugWithLogger = logger;
    }

    public static void debug(String message, int level) {
        if (debugLevel >= level) {
            if (debugWithLogger)
                logger.info(message);
            else
                System.out.println(message);
        }
    }

}
