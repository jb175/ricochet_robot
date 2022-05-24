package com.isep.model;

/**
 * Class Board
 */
public class Board {

  //
  // Fields
  //
  private int[] quarterBoardSize;
  private int cellSize;
  private Boolean[][][] walls;
  private Position[] angles;
  private int wallAgainstBorderPerQuarter;
  private int numberOfCornersPerQuarter;
  private Boolean[][] quarterHorizontalWallTable;
  private Boolean[][] quarterVerticalWallTable;
  private Boolean[][][][] quarters;
  private String[] colors;
  private String[] shapes;
  private Robot[] robots;
  private Token[] tokens;

  //
  // Constructors
  //
  public Board(int[] quarterBoardSize, int cellSize, int wallAgainstBorderPerQuarter, int numberOfCornersPerQuarter, String[] colors, String[] shapes) {
    this.quarterBoardSize = quarterBoardSize;
    this.cellSize = cellSize;
    walls = null;
    angles = null;
    this.wallAgainstBorderPerQuarter = wallAgainstBorderPerQuarter;
    this.numberOfCornersPerQuarter = numberOfCornersPerQuarter;
    quarterHorizontalWallTable = null;
    quarterVerticalWallTable = null;
    quarters = null;
    this.colors = colors;
    this.shapes = shapes;
    robots = null;
    tokens = null;
  }
  
  //
  // Methods
  //

  //
  // Accessor methods
  //
  public int[] getQuarterBoardSize() {
    return quarterBoardSize;
  }

  public int getCellSize() {
    return cellSize;
  }

  public Boolean[][][] getWalls() {
    return walls;
  }

  public Position[] getAngles() {
    return angles;
  }

  public int getWallAgainstBorderPerQuarter() {
    return wallAgainstBorderPerQuarter;
  }

  public int getNumberOfCornersPerQuarter() {
    return numberOfCornersPerQuarter;
  }

  public Boolean[][] getQuarterHorizontalWallTable() {
    return quarterHorizontalWallTable;
  }

  public Boolean[][] getQuarterVerticalWallTable() {
    return quarterVerticalWallTable;
  }

  public Boolean[][][][] getQuarters() {
    return quarters;
  }

  public String[] getColors() {
    return colors;
  }

  public String[] getShapes() {
    return shapes;
  }

  public Robot[] getRobots() {
    return this.robots;
  }

  public void setWalls(Boolean[][][] walls) {
    this.walls = walls;
  }

  public void setAngles(Position[] angles) {
    this.angles = angles;
  }

  public void setQuarterHorizontalWallTable(Boolean[][] quarterHorizontalWallTable) {
    this.quarterHorizontalWallTable = quarterHorizontalWallTable;
  }

  public void setQuarterVerticalWallTable(Boolean[][] quarterVerticalWallTable) {
    this.quarterVerticalWallTable = quarterVerticalWallTable;
  }

  public void setQuarters(Boolean[][][][] quarters) {
    this.quarters = quarters;
  }

  public void setRobots(Robot[] robots) {
    this.robots = robots;
  }

  public void setTokens(Token[] tokens) {
    this.tokens = tokens;
  }

  //
  //
  // Other methods
  //

}
