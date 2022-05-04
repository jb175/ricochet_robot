module com.isep {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.isep to javafx.fxml;
    exports com.isep;
}
