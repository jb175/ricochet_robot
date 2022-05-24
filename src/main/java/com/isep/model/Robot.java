package com.isep.model;

/**
 * Class Robot
 */
public class Robot {

  //
  // Fields
  //

  private final String color;
  private Position position;
  
  //
  // Constructors
  //
  public Robot (String color, Position position) {
    this.color = color;
    this.position = position;
  }

  public String getColor() {
    return color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }  
}
