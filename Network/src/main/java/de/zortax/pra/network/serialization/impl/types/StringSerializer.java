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

package de.zortax.pra.network.serialization.impl.types;// Created by leo on 12.07.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCode;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class StringSerializer implements FieldSerializer<String> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        byte[] val = ((String) f.get(instance)).getBytes();
        byte[] bytes = new byte[val.length + f.getName().getBytes().length + 5];
        bytes[0] = TypeCode.STRING.getCode();
        System.arraycopy(CharSerializer.toByteArray((char) f.getName().getBytes().length), 0, bytes, 1, 2);
        System.arraycopy(f.getName().getBytes(), 0, bytes, 3, f.getName().getBytes().length);
        System.arraycopy(CharSerializer.toByteArray((char) val.length), 0, bytes, f.getName().getBytes().length + 3, 2);
        System.arraycopy(val, 0, bytes, f.getName().getBytes().length + 5, val.length);
        return bytes;
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public String getValue(byte[] bytes) {
        int nameSize = CharSerializer.fromByteArray(new byte[]{bytes[1], bytes[2]});
        int valueSize = CharSerializer.fromByteArray(new byte[]{bytes[nameSize + 3], bytes[nameSize + 4]});
        byte[] value = new byte[valueSize];
        System.arraycopy(bytes, nameSize + 5, value, 0, valueSize);
        return new String(value);
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        int nameSize = CharSerializer.fromByteArray(new byte[]{allData[index + 1], allData[index + 2]});
        int valueSize = CharSerializer.fromByteArray(new byte[]{allData[index + nameSize + 3], allData[index + nameSize + 4]});
        byte[] block = new byte[valueSize + nameSize + 5];
        System.arraycopy(allData, index, block, 0, nameSize + valueSize + 5);
        return block;
    }
}
