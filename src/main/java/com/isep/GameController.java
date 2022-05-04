package com.isep;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameController {
    @FXML
    private GridPane grid;
    protected static int[] quarterBoardSize = {8,8};
    protected static int cellSize = 40;
    private Boolean[][][] walls;

    @FXML
    private void initialize() throws Exception {
        initializeBoard();
        updateGrid();
    }

    @FXML
    private void initializeBoard() throws Exception {
        
        //initialisation des variables de génération
        int wallAgainstBorder = 1;
        int numberOfCornersPerQuarter = 4;

        //on crée les colonnes et lignes de l'affichage
        for (int i = 0; i < 2*quarterBoardSize[0]; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(cellSize);
            grid.getColumnConstraints().add(column);
        }
        for (int j = 0; j < 2*quarterBoardSize[1]; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(cellSize);
            grid.getRowConstraints().add(row);
        }
        
        //on leur ajoute  des espace pour stocker leurs images
        for (int i = 0; i < 2*quarterBoardSize[0]; i++) {
            for (int j = 0; j < 2*quarterBoardSize[1]; j++) {
                grid.add(new Group(), i, j);
                addImage(i, j);//background
                addImage(i, j);//wallcolumn
                addImage(i, j);//wallrow
            }
        }


        //wall
        Boolean[][][][] quarters = new Boolean[2][4][][];
        for (int k = 0; k < 4; k++) { //pour chaque quart

            ///////////////////////////////////////////////
            // initialisation des bordures et des listes //
            ///////////////////////////////////////////////

            //horizontal
            ArrayList<Boolean[]> quarterHorizontalWallTableList = new ArrayList<>();
            for (int c = 0; c < quarterBoardSize[0]; c++) {

                ArrayList<Boolean> column = new ArrayList<>();
                for (int l = 0; l < quarterBoardSize[1]+1; l++) {
                    if (l==0) { //border
                        column.add(true);
                    } else if(l==quarterBoardSize[1]-1 && c==quarterBoardSize[1]-1) { //central border
                        column.add(true);
                    } else { //otherwise
                        column.add(false);
                    }
                }
                quarterHorizontalWallTableList.add(column.toArray(new Boolean[column.size()]));

            }
            Boolean[][] quarterHorizontalWallTable = quarterHorizontalWallTableList.toArray(new Boolean[quarterHorizontalWallTableList.size()][quarterHorizontalWallTableList.get(0).length]);

            //vertical
            ArrayList<Boolean[]> quarterVerticalWallTableList = new ArrayList<>();
            for (int c = 0; c < quarterBoardSize[0]+1; c++) {

                ArrayList<Boolean> column = new ArrayList<>();
                for (int l = 0; l < quarterBoardSize[1]; l++) {
                    if (c==0) { //border
                        column.add(true);
                    } else if(l==quarterBoardSize[0]-1 && c==quarterBoardSize[0]-1) { //central border
                        column.add(true);
                    } else { //otherwise
                        column.add(false);
                    }
                }
                quarterVerticalWallTableList.add(column.toArray(new Boolean[column.size()]));

            }
            Boolean[][] quarterVerticalWallTable = quarterVerticalWallTableList.toArray(new Boolean[quarterVerticalWallTableList.size()][quarterVerticalWallTableList.get(0).length]);

            
            //////////////////////////////////////
            // initialisation des mur des cotés //
            //////////////////////////////////////

            for (int index = 0; index < 2; index++) { //pour chaque coté

                //on recherche des positions qui marchent
                ArrayList<Integer> positions = new ArrayList<>();
                int counter = 0;
                while (positions.size()<wallAgainstBorder) {
                    int position = (int)(Math.random()*(quarterBoardSize[1]-2))+2;
                    if (!(positions.contains(position) || positions.contains(position-1) || positions.contains(position+1))) {
                        positions.add(position);
                    }
                    if (counter == 100) {
                        throw new Exception("Erreur lors de l'initialisation du plateau:\nil n'y a pas assez de place pour placer les murs");
                    } else {
                        counter++;
                    }
                }

                for (int position : positions) {
                    if (index==0) { //horizontal
                        quarterHorizontalWallTable[0][position] = true;
                    } else { //vertical
                        quarterVerticalWallTable[position][0] = true;
                    }
                }
            }
            
            //////////////////////////////////////////////
            // initialisation des mur d'angles centraux //
            //////////////////////////////////////////////

            //à faire

            if (k%2 == 0) { //stockage conventionel
                quarters[0][k] = tableRotate(quarterHorizontalWallTable, k);
                quarters[1][k] = tableRotate(quarterVerticalWallTable, k);
            } else { //le retournement par 1 et 3 inverse les 2 listes (8*9 deviens 9*8 ...)
                quarters[0][k] = tableRotate(quarterVerticalWallTable, k);
                quarters[1][k] = tableRotate(quarterHorizontalWallTable, k);
            }
        }

        //on stock les variables finales
        this.walls = new Boolean[][][] {boardMaker(quarters[0]), boardMaker(quarters[1])};

    }

    @FXML
    private void updateGrid() {
        //background
        for (int i = 0; i < 2*quarterBoardSize[0]; i++) {
            for (int j = 0; j < 2*quarterBoardSize[1]; j++) {
                getCell(i, j, 0).setImage(new Image(getClass().getResourceAsStream("/img/BoardEmpty.png")));
                if ((i == quarterBoardSize[0]-1 || i == quarterBoardSize[0]) && (j == quarterBoardSize[1]-1 || j == quarterBoardSize[1])) {
                    getCell(i, j, 0).setImage(null);
                }
            }
        }

        //wall
        //on initialise les lignes horizontales sur l'image 1
        for (int i = 0; i < walls[0].length; i++) {
            for (int j = 0; j < walls[0][i].length; j++) {
                if(walls[0][i][j]) {
                    try {
                        getCell(i, j, 1).setImage(new Image(getClass().getResourceAsStream("/img/TopWall.png")));
                    } catch (Exception e) {}
                    try {
                        getCell(i, j-1, 1).setImage(new Image(getClass().getResourceAsStream("/img/BottomWall.png")));
                    } catch (Exception e) {}
                }
            }
        }
        //on initialise les lignes verticales sur l'image 2
        for (int i = 0; i < walls[1].length; i++) {
            for (int j = 0; j < walls[1][i].length; j++) {
                if(walls[1][i][j]) {
                    try {
                        getCell(i, j, 2).setImage(new Image(getClass().getResourceAsStream("/img/LeftWall.png")));
                    } catch (Exception e) {}
                    try {
                        getCell(i-1, j, 2).setImage(new Image(getClass().getResourceAsStream("/img/RightWall.png")));
                    } catch (Exception e) {}
                }
            }
        }
    }

    //accéder à une cas à partir de la grille et des coordonées
    private ImageView getCell(int column, int row, int image) {
        return (ImageView)((Group)this.grid.getChildren().get(column*2*quarterBoardSize[1]+row)).getChildren().get(image);
    }

    private void addImage(int column, int row) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        this.getGroup(column, row).getChildren().add(imageView);
    }

    private Group getGroup(int column, int row) {
        return (Group)this.grid.getChildren().get(column*2*quarterBoardSize[1]+row);
    }

    private Boolean[][] tableRotate(Boolean[][] list, int rotation) {
        ArrayList<ArrayList<Boolean>> t = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            ArrayList<Boolean> l = new ArrayList<>();
            for (int j = 0; j < list[i].length; j++) {
                l.add(list[i][j]);
            }
            t.add(l);
        }

        for (int k = 0; k < rotation%4; k++) {
            int[] size = {t.size(), t.get(0).size()};
            for (int i = 0; i < size[1]; i++) {
                ArrayList<Boolean> l = new ArrayList<>();
                for (int j = 0; j < size[0]; j++) {
                    l.add(t.get(j).get(i));
                }
                t.add(size[0],l);
            }
            for (int i = 0; i < size[0]; i++) {
                t.remove(0);
            }
        }

        ArrayList<Boolean[]> rotatedTable = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            ArrayList<Boolean> rotatedList = new ArrayList<>();
            for (int j = 0; j < t.get(0).size(); j++) {
                rotatedList.add(t.get(i).get(j));
            }
            rotatedTable.add(rotatedList.toArray(new Boolean[rotatedList.size()]));
        }

        return rotatedTable.toArray(new Boolean[rotatedTable.size()][rotatedTable.get(0).length]);
    }



    private Boolean[][] boardMaker(Boolean[][][] quarters) {
        ArrayList<Boolean[]> board = new ArrayList<>(); //on initialise une arraylist pour contenir toutese les valeurs
        for (int quarterColumn = 0; quarterColumn < 2; quarterColumn++) { //pour chaque colonne de tableaux
            for (int c = 0; c < quarters[2*quarterColumn].length; c++) { //pour chaque colonne

                ArrayList<Boolean> column = new ArrayList<>(); //on initialise une colonne avec une arraylist pour contenir toute une des colonne finales
                for (int quarterLine = 0; quarterLine < 2; quarterLine++) { //pour chaque ligne de tableaux
                    for (int l = 0; l < quarters[2*quarterColumn][c].length; l++) { //pour chaque ligne
                        column.add(quarters[2*quarterColumn+quarterLine][c][l]);
                    }
                }
                board.add(column.toArray(new Boolean[column.size()])); //convertion de l'arraylist en array et ajout dans le tableau général

            }
        }
        return board.toArray(new Boolean[board.size()][board.get(0).length]); //convertion de l'arraylist en array et retour de son tableau
    }
}
