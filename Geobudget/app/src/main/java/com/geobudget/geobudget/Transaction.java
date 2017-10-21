package com.geobudget.geobudget;

import java.util.Date;

/**
 * Created by joel on 21/10/17.
 */

public class Transaction extends DatabaseEntry {
    private Float expenditure;
    private Date date;
    private Integer budget;

    public Float getExpenditure() {
        return this.expenditure;
    }

    public void setExpenditure(Float expenditure) {
        this.expenditure = expenditure;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getBudget() {
        return this.budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }
}
