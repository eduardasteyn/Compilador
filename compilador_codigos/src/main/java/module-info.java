module compilador {
    requires javafx.controls;
    requires javafx.fxml;

    opens compilador to javafx.fxml;
    exports compilador;
}
