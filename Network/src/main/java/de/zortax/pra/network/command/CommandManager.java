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

package de.zortax.pra.network.command;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.network.event.EventManager;
import de.zortax.pra.network.event.CommandExecutedEvent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager {

    private Scanner scanner;
    private HashMap<String, Method> commands;
    private InputThread inputThread;
    private Thread inputThreadObject;

    private Logger logger;
    private EventManager eventManager;

    public static CommandManager instance;

    /**
     * Creates a new command manager
     */
    public CommandManager(Logger logger, EventManager eventManager) {

        if (instance != null)
            throw new IllegalStateException("There is already a command manager instance running!");

        instance = this;

        this.logger = logger;
        this.eventManager = eventManager;

        logger.log(Level.INFO, "Initializing command manager...");
        scanner = new Scanner(System.in);
        commands = new HashMap<>();
        inputThread = new InputThread();
        inputThreadObject = new Thread(inputThread);

        addCommand(HelpCommand.class);
        addCommand(ExceptionCommand.class);
    }

    /**
     * Starts the thread that reads the commands from the console prompt
     */
    public void startInputThread() {
        if (inputThreadObject.isAlive())
            throw new IllegalStateException("InputThread is already running!");
        else
            inputThreadObject.start();
    }

    /**
     * Adds a new command executor
     * @param command the class the onCommand method is in
     */
    public void addCommand(Class command) {
        Method[] methods = command.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PraCommand.class)) {
                commands.put(method.getAnnotation(PraCommand.class).name().toLowerCase(), method);
            }
        }
    }

    /**
     * Returns all command executors
     * @return all onCommand methods mapped to their command's name
     */
    public HashMap<String, Method> getCommands() {
        return commands;
    }

    /**
     * Returns the input thread
     * @return the input thread's Thread instance
     */
    public Thread getInputThread() {
        return inputThreadObject;
    }

    /**
     * Executes a command with args
     * @param sender the command sender
     * @param command the command with arguments
     */
    public void execute(CommandSender sender, String command) {
        try {
            String[] cmd = command.split(" ");
            if (cmd.length > 0) {
                Method cmdMethod = getCommand(cmd[0]);
                if (cmdMethod != null) {
                    logger.log(Level.INFO, sender.getName() + " issued server command " + cmd[0]);
                    if (cmdMethod.getParameterCount() == 2 && cmdMethod.getParameterTypes()[0].equals(CommandSender.class) && cmdMethod.getParameterTypes()[1].equals(String[].class)) {
                        String[] args = new String[cmd.length - 1];
                        System.arraycopy(cmd, 1, args, 0, args.length);
                        PraCommand annotation = cmdMethod.getAnnotation(PraCommand.class);
                        if (sender.hasPermission(annotation.permission())) {
                            if (annotation.minArgs() > 0 && args.length < annotation.minArgs()) {
                                sender.sendMessage(Level.WARNING, "Error: Not enough arguments!");
                                sender.sendMessage(Level.WARNING, "Usage: " + annotation.usage());
                                return;
                            }
                            if (annotation.maxArgs() > -1 && args.length > annotation.maxArgs()) {
                                sender.sendMessage(Level.WARNING, "Error: Too many arguments!");
                                sender.sendMessage(Level.WARNING, "Usage: " + annotation.usage());
                                return;
                            }
                            CommandExecutedEvent event = new CommandExecutedEvent(cmd, commands.get(cmd[0]), annotation);
                            eventManager.callEvent(event);
                            if (!event.isCancelled())
                                commands.get(cmd[0]).invoke(null, sender, args);
                        } else {
                            sender.sendMessage(Level.WARNING, "You do not have permissions to perform this command!");
                        }
                    } else if (cmdMethod.getParameterCount() == 1) {
                        CommandExecutedEvent event = new CommandExecutedEvent(cmd, commands.get(cmd[0]), cmdMethod.getAnnotation(PraCommand.class));
                        eventManager.callEvent(event);
                        if (!event.isCancelled())
                            commands.get(cmd[0]).invoke(null, sender);
                    } else if (cmdMethod.getParameterCount() == 0) {
                        CommandExecutedEvent event = new CommandExecutedEvent(cmd, commands.get(cmd[0]), cmdMethod.getAnnotation(PraCommand.class));
                        eventManager.callEvent(event);
                        if (!event.isCancelled())
                            commands.get(cmd[0]).invoke(null);
                    }
                } else {
                    sender.sendMessage(Level.WARNING, "Command \"" + cmd[0] + "\" not found!");
                }
            }
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    private Method getCommand(String name) {
        if (commands.containsKey(name.toLowerCase()))
            return commands.get(name.toLowerCase());
        else {
            for (Method m : commands.values()) {
                for (String alias : m.getAnnotation(PraCommand.class).aliases()) {
                    if (name.equalsIgnoreCase(alias))
                        return m;
                }
            }
            return null;
        }
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static EventManager getEventManager() {
        return instance.eventManager;
    }

    private class InputThread implements Runnable {

        @Override
        public void run() {
            logger.log(Level.INFO, "Input listener started...");
            try {
                while (true)
                    execute(ConsoleSender.instance, scanner.nextLine());
            } catch (Exception e) {
                ExceptionHandler.addException(e);
            }
        }
    }

}
