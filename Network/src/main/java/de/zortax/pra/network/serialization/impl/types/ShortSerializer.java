package de.zortax.pra.network.serialization.impl.types;//  Created by leo on 12.07.17.

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class ShortSerializer implements FieldSerializer<Short> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        byte[] bytes = toByteArray((short) f.get(instance));
        return Util.toBytes(TypeCodes.SHORT, f.getName(), bytes);
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Short getValue(byte[] bytes) {
        return fromByteArray(new byte[]{bytes[bytes.length - 2], bytes[bytes.length - 1]});
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 2);
    }

    public static byte[] toByteArray(short val) {
        return new byte[]{(byte) (val >> 8), (byte) val};
    }

    public static short fromByteArray(byte[] array) {
        return (short) (array[0] << 8 | (array[1] & 0xFF));
    }

}
