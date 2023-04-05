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
import java.time.Year;
import java.util.ResourceBundle;

public class ControllerCurrentSalaries implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
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

                Employee e;
                if (deductible==0)
                {
                    if (ControllerCompSal.check_if_exists(resultSet.getInt("ID"), ControllerCompSal.getCurrentMonth(), Year.now().getValue())==1)
                    {
                        e = new EmployeeNotDeductible(resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents"));
                        double[] totals = get_totals(resultSet.getInt("ID"));
                        e.setNetPay(totals[0]);
                        e.setTotalVouchers(totals[1]);

                        data.add(e);
                    }
                }
                else
                {
                    if (ControllerCompSal.check_if_exists(resultSet.getInt("ID"), ControllerCompSal.getCurrentMonth(), Year.now().getValue())==1)
                    {
                        e = new EmployeeDeductible(resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents"));
                        double[] totals = get_totals(resultSet.getInt("ID"));
                        e.setNetPay(totals[0]);
                        e.setTotalVouchers((int) totals[1]);

                        data.add(e);
                    }
                }
            }
            table.setItems(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected double[] get_totals (int id) throws SQLException {
        double[] totals = new double[2];

        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Month, NetSalary, TotalVouchers FROM salariesHistory WHERE ID='"+id+"' and Month='"+ControllerCompSal.getCurrentMonth()+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
            if (queryResult.getInt("ID")==id && queryResult.getString("Month").compareTo(ControllerCompSal.getCurrentMonth())==0)
            {
                totals[0]=queryResult.getDouble("NetSalary");
                totals[1]=queryResult.getDouble("TotalVouchers");
            }

        conn.close();
        statement.close();
        return totals;
    }
}
