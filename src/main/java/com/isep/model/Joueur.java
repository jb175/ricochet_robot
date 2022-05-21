package com.isep.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class Joueur
 */
public class Joueur {

  //
  // Fields
  //
  private StringProperty nom;
  private int score;
  
  //
  // Constructors
  //
  public Joueur (String nom) {
    this.nom = new SimpleStringProperty(nom);
    score = 0;
  }

  public String getNom() {
    return nom.get();
  }

  public StringProperty getNomProperty(){
    return nom;
  }

  public void setNom(String nom) {
    this.nom.set(nom);
  }

  public int getScore() {
    return score;
  }

  public StringProperty getScoreProperty(){
    return new SimpleStringProperty(Integer.toString(score));
  }

  public void setScore(int score) {
    this.score = this.score + score;
  }
  
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
