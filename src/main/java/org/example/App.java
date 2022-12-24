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
org.openjfx
javafx-archetype-fxml
0.0.6
 */
public class App extends Application {

  //  private static Scene scene;
    /*  scene = new Scene(loadFXML("primary"), 640, 480);
         stage.setScene(scene);
         stage.show(); */
    @Override
    public void start(Stage stage) throws IOException {
        probaFXML1(stage);
    }

    private void probaFXML1(Stage stage){
        try {
            URL urlFXMLfajla = this.getClass().getClassLoader().getResource("main.fxml");
            VBox koren = FXMLLoader.<VBox>load(urlFXMLfajla);
            final double SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight() * 2 / 3.0;
            Scene scene = new Scene(koren, SCREEN_HEIGHT, SCREEN_HEIGHT);
         //   scene.getStylesheets().add("main.css");
            stage.setScene(scene);
            stage.show();
        } catch(IOException exception){
            exception.printStackTrace();
        }
    }
    /*
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    } */

    public static void main(String[] args) {
        launch();
    }

}

/*
Kako bi se za definisanje elemenata koristili prosti nazivi klasa,
neophodno je prethodno izvršiti import tipa. Za razliku od standardnog Java koda,
gde nije neophodno izvršiti import tipova iz paketa java.lang, kod FXML-a je neophodno
 importovati sve tipove.

Stoga se za importovanje potrebnih tipova koriste specijalni import tagovi, kao u primeru:
1
2
3

<?import javafx.scene.layout.VBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.Button?>


Dakle, import direktive se u FXML dokumentu pišu između tagova <? i ?>.
Baš kao i u Java jeziku, i ovde je moguće importovati sve klase nekog paketa korišćenjem
karaktera zvezdica (*):
1
2

<?import javafx.scene.control.*?>
<?import java.lang.*?>

Ukoliko je pri kreiranju Java objekata poštovana JavaBean konvencija,
 setovanje propertija tih objekata se može vršiti iz FXML-a, i to na dva načina.

Prvi način podrazumeva korišćenje atributa:

<Label text="FXML is cool!"/>


Drugi način podrazumeva korišćenje property elementa:
1
2
3

<Label>
    <text>FXML is cool!</text>
</Label>
 */