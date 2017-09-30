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

package de.zortax.pra.network.error;//  Created by Leonard on 03.03.2017.

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

    // TODO

    private static ConcurrentHashMap<Date, Exception> exceptions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> typeStats = new ConcurrentHashMap<>();
    private static Logger logger;

    public static void setLogger(Logger logger) {
        ExceptionHandler.logger = logger;
    }

    public static void addException(Exception e) {
        exceptions.put(new Date(), e);
        typeStats.put(e.getClass().getName(), typeStats.getOrDefault(e.getClass().getName(), 0) + 1);
        if (logger != null)
            logger.log(Level.SEVERE, e.getMessage(), e);
    }

    public static ConcurrentHashMap<Date, Exception> getExceptions() {
        return exceptions;
    }

    public static int getExceptionCount(String className) {
        return typeStats.getOrDefault(className, 0);
    }

}