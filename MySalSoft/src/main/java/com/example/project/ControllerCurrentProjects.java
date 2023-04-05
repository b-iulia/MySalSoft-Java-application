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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCurrentProjects implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button newProjectButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Project> table;
    @FXML
    private TextField nameField;
    @FXML
    private TextField payField;
    @FXML
    private Label warningAdd;
    @FXML
    private Label warningRemove;
    @FXML
    private ChoiceBox<String> id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            display_projects();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<String> IDs = new ArrayList<>();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String get_ID = "SELECT ProjectId FROM projects";
            Statement statement = conn.createStatement();
            ResultSet queryResult = statement.executeQuery(get_ID);
            while (queryResult.next())
                IDs.add(String.valueOf(queryResult.getInt("ProjectId")));

            conn.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        id.getItems().addAll(IDs);
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
    protected void newProjectButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (nameField.getText().isBlank() || payField.getText().isBlank())
            warningAdd.setText("Both fields are required!");
        else
        {
            if (Integer.valueOf(payField.getText())<0)
                warningAdd.setText("Pay field cannot be negative!");
            else
            {
                addInProjects();
                ControllerAdminHome.open_new_scene(newProjectButton, 900, 600, "currentProjects.fxml");
            }
        }
    }
    @FXML
    protected void deleteButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (id.getSelectionModel().isEmpty())
            warningRemove.setText("Please select the project id");
        else
        {
            DatabaseConnection con = new DatabaseConnection();
            Connection connection_link = con.getDatabaseLink();
            String deleteRow = "DELETE FROM projects WHERE ProjectId = ?";
            PreparedStatement statement = connection_link.prepareStatement(deleteRow);
            statement.setInt(1, Integer.valueOf(id.getValue()));
            statement.executeUpdate();

            ControllerAdminHome.open_new_scene(deleteButton, 900, 600, "currentProjects.fxml");
        }
    }
    protected void addInProjects () throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();
        String query = "INSERT INTO projects (Name, Pay) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection_link.prepareStatement(query);
        preparedStatement.setString(1, nameField.getText());
        preparedStatement.setInt(2, Integer.valueOf(payField.getText()));

        preparedStatement.execute();

        connection_link.close();
        statement.close();
    }

    public void display_projects() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM projects");

            ObservableList<Project> projects = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Project p;
                p = new Project(resultSet.getInt("ProjectId"), resultSet.getString("Name"), resultSet.getInt("Pay"));
                projects.add(p);
            }
            table.setItems(projects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
