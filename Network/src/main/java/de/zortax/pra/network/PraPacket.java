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

package de.zortax.pra.network;//  Created by Leonard on 03.03.2017.

import com.google.gson.Gson;
import de.zortax.pra.network.error.ExceptionHandler;
import de.zortax.pra.network.serialization.Serializer;
import de.zortax.pra.network.serialization.impl.PraSerializer;

import java.io.Serializable;

public abstract class PraPacket implements Serializable {

    private static transient Serializer serializer = new PraSerializer();
    private final Long timestamp = System.currentTimeMillis();
    private boolean requestFlag = false;
    private String requestID = "";
    private String source = "";

    /**
     * Sets the Serializer implementation used to (de-) serialize packets
     * @param serializer the Serializer instance
     */
    public static void setSerializer(Serializer serializer) {
        PraPacket.serializer = serializer;
    }

    /**
     * @return the data that actually being send
     */
    public byte[] getBytes() {
        try {
            String c = this.getClass().getName() + ">";
            byte[] serialized = serializer.serialize(this);
            byte[] bytes = new byte[c.getBytes().length + serialized.length];
            System.arraycopy(c.getBytes(), 0, bytes, 0, c.getBytes().length);
            System.arraycopy(serialized, 0, bytes, c.getBytes().length, serialized.length);
            return bytes;
        } catch (IllegalAccessException e) {
            ExceptionHandler.addException(e);
        }
        return null;
    }

    /**
     * @param bytes raw packet data
     * @return the PraPacket instance that was created from the bytes array
     * @throws Exception what could actually go wrong?
     */
    public static PraPacket fromBytes(byte[] bytes) throws Exception {
        String s = new String(bytes);
        String[] split = s.split(">");
        byte[] packet = new byte[bytes.length - (split[0].getBytes().length + 1)];
        System.arraycopy(bytes, bytes.length - packet.length, packet, 0, packet.length);
        return (PraPacket) serializer.deserialize(packet, Class.forName(split[0]));
    }

    /**
     * @return the time the packet was created on
     */
    public Long getTimestamp(){
        return timestamp;
    }

    /**
     * @return if this packet is a request packet or it's answer
     */
    public final boolean getRequestFlag() {
        return requestFlag;
    }

    /**
     * @param flag if this packet is a request packet or the answer to a request packet
     */
    public final void setRequestFlag(boolean flag) {
        this.requestFlag = flag;
    }

    /**
     * @return the ID of this request/answer
     */
    public final String getRequestID() {
        return requestID;
    }

    /**
     * @param requestID the ID of this request/answer
     */
    public final void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    /**
     * @return the original source of this packet
     */
    public String getSource() {
        return source;
    }
}

