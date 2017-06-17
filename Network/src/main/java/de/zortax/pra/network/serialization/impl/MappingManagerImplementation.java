package de.zortax.pra.network.serialization.impl;//  Created by leo on 16.06.17.

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.MappingManager;
import de.zortax.pra.network.serialization.TypeMapping;

public class MappingManagerImplementation implements MappingManager<Short> {

    private StringSerializer stringSerializer;
    private ByteSerializer byteSerializer;
    private IntegerSerializer integerSerializer;

    public MappingManagerImplementation(String charset) {
        stringSerializer = new StringSerializer(this, charset);
        byteSerializer = new ByteSerializer(this);
        integerSerializer = new IntegerSerializer();
    }

    @Override
    public TypeMapping<Short> getMappingFor(Object object) {


        return null;
    }

    @Override
    public TypeMapping<Short> getMappingFor(Object object, String name) {
        return null;
    }

    @Override
    public byte[] getCode(TypeMapping<Short> mapping) {
        return new byte[0];
    }

    @Override
    public <V> ClassSerializer<V> getSerializer(Class<V> type) {
        if (type.equals(String.class))
            return (ClassSerializer<V>) stringSerializer;
        else if (type.equals(byte.class) || type.equals(Byte.class))
            return (ClassSerializer<V>) byteSerializer;
        else if (type.equals(int.class) || type.equals(Integer.class))
            return (ClassSerializer<V>) integerSerializer;
        else
            return null;
    }

    @Override
    public TypeMapping<Short> parseMapping(byte[] bytes) {
        return null;
    }

    @Override
    public boolean isCustomMapped(Object obj) {
        return false;
    }

    @Override
    public byte[] getDataTypeCode(String name) {
        return new byte[0];
    }

    @Override
    public byte[] getDataTypeCode() {
        return new byte[0];
    }

    @Override
    public byte[] getDataTypeCloser() {
        return new byte[0];
    }
}
