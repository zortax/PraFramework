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

public class HelpCommand {

    @PraCommand(name = "help", aliases = {"?", "wtf"}, usage = "help [command]", permission = "cmd.help", description = "Shows help")
    public static void onCommand(CommandSender sender, String[] args) {
        if (args == null || args.length == 0) {

            CommandManager.instance.getCommands().values().stream().filter(m -> sender.hasPermission(m.getAnnotation(PraCommand.class).permission())).forEach(m -> {

                PraCommand annotation = m.getAnnotation(PraCommand.class);

                sender.sendMessage(" ");
                sender.sendMessage(annotation.name() + " | " + annotation.usage());
                sender.sendMessage("    " + annotation.description());
                sender.sendMessage(" ");

            });

        } else if (CommandManager.instance.getCommands().containsKey(args[0])) {

            PraCommand annotation = CommandManager.instance.getCommands().get(args[0]).getAnnotation(PraCommand.class);

            sender.sendMessage(" ");
            sender.sendMessage(annotation.name() + " | " + annotation.usage());
            sender.sendMessage("    " + annotation.description());
            sender.sendMessage(" ");

        } else {

            sender.sendMessage("There is no command with the name \"" + args[0] + "\"!");

        }
    }

}
