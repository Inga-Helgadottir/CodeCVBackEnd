package com.CodeCV.BackEnd.entities;

import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.entity.Shift;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EntityTest {
    public List<Employee> employees;
    public Employee employee1;
    public Employee employee2;

    public List<Shift> shifts;
    public Shift shift1;
    public Shift shift2;
    public Shift shift3;

    public List<Role> roles;
    public Role employeeRole;
    public Role adminRole;

    @BeforeEach
    void setUp() {
        employees = new ArrayList<>();
        shifts = new ArrayList<>();
        roles = new ArrayList<>();

        LocalDate date = LocalDate.of(2026, Month.MAY, 18);
        LocalDateTime dt1Start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime dt1End = dt1Start.plusHours(3);

        LocalDateTime dt2Start = LocalDateTime.of(date, LocalTime.of(5, 10));
        LocalDateTime dt2End = dt2Start.plusHours(3);

        LocalDateTime dt3Start = LocalDateTime.of(date, LocalTime.of(10, 20));
        LocalDateTime dt3End = dt3Start.plusHours(3);

        // Create roles
        employeeRole = new Role("employee", "works here, no authority");
        adminRole = new Role("admin", "full access");

        // Create shifts
        shift1 = new Shift("building A", dt1Start, dt1End, 2L);
        shift2 = new Shift("building B", dt2Start, dt2End, 2L);
        shift3 = new Shift("building C", dt3Start, dt3End, 2L);

        // Create employees
        employee1 = new Employee("first name", "last name", "email@gmail.com", "pass", adminRole);
        employee2 = new Employee("first name2", "last name2", "email2@gmail.com", "pass2", employeeRole);

        // Assign shifts
        employee1.addShiftToEmployee(shift1);
        employee1.addShiftToEmployee(shift3);

        employee2.addShiftToEmployee(shift1);
        employee2.addShiftToEmployee(shift2);
        employee2.addShiftToEmployee(shift3);

        // Add to lists
        employees.add(employee1);
        employees.add(employee2);

        shifts.add(shift1);
        shifts.add(shift2);
        shifts.add(shift3);

        roles.add(employeeRole);
        roles.add(adminRole);
    }

    @Test
    void generalVariableCheckShift(){
        //shift1("building A", 00:00, 03:00, 2L);
        assertEquals("building A", shift1.getDepartment());
        assertEquals(0, shift1.getStart_time().getHour());
        assertEquals(3, shift1.getEnd_time().getHour());
        assertEquals(2L, shift1.getAmount_of_employees_needed());

        shift1.setDepartment("HR");
        assertEquals("HR", shift1.getDepartment());

        //shift2("building B", 05:10, 08:10, 2L);
        assertEquals("building B", shift2.getDepartment());
        assertEquals(5, shift2.getStart_time().getHour());
        assertEquals(8, shift2.getEnd_time().getHour());
        assertEquals(2L, shift2.getAmount_of_employees_needed());

        shift2.setStart_time(shift2.getStart_time().plusHours(1));
        assertEquals(6, shift2.getStart_time().getHour());

        //shift3("building C", 10:20, 13:20, 2L);
        assertEquals("building C", shift3.getDepartment());
        assertEquals(10, shift3.getStart_time().getHour());
        assertEquals(13, shift3.getEnd_time().getHour());
        assertEquals(2L, shift3.getAmount_of_employees_needed());

        shift3.setEnd_time(shift3.getEnd_time().plusHours(1));
        assertEquals(14, shift3.getEnd_time().getHour());
    }

    @Test
    void generalVariableCheckEmployee(){
        //employee1("first name", "last name", "email@gmail.com");
        assertEquals("first name", employee1.getFirst_name());
        assertEquals("last name", employee1.getLast_name());
        assertEquals("email@gmail.com", employee1.getEmail());

        //employee2("first name2", "last name2", "email2@gmail.com");
        assertEquals("first name2", employee2.getFirst_name());
        assertEquals("last name2", employee2.getLast_name());
        assertEquals("email2@gmail.com", employee2.getEmail());

        employee1.setFirst_name("new first name");
        assertEquals("new first name", employee1.getFirst_name());

        employee2.setLast_name("new last name");
        assertEquals("new last name", employee2.getLast_name());

        employee1.setEmail("newEmail@gmail.com");
        assertEquals("newEmail@gmail.com", employee1.getEmail());
    }

    @Test
    void roleCheck(){
        assertEquals(adminRole.getRole_name(), employee1.getRole().getRole_name());
        employee1.setRole(employeeRole);
        assertEquals(employeeRole.getRole_name(), employee1.getRole().getRole_name());
    }

    @Test
    void areThereEnoughEmployeesCorrectlyFilledOut(){
        //shift1 and shift3 have enough employees
        //shift2 needs 1 more employee
        boolean expectedAreThereEnoughEmployeesForShift1 = true;
        boolean actualAreThereEnoughEmployeesForShift1 = shift1.areThereEnoughEmployees();
        assertEquals(expectedAreThereEnoughEmployeesForShift1, actualAreThereEnoughEmployeesForShift1);

        boolean expectedAreThereEnoughEmployeesForShift2 = false;
        boolean actualAreThereEnoughEmployeesForShift2 = shift2.areThereEnoughEmployees();
        assertEquals(expectedAreThereEnoughEmployeesForShift2, actualAreThereEnoughEmployeesForShift2);

        boolean expectedAreThereEnoughEmployeesForShift3 = true;
        boolean actualAreThereEnoughEmployeesForShift3 = shift3.areThereEnoughEmployees();
        assertEquals(expectedAreThereEnoughEmployeesForShift3, actualAreThereEnoughEmployeesForShift3);
    }

    @Test
    void addShiftToEmployeeIsShiftAdded() {
        //employee1 is on shifts 1 and 3
        //employee2 is on shifts 1, 2 and 3

        //shift1 = building A
        //shift2 = building B
        //shift3 = building C

        boolean expectedIsEmployee1OnShifts1And3 = true;
        List<Shift> employee1Shifts = employee1.getShifts();

        boolean actualIsEmployee1OnShifts1And3 =
                employee1Shifts.stream().anyMatch(s -> s.getDepartment().equals("building A")) &&
                employee1Shifts.stream().anyMatch(s -> s.getDepartment().equals("building C"));
        assertEquals(expectedIsEmployee1OnShifts1And3, actualIsEmployee1OnShifts1And3);

        boolean expectedIsEmployee2OnShifts1And2And3 = true;
        List<Shift> employee2Shifts = employee2.getShifts();

        boolean actualIsEmployee2OnShifts1And2And3 =
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building A")) &&
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building B")) &&
                employee2Shifts.stream().anyMatch(s -> s.getDepartment().equals("building C"));
        assertEquals(expectedIsEmployee2OnShifts1And2And3, actualIsEmployee2OnShifts1And2And3);
    }

    @Test
    void addShiftToEmployeeIsEmployeeAdded() {
        //employee1 is on shifts 1 and 3
        //employee2 is on shifts 1, 2 and 3

        int expectedShift1NumberOfEmployees = 2;
        int actualShift1NumberOfEmployees = shift1.getEmployees().size();
        assertEquals(expectedShift1NumberOfEmployees, actualShift1NumberOfEmployees);

        int expectedShift2NumberOfEmployees = 1;
        int actualShift2NumberOfEmployees = shift2.getEmployees().size();
        assertEquals(expectedShift2NumberOfEmployees, actualShift2NumberOfEmployees);

        int expectedShift3NumberOfEmployees = 2;
        int actualShift3NumberOfEmployees = shift3.getEmployees().size();
        assertEquals(expectedShift3NumberOfEmployees, actualShift3NumberOfEmployees);
    }

    @AfterEach
    void tearDown() {
        employees.clear();
        shifts.clear();
    }
}