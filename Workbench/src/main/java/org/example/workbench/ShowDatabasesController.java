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
import javafx.scene.control.*;
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
    public TextField tf_dbname;
    public static String databasename = "---";
    AlertMessage message;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message = new AlertMessage();
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
                            databasename = dbname;
                            try {
                                // goto showTables pages
                                changePage(actionEvent, "showTables.fxml");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    // add handler for btn_delete
                    btn_delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // delete database
                            try {
                                Connection connection1 = null;
                                Statement statement1 = null;
                                connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=shad");
                                statement1 = connection1.createStatement();
                                String sql1 = "DROP DATABASE " + dbname + ";";
                                statement1.execute(sql1);
                                connection1.close();
                                message.succces("Data Delete", "Database Deleted Successfully.");
                                // refresh page
                                changePage(actionEvent, "showDatabases.fxml");
                            }catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
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
            message.error("MySQL Sever", "Database connection problem.");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            message.error("MySQL Sever", "Database connection problem.");
            throw new RuntimeException(e);
        }
    }

    public void btn_create_new_schema(ActionEvent actionEvent) throws IOException {
        String dbname = tf_dbname.getText();
        if(dbname.length()>0){
            Connection connection = null;
            Statement statement = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=shad");
                statement = connection.createStatement();
                String sql = "CREATE DATABASE " + dbname;
                statement.execute(sql);
                connection.close();
                message.succces("Database Creation", "Database Created Successfully.");
                // refresh page
                changePage(actionEvent,"showDatabases.fxml");
            } catch (SQLException e) {
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            }
        }else{
            // error message
            message.error("Database Creation", "Insert Database Name Unique.");
        }
    }

    public void btn_logout(ActionEvent actionEvent) throws IOException {
        changePage(actionEvent,"userLogIn.fxml");
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
