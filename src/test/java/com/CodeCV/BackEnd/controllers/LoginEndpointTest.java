package com.CodeCV.BackEnd.controllers;

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
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginEndpointTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private EmployeeLogin adminLoginDTO;
    private EmployeeLogin userLoginDTO;

    @BeforeEach
    public void setup() {
        //Creating roles
        Role employeeRole = new Role("EMPLOYEE", "A user role gets you access to see all employees and shifts");
        roleRepository.save(employeeRole);

        Role adminRole = new Role("ADMIN", "An admin role gives you full access to the system");
        roleRepository.save(adminRole);

        //Creating employees
        Employee emp = new Employee("employee", "Doe", "employee@example.com", "passwordHere", employeeRole);
        employeeRepository.save(emp);

        Employee emp2 = new Employee("admin", "Smith", "admin@example.com", "passwordHereToo", adminRole);
        employeeRepository.save(emp2);

        //Creating EmployeeLogin objects
        userLoginDTO = new EmployeeLogin(emp.getEmail(), emp.getPassword());
        adminLoginDTO = new EmployeeLogin(emp2.getEmail(), emp2.getPassword());
    }

    @Test
    public void testAdminLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.first_name").value("admin"))
                .andExpect(jsonPath("$.last_name").value("Smith"))
                .andExpect(jsonPath("$.role.role_name").value("ADMIN"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testUserLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("employee@example.com"))
                .andExpect(jsonPath("$.first_name").value("employee"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.role.role_name").value("EMPLOYEE"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        EmployeeLogin invalidLogin = new EmployeeLogin("wrongInfoHere@example.com", "wrong password here");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().is(404));
    }

    @Test
    public void testMalformedJson() throws Exception {
        String malformedJson = "{ \"email\": \"test\", \"password\": ";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().is(400));
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        roleRepository.deleteAll();
    }
}