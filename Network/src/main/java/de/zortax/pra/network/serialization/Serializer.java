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

package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

import de.zortax.pra.network.error.ExceptionHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Serializer {

    private boolean useFieldNames;
    private MappingManager mappingManager;

    Serializer(boolean useFieldNames, MappingManager mappingManager) {
        this.useFieldNames = useFieldNames;
        this.mappingManager = mappingManager;
    }


    public byte[] serialize(Object obj) {
        return serialize(obj, null);
    }

    public byte[] serialize(Object obj, String name) {

        try {

            if (mappingManager.isCustomMapped(obj)) {
                TypeMapping tm = useFieldNames && name != null ? mappingManager.getMappingFor(obj, name) : mappingManager.getMappingFor(obj);
                return mappingManager.getSerializer(obj.getClass()).getBytes(obj, tm);
            } else {

                ArrayList<Byte> allBytes = new ArrayList<>();
                if (useFieldNames && name != null) {
                    for (byte b : mappingManager.getDataTypeCode(name))
                        allBytes.add(b);
                } else {
                    for (byte b : mappingManager.getDataTypeCode())
                        allBytes.add(b);
                }
                ArrayList<Field> allFields = new ArrayList<>();
                allFields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
                Class currentClass = obj.getClass();
                while (currentClass.getSuperclass() != null) {
                    currentClass = currentClass.getSuperclass();
                    allFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                }

                for (Field f : allFields) {
                    for (byte b : useFieldNames ? serialize(f.get(obj), f.getName()) : serialize(f.get(obj))) {
                        allBytes.add(b);
                    }
                }

                for (byte b : mappingManager.getDataTypeCloser())
                    allBytes.add(b);

                byte[] bytes = new byte[allBytes.size()];
                for (int i = 0; i < allBytes.size(); i++)
                    bytes[i] = allBytes.get(i);

                return bytes;
            }

        } catch (Exception e) {
            ExceptionHandler.addException(e);
        }

        return null;
    }

    public <T>  T deserialize(byte[] data, Class<T> type) {

        return null;
    }

}
