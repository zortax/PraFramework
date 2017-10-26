package de.zortax.pra.network.serialization.impl.types;

import de.zortax.pra.network.serialization.impl.PraSerializer;
import de.zortax.pra.network.serialization.impl.Util;
import org.junit.Assert;
import org.junit.Test;

// Created by leo on 16.07.17
public class ArraySerializerTest {

    private final PraSerializer serializer = new PraSerializer();

    @Test
    public void arrayLength(){
        int[][][] testArray = new int[1][1][1];
        int dimensions = Util.getArrayDimensions(testArray);
        Assert.assertEquals(dimensions, 3);
    }
    @Test
    public void toByteArray() throws Exception {

        int[] intArray = new int[]{1, 2, 3, 4, 42, 7};
        byte[] intBytes = ArraySerializer.toByteArray(intArray, serializer);

        int[][] intIntArray = new int[][]{{1, 2, 3}, {42, 7, 13}};
        byte[] intIntByte = ArraySerializer.toByteArray(intIntArray, serializer);

    }

    @Test
    public void fromByteArray() throws Exception {

        int[] intArray = new int[]{1, 2, 3, 4, 42, 7};
        byte[] intBytes = ArraySerializer.toByteArray(intArray, serializer);
        int[] deserializedInts = ArraySerializer.fromByteArray(intBytes, int[].class, serializer).getArray(int[].class);
        Assert.assertArrayEquals(intArray, deserializedInts);

        int[][] intIntArray = new int[][]{{1, 2, 3}, {42, 7, 13}};
        byte[] intIntBytes = ArraySerializer.toByteArray(intIntArray, serializer);
        int[][] deserializedIntInts = ArraySerializer.fromByteArray(intIntBytes, int[][].class, serializer).getArray(int[][].class);
        for (int i = 0; i < intIntArray.length; i++)
            Assert.assertArrayEquals(intIntArray[i], deserializedIntInts[i]);
    }

}
