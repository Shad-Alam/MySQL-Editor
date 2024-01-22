package org.example.workbench;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserLogInController implements Initializable {
    public Button btn_id_connect;
    public PasswordField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private boolean checkPassword(String password){
        if(!password.equals("shad")){
            return false;
        }
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=shad");
            statement = connection.createStatement();
            int result = statement.executeUpdate("create database jee");
            if (result>0){
                connection.close();
                return true;
            }else{
                return false;
            }
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
           return false;
        }
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
