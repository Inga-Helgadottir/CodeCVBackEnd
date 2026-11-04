package com.CodeCV.BackEnd.controller;

import com.CodeCV.BackEnd.dto.EmployeeDTO;
import com.CodeCV.BackEnd.dto.RoleDTO;
import com.CodeCV.BackEnd.entity.Role;
import com.CodeCV.BackEnd.mapper.RoleMapper;
import com.CodeCV.BackEnd.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;
    private RoleMapper roleMapper;

    @PostMapping("/create")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO){
        RoleDTO savedRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable("name") String name){
        Role roleDTO = roleService.getRoleByName(name);
        RoleDTO role = roleMapper.mapToRoleDto(roleDTO);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<RoleDTO>> getAllRoles(){
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/employeesWithRole/{roleName}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithRole(@PathVariable("roleName") String roleName){
        List<EmployeeDTO> employees = roleService.getEmployeesWithRole(roleName);
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/update/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") Long roleId, @RequestBody RoleDTO updatedRole){
        RoleDTO roleDTO = roleService.updateRole(roleId, updatedRole);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping("/delete/{id}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long roleId){
        String deleteRoleResponse = roleService.deleteRole(roleId);
        return ResponseEntity.ok(deleteRoleResponse);
    }

    @PutMapping("/assignARoleToAnEmployee/{employeeId}/{roleId}")
    @RolesAllowed("ROLE_ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<EmployeeDTO> assignARoleToAnEmployee(@PathVariable("employeeId") Long employeeId, @PathVariable("roleId") Long roleId){
        EmployeeDTO employeeDTO = roleService.assignARoleToAnEmployee(employeeId, roleId);
        return ResponseEntity.ok(employeeDTO);
    }
}
