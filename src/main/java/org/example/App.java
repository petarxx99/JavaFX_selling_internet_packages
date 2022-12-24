package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
/*
groupId org.openjfx
ArtifactId javafx-archetype-fxml
version 0.0.6
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL urlFXMLfajla = this.getClass().getClassLoader().getResource("main.fxml");
        VBox koren = FXMLLoader.<VBox>load(urlFXMLfajla);

        final double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight() * 2 / 3.0;
        Scene scene = new Scene(koren, SCREEN_HEIGHT, SCREEN_HEIGHT);
        scene.getStylesheets().add("main.css");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}
