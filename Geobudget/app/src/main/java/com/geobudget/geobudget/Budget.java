package com.geobudget.geobudget;

/**
 * Created by joel on 21/10/17.
 */

public class Budget extends DatabaseEntry {
    private String category;
    private float allowance;
    private float totalExpenditure;
    private boolean isIncome;

    public Budget(int id, String category, float allowance, float totalExpenditure, boolean isIncome) {
        super(id);
        this.category = category;
        this.allowance = allowance;
        this.totalExpenditure = totalExpenditure;
        this.isIncome = isIncome;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAllowance() {
        return this.allowance;
    }

    public void setAllowance(float allowance) {
        this.allowance = allowance;
    }

    public float getTotalExpenditure() { return this.totalExpenditure;}

    public void setTotalExpenditure(float totalExpenditure) {this.totalExpenditure = totalExpenditure;}

    public boolean getIsIncome() { return this.isIncome;}

    public void setIsIncome(boolean isIncome) {this.isIncome = isIncome;}
}
