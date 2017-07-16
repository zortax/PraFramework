package de.zortax.pra.network.serialization.impl.types;

import org.junit.Assert;
import org.junit.Test;

// Created by leo on 16.07.17
public class DoubleSerializerTest {

    public static final double TEST_VALUE = 3.9223D;

    @Test
    public void toByteArray() throws Exception {
        byte[] bytes = DoubleSerializer.toByteArray(TEST_VALUE);
    }

    @Test
    public void fromByteArray() throws Exception {
        byte[] bytes = DoubleSerializer.toByteArray(TEST_VALUE);
        double val = DoubleSerializer.fromByteArray(bytes);
        Assert.assertEquals(TEST_VALUE, val, 0);
    }

}