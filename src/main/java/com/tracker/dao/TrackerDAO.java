package com.tracker.dao;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import com.tracker.util.DatabaseConnection;
import com.model.Tracker;
public class TrackerDAO {
    public List<Tracker> getAllTrackers() throws SQLException {
        List<Tracker> trackers = new ArrayList<>();
        String query = "SELECT * FROM expenses";
        try (Connection conn = DatabaseConnection.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Tracker tracker = new Tracker();
                tracker.setExpenseId(rs.getInt("expense_id"));
                tracker.setCategoryId(rs.getInt("category_id"));
                tracker.setType(rs.getString("type"));
                tracker.setAmount(rs.getFloat("amount"));
                tracker.setDescription(rs.getString("description"));
                tracker.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                tracker.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                trackers.add(tracker);
            }
        }
        return trackers;
    }
    public int addTracker(Tracker tracker) throws SQLException {
        String query = "INSERT INTO expenses (category_id, type, amount, description, expense_date, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tracker.getCategoryId());
            pstmt.setString(2, tracker.getType());
            pstmt.setFloat(3, tracker.getAmount());
            pstmt.setString(4, tracker.getDescription());
            pstmt.setDate(5, Date.valueOf(tracker.getExpenseDate()));
            pstmt.setTimestamp(6, Timestamp.valueOf(tracker.getCreatedAt()));
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected==0){
                throw new SQLException("Creating tracker failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating tracker failed, no ID obtained.");
                }
            }
        } 
    }

    public void deleteTracker(int expenseId) throws SQLException {
        String query = "DELETE FROM expenses WHERE expense_id = ?";
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query))
        {
            pstmt.setInt(1, expenseId);
            int isDeleted = pstmt.executeUpdate();
            if(isDeleted==0){
                throw new SQLException("Deleting tracker failed, no rows affected.");
            }
        }
    }

    public void updateTracker(Tracker tracker) throws SQLException {
        String query = "UPDATE expenses SET category_id = ?, type = ?, amount = ?, description = ?, expense_date = ? WHERE expense_id = ?";
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, tracker.getCategoryId());
            pstmt.setString(2, tracker.getType());
            pstmt.setFloat(3, tracker.getAmount());
            pstmt.setString(4, tracker.getDescription());
            pstmt.setDate(5, Date.valueOf(tracker.getExpenseDate()));
            pstmt.setInt(6, tracker.getExpenseId());
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected==0){
                throw new SQLException("Updating tracker failed, no rows affected.");
            }
        }
    }


}
