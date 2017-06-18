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

import de.zortax.pra.network.serialization.impl.MappingManagerImplementation;

public class SerializerFactory {

    private boolean useFieldNames;
    private MappingManager mappingManager;

    SerializerFactory() {
        this.useFieldNames = true;
        this.mappingManager = new MappingManagerImplementation("UTF-8");
    }

    public SerializerFactory disableFieldNames() {
        this.useFieldNames = false;
        return this;
    }

    public SerializerFactory enableFieldNames() {
        this.useFieldNames = true;
        return this;
    }

    public SerializerFactory charset(String charset) {
        this.mappingManager = new MappingManagerImplementation(charset);
        return this;
    }

    public SerializerFactory setMappingManager(MappingManager mappingManager)  {
        this.mappingManager = mappingManager;
        return this;
    }

    public Serializer build() {
        Serializer serializer = new Serializer(useFieldNames, mappingManager);
        return serializer;
    }

}
