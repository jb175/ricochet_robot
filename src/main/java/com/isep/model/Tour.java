package com.isep.model;

import java.util.ArrayList;

/**
 * Class Tour
 */
public class Tour {

  //
  // Fields
  //

  private Jeton objectif;
  private Temps temps;
  private Joueur gagnant;
  private ArrayList<Solution> solution;
  
  //
  // Constructors
  //
  public Tour () { }

  public Jeton getObjectif() {
    return objectif;
  }

  public void setObjectif(Jeton objectif) {
    this.objectif = objectif;
  }

  public Temps getTemps() {
    return temps;
  }

  public void setTemps(Temps temps) {
    this.temps = temps;
  }

  public Joueur getGagnant() {
    return gagnant;
  }

  public void setGagnant(Joueur gagnant) {
    this.gagnant = gagnant;
  }

  public ArrayList<Solution> getSolution() {
    return solution;
  }

  public void setSolution(ArrayList<Solution> solution) {
    this.solution = solution;
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
