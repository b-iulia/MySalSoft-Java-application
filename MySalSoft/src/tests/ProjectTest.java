import static org.junit.Assert.*;

import com.example.project.Employee;
import com.example.project.Project;
import org.junit.*;
import java.util.HashMap;
import java.util.Map;

public class ProjectTest {
    private Project project;
    private Employee employee;
    private Map<Project, Double> project_hours;

    @Before
    public void setUp() {
        project = new Project(1, "Project 1", 100.0);
        employee = new Employee(1, "John", "Doe", "johndoe@example.com", "128445677896", 2000, "Full-time", 10, 0);
        project_hours = new HashMap<Project, Double>();
        project_hours.put(project, 100.0);
    }

    @Test
    public void testGetProjectPay() {
        employee.setProject_hours(project, 25.0);
        double expected = 2250.0;
        double result = project.getProjectPay(employee);
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testGetLastName() {
        String expected = "Project 1";
        String result = project.getLastName();
        assertEquals(expected, result);
    }

    @Test
    public void testSetLastName() {
        project.setLastName("Project 2");
        String expected = "Project 2";
        String result = project.getLastName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetProject_id() {
        int expected = 1;
        int result = project.getProject_id();
        assertEquals(expected, result);
    }

    @Test
    public void testSetProject_id() {
        project.setProject_id(2);
        int expected = 2;
        int result = project.getProject_id();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPay() {
        double expected = 100.0;
        double result = project.getPay();
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testSetPay() {
        project.setPay(2000.0);
        double expected = 2000.0;
        double result = project.getPay();
        assertEquals(expected, result, 0.1);
    }
}
