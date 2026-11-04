package com.CodeCV.BackEnd.controller;

import com.CodeCV.BackEnd.dto.EmployeeLoginDTO;
import com.CodeCV.BackEnd.entity.EmployeeLogin;
import com.CodeCV.BackEnd.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private LoginService loginService;

    @PostMapping
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeLoginDTO> login(@RequestBody EmployeeLogin login) throws Exception {
        EmployeeLoginDTO employeeLoginCheck = loginService.login(login.getEmail(), login.getPassword());
        return ResponseEntity.ok(employeeLoginCheck);
    }
}
