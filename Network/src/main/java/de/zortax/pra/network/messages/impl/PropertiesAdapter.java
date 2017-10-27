package de.zortax.pra.network.messages.impl;// Created by leo on 27.10.17

import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.network.messages.MessageAdapter;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PropertiesAdapter implements MessageAdapter {

    private ConcurrentHashMap<String, Properties> languages;
    private Properties defaultMessages;

    @Override
    public void initialize(Logger logger) {
        this.languages = new ConcurrentHashMap<>();
        this.defaultMessages = new Properties();
        try {
            this.defaultMessages.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/resources/i18n/messages.properties"));
        } catch (IOException e) {
            ExceptionHandler.addException(e);
        }
    }

    @Override
    public String getMessage(String key, String lang) {
        if (languages.containsKey(lang))
            return languages.get(lang).getProperty(key);
        else {
            Properties langProps = new Properties(defaultMessages);
            try {
                langProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/resources/i18n/" + lang + ".properties"));
            } catch (IOException e) {
                ExceptionHandler.addException(e);
            }
            languages.put(lang, langProps);
            return langProps.getProperty(key);
        }
    }

}
