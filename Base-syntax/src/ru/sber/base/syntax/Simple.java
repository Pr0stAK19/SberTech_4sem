package ru.sber.base.syntax;

import java.util.Scanner;

public class Simple {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите число: ");
        double number = scanner.nextInt();
        int del = 0;
        int i = 2;
        Boolean flag = Boolean.TRUE;
        while ((del < 1) && flag ) {
                if (i <Math.sqrt(number)) {
                    if (number % i != 0) {
                        i++;
                    } else {
                        del++;
                    }
                }
                else{
                    flag = Boolean.FALSE;
                }

        }
        if ((del < 1) && (number > 0)) {
            System.out.println("Число простое");
        }
        else {
            System.out.println("Число составное/не натуральное");
        }
        scanner.close();
    }
}



