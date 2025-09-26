package com.tracker.dao;
import java.util.*;
import java.sql.*;
import com.tracker.util.DatabaseConnection;
import com.model.Categories;

public class CategoriesDAO {
    public List<Categories> getAllCategories() throws SQLException{
        List<Categories> categories =  new ArrayList<>();
        String query = "SELECT * FROM categories";
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ){
            while(rs.next()){
                Categories category = new Categories(rs.getString("name"));
                category.setCategoryId(rs.getInt("category_id"));
                categories.add(category);
            }
        }
        return categories;
    }


    public int addCategory(Categories category) throws SQLException{
        String query = "INSERT INTO categories (name) VALUES (?)";
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ){
            pstmt.setString(1, category.getCategoryName());
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected==0){
                throw new SQLException("Creating category failed, no rows affected.");
            }
            try(ResultSet generatedKeys = pstmt.getGeneratedKeys();){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }

    public void deleteCategory(int categoryId) throws SQLException{
        String query = "DELETE FROM categories WHERE category_id = ?";
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
        ){
            pstmt.setInt(1, categoryId);
            int isDeleted = pstmt.executeUpdate();
            if(isDeleted==0){
                throw new SQLException("Deleting category failed, no rows affected.");
            }
        }
    }

    public void updateCategory(Categories category) throws SQLException{
        String query = "UPDATE categories SET name = ? WHERE category_id = ?";
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
        ){
            pstmt.setString(1, category.getCategoryName());
            pstmt.setInt(2, category.getCategoryId());
            int isUpdate = pstmt.executeUpdate();
            if(isUpdate==0){
                throw new SQLException("Updating category failed, no rows affected.");
            }
        }
    }

    public int getCategoryIdByName(String name) throws SQLException{
        String query = "SELECT category_id FROM categories WHERE name = ?";
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
        ){
            pstmt.setString(1, name);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("category_id");
                }
                throw new SQLException("Category not found: " + name);
            }
        }
    }

}
