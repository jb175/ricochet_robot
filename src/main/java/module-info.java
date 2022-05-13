module com.isep {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.isep to javafx.fxml;
    exports com.isep;
    exports com.isep.model;
    opens com.isep.model to javafx.fxml;
}
