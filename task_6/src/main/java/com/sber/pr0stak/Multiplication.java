package com.sber.pr0stak;

class Multiplication {
    public int multiply(int a, int b) throws ParityException{
        if (a % 2 !=0 || b % 2 !=0){
            throw new ParityException("Одно из введенных чисел нечетное: " + a + ", " + b);
        }
        return a * b;
    }
}
