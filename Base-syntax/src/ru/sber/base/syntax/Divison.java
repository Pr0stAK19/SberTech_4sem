package ru.sber.base.syntax;

import java.util.Scanner;

public class Divison {

    public static void main( String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Введите натуральные делимое и делитель");
        int a = in.nextInt();
        int b = in.nextInt();

        if (b != 0) {
            int c = a / b;
            System.out.println("Частное:  " +  c + " Остаток: " + (a % b));
        }
        else
            System.out.println("На ноль делить нельзя");
        in.close();
    }
}
