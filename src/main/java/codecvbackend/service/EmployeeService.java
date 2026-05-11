package codecvbackend.service;

import codecvbackend.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDto);

    EmployeeDTO getEmployeeById(Long employeeId);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee);

    void deleteEmployee(Long employeeId);
}
