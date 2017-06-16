package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

public interface ClassSerializer<T> {

    byte[] getBytes(T instance, TypeMapping mapping) throws Exception;

    T getInstance(byte[] bytes) throws Exception;

}
