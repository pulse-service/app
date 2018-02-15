package com.electrocraft.nirzo.pluse.model;

/**
 * Create on 2/15/2018.
 * This class responsible to produce spinner
 *
 * @author Faisal Mohammad
 * @version 0.1
 * @since 0.1
 */

public class SpinnerHelper {

    private int position;
    private String databaseId;
    private String databaseValue;

    public SpinnerHelper(int position, String databaseId, String databaseValue) {
        this.position = position;
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public int getPosition() {
        return position;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    @Override
    public String toString() {
        return databaseValue;
    }
}
