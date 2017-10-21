package com.geobudget.geobudget;

/**
 * Created by joel on 21/10/17.
 */

public class Budget extends DatabaseEntry {
    private String category;
    private Float allowance;

    public Budget(int id, String category, Float allowance) {
        super(id);
        this.category = category;
        this.allowance = allowance;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getAllowance() {
        return this.allowance;
    }

    public void setAllowance(Float allowance) {
        this.allowance = allowance;
    }
}
