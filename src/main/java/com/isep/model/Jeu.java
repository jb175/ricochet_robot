package com.isep.model;

import java.util.ArrayList;

/**
 * Class Jeu
 */
class Jeu {

  //
  // Fields
  //
  private ArrayList<Tour> tours;
  private ArrayList<Joueur> joueurs;
  private Plateau plateau;
  
  //
  // Constructors
  //
  public Jeu () { }

  public ArrayList<Tour> getTours() {
    return tours;
  }

  public void setTours(ArrayList<Tour> tours) {
    this.tours = tours;
  }

  public ArrayList<Joueur> getJoueurs() {
    return joueurs;
  }

  public void setJoueurs(ArrayList<Joueur> joueurs) {
    this.joueurs = joueurs;
  }

  public Plateau getPlateau() {
    return plateau;
  }

  public void setPlateau(Plateau plateau) {
    this.plateau = plateau;
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
