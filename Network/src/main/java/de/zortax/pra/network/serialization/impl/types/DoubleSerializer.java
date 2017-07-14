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

package de.zortax.pra.network.serialization.impl.types;//  Created by leo on 14.07.17.

import de.zortax.pra.network.serialization.FieldSerializer;
import de.zortax.pra.network.serialization.impl.TypeCodes;
import de.zortax.pra.network.serialization.impl.Util;

import java.lang.reflect.Field;

public class DoubleSerializer implements FieldSerializer<Double> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        return Util.toBytes(TypeCodes.DOUBLE, f.getName(), toByteArray((double) f.get(instance)));
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Double getValue(byte[] bytes) {
        return fromByteArray(Util.getValueBytes(bytes, 8));
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 8);
    }

    public static byte[] toByteArray(double value) {
        Double d = new Double(value);
        return new byte[] {
                (byte) (d.byteValue() >>> 56),
                (byte) (d.byteValue() >>> 48),
                (byte) (d.byteValue() >>> 40),
                (byte) (d.byteValue() >>> 32),
                (byte) (d.byteValue() >>> 24),
                (byte) (d.byteValue() >>> 16),
                (byte) (d.byteValue() >>> 8),
                d.byteValue()
        };
    }

    public static double fromByteArray(byte[] array) {
        return (array[0] << 56
                | (array[1] & 0xFF) << 48
                | (array[2] & 0xFF) << 40
                | (array[3] & 0xFF) << 32
                | (array[4] & 0xFF) << 24
                | (array[5] & 0xFF) << 16
                | (array[6] & 0xFF) << 8
                | (array[7] & 0xFF)
        );
    }

}
