package com.isep;

import com.isep.model.Joueur;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    protected static Stage stage;
    private static Scene scene;
    private boolean isJouerClicked;
    private ObservableList<Joueur> joueursData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        App.stage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        App.stage.setTitle("Ricochet Robots");
        App.stage.setScene(new Scene(loadFXML("root"), 900, 660));
        App.stage.show();
    }

    public void switchToGame(ActionEvent event) throws IOException{
        stage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        stage.setTitle("Ricochet Robots");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("game.fxml"));
        scene = new Scene(fxmlLoader.load(), 2*GameController.plateau.getQuarterBoardSize()[0]*GameController.plateau.getCellSize()+20+300, 2*GameController.plateau.getQuarterBoardSize()[1]*GameController.plateau.getCellSize()+20);
        stage.setScene(scene);
        stage.show();
        GameController controller = fxmlLoader.getController();
        if(showPlayerEditDialog()){
            controller.updateJoueurs(this);
        }
    }

    public Boolean showPlayerEditDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("player.fxml"));
        AnchorPane page = (AnchorPane) fxmlLoader.load();
        Stage dialogStage = new Stage();
        dialogStage.getIcons().add(new Image("file:src/main/resources/img/Board.png"));
        dialogStage.setTitle("Qui veut jouer ?");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(App.stage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        PlayerController controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);
        controller.setApp(this);
        dialogStage.showAndWait();
        return controller.isJouerClicked();
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

    public ObservableList<Joueur> getJoueursData() {
        return joueursData;
    }

}