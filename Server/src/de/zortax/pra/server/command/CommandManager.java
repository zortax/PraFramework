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

    private class InputThread implements Runnable {

        @Override
        public void run() {
            ServerManager.logger.log(Level.INFO, "Input listener started...");
            try {
                while (true) {
                    try {
                        String[] cmd = scanner.nextLine().split(" ");
                        if (cmd.length > 0) {
                            if (commands.containsKey(cmd[0])) {
                                ServerManager.logger.log(Level.INFO, "Console issued server command " + cmd[0]);
                                Method cmdMethod = commands.get(cmd[0]);
                                if (cmdMethod.getParameterCount() == 1 && cmdMethod.getParameterTypes()[0].equals(String[].class)) {
                                    String[] args = new String[cmd.length - 1];
                                    System.arraycopy(cmd, 1, args, 0, args.length);
                                    PraCommand annotation = cmdMethod.getAnnotation(PraCommand.class);
                                    if (annotation.minArgs() > 0 && args.length < annotation.minArgs()) {
                                        ServerManager.logger.log(Level.WARNING, "Error: Not enough arguments!");
                                        ServerManager.logger.log(Level.WARNING, "Usage: " + annotation.usage());
                                        continue;
                                    }
                                    if (annotation.maxArgs() > -1 && args.length > annotation.maxArgs()) {
                                        ServerManager.logger.log(Level.WARNING, "Error: Too many arguments!");
                                        ServerManager.logger.log(Level.WARNING, "Usage: " + annotation.usage());
                                        continue;
                                    }
                                    commands.get(cmd[0]).invoke(null, (Object) args);
                                } else if (cmdMethod.getParameterCount() == 0) {
                                    commands.get(cmd[0]).invoke(null);
                                }
                            } else {
                                ServerManager.logger.log(Level.WARNING, "Command \"" + cmd[0] + "\" not found!");
                            }
                        }
                    } catch (Exception e) {
                        ExceptionHandler.addException(e);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.addException(e);
            }
        }
    }

}
