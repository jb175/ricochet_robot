package com.isep;

/**
 * Class Position
 */
public class Position {

    //
    // Fields
    //
    private int column;
    private int row;

    //
    // Constructors
    //
    public Position (int column, int row) {
        this.column = column;
        this.row = row;
    }

    //
    // Methods
    //
    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public String getPosition () {
        return (" Colonne : " + column + "| Ligne : " + row);
    }

}
