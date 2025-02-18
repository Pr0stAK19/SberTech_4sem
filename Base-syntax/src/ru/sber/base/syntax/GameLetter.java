package ru.sber.base.syntax;

import java.util.Random;
import java.util.Scanner;

public class GameLetter {
    public static void main(String[] args) {
        Random random = new Random();
        char targetLetter = (char) (random.nextInt(26) + 'A'); // Загаданная буква от A до Z
        Scanner scanner = new Scanner(System.in);
        char guessedLetter;

        System.out.println("Угадайте загаданную букву (A-Z):");

        while (true) {
            guessedLetter = scanner.nextLine().toUpperCase().charAt(0);

            if (guessedLetter == targetLetter) {
                System.out.println("Right!");
                break;
            } else if (guessedLetter < targetLetter) {
                System.out.println("You’re too low.");
            } else {
                System.out.println("You’re too high.");
            }
        }

        scanner.close();
    }
}
