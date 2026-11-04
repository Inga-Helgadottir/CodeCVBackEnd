package com.CodeCV.BackEnd.endpoints;

import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.EmployeeLogin;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.repository.EmployeeRepository;
import com.CodeCV.BackEnd.repository.RoleRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoleEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long employeeId;
    private Long employeeRoleId;
    private String employeeToken;
    private String adminToken;
    private Role adminRole;
    private Role employeeRole;
    private final String newRole = """
        {
            "role_description": "This is a test role",
            "role_name": "New Role Test"
        }
        """;
    private String updatedRole = "";

    @BeforeEach
    void setup() throws Exception {
        //Creating roles and saving info
        employeeRole = new Role("EMPLOYEE", "An employee role gives you access to your own information and most shift information");
        adminRole = new Role("ADMIN", "Full access");
        roleRepository.save(employeeRole);
        roleRepository.save(adminRole);
        this.employeeRoleId = employeeRole.getId();

        //Creating employees and saving info
        Employee emp = new Employee("John", "Doe", "john@example.com", "passwordHere", employeeRole);
        employeeRepository.save(emp);
        this.employeeId = emp.getId();

        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", "passwordHereToo", adminRole);
        employeeRepository.save(emp2);

        //Creating employeeLogin objects for each employee
        EmployeeLogin userLoginDTO = new EmployeeLogin("john@example.com", "passwordHere");
        EmployeeLogin adminLoginDTO = new EmployeeLogin("jane@example.com", "passwordHereToo");

        //Saving tokens
        this.employeeToken = performLoginAndGetToken(mockMvc, objectMapper, userLoginDTO);
        this.adminToken = performLoginAndGetToken(mockMvc, objectMapper, adminLoginDTO);

        //Creating an updateRole object
        String id = String.valueOf(employeeRoleId);
        updatedRole = """
        {
            "id": %s,
            "role_description": "Changed the description",
            "role_name": "EMPLOYEE"
        }
        """.formatted(id);

        /*
        Test Database:
            Roles
                ("EMPLOYEE", "An employee role gets you access to see all employees and shifts");
                ("ADMIN", "An admin role gives you full access to the system");
            Employees
                ("John", "Doe", "john@example.com", "passwordHere", employeeRole);
                ("Jane", "Smith", "jane@example.com", "passwordHereToo", adminRole);
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
    void testCreateRoleSuccess() throws Exception {
        mockMvc.perform(post("/roles/create")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRole))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role_description").value("This is a test role"))
                .andExpect(jsonPath("$.role_name").value("New Role Test"));
    }

    @Test
    void testCreateRoleWrongToken() throws Exception {
        mockMvc.perform(post("/roles/create")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRole))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateRoleNoToken() throws Exception {
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRole))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllRolesSuccess() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.role_name=='ADMIN')]").exists())
                .andExpect(jsonPath("$.[?(@.role_name=='EMPLOYEE')]").exists())
                .andExpect(jsonPath("$.[?(@.role_description=='Full access')]").exists());
    }

    @Test
    void testGetAllRolesWrongToken() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllRolesNoToken() throws Exception {
        mockMvc.perform(get("/roles/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetRoleByNameSuccess() throws Exception {
        mockMvc.perform(get("/roles/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.role_name").value("ADMIN"))
                .andExpect(jsonPath("$.role_description").value("Full access"));
    }

    @Test
    void testGetRoleByNameWrongToken() throws Exception {
        mockMvc.perform(get("/roles/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetRoleByNameNoToken() throws Exception {
        mockMvc.perform(get("/roles/" + adminRole.getRole_name()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeesWithRoleSuccess() throws Exception {
        mockMvc.perform(get("/roles/employeesWithRole/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].first_name").value("Jane"))
                .andExpect(jsonPath("$[0].last_name").value("Smith"))
                .andExpect(jsonPath("$[0].email").value("jane@example.com"));
    }

    @Test
    void testGetEmployeesWithRoleWrongToken() throws Exception {
        mockMvc.perform(get("/roles/employeesWithRole/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEmployeesWithRoleNoToken() throws Exception {
        mockMvc.perform(get("/roles/employeesWithRole/" + adminRole.getRole_name()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateRoleSuccess() throws Exception {
        mockMvc.perform(put("/roles/update/" + employeeRoleId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRole))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role_description").value("Changed the description"))
                .andExpect(jsonPath("$.role_name").value("EMPLOYEE"));
    }

    @Test
    void testUpdateRoleWrongToken() throws Exception {
        mockMvc.perform(put("/roles/update/" + employeeRoleId)
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRole))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateRoleNoToken() throws Exception {
        mockMvc.perform(put("/roles/update/" + employeeRoleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRole))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteRoleSuccess() throws Exception {
        String response = "The Role " + employeeRole.getRole_name() + " has been deleted!";

        mockMvc.perform(delete("/roles/delete/" + employeeRoleId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        //Making sure it is still there
        mockMvc.perform(get("/roles/" + employeeRole.getRole_name())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRoleWrongToken() throws Exception {
        mockMvc.perform(delete("/roles/delete/" + adminRole.getId())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());

        //Making sure it is still there
        mockMvc.perform(get("/roles/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRoleNoToken() throws Exception {
        mockMvc.perform(delete("/roles/delete/" + adminRole.getId()))
                .andExpect(status().isForbidden());

        //Making sure it is still there
        mockMvc.perform(get("/roles/" + adminRole.getRole_name())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignARoleToAnEmployeeSuccess() throws Exception {
        mockMvc.perform(put("/roles/assignARoleToAnEmployee/" + employeeId + "/" + adminRole.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testAssignARoleToAnEmployeeWrongToken() throws Exception {
        mockMvc.perform(put("/roles/assignARoleToAnEmployee/" + employeeId + "/" + adminRole.getId())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAssignARoleToAnEmployeeNoToken() throws Exception {
        mockMvc.perform(put("/roles/assignARoleToAnEmployee/" + employeeId + "/" + adminRole.getId()))
                .andExpect(status().isForbidden());
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        roleRepository.deleteAll();
    }
}




