package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ControllerAfterAddingEmp {
    @FXML
    private Button logOutB;
    @FXML
    private Button addAnotherButton;
    @FXML
    private Button cancelB;

    @FXML
    protected void logOutBOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutB, 520, 400, "login.fxml");
    }
    @FXML
    protected void addAnotherButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(addAnotherButton, 900, 600, "addEmployee.fxml");
    }
    @FXML
    protected void cancelBOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(cancelB, 900, 600, "adminHome.fxml");
    }
}
