package org.example.workbench;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class TableDescription {
    public SimpleStringProperty fields;
    public TextField data;
    TableDescription(String fields, TextField textField){
        this.fields = new SimpleStringProperty(fields);
        this.data = textField;
    }

    public String getFields(){
        return fields.get();
    }

    public TextField getData(){
        return data;
    }
}
