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

package de.zortax.pra.client;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.PraPacket;
import de.zortax.pra.network.event.*;
import de.zortax.pra.network.handler.PacketHandler;
import de.zortax.pra.network.packets.HandshakePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PraClient {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private ConcurrentHashMap<Class<? extends PraPacket>, ArrayList<PacketHandler>> packetHandlers;
    private ArrayList<PacketHandler> defaultHandlers;
    private Thread inputThread;
    private RequestManager requestManager;
    private EventManager eventManager;

    public static final int PROTOCOL_VERSION = 1;

    private static final Object lock = new Object();


    public PraClient(String serverAddress, int port, PacketHandler defaultPacketHandler, String clientName, String clientVersion, String protocolVersion) {
        try {
            this.socket = new Socket(serverAddress, port);
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.packetHandlers = new ConcurrentHashMap<>();
            this.defaultHandlers = new ArrayList<>();
            this.eventManager = new EventManager();
            this.inputThread = new Thread(new InputThread());
            if (defaultPacketHandler != null)
                this.defaultHandlers.add(defaultPacketHandler);
            this.inputThread.start();
            this.requestManager = new RequestManager(this);
            addPacketHandler(requestManager);
            sendPacket(new HandshakePacket(clientName, clientVersion, protocolVersion));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PraClient(String serverAddress, int port) {
        this(serverAddress, port, null, "pra-client", "unknown", "pra_" + PROTOCOL_VERSION);
    }

    public PraClient(String serverAddress, int port, String clientName, String clientVersion) {
        this(serverAddress, port, null, clientName, clientVersion, "pra_" + PROTOCOL_VERSION);
    }

    public void close() {
        try {
            synchronized (lock) {
                inputThread.interrupt();
                dataOutputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void addPacketHandler(PacketHandler handler) {
        for (Class<? extends PraPacket> type : handler.getPacketTypes()) {
            if (packetHandlers.contains(type)) {
                packetHandlers.get(type).add(handler);
            } else {
                ArrayList<PacketHandler> handlers = new ArrayList<>();
                handlers.add(handler);
                packetHandlers.put(type, handlers);
            }
        }
    }

    public List<PacketHandler> getHandlersFor(Class<? extends PraPacket> packetType) {
        ArrayList<PacketHandler> list = new ArrayList<>();
        list.addAll(packetHandlers.getOrDefault(packetType, defaultHandlers));
        list.addAll(packetHandlers.getOrDefault(PraPacket.class, new ArrayList<>()));
        return list;
    }

    public ConcurrentHashMap<Class<? extends PraPacket>, ArrayList<PacketHandler>> getPacketHandlers() {
        return packetHandlers;
    }

    public void putAllPacketHandlers(ConcurrentHashMap<Class<? extends PraPacket>, ArrayList<PacketHandler>> handlers) {
        packetHandlers.putAll(handlers);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public Socket getSocket() {
        return socket;
    }

    /*
     * Threadsafe! (Call it async!)
     */
    public void sendPacket(final PraPacket packet) {
        synchronized (lock) {
            PacketSendEvent event = new PacketSendEvent(packet, null);
            eventManager.callEvent(event);
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
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    private class InputThread implements Runnable {

        private DataInputStream dataInputStream;

        public InputThread() {
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        int size = dataInputStream.readInt();
                        byte[] bytes = new byte[size];
                        for (int i = 0; i < size; i++) {
                            bytes[i] = dataInputStream.readByte();
                        }
                        PacketPreProcessingEvent preProcessingEvent = new PacketPreProcessingEvent(bytes, null);
                        eventManager.callEvent(preProcessingEvent);
                        final PraPacket packet = PraPacket.fromBytes(preProcessingEvent.getRawData());
                        UnhandledPacketEvent event = new UnhandledPacketEvent(packet, null);
                        eventManager.callEvent(event);
                        if (!event.isCancelled()) {
                            try {
                                getHandlersFor(packet.getClass()).forEach(handler -> handler.handlePacket(packet));
                                eventManager.callEvent(new HandledPacketEvent(packet, null));
                            } catch (Exception e) {
                                // PacketHandler exception
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        // TODO: Connection lost
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
