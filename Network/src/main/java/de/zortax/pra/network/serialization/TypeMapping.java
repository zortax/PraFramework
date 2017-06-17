package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

public class TypeMapping<T> {

    private byte code;
    private T valueSize;
    private T nameSize;

    public TypeMapping(byte code, T valueSize) {
        this.code = code;
        this.valueSize = valueSize;
    }

    public TypeMapping(byte code, T nameSize, T valueSize) {
        this.code = code;
        this.nameSize = nameSize;
        this.valueSize = valueSize;
    }

    public byte getCode() {
        return code;
    }

    public T getValueSize() {
        return valueSize;
    }

    public T getNameSize() {
        return nameSize;
    }

}
