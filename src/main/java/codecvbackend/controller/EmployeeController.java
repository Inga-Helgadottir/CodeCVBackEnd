package codecvbackend.controller;

import codecvbackend.Entity.Shift;
import codecvbackend.dto.EmployeeDTO;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    // Build Add Employee REST API
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Build get employee REST API
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long employeeId){
        EmployeeDTO employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    // Build get employee shifts REST API
    @GetMapping("{id}")
    public ResponseEntity<List<ShiftDTO>> getEmployeeShiftsById(@PathVariable("id") Long employeeId){
        List<ShiftDTO> shiftsDTOs = employeeService.getAllEmployeeShifts(employeeId);
        return ResponseEntity.ok(shiftsDTOs);
    }

    // Build get all employees REST API
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Build update employee REST API
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDTO updatedEmployee){
        EmployeeDTO employeeDTO = employeeService.updateEmployee(employeeId, updatedEmployee);
        return ResponseEntity.ok(employeeDTO);
    }

    // Build delete employee REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully!");
    }
}
