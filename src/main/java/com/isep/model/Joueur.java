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
  private String pseudo;
  private int score = 0;
  private IntegerProperty coupJoueur;
  private StringProperty nameJoueur;
  //
  // Constructors
  //
  //public Joueur(String nameJoueur) {
    //this.nameJoueur = nameJoueur;
  //int coupJoueur = 0;
  //this.coupJoueur = new SimpleIntegerProperty(coupJoueur);
  //this.nameJoueur = new SimpleStringProperty(nameJoueur);
  //}
  // public StringProperty coupJoueurProperty(){
  //if(coupJoueur.get() == 1){
  //  return new SimpleStringProperty();

  //} else if (coupJoueur.get() == 2) {
  //  return new SimpleStringProperty();
  //}else if (coupJoueur.get() == 3) {
  //  return new SimpleStringProperty();
  //}else if (coupJoueur.get() == 4) {
  //  return new SimpleStringProperty();
  //}else{
  //  return null;
  //}

  //}
  //public StringProperty nameJoueurProperty(){
  //  return nameJoueur;
  // }
  public String getName() {
    return this.nameJoueur.get();
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
