package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class ControllerSalariesEmp implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView<Salary> table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            table.setItems(salList());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    protected void logOutButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutButton, 520, 400, "login.fxml");
    }
    @FXML
    protected void cancelButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(cancelButton, 900, 600, "empHome.fxml");
    }

    protected ObservableList<Salary> salList () throws SQLException {
        ObservableList<Salary> list = FXCollections.observableArrayList();
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_emp = "SELECT ID, Year, Month, NetSalary, TotalVouchers FROM salarieshistory WHERE ID='"+ControllerLogin.loggedInUser+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_emp);
        while (queryResult.next())
            if (queryResult.getInt("ID")==ControllerLogin.loggedInUser)
            {
                Salary s = new Salary(queryResult.getInt("Year"), queryResult.getString("Month"), queryResult.getDouble("NetSalary"), queryResult.getDouble("TotalVouchers"));
                list.add(s);
            }

        conn.close();
        statement.close();
        return list;
    }
}