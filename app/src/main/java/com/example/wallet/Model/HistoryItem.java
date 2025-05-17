package com.example.wallet.Model;

public class HistoryItem {
    private String title;
    private String amount;
    private boolean isHeader;

    // Constructor for Header (Month)
    public HistoryItem(String title) {
        this.title = title;
        this.isHeader = true; // It's a header (month)
    }

    // Constructor for Expense
    public HistoryItem(String title, String amount) {
        this.title = title;
        this.amount = amount;
        this.isHeader = false; // It's an expense
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
