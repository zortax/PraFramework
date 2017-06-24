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
import de.zortax.pra.network.serialization.impl.TypeCodes;

import java.lang.reflect.Field;

public class CharSerializer implements FieldSerializer<Character> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {

        char value = (char) f.get(instance);
        byte[] valueBytes = toByteArray(value);
        byte[] nameSize = toByteArray((char) f.getName().getBytes().length);
        byte[] bytes = new byte[f.getName().getBytes().length + 5];
        bytes[0] = TypeCodes.CHAR.getCode();
        bytes[1] = nameSize[0];
        bytes[2] = nameSize[1];
        for (int i = 0; i < f.getName().getBytes().length; i++)
            bytes[i + 3] = f.getName().getBytes()[i];
        bytes[bytes.length - 2] = valueBytes[0];
        bytes[bytes.length - 1] = valueBytes[1];

        return bytes;
    }

    @Override
    public String getFieldName(byte[] bytes) {

        byte[] nameSize = new byte[2];
        nameSize[0] = bytes[1];
        nameSize[1] = bytes[2];

        byte[] name = new byte[(int) fromByteArray(nameSize)];
        System.arraycopy(bytes, 3, name, 0, name.length);

        return new String(name);
    }

    @Override
    public Character getValue(byte[] bytes) {
        return fromByteArray(new byte[]{bytes[bytes.length - 2], bytes[bytes.length - 1]});
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {

        byte[] nameSizeChar = new byte[2];
        nameSizeChar[0] = allData[index + 1];
        nameSizeChar[1] = allData[index + 2];
        int nameSize = (int) fromByteArray(nameSizeChar);
        int size = nameSize + 5;

        byte[] block = new byte[size];

        System.arraycopy(allData, index, block, 0, block.length);

        return block;
    }

    public static byte[] toByteArray(char character) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (character >> 8);
        bytes[1] = (byte) character;
        return bytes;
    }

    public static char fromByteArray(byte[] bytes) {
        return (char) (bytes[0] << 8 | (bytes[1] & 0xFF));
    }
}
