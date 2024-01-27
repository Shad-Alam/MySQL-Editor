package org.example.workbench;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
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

    private ObservableList<ObservableList> data;
    private ObservableList<ObservableList<Button>> buttons;

    public String tablename, insertCmd = "", updateCmd = "";
    private String primaryKey = "-1";
    int id = 1;

    AlertMessage message;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message = new AlertMessage();
        data = FXCollections.observableArrayList();
        buttons = FXCollections.observableArrayList();
        tablename = ShowTablesController.tbname_store;
        tb_column_fields.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("Fields"));
        tb_column_data.setCellValueFactory(new PropertyValueFactory<TableDescription, TextField>("Data"));

        tb_column_fields.setText(tablename + "Fields");
        tb_column_fields.setStyle("-fx-alignment: CENTER;");
        tb_column_data.setStyle("-fx-alignment: CENTER;");

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
                if(field.equals("IDK")){
                    textField.setDisable(true);
                    textField.setPromptText("Primary Key");
                    dataType.add("int");
                }else if(type.equals("varchar(55)")) {
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

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + tablename + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            int ssd = 0;
            for(int a=0; a<resultSet.getMetaData().getColumnCount(); a++){
                int b = a;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(a + 1));
                col.setStyle("-fx-alignment: CENTER;");
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(b).toString());
                    }
                });
                tableView.getColumns().addAll(col);
            }

            while(resultSet.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int a=1; a<= resultSet.getMetaData().getColumnCount(); a++){
                    row.add(resultSet.getString(a));
                }
                data.add(row);
            }

            tableView.setItems(data);
            connection.close();
        } catch (SQLException e) {
            // error message
            message.error("MySQL Sever", "Database connection problem.");
            throw new RuntimeException(e);
        }
    }

    public void btn_insert(ActionEvent actionEvent) {
        if(list.size()==id-1) {
            insertCmd = "INSERT INTO " + tablename + "(";
            String sm = ""; boolean port = false;
            for (int a = 1; a < list.size(); a++) {
                String td = list.get(a).getFields();
                String ss = list.get(a).getData().getText();
                if(ss.length()==0){
                    port = true;
                }
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
            if(port){
                message.error("Data Insertion", "Fill up all Textfield.");
                return;
            }
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
                Statement statement = connection.createStatement();
                statement.execute(insertCmd);
                connection.close();
                // give success message
                message.succces("Table Data", "Data Inserted Successfully.");
                // refresh page
                changePage(actionEvent,"loadData.fxml");
            } catch (SQLException e) {
                // give error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            } catch (IOException e) {
                // give error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            }
            System.out.println(insertCmd);
            insertCmd = "";
        }else{
            // error message
            message.error("Table Delete", "Fill up all Textfield.");
        }
    }

    public void btn_back(ActionEvent actionEvent) throws IOException {
        changePage(actionEvent,"showTables.fxml");
    }

    // update table data
    public void btn_update(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        Object val = selectionModel.getSelectedItem();
        ObservableList<String> values = (ObservableList<String>) val;
        System.out.println(updateCmd);
        if(!primaryKey.equals("-1")) {
            try {
                updateCmd = "UPDATE " + tablename + " SET ";
                // get selected column value from dynamic table view
                for (int a = 0; a <list.size(); a++) {
                    String field = list.get(a).getFields();
                    String value = list.get(a).getData().getText();
                    String datatype = dataType.get(a);
                    if(a>0){
                        System.out.println(field + " " + datatype);
                        if(a==list.size()-1){
                            if (datatype.equals("int")) {
                                updateCmd += field + " = " + value + " ";
                            } else {
                                updateCmd += field + " = '" + value + "' ";
                            }
                        }else {
                            if (datatype.equals("int")) {
                                updateCmd += field + " = " + value + ",";
                            } else {
                                updateCmd += field + " = '" + value + "',";
                            }
                        }
                    }
                }
                updateCmd+= " WHERE IDK = " + primaryKey + ";";
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
                Statement statement = connection.createStatement();
                statement.execute(updateCmd);
                connection.close();
                // give success message
                message.succces("Table Data", "Column Data Updated Successfully.");
                // refresh page
                changePage(actionEvent, "loadData.fxml");
            } catch (SQLException e) {
                // give error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            } catch (IOException e) {
                // give error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            }
        }else{
            // error message here
            message.error("Table Update", "Choose any column and press Select Button.");
        }

        primaryKey = "-1";
        updateCmd = "";
    }

    // delete table data
    public void btn_delete(ActionEvent actionEvent) {
        if(!primaryKey.equals("-1")){
            String sql = "DELETE FROM " + tablename + " WHERE IDK = " + primaryKey + ";";
            System.out.println(sql);
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + ShowDatabasesController.databasename, "root", "shad");
                Statement statement = connection.createStatement();
                statement.execute(sql);
                connection.close();
                message.succces("Table Data", "Column Data Deletion Successfully.");
                // refresh page
                changePage(actionEvent, "loadData.fxml");
            } catch (SQLException e) {
                // error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            } catch (IOException e) {
                // error message
                message.error("MySQL Sever", "Database connection problem.");
                throw new RuntimeException(e);
            }
        }else{
            // error message
            message.error("Table Delete", "Choose any column and press Select Button.");
        }

        primaryKey = "-1";
    }

    public void btn_select(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
        if(selectionModel.isEmpty()){
            message.error("Column Select", "Select any column, if it is not empty.");
            return;
        }
        Object val = selectionModel.getSelectedItem();
        ObservableList<String> values = (ObservableList<String>) val;

        // get selected column value from dynamic table view
        for (int a = 0; a <list.size(); a++) {
            String field = list.get(a).getFields();
            String datatype = dataType.get(a);
            if(a==0){
                primaryKey = values.get(a);
            }
            list.get(a).getData().setText(values.get(a));
        }

        updateCmd+= " WHERE IDK = " + primaryKey + ";";
    }

    public void btn_clear(ActionEvent actionEvent) {
        for (int a = 0; a <list.size(); a++) {
            list.get(a).getData().setText("");
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
