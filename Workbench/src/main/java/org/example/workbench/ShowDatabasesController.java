package org.example.workbench;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.sql.*;
import java.util.ResourceBundle;

public class ShowDatabasesController implements Initializable {
    public TableView<ShowDatabases> tableView;
    public TableColumn<ShowDatabases,String> tb_column_databases;
    public TableColumn<ShowDatabases, Button> tb_column_showTables;
    public TableColumn<ShowDatabases, Button> tb_column_dropDatabase;

    public ObservableList<ShowDatabases> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tb_column_databases.setCellValueFactory(new PropertyValueFactory<ShowDatabases,String>("Dbname"));
        tb_column_showTables.setCellValueFactory(new PropertyValueFactory<ShowDatabases,Button>("Showtable"));
        tb_column_dropDatabase.setCellValueFactory(new PropertyValueFactory<ShowDatabases,Button>("Dropdatabase"));
        tableView.setStyle("-fx-border-color: #D6DBDF; -fx-border-width: 1px 1px 1px 1px;");
        tb_column_databases.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");
        tb_column_showTables.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");
        tb_column_dropDatabase.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=shad");
            statement = connection.createStatement();
            String sql = "SHOW DATABASES";
            ResultSet resultSet = statement.executeQuery(sql);

            ShowDatabases showDatabases;
            int result = 0;
            while(resultSet.next()){
                String dbname = resultSet.getString("Database");
                Button btn_showtables = new Button("Use");
                btn_showtables.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                btn_showtables.setMaxWidth(70);

                Button btn_delete = new Button("Delete");
                btn_delete.setMaxWidth(80);
                btn_delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                // add a handler for btn_delete

                if(dbname.equals("information_schema") || dbname.equals("mysql") || dbname.equals("performance_schema") ||dbname.equals("sys")){
                    btn_showtables.setStyle("-fx-background-color: black; -fx-text-fill: white;");
                    btn_delete.setStyle("-fx-background-color: black; -fx-text-fill: white;");
                }else{
                    // add a handler for btn_showtable;
                    btn_showtables.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // pass database name
                            btn_showtables.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                        }
                    });
                    btn_delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // pass database name
                            btn_delete.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        }
                    });
                    }
                // disable some database name
                showDatabases = new ShowDatabases(dbname, btn_showtables, btn_delete);
                list.add(showDatabases);
                tableView.setItems(list);
                result++;
                System.out.println(dbname);
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btn_create_new_schema(ActionEvent actionEvent) {
    }

    public void btn_logout(ActionEvent actionEvent) throws IOException {
        Parent parent =
                FXMLLoader.load(getClass().getResource("userLogIn.fxml"));
        Scene scene = new Scene(parent);
        Stage stage =
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
