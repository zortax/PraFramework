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

package de.zortax.pra.network.serialization.impl;// Created by leo on 18.06.17

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.SerializationUtils;
import de.zortax.pra.network.serialization.TypeMapping;

import java.util.ArrayList;

public class LongSerializer implements ClassSerializer<Long> {
    @Override
    public byte[] getBytes(Long instance, TypeMapping mapping) throws Exception {
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = (int) mapping.getValueSize() - 1; i >= 0; i++)
            bytes.add((byte) (instance >> (i * 8)));
        return SerializationUtils.toByteArray(bytes);
    }

    @Override
    public Long getInstance(byte[] bytes) throws Exception {
        long value = 0;
        short shift = (short) ((bytes.length - 1) * 8);
        for (int i = 0; i < bytes.length; i++) {
            if (i == 0)
                value = bytes[i] << shift;
            else
                value |= (bytes[i] & 0xFF) << shift;
            shift -= 8;
        }
        return value;
    }
}
