package de.zortax.pra.network.serialization.impl;// Created by leo on 16.06.17

public enum MappingCodes {

    FALSE((byte) 0, Boolean.class),
    TRUE((byte) 1, Boolean.class),
    BYTE((byte) 2, Byte.class),
    CHAR((byte) 3, Character.class),
    SHORT((byte) 4, Short.class),
    INTEGER((byte) 5, Integer.class),
    LONG((byte) 6, Long.class),
    FLOAT((byte) 7, Float.class),
    DOUBLE((byte) 8, Double.class),
    ARRAY((byte) 9, null),
    STRING((byte) 10, String.class);

    private byte code;
    private Class type;

    MappingCodes(byte code, Class type) {
        this.code = code;
        this.type = type;
    }

    public byte getCode() {
        return code;
    }

    public Class getType() {
        return type;
    }

}
