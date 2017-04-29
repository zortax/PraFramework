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
import de.zortax.pra.network.event.HandledPacketEvent;
import de.zortax.pra.network.event.PacketPreProcessingEvent;
import de.zortax.pra.network.event.PacketSendEvent;
import de.zortax.pra.network.event.UnhandledPacketEvent;
import de.zortax.pra.network.packets.HandshakePacket;
import de.zortax.pra.server.ServerManager;
import de.zortax.pra.server.events.ClientConnectedEvent;
import de.zortax.pra.server.events.ClientDisconnectedEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.logging.Level;

public final class ClientHandler extends Thread implements Client {

    private Socket client;
    private String serverName = null;
    private String serverVersion = null;
    private String pluginVersion = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private boolean connected;

    public ClientHandler(Socket client) {
        this.client = client;
        this.connected = false;
        try {
            this.dataOutputStream = new DataOutputStream(client.getOutputStream());
            this.dataOutputStream.flush();
        } catch (IOException e) {
            ExceptionHandler.addException(e);
        }
    }

    public void closeConnection() {
        try {
            this.connected = false;
            this.interrupt();
            this.client.close();
            this.dataInputStream.close();
            ServerManager.eventManager.callEvent(new ClientDisconnectedEvent(this));
            ServerManager.server.removeClientHandler(this);
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    public void sendPacket(PraPacket packet) {
        if (!connected)
            throw new IllegalStateException("Client is not connected!");
        PacketSendEvent event = new PacketSendEvent(packet, this);
        ServerManager.eventManager.callEvent(event);
        if (!event.isCancelled()) {
            try {
                dataOutputStream.writeInt(event.getPacket().getBytes().length);
                dataOutputStream.flush();
                dataOutputStream.write(event.getPacket().getBytes());
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

            ServerManager.debug("Opening DataInputStream for " + getInetAddress().toString() + "...", 9);
            dataInputStream = new DataInputStream(client.getInputStream());

            ServerManager.debug("Waiting for HandshakePacket size...", 9);
            int size = dataInputStream.readInt();
            ServerManager.debug("Received HandshakePacket size: " + size, 9);
            byte[] bytes = new byte[size];
            ServerManager.debug("Reading bytes...", 9);
            int read = 0;
            for (int i = 0; i < size; i++) {
                bytes[i] = dataInputStream.readByte();
                read++;
            }
            ServerManager.debug("Bytes read: " + read, 9);

            ServerManager.debug("Creating HandshakePaket instance from bytes...", 9);
            HandshakePacket packet = (HandshakePacket) PraPacket.fromBytes(bytes);
            this.serverName = packet.getServerName();
            this.serverVersion = packet.getServerVersion();
            this.pluginVersion = packet.getPluginVersion();
            ServerManager.debug("Handshake information:", 8);
            ServerManager.debug("  Client name:             " + serverName, 8);
            ServerManager.debug("  Client version:          " + serverVersion, 8);
            ServerManager.debug("  Client protocol version: " + pluginVersion, 8);

            ServerManager.debug("Registering client to ServerManager...", 10);
            ServerManager.server.addClientHandler(this);

            ServerManager.debug("Connection established (connected = true)!", 8);
            this.connected = true;

            ServerManager.debug("Calling events...", 10);
            ServerManager.eventManager.callEvent(new HandledPacketEvent(packet, this));
            ServerManager.eventManager.callEvent(new ClientConnectedEvent(this));

            ServerManager.debug("Starting client loop...", 9);

            while (true) {

                int s = dataInputStream.readInt();

                ServerManager.debug("Received packet size: " + s, 9);
                byte[] data = new byte[s];
                int r = 0;
                for (int i = 0; i < s; i++) {
                    data[i] = dataInputStream.readByte();
                    r++;
                }
                ServerManager.debug("Bytes read: " + r, 9);
                ServerManager.debug("Calling PacketPreProcessingEvent...", 9);
                PacketPreProcessingEvent preEvent = new PacketPreProcessingEvent(data, this);
                ServerManager.eventManager.callEvent(preEvent);
                ServerManager.debug("Creating PraPacket instance from bytes...", 9);
                PraPacket incomingPacket = PraPacket.fromBytes(preEvent.getRawData());
                try {
                    ServerManager.debug("Calling UnhandledPacketEvent...", 9);
                    UnhandledPacketEvent event = new UnhandledPacketEvent(incomingPacket, this);
                    ServerManager.eventManager.callEvent(event);
                    if (!event.isCancelled()) {
                        ServerManager.debug("Calling packet handlers...", 9);
                        ServerManager.server.getHandlersFor(incomingPacket.getClass()).forEach(handler -> handler.handlePacket(this, incomingPacket));
                        ServerManager.debug("Calling HandledPacketEvent...", 9);
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
            ServerManager.logger.log(Level.WARNING, "Connection to " + client.getInetAddress().toString() + " (" + serverName + ") was interrupted: " + e1.getMessage());
            closeConnection();
            ExceptionHandler.addException(e1);
        }
    }

    public String getClientName() {
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
