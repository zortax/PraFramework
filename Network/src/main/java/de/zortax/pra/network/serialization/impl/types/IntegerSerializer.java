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
import de.zortax.pra.network.serialization.impl.TypeCode;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class IntegerSerializer implements FieldSerializer<Integer> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        byte[] bytes = toByteArray((int) f.get(instance));
        return Util.toBytes(TypeCode.INT, f.getName(), bytes);
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Integer getValue(byte[] bytes) {
        byte[] intBytes = new byte[4];
        System.arraycopy(bytes, bytes.length - 4, intBytes, 0, 4);
        return fromByteArray(intBytes);
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 4);
    }

    public static byte[] toByteArray(int integer) {
        return new byte[] {
                (byte) (integer >> 24),
                (byte) (integer >> 16),
                (byte) (integer >> 8),
                (byte) integer
        };
    }

    public static int fromByteArray(byte[] array) {
        return (array[0] << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8) | (array[3] & 0xFF);
    }

}
