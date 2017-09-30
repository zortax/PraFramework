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

import de.zortax.pra.network.api.Client;
import de.zortax.pra.network.event.Event;

/**
 * Gets called when a client connected to the server
 */
public class ClientConnectedEvent implements Event {

    private Client client;

    public ClientConnectedEvent(Client client) {
        this.client = client;
    }

    /**
     * @return the client that just connected to the server
     */
    public Client getClient() {
        return client;
    }

}
