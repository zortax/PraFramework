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

package de.zortax.pra.network.event;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.error.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {

    private ConcurrentHashMap<String, ConcurrentHashMap<EventPrio, ArrayList<Listener>>> eventListener;

    public EventManager() {
        this.eventListener = new ConcurrentHashMap<>();
    }

    public void addListener(Object listener) {

        try {
            for (Method method : listener.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    if (method.getParameterCount() == 1 && Event.class.isAssignableFrom(method.getParameters()[0].getType())) {
                        EventHandler annotation = method.getAnnotation(EventHandler.class);
                        ConcurrentHashMap<EventPrio, ArrayList<Listener>> listeners = eventListener.getOrDefault(method.getParameters()[0].getType().getName(), new ConcurrentHashMap<>());
                        ArrayList<Listener> methods = listeners.getOrDefault(annotation.prio(), new ArrayList<>());
                        method.setAccessible(true);
                        methods.add(new Listener(listener, method));
                        listeners.put(annotation.prio(), methods);
                        eventListener.put((method.getParameters()[0].getType().getName()), listeners);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    public void callEvent(Event event) {
        ConcurrentHashMap<EventPrio, ArrayList<Listener>> listeners = eventListener.getOrDefault(event.getClass().getName(), new ConcurrentHashMap<>());

        if (listeners.containsKey(EventPrio.HIGHEST))
            listeners.get(EventPrio.HIGHEST).forEach(listener -> listener.invoke(event));

        if (listeners.containsKey(EventPrio.HIGH))
            listeners.get(EventPrio.HIGHEST).forEach(listener -> listener.invoke(event));

        if (listeners.containsKey(EventPrio.NORMAL))
            listeners.get(EventPrio.NORMAL).forEach(listener -> listener.invoke(event));

        if (listeners.containsKey(EventPrio.LOW))
            listeners.get(EventPrio.LOW).forEach(listener -> listener.invoke(event));

        if (listeners.containsKey(EventPrio.LOWEST))
            listeners.get(EventPrio.LOWEST).forEach(listener -> listener.invoke(event));
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<EventPrio, ArrayList<Listener>>> getAllListeners() {
        return eventListener;
    }

    public static class Listener {

        private Object owner;
        private Method listener;

        Listener(Object owner, Method listener) {
            this.owner = owner;
            this.listener = listener;
        }

        void invoke(Event event) {
            try {
                listener.invoke(owner, event);
            } catch (Exception e) {
                ExceptionHandler.addException(e);
            }
        }

    }

}
