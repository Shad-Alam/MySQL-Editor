package org.example.workbench;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserLogInController implements Initializable {
    public Button btn_id_connect;
    public PasswordField tf_password;
    public static String kkisjekeickckidkdieekKIDDKEIEIKDIEKEIEI = "";

    AlertMessage message;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message = new AlertMessage();
    }

    private boolean checkPassword(String password){
        Connection connection = null;
        Statement statement = null;
        kkisjekeickckidkdieekKIDDKEIEIKDIEKEIEI = password;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password="+password);
            connection.close();
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
           return false;
        }
    }

    public void btn_connect(ActionEvent actionEvent) throws IOException {
        String password = tf_password.getText();
        // check password match or not
        if(checkPassword(password)){
            // give a success message
            message.succces("Connected to Server", "Server Connected Successfully.");
            // goto showDatabases page after, connect to the server
            changePage(actionEvent, "showDatabases.fxml");
            System.out.println("Connected Successfully");
        }else{
            // give a error message
            message.succces("Server Connection", "Invalid Password.");
        }
    }

    private void changePage(ActionEvent actionEvent, String page) throws IOException {
        Parent parent =
                FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(parent);
        Stage stage =
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
