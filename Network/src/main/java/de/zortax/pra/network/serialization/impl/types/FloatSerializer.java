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

public class FloatSerializer implements FieldSerializer<Float> {

    @Override
    public byte[] toBytes(Field f, Object instance) throws IllegalAccessException {
        return Util.toBytes(TypeCodes.FLOAT, f.getName(), toByteArray((float) f.get(instance)));
    }

    @Override
    public String getFieldName(byte[] bytes) {
        return Util.getFieldName(bytes);
    }

    @Override
    public Float getValue(byte[] bytes) {
        return fromByteArray(Util.getValueBytes(bytes, 4));
    }

    @Override
    public byte[] getBlockFrom(byte[] allData, int index) {
        return Util.getBlock(allData, index, 4);
    }

    public static byte[] toByteArray(float value) {
        Float f = new Float(value);
        return new byte[] {
                (byte) (f.byteValue() >>> 24),
                (byte) (f.byteValue() >>> 16),
                (byte) (f.byteValue() >>> 8),
                f.byteValue()
        };
    }

    public static float fromByteArray(byte[] array) {
        return (array[0] << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8) | (array[3] & 0xFF);
    }

}
