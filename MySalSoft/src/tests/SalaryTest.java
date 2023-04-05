import static org.junit.Assert.*;
import com.example.project.Salary;
import org.junit.*;

public class SalaryTest {
    private Salary salary;

    @Before
    public void setUp() {
        salary = new Salary(2022, "January", 2000.0, 150.0);
    }

    @Test
    public void testGetYear() {
        int expected = 2022;
        int result = salary.getYear();
        assertEquals(expected, result);
    }

    @Test
    public void testSetYear() {
        salary.setYear(2021);
        int expected = 2021;
        int result = salary.getYear();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMonth() {
        String expected = "January";
        String result = salary.getMonth();
        assertEquals(expected, result);
    }

    @Test
    public void testSetMonth() {
        salary.setMonth("February");
        String expected = "February";
        String result = salary.getMonth();
        assertEquals(expected, result);
    }

    @Test
    public void testGetSalary() {
        double expected = 2000.0;
        double result = salary.getSalary();
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testSetSalary() {
        salary.setSalary(2500.0);
        double expected = 2500.0;
        double result = salary.getSalary();
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testGetM_vouchers() {
        double expected = 150.0;
        double result = salary.getM_vouchers();
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testSetM_vouchers() {
        salary.setM_vouchers(200.0);
        double expected = 200.0;
        double result = salary.getM_vouchers();
        assertEquals(expected, result, 0.1);
    }
}
