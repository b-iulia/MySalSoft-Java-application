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
import java.time.Year;
import java.util.*;
import static java.time.Month.of;

public class ControllerSalHistory implements Initializable {
    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label firstLabel;
    @FXML
    private Label lastLabel;
    @FXML
    private ChoiceBox<String> id;
    @FXML
    private TableView<Salary> table;

    private List<Employee> employeeList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEmployeeList();
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
                    String[] name;
                    try {
                        name = get_name_by_id(Integer.valueOf(id.getValue()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    firstLabel.setText(name[0]);
                    lastLabel.setText(name[1]);
                    table.setItems(salList(Integer.valueOf(id.getValue())));
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

    protected ObservableList<Salary> salList (int id) {
        ObservableList<Salary> list = FXCollections.observableArrayList();
        for (Employee e : employeeList)
            if (e.getID() == id)
            {
                list.addAll(e.getSalaries_history());
                return list;
            }
        return list;
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

    public void setEmployeeList() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM employee");

            while (resultSet.next()) {
                Employee e;
                if (ControllerCompSal.check_if_exists(resultSet.getInt("ID"), ControllerCompSal.getCurrentMonth(), Year.now().getValue())==1)
                {
                    e = new Employee (resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents"));
                    add_to_list(e);
                    employeeList.add(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected void add_to_list(Employee e) throws SQLException {
        for (int i = Year.now().getValue(); i>=Year.now().getValue()-5; i--)
            for (int j=12; j>=1; j--)
            {
                double[] v = get_sal(e, String.valueOf(of(j)), i);
                if (v[0]!=0)
                {
                    Salary s = new Salary(i, String.valueOf(of(j)), v[0], v[1]);
                    e.setSalaries_history(s);
                }
            }
    }
    protected double[] get_sal (Employee e, String month, int year) throws SQLException {
        double[] v = new double[2];
        v[0]=0;
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Year, Month, NetSalary, TotalVouchers FROM salarieshistory WHERE ID='"+e.getID()+"' and Year='"+year+"' and Month='"+month+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
            if (queryResult.getInt("ID")==e.getID() && queryResult.getInt("Year")==year && (queryResult.getString("Month").toLowerCase()).compareTo(month.toLowerCase())==0)
            {
                v[0] = queryResult.getDouble("NetSalary");
                v[1] = queryResult.getDouble("TotalVouchers");
                return v;
            }

        conn.close();
        statement.close();
        return v;
    }
}