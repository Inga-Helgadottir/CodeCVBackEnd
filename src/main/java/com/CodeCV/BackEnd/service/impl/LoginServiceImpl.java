package com.CodeCV.BackEnd.service.impl;

import com.CodeCV.BackEnd.dto.EmployeeLoginDTO;
import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import com.CodeCV.BackEnd.mapper.RoleMapper;
import com.CodeCV.BackEnd.mapper.ShiftMapper;
import com.CodeCV.BackEnd.repository.EmployeeRepository;
import com.CodeCV.BackEnd.security.JwtUtil;
import com.CodeCV.BackEnd.service.LoginService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final EmployeeRepository employeeRepository;
    private final ShiftMapper shiftMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;

    public LoginServiceImpl(EmployeeRepository employeeRepository, ShiftMapper shiftMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtils) {
        this.employeeRepository = employeeRepository;
        this.shiftMapper = shiftMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public EmployeeLoginDTO login(String email, String password) {

        try {
            List<Employee> employees = employeeRepository.findAll();
            Employee employee = null;

            for(Employee emp : employees){
                if(emp.getEmail().equals(email)){
                    employee = emp;
                }
            }

            if(employee != null){
                if (passwordEncoder.matches(password, employee.getPassword()) || password.equals(employee.getPassword())) {
                    String token = jwtUtils.generateToken(employee.getEmail(), employee.getRole().getRole_name());
                    return new EmployeeLoginDTO(employee.getId(), employee.getFirst_name(), employee.getLast_name(), employee.getEmail(),roleMapper.mapToRoleDto(employee.getRole()), token, shiftMapper.mapToShiftDtos(employee.getShifts()));
                } else {
                        throw new AuthenticationException("The password you have entered is incorrect");
                }
            }else{
                throw new ResourceNotFoundException("There is no employee with the email: " + email);
            }
        } catch (AuthenticationException | ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
