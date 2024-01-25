package org.example.workbench;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoadDataController implements Initializable {
    // insert part
    public TableView<TableDescription> inertTableview;
    public TableColumn<TableDescription, String> tb_column_fields;
    public TableColumn<TableDescription, TextField> tb_column_data;

    ObservableList<TableDescription> list = FXCollections.observableArrayList();
    ObservableList<String> dataType = FXCollections.observableArrayList();

    // load value part
    public TableView tableView;

    public String tablename, insertCmd = "";
    int id = 1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablename = ShowTablesController.tbname_store;
        tb_column_fields.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("Fields"));
        tb_column_data.setCellValueFactory(new PropertyValueFactory<TableDescription, TextField>("Data"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
            Statement statement = connection.createStatement();
            String sql = "DESC " + tablename + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            TableDescription tableDescription;

            while(resultSet.next()){
                String field = resultSet.getString("Field");
                String type = resultSet.getString("Type");
                insertCmd+=field + ",";
                TextField textField = new TextField();
                if(type.equals("varchar(55)")) {
                    dataType.add("string");
                    textField.setPromptText("VARCHAR(55)");
                }else{
                    dataType.add("int");
                    textField.setPromptText("INT");
                }
                textField.setId("tf_" + String.valueOf(id));
                id++;

                tableDescription = new TableDescription(field, textField);
                list.add(tableDescription);
                inertTableview.setItems(list);

                System.out.println(field + " " + type);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btn_insert(ActionEvent actionEvent) {
        if(list.size()==id-1) {
            insertCmd = "INSERT INTO " + tablename + "(";
            String sm = "";
            for (int a = 0; a < list.size(); a++) {
                String td = list.get(a).getFields();
                String ss = list.get(a).getData().getText();
                if (a == list.size() - 1) {
                    if(dataType.get(a).equals("string")) {
                        sm += "'" + ss + "');";
                    }else {
                        sm += ss + ");";
                    }
                    insertCmd += td + ") VALUES(" + sm;
                } else {
                    if(dataType.get(a).equals("string")) {
                        sm += "'" + ss + "',";
                    }else {
                        sm += ss + ",";
                    }
                    insertCmd += td + ",";
                }
            }

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
                Statement statement = connection.createStatement();
                statement.execute(insertCmd);
                // give success message
                connection.close();
            } catch (SQLException e) {
                // give a error message
                throw new RuntimeException(e);
            }
            System.out.println(insertCmd);
            insertCmd = "";
        }
    }

    public void btn_back(ActionEvent actionEvent) throws IOException {
        Parent parent =
                FXMLLoader.load(getClass().getResource("showTables.fxml"));
        Scene scene = new Scene(parent);
        Stage stage =
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
