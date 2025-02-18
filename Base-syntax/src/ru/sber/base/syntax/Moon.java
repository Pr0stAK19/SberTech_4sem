package ru.sber.base.syntax;

import java.util.Scanner;

public class Moon {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ваш вес на Земле (кг): ");
        double earthWeight = scanner.nextDouble();

        double moonWeight = earthWeight * 0.17;

        System.out.println("Ваш вес на Луне: " + moonWeight + " кг");

        scanner.close();
    }
}

