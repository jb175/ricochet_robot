package com.isep;

import com.isep.model.Joueur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;

public class PlayerController {

    private boolean jouerClicked = false;
    private App app;
    private Stage dialogStage;

    @FXML
    private TableView<Joueur> joueurs;

    @FXML
    private TableColumn<Joueur, String> nomColumn;

    @FXML
    private TextField nomField;

    @FXML
    private void initialize() {
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
    }

    @FXML
    private void deleteJoueur() {
        int selectedIndex = joueurs.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            joueurs.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(App.stage);
            alert.setTitle("AVERTISSEMENT");
            alert.setHeaderText("Aucun joueur sélectionné");
            alert.setContentText("Veuillez sélectionner un joueur dans le tableau !");
            alert.showAndWait();
        }
    }

    @FXML
    private void addJoueur() {
        if (!Objects.equals(nomField.getText(), "")) {
            app.getJoueursData().add(new Joueur(nomField.getText()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(App.stage);
            alert.setTitle("ERREUR");
            alert.setHeaderText("Nom de joueur invalide");
            alert.setContentText("Veuillez saisir un nom de joueur valide !");

            alert.showAndWait();
        }
    }

    @FXML
    private void jouer(){
        jouerClicked = true;
        dialogStage.close();
    }

    public void setApp(App app) {
        this.app = app;
        joueurs.setItems(app.getJoueursData());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isJouerClicked() {
        return jouerClicked;
    }



}
