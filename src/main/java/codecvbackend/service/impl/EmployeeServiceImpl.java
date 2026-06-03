package codecvbackend.service.impl;

import codecvbackend.Entity.Employee;
import codecvbackend.Entity.Shift;
import codecvbackend.dto.EmployeeDTO;
import codecvbackend.dto.ShiftDTO;
import codecvbackend.exception.ResourceNotFoundException;
import codecvbackend.mapper.EmployeeMapper;
import codecvbackend.mapper.ShiftMapper;
import codecvbackend.repository.EmployeeRepository;
import codecvbackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));

        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<ShiftDTO> getAllEmployeeShifts(Long employeeId) {
        Employee employeeToCheck = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));
        List<Shift> shifts = employeeToCheck.getShifts();

        return ShiftMapper.mapToShiftDtos(shifts);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList());
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

        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Shift not found with id: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public List<Employee> getEmployeesById(List<Long> employeeIds){
        return new ArrayList<>(employeeRepository.findAllById(employeeIds));
    }

    @Override
    public List<EmployeeDTO> getEmployeesByShiftId(Long shiftId) {
        List<EmployeeDTO> allEmployees = getAllEmployees();
        List<EmployeeDTO> allEmployeesOnThatShift = new ArrayList<>();

        for(EmployeeDTO employeeDTO : allEmployees){
            for(Shift shift : employeeDTO.getShifts()){
                if(shift.getId().equals(shiftId)){
                    allEmployeesOnThatShift.add(employeeDTO);
                }
            }
        }
        return allEmployees;
    }
}