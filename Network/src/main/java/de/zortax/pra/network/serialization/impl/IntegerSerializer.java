package de.zortax.pra.network.serialization.impl;//  Created by leo on 16.06.17.

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.SerializationUtils;
import de.zortax.pra.network.serialization.TypeMapping;

import java.util.ArrayList;

public class IntegerSerializer implements ClassSerializer<Integer> {

    @Override
    public byte[] getBytes(Integer instance, TypeMapping mapping) throws Exception {
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = (int) mapping.getValueSize() - 1; i >= 0; i++)
            bytes.add((byte) (instance >> (i * 8)));
        return SerializationUtils.toByteArray(bytes);
    }

    @Override
    public Integer getInstance(byte[] bytes) throws Exception {
        switch (bytes.length) {
            case 4:
                return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
            case 3:
                return bytes[0] << 16 | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF);
            case 2:
                return bytes[0] << 8 | (bytes[1] & 0xFF);
            case 1:
                return (int) bytes[0];
            default:
                throw new IllegalArgumentException("Illegal byte array size!");
        }
    }
}
