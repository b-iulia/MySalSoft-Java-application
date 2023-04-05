package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerDisplayEmp implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button editEmp;
    @FXML
    private TableView<Employee> table;

    @FXML
    protected void logOutButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutButton, 520, 400, "login.fxml");
    }
    @FXML
    protected void cancelButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(cancelButton, 900, 600, "adminHome.fxml");
    }
    @FXML
    protected void editEmpOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(editEmp, 900, 600, "editEmp.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM employee");

            ObservableList<Employee> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int deductible;
                if (resultSet.getString("Tax_deductible").compareTo("Yes")==0)
                    deductible = 1;
                else
                    deductible = 0;

                if (deductible==0)
                    data.add(new EmployeeNotDeductible(resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents")));
                else
                    data.add(new EmployeeDeductible(resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents")));
            }
            table.setItems(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
