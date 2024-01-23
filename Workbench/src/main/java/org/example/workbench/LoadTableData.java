package org.example.workbench;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

public class LoadTableData {
    public SimpleStringProperty tbname;
    public Button showTable;
    public Button dropTable;
    LoadTableData(String tbname, Button showTable, Button dropTable){
        this.tbname = new SimpleStringProperty(tbname);
        this.showTable = showTable;
        this.dropTable = dropTable;
    }

    public String getAlltablename(){
        return tbname.get();
    }

    public Button getShowtable(){
        return showTable;
    }

    public Button getDroptable(){
        return dropTable;
    }
}
