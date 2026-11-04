package com.CodeCV.BackEnd.endpoints;

import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.EmployeeLogin;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.entity.Shift;
import com.CodeCV.BackEnd.repository.EmployeeRepository;
import com.CodeCV.BackEnd.repository.RoleRepository;
import com.CodeCV.BackEnd.repository.ShiftRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long employeeId;
    private Long shiftId;
    private Long shift2Id;
    private String employeeToken;
    private String employeeToken2;
    private String adminToken;
    private Employee emp;
    public String newEmployeeJson = """
        {
            "first_name": "Alice",
            "last_name": "Smith",
            "email": "alice.smith@example.com",
            "password": "password"
        }
        """;
    private final String updatePasswordDTO = """
        {
            "email":"ninap@example.com",
            "oldPassword":"passwordHereTooToo",
            "newPassword":"newPasswordHere"
        }
        """;

    private String updatedEmployeeJson = "";
    private Long id;
    private String firstName;
    private String lastName;

    @BeforeEach
    void setup() throws Exception {
        //Creating the start and end times for all the shifts
        LocalDateTime startTime = LocalDateTime.parse("2026-06-16T13:01:16.396935");
        LocalDateTime endTime = LocalDateTime.parse("2026-06-16T21:01:16.397934");

        //Creating shifts and saving info
        Shift shift = new Shift("HR", startTime, endTime, 2L);
        Shift shift2 = new Shift("Building A", startTime, endTime, 1L);
        shiftRepository.save(shift);
        shiftRepository.save(shift2);
        this.shiftId = shift.getId();
        this.shift2Id = shift2.getId();

        //Creating roles
        Role employeeRole = new Role("EMPLOYEE", "An employee role gives you access to your own information and most shift information");
        Role adminRole = new Role("ADMIN", "Full access");
        roleRepository.save(employeeRole);
        roleRepository.save(adminRole);

        //Creating employees and saving info
        emp = new Employee("John", "Doe", "john@example.com", "passwordHere", employeeRole);
        emp.addShiftToEmployee(shift);
        employeeRepository.save(emp);
        this.employeeId = emp.getId();

        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", "passwordHereToo", adminRole);
        employeeRepository.save(emp2);

        Employee emp3 = new Employee("Nina", "Parker", "ninap@example.com", "passwordHereTooToo", employeeRole);
        employeeRepository.save(emp3);

        //Creating employeeLogin objects for each employee
        EmployeeLogin userLoginDTO = new EmployeeLogin("john@example.com", "passwordHere");
        EmployeeLogin user2LoginDTO = new EmployeeLogin("ninap@example.com", "passwordHereTooToo");
        EmployeeLogin adminLoginDTO = new EmployeeLogin("jane@example.com", "passwordHereToo");

        //Saving tokens
        this.employeeToken = performLoginAndGetToken(mockMvc, objectMapper, userLoginDTO);
        this.employeeToken2 = performLoginAndGetToken(mockMvc, objectMapper, user2LoginDTO);
        this.adminToken = performLoginAndGetToken(mockMvc, objectMapper, adminLoginDTO);

        //Creating an employee update json
        id = emp.getId();
        firstName = emp.getFirst_name();
        lastName = emp.getLast_name();
        updatedEmployeeJson = """
        {
            "id":%s,
            "first_name": "%s",
            "last_name": "%s",
            "email": "changedEmail@gmail.com"
        }
        """.formatted(id, firstName, lastName);

        /*
        Test Database:
            Employees
                First name, Last name, Email, Password, Role, Shifts
                "John", "Doe", "john@example.com", "passwordHere", Employee, Shift("HR", startTime, endTime, 2L);
                "Jane", "Smith", "jane@example.com", "passwordHereToo", Admin,
                "Nina", "Parker", "ninap@example.com", "passwordHereTooToo", Employee,
        */
    }

    private String performLoginAndGetToken(MockMvc mockMvc, ObjectMapper objectMapper, EmployeeLogin loginDTO) throws Exception {
        String response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("token").asString();
    }

    @Test
    void testCreateEmployeeSuccess() throws Exception {
        mockMvc.perform(post("/employees/create")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name").value("Alice"))
                .andExpect(jsonPath("$.last_name").value("Smith"))
                .andExpect(jsonPath("$.email").value("alice.smith@example.com"));
    }

    @Test
    void testCreateEmployeeWrongToken() throws Exception {
        mockMvc.perform(post("/employees/create")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateEmployeeNoToken() throws Exception {
        mockMvc.perform(post("/employees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllEmployeesSuccess() throws Exception {
        mockMvc.perform(get("/employees/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[?(@.first_name=='John')]").exists())
                .andExpect(jsonPath("$.[?(@.last_name=='Doe')]").exists())
                .andExpect(jsonPath("$.[?(@.email=='john@example.com')]").exists())
                .andExpect(jsonPath("$.[?(@.first_name=='Jane')]").exists())
                .andExpect(jsonPath("$.[?(@.last_name=='Smith')]").exists())
                .andExpect(jsonPath("$.[?(@.email=='jane@example.com')]").exists());
    }

    @Test
    void testGetAllEmployeesWrongToken() throws Exception {
        mockMvc.perform(get("/employees/all")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllEmployeesNoToken() throws Exception {
        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeeByIdSuccess() throws Exception {
        mockMvc.perform(get("/employees/getById/" + employeeId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.role.role_name").value("EMPLOYEE"));
    }

    @Test
    void testGetEmployeeByIdWrongToken() throws Exception {
        mockMvc.perform(get("/employees/getById/" + employeeId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeeByIdNoToken() throws Exception {
        mockMvc.perform(get("/employees/getById/" + employeeId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetYourOwnInformationSuccess() throws Exception {
        mockMvc.perform(get("/employees/myInfo")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.role.role_name").value("EMPLOYEE"));
        
        mockMvc.perform(get("/employees/myInfo")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.first_name").value("Jane"))
                .andExpect(jsonPath("$.last_name").value("Smith"))
                .andExpect(jsonPath("$.role.role_name").value("ADMIN"));
    }

    @Test
    void testGetYourOwnInformationNoToken() throws Exception {
        mockMvc.perform(get("/employees/myInfo"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeesByShiftIdSuccess() throws Exception {
        mockMvc.perform(get("/employees/employeesOnShift/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].first_name").value("John"))
                .andExpect(jsonPath("$[0].last_name").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        mockMvc.perform(get("/employees/employeesOnShift/" + shiftId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].first_name").value("John"))
                .andExpect(jsonPath("$[0].last_name").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void testGetEmployeesByShiftIdWrongToken() throws Exception {
        mockMvc.perform(get("/employees/employeesOnShift/" + shiftId)
                        .header("Authorization", "Bearer " + employeeToken2))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeesByShiftIdNoToken() throws Exception {
        mockMvc.perform(get("/employees/employeesOnShift/" + shiftId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeeShiftsByIdSuccess() throws Exception {
        mockMvc.perform(get("/employees/employeeShifts/" + employeeId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists());

        mockMvc.perform(get("/employees/employeeShifts/" + employeeId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists());
    }

    @Test
    void testGetEmployeeShiftsByIdWrongToken() throws Exception {
        mockMvc.perform(get("/employees/employeeShifts/" + employeeId)
                        .header("Authorization", "Bearer " + employeeToken2))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeeShiftsByIdNoToken() throws Exception {
        mockMvc.perform(get("/employees/employeeShifts/" + employeeId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddEmployeeToShiftSuccess() throws Exception {
        mockMvc.perform(put("/employees/addEmployeeToShift/" + employeeId + "/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.shifts").isArray())
                .andExpect(jsonPath("$.shifts.length()").value(2))
                .andExpect(jsonPath("$.shifts.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.amount_of_employees_needed=='2')]").exists());

        mockMvc.perform(put("/employees/addEmployeeToShift/" + employeeId + "/" + shift2Id)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.shifts").isArray())
                .andExpect(jsonPath("$.shifts.length()").value(3))
                .andExpect(jsonPath("$.shifts.[?(@.department=='Building A')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.amount_of_employees_needed=='2')]").exists())
                .andExpect(jsonPath("$.shifts.[?(@.amount_of_employees_needed=='1')]").exists());
    }

    @Test
    void testAddEmployeeToShiftWrongToken() throws Exception {
        mockMvc.perform(put("/employees/addEmployeeToShift/" + employeeId + "/" + shift2Id)
                        .header("Authorization", "Bearer " + employeeToken2))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddEmployeeToShiftNoToken() throws Exception {
        mockMvc.perform(put("/employees/addEmployeeToShift/" + employeeId + "/" + shift2Id))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateEmployeeSuccess() throws Exception {
        mockMvc.perform(patch("/employees/update")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.first_name").value(firstName))
                .andExpect(jsonPath("$.last_name").value(lastName))
                .andExpect(jsonPath("$.email").value("changedEmail@gmail.com"));
   }

    @Test
    void testUpdateEmployeeWrongToken() throws Exception {
        mockMvc.perform(patch("/employees/update")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateEmployeeNoToken() throws Exception {
        mockMvc.perform(patch("/employees/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatePasswordSuccess() throws Exception {
        mockMvc.perform(put("/employees/updatePassword")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordDTO))
                .andExpect(status().isOk());

        mockMvc.perform(put("/employees/updatePassword")
                        .header("Authorization", "Bearer " + employeeToken2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordDTO))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePasswordWrongToken() throws Exception {
        mockMvc.perform(put("/employees/updatePassword")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordDTO))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatePasswordNoToken() throws Exception {
        mockMvc.perform(put("/employees/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordDTO))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteEmployeeSuccess() throws Exception {
        String response = "The Employee " + emp.getFirst_name() + " " + emp.getLast_name() + " has been deleted!";
        mockMvc.perform(delete("/employees/delete/" + employeeId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        mockMvc.perform(get("/employees/" + employeeId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEmployeeWrongToken() throws Exception {
        mockMvc.perform(delete("/employees/delete/" + employeeId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteEmployeeNoToken() throws Exception {
        mockMvc.perform(delete("/employees/delete/" + employeeId))
                .andExpect(status().isForbidden());
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        shiftRepository.deleteAll();
        roleRepository.deleteAll();
    }
}




