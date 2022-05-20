package com.isep;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        stage.setTitle("RicochetRobot");
        scene = new Scene(loadFXML("game"), 2*GameController.plateau.getQuarterBoardSize()[0]*GameController.plateau.getCellSize()+20+300, 2*GameController.plateau.getQuarterBoardSize()[1]*GameController.plateau.getCellSize()+20);
        stage.setScene(scene);
        //stage.setMaximized(true);

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