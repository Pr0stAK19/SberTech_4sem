package ru.sber.base.syntax;

import java.util.Scanner;

public class Polindrom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите число: ");
        String input = scanner.nextLine();

        String cleanInput = input.replaceAll("[\\W]", "").toLowerCase();
        String reversedInput = new StringBuilder(cleanInput).reverse().toString();

        if (cleanInput.equals(reversedInput)) {
            System.out.println("Число является палиндромом.");
        } else {
            System.out.println("Число не является палиндромом.");
        }

        scanner.close();
    }
}
