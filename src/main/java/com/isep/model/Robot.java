package com.isep.model;

/**
 * Class Robot
 */
public class Robot {

  //
  // Fields
  //

  private final String couleur;
  private Position position;
  
  //
  // Constructors
  //
  public Robot (String couleur, Position position) {
    this.couleur = couleur;
    this.position = position;
  }

  public String getCouleur() {
    return couleur;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }  
}
