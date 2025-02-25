package org.Ex1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

public class SortsTest {

    @Test
    public void testSortedArrayB() {
        double[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, arr);
    }

    @Test
    public void testEmptyArrayB() {
        double[] arr = {};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new double[]{}, arr);
    }

    @Test
    public void testDoubleArrayB() {
        double[] arr = {2, 11.5, 9.2, 3, 8.4, 7, 6.1, 5, 4.4, 1};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new double[]{1, 2, 3, 4.4, 5, 6.1, 7, 8.4, 9.2, 11.5}, arr);
    }


    @Test
    public void testArrayDuplicatesB() {
        double[] arr = {10, 3, 1, 2, 10, 3, 4, 7, 1, 9, 8};
        Sorts.bubbleSort(arr);
        assertArrayEquals(new double[]{1, 1, 2, 3, 3, 4, 7, 8, 9, 10, 10}, arr);
    }



    @Test
    public void testSortedArrayQ() {
        double[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Sorts.quickSort(arr, 0,arr.length - 1);
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, arr);
    }

    @Test
    public void testEmptyArrayQ() {
        double[] arr = {};
        Sorts.quickSort(arr, 0,arr.length - 1);
        assertArrayEquals(new double[]{}, arr);
    }

    @Test
    public void testDoubleArrayQ() {
        double[] arr = {2, 11.5, 9.2, 3, 8.4, 7, 6.1, 5, 4.4, 1};
        Sorts.quickSort(arr,0,arr.length - 1);
        assertArrayEquals(new double[]{1, 2, 3, 4.4, 5, 6.1, 7, 8.4, 9.2, 11.5}, arr);
    }


    @Test
    public void testArrayDuplicatesQ() {
        double[] arr = {10, 3, 1, 2, 10, 3, 4, 7, 1, 9, 8};
        Sorts.quickSort(arr, 0,arr.length - 1);
        assertArrayEquals(new double[]{1, 1, 2, 3, 3, 4, 7, 8, 9, 10, 10}, arr);
    }
}
