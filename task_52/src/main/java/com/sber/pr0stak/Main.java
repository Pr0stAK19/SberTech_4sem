package com.sber.pr0stak;

import java.sql.SQLOutput;

public class Main {
    public static void maim(String[] arguments){
        GenericCollection<Person> People = new GenericCollection<>();

        People.add(new Person("Andrey",20));
        People.add(new Person("Ksenya",19));
        People.add(new Person("Anton",18));
        People.add(new Person("Vadim",19));

        Person Select = People.get(3);
        System.out.println("Элемент по индексу 3: " + Select);

        System.out.println("Размер коллекции: " + People.size());

        People.remove(3);

        System.out.println("Размер коллекции: " + People.size());

        System.out.println("Все элементы коллекции:");
        for (int i = 0; i < People.size(); i++){
            System.out.println(People.get(i));
        }
    }
}
