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

package de.zortax.pra.network.messages.impl;// Created by leo on 27.10.17

import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.network.messages.MessageAdapter;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesAdapter implements MessageAdapter {

    private ConcurrentHashMap<String, Properties> languages;
    private Properties defaultMessages;
    private Logger logger;

    @Override
    public void initialize(Logger logger) {
        this.languages = new ConcurrentHashMap<>();
        this.defaultMessages = new Properties();
        this.logger = logger;
        this.log(Level.INFO, "Loading default messages...");
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
            log(Level.INFO, "Trying to load language \"" + lang + "\"...");
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

    private void log(Level level, String msg) {
        if (logger != null)
            logger.log(level, msg);
    }

}
