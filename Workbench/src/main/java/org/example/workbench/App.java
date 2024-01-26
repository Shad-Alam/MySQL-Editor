package org.example.workbench;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("userLogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mysql Workbench");
        stage.setScene(scene);
        stage.minWidthProperty().bind(scene.heightProperty().multiply(1));
        stage.minHeightProperty().bind(scene.widthProperty().divide(2));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}