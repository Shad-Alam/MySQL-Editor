package org.example.workbench;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserLogInController implements Initializable {
    public Button btn_id_connect;
    public PasswordField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private boolean checkPassword(String password){
        return password.equals("shad");
    }

    public void btn_connect(ActionEvent actionEvent) {
        String password = tf_password.getText();
        // check password match or not
        if(checkPassword(password)){
            System.out.println("Connected Successfully");
            // give a success message
        }else{
            // give a error message
        }
    }
}
