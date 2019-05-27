package com.dyakushev;

import com.dyakushev.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class JSipClient extends Application {


    @Override
    public void start(Stage stage) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            root = fxmlLoader.load();
            Scene mainScene = new Scene(root, 300, 500);
            MainController mainController = fxmlLoader.getController();
            mainController.setMainScene(mainScene);
            stage.setTitle("Main");
            stage.setScene(mainScene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
