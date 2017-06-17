package de.zortax.pra.network.serialization.impl;// Created by leo on 17.06.17

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.TypeMapping;

public class BooleanSerializer implements ClassSerializer<Boolean> {

    @Override
    public byte[] getBytes(Boolean instance, TypeMapping mapping) throws Exception {
        return new byte[0];
    }

    @Override
    public Boolean getInstance(byte[] bytes) throws Exception {
        return bytes[0] == MappingCodes.TRUE.getCode();
    }
}
