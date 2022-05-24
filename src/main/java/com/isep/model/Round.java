package com.isep.model;

/**
 * Class Tour
 */
public class Round {

  //
  // Fields
  //
  private Token target;
  private Player winner;
  private Solution solution;
  
  //
  // Constructors
  //
  public Round(Token target) {
    this.target = target;
    winner  = null;
    solution = null;
  }
  
  //
  // Methods
  //


  //
  // Accessor methods
  //
  public Token getTarget() {
    return target;
  }

  public Player getWinner() {
    return winner;
  }

  public void setWinner(Player winner) {
    this.winner = winner;
  }

  public Solution getSolution() {
    return solution;
  }

  public void setSolution(Solution solution) {
    this.solution = solution;
  };

  //
  // Other methods
  //

  

}
