package com.CodeCV.BackEnd.service.impl;

import com.CodeCV.BackEnd.dto.CreateEmployeeDTO;
import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.ShiftDTO;
import com.CodeCV.BackEnd.dto.UpdatePasswordDTO;
import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.entity.Shift;
import com.CodeCV.BackEnd.exceptions.AuthenticationException;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import com.CodeCV.BackEnd.mapper.EmployeeMapper;
import com.CodeCV.BackEnd.mapper.ShiftMapper;
import com.CodeCV.BackEnd.repository.EmployeeRepository;
import com.CodeCV.BackEnd.repository.ShiftRepository;
import com.CodeCV.BackEnd.security.JwtUtil;
import com.CodeCV.BackEnd.service.EmployeeService;
import com.CodeCV.BackEnd.service.RoleService;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor(force = true)
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private ShiftRepository shiftRepository;
    private EmployeeMapper employeeMapper;
    private ShiftMapper shiftMapper;
    private RoleService roleService;
    private JwtUtil jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ShiftRepository shiftRepository, EmployeeMapper employeeMapper, ShiftMapper shiftMapper, RoleService roleService, JwtUtil jwtUtils, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.employeeMapper = employeeMapper;
        this.shiftMapper = shiftMapper;
        this.roleService = roleService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isAdminOrCurrentCheck (String token, String email, List<Employee> employees){
        String role = jwtUtils.getRole(token);
        String tokenEmail = jwtUtils.getEmail(token);
        boolean isAdmin =  role.equals("Admin") || role.equals("ADMIN") || role.equals("ROLE_ADMIN");
        boolean isCurrent = false;

        if(!employees.isEmpty()){
            for (Employee emp : employees) {
                if(emp.getEmail().equals(tokenEmail)){
                    isCurrent = true;
                    break;
                }
            }
        }else{
            isCurrent = email.equals(tokenEmail);
        }

        return isCurrent || isAdmin;
    }

    @Override
    public EmployeeDTO createEmployee(CreateEmployeeDTO employeeDTO) {
        Role employeeRole = roleService.getRoleByName("Employee");

        if(employeeRole == null){
            employeeRole = new Role("Employee", "Regular employee");
        }

        Employee employee = new Employee(employeeDTO.getFirst_name(), employeeDTO.getLast_name(), employeeDTO.getEmail(), employeeDTO.getPassword(), employeeRole);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if(employees.isEmpty()){
            return new ArrayList<>();
        }

        return employeeMapper.mapToEmployeeDtos(employees);
    }

    @Override
    public Employee getEmployeeByEmail(String employeeEmail) {
        List<Employee> employees = employeeRepository.findAll();
        for(Employee emp : employees){
            if(emp.getEmail().equals(employeeEmail)){
                return emp;
            }
        }
        throw new ResourceNotFoundException("There is no employee with the email: " + employeeEmail);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));

        return employeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public EmployeeDTO getYourOwnInformation(String token){
        String email = jwtUtils.getEmail(token);
        Employee employee = getEmployeeByEmail(email);
        return employeeMapper.mapToEmployeeDto(employee);
    }

    @SneakyThrows
    @Override
    public List<EmployeeDTO> getEmployeesByShiftId(Long shiftId, String token) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );
        List<Employee> employees = shift.getEmployees();

        boolean isAdminOrCurrent = isAdminOrCurrentCheck(token, "", employees);

        if(isAdminOrCurrent){
            return employeeMapper.mapToEmployeeDtos(employees);
        }else{
            throw new AuthenticationException("You do not have the proper access for this request");
        }
    }

    @SneakyThrows
    @Override
    public List<ShiftDTO> getEmployeeShiftsById(Long employeeId, String token) {
        Employee employeeToCheck = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exist with the id: " + employeeId));

        boolean isAdminOrCurrent = isAdminOrCurrentCheck(token, employeeToCheck.getEmail(), new ArrayList<>());

        List<Shift> shifts = employeeToCheck.getShifts();

        if (shifts == null || shifts.isEmpty()) {
            return new ArrayList<>();
        }

        if(isAdminOrCurrent){
            return shiftMapper.mapToShiftDtos(shifts);
        }else{
            throw new AuthenticationException("You do not have the proper access for this request");
        }
    }

    @SneakyThrows
    @Override
    public EmployeeDTO addEmployeeToShift(Long employeeId, Long shiftId, String token) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + employeeId)
        );
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(
                ()-> new ResourceNotFoundException("Shift not found with id: " + shiftId)
        );
        boolean isAdminOrCurrent = isAdminOrCurrentCheck(token, employee.getEmail(), new ArrayList<>());

        if(isAdminOrCurrent){
            employee.addShiftToEmployee(shift);
            employeeRepository.save(employee);
            return employeeMapper.mapToEmployeeDto(employee);
        }else{
            throw new AuthenticationException("You do not have the proper access for this request");
        }
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO updatedEmployee) {
        Long employeeId = updatedEmployee.getId();
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + employeeId)
        );

        employee.setFirst_name(updatedEmployee.getFirst_name());
        employee.setLast_name(updatedEmployee.getLast_name());
        employee.setEmail(updatedEmployee.getEmail());

        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return employeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @SneakyThrows
    @Override
    public String updateEmployeePassword(UpdatePasswordDTO updatedPasswordDTO, String token) {
        boolean isAdminOrCurrent = isAdminOrCurrentCheck(token, updatedPasswordDTO.getEmail(), new ArrayList<>());
        Employee employee = getEmployeeByEmail(updatedPasswordDTO.getEmail());
        if(isAdminOrCurrent){
            assert passwordEncoder != null;
            if(passwordEncoder.matches(updatedPasswordDTO.getOldPassword(), employee.getPassword()) || updatedPasswordDTO.getOldPassword().equals(employee.getPassword())){
                employee.setPassword(passwordEncoder.encode(updatedPasswordDTO.getNewPassword()));
                employeeRepository.save(employee);
                return "Password successfully changed!";
            }else{
                return "Wrong password!";
            }
        }else{
            throw new AuthenticationException("You do not have the proper access for this request");
        }
    }

    @Override
    public String deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("No Employee found with id: " + employeeId)
        );
        String employeeName = employee.getFirst_name() + " " + employee.getLast_name();
        employeeRepository.deleteById(employeeId);
        return "The Employee " + employeeName + " has been deleted!";
    }
}