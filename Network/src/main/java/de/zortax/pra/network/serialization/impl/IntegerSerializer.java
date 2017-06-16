package de.zortax.pra.network.serialization.impl;//  Created by leo on 16.06.17.

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.TypeMapping;

public class IntegerSerializer implements ClassSerializer<Integer> {

    @Override
    public byte[] getBytes(Integer instance, TypeMapping mapping) throws Exception {
        return new byte[0];
    }

    @Override
    public Integer getInstance(byte[] bytes) throws Exception {
        return null;
    }
}
