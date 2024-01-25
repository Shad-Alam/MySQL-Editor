package org.example.workbench;

import javafx.scene.control.Button;

public class HandleButton {
    Button btnmodify;
    Button btndelete;
    HandleButton(Button btnmodify, Button btndelete){
        this.btnmodify = btnmodify;
        this.btndelete = btndelete;
    }

    public Button getBtnmodify(){
        return btnmodify;
    }

    public Button getBtndelete(){
        return btndelete;
    }
}
