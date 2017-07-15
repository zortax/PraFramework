package de.zortax.pra.network.serialization.impl.types;// Created by leo on 14.07.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.ArrayContainer;
import de.zortax.pra.network.serialization.impl.PraSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;
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

        for (byte b : toByteArray(array, praSerializer))
            allBytes.add(b);

        byte[] bytes = new byte[allBytes.size()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = allBytes.get(i);
        return bytes;
    }

    public static byte[] toByteArray(Object object, PraSerializer serializer) throws IllegalAccessException {
        if (object.getClass().isArray()) {
            if (object instanceof Object[]) {
                ArrayList<Byte> bytes = new ArrayList<>();
                Object[] array = (Object[]) object;
                byte[] size = CharSerializer.toByteArray((char) array.length);
                bytes.add(size[0]);
                bytes.add(size[1]);
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
                        bytes = new byte[ints.length * 2];
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
        } else
            return serializer.serialize(object);
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
        return new byte[0];
    }
}
