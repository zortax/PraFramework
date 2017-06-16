package de.zortax.pra.network.serialization.impl;//  Created by leo on 16.06.17.

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.MappingManager;
import de.zortax.pra.network.serialization.SerializationUtils;
import de.zortax.pra.network.serialization.TypeMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class StringSerializer implements ClassSerializer<String> {

    private MappingManager mappingManager;
    private String charset;

    StringSerializer(MappingManager mappingManager, String charset) {
        this.mappingManager = mappingManager;
        this.charset = charset;
    }

    @Override
    public byte[] getBytes(String instance, TypeMapping mapping) throws UnsupportedEncodingException {
        ArrayList<Byte>  bytes = new ArrayList<>();
        for (byte b : mappingManager.getCode(mapping))
            bytes.add(b);
        for (byte b : instance.getBytes(charset))
            bytes.add(b);
        return SerializationUtils.toByteArray(bytes);
    }

    @Override
    public String getInstance(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, charset);
    }

}
