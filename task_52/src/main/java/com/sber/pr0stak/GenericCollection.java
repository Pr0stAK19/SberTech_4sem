package com.sber.pr0stak;

import java.util.ArrayList;

public class GenericCollection<T> {
    private ArrayList<T> ExampleList = new ArrayList<>();

    public void add(T example) {
        ExampleList.add(example);
    }

    public void remove(int index){
        if (index >=0 && index <=ExampleList.size()) {
            ExampleList.remove(index);
        }
        else
            System.out.println("Индекс вне диапазона");
    }

    public T get(int index){
        if (index >=0 && index <=ExampleList.size()) {
            return  ExampleList.get(index);
        }
        else
            System.out.println("Индекс вне диапазона");
            return null;
    }

    public int size(){
        return ExampleList.size();
    }

    public void PrintAll(){
        for (T example : ExampleList){
            System.out.println(example);
        }
    }
}
