package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerComputePopUp {
    @FXML
    private Button logOutB;
    @FXML
    private Button compB;
    @FXML
    private Button cancelButton;
    @FXML
    private Button backButton;
    public List<Employee> employees = new ArrayList<>();

    @FXML
    protected void logOutBOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(logOutB, 520, 400, "login.fxml");
    }
    @FXML
    protected void compBOnAction (ActionEvent e) throws IOException, SQLException {
        compute();
        ControllerAdminHome.open_new_scene(compB, 900, 600, "currentSalaries.fxml");
    }
    @FXML
    protected void backButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(backButton, 900, 600, "adminHome.fxml");
    }
    @FXML
    protected void cancelButtonOnAction (ActionEvent e) throws IOException {
        ControllerAdminHome.open_new_scene(cancelButton, 900, 600, "computeSalaries.fxml");
    }

    protected void compute() throws SQLException {
        get_emp();
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (final Employee e : employees) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        if (check_existence(e, ControllerCompSal.getCurrentMonth(), Year.now().getValue())==0)
                        {
                            int[] details = get_details(e.getID());
                            e.setNetPay(e.compute_salary(details[0], details[1], details[2]));
                            e.setTotalVouchers(e.compute_meal_vouchers_total(details[2]));
                            eliminate_older(e, Year.now().getValue()-5);
                            add_to_history(e);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        ((ExecutorService) executor).shutdown();
    }

//    protected void compute() throws SQLException {
//        get_emp();
//        for (Employee e : employees)
//        {
//            if (check_existence(e, ControllerCompSal.getCurrentMonth(), Year.now().getValue())==0)
//            {
//                int[] details = get_details(e.getID());
//                e.setNetPay(e.compute_salary(details[0], details[1], details[2]));
//                e.setTotalVouchers(e.compute_meal_vouchers_total(details[2]));
//                eliminate_older(e, Year.now().getValue()-5);
//                add_to_history(e);
//            }
//        }
//    }
    protected int check_existence(Employee e, String month, int year) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_ID = "SELECT ID, Year, Month FROM salarieshistory WHERE ID='"+e.getID()+"' and Year='"+year+"' and Month='"+month+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_ID);
        while (queryResult.next())
            if (queryResult.getInt("ID")==e.getID() && queryResult.getInt("Year")==year && queryResult.getString("Month").compareTo(month)==0)
                return 1;

        conn.close();
        statement.close();
        return 0;
    }
    protected void eliminate_older (Employee e, int year) throws SQLException {
        if (check_existence(e, ControllerCompSal.getCurrentMonth(), Year.now().getValue()-5)==1)
        {
            DatabaseConnection connection = new DatabaseConnection();
            Connection conn = connection.getDatabaseLink();
            String sql = "DELETE FROM salarieshistory WHERE ID='"+e.getID()+"' and Year='"+year+"' and Month='"+ControllerCompSal.getCurrentMonth()+"'";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            conn.close();
            statement.close();
        }
    }
    protected void add_to_history(Employee e) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connection_link = con.getDatabaseLink();

        Statement statement = connection_link.createStatement();
        String query = "INSERT INTO salarieshistory (ID, Year, Month, NetSalary, TotalVouchers) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection_link.prepareStatement(query);
        preparedStatement.setInt(1, e.getID());
        preparedStatement.setInt(2, Year.now().getValue());
        preparedStatement.setString(3, ControllerCompSal.getCurrentMonth());
        preparedStatement.setDouble(4, e.getNetPay());
        preparedStatement.setDouble(5, e.getTotalVouchers());

        preparedStatement.execute();

        connection_link.close();
        statement.close();
    }
    protected int[] get_details(int ID) throws SQLException {
        int[] details = new int[3];
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String get_name = "SELECT ExtraHours, Bonuses, WorkedDays FROM empdetails WHERE ID='"+ID+"' AND Month='"+ControllerCompSal.getCurrentMonth()+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(get_name);
        while (queryResult.next())
        {
            details[0] = queryResult.getInt("ExtraHours");
            details[1] = queryResult.getInt("Bonuses");
            details[2] = queryResult.getInt("WorkedDays");
        }

        conn.close();
        statement.close();

        return details;
    }
    protected void get_emp() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM employee");

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

                        Project p = get_project(resultSet.getInt("Project1"));
                        e.setProjects(p);
                        e.setProject_hours(p, get_project_hours(e, "Project1Hours"));
                        p = get_project(resultSet.getInt("Project2"));
                        e.setProjects(p);
                        e.setProject_hours(p, get_project_hours(e, "Project2Hours"));
                        p = get_project(resultSet.getInt("Project3"));
                        e.setProjects(p);
                        e.setProject_hours(p, get_project_hours(e, "Project3Hours"));

                        employees.add(e);
                    }
                }
                else
                {
                    if (ControllerCompSal.check_if_exists(resultSet.getInt("ID"), ControllerCompSal.getCurrentMonth(), Year.now().getValue())==1)
                    {
                        e = new EmployeeDeductible(resultSet.getInt("ID"), resultSet.getString("First_name"), resultSet.getString("Last_name"), resultSet.getString("Email"), resultSet.getString("NIN"), resultSet.getDouble("Gross_pay"), resultSet.getString("Type_of_employment"), resultSet.getInt("Meal_vouchers"), resultSet.getInt("Dependents"));
                        employees.add(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected Project get_project(int id) throws SQLException {
        Project p = new Project(0, "", 0);
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String query = "SELECT ProjectId, Name, Pay FROM projects WHERE ProjectId='"+id+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next())
            p = new Project(queryResult.getInt("ProjectId"), queryResult.getString("Name"), queryResult.getDouble("Pay"));
        conn.close();
        statement.close();

        return p;
    }
    protected double get_project_hours (Employee e, String s) throws SQLException {
        double d=0;
        DatabaseConnection connection = new DatabaseConnection();
        Connection conn = connection.getDatabaseLink();
        String query = "SELECT ID, "+s+" FROM empdetails WHERE ID='"+e.getID()+"'";
        Statement statement = conn.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next())
            d=queryResult.getDouble(s);
        conn.close();
        statement.close();

        return d;
    }
}
