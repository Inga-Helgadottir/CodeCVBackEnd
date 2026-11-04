package com.CodeCV.BackEnd.controller;

import com.CodeCV.BackEnd.dto.CreateEmployeeDTO;
import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.dto.UpdatePasswordDTO;
import com.CodeCV.BackEnd.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    @PostMapping("/create")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody CreateEmployeeDTO employeeDTO){
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getById/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long employeeId){
        EmployeeDTO employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/myInfo")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> getYourOwnInformation(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "");
        EmployeeDTO employee = employeeService.getYourOwnInformation(token);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/employeesOnShift/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByShiftId(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long shiftId)  {
        String token = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "");
        List<EmployeeDTO> employees = employeeService.getEmployeesByShiftId(shiftId, token);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employeeShifts/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ShiftDTO>> getEmployeeShiftsById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long employeeId)  {
        String token = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "");
        List<ShiftDTO> shiftsDTOs = employeeService.getEmployeeShiftsById(employeeId, token);
        return ResponseEntity.ok(shiftsDTOs);
    }

    @PutMapping("/addEmployeeToShift/{employeeId}/{shiftId}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> addEmployeeToShift(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("employeeId") Long employeeId, @PathVariable("shiftId") Long shiftId)  {
        String token = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "");
        EmployeeDTO employeeDTO = employeeService.addEmployeeToShift(employeeId, shiftId, token);
        return ResponseEntity.ok(employeeDTO);
    }

    @PatchMapping("/update")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO updatedEmployee){
        EmployeeDTO employeeDTO = employeeService.updateEmployee(updatedEmployee);
        return ResponseEntity.ok(employeeDTO);
    }

    @PutMapping("/updatePassword")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> updateEmployeePassword(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UpdatePasswordDTO updatedPasswordDTO)  {
        String token = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "");
        String response = employeeService.updateEmployeePassword(updatedPasswordDTO, token);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId){
        String returnMessage = employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(returnMessage);
    }
}
