package de.zortax.pra.network.event;//  Created by leo on 17.08.17.

import java.lang.reflect.Method;

import de.zortax.pra.network.event.Cancellable;
import de.zortax.pra.network.event.Event;
import de.zortax.pra.network.command.PraCommand;

public class CommandExecutedEvent implements Event, Cancellable {

    private boolean cancelled;
    private String[] command;
    private Method method;
    private PraCommand annotation;

    public CommandExecutedEvent(String[] command, Method method, PraCommand annotation) {
        this.cancelled = true;
        this.command = command;
        this.method = method;
        this.annotation = annotation;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public String[] getCommand() {
        return command;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setAnnotation(PraCommand annotation) {
        this.annotation = annotation;
    }

    public PraCommand getAnnotation() {
        return annotation;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
