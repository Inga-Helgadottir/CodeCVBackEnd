package codecvbackend.mapper;

import codecvbackend.Entity.Employee;
import codecvbackend.Entity.Shift;
import codecvbackend.dto.EmployeeDTO;
import codecvbackend.dto.ShiftDTO;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public static EmployeeDTO mapToEmployeeDto(Employee employee){
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getShifts()
                );
    }

    public static Employee mapToEmployee(EmployeeDTO employeeDto){
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getShifts()
        );
    }

    public static List<Employee> mapToEmployees(List<EmployeeDTO> employeeDtos) {
        List<Employee> employees = new ArrayList<>();
        for (EmployeeDTO employeeDTO : employeeDtos){
            employees.add(mapToEmployee(employeeDTO));
        }
        return employees;
    }
}
