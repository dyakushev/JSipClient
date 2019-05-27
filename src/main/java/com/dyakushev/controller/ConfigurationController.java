package com.dyakushev.controller;


import com.dyakushev.model.util.Utils;
import com.dyakushev.pojo.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationController {


    private ObservableList<Property> propertiesList = FXCollections.observableArrayList();

    @FXML
    private TableView<Property> propertyTableView;

    @FXML
    private TableColumn<Property, String> fieldColumn;

    @FXML
    private TableColumn<Property, String> valueColumn;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {

        readProperties();

        propertyTableView.setEditable(true);

        propertyTableView.setItems(propertiesList);
        fieldColumn.setCellValueFactory(new PropertyValueFactory<>("field"));

        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.<Property>forTableColumn());

    }


    @FXML
    private void setOnEditCommit(TableColumn.CellEditEvent<Property, String> event) {
        String newValue = event.getNewValue();
        Property property = event.getTableView().getItems().get(event.getTablePosition().getRow());
        property.setValue(newValue);
    }

    @FXML
    private void saveProperties() {
/*        OutputStream os = null;
        properties = new Properties();
        String field, value;
        try {
            os = new FileOutputStream(Utils.getConfigurationFile());
            for (Property property : propertiesList) {
                field = property.getField();
                value = property.getValue();
                properties.put(field, value);
            }
            properties.store(os, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
        try {
            Utils.writeConfigurationProperties(propertiesList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeConfiguration();
    }


    private void readProperties() {
        try {
            propertiesList = Utils.readConfigurationProperties();
        } catch (IOException e) {
        }
    }

    private void closeConfiguration() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleConfigurationCancelAction() {
        closeConfiguration();
    }

}



