package com.Sber.Pr0stAK.ex3;

public class PaintExample {
    public static void main(String[] args) {
        Point p = new Point(0, 0);
        CircleDrawable circle = new CircleDrawable(p, 5);

        Point h = new Point(0, 0);
        TriangleDrawable triangl = new TriangleDrawable(h, 1, 1.2, 2);

        FigureUtil.draw(circle);
        FigureUtil.draw(circle, Color.RED);

        System.out.println("Area: " + FigureUtil.area(circle));
        System.out.println("Perimeter: " + FigureUtil.perimeter(circle));

        FigureUtil.draw(triangl);
        FigureUtil.draw(triangl, Color.GREEN);

        System.out.println("Area: " + FigureUtil.area(triangl));
        System.out.println("Perimeter: " + FigureUtil.perimeter(triangl));
    }
}