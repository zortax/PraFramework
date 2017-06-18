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
import de.zortax.pra.network.serialization.SerializationUtils;
import de.zortax.pra.network.serialization.TypeMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class StringSerializer implements ClassSerializer<String> {

    private MappingManager mappingManager;
    private String charset;

    StringSerializer(MappingManager mappingManager, String charset) {
        this.mappingManager = mappingManager;
        this.charset = charset;
    }

    @Override
    public byte[] getBytes(String instance, TypeMapping mapping) throws UnsupportedEncodingException {
        ArrayList<Byte>  bytes = new ArrayList<>();
        for (byte b : mappingManager.getCode(mapping))
            bytes.add(b);
        for (byte b : instance.getBytes(charset))
            bytes.add(b);
        return SerializationUtils.toByteArray(bytes);
    }

    @Override
    public String getInstance(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, charset);
    }

}
