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

package de.zortax.pra.network.serialization.impl;// Created by leo on 24.06.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.Serializer;
import de.zortax.pra.network.serialization.impl.types.*;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class PraSerializer implements Serializer {

    private static Unsafe unsafe;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FieldSerializer<ArrayContainer> arraySerializer;
    private FieldSerializer<Byte> byteSerializer;
    private FieldSerializer<Short> shortSerializer;
    private FieldSerializer<Integer> integerSerializer;
    private FieldSerializer<Long> longSerializer;
    private FieldSerializer<Float> floatSerializer;
    private FieldSerializer<Double> doubleSerializer;
    private FieldSerializer<Character> characterSerializer;
    private FieldSerializer<String> stringSerializer;
    private FieldSerializer<Boolean> booleanSerializer;

    public PraSerializer() {
        this.arraySerializer = new ArraySerializer(this);
        this.byteSerializer = new ByteSerializer();
        this.shortSerializer = new ShortSerializer();
        this.integerSerializer = new IntegerSerializer();
        this.longSerializer = new LongSerializer();
        this.floatSerializer = new FloatSerializer();
        this.doubleSerializer = new DoubleSerializer();
        this.characterSerializer = new CharSerializer();
        this.stringSerializer = new StringSerializer();
        this.booleanSerializer = new BooleanSerializer();
    }

    @Override
    public byte[] serialize(Object obj) throws IllegalAccessException {

        if (obj.getClass().equals(byte.class) || obj instanceof Byte)
            return new byte[]{(byte) obj};
        else if (obj.getClass().equals(boolean.class) || obj instanceof Boolean)
            return (boolean) obj ? new byte[]{TypeCode.BOOLEAN_TRUE.getCode()} : new byte[]{TypeCode.BOOLEAN_FALSE.getCode()};
        else if (obj.getClass().equals(char.class) || obj instanceof Character)
            return CharSerializer.toByteArray((char) obj);
        else if (obj.getClass().equals(short.class) || obj instanceof Short)
            return ShortSerializer.toByteArray((short) obj);
        else if (obj.getClass().equals(int.class) || obj instanceof Integer)
            return IntegerSerializer.toByteArray((int) obj);
        else if (obj.getClass().equals(long.class) || obj instanceof Long)
            return LongSerializer.toByteArray((long) obj);
        else if (obj.getClass().equals(float.class) || obj instanceof Float)
            return FloatSerializer.toByteArray((float) obj);
        else if (obj.getClass().equals(double.class) || obj instanceof Double)
            return DoubleSerializer.toByteArray((double) obj);
        else if (obj instanceof String)
            return ((String) obj).getBytes();
        else if (obj.getClass().isArray())
            return ArraySerializer.toByteArray(obj, this);
        else {

            ArrayList<Byte> allBytes = new ArrayList<>();

            for (Field f : Util.getAllFields(obj.getClass())) {
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
                    f.setAccessible(true);
                    if (f.getType().isArray()) {
                        for (byte b : arraySerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(byte.class) || f.getType().equals(Byte.class)) {
                        for (byte b : byteSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(short.class) || f.getType().equals(Short.class)) {
                        for (byte b : shortSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                        for (byte b : integerSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(long.class) || f.getType().equals(Long.class)) {
                        for (byte b : longSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(float.class) || f.getType().equals(Float.class)) {
                        for (byte b : floatSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                        for (byte b : doubleSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(char.class) || f.getType().equals(Character.class)) {
                        for (byte b : characterSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(String.class)) {
                        for (byte b : stringSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)) {
                        for (byte b : booleanSerializer.toBytes(f, obj))
                            allBytes.add(b);
                    } else {
                        byte[] data = serialize(f.get(obj));
                        byte[] name = f.getName().getBytes();
                        byte[] nameSize = CharSerializer.toByteArray((char) name.length);
                        byte[] dataSize = CharSerializer.toByteArray((char) data.length);
                        byte[] type = f.get(obj).getClass().getName().getBytes();
                        byte[] typeSize = CharSerializer.toByteArray((char) type.length);

                        allBytes.add(TypeCode.COMPLEX.getCode());

                        for (byte b : nameSize)
                            allBytes.add(b);
                        for (byte b : name)
                            allBytes.add(b);
                        for (byte b : typeSize)
                            allBytes.add(b);
                        for (byte b : type)
                            allBytes.add(b);
                        for (byte b : dataSize)
                            allBytes.add(b);
                        for (byte b : data)
                            allBytes.add(b);
                    }

                }
            }

            byte[] bytes = new byte[allBytes.size()];
            for (int i = 0; i < bytes.length; i++)
                bytes[i] = allBytes.get(i);
            return bytes;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException {

        if (type.equals(byte.class) || type.equals(Byte.class))
            return (T) new Byte(bytes[0]);
        else if (type.equals(boolean.class) || type.equals(Boolean.class))
            return (T) (TypeCode.fromCode(bytes[0]).equals(TypeCode.BOOLEAN_TRUE) ? Boolean.TRUE : Boolean.FALSE);
        else if (type.equals(char.class) || type.equals(Character.class))
            return (T) new Character(CharSerializer.fromByteArray(bytes));
        else if (type.equals(short.class) || type.equals(Short.class))
            return (T) new Short(ShortSerializer.fromByteArray(bytes));
        else if (type.equals(int.class) || type.equals(Integer.class))
            return (T) new Integer(IntegerSerializer.fromByteArray(bytes));
        else if (type.equals(long.class) || type.equals(Long.class))
            return (T) new Long(LongSerializer.fromByteArray(bytes));
        else if (type.equals(float.class) || type.equals(Float.class))
            return (T) new Float(FloatSerializer.fromByteArray(bytes));
        else if (type.equals(double.class) || type.equals(Double.class))
            return (T) new Double(DoubleSerializer.fromByteArray(bytes));
        else if (type.equals(String.class))
            return (T) new String(bytes);
        else if (type.isArray())
            return ArraySerializer.fromByteArray(bytes, type, this).getArray(type);
        else {

            T instance = type.cast(unsafe.allocateInstance(type));

            for (int i = 0; i < bytes.length; ) {
                byte[] block = new byte[0];
                HashMap<String, Field> allFields = Util.getAllFieldsMapped(type);
                Field f;

                switch (TypeCode.fromCode(bytes[i])) {
                    case BYTE:

                        block = byteSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(byteSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(byte.class) ? byteSerializer.getValue(block).byteValue() : byteSerializer.getValue(block));

                        break;
                    case SHORT:

                        block = shortSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(shortSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(short.class) ? shortSerializer.getValue(block).shortValue() : shortSerializer.getValue(block));

                        break;
                    case INT:

                        block = integerSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(shortSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(int.class) ? integerSerializer.getValue(block).intValue() : integerSerializer.getValue(block));

                        break;
                    case LONG:

                        block = longSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(longSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(long.class) ? longSerializer.getValue(block).longValue() : longSerializer.getValue(block));

                        break;
                    case FLOAT:

                        block = floatSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(floatSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(float.class) ? floatSerializer.getValue(block).floatValue() : floatSerializer.getValue(block));

                        break;
                    case DOUBLE:

                        block = doubleSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(doubleSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(double.class) ? doubleSerializer.getValue(block).doubleValue() : doubleSerializer.getValue(block));

                        break;
                    case CHAR:

                        block = characterSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(characterSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, f.getType().equals(char.class) ? characterSerializer.getValue(block).charValue() : characterSerializer.getValue(block));

                        break;
                    case STRING:

                        block = stringSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(stringSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, stringSerializer.getValue(block));

                        break;
                    case BOOLEAN_TRUE:

                        block = booleanSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(booleanSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, true);

                        break;
                    case BOOLEAN_FALSE:

                        block = booleanSerializer.getBlockFrom(bytes, i);
                        f = allFields.get(booleanSerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, false);

                        break;
                    case ARRAY:

                        block = arraySerializer.getBlockFrom(bytes, i);
                        f = allFields.get(arraySerializer.getFieldName(block));
                        f.setAccessible(true);
                        f.set(instance, ArraySerializer.fromByteArray(ArraySerializer.getArrayBytes(block), f.getType(), this).getArray());

                        break;
                    case COMPLEX:

                        byte[] nameSizeBytes = new byte[2];
                        nameSizeBytes[0] = bytes[i + 1];
                        nameSizeBytes[1] = bytes[i + 2];
                        int nameSize = (int) CharSerializer.fromByteArray(nameSizeBytes);
                        byte[] nameBytes = new byte[nameSize];
                        for (int j = 0; j < nameBytes.length; j++)
                            nameBytes[j] = bytes[i + 3 + j];
                        String name = new String(nameBytes);

                        byte[] typeSizeBytes = new byte[2];
                        typeSizeBytes[0] = bytes[i + 3 + nameSize];
                        typeSizeBytes[1] = bytes[i + 4 + nameSize];
                        int typeSize = (int) CharSerializer.fromByteArray(typeSizeBytes);
                        byte[] typeBytes = new byte[typeSize];
                        for (int j = 0; j < typeBytes.length; j++)
                            typeBytes[j] = bytes[i + 5 + nameSize + j];
                        String typeName = new String(typeBytes);

                        byte[] dataSizeBytes = new byte[2];
                        dataSizeBytes[0] = bytes[i + 5 + nameSize + typeSize];
                        dataSizeBytes[1] = bytes[i + 6 + nameSize + typeSize];
                        int dataSize = (int) CharSerializer.fromByteArray(dataSizeBytes);
                        byte[] dataBytes = new byte[dataSize];
                        for (int j = 0; j < dataBytes.length; j++)
                            dataBytes[j] = bytes[i + 7 + nameSize + typeSize + j];


                        f = allFields.get(name);
                        f.set(instance, deserialize(dataBytes, Class.forName(typeName)));

                        i += 7 + nameSize + dataSize + typeSize;
                        break;
                    default:
                        return null;
                }
                i += block.length;
            }


            return instance;
        }
    }

    public FieldSerializer<Byte> getByteSerializer() {
        return byteSerializer;
    }

    public FieldSerializer<Short> getShortSerializer() {
        return shortSerializer;
    }

    public FieldSerializer<Integer> getIntegerSerializer() {
        return integerSerializer;
    }

    public FieldSerializer<Long> getLongSerializer() {
        return longSerializer;
    }

    public FieldSerializer<Float> getFloatSerializer() {
        return floatSerializer;
    }

    public FieldSerializer<Double> getDoubleSerializer() {
        return doubleSerializer;
    }

    public FieldSerializer<Character> getCharacterSerializer() {
        return characterSerializer;
    }

    public FieldSerializer<String> getStringSerializer() {
        return stringSerializer;
    }

    public FieldSerializer<Boolean> getBooleanSerializer() {
        return booleanSerializer;
    }
}
