package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

public interface MappingManager<T> {

    TypeMapping<T> getMappingFor(Object object);

    TypeMapping<T> getMappingFor(Object object, String name);

    byte[] getCode(TypeMapping<T> mapping);

    <V> ClassSerializer<V> getSerializer(Class<V> type);

    TypeMapping<T> parseMapping(byte[] bytes);

    boolean isCustomMapped(Object obj);

    byte[] getDataTypeCode(String name);

    byte[] getDataTypeCode();

    byte[] getDataTypeCloser();

}
