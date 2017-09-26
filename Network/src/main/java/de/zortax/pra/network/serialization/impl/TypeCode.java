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

package de.zortax.pra.network.serialization.impl;// Created by leo on 24.06.17

public enum TypeCode {

    BYTE((byte) 1),
    SHORT((byte) 2),
    INT((byte) 3),
    LONG((byte) 4),
    FLOAT((byte) 5),
    DOUBLE((byte) 6),
    CHAR((byte) 7),
    STRING((byte) 8),
    BOOLEAN_TRUE((byte) 9),
    BOOLEAN_FALSE((byte) 10),
    ARRAY((byte) 11),
    COMPLEX((byte) 12),
    NULL((byte) 13);

    private byte code;

    TypeCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static TypeCode fromCode(byte code) {
        switch (code) {
            case (byte) 1:
                return BYTE;
            case (byte) 2:
                return SHORT;
            case (byte) 3:
                return INT;
            case (byte) 4:
                return LONG;
            case (byte) 5:
                return FLOAT;
            case (byte) 6:
                return DOUBLE;
            case (byte) 7:
                return CHAR;
            case (byte) 8:
                return STRING;
            case (byte) 9:
                return BOOLEAN_TRUE;
            case (byte) 10:
                return BOOLEAN_FALSE;
            case (byte) 11:
                return ARRAY;
            case (byte) 12:
                return COMPLEX;
            default:
                return NULL;
        }
    }

    public static TypeCode fromClass(Class clazz) {
        if (clazz.isArray())
            return ARRAY;
        else if (clazz.equals(byte.class) || clazz.equals(Byte.class))
            return  BYTE;
        else if (clazz.equals(short.class) || clazz.equals(Short.class))
            return SHORT;
        else if (clazz.equals(int.class) || clazz.equals(Integer.class))
            return INT;
        else if (clazz.equals(long.class) || clazz.equals(Long.class))
            return LONG;
        else if (clazz.equals(float.class) || clazz.equals(Float.class))
            return FLOAT;
        else if (clazz.equals(double.class) || clazz.equals(Double.class))
            return DOUBLE;
        else if (clazz.equals(char.class) || clazz.equals(Character.class))
            return CHAR;
        else if (clazz.equals(String.class))
            return STRING;
        else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
            return BOOLEAN_TRUE;
        else
            return COMPLEX;
    }

}
