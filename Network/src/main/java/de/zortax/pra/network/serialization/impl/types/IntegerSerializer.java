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

package de.zortax.pra.network.serialization.impl.types;// Created by Leonard on 09.07.2017

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class IntegerSerializer implements FieldSerializer<Integer> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {

        ArrayList<Byte> bytes = new ArrayList<>();
        bytes.add(TypeCodes.INT.getCode());

        byte[] nameSize = CharSerializer.toByteArray((char) f.getName().getBytes().length);
        bytes.add(nameSize[0]);
        bytes.add(nameSize[1]);
        for (byte b : f.getName().getBytes())
            bytes.add(b);

        if (f.getType().equals(int.class)) {
            for (byte b : toByteArray(f.getInt(instance)))
                bytes.add(b);
        } else if (f.getType().equals(Integer.class)) {
            for (byte b : toByteArray((Integer) f.get(instance)))
                bytes.add(b);
        } else throw new IllegalArgumentException("Wrong Fieldserializer for this type!");

        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++)
            b[i] = bytes.get(i);
        return b;
    }

    @Override
    public String getFieldName(byte[] bytes) {
        byte[] nameSize = new byte[2];
        nameSize[0] = bytes[1];
        nameSize[1] = bytes[2];
        int nameLength = CharSerializer.fromByteArray(nameSize);
        byte[] name = new byte[nameLength];
        System.arraycopy(bytes, 3, name, 0, nameLength);
        return new String(name);
    }

    @Override
    public Integer getValue(byte[] bytes) {
        byte[] intBytes = new byte[4];
        System.arraycopy(bytes, bytes.length - 4, intBytes, 0, 4);
        return fromByteArray(intBytes);
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        ArrayList<Byte> bytes = new ArrayList<>();
        bytes.add(allData[index]);
        bytes.add(allData[index + 1]);
        bytes.add(allData[index + 2]);

        byte[] nameSizeBytes = new byte[2];
        nameSizeBytes[0] = allData[index + 1];
        nameSizeBytes[1] = allData[index + 2];
        int nameSize = CharSerializer.fromByteArray(nameSizeBytes);
        for (int i = 0; i < nameSize; i++)
            bytes.add(allData[index + 3 + i]);

        for (int i = 0; i < 4; i++)
            bytes.add(allData[index + 3 + nameSize + i]);

        byte[] byteArray = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++)
            byteArray[i] = bytes.get(i);

        return byteArray;
    }

    public static byte[] toByteArray(int integer) {
        return new byte[] {(byte) (integer >> 24), (byte) (integer >> 16), (byte) (integer >> 8), (byte) integer};
    }

    public static int fromByteArray(byte[] array) {
        return (array[0] << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8) | (array[3] & 0xFF);
    }

}
