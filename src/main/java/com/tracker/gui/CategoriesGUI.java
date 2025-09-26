package com.tracker.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import com.tracker.dao.CategoriesDAO;
import com.model.Categories;

public class CategoriesGUI extends JFrame {

    private JTextField categoryNameField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JTable categoriesTable;
    private CategoriesDAO categoriesDAO;

    public CategoriesGUI() {
        setTitle("Categories Management");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        categoriesDAO = new CategoriesDAO();

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Category Name:"));
        categoryNameField = new JTextField(15);
        inputPanel.add(categoryNameField);
        addButton = new JButton("Add Category");
        inputPanel.add(addButton);
        deleteButton = new JButton("Delete Category");
        inputPanel.add(deleteButton);
        updateButton = new JButton("Update Category");
        inputPanel.add(updateButton);
        refreshButton = new JButton("Refresh");
        inputPanel.add(refreshButton);

        categoriesTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(categoriesTable);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addCategory());
        refreshButton.addActionListener(e -> loadCategories());
        deleteButton.addActionListener(e -> deleteCategory());
        updateButton.addActionListener(e -> updateCategory());


        loadCategories();
    }

    private void loadCategories() {
        try {
            List<Categories> categories = categoriesDAO.getAllCategories();
            String[] columnNames = {"Category ID", "Category Name"};
            Object[][] data = new Object[categories.size()][2];
            for (int i = 0; i < categories.size(); i++) {
                data[i][0] = categories.get(i).getCategoryId();
                data[i][1] = categories.get(i).getCategoryName();
            }
            categoriesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        String categoryName = categoryNameField.getText().trim();
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categories category = new Categories(categoryName); // Use constructor with name
        try {
            categoriesDAO.addCategory(category);
            JOptionPane.showMessageDialog(this, "Category added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            categoryNameField.setText("");
            loadCategories();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory(){
        int selectedRow = categoriesTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0);
        try{
            categoriesDAO.deleteCategory(categoryId);
            JOptionPane.showMessageDialog(this, "Category deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCategories();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory(){
        int selectedRow = categoriesTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String categoryName = categoryNameField.getText().trim();
        if(categoryName.isEmpty()){
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0);
        Categories category = new Categories(categoryId, categoryName); // Use constructor with ID and name
        try{
            categoriesDAO.updateCategory(category);
            JOptionPane.showMessageDialog(this, "Category updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            categoryNameField.setText("");
            loadCategories();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
