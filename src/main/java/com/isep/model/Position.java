package com.isep.model;

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


    //
    // Accessor methods
    //
    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }
}
