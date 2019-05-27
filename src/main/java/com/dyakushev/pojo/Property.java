package com.dyakushev.pojo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Property {
    private SimpleStringProperty field;
    private SimpleStringProperty value;

    public Property(String field, String value) {
        this.field = new SimpleStringProperty(field);
        this.value = new SimpleStringProperty(value);

    }

    public Property() {
    }

    public String getField() {
        return field.get();
    }

    public StringProperty fieldProperty() {
        return field;
    }

    public void setField(String field) {
        this.field.set(field);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}

