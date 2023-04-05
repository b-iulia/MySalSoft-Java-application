import static org.junit.Assert.*;
import com.example.project.Employee;
import org.junit.*;

public class EmployeeTest {
    private Employee employee;

    @Before
    public void setUp() {
        employee = new Employee(1, "John", "Doe", "johndoe@example.com", "1234567895874", 2000, "Full-time", 10, 0);
    }

    @Test
    public void testComputeSalary() {
        double expected = 1446.875;
        double result = employee.compute_salary(10, 100, 20);
        assertEquals(expected, result, 0.1);
    }


    @Test
    public void testComputeMealVouchersTotal() {
        double expected = 90.0;
        double result = employee.compute_meal_vouchers_total(10);
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testGetTax() {
        double expected = 200.0;
        double result = employee.get_tax();
        assertEquals(expected, result, 0.1);
    }

    @Test
    public void testGetExtra() {
        double expected = 218.75;
        double result = employee.get_extra(10, 20);
        assertEquals(expected, result, 0.1);
    }
}
