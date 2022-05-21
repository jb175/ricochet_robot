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
  private IntegerProperty score;
  
  //
  // Constructors
  //
  public Joueur (String nom) {
    this.nom = new SimpleStringProperty(nom);
    this.score = new SimpleIntegerProperty();
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
    return score.get();
  }

  public IntegerProperty getScoreProperty(){
    return score;
  }

  public void setScore(int score) {
    this.score.set(this.score.get()+score);
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
