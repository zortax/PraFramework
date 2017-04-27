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

package de.zortax.pra.server.plugin;//  Created by Leonard on 03.03.2017.

import de.zortax.pra.server.ServerManager;
import de.zortax.pra.server.config.PluginConfig;

import java.util.logging.Level;

public abstract class PraPlugin {

    private PluginConfig config = null;

    // "overwritten" by plugins
    @SuppressWarnings("unused")
    public PraPlugin() {

    }

    final void setConfig(PluginConfig config) {
        this.config = config;
    }

    public final String getName() {
        return config.name;
    }

    public final String getVersion() {
        return config.version;
    }

    public final void log(Level level, String msg) {
        ServerManager.logger.log(level, "[" + getName() + "] " + msg);
    }

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();

}
