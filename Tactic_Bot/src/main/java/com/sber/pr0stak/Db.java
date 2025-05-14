package com.sber.pr0stak;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Db {
    private final Connection connection;

    public Db() throws SQLException {
        // Используйте более надежный URL для H2, например с file:
        connection = DriverManager.getConnection("jdbc:h2:file:./financeDB;AUTO_SERVER=TRUE", "sa", "");

        createTable();
    }

    private void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS finance (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    chat_id VARCHAR(50),
                    record_date DATE,
                    reason VARCHAR(255),
                    amount DOUBLE
                )
                """;
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        }
    }

    public void addRecord(String chatId, Date date, String reason, double amount) throws SQLException {
        String sql = "INSERT INTO finance (chat_id, record_date, reason, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, chatId);
            ps.setDate(2, date);
            ps.setString(3, reason);
            ps.setDouble(4, amount);
            ps.executeUpdate();
        }
    }

    public List<Map<String, Object>> getRecordsForDate(String chatId, Date date) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        // Добавлен id и record_date для возможности редактирования/удаления
        String sql = "SELECT id, record_date, reason, amount FROM finance WHERE chat_id = ? AND record_date = ? ORDER BY record_date";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, chatId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("record_date", rs.getDate("record_date"));
                    row.put("reason", rs.getString("reason"));
                    row.put("amount", rs.getDouble("amount"));
                    result.add(row);
                }
            }
        }
        return result;
    }

    // Новый метод для получения записей за временной промежуток
    public List<Map<String, Object>> getRecordsForDateRange(String chatId, Date startDate, Date endDate) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT id, record_date, reason, amount FROM finance WHERE chat_id = ? AND record_date BETWEEN ? AND ? ORDER BY record_date";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, chatId);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("record_date", rs.getDate("record_date"));
                    row.put("reason", rs.getString("reason"));
                    row.put("amount", rs.getDouble("amount"));
                    result.add(row);
                }
            }
        }
        return result;
    }

    public boolean updateRecord(String chatId, int id, Date date, String reason, double amount) throws SQLException {
        // Исправлено: Использование имени таблицы finance
        String sql = "UPDATE finance SET record_date = ?, reason = ?, amount = ? WHERE id = ? AND chat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, date);
            stmt.setString(2, reason);
            stmt.setDouble(3, amount);
            stmt.setInt(4, id);
            stmt.setString(5, chatId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    public boolean deleteRecord(String chatId, int id) throws SQLException {
        // Исправлено: Использование имени таблицы finance
        String sql = "DELETE FROM finance WHERE id = ? AND chat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, chatId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}