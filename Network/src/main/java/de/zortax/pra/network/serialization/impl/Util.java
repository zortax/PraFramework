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

}
