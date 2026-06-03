package codecvbackend.service;

import codecvbackend.Entity.Employee;
import codecvbackend.dto.EmployeeDTO;
import codecvbackend.dto.ShiftDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDto);

    EmployeeDTO getEmployeeById(Long employeeId);

    List<EmployeeDTO> getAllEmployees();

    List<ShiftDTO> getAllEmployeeShifts(Long employeeId);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee);

    void deleteEmployee(Long employeeId);

    List<Employee> getEmployeesById(List<Long> employeeIds);
}
