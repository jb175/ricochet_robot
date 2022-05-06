package com.isep;

import java.util.ArrayList;
import java.util.Collections;

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
    private Position[] angles;

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
                addImage(i, j);//background 0
                addImage(i, j);//wallcolumn1 1
                addImage(i, j);//wallcolumn2 2
                addImage(i, j);//wallrow1 3
                addImage(i, j);//wallrow2 4
                addImage(i, j);//objective 5
                addImage(i, j);//robot 6
            }
        }


        //wall

        Boolean[][][][] quarters = new Boolean[2][4][][];
        ArrayList<Position> angles = new ArrayList<>();
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
            int i = 0;
            while (i<numberOfCornersPerQuarter) { //pour chaque angle
                //on recherche des positions qui marchent
                while (true) {
                    int Hcolumn = (int)(Math.random()*((quarterBoardSize[1]-1)-2))+2;
                    int Hrow = (int)(Math.random()*(quarterBoardSize[0]-2))+2;
                    if (Boolean.TRUE.equals(horizontalVerif(quarterHorizontalWallTable, Hcolumn, Hrow, quarterVerticalWallTable))) {
                        int number = (int)(Math.random()*4);
                        switch (number) {
                            case 0 :
                                int Vcolum = Hcolumn;
                                int Vrow = Hrow;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(quarterVerticalWallTable, Vcolum, Vrow, quarterHorizontalWallTable, Hcolumn, Hrow,0))) {
                                    quarterHorizontalWallTable[Hcolumn][Hrow] = true;
                                    quarterVerticalWallTable[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hrow-1, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hcolumn-1, 2*quarterBoardSize[0]-Hrow-1));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow, 2*quarterBoardSize[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 1 :
                                Vcolum = Hcolumn+1;
                                Vrow = Hrow;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(quarterVerticalWallTable, Vcolum, Vrow, quarterHorizontalWallTable, Hcolumn, Hrow,1))) {
                                    quarterHorizontalWallTable[Hcolumn][Hrow] = true;
                                    quarterVerticalWallTable[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hrow-1, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hcolumn-1, 2*quarterBoardSize[0]-Hrow-1));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow, 2*quarterBoardSize[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 2 :
                                Vcolum = Hcolumn;
                                Vrow = Hrow-1;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(quarterVerticalWallTable, Vcolum, Vrow, quarterHorizontalWallTable, Hcolumn, Hrow,2))) {
                                    quarterHorizontalWallTable[Hcolumn][Hrow] = true;
                                    quarterVerticalWallTable[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow-1));
                                            break;
                                        case 1: //en haut a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hrow, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hcolumn-1, 2*quarterBoardSize[0]-Hrow));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow-1, 2*quarterBoardSize[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 3 :
                                Vcolum = Hcolumn+1;
                                Vrow = Hrow-1;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(quarterVerticalWallTable, Vcolum, Vrow, quarterHorizontalWallTable, Hcolumn, Hrow,3))) {
                                    quarterHorizontalWallTable[Hcolumn][Hrow] = true;
                                    quarterVerticalWallTable[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow-1));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hrow, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*quarterBoardSize[1]-Hcolumn-1, 2*quarterBoardSize[0]-Hrow));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow-1, 2*quarterBoardSize[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                        }
                        break;
                    }
                }
            }
            Collections.shuffle(angles); //pour avoir les objectifs dans des ordres différents
            this.angles = angles.toArray(new Position[angles.size()]);

            if (k%2 == 0) { //stockage conventionel
                quarters[0][k] = tableRotate(quarterHorizontalWallTable, k);
                quarters[1][k] = tableRotate(quarterVerticalWallTable, k);
            } else { //le retournement par 1 et 3 inverse les 2 listes (8*9 deviens 9*8 ...)
                quarters[0][k] = tableRotate(quarterVerticalWallTable, k);
                quarters[1][k] = tableRotate(quarterHorizontalWallTable, k);
            }
        }

        /*on inverse les 2 derniers quarts car la rotation s'éffectue quart par quart dans le sens horaire et l'enregistrement finale ce fait car par quart colonne par collone et ligne par ligne
            avant : 0 3     aprés : 0 1
                    1 2             2 3
        */
        for (int index = 0; index < 2; index++) {
            Boolean[][] quart = quarters[index][1];
            quarters[index][1] = quarters[index][3];
            quarters[index][3] = quarters[index][2];
            quarters[index][2] = quart;
        }//*/

        //on stock les variables finales
        this.walls = new Boolean[][][] {boardMaker(quarters[0], true), boardMaker(quarters[1], false)};
    }

    @FXML
    private void updateGrid() {
        //background
        for (int i = 0; i < 2*quarterBoardSize[0]; i++) {
            for (int j = 0; j < 2*quarterBoardSize[1]; j++) {
                getCell(i, j, 0).setImage(new Image(getClass().getResourceAsStream("/img/Board.png")));
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
                        getCell(i, j-1, 2).setImage(new Image(getClass().getResourceAsStream("/img/BottomWall.png")));
                    } catch (Exception e) {}
                }
            }
        }
        //on initialise les lignes verticales sur l'image 2
        for (int i = 0; i < walls[1].length; i++) {
            for (int j = 0; j < walls[1][i].length; j++) {
                if(walls[1][i][j]) {
                    try {
                        getCell(i, j, 3).setImage(new Image(getClass().getResourceAsStream("/img/LeftWall.png")));
                    } catch (Exception e) {}
                    try {
                        getCell(i-1, j, 4).setImage(new Image(getClass().getResourceAsStream("/img/RightWall.png")));
                    } catch (Exception e) {}
                }
            }
        }

        String[] objectiveColor = {"Red", "Green", "Blue", "Yellow"};
        String[] objectiveShape = {"Circle", "Star", "Triangle", "Hexagon"};
        for (int i=0; i<this.angles.length; i++) {
            getCell(this.angles[i].getColumn(), this.angles[i].getRow(), 0).setImage(new Image(getClass().getResourceAsStream("/img/BoardEmpty.png")));
            getCell(this.angles[i].getColumn(), this.angles[i].getRow(), 5).setImage(new Image(getClass().getResourceAsStream("/img/Objective"+objectiveColor[i%4]+objectiveShape[i/4]+".png")));
        }

        ArrayList<Position> positions = new ArrayList<>();
        int overflow = 0;
        while (positions.size()<objectiveColor.length && overflow <= 100) {
            overflow++;
            int c = (int)(Math.random()*2*GameController.quarterBoardSize[0]);
            int r = (int)(Math.random()*2*GameController.quarterBoardSize[1]);
            boolean validPosition = true;

            //carré central
            if((c==GameController.quarterBoardSize[0]-1 || c==GameController.quarterBoardSize[0])&&(r==GameController.quarterBoardSize[1]-1 || r==GameController.quarterBoardSize[1])) {
                validPosition = false;
            }
            //position déja utilisée par un robot
            for (Position alreadyUsedPosition : positions) {
                if(alreadyUsedPosition.getColumn() == c || (alreadyUsedPosition.getRow() == r)) {
                    validPosition = false;
                }
            }
            if(validPosition) {
                positions.add(new Position(c, r));
            }
            System.out.println(c+" "+r);
        }
        for (int i=0; i<objectiveColor.length; i++) {
            getCell(positions.get(i).getColumn(), positions.get(i).getRow(), 6).setImage(new Image(getClass().getResourceAsStream("/img/"+objectiveColor[i]+"Robot.png")));
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

    private Boolean horizontalVerif(Boolean[][] matrixH, int columnH, int rowH, Boolean[][] matrixV) {
        if (!matrixH[columnH][rowH] && !matrixH[columnH+1][rowH] && !matrixH[columnH-1][rowH]) {
            if (!matrixV[columnH][rowH] && !matrixV[columnH+1][rowH] && !matrixV[columnH][rowH-1] && !matrixV[columnH+1][rowH-1]) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Boolean verticalVerif(Boolean[][] matrixV, int columnV, int rowV, Boolean[][] matrixH, int columnH, int rowH, int nb) {
        switch (nb) {
            case 0:
                if (!matrixH[columnH][rowH+1] && !matrixH[columnH-1][rowH+1] && !matrixV[columnV][rowV] && !matrixV[columnV][rowV+1] && !matrixV[columnV][rowV-1]) {
                    return true;
                } else {
                    return false;
                }
            case 1 :
                if (!matrixH[columnH][rowH+1] && !matrixH[columnH+1][rowH+1] && !matrixV[columnV][rowV] && !matrixV[columnV][rowV+1] && !matrixV[columnV][rowV-1]) {
                    return true;
                } else {
                    return false;
                }
            case 2 :
                if (!matrixH[columnH][rowH-1] && !matrixH[columnH-1][rowH-1] &&!matrixV[columnV][rowV] && !matrixV[columnV][rowV+1] && !matrixV[columnV][rowV-1]) {
                    return true;
                } else {
                    return false;
                }
            case 3 :
                if (!matrixH[columnH][rowH-1] && !matrixH[columnH+1][rowH-1] && !matrixV[columnV][rowV] && !matrixV[columnV][rowV+1] && !matrixV[columnV][rowV-1]) {
                    return true;
                } else {
                    return false;
                }
        }
        return null;
    }

    private Boolean[][] boardMaker(Boolean[][][] quarters, Boolean cond) { //cond sert à supprimer les doublons pour les valeurs centrales ce qui entraine sinon un décalage
        ArrayList<Boolean[]> board = new ArrayList<>(); //on initialise une arraylist pour contenir toutese les valeurs
        for (int quarterColumn = 0; quarterColumn < 2; quarterColumn++) { //pour chaque colonne de tableaux
            for (int c = 0; c < quarters[2*quarterColumn].length; c++) { //pour chaque colonne

                if (!(quarterColumn==1 && c==0 && !cond)) {
                    ArrayList<Boolean> column = new ArrayList<>(); //on initialise une colonne avec une arraylist pour contenir toute une des colonne finales
                    for (int quarterLine = 0; quarterLine < 2; quarterLine++) { //pour chaque ligne de tableaux
                        for (int l = 0; l < quarters[2*quarterColumn][c].length; l++) { //pour chaque ligne
                            if (!(quarterLine==1 && l==0 && cond)) {
                                column.add(quarters[2*quarterColumn+quarterLine][c][l]);
                            }
                        }
                    }
                    board.add(column.toArray(new Boolean[column.size()])); //convertion de l'arraylist en array et ajout dans le tableau général
                }

            }
        }
        return board.toArray(new Boolean[board.size()][board.get(0).length]); //convertion de l'arraylist en array et retour de son tableau
    }
}
