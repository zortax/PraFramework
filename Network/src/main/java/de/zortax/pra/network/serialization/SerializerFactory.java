package de.zortax.pra.network.serialization;//  Created by leo on 16.06.17.

public class SerializerFactory {

    private boolean useFieldNames;
    private MappingManager mappingManager;

    SerializerFactory() {
        this.useFieldNames = true;
    }

    public SerializerFactory disableFieldNames() {
        this.useFieldNames = false;
        return this;
    }

    public SerializerFactory enableFieldNames() {
        this.useFieldNames = true;
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
