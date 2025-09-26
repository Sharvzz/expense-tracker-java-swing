package com.tracker.gui;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.tracker.dao.CategoriesDAO;
import com.model.Categories;
import com.tracker.dao.TrackerDAO;
import com.model.Tracker;
public class TrackerGUI extends JFrame{
    private JComboBox<String> typeComboBox;
    private JComboBox<String> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;
    private JButton addButton;
    private JButton deleteExpense;
    private JButton updateExpense;
    private JButton refreshExpenses;
    private JButton addCategory;
    private CategoriesDAO categoriesDAO;
    private TrackerDAO trackerDAO;
    private JTable expensesTable;


    public TrackerGUI(){
        categoriesDAO = new CategoriesDAO();
        trackerDAO = new TrackerDAO();

        setTitle("Expense Tracker");
        setSize(900,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents(){
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // padding around components

        // Type row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Type:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        typeComboBox = new JComboBox<>(new String[]{"Income","Expense"});
        formPanel.add(typeComboBox, gbc);

        // Category row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        categoryComboBox = new JComboBox<>();
        loadCategories();
        formPanel.add(categoryComboBox, gbc);

        // Amount row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Amount:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        amountField = new JTextField();
        formPanel.add(amountField, gbc);

        // Description row
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        descriptionField = new JTextField();
        formPanel.add(descriptionField, gbc);

        // Date row
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dateField = new JTextField();
        formPanel.add(dateField, gbc);

        // Buttons row
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        addButton = new JButton("Add Entry");
        addButton.addActionListener(e -> saveRecord());
        formPanel.add(addButton, gbc);

        gbc.gridx = 1;
        deleteExpense = new JButton("Delete Entry");
        deleteExpense.addActionListener(e -> deleteExpense());
        formPanel.add(deleteExpense, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        updateExpense = new JButton("Update Entry");
        updateExpense.addActionListener(e -> updateExpense());
        formPanel.add(updateExpense, gbc);

        gbc.gridx = 1;
        refreshExpenses = new JButton("Refresh");
        refreshExpenses.addActionListener(e -> loadExpenses());
        formPanel.add(refreshExpenses, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;

        addCategory = new JButton("Add Category");
        addCategory.addActionListener(e -> {
            CategoriesGUI addCategoryDialog = new CategoriesGUI();
            addCategoryDialog.setVisible(true);
        });

        formPanel.add(addCategory, gbc);

        add(formPanel, BorderLayout.NORTH);

        expensesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expensesTable);
        add(scrollPane, BorderLayout.CENTER);

        loadExpenses();

    }

    private void loadCategories(){
        try{
            List<Categories> categories = categoriesDAO.getAllCategories();
            categoryComboBox.removeAllItems();
            for(Categories cat : categories){
                categoryComboBox.addItem(cat.getCategoryName());
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveRecord() {
        String type = (String) typeComboBox.getSelectedItem();
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        String dateText = dateField.getText();

        if (amountText.isEmpty() || description.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (categoryName == null) {
            JOptionPane.showMessageDialog(this, "Please select a category!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
            return;
        }

        try {
            LocalDate expenseDate = LocalDate.parse(dateText);
            LocalDateTime createdAt = LocalDateTime.now();

            int categoryId = categoriesDAO.getCategoryIdByName(categoryName);

            Tracker tracker = new Tracker();
            tracker.setCategoryId(categoryId);
            tracker.setType(type);
            tracker.setAmount((float) amount);
            tracker.setDescription(description);
            tracker.setExpenseDate(expenseDate);
            tracker.setCreatedAt(createdAt);

            int trackerId = trackerDAO.addTracker(tracker);

            JOptionPane.showMessageDialog(this, "Record saved successfully! ID: " + trackerId);

            // Clear fields
            amountField.setText("");
            descriptionField.setText("");
            dateField.setText("");

            loadExpenses();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving record: " + ex.getMessage());
        }
    }

    private void loadExpenses(){
        try{
            List<Tracker> trackers = trackerDAO.getAllTrackers();
            List<Categories> categories = categoriesDAO.getAllCategories();
            String[] columnNames = {"Expense ID","Category ID"," Category Name","Type","Amount","Description","Expense Date","Created At"};
            Object[][] data = new Object[trackers.size()][8];
            for(int i=0;i<trackers.size();i++){
                Tracker t = trackers.get(i);
                data[i][0] = t.getExpenseId();
                data[i][1] = t.getCategoryId();
                data[i][2] = categories.stream()
                                   .filter(c -> c.getCategoryId() == t.getCategoryId())
                                   .map(Categories::getCategoryName)
                                   .findFirst()
                                   .orElse("Unknown");
                data[i][3] = t.getAmount();
                data[i][4] = t.getDescription();
                data[i][5] = t.getExpenseDate();
                data[i][7] = t.getCreatedAt();
            }
            expensesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            loadCategories();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense(){
        int selectedRow = expensesTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int expenseId = (int) expensesTable.getValueAt(selectedRow, 0);
        try{
            trackerDAO.deleteTracker(expenseId);
            JOptionPane.showMessageDialog(this, "Expense deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadExpenses();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExpense(){
        int selectedRow = expensesTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select an expense to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int expenseId = (int) expensesTable.getValueAt(selectedRow, 0);
        String type = (String) typeComboBox.getSelectedItem();
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        String dateText = dateField.getText();

        if (amountText.isEmpty() || description.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (categoryName == null) {
            JOptionPane.showMessageDialog(this, "Please select a category!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
            return;
        }

        try {
            LocalDate expenseDate = LocalDate.parse(dateText);

            int categoryId = categoriesDAO.getCategoryIdByName(categoryName);

            Tracker tracker = new Tracker();
            tracker.setExpenseId(expenseId);
            tracker.setCategoryId(categoryId);
            tracker.setType(type);
            tracker.setAmount((float) amount);
            tracker.setDescription(description);
            tracker.setExpenseDate(expenseDate);

            trackerDAO.updateTracker(tracker);

            JOptionPane.showMessageDialog(this, "Record updated successfully! ID: " + expenseId);

            // Clear fields
            amountField.setText("");
            descriptionField.setText("");
            dateField.setText("");

            loadExpenses();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating record: " + ex.getMessage());
        }
    }

}
