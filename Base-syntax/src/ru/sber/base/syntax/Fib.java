package ru.sber.base.syntax;

import java.util.Scanner;

public class Fib {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите число членов (от 2 до 100): ");
        int n = scanner.nextInt();
        int a = 0;
        int b = 1;
        int sum;
        System.out.println(a);
        System.out.println(b);
        for (int i = 2; i < n; i++){
            sum = a + b;
            System.out.println(sum);
            a = b;
            b = sum;
        }
        scanner.close();
    }
}