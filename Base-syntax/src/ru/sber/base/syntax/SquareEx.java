package ru.sber.base.syntax;

import java.util.Scanner;

public class SquareEx {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите коэффициенты a, b и c:");
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        double c = scanner.nextDouble();

        if (a == 0) {
            System.out.println("Коэффициент 'a' не может быть равен нулю.");
            return;
        }

        double dis = b * b - 4 * a * c;

        if (dis > 0) {
            double root1 = (-b + Math.sqrt(dis)) / (2 * a);
            double root2 = (-b - Math.sqrt(dis)) / (2 * a);
            System.out.println("Корни уравнения: " + root1 + " и " + root2);
        } else if (dis == 0) {
            double root = -b / (2 * a);
            System.out.println("Единственный корень: " + root);
        } else {
            System.out.println("Корней нет.");
        }
        scanner.close();
    }
}
