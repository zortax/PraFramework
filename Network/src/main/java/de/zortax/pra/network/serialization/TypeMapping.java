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

public class TypeMapping<T> {

    private byte code;
    private T valueSize;
    private T nameSize;

    public TypeMapping(byte code, T valueSize) {
        this.code = code;
        this.valueSize = valueSize;
    }

    public TypeMapping(byte code, T nameSize, T valueSize) {
        this.code = code;
        this.nameSize = nameSize;
        this.valueSize = valueSize;
    }

    public byte getCode() {
        return code;
    }

    public T getValueSize() {
        return valueSize;
    }

    public T getNameSize() {
        return nameSize;
    }

}
