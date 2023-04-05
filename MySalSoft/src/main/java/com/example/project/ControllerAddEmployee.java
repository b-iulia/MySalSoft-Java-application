package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerAddEmployee implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField NINfield;
    @FXML
    private TextField jobField;
    @FXML
    private TextField salaryField;
    @FXML
    private Label warningMessageLabel;
    @FXML
    private ChoiceBox<String> typeOfEmp;
    @FXML
    private ChoiceBox<String> vouchersField;
    @FXML
    private ChoiceBox<String> deductible;
    @FXML
    private TextField dependentsField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeOfEmp.getItems().addAll("Full-time", "Part-time: 2h", "Part-time: 4h", "Part-time: 6h");

        vouchersField.getItems().addAll("0", "15", "20", "30");

        deductible.getItems().addAll("Yes", "No");
    }

    @FXML
    protected void logOutButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutButton, 520, 400, "login.fxml");
    }
    @FXML
    protected void cancelButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(cancelButton, 900, 600, "adminHome.fxml");
    }
    @FXML
    protected void addButtonOnAction (ActionEvent e) throws SQLException, IOException {
        if (firstNameField.getText().isBlank() || lastNameField.getText().isBlank() || emailField.getText().isBlank() || NINfield.getText().isBlank() || jobField.getText().isBlank() || salaryField.getText().isBlank() || typeOfEmp.getSelectionModel().isEmpty() || vouchersField.getSelectionModel().isEmpty() || deductible.getSelectionModel().isEmpty() || dependentsField.getText().isBlank())
            warningMessageLabel.setText("All fields are required!");
        else
        {
            if ((String.valueOf(NINfield.getText())).length()!=13)
                warningMessageLabel.setText("N.I.N must have 13 characters");
            else
            {
                addInEmployee();

                DatabaseConnection connection = new DatabaseConnection();
                Connection connection_link = connection.getDatabaseLink();
                String get_ID = "SELECT ID FROM employee WHERE First_name='"+firstNameField.getText()+"' and Last_name='"+lastNameField.getText()+"'";
                Statement statement = connection_link.createStatement();
                ResultSet queryResult = statement.executeQuery(get_ID);
                int id=1;
                while (queryResult.next())
                    id = queryResult.getInt("ID");

                addInAccounts(id);

                connection_link.close();
                statement.close();

                ControllerAdminHome.open_new_scene(addButton, 900, 600, "afterAddingEmp.fxml");
            }
        }
    }
    protected void addInEmployee () throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();
        String query = "INSERT INTO employee (First_name, Last_name, Email, NIN, Job, Gross_pay, Type_of_employment, Meal_vouchers, Tax_deductible, Dependents) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection_link.prepareStatement(query);
        preparedStatement.setString(1, firstNameField.getText());
        preparedStatement.setString(2, lastNameField.getText());
        preparedStatement.setString(3, emailField.getText());
        preparedStatement.setString(4, NINfield.getText());
        preparedStatement.setString(5, jobField.getText());
        preparedStatement.setDouble(6, Double.valueOf(salaryField.getText()));
        preparedStatement.setString(7, String.valueOf(typeOfEmp.getValue()));
        preparedStatement.setInt(8, Integer.valueOf(vouchersField.getValue()));
        preparedStatement.setString(9, String.valueOf(deductible.getValue()));
        preparedStatement.setInt(10, Integer.valueOf(dependentsField.getText()));

        preparedStatement.execute();

        connection_link.close();
        statement.close();
    }
    protected void addInAccounts (int id) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();

        String password = String.valueOf(id) + lastNameField.getText() + firstNameField.getText().toUpperCase();

        String query2 = "INSERT INTO accounts (ID, Email, Password) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection_link.prepareStatement(query2);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, emailField.getText());
        preparedStatement.setString(3, password);

        preparedStatement.execute();

        connection_link.close();
        statement.close();
    }
}