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
            String sql = "SHOW DATABASES";
            ResultSet resultSet = statement.executeQuery(sql);

            int result = 0;
            while(resultSet.next()){
                String dbname = resultSet.getString("Database");
                result++;
                System.out.println(dbname);
            }

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

    public void btn_connect(ActionEvent actionEvent) throws IOException {
        String password = tf_password.getText();
        // check password match or not
        if(checkPassword(password)){
            Parent parent =
                    FXMLLoader.load(getClass().getResource("showDatabases.fxml"));
            Scene scene = new Scene(parent);
            Stage stage =
                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Connected Successfully");
            // give a success message
        }else{
            // give a error message
        }
    }
}
