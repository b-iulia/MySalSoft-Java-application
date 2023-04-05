package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerAdminHome {
    @FXML
    private Button logOutButton;
    @FXML
    private Button addEmpButton;
    @FXML
    private Button compSalButton;
    @FXML
    private Button displayEmpButton;
    @FXML
    private Button salHistoryButton;
    @FXML
    private Button projectsButton;

    @FXML
    protected void logOutButtonOnAction (ActionEvent e) throws IOException {
        open_new_scene(logOutButton, 520, 400, "login.fxml");
    }
    @FXML
    protected void addEmpButtonOnAction (ActionEvent e) throws IOException {
        open_new_scene(addEmpButton, 900, 600, "addEmployee.fxml");
    }
    @FXML
    protected void compSalButtonOnAction (ActionEvent e) throws IOException {
        open_new_scene(compSalButton, 900, 600, "computeSalaries.fxml");
    }
    @FXML
    protected void displayEmpButtonOnAction (ActionEvent e) throws IOException {
        open_new_scene(displayEmpButton, 900, 600, "displayEmp.fxml");
    }
    @FXML
    protected void salHistoryButtonOnAction (ActionEvent e) throws IOException {
        open_new_scene(salHistoryButton, 900, 600, "salariesHistory.fxml");
    }
    @FXML
    protected void projectsOnAction (ActionEvent e) throws IOException {
        open_new_scene(projectsButton, 900, 600, "currentProjects.fxml");
    }


    public static void open_new_scene(Button button, int width, int height, String file_name) throws IOException {
        Stage s = (Stage) button.getScene().getWindow();
        s.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(file_name));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}