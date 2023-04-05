package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerEmpHome implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Label user;
    @FXML
    private Button salariesButton;

    @FXML
    protected void logOutButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutButton, 520, 400, "login.fxml");
    }
    @FXML
    protected void salariesButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(salariesButton, 900, 600, "salariesEmp.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            user.setText(ControllerLogin.get_name(ControllerLogin.loggedInUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}