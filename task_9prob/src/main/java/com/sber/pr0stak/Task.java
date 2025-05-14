package com.sber.pr0stak;

// Класс для представления задачи
public class Task {
    private int uid;
    private String name;
    private String status; // Например: "PENDING", "DONE"

    // Конструктор для создания объекта задачи
    public Task(int uid, String name, String status) {
        this.uid = uid;
        this.name = name;
        this.status = status;
    }

    // Геттеры для доступа к полям задачи
    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    // Переопределяем toString для удобного вывода задачи
    @Override
    public String toString() {
        return String.format("[%d] %s - %s", uid, name, status);
    }
}
