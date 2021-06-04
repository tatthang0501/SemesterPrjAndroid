package com.example.semesterprojectnguyentatthangb17dccn563.model;

import java.io.Serializable;

public class MonthlyMoney implements Serializable {
    private String month;
    private float money;

    public MonthlyMoney() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
