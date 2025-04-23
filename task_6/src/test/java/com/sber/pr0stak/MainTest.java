package com.sber.pr0stak;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest{
    @Test
    public void testMultiplyWithOddNumber() {
        Multiplication calculator = new Multiplication();
        assertThrows(ParityException.class, () -> calculator.multiply(3, 2));
        assertThrows(ParityException.class, () -> calculator.multiply(4, 5));
    }

    @Test
    public void testMultiplyWithEvenNumbers() throws ParityException {
        Multiplication calculator = new Multiplication();
        int result = calculator.multiply(4, 6);
        assertEquals(24, result);
    }
}

