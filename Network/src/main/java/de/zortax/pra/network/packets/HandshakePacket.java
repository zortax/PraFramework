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

package de.zortax.pra.network.packets;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.network.PraPacket;

public class HandshakePacket extends PraPacket {

    private String serverName;
    private String serverVersion;
    private String pluginVersion;

    public HandshakePacket(String serverName, String serverVersion, String pluginVersion){
        this.serverName = serverName;
        this.serverVersion = serverVersion;
        this.pluginVersion = pluginVersion;
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

}
