package com.tracker.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Expense Tracker");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout()); // centers content
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // spacing between buttons

        JButton addExpenseButton = new JButton("Add Expense");
        JButton addCategoriesButton = new JButton("Add Categories");

        // First button
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(addExpenseButton, gbc);

        // Second button (next to the first)
        gbc.gridx = 1;
        panel.add(addCategoriesButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Open CategoriesGUI when button is clicked
        addCategoriesButton.addActionListener(e -> {
            CategoriesGUI addCategoryDialog = new CategoriesGUI(); // no-arg constructor
            addCategoryDialog.setVisible(true);
        });

        // Open TrackerGUI when Add Expense is clicked
        addExpenseButton.addActionListener(e -> {
            TrackerGUI trackerGUI = new TrackerGUI();
            trackerGUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
