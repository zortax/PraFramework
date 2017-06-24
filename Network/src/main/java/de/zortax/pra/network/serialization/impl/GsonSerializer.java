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

package de.zortax.pra.network.serialization.impl;// Created by leo on 24.06.17

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.zortax.pra.network.serialization.Serializer;

public class GsonSerializer implements Serializer {

    private Gson gson;

    public GsonSerializer() {
        this.gson = new Gson();
    }

    public GsonSerializer(boolean prettyPrinting) {
        if (prettyPrinting)
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        else
            this.gson = new Gson();
    }

    @Override
    public byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) {
        return gson.fromJson(new String(bytes), type);
    }
}
