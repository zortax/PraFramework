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

package de.zortax.pra.network.serialization.impl.types;// Created by leo on 14.07.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.ArrayContainer;
import de.zortax.pra.network.serialization.impl.PraSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ArraySerializer implements FieldSerializer<ArrayContainer> {

    private PraSerializer praSerializer;

    public ArraySerializer(PraSerializer praSerializer) {
        this.praSerializer = praSerializer;
    }

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        Object array = f.get(instance);
        ArrayContainer container = new ArrayContainer(array);

        ArrayList<Byte> allBytes = new ArrayList<>();
        allBytes.add(TypeCodes.ARRAY.getCode());

        byte[] nameSize = CharSerializer.toByteArray((char) f.getName().getBytes().length);
        allBytes.add(nameSize[0]);
        allBytes.add(nameSize[1]);
        for (byte b : f.getName().getBytes())
            allBytes.add(b);

        int dimensions = container.getDimensions();
        allBytes.add((byte) dimensions);
        Class elementType = array.getClass().getComponentType();
        for (int i = 0; i < dimensions - 1; i++)
            elementType = elementType.getComponentType();
        allBytes.add(TypeCodes.fromClass(elementType).getCode());

        byte[] arrayBytes = toByteArray(array, praSerializer);
        byte[] valueSize = CharSerializer.toByteArray((char) arrayBytes.length);
        for (byte b : valueSize)
            allBytes.add(b);
        for (byte b : toByteArray(array, praSerializer))
            allBytes.add(b);

        byte[] bytes = new byte[allBytes.size()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = allBytes.get(i);
        return bytes;
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public ArrayContainer getValue(byte[] bytes) {
        return null;
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        int nameSize = CharSerializer.fromByteArray(new byte[]{allData[index + 1], allData[index + 2]});
        int valueSize = CharSerializer.fromByteArray(new byte[]{allData[index + nameSize + 5], allData[index + nameSize + 6]});
        byte[] block = new byte[nameSize + valueSize + 7];
        System.arraycopy(allData, index, block, 0, block.length);
        return block;
    }

    public static TypeCodes getComponentType(byte[] block) {
        return TypeCodes.fromCode(block[CharSerializer.fromByteArray(new byte[]{block[1], block[2]}) + 4]);
    }

    public static byte[] getArrayBytes(byte[] block) {
        int nameSize = CharSerializer.fromByteArray(new byte[]{block[1], block[2]});
        int valueSize = CharSerializer.fromByteArray(new byte[]{block[nameSize + 5], block[nameSize + 6]});
        byte[] value = new byte[valueSize];
        System.arraycopy(block, block.length - valueSize, value, 0, value.length);
        return value;
    }

    public static byte[] toByteArray(Object object, PraSerializer serializer) throws IllegalAccessException {
        if (object.getClass().isArray()) {
            if (object instanceof Object[]) {
                ArrayList<Byte> bytes = new ArrayList<>();
                Object[] array = (Object[]) object;
                for (Object c : array) {
                    byte[] elementBytes = toByteArray(c, serializer);
                    if (c.getClass().isArray()
                            || TypeCodes.fromClass(c.getClass()).equals(TypeCodes.COMPLEX)
                            || TypeCodes.fromClass(c.getClass()).equals(TypeCodes.STRING)) {
                        byte[] elementSize = CharSerializer.toByteArray((char) elementBytes.length);
                        bytes.add(elementSize[0]);
                        bytes.add(elementSize[1]);
                    }
                    for (byte b : elementBytes)
                        bytes.add(b);
                }
                byte[] allBytes = new byte[bytes.size()];
                for (int i = 0; i < allBytes.length; i++)
                    allBytes[i] = bytes.get(i);
                return allBytes;
            } else {
                TypeCodes type = TypeCodes.fromClass(object.getClass().getComponentType());
                byte[] bytes;
                switch (type) {
                    case BYTE:
                        return (byte[]) object;
                    case BOOLEAN_TRUE:
                        boolean[] bools = (boolean[]) object;
                        bytes = new byte[bools.length];
                        for (int i = 0; i < bools.length; i++)
                            bytes[i] = bools[i] ? TypeCodes.BOOLEAN_TRUE.getCode() : TypeCodes.BOOLEAN_FALSE.getCode();
                        return bytes;
                    case SHORT:
                        short[] shorts = (short[]) object;
                        bytes = new byte[shorts.length * 2];
                        for (int i = 0; i < shorts.length; i++)
                            System.arraycopy(ShortSerializer.toByteArray(shorts[i]), 0, bytes, i * 2, 2);
                        return bytes;
                    case CHAR:
                        char[] chars = (char[]) object;
                        bytes = new byte[chars.length * 2];
                        for (int i = 0; i < chars.length; i++)
                            System.arraycopy(CharSerializer.toByteArray(chars[i]), 0, bytes, i * 2, 2);
                        return bytes;
                    case INT:
                        int[] ints = (int[]) object;
                        bytes = new byte[ints.length * 4];
                        for (int i = 0; i < ints.length; i++)
                            System.arraycopy(IntegerSerializer.toByteArray(ints[i]), 0, bytes, i * 4, 4);
                        return bytes;
                    case LONG:
                        long[] longs = (long[]) object;
                        bytes = new byte[longs.length * 8];
                        for (int i = 0; i < longs.length; i++)
                            System.arraycopy(LongSerializer.toByteArray(longs[i]), 0, bytes, i * 8, 8);
                        return bytes;
                    case FLOAT:
                        float[] floats = (float[]) object;
                        bytes = new byte[floats.length * 4];
                        for (int i = 0; i < floats.length; i++)
                            System.arraycopy(FloatSerializer.toByteArray(floats[i]), 0, bytes, i * 4, 4);
                        return bytes;
                    case DOUBLE:
                        double[] doubles = (double[]) object;
                        bytes = new byte[doubles.length * 8];
                        for (int i = 0; i < doubles.length; i++)
                            System.arraycopy(DoubleSerializer.toByteArray(doubles[i]), 0, bytes, i * 8, 8);
                        return bytes;
                    default:
                        return new byte[0];
                }
            }
        } else {
            if (TypeCodes.fromClass(object.getClass()).equals(TypeCodes.COMPLEX)) {
                int classNameLength =  object.getClass().getName().getBytes().length;
                byte[] classNameSize = CharSerializer.toByteArray((char) classNameLength);
                byte[] objectBytes = serializer.serialize(object);
                byte[] bytes = new byte[classNameLength + objectBytes.length + 2];
                bytes[0] = classNameSize[0];
                bytes[1] = classNameSize[1];
                System.arraycopy(object.getClass().getName().getBytes(), 0, bytes, 2, classNameLength);
                System.arraycopy(objectBytes, 0, bytes, classNameLength + 2, bytes.length);
                return bytes;
            } else return serializer.serialize(object);
        }
    }

    public static ArrayContainer fromByteArray(byte[] bytes, Class arrayClass, PraSerializer serializer) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        return fromByteArray(bytes, arrayClass, Util.getArrayComponentType(arrayClass), Util.getArrayDimensions(arrayClass), serializer);
    }

    public static ArrayContainer fromByteArray(byte[] bytes, Class arrayClass, Class componentClass, int dimension, PraSerializer serializer) throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ArrayList<ElementContainer> elements = getElements(bytes, TypeCodes.fromClass(componentClass), dimension);
        Object array = Array.newInstance(arrayClass.getComponentType(), elements.size());

        for (int i = 0; i < elements.size(); i++)
            Array.set(array, i, getElement(elements.get(i), arrayClass.getComponentType(), serializer));

        return new ArrayContainer(array);
    }

    private static Object getElement(ElementContainer elementContainer, Class<?> componentClass, PraSerializer serializer) throws ClassNotFoundException, InvocationTargetException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        if (elementContainer.isArray()) {
            Object array = Array.newInstance(componentClass.getComponentType(), elementContainer.getElements().size());
            for (int i = 0; i < elementContainer.getElements().size(); i++)
                Array.set(array, i, getElement(elementContainer.getElements().get(i), componentClass.getComponentType(), serializer));
            return array;
        } else {
            switch (TypeCodes.fromClass(componentClass)) {
                case BYTE:
                    return elementContainer.getBytes()[0];
                case BOOLEAN_TRUE:
                    return elementContainer.getBytes()[0] == TypeCodes.BOOLEAN_TRUE.getCode();
                case CHAR:
                    return CharSerializer.fromByteArray(elementContainer.getBytes());
                case SHORT:
                    return ShortSerializer.fromByteArray(elementContainer.getBytes());
                case INT:
                    return IntegerSerializer.fromByteArray(elementContainer.getBytes());
                case LONG:
                    return LongSerializer.fromByteArray(elementContainer.getBytes());
                case FLOAT:
                    return FloatSerializer.fromByteArray(elementContainer.getBytes());
                case DOUBLE:
                    return DoubleSerializer.fromByteArray(elementContainer.getBytes());
                case STRING:
                    return new String(elementContainer.getBytes());
                case COMPLEX:
                    byte[] classNameSize = new byte[]{elementContainer.getBytes()[0], elementContainer.getBytes()[1]};
                    int classNameLength = CharSerializer.fromByteArray(classNameSize);
                    byte[] classNameBytes = new byte[classNameLength];
                    System.arraycopy(elementContainer.getBytes(), 2, classNameBytes, 0, classNameLength);
                    Class<?> type = Class.forName(new String(classNameBytes));
                    byte[] object = new byte[elementContainer.getBytes().length - classNameLength - 2];
                    System.arraycopy(elementContainer.getBytes(), classNameLength + 2, object, 0, object.length);
                    return serializer.deserialize(object, type);
                default:
                    return null;
            }
        }

    }

    private static ArrayList<ElementContainer> getElements(byte[] bytes, TypeCodes componentType, int dimension) {
        ArrayList<ElementContainer> elements = new ArrayList<>();
        if (0 < dimension - 1 || componentType.equals(TypeCodes.STRING) || componentType.equals(TypeCodes.COMPLEX)) {
            for (int j = 0; j < bytes.length; j++) {
                int size = CharSerializer.fromByteArray(new byte[]{bytes[j], bytes[j + 1]});
                byte[] element = new byte[size];
                System.arraycopy(bytes, j + 2, element, 0, size);
                if (0 < dimension - 1)
                    elements.add(new ElementContainer(getElements(element, componentType, dimension - 1)));
                else
                    elements.add(new ElementContainer(element));
                j += size + 1;
            }
        } else if (componentType.equals(TypeCodes.BYTE) || componentType.equals(TypeCodes.BOOLEAN_TRUE))
            for (byte b : bytes)
                elements.add(new ElementContainer(new byte[]{b}));
        else if (componentType.equals(TypeCodes.CHAR) || componentType.equals(TypeCodes.SHORT))
            for (int j = 0; j + 1 < bytes.length; j += 2)
                elements.add(new ElementContainer(new byte[]{bytes[j], bytes[j + 1]}));
        else if (componentType.equals(TypeCodes.INT) || componentType.equals(TypeCodes.FLOAT))
            for (int j = 0; j + 3 < bytes.length; j += 4)
                elements.add(new ElementContainer(new byte[]{bytes[j], bytes[j + 1], bytes[j + 2], bytes[j + 3]}));
        else if (componentType.equals(TypeCodes.LONG) || componentType.equals(TypeCodes.DOUBLE))
            for (int j = 0; j + 7 < bytes.length;j += 8)
                elements.add(new ElementContainer(new byte[]{
                        bytes[j],
                        bytes[j + 1],
                        bytes[j + 2],
                        bytes[j + 3],
                        bytes[j + 4],
                        bytes[j + 5],
                        bytes[j + 6],
                        bytes[j + 7]
                }));
        return elements;
    }

    private static class ElementContainer {

        private boolean array;
        private byte[] bytes;
        private ArrayList<ElementContainer> elements;

        ElementContainer(byte[] bytes) {
            this.bytes = bytes;
            this.array = false;
        }

        ElementContainer(ArrayList<ElementContainer> elements) {
            this.elements = elements;
            this.array = true;
        }

        boolean isArray() {
            return array;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public ArrayList<ElementContainer> getElements() {
            return elements;
        }

    }

}
