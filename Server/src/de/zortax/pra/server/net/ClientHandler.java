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

package de.zortax.pra.server.net;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.PraPacket;
import de.zortax.pra.network.api.Client;
import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.server.events.ClientConnectedEvent;
import de.zortax.pra.server.events.ClientDisconnectedEvent;
import de.zortax.pra.server.events.HandledPacketEvent;
import de.zortax.pra.server.events.UnhandledPacketEvent;
import de.zortax.pra.network.packets.HandshakePacket;
import de.zortax.pra.server.ServerManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

public final class ClientHandler extends Thread implements Client {

    private Socket client;
    private String serverName = null;
    private String serverVersion = null;
    private String pluginVersion = null;
    private Scanner scanner = null;
    private PrintWriter printWriter = null;
    private boolean connected;

    public ClientHandler(Socket client) {
        this.client = client;
        this.connected = false;
        try {
            this.printWriter = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            ExceptionHandler.addException(e);
        }
    }

    public void closeConnection() {
        try {
            this.connected = false;
            this.interrupt();
            this.client.close();
            this.scanner.close();
            ServerManager.eventManager.callEvent(new ClientDisconnectedEvent(this));
            ServerManager.server.removeClientHandler(this);
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    public void sendPacket(PraPacket packet) {
        if (!connected)
            throw new IllegalStateException("Client is not connected!");

        printWriter.println(packet.toString());
        printWriter.flush();
    }

    @Override
    public int getProtocolVersion() {
        return -1;
    }

    @Override
    public Socket getSocket() {
        return client;
    }

    @Override
    public InetAddress getInetAddress() {
        return client.getInetAddress();
    }

    @Override
    public void run() {
        try {
            ServerManager.logger.log(Level.INFO, "Incoming connection from " + client.getInetAddress().toString() + " accepted!");
            scanner = new Scanner(client.getInputStream());

            String rawHandshakePacket = scanner.nextLine();

            HandshakePacket packet = PraPacket.getPacket(PraPacket.getPacketSerial(rawHandshakePacket), HandshakePacket.class);
            this.serverName = packet.getServerName();
            this.serverVersion = packet.getServerVersion();
            this.pluginVersion = packet.getPluginVersion();

            ServerManager.server.addClientHandler(this);
            this.connected = true;

            ServerManager.eventManager.callEvent(new HandledPacketEvent(packet, this));
            ServerManager.eventManager.callEvent(new ClientConnectedEvent(this));

            while (true) {
                PraPacket incomingPacket = PraPacket.fromJson(scanner.nextLine());
                try {
                    UnhandledPacketEvent event = new UnhandledPacketEvent(incomingPacket, this);
                    ServerManager.eventManager.callEvent(event);
                    if (!event.isCancelled()) {
                        ServerManager.server.getHandlersFor(incomingPacket.getClass()).forEach(handler -> handler.handlePacket(this, incomingPacket));
                        ServerManager.eventManager.callEvent(new HandledPacketEvent(packet, this));
                    }
                } catch (Exception e) {
                    ExceptionHandler.addException(e);
                }
            }

        } catch (NoSuchElementException e) {
            ServerManager.logger.log(Level.INFO, client.getInetAddress().toString() + " (" + serverName + ") closed connection!");
            closeConnection();
        } catch (Exception e1) {
            closeConnection();
            ExceptionHandler.addException(e1);
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public boolean isConnected() {
        return connected;
    }
}
