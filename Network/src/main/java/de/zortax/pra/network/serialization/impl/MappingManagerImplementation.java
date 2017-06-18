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

package de.zortax.pra.network.serialization.impl;//  Created by leo on 16.06.17.

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.MappingManager;
import de.zortax.pra.network.serialization.TypeMapping;

public class MappingManagerImplementation implements MappingManager<Short> {

    private StringSerializer stringSerializer;
    private ByteSerializer byteSerializer;
    private IntegerSerializer integerSerializer;
    private BooleanSerializer booleanSerializer;
    private ShortSerializer shortSerializer;
    private LongSerializer longSerializer;

    public MappingManagerImplementation(String charset) {
        stringSerializer = new StringSerializer(this, charset);
        byteSerializer = new ByteSerializer(this);
        integerSerializer = new IntegerSerializer();
        booleanSerializer = new BooleanSerializer();
        shortSerializer = new ShortSerializer();
        longSerializer = new LongSerializer();
    }

    @Override
    public TypeMapping<Short> getMappingFor(Object object) {


        return null;
    }

    @Override
    public TypeMapping<Short> getMappingFor(Object object, String name) {
        return null;
    }

    @Override
    public byte[] getCode(TypeMapping<Short> mapping) {
        return new byte[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> ClassSerializer<V> getSerializer(Class<V> type) {
        if (type.equals(String.class))
            return (ClassSerializer<V>) stringSerializer;
        else if (type.equals(Byte.class))
            return (ClassSerializer<V>) byteSerializer;
        else if (type.equals(Integer.class))
            return (ClassSerializer<V>) integerSerializer;
        else if (type.equals(Boolean.class))
            return (ClassSerializer<V>) booleanSerializer;
        else if (type.equals(Short.class))
            return (ClassSerializer<V>) shortSerializer;
        else if (type.equals(Long.class))
            return (ClassSerializer<V>) longSerializer;
        else
            return null;
    }

    @Override
    public TypeMapping<Short> parseMapping(byte[] bytes) {
        return null;
    }

    @Override
    public boolean isCustomMapped(Object obj) {
        return false;
    }

    @Override
    public byte[] getDataTypeCode(String name) {
        return new byte[0];
    }

    @Override
    public byte[] getDataTypeCode() {
        return new byte[0];
    }

    @Override
    public byte[] getDataTypeCloser() {
        return new byte[0];
    }
}
