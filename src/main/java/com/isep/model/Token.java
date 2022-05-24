package com.isep.model;

/**
 * Class Jeton
 */
public class Token {

  //
  // Fields
  //
  private String couleur;
  private String forme;
  private Position position;
  
  //
  // Constructors
  //
  public Token(String couleur, String forme, Position position) {
    this.couleur = couleur;
    this.forme = forme;
    this.position = position;
  }

  public String getCouleur() {
    return couleur;
  }

  public void setCouleur(String couleur) {
    this.couleur = couleur;
  }

  public String getForme() {
    return forme;
  }

  public void setForme(String forme) {
    this.forme = forme;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  

}
