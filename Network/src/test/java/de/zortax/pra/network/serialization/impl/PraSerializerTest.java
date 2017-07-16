package de.zortax.pra.network.serialization.impl;

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



        ComplexTest complexTest = new ComplexTest((byte) 3, true, 'c', (short) 13,42, 30001, 3.4f, 3.37d, "foo");
        byte[] complexBytes = serializer.serialize(complexTest);
        ComplexTest d = serializer.deserialize(complexBytes, ComplexTest.class);
        Assert.assertEquals(complexTest, d);
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
                String stringVal) {
            this.byteVal = byteVal;
            this.booleanVal = booleanVal;
            this.charVal = charVal;
            this.shortVal = shortVal;
            this.intVal = intVal;
            this.longVal = longVal;
            this.floatVal = floatVal;
            this.doubleVal = doubleVal;
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
                        && stringVal.equals(t.stringVal);
            } else return false;

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