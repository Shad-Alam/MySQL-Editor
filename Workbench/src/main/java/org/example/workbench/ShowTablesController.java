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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ShowTablesController implements Initializable {
    public TableView<LoadTableData> tableView;
    public TableColumn<LoadTableData, String> tb_column_all_tables;
    public TableColumn<LoadTableData, Button> tb_column_showTableData;
    public TableColumn<LoadTableData, Button> tb_column_dropTable;

    public ObservableList<LoadTableData> list = FXCollections.observableArrayList();
    public String databasename;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databasename = ShowDatabasesController.databasename;
        tb_column_all_tables.setText("Tables_in_" + databasename);

        tb_column_all_tables.setCellValueFactory(new PropertyValueFactory<LoadTableData, String>("Alltablename"));
        tb_column_showTableData.setCellValueFactory(new PropertyValueFactory<LoadTableData, Button>("Showtable"));
        tb_column_dropTable.setCellValueFactory(new PropertyValueFactory<LoadTableData, Button>("Droptable"));

        tableView.setStyle("-fx-border-color: #D6DBDF; -fx-border-width: 1px 1px 1px 1px;");
        tb_column_all_tables.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");
        tb_column_showTableData.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");
        tb_column_dropTable.setStyle("-fx-alignment: CENTER;" +
                "-fx-border-color:  #D6DBDF; -fx-border-width: 0.5px 0.5px 0px 0.5px;");

        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databasename,"root" , "shad");
            statement = connection.createStatement();
            String sql = "SHOW TABLES;";
            ResultSet resultSet = statement.executeQuery(sql);

            LoadTableData loadTableData;
            int result = 0;
            while(resultSet.next()){
                String tablename = resultSet.getString("Tables_in_" + databasename);
                Button btn_load = new Button("Load");
                btn_load.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                btn_load.setMaxWidth(70);

                Button btn_delete = new Button("Delete");
                btn_delete.setMaxWidth(80);
                btn_delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                // add a handler for btn_delete

//                if(dbname.equals("information_schema") || dbname.equals("mysql") || dbname.equals("performance_schema") ||dbname.equals("sys")){
//                    btn_showtables.setStyle("-fx-background-color: black; -fx-text-fill: white;");
//                    btn_delete.setStyle("-fx-background-color: black; -fx-text-fill: white;");
//                }else{
                    // add a handler for btn_showtable;
                    btn_load.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // pass database name
//                            btn_showtables.setStyle("-fx-background-color: orange; -fx-text-fill: white;");

//                            databasename = dbname;
//                            try {
//                                Parent parent = FXMLLoader.load(getClass().getResource("showTables.fxml"));
//                                Scene scene = new Scene(parent);
//                                Stage stage =
//                                        (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                                stage.setScene(scene);
//
//                                ShowTablesController showTablesController = new ShowTablesController();
//                                stage.show();
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
                        }
                    });
                    // add handler for btn_delete
                    btn_delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // delete database
                            //try {
//                                Connection connection1 = null;
//                                Statement statement1 = null;
//                                connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=shad");
//                                statement1 = connection1.createStatement();
//                                String sql1 = "DROP DATABASE " + dbname + ";";
//                                statement1.execute(sql1);
//                                connection1.close();
//                                Parent parent =
//                                        FXMLLoader.load(getClass().getResource("showDatabases.fxml"));
//                                Scene scene = new Scene(parent);
//                                Stage stage =
//                                        (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                                stage.setScene(scene);
//                                stage.show();
//                            }catch (IOException e) {
//                                throw new RuntimeException(e);
//                            } catch (SQLException e) {
//                                throw new RuntimeException(e);
//                            }
                        }
                    });
//                }

                loadTableData = new LoadTableData(tablename, btn_load, btn_delete);
                list.add(loadTableData);
                tableView.setItems(list);
                result++;
                System.out.println(tablename);
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btn_create_new_table(ActionEvent actionEvent) {
    }

    public void btn_back(ActionEvent actionEvent) throws IOException {
        Parent parent =
                FXMLLoader.load(getClass().getResource("showDatabases.fxml"));
        Scene scene = new Scene(parent);
        Stage stage =
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void btn_plus(ActionEvent actionEvent) {
    }
}
