package com.sber.pr0stak;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ClassAnalyzer <full-class-name>");
            return;
        }

        String className = args[0];
        try {
            Class<?> clazz = Class.forName(className);
            printClassInfo(clazz);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
    }

    private static void printClassInfo(Class<?> clazz) {
        System.out.println("Class: " + clazz.getName());
        int modifiers = clazz.getModifiers();
        System.out.println("Modifiers: " + Modifier.toString(modifiers));

        System.out.println("\nFields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldInfo = String.format("  %s %s: %s",
                    Modifier.toString(field.getModifiers()), field.getType().getSimpleName(), field.getName());
            System.out.println(fieldInfo);
        }

        System.out.println("\nMethods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String methodInfo = String.format("  %s %s %s(%s)",
                    Modifier.toString(method.getModifiers()), method.getReturnType().getSimpleName(),
                    method.getName(), getParameterTypes(method));
            System.out.println(methodInfo);
        }
    }

    private static String getParameterTypes(Method method) {
        StringBuilder params = new StringBuilder();
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            params.append(paramTypes[i].getSimpleName());
            if (i < paramTypes.length - 1) {
                params.append(", ");
            }
        }
        return params.toString();
    }
}

