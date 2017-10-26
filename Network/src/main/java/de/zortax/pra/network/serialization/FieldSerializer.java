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

package de.zortax.pra.network.serialization;// Created by leo on 24.06.17

import java.lang.reflect.Field;

public interface FieldSerializer<T> {

    /**
     * Transforms the value of a field to an byte array.
     * The first byte indicates the type of the value (see {@link de.zortax.pra.network.serialization.impl.TypeCode TypeCode}),
     * followed by the length of the name of the field as an unsigned short (in two bytes). Then the name of the field
     * comes and finally the length of the content as an unsigned short (in two bytes) with the serialized Content at the
     * end of the byte Array.
     * @param f The Field which should be serialized
     * @param instance The instance of the object containing the field
     * @return The Serialized field
     * @throws IllegalAccessException
     */
    byte[] toBytes(Field f, Object instance) throws IllegalAccessException;

    /**
     *
     * @param bytes an byte array created by {@link FieldSerializer#toBytes(Field, Object)}
     * @return the name of the field serialized in the given byte array
     */
    String getFieldName(byte[] bytes);

    /**
     *
     * @param bytes an byte array created by {@link FieldSerializer#toBytes(Field, Object)}
     * @return The value of the field serialized in the given byte array
     */
    T getValue(byte[] bytes);

    /**
     *
     * @param allData an byte Array containing multiple serialized fields
     * @param index indicates where the two start reading
     * @return the byte array representing one serialized field from allData beginning at the given index
     */
    byte[] getBlockFrom(byte[] allData, int index);

}
