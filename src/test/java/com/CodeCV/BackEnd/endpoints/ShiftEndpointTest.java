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
class ShiftEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long shiftId;
    private String employeeToken;
    private String adminToken;
    private final String newShift = """
        {
            "department":"Main building",
            "start_time":"2018-02-05T12:00:00.332",
            "end_time":"2018-02-05T12:30:00.33",
            "amount_of_employees_needed":"2"
        }
        """;
    private String updatedShift = "";

    @BeforeEach
    void setup() throws Exception {
        //Creating the start and end times for all the shifts
        LocalDateTime startTime = LocalDateTime.parse("2026-06-16T13:01:16.396935");
        LocalDateTime endTime = LocalDateTime.parse("2026-06-16T21:01:16.397934");

        //Creating shifts and saving info
        Shift shift = new Shift("HR", startTime, endTime, 2L);
        Shift shift2 = new Shift("Building A", startTime, endTime, 1L);
        Shift shift3 = new Shift("Building B", startTime, endTime, 3L);
        Shift shift4 = new Shift("Front desk", startTime, endTime, 2L);
        shiftRepository.save(shift);
        shiftRepository.save(shift2);
        shiftRepository.save(shift3);
        shiftRepository.save(shift4);
        this.shiftId = shift.getId();

        //Creating roles
        Role employeeRole = new Role("EMPLOYEE", "An employee role gives you access to your own information and most shift information");
        Role adminRole = new Role("ADMIN", "Full access");
        roleRepository.save(employeeRole);
        roleRepository.save(adminRole);

        //Creating employees and saving info
        Employee emp = new Employee("John", "Doe", "john@example.com", "passwordHere", employeeRole);
        emp.addShiftToEmployee(shift);
        emp.addShiftToEmployee(shift2);
        employeeRepository.save(emp);

        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", "passwordHereToo", adminRole);
        emp2.addShiftToEmployee(shift3);
        emp2.addShiftToEmployee(shift4);
        employeeRepository.save(emp2);

        Employee emp3 = new Employee("Nina", "Parker", "ninap@example.com", "passwordHereTooToo", employeeRole);
        emp3.addShiftToEmployee(shift4);
        employeeRepository.save(emp3);

        //Creating employeeLogin objects for each employee
        EmployeeLogin userLoginDTO = new EmployeeLogin("john@example.com", "passwordHere");
        EmployeeLogin adminLoginDTO = new EmployeeLogin("jane@example.com", "passwordHereToo");

        //Saving tokens
        this.employeeToken = performLoginAndGetToken(mockMvc, objectMapper, userLoginDTO);
        this.adminToken = performLoginAndGetToken(mockMvc, objectMapper, adminLoginDTO);

        //Saving the updated shift
        String id = String.valueOf(shiftId);
        updatedShift = """
        {
            "id": %s,
            "department": "HR",
            "start_time": "2026-07-16T13:01:16.396935",
            "end_time": "2026-07-16T21:01:16.397934",
            "amount_of_employees_needed": 1,
            "how_many_employees_are_missing": 0
        }
        """.formatted(id);

        /*
        Test Database:
            Employees
                First name, Last name, Email, Password, Role,
                Shifts

                "John", "Doe", "john@example.com", "passwordHere", Employee,
                shift("HR", startTime, endTime, 2L), shift2("Building A", startTime, endTime, 1L)

                "Jane", "Smith", "jane@example.com", "passwordHereToo", Admin,
                shift3("Building B", startTime, endTime, 3L), shift4("Front desk", startTime, endTime, 2L);

                "Nina", "Parker", "ninap@example.com", "passwordHereTooToo", Employee,
                shift4("Front desk", startTime, endTime, 2L);

        shift = Shift("HR", startTime, endTime, 2L); = 1 missing employees
        shift2 = Shift("Building A", startTime, endTime, 1L); = no missing employees
        shift3 = Shift("Building B", startTime, endTime, 3L); = missing 2 employees
        shift4 = Shift("Front desk", startTime, endTime, 2L); = no missing employees
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
    void testCreateShiftSuccess() throws Exception {
        mockMvc.perform(post("/shifts/create")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newShift))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.department").value("Main building"))
                .andExpect(jsonPath("$.start_time").value("2018-02-05T12:00:00.332"))
                .andExpect(jsonPath("$.end_time").value("2018-02-05T12:30:00.33"))
                .andExpect(jsonPath("$.amount_of_employees_needed").value(2L));
    }

    @Test
    void testCreateShiftWrongToken() throws Exception {
        mockMvc.perform(post("/shifts/create")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newShift))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateShiftNoToken() throws Exception {
        mockMvc.perform(post("/shifts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newShift))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetShiftByIdSuccess()  throws Exception {
        mockMvc.perform(get("/shifts/getById/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("HR"))
                .andExpect(jsonPath("$.start_time").value("2026-06-16T13:01:16.396935"))
                .andExpect(jsonPath("$.end_time").value("2026-06-16T21:01:16.397934"))
                .andExpect(jsonPath("$.amount_of_employees_needed").value(2L));
    }

    @Test
    void testGetShiftByIdWrongToken()  throws Exception {
        mockMvc.perform(get("/shifts/getById/" + shiftId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetShiftByIdNoToken()  throws Exception {
        mockMvc.perform(get("/shifts/getById/" + shiftId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllShiftsSuccess() throws Exception {
        mockMvc.perform(get("/shifts/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='Front desk')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='Building A')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='Building B')]").exists());

        mockMvc.perform(get("/shifts/all")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllShiftsNoToken() throws Exception {
        mockMvc.perform(get("/shifts/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllShiftsWithMissingEmployeesSuccess() throws Exception {
        mockMvc.perform(get("/shifts/missingEmployees")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.start_time=='2026-06-16T13:01:16.396935')]").exists())
                .andExpect(jsonPath("$.[?(@.end_time=='2026-06-16T21:01:16.397934')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='HR')]").exists())
                .andExpect(jsonPath("$.[?(@.department=='Building B')]").exists());

        mockMvc.perform(get("/shifts/missingEmployees")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllShiftsWithMissingEmployeesNoToken() throws Exception {
        mockMvc.perform(get("/shifts/missingEmployees"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateShiftSuccess() throws Exception {
        mockMvc.perform(put("/shifts/update/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedShift))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("HR"))
                .andExpect(jsonPath("$.start_time").value("2026-07-16T13:01:16.396935"))
                .andExpect(jsonPath("$.end_time").value("2026-07-16T21:01:16.397934"))
                .andExpect(jsonPath("$.amount_of_employees_needed").value(1));
    }

    @Test
    void testUpdateShiftWrongToken() throws Exception {
        mockMvc.perform(put("/shifts/update/" + shiftId)
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedShift))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateShiftNoToken() throws Exception {
        mockMvc.perform(put("/shifts/update/" + shiftId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedShift))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteShiftSuccess() throws Exception {
        String response = "The Shift with the id " + shiftId + " has been deleted!";
        mockMvc.perform(delete("/shifts/delete/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        mockMvc.perform(get("/shifts/getById/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteShiftWrongToken() throws Exception {
        mockMvc.perform(delete("/shifts/delete/" + shiftId)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/shifts/getById/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteShiftNoToken() throws Exception {
        mockMvc.perform(delete("/shifts/delete/" + shiftId))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/shifts/getById/" + shiftId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        shiftRepository.deleteAll();
        roleRepository.deleteAll();
    }
}



