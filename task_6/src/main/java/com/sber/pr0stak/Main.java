package com.sber.pr0stak;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Multiplication calculator = new Multiplication();

        try {
            System.out.print("Введите первое четное число: ");
            int num1 = scanner.nextInt();
            System.out.print("Введите второе четное число: ");
            int num2 = scanner.nextInt();

            int result = calculator.multiply(num1, num2);
            System.out.println("Произведение четных чисел: " + result);
        } catch (ParityException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Введены некорректные данные.");
        } finally {
            System.out.println("Работа программы завершена.");
            scanner.close();
        }
    }
}

