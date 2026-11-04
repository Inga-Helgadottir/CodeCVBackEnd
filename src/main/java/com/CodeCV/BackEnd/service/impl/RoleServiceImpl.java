package com.CodeCV.BackEnd.service.impl;

import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.RoleDTO;
import com.CodeCV.BackEnd.entity.Employee;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.exceptions.ResourceNotFoundException;
import com.CodeCV.BackEnd.mapper.EmployeeMapper;
import com.CodeCV.BackEnd.mapper.RoleMapper;
import com.CodeCV.BackEnd.repository.EmployeeRepository;
import com.CodeCV.BackEnd.repository.RoleRepository;
import com.CodeCV.BackEnd.service.RoleService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, EmployeeMapper employeeMapper, RoleMapper roleMapper) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.employeeMapper = employeeMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public EmployeeDTO assignARoleToAnEmployee(Long employeeId, Long roleId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + employeeId)
        );
        Role role = roleRepository.findById(roleId).orElseThrow(
                ()-> new ResourceNotFoundException("Role not found with id: " + roleId)
        );
        employee.setRole(role);
        employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public Role getRoleByName(String roleName) {
        List<Role> roles = roleRepository.findAll();

        if (!roles.isEmpty()) {
            for(Role role : roles){
                if(role.getRole_name().equalsIgnoreCase(roleName)){
                    return role;
                }
            }
        }
        throw new ResourceNotFoundException("No role found with the name: " + roleName);
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithRole(String roleName) {
        Role role = getRoleByName(roleName);
        List<Employee> employees = role.getEmployees();
        return employeeMapper.mapToEmployeeDtos(employees);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.mapToRole(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.mapToRoleDto(savedRole);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        if(roles.isEmpty()){
            return new ArrayList<>();
        }

        return roleMapper.mapToRoleDtos(roles);
    }

    @Override
    public RoleDTO updateRole(Long roleId, RoleDTO updatedRole) {

        Role role = roleRepository.findById(roleId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + roleId)
        );

        role.setRole_name(updatedRole.getRole_name());
        role.setRole_description(updatedRole.getRole_description());

        Role savedRole = roleRepository.save(role);

        return roleMapper.mapToRoleDto(savedRole);
    }

    @Override
    public String deleteRole(Long roleId) {
        Role employeeRole = getRoleByName("EMPLOYEE");
        if(employeeRole == null){
            employeeRole = new Role("Employee", "Regular employee");
        }
        Role role = roleRepository.findById(roleId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + roleId)
        );

        List<Employee> employees = role.getEmployees();
        for(Employee emp : employees){
            emp.setRole(employeeRole);
        }

        String roleName = role.getRole_name();
        roleRepository.deleteById(roleId);

        return "The Role " + roleName + " has been deleted!";
    }
}
