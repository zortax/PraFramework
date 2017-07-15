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

package de.zortax.pra.network.serialization.impl;//  Created by leo on 12.07.17.

import de.zortax.pra.network.serialization.impl.types.CharSerializer;

public class Util {

    public static String getFieldName(byte[] bytes) {
        byte[] nameSize = new byte[2];
        nameSize[0] = bytes[1];
        nameSize[1] = bytes[2];
        int nameLength = CharSerializer.fromByteArray(nameSize);
        byte[] name = new byte[nameLength];
        System.arraycopy(bytes, 3, name, 0, nameLength);
        return new String(name);
    }

    public static byte[] toBytes(TypeCodes typeCode, String name, byte[] value) {
        byte[] nameBytes = name.getBytes();
        byte[] bytes = new byte[nameBytes.length + value.length + 3];
        bytes[0] = typeCode.getCode();
        System.arraycopy(CharSerializer.toByteArray((char) nameBytes.length), 0, bytes, 1, 2);
        System.arraycopy(nameBytes, 0, bytes, 3, nameBytes.length);
        System.arraycopy(value, 0, bytes, nameBytes.length + 3, value.length);
        return bytes;
    }

    public static byte[] getBlock(byte[] allData, int index, int valueSize) {
        byte[] nameSizeBytes = new byte[2];
        nameSizeBytes[0] = allData[index + 1];
        nameSizeBytes[1] = allData[index + 2];
        int nameSize = CharSerializer.fromByteArray(nameSizeBytes);
        byte[] bytes = new byte[nameSize + valueSize + 3];
        System.arraycopy(allData, index, bytes, 0, bytes.length);
        return bytes;
    }

    public static byte[] getValueBytes(byte[] bytes, int valueSize) {
        byte[] value = new byte[valueSize];
        System.arraycopy(bytes, bytes.length - valueSize, value, 0, valueSize);
        return value;
    }

    public static int getArrayDimensions(Object array) {
        if (!array.getClass().isArray())
            throw new IllegalArgumentException("Object must be an array!");
        int dimensions = 1;
        Class elementType = array.getClass().getComponentType();
        while (elementType.isArray()) {
            dimensions++;
            elementType = elementType.getComponentType();
        }
        return dimensions;
    }

}
