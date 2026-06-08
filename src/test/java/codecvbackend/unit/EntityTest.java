package codecvbackend.unit;

import codecvbackend.Entity.Employee;
import codecvbackend.Entity.Shift;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityTest {
    public List<Employee> employees;
    public Employee employee1;
    public Employee employee2;

    public List<Shift> shifts;
    public Shift shift1;
    public Shift shift2;
    public Shift shift3;

    @BeforeEach
    void setUp() {
        employees = new ArrayList<>();
        shifts = new ArrayList<>();

        LocalDate date = LocalDate.of(2026, Month.MAY, 18);
        LocalDateTime dt1Start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime dt1End = dt1Start.plusHours(3);

        LocalDateTime dt2Start = LocalDateTime.of(date, LocalTime.of(5, 10));
        LocalDateTime dt2End = dt2Start.plusHours(3);

        LocalDateTime dt3Start = LocalDateTime.of(date, LocalTime.of(10, 20));
        LocalDateTime dt3End = dt3Start.plusHours(3);

        // Create shifts
        shift1 = new Shift("building A", dt1Start, dt1End, 2L);
        shift2 = new Shift("building B", dt2Start, dt2End, 2L);
        shift3 = new Shift("building C", dt3Start, dt3End, 2L);

        // Create employees
        employee1 = new Employee("first name", "last name", "email@gmail.com");
        employee2 = new Employee("first name2", "last name2", "email2@gmail.com");

        // Assign shifts
        employee1.addShiftToEmployee(shift1);
        employee1.addShiftToEmployee(shift3);

        employee2.addShiftToEmployee(shift1);
        employee2.addShiftToEmployee(shift2);
        employee2.addShiftToEmployee(shift3);

        // Add to sets
        employees.add(employee1);
        employees.add(employee2);

        shifts.add(shift1);
        shifts.add(shift2);
        shifts.add(shift3);
    }

    @Test
    void generalVariableCheckShift(){
        //shift1("building A", 00:00, 03:00, 2L);
        //shift2("building B", 05:10, 08:10, 2L);
        //shift3("building C", 10:20, 13:20, 2L);

        assertEquals("building A", shift1.getDepartment());
        assertEquals("building B", shift2.getDepartment());
        assertEquals("building C", shift3.getDepartment());

        assertEquals(0, shift1.getStartTime().getHour());
        assertEquals(5, shift2.getStartTime().getHour());
        assertEquals(10, shift3.getStartTime().getHour());

        assertEquals(3, shift1.getEndTime().getHour());
        assertEquals(8, shift2.getEndTime().getHour());
        assertEquals(13, shift3.getEndTime().getHour());

        assertEquals(2L, shift1.getAmountOfEmployeesNeeded());
        assertEquals(2L, shift2.getAmountOfEmployeesNeeded());
        assertEquals(2L, shift3.getAmountOfEmployeesNeeded());
    }

    @Test
    void generalVariableCheckEmployee(){
        //employee1("first name", "last name", "email@gmail.com");
        //employee2("first name2", "last name2", "email2@gmail.com");

        assertEquals("first name", employee1.getFirstName());
        assertEquals("first name2", employee2.getFirstName());

        assertEquals("last name", employee1.getLastName());
        assertEquals("last name2", employee2.getLastName());

        assertEquals("email@gmail.com", employee1.getEmail());
        assertEquals("email2@gmail.com", employee2.getEmail());

        employee1.setFirstName("new first name");
        assertEquals("new first name", employee1.getFirstName());

        employee1.setLastName("new last name");
        assertEquals("new last name", employee1.getLastName());

        employee1.setEmail("newEmail@gmail.com");
        assertEquals("newEmail@gmail.com", employee1.getEmail());
    }

    @Test
    void areThereEnoughEmployeesCorrectlyFilledOut(){
        //shift1 and shift3 have enough employees
        //shift2 needs 1 more employee
        boolean expectedAreThereEnoughEmployeesForShift1 = true;
        boolean expectedAreThereEnoughEmployeesForShift2 = false;
        boolean expectedAreThereEnoughEmployeesForShift3 = true;

        boolean actualAreThereEnoughEmployeesForShift1 = shift1.areThereEnoughEmployees();
        boolean actualAreThereEnoughEmployeesForShift2 = shift2.areThereEnoughEmployees();
        boolean actualAreThereEnoughEmployeesForShift3 = shift3.areThereEnoughEmployees();

        assertEquals(expectedAreThereEnoughEmployeesForShift1, actualAreThereEnoughEmployeesForShift1);
        assertEquals(expectedAreThereEnoughEmployeesForShift2, actualAreThereEnoughEmployeesForShift2);
        assertEquals(expectedAreThereEnoughEmployeesForShift3, actualAreThereEnoughEmployeesForShift3);
    }

    @Test
    void addShiftToEmployeeIsShiftAdded() {
        //In the BeforeEach I added employees to shifts
        //employee1 is on shifts 1 and 3
        //employee2 is on shifts 1, 2 and 3

        //shift1 = building A
        //shift2 = building B
        //shift3 = building C

        boolean expectedIsEmployee1OnShifts1And3 = true;
        boolean expectedIsEmployee2OnShifts1And2And3 = true;

        List<Shift> employee1Shifts = employee1.getShifts();
        List<Shift> employee2Shifts = employee2.getShifts();

        boolean actualIsEmployee1OnShifts1And3 =
                employee1Shifts.stream().anyMatch(s -> s.getDepartment().equals("building A")) &&
                employee1Shifts.stream().anyMatch(s -> s.getDepartment().equals("building C"));

        boolean actualIsEmployee2OnShifts1And2And3 =
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building A")) &&
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building B")) &&
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building C"));

        assertEquals(expectedIsEmployee1OnShifts1And3, actualIsEmployee1OnShifts1And3);
        assertEquals(expectedIsEmployee2OnShifts1And2And3, actualIsEmployee2OnShifts1And2And3);
    }

    @Test
    void addShiftToEmployeeIsEmployeeAdded() {
        //In the BeforeEach I added employees to shifts
        //employee1 is on shifts 1 and 3
        //employee2 is on shifts 1, 2 and 3

        int expectedShift1NumberOfEmployees = 2;
        int expectedShift2NumberOfEmployees = 1;
        int expectedShift3NumberOfEmployees = 2;

        int actualShift1NumberOfEmployees = shift1.getEmployeesOnShifts().size();
        int actualShift2NumberOfEmployees = shift2.getEmployeesOnShifts().size();
        int actualShift3NumberOfEmployees = shift3.getEmployeesOnShifts().size();

        assertEquals(expectedShift1NumberOfEmployees, actualShift1NumberOfEmployees);
        assertEquals(expectedShift2NumberOfEmployees, actualShift2NumberOfEmployees);
        assertEquals(expectedShift3NumberOfEmployees, actualShift3NumberOfEmployees);
    }

    @AfterEach
    void tearDown() {
        employees.clear();
        shifts.clear();
    }
}