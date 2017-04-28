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

package de.zortax.pra.server.command;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.server.ServerManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;

public class CommandManager {

    private Scanner scanner;
    private HashMap<String, Method> commands;
    private InputThread inputThread;
    private Thread inputThreadObject;

    public CommandManager() {

        if (ServerManager.commandManager != null)
            throw new IllegalStateException("There is already a CommandManager instance running!");
        else
            ServerManager.commandManager = this;

        ServerManager.logger.log(Level.INFO, "Initializing command manager...");
        scanner = new Scanner(System.in);
        commands = new HashMap<>();
        inputThread = new InputThread();
        inputThreadObject = new Thread(inputThread);

        addCommand(HelpCommand.class);
    }

    public void startInputThread() {
        if (inputThreadObject.isAlive())
            throw new IllegalStateException("InputThread is already running!");
        else
            inputThreadObject.start();
    }

    public void addCommand(Class command) {
        Method[] methods = command.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PraCommand.class)) {
                commands.put(method.getAnnotation(PraCommand.class).name(), method);
            }
        }
    }

    public HashMap<String, Method> getCommands() {
        return commands;
    }

    public Thread getInputThread() {
        return inputThreadObject;
    }

    public void execute(CommandSender sender, String command) {
        try {
            String[] cmd = command.split(" ");
            if (cmd.length > 0) {
                if (commands.containsKey(cmd[0])) {
                    ServerManager.logger.log(Level.INFO, sender.getName() + " issued server command " + cmd[0]);
                    Method cmdMethod = commands.get(cmd[0]);
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
                            commands.get(cmd[0]).invoke(null, sender, args);
                        } else {
                            sender.sendMessage(Level.WARNING, "You do not have permissions to perform this command!");
                        }
                    } else if (cmdMethod.getParameterCount() == 1)
                        commands.get(cmd[0]).invoke(null, sender);
                    else if (cmdMethod.getParameterCount() == 0)
                        commands.get(cmd[0]).invoke(null);
                } else {
                    sender.sendMessage(Level.WARNING, "Command \"" + cmd[0] + "\" not found!");
                }
            }
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    private class InputThread implements Runnable {

        @Override
        public void run() {
            ServerManager.logger.log(Level.INFO, "Input listener started...");
            try {
                while (true)
                    execute(ConsoleSender.instance, scanner.nextLine());
            } catch (Exception e) {
                ExceptionHandler.addException(e);
            }
        }
    }

}
