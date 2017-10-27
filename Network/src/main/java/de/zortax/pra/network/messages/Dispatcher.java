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

package de.zortax.pra.network.messages;//  Created by leo on 26.09.17.

import de.zortax.pra.network.command.CommandSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dispatcher {

    private ConcurrentHashMap<String, String> constants;

    private MessageAdapter messageAdapter;
    private Logger logger;

    public Dispatcher(MessageAdapter messageAdapter) {
        this(messageAdapter, null);
    }

    public Dispatcher(MessageAdapter messageAdapter, Logger logger) {
        this.messageAdapter = messageAdapter;
        this.constants = new ConcurrentHashMap<>();
        this.messageAdapter.initialize(logger);
        this.logger = logger;
    }

    public void addConstant(String replaceCode, String value) {
        constants.put("%" + replaceCode, value);
    }

    public void removeConstant(String replaceCode) {
        replaceCode = "%" + replaceCode;
        if (constants.containsKey(replaceCode))
            constants.remove(replaceCode);
    }

    public String getMessage(String lang, String key, Object... vals) {
        String msg = messageAdapter.getMessage(key, lang);
        if (msg == null) {
            if (logger != null)
                logger.log(Level.WARNING, "The message adapter returned null for key \"" + key + "\" on language code \"" + lang + "\"!");
            return "null";
        }
        for (Object val : vals) {
            if (int.class.isInstance(val))
                msg = msg.replaceFirst("%int", String.valueOf((int) val));
            if (double.class.isInstance(val))
                msg = msg.replaceFirst("%double", String.valueOf((double) val));
            if (val instanceof String)
                msg = msg.replaceFirst("%s", (String) val);
            else
                msg = msg.replaceFirst("%o", val.toString());
        }
        for (Map.Entry<String, String> entry : constants.entrySet()) {
            while (msg.contains(entry.getKey()))
                msg = msg.replaceFirst(entry.getKey(), entry.getValue());
        }
        return msg;
    }

    public String getMessage(CommandSender sender, String key, Object... vals) {
        return getMessage(sender.getLanguage(), key, vals);
    }

    public void sendMessage(CommandSender sender, String key, Object... vals) {
        sender.sendMessage(getMessage(sender.getLanguage(), key, vals));
    }

}
