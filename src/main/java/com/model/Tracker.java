package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tracker {
    private int expense_id;
    private int category_id;
    private String type;
    private float amount;
    private String description;
    private LocalDate expense_date;
    private LocalDateTime created_at;

    public Tracker() {
        this.expense_date = LocalDate.now();
        this.created_at = LocalDateTime.now();
    }

    public Tracker(int expense_id, int category_id, String type, float amount, String description, LocalDate expense_date) {
        this.expense_id = expense_id;
        this.category_id = category_id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.expense_date = expense_date;
    }

    public int getExpenseId() {
        return expense_id;
    }
    public void setExpenseId(int expense_id) {
        this.expense_id = expense_id;
    }
    public int getCategoryId() {
        return category_id;
    }
    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getExpenseDate() {
        return expense_date;
    }
    public void setExpenseDate(LocalDate expense_date) {
        this.expense_date = expense_date;
    }
    public LocalDateTime getCreatedAt() {
        return created_at;
    }
    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

}
