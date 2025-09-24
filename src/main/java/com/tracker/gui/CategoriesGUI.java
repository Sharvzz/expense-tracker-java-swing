package com.tracker.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import com.tracker.dao.CategoriesDAO;
import com.model.Categories;

public class CategoriesGUI extends JFrame {

    private JTextField categoryNameField;
    private JButton addButton;
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

        categoriesTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(categoriesTable);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addCategory());

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
}
