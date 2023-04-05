package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.*;

public class ControllerLogin {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label loginWarningMessage;
    @FXML
    private Button loginButton;

    public static int loggedInUser;

    @FXML
    protected void cancelButtonOnAction (ActionEvent e) {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }
    @FXML
    protected void loginButtonOnAction (ActionEvent e) throws IOException, SQLException {
        if (usernameField.getText().isBlank() || passField.getText().isBlank())
            loginWarningMessage.setText("Please fill in both fields!");
        else
        {
            int[] logged = validateLogin();
            if (logged[0]==1)
            {
                Stage stage = new Stage();
                String name;
                if (logged[1]==1)
                    name = "adminHome.fxml";
                else
                    name = "empHome.fxml";
                getLoggedInUser();

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name));
                Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();

                Stage s = (Stage) loginButton.getScene().getWindow();
                s.close();
            }
        }
    }

    public int[] validateLogin () {
        int[] logged = {0, 0, 0};
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection_link = connection.getDatabaseLink();

        String checkLogin = "SELECT Email, Password, Type FROM accounts WHERE Email='"+usernameField.getText()+"' and Password='"+passField.getText()+"'";
        try {
            Statement statement = connection_link.createStatement();
            ResultSet queryResult = statement.executeQuery(checkLogin);

            while (queryResult.next()) {
                if (queryResult.getString("Email").compareTo(usernameField.getText())==0 && queryResult.getString("Password").compareTo(passField.getText())==0)
                {
                    logged[0]=1;
                    if (queryResult.getString("Type").compareTo("admin")==0)
                        logged[1]=1;
                }
            }
            if (logged[0]==0)
                loginWarningMessage.setText("Invalid email or password");
            connection_link.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logged;
    }
    protected void getLoggedInUser() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Email FROM employee WHERE Email='"+usernameField.getText()+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
            loggedInUser=queryResult.getInt("ID");

        conn.close();
        statement.close();
    }
    public static String get_name (int id) throws SQLException {
        String name="";
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_name = "SELECT ID, First_name FROM employee WHERE ID='"+id+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_name);
        while (queryResult.next())
            name=queryResult.getString("First_name");

        conn.close();
        statement.close();
        return name;
    }
}