package com.sber.pr0stak;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Используем Optional для получения задачи по ID

// Класс, отвечающий за взаимодействие с базой данных H2
public class TaskRepository {

    private static final String DB_URL = "jdbc:h2:./todo_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public TaskRepository() {
        // При создании репозитория сразу инициализируем базу данных
        initializeDatabase();
    }

    // Инициализация базы данных и создание таблицы задач
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "uid INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "status VARCHAR(50) DEFAULT 'PENDING')";
            stmt.execute(sql);
            System.out.println("База данных H2 инициализирована.");

        } catch (SQLException e) {
            System.err.println("Ошибка инициализации базы данных: " + e.getMessage());
            // В продакшене здесь, возможно, стоит бросить исключение и остановить приложение
        }
    }

    // Добавление новой задачи в базу данных
    public boolean addTask(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Название задачи не может быть пустым.");
            return false;
        }
        String sql = "INSERT INTO tasks (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении задачи: " + e.getMessage());
            return false;
        }
    }

    // Получение всех задач из базы данных
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT uid, name, status FROM tasks ORDER BY status, uid";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                String status = rs.getString("status");
                tasks.add(new Task(uid, name, status));
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка задач: " + e.getMessage());
        }
        return tasks;
    }

    // Получение задачи по ID (используем Optional на случай, если задача не найдена)
    public Optional<Task> getTaskById(int uid) {
        String sql = "SELECT uid, name, status FROM tasks WHERE uid = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, uid);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int taskUid = rs.getInt("uid");
                    String name = rs.getString("name");
                    String status = rs.getString("status");
                    return Optional.of(new Task(taskUid, name, status));
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении задачи по ID: " + e.getMessage());
        }
        return Optional.empty(); // Задача не найдена
    }


    // Обновление статуса задачи по ID
    public boolean updateTaskStatus(int uid, String status) {
        if (status == null || status.trim().isEmpty()) {
            System.err.println("Статус задачи не может быть пустым.");
            return false;
        }
        // В идеале здесь нужно проверить, что статус является допустимым значением ("PENDING" или "DONE")
        String sql = "UPDATE tasks SET status = ? WHERE uid = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.trim().toUpperCase()); // Сохраняем статус в верхнем регистре
            pstmt.setInt(2, uid);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Вернет true, если задача была найдена и обновлена

        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса задачи: " + e.getMessage());
            return false;
        }
    }

    // Удаление задачи по ID
    public boolean deleteTask(int uid) {
        String sql = "DELETE FROM tasks WHERE uid = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, uid);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Вернет true, если задача была найдена и удалена

        } catch (SQLException e) {
            System.err.println("Ошибка при удалении задачи: " + e.getMessage());
            return false;
        }
    }
}