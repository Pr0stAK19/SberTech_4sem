//package com.sber.pr0stak.sql_query;
//
//import com.sber.pr0stak.Connection;
//import com.sber.pr0stak.SQLException;
//
//public class PreparedStatement {
//    String sql = "INSERT INTO tasks (name, status) VALUES (?, ?)";
//    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//        pstmt.setString(1, "New Task");
//        pstmt.setString(2, "active");
//        pstmt.executeUpdate();
//    }
//
//}
