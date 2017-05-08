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

import de.zortax.pra.network.config.Config;
import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.server.ServerManager;
import de.zortax.pra.server.config.PluginConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class PluginLoader {

    private ArrayList<PraPlugin> plugins;
    private Method addURLMethod = null;
    private URLClassLoader classLoader = null;

    public PluginLoader() {
        this.plugins = new ArrayList<>();
        try {
            this.classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            this.addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            this.addURLMethod.setAccessible(true);
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    public void loadPlugins(String pluginPath) {

        ServerManager.logger.log(Level.INFO, "Loading plugins...");

        if (addURLMethod == null) {
            ExceptionHandler.addException(new IllegalStateException("Cannot modify classpath!"));
            return;
        }

        try {
            File file = new File(pluginPath);
            if (!file.exists())
                file.mkdir();
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }

        try (Stream<Path> paths = Files.walk(Paths.get(pluginPath))) {

            paths.filter(path -> (path.toFile().isFile() && path.toFile().getName().endsWith(".jar"))).forEach(path -> {
                try {
                    JarFile jar = new JarFile(path.toFile());
                    ZipEntry description = jar.getEntry("plugin.json");
                    if (description != null) {
                        PluginConfig pluginConfig = Config.load(jar.getInputStream(description), PluginConfig.class);
                        if (pluginConfig.main == null) {
                            throw new IllegalStateException("Invalid plugin.json: main class not defined!");
                        } else {
                            addUrl(path.toUri().toURL());

                            Class clazz = Class.forName(pluginConfig.main);
                            if (clazz.getSuperclass().equals(PraPlugin.class)) {
                                @SuppressWarnings("unchecked") // it's checked ._.
                                        Class<? extends PraPlugin> main = (Class<? extends PraPlugin>) Class.forName(pluginConfig.main);
                                PraPlugin plugin = main.newInstance();
                                plugin.setConfig(pluginConfig);
                                plugin.onLoad();
                                plugin.log(Level.INFO, "Loaded version " + plugin.getVersion() + "!");
                                plugins.add(plugin);
                            } else {
                                throw new Exception("Plugin main class (" + jar.getName() + ") does not extend PraPlugin!");
                            }
                        }
                    }
                } catch (Exception e) {
                    ServerManager.logger.log(Level.SEVERE, "Error while loading Plugin " + path.getFileName() + "!");
                    ExceptionHandler.addException(e);
                }
            });

        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }

    }

    private void addUrl(URL url) throws InvocationTargetException, IllegalAccessException {
        addURLMethod.invoke(classLoader, url);
    }

    public void enablePlugins() {
        plugins.forEach(plugin -> {
            plugin.onEnable();
            plugin.log(Level.INFO, "Enabled version " + plugin.getVersion() + "!");
        });
    }

    public void disablePlugins() {
        plugins.forEach(plugin -> {
            plugin.onDisable();
            plugin.log(Level.INFO, "Disabled version " + plugin.getVersion() + "!");
        });
    }

    public ArrayList<PraPlugin> getPlugins() {
        return plugins;
    }

}
