package de.zortax.pra.network.serialization.impl;// Created by leo on 16.06.17

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.MappingManager;
import de.zortax.pra.network.serialization.TypeMapping;

public class ByteSerializer implements ClassSerializer<Byte> {

    private MappingManager mappingManager;

    public ByteSerializer(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @Override
    public byte[] getBytes(Byte instance, TypeMapping mapping) throws Exception {
        return new byte[]{mappingManager.getCode(mapping)[0], instance};
    }

    @Override
    public Byte getInstance(byte[] bytes) throws Exception {
        return bytes[1];
    }

}
