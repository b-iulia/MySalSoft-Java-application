package com.example.project;

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

import static java.sql.Types.NULL;

public class ControllerEditEmp implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button newProjectButton;
    @FXML
    private Button removeButton;
    @FXML
    private Label warningAdd;
    @FXML
    private Label warningRemove;
    @FXML
    private ChoiceBox<String> idEmp;
    @FXML
    private ChoiceBox<String> idProject;
    @FXML
    private ChoiceBox<String> idProjectDelete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> IDs = new ArrayList<>();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String get_ID = "SELECT ID FROM employee";
            Statement statement = conn.createStatement();
            ResultSet queryResult = statement.executeQuery(get_ID);
            while (queryResult.next())
                IDs.add(String.valueOf(queryResult.getInt("ID")));

            conn.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        idEmp.getItems().addAll(IDs);

        List<String> IDProjects = new ArrayList<>();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String get_ID = "SELECT ProjectId FROM projects";
            Statement statement = conn.createStatement();
            ResultSet queryResult = statement.executeQuery(get_ID);
            while (queryResult.next())
                IDProjects.add(String.valueOf(queryResult.getInt("ProjectId")));

            conn.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        idProject.getItems().addAll(IDProjects);

        idEmp.setOnAction(event -> {
            if (!(idEmp.getSelectionModel().isEmpty())) {
                {
                    List<String> IDDelete = new ArrayList<>();
                    try {
                        DatabaseConnection connection = new DatabaseConnection();
                        Connection conn = connection.getDatabaseLink();
                        String get_ID = "SELECT ID, Project1, Project2, Project3 FROM employee where ID='"+idEmp.getValue()+"'";
                        Statement statement = conn.createStatement();
                        ResultSet queryResult = statement.executeQuery(get_ID);
                        while (queryResult.next())
                        {
                            if (queryResult.getInt("Project1")!=0)
                                IDDelete.add(String.valueOf(queryResult.getInt("Project1")));
                            if (queryResult.getInt("Project2")!=0)
                                IDDelete.add(String.valueOf(queryResult.getInt("Project2")));
                            if (queryResult.getInt("Project3")!=0)
                                IDDelete.add(String.valueOf(queryResult.getInt("Project3")));
                        }

                        conn.close();
                        statement.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    idProjectDelete.getItems().addAll(IDDelete);
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
    protected void newProjectButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (idEmp.getSelectionModel().isEmpty() || idProject.getSelectionModel().isEmpty())
            warningAdd.setText("Select both the employee ID and the project ID to be added.");
        else if (!idEmp.getSelectionModel().isEmpty())
        {
            int resultcheck = check_project_existence(Integer.valueOf(idProject.getValue()), Integer.valueOf(idEmp.getValue()));
            if (resultcheck==-1)
                warningAdd.setText("This employee already works on the maximum number of projects!");
            else if (resultcheck==-2)
                warningAdd.setText("This employee already works on the selected project!");
            else
            {
                addInEmp(Integer.valueOf(idProject.getValue()), resultcheck, Integer.valueOf(idEmp.getValue()));
                ControllerAdminHome.open_new_scene(newProjectButton, 900, 600, "editEmp.fxml");
            }

        }
    }
    @FXML
    protected void removeButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (idProjectDelete.getSelectionModel().isEmpty() || idEmp.getSelectionModel().isEmpty())
            warningRemove.setText("Select both the employee ID and the project ID to be removed.");
        else
        {
            DatabaseConnection con = new DatabaseConnection();
            Connection connection_link = con.getDatabaseLink();

            Statement statement = connection_link.createStatement();
            int position = check_project_existence(Integer.valueOf(idProjectDelete.getValue()), Integer.valueOf(idEmp.getValue()));
            String query="UPDATE employee SET Project1=? WHERE ID='"+idEmp.getValue()+"'";
            if (position==1)
                query = "UPDATE employee SET Project1=? WHERE ID='"+idEmp.getValue()+"'";
            else if (position==2)
                query = "UPDATE employee SET Project2=? WHERE ID='"+idEmp.getValue()+"'";
            else if (position==3)
                query = "UPDATE employee SET Project3=? WHERE ID='"+idEmp.getValue()+"'";

            PreparedStatement preparedStatement = connection_link.prepareStatement(query);
            preparedStatement.setInt(1, 0);

            preparedStatement.execute();
            connection_link.close();
            statement.close();

            ControllerAdminHome.open_new_scene(removeButton, 900, 600, "editEmp.fxml");
        }
    }
    protected int check_project_existence(int projectId, int empId) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Project1, Project2, Project3 FROM employee WHERE ID='"+empId+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
        {
            if (queryResult.getInt("Project1")!=0 && queryResult.getInt("Project2")!=0 && queryResult.getInt("Project3")!=0)
            {
                conn.close();
                statement.close();
                return -1;
            }
            else if (queryResult.getInt("Project1")==projectId || queryResult.getInt("Project2")==projectId || queryResult.getInt("Project3")==projectId)
            {
                conn.close();
                statement.close();
                return -2;
            }
            if (queryResult.getInt("Project1")==0)
            {
                conn.close();
                statement.close();
                return 1;
            }
            if (queryResult.getInt("Project2")==0)
            {
                conn.close();
                statement.close();
                return 2;
            }
            if (queryResult.getInt("Project3")==0)
            {
                conn.close();
                statement.close();
                return 3;
            }
        }
        conn.close();
        statement.close();
        return 0;
    }
    protected void addInEmp (int project_id, int position, int emp_id) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();
        String query="UPDATE employee SET Project1=? WHERE ID='"+emp_id+"'";
        if (position==1)
            query = "UPDATE employee SET Project1=? WHERE ID='"+emp_id+"'";
        else if (position==2)
            query = "UPDATE employee SET Project2=? WHERE ID='"+emp_id+"'";
        else if (position==3)
            query = "UPDATE employee SET Project3=? WHERE ID='"+emp_id+"'";

        PreparedStatement preparedStatement = connection_link.prepareStatement(query);
        preparedStatement.setInt(1, project_id);

        preparedStatement.execute();
        connection_link.close();
        statement.close();
    }
}
