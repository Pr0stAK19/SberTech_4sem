package com.sber.pr0stak;

import java.sql.*;
import java.util.Scanner;

public class ToDoListApp {
    private static final String URL = "jdbc:h2:mem:testdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createTable(connection);
            Scanner scanner = new Scanner(System.in);
            String command;

            while (true) {
                System.out.print("Введите команду (add, show, done, exit): ");
                command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "add":
                        addTask(connection, scanner);
                        break;
                    case "show":
                        showTasks(connection);
                        break;
                    case "done":
                        markTaskDone(connection, scanner);
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Неизвестная команда.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), status VARCHAR(10))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица задач создана!");
        }
    }

    private static void addTask(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();
        String sql = "INSERT INTO tasks (name, status) VALUES (?, 'Pending')";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
            System.out.println("Задача добавлена!");
        }
    }

    private static void showTasks(Connection connection) throws SQLException {
        String sql = "SELECT * FROM tasks";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Список задач:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                System.out.printf("ID: %d, Имя: %s, Статус: %s%n", id, name, status);
            }
        }
    }

    private static void markTaskDone(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Введите ID задачи для завершения: ");
        int id = Integer.parseInt(scanner.nextLine());
        String sql = "UPDATE tasks SET status = 'Done' WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Задача завершена!");
            } else {
                System.out.println("Задача с таким ID не найдена.");
            }
        }
    }
}
