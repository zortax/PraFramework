package de.zortax.pra.network.serialization.impl.types;// Created by leo on 13.07.17

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class LongSerializer implements FieldSerializer<Long> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        return Util.toBytes(TypeCodes.LONG, f.getName(), toByteArray((long) f.get(instance)));
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Long getValue(byte[] bytes) {
        return fromByteArray(Util.getValueBytes(bytes, 8));
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 8);
    }

    public static long fromByteArray(byte[] array) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value <<= 8;
            value |= (array[i] & 0xFF);
        }
        return value;
    }

    public static byte[] toByteArray(long value) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return bytes;
    }

}
