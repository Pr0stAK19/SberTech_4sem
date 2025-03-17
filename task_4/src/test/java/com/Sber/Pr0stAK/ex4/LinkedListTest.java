package com.Sber.Pr0stAK.ex4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {
    @Test
    void testAddAndContains() {
        LinkedList list = new LinkedList();
        list.add(1);
        assertTrue(list.contains(1));
        assertFalse(list.contains(2));
    }

    @Test
    void testRemove() {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        list.remove(1);
        assertFalse(list.contains(1));
        assertTrue(list.contains(2));
    }

    @Test
    void testDisplay() {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        assertEquals("1 -> 2 -> null", list.display());
    }
}
