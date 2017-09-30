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
import de.zortax.pra.network.handler.PacketHandler;
import de.zortax.pra.server.ServerManager;
import de.zortax.pra.server.handler.PingHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class PraServer {

    private ServerSocket serverSocket = null;
    private ClientAcceptor clientAcceptor;
    private ArrayList<Client> clientHandlerList = null;
    private ConcurrentHashMap<Class<? extends PraPacket>, List<PacketHandler>> packetHandlers = null;
    private ArrayList<PacketHandler> defaultPacketHandler = null;
    private Thread acceptorThread = null;
    private final Object lock = new Object();

    public PraServer(String bindingIP, int port) {

        if (ServerManager.server != null)
            ServerManager.logger.warning("There is already a PraServer instance running!");
        else
            ServerManager.server = this;

        try {
            ServerManager.logger.log(Level.INFO, "Starting server socket...");
            this.serverSocket = new ServerSocket(port, 0, InetAddress.getByName(bindingIP));
            this.clientHandlerList = new ArrayList<>();
            this.packetHandlers = new ConcurrentHashMap<>();
            this.defaultPacketHandler = new ArrayList<>();
            this.acceptorThread = new Thread(new AcceptorThread());
            addPacketHandler(new PingHandler());
            this.clientAcceptor = ClientHandler::new;
            ServerManager.logger.log(Level.INFO, "Done!");
        } catch (IOException e) {
            ServerManager.logger.log(Level.SEVERE, "Error while initializing server socket!");
            ExceptionHandler.addException(e);
        }
    }

    /**
     * Starts the server connection listener thread
     */
    public void start() {
        ServerManager.logger.log(Level.INFO, "Starting connection listener...");
        acceptorThread.start();
    }

    /**
     * Interrupts the server connection listener thread
     */
    public void shutdown() {
        acceptorThread.interrupt();
    }

    protected void addClientHandler(ClientHandler handler) {
        synchronized (lock) {
            clientHandlerList.add(handler);
        }
    }

    /**
     * Registers a new packet handler
     * @param handler the packet handler instance to add
     */
    public void addPacketHandler(PacketHandler handler) {
        for (Class<? extends PraPacket> type : handler.getPacketTypes()) {
            if (packetHandlers.containsKey(type)) {
                packetHandlers.get(type).add(handler);
            } else {
                ArrayList<PacketHandler> handlers = new ArrayList<>();
                handlers.add(handler);
                packetHandlers.put(type, handlers);
            }
        }
    }

    /**
     * Gets the packet handlers for a specific packet type
     * @param packetType the packet type
     * @return the packet handlers for that tyoe
     */
    public List<PacketHandler> getHandlersFor(Class<? extends PraPacket> packetType) {
        ArrayList<PacketHandler> list = new ArrayList<>();
        list.addAll(packetHandlers.getOrDefault(packetType, defaultPacketHandler));
        list.addAll(packetHandlers.getOrDefault(PraPacket.class, new ArrayList<>()));
        return list;
    }

    /**
     * @return all clients that are currently connected
     */
    public List<Client> getClients() {
        return clientHandlerList;
    }

    /**
     * Sets the ClientAcceptor implementation
     * @param clientAcceptor the ClientAcceptor instance
     */
    public void setClientAcceptor(ClientAcceptor clientAcceptor) {
        this.clientAcceptor = clientAcceptor;
    }

    protected void removeClientHandler(ClientHandler handler) {
        if (clientHandlerList.contains(handler))
            clientHandlerList.remove(handler);
    }

    private class AcceptorThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    clientAcceptor.accept(serverSocket.accept()).start();
                } catch (Exception e) {
                    ExceptionHandler.addException(e);
                }
            }
        }
    }
}
