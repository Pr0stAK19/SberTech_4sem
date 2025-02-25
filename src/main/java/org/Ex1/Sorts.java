package org.Ex1;

public class Sorts {

    public static void bubbleSort(double[] sortArr) {
        for (int i = 0; i < sortArr.length - 1; i++) {
            for(int j = 0; j < sortArr.length - i - 1; j++) {
                if(sortArr[j + 1] < sortArr[j]) {
                    double swap = sortArr[j];
                    sortArr[j] = sortArr[j + 1];
                    sortArr[j + 1] = swap;
                }
            }
        }
    }

    public static void quickSort (double[] sortArr, int low, int high) {
        if (sortArr.length == 0 || low >= high) return;
        int middle = low + (high - low) / 2;
        double border = sortArr[middle];
        int i = low, j = high;
        while (i <= j) {
            while (sortArr[i] < border) {
                i++;
            }
            while (sortArr[j] > border) {
                j--;
            }
            if (i <= j) {
                double temp = sortArr[i];
                sortArr[i] = sortArr[j];
                sortArr[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j) quickSort(sortArr, low, j);
        if (high > i) quickSort(sortArr, i, high);
    }
}
