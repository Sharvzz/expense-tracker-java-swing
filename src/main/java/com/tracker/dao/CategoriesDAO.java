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

}
