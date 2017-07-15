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

public class ArrayContainer {

    private final Object array;
    private final int dimensions;

    public ArrayContainer(Object array) {
        if (!array.getClass().isArray())
            throw new IllegalArgumentException("Object must be an array!");
        this.array = array;
        this.dimensions = Util.getArrayDimensions(array);
    }
    
    public Object getArray() {
        return array;
    }
    
    public <T> T getArray(Class<T> castType) {
        return castType.cast(array);
    }

    public int getDimensions() {
        return dimensions;
    }

}
