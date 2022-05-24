package com.isep.model;

/**
 * Class Game
 */
public class Game {

  //
  // Fields
  //
  private Player[] players;
  private Board board;
  
  //
  // Constructors
  //
  public Game(Board board) {
    players = null;
    this.board = board;
  }
  
  //
  // Methods
  //

  //
  // Accessor methods
  //
  public Player[] getPlayers() {
    return players;
  }

  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  };

  //
  // Other methods
  //

}
