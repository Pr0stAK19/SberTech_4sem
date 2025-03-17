package com.Sber.Pr0stAK.ex4.ex3;

class FigureUtil {
    private FigureUtil() {}

    public static double area(Figure figure) { return figure.area(); }
    public static double perimeter(Figure figure) { return figure.perimeter(); }
    public static void draw(Figure figure) { ((Drawable) figure).draw(); }
    public static void draw(Figure figure, Color color) { ((Drawable) figure).draw(color); }
}
