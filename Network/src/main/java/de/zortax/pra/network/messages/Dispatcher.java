package de.zortax.pra.network.messages;//  Created by leo on 26.09.17.

import de.zortax.pra.network.command.CommandSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Dispatcher {

    private ConcurrentHashMap<String, String> constants;

    private MessageAdapter messageAdapter;
    private Logger logger;

    public Dispatcher(MessageAdapter messageAdapter) {
        this.messageAdapter = messageAdapter;
        this.constants = new ConcurrentHashMap<>();
    }

    public Dispatcher(MessageAdapter messageAdapter, Logger logger) {
        this(messageAdapter);
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
