package com.geobudget.geobudget;

/**
 * Created by joel on 21/10/17.
 */

public class DatabaseEntry {
    private int id;

    public DatabaseEntry(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
