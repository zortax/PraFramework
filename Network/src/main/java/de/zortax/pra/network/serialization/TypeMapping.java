package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

public class TypeMapping<T> {

    private char code;
    private T valueSize;
    private T nameSize;

    public TypeMapping(char code, T valueSize) {
        this.code = code;
        this.valueSize = valueSize;
    }

    public TypeMapping(char code, T nameSize, T valueSize) {
        this.code = code;
        this.nameSize = nameSize;
        this.valueSize = valueSize;
    }

    public char getCode() {
        return code;
    }

    public T getValueSize() {
        return valueSize;
    }

    public T getNameSize() {
        return nameSize;
    }

}
