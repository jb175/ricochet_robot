package com.isep;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import com.isep.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameController {
    @FXML
    private GridPane grid;

    Robot selectedRobot;
    Position[] positions;

    protected static Plateau plateau = new Plateau(new int[]{8, 8}, 40, 1, 4, new String[]{"Red", "Green", "Blue", "Yellow"}, new String[]{"Circle", "Star", "Triangle", "Hexagon"});

    @FXML
    private void initialize() throws Exception {
        initializeBoard();
        updateGrid();
    }

    @FXML
    private void initializeBoard() throws Exception {

        //on crée les colonnes et lignes de l'affichage
        for (int i = 0; i < 2*plateau.getQuarterBoardSize()[0]; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(plateau.getCellSize());
            grid.getColumnConstraints().add(column);
        }
        for (int j = 0; j < 2*plateau.getQuarterBoardSize()[1]; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(plateau.getCellSize());
            grid.getRowConstraints().add(row);
        }
        
        //on leur ajoute  des espace pour stocker leurs images
        for (int i = 0; i < 2*plateau.getQuarterBoardSize()[0]; i++) {
            for (int j = 0; j < 2*plateau.getQuarterBoardSize()[1]; j++) {
                grid.add(new Group(), i, j);
                addImage(i, j);//background 0
                addImage(i, j);//wallcolumn1 1
                addImage(i, j);//wallcolumn2 2
                addImage(i, j);//wallrow1 3
                addImage(i, j);//wallrow2 4
                addImage(i, j);//objective 5
                addImage(i, j, this.getClass().getDeclaredMethod("RobotSelection", int.class, int.class));//robot 6
                addImage(i, j, this.getClass().getDeclaredMethod("SelectionDestination", int.class, int.class));//selection filter 7
            }
        }


        //wall

        plateau.setQuarters(new Boolean[2][4][][]);
        ArrayList<Position> angles = new ArrayList<>();
        for (int k = 0; k < 4; k++) { //pour chaque quart

            ///////////////////////////////////////////////
            // initialisation des bordures et des listes //
            ///////////////////////////////////////////////

            //horizontal
            ArrayList<Boolean[]> quarterHorizontalWallTableList = new ArrayList<>();
            for (int c = 0; c < plateau.getQuarterBoardSize()[0]; c++) {

                ArrayList<Boolean> column = new ArrayList<>();
                for (int l = 0; l < plateau.getQuarterBoardSize()[1]+1; l++) {
                    if (l==0) { //border
                        column.add(true);
                    } else if(l==plateau.getQuarterBoardSize()[1]-1 && c==plateau.getQuarterBoardSize()[1]-1) { //central border
                        column.add(true);
                    } else { //otherwise
                        column.add(false);
                    }
                }
                quarterHorizontalWallTableList.add(column.toArray(new Boolean[column.size()]));

            }
            plateau.setQuarterHorizontalWallTable(quarterHorizontalWallTableList.toArray(new Boolean[quarterHorizontalWallTableList.size()][quarterHorizontalWallTableList.get(0).length]));

            //vertical
            ArrayList<Boolean[]> quarterVerticalWallTableList = new ArrayList<>();
            for (int c = 0; c < plateau.getQuarterBoardSize()[0]+1; c++) {

                ArrayList<Boolean> column = new ArrayList<>();
                for (int l = 0; l < plateau.getQuarterBoardSize()[1]; l++) {
                    if (c==0) { //border
                        column.add(true);
                    } else if(l==plateau.getQuarterBoardSize()[0]-1 && c==plateau.getQuarterBoardSize()[0]-1) { //central border
                        column.add(true);
                    } else { //otherwise
                        column.add(false);
                    }
                }
                quarterVerticalWallTableList.add(column.toArray(new Boolean[column.size()]));

            }
            plateau.setQuarterVerticalWallTable(quarterVerticalWallTableList.toArray(new Boolean[quarterVerticalWallTableList.size()][quarterVerticalWallTableList.get(0).length]));

            
            //////////////////////////////////////
            // initialisation des mur des cotés //
            //////////////////////////////////////

            for (int index = 0; index < 2; index++) { //pour chaque coté

                //on recherche des positions qui marchent
                ArrayList<Integer> positions = new ArrayList<>();
                int counter = 0;
                while (positions.size()<plateau.getWallAgainstBorderPerQuarter()) {
                    int position = (int)(Math.random()*(plateau.getQuarterBoardSize()[1]-2))+2;
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
                        plateau.getQuarterHorizontalWallTable()[0][position] = true;
                    } else { //vertical
                        plateau.getQuarterVerticalWallTable()[position][0] = true;
                    }
                }
            }
            
            //////////////////////////////////////////////
            // initialisation des mur d'angles centraux //
            //////////////////////////////////////////////

            //à faire
            int i = 0;
            while (i<plateau.getNumberOfCornersPerQuarter()) { //pour chaque angle
                //on recherche des positions qui marchent
                while (true) {
                    int Hcolumn = (int)(Math.random()*((plateau.getQuarterBoardSize()[1]-1)-2))+2;
                    int Hrow = (int)(Math.random()*(plateau.getQuarterBoardSize()[0]-2))+2;
                    if (Boolean.TRUE.equals(horizontalVerif(plateau.getQuarterHorizontalWallTable(), Hcolumn, Hrow, plateau.getQuarterVerticalWallTable()))) {
                        int number = (int)(Math.random()*4);
                        switch (number) {
                            case 0 :
                                int Vcolum = Hcolumn;
                                int Vrow = Hrow;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(plateau.getQuarterVerticalWallTable(), Vcolum, Vrow, plateau.getQuarterHorizontalWallTable(), Hcolumn, Hrow,0))) {
                                    plateau.getQuarterHorizontalWallTable()[Hcolumn][Hrow] = true;
                                    plateau.getQuarterVerticalWallTable()[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hrow-1, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hcolumn-1, 2*plateau.getQuarterBoardSize()[0]-Hrow-1));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow, 2*plateau.getQuarterBoardSize()[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 1 :
                                Vcolum = Hcolumn+1;
                                Vrow = Hrow;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(plateau.getQuarterVerticalWallTable(), Vcolum, Vrow, plateau.getQuarterHorizontalWallTable(), Hcolumn, Hrow,1))) {
                                    plateau.getQuarterHorizontalWallTable()[Hcolumn][Hrow] = true;
                                    plateau.getQuarterVerticalWallTable()[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hrow-1, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hcolumn-1, 2*plateau.getQuarterBoardSize()[0]-Hrow-1));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow, 2*plateau.getQuarterBoardSize()[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 2 :
                                Vcolum = Hcolumn;
                                Vrow = Hrow-1;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(plateau.getQuarterVerticalWallTable(), Vcolum, Vrow, plateau.getQuarterHorizontalWallTable(), Hcolumn, Hrow,2))) {
                                    plateau.getQuarterHorizontalWallTable()[Hcolumn][Hrow] = true;
                                    plateau.getQuarterVerticalWallTable()[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow-1));
                                            break;
                                        case 1: //en haut a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hrow, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hcolumn-1, 2*plateau.getQuarterBoardSize()[0]-Hrow));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow-1, 2*plateau.getQuarterBoardSize()[0]-Hcolumn-1));
                                            break;
                                    }
                                    i++;
                                }
                                break;
                            case 3 :
                                Vcolum = Hcolumn+1;
                                Vrow = Hrow-1;
                                if (Vrow<7 && Boolean.TRUE.equals(verticalVerif(plateau.getQuarterVerticalWallTable(), Vcolum, Vrow, plateau.getQuarterHorizontalWallTable(), Hcolumn, Hrow,3))) {
                                    plateau.getQuarterHorizontalWallTable()[Hcolumn][Hrow] = true;
                                    plateau.getQuarterVerticalWallTable()[Vcolum][Vrow] = true;
                                    switch (k) {
                                        case 0 : //en haut a gauche
                                            angles.add(new Position(Hcolumn, Hrow-1));
                                            break;

                                        case 1: //en haut a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hrow, Hcolumn));
                                            break;

                                        case 2: //en bas a droite
                                            angles.add(new Position(2*plateau.getQuarterBoardSize()[1]-Hcolumn-1, 2*plateau.getQuarterBoardSize()[0]-Hrow));
                                            break;

                                        case 3: //en bas a gauche
                                            angles.add(new Position(Hrow-1, 2*plateau.getQuarterBoardSize()[0]-Hcolumn-1));
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
            plateau.setAngles(angles.toArray(new Position[angles.size()]));

            if (k%2 == 0) { //stockage conventionel
                plateau.getQuarters()[0][k] = tableRotate(plateau.getQuarterHorizontalWallTable(), k);
                plateau.getQuarters()[1][k] = tableRotate(plateau.getQuarterVerticalWallTable(), k);
            } else { //le retournement par 1 et 3 inverse les 2 listes (8*9 deviens 9*8 ...)
                plateau.getQuarters()[0][k] = tableRotate(plateau.getQuarterVerticalWallTable(), k);
                plateau.getQuarters()[1][k] = tableRotate(plateau.getQuarterHorizontalWallTable(), k);
            }
        }

        /*on inverse les 2 derniers quarts car la rotation s'éffectue quart par quart dans le sens horaire et l'enregistrement finale ce fait car par quart colonne par collone et ligne par ligne
            avant : 0 3     aprés : 0 1
                    1 2             2 3
        */
        for (int index = 0; index < 2; index++) {
            Boolean[][] quart = plateau.getQuarters()[index][1];
            plateau.getQuarters()[index][1] = plateau.getQuarters()[index][3];
            plateau.getQuarters()[index][3] = plateau.getQuarters()[index][2];
            plateau.getQuarters()[index][2] = quart;
        }//*/

        //on stock les variables finales
        plateau.setWalls(new Boolean[][][] {boardMaker(plateau.getQuarters()[0], true), boardMaker(plateau.getQuarters()[1], false)});
    }

    @FXML
    private void updateGrid() {
        //background
        for (int i = 0; i < 2*plateau.getQuarterBoardSize()[0]; i++) {
            for (int j = 0; j < 2*plateau.getQuarterBoardSize()[1]; j++) {
                getCell(i, j, 0).setImage(new Image(getClass().getResourceAsStream("/img/Board.png")));
                if ((i == plateau.getQuarterBoardSize()[0]-1 || i == plateau.getQuarterBoardSize()[0]) && (j == plateau.getQuarterBoardSize()[1]-1 || j == plateau.getQuarterBoardSize()[1])) {
                    getCell(i, j, 0).setImage(null);
                }
            }
        }

        //wall
        //on initialise les lignes horizontales sur l'image 1
        for (int i = 0; i < plateau.getWalls()[0].length; i++) {
            for (int j = 0; j < plateau.getWalls()[0][i].length; j++) {
                if(plateau.getWalls()[0][i][j]) {
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
        for (int i = 0; i < plateau.getWalls()[1].length; i++) {
            for (int j = 0; j < plateau.getWalls()[1][i].length; j++) {
                if(plateau.getWalls()[1][i][j]) {
                    try {
                        getCell(i, j, 3).setImage(new Image(getClass().getResourceAsStream("/img/LeftWall.png")));
                    } catch (Exception e) {}
                    try {
                        getCell(i-1, j, 4).setImage(new Image(getClass().getResourceAsStream("/img/RightWall.png")));
                    } catch (Exception e) {}
                }
            }
        }

        ArrayList<Jeton>jetonsList = new ArrayList<>();
        for (int i=0; i<plateau.getAngles().length; i++) {
            getCell(plateau.getAngles()[i].getColumn(), plateau.getAngles()[i].getRow(), 0).setImage(new Image(getClass().getResourceAsStream("/img/BoardEmpty.png")));
            jetonsList.add(new Jeton(plateau.getColors()[i%4], plateau.getShapes()[i/4], new Position(plateau.getAngles()[i].getColumn(), plateau.getAngles()[i].getRow())));
            getCell(plateau.getAngles()[i].getColumn(), plateau.getAngles()[i].getRow(), 5).setImage(new Image(getClass().getResourceAsStream("/img/Objective"+plateau.getColors()[i%4]+plateau.getShapes()[i/4]+".png")));
        }
        plateau.setJetons(jetonsList.toArray(new Jeton[jetonsList.size()]));

        ArrayList<Position> positions = new ArrayList<>();
        int overflow = 0;
        while (positions.size()<plateau.getColors().length && overflow <= 100) {
            overflow++;
            int c = (int)(Math.random()*2*GameController.plateau.getQuarterBoardSize()[0]);
            int r = (int)(Math.random()*2*GameController.plateau.getQuarterBoardSize()[1]);
            boolean validPosition = true;

            //carré central
            if((c==GameController.plateau.getQuarterBoardSize()[0]-1 || c==GameController.plateau.getQuarterBoardSize()[0])&&(r==GameController.plateau.getQuarterBoardSize()[1]-1 || r==GameController.plateau.getQuarterBoardSize()[1])) {
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
        }
        ArrayList<Robot>robotsList = new ArrayList<>();
        for (int i=0; i<plateau.getColors().length; i++) {
            robotsList.add(new Robot(plateau.getColors()[i%4], new Position(positions.get(i).getColumn(), positions.get(i).getRow())));
            getCell(positions.get(i).getColumn(), positions.get(i).getRow(), 6).setImage(new Image(getClass().getResourceAsStream("/img/"+plateau.getColors()[i]+"Robot.png")));
        }

        // robotsList.add(new Robot(plateau.getColors()[0], new Position(9, 7)));
        // getCell(9, 7, 6).setImage(new Image(getClass().getResourceAsStream("/img/"+plateau.getColors()[0]+"Robot.png")));

        // robotsList.add(new Robot(plateau.getColors()[0], new Position(12, 7)));
        // getCell(12, 7, 6).setImage(new Image(getClass().getResourceAsStream("/img/"+plateau.getColors()[0]+"Robot.png")));

        // robotsList.add(new Robot(plateau.getColors()[0], new Position(9, 8)));
        // getCell(9, 8, 6).setImage(new Image(getClass().getResourceAsStream("/img/"+plateau.getColors()[0]+"Robot.png")));


        plateau.setRobots(robotsList.toArray(new Robot[robotsList.size()]));

    }

    private void updateGame(int column, int raw) {
        updateRobots();
        updateSelection(column, raw);
    }

    private void updateRobots() {
        for (int i = 0; i < 2*GameController.plateau.getQuarterBoardSize()[0]; i++) {
            for (int j = 0; j < 2*GameController.plateau.getQuarterBoardSize()[1]; j++) {
                getCell(i, j, 6).setImage(null);
            }
        }
        for (Robot robot : GameController.plateau.getRobots()) {
            getCell(robot.getPosition().getColumn(), robot.getPosition().getRow(), 6).setImage(new Image(getClass().getResourceAsStream("/img/"+robot.getCouleur()+"Robot.png")));
        }
    }

    private void updateSelection(int column, int row) {
        this.positions = new Position[] {};
        RobotSelection(column, row);
    }

    //accéder à une cas à partir de la grille et des coordonées
    private ImageView getCell(int column, int row, int image) {
        return (ImageView)((Group)this.grid.getChildren().get(column*2*plateau.getQuarterBoardSize()[1]+row)).getChildren().get(image);
    }

    private void addImage(int column, int row) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        this.getGroup(column, row).getChildren().add(imageView);
    }

    private void addImage(int column, int row, Method method) {
        ImageView imageView = new ImageView();
        imageView.setOnMouseClicked
                (event -> {
                    try {
                        method.invoke(this, column, row);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                    
                });
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        this.getGroup(column, row).getChildren().add(imageView);
    }

    @SuppressWarnings("unused")
    private void RobotSelection(int column, int row) {
        for (Robot robot : GameController.plateau.getRobots()) {
            if(robot.getPosition().equals(new Position(column, row))) {
                //on affiche la couleur du robot selectionné
                System.out.println("Robot "+robot.getCouleur());
                selectedRobot = robot;

                //on enléve tous les filtres du plateaux
                for (int i = 0; i < 2*GameController.plateau.getQuarterBoardSize()[0]; i++) {
                    for (int j = 0; j < 2*GameController.plateau.getQuarterBoardSize()[1]; j++) {
                        getCell(i, j, 7).setImage(null);
                    }
                }

                //on ajoute un filtre sur la case selectionnée
                getCell(column, row, 7).setImage(new Image(getClass().getResourceAsStream("/img/selection.png")));

                
                this.positions = getPossibilities(robot);
                for (Position p : this.positions) {
                    getCell(p.getColumn(), p.getRow(), 7).setImage(new Image(getClass().getResourceAsStream("/img/selection.png")));
                    
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void SelectionDestination(int column, int row) {
        for (Position p : this.positions) {
            if(p.equals(new Position(column, row))) {
                System.out.println(column+":"+row);
                selectedRobot.setPosition(new Position(column, row));
                updateGame(column, row);
            }
        }
    }

    enum direction{
        top,
        right,
        bottom,
        left
    }
    //////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Ajoute un joueur//////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
    private Position[] getPossibilities(Robot robot) {
        ArrayList<Position> positions = new ArrayList<>();
        int i;

        

        // déplacement droite
        i = 1;
        while (true) {
            if (check1Movement(robot, new Position(robot.getPosition().getColumn()+i, robot.getPosition().getRow()), direction.right)) {
                i++;
            } else {
                if (i!=0) {
                    positions.add(new Position(robot.getPosition().getColumn()+(i-1), robot.getPosition().getRow()));
                }
                break;
            }
        }

        //déplacement gauche
        i = 1;
        while (true) {
            if (check1Movement(robot, new Position(robot.getPosition().getColumn()-i, robot.getPosition().getRow()), direction.left)) {
                i++;
            } else {
                if (i!=0) {
                    positions.add(new Position(robot.getPosition().getColumn()-(i-1), robot.getPosition().getRow()));
                }
                break;
            }
        }

        //déplacement bas
        i = 1;
        while (true) {
            if (check1Movement(robot, new Position(robot.getPosition().getColumn(), robot.getPosition().getRow()+i), direction.bottom)) {
                i++;
            } else {
                if (i!=0) {
                    positions.add(new Position(robot.getPosition().getColumn(), robot.getPosition().getRow()+(i-1)));
                }
                break;
            }
        }

        //déplacement haut
        i = 1;
        while (true) {
            if (check1Movement(robot, new Position(robot.getPosition().getColumn(), robot.getPosition().getRow()-i), direction.top)) {
                i++;
            } else {
                if (i!=0) {
                    positions.add(new Position(robot.getPosition().getColumn(), robot.getPosition().getRow()-(i-1)));
                }
                break;
            }
        }

        return positions.toArray(new Position[positions.size()]);
    }

    private Boolean check1Movement(Robot robot, Position position, direction d) {
        //check wall
        if (d==direction.values()[0]) {
            if (plateau.getWalls()[0][position.getColumn()][position.getRow()+1]) {
                return false;
            }
        } else if (d==direction.values()[1]) {
            if (plateau.getWalls()[1][position.getColumn()][position.getRow()]) {
                return false;
            }
        } else if (d==direction.values()[2]) {
            if (plateau.getWalls()[0][position.getColumn()][position.getRow()]) {
                return false;
            }
        } else if (d==direction.values()[3]) {
            if (plateau.getWalls()[1][position.getColumn()+1][position.getRow()]) {
                return false;
            }
        }
        //check robot
        for (Robot robotT : GameController.plateau.getRobots()) {
            if (robotT.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    private Group getGroup(int column, int row) {
        return (Group)this.grid.getChildren().get(column*2*plateau.getQuarterBoardSize()[1]+row);
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

//    @FXML
//    private TextField textfieldJoueur;
//    @FXML
//    private TableView<Joueur> joueurTable;
//    @FXML
//    private TableColumn<Joueur, String> joueurNameColumn;
//    @FXML
//    private TableColumn<Joueur, String> joueurCoupColumn;

//    public ObservableList<Joueur> getJoueurDat() {
//        return joueurDat;
//    }
//    private ObservableList<Joueur> joueurDat = FXCollections.observableArrayList();


    //    @FXML
    //   private void initialise(){
    //    joueurNameColumn.setCellValueFactory(dataName->dataName.getValue().nameJoueurProperty());
    //   joueurCoupColumn.setCellValueFactory(dataType->dataType.getValue().coupJoueurProperty());
    //}
    //@FXML
    //public void addJoueur(){
    //  if (!Objects.equals(textfieldJoueur.getText(), "")){
    //      this.getJoueurDat().add(new Joueur(textfieldJoueur.getText()));
    //      joueurTable.setItems(this.getJoueurDat());
    //  }
    //}


}
