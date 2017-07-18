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

package de.zortax.pra.network.serialization.impl.types;// Created by leo on 24.06.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCode;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class BooleanSerializer implements FieldSerializer<Boolean> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        byte[] bytes = new byte[f.getName().getBytes().length + 3];
        if (f.getType().equals(boolean.class))
            bytes[0] = f.getBoolean(instance) ? TypeCode.BOOLEAN_TRUE.getCode() : TypeCode.BOOLEAN_FALSE.getCode();
        else
            bytes[0] = f.get(instance).equals(Boolean.TRUE) ? TypeCode.BOOLEAN_TRUE.getCode() : TypeCode.BOOLEAN_FALSE.getCode();
        byte[] nameSize = CharSerializer.toByteArray((char) f.getName().getBytes().length);
        bytes[1] = nameSize[0];
        bytes[2] = nameSize[1];
        byte[] name = f.getName().getBytes();
        System.arraycopy(name, 0, bytes, 3, name.length);
        return bytes;
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Boolean getValue(byte[] bytes) {
        return TypeCode.fromCode(bytes[0]) == TypeCode.BOOLEAN_TRUE;
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 0);
    }
}
