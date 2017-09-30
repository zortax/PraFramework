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

package de.zortax.pra.network.api;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.PraPacket;

import java.net.InetAddress;
import java.net.Socket;

public interface Client {

    /**
     * Starts the thread listening on this client's input stream
     */
    void start();

    /**
     * Closes the connection to this client
     */
    void closeConnection();

    /**
     * Sends a packet to this client
     * @param packet the packet to be send
     * @return true if the packet was sent successfully
     */
    boolean sendPacket(PraPacket packet);

    /**
     * @return the plain Socket instance of this client
     */
    Socket getSocket();

    /**
     * @return the client's inet address
     */
    InetAddress getInetAddress();

    /**
     * @return the client's protocol version
     */
    int getProtocolVersion();

    /**
     * @return the client's name
     */
    default String getClientName() {
        return "";
    }

    /**
     * @return true if this client is currently connected, false if not
     */
    boolean isConnected();

}