import static org.junit.Assert.*;
import com.example.project.EmployeeDeductible;
import org.junit.*;

public class EmployeeDeductibleTest {
    private EmployeeDeductible employeeDeductible;

    @Before
    public void setUp() {
        employeeDeductible = new EmployeeDeductible(1, "John", "Doe", "johndoe@example.com", "123-456-789", 2000, "Full-time", 10, 0);
    }

    @Test
    public void testGetTax() {
        double expected = 0.0;
        double result = employeeDeductible.get_tax();
        assertEquals(expected, result, 0.1);
    }
}
