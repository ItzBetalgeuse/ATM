package sample.ficheros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("JavATM");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("ATM.png"))));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        LoginController.Ventana = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}