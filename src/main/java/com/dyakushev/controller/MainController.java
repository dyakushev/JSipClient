package com.dyakushev.controller;

import com.dyakushev.event.EventOperation;
import com.dyakushev.event.EventListener;
import com.dyakushev.model.SipListenerImpl;

import com.dyakushev.model.util.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController implements EventListener {

    private Scene mainScene;

    private SipListenerImpl model;

    @FXML
    private Label statusLabel;

    @FXML
    private Label listeningPointLabel;

    @FXML
    private Circle circleStatus;

    @FXML
    private Button registerButton;

    @FXML
    private Button unregisterButton;

  /*  {
        Utils.getEvents().subscribe(EventOperation.STATUS, this);
        Utils.getEvents().subscribe(EventOperation.LISTENING_POINT, this);
    }*/

    @Override
    public void update(EventOperation eventOperation, String eventText) {
        switch (eventOperation) {
            case STATUS:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        statusLabel.setText(eventText);
                    }
                });

                break;
            case LISTENING_POINT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listeningPointLabel.setText(eventText);
                    }
                });
                break;
            default:
                break;


        }
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }


    public MainController() {
        Utils.getEvents().subscribe(EventOperation.STATUS, this);
        Utils.getEvents().subscribe(EventOperation.LISTENING_POINT, this);
        model = new SipListenerImpl();
        try {
            model.init();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handleCloseAction() {
        Utils.getEvents().unsubscribe(EventOperation.STATUS, this);
        if (model != null)
            model.stop();
        Platform.exit();
        System.exit(1);


    }

    @FXML
    private void handleConfigurationMenuAction() {
        Scene mainScene = this.getMainScene();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Configuration.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));//, 300, 275));
            stage.setTitle("Configuration");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainScene.getWindow());
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSipTraceMenuAction() {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/SipTrace.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));//, 300, 275));
            stage.setTitle("SIP Trace");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        this.unregisterButton.setDisable(true);
    }

    @FXML
    private void handleRegisterButtonAction() {
        this.registerButton.setDisable(true);
        this.unregisterButton.setDisable(false);
        this.circleStatus.setFill(Color.YELLOW);
        try {
            this.model.createRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handleUnRegisterButtonAction() {
        this.registerButton.setDisable(false);
        this.unregisterButton.setDisable(true);
        this.circleStatus.setFill(Color.RED);
        try {
            this.model.createRegisterCancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
