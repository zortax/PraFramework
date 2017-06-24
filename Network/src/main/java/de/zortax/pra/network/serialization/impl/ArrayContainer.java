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

package de.zortax.pra.network.serialization.impl;// Created by leo on 24.06.17

import java.util.ArrayList;

public class ArrayContainer<T> {

    private ArrayList<T> elements;

    public ArrayContainer() {
        this.elements = new ArrayList<>();
    }

    public void addElement(T element) {
        this.elements.add(element);
    }

    public T[] getArray() {
        return (T[]) elements.toArray();
    }

    public static byte[] toByteArray(Byte[] array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++)
            bytes[i] = array[i];
        return bytes;
    }

    public static short[] toShortArray(Short[] array) {
        short[] shorts = new short[array.length];
        for (int i = 0; i < array.length; i++)
            shorts[i] = array[i];
        return shorts;
    }

    public static int[] toIntArray(Integer[] array) {
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++)
            ints[i] = array[i];
        return ints;
    }

    public static long[] toLongArray(Long[] array) {
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++)
            longs[i] = array[i];
        return longs;
    }

    public static float[] toFloatArray(Float[] array) {
        float[] floats = new float[array.length];
        for (int i = 0; i < array.length; i++)
            floats[i] = array[i];
        return floats;
    }

    public static double[] toDoubleArray(Double[] array) {
        double[] doubles = new double[array.length];
        for (int i = 0; i < array.length; i++)
            doubles[i] = array[i];
        return doubles;
    }

    public static char[] toCharArray(Character[] array) {
        char[] chars = new char[array.length];
        for (int i = 0; i < array.length; i++)
            chars[i] = array[i];
        return chars;
    }

    public static boolean[] toBooleanArray(Boolean[] array) {
        boolean[] booleans = new boolean[array.length];
        for (int i = 0; i < array.length; i++)
            booleans[i] = array[i];
        return booleans;
    }

}
