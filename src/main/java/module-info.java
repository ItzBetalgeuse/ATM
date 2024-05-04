module sample.ficheros {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens sample.ficheros to javafx.fxml;
    exports sample.ficheros;
}