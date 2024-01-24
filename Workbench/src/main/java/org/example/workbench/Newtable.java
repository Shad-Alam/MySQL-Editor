package org.example.workbench;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Newtable {
    public SimpleStringProperty tablerow, datatype;
    Newtable(String tablerow, String datatype){
        this.tablerow = new SimpleStringProperty(tablerow);
        this.datatype = new SimpleStringProperty(datatype);
    }

    public String getTablerow(){
        return tablerow.get();
    }

    public String getDatatype(){
        return datatype.get();
    }
}
