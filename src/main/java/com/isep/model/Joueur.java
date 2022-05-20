package com.isep.model;

/**
 * Class Joueur
 */
public class Joueur {

  //
  // Fields
  //
  private String pseudo;
  private int score = 0;
  
  //
  // Constructors
  //
  public Joueur(String pseudo) {
    this.pseudo = pseudo;
  }

  public String getPseudo() {
    return pseudo;
  }

  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
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
