package com.example.wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Expense {

    private int id;
    private String name;
    private double amount;
    private String timestamp;


    public Expense() {
    }

    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public Expense(String name, double amount, String timestamp) {
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Expense(int id, String name, double amount, String timestamp) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
