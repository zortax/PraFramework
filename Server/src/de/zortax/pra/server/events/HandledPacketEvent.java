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

package de.zortax.pra.server.events;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.PraPacket;
import de.zortax.pra.network.api.Client;
import de.zortax.pra.network.event.Event;

public class HandledPacketEvent implements Event {

    private PraPacket packet;
    private Client sourceClient;

    public HandledPacketEvent(PraPacket packet, Client sourceClient) {
        this.packet = packet;
        this.sourceClient = sourceClient;
    }

    public PraPacket getPacket() {
        return packet;
    }

    public Client getSourceClient() {
        return sourceClient;
    }

}
