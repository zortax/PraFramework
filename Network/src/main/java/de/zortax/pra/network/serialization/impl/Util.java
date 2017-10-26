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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Util {

    /**
     *
     * @param bytes representing one serialized field
     * @return the name of the field
     */
    public static String getFieldName(byte[] bytes) {
        byte[] nameSize = new byte[2];
        nameSize[0] = bytes[1];
        nameSize[1] = bytes[2];
        int nameLength = CharSerializer.fromByteArray(nameSize);
        byte[] name = new byte[nameLength];
        System.arraycopy(bytes, 3, name, 0, nameLength);
        return new String(name);
    }

    /**
     * Helper class for some primitive types like short or int
     * @param typeCode
     * @param name
     * @param value
     * @return
     */
    public static byte[] toBytes(TypeCode typeCode, String name, byte[] value) {
        byte[] nameBytes = name.getBytes();
        byte[] bytes = new byte[nameBytes.length + value.length + 3];
        bytes[0] = typeCode.getCode();
        System.arraycopy(CharSerializer.toByteArray((char) nameBytes.length), 0, bytes, 1, 2);
        System.arraycopy(nameBytes, 0, bytes, 3, nameBytes.length);
        System.arraycopy(value, 0, bytes, nameBytes.length + 3, value.length);
        return bytes;
    }

    /**
     * Helper class for some primitive types like short or int
     * @param allData
     * @param index
     * @param valueSize
     * @return
     */
    public static byte[] getBlock(byte[] allData, int index, int valueSize) {
        byte[] nameSizeBytes = new byte[2];
        nameSizeBytes[0] = allData[index + 1];
        nameSizeBytes[1] = allData[index + 2];
        int nameSize = CharSerializer.fromByteArray(nameSizeBytes);
        byte[] bytes = new byte[nameSize + valueSize + 3];
        System.arraycopy(allData, index, bytes, 0, bytes.length);
        return bytes;
    }

    /**
     * Helper class for some primitive types like short or int
     * @param bytes
     * @param valueSize
     * @return
     */
    public static byte[] getValueBytes(byte[] bytes, int valueSize) {
        byte[] value = new byte[valueSize];
        System.arraycopy(bytes, bytes.length - valueSize, value, 0, valueSize);
        return value;
    }

    public static int getArrayDimensions(Object array) {
        return getArrayDimensions(array.getClass());
    }

    public static int getArrayDimensions(Class clazz) {
        if (!clazz.isArray())
            throw new IllegalArgumentException("Object must be an array!");
        int dimensions = 1;
        Class elementType = clazz.getComponentType();
        while (elementType.isArray()) {
            dimensions++;
            elementType = elementType.getComponentType();
        }
        return dimensions;
    }

    public static Class getArrayComponentType(Class arrayClass) {
        if (!arrayClass.isArray())
            throw new IllegalArgumentException("Class must be an array class!");
        Class componentType = arrayClass.getComponentType();
        while (componentType.isArray())
            componentType = componentType.getComponentType();
        return componentType;
    }

    public static ArrayList<Field> getAllFields(Class clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        while (clazz.getSuperclass() != Object.class) {
            clazz = clazz.getSuperclass();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields;
    }

    public static HashMap<String, Field> getAllFieldsMapped(Class clazz) {
        HashMap<String, Field> fields = new HashMap<>();
        for (Field f : clazz.getDeclaredFields())
            fields.put(f.getName(), f);
        while (clazz.getSuperclass() != Object.class) {
            clazz = clazz.getSuperclass();
            for (Field f : clazz.getDeclaredFields())
                fields.put(f.getName(), f);
        }
        return fields;
    }

}
