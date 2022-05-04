package com.isep;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private void initialize() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        initializeBoard();
        updateGrid();
    }

    @FXML
    private void initializeBoard() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        
        //initialisation variable
        int wallAgainstBorder = 1;

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
        
        for (int i = 0; i < 2*quarterBoardSize[0]; i++) {
            for (int j = 0; j < 2*quarterBoardSize[1]; j++) {
                grid.add(new Group(), i, j);
                addImage(i, j);//background
                addImage(i, j);//wallcolumn
                addImage(i, j);//wallrow
            }
        }


        //wall
        ArrayList<ArrayList<ArrayList<Boolean>>> WallList = new ArrayList<>();
        for (int index = 0; index < 2; index++) {
            ArrayList<ArrayList<Boolean>> table = new ArrayList<>();
            for (int i = 0; i < 2*quarterBoardSize[0]+index; i++) {
                table.add(new ArrayList<>());
            }
            WallList.add(table);
        }

        
        for (int k = 0; k < 4; k++) { //pour chaque quart
            ArrayList<ArrayList<ArrayList<Boolean>>> QuarterWallList = new ArrayList<>();
            for (int index = 0; index < 2; index++) {
                ArrayList<ArrayList<Boolean>> table = new ArrayList<>();
                for (int i = 0; i < quarterBoardSize[0]+index; i++) {
                    ArrayList<Boolean> list = new ArrayList<>();
                    for (int j = 0; j < quarterBoardSize[1]+1-index; j++) {
                        if (index==0) { //horizontal
                            if (j==0) { //border
                                list.add(true);
                            } else if(j==quarterBoardSize[1]-1 && i==quarterBoardSize[1]-1) { //central border
                                list.add(true);
                            } else { //otherwise
                                list.add(false);
                            }
                        } else { //vertical
                            if (i==0) { //border
                                list.add(true);
                            } else if(j==quarterBoardSize[0]-1 && i==quarterBoardSize[0]-1) { //central border
                                list.add(true);
                            } else { //otherwise
                                list.add(false);
                            }
                        }
                    }
                    table.add(list);
                }
                QuarterWallList.add(table);
            }
            for (int index = 0; index < 2; index++) {
                ArrayList<Integer> positions = new ArrayList<>();
                while (positions.size()<wallAgainstBorder) {
                    int position = (int)(Math.random()*(quarterBoardSize[0]-2));
                    if (!(positions.contains(position) || positions.contains(position-1) || positions.contains(position+1))) {
                        positions.add(position);
                    }
                }

                for (int position : positions) {
                    if (index==0) {
                        QuarterWallList.get(index).get(0).set(position+2, true);
                    } else {
                        QuarterWallList.get(index).get(position+2).set(0, true);
                    }
                }
            }
            
            for (int index = 0; index < 2; index++) {
                //convertion
                ArrayList<Boolean[]> t = new ArrayList<>();
                for (int i = 0; i < QuarterWallList.get(index).size(); i++) {
                    ArrayList<Boolean> l = new ArrayList<>();
                    for (int j = 0; j < QuarterWallList.get(index).get(0).size(); j++) {
                        l.add(QuarterWallList.get(index).get(i).get(j));
                    }
                    t.add(l.toArray(new Boolean[l.size()]));
                }

                Boolean[][] turn = tableRotate(t.toArray(new Boolean[QuarterWallList.get(index).size()][QuarterWallList.get(index).get(0).size()]), k);
                System.out.println();
            }
        }

        
        
        
        //convertion
        ArrayList<Boolean[][]> WallListFinal = new ArrayList<>();
        for (int index = 0; index < 2; index++) {
            ArrayList<Boolean[]> table = new ArrayList<>();
            for (int i = 0; i < 2*quarterBoardSize[0]+index; i++) {
                ArrayList<Boolean> list = new ArrayList<>();
                for (int j = 0; j < 2*quarterBoardSize[1]+1-index; j++) {
                    list.add(WallList.get(index).get(i).get(j));
                }
                table.add(list.toArray(new Boolean[list.size()]));
            }
            WallListFinal.add(table.toArray(new Boolean[table.size()][table.get(0).length]));
        }
        walls = WallListFinal.toArray(new Boolean[WallListFinal.size()][WallListFinal.get(0).length][WallListFinal.get(0)[0].length]);

        System.out.println();
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

        Boolean[][] table = rotatedTable.toArray(new Boolean[rotatedTable.size()][rotatedTable.get(0).length]);

        System.out.println();

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
