package com.Sber.Pr0stAK.ex3;

class Circle extends Figure {
    private double radius;

    public Circle(Point point, double radius) {
        super(point);
        this.radius = radius;
    }

    @Override
    double area() { return Math.PI * radius * radius; }

    @Override
    double perimeter() { return 2 * Math.PI * radius; }
}

class Rectangle extends Figure {
    private double width;
    private double height;

    public Rectangle(Point point, double width, double height) {
        super(point);
        this.width = width;
        this.height = height;
    }

    @Override
    double area() { return width * height; }

    @Override
    double perimeter() { return 2 * (width + height); }
}

class Square extends Rectangle {
    public Square(Point point, double side) {
        super(point, side, side);
    }
}

class Triangle extends Figure {
    private double a, b, c;

    public Triangle(Point point, double a, double b, double c) {
        super(point);
        this.a = a; this.b = b; this.c = c;
    }

    @Override
    double area() {
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    double perimeter() { return a + b + c; }
}



class CircleDrawable extends Circle implements Drawable {
    public CircleDrawable(Point point, double radius) {
        super(point, radius);
    }

    public void draw() { System.out.println("Drawing Circle in BLACK"); }
    public void draw(Color color) { System.out.println("Drawing Circle in " + color); }
}

class TriangleDrawable extends Triangle implements Drawable {
    public TriangleDrawable(Point point, double a, double b, double c) {
        super(point,a, b, c);
    }

    public void draw() { System.out.println("Drawing Triangle in BLACK"); }
    public void draw(Color color) { System.out.println("Drawing Triangle in " + color); }
}



