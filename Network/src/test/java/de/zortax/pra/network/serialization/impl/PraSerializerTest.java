package de.zortax.pra.network.serialization.impl;

import de.zortax.pra.network.packets.HandshakePacket;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

// Created by leo on 16.07.17
public class PraSerializerTest {
    @Test
    public void serialize() throws Exception {
        testSerialization();
    }

    @Test
    public void deserialize() throws Exception {
        testSerialization();
    }

    public void testSerialization() throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        PraSerializer serializer = new PraSerializer();

        int intVal = 10;
        byte[] intBytes = serializer.serialize(intVal);
        int t = serializer.deserialize(intBytes, int.class);
        Assert.assertEquals(intVal, t);

        ComplexTest complexTest = new ComplexTest((byte) 3, true, 'c', (short) 13,42, 30001, 3.4f, 3.37d, new float[]{10.3f, 2f, 5.776f}, "foo");
        byte[] complexBytes = serializer.serialize(complexTest);
        ComplexTest d = serializer.deserialize(complexBytes, ComplexTest.class);
        Assert.assertEquals(complexTest, d);

        ComplexTest complexTest2 = new ComplexTest((byte) 1, false, 'k', (short) 21, 7, 99999, 1.2f, 77.998d, new float[]{19,4f, 7.009f, 4.5f}, "bar");
        ComplexTest[][] complexArrayTest = new ComplexTest[][]{{complexTest, d}, {complexTest2}};
        byte[] complexArrayBytes = serializer.serialize(complexArrayTest);
        ComplexTest[][] dArray = serializer.deserialize(complexArrayBytes, ComplexTest[][].class);
        Assert.assertEquals(complexArrayTest.length, dArray.length);
        for (int i = 0; i < dArray.length; i++) {
            Assert.assertEquals(complexArrayTest[i].length, dArray[i].length);
            for (int j = 0; j < dArray[i].length; j++)
                Assert.assertEquals(complexArrayTest[i][j], dArray[i][j]);
        }

        HandshakePacket handshakePacket = new HandshakePacket("test_name", "test_version", "test_version2");
        handshakePacket.setRequestFlag(true);
        handshakePacket.setRequestID("foo_bar_blub");
        byte[] data = serializer.serialize(handshakePacket);
        HandshakePacket deserialize = serializer.deserialize(data, HandshakePacket.class);
        // check inherited fields
        Assert.assertEquals(handshakePacket.getRequestID(), deserialize.getRequestID());

        GenericTest<String> test = new GenericTest<>("n00b");
        byte[] genericBytes = serializer.serialize(test);
        GenericTest<String> deserialized = serializer.deserialize(genericBytes, GenericTest.class);
        Assert.assertEquals(test.getVar(), deserialized.getVar());

    }

    public static class GenericTest<T> {

        T var;

        public GenericTest(T var) {
            this.var = var;
        }

        public T getVar() {
            return var;
        }

    }

    public static class ComplexTest {

        byte byteVal;
        boolean booleanVal;
        char charVal;
        short shortVal;
        int intVal;
        long longVal;
        float floatVal;
        double doubleVal;
        float[] floatArray;
        String stringVal;

        public ComplexTest(
                byte byteVal,
                boolean booleanVal,
                char charVal,
                short shortVal,
                int intVal,
                long longVal,
                float floatVal,
                double doubleVal,
                float[] floatArray,
                String stringVal) {
            this.byteVal = byteVal;
            this.booleanVal = booleanVal;
            this.charVal = charVal;
            this.shortVal = shortVal;
            this.intVal = intVal;
            this.longVal = longVal;
            this.floatVal = floatVal;
            this.doubleVal = doubleVal;
            this.floatArray = floatArray;
            this.stringVal = stringVal;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ComplexTest) {
                ComplexTest t = (ComplexTest) obj;
                return byteVal == t.byteVal
                        && booleanVal == t.booleanVal
                        && charVal == t.charVal
                        && shortVal == t.shortVal
                        && intVal == t.intVal
                        && longVal == t.longVal
                        && floatVal == t.floatVal
                        && doubleVal == t.doubleVal
                        && compareArray(t.floatArray)
                        && stringVal.equals(t.stringVal);
            } else return false;

        }

        private boolean compareArray(float[] array) {
            if (array.length == floatArray.length) {
                for (int i = 0; i < array.length; i++)
                    if (array[i] != floatArray[i])
                        return false;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return byteVal + "\n"
                    + booleanVal + "\n"
                    + charVal + "\n"
                    + shortVal + "\n"
                    + intVal + "\n"
                    + longVal + "\n"
                    + floatVal + "\n"
                    + doubleVal + "\n"
                    + stringVal + "\n";
        }

    }

}