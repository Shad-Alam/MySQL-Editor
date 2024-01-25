package org.example.workbench;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadDataController implements Initializable {
    // insert part
    public TableView<TableDescription> inertTableview;
    public TableColumn<TableDescription, String> tb_column_fields;
    public TableColumn<TableDescription, TableView> tb_column_data;

    // load value part
    public TableView tableView;

    public String tablename;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablename = ShowTablesController.tbname_store;
        System.out.println(tablename);
    }

    public void btn_insert(ActionEvent actionEvent) {
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
