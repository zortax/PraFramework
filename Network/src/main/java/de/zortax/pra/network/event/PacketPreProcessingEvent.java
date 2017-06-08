package de.zortax.pra.network.event;//  Created by Leo on 29.04.2017.

import de.zortax.pra.network.api.Client;

/**
 * Gets called before raw packet data is going to be deserialized automatically
 */
public class PacketPreProcessingEvent implements Event {

    private byte[] rawData;
    private Client source;

    public PacketPreProcessingEvent(byte[] rawData, Client source) {
        this.rawData = rawData;
        this.source = source;
    }

    /**
     * @return the raw packet data
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * @return the client that sent this packet
     */
    public Client getSourceClient() {
        return source;
    }

    /**
     * Sets the raw packet data
     * @param rawData the new raw data
     */
    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

}
