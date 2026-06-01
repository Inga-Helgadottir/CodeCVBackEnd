package codecvbackend.service.impl;

import codecvbackend.Entity.Employee;
import codecvbackend.Entity.Shift;
import codecvbackend.dto.EmployeeDTO;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.exception.ResourceNotFoundException;
import codecvbackend.mapper.EmployeeMapper;
import codecvbackend.repository.EmployeeRepository;
import codecvbackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDto) {
        Employee employee = employeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));

        return employeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<ShiftDTO> getAllEmployeeShifts(Long employeeId) {
        Employee employeeToCheck = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));
        Set<Shift> shifts = employeeToCheck.getShifts();
        List<ShiftDTO> shiftDTOs = getShiftDTOs(shifts);

        return shiftDTOs;
    }

    public static List<ShiftDTO> getShiftDTOs(Set<Shift> shifts){
        List<ShiftDTO> shiftDTOS = new ArrayList<>();
        shifts.forEach(shift -> shiftDTOS.add(new ShiftDTO(shift)));
        return shiftDTOS;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map((employee) -> employeeMapper.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + employeeId)
        );

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());

        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return employeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Shift not found with id: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Set<Employee> getEmployeesById(Set<Long> employeeIds){
        return new HashSet<>(employeeRepository.findAllById(employeeIds));
    }
}