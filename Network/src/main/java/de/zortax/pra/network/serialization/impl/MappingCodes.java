/*

    PraFramework - A simple TCP-Networking framework for Java
    Copyright (C) 2017  Zortax

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

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
