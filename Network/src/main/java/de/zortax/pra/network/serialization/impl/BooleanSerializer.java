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

package de.zortax.pra.network.serialization.impl;// Created by leo on 17.06.17

import de.zortax.pra.network.serialization.ClassSerializer;
import de.zortax.pra.network.serialization.TypeMapping;

public class BooleanSerializer implements ClassSerializer<Boolean> {

    @Override
    public byte[] getBytes(Boolean instance, TypeMapping mapping) throws Exception {
        return new byte[0];
    }

    @Override
    public Boolean getInstance(byte[] bytes) throws Exception {
        return bytes[0] == MappingCodes.TRUE.getCode();
    }
}
