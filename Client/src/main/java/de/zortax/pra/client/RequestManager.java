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
import de.zortax.pra.network.handler.PacketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RequestManager extends PacketHandler {

    private static ConcurrentHashMap<String, Consumer<PraPacket>> callbacks = new ConcurrentHashMap<>();
    private static PraClient client;

    public RequestManager(PraClient client) {
        super(PraPacket.class);
        RequestManager.client = client;
    }

    public static void sendRequest(PraPacket requestPacket, Consumer<PraPacket> onAnswer) {
        requestPacket.setRequestFlag(true);
        String reqID = (requestPacket.getTimestamp() + callbacks.size()) + "";
        requestPacket.setRequestID(reqID);
        callbacks.put(reqID, onAnswer);
        client.sendPacket(requestPacket);
    }
    
    @Override
    public void handlePacket(PraPacket packet) {
        if (packet.getRequestFlag()) {
            if (callbacks.containsKey(packet.getRequestID())) {
                Consumer<PraPacket> c = callbacks.get(packet.getRequestID());
                callbacks.remove(packet.getRequestID());
                c.accept(packet);
            }
        }
    }
}
