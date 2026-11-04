package com.CodeCV.BackEnd.service;

import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.RoleDTO;
import com.CodeCV.BackEnd.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface RoleService {

    EmployeeDTO assignARoleToAnEmployee(Long employeeId, Long roleID);

    Role getRoleByName(String roleName);

    List<EmployeeDTO> getEmployeesWithRole(String roleName);

    RoleDTO createRole(RoleDTO roleDTO);

    List<RoleDTO> getAllRoles();

    RoleDTO updateRole(Long roleId, RoleDTO updatedRole);

    String deleteRole(Long roleId);
}
