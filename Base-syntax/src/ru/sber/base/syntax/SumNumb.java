package ru.sber.base.syntax;

import java.util.Scanner;

public class SumNumb {

    public static void main( String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите натуральное трёхзначное число: ");
        int n = scanner.nextInt();

        if (n < 100 || n > 999) {
            System.out.println("Число должно быть трёхзначным.");
        } else {
            int sum = 0;

            while (n > 0) {
                sum += n % 10;
                n /= 10;
            }
            System.out.println("Сумма цифр: " + sum);
        }

        scanner.close();
    }
}
