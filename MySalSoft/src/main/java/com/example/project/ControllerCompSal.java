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
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCompSal implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField extraField;
    @FXML
    private TextField bonusField;
    @FXML
    private TextField daysField;
    @FXML
    private Label firstLabel;
    @FXML
    private Label lastLabel;
    @FXML
    private TextField project1Field;
    @FXML
    private TextField project2Field;
    @FXML
    private TextField project3Field;
    @FXML
    private Label warningMessageLabel;
    @FXML
    private ChoiceBox<String> id;
    @FXML
    private Button doneButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> IDs = new ArrayList<>();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String get_IDs = "SELECT ID FROM employee";
            Statement statement = conn.createStatement();
            ResultSet queryResult = statement.executeQuery(get_IDs);
            while (queryResult.next())
                IDs.add(String.valueOf(queryResult.getInt("ID")));

            conn.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        id.getItems().addAll(IDs);

        id.setOnAction(event -> {
            if (!(id.getSelectionModel().isEmpty())) {
                {
                    String[] name;
                    try {
                        name = get_name_by_id(Integer.valueOf(id.getValue()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    firstLabel.setText(name[0]);
                    lastLabel.setText(name[1]);
                }
            }
        });
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
    protected void doneButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (id.getSelectionModel().isEmpty()==true && extraField.getText().isBlank()==true && bonusField.getText().isBlank()==true && daysField.getText().isBlank()==true)
            ControllerAdminHome.open_new_scene(doneButton, 900, 600, "computePopUp.fxml");
        else if (id.getSelectionModel().isEmpty()==true || extraField.getText().isBlank()==true || bonusField.getText().isBlank()==true || daysField.getText().isBlank()==true)
            warningMessageLabel.setText("Please finish filling in before exiting. Data will be lost otherwise.");
        else if (!(id.getSelectionModel().isEmpty() || extraField.getText().isBlank() || bonusField.getText().isBlank() || daysField.getText().isBlank()))
        {
            if (check_if_exists(Integer.valueOf(id.getValue()), getCurrentMonth(), Year.now().getValue())==1)
                warningMessageLabel.setText("The data for this employee was already introduced!");
            else
            {
                eliminate_older(Integer.valueOf(id.getValue()), Year.now().getValue()-1);
                int p1, p2, p3;
                if (project1Field.getText().isBlank())
                    p1=0;
                else
                    p1=Integer.valueOf(project1Field.getText());
                if (project2Field.getText().isBlank())
                    p2=0;
                else
                    p2=Integer.valueOf(project2Field.getText());
                if (project3Field.getText().isBlank())
                    p3=0;
                else
                    p3=Integer.valueOf(project3Field.getText());
                addInEmpDetails(p1, p2, p3);
                ControllerAdminHome.open_new_scene(doneButton, 900, 600, "computePopUp.fxml");
            }
        }
    }
    @FXML
    protected void nextButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (id.getSelectionModel().isEmpty() || extraField.getText().isBlank() || bonusField.getText().isBlank() || daysField.getText().isBlank())
            warningMessageLabel.setText("All fields are required!");
        else if (check_if_exists(Integer.valueOf(id.getValue()), getCurrentMonth(), Year.now().getValue())==1)
            warningMessageLabel.setText("The data for this employee was already introduced!");
        else
        {
            if (Integer.valueOf(extraField.getText())<0 || Integer.valueOf(bonusField.getText())<0 || Integer.valueOf(daysField.getText())<0 ||  (!(project1Field.getText().isBlank()) && Integer.valueOf(project1Field.getText())<0) || (!(project2Field.getText().isBlank()) && Integer.valueOf(project2Field.getText())<0) || (!(project3Field.getText().isBlank()) && Integer.valueOf(project3Field.getText())<0))
                warningMessageLabel.setText("Fields cannot contain negative values!");
            else
            {
                eliminate_older(Integer.valueOf(id.getValue()), Year.now().getValue());
                int p1, p2, p3;
                if (project1Field.getText().isBlank())
                    p1=0;
                else
                    p1=Integer.valueOf(project1Field.getText());
                if (project2Field.getText().isBlank())
                    p2=0;
                else
                    p2=Integer.valueOf(project2Field.getText());
                if (project3Field.getText().isBlank())
                    p3=0;
                else
                    p3=Integer.valueOf(project3Field.getText());
                addInEmpDetails(p1, p2, p3);
                ControllerAdminHome.open_new_scene(nextButton, 900, 600, "computeSalaries.fxml");
            }
        }
    }

    protected void eliminate_older (int id, int year) throws SQLException {
        if (check_if_exists(id, ControllerCompSal.getCurrentMonth(), Year.now().getValue()-1)==1)
        {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String sql = "DELETE FROM salarieshistory WHERE ID='"+id+"' and Year='"+year+"' and Month='"+ControllerCompSal.getCurrentMonth()+"'";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            conn.close();
            statement.close();
        }
    }
    protected void addInEmpDetails (int p1, int p2, int p3) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();
        String query = "INSERT INTO empdetails (ID, Year, Month, ExtraHours, Bonuses, WorkedDays, Project1Hours, Project2Hours, Project3Hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection_link.prepareStatement(query);
        preparedStatement.setInt(1, Integer.valueOf(id.getValue()));
        preparedStatement.setInt(2, Year.now().getValue());
        preparedStatement.setString(3, getCurrentMonth());
        preparedStatement.setInt(4, Integer.valueOf(extraField.getText()));
        preparedStatement.setInt(5, Integer.valueOf(bonusField.getText()));
        preparedStatement.setInt(6, Integer.valueOf(daysField.getText()));
        preparedStatement.setInt(7, p1);
        preparedStatement.setInt(8, p2);
        preparedStatement.setInt(9, p3);

        preparedStatement.execute();

        connection_link.close();
        statement.close();
    }

    public static String getCurrentMonth () {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        String month = dateFormat.format(date);
        return month;
    }

    public static int check_if_exists (int id, String month, int year) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Month, Year FROM empdetails WHERE ID='"+id+"' and Month='"+month+"' and Year='"+year+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
            if (queryResult.getInt("ID")==id && queryResult.getString("Month").compareTo(month)==0)
                return 1;

        conn.close();
        statement.close();
        return 0;
    }
    protected String[] get_name_by_id (int id) throws SQLException {
        String[] name = new String[2];
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_name = "SELECT First_name, Last_name FROM employee WHERE ID='"+id+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_name);
        while (queryResult.next())
        {
            name[0] = queryResult.getString("First_name");
            name[1] = queryResult.getString("Last_name");
        }

        conn.close();
        statement.close();

        return name;
    }
}