package com.isep.model;

/**
 * Class Movement
 */
public class Movement {

  //
  // Fields
  //
  private Robot robot;
  private Direction direction;
  
  //
  // Constructors
  //
  public Movement() { }
  
  //
  // Methods
  //

  //
  // Accessor methods
  //
  public Robot getRobot() {
    return robot;
  }

  public void setRobot(Robot robot) {
    this.robot = robot;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  //
  // Other methods
  //

}
