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

package de.zortax.pra.network.config;//  Created by Leonard on 03.03.2017.

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.zortax.pra.network.error.ExceptionHandler;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Config {

    private transient String name;
    private transient static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    final void setName(String name) {
        this.name = name;
    }

    public final boolean save() {
        try {
            PrintWriter pw = new PrintWriter(name, "UTF-8");
            pw.println(this.toString());
            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            ExceptionHandler.addException(e);
            return false;
        }
    }

    public static <T extends Config> T load(String file, Class<T> config) {
        if (!file.endsWith(".json"))
            file += ".json";
        File f = new File(file);
        return load(f, config);
    }

    public static <T extends Config> T load(File file, Class<T> config) {
        try {
            if (file.exists()) {
                String json = (new Scanner(file)).useDelimiter("\\Z").next();
                T cfg = gson.fromJson(json, config);
                cfg.setName(file.getName());
                return cfg;
            } else {
                T cfg = config.newInstance();
                cfg.setName(file.getName());
                if (file.createNewFile()) {
                    cfg.save();
                }
                return cfg;
            }
        } catch (Exception e) {
            ExceptionHandler.addException(e);
            // Try to use default values
            T cfg = null;
            try {
                cfg = config.newInstance();
                cfg.setName(file.getName());
            } catch (InstantiationException | IllegalAccessException e1) {
                ExceptionHandler.addException(e1);
            }
            return cfg;
        }
    }

    public static <T extends Config> T load(InputStream inputStream, Class<T> config) {
        try {
            String json = (new Scanner(inputStream)).useDelimiter("\\Z").next();
            T cfg = gson.fromJson(json, config);
            cfg.setName("unknown stream");
            return cfg;

        } catch (Exception e) {
            ExceptionHandler.addException(e);
            // Try to use default values
            T cfg = null;
            try {
                cfg = config.newInstance();
                cfg.setName("unknown stream");
            } catch (InstantiationException | IllegalAccessException e1) {
                ExceptionHandler.addException(e1);
            }
            return cfg;
        }
    }

    public void reload() {
        cloneDataFrom(Config.load(name, this.getClass()));
    }

    private <T extends Config> void cloneDataFrom(T cfg) {
        try {
            Class clazz = this.getClass();
            for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
                clazz.getDeclaredFields()[i].set(this, cfg.getClass().getFields()[i].get(cfg));
            }
        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }
    }

    public final String getName() {
        return name;
    }

    @Override
    public final String toString() {
        return gson.toJson(this);
    }

}
