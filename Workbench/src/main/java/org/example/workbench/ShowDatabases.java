package org.example.workbench;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class ShowDatabases {
    public SimpleStringProperty dbname;
    public Button showtable, dropdatabase;
    ShowDatabases(String dbname, Button showtable, Button dropdatabase){
        this.dbname = new SimpleStringProperty(dbname);
        this.showtable = showtable;
        this.dropdatabase = dropdatabase;
    }

    public String getDbname(){
        return dbname.get();
    }

    public Button getShowtable(){
        return showtable;
    }
    public Button getDropdatabase(){
        return dropdatabase;
    }
}
