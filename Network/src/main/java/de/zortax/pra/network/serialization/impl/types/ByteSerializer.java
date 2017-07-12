package de.zortax.pra.network.serialization.impl.types;//  Created by leo on 12.07.17.

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class ByteSerializer implements FieldSerializer<Byte> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        return Util.toBytes(TypeCodes.BYTE, f.getName(), new byte[]{(byte) f.get(instance)});
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Byte getValue(byte[] bytes) {
        return bytes[bytes.length - 1];
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 1);
    }
}
