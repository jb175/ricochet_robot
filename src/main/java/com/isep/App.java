package com.isep;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // on charge la vue "RootLayout"
        stage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        stage.setTitle("Ricochet Robots");
        stage.setScene(new Scene(loadFXML("root"), 900, 660));
        stage.show();
    }

    public void switchToGame(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        stage.setTitle("RicochetRobot");
        scene = new Scene(loadFXML("game"), 2*GameController.plateau.getQuarterBoardSize()[0]*GameController.plateau.getCellSize()+20, 2*GameController.plateau.getQuarterBoardSize()[1]*GameController.plateau.getCellSize()+20);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}