package com.Sber.Pr0stAK.ex3;

abstract class Figure {
    protected Point point;

    public Figure(Point point) {
        this.point = point;
    }

    abstract double area();
    abstract double perimeter();
}
