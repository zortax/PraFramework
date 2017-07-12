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

    private static final transient Gson gson = new Gson();
    private static transient Serializer serializer = new PraSerializer();
    private final Long timestamp = System.currentTimeMillis();
    private boolean requestFlag = false;
    private String requestID = "";
    private String source = "";

    @Override
    public String toString() {
        return "<HEADER>" + this.getClass().getName() + "</HEADER>:" + gson.toJson(this);
    }


    /**
     * @return the data that actually being send
     */
    public byte[] getBytes() {
        try {
            return serializer.serialize(this);
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
        return fromJson(new String(bytes));
    }

    /**
     * Default Packet deserialization
     * @param rawPacket raw packet (header + json)
     * @return PraPacket instance created
     * @throws Exception That's probably gonna work...
     */
    public static PraPacket fromJson(String rawPacket) throws Exception  {
        if (!rawPacket.contains("<HEADER>") || !rawPacket.contains("</HEADER>:")) {
            throw new IllegalArgumentException("Wrong Packet format, doesn't contain header!");
        }
        String[] split = rawPacket.split(":", 2);
        if (split.length != 2) {
            throw new IllegalArgumentException("Wrong Packet format!");
        }
        split[0] = split[0].replace("<HEADER>", "").replace("</HEADER>", "");
        Class<?> clazzUnknown = Class.forName(split[0]);

        if (!clazzUnknown.getSuperclass().equals(PraPacket.class)) {
            throw new IllegalArgumentException("Class doesn't extend PraPacket!");
        }

        Class<? extends PraPacket> clazz = (Class<? extends PraPacket>) clazzUnknown;
        return getPacket(split[1], clazz);

    }

    /**
     * @param rawPacket raw packet (header + json)
     * @return the actual packet json without the header
     */
    public static String getPacketSerial(String rawPacket) {
        if (!rawPacket.contains("<HEADER>") || !rawPacket.contains("</HEADER>:")) {
            return rawPacket;
        } else {
            return rawPacket.split(":", 2)[1];
        }
    }

    /** Creates an instance of the packet class with json
     * @param serial the JSON the instance is created with
     * @param clazz the packet class
     * @param <T> type of the packet
     * @return created instance of clazz
     */
    public static <T extends PraPacket> T getPacket(String serial, Class<T> clazz) {
        return gson.fromJson(serial, clazz);
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

