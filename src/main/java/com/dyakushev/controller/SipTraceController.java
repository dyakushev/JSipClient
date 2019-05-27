package com.dyakushev.controller;


import com.dyakushev.event.EventOperation;
import com.dyakushev.event.EventListener;

import com.dyakushev.model.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SipTraceController implements EventListener {
    @FXML
    private TextArea sipTraceTextArea;
    @FXML
    private Button closeButton;


    public SipTraceController() {
        Utils.getEvents().subscribe(EventOperation.LOG, this);
    }

    @Override
    public void update(EventOperation eventOperation, String eventText) {
        switch (eventOperation) {
            case LOG:
                this.sipTraceTextArea.appendText(System.getProperty("line.separator"));
                this.sipTraceTextArea.appendText(eventText);
                break;
        }
    }

    @FXML
    private void handleCloseAction() {
        Utils.getEvents().unsubscribe(EventOperation.LOG, this);
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();


    }

    @FXML
    private void handleClearAction() {
        this.sipTraceTextArea.clear();
    }
}
