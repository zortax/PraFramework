package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

import java.util.List;

public class SerializationUtils {

    public static Serializer create() {
        return new SerializerFactory().build();
    }

    public static SerializerFactory createFactory() {
        return new SerializerFactory();
    }

    public static byte[] toByteArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++)
            bytes[i] = byteList.get(i);
        return bytes;
    }

}
